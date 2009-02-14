package org.castor.cpa.persistence.sql.keygen;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;

public final class KeyGeneratorTypeHandlerBigDecimal
implements KeyGeneratorTypeHandler < BigDecimal > {
    /** Value to be returned by getValue() method if current row of the record set is not valid
     *  and the type handler should not fail in this case.  */
    private static final BigDecimal ZERO = new BigDecimal(0);

    /** Value to add to another to increment this by one. */
    private static final BigDecimal ONE = new BigDecimal(1);

    /** <code>true</code> if the type handler should fail when current row of the record set is
     *  not valid, <code>false</code> otherwise. */
    private final boolean _fail;
    
    /**
     * Construct an type handler for big decimal values.
     * 
     * @param fail <code>true</code> if the type handler should fail when current row of the
     *        record set is not valid, <code>false</code> otherwise.
     */
    public KeyGeneratorTypeHandlerBigDecimal(final boolean fail) {
        _fail = fail;
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getNextValue(final ResultSet rs) throws PersistenceException, SQLException {
        return increment(getValue(rs));
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getValue(final ResultSet rs) throws PersistenceException, SQLException {
        if (rs.next()) {
            BigDecimal value = rs.getBigDecimal(1);
            if (value == null) { value = ZERO; }
            return value;
        } else if (!_fail) {
            return ZERO;
        }

        String msg = Messages.format("persist.keyGenFailed", "");
        throw new PersistenceException(msg);
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal increment(final BigDecimal value) {
        return value.add(ONE);
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal add(final BigDecimal value, final int offset) {
        return value.add(new BigDecimal(offset));
    }

    /**
     * {@inheritDoc}
     */
    public void bindValue(final PreparedStatement stmt, final int index, final BigDecimal value)
    throws SQLException {
        stmt.setBigDecimal(index, value);
    }
}

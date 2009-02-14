package org.castor.cpa.persistence.sql.keygen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;

public final class KeyGeneratorTypeHandlerLong
implements KeyGeneratorTypeHandler < Long > {
    /** Value to be returned by getValue() method if current row of the record set is not valid
     *  and the type handler should not fail in this case.  */
    private static final Long ZERO = new Long(0);

    /** <code>true</code> if the type handler should fail when current row of the record set is
     *  not valid, <code>false</code> otherwise. */
    private final boolean _fail;
    
    /**
     * Construct an type handler for long values.
     * 
     * @param fail <code>true</code> if the type handler should fail when current row of the
     *        record set is not valid, <code>false</code> otherwise.
     */
    public KeyGeneratorTypeHandlerLong(final boolean fail) {
        _fail = fail;
    }

    /**
     * {@inheritDoc}
     */
    public Long getNextValue(final ResultSet rs) throws PersistenceException, SQLException {
        return increment(getValue(rs));
    }

    /**
     * {@inheritDoc}
     */
    public Long getValue(final ResultSet rs) throws PersistenceException, SQLException {
        if (rs.next()) {
            return new Long(rs.getLong(1));
        } else if (!_fail) {
            return ZERO;
        }

        String msg = Messages.format("persist.keyGenFailed", "");
        throw new PersistenceException(msg);
    }

    /**
     * {@inheritDoc}
     */
    public Long increment(final Long value) {
        return new Long(value.longValue() + 1);
    }

    /**
     * {@inheritDoc}
     */
    public Long add(final Long value, final int offset) {
        return new Long(value.longValue() + offset);
    }

    /**
     * {@inheritDoc}
     */
    public void bindValue(final PreparedStatement stmt, final int index, final Long value)
    throws SQLException {
        stmt.setLong(index, value.longValue());
    }
}

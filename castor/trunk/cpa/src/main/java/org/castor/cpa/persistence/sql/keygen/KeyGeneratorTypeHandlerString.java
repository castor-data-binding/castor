package org.castor.cpa.persistence.sql.keygen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;

public final class KeyGeneratorTypeHandlerString
implements KeyGeneratorTypeHandler < String > {
    /** Value to be returned by getValue() method if current row of the record set is not valid
     *  and the type handler should not fail in this case.  */
    private static String _zero;

    /** <code>true</code> if the type handler should fail when current row of the record set is
     *  not valid, <code>false</code> otherwise. */
    private final boolean _fail;
    
    /**
     * Construct an type handler for string values.
     * 
     * @param fail <code>true</code> if the type handler should fail when current row of the
     *        record set is not valid, <code>false</code> otherwise.
     * @param length Length of the string.
     */
    public KeyGeneratorTypeHandlerString(final boolean fail, final int length) {
        if (_zero == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) { sb.append('z'); }
            _zero = sb.toString();
        }
        _fail = fail;
    }

    /**
     * {@inheritDoc}
     */
    public String getNextValue(final ResultSet rs) throws PersistenceException, SQLException {
        return increment(getValue(rs));
    }

    /**
     * {@inheritDoc}
     */
    public String getValue(final ResultSet rs) throws PersistenceException, SQLException {
        if (rs.next()) {
            String value = rs.getString(1);
            if (value == null) { value = _zero; }
            return value;
        } else if (!_fail) {
            return _zero;
        }

        String msg = Messages.format("persist.keyGenFailed", "");
        throw new PersistenceException(msg);
    }

    /**
     * {@inheritDoc}
     */
    public String increment(final String value) {
        char[] array = value.toCharArray();
        boolean overflow = true;
        for (int i = array.length - 1; overflow && (i >= 0); i--) {
            array[i]++;
            overflow = (array[i] > 'z');
            if (overflow) { array[i] = 'a'; }
        }
        return new String(array);
    }

    /**
     * {@inheritDoc}
     */
    public String add(final String value, final int offset) {
        char[] array = value.toCharArray();
        for (int j = 0; j < offset; j++) {
            boolean overflow = true;
            for (int i = array.length - 1; overflow && (i >= 0); i--) {
                array[i]++;
                overflow = (array[i] > 'z');
                if (overflow) { array[i] = 'a'; }
            }
        }
        return new String(array);
    }

    /**
     * {@inheritDoc}
     */
    public void bindValue(final PreparedStatement stmt, final int index, final String value)
    throws SQLException {
        stmt.setString(index, value);
    }
}

/*
 * Copyright 2009 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.persistence.sql.keygen.typehandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;

/** 
 *  Class implementing the KeyGeneratorTypeHandler for String type.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class KeyGeneratorTypeHandlerString
implements KeyGeneratorTypeHandler <String> {
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

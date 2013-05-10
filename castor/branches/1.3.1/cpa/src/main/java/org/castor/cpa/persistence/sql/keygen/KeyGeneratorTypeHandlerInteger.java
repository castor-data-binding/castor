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
package org.castor.cpa.persistence.sql.keygen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;

/** 
 *  Class implementing the KeyGeneratorTypeHandler for Integer type.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class KeyGeneratorTypeHandlerInteger
implements KeyGeneratorTypeHandler <Integer> {
    /** Value to be returned by getValue() method if current row of the record set is not valid
     *  and the type handler should not fail in this case.  */
    private static final Integer ZERO = new Integer(0);

    /** <code>true</code> if the type handler should fail when current row of the record set is
     *  not valid, <code>false</code> otherwise. */
    private final boolean _fail;
    
    /**
     * Construct an type handler for integer values.
     * 
     * @param fail <code>true</code> if the type handler should fail when current row of the
     *        record set is not valid, <code>false</code> otherwise.
     */
    public KeyGeneratorTypeHandlerInteger(final boolean fail) {
        _fail = fail;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getNextValue(final ResultSet rs) throws PersistenceException, SQLException {
        return increment(getValue(rs));
    }

    /**
     * {@inheritDoc}
     */
    public Integer getValue(final ResultSet rs) throws PersistenceException, SQLException {
        if (rs.next()) {
            return new Integer(rs.getInt(1));
        } else if (!_fail) {
            return ZERO;
        }

        String msg = Messages.format("persist.keyGenFailed", "");
        throw new PersistenceException(msg);
    }

    /**
     * {@inheritDoc}
     */
    public Integer increment(final Integer value) {
        return new Integer(value.intValue() + 1);
    }

    /**
     * {@inheritDoc}
     */
    public Integer add(final Integer value, final int offset) {
        return new Integer(value.intValue() + offset);
    }

    /**
     * {@inheritDoc}
     */
    public void bindValue(final PreparedStatement stmt, final int index, final Integer value)
    throws SQLException {
        stmt.setInt(index, value.intValue());
    }
}

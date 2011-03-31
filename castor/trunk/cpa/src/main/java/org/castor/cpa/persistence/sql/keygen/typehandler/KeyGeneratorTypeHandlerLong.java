/*
 * Copyright 2010 Werner Guttmann
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
 *  Class implementing the KeyGeneratorTypeHandler for Long type.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class KeyGeneratorTypeHandlerLong
implements KeyGeneratorTypeHandler <Long> {
    /** Value to be returned by getValue() method if current row of the record set is not valid
     *  and the type handler should not fail in this case.  */
    private static final Long ZERO = new Long(0);

    /** <code>true</code> if the type handler should fail when current row of the record set is
     *  not valid, <code>false</code> otherwise. */
    private final boolean _fail;
    
    private Long _allocationSize;
    
    /**
     * Construct an type handler for long values.
     * 
     * @param fail <code>true</code> if the type handler should fail when current row of the
     *        record set is not valid, <code>false</code> otherwise.
     */
    public KeyGeneratorTypeHandlerLong(final boolean fail) {
        _fail = fail;
        _allocationSize = 1L;
    }

    public KeyGeneratorTypeHandlerLong(final boolean fail, final int allocationSize) {
        this(fail);
        _allocationSize = new Long(allocationSize);
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
        return new Long(value.longValue() + _allocationSize);
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

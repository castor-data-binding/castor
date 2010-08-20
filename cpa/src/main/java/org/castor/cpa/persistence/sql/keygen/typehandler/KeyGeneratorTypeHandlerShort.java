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
 * Class implementing the {@link KeyGeneratorTypeHandler} for {@link Short}
 * type.
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public final class KeyGeneratorTypeHandlerShort implements
        KeyGeneratorTypeHandler<Short> {
    
    /**
     * Value to be returned by getValue() method if current row of the record
     * set is not valid and the type handler should not fail in this case.
     */
    private static final Short ZERO = new Short((short) 0);

    /**
     * <code>true</code> if the type handler should fail when current row of the
     * record set is not valid, <code>false</code> otherwise.
     */
    private final boolean _fail;

    private Short allocationSize;

    /**
     * Construct an type handler for Short values.
     * 
     * @param fail
     *            <code>true</code> if the type handler should fail when current
     *            row of the record set is not valid, <code>false</code>
     *            otherwise.
     */
    public KeyGeneratorTypeHandlerShort(final boolean fail) {
        _fail = fail;
        this.allocationSize = 1;
    }

    public KeyGeneratorTypeHandlerShort(final boolean fail, int allocationSize) {
        this(fail);
        this.allocationSize = new Short((short) allocationSize);
    }

    /**
     * {@inheritDoc}
     */
    public Short getNextValue(final ResultSet rs) throws PersistenceException,
            SQLException {
        return increment(getValue(rs));
    }

    /**
     * {@inheritDoc}
     */
    public Short getValue(final ResultSet rs) throws PersistenceException,
            SQLException {
        if (rs.next()) {
            return new Short(rs.getShort(1));
        } else if (!_fail) {
            return ZERO;
        }

        String msg = Messages.format("persist.keyGenFailed", "");
        throw new PersistenceException(msg);
    }

    /**
     * {@inheritDoc}
     */
    public Short increment(final Short value) {
        return new Short((short) (value.shortValue() + allocationSize));
    }

    /**
     * {@inheritDoc}
     */
    public Short add(final Short value, final int offset) {
        return new Short((short) (value.shortValue() + offset));
    }

    /**
     * {@inheritDoc}
     */
    public void bindValue(final PreparedStatement stmt, final int index,
            final Short value) throws SQLException {
        stmt.setShort(index, value.shortValue());
    }
}

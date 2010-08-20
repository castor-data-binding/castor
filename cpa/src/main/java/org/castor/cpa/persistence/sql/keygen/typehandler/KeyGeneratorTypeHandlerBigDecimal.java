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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;

/** 
 *  Class implementing the KeyGeneratorTypeHandler for BigDecimal type.
 *  
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class KeyGeneratorTypeHandlerBigDecimal
implements KeyGeneratorTypeHandler <BigDecimal> {
    /** Value to be returned by getValue() method if current row of the record set is not valid
     *  and the type handler should not fail in this case.  */
    private static final BigDecimal ZERO = new BigDecimal(0);

    /** Value to add to another to increment this by one. */
    private static final BigDecimal ONE = new BigDecimal(1);

    /** <code>true</code> if the type handler should fail when current row of the record set is
     *  not valid, <code>false</code> otherwise. */
    private final boolean _fail;
    
    private BigDecimal allocationSize;
    
    /**
     * Construct an type handler for big decimal values.
     * 
     * @param fail <code>true</code> if the type handler should fail when current row of the
     *        record set is not valid, <code>false</code> otherwise.
     */
    public KeyGeneratorTypeHandlerBigDecimal(final boolean fail) {
        _fail = fail;
        this.allocationSize = ONE;
    }
    
    public KeyGeneratorTypeHandlerBigDecimal(final boolean fail, int allocationSize) {
        this(fail);
        this.allocationSize = new BigDecimal(allocationSize);
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
        return value.add(allocationSize);
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

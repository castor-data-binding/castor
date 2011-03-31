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

import java.sql.Types;

import org.exolab.castor.mapping.MappingException;

/**
 * Factory for creating {@link KeyGeneratorTypeHandler} instances.
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public final class KeyGeneratorTypeHandlerFactory {
    /**
     * Returns an {@link KeyGeneratorTypeHandler} instance.
     * 
     * @param sqlType The SQL type of the underlying identity field. 
     * @param allocationSize Allocation size.
     * @return A {@link KeyGeneratorTypeHandler} instance.
     * @throws MappingException If an invalid SQL tyope is given.
     */
    public static KeyGeneratorTypeHandler<?> getTypeHandler(final int sqlType,
            final int allocationSize) throws MappingException {
        switch (sqlType) {
        case Types.BIGINT:
            return new KeyGeneratorTypeHandlerLong(true, allocationSize);
        case Types.SMALLINT:
            return new KeyGeneratorTypeHandlerShort(true, allocationSize);
        case Types.INTEGER:
            return new KeyGeneratorTypeHandlerInteger(true, allocationSize);
        case Types.DECIMAL:
        case Types.NUMERIC:
            return new KeyGeneratorTypeHandlerBigDecimal(true, allocationSize);
        default:
            throw new MappingException("Invalid SQL type " + sqlType);
        }
    }
    
    /**
     * Hide constructor of utility class.
     */
    private KeyGeneratorTypeHandlerFactory() { }
}

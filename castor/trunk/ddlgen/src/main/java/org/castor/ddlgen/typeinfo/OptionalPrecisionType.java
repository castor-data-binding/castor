/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.typeinfo;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.schemaobject.Field;

/**
 * Final TypeInfo for types having an optional precision parameter.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class OptionalPrecisionType extends AbstractType {
    //--------------------------------------------------------------------------

    /** Default precision parameter from ddl.properties file. Will be used if no specific
     *  precision is specified at field mapping. */
    private final Integer _defaultPresision;
    
    //--------------------------------------------------------------------------

    /**
     * Construct a new TypeInfo instance with given JDBC type, SQL type and Configuration.
     * 
     * @param jdbcType The JDBC type.
     * @param sqlType The SQL type.
     * @param conf The configuration to get default parameter values from.
     */
    public OptionalPrecisionType(final String jdbcType, final String sqlType,
                                 final DDLGenConfiguration conf) {
        super(jdbcType, sqlType);

        String param = PARAM_PREFIX + jdbcType + PARAM_POSTFIX_PRECISION;
        _defaultPresision = conf.getInteger(param);
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toDDL(final Field field) {
        Integer presision = field.getPrecision();
        if (presision == null) { presision = _defaultPresision; }
        
        StringBuffer sb = new StringBuffer();
        sb.append(getSqlType());        
        if (presision != null) {
            sb.append('(').append(presision).append(')');
        }
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}

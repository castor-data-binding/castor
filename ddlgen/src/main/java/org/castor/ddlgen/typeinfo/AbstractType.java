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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.GeneratorException;

/**
 * Abstract TypeInfo with common properties of all implementations.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class AbstractType implements TypeInfo {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractType.class);
    
    /** Prefix of all parameters for types in ddl.properties file. */
    protected static final String PARAM_PREFIX = "default_";
    
    /** Postfix of length parameters for types in ddl.properties file. */
    protected static final String PARAM_POSTFIX_LENGTH = "_length";
    
    /** Postfix of precision parameters for types in ddl.properties file. */
    protected static final String PARAM_POSTFIX_PRECISION = "_precision";
    
    /** Postfix of decimals parameters for types in ddl.properties file. */
    protected static final String PARAM_POSTFIX_DECIMALS = "_decimals";
    
    //--------------------------------------------------------------------------

    /** JDBC type. */
    private final String _jdbcType;

    /** SQL type. */
    private final String _sqlType;

    //--------------------------------------------------------------------------

    /**
     * Construct a new TypeInfo instance with given Configuration, JDBC type and SQL type.
     * 
     * @param jdbcType The JDBC type.
     * @param sqlType The SQL type.
     */
    public AbstractType(final String jdbcType, final String sqlType) {
        _jdbcType = jdbcType;
        _sqlType = sqlType;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final String getJdbcType() { return _jdbcType; }
    
    /**
     * {@inheritDoc}
     */
    public final String getSqlType() { return _sqlType; }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final void merge(final TypeInfo type) throws GeneratorException {
        if (type == null) {
            LOG.error("Merge table: type is null");
            throw new GeneratorException("Merge table: type is null"); 
        }
        
        if (_jdbcType == null || _jdbcType.equalsIgnoreCase(type.getJdbcType())) {
            LOG.warn("Merge table: types have different jdbc name");
        }

        if (_sqlType == null || _sqlType.equalsIgnoreCase(type.getSqlType())) {
            LOG.warn("Merge table: types have different sql name");
        }
    }

    //--------------------------------------------------------------------------
}

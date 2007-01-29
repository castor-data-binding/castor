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
package org.castor.ddlgen.engine.mysql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.AbstractTypeMapper;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.typeinfo.NoParamType;
import org.castor.ddlgen.typeinfo.OptionalLengthType;
import org.castor.ddlgen.typeinfo.OptionalPrecisionDecimalsType;
import org.castor.ddlgen.typeinfo.OptionalPrecisionType;
import org.castor.ddlgen.typeinfo.RequiredLengthType;

/**
 * Final TypeMapper for MySQL database.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MysqlTypeMapper extends AbstractTypeMapper {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(MysqlTypeMapper.class);

    //--------------------------------------------------------------------------

    /**
     * Construct a TypeMapper for MySQL database using given configuration to 
     * get default parameters for parameterized types.
     * 
     * @param conf The configuration to get default parameter values from.
     */
    public MysqlTypeMapper(final DDLGenConfiguration conf) {
        super(conf);
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected void initialize(final DDLGenConfiguration conf) {
        // numeric types
        this.add(new NoParamType("bit", "TINYINT(1)"));
        this.add(new OptionalPrecisionType("tinyint", "TINYINT", conf));
        this.add(new OptionalPrecisionType("smallint", "SMALLINT", conf));
        this.add(new OptionalPrecisionType("integer", "INTEGER", conf));
        this.add(new OptionalPrecisionType("int", "INTEGER", conf));
        this.add(new OptionalPrecisionType("bigint", "BIGINT", conf));
        
        this.add(new OptionalPrecisionDecimalsType("float", "FLOAT", conf));
        this.add(new OptionalPrecisionDecimalsType("double", "DOUBLE", conf));
        this.add(new OptionalPrecisionDecimalsType("real", "REAL", conf));
        this.add(new OptionalPrecisionDecimalsType("numeric", "NUMERIC", conf));
        this.add(new OptionalPrecisionDecimalsType("decimal", "DECIMAL", conf));

        // character types
        this.add(new OptionalLengthType("char", "CHAR", conf));
        this.add(new OptionalLengthType("varchar", "VARCHAR", conf));
        LOG.warn("MySql does not support 'LONGVARCHAR' type, use VARCHAR instead.");
        this.add(new OptionalLengthType("longvarchar", "VARCHAR", conf));
        
        // date and time types
        this.add(new NoParamType("date", "DATE"));
        this.add(new NoParamType("time", "TIME"));
        this.add(new OptionalPrecisionType("timestamp", "TIMESTAMP", conf));
        
        // other types
        this.add(new RequiredLengthType("binary", "BINARY", conf));
        this.add(new RequiredLengthType("varbinary", "VARBINARY", conf));
        LOG.warn("MySql does not support 'LONGBINARY' type, use VARBINARY instead.");
        this.add(new RequiredLengthType("longvarbinary", "VARBINARY", conf));
        
        this.add(new NoParamType("other", "BLOB"));
        this.add(new NoParamType("javaobject", "BLOB"));
        this.add(new NoParamType("blob", "BLOB"));
        this.add(new NoParamType("clob", "TEXT"));
    }

    //--------------------------------------------------------------------------
}

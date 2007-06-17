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
package org.castor.ddlgen.engine.db2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.AbstractTypeMapper;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.typeinfo.LobType;
import org.castor.ddlgen.typeinfo.NoParamType;
import org.castor.ddlgen.typeinfo.NotSupportedType;
import org.castor.ddlgen.typeinfo.OptionalLengthType;
import org.castor.ddlgen.typeinfo.OptionalPrecisionDecimalsType;
import org.castor.ddlgen.typeinfo.RequiredLengthType;
import org.castor.ddlgen.typeinfo.RequiredPrecisionType;

/**
 * Final TypeMapper for DB2 database.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class Db2TypeMapper extends AbstractTypeMapper {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Db2TypeMapper.class);

    //--------------------------------------------------------------------------

    /**
     * Construct a TypeMapper for DB2 database using given configuration to get default
     * parameters for parameterized types.
     * 
     * @param conf The configuration to get default parameter values from.
     */
    public Db2TypeMapper(final DDLGenConfiguration conf) {
        super(conf);
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    protected void initialize(final DDLGenConfiguration conf) {
        // numeric types
        LOG.warn("Db2 does not support 'BIT' type.");
        this.add(new NotSupportedType("bit"));
        LOG.warn("Db2 does not support 'TINY' type, use SMALLINT instead.");
        this.add(new NoParamType("tinyint", "SMALLINT"));
        this.add(new NoParamType("smallint", "SMALLINT"));
        this.add(new NoParamType("integer", "INTEGER"));
        this.add(new NoParamType("int", "INTEGER"));
        this.add(new NoParamType("bigint", "BIGINT"));
        
        this.add(new RequiredPrecisionType("float", "FLOAT", conf));
        this.add(new NoParamType("double", "DOUBLE"));
        this.add(new NoParamType("real", "REAL"));
        this.add(new OptionalPrecisionDecimalsType("numeric", "NUMERIC", conf));
        this.add(new OptionalPrecisionDecimalsType("decimal", "DECIMAL", conf));

        // character types
        this.add(new OptionalLengthType("char", "CHAR", conf));
        this.add(new RequiredLengthType("varchar", "VARCHAR", conf));
        this.add(new NoParamType("longvarchar", "LONG VARCHAR"));
        
        // date and time types
        this.add(new NoParamType("date", "DATE"));
        this.add(new NoParamType("time", "TIME"));
        this.add(new NoParamType("timestamp", "TIMESTAMP"));
        
        // other types
        this.add(new OptionalLengthType("binary", "CHAR", " FOR BIT DATA", conf));
        this.add(new RequiredLengthType("varbinary", "VARCHAR", " FOR BIT DATA", conf));
        this.add(new NoParamType("longvarbinary", "LONG VARCHAR FOR BIT DATA"));
        
        this.add(new LobType("other", "BLOB", conf));
        this.add(new LobType("javaobject", "BLOB", conf));
        this.add(new LobType("blob", "BLOB", conf));
        this.add(new LobType("clob", "CLOB", conf));
    }

    //--------------------------------------------------------------------------
}

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
package org.castor.ddlgen.engine.mssql;

import org.castor.ddlgen.AbstractGenerator;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.MappingHelper;

/**
 * Generator for Microsoft SQL server.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MssqlGenerator extends AbstractGenerator {
    //--------------------------------------------------------------------------

    /** Name of database engine. */
    public static final String NAME = "mssql";
    
    /** Path to specific configuration for generator. */
    public static final String ENGINE_CONFIG_PATH =
        "org/castor/ddlgen/engine/" + NAME + "/";
    
    /** Filename of specific configuration for generator. */
    public static final String ENGINE_CONFIG_NAME = NAME + ".properties";
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for MssqlGenerator.
     * 
     * @param configuration Configuration to use by the generator.
     */
    public MssqlGenerator(final DDLGenConfiguration configuration) {
        super(configuration);
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        setMappingHelper(new MappingHelper());
        setTypeMapper(new MssqlTypeMapper(getConfiguration()));
        setSchemaFactory(new MssqlSchemaFactory());
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getEngineName() { return NAME; }
    
    /**
     * {@inheritDoc}
     */
    public String getEngineConfigPath() { return ENGINE_CONFIG_PATH; }

    /**
     * {@inheritDoc}
     */
    public String getEngineConfigName() { return ENGINE_CONFIG_NAME; }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String generateHeader() {
        String newline = getConfiguration().getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);

        StringBuffer buff = new StringBuffer("/* ");
        buff.append(newline);
        buff.append(new java.util.Date());
        buff.append(newline);

        buff.append("Castor DDL Generator from mapping for Microsoft SQL Server");
        buff.append(newline);
        buff.append(getConfiguration().getStringValue(
                DDLGenConfiguration.HEADER_COMMENT_KEY, ""));
        buff.append(newline);
        buff.append("*/");
        
        return buff.toString();
    }
    
    //--------------------------------------------------------------------------
}

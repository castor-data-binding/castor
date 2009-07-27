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
package org.castor.ddlgen;

/** 
 * Handle the configuration for DDL generator.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class DDLGenConfiguration extends Configuration {
    //--------------------------------------------------------------------------
    
    /** Default value for newline at generated DDL. */
    public static final String DEFAULT_NEWLINE = System.getProperty("line.separator");

    /** Default indention at generated DDL. */
    public static final String DEFAULT_INDENT = "    ";

    /** Default value for field delimiter at generated DDL. */
    public static final String DEFAULT_FIELD_DELIMITER = ",";

    /** Default value for statement delimiter at generated DDL. */
    public static final String DEFAULT_STATEMENT_DELIMITER = ";";

    //--------------------------------------------------------------------------
    // Global configuration keys
    
    /** Generator classes of supported database engines. */
    public static final String GENERATORS_KEY =
        "org.castor.ddlgen.Generators";

    /** Default database engine. */
    public static final String DEFAULT_ENGINE_KEY =
        "org.castor.ddlgen.DefaultEngine";

    /** Schema name. */
    public static final String SCHEMA_NAME_KEY =
        "org.castor.ddlgen.SchemaName";

    /** How to group generated DDL statements? Supported values are TABLE and DDLTYPE. */
    public static final String GROUP_DDL_KEY =
        "org.castor.ddlgen.GroupStatements";

    /** Group ddl by table. */
    public static final String GROUP_DDL_BY_TABLE = "TABLE";

    /** Group ddl by ddltype. */
    public static final String GROUP_DDL_BY_DDLTYPE = "DDLTYPE";

    /** How to format characters of generated DDL statements? Supported values are
     *  SENSITIVE, UPPER and LOWER. */
    public static final String CHAR_FORMAT_KEY =
        "org.castor.ddlgen.CharFormat";

    /** Do not format characters. */
    public static final String CHAR_FORMAT_SENSITIVE = "SENSITIVE";

    /** Convert characters to upper case. */
    public static final String CHAR_FORMAT_UPPER = "UPPER";

    /** Convert characters to lower case. */
    public static final String CHAR_FORMAT_LOWER = "LOWER";

    /** Newline. */
    public static final String NEWLINE_KEY =
        "org.castor.ddlgen.Newline";

    /** Indention. */
    public static final String INDENT_KEY =
        "org.castor.ddlgen.Indention";
    
    /** Generate DDL for CREATE statement. */
    public static final String GENERATE_DDL_FOR_SCHEMA_KEY =
        "org.castor.ddlgen.GenerateSchema";

    /** Generate DDL for DROP statement. */
    public static final String GENERATE_DDL_FOR_DROP_KEY =
        "org.castor.ddlgen.GenerateDrop";

    /** Generate DDL for CREATE statement. */
    public static final String GENERATE_DDL_FOR_CREATE_KEY =
        "org.castor.ddlgen.GenerateCreate";

    /** Generate DDL for PRIMARY KEY statement. */
    public static final String GENERATE_DDL_FOR_PRIMARYKEY_KEY = 
        "org.castor.ddlgen.GeneratePrimaryKey";

    /** Generate DDL for FOREIGN KEY statement. */
    public static final String GENERATE_DDL_FOR_FOREIRNKEY_KEY = 
        "org.castor.ddlgen.GenerateForeignKey";

    /** Generate DDL for INDEX statement. */
    public static final String GENERATE_DDL_FOR_INDEX_KEY =
        "org.castor.ddlgen.GenerateIndex";

    /** Generate DDL for KEY GENERATOR statement. */
    public static final String GENERATE_DDL_FOR_KEYGENERATOR_KEY = 
        "org.castor.ddlgen.GenerateKeyGenerator";

    //--------------------------------------------------------------------------
    // Database specific configuration keys

    /** Key generator factory classes of supported database engines. */
    public static final String KEYGEN_FACTORIES_KEY =
        "org.castor.ddlgen.KeyGeneratorFactories";

    /** Header comment. */
    public static final String HEADER_COMMENT_KEY =
        "org.castor.ddlgen.HeaderComment";

    /** Trigger template. */
    public static final String TRIGGER_TEMPLATE_KEY =
        "org.castor.ddlgen.TriggerTemplate";
    
    /** MySQL storage engine is one of MYISAM, InnoDB, MERGE, MEMORY, BDB, ISAM
     *  or null for default. */
    public static final String STORAGE_ENGINE_KEY =
        "org.castor.ddlgen.engine.mysql.StorageEngine";

    /** MySQL delete strategy for foreign keys, one of the CASCADE, RESTRICT,
     *  SET NULL, NO ACTION. Default is NO ACTION. */
    public static final String FOREIGN_KEY_ON_DELETE_KEY =
        "org.castor.ddlgen.engine.mysql.ForeignKeyOnDeleteStrategy";

    /** MySQL update strategy for foreign keys, one of the CASCADE, RESTRICT,
     *  SET NULL, NO ACTION. Default is NO ACTION. */
    public static final String FOREIGN_KEY_ON_UPDATE_KEY =
        "org.castor.ddlgen.engine.mysql.ForeignKeyOnUpdateStrategy";
    
    //--------------------------------------------------------------------------

    /**
     * Constructor for DDLGenConfiguration.
     */
    public DDLGenConfiguration() {
        super();
    }

    //--------------------------------------------------------------------------
}

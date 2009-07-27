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
package org.castor.ddlgen.keygenerator;

import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.schemaobject.KeyGenerator;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;
import org.exolab.castor.mapping.xml.Param;

/**
 * HIGH-LOW key generator will be handled by Castor so no DDL needs to be created. It
 * only requires a table to lookup the next values to be used.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class HighLowKeyGenerator extends KeyGenerator {
    //--------------------------------------------------------------------------

    /** Name of key generator algorithm. */
    public static final String ALGORITHM_NAME = "HIGH-LOW";
    
    /** Parameter that defines the name of the sequence table. */
    private static final String PARAM_TABLE_NAME = "table";
    
    /** Parameter that defines the name of the column which contains the table name. */
    private static final String PARAM_KEY_COLUMN = "key-column";
    
    /** Parameter that defines the name of the column used to reserve key values. */
    private static final String PARAM_VALUE_COLUMN = "value-column";
    
    /** Parameter that defines the number of new keys grabed at a time. */
    private static final String PARAM_GRAB_SIZE = "grab-size";
    
    /** Parameter that defines if the same connection should be used to write to the
     *  sequence table. */
    private static final String PARAM_SAME_CONNECTION = "same-connection";
    
    /** Parameter that defines if globally unique keys should be generated. */
    private static final String PARAM_GLOBAL_KEYS = "global";

    //--------------------------------------------------------------------------

    /** Name of the special sequence table. */
    private String _tableName;

    /** Name of the column which contains table names. */
    private String _keyColumn;

    /** Name of the column which is used to reserve primary key values. */
    private String _valueColumn;

    /** Number of new keys the key generator should grab from the sequence table at a
     *  time. */
    private int _grabSize;

    /** Shell the same Connection be used for writing to the sequence table. This is
     *  needed when working in EJB environment, though less efficient. Default to
     *  "false". */
    private boolean _isSameConnection = false;

    /** Shell globally unique keys be generated. Defaults to "false". */
    private boolean _isGlobal = false;

    //--------------------------------------------------------------------------

    /**
     * Constructor for HIGH-LOW key generator specified by given defintion.
     * 
     * @param definition Key generator definition.
     * @throws GeneratorException If grab size parameter can't be parsed as integer.
     */
    public HighLowKeyGenerator(final KeyGeneratorDef definition)
    throws GeneratorException {
        super(ALGORITHM_NAME, definition.getAlias());
        
        Param[] params = definition.getParam();
        for (int i = 0; i < params.length; i++) {
            String name = params[i].getName();
            String value = params[i].getValue();
            if (name == null) { continue; }
            if (PARAM_TABLE_NAME.equalsIgnoreCase(value)) {
                _tableName = value;
            } else if (PARAM_KEY_COLUMN.equalsIgnoreCase(value)) {
                _keyColumn = value;
            } else if (PARAM_VALUE_COLUMN.equalsIgnoreCase(value)) {
                _valueColumn = value;
            } else if (PARAM_GRAB_SIZE.equalsIgnoreCase(value)) {
                try {
                    _grabSize = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    throw new GeneratorException("Can't parse integer" + value, nfe);
                }
            } else if (PARAM_SAME_CONNECTION.equalsIgnoreCase(name)) {
                _isSameConnection = Boolean.valueOf(value).booleanValue();
            } else if (PARAM_GLOBAL_KEYS.equals(name.toLowerCase())) {
                _isGlobal = Boolean.valueOf(value).booleanValue();
            }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Get name of the special sequence table.
     * 
     * @return Name of the special sequence table.
     */
    public String getTableName() { return _tableName; }

    /**
     * Get name of the column which contains table names.
     * 
     * @return Name of the column which contains table names.
     */
    public String getKeyColumn() { return _keyColumn; }

    /**
     * Get name of the column which is used to reserve primary key values.
     * 
     * @return Name of the column which is used to reserve primary key values.
     */
    public String getValueColumn() { return _valueColumn; }

    /**
     * Get number of new keys the key generator should grab from the sequence table at a
     * time.
     * 
     * @return Number of new keys the key generator should grab from the sequence table
     *         at a time.
     */
    public int getGrabSize() { return _grabSize; }

    /**
     * Shell the same Connection be used for writing to the sequence table?
     * 
     * @return If <code>true</code> it uses the same connection t write to the sequence
     *         table. 
     */
    public boolean isSameConnection() { return _isSameConnection; }

    /**
     * Shell globally unique keys be generated?
     * 
     * @return If <code>true</code> globally unique keys are generated.
     */
    public boolean isGlobal() { return _isGlobal; }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void toCreateDDL(final DDLWriter writer) { }

    /**
     * {@inheritDoc}
     */
    public void toDropDDL(final DDLWriter writer) { }

    //--------------------------------------------------------------------------
}

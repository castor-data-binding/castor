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

import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.KeyGeneratorFactory;
import org.castor.ddlgen.schemaobject.Field;
import org.castor.ddlgen.schemaobject.KeyGenerator;
import org.castor.ddlgen.schemaobject.Table;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;

/**
 * Abstract base class for SEQUENCE key generator factories.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class SequenceKeyGeneratorFactory implements KeyGeneratorFactory {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final String getAlgorithmName() { return SequenceKeyGenerator.ALGORITHM_NAME; }
    
    /**
     * {@inheritDoc}
     */
    public final boolean hasMandatoryParameters() { return false; }

    /**
     * {@inheritDoc}
     */
    public final KeyGenerator createKeyGenerator() throws GeneratorException {
        return new SequenceKeyGenerator(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public final KeyGenerator createKeyGenerator(final KeyGeneratorDef definition)
    throws GeneratorException {
        return new SequenceKeyGenerator(this, definition);
    }

    //--------------------------------------------------------------------------

    /**
     * Generate create script for given SEQUENCE key generator.
     * 
     * @param key SEQUENCE key generator to generate DDL for.
     * @return Create script of SEQUENCE key generator.
     */
    public abstract String toCreateDDL(final KeyGenerator key);

    /**
     * Generate drop script for given SEQUENCE key generator.
     * 
     * @param key SEQUENCE key generator to generate DDL for.
     * @return Drop script of SEQUENCE key generator.
     */
    public abstract String toDropDDL(final KeyGenerator key);

    //--------------------------------------------------------------------------

    /**
     * Build a string containing names of all primary key columns separated by '_'.
     * 
     * @param table Table build primary key column names string for.
     * @return String containing names of all primary key columns separated by '_'.
     */
    protected final String toPrimaryKeyList(final Table table) {
        boolean hasPrimaryKey = false;
        StringBuffer sb = new StringBuffer();

        boolean isFirstField = true;
        for (int i = 0; i < table.getFieldCount(); i++) {
            Field field = table.getField(i);
            if (field.isIdentity()) {
                hasPrimaryKey = true;
                if (!isFirstField) { sb.append("_"); }
                isFirstField = false;
                sb.append(field.getName());
            }
        }
        
        //have no primary key
        if (!hasPrimaryKey) { return ""; }
        return sb.toString();
    }
    
    /**
     * Build a string containing types of all primary key columns separated by '_'.
     * 
     * @param table Table build primary key column types string for.
     * @return String containing types of all primary key columns separated by '_'.
     */
    protected final String toPrimaryKeyTypeList(final Table table) {
        boolean hasPrimaryKey = false;
        StringBuffer sb = new StringBuffer();

        boolean isFirstField = true;
        for (int i = 0; i < table.getFieldCount(); i++) {
            Field field = table.getField(i);
            if (field.isIdentity()) {
                hasPrimaryKey = true;
                if (!isFirstField) { sb.append("_"); }
                isFirstField = false;
                sb.append(field.getType().getSqlType());
            }
        }
        
        if (!hasPrimaryKey) { return ""; }
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}

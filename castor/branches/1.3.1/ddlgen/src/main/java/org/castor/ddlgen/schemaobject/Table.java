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
package org.castor.ddlgen.schemaobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.GeneratorException;

/**
 * Abstract base class of all table implementations.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class Table extends AbstractSchemaObject {
    //--------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Table.class);

    //--------------------------------------------------------------------------

    /** List of indices for this table. */
    private List<Index> _indices = new ArrayList<Index>();
    
    /** List of foreign keys for this table. */
    private List<ForeignKey> _foreignKeys = new ArrayList<ForeignKey>();
    
    /** List of field for this table. */
    private List<Field> _fields = new ArrayList<Field>();
    
    /** Map of fields assoizated with their name. */
    private Map<String, Field> _fieldMap = new HashMap<String, Field>();
    
    /** Key generator used for identities of this table. */
    private KeyGenerator _keyGenerator;
    
    /** Primary key with identities of this table. */
    private PrimaryKey _primaryKey;

    /** Schema this table belongs to. */
    private Schema _schema;  
    
    //--------------------------------------------------------------------------

    /**
     * Add given index to list of indices.
     * 
     * @param index Index to add to list of indices.
     */
    public final void addIndex(final Index index) {        
        _indices.add(index);
    }
    
    /**
     * Get number of indices.
     * 
     * @return Number of indices.
     */
    public final int getIndexCount() {
        return _indices.size();
    }
    
    /**
     * Get index at given index.
     * 
     * @param index Index of index to return.
     * @return Index at given index.
     */
    public final Index getIndex(final int index) {
        return _indices.get(index);        
    }
    
    /**
     * Add given foreign key to list of foreign keys.
     * 
     * @param foreignKey Foreign key to add to list of foreign keys.
     */
    public final void addForeignKey(final ForeignKey foreignKey) {
        _foreignKeys.add(foreignKey);       
    }
    
    /**
     * Get number of foreign keys.
     * 
     * @return Number of foreign keys.
     */
    public final int getForeignKeyCount() {
        return _foreignKeys.size();
    }

    /**
     * Get foreign key at given index.
     * 
     * @param index Index of foreign key to return.
     * @return Foreign key at given index.
     */
    public final ForeignKey getForeignKey(final int index) {
        return _foreignKeys.get(index);
    }

    /**
     * Add given field to list of fields.
     * 
     * @param field Field to add to list of fields.
     */
    public final void addField(final Field field) {
        _fields.add(field);
        _fieldMap.put(field.getName(), field);
    }
    
    /**
     * Get number of fields.
     * 
     * @return Number of fields.
     */
    public final int getFieldCount() {
        return _fields.size();
    }
    
    /**
     * Get field at given index.
     * 
     * @param index Index of field to return.
     * @return Field at given index.
     */
    public final Field getField(final int index) {
        return _fields.get(index);
    }
    
    /**
     * Get field with given name.
     * 
     * @param name Name of field to return.
     * @return Field with given name.
     */
    public final Field getField(final String name) {
        return _fieldMap.get(name);
    }
    
    /**
     * Set key generator used for identities of this table.
     * 
     * @param keyGenerator Key generator used for identities of this table.
     */
    public final void setKeyGenerator(final KeyGenerator keyGenerator) {
        _keyGenerator = keyGenerator;
    }

    /**
     * Get key generator used for identities of this table.
     * 
     * @return Key generator used for identities of this table.
     */
    public final KeyGenerator getKeyGenerator() {
        return _keyGenerator;
    }

    /**
     * Set primary key with identities of this table.
     * 
     * @param primaryKey Primary key with identities of this table.
     */
    public final void setPrimaryKey(final PrimaryKey primaryKey) {
        _primaryKey = primaryKey;
    }

    /**
     * Get primary key with identities of this table.
     * 
     * @return Primary key with identities of this table.
     */
    public final PrimaryKey getPrimaryKey() {
        return _primaryKey;
    }

    /**
     * Set schema this table belongs to.
     * 
     * @param schema Schema this table belongs to.
     */
    public final void setSchema(final Schema schema) {
        _schema = schema;
    }

    /**
     * Get schema this table belongs to.
     * 
     * @return Schema this table belongs to.
     */
    public final Schema getSchema() {
        return _schema;
    }

    //--------------------------------------------------------------------------
    
    /**
     * Concatenate all fields names delimited by line separator.
     * 
     * @param writer DDLWriter to write schema objects to.
     * @throws GeneratorException If generation of the script failed or is not supported.
     */
    protected final void fields(final DDLWriter writer) throws GeneratorException {
        String delimiter = DDLGenConfiguration.DEFAULT_FIELD_DELIMITER;
        
        writer.indent();
        for (int i = 0; i < getFieldCount(); i++) {
            if (i > 0) { writer.println(delimiter); }
            getField(i).toCreateDDL(writer);
        }
        writer.unindent();
    }

    //--------------------------------------------------------------------------

    /**
     * Check if given table can be merged with this one.
     * 
     * @param table Table to check if it is able to be merged.
     * @throws GeneratorException If tables cannot be merged.
     */
    public final void merge(final Table table) throws GeneratorException {
        if (table == null) {
            String msg = "Table to merge is missing.";
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (!equals(getName(), table.getName())) {
            String msg = "Name of table differs from: " + getName();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        if (getFieldCount() != table.getFieldCount()) {
            String msg = "Field count of table differs from: " + getFieldCount();
            LOG.error(msg);
            throw new GeneratorException(msg); 
        }
        
        for (int i = 0; i < getFieldCount(); i++) {
            Field field1 = getField(i);
            Field field2 = null;
            for (int j = 0; j < table.getFieldCount(); j++) {
                field2 = table.getField(j);
                if (!equals(field1.getName(), field2.getName())) { break; }
            }
            field1.merge(field2);
        }
        
        for (int i = 0; i < getForeignKeyCount(); i++) {
            ForeignKey fk1 = getForeignKey(i);
            ForeignKey fk2 = null;
            for (int j = 0; j < table.getForeignKeyCount(); j++) {
                fk2 = table.getForeignKey(j); 
                if (!equals(fk1.getName(), fk2.getName())) { break; }
            }
            fk1.merge(fk2);
        }
        
        if (_keyGenerator != null) { _keyGenerator.merge(table.getKeyGenerator()); }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        Table table = (Table) other;
        return equals(getName(), table.getName())
            && equals(_schema, table._schema)
            && equals(_primaryKey, table._primaryKey)
            && equals(_keyGenerator, table._keyGenerator)
            && equals(_fields, table._fields)
            && equals(_foreignKeys, table._foreignKeys)
            && equals(_indices, table._indices);
    }

    /**
     * {@inheritDoc}
     */
    public final int hashCode() {
        int hashCode = 0;
        if (getName() != null) { hashCode += getName().hashCode(); }
        hashCode *= HASHFACTOR;
        if (_schema != null) { hashCode += _schema.hashCode(); }
        hashCode *= HASHFACTOR;
        if (_primaryKey != null) { hashCode += _primaryKey.hashCode(); }
        hashCode *= HASHFACTOR;
        if (_keyGenerator != null) { hashCode += _keyGenerator.hashCode(); }
        hashCode *= HASHFACTOR;
        hashCode += _fields.hashCode();
        hashCode *= HASHFACTOR;
        hashCode += _foreignKeys.hashCode();
        hashCode *= HASHFACTOR;
        hashCode += _indices.hashCode();
        return hashCode;
    }

    //--------------------------------------------------------------------------
}

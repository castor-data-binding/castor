/*
 * Copyright 2011 Ralf Joachim
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
 *
 * $Id$
 */
package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.spi.Identity;

/**
 * Class representing a table for an entity.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class EntityTableInfo extends TableInfo {
    //-----------------------------------------------------------------------------------    

    /** Table extended by this one. */
    private EntityTableInfo _extendedTable;

    /** List of tables that are extending this one. */
    private final List<EntityTableInfo> _extendingTables = new ArrayList<EntityTableInfo>();

    /** List of primary key columns. */
    private final List<ColumnInfo> _primaryKeyColumns = new ArrayList<ColumnInfo>();

    /** List of simple columns. */
    private final List<ColumnInfo> _simpleColumns = new ArrayList<ColumnInfo>();

    /** List of foreign keys. */
    private final List<ForeignKeyInfo> _foreignKeys = new ArrayList<ForeignKeyInfo>();

    /** List of references of one to one or one to many relations. */
    private final List<ForeignReferenceInfo> _foreignReferences =
        new ArrayList<ForeignReferenceInfo>();

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor taking tableName in order to construct table that holds his name only.
     * 
     * @param tableName Name of the table to be constructed.
     */
    protected EntityTableInfo(final String tableName) {
        super(tableName);
    }

    //-----------------------------------------------------------------------------------
    
    /**
     * Set table extended by this one.
     * 
     * @param table Table extended by this one.
     */
    protected void setExtendedTable(final EntityTableInfo table) {
        _extendedTable = table;
    }
    
    /**
     * Add table that is extending this one.
     * 
     * @param table Extending table to add.
     */
    protected void addExtendingTable(final EntityTableInfo table) {
        _extendingTables.add(table);
    }

    //-----------------------------------------------------------------------------------
    
    /**
     * Add primary key column.
     * 
     * @param fieldName Name of field in mapping.
     * @param name Name of this column.
     * @param type SQL type of this column.
     * @param convertFrom Converter to convert value of this column.
     */
    protected void addPrimaryKeyColumn(final String fieldName,
            final String name, final int type, final TypeConvertor convertFrom) {
        _primaryKeyColumns.add(new ColumnInfo(-1, fieldName, name,
                type, convertFrom, true, false, false));
    }
    
    /**
     * Add simple column.
     * 
     * @param fieldIndex Index of the field in array of field values.
     * @param fieldName Name of field in mapping.
     * @param name Name of this column.
     * @param type SQL type of this column.
     * @param convertFrom Converter to convert value of this column.
     * @param store Flag telling if column is persistent or not.
     * @param dirty Flag telling if this column was changed or not.
     */
    protected void addSimpleColumn(final int fieldIndex, final String fieldName,
            final String name, final int type, final TypeConvertor convertFrom,
            final boolean store, final boolean dirty) {
        _simpleColumns.add(new ColumnInfo(fieldIndex, fieldName,
                name, type, convertFrom, false, store, dirty));
    }
    
    /**
     * Add foreign key.
     * 
     * @param foreignKey Foreign key to add.
     */
    protected void addForeignKey(final ForeignKeyInfo foreignKey) {
        _foreignKeys.add(foreignKey);
    }

    /**
     * Add foreign reference.
     * 
     * @param foreignReference Foreign reference to add.
     */
    protected void addForeignReference(final ForeignReferenceInfo foreignReference) {
        _foreignReferences.add(foreignReference);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning extendedTable currently set.
     * 
     * @return ExtendedTable currently set.
     */
    public EntityTableInfo getExtendedTable() { return _extendedTable; }

    /**
     * Method returning list of tables extending this one.
     * 
     * @return List of extending tables.
     */
    public List<EntityTableInfo> getExtendingTables() { return _extendingTables; }

    /**
     * Method returning columns currently set.
     * 
     * @return List of columns currently set.
     */
    public List<ColumnInfo> getPrimaryKeyColumns() { return _primaryKeyColumns; }

    /**
     * Method returning columns currently set.
     * 
     * @return List of columns currently set.
     */
    public List<ColumnInfo> getSimpleColumns() { return _simpleColumns; }

    /**
     * Method returning foreign keys currently set.
     * 
     * @return List of foreign keys currently set.
     */
    public List<ForeignKeyInfo> getForeignKeys() { return _foreignKeys; }
    
    /**
     * Method returning foreign references currently set.
     * 
     * @return List of foreign references currently set.
     */
    public List<ForeignReferenceInfo> getForeignReferences() { return _foreignReferences; }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param identity Identity containing values to be assigned to corresponding columns.
     * @return List containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Identity identity) {
        return toSQL(getPrimaryKeyColumns(), identity);
    }

    /**
     * Method appending values from passed array to corresponding columns.
     * 
     * @param input Identity containing values to be assigned to corresponding columns.
     * @return List containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Object[] input) {
        List<ColumnValue> values = new ArrayList<ColumnValue>();

        for (ColumnInfo column : getSimpleColumns()) {
            values.add(new ColumnValue(column, input[column.getFieldIndex()]));
        }

        for (ForeignKeyInfo foreignKey : getForeignKeys()) {
            Identity identity = (Identity) input[foreignKey.getFieldIndex()];
            values.addAll(toSQL(foreignKey.getFromColumns(), identity));
        }

        return values;
    }
    
    //-----------------------------------------------------------------------------------    
}

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
 * $Id: TableInfo.java 8469 2009-12-28 16:47:54Z rjoachim $
 */
package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.spi.Identity;

/**
 * Class representing a table for an entity.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class EntityTableInfo extends TableInfo {
    //-----------------------------------------------------------------------------------    

    /** Name of the table. */
    private final String _tableName;

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
    private final List<ForeignReferenceInfo> _foreignReferences = new ArrayList<ForeignReferenceInfo>();

    /** List of references of many to many relations. */
    private final List<ManyToMany> _manyToManys = new ArrayList<ManyToMany>();

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor taking tableName in order to construct Table that holds his name only.
     * 
     * @param tableName Name of the table to be constructed.
     */
    protected EntityTableInfo(final String tableName) {
        _tableName = tableName;
    }

    //-----------------------------------------------------------------------------------
    
    protected void setExtendedTable(final EntityTableInfo table) {
        _extendedTable = table;
    }
    
    protected void addExtendingTable(final EntityTableInfo table) {
        _extendingTables.add(table);
    }

    //-----------------------------------------------------------------------------------
    
    /**
     * Add primary key column.
     * 
     * @param name Name of this column.
     * @param type SQL type of this column.
     * @param convertFrom Converter to convert value of this column.
     */
    protected void addPrimaryKeyColumn(final String name, final int type,
            final TypeConvertor convertFrom) {
        ColumnInfo column = new ColumnInfo(name, -1, type, convertFrom, true, false, false);
        _primaryKeyColumns.add(column);
    }
    
    /**
     * Add simple column.
     * 
     * @param name Name of this column.
     * @param index Index of the field this column belongs to.
     * @param type SQL type of this column.
     * @param convertFrom Converter to convert value of this column.
     * @param store Flag telling if column is persistent or not.
     * @param dirty Flag telling if this column was changed or not.
     */
    protected void addSimpleColumn(final String name, final int index, final int type,
            final TypeConvertor convertFrom, final boolean store, final boolean dirty) {
        ColumnInfo column = new ColumnInfo(name, index, type, convertFrom, false, store, dirty);
        _simpleColumns.add(column);
    }
    
    /**
     * Method to add a single column to the columns list.
     * 
     * @param column Column to be added.
     */
    protected void addColumn(final ColumnInfo column) {
        _simpleColumns.add(column);
    }

    protected void addForeignKey(final ForeignKeyInfo foreignKey) {
        _foreignKeys.add(foreignKey);
    }

    protected void addForeignReference(final ForeignReferenceInfo foreignReference) {
        _foreignReferences.add(foreignReference);
    }

    protected void addManyToMany(final ManyToMany manyToMany) {
        _manyToManys.add(manyToMany);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning name of this table.
     * 
     * @return Name of the table currently set.
     */
    public String getTableName() { return _tableName; }

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
     * Method returning list of all columns belonging to this table.
     * 
     * @return List of collected columns.
     */
    public List<ColumnInfo> getAllColumns() {
        List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
        columns.addAll(getPrimaryKeyColumns());
        columns.addAll(getSimpleColumns());
        
        for (ForeignKeyInfo foreignKey : getForeignKeys()) {
            for (ColumnInfo column : foreignKey.getFromColumns()) {
                if (!columns.contains(column)) {
                    columns.add(column);
                }
            }
        }

        return columns;
    }

    public List<ForeignKeyInfo> getForeignKeys() {
        return _foreignKeys;
    }
    
    public List<ForeignReferenceInfo> getForeignReferences() {
        return _foreignReferences;
    }
    
    public List<ManyToMany> getManyToManys() {
        return _manyToManys;
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param input Identity containing values to be assigned to corresponding columns.
     * @return ArrayList containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Identity input) {
        List<ColumnValue> values = new ArrayList<ColumnValue>();

        List<ColumnInfo> columns = getPrimaryKeyColumns();
        for (int i = 0; i < columns.size(); i++) {
            ColumnInfo column = columns.get(i);
            values.add(new ColumnValue(column, column.getIndex(), input.get(i)));
        }

        return values;
    }

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param input Identity containing values to be assigned to corresponding columns.
     * @return ArrayList containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Object[] input) {
        List<ColumnValue> values = new ArrayList<ColumnValue>();

        for (ColumnInfo column : getSimpleColumns()) {
            values.add(new ColumnValue(column, column.getIndex(), null));
        }

        for (ForeignKeyInfo foreignKey : getForeignKeys()) {
            for (ColumnInfo column : foreignKey.getFromColumns()) {
                if (!values.contains(column)) {
                    values.add(new ColumnValue(column, column.getIndex() , null));
                }
            }
        }

        for (ForeignReferenceInfo referer : getForeignReferences()) {
//            EntityTableInfo referenced = (EntityTableInfo) referer.getFromTable();
//            for (ColumnInfo column : referenced.getPrimaryKeyColumns()) {
            for (ColumnInfo column : referer.getFromColumns()) {
                if (!values.contains(column)) {
                    values.add(new ColumnValue(column, column.getIndex() , null));
                }
            }
        }

        for (ManyToMany manyToMany : getManyToManys()) {
            RelationTableInfo relation = (RelationTableInfo) manyToMany.getFromTable();
            for (ColumnInfo column : relation.getRightForeignKey().getFromColumns()) {
                if (!values.contains(column)) {
                    values.add(new ColumnValue(column, column.getIndex() , null));
                }
            }
        }

        int size = getSimpleColumns().size() + getForeignKeys().size()
                 + getForeignReferences().size() + getManyToManys().size();
        int counter = 0;
        for (int i = 0; i < size; ++i) {
            Object inpt = input[i];
            if (inpt == null) {
                // append 'is NULL' in case the value is null
                while (counter < values.size()
                        && i == values.get(counter).getIndex()) {
                    values.get(counter).setValue(null);
                    counter++;
                }
            } else if (inpt instanceof Identity) {
                Identity identity = (Identity) inpt;

                int indx = 0;
                while (counter < values.size()
                        && i == values.get(counter).getIndex()) {
                    if (identity.get(indx) != null) {
                        values.get(counter).setValue(identity.get(indx));
                    }
                    indx++;
                    counter++;
                }

                if (identity.size() != indx) {
                    throw new PersistenceException("Size of identity field mismatch!");
                }
            } else {
                while (counter < values.size()
                        && i == values.get(counter).getIndex()) {
                    if (!(inpt instanceof Collection)) {
                        values.get(counter).setValue(inpt);
                    }
                    counter++;
                }
            }
        }

        return values;
    }

    //-----------------------------------------------------------------------------------    
}

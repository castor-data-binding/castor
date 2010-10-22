/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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

import org.exolab.castor.persist.spi.Identity;

/**
 * Class representing given table classes as Tables.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TableInfo {
    //-----------------------------------------------------------------------------------    

    /** Name of the table. */
    private final String _tableName;

    /** Table extended by this one. */
    private TableInfo _extendedTable;

    /** List of tables that are extending this one. */
    private final List<TableInfo> _extendingTables = new ArrayList<TableInfo>();

    /** Primary key of the table. */
    private final PrimaryKeyInfo _primaryKey;

    /** List of columns. */
    private final List<ColInfo> _columns = new ArrayList<ColInfo>();

    /** List of foreign keys consisting of TableLinks. */
    private final List<TableLink> _foreignKeys = new ArrayList<TableLink>();

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor taking tableName in order to construct Table that holds his name only.
     * 
     * @param tableName Name of the table to be constructed.
     */
    protected TableInfo(final String tableName) {
        _tableName = tableName;
        _primaryKey = new PrimaryKeyInfo(this);
    }

    //-----------------------------------------------------------------------------------
    
    protected void setExtendedTable(final TableInfo table) {
        _extendedTable = table;
    }
    
    protected void addExtendingTable(final TableInfo table) {
        _extendingTables.add(table);
    }

    /**
     * Method to add a single column to the columns list.
     * 
     * @param column Column to be added.
     */
    protected void addColumn(final ColInfo column) {
        _columns.add(column);
        
        if (column.isPrimaryKey()) {
            _primaryKey.addColumn(column);
        }
    }

    protected void addForeignKey(final TableLink foreignKey) {
        _foreignKeys.add(foreignKey);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning list of all columns belonging to this table.
     * 
     * @return List of collected columns.
     */
    public List<ColInfo> iterateAll() {
        List<ColInfo> cols = new ArrayList<ColInfo>();
        cols.addAll(_columns);
        
        for (TableLink lnk : _foreignKeys) {
            for (ColInfo col : lnk.getStartCols()) {
                if (!cols.contains(col)) {
                    cols.add(col);
                }
            }
        }

        return cols;
    }

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param input Identity containing values to be assigned to corresponding columns.
     * @return ArrayList containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Identity input) {
        List<ColumnValue> values = new ArrayList<ColumnValue>();

        List<ColInfo> columns = getPrimaryKey().getColumns();
        for (int i = 0; i < columns.size(); i++) {
        	ColInfo column = columns.get(i);
            values.add(new ColumnValue(column, column.getFieldIndex(), input.get(i)));
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

        for (ColInfo column : _columns) {
            if (!column.isPrimaryKey()) {
                values.add(new ColumnValue(column, column.getFieldIndex(), null));
            }
        }

        for (TableLink lnk : _foreignKeys) {
            for (ColInfo column : lnk.getStartCols()) {
                if (!values.contains(column)) {
                	int index = column.getFieldIndex();
                    if (index == -1) {
                        // index of foreign key columns has to be taken from tableLink
                        // because the fields in this case have to use other fieldindexes
                        // than in their tables.
                        index = lnk.getFieldIndex();
                    }
                    values.add(new ColumnValue(column, index , null));
                }
            }
        }

        int size = _columns.size() + _foreignKeys.size() - _primaryKey.getColumns().size();
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
    public TableInfo getExtendedTable() { return _extendedTable; }

    /**
     * Method returning list of tables extending this one.
     * 
     * @return List of extending tables.
     */
    public List<TableInfo> getExtendingTables() { return _extendingTables; }

    /**
     * Get primary key of the table.
     * 
     * @return Primary key of the table.
     */
    public PrimaryKeyInfo getPrimaryKey() { return _primaryKey; }

    /**
     * Method returning columns currently set.
     * 
     * @return List of columns currently set.
     */
    public List<ColInfo> getColumns() { return _columns; }

    /**
     * Method returning list of foreign keys.
     * 
     * @return List of foreign keys.
     */
    public List<TableLink> getForeignKeys() { return _foreignKeys; }

    //-----------------------------------------------------------------------------------    
}

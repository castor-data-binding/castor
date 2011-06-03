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
import java.util.List;

import org.exolab.castor.mapping.TypeConvertor;

/**
 * 
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class ManyToMany {
    //-----------------------------------------------------------------------------------    

    /** Table referenced by the foreign key. */
    private final EntityTableInfo _toTable;

    /** Table that holds the foreign key. */
    private final TableInfo _fromTable;

    /** Alias of the table that holds the foreign key. */
    private final String _fromAlias;

    /** List of columns of the foreign key. */
    private final List<ColumnInfo> _fromColumns = new ArrayList<ColumnInfo>();

    //-----------------------------------------------------------------------------------    

    /**
     * Construct a foreign reference.
     * 
     * @param toTable Table referenced by the foreign key.
     * @param fromTable Table that holds the foreign key.
     * @param fromAlias Alias of the table that holds the foreign key.
     */
    protected ManyToMany(final EntityTableInfo toTable, final TableInfo fromTable,
            final String fromAlias) {
        _toTable = toTable;
        _fromTable = fromTable;
        _fromAlias = fromAlias;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Add from column.
     * 
     * @param name Name of this column.
     * @param index Index of the field this column belongs to.
     * @param type SQL type of this column.
     * @param convertFrom Converter to convert value of this column.
     */
    protected void addFromColumn(final String name, final int index, final int type,
            final TypeConvertor convertFrom) {
        ColumnInfo column = new ColumnInfo(name, index, type, convertFrom, false, false, false);
        _fromColumns.add(column);
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Method returning the table referenced by the foreign key.
     * 
     * @return Table referenced by the foreign key.
     */
    public EntityTableInfo getToTable() { return _toTable; }

    /**
     * Method returning the table that holds the foreign key.
     * 
     * @return Table that holds the foreign key.
     */
    public TableInfo getFromTable() { return _fromTable; }

    /**
     * Method returning the alias of the table that holds the foreign key.
     * 
     * @return Alias of the table that holds the foreign key.
     */
    public String getFromAlias() { return _fromAlias; }

    /**
     * Method returning list of columns of the foreign key.
     * 
     * @return List of columns of the foreign key
     */
    public List<ColumnInfo> getFromColumns() { return _fromColumns; }

    //-----------------------------------------------------------------------------------    
}

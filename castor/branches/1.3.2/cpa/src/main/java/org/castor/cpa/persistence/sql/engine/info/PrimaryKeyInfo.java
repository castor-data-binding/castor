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
import java.util.List;

/**
 * Class representing primary key of a table.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class PrimaryKeyInfo {
    //-----------------------------------------------------------------------------------

    /** Table the primary key belongs to. */
    private final TableInfo _table;

    /** List of primary key columns. */
    private final List<ColumnInfo> _columns = new ArrayList<ColumnInfo>();

    //-----------------------------------------------------------------------------------

    /**
     * Construct primary key info for given table.
     * 
     * @param table Table the primary key belongs to.
     */
    protected PrimaryKeyInfo(final TableInfo table) {
        _table = table;
    }
    
    //-----------------------------------------------------------------------------------
    
    /**
     * Add column to list of primary key columns.
     * 
     * @param column Column that belongs to primary key.
     */
    protected void addColumn(final ColumnInfo column) {
        _columns.add(column);
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Get table the primary key belongs to.
     * 
     * @return Table the primary key belongs to.
     */
    public TableInfo getTable() { return _table; }

    /**
     * Get list of primary key columns.
     * 
     * @return List of primary key columns.
     */
    public List<ColumnInfo> getColumns() { return _columns; }

    //-----------------------------------------------------------------------------------
}

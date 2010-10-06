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
 * $Id: TableLink.java 8469 2009-12-28 16:47:54Z rjoachim $
 */

package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing relations between tables.
 * There are 3 possible types of tableLinks:
 *      SIMPLE: Used for 1:1 relations. No join will be added to the query. Just the startColumns
 *              will be added to the query.
 *      MANY_KEY: Used to express 1:m relations. Join will be added to the query.
 *      MANY_TABLE: Used to express n:m relations. Join will be added to the query.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TableLink {
    //-----------------------------------------------------------------------------------    

    /** Constant defining simple table-link (1:1). */
    public static final int SIMPLE = 0;

    /** Constant defining many key relation (1:m). */
    public static final int MANY_KEY = 1;

    /** Constant defining many table relation (n:m). */
    public static final int MANY_TABLE = 2;

    //-----------------------------------------------------------------------------------    

    /** List of columns of the left table used to construct a join. */
    private List<ColInfo> _startCols;

    /** Target table to be joined on the left one. */
    private TableInfo _targetTable;

    /** List of columns of the target table used to construct a join. */
    private List<ColInfo> _targetCols;

    /** Table alias needed to join a table already in the query. */
    private String _tableAlias;

    /** Variable storing type of the relation (SIMPLE, MANY_KEY or MANY_TABLE). */
    private int _relationType;

    /** Variable storing many key of the tableLink when existing. */
    private List<String> _manyKey;

    /** Variable storing fieldIndex for the tableLink. Combined columns that are used as
     *  startCols of a tableLink need an own field index.
     */
    private int _fldIndex;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param targetTable The right table of the join.
     * @param relationType The type of the relation.
     * @param tableAlias Alias of the table.
     * @param startColumns Columns of the left table needed for joins.
     * @param fieldIndex Index of the startColumns.
     */
    protected TableLink(final TableInfo targetTable, final int relationType,
            final String tableAlias, final List<ColInfo> startColumns, final int fieldIndex) {
        _targetTable = targetTable;
        _relationType = relationType;
        _tableAlias = tableAlias;
        _startCols = startColumns;
        _fldIndex = fieldIndex;
        _targetCols = new ArrayList<ColInfo>();
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning a list of start columns.
     * 
     * @return List of start columns.
     */
    public List<ColInfo> getStartCols() { return _startCols; }

    /**
     * Method adding a single targetCol.
     * 
     * @param col Column to be added as a target column.
     */
    public void addTargetCol(final ColInfo col) { _targetCols.add(col); }

    /**
     * Method adding a list of targetCols.
     * 
     * @param cols List of columns to be added as target columns.
     */
    public void addTargetCols(final List<ColInfo> cols) { _targetCols.addAll(cols); }

    /**
     * Method returning a list of target columns.
     * 
     * @return List of target columns of the join.
     */
    public List<ColInfo> getTargetCols() { return _targetCols; }

    /**
     * Method returning the right table of the join.
     * 
     * @return TargetTable of the join.
     */
    public TableInfo getTargetTable() { return _targetTable; }

    /**
     * Method returning the table alias to be used for a potential join.
     * 
     * @return The tableAlias to be used for a join.
     */
    public String getTableAlias() { return _tableAlias; }

    /**
     * Method returning relation type currently set.
     * 
     * @return Relation type currently set.
     */
    public int getRelationType() { return _relationType; }

    /**
     * Method given setting many key.
     * 
     * @param manyKey The many key to be set.
     */
    public void setManyKey(final List<String> manyKey) { _manyKey = manyKey; }

    /**
     * Method returning many key.
     * 
     * @return Many key currently set.
     */
    public List<String> getManyKey() { return _manyKey; }

    /**
     * Method returning fieldIndex currently set.
     * 
     * @return FieldIndex currently set.
     */
    public int getFieldIndex() { return _fldIndex; }

    //-----------------------------------------------------------------------------------    
}

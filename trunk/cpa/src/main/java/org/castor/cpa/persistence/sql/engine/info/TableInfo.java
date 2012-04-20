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
 * $Id$
 */
package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.persist.spi.Identity;

/**
 * Abstract base class representing given table.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class TableInfo {
    //-----------------------------------------------------------------------------------    

    /** Name of the table. */
    private final String _tableName;
    
    //-----------------------------------------------------------------------------------    
    
    /**
     * Constructor taking tableName in order to construct table that holds his name only.
     * 
     * @param tableName Name of the table to be constructed.
     */
    protected TableInfo(final String tableName) {
        _tableName = tableName;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning name of this table.
     * 
     * @return Name of the table currently set.
     */
    public final String getTableName() { return _tableName; }

    //-----------------------------------------------------------------------------------    

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param columns List of columns.
     * @param identity Identity containing values to be assigned to corresponding columns.
     * @return List containing all columns with their corresponding values.
     */
    public static final List<ColumnValue> toSQL(final List<ColumnInfo> columns,
            final Identity identity) {
        List<ColumnValue> values = new ArrayList<ColumnValue>(columns.size());

        for (int i = 0; i < columns.size(); i++) {
            ColumnInfo column = columns.get(i);
            if (identity == null) {
                values.add(new ColumnValue(column, null));
            } else {
                values.add(new ColumnValue(column, identity.get(i)));
            }
        }

        return values;
    }

    //-----------------------------------------------------------------------------------    
}

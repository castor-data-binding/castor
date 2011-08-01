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

/**
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class ColumnValue {
    //-----------------------------------------------------------------------------------    

    /** Column the value belongs to. */
    private final ColumnInfo _column;
    
    /** Value of the column. */
    private Object _value;
    
    //-----------------------------------------------------------------------------------    

    /**
     * Construct ColumnValue for given column with given value. 
     * 
     * @param column Column the value belongs to.
     * @param value Value of the column.
     */
    protected ColumnValue(final ColumnInfo column, final Object value) {
        _column = column;
        _value = _column.toSQL(value);
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Method returning name of this column.
     * 
     * @return Name of this column.
     */
    public String getName() { return _column.getName(); }
    
    /**
     * Method returning SQL type of this column.
     * 
     * @return SQL type of this column.
     */
    public int getType() { return _column.getType(); }
    
    /**
     * Method returning store flag.
     * 
     * @return Store flag.
     */
    public boolean isStore() { return _column.isStore(); }
    
    /**
     * Method returning dirty flag.
     * 
     * @return Dirty flag.
     */
    public boolean isDirty() { return _column.isDirty(); }
    
    /**
     * Get value of the column.
     * 
     * @return Value of the column.
     */
    public Object getValue() { return _value; }

    //-----------------------------------------------------------------------------------    
}

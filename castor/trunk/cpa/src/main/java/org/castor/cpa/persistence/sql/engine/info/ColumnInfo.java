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

import org.exolab.castor.mapping.TypeConvertor;

/**
 * Class representing columns belonging to a table.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class ColumnInfo implements Cloneable {
    //-----------------------------------------------------------------------------------    

    /** Variable storing name of this column. */
    private final String _name;

    /** Variable storing the index of the field this column belongs to. */
    private final int _index;

    /** Variable storing the type of this column. */
    private final int _type;
    
    /** Variable storing the converter to convert the type of this columns value. */
    private final TypeConvertor _convertFrom;

    /** Flag telling if column is persistent or not. */
    private final boolean _store;

    /** Flag telling if column is dirty or not. */
    private final boolean _dirty;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor with all given values.
     * 
     * @param name Name of this column.
     * @param index Index of the field this column belongs to.
     * @param type SQL type of this column.
     * @param convertFrom Converter to convert value of this column.
     * @param store Flag telling if column is persistent or not.
     * @param dirty Flag telling if this column was changed or not.
     */
    protected ColumnInfo (final String name, final int index, final int type,
            final TypeConvertor convertFrom, final boolean store, final boolean dirty) {
        _name = name;
        _index = index;
        _type = type;
        _convertFrom = convertFrom;
        _store = store;
        _dirty = dirty;
    }

    /**
     * Constructor creating new column with only name set.
     * 
     * @param name Name to be set.
     */
    protected ColumnInfo(final String name) {
        _name = name; 
        _index = 0;
        _type = 0;
        _convertFrom = null;
        _store = false;
        _dirty = false;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning name of this column.
     * 
     * @return Name of this column.
     */
    public String getName() { return _name; }

    /**
     * Method returning the field index of this column.
     * 
     * @return Index of the field this column belongs to.
     */
    public int getIndex() { return _index; }

    /**
     * Method returning SQL type currently set.
     * 
     * @return SQL type currently set.
     */
    public int getType() { return _type; }

    /**
     * Method returning store flag.
     * 
     * @return Store flag.
     */
    public boolean isStore() { return _store; }

    /**
     * Method returning dirty flag.
     * 
     * @return Dirty flag.
     */
    public boolean isDirty() { return _dirty; }

    //-----------------------------------------------------------------------------------    

    /**
     * Method to translate java data types to sql data types.
     * 
     * @param object Object to be translated to sql data type.
     * @return Object converted to sql data type.
     */
    protected Object toSQL(final Object object) {
        if ((object == null) || (_convertFrom == null)) {
            return object;
        }
        return _convertFrom.convert(object);
    }

    //-----------------------------------------------------------------------------------    
}

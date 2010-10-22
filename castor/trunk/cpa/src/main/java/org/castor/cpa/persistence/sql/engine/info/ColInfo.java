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
public final class ColInfo implements Cloneable {
    //-----------------------------------------------------------------------------------    

    /** Variable storing name of this column. */
    private String _sqlName;

    /** Variable storing the type of this column. */
    private int _type;
    
    /** Does this column belong to a primary key? */
    private boolean _primaryKey = false;

    /** Flag telling if column is persistent or not. */
    private boolean _store;

    /** Variable storing the index of the field this column belongs to. */
    private int _fldIndex;

    /** Flag telling if column is dirty or not. */
    private boolean _dirty;

    /** Variable storing the converter to convert the type of this columns value. */
    private TypeConvertor _convertFrom;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor with all given values.
     * 
     * @param sqlName Name of this column
     * @param type Type of this column
     * @param convertTo Converter to convert value of this column
     * @param convertFrom Converter to convert value of this column
     * @param store Flag telling if column is persistent or not
     * @param fieldIndex Index of the field this column belongs to
     * @param dirty Flag telling if this column was changed or not
     * @param primaryKey Does this column belong to a primary key?
     */
    protected ColInfo (final String sqlName, final int type,
            final TypeConvertor convertFrom, final boolean store,
            final int fieldIndex, final boolean dirty, final boolean primaryKey) {
        _sqlName = sqlName;
        _type = type;
        _convertFrom = convertFrom;
        _store = store;
        _fldIndex = fieldIndex;
        _dirty = dirty;
        _primaryKey = primaryKey;
    }

    /**
     * Constructor creating new column with only name set.
     * 
     * @param sqlName Name to be set.
     */
    protected ColInfo(final String sqlName) { _sqlName = sqlName; }

    //-----------------------------------------------------------------------------------    

    /**
     * Method to translate java data types to sql data types.
     * 
     * @param object Object to be translated to sql data type.
     * @return Object converted to sql data type.
     */
    public Object toSQL(final Object object) {
        if ((object == null) || (_convertFrom == null)) {
            return object;
        }
        return _convertFrom.convert(object);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning name of this column.
     * 
     * @return Name of this column.
     */
    public String getName() { return _sqlName; }

    /**
     * Does this column belong to a primary key?
     * 
     * @return Does this column belong to a primary key?
     */
    public boolean isPrimaryKey() { return _primaryKey; }

    /**
     * Method returning sqlType currently set.
     * 
     * @return SqlType currently set.
     */
    public int getSqlType() { return _type; }

    /**
     * Method returning store flag.
     * 
     * @return Store flag.
     */
    public boolean isStore() { return _store; }

    /**
     * Method returning the field index of this column.
     * 
     * @return Index of the field this column belongs to.
     */
    public int getFieldIndex() { return _fldIndex; }

    /**
     * Method setting the given field index.
     * 
     * @param fieldIndex FieldIndex to be set.
     */
    public void setFieldIndex(final int fieldIndex) { _fldIndex = fieldIndex; }

    /**
     * Method returning dirty flag.
     * 
     * @return Dirty flag.
     */
    public boolean isDirty() { return _dirty; }

    //-----------------------------------------------------------------------------------    
}

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

/**
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class ColumnValue {
    //-----------------------------------------------------------------------------------    

	private final ColumnInfo _info;
    private final int _index;
    private Object _value;
    
    //-----------------------------------------------------------------------------------    

    protected ColumnValue(final ColumnInfo info, final int index, final Object value) {
        _info = info;
        _index = index;
        
        setValue(value);
    }
    
    //-----------------------------------------------------------------------------------    

    protected void setValue(final Object value) { _value = _info.toSQL(value); }
    
    //-----------------------------------------------------------------------------------    

    public String getName() { return _info.getName(); }
    
    public int getType() { return _info.getType(); }
    
    public boolean isStore() { return _info.isStore(); }
    
    public boolean isDirty() { return _info.isDirty(); }
    
    public int getIndex() { return _index; }
    
    public Object getValue() { return _value; }

    //-----------------------------------------------------------------------------------    
}

/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
 */
package org.castor.cpa.query.object.literal;

/**
 * Final immutable class that represents a enum expression.
 *
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7121 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.3
 */
public final class EnumLiteral extends AbstractLiteral {
    //--------------------------------------------------------------------------
    
    /** Enum constant represented by this enum literal. */
    private final Enum < ? > _value;
    
    //--------------------------------------------------------------------------

    /**
     * Construct enum literal for given string value. The string value has to contain the full
     * class name of the enumeration including the name of the constant separated by dot. If
     * an enum constant can not be found an IllegalArgumentException will be thrown.
     * 
     * @param value String value with full name of the enum constant.
     */
    public EnumLiteral(final String value) {
        if (value == null) { throw new NullPointerException(); }
        
        int dot = value.lastIndexOf('.');
        if (dot < 0) { throw new IllegalArgumentException(); }
        
        Class type = null;
        try {
            String typename = value.substring(0, dot);
            type = Class.forName(typename);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException();
        }
        if (!type.isEnum()) { throw new IllegalArgumentException(); }
        
        Object[] enumvalues = type.getEnumConstants();
        
        String enumname = value.substring(dot + 1);
        Enum < ? > enumvalue = null;
        for (int i = 0; i < enumvalues.length; i++) {
            if (enumname.equals(enumvalues[i].toString())) {
                enumvalue = (Enum) enumvalues[i];
            }
        }
        if (enumvalue == null) { throw new IllegalArgumentException(); }
        _value = enumvalue;
    }
    
    /**
     * Construct enum literal for given enum constant.
     * 
     * @param value Enum constant to represent by the enum literal.
     */
    public EnumLiteral(final Enum < ? > value) {
        if (value == null) { throw new NullPointerException(); }
        _value = value;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Get enum constant represented by this enum literal.
     * 
     * @return Enum constant represented by this enum literal.
     */
    public Enum < ? > getValue() {
        return _value;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public StringBuilder toString(final StringBuilder sb) {
        return sb.append(_value.getClass().getName()).append('.').append(_value);
    }

    //--------------------------------------------------------------------------
          
}

/*
 * Copyright 2007 Ralf Joachim
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
package org.castor.cpa.persistence.convertor;

/**
 * Abstract base class to convert from one type to another.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public abstract class AbstractTypeConvertor implements TypeConvertor {
    //-----------------------------------------------------------------------------------

    /** The type being converted from. */
    private final Class _fromType;

    /** The type being converted to. */
    private final Class _toType;

    //-----------------------------------------------------------------------------------

    /**
     * Construct a Converter between given fromType an toType.
     * 
     * @param fromType The type being converted from.
     * @param toType The type being converted to.
     */
    public AbstractTypeConvertor(final Class fromType, final Class toType) {
        _fromType = fromType;
        _toType = toType;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Class fromType() { return _fromType; }
    
    /**
     * {@inheritDoc}
     */
    public final Class toType() { return _toType; }
    
    /**
     * {@inheritDoc}
     */
    public final String toString() {
        return _fromType.getName() + "-->" + _toType.getName();
    }
    
    //-----------------------------------------------------------------------------------
}

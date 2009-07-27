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

import org.castor.core.util.AbstractProperties;

/**
 * Interface for a type convertor. A type convertor converts a Java object from one
 * type to another. A type convertor implementation is required for each type of
 * conversion.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public interface TypeConvertor extends org.exolab.castor.mapping.TypeConvertor, Cloneable {
    //-----------------------------------------------------------------------------------

    /**
     * Configure the converter with given configuration.
     * 
     * @param properties Properties to use.
     */
    void configure(AbstractProperties properties);
    
    /**
     * Initialize the converter with the given parameter. If no parameter is available the method
     * don't need to be called or may be called with <code>null</code> which both should lead to
     * the default behaviour of the converter.
     * 
     * @param parameter The parameter for the convertor (null if is not specified).
     */
    void parameterize(String parameter);
    
    /**
     * Creates and returns a copy of this object.
     * 
     * @return A clone of this instance.
     */
    Object clone();

    //-----------------------------------------------------------------------------------

    /**
     * Get the type being converted from.
     * 
     * @return The type being converted from.
     */
    Class < ? > fromType();
    
    /**
     * Get the type being converted to.
     * 
     * @return The type being converted to.
     */
    Class < ? > toType();
    
    /**
     * Convert the object from one type to another.
     *
     * @param object The object to convert.
     * @return The converted object.
     * @throws ClassCastException The object is not of the type supported by this convertor.
     */
    Object convert(Object object) throws ClassCastException;
    
    //-----------------------------------------------------------------------------------
}

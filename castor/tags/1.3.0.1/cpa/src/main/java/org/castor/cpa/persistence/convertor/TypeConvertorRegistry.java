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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;
import org.exolab.castor.mapping.MappingException;

/**
 * Registry of type convertors.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1.3
 */
public final class TypeConvertorRegistry {
    //-----------------------------------------------------------------------------------
    
    /** Map of maps of type converters. For the outer one the key is <tt>fromType</tt>
     *  while the key of the inner one is <tt>toType</tt>. */
    private final Map < Class < ? > , Map < Class < ? > , TypeConvertor > > _convertors
        = new HashMap < Class < ? > , Map < Class < ? > , TypeConvertor > > ();

    //-----------------------------------------------------------------------------------

    /**
     * Create a new registry instance of type convertors. The registry will be initialized
     * with all type convertors specified through <b>TYPE_CONVERTORS</b> property of given
     * properties. The properties get also passed to the type converters for them to
     * get any required property.
     * 
     * @param properties The properties to use.
     */
    public TypeConvertorRegistry(final AbstractProperties properties) {
        Object[] objects = properties.getObjectArray(
                CPAProperties.TYPE_CONVERTORS, properties.getApplicationClassLoader());
        for (int i = 0; i < objects.length; i++) {
            TypeConvertor convertor = (TypeConvertor) objects[i];
            convertor.configure(properties);
            
            putConvertor(convertor.fromType(), convertor.toType(), convertor);
        }
    }

    //-----------------------------------------------------------------------------------

    /**
     * Put given type convertor in map of maps of convertors.
     * 
     * @param fromType The Java type to convert from.
     * @param toType The Java type to convert to.
     * @param convertor The TypeConverter to convert fromType into toType.
     */
    private void putConvertor(final Class < ? > fromType, final Class < ? > toType,
            final TypeConvertor convertor) {
        Map < Class < ? > , TypeConvertor > convertors = _convertors.get(fromType);
        if (convertors == null) {
            convertors = new HashMap < Class < ? > , TypeConvertor > ();
            _convertors.put(fromType, convertors);
        }
        convertors.put(toType, convertor);
    }
    
    /**
     * Returns a type convertor. A type convertor can be used to convert
     * an object from Java type <tt>fromType</tt> to Java type <tt>toType</tt>.
     *
     * @param fromType The Java type to convert from.
     * @param toType The Java type to convert to.
     * @return The TypeConverter to convert fromType into toType.
     * @throws MappingException No suitable convertor was found.
     */
    private TypeConvertor getConvertor(final Class < ? > fromType, final Class < ? > toType)
    throws MappingException {
        TypeConvertor convertor = null;
        
        // first search exact match of from and to type.
        Map < Class < ? > , TypeConvertor > convertors = _convertors.get(fromType);
        if (convertors != null) {
            convertor = convertors.get(toType);
        }

        // if not found yet search assignment compatible from and to type.
        if (convertor == null) {
            Iterator < Map.Entry < Class < ? > , Map < Class < ? > , TypeConvertor > > > fromIter;
            fromIter = _convertors.entrySet().iterator();
            while ((convertor == null) && fromIter.hasNext()) {
                Map.Entry < Class < ? > , Map < Class < ? > , TypeConvertor > > fromEntry;
                fromEntry = fromIter.next();
                if (fromEntry.getKey().isAssignableFrom(fromType)) {
                    convertors = fromEntry.getValue();
                    
                    Iterator < Map.Entry < Class < ? > , TypeConvertor > > toIter;
                    toIter = convertors.entrySet().iterator();
                    while ((convertor == null) && toIter.hasNext()) {
                        Map.Entry < Class < ? > , TypeConvertor > toEntry = toIter.next();
                        if (toEntry.getKey().isAssignableFrom(toType)) {
                            convertor = toEntry.getValue();
                        }
                    }
                }
            }
            
            // if still not found throw an exception.
            if (convertor == null) {
                throw new MappingException("mapping.noConvertor",
                        fromType.getName(), toType.getName());
            }

            // put the assignment compatible convertor found into the map
            // for faster retrieval when it is needed a second time.
            putConvertor(fromType, toType, convertor);
        }
        
        return convertor;
    }

    /**
     * Returns a type convertor initialized with parameter if specified. A type convertor
     * can be used to convert an object from Java type <tt>fromType</tt> to Java type
     * <tt>toType</tt>.
     *
     * @param fromType The Java type to convert from.
     * @param toType The Java type to convert to.
     * @param parameter The parameter for the convertor (null if is not specified).
     * @return The TypeConverter to convert fromType into toType.
     * @throws MappingException No suitable convertor was found.
     */
    public TypeConvertor getConvertor(final Class < ? > fromType, final Class < ? > toType,
            final String parameter) throws MappingException {
        TypeConvertor convertor = getConvertor(fromType, toType);
        
        // if a parameter is to be set, create a clone of the type convertor
        // and initialize it with the parameter given.
        if (parameter != null) {
            convertor = (TypeConvertor) convertor.clone();
            convertor.parameterize(parameter);
        }
        
        return convertor;
    }
    
    //-----------------------------------------------------------------------------------
}

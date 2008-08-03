/*
 * Copyright 2005-2007 Werner Guttmann
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
package org.exolab.castor.builder.printing;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Factory class to create {@link JClassPrinter} instances.
 */
public class JClassPrinterFactory {

    /**
     * Store for already loaded printers.
     */
    private static Map _printers = new HashMap();
    
    /**
     * The default type class name.
     */
    private static String _default;
    
    /**
     * Loads the classes form configuration and stores it in the map.
     * @param typeString The configuration string, a comma separated list
     *                   of jClassPrinter classes.
     */
    public static void init(final String typeString) {
        StringTokenizer tokenizer = new StringTokenizer(typeString,",");
        while(tokenizer.hasMoreTokens()) {
            String classname = tokenizer.nextToken();
            try {
                Class c = Class.forName(classname);
                JClassPrinter classPrinter = (JClassPrinter) c.newInstance();
                _printers.put(classname, classPrinter);
                if (_default == null) {
                    _default = classname;
                }
                    
            } catch (Exception e) {
                
                IllegalArgumentException exception = 
                    new IllegalArgumentException("JClassPrinter class not found: "
                        + classname);
                exception.initCause(e);
                throw exception;
            } 
        }
        
    }
    
    /**
     * Returns the default class name.
     * @return The default class name.
     */
    public static String getDefaultType() {
        return _default;
    }
    
    /**
     * Returns the {@link JClassPrinter} instance represented by the 
     * given class name.
     * 
     * @param key The fully qualified class name.
     * @return The instance of {@link JClassPrinter} represented by the 
     *        class name.
     */
    public static JClassPrinter getJClassPrinter(final String classname) {
        return (JClassPrinter) _printers.get(classname);
    }
    
}

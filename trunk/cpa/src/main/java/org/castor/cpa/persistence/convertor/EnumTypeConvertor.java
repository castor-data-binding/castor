/*
 * Copyright 2006 Werner Guttmann
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

import java.lang.reflect.Method;

/**
 * Custom convertor used to handle enum types.
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class EnumTypeConvertor extends AbstractSimpleTypeConvertor {
    //-----------------------------------------------------------------------------------
    
    private final Method _method;
    
    //-----------------------------------------------------------------------------------

    public EnumTypeConvertor(final Class<?> fromType, final Class<?> toType,
                             final Method method) {
        super(fromType, toType);
        _method = method;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Object convert(final Object object) {
        try { // Invoking method for conversion via reflection.
            return _method.invoke(this.toType(), (String) object);
        } catch (Exception ex) {
            return null;
        }
    }

    //-----------------------------------------------------------------------------------
}
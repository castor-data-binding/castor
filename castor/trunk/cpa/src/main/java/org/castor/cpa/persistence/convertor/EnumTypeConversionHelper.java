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

/**
 * Can be used to get a type-safe enum constant value via its ordinal value.
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class EnumTypeConversionHelper {
    //-----------------------------------------------------------------------------------
    
    private final Class<?> _enumType;

    //-----------------------------------------------------------------------------------
    
    public EnumTypeConversionHelper(final Class<?> enumType) {
        _enumType = enumType;
    }
    
    //-----------------------------------------------------------------------------------

    public Class<?> getEnumConstantValueByOrdinal(final int ordinal) {
        return (Class<?>) _enumType.getEnumConstants()[ordinal];
    }

    //-----------------------------------------------------------------------------------
}

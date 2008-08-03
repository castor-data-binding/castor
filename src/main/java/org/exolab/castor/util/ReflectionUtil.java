/*
 * Copyright 2007 Werner Guttmann
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
package org.exolab.castor.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class to support reflection-based operations.
 *
 * @since 1.1.2
 */
public class ReflectionUtil {

    /**
     * Calls isEnum() method on target class vi areflection to find out
     * whether the given type is a Java 5 enumeration.
     * @param type The type to analyze.
     * @return True if the type given is a Java 5.0 enum.
     * @throws NoSuchMethodException If the method can not be found.
     * @throws IllegalAccessException If access to this method is illegal
     * @throws InvocationTargetException If the target method can not be invoked.
     */
    public static Boolean isEnumViaReflection(Class type) 
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method isEnumMethod = type.getClass().getMethod("isEnum", (Class[]) null);
        return (Boolean) isEnumMethod.invoke(type, (Object[]) null);
    }


}

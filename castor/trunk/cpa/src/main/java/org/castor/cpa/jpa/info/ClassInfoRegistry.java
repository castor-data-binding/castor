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
package org.castor.cpa.jpa.info;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds {@link ClassInfo}s describing the given classes
 * mapped to class objects.
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class ClassInfoRegistry {
    //-----------------------------------------------------------------------------------
    
    /**
     * Map for ClassInfo storage.
     */
    private static final Map<Class<?>, ClassInfo> CLASS_INFOS = new HashMap<Class<?>, ClassInfo>();

    //-----------------------------------------------------------------------------------
    
    /**
     * Returns the {@link ClassInfo} instance registered for the given Class type.
     * @param type A given {@link Class} type.
     * @return The {@link ClassInfo} registered for the given {@link Class} type.
     */
    public static ClassInfo getClassInfo(final Class<?> type) {
        return CLASS_INFOS.get(type);
    }

    /**
     * Registers a {@link ClassInfo} instance for the given {@link Class} instance.
     * @param type A given {@link Class} type.
     * @param classInfo The {@link ClassInfo} instance to register.
     */
    public static void registerClassInfo(final Class<?> type, final ClassInfo classInfo) {
        CLASS_INFOS.put(type, classInfo);
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Hide constructor of utility class.
     */
    private ClassInfoRegistry() { }
    
    //-----------------------------------------------------------------------------------
}

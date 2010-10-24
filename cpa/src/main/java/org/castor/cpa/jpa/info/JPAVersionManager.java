/*
 * Copyright 2010 Werner Guttmann
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public class JPAVersionManager {

    private Map<Class<?>, String> entityVersions = 
        new ConcurrentHashMap<Class<?>, String>();

    /**
     * Thread-safe singleton implementation based on the initialization on
     * demand holder idiom.
     */
    private static class SingletonHolder {
        private static final JPAVersionManager INSTANCE = new JPAVersionManager();
    }

    public static JPAVersionManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(Class<?> type, String versionField)
            throws MultipleVersionFieldDefinitionException {
        synchronized (entityVersions) {
            if (entityVersions.containsKey(type)) {
                throw new MultipleVersionFieldDefinitionException();
            }
            entityVersions.put(type, versionField);
        }
    }

    public boolean contains(Class<?> type) {
        return entityVersions.containsKey(type);
    }

    public String get(Class<?> type) {
        return entityVersions.get(type);
    }

    public void reset() {
        entityVersions.clear();
    }

    public boolean isEmpty() {
        return entityVersions.isEmpty();
    }
}
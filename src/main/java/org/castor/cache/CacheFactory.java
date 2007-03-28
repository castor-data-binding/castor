/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
package org.castor.cache;

/**
 * A factory for instantiating Cache implementations. To provide an implementation 
 * for a specific cache type, please implement this interface.
 * 
 * When providing your own cache instance as explained in the JavaDocs for this 
 * package, please make sure that you provide valid values for the <b>name</b> and
 * <b>className</b> properties.  
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public interface CacheFactory {
    /**
     * Instantiates an instance of the cache implementation this factory is responsible
     * for using the given classloader.
     * 
     * @param classLoader A ClassLoader instance.
     * @return A Cache instance.
     * @throws CacheAcquireException Problem instantiating a cache instance.
     */
    Cache getCache(ClassLoader classLoader) throws CacheAcquireException;

    /**
     * Returns the short alias cache type for this factory instance.
     * 
     * @return The short alias cache type. 
     */
    String getCacheType();

    /**
     * Returns the full class name of the underlying cache implementation.
     * 
     * @return The full cache class name. 
     */
    String getCacheClassName();
    
    /**
     * Allows for cache-specific shutdown operations and resource cleanup.
     */
    void shutdown();
}

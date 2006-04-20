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
 *
 * $Id$
 */
package org.castor.cache;

import java.util.Properties;

/**
 * Base implementation of all LRU cache types. 
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractBaseCache implements Cache {
    //--------------------------------------------------------------------------

    /** Virtual name of this cache. Castor sets the cache name to the class name of the
     *  objects stored in the cache. */
    private String _cacheName = Cache.DEFAULT_NAME;

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * @see org.castor.cache.Cache#initialize(java.util.Map)
     */
    public void initialize(final Properties params) throws CacheAcquireException {
        String param = params.getProperty(Cache.PARAM_NAME, Cache.DEFAULT_NAME);
        _cacheName = (String) param;
    }

    /**
     * @see org.castor.cache.Cache#close()
     */
    public void close() { }

    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * @see Cache#getName()
     */
    public final String getName() { return _cacheName; }
    
    //--------------------------------------------------------------------------
    // additional operations of cache interface
    
    /**
     * @see org.castor.cache.Cache#expire(java.lang.Object)
     */
    public final void expire(final Object key) { remove(key); }
    
    /**
     * @see org.castor.cache.Cache#expireAll()
     */
    public final void expireAll() { clear(); }

    //--------------------------------------------------------------------------
}

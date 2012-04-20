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

import java.util.Properties;

/**
 * Base implementation of all cache types. 
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractBaseCache<K, V> implements Cache<K, V> {
    //--------------------------------------------------------------------------

    /** Virtual name of this cache. Castor sets the cache name to the class name of the
     *  objects stored in the cache. */
    private String _cacheName = Cache.DEFAULT_NAME;

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     */
    public void initialize(final Properties params) throws CacheAcquireException {
        String param = params.getProperty(Cache.PARAM_NAME, Cache.DEFAULT_NAME);
        _cacheName = param;
    }

    /**
     * {@inheritDoc}
     */
    public void close() { }

    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     */
    public final String getName() { return _cacheName; }
    
    //--------------------------------------------------------------------------
    // additional operations of cache interface
    
    /**
     * {@inheritDoc}
     */
    public final void expire(final Object key) { remove(key); }
    
    /**
     * {@inheritDoc}
     */
    public final void expireAll() { clear(); }

    //--------------------------------------------------------------------------
}

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
package org.castor.cache.distributed;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.castor.cache.AbstractBaseCache;

/**
 * Base implementation of all distributed cache types. 
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-05-05 13:53:54 -0600 (Fri, 05 May 2006) $
 * @since 1.0
 */
public abstract class AbstractDistributedCache extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The cache instance. */
    private Map<Object, Object> _cache = null;

    //--------------------------------------------------------------------------
    // getter/setter for cache
    
    /**
     * Get the cache instance.
     * 
     * @return The cache instance.
     */
    protected final Map<Object, Object> getCache() { return _cache; }
    
    /**
     * Set the cache instance.
     * 
     * @param cache The cache instance.
     */
    protected final void setCache(final Map<Object, Object> cache) {
        _cache = cache;
    }
    
    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     */
    public final int size() { return _cache.size(); }

    /**
     * {@inheritDoc}
     */
    public final boolean isEmpty() { return _cache.isEmpty(); }

    /**
     * {@inheritDoc}
     */
    public final boolean containsKey(final Object key) {
        return _cache.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean containsValue(final Object value) {
        return _cache.containsValue(value);
    }
    
    /**
     * {@inheritDoc}
     */
    public final Object get(final Object key) {
        return _cache.get(key);
    }

    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final Object put(final Object key, final Object value) {
        return _cache.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public final Object remove(final Object key) {
        return _cache.remove(key);
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final void putAll(final Map<? extends Object, ? extends Object> map) {
        _cache.putAll(map);
    }

    /**
     * {@inheritDoc}
     */
    public final void clear() { _cache.clear(); }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public final Set<Object> keySet() {
        return Collections.unmodifiableSet(_cache.keySet());
    }
    
    /**
     * {@inheritDoc}
     */
    public final Collection<Object> values() {
        return Collections.unmodifiableCollection(_cache.values());
    }

    /**
     * {@inheritDoc}
     */
    public final Set<Entry<Object, Object>> entrySet() {
        return Collections.unmodifiableSet(_cache.entrySet());
    }

    //--------------------------------------------------------------------------
}

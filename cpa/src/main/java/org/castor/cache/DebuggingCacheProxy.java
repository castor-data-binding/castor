/*
 * Copyright 2005 Ralf Joachim
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

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A debugging cache proxy.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class DebuggingCacheProxy implements Cache {
    //--------------------------------------------------------------------------
    
    /** The wrapped cache. */
    private Cache _cache;
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private Log _log;

    //--------------------------------------------------------------------------

    /**
     * Construct a DebugCacheProxy for given cache.
     * 
     * @param cache The wrapped cache.
     */
    public DebuggingCacheProxy(final Cache cache) { _cache = cache; }

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     */
    public void initialize(final Properties params) throws CacheAcquireException {
        _cache.initialize(params);
        _log = LogFactory.getLog(_cache.getClass());
        _log.debug(getType() + ".initialize() [" + getName() + "]");
    }
    
    /**
     * {@inheritDoc}
     */
    public void close() {
        _log.debug(getType() + ".close() [" + getName() + "]");
        _cache.close();
    }
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     */
    public String getType() { return _cache.getType(); }

    /**
     * {@inheritDoc}
     */
    public String getName() { return _cache.getName(); }
    
    //--------------------------------------------------------------------------
    // additional operations of cache interface
    
    /**
     * {@inheritDoc}
     */
    public void expire(final Object key) {
        _log.debug(getType() + ".expire(" + key + ") [" + getName() + "]");
        _cache.expire(key);
    }
    
    /**
     * {@inheritDoc}
     */
    public void expireAll() {
        _log.debug(getType() + ".expireAll() [" + getName() + "]");
        _cache.expireAll();
    }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     */
    public int size() {
        _log.debug(getType() + ".size() [" + getName() + "]");
        return _cache.size();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        _log.debug(getType() + ".isEmpty() [" + getName() + "]");
        return _cache.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        _log.debug(getType() + ".containsKey(" + key + ") [" + getName() + "]");
        return _cache.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        _log.debug(getType() + ".containsValue(" + value + ") [" + getName() + "]");
        return _cache.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object key) {
        _log.debug(getType() + ".get(" + key + ") [" + getName() + "]");
        return _cache.get(key);
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Object put(final Object key, final Object value) {
        _log.debug(getType() + ".put(" + key + ", " + value + ") [" + getName() + "]");
        return _cache.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object remove(final Object key) {
        _log.debug(getType() + ".remove(" + key + ") [" + getName() + "]");
        return _cache.remove(key);
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public void putAll(final Map < ? extends Object, ? extends Object > map) {
        _log.debug(getType() + ".putAll(" + map + ") [" + getName() + "]");
        _cache.putAll(map);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        _log.debug(getType() + ".clear() [" + getName() + "]");
        _cache.clear();
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Set < Object > keySet() {
        _log.debug(getType() + ".keySet() [" + getName() + "]");
        return _cache.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public Collection < Object > values() {
        _log.debug(getType() + ".values() [" + getName() + "]");
        return _cache.values();
    }

    /**
     * {@inheritDoc}
     */
    public Set < Entry < Object, Object > > entrySet() {
        _log.debug(getType() + ".entrySet() [" + getName() + "]");
        return _cache.entrySet();
    }

    //--------------------------------------------------------------------------
}

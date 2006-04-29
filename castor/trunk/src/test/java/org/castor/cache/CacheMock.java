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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cache to test AbstractBaseCache and DebuggingCacheProxy.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class CacheMock extends AbstractBaseCache {
    //--------------------------------------------------------------------------
    
    /** The type of the cache. */
    public static final String TYPE = "mock";
    
    /** The internal map. */
    private Map _map = new HashMap();
    
    //--------------------------------------------------------------------------
    
    /**
     * Default constructor.
     */
    public CacheMock() {
        // put some entries for testing into the cache
        _map.put("first key", "first value");
        _map.put("second key", "second value");
        _map.put("third key", "third value");
    }
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * @see org.castor.cache.Cache#getType()
     */
    public String getType() { return "dummy type"; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * @see java.util.Map#size()
     */
    public int size() { return _map.size(); }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() { return _map.isEmpty(); }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) { return _map.containsKey(key); }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) { return _map.containsValue(value); }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key) { return _map.get(key); }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value) {
        return _map.put(key, value);
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key) { return _map.remove(key); }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map map) { _map.putAll(map); }

    /**
     * @see java.util.Map#clear()
     */
    public void clear() { _map.clear(); }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * @see java.util.Map#keySet()
     */
    public Set keySet() { return _map.keySet(); }

    /**
     * @see java.util.Map#values()
     */
    public Collection values() { return _map.values(); }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() { return _map.entrySet(); }

    //--------------------------------------------------------------------------
}

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
package org.castor.cache.distributed;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Cache to test access to distributed caches (Coherence, FKCache and JCache)
 * without having their implementations available.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class DistributedCacheMock implements Map {
    //--------------------------------------------------------------------------
    
    /** Exception to be thrown for testing when calling any method. */
    private static Exception _exception = null;
    
    /** Counter to check execution of methods. */
    private static int _counter = 0;
    
    /** The internal map. */
    private Map _map;
    
    //--------------------------------------------------------------------------
    // methods to set test behaviour and check test execution
    
    /**
     * Set exception to be thrown at release method. If set to <code>null</code>
     * no exception will be thrown. 
     * 
     * @param exception The exception to throw.
     */
    public static void setException(final Exception exception) {
        _exception = exception;
    }
    
    /**
     * Get counter value.
     * 
     * @return The counter value.
     */
    public static int getCounter() { return _counter; }

    //--------------------------------------------------------------------------
    // additional methods of distributed cache implementations

    /**
     * CoherenceCache release() method.
     * 
     * @throws Exception For testing exception handling.
     */
    public void release() throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
    }
    
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

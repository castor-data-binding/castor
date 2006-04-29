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

import java.util.HashMap;

/**
 * Cache to test access to distributed caches (OsCache) without having their
 * implementations available.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class DistributedOsCacheMock {
    //--------------------------------------------------------------------------
    
    /** Exception to be thrown for testing when calling any method. */
    private static Exception _exception = null;
    
    /** Counter to check execution of methods. */
    private static int _counter = 0;
    
    /** String array to check groups passed to putInCache() and flushEntry(). */
    private static String[] _groups = null;
    
    /** The internal map. */
    private HashMap _map = new HashMap();
    
    //--------------------------------------------------------------------------
    // methods to set test behaviour
    
    public static void setException(final Exception exception) {
        _exception = exception;
    }

    public static int getCounter() { return _counter; }
    
    public static String[] getGroups() { return _groups; }
    
    //--------------------------------------------------------------------------
    // constructor adn destry method for OsCache
    
    public DistributedOsCacheMock() {
        // put some entries for testing into the cache
        _map.put("first key", "first value");
        _map.put("second key", "second value");
        _map.put("third key", "third value");
    }
    
    public void destroy() throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
    }

    //--------------------------------------------------------------------------
    // operations of JcsCache

    /**
     * Get value associated with given key from map.
     * 
     * @param key The key to search for.
     * @return The value associated with key.
     * @throws Exception For testing only.
     */
    public Object getFromCache(final String key) throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        return _map.get(key);
    }
    
    /**
     * Cancel update for given key if getFromCache() fails.
     * 
     * @param key The key to search for.
     * @throws Exception For testing only.
     */
    public void cancelUpdate(final String key) throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
    }
    
    /**
     * Put new association from given key to given value into map.
     * 
     * @param key The key to associate value with.
     * @param value The value to associate with key.
     * @throws Exception For testing only.
     */
    public void putInCache(final String key, final Object value, final String[] groups)
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _groups = groups;
        _map.put(key, value);
    }

    /**
     * Remove association for key from this map.
     * 
     * @param key The key whose association should be removed.
     * @throws Exception For testing only.
     */
    public void flushEntry(final String key) throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _map.remove(key);
    }
    
    /**
     * Clear all entries of the cache.
     * 
     * @throws Exception For testing only.
     */
    public void flushGroup(final String group) throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _groups = new String[] {group};
        _map.clear();
    }

    //--------------------------------------------------------------------------
}

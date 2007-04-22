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

/**
 * CacheFactory to test access to distributed caches without having their
 * implementations available.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class DistributedCacheFactoryMock {
    //--------------------------------------------------------------------------
    
    /** Exception to be thrown for testing when calling any method. */
    private static Exception _exception = null;
    
    /** Counter to check execution of methods. */
    private static int _counter = 0;
    
    /** Name of the cache instance. */
    private static String _name = null;
    
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
    
    /**
     * Get name of the cache instance.
     * 
     * @return Name of the cache instance.
     */
    public static String getName() { return _name; }

    //--------------------------------------------------------------------------
    // static factory methods for CacheFactory
    
    /**
     * FKCache and JCache getInstance() method.
     * 
     * @return An instance of DistributedCacheFactoryMock.
     * @throws Exception For testing exception handling.
     */
    public static DistributedCacheFactoryMock getInstance()
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _name = "dummy factory";
        return new DistributedCacheFactoryMock();
    }

    /**
     * JcsCache getInstance(String) method.
     * 
     * @return An instance of DistributedJcsCacheMock.
     * @throws Exception For testing exception handling.
     */
    public static DistributedJcsCacheMock getInstance(final String name)
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _name = name;
        return new DistributedJcsCacheMock();
    }

    //--------------------------------------------------------------------------
    // static factory and shutdown methods for Cache
    
    /**
     * CoherenceCache getCache(String) method.
     * 
     * @param name Name of the cache.
     * @return An instance of DistributedCacheMock.
     * @throws Exception For testing exception handling.
     */
    public static DistributedCacheMock getCache(final String name)
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _name = name;
        return new DistributedCacheMock();
    }
    
    /**
     * CoherenceCache shutdown() method.
     * 
     * @throws Exception For testing exception handling.
     */
    public static void shutdown()
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
    }
    
    //--------------------------------------------------------------------------
    // instance factory method for Cache
    
    /**
     * FKCache getMapAccess() method.
     * 
     * @return An instance of DistributedCacheMock.
     * @throws Exception For testing exception handling.
     */
    public DistributedCacheMock getMapAccess()
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _name = "dummy cache";
        return new DistributedCacheMock();
    }
    
    /**
     * JCache getMapAccess(String) method.
     * 
     * @param name Name of the cache.
     * @return An instance of DistributedCacheMock.
     * @throws Exception For testing exception handling.
     */
    public DistributedCacheMock getMapAccess(final String name)
    throws Exception {
        if (_exception != null) { throw _exception; }
        _counter++;
        _name = name;
        return new DistributedCacheMock();
    }

    //--------------------------------------------------------------------------
}

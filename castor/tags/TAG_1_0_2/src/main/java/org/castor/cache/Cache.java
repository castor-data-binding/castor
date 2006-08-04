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

import java.util.Map;
import java.util.Properties;

import org.castor.cache.simple.CountLimited;

/**
 * Interface specification for performance caches as used in Castor. Please implement 
 * this interface if you wish to provide your own cache implementation.
 * <p>
 * At initialization each cache implementation gets passed a properties map containing
 * key/value pairs. Apart of 3 reserved standard properties, individual once can be
 * used to configure the cache behavier. The standard properties are:
 * <p>
 * <b>type</b> which is evaluated by the CacheFactoryRegistry and defines the requested
 * cache type. If not set <b>count-limited</b> cahce will be used as default.
 * <br>
 * <b>debug</b> is also evaluated by the CacheFactoryRegistry and defines if the cache
 * instance will be wrapped by a DebuggingCacheProxy to log debug messages at every
 * access to the cache. If not set no debugging will take place.
 * <br>
 * <b>name</b> is used by AbstractBaseCache to set the name of the cache instance. At
 * the moment every cache type available extends this AbstractBaseCache. The name does
 * not influence internal behavier of the cache but is usefull to identify from which
 * cache instance debug messages are coming from. By default castor uses the classname
 * of the cached objects as name for the cache. If not present the name will be empty.
 * <p>
 * For a description of the individual properties you should have a look at the javadoc
 * of the different cache types. It needs to be noted that only string keys and values
 * are allowed.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public interface Cache extends Map {
    //--------------------------------------------------------------------------
    
    /** Mapped initialization parameter: type */
    String PARAM_TYPE = "type";
    
    /** Default cache type to be used. */
    String DEFAULT_TYPE = CountLimited.TYPE;
    
    /** Mapped initialization parameter: name */
    String PARAM_NAME = "name";
    
    /** Default cache name to be used. */
    String DEFAULT_NAME = "";
    
    /** Mapped initialization parameter: debug */
    String PARAM_DEBUG = "debug";
    
    /** Default is debugging switched off. */
    String DEFAULT_DEBUG = "false";
    
    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * Lyfe-cycle method to allow custom initialization of cache implementations.
     * 
     * @param params Parameters to initialize the cache (e.g. name, capacity).
     * @throws CacheAcquireException If cache can not be initialized.
     */
    void initialize(Properties params) throws CacheAcquireException;
    
    /** 
     * Life-cycle method to allow custom resource cleanup for a cache implementation.
     */
    void close();
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * Indicates the type of this cache.
     * 
     * @return The cache type.
     */
    String getType();

    /**
     * Get virtual name of this cache. Castor sets the cache name to the class name of the
     * objects stored in the cache.
     * 
     * @return The cache name.
     */
    String getName();
    
    //--------------------------------------------------------------------------
    // additional operations of cache interface
    
    /**
     * Remove the mapping identified by key from the cache.
     * 
     * @param key the key that needs to be removed.
     */
    void expire(Object key);
    
    /**
     * Removes all mappings from the cache.
     */
    void expireAll();

    //--------------------------------------------------------------------------
}

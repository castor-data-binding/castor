/*
 * Copyright 2005 Werner Guttmann
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

import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cache.CacheAcquireException;


/**
 * Gigaspaces implementation of Castor JDO Cache.
 * 
 * For more details of Coherence, see http://www.gigaspaces.com/ 
 * 
 * @see http://www.gigaspaces.com/  
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */

public final class GigaspacesCache extends AbstractDistributedCache {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(GigaspacesCache.class);
    
    /** The type of the cache. */
    public static final String TYPE = "gigaspaces";
    
    /** The classname of the implementations factory class. */
    public static final String IMPLEMENTATION = "com.j_spaces.map.CacheFinder";
    
    /** Parameter types for calling getCache() method on IMPLEMENTATION. */
    private static final Class[] TYPES_FIND_CACHE = new Class[] {String.class};

    /**
     * Default cache URL
     */
    private static final String DEFAULT_CACHE_URL = "/./";

    /**
     * Default cache properties
     */
    private static final String DEFAULT_CACHE_PROPERTIES = "";
    

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#initialize(java.util.Map)
     */
    public void initialize(final Properties params) throws CacheAcquireException {
        initialize(IMPLEMENTATION, params);
    }

    /**
     * Normally called to initialize CoherenceCache. To be able to test the method
     * without having <code>com.tangosol.net.CacheFactory</code> implementation,
     * it can also be called with a test implementations classname.
     * 
     * @param implementation Cache implementation classname to initialize.
     * @param params Parameters to initialize the cache (e.g. name, capacity).
     * @throws CacheAcquireException If cache can not be initialized.
     */
    public void initialize(final String implementation, final Properties params)
    throws CacheAcquireException {
        super.initialize(params);
        
        String cacheURL = params.getProperty("cacheURL", DEFAULT_CACHE_URL);
        String cacheProperties = params.getProperty("cacheProperties", DEFAULT_CACHE_PROPERTIES);
        
        StringBuffer clusterURL = new StringBuffer();
        clusterURL.append(cacheURL);
        clusterURL.append(getName());
        clusterURL.append("?");
        clusterURL.append(cacheProperties);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug(clusterURL.toString());
        }
        
        try {
            ClassLoader ldr = this.getClass().getClassLoader();
            Class cls = ldr.loadClass(implementation);
            setCache((Map) invokeStaticMethod(
                    cls, "find", TYPES_FIND_CACHE, new Object[] {clusterURL.toString()}));
        } catch (Exception e) {
            LOG.error("Problem!", e);
            String msg = "Error creating Gigaspaces cache: " + e.getMessage();
            LOG.error(msg, e);
            throw new CacheAcquireException(msg, e);
        }
    }
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#getType()
     */
    public String getType() { return TYPE; }

    //--------------------------------------------------------------------------
}

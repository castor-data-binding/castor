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
 * Gigaspaces implementation of Castor JDO Cache.<br><br>
 * 
 * Gigaspaces supports a wide variety of cache topologies, allowing the user
 * to distribute and/or replicate application data as needed. This cache instance
 * replicates this flexibility by allowing you to configure it (and thus the 
 * underlying Gigaspaces instance) as follows.<br>
 * 
 * <pre>
 * &lt;cache-type type="gigaspaces"&gt;
 *    &lt;cacheUrl&gt;/./&lt;/cacheURL&gt;
 *    &lt;cacheProperties&gt;schema=cache&lt;/cacheProperties&gt;
 * &lt;/cache-type&gt;
 * </pre>
 * 
 * As mentioned briefly above, the main issue is the cache topology usage. Per
 * definition, Gigaspaces caches can be started in various modes:<br><br>
 * 
 * <ul>
 *    <li><b>Embedded</b> – cache running as part of the application VM 
 *        (<tt>/./myCache?schema=cache</tt>)</li>
 *    <li><b>Remote</b> - means you need to run cache server and have relevant
 *        url at the client to connect to it (<tt>jini//&#042/&#042/myCache</tt>)</li>
 *    <li><b>Master local</b> - means you need to run a cache server and have relevant 
 *        url at the client to connect to it. The URL should include 
 *        '<tt>useLocalCachey</tt>' as part of it 
 *        (<tt>jini//&#042/&#042/myCache?useLocalCache</tt>)</li>
 * </ul><br>
 * 
 * Each of the above can run in <i>replicated</i> or <i>partitioned</i> mode. This 
 * means you should run several instance in one of the above mode using the relevant 
 * schema name, total_membres and id.<br><br>
 *
 * <p><i>instance 1</i>:<br>
 * "<tt>/./myCache?schema=cache&cluster_schema=replicated&total_members=2&id=1</tt>"</p>
 * <p><i>instance 2</i>:<br>
 * "<tt>/./myCache?schema=cache&cluster_schema=replicated&total_members=2&id=2</tt>"</p><br>
 *
 * or<br><br>
 *
 * <p><i>instance 1</i>:<br>
 * "<tt>/./myCache?schema=cache&cluster_schema=partitioned&total_members=2&id=1</tt>"</p>
 * <p><i>instance 2</i>:<br>
 * "<tt>/./myCache?schema=cache&cluster_schema=partitioned&total_members=2&id=2</tt>"</p><br>
 *
 * <p>When running the cache in server or in embedded mode, you <b>must</b> have the 
 * cache schema to be used – i.e. '<tt>schema=cache</tt>'.</p><br>
 *
 * For more information on cache topoligies and the use of URLs with Gigaspaces, 
 * please see 
 * <a href="http://www.gigaspaces.com/docs/manual/The_Space_URL.htm">here</a>.<br>
 * 
 * For more details on Gigaspaces in gernal, please see http://www.gigaspaces.com/.<br> 
 * 
 * @see http://www.gigaspaces.com/
 * @see http://www.gigaspaces.com/wiki  
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

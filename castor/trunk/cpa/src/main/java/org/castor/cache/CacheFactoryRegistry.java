/*
 * Copyright 2005 Bruce Snyder, Werner Guttmann, Ralf Joachim
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
import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;

/**
 * Registry for {@link CacheFactory} implementations obtained from the Castor
 * properties file and used by the JDO mapping configuration file.
 * 
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class CacheFactoryRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(CacheFactoryRegistry.class);
    
    /** Name of the proxy class. */
    private static final String PROXY_CLASSNAME = DebuggingCacheProxy.class.getName();
    
    /** Association between {@link Cache} name and factory implementation. */
    private Hashtable  _cacheFactories = new Hashtable();
    
    //--------------------------------------------------------------------------

    /**
     * Construct an instance of CacheFactoryRegistry that uses given Configuration
     * to get required configuration properties.
     * 
     * @param config The Configuration.
     */
    public CacheFactoryRegistry(final Configuration config) {
        Object[] objects = config.getObjectArray(
                CPAConfiguration.CACHE_FACTORIES, config.getApplicationClassLoader());
        for (int i = 0; i < objects.length; i++) {
            CacheFactory factory = (CacheFactory) objects[i];
            _cacheFactories.put(factory.getCacheType(), factory);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns a {@link Cache} instance with the specified properties.
     * <p>
     * The type of the returned cache is taken from the <b>type</b> property. If not
     * specified a <b>count-limited</b> cache will be returned. If the type of the
     * cache specified is unknown a CacheAcquireException will be thrown.
     * <p>
     * If the given properties contain a <b>debug</b> property set to <b>true</b> or if
     * debugging for the selected cache type is enabled, the returned cache will be
     * wrapped by a DebuggingCacheProxy. This proxy will output debug messages to the
     * log if logging for the Cache interface is enabled through the logging system.
     *
     * @param props Properties to initialize the cache with.
     * @param classLoader A ClassLoader instance.
     * @return A {@link Cache} instance.
     * @throws CacheAcquireException A cache of the type specified can not be acquired.
     */
    public Cache getCache(final Properties props, final ClassLoader classLoader) 
    throws CacheAcquireException {
        String cacheType = props.getProperty(Cache.PARAM_TYPE, Cache.DEFAULT_TYPE);
        CacheFactory cacheFactory = (CacheFactory) _cacheFactories.get(cacheType);
        if (cacheFactory == null) {
            LOG.error("Unknown cache type '" + cacheType + "'");
            throw new CacheAcquireException("Unknown cache type '" + cacheType + "'");
        }
        
        Cache cache = cacheFactory.getCache(classLoader);
        
        String prop = props.getProperty(Cache.PARAM_DEBUG, Cache.DEFAULT_DEBUG);
        boolean objectDebug = Boolean.valueOf(prop).booleanValue();
        boolean cacheDebug = LogFactory.getLog(Cache.class).isDebugEnabled();
        boolean cacheTypeDebug = LogFactory.getLog(cache.getClass()).isDebugEnabled();
        if (cacheTypeDebug || (cacheDebug && objectDebug)) {
            try {
                ClassLoader loader = CacheFactoryRegistry.class.getClassLoader();
                Class cls = loader.loadClass(PROXY_CLASSNAME);
                Class[] types = new Class[] {Cache.class};
                Object[] params = new Object[] {cache};
                cache = (Cache) cls.getConstructor(types).newInstance(params);
            } catch (Exception e) {
                String msg = "Error creating instance of: " + PROXY_CLASSNAME;
                LOG.error(msg, e);
                throw new CacheAcquireException(msg, e);
            }
        }
        
        cache.initialize(props);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully instantiated '" + cacheType + "' cache: "
                    + props.get(Cache.PARAM_NAME));
        }
        
        return cache;
    }
    
    /**
     * Returns a collection of the current configured cache factories.
     * 
     * @return Collection of the current configured cache factories.
     */
    public Collection getCacheFactories() {
        return Collections.unmodifiableCollection(_cacheFactories.values());
    }
    
    /**
     * Returns a collection of the current configured cache factory names.
     *
     * @return Names of the configured cache factories.
     */
    public Collection getCacheNames() {
        return Collections.unmodifiableCollection(_cacheFactories.keySet());
    }
    
    //--------------------------------------------------------------------------
}

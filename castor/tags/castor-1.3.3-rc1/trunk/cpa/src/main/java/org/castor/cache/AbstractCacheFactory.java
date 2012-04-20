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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base implementation of {@link CacheFactory}. Users interested in supplying their
 * own cache implementations might want to extend this class to provide their
 * own {@link CacheFactory} instance. 
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractCacheFactory<K, V> implements CacheFactory<K, V> {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractCacheFactory.class);
    
    /** Has the cache factory been initialized? */
    private boolean _initialized = false;
    
    /**
     * Has the cache factory been initialized?
     * 
     * @return <code>true</code> if cache factory has been initialized,
     *         <code>false</code> otherwise.
     */
    protected final boolean isInitialized() { return _initialized; }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public final synchronized Cache<K, V> getCache(final ClassLoader classLoader)
    throws CacheAcquireException {
        ClassLoader loader = classLoader;
        if (loader == null) { loader = Thread.currentThread().getContextClassLoader(); }
        
        Cache<K, V> cache = null;
        try {
            cache = (Cache<K, V>) loader.loadClass(getCacheClassName()).newInstance();
        } catch (Exception e) {
            String msg = "Error creating cache instance of: " + getCacheClassName();
            LOG.error(msg, e);
            throw new CacheAcquireException(msg, e);
        }
        
        _initialized = true;
        
        return cache;
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() { }
}

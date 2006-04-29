/*
 * Copyright 2005 Tim Telcik, Werner Guttmann, Ralf Joachim
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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.AbstractCacheFactory;

/**
 * Implements {@link CacheFactory} for the {@link CoherenceCache} implementation 
 * of {@link Cache}.
 *
 * @author <a href="mailto:ttelcik AT hbf DOT com DOT au">Tim Telcik</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class CoherenceCacheFactory extends AbstractCacheFactory {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(CoherenceCacheFactory.class);

    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheType()
     */
    public String getCacheType() { return CoherenceCache.TYPE; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheClassName()
     */
    public String getCacheClassName() { return CoherenceCache.class.getName(); }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#shutdown()
     */
    public void shutdown() { shutdown(CoherenceCache.IMPLEMENTATION); }

    /**
     * Normally called to shutdown CoherenceCache. To be able to test the method
     * without having <code>com.tangosol.net.CacheFactory</code> implementation,
     * it can also be called with a test implementations classname.
     * 
     * @param implementation Cache implementation classname to shutdown.
     */
    public void shutdown(final String implementation) {
        try {
            ClassLoader ldr = this.getClass().getClassLoader();
            Class cls = ldr.loadClass(implementation);
            if (cls != null) {
                Method method = cls.getMethod("shutdown", null);
                method.invoke(null, null);
            }
        } catch (Exception e) {
            LOG.error("Problem shutting down Coherence cluster member", e);
        }
    }
}

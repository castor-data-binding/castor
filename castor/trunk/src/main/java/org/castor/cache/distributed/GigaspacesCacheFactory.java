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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.AbstractCacheFactory;

/**
 * Implements {@link org.castor.cache.CacheFactory} for the {@link GigaspacesCache}
 * implementation of {@link org.castor.cache.Cache}.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class GigaspacesCacheFactory extends AbstractCacheFactory {
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(GigaspacesCacheFactory.class);

    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheType()
     */
    public String getCacheType() { return GigaspacesCache.TYPE; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheClassName()
     */
    public String getCacheClassName() { return GigaspacesCache.class.getName(); }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#shutdown()
     */
    public void shutdown() { shutdown(GigaspacesCache.IMPLEMENTATION); }

    /**
     * Normally called to shutdown GigaspacesCache. To be able to test the method
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
            LOG.error("Problem shutting down Gigaspaces cluster member", e);
        }
    }
}

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
package org.castor.cache.distributed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;

/**
 * Implements {@link CacheFactory} for the {@link OsCache} implementation of 
 * {@link org.castor.cache.Cache}.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class OsCacheFactory implements CacheFactory {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(OsCacheFactory.class);

    /** The cache instance for all caches of this factory. */
    private Object _cache = null;
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCache(java.lang.ClassLoader)
     */
    public Cache getCache(final ClassLoader classLoader)
    throws CacheAcquireException {
        return getCache(OsCache.IMPLEMENTATION, classLoader);
    }
    
    /**
     * Normally called to initialize OsCache. To be able to test the method without
     * having <code>com.opensymphony.oscache.general.GeneralCacheAdministrator</code>
     * implementation, it can also be called with a test implementations classname.
     * 
     * @param implementation Cache implementation classname to initialize.
     * @param classLoader A ClassLoader instance.
     * @return A Cache instance.
     * @throws CacheAcquireException Problem instantiating a cache instance.
     */
    public Cache getCache(final String implementation, final ClassLoader classLoader)
    throws CacheAcquireException {
        ClassLoader loader = classLoader;
        if (loader == null) { loader = Thread.currentThread().getContextClassLoader(); }
        
        if (_cache == null) {
            try {
                _cache = loader.loadClass(implementation).newInstance();
            } catch (ClassNotFoundException cnfe) {
                String msg = "Cannot find class " + implementation + ".";
                LOG.error(msg, cnfe);
                throw new CacheAcquireException(msg, cnfe);
            } catch (IllegalAccessException iae) {
                String msg = "Illegal access with class " + implementation + ".";
                LOG.error(msg, iae);
                throw new CacheAcquireException(msg, iae);
            } catch (InstantiationException ie) {
                String msg = "Cannot create instance of " + implementation + ".";
                LOG.error(msg, ie);
                throw new CacheAcquireException(msg, ie);
            }
        }
        
        Cache cache = null;
        try {
            Class cls = loader.loadClass(getCacheClassName());
            Constructor cst = cls.getConstructor(new Class[] {Object.class});
            cache = (Cache) cst.newInstance(new Object[] {_cache});
        } catch (ClassNotFoundException cnfe) {
            String msg = "Cannot find class " + getCacheClassName() + ".";
            LOG.error(msg, cnfe);
            throw new CacheAcquireException(msg, cnfe);
        } catch (NoSuchMethodException nsme) {
            String msg = "NoSuchMethodException";
            LOG.error(msg, nsme);
            throw new CacheAcquireException(msg, nsme);
        } catch (IllegalAccessException iae) {
            String msg = "Illegal access with class " + getCacheClassName() + ".";
            LOG.error(msg, iae);
            throw new CacheAcquireException(msg, iae);
        } catch (InstantiationException ie) {
            String msg = "Cannot create instance of " + getCacheClassName() + ".";
            LOG.error(msg, ie);
            throw new CacheAcquireException(msg, ie);
        } catch (InvocationTargetException ite) {
            String msg = "InvocationTargetException";
            LOG.error(msg, ite);
            throw new IllegalStateException(ite.getMessage()); 
        }
        
        return cache;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheType()
     */
    public String getCacheType() { return OsCache.TYPE; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#getCacheClassName()
     */
    public String getCacheClassName() { return OsCache.class.getName(); }

    /**
     * {@inheritDoc}
     * @see org.castor.cache.CacheFactory#shutdown()
     */
    public void shutdown() {
        synchronized (_cache) {
            invokeMethod(_cache, "destroy", null, null);
        }
    }

    /**
     * Invoke method with given name and arguments having parameters of types
     * specified on the given target. Any possible exception will be catched and
     * IllegalStateException will be thrown instead.
     * 
     * @param target The target object to invoke the method on.
     * @param name The name of the method to invoke.
     * @param types The types of the parameters.
     * @param arguments The parameters.
     * @return The result of the method invocation.
     */
    private Object invokeMethod(final Object target, final String name,
            final Class[] types, final Object[] arguments) {
        try {
            Method method = target.getClass().getMethod(name, types);
            return method.invoke(target, arguments);
        } catch (SecurityException e) {
            LOG.error("SecurityException", e);
            throw new IllegalStateException(e.getMessage());
        } catch (NoSuchMethodException e) {
            LOG.error("NoSuchMethodException", e);
            throw new IllegalStateException(e.getMessage()); 
        } catch (IllegalArgumentException e) {
            LOG.error("IllegalArgumentException", e);
            throw new IllegalStateException(e.getMessage()); 
        } catch (IllegalAccessException e) {
            LOG.error("IllegalAccessException", e);
            throw new IllegalStateException(e.getMessage()); 
        } catch (InvocationTargetException e) {
            LOG.error("InvocationTargetException", e);
            throw new IllegalStateException(e.getMessage()); 
        }
    }
}

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Base implementation of all LRU cache types. 
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-05-05 13:53:54 -0600 (Fri, 05 May 2006) $
 * @since 1.0
 */
public abstract class AbstractBaseCache implements Cache {
    //--------------------------------------------------------------------------

    /** Virtual name of this cache. Castor sets the cache name to the class name of the
     *  objects stored in the cache. */
    private String _cacheName = Cache.DEFAULT_NAME;

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#initialize(java.util.Properties)
     */
    public void initialize(final Properties params) throws CacheAcquireException {
        String param = params.getProperty(Cache.PARAM_NAME, Cache.DEFAULT_NAME);
        _cacheName = param;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#close()
     */
    public void close() { }

    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     * @see Cache#getName()
     */
    public final String getName() { return _cacheName; }
    
    //--------------------------------------------------------------------------
    // additional operations of cache interface
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#expire(java.lang.Object)
     */
    public final void expire(final Object key) { remove(key); }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#expireAll()
     */
    public final void expireAll() { clear(); }

        /**
         * Invoke static method with given name and arguments having parameters of
         * types specified on the given target.
         * 
         * @param target The target object to invoke the method on.
         * @param name The name of the method to invoke.
         * @param types The types of the parameters.
         * @param arguments The parameters.
         * @return The result of the method invokation.
         * @throws NoSuchMethodException If a matching method is not found or if the
         *         name is "&lt;init&gt;"or "&lt;clinit&gt;".
         * @throws IllegalAccessException If this Method object enforces Java language
         *         access control and the underlying method is inaccessible.
         * @throws InvocationTargetException If the underlying method throws an exception.
         */
        protected final Object invokeStaticMethod(final Class target, 
                final String name, 
                final Class[] types, 
                final Object[] arguments) 
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
        {
            Method method = target.getMethod(name, types);
            return method.invoke(null, arguments);
        }
    
        /**
         * Invoke method with given name and arguments having parameters of types
         * specified on the given target.
         * 
         * @param target The target object to invoke the method on.
         * @param name The name of the method to invoke.
         * @param types The types of the parameters.
         * @param arguments The parameters.
         * @return The result of the method invokation.
         * @throws NoSuchMethodException If a matching method is not found or if the
         *         name is "&lt;init&gt;"or "&lt;clinit&gt;".
         * @throws IllegalAccessException If this Method object enforces Java language
         *         access control and the underlying method is inaccessible.
         * @throws InvocationTargetException If the underlying method throws an exception.
         */
        protected final Object invokeMethod(final Object target, 
                final String name, 
                final Class[] types, 
                final Object[] arguments) 
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
        {
            Method method = target.getClass().getMethod(name, types);
            return method.invoke(target, arguments);
        }
    //--------------------------------------------------------------------------
}

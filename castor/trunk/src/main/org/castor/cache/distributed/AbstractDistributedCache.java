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
 *
 * $Id$
 */
package org.castor.cache.distributed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.castor.cache.AbstractBaseCache;

/**
 * Base implementation of all distributed cache types. 
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractDistributedCache extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The cache instance. */
    private Map _cache = null;

    //--------------------------------------------------------------------------
    // getter/setter for cache
    
    /**
     * Get the cache instance.
     * 
     * @return The cache instance.
     */
    protected final Map getCache() { return _cache; }
    
    /**
     * Set the cache instance.
     * 
     * @param cache The cache instance.
     */
    protected final void setCache(final Map cache) { _cache = cache; }
    
    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     * @see java.util.Map#size()
     */
    public final int size() { return _cache.size(); }

    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public final boolean isEmpty() { return _cache.isEmpty(); }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public final boolean containsKey(final Object key) {
        return _cache.containsKey(key);
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public final boolean containsValue(final Object value) {
        return _cache.containsValue(value);
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#get(java.lang.Object)
     */
    public final Object get(final Object key) {
        return _cache.get(key);
    }

    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public final Object put(final Object key, final Object value) {
        return _cache.put(key, value);
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#remove(java.lang.Object)
     */
    public final Object remove(final Object key) {
        return _cache.remove(key);
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#putAll(java.util.Map)
     */
    public final void putAll(final Map map) { _cache.putAll(map); }

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public final void clear() { _cache.clear(); }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public final Set keySet() {
        return Collections.unmodifiableSet(_cache.keySet());
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public final Collection values() {
        return Collections.unmodifiableCollection(_cache.values());
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public final Set entrySet() {
        return Collections.unmodifiableSet(_cache.entrySet());
    }

    //--------------------------------------------------------------------------
    // helper methods

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
     *         name is "<init>"or "<clinit>".
     * @throws IllegalAccessException If this Method object enforces Java language
     *         access control and the underlying method is inaccessible.
     * @throws InvocationTargetException If the underlying method throws an exception.
     */
    protected final Object invokeStaticMethod(final Class target,
            final String name, final Class[] types, final Object[] arguments) 
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
     *         name is "<init>"or "<clinit>".
     * @throws IllegalAccessException If this Method object enforces Java language
     *         access control and the underlying method is inaccessible.
     * @throws InvocationTargetException If the underlying method throws an exception.
     */
    protected final Object invokeMethod(final Object target,
            final String name, final Class[] types, final Object[] arguments) 
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = target.getClass().getMethod(name, types);
        return method.invoke(target, arguments);
    }
    
    //--------------------------------------------------------------------------
}

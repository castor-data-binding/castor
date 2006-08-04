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
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.AbstractBaseCache;
import org.castor.cache.CacheAcquireException;

/**
 * OSCache (opensymphony) implementation of Castor JDO Cache.
 * 
 * For more details of OSCache, see http://www.opensymphony.com/oscache
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class OsCache extends AbstractBaseCache {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(OsCache.class);

    /** The type of the cache. */
    public static final String TYPE = "oscache";
    
    /** The classname of the implementations cache class. */
    public static final String IMPLEMENTATION =
        "com.opensymphony.oscache.general.GeneralCacheAdministrator";
    
    /** Classname of exception thrown by getFromCache() method of oscache. */
    public static final String NEEDS_REFRESH_EXCEPTION =
        "com.opensymphony.oscache.base.NeedsRefreshException";
  
    /** Parameter types for calling getFromCache() method on cache instance. */
    private static final Class[] TYPES_GET = new Class[] {String.class};
    
    /** Parameter types for calling cancelUpdate() method on cache instance. */
    private static final Class[] TYPES_CANCEL = TYPES_GET;
    
    /** Parameter types for calling putInCache() method on cache instance. */
    private static final Class[] TYPES_PUT =
        new Class[] {String.class, Object.class, String[].class};
    
    /** Parameter types for calling flushEntry() method on cache instance. */
    private static final Class[] TYPES_REMOVE = TYPES_GET;
    
    /** Parameter types for calling flushGroup() method on cache instance. */
    private static final Class[] TYPES_CLEAR = TYPES_GET;
    
    /** The cache instance. */
    private final Object _cache;

    /** The method to invoke on cache instead of calling getFromCache() directly. */
    private Method _getMethod;

    /** The method to invoke on cache instead of calling cancelUpdate() directly. */
    private Method _cancelMethod;

    /** The method to invoke on cache instead of calling putInCache() directly. */
    private Method _putMethod;

    /** The method to invoke on cache instead of calling flushEntry() directly. */
    private Method _removeMethod;

    /** The method to invoke on cache instead of calling flushGroup() directly. */
    private Method _clearMethod;
    
    /** The string array of groups which every entry of this cache belongs to. */
    private String[] _groups;

    //--------------------------------------------------------------------------
    
    /**
     * Construct an instance of OsCache that wrapps access to given cache implementation
     * of class <code>com.opensymphony.oscache.general.GeneralCacheAdministrator</code>.
     * 
     * @param cache The cache to be wrapped.
     */
    public OsCache(final Object cache) { _cache = cache; }
    
    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#initialize(java.util.Map)
     */
    public void initialize(final Properties params) throws CacheAcquireException {
        super.initialize(params);

        Class cls = _cache.getClass();
        try {
            _getMethod = cls.getMethod("getFromCache", TYPES_GET);
            _cancelMethod = cls.getMethod("cancelUpdate", TYPES_CANCEL);
            _putMethod = cls.getMethod("putInCache", TYPES_PUT);
            _removeMethod = cls.getMethod("flushEntry", TYPES_REMOVE);
            _clearMethod = cls.getMethod("flushGroup", TYPES_CLEAR);
        } catch (Exception e) {
            String msg = "Failed to find method on OSCache instance: " + e.getMessage();
            LOG.error(msg, e);
            throw new CacheAcquireException(msg, e);
        }
        
        _groups = new String[] {getName()};
    }

    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     * @see org.castor.cache.Cache#getType()
     */
    public String getType() { return TYPE; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     * @see java.util.Map#size()
     */
    public int size() {
        throw new UnsupportedOperationException("size()");
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("isEmpty()");
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        return (get(key) != null);
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) {
        throw new UnsupportedOperationException("containsValue(Object)");
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key) {
        try {
            return _getMethod.invoke(_cache, new Object[] {String.valueOf(key)});
        } catch (InvocationTargetException e) {
            if (e.getCause().getClass().getName().equals(NEEDS_REFRESH_EXCEPTION)) {
                invokeCacheMethod(_cancelMethod, new Object[] {String.valueOf(key)});
                return null;
            }

            String msg = "Failed to call method on OSCache instance: " + e.getMessage();
            LOG.error(msg, e);
            throw new IllegalStateException(e.getMessage());
        } catch (Exception e) {
            String msg = "Failed to call method on OSCache instance: " + e.getMessage();
            LOG.error(msg, e);
            throw new IllegalStateException(e.getMessage());
        }
    }

    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value) {
        Object oldValue = get(key);
        invokeCacheMethod(_putMethod, new Object[] {String.valueOf(key), value, _groups});
        return oldValue;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key) {
        Object oldValue = get(key);
        invokeCacheMethod(_removeMethod, new Object[] {String.valueOf(key)});
        return oldValue;
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map map) {
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = String.valueOf(entry.getKey());
            invokeCacheMethod(_putMethod, new Object[] {key, entry.getValue(), _groups});
        }
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public void clear() {
        invokeCacheMethod(_clearMethod, new Object[] {getName()});
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        throw new UnsupportedOperationException("keySet()");
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public Collection values() {
        throw new UnsupportedOperationException("values()");
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        throw new UnsupportedOperationException("entrySet()");
    }
    
    //--------------------------------------------------------------------------
    // helper methods

    /**
     * Invoke given method on cache with given arguments. Any possible exception will
     * be catched and IllegalStateException will be thrown instead.
     * 
     * @param method The method to call on cache.
     * @param arguments The parameters.
     * @return The result of the method invocation.
     */
    private Object invokeCacheMethod(final Method method, final Object[] arguments) {
        try {
            return method.invoke(_cache, arguments);
        } catch (Exception e) {
            String msg = "Failed to call method on OSCache instance: " + e.getMessage();
            LOG.error(msg, e);
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------
}

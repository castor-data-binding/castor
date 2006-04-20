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
 *
 * $Id$
 */
package org.castor.persist.cache.distributed;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.persist.cache.AbstractBaseCache;
import org.exolab.castor.persist.cache.CacheAcquireException;


/**
* JCACHE implementation of Castor JDO Cache.
* 
* JCACHE is the Java Temporary Caching API (JSR-107).
* 
* NOTE: While this cache implementation should work any JCACHE-compliant
* provider, it is currently intended to work with the FKache open source
* reference implementation. 
* 
* For more details of JCACHE, see http://www.jcp.org/en/jsr/detail?id=107
* For more details of FKCache, see http://jcache.sourceforge.net 
* 
* @author <a href="ttelcik@hbf.com.au">Tim Telcik</a>
* @version $Revision$ $Date$
* 
* @see javax.util.jcache.CacheMap
* @see javax.util.jcache.CacheAccessFactory
*/
public class FKCache extends AbstractBaseCache {

    // private CacheMap cache = null;
    private Object _cache = null;

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(FKCache.class);

    /**
     * Creates an instance of this class.
     * @throws CacheAcquireException If this cache instance cannot be instantiated 
     * successfully.
     */
    public FKCache() {
        super();
    }

    /**
     * Maps the specified <code>key</code> to the specified 
     * <code>value</code> in this Map. Neither the key nor the 
     * value can be <code>null</code>. 
     * <p>
     * The value can be retrieved by calling the <code>get</code> method 
     * with a key that is equal to the original key.
     * <p>
     * @param      key     the Map key.
     * @param      value   the value.
     * @return     the previous value of the specified key in this Map,
     *             or <code>null</code> if it did not have one.
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>.
     */
    public Object put(final Object key, final Object value) {
        if (_log.isDebugEnabled()) {
            _log.debug("Creating cache entry for key " + key + " with value " + value);
        }
        return invokeMethod (this._cache, 
                "put", 
                new Class[] {Object.class, Object.class}, 
                new Object[] {key, value});
    }

    /**
     *Returns the value to which the specified key is mapped in this Map.
     *@param key - a key in the Map.
     *@return the value to which the key is mapped in this Map; null if 
     * the key is not mapped to any value in this Map.
     */
    public Object get(final Object key) {
        if (_log.isDebugEnabled()) {
            _log.debug("Getting cache entry for key " + key);
        }
        return invokeMethod (this._cache, 
                "get", 
                new Class[] {Object.class}, 
                new Object[] {key});
    }

    /**
     * Removes the key (and its corresponding value) from this 
     * Map. This method does nothing if the key is not in the Map.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this Map,
     *          or <code>null</code> if the key did not have a mapping.
     */
    public Object remove(final Object key) {
        if (_log.isDebugEnabled()) {
            _log.debug("Removing cache entry for key " + key);
        }
        return invokeMethod (this._cache, 
                "remove", 
                new Class[] {Object.class}, 
                new Object[] {key});
    }

    /**
     * Returns an enumeration of the values in this LRU map.
     * Use the Enumeration methods on the returned object to fetch the elements
     * sequentially.
     *
     * @return  an enumeration of the values in this Map.
     * @see     java.util.Enumeration
     */
    public Enumeration elements() {
        Set set = (Set) invokeMethod (this._cache, "keySet", null, null);
        return Collections.enumeration(set);
    }

    /**
     *  Indicates whether the cache holds a valuze object for the specified key.
     * @see org.exolab.castor.persist.cache.Cache#contains(java.lang.Object)
     */
    public boolean contains(final Object key) {
        if (_log.isDebugEnabled()) {
            _log.debug("Testing for entry for key " + key);
        }
        return invokeMethodReturnBoolean (this._cache, 
                "containsKey", 
                new Class[] {Object.class}, 
                new Object[] {key});
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#size()
     */
    public int size() {
        return invokeMethodReturnInt (this._cache, "size", null, null);
   }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#clear()
     */
    public void clear() {
        if (_log.isDebugEnabled()) {
            _log.debug("Clearing cache of its entries.");
        }
        invokeMethod (this._cache, "clear", null, null);
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#isEmpty()
     */
    public boolean isEmpty() {
       return invokeMethodReturnBoolean (this._cache, "isEmpty", null, null);
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        return invokeMethodReturnBoolean (this._cache, 
                "containsKey", 
                new Class[] {Object.class}, 
                new Object[] {key});
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) {
        return invokeMethodReturnBoolean (this._cache, 
                "containsValue", 
                new Class[] {Object.class}, 
                new Object[] {value});
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#values()
     */
    public Collection values() {
        Collection collection = 
            (Collection) invokeMethod (this._cache, "values", null, null);
        return Collections.unmodifiableCollection(collection);
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#putAll(java.util.Map)
     */
    public void putAll(final Map aMap) {
        invokeMethod (this._cache, 
                "putAll", 
                new Class[] {Map.class}, 
                new Object[] {aMap});
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#entrySet()
     */
    public Set entrySet() {
        Set set = (Set) invokeMethod (this._cache, "entrySet", null, null);
        return Collections.unmodifiableSet(set);
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#keySet()
     */
    public Set keySet() {
        Set set = (Set) invokeMethod (this._cache, "keySet", null, null);
        return Collections.unmodifiableSet(set);
    }
    
    public void initialize() throws CacheAcquireException {
        try {
            // CacheAccessFactory factory = CacheAccessFactory.getInstance();
            
            // Object factory = Class.forName("javax.util.jcs.CacheAccessFactory", 
            //    false, classLoader);
            // TODO [WG]: use other forName (...);
            Class factoryClass = Class.forName("javax.util.jcache.CacheAccessFactory");
            
            Object factory = 
                invokeStaticMethodWithExceptions (factoryClass, 
                        "getInstance", 
                        null, 
                        null); 
            
            // this.cache = factory.getMapAccess( this.getClassName() );
            // TODO [WG]: can entity class name be passed to getMapAccess() as region
//            this._cache = invokeMethodWithExceptions (factory, 
//                    "getMapAccess", 
//                    new Class[] {String.class}, 
//                    new Object[] {this.getClassName()});
            this._cache = invokeMethodWithExceptions (factory, 
                    "getMapAccess", 
                    null, null); 
                    
        } catch (ClassNotFoundException e) {
            String msg = "Error creating cache: " + e.getMessage();
            _log.error(msg, e);
            throw new CacheAcquireException(msg);
        } catch (SecurityException e) {
            String msg = "Error creating cache: " + e.getMessage();
            _log.error(msg, e);
            throw new CacheAcquireException(msg);
        } catch (NoSuchMethodException e) {
            String msg = "Error creating cache: " + e.getMessage();
            _log.error(msg, e);
            throw new CacheAcquireException(msg);
        } catch (IllegalArgumentException e) {
            String msg = "Error creating cache: " + e.getMessage();
            _log.error(msg, e);
            throw new CacheAcquireException(msg);
        } catch (IllegalAccessException e) {
            String msg = "Error creating cache: " + e.getMessage();
            _log.error(msg, e);
            throw new CacheAcquireException(msg);
        } catch (InvocationTargetException e) {
            String msg = "Error creating cache: " + e.getMessage();
            _log.error(msg, e);
            throw new CacheAcquireException(msg);
        }
    }

    public void close() {
        _log.debug ("Closing " + getCacheType() + "instance for " + getClassName());
    }
    
}

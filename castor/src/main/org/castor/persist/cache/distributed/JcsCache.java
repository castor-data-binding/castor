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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.persist.cache.AbstractBaseCache;
import org.exolab.castor.persist.cache.CacheAcquireException;
import org.exolab.castor.persist.cache.MethodNotImplementedException;


/**
* JCS (Java Caching System) implementation of Castor JDO Cache.
* 
* For more details of JCS, see http://jakarta.apache.org/jcs
* 
* @author <a href="ttelcik@hbf.com.au">Tim Telcik</a>
* @version $Revision$ $Date$
* @see org.apache.jcs.JCS
*/
public class JcsCache extends AbstractBaseCache {

    private Object _cache = null;
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = 
        LogFactory.getFactory().getInstance(JcsCache.class);

    /**
     * Constructor
     * @throws CacheAcquireException If this cache implementation cannot be 
     * instantiated successfully.
     */
    public JcsCache() {
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
        Object oldValue = invokeMethod (this._cache, 
                "get", 
                new Class[] {Object.class}, 
                new Object[] {key});
        invokeMethod (this._cache, 
                "put", 
                new Class[] {Object.class, Object.class}, 
                new Object[] {key, value});
        return oldValue;
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
        return invokeMethod (this._cache, "get", 
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
        Object value = invokeMethod (this._cache, 
                "get", 
                new Class[] {Object.class}, 
                new Object[] {key});
        invokeMethod (this._cache, 
                "remove", 
                new Class[] {Object.class},
                new Object[] {key});
        return value;
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
        throw new UnsupportedOperationException("Method elements() is not supported");
    }

    /**
     *  Indicates whether the cache holds a valuze object for the specified key.
     * @see org.exolab.castor.persist.cache.Cache#contains(java.lang.Object)
     */
    public boolean contains(final Object key) {
        if (_log.isDebugEnabled()) {
            _log.debug("Testing for entry for key " + key);
        }
        Object value = invokeMethod (this._cache, 
                "get", 
                new Class[] {Object.class}, 
                new Object[] {key});
        return (value != null);
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#size()
     */
    public int size() throws MethodNotImplementedException {
        throw new MethodNotImplementedException ("size()");
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#clear()
     */
    public void clear() throws MethodNotImplementedException {
        throw new MethodNotImplementedException ("clear()");
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#isEmpty()
     */
    public boolean isEmpty() throws MethodNotImplementedException {
        return size() == 0;
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        return contains(key);
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) 
        throws MethodNotImplementedException {
        throw new MethodNotImplementedException ("containsValue (Object)");
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#values()
     */
    public Collection values() throws MethodNotImplementedException {
        throw new MethodNotImplementedException("values()");
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#putAll(java.util.Map)
     */
    public void putAll(final Map aMap) {
        for (Iterator iter = aMap.keySet().iterator(); iter.hasNext();) {
            Object key = iter.next();
            put (key, aMap.get((key)));
        }
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#entrySet()
     */
    public Set entrySet() throws MethodNotImplementedException {
        throw new MethodNotImplementedException("entrySet()");
    }

    /* (non-Javadoc)
     * @see org.exolab.castor.persist.cache.Cache#keySet()
     */
    public Set keySet() throws MethodNotImplementedException {
        throw new MethodNotImplementedException("keySet");
    }

    /**
     * Initializes this cache instance.
     * @throws CacheAcquireException If the cache cannot be initialized.
     */ 
    public void initialize() throws CacheAcquireException {
        try {
            // Object factory = Class.forName("javax.util.jcs.CacheAccessFactory", 
            //    false, classLoader);
            // TODO [WG]: use other forName (...);
            Class factoryClass = Class.forName("org.apache.jcs.JCS");
            
            // this.cache = JCS.getInstance( this.getClassName() );
            this._cache = invokeStaticMethodWithExceptions (factoryClass, 
                    "getInstance", 
                    new Class[] {String.class}, 
                    new Object[] {this.getClassName()});
            
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

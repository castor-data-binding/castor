/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.cache.hashbelt;

import java.util.Iterator;
import java.util.Map;

/**
 * A type of hashbelt that moves requested elements back into the first
 * container when a get or add occurs.
 * <p>
 * Objects which are rarely used will work their way down the conveyor belt, and
 * eventually be discarded, if they are not referenced.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class LRUHashbelt extends AbstractHashbelt {
    //--------------------------------------------------------------------------

    /** The type of the cache. */
    public static final String TYPE = "lru";
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * {@inheritDoc}
     */
    public String getType() { return TYPE; }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * {@inheritDoc}
     */
    public Object get(final Object key) {
        if (key == null) { throw new NullPointerException("key"); }
        
        Object result = null;

        try {
            lock().writeLock().acquire();
        } catch (InterruptedException ex) {
            return null;
        }
        
        try {
            result = removeObjectFromCache(key);
            if (result != null) { putObjectIntoCache(key, result); }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            lock().writeLock().release();
        }
        
        return result;
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Object put(final Object key, final Object value) {
        if (key == null) { throw new NullPointerException("key"); }
        if (value == null) { throw new NullPointerException("value"); }
        
        Object result = null;

        try {
            lock().writeLock().acquire();
        } catch (InterruptedException ex) {
            return null;
        }
        
        try {
            result = putObjectIntoCache(key, value);
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            lock().writeLock().release();
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Object remove(final Object key) {
        if (key == null) { throw new NullPointerException("key"); }

        Object result = null;

        try {
            lock().writeLock().acquire();
        } catch (InterruptedException ex) {
            return null;
        }
        
        try {
            result = removeObjectFromCache(key);
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            lock().writeLock().release();
        }
        
        return result;
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public void putAll(final Map<? extends Object, ? extends Object> map) {
        if (map.containsKey(null)) { throw new NullPointerException("key"); }
        if (map.containsValue(null)) { throw new NullPointerException("value"); }

        Iterator<? extends Entry<? extends Object, ? extends Object>> iter;
        iter = map.entrySet().iterator();
        Entry<? extends Object, ? extends Object> entry;

        try {
            lock().writeLock().acquire();
        } catch (InterruptedException ex) {
            return;
        }
        
        try {
            while (iter.hasNext()) {
                entry = iter.next();
                putObjectIntoCache(entry.getKey(), entry.getValue());
            }
        } catch (RuntimeException ex) {
            throw ex;
        } finally {
            lock().writeLock().release();
        }
    }

    //--------------------------------------------------------------------------
}

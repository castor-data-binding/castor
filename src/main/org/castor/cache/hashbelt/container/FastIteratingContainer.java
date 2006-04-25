/*
 * Copyright 2005, 2006 Gregory Block, Ralf Joachim
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
package org.castor.cache.hashbelt.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.castor.util.concurrent.ConcurrentHashMap;

/**
 * The FastIteratingContainer implementation of the Container interface assuems two
 * things:
 * <ul>
 * <li>Puts and removes are rare in proportion to gets and iteration are common.
 *     Put and remove are much more expensive here than in MapContainer.</li>
 * <li>Keys will not be reused (using a key twice with different values implicitly
 *     does an expensive remove).</li>
 * </ul>
 * In order for allow for removes and adds to be called while an iterator is in use,
 * iterator-responses use cloned key/value lists.
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class FastIteratingContainer implements Container {
    //--------------------------------------------------------------------------

    /** The hashmap to store key/value pairs. */
    private Map _container = new ConcurrentHashMap();
    
    /** List of keys in the container. */
    private ArrayList _keys = new ArrayList();
    
    /** List of values in the container. */
    private ArrayList _values = new ArrayList();
    
    /** Timestamp of this container. */
    private long _timestamp = 0;
    
    //--------------------------------------------------------------------------
    // additional operations of container interface
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.hashbelt.container.Container#updateTimestamp()
     */
    public void updateTimestamp() { _timestamp = System.currentTimeMillis(); }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.hashbelt.container.Container#getTimestamp()
     */
    public long getTimestamp() { return _timestamp; }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.hashbelt.container.Container#keyIterator()
     */
    public synchronized Iterator keyIterator() {
        return ((ArrayList) _keys.clone()).iterator();
    }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.hashbelt.container.Container#valueIterator()
     */
    public synchronized Iterator valueIterator() {
        return ((ArrayList) _values.clone()).iterator();
    }
    
    //--------------------------------------------------------------------------
    // query operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#size()
     */
    public int size() {
        return _container.size();
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return _container.isEmpty();
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        return _container.containsKey(key);
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) {
        return _container.containsValue(value);
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key) {
        return _container.get(key);
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value) {
        if (value == null) { throw new IllegalArgumentException(); }
        Object oldValue = _container.put(key, value);
        if (oldValue == null) {
            synchronized (this) {
                _keys.add(key);
                _values.add(value);
            }
        } else if (oldValue != value) {
            synchronized (this) {
                _values.remove(oldValue);
                _values.add(value);
            }
        }
        return oldValue;
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key) {
        Object oldValue = _container.remove(key);
        if (oldValue != null) {
            synchronized (this) {
                _keys.remove(key);
                _values.remove(oldValue);
            }
        }
        return oldValue;
    }
    
    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map map) {
        Iterator iter = map.entrySet().iterator();
        synchronized (this) {
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                put(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public void clear() {
        _container.clear();
        synchronized (this) {
            _keys.clear();
            _values.clear();
        }
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        return _container.keySet();
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public Collection values() {
        return _container.values();
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        return _container.entrySet();
    }

    //--------------------------------------------------------------------------
}

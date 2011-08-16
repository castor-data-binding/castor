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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The FastIteratingContainer implementation of the Container interface assuems two
 * things:
 * <ul>
 * <li>Puts and removes are rare in proportion to gets and iteration are common.
 *     Put and remove are much more expensive here than in MapContainer.</li>
 * <li>Keys will not be reused (using a key twice with different values implicitly
 *     does an expensive remove).</li>
 * </ul>
 * 
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class FastIteratingContainer<K, V> implements Container<K, V> {
    //--------------------------------------------------------------------------

    /** The hashmap to store key/value pairs. */
    private Map<K, V> _container = new HashMap<K, V>();
    
    /** List of keys in the container. */
    private List<K> _keys = new ArrayList<K>();
    
    /** List of values in the container. */
    private List<V> _values = new ArrayList<V>();
    
    /** Timestamp of this container. */
    private long _timestamp = 0;
    
    //--------------------------------------------------------------------------
    // additional operations of container interface
    
    /**
     * {@inheritDoc}
     */
    public void updateTimestamp() { _timestamp = System.currentTimeMillis(); }
    
    /**
     * {@inheritDoc}
     */
    public long getTimestamp() { return _timestamp; }
    
    /**
     * {@inheritDoc}
     */
    public Iterator<K> keyIterator() {
        return _keys.iterator();
    }
    
    /**
     * {@inheritDoc}
     */
    public Iterator<V> valueIterator() {
        return _values.iterator();
    }
    
    //--------------------------------------------------------------------------
    // query operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public int size() {
        return _container.size();
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return _container.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        return _container.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        return _container.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public V get(final Object key) {
        return _container.get(key);
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public V put(final K key, final V value) {
        if (value == null) { throw new IllegalArgumentException(); }
        V oldValue = _container.put(key, value);
        if (oldValue == null) {
            _keys.add(key);
            _values.add(value);
        } else if (oldValue != value) {
            _values.remove(oldValue);
            _values.add(value);
        }
        return oldValue;
    }
    
    /**
     * {@inheritDoc}
     */
    public V remove(final Object key) {
        V oldValue = _container.remove(key);
        if (oldValue != null) {
            _keys.remove(key);
            _values.remove(oldValue);
        }
        return oldValue;
    }
    
    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        _container.clear();
        _keys.clear();
        _values.clear();
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Set<K> keySet() {
        return _container.keySet();
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<V> values() {
        return _container.values();
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Entry<K, V>> entrySet() {
        return _container.entrySet();
    }

    //--------------------------------------------------------------------------
}

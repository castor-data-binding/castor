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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class FastIteratingContainer implements Container {
    //--------------------------------------------------------------------------

    /** The hashmap to store key/value pairs. */
    private Map < Object, Object > _container = new ConcurrentHashMap < Object, Object > ();
    
    /** List of keys in the container. */
    private List < Object > _keys = new ArrayList < Object > ();
    
    /** List of values in the container. */
    private List < Object > _values = new ArrayList < Object > ();
    
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
    public synchronized Iterator < Object > keyIterator() {
        return new ArrayList < Object > (_keys).iterator();
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized Iterator < Object > valueIterator() {
        return new ArrayList < Object > (_values).iterator();
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
    public Object get(final Object key) {
        return _container.get(key);
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
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
     */
    public synchronized void putAll(final Map < ? extends Object, ? extends Object > map) {
        Iterator < ? extends Entry < ? extends Object, ? extends Object > > iter;
        iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry < ? extends Object, ? extends Object > entry = iter.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
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
     */
    public Set < Object > keySet() {
        return _container.keySet();
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection < Object > values() {
        return _container.values();
    }
    
    /**
     * {@inheritDoc}
     */
    public Set < Entry < Object, Object > > entrySet() {
        return _container.entrySet();
    }

    //--------------------------------------------------------------------------
}

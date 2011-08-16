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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An implementation of a container that uses weak references for storing values
 * in the map, so that values can be removed from the map by the system when the
 * system is under memory pressure. Keys, however, are kept strong - so contains()
 * may well find an element, but the value may have been lost. Make sure you test
 * for null returns from put.
 * <p>
 * Note that keys are hard references; in a situation where OutOfMemory will
 * occur, the JVM will first wipe out all unreferenced objects whose only link
 * is a weak reference. An out of memory will wipe all values from the maps
 * which are currently unreferenced. The keys remain until the hashbelt
 * containers are garbage collected, an put is called with that key or when the
 * value should be accessed through any operation of the Container interface.
 * 
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of cached values
 * 
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class WeakReferenceContainer<K, V> implements Container<K, V> {
    //--------------------------------------------------------------------------

    /** The hashmap to store key/value pairs. */
    private HashMap<K, WeakReference<V>> _container = new HashMap<K, WeakReference<V>>();
    
    /** ReadWriteLock to synchronize access to container. */
    private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock();
    
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
        return new ArrayList<K>(keySet()).iterator();
    }
    
    /**
     * {@inheritDoc}
     */
    public Iterator<V> valueIterator() {
        return values().iterator();
    }
    
    //--------------------------------------------------------------------------
    // query operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public int size() {
        try {
            _lock.readLock().lock();
            return _container.size();
        } finally {
            _lock.readLock().unlock();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        try {
            _lock.readLock().lock();
            return _container.isEmpty();
        } finally {
            _lock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(final Object key) {
        try {
            _lock.readLock().lock();
            return _container.containsKey(key);
        } finally {
            _lock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(final Object value) {
        try {
            _lock.writeLock().lock();
            Iterator<Entry<K, WeakReference<V>>> iter = _container.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<K, WeakReference<V>> entry = iter.next();
                WeakReference<V> ref = entry.getValue();
                // check to see if we've got a referenceable object to return.
                V found = ref.get();
                if (found != null) {
                    // if we have found an object we test for equality.
                    if (found.equals(value)) { return true; }
                } else {
                    // else we lost the referent so we remove the whole entry.
                    iter.remove();
                }
            }
            return false;
        } finally {
            _lock.writeLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public V get(final Object key) {
        try {
            _lock.writeLock().lock();
            WeakReference<V> ref = _container.get(key);
            // if we have no ref then there is no entry in the container.
            if (ref == null) { return null; }
            // check to see if we've got a referenceable object to return.
            V found = ref.get();
            // if we have found an object we return it.
            if (found != null) { return found; }
            // else we lost the referent so we remove the whole entry.
            _container.remove(key);
            // and return as haven't found anything.
            return null;
        } finally {
            _lock.writeLock().unlock();
        }
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public V put(final K key, final V value) {
        _lock.writeLock().lock();
        WeakReference<V> ref = _container.put(key, new WeakReference<V>(value));
        _lock.writeLock().unlock();
        // if we have no ref then there is no previous entry in the container.
        if (ref == null) { return null; }
        // check to see if we've got a referenceable object to return.
        V found = ref.get();
        // if we have found an object we return it.
        if (found != null) { return found; }
        // else we lost the referent so we return as haven't found anything.
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public V remove(final Object key) {
        _lock.writeLock().lock();
        WeakReference<V> ref = _container.remove(key);
        _lock.writeLock().unlock();
        // if we have no ref then there is no previous entry in the container.
        if (ref == null) { return null; }
        // check to see if we've got a referenceable object to return.
        V found = ref.get();
        // if we have found an object we return it.
        if (found != null) { return found; }
        // else we lost the referent and return as haven't found anything.
        return null;
    }
    
    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public void putAll(final Map<? extends K, ? extends V> map) {
        _lock.writeLock().lock();
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            _container.put(entry.getKey(), new WeakReference<V>(entry.getValue()));
        }
        _lock.writeLock().unlock();
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        _lock.writeLock().lock();
        _container.clear();
        _lock.writeLock().unlock();
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     */
    public Set<K> keySet() {
        try {
            _lock.readLock().lock();
            return _container.keySet();
        } finally {
            _lock.readLock().unlock();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<V> values() {
        Collection<V> col = new ArrayList<V>();
        _lock.writeLock().lock();
        Iterator<Entry<K, WeakReference<V>>> iter = _container.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<K, WeakReference<V>> entry = iter.next();
            WeakReference<V> ref = entry.getValue();
            // check to see if we've got a referenceable object to return.
            V found = ref.get();
            if (found != null) {
                // if we have found an object we add it to col.
                col.add(found);
            } else {
                // else we lost the referent so we remove the whole entry.
                iter.remove();
            }
        }
        _lock.writeLock().unlock();
        return col;
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Entry<K, V>> entrySet() {
        HashMap<K, V> map = new HashMap<K, V>();
        _lock.writeLock().lock();
        Iterator<Entry<K, WeakReference<V>>> iter = _container.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<K, WeakReference<V>> entry = iter.next();
            WeakReference<V> ref = entry.getValue();
            // check to see if we've got a referenceable object to return.
            V found = ref.get();
            if (found != null) {
                // if we have found an object we add it to col.
                map.put(entry.getKey(), found);
            } else {
                // else we lost the referent so we remove the whole entry.
                iter.remove();
            }
        }
        _lock.writeLock().unlock();
        return map.entrySet();
    }

    //--------------------------------------------------------------------------
}

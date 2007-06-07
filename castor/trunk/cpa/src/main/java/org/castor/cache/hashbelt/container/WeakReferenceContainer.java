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
 * @author <a href="mailto:gblock AT ctoforaday DOT com">Gregory Block</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class WeakReferenceContainer implements Container {
    //--------------------------------------------------------------------------

    /** The hashmap to store key/value pairs. */
    private HashMap _container = new HashMap();
    
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
        return new ArrayList(keySet()).iterator();
    }
    
    /**
     * {@inheritDoc}
     * @see org.castor.cache.hashbelt.container.Container#valueIterator()
     */
    public Iterator valueIterator() {
        return values().iterator();
    }
    
    //--------------------------------------------------------------------------
    // query operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#size()
     */
    public synchronized int size() {
        return _container.size();
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public synchronized boolean isEmpty() {
        return _container.isEmpty();
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public synchronized boolean containsKey(final Object key) {
        return _container.containsKey(key);
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public synchronized boolean containsValue(final Object value) {
        Iterator iter = _container.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            WeakReference ref = (WeakReference) entry.getValue();
            // check to see if we've got a referenceable object to return.
            Object found = ref.get();
            if (found != null) {
                // if we have found an object we test for equality.
                if (found.equals(value)) { return true; }
            } else {
                // else we lost the referent so we remove the whole entry.
                iter.remove();
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#get(java.lang.Object)
     */
    public synchronized Object get(final Object key) {
        WeakReference ref = (WeakReference) _container.get(key);
        // if we have no ref then there is no entry in the container.
        if (ref == null) { return null; }
        // check to see if we've got a referenceable object to return.
        Object found = ref.get();
        // if we have found an object we return it.
        if (found != null) { return found; }
        // else we lost the referent so we remove the whole entry.
        _container.remove(key);
        // and return as haven't found anything.
        return null;
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public synchronized Object put(final Object key, final Object value) {
        WeakReference ref = (WeakReference) _container.put(key, new WeakReference(value));
        // if we have no ref then there is no previous entry in the container.
        if (ref == null) { return null; }
        // check to see if we've got a referenceable object to return.
        Object found = ref.get();
        // if we have found an object we return it.
        if (found != null) { return found; }
        // else we lost the referent so we return as haven't found anything.
        return null;
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#remove(java.lang.Object)
     */
    public synchronized Object remove(final Object key) {
        WeakReference ref = (WeakReference) _container.remove(key);
        // if we have no ref then there is no previous entry in the container.
        if (ref == null) { return null; }
        // check to see if we've got a referenceable object to return.
        Object found = ref.get();
        // if we have found an object we return it.
        if (found != null) { return found; }
        // else we lost the referent and return as haven't found anything.
        return null;
    }
    
    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#putAll(java.util.Map)
     */
    public synchronized void putAll(final Map map) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            _container.put(entry.getKey(), new WeakReference(entry.getValue()));
        }
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public synchronized void clear() {
        _container.clear();
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public synchronized Set keySet() {
        return _container.keySet();
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public synchronized Collection values() {
        Collection col = new ArrayList();
        Iterator iter = _container.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            WeakReference ref = (WeakReference) entry.getValue();
            // check to see if we've got a referenceable object to return.
            Object found = ref.get();
            if (found != null) {
                // if we have found an object we add it to col.
                col.add(found);
            } else {
                // else we lost the referent so we remove the whole entry.
                iter.remove();
            }
        }
        return col;
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public synchronized Set entrySet() {
        HashMap map = new HashMap();
        Iterator iter = _container.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            WeakReference ref = (WeakReference) entry.getValue();
            // check to see if we've got a referenceable object to return.
            Object found = ref.get();
            if (found != null) {
                // if we have found an object we add it to col.
                map.put(entry.getKey(), found);
            } else {
                // else we lost the referent so we remove the whole entry.
                iter.remove();
            }
        }
        return map.entrySet();
    }

    //--------------------------------------------------------------------------
}

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
package org.castor.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An IdentityMap that uses reference-equality instead of object-equality. According
 * to its special function it violates some design contracts of the <code>Map</code>
 * interface.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 7491 $ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 0.9.9
 */
public final class IdentityMap implements Map {
    //--------------------------------------------------------------------------

    /** Default number of buckets. */
    private static final int    DEFAULT_CAPACITY = 17;
    
    /** Default load factor. */
    private static final float  DEFAULT_LOAD = 0.75f;
    
    /** Default number of entries. */
    private static final int    DEFAULT_ENTRIES = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD);
    
    /** Default factor to increment capacity. */
    private static final int    DEFAULT_INCREMENT = 2;
    
    /** First prime number to check is 3 as we prevent 2 by design. */
    private static final int    FIRST_PRIME_TO_CHECK = 3;
    
    //--------------------------------------------------------------------------

    /** Number of buckets. */
    private int                     _capacity = DEFAULT_CAPACITY;
    
    /** Maximum number of entries before rehashing. */
    private int                     _maximum = DEFAULT_ENTRIES;

    /** Buckets. */
    private Entry[]                 _buckets = new Entry[DEFAULT_CAPACITY];

    /** Number of map entries. */
    private int                     _entries = 0;

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @see java.util.Map#clear()
     */
    public void clear() {
        _capacity = DEFAULT_CAPACITY;
        _maximum = DEFAULT_ENTRIES;
        _buckets = new Entry[DEFAULT_CAPACITY];
        _entries = 0;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#size()
     */
    public int size() { return _entries; }

    /**
     * {@inheritDoc}
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() { return (_entries == 0); }

    /**
     * {@inheritDoc}
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value) {
        int hash = System.identityHashCode(key);
        int index = hash % _capacity;
        if (index < 0) { index = -index; }

        Entry entry = _buckets[index];
        Entry prev = null;
        while (entry != null) {
            if (entry.getKey() == key) {
                // There is a mapping for this key so we have to replace the value.
                Object ret = entry.getValue();
                entry.setValue(value);
                return ret;
            }
            prev = entry;
            entry = entry.getNext();
        }
        if (prev == null) {
            // There is no previous entry in this bucket.
            _buckets[index] = new Entry(key, hash, value);
        } else {
            // Next entry is empty so we have no mapping for this key.
            prev.setNext(new Entry(key, hash, value));
        }
        _entries++;
        if (_entries > _maximum) { rehash(); }
        return null;
    }

    /**
     * Rehash the map into a new array with increased capacity.
     */
    private void rehash() {
        long nextCapacity = _capacity * DEFAULT_INCREMENT;
        if (nextCapacity > Integer.MAX_VALUE) { return; }
        nextCapacity = nextPrime(nextCapacity);
        if (nextCapacity > Integer.MAX_VALUE) { return; }

        int newCapacity = (int) nextCapacity;
        Entry[] newBuckets = new Entry[newCapacity];

        Entry entry = null;
        Entry temp = null;
        Entry next = null;
        int newIndex = 0;

        for (int index = 0; index < _capacity; index++) {
            entry = _buckets[index];
            while (entry != null) {
                next = entry.getNext();

                newIndex = entry.getHash() % newCapacity;
                if (newIndex < 0) { newIndex = -newIndex; }

                temp = newBuckets[newIndex];
                if (temp == null) {
                    // First entry of the bucket.
                    entry.setNext(null);
                } else {
                    // Hook entry into beginning of the buckets chain.
                    entry.setNext(temp);
                }
                newBuckets[newIndex] = entry;

                entry = next;
            }
        }

        _capacity = newCapacity;
        _maximum = (int) (newCapacity * DEFAULT_LOAD);
        _buckets = newBuckets;
    }

    /**
     * Find next prime number greater than minimum.
     * 
     * @param  minimum  The minimum (exclusive) value of the next prime number.
     * @return The next prime number greater than minimum.
     */
    private long nextPrime(final long minimum) {
        long candidate = ((minimum + 1) / 2) * 2 + 1;
        while (!isPrime(candidate)) { candidate += 2; }
        return candidate;
    }

    /**
     * Check for prime number.
     * 
     * @param  candidate  Number to be checked for being a prime number.
     * @return <code>true</code> if the given number is a prime number
     *         <code>false</code> otherwise.
     */
    private boolean isPrime(final long candidate) {
        if ((candidate / 2) * 2 == candidate) { return false; }
        long stop = candidate / 2;
        for (long i = FIRST_PRIME_TO_CHECK; i < stop; i += 2) {
            if ((candidate / i) * i == candidate) { return false; }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        int hash = System.identityHashCode(key);
        int index = hash % _capacity;
        if (index < 0) { index = -index; }

        Entry entry = _buckets[index];
        while (entry != null) {
            if (entry.getKey() == key) { return true; }
            entry = entry.getNext();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key) {
        int hash = System.identityHashCode(key);
        int index = hash % _capacity;
        if (index < 0) { index = -index; }

        Entry entry = _buckets[index];
        while (entry != null) {
            if (entry.getKey() == key) { return entry.getValue(); }
            entry = entry.getNext();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key) {
        int hash = System.identityHashCode(key);
        int index = hash % _capacity;
        if (index < 0) { index = -index; }

        Entry entry = _buckets[index];
        Entry prev = null;
        while (entry != null) {
            if (entry.getKey() == key) {
                // Found the entry.
                if (prev == null) {
                    // First element in bucket matches.
                    _buckets[index] = entry.getNext();
                } else {
                    // Remove the entry from the chain.
                    prev.setNext(entry.getNext());
                }
                _entries--;
                return entry.getValue();
            }
            prev = entry;
            entry = entry.getNext();
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        Set set = new IdentitySet(_capacity);
        
        for (int i = 0; i < _capacity; i++) {
            Entry entry = _buckets[i];
            while (entry != null) {
                set.add(entry.getKey());
                entry = entry.getNext();
            }
        }
        
        return set;
    }

    /**
     * In contrast with the design contract of the <code>Map</code> interface this method
     * has not been implemented and throws a <code>UnsupportedOperationException</code>.
     * 
     * {@inheritDoc}
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        Set set = new IdentitySet(_capacity);
        
        for (int i = 0; i < _capacity; i++) {
            Entry entry = _buckets[i];
            while (entry != null) {
                set.add(entry);
                entry = entry.getNext();
            }
        }
        
        return set;
    }

    /**
     * In contrast with the design contract of the <code>Map</code> interface this method
     * has not been implemented and throws a <code>UnsupportedOperationException</code>.
     * 
     * {@inheritDoc}
     * @see java.util.Map#values()
     */
    public Collection values() {
        throw new UnsupportedOperationException();
    }

    /**
     * In contrast with the design contract of the <code>Map</code> interface this method
     * has not been implemented and throws a <code>UnsupportedOperationException</code>.
     * 
     * {@inheritDoc}
     * @see java.util.Map#containsValue
     */
    public boolean containsValue(final Object value) {
        throw new UnsupportedOperationException();
    }

    /**
     * This optional method has not been implemented for <code>IdentityMap</code> instead
     * it throws a <code>UnsupportedOperationException</code> as defined in the
     * <code>Map</code> interface.
     * 
     * {@inheritDoc}
     * @see java.util.Map#putAll
     */
    public void putAll(final Map map) {
        throw new UnsupportedOperationException();
    }
    
    //--------------------------------------------------------------------------

    /**
     * An entry of the <code>IdentityMap</code>.
     */
    public final class Entry implements Map.Entry {
        /** Key of entry. */
        private Object  _key;
        
        /** Identity hashcode of key. */
        private int     _hash;
        
        /** Value of entry. */
        private Object  _value;
        
        /** Reference to next entry. */
        private Entry   _next = null;

        /**
         * Construct an entry.
         * 
         * @param key    Key of entry.
         * @param hash   Identity hashcode of key.
         * @param value  Value of entry.
         */
        public Entry(final Object key, final int hash, final Object value) {
            _key = key;
            _hash = hash;
            _value = value;
        }

        /**
         * Get key of entry.
         *
         * @return Key of entry.
         */
        public Object getKey() { return _key; }

        /**
         * Get identity hashcode of key.
         *
         * @return Identity hashcode of key.
         */
        public int getHash() { return _hash; }

        /**
         * Set value of entry.
         *
         * @param  value    New value of entry.
         * @return Previous entry in the map.
         */
        public Object setValue(final Object value) {
            Object temp = _value;
            _value = value;
            return temp;
        }

        /**
         * Get value of entry.
         *
         * @return Value of entry.
         */
        public Object getValue() { return _value; }

        /**
         * Set reference to next entry.
         *
         * @param  next     New reference to next entry.
         */
        public void setNext(final Entry next) { _next = next; }

        /**
         * Get reference to next entry.
         *
         * @return Reference to next entry.
         */
        public Entry getNext() { return _next; }
    }

    //--------------------------------------------------------------------------
}

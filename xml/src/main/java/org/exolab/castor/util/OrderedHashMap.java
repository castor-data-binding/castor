/*
 * Copyright (C) 2004, Intalio Inc.
 *
 * The program(s) herein may be used and/or copied only with the
 * written permission of Intalio Inc. or in accordance with the terms
 * and conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *
 * $Id$
 * 
 * Created on Dec 14, 2004 by kvisco
 *
 */
package org.exolab.castor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A very simple extended HashMap, which maintains order via an ArrayList.
 * 
 * This class provides similar, though not identical, functionality as 
 * the JDK's LinkedHashMap, but works under JDK 1.2 and JDK 1.3.
 * 
 * This class is not synchronized, if more than one thread accesses an
 * instance of this class and at least one thread modifies the map, 
 * the OrderedHashMap instance must be synchronized via a call to
 * Collections.synchronizedMap method.
 * 
 * The #entrySet() and #keySet() methods return unmodifiable sets.
 * 
 * The #values() method returns an unmodifiable collection.
 * 
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class OrderedHashMap < K , V > extends HashMap < K , V > {
    /** SerialVersionUID. */
    private static final long serialVersionUID = -1648679783713336948L;

    /**
     * Ordered list of contained values.
     */
    private ArrayList < V > _orderedValues = null;
    
    /**
     * Creates a new OrderedHashMap.
     */
    public OrderedHashMap() {
        super();
        _orderedValues = new ArrayList < V > ();
    }
    
    /**
     * Creates a new OrderedHashMap with the given initial capacity
     * 
     * @param initialCapacity
     */
    public OrderedHashMap(int initialCapacity) {
        super(initialCapacity);
        _orderedValues = new ArrayList<V>(initialCapacity);
    }
    
    
    /**
     * Creates a new OrderedHashMap with the same entries
     * as the given map.
     * 
     * @param m the Map to initialize this Map with
     */
    public OrderedHashMap(Map<? extends K, ? extends V> m) {
        this(m.size());
        putAll(m);
    }
    
    
    /**
     * @see java.util.Map#clear()
     */
    public void clear() {
        super.clear();
        _orderedValues.clear();
    }
    
    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        OrderedHashMap<K, V> map = new OrderedHashMap<K, V>(size());
        map.putAll(this);
        return map;
        
    }
    
    /**
     * Returns the Map.Entry set for this Map. Note that the 
     * returned Set is an unmodifiable Set
     * 
     * @see java.util.Map#entrySet()
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    } 

    /**
     * Returns the key set for this Map. Note that the returned 
     * set is an unmodifiable Set
     * 
     * @see java.util.Map#keySet()
     */
    public Set<K> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    } 
    
    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public V put(K key, V value) {
        //-- remove any current value references from
        //-- the ordered list for the given key
        V obj = super.get(key);
        if (obj != null) {
            _orderedValues.remove(obj);
        }
    	obj = super.put(key, value);
        _orderedValues.add(value);
        return obj;
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends K, ? extends V> m) {
    	for (Iterator<? extends Map.Entry<? extends K, ? extends V>> entries = m.entrySet().iterator(); entries.hasNext(); ) {
            Map.Entry<? extends K, ? extends V> e = entries.next();
            put(e.getKey(), e.getValue());
        }
    }
    
    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public V remove(Object key) {
        V obj = super.remove(key);
        _orderedValues.remove(obj);
        return obj;
    }
    

    /**
     * Returns the set of values for this Map. Note that
     * the returned Collection is unmodifiable.
     * 
     * @see java.util.Map#values()
     */
    public Collection<V> values() {
        return Collections.unmodifiableList(_orderedValues);
    }

    
    
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 2000-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.adaptx.util;


/**
 * My implementation of a JDK 1.2 Map. I do not use synchronization,
 * so be careful in a threaded environment. I also do not specifically
 * "implements" java.util.Map, since support for JDK 1.1 is needed. 
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class HashMap {
    
    /**
     * The default number of buckets in this Map
    **/
    public static final int DEFAULT_SIZE = 17;
    
    /**
     * the list of names
    **/
    private Bucket[] table   = null;
    
    private int elementCount = 0;
    
    /**
     * Creates a new HashMap with the default number of buckets
    **/
    public HashMap() {
        this(HashMap.DEFAULT_SIZE);
    } //-- HashMap

    /**
     * Creates a new HashMap with the given number of buckets.
     * @param size, the number of buckets to use, this value must
     * be a non-zero positive integer, if not the default size will
     * be used.
     * <BR />
     * <B>Note:</B>The number of buckets is not the same as the
     * allocating space for a desired number of entries. If fact,
     * if you know number of entries that will be in the hash, you
     * might want to use a HashMap with a different implementation.
     * This Map is implemented using separate chaining.
    **/
    public HashMap(int size) {
        super();
        // protect against illegal size
        if (size <= 0) size = DEFAULT_SIZE;
        //initialize table
        table = new Bucket[size];
        for (int i = 0; i < size; i++)
            table[i] = new Bucket();
            
    } //-- Map
    
    /**
     * Removes all entries from this Map
    **/
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i].size = 0;
            table[i].entry = null;
        }
        elementCount = 0;
    } //-- clear
    
    /**
     * Returns true if the given object is a key contained in this Map
     * @return true if the given object is a key contained in this Map
    **/
    public boolean containsKey(Object key) {
        if (elementCount == 0) return false;
        else return (findEntry(key) != null);
    } //-- containsKey

    /**
     * Returns true if the given object is a value contained in this Map
     *
     * <BR />
     * <B>Note:</B> Depending on the size of the Map, this could be a 
     * slow operation. If you know the key an object would be associated with,
     * contains key would be much faster, or simply do (get(key) != null).
     *
     * @return true if the given object is a value contained in this Map
    **/
    public boolean containsValue(Object value) {
        
        if (elementCount == 0) return false;
        
        boolean nullValue = (value == null);
        
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i].entry;
            
            while (entry != null) {                
                if (nullValue) {
                    if (entry.getValue() == null) return true;
                }
                else {
                    if (value.equals(entry.getValue())) return true;
                }
                entry = entry.next;
            }
        }
        
        return false;
        
    } //-- containsValue
    
    
    /**
     * Returns an interator for the entries of this Map. 
     * Each element returned by a call to Iterator#next() is
     * a Map.Entry.
     * <BR />
     * Note: This is different than a JDK 1.2 Map because
     * I didn't want to deal with implementing Sets at this point.
     * @return an Iterator for the entries of this Map.
    **/
    public Iterator entries() {
        return new HashMapEntryIterator(table);
    } //-- entries
    
    /**
     * Returns true if the given Object is a HashMap which contains
     * equivalent HashMap entries as this HashMap.
     * @return true if the given Object is a HashMap, and is equivalent
     * to this Map
     * <BR />
     * I will be probably make an interface for Map, to allow comparisons
     * with different Map implemenations.
    **/
    public boolean equals(Object object) {
        
        if (object == this) return true;
        
        if (object instanceof HashMap) {
            HashMap map = (HashMap) object;
            if (map.size() != size()) return false;
            Iterator iter = map.entries();
            while (iter.hasNext()) {
                Entry entry = (Entry)iter.next();
                if (!entry.equals(findEntry(entry.getKey())))
                    return false;
            }
            return true;
        }
        return false;
    } //-- equals
    
    /**
     * Returns the value associated with the given key
     * @return the value associated with the given key
    **/
    public Object get(Object key) {
        Entry entry = findEntry(key);
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    } //-- get
    
    /**
     * Returns the hashCode for this Map. The hash code is the
     * sum of all the hash codes of each entry in the map
    **/
    public int hashCode() {
        int value = 0;
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i].entry;
            while (entry != null) {
                value += entry.hashCode();
                entry = entry.next;
            }
        }
        return value;
    } //-- hashCode
    
    /**
     * Returns true if this map contains no entries
     * @return true if this map contains no entries
    **/
    public boolean isEmpty() {
        return (elementCount == 0);
    } //-- isEmpty
    
    public Iterator keys() {
        return new HashMapKeyIterator(table);
    } //-- keys
    
    /**
     * Associated the specified value with the given key in this Map
     * @param key the object to associate with the given value
     * @param value the object to add an association in this Map
    **/
    public void put(Object key, Object value) {
        Entry entry = findEntry(key);
        if (entry == null) 
            addEntry(key, value);
        else 
            entry.setValue(value);
    } //-- put
    
    /** 
     * Removes the association with the given Key in the Map.
     * @param key the object key to remove the association for
     * @return the associated value being removed from this Map
    **/
    public Object remove(Object key) {
        
        if (elementCount == 0) return null;
        
        int hash = hashCode(key);
        
        Bucket bucket = table[hash % table.length];
        
        Entry entry = bucket.entry;
        
        boolean nullKey = (key == null);
        
        while (entry != null) {
            
            if (nullKey) {
                if (entry.getKey() == null) break; //-- found
            }
            else if (hash < entry.hash)
                return null; //-- not in list
            else {
                if (entry.getKey().equals(key)) break; //-- found
            }
            entry = entry.next;
        }
        
        Object value = null;
        
        if (entry != null) {
            
            if (entry.prev != null)
                entry.prev.next = entry.next;
            else
                bucket.entry = entry.next;
                
            if (entry.next != null) 
                entry.next.prev = entry.prev;
            
            value = entry.getValue();
            --bucket.size;
            --elementCount;
        }
        
        return value;
    } //-- remove
    
    /**
     * Returns the number of associations in the Map
     * @return the number of associations in the Map
    **/
    public int size() {
        return elementCount;
    } //-- size
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    private int hashCode(Object key) {
        int hash = (key == null) ? 0 : key.hashCode();
        if (hash < 0) hash = -hash;
        return hash;
    } //-- hashCode(Object)
    
    /**
     * Creates a new Entry for the given mapping and adds it
     * to this Map
     * @param key the key to associate with the given value
     * @param value the object to add an association for in this Map
    **/
    private void addEntry(Object key, Object value) {
        
        // compute hash
        int hash = hashCode(key);
        
        Entry newEntry = new Entry(hash, key, value);
        
        Bucket bucket = table[hash % table.length];
        
        if (bucket.entry == null) bucket.entry = newEntry;
        else {
            //-- entries are ordered for faster retrieval
            Entry current = bucket.entry;
            while (current != null) {
                if (hash <= current.hash) {                    
                    //-- insert new entry
                    if (current.prev != null) {                        
                        //-- current.prev<->newEntry
                        current.prev.next = newEntry;
                        newEntry.prev = current.prev;
                    }
                    // bucket.entry->newEntry
                    else bucket.entry = newEntry;
                    //--newEntry->current
                    newEntry.next = current;
                    break;
                }
                else if (current.next == null) {
                    //-- add entry to end of list
                    //-- current<->newEntry->null
                    current.next = newEntry;
                    newEntry.prev = current;
                    break;
                }
                current = current.next;
            }
        }
        
        ++bucket.size;
        ++elementCount;
    } //-- addEntry
    
    /**
     * Finds the Map entry associated with the given Key
     * @return the desired Map.Entry, or null if not found
    **/
    private Entry findEntry(Object key) {
        if (elementCount == 0) return null;
        return findEntry(key, hashCode(key));
    } //-- findEntry

    /**
     * Finds the Map entry associated with the given Key
     * @param key the key to search for
     * @param hash the pre-computed hashCode of the given key
     * @return the desired Map.Entry, or null if not found
    **/
    private Entry findEntry(Object key, int hash) {
        
        if (elementCount == 0) return null;
        
        int index = hash % table.length;
        
        Entry entry = table[index].entry;
        
        boolean nullKey = (key == null);
        
        while (entry != null) {
            
            if (nullKey) {
                if (entry.getKey() == null) 
                    return entry;
            }
            else if (hash == entry.hash) {
                if (entry.getKey().equals(key))
                    return entry;
            }
            else if (hash < entry.hash) break; //-- not in list
            
            entry = entry.next;
        }
        
        return null;
    } //-- findEntry
    
    
    //-----------------/
    //- Inner classes -/
    //-----------------/
    
    /**
     * The Entry bucket used by this Map implementation
    **/
    class Bucket {
        int size = 0;
        Entry entry = null;
    } //-- Bucket
    
    /**
     * An object representing an entry in the Map table
    **/
    class Entry {
        
        int hash = 0;
        
        Object key   = null;
        Object value = null;
        
        Entry next = null;
        Entry prev = null;
        
        public Entry(int hashCode, Object key, Object value) {
            this.hash = hashCode;
            this.key = key;
            this.value = value;
        } //-- Entry
        
        public Object getKey() {
            return key;
        } //-- getKey
        
        public Object getValue() {
            return value;
        } //-- getValue
        
        public boolean equals(Object object) {
            
            if (object == null) return false;
            
            if (object instanceof Entry) {
                
                Entry entry = (Entry) object;
                
                if (this.key == null)
                    if (entry.getKey() != null) 
                        return false;
                else
                    if (!this.key.equals(entry.getKey())) 
                        return false;
               
                if (this.value == null)
                    return (entry.getValue() == null);
                else 
                    return (this.value.equals(entry.getValue()));
            }
            
            return false;
        } //-- equals
        
        public int hashCode() {
            int keyHash = (key == null) ? 0 : key.hashCode();
            int valHash = (value == null) ? 0 : value.hashCode();
            return (keyHash ^ valHash);
        } //-- hashCode
        
        public void setValue(Object value) {
            this.value = value;
        } //-- setValue
    } //-- Entry
    
} //-- Map

class HashMapEntryIterator implements Iterator {
    
    HashMap.Bucket[] table = null;
    private int index = 0;
    private HashMap.Entry current = null;
    private HashMap.Entry next = null;
    
    HashMapEntryIterator(HashMap.Bucket[] table) {
        
        System.out.println("<HashMapEntryIterator>");
        this.table = table;
        //-- find first
        while (index < table.length) {
            next = table[index].entry;
            if (next != null) break;
            ++index;
        }
        
        System.out.println("</HashMapEntryIterator>");
    } //-- HashMapEntryIterator
    
    public boolean hasNext() {
        return (next != null);
    } //-- hasNext
    
    public Object next() {
        current = next;        
        if (next == null) return null;
        next = next.next;
        if (next == null) {
            //-- find next non empty bucket
            while (++index < table.length) {
                next = table[index].entry;
                if (next != null) break;
            }
        }
        return current;
    } //-- next
    
    public Object remove() {
        //-- Not Yet Implemented
        throw new IllegalStateException("#remove() is not yet implemented");
    } //-- remove
    
} //-- HashMapEntryIterator

class HashMapKeyIterator extends HashMapEntryIterator {
    
    HashMapKeyIterator(HashMap.Bucket[] table) {
        super(table);
    } //-- HashMapKeyIterator
    
    public Object next() {
        HashMap.Entry entry = (HashMap.Entry)super.next();
        if (entry != null) return entry.getKey();
        return null;
    } //-- next
    
} // HashMapKeyIterator

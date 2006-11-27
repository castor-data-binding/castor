/**
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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping.loader;


import java.util.Enumeration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MapItem;


/**
 * Implementation of various collection handlers for the Java 1.2
 * libraries.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-26 13:08:15 -0600 (Wed, 26 Apr 2006) $
 */
public final class J2CollectionHandlers
{


    public static CollectionHandlers.Info[] getCollectionHandlersInfo()
    {
        return _colHandlers;
    }


    /**
     * List of all the default collection handlers.
     */
    private static CollectionHandlers.Info[] _colHandlers = new CollectionHandlers.Info[] {
       new CollectionHandlers.Info("list", List.class, false, new CollectionHandler() {
          public Object add(Object collection, final Object object) {
             if (collection == null) {
                 collection = new ArrayList();
                 ((Collection) collection).add(object);
                 return collection;
             }
            ((Collection) collection).add(object);
             return null;
          }
          public Enumeration elements(final Object collection) {
              if (collection == null) {
                  return new CollectionHandlers.EmptyEnumerator();
              }
              return new IteratorEnumerator(((Collection) collection).iterator());
          }
          public int size(final Object collection) {
             if (collection == null) {
                return 0;
             }
             return ((Collection) collection).size();
          }
          public Object clear(final Object collection) {
             if (collection != null) {
                ((Collection) collection).clear();
             }
             return null;
          }
          public String toString() {
             return "List";
          }
       } ),
        new CollectionHandlers.Info( "arraylist", ArrayList.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new ArrayList();
                    ( (Collection) collection ).add( object );
                    return collection;
                }
                ( (Collection) collection ).add( object );
                return null;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Collection) collection ).iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Collection) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Collection) collection ).clear();
                return null;
            }
            public String toString() {
                return "ArrayList";
            }
        } ),
        // For Collection/ArrayList (1.2)
        new CollectionHandlers.Info( "collection", Collection.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new ArrayList();
                    ( (Collection) collection ).add( object );
                    return collection;
                }
                ( (Collection) collection ).add( object );
                return null;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Collection) collection ).iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Collection) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Collection) collection ).clear();
                return null;
            }
            public String toString() {
                return "Collection";
            }
        } ),
        // For Set/HashSet (1.2)
        new CollectionHandlers.Info( "set", Set.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new HashSet();
                    ( (Set) collection ).add( object );
                    return collection;
                }
                //if ( ! ( (Set) collection ).contains( object ) )
                ( (Set) collection ).add( object );
                return null;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Set) collection ).iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Set) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Set) collection ).clear();
                return null;
            }
            public String toString() {
                return "Set";
            }
        } ),
        // For Map/HashMap (1.2)
        new CollectionHandlers.Info( "map", Map.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                
                Object key = object;
                Object value = object;
                
                if (object instanceof MapItem) {
                    MapItem item = (MapItem)object;
                    key = item.getKey();
                    value = item.getValue();
                    if (value == null) {
                        value = object;
                    }
                    if (key == null) {
                        key = value;
                    }
                }
                
                if ( collection == null ) {
                    collection = new HashMap();
                    ( (Map) collection ).put( key, value );
                    return collection;
                }
                ( (Map) collection ).put( key, value );
                return null;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Map) collection ).values().iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Map) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Map) collection ).clear();
                return null;
            }
            public String toString() {
                return "Map";
            }
        } ),
        // For SortedSet (1.2 aka 1.4)
        new CollectionHandlers.Info("sortedset", SortedSet.class, false, new SortedSetCollectionHandler()),

        // For SortedMap (1.2 aka 1.4)
        new CollectionHandlers.Info("sortedmap", SortedMap.class, false, new SortedMapCollectionHandler()),
        
        // For java.util.Iterator
        new CollectionHandlers.Info( "iterator", Iterator.class, false, new CollectionHandler() {
            public Object add(Object collection, Object object) {
                //-- do nothing, cannot add elements to an enumeration
                return null;
            }
            public Enumeration elements(Object collection) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return ((Enumeration) collection);
            }
            public int size(Object collection) {
                //-- Nothing we can do without iteratin over the iterator
                return 0;
            }
            public Object clear(Object collection) {
                return null;
            }
            public String toString() {
                return "Iterator";
            }
        } )

    };


    private static final class SortedSetCollectionHandler implements CollectionHandler {
        
        /**
         * @inheritDoc
         */
        public Object add(Object collection, final Object object) {
            if (collection == null) {
                collection = new TreeSet();
                ((Set) collection).add(object);
                return collection;
            }
            // if (!((Set) collection).contains(object))
            ((Set) collection).add(object);
            return null;
            
        }

        /**
         * @inheritDoc
         */
        public Enumeration elements(final Object collection) {
            if (collection == null) {
                return new CollectionHandlers.EmptyEnumerator();
            }
            return new IteratorEnumerator(((Set) collection).iterator());
        }

        /**
         * @inheritDoc
         */
        public int size(final Object collection) {
            if (collection == null) {
                return 0;
            }
            return ((Set) collection).size();
        }

        /**
         * @inheritDoc
         */
        public Object clear(final Object collection) {
            if (collection != null) {
                ((Set) collection).clear();
            }
            return null;
        }

        /**
         * @inheritDoc
         */
        public String toString() {
            return "SortedSet";
        }
    }

    private static final class SortedMapCollectionHandler implements CollectionHandler {
        
        public Object add(Object collection, Object object) {

            Object key = object;
            Object value = object;

            if (object instanceof MapItem) {
                MapItem item = (MapItem) object;
                key = item.getKey();
                value = item.getValue();
                if (value == null) {
                    value = object;
                }
                if (key == null) {
                    key = value;
                }
            }

            if (collection == null) {
                collection = new TreeMap();
                ((SortedMap) collection).put(key, value);
                return collection;
            }
            ((SortedMap) collection).put(key, value);
            return null;
        }
        
        public Enumeration elements(final Object collection) {
            if (collection == null)
                return new CollectionHandlers.EmptyEnumerator();
            return new IteratorEnumerator(((SortedMap) collection).values()
                    .iterator());
        }
        
        public int size(final Object collection) {
            if (collection == null)
                return 0;
            return ((SortedMap) collection).size();
        }
        
        public Object clear(final Object collection) {
            if (collection != null)
                ((SortedMap) collection).clear();
            return null;
        }
        
        public String toString() {
            return "SortedMap";
        }
    }
    

    /**
     * Enumerator for an iterator.
     */
    static final class IteratorEnumerator
        implements Enumeration
    {

        private final Iterator _iterator;

        IteratorEnumerator( Iterator iterator )
        {
            _iterator = iterator;
        }

        public boolean hasMoreElements()
        {
            return _iterator.hasNext();
        }

        public Object nextElement()
        {
            return _iterator.next();
        }

    }


}

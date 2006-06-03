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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping.loader;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MapItem;

import java.lang.reflect.*;

/**
 * Implementation of various collection handlers for the Java 1.1
 * libraries.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see CollectionHandler
 */
public final class J1CollectionHandlers
{


    public static CollectionHandlers.Info[] getCollectionHandlersInfo()
    {
        return _colHandlers;
    }


    /**
     * List of all the default collection handlers.
     */
    private static CollectionHandlers.Info[] _colHandlers = new CollectionHandlers.Info[] {
        // For array (any)
        new CollectionHandlers.Info( "array", Object[].class, true, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {

                    // If the collection is of primitive type, the instantiation
                    // (if it was null) is handled in the FieldHandlerImpl. We
                    // can rely here that we deal only with array of object.
                    Object newArray = Array.newInstance(object.getClass(), 1);
                    Array.set(newArray, 0,  object);
                    return newArray;
                }
                
                Class type = collection.getClass();
                if (!type.isArray()) {
                    String err = "J1CollectionHandlers.array#add: type "+
                        "mismatch, expecting an array, instead received: ";
                    err += type.getName();
                    throw new IllegalArgumentException(err);
                }
                    
                type = type.getComponentType();
                
                Object newArray = Array.newInstance(type, Array.getLength(collection)+1);

                for ( int i = 0 ; i < Array.getLength(collection) ; ++i )
                    Array.set(newArray, i,  Array.get(collection, i ));

                Array.set(newArray, Array.getLength(collection),  object);

                return newArray;
                
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new ArrayEnumerator( collection );
            }
            public int size( Object collection ) {
                if ( collection == null )
                    return 0;
                return Array.getLength(collection);
            }
            public Object clear( Object collection ) {
                if ( collection == null ) {
                    return null;
                }
                Class type = collection.getClass();
                if (!type.isArray()) {
                    String err = "J1CollectionHandlers.array#add: type "+
                        "mismatch, expecting an array, instead received: ";
                    err += type.getName();
                    throw new IllegalArgumentException(err);
                }
                type = type.getComponentType();
                return Array.newInstance(type,0);
            }
            public String toString() {
                return "Object[]";
            }
        } ),
        // For Vector (1.1)
        new CollectionHandlers.Info( "vector", Vector.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new Vector();
                    ( (Vector) collection ).addElement( object );
                    return collection;
                }
                ( (Vector) collection ).addElement( object );
          return null;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return ( (Vector) collection ).elements();
            }
            public int size( Object collection ) {
                if ( collection == null )
                    return 0;
                return ( (Vector) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Vector) collection ).removeAllElements();
                return null;
            }
            public String toString() {
                return "Vector";
            }
        } ),
        // For Hashtable (1.1)
        new CollectionHandlers.Info( "hashtable", Hashtable.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                
                Object key = object;
                Object value = object;
                
                if (object instanceof org.exolab.castor.mapping.MapItem) {
                    MapItem mapItem = (MapItem)object;
                    key = mapItem.getKey();
                    value = mapItem.getValue();
                    if (value == null) {
                        value = object;
                    }
                    if (key == null) {
                        key = value;
                    }
                }
                
                if ( collection == null ) {
                    collection = new Hashtable();
                    ( (Hashtable) collection ).put( key, value );
                    return collection;
                }
                ( (Hashtable) collection ).put( key, value );
          return null;
            }
            
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return ( (Hashtable) collection ).elements();
            }
            public int size( Object collection ) {
                if ( collection == null )
                    return 0;
                return ( (Hashtable) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Hashtable) collection ).clear();
                return null;
            }
            public String toString() {
                return "Hashtable";
            }
        } ),
        // For Enumeration (1.1)
        new CollectionHandlers.Info( "enumerate", Enumeration.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                //-- do nothing, cannot add elements to an enumeration
                return null;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return ( (Enumeration) collection );
            }
            public int size( Object collection ) {
                //-- Nothing we can do without destroying the enumeration
                return 0;
            }
            public Object clear( Object collection ) {
                //-- Should we iterate over nextElement?
                return null;
            }
            public String toString() {
                return "Enumeration";
            }
        } )
        
    };


    /**
     * Enumerator for an array.
     */
    static final class ArrayEnumerator
        implements Enumeration
    {

        private final Object _array;

        private int            _index;

        ArrayEnumerator( Object array )
        {
            _array = array;
        }

        public boolean hasMoreElements()
        {
            return ( _index < Array.getLength(_array) );
        }

        public Object nextElement()
        {
            if ( _index >=  Array.getLength(_array) )
                throw new NoSuchElementException();
            return Array.get(_array, _index++ );
        }

    }


}
 

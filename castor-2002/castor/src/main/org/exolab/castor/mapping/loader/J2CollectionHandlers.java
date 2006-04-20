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
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.CollectionHandler;


/**
 * Implementation of various collection handlers for the Java 1.2
 * libraries.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
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
        // For Collection/ArrayList (1.2)
        new CollectionHandlers.Info( "collection", Collection.class, false, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new ArrayList();
                    ( (Collection) collection ).add( object );
                    return collection;
                } else {
                    ( (Collection) collection ).add( object );
                    return null;
                }
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
                } else {
                    //if ( ! ( (Set) collection ).contains( object ) )
                    ( (Set) collection ).add( object );
                    return null;
                }
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
                if ( collection == null ) {
                    collection = new HashMap();
                    ( (HashMap) collection ).put( object, object );
                    return collection;
                } else {
                    ( (HashMap) collection ).put( object, object );
                    return null;
                }
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (HashMap) collection ).values().iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (HashMap) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Map) collection ).clear();
                return null;
            }
            public String toString() {
                return "Map";
            }
        } )
    };


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

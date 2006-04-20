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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping.loader;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.CollectionHandler;


/**
 * Implementation of various collection handlers for the Java 1.1
 * libraries.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see CollectionHandler
 */
final class CollectionHandlers
{


    // XXX Collection handlers to support for 1.2:
    //   HashMap, HashSet, ArrayList, LinkedList
    //   TreeMap, TreeSet, WeakHashMap


    /**
     * Returns the collection's Java class from the collection name.
     * The collection name may be a short name (e.g. <tt>vector</tt>)
     * or the collection Java class name (e.g. <tt>java.util.Vector</tt>).
     * If the collection is not supported, an exception is thrown.
     *
     * @param name The collection name
     * @return The collection Java class
     * @throws MappingException The named collection is not supported
     */
    static Class getCollectionType( String name )
        throws MappingException
    {
        for ( int i = 0 ; i < _colHandlers.length ; ++i )
            if ( _colHandlers[ i ].shortName.equals( name ) ||
                 _colHandlers[ i ].javaClass.getName().equals( name ) )
                return _colHandlers[ i ].javaClass;
        throw new MappingException( "mapping.noCollectionHandler", name );
    }


    /**
     * Returns the collection's handler based on the Java class.
     *
     * @param javaClass The collection's Java class
     * @return The collection handler
     * @throws MappingException The collection class is not supported
     */
    static CollectionHandler getHandler( Class javaClass )
        throws MappingException
    {
        for ( int i = 0 ; i < _colHandlers.length ; ++i )
            if ( _colHandlers[ i ].javaClass.equals( javaClass ) )
                return _colHandlers[ i ].handler;
        throw new MappingException( "mapping.noCollectionHandler", javaClass.getName() );
    }


    /**
     * Returns true if the collection is an enumeration. Enumeration are
     * a special type, the 'set' method accepts an object while the 'get'
     * method returns an enumeration of methods.
     *
     * @param colType The collection type as return from {@link #getCollectionType}
     * @return True if the collection type is an enumeration
     */
    static boolean isEnumerate( Class colType )
    {
        return ( colType == Enumeration.class );
    }


    /**
     * Information about a collection handler. Registered in the
     * constructor to support a collection.
     */
    static final class CollectionHandlerInfo
    {

        /**
         * The short name of the collection (e.g. <tt>vector</tt>).
         */
        final String            shortName;

        /**
         * The Java class of the collection (e.g. <tt>java.util.Vector</tt>).
         */
        final Class             javaClass;

        /**
         * The collection handler instance.
         */
        final CollectionHandler handler;

        CollectionHandlerInfo( String shortName, Class javaClass, CollectionHandler handler )
        {
            this.shortName = shortName;
            this.javaClass = javaClass;
            this.handler = handler;
        }

    }


    /**
     * List of all the default collection handlers.
     */
    private static CollectionHandlerInfo[] _colHandlers = new CollectionHandlerInfo[] {
        // For enumerate ([])
        new CollectionHandlerInfo( "enumerate", Enumeration.class, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                return object;
            }
            public Enumeration elements( Object collection ) {
                return (Enumeration) collection;
            }
            public String toString() {
                return "Enumerate CollectionHandler";
            }
        } ),
        // For array ([])
        new CollectionHandlerInfo( "array", Object[].class, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                Object[] array;
                if ( collection == null ) {
                    array = new Object[ 1 ];
                    array[ 0 ] = object;
                    return array;
                }
                Object[] newArray;
                array = (Object[]) collection;
                newArray = new Object[ array.length + 1 ];
                for ( int i = 0 ; i < array.length ; ++i )
                    newArray[ i ] = array[ i ];
                newArray[ array.length ] = object;
                return newArray;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new EmptyEnumerator();
                return new ArrayEnumerator( (Object[]) collection );
            }
            public String toString() {
                return "Array CollectionHandler";
            }
        } ),
        // For Vector (1.1)
        new CollectionHandlerInfo( "vector", Vector.class, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new Vector();
                    ( (Vector) collection ).addElement( object );
                } else
                    if ( ! ( (Vector) collection ).contains( object ) )
                        ( (Vector) collection ).addElement( object );
                return collection;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new EmptyEnumerator();
                return ( (Vector) collection ).elements();
            }
            public String toString() {
                return "Vector CollectionHandler";
            }
        } ),
        // For Hashtable (1.1)
        new CollectionHandlerInfo( "hashtable", Hashtable.class, new CollectionHandler() {
            public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new Hashtable();
                    ( (Hashtable) collection ).put( object, object );
                } else
                    if ( ! ( (Hashtable) collection ).contains( object ) )
                        ( (Hashtable) collection ).put( object, object );
                return collection;
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new EmptyEnumerator();
                return ( (Hashtable) collection ).elements();
            }
            public String toString() {
                return "Hashtable CollectionHandler";
            }
        } )
    };


    /**
     * Enumerator for an array.
     */
    static final class ArrayEnumerator
        implements Enumeration
    {

        private final Object[] array;

        private int            index;

        ArrayEnumerator( Object[] array )
        {
            this.array = array;
        }

        public boolean hasMoreElements()
        {
            return ( index < array.length );
        }

        public Object nextElement()
        {
            if ( index > array.length )
                throw new NoSuchElementException();
            return array[ index++ ];
        }

    }


    /**
     * Enumerator for a null collection.
     */
    static final class EmptyEnumerator
        implements Enumeration
    {

        public boolean hasMoreElements()
        {
            return false;
        }

        public Object nextElement()
        {
            throw new NoSuchElementException();
        }

    }


}




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


import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.lang.reflect.Method;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.util.Configuration;


/**
 * Utility class for obtaining collection handlers. Based on the
 * configuration and supported classes it will return collections
 * suitable for Java 1.1 and Java 1.2 run times.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see CollectionHandler
 */
final class CollectionHandlers
{


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
    public static Class getCollectionType( String name )
        throws MappingException
    {
        if ( _info == null )
            loadInfo();
        for ( int i = 0 ; i < _info.length ; ++i )
            if ( _info[ i ].shortName.equalsIgnoreCase( name ) ||
                 _info[ i ].javaClass.getName().equals( name ) )
                return _info[ i ].javaClass;
        throw new MappingException( "mapping.noCollectionHandler", name );
    }


    /**
     * Returns the collection's handler based on the Java class.
     *
     * @param javaClass The collection's Java class
     * @return The collection handler
     * @throws MappingException The collection class is not supported
     */
    public static CollectionHandler getHandler( Class javaClass )
        throws MappingException
    {
        if ( _info == null )
            loadInfo();
        for ( int i = 0 ; i < _info.length ; ++i )
            if ( _info[ i ].javaClass.equals( javaClass ) )
                return _info[ i ].handler;
        throw new MappingException( "mapping.noCollectionHandler", javaClass.getName() );
    }


    /**
     * Returns true if the collection requires get/set methods.
     * <tt>java.util</tt> collections only require a get method,
     * but an array collection required both get and set methods.
     *
     * @parfam javaClass The collection's java class
     * @return True if collection requires get/set methods, false
     *  if collection requires only get method
     * @throws MappingException The collection class is not supported
     */
    public static boolean isGetSetCollection( Class javaClass )
        throws MappingException
    {
        if ( _info == null )
            loadInfo();
        for ( int i = 0 ; i < _info.length ; ++i )
            if ( _info[ i ].javaClass.equals( javaClass ) )
                return _info[ i ].getSetCollection;
        throw new MappingException( "mapping.noCollectionHandler", javaClass.getName() );
    }


    /**
     * Called once to load collection handler information for the various
     * collection handlers (Java 1.1, Java 1.2) based on the configuration
     * file.
     */
    private static synchronized void loadInfo()
    {
        if ( _info == null ) {
            Vector          allInfo;
            Info[]          info;
            StringTokenizer tokenizer;
            Class           infoClass;
            Method          method;

            allInfo = new Vector();
            tokenizer = new StringTokenizer( Configuration.getProperty( "org.exolab.castor.mapping.collections", "" ), ", " );
            while ( tokenizer.hasMoreTokens() ) {
                try {
                    if ( CollectionHandlers.class.getClassLoader() != null )
                        infoClass = CollectionHandlers.class.getClassLoader().loadClass( tokenizer.nextToken() );
                    else
                        infoClass = Class.forName( tokenizer.nextToken() );
                    method = infoClass.getMethod( "getCollectionHandlersInfo", null );
                    info = (Info[]) method.invoke( null, null );
                    for ( int i = 0 ; i < info.length ; ++i )
                        allInfo.addElement( info[ i ] );
                } catch ( Exception except ) {
                    // System.err.println( "CollectionHandlers: " + except.toString() );
                }
            }
            _info = new Info[ allInfo.size() ];
            allInfo.copyInto( _info );
        }
    }

    
    private static Info[]  _info;


    static class Info
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

        /**
         * True for collections that require both get and set methods.
         */
        final boolean           getSetCollection;
        
        Info( String shortName, Class javaClass, boolean getSetCollection,
              CollectionHandler handler )
        {
            this.shortName = shortName;
            this.javaClass = javaClass;
            this.handler = handler;
            this.getSetCollection = getSetCollection;
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




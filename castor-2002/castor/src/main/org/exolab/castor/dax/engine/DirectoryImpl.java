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


package org.exolab.castor.dax.engine;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.net.MalformedURLException;
import netscape.ldap.LDAPUrl;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import org.exolab.castor.dax.Directory;
import org.exolab.castor.dax.DirectoryException;
import org.exolab.castor.dax.InvalidSearchException;
import org.exolab.castor.dax.DuplicateRDNException;
import org.exolab.castor.dax.Search;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectNotPersistentException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.persist.ClassHandler;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.persist.PersistenceEngine;
import org.exolab.castor.persist.PersistenceEngineFactory;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.LogInterceptor;


/**
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DirectoryImpl
    implements Directory
{


    private LDAPConnection     _conn;


    private String             _dn;


    private PersistenceEngine  _dirEngine;


    private MappingResolver    _mapResolver;


    private TransactionContext _tx;


    private ClassHandler       _handler;


    DirectoryImpl( LDAPConnection conn, LDAPUrl url, 
                   MappingResolver mapResolver, LogInterceptor logInterceptor )
        throws DirectoryException
    {
        ClassDescriptor clsDesc;

        _conn = conn;
        _dn = url.getDN();
        _mapResolver = mapResolver;
        clsDesc = (ClassDescriptor) _mapResolver.listDescriptors().nextElement();
        try {
            _dirEngine = getEngine( url, clsDesc, logInterceptor );
        } catch ( MappingException except ) {
            throw new DirectoryException( except );
        }
        _handler = _dirEngine.getClassHandler( clsDesc.getJavaClass() );
    }


    public String getDN()
    {
        return _dn;
    }


    public Search createSearch( String expr )
        throws InvalidSearchException, DirectoryException
    {
        Persistence         per;
        int                 next;
        int                 pos;
        StringBuffer        query;
        Vector              types;
        Class[]             array;
        
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        
        try {
            next = expr.indexOf( '$' );
            if ( next <= 0 ) {
                return new SearchImpl( this, _dirEngine.getPersistence( _handler.getJavaClass() ).createQuery( new LDAPQueryExpression( expr ), new Class[ 0 ], null ) );
            } else {
                pos = 0;
                query = new StringBuffer();
                types = new Vector();
                while ( next > 0 ) {
                    if ( next == expr.length() - 1 ) {
                        query.append( expr.substring( pos, next + 1 ) );
                        pos = next;
                        break;
                    }
                    if ( expr.charAt( next + 1 ) == '$' ) {
                        query.append( expr.substring( pos, next + 1 ) );
                        pos = next + 2;
                        next = expr.indexOf( '$', next );
                    }
                    query.append( expr.substring( pos, next + 1 ) );
                    query.append( "\0" );
                    pos = next + 1;
                    next = expr.indexOf( '$', next );
                    types.addElement( null );
                }
                query.append( expr.substring( pos, next ) );
                array = new Class[ types.size() ];
                types.copyInto( array );
                return new SearchImpl( this, _dirEngine.getPersistence( _handler.getJavaClass() ).createQuery( new LDAPQueryExpression( query.toString() ), array, null ) );
            }
        } catch ( QueryException except ) {
            throw new InvalidSearchException( except.getMessage() );
        } catch ( PersistenceException except ) {
            throw new DirectoryException( except );
        }
    }


    public synchronized Object read( Object rdn )
        throws DirectoryException
    {
        ClassHandler  handler;
        Object        object;
        
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        
        handler = _handler;
        // clsDesc = _dirEngine.getClassDesc();
        try {
            if ( _tx != null ) 
                object = _tx.load( _dirEngine, handler, rdn, AccessMode.Shared );
            else {
                TransactionContext tx;
                
                tx = new TransactionContextImpl( _conn );
                object = _tx.load( _dirEngine, handler, rdn, AccessMode.Shared );
                tx.commit();
            }
        } catch ( ObjectNotFoundException except ) {
            return null;
        } catch ( PersistenceException except ) {
            if ( except.getException() != null )
                throw new DirectoryException( except.getException() );
            else
                throw new DirectoryException( except );
        }
        return object;
    }
    
    
    public synchronized void create( Object obj )
        throws DuplicateRDNException, DirectoryException
    {
        Object       rdn;
        
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        
        rdn = _handler.getIdentity( obj );
        if ( rdn == null )
            throw new DirectoryException( "Object has no RDN" );
        try {
            if ( _tx != null ) {
                _tx.create( _dirEngine, obj, rdn );
            } else {
                TransactionContext tx;
                
                tx = new TransactionContextImpl( _conn );
                tx.create( _dirEngine, obj, rdn );
                tx.commit();
            }
        } catch ( DuplicateIdentityException except ) {
            throw new DuplicateRDNException( "Duplicate RDN" );
        } catch ( PersistenceException except ) {
            if ( except.getException() != null )
                throw new DirectoryException( except.getException() );
            else
                throw new DirectoryException( except );
        }
    }
    

    public synchronized void delete( Object obj )
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        
        try {
            if ( _tx != null ) {
                _tx.delete( obj );
            } else {
                TransactionContext tx;
                
                tx = new TransactionContextImpl( _conn );
                tx.delete( obj );
                tx.commit();
            }
        } catch ( ObjectNotPersistentException except ) {
            throw new DuplicateRDNException( "Object not persistent" );
        } catch ( PersistenceException except ) {
            if ( except.getException() != null )
                throw new DirectoryException( except.getException() );
            else
                throw new DirectoryException( except );
        }
    }
    

    public synchronized void begin()
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        if ( _tx != null )
            throw new DirectoryException( "Already inside a transaction" );
        _tx = new TransactionContextImpl( _conn );
    }
    
    
    public synchronized void commit()
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        if ( _tx == null )
            throw new DirectoryException( "Not inside a transaction" );
        try {
            _tx.prepare();
            _tx.commit();
        } catch ( TransactionAbortedException except ) {
            _tx.rollback();
        }
        _tx = null;
    }


    public synchronized void rollback()
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        if ( _tx == null )
            throw new DirectoryException( "Not inside a transaction" );
        _tx.rollback();
        _tx = null;
    }


    public synchronized boolean isPersistent( Object obj )
    {
        // If directory is closed or not inside transaction, return null.
        if ( _dirEngine == null || _tx == null )
            return false;
        return _tx.isPersistent( obj );
    }


    public synchronized void close()
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        _dirEngine = null;
        try {
            _conn.disconnect();
        } catch ( LDAPException except ) {
            throw new DirectoryException( except );
        } finally {
            _conn = null;
        }
    }


    public void finalizer()
    {
        try {
            if ( _conn != null )
                _conn.disconnect();
        } catch ( LDAPException except ) {
        }
    }
    
    
    synchronized TransactionContext getTransactionContext()
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        return _tx;
    }


    synchronized TransactionContext newTransactionContext()
        throws DirectoryException
    {
        if ( _dirEngine == null )
            throw new DirectoryException( "Directory closed" );
        return new TransactionContextImpl( _conn );
    }
    

    PersistenceEngine getPersistenceEngine()
    {
        return _dirEngine;
    }
    
    
    private static Hashtable  _engines = new Hashtable();
    
    
    public static PersistenceEngine getEngine( LDAPUrl url, ClassDescriptor clsDesc, LogInterceptor logInterceptor )
        throws MappingException
    {
        PersistenceEngine engine;
        
        synchronized ( _engines ) {
            engine = (PersistenceEngine) _engines.get( url );
            if ( engine == null ) {
                engine = new PersistenceEngineFactory().createEngine( new SingleMapping( clsDesc ),
                                                                      new MozillaFactory( url.getDN() ), logInterceptor );
                _engines.put( url, engine );
            }
            return engine;
        }
    }
    
    
    static class SingleMapping
        implements MappingResolver
    {
        
        private Hashtable _clsDescs;
        
        SingleMapping( ClassDescriptor clsDesc )
        {
            _clsDescs = new Hashtable();
            _clsDescs.put( clsDesc.getJavaClass(), clsDesc );
        }
        
        public ClassDescriptor getDescriptor( Class type )
        {
            return (ClassDescriptor) _clsDescs.get( type );
        }
        
        public Enumeration listDescriptors()
        {
            return _clsDescs.elements();
        }
        
        public Enumeration listJavaClasses()
        {
            return _clsDescs.keys();
        }

        public ClassLoader getClassLoader()
        {
            return null;
        }
        
    }
    
    
}

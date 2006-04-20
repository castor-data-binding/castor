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


package org.exolab.castor.jdo.engine;


import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.SystemException;
import javax.transaction.Synchronization;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.ObjectNotPersistentException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.PersistenceInfo;
import org.exolab.castor.persist.PersistenceInfoGroup;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.util.Messages;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;




/**
 * An implementation of the JDO database supporting explicit transaction
 * demaracation.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DatabaseImpl
    implements Database, Synchronization
{


    /**
     * The database engine used to access the underlying SQL database.
     */
    //protected LockEngine   _dbEngine;
    protected PersistenceInfoGroup  _scope;

    /**
     * The transaction context is this database was accessed with an
     * {@link XAResource}.
     */
    protected TransactionContext  _ctx;


    /**
     * The lock timeout for this database. Zero for immediate
     * timeout, an infinite value for no timeout. The timeout is
     * specified in seconds.
     */
    private int                _lockTimeout;


    /**
     * The log interceptor to which all logging and tracing messages will be sent.
     */
    private LogInterceptor    _logInterceptor;


    /**
     * The name of this database.
     */
    private String             _dbName;


    /**
     * True if the transaction is listed as synchronized and
     * subordinate to this transaction.
     */
    private Transaction         _transaction;


    /*
     * True if user prefer all reachable object to be stored automatically.
     * False if user want only dependent object to be stored.
     */
    private boolean _autoStore;

    /**
     * The class loader for application classes (may be null).
     */
    private ClassLoader _classLoader;

    public DatabaseImpl( String dbName, int lockTimeout, LogInterceptor logInterceptor,
                         Transaction transaction, ClassLoader classLoader )
            throws DatabaseNotFoundException {
        // Locate a suitable datasource and database engine
        // and report if not mapping registered any of the two.
        // A new ODMG engine is created each time with different
        // locking mode.
        DatabaseRegistry dbs;
        
        dbs = DatabaseRegistry.getDatabaseRegistry( dbName );
        if ( dbs == null )
            throw new DatabaseNotFoundException( Messages.format( "jdo.dbNoMapping", dbName ) );
        LockEngine[] pe = { DatabaseRegistry.getLockEngine( dbs ) };
        _scope = new PersistenceInfoGroup( pe );
        _logInterceptor = logInterceptor;
        _dbName = dbName;
        _lockTimeout = lockTimeout;

        _transaction = transaction;
        if ( _transaction != null ) {
            _ctx = new TransactionContextImpl( this, true );
            _ctx.setLockTimeout( _lockTimeout );
            _ctx.setAutoStore( _autoStore );
        }
        _classLoader = classLoader;
    }

    LockEngine getLockEngine()
    {
        return _scope.getLockEngine();
    }
    public PersistenceInfoGroup getScope() {
        return _scope;
    }

    /*
     * True if user prefer all reachable object to be stored automatically.
     * False if user want only dependent object to be stored.
     */
    public void setAutoStore( boolean autoStore ) {
        _autoStore = autoStore;
    }

    /*
     * Return if the current transaction is set to autoStore, it there is
     * transaction active. If there is no active transaction, return if 
     * the next transaction will be set to autoStore.
     */
    public boolean isAutoStore() {
        if ( _ctx != null )
            return _ctx.isAutoStore();
        else
            return _autoStore;
    }

    /**
     * Gets the current application ClassLoader's instance. 
     * For use in OQLQueryImpl and TransactionContext.
     * @return the current ClassLoader's instance, or <code>null</code> if not provided
     */
    public ClassLoader getClassLoader()
    {
        return _classLoader;
    }
    

    public synchronized void close()
        throws PersistenceException
    {
        try {
            if ( _transaction == null ) {
                if ( _ctx != null && _ctx.isOpen() ) {
                    try {
                        _ctx.rollback();
                    } catch ( Exception except ) {
                    }
                    try {
                        _ctx.close();
                    } catch ( Exception except ) {
                    }
                     throw new PersistenceException( "jdo.dbClosedTxRolledback" );
                }
            }
        } finally {
            _scope = null;
        }
    }


    public boolean isClosed()
    {
        return ( _scope == null );
    }

    public Object load( Class type, Complex identity )
            throws TransactionNotInProgressException, ObjectNotFoundException,
            LockNotGrantedException, PersistenceException {
    
        return load( type, (Object)identity );
    }

    public Object load( Class type, Object identity) throws ObjectNotFoundException, LockNotGrantedException, TransactionNotInProgressException, PersistenceException {
        TransactionContext tx;
        PersistenceInfo    info;

        tx = getTransaction();
        info = _scope.getPersistenceInfo( type );
        if ( info == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", type.getName() ) );

        return tx.load( info.engine, info.molder, identity, null );
    }

    public Object load( Class type, Complex identity, short accessMode )
            throws TransactionNotInProgressException, ObjectNotFoundException,
            LockNotGrantedException, PersistenceException {

        return load( type, (Object)identity, accessMode );
    }

    public Object load( Class type, Object identity, short accessMode) throws ObjectNotFoundException, LockNotGrantedException, TransactionNotInProgressException, PersistenceException {
        TransactionContext tx;
        PersistenceInfo    info;
        AccessMode         mode;

        switch ( accessMode ) {
        case ReadOnly:
            mode = AccessMode.ReadOnly;
            break;
        case Shared:
            mode = AccessMode.Shared;
            break;
        case Exclusive:
            mode = AccessMode.Exclusive;
            break;
        case DbLocked:
            mode = AccessMode.DbLocked;
            break;
        default:
            throw new IllegalArgumentException( "Value for 'accessMode' is invalid" );
        }

        tx = getTransaction();
        info = _scope.getPersistenceInfo( type );
        if ( info == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", type.getName()) );
        
        return tx.load( info.engine, info.molder, identity, mode );
    }

    public void create( Object object )
            throws ClassNotPersistenceCapableException, DuplicateIdentityException,
            TransactionNotInProgressException, PersistenceException {
        TransactionContext tx;
        PersistenceInfo    info;

        tx = getTransaction();
        info = _scope.getPersistenceInfo( object.getClass() );
        if ( info == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", object.getClass().getName()) );

        tx.create( info.engine, info.molder, object, null );
    }

    public void update( Object object )
        throws ClassNotPersistenceCapableException, ObjectModifiedException,
               TransactionNotInProgressException, PersistenceException
    {
        TransactionContext tx;
        PersistenceInfo    info;

        tx = getTransaction();
        info = _scope.getPersistenceInfo( object.getClass() );
        if ( info == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", object.getClass().getName()) );

        tx.update( info.engine, info.molder, object, null );
    }


    /**
     * @deprecated
     */
    public synchronized void makePersistent( Object object )
        throws ClassNotPersistenceCapableException, DuplicateIdentityException,
               TransactionNotInProgressException, PersistenceException
    {
        create( object );
    }


    public void remove( Object object )
        throws ObjectNotPersistentException, LockNotGrantedException, 
               TransactionNotInProgressException, PersistenceException
    {
        TransactionContext tx;
        PersistenceInfo info;
        
        tx = getTransaction();
        info = _scope.getPersistenceInfo( object.getClass() );
        if ( info == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", object.getClass().getName()) );

        tx.delete( object );
    }


    /**
     * @deprecated
     */
    public synchronized void deletePersistent( Object object )
        throws ObjectNotPersistentException, LockNotGrantedException, 
               PersistenceException
    {
        remove( object );
    }

    public boolean isPersistent( Object object )
    {
        TransactionContext tx;
        
        if ( _scope == null )
            throw new IllegalStateException( Messages.message( "jdo.dbClosed" ) );
        if ( _ctx != null && _ctx.isOpen()  )
            return _ctx.isPersistent( object );
        return false;
    }

    public Object getIdentity(Object object)
    {
        TransactionContext tx;
        
        if ( _scope == null )
            throw new IllegalStateException( Messages.message( "jdo.dbClosed" ) );
        if ( _ctx != null && _ctx.isOpen()  )
            return _ctx.getIdentity( object );
        return null;
    }


    public void lock( Object object )
        throws LockNotGrantedException, ObjectNotPersistentException,
               TransactionNotInProgressException,  PersistenceException
    {
        if ( _ctx == null || ! _ctx.isOpen() )
            throw new TransactionNotInProgressException( Messages.message( "jdo.txNotInProgress" ) );
        _ctx.writeLock( object, _lockTimeout );
    }


    public OQLQuery getOQLQuery()
    {
        return new OQLQueryImpl( this );
    }


    public OQLQuery getOQLQuery( String oql )
        throws QueryException
    {
        OQLQuery oqlImpl;

        oqlImpl = new OQLQueryImpl( this );
        oqlImpl.create( oql );
        return oqlImpl;
    }
    
    
    public Query getQuery()
    {
        return new OQLQueryImpl( this );
    }


    protected void finalize()
        throws Throwable
    {
        if ( _scope != null )
            close();
    }


    protected TransactionContext getTransaction()
        throws TransactionNotInProgressException
    {
        TransactionContext tx;
        
        if ( _scope == null )
            throw new IllegalStateException( Messages.message( "jdo.dbClosed" ) );
        if ( _ctx != null && _ctx.isOpen()  )
            return _ctx;
        throw new TransactionNotInProgressException( Messages.message( "jdo.dbTxNotInProgress" ) );
    }


    public void begin()
        throws PersistenceException
    {
        if ( _transaction != null )
            throw new IllegalStateException( Messages.message( "jdo.txInJ2EE" ) );

        if ( _ctx != null && _ctx.isOpen() )
            throw new PersistenceException( Messages.message( "jdo.txInProgress" ) );

        _ctx = new TransactionContextImpl( this, false );
        _ctx.setLockTimeout( _lockTimeout );
        _ctx.setAutoStore( _autoStore );
    }


    public void commit()
        throws TransactionNotInProgressException, TransactionAbortedException
    {

        if ( _transaction != null )
            throw new IllegalStateException( Messages.message( "jdo.txInJ2EE" ) );

        if ( _ctx == null || ! _ctx.isOpen() )
            throw new TransactionNotInProgressException( Messages.message( "jdo.txNotInProgress" ) );
        if ( _ctx.getStatus() == Status.STATUS_MARKED_ROLLBACK )
            throw new TransactionAbortedException( Messages.message( "jdo.txAborted" ) );
        try {
            _ctx.prepare();
            _ctx.commit();
        } catch ( TransactionAbortedException except ) {
            _ctx.rollback();
            throw except;
        } finally {
             try {
                _ctx.close();
            } catch (Exception ex) {
            }
           _ctx = null;
        }
    }


    public void rollback()
        throws TransactionNotInProgressException
    {
        if ( _transaction != null )
            throw new IllegalStateException( Messages.message( "jdo.txInJ2EE" ) );

        // If inside XA transation throw IllegalStateException
        if ( _ctx == null || ! _ctx.isOpen() )
            throw new TransactionNotInProgressException( Messages.message( "jdo.txNotInProgress" ) );
        _ctx.rollback();
        _ctx = null;
    }


    public void beforeCompletion()
    {
        if ( _transaction == null || _ctx == null || ! _ctx.isOpen() )
            throw new IllegalStateException( Messages.message( "jdo.txNotInProgress" ) );
        if ( _ctx.getStatus() == Status.STATUS_MARKED_ROLLBACK ) {
            try {
                _transaction.setRollbackOnly();
            } catch ( SystemException except ) {
                if ( _logInterceptor != null )
                    _logInterceptor.exception( except );
            }
            return;
        }
        try {
            _ctx.prepare();
        } catch ( TransactionAbortedException except ) {
            if ( _logInterceptor != null )
                _logInterceptor.exception( except );
            try {
                _transaction.setRollbackOnly();
            } catch ( SystemException except2 ) {
                if ( _logInterceptor != null )
                    _logInterceptor.exception( except2 );
            }
            _ctx.rollback();
        }
    }


    public void afterCompletion( int status )
    {
        if ( _transaction == null || _ctx == null || ! _ctx.isOpen() )
            throw new IllegalStateException( Messages.message( "jdo.txNotInProgress" ) );
        if ( _ctx.getStatus() == Status.STATUS_ROLLEDBACK )
            return;
        if ( _ctx.getStatus() != Status.STATUS_PREPARED )
            throw new IllegalStateException( "Unexpected state: afterCompletion called at status " + _ctx.getStatus() );
        switch ( status ) {
        case Status.STATUS_COMMITTED:
            try {
                _ctx.commit();
            } catch ( TransactionAbortedException except ) {
                if ( _logInterceptor != null )
                    _logInterceptor.exception( except );
                _ctx.rollback();
            }
            _ctx = null;
            return;
        case Status.STATUS_ROLLEDBACK:
            _ctx.rollback();
            _ctx = null;
            return;
        default:
            _ctx.rollback();
            _ctx = null;
            throw new IllegalStateException( "Unexpected state: afterCompletion called with status " + status );
        }
    }


    public boolean isActive()
    {
        return ( _ctx != null && _ctx.isOpen() );
    }


    /**
     * @deprecated Use {@link #commit} and {@link #rollback} instead
     */
    public void checkpoint()
        throws TransactionNotInProgressException, TransactionAbortedException
    {
    }


    public String toString()
    {
        return _dbName;
    }



    /**
     * Get the underlying JDBC Connection.
     * Only for internal / advanced use !
     * Never try to close it (is done by castor).
     */
    public Object /* java.sql.Connection */ getConnection()
            throws org.exolab.castor.jdo.PersistenceException
    {
        return _ctx.getConnection( _scope.getLockEngine() );
    }

}

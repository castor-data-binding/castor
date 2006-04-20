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


package org.exolab.castor.jdo.engine;


import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.SystemException;
import javax.transaction.Synchronization;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.ObjectNotPersistentException;
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
import org.exolab.castor.persist.ClassHandler;
import org.exolab.castor.persist.TransactionContext;
import org.exolab.castor.persist.PersistenceEngine;
import org.exolab.castor.persist.PersistenceExceptionImpl;
import org.exolab.castor.persist.ClassNotPersistenceCapableExceptionImpl;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.util.Messages;


/**
 * An implementation of the JDO database supporting explicit transaction
 * demaracation.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DatabaseImpl
    implements Database, Synchronization
{


    /**
     * The database engine used to access the underlying SQL database.
     */
    protected PersistenceEngine   _dbEngine;


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


    public DatabaseImpl( String dbName, int lockTimeout, LogInterceptor logInterceptor,
                         Transaction transaction )
        throws DatabaseNotFoundException
    {
        // Locate a suitable datasource and database engine
        // and report if not mapping registered any of the two.
        // A new ODMG engine is created each time with different
        // locking mode.
        DatabaseRegistry dbs;
        
        dbs = DatabaseRegistry.getDatabaseRegistry( dbName );
        if ( dbs == null )
            throw new DatabaseNotFoundException( Messages.format( "jdo.dbNoMapping", dbName ) );
        _dbEngine = DatabaseRegistry.getPersistenceEngine( dbs );
        _logInterceptor = logInterceptor;
        _dbName = dbName;
        _lockTimeout = lockTimeout;

        _transaction = transaction;
        if ( _transaction != null ) {
            _ctx = new TransactionContextImpl( true );
            _ctx.setLockTimeout( _lockTimeout );
        }
    }


    PersistenceEngine getPersistenceEngine()
    {
        return _dbEngine;
    }


    public synchronized void close()
        throws PersistenceException
    {
        try {
            if ( _ctx != null && _ctx.isOpen() ) {
                try {
                    _ctx.rollback();
                } catch ( Exception except ) {
                }
                throw new PersistenceExceptionImpl( "jdo.dbClosedTxRolledback" );
            }
        } finally {
            _ctx = null;
            _dbEngine = null;
        }
    }


    public boolean isClosed()
    {
        return ( _dbEngine == null );
    }


    public Object load( Class type, Object identity )
        throws TransactionNotInProgressException, ObjectNotFoundException,
               LockNotGrantedException, PersistenceException
    {
        TransactionContext tx;
        ClassHandler       handler;
        Object             object;

        tx = getTransaction();
        handler = _dbEngine.getClassHandler( type );
        if ( handler == null )
            throw new ClassNotPersistenceCapableExceptionImpl( type );
        object = tx.fetch( _dbEngine, handler, identity, null );
        if ( object == null ) {
            object = handler.newInstance();
            tx.load( _dbEngine, handler, object, identity, null );
        }
        return object;
    }


    public Object load( Class type, Object identity, short accessMode )
        throws TransactionNotInProgressException, ObjectNotFoundException,
               LockNotGrantedException, PersistenceException
    {
        TransactionContext tx;
        ClassHandler       handler;
        AccessMode         mode;
        Object             object;

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
        handler = _dbEngine.getClassHandler( type );
        if ( handler == null )
            throw new ClassNotPersistenceCapableExceptionImpl( type );
        object = tx.fetch( _dbEngine, handler, identity, mode );
        if ( object == null ) {
            object = handler.newInstance();
            tx.load( _dbEngine, handler, object, identity, mode );
        }
        return object;
    }


    public void create( Object object )
        throws ClassNotPersistenceCapableException, DuplicateIdentityException,
               TransactionNotInProgressException, PersistenceException
    {
        TransactionContext tx;
        ClassHandler       handler;

        tx = getTransaction();
        handler = _dbEngine.getClassHandler( object.getClass() );
        if ( handler == null )
            throw new ClassNotPersistenceCapableExceptionImpl( object.getClass() );
        tx.create( _dbEngine, object, handler.getIdentity( object ) );
    }


    public void update( Object object )
        throws ClassNotPersistenceCapableException, DuplicateIdentityException,
               TransactionNotInProgressException, PersistenceException
    {
        TransactionContext tx;
        ClassHandler       handler;

        tx = getTransaction();
        handler = _dbEngine.getClassHandler( object.getClass() );
        if ( handler == null )
            throw new ClassNotPersistenceCapableExceptionImpl( object.getClass() );
        tx.update( _dbEngine, object, handler.getIdentity( object ) );
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
        
        tx = getTransaction();
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
        
        if ( _dbEngine == null )
            throw new IllegalStateException( Messages.message( "jdo.dbClosed" ) );
        if ( _ctx != null && _ctx.isOpen()  )
            return _ctx.isPersistent( object );
        return false;
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
    
    
    protected void finalize()
        throws Throwable
    {
        if ( _dbEngine != null )
            close();
    }


    protected TransactionContext getTransaction()
        throws TransactionNotInProgressException
    {
        TransactionContext tx;
        
        if ( _dbEngine == null )
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
        _ctx = new TransactionContextImpl( false );
        _ctx.setLockTimeout( _lockTimeout );
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


}






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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.*;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.*;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Messages;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;


/**
 * An implementation of the JDO database supporting explicit transaction
 * demarcation.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date$
 */
public class DatabaseImpl
    implements Database, Synchronization
{

    /**
     * Property listing all the available {@link TxSynchronizable}
     * implementations (<tt>org.exolab.castor.persit.TxSynchronizable</tt>).
     */
    private static final String TxSynchronizableProperty = 
        "org.exolab.castor.persist.TxSynchronizable";


    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance( DatabaseImpl.class );


    /**
     * The database engine used to access the underlying SQL database.
     */
    //protected LockEngine   _dbEngine;
    protected PersistenceInfoGroup     _scope;


    /**
     * The transaction context is this database was accessed with an
     * {@link javax.transaction.xa.XAResource}.
     */
    protected TransactionContext       _ctx;


    /**
     * List of TxSynchronizeable implementations that should all be
     * informed about changes after commit of transactions. 
     */
    private ArrayList                  _synchronizables;


    /**
     * The lock timeout for this database. Zero for immediate
     * timeout, an infinite value for no timeout. The timeout is
     * specified in seconds.
     */
    private int                        _lockTimeout;


    /**
     * The default callback interceptor for transaction
     */
    private CallbackInterceptor        _callback;

    /**
     * The instance factory to that creates new instances of data object
     */
    private InstanceFactory            _instanceFactory;

    /**
     * The name of this database.
     */
    private String                     _dbName;


    /**
     * True if the transaction is listed as synchronized and
     * subordinate to this transaction.
     */
    private Transaction                _transaction;


    /**
     * True if user prefer all reachable object to be stored automatically.
     * False if user want only dependent object to be stored.
     */
    private boolean                    _autoStore;


    /**
     * The class loader for application classes (may be null).
     */
    private ClassLoader                _classLoader;


    /**
     * The transaction to database map
     */
    private TxDatabaseMap              _txMap;
    
    /**
	 * {@link CacheManager} instance.
	 */
    private CacheManager cacheManager;

    public DatabaseImpl( String dbName, int lockTimeout, CallbackInterceptor callback,
                         InstanceFactory instanceFactory, Transaction transaction, 
                         ClassLoader classLoader, boolean autoStore )
        throws DatabaseNotFoundException 
    {
        // Locate a suitable datasource and database engine
        // and report if not mapping registered any of the two.
        // A new ODMG engine is created each time with different
        // locking mode.
        DatabaseRegistry dbs;

        _autoStore = autoStore;
        _log.debug( "Fetching DatabaseRegistry: " + dbName );
        dbs = DatabaseRegistry.getDatabaseRegistry( dbName );
        if ( dbs == null )
            throw new DatabaseNotFoundException( Messages.format( "jdo.dbNoMapping", dbName ) );
        LockEngine[] pe = { DatabaseRegistry.getLockEngine( dbs ) };
        _scope = new PersistenceInfoGroup( pe );
        _callback = callback;
        _instanceFactory = instanceFactory;
        _dbName = dbName;
        _lockTimeout = lockTimeout;

        _transaction = transaction;
        if (_transaction != null) {
            _ctx = new TransactionContextImpl(this, true);
        } else {
            _ctx = new TransactionContextImpl(this, false);
        }
        _ctx.setLockTimeout(_lockTimeout);
        _ctx.setAutoStore(_autoStore);
        _ctx.setCallback(_callback);
        _ctx.setInstanceFactory(_instanceFactory);
        _classLoader = classLoader;
        
        loadSynchronizables();
    }

    LockEngine getLockEngine()
    {
        return _scope.getLockEngine();
    }

    public PersistenceInfoGroup getScope() {
        return _scope;
    }

    /**
     * True if user prefer all reachable object to be stored automatically.
     * False if user want only dependent object to be stored.
     */
    public void setAutoStore( boolean autoStore ) {
        _autoStore = autoStore;
    }

    /**
     * Return if the current transaction is set to autoStore, it there is
     * transaction active. If there is no active transaction, return if
     * the next transaction will be set to autoStore.
     */
    public boolean isAutoStore() {
        if ( _ctx != null )
            return _ctx.isAutoStore();

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
    

    /**                              }
     * Return the name of the database
     */                               
    public String getDatabaseName()  
    {                                
        return _dbName;                  
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
                     throw new PersistenceException( Messages.message( "jdo.dbClosedTxRolledback" ) );
                }
            }
            else if ( _ctx != null && _ctx.isOpen() )
            {
                try
                {
                    _ctx.close();
                }
                catch( Exception e )
                {
                    throw new PersistenceException( e.getMessage() );
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

    public Object load( Class type, Object identity, Object object ) 
            throws TransactionNotInProgressException, ObjectNotFoundException,
            LockNotGrantedException, PersistenceException {

        TransactionContext tx;
        PersistenceInfo    info;

        tx = getTransaction();
        info = _scope.getPersistenceInfo( type );
        if ( info == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", type.getName() ) );

        return tx.load( info.engine, info.molder, identity, object, null );
    }
    public Object load( Class type, Complex identity )
            throws TransactionNotInProgressException, ObjectNotFoundException,
            LockNotGrantedException, PersistenceException {
    
        return load( type, (Object)identity, null );
    }

    public Object load( Class type, Object identity ) 
            throws ObjectNotFoundException, LockNotGrantedException, 
            TransactionNotInProgressException, PersistenceException {

        return load( type, identity, null );
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
        
        return tx.load( info.engine, info.molder, identity, null, mode );
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
    
    /**
     * Get's the CacheManager-instance.
     * Call getCacheManager for every Database-instances.
     * 
     * @return the CacheManager-instance.
     */
    public CacheManager getCacheManager() {
        if(cacheManager == null)
            cacheManager = new CacheManager(this, _ctx, getLockEngine());
        return cacheManager;
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


    public boolean isPersistent( Object object )
    {
        if ( _scope == null )
            throw new IllegalStateException( Messages.message( "jdo.dbClosed" ) );
        if ( _ctx != null && _ctx.isOpen()  )
            return _ctx.isPersistent( object );
        return false;
    }

    public Object getIdentity(Object object)
    {
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
        throws PersistenceException
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


	/**
     * Overrides Object.finalize().
	 * 
	 * Outputs a warning message to teh logs if the current DatabaseImpl 
	 * instance still has valid scope. In this condition - a condition that 
	 * ideally should not occur at all - we close the instance as well to 
	 * free up resources.
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		if (_scope != null) {
			
			// retrieve SQL bound to this Database instance
			OQLQuery oqlQuery = getOQLQuery(); 
			String sql = ((OQLQueryImpl) oqlQuery).getSQL(); 
			
			_log.warn(Messages.format("jdo.finalize_close", this.toString(), _dbName, sql));
			close();
		}
	}


    protected TransactionContext getTransaction()
        throws TransactionNotInProgressException
    {
        if ( _scope == null )
            throw new TransactionNotInProgressException( Messages.message( "jdo.dbClosed" ) );
        if ( _ctx != null && _ctx.isOpen()  )
            return _ctx;
        throw new TransactionNotInProgressException( Messages.message( "jdo.dbTxNotInProgress" ) );
    }


    public void begin()
        throws PersistenceException
    {
        _log.debug( "Beginning tx" );

        if ( _transaction != null )
            throw new IllegalStateException( Messages.message( "jdo.txInJ2EE" ) );

        if ( _ctx != null && _ctx.isOpen() )
            throw new PersistenceException( Messages.message( "jdo.txInProgress" ) );

        //_ctx = new TransactionContextImpl( this, false );
        ((TransactionContextImpl) _ctx).setStatusActive();
        _ctx.setLockTimeout( _lockTimeout );
        _ctx.setAutoStore( _autoStore );
        _ctx.setCallback( _callback );
        _ctx.setInstanceFactory( _instanceFactory );

        registerSynchronizables();
    }


    public void commit()
        throws TransactionNotInProgressException, TransactionAbortedException
    {
        _log.debug( "Committing tx" );

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
        }

        unregisterSynchronizables();
    }


    public void rollback()
        throws TransactionNotInProgressException
    {
        _log.debug( "Rolling back tx" );

        if ( _transaction != null )
            throw new IllegalStateException( Messages.message( "jdo.txInJ2EE" ) );

        // If inside XA transation throw IllegalStateException
        if ( _ctx == null || ! _ctx.isOpen() )
            throw new TransactionNotInProgressException( Messages.message( "jdo.txNotInProgress" ) );
        _ctx.rollback();

        unregisterSynchronizables();
    }


    public void beforeCompletion()
    {
        // XXX [SMH]: Find another test for txNotInProgress
        if ( _transaction == null || _ctx == null || ! _ctx.isOpen() )
            throw new IllegalStateException( Messages.message( "jdo.txNotInProgress" ) );
        if ( _ctx.getStatus() == Status.STATUS_MARKED_ROLLBACK ) {
            try {
                _transaction.setRollbackOnly();
            } catch ( SystemException except ) {
                _log.warn( Messages.format( "jdo.warnException", except ) );
            }
            return;
        }
        try {
            _ctx.prepare();
        } catch ( TransactionAbortedException except ) {
            _log.fatal( Messages.format( "jdo.fatalException", except ) );
            try {
                _transaction.setRollbackOnly();
            } catch ( SystemException except2 ) {
                _log.fatal( Messages.format( "jdo.fatalException", except2 ) );
            }
            _ctx.rollback();
        }
    }


    public void afterCompletion( int status )
    {
        try {
            // XXX [SMH]: Find another test for txNotInProgress
            if ( _transaction == null || _ctx == null )
                throw new IllegalStateException( Messages.message( "jdo.txNotInProgress" ) );
            if ( _ctx.getStatus() == Status.STATUS_ROLLEDBACK )
                return;
            if ( _ctx.getStatus() != Status.STATUS_PREPARED && status != Status.STATUS_ROLLEDBACK )
                throw new IllegalStateException( "Unexpected state: afterCompletion called at status " + _ctx.getStatus() );
            switch ( status ) {
            case Status.STATUS_COMMITTED:
                try {
                    _ctx.commit();
                } catch ( TransactionAbortedException except ) {
                    _log.fatal( Messages.format( "jdo.fatalException", except ) );
                    _ctx.rollback();
                }
                return;
            case Status.STATUS_ROLLEDBACK:
                _ctx.rollback();
                return;
            default:
                _ctx.rollback();
                throw new IllegalStateException( "Unexpected state: afterCompletion called with status " + status );
            }
        } finally {
            if ( _txMap != null && _transaction != null ) {
                _txMap.remove( _transaction );
                _txMap = null;
            }
        }
    }

    void setTxMap( TxDatabaseMap txMap ) 
    {
        _txMap = txMap;
    }

    public boolean isActive()
    {
        return ( _ctx != null && _ctx.isOpen() );
    }


    public String toString() {
        return super.toString()+":"+_dbName;
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

    /**
     * Load the {@link TxSynchronizable} implementations from the 
     * properties file, if not loaded before.
     */
    private void loadSynchronizables()
    {
    	if ( _synchronizables == null ) {
    		_synchronizables = new ArrayList();
    		
    		String syncName = LocalConfiguration.getInstance().getProperty( TxSynchronizableProperty, "" );
    		StringTokenizer tokenizer = new StringTokenizer( syncName, ", " );
    		while ( tokenizer.hasMoreTokens() ) {
    			syncName = tokenizer.nextToken();
    			try {
                	Class cls = null;
                	if (_classLoader != null) {
                		cls = _classLoader.loadClass( syncName );
                	} else {
                		cls = Class.forName( syncName );
                	}
    				TxSynchronizable sync = (TxSynchronizable)cls.newInstance();
    				if ( sync != null ) _synchronizables.add(sync);
    			} catch ( Exception except ) {
    				_log.warn(Messages.format( "jdo.missingTxSynchronizable", syncName ));
    			}
    		}
    		
    		if (_synchronizables.size() == 0) _synchronizables = null;
    	}
    }
    
    /**
     * Register the {@link TxSynchronizable} implementations at the
     * TransactionContect at end of begin().
     */
    private void registerSynchronizables()
    {
    	if ( _synchronizables != null && _synchronizables.size() > 0) {
    		Iterator iter = _synchronizables.iterator();
    		while ( iter.hasNext() ) {
    			_ctx.addTxSynchronizable((TxSynchronizable)iter.next());
    		}
    	}
    }
    
    /**
     * Unregister the {@link TxSynchronizable} implementations at the
     * TransactionContect after commit() or rollback().
     */
    private void unregisterSynchronizables()
    {
    	if ( _synchronizables != null  && _synchronizables.size() > 0) {
    		Iterator iter = _synchronizables.iterator();
    		while ( iter.hasNext() ) {
    			_ctx.removeTxSynchronizable((TxSynchronizable)iter.next());
    		}
    	}
    }
    /**
     * Expire objects from the cache.  Objects expired from the cache will be
     * read from persistent storage, as opposed to being read from the
     * performance cache, during subsequent load/query operations.
     *
     * Objects may be expired from the cache individually, using explicit
     * type/identity pairs in the argument list, or whole classes of objects
     * may be expired by specifying a class type without a corresponding 
     * entry in the identity array.
     *
     * Objects contained within a "master" object, for example objects
     * maintained in a one-to-many relationship, will automatically be expired
     * from the cache without the need to explicitly identify them.  This does
     * not apply when expiring objects by type.  Each type, both container and
     * contained objects need to be specified.
     * 
     * @param type An array of class types.
     * @param identity An array of object identifiers.
     * @deprecated Please use the new {@link org.exolab.castor.jdo.CacheManager} which can be 
     * obtained by calling {@link #getCacheManager()}.
     */
    public void expireCache( Class[] type, Object[] identity )
        throws PersistenceException 
    {
    	throw new PersistenceException ("Please use the new CacheManager to manage Castor performance caches.");
    }
    
}  
                                

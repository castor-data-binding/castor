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


package org.exolab.castor.persist;


import java.util.Hashtable;
import java.util.Enumeration;
import javax.transaction.Status;
import javax.transaction.xa.Xid;
import javax.transaction.xa.XAResource;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.ObjectNotPersistentException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.util.Messages;


/**
 * A transaction context is required in order to perform operations
 * against the database. The transaction context is mapped to an
 * API transaction or an XA transaction. The only way to begin a
 * new transaction is through the creation of a new transaction context.
 * A transaction context is created from an implementation class directly
 * or through {@link XAResourceImpl}.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class TransactionContext
{


    /**
     * IMPLEMENTATION NOTES:
     *
     * An object is considered persistent only if it was queried or created
     * within the context of this transaction. An object is not persistent
     * if it was queried or created by another transactions. An object is
     * not persistent if it was queried with read-only access.
     *
     * A read lock is implicitly obtained on any object that is queried,
     * and a write lock on any object that is queried in exclusive mode,
     * created or deleted in this transaction. The lock can be upgraded to
     * a write lock.
     *
     * The validity of locks is dependent on the underlying persistence
     * engine the transaction mode. Without persistence engine locks
     * provide a strong locking mechanism, preventing objects from being
     * deleted or modified in one transaction while another transaction
     * is looking at them. With a persistent engine in exclusive mode, locks
     * exhibit the same behavior. With a persistent engine in read/write
     * mode or a persistent engine that does not support locking (e.g. LDAP)
     * an object may be deleted or modified concurrently through a direct
     * access mechanism.
     */


    /**
     * Set while transaction is waiting for a lock.
     *
     * @see #getWaitOnLock
     * @see ObjectLock
     */
    private ObjectLock  _waitOnLock;


    /**
     * Collection of objects accessed during this transaction.
     * The object is used as key and {@link ObjectEntry} is the value.
     * @see #addObjectEntry
     */
    private final Hashtable   _objects = new Hashtable();


    /**
     * A list of all the engines associated with this transaction and
     * all the OIDs loaded from them. The persistence engine is used
     * as the key, the value is a hashtable. In that hashtable {@link OID}
     * is the key and {@link ObjectEntry} is the value.
     */
    private final Hashtable   _engineOids = new Hashtable();


    /**
     * The transaction status. See {@link Status} for list of valid values.
     */
    private int         _status;


    /**
     * The timeout waiting to acquire a new lock. Specified in seconds.
     */
    private int         _lockTimeout = 30;


    /**
     * The Xid of this transaction is generated from an XA resource.
     */
    private final Xid   _xid;


    /**
     * The timeout of this transaction. Specified in seconds.
     */
    private int         _txTimeout = 30;


    /**
     * Create a new transaction context. This method is used by the
     * explicit transaction model.
     */
    public TransactionContext()
    {
        _xid = null;
        _status = Status.STATUS_ACTIVE;
    }


    /**
     * Create a new transaction context. This method is used by the
     * XA transaction model, see {@link XAResourceImpl}.
     */
    public TransactionContext( Xid xid )
    {
        _xid = xid;
        _status = Status.STATUS_ACTIVE;
    }


    /**
     * Sets the timeout of this transaction. The timeout is specified
     * in seconds.
     */
    public void setTransactionTimeout( int timeout )
    {
        _txTimeout = timeout;
    }


    /**
     * Returns the timeout of this transaction. The timeout is specified
     * in seconds.
     */
    public int getTransactionTimeout()
    {
        return _txTimeout;
    }


    /**
     * Returns the timeout waiting to acquire a lock. The timeout is
     * specified in seconds.
     */
    public int getLockTimeout()
    {
        return _lockTimeout;
    }


    /**
     * Sets the timeout waiting to acquire a lock. The timeout is
     * specified in seconds.
     */
    public void setLockTimeout( int timeout )
    {
        _lockTimeout = ( timeout >= 0 ? timeout : 0 );
    }


    /**
     * The derived class must implement this method and return an open
     * connection for the specified engine. The connection should be
     * created only one for a given engine in the same transaction.
     *
     * @param engine The persistence engine
     * @return An open connection
     * @throws PersistenceException An error occured talking to the
     *   persistence engine
     */
    public abstract Object getConnection( PersistenceEngine engine )
        throws PersistenceException;


    /**
     * The derived class must implement this method and commit all the
     * connections used in this transaction. If the transaction could
     * not commit fully or partially, this method will throw an {@link
     * TransactionAbortedException}, causing a rollback to occur as
     * the next step.
     *
     * @throws TransactionAbortedException The transaction could not
     *  commit fully or partially and should be rolled back
     */
    protected abstract void commitConnections()
        throws TransactionAbortedException;


    /**
     * The derived class must implement this method and rollback all
     * the connections used in this transaction. The connections may
     * be closed, as they will not be reused in this transaction.
     * This operation is guaranteed to succeed.
     */
    protected abstract void rollbackConnections();


    public synchronized Object fetch( PersistenceEngine engine, ClassHandler handler,
                                      Object identity, AccessMode accessMode )
        throws ObjectNotFoundException, LockNotGrantedException, PersistenceException
    {
        ObjectEntry entry;
        OID         oid;

        oid = new OID( handler, identity );
        entry = getObjectEntry( engine, oid );
        if ( entry != null ) {
            // If the object has been loaded in this transaction from a
            // different engine this is an error. If the object has been
            // deleted in this transaction, it cannot be re-loaded. If the
            // object has been created in this transaction, it cannot be
            // re-loaded but no error is reported.
            if ( entry.engine != engine )
                throw new PersistenceExceptionImpl( "persist.multipleLoad", handler.getJavaClass(), identity );
            if ( entry.deleted )
                throw new ObjectNotFoundExceptionImpl( handler.getJavaClass(), identity );
            if ( ! handler.getJavaClass().isAssignableFrom( entry.object.getClass() ) )
                throw new PersistenceExceptionImpl( "persist.typeMismatch", handler.getJavaClass(), entry.object.getClass() );
            if ( entry.created )
                return oid;
            if ( ( accessMode == AccessMode.Exclusive ||
                   accessMode == AccessMode.DbLocked ) && ! entry.oid.isDbLock() ) {
                // If we are in exclusive mode and object has not been
                // loaded in exclusive mode before, then we have a
                // problem. We cannot return an object that is not
                // synchronized with the database, but we cannot
                // synchronize a live object.
                throw new PersistenceExceptionImpl( "persist.lockConflict", handler.getJavaClass(), identity );
            }
            return entry.object;
        }
        return null;
    }


    /**
     * Obtains an object for use within the transaction. Before an
     * object can be used in a transaction it must be fetched.
     * Multiple access to the same object within the transaction will
     * return the same object instance (except for read-only access).
     * This method is similar to {@link #fetch} except that it will
     * load the object only once within a transaction and always
     * return the same instance.
     * <p>
     * If the object is loaded for read-only then no lock is acquired
     * and updates to the object are not reflected at commit time.
     * If the object is loaded for read-write then a read lock is
     * acquired (unless timeout or deadlock detected) and the object
     * is stored at commit time. The object is then considered persistent
     * and may be deleted or upgraded to write lock. If the object is
     * loaded for exclusive access then a write lock is acquired and the
     * object is synchronized with the persistent copy.
     * <p>
     * Attempting to load the object twice in the same transaction, once
     * with exclusive lock and once with read-write lock will result in
     * an exception.
     *
     * @param engine The persistence engine
     * @param handler The class persistence handler
     * @param object The object to fetch (single instance per transaction)
     * @param identity The object's identity
     * @param accessMode The access mode (see {@link AccessMode})
     *  the values in persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public synchronized void load( PersistenceEngine engine, ClassHandler handler,
                                   Object object, Object identity, AccessMode accessMode )
        throws ObjectNotFoundException, LockNotGrantedException, PersistenceException
    {
        ObjectEntry entry;
        OID         oid;

        oid = new OID( handler, identity );
        entry = getObjectEntry( object );
        if ( entry != null ) {
            // XXX Need to report object loaded with different identity
            if ( entry.oid.getIdentity().equals( identity ) )
                throw new PersistenceExceptionImpl( "persist.multipleLoad", handler.getJavaClass(), identity );

            // If the object has been loaded in this transaction from a
            // different engine this is an error. If the object has been
            // deleted in this transaction, it cannot be re-loaded. If the
            // object has been created in this transaction, it cannot be
            // re-loaded but no error is reported.
            if ( entry.engine != engine )
                throw new PersistenceExceptionImpl( "persist.multipleLoad", handler.getJavaClass(), identity );
            if ( entry.deleted )
                throw new ObjectNotFoundExceptionImpl( handler.getJavaClass(), identity );
            if ( ! handler.getJavaClass().isAssignableFrom( entry.object.getClass() ) )
                throw new PersistenceExceptionImpl( "persist.typeMismatch", handler.getJavaClass(), entry.object.getClass() );
            if ( entry.created )
                return;
            if ( ( accessMode == AccessMode.Exclusive ||
                   accessMode == AccessMode.DbLocked ) && ! entry.oid.isDbLock() ) {
                // If we are in exclusive mode and object has not been
                // loaded in exclusive mode before, then we have a
                // problem. We cannot return an object that is not
                // synchronized with the database, but we cannot
                // synchronize a live object.
                throw new PersistenceExceptionImpl( "persist.lockConflict", handler.getJavaClass(), identity );
            }
            return;
        }

        // Load (or reload) the object through the persistence engine with the
        // requested lock. This might report failure (object no longer exists),
        // hold until a suitable lock is granted (or fail to grant), or
        // report error with the persistence engine.
        accessMode = handler.getAccessMode( accessMode );
        try {
            oid = engine.load( this, handler.getJavaClass(), identity, accessMode, _lockTimeout );
        } catch ( ObjectNotFoundException except ) {
            throw except;
        } catch ( LockNotGrantedException except ) {
            throw except;
        } catch ( ClassNotPersistenceCapableException except ) {
            throw new PersistenceExceptionImpl( except );
        }

        // Need to copy the contents of this object from the cached
        // copy and deal with it based on the transaction semantics.
        // If the mode is read-only we release the lock and forget about
        // it in the contents of this transaction. Otherwise we record
        // the object in this transaction.
        entry = addObjectEntry( object, oid, engine );
        try {
            engine.copyObject( this, oid, object );
            if ( handler.getCallback() != null )
                handler.getCallback().loaded( object );
        } catch ( PersistenceException except ) {
            removeObjectEntry( object );
            engine.forgetObject( this, oid );
            throw except;
        } catch ( Exception except ) {
            removeObjectEntry( object );
            engine.forgetObject( this, oid );
            throw new PersistenceExceptionImpl( except );
        }
        if ( accessMode == AccessMode.ReadOnly ) {
            removeObjectEntry( object );
            engine.releaseLock( this, oid );
        }
    }


    /**
     * Perform a query using the query mechanism and in the specified
     * access mode. The query is performed in this transaction, and
     * the returned query results can only be used while this
     * transaction is open. It is assumed that the query mechanism is
     * compatible with the persistence engine.
     *
     * @param engine The persistence engine
     * @param query A query against the persistence engine
     * @param accessMode The access mode
     * @return A query result iterator
     * @throws QueryException An invalid query
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public synchronized QueryResults query( PersistenceEngine engine, PersistenceQuery query,
                                            AccessMode accessMode )
        throws QueryException, PersistenceException
    {
        // Need to execute query at this point. This will result in a
        // new result set from the query, or an exception.
        query.execute( getConnection( engine ), accessMode );
        return new QueryResults( this, engine, query, accessMode );
    }


    /**
     * Creates a new object in persistent storage and returns the
     * object's OID. The object will be persisted only if the
     * transaction commits. If an identity is provided then duplicate
     * identity check happens in this method, if no identity is
     * provided then duplicate identity check occurs when the
     * transaction completes and the object is not visible in this
     * transaction.
     *
     * @param engine The persistence engine
     * @param object The object to persist
     * @param identity The object's identity (may be null)
     * @return The object's OID
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public synchronized OID create( PersistenceEngine engine, Object object, Object identity )
        throws DuplicateIdentityException, ClassNotPersistenceCapableException, PersistenceException
    {
        OID          oid;
        ObjectEntry  entry;
        ClassHandler handler;

        // Make sure the object has not beed persisted in this transaction.
        entry = getObjectEntry( object );
        if ( entry != null ) {
            if ( entry.deleted )
                throw new ObjectDeletedExceptionImpl( object.getClass(), identity );
            throw new PersistenceExceptionImpl( "persist.objectAlreadyPersistent", object.getClass(), identity );
        }
        handler = engine.getClassHandler( object.getClass() );
        if ( handler == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", object.getClass().getName() ) );

        // Create the object. This can only happen once for each object in
        // all transactions running on the same engine, so after creation
        // add a new entry for this object and use this object as the view
        if ( identity != null && getObjectEntry( engine, new OID( handler, identity ) ) != null )
            throw new DuplicateIdentityException( Messages.format( "persist.duplicateIdentity", object.getClass().getName(), identity ) );
        oid = new OID( handler, identity );
        entry = addObjectEntry( object, oid, engine );
        entry.created = true;

        if ( identity != null ) {
            try {
                // Must perform creation after object is recorded in transaction
                // to prevent circular references.
                engine.create( this, object, identity );
            } catch ( DuplicateIdentityException except ) {
                removeObjectEntry( object );
                throw except;
            } catch ( PersistenceException except ) {
                removeObjectEntry( object );
                throw except;
            } catch ( Exception except ) {
                removeObjectEntry( object );
                throw new PersistenceExceptionImpl( except );
            }
        }
        return oid;
    }


    /**
     * Creates a new object in persistent storage and returns the
     * object's OID. The object will be persisted only if the
     * transaction commits. If an identity is provided then duplicate
     * identity check happens in this method, if no identity is
     * provided then duplicate identity check occurs when the
     * transaction completes and the object is not visible in this
     * transaction.
     *
     * @param engine The persistence engine
     * @param object The object to persist
     * @param identity The object's identity (may be null)
     * @return The object's OID
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public synchronized OID update( PersistenceEngine engine, Object object, Object identity )
        throws DuplicateIdentityException, ClassNotPersistenceCapableException, PersistenceException
    {
        OID          oid;
        ObjectEntry  entry;
        ClassHandler handler;

        // Make sure the object has not beed persisted in this transaction.
        entry = getObjectEntry( object );
        if ( entry != null ) {
            if ( entry.deleted )
                throw new ObjectDeletedExceptionImpl( object.getClass(), identity );
            throw new PersistenceExceptionImpl( "persist.objectAlreadyPersistent", object.getClass(), identity );
        }
        handler = engine.getClassHandler( object.getClass() );
        if ( handler == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", object.getClass().getName() ) );
        if ( identity == null )
            throw new PersistenceExceptionImpl( "persist.noIdentity" );

        // Update the object. This can only happen once for each object in
        // all transactions running on the same engine, so after updating
        // add a new entry for this object and use this object as the view

        if ( getObjectEntry( engine, new OID( handler, identity ) ) != null )
            throw new DuplicateIdentityException( Messages.format( "persist.duplicateIdentity", object.getClass().getName(), identity ) );
        oid = new OID( handler, identity );
        entry = addObjectEntry( object, oid, engine );
        return oid;
    }


    /**
     * Deletes the object from persistent storage. The deletion will
     * take effect only if the transaction is committed, but the
     * object is no longer viewable for the current transaction and
     * locks for access from other transactions will block until this
     * transaction completes. A write lock is acquired in order to
     * assure the object can be deleted.
     *
     * @param object The object to delete from persistent storage
     * @throws ObjectNotPersistentException The object has not been
     *  queried or created in this transaction
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public synchronized void delete( Object object )
        throws ObjectNotPersistentException, LockNotGrantedException, PersistenceException
    {
        ObjectEntry entry;

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        entry = getObjectEntry( object );
        if ( entry == null )
            throw new ObjectNotPersistentExceptionImpl( object.getClass() );
        // Cannot delete same object twice
        if ( entry.deleted )
            throw new ObjectDeletedExceptionImpl( object.getClass(), entry.oid.getIdentity() );

        // If the object has been created in this transaction and had no
        // identity we don't need to remove it from persistent storage
        if ( entry.oid.getIdentity() == null ) {
            entry.engine.releaseLock( this, entry.oid );
            removeObjectEntry( object );
            return;
        }

        ClassHandler handler;

        handler = entry.engine.getClassHandler( object.getClass() );

        // Must acquire a write lock on the object in order to delete it,
        // prevents object form being deleted while someone else is
        // looking at it.
        try {
            entry.engine.softLock( this, entry.oid, _lockTimeout );
            // Mark object as deleted. This will prevent it from being viewed
            // in this transaction and will handle it properly at commit time.
            // The write lock will prevent it from being viewed in another
            // transaction.
            entry.deleted = true;
        } catch ( ObjectDeletedException except ) {
            // Object has been deleted outside this transaction,
            // forget about it
            removeObjectEntry( object );
        }
    }


    /**
     * Acquire a write lock on the object. Read locks are implicitly
     * available when the object is queried. A write lock is only
     * granted for objects that are created or deleted or for objects
     * loaded in exclusive mode - this method can obtain such a lock
     * explicitly. If the object already has a write lock in this
     * transaction or a read lock in this transaction but no read lock
     * in any other transaction, a write lock is obtained. If this
     * object has a read lock in any other transaction this method
     * will block until the other transaction will release its lock.
     * If the specified timeout has elapsed or a deadlock has been
     * detected, an exception will be thrown but the current lock will
     * be retained.
     *
     * @param object The object to lock
     * @param timeout Timeout waiting to acquire lock, specified in
     *  seconds, zero for no waiting, negative to use the default
     *  timeout for this transaction
     * @throws ObjectNotPersistentException The object has not been
     *  queried or created in this transaction
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public synchronized void writeLock( Object object, int timeout )
        throws ObjectNotPersistentException, LockNotGrantedException, PersistenceException
    {
        ObjectEntry entry;

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        entry = getObjectEntry( object );
        if ( entry == null )
            throw new ObjectNotPersistentExceptionImpl( object.getClass() );
        if ( entry.deleted )
            throw new ObjectDeletedExceptionImpl( object.getClass(), entry.oid.getIdentity() );
        try {
            entry.engine.writeLock( this, entry.oid, timeout );
        } catch ( ObjectDeletedException except ) {
            // Object has been deleted outside this transaction,
            // forget about it
            removeObjectEntry( object );
            throw new ObjectNotPersistentExceptionImpl( object.getClass() );
        } catch ( LockNotGrantedException except ) {
            // Can't get lock, but may still keep running
            throw except;
        }
    }


    /**
     * Acquire a write lock on the object. Read locks are implicitly
     * available when the object is queried. A write lock is only
     * granted for objects that are created or deleted or for objects
     * loaded in exclusive mode - this method can obtain such a lock
     * explicitly. If the object already has a write lock in this
     * transaction or a read lock in this transaction but no read lock
     * in any other transaction, a write lock is obtained. If this
     * object has a read lock in any other transaction this method
     * will block until the other transaction will release its lock.
     * If the specified timeout has elapsed or a deadlock has been
     * detected, an exception will be thrown but the current lock will
     * be retained.
     *
     * @param object The object to lock
     * @param timeout Timeout waiting to acquire lock, specified in
     *  seconds, zero for no waiting, negative to use the default
     *  timeout for this transaction
     * @throws ObjectNotPersistentException The object has not been
     *  queried or created in this transaction
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     */
    public synchronized void softLock( Object object, int timeout )
        throws LockNotGrantedException, ObjectNotPersistentException
    {
        ObjectEntry entry;

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        entry = getObjectEntry( object );
        if ( entry == null )
            throw new ObjectNotPersistentExceptionImpl( object.getClass() );
        if ( entry.deleted )
            throw new ObjectDeletedExceptionImpl( object.getClass(), entry.oid.getIdentity() );
        try {
            entry.engine.softLock( this, entry.oid, timeout );
        } catch ( ObjectDeletedException except ) {
            // Object has been deleted outside this transaction,
            // forget about it
            removeObjectEntry( object );
            throw except;
        } catch ( LockNotGrantedException except ) {
            // Can't get lock, but may still keep running
            throw except;
        }
    }


    /**
     * Releases the lock granted on the object. The object is removed
     * from this transaction and will not participate in transaction
     * commit/abort. Any changes done to the object are lost.
     *
     * @param object The object to release the lock
     * @throws ObjectNotPersistentException The object was not queried
     *   or created in this transaction
     * @throws PersistenceException An error occured talking to the
     *   persistence engine
     */
    public synchronized void release( Object object )
        throws ObjectNotPersistentException, PersistenceException
    {
        ObjectEntry entry;

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        entry = getObjectEntry( object );
        if ( entry == null || entry.deleted )
            throw new ObjectNotPersistentExceptionImpl( object.getClass() );
        // Release the lock, forget about the object in this transaction
        entry.engine.releaseLock( this, entry.oid );
        removeObjectEntry( object );
    }


    /**
     * Prepares the transaction prior to committing it. Indicates
     * whether the transaction is read-only (i.e. no modified objects),
     * can commit, or an error has occured and the transaction must
     * be rolled back. This method performs actual storage into the
     * persistence storage. Multiple calls to this method can be done,
     * and do not release locks, allowing <tt>checkpoint</tt> to
     * occur.
     *
     * @return True if the transaction can commit, false if the
     *   transaction is read only
     * @throws  IllegalStateException Method called if transaction is
     *  not in the proper state to perform this operation
     * @throws TransactionAbortedException The transaction has been
     *   aborted due to inconsistency, duplicate object identity, error
     *   with the persistence engine or any other reason
     */
    public synchronized boolean prepare()
        throws TransactionAbortedException
    {
        Enumeration enum;
        ObjectEntry entry;
        int         count;

        if ( _status == Status.STATUS_MARKED_ROLLBACK )
            throw new TransactionAbortedExceptionImpl( "persist.markedRollback" );
        if ( _status != Status.STATUS_ACTIVE )
            throw new IllegalStateException( Messages.message( "persist.noTransaction" ) );

        // No objects in this transaction -- this is a read only transaction
        if ( _objects.size() == 0 ) {
            _status = Status.STATUS_PREPARED;
            return false;
        }

        try {
            _status = Status.STATUS_PREPARING;
            count = 0;
            while ( count < _objects.size() ) {
                count = 0;
                enum = _objects.elements();
                while ( enum.hasMoreElements() ) {
                    entry = (ObjectEntry) enum.nextElement();
                    // If the object has been deleted, it is removed from the
                    // underlying database. This call will detect duplicate
                    // removal attempts. Otherwise the object is stored in
                    // the database.
                    if ( entry.prepared )
                        ++count;
                    else if ( entry.deleted ) {
                        ClassHandler handler;

                        handler = entry.engine.getClassHandler( entry.object.getClass() );
                        entry.engine.delete( this, entry.object.getClass(), entry.oid.getIdentity() );
                        entry.prepared = true;
                    } else {
                        Object       identity;
                        ClassHandler handler;
                        OID          oid;
                        
                        // When storing the object it's OID might change
                        // if the primary identity has been changed
                        handler = entry.engine.getClassHandler( entry.object.getClass() );
                        identity = handler.getIdentity( entry.object );
                        if ( identity == null )
                            throw new TransactionAbortedExceptionImpl( "persist.noIdentity", handler.getJavaClass(), null );
                        if ( handler.getCallback() != null )
                            handler.getCallback().storing( entry.object );
                        oid = entry.engine.store( this, entry.object, identity, _lockTimeout );
                        if ( oid != null ) {
                            entry.oid = oid;
                            entry.modified = true;
                        }
                        entry.prepared = true;
                    }
                }
            }
            _status = Status.STATUS_PREPARED;
            return true;
        } catch ( Exception except ) {
            _status = Status.STATUS_MARKED_ROLLBACK;
            enum = _objects.elements();
            while ( enum.hasMoreElements() ) {
                entry = (ObjectEntry) enum.nextElement();
                entry.prepared = false;
            }
            if ( except instanceof TransactionAbortedException )
                throw (TransactionAbortedException) except;
            // Any error is reported as transaction aborted
            throw new TransactionAbortedExceptionImpl( except );
        }
    }


    /**
     * Commits all changes and closes the transaction releasing all
     * locks on all objects. All objects are now transient. Must be
     * called after a call to {@link #prepare} has returned successfully.
     *
     * @throws TransactionAbortedException The transaction has been
     *   aborted due to inconsistency, duplicate object identity, error
     *   with the persistence engine or any other reason
     * @throws IllegalStateException This method has been called
     *   without calling {@link #prepare} first
     */
    public synchronized void commit()
        throws TransactionAbortedException
    {
        Enumeration enum;
        ObjectEntry entry;

        // Never commit transaction that has been marked for rollback
        if ( _status == Status.STATUS_MARKED_ROLLBACK )
            throw new TransactionAbortedExceptionImpl( "persist.markedRollback" );
        if ( _status != Status.STATUS_PREPARED )
            throw new IllegalStateException( Messages.message( "persist.missingPrepare" ) );

        try {
            _status = Status.STATUS_COMMITTING;

            // Go through all the connections opened in this transaction,
            // commit and close them one by one.
            commitConnections();

        } catch ( Exception except ) {
            // Any error that happens, we're going to rollback the transaction.
            _status = Status.STATUS_MARKED_ROLLBACK;
            throw new TransactionAbortedExceptionImpl( except );
        }

        // Assuming all went well in the connection department,
        // no deadlocks, etc. clean all the transaction locks with
        // regards to the persistence engine.
        enum = _objects.elements();
        while ( enum.hasMoreElements() ) {
            entry = (ObjectEntry) enum.nextElement();
            if ( entry.deleted ) {
                ClassHandler handler;

                handler = entry.engine.getClassHandler( entry.object.getClass() );

                // Object has been deleted inside transaction,
                // engine must forget about it.
                entry.engine.forgetObject( this, entry.oid );
                if ( handler.getCallback() != null ) {
                    try {
                        handler.getCallback().clearing( entry.object );
                    } catch ( Throwable thrw ) { }
                }
                entry.engine.getClassHandler( entry.object.getClass() ).setFieldsNull( entry.object );
            } else {
                // Object has been created/accessed inside the
                // transaction, release its lock.
                if ( entry.modified )
                    entry.engine.updateObject( this, entry.oid, entry.object );
                entry.engine.releaseLock( this, entry.oid );
            }
        }
        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _objects.clear();
        _engineOids.clear();
        _status = Status.STATUS_COMMITTED;
    }


    /*
     * Rolls back all changes and closes the transaction releasing all
     * locks on all objects. All objects are now transient, if they were
     * queried in this transaction. This method may be called at any point
     * during the transaction.
     *
     * @throws  IllegalStateException Method called if transaction is
     *  not in the proper state to perform this operation
     */
    public synchronized void rollback()
    {
        Enumeration enum;
        ObjectEntry entry;

        if ( _status != Status.STATUS_ACTIVE && _status != Status.STATUS_PREPARED &&
             _status != Status.STATUS_MARKED_ROLLBACK )
            throw new IllegalStateException( Messages.message( "persist.noTransaction" ) );

        // Go through all the connections opened in this transaction,
        // rollback and close them one by one.
        rollbackConnections();

        // Clean the transaction locks with regards to the
        // database engine
        enum = _objects.elements();
        while ( enum.hasMoreElements() ) {
            entry = (ObjectEntry) enum.nextElement();
            try {
                if ( entry.created ) {
                    // Object has been created in this transaction,
                    // it no longer exists, forget about it in the engine.
                    entry.engine.copyObject( this, entry.oid, entry.object );
                    entry.engine.forgetObject( this, entry.oid );
                } else {
                    // Object has been queried (possibly) deleted in this
                    // transaction and release the lock.
                    if ( entry.modified )
                        entry.engine.copyObject( this, entry.oid, entry.object );
                    entry.engine.releaseLock( this, entry.oid );
                }
            } catch ( Exception except ) { }
        }
        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _objects.clear();
        _engineOids.clear();
        _status = Status.STATUS_ROLLEDBACK;
    }


    /**
     * Returns true if the object is persistent in this transaction.
     *
     * @param object The object
     * @return True if persistent in transaction
     */
    public boolean isPersistent( Object object )
    {
        return ( getObjectEntry( object ) != null );
    }


    /**
     * Returns the status of this transaction.
     */
    public int getStatus()
    {
        return _status;
    }


    /**
     * Returns true if the transaction is open.
     */
    public boolean isOpen()
    {
        return ( _status == Status.STATUS_ACTIVE || _status == Status.STATUS_MARKED_ROLLBACK );
    }


    protected Xid getXid()
    {
        return _xid;
    }


    /**
     * Indicates which lock this transaction is waiting for. When a
     * transaction attempts to acquire a lock it must indicate which
     * lock it attempts to acquire in order to perform dead-lock
     * detection. This method is called by {@link ObjectLock}
     * before entering the temporary lock-acquire state.
     *
     * @param lock The lock which this transaction attempts to acquire
     */
    void setWaitOnLock( ObjectLock lock )
    {
        _waitOnLock = lock;
    }


    /**
     * Returns the lock which this transaction attempts to acquire.
     *
     * @return The lock which this transaction attempts to acquire
     */
    ObjectLock getWaitOnLock()
    {
        return _waitOnLock;
    }


    /**
     * Marks an object for deletion. Used during the preparation stage to
     * delete an attached relation by marking the object for deletion
     * (if not already deleted) and preparing it a second time.
     */
    void markDelete( PersistenceEngine engine, Class type, Object identity )
        throws LockNotGrantedException, PersistenceException
    {
        ObjectEntry entry;

        entry = getObjectEntry( engine, new OID( engine.getClassHandler( type ), identity ) );
        if ( entry != null && ! entry.deleted ) {
            entry.deleted = true;
            entry.prepared = false;
            try {
                entry.engine.softLock( this, entry.oid, _lockTimeout );
            } catch ( ObjectDeletedException except ) {
                // Object has been deleted outside this transaction,
                // forget about it
                removeObjectEntry( entry.object );
            }
        }
    }


    /**
     * Adds a new entry recording the use of the object in this
     * transaction. This is a copy of the object that is only visible
     * (or deleted) in the context of this transaction. The object is
     * not persisted if it has not been recorded in this transaction.
     *
     * @param object The object to record
     * @param oid The object's OID
     * @param engine The persistence engine used to create this object
     */
    ObjectEntry addObjectEntry( Object object, OID oid, PersistenceEngine engine )
    {
        ObjectEntry entry;
        Hashtable   engineOids;

        entry = new ObjectEntry( engine, object );
        entry.oid = oid;
        _objects.put( object, entry );
        engineOids = (Hashtable) _engineOids.get( engine );
        if ( engineOids == null ) {
            engineOids = new Hashtable();
            _engineOids.put( engine, engineOids );
        }
        engineOids.put( oid, entry );
        return entry;
    }


    /**
     * Retrieves the object entry for the specified object.
     *
     * @param engine The persistence engine used to create this object
     * @param oid The object's OID
     * @return The object entry
     */
    ObjectEntry getObjectEntry( PersistenceEngine engine, OID oid )
    {
        Hashtable engineOids;

        engineOids = (Hashtable) _engineOids.get( engine );
        if ( engineOids == null )
            return null;
       return (ObjectEntry) engineOids.get( oid );
    }


    /**
     * Returns the entry for the object from the object. If the entry
     * does not exist, the object is not persistent in this
     * transaction. An entry will be returned even if the object has
     * been deleted in this transaction.
     *
     * @param object The object to locate
     * @return The object's entry or null if not persistent
     */
    ObjectEntry getObjectEntry( Object object )
    {
        return (ObjectEntry) _objects.get( object );
    }


    /**
     * Removes the entry for an object and returns it. The object is
     * no longer part of the transaction.
     *
     * @param object The object to remove
     * @return The removed entry
     */
    ObjectEntry removeObjectEntry( Object object )
    {
        ObjectEntry entry;

        entry = (ObjectEntry) _objects.remove( object );
        if ( entry == null )
            return null;
        ( (Hashtable) _engineOids.get( entry.engine ) ).remove( entry.oid );
        return entry;
    }


    /**
     * A transaction records all objects accessed during the lifetime
     * of the transaction in this record (queries and created). A
     * single entry exist for each object accessible using the object
     * or it's OID as identities. The entry records the database engine used
     * to persist the object, the object's OID, the object itself, and
     * whether the object has been deleted in this transaction,
     * created in this transaction, or already prepared. Objects
     * identified as read only are not update when the transaction
     * commits.
     */
    static final class ObjectEntry
    {

        /**
         * The engine with which the object was loaded/created.
         */
        final PersistenceEngine  engine;

        /**
         * The object.
         */
        final Object             object;

        /**
         * The OID of the object.
         */
        OID                      oid;

        /**
         * True if the object has been marked for deletion.
         */
        boolean                  deleted;

        /**
         * True if the object has been created in this transaction.
         */
        boolean                  created;

        /**
         * True if the object has been prepared and may be committed.
         */
        boolean                  prepared;

        /**
         * True if the object has been modified in the transaction,
         * stored during the preparation stage and is write locked.
         */
        boolean                  modified;

        ObjectEntry( PersistenceEngine  engine, Object object )
        {
            this.engine = engine;
            this.object = object;
        }

    }


}

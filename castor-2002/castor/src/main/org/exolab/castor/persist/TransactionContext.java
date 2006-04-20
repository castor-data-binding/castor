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


package org.exolab.castor.persist;


import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import javax.transaction.Status;
import javax.transaction.xa.Xid;
import javax.transaction.xa.XAResource;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
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
import org.exolab.castor.mapping.ClassDescriptor;
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
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
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
     * Actually the vector contains instances of {@link ObjectEntry}.
     * @see #addObjectEntry
     */
    private final Vector  _objects = new Vector();


    /**
     * A list of all the engines associated with this transaction and
     * all the OIDs loaded from them. The persistence engine is used
     * as the key, the value is a hashtable. In that hashtable {@link OID}
     * is the key and {@link ObjectEntry} is the value.
     */
    private final Hashtable   _engineOids = new Hashtable();


    /**
     * Collection of objects loaded in read-only mode during this transaction.
     * They are not persistent anymore, but we have to keep them in order
     * to provide uniqueness of objects. E.g., if one depenent object
     * contains reference to another one, the latter shouldn't be loaded twice.
     * In the hashtable {@link OID} is the key and {@link ObjectEntry}
     * is the value.
     * @see #addObjectEntry
     */
    private final Hashtable  _readOnlyObjects = new Hashtable();


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
     * FIFO linked list of objects deleted in this transaction.
     */
    private ObjectEntry  _deletedList;


    /**
     * The database to which this transaction belongs.
     */
    private Database     _db;


    /**
     * Create a new transaction context. This method is used by the
     * explicit transaction model.
     */
    public TransactionContext( Database db )
    {
        _xid = null;
        _status = Status.STATUS_ACTIVE;
        _db = db;
    }


    /**
     * Create a new transaction context. This method is used by the
     * XA transaction model, see {@link XAResourceImpl}.
     */
    public TransactionContext( Database db, Xid xid )
    {
        _xid = xid;
        _status = Status.STATUS_ACTIVE;
        _db = db;
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
     * The derived class must implement this method and close all the
     * connections used in this transaction.
     * @throws TransactionAbortedException The transaction could not
     *  close all the connections
     */
    protected abstract void closeConnections()
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
        ObjectEntry entry = null;
        OID         oid;

        oid = new OID( handler, identity );
        if ( accessMode == AccessMode.ReadOnly ) 
            entry = getReadOnlyObjectEntry( oid );
        if ( entry == null ) 
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
            if ( ! handler.getJavaClass().isAssignableFrom( entry.object.getClass() ) ) {
                // if the object in the cache is not a subclass of the class that is needed,
                // then it may be a subclass.
                // In this case we forget this object, but don't throw the exception,
                // the object of the needed class will be reloaded.
                if ( ! entry.object.getClass().isAssignableFrom( handler.getJavaClass() ) )
                    throw new PersistenceExceptionImpl( "persist.typeMismatch", handler.getJavaClass(), entry.object.getClass() );
                else {
                    release( entry.object );
                    engine.forgetObject( this, oid );
                    return null;
                }
            }
            if ( entry.created )
                return entry.object;
            if ( ( accessMode == AccessMode.Exclusive && ! entry.engine.hasLock( this, oid, true ) )
                    || ( accessMode == AccessMode.DbLocked && ! entry.oid.isDbLock() ) ) {
                // If we are in db-lock mode and object has not been
                // loaded in db-lock mode before, then we have a
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
     * @param identity The object's identity
     * @param accessMode The access mode (see {@link AccessMode})
     *  the values in persistent storage
     * @return The loaded object (single instance per transaction)
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public synchronized Object load( PersistenceEngine engine, ClassHandler handler,
                                     Object identity, AccessMode accessMode )
        throws ObjectNotFoundException, LockNotGrantedException, PersistenceException
    {
        Object      object;
        ObjectEntry entry;
        OID         oid;

        object = fetch( engine, handler, identity, accessMode );
        if ( object != null )
            return object;
        oid = new OID( handler, identity );

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
        // The returned actual cache oid may refer to an extending class.
        // To avoid errors during copying from the cache in engine.copyObject()
        // we must create an instance of the extending class.
        handler = engine.getClassHandler( oid.getJavaClass() );
        object = handler.newInstance();

        // Need to copy the contents of this object from the cached
        // copy and deal with it based on the transaction semantics.
        // If the mode is read-only we release the lock and forget about
        // it in the contents of this transaction. Otherwise we record
        // the object in this transaction.
        entry = addObjectEntry( object, oid, engine );
        try {
            engine.copyObject( this, oid, object, accessMode );
            if ( handler.getCallback() != null ) {
                Class reloadClass;

                handler.getCallback().using( object, _db );
                reloadClass = handler.getCallback().loaded( object, toDatabaseAccessMode( accessMode ) );
                if ( reloadClass != null && object.getClass() != reloadClass ) {
                    release( object );
                    engine.forgetObject( this, oid );
                    handler = engine.getClassHandler( reloadClass );
                    if ( handler == null )
                        throw new ClassNotPersistenceCapableExceptionImpl( reloadClass );
                    return load( engine, handler, identity, accessMode );
                }
            }
        } catch ( Exception except ) {
            release( object );
            engine.forgetObject( this, oid );
            if ( except instanceof PersistenceException )
                throw (PersistenceException) except;
            throw new PersistenceExceptionImpl( except );
        }
        // [oleg] complicated scenarios of object reloading may
        // replace the instance of dependent object.
        // Looks bad, but works. Need to do this better in castorone...
        entry = getObjectEntry( engine, oid );
        if ( entry != null ) 
            object = entry.object;

        if ( accessMode == AccessMode.ReadOnly ) {
            makeReadOnly( object );
        }
        return object;
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
     * @param db The database loading this object
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
        return new QueryResults( this, engine, query, accessMode, _db );
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
    public synchronized OID create( PersistenceEngine engine, Object object,
                                    Object identity )
        throws DuplicateIdentityException, ClassNotPersistenceCapableException, PersistenceException
    {
        OID          oid;
        ObjectEntry  entry;
        ClassHandler handler;
        ClassDescriptor clsDesc;

        // Make sure the object has not beed persisted in this transaction.
        entry = getObjectEntry( object );
        if ( entry != null && ! entry.deleted ) {
            throw new PersistenceExceptionImpl( "persist.objectAlreadyPersistent", object.getClass(), identity );
        }
        handler = engine.getClassHandler( object.getClass() );
        if ( handler == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", object.getClass().getName() ) );

        // [oleg] In the case where key generator is used the value of 
        // identity is dummy, set it to null
        clsDesc = handler.getDescriptor();
        if ( ( clsDesc instanceof JDOClassDescriptor ) &&
                ( ( (JDOClassDescriptor) clsDesc ).getKeyGeneratorDescriptor() != null ) )
            identity = null;

        // Create the object. This can only happen once for each object in
        // all transactions running on the same engine, so after creation
        // add a new entry for this object and use this object as the view
        oid = new OID( handler, identity );
        entry = getObjectEntry( engine, oid );
        if ( identity != null && entry != null ) {
            if ( ! entry.deleted || entry.object != object ) 
                throw new DuplicateIdentityException( Messages.format( "persist.duplicateIdentity", object.getClass().getName(), identity ) );
            else {
                // If the object was already deleted in this transaction, 
                // just undelete it.
                // Remove the entry from a FIFO linked list of deleted entries.
                if ( _deletedList != null ) {
                    ObjectEntry deleted;
                    
                    if ( _deletedList == entry ) 
                        _deletedList = entry.nextDeleted;
                    else {
                        deleted = _deletedList;
                        while ( deleted.nextDeleted != null && deleted.nextDeleted != entry ) 
                            deleted = deleted.nextDeleted;
                        if ( deleted.nextDeleted == entry ) 
                            deleted.nextDeleted = entry.nextDeleted;
                        else 
                            throw new PersistenceExceptionImpl( "persist.deletedNotFound", identity );
                    }
                }
            }
        }

        try {
            if ( handler.getCallback() != null ) {
                handler.getCallback().creating( object, _db );
            }
            // Must perform creation after object is recorded in transaction
            // to prevent circular references.
            if ( entry == null ) 
                entry = addObjectEntry( object, oid, engine );
            oid = engine.create( this, object, identity );
            removeObjectEntry( object );
            entry = addObjectEntry( object, oid, engine );
            entry.created = true;
            if ( handler.getCallback() != null ) {
                handler.getCallback().using( object, _db );
                handler.getCallback().created( object );
            }
            return oid;
        } catch ( Exception except ) {
            release( object );
            if ( except instanceof DuplicateIdentityException )
                throw (DuplicateIdentityException) except;
            if ( except instanceof PersistenceException )
                throw (PersistenceException) except;
            throw new PersistenceExceptionImpl( except );
        }
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
     * @throws ObjectModifiedException Dirty checking mechanism may immediately
     *  report that the object was modified in the database during the long 
     *  transaction.
     */
    public synchronized OID update( PersistenceEngine engine, Object object,
                                    Object identity )
        throws DuplicateIdentityException, ObjectModifiedException,
               ClassNotPersistenceCapableException, PersistenceException
    {
        OID          oid;
        ObjectEntry  entry;
        ClassHandler handler;
        AccessMode   accessMode = null;

        handler = engine.getClassHandler( object.getClass() );
        if ( handler == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", object.getClass().getName() ) );
        if ( identity == null )
            throw new PersistenceExceptionImpl( "persist.noIdentity" );

        // Make sure that nobody is looking at the object
        oid = new OID( handler, identity );
        entry = getObjectEntry( engine, oid );
        if ( entry != null ) {
            if ( entry.deleted )
                throw new ObjectDeletedExceptionImpl( object.getClass(), identity );
            // to prevent circular references
            if ( entry.object == object ) 
                return oid;
            //[Oleg] in some cases (deletion of dependent objects) objects
            //that were previously loaded in this transaction by the same 
            //update() call must be replaced. Thus, we must allow this.
            //I don't see other way.
            release( entry.object );
            //throw new PersistenceExceptionImpl( "persist.objectAlreadyPersistent", object.getClass(), identity );
        }

        // to prevent circular references
        addObjectEntry( object, oid, engine );
        // Check the object's timestamp
        accessMode = handler.getAccessMode( accessMode );
        try {
            oid = engine.update( this, handler.getJavaClass(), object, accessMode, _lockTimeout );
        } catch ( ObjectNotFoundException except ) {
            throw except;
        } catch ( LockNotGrantedException except ) {
            throw except;
        } catch ( ClassNotPersistenceCapableException except ) {
            throw new PersistenceExceptionImpl( except );
        }

        // If the object isn't found in the cache, then attempt to create it.
        if ( oid == null ) {
            removeObjectEntry( object );
            return create( engine, object, identity );
        }

        try {
            if ( handler.getCallback() != null ) {
                handler.getCallback().using( object, _db );
                handler.getCallback().updated( object );
            }
        } catch ( Exception except ) {
            release( object );
            if ( except instanceof PersistenceException )
                throw (PersistenceException) except;
            throw new PersistenceExceptionImpl( except );
        }
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

        try {
            ClassHandler handler;
                
            handler = entry.engine.getClassHandler( entry.object.getClass() );
            if ( handler != null && handler.getCallback() != null )
                handler.getCallback().removing( entry.object );
        } catch ( Exception except ) {
            throw new PersistenceExceptionImpl( except );
        }

        // Must acquire a write lock on the object in order to delete it,
        // prevents object form being deleted while someone else is
        // looking at it.
        try {
            entry.deleted = true;
            entry.engine.softLock( this, entry.oid, _lockTimeout );
            // Mark object as deleted. This will prevent it from being viewed
            // in this transaction and will handle it properly at commit time.
            // The write lock will prevent it from being viewed in another
            // transaction.
            entry.engine.markDelete( this, entry.oid, _lockTimeout );

            // Add the entry to a FIFO linked list of deleted entries.
            if ( _deletedList == null )
                _deletedList = entry;
            else {
                ObjectEntry deleted;

                deleted = _deletedList;
                while ( deleted.nextDeleted != null )
                    deleted = deleted.nextDeleted;
                deleted.nextDeleted = entry;
            }
        } catch ( ObjectDeletedException except ) {
            // Object has been deleted outside this transaction,
            // forget about it
            removeObjectEntryWithDependent( object );
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
            removeObjectEntryWithDependent( object );
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
        } catch ( ObjectDeletedWaitingForLockException except ) {
            // Object has been deleted outside this transaction,
            // forget about it
            removeObjectEntryWithDependent( object );
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
        ObjectEntry  entry;
        ClassHandler handler;

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        entry = getObjectEntry( object );
        if ( entry == null || entry.deleted )
            return;
        // [oleg] Don't be so severe
        //    throw new ObjectNotPersistentExceptionImpl( object.getClass() );

        // Release the lock, forget about the object in this transaction
        entry.engine.releaseLockWithDependent( this, entry.oid );
        removeObjectEntryWithDependent( object );
        handler = entry.engine.getClassHandler( object.getClass() );
        if ( handler.getCallback() != null )
            handler.getCallback().releasing( object, false );
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
        Vector todo;
        Vector done;
        Enumeration enum;
        ObjectEntry entry;

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
            done = new Vector();
            while ( _objects.size() != done.size() ) {
                todo = new Vector();
                enum = _objects.elements();
                while ( enum.hasMoreElements() ) {
                    entry = (ObjectEntry) enum.nextElement();
                    if ( ! done.contains( entry ) ) {
                        todo.addElement( entry );
                    }
                }
                enum = todo.elements();
                while ( enum.hasMoreElements() ) {
                    entry = (ObjectEntry) enum.nextElement();
                    if ( ! entry.deleted ) {
                        Object       identity;
                        ClassHandler handler;
                        OID          oid;
                    
                        // When storing the object it's OID might change
                        // if the primary identity has been changed
                        handler = entry.engine.getClassHandler( entry.object.getClass() );
                        identity = handler.getIdentity( entry.object );
                        oid = entry.engine.store( this, entry.object, identity, _lockTimeout );
                        if ( oid != null ) {
                            entry.oid = oid;
                            entry.modified = true;
                        }
                    }
                    done.addElement( entry );
                }
            }

            _status = Status.STATUS_PREPARING;

            // Process all deleted objects last in FIFO order.
            while ( _deletedList != null ) {
                ClassHandler handler;

                entry = _deletedList;
                _deletedList = _deletedList.nextDeleted;
                handler = entry.engine.getClassHandler( entry.object.getClass() );
                entry.engine.delete( this, entry.object.getClass(), entry.oid.getIdentity() );
            }

            _status = Status.STATUS_PREPARED;
            return true;
        } catch ( Exception except ) {
            _status = Status.STATUS_MARKED_ROLLBACK;
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
            ClassHandler handler;

            entry = (ObjectEntry) enum.nextElement();
            handler = entry.engine.getClassHandler( entry.object.getClass() );
            if ( entry.deleted ) {

                // Object has been deleted inside transaction,
                // engine must forget about it.
                entry.engine.forgetObject( this, entry.oid );
                entry.engine.getClassHandler( entry.object.getClass() ).setFieldsNull( entry.object );
            } else {
                // Object has been created/accessed inside the
                // transaction, release its lock.
                if ( entry.modified )
                    entry.engine.updateObject( this, entry.oid, entry.object );
                entry.engine.releaseLock( this, entry.oid );
            }
            if ( handler.getCallback() != null )
                handler.getCallback().releasing( entry.object, true );
        }
        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _objects.removeAllElements();
        _engineOids.clear();
        _readOnlyObjects.clear();
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
            ClassHandler handler;
                
            entry = (ObjectEntry) enum.nextElement();
            handler = entry.engine.getClassHandler( entry.object.getClass() );
            try {
                if ( entry.created ) {
                    // Object has been created in this transaction,
                    // it no longer exists, forget about it in the engine.
                    entry.engine.revertObject( this, entry.oid, entry.object );
                    entry.engine.forgetObject( this, entry.oid );
                } else {
                    // Object has been queried (possibly) deleted in this
                    // transaction and release the lock.
                    if ( entry.modified )
                        entry.engine.revertObject( this, entry.oid, entry.object );
                    entry.engine.releaseLock( this, entry.oid );
                }
                if ( handler.getCallback() != null )
                    handler.getCallback().releasing( entry.object, false );
            } catch ( Exception except ) { }
        }

        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _objects.removeAllElements();
        _engineOids.clear();
        _readOnlyObjects.clear();
        while ( _deletedList != null ) {
            entry = _deletedList;
            _deletedList = entry.nextDeleted;
            entry.nextDeleted = null;
        }
        _status = Status.STATUS_ROLLEDBACK;
    }


    /**
     * Closes all Connections when working in EJB environment,
     * otherwise does nothing.
     * Must be called before the end of the transaction.
     *
     * @throws TransactionAbortedException The transaction has been
     *   aborted due to inconsistency, duplicate object identity, error
     *   with the persistence engine or any other reason
     * @throws IllegalStateException This method has been called
     *   after the end of the transaction.
     */
    public synchronized void close()
        throws TransactionAbortedException
    {
        Enumeration enum;
        ObjectEntry entry;

        if ( _status != Status.STATUS_ACTIVE &&
             _status != Status.STATUS_MARKED_ROLLBACK ) {
            throw new IllegalStateException( Messages.message( "persist.missingEnd" ) );
        }
        try {
            // Go through all the connections opened in this transaction,
            // close them one by one.
            closeConnections();

        } catch ( Exception except ) {
            // Any error that happens, we're going to rollback the transaction.
            _status = Status.STATUS_MARKED_ROLLBACK;
            throw new TransactionAbortedExceptionImpl( except );
        }
    }


    /**
     * Returns true if the object is persistent in this transaction.
     *
     * @param object The object
     * @return True if persistent in transaction
     */
    public boolean isPersistent( Object object )
    {
        ObjectEntry entry;

        entry = getObjectEntry( object );
        return ( ( entry != null ) && ( ! entry.deleted ) );
    }


    /**
     * Returns the object's identity. If the identity was determined when
     * the object was created, or if the object was retrieved, that identity
     * is returned. If the identity has been modified, this will not be
     * reflected until the transaction commits. Null is returned if the
     * identity is null, the object does not have any identity, or the
     * object is not persistent.
     *
     * @param object The object
     * @return The object's identity, or null
     */
    public Object getIdentity( Object object )
    {
        ObjectEntry entry;

        entry = getObjectEntry( object );
        if ( entry != null )
            return entry.oid.getIdentity();
        return null;
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
            ClassHandler handler;
                
            handler = entry.engine.getClassHandler( entry.object.getClass() );
            try {
                if ( handler != null && handler.getCallback() != null )
                    handler.getCallback().removing( entry.object );
            } catch ( Exception except ) {
                throw new PersistenceExceptionImpl( except );
            }
            try {
                entry.deleted = true;
                entry.engine.softLock( this, entry.oid, _lockTimeout );
                entry.engine.markDelete( this, entry.oid, _lockTimeout );

                // Add the entry to a FIFO linked list of deleted entries.
                if ( _deletedList == null )
                   _deletedList = entry;
                else {
                    ObjectEntry deleted;
                    
                    deleted = _deletedList;
                    while ( deleted.nextDeleted != null )
                        deleted = deleted.nextDeleted;
                    deleted.nextDeleted = entry;
                }
            } catch ( ObjectDeletedException except ) {
                // Object has been deleted outside this transaction,
                // forget about it
                removeObjectEntryWithDependent( entry.object );
            }
            try {
                if ( handler != null && handler.getCallback() != null )
                    handler.getCallback().removed( entry.object );
            } catch ( Exception except ) {
                throw new PersistenceExceptionImpl( except );
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

        entry = new ObjectEntry( (CacheEngine) engine, object );
        entry.oid = oid;
        _objects.addElement( entry );
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
        ObjectEntry entry;

        for ( Enumeration enum = _objects.elements(); enum.hasMoreElements(); ) {
            entry = (ObjectEntry) enum.nextElement();
            if ( entry.object == object )
                return entry;
        }
        return null;
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
        int size;
        ObjectEntry entry;

        size = _objects.size();
        for ( int i = 0; i < size; i++ ) {
            entry = (ObjectEntry) _objects.elementAt( i );
            if ( entry.object == object ) {
                _objects.removeElementAt( i );
                ( (Hashtable) _engineOids.get( entry.engine ) ).remove( entry.oid );
                return entry;
            }
        }
        return null;
    }


    /**
     * Removes the entry for an object and returns it.
     * Recursively does the same for all dependent objects.
     *
     * @param object The object to remove
     * @return The removed entry
     */
    ObjectEntry removeObjectEntryWithDependent( Object object )
    {
        int size;
        ObjectEntry entry;
        RelationHandler[] relations;
        Object related;
        Enumeration enum;

        size = _objects.size();
        for ( int i = 0; i < size; i++ ) {
            entry = (ObjectEntry) _objects.elementAt( i );
            if ( entry.object == object ) {
                _objects.removeElementAt( i );
                ( (Hashtable) _engineOids.get( entry.engine ) ).remove( entry.oid );

                // We have added entries for all dependent objects, now let's remove them
                relations = entry.engine.getClassHandler(object.getClass()).getRelations();
                for ( int j = 0 ; j < relations.length ; ++j ) {
                    if ( relations[ j ] != null && relations[ j ].isAttached() &&
                            relations[ j ].isMulti() ) {
                        enum = (Enumeration) relations[ j ].getRelated( object );
                        if ( enum != null ) {
                            while ( enum.hasMoreElements() ) {
                                related = enum.nextElement();
                                if ( related != null )
                                    removeObjectEntryWithDependent( related );
                            }
                        }
                    }
                }
                return entry;
            }
        }
        return null;
    }


    /**
     * Makes the object read-only: move it to the hashtable of readonly objects
     * The object must be already in the transaction.
     * Readonly objects should be unique in bounds of the transaction, 
     * otherwise they may be loaded twice (e.g.: one dependent object
     * contains reference to another).
     */
    void makeReadOnly( Object object )
    {
        ObjectEntry entry;

        entry = removeObjectEntry( object );
        if ( entry != null ) {
            _readOnlyObjects.put( entry.oid, entry );
            entry.engine.releaseLock( this, entry.oid );
        }
    }


    ObjectEntry getReadOnlyObjectEntry( OID oid )
    {
        return (ObjectEntry) _readOnlyObjects.get( oid );
    }


    /**
     * Converts AccessMode constant to Database short constant
     */ 
    static short toDatabaseAccessMode( AccessMode mode )
    {
        if ( mode == AccessMode.Shared )
            return Database.Shared;
        if ( mode == AccessMode.ReadOnly )
            return Database.ReadOnly;
        if ( mode == AccessMode.DbLocked )
            return Database.DbLocked;
        if ( mode == AccessMode.Exclusive )
            return Database.Exclusive;
        // never happens
        return -1;
    }


    /**
     * A transaction records all objects accessed during the lifetime
     * of the transaction in this record (queries and created). A
     * single entry exist for each object accessible using the object
     * or it's OID as identities. The entry records the database engine used
     * to persist the object, the object's OID, the object itself, and
     * whether the object has been deleted in this transaction,
     * created in this transaction, or modified. Objects identified as
     * read only are not update when the transaction commits.
     */
    static final class ObjectEntry
    {

        /**
         * The engine with which the object was loaded/created.
         */
        final CacheEngine        engine;

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
         * True if the object has been modified in the transaction,
         * stored during the preparation stage and is write locked.
         */
        boolean                  modified;

        /**
         * Link to the next deleted object in a FIFO list of deleted
         * objects.
         */
        ObjectEntry              nextDeleted;

        ObjectEntry( CacheEngine engine, Object object )
        {
            this.engine = engine;
            this.object = object;
        }

    }


}

/*
 * Copyright 2005 Ralf Joachim, Gregory Bock, Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.persist;

import java.sql.Connection;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import javax.transaction.Status;
import javax.transaction.xa.Xid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DbMetaInfo;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.ObjectNotPersistentException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.ObjectDeletedWaitingForLockException;
import org.exolab.castor.persist.ObjectLock;
import org.exolab.castor.persist.PersistenceInfoGroup;
import org.exolab.castor.persist.QueryResults;
import org.exolab.castor.persist.TxSynchronizable;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.util.Messages;

/**
 * A transaction context is required in order to perform operations against the
 * database. The transaction context is mapped to an API transaction or an XA
 * transaction. The only way to begin a new transaction is through the creation
 * of a new transaction context. A transaction context is created from an
 * implementation class directly or through {@link XAResourceImpl}.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin </a>
 * @author <a href="mailto: ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:gblock AT ctoforaday DOT COM">Gregory Block</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public abstract class TransactionContext {

    /**
     * IMPLEMENTATION NOTES:
     * 
     * An object is considered persistent only if it was queried or created
     * within the context of this transaction. An object is not persistent if it
     * was queried or created by another transactions. An object is not
     * persistent if it was queried with read-only access.
     * 
     * A read lock is implicitly obtained on any object that is queried, and a
     * write lock on any object that is queried in exclusive mode, created or
     * deleted in this transaction. The lock can be upgraded to a write lock.
     * 
     * The validity of locks is dependent on the underlying persistence engine
     * the transaction mode. Without persistence engine locks provide a strong
     * locking mechanism, preventing objects from being deleted or modified in
     * one transaction while another transaction is looking at them. With a
     * persistent engine in exclusive mode, locks exhibit the same behavior.
     * With a persistent engine in read/write mode or a persistent engine that
     * does not support locking (e.g. LDAP) an object may be deleted or modified
     * concurrently through a direct access mechanism.
     */

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging </a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            TransactionContext.class);

    /*
     * An object is consider as Transitent if it is not partcipate in the
     * current transaction.
     */
    private static final int OBJECT_STATE_TRANSIENT = 0;

    /*
     * This state is not yet used by castor
     */
    private static final int OBJECT_STATE_HOLLOW = 1;

    /*
     * An object is consider as READ_ONLY if and only if it is load thru this
     * transactionContext as read only object
     */
    private static final int OBJECT_STATE_READ_ONLY = 2;

    /*
     * An object is consider as Persistent if the object is loaded thru this
     * transactionContext
     */
    private static final int OBJECT_STATE_PERSISTENT = 3;

    /*
     * An object is consider as PERSISTENT_NEW if the object is created in this
     * transactionContext
     */
    private static final int OBJECT_STATE_PERSISTENT_NEW = 4;

    /*
     * An object is consider as PERSISTENT_DELETED if the object is loaded thru
     * this transactionContext and deleted
     */
    private static final int OBJECT_STATE_PERSISTENT_DELETED = 5;

    /*
     * An object is consider as PERSISTENT_NEW_DELETED if the object is created
     * in this transactionContext and deleted
     */
    private static final int OBJECT_STATE_PERSISTENT_NEW_DELETED = 6;

    /**
     * The ObjectTracker for this transaction, tracking all of the various bits
     * of state and co-located information (Engine, OID, etc.) for this entry.
     * See ObjectTracker javadocs for more details.
     */
    private final ObjectTracker _tracker = new ObjectTracker();

    /**
     * Set while transaction is waiting for a lock.
     * 
     * @see #getWaitOnLock
     * @see ObjectLock
     */
    private ObjectLock _waitOnLock;

    /**
     * The transaction status. See {@link Status}for list of valid values.
     */
    private int _status;

    /**
     * The timeout waiting to acquire a new lock. Specified in seconds.
     */
    private int _lockTimeout = 30;

    /**
     * The Xid of this transaction is generated from an XA resource.
     */
    private final Xid _xid;

    /**
     * The timeout of this transaction. Specified in seconds.
     */
    private int _txTimeout = 30;

    /**
     * The database to which this transaction belongs.
     */
    private Database _db;

    /**
     * True if user prefer all reachable object to be stored automatically.
     * False if user want only dependent object to be stored.
     */
    private boolean _autoStore;

    /**
     * The default callback interceptor for the data object in this transaction.
     */
    private CallbackInterceptor _callback;

    /**
     * The instance factory to that creates new instances of data object
     */
    private InstanceFactory _instanceFactory;

    /**
     * A list of listeners which will be informed about various transaction
     * states.
     */
    private ArrayList _synchronizeList = new ArrayList();

    /**
     * Create a new transaction context. This method is used by the explicit
     * transaction model.
     */
    public TransactionContext(final Database db) {
        _xid = null;
        _status = -1;
        _db = db;
    }

    /*
     * These constructors should be removed. It is no longer used from within
     * the Castor code. public TransactionContext( Database db, Xid xid ) { _xid =
     * xid; _status = -1; _db = db; }
     *  /* These constructors should be removed. It is no longer used from
     * within the Castor code.
     * 
     * public TransactionContext( Database db, Xid xid , int status) { _xid =
     * xid; _status = status; _db = db; }
     */

    /*
     * This constructor is used in the case of global transaction.
     */
    public TransactionContext(final Database db,
            final javax.transaction.Transaction transaction)
            throws javax.transaction.SystemException {
        _xid = null;
        _db = db;
        // this should be removed when we move
        // to separate implementations of TransactionContext
        // for explicit demarcation and global transactions.
        // In the latter case, I think we should just wrap
        // the global transaction and pass getStatus() through to the
        // wrapped transaction
        _status = transaction.getStatus();
    }

    /**
     * Register a listener which wants to synchronize its state to the state of
     * the transaction.
     */
    public void addTxSynchronizable(final TxSynchronizable synchronizable) {
        _synchronizeList.add(synchronizable);
    }

    /**
     * @see #addTxSynchronizable
     */
    public void removeTxSynchronizable(final TxSynchronizable synchronizable) {
        _synchronizeList.remove(synchronizable);
    }

    /**
     * Inform all registered listeners that the transaction was committed.
     */
    protected void txcommitted() {
        for (int i = 0; i < _synchronizeList.size(); i++) {
            TxSynchronizable sync = (TxSynchronizable) _synchronizeList.get(i);
            try {
                sync.committed(this);
            } catch (Exception e) {
                // nothing to do
            }
        }
    }

    /**
     * Inform all registered listeners that the transaction was rolled back.
     */
    protected void txrolledback() {
        for (int i = 0; i < _synchronizeList.size(); i++) {
            TxSynchronizable sync = (TxSynchronizable) _synchronizeList.get(i);
            try {
                sync.rolledback(this);
            } catch (Exception e) {
                // nothing to do 
            }
        }
    }

    /**
     * Enable or disable autoStore. If enabled, all new objects, which is
     * reachable from other object that is quried, loaded, created in the
     * transaction, will be created when the transaction is committed.
     */
    public void setAutoStore(final boolean autoStore) {
        _autoStore = autoStore;
    }

    /**
     * Test if autoStore options is enabled or not.
     */
    public boolean isAutoStore() {
        return _autoStore;
    }

    /**
     * Overrides the default callback interceptor by a custom interceptor for
     * this database source.
     * <p>
     * The interceptor is a callback that notifies data objects on persistent
     * state events.
     * <p>
     * If callback interceptor is not overrided, events will be sent to data
     * object that implements the org.exolab.castor.jdo.Persistent interface.
     * 
     * @param callback
     *            The callback interceptor, null if disabled
     */
    public void setCallback(final CallbackInterceptor callback) {
        _callback = callback;
    }

    /**
     * Overrides the default instance factory by a custom one for this database
     * source.
     * <p>
     * The factory is used to obatain a new instance of data object when it is
     * needed during loading.
     * 
     * @param factory
     *            The instanceFactory to be used, null if disable
     */
    public void setInstanceFactory(final InstanceFactory factory) {
        _instanceFactory = factory;
    }

    public PersistenceInfoGroup getScope() {
        return _db.getScope();
    }

    /**
     * Sets the timeout of this transaction. The timeout is specified in
     * seconds.
     */
    public void setTransactionTimeout(final int timeout) {
        _txTimeout = timeout;
    }

    /**
     * Returns the timeout of this transaction. The timeout is specified in
     * seconds.
     */
    public int getTransactionTimeout() {
        return _txTimeout;
    }

    /**
     * Returns the timeout waiting to acquire a lock. The timeout is specified
     * in seconds.
     */
    public int getLockTimeout() {
        return _lockTimeout;
    }

    /**
     * Sets the timeout waiting to acquire a lock. The timeout is specified in
     * seconds.
     */
    public void setLockTimeout(final int timeout) {
        _lockTimeout = (timeout >= 0 ? timeout : 0);
    }

    /**
     * Sets the status of the current transaction to STATUS_ATIVE
     */
    public void setStatusActive() {
        _status = Status.STATUS_ACTIVE;
    }

    /**
     * The derived class must implement this method and return an open
     * connection for the specified engine. The connection should be created
     * only one for a given engine in the same transaction.
     * 
     * @param engine
     *            The persistence engine
     * @return An open connection
     * @throws PersistenceException
     *             An error occured talking to the persistence engine
     */
    public abstract Connection getConnection(LockEngine engine)
            throws PersistenceException;

    /**
     * Returns meta-data related to the RDBMS used.
     * 
     * @param engine
     *            LockEngine instance used.
     * @return A DbMetaInfo instance describing var. features of the underlying
     *         RDBMS.
     */
    public abstract DbMetaInfo getConnectionInfo(LockEngine engine)
            throws PersistenceException;

    /**
     * The derived class must implement this method and commit all the
     * connections used in this transaction. If the transaction could not commit
     * fully or partially, this method will throw an {@link
     * TransactionAbortedException}, causing a rollback to occur as the next
     * step.
     * 
     * @throws TransactionAbortedException
     *             The transaction could not commit fully or partially and
     *             should be rolled back
     */
    protected abstract void commitConnections()
            throws TransactionAbortedException;

    /**
     * The derived class must implement this method and close all the
     * connections used in this transaction.
     * 
     * @throws TransactionAbortedException
     *             The transaction could not close all the connections
     */
    protected abstract void closeConnections()
            throws TransactionAbortedException;

    /**
     * The derived class must implement this method and rollback all the
     * connections used in this transaction. The connections may be closed, as
     * they will not be reused in this transaction. This operation is guaranteed
     * to succeed.
     */
    protected abstract void rollbackConnections();

    public synchronized Object fetch(final LockEngine engine, 
            final ClassMolder molder,
            final Object identity, 
            final AccessMode suggestedAccessMode)
    throws ObjectNotFoundException, LockNotGrantedException,
            PersistenceException {

        Object objectInTransaction;
        OID oid;
        AccessMode accessMode;

        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        oid = new OID(engine, molder, identity);
        accessMode = molder.getAccessMode(suggestedAccessMode);
        if (accessMode == AccessMode.ReadOnly) {
            objectInTransaction = _tracker.getObjectForOID(engine, oid, true);
        } else {
            objectInTransaction = _tracker.getObjectForOID(engine, oid, false);
        }

        if (objectInTransaction != null) {
            // Object exists in this transaction.

            // If the object has been loaded in this transaction from a
            // different engine this is an error. If the object has been
            // deleted in this transaction, it cannot be re-loaded. If the
            // object has been created in this transaction, it cannot be
            // re-loaded but no error is reported.
            if (engine != _tracker.getLockEngineForObject(objectInTransaction)) {
                throw new PersistenceException(Messages.format(
                        "persist.multipleLoad", molder.getName(), identity));
            }

            // Objects marked deleted in the transaction return null.
            if (_tracker.isDeleted(objectInTransaction)) {
                return null;
            }

            // ssa, multi classloader feature (note - this code appears to be
            // duplicated, yet different, in both cases. Why?)
            // ssa, FIXME : Are the two following statements equivalent ? (bug
            // 998)
            // if ( ! molder.getJavaClass().isAssignableFrom(
            // entry.object.getClass() ) )
            // if ( ! molder.getJavaClass( _db.getClassLoader()
            // ).isAssignableFrom( entry.object.getClass() ) )
            if (!molder.isAssignableFrom(objectInTransaction.getClass())) {
                throw new PersistenceException(Messages.format(
                        "persist.typeMismatch", molder.getName(), identity));
            }

            // If the object has been created in this transaction, don't bother
            // testing access mode.
            if (_tracker.isCreated(objectInTransaction)) {
                return objectInTransaction;
            }

            if ((accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked)
                    && !_tracker.getOIDForObject(objectInTransaction)
                            .isDbLock()) {
                // If we are in exclusive mode and object has not been
                // loaded in exclusive mode before, then we have a
                // problem. We cannot return an object that is not
                // synchronized with the database, but we cannot
                // synchronize a live object.
                throw new PersistenceException(Messages.format(
                        "persist.lockConflict", molder.getName(), identity));
            }
            return objectInTransaction;
        }
        return null;
    }

    /**
     * Load an object for use within the transaction. Multiple access to the
     * same object within the transaction will return the same object instance
     * (except for read-only access).
     * <p>
     * This method is similar to {@link #fetch}except that it will load the
     * object only once within a transaction and always return the same
     * instance.
     * <p>
     * If the object is loaded for read-only then no lock is acquired and
     * updates to the object are not reflected at commit time. If the object is
     * loaded for read-write then a read lock is acquired (unless timeout or
     * deadlock detected) and the object is stored at commit time. The object is
     * then considered persistent and may be deleted or upgraded to write lock.
     * If the object is loaded for exclusive access then a write lock is
     * acquired and the object is synchronized with the persistent copy.
     * <p>
     * Attempting to load the object twice in the same transaction, once with
     * exclusive lock and once with read-write lock will result in an exception.
     * 
     * @param engine
     *            The persistence engine
     * @param molder
     *            The class persistence molder
     * @param identity
     *            The object's identity
     * @param objectToBeLoaded
     *            The object to fetch (single instance per transaction)
     * @param suggestedAccessMode
     *            The access mode (see {@link AccessMode}) the values in
     *            persistent storage
     * @throws LockNotGrantedException
     *             Timeout or deadlock occured attempting to acquire lock on
     *             object
     * @throws ObjectNotFoundException
     *             The object was not found in persistent storage
     * @throws PersistenceException
     *             An error reported by the persistence engine
     * @return object being loaded
     */
    public synchronized Object load(final LockEngine engine, 
            final ClassMolder molder,
            final Object identity, 
            ProposedObject proposedObject,
            final AccessMode suggestedAccessMode) 
    throws ObjectNotFoundException, LockNotGrantedException, PersistenceException {
        return load(engine, molder, identity, proposedObject,
                suggestedAccessMode, null);
    }

    /**
     * Load an object for use within the transaction. Multiple access to the
     * same object within the transaction will return the same object instance
     * (except for read-only access).
     * <p>
     * This method work the same as
     * {@link #load(LockEngine,ClassMolder,Object,Object,AccessMode)}, except a
     * QueryResults can be specified.
     * <p>
     * 
     * @param engine
     *            The persistence engine
     * @param molder
     *            The class persistence molder
     * @param identity
     *            The object's identity
     * @param proposedObject
     *            The object to fetch (single instance per transaction)
     * @param suggestedAccessMode
     *            The access mode (see {@link AccessMode}) the values in
     *            persistent storage
     * @param results
     *            The QueryResult that the data to be loaded from.
     * @throws LockNotGrantedException
     *             Timeout or deadlock occured attempting to acquire lock on
     *             object
     * @throws ObjectNotFoundException
     *             The object was not found in persistent storage
     * @throws PersistenceException
     *             An error reported by the persistence engine
     * @return object being loaded
     */
    public synchronized Object load(final LockEngine engine, 
            final ClassMolder molder,
            final Object identity, 
            ProposedObject proposedObject,
            final AccessMode suggestedAccessMode, 
            QueryResults results)
    throws ObjectNotFoundException, LockNotGrantedException, PersistenceException {

        Object objectInTransaction;
        OID oid;
        AccessMode accessMode;

        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        // Test that the object to be loaded (which we will fill in) is of an
        // appropriate type for our molder.
        if (proposedObject.getObject() != null
                && !molder.getJavaClass(_db.getClassLoader()).isAssignableFrom(
                        proposedObject.getProposedClass())) {
            throw new PersistenceException(Messages.format(
                    "persist.typeMismatch", molder.getName(), proposedObject.getProposedClass()));
        }

        oid = new OID(engine, molder, identity);
        accessMode = molder.getAccessMode(suggestedAccessMode);
        if (accessMode == AccessMode.ReadOnly) {
            objectInTransaction = _tracker.getObjectForOID(engine, oid, true);
        } else {
            objectInTransaction = _tracker.getObjectForOID(engine, oid, false);
        }

        if (objectInTransaction != null) {
            // Object exists in this transaction.

            // If the object has been loaded, but the instance sugguested to
            // be loaded into is not the same as the loaded instance,
            // error is reported.
            
            // TODO [WG]: could read && propsedObject != objectInTransaction
            if (proposedObject.getObject() != null
                    && proposedObject.getObject() != objectInTransaction) {
                throw new PersistenceException(Messages.format(
                        "persist.multipleLoad", molder.getName(), identity));
            }

            // If the object has been loaded in this transaction from a
            // different engine this is an error. If the object has been
            // deleted in this transaction, it cannot be re-loaded. If the
            // object has been created in this transaction, it cannot be
            // re-loaded but no error is reported.
            if (engine != _tracker.getLockEngineForObject(objectInTransaction)) {
                throw new PersistenceException(Messages.format(
                        "persist.multipleLoad", molder.getName(), identity));
            }

            // Objects marked deleted in the transaction therefore we
            // throw a ObjectNotFoundException to signal that object isn't
            // available any more.
            if (_tracker.isDeleted(objectInTransaction)) {
                throw new ObjectNotFoundException(Messages.format(
                        "persist.objectNotFound", molder.getName(), identity));
            }

            // ssa, multi classloader feature (note - this code appears to be
            // duplicated, yet different, in both cases. Why?)
            // ssa, FIXME : Are the two following statements equivalent ? (bug
            // 998)
            // if ( ! molder.getJavaClass().isAssignableFrom(
            // entry.object.getClass() ) )
            // if ( ! molder.getJavaClass( _db.getClassLoader()
            // ).isAssignableFrom( entry.object.getClass() ) )
            if (!molder.getJavaClass(_db.getClassLoader()).isAssignableFrom(
                    objectInTransaction.getClass())) {
                throw new PersistenceException(Messages.format(
                        "persist.typeMismatch", molder.getName(),
                        objectInTransaction.getClass()));
            }

            // If the object has been created in this transaction, don't bother
            // testing access mode.
            if (_tracker.isCreated(objectInTransaction)) {
                return objectInTransaction;
            }

            if ((accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked)
                    && !_tracker.getOIDForObject(objectInTransaction)
                            .isDbLock()) {
                // If we are in exclusive mode and object has not been
                // loaded in exclusive mode before, then we have a
                // problem. We cannot return an object that is not
                // synchronized with the database, but we cannot
                // synchronize a live object.
                throw new PersistenceException(Messages.format(
                        "persist.lockConflict", molder.getName(), identity));
            }

            return objectInTransaction;
        }

        // Load (or reload, in case the object is stored in a acache) the object through the 
        // persistence engine with the requested lock. This might report failure (object no 
        // longer exists), hold until a suitable lock is granted (or fail to grant), or
        // report error with the persistence engine.
        try {
            if (proposedObject.getObject() != null) {
                objectInTransaction = proposedObject.getObject();
            } else {
                // ssa, multi classloader feature
                // ssa, FIXME : No better way to do that ?
                // object = molder.newInstance();
                if (_instanceFactory != null) {
                    objectInTransaction = _instanceFactory.newInstance(molder
                            .getName(), _db.getClassLoader());
                } else {
                    objectInTransaction = molder.newInstance(_db
                            .getClassLoader());
                }
                
                proposedObject.setProposedClass(objectInTransaction.getClass());
                proposedObject.setActualClass(objectInTransaction.getClass());
                proposedObject.setObject(objectInTransaction);
            }

            molder.setIdentity(this, objectInTransaction, identity);
            _tracker.trackObject(engine, molder, oid, objectInTransaction);
            OID newoid = engine.load(this, oid, proposedObject,
                    suggestedAccessMode, _lockTimeout, results);
                    
            if (proposedObject.isExpanded()) {
            	
                // Remove old OID from ObjectTracker
                _tracker.untrackObject(objectInTransaction);
                
                // Create new OID
                ClassMolder actualClassMolder = engine.getClassMolder(proposedObject.getActualClass());
                OID actualOID = new OID(engine, actualClassMolder, identity);
                actualClassMolder.setIdentity(this, proposedObject.getObject(), identity);

                // Create instance of 'expanded object'
                Object expandedObject = null;
                try {
                    expandedObject = actualClassMolder.newInstance(getClassLoader());
                } catch (InstantiationException e) {
                    _log.error("Cannot create instance of " + molder.getName());
                    throw new PersistenceException ("Cannot craete instance of " + molder.getName());
                } catch (IllegalAccessException e) {
                    _log.error("Cannot create instance of " + molder.getName());
                    throw new PersistenceException ("Cannot craete instance of " + molder.getName());
                } catch (ClassNotFoundException e) {
                    _log.error("Cannot create instance of " + molder.getName());
                    throw new PersistenceException ("Cannot craete instance of " + molder.getName());
                }

                // Add new OID to ObjectTracker
                _tracker.trackObject(engine, molder, actualOID, expandedObject);
                
                ProposedObject proposedExpanded = new ProposedObject();
                proposedExpanded.setProposedClass(proposedObject.getActualClass());
                proposedExpanded.setActualClass(proposedObject.getActualClass());
                proposedExpanded.setObject(expandedObject);
                proposedExpanded.setFields(proposedObject.getFields());
                proposedExpanded.setObjectLockObjectToBeIgnored(true);

                // reload 'expanded object' using correct ClassMolder
                OID onceAgainOID = engine.load(this, actualOID, proposedExpanded,
                        suggestedAccessMode, _lockTimeout, results);

                objectInTransaction = proposedExpanded.getObject();
            } else {
                // rehash the object entry, because oid might have changed!
                _tracker.trackOIDChange(objectInTransaction, engine, oid, newoid);
            }            
            
        } catch (ClassCastException except) {
            _tracker.untrackObject(objectInTransaction);
            throw except;
        } catch (ObjectNotFoundException except) {
            _tracker.untrackObject(objectInTransaction);
            throw except;
        } catch (LockNotGrantedException except) {
            _tracker.untrackObject(objectInTransaction);
            throw except;
        } catch (ClassNotPersistenceCapableException except) {
            _tracker.untrackObject(objectInTransaction);
            throw new PersistenceException(Messages.format("persist.nested",
                    except));
        } catch (InstantiationException e) {
            _tracker.untrackObject(objectInTransaction);
            throw new PersistenceException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            _tracker.untrackObject(objectInTransaction);
            throw new PersistenceException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            _tracker.untrackObject(objectInTransaction);
            throw new PersistenceException(e.getMessage(), e);
        }

        // Need to copy the contents of this object from the cached
        // copy and deal with it based on the transaction semantics.
        // If the mode is read-only we release the lock and forget about
        // it in the contents of this transaction. Otherwise we record
        // the object in this transaction.
        try {
            if (_callback != null) {
                _callback.using(objectInTransaction, _db);
                _callback.loaded(objectInTransaction, accessMode);
            } else if (molder.getCallback() != null) {
                molder.getCallback().using(objectInTransaction, _db);
                molder.getCallback().loaded(objectInTransaction, accessMode);
            }
        } catch (Exception except) {
            release(objectInTransaction);
            throw new PersistenceException(Messages.format("persist.nested",
                    except));
        }

        if (accessMode == AccessMode.ReadOnly) {
            // Mark it read-only.
            _tracker.markReadOnly(objectInTransaction);

            // Release the lock on this object.
            engine.releaseLock(this, oid);

        }
        return objectInTransaction;
    }

    /**
     * Perform a query using the query mechanism and in the specified access
     * mode. The query is performed in this transaction, and the returned query
     * results can only be used while this transaction is open. It is assumed
     * that the query mechanism is compatible with the persistence engine.
     * 
     * @param engine
     *            The persistence engine
     * @param query
     *            A query against the persistence engine
     * @param accessMode
     *            The access mode
     * @return A query result iterator
     * @throws QueryException
     *             An invalid query
     * @throws PersistenceException
     *             An error reported by the persistence engine
     */
    public synchronized QueryResults query(final LockEngine engine,
            final PersistenceQuery query, 
            final AccessMode accessMode, 
            final boolean scrollable)
    throws QueryException, PersistenceException {
        // Need to execute query at this point. This will result in a
        // new result set from the query, or an exception.
        query.execute(getConnection(engine), accessMode, scrollable);
        return new QueryResults(this, engine, query, accessMode, _db);
    }

    public synchronized QueryResults query(final LockEngine engine,
            final PersistenceQuery query, 
            final AccessMode accessMode)
    throws QueryException, PersistenceException {
        return query(engine, query, accessMode, false);
    }

    /**
     * Walk a data object tree starting from the specified object, and mark all
     * object to be created.
     * <p>
     * 
     * @param engine
     *            The persistence engine
     * @param object
     *            The object to persist
     * @throws DuplicateIdentityException
     *             An object with this identity already exists in persistent
     *             storage
     * @throws PersistenceException
     *             An error reported by the persistence engine
     * @throws ClassNotPersistenceCapableException
     *             The class is not persistent capable
     */
    public synchronized void markCreate(final LockEngine engine, 
            final ClassMolder molder,
            Object object, 
            final OID rootObjectOID)
            throws DuplicateIdentityException, PersistenceException {

        OID oid;
        Object identity;

        if (object == null) {
            throw new PersistenceException(
                    "Attempted to mark a null object as created.");
        }

        // Make sure the object has not beed persisted in this transaction.
        identity = molder.getIdentity(this, object);

        // if autoStore is specified, we relieve user life a little bit here
        // so that if an object create automatically and user create it
        // again, it won't receive exception
        if (_autoStore && _tracker.isTracking(object)) {
            return;
        }

        if (_tracker.isDeleted(object)) {
            OID deletedoid = _tracker.getOIDForObject(object);
            throw new PersistenceException(Messages.format(
                    "persist.objectAlreadyPersistent", object.getClass()
                            .getName(), (deletedoid != null) ? deletedoid
                            .getIdentity() : null));
        }

        // Create the object. This can only happen once for each object in
        // all transactions running on the same engine, so after creation
        // add a new entry for this object and use this object as the view
        // Note that the oid which is created is for a dependent object;
        // this is not a change to the rootObjectOID, and therefore doesn't get
        // trackOIDChange()d.
        oid = new OID(engine, molder, rootObjectOID, identity);

        // You shouldn't be able to modify an object marked read-only in this
        // transaction.
        {
            Object trackedObject = _tracker.getObjectForOID(engine, oid, false);
            if (identity != null && trackedObject != null) {
                if (trackedObject != object) {
                    throw new DuplicateIdentityException(
                            "Object being tracked with the OID created for a dependent " 
                            + "object does not match the object to be marked for " 
                            + "creation. Fundamental Tracking Error.");
                } else if (_tracker.isDeleted(object)) {
                    // Undelete it.
                    _tracker.unmarkDeleted(object);
                }
            }
        }

        try {
            _tracker.trackObject(engine, molder, oid, object);
            _tracker.markCreating(object);

            if (_callback != null) {
                _callback.creating(object, _db);
            } else if (molder.getCallback() != null) {
                molder.getCallback().creating(object, _db);
            }

            engine.markCreate(this, oid, object);
        } catch (LockNotGrantedException lneg) {
            // yip: do we need LockNotGrantedException, or should we
            // removed them?
            _tracker.untrackObject(object);
            // Note: This used to throw a very strange empty-string
            // DuplicateIdentityException, which is of course bollocks.
            throw lneg;
        } catch (PersistenceException pe) {
            _tracker.untrackObject(object);
            throw pe;
        } catch (Exception e) {
            _tracker.untrackObject(object);
            throw new PersistenceException(Messages.format("persist.nested", e));
        }
    }

    /**
     * Creates a new object in persistent storage. The object will be persisted
     * only if the transaction commits. If an identity is provided then
     * duplicate identity check happens in this method, if no identity is
     * provided then duplicate identity check occurs when the transaction
     * completes and the object is not visible in this transaction.
     * 
     * @param engine
     *            The persistence engine
     * @param molder
     *            The molder of the creating class
     * @param object
     *            The object to persist
     * @param depended
     *            The master object's OID if exist
     * @throws DuplicateIdentityException
     *             An object with this identity already exists in persistent
     *             storage
     * @throws PersistenceException
     *             An error reported by the persistence engine
     * @throws ClassNotPersistenceCapableException
     *             The class is not persistent capable
     */
    public synchronized void create(final LockEngine engine, 
            final ClassMolder molder,
            Object object, 
            final OID depended) 
    throws DuplicateIdentityException, PersistenceException {

        // markCreate will walk the object tree starting from the specified
        // object and mark all the object to be created.
        markCreate(engine, molder, object, depended);
        walkObjectsToBeCreated();
        walkObjectsWhichNeedCacheUpdate();
    }

    /**
     * After performing create()/update() calls, walk all objects which are to
     * be created in association with that call; lastly, perform the necessary
     * cache updates to those objects if necessary.
     * 
     * @throws DuplicateIdentityException
     * @throws PersistenceException
     */
    private synchronized void walkObjectsToBeCreated()
    throws DuplicateIdentityException, PersistenceException {
        // After the marking is done, we will actually create the object.
        // However, because some objects contains foreign key are key
        // generated, such object should be created after some other.
        // We iterate all object and creating object according the priority.
        Collection createableObjects = _tracker
                .getObjectsWithCreatingStateSortedByLowestMolderPriority();
        Iterator creatableIterator = createableObjects.iterator();
        while (creatableIterator.hasNext()) {
            // Must perform creation after object is recorded in transaction
            // to prevent circular references.
            Object toBeCreated = creatableIterator.next();
            LockEngine toBeCreatedLockEngine = _tracker
                    .getLockEngineForObject(toBeCreated);
            OID toBeCreatedOID = _tracker.getOIDForObject(toBeCreated);
            ClassMolder toBeCreatedMolder = _tracker
                    .getMolderForObject(toBeCreated);

            try {
                // Allow users to create inside the callback.
                // We do this by rechecking that the object is still marked
                // creatable;
                // this will tell us if another process got to it first.
                if (_tracker.isCreating(toBeCreated)) {

                    if (_callback != null) {
                        _callback.creating(toBeCreated, _db);
                    } else if (toBeCreatedMolder.getCallback() != null) {
                        toBeCreatedMolder.getCallback().creating(toBeCreated,
                                _db);
                    }

                    OID oid = toBeCreatedLockEngine.create(this,
                            toBeCreatedOID, toBeCreated);

                    if (oid.getIdentity() == null) {
                        throw new IllegalStateException(
                                "oid.getIdentity() is null after create!");
                    }

                    // rehash the object entry, in case of oid changed
                    _tracker.trackOIDChange(toBeCreated, toBeCreatedLockEngine,
                            toBeCreatedOID, oid);
                    _tracker.markCreated(toBeCreated);

                    if (_callback != null) {
                        _callback.using(toBeCreated, _db);
                        _callback.created(toBeCreated);
                    } else if (toBeCreatedMolder.getCallback() != null) {
                        toBeCreatedMolder.getCallback().using(toBeCreated, _db);
                        toBeCreatedMolder.getCallback().created(toBeCreated);
                    }
                }
            } catch (Exception except) {
                if (_callback != null) {
                    _callback.releasing(toBeCreated, false);
                } else if (toBeCreatedMolder.getCallback() != null) {
                    toBeCreatedMolder.getCallback().releasing(toBeCreated,
                            false);
                }
                _tracker.untrackObject(toBeCreated);
                if (except instanceof DuplicateIdentityException) {
                    throw (DuplicateIdentityException) except;
                } else if (except instanceof PersistenceException) {
                    throw (PersistenceException) except;
                } else {
                    throw new PersistenceException(Messages.format(
                            "persist.nested", except));
                }
            }
        }
    }

    private void walkObjectsWhichNeedCacheUpdate() {
        // after we create the objects, some cache may invalid because the
        // relation are cached on both side. So, we updateCache if it is
        // marked to be update from the markCreate state
        Collection objectsMarkedForUpdate = _tracker
                .getObjectsWithUpdateCacheNeededState();
        Iterator it = objectsMarkedForUpdate.iterator();
        while (it.hasNext()) {
            Object toCacheUpdate = it.next();
            if (_tracker.isCreated(toCacheUpdate)) {
                OID toCacheUpdateOID = _tracker.getOIDForObject(toCacheUpdate);
                LockEngine toCacheUpdateLockEngine = _tracker
                        .getLockEngineForObject(toCacheUpdate);

                toCacheUpdateLockEngine.updateCache(this, toCacheUpdateOID,
                        toCacheUpdate);
                _tracker.unmarkUpdateCacheNeeded(toCacheUpdate);
            }
        }
    }

    /**
     * Update a new object in persistent storage and returns the object's OID.
     * The object will be persisted only if the transaction commits. If an
     * identity is provided then duplicate identity check happens in this
     * method, if no identity is provided then duplicate identity check occurs
     * when the transaction completes and the object is not visible in this
     * transaction.
     * <p>
     * Update will also mark object to be created if the TIMESTAMP equals to
     * NO_TIMESTAMP.
     * 
     * @param engine
     *            The persistence engine
     * @param molder
     *            The object's molder
     * @param object
     *            The object to persist
     * @param depended
     *            The master objects of the specified object to be created if
     *            exisit
     * @return true if the object is marked to be created
     * @throws DuplicateIdentityException
     *             An object with this identity already exists in persistent
     *             storage
     * @throws PersistenceException
     *             An error reported by the persistence engine
     * @throws ClassNotPersistenceCapableException
     *             The class is not persistent capable
     * @throws ObjectModifiedException
     *             Dirty checking mechanism may immediately report that the
     *             object was modified in the database during the long
     *             transaction.
     */
    public boolean markUpdate(final LockEngine engine, 
            final ClassMolder molder,
            Object object, 
            final OID depended) 
    throws DuplicateIdentityException, ObjectModifiedException, 
       ClassNotPersistenceCapableException, PersistenceException {

        Object identity;
        OID oid;

        if (object == null) {
            throw new NullPointerException();
        }

        identity = molder.getActualIdentity(this, object);
        if (molder.isDefaultIdentity(identity)) {
            identity = null;
        }

        oid = new OID(engine, molder, depended, identity);

        // Check the object is in the transaction.
        {
            Object foundInTransaction = _tracker.getObjectForOID(engine, oid,
                    false);
            if (_autoStore && foundInTransaction != null
                    && foundInTransaction == object) {
                return false;
            }

            if (foundInTransaction != null) {
                if (_tracker.isDeleted(foundInTransaction)) {
                    throw new ObjectDeletedException(Messages.format(
                            "persist.objectDeleted", object.getClass(),
                            identity));
                } else {
                    throw new DuplicateIdentityException(
                            "update object which is already in the transaction");
                }
            }
        }

        try {
            _tracker.trackObject(engine, molder, oid, object);
            if (engine.update(this, oid, object, null, 0)) {
                _tracker.markCreating(object);
                // yip: there is one issue here. Because object might be marked
                // to be created in the update process. However, we have no easy
                // way to fire jdoCreating() events from here.

                // TODO: How can I do this? This isn't a problem any more,
                // because create() can handle being called while it's being
                // called elsewhere (is recursive-safe)
                // We may as well do it.
            }
        } catch (DuplicateIdentityException lneg) {
            _tracker.untrackObject(object);
            throw lneg;
        } catch (PersistenceException pe) {
            _tracker.untrackObject(object);
            throw pe;
        } catch (Exception e) {
            _tracker.untrackObject(object);
            throw new PersistenceException(Messages.format("persist.nested", e));
        }

        if (!_tracker.isCreating(object)) {
            try {
                if (_callback != null) {
                    _callback.using(object, _db);
                    _callback.updated(object);
                } else if (molder.getCallback() != null) {
                    molder.getCallback().using(object, _db);
                    molder.getCallback().updated(object);
                }
            } catch (Exception except) {
                release(object);
                if (except instanceof PersistenceException) {
                    throw (PersistenceException) except;
                } else {
                    throw new PersistenceException(except.getMessage(), except);
                }
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Update a new object in persistent storage and returns the object's OID.
     * The object will be persisted only if the transaction commits. If an
     * identity is provided then duplicate identity check happens in this
     * method, if no identity is provided then duplicate identity check occurs
     * when the transaction completes and the object is not visible in this
     * transaction.
     * <p>
     * Update will also mark object to be created if the TIMESTAMP equals to
     * NO_TIMESTAMP.
     * 
     * @param engine
     *            The persistence engine
     * @param molder
     *            The object's molder
     * @param object
     *            The object to persist
     * @param depended
     *            The master objects of the specified object to be created if
     *            exisit
     * @throws DuplicateIdentityException
     *             An object with this identity already exists in persistent
     *             storage
     * @throws PersistenceException
     *             An error reported by the persistence engine
     * @throws ClassNotPersistenceCapableException
     *             The class is not persistent capable
     * @throws ObjectModifiedException
     *             Dirty checking mechanism may immediately report that the
     *             object was modified in the database during the long
     *             transaction.
     */
    public synchronized void update(final LockEngine engine, 
            final ClassMolder molder,
            Object object, 
            final OID depended) 
    throws DuplicateIdentityException, ObjectModifiedException, 
       ClassNotPersistenceCapableException, PersistenceException {

        markUpdate(engine, molder, object, depended);
        walkObjectsToBeCreated();
        walkObjectsWhichNeedCacheUpdate();
    }

    /**
     * Deletes the object from persistent storage. The deletion will take effect
     * only if the transaction is committed, but the object is no longer
     * viewable for the current transaction and locks for access from other
     * transactions will block until this transaction completes. A write lock is
     * acquired in order to assure the object can be deleted.
     * 
     * @param object
     *            The object to delete from persistent storage
     * @throws ObjectNotPersistentException
     *             The object has not been queried or created in this
     *             transaction
     * @throws LockNotGrantedException
     *             Timeout or deadlock occured attempting to acquire lock on
     *             object
     * @throws PersistenceException
     *             An error reported by the persistence engine
     */
    public synchronized void delete(Object object)
    throws ObjectNotPersistentException, LockNotGrantedException,
            PersistenceException {
        if (object == null) {
            throw new PersistenceException("Object to be deleted is null!");
        }

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        if (!_tracker.isTracking(object)) {
            throw new ObjectNotPersistentException(Messages.format(
                    "persist.objectNotPersistent", object.getClass().getName()));
        }

        ClassMolder molder = _tracker.getMolderForObject(object);
        LockEngine engine = _tracker.getLockEngineForObject(object);
        OID oid = _tracker.getOIDForObject(object);

        // Cannot delete same object twice
        if (_tracker.isDeleted(object)) {
            throw new ObjectDeletedException(Messages.format(
                    "persist.objectDeleted", object.getClass().getName(), oid
                            .getIdentity()));
        }

        try {
            if (_callback != null) {
                _callback.removing(object);
            } else if (molder.getCallback() != null) {
                molder.getCallback().removing(object);
            }
        } catch (Exception except) {
            throw new PersistenceException(Messages.format("persist.nested",
                    except));
        }

        // Must acquire a write lock on the object in order to delete it,
        // prevents object form being deleted while someone else is
        // looking at it.
        try {
            _tracker.markDeleted(object);
            engine.softLock(this, oid, _lockTimeout);
            // Mark object as deleted. This will prevent it from being viewed
            // in this transaction and will handle it properly at commit time.
            // The write lock will prevent it from being viewed in another
            // transaction.
            engine.markDelete(this, oid, object, _lockTimeout);

            try {
                if (_callback != null) {
                    _callback.removed(object);
                } else if (molder.getCallback() != null) {
                    molder.getCallback().removed(object);
                }
            } catch (Exception except) {
                throw new PersistenceException(Messages.format(
                        "persist.nested", except));
            }
        } catch (ObjectDeletedException except) {
            // Object has been deleted outside this transaction,
            // forget about it
            _tracker.untrackObject(object);
        }
    }

    /**
     * Acquire a write lock on the object. Read locks are implicitly available
     * when the object is queried. A write lock is only granted for objects that
     * are created or deleted or for objects loaded in exclusive mode - this
     * method can obtain such a lock explicitly. If the object already has a
     * write lock in this transaction or a read lock in this transaction but no
     * read lock in any other transaction, a write lock is obtained. If this
     * object has a read lock in any other transaction this method will block
     * until the other transaction will release its lock. If the specified
     * timeout has elapsed or a deadlock has been detected, an exception will be
     * thrown but the current lock will be retained.
     * 
     * @param object
     *            The object to lock
     * @param timeout
     *            Timeout waiting to acquire lock, specified in seconds, zero
     *            for no waiting, negative to use the default timeout for this
     *            transaction
     * @throws ObjectNotPersistentException
     *             The object has not been queried or created in this
     *             transaction
     * @throws LockNotGrantedException
     *             Timeout or deadlock occured attempting to acquire lock on
     *             object
     * @throws PersistenceException
     *             An error reported by the persistence engine
     */
    public synchronized void writeLock(Object object, final int timeout)
    throws ObjectNotPersistentException, LockNotGrantedException,
       PersistenceException {
        if (object == null) {
            throw new PersistenceException("Object to acquire lock is null!");
        }

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        if (!_tracker.isTracking(object)) {
            throw new ObjectNotPersistentException(Messages.format(
                    "persist.objectNotPersistent", object.getClass().getName()));
        }

        LockEngine engine = _tracker.getLockEngineForObject(object);
        OID oid = _tracker.getOIDForObject(object);

        if (_tracker.isDeleted(object)) {
            throw new ObjectDeletedException(Messages.format(
                    "persist.objectDeleted", object.getClass(), oid
                            .getIdentity()));
        }

        try {

            engine.writeLock(this, oid, timeout);
        } catch (ObjectDeletedException except) {
            // Object has been deleted outside this transaction,
            // forget about it
            _tracker.untrackObject(object);
            throw new ObjectNotPersistentException(Messages.format(
                    "persist.objectNotPersistent", object.getClass().getName()));
        } catch (LockNotGrantedException except) {
            // Can't get lock, but may still keep running
            throw except;
        }
    }

    public synchronized void markModified(final Object object, 
            final boolean updatePersist,
            final boolean updateCache) {
        if (updatePersist) {
            _tracker.markUpdatePersistNeeded(object);
        }
        if (updateCache) {
            _tracker.markUpdateCacheNeeded(object);
        }
    }

    /**
     * Acquire a write lock on the object. Read locks are implicitly available
     * when the object is queried. A write lock is only granted for objects that
     * are created or deleted or for objects loaded in exclusive mode - this
     * method can obtain such a lock explicitly. If the object already has a
     * write lock in this transaction or a read lock in this transaction but no
     * read lock in any other transaction, a write lock is obtained. If this
     * object has a read lock in any other transaction this method will block
     * until the other transaction will release its lock. If the specified
     * timeout has elapsed or a deadlock has been detected, an exception will be
     * thrown but the current lock will be retained.
     * 
     * @param object
     *            The object to lock
     * @param timeout
     *            Timeout waiting to acquire lock, specified in seconds, zero
     *            for no waiting, negative to use the default timeout for this
     *            transaction
     * @throws ObjectNotPersistentException
     *             The object has not been queried or created in this
     *             transaction
     * @throws LockNotGrantedException
     *             Timeout or deadlock occured attempting to acquire lock on
     *             object
     */
    public synchronized void softLock(Object object, 
            final int timeout)
            throws LockNotGrantedException, ObjectNotPersistentException {
        if (object == null) {
            throw new ObjectNotPersistentException(
                    "Object to acquire lock is null!");
        }

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        if (!_tracker.isTracking(object)) {
            throw new ObjectNotPersistentException(Messages.format(
                    "persist.objectNotPersistent", object.getClass().getName()));
        }

        LockEngine engine = _tracker.getLockEngineForObject(object);
        OID oid = _tracker.getOIDForObject(object);

        if (_tracker.isDeleted(object)) {
            throw new ObjectDeletedException(Messages.format(
                    "persist.objectDeleted", object.getClass().getName(), oid
                            .getIdentity()));
        }

        try {
            engine.softLock(this, oid, timeout);
        } catch (ObjectDeletedWaitingForLockException except) {
            // Object has been deleted outside this transaction,
            // forget about it
            _tracker.untrackObject(object);
            throw except;
        } catch (LockNotGrantedException except) {
            // Can't get lock, but may still keep running
            throw except;
        }
    }

    /**
     * Releases the lock granted on the object. The object is removed from this
     * transaction and will not participate in transaction commit/abort. Any
     * changes done to the object are lost.
     * 
     * @param object
     *            The object to release the lock
     * @throws ObjectNotPersistentException
     *             The object was not queried or created in this transaction
     * @throws PersistenceException
     *             An error occured talking to the persistence engine
     */
    public synchronized void release(Object object)
    throws ObjectNotPersistentException, PersistenceException {
        if (object == null) {
            throw new PersistenceException("Object to release lock is null!");
        }

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        if (!_tracker.isTracking(object)) {
            throw new ObjectNotPersistentException(Messages.format(
                    "persist.objectNotPersistent", object.getClass().getName()
                            .getClass()));
        }

        LockEngine engine = _tracker.getLockEngineForObject(object);
        ClassMolder molder = _tracker.getMolderForObject(object);
        OID oid = _tracker.getOIDForObject(object);

        if (_tracker.isDeleted(object)) {
            throw new ObjectDeletedException(Messages.format(
                    "persist.objectDeleted", object.getClass().getName(), oid
                            .getIdentity()));
        }

        // Release the lock, forget about the object in this transaction
        engine.releaseLock(this, oid);
        _tracker.untrackObject(object);

        if (_callback != null) {
            _callback.releasing(object, false);
        } else if (molder != null && molder.getCallback() != null) {
            molder.getCallback().releasing(object, false);
        }

        if (engine == null) {
            throw new PersistenceException(
                    "Release:  Missing engine during release call; fundamental tracking error.");
        }
        if (molder == null) {
            throw new PersistenceException(
                    "Release: Missing molder during release call; fundamental tracking error.");
        }
        if (oid == null) {
            throw new PersistenceException(
                    "Release: Missing OID during release call; fundamental tracking error.");
        }
    }

    /**
     * Prepares the transaction prior to committing it. Indicates whether the
     * transaction is read-only (i.e. no modified objects), can commit, or an
     * error has occured and the transaction must be rolled back. This method
     * performs actual storage into the persistence storage. Multiple calls to
     * this method can be done, and do not release locks, allowing
     * <tt>checkpoint</tt> to occur.
     * 
     * @return True if the transaction can commit, false if the transaction is
     *         read only
     * @throws IllegalStateException
     *             Method called if transaction is not in the proper state to
     *             perform this operation
     * @throws TransactionAbortedException
     *             The transaction has been aborted due to inconsistency,
     *             duplicate object identity, error with the persistence engine
     *             or any other reason
     */
    public synchronized boolean prepare() throws TransactionAbortedException {
        ArrayList todo = new ArrayList();
        ArrayList done = new ArrayList();

        if (_status == Status.STATUS_MARKED_ROLLBACK) {
            throw new TransactionAbortedException("persist.markedRollback");
        }
        if (_status != Status.STATUS_ACTIVE) {
            throw new IllegalStateException(Messages
                    .message("persist.noTransaction"));
        }

        try {
            // No objects in this transaction -- this is a read only transaction
            // Put it into the try block to close connections
            if (_tracker.readWriteSize() == 0) {
                _status = Status.STATUS_PREPARED;
                return false;
            }

            Collection readWriteObjects = _tracker.getReadWriteObjects();
            while (readWriteObjects.size() != done.size()) {
                todo.clear();
                Iterator rwIterator = readWriteObjects.iterator();
                while (rwIterator.hasNext()) {
                    Object object = rwIterator.next();
                    if (!done.contains(object)) {
                        todo.add(object);
                    }
                }

                Iterator todoIterator = todo.iterator();
                while (todoIterator.hasNext()) {
                    Object object = todoIterator.next();
                    // Anything not marked 'deleted' or 'creating' is ready to
                    // consider for store.
                    if ((!_tracker.isDeleted(object))
                            && (!_tracker.isCreating(object))) {
                        LockEngine engine = _tracker
                                .getLockEngineForObject(object);
                        ClassMolder molder = _tracker
                                .getMolderForObject(object);
                        OID oid = _tracker.getOIDForObject(object);

                        OID newoid = engine.preStore(this, oid, object,
                                _lockTimeout);
                        if (newoid != null) {
                            _tracker
                                    .trackOIDChange(object, engine, oid, newoid);
                            _tracker.markUpdateCacheNeeded(object);
                        }
                    }
                    done.add(object);
                }
            }

            // preStore will actually walk all existing object and it might
            // marked
            // some object to be created (and to be removed).
            walkObjectsToBeCreated();

            // Now mark anything ready for create to create them.
            prepareForCreate();

            _status = Status.STATUS_PREPARING;
            prepareForDelete();
            _status = Status.STATUS_PREPARED;

            return true;
        } catch (Exception except) {
            _status = Status.STATUS_MARKED_ROLLBACK;
            if (except instanceof TransactionAbortedException) {
                throw (TransactionAbortedException) except;
            }
            // Any error is reported as transaction aborted
            throw new TransactionAbortedException(Messages.format(
                    "persist.nested", except), except);
        }
    }

    private void prepareForCreate() throws PersistenceException {
        Collection allObjects = _tracker.getReadWriteObjects();
        Iterator it = allObjects.iterator();

        while (it.hasNext()) {
            Object toPrepare = it.next();

            boolean isCreating = _tracker.isCreating(toPrepare);
            boolean isDeleted = _tracker.isDeleted(toPrepare);
            boolean needsPersist = _tracker.isUpdatePersistNeeded(toPrepare);
            boolean needsCache = _tracker.isUpdateCacheNeeded(toPrepare);

            LockEngine engine = _tracker.getLockEngineForObject(toPrepare);
            ClassMolder molder = _tracker.getMolderForObject(toPrepare);
            OID oid = _tracker.getOIDForObject(toPrepare);

            if (!isDeleted && !isCreating) {
                if (needsPersist) {
                    engine.store(this, oid, toPrepare);
                }
                if (needsCache) {
                    engine.softLock(this, oid, _lockTimeout);
                }
            }

            // do the callback
            if (!isDeleted && _callback != null) {
                try {
                    _callback.storing(toPrepare, needsCache);
                } catch (Exception except) {
                    throw new TransactionAbortedException(Messages.format(
                            "persist.nested", except), except);
                }
            } else if (!isDeleted && molder.getCallback() != null) {
                try {
                    molder.getCallback().storing(toPrepare, needsCache);
                    // updatePersistNeeded implies updateCacheNeeded
                } catch (Exception except) {
                    throw new TransactionAbortedException(Messages.format(
                            "persist.nested", except), except);
                }
            }
        }
    }

    private void prepareForDelete() throws PersistenceException {
        Collection objectsToDelete = _tracker
                .getObjectsWithDeletedStateSortedByHighestMolderPriority();
        Iterator it = objectsToDelete.iterator();

        while (it.hasNext()) {
            Object object = it.next();
            LockEngine engine = _tracker.getLockEngineForObject(object);
            OID oid = _tracker.getOIDForObject(object);
            engine.delete(this, oid);
        }
    }

    /**
     * Commits all changes and closes the transaction releasing all locks on all
     * objects. All objects are now transient. Must be called after a call to
     * {@link #prepare}has returned successfully.
     * 
     * @throws TransactionAbortedException
     *             The transaction has been aborted due to inconsistency,
     *             duplicate object identity, error with the persistence engine
     *             or any other reason
     * @throws IllegalStateException
     *             This method has been called without calling {@link #prepare}
     *             first
     */
    public synchronized void commit() throws TransactionAbortedException {
        // Never commit transaction that has been marked for rollback
        if (_status == Status.STATUS_MARKED_ROLLBACK) {
            throw new TransactionAbortedException("persist.markedRollback");
        }
        if (_status != Status.STATUS_PREPARED) {
            throw new IllegalStateException(Messages
                    .message("persist.missingPrepare"));
        }

        try {
            _status = Status.STATUS_COMMITTING;

            // Go through all the connections opened in this transaction,
            // commit and close them one by one.
            commitConnections();
        } catch (Exception except) {
            // Any error that happens, we're going to rollback the transaction.
            _status = Status.STATUS_MARKED_ROLLBACK;
            throw new TransactionAbortedException(Messages.format(
                    "persist.nested", except), except);
        }

        // Assuming all went well in the connection department,
        // no deadlocks, etc. clean all the transaction locks with
        // regards to the persistence engine.
        Collection readWriteObjects = _tracker.getReadWriteObjects();
        Iterator it = readWriteObjects.iterator();
        while (it.hasNext()) {
            Object toCommit = it.next();

            LockEngine engine = _tracker.getLockEngineForObject(toCommit);
            ClassMolder molder = _tracker.getMolderForObject(toCommit);
            OID oid = _tracker.getOIDForObject(toCommit);

            if (_tracker.isDeleted(toCommit)) {
                // Object has been deleted inside transaction,
                // engine must forget about it.
                engine.forgetObject(this, oid);
                molder.setFieldsNull(toCommit);
            } else {
                // Object has been created/accessed inside the
                // transaction, release its lock.
                if (_tracker.isUpdateCacheNeeded(toCommit)) {
                    engine.updateCache(this, oid, toCommit);
                }

                engine.releaseLock(this, oid);
            }

            // Call our release callback on all processed objects.
            if (_callback != null) {
                _callback.releasing(toCommit, true);
            } else if (molder.getCallback() != null) {
                molder.getCallback().releasing(toCommit, true);
            }
        }

        // Call txcommited() before objects are removed to allow
        // TxSynchronizable to iterate through the objects.
        txcommitted();

        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _tracker.clear();
        _status = Status.STATUS_COMMITTED;
    }

    /**
     * Expose an enumeration of the commited object entries to allow
     * TxSynchronizable to iterate through the objects.
     * 
     * @return Iterator of modifiable (read-write) object entries.
     */
    public Iterator iterateReadWriteObjectsInTransaction() {
        return _tracker.getReadWriteObjects().iterator();
    }

    /*
     * Rolls back all changes and closes the transaction releasing all locks on
     * all objects. All objects are now transient, if they were queried in this
     * transaction. This method may be called at any point during the
     * transaction.
     * 
     * @throws IllegalStateException Method called if transaction is not in the
     * proper state to perform this operation
     */
    public synchronized void rollback() {
        if (_status != Status.STATUS_ACTIVE
                && _status != Status.STATUS_PREPARED
                && _status != Status.STATUS_MARKED_ROLLBACK) {
            throw new IllegalStateException(Messages
                    .message("persist.noTransaction"));
        }

        // Go through all the connections opened in this transaction,
        // rollback and close them one by one.
        rollbackConnections();

        // un-delete object first
        _tracker.unmarkAllDeleted();

        // Clean the transaction locks with regards to the
        // database engine
        Collection readWriteObjects = _tracker.getReadWriteObjects();
        Iterator it = readWriteObjects.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            LockEngine engine = _tracker.getLockEngineForObject(object);
            ClassMolder molder = _tracker.getMolderForObject(object);
            OID oid = _tracker.getOIDForObject(object);

            try {
                if (_tracker.isCreating(object)) {
                    // nothing to do
                } else if (_tracker.isCreated(object)) {
                    // Object has been created in this transaction,
                    // it no longer exists, forget about it in the engine.
                    engine.revertObject(this, oid, object);
                    engine.forgetObject(this, oid);
                } else {
                    // Object has been queried (possibly) deleted in this
                    // transaction and release the lock.
                    // if ( entry.updateCacheNeeded || entry.updatePersistNeeded
                    // )
                    engine.revertObject(this, oid, object);
                    engine.releaseLock(this, oid);
                }

                if (_callback != null) {
                    _callback.releasing(object, false);
                } else if (molder.getCallback() != null) {
                    molder.getCallback().releasing(object, false);
                }
            } catch (Exception except) {
                // Don't thow exceptions during a rollback. Just report them.
                _log.error(
                        "Caught exception while rolling back object with OID "
                                + oid, except);
            }
        }

        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _tracker.clear();
        txrolledback();
        _status = Status.STATUS_ROLLEDBACK;
    }

    /**
     * Closes all Connections. Must be called before the end of the transaction
     * in EJB environment or after commit in standalone case.
     * 
     * @throws TransactionAbortedException
     *             The transaction has been aborted due to inconsistency,
     *             duplicate object identity, error with the persistence engine
     *             or any other reason
     * @throws IllegalStateException
     *             This method has been called after the end of the transaction.
     */
    public synchronized void close() throws TransactionAbortedException {
        if (_status != Status.STATUS_ACTIVE
                && _status != Status.STATUS_MARKED_ROLLBACK) {
            throw new IllegalStateException(Messages
                    .message("persist.missingEnd"));
        }
        try {
            // Go through all the connections opened in this transaction,
            // close them one by one.
            closeConnections();

        } catch (Exception except) {
            // Any error that happens, we're going to rollback the transaction.
            _status = Status.STATUS_MARKED_ROLLBACK;
            throw new TransactionAbortedException(Messages.format(
                    "persist.nested", except), except);
        }
    }

    /**
     * Returns true if the object is marked as created in this transaction. Note
     * that this does not find objects in the 'transitional' state of creating.
     * Primarily intended to be used by tests.
     * 
     * @param object
     *            The object to test the state of in this transaction.
     * @return true if the object is marked as created within this transaction.
     */
    public boolean isCreated(final Object object) {
        return _tracker.isCreated(object);
    }

    /**
     * Retrieves the state of the object in this transaction. Specifically, in
     * this case, that the object requires a cache update.
     * 
     * @param object
     *            The object to test the state of in this transaction.
     * @return true if the object is recorded in this transaction with the
     *         requested state.
     */
    public boolean isUpdateCacheNeeded(final Object object) {
        return _tracker.isUpdateCacheNeeded(object);
    }

    /**
     * Retrieves the state of the object in this transaction. Specifically, in
     * this case, that the object requires a persistence update.
     * 
     * @param object
     *            The object to test the state of in this transaction.
     * @return true if the object is recorded in this transaction with the
     *         requested state.
     */
    public boolean isUpdatePersistNeeded(final Object object) {
        return _tracker.isUpdatePersistNeeded(object);
    }

    /**
     * Returns true if the object is persistent in this transaction.
     * 
     * @param object
     *            The object
     * @return True if persistent in transaction
     */
    public boolean isPersistent(final Object object) {
        return (_tracker.isTracking(object) && !_tracker.isDeleted(object));
    }

    /**
     * Returns true if the object is previously queried/loaded/update/create in
     * this transaction
     * 
     * @param object
     *            The object
     * @return True if recorded in this transaction
     */
    public boolean isRecorded(final Object object) {
        return _tracker.isTracking(object);
    }

    public boolean isDepended(final OID master, final Object dependent) {
        OID oid = _tracker.getOIDForObject(dependent);
        if (oid == null) {
            return false;
        }

        OID depends = oid.getDepends();

        if (depends == null) {
            return false;
        }
        return depends.equals(master);
    }

    /**
     * Returns the status of this transaction.
     */
    public int getStatus() {
        return _status;
    }

    /**
     * Returns true if the transaction is open.
     */
    public boolean isOpen() {
        return (_status == Status.STATUS_ACTIVE 
                || _status == Status.STATUS_MARKED_ROLLBACK);
    }

    protected Xid getXid() {
        return _xid;
    }

    /**
     * Indicates which lock this transaction is waiting for. When a transaction
     * attempts to acquire a lock it must indicate which lock it attempts to
     * acquire in order to perform dead-lock detection. This method is called by
     * {@link ObjectLock}before entering the temporary lock-acquire state.
     * 
     * @param lock The lock which this transaction attempts to acquire
     */
    public void setWaitOnLock(final ObjectLock lock) {
        _waitOnLock = lock;
    }

    /**
     * Returns the lock which this transaction attempts to acquire.
     * 
     * @return The lock which this transaction attempts to acquire
     */
    public ObjectLock getWaitOnLock() {
        return _waitOnLock;
    }

    /**
     * True if and only if the specified object is loaded or created in this
     * transaction and is deleted.
     */
    public boolean isDeleted(final Object object) {
        return _tracker.isDeleted(object);
    }

    public boolean isDeletedByOID(final OID oid) {
        Object o = _tracker.getObjectForOID(oid.getLockEngine(), oid, false);
        if (o != null) {
            return _tracker.isDeleted(o);
        }
        
        return false;
    }

    public int getObjectState(Object object) {
        if (_tracker.isReadWrite(object)) {
            boolean created = _tracker.isCreated(object);
            boolean deleted = _tracker.isDeleted(object);
            if (created && !deleted) {
                return OBJECT_STATE_PERSISTENT_NEW;
            }
            if (created && deleted) {
                return OBJECT_STATE_PERSISTENT_NEW_DELETED;
            }
            if (deleted) {
                return OBJECT_STATE_PERSISTENT_DELETED;
            }
            
            return OBJECT_STATE_PERSISTENT;
        } else {
            if (_tracker.isReadOnly(object)) {
                return OBJECT_STATE_READ_ONLY;
            } else {
                return OBJECT_STATE_TRANSIENT;
            }
        }
    }

    /**
     * Get the current application ClassLoader.
     * 
     * @return the current ClassLoader's instance. <code>null</code> if none
     *         has been provided
     */
    public ClassLoader getClassLoader() {
        return _db.getClassLoader();
    }

    /**
     * Marks an object for deletion. Used during the preparation stage to delete
     * an attached relation by marking the object for deletion (if not already
     * deleted) and preparing it a second time.
     */
    void markDelete(final LockEngine engine, final Class type, final Object identity)
    throws LockNotGrantedException, PersistenceException {
        OID oid = new OID(engine, engine.getClassMolder(type), identity);
        Object foundInTransaction = _tracker
                .getObjectForOID(engine, oid, false);
        ClassMolder molder = _tracker.getMolderForObject(foundInTransaction);

        // Grab the 'real' oid for this object; it's possible it has additional data
        // that we'd need.  As we created one from scratch, it's worth trying.
        oid = _tracker.getOIDForObject(foundInTransaction);

        if (foundInTransaction != null
                && !_tracker.isDeleted(foundInTransaction)) {
            try {
                if (_callback != null) {
                    _callback.removing(foundInTransaction);
                } else if (molder.getCallback() != null) {
                    molder.getCallback().removing(foundInTransaction);
                }
            } catch (Exception except) {
                throw new PersistenceException(Messages.format(
                        "persist.nested", except));
            }

            try {
                _tracker.markDeleted(foundInTransaction);
                engine.softLock(this, oid, _lockTimeout);
                engine.markDelete(this, oid, null, _lockTimeout);

                try {
                    if (_callback != null) {
                        _callback.removed(foundInTransaction);
                    } else if (molder.getCallback() != null) {
                        molder.getCallback().removed(foundInTransaction);
                    }
                } catch (Exception except) {
                    throw new PersistenceException(Messages.format(
                            "persist.nested", except));
                }
            } catch (ObjectDeletedException except) {
                // Object has been deleted outside this transaction,
                // forget about it
                _tracker.untrackObject(foundInTransaction);
            }
        }
    }

    /**
     * Expire object from the cache.  Objects expired from the cache will be
     * read from persistent storage, as opposed to being read from the
     * cache, during subsequent load/query operations.
     *
     * @param engine The persistence engine
     * @param molder The class persistence molder
     * @param identity The object's identity
     */
    public synchronized void expireCache(final LockEngine engine, 
            final ClassMolder molder,
            final Object identity)
    throws PersistenceException, LockNotGrantedException {
        OID oid;

        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        oid = new OID(engine, molder, identity);
        Object trackedObject = _tracker.getObjectForOID(engine, oid, false);
        if (trackedObject == null) {
            try {
                // the call to engine.expireCache may result in a
                // recursive call to this.expireCache, therefore,
                // an entry is added to the object list to prevent
                // infinite loops due to bi-directional references
                _tracker.trackObject(engine, molder, oid, identity);

                if (engine.expireCache(this, oid, _lockTimeout)) {
                    engine.releaseLock(this, oid);
                }
            } catch (LockNotGrantedException except) {
                throw except;
            } finally {
                _tracker.untrackObject(identity);
            }
        }
    }

    public boolean isCached(final LockEngine engine, 
            final ClassMolder molder, 
            final Class cls,
            final Object identity)
    throws PersistenceException {
        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        OID oid = new OID(engine, molder, identity);
        return engine.isCached(cls, oid);
    }

    /**
     * Check to see whether this transaction considers an object to have been marked 
     * read-only.
     * @param object The object to test for read-only status
     * @return True if the object is marked read-only in this transaction; otherwise, 
     * false.
     */
    public boolean isReadOnly(final Object object) {
        return _tracker.isReadOnly(object);
    }

    public Database getDatabase() {
        return _db;
    }

}

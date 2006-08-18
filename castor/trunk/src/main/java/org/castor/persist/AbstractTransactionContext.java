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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import javax.transaction.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;

import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DbMetaInfo;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectNotPersistentException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.ObjectLock;
import org.exolab.castor.persist.QueryResults;
import org.exolab.castor.persist.TxSynchronizable;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.exolab.castor.persist.spi.PersistenceQuery;

/**
 * A transaction context is required in order to perform operations against the
 * database. The transaction context is mapped to an API transaction or an XA
 * transaction. The only way to begin a new transaction is through the creation
 * of a new transaction context. A transaction context is created from an
 * implementation class directly or through {@link XAResourceImpl}.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin </a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:gblock AT ctoforaday DOT COM">Gregory Block</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public abstract class AbstractTransactionContext implements TransactionContext {

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

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractTransactionContext.class);
    
    /** Default timeout of transaction and waiting for a lock. */
    private static final int DEFAULT_TIMEOUT = 30;

    /** The ObjectTracker for this transaction, tracking all of the various bits
     *  of state and co-located information (Engine, OID, etc.) for this entry.
     *  See ObjectTracker javadocs for more details. */
    private final ObjectTracker _tracker = new ObjectTracker();

    /** Set while transaction is waiting for a lock. */
    private ObjectLock _waitOnLock;

    /** The transaction status. See {@link Status}for list of valid values. */
    private int _status = -1;

    /** The timeout waiting to acquire a new lock. Specified in seconds. */
    private int _lockTimeout = DEFAULT_TIMEOUT;

    /** The timeout of this transaction. Specified in seconds. */
    private int _txTimeout = DEFAULT_TIMEOUT;

    /** The database to which this transaction belongs. */
    private Database _db;

    /** True if user prefer all reachable object to be stored automatically.
     *  False if user want only dependent object to be stored. */
    private boolean _autoStore;

    /** The default callback interceptor for the data object in this transaction. */
    private CallbackInterceptor _callback;

    /** The instance factory to that creates new instances of data object. */
    private InstanceFactory _instanceFactory;

    /** A list of listeners which will be informed about various transaction states. */
    private ArrayList _synchronizeList = new ArrayList();

    /** Lists all the connections opened for particular database engines
     *  used in the lifetime of this transaction. The database engine
     *  is used as the key to an open/transactional connection. */
    private Hashtable _conns = new Hashtable();

    /** Meta-data related to the RDBMS used. */
    private DbMetaInfo _dbInfo;

    /**
     * Create a new transaction context. This method is used by the explicit
     * transaction model.
     * 
     * @param db Database instance
     */
    public AbstractTransactionContext(final Database db) {
        _db = db;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#addTxSynchronizable(
     *      org.exolab.castor.persist.TxSynchronizable)
     */
    public final void addTxSynchronizable(final TxSynchronizable synchronizable) {
        _synchronizeList.add(synchronizable);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#removeTxSynchronizable(
     *      org.exolab.castor.persist.TxSynchronizable)
     */
    public final void removeTxSynchronizable(final TxSynchronizable synchronizable) {
        _synchronizeList.remove(synchronizable);
    }

    /**
     * Inform all registered listeners that the transaction was committed.
     */
    private void txcommitted() {
        for (int i = 0; i < _synchronizeList.size(); i++) {
            TxSynchronizable sync = (TxSynchronizable) _synchronizeList.get(i);
            try {
                sync.committed(this);
            } catch (Exception ex) {
                String cls = sync.getClass().getName();
                LOG.warn("Exception at " + cls + ".committed()", ex);
            }
        }
    }

    /**
     * Inform all registered listeners that the transaction was rolled back.
     */
    private void txrolledback() {
        for (int i = 0; i < _synchronizeList.size(); i++) {
            TxSynchronizable sync = (TxSynchronizable) _synchronizeList.get(i);
            try {
                sync.rolledback(this);
            } catch (Exception ex) {
                String cls = sync.getClass().getName();
                LOG.warn("Exception at " + cls + ".rolledback()", ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setAutoStore(boolean)
     */
    public final void setAutoStore(final boolean autoStore) {
        _autoStore = autoStore;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isAutoStore()
     */
    public final boolean isAutoStore() {
        return _autoStore;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setCallback(
     *      org.exolab.castor.persist.spi.CallbackInterceptor)
     */
    public final void setCallback(final CallbackInterceptor callback) {
        _callback = callback;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setInstanceFactory(
     *      org.exolab.castor.persist.spi.InstanceFactory)
     */
    public final void setInstanceFactory(final InstanceFactory factory) {
        _instanceFactory = factory;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setTransactionTimeout(int)
     */
    public final void setTransactionTimeout(final int timeout) {
        _txTimeout = timeout;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getTransactionTimeout()
     */
    public final int getTransactionTimeout() {
        return _txTimeout;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getLockTimeout()
     */
    public final int getLockTimeout() {
        return _lockTimeout;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setLockTimeout(int)
     */
    public final void setLockTimeout(final int timeout) {
        _lockTimeout = (timeout >= 0 ? timeout : 0);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setStatus(int)
     */
    public final void setStatus(final int status) {
        _status = status;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getConnection(
     *      org.exolab.castor.persist.LockEngine)
     */
    public final Connection getConnection(final LockEngine engine)
    throws PersistenceException {
        Connection conn = (Connection) _conns.get(engine);
        if (conn == null) {
            conn = createConnection(engine);
            _conns.put(engine, conn);
        }
        return conn;
    }
    
    protected abstract Connection createConnection(final LockEngine engine)
    throws PersistenceException;
        
    protected final Iterator connectionsIterator() {
        return _conns.values().iterator();
    }
    
    protected final void clearConnections() {
        _conns.clear();
    }
    
    /**
     * The derived class must implement this method and commit all the connections
     * used in this transaction. If the transaction could not commit fully or
     * partially, this method will throw an {@link TransactionAbortedException},
     * causing a rollback to occur as the next step.
     * 
     * @throws TransactionAbortedException The transaction could not commit fully
     *         or partially and should be rolled back.
     */
    protected abstract void commitConnections() throws TransactionAbortedException;

    /**
     * The derived class must implement this method and close all the connections
     * used in this transaction.
     * 
     * @throws TransactionAbortedException The transaction could not close all the
     *         connections.
     */
    protected abstract void closeConnections() throws TransactionAbortedException;

    /**
     * The derived class must implement this method and rollback all the
     * connections used in this transaction. The connections may be closed, as
     * they will not be reused in this transaction. This operation is guaranteed
     * to succeed.
     */
    protected abstract void rollbackConnections();

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getConnectionInfo(
     *      org.exolab.castor.persist.LockEngine)
     */
    public final DbMetaInfo getConnectionInfo(final LockEngine engine)
    throws PersistenceException {
        Connection conn = getConnection(engine);
        if (_dbInfo == null) {
            _dbInfo = new DbMetaInfo(conn);
        }
        return _dbInfo;
    }
    
    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext
     *      #fetch(org.exolab.castor.persist.ClassMolder,
     *             org.exolab.castor.persist.spi.Identity,
     *             org.exolab.castor.mapping.AccessMode)
     */
    public final synchronized Object fetch(
            final ClassMolder molder, final Identity identity,
            final AccessMode suggestedAccessMode)
    throws PersistenceException {

        Object objectInTx;
        OID oid;
        AccessMode accessMode;

        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        LockEngine engine = molder.getLockEngine();
        
        oid = new OID(molder, identity);
        accessMode = molder.getAccessMode(suggestedAccessMode);
        if (accessMode == AccessMode.ReadOnly) {
            objectInTx = _tracker.getObjectForOID(engine, oid, true);
        } else {
            objectInTx = _tracker.getObjectForOID(engine, oid, false);
        }

        if (objectInTx != null) {
            // Object exists in this transaction.

            // If the object has been loaded in this transaction from a
            // different engine this is an error. If the object has been
            // deleted in this transaction, it cannot be re-loaded. If the
            // object has been created in this transaction, it cannot be
            // re-loaded but no error is reported.
            if (engine != _tracker.getMolderForObject(objectInTx).getLockEngine()) {
                throw new PersistenceException(Messages.format(
                        "persist.multipleLoad", molder.getName(), identity));
            }

            // Objects marked deleted in the transaction return null.
            if (_tracker.isDeleted(objectInTx)) {
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
            if (!molder.isAssignableFrom(objectInTx.getClass())) {
                throw new PersistenceException(Messages.format(
                        "persist.typeMismatch", molder.getName(), identity));
            }

            // If the object has been created in this transaction, don't bother
            // testing access mode.
            if (_tracker.isCreated(objectInTx)) {
                return objectInTx;
            }

            if ((accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked)
                    && !_tracker.getOIDForObject(objectInTx).isDbLock()) {
                // If we are in exclusive mode and object has not been
                // loaded in exclusive mode before, then we have a
                // problem. We cannot return an object that is not
                // synchronized with the database, but we cannot
                // synchronize a live object.
                throw new PersistenceException(Messages.format(
                        "persist.lockConflict", molder.getName(), identity));
            }
            return objectInTx;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#load(
     *      java.lang.Object, org.castor.persist.ProposedEntity,
     *      org.exolab.castor.mapping.AccessMode)
     */
    public final synchronized Object load(
            final Identity identity,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode) 
    throws PersistenceException {
        return load(identity, proposedObject, suggestedAccessMode, null);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#load(
     *      java.lang.Object, org.castor.persist.ProposedEntity,
     *      org.exolab.castor.mapping.AccessMode, org.exolab.castor.persist.QueryResults)
     */
    public final synchronized Object load(
            final Identity identity,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode, 
            final QueryResults results)
    throws PersistenceException {
        
        Object objectInTx;
        OID oid;
        AccessMode accessMode;

        ClassMolder molder = proposedObject.getActualClassMolder();
        LockEngine engine = molder.getLockEngine();

        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        // Test that the object to be loaded (which we will fill in) is of an
        // appropriate type for our molder.
        if (proposedObject.getEntity() != null
                && !molder.getJavaClass(_db.getClassLoader()).isAssignableFrom(
                        proposedObject.getProposedEntityClass())) {
            throw new PersistenceException(Messages.format("persist.typeMismatch",
                    molder.getName(), proposedObject.getProposedEntityClass()));
        }
        
        oid = new OID(molder, identity);
        accessMode = molder.getAccessMode(suggestedAccessMode);
        if (accessMode == AccessMode.ReadOnly) {
            objectInTx = _tracker.getObjectForOID(engine, oid, true);
        } else {
            objectInTx = _tracker.getObjectForOID(engine, oid, false);
        }

        if (objectInTx != null) {
            // Object exists in this transaction.

            // If the object has been loaded, but the instance sugguested to
            // be loaded into is not the same as the loaded instance,
            // error is reported.
            
            // TODO [WG]: could read && propsedObject != objectInTransaction
            if (proposedObject.getEntity() != null
                    && proposedObject.getEntity() != objectInTx) {
                throw new PersistenceException(Messages.format(
                        "persist.multipleLoad", molder.getName(), identity));
            }

            // If the object has been loaded in this transaction from a
            // different engine this is an error. If the object has been
            // deleted in this transaction, it cannot be re-loaded. If the
            // object has been created in this transaction, it cannot be
            // re-loaded but no error is reported.
            if (engine != _tracker.getMolderForObject(objectInTx).getLockEngine()) {
                throw new PersistenceException(Messages.format(
                        "persist.multipleLoad", molder.getName(), identity));
            }

            // Objects marked deleted in the transaction therefore we
            // throw a ObjectNotFoundException to signal that object isn't
            // available any more.
            if (_tracker.isDeleted(objectInTx)) {
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
                    objectInTx.getClass())) {
                throw new PersistenceException(Messages.format(
                        "persist.typeMismatch", molder.getName(),
                        objectInTx.getClass()));
            }

            // If the object has been created in this transaction, don't bother
            // testing access mode.
            if (_tracker.isCreated(objectInTx)) {
                return objectInTx;
            }

            if ((accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked)
                    && !_tracker.getOIDForObject(objectInTx)
                            .isDbLock()) {
                // If we are in exclusive mode and object has not been
                // loaded in exclusive mode before, then we have a
                // problem. We cannot return an object that is not
                // synchronized with the database, but we cannot
                // synchronize a live object.
                throw new PersistenceException(Messages.format(
                        "persist.lockConflict", molder.getName(), identity));
            }

            return objectInTx;
        }

        // Load (or reload, in case the object is stored in a acache) the object
        // through the persistence engine with the requested lock. This might report
        // failure (object no longer exists), hold until a suitable lock is granted
        // (or fail to grant), or report error with the persistence engine.
        try {
            if (proposedObject.getEntity() != null) {
                objectInTx = proposedObject.getEntity();
            } else {
                // ssa, multi classloader feature
                // ssa, FIXME : No better way to do that ?
                // object = molder.newInstance();
                if (_instanceFactory != null) {
                    objectInTx = _instanceFactory.newInstance(molder
                            .getName(), _db.getClassLoader());
                } else {
                    objectInTx = molder.newInstance(_db
                            .getClassLoader());
                }
                
                proposedObject.setProposedEntityClass(objectInTx.getClass());
                proposedObject.setActualEntityClass(objectInTx.getClass());
                proposedObject.setEntity(objectInTx);
            }

            molder.setIdentity(this, objectInTx, identity);
            _tracker.trackObject(molder, oid, objectInTx);
            OID newoid = engine.load(this, oid, proposedObject,
                    suggestedAccessMode, _lockTimeout, results);
                    
            if (proposedObject.isExpanded()) {
                // Remove old OID from ObjectTracker
                _tracker.untrackObject(objectInTx);
                
                // Create new OID
                ClassMolder actualClassMolder = engine.getClassMolder(
                        proposedObject.getActualEntityClass());
                OID actualOID = new OID(actualClassMolder, identity);
                actualClassMolder.setIdentity(this, proposedObject.getEntity(), identity);

                // Create instance of 'expanded object'
                Object expandedObject = null;
                try {
                    expandedObject = actualClassMolder.newInstance(getClassLoader());
                } catch (Exception e) {
                    LOG.error("Cannot create instance of " + molder.getName());
                    throw new PersistenceException(
                            "Cannot craete instance of " + molder.getName());
                }

                // Add new OID to ObjectTracker
                _tracker.trackObject(molder, actualOID, expandedObject);
                
                ProposedEntity proposedExpanded = new ProposedEntity(proposedObject);
                proposedExpanded.setEntity(expandedObject);
                proposedExpanded.setObjectLockObjectToBeIgnored(true);

                // reload 'expanded object' using correct ClassMolder
                engine.load(this, actualOID, proposedExpanded,
                        suggestedAccessMode, _lockTimeout, results);

                objectInTx = proposedExpanded.getEntity();
            } else {
                // rehash the object entry, because oid might have changed!
                _tracker.trackOIDChange(objectInTx, engine, oid, newoid);
            }            
            
        } catch (ClassCastException except) {
            _tracker.untrackObject(objectInTx);
            throw except;
        } catch (ObjectNotFoundException except) {
            _tracker.untrackObject(objectInTx);
            throw except;
        } catch (LockNotGrantedException except) {
            _tracker.untrackObject(objectInTx);
            throw except;
        } catch (ClassNotPersistenceCapableException except) {
            _tracker.untrackObject(objectInTx);
            throw new PersistenceException(Messages.format("persist.nested",
                    except));
        } catch (InstantiationException e) {
            _tracker.untrackObject(objectInTx);
            throw new PersistenceException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            _tracker.untrackObject(objectInTx);
            throw new PersistenceException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            _tracker.untrackObject(objectInTx);
            throw new PersistenceException(e.getMessage(), e);
        }

        // Need to copy the contents of this object from the cached
        // copy and deal with it based on the transaction semantics.
        // If the mode is read-only we release the lock and forget about
        // it in the contents of this transaction. Otherwise we record
        // the object in this transaction.
        try {
            if (_callback != null) {
                _callback.using(objectInTx, _db);
                _callback.loaded(objectInTx, accessMode);
            } else if (molder.getCallback() != null) {
                molder.getCallback().using(objectInTx, _db);
                molder.getCallback().loaded(objectInTx, accessMode);
            }
        } catch (Exception except) {
            release(objectInTx);
            throw new PersistenceException(Messages.format("persist.nested",
                    except));
        }

        if (accessMode == AccessMode.ReadOnly) {
            // Mark it read-only.
            _tracker.markReadOnly(objectInTx);

            // Release the lock on this object.
            engine.releaseLock(this, oid);

        }
        return objectInTx;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#query(
     *      org.exolab.castor.persist.LockEngine,
     *      org.exolab.castor.persist.spi.PersistenceQuery,
     *      org.exolab.castor.mapping.AccessMode, boolean)
     */
    public final synchronized QueryResults query(final LockEngine engine,
            final PersistenceQuery query, final AccessMode accessMode,
            final boolean scrollable)
    throws PersistenceException {
        // Need to execute query at this point. This will result in a
        // new result set from the query, or an exception.
        query.execute(getConnection(engine), accessMode, scrollable);
        return new QueryResults(this, engine, query, accessMode, _db);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#markCreate(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
    public final synchronized void markCreate(
            final ClassMolder molder, final Object object,  final OID rootObjectOID)
    throws PersistenceException {
        if (object == null) {
            throw new PersistenceException(
                    "Attempted to mark a null object as created.");
        }

        LockEngine engine = molder.getLockEngine();
        
        // Make sure the object has not beed persisted in this transaction.
        Identity identity = molder.getIdentity(this, object);

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
        OID oid = new OID(molder, rootObjectOID, identity);

        // You shouldn't be able to modify an object marked read-only in this
        // transaction.
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

        try {
            _tracker.trackObject(molder, oid, object);
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
            throw new PersistenceException(Messages.format("persist.nested", e), e);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#create(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
    public final synchronized void create(
            final ClassMolder molder, final Object object, final OID depended)
    throws PersistenceException {

        // markCreate will walk the object tree starting from the specified
        // object and mark all the object to be created.
        markCreate(molder, object, depended);
        walkObjectsToBeCreated();
        walkObjectsWhichNeedCacheUpdate();
    }

    /**
     * After performing create()/update() calls, walk all objects which are to
     * be created in association with that call; lastly, perform the necessary
     * cache updates to those objects if necessary.
     * 
     * @throws PersistenceException
     */
    private synchronized void walkObjectsToBeCreated() throws PersistenceException {
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
            OID toBeCreatedOID = _tracker.getOIDForObject(toBeCreated);
            ClassMolder toBeCreatedMolder = _tracker.getMolderForObject(toBeCreated);
            LockEngine toBeCreatedLockEngine = toBeCreatedMolder.getLockEngine();

            try {
                // Allow users to create inside the callback.
                // We do this by rechecking that the object is still marked
                // creatable;
                // this will tell us if another process got to it first.
                if (_tracker.isCreating(toBeCreated)) {

                    if (_callback != null) {
                        _callback.creating(toBeCreated, _db);
                    } else if (toBeCreatedMolder.getCallback() != null) {
                        toBeCreatedMolder.getCallback().creating(toBeCreated, _db);
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
                            "persist.nested", except), except);
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
                LockEngine toCacheUpdateLocker =
                    _tracker.getMolderForObject(toCacheUpdate).getLockEngine();
                toCacheUpdateLocker.updateCache(this, toCacheUpdateOID, toCacheUpdate);
                _tracker.unmarkUpdateCacheNeeded(toCacheUpdate);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#markUpdate(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
    public final boolean markUpdate(
            final ClassMolder molder, final Object object, final OID depended)
    throws PersistenceException {
        if (object == null) { throw new NullPointerException(); }

        LockEngine engine = molder.getLockEngine();
        
        Identity identity = molder.getActualIdentity(this, object);
        if (molder.isDefaultIdentity(identity)) { identity = null; }

        OID oid = new OID(molder, depended, identity);

        // Check the object is in the transaction.
        Object foundInTransaction = _tracker.getObjectForOID(engine, oid, false);
        if (_autoStore && foundInTransaction != null
                && foundInTransaction == object) {
            return false;
        }

        if (foundInTransaction != null) {
            if (_tracker.isDeleted(foundInTransaction)) {
                throw new ObjectDeletedException(Messages.format(
                        "persist.objectDeleted", object.getClass(), identity));
            }
            
            throw new DuplicateIdentityException(
                    "update object which is already in the transaction");
        }

        try {
            _tracker.trackObject(molder, oid, object);
            if (engine.update(this, oid, object, null, 0)) {
                _tracker.markCreating(object);
                // yip: there is one issue here. Because object might be marked
                // to be created in the update process. However, we have no easy
                // way to fire jdoCreating() events from here.

                // TODO How can I do this? This isn't a problem any more,
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
            throw new PersistenceException(Messages.format("persist.nested", e), e);
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
                }
                throw new PersistenceException(except.getMessage(), except);
            }
            return false;
        } 
        return true;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#update(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object, org.exolab.castor.persist.OID)
     */
    public final synchronized void update(
            final ClassMolder molder, final Object object, final OID depended)
    throws PersistenceException {

        markUpdate(molder, object, depended);
        walkObjectsToBeCreated();
        walkObjectsWhichNeedCacheUpdate();
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#delete(java.lang.Object)
     */
    public final synchronized void delete(final Object object)
    throws PersistenceException {
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
        LockEngine engine = molder.getLockEngine();
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
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#writeLock(java.lang.Object, int)
     */
    public final synchronized void writeLock(final Object object, final int timeout)
    throws PersistenceException {
        if (object == null) {
            throw new PersistenceException("Object to acquire lock is null!");
        }

        // Get the entry for this object, if it does not exist
        // the object has never been persisted in this transaction
        if (!_tracker.isTracking(object)) {
            throw new ObjectNotPersistentException(Messages.format(
                    "persist.objectNotPersistent", object.getClass().getName()));
        }

        LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
        OID oid = _tracker.getOIDForObject(object);

        if (_tracker.isDeleted(object)) {
            throw new ObjectDeletedException(Messages.format(
                    "persist.objectDeleted", object.getClass(), oid.getIdentity()));
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

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#markModified(
     *      java.lang.Object, boolean, boolean)
     */
    public final synchronized void markModified(final Object object, 
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
     * Releases the lock granted on the object. The object is removed from this
     * transaction and will not participate in transaction commit/abort. Any
     * changes done to the object are lost.
     * 
     * @param object The object to release the lock.
     * @throws PersistenceException The object was not queried or created in this
     *         transaction.An error occured talking to the persistence engine.
     */
    private synchronized void release(final Object object) throws PersistenceException {
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

        ClassMolder molder = _tracker.getMolderForObject(object);
        LockEngine engine = molder.getLockEngine();
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
            throw new PersistenceException("Release: "
                    + "Missing engine during release call; fundamental tracking error.");
        }
        if (molder == null) {
            throw new PersistenceException("Release: "
                    + "Missing molder during release call; fundamental tracking error.");
        }
        if (oid == null) {
            throw new PersistenceException("Release: "
                    + "Missing OID during release call; fundamental tracking error.");
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#prepare()
     */
    public final synchronized boolean prepare() throws TransactionAbortedException {
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
                        LockEngine engine =
                            _tracker.getMolderForObject(object).getLockEngine();
                        //_tracker.getMolderForObject(object);
                        OID oid = _tracker.getOIDForObject(object);

                        OID newoid = engine.preStore(this, oid, object, _lockTimeout);
                        if (newoid != null) {
                            _tracker.trackOIDChange(object, engine, oid, newoid);
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

            ClassMolder molder = _tracker.getMolderForObject(toPrepare);
            LockEngine engine = molder.getLockEngine();
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
            LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
            OID oid = _tracker.getOIDForObject(object);
            engine.delete(this, oid);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#commit()
     */
    public final synchronized void commit() throws TransactionAbortedException {
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

            ClassMolder molder = _tracker.getMolderForObject(toCommit);
            LockEngine engine = molder.getLockEngine();
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
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#iterateReadWriteObjectsInTransaction()
     */
    public final Iterator iterateReadWriteObjectsInTransaction() {
        return _tracker.getReadWriteObjects().iterator();
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#rollback()
     */
    public final synchronized void rollback() {
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

        // Clean the transaction locks with regards to the database engine.
        Collection readWriteObjects = _tracker.getReadWriteObjects();
        OID oid = null;
        try {
            Iterator it = readWriteObjects.iterator();
            // First revert all objects
            while (it.hasNext()) {
                Object object = it.next();
                LockEngine engine = _tracker.getMolderForObject(object).getLockEngine();
                oid = _tracker.getOIDForObject(object);
                if (!_tracker.isCreating(object)) {
                    engine.revertObject(this, oid, object);
                }
            }

            // then forget object or release lock on them
            it = readWriteObjects.iterator();
            while (it.hasNext()) {
                Object object = it.next();
                ClassMolder molder = _tracker.getMolderForObject(object);
                LockEngine engine = molder.getLockEngine();
                oid = _tracker.getOIDForObject(object);

                if (!_tracker.isCreating(object)) {
                    if (_tracker.isCreated(object)) {
                        // Object has been created in this transaction,
                        // it no longer exists, forget about it in the engine.
                        engine.forgetObject(this, oid);
                    } else {
                        // Object has been queried (possibly) deleted in this
                        // transaction and release the lock.
                        engine.releaseLock(this, oid);
                    }
                }

                if (_callback != null) {
                    _callback.releasing(object, false);
                } else if (molder.getCallback() != null) {
                    molder.getCallback().releasing(object, false);
                }
            }
        } catch (Exception except) {
            // Don't thow exceptions during a rollback. Just report them.
            LOG.error("Caught exception at rollback of object with OID " + oid, except);
        }

        // Forget about all the objects in this transaction,
        // and mark it as completed.
        _tracker.clear();
        txrolledback();
        _status = Status.STATUS_ROLLEDBACK;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#close()
     */
    public final synchronized void close() throws TransactionAbortedException {
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
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isCreated(java.lang.Object)
     */
    public final boolean isCreated(final Object object) {
        return _tracker.isCreated(object);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isUpdateCacheNeeded(java.lang.Object)
     */
    public final boolean isUpdateCacheNeeded(final Object object) {
        return _tracker.isUpdateCacheNeeded(object);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isUpdatePersistNeeded(java.lang.Object)
     */
    public final boolean isUpdatePersistNeeded(final Object object) {
        return _tracker.isUpdatePersistNeeded(object);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isPersistent(java.lang.Object)
     */
    public final boolean isPersistent(final Object object) {
        return (_tracker.isTracking(object) && !_tracker.isDeleted(object));
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isRecorded(java.lang.Object)
     */
    public final boolean isRecorded(final Object object) {
        return _tracker.isTracking(object);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isDepended(
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
    public final boolean isDepended(final OID master, final Object dependent) {
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
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getStatus()
     */
    public final int getStatus() {
        return _status;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isOpen()
     */
    public final boolean isOpen() {
        return ((_status == Status.STATUS_ACTIVE) 
             || (_status == Status.STATUS_MARKED_ROLLBACK));
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#setWaitOnLock(
     *      org.exolab.castor.persist.ObjectLock)
     */
    public final void setWaitOnLock(final ObjectLock lock) {
        _waitOnLock = lock;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getWaitOnLock()
     */
    public final ObjectLock getWaitOnLock() {
        return _waitOnLock;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isDeleted(java.lang.Object)
     */
    public final boolean isDeleted(final Object object) {
        return _tracker.isDeleted(object);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isDeletedByOID(
     *      org.exolab.castor.persist.OID)
     */
    public final boolean isDeletedByOID(final OID oid) {
        Object o = _tracker.getObjectForOID(oid.getMolder().getLockEngine(), oid, false);
        if (o != null) {
            return _tracker.isDeleted(o);
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getClassLoader()
     */
    public final ClassLoader getClassLoader() {
        return _db.getClassLoader();
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#expireCache(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object)
     */
    public final synchronized void expireCache(
            final ClassMolder molder, final Identity identity)
    throws PersistenceException {
        OID oid;
        
        LockEngine engine = molder.getLockEngine();

        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        oid = new OID(molder, identity);
        Object trackedObject = _tracker.getObjectForOID(engine, oid, false);
        if (trackedObject == null) {
            try {
                // the call to engine.expireCache may result in a
                // recursive call to this.expireCache, therefore,
                // an entry is added to the object list to prevent
                // infinite loops due to bi-directional references
                _tracker.trackObject(molder, oid, identity);

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

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isCached(
     *      org.exolab.castor.persist.ClassMolder,
     *      java.lang.Class, java.lang.Object)
     */
    public final boolean isCached(final ClassMolder molder,
            final Class cls, final Identity identity)
    throws PersistenceException {
        if (identity == null) {
            throw new PersistenceException("Identities can't be null!");
        }

        OID oid = new OID(molder, identity);
        return molder.getLockEngine().isCached(cls, oid);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#isReadOnly(java.lang.Object)
     */
    public final boolean isReadOnly(final Object object) {
        return _tracker.isReadOnly(object);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getDatabase()
     */
    public final Database getDatabase() {
        return _db;
    }
    
    /**
     * Returns true if the object given is locked.
     * 
     * @param cls Class instance of the object to be investigated.
     * @param identity Identity of the object to be investigated. 
     * @param lockEngine Current LcokEngine instance
     * @return True if the object in question is locked.
     */
    public final boolean isLocked(final Class cls, final Identity identity,
            final LockEngine lockEngine) {
        OID oid = new OID(lockEngine.getClassMolder(cls), identity);
        return lockEngine.isLocked(cls, oid);
    }

    /**
     * @inheritDoc
     * @see org.castor.persist.TransactionContext#getNamedQuery(java.lang.String)
     */
    public String getNamedQuery(final ClassMolder molder, final String name) 
    throws QueryException    {
        if (molder == null) {
            throw new QueryException("Invalid argument - molder is null");
        }
        return molder.getNamedQuery(name);
    }
    
    
}

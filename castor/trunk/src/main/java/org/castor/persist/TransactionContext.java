package org.castor.persist;

import java.sql.Connection;
import java.util.Iterator;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DbMetaInfo;
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
 * implementation class directly or through
 * {@link org.exolab.castor.persist.XAResourceImpl}.
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin </a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:gblock AT ctoforaday DOT COM">Gregory Block</a>
 * @version $Revision$ $Date: 2006-04-22 11:05:30 -0600 (Sat, 22 Apr 2006) $
 * @since 1.0
 */
public interface TransactionContext {
    /**
     * Register a listener which wants to synchronize its state to the state of the
     * transaction.
     * 
     * @param synchronizable The TxSynchronizable implementation to register.
     */
    void addTxSynchronizable(final TxSynchronizable synchronizable);

    /**
     * Unregister a listener which wants to synchronize its state to the state of the
     * transaction.
     * 
     * @param synchronizable The TxSynchronizable implementation to unregister.
     */
    void removeTxSynchronizable(final TxSynchronizable synchronizable);

    /**
     * Enable or disable autoStore. If enabled, all new objects, which are reachable
     * from other object that are queried, loaded, created in the transaction, will be
     * created when the transaction is committed.
     * 
     * @param autoStore When set to <code>true</code> autoStore will be enabled.
     */
    void setAutoStore(final boolean autoStore);

    /**
     * Test if autoStore option is enabled or not.
     *
     * @return <code>true</code> if autoStore option is enabled.
     */
    boolean isAutoStore();

    /**
     * Overrides the default callback interceptor by a custom interceptor for this
     * database source.
     * <p>
     * The interceptor is a callback that notifies data objects on persistent state
     * events.
     * <p>
     * If callback interceptor is not overriden, events will be sent to data object
     * that implements the org.exolab.castor.jdo.Persistent interface.
     * 
     * @param callback The callback interceptor, disabled if null.
     */
    void setCallback(final CallbackInterceptor callback);

    /**
     * Overrides the default instance factory by a custom one for this database
     * source.
     * <p>
     * The factory is used to obatain a new instance of data object when it is
     * needed during loading.
     * 
     * @param factory The instanceFactory to be used, disable if null.
     */
    void setInstanceFactory(final InstanceFactory factory);

    /**
     * Sets the timeout of this transaction. The timeout is specified in seconds.
     * 
     * @param timeout The timeout for this transaction in seconds.
     */
    void setTransactionTimeout(final int timeout);

    /**
     * Returns the timeout of this transaction. The timeout is specified in seconds.
     * 
     * @return The timeout of this transaction in seconds.
     */
    int getTransactionTimeout();

    /**
     * Sets the timeout waiting to acquire a lock. The timeout is specified in
     * seconds.
     * 
     * @param timeout The timeout waiting to acquire a lock in seconds.
     */
    void setLockTimeout(final int timeout);

    /**
     * Returns the timeout waiting to acquire a lock. The timeout is specified in
     * seconds.
     * 
     * @return The timeout waiting to acquire a lock in seconds.
     */
    int getLockTimeout();

    /**
     * Sets the status of the current transaction to the given one.
     * 
     * @param status The status to set for this transaction.
     */
    void setStatus(final int status);

    /**
     * Returns the status of this transaction.
     * 
     * @return The status of this transaction.
     */
    int getStatus();

    /**
     * Indicates which lock this transaction is waiting for. When a transaction
     * attempts to acquire a lock it must indicate which lock it attempts to
     * acquire in order to perform dead-lock detection. This method is called by
     * {@link ObjectLock} before entering the temporary lock-acquire state.
     * 
     * @param lock The lock which this transaction attempts to acquire
     */
    void setWaitOnLock(final ObjectLock lock);

    /**
     * Returns the lock which this transaction attempts to acquire.
     * 
     * @return The lock which this transaction attempts to acquire
     */
    ObjectLock getWaitOnLock();

    /**
     * Return an open connection for the specified engine. Only one connection should
     * be created for a given engine in the same transaction.
     * 
     * @param engine The persistence engine.
     * @return An open connection.
     * @throws PersistenceException An error occured talking to the persistence engine.
     */
    Connection getConnection(final LockEngine engine) throws PersistenceException;

    /**
     * Returns meta-data related to the RDBMS used.
     * 
     * @param engine LockEngine instance used.
     * @return A DbMetaInfo instance describing various features of the underlying
     *         RDBMS.
     * @throws PersistenceException An error occured talking to the persistence engine.
     */
    DbMetaInfo getConnectionInfo(final LockEngine engine) throws PersistenceException;

    Object fetch(final ClassMolder molder,
            final Identity identity, final AccessMode suggestedAccessMode)
    throws PersistenceException;

    /**
     * Load an object for use within the transaction. Multiple access to the same
     * object within the transaction will return the same object instance (except
     * for read-only access).
     * <p>
     * This method is similar to {@link #fetch} except that it will load the object
     * only once within a transaction and always return the same instance.
     * <p>
     * If the object is loaded for read-only then no lock is acquired and updates
     * to the object are not reflected at commit time. If the object is loaded for
     * read-write then a read lock is acquired (unless timeout or deadlock detected)
     * and the object is stored at commit time. The object is then considered
     * persistent and may be deleted or upgraded to write lock. If the object is
     * loaded for exclusive access then a write lock is acquired and the object is
     * synchronized with the persistent copy.
     * <p>
     * Attempting to load the object twice in the same transaction, once with
     * exclusive lock and once with read-write lock will result in an exception.
     * 
     * @param identity The object's identity.
     * @param proposedObject The object to fetch (single instance per transaction).
     * @param suggestedAccessMode The access mode (see {@link AccessMode}) the values
     *        in persistent storage.
     * @return object being loaded.
     * @throws PersistenceException Timeout or deadlock occured attempting to acquire
     *         lock on object. The object was not found in persistent storage. An error
     *         reported by the persistence engine.
     */
    Object load(final Identity identity,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode) 
    throws PersistenceException;

    /**
     * Load an object for use within the transaction. Multiple access to the same
     * object within the transaction will return the same object instance (except
     * for read-only access).
     * <p>
     * In addition to {@link #load(Identity,ProposedEntity,AccessMode)}
     * a QueryResults can be specified.
     * 
     * @param identity The object's identity.
     * @param proposedObject The object to fetch (single instance per transaction).
     * @param suggestedAccessMode The access mode (see {@link AccessMode}) the values
     *        in persistent storage.
     * @param results The QueryResult that the data to be loaded from.
     * @return object being loaded.
     * @throws PersistenceException Timeout or deadlock occured attempting to acquire
     *         lock on object. The object was not found in persistent storage. An error
     *         reported by the persistence engine.
     */
    Object load(final Identity identity,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode, 
            final QueryResults results)
    throws PersistenceException;

    /**
     * Perform a query using the query mechanism with the specified access mode. The
     * query is performed in this transaction, and the returned query results can
     * only be used while this transaction is open. It is assumed that the query
     * mechanism is compatible with the persistence engine.
     * 
     * @param engine The persistence engine.
     * @param query A query against the persistence engine.
     * @param accessMode The access mode.
     * @param scrollable The db cursor mode.
     * @return A query result iterator
     * @throws PersistenceException An error reported by the persistence engine or
     *         an invalid query.
     */
    QueryResults query(final LockEngine engine, final PersistenceQuery query,
            final AccessMode accessMode, final boolean scrollable)
    throws PersistenceException;
    
    /**
     * Walk a data object tree starting from the specified object, and mark all
     * objects to be created.
     * 
     * @param molder The class persistence molder.
     * @param object The object to persist.
     * @param rootObjectOID The OID of the root object to start walking.
     * @throws PersistenceException An object with this identity already exists in
     *         persistent storage. The class is not persistent capable. An error
     *         reported by the persistence engine.
     */
    void markCreate(final ClassMolder molder,
            final Object object, final OID rootObjectOID)
    throws PersistenceException;

    /**
     * Creates a new object in persistent storage. The object will be persisted
     * only if the transaction commits. If an identity is provided then duplicate
     * identity check happens in this method, if no identity is provided then
     * duplicate identity check occurs when the transaction completes and the
     * object is not visible in this transaction.
     * 
     * @param molder The molder of the creating class.
     * @param object The object to persist.
     * @param depended The master object's OID if exist.
     * @throws PersistenceException An object with this identity already exists in
     *         persistent storage. The class is not persistent capable. An error
     *         reported by the persistence engine.
     */
    void create(final ClassMolder molder, final Object object, final OID depended) 
    throws PersistenceException;

    /**
     * Update a new object in persistent storage and returns the object's OID. The
     * object will be persisted only if the transaction commits. If an identity is
     * provided then duplicate identity check happens in this method, if no identity
     * is provided then duplicate identity check occurs when the transaction
     * completes and the object is not visible in this transaction.
     * <p>
     * Update will also mark objects to be created if the TIMESTAMP equals to
     * NO_TIMESTAMP.
     * 
     * @param molder The object's molder.
     * @param object The object to persist.
     * @param depended The master objects of the specified object to be created if
     *        exisit.
     * @return true if the object is marked to be created.
     * @throws PersistenceException An object with this identity already exists in
     *         persistent storage. The class is not persistent capable. Dirty checking
     *         mechanism may immediately report that the object was modified in the
     *         database during the long transaction. An error reported by the
     *         persistence engine.
     */
    boolean markUpdate(final ClassMolder molder,
            final Object object, final OID depended) 
    throws PersistenceException;
    
    /**
     * Update a new object in persistent storage and returns the object's OID. The
     * object will be persisted only if the transaction commits. If an identity is
     * provided then duplicate identity check happens in this method, if no identity
     * is provided then duplicate identity check occurs when the transaction
     * completes and the object is not visible in this transaction.
     * <p>
     * Update will also mark objects to be created if the TIMESTAMP equals to
     * NO_TIMESTAMP.
     * 
     * @param molder The object's molder.
     * @param object The object to persist.
     * @param depended The master objects of the specified object to be created if
     *        exisit.
     * @throws PersistenceException An object with this identity already exists in
     *         persistent storage. The class is not persistent capable. Dirty checking
     *         mechanism may immediately report that the object was modified in the
     *         database during the long transaction. An error reported by the persistence
     *         engine.
     */
    void update(final ClassMolder molder, final Object object,
            final OID depended) 
    throws PersistenceException;
    
    /**
     * Deletes the object from persistent storage. The deletion will take effect
     * only if the transaction is committed, but the object is no longer viewable
     * for the current transaction and locks for access from other transactions
     * will block until this transaction completes. A write lock is acquired in
     * order to assure the object can be deleted.
     * 
     * @param object The object to delete from persistent storage.
     * @throws PersistenceException The object has not been queried or created in this
     *         transaction. Timeout or deadlock occured attempting to acquire lock on
     *         object. An error reported by the persistence engine.
     */
    void delete(Object object) throws PersistenceException;
    
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
     * @param object The object to lock.
     * @param timeout Timeout waiting to acquire lock, specified in seconds, zero
     *        for no waiting, negative to use the default timeout for this transaction.
     * @throws PersistenceException The object has not been queried or created in this
     *         transaction. Timeout or deadlock occured attempting to acquire lock on
     *         object. An error reported by the persistence engine.
     */
    void writeLock(final Object object, final int timeout)
    throws PersistenceException;
    
    void markModified(final Object object, 
            final boolean updatePersist,
            final boolean updateCache);

    /**
     * Prepares the transaction prior to committing it. Indicates whether the
     * transaction is read-only (i.e. no modified objects), can commit, or an error
     * has occured and the transaction must be rolled back. This method performs
     * actual storage into the persistence storage. Multiple calls to this method
     * can be done, and do not release locks, allowing <tt>checkpoint</tt> to occur.
     * An IllegalStateException is thrown if transaction is not in the proper state
     * to perform this operation.
     * 
     * @return True if the transaction can commit, false if the transaction is
     *         read only
     * @throws TransactionAbortedException The transaction has been aborted due to
     *         inconsistency, duplicate object identity, error with the persistence
     *         engine or any other reason.
     */
    boolean prepare() throws TransactionAbortedException;
    
    /**
     * Commits all changes and closes the transaction releasing all locks on all
     * objects. All objects are now transient. Must be called after a call to
     * {@link #prepare} has returned successfully. Throws an IllegalStateException
     * when called without calling {@link #prepare} first.
     * 
     * @throws TransactionAbortedException The transaction has been aborted due to
     *         inconsistency, duplicate object identity, error with the persistence
     *         engine or any other reason.
     */
    void commit() throws TransactionAbortedException;
    
    /**
     * Rolls back all changes and closes the transaction releasing all locks on
     * all objects. All objects are now transient, if they were queried in this
     * transaction. This method may be called at any point during the transaction.
     * Throws an IllegalStateException if transaction is not in the proper state
     * to perform this operation.
     */
    void rollback();
    
    /**
     * Closes all Connections. Must be called before the end of the transaction
     * in EJB environment or after commit in standalone case. Throws an 
     * IllegalStateException if this method has been called after the end of the
     * transaction.
     * 
     * @throws TransactionAbortedException The transaction has been aborted due to
     *         inconsistency, duplicate object identity, error with the persistence
     *         engine or any other reason.
     */
    void close() throws TransactionAbortedException;
    
    /**
     * Expose an enumeration of the commited object entries to allow TxSynchronizable
     * to iterate through the objects.
     * 
     * @return Iterator of modifiable (read-write) object entries.
     */
    Iterator iterateReadWriteObjectsInTransaction();
    
    /**
     * Returns true if the object is marked as created in this transaction. Note
     * that this does not find objects in the 'transitional' state of creating.
     * Primarily intended to be used by tests.
     * 
     * @param object The object to test the state of in this transaction.
     * @return <code>true</code> if the object is marked as created within this
     *         transaction.
     */
    boolean isCreated(final Object object);

    /**
     * Retrieves the state of the object in this transaction. Specifically, in
     * this case, that the object requires a cache update.
     * 
     * @param object The object to test the state of in this transaction.
     * @return <code>true</code> if the object is recorded in this transaction with
     *         the requested state.
     */
    boolean isUpdateCacheNeeded(final Object object);

    /**
     * Retrieves the state of the object in this transaction. Specifically, in
     * this case, that the object requires a persistence update.
     * 
     * @param object The object to test the state of in this transaction.
     * @return <code>true</code> if the object is recorded in this transaction with
     *         the requested state.
     */
    boolean isUpdatePersistNeeded(final Object object);

    /**
     * Returns true if the object is persistent in this transaction.
     * 
     * @param object The object.
     * @return <code>true</code> if persistent in transaction.
     */
    boolean isPersistent(final Object object);

    /**
     * Returns true if the object is previously queried/loaded/update/create in
     * this transaction
     * 
     * @param object The object.
     * @return <code>true</code> if recorded in this transaction.
     */
    boolean isRecorded(final Object object);

    boolean isDepended(final OID master, final Object dependent);
    
    /**
     * Returns true if the transaction is open.
     * 
     * @return <code>true</code> if the transaction is open.
     */
    boolean isOpen();

    /**
     * Returns true if and only if the specified object is loaded or created in this
     * transaction and is deleted.
     * 
     * @param object The object to test the state of in this transaction.
     * @return <code>true</code> if the object is deleted.
     */
    boolean isDeleted(final Object object);

    boolean isDeletedByOID(final OID oid);

    /**
     * Check to see whether this transaction considers an object to have been marked 
     * read-only.
     * 
     * @param object The object to test for read-only status
     * @return <code>true</code> if the object is marked read-only in this transaction;
     *         otherwise, <code>false</code>. 
     */
    boolean isReadOnly(final Object object);

    boolean isCached(final ClassMolder molder, final Class cls, final Identity identity)
    throws PersistenceException;
    
    /**
     * Expire object from the cache.  Objects expired from the cache will be
     * read from persistent storage, as opposed to being read from the
     * cache, during subsequent load/query operations.
     *
     * @param molder The class persistence molder.
     * @param identity The object's identity.
     * @throws PersistenceException If identity is null or any problem that happens
     *         during expiration of cache values.
     */
    void expireCache(final ClassMolder molder, final Identity identity)
    throws PersistenceException;
    
    /**
     * Get the current application ClassLoader.
     * 
     * @return the current ClassLoader's instance. <code>null</code> if none
     *         has been provided
     */
    ClassLoader getClassLoader();

    Database getDatabase();
    
    /**
     * Returns true if the object given is locked.
     * 
     * @param cls Class instance of the object to be investigated.
     * @param identity Identity of the object to be investigated. 
     * @param lockEngine Current LcokEngine instance
     * @return True if the object in question is locked.
     */
    boolean isLocked(Class cls, Identity identity, LockEngine lockEngine);

    /**
     * Creates an OQL query based upon a named query as defined in the 
     * mapping file.
     *
     * @param molder Specific class molder.
     * @param name Name of the (named) query to create.
     * @return An OQL query
     * @throws QueryException If the named query can not be found
     */
    String getNamedQuery(ClassMolder molder, String name) throws QueryException;

}

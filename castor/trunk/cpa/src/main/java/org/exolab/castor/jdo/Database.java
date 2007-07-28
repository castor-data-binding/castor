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


package org.exolab.castor.jdo;


import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.PersistenceInfoGroup;
import org.exolab.castor.persist.spi.Identity;

/**
 * An open connection to the database. This object represents an open
 * connection to the database that can be used to perform transactional
 * operations on the database.
 * <p>
 * Database operations can only be performed in the context of a
 * transaction. Client applications should begin and commit a transaction
 * using the {@link #begin} and {@link #commit} methods. Server
 * applications should use implicit transaction demaraction by the
 * container or explicit transaction demarcation using <tt>javax.transaction.UserTransaction</tt>.
 * <p>
 * All objects queried and created during a transaction are persistent.
 * Changes to persistent objects will be stored in the database when
 * the transaction commits. Changes will not be stored if the transaction
 * is rolled back or fails to commit.
 * <p>
 * Once the transaction has committed or rolled back, all persistent
 * objects become transient. Opening a new transaction does not make
 * these objects persistent.
 * <p>
 * For example:
 * <pre>
 * Database     db;
 * Query        oql;
 * QueryResults results;
 * Product      prod;
 *
 * <font color="red">// Open a database and start a transaction</font>
 * db = jdo.getDatabase();
 * db.begin();
 * <font color="red">// Select all the products in a given group</font>
 * oql = db.getOQLQuery( "SELECT p FROM Product p WHERE group=$");
 * oql.bind( groupId );
 * results = oql.execute();
 * while ( results.hasMore() ) {
 *   <font color="red">// A 25% mark down for each product and mark as sale</font>
 *   prod = (Product) results.next();
 *   prod.markDown( 0.25 );
 *   prod.setOnSale( true );
 * }
 * <font color="red">// Commit all changes, close the database</font>
 * db.commit();
 * db.close();
 * </pre>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-03-16 16:04:24 -0700 (Thu, 16 Mar 2006) $
 * @see JDOManager#getDatabase()
 * @see Query
 */
public interface Database {
    /** Read only access. Used with queries and the {@link #load(Class,Object)} method to load
     *  objects as read-only.
     *  <br/>
     *  Read-only objects are not persistent and changes to these objects are not reflected in
     *  the database when the transaction commits. */
    AccessMode READONLY = AccessMode.ReadOnly;

    /** Read only access. Used with queries and the {@link #load(Class,Object)} method to load
     *  objects as read-only.
     *  <br>
     *  Read-only objects are not persistent and changes to these objects are not reflected in
     *  the database when the transaction commits.
     *  @deprecated Use READONLY instead. */
    AccessMode ReadOnly = AccessMode.ReadOnly;

    /** Shared access. Used with queries and the {@link #load(Class,Object)} method to load
     *  objects with shared access.
     *  <br/>
     *  Shared access allows the same record to be accessed by two concurrent transactions, each
     *  with it's own view (object).
     *  <br/>
     *  These objects acquire a read lock which escalated to a write lock when the transaction
     *  commits if the object has been modified. Dirty checking is enabled for all fields marked
     *  as such, and a cached copy is used to populate the object. */
    AccessMode SHARED = AccessMode.Shared;

    /** Shared access. Used with queries and the {@link #load(Class,Object)} method to load
     *  objects with shared access.
     *  <br/>
     *  Shared access allows the same record to be accessed by two concurrent transactions, each
     *  with it's own view (object).
     *  <br/>
     *  These objects acquire a read lock which escalated to a write lock when the transaction
     *  commits if the object has been modified. Dirty checking is enabled for all fields marked
     *  as such, and a cached copy is used to populate the object.
     *  @deprecated Use SHARED instead. */
    AccessMode Shared = AccessMode.Shared;

    /** Exclusive access. Used with queries and the {@link #load(Class,Object)} method to load
     *  objects with exclusive access.
     *  <br/>
     *  Exclusive access prevents two concurrent transactions from accessing the same record. In
     *  exclusive mode objects acquire a write lock, and concurrent transactions will block until
     *  the lock is released at commit time.
     *  <br/>
     *  Dirty checking is enabled for all fields marked as such. When an object is first loaded
     *  in the transaction, it will be synchronized with the database and not populated from the
     *  cache. */
    AccessMode EXCLUSIVE = AccessMode.Exclusive;

    /** Exclusive access. Used with queries and the {@link #load(Class,Object)} method to load
     *  objects with exclusive access.
     *  <br/>
     *  Exclusive access prevents two concurrent transactions from accessing the same record. In
     *  exclusive mode objects acquire a write lock, and concurrent transactions will block until
     *  the lock is released at commit time.
     *  <br/>
     *  Dirty checking is enabled for all fields marked as such. When an object is first loaded
     *  in the transaction, it will be synchronized with the database and not populated from the
     *  cache.
     *  @deprecated Use EXCLUSIVE instead. */
    AccessMode Exclusive = AccessMode.Exclusive;

    /** Database lock access. Used with queries and the {@link #load(Class,Object)} method to
     *  load objects with a database lock.
     *  <br/>
     *  Database lock prevents two concurrent transactions from accessing the same record either
     *  through Castor or direct database access by acquiring a write lock in the select statement.
     *  Concurrent transactions will block until the lock is released at commit time.
     *  <br/>
     *  When an object is first loaded in the transaction, it will be synchronized with the
     *  database and not populated from the cache. Dirty checking is not required. */
    AccessMode DBLOCKED = AccessMode.DbLocked;

    /** Database lock access. Used with queries and the {@link #load(Class,Object)} method to
     *  load objects with a database lock.
     *  <br/>
     *  Database lock prevents two concurrent transactions from accessing the same record either
     *  through Castor or direct database access by acquiring a write lock in the select statement.
     *  Concurrent transactions will block until the lock is released at commit time.
     *  <br/>
     *  When an object is first loaded in the transaction, it will be synchronized with the
     *  database and not populated from the cache. Dirty checking is not required.
     *  @deprecated Use DBLOCKED instead. */
    AccessMode DbLocked = AccessMode.DbLocked;

    /**
     * Creates an OQL query with no statement. {@link OQLQuery#create}
     * must be called before the query can be executed.
     *
     * @return An OQL query
     */
    OQLQuery getOQLQuery();

    /**
     * Creates an OQL query from the supplied statement.
     * @param oql An OQL query statement
     * @return An OQL query
     * @throws PersistenceException
     */
    OQLQuery getOQLQuery( String oql )
        throws PersistenceException;

    /**
     * Creates an empty query. The query must be created before
     * it can be executed.
     *
     * @return A query
     */
    Query getQuery();

    /**
     * Creates an OQL query based upon a named query as defined in the 
     * mapping file. {@link OQLQuery#create}
     *
     * @param name Name of the (named) query to create.
     * @return An OQL query
     * @throws PersistenceException 
     */
    OQLQuery getNamedQuery(String name) throws PersistenceException;

    PersistenceInfoGroup getScope();

    /**                              
     * Return the name of the database
     * @return The database name.
     */                               
    String getDatabaseName(); 

    /**
     * Load an object of the specified type and given identity.
     * Once loaded the object is persistent. Calling this method with
     * the same identity in the same transaction will return the same
     * object. This method is equivalent to a query that returns a
     * single object. If the identity spans on more than one field, all
     * of the identity fields can be wrapped in a Complex object.
     * 
     * @param type The object's type
     * @param identity The object's identity
     * @return The object instance.
     * @throws ObjectNotFoundException No object of the given type and
     *  identity was found in persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire a lock on the object
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    Object load(final Class type, final Object identity)
    throws TransactionNotInProgressException, ObjectNotFoundException,
           LockNotGrantedException, PersistenceException;

    /**
     * <p>
     * Load an object of the specified type and given identity.
     * Once loaded the object is persistent. Calling this method with
     * the same identity in the same transaction will return the same
     * object. This method is equivalent to a query that returns a
     * single object.
     *
     * @param type The object's type
     * @param identity The object's identity
     * @param mode The access mode
     * @return The object instance.
     * @throws ObjectNotFoundException No object of the given type and
     *  identity was found in persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire a lock on the object
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    Object load(final Class type, final Object identity, final AccessMode mode) 
    throws TransactionNotInProgressException, ObjectNotFoundException,
           LockNotGrantedException, PersistenceException;

    /**
     * <p>
     * Load an object of the specified type and given identity into 
     * a given instance of object.
     *
     * Once loaded the object is persistent. Calling this method with
     * the same identity in the same transaction will return the same
     * object. This method is equivalent to a query that returns a
     * single object. If the identity spans on more than one field, all
     * of the identity fields can be wrapped in a Complex object.
     *
     * @param type The object's type
     * @param identity The object's identity
     * @param object The object instance to be loaded into
     * @return The object instance.
     * @throws ObjectNotFoundException No object of the given type and
     *  identity was found in persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire a lock on the object
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    Object load(final Class type, final Object identity, final Object object)
    throws ObjectNotFoundException, LockNotGrantedException,
           TransactionNotInProgressException, PersistenceException;

    /**
     * Creates a new object in persistent storage. The object will be
     * persisted only if the transaction commits.
     * <p>
     * If the object has an identity then duplicate identity check happens
     * in this method, and the object is visible to queries in this
     * transaction. If the identity is null, duplicate identity check
     * occurs when the transaction completes and the object is not
     * visible to queries until the transaction commits.
     *
     * @param object The object to create
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    void create(Object object)
    throws ClassNotPersistenceCapableException, DuplicateIdentityException,
           TransactionNotInProgressException, PersistenceException;

    /**
     * Removes the object from persistent storage. The deletion will
     * take effect only if the transaction is committed, but the
     * object is no longer visible to queries in the current transaction
     * and locks for access from other transactions will block until
     * this transaction completes.
     *
     * @param object The object to remove
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws ObjectNotPersistentException The object has not been
     *  queried or created in this transaction
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire a lock on the object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    void remove(Object object)
    throws ObjectNotPersistentException, LockNotGrantedException, 
           TransactionNotInProgressException, PersistenceException;

    /**
     * Update a data object which is queried/loaded/created in <b>another
     * transaction</b>. This method is used only for long transaction 
     * support. Calling this method for data object queried/loaded/created
     * in the same transaction results in Exception.
     * <p>
     * For example, the data object may be sent to a client application and 
     * dispayed to a user. After that the objects is being modified in the
     * client application, the object returns back and is update to the 
     * database in the second transaction. 
     * <p>
     * See <a href="http://castor.exolab.org/long-transact.html">Long 
     * Transaction</a> on Castor website.
     *
     * @param object The object to create
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    void update(Object object)
    throws ClassNotPersistenceCapableException,
           TransactionNotInProgressException, PersistenceException;

    /**
     * Acquire a soft write lock on the object. Read locks are implicitly
     * available when the object is queried. A write lock is only
     * granted for objects that are created or deleted or for objects
     * loaded in <tt>exclusive</t> mode - this method can obtain such a
     * lock explicitly.
     * <p>
     * A soft lock is acquired in memory, not in the database. To acquire
     * a lock in the database, use the <tt>locked</tt> access mode.
     * <p>
     * If the object already has a write lock in this
     * transaction or a read lock in this transaction but no read lock
     * in any other transaction, a write lock is obtained. If this
     * object has a read lock in any other transaction this method
     * will block until the other transaction will release its lock.
     * If the timeout has elapsed or a deadlock has been detected,
     * an exception will be thrown but the current lock will be retained.
     *
     * @param object The object to lock
     * @throws TransactionNotInProgressException Method called while
     *   transaction is not in progress
     * @throws ObjectNotPersistentException The object has not been
     *  queried or created in this transaction
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire a lock on the object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    void lock(Object object)
    throws LockNotGrantedException, ObjectNotPersistentException,
           TransactionNotInProgressException,  PersistenceException;

    /**
     * Begin a new transaction. A transaction must be open in order
     * to query and persist objects.
     *
     * @throws PersistenceException A transaction is already open on
     *  this database, or an error reported by the persistence engine
     */
    void begin() throws PersistenceException;

    /**
     * Return if the current transaction is set to autoStore, it there is
     * transaction active. If there is no active transaction, return if 
     * the next transaction will be set to autoStore.
     * <p>
     * If autoStore is set on. AutoStore will create
     * all reachable object if the object is not loaded from the
     * transaction. If it is turn off, only dependent object will 
     * be created automatically.
     * @return True if the current transaction is set to 'autoStore'.
     */
    boolean isAutoStore();

    /**
     * True if autoStore is set on. 
     * <p>
     * This method should be called before begin().
     * <p>
     * If autoStore is set, and db.create( theDataObject ) is called,
     * Castor will create theDataObject, and create each object
     * that does not exist in the transaction and reachable from
     * theDataObject.
     * <p>
     * If db.update( theDataObject ), and theDataObject is
     * loaded/queuied/created in a previous transaction, Castor
     * will let theDataObject, and all reachable object from
     * theDataObject, participate in the current transaction.
     * <p>
     * If autoStore is not set, Castor will only create/update/store
     * dependent object, and related objects must be created/update 
     * explicitly.
     * @param autoStore True if this feature should be enabled.
     */
    void setAutoStore(boolean autoStore);

    /**
     * Commits and closes the transaction. All changes made to persistent
     * objects during the transaction are made persistent; objects created 
     * during the transaction are made durable; and, objects removed during 
     * the transaction are removed from the database.
     * <p>
     * In other words, any modifications to any data objects which are 
     * queried/loaded/created/update to this database is automatically 
     * stored to the database and visible to subsequence transactions. 
     * (ie. update is solely used for long transaction support and should
     * not be called for any data object queried/loaded/created in the 
     * this transaction.)
     * <p>
     * If the transaction cannot commit, the entire transaction rolls
     * back and a {@link TransactionAbortedException} exception is
     * thrown.
     * <p>
     * After this method returns, the transaction is closed and all
     * persistent objects are transient. Using {@link #begin} to open a
     * new transaction will not restore objects to their persistent
     * stage.
     *
     * @throws TransactionNotInProgressException Method called while
     *  transaction is not in progress
     * @throws TransactionAbortedException The transaction cannot
     *  commit and has been rolled back
     */
    void commit() throws TransactionNotInProgressException, TransactionAbortedException;

    /**
     * Rolls back and closes the transaction. All changes made to
     * persistent objects during the transaction are lost, objects
     * created during the transaction are not made durable and objects
     * removed during the transaction continue to exist.
     *
     * @throws TransactionNotInProgressException Method called while
     *  transaction is not in progress
     */
    void rollback() throws TransactionNotInProgressException;

    /**
     * Returns true if a transaction is currently active.
     *
     * @return True if a transaction is active
     */
    boolean isActive();

    /**
     * Returns true if the database is closed.
     *
     * @return True if the database is closed
     */
    boolean isClosed();

    /**
     * Returns true if the specified object is currently locked.
     * 
     * @param cls Class instance.
     * @param identity Object identity.
     * @return True if the object specified is locked; false otherwise.
     */
    boolean isLocked (Class cls, Object identity) throws PersistenceException;
    
    /**
     * Closes the database. If a client transaction is in progress the
     * transaction will be rolled back and an exception thrown.
     * If an app-server transaction is in progress, the transaction
     * will commit/rollback when triggered by the application server.  
     *
     * @throws PersistenceException An error occured while
     *         attempting to close the database
     */
    void close() throws PersistenceException;

    /**
     * Returns true if the object is persistent. An object is persistent
     * if it was created or queried in this transaction. If the object
     * was created or queried in another transaction, or there is no
     * open transaction, this method returns null.
     *
     * @param object The object
     * @return True if persistent in this transaction
     */
    boolean isPersistent(Object object);

    /**
     * Returns the object's identity. The identity will be determined by calling the
     * getters of the fields defined as identities in the mapping. If a mapping for
     * the objects class could not be found a ClassNotPersistenceCapableException
     * will be thrown. Null is only returned if the objects identity is null. It is
     * not required to have an active transaction when using this method.
     * <p>
     * <b>Note:</b> Prior to 0.9.9.1 release of castor the identity could only be
     * determined if the object took part in the transaction. If this was not the case,
     * the previous implementation also returned null.
     * 
     * @param object The object.
     * @return The object's identity, or null.
     * @throws PersistenceException The class is not persistent capable.
     */
    Identity getIdentity(Object object) throws PersistenceException;
    
    /** 
     * Returns the current ClassLoader if one has been set for this Database instance.
     * 
     * @return ClassLoader the current ClassLoader instance, <code>null</code> if no 
     *         ClassLoader's instance has been explicitely set.
     */
    ClassLoader getClassLoader ();

    /**
     * Get's the CacheManager instance.
     * Call getCacheManager for every Database-instances.
     * 
     * @return the CacheManager-instance.
     */
    CacheManager getCacheManager();

    /**
     * Gets the underlying JDBC connection.
     * 
     * This is for <b>advanced</b> use only. Please make sure that you <b>never</b>
     * close this Connection instance, as it will be closed by Castor.
     * 
     * @return the underlying JDBC connection, if present; otherwise null 
     * @throws PersistenceException If the underlying JDBC connection cannot be obtained.
     */
    java.sql.Connection getJdbcConnection() throws PersistenceException;
}


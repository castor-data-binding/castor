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
import javax.transaction.xa.Xid;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.Messages;


/**
 * An interface to a persistence engine. The persistence engine
 * implements object caching, transaction isolatio and locking, and
 * rollback. The persistence APIs interact with this interface and
 * {@link TransactionContext}. A persistence engine is obtained from
 * {@link PersistenceEngineFactory} given the SPI factory and list
 * of object mappings that must be supported.
 * <p>
 * All operations against the persistence engine must be carried in
 * the context of a transaction. For efficiency with caching and
 * locking, a single persistence engine should be implemented on top
 * of a given datastore.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface PersistenceEngine
{


    /**
     * Returns the class handler for this Java type. The type
     * must be known to this engine.
     *
     * @param type The Java type
     * @return The class handler
     */
    public ClassHandler getClassHandler( Class type );


    /**
     * Returns the persistence implementation for this Java type.
     *
     * @param type The Java type
     * @return The persistence implementation
     */
    public Persistence getPersistence( Class type );


    /**
     * Sets the log writer used to trace persistence operations.
     *
     * @param logWriter The log writer, or null
     */
    // public void setLogWriter( PrintWriter logWriter );


    /**
     * Returns the log writer used to trace persistence operations.
     *
     * @return The log writer, or null
     */
    // public PrintWriter getLogWriter();


    /**
     * Loads an object of the specified type and identity from
     * persistent storage. In exclusive mode the object is always
     * loaded and a write lock is obtained on the object, preventing
     * concurrent updates. In non-exclusive mode the object is either
     * loaded or obtained from the cache with a read lock. The object's
     * OID is always returned, this OID must be used in subsequent
     * operations on the object.
     *
     * @param tx The transaction context
     * @param type The type of the object to load
     * @param identity The identity of the object to load
     * @param accessMode The access mode (null equals shared)
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public OID load( TransactionContext tx, Class type, Object identity,
                     AccessMode accessMode, int timeout )
        throws ObjectNotFoundException, LockNotGrantedException,
               PersistenceException, ClassNotPersistenceCapableException;
    

    /**
     * Loads an object of the specified type and identity from the
     * query results. In exclusive mode the object is always loaded
     * and a write lock is obtained on the object, preventing
     * concurrent updates. In non-exclusive mode the object is either
     * loaded or obtained from the cache with a read lock. The object's
     * OID is always returned, this OID must be used in subsequent
     * operations on the object.
     *
     * @param tx The transaction context
     * @param query The query persistence engine
     * @param identity The identity of the object to load
     * @param accessMode The access mode (null equals shared)
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public OID fetch( TransactionContext tx, PersistenceQuery query, Object identity,
                      AccessMode accessMode, int timeout )
        throws ObjectNotFoundException, LockNotGrantedException,
               PersistenceException;


    /**
     * Creates a new object in this engine. The object must not be
     * persistent and must have a unique identity within this engine.
     * If the identity is specified the object is created in
     * persistent storage immediately with the identity. If the
     * identity is not specified, the object is created only when the
     * transaction commits. The object's OID is returned. The OID is
     * guaranteed to be unique for this engine even if no identity was
     * specified.
     *
     * @param tx The transaction context
     * @param obj The newly created object
     * @param identity The identity of the object, or null
     * @return The object's OID
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public OID create( TransactionContext tx, Object obj, Object identity )
        throws DuplicateIdentityException, PersistenceException,
               ClassNotPersistenceCapableException;
    

    /**
     * Updates an existing object to this engine. The object must not be
     * persistent and must not have the identity of a persistent object.
     * The object's OID is returned. The OID is guaranteed to be unique
     * for this engine even if no identity was specified.
     * If the object implements TimeStampable interface, verify 
     * the object's timestamp.
     *
     * @param tx The transaction context
     * @param type The type of the object to load
     * @param object The object
     * @param accessMode The desired access mode
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     * @throws ObjectModifiedException Dirty checking mechanism may immediately
     *  report that the object was modified in the database during the long 
     *  transaction.
     */
    public OID update( TransactionContext tx, Class type, Object object,
                       AccessMode accessMode, int timeout )
        throws ObjectNotFoundException, LockNotGrantedException, ObjectModifiedException,
               PersistenceException, ClassNotPersistenceCapableException;


    /**
     * Called at transaction commit time to delete the object. Object
     * deletion occurs in three distinct steps:
     * <ul>
     * <li>A write lock is obtained on the object to assure it can be
     *     deleted and the object is marked for deletion in the
     *     transaction context
     * <li>As part of transaction preparation the object is deleted
     *     from persistent storage using this method
     * <li>The object is removed from the cache when the transaction
     *     completes with a call to {@link #forgetObject}
     * </ul>
     *
     * @param tx The transaction context
     * @param type The object type
     * @param identity The object's identity
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void delete( TransactionContext tx, Class type, Object identity )
        throws PersistenceException;
    

    /**
     * Called at transaction commit to store an object that has been
     * loaded during the transaction. If the object has been created
     * in this transaction but without an identity, the object will
     * be created in persistent storage. Otherwise the object will be
     * stored and dirty checking might occur in order to determine
     * whether the object is valid. The object's OID might change
     * during this process, and the new OID will be returned. If the
     * object was not stored (not modified), null is returned.
     *
     * @param tx The transaction context
     * @param obj The object to store
     * @param identity The object's identity
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     * @throws ObjectModifiedException The object has been modified in
     *  persistent storage since it was loaded, the memory image is
     *  no longer valid
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public OID store( TransactionContext tx, Object obj,
                      Object identity, int timeout )
        throws LockNotGrantedException, ObjectDeletedException,
               ObjectModifiedException, DuplicateIdentityException,
               PersistenceException;
    

    /**
     * Acquire a write lock on the object. A write lock assures that
     * the object exists and can be stored/deleted when the
     * transaction commits. It prevents any concurrent updates to the
     * object from this point on. However, it does not guarantee that
     * the object has not been modified in the course of the
     * transaction. For that the object must be loaded with exclusive
     * access.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void writeLock( TransactionContext tx, OID oid, int timeout )
        throws LockNotGrantedException, ObjectDeletedException, PersistenceException;
    

    /**
     * Acquire a soft lock on the object. A soft lock is acquired in
     * memory but not in the database. It prevents any concurrent updates
     * to the object from this point on. However, it does not guarantee that
     * the object has not been modified in the course of the transaction or
     * exists in the persistent storage. For that the object must be loaded
     * with exclusive access.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     */
    public void softLock( TransactionContext tx, OID oid, int timeout )
        throws LockNotGrantedException, ObjectDeletedException;


    /**
     * Obtain a copy of the cached object give the object's OID. The
     * cached object is copied into the supplied object without
     * affecting the locks. This method is generally called after a
     * successful return from {@link #load}, the transaction is known
     * to have a read lock on the object.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param obj The object into which to copy
     * @throws PersistenceException An error reported by the
     *  persistence engine obtaining a dependent object
     */
    public void copyObject( TransactionContext tx, OID oid, Object obj, AccessMode accessMode )
        throws PersistenceException;


    /**
     * Update the cached object with changes done to its copy. The
     * supplied object is copied into the cached object using a write
     * lock. This method is generally called after a successful return
     * from {@link #store} and is assumed to have obtained a write
     * lock.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param obj The object to copy from
     */
    public void updateObject( TransactionContext tx, OID oid, Object obj );


    /**
     * Called at transaction commit or rollback to release all locks
     * held on the object. Must be called for all objects that were
     * queried but not created within the transaction.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void releaseLock( TransactionContext tx, OID oid );


    /**
     * Called to release lock held on the object and all its dependents.
     * Is used to provide the ReadOnly mode.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void releaseLockWithDependent( TransactionContext tx, OID oid );


    /**
     * Called at transaction commit or rollback to forget an object
     * and release its locks. Must be called for all objects that were
     * created when the transaction aborts, and for all objects that
     * were deleted when the transaction completes. The transaction is
     * known to have a write lock on the object.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void forgetObject( TransactionContext tx, OID oid );


    /**
     * Returns an association between Xid and transactions contexts.
     * The association is shared between all transactions open against
     * this cache engine through the XAResource interface.
     */
    public Hashtable getXATransactions();


}



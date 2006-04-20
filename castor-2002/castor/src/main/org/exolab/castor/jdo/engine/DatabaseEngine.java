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


import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;
import org.odmg.ObjectNotPersistentException;
import org.odmg.LockNotGrantedException;
import org.odmg.ClassNotPersistenceCapableException;
import org.odmg.ObjectDeletedException;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.exolab.castor.jdo.DuplicatePrimaryKeyException;
import org.exolab.castor.jdo.ODMGSQLException;
import org.exolab.castor.jdo.desc.ObjectDesc;


/**
 * The SQL engine performs persistence of one object type against one
 * SQL database. It can only persist simple objects and extended
 * relationships. An SQL engine is created for each object type
 * represented by a database. When persisting, it requires a physical
 * connection that maps to the SQL database and the transaction
 * running on that database, through the {@link ConnectionProvider}
 * interface.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class DatabaseEngine
{


    /**
     * Timeout waiting to acquire lock. Specified in milliseconds and defaults
     * to 10 seconds.
     */
    private int       _lockTimeout = 10000;


    /**
     * Mapping of type information to object types. The object's class is used
     * as the key and {@link TypeInfo} is the value. {@link TypeInfo} provides
     * sufficient information to persist the object, manipulated it in memory
     * and invoke the object's interceptor.
     */
    private Hashtable _typeInfo = new Hashtable();


    /**
     * Mapping of OIDs to objects. The object is used as the key, and
     * {@link OID} is the value.
     */
    private Hashtable  _oids = new Hashtable();


    /**
     * Mapping of object locks to OIDs. The {@link OID} is used as the
     * key, and {@link ObjectLock} is the value.
     */
    private Hashtable _locks = new Hashtable();


    /**
     * Database source that applies to this engine.
     */
    private DatabaseSource _dbs;


    /**
     * Collection of all database engines with the database source
     * used as the key.
     */
    private static Hashtable  _dbEngines = new Hashtable();

    
    private DatabaseEngine( DatabaseSource dbs, PrintWriter logWriter )
    {
	Enumeration  enum;
	ObjectDesc   objDesc;

	if ( dbs == null )
	    throw new NullPointerException( "Argument 'dbs' is null" );
	_dbs = dbs;
	enum = _dbs.getMappingTable().listDescriptors();
	while ( enum.hasMoreElements() ) {
	    objDesc = (ObjectDesc) enum.nextElement();
	    _typeInfo.put( objDesc.getObjectClass(),
			   new TypeInfo( new SQLEngine( objDesc, logWriter ), objDesc, null ) );
	}
    }


    public String getName()
    {
	return _dbs.getName();
    }


    static DatabaseEngine getDatabaseEngine( DatabaseSource dbs, PrintWriter logWriter )
    {
	DatabaseEngine dbEngine;

	synchronized ( _dbEngines ) {
	    dbEngine = (DatabaseEngine) _dbEngines.get( dbs );
	    if ( dbEngine == null ) {
		dbEngine = new DatabaseEngine( dbs, logWriter );
		_dbEngines.put( dbs, dbEngine );
	    }
	    return dbEngine;
	}
    }


    public static DatabaseEngine getDatabaseEngine( Object obj )
    {
	Enumeration    enum;
	DatabaseEngine dbEngine;
	OID            oid;

	enum = _dbEngines.elements();
	while ( enum.hasMoreElements() ) {
	    dbEngine = (DatabaseEngine) enum.nextElement();
	    oid = dbEngine.getOID( obj );
	    if ( oid != null )
		return dbEngine;
	}
	return null;
    }


    public SQLEngine getSQLEngine( Class type )
    {
	TypeInfo typeInfo;

	typeInfo = (TypeInfo) _typeInfo.get( type );
	return typeInfo.engine;
    }


    public static DatabaseEngine getDatabaseEngine( Class type )
    {
	Enumeration    enum;
	DatabaseEngine dbEngine;

	enum = _dbEngines.elements();
	while ( enum.hasMoreElements() ) {
	    dbEngine = (DatabaseEngine) enum.nextElement();
	    if ( dbEngine._typeInfo.get( type ) != null )
		return dbEngine;
	}
	return null;
    }


    public static OID getObjectOID( Object obj )
    {
	Enumeration    enum;
	DatabaseEngine dbEngine;
	OID            oid;

	enum = _dbEngines.elements();
	while ( enum.hasMoreElements() ) {
	    dbEngine = (DatabaseEngine) enum.nextElement();
	    oid = dbEngine.getOID( obj );
	    if ( oid != null )
		return oid;
	}
	return null;
    }


    public static OID createObjectOID( Object obj )
	throws ClassNotPersistenceCapableException
    {
	Enumeration    enum;
	DatabaseEngine dbEngine;
	TypeInfo       typeInfo;

	enum = _dbEngines.elements();
	while ( enum.hasMoreElements() ) {
	    dbEngine = (DatabaseEngine) enum.nextElement();
	    typeInfo = (TypeInfo) dbEngine._typeInfo.get( obj.getClass() );
	    if ( typeInfo != null ) {
		if ( typeInfo.objDesc.getPrimaryKeyField() == null )
		    return new OID( dbEngine, typeInfo.objDesc, null );
		else
		    return new OID( dbEngine, typeInfo.objDesc,
				    typeInfo.objDesc.getPrimaryKeyField().getValue( obj ) );
	    }
	}
	throw new ClassNotPersistenceCapableExceptionImpl( obj.getClass() );
    }


    /**
    /**
     * Loads an object and return's its OID. The object is loaded from the
     * database based on its type and primary key and its OID is returned.
     * Normally a read lock is obtained on the object. In exclusive mode
     * a write lock is obtained on the object, and if the object has existed
     * in memory before it is synchronized with the database.
     *
     * @param type The type of object to load
     * @param primKey The primary key to use
     * @param tx The transaction context
     * @param exclusive Ture if database open in exclusive mode
     * @return The object's OID, or null if the object was not found
     * @throws ODMGException An error occured with the persistence engine
     * @throws LockNotGrantedException Timeout or deadlock occured waiting
     *   to obtain lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *   persistent storage
     */
    OID load( Class type, Object primKey, TransactionContext tx, boolean exclusive )
	throws ODMGException, LockNotGrantedException, ObjectDeletedException
    {
	Object     obj;
	OID        oid;
	ObjectLock lock;
	TypeInfo   typeInfo;

	typeInfo = (TypeInfo) _typeInfo.get( type );
	// Must prevent concurrent attempt to retrieve the same object
	// Best way to do that is through the type
	synchronized ( type ) {
	    // Create an OID to represent the object and see if we
	    // have a lock (i.e. object is cached).
	    oid = new OID( this, typeInfo.objDesc, primKey );
	    lock = getLock( oid );
	    if ( lock == null ) {
		// Object has not been loaded yet, load the object now
		// and acquire lock on it (write in exclusive mode)
		try {
		    obj = typeInfo.objDesc.createNew();
		} catch ( Exception except ) {
		    // This should never happen, object creation has been
		    // tested when creating the mapping
		    throw new ODMGRuntimeException( except.toString() );
		}

		// Object not found, return null
		if ( ! typeInfo.engine.load( tx.getConnection( this ), obj, primKey ) ) {
		    return null;
		}

		if ( typeInfo.interceptor != null ) {
		    typeInfo.interceptor.created( obj );
		    typeInfo.interceptor.loaded( obj );
		}

		lock = new ObjectLock( obj );
		try {
		    lock.acquire( tx, exclusive, 0 );
		} catch ( Exception except ) {
		    // This should never happen
		}
		setLock( oid, lock );
		setOID( obj, oid );
	    } else {
		// Object has been loaded before, must acquire lock
		// on it (write in exclusive mode)
		try {
		    tx.setWaitOnLock( lock );
		    obj = lock.acquire( tx, exclusive, _lockTimeout );
		} finally {
		    tx.setWaitOnLock( null );
		}
		// In exclusive mode must synchronize with the databse,
		// if an error occurs the object is immediately deleted
		// from memory
		if ( exclusive ) {
		    try {
			if ( ! typeInfo.engine.load( tx.getConnection( this ),
						     obj, primKey ) ) {
			    // Object not found a second time, delete it from
			    // memory and return null;
			    lock.delete( tx );
			    removeLock( oid );
			    removeOID( obj );
			    if ( typeInfo.interceptor != null )
				typeInfo.interceptor.forget( obj );
			    return null;
			}
 			if ( typeInfo.interceptor != null )
			    typeInfo.interceptor.loaded( obj );
		    } catch ( ODMGException except ) {
			lock.delete( tx );
			removeLock( oid );
			removeOID( obj );
			if ( typeInfo.interceptor != null )
			    typeInfo.interceptor.forget( obj );
			throw except;
		    }
		}
	    }
	}
	return oid;
    } 


    /**
     * Creates a new object in this engine. The object must not have been
     * persisted before. If the object has a primary key, that primary key
     * is used to create a copy of the object in the persistence engine and
     * an {@link OID} with that primary key is returned. If the object has
     * no primary key, a unique {@link OID} is created, the object is not
     * created in the persistence engine until the transaction commits and
     * the object cannot be queried inside the transaction.
     *
     * @param obj The object to create
     * @param tx The transaction context
     * @throws ODMGRuntimeException The object is already persisted or an error
     *   occured talking to the persistence engine
     * @throws DuplicatePrimaryKeyExceptionImpl An object with the same
     *   primary key already exists in this transaction or outside it
     * @throws ClassNotPersistentCapableException Persistence not
     *  supported for this class
     */
    OID create( Object obj, TransactionContext tx )
	throws ODMGRuntimeException, DuplicatePrimaryKeyException,
	       ClassNotPersistenceCapableException
    {
	OID        oid;
	ObjectLock lock;
	TypeInfo   typeInfo;

	typeInfo = (TypeInfo) _typeInfo.get( obj.getClass() );
	synchronized ( tx ) {
	    if ( typeInfo == null )
		throw new ClassNotPersistenceCapableExceptionImpl( obj.getClass() );
	    oid = new OID( this, typeInfo.objDesc, null );
	    // Make sure object is not persistent. If object has been created,
	    // deleted or retrieve, this is an error.
	    if ( getObjectOID( obj ) != null  )
		throw new ODMGRuntimeException( "Attempt to persist an object that is already persisted" );

	    // If the object has a primary key, perform duplicate primary
	    // key check at this point and report duplicates.
	    if ( oid.getPrimaryKey() != null ) {
		// If we can grab a lock for the object, then a duplicate primary
		// key exists.
		if ( getLock( oid ) != null )
		    throw new DuplicatePrimaryKeyException( obj.getClass(), oid.getPrimaryKey() );
		// Create the object in the database, preventing concurrent
		// object creation and further complaining about duplicate key.
		typeInfo.engine.create( tx.getConnection( this ), obj, oid.getPrimaryKey() );
	    }
		
	    // Record the object as created in this database and acquire
	    // write lock on the object. The object will only be retrieved
	    // by this transaction until the lock is released on commit.
	    lock = new ObjectLock( obj );
	    try {
		lock.acquire( tx, true, 0 );
	    } catch ( Exception except ) {
		// This should never happen
	    }
	    setLock( oid, lock );
	}
	return oid;
    }


    /**
     * Acquires a write lock on the object. The object must have been
     * created or queried in this transaction (hence have a read lock).
     * The object can only be deleted/stored after a lock has been
     * obtained.
     * 
     * @param obj The object to lock
     * @param tx The transaction context
     * @throws ObjectNotPersistentException The object is not persistent
     *   in this database
     * @throws ODMGRuntimeException The object has not been queried/created in
     *   this transaction
     * @throws LockNotGrantedException Attempt to acquire the lock
     *   timed out or a deadlock has been detected
     * @throws ObjectDeletedException The object has been deleted
     */
    void writeLock( Object obj, TransactionContext tx )
	throws ODMGRuntimeException, LockNotGrantedException
    {
	OID        oid;
	ObjectLock lock;
	TypeInfo   typeInfo;

	typeInfo = (TypeInfo) _typeInfo.get( obj.getClass() );
	synchronized ( tx ) {
	    oid = getOID( obj );
	    // If we do not know about the object in this engine, the
	    // object is not persistent. An object cannot be deleted
	    // if it was not created/deleted from a given database.
	    if ( oid == null )
		throw new ObjectNotPersistentExceptionImpl( obj );
	    lock = getLock( oid );

	    // If this transaction has no read/write lock on the object,
	    // the object has not been queried/retrieved in this transaction.
	    if ( ! lock.hasLock( tx, false ) )
		throw new ODMGRuntimeException( "Object has not been queried/created in this transaction" );

	    synchronized ( tx ) {
		// Must acquire write lock on the object in order to delete
		// it. Will complain if timeout/deadlock occurs.
		try {
		    tx.setWaitOnLock( lock );
		    lock.acquire( tx, true, _lockTimeout );
		} finally {
		    tx.setWaitOnLock( null );
		}
		// Attempt to obtain a lock on the database. If this attempt
		// fails, release the lock and report the exception.
		try {
		    typeInfo.engine.writeLock( tx.getConnection( this ),
					       obj, oid.getPrimaryKey() );
		} catch ( ODMGSQLException except ) {
		    lock.release( tx );
		    throw new LockNotGrantedException( "Persistence engine reported:" +
						       except.toString() );
		}
	    }
	}
    }


    /**
     * Deletes the object. The object must have been write locked
     * prior to this call. The object is deleted from the persistence
     * engine, but not removed from memory. Called when the transaction
     * commits, must call {@link #forgetObject} afterwards.
     *
     * @param oid The object OID
     * @param tx The transaction context
     * @throws ODMGException An error occured with the persistence
     *   engine
     */
    void delete( OID oid, TransactionContext tx )
	throws ODMGException
    {
	ObjectLock lock;
	Object     obj;
	TypeInfo   typeInfo;

	synchronized ( tx ) {
	    lock = getLock( oid );
	    // If this transaction has no write lock on the object,
	    // something went foul.
	    obj = lock.acquire( tx, true, 0 );
	    typeInfo = (TypeInfo) _typeInfo.get( obj.getClass() );
	    typeInfo.engine.delete( tx.getConnection( this ),
				    obj, oid.getPrimaryKey() );
	}
    }


    /**
     * Stores the object. The object must have been write locked
     * prior to this call. The object is updated in the database and
     * the lock is released. If the object is created at this point
     * or the primary key has changed, an new {@link OID} will be
     * returned and must be used for subsequent object access.
     *
     * @param oid The object OID
     * @param tx The transaction context
     * @param oid The object OID
     * @throws ODMGException An error occured with the persistence
     *   engine
     * @throws LockNotGrantedException Timeout or deadlock occured
     *   attempting to obtain write lock on object
     * @throws ObjectDeletedException The object has been deleted
     */
    OID store( OID oid, TransactionContext tx )
	throws ODMGException, LockNotGrantedException, ObjectDeletedException
    {
	Object     obj;
	ObjectLock lock;
	Object     newPrimKey;
	Object     oldPrimKey;
	TypeInfo   typeInfo;

	synchronized ( tx ) {
	    lock = getLock( oid );
	    // Must acquire a write lock on the object in order to proceed
	    try {
		tx.setWaitOnLock( lock );
		obj = lock.acquire( tx, true, _lockTimeout );
	    } finally {
		tx.setWaitOnLock( null );
	    }
	    typeInfo = (TypeInfo) _typeInfo.get( obj.getClass() );
	    if ( typeInfo.interceptor != null )
		typeInfo.interceptor.storing( obj );

	    // If the object has a primary key, it was retrieved/created
	    // before and need only be stored. If the object has no
	    // primary key, the object must be created at this point.
	    oldPrimKey = oid.getPrimaryKey();
	    newPrimKey = typeInfo.objDesc.getPrimaryKeyField().getValue( obj );
	    if ( newPrimKey == null )
		throw new ODMGException( "Attempt to create/store object without setting the primary key" );
	    if ( oldPrimKey == null ) {
		oid = new OID( this, typeInfo.objDesc, newPrimKey );
		if ( getLock( oid ) != null )
		    throw new DuplicatePrimaryKeyException( obj.getClass(), newPrimKey );
		removeOID( obj );
		removeLock( oid );
		typeInfo.engine.create( tx.getConnection( this ), obj, newPrimKey );
		setLock( oid, lock );
		setOID( obj, oid );
	    } else if ( newPrimKey == oldPrimKey ||
			typeInfo.objDesc.getPrimaryKey().equals( oldPrimKey, newPrimKey ) ) {
		typeInfo.engine.store( tx.getConnection( this ), obj, oldPrimKey );
	    } else {
		// XXX
		// typeInfo.engine.changePK( tx.getConnection( this ), oldPrimKey, newPrimKey );
		removeOID( obj );
		removeLock( oid );
		typeInfo.engine.store( tx.getConnection( this ), obj, newPrimKey );
		oid = new OID( this, typeInfo.objDesc, newPrimKey );
		setLock( oid, lock );
		setOID( obj, oid );
	    }
	}
	return oid;
    }


    OID query( Class type, String sql, Object[] values, TransactionContext tx, boolean exclusive )
	throws ODMGException, LockNotGrantedException, ObjectDeletedException
    {
	Object     obj;
	OID        oid;
	ObjectLock lock;
	TypeInfo   typeInfo;
	Object     primKey;

	typeInfo = (TypeInfo) _typeInfo.get( type );
	// Must prevent concurrent attempt to retrieve the same object
	// Best way to do that is through the type
	synchronized ( type ) {
	    obj = typeInfo.engine.query( tx.getConnection( this ), sql, values );
	    if ( obj == null )
		return null;
	    primKey = typeInfo.objDesc.getPrimaryKeyField().getValue( obj );

	    // Create an OID to represent the object and see if we
	    // have a lock (i.e. object is cached).
	    oid = new OID( this, typeInfo.objDesc, primKey );
	    lock = getLock( oid );
	    if ( lock == null ) {
		if ( typeInfo.interceptor != null ) {
		    typeInfo.interceptor.created( obj );
		    typeInfo.interceptor.loaded( obj );
		}

		lock = new ObjectLock( obj );
		try {
		    lock.acquire( tx, exclusive, 0 );
		} catch ( Exception except ) {
		    // This should never happen
		}
		setLock( oid, lock );
		setOID( obj, oid );
	    } else {
		// Object has been loaded before, must acquire lock
		// on it (write in exclusive mode)
		try {
		    tx.setWaitOnLock( lock );
		    obj = lock.acquire( tx, exclusive, _lockTimeout );
		} finally {
		    tx.setWaitOnLock( null );
		}
		// In exclusive mode must synchronize with the databse,
		// if an error occurs the object is immediately deleted
		// from memory
		if ( exclusive ) {
		    try {
			if ( ! typeInfo.engine.load( tx.getConnection( this ),
						     obj, primKey ) ) {
			    // Object not found a second time, delete it from
			    // memory and return null;
			    lock.delete( tx );
			    removeLock( oid );
			    removeOID( obj );
			    if ( typeInfo.interceptor != null )
				typeInfo.interceptor.forget( obj );
			    return null;
			}
 			if ( typeInfo.interceptor != null )
			    typeInfo.interceptor.loaded( obj );
		    } catch ( ODMGException except ) {
			lock.delete( tx );
			removeLock( oid );
			removeOID( obj );
			if ( typeInfo.interceptor != null )
			    typeInfo.interceptor.forget( obj );
			throw except;
		    }
		}
	    }
	}
	return oid;
    } 


    /**
     * Releases a lock on the object. Must be called when the
     * transaction commits/aborts on all objects that were not
     * created by the transaction.
     *
     * @param oid The object OID
     * @param tx The transaction context
     */
    void releaseLock( OID oid, TransactionContext tx )
    {
	ObjectLock lock;

	synchronized ( tx ) {
	    lock = getLock( oid );
	    lock.release( tx );
	}
    }


    /**
     * Forgets an object that was created/deleted during a transaction.
     * Called when the transaction aborts for a created object, or when
     * the transaction commits for a deleted object.
     *
     * @param oid The object OID
     * @param tx The transaction context
     */
    void forgetObject( OID oid, TransactionContext tx )
    {
	ObjectLock lock;
	Object     obj;
	TypeInfo   typeInfo;

	synchronized ( tx ) {
	    lock = getLock( oid );
	    // If this transaction has no write lock on the object,
	    // something went foul.
	    obj = lock.acquire( tx, true, 0 );
	    removeLock( oid );
	    removeOID( obj );
	    lock.delete( tx );
	    typeInfo = (TypeInfo) _typeInfo.get( obj.getClass() );
	    if ( typeInfo.interceptor != null )
		typeInfo.interceptor.forget( obj );
	}
    }


    /**
     * Obtains an object based on the OID. Always acquires a read lock
     * on the object but never waits for the lock. Expected to be called
     * after the object has been locked by the transaction.
     *
     * @param oid The OID
     * @param tx The transaction context
     * @return The object
     */
    Object getObject( OID oid, TransactionContext tx )
    {
	try {
	    return getLock( oid ).acquire( tx, false, 0 );
	} catch ( LockNotGrantedException except ) {
	    // This should never happen
	    throw new ODMGRuntimeException( except.getMessage() );
	}
    }


    private ObjectLock getLock( OID oid )
    {
	return (ObjectLock) _locks.get( oid );
    }


    private void setLock( OID oid, ObjectLock lock )
    {
	_locks.put( oid, lock );
    }


    private void removeLock( OID oid )
    {
	_locks.remove( oid );
    }


    private OID getOID( Object obj )
    {
	return (OID) _oids.get( obj );
    }


    private void setOID( Object obj, OID oid )
    {
	_oids.put( obj, oid );
    }


    private void removeOID( Object obj )
    {
	_oids.remove( obj );
    }


    Connection createConnection()
	throws SQLException
    {
	return _dbs.getConnection();
    }


    /**
     * Used by {@link DatabaseImpl} to determine the object descriptor
     * for a given class. Returns true if no mapping is found.
     */
    ObjectDesc getObjectDesc( Class type )
    {
	TypeInfo   typeInfo;

	typeInfo = (TypeInfo) _typeInfo.get( type );
	if ( typeInfo == null )
	    return null;
	else
	    return typeInfo.objDesc;
    }


    /**
     * Provides information about an object of a specific type
     * (class). This information includes a way to persist the object,
     * the object's descriptor and an interceptor requesting
     * notification about activities that affect an object.
     */
    static class TypeInfo
    {

	SQLEngine   engine;

	ObjectDesc  objDesc;

	Interceptor interceptor;

	TypeInfo( SQLEngine engine, ObjectDesc objDesc, Interceptor interceptor )
	{
	    this.engine = engine;
	    this.objDesc = objDesc;
	    this.interceptor = interceptor;
	}

    }


}


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


import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;
import org.odmg.ObjectNotPersistentException;
import org.odmg.ObjectDeletedException;
import org.odmg.LockNotGrantedException;
import org.odmg.ClassNotPersistenceCapableException;
import org.odmg.TransactionNotInProgressException;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.exolab.castor.jdo.ODMGSQLException;
import org.exolab.castor.jdo.TransactionAbortedReasonException;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
final class TransactionContext
{


    static class Status
    {
	/**
	 * The transaction is open and in progress.
	 */
	static final int Open = 0;

	/**
	 * The transaction has committed.
	 */
	static final int Committed = 1;

	/**
	 * The transaction has been aborted.
	 */
	static final int Rolledback = 2;

    }


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
     */
    private Hashtable   _objects = new Hashtable();


    /**
     * The transaction status.
     */
    private int         _status = Status.Open;


    /**
     * Lists all the connections opened for particular database engines
     * used in the lifetime of this transaction. The database engine
     * is used as the key to an open/transactional connection.
     */
    private Hashtable   _conns = new Hashtable();


    synchronized void create( DatabaseEngine dbEngine, Object obj )
    {
	OID         oid;
	ObjectEntry entry;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	oid = dbEngine.create( obj, this );
	entry = addObjectEntry( obj, oid, dbEngine );
	entry.created = true;
	entry.writeLock = true;
    }


    synchronized void delete( Object obj )
    {
	ObjectEntry entry;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	entry = getObjectEntry( obj );
	if ( entry == null )
	    throw new ObjectNotPersistentExceptionImpl( obj );
	entry.dbEngine.writeLock( obj, this );
	entry.deleted = true;
    }


    synchronized void lock( Object obj, boolean write )
	throws LockNotGrantedException
    {
	ObjectEntry entry;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	entry = getObjectEntry( obj );
	if ( entry == null )
	    throw new ObjectNotPersistentExceptionImpl( obj );
	// Read lock is available, only need to acquire write lock
	if ( write ) {
	    try {
		entry.dbEngine.writeLock( obj, this );
		entry.writeLock = true;
	    } catch ( ODMGRuntimeException except ) {
		throw new LockNotGrantedException( "Lock not granted for the following reason: " +
						   except.toString() );
	    }
	}
    }


    synchronized void unlock( Object obj )
    {
	ObjectEntry entry;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	entry = getObjectEntry( obj );
	if ( entry == null )
	    throw new ObjectNotPersistentExceptionImpl( obj );
	entry.dbEngine.releaseLock( entry.oid, this );
	removeObjectEntry( obj );
    }


    Object load( DatabaseEngine dbEngine, Class type, Object primKey, boolean exclusive )
	throws ODMGException
    {
	Object obj;
	OID    oid;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	oid = dbEngine.load( type, primKey, this, exclusive );
	if ( oid == null )
	    return null;
	obj = dbEngine.getObject( oid, this );
	addObjectEntry( obj, oid, dbEngine );
	return obj;
    }


    Object query( DatabaseEngine dbEngine, Class type, String sql, Object[] values, boolean exclusive )
	throws ODMGException
    {
	Object obj;
	OID    oid;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	oid = dbEngine.query( type, sql, values, this, exclusive );
	if ( oid == null )
	    return null;
	obj = dbEngine.getObject( oid, this );
	addObjectEntry( obj, oid, dbEngine );
	return obj;
    }


    synchronized void commit()
    {
	Enumeration enum;
	ObjectEntry entry;
	Connection  conn;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );
	try {
	    // Delete first, then create/destroy. This prevents random
	    // tackling with mutliple keys.
	    enum = _objects.elements();
	    while ( enum.hasMoreElements() ) {
		entry = (ObjectEntry) enum.nextElement();
		if ( entry.deleted ) {
		    entry.dbEngine.delete( entry.oid, this );
		}
	    }
	    enum = _objects.elements();
	    while ( enum.hasMoreElements() ) {
		// If object has been deleted, delete it, if not
		// store it (also creates)
		entry = (ObjectEntry) enum.nextElement();
		if (  ! entry.deleted ) {
		    entry.dbEngine.store( entry.oid, this );
		}
	    }

	    enum = _conns.elements();
	    while ( enum.hasMoreElements() ) {
		conn = (Connection) enum.nextElement();
		conn.commit();
		conn.close();
	    }
	    _conns.clear();

	    enum = _objects.elements();
	    while ( enum.hasMoreElements() ) {
		// If object has been deleted, forget about it,
		// otherwise just release locks held
		entry = (ObjectEntry) enum.nextElement();
		if ( entry.deleted ) {
		    entry.dbEngine.forgetObject( entry.oid, this );
		} else {
		    entry.dbEngine.releaseLock( entry.oid, this );
		}
	    }	    
	    _objects.clear();
	    _status = Status.Committed;
	} catch ( Exception except ) {
	    rollback();
	    throw new TransactionAbortedReasonException( except );
	}
    }


    synchronized void checkpoint()
    {
	Enumeration enum;
	ObjectEntry entry;
	Object      obj;
	Connection  conn;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );

	try {
	    enum = _objects.elements();
	    while ( enum.hasMoreElements() ) {
		entry = (ObjectEntry) enum.nextElement();
		// If object has been deleted, the object is deleted
		// from the database. If the object has been created,
		// it is added (new OID, do not forget about it when
		// rolling back). Otherwise, object is just stored.
		// Since the OID might change during storage and we keep
		// holding to the object, update to the new OID.
		if ( entry.deleted ) {
		    entry.dbEngine.delete( entry.oid, this );
		} else {
		    entry.oid = entry.dbEngine.store( entry.oid, this );
		    entry.created = false;
		}
	    }

	    enum = _conns.elements();
	    while ( enum.hasMoreElements() ) {
		conn = (Connection) enum.nextElement();
		conn.commit();
		conn.setAutoCommit( false );
	    }
	    _conns.clear();

	    enum = _objects.keys();
	    while ( enum.hasMoreElements() ) {
		obj = enum.nextElement();
		entry = (ObjectEntry) _objects.get( obj );
		if ( entry.deleted ) {
		    // Forget about deleted objects
		    entry.dbEngine.forgetObject( entry.oid, this );
		    _objects.remove( obj );
		    enum = _objects.keys();
		} else if ( entry.writeLock ) {
		    // If object has been locked before, must reacquire lock.
		    entry.dbEngine.writeLock( obj, this );
		}
	    }
	} catch ( Exception except ) {
	    rollback();
	    throw new TransactionAbortedReasonException( except );
	}
    }


    synchronized void rollback()
    {
	Enumeration enum;
	ObjectEntry entry;
	Connection  conn;

	if ( _status != Status.Open )
	    throw new TransactionNotInProgressException( "Transaction has been closed" );

	enum = _conns.elements();
	while ( enum.hasMoreElements() ) {
	    conn = (Connection) enum.nextElement();
	    try {
		conn.rollback();
		conn.close();
	    } catch ( SQLException except ) { }
	}
	_conns.clear();
	
	enum = _objects.elements();
	while ( enum.hasMoreElements() ) {
	    entry = (ObjectEntry) enum.nextElement();
	    // If object has been created, forget about it (cancel
	    // its creation), otherwise release all locks. Recover
	    // from any error.
	    try {
		if ( entry.created ) {
		    entry.dbEngine.forgetObject( entry.oid, this );
		} else {
		    entry.dbEngine.releaseLock( entry.oid, this );
		}
	    } catch ( Exception except ) { }
	}
	_objects.clear();
	_status = Status.Rolledback;
    }


    int getStatus()
    {
	return _status;
    }


    void setWaitOnLock( ObjectLock lock )
    {
	_waitOnLock = lock;
    }


    ObjectLock getWaitOnLock()
    {
	return _waitOnLock;
    }


    Connection getConnection( DatabaseEngine dbEngine )
	throws ODMGRuntimeException
    {
	Connection conn;

	conn = (Connection) _conns.get( dbEngine );
	if ( conn == null ) {
	    try {
		conn = dbEngine.createConnection();
		conn.setAutoCommit( false );
		_conns.put( dbEngine, conn );
	    } catch ( SQLException except ) {
		throw new ODMGSQLException( except );
	    }
	}
	return conn;
    }


    private ObjectEntry addObjectEntry( Object obj, OID oid, DatabaseEngine dbEngine )
    {
	ObjectEntry entry;
 
	entry = new ObjectEntry();
	entry.dbEngine = dbEngine;
	entry.oid = oid;
	_objects.put( obj, entry );
	return entry;
    }


    private ObjectEntry getObjectEntry( Object obj )
    {
	return (ObjectEntry) _objects.get( obj );
    }


    private ObjectEntry removeObjectEntry( Object obj )
    {
	return (ObjectEntry) _objects.remove( obj );
    }


    private static class ObjectEntry
    {
	DatabaseEngine dbEngine;
	OID            oid;
	boolean        deleted;
	boolean        created;
	boolean        writeLock;
    }


}

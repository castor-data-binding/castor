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


import org.odmg.ODMGRuntimeException;
import org.odmg.ObjectDeletedException;
import org.odmg.LockNotGrantedException;


/**
 * Read/write locks and lock synchronization on an object. Each object
 * is required to have one <tt>ObjectLock</tt> which at any given time
 * may be unlocked, write locked by one transaction, or read locked
 * by one or more transactions.
 * <p>
 * In order to obtain a lock, the transaction must call {@link
 * #acquire} passing itself, the lock type and the lock timeout. The
 * transaction must attempt to obtain only one lock at any given time
 * by synchronizing all calls to {@link #acquire}. If the transaction
 * has acquired a read lock it may attempt to re-acquire the read
 * lock. If the transaction attempts to acquire a write lock the lock
 * will be upgraded.
 * <p>
 * A read lock cannot be acquired while there is a write lock on the
 * object, and a write lock cannot be acquired while there is one or
 * more read locks. If a lock cannot be acquired, the transaction
 * will hold until the lock is available or timeout occurs. If timeout
 * occured (or a dead lock has been detected), {@link
 * LockNotGrantedException} is thrown. If the object has been deleted
 * while waiting for the lock, {@link ObjectDeletedException} is
 * thrown.
 * <p>
 * When the lock is acquired, {@link #acquire} will return the locked
 * object.
 * <p>
 * The transaction must call {@link #release} when the lock is no
 * longer required, allowing other transactions to obtain a lock. The
 * transaction must release all references to the object prior to
 * calling {@link #release}.
 * <p>
 * If the object has been deleted, the transaction must call {@link
 * #delete} instead of {@link #release}.
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 * @see TransactionContext
 */
final class ObjectLock
{


    /**
     * The object being locked, null if the object has been deleted.
     */
    private Object             _object;


    /**
     * Write lock on this object. Refers to the transaction that has
     * acquired the write lock. Read and write locks are mutually
     * exclusive.
     */
    private TransactionContext _writeLock;


    /**
     * Read locks on this object. A LinkedTx list of all transactions
     * that have acquired a read lock. Read and write locks are
     * mutually exclusive.
     */
    private LinkedTx           _readLock;


    /**
     * List of all transactions waiting for a read lock. Attempts to
     * acquire read lock while object has write lock will be recorded
     * here. When write lock is released, all read locks will acquire.
     */
    private LinkedTx           _readWaiting;


    /**
     * List of all transactions waiting for a write lock (including
     * waiting for upgrade from read lock). Attempts to acquire a
     * write lock while object has a read lock will be recorded here.
     * When read lock is released, the first write lock will acquire.
     */
    private LinkedTx          _writeWaiting;


    /**
     * Create a new lock for the specified object. Must not create two
     * locks for the same object. This will be the object returned from
     * a successful call to {@link #acquire}.
     *
     * @param obj The object to create a lock for
     */
    ObjectLock( Object obj )
    {
	_object = obj;
    }


    /**
     * Returns true if the transaction holds a read or write lock on
     * the object. This method is an efficient mean to determine whether
     * a lock is required, or if the object is owned by the transaction.
     *
     * @param tx The transaction
     * @param write True if must have a write lock
     * @return True if the transaction has a lock on this object
     */
    synchronized boolean hasLock( TransactionContext tx, boolean write )
    {
	LinkedTx read;

	if ( _writeLock == tx )
	    return true;
	if ( write )
	    return false;
	read = _readLock;
	while ( read != null ) {
	    if ( read.tx == tx )
		return true;
	    read = read.next;
	}
	return false;
    }


    /**
     * Acquires a lock on the object on behalf of the specified
     * transaction. A write lock will be acquired only if there are no
     * read/write locks on the object; only one write lock may be in
     * effect.  A read lock will be acquired only if there is no write
     * lock on the object; multiple read locks are allowed. If the
     * lock cannot be acquired immediately, the thread will block
     * until the lock is made available or the timeout has elapsed.
     * If the timeout has elapsed or a dead lock has been detected,
     * a {@link LockNotGrantedException} is thrown. If the object has
     * been deleted while waiting for a lock, a {@link
     * ObjectDeletedException} is thrown. To prevent dead locks, a
     * transaction must only call this method for any given object
     * from a single thread and must mark the lock it is trying to
     * acquire and return it from a call to {@link
     * TransactionContext#getWaitingOnLock} if the call to this method
     * has not returned yet. If a read lock is available for the
     * transaction and a write lock is requested, the read lock is
     * cancelled whether or not the write is acquired.
     * 
     * @param tx The transaction requesting the lock
     * @param write The type of lock requested
     * @param timeout Timeout waiting to acquire lock (in milliseconds),
     *   zero for no waiting
     * @return The locked object
     * @throws LockNotGrantedException Lock could not be granted in
     *   the specified timeout or a dead lock has been detected
     * @throws ObjectDeletedException The object has been deleted while
     *   waiting for the lock
     */
    synchronized Object acquire( TransactionContext tx, boolean write, int timeout )
	throws ObjectDeletedException, LockNotGrantedException
    {
	if ( _writeLock == tx ) {
	    // Already have write lock, can continue
	} else if ( _readLock == null && _writeLock == null ) {
	    // No locks, can acquire immediately
	    if ( write )
		_writeLock = tx;
	    else
		_readLock = new LinkedTx( tx, null );
	} else if ( write && _writeLock == null && _readLock.tx == tx && _readLock.next == null ) {
	    // Upgrading from read to write, no other locks, can upgrade
	    _writeLock = tx;
	    _readLock = null;
	} else if ( ! write && _writeLock == null ) {
	    // Looking for read lock and no write locks, can acquire
	    // Make sure we do not wait twice for the same lock
	    LinkedTx read;

	    read = _readLock;
	    while ( read != null ) {
		if ( read.tx == tx )
		    return _object;
	    }
	    _readLock = new LinkedTx( tx, _readLock );
	} else {
	    // Detect possibility of dead-lock
	    detectDeadlock( tx );

	    // Must wait for lock and then attempt to reacquire
	    if ( write ) {
		// If we already have a read lock, cancel it, we're upgrading
		_writeWaiting = new LinkedTx( tx, _writeWaiting );
		if ( _readLock.tx == tx ) {
		    _readLock = _readLock.next;
		} else {
		    LinkedTx wait;

		    wait = _readLock;
		    while ( wait.next != null ) {
			if ( wait.next.tx == tx ) {
			    wait.next = wait.next.next;
			    break;
			}
			wait = wait.next;
		    }
		}
	    } else {
		// Add new transaction waitin for read lock
		_readWaiting = new LinkedTx( tx, _readWaiting );
	    }

	    // Wait until notified or timeout elapses. Must detect
	    // when notified but object deleted (i.e. locks released)
	    // All read locks are notified at once, hence wait on the
	    // lock; only one write lock is notified at once, hence
	    // wait on the locking transaction.
	    if ( timeout > 0 ) {
		try {
		    if ( write )
			tx.wait( timeout );
		    else
			this.wait( timeout );
		} catch ( InterruptedException except ) {
		    throw new LockNotGrantedException( "Timeout occured while waiting for lock" );
		}
	    }
	    if ( _object == null ) {
		// If object has been deleted while waiting for lock, report deletion.
		throw new ObjectDeletedException( "Object has been deleted while waiting for lock" );
	    }

	    // If waiting for read lock the notified when there is no write
	    // lock. If waiting for write lock then notified when there is
	    // no read lock/write lock. Otherwise timeout occured.
	    if ( write && _writeLock == null && _readLock == null ) {
		_writeLock = tx;
	    } else if ( ! write && _writeLock == null ) {
		_readLock = new LinkedTx( tx, _readLock );
	    } else {
		// Timeout occured, must remove transaction from waiting list
		if ( write ) {
		    if ( _writeWaiting.tx == tx ) {
			_writeWaiting = _writeWaiting.next;
		    } else {
			LinkedTx wait;
			
			wait = _writeWaiting;
			while ( wait.next != null ) {
			    if ( wait.next.tx == tx ) {
				wait.next = wait.next.next;
				break;
			    }
			    wait = wait.next;
			}
		    }
		} else {
		    if ( _readWaiting.tx == tx ) {
			_readWaiting = _readWaiting.next;
		    } else {
			LinkedTx wait;
			
			wait = _readWaiting;
			while ( wait.next != null ) {
			    if ( wait.next.tx == tx ) {
				wait.next = wait.next.next;
				break;
			    }
			    wait = wait.next;
			}
		    }
		}
		throw new LockNotGrantedException( "Timeout occured while waiting for lock" );
	    }
	}
	return _object;
    }


    /**
     * Releases a lock on the object previously acquired with {@link
     * #acquire}. A write lock cannot be downgraded into a read lock
     * and the transaction loses its lock on the object. Other
     * transactions are allowed to acquire a read/write lock on the
     * object.
     *
     * @param tx The transaction that holds the lock
     */
    synchronized void release( TransactionContext tx )
    {
	// If this is the only lock, release it and notify the waiting transactions.
	// Either notify the next write-lock waiting, or notify all read-lock
	// waiting at once.
	if ( _writeLock == tx || ( _readLock.tx == tx && _readLock.next == null ) ) {
	    _writeLock = null;
	    _readLock = null;
	    if ( _writeWaiting != null ) {
		synchronized ( _writeWaiting.tx ) {
		    _writeWaiting.tx.notify();
		}
		_writeWaiting = _writeWaiting.next;
	    } else {
		_readWaiting = null;
		this.notifyAll();
	    }
	} else {
	    // Release one read lock out of possible many, but don't notify
	    // anyone (this is a read lock, and the object is still locked)
	    if ( _readLock.tx == tx ) {
		_readLock = _readLock.next;
	    } else {
		LinkedTx read;

		read = _readLock;
		while ( read.next != null ) {
		    if ( read.next.tx == tx ) {
			read.next = read.next.next;
			break;
		    }
		    read = read.next;
		}
	    }
	}
    }


    /**
     * Informs the lock that the object has been deleted by the
     * transaction holding the write lock. The lock on the object is
     * released and all transactions waiting for a lock will
     * terminate with an {@link ObjectDeletedException}.
     *
     * @param tx The transaction that holds the lock
     */
    synchronized void delete( TransactionContext tx )
    {
	if ( tx != _writeLock )
	    throw new ODMGRuntimeException( "Internal error: cannot delete object without acquiring write lock first" );
	// Mark lock as unlocked and deleted
	_object = null;
	_writeLock = null;
	// Notify all waiting threads that must terminate with an
	// exception and not attempt to acquire the lock
	while ( _writeWaiting != null ) {
	    _writeWaiting.tx.notify();
	    _writeWaiting = _writeWaiting.next;
	}
	while ( _readWaiting != null ) {
	    _readWaiting.tx.notify();
	    _readWaiting = _readWaiting.next;
	}
    }


    /**
     * Detects a possible dead lock involving the transaction waiting
     * to acquire this lock. If the lock is locked (read or write) by
     * any transaction waiting for a lock on <tt>waitingTx</tt>, a
     * dead lock is detected and {@link LockNotGrantedException}
     * thrown.
     *
     * @param waitingTx The transaction waiting to acquire this lock
     */
    private void detectDeadlock( TransactionContext waitingTx )
	throws LockNotGrantedException
    {
	ObjectLock waitOn;

	// Inspect write lock and all read locks (the two are mutually exclusive).

	// For each lock look at all the waiting transactions( waitOn) and
	// determine whether they are currently waiting for a lock. A transaction
	// is waiting for a lock if it has called acquireLock() and has not
	// returned from the call.

	// If one of these locks is locked (read or write) by this transaction,
	// a dead lock has been detected. Recursion is necessary to prevent
	// indirect dead locks (A locked by B, B locked by C, C acquires lock on A)

	// Only the last lock attempt in a dead-lock situation will cancel.

	if ( _writeLock != null ) {
	    waitOn = _writeLock.getWaitOnLock();
	    if ( waitOn != null ) {
		LinkedTx read;

		if ( waitOn._writeLock == waitingTx ) {
		    throw new LockNotGrantedException( "Deadlock detected" );
		}
		read = _readLock;
		while ( read != null ) {
		    if ( read.tx == waitingTx )
			throw new LockNotGrantedException( "Deadlock detected" );
		    read = read.next;
		}
		waitOn.detectDeadlock( waitingTx );
	    }
	} else {
	    LinkedTx lock;

	    lock = _readLock;
	    while ( lock != null ) {
		waitOn = lock.tx.getWaitOnLock();
		if ( waitOn != null ) {
		    LinkedTx read;

		    if ( waitOn._writeLock == waitingTx ) {
			throw new LockNotGrantedException( "Deadlock detected" );
		    }
		    read = _readLock;
		    while ( read != null ) {
			if ( read.tx == waitingTx ) 
			    throw new LockNotGrantedException( "Deadlock detected" );
			read = read.next;
		    }
		    waitOn.detectDeadlock( waitingTx );
		}
		lock = lock.next;
	    }
	}
    }


    /**
     * Object uses to hold a linked list of transactions holding
     * write locks or waiting for a read/write lock.
     */
    static class LinkedTx
    {

	TransactionContext tx;

	LinkedTx           next;

	LinkedTx( TransactionContext tx, LinkedTx next )
	{
	    this.tx = tx;
	    this.next = next;
	}

    }


}


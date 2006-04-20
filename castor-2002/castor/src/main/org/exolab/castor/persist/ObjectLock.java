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


import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.util.Messages;


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


    final static boolean TRACE = false;


    /**
     * The object being locked, null if the object has been deleted.
     */
    private Object              _object;


    /**
     * The object's OID.
     */
    private OID                 _oid;


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
    ObjectLock( Object object, OID oid )
    {
        _object = object;
        _oid = oid;
    }


    /**
     * Return the object's OID.
     */
    OID getOID()
    {
        return _oid;
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
    boolean hasLock( TransactionContext tx, boolean write )
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
     * TransactionContext#getWaitOnLock} if the call to this method
     * has not returned yet. If a read lock is available for the
     * transaction and a write lock is requested, the read lock is
     * cancelled whether or not the write is acquired.
     *
     * @param tx The transaction requesting the lock
     * @param write The type of lock requested
     * @param timeout Timeout waiting to acquire lock (in milliseconds),
     *  zero for no waiting
     * @return The locked object
     * @throws LockNotGrantedException Lock could not be granted in
     *  the specified timeout or a dead lock has been detected
     * @throws ObjectDeletedWaitingForLockException The object has
     *  been deleted while waiting for the lock
     */
    synchronized Object acquire( TransactionContext tx, boolean write, int timeout )
        throws LockNotGrantedException, ObjectDeletedWaitingForLockException
    {
	// Note: This method must succeed even if an exception is thrown
	// in the middle. An exception may be thrown by a Thread.stop().
	// Must make sure not to lose consistency.
	    
	while ( true ) {
            try {
                // Repeat forever until lock is acquired or timeout
                
                if ( _writeLock == tx ) {
                    // Already have write lock, can acquire object
                    return _object;
                } else if ( _readLock == null && _writeLock == null ) {
                    // No locks, can acquire immediately
                    if ( write )
                        _writeLock = tx;
                    else
                        _readLock = new LinkedTx( tx, null );
                    if ( TRACE )
                        System.out.println( "Acquired on " + toString() + " by " + tx );
                    return _object;
                } else if ( write && _writeLock == null &&
                            _readLock.tx == tx && _readLock.next == null ) {
                    // Upgrading from read to write, no other locks, can upgrade
                    // Order is important in case thread is stopped in the middle
                    _readLock = null;
                    _writeLock = tx;
                    if ( TRACE )
                        System.out.println( "Acquired on " + toString() + " by " + tx );
                    return _object;
                } else if ( ! write && _writeLock == null && _writeWaiting == null ) {
                    // Looking for read lock and no write locks, can acquire
                    // But only if not other transaction is waiting for write lock first
                    // Make sure we do not wait twice for the same lock
                    LinkedTx read;
                    
                    read = _readLock;
                    while ( read != null ) {
                        if ( read.tx == tx )
                            return _object;
                        read = read.next;
                    }
                    if ( TRACE )
                        System.out.println( "Acquired on " + toString() + " by " + tx );
                    _readLock = new LinkedTx( tx, _readLock );
                    return _object;
                } else {
                    // Don't wait if timeout is zero
                    if ( timeout == 0 ) {
                        if ( TRACE )
                            System.out.println( "Timeout on " + this.toString() + " by " + tx );
                        throw new LockNotGrantedExceptionImpl( write ? "persist.writeLockTimeout" :
                                                               "persist.readLockTimeout" );
                    }
                    if ( TRACE )
                        System.out.println( "Waiting on " + this.toString() + " by " + tx );
		    // Detect possibility of dead-lock. Must remain in wait-on-lock
		    // position until lock is granted or exception thrown.
		    tx.setWaitOnLock( this );
		    detectDeadlock( tx );
		    
		    // Must wait for lock and then attempt to reacquire
		    if ( write )
			_writeWaiting = new LinkedTx( tx, _writeWaiting );
		    else
			_readWaiting = new LinkedTx( tx, _readWaiting );
		    
		    // Wait until notified or timeout elapses. Must detect
		    // when notified but object deleted (i.e. locks released)
		    // All waiting transactions are notified at once, but once
		    // notified a race condition starts to acquire new lock
		    long clock = System.currentTimeMillis();
		    try {
			wait( timeout * 1000 );
		    } catch ( InterruptedException except ) {
			// If the thread is interrupted, come out with the proper message
			throw new LockNotGrantedExceptionImpl( write ? "persist.writeLockTimeout" :
                                                               "persist.readLockTimeout" );
		    }
		    if ( _object == null )
			// If object has been deleted while waiting for lock, report deletion.
			throw new ObjectDeletedWaitingForLockException();

		    // Try to re-acquire lock, this time less timeout,
		    // eventually timeout of zero will either succeed or fail
		    // without blocking.
		    timeout -= ( System.currentTimeMillis() - clock );
		    if ( timeout < 0 )
			timeout = 0;
		    removeWaiting( tx );
		    tx.setWaitOnLock( null );
                }
            } finally {
                // Must always remove waiting transaction.
                removeWaiting( tx );
                tx.setWaitOnLock( null );
            }
	}
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
        if ( TRACE )
            System.out.println( "Release " + this.toString() + " by " + tx );
        try {
            tx.setWaitOnLock( null );
            if ( _writeLock == tx ) {
                _writeLock = null;
            } else {
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
                    if ( read == null )
                        throw new IllegalStateException( Messages.message( "persist.notOwnerLock" ) );
                }
            }
            // Notify all waiting transactions that they may attempt to
            // acquire lock. First one to succeed wins (or multiple if
            // waiting for read lock).
            notifyAll();
        } catch ( ThreadDeath death ) {
            // This operation must never fail, not even in the
            // event of a thread death
            release( tx );
            throw death;
        }
    }


    /**
     * Informs the lock that the object has been deleted by the
     * transaction holding the write lock. The lock on the object is
     * released and all transactions waiting for a lock will
     * terminate with an {@link ObjectDeletedException}.
     *
     * @param tx The transaction that holds the lock
     * @throws RuntimeException Attempt to delete object without
     *   acquiring a write lock
     */
    synchronized void delete( TransactionContext tx )
    {
        if ( tx != _writeLock )
            throw new RuntimeException( Messages.message( "persist.notOwnerLock" ) );
        if ( TRACE )
            System.out.println( "Delete " + this.toString() + " by " + tx );
        try {
            // Mark lock as unlocked and deleted, notify all waiting transactions
            _object = null;
            _writeLock = null;
            notifyAll();
        } catch ( ThreadDeath death ) {
            // Delete operation must never fail, not even in the
            // event of a thread death
            release( tx );
            throw death;
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
        // is waiting for a lock if it has called acquire() and has not
        // returned from the call.

        // If one of these locks is locked (read or write) by this transaction,
        // a dead lock has been detected. Recursion is necessary to prevent
        // indirect dead locks (A locked by B, B locked by C, C acquires lock on A)

        // Only the last lock attempt in a dead-lock situation will cancel.

        if ( _writeLock != null ) {
            // _writeLock is the blocking transaction. We are only interested in
            // a blocked transacrtion.
            waitOn = _writeLock.getWaitOnLock();
            if ( waitOn != null ) {
                LinkedTx read;

                // Is the blocked transaction blocked by the transaction locking
                // this object? This is a deadlock.
                if ( waitOn._writeLock == waitingTx ) {
                    throw new LockNotGrantedExceptionImpl( "persist.deadlock" );
                }
                read = waitOn._readLock;
                while ( read != null ) {
                    if ( read.tx == waitingTx )
                        throw new LockNotGrantedExceptionImpl( "persist.deadlock" );
                    read = read.next;
                }
                waitOn.detectDeadlock( waitingTx );
            }
        } else {
            LinkedTx lock;

            lock = _readLock;
            while ( lock != null ) {

                // T1 trying to acquire lock on O1, which is locked by T2
                // T2 trying to acauire lock on O1, T1 is waiting on O1

                // lock is the blocking transaction. We are only interested in
                // a blocked transacrtion.
                waitOn = lock.tx.getWaitOnLock();
                if ( waitOn != null && lock.tx != waitingTx ) {
                    LinkedTx read;

                    if ( waitOn._writeLock == waitingTx ) {
                        throw new LockNotGrantedExceptionImpl( "persist.deadlock" );
                    }
                    read = waitOn._readLock;
                    while ( read != null ) {
                        if ( read.tx == waitingTx )
                            throw new LockNotGrantedExceptionImpl( "persist.deadlock" );
                        read = read.next;
                    }
                    waitOn.detectDeadlock( waitingTx );
                }
                lock = lock.next;
            }
        }
    }


    /**
     * Remove the transaction from the waiting list (both read and write).
     */
    private void removeWaiting( TransactionContext tx )
    {
        try {
            if ( _writeWaiting != null ) {
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
            }
            if ( _readWaiting != null ) {
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
        } catch ( ThreadDeath death ) {
            // This operation must never fail, not even in the
            // event of a thread death
            removeWaiting( tx );
            throw death;
        }
    }


    public String toString()
    {
        return _oid.toString() + " " + ( ( _readLock == null ? "-" : "R" ) + "/" +
                                         ( _writeLock == null ? "-" : "W" ) );
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


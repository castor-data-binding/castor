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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.TransactionContext;
import org.castor.persist.cache.CacheEntry;
import org.castor.util.Messages;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectDeletedException;

/**
 * Read/write locks and lock synchronization on an object. Each object
 * is required to have one <tt>ObjectLock</tt> which at any given time
 * may be unlocked, write locked by one transaction, or read locked
 * by one or more transactions.
 * <p>
 * In order to obtain a lock, the transaction must call one of the
 * acquire, passing itself, the lock type and the lock timeout. The
 * transaction must attempt to obtain only one lock at any given time
 * by synchronizing all calls to one of the <tt>acquire</tt>. If the transaction
 * has acquired a read lock it may attempt to re-acquire the read
 * lock. If the transaction attempts to acquire a write lock the lock
 * will be upgraded.
 * <p>
 * A read lock cannot be acquired while there is a write lock on the
 * object, and a write lock cannot be acquired while there is one or
 * more read locks. If a lock cannot be acquired, the transaction
 * will hold until the lock is available or timeout occurs. If timeout
 * occured (or a dead lock has been detected), {@link
 * LockNotGrantedException} is thrown. If the object has been delete
 * while waiting for the lock, {@link ObjectDeletedException} is
 * thrown.
 * <p>
 * When the lock is acquired, the locked object is returned.
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
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see TransactionContext
 */

public final class ObjectLock implements DepositBox {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getFactory().getInstance(ObjectLock.class);

    protected static final short ACTION_READ = 1;

    protected static final short ACTION_WRITE = 2;

    protected static final short ACTION_CREATE = 3;

    protected static final short ACTION_UPDATE = 4;

    private static final int[] LOCK = new int[0];

    private static int _idcount = 0;
    
    private int _id;

    /** The object being locked. */
    private Object[] _object;

    /** The object's OID. */
    private OID _oid;

    /** Write lock on this object. Refers to the transaction that has
     *  acquired the write lock. Read and write locks are mutually
     *  exclusive. */
    private TransactionContext _writeLock;

    /** Read locks on this object. A LinkedTx list of all transactions
     *  that have acquired a read lock. Read and write locks are
     *  mutually exclusive. */
    private LinkedTx _readLock;

    /** List of all transactions waiting for a read lock. Attempts to
     *  acquire read lock while object has write lock will be recorded
     *  here. When write lock is released, all read locks will acquire. */
    private LinkedTx _readWaiting;

    private int _waitCount;

    /** List of all transactions waiting for a write lock (including
     *  waiting for upgrade from read lock). Attempts to acquire a
     *  write lock while object has a read lock will be recorded here.
     *  When read lock is released, the first write lock will acquire. */
    private LinkedTx _writeWaiting;

    private TransactionContext _confirmWaiting;

    private short _confirmWaitingAction;

    /** Number of transactions which are interested to invoke method on this lock.
     *  If the number is zero, and the lock isFree(), then it is safe dispose this lock. */
    private int _gateCount;

    private long _timeStamp;

    private boolean _deleted;

    private boolean _invalidated;

    private boolean _isExpired;
    
    private Object[] _expiredObject;

    /**
     * Create a new lock for the specified object. Must not create two
     * locks for the same object. This will be the object returned from
     * a successful call to one of the <tt>acquire</tt>.
     *
     * @param oid The object to create a lock for
     */
    public ObjectLock(final OID oid) {
        _oid = oid;

        // give each instance of ObjectLock an id, for debug only
        synchronized (LOCK) {
            _id = _idcount;
            _idcount++;
        }
    }
    
    public ObjectLock(final CacheEntry entry) {
        this(entry.getOID());
        
        _isExpired = false; 
        _expiredObject = null;
        _object = entry.getEntry();
        _timeStamp = entry.getTimeStamp();
    }


    /**
     * Return the object's OID.
     */
    public OID getOID() {
        return _oid;
    }

    /** 
     * Set OID of this lock to new value.
     *
     */
    void setOID(final OID oid) {
        _oid = oid;
    }

    /**
     * Indicate that a transaction is interested in this lock.
     * A transaction should call this method if it is going to
     * change the state of this lock (by calling acquire, update 
     * or relase.) It method should be synchronized externally 
     * to avoid race condition. enter and leave should be called 
     * exactly the same number of time.
     */
    void enter() {
        _gateCount++;
    }

    /**
     * Indicate that a transaction is not interested to change the 
     * state of this lock anymore. (ie, will not call either acquire
     * update, release or delete.) 
     * It method should be synchronized externally.
     */
    void leave() {
        _gateCount--;
    }

    /**
     * Return true if there is any transaction called {@link #enter},
     * but not yet called {@link #leave}.
     */ 
    boolean isEntered() {
        return _gateCount != 0;
    }

    /**
     * Return true if this object can be safely disposed. An ObjectLock
     * can be safely disposed if and only if the no transaction is 
     * holding any lock, nor any transaction isEntered.
     */
    boolean isDisposable() {
        return _gateCount == 0 && isFree() && _waitCount == 0;
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
    boolean hasLock(final TransactionContext tx, final boolean write) {
        LinkedTx read;

        if (_writeLock == tx)
            return true;

        if (_confirmWaiting == tx) {
            if ((_confirmWaitingAction == ACTION_WRITE) || (_confirmWaitingAction == ACTION_CREATE))
                return true;
            else if (!write && _confirmWaitingAction == ACTION_READ) 
                return true;
            return false;
        }

        if (write)
            return false;
        read = _readLock;
        while (read != null) {
            if (read.tx == tx)
                return true;
            read = read.next;
        }

        return false;
    }

    /**
     * Return true if and only if this lock can be safely disposed
     *
     * @return True if no lock and no waiting
     */
    boolean isFree() {
        return ((_writeLock == null) && (_readLock == null) && 
                 (_writeWaiting == null) && (_readWaiting == null) && 
                 (_confirmWaiting == null) && (_waitCount == 0));
    }

    boolean isExclusivelyOwned(final TransactionContext tx) {
        if ((_writeLock == null) && (_readLock == null))
            return false;

        if ((_writeLock == null) && (_readLock.tx == tx) && 
                        (_readLock.next.tx == null))
            return true;

        if ((_writeLock == tx) && (_readLock == null))
            return true;

        return false;
    }

    
    /**
     * Return true if this entry has been expired from the cache
     */
    boolean isExpired() {
        return _isExpired; 
    }

    public Object[] getObject() { 
        if ((_expiredObject != null) && (_object == null))
            return _expiredObject;
        return _object; 
    }
   
    /**
     * Indicate that object needs to be expired from the cache
     */
    public void expire() {
        _isExpired = true; 
    }
   
    /**
     * Indicate that object has been removed from the cache. Perform any 
     * post expiration cleanup.  In particular, remove the reference to any
     * saved cached objects.
     */
    public void expired() { 
        _isExpired = false; 
        _expiredObject = null;
    }

    synchronized void acquireLoadLock(final TransactionContext tx, final boolean write, final int timeout)
    throws LockNotGrantedException, ObjectDeletedWaitingForLockException {
        int internalTimeout = timeout;
        long endtime = (internalTimeout > 0) ? System.currentTimeMillis() + internalTimeout * 1000 : Long.MAX_VALUE;
        while (true) {
            try {
                // cases to consider:
                // 3/ waitingForConfirmation exist
                //      then, we wait
                // 4/ need a read, and objectLock has something
                //      then, we return and wait for confirmation
                // 5/ need a read, and objectLock has nothing
                //      then, we return and wait for confirmation
                // 6/ need a write
                //      then, we return and wait for confirmation
                // 7/ we're in some kind of lock, or waiting, exception
                // 1/ write exist 
                //      then, put it tx into read/write waiting
                // 2/ read exist
                //      then, put it read, or write waiting
                if (_deleted) {
                    throw new ObjectDeletedWaitingForLockException("Object deleted");
                } else if (_confirmWaiting != null) {
                    // other thread is loading or creating object and haven't finished
                    try {
                        _waitCount++;
                        wait();                        
                    } catch (InterruptedException e) {
                        throw new LockNotGrantedException("Thread interrupted acquiring lock!", e);
                    } finally {
                        _waitCount--;
                    }
                } else if (_writeLock == tx) {
                    //throw new IllegalStateException("Transaction: "+tx+" has already hold the write lock on "+_oid+
                    //        " Acquire shouldn't be called twice");
                    return;
                } else if ((_readLock == null) && (_writeLock == null) && write) {
                    // no transaction hold any lock,
                    _confirmWaiting = tx;
                    _confirmWaitingAction = ACTION_WRITE;
                    return;
                } else if ((_readLock == null) && (_writeLock == null) && !write) {
                    // no transaction hold any lock, 
                    if (_object == null) {
                        _confirmWaiting = tx;
                        _confirmWaitingAction = ACTION_READ;
                        return;
                    }
                    _readLock = new LinkedTx(tx, null);
                    return;
                } else if ((_readLock != null) && !write) {
                    // already a transaction holding read lock, can acquire read lock
                    LinkedTx linked = _readLock;
                    while (linked != null) {
                        if (linked.tx == tx)
                            throw new IllegalStateException("Transaction: " + tx + " has already hold the write lock on " + _oid + 
                            " Acquire shouldn't be called twice");
                            //return;
                        linked = linked.next;
                    }
                   
                    // if not already in readLock
                    _readLock = new LinkedTx(tx, _readLock);
                    return;
                } else {
                    // other transaction holding writeLock, waits for write
                    // or, other transaction holding readLock, waiting for read
                    if (internalTimeout == 0) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Timeout on " + this.toString() + " by " + tx);
                        }
                        throw new LockNotGrantedException((write ? "persist.writeLockTimeout" :
                                                               "persist.readLockTimeout") + _oid + "/" + _id + " by " + tx);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Waiting on " + this.toString() + " by " + tx);
                    }
                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.
                    tx.setWaitOnLock(this);
                    detectDeadlock(tx, 10);
                    
                    // Must wait for lock and then attempt to reacquire
                    if (write)
                        _writeWaiting = new LinkedTx(tx, _writeWaiting);
                    else
                        _readWaiting = new LinkedTx(tx, _readWaiting);
                    
                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait((waittime < 0) ? 0 : waittime);
                    } catch (InterruptedException except) {
                        // If the thread is interrupted, come out with the proper message
                        throw new LockNotGrantedException(write ? "persist.writeLockTimeout" :
                                                               "persist.readLockTimeout" + _oid + "/" + _id + " by " + tx, except);
                    }

                    if (_deleted)
                        // If object has been deleted while waiting for lock, report deletion.
                        throw new ObjectDeletedWaitingForLockException("object deleted" + _oid + "/" + _id + " by " + tx);

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if (System.currentTimeMillis() > endtime)
                        internalTimeout = 0;

                    removeWaiting(tx);
                    tx.setWaitOnLock(null);
                }
            } finally {
                removeWaiting(tx);       
                tx.setWaitOnLock(null);
            }
        }
    }

    synchronized void acquireCreateLock(final TransactionContext tx)
    throws LockNotGrantedException {
        while (true) {
            // cases to consider:
            // 1/ waitingForConfirmation exist
            // 2/ lock can't be granted, throw LockNotGrantedException
            // 3/ lock can be granted
            //      then, we return and wait for confirmation
            if (_deleted || (_confirmWaiting != null)) {
                // other thread is loading or creating object and haven't finished
                try {
                    _waitCount++;
                    wait();
                    while (_deleted) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    throw new LockNotGrantedException("Thread interrupted acquiring lock!", e);
                } finally {
                    _waitCount--;
                }
            } else if ((_readLock != null) || (_writeLock != null)) {
                throw new LockNotGrantedException("Lock already exist!");
            } else {
                _confirmWaiting = tx;
                _confirmWaitingAction = ACTION_CREATE;
                return;
            }
        }
    }

    // probaraly we just don't need update....
    synchronized void acquireUpdateLock(final TransactionContext tx, final int timeout)
    throws LockNotGrantedException, ObjectDeletedWaitingForLockException {
        int internalTimeout = timeout;
        long endtime = (internalTimeout > 0) ? System.currentTimeMillis() + internalTimeout * 1000 : Long.MAX_VALUE;
        while (true) {
            try {
                // case to consider:
                // 1/ waitingForConfirmation exist
                // 2/ lock can be granted, and _object is not empty
                //      then, we return and wait for confirmation
                // 3/ lock can not granted, wait

                if (_deleted || (_confirmWaiting != null)) {
                    try {
                        _waitCount++;
                        wait();
                        /*
                        if ( _deleted ) {
                            throw new ObjectDeletedWaitingForLockException("Object deleted!");
                        }*/
                    } catch (InterruptedException e) {
                        throw new LockNotGrantedException("Thread interrupted acquiring lock!", e);
                    } finally {
                        _waitCount--;
                    }
                } else if (_writeLock == tx) {
                    return;
                } else if ((_writeLock == null) && (_readLock == null)) {
                    // can get the lock now
                    _confirmWaiting = tx;
                    _confirmWaitingAction = ACTION_UPDATE;
                    return;
                } else {
                    if (internalTimeout == 0) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Timeout on " + this.toString() + " by " + tx);
                        }
                        throw new LockNotGrantedException(Messages.message("persist.writeLockTimeout"));
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Waiting on " + this.toString() + " by " + tx);
                    }
                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.
                    tx.setWaitOnLock(this);
                    detectDeadlock(tx, 10);
                    
                    // Must wait for lock and then attempt to reacquire
                    _writeWaiting = new LinkedTx(tx, _writeWaiting);
                    
                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait((waittime < 0) ? 0 : waittime);
                    } catch (InterruptedException except) {
                        // If the thread is interrupted, come out with the proper message
                        throw new LockNotGrantedException(Messages.message("persist.writeLockTimeout") + _oid + "/" + _id + " by " + tx, except);
                    }

                    if (_deleted)
                        // If object has been deleted while waiting for lock, report deletion.
                        throw new ObjectDeletedWaitingForLockException("Object deleted " + _oid + "/" + _id + " by " + tx);

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if (System.currentTimeMillis() > endtime)
                        internalTimeout = 0;

                    removeWaiting(tx);
                    tx.setWaitOnLock(null);
                }
            } finally {
                removeWaiting(tx);
                tx.setWaitOnLock(null);                
            }
        }
    }

    public synchronized void setObject(final TransactionContext tx, final Object[] object) {

        _isExpired = false; // initialize cache expiration flag to false
        _expiredObject = null;

        if ((_confirmWaiting != null) && (_confirmWaiting == tx)) {
            _timeStamp = System.currentTimeMillis();
            _object = object;
            if (_confirmWaitingAction == ACTION_READ) {
                _readLock = new LinkedTx(tx, null);
            } else {
                _writeLock = tx;
            }
            _confirmWaiting = null;
            notifyAll();
        } else if ((_writeLock != null) && (_writeLock == tx)) {
            _timeStamp = System.currentTimeMillis();
            _object = object;
        } else
            throw new IllegalArgumentException("Transaction tx does not own this lock, " + toString() + "!");
    }

    public synchronized Object[] getObject(final TransactionContext tx) {
        if ((_confirmWaiting != null) && (_confirmWaiting == tx))
            return _object;
        else if ((_writeLock != null) && (_writeLock == tx))
            return _object;
        else {
            LinkedTx link = _readLock;
            while (link != null) {
                if (link.tx == tx) 
                    return _object;
                link = link.next;
            }
            throw new IllegalArgumentException("Transaction tx does not own this lock!");
        }
    }

    public synchronized long getTimeStamp() {
        return _timeStamp;
    }

    synchronized void confirm(final TransactionContext tx, final boolean succeed) {

        // cases to consider:
        // 1/ not in waitingForConfirmation
        // 2/ load_read,
        //         downgrade the lock
        // 3/ else
        //         move confirmation and
        //         notify()
        if (_confirmWaiting == tx) {
            if (succeed) {
                if (_confirmWaitingAction == ACTION_READ) {
                    if (_readLock == null) 
                        _readLock = new LinkedTx(tx, null);
                } else {
                    _writeLock = tx;
                }
            }
            _confirmWaiting = null;
            notifyAll();
        } else if (_confirmWaiting == null) {
            if (!succeed) {
                // remove it from readLock
                if (_writeLock != null) {
                    // same as delete the lock
                    _deleted = true;
                    _object = null;
                    _timeStamp =  System.currentTimeMillis();
                    //_writeLock = null;
                    notifyAll();
                } else if (_readLock == null) {
                } else if (_readLock.tx == tx)
                    _readLock = _readLock.next;
                else {
                    LinkedTx link = _readLock;
                    while (link != null) {
                        if ((link.next != null) && (link.next.tx == tx)) {
                            link.next = link.next.next;
                            notifyAll();
                            return;
                        }
                        link = link.next;

                    }
                }
            }
            notifyAll();
        } else 
            throw new IllegalStateException("Confirm transaction does not match the locked transaction");        
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
     * @param internalTimeout Timeout waiting to acquire lock (in milliseconds),
     *  zero for no waiting
     * @throws LockNotGrantedException Lock could not be granted in
     *  the specified timeout or a dead lock has been detected
     * @throws ObjectDeletedWaitingForLockException The object has
     *  been deleted while waiting for the lock
     */
    synchronized void upgrade(final TransactionContext tx, final int timeout)
    throws LockNotGrantedException, ObjectDeletedWaitingForLockException {
        int internalTimeout = timeout;
        // Note: This method must succeed even if an exception is thrown
        // in the middle. An exception may be thrown by a Thread.stop().
        // Must make sure not to lose consistency.

        if (_confirmWaiting != null) {
            IllegalStateException e = new IllegalStateException("Internal error: acquire when confirmWaiting is not null");
            throw e;
        }
        if (!hasLock(tx, false)) {
            IllegalStateException e = new IllegalStateException("Transaction didn't previously acquire this lock");
            throw e;
        }

        long endtime = (internalTimeout > 0) ? System.currentTimeMillis() + internalTimeout * 1000 : Long.MAX_VALUE;
        while (true) {
            // Repeat forever until lock is acquired or timeout
            try {

                if (_writeLock == tx) {
                    // Already have write lock, can acquire object
                    return;
                } else if ((_writeLock == null) && 
                            (_readLock.tx == tx) && (_readLock.next == null)) {
                    // Upgrading from read to write, no other locks, can upgrade
                    // Order is important in case thread is stopped in the middle
                    //_readLock = null;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Acquired on " + this.toString() + " by " + tx);
                    }
                    _writeLock = tx;
                    _readLock = null;
                    return;
                } else {
                    // Don't wait if timeout is zero
                    if (internalTimeout == 0) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Timeout on " + this.toString() + " by " + tx);
                        }
                        throw new LockNotGrantedException("persist.writeTimeout" + _oid + "/" + _id + " by " + tx);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Waiting on " + this.toString() + " by " + tx);
                    }
                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.

                    tx.setWaitOnLock(this);
                    detectDeadlock(tx, 10);
                    
                    // Must wait for lock and then attempt to reacquire
                    _writeWaiting = new LinkedTx(tx, _writeWaiting);
                    
                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait((waittime < 0) ? 0 : waittime);
                    } catch (InterruptedException except) {
                        // If the thread is interrupted, come out with the proper message
                        throw new LockNotGrantedException("persist.writeLockTimeout", except);
                    }

                    if (_deleted)
                        // object should not be deleted, as we got lock on it
                        throw new IllegalStateException("internal error: object deleted" + _oid + "/" + _id + " by " + tx);

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if (System.currentTimeMillis() > endtime)
                        internalTimeout = 0;
                    removeWaiting(tx);
                    tx.setWaitOnLock(null);
                }
            } finally {
                // Must always remove waiting transaction.
                removeWaiting(tx);
                tx.setWaitOnLock(null);
            }
        } 
    }


    /**
     * Releases a lock on the object previously acquired. 
     * A write lock cannot be downgraded into a read lock
     * and the transaction loses its lock on the object. Other
     * transactions are allowed to acquire a read/write lock on the
     * object.
     *
     * @param tx The transaction that holds the lock
     */
    synchronized void release(final TransactionContext tx) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Release " + this.toString() + " by " + tx);
        }

        try {
            tx.setWaitOnLock(null);
            if (_writeLock == tx) {
                _writeLock = null;
                if (_invalidated || _deleted) {
                    _timeStamp = System.currentTimeMillis();
                    // save a copy of the expired objects contents;
                    // this will be used to expire all contained objects
                    if (_isExpired) 
                        _expiredObject = _object;
                    _object = null;
                }
                _deleted = false;
                _invalidated = false;
            } else if (_readLock != null) {
                if (_readLock.tx == tx) {
                    _readLock = _readLock.next;
                } else {
                    LinkedTx read = _readLock;
                    while (read != null) {
                        if (read.next != null && read.next.tx == tx) {
                            read.next = read.next.next;
                            break;
                        } 
                        read = read.next;
                    }
                    if (read == null)
                        throw new IllegalStateException(Messages.message("persist.notOwnerLock") + _oid + "/" + _id + " by " + tx);
                }
            } else 
                throw new IllegalStateException(Messages.message("persist.notOwnerLock") + _oid + "/" + _id + " by " + tx);

            // Notify all waiting transactions that they may attempt to
            // acquire lock. First one to succeed wins (or multiple if
            // waiting for read lock).
            notifyAll();
        } catch (ThreadDeath death) {
            // This operation must never fail, not even in the
            // event of a thread death
            release(tx);
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
    synchronized void delete(final TransactionContext tx) {

        if (tx != _writeLock)
            throw new IllegalStateException(Messages.message("persist.notOwnerLock") + " oid:" + _oid + "/" + _id + " by " + tx);

        if (LOG.isDebugEnabled()) {
            LOG.debug ("Delete " + this.toString() + " by " + tx);
        }

        try {
            // Mark lock as unlocked and deleted, notify all waiting transactions
            _deleted = true;
            //_writeLock = null;
            _object = null;
            notifyAll();
        } catch (ThreadDeath death) {
            // Delete operation must never fail, not even in the
            // event of a thread death
            release(tx);
            throw death;
        }
    }

    synchronized void invalidate(final TransactionContext tx) {
        
        if (tx != _writeLock) 
            throw new IllegalStateException(Messages.message("persist.notOwnerLock") + " oid:" + _oid + "/" + _id + " by " + tx);

        if (LOG.isDebugEnabled()) {
            LOG.debug ("Delete " + this.toString() + " by " + tx);
        }

        _invalidated = true;
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
    private void detectDeadlock(final TransactionContext waitingTx, final int numOfRec)
    throws LockNotGrantedException {
        ObjectLock waitOn;

        if (numOfRec <= 0) return;

        // Inspect write lock and all read locks (the two are mutually exclusive).

        // For each lock look at all the waiting transactions( waitOn) and
        // determine whether they are currently waiting for a lock. A transaction
        // is waiting for a lock if it has called acquire() and has not
        // returned from the call.

        // If one of these locks is locked (read or write) by this transaction,
        // a dead lock has been detected. Recursion is necessary to prevent
        // indirect dead locks (A locked by B, B locked by C, C acquires lock on A)

        // Only the last lock attempt in a dead-lock situation will cancel.

        if (_writeLock != null) {
            // _writeLock is the blocking transaction. We are only interested in
            // a blocked transacrtion.
            waitOn = _writeLock.getWaitOnLock();
            if (waitOn != null) {
                LinkedTx read;

                // Is the blocked transaction blocked by the transaction locking
                // this object? This is a deadlock.
                if (waitOn._writeLock == waitingTx) {
                    throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                }
                read = waitOn._readLock;
                while (read != null) {
                    if (read.tx == waitingTx)
                        throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                    read = read.next;
                }
                waitOn.detectDeadlock(waitingTx, numOfRec - 1);
            }
        } else {
            LinkedTx lock;

            lock = _readLock;
            while (lock != null) {
                // T1 trying to acquire lock on O1, which is locked by T2
                // T2 trying to acauire lock on O1, T1 is waiting on O1

                // lock is the blocking transaction. We are only interested in
                // a blocked transacrtion.
                waitOn = lock.tx.getWaitOnLock();
                if ((waitOn != null) && (lock.tx != waitingTx)) {
                    LinkedTx read;

                    if (waitOn._writeLock == waitingTx) {
                        throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                    }
                    read = waitOn._readLock;
                    while (read != null) {
                        if (read.tx == waitingTx)
                            throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                        read = read.next;
                    }
                    waitOn.detectDeadlock(waitingTx, numOfRec - 1);
                }
                lock = lock.next;
            }
        }
    }


    /**
     * Remove the transaction from the waiting list (both read and write).
     */
    private void removeWaiting(final TransactionContext tx) {

        try {
            if (_writeWaiting != null) {
                if (_writeWaiting.tx == tx) {
                    _writeWaiting = _writeWaiting.next;
                } else {
                    LinkedTx wait;
                    
                    wait = _writeWaiting;
                    while (wait.next != null) {
                        if (wait.next.tx == tx) {
                            wait.next = wait.next.next;
                            break;
                        }
                        wait = wait.next;
                    }
                }
            }
            if (_readWaiting != null) {
                if (_readWaiting.tx == tx) {
                    _readWaiting = _readWaiting.next;
                } else {
                    LinkedTx wait;
                    
                    wait = _readWaiting;
                    while (wait.next != null) {
                        if (wait.next.tx == tx) {
                            wait.next = wait.next.next;
                            break;
                        }
                        wait = wait.next;
                    }
                }
            }
            if (_deleted && (_readWaiting == null) && (_writeWaiting == null) && (_confirmWaiting == null)) {
                _deleted = false;
            }
        } catch (ThreadDeath death) {
            // This operation must never fail, not even in the
            // event of a thread death
            removeWaiting(tx);
            throw death;
        }
    }


    public String toString() {
        return _oid.toString() + "/" + _id + " " + (((_readLock == null) ? "-" : "R") + "/" +
                                         ((_writeLock == null) ? "-" : "W"));
    }


    /**
     * Object uses to hold a linked list of transactions holding
     * write locks or waiting for a read/write lock.
     */
    static class LinkedTx {
        TransactionContext tx;

        LinkedTx           next;

        LinkedTx(final TransactionContext tx, final LinkedTx next) {
            this.tx = tx;
            this.next = next;
        }
    }
}


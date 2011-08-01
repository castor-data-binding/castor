/*
 * Copyright 2011 Assaf Arkin, Thomas Yip, Wensheng Dou, Ralf Joachim
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
 *
 * $Id$
 */
package org.exolab.castor.persist;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.persist.TransactionContext;
import org.exolab.castor.jdo.LockNotGrantedException;

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
 * #delete} first, then {@link #release}.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:wsdou55 AT gmail DOT com">Wensheng Dou</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class ObjectLock implements DepositBox {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getFactory().getInstance(ObjectLock.class);

    /** Milliseconds per second. */
    private static final long ONE_SECOND = 1000;
    
    /** The idcount for all the instances. */
    private static AtomicInteger _idcount = new AtomicInteger(0);
    
    //-----------------------------------------------------------------------------------    

    /** The id of this instance. */
    private final int _id;

    /** OID of the entity locked. */
    private OID _oid;

    /** Actual values of the entity locked. */
    private Object[] _object;

    private Object[] _expiredObject;

    /** Associated version of the entity. */
    private long _version;

    /** If the object is deleted, then it is true. */
    private boolean _deleted;

    private boolean _invalidated;

    private boolean _expired;
    
    /** Number of transactions which are interested to invoke method on this lock.
     *  If the number is zero, and the lock isFree(), then it is safe to dispose this lock. */
    private int _gateCount;

    /** This lock is used to protected the ObjectLock instance. */
    private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock();
    
    /** Waiting for others transactions. */
    private final Condition _conditionForLock = _lock.writeLock().newCondition();
    
    /** Write lock on this object. Refers to the transaction that has
     *  acquired the write lock. Read and write locks are mutually
     *  exclusive. */
    private TransactionContext _writeTransaction;

    /** Read locks on this object. A list of all transactions
     *  that have acquired a read lock. Read and write locks are
     *  mutually exclusive. */
    private final Set<TransactionContext> _readTransactions =
        new HashSet<TransactionContext>();

    /** Set of all transactions waiting for a read lock. Attempts to
     *  acquire read lock while object has write lock will be recorded
     *  here. When write lock is released, all read locks will acquire. */
    private final Set<TransactionContext> _readWaitingTransactions =
        new HashSet<TransactionContext>();

    /** Set of all transactions waiting for a write lock (including
     *  waiting for upgrade from read lock). Attempts to acquire a
     *  write lock while object has a read lock will be recorded here.
     *  When read lock is released, the first write lock will acquire. */
    private final Set<TransactionContext> _writeWaitingTransactions =
        new HashSet<TransactionContext>();

    private TransactionContext _confirmWaitingTransaction;

    private LockAction _confirmWaitingAction;

    //-----------------------------------------------------------------------------------    

    /**
     * Create a new lock for the specified object. Must not create two
     * locks for the same object. This will be the object returned from
     * a successful call to one of the <tt>acquire</tt>.
     *
     * @param oid The object to create a lock for
     */
    protected ObjectLock(final OID oid) {
        _oid = oid;

        // give each instance an unique id, for debug only
        _id = _idcount.getAndIncrement();
    }
    
    protected ObjectLock(final OID oid, final Object[] object, final long version) {
        this(oid);
        
        _object = object;
        _version = version;
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Get OID of the entity locked.
     * 
     * @return OID of the entity locked.
     */
    public OID getOID() {
        return _oid;
    }

    /** 
     * Set OID of the entity locked.
     * 
     * @param oid OID of the entity locked.
     */
    protected void setOID(final OID oid) {
        _oid = oid;
    }

    public Object[] getObject() {
        if ((_expiredObject != null) && (_object == null)) {
            return _expiredObject;
        }
        return _object; 
    }
   
    public Object[] getObject(final TransactionContext tx) {
        try {
            _lock.readLock().lock();
            if (!hasLock(tx)) {
                throw new IllegalArgumentException("Transaction tx does not own this lock!");
            }
            return _object;
        } finally {
            _lock.readLock().unlock();
        }
    }

    public void setObject(final TransactionContext tx, final Object[] object, final long version) {
        // initialize cache expiration flag to false
        try {
            _lock.writeLock().lock();
            _deleted = false;
            _expired = false;
            _expiredObject = null;
            
            if (_writeTransaction == tx) {
                _version = version;
                _object = object;
            } else if (_confirmWaitingTransaction == tx) {
                _version = version;
                _object = object;
                if (_confirmWaitingAction == LockAction.READ) {
                    _readTransactions.add(tx);
                } else {
                    _writeTransaction = tx;
                }
                _confirmWaitingTransaction = null;
                _conditionForLock.signalAll();
            } else {
                throw new IllegalArgumentException(
                        "Transaction tx does not own this lock, " + toString() + "!");
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public long getVersion() {
        try {
            _lock.readLock().lock();
            return _version;
        } finally {
            _lock.readLock().unlock();
        }
    }
    
    public void setVersion(final long version) {
        _lock.writeLock().lock();
        _version = version;
        _lock.writeLock().unlock();
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Indicate that a transaction is interested in this lock. A transaction should call
     * this method if it is going to change the state of this lock (by calling acquire,
     * update or release.) The method should be synchronized externally to avoid race
     * condition. enter and leave should be called exactly the same number of time.
     */
    protected void enter() {
        _gateCount++;
    }

    /**
     * Indicate that a transaction is not interested to change the state of this lock
     * anymore (ie, will not call either acquire, update, release or delete). The
     * method should be synchronized externally.
     */
    protected void leave() {
        _gateCount--;
    }

    /**
     * Are there transactions interested in this lock?
     * 
     * @return <code>true</code> if there is any transaction called {@link #enter},
     *         but not yet called {@link #leave}.
     */ 
    protected boolean isEntered() {
        return (_gateCount != 0);
    }

    //-----------------------------------------------------------------------------------    

    /**
     * The TransactionContext tries to acquire a lock for LockAction.
     *
     * @param tx The TransactionContext
     * @param action The LockAction
     * @param timeout The timeout for acquiring the lock
     * @throws LockNotGrantedException If the lock is not granted
     */
    protected void acquireLock(final TransactionContext tx,
            final LockAction action, final int timeout)
    throws LockNotGrantedException {
        long endtime = Long.MAX_VALUE;
        if (timeout > 0) {
            endtime = System.currentTimeMillis() + timeout * ONE_SECOND;
        }

        try {
            _lock.writeLock().lock();
            while (true) {
                // cases to consider:
                // 3/ waitingForConfirmation exist
                //      then, we wait
                // 4/ need a read, and objectLock has something
                //      then, we get lock and return
                // 5/ need a read, and objectLock has nothing
                //      then, we return and wait for confirmation
                // 6/ need a write
                //      then, we return and wait for confirmation
                // 7/ we're in some kind of lock, or waiting, exception
                // 1/ write exist 
                //      then, put it tx into read/write waiting
                // 2/ read exist
                //      then, put it read, or write waiting
                if (_deleted && (action != LockAction.CREATE)) {
                    throw new ObjectDeletedWaitingForLockException("Object deleted");
                } else if (_confirmWaitingTransaction != null) {
                    // other thread haven't finished
                    try {
                        _conditionForLock.await();                        
                    } catch (InterruptedException e) {
                        throw new LockNotGrantedException("Thread interrupted acquiring lock!", e);
                    } 
                } else if ((_writeTransaction == null) && _readTransactions.isEmpty()) {
                    // no transaction hold any lock, 
                    if ((_object != null) && (action == LockAction.READ)) {
                        _readTransactions.add(tx);
                    } else {
                        _confirmWaitingTransaction = tx;
                        _confirmWaitingAction = action;
                    }
                    return;
                } else {
                    if (action == LockAction.CREATE) {
                        throw new LockNotGrantedException("Lock already exist!");
                    } else if (_writeTransaction == tx) {
                        return;
                    } else if (!_readTransactions.isEmpty() && (action == LockAction.READ)) {
                        if (!_readTransactions.contains(tx)) {
                            _readTransactions.add(tx);
                        }
                        return;
                    }
                    waitingForLock(tx, (action != LockAction.READ), endtime);
                }
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    /**
     * Other transaction holding writeLock, waits for write or, other transaction 
     * holding readLock, waiting for read.
     * 
     * @param tx The transaction
     * @param write true, if it is write
     * @param endtime when the transaction should end
     * @throws LockNotGrantedException Lock could not be granted in the specified timeout
     *         or a dead lock has been detected
     * @throws ObjectDeletedWaitingForLockException
     */
    private void waitingForLock(final TransactionContext tx, final boolean write,
            final long endtime) throws LockNotGrantedException {       
        // Don't wait if timeout is zero
        if (System.currentTimeMillis() > endtime) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Timeout on " + this.toString() + " by " + tx);
            }
            throw new LockNotGrantedException(
                    (write ? "persist.writeLockTimeout" : "persist.readLockTimeout")
                    + _oid + "/" + _id + " by " + tx);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Waiting on " + this.toString() + " by " + tx);
        }
        
        try {
            // Detect possibility of dead-lock. Must remain in wait-on-lock
            // position until lock is granted or exception thrown.
            tx.setWaitOnLock(this);
            detectDeadlock(tx, 10);
            
            // Must wait for lock and then attempt to reacquire
            if (write) {
                _writeWaitingTransactions.add(tx);
            } else {
                _readWaitingTransactions.add(tx);
            }
            
            // Wait until notified or timeout elapses. Must detect
            // when notified but object deleted (i.e. locks released)
            // All waiting transactions are notified at once, but once
            // notified a race condition starts to acquire new lock
            try {
                long waittime = endtime - System.currentTimeMillis();
                _conditionForLock.await((waittime < 0) ? 0 : waittime, TimeUnit.MILLISECONDS);
            } catch (InterruptedException except) {
                // If the thread is interrupted, come out with the proper message
                throw new LockNotGrantedException(
                        (write ? "persist.writeLockTimeout" : "persist.readLockTimeout")
                        + _oid + "/" + _id + " by " + tx, except);
            }

            if (_deleted) {
                // If object has been deleted while waiting for lock, report deletion.
                throw new ObjectDeletedWaitingForLockException(
                        "object deleted" + _oid + "/" + _id + " by " + tx);
            } 
        } finally {
            removeWaiting(tx);
            tx.setWaitOnLock(null);
        }
    }

    /**
     * Remove the transaction from the waiting list (both read and write).
     * @param tx the TransactionContext
     */
    private void removeWaiting(final TransactionContext tx) {
        try {
            _writeWaitingTransactions.remove(tx);
            _readWaitingTransactions.remove(tx);
        } catch (ThreadDeath death) {
            // This operation must never fail, not even in the
            // event of a thread death
            removeWaiting(tx);
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
     * @param numOfRec the deep of recursion
     * @throws LockNotGrantedException when a dead lock has been detected
     */
    private void detectDeadlock(final TransactionContext waitingTx, final int numOfRec)
        throws LockNotGrantedException {
        if (numOfRec <= 0) {
            return;
        }

        // Inspect write lock and all read locks (the two are mutually exclusive).

        // For each lock look at all the waiting transactions( waitOn) and
        // determine whether they are currently waiting for a lock. A transaction
        // is waiting for a lock if it has called acquire() and has not
        // returned from the call.

        // If one of these locks is locked (read or write) by this transaction,
        // a dead lock has been detected. Recursion is necessary to prevent
        // indirect dead locks (A locked by B, B locked by C, C acquires lock on A)

        // Only the last lock attempt in a dead-lock situation will cancel.

        if (_writeTransaction != null) {
            // _writeTransaction is the blocking transaction. We are only interested in
            // a blocked transaction.
            ObjectLock waitOn = _writeTransaction.getWaitOnLock();
            if (waitOn != null) {
                // Is the blocked transaction blocked by the transaction locking
                // this object? This is a deadlock.
                if (waitOn._writeTransaction == waitingTx) {
                    throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                }
                if (waitOn._readTransactions.contains(waitingTx)) {
                    throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                }
                waitOn.detectDeadlock(waitingTx, numOfRec - 1);
            }
        } else {
            Iterator<TransactionContext> iterReadTx = _readTransactions.iterator();
            TransactionContext tx;
            while (iterReadTx.hasNext()) {
                // T1 trying to acquire lock on O1, which is locked by T2
                // T2 trying to acquire lock on O1, T1 is waiting on O1

                // lock is the blocking transaction. We are only interested in
                // a blocked transaction.
                tx = iterReadTx.next();
                ObjectLock waitOn = tx.getWaitOnLock();
                if ((waitOn != null) && (tx != waitingTx)) {
                    if (waitOn._writeTransaction == waitingTx) {
                        throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                    }
                    if (waitOn._readTransactions.contains(waitingTx)) {
                        throw new LockNotGrantedException(Messages.message("persist.deadlock"));
                    }
                    waitOn.detectDeadlock(waitingTx, numOfRec - 1);
                }
            }
        }
    }

    public void confirm(final TransactionContext tx, final boolean succeed) {
        // cases to consider:
        // 1/ not in waitingForConfirmation
        // 2/ load_read,
        //         downgrade the lock
        // 3/ else
        //         move confirmation and
        //         notify()
        try {
            _lock.writeLock().lock();
            if (_confirmWaitingTransaction == tx) {
                if (succeed) {
                    if (_confirmWaitingAction == LockAction.READ) {
                        _readTransactions.add(tx);
                    } else {
                        _writeTransaction = tx;
                    }
                }
                _confirmWaitingTransaction = null;
                _conditionForLock.signalAll();
            } else if (_confirmWaitingTransaction == null) {
                if (!succeed) {
                    if (_writeTransaction != null) {
                        _object = null;
                        _version =  System.currentTimeMillis();
                    } else {
                        _readTransactions.remove(tx);
                    }
                }
                _conditionForLock.signalAll();
            } else {
                throw new IllegalStateException(
                        "Confirm transaction does not match the locked transaction");        
            }
        } finally {
            _lock.writeLock().unlock();
        }
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
     * ObjectDeletedWaitingForLockException} is thrown. To prevent dead locks, a
     * transaction must only call this method for any given object
     * from a single thread and must mark the lock it is trying to
     * acquire and return it from a call to {@link
     * TransactionContext#getWaitOnLock} if the call to this method
     * has not returned yet. If a read lock is available for the
     * transaction and a write lock is requested, the read lock is
     * cancelled whether or not the write is acquired.
     *
     * @param tx The transaction requesting the lock
     * @param timeout Timeout waiting to acquire lock (in milliseconds),
     *  zero for no waiting
     * @throws LockNotGrantedException Lock could not be granted in
     *  the specified timeout or a dead lock has been detected
     */
    protected void upgrade(final TransactionContext tx, final int timeout)
        throws LockNotGrantedException {
        // Note: This method must succeed even if an exception is thrown
        // in the middle. An exception may be thrown by a Thread.stop().
        // Must make sure not to lose consistency.
        try {
            _lock.writeLock().lock();
            if (_confirmWaitingTransaction != null) {
                throw new IllegalStateException(
                        "Internal error: acquire when confirmWaiting is not null");
            }
            if (!hasLock(tx)) {
                throw new IllegalStateException(
                        "Transaction didn't previously acquire this lock");
            }

            long endtime = (timeout > 0)
                         ? System.currentTimeMillis() + timeout * ONE_SECOND
                         : Long.MAX_VALUE;
            while (true) {
                // Repeat forever until lock is acquired or timeout
                if (_writeTransaction == tx) {
                    // Already have write lock, can acquire object
                    return;
                } else if ((_writeTransaction == null)
                        && (_readTransactions.contains(tx)) && (_readTransactions.size() == 1)) {
                    // Upgrading from read to write, no other locks, can upgrade
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Acquired on " + this.toString() + " by " + tx);
                    }
                    _writeTransaction = tx;
                    _readTransactions.clear();
                    return;
                } else {
                    waitingForLock(tx, true, endtime);
                }
            } 
        } finally {
            _lock.writeLock().unlock();
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
    public void release(final TransactionContext tx) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Release " + this.toString() + " by " + tx);
        }

        try {
            _lock.writeLock().lock();
            tx.setWaitOnLock(null);
            if (_writeTransaction == tx) {
                _writeTransaction = null;
                if (_invalidated || _deleted) {
                    _version = System.currentTimeMillis();
                    // save a copy of the expired objects contents;
                    // this will be used to expire all contained objects
                    if (_expired) {
                        _expiredObject = _object;
                    }
                    _object = null;
                }
                _invalidated = false;
            } else if (!_readTransactions.remove(tx)) {
                throw new IllegalStateException(Messages.message(
                    "persist.notOwnerLock") + _oid + "/" + _id + " by " + tx);
            }

            // Notify all waiting transactions that they may attempt to
            // acquire lock. First one to succeed wins (or multiple if
            // waiting for read lock).
            _conditionForLock.signalAll();
        } catch (ThreadDeath death) {
            // This operation must never fail, not even in the
            // event of a thread death
            release(tx);
            throw death;
        } finally {
            _lock.writeLock().unlock();
        }
    }

    /**
     * Informs the lock that the object has been deleted by the
     * transaction holding the write lock. The lock on the object is
     * released and all transactions waiting for a lock will
     * terminate with an {@link ObjectDeletedWaitingForLockException}.
     *
     * @param tx The transaction that holds the lock
     */
    public void delete(final TransactionContext tx) {
        try {
            _lock.writeLock().lock();
            if (_writeTransaction != tx) {
                throw new IllegalStateException(Messages.message(
                        "persist.notOwnerLock") + " oid:" + _oid + "/" + _id + " by " + tx);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug ("Delete " + this.toString() + " by " + tx);
            }

            // Mark lock as unlocked and deleted
            _deleted = true;
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public void invalidate(final TransactionContext tx) {
        try {
            _lock.writeLock().lock();
            if (_writeTransaction != tx) {
                throw new IllegalStateException(Messages.message(
                        "persist.notOwnerLock") + " oid:" + _oid + "/" + _id + " by " + tx);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug ("Invalidate " + this.toString() + " by " + tx);
            }

            _invalidated = true;
        } finally {
            _lock.writeLock().unlock();
        }
    }

    /**
     * Indicate that object needs to be expired from the cache.
     */
    public void expire() {
        _expired = true; 
    }
   
    /**
     * Indicate that object has been removed from the cache. Perform any 
     * post expiration cleanup.  In particular, remove the reference to any
     * saved cached objects.
     */
    protected void expired() { 
        _expired = false; 
        _expiredObject = null;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Has this entity been expired from the cache?
     * 
     * @return <code>true</code> if this entity has been expired from the cache.
     */
    protected boolean isExpired() {
        return _expired; 
    }

    /**
     * @return true if this object can be safely disposed. An ObjectLock
     * can be safely disposed if and only if the no transaction is 
     * holding or waiting any lock, nor any transaction isEntered.
     */
    protected boolean isDisposable() {
        return !isEntered() && isFree();
    }

    /**
     * Return true if and only if this lock can be safely disposed.
     *
     * @return True if no lock and no waiting.
     */
    private boolean isFree() {
        return ((_writeTransaction == null) && (_readTransactions.isEmpty())
                && (_writeWaitingTransactions.isEmpty()) && (_readWaitingTransactions.isEmpty())
                && (_confirmWaitingTransaction == null) && !hasWaiter());
    }

    /**
     * @return true if no transactions are waiting for the lock.
     */
    private boolean hasWaiter () {
        try {
            _lock.writeLock().lock();
            return _lock.hasWaiters(_conditionForLock);
        } finally {
            _lock.writeLock().unlock();
        }
    }
    
    /**
     * Returns true if the transaction holds a read or write lock on the object. This
     * method is an efficient mean to determine whether a lock is required, or if the
     * object is owned by the transaction.
     *
     * @param tx The transaction.
     * @return <code>true</code> if the transaction has a lock on this object.
     */
    protected boolean hasLock(final TransactionContext tx) {
        if (_writeTransaction == tx) { return true; }
        if (_confirmWaitingTransaction == tx) { return true; }
        return _readTransactions.contains(tx);
    }

    /**
     * Returns true if the transaction holds a write lock on the object. This method is
     * an efficient mean to determine whether a lock is required, or if the object is
     * owned by the transaction.
     *
     * @param tx The transaction.
     * @return <code>true</code> if the transaction has a write lock on this object.
     */
    protected boolean hasWriteLock(final TransactionContext tx) {
        if (_writeTransaction == tx) { return true; }

        if (_confirmWaitingTransaction == tx) {
            if ((_confirmWaitingAction == LockAction.WRITE)
                    || (_confirmWaitingAction == LockAction.CREATE)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isExclusivelyOwned(final TransactionContext tx) {
        if (_writeTransaction == tx) { return true; }
        if (_readTransactions.contains(tx) && (_readTransactions.size() == 1)) { return true; }
        return false;
    }
    
    //-----------------------------------------------------------------------------------    

    public String toString() {
        return _oid.toString() + "/" + _id + " "
                + (((_readTransactions.isEmpty()) ? "-" : "R") + "/"
                + ((_writeTransaction == null) ? "-" : "W"));
    }

    //-----------------------------------------------------------------------------------    
}


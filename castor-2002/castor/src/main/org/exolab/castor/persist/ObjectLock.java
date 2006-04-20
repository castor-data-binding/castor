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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist;


import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.util.Messages;
import org.exolab.castor.jdo.ObjectDeletedException;


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
 * LockNotGrantedException} is thrown. If the object has been delete
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
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 * @version $Revision$ $Date$
 * @see Key
 */

final class ObjectLock {


    final static short ACTION_READ      = 1;

    final static short ACTION_WRITE     = 2;

    final static short ACTION_CREATE    = 3;

    final static short ACTION_UPDATE    = 4;

    final static short LOCK_TO_LOCK     = 1;

    final static short LOCK_TO_CACHE    = 2;

    final static short LOCK_TO_DESTROY  = 3;

    /* for debug only */
    private static int idcount = 0;

    /* for debug only */
    private final static int[] lock = new int[0];

    /* for debug only */
    private int        _id;

    /**
     * Indicates if this object lock is run in debug mode. If true, messages
     * are emitted to System.out
     */
    private final static boolean TRACE = false;

    /**
     * Indicates if an object lock is allowed to be acquired multiple times.
     * If set to false, any acquiring on the same lock using the same {@link Key} 
     * after a succeed acquiring will result in LockUpgradeFailedException. 
     * Assure and Upgrade should be used instead to avoid such exception to
     * be thrown.
     */
    private final static boolean REACQUIRE_ALLOWED = false;

    /**
     * The object's identity.
     */
    private Object              _oid;

    /**
     * The object being locked.
     */
    private Object              _object;

    /**
     * The new version of the object being locked. 
     * If the commit is sucessful this object will become the locked 
     * object. If it fails, this value will set null.
     */
    private Object              _newObject;

    /**
     * The Key that being used to load the the current value, 
     * or being used to modified and committed sucessfully
     */
    private Key                 _masterKey;

    /**
     * The latest timestamp of the value is being loaded, or being 
     * modified and committed successfully.
     */
    private long                _timestamp;

    /**
     * Write lock on this object. Refers to the transaction that has
     * acquired the write lock. Read and write locks are mutually
     * exclusive.
     */
    private Key                 _writeLock;

    /**
     * Read locks on this object. A LinkedTx list of all transactions
     * that have acquired a read lock. Read and write locks are
     * mutually exclusive.
     */
    private LinkedTx            _readLock;

    /**
     * List of all transactions waiting for a read lock. Attempts to
     * acquire read lock while object has write lock will be recorded
     * here. When write lock is released, all read locks will acquire.
     */
    private Key                 _readWaiting;

    /**
     * List of all transactions waiting for a write lock (including
     * waiting for upgrade from read lock). Attempts to acquire a
     * write lock while object has a read lock will be recorded here.
     * When read lock is released, the first write lock will acquire.
     */
    private Key                 _writeWaiting;

    /**
     * Indicates if the object being locked is deleted by the Key 
     * holding the write lock
     */
    private boolean             _deleted;

    /**
     * Indciates the object being locked have become invalid. The
     * locked object should not be used to any transaction
     */
    private boolean             _invalidated;

    /**
     * Indicates that the object being locked is set to be evicted.
     */
    private boolean             _evicted;

    /**
     * Number of transactions which are interested to invoke method 
     * on this lock.
     * If the number is zero, and the lock isFree(), then it is safe
     * dispose this lock.
     */
    private int                 _gateCount;

    /**
     * Indicates how many threads had invoked Object.wait() for this 
     * lock.
     */
    private int                 _waitCount;

    /**
     * The Key that is in the vital point of this object lock.
     * Vital point happens when the object lock is waiting for a
     * thread that holding a key to set the inital value into the
     * object lock. In the perido that a thread've gotten the lock
     * and set the value (or fail and release the lock), the state 
     * of the object lock is unknown. As a result, every object
     * that is waiting for a lock in vital point state will be 
     * blocked.
     */
    private Key                 _vital;

    /**
     * The pending action of a key in vital point
     */
    private short               _vitalAction;

    /**
     * Create a new lock for the specified object. Must not create two
     * locks for the same object. This will be the object returned from
     * a successful call to {@link #acquire}.
     *
     * @param obj The object to create a lock for
     */
    ObjectLock( Object oid ) {
        _oid = oid;

        if ( TRACE ) {
            // give each instance of ObjectLock an id, for debug only
            synchronized ( lock ) {
                _id = idcount;
                idcount++;
            }
        }
    }

    /**
     * Return the object's OID.
     */
    Object getOID() {
        return _oid;
    }

    /** 
     * Set OID of this lock to new value.
     *
     */
    void setOID( Object oid ) {
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
    synchronized void enter( Key key ) {
        _gateCount++;
    }

    /**
     * Indicate that a transaction is not interested to change the 
     * state of this lock anymore. (ie, will not call either acquire
     * update, release or delete.)
     *
     * @return the action that typeInfo to take
     */
    synchronized short leave( Key key ) {
        _gateCount--;
        if ( _object == null && _masterKey == key )
            release( key );

        if ( _gateCount != 0 ) {
            return LOCK_TO_LOCK;
        } else {
            if ( !isFree() )
                return LOCK_TO_LOCK;
            else if ( _deleted || _invalidated )
                return LOCK_TO_DESTROY;
            else
                return LOCK_TO_CACHE;
        }
    }

    /**
     * Determines if the specified key is the key being used to load 
     * the the current value, or being used to modified and committed 
     * sucessfully.
     */
    synchronized boolean isMaster( Key key ) {
        return _masterKey == key;
    }

    /**
     * Return true if there is any transaction called {@ink enter}, 
     * but not yet called {@link leave}.
     */
     /*
    boolean isEntered() {
        return _gateCount != 0;
    }*/

    /**
     * Return true if this object can be safely disposed. An ObjectLock
     * can be safely disposed if and only if the no transaction is 
     * holding any lock, nor any transaction isEntered.
     */
     /*
    boolean isDisposable() {
        return _gateCount == 0 && isFree() && _waitCount == 0;
    }*/

    /**
     * Returns true if the transaction holds a read or write lock on
     * the object. This method is an efficient mean to determine whether
     * a lock is required, or if the object is owned by the transaction.
     *
     * @param key The transaction
     * @param write True if must have a write lock
     * @return True if the transaction has a lock on this object
     */
    public synchronized boolean hasLock( Key key, boolean write ) {

        if ( _writeLock == key )
            return true;

        if ( write )
            return false;

        LinkedTx read = _readLock;
        while ( read != null ) {
            if ( read.key == key )
                return true;
            read = read.next;
        }
        return false;
    }

    /**
     * Return true if and no key is current waiting or holding
     * any the current locks
     *
     * @return True if no lock and no waiting
     */
    private boolean isFree() {
        return ( _writeLock == null && _readLock == null && 
                 _writeWaiting == null && _readWaiting == null );
    }

    /*
    boolean isExclusivelyOwned( Key key ) {
        LinkedTx read;

        if ( _writeLock == null && _readLock == null )
            return false;

        if ( _writeLock == null && _readLock.key == key && 
                        _readLock.next.key == null )
            return true;

        if ( _writeLock == key && _readLock == null )
            return true;

        return false;
    }*/
    synchronized void downgrade( Key key ) {
        if ( _writeLock == key ) {
            _writeLock = null;
            _readLock  = new LinkedTx( key, _readLock );
        } 
    }

    synchronized void acquire( Key key, boolean write, int timeout ) 
            throws LockNotGrantedException, ObjectDeletedWaitingForLockException {
        
        long endtime = timeout>0? System.currentTimeMillis() + timeout*1000: Long.MAX_VALUE;

        while ( true ) {
            if ( _deleted ) {
                throw new ObjectDeletedWaitingForLockException("Object deleted");
            } else if ( _readLock == null && _writeLock == null && write ) {
                // the only key now, get write lock immediately
                _writeLock = key;
            } else if ( _readLock == null && _writeLock == null && !write ) {
                // the only key now, get read lock immediately
                _readLock = new LinkedTx( key, null );
                return;
            } else if ( _writeLock == null && _readLock.key == key 
                    && _readLock.next == null && write ) {
                // upgrade to write lock immediately
                _readLock = null;
                _writeLock = key;
                return;
            } else if ( _writeLock == key ) {
                // this transaction already holding a write lock
                if ( REACQUIRE_ALLOWED )
                    return;
                else
                    throw new LockUpgradeFailedException( 
                              Messages.message("persist.lockReacquireFailed") );
            } else if ( _readLock.key == key && !write ) {
                // this transaction already holding a read lock
                if ( REACQUIRE_ALLOWED )
                    return;
                else
                    throw new LockUpgradeFailedException( 
                              Messages.message("persist.lockReacquireFailed") );
            } else if ( _readLock == null && !write ) {
                // other transaction is holding read lock
                acquireReadLockInternal( key, timeout );
            } else if ( _readLock == null && write ) {
                // other transaction is holding write lock
                acquireWriteLockInternal( key, timeout );
            } else if ( _readLock != null ) {
                // all other cases, find out if key has a read lock already
                LinkedTx linked = _readLock;
                boolean alreadyReadLocked = false;
                while ( linked != null ) {
                    if ( linked.key == key ) {
                        alreadyReadLocked = true;
                        break;
                    }
                    linked = linked.next;
                }
                if ( write ) {
                    // other transaction is holding write lock, wait for upgrade or
                    // acquire a new write lock
                    if ( alreadyReadLocked )
                        upgradeLockInternal( key, timeout );
                    else 
                        acquireWriteLockInternal( key, timeout );
                } else {
                    // this or other transaction already has read lock, add itself 
                    // to the readlock list or complaint about reentrance
                    if ( alreadyReadLocked ) {
                        if ( REACQUIRE_ALLOWED )
                            return;
                        else
                            throw new LockUpgradeFailedException( Messages.message("persist.lockReacquireFailed") );
                    } else
                        _readLock = new LinkedTx( key, _readLock );
                }
            }
        }
    }

    /*
    synchronized void acquireCreateLock( Key key ) 
            throws LockNotGrantedException {

        while ( true ) {
            // cases to consider:
            // 1/ waitingForConfirmation exist
            // 2/ lock can't be granted, throw LockNotGrantedException
            // 3/ lock can be granted
            //      then, we return and wait for confirmation
            if ( _deleted || _confirmWaiting != null ) {
                // other thread is loading or creating object and haven't finished
                try {
                    _waitCount++;
                    wait();
                    while ( _deleted ) {
                        wait();
                    }
                } catch ( InterruptedException e ) {
                    throw new LockNotGrantedException("Thread interrupted acquiring lock!");
                } finally {
                    _waitCount--;
                }
            } else if ( _readLock != null || _writeLock != null ) {
                throw new LockNotGrantedException("Lock already exist!");
            } else {
                _confirmWaiting = key;
                _confirmWaitingAction = ACTION_CREATE;
                return;
            }
        }
    }*/

    /*
    // probaraly we just don't need update....
    synchronized void acquireUpdateLock( Key key, int timeout ) 
            throws LockNotGrantedException, ObjectDeletedException,
            ObjectDeletedWaitingForLockException {

        long endtime = timeout<0? System.currentTimeMillis() + timeout*1000: Long.MAX_VALUE;
        while ( true ) {
            try {
                // case to consider:
                // 1/ waitingForConfirmation exist
                // 2/ lock can be granted, and _object is not empty
                //      then, we return and wait for confirmation
                // 3/ lock can not granted, wait

                if ( _deleted || _confirmWaiting != null ) {
                    try {
                        _waitCount++;
                        wait();
                        //if ( _deleted ) {
                        //    throw new ObjectDeletedWaitingForLockException("Object deleted!");
                    } catch ( InterruptedException e ) {
                        throw new LockNotGrantedException("Thread interrupted acquiring lock!");
                    } finally {
                        _waitCount--;
                    }
                } else if ( _writeLock == key ) {
                    return;
                } else if ( _writeLock == null && _readLock == null ) {
                    // can get the lock now
                    _confirmWaiting = key;
                    _confirmWaitingAction = ACTION_UPDATE;
                    return;
                } else {
                    if ( timeout == 0 ) {
                        if ( TRACE )
                            System.out.println( "Timeout on " + this.toString() + " by " + key );
                        throw new LockNotGrantedException( "persist.writeLockTimeout" );
                    }
                    if ( TRACE )
                        System.out.println( "Waiting on " + this.toString() + " by " + key );
                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.
                    key.setWaitOnLock( this );
                    detectDeadlock( key, 10 );
                    
                    // Must wait for lock and then attempt to reacquire
                    key.setNext( _writeWaiting );
                    _writeWaiting = key;
                    
                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    long clock = System.currentTimeMillis();
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait( waittime<0? 0: waittime );
                    } catch ( InterruptedException except ) {
                        // If the thread is interrupted, come out with the proper message
                        throw new LockNotGrantedException( "persist.writeLockTimeout" + _oid + "/" + _id + " by " + key );
                    }

                    if ( _deleted )
                        // If object has been deleted while waiting for lock, report deletion.
                        throw new ObjectDeletedWaitingForLockException("object deleted" + _oid + "/" + _id + " by " + key);

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if ( System.currentTimeMillis() > endtime )
                        timeout = 0;

                    removeWaiting( key );
                    key.setWaitOnLock( null );
                }
            } finally {
                removeWaiting( key );
                key.setWaitOnLock( null );                
            }
        }
    }*/

    /*
    public synchronized void checkin( Key key, Object object ) {

        if ( _confirmWaiting != null && _confirmWaiting == key ) {
            _timestamp = System.currentTimeMillis();
            _object = object;
            if ( _confirmWaitingAction == ACTION_READ ) {
                _readLock = new LinkedTx( key, null );
            } else {
                _writeLock = key;
            }
            _confirmWaiting = null;
            notifyAll();
        } else if ( _writeLock != null && _writeLock == key ) {
            _timestamp = System.currentTimeMillis();
            _newobject = object;
        } else
            throw new IllegalArgumentException("Transaction key does not own this lock, "+toString()+"!");
    }*/

    public synchronized void setObject( Key key, Object object ) {
        /*
        if ( _confirmWaiting != null && _confirmWaiting == key ) {
            _timestamp = System.currentTimeMillis();
            _object = object;
            if ( _confirmWaitingAction == ACTION_READ ) {
                _readLock = new LinkedTx( key, null );
            } else {
                _writeLock = key;
            }
            _confirmWaiting = null;
            notifyAll();
        } else */
            
        if ( _writeLock != null && _writeLock == key ) {
            _timestamp = System.currentTimeMillis();
            _object = object;
        } else
            throw new IllegalArgumentException("Transaction key does not own this lock, "+toString()+"!");
    }

    public synchronized Object getObject( Key key ) {
        /*
        if ( _confirmWaiting != null && _confirmWaiting == key )
            return _object;
        else*/
        if ( _writeLock != null && _writeLock == key )
            return _object;
        else {
            LinkedTx link = _readLock;
            while ( link != null ) {
                if ( link.key == key ) 
                    return _object;
                link = link.next;
            }
            throw new IllegalArgumentException("Transaction key does not own this lock!");
        }
    }

    public synchronized void pendObject( Key key, Object object ) {

        if ( _writeLock == key )
            _newObject = object;
    }

    public synchronized long getTimeStamp() {
        return _timestamp;
    }

    /*
    synchronized void confirm( Key key, boolean succeed ) {

        // cases to consider:
        // 1/ not in waitingForConfirmation
        // 2/ load_read,
        //         downgrade the lock
        // 3/ else
        //         move confirmation and
        //         notify()
        if ( _confirmWaiting == key ) {
            if ( succeed ) {
                if ( _confirmWaitingAction == ACTION_READ ) {
                    if ( _readLock == null ) 
                        _readLock = new LinkedTx( key, null );
                } else {
                    _writeLock = key;
                }
            }
            _confirmWaiting = null;
            notifyAll();
        } else if ( _confirmWaiting == null ) {
            if ( !succeed ) {
                // remove it from readLock
                if ( _writeLock != null ) {
                    // same as delete the lock
                    _deleted = true;
                    _object = null;
                    _timestamp =  System.currentTimeMillis();
                    //_writeLock = null;
                    notifyAll();
                } else if ( _readLock == null ) {
                } else if ( _readLock.key == key )
                    _readLock = _readLock.next;
                else {
                    LinkedTx link = _readLock;
                    while ( link != null ) {
                        if ( link.next != null && link.next.key == key ) {
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
    }*/

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
     * Key#getWaitOnLock} if the call to this method
     * has not returned yet. If a read lock is available for the
     * transaction and a write lock is requested, the read lock is
     * cancelled whether or not the write is acquired.
     *
     * @param key The transaction requesting the lock
     * @param write The type of lock requested
     * @param timeout Timeout waiting to acquire lock (in milliseconds),
     *  zero for no waiting
     * @return The locked object
     * @throws LockNotGrantedException Lock could not be granted in
     *  the specified timeout or a dead lock has been detected
     * @throws ObjectDeletedWaitingForLockException The object has
     *  been deleted while waiting for the lock
     */
    synchronized void upgrade( Key key, int timeout )
            throws LockNotGrantedException, ObjectDeletedWaitingForLockException {

        // Note: This method must succeed even if an exception is thrown
        // in the middle. An exception may be thrown by a Thread.stop().
        // Must make sure not to lose consistency.
        /*
        if ( _confirmWaiting != null ) {
            IllegalStateException e = new IllegalStateException("Internal error: acquire when confirmWaiting is not null");
            throw e;
        }*/
        if ( !hasLock( key, false ) ) {
            IllegalStateException e = new IllegalStateException("Transaction doesn't previously acquire this lock");
            throw e;
        }

        long endtime = timeout<0? System.currentTimeMillis() + timeout*1000: Long.MAX_VALUE;
        while ( true ) {
            // Repeat forever until lock is acquired or timeout
            try {

                if ( _writeLock == key ) {
                    // Already have write lock, can acquire object
                    return;
                } else if ( _writeLock == null && 
                            _readLock.key == key && _readLock.next == null ) {
                    // Upgrading from read to write, no other locks, can upgrade
                    // Order is important in case thread is stopped in the middle
                    //_readLock = null;
                    if ( TRACE )
                        System.out.println( "Acquired on " + toString() + " by " + key );
                    _writeLock = key;
                    _readLock = null;
                    return;
                } else {
                    // Don't wait if timeout is zero
                    if ( timeout == 0 ) {
                        if ( TRACE )
                            System.out.println( "Timeout on " + this.toString() + " by " + key );
                        throw new LockNotGrantedException( "persist.writeTimeout" + _oid + "/" + _id + " by " + key );
                    }
                    if ( TRACE )
                        System.out.println( "Waiting on " + this.toString() + " by " + key );
                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.

                    key.setWaitOnLock( this );
                    detectDeadlock( key, 10 );
                    
                    // Must wait for lock and then attempt to reacquire
                    key.setNext( _writeWaiting );
                    _writeWaiting = key;
                    
                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    long clock = System.currentTimeMillis();
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait( waittime<0? 0: waittime );
                    } catch ( InterruptedException except ) {
                        // If the thread is interrupted, come out with the proper message
                        throw new LockNotGrantedException( "persist.writeLockTimeout" );
                    }

                    if ( _deleted )
                        // object should not be deleted, as we got lock on it
                        throw new IllegalStateException("internal error: object deleted" + _oid + "/" + _id + " by " + key);

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if ( System.currentTimeMillis() > endtime )
                        timeout = 0;
                    removeWaiting( key );
                    key.setWaitOnLock( null );
                }
            } finally {
                // Must always remove waiting transaction.
                removeWaiting( key );
                key.setWaitOnLock( null );
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
     * @param key The transaction that holds the lock
     */
    synchronized void release( Key key ) {

        if ( TRACE )
            System.out.println( "Release " + this.toString() + " by " + key );

        try {
            key.setWaitOnLock( null );
            if ( _writeLock == key ) {
                _writeLock = null;
                if ( _invalidated || _deleted ) {
                    _timestamp = System.currentTimeMillis();
                    _object = null;
                }
                _deleted = false;
                _invalidated = false;
            } else if ( _readLock != null ) {
                if ( _readLock.key == key ) {
                    _readLock = _readLock.next;
                } else {
                    LinkedTx read = _readLock;
                    while ( read != null ) {
                        if ( read.next != null && read.next.key == key ) {
                            read.next = read.next.next;
                            break;
                        } 
                        read = read.next;
                    }
                    if ( read == null )
                        throw new IllegalStateException( "persist.notOwnerLock" + _oid + "/" + _id + " by " + key );
                }
            } else 
                throw new IllegalStateException( "persist.notOwnerLock" + _oid + "/" + _id + " by " + key );

            // Notify all waiting transactions that they may attempt to
            // acquire lock. First one to succeed wins (or multiple if
            // waiting for read lock).
            notifyAll();
        } catch ( ThreadDeath death ) {
            // This operation must never fail, not even in the
            // event of a thread death
            release( key );
            throw death;
        }
    }

    /**
     * Informs the lock that the object has been deleted by the
     * transaction holding the write lock. The lock on the object is
     * released and all transactions waiting for a lock will
     * terminate with an {@link ObjectDeletedException}.
     *
     * @param key The transaction that holds the lock
     * @throws RuntimeException Attempt to delete object without
     *   acquiring a write lock
     */
    synchronized void delete( Key key ) {

        if ( key != _writeLock )
            throw new IllegalStateException( "persist.notOwnerLock oid:" + _oid + "/" + _id + " by " + key );

        if ( TRACE )
            System.out.println( "Delete " + this.toString() + " by " + key );

        try {
            // Mark lock as unlocked and deleted, notify all waiting transactions
            _deleted = true;
            //_writeLock = null;
            _object = null;
            notifyAll();
        } catch ( ThreadDeath death ) {
            // Delete operation must never fail, not even in the
            // event of a thread death
            release( key );
            throw death;
        }
    }

    synchronized void invalidate( Key key ) {
        
        if ( key != _writeLock ) 
            throw new IllegalStateException( "persist.notOwnerLock oid:" + _oid + "/" + _id + " by " + key );

        if ( TRACE )
            System.out.println( "Delete " + this.toString() + " by " + key );

        _invalidated = true;
    }

    /**
     * Internal method used for upgrade a lock from read to write.
     * Key must have already holding a read lock, 
     * and the current thread must already synchronized with "this"
     */
    private void upgradeLockInternal( Key key, int timeout )
            throws LockUpgradeFailedException, LockNotGrantedException {

        long endtime = timeout>0? System.currentTimeMillis() + timeout*1000: Long.MAX_VALUE;
        while ( true ) {
            if ( _writeLock == null && _readLock.key == key && _readLock.next == null ) {
                _readLock = null;
                _writeLock = key;
                return;
            } else {
                if ( timeout == 0 ) {
                    if ( TRACE )
                        System.out.println( "Timeout on " + this.toString() + " by " + key );
                    throw new LockUpgradeFailedException( Messages.message("persist.lockUpgradeFailed") );
                } 

                try {
                    if ( TRACE )
                        System.out.println( "Waiting on " + this.toString() + " by " + key );

                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.
                    key.setWaitOnLock( this );
                    detectDeadlock( key, 10 );

                    // Must wait for lock and then attempt to upgrade
                    key.setNext( _writeWaiting );
                    _writeWaiting = key;

                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait( waittime<0? 0: waittime );
                    } catch ( InterruptedException except ) {
                        // If the thread is interrupted, come out with the proper message
                        if ( TRACE )
                            System.out.println( "Interrupted while waiting on " + this.toString() + " by " + key );
                        throw new LockUpgradeFailedException( Messages.message("persist.lockUpgradeFailed") );
                    }

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if ( System.currentTimeMillis() > endtime )
                        timeout = 0;
                } finally {
                    removeWaiting( key );
                    key.setWaitOnLock( null );
                }
            }
        }
    }

    /**
     * Internal method used to acquire a read lock. 
     * Key must hold neither read nor write lock before calling this method,
     * and the current thread must synchronized with "this"
     */
    private void acquireReadLockInternal( Key key, int timeout )
            throws LockNotGrantedException, ObjectDeletedWaitingForLockException {

        long endtime = timeout>0? System.currentTimeMillis() + timeout*1000: Long.MAX_VALUE;
        while ( true ) {
            if ( _writeLock == null ) {
                _readLock = new LinkedTx( key, _readLock );
                return;
            } else {
                // other transaction holding writeLock, waits for write
                // or, other transaction holding readLock, waiting for read
                if ( timeout == 0 ) {
                    if ( TRACE )
                        System.out.println( "Timeout on " + this.toString() + " by " + key );
                    throw new LockNotGrantedException( Messages.message("persist.readLockTimeout") );
                } 

                try {
                    if ( TRACE )
                        System.out.println( "Waiting on " + this.toString() + " by " + key );

                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.
                    key.setWaitOnLock( this );
                    detectDeadlock( key, 10 );

                    // Must wait for lock and then attempt to upgrade
                    key.setNext( _readWaiting );
                    _readWaiting = key;

                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait( waittime<0? 0: waittime );
                    } catch ( InterruptedException except ) {
                        // If the thread is interrupted, come out with the proper message
                        if ( TRACE )
                            System.out.println( "Interrupted while waiting on " + this.toString() + " by " + key );
                        throw new LockNotGrantedException( Messages.message("persist.readLockTimeout") );
                    }

                    if ( _deleted )  {
                        // If object has been deleted while waiting for lock, report deletion.
                        if ( TRACE )
                            System.out.println( "Deleted while waiting on " + this.toString() + " by " + key );
                        throw new ObjectDeletedWaitingForLockException( Messages.message("persist.deletedWaitingForLock") );
                    }

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if ( System.currentTimeMillis() > endtime )
                        timeout = 0;
                } finally {
                    removeWaiting( key );
                    key.setWaitOnLock( null );
                }
            }
        }
    }

    /**
     * Internal method used to acquire a write lock. 
     * Key must hold neither read nor write lock before calling this method,
     * and the current thread must synchronized with "this"
     */
    private void acquireWriteLockInternal( Key key, int timeout )
            throws LockNotGrantedException, ObjectDeletedWaitingForLockException {

        long endtime = timeout>0? System.currentTimeMillis() + timeout*1000: Long.MAX_VALUE;
        while ( true ) {
            if ( _writeLock == null && _readLock == null ) {
                _writeLock = key;
                return;
            } else {
                // other transaction holding writeLock, waits for write
                // or, other transaction holding readLock, waiting for read
                if ( timeout == 0 ) {
                    if ( TRACE )
                        System.out.println( "Timeout on " + this.toString() + " by " + key );
                    throw new LockNotGrantedException( Messages.message("persist.writeLockTimeout") );
                } 

                try {
                    if ( TRACE )
                        System.out.println( "Waiting on " + this.toString() + " by " + key );

                    // Detect possibility of dead-lock. Must remain in wait-on-lock
                    // position until lock is granted or exception thrown.
                    key.setWaitOnLock( this );
                    detectDeadlock( key, 10 );

                    // Must wait for lock and then attempt to upgrade
                    key.setNext( _writeWaiting );
                    _writeWaiting = key;

                    // Wait until notified or timeout elapses. Must detect
                    // when notified but object deleted (i.e. locks released)
                    // All waiting transactions are notified at once, but once
                    // notified a race condition starts to acquire new lock
                    try {
                        long waittime = endtime - System.currentTimeMillis();
                        wait( waittime<0? 0: waittime );
                    } catch ( InterruptedException except ) {
                        // If the thread is interrupted, come out with the proper message
                        if ( TRACE )
                            System.out.println( "Interrupted while waiting on " + this.toString() + " by " + key );
                        throw new LockNotGrantedException( Messages.message("persist.writeLockTimeout") );
                    }

                    if ( _deleted )  {
                        // If object has been deleted while waiting for lock, report deletion.
                        if ( TRACE )
                            System.out.println( "Deleted while waiting on " + this.toString() + " by " + key );
                        throw new ObjectDeletedWaitingForLockException( Messages.message("persist.persist.deletedWaitingForLock") );
                    }

                    // Try to re-acquire lock, this time less timeout,
                    // eventually timeout of zero will either succeed or fail
                    // without blocking.
                    if ( System.currentTimeMillis() > endtime )
                        timeout = 0;
                } finally {
                    removeWaiting( key );
                    key.setWaitOnLock( null );
                }
            }
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
    private void detectDeadlock( Key waitingTx, int numOfRec )
            throws LockNotGrantedException {

        ObjectLock waitOn;

        if ( numOfRec <= 0 ) return;

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
                    throw new LockNotGrantedException( "persist.deadlock" );
                }
                read = waitOn._readLock;
                while ( read != null ) {
                    if ( read.key == waitingTx )
                        throw new LockNotGrantedException( "persist.deadlock" );
                    read = read.next;
                }
                waitOn.detectDeadlock( waitingTx, numOfRec - 1 );
            }
        } else {
            LinkedTx lock;

            lock = _readLock;
            while ( lock != null ) {
                // T1 trying to acquire lock on O1, which is locked by T2
                // T2 trying to acauire lock on O1, T1 is waiting on O1

                // lock is the blocking transaction. We are only interested in
                // a blocked transacrtion.
                waitOn = lock.key.getWaitOnLock();
                if ( waitOn != null && lock.key != waitingTx ) {
                    LinkedTx read;

                    if ( waitOn._writeLock == waitingTx ) {
                        throw new LockNotGrantedException( "persist.deadlock" );
                    }
                    read = waitOn._readLock;
                    while ( read != null ) {
                        if ( read.key == waitingTx )
                            throw new LockNotGrantedException( "persist.deadlock" );
                        read = read.next;
                    }
                    waitOn.detectDeadlock( waitingTx, numOfRec - 1 );
                }
                lock = lock.next;
            }
        }
    }


    /**
     * Remove the transaction from the waiting list (both read and write).
     */
    private void removeWaiting( Key key ) {

        try {
            if ( _writeWaiting != null ) {
                if ( _writeWaiting == key ) {
                    _writeWaiting = _writeWaiting.getNext();
                } else {
                    Key wait;
                    
                    wait = _writeWaiting;
                    while ( wait.getNext() != null ) {
                        if ( wait.getNext() == key ) {
                            wait.setNext( wait.getNext().getNext() );
                            break;
                        }
                        wait.setNext( wait.getNext() );
                    }
                }
            }
            if ( _readWaiting != null ) {
                if ( _readWaiting == key ) {
                    _readWaiting = _readWaiting.getNext();
                } else {
                    Key wait;
                    
                    wait = _readWaiting;
                    while ( wait.getNext() != null ) {
                        if ( wait.getNext() == key ) {
                            wait.setNext( wait.getNext().getNext() );
                            break;
                        }
                        wait.setNext( wait.getNext() );
                    }
                }
            }
            if ( _deleted && _readWaiting == null && _writeWaiting == null /*&& _confirmWaiting == null*/ ) {
                _deleted = false;
            }
        } catch ( ThreadDeath death ) {
            // This operation must never fail, not even in the
            // event of a thread death
            removeWaiting( key );
            throw death;
        }
    }


    public String toString()
    {
        return _oid.toString() + "/" + _id + " " + ( ( _readLock == null ? "-" : "R" ) + "/" +
                                         ( _writeLock == null ? "-" : "W" ) );
    }


    /**
     * Object uses to hold a linked list of transactions holding
     * write locks or waiting for a read/write lock.
     */
    private static class LinkedTx {

        Key         key;

        LinkedTx    next;

        LinkedTx( Key key, LinkedTx next )
        {
            this.key = key;
            this.next = next;
        }

    }


}


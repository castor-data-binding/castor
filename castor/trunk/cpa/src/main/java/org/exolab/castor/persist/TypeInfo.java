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
 * $Id: LockEngine.java 8888 2011-05-12 12:35:39Z rjoachim $
 */

package org.exolab.castor.persist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cache.Cache;
import org.castor.persist.TransactionContext;
import org.castor.persist.cache.CacheEntry;
import org.exolab.castor.jdo.LockNotGrantedException;

/**
 * Provides information about an object of a specific type (class's full name).
 * This information includes the object's descriptor and lifecycle interceptor
 * requesting notification about activities that affect an object.
 * <p>
 * It also provides caching for a persistence storage. Different {@link Cache} mechanisms
 * can be specified. 
 * <p>
 * Each class hierarchy gets its own cache, so caches can be
 * controlled on a class-by-class basis.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @version $Revision: 8888 $ $Date: 2006-04-22 11:05:30 -0600 (Sat, 22 Apr 2006) $
 */
public final class TypeInfo {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static Log _log = LogFactory.getFactory().getInstance(TypeInfo.class);
    
    /** The Map contains all the in-used ObjectLock of the class type, which
     *  keyed by the OID representing the object. All extends classes share the
     *  same map as the base class. */
    private final Map<OID, ObjectLock> _locks = new HashMap<OID, ObjectLock>();
    
    /** The Map contains all the freed ObjectLock of the class type, which keyed
     *  by the OID representing the object. ObjectLock put into cache maybe
     *  disposed by LRU mechanisum. All extends classes share the same map as the
     *  base class. */
    private final Cache _cache;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor for creating base class info.
     *
     * @param cache The new LRU which will be used to store and dispose freed ObjectLock.
     */
    public TypeInfo(final Cache cache) {
        _cache = cache;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Life-cycle method to allow shutdown of cache instances.
     */
    public void closeCache() {
        _cache.close();
    }
    
    /**
     * Dump all objects in cache or lock to output.
     * @param name the class's full name
     */
    public void dumpCache(final String name) {
        _log.info(name + ".dumpCache()...");
        
        synchronized (_locks) {
            for (OID entry : _locks.keySet()) {
                _log.info("In locks: " + entry);
            }
            for (Object entry : _cache.keySet()) {
                _log.info("In cache: " + entry);
            }
        }
    }
    
    /**
     * Expire all objects of this class from the cache.
     */
    public void expireCache() {
        synchronized (_locks) {
            // Mark all objects currently participating in a
            // transaction as expired.  They will be not be added back to
            // the LRU when the transaction's complete (@see release)
            // XXX [SMH]: Reconsider removing from locks (unknown side-effects?).
            for (Iterator<ObjectLock> iter = _locks.values().iterator(); iter.hasNext(); ) {
                ObjectLock objectLock = iter.next();
                objectLock.expire();
                iter.remove();
            }
            
            // Remove all objects not participating in a transaction from the cache.
            _cache.clear();
        }
    }

    /**
     * Acquire the object lock for transaction. After this method is called,
     * user must call {@link ObjectLock#confirm(TransactionContext, boolean)} 
     * exactly once.
     *
     * @param oid The OID of the lock.
     * @param tx The context of the transaction to acquire lock.
     * @param lockAction The inital action to be performed on the lock.
     * @param timeout    The time limit to acquire the lock.
     * @return The object lock for the OID within this transaction context. 
     * @throws ObjectDeletedWaitingForLockException
     * @throws LockNotGrantedException Timeout or deadlock occured attempting
     *         to acquire lock on object
     */
    public ObjectLock acquire(final OID oid, final TransactionContext tx,
            final LockAction lockAction, final int timeout)
    throws LockNotGrantedException {
        ObjectLock entry = null;
        // sync on "locks" is, unfortunately, necessary if we employ
        // some LRU mechanism, especially if we allow NoCache, to avoid
        // duplicated LockEntry exist at the same time.
        synchronized (_locks) {
            // consult with the 'first level' cache, aka current transaction 
            entry = _locks.get(oid);
            if (entry == null) {
                // consult with the 'second level' cache, aka physical cache
                CacheEntry cachedEntry = (CacheEntry) _cache.remove(oid);
                if (cachedEntry != null) {
                    // found in 'second level' cache
                    entry = new ObjectLock(cachedEntry.getOID(),
                            cachedEntry.getEntry(), cachedEntry.getTimeStamp());
                } else {
                    // not found in 'second level' cache
                    entry = new ObjectLock(oid);
                }
                _locks.put(entry.getOID(), entry);
            }
            entry.enter();
        }
        
        // ObjectLock.acquire() may call Object.wait(), so a thread can not
        // been synchronized with ANY shared object before acquire().
        // So, it must be called outside synchronized( locks ) block.
        boolean failed = true;
        try {
            entry.acquireLock(tx, lockAction, timeout);
            failed = false;
            return entry;
        } finally {
            synchronized (_locks) {
                entry.leave();
                if (failed) {
                    // The need of this block may not be too obvious.
                    // At the very moment, if it happens, current thread 
                    // failed to acquire a lock. Then, another thread just
                    // release the lock right after. The released entry
                    // then will not be moved to cache because inLocksGap 
                    // isn't zero. So, there maybe a chance of memory 
                    // leak, as the entry was in "locks", but not in 
                    // "cache" as supposed. To avoid it from happening,
                    // we ensure here that the entry which should be move 
                    // to "cache" from "locks" is actually moved.
                    if (entry.isDisposable()) {
                        _locks.remove(entry.getOID());
                        if (entry.isExpired()) {
                            _cache.expire(entry.getOID());
                            entry.expired();
                        } else {
                            _cache.put(oid, new CacheEntry(
                                    entry.getOID(), entry.getObject(), entry.getVersion()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Upgrade the lock to write lock.
     * 
     * @param  oid The OID of the lock.
     * @param  tx The transaction in action.
     * @param  timeout  Time limit.
     * @return The upgraded ObjectLock instance.
     * @throws ObjectDeletedWaitingForLockException
     * @throws LockNotGrantedException Timeout or deadlock occured attempting
     *         to acquire lock on object.
     */
    public ObjectLock upgrade(final OID oid, final TransactionContext tx, final int timeout)
    throws LockNotGrantedException {
        OID internaloid = oid;
        ObjectLock entry = null;
        synchronized (_locks) {
            entry = _locks.get(internaloid);
            if (entry == null) {
                throw new ObjectDeletedWaitingForLockException(
                        "Lock entry not found. Deleted?");
            }
            if (!entry.hasLock(tx, false)) {
                throw new IllegalStateException(
                        "Transaction does not hold the any lock on " + internaloid + "!");    
            }
            internaloid = entry.getOID();
            entry.enter();
        }
        
        try {
            entry.upgrade(tx, timeout);
            return entry;
        } finally {
            synchronized (_locks) {
                entry.leave();
            }
        }
    }

    /** 
     * Reassure the lock which have been successfully acquired by the transaction.
     *
     * @param  oid      The OID of the lock.
     * @param  tx       The transaction in action.
     * @param  write    <code>true</code> if we want to upgrade or reassure a
     *                  write lock, <code>false</code> for read lock.
     * @return The reassured ObjectLock instance.
     */
    public ObjectLock assure(final OID oid, final TransactionContext tx, final boolean write) {
        synchronized (_locks) {
            ObjectLock entry = _locks.get(oid);
            if (entry == null) {
                throw new IllegalStateException(
                        "Lock, " + oid + ", doesn't exist or no lock!");
            }
            if (!entry.hasLock(tx, write)) {
                throw new IllegalStateException(
                        "Transaction " + tx + " does not hold the "
                        + (write ? "write" : "read") + " lock: " + entry + "!");
            }
            return entry;
        }
    }

    /**
     * Move the locked object from one OID to another OID for transaction.
     * It is to be called by after create.
     *
     * @param orgoid  Orginal OID before the object is created.
     * @param newoid  New OID after the object is created.
     * @param tx      The TransactionContext of the transaction in action.
     * @return An ObjectLock instance whose OID has been assigned to a new value.
     * @throws LockNotGrantedException Timeout or deadlock occured attempting to
     *         acquire lock on object
     */
    public ObjectLock rename(final OID orgoid, final OID newoid, final TransactionContext tx)
    throws LockNotGrantedException {
        synchronized (_locks) {
            ObjectLock entry = _locks.get(orgoid);
            ObjectLock newentry = _locks.get(newoid);

            // validate locks
            if (orgoid == newoid) {
                throw new LockNotGrantedException("Locks are the same");
            }
            if (entry == null) {
                throw new LockNotGrantedException("Lock doesn't exist!");
            }
            if (!entry.isExclusivelyOwned(tx)) {
                throw new LockNotGrantedException(
                        "Lock to be renamed is not own exclusively by transaction!");
            }
            if (entry.isEntered()) {
                throw new LockNotGrantedException(
                        "Lock to be renamed is acquired by another transaction!");
            }
            if (newentry != null) {
                throw new LockNotGrantedException(
                        "Lock is already existed for the new oid.");
            }

            entry = _locks.remove(orgoid);
            entry.setOID(newoid);
            _locks.put(newoid, entry);

            // copy oid status
            newoid.setDbLock(orgoid.isDbLock());

            return newentry;
        }
    }

    /**
     * Delete the object lock. It's called after the object is deleted from
     * the persistence and the transaction committed.
     *
     * @param oid   The OID of the ObjectLock.
     * @param tx    The transactionContext of transaction in action.
     * @return The just-deleted ObjectLock instance.
     */
    public ObjectLock delete(final OID oid, final TransactionContext tx) {
        ObjectLock entry;
        synchronized (_locks) {
            entry = _locks.get(oid);
            if (entry == null) {
                throw new IllegalStateException("No lock to destroy!");
            }
            entry.enter();
        }

        try {
            entry.delete(tx);
            return entry;
        } finally {
            synchronized (_locks) {
                entry.leave();
                if (entry.isDisposable()) {
                    _cache.put(oid, new CacheEntry(
                            entry.getOID(), entry.getObject(), entry.getVersion()));
                    _locks.remove(oid);
                }
            }
        }
    }

    /**
     * Release the object lock. It's called after the object the transaction
     * has been committed.
     *
     * @param oid   The OID of the ObjectLock.
     * @param tx    The transactionContext of transaction in action.
     * @return The just-released ObjectLock instance.
     */
    public ObjectLock release(final OID oid, final TransactionContext tx) {
        ObjectLock entry = null;
        synchronized (_locks) {
            entry = _locks.get(oid);
            if (entry == null) {
                throw new IllegalStateException(
                        "No lock to release! " + oid + " for transaction " + tx);
            }
            entry.enter();
        }

        try {
            entry.release(tx);
            return entry;
        } finally {
            synchronized (_locks) {
                entry.leave();
                if (entry.isDisposable()) {
                    _cache.put(oid, new CacheEntry(
                            entry.getOID(), entry.getObject(), entry.getVersion()));
                    if (entry.isExpired()) {
                        _cache.expire(oid);
                        entry.expired();
                    }
                    _locks.remove(oid);
                }
            }
        }
    }

    /**
     * Indicates whether an object with the specified identifier is curretly cached.
     *  
     * @param oid     The Object identifier.
     * @return True if the object is cached. 
     */
    public boolean isCached(final OID oid) {
        return _cache.containsKey(oid);
    }
    
    /**
     * Indicates whether an object with the specified OID is currently locked.
     * 
     * @param oid Object identifier.
     * @return True if the object is locked.
     */
    public boolean isLocked(final OID oid) {
        return _locks.containsKey(oid);
    }

    //-----------------------------------------------------------------------------------    
}

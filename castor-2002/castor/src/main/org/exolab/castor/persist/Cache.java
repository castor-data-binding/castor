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
 */


package org.exolab.castor.persist;


import java.util.Hashtable;
import java.util.Enumeration;
import org.exolab.castor.jdo.LockNotGrantedException;

// for CacheTest

import org.exolab.castor.mapping.*;


/**
 *
 * @author
 * @version $Revision$ $Date$
 */
public final class Cache
{


    /**
     * Mapping of object locks in "transcation state" to OIDs. The {@link OID} 
     * is used as the key, and {@link ObjectLock} is the value. There is one 
     * lock per OID.
     */
    private final Hashtable _locks = new Hashtable();
    
    /**
     * Mapping of cache object (not hold by any transcation) to OIDs. 
     * The {@link OID} is used as the key, and {@link ObjectLock} is the value. 
     * There is one lock per OID.
     */
    private LRU _cache;
    
    // private static TransactionContext _cacheTx = new CacheTransaction();
    
    // private static final long StampInUse   = 0;
    
    // private long  _counter;
    
    
    /**
     * Specify no caching as the caching mechanism of this Cache. All released object
     * will be discarded.
     */
    public final static int CACHE_NONE = 0;
    
    /**
     * Specify Count-Limited Least Recently Used is used as caching mechanism of this Cache.
     * Object Lock which is not hold by any transcation will be put in the cache, until
     * the cache is full and other object overwritten it.
     */
    public final static int CACHE_COUNT_LIMITED = 1;
    
    /**
     * Specify Time-Limited Least Recently Used is used as caching mechanism of this Cache.
     * Object Lock which is not hold by any transcation will be put in the cache, until
     * timeout is reached.
     */
    public final static int CACHE_TIME_LIMITED = 2;
    
    /**
     * Specify unlimited cache as caching mechanism of this Cache.
     * Object Lock which is not hold by any transcation will be put in the cache 
     * for later use.
     */
    public final static int CACHE_UNLIMITED = 3;
    
    
    /**
     * Four type of cache can be used: CACHE_NONE, CACHE_COUNT_LIMITED, CACHE_TIME_LIMITED, 
     * and CACHE_UNLIMITED.
     * {@param param} no effect for CACHE_NONE, CACHE_UNLIMITED.
     * for CACHE_COUNT_LIMITED, it is the max number of {@link ObjectLock} kept in cache at any
     * given time.
     * for CACHE_TIME_LIMITED, it is the time in second for a {@link ObjectLock} kept in cache
     * before it is removed.
     */
    Cache( int type, int param ) {
        switch ( type ) {
        case CACHE_COUNT_LIMITED :
            if ( param > 0 ) 
                _cache = new CountLimitedLRU( param );
            else 
                _cache = new NoCache();
            break;
        case CACHE_TIME_LIMITED :
            if ( param > 0 ) 
                _cache = new TimeLimitedLRU( param );
            else 
                _cache = new NoCache();
            break;
        case CACHE_UNLIMITED :
            _cache = new UnlimitedLRU();
            break;
        case CACHE_NONE :
            _cache = new NoCache();
            break;
        default :
            _cache = new CountLimitedLRU( 100 );
        }
    }
    
    synchronized ObjectLock getLockForAquire( OID oid ) {
        //System.out.println("getLockForAquire: "+oid);
        CacheEntry entry = (CacheEntry) _locks.get( oid );
        
        // try cache if not in _locks
        if ( entry == null ) {
            //System.out.println("trying cache, cus it's not in lock");
            entry = (CacheEntry) _cache.remove( oid );
        }
        
        if ( entry != null ) {
            /*
              synchronized ( entry ) {
              // counter is needed to avoid an entry removed
              // while CacheEngine acquring a lock
            */
            entry.aquireCounter++;
            
            _locks.put( oid, entry );
            
            return entry.lock;
            /*    
             }
            */
        }
        
        return null;
    }
    
    synchronized void finishLockForAquire( OID oid, ObjectLock lock ) {
        //System.out.println("finishLockForAquire: "+oid);
        CacheEntry entry = (CacheEntry) _locks.get( oid );
        
        if ( entry != null && entry.lock == lock ) {
            // synchronized ( entry ) {
            entry.aquireCounter--;
            if ( entry.aquireCounter < 0 ) {
                throw new RuntimeException("unmatch aquirelock and finishlock");
            }
            synchronized ( entry.lock ) {
                //System.out.println("finishLock " + "aquire counter: "+entry.aquireCounter+" entry.lock.isFree(): "+entry.lock.isFree());
                if ( entry.aquireCounter <= 0 && entry.lock.isFree() ) {
                    // move entry to cache
                    _locks.remove( oid );
                    if ( !entry.lock.isDeleted() )
                        _cache.put( oid, entry );
                }
            }
            // }
        }
        
    }
    
    synchronized ObjectLock releaseLock( OID oid )
    {
        CacheEntry entry = (CacheEntry) _locks.get( oid ); 
        
        if ( entry == null )
            return null;
        
        if ( entry != null ) {
            synchronized ( entry.lock ) {
                //System.out.println("releaseLock " + "aquire counter: "+entry.aquireCounter+" entry.lock.isFree(): "+entry.lock.isFree());
                if ( entry.aquireCounter <= 0 && entry.lock.isFree() ) {
                    // move entry to cache
                    _locks.remove( oid );
                    if ( !entry.lock.isDeleted() )
                        _cache.put( oid, entry );
                }
            }
        }
        return entry.lock;
    }
    
    
    synchronized void addLock( OID oid, ObjectLock lock )
    {
        CacheEntry entry;
        
        entry = new CacheEntry( oid, lock );
        
        _locks.put( oid, entry );
    }
    
    
    synchronized void removeLock( OID oid )
    {
        _locks.remove( oid );
        _cache.remove( oid );
    }
    
    
    static class CacheEntry
    {
        
        final ObjectLock lock;
        
        long aquireCounter;
        
        long stamp;
        
        CacheEntry( OID oid, ObjectLock lock )
        {
            this.lock = lock;
        }
        
    }


}


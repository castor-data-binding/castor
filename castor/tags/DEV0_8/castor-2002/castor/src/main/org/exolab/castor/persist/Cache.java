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


import java.util.Hashtable;
import java.util.Enumeration;
import org.exolab.castor.jdo.LockNotGrantedException;


/**
 * A cache for holding objects of a particular type. The cache has a
 * finite size and can be optimized for a particular class based on
 * the application behavior.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
final class Cache
    extends Thread
{

    /**
     * Mapping of object locks to OIDs. The {@link OID} is used as the
     * key, and {@link ObjectLock} is the value. There is one lock per OID.
     */
    private final Hashtable _locks = new Hashtable();


    private long  _counter;


    private static TransactionContext _cacheTx = new CacheTransaction();


    private static final long StampInUse   = 0;


    Cache()
    {
    }


    synchronized ObjectLock getLock( OID oid )
    {
        CacheEntry entry;

        entry = (CacheEntry) _locks.get( oid ); 
        if ( entry == null )
            return null;
        entry.stamp = StampInUse;
        return entry.lock;
    }


    synchronized ObjectLock releaseLock( OID oid )
    {
        CacheEntry entry;

        entry = (CacheEntry) _locks.get( oid ); 
        if ( entry == null )
            return null;
        entry.stamp = System.currentTimeMillis();
        return entry.lock;
    }


    synchronized void addLock( OID oid, ObjectLock lock )
    {
        CacheEntry entry;

        entry = new CacheEntry( oid, lock );
        entry.stamp = StampInUse;
        _locks.put( oid, entry );
    }


    void removeLock( OID oid )
    {
        _locks.remove( oid );
    }


    public void run()
    {
        Enumeration enum;
        CacheEntry  entry;
        OID         oid;

        while ( true ) {
            enum = _locks.keys();
            while ( enum.hasMoreElements() ) {
                oid = (OID) enum.nextElement();
                entry = (CacheEntry) _locks.get( oid );
                synchronized ( this ) {
                    if ( entry.stamp != StampInUse ) {
                        try {
                            Object obj;
                            
                            obj = entry.lock.acquire( _cacheTx, true, 0 );
                            _locks.remove( oid );
                            entry.lock.release( _cacheTx );
                        } catch ( LockNotGrantedException except ) { }
                    }
                }
            }
        }
    }


    static class CacheEntry
    {

        final ObjectLock lock;
        
        long             stamp;

        CacheEntry( OID oid, ObjectLock lock )
        {
            this.lock = lock;
        }

    }


    static class CacheTransaction
        extends TransactionContext
    {

        public Object getConnection( PersistenceEngine engine )
        {
            return null;
        }

        protected void commitConnections()
        {
        }

        protected void rollbackConnections()
        {
        }

    }


}

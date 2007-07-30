/*
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

package org.exolab.castor.jdo;

import org.castor.persist.TransactionContext;
import org.castor.util.Messages;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.spi.Identity;

/**
 * CacheManager handles expiring objects from the cache.
 * 
 * CacheManager is created from DatabaseImpl and should not be instantiated
 * manually.
 * 
 * @author <a href="mailto:dulci@start.no">Stein M. Hugubakken </a>
 * @version $Revision$ $Date: 2006-04-22 11:05:30 -0600 (Sat, 22 Apr 2006) $
 */
public class CacheManager {
    /** Database instance. */
    private Database _db;

    /** Lock engine. */
    private LockEngine _lockEngine;

    /**
     * Currently active transaction context.
     */
    private TransactionContext _transactionContext;

    
    /**
     * Creates an instance of this class.
     * @param db Database instance.
     * @param transactionContext Active transaction context.
     * @param lockEngine Lock engine
     */
    public CacheManager(final Database db, final TransactionContext transactionContext, final LockEngine lockEngine) {
        this._db = db;
        this._transactionContext = transactionContext;
        this._lockEngine = lockEngine;
    }

    /**
     * Indicates whether am instance of cls is currently cached.
     * @param cls The class type.
     * @param identity The object identity.
     * @return True if the object is cached.
     * @throws PersistenceException If a problem occured resolving the object's cache membership.
     */
    public boolean isCached (final Class cls, final Object identity) throws PersistenceException {
        if (_transactionContext != null && _transactionContext.isOpen()) {
            return _transactionContext.isCached(_lockEngine.getClassMolder(cls), cls, new Identity(identity));
        }
        
        throw new PersistenceException("isCached() has to be called within an active transaction.");
    }

    /**
     * Dump all cached objects to log.
     */
    public void dumpCache() {
        _lockEngine.dumpCache();
    }

    /**
     * Dump cached objects of specific type to log.
     */
    public void dumpCache(final Class cls) {
        _lockEngine.dumpCache(cls);
    }

    /**
     * Expires all objects from cache.
     * <p>
     * Objects expired from the cache will be read from persistent storage, as
     * opposed to being read from the performance cache, during subsequent
     * load/query operations.
     * <p>
     * When objects are expired from the cache individually, by identity,
     * objects contained within a "master" object, for example objects
     * maintained in a one-to-many relationship, will automatically be expired
     * from the cache, without the need to explicitly identify them. This does
     * not apply when expiring objects by type. Each type, both container and
     * contained objects need to be specified.
     */
    public void expireCache() {
        _lockEngine.expireCache();
    }

    /**
     * Expires a type with a specific identity from cache.
     * <p>
     * Objects expired from the cache will be read from persistent storage, as
     * opposed to being read from the performance cache, during subsequent
     * load/query operations.
     * <p>
     * When objects are expired from the cache individually, by identity,
     * objects contained within a "master" object, for example objects
     * maintained in a one-to-many relationship, will automatically be expired
     * from the cache, without the need to explicitly identify them. This does
     * not apply when expiring objects by type. Each type, both container and
     * contained objects need to be specified.
     * <p>
     * 
     * @param type The type to expire.
     * @param identity Identity of the object to expire.
     */
    public void expireCache(final Class type, final Object identity) throws PersistenceException {
        expireCache(type, new Object[] {identity});
    }

    /**
     * Expires a type with specific identities from cache.
     * <p>
     * Objects expired from the cache will be read from persistent storage, as
     * opposed to being read from the performance cache, during subsequent
     * load/query operations.
     * <p>
     * When objects are expired from the cache individually, by identity,
     * objects contained within a "master" object, for example objects
     * maintained in a one-to-many relationship, will automatically be expired
     * from the cache, without the need to explicitly identify them. This does
     * not apply when expiring objects by type. Each type, both container and
     * contained objects need to be specified.
     * <p>
     * 
     * @param type The type to expire.
     * @param identity An array of object identifiers to expire.
     */
    public void expireCache(final Class type, final Object[] identity) throws PersistenceException {
        testForOpenDatabase();
        ClassMolder molder = _lockEngine.getClassMolder(type);
        for (int i = 0; i < identity.length; i++) {
            _transactionContext.expireCache(molder, new Identity(identity[i]));
        }
    }

    /**
     * Expires all instances of specified types from cache.
     * <p>
     * Objects expired from the cache will be read from persistent storage, as
     * opposed to being read from the performance cache, during subsequent
     * load/query operations.
     * <p>
     * When objects are expired from the cache individually, by identity,
     * objects contained within a "master" object, for example objects
     * maintained in a one-to-many relationship, will automatically be expired
     * from the cache, without the need to explicitly identify them. This does
     * not apply when expiring objects by type. Each type, both container and
     * contained objects need to be specified.
     * <p>
     * 
     * @param type An array of types to expire.
     */
    public void expireCache(final Class[] type) {
        for (int i = 0; i < type.length; i++) {
            _lockEngine.expireCache(type[i]);
        }
    }
    
    /**
     * Throws a PersistenceException if the database is closed, otherwise it does nothing.
     * 
     * @throws PersistenceException
     */
    private void testForOpenDatabase() throws PersistenceException {
        if (_db.isClosed()) {
            throw new PersistenceException(Messages.message("jdo.dbClosed"));
        }
    }
}

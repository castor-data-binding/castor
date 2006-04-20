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

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.io.Writer;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.Connector;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.LogInterceptor;
import org.exolab.castor.util.Messages;


/**
 * LockEngine is a gateway for a data store that implements the interfaces 
 * in the {@link org.exolab.castor.persist.spi} package.
 * <p>
 * It mantains dirty checking cache state and lock, and provides a thread safe 
 * enviroment for transactions. LockEngine garantees that no two conflicting 
 * operations will be let running concurrently for the same object. 
 * <p>
 * For example, it ensures that exactly one transaction may read (load) exclusively
 * on one entity; transaction can not deleted an entity while the other is 
 * reading it, etc...
 * <p>
 * It also provides caching for a persistence storage. Different {@link LRU} mechanisms
 * can be specified. 
 * <p>
 * User should not create more than one instance of LockEngine for each persistent 
 * storage. So that object can be properly locked and ObjectModifiedException can
 * be avoided.
 * <p>
 * However, if more than one instance of LockEngine or some other external 
 * application run concurrently, if the {@link Persistence} supports dirty checking,
 * like a fully complaint JDBC Relational Database, proper 
 * ObjectModifiedException will be thrown to ensure data consistency.
 * <p>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 * @version $Revision$ $Date$
 */
public final class LockEngine {


    /**
     * IMPLEMENTATION NOTES:
     *
     * An object may be persistent in multiple caches at any given
     * time. There is no way to load an object from multiple caches,
     * but an object can be loaded in one engine and then made
     * persistent in another. The engines are totally independent and
     * no conflicts should occur.
     *
     * Each class hierarchy gets its own cache, so caches can be
     * controlled on a class-by-class basis.
     */


    /**
     * Mapping of type information to object types. The object's class is used
     * as the key and {@link TypeInfo} is the value. {@link TypeInfo} provides
     * sufficient information to persist the object, manipulated it in memory
     * and invoke the object's interceptor.
     */
    private HashMap             _typeInfo = new HashMap();


    /**
     * Used by the constructor when creating handlers to temporarily
     * hold the persistence factory for use by {@link #addClassHandler}.
     */
    private PersistenceFactory  _factory;


    /**
     * The log interceptor used to trace persistence operations. May be null.
     */
    private LogInterceptor      _log;


    /**
     * The connector that handle the connections for the underneath data store
     */
    private Connector           _connector;


    /**
     * All object lock associated with a key.
     * Keyed by "Key" and value is a ArrayList contains ObjectLock(s).
     */
    private HashMap             _entityLocks = new HashMap();


    /**
     * A java.io.Writer that do nothing
     */
    private final static Writer _voidWriter  = new Writer() {
        public void write(char[] cbuf, int off, int len) {}
        public void flush() {}
        public void close() {}
    };


    /**
     * Construct a new cache engine with the specified mapping table, 
     * persistence engine and the log interceptor.
     *
     * @param mapResolver Provides mapping information for objects
     *  supported by this cache
     * @param factory Factory for creating persistence engines for each
     *  object described in the map
     * @param logInterceptor Log interceptor to use for cache and all its
     *  persistence engines
     * @throws MappingException Indicate that one of the mappings is
     *  invalid
     */
    public LockEngine( PersistenceFactory factory, 
            org.exolab.castor.jdo.conf.Database conf,
            LogInterceptor logInterceptor )
            throws MappingException, PersistenceException {

        _log = _log!=null?logInterceptor:new OutputLogInterceptor(_voidWriter);;

        _factory = factory;

        _connector = factory.getConnector( this, conf );

    }

    // ==========================================
    //             the connector stuff
    // ==========================================
    /**
     * Get the connector for this LockEngine. A connector repsonsible for a
     * connection and its associated connection.
     */
    public Connector getConnector() {

        return _connector;
    }

    
    public void startConnection( Key key ) 
            throws PersistenceException {

        synchronized( _entityLocks ) {
            if ( _entityLocks.get( key ) != null )
                return;
            _connector.start( key );
            _entityLocks.put( key, new ArrayList() );
        }
    }

    public void commitConnection( Key key ) 
            throws PersistenceException {

        _connector.commit( key );

    }

    public void rollbackConnection( Key key ) 
            throws PersistenceException {

        _connector.rollback( key );

        ArrayList locks = null;
        synchronized ( _entityLocks ) {
            locks = (ArrayList) _entityLocks.remove( key );
        }
        Iterator itor = locks.iterator();
        while ( itor.hasNext() ) {
            ObjectLock lock = (ObjectLock) itor.next();

        }
    }

    public void releaseConnection( Key key ) 
            throws PersistenceException {
    }

    public void closeConnection( Key key ) 
            throws PersistenceException {
        _connector.close( key );
    }

    public void prepareConnection( Key key ) 
            throws PersistenceException {
        _connector.prepare( key );
    }

    // ==========================================
    //         the pre-fetch stuff
    // ==========================================
    /**
     * This method is used by the bridge layer to insert an prefetched
     * entity into the LockEngine. Each inserted entity is read-locked. 
     * If the entity has already locked by this transaction or any 
     * other transaction, calling this method result in no effect.
     */
    public void addEntity( Key key, Entity entity ) 
            throws ClassNotPersistenceCapableException {

        TypeInfo typeInfo = getTypeInfo( entity.base );

        ObjectLock lock = null;

        try {

            lock = typeInfo.enter( key, entity.identity );

            lock.acquire( key, false, 0 );

        } catch ( LockUpgradeFailedException ex ) {

        } catch ( LockNotGrantedException ex ) {
            // ignore 
        } finally {
            typeInfo.leave( key, lock );
        }
    }

    /**
     * This method is used by the bridge layer to insert an list of
     * entities' identities into the LockEngine. Each inserted list
     * will be read-locked. If the list has already locked by this
     * transaction or any other transaciton, calling this method 
     * results in no effect.
     */
    public void addRelated( Key key, Relation relation ) {
    }

    /**
     * To be removed. OQLQueryImpl depends on this method and should
     * be refactored. 
     */
    public Persistence getPersistence( Class cls ) {
        throw new RuntimeException("Method not implemented!");
    }

    // ==========================================
    //         Referential integrity stuff
    // ==========================================
    /**
     * Updates the corresponding Relations of the specified Entity.<p>
     *
     * The Entity, and Relation of an Entity to another, is maintained
     * (cache and lock) separately. When the state of an Entity changed
     * the corresponding Relation, if any, should be updated too. For
     * example, when the foreign key field in a Entity is set to null,
     * the corresponds Relation should be removed.<p>
     *
     * @param key The key which holds the locks
     * @param entity The specified entity
     * @throws LockNotGrantedException ??
     */
    private void updateRelationLists( Key key, Entity entity ) 
            throws LockNotGrantedException {

        // for each relation in entity, update the relationship list
        Entity.EntityIterator itor = entity.iterator();
        while ( itor.next() ) {
            int len = itor.getEntityFieldSize();
            for ( int i = 0; i < len; i++ ) {
                EntityFieldInfo info = itor.getEntityFieldInfo(i);
                Object value = itor.getFieldValue(i);
                switch ( info.cardinality ) {
                case EntityFieldInfo.CARDINALITY_MANY_TO_ONE:
                    if ( value != null ) {
                        //TypeInfo typeInfo = TypeInfo.getInfo( _typeInfo, info );
                        //ObjectLock lock = typeInfo.acquire( value, key, 0 );
                        //Relation relation = (Relation) lock.getObject( key );
                        //if ( !relation.list.contains( entity.identity ) ) {
                            // if relation does not contains the identity update it
                        //    rlock.add( key, entity.identity );
                        //}
                    }
                    break;
                }
            }
        }
    }

    // ==========================================
    //                Main Stuff
    // ==========================================
    /**
     * Loads an object of the specified type and identity from
     * persistent storage. In exclusive mode the object is always
     * loaded and a write lock is obtained on the object, preventing
     * concurrent updates. In non-exclusive mode the object is either
     * loaded or obtained from the cache with a read lock. 
     *
     * @param key The key used to lock entity to be loaded.
     * @param entity The entity instance to be loaded into
     * @param accessMode The desired access mode
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void load( Key key, Entity entity, AccessMode accessMode, int timeout )
            throws ObjectNotFoundException, LockNotGrantedException, PersistenceException,
            ClassNotPersistenceCapableException, ObjectDeletedWaitingForLockException {

        TypeInfo typeInfo = getTypeInfo( entity.base );

        ObjectLock entry  = null;

        try {
            entry = typeInfo.enter( key, entity.identity );

            entry.acquire( key, accessMode.isExclusive(), timeout );

            if ( accessMode == AccessMode.DbLocked ) {
                if ( !entry.isMaster( key ) ) {
                    // always reload in DbLocked mode
                    _log.loading( entity.base, entity.identity );
                    typeInfo.persist.load( key, entity, accessMode );

                    Entity lockedEntity = (Entity) entry.getObject( key );
                    if ( lockedEntity == null ) 
                        // if lockedEntity is null, create a new one
                        lockedEntity = new Entity();
                    lockedEntity.copyInto( entity );
                    entry.setObject( key, lockedEntity );
                }
            } else {
                Entity lockedEntity = (Entity) entry.getObject( key );
                if ( lockedEntity == null ) {
                    // the lock is new, load it from data store
                    _log.loading( entity.base, entity.identity );
                    typeInfo.persist.load( key, entity, accessMode );
                    try {
                        lockedEntity = (Entity) entity.clone();
                    } catch ( CloneNotSupportedException e ) {
                        // Entity class implements Cloneable and is final.
                        // We are sure it won't happens
                    }
                    entry.setObject( key, lockedEntity );

                    if ( accessMode == AccessMode.Shared && entry.isMaster( key ) ) {
                        entry.downgrade( key );
                    }
                }
            }

            updateRelationLists( key, entity );

        } catch ( ObjectDeletedWaitingForLockException except ) {
            // This is equivalent to object does not exist
            throw new ObjectNotFoundException( 
            Messages.format("persist.objectNotFound", entity.base, entity.identity ));
        } finally {
            typeInfo.leave( key, entry );
        }
    }

    /**
     * Creates a new entity in the persistence storage. The entity must not 
     * be persistent and must have a unique identity within this engine.
     *
     * @param key The transaction context
     * @param entity The entity to be created
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void create( Key key, Entity entity )
            throws DuplicateIdentityException, PersistenceException,
            ClassNotPersistenceCapableException {

        TypeInfo typeInfo = getTypeInfo( entity.base );

        ObjectLock entry  = null;

        boolean write = true;   // just for readability

        if ( entity.identity != null ) {

            try {

                entry = typeInfo.enter( key, entity.identity );

                entry.acquire( key, write, 0 );

                _log.creating( entity.base, entity.identity );

                typeInfo.persist.create( key, entity );

                updateRelationLists( key, entity );

                // should catch some other exception if destory is not succeed
            } catch ( LockNotGrantedException except ) {
                // Someone else is using the object, definite duplicate key
                throw new DuplicateIdentityException( 
                    Messages.format( "persist.duplicateIdentity", 
                    entity.base, entity.identity ) );
            } catch ( DuplicateIdentityException except ) {
                // we got a write lock and the persistence storage already
                // recorded. Should destory the lock
                entry.release( key );
                throw except;
            } finally {
                typeInfo.leave( key, entry );
            }
        } else {    // identity is null

            try {
                _log.creating( entity.base, entity.identity );

                typeInfo.persist.create( key, entity );

                entry = typeInfo.enter( key, entity.identity );

                entry.acquire( key, write, 0 );

                updateRelationLists( key, entity );

            } catch ( LockNotGrantedException e ) {
                e.printStackTrace();
                throw new DuplicateIdentityException( 
                    "Key Generator Failure. Duplicated Identity is generated!" );
            } finally {
                typeInfo.leave( key, entry );
            }
        }
    }

    /**
     * Called at transaction commit time to delete the entity. Entity
     * deletion occurs in three distinct steps:
     * <ul>
     * <li>A write lock is obtained on the entity to assure it can be
     *     deleted and the entity is marked for deletion in the
     *     transaction context
     * <li>As part of transaction preparation the entity is deleted
     *     from persistent storage using this method
     * <li>The entity is removed from the cache when the transaction
     *     completes
     * </ul>
     *
     * @param key The transaction context
     * @param entity The entity
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void delete( Key key, Entity entity )
            throws PersistenceException {

        TypeInfo typeInfo = getTypeInfo( entity.base );

        ObjectLock entry  = null;

        try {

            entry = typeInfo.enter( key, entity.identity );

            _log.removing( entity.base, entity.identity );

            typeInfo.persist.delete( key, entity );

            // for each relation in entity, update the relationship list
            Entity.EntityIterator itor = entity.iterator();
            while ( itor.next() ) {
                int len = itor.getEntityFieldSize();
                for ( int i = 0; i < len; i++ ) {
                    EntityFieldInfo info = itor.getEntityFieldInfo(i);
                    Object value = itor.getFieldValue(i);
                    switch ( info.cardinality ) {
                    case EntityFieldInfo.CARDINALITY_MANY_TO_ONE:
                        if ( value != null ) {
                        //    TypeInfo relInfo = TypeInfo.getInfo( _typeInfo, info );
                        //    RelationLock lock = relationInfo.acquire( key, value, 0 );
                            // if a relation does not contains the identity update it
                        //    lock.pendRemove( key, entity.identity );
                        }
                        break;
                    }
                }
            }
        } catch ( LockNotGrantedException except ) {
            throw new IllegalStateException( 
                Messages.format( "persist.internal",
                "Attempt to delete object for which no lock was acquired" ) );
        } finally {
            typeInfo.leave( key, entry );
        }
    }

    /**
     * Updates an existing object to this engine. The object must not be
     * persistent and must not have the identity of a persistent object.
     * The object's OID is returned. The OID is guaranteed to be unique
     * for this engine even if no identity was specified.
     * If the object implements TimeStampable interface, verify
     * the object's timestamp.
     *
     * @param key The transaction context
     * @param accessMode The desired access mode
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     * @throws ObjectModifiedException Dirty checking mechanism may immediately
     *  report that the object was modified in the database during the long
     *  transaction.
     */
    public void update( Key key, Entity entity, int timeout )
            throws ObjectNotFoundException, LockNotGrantedException, ObjectModifiedException,
                   PersistenceException, ClassNotPersistenceCapableException,
                   ObjectDeletedWaitingForLockException {

        TypeInfo typeInfo = getTypeInfo( entity.base );

        ObjectLock entry  = null;

        try {
            entry = typeInfo.enter( key, entity.identity );

            entry.acquire( key, true, 0 );

            Entity lockedEntity = (Entity) entry.getObject( key );

            boolean inCache = true;
            if ( lockedEntity == null ) {
                inCache = false;

                lockedEntity = new Entity( entity.base, entity.actual, entity.identity );

                _log.loading( entity.base, entity.identity );

                typeInfo.persist.load( key, lockedEntity, AccessMode.Exclusive );
            }

            if ( entity.longStamp == lockedEntity.longStamp 
                   && entity.objectStamp == lockedEntity.objectStamp ) {
                // bingo
            } else if ( !inCache && entity.longStamp == 0 && entity.objectStamp == null ) {
                // create the object
                _log.creating( entity.base, entity.identity );
                typeInfo.persist.create( key, entity );
            } else {
                throw new ObjectModifiedException("Data object is modified");
            }
        } catch ( DuplicateIdentityException e ) {
            entry.release( key );
            throw new ObjectModifiedException("Data Object doesn't not have valid timestamp");
        } catch ( ObjectModifiedException e ) {
            entry.release( key );
            throw e;
        } catch ( ObjectDeletedWaitingForLockException except ) {
            // This is equivalent to object not existing
            throw new ObjectNotFoundException( Messages.format("persist.objectNotFound", entity.base, entity.identity) );
        } finally {
            typeInfo.leave( key, entry );
        }
    }


    /**
     * Called at transaction commit to store an object that has been
     * loaded during the transaction. If the object has been created
     * in this transaction but without an identity, the object will
     * be created in persistent storage. Otherwise the object will be
     * stored and dirty checking might occur in order to determine
     * whether the object is valid. The object's OID might change
     * during this process, and the new OID will be returned. If the
     * object was not stored (not modified), null is returned.
     *
     * @param key The transaction context
     * @param oid The object's identity
     * @param object The object to store
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID if stored, null if ignored
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     * @throws ObjectModifiedException The object has been modified in
     *  persistent storage since it was loaded, the memory image is
     *  no longer valid
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
     /*
    public void preStore( Key key, Entity entity, int timeout ) 
            throws LockNotGrantedException, PersistenceException {


        TypeInfo    typeInfo;
        ObjectLock  lock = null;
        boolean     modified;

        typeInfo = (TypeInfo) _typeInfo.get( entity.base );

        // Acquire a read lock first. Only if the object has been modified
        // do we need a write lock.

        // 092: oid = new OID( this, typeInfo.molder, oid.getIdentity() );

        // acquire read lock
        // getLockedField();
        // isPersistFieldChange()?
        // if no, return null
        // if yes, get flattened fields, 
        // acquire write lock
        // setLockedField( );
        try {
            lock = typeInfo.assure( entity.identity, key, false );

            //oid = lock.getOID();

            modified = typeInfo.molder.preStore( key, oid, lock, object, timeout );
        } catch ( LockNotGrantedException e ) {
            throw e;
        } catch ( ObjectModifiedException e ) {
            lock.invalidate( key );
            throw e;
        } catch ( ObjectDeletedException e ) {
            lock.delete( key );
            throw e;
        }

        if ( modified )
            return oid;
        else
            return null;
    }*/

    public void store( Key key, Entity entity ) 
            throws LockNotGrantedException, ObjectDeletedException,
            ObjectModifiedException, DuplicateIdentityException,
            PersistenceException {

        TypeInfo typeInfo = getTypeInfo( entity.base );

        ObjectLock lock = null;

        // Attempt to obtain a lock on the database. If this attempt
        // fails, release the lock and report the exception.

        try {
            lock = typeInfo.enter( key, entity.identity );

            lock.acquire( key, true, 0 );

            Entity old = (Entity) lock.getObject(key);

            _log.storing( entity.base, entity.identity );

            typeInfo.persist.store( key, entity, old );

            //lock.pendChange( key, entity );

            // for each relation in entity, update the relationship list
            Entity.EntityIterator itor = entity.iterator();
            while ( itor.next() ) {
                int len = itor.getEntityFieldSize();
                for ( int i = 0; i < len; i++ ) {
                    EntityFieldInfo info = itor.getEntityFieldInfo(i);
                    Object value = itor.getFieldValue(i);
                    switch ( info.cardinality ) {
                    case EntityFieldInfo.CARDINALITY_MANY_TO_ONE:
                        /*
                        if ( value == null ) {
                            TypeInfo typeInfo = _typeInfo.get( info );
                            RelationLock lock = relationInfo.acquire( value, key, ObjectLock.ACTION_PEEK, 0 );
                            // if a relation does not contains the identity update it
                            lock.pendRemove( key, entity.identity );
                        } else if ( value != old.value[i] ) {
                            TypeInfo typeInfo = _typeInfo.get( info );
                            RelationLock lock = relationInfo.acquire( value, key, ObjectLock.ACTION_PEEK, 0 );
                            lock.pendRemove( key, entity.identity );

                            lock = relationInfo.acquire( value, key, ObjectLock.ACTION_PEEK, 0 );
                            lock.pendAdd( key, entity.identity );
                        }*/
                        break;
                    }
                }
            }

        } catch ( ObjectModifiedException e ) {
            lock.invalidate( key );
            throw e;
        } catch ( DuplicateIdentityException e ) {
            throw e;
        } catch ( LockNotGrantedException e ) {
            throw e;
        } catch ( PersistenceException e ) {
            lock.invalidate( key );
            throw e;
        } 
    }



    /**
     * Acquire a write lock on the object. A write lock assures that
     * the object exists and can be stored/deleted when the
     * transaction commits. It prevents any concurrent updates to the
     * object from this point on. However, it does not guarantee that
     * the object has not been modified in the course of the
     * transaction. For that the object must be loaded with exclusive
     * access.
     *
     * @param key The transaction context
     * @param oid The object's OID
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void writeLock( Key key, Entity entity, int timeout )
            throws ObjectDeletedException, LockNotGrantedException, PersistenceException {

        ObjectLock lock;
        TypeInfo   typeInfo;

        /*
        typeInfo = (TypeInfo) _typeInfo.get( entity.getName() );
        // Attempt to obtain a lock on the database. If this attempt
        // fails, release the lock and report the exception.

        try {
            typeInfo.upgrade( entity.identity, key, timeout );

            //typeInfo.persist.writeLock( key, lock...);
        } catch ( IllegalStateException e ) {
            throw e;
        } catch ( ObjectDeletedWaitingForLockException e ) {
            throw new IllegalStateException("Object deleted waiting for lock?????????");
        } catch ( LockNotGrantedException e ) {
            throw e;
        }*/
    }


    /**
     * Reload an object which is already being loaded/created/update
     * this the lock engine
     *
     * @param key The Key that is holding lock to the entity
     * @param entity The entity instance to be filled. The entity should
     *        contains the {@ EntityInfo} and identity of the interested object
     */
    public void reload( Key key, Entity entity ) {
    }

    /**
     * Acquire a write lock on the object in memory. A soft lock prevents
     * other threads from changing the object, but does not acquire a lock
     * on the database.
     *
     * @param key The transaction context
     * @param oid The object's OID
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     */
    public void softLock( Key key, Entity entity, int timeout )
            throws LockNotGrantedException {

        /*
        ObjectLock lock;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        typeInfo.upgrade( oid, key, timeout );
        */
    }

    /**
     * Reverts an object to the cached copy given the object's OID.
     * The cached object is copied into the supplied object without
     * affecting the locks, loading relations or emitting errors.
     * This method is used during the rollback phase.
     *
     * @param key The transaction context
     * @param oid The object's oid
     * @param object The object into which to copy
     * @throws PersistenceException An error reported by the
     *  persistence engine obtaining a dependent object
     */
     /*
    public void revertObject( Key key, OID oid, Object object )
            throws PersistenceException {
        TypeInfo   typeInfo;
        ObjectLock lock;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        try {
            lock = typeInfo.assure( oid, key, false );
            typeInfo.molder.revertObject( key, oid, lock, object );
        } catch ( LockNotGrantedException e ) {
            throw new IllegalStateException("Write Lock expected!");
        } catch ( PersistenceException except ) {
            //typeInfo.destory( oid, key );
            throw except;
        }
    } */

    /**
     * Update the cached object with changes done to its copy. The
     * supplied object is copied into the cached object using a write
     * lock. This method is generally called after a successful return
     * from {@link #store} and is assumed to have obtained a write
     * lock.
     *
     * @param key The transaction context
     * @param oid The object's oid
     * @param object The object to copy from
     */
     /*
    public void updateCache( Key key, OID oid, Object object ) {
        TypeInfo   typeInfo;
        Object[]   fields;
        ObjectLock lock;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        try {
            lock = typeInfo.assure( oid, key, true );
            typeInfo.molder.updateCache( key, oid, lock, object );
        } catch ( LockNotGrantedException e ) {
            throw new IllegalStateException("Write Lock expected!");
        } catch ( PersistenceException except ) {
            typeInfo.delete( oid, key );
        }
    } */

    /**
     * Called at transaction commit or rollback to release all locks
     * held on the object. Must be called for all objects that were
     * queried but not created within the transaction.
     *
     * @param key The transaction context
     * @param oid The object OID
     */
    public void releaseLock( Key key, Entity entity ) {
        /*
        ObjectLock lock;
        TypeInfo   typeInfo;
        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        lock = typeInfo.release( oid, key );
        */
        // 092: lock.getOID().setDbLock( false );
    } 


    /**
     * Called at transaction commit or rollback to forget an object
     * and release its locks. Must be called for all objects that were
     * created when the transaction aborts, and for all objects that
     * were deleted when the transaction completes. The transaction is
     * known to have a write lock on the object.
     *
     * @param key The transaction context
     * @param oid The object OID
     */
     /*
    public void forgetObject( Key key, OID oid ) {
        ObjectLock lock;
        Object[]   fields;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        //lock = typeInfo.locks.aquire( oid, key );
        try {
            typeInfo.assure( oid, key, true );
            
            typeInfo.delete( oid, key );

            typeInfo.release( oid, key );
        } catch ( LockNotGrantedException except ) {
            // If this transaction has no write lock on the object,
            // something went foul.
            _log.message( Messages.format( "persist.internal", "forgetObject: " + except.toString() ) );
            throw new IllegalStateException( except.toString() );
        }
    } */

    /**
     * Helper methods to get a specified typeInfo from the maps
     * and to do the necessary checks
     */
    private TypeInfo getTypeInfo( EntityInfo base ) 
            throws ClassNotPersistenceCapableException {

        TypeInfo typeInfo = (TypeInfo) _typeInfo.get( base );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableException( 
            Messages.format("persist.classNotPersistenceCapable", base) );

        return typeInfo;
    }
        

    /**
     * Provides information about an object of a specific type
     * (class's full name). This information includes the object's descriptor and
     * lifecycle interceptor requesting notification about activities
     * that affect an object.
     */
    private class TypeInfo {

        /**
         * The base entity this TypeInfo belongs to
         */
        private final EntityInfo    base;

        /**
         * The molder for this class.
         */
        private final Persistence   persist;

        /**
         * The Map contains all the in-used ObjectLock of the class type,
         * which keyed by the OID representing the object.
         * All extends classes share the same map as the base class.
         *
         */
        private final HashMap       locks;
        
        /**
         * The Map contains all the freed ObjectLock of the class type,
         * which keyed by the OID representing the object. ObjectLock
         * put into cache maybe disposed by LRU mechanisum.
         * All extends classes share the same map as the base class.
         */
        private final LRU           cache;

        /**
         * Constructor for creating base class info
         *
         * @param  molder is the classMolder of this type
         * @param  locks is the new HashMap which will be used
         *         for holding all the in-used ObjectLock
         * @param  cache is the new LRU which will be used to
         *         store and dispose freed ObjectLock
         *
         */
        private TypeInfo( Entity base, Persistence persist, HashMap locks, LRU cache ) {
            this.base       = base;
            this.persist    = persist;
            this.locks      = locks;
            this.cache      = cache;
        }

        private synchronized ObjectLock enter( Key key, Object oid ) {

            ObjectLock entry = (ObjectLock) locks.get( oid );
            if ( entry == null ) {
                entry = (ObjectLock) cache.remove( oid );
                if ( entry != null ) {
                    oid = entry.getOID();
                    locks.put( oid, entry );
                }
            }
            if ( entry == null ) {
                entry = new ObjectLock( oid );
                locks.put( oid, entry );
            } else {
                oid = entry.getOID();
            }
            entry.enter( key );
            return entry;
        }

        private synchronized void leave( Key key, ObjectLock entry ) {
            if ( entry == null )
                return;

            short action = entry.leave( key );
            switch ( action ) {
            case ObjectLock.LOCK_TO_LOCK :
                break;
            case ObjectLock.LOCK_TO_CACHE :
                locks.remove( entry.getOID() );
                cache.put( entry.getOID(), entry );
                break;
            case ObjectLock.LOCK_TO_DESTROY:
                locks.remove( entry.getOID() );
                break;
            }
        }

        /**
         * Acquire the object lock for transaction. After this method is call,
         * user must call {@link ObjectLock.confirm} exactly once.
         *
         * @param oid  the OID of the lock
         * @param key   the transactionContext of the transaction to 
         *             acquire lock
         * @param lockAction   the inital action to be performed on
         *                     the lock
         * @param timeout      the time limit to acquire the lock
         */
         /*
        private ObjectLock acquire( Object oid, Key key, short lockAction, 
                int timeout ) throws ObjectDeletedWaitingForLockException, 
                LockNotGrantedException, ObjectDeletedException {
            ObjectLock entry = null;
            boolean newentry = false;
            boolean failed = true;
            // sync on "locks" is, unfortunately, necessary if we employ 
            // some LRU mechanism, especially if we allow NoCache, to avoid
            // duplicated LockEntry exist at the same time.
            synchronized( this ) {
                entry = (ObjectLock) locks.get( oid );
                if ( entry == null ) {
                    entry = (ObjectLock) cache.remove( oid );
                    if ( entry != null ) {
                        oid = entry.getOID();
                        locks.put( oid, entry );
                    }
                }
                if ( entry == null ) {
                    newentry = true;
                    entry = new ObjectLock( oid );
                    locks.put( oid, entry );
                } else {
                    oid = entry.getOID();
                }
                entry.enter();
            }
            // ObjectLock.acquire() may call Object.wait(), so a thread can not
            // been synchronized with ANY shared object before acquire(). 
            // So, it must be called outside synchronized( locks ) block.
            try {
                switch ( lockAction ) {
                case ObjectLock.ACTION_READ:
                    //entry.acquireLoadLock( key, false, timeout );
                    break;

                case ObjectLock.ACTION_WRITE:
                    //entry.acquireLoadLock( key, true, timeout );
                    break;

                case ObjectLock.ACTION_CREATE:
                    entry.acquireCreateLock( key );
                    break;

                case ObjectLock.ACTION_UPDATE:
                    entry.acquireUpdateLock( key, timeout );
                    break;

                default:
                    throw new IllegalArgumentException( "lockType "+lockAction+" is undefined!"); 
                }
                failed = false;
                return entry;
            } finally {
                synchronized( this ) {
                    entry.leave();
                    if ( failed ) {
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
                        if ( entry.isDisposable() ) {
                            locks.remove( oid );
                            cache.put( oid, entry );
                        }
                    }
                }
            }
        } */

        /**
         * Upgrade the lock to write lock.

         * @param  oid  the OID of the lock
         * @param  key   the transaction in action
         * @param  timeout  time limit
         */
         /*
        private ObjectLock upgrade( Object oid, Key key, int timeout ) 
                throws ObjectDeletedWaitingForLockException, LockNotGrantedException {
            ObjectLock entry = null;
            synchronized ( this ) {
                entry = (ObjectLock) locks.get( oid );
                if ( entry == null ) 
                    throw new ObjectDeletedWaitingForLockException("Lock entry not found. Deleted?");
                if ( !entry.hasLock( key, false ) )
                    throw new IllegalStateException("Transaction does not hold the any lock on "+oid+"!");    
                oid = entry.getOID();
                entry.enter();
            }
            try {
                entry.upgrade( key, timeout );
                return entry;
            } finally {
                synchronized ( this ) {
                    entry.leave();
                }
            }
        } */

        /** 
         * Reaasure the lock which have been successfully acquired by the 
         * transaction.
         *
         * @param  oid  the OID of the lock
         * @param  key   the transaction in action
         * @param  write  true if we want to upgrade or reassure a write lock
         *                false for read lock
         * @param  timeout  time limit
         *
         */
         /*
        private synchronized ObjectLock assure( Object oid, Key key, boolean write ) 
                throws ObjectDeletedWaitingForLockException, LockNotGrantedException {
            ObjectLock entry = (ObjectLock) locks.get( oid );
            if ( entry == null ) 
                throw new IllegalStateException("Lock, "+oid+", doesn't exist or no lock!");
            if ( !entry.hasLock( key, write ) )
                throw new IllegalStateException("Transaction "+key+" does not hold the "+(write?"write":"read")+" lock: "+entry+"!");
            return entry;
        } */

        /**
         * Move the locked object from one OID to another OID for transaction
         * It is to be called by after create.
         *
         * @param orgoid  orginal OID before the object is created
         * @param newoid  new OID after the object is created
         * @param key      the Key of the transaction in action
         *
         */
         /*
        private synchronized ObjectLock rename( OID orgoid, OID newoid, Key key ) 
                throws LockNotGrantedException {
            ObjectLock entry, newentry;
            boolean write;

            entry = (ObjectLock) locks.get( orgoid );
            newentry = (ObjectLock) locks.get( newoid );

            // validate locks
            if ( orgoid == newoid ) 
                throw new LockNotGrantedException("Locks are the same");
            if ( entry == null ) 
                throw new LockNotGrantedException("Lock doesn't exsit!");
            if ( !entry.isExclusivelyOwned( key ) ) 
                throw new LockNotGrantedException("Lock to be renamed is not own exclusively by transaction!");
            if ( entry.isEntered() ) 
                throw new LockNotGrantedException("Lock to be renamed is being acquired by another transaction!");
            if ( newentry != null ) 
                throw new LockNotGrantedException("Lock is already existed for the new oid.");

            entry = (ObjectLock) locks.remove( orgoid );
            entry.setOID( newoid );
            locks.put( newoid, entry );

            // copy oid status
            newoid.setDbLock( orgoid.isDbLock() );
            newoid.setStamp( orgoid.getStamp() );

            return newentry;
        } */

        /**
         * Delete the object lock. It's called after the object is 
         * deleted from the persistence and the transaction committed.
         *
         * @param oid is the OID of the ObjectLock
         * @param key is the transactionContext of transaction in action
         *
         */
         /*
        private ObjectLock delete( Object oid, Key key ) {
            ObjectLock entry;
            synchronized( this ) {
                entry = (ObjectLock) locks.get( oid );

                if ( entry == null )
                    throw new IllegalStateException("No lock to destory!");
                entry.enter();
            }

            try {
                entry.delete(key);
                return entry;
            } finally {
                synchronized( this ) {
                    entry.leave();
                    if ( entry.isDisposable() ) {
                        locks.remove( oid );
                    }
                }
            }
        } */

        /**
         * Release the object lock. It's called after the object is 
         * the transaction committed.
         *
         * @param oid is the OID of the ObjectLock
         * @param key is the transactionContext of transaction in action
         *
         */
         /*
        private ObjectLock release( Object oid, Key key ) {
            boolean failed = true;
            ObjectLock entry = null;
            synchronized( this ) {
                entry = (ObjectLock) locks.get( oid );

                if ( entry == null ) 
                    throw new IllegalStateException("No lock to release! "+oid+" for transaction "+key);

                entry.enter();
            }
            try {
                entry.release(key);
                failed = false;
                return entry;
            } finally {
                synchronized( this ) {
                    entry.leave();
                    if ( entry.isDisposable() ) {
                        cache.put( oid, entry );
                        locks.remove( oid );
                    }
                }
            }
        } */
    }
}

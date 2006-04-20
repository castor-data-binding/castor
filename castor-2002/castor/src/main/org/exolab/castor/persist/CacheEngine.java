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


import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.engine.CacheType;
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.persist.KeyGeneratorFactoryRegistry;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.KeyGeneratorFactory;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.util.Messages;


/**
 * Implements the object cache engine sitting between a persistence engine
 * and persistence storage SPI.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class CacheEngine
    implements PersistenceEngine
{


    /**
     * IMPLEMENTATION NOTES:
     *
     * An object may be persistent in multiple caches at any given
     * time. There is no way to load an object from multiple caches,
     * but an object can be loaded in one engine and then made
     * persistent in another. The engines are totally independent and
     * no conflicts should occur.
     *
     * For efficiency all objects are cached as an array of fields.
     * Each field holds the cached field value, the identity or a
     * related object, or a vector holding multiple identities of
     * related objects.
     *
     * Each class heirarchy gets its own cache, so caches can be
     * controlled on a class-by-class basis.
     *
     */

    /**
     *  The Default time limit for cache is 100.  
     */
    public final static int DEFAULT_COUNT_LIMIT = 100;

    /**
     *  The Default time limit in second is 30.  
     */
    public final static int DEFAULT_TIME_LIMIT = 30;

    /**
     * The Default cache mechansim which will be used for Caching of ObjectLock
     */
    public final static int DEFAULT_CACHE_TYPE = Cache.CACHE_COUNT_LIMITED;

    /**
     * The Default cache value which will be used for Caching of ObjectLock
     */
    public final static int DEFAULT_CACHE_VALUE = DEFAULT_COUNT_LIMIT;


    /**
     * Mapping of type information to object types. The object's class is used
     * as the key and {@link TypeInfo} is the value. {@link TypeInfo} provides
     * sufficient information to persist the object, manipulated it in memory
     * and invoke the object's interceptor.
     */
    private Hashtable _typeInfo = new Hashtable();


    /**
     * All the XA transactions running against this cache engine.
     */
    private Hashtable _xaTx = new Hashtable();


    /**
     * Used by the constructor for creating handlers and temporarily
     * registering them to prevent circular references. See {@link
     * #addClassHandler}.
     */
    private Hashtable _handlers = new Hashtable();


    /**
     * Used by the constructor when creating handlers to temporarily
     * hold the mapping resolver for use by {@link #addClassHandler}.
     */
    private MappingResolver _mapResolver;


    /**
     * Used by the constructor when creating handlers to temporarily
     * hold the persistence factory for use by {@link #addClassHandler}.
     */
    private PersistenceFactory _factory;


    /**
     * The log interceptor used to trace persistence operations. May be null.
     */
    private LogInterceptor     _logInterceptor;


    /**
     * Construct a new cache engine with the specified name, mapping
     * table and persistence engine.
     *
     * @param mapResolver Provides mapping information for objects
     *  supported by this cache
     * @param factory Factory for creating persistence engines for each
     *  object described in the map
     * @param logWriter Log writer to use for cache and all its
     *  persistence engines
     * @throws MappingException Indicate that one of the mappings is
     *  invalid
     */
    CacheEngine( MappingResolver mapResolver,
                 PersistenceFactory factory, LogInterceptor logInterceptor )
        throws MappingException
    {
        Enumeration   enum;
        ClassHandler  handler;

        _logInterceptor = logInterceptor;
        _mapResolver = mapResolver;
        _factory = factory;
        _handlers = new Hashtable();
        // This step creates the handlers and persistence engines for all
        // the classes resolved from the mapping file. addClassHandler has
        // special mechanisms to deal with inheritance and relations that
        // are forward referenced.
        enum = mapResolver.listJavaClasses();
        while ( enum.hasMoreElements() )
            handler = addClassHandler( (Class) enum.nextElement() );
        _handlers = null;
        _mapResolver = null;
        _factory = null;
    }

    /**
     * Used by the constructor and {@link ClassHandler} to create a
     * new class handler and register it. The class handler must be
     * registered before it has been fully created, and then normalized,
     * in order to prevent circular references when handling relations.
     */
    ClassHandler addClassHandler( Class javaClass )
        throws MappingException
    {
        ClassHandler handler;

        // If a handler exists for the class, return it, otherwise create a new one.
        handler = (ClassHandler) _handlers.get( javaClass );
        if ( handler == null ) {
            ClassDescriptor     clsDesc;
            Persistence         persist;

            clsDesc = _mapResolver.getDescriptor( javaClass );
            if ( clsDesc != null && clsDesc != MappingLoader.NoDescriptor ) {
                // Place the handler in the hashtable first, since normalize() might
                // reference it (extends, relation, etc) and we don't want an infinite loop
                handler = new ClassHandler( clsDesc );
                _handlers.put( javaClass, handler );

                // Create a new persistence engine for that type and add the type info
                persist = _factory.getPersistence( handler.getDescriptor(), _logInterceptor );
                if ( persist != null ) {
                    // At this point the extends typeInfo has been registered, so we know
                    // it exists. We need the extends in order to share cache between objects
                    // in the same heirarchy.
                    if ( handler.getExtends() != null ) {
                        ClassDescriptor extend;
                        TypeInfo        typeInfo;

                        extend = handler.getDescriptor();
                        while ( extend.getExtends() != null )
                            extend = extend.getExtends();
                        typeInfo = (TypeInfo) _typeInfo.get( extend.getJavaClass() );
                        if ( typeInfo == null ) {
                            if ( _logInterceptor != null )
                                _logInterceptor.message( Messages.format( "persist.noEngine", handler.getJavaClass() ) );
                        } else {
                            _typeInfo.put( handler.getJavaClass(),
                                    new TypeInfo( handler, persist, typeInfo.cache ) );
                        }
                    } else {
                        int type = -1;
                        int cap = 0;
                        if ( clsDesc instanceof JDOClassDescriptor ) {
                            CacheType ctype = ((JDOClassDescriptor) clsDesc).getCacheType();
                            if ( ctype != null ) {
                                type = ctype.getType();
                                cap = ctype.getCapacity();
                            } 
                        } else {
                            System.err.println("Warning: JDOClassDescriptor expected! Default Cache setting will be used.");
                            type = DEFAULT_CACHE_TYPE;
                            cap = DEFAULT_CACHE_VALUE;
                        }
                        _typeInfo.put( handler.getJavaClass(),
                                new TypeInfo( handler, persist, new Cache(type, cap) ) );
                    }
                } else if ( _logInterceptor != null )
                    _logInterceptor.message( Messages.format( "persist.noEngine", handler.getJavaClass() ) );
                // Note: this should be done at the very end, otherwise
                // the implicit recursion may cause unpleasant surprises
                handler.normalize( this );
            } 
        }
        return handler;
    }


    public Persistence getPersistence( Class type )
    {
        TypeInfo typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( type );
        if ( typeInfo == null )
            return null;
        else
            return typeInfo.persist;
    }


    public ClassHandler getClassHandler( Class type )
    {
        TypeInfo typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( type );
        if ( typeInfo == null )
            return null;
        else
            return typeInfo.handler;
    }


    /**
     * Loads an object of the specified type and identity from
     * persistent storage. In exclusive mode the object is always
     * loaded and a write lock is obtained on the object, preventing
     * concurrent updates. In non-exclusive mode the object is either
     * loaded or obtained from the cache with a read lock. The object's
     * OID is always returned, this OID must be used in subsequent
     * operations on the object. Must call {@link #copyObject} to obtain
     * the object.
     *
     * @param tx The transaction context
     * @param type The type of the object to load
     * @param identity The identity of the object to load
     * @param accessMode The desired access mode
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public OID load( TransactionContext tx, Class type, Object identity,
                     AccessMode accessMode, int timeout )
        throws ObjectNotFoundException, LockNotGrantedException,
               PersistenceException, ClassNotPersistenceCapableException
    {
        Object[]   fields;
        OID        oid;
        ObjectLock lock;
        TypeInfo   typeInfo;
        boolean    writeLock;

        typeInfo = (TypeInfo) _typeInfo.get( type );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableExceptionImpl( type );

        // Create an OID to represent the object and see if we
        // have a lock (i.e. object is cached).
        oid = new OID( typeInfo.handler, identity );
        lock = typeInfo.cache.getLockForAquire( oid );
        writeLock = ( accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked );

        if ( lock != null ) {
            // Object has been loaded before, must acquire lock
            // on it (write in exclusive mode)
            try {
                fields = (Object[]) lock.acquire( tx, writeLock, timeout );
            } catch ( ObjectDeletedWaitingForLockException except ) {
                // This is equivalent to object not existing
                throw new ObjectNotFoundExceptionImpl( type, identity );
            } finally {
                // signal cache that it's now safe to release the object
                // from transcation state to cache state.
                typeInfo.cache.finishLockForAquire( oid, lock );
            }
            // Get the actual OID of the object, this one contains the
            // object's stamp that will be used for dirty checking.
            oid = lock.getOID();

            // XXX Problem, obj might be parent class but attempting
            //     to load derived class, will still return parent class
            //     Need to solve by swapping to a new object

            if ( writeLock && ! oid.isDbLock() ) {
                // Db-lock mode we always synchronize the object with
                // the database and obtain a lock on the object.
                try {
                    if ( _logInterceptor != null )
                        _logInterceptor.loading( typeInfo.javaClass, identity );
                    oid.setStamp( typeInfo.persist.load( tx.getConnection( this ),
                                                         fields, identity, accessMode ) );
                    if ( accessMode == AccessMode.DbLocked )
                        oid.setDbLock( true );
                } catch ( ObjectNotFoundException except ) {
                    // Object was not found in persistent storge, must dump
                    // it from the cache
                    typeInfo.cache.removeLock( oid );
                    lock.delete( tx );
                    throw except;
                } catch ( PersistenceException except ) {
                    // Report any error talking to the persistence engine
                    typeInfo.cache.removeLock( oid );
                    lock.delete( tx );
                    throw except;
                }
                // At this point the object is known to exist in
                // persistence storage and we have a write lock on it.
                return oid;
            } else {
                // Non-exclusive mode, we do not attempt to touch the database
                // at this point, simply return the object's oid.
                return oid;
            }

        } else {

            // Object has not been loaded yet, or cleared from the cache.
            // The object is now loaded and a lock is acquired.
            fields = typeInfo.handler.newFieldSet();
            try {
                if ( _logInterceptor != null )
                    _logInterceptor.loading( typeInfo.javaClass, identity );
                oid.setStamp( typeInfo.persist.load( tx.getConnection( this ),
                                                     fields, identity, accessMode ) );
            } catch ( ObjectNotFoundException except ) {
                // Object was not found in persistent storge
                throw except;
            } catch ( PersistenceException except ) {
                // Report any error talking to the persistence engine
                throw except;
            } 
            // Create a lock for the object, register the lock and OID.
            // The lock is created for read or write depending on the
            // mode.
            lock = new ObjectLock( fields, oid );
            try {
                lock.acquire( tx, writeLock, 0 );

            } catch ( Exception except ) {
                // This should never happen since we just created the lock
            }
            if ( accessMode == AccessMode.DbLocked )
                oid.setDbLock( true );
            typeInfo.cache.addLock( oid, lock );
            return oid;
        }
    }


    /**
     * Loads an object of the specified type and identity from the
     * query results. In exclusive mode the object is always loaded
     * and a write lock is obtained on the object, preventing
     * concurrent updates. In non-exclusive mode the object is either
     * loaded or obtained from the cache with a read lock. The object's
     * OID is always returned, this OID must be used in subsequent
     * operations on the object. Must call {@link #copyObject} to obtain
     * the object.
     *
     * @param tx The transaction context
     * @param query The query persistence engine
     * @param identity The identity of the object to load
     * @param accessMode The desired access mode
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
     * @throws ObjectNotFoundException The object was not found in
     *  persistent storage
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public OID fetch( TransactionContext tx, PersistenceQuery query, Object identity,
                      AccessMode accessMode, int timeout )
        throws ObjectNotFoundException, LockNotGrantedException,
               PersistenceException
    {
        Object[]   fields;
        OID        oid;
        ObjectLock lock;
        TypeInfo   typeInfo;
        boolean    writeLock;

        typeInfo = (TypeInfo) _typeInfo.get( query.getResultType() );
        // Create an OID to represent the object and see if we
        // have a lock (i.e. object is cached).
        oid = new OID( typeInfo.handler, identity );
        lock = typeInfo.cache.getLockForAquire( oid );
        writeLock = ( accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked );

        if ( lock != null ) {

            // Object has been loaded before, must acquire lock on it
            try {
                fields = (Object[]) lock.acquire( tx, writeLock, timeout );
            } catch ( ObjectDeletedWaitingForLockException except ) {
                // This is equivalent to object not existing
                throw new ObjectNotFoundExceptionImpl( query.getResultType(), identity );
            } finally {

                // signal cache that it's now safe to release the object
                // from transcation state to cache state.
                typeInfo.cache.finishLockForAquire( oid, lock );

            }
            // Get the actual OID of the object, this one contains the
            // object's stamp that will be used for dirty checking.
            oid = lock.getOID();

            // XXX Problem, obj might be parent class but attempting
            //     to load derived class, will still return parent class
            //     Need to solve by swapping to a new object

            if ( writeLock && ! oid.isDbLock() ) {
                // In db-lock mode we always synchronize the object with
                // the database and obtain a lock on the object.
                try {
                    if ( _logInterceptor != null )
                        _logInterceptor.loading( typeInfo.javaClass, identity );
                    oid.setStamp( query.fetch( fields, identity ) );
                    if ( accessMode == AccessMode.DbLocked )
                        oid.setDbLock( true );
                } catch ( ObjectNotFoundException except ) {
                    // Object was not found in persistent storge, must dump
                    // it from the cache
                    lock.delete( tx );
                    typeInfo.cache.removeLock( oid );

                    throw except;
                } catch ( PersistenceException except ) {
                    // Report any error talking to the persistence engine
                    lock.delete( tx );
                    typeInfo.cache.removeLock( oid );
                    throw except;
                }
                // At this point the object is known to exist in
                // persistence storage and we have a write lock on it.
                return oid;
            } else {
                // Non-exclusive mode, we do not attempt to touch the database
                // at this point, simply return the object's oid.
                return oid;
            }

        } else {

            // Object has not been loaded yet, or cleared from the cache.
            // The object is now loaded from the query and a lock is acquired.
            fields = typeInfo.handler.newFieldSet();
            try {
                if ( _logInterceptor != null )
                    _logInterceptor.loading( typeInfo.javaClass, identity );
                oid.setStamp( query.fetch( fields, identity ) );
            } catch ( ObjectNotFoundException except ) {
                // Object was not found in persistent storge
                throw except;
            } catch ( PersistenceException except ) {
                // Report any error talking to the persistence engine
                throw except;
            }
            // Create a lock for the object, register the lock and OID.
            // The lock is created for read or write depending on the
            // mode.
            lock = new ObjectLock( fields, oid );
            try {
                lock.acquire( tx, writeLock, 0 );
            } catch ( Exception except ) {
                // This should never happen since we just created the lock
            }
            if ( accessMode == AccessMode.DbLocked )
                oid.setDbLock( true );
            typeInfo.cache.addLock( oid, lock );
            return oid;
        }
    }


    /**
     * Creates a new object in this engine. The object must not be
     * persistent and must have a unique identity within this engine.
     * If the identity is specified the object is created in
     * persistent storage immediately with the identity. If the
     * identity is not specified, the object is created only when the
     * transaction commits. The object's OID is returned. The OID is
     * guaranteed to be unique for this engine even if no identity was
     * specified.
     *
     * @param tx The transaction context
     * @param object The newly created object
     * @param identity The identity of the object, or null
     * @return The object's OID
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public OID create( TransactionContext tx, Object object, Object identity )
        throws DuplicateIdentityException, PersistenceException,
               ClassNotPersistenceCapableException
    {
        OID        oid;
        ObjectLock lock;
        Object[]   fields;
        TypeInfo   typeInfo;
        Object     stamp;

        typeInfo = (TypeInfo) _typeInfo.get( object.getClass() );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableExceptionImpl( object.getClass() );

        // Must prevent concurrent attempt to create the same object
        // Best way to do that is through the type
        synchronized ( typeInfo ) {
            oid = new OID( typeInfo.handler, identity );

            if ( identity != null ) {
                // If the object has a known identity at creation time, perform
                // duplicate identity check.
                lock = typeInfo.cache.getLockForAquire( oid );
                if ( lock != null ) {
                    try {
                        int count = 10;
                        while ( true ) {
                            try {
                                fields = (Object[]) lock.acquire( tx, true, 0 );
                                break;
                            } catch ( ObjectDeletedWaitingForLockException e ) {
                                count--;
                                if ( count <= 0 )
                                    throw e;
                            }
                        }
                    } catch ( LockNotGrantedException except ) {
                        // Someone else is using the object, definite duplicate key
                        throw new DuplicateIdentityExceptionImpl( object.getClass(), identity );
                    } finally {

                        // signal cache that it's now safe to release the object
                        // from transcation state to cache state.
                        typeInfo.cache.finishLockForAquire( oid, lock );

                    }
                    // Dump the memory image of the object, it might have been deleted
                    // from persistent storage
                    typeInfo.cache.removeLock( oid );
                    lock.delete( tx );
                }
            }

            // Check integrity of object before creating it, assuring no fields
            // are null, and then place it in the cache. This copy will be deleted
            // if the transaction ends up rolling back.
            try {
                typeInfo.handler.checkValidity( object );
            } catch ( ValidityException except ) {
                throw new PersistenceExceptionImpl( except );
            }
            fields = typeInfo.handler.newFieldSet();
            typeInfo.handler.copyInto( object, fields );

            if ( _logInterceptor != null )
                _logInterceptor.creating( typeInfo.javaClass, identity );
            // Is it creating after delete?
            if ( ! tx.getObjectEntry( object ).deleted ) {
                // Create the object in persistent storage acquiring a lock on the object.
                // If no identity was given for the object, this method will attempt to
                // create an identity using a key generator.
                identity = typeInfo.persist.create( tx.getConnection( this ), fields, identity );
            }
            if ( identity == null )
                throw new PersistenceExceptionImpl( "persist.noIdentity", object.getClass().getName() );
            typeInfo.handler.setIdentity( object, identity );
            oid = new OID( typeInfo.handler, identity );
            oid.setDbLock( true );
            stamp = null;
            // XXX Here we must get the database stamp of the created object, if supported 
            //     (not implemented yet, therefore use the local timestamp).
            oid.setStamp( stamp ); 
            setObjectTimeStamp( object, oid.getStamp() );

            // Copy the contents of the object we just created into the
            // cache engine.
            lock = new ObjectLock( fields, oid );
            try {
                lock.acquire( tx, true, 0 );
            } catch ( Exception except ) {
                // This should never happen since we just created the lock
            }
            oid.setDbLock( true );

            // Store/create/delete all the dependent objects last since
            // dependent objects reference main object.
            RelationHandler[] relations;

            relations = typeInfo.handler.getRelations();
            for ( int i = 0 ; i < relations.length ; ++i ) {
                if ( relations[ i ] != null && relations[ i ].isAttached() ) {
                    Object related;

                    if ( relations[ i ].isMulti() ) {
                        Enumeration enum;

                        enum = (Enumeration) relations[ i ].getRelated( object );
                        if ( enum != null ) {
                            while ( enum.hasMoreElements() ) {
                                related = enum.nextElement();
                                if ( related != null && ! tx.isPersistent( related ) )
                                    tx.create( this, related, relations[ i ].getIdentity( related ) );
                            }
                        }
                    }
                }
            }

            // Add the object to the cache.
            typeInfo.cache.addLock( oid, lock );
        }
        return oid;
    }


    /**
     * Called at transaction commit time to delete the object. Object
     * deletion occurs in three distinct steps:
     * <ul>
     * <li>A write lock is obtained on the object to assure it can be
     *     deleted and the object is marked for deletion in the
     *     transaction context
     * <li>As part of transaction preparation the object is deleted
     *     from persistent storage using this method
     * <li>The object is removed from the cache when the transaction
     *     completes with a call to {@link #forgetObject}
     * </ul>
     *
     * @param tx The transaction context
     * @param type The object type
     * @param identity The object's identity
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void delete( TransactionContext tx, Class type, Object identity )
        throws PersistenceException
    {
        ObjectLock lock;
        TypeInfo   typeInfo;
        OID        oid;
        Object[]   fields;

        typeInfo = (TypeInfo) _typeInfo.get( type );
        oid = new OID( typeInfo.handler, identity );
        // Get the lock from the OID. Assure the object has a write
        // lock -- since this was done during the transaction, we
        // don't wait to acquire the lock.
        try {            
            lock = typeInfo.cache.getLockForAquire( oid );
            lock.acquire( tx, true, 0 );
            if ( lock == null )
                throw new IllegalStateException( Messages.format( "persist.internal",
                        "Attempt to delete object for which no lock was acquired" ) );
        } catch ( LockNotGrantedException except ) {
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to delete object for which no lock was acquired" ) );
        }
        
        if ( _logInterceptor != null )
            _logInterceptor.removing( typeInfo.javaClass, oid.getIdentity() );
        typeInfo.persist.delete( tx.getConnection( this ), oid.getIdentity() );
    }



    void markDelete( TransactionContext tx, OID oid, int timeout )
        throws PersistenceException
    {
        ObjectLock lock = null;
        TypeInfo   typeInfo;
        Object[]   fields;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        try {   
            lock = typeInfo.cache.getLockForAquire( oid );
        } finally {
            typeInfo.cache.finishLockForAquire( oid, lock );
        }

        if ( lock == null || ! lock.hasLock( tx, false ) )
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to delete object for which no lock was acquired" ) );
        try {
            fields = (Object[]) lock.acquire( tx, true, 0 );
        } catch ( LockNotGrantedException except ) {
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to delete object for which no lock was acquired" ) );
        }
        
        // Store/create/delete all the dependent objects first. Must perform that
        // operation on all descendent classes.
        RelationHandler[] relations;

        relations = typeInfo.handler.getRelations();
        for ( int i = 0 ; i < relations.length ; ++i ) {
            if ( relations[ i ] != null && fields[ i ] != null &&
                 relations[ i ].isAttached() ) {
                if ( relations[ i ].isMulti() ) {
                    Vector  related;
                    Object  relIdentity;

                    related = (Vector) fields[ i ];
                    for ( int j = 0 ; j < related.size() ; ++j ) {
                        relIdentity = related.elementAt( j );
                        if ( relIdentity != null )
                            tx.markDelete( this, relations[ i ].getRelatedClass(), relIdentity );
                    }
                }
            }
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
     * @param tx The transaction context
     * @param type The type of the object to load
     * @param object The object
     * @param accessMode The desired access mode
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @return The object's OID
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
    public OID update( TransactionContext tx, Class type, Object object,
                       AccessMode accessMode, int timeout )
        throws ObjectNotFoundException, LockNotGrantedException, ObjectModifiedException,
               PersistenceException, ClassNotPersistenceCapableException
    {
        Object[]   fields;
        Object     identity;
        OID        oid;
        ObjectLock lock;
        TypeInfo   typeInfo;
        boolean    writeLock;

        // If the object is new, don't try to load it from the cache
        if ( ( object instanceof TimeStampable ) &&
                ( ( TimeStampable ) object ).jdoGetTimeStamp() == 0 ) {
            return null;
        }

        typeInfo = (TypeInfo) _typeInfo.get( type );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableExceptionImpl( type );

        // Create an OID to represent the object and see if we
        // have a lock (i.e. object is cached).
        identity = typeInfo.handler.getIdentity( object );
        oid = new OID( typeInfo.handler, identity );
        lock = typeInfo.cache.getLockForAquire( oid );
        writeLock = ( accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked );

        if ( lock != null ) {
            // Object has been loaded before, must acquire lock
            // on it (write in exclusive mode)
            try {
                // Get the actual OID of the object, this one contains the
                // object's stamp that will be used for dirty checking.
                oid = lock.getOID();

                // If it implements TimeStampable interface, verify 
                // the object's timestamp against the cache
                if ( object instanceof TimeStampable ) {
                    if ( oid.getStamp() == null ) 
                        throw new IllegalStateException( Messages.format( "persist.internal",
                                                        "Cache stamp for the TimeStamped object is null " ) );
                    if ( ! checkObjectTimeStamp( object, oid.getStamp() ) ) 
                        throw new ObjectModifiedException( Messages.format( "persist.objectModified", type.getName(), identity ) );
                }
                fields = (Object[]) lock.acquire( tx, writeLock, timeout );
            } catch ( ObjectDeletedWaitingForLockException except ) {
                // This is equivalent to object not existing
                throw new ObjectNotFoundExceptionImpl( type, identity );
            } finally {
                // signal cache that it's now safe to release the object
                // from transcation state to cache state.
                typeInfo.cache.finishLockForAquire( oid, lock );
            }

            // update all dependent and related objects
            RelationHandler[] relations;

            relations = typeInfo.handler.getRelations();
            for ( int i = 0 ; i < relations.length ; ++i ) {
                if ( relations[ i ] != null && relations[ i ].isMulti() ) {
                    Object related;
                    Enumeration enum;
                    Object[] origIdentity;
                    Object relIdentity;

                    // make sure that all dependent objects are included in the transaction,
                    // otherwise objects that should be deleted won't be deleted
                    if ( fields[ i ] == null )
                        origIdentity = new Object[ 0 ];
                    else {
                        origIdentity = new Object[ ( (Vector) fields[ i ] ).size() ];
                        ( (Vector) fields[ i ] ).copyInto( origIdentity );
                    }

                    enum = (Enumeration) relations[ i ].getRelated( object );
                    if ( enum != null ) {
                        while ( enum.hasMoreElements() ) {
                            related = enum.nextElement();
                            if ( related == null )
                                continue;
                            relIdentity = relations[ i ].getIdentity( related );
                            if ( relIdentity == null )
                                continue;
                            tx.update( this, related, relIdentity );
                            for( int j = 0; j < origIdentity.length; j++ ) {
                                if ( origIdentity[ j ] != null && origIdentity[ j ].equals( relIdentity ) ) {
                                    origIdentity[ j ] = null;
                                    break;
                                }
                            }
                        }
                    }

                    if ( relations[ i ].isAttached() ) {
                        for ( int j = 0 ; j < origIdentity.length ; ++j ) {
                            if ( origIdentity[ j ] != null ) {
                                ClassHandler relHandler;

                                relHandler = relations[ i ].getRelatedHandler();
                                tx.load( this, relHandler, origIdentity[ j ], accessMode );
                            }
                        }
                    }
                }
            }

            if ( writeLock && ! oid.isDbLock() ) {
                // Db-lock mode we always synchronize the object with
                // the database and obtain a lock on the object.
                try {
                    if ( _logInterceptor != null )
                        _logInterceptor.loading( typeInfo.javaClass, identity );
                    oid.setStamp( typeInfo.persist.load( tx.getConnection( this ),
                                                         fields, identity, accessMode ) );
                    if ( accessMode == AccessMode.DbLocked )
                        oid.setDbLock( true );
                } catch ( ObjectNotFoundException except ) {
                    // Object was not found in persistent storge, must dump
                    // it from the cache
                    typeInfo.cache.removeLock( oid );
                    lock.delete( tx );
                    throw except;
                } catch ( PersistenceException except ) {
                    // Report any error talking to the persistence engine
                    typeInfo.cache.removeLock( oid );
                    lock.delete( tx );
                    throw except;
                }

                // At this point the object is known to exist in
                // persistence storage and we have a write lock on it.
                return oid;
            } else {
                // Non-exclusive mode, we do not attempt to touch the database
                // at this point, just load the object from the cache
                return oid;
            }
        } else {
            // Object has not been loaded yet, or cleared from the cache.
            // If the object is not new, report an error
            if ( ( object instanceof TimeStampable ) &&
                    ( ( TimeStampable ) object ).jdoGetTimeStamp() != 0 ) {
                throw new ObjectModifiedException( Messages.format( "persist.objectModified", type.getName(), 
                                                   typeInfo.handler.getIdentity( object ) ) );
            }
            // Otherwise indicate that the object should be created
            return null;
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
     * @param tx The transaction context
     * @param object The object to store
     * @param identity The object's identity
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
    public OID store( TransactionContext tx, Object object,
                      Object identity, int timeout )
        throws LockNotGrantedException, ObjectDeletedException,
               ObjectModifiedException, DuplicateIdentityException,
               PersistenceException
    {
        Object[]   fields;
        Object[]   original;
        ObjectLock lock;
        Object     oldIdentity;
        TypeInfo   typeInfo;
        OID        oid;
        short      modified;

        typeInfo = (TypeInfo) _typeInfo.get( object.getClass() );
        oid = new OID( typeInfo.handler, identity );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock == null || ! lock.hasLock( tx, false ) ) {
            typeInfo.cache.finishLockForAquire( oid, lock );
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to store object for which no lock was acquired" ) );
        }                                                           

        // Acquire a read lock first. Only if the object has been modified
        // do we need a write lock.

        try {
            original = (Object[]) lock.acquire( tx, false, 0 );

            // Get the real OID with the exclusive and stamp info.
            oid = lock.getOID();

            // Store/create/delete all the dependent objects first. Must perform that
            // operation on all descendent classes.
            RelationHandler[] relations;

            relations = typeInfo.handler.getRelations();
            for ( int i = 0 ; i < relations.length ; ++i ) {
                if ( relations[ i ] != null ) {

                    // XXX Need validity check and better testing for null elements

                    // relations[ i ].checkValidity( object );
                    if ( relations[ i ].isMulti() ) {
                        Enumeration enum;
                        Object[]    origIdentity;

                        enum = (Enumeration) relations[ i ].getRelated( object );
                        if ( original[ i ] == null )
                            origIdentity = new Object[ 0 ];
                        else {
                            origIdentity = new Object[ ( (Vector) original[ i ] ).size() ];
                            ( (Vector) original[ i ] ).copyInto( origIdentity );
                        }
                        if ( enum != null ) {
                            while ( enum.hasMoreElements() ) {
                                Object related;
                                Object relIdentity;
                                boolean isOriginal = false;
                                
                                related = enum.nextElement();
                                if ( related != null ) {
                                    relIdentity = relations[ i ].getIdentity( related );
                                    for ( int j = 0 ; j < origIdentity.length ; ++j ) {
                                        if ( origIdentity[ j ] != null &&
                                             origIdentity[ j ].equals( relIdentity ) ) {
                                            ClassHandler relHandler;
                                            OID relOid;

                                            relHandler = ((TypeInfo) _typeInfo.get( related.getClass() )).handler;
                                            relOid = new OID( relHandler, origIdentity[ j ] );
                                            isOriginal = ! tx.getObjectEntry( this, relOid ).deleted;
                                            origIdentity[ j ] = null;
                                            break;
                                        }
                                    }
                                    if ( ! isOriginal && ! tx.isPersistent( related ) )
                                        tx.create( this, related, relIdentity );
                                }
                            }
                        }
                        if ( relations[ i ].isAttached() ) {
                            for ( int j = 0 ; j < origIdentity.length ; ++j ) {
                                if ( origIdentity[ j ] != null ) 
                                    tx.markDelete( this, relations[ i ].getRelatedClass(), origIdentity[ j ] );
                            }
                        }
                        
                    }
                }
            }

            // If the object has a identity, it was retrieved/created before and
            // need only be stored. If the object has no identity, the object must
            // be created at this point.
            oldIdentity = oid.getIdentity();
            if ( oldIdentity == null ) {
                // The object has no old identity. This is an object that was
                // created during this transaction and must now be created in
                // persistent storage. A new OID is required to check for
                // duplicate identity.
                try {
                    return create( tx, object, identity );
                } catch ( ClassNotPersistenceCapableException except ) {
                    throw new PersistenceExceptionImpl( "persist.internal", except.toString() );
                }
            } else {
                // Identity change not supported
                if ( ! identity.equals( oldIdentity ) )
                    throw new PersistenceExceptionImpl( "persist.changedIdentity",
                                                        typeInfo.javaClass.getName(), identity );

                // Check if object has been modified, and whether it can be stored.
                modified = typeInfo.handler.isModified( object, original );
                if ( typeInfo.handler.getCallback() != null ) {
                    try {
                        typeInfo.handler.getCallback().storing( object, modified != ClassHandler.Unmodified );
                    } catch ( Exception except ) {
                        throw new PersistenceExceptionImpl( except );
                    }
                    modified = typeInfo.handler.isModified( object, original );
                }
                if ( modified == ClassHandler.Unmodified )
                    return null;
                try {
                    typeInfo.handler.checkValidity( object );
                } catch ( ValidityException except ) {
                    throw new PersistenceExceptionImpl( except );
                }

                // Object has been modified, must write it. Acquire a write lock and
                // block is some other transaction has a read lock on the object.
                // First one to call this method gets to commit.
                original = (Object[]) lock.acquire( tx, /*(*/ ClassHandler.Unmodified != modified /* == ClassHandler.LockRequired )*/, timeout );

                // The object has an old identity, it existed before, one need
                // to store the new contents.
                if ( _logInterceptor != null )
                    _logInterceptor.storing( typeInfo.javaClass, identity );

                // Create a new field set, copy the object into that set and try
                // to update the database. Use the original fields for dirty checking.
                // If the update succeeded, time to reflect that in the cache and
                // proceed to the next step.
                fields = typeInfo.handler.newFieldSet();
                typeInfo.handler.copyInto( object, fields );
                if ( oid.isDbLock() )
                    oid.setStamp( typeInfo.persist.store( tx.getConnection( this ),
                                                          fields, identity, null, null ) );
                else {
                    try {
                        oid.setStamp( typeInfo.persist.store( tx.getConnection( this ),
                                                              fields, identity, original, oid.getStamp() ) );
                    } catch ( ObjectModifiedException except ) {
                        // If object modified in database, remove it from cache (only if we have a
                        // write lock)
                        typeInfo.cache.removeLock( oid );
                        if ( lock.hasLock( tx, true ) )
                            lock.delete( tx );
                        else {
                            try {
                                lock.acquire( tx, true, 0 );
                                lock.delete( tx );
                            } catch ( LockNotGrantedException except2 ) { }
                        }
                        throw except;
                    }
                }
                setObjectTimeStamp( object, oid.getStamp() );
            }
        } finally {
            // signal cache that it's now safe to release the object
            // from transcation state to cache state.
            typeInfo.cache.finishLockForAquire( oid, lock );
        }

        return oid;
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
     * @param tx The transaction context
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
    public void writeLock( TransactionContext tx, OID oid, int timeout )
        throws LockNotGrantedException, PersistenceException
    {
        ObjectLock lock;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock == null || ! lock.hasLock( tx, false ) )
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to lock object for which no lock was acquired" ) );

        // Attempt to obtain a lock on the database. If this attempt
        // fails, release the lock and report the exception.
        try {
        lock.acquire( tx, true, timeout );
        } finally {

            // signal cache that it's now safe to release the object
            // from transcation state to cache state.
            typeInfo.cache.finishLockForAquire( oid, lock );
        }
        try {
            typeInfo.persist.writeLock( tx.getConnection( this ), oid.getIdentity() );
        } catch ( ObjectDeletedException except ) {
            typeInfo.cache.removeLock( oid );
            lock.delete( tx );
            throw except;
        } catch ( PersistenceException except ) {
            typeInfo.cache.removeLock( oid );
            lock.delete( tx );
            throw except;
        }
    }


    /**
     * Acquire a write lock on the object in memory. A soft lock prevents
     * other threads from changing the object, but does not acquire a lock
     * on the database.
     *
     * @param tx The transaction context
     * @param oid The object's OID
     * @param timeout The timeout waiting to acquire a lock on the
     *  object (specified in seconds)
     * @throws LockNotGrantedException Timeout or deadlock occured
     *  attempting to acquire lock on object
     * @throws ObjectDeletedException The object has been deleted from
     *  persistent storage
     */
    public void softLock( TransactionContext tx, OID oid, int timeout )
        throws LockNotGrantedException
    {
        ObjectLock lock;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock == null || ! lock.hasLock( tx, false ) )
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to lock object for which no lock was acquired" ) );

        // Attempt to obtain a lock on the database. If this attempt
        // fails, release the lock and report the exception.
        try {
        lock.acquire( tx, true, timeout );
        } finally {
            // signal cache that it's now safe to release the object
            // from transcation state to cache state.
            typeInfo.cache.finishLockForAquire( oid, lock );
        }

    }


    /**
     * Obtain a copy of the cached object give the object's OID. The
     * cached object is copied into the supplied object without
     * affecting the locks. This method is generally called after a
     * successful return from {@link #load}, the transaction is known
     * to have a read lock on the object.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param object The object into which to copy
     * @param db The database in which the object was created
     * @throws PersistenceException An error reported by the
     *  persistence engine obtaining a dependent object
     */
    public void copyObject( TransactionContext tx, OID oid, Object object, AccessMode accessMode )
        throws PersistenceException
    {
        ObjectLock lock;
        TypeInfo   typeInfo;
        Object[]   fields;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock == null )
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to copy object which is not persistent" ) );

        // Acquire a read lock on the object. This method is generarlly
        // called after a successful return from load(), so we don't
        // want to wait for the lock.
        try {
            fields = (Object[]) lock.acquire( tx, false, 0 );
            /* XXX How do we handle this
            if ( ! obj.getClass().isAssignableFrom( locked.getClass() ) )
                throw new IllegalArgumentException( Messages.format( "persist.typeMismatch",
                                                                     object.getClass(), locked.getClass() ) );
            */
        } catch ( LockNotGrantedException except ) {
            // If this transaction has no write lock on the object,
            // something went foul.
            if ( _logInterceptor != null )
                _logInterceptor.message( Messages.format( "persist.internal", "copyObject: " + except.toString() ) );
            throw new IllegalStateException( except.toString() );
        } finally {

            // signal cache that it's now safe to release the object
            // from transcation state to cache state.
            typeInfo.cache.finishLockForAquire( oid, lock );
        }
        typeInfo.handler.setIdentity( object, oid.getIdentity() );
        typeInfo.handler.copyInto( fields, object, new FetchContext( tx, this, accessMode ) );
        setObjectTimeStamp( object, oid.getStamp() );
    }


    /**
     * Set object's timestamp of type "long" from the OID's stamp of type Object
     * if the object is TimeStampable
     */
    private void setObjectTimeStamp(Object object, Object stamp) {
        long timeStamp = 0;
        
        if ( ! (object instanceof TimeStampable) || ( stamp == null ) ) {
            return;
        }

        if ( stamp instanceof Long ) {
            timeStamp = ( (Long) stamp ).longValue(); 
        } else if ( stamp instanceof Date ) {
            timeStamp = ( (Date) stamp ).getTime(); 
        } else if ( stamp instanceof Integer ) {
            timeStamp = ( (Integer) stamp ).intValue(); 
        } else if ( stamp instanceof BigDecimal ) {
            timeStamp = ( (BigDecimal) stamp ).longValue(); 
        }
        ( (TimeStampable) object ).jdoSetTimeStamp( timeStamp );
    }


    /**
     * Check equality of object's timestamp of type "long" and the OID's stamp of type Object
     * if the object is TimeStampable, otherwise return true
     */
    private boolean checkObjectTimeStamp(Object object, Object stamp) {
        long timeStamp = 0;
        
        // if object is not instance of TimeStampable or stamp is null, 
        // we assume that the chech should not be performed, all is fine
        if ( ! (object instanceof TimeStampable) || ( stamp == null ) ) {
            return true;
        }

        if ( stamp instanceof Long ) {
            timeStamp = ( (Long) stamp ).longValue(); 
        } else if ( stamp instanceof Date ) {
            timeStamp = ( (Date) stamp ).getTime(); 
        } else if ( stamp instanceof Integer ) {
            timeStamp = ( (Integer) stamp ).intValue(); 
        } else if ( stamp instanceof BigDecimal ) {
            timeStamp = ( (BigDecimal) stamp ).longValue(); 
        }
        return ( timeStamp == ( (TimeStampable) object ).jdoGetTimeStamp() );
    }


    /**
     * Update the cached object with changes done to its copy. The
     * supplied object is copied into the cached object using a write
     * lock. This method is generally called after a successful return
     * from {@link #store} and is assumed to have obtained a write
     * lock.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param object The object to copy from
     */
    public void updateObject( TransactionContext tx, OID oid, Object object )
    {
        ObjectLock lock;
        TypeInfo   typeInfo;
        Object[]   fields;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock == null )
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to copy object which is not persistent" ) );
        // Acquire a write lock on the object. This method is always
        // called after a successful return from store(), so we don't
        // need to wait for the lock
        try {
            fields = (Object[]) lock.acquire( tx, false, 0 );
            typeInfo.handler.copyInto( object, fields );
        } catch ( LockNotGrantedException except ) {
            // If this transaction has no write lock on the object,
            // something went foul.
            if ( _logInterceptor != null )
                _logInterceptor.message( Messages.format( "persist.internal", "updateObject: " + except.toString() ) );
            throw new IllegalStateException( except.toString() );
        } catch ( PersistenceException except ) {
            // This should never happen
        } finally {

            // signal cache that it's now safe to release the object
            // from transcation state to cache state.
            typeInfo.cache.finishLockForAquire( oid, lock );
        }
    }


    /**
     * Reverts an object to the cached copy given the object's OID.
     * The cached object is copied into the supplied object without
     * affecting the locks, loading relations or emitting errors.
     * This method is used during the rollback phase.
     *
     * @param tx The transaction context
     * @param oid The object's oid
     * @param object The object into which to copy
     * @throws PersistenceException An error reported by the
     *  persistence engine obtaining a dependent object
     */
    public void revertObject( TransactionContext tx, OID oid, Object object )
        throws PersistenceException
    {
        ObjectLock lock = null;
        TypeInfo   typeInfo;
        Object[]   fields;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        try {
            lock = typeInfo.cache.getLockForAquire( oid );      
        } finally {
            typeInfo.cache.finishLockForAquire( oid, lock );
        }
        if ( lock == null )
            return;

        // Acquire a read lock on the object. This method is generarlly
        // called after a successful return from load(), so we don't
        // want to wait for the lock.
        try {
            fields = (Object[]) lock.acquire( tx, false, 0 );
            /* XXX How do we handle this
            if ( ! obj.getClass().isAssignableFrom( locked.getClass() ) )
                throw new IllegalArgumentException( Messages.format( "persist.typeMismatch",
                                                                     object.getClass(), locked.getClass() ) );
            */
            typeInfo.handler.setIdentity( object, oid.getIdentity() );
            typeInfo.handler.copyInto( fields, object, null );
        } catch ( LockNotGrantedException except ) {
            // If this transaction has no write lock on the object,
            // ignore it as to not break the rollback.
            if ( _logInterceptor != null )
                _logInterceptor.message( Messages.format( "persist.internal", "copyObject: " + except.toString() ) );
        }
    }


    /**
     * Called at transaction commit or rollback to release all locks
     * held on the object. Must be called for all objects that were
     * queried but not created within the transaction.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void releaseLock( TransactionContext tx, OID oid )
    {
        ObjectLock lock;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        oid.setDbLock( false );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock != null && lock.hasLock( tx, false ) )
            lock.release( tx );
        typeInfo.cache.finishLockForAquire( oid, lock );
    }


    /**
     * Called to release lock held on the object and all its dependents.
     * Is used to provide the ReadOnly mode.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void releaseLockWithDependent( TransactionContext tx, OID oid )
    {
        ObjectLock lock;
        TypeInfo   typeInfo;
        Object[]   fields;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        oid.setDbLock( false );
        lock = typeInfo.cache.getLockForAquire( oid );
        if ( lock != null ) {
            try {
                fields = (Object[]) lock.acquire( tx, true, 0 );
        
                // Release lock for all the dependent objects
                typeInfo.handler.releaseDependent( fields, tx, this );
        
            } catch ( LockNotGrantedException except ) {
            } finally {
                // signal cache that it's now safe to release the object
                // from transcation state to cache state.
                if ( lock.hasLock( tx, false ) )
                    lock.release( tx );
                typeInfo.cache.finishLockForAquire( oid, lock );
            }
        }
    }


    /**
     * Called at transaction commit or rollback to forget an object
     * and release its locks. Must be called for all objects that were
     * created when the transaction aborts, and for all objects that
     * were deleted when the transaction completes. The transaction is
     * known to have a write lock on the object.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void forgetObject( TransactionContext tx, OID oid )
    {
        ObjectLock lock;
        Object[]   fields;
        TypeInfo   typeInfo;
        RelationHandler[] relations;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        lock = typeInfo.cache.getLockForAquire( oid );

        if ( lock != null ) {
            try {
                fields = (Object[]) lock.acquire( tx, true, 0 );
                typeInfo.cache.removeLock( oid );
                lock.delete( tx );
            } catch ( LockNotGrantedException except ) {
                // If this transaction has no write lock on the object,
                // something went foul.
                if ( _logInterceptor != null )
                    _logInterceptor.message( Messages.format( "persist.internal", "forgetObject: " + except.toString() ) );
                throw new IllegalStateException( except.toString() );
            } finally {
                // signal cache that it's now safe to release the object
                // from transcation state to cache state.
                typeInfo.cache.finishLockForAquire( oid, lock );
            }
        }
    }



    /**
     * Returns an association between Xid and transactions contexts.
     * The association is shared between all transactions open against
     * this cache engine through the XAResource interface.
     */
    public Hashtable getXATransactions()
    {
        return _xaTx;
    }


    boolean hasLock( TransactionContext tx, OID oid, boolean write )
        throws PersistenceException
    {
        ObjectLock lock = null;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getJavaClass() );
        try {
            lock = typeInfo.cache.getLockForAquire( oid );
        } finally {
            typeInfo.cache.finishLockForAquire( oid, lock );
        }

        return ( lock != null && lock.hasLock( tx, write ) );
    }



    /**
     * Provides information about an object of a specific type
     * (class). This information includes the object's descriptor and
     * lifecycle interceptor requesting notification about activities
     * that affect an object.
     */
    static class TypeInfo
    {

        /**
         * The Java class represented by this type info.
         */
        final Class        javaClass;

        /**
         * The handler for this class.
         */
        final ClassHandler handler;

        /**
         * The presistence engine for this class.
         */
        final Persistence  persist;

        /**
         * The cache used by this class (always the cache of the top level class).
         */
        final Cache        cache;

        TypeInfo( ClassHandler handler, Persistence persist, Cache cache )
        {
            this.persist = persist;
            this.handler = handler;
            this.javaClass = handler.getJavaClass();
            this.cache = cache;
        }

    }


}



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

import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;
import org.castor.cache.CacheFactoryRegistry;
import org.castor.jdo.engine.ConnectionFactory;
import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.cache.CacheEntry;
import org.castor.util.Configuration;
import org.castor.util.Messages;

import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * LockEngine is a gateway for all the <tt>ClassMolder</tt>s of a persistence 
 * storage. It mantains dirty checking cache state and lock, and provides a 
 * thread safe enviroment for <tt>ClassMolder</tt>. LockEngine garantees that 
 * no two conflicting operations will be let running concurrently for the same object. 
 * <p>
 * For example, it ensures that exactly one transaction may read (load) exclusively
 * on one object; transaction can not deleted an object while the other is 
 * reading it, etc...
 * <p>
 * It also provides caching for a persistence storage. Different {@link Cache} mechanisms
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
 * IMPLEMENTATION NOTES:
 * <p>
 * An object may be persistent in multiple caches at any given
 * time. There is no way to load an object from multiple caches,
 * but an object can be loaded in one engine and then made
 * persistent in another. The engines are totally independent and
 * no conflicts should occur.
 * <p>
 * Each class hierarchy gets its own cache, so caches can be
 * controlled on a class-by-class basis.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date$
 */
public final class LockEngine {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance( LockEngine.class );
    
    private static CacheFactoryRegistry _cacheFactoryRegistry;

    /**
     * Mapping of type information to object types. The object's class is used
     * as the key and {@link TypeInfo} is the value. {@link TypeInfo} provides
     * sufficient information to persist the object, manipulated it in memory
     * and invoke the object's interceptor.
     */
    private HashMap _typeInfo = new HashMap();

    /**
     * All the XA transactions running against this cache engine.
     */
    private HashMap _xaTx = new HashMap();
    
    /**
     * The ConnectionFactory.
     */
    private ConnectionFactory _connectionFactory;
    
    /**
     * Used by the constructor when creating handlers to temporarily
     * hold the persistence factory for use by {@link #getClassMolder}.
     */
    private PersistenceFactory _persistenceFactory;

    /**
     * Construct a new cache engine with the specified mapping table, 
     * persistence engine and the log interceptor.
     *
     * @param  dbName       Name of database configuration.
     * @param  mapResolver  Provides mapping information for objects
     *                      supported by this cache
     * @param  persistenceFactory      Factory for creating persistence engines for each
     *                      object described in the map
     * @throws MappingException Indicate that one of the mappings is invalid
     */
    public LockEngine(final ConnectionFactory connectionFactory,
                      final MappingResolver mapResolver,
                      final PersistenceFactory persistenceFactory)
    throws MappingException {
        if (_cacheFactoryRegistry == null) {
            Configuration config = Configuration.getInstance();
            _cacheFactoryRegistry = new CacheFactoryRegistry(config);
        }
        
        _connectionFactory = connectionFactory;
        _persistenceFactory = persistenceFactory;
        
        try {
            Vector v = ClassMolderHelper.resolve( (MappingLoader) mapResolver, this, _persistenceFactory );
    
            _typeInfo = new HashMap();
            Enumeration enumeration = v.elements();

            HashSet processedClasses = new HashSet();
            HashSet freshClasses = new HashSet();
            // copy things into an arraylist
            while ( enumeration.hasMoreElements() )
                freshClasses.add( enumeration.nextElement() );

            // iterates through all the ClassMolders in the LockEngine.
            // We first create a TypeInfo for all the base class (ie not extends
            // other class) in the first iteration.
            int counter = 0;
            do {
                counter = freshClasses.size();
                Iterator itor = freshClasses.iterator();
                while ( itor.hasNext() ) {
                    ClassMolder molder = (ClassMolder) itor.next();
                    ClassMolder extend = molder.getExtends();

                    if ( extend == null ) {
                        // create new Cache instance for the base type
                        Cache cache = null;
                        try {
                            cache = _cacheFactoryRegistry.getCache(
                                    molder.getCacheParams(),
                                    mapResolver.getClassLoader());
                        } catch (CacheAcquireException e) {
                            String msg = Messages.message("persist.cacheCreationFailed");
                            _log.error(msg, e);
                            throw new MappingException(msg, e);
                        }
                        TypeInfo info = new TypeInfo( molder, new HashMap(), cache ); 
                        _typeInfo.put( molder.getName(), info );
                        itor.remove();
                        processedClasses.add( molder );

                    } else if ( processedClasses.contains( molder.getExtends() ) ) {
                        // use the base type to construct the new type
                        TypeInfo baseInfo = (TypeInfo)_typeInfo.get( extend.getName() );
                        _typeInfo.put( molder.getName(), new TypeInfo( molder, baseInfo ) );
                        itor.remove();
                        processedClasses.add( molder );

                    } else {
                        // do nothing and wait for the next turn
                    }

                }
            } while ( freshClasses.size() > 0 && counter != freshClasses.size() );

            // error if there is molder left.
            if ( freshClasses.size() > 0 ) {
                Iterator itor = freshClasses.iterator();
                while ( itor.hasNext() ) {
                    ClassMolder molder = (ClassMolder)itor.next();
                    _log.error("The base class, " + (molder.getExtends().getName())
                        + ", of the extends class ," + molder.getName() 
                        + " can not be resolved! ");
                }
                throw new MappingException("Some base class can not be resolved!");
            }
            // XXX [SMH]: Remove this comment-block or is it something we need?
            /*
            while ( enumeration.hasMoreElements() ) {
                molder = (ClassMolder) enumeration.nextElement();
                if ( molder.getExtends() != null ) {
                    ClassMolder extend = molder.getExtends();
                    while ( extend.getExtends() != null ) {
                        extend = extend.getExtends();
                    }
                    // ssa, FIXME : Is that part still necessary ?
//                    if ( _typeInfo.containsKey( extend.getName() ) ) {
                    if ( false ) {
                        baseInfo = (TypeInfo)_typeInfo.get( extend.getName() );
                        _typeInfo.put( molder.getName(), baseInfo );
                    } else {
                        waitingForBase.add( molder );
                    }
                } else {
                    LRU lru = LRU.create( molder.getCacheType(), molder.getCacheParam() );

                    info = new TypeInfo( molder, new HashMap(), lru ); 

                    _typeInfo.put( molder.getName(), info );
                }
            }
            // we then iterate through all extended classes in which the 
            // using the base typeInfo.
            enumeration = waitingForBase.elements();
            while ( enumeration.hasMoreElements() ) {
                molder = (ClassMolder) enumeration.nextElement();
                ClassMolder extend = molder.getExtends();
                while ( extend.getExtends() != null ) {
                    extend = extend.getExtends();
                }
                baseInfo = (TypeInfo) _typeInfo.get( extend.getName() );
                if ( baseInfo != null ) {
                    info = new TypeInfo( molder, baseInfo );
                    _typeInfo.put( molder.getName(), info );
                } else {
                    throw new MappingException("Base class "+extend.getName()+" of "+molder.getName()+" not found!");
                }
            } */
        } catch ( ClassNotFoundException e ) {
            throw new MappingException("Declared Class not found!" );
        }
    }
    
    public ConnectionFactory getConnectionFactory() { return _connectionFactory; }

    /**
     * Get classMolder which represents the given java data object class
     * Dependent class will not be returned to avoid persistenting 
     * a dependent class without 
     * @param cls Class instance for whic a class molder should be returned. 
     * @return The class molder for the specified class.
     */
    public ClassMolder getClassMolder( Class cls ) {
        TypeInfo info = (TypeInfo)_typeInfo.get( cls.getName() );
        if ( info != null ) {
            if ( !info.molder.isDependent() ) 
                return info.molder;
        }
        return null;
    }
    
    public ClassMolder getClassMolderWithDependent( Class cls ) {
        TypeInfo info = (TypeInfo)_typeInfo.get( cls.getName() );
        return (info != null) ? info.molder : null;
    }

    public Persistence getPersistence( Class cls ) {
        ClassMolder molder = getClassMolder( cls );
        if ( molder != null )
            return molder.getPersistence();
        return null;
    }

    /**
     * Loads an object of the specified type and identity from
     * persistent storage. In exclusive mode the object is always
     * loaded and a write lock is obtained on the object, preventing
     * concurrent updates. In non-exclusive mode the object is either
     * loaded or obtained from the cache with a read lock. The object's
     * OID is always returned.
     *
     * @param tx The transaction context
     * @param oid The identity of the object to load
     * @param object The type of the object to load
     * @param suggestedAccessMode The desired access mode
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
     * @throws ObjectDeletedWaitingForLockException The object has been deleted, but is waiting for a lock.
     */
    public OID load(TransactionContext tx, OID oid, ProposedEntity proposedObject, AccessMode suggestedAccessMode, int timeout)
    throws ObjectNotFoundException, LockNotGrantedException, PersistenceException,
    ClassNotPersistenceCapableException, ObjectDeletedWaitingForLockException {
        return load(tx, oid, proposedObject, suggestedAccessMode, timeout, null);
    }

    public OID load(TransactionContext tx, OID oid, ProposedEntity proposedObject, AccessMode suggestedAccessMode, int timeout, QueryResults results)
            throws ObjectNotFoundException, LockNotGrantedException, PersistenceException,
            ClassNotPersistenceCapableException, ObjectDeletedWaitingForLockException {

        OID        lockedOid;
        ObjectLock lock;
        TypeInfo   typeInfo;
        boolean    succeed;
        short      action;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", oid.getName() ) );

        ClassMolder molder = oid.getMolder();
        AccessMode accessMode = molder.getAccessMode( suggestedAccessMode );

        succeed = false;

        lock = null;

        try {

            if ( accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked )
                action = ObjectLock.ACTION_WRITE;
            else
                action = ObjectLock.ACTION_READ;

            lock = typeInfo.acquire( oid, tx, action, timeout );

            lockedOid = lock.getOID();

            Object stamp = typeInfo.molder.load(tx, lockedOid, lock, proposedObject, suggestedAccessMode, results);

			// if object has been expanded, return early            
            if (proposedObject.isExpanded()) {
                // Current transaction holds lock for old OID                
                typeInfo.release(oid, tx);
                return oid;
            }
            
            // proposal change: lockedOid parameter is not really neccesary.
            // we can added getOID() method in DepositBox. It make code a little
            // bit clear?
            // And should ClassMolder the one who set stamp?

            succeed = true;

            lockedOid.setStamp( stamp );

            if ( lockedOid != null )
                oid = lockedOid;

            if (_log.isDebugEnabled()) {
                if (proposedObject.isExpanded()) {
                    _log.debug(Messages.format("jdo.loading.with.id", proposedObject.getActualEntityClass(), oid.getIdentity()));
                } else {
                    _log.debug(Messages.format("jdo.loading.with.id", typeInfo.molder.getName(), oid.getIdentity()));
                }
            }
        } catch (ObjectDeletedWaitingForLockException except) {
            // This is equivalent to object does not exist
            throw new ObjectNotFoundException(Messages.format("persist.objectNotFound", oid.getName(), oid.getIdentity()), except);
        } catch (LockNotGrantedException e) {
            if (lock != null) {
                lock.release(tx);
            }
            throw e;
        } finally {
            if ( lock != null ) lock.confirm( tx, succeed );
        }
        return oid;
    }

    /**
     * Mark an object and its related or dependent object to be created
     *
     * @param tx The transaction context
     * @param oid The identity of the object, or null
     * @param object The newly created object
     * @throws PersistenceException An error reported by the persistence engine
     * @throws LockNotGrantedException Timeout or deadlock occured attempting to acquire lock on object.
     */
    public void markCreate( TransactionContext tx, OID oid, Object object )
            throws PersistenceException, LockNotGrantedException {

        TypeInfo   typeInfo;
        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );

        if ( typeInfo == null )
           throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", oid.getName() ) );

        typeInfo.molder.markCreate( tx, oid, null, object );
    }

    /**
     * Creates a new object in the persistence storage. The object must not
     * be persistent and must have a unique identity within this engine.
     * If the identity is specified the object is created in
     * persistent storage immediately with the identity. If the
     * identity is not specified, the object is created only when the
     * transaction commits. The object's OID is returned. The OID is
     * guaranteed to be unique for this engine even if no identity was
     * specified.
     *
     * @param tx The transaction context
     * @param oid The identity of the object, or null
     * @param object The newly created object
     * @return The object's OID
     * @throws DuplicateIdentityException An object with this identity
     *  already exists in persistent storage
     * @throws PersistenceException An error reported by the
     *  persistence engine
     * @throws ClassNotPersistenceCapableException The class is not
     *  persistent capable
     */
    public OID create( TransactionContext tx, OID oid, Object object )
            throws DuplicateIdentityException, PersistenceException,
            ClassNotPersistenceCapableException {

        TypeInfo typeInfo;
        ObjectLock lock;
        OID newoid;
        boolean succeed;

        typeInfo = (TypeInfo) _typeInfo.get( object.getClass().getName() );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableException( Messages.format( "persist.classNotPersistenceCapable", object.getClass().getName()) );
            
        lock = null;

        if ( oid.getIdentity() != null ) {

            lock = null;

            succeed = false;

            try {

                lock = typeInfo.acquire( oid, tx, ObjectLock.ACTION_CREATE, 0 );

                if (_log.isDebugEnabled()) {
                	_log.debug( Messages.format( "jdo.creating.with.id", typeInfo.molder.getName(), oid.getIdentity() ) );
                }

                oid = lock.getOID();

                typeInfo.molder.create( tx, oid, lock, object );

                succeed = true;

                oid.setDbLock( true );

                return oid;
                // should catch some other exception if destory is not succeed
            } catch (LockNotGrantedException except) {
                // Someone else is using the object, definite duplicate key
                throw new DuplicateIdentityException(Messages.format( 
                    "persist.duplicateIdentity", object.getClass().getName(), 
                    oid.getIdentity()), except);
            } catch ( DuplicateIdentityException except ) {
                // we got a write lock and the persistence storage already
                // recorded. Should destory the lock
                //typeInfo.delete( oid, tx );
                throw except;
            } finally {
                if ( lock != null ) 
                    lock.confirm( tx, succeed );
            }
        } else {    // identity is null

            succeed = false;

            try {
                if (_log.isDebugEnabled()) {
                	_log.debug( Messages.format( "jdo.creating.with.id", typeInfo.molder.getName(), oid.getIdentity() ) );
                }

                lock = typeInfo.acquire( oid, tx, ObjectLock.ACTION_CREATE, 0 );

                oid = lock.getOID();

                Object newids = typeInfo.molder.create( tx, oid, lock, object );
                succeed = true;

                oid.setDbLock( true );

                newoid = new OID( oid.getLockEngine(), oid.getMolder(), oid.getDepends(), newids );

                typeInfo.rename( oid, newoid, tx );

                return newoid;
            } catch ( LockNotGrantedException e ) {
                e.printStackTrace();
                throw new PersistenceException( Messages.format("persist.nested","Key Generator Failure. Duplicated Identity is generated!") );
            } finally {
                if ( lock != null )
                    lock.confirm( tx, succeed );
            }
        }
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
     * @param oid The object's identity
     * @throws PersistenceException An error reported by the
     *  persistence engine
     */
    public void delete( TransactionContext tx, OID oid )
            throws PersistenceException {
        TypeInfo   typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );

        try {
            typeInfo.assure( oid, tx, true );

            if (_log.isDebugEnabled()) {
            	_log.debug( Messages.format( "jdo.removing", typeInfo.molder.getName(), oid.getIdentity() ) );
            }

            typeInfo.molder.delete( tx, oid );

        } catch ( LockNotGrantedException except ) {
            throw new IllegalStateException( Messages.format( "persist.internal",
                                                              "Attempt to delete object for which no lock was acquired" ) );
        }
    }

    public void markDelete(TransactionContext tx, OID oid, Object object, int timeout)
        throws PersistenceException, LockNotGrantedException {

        TypeInfo typeInfo = (TypeInfo) _typeInfo.get(oid.getName());

        ObjectLock lock = typeInfo.upgrade(oid, tx, timeout);

        typeInfo.molder.markDelete(tx, oid, lock, object);

        lock.expire();
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
     * @param oid The object's identity
     * @param object The object
     * @param suggestedAccessMode The desired access mode
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
     * @throws ObjectDeletedWaitingForLockException
     */
    public boolean update( TransactionContext tx, OID oid, Object object, AccessMode suggestedAccessMode, int timeout )
            throws ObjectNotFoundException, LockNotGrantedException, ObjectModifiedException,
                   PersistenceException, ClassNotPersistenceCapableException,
                   ObjectDeletedWaitingForLockException {

        TypeInfo   typeInfo;
        ObjectLock lock;
        boolean    succeed;
        // [oleg] these variables are not used
        //boolean    write;
        //AccessMode accessMode;

        // If the object is new, don't try to load it from the cache

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", oid.getName() ) );

        //accessMode = typeInfo.molder.getAccessMode( suggestedAccessMode );
        //write = ( accessMode == AccessMode.Exclusive || accessMode == AccessMode.DbLocked );
        succeed = false;
        lock = null;
        try {
            // Create an OID to represent the object and see if we
            // have a lock (i.e. object is cached).

            // Object has been loaded before, must acquire lock
            // on it (write in exclusive mode)

            // [Yip] I rather limited update to always acquire read lock
            // (preferrably dblock), to avoid further concurrency problem.
            lock = typeInfo.acquire( oid, tx, ObjectLock.ACTION_UPDATE, timeout );

            /*
            if ( write && ! oid.isDbLock() ) {
                // Db-lock mode we always synchronize the object with
                // the database and obtain a lock on the object.
                _log.debug( Messages.format( "jdo.loading", typeInfo.javaClass, OID.flatten( oid.getIdentities() ) ) );
            }*/
            oid = lock.getOID();

            boolean creating = typeInfo.molder.update( tx, oid, lock, object, suggestedAccessMode );

            if ( creating )
                succeed = false;
            else
                succeed = true;

            return creating;

            /*
            if ( accessMode == AccessMode.DbLocked )
                oid.setDbLock( true );
             */
            /*
            if ( accessMode == AccessMode.ReadOnly )
                typeInfo.release( oid, tx );
            */
        } catch (ObjectModifiedException e) {
            throw e;
        } catch (ObjectDeletedWaitingForLockException except) {
            // This is equivalent to object not existing
            throw new ObjectNotFoundException(Messages.format("persist.objectNotFound", oid.getName(), oid.getIdentity()), except);
        } finally {
            if ( lock != null )
                lock.confirm( tx, succeed );
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
    public OID preStore( TransactionContext tx, OID oid, Object object, int timeout ) 
            throws LockNotGrantedException, PersistenceException {


        TypeInfo   typeInfo;
        ObjectLock lock = null;
        boolean    modified;


        typeInfo = (TypeInfo) _typeInfo.get( object.getClass().getName() );

        // Acquire a read lock first. Only if the object has been modified
        // do we need a write lock.

        oid = new OID( this, typeInfo.molder, oid.getIdentity() );

        // acquire read lock
        // getLockedField();
        // isPersistFieldChange()?
        // if no, return null
        // if yes, get flattened fields, 
        // acquire write lock
        // setLockedField( );
        try {
            lock = typeInfo.assure( oid, tx, false );

            oid = lock.getOID();

            modified = typeInfo.molder.preStore( tx, oid, lock, object, timeout );
        } catch ( LockNotGrantedException e ) {
            throw e;
        } catch ( ObjectModifiedException e ) {
            lock.invalidate( tx );
            throw e;
        } catch ( ObjectDeletedException e ) {
            lock.delete( tx );
            throw e;
        }

        if ( modified ) {
            return oid;
        }

        return null;
    }

    public void store( TransactionContext tx, OID oid, Object object ) 
            throws LockNotGrantedException, ObjectDeletedException,
            ObjectModifiedException, DuplicateIdentityException,
            PersistenceException {

        ObjectLock lock = null;
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        // Attempt to obtain a lock on the database. If this attempt
        // fails, release the lock and report the exception.

        try {
            lock = typeInfo.assure( oid, tx, false );

            if (_log.isDebugEnabled ()) {
            	_log.debug( Messages.format( "jdo.storing.with.id", typeInfo.molder.getName(), oid.getIdentity() ) );
            }

            typeInfo.molder.store( tx, oid, lock, object );
        } catch ( ObjectModifiedException e ) {
            lock.invalidate( tx );
            throw e;
        } catch ( DuplicateIdentityException e ) {
            throw e;
        } catch ( LockNotGrantedException e ) {
            throw e;
        } catch ( PersistenceException e ) {
            lock.invalidate( tx );
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
            throws ObjectDeletedException, LockNotGrantedException, PersistenceException {

        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        // Attempt to obtain a lock on the database. If this attempt
        // fails, release the lock and report the exception.

        try {
            typeInfo.upgrade( oid, tx, timeout );

            // typeInfo.engine.writeLock( tx, lock...);
        } catch ( IllegalStateException e ) {
            throw e;
        } catch ( ObjectDeletedWaitingForLockException e ) {
            throw new IllegalStateException("Object deleted waiting for lock?????????");
        } catch ( LockNotGrantedException e ) {
            throw e;
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
     *  persistent storage
     */
    public void softLock( TransactionContext tx, OID oid, int timeout )
            throws LockNotGrantedException {
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        typeInfo.upgrade( oid, tx, timeout );
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
            throws PersistenceException {
        TypeInfo   typeInfo;
        ObjectLock lock;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        try {
            lock = typeInfo.assure( oid, tx, false );
            typeInfo.molder.revertObject( tx, oid, lock, object );
        } catch ( LockNotGrantedException e ) {
            throw new IllegalStateException("Write Lock expected!");
        } catch ( PersistenceException except ) {
            //typeInfo.destory( oid, tx );
            throw except;
        }
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
    public void updateCache( TransactionContext tx, OID oid, Object object ) {
        TypeInfo   typeInfo;
        ObjectLock lock;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        lock = typeInfo.assure( oid, tx, true );
        typeInfo.molder.updateCache( tx, oid, lock, object );
    }

    /**
     * Called at transaction commit or rollback to release all locks
     * held on the object. Must be called for all objects that were
     * queried but not created within the transaction.
     *
     * @param tx The transaction context
     * @param oid The object OID
     */
    public void releaseLock( TransactionContext tx, OID oid ) {
        ObjectLock lock;
        TypeInfo   typeInfo;
        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        lock = typeInfo.release( oid, tx );
        lock.getOID().setDbLock( false );

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
    public void forgetObject( TransactionContext tx, OID oid ) {
        TypeInfo   typeInfo;

        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        //lock = typeInfo.locks.aquire( oid, tx );
        typeInfo.assure( oid, tx, true );
        typeInfo.delete( oid, tx );
        typeInfo.release( oid, tx );
    }

    /**
     * Expire object from the cache.  If the object to be expired is currently
     * cached, then a write lock is first acquired for this object. In addition,
     * a write lock is acquired on all objects related to, or contained within,
     * this object.  The version of the objects represented by their locks is
     * then marked as "expired".  Upon the release of each write lock
     * (@see TransactionContext#expireCache), the cached version of the objects
     * will not be placed back in the cache (@see TypeInfo#release).
     * A subsequent read/query transaction will therefore load the values of
     * the object from persistent storage.
     *
     * @param tx The transaction context
     * @param oid The object OID
     * @param timeout The max time to wait while acquiring a lock on the
     *  object (specified in seconds)
     * @return True if the object was expired successfully from the cache.
     * @throws LockNotGrantedException Timeout or deadlock occured attempting to acquire lock on object
     * @throws PersistenceException An error reported by the persistence engine
     * @throws ClassNotPersistenceCapableException The class is not persistent capable
     * @throws ObjectModifiedException Dirty checking mechanism may immediately
     *  report that the object was modified in the database during the long
     *  transaction.
     * @throws ObjectDeletedException Object has been deleted from the persistence store.
     */
    public boolean expireCache( TransactionContext tx, OID oid, int timeout )
        throws ClassNotPersistenceCapableException, LockNotGrantedException,
        ObjectDeletedException, PersistenceException
    {
        TypeInfo   typeInfo;
        boolean    succeed;
        ObjectLock lock;
 
        typeInfo = (TypeInfo) _typeInfo.get( oid.getName() );
        if ( typeInfo == null )
            throw new ClassNotPersistenceCapableException( Messages.format("persist.classNotPersistenceCapable", oid.getName() ) );
   
        succeed = false;
        lock = null;
        try {
            if (typeInfo.isCached(oid)) {
                lock = typeInfo.acquire( oid, tx, ObjectLock.ACTION_WRITE, timeout );
                typeInfo.molder.expireCache(tx, lock);
                lock.expire();
                succeed = true;
            }
        } catch ( LockNotGrantedException e ) {
            throw e;
        } catch ( ObjectDeletedException e ) {
            throw e;
        } catch ( PersistenceException e ) {
            throw e;
        } finally {
            if ( lock != null ) lock.confirm( tx, succeed );
        }
 
        return succeed;
    }
 
    /**
     * Forces the cache to be expired for the object represented by
     * ClassMolder and identity.  If identity is null then expire
     * all objects of the type represented by ClassMolder.
     * @param cls Class type instance.
     */
    public void expireCache(Class cls) {
        TypeInfo typeInfo = (TypeInfo) _typeInfo.get(cls.getName());
        if (typeInfo != null) {
            typeInfo.expireCache();
        }
    }
    /**
     * Expires all objects of all types from cache.
     */
    public void expireCache() {
        for (Iterator iter = _typeInfo.values().iterator(); iter.hasNext();) {
            ((TypeInfo) iter.next()).expireCache();
        }
    }
    /**
     * Dump cached objects of all types to output.
     */
    public void dumpCache() {
        for (Iterator iter = _typeInfo.values().iterator(); iter.hasNext();) {
            ((TypeInfo) iter.next()).dumpCache();
        }
    }

    /**
     * Close all caches (to allow for resource clean-up)
     */
    public void closeCaches() {
        for (Iterator iter = _typeInfo.values().iterator(); iter.hasNext();) {
            ((TypeInfo) iter.next()).closeCache();
        }
        
        for (Iterator iter = _cacheFactoryRegistry.getCacheFactories().iterator(); iter.hasNext(); ) {
            ((CacheFactory) iter.next()).shutdown(); 
        }
    }

    /**
     * Dump cached objects of specific type to output.
     * @param cls A class type.
     */
    public void dumpCache(Class cls) {
        TypeInfo typeInfo = (TypeInfo) _typeInfo.get(cls.getName());
        if (typeInfo != null) {
            typeInfo.dumpCache();
        }
    }

    /**
     * Returns an association between Xid and transactions contexts.
     * The association is shared between all transactions open against
     * this cache engine through the XAResource interface.
     * @return Association between XId and transaction contexts.
     */
    public HashMap getXATransactions()
    {
        return _xaTx;
    }


    /**
     * Provides information about an object of a specific type (class's full name).
     * This information includes the object's descriptor and lifecycle interceptor
     * requesting notification about activities that affect an object.
     */
    private class TypeInfo {
        /** The molder for this class. */
        private final ClassMolder molder;

        /** The full qualified name of the Java class represented by this type info. */
        private final String name;

        /** The Map contains all the in-used ObjectLock of the class type, which
         *  keyed by the OID representing the object. All extends classes share the
         *  same map as the base class. */
        private final HashMap locks;
        
        /** The Map contains all the freed ObjectLock of the class type, which keyed
         *  by the OID representing the object. ObjectLock put into cache maybe
         *  disposed by LRU mechanisum. All extends classes share the same map as the
         *  base class. */
        private final Cache cache;

        /**
         * Constructor for creating base class info.
         *
         * @param  molder   The classMolder of this type.
         * @param  locks    The new HashMap which will be used
         *         for holding all the in-used ObjectLock.
         * @param  cache    The new LRU which will be used to
         *         store and dispose freed ObjectLock.
         */
        private TypeInfo(ClassMolder molder, HashMap locks, Cache cache) {
            this.name = molder.getName();
            this.molder = molder;
            this.locks = locks;
            this.cache = cache;
        }

        /**
         * Constructor for creating extended class info.
         * 
         * @param  molder   The classMolder of this type.
         * @param  base     The TypeInfo of the base class of
         *         the molder's class.
         */
        private TypeInfo( ClassMolder molder, TypeInfo base ) {
            this(molder, base.locks, base.cache);
        }

        /**
         * Life-cycle method to allow shutdown of cache instances.
         */
        public void closeCache() {
            cache.close();
        }
        
        /**
         * Dump all objects in cache or lock to output.
         */
        public void dumpCache() {
			_log.info(name + ".dumpCache()...");
            synchronized (locks) {
                for (Iterator iter = locks.values().iterator(); iter.hasNext();) {
                    ObjectLock entry = (ObjectLock) iter.next();
                    _log.info("In locks: " + entry);
                }

                for (Iterator iter = cache.values().iterator(); iter.hasNext();) {
                    ObjectLock entry = (ObjectLock) iter.next();
                    _log.info("In cache: " + entry.getOID());
                }
            }
        }
        
        /**
         * Expire all objects of this class from the cache.
         */
        public void expireCache() {
            synchronized (locks) {
                // Mark all objects currently participating in a
                // transaction as expired.  They will be not be added back to
                // the LRU when the transaction's complete (@see release)
                // XXX [SMH]: Reconsider removing from locks (unknown side-effects?).
                for (Iterator iter = locks.values().iterator(); iter.hasNext();) {
                    ObjectLock objectLock = (ObjectLock) iter.next();
                    objectLock.expire();
                    iter.remove();
                }
                
                // Remove all objects not participating in a transaction from the cache.
                cache.clear();
            }
        }

        /**
         * Acquire the object lock for transaction. After this method is called,
         * user must call {@link ObjectLock#confirm(TransactionContext, boolean)} 
         * exactly once.
         *
         * @param oid        The OID of the lock.
         * @param tx         The context of the transaction to acquire lock.
         * @param lockAction The inital action to be performed on the lock.
         * @param timeout    The time limit to acquire the lock.
         * @return The object lock for the OID within this transaction context. 
         * @throws ObjectDeletedWaitingForLockException
         * @throws LockNotGrantedException Timeout or deadlock occured attempting
         *         to acquire lock on object
         * @throws ObjectDeletedException Object has been deleted from the
         *         persistence store.
         */
        private ObjectLock acquire(OID oid, TransactionContext tx,
                                   short lockAction, int timeout)
        throws ObjectDeletedWaitingForLockException,
               LockNotGrantedException, ObjectDeletedException {
            
            ObjectLock entry = null;
            // sync on "locks" is, unfortunately, necessary if we employ
            // some LRU mechanism, especially if we allow NoCache, to avoid
            // duplicated LockEntry exist at the same time.
            synchronized (locks) {
                entry = (ObjectLock) locks.get(oid);
                if (entry == null) {
                    CacheEntry cachedEntry = (CacheEntry) cache.remove(oid);
                    if (cachedEntry != null) {
                        entry = new ObjectLock(cachedEntry);
                        locks.put(oid, entry);
                        
                        OID cacheOid = entry.getOID();
                        if (oid.getName().equals(cacheOid.getName())) {
                            entry.setOID(oid);
                            locks.put(oid, entry);
                        } else {
                            entry = null;
                        }
                    }
                }
                
                if (entry == null) {
                    entry = new ObjectLock(oid);
                    locks.put(oid, entry);
                } else {
                    oid = entry.getOID();
                }
                entry.enter();
            }
            
            // ObjectLock.acquire() may call Object.wait(), so a thread can not
            // been synchronized with ANY shared object before acquire().
            // So, it must be called outside synchronized( locks ) block.
            boolean failed = true;
            try {
                switch (lockAction) {
                case ObjectLock.ACTION_READ:
                    entry.acquireLoadLock(tx, false, timeout);
                    break;

                case ObjectLock.ACTION_WRITE:
                    entry.acquireLoadLock(tx, true, timeout);
                    break;

                case ObjectLock.ACTION_CREATE:
                    entry.acquireCreateLock(tx);
                    break;

                case ObjectLock.ACTION_UPDATE:
                    entry.acquireUpdateLock(tx, timeout);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "lockType " + lockAction + " is undefined!"); 
                }
                
                failed = false;
                return entry;
            } finally {
                synchronized (locks) {
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
                            locks.remove(oid);
                            if (entry.isExpired()) {
                                cache.expire(oid);
                                entry.expired();
                            } else {
                                cache.put(oid, new CacheEntry(entry));
                            }
                        }
                    }
                }
            }
        }

        /**
         * Upgrade the lock to write lock.
         * 
         * @param  oid      The OID of the lock.
         * @param  tx       The transaction in action.
         * @param  timeout  Time limit.
         * @return The upgraded ObjectLock instance.
         * @throws ObjectDeletedWaitingForLockException
         * @throws LockNotGrantedException Timeout or deadlock occured attempting
         *         to acquire lock on object.
         */
        private ObjectLock upgrade(OID oid, TransactionContext tx, int timeout)
        throws ObjectDeletedWaitingForLockException, LockNotGrantedException {
            ObjectLock entry = null;
            synchronized (locks) {
                entry = (ObjectLock) locks.get(oid);
                if (entry == null) {
                    throw new ObjectDeletedWaitingForLockException(
                            "Lock entry not found. Deleted?");
                }
                if (!entry.hasLock(tx, false)) {
                    throw new IllegalStateException(
                            "Transaction does not hold the any lock on " + oid + "!");    
                }
                oid = entry.getOID();
                entry.enter();
            }
            
            try {
                entry.upgrade(tx, timeout);
                return entry;
            } finally {
                synchronized (locks) {
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
        private ObjectLock assure(OID oid, TransactionContext tx, boolean write) {
            synchronized (locks) {
                ObjectLock entry = (ObjectLock) locks.get(oid);
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
        private ObjectLock rename(OID orgoid, OID newoid, TransactionContext tx)
        throws LockNotGrantedException {
            synchronized (locks) {
                ObjectLock entry, newentry;
                entry = (ObjectLock) locks.get(orgoid);
                newentry = (ObjectLock) locks.get(newoid);

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

                entry = (ObjectLock) locks.remove(orgoid);
                entry.setOID(newoid);
                locks.put(newoid, entry);

                // copy oid status
                newoid.setDbLock(orgoid.isDbLock());
                newoid.setStamp(orgoid.getStamp());

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
        private ObjectLock delete(OID oid, TransactionContext tx) {
            ObjectLock entry;
            synchronized (locks) {
                entry = (ObjectLock) locks.get(oid);
                if (entry == null) {
                    throw new IllegalStateException("No lock to destroy!");
                }
                entry.enter();
            }

            try {
                entry.delete(tx);
                return entry;
            } finally {
                synchronized (locks) {
                    entry.leave();
                    if (entry.isDisposable()) {
                        cache.put(oid, new CacheEntry(entry));
                        locks.remove(oid);
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
        private ObjectLock release(OID oid, TransactionContext tx) {
            ObjectLock entry = null;
            synchronized (locks) {
                entry = (ObjectLock) locks.get( oid );
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
                synchronized (locks) {
                    entry.leave();
                    if (entry.isDisposable()) {
                        cache.put(oid, new CacheEntry(entry));
                        if (entry.isExpired()) {
                            cache.expire(oid);
                            entry.expired();
                        }
                        locks.remove( oid );
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
		public boolean isCached(Object oid) {
            return cache.containsKey(oid);
		}
        
        /**
         * Indicates whether an object with the specified OID is currently locked.
         * 
         * @param oid Object identifier.
         * @return True if the object is locked.
         */
        public boolean isLocked(OID oid)
        {
            return locks.containsKey(oid);
        }
    }

	/**
     * Provides information about whether an object of Class cls with identity iod is currently cached.
     * 
     * @param cls Class type.
     * @param oid Object identity
     * @return True if the specified object is in the cache.
     */
	public boolean isCached(Class cls, Object oid) {
        TypeInfo typeInfo = (TypeInfo) _typeInfo.get(cls.getName());
        return typeInfo.isCached (oid);
	}

	public boolean isLocked(Class cls, OID oid) {
        TypeInfo typeInfo = (TypeInfo) _typeInfo.get(cls.getName());
        return typeInfo.isLocked (oid);
	}
}

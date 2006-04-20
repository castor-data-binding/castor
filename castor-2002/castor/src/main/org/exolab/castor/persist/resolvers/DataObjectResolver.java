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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.persist.resolvers;

import java.util.Map;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.Collection;
import org.exolab.castor.util.Messages;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.session.TransactionContext;
import org.exolab.castor.persist.session.OID;
import org.exolab.castor.persist.AccessMode;
import org.exolab.castor.persist.Entity;
import org.exolab.castor.persist.EntityInfo;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.DataObjectAccessException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;

/**
 * A DataObjectResolver
 *
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 */
public class DataObjectResolver extends Resolver {

    // Implementation note: 
    // There is 5 phases of a data object
    // 1/ it is either queried/created/updated.
    // 2/ it is markDeleted/locked/softLocked
    // 3/ it is preStored/preDeleted
    // 4/ it is stored, if modified /deleted if marked for deletion 
    // 5a/ it is revert if transaction failed
    // 5b/ it is released/forgotten
    // EBNF grammer for the sequence of actions of an data object 
    // is as of the following:
    //   BEGIN         := ( CREATE | Load() | Update() )  ( FAILED | UPGRADE )
    //   CREATE        := preCreate() | create() | postCreate()
    //   FAILED        := releaseLock() DONE 
    //   UPGRADE       := ( [ softLock() | lock() ]*  FAILEDUPGRADE ) PERSISTENCE
    //   FAILEDUPGRADE := PERSISTENCE
    //   PERSISTENCE   := ( markDelete() ( FAILEDUPGRADE | COMMIT ) ) | PREPARE
    //   PREPARE       := ( ( preStore() markDelete() ) | preStore() | markDelete() ) COMMIT
    //   COMMIT        := [ delete() | store() ] POSTCOMMIT
    //   POSTCOMMIT    := [ revert() ]  DONE
    //   DONE          := *END*


    /**
     * The root of the EntityInfo tree that this DataObjectResolver 
     * reponsible for.
     */
    protected EntityInfo     entityInfo;

    /**
     * The collection contains all ResolvingStrategy for the EntityInfo tree
     * that this DataObjectResolver reponsible for
     */
    protected Collection     strategies;

    /**
     * The LockEngine that the entity of this type belongs to.
     */
    protected LockEngine     lockEngine;

    /**
     * The declared mode for this type
     */
    protected AccessMode     declaredMode;

    /**
     * The map of entityClass and data object class
     * Keyed by entityInfo and valued by java.lang.Class
     */
    protected Map            classMap;

    /**
     * Constructor
     */
    public DataObjectResolver() {
    }

    /* 
     * Load an Object with the specified identity
     *
     * @param tx The TransactionContext in action
     * @param oid The identity of the object. This should has a valid oid.identity.
     * @param timeout The timeout value of the attempt
     * @returns The loaded object
     */
    public Object load( TransactionContext tx, OID oid, AccessMode mode, int timeout )
            throws ObjectNotFoundException, PersistenceException {

        // load the array of fields from LockEngine

        // set time stamp

        // if it is an extend class, all table would have
        // be joined already here. we will try to determined
        // the right class here.
        // It is where we add determinance support later too.
        //     
        //     after we determined, we call tx.addObjectEntry( id, object)
        //     before we iterate htur the avaliable strategies

        // get the right set of accessors, depend on the actual
        // initstated object

        // iterate thru all available strategies

        Entity entity           = new Entity( entityInfo, oid.getIdentity() );

        LockEngine le           = getLockEngine( tx, oid );

        AccessMode effective    = declaredMode.getEffectiveMode( mode );

        lockEngine.load( tx.getKey(), entity, effective, timeout );

        Object newInstance      = getNewInstance( tx, oid, entity );

        // must add to transaction first to to prevent circular references.
        tx.addObjectEntry( oid, newInstance );

        // set the identities into the target object
        /* re-factored into a strategy
        setIdentity( tx, object, oid.getIdentity() );
        */

        // set the timeStamp of the data object to locker's timestamp
        /* re-factored into a strategy
        if ( object instanceof TimeStampable ) {
            ((TimeStampable)object).jdoSetTimeStamp( locker.getTimeStamp() );
        }*/

        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.load( tx, oid, newInstance, entity, mode, timeout );
        }
        return newInstance;
    }

    public void preCreate( TransactionContext tx, OID oid, Object objectToBeCreated )
            throws PersistenceException {

        // must add to transaction first to to prevent circular references.
        tx.addObjectEntry( oid, objectToBeCreated );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.preCreate( tx, oid, objectToBeCreated );
        }

    }

    public void create( TransactionContext tx, OID oid, Object objectToBeCreated )
            throws DuplicateIdentityException, PersistenceException {

        Entity entity = new Entity();

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.create( tx, oid, objectToBeCreated, entity );
        }

        LockEngine le        = getLockEngine( tx, oid );

        le.create( tx.getKey(), entity );

    }

    public void postCreate( TransactionContext tx, OID oid, Object objectToBeCreated )
            throws DuplicateIdentityException, PersistenceException {

        // yip: should I do something to the LockEngine here?
        Entity entity = new Entity();
        LockEngine le        = getLockEngine( tx, oid );

        le.create( tx.getKey(), entity );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.postCreate( tx, oid, objectToBeCreated, entity );
        }
    }

    public void update( TransactionContext tx, OID oid, 
            Object objectToBeUpdated, int timeout )
            throws DuplicateIdentityException, LockNotGrantedException, 
            PersistenceException {

        Entity entity       = new Entity();

        // must add to transaction first to to prevent circular references.
        tx.addObjectEntry( oid, objectToBeUpdated );

        LockEngine le       = getLockEngine( tx, oid );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.update( tx, oid, objectToBeUpdated, entity, timeout );
        }

        le.update( tx.getKey(), entity, timeout );
    }

    public void writeLock( TransactionContext tx, OID oid, Object objectToBeLocked,
            int timeout )
            throws LockNotGrantedException, PersistenceException {

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().writeLock( tx.getKey(), entity, timeout );
    }

    public void softLock( TransactionContext tx, OID oid, Object objectToBeLocked,
            int timeout )
            throws LockNotGrantedException {

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().softLock( tx.getKey(), entity, timeout );
    }

    public void markDelete( TransactionContext tx, OID oid, Object objectToBeDeleted,
            int timeout ) 
            throws ObjectNotFoundException, LockNotGrantedException, 
            PersistenceException {

        if ( !oid.getIdentity().equals( getIdentity( tx, objectToBeDeleted ) ) )
            throw new PersistenceException(
                      Messages.format("persist.changedIdentity",
                      oid.getJavaClass().getName(),oid.getIdentity()) );

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().reload( tx.getKey(), entity );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.markDelete( tx, oid, objectToBeDeleted, entity, timeout );
        }
    }

    public void preStore( TransactionContext tx, OID oid, 
            Object objectToBeTestForModification, int timeout )
            throws LockNotGrantedException, PersistenceException {

        if ( !oid.getIdentity().equals( getIdentity( tx, objectToBeTestForModification ) ) )
            throw new PersistenceException(
                      Messages.format("persist.changedIdentity",
                      oid.getJavaClass().getName(),oid.getIdentity()) );

        // load the locked fields from LockEngine
        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().reload( tx.getKey(), entity );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.preStore( tx, oid, objectToBeTestForModification, entity, timeout );
        }
    }


    public void delete( TransactionContext tx, OID oid, Object objectToBeDeleted )
            throws PersistenceException {

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().delete( tx.getKey(), entity );
    }

    public void store( TransactionContext tx, OID oid, Object objectToBeStored )
            throws DuplicateIdentityException, ObjectModifiedException, 
            ObjectDeletedException, LockNotGrantedException, PersistenceException {

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().reload( tx.getKey(), entity );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.store( tx, oid, objectToBeStored, entity );
        }

        oid.getLockEngine().store( tx.getKey(), entity );
    }

    public void revertObject( TransactionContext tx, OID oid, Object objectToBeReverted ) {

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().reload( tx.getKey(), entity );

        // iterate thru all avaliable strategies
        Iterator itor = strategies.iterator();
        while ( itor.hasNext() ) {
            ResolvingStrategy strategy = (ResolvingStrategy) itor.next();
            strategy.revert( tx, oid, objectToBeReverted, entity );
        }
    }

    public void releaseLock( TransactionContext tx, OID oid, Object objectToBeUnLocked ) {

        Entity entity = new Entity( entityInfo, oid.getIdentity() );
        oid.getLockEngine().releaseLock( tx.getKey(), entity );
    }

    /**
     * Return the proper lock engine for the specified object
     * 
     * @param oid The object identity of the lock engine
     */
    protected LockEngine getLockEngine( TransactionContext tx, OID oid ) {
        // yip: in the future, we would like to add some abstraction
        // on how to get the right lockEngine for the right type.
        // for example, for clustering, we might store customer with
        // id of [0,100000) in one database and everything else in
        // the other. In such case, we uses the id to determines which
        // LockEngine to be used.
        
        // for now, we simply return the only lockEngine
        oid.setLockEngine( lockEngine );
        return lockEngine;
    }

    /**
     * Returns a new instance of object that compatible with the oid
     * and the entity
     * 
     * @param OID the object identity of the object
     * @param Entity the entity loaded from the data store
     */
    protected Object getNewInstance( TransactionContext tx, OID oid, Entity entity ) 
            throws PersistenceException {

        // yip: need to provides a way to support multiple ClassLoader enviorment.
        // (eg, put back 0.9.2 ReflectionService code)
        Class requestedClass = oid.getJavaClass();

        Class determinedClass = (Class) classMap.get( entity.actual );

        if ( !requestedClass.isAssignableFrom( determinedClass ) )
            throw new PersistenceException( 
            Messages.format("persist.objectEntityNotCompatible", entity, requestedClass) );

        try {
            Object result = determinedClass.newInstance();
            oid.setJavaClass( determinedClass );
            return result;
        } catch ( InstantiationException ie ) {
            throw new DataObjectAccessException( 
            Messages.format("dataaccess.instantiationFailed", determinedClass.getName() ) );
        } catch ( IllegalAccessException iae ) {
            throw new DataObjectAccessException(
            Messages.format("dataaccess.constructorNotAccessible", determinedClass.getName()) );
        }
    }

    /**
     * Get the identity from a object of the base type
     * If object isn't persistent and key generator is used, returns null
     *
     * @param tx the transaction context
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getIdentity( TransactionContext tx, Object o ) {
        throw new RuntimeException("Method not implemented");
    }

    
    /**
     * Get the identity from a object of the base type
     *
     * @param tx the transaction context
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getActualIdentity( TransactionContext tx, Object o ) {
        throw new RuntimeException("Method not implemented");
    }


}

/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.persist.resolver;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.spi.Identity;

/**
 * Strategy pattern to allow for common operations related to field resolution based upon 
 * the type of the field.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public interface ResolverStrategy {
    
    /**
     * Create an object of the base class with specified identity into the 
     * persistence storage.
     *
     * @param tx   transaction in action
     * @param object  the object to be created
     * @return  the identity of the object
     */
    Object create(TransactionContext tx, Object object)
    throws PersistenceException;
    

    /**
     * Called after successful creation of an object of the base class.
     *
     * @param tx   transaction in action
     * @param oid  the object identity of the object to be created.
     * @param object  the object to be created
     * @param field The field value as returned by the SQLEngine
     * @param createdId ???
     * @return  the identity of the object
     */
    Object postCreate(TransactionContext tx, OID oid, Object object,
            Object field, Identity createdId) 
    throws PersistenceException;
    
    /**
     * Walk the object model and mark object that should be created.
     * 
     * @param tx transaction in action
     * @param oid the object identity of the object to be created.
     * @param object the object to be created
     * @return true if there's objects that should be created
     */
    boolean markCreate (TransactionContext tx, OID oid, Object object)
    throws PersistenceException;
    
    /**
     * Check the object for modification. If dpendent object is dereferenced, it
     * method will remove the object thru the transaction. If an related object
     * is dereferenced, it method will make sure the formally object will be
     * dereferenced from the other side as well.
     *
     * This method is called in prepare (for commit) state of the transaction.
     * This method indicates if the object needed to be persist or cache should
     * be update using TransactionContext.markDelete.
     *
     * @param tx transaction in action
     * @param oid the object identity of the object
     * @param object the data object to be checked
     * @param timeout  timeout of updating the lock if needed
     * @param field The field value as returned by the SQLEngine
     * @exception PersistenceException If it is not possible to successfully complete this method.
     *
     * @return true if the object is modified
     */
    UpdateFlags preStore(TransactionContext tx, OID oid, Object object,
            int timeout, Object field) throws PersistenceException;
    
    /**
     * Store a data object into the persistent storage of the base class of this
     * ClassMolder.
     *
     * @param tx Transaction in action
     * @param object the object to be stored
     * @param field The field value as returned by the SQLEngine
     */
    Object store(TransactionContext tx, Object object, Object field)
    throws PersistenceException;
    
    /**
     * Update the object which loaded or created in the other transaction to
     * the persistent storage.
     *
     * @param tx Transaction in action
     * @param oid the object identity of the stored object
     * @param object the object to be stored
     * @param suggestedAccessMode Suggested access mode
     * @param field The field value as returned by the SQLEngine
     * @exception PersistenceException If it is not possible to successfully complete this method.
     */
    void update(TransactionContext tx, OID oid, Object object, 
            AccessMode suggestedAccessMode, Object field)
    throws PersistenceException;

    /**
     * Update the object which loaded or created in the other transaction to
     * the persistent storage.
     *
     * @param tx Transaction in action
     * @param oid the object identity of the stored object
     * @param object the object to be stored
     * @param suggestedAccessMode Suggested access mode
     * @return TODO
     * @exception PersistenceException If it is not possible to successfully complete this method.
     */
    boolean updateWhenNoTimestampSet(TransactionContext tx, OID oid, Object object, 
            AccessMode suggestedAccessMode)
    throws PersistenceException;

    /**
     * Update the dirty checking cache. This method is called after a transaction 
     * completed successfully.
     *
     * @param tx - transaction in action
     * @param oid - object's identity of the target object
     * @param object - the target object
     * @return The object tha twas stored in the cache previously.
    */
    Object updateCache(TransactionContext tx, OID oid, Object object);
    
    /**
     * Prepare to delete an object with the specified identity. If any sub-object
     * should be deleted along with the target object, it should be deleted
     * by this method.
     *
     * @param tx - transaction in action
     * @param object - the target object
     * @param field The field value as returned by the SQLEngine
     * @exception PersistenceException If it is not possible to successfully complete this method.
     */
    void markDelete(TransactionContext tx, Object object, Object field)
    throws PersistenceException;
    
    /**
     * Revert the object back to the state of begining of the transaction
     * If the object is loaded, it will be revert as it was loaded. If the
     * object is created, it will be revert as it was just created.
     *
     * @param tx - transaction in action
     * @param oid - the object identity of the target object
     * @param field The field value as returned by the SQLEngine
     * @param object - the target object
     * @exception PersistenceException If it is not possible to successfully complete this method.
     */
    void revertObject(TransactionContext tx, OID oid, Object object, Object field)
    throws PersistenceException;
    
    /**
     * Inspect the fields stored in the object passed as an argument for
     * contained objects.  Request an expireCache for each contained object.
     *
     * @param tx The {@link TransactionContext}
     * @param field The field value as returned by the SQLEngine
     * @exception PersistenceException If it is not possible to successfully complete this method.
     */
    void expireCache(TransactionContext tx, Object field) 
    throws PersistenceException;
    
    /**
     * Load an object with specified identity from the persistent storage.
     *
     * @param tx   the TransactionContext in action
     * @param oid  the object identity of the desired object
     * @param proposedObject Object holder storing information about assumed and actual object
     *        instances.
     * @param suggestedAccessMode the acessMode for the object
     * @exception PersistenceException If it is not possible to successfully complete this method.
     */
    void load(TransactionContext tx, OID oid, ProposedEntity proposedObject, 
            AccessMode suggestedAccessMode)
    throws PersistenceException;


    /**
     * Remove the reference of a related object from an object of
     * the base class. <p>
     *
     * If the related object is PersistanceCapable, the field will
     * be set null. If the related object is a Collection, then
     * the related object will be removed from the Collection. <p>
     *
     * If any changed occured, transactionContext.markModified
     * will be called, to indicate the object is modified. <p>
     *
     * It method will iterate thur all of the object's field and
     * try to remove all the occurrence.
     *
     * @param tx the TransactionContext of the transaction in action
     * @param object the target object of the base type of this ClassMolder
     * @param relatedMolder the ClassMolder of the related object to be
     *                       removed from the object
     * @param relatedObject the object to be removed
     * @return Various flags related to updating/removing object instances.
     */
    UpdateAndRemovedFlags removeRelation (TransactionContext tx, Object object,
            ClassMolder relatedMolder, Object relatedObject);

}    

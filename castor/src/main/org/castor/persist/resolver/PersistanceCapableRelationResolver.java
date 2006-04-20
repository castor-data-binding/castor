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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.ProposedObject;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.castor.persist.proxy.SingleProxy;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.ClassMolderHelper;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.OID;

/**
 * Implementation of {@link org.castor.persist.resolver.ResolverStrategy} for 1:1 relations
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public class PersistanceCapableRelationResolver implements ResolverStrategy {

    /**
     * Class molder of the enclosing class.
     */
    private ClassMolder classMolder;

    /**
     * Field molder for the field to be resolved.
     */
    private FieldMolder fieldMolder;
   
    /**
     * Indicates whether debug mode is active
     */
    //TODO [WG]: Investigate about its use ....
    private boolean debug;
    
    /**
     * Creates an instance of this resolver class.
     * @param classMolder Enclosing class molder.
     * @param fieldMolder Field Molder
     * @param debug True if debug mode is on.
     */
    public PersistanceCapableRelationResolver (ClassMolder classMolder, FieldMolder fieldMolder, boolean debug) {
        this.classMolder = classMolder;
        this.fieldMolder = fieldMolder;
        this.debug = debug;
    }
    
    /**
     * Common Log instance.
     */
    private static final Log _log = LogFactory.getLog (PersistanceCapableRelationResolver.class);
    
    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#create(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public Object create(TransactionContext tx, Object object) 
    {
        Object field = null;
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        Object o = fieldMolder.getValue( object, tx.getClassLoader() );
        if ( o != null ) {
            Object fid = fieldClassMolder.getIdentity( tx, o );
            if ( fid != null ) {
                field = fid;
            }
        }
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public boolean markCreate(TransactionContext tx, OID oid, Object object) 
        throws DuplicateIdentityException, PersistenceException 
    {
        // create dependent object if exists
        boolean updateCache = false;
        
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = fieldMolder.getFieldLockEngine();
        Object o = fieldMolder.getValue( object, tx.getClassLoader() );
        if ( o != null ) {
            if ( fieldMolder.isDependent() ) {
                if ( !tx.isRecorded( o ) ) {
                    tx.markCreate( fieldEngine, fieldClassMolder, o, oid );
                    if ( !fieldMolder.isStored() && fieldClassMolder.isKeyGenUsed() ) {
                        updateCache = true;
                    }
                } else {
                    // fail-fast principle: if the object depend on another object,
                    // throw exception
                    if ( !tx.isDepended( oid, o ) )
                        throw new PersistenceException(
                        "Dependent object may not change its master. Object: "+o+" new master: "+oid);
                }
            } else if ( tx.isAutoStore() ) {
                if ( !tx.isRecorded( o ) ) {
                    tx.markCreate( fieldEngine, fieldClassMolder, o, null );
                    if ( !fieldMolder.isStored() && fieldClassMolder.isKeyGenUsed() ) {
                        updateCache = true;
                    }
                }
            }
        }
        
        return updateCache;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#preStore(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, int, java.lang.Object)
     */
    public UpdateFlags preStore(TransactionContext tx, OID oid, Object object, int timeout, Object field) 
        throws PersistenceException 
    {
        UpdateFlags flags = new UpdateFlags();
        
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = fieldMolder.getFieldLockEngine();
        Object value = fieldMolder.getValue( object, tx.getClassLoader() );
        Object newField = null;
        if ( value != null ) {
        	newField = fieldClassMolder.getIdentity( tx, value );
            flags.setNewField(newField);
        }

        // | yip: don't delete the following comment,
        //      until it proved working by time. :-P
        // if ids are the same or not canCreate
        //    if object is deleted
        //        warn
        //    if object are the same
        //        done
        //    not the same
        //        exception
        // ids not the same or canCreate
        //    if depend
        //       if old is not null
        //          delete old
        //          removeRelation
        //       if canCreate
        //          create new
        //    not depend and autoStore
        //       if old is not null
        //          removeRelation
        //       if canCreate
        //          createObject
        //    not depend nor autoStore
        //       if old is not null
        //          removeRelation
        //       if new is not null
        if ( ClassMolderHelper.isEquals( field, newField ) ) {
        	
//        	 TODO [WG]: can anybody please explain to me the meaning of the next two lines.
        	if ( !debug )
                return flags;

            if ( field == null )
                return flags; // do the next field if both are null

            if ( value != null && tx.isDeleted(value) ) {
                _log.warn ("Deleted object found!");
                if ( fieldMolder.isStored() && fieldMolder.isCheckDirty() )
                    flags.setUpdatePersist(true);
                flags.setUpdateCache(true);
                fieldMolder.setValue( object, null, tx.getClassLoader() );
                return flags;
            }

            if ( tx.isAutoStore() || fieldMolder.isDependent() )
                if ( value != tx.fetch( fieldEngine, fieldClassMolder, field, null ) )
                    throw new DuplicateIdentityException("");
        } else {
            if ( fieldMolder.isStored() && fieldMolder.isCheckDirty() )
                flags.setUpdatePersist(true);
            flags.setUpdateCache(true);

            if ( fieldMolder.isDependent() ) {
                if ( field != null ) {
                    Object reldel = tx.fetch( fieldEngine, fieldClassMolder, field, null );
                    if ( reldel != null )
                        tx.delete( reldel );
                }

                if ( value != null && !tx.isRecorded( value ) ) {
                    tx.markCreate( fieldEngine, fieldClassMolder, value, oid );
                }

            } else if ( tx.isAutoStore() ) {
                if ( field != null ) {
                    Object deref = tx.fetch( fieldEngine, fieldClassMolder, field, null );
                    if ( deref != null )
                        fieldClassMolder.removeRelation( tx, deref, this.classMolder, object );
                }

                if ( value != null && !tx.isRecorded( value ) )
                    tx.markCreate( fieldEngine, fieldClassMolder, value, null );
            } else {
                if ( field != null ) {
                    Object deref = tx.fetch( fieldEngine, fieldClassMolder, field, null );
                    if ( deref != null )
                        fieldClassMolder.removeRelation( tx, deref, this.classMolder, object );
                }

                // yip: user're pretty easily to run into cache
                // integrity problem here, if user forgot to create
                // "value" explicitly. Let's put error message here
                if ( value != null && !tx.isRecorded( value ) )
                    throw new PersistenceException(
                    "Object, "+object+", links to another object, "+value
                    +" that is not loaded/updated/created in this transaction");
            }
        }
        
        return flags;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#store(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public Object store(TransactionContext tx, Object object, Object field) 
    {
        Object newField = null;
        if ( fieldMolder.isStored() ) {
            ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
            Object value = fieldMolder.getValue( object, tx.getClassLoader() );
            if ( value != null )
                newField = fieldClassMolder.getIdentity( tx, value );
        }
        return newField;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#update(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void update(TransactionContext tx, OID oid, Object object, AccessMode suggestedAccessMode, Object field)
    throws PersistenceException, ObjectModifiedException 
    {
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = fieldMolder.getFieldLockEngine();
        Object o = fieldMolder.getValue( object, tx.getClassLoader() );
        if ( fieldMolder.isDependent() ) {
            // depedent class won't have persistenceInfo in LockEngine
            // must look at fieldMolder for it

            if ( o != null && !tx.isRecorded(o) )
                tx.markUpdate( fieldEngine, fieldClassMolder, o, oid );

            // load the cached dependent object from the data store.
            // The loaded will be compared with the new one
            if ( field != null ) {
    			ProposedObject proposedValue = new ProposedObject ();
                tx.load(fieldEngine, fieldClassMolder, field, proposedValue, suggestedAccessMode);
            }
        } else if ( tx.isAutoStore() ) {
            if ( o != null && !tx.isRecorded(o) )
                tx.markUpdate( fieldEngine, fieldClassMolder, o, null );

            if ( field != null ) {
    			ProposedObject proposedValue = new ProposedObject ();
                tx.load(fieldEngine, fieldClassMolder, field, proposedValue, suggestedAccessMode);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#updateCache(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public Object updateCache(TransactionContext tx, OID oid, Object object) {
        Object field = null;
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        Object value = fieldMolder.getValue( object, tx.getClassLoader() );
        if ( value != null ) {
            Object fid = fieldClassMolder.getIdentity( tx, value );
            if ( fid != null ) {
                field = fid;
            }
        } else {
            field = null;
        }
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markDelete(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public void markDelete(TransactionContext tx, Object object, Object field) 
        throws ObjectNotFoundException, PersistenceException 
    {
            // persistanceCapable include many_to_one
            ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
            LockEngine fieldEngine = fieldMolder.getFieldLockEngine();
            if ( fieldMolder.isDependent() ) {
                Object fid = field;
                Object fetched = null;
                if ( fid != null ) {
                    fetched = tx.fetch( fieldEngine, fieldClassMolder, fid, null );
                    if ( fetched != null )
                        tx.delete( fetched );
                }

                Object fobject = fieldMolder.getValue( object, tx.getClassLoader() );
                if ( fobject != null && tx.isPersistent( fobject ) ) {
                    tx.delete( fobject );
                }
            } else {
                // delete the object from the other side of the relation
                Object fid = field;
                Object fetched = null;
                if ( fid != null ) {
                    fetched = tx.fetch( fieldEngine, fieldClassMolder, field, null );
                    if ( fetched != null )
                        fieldClassMolder.removeRelation( tx, fetched, this.classMolder, object );
                }
            }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#revertObject(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object)
     */
    public void revertObject(TransactionContext tx, OID oid, Object object, Object field) 
        throws PersistenceException 
    {
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = fieldMolder.getFieldLockEngine();
        
        Object value;

        if ( field != null ) {
            value = tx.fetch( fieldEngine, fieldClassMolder, field, null );
            fieldMolder.setValue( object, value, tx.getClassLoader() );
        } else {
            fieldMolder.setValue( object, null, tx.getClassLoader() );
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#expireCache(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public void expireCache(TransactionContext tx, Object field)
        throws PersistenceException 
    {
        // field is not primitive type. Related object will be expired
    	 
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = fieldMolder.getFieldLockEngine();

        if ( field != null ) {
            // use the corresponding Persistent fields as the identity
            tx.expireCache(fieldEngine, fieldClassMolder, field );
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#load(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, org.castor.persist.ProposedObject, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void load(TransactionContext tx, OID oid, ProposedObject proposedObject, AccessMode suggestedAccessMode, Object field)
            throws ObjectNotFoundException, PersistenceException {
        // field is not primitive type. Related object will be loaded
        // thru the transaction in action if needed.

        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = fieldMolder.getFieldLockEngine();

        if ( field != null ) {
            // use the corresponding Persistent fields as the identity,
            // and we ask transactionContext in action to load it.
        	Object temp;
            try {
				// should I use lazy loading for this object?
				if (fieldMolder.isLazy()) {
					temp = SingleProxy.getProxy(tx, fieldEngine, fieldClassMolder, field, null, suggestedAccessMode);
				} else {
					ProposedObject proposedTemp = new ProposedObject();
					temp = tx.load(fieldEngine, fieldClassMolder, field, proposedTemp, suggestedAccessMode);
				}
            } catch (ObjectNotFoundException ex) {
                temp = null;
            }
            fieldMolder.setValue(proposedObject.getObject(), temp, tx.getClassLoader());
        } else {
            fieldMolder.setValue(proposedObject.getObject(), null, tx.getClassLoader());
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#postCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object postCreate(TransactionContext tx, OID oid, Object object,
            Object field, Object createdId) {
        return field;
    }

    
    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#removeRelation(org.castor.persist.TransactionContext, java.lang.Object, org.exolab.castor.persist.ClassMolder, java.lang.Object)
     */
    public UpdateAndRemovedFlags removeRelation( TransactionContext tx, Object object,
            ClassMolder relatedMolder, Object relatedObject )  {
        // de-reference the object
        UpdateAndRemovedFlags flags = new UpdateAndRemovedFlags();
        ClassMolder fieldClassMolder = fieldMolder.getFieldClassMolder();
        
        ClassMolder relatedBaseMolder = relatedMolder;
        while ( fieldClassMolder != relatedBaseMolder && relatedBaseMolder != null ) {
            relatedBaseMolder = relatedBaseMolder.getExtends();
        }
        if ( fieldClassMolder == relatedBaseMolder ) {
            Object related = fieldMolder.getValue( object, tx.getClassLoader() );
            if ( related == relatedObject ) {
                fieldMolder.setValue( object, null, tx.getClassLoader() );
                flags.setUpdateCache(true);
                flags.setUpdatePersist(true);
                flags.setRemoved(true);
            }
            // |
            
        }
        return flags;
    }
}

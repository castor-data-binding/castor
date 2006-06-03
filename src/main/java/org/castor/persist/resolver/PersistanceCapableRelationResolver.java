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
import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.castor.persist.proxy.LazyCGLIB;
import org.castor.persist.proxy.SingleProxy;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.ClassMolderHelper;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.OID;

/**
 * Implementation of {@link org.castor.persist.resolver.ResolverStrategy} for 1:1 relations
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public final class PersistanceCapableRelationResolver implements ResolverStrategy {

    /**
     * Class molder of the enclosing class.
     */
    private ClassMolder _classMolder;

    /**
     * Field molder for the field to be resolved.
     */
    private FieldMolder _fieldMolder;
    
    private int _fieldIndex;
   
    /**
     * Indicates whether debug mode is active
     */
    //TODO [WG]: Investigate about its use ....
    private boolean _debug;
    
    /**
     * Creates an instance of this resolver class.
     * @param classMolder Enclosing class molder.
     * @param fieldMolder Field Molder
     * @param fieldIndex Field index within all fields of parent class molder.
     * @param debug True if debug mode is on.
     */
    public PersistanceCapableRelationResolver (final ClassMolder classMolder,
            final FieldMolder fieldMolder, 
            final int fieldIndex,
            final boolean debug) {
        _classMolder = classMolder;
        _fieldMolder = fieldMolder;
        _fieldIndex = fieldIndex;
        _debug = debug;
    }
    
    /**
     * Common Log instance.
     */
    private static final Log LOG = LogFactory.getLog (PersistanceCapableRelationResolver.class);
    
    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#create(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public Object create(final TransactionContext tx, final Object object) {
        Object field = null;
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            Object fid = fieldClassMolder.getIdentity(tx, o);
            if (fid != null) {
                field = fid;
            }
        }
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public boolean markCreate(final TransactionContext tx, final OID oid, final Object object)
    throws PersistenceException {
        // create dependent object if exists
        boolean updateCache = false;
        
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            if (_fieldMolder.isDependent()) {
                if (!tx.isRecorded(o)) {
                    tx.markCreate(fieldClassMolder, o, oid);
                    if (!_fieldMolder.isStored() && fieldClassMolder.isKeyGenUsed()) {
                        updateCache = true;
                    }
                } else {
                    // fail-fast principle: if the object depend on another object,
                    // throw exception
                    if (!tx.isDepended(oid, o)) {
                        throw new PersistenceException(
                                "Dependent object may not change its master. Object: " + o + " new master: " + oid);
                    }
                }
            } else if (tx.isAutoStore()) {
                if (!tx.isRecorded(o)) {
                    tx.markCreate(fieldClassMolder, o, null);
                    if (!_fieldMolder.isStored() && fieldClassMolder.isKeyGenUsed()) {
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
    public UpdateFlags preStore(final TransactionContext tx, final OID oid,
            final Object object, final int timeout, final Object field)
    throws PersistenceException {
        UpdateFlags flags = new UpdateFlags();
        
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object value = _fieldMolder.getValue(object, tx.getClassLoader());
        Object newField = null;
        if (value != null) {
            newField = fieldClassMolder.getIdentity(tx, value);
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
        if (ClassMolderHelper.isEquals(field, newField)) {

            /*
             * Let's deal with a situation where there's no dependent object (field == null),
             * a 'new' dependent object has been set (value != null), but as we are using a key
             * generator on this newly set object, calling fieldClassMolder.getIdentity() will return
             * null (and hence newField == null). In this case, we still have to mark this new object for 
             * creation and instruct Castor to update the cache(s) as well
             */
            if (field == null && value != null &&  _fieldMolder.isDependent() && !tx.isRecorded(value)) {
                if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);
                tx.markCreate(fieldClassMolder, value, oid);
            }
            
            //TODO [WG]: can anybody please explain to me the meaning of the next two lines.
            if (!_debug) { return flags; }
            if (field == null) { return flags; } // do the next field if both are null

            if ((value != null) && tx.isDeleted(value)) {
                LOG.warn ("Deleted object found!");
                if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);
                _fieldMolder.setValue(object, null, tx.getClassLoader());
                return flags;
            }

            if (tx.isAutoStore() || _fieldMolder.isDependent()) {
                if (value != tx.fetch(fieldClassMolder, field, null)) {
                    throw new DuplicateIdentityException("");
                }
            }
        } else {
            if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                flags.setUpdatePersist(true);
            }
            flags.setUpdateCache(true);

            if (_fieldMolder.isDependent()) {
                if (field != null) {
                    Object reldel = tx.fetch(fieldClassMolder, field, null);
                    if (reldel != null) {
                        tx.delete(reldel);
                    }
                }

                if ((value != null) && !tx.isRecorded(value)) {
                    tx.markCreate(fieldClassMolder, value, oid);
                }

            } else if (tx.isAutoStore()) {
                if (field != null) {
                    Object deref = tx.fetch(fieldClassMolder, field, null);
                    if (deref != null) {
                        fieldClassMolder.removeRelation(tx, deref, this._classMolder, object);
                    }
                }

                if ((value != null) && !tx.isRecorded(value)) {
                    tx.markCreate(fieldClassMolder, value, null);
                }
            } else {
                if (field != null) {
                    Object deref = tx.fetch(fieldClassMolder, field, null);
                    if (deref != null) {
                        fieldClassMolder.removeRelation(tx, deref, this._classMolder, object);
                    }
                }

                // yip: user're pretty easily to run into cache
                // integrity problem here, if user forgot to create
                // "value" explicitly. Let's put error message here
                if ((value != null) && !tx.isRecorded(value) && !_fieldMolder.isLazy()) {
                    throw new PersistenceException(
                            "Object, " + object + ", links to another object, " + value
                            + " that is not loaded/updated/created in this transaction");
                }
            }
        }
        
        return flags;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#store(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public Object store(final TransactionContext tx, final Object object, final Object field) {
        Object newField = null;
        if (_fieldMolder.isStored()) {
            ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
            Object value = _fieldMolder.getValue(object, tx.getClassLoader());
            if (value != null) {
                newField = fieldClassMolder.getIdentity(tx, value);
            }
        }
        return newField;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#update(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void update(final TransactionContext tx, final OID oid, final Object object, final AccessMode suggestedAccessMode, final Object field)
    throws PersistenceException {
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (_fieldMolder.isDependent()) {
            // depedent class won't have persistenceInfo in LockEngine
            // must look at fieldMolder for it

            if ((o != null) && !tx.isRecorded(o)) {
                tx.markUpdate(fieldClassMolder, o, oid);
            }

            // load the cached dependent object from the data store.
            // The loaded will be compared with the new one
            if (field != null) {
                ProposedEntity proposedValue = new ProposedEntity(fieldClassMolder);
                tx.load(field, proposedValue, suggestedAccessMode);
            }
        } else if (tx.isAutoStore()) {
            if ((o != null) && !tx.isRecorded(o)) {
                tx.markUpdate(fieldClassMolder, o, null);
            }

            if (field != null) {
                ProposedEntity proposedValue = new ProposedEntity(fieldClassMolder);
                tx.load(field, proposedValue, suggestedAccessMode);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#updateCache(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public Object updateCache(final TransactionContext tx, final OID oid, final Object object) {
        Object field = null;
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object value = _fieldMolder.getValue(object, tx.getClassLoader());
        if (value != null) {
            Object fid = fieldClassMolder.getIdentity(tx, value);
            if (_fieldMolder.isLazy() && (value instanceof LazyCGLIB)) {
                boolean hasMaterialized = ((LazyCGLIB) value).interceptedHasMaterialized().booleanValue();
                if (!hasMaterialized) {
                    fid = fieldClassMolder.getActualIdentity(tx, value);
                }
            }
            if (fid != null) {
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
    public void markDelete(final TransactionContext tx, final Object object, final Object field)
    throws PersistenceException {
            // persistanceCapable include many_to_one
            ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
            if (_fieldMolder.isDependent()) {
                Object fid = field;
                Object fetched = null;
                if (fid != null) {
                    fetched = tx.fetch(fieldClassMolder, fid, null);
                    if (fetched != null) {
                        tx.delete(fetched);
                    }
                }

                Object fobject = _fieldMolder.getValue(object, tx.getClassLoader());
                if ((fobject != null) && tx.isPersistent(fobject)) {
                    tx.delete(fobject);
                }
            } else {
                // delete the object from the other side of the relation
                Object fid = field;
                Object fetched = null;
                if (fid != null) {
                    fetched = tx.fetch(fieldClassMolder, field, null);
                    if (fetched != null) {
                        fieldClassMolder.removeRelation(tx, fetched, this._classMolder, object);
                    }
                }
            }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#revertObject(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object)
     */
    public void revertObject(final TransactionContext tx, final OID oid, final Object object, final Object field)
    throws PersistenceException {
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        
        Object value;

        if (field != null) {
            value = tx.fetch(fieldClassMolder, field, null);
            _fieldMolder.setValue(object, value, tx.getClassLoader());
        } else {
            _fieldMolder.setValue(object, null, tx.getClassLoader());
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#expireCache(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public void expireCache(final TransactionContext tx, final Object field)
    throws PersistenceException {
        // field is not primitive type. Related object will be expired

        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();

        if (field != null) {
            // use the corresponding Persistent fields as the identity
            tx.expireCache(fieldClassMolder, field);
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#load(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, org.castor.persist.ProposedObject, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void load(final TransactionContext tx, final OID oid, final ProposedEntity proposedObject, final AccessMode suggestedAccessMode)
    throws PersistenceException {
        // field is not primitive type. Related object will be loaded
        // thru the transaction in action if needed.

        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();

        Object fieldValue = proposedObject.getField(_fieldIndex);
        if (fieldValue != null) {
            // use the corresponding Persistent fields as the identity,
            // and we ask transactionContext in action to load it.
            Object temp;
            try {
                // should I use lazy loading for this object?
                if (_fieldMolder.isLazy()) {
                    temp = SingleProxy.getProxy(tx, fieldClassMolder, fieldValue, null, suggestedAccessMode);
                } else {
                    ProposedEntity proposedTemp = new ProposedEntity(fieldClassMolder);
                    temp = tx.load(fieldValue, proposedTemp, suggestedAccessMode);
                }
            } catch (ObjectNotFoundException ex) {
                temp = null;
            }
            _fieldMolder.setValue(proposedObject.getEntity(), temp, tx.getClassLoader());
        } else {
            _fieldMolder.setValue(proposedObject.getEntity(), null, tx.getClassLoader());
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#postCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object postCreate(final TransactionContext tx, final OID oid, final Object object,
            final Object field, final Object createdId) {
        return field;
    }

    
    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#removeRelation(org.castor.persist.TransactionContext, java.lang.Object, org.exolab.castor.persist.ClassMolder, java.lang.Object)
     */
    public UpdateAndRemovedFlags removeRelation(final TransactionContext tx, final Object object,
            final ClassMolder relatedMolder, final Object relatedObject)  {
        // de-reference the object
        UpdateAndRemovedFlags flags = new UpdateAndRemovedFlags();
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        
        ClassMolder relatedBaseMolder = relatedMolder;
        while ((fieldClassMolder != relatedBaseMolder) && (relatedBaseMolder != null)) {
            relatedBaseMolder = relatedBaseMolder.getExtends();
        }
        if (fieldClassMolder == relatedBaseMolder) {
            Object related = _fieldMolder.getValue(object, tx.getClassLoader());
            if (related == relatedObject) {
                _fieldMolder.setValue(object, null, tx.getClassLoader());
                flags.setUpdateCache(true);
                flags.setUpdatePersist(true);
                flags.setRemoved(true);
            }
        }
        return flags;
    }
    
    /**
     * @inheritDoc
     */
    public boolean updateWhenNoTimestampSet(
            final TransactionContext tx, 
            final OID oid, 
            final Object object, 
            final AccessMode suggestedAccessMode) 
    throws PersistenceException {
        boolean updateCache = false;
        // create dependent object if exists
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            if (_fieldMolder.isDependent()) {
                // creation of dependent object should be delayed to the
                // preStore state.
                // otherwise, in the case of keygenerator being used in both
                // master and dependent object, and if an dependent
                // object is replaced by another before commit, the
                // orginial dependent object will not be removed.
                //
                // the only disadvantage for that appoarch is that an
                // OQL Query will not able to include the newly generated
                // dependent object.
                if (!tx.isRecorded(o)) {
                    tx.markCreate(fieldClassMolder, o, oid);
                    if (!_fieldMolder.isStored() && fieldClassMolder._isKeyGenUsed) {
                        updateCache = true;
                    }
                }
                // fail-fast principle: if the object depend on another object,
                // throw exception
                // if ( !tx.isDepended( oid, o ) )
                    //    throw new PersistenceException("Dependent object may not change its master. Object: "+o+" new master: "+oid);
            } else if (tx.isAutoStore()) {
                if (!tx.isRecorded(o)) {
                    // related object should be created right the way, if autoStore
                    // is enabled, to obtain a database lock on the row. If both side
                    // uses keygenerator, the current object will be updated in the
                    // store state.
                    boolean creating = tx.markUpdate(fieldClassMolder, o, null);
                    // if _fhs[i].isStore is true for this field,
                    // and if key generator is used
                    // and if the related object is replaced this object by null
                    // and if everything else is not modified
                    // then, objectModifiedException will be thrown
                    // there are two solutions, first introduce preCreate state,
                    // and walk the create graph, and create non-store object
                    // first. However, it doesn't guarantee solution. because
                    // every object may have field which uses key-generator
                    // second, we can do another SQLStatement at the very end of
                    // this method.
                    // note, one-many and many-many doesn't affected, because
                    // it is always non-store fields.
                    if (creating && !_fieldMolder.isStored() && fieldClassMolder._isKeyGenUsed) {
                        updateCache = true;
                    }
                }
            }
        }
        return updateCache;
    }
    
}

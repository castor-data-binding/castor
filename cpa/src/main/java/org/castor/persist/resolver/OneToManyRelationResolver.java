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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateFlags;
import org.castor.persist.proxy.LazyCollection;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.ClassMolderHelper;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.spi.Identity;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public final class OneToManyRelationResolver extends ManyRelationResolver {
    
    /**
     * Creates an instance of OneToMany.
     * 
     * @param classMolder
     * @param fieldMolder
     * @param fieldIndex Field index within all fields of parent class molder.
     */
    public OneToManyRelationResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder, final int fieldIndex) {
        super(classMolder, fieldMolder, fieldIndex);
    }
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #markCreate(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
    public boolean markCreate(final TransactionContext tx, final OID oid,
            final Object object)
    throws PersistenceException {
        boolean updateCache = false;
        // create dependent objects if exists
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            Iterator itor = ClassMolderHelper.getIterator(o);
            while (itor.hasNext()) {
                Object oo = itor.next();
                if (_fieldMolder.isDependent()) {
                    if (!tx.isRecorded(oo)) {
                        // autoCreated = true;
                        tx.markCreate(fieldClassMolder, oo, oid);
                        if (fieldClassMolder.isKeyGenUsed()) {
                            updateCache = true;
                        }
                    } else
                    // fail-fast principle: if the object depend on another
                    // object,
                    // throw exception
                    if (!tx.isDepended(oid, oo)) {
                        throw new PersistenceException(
                                "Dependent object may not change its master");
                    }
                } else if (tx.isAutoStore()) {
                    if (!tx.isRecorded(oo)) {
                        tx.markCreate(fieldClassMolder, oo, null);
                        if (fieldClassMolder.isKeyGenUsed()) {
                            updateCache = true;
                        }
                    }
                }
            }
        }
        return updateCache;
    }
    
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #markDelete(org.castor.persist.TransactionContext, java.lang.Object,
     *      java.lang.Object)
     */
    public void markDelete(final TransactionContext tx, final Object object,
            final Object field) 
    throws PersistenceException {
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        // markDelete mix with prestore
        // so, store is not yet called, and only the loaded (or created)
        // relation have to be deleted.
        // not really. cus, the other created relation, may already
        // has reference to this object. so, how to deal with that?
        if (_fieldMolder.isDependent()) {
            ArrayList alist = (ArrayList) field;
            if (field != null) {
                for (int j = 0; j < alist.size(); j++) {
                    Identity fid = (Identity) alist.get(j);
                    Object fetched = null;
                    if (fid != null) {
                        fetched = tx.fetch(fieldClassMolder, fid, null);
                        if (fetched != null) {
                            tx.delete(fetched);
                        }
                    }
                }

                Iterator itor = ClassMolderHelper.getIterator(_fieldMolder
                        .getValue(object, tx.getClassLoader()));
                while (itor.hasNext()) {
                    Object fobject = itor.next();
                    if (fobject != null && tx.isPersistent(fobject)) {
                        tx.delete(fobject);
                    }
                }
            }
        } else {
            if (field != null) {
                ArrayList alist = (ArrayList) field;
                for (int j = 0; j < alist.size(); j++) {
                    Identity fid = (Identity) alist.get(j);
                    Object fetched = null;
                    if (fid != null) {
                        fetched = tx.fetch(fieldClassMolder, fid, null);
                        if (fetched != null) {
                            fieldClassMolder.removeRelation(tx, fetched,
                                    fieldClassMolder, object);
                        }
                    }
                }

                Iterator itor = ClassMolderHelper.getIterator(_fieldMolder
                        .getValue(object, tx.getClassLoader()));
                while (itor.hasNext()) {
                    Object fobject = itor.next();
                    if (fobject != null && tx.isPersistent(fobject)) {
                        fieldClassMolder.removeRelation(tx, fobject,
                                fieldClassMolder, object);
                    }
                }
            }
        }
    }
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #preStore(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object, int,
     *      java.lang.Object)
     */
    public UpdateFlags preStore(final TransactionContext tx, final OID oid,
            final Object object, final int timeout, final Object field)
    throws PersistenceException {
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object value = _fieldMolder.getValue(object, tx.getClassLoader());
        
        Iterator<Identity> removedItor;
        Iterator<Object> addedItor;        
        if (!(value instanceof LazyCollection)) {
            List<Identity> orgFields = (List<Identity>) field;

            Collection<Identity> removed = ClassMolderHelper.getRemovedIdsList(tx,
                    orgFields, value, fieldClassMolder);
            removedItor = removed.iterator();

            Collection<Object> added = ClassMolderHelper.getAddedEntitiesList(tx,
                    orgFields, value, fieldClassMolder);
            addedItor = added.iterator();
        } else {
        	LazyCollection lazy = (LazyCollection) value;

            // RelationCollection has to clean up its state at the end of the transaction
            tx.addTxSynchronizable(lazy);

            removedItor = lazy.getRemovedIdsList().iterator();
            addedItor = lazy.getAddedEntitiesList().iterator();
        }

        UpdateFlags flags = new UpdateFlags();

        if (removedItor.hasNext()) {
            if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                flags.setUpdatePersist(true);
            }
            flags.setUpdateCache(true);

            if (_fieldMolder.isDependent()) {
                while (removedItor.hasNext()) {
                    Identity removedId = (Identity) removedItor.next();
                    Object removedEntity = tx.fetch(fieldClassMolder, removedId, null);
                    if (removedEntity != null) {
                        tx.delete(removedEntity);
                    }
                }
            } else {
                while (removedItor.hasNext()) {
                    Identity removedId = (Identity) removedItor.next();
                    Object removedEntity = tx.fetch(fieldClassMolder, removedId, null);
                    if (removedEntity != null) {
                        fieldClassMolder.removeRelation(tx, removedEntity, _classMolder, object);
                    }
                }
            }
        }

        if (addedItor.hasNext()) {
            if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                flags.setUpdatePersist(true);
            }
            flags.setUpdateCache(true);

            if (_fieldMolder.isDependent()) {
                while (addedItor.hasNext()) {
                    Object addedEntity = addedItor.next();
                    if (addedEntity != null) {
                        tx.markCreate(fieldClassMolder, addedEntity, oid);
                    }
                }
            } else if (tx.isAutoStore()) {
                while (addedItor.hasNext()) {
                    Object addedEntity = addedItor.next();
                    if (addedEntity != null) {
                        if (!tx.isRecorded(addedEntity)) {
                            tx.markCreate(fieldClassMolder, addedEntity, null);
                        }
                    }
                }
            }
        }

        return flags;
    }
    
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #update(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object,
     *      org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void update(final TransactionContext tx, final OID oid,
            final Object object, final AccessMode suggestedAccessMode,
            final Object field)
    throws PersistenceException {
        ArrayList v = (ArrayList) field;
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        ArrayList newSetOfIds = new ArrayList();
        Iterator itor = ClassMolderHelper.getIterator(_fieldMolder
                .getValue(object, tx.getClassLoader()));

        // iterate the collection of this data object field
        while (itor.hasNext()) {
            Object element = itor.next();
            Object actualIdentity = fieldClassMolder.getActualIdentity(tx, element);
            newSetOfIds.add(actualIdentity);
            if (!tx.isRecorded(element)) {
                if (_fieldMolder.isDependent() && !_fieldMolder.isLazy()) {
                    if (v != null && v.contains(actualIdentity)) {
                        tx.markUpdate(fieldClassMolder, element, oid);
                    }
                } else if (tx.isAutoStore()) {
                    tx.markUpdate(fieldClassMolder, element, null);
                }
            }
        }

        // load all old objects for comparison in the preStore state
        if (v != null) {
            for (int j = 0; j < v.size(); j++) {
                if (!newSetOfIds.contains(v.get(j))) {
                    // load all the dependent object in cache for
                    // modification
                    // check at commit time.
                    ProposedEntity proposedValue = new ProposedEntity(fieldClassMolder);
                    tx.load((Identity) v.get(j), proposedValue, suggestedAccessMode);
                }
            }
        }
    }
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #postCreate(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, org.exolab.castor.persist.DepositBox,
     *      java.lang.Object, org.exolab.castor.mapping.AccessMode,
     *      java.lang.Object)
     */
    public Object postCreate(final TransactionContext tx, final OID oid,
            final Object object, final Object field, 
            final Identity createdId) {
        return field;
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
        // create dependent objects if exists
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            Iterator itor = ClassMolderHelper.getIterator(o);
            while (itor.hasNext()) {
                Object oo = itor.next();
                if (_fieldMolder.isDependent()) {
                    if (!tx.isRecorded(oo)) {
                        // autoCreated = true;
                        tx.markCreate(fieldClassMolder, oo, oid);
                        if (fieldClassMolder._isKeyGenUsed) {
                            updateCache = true;
                        }
                    } else
                    // fail-fast principle: if the object depend on another
                    // object,
                    // throw exception
                    if (!tx.isDepended(oid, oo)) {
                        throw new PersistenceException(
                                "Dependent object may not change its master");
                    }
                } else if (tx.isAutoStore()) {
                    if (!tx.isRecorded(oo)) {
                        boolean creating = tx.markUpdate(fieldClassMolder, oo, null);
                        if (creating && fieldClassMolder._isKeyGenUsed) {
                            updateCache = true;
                        }
                    }
                }
            }
        }
        return updateCache;
    }
    

}

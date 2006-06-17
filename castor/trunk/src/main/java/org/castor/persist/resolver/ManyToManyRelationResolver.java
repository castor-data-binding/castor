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

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateFlags;
import org.castor.persist.proxy.RelationCollection;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.ClassMolderHelper;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.Lazy;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.spi.Identity;

/**
 * Implementation of {@link org.castor.persist.resolver.ResolverStrategy} for M:N relations.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public final class ManyToManyRelationResolver extends ManyRelationResolver {
    
    /**
     * Creates an instance of ManyToManyRelationResolver
     * @param classMolder Associated ClassMolder.
     * @param fieldMolder Associated FieldMolder.
     * @param fieldIndex Field index within all fields of parent class molder.
     * @param debug ???
     */
    public ManyToManyRelationResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder, 
            final int fieldIndex, 
            final boolean debug) {
        super(classMolder, fieldMolder, fieldIndex, debug);
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
        // create relation if the relation table
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            Iterator itor = ClassMolderHelper.getIterator(o);
            // many-to-many relation is never dependent relation
            while (itor.hasNext()) {
                Object oo = itor.next();
                if (tx.isAutoStore() && !tx.isRecorded(oo)) {
                    tx.markCreate(fieldClassMolder, oo, null);
                    updateCache = true;
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
        // delete the relation in relation table too
        /*
         * _fhs[i].getRelationLoader().deleteRelation(
         * tx.getConnection(oid.getLockEngine()), oid.getIdentity() );
         */

        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        // markDelete mix with prestore
        // so, store is not yet called, and only the loaded (or created)
        // relation have to be deleted.
        // not really. cus, the other created relation, may already
        // has reference to this object. so, how to deal with that?
        if (field != null) {
            ArrayList alist = (ArrayList) field;
            for (int j = 0; j < alist.size(); j++) {
                Identity fid = (Identity) alist.get(j);
                Object fetched = null;
                if (fid != null) {
                    fetched = tx.fetch(fieldClassMolder, fid, null);
                    if (fetched != null) {
                        fieldClassMolder.removeRelation(tx, fetched,
                                _classMolder, object);
                    }
                }
            }
        }

        Iterator itor = ClassMolderHelper.getIterator(_fieldMolder.getValue(
                object, tx.getClassLoader()));
        while (itor.hasNext()) {
            Object fobject = itor.next();
            if (fobject != null && tx.isPersistent(fobject)) {
                fieldClassMolder.removeRelation(tx, fobject, _classMolder,
                        object);
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
        UpdateFlags flags = new UpdateFlags();
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object value = _fieldMolder.getValue(object, tx.getClassLoader());
        ArrayList orgFields = (ArrayList) field;
        if (!(value instanceof Lazy)) {
            Collection removed = ClassMolderHelper.getRemovedIdsList(tx,
                    orgFields, value, fieldClassMolder);
            Iterator removedItor = removed.iterator();
            if (removedItor.hasNext()) {
                if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);
            }
            while (removedItor.hasNext()) {
                // must be loaded thur transaction, so that the related object
                // is properly locked and updated before we delete it.
                Identity identity = (Identity) removedItor.next();
                if (!tx.isDeletedByOID(new OID(fieldClassMolder, identity))) {
                    ProposedEntity proposedValue = new ProposedEntity(fieldClassMolder);
                    Object reldel = tx.load(identity, proposedValue, null);
                    if (reldel != null && tx.isPersistent(reldel)) {
                        tx.writeLock(reldel, tx.getLockTimeout());

                        _fieldMolder.getRelationLoader().deleteRelation(
                                tx.getConnection(oid.getMolder().getLockEngine()),
                                oid.getIdentity(), identity);

                        fieldClassMolder.removeRelation(tx, reldel, _classMolder, object);
                    }
                }
            }

            Collection added = ClassMolderHelper.getAddedValuesList(tx,
                    orgFields, value, fieldClassMolder);
            Iterator addedItor = added.iterator();
            if (addedItor.hasNext()) {
                if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);
            }
            while (addedItor.hasNext()) {
                Object addedField = addedItor.next();
                tx.markModified(addedField, false/* updatePersist */,
                        true/* updateCache */);

                if (tx.isPersistent(addedField)) {
                    _fieldMolder.getRelationLoader().createRelation(
                            tx.getConnection(oid.getMolder().getLockEngine()),
                            oid.getIdentity(),
                            fieldClassMolder.getIdentity(tx, addedField));
                } else {
                    if (tx.isAutoStore()) {
                        if (!tx.isDeleted(addedField)) {
                            tx.markCreate(fieldClassMolder, addedField, null);
                        }
                    }
                }
            }

        } else {
            RelationCollection lazy = (RelationCollection) value;

            // this RelationCollection has to clean up its state at the end of
            // the
            // transaction
            tx.addTxSynchronizable(lazy);

            ArrayList deleted = lazy.getDeleted();
            if (!deleted.isEmpty()) {
                if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);

                Iterator itor = deleted.iterator();
                while (itor.hasNext()) {
                    flags.setUpdateCache(true);
                    Identity deletedId = (Identity) itor.next();
                    Object toBeDeleted = lazy.find(deletedId);
                    if (toBeDeleted != null) {
                        if (tx.isPersistent(toBeDeleted)) {
                            tx.writeLock(toBeDeleted, 0);

                            _fieldMolder.getRelationLoader().deleteRelation(
                                    tx.getConnection(oid.getMolder().getLockEngine()),
                                    oid.getIdentity(), deletedId);

                            fieldClassMolder.removeRelation(tx, toBeDeleted,
                                    this._classMolder, object);
                        }
//                    } else {
//                        // what to do if it happens?
                    }
                }
            }

            ArrayList added = lazy.getAdded();
            if (!added.isEmpty()) {
                if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);

                Iterator itor = added.iterator();
                while (itor.hasNext()) {
                    Identity addedId = (Identity) itor.next();
                    Object toBeAdded = lazy.find(addedId);
                    if (toBeAdded != null) {
                        if (tx.isPersistent(toBeAdded)) {
                            _fieldMolder.getRelationLoader().createRelation(
                                    tx.getConnection(oid.getMolder().getLockEngine()),
                                    oid.getIdentity(), addedId);
                        } else {
                            if (tx.isAutoStore()) {
                                if (!tx.isRecorded(toBeAdded)) {
                                    tx.markCreate(fieldClassMolder, toBeAdded, null);
                                }
                            }
                        }
//                    } else {
//                        // what to do if it happens?
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
        if (tx.isAutoStore()) {
            ArrayList newSetOfIds = new ArrayList();

            // iterate the collection of this data object field
            Iterator itor = ClassMolderHelper.getIterator(_fieldMolder
                    .getValue(object, tx.getClassLoader()));
            while (itor.hasNext()) {
                Object element = itor.next();
                Object actualIdentity = fieldClassMolder.getActualIdentity(tx, element);
                newSetOfIds.add(actualIdentity);
                if (!tx.isRecorded(element)) {
                    tx.markUpdate(fieldClassMolder, element, null);
                }
            }
            // load all old objects for comparison in the preStore state
            if (v != null) {
                for (int j = 0, l = v.size(); j < l; j++) {
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
    }
    
    /**
     * @see org.exolab.castor.persist.resolver.RelationResolver
     *      #postCreate(org.exolab.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, org.exolab.castor.persist.DepositBox,
     *      java.lang.Object, org.exolab.castor.mapping.AccessMode,
     *      java.lang.Object)
     */
    public Object postCreate(final TransactionContext tx, final OID oid,
            final Object object, final Object field, final Identity createdId) 
        throws PersistenceException {
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        Object result = field;
        if (o != null) {
            ArrayList fids = ClassMolderHelper.extractIdentityList(tx, fieldClassMolder, o);
            result = fids;
            Iterator itor = ClassMolderHelper.getIterator(o);
            while (itor.hasNext()) {
                Object oo = itor.next();
                if (tx.isPersistent(oo)) {
                    _fieldMolder.getRelationLoader().createRelation(
                            tx.getConnection(oid.getMolder().getLockEngine()), createdId,
                            fieldClassMolder.getIdentity(tx, oo));
                }
            }
        }
        return result;
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
        // create relation if the relation table
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            Iterator itor = ClassMolderHelper.getIterator(o);
            // many-to-many relation is never dependent relation
            while (itor.hasNext()) {
                Object oo = itor.next();
                if (tx.isAutoStore() && !tx.isRecorded(oo)) {
                    boolean creating = tx.markUpdate(fieldClassMolder, oo, null);
                    if (creating) {
                        updateCache = true;
                    }
                }
            }
        }
        return updateCache;
    }

}

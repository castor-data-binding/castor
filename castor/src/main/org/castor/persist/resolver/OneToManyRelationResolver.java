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

import org.castor.persist.ProposedObject;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateFlags;
import org.castor.persist.proxy.RelationCollection;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.ClassMolderHelper;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.Lazy;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.OID;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public final class OneToManyRelationResolver extends ManyRelationResolver {
    
    /**
     * Creates an instance of OneToMany
     * @param classMolder
     * @param fieldMolder
     * @param debug
     */
    public OneToManyRelationResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder, final boolean debug) {
        super(classMolder, fieldMolder, debug);
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
        LockEngine fieldEngine = _fieldMolder.getFieldLockEngine();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            Iterator itor = ClassMolderHelper.getIterator(o);
            while (itor.hasNext()) {
                Object oo = itor.next();
                if (_fieldMolder.isDependent()) {
                    if (!tx.isRecorded(oo)) {
                        // autoCreated = true;
                        tx.markCreate(fieldEngine, fieldClassMolder, oo, oid);
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
                        tx.markCreate(fieldEngine, fieldClassMolder, oo, null);
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
        LockEngine fieldEngine = _fieldMolder.getFieldLockEngine();
        // markDelete mix with prestore
        // so, store is not yet called, and only the loaded (or created)
        // relation have to be deleted.
        // not really. cus, the other created relation, may already
        // has reference to this object. so, how to deal with that?
        if (_fieldMolder.isDependent()) {
            ArrayList alist = (ArrayList) field;
            if (field != null) {
                for (int j = 0; j < alist.size(); j++) {
                    Object fid = alist.get(j);
                    Object fetched = null;
                    if (fid != null) {
                        fetched = tx.fetch(fieldEngine, fieldClassMolder, fid,
                                null);
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
                    Object fid = alist.get(j);
                    Object fetched = null;
                    if (fid != null) {
                        fetched = tx.fetch(fieldEngine, fieldClassMolder, fid,
                                null);
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
        UpdateFlags flags = new UpdateFlags();
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = _fieldMolder.getFieldLockEngine();
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
                Object removedId = removedItor.next();
                Object reldel = tx.fetch(fieldEngine, fieldClassMolder,
                        removedId, null);
                if (reldel != null) {
                    if (_fieldMolder.isDependent()) {
                        tx.delete(reldel);
                    } else {
                        fieldClassMolder.removeRelation(tx, reldel,
                                this._classMolder, object);
                    }
//                } else {
//                    // should i notify user that the object does not exist?
//                    // user can't delete dependent object himself. So, must
//                    // error.
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
                Object addedValue = addedItor.next();
                if (_fieldMolder.isDependent()) {
                    if (!tx.isRecorded(addedValue)) {
                        tx.markCreate(fieldEngine, fieldClassMolder,
                                addedValue, oid);
//                    } else {
//                        // should i notify user that the object does not exist?
//                        // user can't create dependent object himself. So, must
//                        // be
//                        // an error.
                    }
                } else if (tx.isAutoStore()) {
                    if (!tx.isRecorded(addedValue)) {
                        tx.markCreate(fieldEngine, fieldClassMolder,
                                addedValue, null);
                    }
                }
            }

            // it would be good if we also compare the new field element with
            // the element in the transaction, when debug is set true
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

                // if ( fieldMolder.isDependent() ) {
                Iterator itor = deleted.iterator();
                while (itor.hasNext()) {
                    flags.setUpdateCache(true);
                    Object toBeDeleted = lazy.find(itor.next());
                    if (toBeDeleted != null && tx.isPersistent(toBeDeleted)) {
                        if (_fieldMolder.isDependent()) {
                            tx.delete(toBeDeleted);
                        } else {
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

                if (_fieldMolder.isDependent()) {
                    Iterator itor = added.iterator();
                    while (itor.hasNext()) {
                        Object toBeAdded = lazy.find(itor.next());
                        if (toBeAdded != null) {
                            tx.markCreate(fieldEngine, fieldClassMolder,
                                    toBeAdded, oid);
//                        } else {
//                            // what to do if it happens?
                        }
                    }
                } else if (tx.isAutoStore()) {
                    Iterator itor = added.iterator();
                    while (itor.hasNext()) {
                        Object toBeAdded = lazy.find(itor.next());
                        if (toBeAdded != null) {
                            if (!tx.isRecorded(toBeAdded)) {
                                tx.markCreate(fieldEngine, fieldClassMolder,
                                        toBeAdded, null);
                            }
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
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        LockEngine fieldEngine = _fieldMolder.getFieldLockEngine();
        if (_fieldMolder.isDependent()) {
            if (!_fieldMolder.isLazy()) {
                Iterator itor = ClassMolderHelper.getIterator(_fieldMolder
                        .getValue(object, tx.getClassLoader()));
                ArrayList v = (ArrayList) field;
                ArrayList newSetOfIds = new ArrayList();

                // iterate the collection of this data object field
                while (itor.hasNext()) {
                    Object element = itor.next();
                    Object actualIdentity = fieldClassMolder.getActualIdentity(
                            tx, element);
                    newSetOfIds.add(actualIdentity);
                    if (v != null && v.contains(actualIdentity)) {
                        if (!tx.isRecorded(element)) {
                            tx.markUpdate(fieldEngine, fieldClassMolder,
                                    element, oid);
                        }
//                    } else {
//                        // if ( !tx.isRecorded( element ) ) tx.markCreate(
//                        // fieldEngine, fieldClassMolder, element, oid );
                    }
                }
                if (v != null) {
                    for (int j = 0, l = v.size(); j < l; j++) {
                        if (!newSetOfIds.contains(v.get(j))) {
                            // load all the dependent object in cache for
                            // modification
                            // check at commit time.
                            ProposedObject proposedValue = new ProposedObject();
                            tx
                                    .load(oid.getLockEngine(),
                                            fieldClassMolder, v.get(j),
                                            proposedValue, suggestedAccessMode);
                        }
                    }
                }
            } else {
                // ArrayList avlist = (ArrayList) fields[i];
                fieldClassMolder = _fieldMolder.getFieldClassMolder();
                fieldEngine = _fieldMolder.getFieldLockEngine();
                // RelationCollection relcol = new RelationCollection( tx, oid,
                // fieldEngine, fieldClassMolder, accessMode, avlist );
            }
        } else if (tx.isAutoStore()) {
            Iterator itor = ClassMolderHelper.getIterator(_fieldMolder
                    .getValue(object, tx.getClassLoader()));
            ArrayList v = (ArrayList) field;
            ArrayList newSetOfIds = new ArrayList();

            // iterate the collection of this data object field
            while (itor.hasNext()) {
                Object element = itor.next();
                Object actualIdentity = fieldClassMolder.getActualIdentity(tx,
                        element);
                newSetOfIds.add(actualIdentity);
                if (v != null && v.contains(actualIdentity)) {
                    if (!tx.isRecorded(element)) {
                        tx.markUpdate(fieldEngine, fieldClassMolder, element,
                                null);
                    }
                } else {
                    if (!tx.isRecorded(element)) {
                        tx.markUpdate(fieldEngine, fieldClassMolder, element,
                                null);
                    }
                }
            }
            // load all old objects for comparison in the preStore state
            if (v != null) {
                for (int j = 0, l = v.size(); j < l; j++) {
                    if (!newSetOfIds.contains(v.get(j))) {
                        // load all the dependent object in cache for
                        // modification
                        // check at commit time.
                        ProposedObject proposedValue = new ProposedObject();
                        tx.load(oid.getLockEngine(), fieldClassMolder,
                                v.get(j), proposedValue, suggestedAccessMode);
                    }
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
            final Object createdId) {
        return field;
    }

}

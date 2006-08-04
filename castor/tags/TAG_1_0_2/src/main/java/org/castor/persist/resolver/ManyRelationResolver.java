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
import java.util.Iterator;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.castor.persist.proxy.CollectionProxy;
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
 * Implementation of {@link org.castor.persist.resolver.ResolverStrategy} for many relations. This class carries
 * behaviour common to 1:M and M:N relations.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public abstract class ManyRelationResolver implements ResolverStrategy {

    /**
     * Associated {@link ClassMolder}.
     */
    protected ClassMolder _classMolder;
    
    /**
     * Associated {@link FieldMolder}.
     */
    protected FieldMolder _fieldMolder;
    
    private int _fieldIndex;
    
    /** 
     * Creates an instance of ManyRelationResolver
     * @param classMolder Associated {@link ClassMolder}
     * @param fieldMolder Associated {@link FieldMolder}
     * @param fieldIndex Field index within all fields of parent class molder.
     * @param debug ???
     */
    public ManyRelationResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder, 
            final int fieldIndex,
            final boolean debug) {
        _classMolder = classMolder;
        _fieldMolder = fieldMolder;
        _fieldIndex = fieldIndex;
    }
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #create(org.castor.persist.TransactionContext,
     *      java.lang.Object)
     */
    public final Object create(final TransactionContext tx, final Object object) {
        Object field = null;
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object o = _fieldMolder.getValue(object, tx.getClassLoader());
        if (o != null) {
            ArrayList fids = 
                ClassMolderHelper.extractIdentityList(tx, fieldClassMolder, o);
            field = fids;
        }
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public abstract boolean markCreate(final TransactionContext tx, final OID oid,
            final Object object) 
    throws PersistenceException;

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#preStore(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, int, java.lang.Object)
     */
    public abstract UpdateFlags preStore(final TransactionContext tx,
            final OID oid, final Object object, final int timeout,
            final Object field) 
    throws PersistenceException;

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#store(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public final Object store(final TransactionContext tx, final Object object,
            final Object field) {
        // nothing to do ....
        return null;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#update(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public abstract void update(final TransactionContext tx, final OID oid,
            final Object object, final AccessMode suggestedAccessMode,
            final Object field) throws PersistenceException;

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#updateCache(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public final Object updateCache(final TransactionContext tx, final OID oid,
            final Object object) {
        Object field = null;
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        Object value = _fieldMolder.getValue(object, tx.getClassLoader());
        if (value != null) {
            ArrayList fids;
            if (!(value instanceof Lazy)) {
                fids = ClassMolderHelper.extractIdentityList(tx,
                        fieldClassMolder, value);
            } else {
                RelationCollection lazy = (RelationCollection) value;
                fids = (ArrayList) lazy.getIdentitiesList().clone();
            }
            field = fids;
        }
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markDelete(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public abstract void markDelete(final TransactionContext tx,
            final Object object, final Object field)
    throws PersistenceException;

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#revertObject(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object)
     */
    public final void revertObject(final TransactionContext tx, final OID oid,
            final Object object, final Object field)
    throws PersistenceException {
        Object o = field;
        if (o == null) {
            _fieldMolder.setValue(object, null, tx.getClassLoader());
        } else if (!_fieldMolder.isLazy()) { // <-- fix for bug #1046
            ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
            Class collectionType = _fieldMolder.getCollectionType();

            ArrayList v = (ArrayList) field;
            if (v != null) {
                if (collectionType.isArray()) {
                    Object[] arrayValue = (Object[]) java.lang.reflect.Array
                            .newInstance(collectionType.getComponentType(), v
                                    .size());
                    for (int j = 0, l = v.size(); j < l; j++) {
                        arrayValue[j] = tx.fetch(fieldClassMolder, (Identity) v.get(j), null);
                    }
                    _fieldMolder.setValue(object, arrayValue, tx
                            .getClassLoader());
                } else {
                    CollectionProxy cp = CollectionProxy.create(_fieldMolder,
                            object, tx.getClassLoader());
                    // clear collection
                    _fieldMolder.setValue(object, cp.getCollection(), tx
                            .getClassLoader());

                    for (int j = 0, l = v.size(); j < l; j++) {
                        Object obj = tx.fetch(fieldClassMolder, (Identity) v.get(j), null);
                        if (obj != null) {
                            cp.add((Identity) v.get(j), obj);
                        }
                    }
                    cp.close();
                    // fieldMolder.setValue( object, cp.getCollection() );
                }
            } else {
                _fieldMolder.setValue(object, null, tx.getClassLoader());
            }
        } else {
            ArrayList list = (ArrayList) field;
            ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();

            RelationCollection relcol = new RelationCollection(tx, oid,
                    fieldClassMolder, null, list);
            _fieldMolder.setValue(object, relcol, tx.getClassLoader());
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#expireCache(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public final void expireCache(final TransactionContext tx, final Object field)
    throws PersistenceException {
        // field is one-to-many and many-to-many type. All the related
        // objects will be expired

        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();

        ArrayList v = (ArrayList) field;
        if (v != null) {
            for (int j = 0, l = v.size(); j < l; j++) {
                tx.expireCache(fieldClassMolder, (Identity) v.get(j));
            }
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#load(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, org.castor.persist.ProposedObject, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public final void load(final TransactionContext tx, final OID oid,
            final ProposedEntity proposedObject,
            final AccessMode suggestedAccessMode)
    throws PersistenceException {
        // field is one-to-many and many-to-many type. All the related
        // object will be loaded and put in a Collection. And, the
        // collection will be set as the field.
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();

        if (!_fieldMolder.isLazy()) {
            // lazy loading is not specified, load all objects into
            // the collection and set the Collection as the data object
            // field.
            ArrayList v = (ArrayList) proposedObject.getField(_fieldIndex);
            if (v != null) {
                // simple array type support
                Class collectionType = _fieldMolder.getCollectionType();
                if (collectionType.isArray()) {
                    Object[] value = (Object[]) java.lang.reflect.Array
                            .newInstance(collectionType.getComponentType(), v.size());
                    for (int j = 0, l = v.size(); j < l; j++) {
                        ProposedEntity proposedValue = new ProposedEntity(fieldClassMolder);
                        value[j] = tx.load((Identity) v.get(j), proposedValue, suggestedAccessMode);
                    }
                    _fieldMolder.setValue(proposedObject.getEntity(), value, tx
                            .getClassLoader());
                } else {
                    CollectionProxy cp = CollectionProxy.create(_fieldMolder,
                            proposedObject.getEntity(), tx.getClassLoader());
                    for (int j = 0, l = v.size(); j < l; j++) {
                        ProposedEntity proposedValue = new ProposedEntity(fieldClassMolder);
                        cp.add((Identity) v.get(j), tx.load((Identity) v.get(j), proposedValue, suggestedAccessMode));
                    }
                    cp.close();
                }
            } else {
                _fieldMolder.setValue(proposedObject.getEntity(), null, tx
                        .getClassLoader());
            }
        } else {
            // lazy loading is specified. Related object will not be loaded.
            // A lazy collection with all the identity of the related object
            // will constructed and set as the data object's field.
            ArrayList list = (ArrayList) proposedObject.getField(_fieldIndex);
            RelationCollection relcol = new RelationCollection(tx, oid,
                    fieldClassMolder, suggestedAccessMode, list);
            _fieldMolder.setValue(proposedObject.getEntity(), relcol, tx
                    .getClassLoader());
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#postCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public abstract Object postCreate(final TransactionContext tx,
            final OID oid, final Object object, final Object field,
            final Identity createdId)
    throws PersistenceException;

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#removeRelation(org.castor.persist.TransactionContext, java.lang.Object, org.exolab.castor.persist.ClassMolder, java.lang.Object)
     */
    public final UpdateAndRemovedFlags removeRelation(final TransactionContext tx,
            final Object object, final ClassMolder relatedMolder,
            final Object relatedObject) {
        UpdateAndRemovedFlags flags = new UpdateAndRemovedFlags();
        // remove the object from the collection
        ClassMolder fieldClassMolder = _fieldMolder.getFieldClassMolder();
        ClassMolder relatedBaseMolder = relatedMolder;
        while (fieldClassMolder != relatedBaseMolder
                && relatedBaseMolder != null) {
            relatedBaseMolder = relatedBaseMolder.getExtends();
        }
        if (fieldClassMolder == relatedBaseMolder) {
            boolean changed = false;
            Object related = _fieldMolder.getValue(object, tx.getClassLoader());

            if (related instanceof RelationCollection) {
                RelationCollection lazy = (RelationCollection) related;
                changed = lazy.remove(relatedObject);
            } else {
                Iterator itor = ClassMolderHelper.getIterator(related);
                while (itor.hasNext()) {
                    Object o = itor.next();
                    if (o == relatedObject) {
                        changed = true;
                        itor.remove();
                    }
                }
            }
            if (changed) {
                flags.setUpdateCache(true);
                flags.setUpdatePersist(false);
                flags.setRemoved(true);
            }
        }
        return flags;
    }
    
}

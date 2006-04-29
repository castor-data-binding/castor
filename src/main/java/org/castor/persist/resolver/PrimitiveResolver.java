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
import org.exolab.castor.persist.ClassMolderHelper;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.OID;

/**
 * Implementation of {@link org.castor.persist.resolver.ResolverStrategy} for primitive fields.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class PrimitiveResolver implements ResolverStrategy {

    private FieldMolder _fieldMolder;
    private int _fieldIndex;

    /**
     * Creates an instance of PrimitiveResolver
     * 
     * @param classMolder Associated {@link ClassMolder}
     * @param fieldMolder Associated {@link FieldMolder}
     * @param fieldIndex Field index within all fields of parent class molder.
     * @param debug ???
     */
    public PrimitiveResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder, 
            final int fieldIndex,
            final boolean debug) {
        this._fieldMolder = fieldMolder;
        this._fieldIndex = fieldIndex;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#create(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public Object create(final TransactionContext tx, final Object object) {
        return _fieldMolder.getValue(object, tx.getClassLoader());
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public boolean markCreate(final TransactionContext tx, final OID oid,
            final Object object) {
        boolean updateCache = false;
        return updateCache;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#preStore(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, int, java.lang.Object)
     */
    public UpdateFlags preStore(final TransactionContext tx, final OID oid,
            final Object object, final int timeout, final Object field) {
        UpdateFlags flags = new UpdateFlags();
        Object value = _fieldMolder.getValue(object, tx.getClassLoader());
        if (!ClassMolderHelper.isEquals(field, value)) {
            if (_fieldMolder.isReadonly()) {
                _fieldMolder.setValue(object, field, tx.getClassLoader());
            } else {
                if (_fieldMolder.isStored() /* && _fhs[i].isCheckDirty() */) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);
            }
        }
        return flags;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#store(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public Object store(final TransactionContext tx, final Object object,
            final Object field) {
        Object newField = null;
        newField = _fieldMolder.getValue(object, tx.getClassLoader());
        return newField;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#update(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void update(final TransactionContext tx, final OID oid,
            final Object object, final AccessMode suggestedAccessMode,
            final Object field) {
        // nothing to do
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#updateCache(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object)
     */
    public Object updateCache(final TransactionContext tx, final OID oid,
            final Object object) {
        // should give some attemp to reduce new object array
        Object field = _fieldMolder.getValue(object, tx.getClassLoader());
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#markDelete(org.castor.persist.TransactionContext, java.lang.Object, java.lang.Object)
     */
    public void markDelete(final TransactionContext tx, final Object object,
            final Object field) {
        // nothing to do
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#revertObject(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object)
     */
    public void revertObject(final TransactionContext tx, final OID oid,
            final Object object, final Object field) {
        _fieldMolder.setValue(object, field, tx.getClassLoader());
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy
     * #expireCache(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public void expireCache(final TransactionContext tx, final Object field) {
        // nothing to do
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy
     * #load(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, org.castor.persist.ProposedObject, org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void load(final TransactionContext tx, final OID oid,
            final ProposedEntity proposedObject,
            final AccessMode suggestedAccessMode) {
        // simply set the corresponding Persistent field value into the object
        Object fieldValue = proposedObject.getField(_fieldIndex);
        if (fieldValue != null) {
            _fieldMolder.setValue(proposedObject.getEntity(), fieldValue, tx
                    .getClassLoader());
        } else {
            _fieldMolder.setValue(proposedObject.getEntity(), null, tx
                    .getClassLoader());
        }
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#postCreate(org.castor.persist.TransactionContext, org.exolab.castor.persist.OID, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object postCreate(final TransactionContext tx, final OID oid,
            final Object object, final Object field, final Object createdId) {
        return field;
    }

    /* (non-Javadoc)
     * @see org.castor.persist.resolver.ResolverStrategy#removeRelation(org.castor.persist.TransactionContext, java.lang.Object, org.exolab.castor.persist.ClassMolder, java.lang.Object)
     */
    public UpdateAndRemovedFlags removeRelation(final TransactionContext tx,
            final Object object, final ClassMolder relatedMolder,
            final Object relatedObject) {
        return new UpdateAndRemovedFlags();
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
        // nothing need to be done here for primitive
        return false;
    }
}

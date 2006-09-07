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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.spi.Identity;

/**
 * Implementation of {@link org.castor.persist.resolver.ResolverStrategy} for
 * serializable fields.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 */
public final class SerializableResolver implements ResolverStrategy {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SerializableResolver.class);

    /**
     * Associated {@link FieldMolder}.
     */
    private FieldMolder _fieldMolder;
    private int _fieldIndex;
    
    /** 
     * Creates an instance of SerializableResolver.
     * @param classMolder Associated {@link ClassMolder}
     * @param fieldMolder Associated {@link FieldMolder}
     * @param fieldIndex Field index within all fields of parent class molder.
     * @param debug ???
     */
    public SerializableResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder, 
            final int fieldIndex,
            final boolean debug) {
        this._fieldMolder = fieldMolder;
        this._fieldIndex = fieldIndex;
    }
    
    /**
     * @throws PersistenceException
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #create(org.castor.persist.TransactionContext,
     *      java.lang.Object)
     */
    public Object create(final TransactionContext tx, final Object object)
            throws PersistenceException {
        Object field = null;
        try {
            Object dependent =
                _fieldMolder.getValue(object, tx.getClassLoader());
            if (dependent != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(dependent);
                field = bos.toByteArray();
            }
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error during serializing dependent object", e);
        }
        return field;
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #markCreate(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
    public boolean markCreate(final TransactionContext tx, final OID oid,
            final Object object) {
        boolean updateCache = false;
        return updateCache;
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

        // deserialize byte[] into java object
        try {
            byte[] bytes = (byte[]) field;
            Object fieldValue = _fieldMolder.getValue(object, tx.getClassLoader());
            if (fieldValue != null && bytes != null) {
                // The following code can be updated, after Blob-->InputStream
                // to enhance performance.
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream os = new ObjectInputStream(bis);
                Object dependent = os.readObject();
                if (!dependent.equals(fieldValue)) {
                    if (_fieldMolder.isStored() && _fieldMolder.isCheckDirty()) {
                        flags.setUpdatePersist(true);
                    }
                    flags.setUpdateCache(true);
                }
            } else if (fieldValue != null || bytes != null) {
                // indicate store is needed
                if (_fieldMolder.isStored() /* && fieldMolder.isCheckDirty() */) {
                    flags.setUpdatePersist(true);
                }
                flags.setUpdateCache(true);
            }
        } catch (OptionalDataException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        }
        return flags;
    }

    /**
     * @throws PersistenceException
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #store(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
    public Object store(final TransactionContext tx, final Object object,
            final Object field)
    throws PersistenceException {
        Object newField = null;
        try {
            Object dependent = 
                _fieldMolder.getValue(object, tx.getClassLoader());
            if (dependent != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(dependent);
                newField = bos.toByteArray();
            }
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error during serializing dependent object", e);
        }
        return newField;
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #update(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object, 
     *      org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public void update(final TransactionContext tx, final OID oid,
            final Object object, final AccessMode suggestedAccessMode,
            final Object field) {
        // nothing to do
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #updateCache(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
    public Object updateCache(final TransactionContext tx, final OID oid,
            final Object object) {
        Object field = null;
        try {
            Object o = _fieldMolder.getValue(object, tx.getClassLoader());
            if (o != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(o);
                field = bos.toByteArray();
            }
        } catch (IOException e) {
            // TODO [WG]: investigate ????????????????????????????
            // It won't happen. ByteArrayOutputStream will not throw IOException
            String msg = "ByteArrayOutputStream throw IOException: " + e.getMessage();
            LOG.error(msg, e);
        }
        return field;
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #markDelete(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object)
     */
    public void markDelete(final TransactionContext tx, final Object object,
            final Object field) {
        // nothing to do
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #revertObject(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, org.exolab.castor.persist.DepositBox,
     *      java.lang.Object)
     */
    public void revertObject(final TransactionContext tx, final OID oid,
            final Object object, final Object field)
            throws PersistenceException {
        // deserialize byte[] into java object
        try {
            byte[] bytes = (byte[]) field;
            if (bytes != null) {
                // The following code can be updated, after Blob-->InputStream
                // to enhance performance.
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream os = new ObjectInputStream(bis);
                Object o = os.readObject();
                _fieldMolder.setValue(object, o, tx.getClassLoader());
            } else {
                _fieldMolder.setValue(object, null, tx.getClassLoader());
            }
        } catch (OptionalDataException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        }
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy#
     *      expireCache(org.castor.persist.TransactionContext, java.lang.Object)
     */
    public void expireCache(final TransactionContext tx, final Object field) {
        // nothing to do
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #load(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, ProposedEntity, org.exolab.castor.mapping.AccessMode)
     */
    public void load(final TransactionContext tx, final OID oid,
            final ProposedEntity proposedObject,
            final AccessMode suggestedAccessMode)
    throws PersistenceException {
        // deserialize byte[] into java object
        try {
            byte[] bytes = (byte[]) proposedObject.getField(_fieldIndex);
            if (bytes != null) {
                // The following code can be updated, after Blob-->InputStream
                // to enhance performance.
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream os = new ObjectInputStream(bis);
                Object object = os.readObject();
                _fieldMolder.setValue(proposedObject.getEntity(), object, tx
                        .getClassLoader());
            } else {
                _fieldMolder.setValue(proposedObject.getEntity(), null, tx
                        .getClassLoader());
            }
        } catch (OptionalDataException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error while deserializing an dependent object", e);
        }
    }

    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #postCreate(org.castor.persist.TransactionContext,
     *      org.exolab.castor.persist.OID, java.lang.Object, 
     *      org.exolab.castor.mapping.AccessMode, java.lang.Object)
     */
    public Object postCreate(final TransactionContext tx, final OID oid,
            final Object object, final Object field, final Identity createdId) {
        return field;
    }
    
    /**
     * @see org.castor.persist.resolver.ResolverStrategy
     *      #removeRelation(org.castor.persist.TransactionContext,
     *      java.lang.Object, org.exolab.castor.persist.ClassMolder,
     *      java.lang.Object)
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

package org.castor.persist.resolver;

import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.FieldMolder;
import org.exolab.castor.persist.OID;
import org.exolab.castor.persist.FieldMolder.CascadingType;
import org.exolab.castor.persist.spi.Identity;

/**
 * Abstract base of a {@link org.castor.persist.resolver.ResolverStrategy} implementation
 * for any kind of relation.
 * 
 * @author Michael Schr?der
 */
public abstract class BaseRelationResolver implements ResolverStrategy {

    /** Associated {@link ClassMolder}. */
    protected ClassMolder _classMolder;
    
    /** Associated {@link FieldMolder}. */
    protected FieldMolder _fieldMolder;
    
    /** 
     * Creates an instance of BasicRelationResolver.
     * 
     * @param classMolder Associated {@link ClassMolder}
     * @param fieldMolder Associated {@link FieldMolder}
     */
    public BaseRelationResolver(final ClassMolder classMolder,
            final FieldMolder fieldMolder) {
        _classMolder = classMolder;
        _fieldMolder = fieldMolder;
    }
    
    public boolean isCascadingCreate(TransactionContext tx) {
	return tx.isAutoStore() || _fieldMolder.getCascading().contains(CascadingType.CREATE);
    }
    
    // TODO: what's the role of autostore with these ones?

    public boolean isCascadingDelete() {
	return _fieldMolder.getCascading().contains(CascadingType.DELETE);
    }

    public boolean isCascadingUpdate(TransactionContext tx) {
	return tx.isAutoStore() || _fieldMolder.getCascading().contains(CascadingType.UPDATE);
    }

    
    public abstract Object create(TransactionContext tx, Object object) throws PersistenceException;

    public abstract void expireCache(TransactionContext tx, Object field) throws PersistenceException;

    public abstract void load(TransactionContext tx, OID oid, ProposedEntity proposedObject, 
	    AccessMode suggestedAccessMode) throws PersistenceException;

    public abstract boolean markCreate(TransactionContext tx, OID oid, Object object) throws PersistenceException;

    public abstract void markDelete(TransactionContext tx, Object object, Object field) throws PersistenceException;

    public abstract Object postCreate(TransactionContext tx, OID oid, Object object, Object field, Identity createdId)
	    throws PersistenceException;

    public abstract UpdateFlags preStore(TransactionContext tx, OID oid, Object object, int timeout, Object field)
	    throws PersistenceException;

    public abstract UpdateAndRemovedFlags removeRelation(TransactionContext tx, Object object, ClassMolder relatedMolder,
	    Object relatedObject);

    public abstract void revertObject(TransactionContext tx, OID oid, Object object, Object field) throws PersistenceException;

    public abstract Object store(TransactionContext tx, Object object, Object field) throws PersistenceException;

    public abstract void update(TransactionContext tx, OID oid, Object object, AccessMode suggestedAccessMode, Object field)
	    throws PersistenceException;

    public abstract Object updateCache(TransactionContext tx, OID oid, Object object);

    public abstract boolean updateWhenNoTimestampSet(TransactionContext tx, OID oid, Object object,
	    AccessMode suggestedAccessMode) throws PersistenceException;

}

/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.persist;

import java.sql.Connection;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.util.ClassLoadingUtils;
import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.castor.persist.resolver.ResolverFactory;
import org.castor.persist.resolver.ResolverStrategy;
import org.castor.util.Messages;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.jdo.engine.JDOCallback;
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.util.JDOClassDescriptorResolverImpl;

/**
 * ClassMolder is a 'binder' for one type of data object and its corresponding 
 * {@link Persistence}. For example, when ClassMolder is asked to load
 * an object, it acquires the field values from {@link Persistence} and binds 
 * them into the target object. 
 * <p>
 * It resolves relations via {@link TransactionContext} and subsequently binds 
 * these related objects into the target object, too.
 * <p>
 * Apart from loading, ClassMolder is also responsible for storing, removing,
 * creating an object to and from a persistence storage, as well as
 * reverting an object to its previous state.
 * <p>
 * Each instance of ClassMolder deals with exactly one persistable type,
 * interacts with one instance of Persistent and belongs to one
 * {@link LockEngine}.
 * <p>
 *
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:wernert DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 */

public class ClassMolder {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static Log _log = LogFactory.getFactory().getInstance(ClassMolder.class);
    
    /** The fully qualified name of the java data object class which this ClassMolder
     *  corresponds to. We call it base class. */
    private String _name;

    /** Associated identity <tt>FieldMolder</tt>s. */
    private FieldMolder[] _ids;

    /** Associated field <tt>FieldMolder</tt>s. */
    private FieldMolder[] _fhs;

    /** <tt>ClassMolder</tt> of the java data object class's ClassMolder which
     *  the base class extends; null if this class does not extend any other 
     *  classes. */
    private ClassMolder _extends;

    /** <tt>ClassMolder</tt> of the java data object class's ClassMolder which
     *  the base class depends on. <tt>null</tt> if it class is an indenpendent
     *  class. */
    private ClassMolder _depends;

    /** A Vector of <tt>ClassMolder</tt>s for all the direct dependent class of the
     *  base class. */
    private Vector _dependent;

    /** A Vector of <tt>ClassMolder</tt>s for all the direct extending class of the
     *  base class. */
    private Vector _extendent;

    /** Default accessMode of the base class. */
    private AccessMode _accessMode;

    /** Associated {@link Persistence} instance. */
    private Persistence _persistence;

    /** Associated {@link LockEngine} instance. */
    private LockEngine _engine;

    /** The CallbackInterceptor for the base class. */
    private CallbackInterceptor _callback;

    /** The parameters to be used for caching freed instance of the base class. */
    private Properties _cacheParams;

    /** Is a key kenerator used for the base class? */
    public boolean _isKeyGenUsed;

    /** True if org.exolab.castor.debug="true". */
    private boolean _debug;

    /** True if the representing class implements the interface TimeStampable. */
    private boolean _timeStampable;

    /** Create priority. */
    private int _priority = -1;

    /** True if all {@link ResolverStrategy} have been reset. */
    private boolean _resolversHaveBeenReset = false;
        
    /** All field resolver instances. */
    private ResolverStrategy[] _resolvers;
    
    /** ClassDescriptor for the class this molder is responsible for. */
    private final ClassDescriptor _clsDesc;

    /**
     * Creates an instance of this class.
     * 
     * @param ds is the helper class for resolving depends and extends relationship
     *        among all the ClassMolder in the same LockEngine.
     * @param classDescrResolver {@link ClassDescriptorResolver} instance
     * @param classDescrResolver class descriptor resolver.
     * @param lock the lock engine.
     * @param clsDesc the classDescriptor for the base class.
     * @param persist the Persistence for the base class.
     * @throws ClassNotFoundException If a class cannot be loaded.
     * @throws MappingException if an error occurred with analyzing the mapping information.
     */
    ClassMolder(final DatingService ds, final ClassDescriptorResolver classDescrResolver, 
            final LockEngine lock,
            final ClassDescriptor clsDesc, final Persistence persist)
            throws ClassNotFoundException, MappingException {

        _debug = Boolean.getBoolean("org.exolab.castor.debug");
        _engine = lock;
        _persistence = persist;
        _clsDesc = clsDesc;
        
        ClassMapping clsMap = ((ClassDescriptorImpl) clsDesc).getMapping();
        _name = clsMap.getName();
        _accessMode = AccessMode.valueOf(clsMap.getAccess().toString());

        _timeStampable = TimeStampable.class.isAssignableFrom(clsDesc.getJavaClass());

        ds.register(_name, this);

        ClassMapping dependClassMapping = (ClassMapping) clsMap.getDepends();
        ClassMapping extendsClassMapping = (ClassMapping) clsMap.getExtends();

        //if ( dep != null && ext != null )
        //    throw new MappingException("A JDO cannot both extends and depends on other objects");

        if (dependClassMapping != null) {
            ds.pairDepends(this, dependClassMapping.getName());
        }

        if (extendsClassMapping != null) {
            ds.pairExtends(this, extendsClassMapping.getName());
        }

        if (clsDesc instanceof JDOClassDescriptor) {
            _cacheParams = ((JDOClassDescriptor) clsDesc).getCacheParams();
            _isKeyGenUsed = (((JDOClassDescriptor) clsDesc)
                    .getKeyGeneratorDescriptor() != null);
        }

        // construct <tt>FieldMolder</tt>s for each of the identity fields of
        // the base class.
        FieldMapping[] fmId = ClassMolderHelper.getIdFields(clsMap);
        _ids = new FieldMolder[fmId.length];
        for (int i = 0; i < _ids.length; i++) {
            _ids[i] = new FieldMolder(ds, this, fmId[i]);
        }

        // construct <tt>FieldModlers</tt>s for each of the non-transient fields 
        // of the base class 
        FieldMapping[] fmFields = ClassMolderHelper.getFullFields(clsMap);
        
        int numberOfNonTransientFieldMolders = 0;
        for (int i = 0; i < fmFields.length; i++) {
            if (!isFieldTransient(fmFields[i])) {
                numberOfNonTransientFieldMolders += 1;
            }
        }
        _fhs = new FieldMolder[numberOfNonTransientFieldMolders];
        _resolvers = new ResolverStrategy[numberOfNonTransientFieldMolders];
        
        int fieldMolderNumber = 0;
        for (int i = 0; i < fmFields.length; i++) {
            
            // don't create field molder for transient fields
            if (isFieldTransient(fmFields[i])) {
                continue;
            }
            
            if ((fmFields[i].getSql() != null) && (fmFields[i].getSql().getManyTable() != null)) {
                // the fields is not primitive
                String[] relatedIdSQL = null;
                int[] relatedIdType = null;
                TypeConvertor[] relatedIdConvertTo = null;
                TypeConvertor[] relatedIdConvertFrom = null;
                
                String manyTable = fmFields[i].getSql().getManyTable();

                String[] idSQL = new String[fmId.length];
                int[] idType = new int[fmId.length];
                TypeConvertor[] idConvertFrom = new TypeConvertor[fmId.length];
                TypeConvertor[] idConvertTo = new TypeConvertor[fmId.length];
                FieldDescriptor[] fd = ((ClassDescriptorImpl) clsDesc).getIdentities();
                for (int j = 0; j < fmId.length; j++) {
                    idSQL[j] = fmId[j].getSql().getName()[0];

                    if (fd[j] instanceof JDOFieldDescriptor) {
                        int[] type = ((JDOFieldDescriptor) fd[j]).getSQLType();
                        idType[j] = (type == null) ? 0 : type[0];
                        FieldHandlerImpl fh = (FieldHandlerImpl) fd[j].getHandler();
                        idConvertTo[j] = fh.getConvertTo();
                        idConvertFrom[j] = fh.getConvertFrom();
                    } else {
                        throw new MappingException(
                                "Identity type must contains sql information: " + _name);
                    }
                }

//                String relatedType = fmFields[i].getType();
//                ClassDescriptor relDesc = 
//                    classDescrResolver.getMappingLoader().getDescriptor(relatedType);
                ClassDescriptor relDesc = null;
                try {
                    relDesc = ((JDOClassDescriptorResolverImpl) classDescrResolver).resolve(fmFields[i].getType());
                } catch (ResolverException e) {
                    throw new MappingException("Problem resolving class descriptor for class " 
                            + fmFields.getClass(), e);
                }
                                
                if (relDesc instanceof JDOClassDescriptor) {
                    FieldDescriptor[] relatedIds = ((JDOClassDescriptor) relDesc).getIdentities();
                    relatedIdSQL = new String[relatedIds.length];
                    relatedIdType = new int[relatedIds.length];
                    relatedIdConvertTo = new TypeConvertor[relatedIds.length];
                    relatedIdConvertFrom = new TypeConvertor[relatedIds.length];
                    for (int j = 0; j < relatedIdSQL.length; j++) {
                        if (relatedIds[j] instanceof JDOFieldDescriptor) {
                            String[] tempId = ((JDOFieldDescriptor) relatedIds[j]).getSQLName();
                            relatedIdSQL[j] = (tempId == null) ? null : tempId[0];
                            int[] tempType = ((JDOFieldDescriptor) relatedIds[j]).getSQLType();
                            relatedIdType[j] = (tempType == null) ? 0 : tempType[0];
                            FieldHandlerImpl fh = (FieldHandlerImpl) relatedIds[j].getHandler();
                            relatedIdConvertTo[j] = fh.getConvertTo();
                            relatedIdConvertFrom[j] = fh.getConvertFrom();
                        } else {
                            throw new MappingException(
                                    "Field type is not persistence-capable: "
                                    + relatedIds[j].getFieldName());
                        }
                    }
                }

                // if many-key exist, idSQL is overridden
                String[] manyKey = fmFields[i].getSql().getManyKey();
                if ((manyKey != null) && (manyKey.length != 0)) {
                    if (manyKey.length != idSQL.length) {
                        throw new MappingException(
                                "The number of many-keys doesn't match referred object: "
                                + clsDesc.getJavaClass().getName());
                    }
                    idSQL = manyKey;
                }

                // if name="" exist, relatedIdSQL is overridden
                String[] manyName = fmFields[i].getSql().getName();
                if ((manyName != null) && (manyName.length != 0)) {
                    if (manyName.length != relatedIdSQL.length) {
                        throw new MappingException(
                                "The number of many-keys doesn't match referred object: "
                                + relDesc.getJavaClass().getName());
                    }
                    relatedIdSQL = manyName;
                }
                
                _fhs[fieldMolderNumber] = new FieldMolder(ds, this, fmFields[i], manyTable, idSQL,
                        idType, idConvertTo, idConvertFrom,
                        relatedIdSQL, relatedIdType, relatedIdConvertTo, relatedIdConvertFrom);
            } else {
                _fhs[fieldMolderNumber] = new FieldMolder(ds, this, fmFields[i]);
            }
            
            // create RelationResolver instance
            _resolvers[fieldMolderNumber] = ResolverFactory.createRelationResolver(
                    _fhs[fieldMolderNumber], this, fieldMolderNumber, _debug);

            fieldMolderNumber += 1;
        }

        // ssa, FIXME : Are the two statements equivalents ?
        //        if ( Persistent.class.isAssignableFrom( _base ) )
        if (Persistent.class.isAssignableFrom(ds.resolve(_name))) {
            _callback = new JDOCallback();
        }
    }
    
    public ClassDescriptor getClassDescriptor() { return _clsDesc; }

    private boolean isFieldTransient(final FieldMapping fieldMapping) {
        boolean isFieldTransient = fieldMapping.getTransient();
        if (fieldMapping.getSql() != null) {
            isFieldTransient |= fieldMapping.getSql().getTransient();
        }
        return isFieldTransient;
    }

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
     */
    public boolean removeRelation(final TransactionContext tx, final Object object,
            final ClassMolder relatedMolder, final Object relatedObject) {

        boolean removed = false;
        boolean updateCache = false;
        boolean updatePersist = false;
        
        resetResolvers();
        
        UpdateAndRemovedFlags flags = null;
        for (int i = 0; i < _fhs.length; i++) {
            flags = _resolvers[i].removeRelation(tx, object, relatedMolder,
                    relatedObject);
            updateCache |= flags.getUpdateCache();
            updatePersist |= flags.getUpdatePersist();
            removed |= flags.getRemoved();
        }
        tx.markModified(object, updatePersist, updateCache);
        return removed;
    }

    /**
     * Determines the create priority of the data object class represented by
     * this ClassMolder. Concpetually, this method determines the order of
     * which data object should be created.
     *  
     * A priority of 0 indicates that an object represented by this ClassMolder
     * can be created independently, without having to consider any other 
     * data object.
     *
     * This method should only be called after DatingService is closed.
     */
    public int getPriority() {
        if (_priority == -2) {
            // circular reference detected, do not loop
            return 0;
        } else if (_priority < 0) {
            // mark current Molder to avoid infinite loop with circular reference
            _priority = -2;
            int maxPrior = 0;
            for (int i = 0; i < _fhs.length; i++) {
                if (_fhs[i].isPersistanceCapable()
                        && (_fhs[i].getFieldClassMolder() != this)
                        && _fhs[i].isStored()) {
                    int refPrior = _fhs[i].getFieldClassMolder().getPriority() + 1;
                    maxPrior = Math.max(maxPrior, refPrior);
                }
                /* should and "if case" for add _ids[i].isForeginKey() in the future */
            }

            /* adjust priority if there are dependent classes */
            if (_depends != null) {
                maxPrior = Math.max(maxPrior, _depends.getPriority() + 1);
            }

            /* adjust priority if there are extendent classes */
            if (_extends != null) {
                  maxPrior = Math.max(maxPrior, _extends.getPriority() + 1);
            }

            _priority  = maxPrior;
        }
        return _priority;
    }

    /**
     * Load an object with specified identity from the persistent storage.
     *
     * @param tx   the TransactionContext in action
     * @param oid  the object identity of the desired object
     * @param locker   the {@link DepositBox} of the object which is used to
     *                 store the dirty checking cache of the object.
     * @param suggestedAccessMode the acessMode for the object
     * @return the object stamp for the object in the persistent storage
     */
    public Object load(final TransactionContext tx, final OID oid, final DepositBox locker,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode)
    throws PersistenceException {
        return load(tx, oid, locker, proposedObject, suggestedAccessMode, null);
    }

    /**
     * Loads the field values.
     * @param tx Currently active transaction context
     * @param oid Current OID.
     * @param locker Current cache instance
     * @param proposedObject ProposedEntity instance
     * @param suggestedAccessMode Suggested access mode
     * @param results OQL QueryResults instance
     * @return A JDO timestamp.
     * @throws ObjectNotFoundException If the object in question cannot be found.
     * @throws PersistenceException For any other persistence-ralted problem.
     */
    private Object loadFields(final TransactionContext tx, final OID oid, final DepositBox locker,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode,
            final QueryResults results)
    throws PersistenceException {
                   
        Object stamp = null;
        AccessMode accessMode = getAccessMode(suggestedAccessMode);

        // set the field values to 'null' anyhow; if we don't find
        // in the cache later on, this indicates that the field values 
        // should be loaded from the persistence storage
        proposedObject.setFields(null);
        
        // try to load the field values from the cache, except when being told
        // to ignore them
        if (!proposedObject.isObjectLockObjectToBeIgnored()) {
            Object[] cachedFieldValues = locker.getObject(tx);
            if (_log.isDebugEnabled()) {
                StringBuffer buffer = new StringBuffer(80);
                buffer.append("Field values loaded from cache: ");
                if (cachedFieldValues != null) {
                    buffer.append("[");
                    for (int i = 0; i < cachedFieldValues.length; i++) {
                        buffer.append(cachedFieldValues[i]);
                        if (i > 0) {
                            buffer.append (",");
                        }
                    }
                    buffer.append("]");
                } else {
                    buffer.append("null");
                }
            }
            proposedObject.setFields(cachedFieldValues);
        }
        
        // load the fields from the persistent storage if the cache is empty
        // or the access mode is DBLOCKED (thus guaranteeing that a lock at the
        // database level will be created)
        if (!proposedObject.isFieldsSet() || accessMode == AccessMode.DbLocked) {
            proposedObject.initializeFields(_fhs.length);
            if (results != null) {
                stamp = results.getQuery().fetch(proposedObject);
            } else {
                Connection conn = tx.getConnection(oid.getMolder().getLockEngine());
                stamp = _persistence.load(conn, proposedObject, oid.getIdentity(), accessMode);
            }

            if (proposedObject.isExpanded()) {
                if (_log.isDebugEnabled()) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Actual object has been expanded from ");
                    sb.append(proposedObject.getProposedEntityClass());
                    sb.append(" to ");
                    sb.append(proposedObject.getActualEntityClass());
                    sb.append(", with the field values to set as follows:\n");
                    for (int i = 0; i < proposedObject.getNumberOfFields(); i++) {
                        sb.append("field ");
                        sb.append(i + 1);
                        sb.append(": ");
                        sb.append(proposedObject.getField(i));
                        sb.append('\n');
                    }
                    _log.debug(sb);
                }
            }

            oid.setDbLock(accessMode == AccessMode.DbLocked);
            
            // store (new) field values to cache
            locker.setObject(tx, proposedObject.getFields());

        }

        proposedObject.setActualClassMolder(this);

        return stamp;
    }

    public Object load(final TransactionContext tx, final OID oid, final DepositBox locker,
            final ProposedEntity proposedObject, final AccessMode suggestedAccessMode,
            final QueryResults results)
    throws PersistenceException {
        
        int fieldType;
        AccessMode accessMode = getAccessMode(suggestedAccessMode);
        
        resetResolvers();
        
        if (oid.getIdentity() == null) {
            throw new PersistenceException(
                    "The identities of the object to be loaded is null");
        }
        
        // obtain field values
        Object stamp = loadFields(tx, oid, locker, proposedObject, suggestedAccessMode, results);
        
        // if the object has been expanded, return early
        if (proposedObject.isExpanded()) {
            return stamp;
        }
        
        // set the timeStamp of the data object to locker's timestamp
        if (proposedObject.getEntity() instanceof TimeStampable) {
            ((TimeStampable) proposedObject.getEntity()).jdoSetTimeStamp(locker.getTimeStamp());
        }

        // set the identities into the target object
        setIdentity(tx, proposedObject.getEntity(), oid.getIdentity());

        // iterates thur all the field of the object and bind all field.
        for (int i = 0; i < _fhs.length; i++) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
            case FieldMolder.SERIALIZABLE:
            case FieldMolder.PERSISTANCECAPABLE:
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                _resolvers[i].load(tx, oid, proposedObject, accessMode);
                break;
            default:
                throw new PersistenceException("Unexpected field type!");
            }
        }
        return stamp;
    }

    /**
     * Create an object of the base class with specified identity into the persistence storage.
     *
     * @param tx   transaction in action
     * @param oid  the object identity of the object to be created.
     * @param locker the dirty checking cache of the object
     * @param object  the object to be created
     * @return  the identity of the object
     */
    public Identity create(final TransactionContext tx, final OID oid, final DepositBox locker,
            final Object object) throws PersistenceException {

        int fieldType;

        resetResolvers();

        if (_persistence == null) {
            throw new PersistenceException("non persistence capable: "
                    + oid.getName());
        }

        ProposedEntity entity = new ProposedEntity();
        entity.initializeFields(_fhs.length);
        Identity ids = oid.getIdentity();

        // copy the object to cache should make a new field now,
        for (int i = 0; i < _fhs.length; i++) {
            fieldType = _fhs[i].getFieldType();

            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
            case FieldMolder.PERSISTANCECAPABLE:
            case FieldMolder.SERIALIZABLE:
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                entity.setField(_resolvers[i].create(tx, object), i);
                break;

            default:
                throw new IllegalArgumentException("Field type invalid!");
            }
        }
        
        // ask Persistent to create the object into the persistence storage
        Identity createdId = _persistence.create(tx.getDatabase(),
                tx.getConnection(oid.getMolder().getLockEngine()), entity, ids);

        if (createdId == null) {
            throw new PersistenceException("Identity can't be created!");
        }

        // set the field values into the cache
        locker.setObject(tx, entity.getFields());
        oid.setDbLock(true);

        // set the new timeStamp into the data object
        if (object instanceof TimeStampable) {
            ((TimeStampable) object).jdoSetTimeStamp(locker.getTimeStamp());
        }

        // set the identity into the object
        setIdentity(tx, object, createdId);

        // after successful creation, add the entry in the relation table for
        // all many-to-many relationship
        for (int i = 0; i < _fhs.length; i++) {
            entity.setField(_resolvers[i].postCreate(
                    tx, oid, object, entity.getField(i), createdId), i);
        }

        return createdId;
    }


    /**
     * Walk the object model and mark object that should be created.
     *
     * @param tx   transaction in action
     * @param oid  the object identity of the object to be created.
     * @param locker the dirty checking cache of the object
     * @param object  the object to be created
     */
    public void markCreate(final TransactionContext tx, final OID oid, final DepositBox locker,
            final Object object) throws PersistenceException {

        boolean updateCache = false;

        resetResolvers();

        // iterate all the fields and mark all the dependent object.
        for (int i = 0; i < _fhs.length; i++) {
            updateCache |= _resolvers[i].markCreate(tx, oid, object);
        }

        tx.markModified(object, false, updateCache);

    }

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
     * @param locker the dirty check cache for the object
     * @param object the data object to be checked
     * @param timeout  timeout of updating the lock if needed
     *
     * @return true if the object is modified
     */
    public boolean preStore(final TransactionContext tx, final OID oid, final DepositBox locker,
            final Object object, final int timeout) throws PersistenceException {

        if (oid.getIdentity() == null) {
            throw new PersistenceException(Messages.format(
                    "persist.missingIdentityForStore", _name));
        }

        if (!oid.getIdentity().equals(getIdentity(tx, object))) {
            throw new PersistenceException(Messages.format(
                    "jdo.identityChanged", _name, oid.getIdentity(),
                    getIdentity(tx, object)));
        }

        Object[] fields = locker.getObject(tx);

        if (fields == null) {
            throw new PersistenceException(
                    Messages.format("persist.objectNotFound", _name, oid));
        }

        Object[] newfields = new Object[_fhs.length];
        boolean updateCache = false;
        boolean updatePersist = false;

        // iterate thru all the data object fields for modification
        UpdateFlags flags;
        for (int i = 0; i < newfields.length; i++) {
            flags = _resolvers[i].preStore(tx, oid, object, timeout, fields[i]);
            updateCache |= flags.getUpdateCache();
            updatePersist |= flags.getUpdatePersist();
            newfields[i] = flags.getNewField();
        }

        tx.markModified(object, updatePersist, updateCache);

        if (updateCache || updatePersist) {
            tx.writeLock(object, timeout);
        }

        return updateCache;
    }

    /**
     * Store a data object into the persistent storage of the base class of this
     * ClassMolder.
     *
     * @param tx Transaction in action
     * @param oid the object identity of the stored object
     * @param locker the dirty check cache of the object
     * @param object the object to be stored
     */
    public void store(final TransactionContext tx, final OID oid, final DepositBox locker,
            final Object object) throws PersistenceException {

        if (oid.getIdentity() == null) {
            throw new PersistenceException(Messages.format(
                    "persist.missingIdentityForStore", _name));
        }

        if (!oid.getIdentity().equals(getIdentity(tx, object))) {
            throw new PersistenceException(Messages.format(
                    "jdo.identityChanged", _name, oid.getIdentity(),
                    getIdentity(tx, object)));
        }

        // load field values from cache (if availabe)
        ProposedEntity oldentity = new ProposedEntity();
        oldentity.setFields(locker.getObject(tx));

        if (oldentity.getFields() == null) {
            throw new PersistenceException(
                    Messages.format("persist.objectNotFound", _name, oid));
        }

        ProposedEntity newentity = new ProposedEntity();
        newentity.initializeFields(_fhs.length);
        for (int i = 0; i < _fhs.length; i++) {
            newentity.setField(_resolvers[i].store(tx, object, oldentity.getField(i)), i);
        }
        
        Connection conn = tx.getConnection(oid.getMolder().getLockEngine());
        oid.setStamp(_persistence.store(conn, oid.getIdentity(), newentity, oldentity));
    }

    /**
     * Update the object which loaded or created in the other transaction to
     * the persistent storage.
     *
     * @param tx Transaction in action
     * @param oid the object identity of the stored object
     * @param locker the dirty check cache of the object
     * @param object the object to be stored
     * @return boolean true if the updating object should be created
     */
    public boolean update(final TransactionContext tx, final OID oid, final DepositBox locker,
            final Object object, final AccessMode suggestedAccessMode) throws PersistenceException {

        AccessMode accessMode = getAccessMode(suggestedAccessMode);

        resetResolvers();

        Object[] fields = locker.getObject(tx);

        if ((!isDependent()) && (!_timeStampable)) {
            throw new IllegalArgumentException(
                    "A master object that involves in a long transaction must be a TimeStampable!");
        }

        long lockTimestamp = locker.getTimeStamp();
        long objectTimestamp = _timeStampable ? ((TimeStampable) object)
                .jdoGetTimeStamp() : 1;

        if ((objectTimestamp > 0) && (oid.getIdentity() != null)) {
            // valid range of timestamp
            
            if ((_timeStampable) && (lockTimestamp == TimeStampable.NO_TIMESTAMP)) {
                throw new PersistenceException(Messages.format(
                        "persist.objectNotInCache", _name, oid.getIdentity())); 
            }

            if (_timeStampable && objectTimestamp != lockTimestamp) {
                throw new ObjectModifiedException("Timestamp mismatched!");
            }

            if (!_timeStampable && isDependent() && (fields == null)) {
                // allow a dependent object not implements timeStampable
                fields = new Object[_fhs.length];
                Connection conn = tx.getConnection(oid.getMolder().getLockEngine());
                
                ProposedEntity proposedObject = new ProposedEntity(this);
                proposedObject.setProposedEntityClass(object.getClass());
                proposedObject.setEntity(object);
                proposedObject.setFields(fields);
                _persistence.load(conn, proposedObject, oid.getIdentity(), accessMode);
                fields = proposedObject.getFields();
                
                oid.setDbLock(accessMode == AccessMode.DbLocked);
                locker.setObject(tx, proposedObject.getFields());
            }

            // load the original field into the transaction. so, store will
            // have something to compare later.
            try {
                for (int i = 0; i < _fhs.length; i++) {
                    _resolvers[i].update(tx, oid, object, accessMode, fields[i]);
                }
            } catch (ObjectNotFoundException e) {
                _log.warn(e.getMessage(), e);
                throw new ObjectModifiedException(
                        "dependent object deleted concurrently");
            }
            return false;
        } else if ((objectTimestamp == TimeStampable.NO_TIMESTAMP) || (objectTimestamp == 1)) {
            // work almost like create, except update the sub field instead of create
            // iterate all the fields and mark all the dependent object.
            boolean updateCache = false;

            for (int i = 0; i < _fhs.length; i++) {
                updateCache |= _resolvers[i].updateWhenNoTimestampSet(
                        tx, oid, object, suggestedAccessMode);
            }

            tx.markModified(object, false, updateCache);
            return true;
        } else {
            if (_log.isWarnEnabled()) {
                _log.warn("object: " + object + " timestamp: " + objectTimestamp
                        + " lockertimestamp: " + lockTimestamp);
            }
            throw new ObjectModifiedException(
                    "Invalid object timestamp detected.");
        }
    }

    /**
     * Update the dirty checking cache. This method is called after a 
     * transaction completed successfully.
     *
     * @param tx - transaction in action
     * @param oid - object's identity of the target object
     * @param locker - the dirty checking cache of the target object
     * @param object - the target object
     */
    public void updateCache(final TransactionContext tx, 
            final OID oid, 
            final DepositBox locker, 
            final Object object) {

        Object[] fields;
        int fieldType;

        resetResolvers();

        if (oid.getIdentity() == null) {
            throw new IllegalStateException(
                    Messages.format("persist.missingIdentityForCacheUpdate", _name));
        }

        fields = new Object[_fhs.length];

        for (int i = 0; i < _fhs.length; i++) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
            case FieldMolder.SERIALIZABLE:
            case FieldMolder.PERSISTANCECAPABLE:
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                fields[i] = _resolvers[i].updateCache(tx, oid, object);
                break;
            default:
                throw new IllegalArgumentException("Field type invalid!");
            }
        }

        // store new field values in cache
        locker.setObject(tx, fields);
        
        if (object instanceof TimeStampable) {
            ((TimeStampable) object).jdoSetTimeStamp(locker.getTimeStamp());
        }

    }

    /**
     * Delete an object of the base type from the persistence storage.
     * All object to be deleted by this method will be <tt>markDelete</tt>
     * before it method is called.
     *
     * @param tx - transaction in action
     * @param oid - the object identity of the target object
     */
    public void delete(final TransactionContext tx, final OID oid) throws PersistenceException {

        resetResolvers();

        Identity ids = oid.getIdentity();

        for (int i = 0; i < _fhs.length; i++) {
            if (_fhs[i].isManyToMany()) {
                _fhs[i].getRelationLoader().deleteRelation(
                        tx.getConnection(oid.getMolder().getLockEngine()), ids);
            }
        }

        _persistence.delete(tx.getConnection(oid.getMolder().getLockEngine()), ids);

        // All field along the extend path will be deleted by transaction
        // However, everything off the path must be deleted by ClassMolder.

        Vector extendPath = new Vector();
        ClassMolder base = this;
        while (base != null) {
            extendPath.add(base);
            base = base._extends;
        }

        base = _depends;
        while (base != null) {
            if (base._extendent != null) {
                for (int i = 0; i < base._extendent.size(); i++) {
                    if (extendPath.contains(base._extendent.get(i))) {
                        // NB: further INVESTIGATION
                    }
                }
            }

            base = base._extends;
        }

    }

    /**
     * Prepare to delete an object with the specified identity. If any sub-object
     * should be deleted along with the target object, it should be deleted
     * by this method.
     *
     * @param tx - transaction in action
     * @param oid - object's identity of the target object
     * @param locker - the dirty checking cache of the target object
     * @param object - the target object
     */
    public void markDelete(final TransactionContext tx, final OID oid, final DepositBox locker,
            final Object object) throws PersistenceException {

        resetResolvers();

        Object[] fields = locker.getObject(tx);

        for (int i = 0; i < _fhs.length; i++) {
            int fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
            case FieldMolder.SERIALIZABLE:
            case FieldMolder.PERSISTANCECAPABLE:
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                _resolvers[i].markDelete(tx, object, fields[i]);
                break;
            default:
                throw new PersistenceException("Invalid field type!");
            }
        }
    }

    /**
     * Revert the object back to the state of begining of the transaction
     * If the object is loaded, it will be revert as it was loaded. If the
     * object is created, it will be revert as it was just created.
     *
     * @param tx - transaction in action
     * @param oid - the object identity of the target object
     * @param locker - the dirty checking cache of the target object
     * @param object - the target object
     */
    public void revertObject(final TransactionContext tx, 
            final OID oid, 
            final DepositBox locker, 
            final Object object)
            throws PersistenceException {

        resetResolvers();

        if (oid.getIdentity() == null) {
            throw new PersistenceException(
                    Messages.format("persist.missingIdentityForReverting", _name));
        }

        Object[] fields = locker.getObject(tx);

        setIdentity(tx, object, oid.getIdentity());

        for (int i = 0; i < _fhs.length; i++) {
            if (fields != null) {
                _resolvers[i].revertObject(tx, oid, object, fields[i]);
            } else {
                _resolvers[i].revertObject(tx, oid, object, null);
            }
        }
    }

    /**
     * Return a new instance of the base class with the provided ClassLoader object.
     *
     * @param loader the ClassLoader object to use to create a new object.
     * @return Object the object reprenseted by this ClassMolder, and instanciated with the
     *         provided ClassLoader instance.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object newInstance(final ClassLoader loader)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class aClass = null;
        aClass = ClassLoadingUtils.loadClass(loader, _name);
        return aClass.newInstance();
    }

    /**
     * Get the effective accessMode of the the base type.
     * 
     * @param txMode - the default transaction accessMode.
     * @return the effective acessMode of the base type.
     */
    public AccessMode getAccessMode(final AccessMode txMode) {

        if (txMode == null) {
            return _accessMode;
        }

        if (_accessMode == AccessMode.ReadOnly || txMode == AccessMode.ReadOnly) {
            return AccessMode.ReadOnly;
        }
        if (_accessMode == AccessMode.DbLocked || txMode == AccessMode.DbLocked) {
            return AccessMode.DbLocked;
        }
        if (_accessMode == AccessMode.Exclusive
                || txMode == AccessMode.Exclusive) {
            return AccessMode.Exclusive;
        }
        return txMode;

    }

    /**
     * Get the callback interceptor of the base type.
     */
    public CallbackInterceptor getCallback() {
        return _callback;
    }

    /**
     * Test if the specified identity is the default value of the type.
     */
    public boolean isDefaultIdentity(final Identity identity) {
        if (identity == null) { return true; }
        for (int i = 0; i < identity.size(); i++) {
            if (!_ids[i].isDefault(identity.get(i))) { return false; }
        }
        return true;
    }

    /**
     * Get the identity from a object of the base type.
     * If object isn't persistent and key generator is used, returns null.
     *
     * @param tx the transaction context.
     * @param o - object of the base type.
     * @return return an Object[] which contains the identity of the object.
     */
    public Identity getIdentity(final TransactionContext tx, final Object o) {
        // [oleg] In the case where key generator is used,
        // the value of identity is dummy, set it to null
        if (isKeyGeneratorUsed() && !(tx.isPersistent(o) || tx.isReadOnly(o))) {
            return null;
        }
        return getActualIdentity(tx, o);
    }

    /**
     * Get the identity from a object of the base type.
     *
     * @param tx the transaction context.
     * @param o - object of the base type.
     * @return return an Object[] which contains the identity of the object.
     */
    public Identity getActualIdentity(final TransactionContext tx, final Object o) {
        return getActualIdentity(tx.getClassLoader(), o);
    }

    /**
     * Get the identity from a object of the base type.
     *
     * @param loader the current class loader.
     * @param o - object of the base type.
     * @return return an Object[] which contains the identity of the object.
     */
    public Identity getActualIdentity(final ClassLoader loader, final Object o) {
        Object[] ids = new Object[_ids.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = _ids[i].getValue(o, loader);
        }
        if (ids[0] == null) { return null; }
        return new Identity(ids);
    }

    /**
     * Set the identity into an object.
     *
     * @param tx the transaction context.
     * @param object the object to set the identity.
     * @param identity the new identity for the object.
     */
    public void setIdentity(final TransactionContext tx, 
            final Object object, 
            final Identity identity)
            throws PersistenceException {

        if (identity.size() != _ids.length) {
            throw new PersistenceException("Identity size mismatched!");
        }

        for (int i = 0; i < _ids.length; i++) {
            _ids[i].setValue(object, identity.get(i), tx.getClassLoader());
        }
    }
    
    /**
     * Get the Persisetence of the base type.
     */
    public Persistence getPersistence() {
        return _persistence;
    }

    /**
     * Mutator method to set the PersistenceEngine of.
     */
    public void setPersistence(final Persistence persist) {
        _persistence = persist;
    }

    /**
     * Get the base class of this ClassMolder given a ClassLoader.
     * 
     * @param loader the classloader.
     * @return the <code>Class</code> instance.
     */
    public Class getJavaClass(final ClassLoader loader) {
        Class result = null;
        try {
            result = ClassLoadingUtils.loadClass(loader, _name);
        } catch (ClassNotFoundException e) {
            _log.error("Unable to load base class of " + getName(), e);
        }
        return result;
    }

    // ssa, FIXME : is that method necessary ?
    /**
     * check if the current ClassModlder is assignable from the <code>class</code>
     * instance.
     *
     * @param cls the Class to check the assignation
     * @return true if assignable
     */
    public boolean isAssignableFrom (final Class cls) {
        ClassLoader loader = cls.getClassLoader();
        Class molderClass = null;
        try {
            molderClass = ClassLoadingUtils.loadClass(loader, _name);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return molderClass.isAssignableFrom(cls);
    }

    /**
     * Get the fully qualified name of the base type of this ClassMolder.
     */
    public String getName() {
        return _name;
    }

    /**
     * Get the FieldMolder of the fields of the base type,
     * except the identity fields.
     */
    public FieldMolder[] getFields() {
        return _fhs;
    }

    /**
     * Get the FieldMolders of the identity fields.
     */
    public FieldMolder[] getIds() {
        return _ids;
    }

    /**
     * Get the extends class' ClassMolder.
     */
    public ClassMolder getExtends() {
        return _extends;
    }

    /**
     * Get the depends class' ClassMolder.
     */
    public ClassMolder getDepends() {
        return _depends;
    }

    /**
     * Get the LockEngine which this ClassMolder belongs to.
     */
    public LockEngine getLockEngine() {
        return _engine;
    }

    /**
     * Returns the active cache parameters.
     * 
     * @return Active cache parameters.
     */
    public Properties getCacheParams() {
        return _cacheParams;
    }

    /**
     * Return true if the base type of this ClassMolder is an dependent class.
     */
    public boolean isDependent() {
        return _depends != null;
    }

    /**
     * Set all persistence fields of object of the base type to null.
     * @param object - target object
     */
    public void setFieldsNull(final Object object) {
        // TODO [WG]: remove method ?
        /*
        for ( int i=0; i < _ids.length; i++ ) {
            _ids[i].setValue( object, null );
        }
        for ( int i=0; i < _fhs.length; i++ ) {
            _fhs[i].setValue( object, null );
        }*/
    }

    /**
     * Mutator method to add a extent ClassMolder.
     */
    void addExtendent(final ClassMolder ext) {
        if (_extendent == null) {
            _extendent = new Vector();
        }
        _extendent.add(ext);
    }

    /**
     * Mutator method to add a dependent ClassMolder.
     */
    void addDependent(final ClassMolder dep) {
        if (_dependent == null) {
            _dependent = new Vector();
        }
        _dependent.add(dep);
    }

    /**
     * Mutator method to set the extends ClassMolder.
     */
    void setExtends(final ClassMolder ext) {
        _extends = ext;
        ext.addExtendent(this);
    }

    /**
     * Mutator method to set the depends ClassMolder.
     */
    void setDepends(final ClassMolder dep) {
        _depends = dep;
        dep.addDependent(this);
    }

    public String toString() {
        return "ClassMolder " + _name;
    }

    /**
     * Return true if a key generator is used for the base type of this ClassMolder.
     */
    public boolean isKeyGenUsed() {
        return _isKeyGenUsed;
    }

    /**
     * Return true if a key generator is used for the base type of this ClassMolder.
     */
    public boolean isKeyGeneratorUsed() {
        return _isKeyGenUsed || (_extends != null && _extends.isKeyGeneratorUsed());
    }

    /**
     * Inspect the fields stored in the object passed as an argument for
     * contained objects.  Request an expireCache for each contained object.
     *
     * @param tx The {@link org.castor.persist.TransactionContext}
     * @param locker The object that contains the fields to be inspected
     */
    public void expireCache(final TransactionContext tx, final ObjectLock locker)
    throws PersistenceException {
        
        // TODO [WG]: can this really happen, or is this obsolete code
        if (locker == null) {
            return;
        }

        // get field values from cache
        Object[] fields = locker.getObject();

        if (fields == null) {
            return;
        }

        // iterate through all the field values and expire contained objects
        for (int i = 0; i < _fhs.length; i++) {
            _resolvers[i].expireCache(tx, fields[i]);
        }
    }
    
    public void resetResolvers () {
        if (!_resolversHaveBeenReset) {
            for (int i = 0; i < _fhs.length; i++) {
                _resolvers[i] = ResolverFactory.createRelationResolver (_fhs[i], this, i, _debug);
            }
            
            _resolversHaveBeenReset = true;
        }
    }

    /**
     * Returns the actual (OQL) statement for the specified named query.
     * @param name Named query name.
     * @return The actual (OQL) statement 
     */
    public String getNamedQuery(final String name) {
        return (String) ((JDOClassDescriptor) _clsDesc).getNamedQueries().get(name);
    }
}


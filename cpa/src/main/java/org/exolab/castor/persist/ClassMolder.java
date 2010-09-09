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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.util.JDOClassDescriptorResolver;
import org.castor.jdo.util.ClassLoadingUtils;
import org.castor.persist.ProposedEntity;
import org.castor.persist.TransactionContext;
import org.castor.persist.UpdateAndRemovedFlags;
import org.castor.persist.UpdateFlags;
import org.castor.persist.resolver.ResolverFactory;
import org.castor.persist.resolver.ResolverStrategy;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.jdo.engine.JDOCallback;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.NamedNativeQuery;
import org.exolab.castor.mapping.xml.Sql;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.ResolverException;

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
    private Vector<ClassMolder> _dependent;

    /** A Vector of <tt>ClassMolder</tt>s for all the direct extending class of the
     *  base class. */
    private Vector<ClassMolder> _extendent;

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
     * @param classDescriptorResolver {@link ClassDescriptorResolver} instance
     * @param lock the lock engine.
     * @param classDescriptor the classDescriptor for the base class.
     * @param persistenceEngine the Persistence for the base class.
     * @throws ClassNotFoundException If a class cannot be loaded.
     * @throws MappingException if an error occurred with analyzing the mapping information.
     */
    ClassMolder(final DatingService ds, final ClassDescriptorResolver classDescriptorResolver, 
            final LockEngine lock, final ClassDescriptor classDescriptor, 
            final Persistence persistenceEngine)
            throws ClassNotFoundException, MappingException {

        _engine = lock;
        _persistence = persistenceEngine;
        _clsDesc = classDescriptor;
        
        ClassMapping classMapping = ((ClassDescriptorImpl) classDescriptor).getMapping();
        _name = classDescriptor.getJavaClass().getName();
        _accessMode = AccessMode.valueOf(classMapping.getAccess().toString());

        ds.register(_name, this);

        // TODO: replace usage of ClassMapping.getDepends() with use of ClassDescriptor.get???()
        ClassMapping dependClassMapping = (ClassMapping) classMapping.getDepends();
//        ClassDescriptor dependClassDescriptor = ((ClassDescriptorImpl) classDescriptor).getDepends();
        // TODO: replace usage of ClassMapping.getExtends() with use of ClassDescriptor.get???()
        ClassMapping extendsClassMapping = (ClassMapping) classMapping.getExtends();
//        ClassDescriptor extendsClassDescriptor = ((ClassDescriptorImpl) classDescriptor).getExtends();

        //if ( dep != null && ext != null )
        //    throw new MappingException("A JDO cannot both extends and depends on other objects");

        if (dependClassMapping != null) {
            ds.pairDepends(this, dependClassMapping.getName());
        }
//        if (dependClassDescriptor != null) {
//            ds.pairDepends(this, dependClassDescriptor.getClass().getName());
//        }

        if (extendsClassMapping != null) {
            ds.pairExtends(this, extendsClassMapping.getName());
        }
//        if (extendsClassDescriptor != null) {
//            ds.pairExtends(this, extendsClassDescriptor.getClass().getName());
//        }

        if (classDescriptor.hasNature(ClassDescriptorJDONature.class.getName())) {
            ClassDescriptorJDONature nature = new ClassDescriptorJDONature(classDescriptor);
            _cacheParams = nature.getCacheParams();
            _isKeyGenUsed = nature.getKeyGeneratorDescriptor() != null;
        }

        // construct <tt>FieldMolder</tt>s for each of the identity fields of
        // the base class.
        
        //TODO[WG]: in oder to switch towards the use of ClassDescriptor.getIdentities()
        //TODO[WG]: one needs to replicate the functionality of getIdFields() on the descriptor side.
        FieldMapping[] identityFieldMappings = ClassMolderHelper.getIdFields(classMapping);
        _ids = new FieldMolder[identityFieldMappings.length];
        int m = 0;
        for (FieldMapping identityFieldMapping : identityFieldMappings) {
            _ids[m++] = new FieldMolder(ds, this, identityFieldMapping);
            
        }

        // construct <tt>FieldModlers</tt>s for each of the non-transient fields 
        // of the base class 
        //TODO[WG]: in oder to switch towards the use of ClassDescriptor.getFields()
        //TODO[WG]: one needs to replicate the functionality of getFullFields() on the descriptor side.
        FieldMapping[] fieldMappings = ClassMolderHelper.getFullFields(classMapping);
        
        int numberOfNonTransientFieldMolders = 0;
        for (FieldMapping fieldMapping : fieldMappings) {
            if (!isFieldTransient(fieldMapping)) {
                numberOfNonTransientFieldMolders += 1;
            }
        }

        _fhs = new FieldMolder[numberOfNonTransientFieldMolders];
        _resolvers = new ResolverStrategy[numberOfNonTransientFieldMolders];
        
        int fieldMolderCount = 0;
        for (FieldMapping fieldMapping : fieldMappings) {
            
            // don't create field molder for transient fields
            if (isFieldTransient(fieldMapping)) {
                continue;
            }
            
            Sql sqlMapping = fieldMapping.getSql();
            
            if ((sqlMapping != null) && (sqlMapping.getManyTable() != null)) {
                // the fields is not primitive
                String[] relatedIdSQL = null;
                int[] relatedIdType = null;
                TypeConvertor[] relatedIdConvertTo = null;
                TypeConvertor[] relatedIdConvertFrom = null;
                
                String manyTable = sqlMapping.getManyTable();

                String[] idSQL = new String[identityFieldMappings.length];
                int[] idType = new int[identityFieldMappings.length];
                TypeConvertor[] idConvertFrom = new TypeConvertor[identityFieldMappings.length];
                TypeConvertor[] idConvertTo = new TypeConvertor[identityFieldMappings.length];
                FieldDescriptor[] identityFieldDescriptors = ((ClassDescriptorImpl) classDescriptor).getIdentities();
                int identityFieldCount = 0;
                for (FieldMapping identityFieldMapping : identityFieldMappings) {
                    idSQL[identityFieldCount] = identityFieldMapping.getSql().getName()[0];
                    FieldDescriptor identityFieldDescriptor = identityFieldDescriptors[identityFieldCount];
                    if (identityFieldDescriptor.hasNature(FieldDescriptorJDONature.class.getName())) {
                        int[] type = new FieldDescriptorJDONature(identityFieldDescriptor).getSQLType();
                        idType[identityFieldCount] = (type == null) ? 0 : type[0];
                        FieldHandlerImpl fh = (FieldHandlerImpl) identityFieldDescriptor.getHandler();
                        idConvertTo[identityFieldCount] = fh.getConvertTo();
                        idConvertFrom[identityFieldCount] = fh.getConvertFrom();
                    } else {
                        throw new MappingException(
                                "Identity type must contains sql information: " + _name);
                    }
                    identityFieldCount++;
                }

                ClassDescriptor relatedClassDescriptor = null;
                try {
                    JDOClassDescriptorResolver jdoCDR = (JDOClassDescriptorResolver) classDescriptorResolver;
                    relatedClassDescriptor = jdoCDR.resolve(fieldMapping.getType());
                } catch (ResolverException e) {
                    throw new MappingException("Problem resolving class descriptor for class " 
                            + fieldMappings.getClass(), e);
                }
                                
                if (relatedClassDescriptor.hasNature(ClassDescriptorJDONature.class.getName())) {
                    FieldDescriptor[] relatedIdentityDescriptors = ((ClassDescriptorImpl) relatedClassDescriptor).getIdentities();
                    relatedIdSQL = new String[relatedIdentityDescriptors.length];
                    relatedIdType = new int[relatedIdentityDescriptors.length];
                    relatedIdConvertTo = new TypeConvertor[relatedIdentityDescriptors.length];
                    relatedIdConvertFrom = new TypeConvertor[relatedIdentityDescriptors.length];
                    int relatedIdentityCount = 0;
                    for (FieldDescriptor relatedIdentityDescriptor : relatedIdentityDescriptors) {
                        if (relatedIdentityDescriptor.hasNature(FieldDescriptorJDONature.class.getName())) {
                            FieldDescriptorJDONature nature = new FieldDescriptorJDONature(relatedIdentityDescriptor);
                            String[] tempId = nature.getSQLName();
                            relatedIdSQL[relatedIdentityCount] = (tempId == null) ? null : tempId[0];
                            int[] tempType =  nature.getSQLType();
                            relatedIdType[relatedIdentityCount] = (tempType == null) ? 0 : tempType[0];
                            FieldHandlerImpl fh = (FieldHandlerImpl) relatedIdentityDescriptors[relatedIdentityCount].getHandler();
                            relatedIdConvertTo[relatedIdentityCount] = fh.getConvertTo();
                            relatedIdConvertFrom[relatedIdentityCount] = fh.getConvertFrom();
                        } else {
                            throw new MappingException(
                                    "Field type is not persistence-capable: "
                                    + relatedIdentityDescriptors[relatedIdentityCount].getFieldName());
                        }
                        relatedIdentityCount++;
                    }
                }

                // if many-key exist, idSQL is overridden
                String[] manyKey = sqlMapping.getManyKey();
                if ((manyKey != null) && (manyKey.length != 0)) {
                    if (manyKey.length != idSQL.length) {
                        throw new MappingException(
                                "The number of many-keys doesn't match referred object: "
                                + classDescriptor.getJavaClass().getName());
                    }
                    idSQL = manyKey;
                }

                // if name="" exist, relatedIdSQL is overridden
                String[] manyName = fieldMapping.getSql().getName();
                if ((manyName != null) && (manyName.length != 0)) {
                    if (manyName.length != relatedIdSQL.length) {
                        throw new MappingException(
                                "The number of many-keys doesn't match referred object: "
                                + relatedClassDescriptor.getJavaClass().getName());
                    }
                    relatedIdSQL = manyName;
                }
                
                SQLRelationLoader loader = _persistence.createSQLRelationLoader(
                        manyTable, idSQL, idType, idConvertTo, idConvertFrom,
                        relatedIdSQL, relatedIdType, relatedIdConvertTo, relatedIdConvertFrom);
                _fhs[fieldMolderCount] = new FieldMolder(ds, this, fieldMapping, loader);
            } else {
                _fhs[fieldMolderCount] = new FieldMolder(ds, this, fieldMapping);
            }
            
            // create RelationResolver instance
            _resolvers[fieldMolderCount] = ResolverFactory.createRelationResolver(
                    _fhs[fieldMolderCount], this, fieldMolderCount);

            fieldMolderCount += 1;
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
            // find root of extends hierarchy
            ClassMolder root = this;
            while (root._extends != null) { root = root._extends; }
            
            // find all class molders of extends hierarchy
            List<ClassMolder> molders = getAllExtendentMolders(root);
            
            // mark all class molders of extends hierarchy as work in progress
            for (ClassMolder molder : molders) { molder._priority = -2; }
            
            // preset maximum priority
            int maxPriority = 0;
            
            // calculate maximum priority from all classes referenced by the whole hierarchy
            for (ClassMolder molder : molders) {
                FieldMolder[] fhs = molder._fhs;
                for (int i = 0; i < fhs.length; i++) {
                    FieldMolder fh = fhs[i];
                    if (fh.isPersistanceCapable() && fh.isStored()
                            && (fh.getFieldClassMolder() != this)) {
                        int refPriority = fh.getFieldClassMolder().getPriority() + 1;
                        maxPriority = Math.max(maxPriority, refPriority);
                    }
                }
            }

            // set priority of the whole hierarchy to the calculated maximum
            for (ClassMolder molder : molders) { molder._priority = maxPriority; }
        }
        
        return _priority;
    }
    
    private List<ClassMolder> getAllExtendentMolders(final ClassMolder root) {
        List<ClassMolder> molders = new ArrayList<ClassMolder>();
        molders.add(root);
        
        if (root._extendent != null) {
            for (ClassMolder extendent : root._extendent) {
                molders.addAll(getAllExtendentMolders(extendent));
            }
        }
        return molders;
    }

    public void loadTimeStamp(final TransactionContext tx, final DepositBox locker,
            final AccessMode suggestedAccessMode)
    throws PersistenceException {
        Object loadObject = null;
        try {
            loadObject = newInstance(tx.getClassLoader());
        } catch (Exception ex) {
            throw new PersistenceException("failed to load object", ex);
        }

        ProposedEntity proposedObject = new ProposedEntity(this);
        proposedObject.setProposedEntityClass(loadObject.getClass());
        proposedObject.setEntity(loadObject);
        
        OID oid = locker.getOID();
        
        Object[] cachedFieldValues = locker.getObject(tx);
        proposedObject.setFields(cachedFieldValues);
        
        AccessMode accessMode = getAccessMode(suggestedAccessMode);

        // load the fields from the persistent storage if the cache is empty
        // or the access mode is DBLOCKED (thus guaranteeing that a lock at the
        // database level will be created)
        if (!proposedObject.isFieldsSet() || accessMode == AccessMode.DbLocked) {
            proposedObject.initializeFields(_fhs.length);

            Connection conn = tx.getConnection(oid.getMolder().getLockEngine());
            _persistence.load(conn, proposedObject, oid.getIdentity(), accessMode);

            oid.setDbLock(accessMode == AccessMode.DbLocked);
            
            // store (new) field values to cache
            locker.setObject(tx, proposedObject.getFields(), System.currentTimeMillis());
        }

        mold(tx, locker, proposedObject, accessMode);
    }

    /**
     * Loads the field values.
     * 
     * @param tx Currently active transaction context
     * @param locker Current cache instance
     * @param proposedObject ProposedEntity instance
     * @param accessMode Suggested access mode
     * @param results OQL QueryResults instance
     * @throws ObjectNotFoundException If the object in question cannot be found.
     * @throws PersistenceException For any other persistence-related problem.
     */
    public void load(final TransactionContext tx, final DepositBox locker,
            final ProposedEntity proposedObject, final AccessMode accessMode,
            final QueryResults results)
    throws PersistenceException {
        OID oid = locker.getOID();
        if (oid.getIdentity() == null) {
            throw new PersistenceException(
                    "The identities of the object to be loaded is null");
        }
        
        proposedObject.initializeFields(_fhs.length);
        if (results != null) {
            results.getQuery().fetch(proposedObject);
        } else {
            Connection conn = tx.getConnection(oid.getMolder().getLockEngine());
            _persistence.load(conn, proposedObject, oid.getIdentity(), accessMode);
        }

        oid.setDbLock(accessMode == AccessMode.DbLocked);
        
        // store (new) field values to cache
        locker.setObject(tx, proposedObject.getFields(), System.currentTimeMillis());
    }

    public void mold(final TransactionContext tx, final DepositBox locker,
            final ProposedEntity proposedObject, final AccessMode accessMode)
    throws PersistenceException {
        OID oid = locker.getOID();

        // Check for version field.
        if (_clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(_clsDesc);
            String versionField = jdoNature.getVersionField();

            // Check if version field was set and has content.
            // TODO: that check should be moved to e.g. ClassDescriptorJDONature
            if (versionField != null && versionField.length() > 0) {
                // Find field descriptor for version field.
                FieldDescriptor versionFieldDescriptor = jdoNature.getField(versionField);
                FieldHandler fieldHandler = versionFieldDescriptor.getHandler();
                // Set the entity's version to the locker's version.
                fieldHandler.setValue(proposedObject.getEntity(), locker.getVersion());
            }
        }

        // set the timeStamp of the data object to locker's timestamp
        if (proposedObject.getEntity() instanceof TimeStampable) {
            ((TimeStampable) proposedObject.getEntity()).jdoSetTimeStamp(locker
                    .getVersion());
        }
        
        // set the identities into the target object
        setIdentity(tx, proposedObject.getEntity(), oid.getIdentity());

        // iterates over all the field of the object and bind all field.
        for (int i = 0; i < _fhs.length; i++) {
            FieldPersistenceType fieldType = _fhs[i].getFieldPertsistenceType();
            
            switch (fieldType) {
            case PRIMITIVE:
            case SERIALIZABLE:
            case PERSISTANCECAPABLE:
            case ONE_TO_MANY:
            case MANY_TO_MANY:
                _resolvers[i].load(tx, oid, proposedObject, accessMode);
                break;
            default:
                throw new PersistenceException("Unexpected field type!");
            }
        }
        
        // Check for version field.
        if (_clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            ClassDescriptorJDONature jdoNature =
                    new ClassDescriptorJDONature(_clsDesc);
            String versionField = jdoNature.getVersionField();

            // Check if version field was set and has content.
            if (versionField != null && versionField.length() > 0) {
                // Find field descriptor for version field.
                FieldDescriptor versionFieldDescriptor =
                        jdoNature.getField(versionField);
                FieldHandler fieldHandler = versionFieldDescriptor.getHandler();
                // Set the version of the locker to the data object's version.
                locker.setVersion((Long) fieldHandler.getValue(proposedObject
                        .getEntity()));
            }
        }

        // set the timeStamp of locker to the one of data object
        if (proposedObject.getEntity() instanceof TimeStampable) {
            locker.setVersion(((TimeStampable) proposedObject.getEntity())
                    .jdoGetTimeStamp());
        }
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

        if (_persistence == null) {
            throw new PersistenceException("non persistence capable: "
                    + oid.getName());
        }

        ProposedEntity entity = new ProposedEntity();
        entity.initializeFields(_fhs.length);
        Identity ids = oid.getIdentity();

        long timeStamp = System.currentTimeMillis();

        // Check for version field.
        if (_clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(_clsDesc);
            String versionField = jdoNature.getVersionField();

            // Check if version field was set and has content.
            // TODO: that check should be moved to e.g. ClassDescriptorJDONature
            if (versionField != null && versionField.length() > 0) {
                // Find field descriptor for version field.
                FieldDescriptor versionFieldDescriptor =
                        jdoNature.getField(versionField);
                FieldHandler fieldHandler = versionFieldDescriptor.getHandler();
                fieldHandler.setValue(object, timeStamp);
            }
        }
        
        // set the new timeStamp into the data object
        if (object instanceof TimeStampable) {
            ((TimeStampable) object).jdoSetTimeStamp(timeStamp);
        }

        // copy the object to cache should make a new field now,
        for (int i = 0; i < _fhs.length; i++) {
            FieldPersistenceType fieldPersistenceType = _fhs[i].getFieldPertsistenceType();

            switch (fieldPersistenceType) {
            case PRIMITIVE:
            case PERSISTANCECAPABLE:
            case SERIALIZABLE:
            case ONE_TO_MANY:
            case MANY_TO_MANY:
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
        locker.setObject(tx, entity.getFields(), timeStamp);
        oid.setDbLock(true);

        // set the identity into the object
        setIdentity(tx, object, createdId);

        // after successful creation, add the entry in the relation table for
        // all many-to-many relationship
        //ASE: This is the source of problem with M:N relations. As we see at
        //     this point, for every persisted object in any M:N relation, a
        //	   new entry to the relation table is created. But this happens not
        //	   only when both related objects are persisted but also after
        //	   persistence of the first. But that cannot work!
        // 	   A solution would be to store a collection of relations which have
        //	   to persisted in the relation table after all objects are persisted 
        //	   independently!
        
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
     * @throws PersistenceException If identity is missing  for storage
     * or the identity is modified
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

        long timeStamp = System.currentTimeMillis();

        // Set the new timestamp into version field.
        if (_clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(_clsDesc);
            String versionField = jdoNature.getVersionField();

            // Check if version field was set and has content.
            // TODO: that check should be moved to e.g. ClassDescriptorJDONature
            if (versionField != null && versionField.length() > 0) {
                // Find field descriptor for version field.
                FieldDescriptor versionFieldDescriptor =
                        jdoNature.getField(versionField);
                FieldHandler fieldHandler = versionFieldDescriptor.getHandler();
                fieldHandler.setValue(object, timeStamp);
            }
        }

        // set the new timeStamp into the data object
        if (object instanceof TimeStampable) {
            ((TimeStampable) object).jdoSetTimeStamp(timeStamp);
        }

        ProposedEntity newentity = new ProposedEntity();
        newentity.initializeFields(_fhs.length);
        for (int i = 0; i < _fhs.length; i++) {
            newentity.setField(_resolvers[i].store(tx, object, oldentity.getField(i)), i);
        }
        
        // Gets connection reference
        Connection conn = tx.getConnection(oid.getMolder().getLockEngine());

        // Current molder is leaf of extends hierarchy and therefore extending table is null
        String extendingTableName = null;

        // Start with current molder
        ClassMolder molder = this;
        
        // Loop over all extended molders and store all values of extends hierarchy
        while (molder != null) {
            // Gets name of current table
            String tableName = new ClassDescriptorJDONature(
                    molder.getClassDescriptor()).getTableName();
            
            // Only need to persist values if current table name is different than extending one
            if (!tableName.equals(extendingTableName)) {
                molder._persistence.store(conn, oid.getIdentity(), newentity, oldentity);
            }
         
            extendingTableName = tableName;
            molder = molder._extends;
        }        
    }

    /**
     * Update the object which loaded or created in the other transaction to the
     * persistent storage.
     * 
     * @param tx
     *            Transaction in action
     * @param oid
     *            the object identity of the stored object
     * @param locker
     *            the dirty check cache of the object
     * @param object
     *            the object to be stored
     * @return boolean true if the updating object should be created
     */
    public boolean update(final TransactionContext tx, final OID oid,
            final DepositBox locker, final Object object,
            final AccessMode suggestedAccessMode) throws PersistenceException {

        AccessMode accessMode = getAccessMode(suggestedAccessMode);

        Object[] fields = locker.getObject(tx);

        boolean timeStampable = false;
        long objectTimestamp = 1;
        
        if (object instanceof TimeStampable) {
            timeStampable = true;
            objectTimestamp = ((TimeStampable) object).jdoGetTimeStamp();
        }
        
        ClassDescriptorJDONature jdoNature = null;

        // Check for version field.
        if (_clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            jdoNature = new ClassDescriptorJDONature(_clsDesc);
            String versionField = jdoNature.getVersionField();
            // TODO: that check should be moved to e.g. ClassDescriptorJDONature
            if (versionField != null && versionField.length() > 0) {
                objectTimestamp =  getObjectVersion(versionField, jdoNature, object);
                timeStampable = true;
            }
        }
        
        if ((!isDependent()) && (!timeStampable)) {
            throw new IllegalArgumentException(
                    "A master object that involves in a long transaction must be a TimeStampable!");
        }

        long lockTimestamp = locker.getVersion();

        if ((objectTimestamp > 0) && (oid.getIdentity() != null)) {
            // valid range of timestamp

            if ((timeStampable)
                    && (lockTimestamp == TimeStampable.NO_TIMESTAMP)) {
                throw new PersistenceException(Messages.format(
                        "persist.objectNotInCache", _name, oid.getIdentity()));
            }

            if (timeStampable && objectTimestamp != lockTimestamp) {
                throw new ObjectModifiedException("Timestamp mismatched!");
            }

            if (!timeStampable && isDependent() && (fields == null)) {
                // allow a dependent object not implements timeStampable
                fields = new Object[_fhs.length];
                Connection conn =
                        tx.getConnection(oid.getMolder().getLockEngine());

                ProposedEntity proposedObject = new ProposedEntity(this);
                proposedObject.setProposedEntityClass(object.getClass());
                proposedObject.setEntity(object);
                proposedObject.setFields(fields);
                _persistence.load(conn, proposedObject, oid.getIdentity(),
                        accessMode);
                fields = proposedObject.getFields();

                oid.setDbLock(accessMode == AccessMode.DbLocked);
                locker.setObject(tx, proposedObject.getFields(), System
                        .currentTimeMillis());
            }

            // load the original field into the transaction. so, store will
            // have something to compare later.
            try {
                for (int i = 0; i < _fhs.length; i++) {
                    _resolvers[i]
                            .update(tx, oid, object, accessMode, fields[i]);
                }
            } catch (ObjectNotFoundException e) {
                _log.warn(e.getMessage(), e);
                throw new ObjectModifiedException(
                        "dependent object deleted concurrently");
            }
            return false;
        } else if ((objectTimestamp == TimeStampable.NO_TIMESTAMP)
                || (objectTimestamp == 1)) {
            // work almost like create, except update the sub field instead of
            // create
            // iterate all the fields and mark all the dependent object.
            boolean updateCache = false;

            for (int i = 0; i < _fhs.length; i++) {
                updateCache |=
                        _resolvers[i].updateWhenNoTimestampSet(tx, oid, object,
                                suggestedAccessMode);
            }

            tx.markModified(object, false, updateCache);
            return true;
        } else {
            if (_log.isWarnEnabled()) {
                _log.warn("object: " + object + " timestamp: "
                        + objectTimestamp + " lockertimestamp: "
                        + lockTimestamp);
            }
            throw new ObjectModifiedException(
                    "Invalid object timestamp detected.");
        }
    }

    /**
     * Acquires the version of the specified object by accessing the given
     * field.
     * 
     * @param versionField
     *            the version field.
     * @param jdoNature
     *            the {@link ClassDescriptorJDONature}.
     * @param object
     *            the object.
     * @return the object's version.
     */
    @SuppressWarnings("unchecked")
    private Long getObjectVersion(final String versionField,
            final ClassDescriptorJDONature jdoNature, final Object object) {
        FieldDescriptor versionFieldDescriptor =
                jdoNature.getField(versionField);
        return (Long) versionFieldDescriptor.getHandler().getValue(object);
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

        if (oid.getIdentity() == null) {
            throw new IllegalStateException(
                    Messages.format("persist.missingIdentityForCacheUpdate", _name));
        }

        fields = new Object[_fhs.length];

        for (int i = 0; i < _fhs.length; i++) {
            FieldPersistenceType fieldPersistenceType = _fhs[i].getFieldPertsistenceType();
            switch (fieldPersistenceType) {
            case PRIMITIVE:
            case SERIALIZABLE:
            case PERSISTANCECAPABLE:
            case ONE_TO_MANY:
            case MANY_TO_MANY:
                fields[i] = _resolvers[i].updateCache(tx, oid, object);
                break;
            default:
                throw new IllegalArgumentException("Field type invalid!");
            }
        }
        
        Long objectVersion = null;
        
        // Check for version field.
        if (_clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            ClassDescriptorJDONature jdoNature = new ClassDescriptorJDONature(_clsDesc);
            String versionField = jdoNature.getVersionField();
            // TODO: that check should be moved to e.g. ClassDescriptorJDONature
            if (versionField != null && versionField.length() > 0) {
                objectVersion = getObjectVersion(versionField, jdoNature, object);
            }
        }
        
        // store new field values in cache
        if (object instanceof TimeStampable) {
            locker.setObject(tx, fields, ((TimeStampable) object).jdoGetTimeStamp());
        } else if (objectVersion != null) {
            locker.setObject(tx, fields, objectVersion);
        } else {
            locker.setObject(tx, fields, System.currentTimeMillis());
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
        Connection conn = tx.getConnection(oid.getMolder().getLockEngine());
        Identity ids = oid.getIdentity();

        for (int i = 0; i < _fhs.length; i++) {
            if (_fhs[i].isManyToMany()) {
                _fhs[i].getRelationLoader().deleteRelation(conn, ids);
            }
        }

        // Must delete record of extend path from extending to root class
        // In addition we remember the extend path to delete everything off the path ourself
        Vector<ClassMolder> extendPath = new Vector<ClassMolder>();
        ClassMolder molder = this;
        while (molder != null) {
            molder._persistence.delete(conn, ids);
            extendPath.add(molder);
            molder = molder._extends;
        }
        
        ClassMolder base = _depends;
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

        Object[] fields = locker.getObject(tx);

        for (int i = 0; i < _fhs.length; i++) {
            FieldPersistenceType fieldType = _fhs[i].getFieldPertsistenceType();
            switch (fieldType) {
            case PRIMITIVE:
            case SERIALIZABLE:
            case PERSISTANCECAPABLE:
            case ONE_TO_MANY:
            case MANY_TO_MANY:
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
        Class<?> aClass = null;
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
     * Get the base class of this ClassMolder given a ClassLoader.
     * 
     * @param loader the classloader.
     * @return the <code>Class</code> instance.
     */
    public Class<?> getJavaClass(final ClassLoader loader) {
        Class<?> result = null;
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
     * Mutator method to add a extent ClassMolder.
     */
    void addExtendent(final ClassMolder ext) {
        if (_extendent == null) {
            _extendent = new Vector<ClassMolder>();
        }
        _extendent.add(ext);
    }

    /**
     * Mutator method to add a dependent ClassMolder.
     */
    void addDependent(final ClassMolder dep) {
        if (_dependent == null) {
            _dependent = new Vector<ClassMolder>();
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
                _resolvers[i] = ResolverFactory.createRelationResolver (_fhs[i], this, i);
            }
            
            _resolversHaveBeenReset = true;
        }
    }

    /**
     * Returns the actual (OQL) statement for the specified named query.
     * 
     * @param name Named query name.
     * @return The actual (OQL) statement.
     */
    public String getNamedQuery(final String name) {
        return new ClassDescriptorJDONature(_clsDesc).getNamedQueries().get(name);
    }

    /**
     * Returns the actual (SQL) statement for the specified named native query.
     * 
     * @param name Named query name.
     * @return The actual (SQL) statement.
     */
    public NamedNativeQuery getNamedNativeQuery(final String name) {
        return new ClassDescriptorJDONature(_clsDesc).getNamedNativeQueries().get(name);
    }
}

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


import java.math.BigDecimal;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OptionalDataException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.exolab.castor.jdo.Persistent;

import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.JDOCallback;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.jdo.engine.JDOClassDescriptor;
import org.exolab.castor.jdo.engine.JDOFieldDescriptor;
import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.util.Messages;
import java.sql.Connection;


/**
 * ClassMolder is a binder for one type of data object and its
 * {@link Persistence}. For example, when ClassMolder is asked to load
 * an object, it acquires values from {@link Persistence} and binds it
 * into an target object. It also resolves relations via
 * {@link TransactionContext} and bind related object into an target object.
 * <p>
 * Beside loading, ClassMolder is also repsonsible for storing, removing,
 * creating an object to and from a persistence storage, as well as
 * reverting an object to previous state.
 * <p>
 * Each instance of ClassMolder deals with exactly one persistable type,
 * interacts with one instance of Persistent and belongs to one
 * {@link LockEngine}.
 * <p>
 *
 *
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 */

public class ClassMolder {

    /**
     * The fully qualified name of the java data object class which this ClassMolder
     * corresponds to. We call it base class.
     */
    private String _name;

    /**
     * <tt>FieldMolder</tt>s of the identity of the base class
     */
    private FieldMolder[] _ids;

    /**
     * <tt>FieldMolder</tt>s of the fields of the base class
     */
    private FieldMolder[] _fhs;

    /**
     * <tt>ClassMolder</tt> of the java data object class's ClassMolder which
     * the base class extends.
     */
    private ClassMolder _extends;

    /**
     * <tt>ClassMolder</tt> of the java data object class's ClassMolder which
     * the base class depends on. <tt>null</tt> if it class is an indenpendent
     * class.
     */
    private ClassMolder _depends;

    /**
     * A Vector of <tt>ClassMolder</tt>s for all the direct dependent class of the
     * base class.
     */
    private Vector _dependent;

    /**
     * A Vector of <tt>ClassMolder</tt>s for all the direct extending class of the
     * base class.
     */
    private Vector _extendent;

    /**
     * Default accessMode of the base class.
     */
    private AccessMode _accessMode;

    /**
     * The Persistent of the base class.
     */
    private Persistence _persistence;

    /**
     * The LockEngine which this instace of ClassMolder belong to.
     */
    private LockEngine _engine;

    /**
     * The CallbackInterceptor for the base class.
     */
    private CallbackInterceptor _callback;

    /**
     * The LRU mechanism to be used for caching freed instance of the base class.
     */
    private int _cachetype;

    /**
     * The LRU parameter(or capcity) to be used for caching freed instance of the
     * base class.
     */
    private int _cacheparam;

    /**
     * Is a key kenerator used for the base class?
     */
    private boolean _isKeyGenUsed;

    /**
     * True if org.exolab.castor.debug="true"
     */
    private boolean _debug;


    /**
     * True if the representing class implements the interface TimeStampable
     */
    private boolean _timeStampable;

    /**
     * Constructor
     *
     * @param ds      is the helper class for resolving depends and extends relationship
     *                among all the ClassMolder in the same LockEngine.
     * @param loader  is the mapping loader
     * @param classDescriptor   the classDescriptor for the base class
     * @param persist the Persistent for the base class
     */
    ClassMolder( DatingService ds, MappingLoader loader, LockEngine lock,
            ClassDescriptor clsDesc, Persistence persist )
            throws ClassNotFoundException, MappingException {

        _debug = Boolean.getBoolean("org.exolab.castor.debug");

        ClassMapping clsMap = ((ClassDescriptorImpl) clsDesc).getMapping();

        _engine = lock;

        _persistence = persist;

        _name = clsMap.getName();

        _accessMode = AccessMode.getAccessMode( clsMap.getAccess().toString() );

        _timeStampable = TimeStampable.class.isAssignableFrom( clsDesc.getJavaClass() );

        ds.register( _name, this );

        ClassMapping dep = (ClassMapping) clsMap.getDepends();
        ClassMapping ext = (ClassMapping) clsMap.getExtends();

        //if ( dep != null && ext != null )
        //    throw new MappingException("A JDO cannot both extends and depends on other objects");

        if ( dep != null ) {
            ds.pairDepends( this, dep.getName() );
        }

        if ( ext != null ) {
            ds.pairExtends( this, ext.getName() );
        }

        if ( clsDesc instanceof JDOClassDescriptor ) {
            if ( ((JDOClassDescriptor) clsDesc).getCacheType() != null ) {
                _cachetype = LRU.mapType( ((JDOClassDescriptor) clsDesc).getCacheType() );
                _cacheparam = ((JDOClassDescriptor) clsDesc).getCacheParam();
            }
            _isKeyGenUsed = ( ( (JDOClassDescriptor) clsDesc ).getKeyGeneratorDescriptor() != null );
        }

        // construct <tt>FieldModler</tt>s for each of the identity fields of
        // the base class.
        FieldMapping[] fmId = getIdFields( clsMap );
        _ids = new FieldMolder[fmId.length];
        for ( int i=0; i<_ids.length; i++ ) {
            _ids[i] = new FieldMolder( ds, this, fmId[i] );
        }

        // construct <tt>FieldModlers</tt>s for each of the fields of the base
        // class.
        FieldMapping[] fmFields = getFullFields( clsMap );
        _fhs = new FieldMolder[fmFields.length];
        for ( int i=0; i<_fhs.length; i++ ) {
            if ( fmFields[i].getSql() != null && fmFields[i].getSql().getManyTable() != null ) {
                // the fields is not primitive
                String manyTable = null;
                String[] idSQL = null;
                int[] idType = null;
                String[] relatedIdSQL = null;
                int[] relatedIdType = null;
                TypeConvertor[] idConvertTo = null;
                TypeConvertor[] idConvertFrom = null;
                String[] idConvertParam = null;
                TypeConvertor[] relatedIdConvertTo = null;
                TypeConvertor[] relatedIdConvertFrom = null;
                String[] relatedIdConvertParam = null;

                manyTable = fmFields[i].getSql().getManyTable();

                idSQL = new String[fmId.length];
                idType = new int[fmId.length];
                idConvertFrom = new TypeConvertor[fmId.length];
                idConvertTo = new TypeConvertor[fmId.length];
                idConvertParam = new String[fmId.length];
                FieldDescriptor[] fd = ((ClassDescriptorImpl)clsDesc).getIdentities();
                for ( int j=0; j < fmId.length; j++ ) {
                    idSQL[j] = fmId[j].getSql().getName()[0];

                    if ( fd[j] instanceof JDOFieldDescriptor ) {
                        int[] type = ((JDOFieldDescriptor)fd[j]).getSQLType();
                        idType[j] = type==null? 0: type[0];
                        FieldHandlerImpl fh = (FieldHandlerImpl)fd[j].getHandler();
                        idConvertTo[j] = fh.getConvertTo();
                        idConvertFrom[j] = fh.getConvertFrom();
                        idConvertParam[j] = fh.getConvertParam();
                    } else {
                        throw new MappingException("Identity type must contains sql information: "+ _name );
                    }
                }

                relatedIdSQL = null;
                String relatedType = fmFields[i].getType();
                ClassDescriptor relDesc = loader.getDescriptor( ds.resolve( relatedType ) );
                if ( relDesc instanceof JDOClassDescriptor ) {
                    FieldDescriptor[] relatedIds = ((JDOClassDescriptor)relDesc).getIdentities();
                    relatedIdSQL = new String[relatedIds.length];
                    relatedIdType = new int[relatedIds.length];
                    relatedIdConvertTo = new TypeConvertor[relatedIds.length];
                    relatedIdConvertFrom = new TypeConvertor[relatedIds.length];
                    relatedIdConvertParam = new String[relatedIds.length];
                    for ( int j=0; j < relatedIdSQL.length; j++ ) {
                        if ( relatedIds[j] instanceof JDOFieldDescriptor ) {
                            String[] tempId = ((JDOFieldDescriptor)relatedIds[j]).getSQLName();
                            relatedIdSQL[j] = tempId==null? null: tempId[0];
                            int[] tempType = ((JDOFieldDescriptor)relatedIds[j]).getSQLType();
                            relatedIdType[j] = tempType==null? 0: tempType[0];
                            FieldHandlerImpl fh = (FieldHandlerImpl)relatedIds[j].getHandler();
                            relatedIdConvertTo[j] = fh.getConvertTo();
                            relatedIdConvertFrom[j] = fh.getConvertFrom();
                            relatedIdConvertParam[j] = fh.getConvertParam();
                        } else {
                            throw new MappingException("Field type is not persistence-capable: "+ relatedIds[j].getFieldName() );
                        }
                    }
                }

                // if many-key exist, idSQL is overridden
                String[] manyKey = fmFields[i].getSql().getManyKey();
                if ( manyKey != null && manyKey.length != 0 ) {
                    if ( manyKey.length != idSQL.length )
                        throw new MappingException("The number of many-keys doesn't match referred object: "+clsDesc.getJavaClass().getName());
                    idSQL = manyKey;
                }

                // if name="" exist, relatedIdSQL is overridden
                String[] manyName = fmFields[i].getSql().getName();
                if ( manyName != null && manyName.length != 0 ) {
                    if ( manyName.length != relatedIdSQL.length )
                        throw new MappingException("The number of many-keys doesn't match referred object: "+relDesc.getJavaClass().getName());
                    relatedIdSQL = manyName;
                }

                _fhs[i] = new FieldMolder( ds, this, fmFields[i], manyTable,
                        idSQL, idType, idConvertTo, idConvertFrom, idConvertParam,
                        relatedIdSQL, relatedIdType, relatedIdConvertTo, relatedIdConvertFrom, relatedIdConvertParam );
            } else {
                _fhs[i] = new FieldMolder( ds, this, fmFields[i] );
            }
        }

        // ssa, FIXME : Are the two statements equivalents ?
		//        if ( Persistent.class.isAssignableFrom( _base ) )
        if ( Persistent.class.isAssignableFrom( ds.resolve(_name) ) )
            _callback = new JDOCallback();
    }

    /*
     * Get the all the id fields of a class
     * If the class, C, is a dependent class, then
     * the depended class', D, id fields will be
     * appended at the back and returned.
     * If the class is an extended class, the id
     * fields of the extended class will be returned.
     */
    private FieldMapping[] getIdFields( ClassMapping clsMap )
            throws MappingException {
        ClassMapping base;
        FieldMapping[] fmDepended;
        FieldMapping[] fmResult;
        FieldMapping[] fmBase;
        FieldMapping[] fmIds;
        String[] identities;

        // start with the extended class
        base = clsMap;
        while ( base.getExtends() != null ) {
            base = (ClassMapping) base.getExtends();
        }
        fmDepended = null;

        identities = base.getIdentity();

        if ( identities == null || identities.length == 0 )
            throw new MappingException("Identity is null!");


        fmIds = new FieldMapping[identities.length];
        fmBase = base.getFieldMapping();
        for ( int i=0,j=0; i<fmBase.length; i++ ) {
            for ( int k=0; k<identities.length; k++ ) {
                if ( fmBase[i].getName().equals( identities[k] ) ) {
                    fmIds[k] = fmBase[i];
                    break;
                }
            }
        }
        if ( fmDepended == null )
            return fmIds;

        // join depend ids and class id
        fmResult = new FieldMapping[fmDepended.length + identities.length];
        System.arraycopy( fmIds, 0, fmResult, 0, fmIds.length );
        System.arraycopy( fmDepended, 0 , fmResult, fmIds.length, fmDepended.length );
        return fmIds;
    }
    /*
     * Get all the field mapping, including all the field
     * in extended class, but id fields
     */
    private FieldMapping[] getFullFields( ClassMapping clsMap )
            throws MappingException {
        FieldMapping[] extendFields;
        FieldMapping[] thisFields;
        FieldMapping[] fields = null;
        String[] identities;
        boolean idfield;
        ClassMapping extend = (ClassMapping) clsMap.getExtends();
        ClassMapping origin;
        ArrayList fieldList;

        if ( extend != null ) {
            origin = extend;
            while (origin.getExtends() != null) {
                origin = (ClassMapping) origin.getExtends();
            }
            identities = origin.getIdentity();
            extendFields = getFullFields( extend );
            thisFields = clsMap.getFieldMapping();

            fieldList = new ArrayList(extendFields.length + thisFields.length);
            for (int i = 0; i < extendFields.length; i++) {
                fieldList.add(extendFields[i]);
            }
            for ( int i=0; i<thisFields.length; i++ ) {
                for ( int k=0; k<identities.length; k++ ) {
                    if ( ! thisFields[i].getName().equals( identities[k] ) ) {
                        fieldList.add(thisFields[i]);
                        break;
                    }
                }
            }
            fields = new FieldMapping[fieldList.size()];
            fieldList.toArray(fields);
        } else {
            identities = clsMap.getIdentity();
            if ( identities == null || identities.length == 0 )
                throw new MappingException("Identity is null!");

            // return array of fieldmapping without the id field
            thisFields = clsMap.getFieldMapping();
            fields = new FieldMapping[thisFields.length-identities.length];

            for ( int i=0,j=0; i<thisFields.length; i++ ) {
                idfield = false;
                IDSEARCH:
                for ( int k=0; k<identities.length; k++ ) {
                    if ( thisFields[i].getName().equals( identities[k] ) ) {
                        idfield = true;
                        break IDSEARCH;
                    }
                }
                if ( !idfield ) {
                    fields[j] = thisFields[i];
                    j++;
                }
            }

        }
        return fields;
    }

    /**
     * Resolve and construct all the <tt>ClassMolder</tt>s given a MappingLoader.
     *
     * @param   loader    MappingLoader for the LockEngine
     * @param   lock      LockEngine for all the ClassMolder
     * @param   factory   factory class for getting Persistent of the ClassMolder
     *
     * @return  Vector of all of the <tt>ClassMolder</tt>s from a MappingLoader
     */
    public static Vector resolve( MappingLoader loader, LockEngine lock,
            PersistenceFactory factory, LogInterceptor logInterceptor )
            throws MappingException, ClassNotFoundException {

        Vector result = new Vector();
        Enumeration enum;
        ClassMolder mold;
        ClassMapping map;
        Persistence persist;
        ClassDescriptor desc;

        DatingService ds = new DatingService( loader.getClassLoader() );

        enum = loader.listJavaClasses();
        while ( enum.hasMoreElements() ) {
            desc = (ClassDescriptor) loader.getDescriptor((Class)enum.nextElement());
            persist = factory.getPersistence( desc, logInterceptor );
            mold = new ClassMolder( ds, loader, lock, desc, persist );
            result.add( mold );
        }
        ds.close();
        return result;
    }

    /**
     * Remove the reference of a related object from an object of.
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
     * try to remove all the occurence.
     *
     * @param tx      the TransactionContext of the transaction in action
     * @param object  the target object of the base type of this ClassMolder
     * @param relatedMolder  the ClassMolder of the related object to be
     *                       removed from the object
     * @param related        the object to be removed
     */
    private boolean removeRelation( TransactionContext tx, Object object,
            ClassMolder relatedMolder, Object relatedObject )  {

        boolean removed = false;
        boolean updateCache = false;
        boolean updatePersist = false;
        ClassMolder relatedBaseMolder = null;
        for ( int i=0; i < _fhs.length; i++ ) {
            int fieldType = _fhs[i].getFieldType();
            ClassMolder fieldClassMolder;
            switch (fieldType) {
            case FieldMolder.PERSISTANCECAPABLE:
                // de-reference the object
                fieldClassMolder = _fhs[i].getFieldClassMolder();

                relatedBaseMolder = relatedMolder;
                while ( fieldClassMolder != relatedBaseMolder && relatedBaseMolder != null ) {
                    relatedBaseMolder = relatedBaseMolder._extends;
                }
                if ( fieldClassMolder == relatedBaseMolder ) {
                    Object related = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( related == relatedObject ) {
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                        updateCache = true;
                        updatePersist = true;
                        removed = true;
                    }
                    // |
                }
                break;
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                // remove the object from the collection
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                relatedBaseMolder = relatedMolder;
                while ( fieldClassMolder != relatedBaseMolder && relatedBaseMolder != null ) {
                    relatedBaseMolder = relatedBaseMolder._extends;
                }
                if ( fieldClassMolder == relatedBaseMolder ) {
                    // same action to be taken for lazy and non lazy collection
                    boolean changed = false;
                    Object related = _fhs[i].getValue( object, tx.getClassLoader() );
                    Iterator itor = getIterator( related );
                    while ( itor.hasNext() ) {
                        Object o = itor.next();
                        if ( o == relatedObject ) {
                            changed = true;
                            itor.remove();
                        }
                    }
                    if ( changed ) {
                        updateCache = true;
                        updatePersist = false;
                        removed = true;
                    }
                }
                break;
            }
        }
        tx.markModified( object, updatePersist, updateCache );
        return removed;
    }

    /**
     * Load an object with specified identity from the persistent storage.
     *
     * @param tx   the TransactionContext in action
     * @param oid  the object identity of the desired object
     * @param locker   the {@link DepositBox} of the object which is used to
     *                 store the dirty checking cache of the object.
     * @param accessMode  the acessMode for the object
     * @return the object stamp for the object in the persistent storage
     */
    public Object load( TransactionContext tx, OID oid, DepositBox locker,
            Object object, AccessMode suggestedAccessMode )
            throws ObjectNotFoundException, PersistenceException {

        Connection conn;
        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] fields;
        Object ids;
        Object stamp = null;
        Object temp;
        int fieldType;
        AccessMode accessMode = getAccessMode( suggestedAccessMode );

        if ( oid.getIdentity() == null )
            throw new PersistenceException("The identities of the object to be loaded is null");

        // load the fields from the persistent storage if the cache is empty
        // and the accessMode is readOnly.
        fields = (Object[]) locker.getObject( tx );
        if ( fields == null || accessMode == AccessMode.DbLocked ) {
            fields = new Object[_fhs.length];
            conn = (Connection)tx.getConnection(oid.getLockEngine());
            stamp = _persistence.load( conn, fields, oid.getIdentity(), accessMode );
            oid.setDbLock( accessMode == AccessMode.DbLocked );
            locker.setObject( tx, fields );
        }

        // set the timeStamp of the data object to locker's timestamp
        if ( object instanceof TimeStampable ) {
            ((TimeStampable)object).jdoSetTimeStamp( locker.getTimeStamp() );
        }

        // set the identities into the target object
        ids = oid.getIdentity();
        setIdentity( tx, object, ids );

        // iterates thur all the field of the object and bind all field.
        for ( int i = 0; i < _fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                // simply set the corresponding Persistent field value into the object
                temp = fields[i];
                if ( temp != null )
                    _fhs[i].setValue( object, temp, tx.getClassLoader() );
                else
                    _fhs[i].setValue( object, null, tx.getClassLoader() );
                break;

            case FieldMolder.SERIALIZABLE:
                // deserialize byte[] into java object
                try {
                    byte[] bytes = (byte[]) fields[i];
                    if ( bytes != null ) {
                        // The following code can be updated, after Blob-->InputStream
                        // to enhance performance.
                        ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
                        ObjectInputStream os = new ObjectInputStream( bis );
                        Object o = os.readObject();
                        _fhs[i].setValue( object, o, tx.getClassLoader() );
                    } else {
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                    }
                } catch ( OptionalDataException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                } catch ( ClassNotFoundException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                } catch ( IOException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                }
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                // field is not primitive type. Related object will be loaded
                // thru the transaction in action if needed.

                // lazy loading for object (hollow object) is not support in
                // this version. Warns user if lazy loading is specified.
                if ( _fhs[i].isLazy() )
                    System.err.println( "Warning: Lazy loading of object is not yet support!" );

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();

                if ( fields[i] != null ) {
                    // use the corresponding Persistent fields as the identity,
                    // and we ask transactionContext in action to load it.
                    try {
                        temp = tx.load( fieldEngine, fieldClassMolder, fields[i], null, suggestedAccessMode );
                    } catch (Exception ex) {
                        temp = null;
                    }
                    _fhs[i].setValue( object, temp, tx.getClassLoader() );
                } else {
                    _fhs[i].setValue( object, null, tx.getClassLoader() );
                }
                break;
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                // field is one-to-many and many-to-many type. All the related
                // object will be loaded and put in a Collection. And, the
                // collection will be set as the field.
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();

                if ( !_fhs[i].isLazy() ) {
                    // lazy loading is not specified, load all objects into
                    // the collection and set the Collection as the data object
                    // field.

                    CollectionProxy cp = CollectionProxy.create( _fhs[i], object, tx.getClassLoader() );
                    ArrayList v = (ArrayList)fields[i];
                    if ( v != null ) {
                        for ( int j=0,l=v.size(); j<l; j++ ) {
                            cp.add( v.get(j), tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null, suggestedAccessMode ) );
                        }
                        cp.close();
                        //_fhs[i].setValue( object, cp.getCollection() );
                    } else {
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                    }
                } else {
                    // lazy loading is specified. Related object will not be loaded.
                    // A lazy collection with all the identity of the related object
                    // will constructed and set as the data object's field.
                    ArrayList list = (ArrayList) fields[i];
                    RelationCollection relcol = new RelationCollection( tx, oid, fieldEngine, fieldClassMolder, accessMode, list );
                    _fhs[i].setValue( object, relcol, tx.getClassLoader() );
                }
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
    public Object create( TransactionContext tx, OID oid, DepositBox locker, Object object )
            throws DuplicateIdentityException, PersistenceException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        ArrayList fids;
        Object[] fields;
        Object createdId;
        Object ids;
        Object fid;
        Object o;

        int fieldType;

        if ( _persistence == null )
            throw new PersistenceException("non persistence capable: "+oid.getName());

        // optimization note: because getObject is an expensive operation,
        // if this method is divided into 3 phase, the performance will be
        // enhanced.
        // 1/ getObject into array
        // 2/ put things in cache
        // 3/ deal with relations

        fields = new Object[_fhs.length];
        ids = oid.getIdentity();

        // copy the object to cache should make a new field now,
        for ( int i=0; i<_fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();

            switch (fieldType) {
            case FieldMolder.PRIMITIVE: // primitive includes int, float, Date, Time etc
                fields[i] = _fhs[i].getValue( object, tx.getClassLoader() );
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( o != null ) {
                    if ( !_fhs[i].isDependent() ) {
                        fid = fieldClassMolder.getIdentity( tx, o );
                        if ( fid != null ) {
                            fields[i] = fid;
                        }
                    } // dependent object will be created at preStore state
                }
                break;

            case FieldMolder.SERIALIZABLE:
                try {
                    Object dependent = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( dependent != null ) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream( bos );
                        os.writeObject( dependent );
                        fields[i] = bos.toByteArray();
                    } else {
                        fields[i] = null;
                    }
                } catch ( IOException e ) {
                    throw new PersistenceException( "Error during serializing dependent object", e );
                }
                break;

            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( o != null && !_fhs[i].isDependent() ) {
                    fids = extractIdentityList( tx, fieldClassMolder, o );
                    fields[i] = fids;
                } // dependent objects will be created at preStore state
                break;

            case FieldMolder.MANY_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( o != null ) {
                    fids = extractIdentityList( tx, fieldClassMolder, o );
                    fields[i] = fids;
                }
                break;

            default:
                throw new IllegalArgumentException("Field type invalid!");
            }
        }

        // ask Persistent to create the object into the persistence storage
        createdId = _persistence.create( (Connection)tx.getConnection(oid.getLockEngine()),
                fields, ids );

        if ( createdId == null )
            throw new PersistenceException("Identity can't be created!");

        // if the creation succeed, we set the field into the dirty checking
        // cache.
        locker.setObject( tx, fields );
        oid.setDbLock( true );

        // set the identity into the object
        setIdentity( tx, object, createdId );

        boolean autoCreated = false;

        // iterate all the fields and create all the dependent object.
        for ( int i=0; i<_fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
            case FieldMolder.SERIALIZABLE:
                // nothing need to be done here for primitive
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                // create dependent object if exists
                autoCreated = false;
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( o != null ) {
                    if ( _fhs[i].isDependent() ) {
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
                        /*
                        if ( !tx.isPersistent( o ) ) {
                            tx.create( fieldEngine, fieldClassMolder, o, oid );
                        } else {}
                            // fail-fast principle: if the object depend on another object,
                           // throw exception
                         */
                        //if ( !tx.isDepended( oid, o ) )
                        //    throw new PersistenceException("Dependent object may not change its master. Object: "+o+" new master: "+oid);
                    } else if ( tx.isAutoStore() ) {
                        if ( !tx.isPersistent( o ) && !tx.isDeleted( o ) ) {
                            // related object should be created right the way, if autoStore
                            // is enabled, to obtain a database lock on the row. If both side
                            // uses keygenerator, the current object will be updated in the
                            // store state.
                            OID fieldOid = tx.create( fieldEngine, fieldClassMolder, o, null );
                            if ( _isKeyGenUsed ) {
                                fields[i] = fieldOid.getIdentity();
                                autoCreated = true;
                            }
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
                        }
                    }
                }
                break;

            case FieldMolder.ONE_TO_MANY:
                // create dependent objects if exists
                autoCreated = false;
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( o != null ) {
                    Iterator itor = getIterator( o );
                    while (itor.hasNext()) {
                        Object oo = itor.next();
                        if ( _fhs[i].isDependent() ) {
                            /*
                            if ( !tx.isPersistent( oo ) ) {
                                autoCreated = true;
                                tx.create( fieldEngine, fieldClassMolder, oo, oid );
                            } else
                                // fail-fast principle: if the object depend on another object,
                                // throw exception
                                if ( !tx.isDepended( oid, oo ) )
                                    throw new PersistenceException("Dependent object may not change its master");
                             */
                        } else if ( tx.isAutoStore() ) {
                            if ( !tx.isPersistent( oo ) && !tx.isDeleted( oo ) ) {
                                OID fieldOid = tx.create( fieldEngine, fieldClassMolder, oo, null );
                                if ( _isKeyGenUsed ) {
                                    ((ArrayList)fields[i]).add( fieldOid.getIdentity() );
                                    autoCreated = true;
                                }
                            }
                        }
                    }
                }
                break;

            case FieldMolder.MANY_TO_MANY:
                // create relation if the relation table
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( o != null ) {
                    Iterator itor = getIterator( o );
                    // many-to-many relation is never dependent relation
                    while (itor.hasNext()) {
                        Object oo = itor.next();
                        if ( tx.isPersistent( oo ) ) {
                            _fhs[i].getRelationLoader().createRelation(
                            (Connection)tx.getConnection(oid.getLockEngine()),
                            createdId, fieldClassMolder.getIdentity( tx, oo ) );
                        } else if ( tx.isAutoStore() ) {
                            if ( !tx.isPersistent( oo ) && !tx.isDeleted( oo ) ) {
                                OID fieldOid = tx.create( fieldEngine, fieldClassMolder, oo, null );
                                if ( _isKeyGenUsed ) {
                                    ((ArrayList)fields[i]).add( fieldOid.getIdentity() );
                                    autoCreated = true;
                                }
                            }
                        }
                    }
                }
                break;
            }
        }

        if ( autoCreated ) {
            // set the object into DespositBox again, if changes made
            locker.setObject( tx, fields );
        }

        // set the new timeStamp into the data object
        if ( object instanceof TimeStampable ) {
            ((TimeStampable)object).jdoSetTimeStamp( locker.getTimeStamp() );
        }

        if ( createdId != ids )
            return createdId;
        else
            return null;
    }

    private boolean isEquals( Collection c1, Collection c2 ) {

        if ( c1 == c2 )
            return true;
        if ( c1 == null || c2 == null )
            return false;
        if ( c1.containsAll( c2 ) && c2.containsAll( c1 ) )
            return true;
        return false;
    }

    /**
     * A utility method which compare object.
     */
    private static boolean isEquals( Object o1, Object o2 ) {
        if ( o1 == o2 )
            return true;
        if ( o1 == null || o2 == null )
            return false;
        if ( o1.equals( o2 ) )
            return true;
        // [oleg] is some special cases equals doesn't work properly
        if ( (o1 instanceof java.math.BigDecimal) && ((java.math.BigDecimal) o1).compareTo(o2) == 0) {
            return true;
        }
        if ((o1 instanceof java.sql.Timestamp) && (o2 instanceof java.sql.Timestamp)) {
            java.sql.Timestamp t1 = (java.sql.Timestamp) o1;
            java.sql.Timestamp t2 = (java.sql.Timestamp) o2;
            return (t1.getTime() == t2.getTime() && t1.getNanos() / 1000000 == t2.getNanos() / 1000000);
        }

        if((o1 instanceof byte[]) && (o2 instanceof byte[])) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if((o1 instanceof char[]) && (o2 instanceof char[])) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        return false;
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
    public boolean preStore( TransactionContext tx, OID oid, DepositBox locker, Object object, int timeout )
            throws PersistenceException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] newfields;
        Object[] fields;
        Object value;
        Iterator itor;
        ArrayList list;
        ArrayList orgFields;
        int fieldType;

        if ( oid.getIdentity() == null )
            throw new PersistenceException("The identity of the object to be stored is null");

        if ( !oid.getIdentity().equals( getIdentity( tx, object ) ) )
            throw new PersistenceException("Identity changes is not allowed!");

        fields = (Object[]) locker.getObject( tx );

        if ( fields == null )
            throw new PersistenceException("Object, "+oid+",  isn't loaded in the persistence storage!");

        newfields = new Object[_fhs.length];
        boolean updateCache = false;
        boolean updatePersist = false;

        // iterate thru all the data object fields for modification
        for ( int i=0; i<newfields.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value =  _fhs[i].getValue( object, tx.getClassLoader() );
                if ( !isEquals( fields[i], value ) ) {
                    if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                        updatePersist = true;
                    updateCache = true;
                }
                break;

            case FieldMolder.SERIALIZABLE:
                // deserialize byte[] into java object
                try {
                    byte[] bytes = (byte[]) fields[i];
                    Object fieldValue = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( fieldValue == null && bytes == null) {
                        // do nothing
                    } else if ( fieldValue == null || bytes == null ) {
                        // indicate store is needed
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;
                    } else { // both not null
                        // The following code can be updated, after Blob-->InputStream
                        // to enhance performance.
                        ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
                        ObjectInputStream os = new ObjectInputStream( bis );
                        Object dependent = os.readObject();
                        if ( !dependent.equals( fieldValue ) ) {
                            if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                                updatePersist = true;
                            updateCache = true;
                        }
                    }
                } catch ( OptionalDataException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                } catch ( ClassNotFoundException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                } catch ( IOException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                }
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                boolean canCreate = false;

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( value != null ) {
                    newfields[i] = fieldClassMolder.getIdentity( tx, value );
                    canCreate = !tx.isPersistent( value ) && !tx.isDeleted( value ) ;
                }

                // | yip: don't delete the following comment,
                //      until it proved working by time. :-P
                // <oleg> if a key generator is used, identity is null before creation,
                // so... I introduced "canCreate" that means that value != null and is not persistent
                // and changed "newFields != null" to "value != null"
                // </oleg>
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
                if ( isEquals( fields[i], newfields[i] ) ) {
                    if ( !_debug )
                        break;

                    if ( fields[i] == null )
                        break; // do the next field if both are null

                    if ( value != null && tx.isDeleted(value) ) {
                        System.err.println("Warning: deleted object found!");
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                        break;
                    }

                    if ( tx.isAutoStore() || _fhs[i].isDependent() )
                        if ( value != tx.fetch( fieldEngine, fieldClassMolder, fields[i], null ) )
                            throw new DuplicateIdentityException("");
                } else {
                    if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                        updatePersist = true;
                    updateCache = true;

                    if ( _fhs[i].isDependent() ) {
                        if ( fields[i] != null ) {
                            Object reldel = tx.fetch( fieldEngine, fieldClassMolder, fields[i], null );
                            if ( reldel != null )
                                tx.delete( reldel );
                        }

                        if ( canCreate )
                            tx.create( fieldEngine, fieldClassMolder, value, oid );

                    } else {
                        if ( fields[i] != null ) {
                            Object deref = tx.fetch( fieldEngine, fieldClassMolder, fields[i], null );
                            if ( deref != null )
                                fieldClassMolder.removeRelation( tx, deref, this, object );
                        }

                        if ( tx.isAutoStore() && canCreate )
                            tx.create( fieldEngine, fieldClassMolder, value, null );
                    }
                }
                break;

            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                orgFields = (ArrayList)fields[i];
                if ( ! (value instanceof Lazy) ) {
                    Collection removed = getRemovedIdsList( tx, orgFields, value, fieldClassMolder );
                    Iterator removedItor = removed.iterator();
                    if ( removedItor.hasNext() ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;
                    }
                    while ( removedItor.hasNext() ) {
                        Object removedId = removedItor.next();
                        Object reldel = tx.fetch( fieldEngine, fieldClassMolder, removedId, null );
                        if ( reldel != null ) {
                            if ( _fhs[i].isDependent() )
                                tx.delete( reldel );
                            else
                                fieldClassMolder.removeRelation( tx, reldel, this, object );
                        } else {
                            // should i notify user that the object does not exist?
                            // user can't delete dependent object himself. So, must
                            // error.
                        }
                    }

                    Collection added = getAddedValuesList( tx, orgFields, value, fieldClassMolder );
                    Iterator addedItor = added.iterator();
                    if ( addedItor.hasNext() ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;
                    }
                    while ( addedItor.hasNext() ) {
                        Object addedValue = addedItor.next();
                        if ( _fhs[i].isDependent() ) {
                            if ( !tx.isPersistent( addedValue ) ) {
                                tx.create( fieldEngine, fieldClassMolder, addedValue, oid );
                            } else {
                                // should i notify user that the object does not exist?
                                // user can't create dependent object himself. So, must be
                                // an error.
                            }
                        } else if ( tx.isAutoStore() ) {
                            if ( !tx.isPersistent( addedValue ) && !tx.isDeleted( value ) )
                                tx.create( fieldEngine, fieldClassMolder, addedValue, null );
                        }
                    }

                    // it would be good if we also compare the new field element with
                    // the element in the transaction, when debug is set true
                } else {
                    RelationCollection lazy = (RelationCollection) value;
                    ArrayList deleted = lazy.getDeleted();
                    if ( deleted != null ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;

                        //if ( _fhs[i].isDependent() ) {
                        itor = deleted.iterator();
                        while ( itor.hasNext() ) {
                            updateCache = true;
                            Object toBeDeleted = lazy.find( itor.next() );
                            if ( toBeDeleted != null && tx.isPersistent( toBeDeleted ) ) {
                                if ( _fhs[i].isDependent() )
                                    tx.delete( toBeDeleted );
                                else
                                    fieldClassMolder.removeRelation( tx, toBeDeleted, this, object );
                            } else {
                                // what to do if it happens?
                            }
                        }
                    }

                    ArrayList added = lazy.getAdded();
                    if ( added != null ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;

                        if ( _fhs[i].isDependent() ) {
                            itor = added.iterator();
                            while ( itor.hasNext() ) {
                                Object toBeAdded = lazy.find( itor.next() );
                                if ( toBeAdded != null ) {
                                    tx.create( fieldEngine, fieldClassMolder, toBeAdded, oid );
                                } else {
                                    // what to do if it happens?
                                }
                            }
                        } else if ( tx.isAutoStore() ) {
                            itor = added.iterator();
                            while ( itor.hasNext() ) {
                                Object toBeAdded = lazy.find( itor.next() );
                                if ( toBeAdded != null )
                                    if ( !tx.isPersistent( toBeAdded ) && !tx.isDeleted( toBeAdded ) )
                                        tx.create( fieldEngine, fieldClassMolder, toBeAdded, null );
                            }
                        }
                    }

                }
                break;
            case FieldMolder.MANY_TO_MANY:

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                orgFields = (ArrayList) fields[i];
                if ( ! (value instanceof Lazy) ) {
                    Collection removed = getRemovedIdsList( tx, orgFields, value, fieldClassMolder );
                    Iterator removedItor = removed.iterator();
                    if ( removedItor.hasNext() ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;
                    }
                    while ( removedItor.hasNext() ) {
                        Object id = removedItor.next();
                        // must be loaded thur transaction, so that the related object
                        // is properly locked and updated before we delete it.
                        if ( !tx.isDeletedByOID( new OID( fieldEngine, fieldClassMolder, id ) ) ) {
                            Object reldel = tx.load( fieldEngine, fieldClassMolder, id, null, null );
                            if ( reldel != null && tx.isPersistent( reldel ) ) {
                                tx.writeLock( reldel, tx.getLockTimeout() );

                                _fhs[i].getRelationLoader().deleteRelation(
                                (Connection)tx.getConnection(oid.getLockEngine()),
                                oid.getIdentity(), id );

                                fieldClassMolder.removeRelation( tx, reldel, this, object );
                            }
                        }
                    }

                    Collection added = getAddedValuesList( tx, orgFields, value, fieldClassMolder );
                    Iterator addedItor = added.iterator();
                    if ( addedItor.hasNext() ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;
                    }
                    while ( addedItor.hasNext() ) {
                        Object addedField = addedItor.next();
                        tx.markModified( addedField, false/*updatePersist*/, true/*updateCache*/ );

                        if ( tx.isPersistent( addedField ) ) {
                            _fhs[i].getRelationLoader().createRelation(
                            (Connection)tx.getConnection(oid.getLockEngine()),
                            oid.getIdentity(), fieldClassMolder.getIdentity( tx, addedField ) );
                        } else {
                            if ( tx.isAutoStore() )
                                if ( !tx.isDeleted( addedField ) )
                                    tx.create( fieldEngine, fieldClassMolder, addedField, null );
                        }
                    }

                } else {
                    RelationCollection lazy = (RelationCollection) value;
                    ArrayList deleted = lazy.getDeleted();
                    if ( deleted != null ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;

                        itor = deleted.iterator();
                        while ( itor.hasNext() ) {
                            updateCache = true;
                            Object deletedId = itor.next();
                            Object toBeDeleted = lazy.find( deletedId );
                            if ( toBeDeleted != null ) {
                                if ( tx.isPersistent(toBeDeleted) ) {
                                    tx.writeLock( toBeDeleted, 0 );

                                    _fhs[i].getRelationLoader().deleteRelation(
                                    (Connection)tx.getConnection(oid.getLockEngine()),
                                    oid.getIdentity(), deletedId );

                                    fieldClassMolder.removeRelation( tx, toBeDeleted, this, object );
                                }
                            } else {
                                // what to do if it happens?
                            }
                        }
                    }

                    ArrayList added = lazy.getAdded();
                    if ( added != null ) {
                        if ( _fhs[i].isStored() && _fhs[i].isCheckDirty() )
                            updatePersist = true;
                        updateCache = true;

                        itor = added.iterator();
                        while ( itor.hasNext() ) {
                            Object addedId = itor.next();
                            Object toBeAdded = lazy.find( addedId );
                            if ( toBeAdded != null ) {
                                if ( tx.isPersistent( toBeAdded ) ) {
                                    _fhs[i].getRelationLoader().createRelation(
                                    (Connection)tx.getConnection(oid.getLockEngine()),
                                    oid.getIdentity(), addedId );
                                } else {
                                    if ( tx.isAutoStore() )
                                        if ( !tx.isPersistent( toBeAdded ) && !tx.isDeleted( toBeAdded ) )
                                            tx.create( fieldEngine, fieldClassMolder, toBeAdded, null );
                                }
                            } else {
                                // what to do if it happens?
                            }
                        }
                    }
                }
                break;
            default:
            }
        }


        tx.markModified( object, updatePersist, updateCache );

        if ( updateCache || updatePersist )
            tx.writeLock( object, timeout );

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
    public void store( TransactionContext tx, OID oid, DepositBox locker, Object object )
            throws DuplicateIdentityException, PersistenceException,
            ObjectModifiedException, ObjectDeletedException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] newfields;
        Object[] fields;
        Object value;
        int fieldType;

        if ( oid.getIdentity() == null )
            throw new PersistenceException("The identities of the object to be stored is null");

        if ( !oid.getIdentity().equals( getIdentity( tx, object ) ) )
            throw new PersistenceException("Identities changes is not allowed!");

        fields = (Object[]) locker.getObject( tx );

        if ( fields == null )
            throw new PersistenceException("Object, "+oid+",  isn't loaded in the persistence storage!");

        newfields = new Object[_fhs.length];
        for ( int i=0; i<newfields.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                newfields[i] = _fhs[i].getValue( object, tx.getClassLoader() );
                break;
            case FieldMolder.SERIALIZABLE:
                try {
                    Object dependent = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( dependent != null ) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream( bos );
                        os.writeObject( dependent );
                        newfields[i] = bos.toByteArray();
                    } else {
                        newfields[i] = null;
                    }
                } catch ( IOException e ) {
                    throw new PersistenceException( "Error during serializing dependent object", e );
                }
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( value != null )
                    newfields[i] = fieldClassMolder.getIdentity( tx, value );
                break;
            case FieldMolder.ONE_TO_MANY:
                break;
            case FieldMolder.MANY_TO_MANY:
                break;
            default:
            }
        }
        Object stamp = _persistence.store( tx.getConnection(oid.getLockEngine()),
                newfields, oid.getIdentity(), fields, oid.getStamp() );
        oid.setStamp( stamp );
    }

    /**
     * It is assumed the returned collection will not be modified. Any modification
     * to the returned collection may or may not affect the original collection or map.
     */
    private Collection getAddedValuesList( TransactionContext tx, ArrayList orgIds, Object collection, ClassMolder ch ) {

        if ( collection == null )
            return new ArrayList(0);

        if ( collection instanceof Map ) {
            if ( orgIds == null || orgIds.size() == 0 ) {
                if ( collection == null )
                    return new ArrayList(0);
                else
                    return ((Map) collection).values();
            }

            Map newMap = (Map) collection;
            ArrayList added = new ArrayList( newMap.size() );
            Iterator newItor = newMap.entrySet().iterator();
            while ( newItor.hasNext() ) {
                Map.Entry newId = (Map.Entry) newItor.next();
                if ( !orgIds.contains( newId.getKey() ) )
                    added.add( newId.getValue() );
            }
            return added;
        }

        if ( collection instanceof Collection ) {
            if ( orgIds == null || orgIds.size() == 0 ) {
                if ( collection == null )
                    return new ArrayList(0);
                else
                    return (Collection) collection;
            }

            if ( collection == null )
                return new ArrayList(0);

            Collection newValues = (Collection) collection;
            ArrayList added = new ArrayList( newValues.size() );
            Iterator newItor = newValues.iterator();
            while ( newItor.hasNext() ) {
                Object newValue = newItor.next();
                Object newId = ch.getIdentity( tx, newValue );
                if ( newId == null || !orgIds.contains( newId ) )
                    added.add( newValue );
            }
            return added;
        }

        throw new IllegalArgumentException( "Collection type "+collection.getClass().getName()+" is not supported!" );
    }

    /**
     * It is assumed the returned collection will not be modified. Any modification
     * to the returned collection may or may not affect the original collection or map.
     */
    private Collection getRemovedIdsList( TransactionContext tx, ArrayList orgIds, Object collection, ClassMolder ch ) {

        if ( collection == null ) {
            if ( orgIds == null )
                return new ArrayList(0);
            else
                return orgIds;
        }

        if ( collection instanceof Map ) {
            if ( orgIds == null || orgIds.size() == 0 )
                return new ArrayList(0);

            Map newMap = (Map) collection;
            Iterator orgItor = orgIds.iterator();
            ArrayList removed = new ArrayList( orgIds.size() );
            while ( orgItor.hasNext() ) {
                Object id = orgItor.next();
                if ( !newMap.containsKey( id ) )
                    removed.add( id );
            }
            return removed;
        }

        if ( collection instanceof Collection ) {
            if ( orgIds == null || orgIds.size() == 0 )
                return new ArrayList(0);

            Collection newCol = (Collection) collection;
            Iterator orgItor = orgIds.iterator();
            ArrayList removed = new ArrayList(0);

            // make a new map of key and value of the new collection
            HashMap newMap = new HashMap();
            Iterator newColItor = newCol.iterator();
            while ( newColItor.hasNext() ) {
                Object newObject = newColItor.next();
                Object newId = ch.getIdentity( tx, newObject );
                if ( newId != null )
                    newMap.put( newId, newObject );
            }
            while ( orgItor.hasNext() ) {
                Object id = orgItor.next();
                if ( !newMap.containsKey( id ) )
                    removed.add( id );
            }
            return removed;
        }

        throw new IllegalArgumentException( "Collection type "+collection.getClass().getName()+" is not supported!" );
    }

    /**
     * Update the object which loaded or created in the other transaction to
     * the persistent storage.
     *
     * @param tx Transaction in action
     * @param oid the object identity of the stored object
     * @param locker the dirty check cache of the object
     * @param object the object to be stored
     */
    public Object update( TransactionContext tx, OID oid, DepositBox locker, Object object, AccessMode suggestedAccessMode )
            throws PersistenceException, ObjectModifiedException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] fields;
        Object ids;
        AccessMode am;
        Object value;
        Object stamp;
        Object[] temp;
        int fieldType;
        Object o;
        AccessMode accessMode = getAccessMode( suggestedAccessMode );

        fields = (Object[]) locker.getObject( tx );

        if ( !isDependent() && !_timeStampable )
            throw new IllegalArgumentException("A master object that involves in a long transaction must be a TimeStampable!");

        long lockTimestamp = locker.getTimeStamp();
        long objectTimestamp = _timeStampable? ((TimeStampable)object).jdoGetTimeStamp(): 1;

        if ( objectTimestamp > 0 ) {
            // valid range of timestamp

            if ( _timeStampable && objectTimestamp != lockTimestamp )
                throw new ObjectModifiedException("Timestamp mismatched!");

            if ( !_timeStampable && isDependent() && fields == null  ) {
                // allow a dependent object not implements timeStampable
                fields = new Object[_fhs.length];
                Connection conn = (Connection)tx.getConnection(oid.getLockEngine());
                stamp = _persistence.load( conn, fields, oid.getIdentity(), accessMode );
                oid.setDbLock( accessMode == AccessMode.DbLocked );
                locker.setObject( tx, fields );
            }

            ids = oid.getIdentity();

            // load the original field into the transaction. so, store will
            // have something to compare later.
            try {
                for ( int i=0; i <_fhs.length; i++ ) {
                    fieldType = _fhs[i].getFieldType();
                    switch (fieldType) {
                    case FieldMolder.PRIMITIVE:
                        break;
                    case FieldMolder.SERIALIZABLE:
                        break;
                    case FieldMolder.PERSISTANCECAPABLE:
                        fieldClassMolder = _fhs[i].getFieldClassMolder();
                        fieldEngine = _fhs[i].getFieldLockEngine();
                        o = _fhs[i].getValue( object, tx.getClassLoader() );
                        if ( _fhs[i].isDependent() ) {
                            // depedent class won't have persistenceInfo in LockEngine
                            // must look at fieldMolder for it

                            if ( o != null && !tx.isRecorded(o) )
                                tx.update( fieldEngine, fieldClassMolder, o, oid );

                            // load the cached dependent object from the data store.
                            // The loaded will be compared with the new one
                            if ( fields[i] != null )
                                tx.load( fieldEngine, fieldClassMolder, fields[i], null, suggestedAccessMode );
                        } else if ( tx.isAutoStore() ) {
                            if ( o != null && !tx.isRecorded(o) )
                                tx.update( fieldEngine, fieldClassMolder, o, null );

                            if ( fields[i] != null )
                                tx.load( fieldEngine, fieldClassMolder, fields[i], null, suggestedAccessMode );
                        }
                        break;

                    case FieldMolder.ONE_TO_MANY:
                        fieldClassMolder = _fhs[i].getFieldClassMolder();
                        fieldEngine = _fhs[i].getFieldLockEngine();
                        if ( _fhs[i].isDependent() ) {
                            if ( !_fhs[i].isLazy() ) {
                                Iterator itor = getIterator( _fhs[i].getValue( object, tx.getClassLoader() ) );
                                ArrayList v = (ArrayList)fields[i];
                                ArrayList newSetOfIds = new ArrayList();

                                // iterate the collection of this data object field
                                while ( itor.hasNext() ) {
                                    Object element = itor.next();
                                    Object actualIdentity = fieldClassMolder.getActualIdentity( tx, element );
                                    newSetOfIds.add( actualIdentity );
                                    if ( v != null && v.contains( actualIdentity ) ) {
                                        if ( !tx.isRecorded( element ) )
                                            tx.update( fieldEngine, fieldClassMolder, element, oid );
                                    } else {
                                        if ( !tx.isRecorded( element ) )
                                            tx.create( fieldEngine, fieldClassMolder, element, oid );
                                    }
                                }
                                if ( v != null ) {
                                    for ( int j=0,l=v.size(); j<l; j++ ) {
                                        if ( !newSetOfIds.contains( v.get(j) ) ) {
                                            // load all the dependent object in cache for modification
                                            // check at commit time.
                                            tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null, suggestedAccessMode );

                                        }
                                    }
                                }
                            } else {
                                ArrayList avlist = (ArrayList) fields[i];
                                fieldClassMolder = _fhs[i].getFieldClassMolder();
                                fieldEngine = _fhs[i].getFieldLockEngine();
                                RelationCollection relcol = new RelationCollection( tx, oid, fieldEngine, fieldClassMolder, accessMode, avlist );
                            }
                        } else if ( tx.isAutoStore() ) {
                            Iterator itor = getIterator( _fhs[i].getValue( object, tx.getClassLoader() ) );
                            ArrayList v = (ArrayList)fields[i];
                            ArrayList newSetOfIds = new ArrayList();

                            // iterate the collection of this data object field
                            while ( itor.hasNext() ) {
                                Object element = itor.next();
                                Object actualIdentity = fieldClassMolder.getActualIdentity( tx, element );
                                newSetOfIds.add( actualIdentity );
                                if ( !tx.isRecorded( element ) )
                                    tx.update( fieldEngine, fieldClassMolder, element, null );
                            }
                            // load all old objects for comparison in the preStore state
                            if ( v != null ) {
                                for ( int j=0,l=v.size(); j<l; j++ ) {
                                    if ( !newSetOfIds.contains( v.get(j) ) ) {
                                        System.out.println( "load object with id of: "+v.get(j) );
                                        // load all the dependent object in cache for modification
                                        // check at commit time.
                                        tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null, suggestedAccessMode );
                                    }
                                }
                            }
                        }
                        break;
                    case FieldMolder.MANY_TO_MANY:
                        fieldClassMolder = _fhs[i].getFieldClassMolder();
                        fieldEngine = _fhs[i].getFieldLockEngine();
                        if ( tx.isAutoStore() ) {
                            Iterator itor = getIterator( _fhs[i].getValue( object, tx.getClassLoader() ) );
                            ArrayList v = (ArrayList)fields[i];
                            ArrayList newSetOfIds = new ArrayList();

                            // iterate the collection of this data object field
                            while ( itor.hasNext() ) {
                                Object element = itor.next();
                                Object actualIdentity = fieldClassMolder.getActualIdentity( tx, element );
                                newSetOfIds.add( actualIdentity );
                                if ( !tx.isRecorded( element ) ) {
                                    OID updatedOID = tx.update( fieldEngine, fieldClassMolder, element, null );
                                }
                            }
                            // load all old objects for comparison in the preStore state
                            if ( v != null ) {
                                for ( int j=0,l=v.size(); j<l; j++ ) {
                                    if ( !newSetOfIds.contains( v.get(j) ) ) {
                                        System.out.println( "load object with id of: "+v.get(j) );
                                        // load all the dependent object in cache for modification
                                        // check at commit time.
                                        tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null, suggestedAccessMode );
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            } catch ( ObjectNotFoundException e ) {
                throw new ObjectModifiedException("dependent object deleted concurrently");
            }
            return ids;
        } else if ( objectTimestamp == TimeStampable.NO_TIMESTAMP ) {
            // work almost like create, except update the sub field instead of create
            fields = new Object[_fhs.length];
            ids = oid.getIdentity();

            // copy the object to cache should make a new field now,
            for ( int i=0; i<_fhs.length; i++ ) {
                fieldType = _fhs[i].getFieldType();

                switch (fieldType) {
                case FieldMolder.PRIMITIVE: // primitive includes int, float, Date, Time etc
                    fields[i] = _fhs[i].getValue( object, tx.getClassLoader() );
                    break;

                case FieldMolder.PERSISTANCECAPABLE:
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null ) {
                        if ( !_fhs[i].isDependent() ) {
                            Object fid = fieldClassMolder.getIdentity( tx, o );
                            if ( fid != null ) {
                                fields[i] = fid;
                            }
                        } // dependent object will be created at preStore state
                    }
                    break;

                case FieldMolder.SERIALIZABLE:
                    try {
                        Object dependent = _fhs[i].getValue( object, tx.getClassLoader() );
                        if ( dependent != null ) {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutputStream os = new ObjectOutputStream( bos );
                            os.writeObject( dependent );
                            fields[i] = bos.toByteArray();
                        } else {
                            fields[i] = null;
                        }
                    } catch ( IOException e ) {
                        throw new PersistenceException( "Error during serializing dependent object", e );
                    }
                    break;

                case FieldMolder.ONE_TO_MANY:
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null && !_fhs[i].isDependent() ) {
                        ArrayList fids = extractIdentityList( tx, fieldClassMolder, o );
                        fields[i] = fids;
                    } // dependent objects will be created at preStore state
                    break;

                case FieldMolder.MANY_TO_MANY:
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null ) {
                        ArrayList fids = extractIdentityList( tx, fieldClassMolder, o );
                        fields[i] = fids;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Field type invalid!");
                }
            }

            // ask Persistent to create the object into the persistence storage
            Object createdId = _persistence.create( (Connection)tx.getConnection(oid.getLockEngine()),
                    fields, ids );

            if ( createdId == null )
                throw new PersistenceException("Identity can't be created!");

            // if the creation succeed, we set the field into the dirty checking
            // cache.
            locker.setObject( tx, fields );
            oid.setDbLock( true );

            // set the identity into the object
            setIdentity( tx, object, createdId );

            boolean autoCreated = false;

            // iterate all the fields and create all the dependent object.
            for ( int i=0; i<_fhs.length; i++ ) {
                fieldType = _fhs[i].getFieldType();
                switch (fieldType) {
                case FieldMolder.PRIMITIVE:
                case FieldMolder.SERIALIZABLE:
                    // nothing need to be done here for primitive
                    break;

                case FieldMolder.PERSISTANCECAPABLE:
                    // create dependent object if exists
                    autoCreated = false;
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null ) {
                        if ( _fhs[i].isDependent() ) {
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
                            /*
                            if ( !tx.isPersistent( o ) ) {
                                tx.create( fieldEngine, fieldClassMolder, o, oid );
                            } else {}
                                // fail-fast principle: if the object depend on another object,
                               // throw exception
                             */
                            //if ( !tx.isDepended( oid, o ) )
                            //    throw new PersistenceException("Dependent object may not change its master. Object: "+o+" new master: "+oid);
                        } else if ( tx.isAutoStore() ) {
                            if ( !tx.isRecorded( o ) ) {
                                // related object should be created right the way, if autoStore
                                // is enabled, to obtain a database lock on the row. If both side
                                // uses keygenerator, the current object will be updated in the
                                // store state.
                                OID fieldOid = tx.update( fieldEngine, fieldClassMolder, o, null );
                                if ( _isKeyGenUsed ) {
                                    fields[i] = fieldOid.getIdentity();
                                    autoCreated = true;
                                }
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
                            }
                        }
                    }
                    break;

                case FieldMolder.ONE_TO_MANY:
                    // create dependent objects if exists
                    autoCreated = false;
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null ) {
                        Iterator itor = getIterator( o );
                        while (itor.hasNext()) {
                            Object oo = itor.next();
                            if ( _fhs[i].isDependent() ) {
                                /*
                                if ( !tx.isPersistent( oo ) ) {
                                    autoCreated = true;
                                    tx.create( fieldEngine, fieldClassMolder, oo, oid );
                                } else
                                    // fail-fast principle: if the object depend on another object,
                                    // throw exception
                                    if ( !tx.isDepended( oid, oo ) )
                                        throw new PersistenceException("Dependent object may not change its master");
                                 */
                            } else if ( tx.isAutoStore() ) {
                                if ( !tx.isRecorded( o ) ) {
                                    OID fieldOid = tx.update( fieldEngine, fieldClassMolder, oo, null );
                                    if ( _isKeyGenUsed ) {
                                        ((ArrayList)fields[i]).add( fieldOid.getIdentity() );
                                        autoCreated = true;
                                    }
                                }
                            }
                        }
                    }
                    break;

                case FieldMolder.MANY_TO_MANY:
                    // create relation if the relation table
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null ) {
                        Iterator itor = getIterator( o );
                        // many-to-many relation is never dependent relation
                        while (itor.hasNext()) {
                            Object oo = itor.next();
                            if ( tx.isPersistent( oo ) ) {
                                _fhs[i].getRelationLoader().createRelation(
                                (Connection)tx.getConnection(oid.getLockEngine()),
                                createdId, fieldClassMolder.getIdentity( tx, oo ) );
                            } else if ( tx.isAutoStore() ) {
                                if ( !tx.isRecorded( oo ) ) {
                                    OID fieldOid = tx.update( fieldEngine, fieldClassMolder, oo, null );
                                    if ( _isKeyGenUsed ) {
                                        ((ArrayList)fields[i]).add( fieldOid.getIdentity() );
                                        autoCreated = true;
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }

            if ( autoCreated ) {
                // set the object into DespositBox again, if changes made
                locker.setObject( tx, fields );
            }

            // set the new timeStamp into the data object
            if ( object instanceof TimeStampable ) {
                ((TimeStampable)object).jdoSetTimeStamp( locker.getTimeStamp() );
            }
            return createdId;
        } else {
            System.err.println( "object: "+object+" timestamp: "+objectTimestamp+"lockertimestamp: "+lockTimestamp );
            throw new ObjectModifiedException("Invalid object timestamp detected.");
        }
    }

    /**
     * Update the dirty checking cache. This method is called after a transaction completed successfully.
     *
     * @param tx - transaction in action
     * @param oid - object's identity of the target object
     * @param locker - the dirty checking cache of the target object
     * @param object - the target object
     */
    public void updateCache( TransactionContext tx, OID oid, DepositBox locker, Object object ) {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        ArrayList fids;
        Object fid;
        Object[] fields;
        Object value;

        int fieldType;

        fields = new Object[_fhs.length];

        if ( oid.getIdentity() == null )
            throw new IllegalStateException("The identities of the cache to be updated is null");

        for ( int i=0; i < _fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                // should give some attemp to reduce new object array
                fields[i] = _fhs[i].getValue( object, tx.getClassLoader() );
                break;

            case FieldMolder.SERIALIZABLE:
                try {
                    Object o = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( o != null ) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream( bos );
                        os.writeObject( o );
                        fields[i] = bos.toByteArray();
                    } else {
                        fields[i] = null;
                    }
                } catch ( IOException e ) {
                    // It won't happen. ByteArrayOutputStream will not throw IOException
                }
                break;
            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( value != null ) {
                    fid = fieldClassMolder.getIdentity( tx, value );
                    if ( fid != null ) {
                        fields[i] = fid;
                    }
                } else {
                    fields[i] = null;
                }
                break;
            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( value != null ) {
                    if ( !(value instanceof Lazy) ) {
                        fids = extractIdentityList( tx, fieldClassMolder, value );
                        fields[i] = fids;
                    } else {
                        RelationCollection lazy = (RelationCollection) value;
                        fids = (ArrayList)lazy.getIdentitiesList().clone();
                        fields[i] = fids;
                    }
                } else {
                    fields[i] = null;
                }
                break;
            case FieldMolder.MANY_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                value = _fhs[i].getValue( object, tx.getClassLoader() );
                if ( value != null ) {
                    if ( !(value instanceof Lazy ) ) {
                        fids = extractIdentityList( tx, fieldClassMolder, value );
                        fields[i] = fids;
                    } else {
                        RelationCollection lazy = (RelationCollection) value;
                        fids = (ArrayList)lazy.getIdentitiesList().clone();
                        fields[i] = fids;
                    }
                } else {
                    fields[i] = null;
                }
                break;
            default:
                throw new IllegalArgumentException("Field type invalid!");
            }
        }

        locker.setObject( tx, fields );
        if ( object instanceof TimeStampable ) {
            ((TimeStampable)object).jdoSetTimeStamp( locker.getTimeStamp() );
        }

    }

    /**
     * It method is called by delete to delete the extended object of the base
     * type from the Persistence.
     *
     */
    private static void deleteExtend( TransactionContext tx, ClassMolder extend, Object identity )
            throws ObjectNotFoundException, PersistenceException {
        // if the extend field contains dependent of field type,
        // fields must be loaded, so that we get the foreign key
        // of the depedent table. We may get no result. And, it
        // is good and we don't have to process further.
        // it will be cheaper if we go directly to SQL and load
        // only the foreign field needed. But, it require extra
        // method in Persistent SPI. So, maybe we rather stay
        // with the expensive appoarch.

        // if there is any depedent field of reference type, the simplest way is
        // to load the dependent objects and delete it using tansaction.
        // (Will ObjectNotFound throws in the loading??)

        // if the extend field has many-to-many field, we must delete the
        // relation from he relation table.
        Object[] persistFields = null;
        for ( int i=0; i < extend._fhs.length; i++ ) {
            if ( extend._fhs[i].isDependent() ) {
                try {
                    if ( persistFields == null ) {

                        persistFields = new Object[extend._fhs.length];
                        extend._persistence.load( (Connection)tx.getConnection(extend._engine),
                        persistFields, identity, AccessMode.ReadOnly );
                    }

                    if ( extend._fhs[i].isMulti() ) {
                        ArrayList listOfIds = (ArrayList)persistFields[i];
                        for ( int j=0; i < listOfIds.size(); j++ ) {
                            extend._persistence.delete( tx.getConnection(extend._fhs[i].getFieldLockEngine()),
                            listOfIds.get(j) );
                        }
                    }
                } catch ( ObjectNotFoundException e ) {
                }
            } else if ( extend._fhs[i].isManyToMany() ) {
                extend._fhs[i].getRelationLoader().deleteRelation(
                (Connection)tx.getConnection(extend._fhs[i].getFieldLockEngine()),
                identity );
            }
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
    public void delete( TransactionContext tx, OID oid )
            throws PersistenceException {

        Object ids = oid.getIdentity();
        _persistence.delete( (Connection)tx.getConnection(oid.getLockEngine()), ids );

        // All field along the extend path will be deleted by transaction
        // However, everything off the path must be deleted by ClassMolder.

        Vector extendPath = new Vector();
        ClassMolder base = this;
        while ( base != null ) {
            extendPath.add( base );
            base = base._extends;
        }

        base = _depends;
        while ( base != null ) {
            if ( base._extendent != null )
                for ( int i=0; i < base._extendent.size(); i++ )
                    if ( !extendPath.contains( base._extendent.get(i) ) ) {
                        //deleteExtend( tx, (ClassMolder)base._extendent.get(i), ids );
                    } else {
                    }

            base = base._extends;
        }

        if ( _extendent != null ) {
            for ( int i=0; i < _extendent.size(); i++ ) {
                if ( !extendPath.contains( _extendent.get(i) ) ) {
                    //deleteExtend( tx, (ClassMolder)_extendent.get(i), ids );
                }
            }
        }

        for( int i=0; i < _fhs.length; i++ ) {
            if( _fhs[i].isManyToMany() ) {
                _fhs[i].getRelationLoader().deleteRelation(
                  (Connection)tx.getConnection(oid.getLockEngine()),
                  ids);
            }
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
    public void markDelete( TransactionContext tx, OID oid, DepositBox locker, Object object )
            throws ObjectNotFoundException, PersistenceException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] fields;

        fields = (Object[])locker.getObject( tx );


        for ( int i=0; i < _fhs.length; i++ ) {
            int fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
            case FieldMolder.SERIALIZABLE:
                break;
            case FieldMolder.PERSISTANCECAPABLE:
                // persistanceCapable include many_to_one
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                if ( _fhs[i].isDependent() ) {
                    Object fid = fields[i];
                    Object fetched = null;
                    if ( fid != null ) {
                        fetched = tx.fetch( fieldEngine, fieldClassMolder, fid, null );
                        if ( fetched != null )
                            tx.delete( fetched );
                    }

                    Object fobject = _fhs[i].getValue( object, tx.getClassLoader() );
                    if ( fobject != null && tx.isPersistent( fobject ) ) {
                        tx.delete( fobject );
                    }
                } else {
                    // delete the object from the other side of the relation
                    Object fid = fields[i];
                    Object fetched = null;
                    if ( fid != null ) {
                        fetched = tx.fetch( fieldEngine, fieldClassMolder, fields[i], null );
                        if ( fetched != null )
                            fieldClassMolder.removeRelation( tx, fetched, this, object );
                    }
                }
                break;
            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                // markDelete mix with prestore
                // so, store is not yet called, and only the loaded (or created)
                // relation have to be deleted.
                // not really. cus, the other created relation, may already
                // has reference to this object. so, how to deal with that?
                if ( _fhs[i].isDependent() ) {
                    ArrayList alist = (ArrayList) fields[i];
                    if ( fields[i] != null ) {
                        for ( int j=0; j<alist.size(); j++ ) {
                            Object fid =  alist.get(j);
                            Object fetched = null;
                            if ( fid != null ) {
                                fetched = tx.fetch( fieldEngine, fieldClassMolder, fid, null );
                                if ( fetched != null )
                                    tx.delete( fetched );
                            }
                        }

                        Iterator itor = getIterator( _fhs[i].getValue( object, tx.getClassLoader() ) );
                        while ( itor.hasNext() ) {
                            Object fobject = itor.next();
                            if ( fobject != null && tx.isPersistent( fobject ) ) {
                                tx.delete( fobject );
                            }
                        }
                    }
                } else {
                    if ( fields[i] != null ) {
                        ArrayList alist = (ArrayList) fields[i];
                        for ( int j=0; j<alist.size(); j++ ) {
                            Object fid = alist.get(j);
                            Object fetched = null;
                            if ( fid != null ) {
                                fetched = tx.fetch( fieldEngine, fieldClassMolder, fid, null );
                                if ( fetched != null ) {
                                    fieldClassMolder.removeRelation( tx, fetched, this, object );
                                }
                            }
                        }

                        Iterator itor = getIterator( _fhs[i].getValue( object, tx.getClassLoader() ) );
                        while ( itor.hasNext() ) {
                            Object fobject = itor.next();
                            if ( fobject != null && tx.isPersistent( fobject ) ) {
                                fieldClassMolder.removeRelation( tx, fobject, this, object );
                            }
                        }
                    }
                }
                break;
            case FieldMolder.MANY_TO_MANY:
                // delete the relation in relation table too
                /*
                _fhs[i].getRelationLoader().deleteRelation(
                (Connection)tx.getConnection(oid.getLockEngine()),
                oid.getIdentity() );
                */

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                // markDelete mix with prestore
                // so, store is not yet called, and only the loaded (or created)
                // relation have to be deleted.
                // not really. cus, the other created relation, may already
                // has reference to this object. so, how to deal with that?
                ArrayList alist = (ArrayList) fields[i];
                for ( int j=0; j<alist.size(); j++ ) {
                    Object fid = alist.get(j);
                    Object fetched = null;
                    if ( fid != null ) {
                        fetched = tx.fetch( fieldEngine, fieldClassMolder, fid, null );
                        if ( fetched != null ) {
                            fieldClassMolder.removeRelation( tx, fetched, this, object );
                        }
                    }
                }

                Iterator itor = getIterator( _fhs[i].getValue( object, tx.getClassLoader() ) );
                while ( itor.hasNext() ) {
                    Object fobject = itor.next();
                    if ( fobject != null && tx.isPersistent( fobject ) ) {
                        fieldClassMolder.removeRelation( tx, fobject, this, object );
                    }
                }
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
    public void revertObject( TransactionContext tx, OID oid, DepositBox locker, Object object )
            throws PersistenceException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] fields;
        Object value;
        int fieldType;

        if ( oid.getIdentity() == null )
            throw new PersistenceException("The identities of the object to be revert is null");

        fields = (Object[]) locker.getObject( tx );

        setIdentity( tx, object, oid.getIdentity() );

        for ( int i=0; i < _fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                Object temp = fields[i];
                break;
            case FieldMolder.SERIALIZABLE:
                // deserialize byte[] into java object
                try {
                    byte[] bytes = (byte[]) fields[i];
                    if ( bytes != null ) {
                        // The following code can be updated, after Blob-->InputStream
                        // to enhance performance.
                        ByteArrayInputStream bis = new ByteArrayInputStream( bytes );
                        ObjectInputStream os = new ObjectInputStream( bis );
                        Object o = os.readObject();
                        _fhs[i].setValue( object, o, tx.getClassLoader() );
                    } else {
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                    }
                } catch ( OptionalDataException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                } catch ( ClassNotFoundException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                } catch ( IOException e ) {
                    throw new PersistenceException( "Error while deserializing an dependent object", e );
                }
                break;
            case FieldMolder.PERSISTANCECAPABLE:

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();

                if ( fields[i] != null ) {
                    value = tx.load( fieldEngine, fieldClassMolder, fields[i], null, null );
                    _fhs[i].setValue( object, value, tx.getClassLoader() );
                } else {
                    _fhs[i].setValue( object, null, tx.getClassLoader() );
                }
                break;
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:

                Object o = fields[i];
                if ( o == null ) {
                    _fhs[i].setValue( object, null, tx.getClassLoader() );
                } else if ( !(o instanceof Lazy) ) {
                    ArrayList col = new ArrayList();
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();

                    CollectionProxy cp = CollectionProxy.create( _fhs[i], object, tx.getClassLoader() );
                    ArrayList v = (ArrayList)fields[i];
                    if ( v != null ) {
                        for ( int j=0,l=v.size(); j<l; j++ ) {
                            cp.add( v.get(j), tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null, null ) );
                        }
                        cp.close();
                        //_fhs[i].setValue( object, cp.getCollection() );
                    } else {
                        _fhs[i].setValue( object, null, tx.getClassLoader() );
                    }
                } else {
                    ArrayList list = (ArrayList) fields[i];
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();

                    RelationCollection relcol = new RelationCollection( tx, oid, fieldEngine, fieldClassMolder, null, list );
                    _fhs[i].setValue( object, relcol, tx.getClassLoader() );
                }
                break;
            default:
            }
        }
    }

    /**
     * Acquire a write lock on an object of the base type with the specified
     * identity from the persistence storage.
     *
     * @param tx - Transaction in action
     * @param oid - the object identity of the target object
     * @param locker - the dirty checking cache of the object
     * @param object - the target object
     */
    public void writeLock( TransactionContext tx, OID oid, DepositBox locker, Object object )
            throws PersistenceException {
        // call SQLEngine to lock an record
    }


    /**
     * Return a new instance of the base class with the provided ClassLoader object
     *
     * @param laoder the ClassLoader object to use to create a new object
     * @return Object the object reprenseted by this ClassMolder, and instanciated
     * with the provided ClassLoader instance.
     */
    public Object newInstance( ClassLoader loader ) {
        try {
            if (loader != null )
                return loader.loadClass(_name).newInstance();
            else
                return Class.forName(_name).newInstance();
        } catch (ClassNotFoundException e) {
        } catch ( IllegalAccessException e ) {
        } catch ( InstantiationException e ) {
        } catch ( ExceptionInInitializerError e ) {
        } catch ( SecurityException e ) {
        }
        return null;
    }

    /**
     * Get the effective accessMode of the the base type
     *
     * @param txMode - the default transaction accessMode
     * @return the effective acessMode of the base type
     */
    public AccessMode getAccessMode( AccessMode txMode ) {

        if ( txMode == null )
            return _accessMode;

        if ( _accessMode == AccessMode.ReadOnly || txMode == AccessMode.ReadOnly )
            return AccessMode.ReadOnly;
        if ( _accessMode == AccessMode.DbLocked || txMode == AccessMode.DbLocked )
            return AccessMode.DbLocked;
        if ( _accessMode == AccessMode.Exclusive || txMode == AccessMode.Exclusive )
            return AccessMode.Exclusive;
        return txMode;

    }

    /**
     * Get the callback interceptor of the base type
     *
     */
    public CallbackInterceptor getCallback() {
        return _callback;
    }

    /**
     * Get the identity from a object of the base type
     * If object isn't persistent and key generator is used, returns null
     *
     * @param tx the transaction context
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getIdentity( TransactionContext tx, Object o ) {
        // [oleg] In the case where key generator is used,
        // the value of identity is dummy, set it to null
        if ( isKeyGeneratorUsed() && ! (tx.isPersistent(o) || tx.isReadOnly(o))) {
            return null;
        } else {
            return getActualIdentity( tx, o );
        }
    }

    /**
     * Get the identity from a object of the base type
     *
     * @param tx the transaction context
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getActualIdentity( TransactionContext tx, Object o ) {
        return getActualIdentity( tx.getClassLoader(), o );
    }

    /**
     * Get the identity from a object of the base type
     *
     * @param loader the current class loader
     * @param o - object of the base type
     * @return return an Object[] which contains the identity of the object
     */
    public Object getActualIdentity( ClassLoader loader, Object o ) {
        Object temp;

        if ( _ids.length == 1 ) {
            return _ids[0].getValue( o, loader );
        } else if ( _ids.length == 2 ) {
            temp = _ids[0].getValue( o, loader );
            return temp==null? null: new Complex( temp, _ids[1].getValue( o, loader ) );
        } else {
        Object[] osIds = new Object[_ids.length];
        for ( int i=0; i<osIds.length; i++ ) {
            osIds[i] = _ids[i].getValue( o, loader );
        }
            if ( osIds[0] == null )
                return null;
            else
                return new Complex( osIds );
        }
    }

    /**
     * Set the identity into an object
     *
     * @param tx the transaction context
     * @param object the object to set the identity
     * @param identity the new identity for the object
     */
    public void setIdentity( TransactionContext tx, Object object, Object identity )
            throws PersistenceException {

        if ( _ids.length > 1 ) {
            if ( identity instanceof Complex ) {
                Complex com = (Complex) identity;
                if ( com.size() != _ids.length )
                    throw new PersistenceException( "Complex size mismatched!" );

                for ( int i=0; i<_ids.length; i++ ) {
                    _ids[i].setValue( object, com.get(i), tx.getClassLoader() );
                }
            }
        } else {
            if ( identity instanceof Complex )
                throw new PersistenceException( "Complex type not accepted!" );
            _ids[0].setValue( object, identity, tx.getClassLoader() );
        }
    }
    /**
     * Get the Persisetence of the base type
     *
     */
    public Persistence getPersistence() {
        return _persistence;
    }

    /**
     * Mutator method to set the PersistenceEngine of
     *
     */
    public void setPersistence( Persistence persist ) {
        _persistence = persist;
    }


    /**
     * Get the base class of this ClassMolder given a ClassLoader
     * @param loader the classloader
     * @return the <code>Class</code> instance
     */
    public Class getJavaClass( ClassLoader loader ) {
        Class result = null;
        try {
            result = ( loader != null) ? loader.loadClass(_name) : Class.forName(_name);
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
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
    public boolean isAssignableFrom ( Class cls ) {
        ClassLoader loader = cls.getClassLoader();
        Class molderClass = null;
        try {
            if ( loader != null )
                molderClass = loader.loadClass( _name );
            else
                molderClass = Class.forName( _name );
        } catch ( ClassNotFoundException e ) {
            return false;
        }
        return molderClass.isAssignableFrom( cls );

    }

    /**
     * Get the fully qualified name of the base type of this ClassMolder
     *
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
     * Get the FieldMolders of the identity fields
     */
    public FieldMolder[] getIds() {
        return _ids;
    }

    /**
     * Get the extends class' ClassMolder
     *
     */
    public ClassMolder getExtends() {
        return _extends;
    }

    /**
     * Get the depends class' ClassMolder
     */
    public ClassMolder getDepends() {
        return _depends;
    }

    /**
     * Get the LockEngine which this ClassMolder belongs to.
     *
     */
    public LockEngine getLockEngine() {
        return _engine;
    }
    /**
     * Return the preferred LRU cache mechanism for caching object of this type
     *
     */
    public int getCacheType() {
        return _cachetype;
    }
    /**
     * Return the preferred LRU cache capacity for caching object of this type
     *
     */
    public int getCacheParam() {
        return _cacheparam;
    }

    /**
     * Return true if the base type of this ClassMolder is an dependent
     * class.
     */
    public boolean isDependent() {
        return _depends != null;
    }

    /**
     * Set all persistence fields of object of the base type to null.
     *
     * @param object - target object
     */
    public void setFieldsNull( Object object ) {
        /*
        for ( int i=0; i < _ids.length; i++ ) {
            _ids[i].setValue( object, null );
        }
        for ( int i=0; i < _fhs.length; i++ ) {
            _fhs[i].setValue( object, null );
        }*/
    }

    /**
     * Mutator method to add a extent ClassMolder
     *
     */
    void addExtendent( ClassMolder ext ) {
        if ( _extendent == null )
            _extendent = new Vector();
        _extendent.add( ext );
    }

    /**
     * Mutator method to add a dependent ClassMolder
     *
     */
    void addDependent( ClassMolder dep ) {
        if ( _dependent == null )
            _dependent = new Vector();
        _dependent.add( dep );
    }
    /**
     * Mutator method to set the extends ClassMolder
     *
     */
    void setExtends( ClassMolder ext ) {
        _extends = ext;
        ext.addExtendent( this );
    }

    /**
     * Mutator method to set the depends ClassMolder
     *
     */
    void setDepends( ClassMolder dep ) {
        _depends = dep;
        dep.addDependent( this );
    }

    /**
     * Return the iterator on values of the specified Collection
     * Or, return the iterator on values of the specified Map
     *
     * @param o - a Collection
     */
    private Iterator getIterator( Object o ) {
        if ( o == null ) {
            return new Iterator() {
                public boolean hasNext() {
                    return false;
                }
                public Object next() {
                    throw new NoSuchElementException();
                }
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else if ( o instanceof Collection ) {
            return ((Collection) o).iterator();
        } else if ( o instanceof Map ) {
            return ((Map) o).values().iterator();
        } else {
            throw new IllegalArgumentException();
        }
    }



    /**
     * Return all the object identity of a Collection of object of the same
     * type.
     *
     * @param tx the transaction context
     * @param molder - class molder of the type of the objects
     * @param col - a Collection or Vector containing
     * @return an <tt>ArrayList</tt>s which contains list of object identity
     */
    private ArrayList extractIdentityList( TransactionContext tx, ClassMolder molder, Object col ) {
        if ( col == null ) {
            return new ArrayList();
        } else if ( col instanceof Collection ) {
            ArrayList idList = new ArrayList();
            Iterator itor = ((Collection)col).iterator();
            while ( itor.hasNext() ) {
                Object id = molder.getIdentity( tx, itor.next() );
                if ( id != null )
                    idList.add( id );
            }
            return idList;
        } else if ( col instanceof Map ) {
            ArrayList idList = new ArrayList();
            Iterator itor = ((Map)col).keySet().iterator();
            while ( itor.hasNext() ) {
                idList.add( itor.next() );
            }
            return idList;
        } else {
            throw new IllegalArgumentException("A Collection or Map is expected!");
        }
    }

    public String toString() {
        return "ClassMolder "+ _name;
    }

    /**
     * Return true if a key generator is used for the base type of this ClassMolder
     */
    public boolean isKeyGeneratorUsed() {
        return _isKeyGenUsed || (_extends != null && _extends. isKeyGeneratorUsed());
    }

}

/**
 * This class is a proxy for different types of Colleciton and Maps.
 */
abstract class CollectionProxy {

    abstract Object getCollection();

    abstract void add( Object key, Object value );

    abstract void close();

    static CollectionProxy create( FieldMolder fm, Object object, ClassLoader cl ) {
        Class cls = fm.getCollectionType();
        if ( cls == Vector.class ) {
            return new ColProxy( fm, object, cl, new Vector() );
        } else if ( cls == ArrayList.class ) {
            return new ColProxy( fm, object, cl, new ArrayList() );
        } else if ( cls == Collection.class ) {
            return new ColProxy( fm, object, cl, new ArrayList() );
        } else if ( cls == Set.class ) {
            return new ColProxy( fm, object, cl, new HashSet() );
        } else if ( cls == HashSet.class ) {
            return new ColProxy( fm, object, cl, new HashSet() );
        } else if ( cls == Hashtable.class ) {
            return new MapProxy( fm, object, cl, new Hashtable() );
        } else if ( cls == HashMap.class ) {
            return new MapProxy( fm, object, cl, new HashMap() );
        } else if ( cls == Map.class ) {
            return new MapProxy( fm, object, cl, new HashMap() );
        } else {
            throw new IllegalArgumentException("Collection Proxy doesn't exist for it type");
        }
    }

    private static class ColProxy extends CollectionProxy {
        private Collection _col;
        private FieldMolder _fm;
        private Object _object;
        private ClassLoader _cl;
        private ColProxy( FieldMolder fm, Object object, ClassLoader cl, Collection col ) {
            _cl = cl;
            _fm = fm;
            _col = col;
            _object = object;
        }
        Object getCollection() {
            return _col;
        }
        void add( Object key, Object value ) {
            if ( !_fm.isAddable() )
                _col.add( value );
            else
                _fm.addValue( _object, value, _cl );
        }
        void close() {
            if ( !_fm.isAddable() )
                _fm.setValue( _object, _col, _cl );
        }
    }

    private static class MapProxy extends CollectionProxy {
        private Map _map;
        private FieldMolder _fm;
        private Object _object;
        private ClassLoader _cl;
        private MapProxy( FieldMolder fm, Object object, ClassLoader cl, Map map ) {
            _cl = cl;
            _map = map;
            _fm = fm;
            _object = object;
        }
        Object getCollection() {
            return _map;
        }
        void add( Object key, Object value ) {
            _map.put( key, value );
        }
        void close() {
            if ( !_fm.isAddable() )
                _fm.setValue( _object, _map, _cl );
        }

    }
}


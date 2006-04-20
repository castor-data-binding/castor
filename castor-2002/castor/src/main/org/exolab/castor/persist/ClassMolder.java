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
 * $Id: ClassMolder.java,
 */


package org.exolab.castor.persist;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
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
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.mapping.loader.RelationDescriptor;
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
import java.util.Vector;
import java.util.ArrayList;


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
     * The java data object class which this ClassMolder corresponding to.
     * (We calls it base class.)
     */
    private Class _base;

    /**
     * The fully qualified name of the base class.
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
     * Constructor
     *
     * @param ds      is the helper class for resolving depends and extends relationship
     *                among all the ClassMolder in the same LockEngine.
     * @param loader  is the mapping loader
     * @param classDescriptor   the classDescriptor for the base class
     * @param persist the Persistent for the base class
     */
    ClassMolder( DatingService ds, MappingLoader loader, LockEngine lock, ClassDescriptor clsDesc, Persistence persist )
            throws ClassNotFoundException, MappingException {

        ClassMapping clsMap = ((ClassDescriptorImpl) clsDesc).getMapping();

        _engine = lock;

        _persistence = persist;

        _name = clsMap.getName();

        _base = clsDesc.getJavaClass();

        _accessMode = AccessMode.getAccessMode( clsMap.getAccess() );

        ds.register( _name, this );

        ClassMapping dep = (ClassMapping) clsMap.getDepends();
        ClassMapping ext = (ClassMapping) clsMap.getExtends();

        if ( dep != null && ext != null )
            throw new MappingException("A JDO cannot both extends and depends on other objects");

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

                manyTable = fmFields[i].getSql().getManyTable();

                idSQL = new String[fmId.length];
                idType = new int[fmId.length];
                FieldDescriptor[] fd = ((ClassDescriptorImpl)clsDesc).getIdentities();
                for ( int j=0; j < fmId.length; j++ ) {
                    idSQL[j] = fmId[j].getSql().getName();
                    
                    if ( fd[j] instanceof JDOFieldDescriptor ) {
                        idType[j] = ((JDOFieldDescriptor)fd[j]).getSQLType();
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
                    for ( int j=0; j < relatedIdSQL.length; j++ ) {
                        if ( relatedIds[j] instanceof JDOFieldDescriptor ) {
                            relatedIdSQL[j] = ((JDOFieldDescriptor)relatedIds[j]).getSQLName();
                            relatedIdType[j] = ((JDOFieldDescriptor)relatedIds[j]).getSQLType();
                        } else {
                            throw new MappingException("Field type is not persistence-capable: "+ relatedIds[j].getFieldName() );
                        }
                    }
                }

                // if many-key exist, idSQL is overridden
                String manyKey = fmFields[i].getSql().getManyKey();
                String[] keys = breakApart( manyKey, ' ' );
                if ( keys != null && keys.length != 0 ) {
                    if ( keys.length != idSQL.length )
                        throw new MappingException("The number of many-keys doesn't match referred object: "+clsDesc.getJavaClass().getName());
                    idSQL = keys;
                }

                // if name="" exist, relatedIdSQL is overridden
                String manyName = fmFields[i].getSql().getName();
                String[] names = breakApart( manyName, ' ' );
                if ( names != null && names.length != 0 ) {
                    if ( names.length != relatedIdSQL.length )
                        throw new MappingException("The number of many-keys doesn't match referred object: "+relDesc.getJavaClass().getName());
                    relatedIdSQL = names;
                }

                _fhs[i] = new FieldMolder( ds, this, fmFields[i], manyTable, idSQL, idType, relatedIdSQL, relatedIdType );
            } else {
                _fhs[i] = new FieldMolder( ds, this, fmFields[i] );
            }
        }

        if ( Persistent.class.isAssignableFrom( _base ) )
            _callback = new JDOCallback();
    }

    /**
     * Break a string into array of substring which serparated
     * by a delimitator
     */
    private String[] breakApart( String strings, char delimit ) {
        if ( strings == null )
            return new String[0];
        Vector v = new Vector();
        int start = 0;
        int count = 0;
        while ( count < strings.length() ) {
            if ( strings.charAt( count ) == delimit ) {
                if ( start < (count - 1) ) {
                    v.add( strings.substring( start, count ) );
                    count++;
                    start = count;
                    continue;
                }
            } 
            count++;
        }
        if ( start < (count - 1) ) {
            v.add( strings.substring( start, count ) );
        }

        String[] result = new String[v.size()];
        v.copyInto( result );
        return result;
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
        boolean idfield;

        // start with the extended class
        base = clsMap;
        while ( base.getExtends() != null ) {
            base = (ClassMapping) base.getExtends();
        }
        fmDepended = null; 

        identities = breakApart( base.getIdentity(), ' ' );
       
        if ( identities == null || identities.length == 0 )
            throw new MappingException("Identity is null!");


        fmIds = new FieldMapping[identities.length];
        fmBase = base.getFieldMapping();
        for ( int i=0,j=0; i<fmBase.length; i++ ) {
            idfield = false;
            IDSEARCH:
            for ( int k=0; k<identities.length; k++ ) {
                if ( fmBase[i].getName().equals( identities[k] ) ) {
                    idfield = true;
                    break IDSEARCH;
                }
            }
            if ( idfield ) {
                fmIds[j] = fmBase[i];
                j++;
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


        if ( extend != null ) {
            extendFields = getFullFields( extend );
            thisFields = clsMap.getFieldMapping();

            fields = new FieldMapping[extendFields.length+thisFields.length];
            System.arraycopy( extendFields, 0, fields, 0, extendFields.length );
            System.arraycopy( thisFields, 0, fields, extendFields.length, thisFields.length );
        } else {
            identities = breakApart( clsMap.getIdentity(), ' ' );
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

        DatingService ds = new DatingService( ClassLoader.getSystemClassLoader() );
        enum = loader.listRelations();
        while ( enum.hasMoreElements() ) {
            ds.registerRelation( (RelationDescriptor) enum.nextElement() );
        }

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
    private boolean removeRelation( TransactionContext tx, Object object, ClassMolder relatedMolder, Object relatedObject ) {

        boolean removed = false;
        boolean updateCache = false;
        boolean updatePersist = false;
        for ( int i=0; i < _fhs.length; i++ ) {
            int fieldType = _fhs[i].getFieldType();
            ClassMolder fieldClassMolder;
            switch (fieldType) {
            case FieldMolder.PERSISTANCECAPABLE:
                // de-reference the object
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                if ( fieldClassMolder == relatedMolder ) {
                    Object related = _fhs[i].getValue( object );
                    if ( related == relatedObject ) {
                        _fhs[i].setValue( object, null );
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
                if ( fieldClassMolder == relatedMolder ) {
                    // same action to be taken for lazy and non lazy collection
                    Object related = _fhs[i].getValue( object );
                    ArrayList alist = (ArrayList) related;
                    boolean changed = alist.remove( relatedObject );
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
            Object object, AccessMode accessMode )
            throws ObjectNotFoundException, PersistenceException {       

        Connection conn;
        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Object[] fields;
        Object ids;
        Object stamp = null;        
        Object temp;
        int fieldType;

        if ( oid.getIdentity() == null ) 
            throw new PersistenceException("The identities of the object to be loaded is null");
      
        // load the fields from the persistent storage if the cache is empty
        // and the accessMode is readOnly.
        fields = (Object[]) locker.getObject( tx );
        if ( fields == null ) {
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
        setIdentity( object, ids );

        // iterates thur all the field of the object and bind all field.
        for ( int i = 0; i < _fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                // simply set the corresponding Persistent field value into the object
                temp = fields[i];
                if ( temp != null ) 
                    _fhs[i].setValue( object, temp );
                else
                    _fhs[i].setValue( object, null );
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
                    temp = tx.load( fieldEngine, fieldClassMolder, fields[i], null );
                    _fhs[i].setValue( object, temp );
                } else {
                    _fhs[i].setValue( object, null );
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
                    ArrayList col = new ArrayList();
                    ArrayList v = (ArrayList)fields[i];
                    if ( v != null ) {
                        for ( int j=0,l=v.size(); j<l; j++ ) {
                            col.add( tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null ) );
                        }
                        _fhs[i].setValue( object, col );
                    } else {
                        _fhs[i].setValue( object, null );
                    }
                } else {
                    // lazy loading is specified. Related object will not be loaded.
                    // A lazy collection with all the identity of the related object
                    // will constructed and set as the data object's field.
                    ArrayList list = (ArrayList) fields[i];
                    RelationCollection relcol = new RelationCollection( tx, oid, fieldEngine, fieldClassMolder, null, list );
                    _fhs[i].setValue( object, relcol );
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
            throw new PersistenceException("non persistence capable: "+oid.getJavaClass());        

        // optimization note: because getObject is an expensive operation, 
        // if this method divided into 3 phase, the performance will be
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
                fields[i] = _fhs[i].getValue( object );
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object );
                if ( o != null ) {
                    fid = fieldClassMolder.getIdentity( o );
                    if ( fid != null ) {
                        fields[i] = fid;
                    }
                }
                break;

            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object );
                if ( o != null ) {
                    fids = getIds( fieldClassMolder, o );
                    fields[i] = fids;
                }
                break;

            case FieldMolder.MANY_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object );
                if ( o != null ) {
                    fids = getIds( fieldClassMolder, o );
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

        // set the new timeStamp into the data object
        if ( object instanceof TimeStampable ) {
            ((TimeStampable)object).jdoSetTimeStamp( locker.getTimeStamp() );
        }

        // set the identity into the object
        setIdentity( object, createdId );

        // iterate all the fields and create all the dependent object.
        for ( int i=0; i<_fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                // nothing need to be done here for primitive
                break;

            case FieldMolder.PERSISTANCECAPABLE:
                // create dependent object if exists
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object );
                if ( o != null ) {
                    if ( _fhs[i].isDependent() ) {
                        if ( !tx.isPersistent( o ) ) 
                            tx.create( fieldEngine, fieldClassMolder, o, oid );
                        else 
                            // fail-fast principle: if the object depend on another object,
                            // throw exception
                            if ( !tx.isDepended( oid, o ) )
                                throw new PersistenceException("Dependent object may not change its master. Object: "+o+" new master: "+oid);
                    } else {
                        //if ( !tx.isPersistent( o ) ) 
                        //    tx.create( fieldEngine, fieldClassMolder, o, null );
                    }
                }
                break;
            case FieldMolder.ONE_TO_MANY:
                // create dependent objects if exists
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object );
                if ( o != null ) {
                    Iterator itor = getIterator( o );
                    while (itor.hasNext()) {
                        Object oo = itor.next();
                        if ( _fhs[i].isDependent() ) {
                            if ( !tx.isPersistent( oo ) ) {
                                tx.create( fieldEngine, fieldClassMolder, oo, oid );
                            } else 
                                // fail-fast principle: if the object depend on another object,
                                // throw exception
                                if ( !tx.isDepended( oid, oo ) )
                                    throw new PersistenceException("Dependent object may not change its master");
                        } else {
                            // only dependent object will be created when 
                            // master object is created. 
                            // user should create all independent object 
                            // manually.

                            // if ( !tx.isPersistent( oo ) ) 
                            //    tx.create( fieldEngine, fieldClassMolder, oo, null );
                        }
                    }
                }
                break;
            case FieldMolder.MANY_TO_MANY:
                // create relation if the relation table
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                o = _fhs[i].getValue( object );
                if ( o != null ) {
                    Iterator itor = getIterator( o );
                    // many-to-many relation is never dependent relation
                    while (itor.hasNext()) {
                        Object oo = itor.next();
                        if ( !tx.isPersistent( oo ) )                         
                            _fhs[i].getRelationLoader().createRelation( 
                            (Connection)tx.getConnection(oid.getLockEngine()),
                            oid.getIdentity(), fieldClassMolder.getIdentity(oo) );
                    }
                }
                break;
            default:
                // should never happen
                throw new IllegalArgumentException("Field type invalid!");
            }
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
    
    private boolean isEquals( Object o1, Object o2 ) {
        
        if ( o1 == o2 )
            return true;
        if ( o1 == null || o2 == null )
            return false;
        if ( o1.equals( o2 ) )
            return true;
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
        boolean modified;
        boolean lockrequired;
        boolean updateCache;

        if ( oid.getIdentity() == null ) 
            throw new PersistenceException("The identity of the object to be stored is null");

        if ( !oid.getIdentity().equals( getIdentity( object ) ) ) 
            throw new PersistenceException("Identity changes is not allowed!");

        fields = (Object[]) locker.getObject( tx );

        if ( fields == null ) 
            throw new PersistenceException("Object, "+oid+",  isn't loaded in the persistence storage!");

        newfields = new Object[_fhs.length];
        modified = false;
        lockrequired = false;
        updateCache = false;

        // iterate thru all the data object fields for modification
        for ( int i=0; i<newfields.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value =  _fhs[i].getValue( object );
                if ( !isEquals( fields[i], value ) ) {
                    updateCache = true;
                    if ( _fhs[i].isStored() ) {
                        if ( _fhs[i].isCheckDirty() ) {
                            modified = true;
                            lockrequired = true;
                        } else {
                            modified = true;
                        }
                    }
                }
                break;
            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object );
                if ( value != null )
                    newfields[i] = fieldClassMolder.getIdentity( value );

                // the following code probably can be optimized if needed
                if ( !isEquals( fields[i], newfields[i] ) ) {
                    updateCache = true;
                    if ( _fhs[i].isStored() ) {
                        if ( _fhs[i].isCheckDirty() ) {
                            modified = true;
                            lockrequired = true;
                        } else {
                            modified = true;
                        }
                    }

                    if ( value != null ) {
                        if ( _fhs[i].isDependent() ) {
                            if ( fields[i].equals( newfields[i] ) ) {
                                if ( fields[i] != null ) { 
                                    Object reldel = tx.fetch( fieldEngine, fieldClassMolder, fields[i], null );
                                    if ( reldel != null ) {
                                        tx.delete( reldel );
                                    } else {
                                        // should i notify user that the object does not exist?
                                        // user can't delete dependent object himself. So, must
                                        // error.                                   
                                    }
                                        
                                }
                                if ( newfields[i] != null ) {
                                    if ( !tx.isPersistent( value ) ) {
                                        // should be created if transaction have no record of the object
                                        tx.create( fieldEngine, fieldClassMolder, value, oid );
                                    } else {
                                        // should i notify user that the object does not exist?
                                        // user can't create dependent object himself. So, must
                                        // error.                                   
                                    }
                                }
                            }
                        } else {
                            // need to do anything for non-dependent object?
                            // it seem not. should list all the case and make sure
                            if ( tx.isPersistent( value ) )
                                fieldClassMolder.removeRelation( tx, value, this, object );
                        }
                    }
                }
                break;
            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object );
                orgFields = (ArrayList)fields[i];
                if ( ! (value instanceof Lazy) ) {
                    // get a list of all of the identities from the object
                    
                    itor = getIterator( value );
                    ArrayList v = (ArrayList) value;
                    list = new ArrayList( v.size() );
                    for ( int j=0; j<v.size(); j++ ) {
                        list.add( fieldClassMolder.getIdentity( v.get(j) ) );
                    }    
                    
                    ArrayList cachedList = (ArrayList)fields[i];
                    if ( !isEquals( cachedList, list ) ) {
                        updateCache = true;
                        if ( _fhs[i].isStored() ) {
                            if ( _fhs[i].isCheckDirty() ) {
                                modified = true;
                                lockrequired = true;
                            } else {
                                modified = true;
                            }
                        }

                        if ( orgFields != null && list != null ) {
                            // remove dereferenced relation
                            for ( int j=0; j<orgFields.size(); j++ ) {
                                if ( !list.contains( orgFields.get(j) ) ) {
                                    Object reldel = tx.fetch( fieldEngine, fieldClassMolder, orgFields.get(j), null );
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
                            }
                            // add relation which added after it's created or loaded
                            for ( int j=0; j<list.size(); j++ ) {
                                if ( !orgFields.contains( list.get(j) ) ) {
                                    if ( _fhs[i].isDependent() ) {
                                        if ( !tx.isPersistent( v.get(j) ) ) {
                                            tx.create( fieldEngine, fieldClassMolder, v.get(j), oid );
                                        } else {
                                        // should i notify user that the object does not exist?
                                        // user can't create dependent object himself. So, must
                                        // error.                                   
                                        }
                                    }
                                }
                            }
                        } else if ( orgFields != null ) {
                            for ( int j=0; j<orgFields.size(); j++ ) {
                                Object reldel = tx.fetch( fieldEngine, fieldClassMolder, orgFields.get(j), null );
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
                        } else {    // list != null 
                            for ( int j=0; j<list.size(); j++ ) {
                                if ( _fhs[i].isDependent() ) {
                                    if ( !tx.isPersistent( v.get(j) ) ) {
                                        tx.create( fieldEngine, fieldClassMolder, v.get(j), oid );
                                    } else {
                                    // should i notify user that the object does not exist?
                                    // user can't create dependent object himself. So, must
                                    // error.                                   
                                    }
                                }
                            }
                        }
                        //}
                    }
                } else {
                    RelationCollection lazy = (RelationCollection) value;
                    ArrayList deleted = lazy.getDeleted();
                    if ( deleted != null ) {
                        if ( _fhs[i].isStored() ) {
                            if ( _fhs[i].isCheckDirty() ) {
                                modified = true;
                                lockrequired = true;
                            } else {
                                modified = true;
                            }
                        }

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
                        if ( _fhs[i].isStored() ) {
                            if ( _fhs[i].isCheckDirty() ) {
                                modified = true;
                                lockrequired = true;
                            } else {
                                modified = true;
                            }
                        }

                        if ( _fhs[i].isDependent() ) {
                            itor = added.iterator();
                            while ( itor.hasNext() ) {
                                updateCache = true;
                                Object toBeAdded = lazy.find( itor.next() );
                                if ( toBeAdded != null ) {
                                    tx.create( fieldEngine, fieldClassMolder, toBeAdded, oid );
                                } else {
                                    // what to do if it happens?
                                }
                            }
                        } else {
                        }
                    }

                }
                break;
            case FieldMolder.MANY_TO_MANY:

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object );
                if ( ! (value instanceof Lazy) ) {
                    itor = getIterator( value );

                    ArrayList v = (ArrayList) value;
                    list = new ArrayList( (v==null? 0 : v.size()) );
                    if ( v != null )
                        for ( int j=0; j<v.size(); j++ ) {
                            list.add( fieldClassMolder.getIdentity( v.get(j) ) );
                        }
                    
                    orgFields = (ArrayList)fields[i];
                    if ( !isEquals( orgFields, list ) ) {
                        updateCache = true;
                        itor = getIterator( value );
                        while ( value != null && itor.hasNext() ) {
                            Object newobj = itor.next();

                            if ( !tx.isPersistent( newobj ) ) {
                                // should be created if transaction have no record of the object
                                // (will not be dependent, so don't create it)
                                //if ( _fhs[i].isDependent() ) {
                                //    tx.create( fieldEngine, fieldClassMolder, newobj, oid );
                                //}
                                // create the relation in relation table too
                                _fhs[i].getRelationLoader().createRelation( 
                                (Connection)tx.getConnection(oid.getLockEngine()), 
                                oid.getIdentity(), fieldClassMolder.getIdentity(newobj) );
                            }
                        }

                        // need to add support for add and delete relation
                        // delete relation which no long exist
                        if ( orgFields != null && list != null ) {
                            for ( int j=0; j<orgFields.size(); j++ ) {
                                if ( !list.contains( orgFields.get(j) ) ) {
                                    // must be loaded thur transaction, so that the related object
                                    // is properly locked and updated before we delete it.
                                    Object reldel = tx.load( fieldEngine, fieldClassMolder, orgFields.get(j), null );
                                    if ( reldel != null ) {
                                        tx.writeLock( reldel, tx.getLockTimeout() );
                                     
                                        _fhs[i].getRelationLoader().deleteRelation( 
                                        (Connection)tx.getConnection(oid.getLockEngine()), 
                                        oid.getIdentity(), orgFields.get(j) );

                                        fieldClassMolder.removeRelation( tx, reldel, this, object );
                                    } else {
                                        // the object not there, and we try to delete the rubbish relation,
                                        // if there is
                                        _fhs[i].getRelationLoader().deleteRelation( 
                                        (Connection)tx.getConnection(oid.getLockEngine()), 
                                        oid.getIdentity(), orgFields.get(j) );
                                    }
                                }
                            }
                            // add relation which added after it's created or loaded
                            for ( int j=0; j<list.size(); j++ ) {
                                if ( !orgFields.contains( list.get(j) ) ) {
                                    // must be loaded thur transaction, so that the related object
                                    // is properly locked and updated before we create it.
                                    Object reladd = tx.load( fieldEngine, fieldClassMolder, list.get(j), null );
                                    if ( reladd != null ) {
                                        tx.writeLock( reladd, tx.getLockTimeout() );
                                     
                                        _fhs[i].getRelationLoader().createRelation( 
                                        (Connection)tx.getConnection(oid.getLockEngine()), 
                                        oid.getIdentity(), orgFields.get(j) );
                                    } else {
                                        // ignored if object not found, if later in transaction 
                                        // the other side of object is added. then, the relation 
                                        // will be added if the other side of object is just 
                                        // deleted in this transaction, then it seem to be an 
                                        // non-critical error ignore it seem to better than annoy 
                                        // user
                                    }
                                }
                            }
                        } else if ( orgFields != null ) {
                            for ( int j=0; j<orgFields.size(); j++ ) {
                                // must be loaded thur transaction, so that the related object
                                // is properly locked and updated before we delete it.
                                Object reldel = tx.load( fieldEngine, fieldClassMolder, orgFields.get(j), null );
                                if ( reldel != null ) {
                                    tx.writeLock( reldel, tx.getLockTimeout() );
                                 
                                    _fhs[i].getRelationLoader().deleteRelation( 
                                    (Connection)tx.getConnection(oid.getLockEngine()), 
                                    oid.getIdentity(), orgFields.get(j) );

                                    fieldClassMolder.removeRelation( tx, reldel, this, object );                                   
                                } else {
                                    // the object not there, and we try to delete the rubbish relation,
                                    // if there is
                                    _fhs[i].getRelationLoader().deleteRelation( 
                                    (Connection)tx.getConnection(oid.getLockEngine()), 
                                    oid.getIdentity(), orgFields.get(j) );
                                }
                            }
                        } else {
                            for ( int j=0; j<list.size(); j++ ) {
                                // must be loaded thur transaction, so that the related object
                                // is properly locked and updated before we create it.
                                Object reladd = tx.load( fieldEngine, fieldClassMolder, list.get(j), null );
                                if ( reladd != null ) {
                                    tx.writeLock( reladd, tx.getLockTimeout() );
                                 
                                    _fhs[i].getRelationLoader().createRelation( 
                                    (Connection)tx.getConnection(oid.getLockEngine()), 
                                    oid.getIdentity(), orgFields.get(j) );
                                } else {
                                    // ignored if object not found, if later in transaction 
                                    // the other side of object is added. then, the relation 
                                    // will be added if the other side of object is just 
                                    // deleted in this transaction, then it seem to be an 
                                    // non-critical error ignore it seem to better than annoy 
                                    // user
                                }
                            }
                        }
                    }
                } else {
                    RelationCollection lazy = (RelationCollection) value;
                    ArrayList deleted = lazy.getDeleted();
                    if ( deleted != null ) {
                        if ( _fhs[i].isStored() ) {
                            if ( _fhs[i].isCheckDirty() ) {
                                modified = true;
                                lockrequired = true;
                            } else {
                                modified = true;
                            }
                        }

                        itor = deleted.iterator();
                        while ( itor.hasNext() ) {
                            updateCache = true;
                            Object deletedId = itor.next();
                            Object toBeDeleted = lazy.find( deletedId );
                            if ( toBeDeleted != null && !tx.isPersistent(toBeDeleted) ) {
                                tx.writeLock( toBeDeleted, 0 );

                                _fhs[i].getRelationLoader().deleteRelation( 
                                (Connection)tx.getConnection(oid.getLockEngine()), 
                                oid.getIdentity(), deletedId );

                                fieldClassMolder.removeRelation( tx, toBeDeleted, this, object );
                            } else { 
                                // what to do if it happens?
                            }
                        }
                    }

                    ArrayList added = lazy.getAdded();
                    if ( added != null ) {
                        updateCache = true;
                        if ( _fhs[i].isStored() ) {
                            if ( _fhs[i].isCheckDirty() ) {
                                modified = true;
                                lockrequired = true;
                            } else {
                                modified = true;
                            }
                        }

                        itor = added.iterator();
                        while ( itor.hasNext() ) {
                            Object addedId = itor.next();
                            Object toBeAdded = lazy.find( addedId );
                            if ( toBeAdded != null ) {
                                _fhs[i].getRelationLoader().createRelation( 
                                (Connection)tx.getConnection(oid.getLockEngine()), 
                                oid.getIdentity(), addedId );
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
 

        if ( lockrequired ) {
            //tx.writeLock( object, tx.getTransactionTimeout() );
        }
        tx.markModified( object, modified, updateCache );

        if ( updateCache || modified )
            tx.writeLock( object, timeout );


        if ( getCallback() != null ) {
            try {
                getCallback().storing( object, (updateCache || modified) );
            } catch ( Exception except ) {
                throw new PersistenceException( except.getMessage(), except );
            }
        }

        return updateCache;
        // checkValidity
        // call store of each fieldMolder
        // update oid and setStamp
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

        if ( !oid.getIdentity().equals( getIdentity( object ) ) ) 
            throw new PersistenceException("Identities changes is not allowed!");

        fields = (Object[]) locker.getObject( tx );

        if ( fields == null ) 
            throw new PersistenceException("Object, "+oid+",  isn't loaded in the persistence storage!");

        newfields = new Object[_fhs.length];
        for ( int i=0; i<newfields.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                newfields[i] = _fhs[i].getValue( object );
                break;
            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object );
                if ( value != null ) 
                    newfields[i] = fieldClassMolder.getIdentity( value );
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
     * Update the object which loaded or created in the other transaction to
     * the persistent storage. 
     *
     * @param tx Transaction in action
     * @param oid the object identity of the stored object
     * @param locker the dirty check cache of the object
     * @param object the object to be stored
     */
    public void update( TransactionContext tx, OID oid, DepositBox locker, Object object, AccessMode accessMode )
            throws PersistenceException, ObjectModifiedException {

        ClassMolder fieldClassMolder;
        LockEngine fieldEngine;
        Iterator itor;
        Object[] fields;
        Object ids;
        AccessMode am;
        Object value;
        Object stamp;        
        Object[] temp;
        int fieldType;
        long timestamp;
        Object o;

        fields = (Object[]) locker.getObject( tx );

        if ( fields == null ) {
            if ( !isDependent() ) {
                throw new ObjectModifiedException("Object is cleared from cache.");
            } else {
                fields = new Object[_fhs.length];
                Connection conn = (Connection)tx.getConnection(oid.getLockEngine());
                stamp = _persistence.load( conn, fields, oid.getIdentity(), accessMode );
                oid.setDbLock( accessMode == AccessMode.DbLocked );
                locker.setObject( tx, fields );
            }
        }

        timestamp = locker.getTimeStamp();

        ids = oid.getIdentity();

        // If the object implements TimeStampable interface, verify
        // the object's timestamp against the cache
        if ( object instanceof TimeStampable ) {
            TimeStampable ts = (TimeStampable) object;
            if ( ts.jdoGetTimeStamp() != timestamp ) {
                throw new ObjectModifiedException( "Time stamp mismatched!" );
                    /*Messages.format( "persist.objectModified", object.getClass(), OID.flatten( ids ) ) ); */
            } 
        } else if ( !isDependent() ) {
            throw new IllegalArgumentException("A long transaction object must implement the TimeStampable interface!");
        }

        // load the original field into the transaction. so, store will
        // have something to compare later.
        try {
            for ( int i=0; i <_fhs.length; i++ ) {
                fieldType = _fhs[i].getFieldType();
                switch (fieldType) {
                case FieldMolder.PRIMITIVE:
                    break;
                case FieldMolder.PERSISTANCECAPABLE:
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    if ( _fhs[i].isDependent() ) {
                        // depedent class won't have persistenceInfo in LockEngine
                        // must look at fieldMolder for it

                        // load the cached dependent object from the data store. 
                        // The loaded will be compared with the new one
                        value = tx.load( fieldEngine, fieldClassMolder, fields[i], null );
                        o = _fhs[i].getValue( object );
                        if ( o != null )
                            tx.update( fieldEngine, fieldClassMolder, o, oid );
                    } else {
                        o = _fhs[i].getValue( object );
                        if ( !tx.isPersistent( o ) ) {
                            //tx.update( fieldEngine, fieldClassMolder, o, null );
                        } else {
                            //tx.create( fieldEngine, fieldClassMolder, o, null );
                        }
                    }
                    break;

                case FieldMolder.ONE_TO_MANY:
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    if ( _fhs[i].isDependent() ) {
                        if ( !_fhs[i].isLazy() ) {
                            Collection al = (Collection) _fhs[i].getValue( object );
                            ArrayList v = (ArrayList)fields[i];
                            if ( al != null ) {
                                if ( v != null ) {
                                    Iterator it = al.iterator();
                                    while ( it.hasNext() ) {
                                        Object element = it.next();
                                        if ( v.contains( fieldClassMolder.getIdentity( element ) ) ) {
                                            tx.update( fieldEngine, fieldClassMolder, element, oid );
                                        } else {
                                            tx.create( fieldEngine, fieldClassMolder, element, oid );
                                        }
                                    }
                                }
                            }
                            ArrayList col = new ArrayList();
                            if ( v != null ) {
                                for ( int j=0,l=v.size(); j<l; j++ ) {
                                    // load all the object
                                    tx.load( oid.getLockEngine(), fieldClassMolder, v.get(j), null );
                                }
                            } 
                        } else {
                            ArrayList avlist = (ArrayList) fields[i];
                            fieldClassMolder = _fhs[i].getFieldClassMolder();
                            fieldEngine = _fhs[i].getFieldLockEngine();
                            RelationCollection relcol = new RelationCollection( tx, oid, fieldEngine, fieldClassMolder, null, avlist );
                        }
                    } else {
                        // related object should be update maually
                        /*
                        o = _fhs[i].getValue( object );
                        if ( !tx.isPersistent( o ) ) 
                            //tx.update( fieldEngine, fieldClassMolder, o, null );
                        else 
                            //tx.create( fieldEngine, fieldClassMolder, o, null );
                        */
                    }
                    break;                
                case FieldMolder.MANY_TO_MANY:
                    break;
                }
            }
        } catch ( ObjectNotFoundException e ) {
            throw new ObjectModifiedException("dependent object deleted concurrently");
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
                fields[i] = _fhs[i].getValue( object );
                break;
            case FieldMolder.PERSISTANCECAPABLE:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();
                value = _fhs[i].getValue( object );
                if ( value != null ) {
                    fid = fieldClassMolder.getIdentity( value );
                    if ( fid != null ) {
                        fields[i] = fid;
                    }
                } else {
                    fields[i] = null;
                }
                break;
            case FieldMolder.ONE_TO_MANY:
                fieldClassMolder = _fhs[i].getFieldClassMolder();
                value = _fhs[i].getValue( object );
                if ( value != null ) {
                    if ( !(value instanceof Lazy) ) {
                        fids = getIds( fieldClassMolder, value );
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
                value = _fhs[i].getValue( object );
                if ( value != null ) {
                    if ( !(value instanceof Lazy ) ) {
                        fids = getIds( fieldClassMolder, value );
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

                    Object fobject = _fhs[i].getValue( object );
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
                    for ( int j=0; j<alist.size(); j++ ) {
                        Object fid =  alist.get(j);
                        Object fetched = null;
                        if ( fid != null ) {    
                            fetched = tx.fetch( fieldEngine, fieldClassMolder, fid, null );
                            if ( fetched != null ) 
                                tx.delete( fetched );
                        }
                    }

                    ArrayList blist = (ArrayList) _fhs[i].getValue( object );
                    for ( int j=0; j<blist.size(); j++ ) {
                        Object fobject = blist.get(j);
                        if ( fobject != null && tx.isPersistent( fobject ) ) {
                            tx.delete( fobject );
                        }
                    }
                } else {
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

                    ArrayList blist = (ArrayList) _fhs[i].getValue( object );
                    for ( int j=0; j<blist.size(); j++ ) {
                        Object fobject = blist.get(j);
                        if ( fobject != null && tx.isPersistent( fobject ) ) {
                            fieldClassMolder.removeRelation( tx, fobject, this, object );
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

                ArrayList blist = (ArrayList) _fhs[i].getValue( object );
                for ( int j=0; j<blist.size(); j++ ) {
                    Object fobject = blist.get(j);
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

        setIdentity( object, oid.getIdentity() );

        for ( int i=0; i < _fhs.length; i++ ) {
            fieldType = _fhs[i].getFieldType();
            switch (fieldType) {
            case FieldMolder.PRIMITIVE:
                Object temp = fields[i];
                break;
            case FieldMolder.PERSISTANCECAPABLE:

                fieldClassMolder = _fhs[i].getFieldClassMolder();
                fieldEngine = _fhs[i].getFieldLockEngine();

                if ( fields[i] != null ) {
                    // will load() work as we wanted if the the 
                    // persistenceCapable field was deleted in
                    // the transaction?
                    value = tx.load( fieldEngine, fieldClassMolder, fields[i], null );
                    _fhs[i].setValue( object, value );
                } else {
                    _fhs[i].setValue( object, null );
                }
                break;
            case FieldMolder.ONE_TO_MANY:
            case FieldMolder.MANY_TO_MANY:
                Object o = fields[i];
                if ( o == null ) {
                    _fhs[i].setValue( object, null );
                } else if ( !(o instanceof Lazy) ) {
                    ArrayList col = new ArrayList();
                    //(ArrayList)_fhs[i].getCollectionType().newInstance();
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();

                    ArrayList v = (ArrayList)fields[i];
                    for ( int j=0,l=v.size(); j<l; j++ ) {
                        col.add( tx.load( fieldEngine, fieldClassMolder, v.get(j), null ) );
                    }
                    _fhs[i].setValue( object, col );
                } else {
                    ArrayList list = (ArrayList) fields[i];
                    fieldClassMolder = _fhs[i].getFieldClassMolder();
                    fieldEngine = _fhs[i].getFieldLockEngine();
                    RelationCollection relcol = new RelationCollection( tx, oid, fieldEngine, fieldClassMolder, null, list );
                    _fhs[i].setValue( object, relcol );
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
     * Return a new instance of the base class
     *
     */
    public Object newInstance() {
        try {
            return _base.newInstance();
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
     *
     * @param o - object of the base type
     * return an Object[] contains the identity of the object
     */
    public Object getIdentity( Object o ) {
        Object temp;
        if ( _ids.length == 1 ) {
            return _ids[0].getValue( o );
        } else if ( _ids.length == 2 ) {
            temp = _ids[0].getValue( o );
            return temp==null? null: new Complex( temp, _ids[1].getValue( o ) );
        } else {
            Object[] osIds = new Object[_ids.length];
            for ( int i=0; i<osIds.length; i++ ) {
                osIds[i] = _ids[i].getValue( o );
            }
            if ( osIds[0] != null )
                return null;
            else
                return new Complex( osIds );
        }
    }

    /**
     * Set the identity into an object 
     *
     * @param object 
     * @param identity 
     */
    public void setIdentity( Object object, Object identity ) 
            throws PersistenceException {
                
        if ( _ids.length > 1 ) {
            if ( identity instanceof Complex ) {
                Complex com = (Complex) identity;
                if ( com.size() != _ids.length )
                    throw new PersistenceException( "Complex size mismatched!" );
                
                for ( int i=0; i<_ids.length; i++ ) {
                    _ids[i].setValue( object, com.get(i) );
                }
            }
        } else {
            if ( identity instanceof Complex )
                throw new PersistenceException( "Complex type not accepted!" );
            
            _ids[0].setValue( object, identity );
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
     * Get the base type of this ClassMolder
     *
     */
    public Class getJavaClass() {
        return _base;
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
     * Mutator method to set the deepends ClassMolder
     *
     */
    void setDepends( ClassMolder dep ) {
        _depends = dep;
        dep.addDependent( this );
    }

    /**
     * Return the iterator of a Collection
     *
     * @param o - a Collection
     */
    private Iterator getIterator( Object o ) {
        if ( o instanceof Collection ) {
            return ((Collection) o).iterator();
        } else if ( o instanceof Vector ) {
            return null;            
        } else {
            return null;
        }
    }

    /**
     * Return all the object identity of a Collection of object of the same 
     * type.
     * 
     * @param molder - class molder of the type of the objects
     * @param o - a Collection or Vector containing 
     * @return a list of <tt>Object[]</tt>s which contains object identity
     */
    private ArrayList getIds( ClassMolder molder, Object o ) {
        ArrayList v = null;
        Vector vo;
        Iterator i;
        Collection c;
        Enumeration e;
        if ( o instanceof Collection ) {
            c = (Collection) o ;
            i = c.iterator();
            v = new ArrayList( c.size() );
            while ( i.hasNext() ) {
                v.add( molder.getIdentity( i.next() ) );
            }
        } else if ( o instanceof Vector ) {
            vo = (Vector) o;
            e = vo.elements();
            v = new ArrayList( vo.size() );
            while ( e.hasMoreElements() ) {
                v.add( molder.getIdentity( e.nextElement() ) );
            }
        } else if ( o != null ) {
            v = null;
            throw new RuntimeException("expecting collection");
        }
        return v;
    }

    public String toString() {
        return "ClassMolder "+ _base.getName();
    }

    /**
     * Return true if a key generator is used for the base type of this ClassMolder
     */
    public boolean isKeyGeneratorUsed() {
        return _isKeyGenUsed;
    }
}



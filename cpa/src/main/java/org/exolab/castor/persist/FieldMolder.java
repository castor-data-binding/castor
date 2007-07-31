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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.util.ClassLoadingUtils;
import org.castor.util.Messages;
import org.exolab.castor.jdo.DataObjectAccessException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.types.SqlDirtyType;


/**
 * FieldMolder represents a field of a data object class. It is used by
 * ClassMolder to set and get the value from a field of a data object.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public class FieldMolder {

    // accepted collection types
    private static final String COLLECTION_TYPE_ARRAY = "array";
    private static final String COLLECTION_TYPE_COLLECTION = "collection";
    private static final String COLLECTION_TYPE_VECTOR = "vector";
    private static final String COLLECTION_TYPE_ARRAYLIST = "arraylist";
    private static final String COLLECTION_TYPE_HASHTABLE = "hashtable";
    private static final String COLLECTION_TYPE_MAP = "map";
    private static final String COLLECTION_TYPE_SET = "set";
    private static final String COLLECTION_TYPE_HASHMAP = "hashmap";
    private static final String COLLECTION_TYPE_HASHSET = "hashset";
    private static final String COLLECTION_TYPE_SORTED_SET = "sortedset";
    private static final String COLLECTION_TYPE_ITERATOR = "iterator";
    private static final String COLLECTION_TYPE_ENUMERATON = "enumerate";
    private static final String COLLECTION_TYPE_SORTED_MAP = "sortedmap";
    
    private static final String FIELD_TYPE_SERIALIZABLE = "serializable";
    
    public static final short PRIMITIVE = 0;
    public static final short SERIALIZABLE = 1;
    public static final short PERSISTANCECAPABLE = 2;
    public static final short ONE_TO_MANY = 3;
    public static final short MANY_TO_MANY = 4;

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(FieldMolder.class);

    // method prefixes
    private static final String METHOD_GET_PREFIX = "get";
    private static final String METHOD_IS_PREFIX = "is";
    private static final String METHOD_SET_PREFIX = "set";
    private static final String METHOD_ADD_PREFIX = "add";
    private static final String METHOD_CREATE_PREFIX = "create";
    private static final String METHOD_DELETE_PREFIX = "delete";
    private static final String METHOD_HAS_PREFIX = "has";
    
    private boolean _lazy;

    private boolean _check;

    private boolean _store;

    private boolean _multi;

    private boolean _serial;

    private boolean _addable;

    private ClassMolder _eMold;

    private ClassMolder _fMold;

    private Class _colClass;

    private String _fType;

    private String        _fieldName;

    private Object        _default;

    private boolean _readonly;
    
    /**
     * Indicates whether this field has been flagged as transient, i.e. not to be considered 
     * during any persistence operations.
     */
    private boolean _transient;
    
    /**
     * Specifies the jav.autil.Comparator instance to use with a SortedSet/SortedMap collection.
     */
    private String _comparator;

    /**
     * Collection of reflection service keyed by ClassLoader instance.
     */
    private HashMap _reflectServices;

    /**
     * Default reflection service
     */
    private ReflectService _defaultReflectService;

    private SQLRelationLoader _manyToManyLoader;

    public String toString() {
        return "FieldMolder for " + _eMold.getName() + "." + _fieldName + " of type " + _fType;
    }

    public String getName() {
        return _fieldName;
    }
    
    /**
     * Returns the java.util.Comparator instance to be used with SortedSets; null, if not specified.
     * @return the java.util.Comparator instance to be used with SortedSets
     */
    public String getComparator() {
        return _comparator;
    }

    /*
    void setRelationDescriptor( RelationDescriptor rd ) throws MappingException {
        _loader = new SQLRelationLoader( rd, _eMold.getName() );
    }
    public SQLRelationLoader getRelationLoader() {
        return _loader;
    }*/
    public short getFieldType() {
        if (!isPersistanceCapable()) {
            return isSerializable() ? SERIALIZABLE : PRIMITIVE;
        }
 
        if (!isMulti()) {
            return PERSISTANCECAPABLE;
        }

        if (!isManyToMany()) {
            return ONE_TO_MANY;
        }

        return MANY_TO_MANY;
    }

    public SQLRelationLoader getRelationLoader() {
        return _manyToManyLoader;
    }

    public boolean isStored() {
        return _fMold == null || _store;
    }

    public boolean isManyToMany() {
        return _manyToManyLoader != null;
    }
    public boolean isDependent() {
        if (_fMold == null) {
            return false;
        }
        ClassMolder extendPath = _eMold;
        ClassMolder depends = _fMold.getDepends();
        while (extendPath != null) {
            if (extendPath == depends) {
                return true;
            } 
            extendPath = extendPath.getExtends();
        }
        return false;
    }

    public boolean isMulti() {
        return _multi;
    }

    public boolean isPersistanceCapable() {
        return _fMold != null;
    }

    public boolean isSerializable() {
        return _serial;
    }

    public boolean isCheckDirty() {
        return _check;
    }
    public boolean isLazy() {
        return _lazy;
    }
    public boolean isAddable() {
        return _addable;
    }
    public boolean isTransient() {
        return _transient;
    }
    void setFieldClassMolder(final ClassMolder fMold) {
        _fMold = fMold;
    }
    public ClassMolder getEnclosingClassMolder() {
        return _eMold;
    }

    public ClassMolder getFieldClassMolder() {
        return _fMold;
    }

    public LockEngine getFieldLockEngine() {
        return (_fMold == null) ? null : _fMold.getLockEngine();
    }

    public boolean isReadonly() {
        return _readonly;
    }

    public Class getCollectionType() {
        return _colClass;
    }

    public Object getValue(final Object object, final ClassLoader loader) {
        Object internalObject = object;
        ReflectService rf = getContextReflectService(loader);
        try {
            // If field is accessed directly, get it's value, if not
            // need to call get method. It's possible to not have a
            // way to access the field.
            //if ( _handler != null )
            //    value = _handler.getValue( object );
            //else
            if (rf._field != null) {
                return rf._field.get(internalObject);
            } else if (rf._getMethod != null) {
                if (rf._getSequence != null) {
                    for (int i = 0; i < rf._getSequence.length; i++) {
                        internalObject = rf._getSequence[i].invoke(internalObject, (Object[]) null);
                        if (internalObject == null) {
                            break;
                        }
                    }
                }
                // If field has 'has' method, false means field is null
                // and do not attempt to call getValue. Otherwise,
                if ((internalObject == null) || ((rf._hasMethod != null) && !((Boolean) rf._hasMethod.invoke(internalObject, (Object[]) null)).booleanValue())) {
                    return null;
                }
                return rf._getMethod.invoke(internalObject, (Object[]) null);
            } else {
                return null;
            }
        } catch (IllegalAccessException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format("mapping.schemaChangeNoAccess", toString()), except);
        } catch (InvocationTargetException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format("mapping.schemaChangeInvocation",
                                                              toString(), except), except);
        }
    }

    public void addValue(final Object object, final Object value, final ClassLoader loader) {

        ReflectService rf = getContextReflectService(loader);
        
        if (_log.isDebugEnabled()) {
            _log.debug("Calling " + rf._addMethod.getName() + " on " + object.getClass().getName() + " with value " + value);
        }
        
        try {
            if (rf._addMethod == null) {
                throw new DataObjectAccessException(Messages.format("mapping.addMethodNotDefined", this.getName()));
            }

            if (value == null) {
                throw new NullPointerException("Adding null value is not allowed");
            }

            rf._addMethod.invoke(object, new Object[] {value});
        } catch (IllegalArgumentException e) {
            throw new DataObjectAccessException("Argument ," + value + ", cannot be added!", e);
        } catch (IllegalAccessException e) {
            throw new DataObjectAccessException("Field access error", e);
        } catch (InvocationTargetException e) {
            throw new DataObjectAccessException("Field invocation error", e);
        }
    }

    public void setValue(final Object object, final Object value, final ClassLoader loader) {
        Object internalObject = object;
        // If there is a convertor, apply conversion here.
        ReflectService rf = getContextReflectService(loader);
        try {
            //if ( _handler != null )
            //    _handler.setValue( object, value );
            //else
            if (rf._field != null) {
                rf._field.set(internalObject, (value == null) ? _default : value);
            } else if (rf._setMethod != null) {

                if (rf._getSequence != null) {
                    for (int i = 0; i < rf._getSequence.length; i++) {
                        Object last;

                        last = internalObject;
                        internalObject = rf._getSequence[i].invoke(internalObject, (Object[]) null);
                        if (internalObject == null) {
                            // if the value is not null, we must instantiate
                            // the object in the sequence
                            if ((value == null) || (rf._setSequence[i] == null)) {
                                break;
                            }
                            internalObject = Types.newInstance(rf._getSequence[i].getReturnType());
                            rf._setSequence[i].invoke(last, new Object[] {internalObject});
                        }
                    }
                }
                if (internalObject != null) {
                    if ((value == null) && (rf._deleteMethod != null)) {
                        rf._deleteMethod.invoke(internalObject, (Object[]) null);
                    } else {
                        rf._setMethod.invoke(internalObject, new Object[] {(value == null) ? _default : value});
                    }
                }
            } else {
                throw new DataObjectAccessException("no method to set value for field: " + _fType + " in class: " + _eMold);
            }
            // If the field has no set method, ignore it.
            // If this is a problem, identity it someplace else.
        } catch (IllegalArgumentException except) {
            // Graceful way of dealing with unwrapping exception
            if (value == null) {
                throw new DataObjectAccessException(Messages.format("mapping.typeConversionNull", toString()));
            }
            throw new DataObjectAccessException(Messages.format("mapping.typeConversion", toString(), value.getClass().getName()));
        } catch (IllegalAccessException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format("mapping.schemaChangeNoAccess", toString()), except);
        } catch (InvocationTargetException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format("mapping.schemaChangeInvocation",
                                                              toString(), except.getMessage()), except);
        }
    }

    /**
     * Check if the specified value is the default value of the Field
     * represented by this FieldMolder
     */
    public boolean isDefault(final Object value) {
        if (_default == value) {
            return true;
        }

        if (_default == null) {
            return false;
        }

        if (_default.equals(value)) {
            return true;
        }

        return false;
    }

    // ======================================================
    //  copy from FieldHanlder.java and modified
    // ======================================================
    protected Class getCollectionType(final String coll, final boolean lazy) {
        /* 
         * Class type;
         */

        for (int i = 0; i < INFO.length; i++) {
            if (INFO[i]._name.equals(coll)) {
                return INFO[i]._type;
            }
        }
        return null;
        /*
        if ( "collection".equals( coll ) )
            type = ArrayList.class;
        else if ( "vector".equals( coll ) )
            type = Vector.class;
        else
            throw new MappingException( "Unsupported collection type: " + coll );

        if ( lazy )
            return null;
        else
            return type;*/
    }

    static class CollectionInfo {
        String _name;
        Class _type;
        CollectionInfo(final String name, final Class type) {
            this._name = name;
            this._type = type;
        }
    }

    private static final CollectionInfo[] INFO =
        {new CollectionInfo(COLLECTION_TYPE_COLLECTION, java.util.Collection.class),
         new CollectionInfo(COLLECTION_TYPE_VECTOR, java.util.Vector.class),
         new CollectionInfo(COLLECTION_TYPE_ARRAYLIST, java.util.ArrayList.class),
         new CollectionInfo(COLLECTION_TYPE_HASHTABLE, java.util.Hashtable.class),
         new CollectionInfo(COLLECTION_TYPE_HASHMAP, java.util.HashMap.class),
         new CollectionInfo(COLLECTION_TYPE_SET, java.util.Set.class),
         new CollectionInfo(COLLECTION_TYPE_HASHSET, java.util.HashSet.class),
         new CollectionInfo(COLLECTION_TYPE_MAP, java.util.Map.class),
         new CollectionInfo(COLLECTION_TYPE_ARRAY, Object[].class),
         new CollectionInfo(COLLECTION_TYPE_SORTED_SET, java.util.SortedSet.class),
         new CollectionInfo(COLLECTION_TYPE_ITERATOR, java.util.Iterator.class),
         new CollectionInfo(COLLECTION_TYPE_ENUMERATON, java.util.Enumeration.class),
         new CollectionInfo(COLLECTION_TYPE_SORTED_MAP, java.util.SortedMap.class)};

          //( array | vector | hashtable | collection | set | map )


    /**
     * Creates a single field descriptor. The field mapping is used to
     * create a new stock {@link FieldMolder}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param eMold The ClassMolder to which the field belongs
     * @param fieldMap The field mapping information
     * @throws MappingException The field or its accessor methods are not
     *  found, not accessible, not of the specified type, etc
     */
    public FieldMolder(final DatingService ds, final ClassMolder eMold, final FieldMapping fieldMap, final String manyTable,
            final String[] idSQL, final int[] idType, final TypeConvertor[] idTo, final TypeConvertor[] idFrom, final String[] idParam,
            final String[] relatedIdSQL, final int[] relatedIdType, final TypeConvertor[] ridTo, final TypeConvertor[] ridFrom, final String[] ridParam)
            throws MappingException {

        this(ds, eMold, fieldMap);

        _manyToManyLoader = new SQLRelationLoader(manyTable,
                idSQL, idType, idTo, idFrom, idParam,
                relatedIdSQL, relatedIdType, ridTo, ridFrom, ridParam);
    }

    public FieldMolder(final DatingService ds, final ClassMolder eMold, final FieldMapping fieldMap)
            throws MappingException {

        try {
            // create the reflection service with the ClassLoader hold in the
            // SatingService object as default
            _defaultReflectService = new ReflectService();
            _reflectServices = new HashMap();

            // Set enclosing ClassMolder
            _eMold = eMold;

            // Set isLazy
            _lazy = fieldMap.getLazy();

            if ((fieldMap.getSql() == null)
                    || (fieldMap.getSql().getDirty() == null)
                    || !fieldMap.getSql().getDirty().equals(SqlDirtyType.IGNORE)) {
                _check = true;
            }

            if (FIELD_TYPE_SERIALIZABLE.equals(fieldMap.getType())) {
                _serial = true;
            }

            if (fieldMap.getSql() == null) {
                _store = false;
            } else if (fieldMap.getSql().getManyTable() != null) {
                _store = false;
            } else if (fieldMap.getSql().getName().length == 0) {
                _store = false;
            } else {
                _store = true;
            }

            if (fieldMap.getSql() != null) {
                _readonly = fieldMap.getSql().getReadOnly();
            }

            // check if comparator is specified, and if so, use it
            if (fieldMap.getComparator() != null) {
                _comparator = fieldMap.getComparator();
            }
            
            // check whether complete field is declared transient
            _transient = fieldMap.getTransient();
            
            if (fieldMap.getSql() != null) {
                boolean isSQLTransient = fieldMap.getSql().getTransient();
                if (_transient && !isSQLTransient) {
                    throw new MappingException (Messages.message("persist.transient.conflict"));
                }
                _transient = isSQLTransient;  
            }
            
            if (fieldMap.getCollection() != null) {
                _multi = true;
            }

            // Set collection type
            if (fieldMap.getCollection() != null) {
              // simple arrays support
              if (COLLECTION_TYPE_ARRAY.equals(fieldMap.getCollection().toString())) {
                String arrayClassName = "[L" + fieldMap.getType() + ";";
                try {
                  _colClass = ds.resolve(arrayClassName);
                } catch (ClassNotFoundException e) {
                  throw new MappingException("mapping.classNotFound",
                                             arrayClassName);
                }

              } else {
                _colClass = getCollectionType(fieldMap.getCollection().toString(),
                                              _lazy);
                
                if (_colClass != SortedSet.class && _comparator != null) {
                    throw new MappingException (Messages.message("mapping.wrong.use.of.comparator"));                
                }
              }
              _store = false;
            }
            // Set field name, if it is null, we try to discover it with
            // return type of set/get method.
            _fType = fieldMap.getType();

            Class javaClass;
            try {
                javaClass = ds.resolve(eMold.getName());
            } catch (ClassNotFoundException e) {
                throw new MappingException("mapping.classNotFound", eMold.getName());
            }
            // ssa, multi classloader feature
            // set the default classloader to the hash table
            // ssa, FIXME : Shoudln't we have a ref to the Classloader used
            // instead of asking it to the newly created class ?
            _defaultReflectService._loader = javaClass.getClassLoader();
            if (null != _defaultReflectService._loader) {
                _reflectServices.put (_defaultReflectService._loader, this._defaultReflectService);
            }

            String fieldName = fieldMap.getName();
            String fieldType = fieldMap.getType();
            Class  declaredClass = null;
            if (fieldType != null) {
                try {
                    declaredClass = Types.typeFromName(javaClass.getClassLoader(), fieldType);
                    _defaultReflectService._fClass = declaredClass;
                } catch (ClassNotFoundException cnfe) {
                    throw new MappingException("mapping.classNotFound", declaredClass);
                }
            }

            if (fieldMap.getDirect()) {

                // No accessor, map field directly.
                Class  fieldClass = (_colClass != null) ? _colClass : null;
                _defaultReflectService._field = findField(javaClass, fieldName, fieldClass);
                if (_defaultReflectService._field == null) {
                    throw new MappingException(Messages.format("mapping.fieldNotAccessible", fieldName, javaClass.getName()));
                }
                _defaultReflectService._fClass = _defaultReflectService._field.getType();
                if ((_defaultReflectService._field.getModifiers() != Modifier.PUBLIC) && (_defaultReflectService._field.getModifiers() != (Modifier.PUBLIC | Modifier.VOLATILE))) {
                    throw new MappingException(Messages.format("mapping.fieldNotAccessible", _defaultReflectService._field.getName(), _defaultReflectService._field.getDeclaringClass().getName()));
                }
            } else if ((fieldMap.getGetMethod() == null) && (fieldMap.getSetMethod() == null)) {

                // Container object, map field to fields of the container
                int point;
                ArrayList getSeq = new ArrayList();
                ArrayList setSeq = new ArrayList();
                String name = fieldMap.getName();
                Class last;
                Method method = null;
                String methodName = null;

                try {
                    while (true) {
                        point = name.indexOf('.');
                        if (point < 0) {
                            break;
                        }
                        last = javaClass;
                        
                        if (fieldMap.getType().compareTo("boolean") == 0) {
                            try {
                                methodName = METHOD_IS_PREFIX + capitalize(name.substring(0, point));
                                method = javaClass.getMethod(methodName, (Class[]) null);
                            } catch (NoSuchMethodException nsme) {
                                if (_log.isDebugEnabled()) {
                                    _log.debug (Messages.format("mapping.accessorNotFound", methodName, "boolean", getName()));
                                }
                            }
                        }
                        
                        if (method == null) {
                            methodName = METHOD_GET_PREFIX + capitalize(name.substring(0, point));
                            method = javaClass.getMethod(methodName, (Class[]) null);
                        }
                        
                        name = name.substring(point + 1);
                        // Make sure method is not abstract/static
                        // (note: Class.getMethod() returns only public methods).
                        if (((method.getModifiers() & Modifier.ABSTRACT) != 0) || ((method.getModifiers() & Modifier.STATIC) != 0)) {
                            throw new MappingException("mapping.accessorNotAccessible", methodName, javaClass.getName());
                        }
                        getSeq.add(method);
                        javaClass = method.getReturnType();
                        // setter;   Note: javaClass already changed, use "last"
                        if (fieldMap.getType().compareTo("boolean") == 0) {
                            methodName = METHOD_SET_PREFIX + methodName.substring(2);
                        } else {
                            methodName = METHOD_SET_PREFIX + methodName.substring(3);
                        }

                        try {
                            method = last.getMethod(methodName, new Class[] {javaClass});
                            if (((method.getModifiers() & Modifier.ABSTRACT) != 0) || ((method.getModifiers() & Modifier.STATIC) != 0)) {
                                method = null;
                            }
                        } catch (Exception except) {
                            method = null;
                        }
                        setSeq.add(method);
                        method = null;
                        
                    }
                } catch (Exception ex) {
                    throw new MappingException(Messages.format ("mapping.accessorNotFound",
                           methodName, null, javaClass.getName()), ex);
                }
                if (getSeq.size() > 0) {
                    _defaultReflectService._getSequence = (Method[]) getSeq.toArray(new Method[getSeq.size()]);
                    _defaultReflectService._setSequence = (Method[]) setSeq.toArray(new Method[setSeq.size()]);
                }
                Class methodClass = (_colClass != null) ? _colClass : declaredClass;
                _defaultReflectService._getMethod = null;
                
                // if field is of type boolean, check whether is<Field>() is defined.
                if (fieldMap.getType() != null && fieldMap.getType().compareTo("boolean") == 0) {
                    _defaultReflectService._getMethod = 
                        findAccessor(javaClass, METHOD_IS_PREFIX + capitalize(name), methodClass, true);
                }
                
                if (_defaultReflectService._getMethod == null) {
                    _defaultReflectService._getMethod = 
                        findAccessor(javaClass, METHOD_GET_PREFIX + capitalize(name), methodClass, true);
                }

                if (_defaultReflectService._getMethod == null) {
                    if (fieldMap.getType().compareTo("boolean") == 0) {
                        throw new MappingException("mapping.accessorNotFound",
                                METHOD_GET_PREFIX + "/" + METHOD_IS_PREFIX + capitalize(name), fieldMap.getType(), eMold.getName());
                    }
                    throw new MappingException("mapping.accessorNotFound",
                            METHOD_GET_PREFIX + capitalize(name), fieldMap.getType(), eMold.getName());
                }
                
                // update fClass, because we can't tell between primitive
                // and primitive wrapper from the mapping
                if (_colClass == null) {
                    _defaultReflectService._fClass = _defaultReflectService._getMethod.getReturnType();
                }

                _defaultReflectService._setMethod = findAccessor(javaClass, METHOD_SET_PREFIX + capitalize(name), methodClass, false);

                if (_defaultReflectService._setMethod == null) {
                    _defaultReflectService._addMethod = findAccessor(javaClass, METHOD_ADD_PREFIX + capitalize(name), declaredClass, false);

                    // look again, but this time without a trailing 's'
                    if ((_defaultReflectService._addMethod == null) && (name.endsWith("s"))) {
                        _defaultReflectService._addMethod = findAccessor(javaClass, METHOD_ADD_PREFIX + capitalize(name).substring(0, name.length() - 1), declaredClass, false);
                    }

                    // if add<FieldName>() has been found, set _addable to true 
                    if (_defaultReflectService._addMethod != null) {
                        _addable = true;
                    }
                }
                
                if ((_defaultReflectService._setMethod == null) && (_defaultReflectService._addMethod == null)) {
                    throw new MappingException("mapping.accessorNotFound", METHOD_SET_PREFIX + "/" + METHOD_ADD_PREFIX + capitalize(name), declaredClass, javaClass.getName());
                }
                
            } else {

                // Bean type object, map field to get<Method>/set<Method>

                Class methodClass = _defaultReflectService._fClass;

                // First look up the get accessors
                if (fieldMap.getGetMethod() != null) {
                    if (_colClass != null) {
                        _defaultReflectService._getMethod
                            = findAccessor(javaClass, fieldMap.getGetMethod(), _colClass, true);
                    } else {
                        _defaultReflectService._getMethod
                            = findAccessor(javaClass, fieldMap.getGetMethod(), methodClass, true);
                    }

                    if (_defaultReflectService._getMethod == null) {
                        throw new MappingException("mapping.accessorNotFound", fieldMap.getGetMethod(), methodClass, javaClass.getName());
                    }

                    // set/reset the fClass to actual field class
                    if (_colClass == null) {
                        _defaultReflectService._fClass = _defaultReflectService._getMethod.getReturnType();
                    }

                } else {
                    throw new MappingException("mapping.getMethodMappingNotFound",
                                                (_colClass != null) ? _colClass : methodClass, javaClass.getName());
                }

                // Second look up the set/add accessor
                if (fieldMap.getSetMethod() != null) {

                    if (_colClass != null) {
                        _defaultReflectService._setMethod = findAccessor(javaClass, fieldMap.getSetMethod(), _colClass, false);

                        // find addXXX method only if lazy loading is turned off
                        if ((_defaultReflectService._setMethod == null) && !fieldMap.getLazy()) {
                            _defaultReflectService._addMethod = 
                                findAccessor(javaClass, fieldMap.getSetMethod(), declaredClass, false);
                            if (_defaultReflectService._addMethod != null) {
                                _addable = true;
                            }
                        }

                    } else {
                        // find setXXX method
                        _defaultReflectService._setMethod = findAccessor(javaClass, fieldMap.getSetMethod(), methodClass, false);
                    }


                    if (_defaultReflectService._setMethod == null && _defaultReflectService._addMethod == null) {
                        throw new MappingException("mapping.accessorNotFound", fieldMap.getSetMethod(), methodClass, javaClass.getName());
                    }

                        if (_defaultReflectService._fClass == null) {
                            _defaultReflectService._fClass = _defaultReflectService._setMethod.getParameterTypes()[0];
                        }
                    } else {
                        throw new MappingException("mapping.setMethodMappingNotFound", (_colClass != null) ? _colClass : methodClass, javaClass.getName());
                }
            }


            // If there is a create method, add it to the field handler

            // Note: create method is used for enclosing object of this field to determine
            // what exact instance to be created.
            if (fieldMap.getCreateMethod() != null) {
                try {
                    _defaultReflectService._createMethod = javaClass.getMethod(fieldMap.getCreateMethod(), (Class[]) null);
                } catch (Exception except) {
                    // No such/access to method
                    throw new MappingException("mapping.createMethodNotFound",
                                                fieldMap.getCreateMethod(), javaClass.getName());
                }
            } else if ((fieldMap.getName() != null) && !Types.isSimpleType(_defaultReflectService._fClass)) {
                try {
                    Method method;

                    method = javaClass.getMethod(METHOD_CREATE_PREFIX + capitalize(fieldMap.getName()), (Class[]) null);
                    _defaultReflectService._createMethod = method;
                } catch (Exception except) {
                    // no explicit exception handling
                }
            }

            // If there is an has/delete method, add them to field handler
            if (fieldMap.getName() != null) {
                Method hasMethod = null;
                Method deleteMethod = null;

                try {
                    hasMethod = javaClass.getMethod(METHOD_HAS_PREFIX + capitalize(fieldMap.getName()), (Class[]) null);
                    if (((hasMethod.getModifiers() & Modifier.PUBLIC) == 0) || ((hasMethod.getModifiers() & Modifier.STATIC) != 0)) {
                        hasMethod = null;
                    }
                    try {
                        if (((hasMethod.getModifiers() & Modifier.PUBLIC) == 0) || ((hasMethod.getModifiers() & Modifier.STATIC) != 0)) {
                            deleteMethod = null;
                        }
                        deleteMethod = javaClass.getMethod(METHOD_DELETE_PREFIX + capitalize(fieldMap.getName()), (Class[]) null);
                    } catch (Exception except) {
                        // no explicit exception handling 
                    }
                    _defaultReflectService._hasMethod = hasMethod;
                    _defaultReflectService._deleteMethod = deleteMethod;
                } catch (Exception except) {
                    // no explicit exception handling
                }
            }

            if ((_defaultReflectService._field == null) && (_defaultReflectService._setMethod == null) && (_defaultReflectService._getMethod == null)) {
                throw new MappingException("_field or _setMethod can't be created");
            }

            ds.pairFieldClass(this, _fType);
        } catch (NullPointerException e) {
            _log.fatal("Caught unexpected NullPointerException: ", e);
            throw new MappingException("Unexpected Null pointer!\n" + e);
        }
        _fieldName = fieldMap.getName();

        // If the field is of a primitive type we use the default value
        _default = Types.getDefault(_defaultReflectService._fClass);
        // make the default to null for wrappers of primitives
        if (!_defaultReflectService._fClass.isPrimitive()) {
            _default = null;
        }

    }

    /**
     * Returns the named field. Uses reflection to return the named
     * field and check the field type, if specified.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldName The name of the field
     * @param internalFieldType The type of the field if known, or null
     * @return The field, null if not found
     * @throws MappingException The field is not accessible or is not of the
     *  specified type
     */
    private Field findField(final Class javaClass, final String fieldName, final Class fieldType) throws MappingException {
        Class internalFieldType = fieldType;
        Field field;

        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            field = javaClass.getField(fieldName);
            if ((field.getModifiers() != Modifier.PUBLIC) && (field.getModifiers() != (Modifier.PUBLIC | Modifier.VOLATILE))) {
                throw new MappingException("mapping.fieldNotAccessible", fieldName, javaClass.getName());
            }
            if (internalFieldType == null) {
                internalFieldType = Types.typeFromPrimitive(field.getType());
            } else if (Types.typeFromPrimitive(internalFieldType) != Types.typeFromPrimitive(field.getType())) {
                throw new MappingException("mapping.fieldTypeMismatch", field, internalFieldType.getName());
            }
            return field;
        } catch (NoSuchFieldException except) {
            // no explicit exception handling
        } catch (SecurityException except) {
            // no explicit exception handling
        }
        return null;
    }


    /**
     * Returns the named accessor. Uses reflection to return the named
     * accessor and check the return value or parameter type, if
     * specified.
     * 
     * TODO Most of this code appears to be (badly) duplicated in MappingLoader, which
     *      is probably a closer source to the true behavior of this method.  At some
     *      point in the future, this should get rationalized.
     * 
     *
     * @param javaClass The class to which the field belongs
     * @param methodName The name of the accessor method
     * @param fieldType The type of the field if known, or null
     * @param getMethod True if get method, false if set method
     * @return The method, null if not found
     * @throws MappingException The method is not accessible or is not of the
     *  specified type
     */
    private static Method findAccessor(final Class javaClass, final String methodName, final Class fieldType,
            final boolean getMethod) throws MappingException {

        Class internalFieldType = fieldType;
        Method   method = null;
        Method[] methods;
        Class[] parameterTypes;
        Class fieldTypeFromPrimitive;
        int      i;

        try {
            if (getMethod) {
                // Get method: look for the named method or prepend get to the
                // method name. Look up the field and potentially check the
                // return type.
                method = javaClass.getMethod(methodName, new Class[0]);
                
                if (internalFieldType == null) {
                    internalFieldType = Types.typeFromPrimitive(method.getReturnType());
                } else {
                        internalFieldType = Types.typeFromPrimitive(internalFieldType);
                        Class returnType = Types.typeFromPrimitive(method.getReturnType());
                        
                        //-- First check against whether the declared type is
                        //-- an interface or abstract class. We also check
                        //-- type as Serializable for CMP 1.1 compatibility.
                        if (internalFieldType.isInterface()
                                || ((internalFieldType.getModifiers() & Modifier.ABSTRACT) != 0)
                                || (internalFieldType == java.io.Serializable.class)) {
                            if (!internalFieldType.isAssignableFrom(returnType)) {
                                throw new MappingException("mapping.accessorReturnTypeMismatch", method, internalFieldType.getName());
                            }
                        } else {
                            if (!returnType.isAssignableFrom(internalFieldType)) {
                                throw new MappingException("mapping.accessorReturnTypeMismatch", method, internalFieldType.getName());
                            }
                        }
                    }      
             } else {
                 method = null;
                 fieldTypeFromPrimitive = null;

                 // Set method: look for the named method or prepend set to the
                 // method name. If the field type is know, look up a suitable
                 // method. If the fielf type is unknown, lookup the first
                 // method with that name and one parameter.
                 if (internalFieldType != null) {
                     fieldTypeFromPrimitive = Types.typeFromPrimitive(internalFieldType);
                     // first check for setter with reference type (e.g. setXxx(Integer))
                     try {
                         method = javaClass.getMethod(methodName, new Class[] {fieldTypeFromPrimitive});
                     } catch (Exception except) {
                         // if setter for reference type could not be found
                         // try to find one for primitive type (e.g. setXxx(int))
                         try {
                             method = javaClass.getMethod(methodName, new Class[] {internalFieldType});
                         } catch (Exception except2) {
                             // log.warn ("Unexpected exception", except2);
                         }
                     }
                 }
                 if (null == method) {
                     methods = javaClass.getMethods();
                     method = null;
                     for (i = 0; i < methods.length; ++i) {
                         if (methods[i].getName().equals(methodName)) {
                             parameterTypes = methods[i].getParameterTypes();
                             if (parameterTypes.length != 1) {
                                 continue;
                             }
                             
                             Class paramType = Types.typeFromPrimitive(parameterTypes[0]);
                             
                             if ((internalFieldType == null)
                                     || paramType.isAssignableFrom(fieldTypeFromPrimitive)) {
                                 //-- check straight match
                                 method = methods[i];
                                 break;
                             } else if (internalFieldType.isInterface()
                                     || ((internalFieldType.getModifiers() & Modifier.ABSTRACT) != 0)) {
                                 //-- Check against whether the declared type is
                                 //-- an interface or abstract class. 
                                 if (fieldTypeFromPrimitive.isAssignableFrom(paramType)) {
                                     method = methods[i];
                                     break;
                                 }
                             }
                         }
                     }
                     if (method == null) {
                         return null;
                     }
                 }
             }
             // Make sure method is public and not static.
             // (note: Class.getMethod() returns only public methods).
             //if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
             if ((method.getModifiers() & Modifier.STATIC) != 0) {
                 throw new MappingException("mapping.accessorNotAccessible", methodName, javaClass.getName());
             }
             return method;
         } catch (MappingException except) {
             throw except;
         } catch (Exception except) {
             //System.out.println(except.toString());
         }
         return null;
     }

    private String capitalize(final String name) {
        char first;

        first = name.charAt(0);
        if (Character.isUpperCase(first)) {
            return name;
        }
        return Character.toUpperCase(first) + name.substring(1);
    }

    /**
     * Get the ReeflectService in used given a ClassLoadaer instance
     *
     * @param loader the current ClassLoader's instance.
     * @return the <code>ReflectService</code> instance. If doess't yet exist
     * for the given classloader, then it creates one prior to returning it.
     */
    private ReflectService getContextReflectService(final ClassLoader loader) {

        if ((null == loader) || (this._defaultReflectService._loader == loader)) {
            return this._defaultReflectService;
        }

        ReflectService resultReflectService = (ReflectService) _reflectServices.get(loader);
        if (null == resultReflectService) {
            // create a new Refelect service and store it in the hashtable
            resultReflectService = new ReflectService(_defaultReflectService, loader);
        }
        return resultReflectService;
    }


    /**
     * Provides all the necessary instances of <code>Method</code>, <code>Class</code>
     * and <code>Field</code> for a given <code>ClassLoader</code> instance.
     *
     */
    private class ReflectService {

        /**
         * Default constructor. All fields need to be set one by one.
         */
        ReflectService () {
            // no code to execute
        }

        /**
         * Contructs a ReflectService object based on the instance provided
         * and uses the <code>ClassLoader</code> to build Reflection fields.
         * @param refSrv the ReflectService that serve as a based for the new instance
         * @loader the new ClassLoader
         */
        ReflectService(final ReflectService refSrv, final ClassLoader loader) {
            this._loader = loader;
            this._fClass = cloneClass(refSrv._fClass);
            this._field  = cloneField(refSrv._field);
            this._getSequence = cloneMethods(refSrv._getSequence);
            this._setSequence = cloneMethods(refSrv._setSequence);
            this._getMethod = cloneMethod(refSrv._getMethod);
            this._addMethod = cloneMethod(refSrv._addMethod);
            this._setMethod = cloneMethod(refSrv._setMethod);
            this._hasMethod = cloneMethod(refSrv._hasMethod);
            this._deleteMethod = cloneMethod(refSrv._deleteMethod);
            this._createMethod = cloneMethod(refSrv._createMethod);

        }

        ClassLoader _loader;

        Class _fClass;

        Field         _field;
        Method[]      _getSequence;
        Method[]      _setSequence;
        Method        _getMethod;
        Method        _setMethod;
        Method        _addMethod;
        Method        _hasMethod;
        Method        _deleteMethod;
        Method        _createMethod;

        /**
         * constructs a Field instance with the current ClassLoader
         */
        private Field cloneField(final Field originalField) {
            if (null == originalField) {
                return null;
            }

            Field resultField = null;
            try {
                resultField = originalField.getDeclaringClass().getField(originalField.getName());
            } catch (NoSuchFieldException e) {
                // ssa, FIXME shoudl never happen
                e.printStackTrace();
            }
            return resultField;
        }

        /**
         * constructs a Method instance with the current ClassLoader
         */
        private Method cloneMethod(final Method originalMethod) {
            if (null == originalMethod) {
                return null;
            }

            Method resultMethod = null;
            try {
                Class newCls = loadClass(originalMethod.getDeclaringClass().getName());
                String methodName = originalMethod.getName();
                Class[] methodParams = originalMethod.getParameterTypes();
                for (int i = 0; i < methodParams.length; i++) {
                      if (!methodParams[i].isPrimitive()) {
                              methodParams[i] = loadClass(methodParams[i].getName());
                      }
                }
                resultMethod = newCls.getMethod(methodName, methodParams);
            } catch (NoSuchMethodException e) {
                // ssa, FIXME shoudl never happen
                e.printStackTrace();
            }
            return resultMethod;
        }

        /**
         * constructs an Array of Method instances with the current ClassLoader
         */
        private Method[] cloneMethods(final Method[] originalMethods) {
            if (null == originalMethods) {
                return null;
            }

            Method [] resultMethods = new Method[originalMethods.length];
            for (int i = 0; i < originalMethods.length; i++) {
                resultMethods[i] = cloneMethod(originalMethods[i]);
            }
            return resultMethods;
        }

        /**
         * constructs a <code>Class</code> instance with the current ClassLoader
         */
        private Class cloneClass(final Class originalClass) {
            if (null == originalClass) {
                return null;
            }
            if (originalClass.isPrimitive()) {
                return originalClass;
            }

            return loadClass(originalClass.getName());
        }

        /**
         * Helper method to load the class given its full qualified name
         */
        private Class loadClass(final String name) {
            Class resultClass = null;
            try {
                resultClass = ClassLoadingUtils.loadClass(_loader, name);
            } catch (ClassNotFoundException e) {
                // ssa, FIXME : should never happen
                e.printStackTrace();
            }

            return resultClass;
        }
    }

}

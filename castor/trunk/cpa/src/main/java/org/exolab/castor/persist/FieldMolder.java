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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.jdo.util.ClassLoadingUtils;
import org.castor.persist.CascadingType;
import org.exolab.castor.jdo.DataObjectAccessException;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.types.FieldMappingCollectionType;

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

    private ClassMolder _enclosingClassMolder;

    private ClassMolder _fieldClassMolder;

    private Class<?> _collectionClass;

    private String _fieldType;

    private String _fieldName;

    private Object _default;

    private boolean _readonly;
    
    /**
     * Enlists the cascading operations defined for this field.
     */
    private EnumSet<CascadingType> _cascading;
    
    /** Indicates whether this field has been flagged as transient, i.e. not to be considered 
     *  during any persistence operations. */
    private boolean _transient;
    
    /** Specifies the {@link java.util.Comparator} instance to use with a {@link SortedSet}/SortedMap collection. */
    private String _comparator;

    /** Collection of {@link ReflectService} instances keyed by {@link ClassLoader} instance. */
    private HashMap<ClassLoader, ReflectService> _reflectServices;

    /** Default {@link ReflectService}. */
    private ReflectService _defaultReflectService;

    private SQLRelationLoader _manyToManyLoader;

    public String toString() {
        return "FieldMolder for " + _enclosingClassMolder.getName() + "." + _fieldName + " of type " + getFieldTypeName();
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
    public FieldPersistenceType getFieldPertsistenceType() {
        if (!isPersistanceCapable()) {
            return isSerializable() ? FieldPersistenceType.SERIALIZABLE : FieldPersistenceType.PRIMITIVE;
        }
 
        if (!isMulti()) {
            return FieldPersistenceType.PERSISTANCECAPABLE;
        }

        if (!isManyToMany()) {
            return FieldPersistenceType.ONE_TO_MANY;
        }

        return FieldPersistenceType.MANY_TO_MANY;
    }

    public SQLRelationLoader getRelationLoader() {
        return _manyToManyLoader;
    }

    public boolean isStored() {
        return _fieldClassMolder == null || _store;
    }

    public boolean isManyToMany() {
        return _manyToManyLoader != null;
    }
    public boolean isDependent() {
        if (_fieldClassMolder == null) {
            return false;
        }
        ClassMolder extendPath = _enclosingClassMolder;
        ClassMolder depends = _fieldClassMolder.getDepends();
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
        return _fieldClassMolder != null;
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
        _fieldClassMolder = fMold;
    }
    public ClassMolder getEnclosingClassMolder() {
        return _enclosingClassMolder;
    }

    public ClassMolder getFieldClassMolder() {
        return _fieldClassMolder;
    }

    public LockEngine getFieldLockEngine() {
        return (_fieldClassMolder == null) ? null : _fieldClassMolder.getLockEngine();
    }

    public boolean isReadonly() {
        return _readonly;
    }

    public Class<?> getCollectionType() {
        return _collectionClass;
    }
    
    /**
     * Returns the 'cascading operations' defined for this field.
     * @return the 'cascading operations' defined.
     */
    public EnumSet<CascadingType> getCascading() {
        return _cascading;
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
            if (rf.getField() != null) {
                return rf.getField().get(internalObject);
            } else if (rf.getGetMethod() != null) {
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
                if ((internalObject == null) || ((rf._hasMethod != null)
                        && !((Boolean) rf._hasMethod.invoke(
                                internalObject, (Object[]) null)).booleanValue())) {
                    return null;
                }
                return rf.getGetMethod().invoke(internalObject, (Object[]) null);
            } else {
                return null;
            }
        } catch (IllegalAccessException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format(
                    "mapping.schemaChangeNoAccess", toString()), except);
        } catch (InvocationTargetException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format("mapping.schemaChangeInvocation",
                                                              toString(), except), except);
        }
    }

    public void addValue(final Object object, final Object value, final ClassLoader loader) {

        ReflectService rf = getContextReflectService(loader);
        
        if (_log.isDebugEnabled()) {
            _log.debug("Calling " + rf.getAddMethod().getName()
                    + " on " + object.getClass().getName() + " with value " + value);
        }
        
        try {
            if (rf.getAddMethod() == null) {
                throw new DataObjectAccessException(Messages.format(
                        "mapping.addMethodNotDefined", this.getName()));
            }

            if (value == null) {
                throw new NullPointerException("Adding null value is not allowed");
            }

            rf.getAddMethod().invoke(object, new Object[] {value});
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
            if (rf.getField() != null) {
                rf.getField().set(internalObject, (value == null) ? _default : value);
            } else if (rf.getSetMethod() != null) {

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
                        rf.getSetMethod().invoke(internalObject,
                                new Object[] {(value == null) ? _default : value});
                    }
                }
            } else {
                throw new DataObjectAccessException(
                        "no method to set value for field: " + getFieldTypeName() + " in class: " + _enclosingClassMolder);
            }
            // If the field has no set method, ignore it.
            // If this is a problem, identity it someplace else.
        } catch (IllegalArgumentException except) {
            // Graceful way of dealing with unwrapping exception
            if (value == null) {
                throw new DataObjectAccessException(Messages.format(
                        "mapping.typeConversionNull", toString()));
            }
            throw new DataObjectAccessException(Messages.format(
                    "mapping.typeConversion", toString(), value.getClass().getName()));
        } catch (IllegalAccessException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format(
                    "mapping.schemaChangeNoAccess", toString()), except);
        } catch (InvocationTargetException except) {
            // This should never happen
            throw new DataObjectAccessException(Messages.format(
                    "mapping.schemaChangeInvocation", toString(), except.getMessage()), except);
        }
    }

    /**
     * Check if the specified value is the default value of the Field
     * represented by this FieldMolder.
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
    //  copy from FieldHandler.java and modified
    // ======================================================
    protected Class<?> getCollectionType(final String coll, final boolean lazy) {
        /* 
         * Class type;
         */

        for (int i = 0; i < INFO.length; i++) {
            if (INFO[i].getName().equals(coll)) {
                return INFO[i].getType();
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

    public static class CollectionInfo {
        private String _name;
        private Class<?> _type;
        
        public CollectionInfo(final String name, final Class<?> type) {
            _name = name;
            _type = type;
        }
        
        public String getName() { return _name; }
        public Class<?> getType() { return _type; }
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
     * @param fieldMapping The field mapping information
     * @throws MappingException The field or its accessor methods are not
     *  found, not accessible, not of the specified type, etc
     */
    public FieldMolder(final DatingService ds, final ClassMolder eMold, final FieldDescriptor fieldDescriptor,
            final SQLRelationLoader loader) throws MappingException {

        this(ds, eMold, fieldDescriptor);
        
        _manyToManyLoader = loader;
    }

    public FieldMolder(final DatingService datingService, final ClassMolder enclosingClassMolder, final FieldDescriptor fieldDescriptor)
            throws MappingException {

        String fieldName = fieldDescriptor.getFieldName();
        String fieldType = fieldDescriptor.getFieldType().getName();
        
        try {
            // create the reflection service with the ClassLoader hold in the
            // SatingService object as default
            _defaultReflectService = new ReflectService();
            _reflectServices = new HashMap<ClassLoader, ReflectService>();

            // Set enclosing ClassMolder
            _enclosingClassMolder = enclosingClassMolder;

            if ("java.io.Serializable".equals(fieldType)) {
                _serial = true;
            }
            
            // check whether complete field is declared transient
            _transient = fieldDescriptor.isTransient();
            
            dealWithSqlMapping(fieldDescriptor);

            // check if comparator is specified, and if so, use it
            String comparator = ((FieldDescriptorImpl) fieldDescriptor).getComparator();
            if (comparator != null) {
                _comparator = comparator;
            }
            
            establishCollectionDefinition(datingService, ((FieldDescriptorImpl) fieldDescriptor).getCollection(), 
                    fieldType);
            
            // Set field name, if it is null, we try to discover it with
            // return type of set/get method.
            setFieldTypeName(fieldType);

            Class enclosingClass;
            try {
                enclosingClass = datingService.resolve(enclosingClassMolder.getName());
            } catch (ClassNotFoundException e) {
                throw new MappingException("mapping.classNotFound", enclosingClassMolder.getName());
            }
            // ssa, multi classloader feature
            // set the default classloader to the hash table
            // ssa, FIXME : Shoudln't we have a ref to the Classloader used
            // instead of asking it to the newly created class ?
            _defaultReflectService._loader = enclosingClass.getClassLoader();
            if (null != _defaultReflectService._loader) {
                _reflectServices.put(_defaultReflectService._loader, this._defaultReflectService);
            }

            Class declaredClass = null;
            if (fieldType != null) {
                try {
                    declaredClass = Types.typeFromName(enclosingClass.getClassLoader(), fieldType);
                    _defaultReflectService.setFieldType(declaredClass);
                } catch (ClassNotFoundException cnfe) {
                    throw new MappingException("mapping.classNotFound", declaredClass);
                }
            }

            if (((FieldDescriptorImpl) fieldDescriptor).isDirect()) {
                establishDirectFieldAccess(fieldName, enclosingClass);
            } else {
                String getMethod = ((FieldDescriptorImpl) fieldDescriptor).getGetMethod();
                String setMethod = ((FieldDescriptorImpl) fieldDescriptor).getSetMethod();
                
                if ((getMethod == null) && (setMethod == null)) {

                    // Container object, map field to fields of the container
                    int point;
                    ArrayList<Method> getSeq = new ArrayList<Method>();
                    ArrayList<Method> setSeq = new ArrayList<Method>();
                    String name = fieldName;
                    Class<?> last;
                    Method method = null;
                    String methodName = null;

                    try {
                        while (true) {
                            point = name.indexOf('.');
                            if (point < 0) {
                                break;
                            }
                            last = enclosingClass;
                            
                            if (fieldType.compareTo("boolean") == 0 || fieldType.compareTo("java.lang.Boolean") == 0) {
                                try {
                                    methodName = METHOD_IS_PREFIX
                                               + capitalize(name.substring(0, point));
                                    method = enclosingClass.getMethod(methodName, (Class[]) null);
                                } catch (NoSuchMethodException nsme) {
                                    if (_log.isDebugEnabled()) {
                                        _log.debug (Messages.format("mapping.accessorNotFound",
                                                methodName, "boolean", getName()));
                                    }
                                }
                            }
                            
                            if (method == null) {
                                methodName = METHOD_GET_PREFIX + capitalize(name.substring(0, point));
                                method = enclosingClass.getMethod(methodName, (Class[]) null);
                            }
                            
                            name = name.substring(point + 1);
                            // Make sure method is not abstract/static
                            // (note: Class.getMethod() returns only public methods).
                            if (((method.getModifiers() & Modifier.ABSTRACT) != 0)
                                    || ((method.getModifiers() & Modifier.STATIC) != 0)) {
                                throw new MappingException("mapping.accessorNotAccessible",
                                        methodName, enclosingClass.getName());
                            }
                            getSeq.add(method);
                            enclosingClass = method.getReturnType();
                            // setter;   Note: javaClass already changed, use "last"
                            if (fieldType.compareTo("boolean") == 0 || fieldType.compareTo("java.lang.Boolean") == 0) {
                                methodName = METHOD_SET_PREFIX + methodName.substring(2);
                            } else {
                                methodName = METHOD_SET_PREFIX + methodName.substring(3);
                            }

                            try {
                                method = last.getMethod(methodName, new Class[] {enclosingClass});
                                if (((method.getModifiers() & Modifier.ABSTRACT) != 0)
                                        || ((method.getModifiers() & Modifier.STATIC) != 0)) {
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
                               methodName, null, enclosingClass.getName()), ex);
                    }
                    if (getSeq.size() > 0) {
                        _defaultReflectService._getSequence =
                            getSeq.toArray(new Method[getSeq.size()]);
                        _defaultReflectService._setSequence =
                            setSeq.toArray(new Method[setSeq.size()]);
                    }
                    Class methodClass = (_collectionClass != null) ? _collectionClass : declaredClass;
                    _defaultReflectService.setGetMethod(null);
                    
                    // if field is of type boolean, check whether is<Field>() is defined.
                    if (fieldType != null && (fieldType.compareTo("boolean") == 0 || fieldType.compareTo("java.lang.Boolean") == 0)) {
                        _defaultReflectService.setGetMethod(findAccessor(
                                enclosingClass, METHOD_IS_PREFIX + capitalize(name), methodClass, true));
                    }
                    
                    if (_defaultReflectService.getGetMethod() == null) {
                        _defaultReflectService.setGetMethod(findAccessor(
                                enclosingClass, METHOD_GET_PREFIX + capitalize(name), methodClass, true));
                    }

                    Method getMethodTemp = _defaultReflectService.getGetMethod();
                    if (getMethodTemp == null) {
                        if (fieldType.compareTo("boolean") == 0 || fieldType.compareTo("java.lang.Boolean") == 0) {
                            throw new MappingException("mapping.accessorNotFound",
                                    METHOD_GET_PREFIX + "/" + METHOD_IS_PREFIX + capitalize(name),
                                    fieldType, enclosingClassMolder.getName());
                        }
                        throw new MappingException("mapping.accessorNotFound",
                                METHOD_GET_PREFIX + capitalize(name),
                                fieldType, enclosingClassMolder.getName());
                    }
                    
                    // update fClass, because we can't tell between primitive
                    // and primitive wrapper from the mapping
                    if (_collectionClass == null) {
                        _defaultReflectService.setFieldType(_defaultReflectService.getGetMethod().getReturnType());
                    }

                    _defaultReflectService.setSetMethod(findAccessor(
                            enclosingClass, METHOD_SET_PREFIX + capitalize(name), methodClass, false));

                    if (_defaultReflectService.getSetMethod() == null) {
                        _defaultReflectService.setAddMethod(findAccessor(
                                enclosingClass, METHOD_ADD_PREFIX + capitalize(name), declaredClass, false));

                        // look again, but this time without a trailing 's'
                        if ((_defaultReflectService.getAddMethod() == null) && (name.endsWith("s"))) {
                            _defaultReflectService.setAddMethod(findAccessor(
                                    enclosingClass, METHOD_ADD_PREFIX
                                    + capitalize(name).substring(0, name.length() - 1),
                                    declaredClass, false));
                        }

                        // if add<FieldName>() has been found, set _addable to true 
                        if (_defaultReflectService.getAddMethod() != null) {
                            _addable = true;
                        }
                    }
                    
                    if ((_defaultReflectService.getSetMethod() == null)
                            && (_defaultReflectService.getAddMethod() == null)) {
                        throw new MappingException("mapping.accessorNotFound",
                                METHOD_SET_PREFIX + "/" + METHOD_ADD_PREFIX + capitalize(name),
                                declaredClass, enclosingClass.getName());
                    }
                    
                } else {
                    
                    // there's a get method and/or set method specified, but we don't know whether 
                    // both are specified
                    // NOTE: a get method has to be specified once a set method has been provided

                    // Bean type object, map field to get<Method>/set<Method>

                    Class<?> fieldClassType = _defaultReflectService.getFieldType();

                    establishGetMethod(enclosingClass, getMethod, fieldClassType);
                    establishSetAndAddMethod(((FieldDescriptorImpl) fieldDescriptor).isLazy(), enclosingClass,
                            declaredClass, setMethod, fieldClassType);
                }
            }


            establishCreateMethod(((FieldDescriptorImpl) fieldDescriptor).getCreateMethod(), fieldName, enclosingClass);
            establishHasAndDeleteMethods(fieldName, enclosingClass);

            if ((_defaultReflectService.getField() == null)
                    && (_defaultReflectService.getSetMethod() == null)
                    && (_defaultReflectService.getGetMethod() == null)) {
                throw new MappingException("_field or _setMethod can't be created");
            }

            datingService.pairFieldClass(this, getFieldTypeName());
        } catch (NullPointerException e) {
            _log.fatal("Caught unexpected NullPointerException: ", e);
            throw new MappingException("Unexpected Null pointer!\n" + e);
        }
        
        _fieldName = fieldName;

        // If the field is of a primitive type we use the default value
        _default = Types.getDefault(_defaultReflectService.getFieldType());
        // make the default to null for wrappers of primitives
        if (!_defaultReflectService.getFieldType().isPrimitive()) {
            _default = null;
        }

    }

    /**
     * @param datingService
     * @param fieldMapping
     * @param fieldType
     * @throws MappingException
     */
    private void establishCollectionDefinition(
            final DatingService datingService, final FieldMappingCollectionType collectionType,
            String fieldType) throws MappingException {
        if (collectionType != null) {
            _multi = true;
            // simple arrays support
            if (COLLECTION_TYPE_ARRAY.equals(collectionType.toString())) {
                String arrayClassName = "[L" + fieldType + ";";
                try {
                    _collectionClass = datingService.resolve(arrayClassName);
                } catch (ClassNotFoundException e) {
                    throw new MappingException("mapping.classNotFound",
                            arrayClassName);
                }
            } else {
                _collectionClass = getCollectionType(collectionType.toString(),
                        _lazy);
                if (_collectionClass != SortedSet.class && _comparator != null) {
                    throw new MappingException(
                            Messages.message("mapping.wrong.use.of.comparator"));
                }
            }
            _store = false;
        }
    }

    /**
     * If direct field access is configured, set the (directly accessed) field on the {@link FieldHandler}.
     * @param fieldName The name of the field.
     * @param enclosingClass The class type of the enclosing class.
     * @throws MappingException If no such field is present on the class in question.
     */
    private void establishDirectFieldAccess(String fieldName,
            Class<?> enclosingClass) throws MappingException {
        Class<?>  fieldClass = (_collectionClass != null) ? _collectionClass : null;
        _defaultReflectService.setField(findField(enclosingClass, fieldName, fieldClass));
        if (_defaultReflectService.getField() == null) {
            throw new MappingException(Messages.format(
                    "mapping.fieldNotAccessible", fieldName, enclosingClass.getName()));
        }
        _defaultReflectService.setFieldType(_defaultReflectService.getField().getType());
        int modifiers = _defaultReflectService.getField().getModifiers();
        if ((modifiers != Modifier.PUBLIC) && (modifiers != (Modifier.PUBLIC | Modifier.VOLATILE))) {
            throw new MappingException(Messages.format(
                    "mapping.fieldNotAccessible", _defaultReflectService.getField().getName(),
                    _defaultReflectService.getField().getDeclaringClass().getName()));
        }
    }

    /**
     * Add a set and/or add method to the {@link FieldHandler} if given.
     * @param isLazy Indicates whether the current field is lazy.
     * @param enclosingClass The enclosing class type.
     * @param declaredClass The declaring class type.
     * @param setMethod The name of the set method (if specified).
     * @param fieldClassType The field class type.
     * @throws MappingException If the given method is not present on the enclosing class.
     */
    private void establishSetAndAddMethod(final boolean isLazy,
            Class<?> enclosingClass, Class<?> declaredClass, String setMethod,
            Class<?> fieldClassType) throws MappingException {
        if (setMethod != null) {

            if (_collectionClass != null) {
                _defaultReflectService.setSetMethod(findAccessor(
                        enclosingClass, setMethod, _collectionClass, false));

                // find addXXX method only if lazy loading is turned off
                if ((_defaultReflectService.getSetMethod() == null) && !isLazy) {
                    _defaultReflectService.setAddMethod(findAccessor(
                            enclosingClass, setMethod, declaredClass, false));
                    if (_defaultReflectService.getAddMethod() != null) {
                        _addable = true;
                    }
                }

            } else {
                // find setXXX method
                _defaultReflectService.setSetMethod(findAccessor(
                        enclosingClass, setMethod, fieldClassType, false));
            }


            if ((_defaultReflectService.getSetMethod() == null)
                    && (_defaultReflectService.getAddMethod() == null)) {
                throw new MappingException("mapping.accessorNotFound",
                        setMethod, fieldClassType, enclosingClass.getName());
            }

            if (_defaultReflectService.getFieldType() == null) {
                _defaultReflectService.setFieldType(_defaultReflectService.getSetMethod().getParameterTypes()[0]);
            }
        } else {
            throw new MappingException("mapping.setMethodMappingNotFound",
                    (_collectionClass != null) ? _collectionClass : fieldClassType, enclosingClass.getName());
        }
    }

    /**
     * Add a get method to the {@link FieldHandler} if specified.
     * @param enclosingClass The enclosing Class type.
     * @param getMethod The Name of the get method (null possible, if not specified).
     * @param fieldClassType The Class type of the field.
     * @throws MappingException If the method defined is not present on the given enclosing class.
     */
    private void establishGetMethod(Class<?> enclosingClass, String getMethod,
            Class<?> fieldClassType) throws MappingException {
        if (getMethod != null) {
            if (_collectionClass != null) {
                _defaultReflectService.setGetMethod(findAccessor(
                        enclosingClass, getMethod, _collectionClass, true));
            } else {
                _defaultReflectService.setGetMethod(findAccessor(
                        enclosingClass, getMethod, fieldClassType, true));
            }

            if (_defaultReflectService.getGetMethod() == null) {
                throw new MappingException("mapping.accessorNotFound",
                        getMethod, fieldClassType, enclosingClass.getName());
            }

            // set/reset the fClass to actual field class
            if (_collectionClass == null) {
                _defaultReflectService.setFieldType(_defaultReflectService.getGetMethod().getReturnType());
            }

        } else {
            throw new MappingException("mapping.getMethodMappingNotFound",
                    (_collectionClass != null) ? _collectionClass : fieldClassType, enclosingClass.getName());
        }
    }

    /**
     * Adds has and delete methods to the {@link FieldHandler} if present.
     * @param fieldName Name of the field.
     * @param javaClass Class type of the field.
     */
    private void establishHasAndDeleteMethods(String fieldName,
            Class<?> javaClass) {
        if (fieldName != null) {
            Method hasMethod = null;
            Method deleteMethod = null;

            try {
                hasMethod = javaClass.getMethod(METHOD_HAS_PREFIX
                        + capitalize(fieldName), (Class[]) null);
                if (((hasMethod.getModifiers() & Modifier.PUBLIC) == 0)
                        || ((hasMethod.getModifiers() & Modifier.STATIC) != 0)) {
                    hasMethod = null;
                }
                try {
                    if (((hasMethod.getModifiers() & Modifier.PUBLIC) == 0)
                            || ((hasMethod.getModifiers() & Modifier.STATIC) != 0)) {
                        deleteMethod = null;
                    }
                    deleteMethod = javaClass.getMethod(METHOD_DELETE_PREFIX
                            + capitalize(fieldName), (Class[]) null);
                } catch (Exception except) {
                    // no explicit exception handling
                }
                _defaultReflectService._hasMethod = hasMethod;
                _defaultReflectService._deleteMethod = deleteMethod;
            } catch (Exception except) {
                // no explicit exception handling
            }
        }
    }

    /**
     * Adds a create method to the {@link FieldHandler} if specified.
     * @param createMethod Name of the method to create object instances.
     * @param fieldName Name of the field.
     * @param javaClass Class type.
     * @throws MappingException If the specified method is not present on the given class type.
     */
    private void establishCreateMethod(String createMethod, String fieldName,
            Class<?> javaClass) throws MappingException {
        // If there is a create method, add it to the field handler
        // Note: create method is used for enclosing object of this field to
        // determine
        // what exact instance to be created.
        if (createMethod != null) {
            try {
                _defaultReflectService.setCreateMethod(javaClass.getMethod(
                        createMethod, (Class[]) null));
            } catch (Exception except) {
                // No such/access to method
                throw new MappingException("mapping.createMethodNotFound",
                        createMethod, javaClass.getName());
            }
        } else if ((fieldName != null)
                && !Types.isSimpleType(_defaultReflectService.getFieldType())) {
            try {
                Method method;

                method = javaClass.getMethod(METHOD_CREATE_PREFIX
                        + capitalize(fieldName), (Class[]) null);
                _defaultReflectService.setCreateMethod(method);
            } catch (Exception except) {
                // no explicit exception handling
            }
        }
    }

    private void dealWithSqlMapping(FieldDescriptor fieldDescriptor) throws MappingException {
        if (fieldDescriptor.hasNature(FieldDescriptorJDONature.class.getName())) {
            FieldDescriptorJDONature nature = new FieldDescriptorJDONature(fieldDescriptor);
            if (nature.isDirtyCheck()) {
                _check = true;
            }

            if (nature.getManyTable() != null) {
                _store = false;
            } else if (nature.getSQLName() == null || nature.getSQLName().length == 0) {
                _store = false;
            } else {
                _store = true;
            }
        
            _readonly = nature.isReadonly();
   
            _cascading = EnumSet.noneOf(CascadingType.class);

            // TODO: in the schema, cascading is still simply a string.
            //       when this is finally made into a proper list (enumeration and all)
            //       this will probably have to be changed
            // TODO: also, we should probably use constants
            // NOTE: we assume here that the types are delimited by whitespace
            if (nature.getCascading() != null) {
                String[] temp = nature.getCascading().toLowerCase().trim().split("\\s+");
                List<String> cascadingTypes = java.util.Arrays.asList(temp);
                if (cascadingTypes.contains("all")) {
                    _cascading = EnumSet.allOf(CascadingType.class);
                } else {
                    if (cascadingTypes.contains("create")) {
                	_cascading.add(CascadingType.CREATE);
                    }
                    if (cascadingTypes.contains("delete")) {
                	_cascading.add(CascadingType.DELETE);
                    }
                    if (cascadingTypes.contains("update")) {
                	_cascading.add(CascadingType.UPDATE);
                    }
                }
            }
            
            boolean isSQLTransient = nature.isTransient();
            if (_transient && !isSQLTransient) {
                throw new MappingException (Messages.message("persist.transient.conflict"));
            }
            _transient = isSQLTransient;
            
        }
        _lazy = ((FieldDescriptorImpl) fieldDescriptor).isLazy();
    }

    /**
     * Returns the named field. Uses reflection to return the named
     * field and check the field type, if specified.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldName The name of the field
     * @param fieldType The type of the field if known, or null
     * @return The field, null if not found
     * @throws MappingException The field is not accessible or is not of the
     *  specified type
     */
    private Field findField(final Class<?> javaClass, final String fieldName, final Class<?> fieldType)
    throws MappingException {
        Class<?> internalFieldType = fieldType;
        Field field;

        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            field = javaClass.getField(fieldName);
            if ((field.getModifiers() != Modifier.PUBLIC)
                    && (field.getModifiers() != (Modifier.PUBLIC | Modifier.VOLATILE))) {
                throw new MappingException("mapping.fieldNotAccessible",
                        fieldName, javaClass.getName());
            }
            if (internalFieldType == null) {
                internalFieldType = Types.typeFromPrimitive(field.getType());
            } else if (Types.typeFromPrimitive(internalFieldType)
                    != Types.typeFromPrimitive(field.getType())) {
                throw new MappingException("mapping.fieldTypeMismatch",
                        field, internalFieldType.getName());
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
     * Returns the named accessor. Uses reflection to return the named accessor and
     * check the return value or parameter type, if specified.
     *
     * @param javaClass The class to which the field belongs.
     * @param methodName The name of the accessor method.
     * @param fieldType The type of the field if known, or null.
     * @param getMethod True if get method, false if set method.
     * @return The method, null if not found.
     * @throws MappingException The method is not accessible or is not of the
     *         specified type.
     */
    public static final Method findAccessor(final Class<?> javaClass, final String methodName,
            final Class<?> fieldType, final boolean getMethod) throws MappingException {
        Class<?> internalFieldType = fieldType;
        try {
            Method method = null;

            if (getMethod) {
                // Get method: look for the named method or prepend get to the method
                // name. Look up the field and potentially check the return type.
                method = javaClass.getMethod(methodName, new Class[0]);

                // The MapItem is used to handle the contents of maps. Since the MapItem
                // has to use Object for its methods we cannot (but also don't have to)
                // check for correct types.
                if (javaClass == MapItem.class) {
                    if (methodName.equals("getKey")) { return method; }
                    if (methodName.equals("getValue")) { return method; }
                }

                if (internalFieldType == null) {
                    internalFieldType = Types.typeFromPrimitive(method.getReturnType());
                } else {
                    internalFieldType = Types.typeFromPrimitive(internalFieldType);
                    Class<?> returnType = Types.typeFromPrimitive(method.getReturnType());

                    //-- First check against whether the declared type is
                    //-- an interface or abstract class. We also check
                    //-- type as Serializable for CMP 1.1 compatibility.
                    if (internalFieldType.isInterface()
                            || ((internalFieldType.getModifiers() & Modifier.ABSTRACT) != 0)
                            || (internalFieldType == java.io.Serializable.class)) {

                        if (!internalFieldType.isAssignableFrom(returnType)) {
                            throw new MappingException(
                                    "mapping.accessorReturnTypeMismatch",
                                    method, internalFieldType.getName());
                        }
                    } else {
                        if (!returnType.isAssignableFrom(internalFieldType)) {
                            throw new MappingException(
                                    "mapping.accessorReturnTypeMismatch",
                                    method, internalFieldType.getName());
                        }
                    }
                }
            } else {
                // Set method: look for the named method or prepend set to the method
                // name. If the field type is know, look up a suitable method. If the
                // field type is unknown, lookup the first method with that name and
                // one parameter.
                Class<?> fieldTypePrimitive = null;
                if (internalFieldType != null) {
                    fieldTypePrimitive = Types.typeFromPrimitive(internalFieldType);
                    // first check for setter with reference type (e.g. setXxx(Integer))
                    try {
                        method = javaClass.getMethod(methodName, new Class[] {fieldTypePrimitive});
                    } catch (Exception ex) {
                        // if setter for reference type could not be found
                        // try to find one for primitive type (e.g. setXxx(int))
                        try {
                            method = javaClass.getMethod(
                                    methodName, new Class[] {internalFieldType});
                        } catch (Exception ex2) {
                            // LOG.warn("Unexpected exception", ex2);
                        }
                    }
                }

                if (method == null) {
                    Method[] methods = javaClass.getMethods();
                    for (int i = 0; i < methods.length; ++i) {
                        if (methods[i].getName().equals(methodName)) {
                            Class[] paramTypes = methods[i].getParameterTypes();
                            if (paramTypes.length != 1) { continue; }

                            Class<?> paramType = Types.typeFromPrimitive(paramTypes[0]);

                            if (internalFieldType == null) {
                                method = methods[i];
                                break;
                            } else if (paramType.isAssignableFrom(fieldTypePrimitive)) {
                                method = methods[i];
                                break;
                            } else if (internalFieldType.isInterface()
                                    || isAbstract(internalFieldType)) {
                                if (fieldTypePrimitive.isAssignableFrom(paramType)) {
                                    method = methods[i];
                                    break;
                                }
                            }
                        }
                    }

                    if (method == null) { return null; }
                }
            }

            // Make sure method is public and not static.
            // (note: Class.getMethod() returns only public methods).
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                throw new MappingException(
                        "mapping.accessorNotAccessible", methodName, javaClass.getName());
            }
            return method;
        } catch (MappingException ex) {
            throw ex;
        } catch (Exception ex) {
            return null;
        }
    }
    
    private static boolean isAbstract(final Class<?> cls) {
        return ((cls.getModifiers() & Modifier.ABSTRACT) != 0);
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
     * Get the {@link ReflectService} used given a {@link ClassLoader} instance.
     *
     * @param loader the current {@link ClassLoader} instance.
     * @return the {@link ReflectService} instance for the given {@link ClassLoader}. If doess't yet exist
     * for the given {@link ClassLoader}, then it creates one prior to returning it.
     */
    private ReflectService getContextReflectService(final ClassLoader loader) {

        if ((null == loader) || (this._defaultReflectService._loader == loader)) {
            return this._defaultReflectService;
        }

        ReflectService resultReflectService = _reflectServices.get(loader);
        if (null == resultReflectService) {
            // create a new ReflectService and store it in the hashtable
            resultReflectService = new ReflectService(_defaultReflectService, loader);
        }
        return resultReflectService;
    }


    private void setFieldTypeName(String fieldType) {
        _fieldType = fieldType;
    }

    private String getFieldTypeName() {
        return _fieldType;
    }

    /**
     * Provides all the necessary instances of <code>Method</code>, <code>Class</code>
     * and <code>Field</code> for a given <code>ClassLoader</code> instance.
     *
     */
    private class ReflectService {

        /**
         * Default constructor. 
         */
        public ReflectService () {
        }

        /**
         * Constructs a ReflectService object based on the instance provided
         * and uses the <code>ClassLoader</code> to build Reflection fields.
         * 
         * @param refSrv the ReflectService that serve as a based for the new instance
         * @loader the new ClassLoader
         */
        public ReflectService(final ReflectService refSrv, final ClassLoader loader) {
            this._loader = loader;
            this.setFieldType(cloneClass(refSrv.getFieldType()));
            this.setField(cloneField(refSrv.getField()));
            this._getSequence = cloneMethods(refSrv._getSequence);
            this._setSequence = cloneMethods(refSrv._setSequence);
            this.setGetMethod(cloneMethod(refSrv.getGetMethod()));
            this.setAddMethod(cloneMethod(refSrv.getAddMethod()));
            this.setSetMethod(cloneMethod(refSrv.getSetMethod()));
            this._hasMethod = cloneMethod(refSrv._hasMethod);
            this._deleteMethod = cloneMethod(refSrv._deleteMethod);
            this.setCreateMethod(cloneMethod(refSrv.getCreateMethod()));

        }

        private ClassLoader _loader;

        private Class<?> fieldType;

        private Field _field;
        private Method[] _getSequence;
        private Method[] _setSequence;
        private Method _getMethod;
        private Method _setMethod;
        private Method _addMethod;
        private Method _hasMethod;
        private Method _deleteMethod;
        private Method _createMethod;

        /**
         * constructs a Field instance with the current ClassLoader.
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
         * constructs a Method instance with the current ClassLoader.
         */
        private Method cloneMethod(final Method originalMethod) {
            if (null == originalMethod) {
                return null;
            }

            Method resultMethod = null;
            try {
                Class<?> newCls = loadClass(originalMethod.getDeclaringClass().getName());
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
         * constructs an Array of Method instances with the current ClassLoader.
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
         * constructs a <code>Class</code> instance with the current ClassLoader.
         */
        private Class<?> cloneClass(final Class<?> originalClass) {
            if (null == originalClass) {
                return null;
            }
            if (originalClass.isPrimitive()) {
                return originalClass;
            }

            return loadClass(originalClass.getName());
        }

        /**
         * Helper method to load the class given its full qualified name.
         */
        private Class<?> loadClass(final String name) {
            Class<?> resultClass = null;
            try {
                resultClass = ClassLoadingUtils.loadClass(_loader, name);
            } catch (ClassNotFoundException e) {
                // ssa, FIXME : should never happen
                e.printStackTrace();
            }

            return resultClass;
        }

        private void setCreateMethod(Method createMethod) {
            _createMethod = createMethod;
        }

        private Method getCreateMethod() {
            return _createMethod;
        }

        private void setField(Field field) {
            _field = field;
        }

        private Field getField() {
            return _field;
        }

        private void setFieldType(Class<?> fieldType) {
            this.fieldType = fieldType;
        }

        private Class<?> getFieldType() {
            return fieldType;
        }

        private void setGetMethod(Method getMethod) {
            _getMethod = getMethod;
        }

        private Method getGetMethod() {
            return _getMethod;
        }

        private void setSetMethod(Method setMethod) {
            _setMethod = setMethod;
        }

        private Method getSetMethod() {
            return _setMethod;
        }

        private void setAddMethod(Method addMethod) {
            _addMethod = addMethod;
        }

        private Method getAddMethod() {
            return _addMethod;
        }
    }
}

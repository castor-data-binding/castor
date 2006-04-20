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


import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.types.DirtyType;
import org.exolab.castor.util.Messages;
import org.exolab.castor.jdo.DataObjectAccessException;


/**
 * FieldMolder represents a field of a data object class. It is used by
 * ClassMolder to set and get the value from a field of a data object.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public class FieldMolder {

    public static final short PRIMITIVE = 0;

    public static final short SERIALIZABLE = 1;

    public static final short PERSISTANCECAPABLE = 2;

    public static final short ONE_TO_MANY = 3;

    public static final short MANY_TO_MANY = 4;

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
     * Collection of reflection service keyed by ClassLoader instance.
     */
    private HashMap _reflectServices;

    /**
     * Default reflection service
     */
    private ReflectService _defaultReflectService;

    private SQLRelationLoader _manyToManyLoader;

    public String toString() {
        return "FieldMolder of "+_eMold.getName()+".set"+_fieldName+"("+_fType+" "+_fieldName+")";
    }

    public String getName()
    {
        return _fieldName;
    }

    /*
    void setRelationDescriptor( RelationDescriptor rd ) throws MappingException {
        _loader = new SQLRelationLoader( rd, _eMold.getName() );
    }
    public SQLRelationLoader getRelationLoader() {
        return _loader;
    }*/
    public short getFieldType() {
        if ( ! isPersistanceCapable() )
            if ( ! isSerializable() )
                return PRIMITIVE;
            else
                return SERIALIZABLE;

        if ( ! isMulti() )
            return PERSISTANCECAPABLE;

        if ( ! isManyToMany() )
            return ONE_TO_MANY;

        else
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
        if ( _fMold == null )
            return false;
        ClassMolder extendPath = _eMold;
        ClassMolder depends = _fMold.getDepends();
        while ( extendPath != null ) {
            if ( extendPath == depends ) {
                return true;
            } else
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
    void setFieldClassMolder( ClassMolder fMold ) {
        _fMold = fMold;
    }
    public ClassMolder getEnclosingClassMolder() {
        return _eMold;
    }

    public ClassMolder getFieldClassMolder() {
        return _fMold;
    }

    public LockEngine getFieldLockEngine() {
        return (_fMold==null?null:_fMold.getLockEngine());
    }

    public boolean isReadonly() {
        return _readonly;
    }

    public Class getCollectionType() {
        return _colClass;
    }

    public Object getValue( Object object, ClassLoader loader ) {
        ReflectService rf = getContextReflectService ( loader );
        try {
            // If field is accessed directly, get it's value, if not
            // need to call get method. It's possible to not have a
            // way to access the field.
            //if ( _handler != null )
            //    value = _handler.getValue( object );
            //else
            if ( rf._field != null )
                return rf._field.get( object );
            else if ( rf._getMethod != null ) {
                if ( rf._getSequence != null )
                    for ( int i = 0; i < rf._getSequence.length; i++ ) {
                        object = rf._getSequence[ i ].invoke( object, null );
                        if ( object == null )
                            break;
                    }
                // If field has 'has' method, false means field is null
                // and do not attempt to call getValue. Otherwise,
                if (  object == null ||
                        ( rf._hasMethod != null && ! ( (Boolean) rf._hasMethod.invoke( object, null ) ).booleanValue() ) )
                    return null;
                else
                    return rf._getMethod.invoke( object, null );
            } else
                return null;
        } catch ( IllegalAccessException except ) {
            // This should never happen
            throw new DataObjectAccessException( Messages.format( "mapping.schemaChangeNoAccess", toString() ), except );
        } catch ( InvocationTargetException except ) {
            // This should never happen
            throw new DataObjectAccessException( Messages.format( "mapping.schemaChangeInvocation",
                                                              toString(), except ) );
        }
    }

    public void addValue( Object object, Object value, ClassLoader loader ) {

        ReflectService rf = getContextReflectService ( loader );
        try {
            if ( rf._addMethod == null )
                throw new DataObjectAccessException("No add method defined for this field");

            if ( value == null )
                throw new NullPointerException("Adding null value is not allowed");

            rf._addMethod.invoke( object, new Object[] { value } );
        } catch ( IllegalArgumentException e ) {
            throw new DataObjectAccessException("Argument ,"+value+", cannot be added!", e );
        } catch ( IllegalAccessException e ) {
            throw new DataObjectAccessException("Field access error", e );
        } catch ( InvocationTargetException e ) {
            throw new DataObjectAccessException("Field invocation error", e );
        }
    }

    public void setValue( Object object, Object value, ClassLoader loader ) {
        // If there is a convertor, apply conversion here.
        ReflectService rf = getContextReflectService ( loader );
        try {
            //if ( _handler != null )
            //    _handler.setValue( object, value );
            //else
            if ( rf._field != null ) {
                rf._field.set( object, value == null ? _default : value );
            } else if ( rf._setMethod != null ) {

                if ( rf._getSequence != null )
                    for ( int i = 0; i < rf._getSequence.length; i++ ) {
                        Object last;

                        last = object;
                        object = rf._getSequence[ i ].invoke( object, null );
                        if ( object == null ) {
                            // if the value is not null, we must instantiate
                            // the object in the sequence
                            if ( value == null || rf._setSequence[ i ] == null )
                                break;
                            else {
                                object = Types.newInstance( rf._getSequence[ i ].getReturnType() );
                                rf._setSequence[ i ].invoke( last, new Object[] { object } );
                            }
                        }
                    }
                if ( object != null ) {
                    if ( value == null && rf._deleteMethod != null )
                        rf._deleteMethod.invoke( object, null );
                    else
                        rf._setMethod.invoke( object, new Object[] { value == null ? _default : value } );
                }
            } else {
                throw new DataObjectAccessException("no method to set value for field: "+_fType+" in class: "+_eMold);
            }
            // If the field has no set method, ignore it.
            // If this is a problem, identity it someplace else.
        } catch ( IllegalArgumentException except ) {
            // Graceful way of dealing with unwrapping exception
            if ( value == null )
                throw new DataObjectAccessException( Messages.format( "mapping.typeConversionNull", toString() ) );
            else
                throw new DataObjectAccessException( Messages.format( "mapping.typeConversion",
                                                            toString(), value.getClass().getName() ) );
        } catch ( IllegalAccessException except ) {
            // This should never happen
            throw new DataObjectAccessException( Messages.format( "mapping.schemaChangeNoAccess", toString() ), except );
        } catch ( InvocationTargetException except ) {
            // This should never happen
            throw new DataObjectAccessException( Messages.format( "mapping.schemaChangeInvocation",
                                                              toString(), except.getMessage() ), except );
        }
    }

    /**
     * Check if the specified value is the default value of the Field
     * represented by this FieldMolder
     */
    public boolean isDefault( Object value ) {
        if ( _default == value )
            return true;

        if ( _default == null )
            return false;

        if ( _default.equals( value ) )
            return true;

        return false;
    }

    // ======================================================
    //  copy from FieldHanlder.java and modified
    // ======================================================
    protected Class getCollectionType( String coll, boolean lazy )
            throws MappingException {
        /* 
         * Class type;
         */

        for ( int i=0; i<info.length; i++ ) {
            if ( info[i].name.equals( coll ) )
                return info[i].type;
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
        String name;
        Class type;
        CollectionInfo( String name, Class type ) {
            this.name = name;
            this.type = type;
        }
    }

    private static CollectionInfo[] info =
        { new CollectionInfo( "collection", java.util.Collection.class ),
          new CollectionInfo( "vector", java.util.Vector.class ),
          new CollectionInfo( "arraylist", java.util.ArrayList.class ),
          new CollectionInfo( "hashtable", java.util.Hashtable.class ),
          new CollectionInfo( "hashmap", java.util.HashMap.class ),
          new CollectionInfo( "set", java.util.Set.class ),
          new CollectionInfo( "hashset", java.util.HashSet.class ),
          new CollectionInfo( "map", java.util.Map.class ),
          new CollectionInfo( "array", Object[].class ) };


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
    public FieldMolder( DatingService ds, ClassMolder eMold, FieldMapping fieldMap, String manyTable,
            String[] idSQL, int[] idType, TypeConvertor[] idTo, TypeConvertor[] idFrom, String[] idParam,
            String[] relatedIdSQL, int[] relatedIdType, TypeConvertor[] ridTo, TypeConvertor[] ridFrom, String[] ridParam )
            throws MappingException {

        this( ds, eMold, fieldMap );

        _manyToManyLoader = new SQLRelationLoader( manyTable,
                idSQL, idType, idTo, idFrom, idParam,
                relatedIdSQL, relatedIdType, ridTo, ridFrom, ridParam );
    }

    public FieldMolder( DatingService ds, ClassMolder eMold, FieldMapping fieldMap )
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

            if ( fieldMap.getSql() == null
                    || fieldMap.getSql().getDirty() == null
                    || ! fieldMap.getSql().getDirty().equals(DirtyType.IGNORE) ) {
                _check = true;
            }

            if ( "serializable".equals( fieldMap.getType() ) )
                _serial = true;

            if ( fieldMap.getSql() == null )
                _store = false;
            else if ( fieldMap.getSql().getManyTable() != null )
                _store = false;
            else if ( fieldMap.getSql().getName().length == 0 )
                _store = false;
            else
                _store = true;

            if ( fieldMap.getSql() != null )
                _readonly = fieldMap.getSql().getReadonly();

            if ( fieldMap.getCollection() != null )
                _multi = true;

            // Set collection type
            if ( fieldMap.getCollection() != null ) {
              // simple arrays support
              if ("array".equals(fieldMap.getCollection().toString())) {
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
                throw new MappingException( "mapping.classNotFound", eMold.getName() );
            }
            // ssa, multi classloader feature
            // set the default classloader to the hash table
            // ssa, FIXME : Shoudln't we have a ref to the Classloader used
            // instead of asking it to the newly created class ?
            _defaultReflectService._loader = javaClass.getClassLoader();
            if ( null != _defaultReflectService._loader )
                _reflectServices.put (_defaultReflectService._loader, this._defaultReflectService);

            String fieldName = fieldMap.getName();
            String fieldType = fieldMap.getType();
            Class  declaredClass = null;
            if ( fieldType != null ) {
                try {
                    declaredClass = Types.typeFromName( javaClass.getClassLoader(), fieldType );
                    _defaultReflectService._fClass = declaredClass;
                } catch ( ClassNotFoundException cnfe ) {
                    throw new MappingException( "mapping.classNotFound", declaredClass );
                }
            }

            if ( fieldMap.getDirect() ) {

                // No accessor, map field directly.
                Class  fieldClass = _colClass!=null? _colClass : null;
                _defaultReflectService._field = findField( javaClass, fieldName, fieldClass );
                if ( _defaultReflectService._field == null )
                    throw new MappingException( Messages.format("mapping.fieldNotAccessible", fieldName, javaClass.getName()) );
                _defaultReflectService._fClass = _defaultReflectService._field.getType();
                if ( _defaultReflectService._field.getModifiers() != Modifier.PUBLIC &&
                     _defaultReflectService._field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
                    throw new MappingException( Messages.format("mapping.fieldNotAccessible", _defaultReflectService._field.getName(),
                                            _defaultReflectService._field.getDeclaringClass().getName()) );
            } else if ( fieldMap.getGetMethod() == null && fieldMap.getSetMethod() == null ) {

                // Container object, map field to fields of the container
                int point;
                ArrayList getSeq = new ArrayList();
                ArrayList setSeq = new ArrayList();
                String name = fieldMap.getName();
                Class last;
                Method method;
                String methodName = null;

                try {
                    while ( true ) {
                        point = name.indexOf( '.' );
                        if ( point < 0 )
                            break;
                        last = javaClass;
                        methodName = "get" + capitalize( name.substring( 0, point ) );
                        method = javaClass.getMethod( methodName, null );
                        name = name.substring( point + 1 );
                        // Make sure method is not abstract/static
                        // (note: Class.getMethod() returns only public methods).
                        if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                             ( method.getModifiers() & Modifier.STATIC ) != 0 )
                            throw new MappingException( "mapping.accessorNotAccessible",
                                                        methodName, javaClass.getName() );
                        getSeq.add( method );
                        javaClass = method.getReturnType();
                        // setter;   Note: javaClass already changed, use "last"
                        methodName = "set" + methodName.substring(3);
                        try {
                            method = last.getMethod( methodName, new Class[] { javaClass } );
                            if ( ( method.getModifiers() & Modifier.ABSTRACT ) != 0 ||
                                 ( method.getModifiers() & Modifier.STATIC ) != 0 )
                                method = null;
                        } catch ( Exception except ) {
                            method = null;
                        }
                        setSeq.add( method );
                    }
                } catch (Exception ex) {
                    throw new MappingException( "mapping.accessorNotFound",
                           methodName, null, javaClass.getName() );
                }
                if ( getSeq.size() > 0 ) {
                    _defaultReflectService._getSequence = (Method[]) getSeq.toArray( new Method[ 0 ] );
                    _defaultReflectService._setSequence = (Method[]) setSeq.toArray( new Method[ 0 ] );
                }
                Class methodClass = _colClass!=null? _colClass: null;
                _defaultReflectService._getMethod = findAccessor( javaClass, "get" + capitalize( name ), methodClass, true );

                if ( _defaultReflectService._getMethod == null )
                    throw new MappingException( "mapping.accessorNotFound",
                            "get" + name, null, javaClass.getName() );

                // update fClass, because we can't tell between primitive
                // and primitive wrapper from the mapping
                if ( _colClass == null )
                    _defaultReflectService._fClass = _defaultReflectService._getMethod.getReturnType();

                _defaultReflectService._setMethod = findAccessor( javaClass, "set" + capitalize( name ), methodClass, false );

                if ( _defaultReflectService._setMethod == null )
                    _defaultReflectService._addMethod
                        = findAccessor( javaClass, "add" + capitalize( name ), null, false );

                if ( _defaultReflectService._addMethod == null && name.endsWith("s") )
                    _defaultReflectService._addMethod
                        = findAccessor( javaClass, "add" + capitalize( name ).substring(0,name.length()-1), declaredClass, false );

                if ( _defaultReflectService._setMethod == null && _defaultReflectService._addMethod == null )
                    throw new MappingException( "mapping.accessorNotFound",
                        "set/add" + capitalize( name ), declaredClass, javaClass.getName() );

                if ( _defaultReflectService._addMethod != null )
                    _addable = true;

            } else {

                // Bean type object, map field to get<Method>/set<Method>

                Class methodClass = _defaultReflectService._fClass;

                // First look up the get accessors
                if ( fieldMap.getGetMethod() != null ) {
                    if ( _colClass != null ) {
                        _defaultReflectService._getMethod
                            = findAccessor( javaClass, fieldMap.getGetMethod(), _colClass, true );
                    } else {
                        _defaultReflectService._getMethod
                            = findAccessor( javaClass, fieldMap.getGetMethod(), methodClass, true );
                    }

                    if ( _defaultReflectService._getMethod == null )
                        throw new MappingException( "mapping.accessorNotFound",
                                fieldMap.getGetMethod(), methodClass, javaClass.getName() );

                    // set/reset the fClass to actual field class
                    if ( _colClass == null )
                        _defaultReflectService._fClass = _defaultReflectService._getMethod.getReturnType();

                } else {
                    throw new MappingException( "mapping.getMethodMappingNotFound",
                                                _colClass!=null?_colClass:methodClass, javaClass.getName() );
                }

                // Second look up the set/add accessor
                if ( fieldMap.getSetMethod() != null ) {

                    if ( _colClass != null ) {
                        _defaultReflectService._setMethod = findAccessor( javaClass, fieldMap.getSetMethod(), _colClass, false );

                        // find addXXX method only if lazy loading is turned off
                        if ( _defaultReflectService._setMethod == null && !fieldMap.getLazy() )
                            _defaultReflectService._addMethod = findAccessor( javaClass, fieldMap.getSetMethod(), methodClass, false );

                    } else {
                        // find setXXX method
                        _defaultReflectService._setMethod = findAccessor( javaClass, fieldMap.getSetMethod(), methodClass, false );
                    }


                    if ( _defaultReflectService._setMethod == null && _defaultReflectService._addMethod == null )
                        throw new MappingException( "mapping.accessorNotFound",
                                fieldMap.getSetMethod(), methodClass, javaClass.getName() );

                    if ( _defaultReflectService._fClass == null )
                        _defaultReflectService._fClass = _defaultReflectService._setMethod.getParameterTypes()[ 0 ];
                } else {
                    throw new MappingException( "mapping.setMethodMappingNotFound",
                                                _colClass!=null?_colClass:methodClass, javaClass.getName() );
                }
            }


            // If there is a create method, add it to the field handler

            // Note: create method is used for enclosing object of this field to determine
            // what exact instance to be created.
            if ( fieldMap.getCreateMethod() != null ) {
                try {
                    _defaultReflectService._createMethod = javaClass.getMethod( fieldMap.getCreateMethod(), null );
                } catch ( Exception except ) {
                    // No such/access to method
                    throw new MappingException( "mapping.createMethodNotFound",
                                                fieldMap.getCreateMethod(), javaClass.getName() );
                }
            } else if ( fieldMap.getName() != null && ! Types.isSimpleType( _defaultReflectService._fClass ) ) {
                try {
                    Method method;

                    method = javaClass.getMethod( "create" + capitalize( fieldMap.getName() ), null );
                    _defaultReflectService._createMethod = method;
                } catch ( Exception except ) { }
            }

            // If there is an has/delete method, add them to field handler
            if ( fieldMap.getName() != null ) {
                Method hasMethod = null;
                Method deleteMethod = null;

                try {
                    hasMethod = javaClass.getMethod( "has" + capitalize( fieldMap.getName() ), null );
                    if ( ( hasMethod.getModifiers() & Modifier.PUBLIC ) == 0 ||
                         ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 )
                        hasMethod = null;
                    try {
                        if ( ( hasMethod.getModifiers() & Modifier.PUBLIC ) == 0 ||
                             ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 )
                            deleteMethod = null;
                        deleteMethod = javaClass.getMethod( "delete" + capitalize( fieldMap.getName() ), null );
                    } catch ( Exception except ) { }
                    _defaultReflectService._hasMethod = hasMethod;
                    _defaultReflectService._deleteMethod = deleteMethod;
                } catch ( Exception except ) { }
            }

            if ( _defaultReflectService._field == null && _defaultReflectService._setMethod == null && _defaultReflectService._getMethod == null ) {
                throw new MappingException( "_field or _setMethod can't be created" );
            }

            ds.pairFieldClass( this, _fType );
        } catch ( NullPointerException e ) {
            throw new MappingException("Unexpected Null pointer!\n"+e);
        }
        _fieldName = fieldMap.getName();

        // If the field is of a primitive type we use the default value
        _default = Types.getDefault( _defaultReflectService._fClass );
        // make the default to null for wrappers of primitives
        if ( !_defaultReflectService._fClass.isPrimitive() )
            _default = null;

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
    private Field findField( Class javaClass, String fieldName, Class fieldType )
        throws MappingException
    {
        Field field;

        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            field = javaClass.getField( fieldName );
            if ( field.getModifiers() != Modifier.PUBLIC &&
                 field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
                throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
            if ( fieldType == null )
                fieldType = Types.typeFromPrimitive( field.getType() );
            else if ( Types.typeFromPrimitive( fieldType ) != Types.typeFromPrimitive( field.getType() ) )
                throw new MappingException( "mapping.fieldTypeMismatch", field, fieldType.getName() );
            return field;
        } catch ( NoSuchFieldException except ) {
        } catch ( SecurityException except ) {
        }
        return null;
    }


    /**
     * Returns the named accessor. Uses reflection to return the named
     * accessor and check the return value or parameter type, if
     * specified.
     *
     * @param javaClass The class to which the field belongs
     * @param methodName The name of the accessor method
     * @param fieldType The type of the field if known, or null
     * @param getMethod True if get method, false if set method
     * @return The method, null if not found
     * @throws MappingException The method is not accessible or is not of the
     *  specified type
     */
    private static Method findAccessor( Class javaClass, String methodName,
                                        Class fieldType, boolean getMethod )
        throws MappingException
    {
        Method   method;
        Method[] methods;
        int      i;

        try {
            if ( getMethod ) {
                // Get method: look for the named method or prepend get to the
                // method name. Look up the field and potentially check the
                // return type.
                method = javaClass.getMethod( methodName, new Class[ 0 ] );
                if ( fieldType == null ) {
                    fieldType = Types.typeFromPrimitive( method.getReturnType() );
                } else if ( fieldType == java.io.Serializable.class && java.io.Serializable.class.isAssignableFrom( method.getReturnType() ) ) {
                    // special case
                } else if ( ! Types.typeFromPrimitive( fieldType ).isAssignableFrom( Types.typeFromPrimitive( method.getReturnType() ) ) )
                    throw new MappingException( "mapping.accessorReturnTypeMismatch",
                                                method, fieldType.getName() );
            } else {
                // Set method: look for the named method or prepend set to the
                // method name. If the field type is know, look up a suitable
                // method. If the fielf type is unknown, lookup the first
                // method with that name and one parameter.
                if ( fieldType != null ) {
                    try {
                        method = javaClass.getMethod( methodName, new Class[] { fieldType } );
                    } catch ( Exception except ) {
                        method = javaClass.getMethod( methodName, new Class[] { Types.typeFromPrimitive( fieldType ) } );
                    }
                } else {
                    methods = javaClass.getMethods();
                    method = null;
                    for ( i = 0 ; i < methods.length ; ++i ) {
                        if ( methods[ i ].getName().equals( methodName ) &&
                             methods[ i ].getParameterTypes().length == 1 ) {
                            method = methods[ i ];
                            break;
                        }
                    }
                    if ( method == null )
                        return null;
                }
            }
            // Make sure method is public and not abstract/static.
            if ( ( method.getModifiers() & Modifier.PUBLIC ) == 0 ||
                 ( method.getModifiers() & Modifier.STATIC ) != 0 )
                throw new MappingException( "mapping.accessorNotAccessible",
                                            methodName, javaClass.getName() );
            return method;
        } catch ( MappingException except ) {
            throw except;
        } catch ( Exception except ) {
        }
        return null;
    }

    private String capitalize( String name )
    {
        char first;

        first = name.charAt( 0 );
        if ( Character.isUpperCase( first  ) )
            return name;
        return Character.toUpperCase( first ) + name.substring( 1 );
    }

    /**
     * Get the ReeflectService in used given a ClassLoadaer instance
     *
     * @param loader the current ClassLoader's instance.
     * @return the <code>ReflectService</code> instance. If doess't yet exist
     * for the given classloader, then it creates one prior to returning it.
     */
    private ReflectService getContextReflectService ( ClassLoader loader ) {

        if ( null == loader || this._defaultReflectService._loader == loader)
            return this._defaultReflectService;

        ReflectService resultReflectService = (ReflectService) _reflectServices.get(loader);
        if ( null == resultReflectService ) {
            // create a new Refelect service and store it in the hashtable
            resultReflectService = new ReflectService ( _defaultReflectService, loader );
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
        ReflectService () { }

        /**
         * Contructs a ReflectService object based on the instance provided
         * and uses the <code>ClassLoader</code> to build Reflection fields.
         * @param refSrv the ReflectService that serve as a based for the new instance
         * @loader the new ClassLoader
         */
        ReflectService ( ReflectService refSrv, ClassLoader loader )
        {
            this._loader = loader;
            this._fClass = cloneClass ( refSrv._fClass );
            this._field  = cloneField ( refSrv._field );
            this._getSequence = cloneMethods ( refSrv._getSequence );
            this._setSequence = cloneMethods ( refSrv._setSequence );
            this._getMethod = cloneMethod( refSrv._getMethod );
            this._addMethod = cloneMethod( refSrv._addMethod );
            this._setMethod = cloneMethod( refSrv._setMethod );
            this._hasMethod = cloneMethod( refSrv._hasMethod );
            this._deleteMethod = cloneMethod( refSrv._deleteMethod );
            this._createMethod = cloneMethod( refSrv._createMethod );

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
        private Field cloneField ( Field originalField )
        {
            if ( null == originalField )
                return null;

            Field resultField = null;
            try {
                resultField = originalField.getDeclaringClass().getField(originalField.getName());
            }catch (NoSuchFieldException e) {
                // ssa, FIXME shoudl never happen
                e.printStackTrace();
            }
            return resultField;
        }

        /**
         * constructs a Method instance with the current ClassLoader
         */
        private Method cloneMethod ( Method originalMethod )
        {
            if ( null == originalMethod)
                return null;

            Method resultMethod = null;
            try {
                Class newCls = loadClass( originalMethod.getDeclaringClass().getName() );
                String methodName = originalMethod.getName();
                Class[] methodParams = originalMethod.getParameterTypes();
                for(int i = 0; i < methodParams.length; i++) {
                      if (!methodParams[i].isPrimitive()) {
                              methodParams[i] = loadClass(methodParams[i].getName());
                      }
                }
                resultMethod = newCls.getMethod( methodName, methodParams );
            } catch ( NoSuchMethodException e ) {
                // ssa, FIXME shoudl never happen
                e.printStackTrace();
            }
            return resultMethod;
        }

        /**
         * constructs an Array of Method instances with the current ClassLoader
         */
        private Method[] cloneMethods ( Method[] originalMethods )
        {
            if ( null == originalMethods )
                return null;

            Method [] resultMethods = new Method[originalMethods.length];
            for ( int i=0 ; i<originalMethods.length ; i++ ) {
                resultMethods[i] = cloneMethod ( originalMethods[ i ] );
            }
            return resultMethods;
        }

        /**
         * constructs a <code>Class</code> instance with the current ClassLoader
         */
        private Class cloneClass ( Class originalClass )
        {
            if ( null == originalClass )
                return null;
            if ( originalClass.isPrimitive() )
                return originalClass;

            return loadClass ( originalClass.getName() );
        }

        /**
         * Helper method to load the class given its full qualified name
         */
        private Class loadClass ( String name )
        {
            Class resultClass = null;
            try {
                if ( null != _loader )
                    resultClass  = _loader.loadClass( name );
                else
                    resultClass = Class.forName( name );
            } catch (ClassNotFoundException e) {
                // ssa, FIXME : should never happen
                e.printStackTrace();
            }

            return resultClass;
        }
    }

}

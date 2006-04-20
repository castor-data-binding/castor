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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping.loader;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import org.exolab.castor.mapping.AbstractFieldHandler;
import org.exolab.castor.mapping.ExtendedFieldHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingRuntimeException;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.util.Messages;


/**
 * A field handler that knows how to get/set the values of a field
 * directly or through the get/set methods. Uses reflection.
 * <p>
 * Note: the field Java type is obtained from {@link TypeInfo#getFieldType()},
 * but if the field is a collection, the actual field/accessor type is
 * obtained from {@link TypeInfo#getCollectionHandler} and the object to create
 * (with {@link #newInstance}) is the former field type.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public final class FieldHandlerImpl
    extends AbstractFieldHandler
{


    /**
     * The prefix for an "add" method
    **/
    private static final String ADD_PREFIX = "add";
    

    /**
     * The underlying field handler used by this handler.
     */
    private final FieldHandler _handler;


    /**
     * The Java field described and accessed through this descriptor.
     */
    private final Field   _field;


    /**
     * The sequence of methods used to obtain the nested field. May be null.
     */
    private Method[]      _getSequence;


    /**
     * The sequence of methods used to create the nested object. May be null.
     */
    private Method[]      _setSequence;


    /** 
      * The method used to "incrementally" set the value of this field.  
      * This is only used if the field is a collection 
      */ 
    private Method        _addMethod; 


    /**
     * The method used to obtain the value of this field. May be null.
     */
    private Method        _getMethod;


    /**
     * The method used to set the value of this field. May be null.
     */
    private Method        _setMethod;


    /**
     * The method used to check if the value of this field exists. May be null.
     */
    private Method  _hasMethod;


    /**
     * The method used to delete the value of this field. May be null.
     */
    private Method  _deleteMethod;


    /**
     * The method used to create a new instance of the field.
     */
    private Method  _createMethod;


    /**
     * The Java field name.
     */
    private final String  _fieldName;


    /**
     * The Java field type.
     */
    private final Class  _fieldType;


    /**
     * True if this field is an immutable type.
     */
    private final boolean  _immutable;


    /**
     * The default value for primitive fields. Will be set if the field is null.
     */
    private final Object   _default;


    /**
     * Returns true if the field is required. Required fields cannot
     * be null on output.
     */
    private boolean        _required;


    /**
     * Convertor to apply when setting the value of the field. Converts from
     * the value to the field type. Null if no convertor is required.
     */
    private TypeConvertor  _convertTo = null;


    /**
     * Convertor to apply when reading the value of the field. Converts from
     * the field type to the return value. Null if no convertor is required.
     */
    private TypeConvertor  _convertFrom = null;


    /**
     * Parameter for the type convertor. Null if no convertor is required
     * or on parameter is specified.
     */
    private final String         _convertParam;


    /**
     * The collection handler for multi valued fields.
     */
    private final CollectionHandler _colHandler;


    /**
     * Construct a new field handler for the specified field. The field must
     * be public, and may not be static or transient. The field name is
     * determined from the Java field, the type from the type information.
     *
     * @param field The field being described
     * @param typeInfo Type information
     * @throws MappingException If the field is not public, is static or
     *    transient
     */
    public FieldHandlerImpl( FieldHandler handler, TypeInfo typeInfo )
        throws MappingException
    {
        _handler = handler;
        _field = null;
        _fieldName = handler.toString();
        _fieldType = Types.typeFromPrimitive( typeInfo.getFieldType() );
        _immutable = typeInfo.isImmutable();
        _required = typeInfo.isRequired();
        _default = typeInfo.getDefaultValue();
        _convertTo = typeInfo.getConvertorTo();
        _convertFrom = typeInfo.getConvertorFrom();
        _convertParam = typeInfo.getConvertorParam();
        _colHandler = typeInfo.getCollectionHandler();
    }


    /**
     * Construct a new field handler for the specified field. The field must
     * be public, and may not be static or transient. The field name is
     * determined from the Java field, the type from the type information.
     *
     * @param field The field being described
     * @param typeInfo Type information
     * @throws MappingException If the field is not public, is static or
     *    transient
     */
    public FieldHandlerImpl( Field field, TypeInfo typeInfo )
        throws MappingException
    {
        if ( field.getModifiers() != Modifier.PUBLIC &&
             field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
            throw new MappingException( "mapping.fieldNotAccessible", field.getName(),
                                        field.getDeclaringClass().getName() );
        _handler = null;
        _field = field;
        _fieldType = Types.typeFromPrimitive( typeInfo.getFieldType() );
        _fieldName = field.getName() + "(" + _fieldType.getName() + ")";
        _immutable = typeInfo.isImmutable();
        _required = typeInfo.isRequired();
        // If the field is of a primitive type or if it is required
        // we use the default value
        if ( _field.getType().isPrimitive() )
            _default = typeInfo.getDefaultValue();
        else
            _default = null;
        _convertTo = typeInfo.getConvertorTo();
        _convertFrom = typeInfo.getConvertorFrom();
        _convertParam = typeInfo.getConvertorParam();
        _colHandler = typeInfo.getCollectionHandler();
    }


    /**
     * Construct a new field handler for the specified field that
     * is accessed through the accessor methods (get/set). The accessor
     * methods must be public and not static. The field name is
     * required for descriptive purposes. The field type must match
     * the return value of the get method and the single parameter of
     * the set method. Either get or set methods are optional.
     *
     * @param fieldName The field being described
     * @param getMethod The method used to retrieve the field value,
     *  must accept no parameters and have a return type castable to
     *  the field type
     * @param setMethod The method used to set the field value, must
     *  accept a single paramater that is castable to the field type
     * @param typeInfo Type information
     * @throws MappingException If the get or set method are not public,
     *   are static, or do not specify the proper types
     *
     */
    public FieldHandlerImpl( String fieldName, Method[] getSequence, Method[] setSequence, 
                             Method getMethod, Method setMethod, TypeInfo typeInfo )
        throws MappingException
    {
        _handler = null;
        _field = null;
        if ( fieldName == null )
            throw new IllegalArgumentException( "Argument 'fieldName' is null" );
            
        // Originally commented out by Oleg....not sure why?
        // if ( getMethod == null && setMethod == null )
        //    throw new IllegalArgumentException( "Both arguments 'getMethod' and 'setMethod' are null" );
        
        _getSequence = getSequence;
        _setSequence = setSequence;

        if ( setMethod != null ) {
            //-- might be an "add" method
            if ( setMethod.getName().startsWith(ADD_PREFIX) ) {
                Class pType = setMethod.getParameterTypes()[0];
                if (pType != typeInfo.getFieldType() )
                    setAddMethod (setMethod);
                else 
                    setWriteMethod(setMethod);
            }
            else setWriteMethod( setMethod );
        }
        
        if ( getMethod != null )
            setReadMethod(getMethod);
        
        _fieldType = Types.typeFromPrimitive( typeInfo.getFieldType() );
        _fieldName = fieldName + "(" + _fieldType.getName() + ")";
        _immutable = typeInfo.isImmutable();
        _required = typeInfo.isRequired();
        // If the field is of a primitive type or if it is required
        // we use the default value 
        if ( setMethod != null && setMethod.getParameterTypes()[0].isPrimitive() )
            _default = typeInfo.getDefaultValue();
        else
            _default = null;
        _convertTo = typeInfo.getConvertorTo();
        _convertFrom = typeInfo.getConvertorFrom();
        _convertParam = typeInfo.getConvertorParam();
        _colHandler = typeInfo.getCollectionHandler();
    }

        public TypeConvertor getConvertFrom() {
            return _convertFrom;
        }
        public TypeConvertor getConvertTo() {
            return _convertTo;
        }
        public String getConvertParam() {
            return _convertParam;
        }


    public Object getValue( Object object )
    {
        Object value;

        try {
            // If field is accessed directly, get it's value, if not
            // need to call get method. It's possible to not have a
            // way to access the field.
            if ( _handler != null )
                value = _handler.getValue( object );
            else if ( _field != null )
                value = _field.get( object );
            else if ( _getMethod != null ) {
                if ( _getSequence != null ) 
                    for ( int i = 0; i < _getSequence.length; i++ ) {
                        object = _getSequence[ i ].invoke( object, null );
                        if ( object == null )
                            break;
                    }
                // Some of the objects in the sequence might be null, then 
                // the value is null
                // If field has 'has' method, false means field is null
                // and do not attempt to call getValue. Otherwise, 
                if ( object == null ||
                          ( _hasMethod != null && ! ( (Boolean) _hasMethod.invoke( object, null ) ).booleanValue() ) )
                    value = null;
                else
                    value = _getMethod.invoke( object, null );
            } else
                value = null;
        } catch ( IllegalAccessException except ) {
            // This should never happen
            throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
        } catch ( InvocationTargetException except ) {
            // This should never happen
            throw new IllegalStateException( Messages.format( "mapping.schemaChangeInvocation",
                                                              toString(), except ) );
        }

        //-- If a collection, return an enumeration of it's values.
        //-- Only use collection handler, if there is no convertor
        if (( _colHandler != null ) && (_convertFrom == null)) {
            if ( value == null )
                return new CollectionHandlers.EmptyEnumerator();
            else
                return _colHandler.elements( value );
        }

        // If there is a convertor, apply it
        if ( _convertFrom == null || value == null )
            return value;
        try {
            return _convertFrom.convert( value, _convertParam );
        } catch ( ClassCastException except ) {
            throw new IllegalArgumentException( Messages.format( "mapping.wrongConvertor",  value.getClass().getName() ) );
        }
    }
    


    public void setValue( Object object, Object value )
    {
        if ( _colHandler == null || _addMethod != null) {

            // If there is a convertor, apply conversion here.
            if ( value != null && _convertTo != null ) {
                try {
                    value = _convertTo.convert( value, _convertParam );
                } catch ( ClassCastException except ) {
                    throw new IllegalArgumentException( Messages.format( "mapping.wrongConvertor", value.getClass().getName() ) );
                }
            }
            else {
                //-- unwrap MapItem if necessary
                //if (_colHandler != null) {
                //    if ((value instanceof MapItem) && (_fieldType != MapItem.class))
                //    {
                //        value = ((MapItem)value).getValue();
                //    }
                //}
            }


            try {
                if ( _handler != null )
                    _handler.setValue( object, value );
                else if ( _field != null )
                    _field.set( object, value == null ? _default : value );
                else {
                    
                    //-- either add or set
                    Method setter = selectWriteMethod( value );
                    
                    if (setter != null) {
                        if ( _getSequence != null ) 
                            for ( int i = 0; i < _getSequence.length; i++ ) {
                                Object last;

                                last = object;
                                object = _getSequence[ i ].invoke( object, null );
                                if ( object == null ) {
                                    // if the value is not null, we must instantiate
                                    // the object in the sequence
                                    if ( value == null || _setSequence[ i ] == null )
                                        break;
                                    else {
                                        object = Types.newInstance( _getSequence[ i ].getReturnType() );
                                        _setSequence[ i ].invoke( last, new Object[] { object } );
                                    }
                                }
                            }
                        if ( object != null ) {
                            if ( value == null && _deleteMethod != null )
                                _deleteMethod.invoke( object, null );
                            else
                                setter.invoke( object, new Object[] { value == null ? _default : value } );
                        }
                    }
                }
                // If the field has no set method, ignore it.
                // If this is a problem, identity it someplace else.
            } catch ( IllegalArgumentException except ) {
                // Graceful way of dealing with unwrapping exception
                if ( value == null )
                    throw new IllegalArgumentException( Messages.format( "mapping.typeConversionNull", toString() ) );
                else
                    throw new IllegalArgumentException( Messages.format( "mapping.typeConversion",
                                                                         toString(), value.getClass().getName() ) );
            } catch ( IllegalAccessException except ) {
                // This should never happen
                throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
            } catch ( InvocationTargetException except ) {
                // This should never happen
                throw new MappingRuntimeException(except.getTargetException());
            }

        } else if ( value != null ) {
            Object collect;
            try {
                // Get the field value (the collection), add the value to it,
                // possibly yielding a new collection (in the case of an array),
                // and set that collection back into the field.
                if ( _handler != null ) {
                    collect = _handler.getValue( object );
                    collect = _colHandler.add( collect, value );
                    if ( collect != null )
                        _handler.setValue( object, collect );
                } else if ( _field != null ) {
                    collect = _field.get( object );
                    if (collect == null) {
                        // The type of the collection.
                        Class type = _field.getType();
                        //-- Handle Arrays, we need to declare the array with
                        //-- the correct type. The other cases are handled in  
                        //-- the J1CollectionHandler during the 
                        //-- add(collect,value) call
                        if (type.isArray()) {
                            Class componentType = type.getComponentType();
                            Class valueType = value.getClass();
                            if (componentType.isPrimitive() || 
                               ((!valueType.isArray()) && (valueType != componentType))) 
                            {
                                try {
                                    collect = Array.newInstance(componentType, 0);
                                } 
                                catch (Exception e) {
                                    String err = "Unable to instantiate an array of '" + 
                                        componentType + "' : " + e;
                                    throw new IllegalStateException(err);
                                }
                            }
                        }
                    }
                    collect = _colHandler.add( collect, value );
                    if ( collect != null )
                        _field.set( object, collect );
                        
                } 
                else if ( _getMethod != null ) {
                    if ( _getSequence != null ) 
                        for ( int i = 0; i < _getSequence.length; i++ ) 
                            object = _getSequence[ i ].invoke( object, null );
                    collect = _getMethod.invoke( object, null );

                    // If we deal with a collection who is an array of primitive
                    // and that has not been instantiated, we have to handle the
                    // instantiation here rather than in J1CollectionHandler,
                    // because we have acces to the Field object here.
                    boolean setCollection = false;
                    if (collect == null) {
                        // The return type of the get method should be the type of the collection.
                        Class type = _getMethod.getReturnType();
                        
                        //-- Handle Arrays, we need to declare the array with
                        //-- the correct type. The other cases are handled in  
                        //-- the J1CollectionHandler during the 
                        //-- add(collect,value) call
                        if (type.isArray()) {
                            Class componentType = type.getComponentType();
                            Class valueType = value.getClass();
                            if (componentType.isPrimitive() || 
                               ((!valueType.isArray()) && (valueType != componentType))) 
                            {
                                try {
                                    collect = Array.newInstance(componentType, 0);
                                } 
                                catch (Exception e) {
                                    String err = "Unable to instantiate an array of '" + 
                                        componentType + "' : " + e;
                                    throw new IllegalStateException(err);
                                }
                            }
                        }
                        setCollection = true;
                    }
                    else {
                        setCollection = collect.getClass().isArray();
                    }

                    Object tmp = _colHandler.add(collect, value);
                    
                    //-- make sure we do not overwrite collect unless
                    //-- the new collection is not null
                    if (tmp != null) collect = tmp;

                    if ( setCollection && (_setMethod != null))
                        _setMethod.invoke( object, new Object[] { collect } );
                }                
            } catch ( IllegalAccessException except ) {
                // This should never happen
                throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
            } catch ( InvocationTargetException except ) {
                // This should never happen
                throw new MappingRuntimeException(except.getTargetException());
            }

        }
    }


    public void resetValue( Object object )
    {
        if ( _colHandler == null ) {

            try {
                if ( _handler != null )
                    _handler.resetValue( object );
                else if ( _field != null )
                    _field.set( object, _default );
                else if ( _setMethod != null ) {
                    if ( _getSequence != null ) 
                        for ( int i = 0; i < _getSequence.length; i++ ) {
                            object = _getSequence[ i ].invoke( object, null );
                            if ( object == null )
                                break;
                        }
                    if ( object != null ) {
                        if ( _deleteMethod != null )
                            _deleteMethod.invoke( object, null );
                        else
                            _setMethod.invoke( object, new Object[] { _default } );
                    }
                }
                // If the field has no set method, ignore it.
                // If this is a problem, identity it someplace else.
            } catch ( IllegalArgumentException except ) {
                // Graceful way of dealing with unwrapping exception
                throw new IllegalArgumentException( Messages.format( "mapping.typeConversionNull", toString() ) );
            } catch ( IllegalAccessException except ) {
                // This should never happen
                throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
            } catch ( InvocationTargetException except ) {
                // This should never happen
                throw new MappingRuntimeException(except.getTargetException());
            }

        } else {
            Object collect;

            try {
                // Get the field value (the collection), add the value to it,
                // possibly yielding a new collection (in the case of an array),
                // and set that collection back into the field.
                if ( _handler != null ) {
                    _handler.resetValue( object );
                } else if ( _field != null ) {
                    collect = _field.get( object );
                    collect = _colHandler.clear( collect );
                    if ( collect != null )
                        _field.set( object, collect );
                } else if ( _getMethod != null ) {
                    if ( _getSequence != null ) 
                        for ( int i = 0; i < _getSequence.length; i++ ) 
                            object = _getSequence[ i ].invoke( object, null );
                    collect = _getMethod.invoke( object, null );
                    collect = _colHandler.clear( collect );
                    if ( collect != null && _setMethod != null)
                        _setMethod.invoke( object, new Object[] { collect } );
                }                
            } catch ( IllegalAccessException except ) {
                // This should never happen
                throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
            } catch ( InvocationTargetException except ) {
                // This should never happen
                throw new MappingRuntimeException(except.getTargetException());
            }

        }
    }


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        return newInstance( parent, null);
    }

    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @param args the set of constructor arguments
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent, Object[] args )
        throws IllegalStateException
    {
        if (_fieldType.isInterface() && _createMethod == null)
            return null;
            
        if ( _immutable )
            throw new IllegalStateException( Messages.format( "mapping.classNotConstructable", _fieldType ) );
        if ( _handler != null ) {
            if (_handler instanceof ExtendedFieldHandler)
                return ((ExtendedFieldHandler)_handler).newInstance( parent, args );
            else
                return _handler.newInstance( parent );
        }
        // If we have a create method and parent object, call the create method.
        if ( _createMethod != null && parent != null ) {
            try {
                return _createMethod.invoke( parent, args );
            } catch ( IllegalAccessException except ) {
                // This should never happen
                throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
            } catch ( InvocationTargetException except ) {
                // This should never happen
                throw new MappingRuntimeException(except.getTargetException());
            }
        }
        return Types.newInstance( _fieldType, args );
    } //-- newInstance

    /**
     * Mutator method used by {@link MappingLoader}.
     */
    void setRequired( boolean required )
    {
        _required = required;
    }

    /**
     * Sets the TypeConvertor used during calls to getValue
     *
     * @param convertor the TypeConvertor to use during calls
     * to getValue
    **/
    public void setConvertFrom(TypeConvertor convertor) {
        _convertFrom = convertor;
    } //-- setConvertFrom
    
    /**
     * Sets the TypeConvertor used during calls to setValue
     *
     * @param convertor the TypeConvertor to use during calls
     * to setValue
    **/
    public void setConvertTo(TypeConvertor convertor) {
        _convertTo = convertor;
    } //-- setConvertTo

    /**
     * Mutator method used by {@link MappingLoader} and
     * {@link org.exolab.castor.xml.Introspector}.
     * Please understand how this method is used before you start
     * playing with it! :-)
     */
    public void setCreateMethod( Method method )
        throws MappingException
    {
        if ( ( method.getModifiers() & Modifier.PUBLIC ) == 0 ||
             ( method.getModifiers() & Modifier.STATIC ) != 0 ) 
            throw new MappingException( "mapping.accessorNotAccessible",
                                        method, method.getDeclaringClass().getName() );
        if ( method.getParameterTypes().length != 0 )
            throw new MappingException( "mapping.createMethodNoParam",
                                        method, method.getDeclaringClass().getName() );
        _createMethod = method;
    }


    /**
     * Mutator method used by {@link MappingLoader} and
     * {@link org.exolab.castor.xml.Introspector}.
     * Please understand how this method is used before you start
     * playing with it! :-)
     */
    public void setHasDeleteMethod( Method hasMethod, Method deleteMethod )
        throws MappingException
    {
        if ( hasMethod != null ) {
            if ( ( hasMethod.getModifiers() & Modifier.PUBLIC ) == 0 ||
                 ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 ) 
                throw new MappingException( "mapping.accessorNotAccessible",
                                            hasMethod, hasMethod.getDeclaringClass().getName() );
            if ( hasMethod.getParameterTypes().length != 0 )
                throw new MappingException( "mapping.createMethodNoParam",
                                            hasMethod, hasMethod.getDeclaringClass().getName() );
            _hasMethod = hasMethod;
        }

        if ( deleteMethod != null ) {
            if ( ( deleteMethod.getModifiers() & Modifier.PUBLIC ) == 0 ||
                 ( deleteMethod.getModifiers() & Modifier.STATIC ) != 0 ) 
                throw new MappingException( "mapping.accessorNotAccessible",
                                            deleteMethod, deleteMethod.getDeclaringClass().getName() );
            if ( deleteMethod.getParameterTypes().length != 0 )
                throw new MappingException( "mapping.createMethodNoParam",
                                            deleteMethod, deleteMethod.getDeclaringClass().getName() );
            _deleteMethod = deleteMethod;
        }
    }


    /**
     * Mutator method used by {@link org.exolab.castor.xml.Introspector}.
     * Please understand how this method is used before you start
     * playing with it! :-)
     */
    public void setReadMethod( Method method )
        throws MappingException
    {
        if ( ( method.getModifiers() & Modifier.PUBLIC ) == 0 ||
             ( method.getModifiers() & Modifier.STATIC ) != 0 ) 
            throw new MappingException( "mapping.accessorNotAccessible",
                                        method, method.getDeclaringClass().getName() );
        if ( method.getParameterTypes().length != 0 )
            throw new MappingException( "mapping.readMethodHasParam",
                                        method, method.getDeclaringClass().getName() );
        _getMethod = method;
    }


    /**
     * Mutator method used by {@link org.exolab.castor.xml.Introspector}.
     * Please understand how this method is used before you start
     * playing with it! :-)
     */
    public void setWriteMethod( Method method )
        throws MappingException
    {
        if ( ( method.getModifiers() & Modifier.PUBLIC ) == 0 ||
             ( method.getModifiers() & Modifier.STATIC ) != 0 ) 
            throw new MappingException( "mapping.accessorNotAccessible",
                                        method, method.getDeclaringClass().getName() );
        if ( method.getParameterTypes().length != 1 )
            throw new MappingException( "mapping.writeMethodNoParam",
                                        method, method.getDeclaringClass().getName() );
        _setMethod = method;
    }


    /**
     * Mutator method used by {@link org.exolab.castor.xml.Introspector}.
     * Please understand how this method is used before you start
     * playing with it! :-)
     */
    public void setAddMethod( Method method ) 
        throws MappingException
    {
        if ( ( method.getModifiers() & Modifier.PUBLIC ) == 0 ||
             ( method.getModifiers() & Modifier.STATIC ) != 0 ) 
            throw new MappingException( "mapping.accessorNotAccessible",
                                        method, method.getDeclaringClass().getName() );
        if ( method.getParameterTypes().length != 1 )
            throw new MappingException( "mapping.writeMethodNoParam",
                                        method, method.getDeclaringClass().getName() );
        _addMethod = method;
        
        //-- make sure add method is not the same as the set method
        if (_addMethod == _setMethod) _setMethod = null;
        
    } //-- setAddMethod
    
    /** 
      * Selects the appropriate "write" method based on the 
      * value. This is used when there is an "add" method 
      * and a "set" method. 
      * 
      * @return the selected write method 
     **/ 
     private Method selectWriteMethod( Object value ) { 
          
          
         Method setter = null; 
          
         if (_setMethod != null) { 
              
             if (_addMethod == null) return _setMethod; 
              
             if (value == null) { 
                 if (_default != null) value = _default; 
                 else return _setMethod; 
             } 
              
             //-- check value's class type 
             Class paramType = _setMethod.getParameterTypes()[0]; 
                                  
             if (paramType.isAssignableFrom(value.getClass())) 
                 return _setMethod; 
         } 
          
         return _addMethod; 
          
     } //-- selectWriteMethod 
  
    /**
     * Return true if the field is a collection.
     */
    public boolean isCollection() {
        return (_colHandler != null); 
    }

    public String toString()
    {
        return _fieldName;
    }
    

}


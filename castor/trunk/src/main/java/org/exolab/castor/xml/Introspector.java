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
package org.exolab.castor.xml;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.castor.xml.JavaNaming;
import org.castor.xml.XMLConfiguration;
import org.castor.xml.InternalContext;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.FieldHandlerFactory;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.ReflectionUtil;
import org.exolab.castor.xml.descriptors.CoreDescriptors;
import org.exolab.castor.xml.handlers.ContainerFieldHandler;
import org.exolab.castor.xml.handlers.DateFieldHandler;
import org.exolab.castor.xml.handlers.DefaultFieldHandlerFactory;
import org.exolab.castor.xml.util.ContainerElement;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * A Helper class for the Marshaller and Unmarshaller,
 * basically the common code base between the two. This
 * class handles the introspection to dynamically create
 * descriptors.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
 */
public final class Introspector {

    /**
     * The default FieldHandlerFactory
     */
    private FieldHandlerFactory DEFAULT_HANDLER_FACTORY = new DefaultFieldHandlerFactory();

    private static final Class[] EMPTY_CLASS_ARGS = new Class[0];

    /**
     * Name of the java.util.List collection
     */
    private static final String LIST = "java.util.List";

    /**
     * Name of the java.util.Map collection
     */
    private static final String MAP = "java.util.Map";

    /**
     * Name of the java.util.Map collection
     */
    private static final String SET_COLLECTION = "java.util.Set";

    /**
     * Used as a prefix for the name of a container field
     */
    private static final String COLLECTION_WRAPPER_PREFIX = "##container_for_";


    /**
     * The default flag indicating whether or not collections
     * (arrays, vectors, etc) should be wrapped in a container element.
     *
     * @see _wrapCollectionsInContainer
     */
    private static final boolean WRAP_COLLECTIONS_DEFAULT = false;

    /**
     * The set of available collections to use
     * during introspection. JDK dependant.
    **/
    private static final Class[] _collections = loadCollections();


    /**
     * The default naming conventions
    **/
    private static XMLNaming _defaultNaming = null;

    /**
     * The naming conventions to use
    **/
    private XMLNaming _xmlNaming = null;

    /**
     * The NodeType to use for primitives
    **/
    private NodeType _primitiveNodeType = null;


    /**
     * The variable flag indicating whether or not collections
     * (arrays, vectors, etc) should be wrapped in a container element.
     * For example:
     *
     * <pre>
     *    &lt;foos&gt;
     *       &lt;foo&gt;foo1&lt;/foo&gt;
     *       &lt;foo&gt;foo2&lt;/foo&gt;
     *    &lt;/foos&gt;
     *
     *   instead of the default:
     *
     *    &lt;foos&gt;foo1&lt;foos&gt;
     *    &lt;foos&gt;foo2&lt;/foos&gt;
     *
     * </pre>
     *
     */
    private boolean _wrapCollectionsInContainer = WRAP_COLLECTIONS_DEFAULT;


    /**
     * The set of registered FieldHandlerFactory instances
     */
    private Vector _handlerFactoryList = null;

    /**
     * The set of registered FieldHandlerFactory instances
     * associated with their supported types
     */
    private Hashtable _handlerFactoryMap =  null;

    /**
     * A flag indicating that MapKeys should be saved. To remain
     * backward compatible this may be disable via the
     * castor.properties.
     */
    private boolean _saveMapKeys = true;

    /**
     * Specifies class loader to be used.
     */
    private ClassLoader _classLoader = null;
    
    /**
     * The {@link JavaNaming} to be used.
     */
    private JavaNaming _javaNaming;

    private InternalContext _internalContext;

    /**
     * Creates a new instance of the Introspector.
     */
    public Introspector() {
        this(null);
    } //-- Introspector

    /**
     * Creates a new instance of the Introspector.
     *
     * @param classLoader
     */
    public Introspector(final ClassLoader classLoader) {
        super();
        _classLoader = classLoader;
        init();
    } //-- Introspector

    private void init() {
        if (_internalContext != null) {
            _javaNaming = _internalContext.getJavaNaming();
            _xmlNaming = _internalContext.getXMLNaming();
            setPrimitiveNodeType(_internalContext.getPrimitiveNodeType());
            _wrapCollectionsInContainer = _internalContext.getBooleanProperty(XMLConfiguration.WRAP_COLLECTIONS_PROPERTY).booleanValue();
            _saveMapKeys = 
                _internalContext.getBooleanProperty(XMLConfiguration.SAVE_MAP_KEYS).booleanValue();
        }
    } //-- init

    public void setInternalContext(InternalContext internalContext) {
        _internalContext = internalContext;
        init();
    }

    /**
     * Registers the given "generalized" FieldHandlerFactory with this
     * Introspector.
     *
     * @param factory the FieldHandlerFactory to add to this
     * introspector
     * @throws IllegalArgumentException if the given factory is null
     */
    public synchronized void addFieldHandlerFactory(FieldHandlerFactory factory) {
        if (factory == null) {
            String err = "The argument 'factory' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if (_handlerFactoryList == null) {
            _handlerFactoryList = new Vector();
        }
        _handlerFactoryList.addElement(factory);
        registerHandlerFactory(factory);
    } //-- addFieldHandlerFactory

    /**
     * Returns the NodeType for java primitives
     *
     * @return the NodeType for java primitives
    **/
    public NodeType getPrimitiveNodeType() {
        return _primitiveNodeType;
    } //-- getPrimitiveNodeType

    /**
     * Creates an XMLClassDescriptor for the given class by using Reflection.
     * @param c the Class to create the XMLClassDescriptor for
     * @return the new XMLClassDescriptor created for the given class
     * @exception MarshalException when an error occurs during the creation
     * of the ClassDescriptor.
     **/
    public XMLClassDescriptor generateClassDescriptor(Class c)
        throws MarshalException
    {
        return generateClassDescriptor(c, null);
    } //-- generateClassDescriptor(Class)

    /**
     * Creates an XMLClassDescriptor for the given class by using Reflection.
     * @param c the Class to create the XMLClassDescriptor for
     * @param errorWriter a PrintWriter to print error information to
     * @return the new XMLClassDescriptor created for the given class
     * @exception MarshalException when an error occurs during the creation
     * of the ClassDescriptor.
     **/
    public XMLClassDescriptor generateClassDescriptor
        (Class c, PrintWriter errorWriter) throws MarshalException
    {

        if (c == null) return null;

        //-- handle arrays
        if (c.isArray()) return null;

        //-- handle base objects
        if ((c == Void.class) ||
            (c == Class.class)||
            (c == Object.class)) {
            throw new MarshalException (
                MarshalException.BASE_CLASS_OR_VOID_ERR );
        }

        //-- handle core descriptors
        XMLClassDescriptor coreDesc = CoreDescriptors.getDescriptor(c);
        if (coreDesc != null)
            return coreDesc;


        //--------------------------/
        //- handle complex objects -/
        //--------------------------/

        XMLClassDescriptorImpl classDesc
            = new IntrospectedXMLClassDescriptor(c);

        Method[] methods = c.getMethods();
        List      dateDescriptors = new ArrayList(3);
        Hashtable methodSets      = new Hashtable();

        int methodCount = 0;

        Class superClass = c.getSuperclass();
        Class[] interfaces = c.getInterfaces();



        //-- create method sets
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            Class owner = method.getDeclaringClass();

            //-- ignore methods from super-class, that will be
            //-- introspected separately, if necessary
            if (owner != c) {
                //-- if declaring class is anything but
                //-- an interface, than just continue,
                //-- the field comes from a super class
                //-- (e.g. java.lang.Object)
                if (!owner.isInterface()) continue;

                //-- owner is an interface, is it an
                //-- interface this class implements
                //-- or a parent class?
                if (interfaces.length > 0) {
                    boolean found = false;
                    for (int count = 0; count < interfaces.length; count++) {
                        if (interfaces[count] == owner) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) continue;
                }
            }
            else {
                //-- look for overloaded methods
                if (superClass != null) {
                    Class[] args = method.getParameterTypes();
                    String name = method.getName();
                    Method tmpMethod = null;
                    try {
                        tmpMethod = superClass.getMethod(name, args);
                    }
                    catch(NoSuchMethodException nsme) {
                        //-- do nothing
                    }
                    if (tmpMethod != null) continue;
                }
            }


            //-- if method is static...ignore
            if ((method.getModifiers() & Modifier.STATIC) != 0) continue;

            String methodName = method.getName();

            //-- read methods
            if (methodName.startsWith(JavaNaming.METHOD_PREFIX_GET)) {
                if (method.getParameterTypes().length != 0) continue;
                //-- disable direct field access
                ++methodCount;
                //-- make sure return type is "descriptable"
                //-- and not null
                Class type = method.getReturnType();
                if (type == null) continue;
                if (!isDescriptable(type)) continue;

                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                fieldName = _javaNaming.toJavaMemberName(fieldName);

                MethodSet methodSet = (MethodSet)methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.get = method;
            }
            else if (methodName.startsWith(JavaNaming.METHOD_PREFIX_IS)) {
                if (method.getParameterTypes().length != 0) continue;
                //-- make sure type is not null, and a boolean
                Class type = method.getReturnType();
                if (type == null) continue;
                if (type.isPrimitive()) {
                    if (type != Boolean.TYPE) continue;
                }
                else {
                    if (type != Boolean.class) continue;
                }
                //-- disable direct field access
                ++methodCount;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(JavaNaming.METHOD_PREFIX_IS.length());
                fieldName = _javaNaming.toJavaMemberName(fieldName);

                MethodSet methodSet = (MethodSet)methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.get = method;
            }
            //-----------------------------------/
            //-- write methods (collection item)
            else if (methodName.startsWith(JavaNaming.METHOD_PREFIX_ADD)) {
                if (method.getParameterTypes().length != 1) continue;
                //-- disable direct field access
                ++methodCount;
                //-- make sure parameter type is "descriptable"
                if (!isDescriptable(method.getParameterTypes()[0])) continue;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                fieldName = _javaNaming.toJavaMemberName(fieldName);
                MethodSet methodSet = (MethodSet) methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.add = method;
            }
            //-- write method (singleton or collection)
            else if (methodName.startsWith(JavaNaming.METHOD_PREFIX_SET)) {
                if (method.getParameterTypes().length != 1) continue;
                //-- disable direct field access
                ++methodCount;
                //-- make sure parameter type is "descriptable"
                if (!isDescriptable(method.getParameterTypes()[0])) continue;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                fieldName = _javaNaming.toJavaMemberName(fieldName);
                MethodSet methodSet = (MethodSet) methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.set = method;
            }
            else if (methodName.startsWith(JavaNaming.METHOD_PREFIX_CREATE)) {
                if (method.getParameterTypes().length != 0) continue;
                Class type = method.getReturnType();
                //-- make sure return type is "descriptable"
                //-- and not null
                if (!isDescriptable(type)) continue;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(JavaNaming.METHOD_PREFIX_CREATE.length());
                fieldName = _javaNaming.toJavaMemberName(fieldName);
                MethodSet methodSet = (MethodSet) methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.create = method;
            }
        } //-- end create method sets


        //-- Loop Through MethodSets and create
        //-- descriptors
        Enumeration enumeration = methodSets.elements();

        while (enumeration.hasMoreElements()) {

            MethodSet methodSet = (MethodSet) enumeration.nextElement();

            //-- create XMLFieldDescriptor
            String xmlName = _xmlNaming.toXMLName(methodSet.fieldName);

            boolean isCollection = false;

            //-- calculate class type
            //-- 1st check for add-method, then set or get method
            Class type = null;
            if (methodSet.add != null) {
                type = methodSet.add.getParameterTypes()[0];
                isCollection = true;
            }

            //-- if there was no add method, use get/set methods
            //-- to calculate type.
            if (type == null) {
                if (methodSet.get != null) {
                    type = methodSet.get.getReturnType();
                }
                else if (methodSet.set != null) {
                    type = methodSet.set.getParameterTypes()[0];
                }
                else {
                    //-- if we make it here, the only method found
                    //-- was a create method, which is useless by itself.
                    continue;
                }
            }

            //-- Handle Collections
            isCollection = (isCollection || isCollection(type));

            TypeInfo typeInfo = null;
            CollectionHandler colHandler = null;

            //-- If the type is a collection and there is no add method,
            //-- then we obtain a CollectionHandler
            if (isCollection && (methodSet.add == null)) {

                try {
                    colHandler = CollectionHandlers.getHandler(type);
                }
                catch(MappingException mx) {
                    //-- No collection handler available,
                    //-- proceed anyway...
                }

                //-- Find component type
                if (type.isArray()) {
                    //-- Byte arrays are handled as a special case
                    //-- so don't use CollectionHandler
                    if (type.getComponentType() == Byte.TYPE) {
                        colHandler = null;
                    }
                    else type = type.getComponentType();
                }
            }

            typeInfo = new TypeInfo(type, null, null, false, null, colHandler);

            //-- Create FieldHandler first, before the XMLFieldDescriptor
            //-- in case we need to use a custom handler

            FieldHandler handler = null;
            boolean customHandler = false;
            try {
                handler = new FieldHandlerImpl(methodSet.fieldName,
                                                null,
                                                null,
                                                methodSet.get,
                                                methodSet.set,
                                                typeInfo);
                //-- clean up
                if (methodSet.add != null)
                    ((FieldHandlerImpl)handler).setAddMethod(methodSet.add);

                if (methodSet.create != null)
                    ((FieldHandlerImpl)handler).setCreateMethod(methodSet.create);

                //-- handle Hashtable/Map
                if (isCollection && _saveMapKeys && isMapCollection(type)) {
                    ((FieldHandlerImpl)handler).setConvertFrom(new IdentityConvertor());
                }

                //-- look for GeneralizedFieldHandler
                FieldHandlerFactory factory = getHandlerFactory(type);
                if (factory != null) {
                    GeneralizedFieldHandler gfh = factory.createFieldHandler(type);
                    if (gfh != null) {
                        gfh.setFieldHandler(handler);
                        handler = gfh;
                        customHandler = true;
                        //-- swap type with the type specified by the
                        //-- custom field handler
                        if (gfh.getFieldType() != null) {
                            type = gfh.getFieldType();
                        }
                    }
                }

            }
            catch (MappingException mx) {
                throw new MarshalException(mx);
            }


            XMLFieldDescriptorImpl fieldDesc
                = createFieldDescriptor(type, methodSet.fieldName, xmlName);

            if (isCollection) {
                fieldDesc.setMultivalued(true);
                fieldDesc.setNodeType(NodeType.Element);
            }

            //-- check for instances of java.util.Date
            if (java.util.Date.class.isAssignableFrom(type)) {
                //handler = new DateFieldHandler(handler);
                if (!customHandler) {
                    dateDescriptors.add(fieldDesc);
                }
            }

            fieldDesc.setHandler(handler);

            //-- Wrap collections?
            if (isCollection && _wrapCollectionsInContainer) {
                String fieldName = COLLECTION_WRAPPER_PREFIX + methodSet.fieldName;
                //-- If we have a field 'c' that is a collection and
                //-- we want to wrap that field in an element <e>, we
                //-- need to create a field descriptor for
                //-- an object that represents the element <e> and
                //-- acts as a go-between from the parent of 'c'
                //-- denoted as P(c) and 'c' itself
                //
                //   object model: P(c) -> c
                //   xml : <p><e><c></e><p>

                //-- Make new class descriptor for the field that
                //-- will represent the container element <e>
                Class cType = ContainerElement.class;
                XMLClassDescriptorImpl containerClassDesc = new XMLClassDescriptorImpl(cType);

                //-- add the field descriptor to our new class descriptor
                containerClassDesc.addFieldDescriptor(fieldDesc);
                //-- nullify xmlName so that auto-naming will be enabled,
                //-- we can't do this in the constructor because
                //-- XMLFieldDescriptorImpl will create a default one.
                fieldDesc.setXMLName(null);
                fieldDesc.setMatches("*");

                //-- wrap the field handler in a special container field
                //-- handler that will actually do the delgation work
                FieldHandler cHandler = new ContainerFieldHandler(handler);
                fieldDesc.setHandler(cHandler);

                fieldDesc = createFieldDescriptor(cType, fieldName, xmlName);
                fieldDesc.setClassDescriptor(containerClassDesc);
                fieldDesc.setHandler(cHandler);
            }
            //-- add FieldDescriptor to ClassDescriptor
            classDesc.addFieldDescriptor(fieldDesc);


        } //-- end of method loop

        //-- If we didn't find any methods we can try
        //-- direct field access
        if (methodCount == 0) {

            Field[] fields = c.getFields();
            Hashtable descriptors = new Hashtable();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];

                Class owner = field.getDeclaringClass();

                //-- ignore fields from super-class, that will be
                //-- introspected separately, if necessary
                if (owner != c) {
                    //-- if declaring class is anything but
                    //-- an interface, than just continue,
                    //-- the field comes from a super class
                    //-- (e.g. java.lang.Object)
                    if (!owner.isInterface()) continue;

                    //-- owner is an interface, is it an
                    //-- interface this class implements
                    //-- or a parent class?
                    if (interfaces.length > 0) {
                        boolean found = false;
                        for (int count = 0; count < interfaces.length; count++) {
                            if (interfaces[count] == owner) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) continue;
                    }
                }

                //-- make sure field is not transient or static final
                int modifiers = field.getModifiers();
                if (Modifier.isTransient(modifiers)) continue;
                if (Modifier.isFinal(modifiers) &&
                    Modifier.isStatic(modifiers))
                    continue;

                Class type = field.getType();



                if (!isDescriptable(type)) continue;

                //-- Built-in support for JDK 1.1 Collections
                //-- we need to a pluggable interface for
                //-- JDK 1.2+
                boolean isCollection = isCollection(type);


                TypeInfo typeInfo = null;
                CollectionHandler colHandler = null;

                //-- If the type is a collection and there is no add method,
                //-- then we obtain a CollectionHandler
                if (isCollection) {

                    try {
                        colHandler = CollectionHandlers.getHandler(type);
                    }
                    catch(MappingException mx) {
                        //-- No CollectionHandler available, continue
                        //-- without one...
                    }

                    //-- Find component type
                    if (type.isArray()) {
                        //-- Byte arrays are handled as a special case
                        //-- so don't use CollectionHandler
                        if (type.getComponentType() == Byte.TYPE) {
                            colHandler = null;
                        }
                        else type = type.getComponentType();

                    }
                }

                String fieldName = field.getName();
                String xmlName = _xmlNaming.toXMLName(fieldName);

                //-- Create FieldHandler first, before the XMLFieldDescriptor
                //-- in case we need to use a custom handler

                typeInfo = new TypeInfo(type, null, null, false, null, colHandler);

                FieldHandler handler = null;
                boolean customHandler = false;
                try {
                    handler = new FieldHandlerImpl(field, typeInfo);

                    //-- handle Hashtable/Map
                    if (isCollection && _saveMapKeys && isMapCollection(type)) {
                        ((FieldHandlerImpl)handler).setConvertFrom(new IdentityConvertor());
                    }

                    //-- look for GeneralizedFieldHandler
                    FieldHandlerFactory factory = getHandlerFactory(type);
                    if (factory != null) {
                        GeneralizedFieldHandler gfh = factory.createFieldHandler(type);
                        if (gfh != null) {
                            gfh.setFieldHandler(handler);
                            handler = gfh;
                            customHandler = true;
                            //-- swap type with the type specified by the
                            //-- custom field handler
                            if (gfh.getFieldType() != null) {
                                type = gfh.getFieldType();
                            }
                        }
                    }
                }
                catch (MappingException mx) {
                    throw new MarshalException(mx);
                }

                XMLFieldDescriptorImpl fieldDesc =
                        createFieldDescriptor(type, fieldName, xmlName);

                if (isCollection) {
                    fieldDesc.setNodeType(NodeType.Element);
                    fieldDesc.setMultivalued(true);
                }
                descriptors.put(xmlName, fieldDesc);
                classDesc.addFieldDescriptor(fieldDesc);
                fieldDesc.setHandler(handler);

                //-- check for instances of java.util.Date
                if (java.util.Date.class.isAssignableFrom(type)) {
                    if (!customHandler) {
                        dateDescriptors.add(fieldDesc);
                    }
                }

            }
        } //-- end of direct field access


        //-- A temporary fix for java.util.Date
        if (dateDescriptors != null) {
            for (int i = 0; i < dateDescriptors.size(); i++) {
                XMLFieldDescriptorImpl fieldDesc =
                    (XMLFieldDescriptorImpl) dateDescriptors.get(i);
                FieldHandler handler = fieldDesc.getHandler();
                fieldDesc.setImmutable(true);
                DateFieldHandler dfh = new DateFieldHandler(handler);

                //-- patch for java.sql.Date
                Class type = fieldDesc.getFieldType();
                if (java.sql.Date.class.isAssignableFrom(type)) {
                    dfh.setUseSQLDate(true);
                }
                fieldDesc.setHandler(dfh);
            }
        }

        //-- Add reference to superclass...if necessary
        if ((superClass != null) &&
            (superClass != Void.class) &&
            (superClass != Object.class) &&
            (superClass != Class.class))
        {
            try {
                XMLClassDescriptor parent = generateClassDescriptor(superClass, errorWriter);
                if (parent != null) {
                    classDesc.setExtends(parent);
                }
            }
            catch(MarshalException mx) {
                //-- Ignore for now.
            }

        }

        return classDesc;
    } //-- generateClassDescriptor

    /**
     * Removes the given FieldHandlerFactory from this Introspector
     *
     * @param factory the FieldHandlerFactory to remove
     * @return true if the given FieldHandlerFactory was removed, or
     * false otherwise.
     * @throws IllegalArgumentException if the given factory is null
     */
    public synchronized boolean removeFieldHandlerFactory(FieldHandlerFactory factory)
    {
        if (factory == null) {
            String err = "The argument 'factory' must not be null.";
            throw new IllegalArgumentException(err);
        }

        //-- if list is null, just return
        if (_handlerFactoryList == null) return false;

        if (_handlerFactoryList.removeElement(factory)) {
            //-- re-register remaining handlers
            _handlerFactoryMap.clear();
            for (int i = 0; i < _handlerFactoryList.size(); i++) {
                FieldHandlerFactory tmp =
                    (FieldHandlerFactory)_handlerFactoryList.elementAt(i);
                registerHandlerFactory(tmp);
            }
            return true;
        }
        return false;
    } //-- removeFieldHandlerFactory


    /**
     * Sets whether or not collections (arrays, vectors, etc)
     * should be wrapped in a container element. For example:
     *
     * <pre>
     *
     *    &lt;foos&gt;
     *       &lt;foo&gt;foo1&lt;/foo&gt;
     *       &lt;foo&gt;foo2&lt;/foo&gt;
     *    &lt;/foos&gt;
     *
     *   instead of the default:
     *
     *    &lt;foos&gt;foo1&lt;foos&gt;
     *    &lt;foos&gt;foo2&lt;/foos&gt;
     *
     * </pre>
     *
     * @param wrapCollections a boolean that when true indicates
     * collections should be wrapped in a container element.
     *
     */
    public void setWrapCollections(boolean wrapCollections) {
        _wrapCollectionsInContainer = wrapCollections;
    } //-- setWrapCollections

    /**
     * Returns true if the given XMLClassDescriptor was created via
     * introspection
    **/
    public static boolean introspected(XMLClassDescriptor descriptor) {
        return (descriptor instanceof IntrospectedXMLClassDescriptor);
    } //-- introspected

    /**
     * Returns true if the given Class can be marshalled.
     *
     * @param type the Class to check marshallability for.
     * @return true if the given Class can be marshalled.
    **/
    public static boolean marshallable(Class type) {

        //-- make sure type is not Void, or Class;
        if  (type == Void.class || type == Class.class ) return false;

        if (( !type.isInterface() || (type == Object.class))) {

            if (!isPrimitive(type)) {

                //-- make sure type is serializable
                // if (!Serializable.class.isAssignableFrom( type ))
                // return false;

                //-- make sure we can construct the Object
                if (!type.isArray() ) {
                    //-- try to get the default constructor and make
                    //-- sure we are only looking at classes that can
                    //-- be instantiated by calling Class#newInstance
                    try {
                        type.getConstructor( EMPTY_CLASS_ARGS );
                    }
                    catch ( NoSuchMethodException e ) {
                        //-- Allow any built-in descriptor classes
                        //-- that don't have default constructors
                        //-- such as java.sql.Date, java.sql.Time, etc.
                        return (CoreDescriptors.getDescriptor(type) != null);
                    }
                }
            }
        }
        return true;
    } //-- marshallable

    /**
     * Sets the Naming conventions to be used by the Introspector
     *
     * @param naming the implementation of Naming to use. A
     * value of null, will reset the XMLNaming to the
     * default specified in the castor.properties file.
    **/
    public void setNaming(XMLNaming naming) {
        if (naming == null)
            _xmlNaming = _defaultNaming;
        else
            _xmlNaming = naming;
    } //-- setNaming

    /**
     * Sets the NodeType for primitives. If the
     * NodeType is NodeType.Element, all primitives will
     * be treated as Elements, otherwise all primitives
     * will be treated as Attributes.
     *
     * @param nodeType the NodeType to use for primitive values.
    **/
    public void setPrimitiveNodeType(NodeType nodeType) {
        if (nodeType == NodeType.Element)
            _primitiveNodeType = nodeType;
        else
            _primitiveNodeType = NodeType.Attribute;
    } //-- setPrimitiveNodeType

    /**
     * Sets whether or not keys from Hastable / Map instances
     * should be saved in the XML.
     *
     * <p>Note: This is true by default since Castor 0.9.5.3</p>
     *
     * @param saveMapKeys a boolean that when true indicates keys
     * from Hashtable or Map instances should be saved. Otherwise
     * only the value object is saved.
     */
    public void setSaveMapKeys(boolean saveMapKeys) {
        _saveMapKeys = saveMapKeys;
    } //-- setSaveMapKeys

    /**
     * Converts the given xml name to a Java name.
     * @param name the name to convert to a Java Name
     * @param upperFirst a flag to indicate whether or not the
     * the first character should be converted to uppercase.
     **/
    public static String toJavaName(String name, boolean upperFirst) {

        int size = name.length();
        char[] ncChars = name.toCharArray();
        int next = 0;

        boolean uppercase = upperFirst;

        for (int i = 0; i < size; i++) {
            char ch = ncChars[i];

            switch(ch) {
            case ':':
            case '-':
                uppercase = true;
                break;
            default:
                if (uppercase == true) {
                    ncChars[next] = Character.toUpperCase(ch);
                    uppercase = false;
                }
                else ncChars[next] = ch;
                ++next;
                break;
            }
        }
        return new String(ncChars,0,next);
    } //-- toJavaName


    //-------------------/
    //- Private Methods -/
    //-------------------/

    private XMLFieldDescriptorImpl createFieldDescriptor
        (Class type, String fieldName, String xmlName)
    {

        XMLFieldDescriptorImpl fieldDesc =
            new XMLFieldDescriptorImpl(type, fieldName, xmlName, null);

        if (type.isArray()) {
            fieldDesc.setNodeType(NodeType.Element);
        }
        //-- primitive types are converted to attributes by default
        else if (type.isPrimitive()) {
            fieldDesc.setNodeType(_primitiveNodeType);
        }
        else {
            fieldDesc.setNodeType(NodeType.Element);
        }

        //-- wildcard?
        if (type == java.lang.Object.class) {
            fieldDesc.setMatches(xmlName + " *");
        }

        return fieldDesc;
    } //-- createFieldDescriptor


    /**
     * Returns the registered FieldHandlerFactory for the
     * given Class type.
     *
     * @param type the Class type to return the registered
     * FieldHandlerFactory for
     */
    private FieldHandlerFactory getHandlerFactory(Class type) {
        if (_handlerFactoryMap != null) {
            Class tmp = type;
            while (tmp != null) {
                Object obj = _handlerFactoryMap.get(tmp);
                if (obj != null) {
                    return (FieldHandlerFactory)obj;
                }
                tmp = tmp.getSuperclass();
            }
        }

        //-- check DefaultFieldHandlerFactory
        if (DEFAULT_HANDLER_FACTORY.isSupportedType(type))
            return DEFAULT_HANDLER_FACTORY;

        return null;
    } //-- getHandlerFactory

    /**
     * Registers the supported class types for the given
     * FieldHandlerFactory into the map (for faster lookups)
     */
    private void registerHandlerFactory(FieldHandlerFactory factory) {
        if (_handlerFactoryMap == null)
            _handlerFactoryMap = new Hashtable();

        Class[] types = factory.getSupportedTypes();
        for (int i = 0; i < types.length; i++) {
            _handlerFactoryMap.put(types[i], factory);
        }
    } //-- registerHandlerFactory


    /**
     * Returns true if the given Class is an instance of a
     * collection class.
     */
    public static boolean isCollection(Class clazz) {

        if (clazz.isArray()) return true;

        for (int i = 0; i < _collections.length; i++) {
            //-- check to see if clazz is either the
            //-- same as or a subclass of one of the
            //-- available collections. For performance
            //-- reasons we first check if class is
            //-- directly equal to one of the collections
            //-- instead of just calling isAssignableFrom.
            if ((clazz == _collections[i]) ||
                (_collections[i].isAssignableFrom(clazz)))
            {
                return true;
            }
        }
        return false;
    } //-- isCollection


    /**
     * Returns true if the given Class is an instance of a
     * collection class.
     */
    public static boolean isMapCollection(Class clazz) {

        if (clazz.isArray()) return false;

        for (int i = 0; i < _collections.length; i++) {
            //-- check to see if clazz is either the
            //-- same as or a subclass of one of the
            //-- available collections. For performance
            //-- reasons we first check if class is
            //-- directly equal to one of the collections
            //-- instead of just calling isAssignableFrom.
            if ((clazz == _collections[i]) ||
                (_collections[i].isAssignableFrom(clazz)))
            {
                if (_collections[i] == java.util.Hashtable.class)
                    return true;
                //-- For JDK 1.1 compatibility use string name "java.util.Map"
                if (_collections[i].getName().equals(MAP))
                    return true;
            }
        }
        return false;
    } //-- isMapCollection


    /**
     * Returns true if we are allowed to create a descriptor
     * for a given class type
     * @param type the Class type to test
     * @return true if we are allowed to create a descriptor
     * for a given class type
    **/
    private static boolean isDescriptable(Class type) {
        //-- make sure type is not Void, or Class;
        if  (type == Void.class || type == Class.class ) return false;

        //-- check whether it is a Java 5.0 enum
        float javaVersion = Float.valueOf(System.getProperty("java.specification.version")).floatValue();
        if (javaVersion >= 1.5) {
            try {
                Boolean isEnum = ReflectionUtil.isEnumViaReflection(type);
                if (isEnum.booleanValue()) {
                    return true;
                }
            } catch (Exception e) {
                // nothing to report; implies that there's no such method
            }
        }
        
        if ( (!type.isInterface())  &&
             (type != Object.class) &&
             (!isPrimitive(type))) {

            //-- make sure type is serializable
            //if (!Serializable.class.isAssignableFrom( type ))
            // return false;

            //-- make sure we can construct the Object
            if (!type.isArray() ) {

                //-- try to get the default constructor and make
                //-- sure we are only looking at classes that can
                //-- be instantiated by calling Class#newInstance
                try {
                    type.getConstructor( EMPTY_CLASS_ARGS );
                }
                catch ( NoSuchMethodException e ) {

                    //-- Allow any built-in descriptor classes
                    //-- that don't have default constructors
                    //-- such as java.sql.Date, java.sql.Time, etc.
                    return (CoreDescriptors.getDescriptor(type) != null);
                }
            }
        }
        return true;
    } //-- isDescriptable

    /**
     * Returns true if the given class should be treated as a primitive
     * type
     * @return true if the given class should be treated as a primitive
     * type
    **/
    private static boolean isPrimitive(Class type) {

        if (type.isPrimitive()) {
            return true;
        }

        if ((type == Boolean.class) || (type == Character.class)) {
            return true;
        }

        Class superClass = type.getSuperclass();
        if (superClass == Number.class) {
            return true;
        }
        
        if (superClass != null) {
            return superClass.getName().equals("java.lang.Enum");
        } else {
            return false;
        }

    } //-- isPrimitive

    /**
     * Returns an array of collections available during
     * introspection. Allows JDK 1.2+ support without
     * breaking JDK 1.1 support.
     *
     * @return a list of available collections
    **/
    private static Class[] loadCollections() {


        Vector collections = new Vector(6);

        //-- JDK 1.1
        collections.addElement(Vector.class);
        collections.addElement(Enumeration.class);
        collections.addElement(Hashtable.class);

        //-- JDK 1.2+
        ClassLoader loader = Vector.class.getClassLoader();


        Class clazz = null;
        try {
            if (loader != null) {
                clazz = loader.loadClass(LIST);
            }
            else clazz = Class.forName(LIST);
        }
        catch(ClassNotFoundException cnfx) {
            //-- just ignore...either JDK 1.1
            //-- or some nasty ClassLoader
            //-- issue has occurred.
        }
        if (clazz != null) {
            //-- java.util.List found, add to collections,
            //-- also add java.util.Map
            collections.addElement(clazz);

            clazz = null;
            try {
                //-- java.util.Map
                if (loader != null) {
                    clazz = loader.loadClass(MAP);
                }
                else clazz = Class.forName(MAP);
                if (clazz != null) {
                    collections.addElement(clazz);
                }
                //-- java.util.Set
                if (loader != null) {
                    clazz = loader.loadClass(SET_COLLECTION);
                }
                else clazz = Class.forName(SET_COLLECTION);
                if (clazz != null) {
                    collections.addElement(clazz);
                }


            }
            catch(ClassNotFoundException cnfx) {
                //-- just ignore...for now
                //-- some nasty ClassLoader issue has occurred.
            }
        }


        Class[] classes = new Class[collections.size()];
        collections.copyInto(classes);

        return classes;
    } //-- loadCollections


    /**
     * A special TypeConvertor that simply returns the object
     * given. This is used for preventing the FieldHandlerImpl
     * from using a CollectionHandler when getValue is called.
     */
    class IdentityConvertor implements TypeConvertor {
        public Object convert(final Object object) {
            return object;
        }
    } //-- class: IdentityConvertor

    /**
     * A simple struct for holding a set of accessor methods
    **/
    class MethodSet {

        /**
         * A reference to the add method.
        **/
        Method add    = null;

        /**
         * A reference to the create method.
        **/
        Method create = null;

        /**
         * A reference to the get method.
        **/
        Method get    = null;

        /**
         * A reference to the set method.
        **/
        Method set    = null;

        /**
         * The fieldName for the field accessed by the methods in
         * this method set.
        **/
        String fieldName = null;

        MethodSet(String fieldName) {
            super();
            this.fieldName = fieldName;
        }
    } //-- inner class: MethodSet




} //-- Introspector

/**
 * A simple extension of XMLClassDescriptor
 * so that we can set the "instrospected" flag.
**/
class IntrospectedXMLClassDescriptor
    extends XMLClassDescriptorImpl
{
    /**
     * Creates an IntrospectedXMLClassDescriptor
     * @param type the Class type with which this
     * ClassDescriptor describes.
    **/
    IntrospectedXMLClassDescriptor(Class type) {
        super(type);
        setIntrospected(true);
    } //-- XMLClassDescriptorImpl

    /**
     * Creates an IntrospectedXMLClassDescriptor
     * @param type the Class type with which this
     * ClassDescriptor describes.
    **/
    public IntrospectedXMLClassDescriptor(Class type, String xmlName)
    {
        super(type, xmlName);
        setIntrospected(true);
    } //-- XMLClassDescriptorImpl

} //-- IntrospectedClassDescriptor

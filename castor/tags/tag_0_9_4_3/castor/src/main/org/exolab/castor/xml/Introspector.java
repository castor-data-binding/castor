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

import org.exolab.castor.xml.descriptors.CoreDescriptors;
import org.exolab.castor.xml.handlers.ContainerFieldHandler;
import org.exolab.castor.xml.handlers.DateFieldHandler;
import org.exolab.castor.xml.util.ContainerElement;
import org.exolab.castor.xml.util.DefaultNaming;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.List;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A Helper class for the Marshaller and Unmarshaller,
 * basically the common code base between the two. This
 * class handles the introspection to dynamically create
 * descriptors.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public final class Introspector {
    
          
    /**
     * The property name for enabling collection wrapping.
     * The property controls whether or not collections 
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
     * Use this property with a value of true or false in the 
     * castor.properties file
     *
     * org.exolab.castor.xml.introspector.wrapCollections=true
     * -or-
     * org.exolab.castor.xml.introspector.wrapCollections=false
     *
     * This property is false by default.
     */
     public static final String WRAP_COLLECTIONS_PROPERTY =
        "org.exolab.castor.xml.introspector.wrapCollections";
     
    private static final String ADD     = "add";
    private static final String GET     = "get";
    private static final String IS      = "is";
    private static final String SET     = "set";
    private static final String CREATE  = "create";
    
    
    private static final Class[] EMPTY_CLASS_ARGS = new Class[0];

    /**
     * Name of the java.util.List collection
     */
    private static final String LIST = "java.util.List";

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
    private XMLNaming _naming = null;   
    
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
    
    public Introspector() {
        super();
        init();
    } //-- Introspector

    private void init() {
        
        LocalConfiguration config = LocalConfiguration.getInstance();
        
        if (_defaultNaming == null) {
            _defaultNaming = config.getXMLNaming();
        }
        _naming = _defaultNaming;
        setPrimitiveNodeType(config.getPrimitiveNodeType());
        
        //-- wrap collections in a container element?
        String wrap = config.getProperty(WRAP_COLLECTIONS_PROPERTY, null);
        if (wrap != null) {
            _wrapCollectionsInContainer = Boolean.valueOf(wrap).booleanValue();
        }
        
    }
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
        List      dateDescriptors = new List(3);
        Hashtable methodSets      = new Hashtable();
        
        int methodCount = 0;
        
        //-- create method sets
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            
            //-- if method comes from the Object base class, ignore
            if (method.getDeclaringClass() == Object.class) continue;
            
            //-- if method is static...ignore
            if ((method.getModifiers() & Modifier.STATIC) != 0) continue;
            
            String methodName = method.getName();
            
            //-- read methods
            if (methodName.startsWith(GET)) {
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
                fieldName = JavaNaming.toJavaMemberName(fieldName);
                
                MethodSet methodSet = (MethodSet)methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.get = method;
            }
            else if (methodName.startsWith(IS)) {
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
                String fieldName = methodName.substring(IS.length());
                fieldName = JavaNaming.toJavaMemberName(fieldName);
                
                MethodSet methodSet = (MethodSet)methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.get = method;
            }
            //-----------------------------------/
            //-- write methods (collection item)
            else if (methodName.startsWith(ADD)) {
                if (method.getParameterTypes().length != 1) continue;
                //-- disable direct field access
                ++methodCount;
                //-- make sure parameter type is "descriptable" 
                if (!isDescriptable(method.getParameterTypes()[0])) continue;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                fieldName = JavaNaming.toJavaMemberName(fieldName);
                MethodSet methodSet = (MethodSet) methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.add = method;
            }
            //-- write method (singleton or collection)
            else if (methodName.startsWith(SET)) {
                if (method.getParameterTypes().length != 1) continue;
                //-- disable direct field access
                ++methodCount;
                //-- make sure parameter type is "descriptable" 
                if (!isDescriptable(method.getParameterTypes()[0])) continue;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                fieldName = JavaNaming.toJavaMemberName(fieldName);
                MethodSet methodSet = (MethodSet) methodSets.get(fieldName);
                if (methodSet == null) {
                    methodSet = new MethodSet(fieldName);
                    methodSets.put(fieldName, methodSet);
                }
                methodSet.set = method;
            }
            else if (methodName.startsWith(CREATE)) {
                if (method.getParameterTypes().length != 0) continue;
                Class type = method.getReturnType();
                //-- make sure return type is "descriptable" 
                //-- and not null
                if (!isDescriptable(type)) continue;
                //-- caclulate name from Method name
                String fieldName = methodName.substring(CREATE.length());
                fieldName = JavaNaming.toJavaMemberName(fieldName);
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
        Enumeration enum = methodSets.elements();
        
        while (enum.hasMoreElements()) {
            
            MethodSet methodSet = (MethodSet) enum.nextElement();
            
            //-- create XMLFieldDescriptor
            String xmlName = _naming.toXMLName(methodSet.fieldName);
                
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
                typeInfo = new TypeInfo(type);
                colHandler = typeInfo.getCollectionHandler();
                
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
            
            XMLFieldDescriptorImpl fieldDesc 
                = createFieldDescriptor(type, methodSet.fieldName, xmlName);
                
            
            typeInfo = new TypeInfo(type, null, null, false, null, colHandler);
            
            if (isCollection) {
                fieldDesc.setMultivalued(true);
                fieldDesc.setNodeType(NodeType.Element);
            }
            
            FieldHandlerImpl handler = null;
            try {
                handler = new FieldHandlerImpl(methodSet.fieldName,
                                                null,
                                                null,
                                                methodSet.get,
                                                methodSet.set, 
                                                typeInfo);
                //-- clean up
                if (methodSet.add != null) 
                    handler.setAddMethod(methodSet.add);
                                                
                if (methodSet.create != null) 
                    handler.setCreateMethod(methodSet.create);
                    
            }
            catch (MappingException mx) {
                throw new MarshalException(mx);
            }
                    
            //-- check for instances of java.util.Date
            if (java.util.Date.class.isAssignableFrom(type)) {
                //handler = new DateFieldHandler(handler);
                dateDescriptors.add(fieldDesc);
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
                    typeInfo = new TypeInfo(type);
                    colHandler = typeInfo.getCollectionHandler();
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
                String xmlName = _naming.toXMLName(fieldName);
                
                XMLFieldDescriptorImpl fieldDesc = 
                        createFieldDescriptor(type, fieldName, xmlName);
                        
                if (isCollection) {
                    fieldDesc.setNodeType(NodeType.Element);
                    fieldDesc.setMultivalued(true);
                }
                descriptors.put(xmlName, fieldDesc);
                classDesc.addFieldDescriptor(fieldDesc);
                
                typeInfo = new TypeInfo(type, null, null, false, null, colHandler);
                
                FieldHandler handler = null;

                try {
                    handler = new FieldHandlerImpl(field, typeInfo);
                }
                catch (MappingException mx) {
                    throw new MarshalException(mx);
                }
                
                fieldDesc.setHandler(handler);
                   
                //-- check for instances of java.util.Date
                if (java.util.Date.class.isAssignableFrom(type))
                    dateDescriptors.add(fieldDesc);
                
            }            
        } //-- end of direct field access
        
        
        //-- A temporary fix for java.util.Date
        if (dateDescriptors != null) {
            for (int i = 0; i < dateDescriptors.size(); i++) {
                XMLFieldDescriptorImpl fieldDesc =
                    (XMLFieldDescriptorImpl) dateDescriptors.get(i);
                FieldHandler handler = fieldDesc.getHandler();
                fieldDesc.setImmutable(true);
                fieldDesc.setHandler(new DateFieldHandler(handler));
            }
        }
        
        
        return classDesc;
    } //-- generateClassDescriptor
    
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
                        return false;
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
            _naming = _defaultNaming;
        else 
            _naming = naming;
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
     * Returns true if the given Class is an instance of a
     * collection class.
     * 
     * @see loadCollections
    **/
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
     * Returns true if we are allowed to create a descriptor
     * for a given class type
     * @param type the Class type to test
     * @return true if we are allowed to create a descriptor
     * for a given class type
    **/
    private static boolean isDescriptable(Class type) {
        //-- make sure type is not Void, or Class;
        if  (type == Void.class || type == Class.class ) return false;
        
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
                    return false;
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

        if (type.isPrimitive()) return true;
        
        if ((type == Boolean.class) || (type == Character.class))
            return true;
            
        return (type.getSuperclass() == Number.class);
       
    } //-- isPrimitive
    
    /**
     * Returns an array of collections available during
     * introspection. Allows JDK 1.2+ support without
     * breaking JDK 1.1 support.
     *
     * @return a list of available collections
    **/
    private static Class[] loadCollections() {
        Vector collections = new Vector();
        
        //-- JDK 1.1
        collections.addElement(Vector.class);
        collections.addElement(Enumeration.class);
        
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
        if (clazz != null) collections.addElement(clazz);
        
        Class[] classes = new Class[collections.size()];
        collections.copyInto(classes);
        
        return classes;
    } //-- loadCollections
    
    
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

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
 * Copyright 1999, 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import org.exolab.castor.xml.descriptors.*;
import org.exolab.castor.xml.handlers.DateFieldHandler;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.List;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Hashtable;

/**
 * A Helper class for the Marshaller and Unmarshaller,
 * basically the common code base between the two. This
 * class handles the introspection to dynamically create
 * descriptors.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class MarshalHelper {
    
    /**
     * The XSI Namespace URI
    **/
    public static final String XSI_NAMESPACE 
        = "http://www.w3.org/1999/XMLSchema-instance";
          
    private static final String ADD     = "add";
    private static final String GET     = "get";
    private static final String SET     = "set";
    private static final String CREATE  = "create";
    
    
    private static final Class[] EMPTY_CLASS_ARGS = new Class[0];
    
    private static final StringClassDescriptor _StringClassDescriptor 
        = new StringClassDescriptor();

    private static final VectorClassDescriptor _VectorClassDescriptor 
        = new VectorClassDescriptor();
    
    /**
     * Creates an XMLClassDescriptor for the given class by using Reflection.
     * @param c the Class to create the XMLClassDescriptor for
     * @return the new XMLClassDescriptor created for the given class
     * @exception MarshalException when an error occurs during the creation
     * of the ClassDescriptor.
     **/
    public static XMLClassDescriptor generateClassDescriptor(Class c) 
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
    public static XMLClassDescriptor generateClassDescriptor
        (Class c, PrintWriter errorWriter) throws MarshalException
    {
        
        if (c == null) return null;
        
        //-- handle arrays
        if (c.isArray()) return null;
        
        //-- handle Strings
        if (c == String.class)
            return _StringClassDescriptor;
        
        //-- handle Vectors...we need to make this
        //-- plug&play for JDK 1.2
        if (c == java.util.Vector.class) {
            return new VectorClassDescriptor();
        }
        
        //-- handle base objects
        if ((c == Void.class) || 
            (c == Class.class)||
            (c == Object.class)) {
            throw new MarshalException (
                MarshalException.BASE_CLASS_OR_VOID_ERR );
        }
        
        XMLClassDescriptorImpl classDesc 
            = new IntrospectedXMLClassDescriptor(c);
        
        //--------------------------/
        //- handle complex objects -/
        //--------------------------/
        
        Method[] methods = c.getMethods();
        Hashtable descriptors     = null;
        Hashtable createMethods   = null;
        List      dateDescriptors = null;
        
        //-- make sure we have methods before creating
        //-- the hashtables and lists
        if (methods.length > 0) {
            descriptors     = new Hashtable();
            createMethods   = new Hashtable();
            dateDescriptors = new List(3);
        }
        
        int methodCount = 0;
        
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
                                
                ++methodCount;
                
                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                String xmlName   = toXMLName(fieldName);
                
                Class type = method.getReturnType();
                
                if (!isDescriptable(type)) continue;
                
                XMLFieldDescriptorImpl fieldDesc 
                    = (XMLFieldDescriptorImpl) descriptors.get(xmlName);
                
                if (fieldDesc == null) {
                    fieldDesc = createFieldDescriptor(type, fieldName, xmlName);
                    descriptors.put(xmlName, fieldDesc);
                    classDesc.addFieldDescriptor(fieldDesc);
                }
                
                FieldHandlerImpl handler 
                    = (FieldHandlerImpl)fieldDesc.getHandler();
                
                if (handler == null) {
                    TypeInfo typeInfo = new TypeInfo(type);
                    try {
                        handler = new FieldHandlerImpl(fieldName,
                                                       null,
                                                       null,
                                                       method,
                                                       null, 
                                                       typeInfo);
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
                }
                else {
                    try {
                        handler.setReadMethod(method);
                        //-- look for createMethod
                        method = (Method)createMethods.remove(xmlName);
                        if (method != null) handler.setCreateMethod(method);
                    }
                    catch(MappingException mx) {
                        throw new MarshalException(mx);
                    }
                }
                
            } //-- end read method
            //-- write methods
            else if (methodName.startsWith(ADD) ||
                     methodName.startsWith(SET) ) {
                
                if (method.getParameterTypes().length != 1) continue;
                
                ++methodCount;
                
                //-- caclulate name from Method name
                String fieldName = methodName.substring(3);
                String xmlName   = toXMLName(fieldName);
                
                Class type = method.getParameterTypes()[0];
                if (!isDescriptable(type)) continue;
                
                XMLFieldDescriptorImpl fieldDesc 
                    = (XMLFieldDescriptorImpl) descriptors.get(xmlName);
                
                if (fieldDesc == null) {
                    fieldDesc = createFieldDescriptor(type, fieldName, xmlName);
                    descriptors.put(xmlName, fieldDesc);
                    classDesc.addFieldDescriptor(fieldDesc);
                }
                
                //-- collection
                boolean isAdd = false;
                if (methodName.startsWith(ADD)) {
                    fieldDesc.setNodeType(NodeType.Element);
                    fieldDesc.setMultivalued(true);
                    isAdd = true;
                }
                else { 
                    if (type.isArray() || 
                       (type == java.util.Vector.class)) 
                    {
                        fieldDesc.setNodeType(NodeType.Element);
                        fieldDesc.setMultivalued(true);
                    }
                }
                
                FieldHandlerImpl handler 
                    = (FieldHandlerImpl)fieldDesc.getHandler();
                
                if (handler == null) {
                    TypeInfo typeInfo = new TypeInfo(type);
                    try {
                        handler = new FieldHandlerImpl(fieldName,
                                                       null,
                                                       null,
                                                       null,
                                                       method, 
                                                       typeInfo);
                        //-- clean up
                        if (isAdd) handler.setAddMethod(method);
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
                }
                else {
                    try {
                        if (isAdd)
                            handler.setAddMethod(method);
                        else
                            handler.setWriteMethod(method);
                        
                        //-- look for createMethod
                        method = (Method)createMethods.remove(xmlName);
                        if (method != null) handler.setCreateMethod(method);
                    }
                    catch(MappingException mx) {
                        throw new MarshalException(mx);
                    }
                }
            } //-- end write methods
            //-- create methods
            else if (methodName.startsWith(CREATE)) {
                if (method.getParameterTypes().length != 0) continue;
                
                //-- caclulate name from Method name
                String fieldName = methodName.substring(CREATE.length());
                String xmlName   = toXMLName(fieldName);
                
                Class type = method.getReturnType();
                                
                if (!isDescriptable(type)) continue;
                
                XMLFieldDescriptorImpl fieldDesc 
                    = (XMLFieldDescriptorImpl) descriptors.get(xmlName);
                
                if (fieldDesc == null) {
                    //-- add create method to hash and loop...
                    createMethods.put(xmlName, method);
                    continue;
                }
                
                FieldHandlerImpl handler 
                    = (FieldHandlerImpl)fieldDesc.getHandler();
                try {
                    handler.setCreateMethod(method);
                }
                catch(MappingException mx) {
                    throw new MarshalException(mx);
                }
            } //-- end create method
            
        } //-- end of method loop
        
        //-- If we didn't find any methods we can try
        //-- direct field access
        if (methodCount == 0) {           
            Field[] fields = c.getFields();            
            for (int i = 0; i < fields.length; i++) {                
                Field field = fields[i];
                
                Class type = field.getType();       
                boolean isCollection = false;
                //-- contentType of collection
                Class contentType = type;
                if (type.isArray()) {
                    type = type.getComponentType();
                    isCollection = true;
                    //contentType = type;
                }                
                if (!isDescriptable(type)) continue;
                
                //-- Built-in support for JDK 1.1 Collections
                //-- we need to a pluggable interface for 
                //-- JDK 1.2+
                if (type == java.util.Vector.class) {
                    isCollection = true;
                    //contentType = java.lang.Object.class;
                }
                
                String fieldName = field.getName();
                String xmlName = toXMLName(fieldName);
                
                XMLFieldDescriptorImpl fieldDesc = 
                        createFieldDescriptor(type, fieldName, xmlName);
                        
                if (isCollection) {
                    fieldDesc.setNodeType(NodeType.Element);
                    fieldDesc.setMultivalued(true);
                }
                descriptors.put(xmlName, fieldDesc);
                classDesc.addFieldDescriptor(fieldDesc);
                
                TypeInfo typeInfo = new TypeInfo(type);
                
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
                fieldDesc.setHandler(new DateFieldHandler(handler));
            }
        }
        
        
        return classDesc;
    } //-- generateClassDescriptor
    
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
    
    /**
     * Converts a String to the given primitive object type
     * @param type the class type of the primitive in which
     * to convert the String to
     * @param value the String to convert to a primitive
     * @return the new primitive Object
     **/
    public static Object toPrimitiveObject(Class type, String value) {
        
        Object primitive = null;
        
        //-- I tried to order these in the order in which
        //-- (I think) types are used more frequently
        
        boolean isNull = ((value == null) || (value.length() == 0));
        
        // int
        if ((type == Integer.TYPE) || (type == Integer.class)) {
            if (isNull)
                primitive = new Integer(0);
            else
                primitive = new Integer(value);
        }
        // boolean
        else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
            if (isNull)
                primitive = new Boolean(false);
            else				
				primitive = (value.equals("1") || 
							 value.toLowerCase().equals("true")) 
								? Boolean.TRUE : Boolean.FALSE;
        }
        // double
        else if ((type == Double.TYPE) || (type == Double.class)) {
            if (isNull)
                primitive = new Double(0.0);
            else
                primitive = new Double(value);
        }
        // long
        else if ((type == Long.TYPE) || (type == Long.class)) {
            if (isNull)
                primitive = new Long(0);
            else
                primitive = new Long(value);
        }
        // char
        else if (type == Character.TYPE) {
            if (!isNull)
                primitive = new Character(value.charAt(0));
            else 
                primitive = new Character('\0');
        }
        // short
        else if ((type == Short.TYPE) || (type == Short.class)) {
            if (isNull)
                primitive = new Short((short)0);
            else 
                primitive = new Short(value);
        }
        // float
        else if ((type == Float.TYPE) || (type == Float.class)) {
            if (isNull)
                primitive = new Float((float)0);
            else
                primitive = new Float(value);
        }
        // byte
        else if (type == Byte.TYPE) {
            if (isNull)
                primitive = new Byte((byte)0);
            else
                primitive = new Byte(value);
        }
        // otherwise do nothing
        else 
            primitive = value;
        
        return primitive;
    } //-- toPrimitiveObject
    
    /**
     * Creates the XML Name for the given class
     * 
     * @param c the Class to create the XML Name for
    **/
    public static String createXMLName(Class c) {
        //-- create default XML name
        String name = c.getName();
        int idx = name.lastIndexOf('.');
        if (idx >= 0) name = name.substring(idx+1);
        return toXMLName(name);
    } //-- createXMLName
    
    /**
     * Converts the given name to an XML name. It would be nearly
     * impossible for this method to please every one, so I picked
     * common "de-facto" XML naming conventions. This can be overridden
     * by implementing the MarshalInfo for your classes.
     * @param name the String to convert to an XML name
     * @return the xml name representation of the given String
     * <BR /><B>examples:</B><BR />
     * "Blob" becomes "blob" and "DataSource" becomes "data-source".
     * An XML
     **/
    public static String toXMLName(String name) {
        
        if (name == null) return null;
        if (name.length() == 0) return name;
        if (name.length() == 1) return name.toLowerCase();
        
        //-- Follow the Java beans Introspector::decapitalize
        //-- convention by leaving alone String that start with
        //-- 2 uppercase characters.
        if (Character.isUpperCase(name.charAt(0)) &&
            Character.isUpperCase(name.charAt(1))) return name;
        
        //-- process each character
        StringBuffer cbuff = new StringBuffer(name);
        cbuff.setCharAt(0, Character.toLowerCase(cbuff.charAt(0)));
        
        boolean ucPrev = false;
        for (int i = 1; i < cbuff.length(); i++) {
            char ch = cbuff.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (ucPrev) continue;
                ucPrev = true;
                cbuff.insert(i,'-');
                ++i;
                cbuff.setCharAt(i, Character.toLowerCase(ch));
            }
            //-- do not add '-' if preceeded by '.'
            else if (ch == '.') 
                ucPrev = true;
            else 
                ucPrev = false;
        }
        return cbuff.toString();
    }
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    private static XMLFieldDescriptorImpl createFieldDescriptor
        (Class type, String fieldName, String xmlName) 
    {

        XMLFieldDescriptorImpl fieldDesc =
            new XMLFieldDescriptorImpl(type, fieldName, xmlName, null);
            
        if (type.isArray()) {
            fieldDesc.setNodeType(NodeType.Element);
        }
        //-- primitive types are converted to attributes by default
        else if (type.isPrimitive()) {
            fieldDesc.setNodeType(NodeType.Attribute);
        }
        else {
            fieldDesc.setNodeType(NodeType.Element);
        }
        
        return fieldDesc;
    } //-- createFieldDescriptor

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
    
} //-- MarshalHelper

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

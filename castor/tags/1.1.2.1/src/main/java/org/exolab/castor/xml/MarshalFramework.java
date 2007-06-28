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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;


/**
 * A core class for common code shared throughout the
 * Marshalling Framework
 * 
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
abstract class MarshalFramework {

    
    
    //--------------------------/
    //- Public class variables -/
    //--------------------------/

    /**
     * The XSI Namespace URI
    **/
    public static final String XSI_NAMESPACE
        = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * The name of the Schema location attribute
    **/
    public static final String XSI_SCHEMA_LOCATION = "schemaLocation";

    /**
     * The name of the no namespace schema location attribute
    **/
    public static final String XSI_NO_NAMESPACE_SCHEMA_LOCATION 
        = "noNamespaceSchemaLocation";
   
    /**
     * The xml:lang attribute name
     */
    public static final String XML_LANG_ATTR = "xml:lang";
    
    /**
     * The xml:lang attribute, without the "xml:" prefix.
     */
    public static final String LANG_ATTR = "lang";
    
    /**
     * The xsi:nil attribute, without the "xsi:" prefix.
     */
    public static final String NIL_ATTR = "nil";
    
    public static final String XSI_NIL_ATTR = "xsi:nil";
    
    /**
     * The xml:space attribute name
     */    
    public static final String XML_SPACE_ATTR = "xml:space";
    
    /**
     * The xml:space attribute name, without the "xml:" prefix
     */    
    public static final String SPACE_ATTR = "space";
    
    /**
     * The xsi:type attribute name, without the "xsi:" prefix
     */    
    public static final String TYPE_ATTR = "type";
    
    /**
     * The value of 'true'
     */
    public static final String TRUE_VALUE = "true";
    

    //-----------------------------/
    //- Protected class variables -/
    //-----------------------------/

    static final String INTERNAL_XML_NAME = "-error-if-this-is-used-";

    /**
     * The default prefix used for specifying the
     * xsi:type as a classname instead of a schema name.
     * This is a Castor specific hack.
     */
    static final String JAVA_PREFIX = "java:";

    /**
     * The name of the QName type
     */
    static final String QNAME_NAME = "QName";
    
    /**
     * An empty array of field descriptors
     */
    static final XMLFieldDescriptor[] NO_FIELD_DESCRIPTORS 
        = new XMLFieldDescriptor[0];

    /**
     * Returns true if the given Class is a considered a
     * collection by the marshalling framework.
     *
     * @return true if the given Class is considered a collection.
     */
    public static boolean isCollection(Class clazz) {
        return CollectionHandlers.hasHandler(clazz);
    } //-- isCollection
    
    /**
     * Returns the CollectionHandler associated with the
     * given collection, or null if no such handler exists.
     *
     * @return the CollectionHandler for the associated type.
     */
    public CollectionHandler getCollectionHandler(Class clazz) {
        CollectionHandler handler = null;
        try {
            handler = CollectionHandlers.getHandler(clazz);
        }
        catch(MappingException mx) {
            //-- Not a collection, or no handler exists, return null.
        }
        return handler;
    } //-- getCollectionHandler
    
    
    /**
     * Returns true if the given class should be treated as a primitive
     * type. This method will return true for all Java primitive
     * types, the set of primitive object wrappers, as well
     * as Strings.
     *
     * @return true if the given class should be treated as a primitive
     * type
    **/
    static boolean isPrimitive(Class type) {

        if (type == null) return false;

        //-- java primitive
        if (type.isPrimitive()) return true;

        //-- we treat strings as primitives
        if (type == String.class) return true;

        //-- primtive wrapper classes
        if ((type == Boolean.class) || (type == Character.class)) {
            return true;
        }

        Class superClass = type.getSuperclass();
        if (superClass == Number.class) {
            return true;
        }

        if (superClass != null) {
            return superClass.getName().equals("java.lang.Enum");
        }
        
        return false;
        
    } //-- isPrimitive

    /**
     * Returns true if the given class should be treated as an enum type. This method 
     * will return true for all Java 5 (or later) enums, and for enum-style
     * classes.
     *
     * @return true if the given class should be treated as an enum
     **/
    static boolean isEnum(Class type) {

        if (type == null) {
            return false;
        }

        float javaVersion = Float.valueOf(System.getProperty("java.specification.version")).floatValue();
        if (javaVersion >= 1.5) {
            try {
                Boolean isEnum = ReflectionUtil.isEnumViaReflection(type);
                return isEnum.booleanValue();
            } catch (Exception e) {
                // nothing to report; implies that there's no such method
            }
        }
        
        // TODO: add code to cover 1.4 enum-stype classes as well.
        
        return false;

    } //-- isPrimitive

//    /**
//     * Calls isEnum() method on target class vi areflection to find out
//     * whether the given type is a Java 5 enumeration.
//     * @param type The type to analyze.
//     * @return True if the type given is a Java 5.0 enum.
//     * @throws NoSuchMethodException If the method can not be found.
//     * @throws IllegalAccessException If access to this method is illegal
//     * @throws InvocationTargetException If the target method can not be invoked.
//     */
//    private static Boolean isEnumViaReflection(Class type) 
//    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        Method isEnumMethod = type.getClass().getMethod("isEnum", (Class[]) null);
//        return (Boolean) isEnumMethod.invoke(type, (Object[]) null);
//    }
//    
    /**
     * Returns true if any of the fields associated with the given
     * XMLClassDescriptor are located at, or beneath, the given location.
     *
     * @param location the location to compare against
     * @param classDesc the XMLClassDescriptor in which to check the field 
     * locations
     */
    static final boolean hasFieldsAtLocation
        (String location, XMLClassDescriptor classDesc)
    {
        //-- check elements
        XMLFieldDescriptor[] descriptors = classDesc.getElementDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i] == null) continue;
            String tmpLocation = descriptors[i].getLocationPath();
            if ((tmpLocation != null) && (tmpLocation.startsWith(location)))
                return true;
        }
        
        //-- check attributes
        descriptors = classDesc.getAttributeDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i] == null) continue;
            String tmpLocation = descriptors[i].getLocationPath();
            if ((tmpLocation != null) && (tmpLocation.startsWith(location)))
                return true;
        }
        
        //-- check content/text
        XMLFieldDescriptor content = classDesc.getContentDescriptor();
        if (content != null) {
            String tmpLocation = content.getLocationPath();
            if ((tmpLocation != null) && (tmpLocation.startsWith(location)))
                return true;
        }
        return false;
    } //-- hasFieldsAtLocation
    
    /**
     * Compares the given namespaces (as strings) for equality.
     * null and empty values are considered equal.
     *
     * @param ns1 the namespace to compare to argument ns2
     * @param ns2 the namespace to compare to argument ns1
     */
    public static boolean namespaceEquals(String ns1, String ns2) {
        if (ns1 == null) {
            return ((ns2 == null) || (ns2.length() == 0));
        }
        if (ns2 == null) {
            return (ns1.length() == 0);
        }
        return ns1.equals(ns2);
    } //-- namespaceEquals
    
    /**
     * Returns true if the given classes are both the same
     * primitive or primitive wrapper class. For exmaple, if 
     * class "a" is an int (Integer.TYPE) and class "b" is 
     * either an int or Integer.class then true will be
     * returned, otherwise false.
     *
     * @return true if both a and b are considered equal
     */
    static boolean primitiveOrWrapperEquals(Class a, Class b) {
        if (!isPrimitive(a)) return false;
        if (!isPrimitive(b)) return false;
        
        if (a == b) return true;
        
        //-- Boolean/boolean
        if ((a == Boolean.class) || (a == Boolean.TYPE)) {
            return ((b == Boolean.class) || (b == Boolean.TYPE));
        }
        //-- Byte/byte
        else if ((a == Byte.class) || (a == Byte.TYPE)) {
            return ((b == Byte.class) || (b == Byte.TYPE));
        }
        //-- Character/char
        else if ((a == Character.class) || (a == Character.TYPE)) {
            return ((b == Character.class) || (b == Character.TYPE));
        }
        //-- Double/double
        else if ((a == Double.class) || (a == Double.TYPE)) {
            return ((b == Double.class) || (b == Double.TYPE));
        }
        else if ((a == Float.class) || (a == Float.TYPE)) {
            return ((b == Float.class) || (b == Float.TYPE));
        }
        //-- Integer/int
        else if ((a == Integer.class) || (a == Integer.TYPE)) {
            return ((b == Integer.class) || (b == Integer.TYPE));
        }
        //-- Long/long
        else if ((a == Long.class) || (a == Long.TYPE)) {
            return ((b == Long.class) || (b == Long.TYPE));
        }
        //-- Short/short
        else if ((a == Short.class) || (a == Short.TYPE)) {
            return ((b == Short.class) || (b == Short.TYPE));
        }
        
        return false;
    } //-- primitiveOrWrapperEquals
    
    /**
     * 
     */
    private static final InheritanceMatch[] NO_MATCH_ARRAY = new InheritanceMatch[0];
    
    /**
     * Search there is a field descriptor which can accept one of the class
     * descriptor which match the given name and namespace.
     * 
     * @return An array of InheritanceMatch.
     */
    public static InheritanceMatch[] searchInheritance(String name, String namespace,
            XMLClassDescriptor classDesc, XMLClassDescriptorResolver cdResolver)
    throws MarshalException {
        Iterator classDescriptorIterator = null;
        
        try {
            //-- A little required logic for finding Not-Yet-Loaded
            //-- descriptors
            String className =JavaNaming.toJavaClassName(name);
            //-- should use namespace-to-prefix mappings, but
            //-- just create package for now.
            Class clazz = classDesc.getJavaClass();
            String pkg = null;
            if (clazz != null) {
                while (clazz.getDeclaringClass() != null)
                    clazz = clazz.getDeclaringClass();
                pkg = clazz.getName();
                int idx = pkg.lastIndexOf('.');
                if (idx >= 0) {
                    pkg = pkg.substring(0, idx+1);
                    className = pkg + className;
                }
            }
            cdResolver.resolve(className, classDesc.getClass().getClassLoader());
            //-- end Not-Yet-Loaded descriptor logic
            
            //-- resolve all by XML name + namespace URI
            classDescriptorIterator = cdResolver.resolveAllByXMLName(name, namespace, null);
        }
        catch(ResolverException rx) {
            Throwable actual = rx.getCause();
            if (actual instanceof MarshalException) {
                throw (MarshalException)actual;
            }
            if (actual != null) {
                throw new MarshalException(actual);
            }
            throw new MarshalException(rx);
        }

        Vector inheritanceList = null;
        XMLFieldDescriptor descriptor  = null;
        XMLFieldDescriptor[] descriptors = classDesc.getElementDescriptors();
        XMLClassDescriptor cdInherited = null;

        if (classDescriptorIterator.hasNext()) {
            while (classDescriptorIterator.hasNext() && (descriptor == null)) {
                cdInherited = (XMLClassDescriptor) classDescriptorIterator.next();
                Class subclass = cdInherited.getJavaClass();

                for (int i = 0; i < descriptors.length; i++) {

                    if (descriptors[i] == null) continue;
                    
                    //-- skip descriptors with special internal name
                    if (INTERNAL_XML_NAME.equals(descriptors[i].getXMLName())) 
                        continue;
                    
                    //-- check for inheritence
                    Class superclass = descriptors[i].getFieldType();

                    // It is possible that the superclass is of type object if we use any node.
                    if (superclass.isAssignableFrom(subclass) && (superclass != Object.class)) {
                        descriptor = descriptors[i];
                        if (inheritanceList == null)
                            inheritanceList = new Vector(3);
                        inheritanceList.addElement(new InheritanceMatch(descriptor, cdInherited));
                    }
                }
            }
            //-- reset inherited class descriptor, if necessary
            if (descriptor == null) cdInherited = null;
        }
        
        if (inheritanceList != null) {
            InheritanceMatch[] result = new InheritanceMatch[inheritanceList.size()];
            inheritanceList.toArray(result);
            return result;
        }
        return NO_MATCH_ARRAY;
        
    }

     /**
     * Used to store the information when we find a possible inheritance. It
     * store the XMLClassDescriptor of the object to instantiate and the
     * XMLFieldDescriptor of the parent, where the instance of the
     * XMLClassDescriptor will be put.
     */
    public static class InheritanceMatch {

        public XMLFieldDescriptor parentFieldDesc;
        public XMLClassDescriptor inheritedClassDesc;

        public InheritanceMatch(XMLFieldDescriptor fieldDesc, XMLClassDescriptor classDesc) {
            parentFieldDesc    = fieldDesc;
            inheritedClassDesc = classDesc;
        }
    }



    /**
     * An internal implementation of XMLClassDescriptor used by
     * the Unmarshaller and Marshaller...
     */
    class InternalXMLClassDescriptor implements XMLClassDescriptor {

        private XMLClassDescriptor _classDesc = null;
        
        /**
         * Cached arrays
         */
        private XMLFieldDescriptor[] _attributes = null;
        private XMLFieldDescriptor[] _elements   = null;
        private FieldDescriptor[]    _fields     = null;
        
        /**
         * Creates a new InternalXMLClassDescriptor for the given
         * XMLClassDescriptor
         */
        protected InternalXMLClassDescriptor(XMLClassDescriptor classDesc)
        {
            if (classDesc == null) {
                String err = "The argument 'classDesc' must not be null.";
                throw new IllegalArgumentException(err);
            }
            
            //-- prevent wrapping another InternalXMLClassDescriptor,
            while (classDesc instanceof InternalXMLClassDescriptor) {
                classDesc = ((InternalXMLClassDescriptor)classDesc).getClassDescriptor();
            }
            _classDesc = classDesc;
        }
        
        /**
         * Returns the XMLClassDescriptor that this InternalXMLClassDescriptor
         * wraps.
         *
         * @return the XMLClassDescriptor
         */
        public XMLClassDescriptor getClassDescriptor() {
            return _classDesc;
        } //-- getClassDescriptor
        
        
        /**
         * Returns the set of XMLFieldDescriptors for all members
         * that should be marshalled as XML attributes. This
         * includes namespace nodes.
         *
         * @return an array of XMLFieldDescriptors for all members
         * that should be marshalled as XML attributes.
         */
        public XMLFieldDescriptor[]  getAttributeDescriptors() {
            if (_attributes == null) {
                _attributes = _classDesc.getAttributeDescriptors();
            }
            return _attributes;
        } //-- getAttributeDescriptors

        /**
         * Returns the XMLFieldDescriptor for the member
         * that should be marshalled as text content.
         *
         * @return the XMLFieldDescriptor for the member
         * that should be marshalled as text content.
         */
        public XMLFieldDescriptor getContentDescriptor() {
            return _classDesc.getContentDescriptor();
        } //-- getContentDescriptor


        /**
         * Returns the XML field descriptor matching the given
         * xml name and nodeType. If NodeType is null, then
         * either an AttributeDescriptor, or ElementDescriptor
         * may be returned. Null is returned if no matching
         * descriptor is available.
         *
         * @param name the xml name to match against
         * @param nodeType the NodeType to match against, or null if
         * the node type is not known.
         * @return the matching descriptor, or null if no matching
         * descriptor is available.
         *
         */
        public XMLFieldDescriptor getFieldDescriptor
            (String name, String namespace, NodeType nodeType) 
        {
            return _classDesc.getFieldDescriptor(name, namespace, nodeType);
        } //-- getFieldDescriptor

        /**
         * Returns the set of XMLFieldDescriptors for all members
         * that should be marshalled as XML elements.
         *
         * @return an array of XMLFieldDescriptors for all members
         * that should be marshalled as XML elements.
         */
        public XMLFieldDescriptor[]  getElementDescriptors() {
            if (_elements == null) {
                _elements = _classDesc.getElementDescriptors();
            }
            return _elements;
        } //-- getElementDescriptors

        /**
         * @return the namespace prefix to use when marshalling as XML.
         */
        public String getNameSpacePrefix() {
            return _classDesc.getNameSpacePrefix();
        } //-- getNameSpacePrefix

        /**
         * @return the namespace URI used when marshalling and unmarshalling as XML.
         */
        public String getNameSpaceURI() {
            return _classDesc.getNameSpaceURI();
        } //-- getNameSpaceURI

        /**
         * Returns a specific validator for the class described by
         * this ClassDescriptor. A null value may be returned
         * if no specific validator exists.
         *
         * @return the type validator for the class described by this
         * ClassDescriptor.
         */
        public TypeValidator getValidator() {
            return _classDesc.getValidator();
        } //-- getValidator

        /**
         * Returns the XML Name for the Class being described.
         *
         * @return the XML name.
         */
        public String getXMLName() {
            return _classDesc.getXMLName();
        } //-- getXMLName

        /**
         * Returns true if the wrapped ClassDescriptor was created
         * by introspection.
         * 
         * @return true if the wrapped ClassDescriptor was created
         * by introspection.
         */
        public boolean introspected() {
            return Introspector.introspected(_classDesc);
        } //-- introspected
        
        /**
         * @see org.exolab.castor.xml.XMLClassDescriptor#canAccept(
         *      java.lang.String, java.lang.String, java.lang.Object)
         */
        public boolean canAccept(String name,String namespace, Object object) {
            return _classDesc.canAccept(name, namespace, object);
        } //-- canAccept

        /** 
         * {@inheritDoc}
         * 
         * @see org.exolab.castor.xml.XMLClassDescriptor#checkDescriptorForCorrectOrderWithinSequence(org.exolab.castor.xml.XMLFieldDescriptor, org.exolab.castor.xml.UnmarshalState, java.lang.String)
         */
        public void checkDescriptorForCorrectOrderWithinSequence(
                final XMLFieldDescriptor elementDescriptor, 
                final UnmarshalState parentState, 
                final String xmlName) 
            throws ValidationException {
            _classDesc.checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
        }

        //-------------------------------------/
        //- Implementation of ClassDescriptor -/
        //-------------------------------------/
        
        /**
         * Returns the Java class represented by this descriptor.
         *
         * @return The Java class
         */
        public Class getJavaClass() {
            return _classDesc.getJavaClass();
        } //-- getJavaClass


        /**
         * Returns a list of fields represented by this descriptor.
         *
         * @return A list of fields
         */
        public FieldDescriptor[] getFields() {
            if (_fields == null) {
                _fields = _classDesc.getFields();
            }
            return _fields;
        } //-- getFields


        /**
         * Returns the class descriptor of the class extended by this class.
         *
         * @return The extended class descriptor
         */
        public ClassDescriptor getExtends() {
            return _classDesc.getExtends();
        } //-- getExtends


        /**
         * Returns the identity field, null if this class has no identity.
         *
         * @return The identity field
         */
        public FieldDescriptor getIdentity() {
            return _classDesc.getIdentity();
        } //-- getIdentity
    } //-- InternalXMLClassDescriptor
} //-- MarshalFramework

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

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.CollectionHandlers;

import java.util.Vector;


/**
 * A core class for common code shared throughout the
 * Marshalling Framework
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
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
    
    //-----------------------------/
    //- Protected class variables -/
    //-----------------------------/

    /**
     * The default prefix used for specifying the
     * xsi:type as a classname instead of a schema name.
     * This is a Castor specific hack.
    **/
    static final String JAVA_PREFIX = "java:";

    /**
     * The name of the QName type
    **/
    static final String QNAME_NAME = "QName";

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
        if ((type == Boolean.class) || (type == Character.class))
            return true;

        return (type.getSuperclass() == Number.class);
    } //-- isPrimitive

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
     * Search there is a field descriptor which can accept one of the class
     * descriptor which match the given name and namespace.
     * @returns an array of InheritanceMatch.
     */
    public static InheritanceMatch[] searchInheritance(String name, String namespace, XMLClassDescriptor classDesc, ClassDescriptorResolver cdResolver) {


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
        cdResolver.resolve(className);
        //-- end Not-Yet-Loaded descriptor logic

        Vector inheritanceList = new Vector(3);
        XMLFieldDescriptor descriptor  = null;
        ClassDescriptorEnumeration cde = cdResolver.resolveAllByXMLName(name, namespace, null);
        XMLFieldDescriptor[] descriptors = classDesc.getElementDescriptors();
        XMLClassDescriptor cdInherited = null;

        if (cde.hasNext()) {
            while (cde.hasNext() && (descriptor == null)) {
                cdInherited = cde.getNext();
                Class subclass = cdInherited.getJavaClass();

                for (int i = 0; i < descriptors.length; i++) {

                    if (descriptors[i] == null) continue;
                    //-- check for inheritence
                    Class superclass = descriptors[i].getFieldType();

                    // It is possible that the superclass is of type object if we use any node.
                    if (superclass.isAssignableFrom(subclass) && (superclass != Object.class)) {
                        descriptor = descriptors[i];
                        inheritanceList.addElement(new InheritanceMatch(descriptor, cdInherited));
                    }
                }
            }
            //-- reset inherited class descriptor, if necessary
            if (descriptor == null) cdInherited = null;
        }

        InheritanceMatch[] result = new InheritanceMatch[inheritanceList.size()];
        inheritanceList.toArray(result);
        return result;
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


} //-- MarshalFramework

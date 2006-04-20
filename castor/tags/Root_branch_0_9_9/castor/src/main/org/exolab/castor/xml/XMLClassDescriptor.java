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

/**
 * A class descriptor for describing relationships between a Class
 * and an XML element or complexType. This class implements
 * org.exolab.castor.mapping.ClassDescriptor, yet adds 
 * extra methods for handling XML.
 * All fields are of type {@link XMLFieldDescriptor}.
 *
 * @author <a href="kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public interface XMLClassDescriptor extends ClassDescriptor {


    
    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML attributes. This
     * includes namespace nodes.
     *
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML attributes.
     */
    public XMLFieldDescriptor[] getAttributeDescriptors();

    
    /**
     * Returns the XMLFieldDescriptor for the member
     * that should be marshalled as text content.
     * @return the XMLFieldDescriptor for the member
     * that should be marshalled as text content.
     */
    public XMLFieldDescriptor getContentDescriptor();


    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
     */
    public XMLFieldDescriptor[] getElementDescriptors();
    
    /**
     * Returns the XML field descriptor matching the given
     * xml name, namespace, and nodeType. If NodeType is null, then
     * either an AttributeDescriptor, or ElementDescriptor
     * may be returned. Null is returned if no matching
     * descriptor is available.
     *
     * @param name the xml name to match against
     * @param nodeType, the NodeType to match against, or null if
     * the node type is not known.
     * @return the matching descriptor, or null if no matching
     * descriptor is available.
     *
     */
    public XMLFieldDescriptor getFieldDescriptor
        (String name, String namespace, NodeType nodeType);

    /**
     * Returns the namespace prefix to use when marshalling as XML.
     *
     * @return the namespace prefix to use when marshalling as XML.
     */
    public String getNameSpacePrefix();

    /**
     * Returns the namespace URI used when marshalling and unmarshalling as XML.
     *
     * @return the namespace URI used when marshalling and unmarshalling as XML.
     */
    public String getNameSpaceURI();

    /**
     * Returns a specific validator for the class described by
     * this ClassDescriptor. A null value may be returned
     * if no specific validator exists.
     *
     * @return the type validator for the class described by this
     * ClassDescriptor.
     */
    public TypeValidator getValidator();

    /**
     * Returns the XML Name for the Class being described.
     *
     * @return the XML name.
     */
    public String getXMLName();

    /**
     * <p>Returns true if the given object, represented by this 
     * XMLClassDescriptor, can accept a value for the member 
     * associated with the given xml name and namespace.</p>
     * 
     * <p>An XMLClassDescriptor can accept a value for a field if it 
     * contains a descriptor that matches the given xml name and 
     * namespace and if the given object can hold this field 
     * (i.e a value is not already set for this field).</p>
     * 
     * @param name the xml name of the field to check
     * @param namespace the namespace uri
     * @param object the object instance represented by this XMLCLassDescriptor
     * @return true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given.
     */
    public boolean canAccept(String name, String namespace, Object object);

} //-- XMLClassDescriptor



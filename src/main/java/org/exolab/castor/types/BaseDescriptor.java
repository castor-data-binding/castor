/*
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
 * Copyright 2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.types;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.UnmarshalState;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;

/**
 * The Base Descriptor class, this class is extended
 * by the descriptors for the custom Castor schema types.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-16 22:49:25 -0700 (Thu, 16 Dec 2004) $
 */
public class BaseDescriptor implements XMLClassDescriptor {

    /** Used for returning no attribute and no element fields */
    protected static final XMLFieldDescriptor[] noXMLFields = new XMLFieldDescriptor[0];
    /** Used for returning no attribute and no element fields */
    private static final FieldDescriptor[] noJavaFields = new FieldDescriptor[0];
    /** The name of the XML element. */
    private String _xmlName = null;
    /** The class type for the descriptor */
    private Class _class = null;

    //----------------/
    //- Constructors -/
    //----------------/

    protected BaseDescriptor(String xmlName, Class type) {
        super();
        _xmlName = xmlName;
        _class   = type;
    } //-- BaseDescriptor

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Returns the set of XMLFieldDescriptors for all members that should be
     * marshalled as XML attributes.
     *
     * @return an array of XMLFieldDescriptors for all members that should be
     *         marshalled as XML attributes.
     */
    public XMLFieldDescriptor[]  getAttributeDescriptors() {
        return noXMLFields;
    } // getAttributeDescriptors

    /**
     * Returns the XMLFieldDescriptor for the member that should be marshalled
     * as text content.
     *
     * @return the XMLFieldDescriptor for the member that should be marshalled
     *         as text content.
     */
    public XMLFieldDescriptor getContentDescriptor() {
        return null;
    } // getContentDescriptor

    /**
     * Returns the set of XMLFieldDescriptors for all members that should be
     * marshalled as XML elements.
     *
     * @return an array of XMLFieldDescriptors for all members that should be
     *         marshalled as XML elements.
     */
    public XMLFieldDescriptor[]  getElementDescriptors() {
        return noXMLFields;
    } // getElementDescriptors

    /**
     * Returns the XML field descriptor matching the given xml name and
     * nodeType. If NodeType is null, then either an AttributeDescriptor, or
     * ElementDescriptor may be returned. Null is returned if no matching
     * descriptor is available.
     *
     * @param name
     *            The xml name to match against.
     * @param namespace
     *            The namespace uri.
     * @param nodeType
     *            The NodeType to match against, or null if the node type is not
     *            known.
     * @return The matching descriptor, or null if no matching descriptor is
     *         available.
     */
    public XMLFieldDescriptor getFieldDescriptor(String name, String namespace, NodeType nodeType) {
        return null;
    } //-- getFieldDescriptor

    /**
     * @return the namespace prefix to use when marshalling as XML.
     */
    public String getNameSpacePrefix() {
        return null;
    } //-- getNameSpacePrefix

    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
     */
    public String getNameSpaceURI() {
        return null;
    } //-- getNameSpaceURI

    /**
     * Returns a specific validator for the class described by this
     * ClassDescriptor. A null value may be returned if no specific validator
     * exists.
     *
     * @return the type validator for the class described by this
     *         ClassDescriptor.
     */
    public TypeValidator getValidator() {
        return null;
    } //-- getValidator

    /**
     * Returns the XML Name for the Class being described.
     *
     * @return the XML name.
     */
    public String getXMLName() {
        return _xmlName;
    } //-- getXMLName

    /**
     * Returns the String representation of this XMLClassDescriptor.
     *
     * @return the String representation of this XMLClassDescriptor.
     */
    public String toString() {
        String className = null;
        Class type = getJavaClass();
        if (type != null) {
            className = type.getName();
        } else {
            className = "unspecified";
        }

        return super.toString() + "; descriptor for class: " + className + "; xml name: " + getXMLName();
    } //-- toString

    //-------------------------------------/
    //- Implementation of ClassDescriptor -/
    //-------------------------------------/

    /**
     * Returns the Java class represented by this descriptor.
     *
     * @return The Java class
     */
    public Class getJavaClass() {
        return _class;
    } //-- getJavaClass


    /**
     * Returns a list of fields represented by this descriptor.
     *
     * @return A list of fields
     */
    public FieldDescriptor[] getFields() {
        return noJavaFields;
    } //-- getFields


    /**
     * Returns the class descriptor of the class extended by this class.
     *
     * @return The extended class descriptor
     */
    public ClassDescriptor getExtends() {
        return null;
    } //-- getExtends

    /**
     * Returns the identity field, null if this class has no identity.
     *
     * @return The identity field
     */
    public FieldDescriptor getIdentity() {
        return null;
    } //-- getIdentity

    /**
     * Returns the access mode specified for this class.
     *
     * @return The access mode
     */
    public AccessMode getAccessMode() {
        return null;
    } //-- getAccessMode

    /**
     * Returns true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given. An XMLClassDescriptor can accept
     * a field if it contains a descriptor that matches the given name and if
     * the given object can hold this field (i.e a value is not already set for
     * this field).
     * <p>
     * This is mainly used for container object (that can contains other
     * object), in this particular case the implementation will return null.
     *
     * @param name
     *            the xml name of the field to check
     * @param namespace
     *            the namespace uri
     * @param object
     *            the object represented by this XMLCLassDescriptor
     * @return true if the given object represented by this XMLClassDescriptor
     *         can accept a member whose name is given.
     */
    public boolean canAccept(String name, String namespace, Object object) {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLClassDescriptor#checkDescriptorForCorrectOrderWithinSequence(org.exolab.castor.xml.XMLFieldDescriptor, org.exolab.castor.xml.UnmarshalState, java.lang.String)
     */
    public void checkDescriptorForCorrectOrderWithinSequence(final XMLFieldDescriptor elementDescriptor, 
            final UnmarshalState parentState, 
            final String xmlName) throws ValidationException {
        // nothing to check, iow empty implementation
    }

} //-- BaseDescriptor

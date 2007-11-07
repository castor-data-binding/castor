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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.descriptors;

import java.util.Date;

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.handlers.DateFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * A ClassDescriptor for java.util.Date.
 *
 * @author <a href="kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-16 22:49:25 -0700 (Thu, 16 Dec 2004) $
 */
public class DateClassDescriptor extends BaseDescriptor {

    /** Used for returning no attribute and no element fields. */
    private static final XMLFieldDescriptor[]   NO_FIELDS = new XMLFieldDescriptor[0];
    /** The name of the XML element. */
    private static final String                 XML_NAME  = "date";
    /** Our field descriptor. */
    private static final XMLFieldDescriptorImpl CONTENT_DESCRIPTOR;
    /** Our field descriptor array.  Lists the fields we describe. */
    private static final FieldDescriptor[]      FIELDS;
    /** The TypeValidator to use for validation of the described class. */
    private static final TypeValidator          VALIDATOR = null;

    static {
        CONTENT_DESCRIPTOR = new XMLFieldDescriptorImpl(String.class,
                "content", "content", NodeType.Text);

        CONTENT_DESCRIPTOR.setImmutable(true);
        CONTENT_DESCRIPTOR.setHandler(new DateFieldHandler(new XMLFieldHandler() {

            /**
             * {@inheritDoc}
             */
            public Object getValue(final Object object) throws IllegalStateException {
                return object;
            }

            /**
             * {@inheritDoc}
             */
            public void setValue(final Object object, final Object value)
                        throws IllegalStateException, IllegalArgumentException {
                if (object.getClass() == java.util.Date.class) {
                    Date target = (Date) object;
                    if (value.getClass() == java.util.Date.class) {
                        target.setTime(((Date) value).getTime());
                    }
                }
            }

            /**
             * {@inheritDoc}
             */
            public Object newInstance(final Object parent) {
                return null;
            }
        }));

        FIELDS = new FieldDescriptor[1];
        FIELDS[0] = CONTENT_DESCRIPTOR;
    }

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * No-arg constructor.
     */
    public DateClassDescriptor() {
        super();
    } //-- DateDescriptor

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Returns the set of XMLFieldDescriptors for all members that should be
     * marshaled as XML attributes.
     *
     * @return an array of XMLFieldDescriptors for all members that should be
     *         marshaled as XML attributes.
     */
    public XMLFieldDescriptor[]  getAttributeDescriptors() {
        return NO_FIELDS;
    } // getAttributeDescriptors

    /**
     * Returns the XMLFieldDescriptor for the member that should be marshaled
     * as text content.
     *
     * @return the XMLFieldDescriptor for the member that should be marshaled
     *         as text content.
     */
    public XMLFieldDescriptor getContentDescriptor() {
        return CONTENT_DESCRIPTOR;
    } // getContentDescriptor

    /**
     * Returns the set of XMLFieldDescriptors for all members that should be
     * marshaled as XML elements.
     *
     * @return an array of XMLFieldDescriptors for all members that should be
     *         marshaled as XML elements.
     */
    public XMLFieldDescriptor[]  getElementDescriptors() {
        return NO_FIELDS;
    } // getElementDescriptors

    /**
     * Returns the XML field descriptor matching the given xml name and
     * nodeType. If NodeType is null, then either an AttributeDescriptor, or
     * ElementDescriptor may be returned. Null is returned if no matching
     * descriptor is available.
     *
     * @param name the xml name to match against
     * @param namespace the namespace of the element. This may be null. Note: A
     *        null namespace is not the same as the default namespace unless the
     *        default namespace is also null.
     * @param nodeType the NodeType to match against, or null if the node type
     *        is not known.
     * @return the matching descriptor, or null if no matching descriptor is
     *         available.
     */
    public XMLFieldDescriptor getFieldDescriptor(final String name,
            final String namespace, final NodeType nodeType) {
        return null;
    } //-- getFieldDescriptor

    /**
     * @return the namespace prefix to use when marshaling as XML.
     */
    public String getNameSpacePrefix() {
        return null;
    } //-- getNameSpacePrefix

    /**
     * @return the namespace URI used when marshaling and unmarshaling as XML.
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
        return VALIDATOR;
    } //-- getValidator

    /**
     * Returns the XML Name for the Class being described.
     *
     * @return the XML name.
     */
    public String getXMLName() {
        return XML_NAME;
    } //-- getXMLName

    /**
     * Returns the String representation of this XMLClassDescriptor.
     * @return the String representation of this XMLClassDescriptor.
     */
    public String toString() {
        return super.toString() + "; descriptor for class: Date" + "; xml name: " + XML_NAME;
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
        return java.util.Date.class;
    } //-- getJavaClass

    /**
     * Returns a list of fields represented by this descriptor.
     *
     * @return A list of fields
     */
    public FieldDescriptor[] getFields() {
        return FIELDS;
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

} //-- class: DateClassDescriptor

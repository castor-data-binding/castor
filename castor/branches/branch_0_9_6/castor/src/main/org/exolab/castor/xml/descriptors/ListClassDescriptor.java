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
 * Copyright 2000-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.descriptors;


import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * The default java.util.List class descriptor
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ListClassDescriptor
    implements XMLClassDescriptor
{


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The set of attribute descriptors
    **/
    private static final XMLFieldDescriptor[] _attributes =
        new XMLFieldDescriptor[0];

    /**
     * The content descriptor
    **/
    private static final XMLFieldDescriptorImpl _contentDesc = null;

    /**
     * The set of element descriptors
    **/
    private XMLFieldDescriptor[] _elements = null;


    private FieldDescriptor[] _fields = null;

    private XMLFieldDescriptorImpl _desc = null;

    /**
     * The XML name for the described object.
    **/
    private String _xmlName = null;

    /**
     * The desired namespace for the described object
    **/
    private String _nsURI   = null;

    private TypeValidator _validator = null;

      //----------------/
     //- Constructors -/
    //----------------/

    public ListClassDescriptor() {
        this(null);
    } //-- ListClassDescriptor

    public ListClassDescriptor(String xmlName) {
        super();

        _xmlName = xmlName;

        //-- Create FieldDescriptor
        XMLFieldDescriptorImpl _desc
            = new XMLFieldDescriptorImpl(Object.class, "item", _xmlName,
                NodeType.Element);

        _desc.setMultivalued(true);
        _desc.setMatches("*");
        _desc.setHandler( new XMLFieldHandler() {

            public Object getValue( Object object )
                throws IllegalStateException
            {
                List list = (java.util.List)object;
                return new IteratorEnumerator( list.iterator() );
            }

            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ((java.util.List)object).add(value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new ArrayList();
            }
        } );

        _fields = new FieldDescriptor[1];
        _fields[0] = _desc;

        _elements = new XMLFieldDescriptor[1];
        _elements[0] = _desc;
    } //-- ListClassDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the set of attribute XMLFieldDescriptors
     * @return an array of XMLFieldDescriptors for all members that
     * should be marshalled as attributes
    **/
    public XMLFieldDescriptor[] getAttributeDescriptors() {
        return _attributes;
    } //-- getAttributeDescriptors()

    /**
     * Returns the Class that this ClassDescriptor describes
     * @return the Class that this ClassDescriptor describes
    **/
    public Class getJavaClass() {
        return java.util.List.class;
    } //-- getClassType()

    /**
     * Returns the set of element MarshalDescriptors
     * @return an array of MarshalDescriptors for all members that
     * should be marshalled as Elements
    **/
    public XMLFieldDescriptor[] getElementDescriptors() {
        return _elements;
    } //-- getElementDescriptors()

    /**
     * Returns the class descriptor of the class extended by this class.
     *
     * @return The extended class descriptor
     */
    public ClassDescriptor getExtends() {
        return null;
    } //-- getExtends

    /**
     * Returns a list of fields represented by this descriptor.
     *
     * @return A list of fields
     */
    public FieldDescriptor[] getFields() {
        return _fields;
    } //-- getFields

    /**
     * Returns the descriptor for dealing with Text content
     * @return the XMLFieldDescriptor for dealing with Text content
    **/
    public XMLFieldDescriptor getContentDescriptor() {
        return _contentDesc;
    } //-- getContentDescriptor()

    /**
     * Returns the XML field descriptor matching the given
     * xml name and nodeType. If NodeType is null, then
     * either an AttributeDescriptor, or ElementDescriptor
     * may be returned. Null is returned if no matching
     * descriptor is available.
     *
     * @param name the xml name to match against
     * @param namespace the namespace uri
     * @param nodeType, the NodeType to match against, or null if
     * the node type is not known.
     * @return the matching descriptor, or null if no matching
     * descriptor is available.
     *
    **/
    public XMLFieldDescriptor getFieldDescriptor
        (String name, String namespace, NodeType nodeType)
    {
        if ((nodeType == null) || (nodeType == NodeType.Element)) {
            for (int i = 0; i < _elements.length; i++) {
                XMLFieldDescriptor desc = _elements[i];
                if (desc == null) continue;
                if (desc.matches(name)) return desc;
            }
        }
        return null;
    } //-- getFieldDescriptor

    /**
     * @return the namespace prefix to use when marshalling as XML.
    **/
    public String getNameSpacePrefix() {
        return null;
    } //-- getNameSpacePrefix

    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
    **/
    public String getNameSpaceURI() {
        return _nsURI;
    } //-- getNameSpaceURI

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
     * Returns a specific validator for the class described by
     * this ClassDescriptor. A null value may be returned
     * if no specific validator exists.
     *
     * @return the type validator for the class described by this
     * ClassDescriptor.
    **/
    public TypeValidator getValidator() {
        return _validator;
    } //-- getValidator

    /**
     * Returns the XML Name for the Class being described.
     *
     * @return the XML name.
    **/
    public String getXMLName() {
        return _xmlName;
    } //-- getXMLName

    public void setValidator(TypeValidator validator) {
        this._validator = validator;
    } //-- setValidator

    /**
     * Sets the XML Name for the described object.
     * @param xmlName the XML name to use for the described object.
    **/
    public void setXMLName(String xmlName) {
        if ((xmlName != null) && (xmlName.length() > 0)) {
            _xmlName = xmlName;
            _desc.setXMLName(xmlName);
        }
    } //-- setXMLName

    /**
     * Sets the desired namespace URI for the described object
     * @param nsURI is the desired namespace URI
    **/
    public void setNameSpaceURI(String nsURI) {
        this._nsURI = nsURI;
    } //-- setNameSpaceURI

    /**
     * <p>Returns true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given.
     * An XMLClassDescriptor can accept a field if it contains a descriptor that matches
     * the given name and if the given object can hold this field (i.e a value is not already set for
     * this field).
     * @param name the xml name of the field to check
     * @param namespace the namespace uri
     * @param object the object represented by this XMLCLassDescriptor
     * @return true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given.
     */
    public boolean canAccept(String name, String namespace, Object object) {
        //-- ListClassDescriptor only contains one FieldDescriptor
        //-- that matches with a wild-card '*', just return true
        //-- since it can accept any object
        return true;
    }


    /**
     * A simple Enumeration that wraps a JDK 1.2 Iterator
    **/
    static final class IteratorEnumerator implements java.util.Enumeration {

        private final Iterator _iterator;

        IteratorEnumerator( Iterator iterator )
        {
            _iterator = iterator;
        }

        public boolean hasMoreElements()
        {
            return ( _iterator.hasNext() );
        }

        public Object nextElement()
        {
            return _iterator.next();
        }
    } //-- class: IteratorEnumerator

} //-- class: ListClassDescriptor
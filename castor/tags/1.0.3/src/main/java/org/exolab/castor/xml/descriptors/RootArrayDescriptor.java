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
 * Copyright 2002-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.descriptors;


import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;



/**
 * The default class descriptor for Arrays that are passed
 * in as the root-level class.
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-16 22:49:25 -0700 (Thu, 16 Dec 2004) $
**/
public class RootArrayDescriptor
    implements XMLClassDescriptor
{


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The set of element descriptors
    **/
    private static final XMLFieldDescriptor[] elements =
        new XMLFieldDescriptor[1];

    /**
     * The set of attribute descriptors
    **/
    private static final XMLFieldDescriptor[] attributes =
        new XMLFieldDescriptor[0];

    /**
     * The content descriptor
    **/
    private static final XMLFieldDescriptor contentDesc = null;

    private static final FieldDescriptor[] fields = new FieldDescriptor[1];

    /**
     * The XML name for the described object.
    **/
    private String _xmlName = "array";

    /**
     * The desired namespace for the described object
    **/
    private String _nsURI   = null;

    private TypeValidator _validator = null;

    private Class _javaClass = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new RootArrayDescriptor
    **/
    public RootArrayDescriptor(Class array) {
        super();
        if (array == null) {
            String err = "Argument array must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (!array.isArray()) {
            String err = "Argument array must be an array.";
            throw new IllegalArgumentException(err);            
        }
        _javaClass = array.getComponentType();
        
        
        //-- create element descriptor
        XMLFieldDescriptorImpl desc = null;
        FieldHandler handler = null;
        
        desc = new XMLFieldDescriptorImpl(_javaClass, "_elements", null, NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                return object;
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                //-- do nothing
            }
            public Object newInstance( Object parent ) {
                //-- not used
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(true);
        elements[0] = desc;
        fields[0] = desc;
        
    } //-- RootArrayDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the set of attribute XMLFieldDescriptors
     * @return an array of XMLFieldDescriptors for all members that
     * should be marshalled as attributes
    **/
    public XMLFieldDescriptor[] getAttributeDescriptors() {
        return attributes;
    } //-- getAttributeDescriptors()

    /**
     * Returns the Class that this ClassDescriptor describes
     * @return the Class that this ClassDescriptor describes
    **/
    public Class getJavaClass() {
        return _javaClass;
    } //-- getClassType()

    /**
     * Returns the set of element MarshalDescriptors
     * @return an array of MarshalDescriptors for all members that
     * should be marshalled as Elements
    **/
    public XMLFieldDescriptor[] getElementDescriptors() {
        return elements;
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
        return fields;
    } //-- getFields

    /**
     * Returns the descriptor for dealing with Text content
     * @return the XMLFieldDescriptor for dealing with Text content
    **/
    public XMLFieldDescriptor getContentDescriptor() {
        return contentDesc;
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
        return elements[0];
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

    /**
     * Sets the XML Name for the described object.
     * @param xmlName the XML name to use for the described object.
    **/
    public void setXMLName(String xmlName) {
        this._xmlName = xmlName;
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
     * <p>This is mainly used for container object (that can contains other object), in this particular case
     * the implementation will return null.
     * @param name the xml name of the field to check
     * @param namespace the namespace uri
     * @param object the object represented by this XMLCLassDescriptor
     * @return true if the given object represented by this XMLClassDescriptor
     * can accept a member whose name is given.
     */
    public boolean canAccept(String name, String namespace, Object object) {
         return false;
    }
} //-- RootArrayDescriptor

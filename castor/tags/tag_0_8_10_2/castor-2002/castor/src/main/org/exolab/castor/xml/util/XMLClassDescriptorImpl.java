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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.util;


import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.*;

import org.exolab.castor.util.List;

/**
 * An implementation of XMLClassDescriptor
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 * <b>Note:</b>This used to be SimpleMarshalInfo.
 */
public class XMLClassDescriptorImpl 
    implements XMLClassDescriptor 
{
    
    private static final String NULL_CLASS_ERR 
        = "The Class passed as an argument to the constructor of " +
          "XMLClassDescriptorImpl may not be null.";

    /**
     * The Class that this ClassDescriptor describes
    **/
    private Class _class = null;
    
    /**
     * The class name of the Class this marshal info describes.
     * Used when _class == null, or for String expressions
    **/
    private String _className = null;
    
    /**
     * The set of attribute descriptors
    **/
    private List attributeDescriptors = null;
    
    /**
     * The XMLFieldDescriptor for text data
    **/
    private XMLFieldDescriptor contentDescriptor = null;
    
    /**
     * The TypeValidator to use for validation of the described class
    **/
    private TypeValidator validator = null;

    /**
     * The set of element descriptors
    **/
    private List elementDescriptors = null;

    
    /**
     * The namespace prefix that is to be used when marshalling
    **/
    private String nsPrefix = null;
    
    /**
     * The namespace URI used for both Marshalling and Unmarshalling
    **/
    private String nsURI = null;
    

    /**
     * The name of the XML element.
     */
    private String  _xmlName;


    /**
     * The descriptor of the class which this class extends,
     * or null if this is a top-level class.
     */
    private XMLClassDescriptor     _extends;


    /**
     * The field of the identity for this class.
     */
    private FieldDescriptor    _identity;


    /**
     * The access mode specified for this class.
     */
    private AccessMode         _accessMode;

    /**
     * A flag to indicate that this XMLClassDescriptor was
     * created via introspection
    **/
    private boolean            _introspected = false;
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    
    /**
     * Creates an XMLClassDescriptor class used by the Marshalling Framework.
     * @param type the Class type with which this ClassDescriptor describes.
    **/
    public XMLClassDescriptorImpl(Class type) {
        this();
        if (type == null) 
            throw new IllegalArgumentException(NULL_CLASS_ERR);
            
        this._class = type;
        setXMLName(null);
    } //-- XMLClassDescriptorImpl

    /**
     * Creates an XMLClassDescriptor class used by the Marshalling Framework.
     * @param type the Class type with which this ClassDescriptor describes.
    **/
    public XMLClassDescriptorImpl(Class type, String xmlName) {
        this();

        if (type == null) 
            throw new IllegalArgumentException(NULL_CLASS_ERR);
            
        this._class = type;
        setXMLName(xmlName);
    } //-- XMLClassDescriptorImpl

    /**
     * Protected constructor used by this class, and subclasses only
    **/
    protected XMLClassDescriptorImpl() {
        attributeDescriptors = new List(7);
        elementDescriptors = new List(7);
        //validationRules = new List();
    } //-- XMLClassDescriptor
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Adds the given XMLFieldDescriptor to the list of descriptors. The
     * descriptor will be added to the appropriate list by calling
     * XMLFieldDescriptor#getNodeType() to determine it's type.
     * @param descriptor the XMLFieldDescriptor to add 
    **/
    public void addFieldDescriptor(XMLFieldDescriptor descriptor) {
        
	descriptor.setContainingClassDescriptor( this );

        NodeType nodeType = descriptor.getNodeType();
        switch(nodeType.getType()) {
            case NodeType.ATTRIBUTE:
                attributeDescriptors.add(descriptor);
                break;
            case NodeType.TEXT:
                contentDescriptor = descriptor;
                break;
            default:
                elementDescriptors.add(descriptor);
                break;
        }
            
    } //-- addFieldDescriptor

    
    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML attributes.
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML attributes.
    **/
    public XMLFieldDescriptor[]  getAttributeDescriptors() {
        
        XMLFieldDescriptor[] fields 
            = new XMLFieldDescriptor[attributeDescriptors.size()];
            
        attributeDescriptors.toArray(fields);
        return fields;
    } // getAttributeDescriptors
    
    /**
     * Returns the XMLFieldDescriptor for the member
     * that should be marshalled as text content.
     * @return the XMLFieldDescriptor for the member
     * that should be marshalled as text content.
    **/
    public XMLFieldDescriptor getContentDescriptor() {
        return contentDescriptor;
    } // getContentDescriptor
    
    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
    **/
    public XMLFieldDescriptor[]  getElementDescriptors() {
        XMLFieldDescriptor[] fields 
            = new XMLFieldDescriptor[elementDescriptors.size()];
        elementDescriptors.toArray(fields);
        return fields;
    } // getElementDescriptors

    /**
     * Returns the XML field descriptor matching the given
     * xml name and nodeType. If NodeType is null, then
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
    **/
    public XMLFieldDescriptor getFieldDescriptor
        (String name, NodeType nodeType) 
    {
        
        boolean wild = ((nodeType == null) || _introspected);
        
        if (wild || (nodeType == NodeType.Element)) {
            XMLFieldDescriptor desc = null;
            for (int i = 0; i < elementDescriptors.size(); i++) {
                desc = (XMLFieldDescriptor)elementDescriptors.get(i);
                if (desc == null) continue;
                if (desc.matches(name)) return desc;
            }
        }
        
        if (wild || (nodeType == NodeType.Attribute)) {
            XMLFieldDescriptor desc = null;
            for (int i = 0; i < attributeDescriptors.size(); i++) {
                desc = (XMLFieldDescriptor)attributeDescriptors.get(i);
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
        return nsPrefix;
    } //-- getNameSpacePrefix
    
    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
    **/
    public String getNameSpaceURI() {
        return nsURI;
    } //-- getNameSpaceURI

    /**
     * Returns a specific validator for the class described by
     * this ClassDescriptor. A null value may be returned
     * if no specific validator exists. 
     *
     * @return the type validator for the class described by this
     * ClassDescriptor. 
    **/
    public TypeValidator getValidator() {
        return validator;
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
     * Removes the given XMLFieldDescriptor from the list of descriptors. 
     * @param descriptor the XMLFieldDescriptor to remove
    **/
    public void removeFieldDescriptor(XMLFieldDescriptor descriptor) {
        
        if (descriptor == null) return;
        
        NodeType nodeType = descriptor.getNodeType();
        switch(nodeType.getType()) {
            
            case NodeType.ATTRIBUTE:
                attributeDescriptors.remove(descriptor);
                break;
            case NodeType.TEXT:
                if (contentDescriptor == descriptor)
                    contentDescriptor = null;
                break;
            default:
                elementDescriptors.remove(descriptor);
                break;
        }
            
    } //-- removeFieldDescriptor
    
    /**
     * Sets the XMLClassDescriptor that this descriptor inherits from
     * @param classDesc the XMLClassDescriptor that this descriptor
     * extends
    **/
    public void setExtends(XMLClassDescriptor classDesc) {
        
        FieldDescriptor[] fields = null;
        //-- remove reference to previous extended descriptor
        if (_extends != null) {
            sortDescriptors();
            fields = _extends.getFields();
            for (int i = 0; i < fields.length; i++) {
                removeFieldDescriptor((XMLFieldDescriptor)fields[i]);
            }
        }
        
        this._extends = classDesc;
        
        //-- flatten out the hierarchy
        if (_extends != null) {
            fields = classDesc.getFields();
            for (int i = 0; i < fields.length; i++) {
                addFieldDescriptor((XMLFieldDescriptor)fields[i]);
            }
        }
        
    } //-- setExtends
    
    /**
     * Sets the Identity FieldDescriptor, if the FieldDescriptor is
     * not already a contained in this ClassDescriptor, it will be
     * added
    **/
    public void setIdentity(XMLFieldDescriptor fieldDesc) {
        if (fieldDesc != null) {
            if ( (! attributeDescriptors.contains(fieldDesc)) &&
                (! elementDescriptors.contains(fieldDesc))) {
                addFieldDescriptor(fieldDesc);
            }
        }
        this._identity = fieldDesc;
    } //-- setIdentity
    
    /**
     * Sets the namespace prefix used when marshalling as XML.
     * @param nsPrefix the namespace prefix used when marshalling
     * the "described" object
    **/
    public void setNameSpacePrefix(String nsPrefix) {
        this.nsPrefix = nsPrefix;
    } //-- setNameSpacePrefix
    
    /**
     * Sets the namespace URI used when marshalling and unmarshalling as XML.
     * @param nsURI the namespace URI used when marshalling and
     * unmarshalling the "described" Object.
    **/
    public void setNameSpaceURI(String nsURI) {
        this.nsURI = nsURI;
    } //-- setNameSpaceURI

    /**
     * Sets the validator to use for the class described by this
     * ClassDescriptor
     *
     * @param validator the validator to use when peforming validation
     * of the described class. This may be null to signal default
     * validation.
    **/
    public void setValidator(TypeValidator validator) {
        this.validator = validator;
    } //-- setValidator
    
    /**
     * Sets the XML name for the Class described by this XMLClassDescriptor
     *
     * @param xmlName the XML name for the Class described by this 
     * XMLClassDescriptor
    **/
    public void setXMLName(String xmlName) {
        if (xmlName == null) {
            if (_class != null) {
                _xmlName = toXMLName(_class.getName());
            }
        }
        else this._xmlName = xmlName;
    } //-- setXMLName
    
    /** 
     * This method is used to keep the set of descriptors in the proper
     * sorted lists. If you dynamically change the NodeType of
     * an XMLFieldDescriptor after adding it the this ClassDescriptor,
     * then call this method.
    **/
    public void sortDescriptors() {
        
        XMLFieldDescriptor fieldDesc = null;
        NodeType nodeType = null;
        
        List remove = new List(3);
        for (int i = 0; i < attributeDescriptors.size(); i++) {
            fieldDesc = (XMLFieldDescriptor)attributeDescriptors.get(i);
            switch (fieldDesc.getNodeType().getType()) {
                case NodeType.ELEMENT:
                    elementDescriptors.add(fieldDesc);
                    remove.add(fieldDesc);
                    break;
                case NodeType.TEXT:
                    remove.add(fieldDesc);
                    break;
                default:
                    break;
            }
        }
        for (int i = 0; i < remove.size(); i++)
            attributeDescriptors.remove(remove.get(i));
        
        remove.clear();
        for (int i = 0; i < elementDescriptors.size(); i++) {
            fieldDesc = (XMLFieldDescriptor)elementDescriptors.get(i);
            switch (fieldDesc.getNodeType().getType()) {
                case NodeType.ATTRIBUTE:
                    attributeDescriptors.add(fieldDesc);
                    remove.add(fieldDesc);
                    break;
                case NodeType.TEXT:
                    remove.add(fieldDesc);
                    break;
                default:
                    break;
            }
        }
        for (int i = 0; i < remove.size(); i++)
            elementDescriptors.remove(remove.get(i));
            
    } //-- sortDescriptors
    
    /**
     * Returns the String representation of this XMLClassDescriptor
     * @return the String representation of this XMLClassDescriptor
    **/
    public String toString() {
        
        String str = super.toString() + "; descriptor for class: ";
        
        //-- add class name
        if (_class != null) 
            str += _class.getName();
        else 
            str += "[null]";
        
        //-- add xml name
        str += "; xml name: " + _xmlName;
        
        return str;
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
        int size = attributeDescriptors.size();
        size += elementDescriptors.size();
        if (contentDescriptor != null) ++size;
        
        FieldDescriptor[] fields = new FieldDescriptor[size];
        int c = 0;
        for (int i = 0; i < attributeDescriptors.size(); i++)
            fields[c++] = (FieldDescriptor) attributeDescriptors.get(i);
            
        for (int i = 0; i < elementDescriptors.size(); i++)
            fields[c++] = (FieldDescriptor) elementDescriptors.get(i);
            
        if (contentDescriptor != null) 
            fields[c] = contentDescriptor;
            
        return fields;
    } //-- getFields
    


    /**
     * Returns the class descriptor of the class extended by this class.
     *
     * @return The extended class descriptor
     */
    public ClassDescriptor getExtends() {
        return _extends;
    } //-- getExtends


    /**
     * Returns the identity field, null if this class has no identity.
     *
     * @return The identity field
     */
    public FieldDescriptor getIdentity() {
        return _identity;
    } //-- getIdentity


    /**
     * Returns the access mode specified for this class.
     *
     * @return The access mode
     */
    public AccessMode getAccessMode() {
        return _accessMode;
    } //-- getAccessMode
    
    //---------------------/
    //- Protected Methods -/
    //---------------------/
    
    /**
     * Sets the Class type being described by this descriptor.
     *
     * @type the Class type being described
    **/
    protected void setJavaClass(Class type) {
        this._class = type;
    } //-- setJavaClass

    protected void setExtendsWithoutFlatten(XMLClassDescriptor classDesc) {
        this._extends = classDesc;
    } //-- setExtendsWithoutFlatten
    
    /**
     * Sets a flag to indicate whether or not this XMLClassDescriptorImpl
     * was created via introspection
     *
     * @param introspected a boolean, when true indicated that this
     * XMLClassDescriptor was created via introspection
    **/
    protected void setIntrospected(boolean introspected) {
        this._introspected = introspected;
    } //-- setIntrospected
    
    protected String toXMLName(String className) {
        //-- create default XML name
        String name = className;
        int idx = name.lastIndexOf('.');
        if (idx >= 0) name = name.substring(idx+1);
        return MarshalHelper.toXMLName(name);
    }
    
    
} //-- XMLClassDescriptor



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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import java.lang.reflect.*;

/**
 * A class used to obtain information about how to handle Object Marshalling
 * @author <a href="kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class MarshalDescriptor {
    
    private static final String TYPE_NULL_ERR
        = "The class type of a MarshalDescriptor cannot be null.";
        
    /**
     * The type used by Attribute Descriptors
    **/
    public static final short ATTRIBUTE = 0;
    
    /**
     * The type used by Element Descriptors
    **/
    public static final short ELEMENT = 1;
    
    
    /**
     * The access rights for the described field
    **/
    private AccessRights accessRights = AccessRights.both;
    
    /**
     * Flag to indicate that objects should be added
     * to their as soon as they are created, but before they
     * are finished being populated.
    **/
    private boolean incremental = false;
    
    /**
     * The maximum occurance the described field may occur
    **/
    private int maxOccurs = -1;
    
    /**
     * The minimum occurance the described field may occur 
    **/
    private int minOccurs = 0;
    
    /**
     * The programmatic name of the member
    **/
    private String name = null;
    
    /**
     * The namespace prefix that is to be used when marshalling
    **/
    private String nsPrefix = null;
    
    /**
     * The namespace URI used for both Marshalling and Unmarshalling
    **/
    private String nsURI = null;
    
    /**
     * indicates a required field
    **/
    public boolean required = false;
    
    /**
     * The Validator to use when validating 
    **/
    private Validator validator = null;
    
    /**
     * The XML name of the member
    **/
    private String xmlName = null;
    
    /**
     * The Class type of described field
    **/
    private Class type = null;
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    /**
     * Creates a new MarshalDescriptor with the given class
     * type and names
     * @param type the Class type of the described field
     * @param name the programmatic name of the field
     * @param xmlName the XML name of the field
    **/
    protected MarshalDescriptor(Class type, String name, String xmlName) {
        
        if (type == null) 
            throw new IllegalArgumentException(TYPE_NULL_ERR);
            
        this.type = type;
        this.name = name;
        this.xmlName = xmlName;
    } //-- MarshalDescriptor
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Returns the access rights for the given field
     * @return the AccessRights for the given field
    **/
    public AccessRights getAccessRights() {
        return accessRights;
    } //-- getAccessRights
    
    /**
     * Returns the MarshalInfo class for the member described by
     * this descriptor. This can be null if the MarshalInfo should
     * be obtained automatically.
     * @return the MarshalInfo for the Class of the member described by this
     * descriptor. This can be null if the MarshalInfo should be
     * obtained automatically.
    **/
    public abstract MarshalInfo getMarshalInfo();
    
    /**
     * Returns the DescriptorType for this MarshalDescriptor
     * @return the DescriptorType for this MarshalDescriptor
    **/
    public abstract DescriptorType getDescriptorType();    
    
    /**
     * Returns the programmatic name of the field
     * @return the programmatic name of the field
    **/
    public String getName() {
        return name;
    } //-- getName
    
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
     * Returns the XML Name for the member associated with this Descriptor
     * @return the XML Name for the member associated with this Descriptor
    **/
    public String getXMLName() {
        return xmlName;
    } //-- getXMLName
    
    
    /**
     * Returns the Method that should be invoked that will
     * create an instance of this member.
     * By default this method will return null, indicating that the
     * Object's default constructor should be used. This method is
     * useful for Object's that might be protected and need to
     * be instantiated by a different class than the unmarshaller.
    **/
    public Method getCreateMethod() {
        return null;
    } //-- getCreateMethod
    
    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public abstract Object getValue(Object target)
        throws java.lang.reflect.InvocationTargetException,
               java.lang.IllegalAccessException;

        
    /**
     * Returns the type of the field described by this descriptor
     * @return the Class type of the field described by this descriptor
    **/
    public final Class getFieldType() {
        return type;
    } //-- getFieldType

    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field 
    **/
    public abstract void setValue(Object target, Object value)
        throws java.lang.reflect.InvocationTargetException,
               java.lang.IllegalAccessException;
    
    /**
     * Returns the incremental flag which when true indicates that this
     * member may be safely added before the unmarshaller is finished
     * unmarshalling it.
     * @return true if the Object can safely be added before the unmarshaller
     * is finished unmarshalling the Object.
    **/
    public boolean isIncremental() {
        return incremental;
    } //-- isIncremental
    
    /**
     * Returns true if the field described by this descriptor is a required
     * field
     * @return true if the field described by this descriptor is a required
     * field
    **/
    public boolean isRequired() {
        return required;
    } //-- isRequired
    
    /**
     * Returns true if this descriptor can be used to handle elements
     * or attributes with the given XML name. By default this method
     * simply compares the given XML name with the internal XML name.
     * This method can be overridden to provide more complex matching.
     * @param xmlName the XML name to compare
     * @return true if this descriptor can be used to handle elements
     * or attributes with the given XML name.
    **/
    public boolean matches(String xmlName) {
        
        if (xmlName != null) {
            return xmlName.equals(this.xmlName);
        }
        return false;
    } //-- matches
    
    /**
     * Sets the access rights for the described field
     * @param access the AccessRights for the described field
    **/
    public void setAccessRights(AccessRights access) {
        this.accessRights = access;
    } //-- setAccessRights
    
    /**
     * Sets the incremental flag which indicates whether this member
     * can be added before the unmarshaller is finished unmarshalling it.
     * @param incremental the boolean which if true indicated that this
     * member can safely be added before the unmarshaller is finished 
     * unmarshalling it.
    **/
    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    } //-- setIncremental
    
    
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
     * Sets the whether or not the described field is required
     * @param required the flag indicating whether or not the
     * described field is required
    **/
    public void setRequired(boolean required) {
        this.required = required;
    } //-- setRequired
        
} //-- MarshalDescriptor

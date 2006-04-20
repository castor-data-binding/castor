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

import java.util.Vector;
import java.lang.reflect.Method;

/**
 * A simple implementation of the MarshalInfo interface. This class
 * is primarily used by the Marshalling Framework to create information
 * about Objects and Classes when no other MarshalInfo class exists.
 * @author <a href="kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SimpleMarshalInfo implements MarshalInfo {
    
    /**
     * The Class that this MarshalInfo describes
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
    private Vector attributeDescriptors = null;
    
    /**
     * The MarshalDescriptor for characters
    **/
    private MarshalDescriptor contentDescriptor = null;
    
    /**
     * The ValidationRule to use when performing validation
     * of instances of the Class associated with this MarshalInfo
    **/
    private Vector validationRules = null;

    /**
     * The set of element descriptors
    **/
    private Vector elementDescriptors = null;

    
    /**
     * The namespace prefix that is to be used when marshalling
    **/
    private String nsPrefix = null;
    
    /**
     * The namespace URI used for both Marshalling and Unmarshalling
    **/
    private String nsURI = null;
    
    
    /**
     * Creates a MarshalInfo class used by the Marshalling Framework.
    **/
    public SimpleMarshalInfo(Class _class) {
        this._class = _class;
        attributeDescriptors = new Vector(3); 
        elementDescriptors = new Vector(5);
        validationRules = new Vector(7);
    } //-- SimpleMarshalInfo
    
    /**
     * Adds the given MarshalDescriptor to the list of descriptors. The
     * descriptor will be added to the appropriate list by calling
     * MarshalDescriptor#getDescriptorType() to determine it's type.
     * @param marshalDescriptor the MarshalDescriptor to add to the list
     * of descriptors.
    **/
    public void addMarshalDescriptor(MarshalDescriptor marshalDescriptor) {
        
        if (marshalDescriptor.getDescriptorType() == DescriptorType.attribute)
            attributeDescriptors.addElement(marshalDescriptor);
        else
            elementDescriptors.addElement(marshalDescriptor);
            
    } //-- addMarshalDescriptor

    /**
     * Adds the given MarshalDescriptor to the list of descriptors 
     * associated with attributes.
     * @param marshalDescriptor the MarshalDescriptor to add to the list
     * of attribute descriptors.
    **/
    public void addAttributeDescriptor(MarshalDescriptor marshalDescriptor) {
        attributeDescriptors.addElement(marshalDescriptor);
    } //-- addAttributeDescriptor

    /**
     * Adds the given MarshalDescriptor to the list of descriptors 
     * associated with elements.
     * @param marshalDescriptor the MarshalDescriptor to add to the list
     * of attribute descriptors.
    **/
    public void addElementDescriptor(MarshalDescriptor marshalDescriptor) {
        elementDescriptors.addElement(marshalDescriptor);
    } //-- addAttributeDescriptor
        
    /**
     * Adds the ValidationRule to the list of ValidationRules to 
     * use when validating instances of the class associated with this
     * MarshalInfo
     * @param validationRule the ValidationRule to add
    **/
    public void addValidationRule(ValidationRule validationRule) {
        validationRules.addElement(validationRule);
    } //-- setValidatorRule
    
    /**
     * Returns the Class that this MarshalInfo describes
     * @return the Class that this MarshalInfo describes
    **/
    public Class getClassType() {
        return _class;
    } //-- getClassType
    
    /**
     * Returns the set of attribute MarshalDescriptors
     * @return an array of MarshalDescriptors for all members that
     * should be marshalled as Attributes
    **/
    public MarshalDescriptor[] getAttributeDescriptors() {
        int size = attributeDescriptors.size();
        MarshalDescriptor[] mdArray = new MarshalDescriptor[size];
        attributeDescriptors.copyInto(mdArray);
        return mdArray;
    } //-- getAttributeDescriptors
    
    /**
     * Returns the set of element MarshalDescriptors
     * @return an array of MarshalDescriptors for all members that
     * should be marshalled as Elements
    **/
    public MarshalDescriptor[]  getElementDescriptors() {
        int size = elementDescriptors.size();
        MarshalDescriptor[] mdArray = new MarshalDescriptor[size];
        elementDescriptors.copyInto(mdArray);
        return mdArray;
    }

    /**
     * Returns the descriptor for dealing with Text content
     * @return the MarshalDescriptor for dealing with Text content
    **/
    public MarshalDescriptor getContentDescriptor() {
        return contentDescriptor;
    } //-- getContentDescriptor
   
    
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
     * Returns the ValidationRule used for validating the instances
     * of the class associated with this MarshalInfo
     * @return the ValidationRule used for validating the instances
     * of the class associated with this MarshalInfo
    **/
    public ValidationRule[] getValidationRules() {
        int size = validationRules.size();
        ValidationRule[] vrules = new ValidationRule[size];
        validationRules.copyInto(vrules);
        return vrules;
    } //-- getValidationRule
    
    /**
     * Sets the MarshalDescriptor for handling Text content
     * @param marshalDescriptor, the MarshalDescriptor for handling 
     * text content
    **/
    public void setContentDescriptor(MarshalDescriptor marshalDescriptor) {
        this.contentDescriptor = marshalDescriptor;
    } //-- setContentDescriptor
   
    
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
    
    //---------------------/
    //- Protected Methods -/
    //---------------------/
    
    /**
     * Sets the class name of the Class that this MarshalInfo describes. 
     * This value can be either the name of the class or a string expression, 
     * that when evaluated yeilds the proper class name. 
     * Please refer to MarshalExpr. 
     * <BR />
     * The expression uses a similiar syntax to XPath, and is denoted using
     * the same syntax as the W3C's XSLT 1.0 attribute value templates
     * <PRE>
     * examples:
     *   "com.exoffice.xml.Foo" is a String literal and will be used to
     *    instantiate the class com.exoffice.xml.Foo
     *
     *   "com.exoffice.xml.{@name}" will be evaluated to return a class
     *   name derived from the name attribute of the current element being
     *   unmarshalled. if {@name} evaluates to "Bar" then the class 
     *   com.exoffice.xml.Bar will be instantiated.
     * </PRE>
     * @param className the name of the Class that this MarshalInfo describes.
    **/
    protected void setClassName(String className) {
        this._className = className;
    }
    
} //-- MarshalInfo

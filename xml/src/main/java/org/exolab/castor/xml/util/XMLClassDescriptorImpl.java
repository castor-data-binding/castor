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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.castor.xml.XMLNaming;
import org.exolab.castor.mapping.AbstractFieldHandler;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.UnmarshalState;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.AbstractXMLNaming;
import org.exolab.castor.xml.location.XPathLocation;
import org.exolab.castor.xml.util.resolvers.ResolveHelpers;

/**
 * The core implementation of XMLClassDescriptor. This class is used by both
 * generated source code as well as the XMLMappingLoader.
 *
 * @author <a href="keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr
 *          2006) $
 */
public class XMLClassDescriptorImpl extends Validator implements XMLClassDescriptor {

    /**
     * The ALL compositor to signal the fields of the described class must all
     * be present and valid, if they are required.
     */
    private static final short ALL       = 0;

    /**
     * The CHOICE compositor to signal the fields of the described class must be
     * only a choice. They are mutually exclusive.
     */
    private static final short CHOICE    = 1;

    /**
     * The SEQUENCE compositor....currently is the same as ALL.
     */
    private static final short SEQUENCE  = 2;


    private static final String NULL_CLASS_ERR
    = "The Class passed as an argument to the constructor of " +
    "XMLClassDescriptorImpl may not be null.";

    private static final String WILDCARD = "*";

    /**
     * The set of attribute descriptors.
     */
    private XMLFieldDescriptors _attributes = null;

    /**
     * Cached attribute descriptors for improved performance.
     */
    private XMLFieldDescriptor[] _attArray = null;

    /**
     * The Class that this ClassDescriptor describes.
     */
    private Class _class = null;

    /**
     * A variable to keep track of the number of container fields.
     */
    private int _containerCount = 0;

    /**
     * The XMLFieldDescriptor for text data.
     */
    private XMLFieldDescriptor contentDescriptor = null;

    /**
     * The TypeValidator to use for validation of the described class.
     */
    private TypeValidator validator = null;

    /**
     * The set of element descriptors.
     */
    private XMLFieldDescriptors _elements = null;

    /**
     * Cached element descriptors for improved performance.
     */
    private XMLFieldDescriptor[] _elemArray = null;

    /**
     * The namespace prefix that is to be used when marshalling.
     */
    private String nsPrefix = null;

    /**
     * The namespace URI used for both Marshalling and Unmarshalling.
     */
    private String nsURI = null;

    /**
     * The name of the XML element.
     */
    private String  _xmlName;

    /**
     * Flag to indicate XML refers to global element or element with
     * anonymous type in XSD. Useful information for frameworks
     * that use Castor for XML binding and generate additional schema
     * definition elements.
     */
    private boolean elementDefinition = false;

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
     */
    private boolean            _introspected = false;

    private short              _compositor = ALL;
    
    /**
     * Defines the sequence of elements for unmarshalling validation
     * ( to be used with compositor == SEQUENCE only)
     */
    private List sequenceOfElements = new ArrayList();
    
    private List _substitutes = new LinkedList();    

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates an XMLClassDescriptor class used by the Marshalling Framework.
     *
     * @param type the Class type with which this ClassDescriptor describes.
     */
    public XMLClassDescriptorImpl(Class type) {
        this();
        if (type == null) {
            throw new IllegalArgumentException(NULL_CLASS_ERR);
        }

        this._class = type;
        // useless, no name is known setXMLName(null);
    } //-- XMLClassDescriptorImpl

    /**
     * Creates an XMLClassDescriptor class used by the Marshalling Framework.
     *
     * @param type the Class type with which this ClassDescriptor describes.
     */
    public XMLClassDescriptorImpl(Class type, String xmlName) {
        this();

        if (type == null) {
            throw new IllegalArgumentException(NULL_CLASS_ERR);
        }

        this._class = type;
        setXMLName(xmlName);
    } //-- XMLClassDescriptorImpl

    /**
     * Protected constructor used by this class, and subclasses only
     */
    protected XMLClassDescriptorImpl() {
        _attributes = new XMLFieldDescriptors(5);
        _elements = new XMLFieldDescriptors(7);
    } //-- XMLClassDescriptor

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Adds the given XMLFieldDescriptor to the list of descriptors. The
     * descriptor will be added to the appropriate list by calling
     * XMLFieldDescriptor#getNodeType() to determine it's type.
     *
     * @param descriptor the XMLFieldDescriptor to add
     */
    public void addFieldDescriptor(XMLFieldDescriptor descriptor) {
        addFieldDescriptor(descriptor, true);
    } //-- addFieldDescriptor

    /**
     * Returns true if the given XMLFieldDescriptor is contained
     * within this XMLClassDescriptor.
     *
     * @return true if the XMLFieldDescriptor is part of this
     * XMLClassDescriptor, otherwise false.
     */
    public boolean contains(XMLFieldDescriptor descriptor) {
        if (descriptor == null) {
            return false;
        }

        if (_attributes.contains(descriptor)) {
            return true;
        }
        if (_elements.contains(descriptor)) {
            return true;
        }

        return descriptor.equals(contentDescriptor);
    } //-- contains

    /**
     * Returns the set of XMLFieldDescriptors for all members that should be
     * marshalled as XML attributes.
     *
     * @return an array of XMLFieldDescriptors for all members that should be
     *         marshalled as XML attributes.
     */
    public XMLFieldDescriptor[]  getAttributeDescriptors() {
        return (XMLFieldDescriptor[]) getAttributeArray().clone();
    } // getAttributeDescriptors

    /**
     * Returns the XMLFieldDescriptor for the member that should be marshalled
     * as text content.
     *
     * @return the XMLFieldDescriptor for the member that should be marshalled
     *         as text content.
     */
    public XMLFieldDescriptor getContentDescriptor() {
        return contentDescriptor;
    } // getContentDescriptor

    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
     *
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
     */
    public XMLFieldDescriptor[]  getElementDescriptors() {
        return (XMLFieldDescriptor[]) getElementArray().clone();
    } // getElementDescriptors
    
    /**
     * Checks whether the given XMLFieldDescriptor is the one actually expected,
     * given the natural order as defined by a sequence definition
     * @param elementDescriptor The XML field descriptor to be checked
     * @throws ValidationException If the descriptor is not the one expected
     */
    public void checkDescriptorForCorrectOrderWithinSequence(
            final XMLFieldDescriptor elementDescriptor, UnmarshalState parentState, String xmlName) throws ValidationException {
        if (_compositor == SEQUENCE && sequenceOfElements.size() > 0) {
        	
        	if (parentState.expectedIndex == sequenceOfElements.size() ) {
        		throw new ValidationException ("Element with name " + xmlName + " passed to type " + getXMLName() + " in incorrect order; It is not allowed to be the last element of this sequence!");
        	}
        	
        	XMLFieldDescriptor expectedElementDescriptor = (XMLFieldDescriptor) sequenceOfElements.get(parentState.expectedIndex);
            
            String expectedElementName = expectedElementDescriptor.getXMLName();
            String elementName = xmlName;

            boolean anyNode = expectedElementDescriptor.getFieldName().equals("_anyObject") && expectedElementName == null;
            
            // choices
            if (!anyNode && expectedElementDescriptor.getXMLName().equals("-error-if-this-is-used-")) {
            	
            	// find possible names
            	ArrayList possibleNames = new ArrayList();
            	fillPossibleNames(possibleNames, expectedElementDescriptor);
            	
            	// check name
            	if (!possibleNames.contains(elementName)) {
            		if (!expectedElementDescriptor.isRequired()) {
            			 parentState.expectedIndex++;
                         checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
            		} else {
            			throw new ValidationException ("Element with name " + elementName + " passed to type " + getXMLName() + " in incorrect order; expected element has to be member of the expected choice.");
            		}
            	} else {
            		parentState.expectedIndex++;
            	}
                return;
            }

            // multi valued flag
            if (expectedElementDescriptor.isMultivalued() && !parentState.withinMultivaluedElement) {
            	parentState.withinMultivaluedElement = true;
            }
            
            if (!anyNode && !(expectedElementName).equals(elementName)) {
                
                // handle substitution groups !!! 
                List substitutes =  expectedElementDescriptor.getSubstitutes();
                if (substitutes != null && !substitutes.isEmpty()) {
                    if (substitutes.contains(elementName)) {
                        if (!parentState.withinMultivaluedElement) {
                            parentState.expectedIndex++;
                        }
                        return;
                    }
                }
                // handle multi-valued fields
                if (expectedElementDescriptor.isMultivalued()) {
                	parentState.withinMultivaluedElement = false;
                    parentState.expectedIndex++;
                    checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
                    return;
                }
                // handle required fields
                if (expectedElementDescriptor.isRequired()) {
                    throw new ValidationException ("Element with name " + elementName + " passed to type " + getXMLName() + " in incorrect order; expected element with name '" + expectedElementName + "' or any other optional element declared prior to it.");
                }
                
                // non required field, proceed until next required field
                parentState.expectedIndex++;
                checkDescriptorForCorrectOrderWithinSequence(elementDescriptor, parentState, xmlName);
                return;
                
            }
            
            if (!parentState.withinMultivaluedElement) {
                parentState.expectedIndex++;
            }
        }
   }
    
    
    private void fillPossibleNames(List possibleNames, XMLFieldDescriptor descriptor) {
    	XMLFieldDescriptor[] descriptors = ((XMLClassDescriptor)descriptor.getClassDescriptor()).getElementDescriptors();
    	if (descriptors.length == 0) {
    		return;
    	}
    	for (int i = 0; i < descriptors.length; i++) {
    		if ("_items".equals(descriptors[i].getFieldName() ) 
    				|| "-error-if-this-is-used-".equals(descriptors[i].getXMLName())) {
        		fillPossibleNames(possibleNames, descriptors[i]);
        	} else {
        		possibleNames.add(descriptors[i].getXMLName());
        	}
    	}
    }

    /**
     * Returns the XML field descriptor matching the given xml name and
     * nodeType. If NodeType is null, then either an AttributeDescriptor, or
     * ElementDescriptor may be returned. Null is returned if no matching
     * descriptor is available.
     * <p>
     * If an field is matched in one of the container field, it will return the
     * container field that contain the field named 'name'
     *
     * @param name the xml name to match against
     * @param nodeType the NodeType to match against, or null if the node type
     *        is not known.
     * @return the matching descriptor, or null if no matching descriptor is
     *         available.
     */
    public XMLFieldDescriptor getFieldDescriptor(String name, String namespace,
            NodeType nodeType) {
        boolean wild = ((nodeType == null) || _introspected);
        XMLFieldDescriptor result = null;

        XMLFieldDescriptor[] attributes = _attArray;
        XMLFieldDescriptor[] elements   = _elemArray;

        // TODO: clean up location patch
        String location = null;
        if (name != null) {
            int idx = name.lastIndexOf('/');
            if (idx >= 0) {
                location = name.substring(0, idx);
                name = name.substring(idx+1);
            }
        }

        if (wild || (nodeType == NodeType.Element)) {

            if (elements == null) elements = getElementArray();

//            if (_compositor == SEQUENCE && sequenceOfElements.size() > 0) {
//                XMLFieldDescriptor elementDescriptor = (XMLFieldDescriptor) sequenceOfElements.get(sequenceElementCount);
//                String elementName = elementDescriptor.getXMLName();
//                if (!elementName.equals(name)) {
//                    throw new IllegalArgumentException ("Element with name " + name + " passed to type " + getXMLName() + " in incorrect order; expected TODO.");
//                } else {
//                    sequenceElementCount++;
//                }
//            }
            
            for (int i = 0; i < elements.length; i++) {
                XMLFieldDescriptor desc = elements[i];
                
                if (desc == null) continue;

                if (location != null) {
                    if (!location.equals(desc.getLocationPath()))
                        continue;
                }

                if (desc.matches(name)) {
                      if (!desc.matches(WILDCARD)) {
                          // currently, a 'null' namespace value indicates that the XML artifact in question
                          // either is namespace-less or is part of the default namespace; in both cases, 
                          // we currently can not perform namespace comparison.
                          // TODO[wguttmn, 20071205]: add code to handle default namespace declarations rather than checking for null
                          if (namespace == null 
                                  || ResolveHelpers.namespaceEquals(namespace, desc.getNameSpaceURI())) {
                              return desc;
                          }
                      } else {
                          //--  possible wildcard match check for
                          //--  exact match (we need to extend the
                          //--  the descriptor interface to handle this
                          if (name.equals(desc.getXMLName()))
                              return desc;
                          //-- assume wild-card match
                          result = desc;
                      }
                }

                //handle container
                if ( desc.isContainer() ) {
                    XMLClassDescriptor xcd = (XMLClassDescriptor)desc.getClassDescriptor();
                    //prevent endless loop
                    if (xcd != this) {
                        //is it in this class descriptor?
                        if (xcd.getFieldDescriptor(name, namespace, NodeType.Element) != null) {
                            result = desc;
                            break;
                        }
                    }
                }//container
            }

            if (result != null)
                return result;
        }

        //-- handle attributes
        if (wild || (nodeType == NodeType.Attribute))
        {
            if (attributes == null) attributes = getAttributeArray();
            for (int i = 0; i < attributes.length; i++) {
                XMLFieldDescriptor desc = attributes[i];
                if (desc == null) continue;
                if (desc.matches(name)) {
                    return desc;
                }
            }
        }

        //-- handle namespace node
        if (nodeType == NodeType.Namespace) {
            if (attributes == null) attributes = getAttributeArray();
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i] == null) continue;
                if (attributes[i].getNodeType() == NodeType.Namespace) {
                    return attributes[i];
                }
            }
        }

        // To handle container object, we need to check if an attribute of a
        // container field match this attribute
        if (nodeType == NodeType.Attribute) {
            if (elements == null) elements = getElementArray();
            for (int i = 0; i < elements.length; i++) {
                XMLFieldDescriptor desc = elements[i];
                if (desc.isContainer()) {
                    XMLClassDescriptor xcd = (XMLClassDescriptor)desc.getClassDescriptor();
                    //prevent endless loop
                    if (xcd != this) {
                        //is it in this class descriptor?
                        XMLFieldDescriptor temp = xcd.getFieldDescriptor(name, namespace, NodeType.Attribute);
                        if (temp != null) {
                            return desc;
                        }
                    }
                }
            }
        }

        return null;

    } //-- getFieldDescriptor


    /**
     * @return the namespace prefix to use when marshalling as XML.
     */
    public String getNameSpacePrefix() {
        return nsPrefix;
    } //-- getNameSpacePrefix

    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
     */
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
     */
    public TypeValidator getValidator() {
        if (validator != null)
            return validator;
        return this;
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
     * Returns true if XML schema definition of this Class is that of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition() {
            return elementDefinition;
    } //-- isElementDefinition

    /**
     * Returns true if this XMLClassDescriptorImpl has any fields which are
     * container objects. A container object is a Java object which holds
     * data the should be marshalled, but the object itself should not be.
     * So the container object will be "unwrapped" and the fields
     * associated with the container will appear as if they were part
     * of this class.
     *
     * @return true if any of the fields are container fields, otherwise false.
     */
    public boolean hasContainerFields() {
        return (_containerCount > 0);
    } //-- hasContainerFields

    /**
     * Removes the given XMLFieldDescriptor from the list of descriptors.
     *
     * @param descriptor the XMLFieldDescriptor to remove
     * @return true if the descriptor was removed.
     */
    public boolean removeFieldDescriptor(XMLFieldDescriptor descriptor) {

        if (descriptor == null) return false;

        boolean removed = false;
        NodeType nodeType = descriptor.getNodeType();
        switch(nodeType.getType()) {
            case NodeType.NAMESPACE:
            case NodeType.ATTRIBUTE:
                if (_attributes.remove(descriptor)) {
                    removed = true;
                    _attArray = null;
                }
                break;
            case NodeType.TEXT:
                if (contentDescriptor == descriptor) {
                    contentDescriptor = null;
                    removed = true;
                }
                break;
            default:
                if (_elements.remove(descriptor)) {
                    _elemArray = null;
                    removed = true;
                    if (descriptor.isContainer()) --_containerCount;
                }
                break;
        }
        return removed;
    } //-- removeFieldDescriptor

    /**
     * Sets the compositor for the fields of the described
     * class to be ALL.
     */
    public void setCompositorAsAll() {
        _compositor = ALL;
    }  //-- setCompositorAsAll

    /**
     * Sets the compositor for the fields of the described
     * class to be CHOICE.
     */
    public void setCompositorAsChoice() {
        _compositor = CHOICE;
    }  //-- setCompositorAsChoice

    /**
     * Sets the compositor for the fields of the described
     * class to be a Sequence.
     */
    public void setCompositorAsSequence() {
        _compositor = SEQUENCE;
    }  //-- setCompositorAsSequence

    /**
     * Sets the XMLClassDescriptor that this descriptor inherits from
     * @param classDesc the XMLClassDescriptor that this descriptor
     * extends
     */
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
                addFieldDescriptor((XMLFieldDescriptor)fields[i], false);
            }
        }

    } //-- setExtends

    /**
     * Sets the Identity FieldDescriptor, if the FieldDescriptor is
     * not already a contained in this ClassDescriptor, it will be
     * added
     */
    public void setIdentity(XMLFieldDescriptor fieldDesc) {
        if (fieldDesc != null) {
            if ( (! _attributes.contains(fieldDesc)) &&
                (! _elements.contains(fieldDesc))) {
                addFieldDescriptor(fieldDesc);
            }
        }
        this._identity = fieldDesc;
    } //-- setIdentity

    /**
     * Sets the namespace prefix used when marshalling as XML.
     *
     * @param nsPrefix the namespace prefix used when marshalling
     * the "described" object
     */
    public void setNameSpacePrefix(String nsPrefix) {
        this.nsPrefix = nsPrefix;
    } //-- setNameSpacePrefix

    /**
     * Sets the namespace URI used when marshalling and unmarshalling as XML.
     *
     * @param nsURI the namespace URI used when marshalling and
     * unmarshalling the "described" Object.
     */
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
     */
    //public void setValidator(TypeValidator validator) {
    //    this.validator = validator;
    //} //-- setValidator

    /**
     * Sets the XML name for the Class described by this XMLClassDescriptor
     *
     * @param xmlName the XML name for the Class described by this
     * XMLClassDescriptor
     */
    public void setXMLName(String xmlName) {
        if (xmlName == null) {
            if (_xmlName == null && _class != null) {
                _xmlName = _class.getName();
            }
        }
        else this._xmlName = xmlName;
    } //-- setXMLName

    /**
     * Set elementDefinition to true to indicate Class XML schema
     * definition is a global element or element with anonymous type.
     *
     * @param elementDefinition flag to indicate XML definition is
     * global element or element with anonymous type
     */
    public void setElementDefinition(boolean elementDefinition){
            this.elementDefinition = elementDefinition;
    } //-- setElementDefinition

    /**
     * This method is used to keep the set of descriptors in the proper
     * sorted lists. If you dynamically change the NodeType of
     * an XMLFieldDescriptor after adding it the this ClassDescriptor,
     * then call this method.
     */
    public void sortDescriptors() {

        //-- handle attributes
        XMLFieldDescriptor[] descriptors = getAttributeArray();
        for (int i = 0; i < descriptors.length; i++) {
            XMLFieldDescriptor fieldDesc = descriptors[i];
            switch (fieldDesc.getNodeType().getType()) {
                case NodeType.ELEMENT:
                    _elements.add(fieldDesc);
                    _attributes.remove(fieldDesc);
                    _attArray = null;
                    break;
                case NodeType.TEXT:
                    _attributes.remove(fieldDesc);
                    _attArray = null;
                    break;
                default:
                    break;
            }
        }

        //-- handle elements
        descriptors = getElementArray();
        for (int i = 0; i < descriptors.length; i++) {
            XMLFieldDescriptor fieldDesc = descriptors[i];
            switch (fieldDesc.getNodeType().getType()) {
                case NodeType.NAMESPACE:
                case NodeType.ATTRIBUTE:
                    _attributes.add(fieldDesc);
                    _elements.remove(fieldDesc);
                    _elemArray = null;
                    break;
                case NodeType.TEXT:
                    _elements.remove(fieldDesc);
                    _elemArray = null;
                    break;
                default:
                    break;
            }
        }

    } //-- sortDescriptors

    /**
     * Returns the String representation of this XMLClassDescriptor
     *
     * @return the String representation of this XMLClassDescriptor
     */
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

    /**
     * Validates the given Object
     *
     * @param object the Object to validate
     */
    public void validate(Object object)
        throws ValidationException
    {
        validate(object, (ValidationContext)null);
    } //-- validate

    /**
     * Validates the given object
     * @param object the Object to validate
     * @param context the ValidationContext
     */
    public void validate(Object object, ValidationContext context)
    throws ValidationException {
        if (object == null) {
            throw new ValidationException("Cannot validate a null object.");
        }
        Class a = getJavaClass();
        ClassLoader acl = a.getClassLoader();
        Class b = object.getClass();
        ClassLoader bcl = b.getClassLoader();
        if (!getJavaClass().isAssignableFrom(object.getClass())) {
            String err = "The given object is not an instance of the class"
                + " described by this ClassDecriptor.";
            throw new ValidationException(err);
        }

        //-- DEBUG
        //System.out.println("Validating class: " + object.getClass().getName());
        //-- /DEBUG

        XMLFieldDescriptor[] localElements = getElementArray();
        XMLFieldDescriptor[] localAttributes = getAttributeArray();

        if (_extends != null) {

            //-- cascade call for validation
            if (_extends instanceof XMLClassDescriptorImpl) {
                ((XMLClassDescriptorImpl) _extends).validate(object, context);
            } else {
                TypeValidator baseValidator = _extends.getValidator();
                if (baseValidator != null) {
                    baseValidator.validate(object, context);
                }
            }

            //-- get local element descriptors by filtering out inherited ones
            XMLFieldDescriptor[] inheritedElements   = _extends.getElementDescriptors();
            XMLFieldDescriptor[] allElements = localElements;
            localElements = new XMLFieldDescriptor[allElements.length - inheritedElements.length];
            int localIdx = 0;
            for (int i = 0; i < allElements.length; i++) {
                XMLFieldDescriptor desc = allElements[i];
                boolean isInherited = false;
                for (int idx = 0; idx < inheritedElements.length; idx++) {
                    if (inheritedElements[idx].equals(desc)) {
                        isInherited = true;
                        break;
                    }
                }
                if (!isInherited) {
                    localElements[localIdx] = desc;
                    ++localIdx;
                }
            }

            //-- get local attribute descriptors by filtering out inherited ones
            XMLFieldDescriptor[] inheritedAttributes = _extends.getAttributeDescriptors();
            XMLFieldDescriptor[] allAttributes = localAttributes;
            localAttributes = 
                new XMLFieldDescriptor[allAttributes.length - inheritedAttributes.length];
            localIdx = 0;
            for (int i = 0; i < allAttributes.length; i++) {
                XMLFieldDescriptor desc = allAttributes[i];
                boolean isInherited = false;
                for (int idx = 0; idx < inheritedAttributes.length; idx++) {
                    if (inheritedAttributes[idx].equals(desc)) {
                        isInherited = true;
                        break;
                    }
                }
                if (!isInherited) {
                    localAttributes[localIdx] = desc;
                    ++localIdx;
                }
            }

        }

        switch (_compositor) {

            case CHOICE:

                boolean found = false;
                boolean hasLocalDescs = (localElements.length > 0);
                XMLFieldDescriptor fieldDesc = null;
                //-- handle elements, affected by choice
                for (int i = 0; i < localElements.length; i++) {
                    XMLFieldDescriptor desc = localElements[i];

                    if (desc == null) {
                        continue;
                    }

                    FieldHandler handler = desc.getHandler();

                    if (handler.getValue(object) != null) {
                         //Special case if we have a Vector, an ArrayList
                         //or an Array --> need to check if it is not empty
                         if (desc.isMultivalued()) {
                             Object temp = handler.getValue(object);
                             //-- optimize this?
                             if (Array.getLength(temp) == 0) {
                                  temp = null;
                                  continue;
                             }
                             temp = null;
                         }

                        if (found) {
                            String err = null;
                            if (desc.isContainer()) {
                                err = "The group '" + desc.getFieldName();
                                err += "' cannot exist at the same time that ";
                                if (fieldDesc.isContainer()) {
                                    err += "the group '" + fieldDesc.getFieldName();
                                } else {
                                    err += "the element '" + fieldDesc.getXMLName();
                                }
                                err += "' also exists.";
                            } else {
                                 err = "The element '" + desc.getXMLName();
                                 err += "' cannot exist at the same time that ";
                                 err += "element '" + fieldDesc.getXMLName() + "' also exists.";
                            }
                            throw new ValidationException(err);
                        }
                        found = true;
                        fieldDesc = desc;

                        FieldValidator fieldValidator = desc.getValidator();
                        if (fieldValidator != null) {
                            fieldValidator.validate(object, context);
                        }
                    }
                }

                // if all elements are mandatory, print the grammar that the choice 
                // must match.
                if ((!found) && (hasLocalDescs))  {
                    StringBuffer buffer = new StringBuffer(40);
                    boolean existsOptionalElement = false;
                    buffer.append('(');
                    String sep = " | ";
                    for (int i = 0; i < localElements.length; i++) {
                        XMLFieldDescriptor  desc = localElements[i];
                        if (desc == null) {
                            continue;
                        }

                        FieldValidator fieldValidator = desc.getValidator();
                        if (fieldValidator.getMinOccurs() == 0) {
                            existsOptionalElement = true;
                            break;
                        }
                        buffer.append(sep);
                        buffer.append(desc.getXMLName());
                    }
                    buffer.append(')');
                    if (!existsOptionalElement) {
                        String err = "In the choice contained in <" + this.getXMLName()
                                     + ">, at least one of these elements must appear:\n"
                                     + buffer.toString();
                        throw new ValidationException(err);
                    }
                }
                //-- handle attributes, not affected by choice
                for (int i = 0; i < localAttributes.length; i++) {
                    validateField(object, context, localAttributes[i]);
                }
                //-- handle content, not affected by choice
                if (contentDescriptor != null) {
                    validateField(object, context, contentDescriptor);
                }
                break;
            //-- Currently SEQUENCE is handled the same as all
            case SEQUENCE:
            //-- ALL
            default:


                //-- handle elements
                for (int i = 0; i < localElements.length; i++) {
                    final XMLFieldDescriptor fieldDescriptor = localElements[i];
                    if (fieldDescriptor == null) {
                        continue;
                    }
                    validateField(object, context, fieldDescriptor);
                }
                //-- handle attributes
                // for (int i = 0; i < _attributes.size(); i++) {
                for (int i = 0; i < localAttributes.length; i++) {
                    validateField(object, context, localAttributes[i]);
                }
                //-- handle content
                if (contentDescriptor != null) {
                    validateField(object, context, contentDescriptor);
                }
                break;
        }

    } //-- validate

    /**
     * Validates agiven field of an object, as described by its {@link XMLFieldDescriptor}
     * instance.
     * @param object The parent object, whose field to validate.
     * @param context The current {@link ValidationContext} instance.
     * @param fieldDescriptor The {@link XMLFieldDescriptor} instance describing the 
     *          field to validate.
     * @throws ValidationException If validation did report a problem.
     */
    private void validateField(final Object object, final ValidationContext context,
            final XMLFieldDescriptor fieldDescriptor) throws ValidationException {
        FieldValidator fieldValidator = fieldDescriptor.getValidator();
        if (fieldValidator != null) {
            try {
                fieldValidator.validate(object, context);
            } catch (ValidationException e) {
                if (fieldDescriptor.getNodeType() == NodeType.Attribute 
                        || fieldDescriptor.getNodeType() == NodeType.Element) {
                    addLocationInformation(fieldDescriptor, e);
                }
                throw e;
            }
        }
    }

    /**
     * Adds location information to the {@link ValidationException} instance.
     * @param localElement The {@link XMLFieldDescriptor} instance whose has been responsible for
     * generating the error.
     * @param e The {@link ValidationException} to enrich.
     */
    private void addLocationInformation(final XMLFieldDescriptor localElement,
            final ValidationException e) {
        XPathLocation loc = (XPathLocation) e.getLocation();
        if (loc == null) {
            loc = new XPathLocation();
            e.setLocation(loc);
            if (localElement.getNodeType() == NodeType.Attribute) {
                loc.addAttribute(localElement.getXMLName());
            } else {
                loc.addChild(localElement.getXMLName());
            }
        }
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
        return _class;
    } //-- getJavaClass


    /**
     * Returns a list of fields represented by this descriptor.
     *
     * @return A list of fields
     */
    public FieldDescriptor[] getFields() {
        int size = _attributes.size();
        size += _elements.size();
        if (contentDescriptor != null) ++size;


        XMLFieldDescriptor[] fields = new XMLFieldDescriptor[size];

        _attributes.toArray(fields);
        _elements.toArray(fields, _attributes.size());

        if (contentDescriptor != null) {
            fields[size-1] = contentDescriptor;
        }
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

    /**
     * @see org.exolab.castor.xml.XMLClassDescriptor#canAccept(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public boolean canAccept(String name, String namespace, Object object) {

        boolean result = false;
        boolean hasValue = false;
        XMLFieldDescriptor[] fields = null;
        int i = 0;
        //1--direct look up for a field
        XMLFieldDescriptor fieldDesc = this.getFieldDescriptor(name, namespace, NodeType.Element);
        if (fieldDesc == null)
           fieldDesc = this.getFieldDescriptor(name, namespace, NodeType.Attribute);

        //if the descriptor is still null, the field can't be in stored in this classDescriptor
        if (fieldDesc == null)
            return false;

        Object tempObject;
        //3--The descriptor is multivalued
        if (fieldDesc.isMultivalued()) {
            //-- check size
            FieldValidator validator = fieldDesc.getValidator();
            if (validator != null) {
                if (validator.getMaxOccurs() < 0) {
                   result = true;
                }
                 else {
                    // count current objects and add 1
                    tempObject = fieldDesc.getHandler().getValue(object);
                    int current = Array.getLength(tempObject);
                    int newTotal = current + 1;
                    result = (newTotal <= validator.getMaxOccurs());
                 }
            }
            else {
                //-- not created by source generator...assume unbounded
                result = true;
            }
        }
        //3-- the fieldDescriptor is a container
        else if (fieldDesc.isContainer()) {
            tempObject = fieldDesc.getHandler().getValue(object);
            //if the object is not yet instantiated, we return true
            if (tempObject == null)
                result = true;
            else
                result = ((XMLClassDescriptor)fieldDesc.getClassDescriptor()).canAccept(name, namespace, tempObject);
        }
        //4-- Check if the value is set or not
        else {
            FieldHandler handler = fieldDesc.getHandler();

            boolean checkPrimitiveValue = true;
            if (handler instanceof AbstractFieldHandler) {
                hasValue = ((AbstractFieldHandler)handler).hasValue( object );
                //-- poor man's check for a generated handler, since
                //-- all generated handlers extend XMLFieldHandler, however
                //-- this doesn't guarantee that that handler is indeed
                //-- a generated handler, it does however mean that
                //-- the handler definately didn't come from the
                //-- MappingLoader.
                //checkPrimitiveValue = (!(handler instanceof XMLFieldHandler));
            }
            else {
                hasValue = (handler.getValue(object) != null);
            }

            //-- patch for primitives, we should check
            //-- for the has-method somehow...but this
            //-- is good enough for now. This may break
            //-- some non-Castor-generated code with
            //-- primitive values that have been set
            //-- with the same as the defaults
            if (hasValue &&
                checkPrimitiveValue &&
                fieldDesc.getFieldType().isPrimitive())
            {
                if (isDefaultPrimitiveValue(handler.getValue( object ))) {
                    hasValue = false;
                }
            }
            //-- end patch
            result = !hasValue;
        }

        //5--if there is no value and the _compositor is CHOICE
        //--we have to check to see if another value has not been set
        if (result && (_compositor == CHOICE)
            && (fieldDesc.getNodeType() == NodeType.Element) ) {
            fields = getElementArray();
            i = 0;
            while (result && i<fields.length) {
                XMLFieldDescriptor desc = fields[i];
                if (desc != fieldDesc && (object!=null) ) {
                    Object tempObj = desc.getHandler().getValue(object);
                    hasValue = (tempObj != null);
                    if (hasValue) {
                        result = false;
                        //special case for array
                        if (tempObj.getClass().isArray()) {
                            result =  Array.getLength(tempObj) == 0;
                        }
                        //special case for collection
                        if (tempObj instanceof Collection) {
                           result = ((Collection)tempObj).isEmpty();
                        }
                    }
                }
                i++;
            }//while
        }//CHOICE
        return result;
    }//--canAccept

    //---------------------/
    //- Protected Methods -/
    //---------------------/


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

        //-- primtive wrapper classes
        if ((type == Boolean.class) || (type == Character.class))
            return true;

        return (type.getSuperclass() == Number.class);
    } //-- isPrimitive

    /**
     * Checks to see if the given Object is a java primitive
     * (does not check for primitive wrappers) and has a
     * value that is equal to the default value for that
     * primitive. This method will return true if the value
     * is a java primitive with a default value.
     *
     * @return true if the value is a java primitive with
     * a default value
     */
    static boolean isDefaultPrimitiveValue(Object value) {
        if (value == null) return false;

        Class type = value.getClass();
        if (type.isPrimitive()) {
            try {
                return (value.equals(type.newInstance()));
            }
            catch(java.lang.IllegalAccessException iax) {
                //-- Just return false, we should be
                //-- able to instantiate primitive types
            }
            catch(java.lang.InstantiationException ix) {
                //-- Just return false, we should be
                //-- able to instantiate primitive types
            }
        }
        else if (type.getSuperclass() == Number.class) {
            return ((Number)value).intValue() == 0;
        }
        else if (type == Boolean.class) {
            return value.equals(Boolean.FALSE);
        }
        else if (type == Character.class) {
            return ((Character)value).charValue() == '\0';
        }

        return false;
    } //-- isDefaultPrimitiveValue


    /**
     * Sets the Class type being described by this descriptor.
     *
     * @param type the Class type being described
     */
    public void setJavaClass(Class type) {
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
     */
    protected void setIntrospected(boolean introspected) {
        this._introspected = introspected;
    } //-- setIntrospected

//    protected String toXMLName(String className) {
//        //-- create default XML name
//        String name = className;
//        int idx = name.lastIndexOf('.');
//        if (idx >= 0) name = name.substring(idx+1);
//        return _naming.toXMLName(name);
//    }

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Adds the given XMLFieldDescriptor to the list of descriptors. The
     * descriptor will be added to the appropriate list by calling
     * XMLFieldDescriptor#getNodeType() to determine it's type.
     *
     * @param descriptor the XMLFieldDescriptor to add
     */
    private void addFieldDescriptor(XMLFieldDescriptor descriptor, boolean relink) {

        if (descriptor == null) return;

        boolean added = false;

        NodeType nodeType = descriptor.getNodeType();
        switch(nodeType.getType()) {
            case NodeType.NAMESPACE:
            case NodeType.ATTRIBUTE:
                added = _attributes.add(descriptor);
                if (added) {
                    _attArray = null;
                }
                break;
            case NodeType.TEXT:
                contentDescriptor = descriptor;
                added = true;
                break;
            default:
                added = _elements.add(descriptor);
                if (added) {
                    _elemArray = null;
                    if (descriptor.isContainer()) ++_containerCount;
                }
                break;
        }

        if ((added) && (relink)) {
            descriptor.setContainingClassDescriptor( this );
        }

    } //-- addFieldDescriptor


    private XMLFieldDescriptor[] getAttributeArray() {
        //-- create local reference to prevent possible
        //-- null pointer (_attArray could be re-set to null)
        //-- in multi-threaded environment
        XMLFieldDescriptor[] descriptors = _attArray;
        if (descriptors == null) {
            descriptors = _attributes.toArray();
            _attArray = descriptors;
        }
        return descriptors;
    }

    private XMLFieldDescriptor[] getElementArray() {
        //-- create local reference to prevent possible
        //-- null pointer (_elemArray could be re-set to null)
        //-- in multi-threaded environment
        XMLFieldDescriptor[] descriptors = _elemArray;
        if (descriptors == null) {
            descriptors = _elements.toArray();
            _elemArray = descriptors;
        }
        return descriptors;
    }
    
    /**
     * Adds a XMLFieldDescriptor instance to the internally maintained
     * list of sequence elements.
     * @param element An {@link XMLFieldDescriptor} instance for an element definition.
     */
    protected void addSequenceElement(XMLFieldDescriptor element) {
        sequenceOfElements.add(element);
    }

    
    public List getSubstitutes()
    {
        return _substitutes;
    }

    public void setSubstitutes(List substitutes) {
        _substitutes = substitutes;
    }
         
    public boolean isChoice() {
        return this._compositor == CHOICE;
    }
} //-- XMLClassDescriptor

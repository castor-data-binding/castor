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
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */


package org.exolab.castor.xml.util;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.handlers.DateFieldHandler;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Properties;

/**
 * XML field descriptor. Wraps {@link FieldDescriptor} and adds
 * XML-related information, type conversion, etc.
 *
 * @author <a href="keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class XMLFieldDescriptorImpl
    implements XMLFieldDescriptor
{
    
    


    private static final String WILD_CARD = "*";

    private static final String NULL_CLASS_ERR
        = "The 'type' argument passed to the constructor of "
         + "XMLFieldDescriptorImpl may not be null.";

    private static final String NULL_FIELD_NAME_ERR
        = "The 'fieldName' argument passed to the constructor of "
         + "XMLFieldDescriptorImpl may not be null.";

    /**
     * The index of this field within the constructor arguments
     * Note: This field is only applicable if the field is
     * an attribute field and it's supposed to be set via the
     * constructor. A value less than zero indicates that this
     * field is not part of the constructor arguments.
     */
     private int _argIndex = -1;

    /**
     * The type class descriptor, if this field is of a type
     * known by a descriptor.
     */
    private XMLClassDescriptor  _classDescriptor;
    
    /**
     * True if the field is a container field
     */
    private boolean _container = false;
    
    /**
     * A reference to the containing ClassDescriptor
     */
    private ClassDescriptor _contClsDescriptor;

    /**
     * The Java (programmatic) name of the field being described
    **/
    private String _fieldName = null;

    /**
     * The Class type of described field
    **/
    private Class _fieldType = null;

    /**
     * The field handler for get/set field value.
     */
    private FieldHandler  _handler = null;
    
    /**
     * Cached reference to _handler if the
     * FieldHandler is a GeneralizedFieldHandler.
     */
    private GeneralizedFieldHandler  _gfhandler = null;

    /**
     * True if the field type is immutable.
     */
    private boolean _immutable = false;
    
    /**
     * Flag to indicate that objects should be added
     * to their as soon as they are created, but before they
     * are finished being populated.
     */
    private boolean _incremental = false;

    /**
     * True if the field is a reference to another Object in the hierarchy.
    **/
    public boolean _isReference = false;
    
    private boolean _isWild = false;
    
    /**
     * True if the field type is mapped in a Hashtable or Map.
    **/
    private boolean _mapped = false;
    
    private String[] _matches = null;
    
    /**
     * A flag to indicate that the Object described by this
     * descriptor is multivalued
     */
    private boolean _multivalued = false;

    /**
     * True if the field is allowed to have nil content
     */
    private boolean _nillable = false;
    
    /**
     * The node type (attribute, element, text).
     */
    private NodeType _nodeType = null;
    
    /**
     * The namespace prefix that is to be used when marshalling
     */
    private String _nsPrefix = null;

    /**
     * The namespace URI used for both marshalling and unmarshalling
     */
    private String _nsURI = null;


    /**
     * The "user-set" properties of this XMLFieldDescriptor
     */
    private Properties _properties = null;
    
    /**
     * indicates a required field when true
    **/
    public boolean _required = false;

    /**
     * The XML Schema type of this field value
     */
    private String _schemaType = null;
    
    /**
     * True if the field is transient and should not be saved/stored.
     */
    private boolean _transient = false;

    /**
     * The prefix used in case the
     * value of the field described is of type QName.
     */
    private String _qNamePrefix = null;

    /**
     * A flag which indicates the parent class' namespace
     * should be used by default
     */
    private boolean _useParentClassNamespace = false;
    
    private FieldValidator _validator = null;
    
    /**
     * The XML name of the field, this is only the local
     * name.
     *
     * @see _xmlPath
     */
    private String _xmlName    = null;
    
    /**
     * The relative XML path used when wrapping in nested elements,
     * does not include the name of the field itself.
     *
     * @see _xmlName
     */
    private String _xmlPath    = null;

    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * This a private constructor to handle common code among
     * constructors
    **/
    private XMLFieldDescriptorImpl() {
        _matches = new String[0];
    } //-- XMLFieldDescriptorImpl

    public XMLFieldDescriptorImpl
        (Class fieldType, String fieldName, String xmlName, NodeType nodeType)
    {
        this();

        if (fieldType == null)
            throw new IllegalArgumentException(NULL_CLASS_ERR);

        if (fieldName == null)
            throw new IllegalArgumentException(NULL_FIELD_NAME_ERR);

        //////////////Hack for AnyNode//////////////////
        //if the field type is an AnyNode Castor must treat it as
        //an object to avoid changes in the Marshalling framework
        if (fieldType == org.exolab.castor.types.AnyNode.class)
               _fieldType = java.lang.Object.class;
        //////////////////////////////////////////////////
        else this._fieldType  = fieldType;
        this._fieldName  = fieldName;
        this._nodeType   = nodeType;
        this._nodeType = ( nodeType == null ? NodeType.Attribute : nodeType );
        
        //-- call the setXMLName method to handle checking for full path
        setXMLName(xmlName);

    } //-- XMLFieldDescriptorImpl

    /**
     * Makes a new copy of the given XMLFieldDescriptorImpl
     *
     * @param fieldDesc The XMLFieldDescriptor to copy
     * @throws MappingException Invalid mapping information
     */
    public XMLFieldDescriptorImpl(XMLFieldDescriptor fieldDesc)
        throws MappingException
    {
        this(fieldDesc, fieldDesc.getXMLName(), fieldDesc.getNodeType(), null);
    } //-- XMLFieldDescriptorImpl

    /**
     * Construct a new field descriptor for the specified field. This is
     * an XML field descriptor wrapping a field descriptor and adding XML
     * related properties and methods.
     *
     * @param fieldDesc The field descriptor
     * @param xmlName The XML name of the field
     * @param nodeType The node type of this field
     * @throws MappingException Invalid mapping information
     */
    public XMLFieldDescriptorImpl
        ( FieldDescriptor fieldDesc, String xmlName, NodeType nodeType )
        throws MappingException
    {
        this(fieldDesc, xmlName, nodeType, null);
    } //-- XMLFieldDescriptorImpl

    /**
     * Construct a new field descriptor for the specified field. This is
     * an XML field descriptor wrapping a field descriptor and adding XML
     * related properties and methods.
     *
     * @param fieldDesc The field descriptor
     * @param xmlName The XML name of the field
     * @param nodeType The node type of this field
     * @throws MappingException Invalid mapping information
     */
    public XMLFieldDescriptorImpl
        ( FieldDescriptor fieldDesc, String xmlName, NodeType nodeType, NodeType primitiveNodeType )
        throws MappingException
    {

        this();

	    if ( fieldDesc instanceof XMLFieldDescriptor )
          this._contClsDescriptor =
              ( (XMLFieldDescriptor)fieldDesc
                ).getContainingClassDescriptor();

        setHandler(fieldDesc.getHandler());
        _fieldName            = fieldDesc.getFieldName();
         //////////////Hack for AnyNode//////////////////
        //if the field type is an AnyNode Castor must treat it as
        //an object to avoid changes in the Marshalling framework
        if (fieldDesc.getFieldType() == org.exolab.castor.types.AnyNode.class)
               _fieldType = java.lang.Object.class;
        //////////////////////////////////////////////////
        else this._fieldType  = fieldDesc.getFieldType();
        this._transient       = fieldDesc.isTransient();
        this._immutable       = fieldDesc.isImmutable();
        this._required        = fieldDesc.isRequired();
        this._multivalued     = fieldDesc.isMultivalued();

        ClassDescriptor cd    = fieldDesc.getClassDescriptor();
        if (cd != null) {
            if (cd instanceof XMLClassDescriptor)
                this._classDescriptor = (XMLClassDescriptor)cd;
            else
                this._classDescriptor = new XMLClassDescriptorAdapter(cd, null, primitiveNodeType);
        }

        //-- check for instances of java.util.Date
        /* Note to self:
         * This logic really doesn't belong here, as it can
         * interfere with user specified handlers. It should
         * go into XMLMappingLoader. 
         */
        if (_fieldType != null) {
            if (java.util.Date.class.isAssignableFrom(_fieldType)) {
                if (_handler instanceof FieldHandlerImpl) {
                    _handler = new DateFieldHandler(_handler);
                }
            }
        }

        //-- handle xml name
        if ( xmlName == null ) xmlName = getFieldName();
        //-- call the setXMLName method to handle checking for full path
        setXMLName(xmlName);

        if (nodeType == null) {
            if (_multivalued)
                _nodeType = NodeType.Element;
            else
                _nodeType = NodeType.Attribute;
        }
        else _nodeType = nodeType;

        if (_required) {
            _validator = new FieldValidator();
            _validator.setMinOccurs(1);
            _validator.setDescriptor(this);
        }
    } //-- XMLFieldDescriptorImpl

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Returns true if two XMLFieldDescriptors should be treated as
     * equal. Any XMLFieldDescriptor that handles the same field
     * is considered equal.
    **/
    public boolean equals(Object obj) {
        
        if (obj == this) return true;
        
        if ((obj == null) || (!(obj instanceof XMLFieldDescriptor)))
            return false;

        XMLFieldDescriptor descriptor = (XMLFieldDescriptor)obj;
        //-- check field names
        if (!_fieldName.equals(descriptor.getFieldName()))
            return false;
        //-- check field types
        if (!_fieldType.equals(descriptor.getFieldType()))
            return false;
        
        //-- check field handler
        FieldHandler tmpHandler = descriptor.getHandler();
        if (_handler == null) {
            return (tmpHandler == null);
        }
        else if (tmpHandler == null) {
            return false;
        }
               
        //-- the following line is causing some issues when
        //-- used against a FieldHandlerImpl because the
        //-- equals method for FieldHandlerImpl is the default
        //-- replacing with a slightly more generic comparison
        //-- but this should probably change in the future (kv)
        //return (_handler.equals(tmpHandler));
        return (_handler.getClass().isInstance(tmpHandler));
        
    } //-- equals

    /**
     * Returns the hashCode for this XMLFieldDescriptor
     */
    public int hashCode() {
        int hash = 17;
        hash = 17 * _fieldName.hashCode();
        hash = hash * 17 * _fieldType.hashCode();
        if (_handler != null)
            hash = hash * 17 * _handler.hashCode();
        return hash;
    }
    
    /**
     * Set the class which contains this field
     */
    public void setContainingClassDescriptor( ClassDescriptor contClsDesc )
    {
      _contClsDescriptor = contClsDesc;
    }

    /**
     * @return the class which contains this field
     */
    public ClassDescriptor getContainingClassDescriptor()
    {
      return _contClsDescriptor;
    }

    /**
     * Returns the class descriptor related to the field type. If the
     * field type is a class for which a descriptor exists, this
     * descriptor is returned. If the field type is a class for which
     * no mapping is provided, null is returned.
     *
     * @return The class descriptor of the field type, or null
     */
    public ClassDescriptor getClassDescriptor() {
        return _classDescriptor;
    } //-- getClassDescriptor

    /**
     * Return the collection handler of this field type. Returns null
     * if the field is not a collection.
     *
     * @return The collection handler
     */
    public CollectionHandler getCollectionHandler() {
        return null;
    } //-- getCollectionHandler

    /**
     * Returns the index within the constructor argument array where the 
     * value of this field should be. A value less than zero indicates
     * that the value of this field is set via a normal setter method
     * and not via the constructor.
     *
     * Note: This only applies to attribute mapped fields at this time.
     *
     * @return the index within the constructor argument array for 
     * this field.
     */
    public int getConstructorArgumentIndex() {
        return _argIndex;
    } //-- getConstructorArgumentIndex

    /**
     * Returns the name of the field.
     *
     * @return Field name
     */
    public String getFieldName() {
        return _fieldName;
    } //-- getFieldName

    /**
     * Returns the Java type of the field.
     *
     * @return Field type
     */
    public Class getFieldType() {
        if (_gfhandler != null) {
            if (_gfhandler.getFieldType() != null) {
            	return _gfhandler.getFieldType();
            }
        }
        return _fieldType;
    } //-- getFieldType

    /**
     * Returns the handler of the field. In order to persist or marshal
     * a field descriptor will be associated with a handler.
     *
     * @return The field handler
     */
    public FieldHandler getHandler() {
        return _handler;
    } //-- getHandler


    /**
     * Returns the XML Name for the field being described.
     *
     * @return the XML name.
    **/
    public String getXMLName() {
        return _xmlName;
    } //-- getXMLName

    /**
     * Return the "suggested" namespace prefix to use when marshalling
     * as XML.
     *
     * @return the "suggested" namespace prefix.
    **/
    public String getNameSpacePrefix() {
        return _nsPrefix;
    } //-- getNameSpacePrefix

    /**
     * Returns the namespace URI to be used when marshalling and
     * unmarshalling as XML.
     *
     * @return the namespace URI.
    **/
    public String getNameSpaceURI() {

        if ((_nsURI == null) && 
            (_contClsDescriptor != null) &&
            _useParentClassNamespace)
        {
            if (isPrimitive(_fieldType) && (_nodeType == NodeType.Element))
            {
                if (_contClsDescriptor instanceof XMLClassDescriptor) {
                    return ((XMLClassDescriptor)_contClsDescriptor).getNameSpaceURI();
                }
            }
        }
        return _nsURI;
    } //-- getNameSpaceURI


    public NodeType getNodeType()
    {
        return _nodeType;
    }

    /**
     * Returns the "relative" XML path for the field being described.
     *
     * In most cases, this will be null. However sometimes a
     * field may be mapped to a nested element. In which case 
     * the value returned by this method should be the nested
     * element name. If more than one level of nesting is
     * needed each nested element name should be separated by
     * by a path separator (forward slash '/').
     *
     * The location path name is "relative" to the parent Class. The
     * name of the parent should not be included in the path.
     *
     * 
     * For example, give the following two classes:
     * <code>
     *
     *    class Root {    
     *        Bar bar;    
     *    }
     *
     *    class Bar {
     *       String value;
     *    }
     * </code>
     *
     * And the following XML:
     *
     * <code>
     *    &lt;root&gt;
     *       &lt;foo&gt;
     *          &lt;bar&gt; value of bar &lt;/bar&gt;
     *       &lt;/foo&gt;
     *    &lt;/root&gt;
     * </code>
     *
     * Since foo has no associated class, the path for 'bar'
     * would be: "foo"
     * 
     * 
     * @returns the "relative" XML path for the field being described.
     */
    public String getLocationPath() {
        return _xmlPath;
    } //-- getLocationPath
    
    
    /**     
     * Returns the value property with the given name or null
     * if no such property exists. This method is useful for
     * future evolutions of this interface as well as for
     * user-defined extensions. See class declared properties
     * for built-in properties.
     *
     * @param propertyName the name of the property whose value
     * should be returned.
     *
     * @return the value of the property, or null.
     */
    public String getProperty(String propertyName) {
        if ((_properties == null) || (propertyName == null))
            return null;
        return _properties.getProperty(propertyName);
    } //-- getProperty
    
    /**
     * Returns the prefix used in case the value of the
     * field described by this descriptor is of type QName.
     * This is helpful for the Marshaller but not mandatory.
     *
     * @return the prefix used in the QName value.
     */
    public String getQNamePrefix() {
        return _qNamePrefix;
    }

    /**
     * Returns a specific validator for the field described by
     * this descriptor. A null value may be returned
     * if no specific validator exists.
     *
     * @return the type validator for the described field
    **/
    public FieldValidator getValidator() {
        return _validator;
    } //-- getValidator

    /**
     * Returns the XML Schema type of the value
     * of the field described by this descriptor.
     */
     public String getSchemaType() {
         return _schemaType;
     }

    /**
     * Returns true if the value of the field represented by this 
     * descriptor should be set via the constructor of the containing
     * class. This is only valid for attribute mapped fields.
     *
     * @return true if the value of the field represented by this 
     * descriptor should be set via the constructor of the containing
     * class.
     */
    public boolean isConstructorArgument() {
        return (_argIndex >= 0);
    }
    
    /**
     * Returns true if the field described by this descriptor is a container
     * field. A container is a field that should is not a first-class object,
     * and should therefore have no XML representation. 
     *
     * @return true if the field is a container
     */
    public boolean isContainer() {
        return _container;
    } //-- isContainer
    
    /**
     * Returns true if the field type is immutable.
     *
     * @return True if the field type is immutable
     */
    public boolean isImmutable() {
        return _immutable;
    } //-- isImmutable

    /**
     * Returns the incremental flag which when true indicates that this
     * member may be safely added before the unmarshaller is finished
     * unmarshalling it.
     * @return true if the Object can safely be added before the unmarshaller
     * is finished unmarshalling the Object.
    **/
    public boolean isIncremental() {
        return _incremental;
    } //-- isIncremental

    /**
     * Returns true if the field described by this descriptor
     * is Map or Hashtable. If this method returns true, it
     * must also return true for any call to #isMultivalued.
     * 
     * @return true if the field described by this desciptor is
     * a Map or Hashtable, otherwise false.
    **/
    public boolean isMapped() {
        return _mapped;
    } //-- isMapped

    /**
     * Returns true if the Object described by this descriptor can
     * contain more than one value
     * @return true if the Object described by this descriptor can
     * contain more than one value
    **/
    public boolean isMultivalued() {
        return _multivalued;
    } //-- isMultivalued

    /*
     *  (non-Javadoc)
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isNillable()
     */
    public boolean isNillable() {
    	return _nillable;
    }
    /**
     * Returns true if the field described by this descriptor is
     * a reference (ie. IDREF) to another object in the
     * "Object Model" (XML tree)
    **/
    public boolean isReference() {
        return this._isReference;
    } //-- isReference

    /**
     * Returns true if the field described by this descriptor is a required
     * field
     * @return true if the field described by this descriptor is a required
     * field
    **/
    public boolean isRequired() {
        return _required;
    } //-- isRequired

    /**
     * Returns true if the field is transient. Transient fields are
     * never persisted or marshalled.
     *
     * @return True if transient field
     */
    public boolean isTransient() {
        return _transient;
    } //-- isTransient


    /**
     * Set if the field is a container field or not.
     *
     * @param isContainer a boolean indicating whether or not the field
     * is a container field.
     */
    public void setContainer(boolean isContainer) {
        _container = isContainer;
    } //-- setContainer

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
            if (_isWild) return true;
            else if (_matches.length > 0) {
                for (int i = 0; i < _matches.length; i++) {
                    if (xmlName.equals( _matches[i] ) )
                        return true;
                }
            }
            else
                return xmlName.equals(this._xmlName);
        }

        return false;
    } //-- matches
    
    /**
     * Returns true if this descriptor can be used to handle elements
     * or attributes with the given XML name. By default this method
     * simply compares the given XML name with the internal XML name.
     * This method can be overridden to provide more complex matching.
     * @param xmlName the XML name to compare
     * @return true if this descriptor can be used to handle elements
     * or attributes with the given XML name.
     */
    public boolean matches(String xmlName, String namespace) {

        //-- compare namespaces
        if (namespace == null) {
            if ((_nsURI != null) && (_nsURI.length() > 0))
                return false;
        }
        else if (_nsURI == null) {
            if ((namespace.length() > 0) && (!_isWild)) {
                return false;
            }
        }
        else if (!_nsURI.equals(namespace)) {
            return false;
        }
        
        //-- if we make this far the namespaces
        //-- match, now compare names
        if (xmlName != null) {
            if (_isWild) return true;
            else if (_matches.length > 0) {
                for (int i = 0; i < _matches.length; i++) {
                    if (xmlName.equals( _matches[i] ) )
                        return true;
                }
            }
            else
                return xmlName.equals(this._xmlName);
        }

        return false;
    } //-- matches

    /**
     * Sets the XMLClassDescriptor for the described field
     *
     * @param classDescriptor the XMLClassDescriptor for the described field.
     */
    public void setClassDescriptor(XMLClassDescriptor classDescriptor) {
        this._classDescriptor = classDescriptor;
    } //-- setClassDescriptor

    /**
     * Sets whether or not the value of the field represented by this
     * FieldDescriptor should be set via the constructor of the containing
     * ClassDescriptor. The index value greater than 0 specifies the index
     * within the argument array that the value of this field should be.
     *
     * Note: This only applies to attribute mapped fields at this time.
     *
     * @param index the index within the argument array. A value less
     * than zero indicates that this field should not be part of the
     * constructor arguments.
     */
    public void setConstructorArgumentIndex(int index) {
        if (_nodeType != NodeType.Attribute) {
            String err = "constructor arguments are only valid for " +
                "attribute mapped fields.";
            throw new IllegalStateException(err);
        }
        _argIndex = index;
    } //-- setConstructorArgumentIndex

    /**
     * Sets the FieldHandler for the field being described
     * by this FieldDescriptor
     *
     * @param handler the FieldHandler for the field being described
     * by this FieldDescriptor
    **/
    public void setHandler(FieldHandler handler) {
        _handler = handler;
        if (handler instanceof GeneralizedFieldHandler) {
        	_gfhandler = (GeneralizedFieldHandler)handler;
        }
        else {
        	_gfhandler = null;
        }
    } //-- setHandler

    /**
     * Sets the incremental flag which indicates whether this member
     * can be added before the unmarshaller is finished unmarshalling it.
     * @param incremental the boolean which if true indicated that this
     * member can safely be added before the unmarshaller is finished
     * unmarshalling it.
    **/
    public void setIncremental(boolean incremental) {
        this._incremental = incremental;
    } //-- setIncremental

    /**
     * Sets the immutable flag which indicates that changes
     * to this Field result in a new Object to be created, such
     * as java.lang.String. It serves to identify fields which
     * should not be constructed...until after all the data is
     * available.
     * @param immutable the boolean which if true indicated that this
     * described field is immutable
    **/
    public void setImmutable(boolean immutable) {
        this._immutable = immutable;
    } //-- setImmutable

    /**
     * Sets the location path for the field being described.
     *
     * In most cases, this isn't needed. However sometimes a
     * field may be mapped to a nested element. In which case 
     * the value of the location path should be the nested
     * element name. If more than one level of nesting is
     * needed each nested element name should be separated by
     * by a path separator (forward slash '/').
     *
     * The location path name is "relative" to the parent Class. The
     * name of the parent should not be included in the path.
     *
     * 
     * For example, give the following two classes:
     * <code>
     *
     *    class Root {    
     *        Bar bar;    
     *    }
     *
     *    class Bar {
     *       String value;
     *    }
     * </code>
     *
     * And the following XML:
     *
     * <code>
     *    &lt;root&gt;
     *       &lt;foo&gt;
     *          &lt;bar&gt; value of bar &lt;/bar&gt;
     *       &lt;/foo&gt;
     *    &lt;/root&gt;
     * </code>
     *
     * Since foo has no associated class, the path for 'bar'
     * would be: "foo"
     *
     * @param path the "relative" location path for the field.
     * @see getLocationPath.
     */
    public void setLocationPath(String path) {
        //-- need to add some validation to the path at some point.
        _xmlPath = path;
    } //-- setLocationPath
    
    /**
     * Sets whether or not this field has been mapped in a Map or
     * Hashtable.
     *
     * @param mapped a boolean that when true indicates this field is
     * a Hashtable or Map.
    **/
    public void setMapped(boolean mapped) {
        _mapped = mapped;
    } //-- setMapped
    
    /**
     * This is a space separated list of xml names that this
     * Field descriptor matches. A '*' is wild.
     * @param matchExpr the space separated list of xml names, matched
     * by this descriptor
    **/
    public void setMatches(String matchExpr) {
        _isWild = false;
        if ((matchExpr == null) || (matchExpr.length() == 0)) return;

        StringTokenizer st = new StringTokenizer(matchExpr);
        ArrayList names = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (WILD_CARD.equals(token)) {
                _isWild = true;
                break;
            }
            names.add(token);
        }
        _matches = new String[names.size()];
        names.toArray(_matches);

    } //-- setMatches

    public void setMultivalued(boolean multivalued) {
        this._multivalued = multivalued;
    } //-- setMultivalued
    
    /**
     * Sets whether or not the described field is allowed
     * to be nil. A nillable field can have empty content
     * (text or element content), but may have attribute
     * values, and still be considered value, even if
     * the child elements are required.
     * 
     * @param nillable a boolean indicating whether or not
     * the described field may be nillable.
     */
    public void setNillable(boolean nillable) {
    	_nillable = nillable;
    } //-- setNillable
    
    /**     
     * Sets the value property with the given name
     *
     * @param propertyName the name of the property to set
     * the value of
     * @param value the value of the property
     * @see getProperty
     */
    public void setProperty(String propertyName, String value) {
        
        if (propertyName == null) {
            String err = "The argument 'propertyName' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (_properties == null)
            _properties = new Properties();
         
         
        if (value == null) {
            _properties.remove(propertyName);
        }
        else {
            _properties.put(propertyName, value);
        }
    } //-- setProperty
    

    /**
     * Sets the type of the XML Schema type of the value
     * for the field being described.
     * @param type the value type.
     */
     public void setSchemaType(String schemaType) {
        _schemaType =schemaType;
     }

     /**
      * Sets the prefix used in case the value
      * of the field described by this descriptor is of type
      * QName.
      * @param qNamePrefix
      */
      public void setQNamePrefix(String qNamePrefix) {
          _qNamePrefix = qNamePrefix;
      }

    /**
     * Sets the flag indicating that the field described by this
     * descriptor is a reference to another field in the object model.
     *
     * @param isReference, true if the field is a reference to another field.
    **/
    public void setReference(boolean isReference) {
        this._isReference = isReference;
    } //-- setReference

    /**
     * Sets the namespace prefix used when marshalling as XML.
     * @param nsPrefix the namespace prefix used when marshalling
     * the "described" object
    **/
    public void setNameSpacePrefix(String nsPrefix) {
        this._nsPrefix = nsPrefix;
    } //-- setNameSpacePrefix

    /**
     * Sets the namespace URI used when marshalling and unmarshalling as XML.
     * @param nsURI the namespace URI used when marshalling and
     * unmarshalling the "described" Object.
    **/
    public void setNameSpaceURI(String nsURI) {
        this._nsURI = nsURI;
    } //-- setNameSpaceURI


    /**
     * Sets the XML node type for the described field
     *
     * @param nodeType the NodeType for the described field
    **/
    public void setNodeType(NodeType nodeType) {
        this._nodeType = ( nodeType == null ? NodeType.Attribute : nodeType );
    } //-- setNodeType

    /**
     * Sets the whether or not the described field is required
     * @param required the flag indicating whether or not the
     * described field is required
    **/
    public void setRequired(boolean required) {
        this._required = required;
    } //-- setRequired

    /**
     * Sets whether or not the describled field is transient
     * @param isTransient the flag indicating whether or not the
     * described field is "transient".
     * @see #isTransient
    **/
    public void setTransient(boolean isTransient) {
        _transient = isTransient;
    } //-- isTransient

    /**
     * Sets whether or not the namespace for the parent "containing" 
     * class should be used during marshalling/unmarshalling when
     * no specific namespace URI has been set for this field.
     */
    public void setUseParentsNamespace(boolean useParentsNamespace) {
        _useParentClassNamespace = useParentsNamespace;
    }
    
    public void setValidator(FieldValidator validator) {

        //-- remove reference from current FieldValidator
        if (_validator != null) {
            _validator.setDescriptor((XMLFieldDescriptor)null);
        }

        this._validator = validator;

        if (_validator != null) {
            _validator.setDescriptor((XMLFieldDescriptor)this);
        }

    } //-- setValidator

    /**
     * Sets the xml name for the described field
     *
     * @param name the XML name for the described field
    **/
    public void setXMLName(String xmlName) {
        _xmlName = xmlName;
    } //-- setXMLName

    public String toString()
    {
        return "XMLFieldDesciptor: " + _fieldName + " AS " + _xmlName;
    }

    //---------------------/
    //- Protected Methods -/
    //---------------------/

    /**
     * Returns true if a call to #setMatches has been made with a non-null,
     * or non-zero-length value.
     * This method is used by the XML Mapping Loader
     *
     * @return true if a call to #setMatches has been made with a legal value.
     *
    **/
    protected boolean hasNonDefaultMatching() {
        return (_isWild || (_matches.length > 0));
    } //-- hasNonDefaultMatching


    /**
     * Returns true if the given class should be treated as a primitive
     * type. This method will return true for all Java primitive
     * types, the set of primitive object wrappers, as well
     * as Strings.
     *
     * @return true if the given class should be treated as a primitive
     * type
    **/
    protected static boolean isPrimitive(Class type) {

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

} //-- XMLFieldDescriptor


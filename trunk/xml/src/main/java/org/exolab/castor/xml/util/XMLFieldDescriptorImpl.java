/*
 * Copyright 2006 Keith Visco, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml.util;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.descriptors.CoreDescriptors;
import org.exolab.castor.xml.handlers.DateFieldHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Properties;

/**
 * XML field descriptor. Wraps {@link FieldDescriptor} and adds XML-related
 * information, type conversion, etc.
 * <p>
 * Note: When using a GeneralizedFieldHandler the getFieldType() methods of
 * handler and descriptor need to return the same result.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public class XMLFieldDescriptorImpl extends FieldDescriptorImpl implements XMLFieldDescriptor {
    private static final String WILD_CARD = "*";

    private static final String NULL_CLASS_ERR
        = "The 'type' argument passed to the constructor of "
         + "XMLFieldDescriptorImpl may not be null.";

    private static final String NULL_NAME_ERR
        = "The 'fieldName' argument passed to the constructor of "
         + "XMLFieldDescriptorImpl may not be null.";

    /** The index of this field within the constructor arguments. Note: This
     * field is only applicable if the field is an attribute field and it's
     * supposed to be set via the constructor. A value less than zero indicates
     * that this field is not part of the constructor arguments.
     */
    private int _argIndex = -1;

    /** True if the field is a container field. */
    private boolean _container = false;

    /** Flag to indicate that objects should be added to their as soon as they
     * are created, but before they are finished being populated.
     */
    private boolean _incremental = false;

    /** True if the field is a reference to another Object in the hierarchy. */
    public boolean _isReference = false;

    private boolean _isWild = false;

    /** True if the field type is mapped in a Hashtable or Map. */
    private boolean _mapped = false;

    private String[] _matches = null;

    /** True if the field is allowed to have nil content. */
    private boolean _nillable = false;

    /** The node type (attribute, element, text). */
    private NodeType _nodeType = null;

    /** The namespace prefix that is to be used when marshaling */
    private String _nsPrefix = null;

    /** The namespace URI used for both marshaling and unmarshaling. */
    private String _nsURI = null;

    /** The "user-set" properties of this XMLFieldDescriptor. */
    private Properties _xmlProperties = null;

    /** The XML Schema type of this field value. */
    private String _schemaType = null;

    /** The XML Schema type of the components for XML schema lists. */
    private String _componentType = null;

    /** The prefix used in case the value of the field described is of type QName. */
    private String _qNamePrefix = null;

    /** A flag which indicates the parent class' namespace should be used by default. */
    private boolean _useParentClassNamespace = false;

    private FieldValidator _validator = null;

    /** 
     * The local XML name of the field; this does not include any
     * path elements.
     */
    private String _xmlName    = null;

    /** The relative XML path used when wrapping in nested elements, does not
     * include the name of the field itself.
     */
    private String _xmlPath    = null;

	private List _substitutes;

    /**
     * Indicates whether the field described by this {@link XMLFieldDescriptorImpl} is
     * created as a result of a <xs:list> definition.
     */
    private boolean _derivedFromXSList;
	

    //----------------/
    //- Constructors -/
    //----------------/

    public XMLFieldDescriptorImpl(final Class fieldType, final String fieldName,
            final String xmlName, final NodeType nodeType) {
        _matches = new String[0];

        if (fieldName == null) { throw new IllegalArgumentException(NULL_NAME_ERR); }
        if (fieldType == null) { throw new IllegalArgumentException(NULL_CLASS_ERR); }

        setFieldName(fieldName);

        if (fieldType == org.exolab.castor.types.AnyNode.class) {
            // if the field type is an AnyNode Castor must treat it as
            // an object to avoid changes in the marshaling framework
            setFieldType(java.lang.Object.class);
        } else {
            setFieldType(fieldType);
        }

        _nodeType = ((nodeType == null) ? NodeType.Attribute : nodeType );

        //-- call the setXMLName method to handle checking for full path
        setXMLName(xmlName);
    }

    /**
     * Construct a new field descriptor for the specified field. This is an XML
     * field descriptor wrapping a field descriptor and adding XML related
     * properties and methods.
     *
     * @param fieldDesc The field descriptor
     * @param xmlName The XML name of the field
     * @param nodeType The node type of this field
     * @param primitiveNodeType
     * @throws MappingException Invalid mapping information
     */
    public XMLFieldDescriptorImpl(final FieldDescriptor fieldDesc, final String xmlName,
            final NodeType nodeType, final NodeType primitiveNodeType) throws MappingException {
        _matches = new String[0];

        if (fieldDesc instanceof XMLFieldDescriptor) {
            setContainingClassDescriptor(fieldDesc.getContainingClassDescriptor());
        }

        setFieldName(fieldDesc.getFieldName());

        if (fieldDesc.getFieldType() == org.exolab.castor.types.AnyNode.class) {
            // if the field type is an AnyNode Castor must treat it as
            // an object to avoid changes in the marshaling framework
            setFieldType(java.lang.Object.class);
        } else {
            setFieldType(fieldDesc.getFieldType());
        }

        ClassDescriptor cd = fieldDesc.getClassDescriptor();
        if (cd != null) {
            if (cd instanceof XMLClassDescriptor) {
                setClassDescriptor(cd);
            } else {
                setClassDescriptor(new XMLClassDescriptorAdapter(cd, null, primitiveNodeType));
            }
        }

        setHandler(fieldDesc.getHandler());
        // Check for instances of java.util.Date. This logic really doesn't belong here,
        // as it can interfere with user specified handlers. Instead it should go into
        // XMLMappingLoader.
        if (getFieldType() != null) {
            if (java.util.Date.class.isAssignableFrom(getFieldType())) {
                if (getHandler() instanceof FieldHandlerImpl) {
                    setHandler(new DateFieldHandler(getHandler()));
                }
            }
        }

        setTransient(fieldDesc.isTransient());
        setImmutable(fieldDesc.isImmutable());
        setRequired(fieldDesc.isRequired());
        setMultivalued(fieldDesc.isMultivalued());

        //-- handle xml name
        if (xmlName == null) {
            setXMLName(getFieldName());
        } else {
            setXMLName(xmlName);
        }

        if (nodeType == null) {
            if (isMultivalued()) {
                _nodeType = NodeType.Element;
            } else {
                _nodeType = NodeType.Attribute;
            }
        } else {
            _nodeType = nodeType;
        }

        if (isRequired()) {
            _validator = new FieldValidator();
            _validator.setMinOccurs(1);
            _validator.setDescriptor(this);
        }
    }

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Sets whether or not the value of the field represented by this
     * FieldDescriptor should be set via the constructor of the containing
     * ClassDescriptor. The index value greater than 0 specifies the index
     * within the argument array that the value of this field should be.
     * <p>
     * Note: This only applies to attribute mapped fields at this time.
     *
     * @param index the index within the argument array. A value less than zero
     *        indicates that this field should not be part of the constructor
     *        arguments.
     */
    public void setConstructorArgumentIndex(final int index) {
        if (_nodeType != NodeType.Attribute) {
            String err = "constructor arguments only valid for attribute mapped fields.";
            throw new IllegalStateException(err);
        }
        _argIndex = index;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getConstructorArgumentIndex()
     * {@inheritDoc}
     */
    public int getConstructorArgumentIndex() {
        return _argIndex;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isConstructorArgument()
     * {@inheritDoc}
     */
    public boolean isConstructorArgument() {
        return (_argIndex >= 0);
    }

    /**
     * Sets the location path for the field being described.
     * <p>
     * In most cases, this isn't needed. However sometimes a field may be mapped
     * to a nested element. In which case the value of the location path should
     * be the nested element name. If more than one level of nesting is needed
     * each nested element name should be separated by a path separator (forward
     * slash '/').
     * <p>
     * The location path name is "relative" to the parent Class. The name of the
     * parent should not be included in the path.
     * <p>
     * For example, give the following two classes: <code>
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
     * Since foo has no associated class, the path for 'bar' would be: "foo"
     *
     * @param path the "relative" location path for the field.
     * @see #getLocationPath
     */
    public void setLocationPath(final String path) {
        //-- need to add some validation to the path at some point.
        _xmlPath = path;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getLocationPath()
     * {@inheritDoc}
     */
    public String getLocationPath() {
        return _xmlPath;
    }

    /**
     * Sets the namespace prefix used when marshaling as XML.
     *
     * @param nsPrefix The namespace prefix used when marshaling the
     *        "described" object.
     */
    public void setNameSpacePrefix(final String nsPrefix) {
        _nsPrefix = nsPrefix;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getNameSpacePrefix()
     * {@inheritDoc}
     */
    public String getNameSpacePrefix() {
        return _nsPrefix;
    }

    /**
     * Sets whether or not the namespace for the parent "containing" class
     * should be used during marshaling/unmarshaling when no specific
     * namespace URI has been set for this field.
     */
    public void setUseParentsNamespace(final boolean useParentsNamespace) {
        _useParentClassNamespace = useParentsNamespace;
    }

    /**
     * Sets the namespace URI used when marshaling and unmarshaling as XML.
     *
     * @param nsURI The namespace URI used when marshaling and unmarshaling the
     *        "described" Object.
     */
    public void setNameSpaceURI(final String nsURI) {
        _nsURI = nsURI;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getNameSpaceURI()
     * {@inheritDoc}
     */
    public String getNameSpaceURI() {
        ClassDescriptor parent = getContainingClassDescriptor();
        if ((_nsURI == null) && (parent != null) && _useParentClassNamespace) {
            Class type = getFieldType();
            // boolean test = isPrimitive(type) || isBuiltInType(type) || isMappedItem(type);
            if ((_nodeType == NodeType.Element) /* && test*/) {
                if (parent instanceof XMLClassDescriptor) {
                    return ((XMLClassDescriptor) parent).getNameSpaceURI();
                }
            }
        }
        return _nsURI;
    }

    /**
     * Sets the XML node type for the described field.
     *
     * @param nodeType the NodeType for the described field.
     */
    public void setNodeType(final NodeType nodeType) {
        _nodeType = ((nodeType == null) ? NodeType.Attribute : nodeType);
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getNodeType()
     * {@inheritDoc}
     */
    public NodeType getNodeType() {
        return _nodeType;
    }

    /**
     * Sets the value property with the given name.
     *
     * @param propertyName The name of the property to set the value of.
     * @param value The value of the property.
     * @see #getProperty
     */
    public void setXMLProperty(final String propertyName, final String value) {
        if (propertyName == null) {
            String err = "The argument 'propertyName' must not be null.";
            throw new IllegalArgumentException(err);
        }

        if (_xmlProperties == null) {
            _xmlProperties = new Properties();
        }

        if (value == null) {
            _xmlProperties.remove(propertyName);
        } else {
            _xmlProperties.put(propertyName, value);
        }
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getProperty(java.lang.String)
     * {@inheritDoc}
     */
    public String getXMLProperty(final String propertyName) {
        if ((_xmlProperties == null) || (propertyName == null)) { return null; }
        return _xmlProperties.getProperty(propertyName);
    }

    /**
     * Sets the type of the XML Schema type of the value for the field being
     * described.
     *
     * @param schemaType The value type.
     */
    public void setSchemaType(final String schemaType) {
        _schemaType =schemaType;
    }

    /**
     * Sets the type of the XML Schema type of the value for the field being
     * described.
     *
     * @param componentType The component type for &lt;xs:list&gt;s.
     */
    public void setComponentType(final String componentType) {
        _componentType = componentType;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getSchemaType()
     * {@inheritDoc}
     */
    public String getSchemaType() {
         return _schemaType;
     }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getComponentType()
     */
    public String getComponentType() {
         return _componentType;
     }

    public void setValidator(final FieldValidator validator) {
        if (_validator != null) {
            _validator.setDescriptor(null);
        }
        _validator = validator;
        if (_validator != null) {
            _validator.setDescriptor(this);
        }
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getValidator()
     * {@inheritDoc}
     */
    public FieldValidator getValidator() {
        return _validator;
    }

    /**
     * Sets the xml name for the described field.
     *
     * @param xmlName the XML name for the described field.
     */
    public void setXMLName(final String xmlName) {
        _xmlName = xmlName;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#getXMLName()
     * {@inheritDoc}
     */
    public String getXMLName() {
        return _xmlName;
    }

    /**
     * Set if the field is a container field or not.
     *
     * @param isContainer a boolean indicating whether or not the field is a
     *        container field.
     */
    public void setContainer(final boolean isContainer) {
        _container = isContainer;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isContainer()
     * {@inheritDoc}
     */
    public boolean isContainer() {
        return _container;
    }

    /**
     * Sets the incremental flag which indicates whether this member can be
     * added before the unmarshaler is finished unmarshaling it.
     *
     * @param incremental the boolean which if true indicated that this member
     *        can safely be added before the unmarshaler is finished
     *        unmarshaling it.
     */
    public void setIncremental(final boolean incremental) {
        _incremental = incremental;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isIncremental()
     * {@inheritDoc}
     */
    public boolean isIncremental() {
        return _incremental;
    }

    /**
     * Sets whether or not this field has been mapped in a Map or Hashtable.
     *
     * @param mapped a boolean that when true indicates this field is a
     *        Hashtable or Map.
     */
    public void setMapped(final boolean mapped) {
        _mapped = mapped;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isMapped()
     * {@inheritDoc}
     */
    public boolean isMapped() {
        return _mapped;
    }

    /**
     * Sets whether or not the described field is allowed to be nil. A nillable
     * field can have empty content (text or element content), but may have
     * attribute values, and still be considered value, even if the child
     * elements are required.
     *
     * @param nillable a boolean indicating whether or not the described field
     *        may be nillable.
     */
    public void setNillable(final boolean nillable) {
        _nillable = nillable;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isNillable()
     * {@inheritDoc}
     */
    public boolean isNillable() {
        return _nillable;
    }

    /**
     * Sets the flag indicating that the field described by this descriptor is a
     * reference to another field in the object model.
     *
     * @param isReference true if the field is a reference to another field.
     */
    public void setReference(final boolean isReference) {
        _isReference = isReference;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isReference()
     * {@inheritDoc}
     */
    public boolean isReference() {
        return _isReference;
    }

    /**
     * Sets the prefix used in case the value of the field described by this
     * descriptor is of type QName.
     *
     * @param qNamePrefix
     */
    public void setQNamePrefix(final String qNamePrefix) {
        _qNamePrefix = qNamePrefix;
    }

    /**
     * Returns the prefix used in case the value of the field described by this
     * descriptor is of type QName. This is helpful for the marshaler but not
     * mandatory.
     *
     * @return the prefix used in the QName value.
     */
    public String getQNamePrefix() {
        return _qNamePrefix;
    }

    /**
     * This is a space separated list of xml names that this Field descriptor
     * matches. A '*' is wild.
     *
     * @param matchExpr the space separated list of xml names, matched by this
     *        descriptor.
     */
    public void setMatches(String matchExpr) {
        _isWild = false;

        if ((matchExpr == null) || (matchExpr.length() == 0)) {
            return;
        }

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
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#matches(java.lang.String)
     * {@inheritDoc}
     */
    public boolean matches(final String xmlName) {
        if (xmlName != null) {
            if (_isWild) {
                return true;
            } else if (_matches.length > 0) {
                for (int i = 0; i < _matches.length; i++) {
                    if (xmlName.equals(_matches[i])) {
                        return true;
                    }
                }
            } else {
                return xmlName.equals(_xmlName);
            }
        }
        return false;
    }

    /**
     * @see org.exolab.castor.xml.XMLFieldDescriptor#matches(java.lang.String, java.lang.String)
     * {@inheritDoc}
     */
    public boolean matches(final String xmlName, final String namespace) {
        // compare namespaces
        if (namespace == null) {
            if ((_nsURI != null) && (_nsURI.length() > 0)) {
                return false;
            }
        } else if (_nsURI == null) {
            if ((namespace.length() > 0) && (!_isWild)) {
                return false;
            }
        } else if (!_nsURI.equals(namespace)) {
            return false;
        }

        // if we make this far the namespaces match, now compare names
        return matches(xmlName);
    }

    /**
     * Returns true if two XMLFieldDescriptors should be treated as equal. Any
     * XMLFieldDescriptor that handles the same field is considered equal.
     * @param obj The object to compare to <code>this</code>
     *
     * @return true if two XMLFieldDescriptors should be treated as equal.
     */
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj == null) || (!(obj instanceof XMLFieldDescriptor))) {
            return false;
        }

        XMLFieldDescriptor descriptor = (XMLFieldDescriptor) obj;

        // check field names
        if (!getFieldName().equals(descriptor.getFieldName())) {
            return false;
        }

        // check field types
        if (!getFieldType().equals(descriptor.getFieldType())) {
            return false;
        }

        // check field handler
        FieldHandler tmpHandler = descriptor.getHandler();
        if (getHandler() == null) {
            return (tmpHandler == null);
        } else if (tmpHandler == null) {
            return false;
        }

        // The following line causes some issues when used against a FieldHandlerImpl
        // because the equals method for FieldHandlerImpl is the default. Temporarily
        // replace it with a slightly more generic comparison but this should probably
        // change in the future. (kv)
        // return (_handler.equals(tmpHandler));
        return (getHandler().getClass().isInstance(tmpHandler));
    }

    /**
     * Returns the hashCode for this XMLFieldDescriptor
     * @return the hashCode for this XMLFieldDescriptor
     */
    public int hashCode() {
        int hash = 17;
        hash = 17 * getFieldName().hashCode();
        hash = hash * 17 * getFieldType().hashCode();
        if (getHandler() != null) { hash = hash * 17 * getHandler().hashCode(); }
        return hash;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(32);
        buffer.append("XMLFieldDesciptor: ");
        buffer.append(getFieldName());
        buffer.append(" AS ");
        buffer.append(_xmlName);
        if (getNameSpaceURI() != null) {
            buffer.append("{" + getNameSpaceURI() + "}");
        }
        return buffer.toString();
    }

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Returns true if the given class should be treated as a primitive type.
     * This method will return true for all Java primitive types, the set of
     * primitive object wrappers, as well as Strings.
     *
     * @return true If the given class should be treated as a primitive type.
     */
    private static boolean isPrimitive(final Class type) {
        if (type == null) {
            return false;
        }
        if (type.isPrimitive()) {
            return true;
        }
        if (type == String.class) {
            return true;
        }
        if ((type == Boolean.class) || (type == Character.class)) {
            return true;
        }

        // Any class which extends Number should be treated as a primitive.
        return (type.getSuperclass() == Number.class);
    }

    /**
     * Return true if the given class is a "built-in" type. A built-in type is
     * one in which Castor provides the default descriptor for.
     *
     * @param type The class to check.
     * @return true If the given class is a built-in type.
     */
    private static boolean isBuiltInType(final Class type) {
        if (type == null) {
            return false;
        }
        //-- All built-in Java types, such as java.util.Date,
        //-- java.sql.Date, various Collection classes, etc.
        return (CoreDescriptors.getDescriptor(type) != null);
    }

    private static boolean isMappedItem(final Class fieldType) {
        return (fieldType == MapItem.class);
    }
    
    /**
     * Returns the possible substitution groups for this class.
     * @return the possible substitution groups for this class.
     */
    public List getSubstitutes()
    {
        return _substitutes;
    }

    /**
     * Sets the possible substitution groups for this class.
     * @param substitutes Possible substitution groups for this class.
     */
    public void setSubstitutes(List substitutes) {
        _substitutes = substitutes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLFieldDescriptor#setDerivedFromXSList(boolean)
     */
    public void setDerivedFromXSList(final boolean derivedFromXSList) {
        _derivedFromXSList = derivedFromXSList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.XMLFieldDescriptor#isDerivedFromXSList()
     */
    public boolean isDerivedFromXSList() {
        return _derivedFromXSList;
    }

}

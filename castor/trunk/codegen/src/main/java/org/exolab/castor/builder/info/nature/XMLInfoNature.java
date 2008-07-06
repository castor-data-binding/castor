/*
 * Copyright 2008 Lukas Lang
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
 *
 */
package org.exolab.castor.builder.info.nature;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.builder.info.XMLInfo;
import org.exolab.castor.builder.types.XSType;

/**
 * A XML specific view of a {@link PropertyHolder}, which can be a {@link ClassInfo} or a
 * {@link FieldInfo}. Property based implementation.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 */
public final class XMLInfoNature extends BaseNature {

    /**
     * Property namespace prefix.
     */
    private static final String NAMESPACE_PREFIX = "namespaceprefix";

    /**
     * Property namespace URI.
     */
    private static final String NAMESPACE_URI = "namespaceuri";

    /**
     * Property node name.
     */
    private static final String NODE_NAME = "nodename";

    /**
     * Property node type.
     */
    private static final String NODE_TYPE = "nodetype";

    /**
     * Property schema type.
     */
    private static final String SCHEMA_TYPE = "schematype";

    /**
     * Indicates XML schema definition is global element or element with
     * anonymous type.
     */
    private static final String ELEMENT_DEFINITION = "elementdefinition";

    /**
     * A flag indicating if the object described by this XML info can appear
     * more than once.
     */
    private static final String MULTIVALUED = "multivalued";

    /** Indicates the XML object must appear at least once. */
    private static final String REQUIRED = "required";

    /**
     * Constructor taking a PropertyHolder.
     * 
     * @param holder
     *            in focus.
     */
    public XMLInfoNature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * Implementation returns the fully qualified class name.
     * 
     * @return the Nature id.
     * @see org.exolab.castor.builder.info.nature.Nature#getId()
     */
    public String getId() {
        return this.getClass().getName();
    }

    /**
     * Returns the namespace prefix of the object described by this XMLInfo.
     * 
     * @return the namespace prefix of the object described by this XMLInfo
     */
    public String getNamespacePrefix() {
        return (String) this.getProperty(NAMESPACE_PREFIX);
    }

    /**
     * Returns the namespace URI of the object described by this XMLInfo.
     * 
     * @return the namespace URI of the object described by this XMLInfo
     */
    public String getNamespaceURI() {
        return (String) this.getProperty(NAMESPACE_URI);
    }

    /**
     * Returns the XML name for the object described by this XMLInfo.
     * 
     * @return the XML name for the object described by this XMLInfo, or null if
     *         no name has been set
     */
    public String getNodeName() {
        return (String) this.getProperty(NODE_NAME);
    }

    /**
     * Returns the node type for the object described by this XMLInfo.
     * <code>XMLInfo.ELEMENT_TYPE</code> if property is not set.
     * 
     * @return the node type for the object described by this XMLInfo
     */
    public short getNodeType() {
        Short s = (Short) this.getProperty(NODE_TYPE);
        if (s == null) {
            return XMLInfo.ELEMENT_TYPE;
        }
        return s.shortValue();
    }

    /**
     * Returns the string name of the nodeType, either "attribute", "element" or
     * "text".
     * 
     * @return the name of the node-type of the object described by this
     *         XMLInfo.
     */
    public String getNodeTypeName() {
        Short s = (Short) this.getProperty(NODE_TYPE);
        switch (s.shortValue()) {
        case XMLInfo.ATTRIBUTE_TYPE:
            return "attribute";
        case XMLInfo.ELEMENT_TYPE:
            return "element";
        case XMLInfo.TEXT_TYPE:
            return "text";
        default:
            return "unknown";
        }
    }

    /**
     * Returns the XML Schema type for the described object.
     * 
     * @return the XML Schema type.
     */
    public XSType getSchemaType() {
        return (XSType) this.getProperty(SCHEMA_TYPE);
    }

    /**
     * Returns true if XSD is global element or element with anonymous type or
     * false if property is not set.
     * 
     * @return true if xsd is element, false if not or null.
     */
    public boolean isElementDefinition() {
        return getBooleanPropertyDefaultFalse(ELEMENT_DEFINITION);
    }

    /**
     * Returns whether or not the object described by this XMLInfo is
     * multi-valued (appears more than once in the XML document). Returns false
     * if the property was not set.
     * 
     * @return true if this object can appear more than once, false if not or
     *         not set.
     */
    public boolean isMultivalued() {
        return getBooleanPropertyDefaultFalse(MULTIVALUED);
    }

    /**
     * Return true if the XML object described by this XMLInfo must appear at
     * least once in the XML document (or object model). Returns false if the
     * property was not set.
     * 
     * @return true if the XML object must appear at least once, false if not or
     *         not set.
     */
    public boolean isRequired() {
        return getBooleanPropertyDefaultFalse(REQUIRED);
    }

    /**
     * Sets whether or not XSD is element or not.
     * 
     * @param elementDef
     *            The flag indicating whether or not XSD is global element,
     *            element with anonymous type or not.
     */
    public void setElementDefinition(final boolean elementDef) {
        this.setProperty(ELEMENT_DEFINITION, new Boolean(elementDef));
    }

    /**
     * Sets whether the XML object can appear more than once in the XML
     * document.
     * 
     * @param multivalued
     *            The boolean indicating whether or not the object can appear
     *            more than once.
     */
    public void setMultivalued(final boolean multivalued) {
        this.setProperty(MULTIVALUED, new Boolean(multivalued));
    }

    /**
     * Sets the desired namespace prefix for this XMLInfo There is no guarantee
     * that this prefix will be used.
     * 
     * @param nsPrefix
     *            the desired namespace prefix
     */
    public void setNamespacePrefix(final String nsPrefix) {
        this.setProperty(NAMESPACE_PREFIX, nsPrefix);
    }

    /**
     * Sets the Namespace URI for this XMLInfo.
     * 
     * @param nsURI
     *            the Namespace URI for this XMLInfo
     */
    public void setNamespaceURI(final String nsURI) {
        this.setProperty(NAMESPACE_URI, nsURI);
    }

    /**
     * Sets the XML name of the object described by this XMLInfo.
     * 
     * @param name
     *            the XML node name of the described object.
     */
    public void setNodeName(final String name) {
        this.setProperty(NODE_NAME, name);
    }

    /**
     * Sets the nodeType for this XMLInfo.
     * 
     * @param nodeType
     *            the node type of the described object
     */
    public void setNodeType(final short nodeType) {
        Short s = new Short(nodeType);
        this.setProperty(NODE_TYPE, s);
    }

    /**
     * Sets whether or not the XML object must appear at least once.
     * 
     * @param required
     *            the flag indicating whether or not this XML object is required
     */
    public void setRequired(final boolean required) {
        Boolean b = new Boolean(required);
        this.setProperty(REQUIRED, b);
    }

    /**
     * Sets the XML Schema type for this XMLInfo.
     * 
     * @param xsType
     *            the XML Schema type
     */
    public void setSchemaType(final XSType xsType) {
        this.setProperty(SCHEMA_TYPE, xsType);
    }

}

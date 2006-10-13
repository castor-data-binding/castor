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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.XSType;

/**
 * A class for storing XML related information
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-02-23 01:37:50 -0700 (Thu, 23 Feb 2006) $
 */
public class XMLInfo {

    /**
     * Represents the attribute node type
     */
    public static final short ATTRIBUTE_TYPE = 0;

    /**
     * Represents the element node type
     */
    public static final short ELEMENT_TYPE   = 1;

    /**
     * Represents the text node type
     */
    public static final short TEXT_TYPE      = 2;

    /**
     * A flag indicating if the object described by this XML info can appear
     * more than once
     */
    private boolean multivalued = false;

    /**
     * the xml node name
     */
    private String name = null;

    /**
     * The xml node type
     */
    private short nodeType = ELEMENT_TYPE;

    /**
     * The namespace prefix
     */
    private String nsPrefix = null;

    /**
     * The namespace URI
     */
    private String nsURI    = null;

    /**
     * indicates XML schema definition is global element or element with
     * anonymous type.
     */
    private boolean elementDef = false;

    /**
     * indicates the XML object must appear at least once
     */
    private boolean required = false;

    /**
     * The XML Schema type
     */
    private XSType xsType = null;

    /**
     * Creates a new XML Info
     */
    public XMLInfo() {
        super();
    } //-- XMLInfo

    /**
     * Creates a new XMLInfo with the given node type
     *
     * @param nodeType
     *            the nodeType which this XMLInfo represents
     */
    public XMLInfo(short nodeType) {
        this.nodeType = nodeType;
    }  //-- XMLInfo

    /**
     * Creates a new XMLInfo with the given xml name and node type
     *
     * @param name
     *            the xml node name
     * @param nodeType
     *            the nodeType which this XMLInfo represents
     */
    public XMLInfo(String name, short nodeType) {
        this.nodeType = nodeType;
        this.name     = name;
    }  //-- XMLInfo

    /**
     * Returns the XML name for the object described by this XMLInfo
     *
     * @return the XML name for the object described by this XMLInfo, or null if
     *         no name has been set
     */
    public String getNodeName() {
        return name;
    } //-- getNodeName

    /**
     * Returns the namespace prefix of the object described by this XMLInfo
     *
     * @return the namespace prefix of the object described by this XMLInfo
     */
    public String getNamespacePrefix() {
        return nsPrefix;
    } //-- getNamespacePrefix

    /**
     * Returns the namespace URI of the object described by this XMLInfo
     *
     * @return the namespace URI of the object described by this XMLInfo
     */
    public String getNamespaceURI() {
        return nsURI;
    } //-- getNamespaceURI

    /**
     * Returns true if XSD is global element or element with anonymous type.
     *
     * @return true if xsd is element
     */
    public boolean isElementDefinition() {
            return elementDef;
    } //-- isElementDefinition

    /**
     * Returns the node type for the object described by this XMLInfo
     *
     * @return the node type for the object described by this XMLInfo
     */
    public short getNodeType() {
        return nodeType;
    } //-- getNodeType

    /**
     * Returns the string name of the nodeType, either "attribute", "element" or
     * "text".
     *
     * @return the name of the node-type of the object described by this
     *         XMLInfo.
     */
    public String getNodeTypeName() {
        switch (nodeType) {
            case ATTRIBUTE_TYPE:
                return "attribute";
            case ELEMENT_TYPE:
                return "element";
            case TEXT_TYPE:
                return "text";
            default:
                return "unknown";
        }
    } //-- getNodeTypeName

    /**
     * Returns the XML Schema type for the described object.
     *
     * @return the XML Schema type.
     */
    public XSType getSchemaType() {
        return xsType;
    } //-- getXSType

    /**
     * Return whether or not the object described by this XMLInfo is
     * multi-valued (appears more than once in the XML document).
     *
     * @return true if this object can appear more than once.
     */
    public boolean isMultivalued() {
        return multivalued;
    } //-- isMultivalued

    /**
     * Return true if the XML object described by this XMLInfo must appear at
     * least once in the XML document (or object model).
     *
     * @return true if the XML object must appear at least once.
     */
    public boolean isRequired() {
        return required;
    } //-- isRequired

    /**
     * Sets whether the XML object can appear more than once in the XML document.
     *
     * @param multivalued The boolean indicating whether or not the object can appear
     *        more than once.
     */
    public void setMultivalued(boolean multivalued) {
        this.multivalued = multivalued;
    } //-- setMultivalued

    /**
     * Sets the XML name of the object described by this XMLInfo.
     *
     * @param name
     *            the XML node name of the described object.
     */
    public void setNodeName(String name) {
        this.name = name;
    } //-- setName

    /**
     * Sets the desired namespace prefix for this XMLInfo There is no guarantee
     * that this prefix will be used.
     *
     * @param nsPrefix
     *            the desired namespace prefix
     */
    public void setNamespacePrefix(String nsPrefix) {
        this.nsPrefix = nsPrefix;
    } //-- setNamespacePrefix

    /**
     * Sets the Namespace URI for this XMLInfo
     *
     * @param nsURI
     *            the Namespace URI for this XMLInfo
     */
    public void setNamespaceURI(String nsURI) {
        this.nsURI = nsURI;
    } //-- setNamespaceURI

    /**
     * Sets whether or not XSD is element or not.
     *
     * @param elementDef
     *            The flag indicating whether or not XSD is global element,
     *            element with anonymous type or not.
     */
    public void setElementDefinition(boolean elementDef) {
        this.elementDef = elementDef;
    } //-- setElementDefinition

    /**
     * Sets the nodeType for this XMLInfo
     *
     * @param nodeType
     *            the node type of the described object
     */
    public void setNodeType(short nodeType) {
        this.nodeType = nodeType;
    } //-- setNodeType

    /**
     * Sets the XML Schema type for this XMLInfo
     *
     * @param xsType
     *            the XML Schema type
     */
    public void setSchemaType(XSType xsType) {
        this.xsType = xsType;
    } //-- setSchemaType

    /**
     * Sets whether or not the XML object must appear at least once
     *
     * @param required
     *            the flag indicating whether or not this XML object is required
     */
    public void setRequired(boolean required) {
        this.required = required;
    } //-- setRequired

} //-- XMLInfo

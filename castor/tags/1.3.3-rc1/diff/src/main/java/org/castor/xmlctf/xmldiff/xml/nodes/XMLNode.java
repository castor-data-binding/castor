/*
 * Copyright 2007 Edward Kuns
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
 * $Id: Element.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.castor.xmlctf.xmldiff.xml.nodes;

import java.util.Iterator;

import org.castor.xmlctf.xmldiff.xml.Location;

/**
 * The base node for all XMLNode types.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public abstract class XMLNode {

    /** Node is a root node. */
    public static final int    ROOT                   = 1;
    /** Node is an element. */
    public static final int    ELEMENT                = 2;
    /** Node is an attribute. */
    public static final int    ATTRIBUTE              = 3;
    /** Node is a text node. */
    public static final int    TEXT                   = 4;
    /** Node is a processing instruction. */
    public static final int    PROCESSING_INSTRUCTION = 5;

    /** The localname (non-qualified) for this XMLNode. */
    private final String       _localName;
    /** The node type being created. */
    private final int          _nodeType;

    /** A reference for the parent node. */
    private ParentNode         _parent           = null;
    /** The namespace to which this XMLNode belongs. */
    private String             _namespace        = null;

    /**
     * Creates a new XMLNode
     *
     * @param namespace the namespace URI for this node. [May be null]
     * @param localName the local-name of this node. [May be null]
     * @param nodeType the node type being created
     */
    XMLNode(final String namespace, final String localName, final int nodeType) {
        _namespace = namespace;
        _localName = localName;
        _nodeType  = nodeType;
    }

    /**
     * Returns the type of this node.
     * @return The type of this node
     */
    public final int getNodeType() {
        return _nodeType;
    }

    /**
     * Returns the local name of the node. Returns the local name of an element
     * or attribute, the prefix of a namespace node, the target of a processing
     * instruction, or null for all other node types.
     *
     * @return The local name of the node, or null if the node has no name
     */
    public String getLocalName() {
        return _localName;
    }

    /**
     * Returns the namespace URI the node. Returns the namespace URI of an
     * element, attribute or namespace node, or null for all other node types.
     *
     * @return The namespace URI of the node, or null if the node has no
     *         namespace URI
     */
    public String getNamespaceURI() {
        return _namespace;
    }

    /**
     * Returns the parent node, or null if the node has no parent. This method
     * is valid on all node types except the root node. Attribute and namespace
     * nodes have the element as their parent node.
     *
     * @return The parent node, or null
     */
    public ParentNode getParentNode() {
        return _parent;
    }

    /**
     * Returns the root node.
     *
     * @return The root node
     */
    public XMLNode getRootNode() {
        return (_parent != null) ? _parent.getRootNode() : null;
    }

    /**
     * Returns the string value of the node. The string value of a text node or
     * an attribute node is its text value. The string value of an element or a
     * root node is the concatenation of the string value of all its child
     * nodes. The string value of a namespace node is its namespace URI. The
     * string value of a processing instruction is the instruction, and the
     * string value of a comment is the comment text.
     *
     * @return The string value of the node
     */
    public abstract String getStringValue();

    /**
     * Returns the namespace URI associated with this namespace prefix, as
     * defined in the context of this node. Returns null if the prefix is
     * undefined. Returns empty if the prefix is defined and associated with no
     * namespace. This method is valid only for element nodes.
     *
     * @param prefix The namespace prefix
     * @return The namespace URI, or null
     */
    public String getNamespaceURI(final String prefix) {
        return (_parent != null) ? _parent.getNamespaceURI(prefix) : null;
    }

    /**
     * Sets the namespace URI for this XMLNode.
     * @param namespace the Namespace URI
     */
    public void setNamespace(final String namespace) {
        _namespace = namespace;
    }

    /**
     * Sets the parent XMLNode.
     *
     * @param node the XMLNode which is the parent of this XMLNode
     */
    void setParent(final ParentNode node) {
        _parent = node;
    }

    /**
     * Finds and returns the location of this node in its root's tree.
     * @return the location of this node in its root's tree.
     */
    public String getNodeLocation() {
        int column = -1;
        int line = -1;

        String xpath = "XPATH: " + getXPath();

        if (this instanceof Element) {
            Location loc = ((Element) this).getLocation();
            if (loc != null) {
                line = loc.getLineNumber();
                column = loc.getColumnNumber();
            }
        }

        String location = null;
        if (line >= 0) {
            location = "[" + line + ", " + column + "] " + xpath;
        } else {
            location = xpath;
        }

        return location;
    }

    /**
     * Returns the XPath from the root node to this node.
     * @return the XPath from the root node to this node.
     */
    protected String getXPath() {
        StringBuffer xpath = new StringBuffer();

        switch (getNodeType()) {
            case XMLNode.ATTRIBUTE:
                xpath.append(getParentNode().getXPath() + "/@" + getLocalName());
                break;
            case XMLNode.ELEMENT:
                String name = getLocalName();
                xpath.append(getParentNode().getXPath() + "/" + name);

                // Do we have elements of the same type before us in our parent's list?
                int position = 1;
                Iterator i = getParentNode().getChildIterator();
                while (i.hasNext()) {
                    XMLNode sibling = (XMLNode) i.next();
                    if (sibling == this) {
                        break;
                    }
                    if (name.equals(sibling.getLocalName())) {
                        ++position;
                    }
                }

                boolean usePosition = (position > 1);
                if (!usePosition) {
                    // Do we have elements of the same type after us in our parent's list?
                    while (i.hasNext()) {
                        XMLNode sibling = (XMLNode) i.next();
                        if (name.equals(sibling.getLocalName())) {
                            usePosition = true;
                            break;
                        }
                    }
                }
                if (usePosition) {
                    xpath.append("[" + position + "]");
                }
                break;
            case XMLNode.TEXT:
                xpath.append(getParentNode().getXPath() + "/text()");
                break;
            case XMLNode.ROOT:
                break;
            case XMLNode.PROCESSING_INSTRUCTION:
                xpath.append(getParentNode().getXPath() + "/pi()");
                break;
            default:
                break;
        }

        return xpath.toString();
    }

}

/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */


package org.exolab.adaptx.xpath;


/**
 * Abstract class representing a node in a document tree, on which
 * XPath expressions can be evaluated. This abstract class provides
 * all the services required by XPath, and enables multiple
 * implementations to exist. It does not make use of iterators or
 * traversals, but assumes double-linked lists are used to maintain
 * node lists.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$
 */
public abstract class XPathNode 
    implements java.io.Serializable
{


    /**
     * Node is an element.
     */
    public static final int ELEMENT = 1;


    /**
     * Node is an attribute.
     */
    public static final int ATTRIBUTE = 2;


    /**
     * Node is a text node.
     */
    public static final int TEXT = 3;


    /**
     * Node is a processing instruction.
     */
    public static final int PI = 7;


    /**
     * Node is a comment.
     */
    public static final int COMMENT = 8;


    /**
     * Node is a root node.
     */
    public static final int ROOT = 9;


    /**
     * Node is a namespace node.
     */
    public static final int NAMESPACE = 13;


    /**
     * Returns the first child node of this node, or null if
     * the node has no children. Child nodes are only returned
     * for an element or root node.
     *
     * @return The first child node of this node, or null
    **/
    public abstract XPathNode getFirstChild();


    /**
     * Returns true if this node has any child nodes.
     *
     * @return True if this node has any child nodes.
    **/
    public abstract boolean hasChildNodes();


    /**
     * Returns the next sibling node in document order, or null
     * if this node is the last node. This method is value for
     * any node except the root node.
     *
     * @return The next sibling node in document order, or null
    **/
    public abstract XPathNode getNext();


    /**
     * Returns the previous sibling node in document order, or null
     * if this node is the first node. This method can is valid for
     * any node except the root node.
     *
     * @return The previous sibling node in document order, or null
    **/
    public abstract XPathNode getPrevious();


    /**
     * Returns the type of this node.
     *
     * @return The type of this node
    **/
    public abstract int getNodeType();


    /**
     * Returns the first in a list of attribute nodes, or null
     * if the node has no attributes. This method is valid only
     * for the element node.
     *
     * @return The first in a list of attribute nodes, or null
    **/
    public abstract XPathNode getFirstAttribute();


    /**
     * Returns the value of the named attribute, or null if the
     * node has no such attribute. If the argument <tt>uri</tt>
     * is null, the node's namespace URI will be used. This method
     * is valid only for the element node.
     *
     * @param uri The attribute's namespace URI, or null
     * @param local The attribute's local name
     * @return The attribute's value, or null if no such attribute exists
    **/
    public abstract String getAttribute( String uri, String local );


    /**
     * Returns the first in a list of namespace nodes, or null
     * if the node has no namespaces. This method is valid only
     * for the element node.
     *
     * @return The first in a list of namespace nodes, or null
    **/
    public abstract XPathNode getFirstNamespace();


    /**
     * Returns the local name of the node. Returns the local
     * name of an element or attribute, the prefix of a namespace
     * node, the target of a processing instruction, or null for
     * all other node types.
     *
     * @return The local name of the node, or null if the node has
     * no name
    **/
    public abstract String getLocalName();


    /**
     * Returns the namespace URI the node. Returns the namespace URI
     * of an element, attribute or namespace node,  or null for
     * all other node types.
     *
     * @return The namespace URI of the node, or null if the node has
     * no namespace URI
    **/
    public abstract String getNamespaceURI();


    /**
     * Returns the parent node, or null if the node has no parent.
     * This method is valid on all node types except the root node.
     * Attribute and namespace nodes have the element as their parent
     * node.
     *
     * @return The parent node, or null
    **/
    public abstract XPathNode getParentNode();


    /**
     * Returns the root node. 
     * Note to implementors: This should NOT return null. 
     *
     * @return The root node
    **/
    public abstract XPathNode getRootNode();


    /**
     * Returns the string value of the node. The string value of a text
     * node or an attribute node is it's text value. The string value of
     * an element or a root node is the concatenation of the string value
     * of all its child nodes. The string value of a namespace node is its
     * namespace URI. The string value of a processing instruction is the
     * instruction, and the string value of a comment is the comment text.
     *
     * @return The string value of the node
    **/
    public abstract String getStringValue();


    /**
     * Returns the namespace URI associated with this namespace prefix,
     * as defined in the context of this node. Returns null if the prefix
     * is undefined. Returns empty if the prefix is defined and associated
     * with no namespace. This method is valid only for element nodes.
     *
     * @param prefix The namespace prefix
     * @return The namespace URI, or null
    **/
    public abstract String getNamespaceURI( String prefix );


    /**
     * Returns the namespace prefix associated with this namespace URI,
     * as defined in the context of this node. Returns null if no prefix
     * is defined for this namespace URI. Returns an empty string if the
     * default prefix is associated with this namespace URI. This method
     * is valid only for element nodes.
     *
     * @param uri The namespace URI
     * @return The namespace prefix, or null
    **/
    public abstract String getNamespacePrefix( String uri );


} //-- XPathNode

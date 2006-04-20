/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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


package org.exolab.adaptx.xml.dom2xpn;

import org.exolab.adaptx.xpath.XPathNode;

import org.w3c.dom.*;

/**
 * The base wrapper class for a DOM nodes, used by the
 * implementation of XPathNode for the W3C DOM API
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
abstract class BaseDOMXPathNode extends XPathNode {


    /**
     * A reference for the parent node
    **/
    private XPathNode _parent = null;
    
    /**
     * A pointer to the next node sibling
    **/
    private XPathNode _next = null;
    
    /**
     * A reference to the preceeding node sibling 
    **/
    private XPathNode _previous = null;
    
    
    /**
     * Creates a new BaseDOMXPathNode
    **/
    BaseDOMXPathNode() {
        super();
    } //-- BaseDOMXPathNode
    
    
    
    /**
     * Returns the first child node of this node, or null if
     * the node has no children. Child nodes are only returned
     * for an element or root node.
     *
     * @return The first child node of this node, or null
    **/
    public XPathNode getFirstChild() {
        return null;
    } //-- getFirstChild


    /**
     * Returns true if this node has any child nodes.
     *
     * @return True if this node has any child nodes.
    **/
    public boolean hasChildNodes() {
        return false;
    } //-- hasChildNodes


    /**
     * Returns the next sibling node in document order, or null
     * if this node is the last node. This method is value for
     * any node except the root node.
     *
     * @return The next sibling node in document order, or null
    **/
    public XPathNode getNext() {
        return _next;
    } //-- next


    /**
     * Returns the previous sibling node in document order, or null
     * if this node is the first node. This method can is valid for
     * any node except the root node.
     *
     * @return The previous sibling node in document order, or null
    **/
    public XPathNode getPrevious() {
        return _previous;
    }

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
    public XPathNode getFirstAttribute() {
        return null;
    } //-- getFirstAttribute


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
    public String getAttribute( String uri, String local ) {
        return null;
    } //-- getAttribute


    /**
     * Returns the first in a list of namespace nodes, or null
     * if the node has no namespaces. This method is valid only
     * for the element node.
     *
     * @return The first in a list of namespace nodes, or null
    **/
    public XPathNode getFirstNamespace() {
        return null;
    } //-- getFirstNamespace


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
    public String getNamespaceURI() {
        return null;
    } //-- getNamespaceURI


    /**
     * Returns the parent node, or null if the node has no parent.
     * This method is valid on all node types except the root node.
     * Attribute and namespace nodes have the element as their parent
     * node.
     *
     * @return The parent node, or null
    **/
    public XPathNode getParentNode() {
        return _parent;
    } //-- getParentNode


    /**
     * Returns the root node.
     *
     * @return The root node
    **/
    public XPathNode getRootNode() {
        if (_parent == null) return null;
        return _parent.getRootNode();
    } //-- getRootNode


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
    public String getNamespaceURI( String prefix ) {
        if (_parent == null) return null;
        return _parent.getNamespaceURI( prefix );
    } //-- getNamespaceURI


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
    public String getNamespacePrefix( String uri ) {
        if (_parent == null) return null;
        return _parent.getNamespacePrefix(uri);
    } //-- getNamespacePrefix


    /* protected methods */
    
    /**
     * Sets the "next" XPathNode sibling
     *
     * @param node the XPathNode which is the "next" sibling of this
     * XPathNode
    **/    
    void setNext(BaseDOMXPathNode node) {
        _next = node;
    } //-- setNext

    /**
     * Sets the parent XPathNode
     *
     * @param node the XPathNode which is the parent of this
     * XPathNode
    **/    
    void setParent(BaseDOMXPathNode node) {
        _parent = node;
    } //-- setParent

    /**
     * Sets the "previous" XPathNode sibling
     *
     * @param node the XPathNode which is the "previous" sibling of this
     * XPathNode
    **/    
    void setPrevious(BaseDOMXPathNode node) {
        _previous = node;
    } //-- setPrevious
    
    
} //-- BaseDOMXPathNode

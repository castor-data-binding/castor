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


package org.exolab.adaptx.xslt.dom;

import org.exolab.adaptx.xpath.XPathNode;

/**
 * A simple implementation of XPathNode which allows children
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
abstract class ParentNode extends BaseNode {


    /**
     * A reference to the first child of this node
    **/
    private BaseNode _firstChild = null;
    
    /**
     * A reference to the last child of this node
    **/
    private BaseNode _lastChild  = null;
    
    /**
     * Creates a new ParentNode
     *
     * @param namespace the namespace URI for this node. [May be null]
     * @param localName the local-name of this node. [Cannot be null]
    **/
    ParentNode(String namespace, String localName) {
        
        super(namespace, localName);
        
    } //-- ParentNode

    /**
     * Returns the first child node of this node, or null if
     * the node has no children. Child nodes are only returned
     * for an element or root node.
     *
     * @return The first child node of this node, or null
    **/
    public XPathNode getFirstChild() {
        return _firstChild;
    } //-- getFirstChild


    /**
     * Returns true if this node has any child nodes.
     *
     * @return True if this node has any child nodes.
    **/
    public boolean hasChildNodes() {
        return (_firstChild != null);
    }

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
    public String getStringValue() {
        
        if (_firstChild == null) return "";
        
        StringBuffer sb = new StringBuffer();
        
        XPathNode child = getFirstChild();
        while (child != null) {
            sb.append( child.getStringValue() );
            child = child.getNext();
        }
        return sb.toString();
        
    } //-- getStringValue


    /* Local Methods */
    
    /**
     * Adds the given XPathNode to this ParentNode. The XPathNode
     * must come from this implementation, or an Exception will
     * be thrown.
     *
     * @param attr the Attribute to add
    **/
    public void addChild(XPathNode node) {
        
        if (node == null) return;
        
        if (! (node instanceof BaseNode )) {
            throw new IllegalStateException("The given XPathNode is from" +
              " a different implementation.");
        }
            
        BaseNode child = (BaseNode)node;
        child.setParent( this );
        child.setNext( null );
        if (_firstChild == null) {
            _firstChild = child;
            _lastChild = child;
            _lastChild.setPrevious( null );
        }
        else {            
            //-- normalize text nodes if necessary
            if ((_lastChild.getNodeType() == XPathNode.TEXT) &&
                (child.getNodeType() == XPathNode.TEXT)) 
            {
                Text text = (Text) _lastChild;
                String value = text.getStringValue();
                text.setValue(value + child.getStringValue());
            }
            else {
                child.setPrevious( _lastChild );
                _lastChild.setNext( child );
                _lastChild = child;
            }
        }
    } //-- addChild

} //-- ParentNode

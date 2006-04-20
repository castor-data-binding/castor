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
 * An interface for Parent XPathNodes. There are only add
 * methods...and no remove methods, since this interface
 * is used for an XPathNode implementation which simply wraps
 * a W3C DOM implementation.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
abstract class ParentDOMXPathNode extends BaseDOMXPathNode {

    /**
     * A reference to the first child XPathNode
    **/
    BaseDOMXPathNode _firstChild = null;
    
    /**
     * A reference to the last child XPathNode
    **/
    BaseDOMXPathNode _lastChild = null;
    
    /**
     * Creates a new ParentDOMXPathNode
    **/
    ParentDOMXPathNode() {
        super();
    } //-- ParentDOMXPathNode 
    
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
    } //-- hasChildNodes
    
    /* protected methods */
    
    /**
     * Adds the given child XPathNode to this XPathNode. Adding
     * the node to this Parent will reset any parent node, and
     * any siblings.
     *
     * @param node the XPathNode to add as a child
    **/
    void addChild(BaseDOMXPathNode node) {
        
        node.setNext(null);
        node.setParent(this);
        
        if (_firstChild == null) {
            _firstChild = node;
            _lastChild = node;
            _firstChild.setPrevious(null);
        }
        else {
            _lastChild.setNext(node);
            node.setPrevious(_lastChild);
            _lastChild = node;
        }
    } //-- addChild
    
} //-- ParentDOMXPathNode

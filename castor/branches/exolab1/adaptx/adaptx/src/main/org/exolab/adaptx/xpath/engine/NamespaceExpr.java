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


package org.exolab.adaptx.xpath.engine;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;


/**
 * Represents a Namespace Expression
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @author <a href="mailto:blondeau@intalio.com">David Blondeau</a>
**/
class NamespaceExpr extends NodeExpression {

    
    // Match is done on the prefix. The namespace axis does not seems to be used
    // with name tests, it is often used with the wild card and a predicate.
    private static final String WILD_CARD = "*";

    private boolean _isWild = false;
    
    private String _prefix;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    public NamespaceExpr() {
    } //-- NamespaceExpr
    
    public NamespaceExpr(String qname)
        throws XPathException
    {
        if (qname == null)
            throw new XPathException( "Argument qname is null" );
            
        if (qname.equals(WILD_CARD)) {
            _prefix = WILD_CARD;
            _isWild = true;
        } 
        else {
            int index = qname.indexOf(':');
            if (index >= 1) {
                _prefix = qname.substring(index + 1);
                if (!(qname.substring(0, index).equals("xmlns"))) {
                    throw new XPathException(
                        "The prefix of the namespace declaration is not xmlns");
                }
            } 
            else {
                _prefix = qname;
            }
        }
    } //-- NamespaceExpr
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    
    /**
     * Returns the String representation of this NamespaceExpr
    **/
    public String toString() {
        return _prefix;
    } //-- toString
    
    
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public XPathResult evaluate(XPathContext context)
        throws XPathException 
    {
        XPathNode node = context.getNode();
        NodeSet nodeSet = context.newNodeSet();
        if (_prefix.equals(WILD_CARD)) 
            addNamespacesWild(node, nodeSet);
        else
            addNamespaces(node, nodeSet);
        return nodeSet;
    } //-- evaluate
    
    /**
     * Adds all namespace nodes that are in-scope for the given XPathNode and
     * match the prefix declared to the given NodeSet
     *
     * @param node the XPathNode to obtain namespaces for
     * @param nodeSet the NodeSet to add namespace nodes to
    **/
    private void addNamespaces(XPathNode node, NodeSet nodeSet) {
        if (node == null) return;
        XPathNode namespace = node.getFirstNamespace();
        while (namespace != null) {
            String nsPrefix = namespace.getLocalName();            
            if (((nsPrefix == null) && (_prefix.length() == 0))
                || nsPrefix.equals(_prefix)) {
                nodeSet.add(namespace);
            }
            namespace = namespace.getNext();
        }
        addNamespaces(node.getParentNode(), nodeSet);
    } //-- addNamespaces

    /**
     * Adds all namespace nodes in-scope for the given XPathNode to the given 
     * NodeSet
     *
     * @param node the XPathNode to obtain namespaces for
     * @param nodeSet the NodeSet to add namespace nodes to
    **/
    private void addNamespacesWild(XPathNode node, NodeSet nodeSet) {
        if (node == null) return;
        XPathNode namespace = node.getFirstNamespace();
        while (namespace != null) {
            nodeSet.add(namespace);
            namespace = namespace.getNext();
        }
        addNamespacesWild(node.getParentNode(), nodeSet);
    } //-- addNamespacesWild
    
    public short getExprType() { return XPathExpression.STEP; }
    
    
    /**
     * Returns the type of this NodeExpr
     * @return the type of this NodeExpr
    **/
    public short getNodeExprType() { return NodeExpression.NAMESPACE_EXPR; }
    
    
    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @exception XPathException when an error occurs during
     * evaluation
    **/
    public boolean matches(XPathNode node, XPathContext context)
        throws XPathException
    {
        if (node == null) return false;
        
        if (node.getNodeType() == XPathNode.NAMESPACE) {
            if (_prefix.equals(WILD_CARD)) return true;
            String nsPrefix = node.getLocalName();            
            return (((nsPrefix == null) && (_prefix.length() == 0))
                || nsPrefix.equals(_prefix));
        }
        return false;
    } //-- matches
    
} //-- NamespaceExpr

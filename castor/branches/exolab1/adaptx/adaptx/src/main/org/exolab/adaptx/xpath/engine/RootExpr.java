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
 * Represents an RootExpr
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class RootExpr extends PathExpr {
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    public RootExpr() {
        super();
    }
    
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
        if (context == null) return context.newNodeSet();
        XPathNode node = context.getNode();
        NodeSet nodes = context.newNodeSet(1);
        node = node.getRootNode();
        if (node != null) nodes.add(node);
        return nodes;
    } //-- evaluate
    
    /**
     * Determines the priority of a PatternExpr as follows:
     * <PRE>
     *  From the 19991116 XSLT 1.0 Recommendation:
     *  + If the pattern has the form of a QName preceded by a
     *    ChildOrAttributeAxisSpecifier or has the form 
     *    processing-instruction(Literal) then the priority is 0.
     *  + If the pattern has the form NCName:* preceded by a 
     *    ChildOrAttributeAxisSpecifier, then the priority is -0.25
     *  + Otherwise if the pattern consists of just a NodeTest 
     *    preceded by a ChildOrAttributeAxisSpecifier then the
     *    priority is -0.5
     *  + Otherwise the priority is 0.5
     * </PRE>
     * @return the priority for this PatternExpr
    **/
    public double getDefaultPriority() {
        return 0.5;
    } //-- getDefaultPriority
    
    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context node.
     * @param node the node to determine a match for
     * @param context the Node which represents the current context
     * @param exprContext the ExprContext which has additional
     * information sometimes needed for evaluating XPath expressions
     * @return true if the given node is matched by this MatchExpr
     * @exception InvalidExprException when an invalid expression is
     * encountered during evaluation
    **/
    public boolean matches(XPathNode node, XPathContext context)
        throws XPathException 
    {
        if (node == null) return false;
        return (node.getNodeType() == XPathNode.ROOT);
    } //-- matches
    
    public String toString() {
        return Names.ROOT_EXPR;
    } //-- toString
    
} //-- RootExpr

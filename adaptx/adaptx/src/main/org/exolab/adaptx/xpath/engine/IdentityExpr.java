/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 */


package org.exolab.adaptx.xpath.engine;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;


/**
 * Represents an IdentityExpr
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
class IdentityExpr extends NodeExpression {
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    
    public IdentityExpr() {
        super();
    } //-- IdentityExpr
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    
    /**
     * Returns the String representation of this NodeExpr
    **/
    public String toString() {
        return ".";
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
        if (node != null) {
            NodeSet nodeSet = context.newNodeSet( node );
            return nodeSet;
        }
        else return context.newNodeSet();
        
    } //-- evaluate
    
    public short getExprType() { return XPathExpression.STEP; }
        
    /**
     * Returns the type of this NodeExpr
     * @return the type of this NodeExpr
    **/
    public short getNodeExprType() { return NodeExpression.IDENTITY_EXPR; }
    
    
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
        return (node == context.getNode());
    } //-- matches
    
} //-- IdentityExpr

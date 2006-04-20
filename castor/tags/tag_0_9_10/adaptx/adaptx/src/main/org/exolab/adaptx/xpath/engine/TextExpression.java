/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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
import org.exolab.adaptx.xpath.expressions.NodeExpression;

/**
 * A class that represents the XPath text() node test
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
class TextExpression
    extends NodeExpressionImpl 
{
    

    /**
     * Creates a new TextExpression
     */
    public TextExpression()
    {
        super();
    } //-- TextExpression
    

    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
     */
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {        
        NodeSet nodeSet = context.newNodeSet();
        XPathNode node = context.getNode();
        if ( node == null )
            return nodeSet;

        XPathNode child;
        child = node.getFirstChild();
        while ( child != null ) {
            if ( child.getNodeType() == XPathNode.TEXT )
                nodeSet.add( child );
            child = child.getNext();
        }
        return nodeSet;
    } //-- evaluate

    
    public short getExprType ()
    { 
        return XPathExpression.NODE_TEST;
    }


    public short getNodeExprType ()
    { 
        return NodeExpression.TEXT_EXPR;
    }
    

    public String toString()
    {
        return Names.TEXT_FN + "()";
    }


    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @exception XPathException when an error occurs during
     * evaluation
     */
    public boolean matches( XPathNode node, XPathContext context )
        throws XPathException
    {
        return ( node != null && node.getNodeType() == XPathNode.TEXT );
    } //-- matches

    
} //-- TextExpression
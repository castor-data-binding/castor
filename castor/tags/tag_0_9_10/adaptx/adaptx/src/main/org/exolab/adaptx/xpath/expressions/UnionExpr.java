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


package org.exolab.adaptx.xpath.expressions;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;

/**
 * An abstract class that represents an XPath Union expression
 *
 * <PRE>
 * UnionExpr ::= PathExpr | (PathExpr '|' UnionExpr)
 * </PRE>
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public abstract class UnionExpr
    implements XPathExpression, MatchExpression
{


    /**
     * Returns the PathExpr of this UnionExpr. A UnionExpr
     * is defined by XPath 1.0 as:
     *
     * <PRE>
     * UnionExpr ::= PathExpr | (PathExpr '|' UnionExpr)
     * </PRE>
     *
     * @return the PathExpr of this UnionExpr.
     */
    public abstract PathExpr getPathExpr();
    
    /**
     * Returns the UnionExpr that this UnionExpr is in union
     * with. A UnionExpr is defined by XPath 1.0 as:
     *
     *
     * <PRE>
     * UnionExpr ::= PathExpr | (PathExpr '|' UnionExpr)
     * </PRE>
     *
     * @return the UnionExpr that this UnionExpr is in union 
     * with, or null if this is UnionExpr only contains
     * a PathExpr.
     * @see #getPathExpr
     */
    public abstract UnionExpr getUnionExpr();
    
    
    /**
     * Returns the type of Expr this Expr represents
     *
     * @return the type of Expr this Expr represents
     */
    public final short getExprType() {
        return XPathExpression.UNION_EXPR;
    } //-- getExprType
    
    /**
     * Retrieves the PathExpr that matches the given node. If more
     * than one PathExpr matches the given node, the most specific
     * PathExpr will be returned.
     *
     * @param node the node to test for matching
     * @return the matching PathExpr or null if none match
     */
    public PathExpr getMatchingExpr
        (XPathNode node, XPathContext context) 
        throws XPathException
    {
        PathExpr pathExpr = getPathExpr();
        PathExpr match = null;
        if (pathExpr != null) {
            if (pathExpr.matches(node, context))
                match = pathExpr;
        }
        if (getUnionExpr() != null) {
            pathExpr = getUnionExpr().getMatchingExpr(node, context);
            if (pathExpr != null) {
                if (match == null) return pathExpr;
                else {
                    if (pathExpr.getDefaultPriority() > 
                        match.getDefaultPriority())
                        match = pathExpr;
                }
            }
        }
        return match;
    } //-- getMatchingExpr
    
    
    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     *
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @exception XPathException when an error occurs during
     * evaluation
     */
    public abstract boolean matches(XPathNode node, XPathContext context)
        throws XPathException;
    
} // -- UnionExpr

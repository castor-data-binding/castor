/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
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
 * An abstract class representing an XPath Location Path 
 * expression.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public abstract class PathExpr 
    implements XPathExpression, MatchExpression
{

      //------------------/    
     //- Public Methods -/
    //------------------/    

    
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public abstract XPathResult evaluate( XPathContext context )
        throws XPathException;
        
    /**
     * Returns the XPathExpression type
     *
     * @return the XPathExpression type
     * @see org.exolab.adaptx.xpath.XPathExpression
     */
    public final short getExprType()
    {
        return XPathExpression.PATH_EXPR;
    } //-- getExprType
    

    /**
     * Returns the PathComponent for the this PathExpr, either
     * a FilterExpr or LocationStep.
     *
     * @return the PathComponent for this PathExpr
     */
    public abstract PathComponent getPathComponent();
    

    public abstract PathExpr getSubPath();

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
     */
    public abstract double getDefaultPriority();
    /*
    {
        if ( _subPath != null )
            return 0.5;
        return _filter.getDefaultPriority();
    } //-- getDefaultPriority
    */
    
    
    /**
     * Returns true if this PathExpr is an absolute expression, 
     * otherwise false (ie. it's a relative expression).
     *
     * @return true if this PathExpr is an absolute expression.
     */ 
    public abstract boolean isAbsolute();
    

    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @exception XPathException when an error occurs during
     * evaluation
    **/
    public abstract boolean matches( XPathNode node, XPathContext context )
        throws XPathException;
        
} // -- PathExpr

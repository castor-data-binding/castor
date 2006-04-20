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

import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathResult;

import org.exolab.adaptx.xpath.expressions.GroupedExpression;
import org.exolab.adaptx.xpath.expressions.PrimaryExpr;

/**
 * Represents the XPath primary expression: '(' + expr + ')'
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class InnerExpr 
    extends PrimaryExpr implements GroupedExpression
{
    
    private XPathExpression _expr = null;

    /**
     * Creates a new InnerExpr which will throw
     * an XPathException
    **/
    InnerExpr() {
        super(PrimaryExpr.EXPR);
    } //-- InnerExpr
    
    /**
     * Creates a new InnerExpr which evaluates to the given
     * expression
     * 
     * @param expr the XPathExpression to evaluate
    **/
    InnerExpr(XPathExpression expr) {
        super(PrimaryExpr.EXPR);
        _expr = expr;
    } //-- InnerExpr
        
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        if (_expr != null)
            return _expr.evaluate( context );
            
        throw new XPathException("missing expression after '('");
    } //-- evaluate
    
    /**
     * Returns the underlying expression of this grouping
     * 
     * @param returns the underlying XPath expresion 
     */
    public XPathExpression getExpression() {
        return _expr;
    } //-- getExpression
    
    /**
     * Returns the String representation of this PrimaryExpr
     * @return the String representation of this PrimaryExpr
    **/
    public String toString() {
        
        if (_expr != null)
            return '(' + _expr.toString() + ')';
       
        return "()";
    } //-- toString
    
} //-- InnerExpr


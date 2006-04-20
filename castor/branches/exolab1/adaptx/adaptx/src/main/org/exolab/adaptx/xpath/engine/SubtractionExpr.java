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
import org.exolab.adaptx.xpath.NumberResult;
import org.exolab.adaptx.xpath.XPathException;


/**
 * Represents an Subtraction Expr. 
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
class SubtractionExpr extends BinaryExpr {

    
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new Subtraction expression
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this BinaryExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this BinaryExpr
    **/
    public SubtractionExpr(XPathExpression leftExpr, XPathExpression rightExpr) {
        super(leftExpr, rightExpr);
    } //-- SubtractionExpr 
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Returns the type of Expr this Expr represents
     * @return the type of Expr this Expr represents
    **/
    public short getExprType() {
        return XPathExpression.NUMBER;
    } //-- getExprType
    
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
        double value = 0;
        
        XPathExpression leftExpr = getLeftSide();
        XPathExpression rightExpr = getRightSide();
        
        if ((leftExpr == null) || (rightExpr == null))
            return NumberResult.NaN;

        XPathResult rResult = rightExpr.evaluate( context );
        XPathResult lResult = leftExpr.evaluate( context );
        return new NumberResult(lResult.numberValue()-rResult.numberValue());
    } //-- evaluate
    
    /**
     * Returns the String representation of this Expr
     * @return the String representation of this Expr
    **/
    public String toString() {
        
        
        StringBuffer sb = new StringBuffer();
        XPathExpression expr = getLeftSide();
        
        if (expr != null)
            sb.append(expr.toString());
        else
            sb.append("null");
        sb.append(" - ");
        
        expr = getRightSide();
        
        if (expr != null)
            sb.append(expr.toString());
        else 
            sb.append("null");
        
        return sb.toString();
    } //-- toString
    
} //-- SubtractionExpr

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
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathException;


/**
 * Represents an AndExpr
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class AndExpr extends XPathExpression {

    
    private static String AND = " and ";
    private static String NULL = "null";
    
    private XPathExpression leftExpr = null;
    private XPathExpression rightExpr = null;
    
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    public AndExpr(XPathExpression leftExpr, XPathExpression rightExpr) {
        if ( leftExpr == null )
            throw new IllegalArgumentException( "Argument leftExpr is null" );
        if ( rightExpr == null )
            throw new IllegalArgumentException( "Argument rightExpr is null" );
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    } //-- AndExpr 
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Returns the type of Expr this Expr represents
     * @return the type of Expr this Expr represents
    **/
    public short getExprType() {
        return XPathExpression.BOOLEAN;
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
        if ((leftExpr == null) || (rightExpr == null))
            return BooleanResult.FALSE;
            
        if (!leftExpr.evaluate(context).booleanValue())
            return BooleanResult.FALSE;
            
        return BooleanResult.from( rightExpr.evaluate( context ) );
    } //-- evaluate
    
    
    /**
     * Returns the String representation of this AndExpr
     * @return the String representation of this AndExpr
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (leftExpr != null) sb.append(leftExpr.toString());
        else sb.append(NULL);
        sb.append(AND);
        if (rightExpr != null) sb.append(rightExpr.toString());
        else sb.append(NULL);
        return sb.toString();
    } //-- toString
    /* */
} //-- Expr

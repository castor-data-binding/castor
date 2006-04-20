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
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathException;


/**
 * Represents an OrExpr
 * @author Keith Visco (kvisco@ziplink.net)
**/
class OrExpr extends XPathExpression {

    
    private static String OR = " or ";
    private static String NULL = "null";
    
    private XPathExpression leftExpr = null;
    private XPathExpression rightExpr = null;
    
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    public OrExpr(XPathExpression leftExpr, XPathExpression rightExpr) {
        if ( leftExpr == null )
            throw new IllegalArgumentException( "Argument leftExpr is null" );
        if ( rightExpr == null )
            throw new IllegalArgumentException( "Argument rightExpr is null" );
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    } //-- OrExpr 
    
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
        XPathResult result;

        if (leftExpr != null) {
            result = leftExpr.evaluate(context);
            if (result.booleanValue())
                return BooleanResult.TRUE;
        }
        if (rightExpr != null) {
            result = rightExpr.evaluate(context);
            if (result.booleanValue())
                return BooleanResult.TRUE;
        }
        return BooleanResult.FALSE;
        
    } //-- evaluate
    
    
    /**
     * Returns the String representation of this AndExpr
     * @return the String representation of this AndExpr
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (leftExpr != null) sb.append(leftExpr.toString());
        else sb.append(NULL);
        sb.append(OR);
        if (rightExpr != null) sb.append(rightExpr.toString());
        else sb.append(NULL);
        return sb.toString();
    } //-- toString
    /* */
} //-- OrExpr

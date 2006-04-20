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
import org.exolab.adaptx.xpath.NumberResult;
import org.exolab.adaptx.xpath.XPathException;


/**
 * Represents an MultiplicativeExpr
 * @author Keith Visco (kvisco@ziplink.net)
**/
class MultiplicativeExpr extends XPathExpression {

    public static final short MULTIPLY      = 0;
    public static final short DIVIDE        = 1;
    public static final short MODULUS       = 2;
    public static final short QUOTIENT      = 3;
    
    private XPathExpression leftExpr = null;
    private XPathExpression rightExpr = null;
    
    private short op = MULTIPLY;
    
    private final static String[] ops = new String[] { Names.MULTIPLY_OP,Names.DIV_OPNAME,Names.MOD_OPNAME,Names.QUO_OPNAME };
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new MultiplicativeExpr using the default operator
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this MultiplicativeExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this MultiplicativeExpr
     * <BR><B>Note:</B> the default operator is MultiplicativeExpr.MULITPLY
    **/
    public MultiplicativeExpr(XPathExpression leftExpr, XPathExpression rightExpr) {
        if ( leftExpr == null )
            throw new IllegalArgumentException( "Argument leftExpr is null" );
        if ( rightExpr == null )
            throw new IllegalArgumentException( "Argument rightExpr is null" );
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    } //-- MultiplicativeExpr 
    
    /**
     * Creates a new MultiplicativeExpr
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this MultiplicativeExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this MultiplicativeExpr
     * @param additiveOp the additive operator for this MultiplicativeExpr
     * @exception InvalidExprException when the additive operator is 
     * invalid 
    **/
    public MultiplicativeExpr
        (XPathExpression leftExpr, XPathExpression rightExpr, short operator) 
        throws XPathException
    {
        this(leftExpr,rightExpr);
        if ((operator < 0) || (operator >= ops.length))
            throw new XPathException("invalid operator for multiplicative expression");
        this.op = operator;
    } //-- MultiplicativeExpr 
    
    /**
     * Creates a new MultiplicativeExpr
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this MultiplicativeExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this MultiplicativeExpr
     * @param additiveOp the additive operator for this MultiplicativeExpr
     * @exception InvalidExprException when the additive operator is 
     * invalid 
    **/
    public MultiplicativeExpr
        (XPathExpression leftExpr, XPathExpression rightExpr, String operator) 
        throws XPathException
    {
        this(leftExpr,rightExpr);
        this.op = -1;
        if (operator != null) {
            for (op = 0; op<ops.length ; ++op)
                if(ops[op].equals(operator))
                    break;
        }
        if ((this.op < 0) || (this.op >= ops.length))
            throw new XPathException("invalid operator for multiplicative expression");
    } //-- MultiplicativeExpr
        
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
        
        if ((leftExpr == null) || (rightExpr == null))
            return NumberResult.NaN;

        double lValue = leftExpr.evaluate(context).numberValue();
        double rValue = rightExpr.evaluate(context).numberValue();
        
        switch(op) {
            case MULTIPLY:
                value = lValue * rValue;
                break;
            case DIVIDE:
                value = lValue / rValue;
                break;
            case MODULUS:
                value = (lValue % rValue);
                break;
            case QUOTIENT:
                value = Math.floor(lValue / rValue);
                break;
            default:
                break;
        }
        return new NumberResult(value);
    } //-- evaluate
    
    public static boolean isMultiplicativeOperator(String operator) {
        if (operator == null) return false;
        for(int i = 0 ; i<ops.length ; ++i)
            if(ops[i].equals(operator))
                return true;
        return false;
    } //-- isMulitplicativeOperator
    
    /**
     * Returns the String representation of this EqualityExpr
     * @return the String representation of this EqualityExpr
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (leftExpr != null)
            sb.append(leftExpr.toString());
        else
            sb.append("null");
        sb.append(" ");
        sb.append(ops[op]);
        sb.append(" ");
        if (rightExpr != null)
            sb.append(rightExpr.toString());
        else sb.append("null");
        
        return sb.toString();
    } //-- toString
    
    /* */
} //-- MultiplicativeExpr

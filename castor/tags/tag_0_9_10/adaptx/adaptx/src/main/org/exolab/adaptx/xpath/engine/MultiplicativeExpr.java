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
import org.exolab.adaptx.xpath.NumberResult;
import org.exolab.adaptx.xpath.XPathException;

import org.exolab.adaptx.xpath.expressions.BinaryExpr;
import org.exolab.adaptx.xpath.expressions.Operator;

/**
 * Represents an MultiplicativeExpr
 *
 * @author Keith Visco (kvisco@intalio.com)
 */
class MultiplicativeExpr 
    extends BinaryExprImpl 
{

    public static final short MULTIPLY      = 0;
    public static final short DIVIDE        = 1;
    public static final short MODULUS       = 2;
    public static final short QUOTIENT      = 3;
    
    private short op = MULTIPLY;
    
    private final static String[] ops = new String[] { 
        Names.MULTIPLY_OP,
        Names.DIV_OPNAME,
        Names.MOD_OPNAME,
        Names.QUO_OPNAME 
    };
    
    private final static NumericOperator[] _operators = new NumericOperator[] {
        new NumericOperator(new MultiplyOperation(), Operator.MULTIPLY_OPERATOR),
        new NumericOperator(new DivideOperation(), Operator.DIVIDE_OPERATOR),
        new NumericOperator(new ModulusOperation(), Operator.MODULUS_OPERATOR),
        new NumericOperator(new QuotientOperation(), Operator.QUOTIENT_OPERATOR)
    };
    
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
        super(leftExpr, rightExpr);
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
        super(leftExpr,rightExpr);
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
        super(leftExpr,rightExpr);
        this.op = -1;
        if (operator != null) {
            for (short opIdx = 0; opIdx < ops.length; opIdx++) {
                if(ops[opIdx].equals(operator)) {
                    op = opIdx;
                    break;
                }
            }
        }
        
        if (op == -1) {
            String err = "The operator '" + operator + 
                "' is invalid for a muliplicative expression.";
            throw new XPathException(err);
        }
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
        Operator operator = _operators[op];
        return operator.execute(getLeftSide(), getRightSide(), context);
    } //-- evaluate
    
    /**
     * Returns the operator for this binary expression
     *
     * @return the operator for this binary expression
     */
    public Operator getOperator() {
        return _operators[op];
    } //-- getOperator
    
    public static boolean isMultiplicativeOperator(String operator) {
        if (operator == null) return false;
        for(int i = 0 ; i<ops.length ; ++i)
            if(ops[i].equals(operator))
                return true;
        return false;
    } //-- isMulitplicativeOperator
    
        
    /**
     * The divide operation
     */
    static class DivideOperation implements NumericOperation {
        
        /** 
         * Executes the operation on the given values
         */
        public double execute(double left, double right) {
            return left/right;
        } //-- execute
        
        public String toString() {
            return Names.DIV_OPNAME;
        }
        
    } //-- DivideOperation
    
    
    /**
     * The modulus operation
     */
    static class ModulusOperation implements NumericOperation {
        
        /** 
         * Executes the operation on the given values
         */
        public double execute(double left, double right) {
            return left % right;
        } //-- execute
        
        public String toString() {
            return Names.MOD_OPNAME;
        }
        
    } //-- ModulusOperation
    
    /**
     * The multiply operation
     */
    static class MultiplyOperation implements NumericOperation {
        
        /** 
         * Executes the operation on the given values
         */
        public double execute(double left, double right) {
            return left*right;
        } //-- execute
        
        public String toString() {
            return Names.MULTIPLY_OP;
        }
        
    } //-- MultiplyOperation
    
    /**
     * The quotient operation
     */
    static class QuotientOperation implements NumericOperation {
        
        /** 
         * Executes the operation on the given values
         */
        public double execute(double left, double right) {
            return Math.floor(left / right);
        } //-- execute
        
        public String toString() {
            return Names.QUO_OPNAME;
        }
        
    } //-- QuotientOperation
    
    
    
} //-- MultiplicativeExpr

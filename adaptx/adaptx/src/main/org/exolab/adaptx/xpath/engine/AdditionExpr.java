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

import org.exolab.adaptx.xpath.expressions.Operator;


/**
 * Represents an Addition Expr. 
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class AdditionExpr extends BinaryExprImpl {

    static final String OP_STRING = " + ";
    
    private static final Operator _operator = new AddOperator();
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new Addition expression
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this AdditiveExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this AdditiveExpr
    **/
    public AdditionExpr(XPathExpression leftExpr, XPathExpression rightExpr) {
        super(leftExpr, rightExpr);
    } //-- AdditionExpr 

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
        return _operator.execute(getLeftSide(), getRightSide(), context);
        
    } //-- evaluate
    
    /**
     * Returns the operator for this binary expression
     *
     * @return the operator for this binary expression
     */
    public Operator getOperator() {
        return _operator;
    } //-- getOperator
    
    /**
     * The implementation of the "+" (addition) Operator.
     */
    static class AddOperator implements Operator {
        
        /**
         * Returns the type for this Operator. The operator
         * type may be one of the pre-defined types, or
         * a user-defined type.
         *
         * @return the operator type
         */
        public int getOperatorType() {
            return Operator.ADD_OPERATOR;
        } //-- getOperator
        
        /**
         * Executes this operator on the given expressions
         *
         * @param left the left-side expression
         * @param right the right-side expression
         * @param context the XPathContext for expression evaluation
         * @return the XPathResult
         * @throws XPathException when an error occurs during execution
         */
        public XPathResult execute
            (XPathExpression left, XPathExpression right, XPathContext context)
            throws XPathException
        {
            if ((left == null) || (right == null))
                return NumberResult.NaN;

            XPathResult rResult = right.evaluate(context);
            XPathResult lResult = left.evaluate(context);
            
            return new NumberResult(lResult.numberValue()+rResult.numberValue());
            
        } //-- execute
        
        /**
         * Executes this operator on the given XPath values
         *
         * @param left the left-side expression
         * @param right the right-side expression
         * @return the XPathResult
         * @throws XPathException when an error occurs during execution
         */
        public XPathResult execute
            (XPathResult left, XPathResult right)
            throws XPathException
        {
            if ((left == null) || (right == null))
                return NumberResult.NaN;

            return new NumberResult(left.numberValue()+right.numberValue());
            
        } //-- execute
            
        /**
         * Returns the string representation of this operator
         */
        public String toString() {
            return OP_STRING;
        }
    } //-- AddOperator
    
} //-- AdditionExpr


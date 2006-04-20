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

import org.exolab.adaptx.xpath.expressions.BinaryExpr;
import org.exolab.adaptx.xpath.expressions.Operator;

/**
 * Represents an AndExpr
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class AndExpr extends BinaryExprImpl {

    
    private static String AND = " and ";
    
    
    /**
     * The AndOperator instance
     */
    private static final Operator _operator = new AndOperator();
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    public AndExpr(XPathExpression leftExpr, XPathExpression rightExpr) {
        super(leftExpr, rightExpr);
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
     * Returns the operator for this binary expression
     *
     * @return the operator for this binary expression
     */
    public Operator getOperator() {
        return _operator;
    } //-- getOperator

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
        return _operator.execute(leftExpr, rightExpr, context);
    } //-- evaluate
    
    
    /**
     * The implementation of the "and" Operator.
     */
    static class AndOperator implements Operator {
        
        /**
         * Returns the type for this Operator. The operator
         * type may be one of the pre-defined types, or
         * a user-defined type.
         *
         * @return the operator type
         */
        public int getOperatorType() {
            return Operator.AND_OPERATOR;
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
                return BooleanResult.FALSE;
                
            if (!left.evaluate(context).booleanValue())
                return BooleanResult.FALSE;
                
            return BooleanResult.from( right.evaluate( context ) );
            
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
                return BooleanResult.FALSE;
                
            if (!left.booleanValue())
                return BooleanResult.FALSE;
                
            return BooleanResult.from( right.booleanValue() );
            
        } //-- execute
            
        /**
         * Returns the string representation of this operator
         */
        public String toString() {
            return AND;
        }
    } //-- AndOperator
    
} //-- class: AndExpr

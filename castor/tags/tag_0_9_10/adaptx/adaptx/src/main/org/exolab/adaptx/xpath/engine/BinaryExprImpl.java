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
import org.exolab.adaptx.xpath.XPathException;

import org.exolab.adaptx.xpath.expressions.BinaryExpr;
import org.exolab.adaptx.xpath.expressions.Operator;

/**
 * Represents a simple binary expression. A binary expression is
 * any expression which has a left hand side and a right hand side.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
abstract class BinaryExprImpl implements BinaryExpr {

    final static String NULL = "null";
    
    protected XPathExpression leftExpr   = null;
    protected XPathExpression rightExpr  = null;

      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new BinaryExprImpl
     *
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this BinaryExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this BinaryExpr
     */
    public BinaryExprImpl(XPathExpression leftExpr, XPathExpression rightExpr) {

        if ( leftExpr == null )
            throw new IllegalArgumentException( "Argument leftExpr is null" );

        if ( rightExpr == null )
            throw new IllegalArgumentException( "Argument rightExpr is null" );

        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;

    } //-- BinaryExpr 

    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Returns the operator for this binary expression
     *
     * @return the operator for this binary expression
     */
    public abstract Operator getOperator();
    

    /**
     * Returns the Expr that should be evaluated as the left hand side
     * of this BinaryExpr
     * @return the Expr that should be evaluated as the left hand side
     * of this BinaryExpr
    **/
    public XPathExpression getLeftSide() {

        return leftExpr;

    } //-- getLeftSide

    /**
     * Returns the Expr that should be evaluated as the right hand side
     * of this BinaryExpr
     * @return the Expr that should be evaluated as the right hand side
     * of this BinaryExpr
    **/
    public XPathExpression getRightSide() {

        return rightExpr;

    } //-- getRightSide

    /**
     * Sets the expr that should be evaluated as the left hand side
     * of this BinaryExpr
     * @param expr the Expr that should be evaluated as the left hand
     * side of this BinaryExpr
    **/
    public void setLeftSide(XPathExpression expr) {

        if ( expr == null )
            throw new IllegalArgumentException( "Argument expr is null" );

        this.leftExpr = expr;
    } //-- setLeftSide

    /**
     * Sets the expr that should be evaluated as the right hand side
     * of this BinaryExpr
     * @param expr the Expr that should be evaluated as the right hand
     * side of this BinaryExpr
    **/
    public void setRightSide(XPathExpression expr) {

        if ( expr == null )
            throw new IllegalArgumentException( "Argument expr is null" );
        this.rightExpr = expr;

    } //-- setLeftSide

    /**
     * Returns the String representation of this BinaryExprImpl
     *
     * @return the String representation of this BinaryExprImpl
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        XPathExpression tmp = getLeftSide();
        if (tmp != null) 
            sb.append(tmp.toString());
        else 
            sb.append(NULL);
        
        sb.append(' ');
        
        Operator op = getOperator();
        if (op != null)
            sb.append(op.toString());
        else 
            sb.append('?');
            
        sb.append(' ');
        
            
        tmp = getRightSide();        
        if (tmp != null) 
            sb.append(tmp.toString());
        else 
            sb.append(NULL);
            
        return sb.toString();
    } //-- toString

} //-- class: BinaryExprImpl


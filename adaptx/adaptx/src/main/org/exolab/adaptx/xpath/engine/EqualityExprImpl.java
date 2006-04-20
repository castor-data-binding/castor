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



import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathException;

import org.exolab.adaptx.xpath.expressions.EqualityExpr;
import org.exolab.adaptx.xpath.expressions.Operator;

/**
 * The implementation of EqualityExpr
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
class EqualityExprImpl extends BinaryExprImpl
    implements EqualityExpr, Operator
{


    private short _op = EQUAL;

    private static String[] _ops = new String[] { "=", "<", ">", "<=", ">=", "!=" };
    

      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new EqualityExpr using the default operator
     *
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this EqualityExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this EqualityExpr
     * <BR><B>Note:</B> the default operator is EqualityExpr.EQUALS
     */
    public EqualityExprImpl( XPathExpression leftExpr, XPathExpression rightExpr )
        throws XPathException
    {
        this( leftExpr, rightExpr, EQUAL );
    } //-- EqualityExpr 
    

    /**
     * Creates a new EqualityExpr
     *
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this EqualityExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this EqualityExpr
     * @param op the comparison operator for this EqualityExpr
     * @exception InvalidExprException when the comparison operator is 
     * invalid 
     */
    public EqualityExprImpl( XPathExpression leftExpr, XPathExpression rightExpr, short op )
        throws XPathException
    {
        super(leftExpr, rightExpr);
        
        if ( _op < 0 || _op >= _ops.length )
            throw new XPathException( "invalid operator for relational expression" );
        _op = op;
    } //-- EqualityExpr 
    

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
     */
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        return execute(getLeftSide(), getRightSide(), context);

    } //-- evaluate
    
    /**
     * Executes this operator on the given expressions
     *
     * @param left the left-side expression
     * @param right the right-side expression
     * @param context the XPathContext 
     * @return the XPathResult
     * @throws XPathException when an error occurs during execution
     */
    public XPathResult execute
        (XPathExpression left, XPathExpression right, XPathContext context)
        throws XPathException
    {
        if ( left == null || right == null )
            return BooleanResult.FALSE;
        
        XPathResult lResult = left.evaluate( context );
        XPathResult rResult = right.evaluate( context );
        return execute(lResult, rResult);
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
        (XPathResult lResult, XPathResult rResult)
        throws XPathException
    {
            
        if ( lResult == null || rResult == null )
            return BooleanResult.FALSE;
        
        int lType = lResult.getResultType();
        int rType = rResult.getResultType();

        //-- Evaluate expression 
        //-- (see section 3.4 XPath 1.0 Recommendation)
        if (lType == XPathResult.NODE_SET) {
            //-- at least one object is a node-set
            NodeSet lnodes = (NodeSet)lResult;
            //-- Both objects are node-sets
            if (rType == lType) {
                NodeSet rnodes = (NodeSet)rResult;
                for (int i = 0; i < lnodes.size(); i++) {
                    lResult = new StringResult(lnodes.item(i).getStringValue());
                    for (int j = 0; j < rnodes.size(); j++) {
                        rResult = new StringResult(rnodes.item(j).getStringValue());
                        if (compare(lResult, rResult))
                            return BooleanResult.TRUE;
                    }
                }
            }
            //-- only left-side object is a node-set
            else if (rType != XPathResult.BOOLEAN) {
                for (int i = 0; i < lnodes.size(); i++) {
                    lResult = new StringResult(lnodes.item(i).getStringValue());
                    if (compare(lResult, rResult))
                        return BooleanResult.TRUE;
                }
            }
            return BooleanResult.FALSE;
        }
        else if ((rType == XPathResult.NODE_SET) && 
                 (lType != XPathResult.BOOLEAN)) 
        {
            //-- only right-side object is a node-set
            NodeSet rnodes = (NodeSet)rResult;
            for (int i = 0; i < rnodes.size(); i++) {
                rResult = new StringResult(rnodes.item(i).getStringValue());
                if (compare(lResult, rResult))
                    return BooleanResult.TRUE;
            }
            return BooleanResult.FALSE;
        }
        return BooleanResult.from(compare(lResult,rResult));

    } //-- execute
  
    /**
     * Returns the equality comparison type. 
     * 
     * @return the equality comparison type
     */
    public int getComparisonType() {
        return _op;
    } //-- getComparisonType
    
    /**
     * Returns the type of Expr this Expr represents
     *
     * @return the type of Expr this Expr represents
     */
    public short getExprType()
    {
        return XPathExpression.BOOLEAN;
    } //-- getExprType
    
    /**
     * Returns the operator for this binary expression
     *
     * @return the operator for this binary expression
     */
    public Operator getOperator() {
        return this;
    } //-- getOperator

    /**
     * Returns the type for this Operator. The operator
     * type may be one of the pre-defined types, or
     * a user-defined type.
     *
     * @return the operator type
     */
    public int getOperatorType() {
        return Operator.EQUALITY_OPERATOR;
    }

    public static boolean isRelationalOperator( String operator )
    {
        if ( operator == null )
            return false;
        for( int i = 0 ; i < _ops.length ; ++i )
            if( _ops[ i ].equals( operator ) )
                return true;
        return false;
    } //-- isRelationalOperator
    

    /**
     * Returns the String representation of this EqualityExpr
     *
     * @return the String representation of this EqualityExpr
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if ( leftExpr != null )
            sb.append( leftExpr.toString() );
        else
            sb.append( "null" );
        sb.append( " " );
        sb.append( _ops[_op] );
        sb.append( " " );
        if ( rightExpr != null )
            sb.append( rightExpr.toString() );
        else
            sb.append( "null" );
        return sb.toString();
    } //-- toString
    
    
    /**
     * Compares the given XPathResults for equality based on the
     * type of equality expression represented by this instance.
     * This method assumes that the handling of NodeSets has 
     * already been done and the proper conversion rules
     * of "Section 3.4 paragraph 6" of the XPath 1.0 Recommendation
     * can be applied.
     * 
     * @param lResult the left-side of the expression
     * @param rResult the right-side of the expression
     */
    private boolean compare(XPathResult lResult, XPathResult rResult) {
        
        int lType = lResult.getResultType();
        int rType = rResult.getResultType();
        
        boolean evalResult = false;
        boolean negate     = false;
        
        
        
        switch ( _op ) {
            case NOT_EQUAL:
                negate = true;
                /* Do not break here */
            case EQUAL:
                //-- See section 3.4 Booleans (paragraph 6)
                if ( lType != rType ) {
                    // Check for Boolean First
                    if  ( lType == XPathResult.BOOLEAN || 
                          rType == XPathResult.BOOLEAN )
                        evalResult = ( rResult.booleanValue() == lResult.booleanValue() );
                    // Then Check for Numbers
                    else if ( lType == XPathResult.NUMBER ||
                              rType == XPathResult.NUMBER )
                        evalResult = ( rResult.numberValue() == lResult.numberValue() );
                    // otherwise compare as Strings
                    else
                        evalResult = rResult.stringValue().equals( lResult.stringValue() );
                } else if ( lType == XPathResult.NODE_SET ) {
                    evalResult = rResult.stringValue().equals( lResult.stringValue() );
                } else
                    evalResult = lResult.equals( rResult );
                    
                if (negate) evalResult = (!evalResult);
                break;
            case LESS_THAN:
                evalResult = ( lResult.numberValue() < rResult.numberValue() );
                break;
            case GREATER_THAN:
                evalResult = ( lResult.numberValue() > rResult.numberValue() );
                break;
            case LT_OR_EQUAL:
                evalResult = ( lResult.numberValue() <= rResult.numberValue() );
                break;
            case GT_OR_EQUAL:
                evalResult = ( lResult.numberValue() >= rResult.numberValue() );
                break;
            default:
                evalResult = false;
        } //-- switch
    
        return  evalResult;
        
    } //-- compare
    
} //-- EqualityExpr

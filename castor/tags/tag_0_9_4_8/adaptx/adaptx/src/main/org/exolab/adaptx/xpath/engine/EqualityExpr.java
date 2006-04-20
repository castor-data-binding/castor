/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
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


/**
 * Represents an EqualityExpr
 *
 * @author Keith Visco (keith@kvisco.com)
**/
class EqualityExpr
    extends XPathExpression
{


    public static final short EQUAL            = 0;
    public static final short LESS_THAN        = 1;
    public static final short GREATER_THAN     = 2;
    public static final short LT_OR_EQUAL      = 3;
    public static final short GT_OR_EQUAL      = 4;
    public static final short NOT_EQUAL        = 5;
    
    
    private XPathExpression leftExpr  = null;


    private XPathExpression rightExpr = null;

    
    private short op = EQUAL;

    
    private static String[] ops = new String[] { "=", "<", ">", "<=", ">=", "!=" };
    

      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new EqualityExpr using the default operator
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this EqualityExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this EqualityExpr
     * <BR><B>Note:</B> the default operator is EqualityExpr.EQUALS
    **/
    public EqualityExpr( XPathExpression leftExpr, XPathExpression rightExpr )
        throws XPathException
    {
        this( leftExpr, rightExpr, EQUAL );
    } //-- EqualityExpr 
    

    /**
     * Creates a new EqualityExpr
     * @param leftSideExpr the Expr that is to be evaluated as
     * the left side of this EqualityExpr
     * @param rightSideExpr the Expr that is to be evaluated as
     * the right side of this EqualityExpr
     * @param compareOp the comparison operator for this EqualityExpr
     * @exception InvalidExprException when the comparison operator is 
     * invalid 
    **/
    public EqualityExpr( XPathExpression leftExpr, XPathExpression rightExpr, short op )
        throws XPathException
    {
        if ( leftExpr == null )
            throw new XPathException( "Argument leftExpr is null" );
        if ( rightExpr == null )
            throw new XPathException( "Argument rightExpr is null" );
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        if ( op < 0 || op >= ops.length )
            throw new XPathException( "invalid operator for relational expression" );
        this.op = op;
    } //-- EqualityExpr 
    

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Returns the type of Expr this Expr represents
     * @return the type of Expr this Expr represents
    **/
    public short getExprType()
    {
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
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        if ( leftExpr == null || rightExpr == null )
            return BooleanResult.FALSE;

        XPathResult lResult = leftExpr.evaluate( context );
        XPathResult rResult = rightExpr.evaluate( context );

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
    } //-- evaluate
    

    public static boolean isRelationalOperator( String operator )
    {
        if ( operator == null )
            return false;
        for( int i = 0 ; i < ops.length ; ++i )
            if( ops[ i ].equals( operator ) )
                return true;
        return false;
    } //-- isRelationalOperator
    

    /**
     * Returns the String representation of this EqualityExpr
     * @return the String representation of this EqualityExpr
    **/
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if ( leftExpr != null )
            sb.append( leftExpr.toString() );
        else
            sb.append( "null" );
        sb.append( " " );
        sb.append( ops[ op] );
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
        
        
        
        switch ( op ) {
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

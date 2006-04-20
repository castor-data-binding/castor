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
import org.exolab.adaptx.xpath.functions.FalseFunctionCall;
import org.exolab.adaptx.xpath.functions.TrueFunctionCall;

/**
 * This class represents a BooleanExpr
 * <PRE>
 * [24] BooleanExpr ::= AndExpr | OrExpr | PrimaryExpr
 * </PRE>
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class BooleanExpr extends XPathExpression {

    private XPathExpression expr = null;

      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new BooleanExpr of the given type
    **/
    public BooleanExpr() {
        this(null);
    } //-- BooleanExpr

    public BooleanExpr(boolean bool) {
        if (bool) this.expr = new TrueFunctionCall();
        else this.expr = new FalseFunctionCall();
    } //-- BooleanExpr
    
    /**
     * Creates a new BooleanExpr of the given type
     * and adds the given BooleanPrimaryExpr to the
     * expression list
    **/
    public BooleanExpr(XPathExpression expr) {
        this.expr = expr;
    } //-- BooleanExpr
    
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
        if (expr != null)
            return BooleanResult.from( expr.evaluate( context ) );
        else
            return BooleanResult.FALSE;

    } //-- evaluate

    /**
     * Returns the String representation of this BooleanExpr
     * @return the String representation of this BooleanExpr
    **/
    public String toString() {
        if (expr!= null) return expr.toString();
        else return "false()";
    } //-- toString

} //-- BooleanExpr


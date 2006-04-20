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
import org.exolab.adaptx.xpath.XPathException;


/**
 * Represents a PrimaryExpr<BR>
 * <PRE>
 * from XSLT WD 19990421
 * [14] PrimaryExpr ::= VariableReference
 *                      | '(' Expr ')'
 *                      | Literal
 *                      | Number
 *                      | FunctionCall
 * </PRE>
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
abstract class PrimaryExpr
    extends XPathExpression
{


    public static final short VARIABLE_REFERENCE = 0;
    public static final short EXPR               = 1;
    public static final short LITERAL            = 2;
    public static final short NUMBER             = 3;
    public static final short FUNCTION_CALL      = 4;

    private short _type = -1;


      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new PrimaryExpr which will simply evaluate
     * to the given string literal
     *
     * @param literal the String literal to evaluate to.
    **/
    protected PrimaryExpr( short type )
    {
        this._type = type;
        
    } //-- PrimaryExpr
    

      //------------------/
     //- Public Methods -/
    //------------------/

    
    /**
     * Returns the type of Expr this Expr represents
     * @return the type of Expr this Expr represents
    **/
    public short getExprType()
    {
        return XPathExpression.PRIMARY;
    } //-- getExprType
    

    /**
     * Retrieves the type of this PrimaryExpr
     * @return the type of this PrimaryExpr
    **/
    public final short getType()
    {
        return _type;
    } //--getType

} //-- PrimaryExpr

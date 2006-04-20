/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * except in compliance with the license. Please see license.txt, 
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

 
package org.exolab.adaptx.xpath;


import org.exolab.adaptx.xpath.engine.Parser;


/**
 * The XPath parser. Provides functionality for creating an 
 * XPathExpression from a given string representation.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$
 */
public final class XPathParser
{

    /**
     * A boolean to indicate whether or not to allow using
     * error expressions or to report error immediately.
    **/
    private boolean _useErrorExpr = false;
    

    /** 
     * Default constructor
    **/
    public XPathParser() {
        super();
    } //-- XPathParser
    
    /**
     * Creates an XPath expression from the given String
     *
     * @param xpath the String to create the expression from
     * @returns the new expression
    **/
    public XPathExpression createExpression( String xpath )
        throws XPathException
    {
        XPathExpression expr = Parser.createExpr( xpath );
        
        if (expr.getExprType() == XPathExpression.ERROR) {
            //-- cause XPathException to be thrown
            if (!_useErrorExpr) expr.evaluate(null);
        }
        return expr;
    } //-- createExpression


    /**
     * Creates an XPath expression that will evaluate to a NodeSet.
     *
     * @param xpath the String to create the select expression from.
     * @return the new expression
    **/
    public XPathExpression createSelectExpression( String xpath ) 
        throws XPathException
    {
        XPathExpression expr = Parser.createSelectExpr( xpath );
        if (expr.getExprType() == XPathExpression.ERROR) {
            //-- cause XPathException to be thrown
            if (!_useErrorExpr) expr.evaluate(null);
        }        
        return expr;
    } //-- createSelectExpr

    public static XPathParser newInstance()
    {
        return new XPathParser();
    }

    /** 
     * Enables the use of an error expression. This
     * allows suppressing syntax errors until the expression is 
     * actually evaluated. By default, an XPathException will be
     * thrown.
     *
     * @param useErrorExpr a boolean that when true will return
     * an ErrorExpr instead of throwing an XPathException
    **/
    public void setUseErrorExpr(boolean useErrorExpr) {
        _useErrorExpr = useErrorExpr;
    } //-- setUseErrorExpr
    
} //-- XPathParser

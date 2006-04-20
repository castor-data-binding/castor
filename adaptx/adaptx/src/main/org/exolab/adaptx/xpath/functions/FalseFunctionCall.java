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


package org.exolab.adaptx.xpath.functions;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathException;


/**
 * A class that represents the XPath 1.0 false() function call
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class FalseFunctionCall 
    extends FunctionCallImpl
{
 
   
    private static final String FALSE = "false";

    
    /**
     * Creates a new FalseFunctionCall
    **/
    public FalseFunctionCall()
    {
        super( FALSE );
    } //-- FalseFunctionCall
    

    /**
     * Evaluates this Expr using the given context Node and ExprContext
     * @param context the current context Node
     * @param exprContext the ExprContext which has additional
     * information sometimes needed for evaluating XPath expressions
     * environment
     * @return the ExprResult
     * @exception InvalidExprException when an invalid expression is
     * encountered during evaluation
    **/
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {        
        return BooleanResult.FALSE;
    } //-- evaluate

    
} //-- FalseFunctionCall

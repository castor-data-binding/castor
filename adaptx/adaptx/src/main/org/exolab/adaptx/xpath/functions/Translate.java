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


package org.exolab.adaptx.xpath.functions;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.engine.FunctionCall;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * A implementation of the "translate" function call
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class Translate
    extends FunctionCall
{

    
    private static final String TRANSLATE = "translate";

    
    /**
     * Creates a new Translate FunctionCall
    **/
    public Translate()
    {
        super( TRANSLATE );
    } //-- Translate
    

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
        if ( getParameterCount() != 3 )
            throw new XPathException( INVALID_NUMBER_PARAMS + this );
            
        String str1 = getParameter( 0 ).evaluate( context ).stringValue();
        String str2 = getParameter( 1 ).evaluate( context ).stringValue();
        String str3 = getParameter( 2 ).evaluate( context ).stringValue();
            
        StringBuffer result = new StringBuffer();
        char[] xchars = str1.toCharArray();
        for ( int i = 0 ; i < xchars.length ; i++ ) {
            int idx = str2.indexOf( xchars[ i ] );
            if ( idx >= 0 ) {
                char nchar = str3.charAt( idx );
                if ( nchar != -1 )
                    xchars[ i ] = nchar;
            }
        }
        return new StringResult( new String( xchars ) );
    } //-- evaluate

    
} //-- Translate


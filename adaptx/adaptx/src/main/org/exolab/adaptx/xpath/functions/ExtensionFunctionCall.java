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
import org.exolab.adaptx.xpath.XPathFunction;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.engine.FunctionCall;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * A class for representing function call
 * @author Keith Visco (kvisco@ziplink.net)
**/
public class ExtensionFunctionCall
    extends FunctionCall 
{

        
    public static final String FUNCTION_NOT_DEFINED =
        "The following function has not been defined: ";
        

    public ExtensionFunctionCall( String name )
    {
        super( name );
    } //-- ExtensionFunctionCall
    
    
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
        String fnName = getFunctionName();
        int index = fnName.indexOf( ':' );
        String local;
        String uri;
        if ( index >= 1 ) {
            local = fnName.substring( index + 1 );
            uri = fnName.substring( 0, index );
            uri = uri == null ? "" : context.getNamespaceURI( uri );
        } else {
            local = fnName;
            uri = "";
        }

        XPathFunction fnCall = context.getFunction( uri, local );
        if ( fnCall == null )
            throw new XPathException( FUNCTION_NOT_DEFINED+fnName );
            
        //-- copy parameters
        XPathResult[] params;
        int nbrParams = getParameterCount();
        params = new XPathResult[ nbrParams ];
        for ( int i = 0 ; i < nbrParams ; i++ )
            params[ i ] = getParameter( i ).evaluate( context );
        return fnCall.call( context, params );
    } //-- evaluate

    
} //-- ExtensionFunctionCall

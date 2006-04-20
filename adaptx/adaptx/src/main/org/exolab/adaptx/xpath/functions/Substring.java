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
 * A implementation of the "substring()" function call
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class Substring
    extends FunctionCall
{

    
    /**
     * Creates a new Substring FunctionCall
    **/
    public Substring()
    {
        super( Names.SUBSTRING_FN );
    } //-- Substring
    

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
        int nparams = getParameterCount();
        if ( nparams < 2 || nparams > 3 )
            throw new XPathException( INVALID_NUMBER_PARAMS + this );
            
        XPathResult result = getParameter( 0 ).evaluate( context );
        String str = result.stringValue();
        
        //-- calculate start index
        int startIdx = 1;
        result = getParameter( 1 ).evaluate( context );
        double dbl = result.numberValue();
        
        if ( Double.isNaN( dbl ) || Double.isInfinite( dbl ) )
            return StringResult.EMPTY;
        
        startIdx = (int) Math.round( dbl );
        
        if ( nparams == 2 ) {
            //-- adjust starting index since XSLT String
            //-- indexing starts at 1, but Java String
            //-- indexing starts at 0.
            if ( startIdx > 0 )
                --startIdx;
            else
                startIdx = 0;
            result = new StringResult( str.substring(startIdx) );
        }
        else {
            //-- calculate length
            result = getParameter( 2 ).evaluate( context );
            dbl = result.numberValue();
            if ( Double.isNaN( dbl ) )
                return StringResult.EMPTY;
            if ( Double.isInfinite( dbl ) )
                return new StringResult( str );
            int length = (int) Math.round( dbl );
            
            //-- convert length into end index
            
            int endIdx = startIdx+length-1;
            //-- adjust starting index since XSLT String
            //-- indexing starts at 1, but Java String
            //-- indexing starts at 0.
            if ( startIdx > 0 )
                --startIdx;
            else
                startIdx = 0;
            result = new StringResult( str.substring( startIdx, endIdx ) );
        }
        return result;
        
    } //-- evaluate

    
} //-- Substring


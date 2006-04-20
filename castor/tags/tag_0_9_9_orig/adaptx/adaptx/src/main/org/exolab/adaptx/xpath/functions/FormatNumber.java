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


import java.text.DecimalFormat;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * A implementation of the "format-number()" function call.
 * <BR />
 * <I>method signature:</I><BR />
 *   String format-number(number, string, string?) <BR />
 * The 3rd argument which is optional (string?) is currently not
 * handled.
 * <BR />
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public class FormatNumber
    extends FunctionCallImpl
{
 
   
    private static final String FORMAT_NUMBER = "format-number";


    /**
     * Creates a new FormatNumber FunctionCall
    **/
    public FormatNumber()
    {
        super( FORMAT_NUMBER );
    } //-- FormatNumber
    

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
        double dbl = result.numberValue();
        
        if ( Double.isNaN( dbl ) )
            return new StringResult( "NaN" );
        if ( Double.isInfinite( dbl ) )
            return new StringResult( "Infinite" );
        
        //-- get number format
        result = getParameter( 1 ).evaluate( context );
        String format = result.stringValue();
        
        String locale = null;
        //-- handle locale
        if ( nparams == 3 ) {
            result = getParameter( 2 ).evaluate( context );
            locale = result.stringValue();
        }
        DecimalFormat df = new DecimalFormat( format );
        return new StringResult( df.format( dbl ) );
    } //-- evaluate

    
} //-- FormatNumber


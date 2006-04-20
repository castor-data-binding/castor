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
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * An implementation of the XPath 1.0 "concat" function call
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Concat
    extends FunctionCallImpl 
{

    
    /**
     * Creates a new Concat FunctionCall
    **/
    public Concat()
    {
        super( Names.CONCAT_FN );
    } //-- Concat
    

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
        int paramCount = getParameterCount();
        if ( paramCount < 2 )
            throw new XPathException( INVALID_NUMBER_PARAMS + this );
        
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < paramCount; i++ ) {
            XPathExpression expr = getParameter( i );
            sb.append( expr.evaluate( context ).stringValue() );
        }
        return new StringResult( sb.toString() );
    } //-- evaluate

    
} //-- Concat

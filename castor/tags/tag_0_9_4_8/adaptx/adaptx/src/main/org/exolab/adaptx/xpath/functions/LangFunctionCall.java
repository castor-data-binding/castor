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
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.engine.FunctionCall;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * A implementation of the "starts-with" function call
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class LangFunctionCall
    extends FunctionCall
{

    
    private static final String XML_LANG = "xml:lang";

    private static final String XML_NS = "http://www.w3.org/XML/1998/namespac";

    
    /**
     * Creates a new Lang FunctionCall
    **/
    public LangFunctionCall()
    {
        super( Names.LANG_FN );
    } //-- LangFunctionCall
    

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
        if ( getParameterCount() != 1 )
            throw new XPathException( INVALID_NUMBER_PARAMS + this );
            
        String test = getParameter( 0 ).evaluate( context ).stringValue();
        XPathNode node = context.getNode();
        String lang = getLangAttr( node );
        if ( lang == null )
            return BooleanResult.FALSE;
        
        if ( lang.equalsIgnoreCase( test ) ) 
            return BooleanResult.TRUE;
            
        int idx = lang.indexOf( '-' );
        if ( idx >= 0 ) {
            lang = lang.substring( 0, idx );
            if ( lang.equalsIgnoreCase( test ) ) 
                return BooleanResult.TRUE;
        }
        return BooleanResult.FALSE;
    } //-- evaluate

    
    private String getLangAttr( XPathNode node )
    {
        String lang = null;

        if ( node == null )
            return null;
        if ( node.getNodeType() != XPathNode.ELEMENT )
            node = node.getParentNode();
        while ( node != null ) {
            if ( node.getNodeType() != XPathNode.ELEMENT )
                break;
            lang = node.getAttribute( XML_NS, XML_LANG );
            if ( lang == null )
                lang = node.getAttribute( "", XML_LANG );
            if ( lang != null && lang.length() > 0 )
                return lang;
            node = node.getParentNode();
        }
        return lang;
    } //-- getLangAttr

    
} //-- LangFunctionCall


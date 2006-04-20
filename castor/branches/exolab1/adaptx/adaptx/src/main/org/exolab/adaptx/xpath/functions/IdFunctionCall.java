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


import java.util.StringTokenizer;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.engine.FunctionCall;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * This class represents an Id() function call
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class IdFunctionCall
    extends FunctionCall 
{
    
    
    private static final String ID = "id";

    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates an IdFunctionCall
    **/
    public IdFunctionCall()
    {
        super( ID );
    } //-- IdExpr
    
    
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
        NodeSet nodeSet = context.newNodeSet();
        XPathNode tmpNode;
        if ( getParameterCount() == 1 ) {
            XPathNode node = context.getNode();
            XPathExpression expr = getParameter( 0 );
            XPathResult result = expr.evaluate( context );
            StringTokenizer st = new StringTokenizer( result.stringValue() );
            while ( st.hasMoreTokens() ) {
                tmpNode = context.getElementById( node, st.nextToken() );
                if ( tmpNode != null )
                    nodeSet.add( tmpNode );
            }
        }
        return nodeSet;
    } //-- evaluate

    
} //-- IdFunctionCall

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
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * A class that represents the XPath name related functions
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public class XMLNamesFunctionCall
    extends FunctionCallImpl
{

    
    public static final short LOCAL_PART   = 1;
    public static final short NAME         = 2;
    public static final short NAMESPACE    = 3;
    

    private short fType = NAME;

    
    /**
     * Creates a new XMLNames Function Call
    **/
    public XMLNamesFunctionCall()
    {
        super( "name" );
    } //-- XMLNamesFunctionCall
    

    /**
     * Creates a new XMLNames Function Call
    **/
    public XMLNamesFunctionCall( short type )
    {
        super( getFunctionName( type ) );
        this.fType = type;
    } //-- XMLNamesFunctionCall
    

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

        XPathNode node = context.getNode();
        String nodeName = null;
        if ( getParameterCount() == 1 ) {
            XPathResult result = getParameter( 0 ).evaluate( context );
            if ( result.getResultType() == XPathResult.NODE_SET ) {
                NodeSet nodes = (NodeSet) result;
                if ( nodes.size() > 0 )
                    node = nodes.item( 0 );
                else
                    node = null;
            }
        }
        if ( node != null && ( node.getNodeType() == XPathNode.ELEMENT ||
                               node.getNodeType() == XPathNode.ATTRIBUTE ) ) {
            switch(fType) {
            case LOCAL_PART:
                return new StringResult( node.getLocalName() );
            case NAMESPACE:
                return new StringResult( node.getNamespaceURI() );
            default:
                return new StringResult( node.getLocalName() );
            }
        }
        return StringResult.EMPTY;
    } //-- evaluate


    /**
     * Returns the local part of the qualified XML name
     * @param qName the qualified XML name
     * @return the local part of the qualified XML name
    **/
    private static String getLocalPart( String qName )
    {
        if ( qName == null )
            return "";
        int idx = qName.indexOf(':');
        if ( idx >= 0 )
            return qName.substring( idx + 1 );
        return qName;
    } //-- getLocalPart

    
    /**
     * Returns the namespace part of the qualified XML name
     * @param qName the qualified XML name
     * @return the namespace part of the qualified XML name
    **/
    private static String getNameSpace( String qName )
    {
        if ( qName == null )
            return "";
        int idx = qName.indexOf( ':' );
        if ( idx > 0 )
            return qName.substring( 0, idx );
        return "";
    } //-- getNameSpace

    
    /**
     * 
    **/
    private static String getFunctionName( short type )
    {
        switch( type ) {
        case LOCAL_PART:
            return "local-part";
        case NAMESPACE:
            return "namespace";
        default:
            return "name";
        }
    } //-- getFunctionName
    

} //-- XMLNamesFunctionCall

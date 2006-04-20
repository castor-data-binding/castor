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

package org.exolab.adaptx.xslt;

import org.exolab.adaptx.xpath.XPathFunction;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;

/**
 * The base class for XSLT functions.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class XSLTFunction extends XPathFunction {


    /**
     * The current (and ever changing) XSLT namespace
    **/
    public static final String XSLT_NAMESPACE = 
        "http://www.w3.org/1999/XSL/Transform/";

    
    public static final String INVALID_NUMBER_PARAMS = 
        "invalid number of parameters for function: ";
    
    private String _name = null;
    
   
    /**
     * Creates a new XSLTFunction with the given name.
     *
     * @param name the function name.
    **/
    public XSLTFunction(String name) {
        _name = name;
    } //-- XSLTFunction
    
    /**
     * Invokes the function and returns the XPath result.
     *
     * @param context The XPath context
     * @param params A list of zero or more arguments
     * @return An XPath result (not null)
     * @throws XPathException An error occured while invoking this function
     */
    public abstract XPathResult call( XPathContext context, XPathResult[] args )
        throws XPathException;


    /**
     * Returns the namespace URI of this function.
     *
     * @return The namespace URI of this function
    **/
    public String getNamespaceURI() {
        return XSLT_NAMESPACE;
    } //-- getNamespaceURI

    /**
     * Returns the name of this function.
     *
     * @return The name of this function
    **/
    public String getFunctionName() {
        return _name;
    } //-- getFunctionName
    
} //-- XSLTFunction

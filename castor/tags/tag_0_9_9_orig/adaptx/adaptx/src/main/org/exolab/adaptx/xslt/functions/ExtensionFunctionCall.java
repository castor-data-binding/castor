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
package org.exolab.adaptx.xslt.functions;

import org.w3c.dom.Node;

import org.exolab.adaptx.xslt.XSLTFunction;
import org.exolab.adaptx.xslt.ProcessorState;
import org.exolab.adaptx.util.List;
import org.exolab.adaptx.xml.XMLUtil;

import org.exolab.adaptx.xpath.*;

/**
 * A class for representing extension function calls
 *
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ExtensionFunctionCall extends XSLTFunction {
        
    public static final String FUNCTION_NOT_DEFINED =
        "The following function has not been defined: ";
        
    
    private ProcessorState ps = null;
    
    /**
     * Creates a new ExtensionFunctionCall
    **/
    public ExtensionFunctionCall(String name, ProcessorState ps) {
        super(name);
        this.ps = ps;
    } //-- ExtensionFunctionCall
    
    
    /**
     * Invokes the function and returns the XPath result.
     *
     * @param context The XPath context
     * @param params A list of zero or more arguments
     * @return An XPath result (not null)
     * @throws XPathException An error occured while invoking this function
     */
    public XPathResult call(XPathContext context, XPathResult[] args)
        throws XPathException
    {
        
        String qname = getFunctionName();
        String ns    = XMLUtil.getNameSpacePrefix(qname);
        String name  = XMLUtil.getLocalPart(qname);
        
        if (ns.length() == 0) ns = null;
        
        XPathFunction fn = ps.getFunction(ns, name);
        if (fn == null)
            throw new XPathException("undefined function: " + qname);
        
        return fn.call(context, args);
            
    } //-- evaluate
    
    /**
     * Returns the namespace that this function call is in
    **/
    public String getNameSpace() {
        return XMLUtil.getNameSpacePrefix(getFunctionName());
    } //-- getNameSpace
    
} //-- ExtensionFunctionCall

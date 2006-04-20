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

import org.exolab.adaptx.xslt.Names;
import org.exolab.adaptx.xslt.ProcessorState;
import org.exolab.adaptx.xslt.XSLTFunction;
import org.exolab.adaptx.xpath.*;
import org.exolab.adaptx.xml.XMLUtil;

/**
 * A implementation of the "system-property" function call
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SystemFunctionCall extends XSLTFunction {
    
    public static final short SYSTEM_PROPERTY    = 1;
    public static final short FUNCTION_AVAILABLE = 2;
    
    private short fType = SYSTEM_PROPERTY;
    
    private ProcessorState ps = null;
    
    /**
     * Creates a new SystemFunctionCall using the default
     * function type (system-property())
    **/
    public SystemFunctionCall(ProcessorState ps) {
        super(Names.SYSTEM_PROPERTY_FN);
        this.ps = ps;
    } //-- SystemFunctionCall
    
    /**
     * Creates a new SystemFunctionCall 
    **/
    public SystemFunctionCall(ProcessorState ps, short type) {
        super(getFunctionName(type));
        this.fType = type;
        this.ps = ps;
    } //-- SystemFunctionCall
    
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
        
        if (args.length != 1)
            throw new XPathException(INVALID_NUMBER_PARAMS+this);
            
        String paramStr = args[0].stringValue();
        
        if (fType == FUNCTION_AVAILABLE) {
            String ns = XMLUtil.getNameSpacePrefix(paramStr);
            if (ns.length() == 0) ns = null;
            String name = XMLUtil.getLocalPart(paramStr);
            return BooleanResult.from(ps.isFunctionAvailable(name, ns));
        }
        else //-- SystemProperty 
            return new StringResult(ps.getProperty(paramStr));
        
    } //-- evaluate
    
    
    private static String getFunctionName(short type) {
        if (type == FUNCTION_AVAILABLE)
            return Names.FUNCTION_AVAILABLE_FN;
        else 
            return Names.SYSTEM_PROPERTY_FN;
    } //-- getFunctionName
    
} //-- SystemFunctionCall


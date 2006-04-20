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
 * 
 */

package org.exolab.adaptx.xslt.functions;

import org.exolab.adaptx.xpath.*;
import org.exolab.adaptx.xslt.ProcessorState;
import org.exolab.adaptx.xslt.XSLTFunction;
import org.exolab.adaptx.xslt.Names;
import org.exolab.adaptx.util.ErrorObserver;

/**
 *
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class GenerateIDFunctionCall extends XSLTFunction {
    
    private ProcessorState ps = null;
    
    /**
     * Creates a new GenerateIDFunctionCall
    **/
    public GenerateIDFunctionCall(ProcessorState ps) {
        super(Names.GENERATE_ID_FN);
        this.ps = ps;
    } //-- GenerateIDFunctionCall
    
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
        
        XPathNode node = context.getNode();
        
        if (args[0].getResultType() == XPathResult.NODE_SET) {
            NodeSet nodeSet = (NodeSet)args[0];
            if (nodeSet.size() == 0) {
                return new StringResult("");
            }
            else node = nodeSet.item(0);
        }
        //-- need to add ErrResult
        else {
            StringBuffer errMsg 
                = new StringBuffer("InvalidParameterType:");
            errMsg.append(" expecting NodeSet as parameter to: ");
            errMsg.append(this.toString());
            ps.getErrorObserver().receiveError(errMsg.toString(),
                ErrorObserver.WARNING);
            return new StringResult(errMsg.toString());
        }
        return new StringResult( ps.generateId(node) );
    } //-- evaluate
    
} //-- GenerateIDFunctionCall

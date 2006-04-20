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

import org.exolab.adaptx.xslt.ProcessorState;
import org.exolab.adaptx.xslt.Names;
import org.exolab.adaptx.xslt.TreeFragmentResult;
import org.exolab.adaptx.xslt.XSLTFunction;
import org.exolab.adaptx.xslt.XSLTFunctionResult;


import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.NodeSet;

/**
 * A class that represents an XSL:P extension function which
 * allows converting an XSLT result-tree-fragment into a 
 * NodeSet.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class RTF2NodeSetFunctionCall extends XSLTFunction {
    
    private ProcessorState ps = null;
    
    /**
     * Creates a new rtf-2-nodeset() Function Call
    **/
    public RTF2NodeSetFunctionCall(ProcessorState ps) {
        super(Names.RTF_2_NODESET_FN);
        this.ps = ps;
    } //-- RTF2NodeSetFunctionCall
    
    /**
     * Invokes the function and returns the XPath result.
     *
     * @param context The XPath context
     * @param params A list of zero or more arguments
     * @return An XPath result (not null)
     * @throws XPathException An error occured while invoking this function
     */
    public XPathResult call( XPathContext context, XPathResult[] args )
        throws XPathException
    {
        if (args.length != 1)
            throw new XPathException(INVALID_NUMBER_PARAMS+this);
            
        if (! (args[0] instanceof TreeFragmentResult) ) {
            String err = "Invalid parameter to " + this.getFunctionName();
            err += "; expecting a result-tree-fragment.";
            throw new XPathException(err);
        }
        
        return ((TreeFragmentResult)args[0]).getValue();
        
    } //-- evaluate
    
} //-- RTF2NodeSetFunctionCall

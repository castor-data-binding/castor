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

import org.w3c.dom.*;
import org.exolab.adaptx.xpath.functions.ErrorFunctionCall;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.XPathExpression;

/**
 * Represents an XSLIf Object (xsl:if)
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version
**/
public class CopyOf extends XSLObject {
                
    private XPathExpression _selectExpr = null;
    private ErrorFunctionCall _efc = null;
    
    /**
     * Creates a new CopyOf object
    **/
    public CopyOf() {
        super(XSLObject.COPY_OF);
    } //-- CopyOf
        
    public NodeSet selectNodes(ProcessorState ps) 
        throws XPathException 
    {
        if (_efc != null) _efc.evaluate(ps);
        if (_selectExpr == null) return new NodeSet();
        
        ps.pushCurrentNode(ps.getNode());
        
        NodeSet result = (NodeSet)_selectExpr.evaluate(ps);
        
        ps.popCurrentNode();
        
        return result;
    } //-- evaluate
    
    /**
     * Sets the attribute with the given name to the given value.
     * @param name the name of the attribute to set
     * @param value the value to set the attribute to
     * @throws XSLException if this XSLObject does not allow attributes
     * with the given name, or if the attribute is read only
    **/
    public void setAttribute(String name, String value) 
        throws XSLException
    {
        if (Names.SELECT_ATTR.equals(name)) {
            try {
                _selectExpr = createSelectExpression(value);
            }
            catch(XPathException xpe) {
                _efc = new ErrorFunctionCall();
                _efc.setError("#error: " + value + " is an invalid select expression.");
            }
        }
        super.setAttribute(name,  value);
    } //-- setAttribute
    
} //-- CopyOf

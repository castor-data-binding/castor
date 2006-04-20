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

import org.exolab.adaptx.xpath.*;


/**
 * Represents an XSLIf Object (xsl:if)
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XSLIf extends XSLObject 
    implements Conditional 
{
                
    private XPathExpression _expr = null;
    
    /**
     * Creates a new XSLIf object
    **/
    public XSLIf() {
        super(XSLObject.IF);
    } //-- XSLIf
    
    /**
     * Returns the XPath Expression of this xsl:if
     *
     * @return the XPath Expression of this xsl:if
    **/
    public XPathExpression getExpression() 
        throws XPathException 
    {
        if (_expr == null) {
            String exprStr = getAttribute(Names.TEST_ATTR);
            if ((exprStr == null) || (exprStr.length() == 0))
                _expr = createExpression("false()");
            else {
                _expr = createExpression(exprStr);
            }
        }
        return _expr;
    } //--getExpression
        
    /**
     * Evaluates this Conditional using the given XPathContext.
     *
     * @param context the XPathContext to evaluate this conditional with.
     * @return the resulting BooleanResult
    **/
    public BooleanResult evaluate(XPathContext context)
        throws XPathException
    {
        // call #getBooleanExpr to make sure we have parsed
        // the test attribute
        XPathExpression xpr = getExpression();
        if (xpr == null)
            return BooleanResult.FALSE;
        else {
            return BooleanResult.from(xpr.evaluate(context));
        }
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
        if (Names.TEST_ATTR.equals(name)) _expr = null;        
        super.setAttribute(name,  value);
    } //-- setAttribute
    
    /**
     * Sets the test expression for this xsl:if
     *
     * @param expr the XPathExpression for this xsl:if
    **/
    public void setExpression(XPathExpression expr) {
        
        try {
            if (expr == null)
                super.setAttribute(Names.TEST_ATTR,"");
            else 
                super.setAttribute(Names.TEST_ATTR, expr.toString());
        }
        catch(XSLException xslException) {};
        
        _expr = expr;
    } //-- setBooleanExpr
    
    
} //-- XSLIf

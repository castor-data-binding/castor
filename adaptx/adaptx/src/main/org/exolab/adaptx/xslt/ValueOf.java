/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
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
import org.exolab.adaptx.xpath.engine.Parser;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathExpression;

/**
 * Represents the xsl:value-of element.
 * Handles string expressions 
 * Section 2.7.13.2 of the W3C XSL Working Draft 1.0 (19981216)
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class ValueOf extends EmptyXSLObject {

    /**
     * the String expression for this value-of
    **/
    private XPathExpression expr = null;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ValueOf 
    **/
    public ValueOf() {
        super(XSLObject.VALUE_OF);
    } //--ValueOf

      //------------------/
     //- Public Methods -/
    //------------------/

    public void setAttribute(String name, String value)
        throws XSLException 
    {
        if (Names.SELECT_ATTR.equals(name)) {
            super.setAttribute(name, value);
            try {
                expr = Parser.createExpr(value);
            }
            catch(XPathException xpe) {
                String err = "Invalid Select Expression: " + xpe.getMessage();
                err += "\n -- expression: " + value;
                throw new XSLException(err);
            }
        }
    }

      //---------------------/
     //- Protected Methods -/
    //---------------------/

    protected XPathExpression getExpression() {
        return expr;
    } //-- getExpression

    /**
     * Retrieves the value of the node (Element or Attribute) 
     * matched by the pattern of this ValueOf Object.
     *
     * @param ps the ProcessorState for accessing the current processing
     * environment
     * @return the String value of the selected node.
    **/
    protected String getValue(ProcessorState ps) {

        if (expr == null) return "";

        try {
            return expr.evaluate(ps).toString();
        }
        catch (XPathException xpe) {
            
            if (ps.getErrorObserver() != null) 
                ps.getErrorObserver().receiveError(xpe);
        }
        
        //-- if we make it here there was an error,
        //-- and the observer want's us to try to continue
        return "";

    } //-- getValue

    /**
     * Sets the XPathExpression used with this ValueOf.
     *
     * @param expr the XPathExpression to use with this ValueOf
    **/
    protected void setExpression(XPathExpression expr) {

        this.expr = expr;
        try {
            setAttribute(Names.SELECT_ATTR,expr.toString());
        }
        catch(XSLException xslException) {};

    } //-- setExpression

} //-- ValueOf


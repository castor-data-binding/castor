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
import org.exolab.adaptx.xpath.engine.Parser;

import org.w3c.dom.*;

/**
 * A representation of an Attribute Value Template
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeValueTemplate {

    /**
     * Token indicating end of an expression
    **/
    private static final String END_EXPR = "}";
    
    /**
     * Token indicating start of an expression
    **/
    private static final String START_EXPR = "{";
    
    /**
     * the components of this AttributeValueTemplate
    **/
    private Component start = null;
    private Component last  = null;

    private String error    = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates an empty AttributeValueTemplate
    **/
    public AttributeValueTemplate() {
        super();
    } //-- AttributeValueTemplate
    
    /**
     * Creates an AttributeValueTemplate using the given String
     * @param attValue the String to create the AttributeValueTemplate from
     * @exception InvalidExprException
    **/
    public AttributeValueTemplate(String attValue) {
        this();
        if (attValue != null) parse(attValue);
    } //-- AttributeValueTemplate

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Adds the given Expr to this AttributeValueTemplate.
     * @param Expr the Expr to add to this AttributeValueTemplate
    **/
    public void addExpr(XPathExpression expr) {
        if (expr == null) return;
        
        Component comp = new Component();
        comp.expr = expr;
        
        if (last == null) 
            last = start = comp;
        else {
            last.next = comp;
            last = comp;
        }
        
    } //-- addExpr

    /**
     * Returns the value of this AttributeValueTemplate when evaluated using
     * the given context Node.
     *
     * @param node the context Node to evaluate this AttributeValueTemplate with
     * @param context the XPathContext to use during evaluation.
    **/ 
    public String evaluate(XPathContext xcontext) 
        throws XSLException
    {
        if (error != null) 
            throw new XSLException(error);
            
        StringBuffer sb = new StringBuffer();
        
        Component comp = start;
        try {
            while (comp != null) {
                XPathResult result = comp.expr.evaluate(xcontext);
                sb.append(result.stringValue());
                comp = comp.next;
            }
        }
        catch(XPathException xpe) {
            throw new XSLException(xpe);
        }
        return sb.toString();
    } //-- getValue

    /**
     * Removes the given Expr from this AttributeValueTemplate
     * @param expr the Expr to remove
    **/
    public void removeExpr(XPathExpression expr) {
        Component comp = start;
        Component prev = null;
        while (comp != null) {
            if (comp.expr == expr) {
                //-- if we are at the start of the list,
                //-- just advance start
                if (comp == start)
                    start = start.next;
                //-- otherwise (prev is not null), so we
                //-- just set prev.next to point to comp.next
                else
                    prev.next = comp.next;
                //-- adjust last, if necessary
                if (comp == last) last = prev;
                break;
            }
            comp = comp.next;
        }
    } //-- removeExpr
    
    /**
     * Returns the String representation of this AttributeValueTemplate
     * @return the String representation of this AttributeValueTemplate
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Component comp = start;
        while (comp != null) {
            sb.append( comp.expr.toString() );
            comp = comp.next;
        }
        return sb.toString();
    } //-- toString
    
      //-------------------/
     //- Private Methods -/
    //-------------------/
    
    /**
     * Parses the given String into this AttributeValueTemplate
     * @exception InvalidExprException
    **/
    private  void parse(String pattern) {
        
        char[] chars = pattern.toCharArray();
        int cc = 0;
        StringBuffer buffer = new StringBuffer();
        boolean inExpr      = false;
        boolean inLiteral   = false;
        char endLiteral = '"';
        char prevCh = '\0';

        while ( cc < chars.length ) {
            char ch = chars[cc++];
            // if in literal just add ch to buffer
            if ( inLiteral && (ch != endLiteral) ) {
                    buffer.append(ch);
                    prevCh = ch;
                    continue;
            }
            switch ( ch ) {
                case '\'' :
                case '"' :
                    buffer.append(ch);
                    if (inLiteral) inLiteral = false;
                    else {
                        inLiteral = true;
                        endLiteral = ch;
                    }
                    break;
                case  '{' :
                    // Ignore case where we find two { without a }
                    if (!inExpr) {
                        //-- clear buffer
                        if ( buffer.length() > 0) {
                            String value = buffer.toString();
                            addExpr(Parser.createLiteralExpr(value));
                            buffer.setLength(0);
                        }
                        inExpr = true;
                    }
                    else if (prevCh == ch) {
                        inExpr = false;
                        buffer.append(ch);
                    }
                    else {
                        buffer.append(ch); //-- simply append '{'
                        ch = '\0';
                    }
                    break;
                case '}':
                    if (inExpr) {
                        inExpr = false;
                        
                        XPathExpression expr = null;
                        try {
                            expr = Parser.createExpr(buffer.toString());
                        }
                        catch(XPathException xpe) {
                            error = xpe.toString();
                            return;
                        }
                        addExpr(expr);
                        buffer.setLength(0);
                        //-- change in case another '}' follows
                        ch = '\0';
                    }
                    else if (prevCh != ch) {
                        if ( buffer.length() > 0) buffer.append('}');
                        else 
                            addExpr(Parser.createLiteralExpr(END_EXPR));
                    }
                    else ch = '\0';
                    break;
                default:
                    buffer.append(ch);
                    break;
            }
            prevCh = ch;
        }
        if ( buffer.length() > 0) {
            if ( inExpr ) {
                //-- error
                StringBuffer errMsg = new StringBuffer();
                errMsg.append("#error evaluating AttributeValueTemplate. ");
                errMsg.append("Missing '}' after: ");
                errMsg.append(buffer.toString());
                error = errMsg.toString();
            }
            else addExpr(Parser.createLiteralExpr(buffer.toString()));
        }

    }
    
    class Component {
        XPathExpression expr = null;
        Component next = null;
    }
    
} //-- AttributeValueTemplate

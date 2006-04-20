/*
 * (C) Copyright Keith Visco 1998, All rights reserved.
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
 

package org.exolab.adaptx.xpath.engine;


import org.exolab.adaptx.xpath.XPathException;


/**
 * The main exception thrown when an Error occurs while
 * parsing an XPath expression
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ParseException extends XPathException {
    
    /**
     * The column number at which the error occured
    **/
    private int column = -1;
    
    /**
     * The expression that caused the error
    **/
    private String expression = null;
    
    /**
     * Creates a new ParseException with the given message.
     * @param message the detail message for this Exception
    **/
    public ParseException(String expr, String message) {
        super(message);
        this.expression = expr;
    } //-- ParseException
    
    /**
     * Creates a new ParseException with the given message.
     * @param message the detail message for this Exception
    **/
    public ParseException(String expr, String message, int columnNumber) {
        super(message);
        this.expression = expr;
        this.column     = columnNumber;
    } //-- ParseException
    
    /**
     * Returns the detail message for this Exception
     * @return the detail message for this Exception
    **/
    public String getMessage() {
        
        StringBuffer sb = new StringBuffer();
        sb.append("An XPath parsing error occured ");
        if (column >= 0) {
            sb.append("at column ");
            sb.append(column);
            sb.append(' ');
        }
        sb.append("in the following expression: ");
        sb.append(expression);
        sb.append("\n -- ");
        sb.append(super.getMessage());
        return sb.toString();
    } //-- getMessage
    
    /**
     * Returns the column number at which the parse error occured.
     * @return the column number at which the parse error occured,
     * or -1, if no column number was specified.
    **/
    public int getColumnNumber() {
        return column;
    } //-- getColumnNumber
    
    /**
     * Return the String representation of this Exception
     * @return the String representation of this Exception
    **/
    public String toString() {
        return getClass().getName() + ": " + getMessage();
    } //-- toString
    
} //-- ParseException

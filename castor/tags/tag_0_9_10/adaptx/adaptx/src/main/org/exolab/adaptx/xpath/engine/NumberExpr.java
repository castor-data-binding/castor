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
 
package org.exolab.adaptx.xpath.engine;

import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.NumberResult;
import org.exolab.adaptx.xpath.expressions.PrimaryExpr;

/**
 * Represents the XPath Number expression
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class NumberExpr extends PrimaryExpr {
    
    //private double       _number = Double.NaN;
    private NumberResult _result = null;
    
    /**
     * Creates a new NumberExpr which evaluates to
     * Double.NaN
    **/
    NumberExpr() {
        super(PrimaryExpr.NUMBER);
        _result = new NumberResult(Double.NaN);
    } //-- NumberExpr
    
    /**
     * Creates a new NumberExpr which evaluates to
     * the given number
     *
     * @param number the Number to evaluate to
    **/
    NumberExpr(double number) {
        super(PrimaryExpr.NUMBER);
        //_number = number;
        _result = new NumberResult(number);
    } //-- NumberExpr
    
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        return _result;
    } //-- evaluate
    
    /**
     * Returns the String representation of this PrimaryExpr
     * @return the String representation of this PrimaryExpr
    **/
    public String toString() {
        return _result.stringValue();
    } //-- toString
    
} //-- NumberExpr


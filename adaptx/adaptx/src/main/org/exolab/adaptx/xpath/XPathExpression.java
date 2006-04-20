/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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

package org.exolab.adaptx.xpath;

/**
 * Interface representing an XPath expression. An XPath expression
 * is thread-safe and can be evaluated multiple times concurrently.
 * It is a compiled version of the textual XPath expression and can be
 * cached for later use.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$
 */
public interface XPathExpression {

    /**
     * Expression of type error.
     */
    public static final short ERROR         = -1;

    /**
     * The Boolean expression type.
     */
    public static final short BOOLEAN         =  0;
    
    /**
     * The FilterExpr expression type.
     */
    public static final short FILTER_EXPR     =  1;
    
    /**
     * The LocationPath expression type.
     */
    public static final short LOCATION_PATH   =  2;

    /**
     * The NodeTest expressions type
     */
    public static final short NODE_TEST       =  3;

    /**
     * The NodeTest expressions type
     */
    public static final short NUMBER          =  4;
    
    /**
     * The PathExpr expression type.
     */
    public static final short PATH_EXPR       =  5;
    
    /**
     * The Primary expression type.
     */
    public static final short PRIMARY         =  6;
    
    /**
     * The Step expression type.
     */
    public static final short STEP            =  7;

    /**
     * The String expression type.
     */
    public static final short STRING          =  8;
    
    /**
     * The union expression type.
     */
    public static final short UNION_EXPR      =  9;

    
    /**
     * Returns the type of this expression.
     *
     * @return The type of this expression
     */
    public short getExprType();

    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
     */
    public XPathResult evaluate( XPathContext context )
        throws XPathException;


    /**
     * Returns the XPath expression as a string. The returned value is
     * a valid XPath expression that can be parsed into an equivalent
     * {@link XPathExpression} object.
     *
     * @return The XPath expression as a string
     */
    public String toString();


} //-- XPathExpression


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

import org.exolab.adaptx.xpath.XPathResult;

/**
 * Abstract class representing the result of an XSLT extension function.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public abstract class XSLTFunctionResult 
    extends XPathResult
{

    /**
     * The tree-fragment result
    **/
    public static final short TREE_FRAGMENT = 0;
    
    /**
     * Returns the type of this result.
     *
     * @return The type of this result
     */
    public abstract short getXSLTResultType();

    //--------------------------------------------/
    //- Methods defined by XPathResult, repeated -/
    //- here for documentation purposes          -/ 
    //--------------------------------------------/
    
    /**
     * Returns the type of this result. This should
     * always be XPathResult.USER_DEFINED.
     *
     * @return The type of this result
     * @see #getXSLTResultType
     */
    public int getResultType() {
        return XPathResult.USER_DEFINED;
    } //-- getResultType


    /**
     * Returns the result as a boolean value. Returns the value of
     * a boolean result, true for a non-empty string result, true
     * for a non-zero number result, and true from a non-empty node-set.
     *
     * @return The result as a boolean value
     */
    public abstract boolean booleanValue();


    /**
     * Returns the result as a number value. Returns 0 or 1 for a
     * boolean result, the parsed value for a string result, the
     * value of a number result, or the parsed value of a node-set.
     *
     * @return The result as a number value
     */
    public abstract double numberValue();


    /**
     * Returns the result as a string value. Returns "false" or
     * "true" for a boolean result, the value of a string result,
     * the string value of a number result, or the string value of
     * a node-set.
     *
     * @return The result as a string value
     */
    public abstract String stringValue();


    /**
     * Returns the result as a Java object. Returns an object of
     * type {@link java.lang.Boolean} for a boolean result, an object of
     * type {@link java.lang.String} for a string result, an object of type
     * {@link java.lang.Double} for a number result, or an object of type
     * {@link NodeSet} for a node-set.
     *
     * @return The result as a Java object
     */
    public abstract Object javaObject();


    /**
     * Returns true if the given expression is the same tyoe as
     * this result and has the same value as this result.
     *
     * @param result An XPath result
     * @return True if same type and same value as this result
     */
    public abstract boolean equals( XPathResult result );


} //-- XSLTFunctionResult

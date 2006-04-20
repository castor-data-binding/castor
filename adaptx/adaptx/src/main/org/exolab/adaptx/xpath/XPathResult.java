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


package org.exolab.adaptx.xpath;


/**
 * Abstract class representing the result of an XPath expression.
 * An XPath result can be one of the following four types:
 * <ul>
 * <li>A boolean result of type {@link #BOOLEAN} represented by
 * an object of type {@link BooleanResult}.</li>
 * <li>A string result of type {@link #STRING} represented by
 * an object of type {@link StringResult}.</li>
 * <li>A number result of type {@link #NUMBER} represented by
 * an object of type {@link NumberResult}.</li>
 * <li>A node-set result of type {@link #NODE_SET} represented by
 * an object of type {@link NodeSet}.</li>
 * </ul>
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$
 */
public abstract class XPathResult
    implements java.io.Serializable
{


    /**
     * An XPath result of type boolean. Returned by {@link BooleanResult}.
     */
    public static final int BOOLEAN  = 0;
    
    
    /**
     * An XPath result of type number. Returned by {@link NumberResult}.
     */
    public static final int NUMBER   = 1;
    
    
    /**
     * An XPath result of type string. Returned by {@link StringResult}.
     */
    public static final int STRING   = 2;
    
    
    /**
     * An XPath result of type node-set. Returned by {@link NodeSet}.
     */
    public static final int NODE_SET = 3;


    /**
     * An XPath result used for XPath extensions
     */
    public static final int USER_DEFINED = 4;
    
    /**
     * Returns the type of this result.
     *
     * @return The type of this result
     */
    public abstract int getResultType();


    /**
     * Returns the result as a boolean value. Returns the value of
     * a boolean result, true for a non-empty string result, true
     * for a non-zero number result, and true from a non-empty node-set.
     *
     * @return The result as a boolean value
     */
    public abstract  boolean booleanValue();


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


}

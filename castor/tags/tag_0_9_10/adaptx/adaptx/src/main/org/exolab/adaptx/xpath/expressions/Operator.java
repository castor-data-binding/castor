/*
 * (C) Copyright Keith Visco 1999-2003  All rights reserved.
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

package org.exolab.adaptx.xpath.expressions;

import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;

/**
 * Represents an Logical or Mathimatical Operator that operates
 * on binary expressions.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public interface Operator {

    /**
     * The add operator type
     */
    public static final int ADD_OPERATOR       = 0;
    
    /**
     * The and operator type
     */
    public static final int AND_OPERATOR       = 1;
    
    /**
     * The divide operator type
     */
    public static final int DIVIDE_OPERATOR     = 2;
            
    /**
     * The "equality" operator type
     */
    public static final int EQUALITY_OPERATOR  = 3;
    
    /**
     * The modulus operator type
     */
    public static final int MODULUS_OPERATOR   = 4;
    
    /**
     * The multiply operator type
     */
    public static final int MULTIPLY_OPERATOR  = 5;
    
    /**
     * The or operator type
     */
    public static final int OR_OPERATOR        = 6;
    
    /**
     * The quotient operator type
     */
    public static final int QUOTIENT_OPERATOR  = 7;
    
    /**
     * The subtract operator type
     */
    public static final int SUBTRACT_OPERATOR  = 8;
    
    //----------------------/
    //- Method definitions -/
    //----------------------/
    
    /**
     * Returns the type for this Operator. The operator
     * type may be one of the pre-defined types, or
     * a user-defined type.
     *
     * @return the operator type
     */
    public int getOperatorType();
    
    /**
     * Executes this operator on the given expressions
     *
     * @param left the left-side expression
     * @param right the right-side expression
     * @param context the XPathContext 
     * @return the XPathResult
     * @throws XPathException when an error occurs during execution
     */
    public XPathResult execute
        (XPathExpression left, XPathExpression right, XPathContext context)
        throws XPathException;

    /**
     * Executes this operator on the given XPath values
     *
     * @param left the left-side expression
     * @param right the right-side expression
     * @return the XPathResult
     * @throws XPathException when an error occurs during execution
     */
    public XPathResult execute
        (XPathResult left, XPathResult right)
        throws XPathException;
    
    
} //-- Operator


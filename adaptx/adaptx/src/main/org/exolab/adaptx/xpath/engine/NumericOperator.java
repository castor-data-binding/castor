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

package org.exolab.adaptx.xpath.engine;

import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NumberResult;

import org.exolab.adaptx.xpath.expressions.Operator;

/**
 * A binary Operator implementation for
 * use with simple mathematical operations.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
class NumericOperator implements Operator {

    private NumericOperation _operation = null;
    private int _type = -1;
    
    /**
     * Creates a new NumericOperator for the given
     * Operation and Operator type
     */
    NumericOperator(NumericOperation operation, int type) {
        _type = type;
        _operation = operation;
    } //-- NumericOperator
    
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
    public int getOperatorType() {
        return _type;
    }
    
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
        throws XPathException
    {
            if ((left == null) || (right == null))
                return NumberResult.NaN;

            double lValue = left.evaluate(context).numberValue();
            double rValue = right.evaluate(context).numberValue();            
            return new NumberResult(_operation.execute(lValue, rValue));
            
    } //-- execute

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
        throws XPathException
    {
            if ((left == null) || (right == null))
                return NumberResult.NaN;

            double lValue = left.numberValue();
            double rValue = right.numberValue();            
            return new NumberResult(_operation.execute(lValue, rValue));
            
    } //-- execute
    
    public String toString() {
        return _operation.toString();
    }
    
} //-- NumericOperator


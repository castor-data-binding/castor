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

package org.exolab.adaptx.xpath.functions;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.NumberResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.engine.FunctionCall;
import org.exolab.adaptx.xpath.engine.Names;


/**
 * A class that represents the XPath 1.0 sum function call.
 *
 * The Sum function takes as an argument an experession which
 * evaluates to a node-set. Each node in the node-set will 
 * have it's string-value converted into a number. Each
 * resulting number will then added together to compute the 
 * sum.
 *
 * Nodes whose string value is the empty-string will be 
 * ignored.
 *
 * If any node within the node-set has a non-zero length 
 * string value that cannot be sucessfully converted to a 
 * number, an XPathException will be thrown.
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 */
public class SumFunctionCall
    extends FunctionCall
{

    
    /**
     * The name of this XPath function
     */
    private static final String SUM = "sum";


    /**
     * Creates a new SumFunctionCall
     */
    public SumFunctionCall() 
    {
        super( SUM );
    } //-- SumFunctionCall
    

    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
     */
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        if ( getParameterCount() != 1 )
            throw new XPathException( INVALID_NUMBER_PARAMS + toString() );
            
        XPathResult result = getParameter( 0 ).evaluate( context );
        
        //-- Make sure the given argument is a node-set
        if ( result.getResultType() != XPathResult.NODE_SET ) {
            String err = INVALID_RESULT + toString();
            throw new XPathException(err + "; a node-set was expected.");
        }
        
        return new NumberResult( computeSum( (NodeSet)result ) );
        
    } //-- evaluate
    

    /**
     * Computes the sum of the given NodeSet. The sum is
     * computed by adding up the number values of
     * each node within the node set. If any non-numeric
     * node values are encountered, an XPathException
     * will be thrown.
     *
     * @param nodeSet the NodeSet to the compute the sum of
     * @return the sum as a double
     * @throws XPathException when a non-numeric value is
     * encountered.
     */
    public double computeSum( NodeSet nodeSet )
        throws XPathException
    {
        double result = 0;
        for ( int i = 0 ; i < nodeSet.size( ); i++ ) {
            result += getNodeValue( nodeSet.item( i ) );
        }
        return result;
    } //-- computeSum
    

    /**
     * Returns the number value of the given node. The number
     * value is computed by converting the string value of 
     * node to a double. If the string value cannot be
     * successfully converted to a double, an XPath exception
     * will be thrown.
     *
     * @param node the node to calculate the number value of
     * @return the number value of the given node.
     */
    private double getNodeValue( XPathNode node )
        throws XPathException
    {
        String strValue = node.getStringValue();
        double result = 0;
        if ((strValue != null) && (strValue.length() > 0)) {
            try {
                return Double.valueOf( strValue ).doubleValue();
            } 
            catch( NumberFormatException nfe ) {
                String err = "Non-numeric value(s) encountered as arguments to " +
                    "the sum() function."; 
                throw new XPathException(err);
            }
        }
        return result;
    } //-- getNodeValue

    
} //-- SumFunctionCall

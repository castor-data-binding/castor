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
 

package org.exolab.adaptx.xpath.functions;


import org.exolab.adaptx.xpath.Parameters;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;

import org.exolab.adaptx.xpath.expressions.FunctionCall;
import org.exolab.adaptx.xpath.expressions.PrimaryExpr;

/**
 * An abstract class representing an XPath function call
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public abstract class FunctionCallImpl
    extends FunctionCall implements Parameters
{
    
    public static final String INVALID_NUMBER_PARAMS = 
        "invalid number of parameters for function: ";
        
    public static final String INVALID_RESULT =
        "unexpected result for function: ";
        
    private static String START_PARAMLIST = "(";
    private static String END_PARAMLIST   = ")";
    private static String EMPTY_PARAMLIST = "()";
    private static String PARAMETER_SEP   = ",";
    

    private int DefaultSize = 3;

    
    private XPathExpression[] params;
    
    
    /**
     * The next available location in the expr array
    **/
    private int count = 0;

    
    String name = "undefined";

    
    public FunctionCallImpl(String name) {
        super();
        this.name = name;
    } //-- FunctionCallImpl
    
    /**
     * Returns the name of this function call
     * @return the name of this function call
    **/
    public String getFunctionName() {
        return this.name;
    } //-- getFunctionName
    
    /**
     * Returns the String representation of this FunctionCall
     * @return the String representation of this FunctionCall
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer(getFunctionName());
        sb.append(START_PARAMLIST);
        for (int i = 0; i < getParameterCount(); i++) {
            if (i > 0) sb.append(PARAMETER_SEP);
            sb.append( getParameter(i));
        }
        sb.append(END_PARAMLIST);
        return sb.toString();
    } //-- toString
    

    /**
     * Adds the specified Expr to the list
     * @param expr the Expr to add to the list
     * @return true if the Expr is added to the list
    **/
    public final void addParameter( XPathExpression expr )
        throws XPathException
    {
        if ( expr == null )
            throw new XPathException( "Cannot add empty parameter" );
        if ( params == null ) {
            params = new XPathExpression[ DefaultSize ];
        } else if ( count == params.length ) {
            XPathExpression[] newParams;
            
            newParams = new XPathExpression[ count + DefaultSize ];
            for ( int i = 0 ; i < count ; ++i )
                newParams[ i ] = params[ i ];
            params = newParams;
        }
        params[ count++ ] = expr;
    } //-- add

    
    /**
     * Returns the Expr at the specified position in this list.
     * @param index the position of the Expr to return
     * @exception IndexOutOfBoundsException 
    **/
    public final XPathExpression getParameter( int index )
        throws IndexOutOfBoundsException
    {
        if ( params == null )
            throw new IndexOutOfBoundsException();
        if ( index < 0 || index >= count )
            throw new IndexOutOfBoundsException();
        return params[ index ];
    } //-- get
    
     
    /**
     * Returns the number of expressions in the List
     * @return the number of expressions in the List
    **/
    public final int getParameterCount()
    {
        return count;
    } //-- size


} //-- FunctionCall

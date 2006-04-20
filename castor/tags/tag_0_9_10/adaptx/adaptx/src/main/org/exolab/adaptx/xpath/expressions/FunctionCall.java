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
 

package org.exolab.adaptx.xpath.expressions;


import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;

/**
 * A abstract class representing an XPath function call
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public abstract class FunctionCall
    extends PrimaryExpr 
{
    
    public FunctionCall() {
        super(PrimaryExpr.FUNCTION_CALL);
    } //-- FunctionCall
    
    /**
     * Returns the name of the function
     *
     * @return the name of the function
     */
    public abstract String getFunctionName();
    
    /**
     * Returns the XPathExpression at the specified position in this list.
     *
     * @param index the position of the Expr to return
     * @exception IndexOutOfBoundsException 
     */
    public abstract XPathExpression getParameter( int index )
        throws IndexOutOfBoundsException;
    
     
    /**
     * Returns the number of expressions in the parameter list
     *
     * @return the number of expressions in the parameter list
     */
    public abstract int getParameterCount();


} //-- FunctionCall

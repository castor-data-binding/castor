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

import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;

/**
 * Represents a simple binary expression. A binary expression is
 * any expression which has a left hand side and a right hand side.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public interface BinaryExpr extends XPathExpression {


    /**
     * Returns the operator for this binary expression
     *
     * @return the operator for this binary expression
     */
    public Operator getOperator();
    
    /**
     * Returns the Expr that should be evaluated as the left hand side
     * of this BinaryExpr
     *
     * @return the Expr that should be evaluated as the left hand side
     * of this BinaryExpr
     */
    public XPathExpression getLeftSide();

    /**
     * Returns the Expr that should be evaluated as the right hand side
     * of this BinaryExpr
     *
     * @return the Expr that should be evaluated as the right hand side
     * of this BinaryExpr
     */
    public XPathExpression getRightSide();

} //-- BinaryExpr


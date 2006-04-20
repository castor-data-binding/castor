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
import org.exolab.adaptx.xpath.BooleanResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;


/**
 * This class represents an XPath predicate expression.
 * 
 * This class handles predicates as a linked list of
 * PredicateExpr, each containing the actual expression
 * used during evaluation.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public abstract class PredicateExpr
    implements XPathExpression
{

    /**
     * Returns the actual XPathExpression for this predicate
     *
     * @return the XPathExpression for this predicate
     */
    public abstract XPathExpression getExpression();
    
    /**
     * Returns the XPathExpression type 
     *
     * @return the XPathExpression type
     */
    public final short getExprType()
    {
        return XPathExpression.BOOLEAN;
    }

    
    /**
     * Returns the next PredicateExpr for this PredicateExpr
     *
     * @returns the next PredicateExpr in this PredicateExpr
     */
    public abstract PredicateExpr getNext();

    /**
     * Returns true if there are more predicate expressions
     * 
     * @return true if there are more predicate expressions
     */
    public abstract boolean hasNext();

} //-- PredicateExpr

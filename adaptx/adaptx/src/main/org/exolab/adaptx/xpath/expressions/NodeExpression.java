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
 * Represents an XPath node expression.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 */
public interface NodeExpression
    extends XPathExpression, MatchExpression
{


    /**
     * The node expression type that selects or 
     * matches "any" node.
     */
    public static final short ANY_EXPR        = 0;
    
    /**
     * The node expression type that selects or
     * matches attribute nodes
     */
    public static final short ATTRIBUTE_EXPR  = 1;
    
    /**
     * The node expression type that selects or
     * matches element nodes
     */
    public static final short ELEMENT_EXPR    = 2;
    
    /**
     * The node expression type that selects or
     * matches any element node with a specific ID
     */
    public static final short ID_EXPR         = 3;
    
    /**
     * The node expression type that selects or
     * matches the current context node
     */
    public static final short IDENTITY_EXPR   = 4;
    
    /**
     * The node expression type that selects or
     * matches the parent node of the current
     * context node.
     */
    public static final short PARENT_EXPR     = 5;
    
    /**
     * The node expression type that selects or
     * matches text nodes
     */
    public static final short TEXT_EXPR       = 6;
    
    /**
     * The node expression type that selects or
     * matches comment nodes
     */
    public static final short COMMENT_EXPR    = 7;
    
    /**
     * The node expression type that selects or
     * matches Processing Instruction nodes
     */
    public static final short PI_EXPR         = 8;
    
    /**
     * The node expression type that selects or
     * matches any element node
     */
    public static final short WILDCARD_EXPR   = 9;
    
    /**
     * The node expression type that selects or
     * matches namespace nodes
     */
    public static final short NAMESPACE_EXPR  = 10;
    
    
    /**
     * Returns the type of this Node exprression, the value
     * must be one of the value Node expression types.
     *
     * @return the type of this Node expression
     */
    public short getNodeExprType();

    
    /**
     * Returns the QName matched by this NodeExpression.
     * The value may be null, for example if this is a
     * TEXT_EXPR or a WILDCARD_EXPR.
     *
     * @return the QName matched by this NodeExpression.
     */
    public String getName();
    
    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @throws XPathException when an error occurs during
     * evaluation
     */
    public boolean matches( XPathNode node, XPathContext context )
        throws XPathException;

    
} //-- NodeExpression

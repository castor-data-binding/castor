/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
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
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.expressions.NodeExpression;

/**
 * Represents an element node expression
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
class ElementExpr
    extends NodeExpressionImpl
{
    
    //-- BEGIN TEMPORARY
    private static final String NS_WILDCARD_PREFIX = "*:";
    //-- END TEMPORARY
    
    private static final String WILD_CARD = "*";
    
    
    private String _name = null;
    
    private boolean _namespaceIsWild = false;
    
    private String _prefix = null;
    
    private boolean _nameIsWild = false;
    
    
      //----------------/
     //- Constructors -/
    //----------------/
    

    
    ElementExpr( String qname )
        throws XPathException
    {
        int index;

        if ( qname == null )
            throw new XPathException( "Argument qname is null" );
        if ( qname.equals( WILD_CARD ) ) {
            _nameIsWild = true;
            _namespaceIsWild = true;
        } 
        else {
            _name = qname;
        
            //-- Quick fix for namespace wildcards
            //-- This code should be moved to the ExprLever and
            //-- ExprParser
            if (qname.startsWith(NS_WILDCARD_PREFIX)) {
                _name = qname.substring(2);
                _namespaceIsWild = true;
                _prefix = WILD_CARD;
            }
            else {
                int idx = qname.indexOf(':');
                if (idx > 0) {
                    _prefix = qname.substring(0,idx);
                    _name   = qname.substring(idx+1);
                }
                if (_name.equals(WILD_CARD)) _nameIsWild = true;
            }
        }
        
    } //-- ElementExpr

      //------------------/
     //- Public Methods -/
    //------------------/
    
    
    /**
     * Determines the priority of a PatternExpr as follows:
     * <PRE>
     *  From the 19991116 XSLT 1.0 Recommendation:
     *  + If the pattern has the form of a QName preceded by a
     *    ChildOrAttributeAxisSpecifier or has the form 
     *    processing-instruction(Literal) then the priority is 0.
     *  + If the pattern has the form NCName:* preceded by a 
     *    ChildOrAttributeAxisSpecifier, then the priority is -0.25
     *  + Otherwise if the pattern consists of just a NodeTest 
     *    preceded by a ChildOrAttributeAxisSpecifier then the
     *    priority is -0.5
     *  + Otherwise the priority is 0.5
     * </PRE>
     * @return the priority for this PatternExpr
    **/
    public double getDefaultPriority() {
        if (_namespaceIsWild) {
            return -0.5;
        }
        else if (_nameIsWild) return -0.25;
        return 0;
    } //-- getDefaultPriority
    
    /**
     * Returns the String representation of this NodeExpr
    **/
    public String toString()
    {
        return getName();
    } //-- toString
    
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
        NodeSet nodeSet = context.newNodeSet();
        XPathNode node = context.getNode();
        if ( node == null )
            return nodeSet;
            
        XPathNode child = node.getFirstChild();
        while ( child != null ) {
            if (matches(child, context)) {
                nodeSet.add( child );
            }
            child = child.getNext();
        }
        return nodeSet;
    } //-- evaluate
    

    public short getExprType()
    {
        return XPathExpression.NODE_TEST;
    }
    
    
    public String getName()
    {
        if (_prefix == null) return _name;
        return _prefix + ':' + _name;
    }

        
    /**
     * Returns the type of this NodeExpr
     * @return the type of this NodeExpr
    **/
    public short getNodeExprType()
    {
        return NodeExpression.ELEMENT_EXPR;
    }
    
    
    /**
     * Determines if the given node is matched by this MatchExpr with
     * respect to the given context.
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this MatchExpr
     * @exception XPathException when an error occurs during
     * evaluation
    **/
    public boolean matches( XPathNode node, XPathContext context )
        throws XPathException
    {
        if ( node == null ) return false;

        if (node.getNodeType() == XPathNode.ELEMENT) {
            String nodeName = node.getLocalName();
            
            if (_namespaceIsWild) {
                if (_nameIsWild) return true;
                return nodeName.equals(_name);
            }
            else {
                if (_prefix == null) {
                    String ns = node.getNamespaceURI();
                    if ((ns != null) && (ns.length() > 0)) {
                        return false;
                    }
                    return (_nameIsWild || nodeName.equals(_name));
                }
                
                if(!_nameIsWild) {
                    if (!nodeName.equals(_name)) return false;
                }
                
                String ns = context.getNamespaceURI(_prefix);
                if (ns == null) {
                    throw new XPathException("Unable to resolve "+
                        "namespace for prefix: " + _prefix);
                }
                //-- compare namespaces
                return ns.equals(node.getNamespaceURI());
                
            }
        }
        return false;
        
    } //-- matches

    
} //-- ElementExpr

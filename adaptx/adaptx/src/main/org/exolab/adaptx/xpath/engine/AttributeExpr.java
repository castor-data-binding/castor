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
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.expressions.NodeExpression;

/**
 * Represents an XPath Attribute expression
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
class AttributeExpr
    extends NodeExpressionImpl
{

    
    private static final String WILD_CARD = "*";
    
    private static final String UNRESOLVABLE_PREFIX_ERROR =
        "Unable to resolve namespace from prefix: ";
        
      //--------------------/
     //- Member Variables -/
    //--------------------/
    
    private final String _local;


    private final String _prefix;
    
      //----------------/
     //- Constructors -/
    //----------------/
    

    /**
     * Creates a new AttributeExpr using the given name
     *
     * @param qname the name of the attribute in QName form.
    **/
    AttributeExpr( String qname )
        throws XPathException
    {
        int index;

        if (qname== null)
            throw new XPathException( "Argument qname is null" );
        if ( qname.equals( WILD_CARD ) ) {
            _local = WILD_CARD;
            _prefix = null;
        } else {
            index = qname.indexOf( ':' );
            if ( index >= 1 ) {
                _local = qname.substring( index + 1 );
                _prefix = qname.substring( 0, index );
            } else {
                _local = qname;
                _prefix = null;
            }
        }
    } //-- AttributeExpr
    
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
        if (WILD_CARD.equals(_local)) 
            return -0.25;
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
    **/
    public XPathResult evaluate( XPathContext context )
        throws XPathException 
    {
        XPathNode node = context.getNode();
        
        if ( node == null ) 
            return context.newNodeSet();
        
        if ( node.getNodeType() == XPathNode.ELEMENT ) {
            //-- if wild, then select all attributes
            if (WILD_CARD.equals(_local)) {
                NodeSet nodes = context.newNodeSet();
                XPathNode attr = node.getFirstAttribute();
                while ( attr != null ){
                    nodes.add( attr );
                    attr = attr.getNext();
                }
                return nodes;
            } 
            //-- otherwise, select only matching attributes
            else {
                String uri = "";
                if (_prefix != null) {
                    uri = context.getNamespaceURI(_prefix);
                    if (uri == null) {
                        String err = UNRESOLVABLE_PREFIX_ERROR + _prefix;
                        throw new XPathException(err);
                    }
                }
                XPathNode attr = node.getFirstAttribute();
                while ( attr != null ) {
                    if (attr.getLocalName().equals( _local ) &&
                       nsEquals(uri, attr.getNamespaceURI()))
                    {
                        NodeSet nodes = context.newNodeSet( 1 );
                        nodes.add( attr );
                        return nodes;
                    }
                    attr = attr.getNext();
                }
            }
        }
        return context.newNodeSet();        
    } //-- evaluate

    /**
     * Returns the expression type of this XPathExpression
     *
     * @return the expression type of this XPathExpression
    **/
    public short getExprType() { 
        return XPathExpression.STEP; 
    } //-- getExprType
    
    /**
     * Returns the name of the attribute that this AttributeExpr matches
     * or selects.
     *
     * @return the name of the attribute that this AttributeExpr matches
    **/
    public String getName() {
        return _prefix == null ? _local : _prefix + ':' + _local; 
    } //-- getName

        
    /**
     * Returns the type of this NodeExpr
     *
     * @return the type of this NodeExpr
    **/
    public short getNodeExprType() {
        return NodeExpression.ATTRIBUTE_EXPR;
    } //-- getNodeExprType
    
    
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
        if ( node == null )
            return false;
            
        String uri = "";
        if (_prefix != null) {
            uri = context.getNamespaceURI(_prefix);
            if (uri == null) {
                String err = UNRESOLVABLE_PREFIX_ERROR + _prefix;
                throw new XPathException(err);
            }
        }
        if ( node.getNodeType() == XPathNode.ATTRIBUTE ) {
            return ( WILD_CARD.equals(_local) ||
                     ( node.getLocalName().equals( _local ) &&
                       nsEquals(uri, node.getNamespaceURI() ) ) );
        }
        return false;
    } //-- matches

    /**
     * Compares the two namespaces and returns true if they
     * are equal.
     *
     * @return true if the two namespaces are equal
    **/
    private boolean nsEquals(String ns1, String ns2) {
        if (ns1 == null) {
            return ((ns2 == null) || (ns2.length() == 0));
        }
        else if (ns2 == null) {
            return (ns1.length() == 0);
        }
        else {
            return ns1.equals(ns2);
        }
    } //-- nsEquals
} //-- AttributeExpr

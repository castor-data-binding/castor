/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
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


/**
 * This class represents a Location Step as defined by XPath
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class LocationStep
    extends FilterBase
{


    public static final short ANCESTORS_AXIS           = 0;
    public static final short ANCESTORS_OR_SELF_AXIS   = 1;
    public static final short ATTRIBUTES_AXIS          = 2;
    public static final short CHILDREN_AXIS            = 3;
    public static final short DESCENDANTS_AXIS         = 4;
    public static final short DESCENDANTS_OR_SELF_AXIS = 5;
    public static final short FOLLOWING_AXIS           = 6;
    public static final short FOLLOWING_SIBLINGS_AXIS  = 7;
    public static final short PARENT_AXIS              = 8;
    public static final short PRECEDING_AXIS           = 9;
    public static final short PRECEDING_SIBLINGS_AXIS  = 10;
    public static final short SELF_AXIS                = 11;
    public static final short NAMESPACE_AXIS           = 12;

    private static final String AXIS_SEPARATOR = "::";


    private short    axisIdentifier = CHILDREN_AXIS;


    private NodeExpression nodeExpr       = null;


    /**
     * This LocationStep may be used to wrap an ErrorExpr
    **/
    private ErrorExpr error         = null;

    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new LocationStep
    **/
    protected LocationStep( int ancestryOp )
    {
        super( ancestryOp );
    } //-- LocationStep

    /**
     * Creates a new LocationStep wrapper for the given ErrorExpr.
     * This allows errors to be deferred until run-time.
     * @param error the ErrorExpr to "wrap".
    **/
    protected LocationStep( ErrorExpr error )
    {
        super( NO_OP );
        this.error = error;
    } //-- LocationStep
    

    /**
     * Creates a new LocationStep with the given AxisIdentifier
    **/
    protected LocationStep( int ancestryOp, short axisIdentifier)
    {
        super( ancestryOp );
        this.axisIdentifier = axisIdentifier;
    } //-- LocationStep


      //------------------/
     //- Public Methods -/
    //------------------/


    public short getExprType()
    {
        return XPathExpression.STEP;
    }


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
        if ( error != null ) error.evaluate( context );
        
        XPathNode node = context.getNode();
        
        if ( node == null || nodeExpr == null )
            return context.newNodeSet();

        NodeSet nodeSet = null;
        //-- process axis
        switch ( axisIdentifier ) {
            case ANCESTORS_AXIS:
                nodeSet = AxisIdentifier.fromAncestors( nodeExpr, context );
                break;
            case ANCESTORS_OR_SELF_AXIS:
                nodeSet = AxisIdentifier.fromAncestorsOrSelf( nodeExpr, context );
                break;
            case ATTRIBUTES_AXIS:
                nodeSet = AxisIdentifier.fromAttributes( nodeExpr, context );
                break;
            case NAMESPACE_AXIS:
                nodeSet = AxisIdentifier.fromNamespaces( nodeExpr, context );
                break;
            case DESCENDANTS_AXIS:
                nodeSet = AxisIdentifier.fromDescendants( nodeExpr, context );
                break;
            case DESCENDANTS_OR_SELF_AXIS:
                nodeSet = AxisIdentifier.fromDescendantsOrSelf( nodeExpr, context );
                break;
            case FOLLOWING_AXIS:
                nodeSet = AxisIdentifier.fromFollowing( nodeExpr, context );
                break;
            case FOLLOWING_SIBLINGS_AXIS:
                nodeSet = AxisIdentifier.fromFollowingSiblings( nodeExpr, context );
                break;
            case PARENT_AXIS:
                nodeSet = AxisIdentifier.fromParent( nodeExpr, context );
                break;
            case PRECEDING_AXIS:
                nodeSet = AxisIdentifier.fromPreceding( nodeExpr, context );
                break;
            case PRECEDING_SIBLINGS_AXIS:
                nodeSet = AxisIdentifier.fromPrecedingSiblings( nodeExpr, context );
                break;
            case SELF_AXIS:
                nodeSet = AxisIdentifier.fromSelf( nodeExpr, context );
                break;
            default: // children
                nodeSet = AxisIdentifier.fromChildren( nodeExpr, context );
                break;
        } //-- end switch
        
        //-- filter nodes
        if ( hasPredicates() ) {
            evaluatePredicates( nodeSet, context );
        }
        return nodeSet;
    } //-- evaluate


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
    public double getDefaultPriority()
    {
        if ( nodeExpr == null )
            return -0.5;

        double priority = -0.5;
        
        switch( nodeExpr.getNodeExprType() ) {
            case NodeExpression.PI_EXPR:
                priority = 0;
                break;
            case NodeExpression.ELEMENT_EXPR:
                priority = ((ElementExpr)nodeExpr).getDefaultPriority();
                break;
            case NodeExpression.ATTRIBUTE_EXPR:
                priority = ((AttributeExpr)nodeExpr).getDefaultPriority();
                break;
            default:
                break;
        }
        
        if ( getPredicate() != null )
            priority = 0.5;
            
        return priority;
    } //-- getDefaultPriority


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
        if ( nodeExpr == null )
            return false;
            
        if (hasPredicates()) {
            NodeSet nodes = (NodeSet) evaluate( context );
            return nodes.contains(node);
        }
        else {
            return nodeExpr.matches(node, context);
        }
        
    } //-- matches


    /**
     * Returns the String representation of this LocationStep
     * @return the String representation of this LocationStep
    **/
    public String toString() {
        
        StringBuffer strbuf = new StringBuffer();
        
        boolean useSeparator = true;
        
        switch ( axisIdentifier ) {
            case ANCESTORS_AXIS:
                strbuf.append( Names.ANCESTORS_AXIS );
                break;
            case ANCESTORS_OR_SELF_AXIS:
                strbuf.append( Names.ANCESTORS_OR_SELF_AXIS );
                break;
            case ATTRIBUTES_AXIS:
                strbuf.append( Names.ATTRIBUTES_AXIS );
                break;
            case DESCENDANTS_AXIS:
                strbuf.append( Names.DESCENDANTS_AXIS );
                break;
            case DESCENDANTS_OR_SELF_AXIS:
                strbuf.append( Names.DESCENDANTS_OR_SELF_AXIS );
                break;
            case FOLLOWING_AXIS:
                strbuf.append( Names.FOLLOWING_AXIS );
                break;
            case FOLLOWING_SIBLINGS_AXIS:
                strbuf.append( Names.FOLLOWING_SIBLINGS_AXIS );
                break;
            case PARENT_AXIS:
                strbuf.append( Names.PARENT_AXIS );
                break;
            case PRECEDING_AXIS:
                strbuf.append( Names.PRECEDING_AXIS );
                break;
            case PRECEDING_SIBLINGS_AXIS:
                strbuf.append( Names.PRECEDING_SIBLINGS_AXIS );
                break;
            case SELF_AXIS:
                strbuf.append( Names.SELF_AXIS );
                break;
            case NAMESPACE_AXIS:
                strbuf.append( Names.NAMESPACE_AXIS );
                break;
            default:
                //-- CHILDREN AXIS
                useSeparator = false;
                break;
        } //-- switch
        
        if (useSeparator) strbuf.append( AXIS_SEPARATOR );

        if ( nodeExpr == null )
            strbuf.append( super.toString() );
        else {
            strbuf.append( nodeExpr.toString() );
            strbuf.append( super.toString() );
        }
        
        return strbuf.toString();
    } //-- toString


    public short getAxisIdentifier()
    {
        return this.axisIdentifier;
    } //-- getAxisIdentifier


      //---------------------/
     //- Protected Methods -/
    //---------------------/

    protected NodeExpression getNodeExpr()
    {
        return this.nodeExpr;
    } //-- getNodeExpr


    public void setAxisIdentifier( short axisIdentifier )
    {
        this.axisIdentifier = axisIdentifier;
    } //-- setAxisIdentifier


    protected void setNodeExpr( NodeExpression nodeExpr )
    {
        this.nodeExpr = nodeExpr;
    } //-- setNodeExpr


} //-- LocationStep

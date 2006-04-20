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
import org.exolab.adaptx.xpath.expressions.LocationStep;
import org.exolab.adaptx.xpath.expressions.NodeExpression;


/**
 * This class represents a Location Step as defined by XPath 1.0
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
class LocationStepImpl extends AbstractPathComponent
    implements LocationStep
{


    private static final String AXIS_SEPARATOR = "::";


    /**
     * The axis-identifier
     */
    private short _axisIdentifier = LocationStep.CHILDREN_AXIS;


    /**
     * The node expression
     */
    private NodeExpression _nodeExpr  = null;


    /**
     * This LocationStep may be used to wrap an ErrorExpr
     */
    private ErrorExpr _error = null;

    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new LocationStep with the given ancestry operator
     */
    protected LocationStepImpl()
    {
        super();
        
    } //-- LocationStep

    /**
     * Creates a new LocationStep wrapper for the given ErrorExpr.
     * This allows errors to be deferred until run-time.
     * @param error the ErrorExpr to "wrap".
     */
    protected LocationStepImpl( ErrorExpr error )
    {
        super();
        _error = error;
    } //-- LocationStepImpl
    

    /**
     * Creates a new LocationStep with the given AxisIdentifier
     */
    protected LocationStepImpl(short axisIdentifier)
    {
        super();
        _axisIdentifier = axisIdentifier;
    } //-- LocationStepImpl


      //------------------/
     //- Public Methods -/
    //------------------/


    /**
     * Returns the XPathExpression type
     *
     * @return the XPathExpression type
     */
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
     */
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        if ( _error != null ) _error.evaluate( context );
        
        XPathNode node = context.getNode();
        
        if ( node == null || _nodeExpr == null )
            return context.newNodeSet();

        NodeSet nodeSet = null;
        //-- process axis
        switch ( _axisIdentifier ) {
            case ANCESTORS_AXIS:
                nodeSet = AxisIdentifier.fromAncestors( _nodeExpr, context );
                break;
            case ANCESTORS_OR_SELF_AXIS:
                nodeSet = AxisIdentifier.fromAncestorsOrSelf( _nodeExpr, context );
                break;
            case ATTRIBUTES_AXIS:
                nodeSet = AxisIdentifier.fromAttributes( _nodeExpr, context );
                break;
            case NAMESPACE_AXIS:
                nodeSet = AxisIdentifier.fromNamespaces( _nodeExpr, context );
                break;
            case DESCENDANTS_AXIS:
                nodeSet = AxisIdentifier.fromDescendants( _nodeExpr, context );
                break;
            case DESCENDANTS_OR_SELF_AXIS:
                nodeSet = AxisIdentifier.fromDescendantsOrSelf( _nodeExpr, context );
                break;
            case FOLLOWING_AXIS:
                nodeSet = AxisIdentifier.fromFollowing( _nodeExpr, context );
                break;
            case FOLLOWING_SIBLINGS_AXIS:
                nodeSet = AxisIdentifier.fromFollowingSiblings( _nodeExpr, context );
                break;
            case PARENT_AXIS:
                nodeSet = AxisIdentifier.fromParent( _nodeExpr, context );
                break;
            case PRECEDING_AXIS:
                nodeSet = AxisIdentifier.fromPreceding( _nodeExpr, context );
                break;
            case PRECEDING_SIBLINGS_AXIS:
                nodeSet = AxisIdentifier.fromPrecedingSiblings( _nodeExpr, context );
                break;
            case SELF_AXIS:
                nodeSet = AxisIdentifier.fromSelf( _nodeExpr, context );
                break;
            default: // children
                nodeSet = AxisIdentifier.fromChildren( _nodeExpr, context );
                break;
        } //-- end switch
        
        //-- filter nodes
        if ( hasPredicates() ) {
            evaluatePredicates( nodeSet, context );
        }
        return nodeSet;
    } //-- evaluate

    /**
     * Returns the axis-identifier for this LocationStep.
     *
     * @return the axis-identifier for this LocationStep.
     */
    public short getAxisIdentifier()
    {
        return _axisIdentifier;        
    } //-- getAxisIdentifier
    
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
        if ( _nodeExpr == null )
            return -0.5;

        double priority = -0.5;
        
        switch( _nodeExpr.getNodeExprType() ) {
            case NodeExpression.PI_EXPR:
                priority = 0;
                break;
            case NodeExpression.ELEMENT_EXPR:
                priority = ((ElementExpr)_nodeExpr).getDefaultPriority();
                break;
            case NodeExpression.ATTRIBUTE_EXPR:
                priority = ((AttributeExpr)_nodeExpr).getDefaultPriority();
                break;
            default:
                break;
        }
        
        if ( getPredicate() != null )
            priority = 0.5;
            
        return priority;
    } //-- getDefaultPriority

    /**
     * Returns the sub-expression encapsulated by this PathComponent.
     * the sub-expression will either be a PrimaryExpr if this
     * PathComponent is a FilterExpr, or a NodeExpression if this
     * Pathcomponent is a LocationStep. This method may return
     * null if no such sub-expression exists for the PathComponent.
     *
     * @return the sub-expression encapsulated by this PathComponent.
     */
    public XPathExpression getSubExpression() {
        return getNodeExpr();
    } //-- getSubExpression

    /**
     * Returns the NodeExpression for this LocationStep.
     *
     * @return the NodeExpression for this LocationStep.
     */
    public NodeExpression getNodeExpr() {
        return _nodeExpr;
    } //-- getNodeExpr

    /**
     * Returns true if this PathComponent is a FilterExpr.
     * Note that if this method returns true, then a call to 
     * #isLocationStep must return false.
     *
     * @return true if this PathComponent is a FilterExpr
     * @see isLocationStep
     */
    public boolean isFilterExpr() {
        return false;
    } //-- isFilterExpr
    
    /**
     * Returns true if this PathComponent is a LocationStep.
     * Note that if this method returns true, then a call to 
     * #isFilterExpr must return false.
     *
     * @return true if this PathComponent is a LocationStep
     * @see isFilterExpr
     */
    public boolean isLocationStep() {
        return true;
    } //-- isLocationStep

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
        if ( _nodeExpr == null )
            return false;
            
        if (hasPredicates()) {
            NodeSet nodes = (NodeSet) evaluate( context );
            return nodes.contains(node);
        }
        else {
            return _nodeExpr.matches(node, context);
        }
        
    } //-- matches


    /**
     * Returns the String representation of this LocationStep
     * @return the String representation of this LocationStep
     */
    public String toString() {
        
        StringBuffer strbuf = new StringBuffer();
        
        boolean useSeparator = true;
        
        switch ( _axisIdentifier ) {
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

        if ( _nodeExpr == null )
            strbuf.append( super.toString() );
        else {
            strbuf.append( _nodeExpr.toString() );
            strbuf.append( super.toString() );
        }
        
        return strbuf.toString();
    } //-- toString



      //---------------------/
     //- Protected Methods -/
    //---------------------/



    void setAxisIdentifier( short axisIdentifier )
    {
        _axisIdentifier = axisIdentifier;
    } //-- setAxisIdentifier


    void setNodeExpr( NodeExpression nodeExpr )
    {
        _nodeExpr = nodeExpr;
    } //-- setNodeExpr


} //-- LocationStep

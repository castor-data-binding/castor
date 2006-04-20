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
 * This class represents a PathExpr and LocationPath 
 * pattern
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class PathExpr
    extends XPathExpression
    implements MatchExpression
{


    public final int ABSOLUTE = 0;
    public final int RELATIVE = 1;
    
    
    /**
     * A PathExpr can be used to wrap an ErrorExpr
    **/
    private ErrorExpr error = null;

    
    /**
     * The local FilterExpr of this PathExpr
    **/
    private FilterBase _filter = null;

    
    /**
     * The subpath of this PathExpr
    **/
    private PathExpr _subPath = null;

    
    private PathExpr parent  = null;
    

    /**
     * Creates a new PathExpr with will not match any node, and will evaluate
     * to an empty NodeSet
    **/
    public PathExpr()
    {
    } //-- PathExpr
    

    /**
     * Creates a new PathExpr wrapper for an ErrorExpr.
     * This allows deferred errors until run-time.
     * @param error the ErrorExpr to wrap
    **/
    public PathExpr( ErrorExpr error )
    {
        this.error = error;
    } //-- PathExpr
    

    /**
     * Creates a new PathExpr with the given FilterBase
    **/
    public PathExpr( FilterBase filterBase )
    {
        super();
        _filter = filterBase;
    } //-- PathExpr


    /**
     * Creates a new PathExpr with the given FilterBase and PathExpr
    **/
    public PathExpr( FilterBase filterBase, PathExpr pathExpr )
    {
        _filter = filterBase;
        _subPath = pathExpr;
        _subPath.parent = this;
    } //-- PathExpr
    
    
      //------------------/    
     //- Public Methods -/
    //------------------/    

    
    /**
     * Returns the type of Expr this Expr represents
     * @return the type of Expr this Expr represents
    **/
    public short getExprType()
    {
        return XPathExpression.PATH_EXPR;
    } //-- getExprType
    

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
        if ( error != null )
            error.evaluate( context );
            
        return evaluate( context, true );
        
    } //-- evaluate
    

    /**
     * Evaluates this Expr using the given context Node and ExprContext
     * @param context the current context Node
     * @param exprContext the ExprContext which has additional
     * information sometimes needed for evaluating XPath expressions
     * environment
     * @return the ExprResult
     * @exception InvalidExprException when an invalid expression is
     * encountered during evaluation
    **/
    protected NodeSet evaluate( XPathContext context, boolean start )
        throws XPathException 
    {
        XPathNode node = context.getNode();
        
        if ( _filter == null || node == null) {
            return context.newNodeSet(0);
        }
        
        XPathContext xpContext = context;
        if (start) {
            if ((isAbsolute()) && (node.getNodeType() != XPathNode.ROOT)) {
                node = node.getRootNode();
                xpContext = context.newContext(node);
            }
        }
        
        NodeSet nodes = (NodeSet) _filter.evaluate( xpContext );
        
        if ( _subPath != null && nodes.size() > 0 ) {
            
            NodeSet tmpNodes = xpContext.newNodeSet();
            
            XPathContext tmpContext = xpContext.newContext(nodes, 0);
            for ( int j = 0 ; j < nodes.size() ; j++ ) {
                tmpContext.setPosition(j);
                tmpNodes.add(_subPath.evaluate( tmpContext, false ));
            }
            nodes = tmpNodes;
        }
        
        if ( nodes == null )
            nodes = context.newNodeSet(0);
            
        return nodes;
    } //-- evaluate(context, ExprContext, boolean)
    
    public FilterBase getFilter()
    {
        return _filter;
    }

    public void setFilter( FilterBase filterBase )
    {
        this._filter = filterBase;
    } //-- setFilter
    

    public void setSubPath( PathExpr pathExpr )
    {
        if ( _subPath != null )
            _subPath.parent = null;
        _subPath = pathExpr;
        if ( _subPath != null )
            _subPath.parent = this;
    } //-- setSubPath
    

    /**
     * Returns the String representation of this PathExpr
     * @return the String representation of this PathExpr
    **/
    public String toString()
    {
        return toString( null );
    } //-- toString


    private String toString( StringBuffer sb )
    {
        if ( sb == null )
            sb = new StringBuffer();
        if ( _filter != null ) {
            switch ( _filter.getAncestryOp( )) {
                case FilterBase.PARENT_OP:
                    sb.append( "/" );
                    break;
                default:
                    break;                
            }
            sb.append( _filter.toString() );
        }
        if ( _subPath != null )
            _subPath.toString( sb );
        return sb.toString();
    } //-- toString
    
    
    
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
        if ( _subPath != null )
            return 0.5;
        return _filter.getDefaultPriority();
    } //-- getDefaultPriority
    
    
    /**
     * Determines if this PathExpr Represents an AbsolutePathExpr or not
    **/
    public boolean isAbsolute()
    {
        if ( _filter == null )
            return true;
        return ( _filter.getAncestryOp() != FilterBase.NO_OP );
    } //-- isAbsolute
    

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
        
        if ( error != null ) error.evaluate( context );
        
        if ( node == null || _filter == null )
            return false;
        
        XPathContext tmpContext = context.newContext(node);
        
        //-- for performance reasons I've duplicated some
        //-- code here. If we don't have any subpaths,
        //-- there is no reason to create NodeSets and
        //-- go through the while loop below. This results
        //-- in significant performance gains. :-)
        
        if ( _subPath == null ) {
            
            boolean useAncestors = hasDescendantsAxis(_filter);
            
            //-- select node's parent or ancestors
            switch ( _filter.getAncestryOp() ) {
                
                case FilterBase.PARENT_OP: {
                    XPathNode parent = node.getParentNode();
                    NodeSet nodes = tmpContext.getNodeSet();
                    while ( parent != null ) {
                        nodes.clear();
                        nodes.add(parent);
                        if (_filter.matches( node, tmpContext )) {
                            return true;
                        }
                        parent = (useAncestors) ? parent.getParentNode() : null;
                    }
                    break;
                }
                default: 
                {
                    //-- This case checks to see if there is
                    //-- any ancestor-or-self context which
                    //-- would allow the given node to be matched.
                    XPathNode ancestor = node;
                    NodeSet nodes = tmpContext.getNodeSet();
                    while ( ancestor != null )  {
                        nodes.clear();
                        nodes.add(ancestor);
                        if ( _filter.matches( node, tmpContext ) ) 
                            return true;
                        ancestor = ancestor.getParentNode();
                    }
                    break;
                }
            }
            return false;
        }
        
        //-- we have subpaths...
        
        PathExpr px = this;
        while ( px._subPath != null ) 
            px = px._subPath;
        
        //-- After much testing, on my stylesheets,
        //-- i've realized just how expensive creating
        //-- and destroying objects are...so I am
        //-- using a small number for the NodeSets, because
        //-- on the typical case, a NodeSet never needs
        //-- to be larger than 1-3 nodes. I was using
        //-- a value of 7 which took about 33% longer
        //-- on average to process this method. I might
        //-- rewrite it to use no NodeSets if possible.
        NodeSet current   = context.newNodeSet( 2 );
        NodeSet ancestors = context.newNodeSet( 2 );
        NodeSet tmp       = null;
        
        current.add(node);

        while ( px != null ) {
            
            FilterBase filter = px._filter; 
            
            boolean useAncestors = hasDescendantsAxis(filter);
            
            for ( int i = 0 ; i < current.size() ; i++ ) {
                XPathNode tnode = current.item(i);

                //-- select node's parent or ancestors
                switch ( filter.getAncestryOp() ) {
                    
                    case FilterBase.PARENT_OP: {
                        XPathNode parent = tnode.getParentNode();
                        while ( parent != null ) {
                            NodeSet nodes = tmpContext.getNodeSet();
                            nodes.clear();
                            nodes.add(parent);
                            if ( filter.matches( tnode, tmpContext ) )
                                ancestors.add( parent );
                                
                            parent = (useAncestors) ? parent.getParentNode() : null;
                        }
                        break;
                    }
                    default:
                        if ( px == this ) {
                            //-- This case checks to see if there is
                            //-- any ancestor-or-self context which
                            //-- would allow the given node to be matched.
                            XPathNode ancestor = tnode;
                            NodeSet nodes = tmpContext.getNodeSet();
                            while ( ancestor != null )  {
                                nodes.clear();
                                nodes.add(ancestor);
                                if ( filter.matches( tnode, tmpContext ) ) 
                                    return true;
                                ancestor = ancestor.getParentNode();
                            }
                        }
                        else 
                            return false; //-- error in expression
                } //-- switch(op)
            } //-- for
            
            if ( ancestors.size() == 0 ) 
                return false;
                
            //-- swap
            current.clear();
            tmp       = current;
            current   = ancestors;
            ancestors = tmp;
            
            if ( px == this )
                break;
            else
                px = px.parent;
        } //-- while

        if ( isAbsolute() ) {
            //-- make sure owning document exists in
            //-- set of context nodes
            XPathNode doc = node.getRootNode();
            return current.contains( doc );
        }
        return ( current.size() > 0 );
    } //-- matches
        
    //-------------------//
    //- Private Methods -//
    //-------------------//
    
    /**
     * Returns true if the given filter is a LocationStep
     * with a from-descendants or from-descendants-or-self axis
     * identifier.
     *
     * @return true if the given filter has an axis of
     * from-descendants or from-descendants-or-self.
    **/
    private boolean hasDescendantsAxis(FilterBase filter) {
        if (filter.getExprType() == XPathExpression.STEP) {
            LocationStep step = (LocationStep)filter;
            int axis = step.getAxisIdentifier();
            return ((axis == LocationStep.DESCENDANTS_AXIS) ||
                    (axis == LocationStep.DESCENDANTS_OR_SELF_AXIS));
        }
        return false;
    } //-- hasDescendantAxis
    
} // -- PathExpr

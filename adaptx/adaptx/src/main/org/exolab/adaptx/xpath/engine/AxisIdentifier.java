/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
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
 * Axis Identifier Functions
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
final class AxisIdentifier {

    /**
     * Selects all the Ancestors of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the ancestors of the context node
     * that match the given MatchExpr
    **/
    public static NodeSet fromAncestors
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        NodeSet nodeSet = context.newNodeSet();
        XPathNode parent = context.getNode().getParentNode();
        while (parent != null) {
            if (matchExpr.matches(parent, context)) {
                nodeSet.add(parent);
            }
            parent = parent.getParentNode();
        }
        return nodeSet;
    } //-- fromAncestors
    
    /**
     * Selects all the Ancestors of the context node, including the
     * context node that match the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the ancestors of the context node
     * and including the context node, that match the given MatchExpr
    **/
    public static NodeSet fromAncestorsOrSelf
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        NodeSet nodeSet = fromAncestors(matchExpr, context);
        XPathNode node = context.getNode();
        if (matchExpr.matches(node, context)) 
            nodeSet.add(node);
        return nodeSet;
        
    } //-- fromAncestorsOrSelf
    
    /**
     * Selects all the attributes of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @param ps the current ProcessorState
     * @return a NodeSet containing all the attributes of the context node
     * that match the given MatchExpr
    **/
    public static NodeSet fromAttributes
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        XPathNode node = context.getNode();
        if (node.getNodeType() != XPathNode.ELEMENT) 
            return context.newNodeSet();
            
        XPathNode attr = node.getFirstAttribute();
        NodeSet nodeSet = context.newNodeSet();
        while (attr != null ) {
            if (matchExpr.matches(attr, context))
                nodeSet.add(attr);
            attr = attr.getNext();
        }
        return nodeSet;
    } //-- fromAttributes
     
    /**
     * Selects all the namespaces of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the namespaces of the context node
     * that match the given MatchExpr
    **/
    
    public static NodeSet fromNamespaces
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        XPathNode node = context.getNode();
        if (node.getNodeType() != XPathNode.ELEMENT) 
            return context.newNodeSet();
            
        XPathNode ns = node.getFirstNamespace();
        NodeSet nodeSet = context.newNodeSet();
        while (ns != null ) {
            if (matchExpr.matches(ns, context))
                nodeSet.add(ns);
            ns = ns.getNext();
        }
        return nodeSet;
    } //-- fromAttributes

    /**
     * Selects all the children of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the children of the context node
     * that match the given MatchExpr
    **/
    public static NodeSet fromChildren 
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        NodeSet nodeSet = context.newNodeSet();
        
        XPathNode node = context.getNode();
        
        XPathNode child = node.getFirstChild();
        while ( child != null ) {
            if ( matchExpr.matches( child, context ) )
                nodeSet.add( child );
            child = child.getNext();
        }
        return nodeSet;
    } //-- fromChildren


    /**
     * Selects all the descendants of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the descendants of the context node
     * that match the given MatchExpr
     * <BR />
     * <B>Note</B> this could be an expensive operation
    **/
    public static NodeSet fromDescendants
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        return fromDescendants(matchExpr, context.getNode(), context);
        
    } //-- fromDecendants

    /**
     * Selects all the descendants of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the descendants of the context node
     * that match the given MatchExpr
     * <BR />
     * <B>Note</B> this could be an expensive operation
    **/
    private static NodeSet fromDescendants
        (MatchExpression matchExpr, XPathNode node, XPathContext context) 
        throws XPathException
    {
        NodeSet nodeSet = context.newNodeSet();
        if (node == null) return nodeSet;
                
        XPathNode child = node.getFirstChild();
        while (child != null) {
            if (matchExpr.matches(child, context))
                nodeSet.add(child);
            //-- check childs descendants
            if (child.hasChildNodes()) {
                NodeSet temp = fromDescendants(matchExpr, child, context);
                nodeSet.add(temp);
            }
            child = child.getNext();
        }
        return nodeSet;
    } //-- fromDecendants
    
    /**
     * Selects all the descendants of the context node, including the
     * context node, that match the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the descendants of the context node,
     * including the context node, that match the given MatchExpr
     * <BR />
     * <B>Note</B> this could be an expensive operation
    **/
    public static NodeSet fromDescendantsOrSelf
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        NodeSet nodeSet = fromDescendants(matchExpr, context);
        XPathNode node = context.getNode();
        if (matchExpr.matches(node, context)) nodeSet.add(node);
        return nodeSet;
    } //-- fromDecendantsOrSelf
    
    /**
     * Selects all the following nodes of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the following nodes of the 
     * context node that match the given MatchExpr
     * <BR />
     * <B>Note</B> this could be an expensive operation
    **/
    public static NodeSet fromFollowing
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        
        XPathNode node = context.getNode();
        NodeSet nodeSet = context.newNodeSet();
        XPathNode child = node.getNext();
        while (child != null) {
            if (matchExpr.matches(child, context))
                nodeSet.add(child);
                
            if (child.hasChildNodes()) {
                NodeSet temp = fromDescendants(matchExpr, child, context);
                nodeSet.add(temp);
            }
            
            XPathNode tmpNode = child.getNext();
            if (tmpNode == null) {
                child = child.getParentNode();
                if ((child != null) && 
                    (child.getNodeType() != XPathNode.ROOT))
                    child = child.getNext();
            } else child = tmpNode;
        }
        return nodeSet;
    } //-- fromFollowing
    
    /**
     * Selects all the following siblings of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the XPathContext
     * @return a NodeSet containing all the following siblings of the 
     * context node that match the given MatchExpr
    **/
    public static NodeSet fromFollowingSiblings
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        XPathNode node = context.getNode();
        NodeSet nodeSet = context.newNodeSet();
        XPathNode child = node.getNext();
        while (child != null) {
            if (matchExpr.matches(child, context))
                nodeSet.add(child);
            child = child.getNext();
        }
        return nodeSet;
    } //-- fromFollowingSiblings
    
    /**
     * Selects the parent of the context node if it matches
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing the parent of the context node
     * if it matches the given MatchExpr
    **/
    public static NodeSet fromParent
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        XPathNode parent = context.getNode().getParentNode();            
        if (matchExpr.matches(parent, context))
            return context.newNodeSet(parent);
        else
            return context.newNodeSet();
    } //-- fromParent

    
    /**
     * Selects all the preceding siblings, in reverse document order,
     * of the context node that match the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the preceding siblings of the 
     * context node that match the given MatchExpr
    **/
    public static NodeSet fromPreceding
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        NodeSet nodeSet = context.newNodeSet();
        XPathNode node = context.getNode();
        XPathNode prev = node.getPrevious();
        if (prev == null) prev = node.getParentNode();
        while (prev != null) {
            if (matchExpr.matches(prev, context))
                nodeSet.add(prev);
            
            XPathNode temp = prev.getPrevious();
            if (temp == null) prev = prev.getParentNode();
            else prev = temp;
        }
        return nodeSet;
    } //-- fromPreceding
    
    /**
     * Selects all the preceding nodes, in reverse document order,
     * of the context node that match
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing all the preceding nodes of the 
     * context node (in reverse document order)
     * that match the given MatchExpr
    **/
    public static NodeSet fromPrecedingSiblings
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        
        NodeSet nodeSet = context.newNodeSet();
        XPathNode prev = context.getNode().getPrevious();
        while (prev != null) {
            if (matchExpr.matches(prev, context))
                nodeSet.add(prev);
            prev = prev.getPrevious();
        }
        return nodeSet;
    } //-- fromPrecedingSiblings
    
    
    /**
     * Selects the the context node if it matches
     * the given MatchExpr
     * @param the MatchExpr to evaluate
     * @param context the current XPathContext
     * @return a NodeSet containing the context node
     * if it matches the given MatchExpr
    **/
    public static NodeSet fromSelf
        (MatchExpression matchExpr, XPathContext context) 
        throws XPathException
    {
        XPathNode node = context.getNode();
        if (matchExpr.matches(node, context))
            return context.newNodeSet( node );
        else
            return context.newNodeSet();
    } //-- fromSelf
    
} //-- AxisIdentifier

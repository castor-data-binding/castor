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


package org.exolab.adaptx.xpath.engine;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.expressions.UnionExpr;

/**
 * This class represents a SelectExpr
 * <PRE>
 * [1] SelectExpr ::= UnionExpr
 * </PRE>
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class SelectExpr implements XPathExpression {
    
    /**
     * The UnionExpr for this SelectExpr
    **/
    private UnionExpr unionExpr = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new SelectExpr. This SelectExpr will select the 
     * empty node set by default.
    **/
    public SelectExpr() {
        super();
    } //-- SelectExpr


    public String toString() {
        if (this.unionExpr != null) return unionExpr.toString();
        else return "";
    } //-- toString
    
    
    public short getExprType() {
        return XPathExpression.UNION_EXPR;
    } //-- getExprType
    
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public XPathResult evaluate(XPathContext context)
        throws XPathException
    {
        return selectNodes(context);
    } //-- evaluate
    
    public UnionExpr getUnionExpr() {
        return unionExpr;
    } //-- setUnionExpr
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/
    
    /**
     * Selects all nodes matching this SelectExpr
     * @param context the XPathContext to use during evaluation.
     * @return the XPathResult
     * @exception XPathException when an error has occurred
     * evaluation
    **/
    protected NodeSet selectNodes(XPathContext context) 
        throws XPathException
    {
        if (unionExpr == null) return context.newNodeSet();
        NodeSet nodes = (NodeSet)unionExpr.evaluate(context);
        reorder(nodes, context);
        return nodes;
    } // -- selectNodes
    
    
    protected void setUnionExpr(UnionExpr unionExpr) {
        this.unionExpr = unionExpr;
    } //-- setUnionExpr

      //-------------------/
     //- Private Methods -/
    //-------------------/

    
    /**
     * Compares the document orders.
    **/
    private int compareOrders(int[] orderA, int[] orderB) {
        int idx = 0;
        while ((idx < orderA.length) && (idx < orderB.length)) {
            if (orderA[idx] < orderB[idx]) return -1;
            else if (orderB[idx] < orderA[idx]) return 1;
            ++idx;
        }
        if (orderA.length < orderB.length) return -1;
        else if (orderB.length < orderA.length) return 1;
        else return 0;
    } //-- compareOrders
    
    /**
     * Reorders the array of Nodes so that the nodes are in Document Order.
    **/
    private void reorder(NodeSet nodes, XPathContext context) {
        if (nodes.size() < 2) return;
        
        SortEntry[] sortArray = new SortEntry[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            SortEntry entry = new SortEntry();
            entry.node = nodes.item(i);
            entry.order = context.getDocumentOrder(entry.node);
            sortArray[i] = entry;
        }
        
        nodes.clear();
        int nc = 0;
        while (nc < sortArray.length) {            
            int next = 0;
            while (sortArray[next] == null) ++next;
            for (int i = next+1; i < sortArray.length; i++) {
                if (sortArray[i] == null) continue;
                if (compareOrders(sortArray[i].order, 
                                  sortArray[next].order) < 0) next = i;
            }
            nodes.add(sortArray[next].node, true);
            sortArray[next] = null;
            ++nc;
        }
        
    } //-- reorder
        
    class SortEntry {
        XPathNode node = null;
        int[] order = null;
    }
} //-- SelectExpr

/*
 * (C) Copyright Keith Visco 1999-2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * except in compliance with the license. Please see license.txt, 
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
package org.exolab.adaptx.xslt;

import org.exolab.adaptx.util.List;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import org.w3c.dom.*;

import org.exolab.adaptx.xpath.*;

/**
 * Represents a Basic XSL Selection Object. 
 * xsl:if, xsl:apply-templates, xsl:for-each
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Selection extends XSLObject {
        
    private static XPathExpression DEFAULT_EXPR = null;
    
    private static final XSLSort[]  EMPTY_SORT_ARRAY = new XSLSort[0];
        
    /** 
     * The selectExpr for this Selection
    **/ 
    private XPathExpression selectExpr = null;
    
    /**
     * Keeps track of the number of xsl:sort actions
    **/
    private int nbrSortKeys = 0;
    
    private static final XPathParser _parser = new XPathParser();
    
    //-- configure XPathParser
    static {
        _parser.setUseErrorExpr(true);
    }
    
    /**
     * Creates a new Selection of the give type.
     * @param type the type of Selection
     * <br />
     * <b>Note:</b> This will be changing soon.
    **/
    public Selection(short type) {
        super(type);
        
        if (DEFAULT_EXPR == null) {
            try {
                DEFAULT_EXPR =
                    _parser.createSelectExpression("node()");
            }
            catch(XPathException xpe) {};
        }
    } //-- Selection
    
    /**
     * @return the SelectExpr of this Selection
    **/
    public XPathExpression getSelectExpr() {
        return this.selectExpr;
    } //--getSelectExpr
    
    /**
     * Returns an Array of any XSLSort elements for this Selection.<BR>
     * Only xsl:apply-templates or xsl:for-each can have xsl:sort elements
    **/
    public XSLSort[] getSortKeys() {
        
        if (nbrSortKeys == 0) return EMPTY_SORT_ARRAY;
        
        //-- use a list in case we counted sort
        //-- elements (see #handleAction) 
        //-- which occured out of order
        List list = new List(nbrSortKeys);
        
        ActionTemplate tmpl = getActions();
        
        if (tmpl.size() > 0) {
            ActionIterator actions = tmpl.actions();
            while (actions.hasNext()) {
                XSLObject xslObject = actions.next();
                if (xslObject.getType() == XSLObject.SORT)
                    list.add(xslObject);
                else break;
            }
        }
        
        if (list.size() > 0)
            return (XSLSort[]) list.toArray(new XSLSort[list.size()]);
        else
            return EMPTY_SORT_ARRAY;
    } //-- getSortElements
    
    /**
     * Returns true if xsl:sort keys have been specified
     * @return true if xsl:sort keys have been specified
     * @see getSortKeys
    **/
    public boolean hasSortKeys() {
        return (nbrSortKeys > 0);
    } //-- hasSortKeys
    
    /**
     * Selects all nodes that match this Selection's SelectExpr 
     * using the given context node and ids.
     * @param context the Node context for evaluate the 
     * SelectExpr with
     * @param idRefs the ID reference table to resolve IDExpr(s)
    **/
    public NodeSet selectNodes(ProcessorState ps) 
        throws XPathException
    {
        if (selectExpr == null) {
            String selectAttr = getAttribute(Names.SELECT_ATTR);
            if ((selectAttr != null) && (selectAttr.length()>0))
                selectExpr = _parser.createSelectExpression(selectAttr);
            else selectExpr = DEFAULT_EXPR;
        }
        
        ps.pushCurrentNode(ps.getNode());
        NodeSet result = (NodeSet)selectExpr.evaluate(ps);
        ps.popCurrentNode();
        return result;
    } //-- selectNodes
    
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/
    
    /**
     * Allows subclasses to handle the append operation of the 
     * given XSLObject. Called from #appendAction
     * this method should return true, if there is nothing left to do,
     * otherwise false, if the super class should handle the append
    **/
    protected boolean handleAction(XSLObject xslObject) {
        
        short selectType = getType();
        
        
        switch (getType()) {
            
            case XSLObject.CHOOSE:
            
                switch (xslObject.getType()) {
                    case XSLObject.IF: //-- for my hack..sorry
                    case XSLObject.WHEN:
                    case XSLObject.OTHERWISE:
                        break;
                    default:
                        return true; //-- filter out unwanted elements
                }
                break;
            case XSLObject.APPLY_IMPORTS:
            case XSLObject.APPLY_TEMPLATES:
            case XSLObject.FOR_EACH:
                if (xslObject.getType() == XSLObject.SORT) {
                    ++nbrSortKeys;
                }
                break;
            default:
                break;
        }
        
        return false;
    } //--  handleAction
        
} //-- Selection

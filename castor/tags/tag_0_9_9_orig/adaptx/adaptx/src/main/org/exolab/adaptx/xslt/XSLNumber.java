/*
 * (C) Copyright Keith Visco 1999-2001  All rights reserved.
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

package org.exolab.adaptx.xslt;

import org.exolab.adaptx.xpath.functions.ErrorFunctionCall;
import org.exolab.adaptx.xpath.*;
import org.mitre.tjt.xsl.XslNumberFormat;

/**
 * Represents the xsl:number element. 
 * Handles numbering in the source tree<BR>
 * Section 2.7.10 of the W3C XSL Working Draft 1.0 (19981216)
 * <BR />
 * Section 9.7 of the W3C XSLT Working Draft 1.0 (1990421)
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XSLNumber extends XSLObject {
    
      //----------------------/
     //- Instance Variables -/
    //----------------------/
    
    /**
     * The expr for this XSLNumber
    **/
    private XPathExpression expr = null;
    
    /**
     * The count pattern of this XSLNumber
    **/
    private Pattern count = null;
    
    
    /**
     * The count pattern of this XSLNumber
    **/
    private Pattern  from = null;
    
    /**
     * int value representing the numbering level of this XSLNumber
    **/
    private String level = Names.SINGLE_VALUE;

    
      //---------------/
     //- Contructors -/
    //---------------/
    
    /**
     * Creates a new XSLNumber Object
    **/
    public XSLNumber() {
        super(XSLObject.NUMBER);
    } //-- XSLNumber
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Performs the numbering of the given XPathNode
     *
     * @param XPathNode the XPathNode to perform numbering on.
     * [This must be of type XPathNode.ELEMENT]
     *
     * @return an array of integers which represent the multi-level
     * number of the given element. Single-level numbering will
     * return an array of size 1.
    **/
    public int[] doNumbering(XPathNode node, ProcessorState ps) 
        throws XPathException
    {
        
        if (node.getNodeType() != XPathNode.ELEMENT) {
            String err = "XPathNode must be of type XPathNode.ELEMENT";
            throw new IllegalArgumentException(err);
        }
        
        int[] counts = null;
        
        //-- check for expr
        if (expr != null) {
            counts = new int[1];
            XPathContext xpc = ps.newContext(node);
            double dbl = expr.evaluate(xpc).numberValue();
            counts[0] = (int)dbl;
            return counts;
        }
        
        //-- check for null element
        if (node == null) return new int[0];
        
        Pattern local_count = this.count;
        
        if (local_count == null) {
            try {
                local_count = new Pattern(node.getLocalName());
            }
            catch(PatternException px) {
                //-- local name should be a valid pattern, so
                //-- this code should be pointless
                throw new XPathException(px);
            }
            
        }
        
        NodeSet nodes;
        
        if (Names.MULTIPLE_VALUE.equals(level)) {
            nodes = getAncestorsOrSelf(local_count, node, ps, false);
            counts = new int[nodes.size()];
            int cnum = 0;
            for (int i = nodes.size()-1; i >= 0; i--) {
                counts[cnum++] =
                    countPreceedingSiblings(local_count, nodes.item(i), ps);
            }
        }
        else if (Names.ANY_VALUE.equals(level)) {
            nodes = getAnyPreviousNodes(local_count, node, ps);
            counts = new int[1];
            counts[0] = nodes.size();
        }
        else {
            nodes = getAncestorsOrSelf(local_count, node, ps, true);
            counts = new int[nodes.size()];
            if (nodes.size() > 0) {
                counts[0] =
                    countPreceedingSiblings(local_count, nodes.item(0), ps);
            }
        }
        
        return counts;
    } //-- doNumbering
    
    /**
     * Performs the numbering of the given XPathNode and 
     * returns the number using the format of this XSLNumber.
     * 
     * @param node the XPathNode to get the number of. This node
     * must be of type XPathNode.ELEMENT.
     * @return the formatted number as a String
    **/
    public String getFormattedNumber(XPathNode node, ProcessorState ps) {
        
        try {
            int[] counts = doNumbering(node, ps);        
            // Uses Tim Taylor's XslNumberFormat to format
            // the result
            String format = getAttribute(Names.FORMAT_ATTR);
            if (format == null) format = "1";
            return XslNumberFormat.format(counts, format);
        }
        catch(XPathException xpe) {};
        return "";
        
    } //-- getFormattedNumber
    
    /**
     * @see XSLObject
    **/
    public void setAttribute(String name, String value) 
        throws XSLException 
    {
        
        ErrorFunctionCall efc = null;
        
        if (Names.VALUE_ATTR.equals(name)) {
            if (value == null) expr = null;
            else {
                try {
                    expr = createExpression(value);
                }
                catch (XPathException xpe) {
                    efc = new ErrorFunctionCall();
                    efc.setError("invalid 'value' attribute of xsl:number"+
                        xpe.getMessage());
                    expr = efc;
                }
            }
        }
        //-- set count
        else if (Names.COUNT_ATTR.equals(name)) {
            if (value == null) count = null; //-- use default
            else {
                try {
                    count = new Pattern(value);
                }
                catch(PatternException pe) {
                    efc = new ErrorFunctionCall();
                    efc.setError("invalid 'count' attribute of xsl:number"+
                        pe.getMessage());
                    expr = efc;
                }
            }
        }
        //-- set from
        else if (Names.FROM_ATTR.equals(name)) {
            if (value == null) from = null; //-- use default
            else {
                try {
                    from = new Pattern(value);
                }
                catch(PatternException pe) {
                    efc = new ErrorFunctionCall();
                    efc.setError("invalid 'from' attribute of xsl:number"+
                        pe.getMessage());
                    expr = efc;
                }
            }
        }
        //-- set level
        else if (Names.LEVEL_ATTR.equals(name)) {
            level = value;
        } //--
        
        super.setAttribute(name,value);
    } //-- setAttribute
    
    /**
     * Sets the count expression of this XSLNumber
     * @param count the String value to use as the count expr
     * expression of this XSLNumber
    **/
    public void setCountAttr(String count) {
        try {
            setAttribute(Names.COUNT_ATTR, count);
        }
        catch(XSLException xslException) {};
    } //-- setCountAttr
    
    /**
     * Sets the format pattern of this XSLNumber
     * @param format the Number Format to use
    **/
    public void setFormatAttr(String format) {
        try {
            setAttribute(Names.FORMAT_ATTR, format);
        }
        catch(XSLException xslException) {};
    } //-- setFormatAttr
    
    public void setFromAttr(String from) {
        try {
            setAttribute(Names.FROM_ATTR, from);
        }
        catch(XSLException xslException) {};
    } //-- setFrom
    
    /**
     * Sets the level of numbering for this XSLNumber
     * @param level the desired level.
     * <PRE>
     *  Levels are "single", "multi", or "any"
     * </PRE>
    **/
    public void setLevel(String level) {
        try {
            setAttribute(Names.LEVEL_ATTR, level);
        }
        catch(XSLException xslException) {};
    } //-- setLevel
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/
    
    /**
     * Counts the number of preceeding element siblings of the given
     * node that match the given pattern
     *
     * @param pattern the Pattern to match against
     * @param node the starting XPathNode
     * @param context the current XPathContext
    **/
    private static int countPreceedingSiblings
        (Pattern pattern, XPathNode node, XPathContext context)
        throws XPathException
    {
        if (node == null) return 0;
        
        int count = 1;
        XPathNode sibling = node.getPrevious();
        while ( sibling != null ) {
            if (sibling.getNodeType() == XPathNode.ELEMENT) {
                if (pattern.matches(sibling, context)) ++count;
            }
            sibling = sibling.getPrevious();
        }
        return count;
    } //-- countPreceedingSiblings
    
    /**
     * Returns all ancestor nodes that match the given Pattern, including
     * the context node if it also matches.
    **/
    private NodeSet getAncestorsOrSelf
       ( Pattern pattern, 
         XPathNode node, 
         XPathContext context, 
         boolean findNearest )
        throws XPathException
    {
        NodeSet nodeSet = new NodeSet();
        XPathNode current = node;
        while ((current!= null) && (current.getNodeType() == XPathNode.ELEMENT))
        {
            
            if ((from != null) && from.matches(current, context)) break;
                
            if (pattern.matches(current, context)) {
                nodeSet.add(current);
                if (findNearest) break;
            }
                
            current = current.getParentNode();
        }
        return nodeSet;
        
    } //-- fromAncestorsOrSelf
    
    /**
     * Retrieves all nodes that come before the given element
     * at any level in the document that match the count pattern of 
     * this XSLNumber starting from the closest element that matches 
     * the from pattern
     *
     * @param element the element to find the nearest ancestor of
     * @return a NodeSet of all matching nodes
    **/
    private NodeSet getAnyPreviousNodes
        (Pattern pattern, XPathNode node, XPathContext context)
            throws XPathException
    {
        
        NodeSet nodes = new NodeSet();
        if (node.getNodeType() != XPathNode.ELEMENT) return nodes;
        
        XPathNode element = node;
        while (element != null) {
            
            //-- Check "from" Pattern 
            if ((from != null) && from.matches(element,context)) 
                return nodes;
                
            //-- Check "count" Pattern
            if (pattern.matches(element, context)) 
                nodes.add(element);
            
            XPathNode sibling = element;
            while ( (sibling = sibling.getPrevious()) != null) {
                if (sibling.getNodeType() == XPathNode.ELEMENT) break;
            }
            if (sibling == null) {
                XPathNode parent = element.getParentNode();
                if (parent.getNodeType() != XPathNode.ELEMENT) break;
                element = parent;
            }
            else element = sibling;
        }
        return nodes;
    } //-- getAnyPreviousNodes
        
} //-- XSLNumber

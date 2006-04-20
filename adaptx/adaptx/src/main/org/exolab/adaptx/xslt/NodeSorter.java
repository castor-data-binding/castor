/*
 * (C) Copyright Keith Visco 1998-2003  All rights reserved.
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
package org.exolab.adaptx.xslt;

import java.text.Collator;
import java.util.Locale;
import java.util.Enumeration;
import java.util.Hashtable;

import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathParser;
import org.exolab.adaptx.util.List;
import org.exolab.adaptx.xml.XMLUtil;


/**
 * Sorts nodes using the given XSLSort elements
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco (keith@kvisco.com)</a>
 */
class NodeSorter {
    
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_LANG = "en";
    
    
    /**
     * Sorts the given Node Array using this XSLSort's properties as a
     * basis for the sort
     * @param nodes the Array of nodes to sort
     * @param pState the ProcessorState to evaluate the Select pattern with
     * @return the a new Array of sorted nodes
    **/
    public static NodeSet sort
        (NodeSet nodes, XSLSort[] sortElements, ProcessorState pState) 
        throws XSLException
    {
        
        if ((nodes.size() < 2) || (sortElements.length == 0)) 
            return nodes;
        
        // Build sortKeys table
        Hashtable keyHash = new Hashtable(nodes.size());
        
        XSLSort         xslSort;
        XPathExpression selectExpr = null;
        XPathNode       node;
        NodeSet         resultNodes;
        String          sortKey;
        NodeSet         bucket;
        
        xslSort = sortElements[0];
        AttributeValueTemplate avt;
        
        String lang = DEFAULT_LANG;
        String order = XSLSort.ASCENDING_ORDER;
        String dataType = XSLSort.TEXT_TYPE;
        
        try {
            String attValue = null;
            // get lang
            attValue = xslSort.getAttribute(XSLSort.LANG_ATTR);
            avt = pState.getAttributeValueTemplate(attValue);
            if (avt != null) lang = avt.evaluate(pState);
            
            // Get Order
            attValue = xslSort.getAttribute(XSLSort.ORDER_ATTR);
            avt = pState.getAttributeValueTemplate(attValue);
            if (avt != null) order = avt.evaluate(pState);
            
            // Get DataType
            attValue = xslSort.getAttribute(XSLSort.DATA_TYPE_ATTR);
            avt = pState.getAttributeValueTemplate(attValue);
            if (avt != null) dataType = avt.evaluate(pState);
        }
        catch(XPathException xpe) {
            throw new XSLException(xpe);
        }
        
        boolean ascending = order.equals(XSLSort.ASCENDING_ORDER);
        
        
        NodeSet noKeyBucket = new NodeSet();
        // Build Hashtable
        
        XPathContext xpc = pState.newContext(nodes, 0);
        for (int i = 0; i < nodes.size(); i++) {
            node = nodes.item(i);
            xpc.setPosition(i);
            try {
                selectExpr = xslSort.getSelectExpr();
                resultNodes = (NodeSet)selectExpr.evaluate(xpc);
                sortKey = resultNodes.stringValue();
                resultNodes = null;
                if (sortKey.length() == 0) {
                    noKeyBucket.add(node, true);
                }
                else {
                    bucket = (NodeSet) keyHash.get(sortKey);
                    if (bucket == null) {
                        bucket = new NodeSet(node);
                        keyHash.put(sortKey,bucket);
                    }
                    else {
                        bucket.add(node, true); 
                    }
                }
                
            }
            catch(XPathException xpe) {
                throw new XSLException(xpe);
            }
        }
        
        
        // Create new sortElements array to handle bucket sorting
        XSLSort[] newSortElements = new XSLSort[sortElements.length-1];
        if (sortElements.length > 1) {
            for (int i = 1; i < sortElements.length; i++)
                newSortElements[i-1] = sortElements[i];
        }
        
        // Return sorted Nodes
        String[] keys = getSortedKeys(keyHash, ascending, dataType, lang);
        NodeSet sorted = new NodeSet(nodes.size());
        
        // add all nodes that had no Key (using document order)
        sorted.add(noKeyBucket, true);
        
        // Add All nodes with sorted keys
        for (int i = 0; i < keys.length; i++) {
            bucket = (NodeSet) keyHash.get(keys[i]);
            // Sort Buckets if necessary
            if (bucket.size() > 1) {
                bucket = sort(bucket,newSortElements,pState);
            }
            sorted.add(bucket, true);
        }
        return sorted;
    } //-- sort


    
    /**
     * Returns a sorted enumeration of the keys of the given Hashtable.
    **/
    private static String[] getSortedKeys
        (Hashtable ht, boolean ascending, String dataType, String lang) 
    {
        if (ht == null) return (new String[0]);
        
        String[] strings = new String[ht.size()];
        Enumeration keys = ht.keys();
        int c = 0;
        while (keys.hasMoreElements())
            strings[c++] = (String) keys.nextElement();
        
        // I should combine sortAsNumber and sortAsText into
        // one method since they are virtually the same and
        // create a Number Collator
        
        if (dataType.intern() == XSLSort.NUMBER_TYPE)
            return sortAsNumbers(strings, ascending, lang);
        else
            return sortAsText(strings, ascending, lang);
    } //-- getSortedKeys
    
    /**
     * Sorts the given String Array by converting the Strings to Numbers
     * and performing a comparison
     * @return the new sorted String Array
    **/
    private static String[] sortAsNumbers
        (String[] strings, boolean ascending, String lang) 
    {
        if (strings.length == 0) return new String[0];
        
        List sorted = new List(strings.length);
        sorted.add(strings[0]);
        String key;
        int comp = -1;
        // Uses a simple insertion sort
        for (int i = 1; i < strings.length; i++) {
            key = strings[i];
            for (int j = 0; j < sorted.size(); j++) {
                comp = compareAsNumbers(key, (String)sorted.get(j));
                
                // Ascending
                if (ascending) {
                    if (comp < 0) {
                        sorted.add(j, key);
                        break;
                    }
                    else if (j == sorted.size()-1) {
                        sorted.add(key);
                        break;
                    }
                }
                // Descending
                else {
                    if (comp > 0) {
                        sorted.add(j, key);
                        break;
                    }
                    else if (j == sorted.size()-1) {
                        sorted.add(key);
                        break;
                    }
                }
            } //-- end for each sorted key
        }
        return (String[]) sorted.toArray(new String[sorted.size()]);
        
    } //-- sortAsNumbers
    
    /**
     * Sorts the given String Array.
     * @return the new sorted String Array
    **/
    private static String[] sortAsText
        (String[] strings, boolean ascending, String lang) 
    {
        Collator collator = Collator.getInstance(XMLUtil.getLocale(lang));
        int comp = -1;
        String key;
        
        if (strings.length == 0) return new String[0];
        
        List sorted = new List(strings.length);
        sorted.add(strings[0]);
        // Uses a simple insertion sort
        for (int i = 1; i < strings.length; i++) {
            key = strings[i];
            for (int j = 0; j < sorted.size(); j++) {
                comp = collator.compare(key, (String)sorted.get(j));
                
                // Ascending
                if (ascending) {
                    if (comp < 0) {
                        sorted.add(j, key);
                        break;
                    }
                    else if (j == sorted.size()-1) {
                        sorted.add(key);
                        break;
                    }
                }
                // Descending
                else {
                    if (comp > 0) {
                        sorted.add(j, key);
                        break;
                    }
                    else if (j == sorted.size()-1) {
                        sorted.add(key);
                        break;
                    }
                }
            } //-- end for each sorted key
        }
        return (String[]) sorted.toArray(new String[sorted.size()]);
        
    } //-- sortAsText
    
    
    private static int compareAsNumbers(String strA, String strB) {
        double dblA, dblB;
        
        try { dblA = (Double.valueOf(strA)).doubleValue(); }
        catch(NumberFormatException nfe) { dblA = 0; }
        try { dblB = (Double.valueOf(strB)).doubleValue(); }
        catch(NumberFormatException nfe) { dblB = 0; }
        
        if (dblA < dblB) return -1;
        else if (dblA == dblB) return 0;
        else return 1;
    } //-- compareAsNumbers


} //-- NodeSorter

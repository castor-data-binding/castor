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
package org.exolab.adaptx.xml;

import org.exolab.adaptx.util.Iterator;
import org.exolab.adaptx.util.List;
import org.exolab.adaptx.util.HashMap;

//import org.exolab.adaptx.util.QuickStack;

import org.exolab.adaptx.xpath.XPathNode;

/**
 * A utility class which helps overcome some DOM 1.0 deficiencies.
 * @author <a href="kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class IDIndexer {

    
    private static final String WILD_CARD = "*";
    
    /**
     * A pointer to the next node to check, 
     * when indexing attributes
    **/
    private List  indexingInfo            = null;
    
    /**
     * A list of Attribute names which should be treated as IDs
    **/
    private HashMap idAttributes          = null;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new DOMHelper
    **/
    public IDIndexer() {
        super();
        indexingInfo      = new List(5);
        idAttributes      = new HashMap(13);
        idAttributes.put("id", "*");
        
    } //-- DOMHelper

      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Adds the given attribute name as an ID attribute.
     * @param attrName the name of the attribute to treat as an Id.
     * @param appliesTo the element that this ID attribute appliesTo,
     * "*" may be used to indicate all Elements.
    **/
    public void addIdAttribute(String attrName, String appliesTo) {
        if ((attrName == null) || (appliesTo == null)) return;
        
        if (!idAttributes.containsKey(attrName))
            idAttributes.put(attrName, appliesTo);
            
    } //-- addIdAttribute
    
    /**
     * Associates the given Id with the given Element
     * @param id the Id to associate with the given Element
     * @param element the element which the Id maps to
    **/
    public void addIdReference(String id, XPathNode node) {
        if ((node == null) || (id == null)) return;
        
        if (node.getNodeType() != XPathNode.ELEMENT) {
            String err = "the argument node must be an element node.";
            throw new IllegalArgumentException(err);
        }
        
        XPathNode root = node.getRootNode();
        IndexState idxState = getIndexState(root);
        idxState.idReferences.put(id, node);
    } //-- addIdReference
    
    /**
     * Determines the document order of a given node.
     * Document Order is defined by
     * the document order of the parent of the given node and 
     * the childNumber of the given Node.
     * The document order for a Document node is 0.
     * @see org.exolab.adaptx.xpath.ExprContext
    **
    public int[] getDocumentOrder(Node node) {
        
        int[] order = null;
        
        if (node == null) {
            order = new int[1];
            order[0] = -1;
            return order;
        }
        
        //-- check cache
        //-- * due to bugs in XML4J 1.1.x (2.x works fine) 
        //-- * we need to use the System.identityHash to
        //-- * create a unique key. The problem is Attr nodes
        //-- * with the same name, generate the same hash code.
        Object key = createKey(node);
        
        order = (int[]) documentOrders.get(key);
        if (order != null) return order;
        
        
        Node parent = null;
        //-- calculate document order
        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            // Use parent's document order for attributes
            parent = getParentNode((Attr)node);
            if (parent == null) {
                // int[3]  {0 = document, 0 = att-list, att-number}
                order = new int[3];
                order[0] = 0;
                order[1] = 0;
                order[2] = childNumber(node);
            }
            else { 
                int[] porder = getDocumentOrder(parent);
                order = new int[porder.length+2];
                for (int i = 0; i < porder.length; i++) 
                    order[i] = porder[i];
                order[order.length-2] = 0;  //-- signal att-list
                order[order.length-1] = childNumber(node);
            }
        }
        else if (node.getNodeType() == Node.DOCUMENT_NODE) {
            order = new int[1];
            order[0] = 0;
        }
        else {
            
            //-- get parent's document order
            parent = getParentNode(node);
            int[] porder = getDocumentOrder(getParentNode(node));
            order = new int[porder.length+1];
            for (int i = 0; i < porder.length; i++) 
                order[i] = porder[i];
            order[order.length-1] = childNumber(node);
        }
        
        //-- add to cache
        documentOrders.put(key,order);
        return order;
    } //-- getDocumentOrder
    
    /**
     * Returns the element XPathNode that is associated with the given Id.
     * @param root the "root" XPathNode to search within
     * @param id the Id of the element to return
     * @return the element XPathNode that is associated with the given Id,
     * or null if no XPathNode was found
    **/
    public XPathNode getElementById(XPathNode root, String id) {
        
        if (root == null) return null;
        
        if (root.getNodeType() != XPathNode.ROOT) {
            root = root.getRootNode();
        }
        IndexState idxState = getIndexState(root);
        XPathNode node = (XPathNode)idxState.idReferences.get(id);
        if ((node != null) || (idxState.done)) return node;
        continueIndexing(root, null);
        return (XPathNode)idxState.idReferences.get(id);
        
    } //-- getElementById
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Returns the child number of a Node
     * @param node the Node to retrieve the child number of
     * @return the child number of the given node
    **/
    private int childNumber(XPathNode node) {
        int c = 1;
        XPathNode tmpNode = node;
        while ((tmpNode = tmpNode.getPrevious())!=  null) ++c;
        return c;
    } //-- childNumber
    
    /**
     * Continues the Indexing the given Document until the given target node 
     * has been Indexed. The target node is only "forward checked", which means 
     * if the node has already been indexed, indexing will continue until the
     * end of the document.
     * @param document the Document to continue indexing
     * @param target the Node to stop indexing at
    **/
    private void continueIndexing(XPathNode root, XPathNode target) {
        
        if (root == null) {
            if (target == null) return;
            root = target.getRootNode();
            if (root == null) return;
        }
        
        IndexState idxState = getIndexState(root);
        if (idxState.done) return;        
        
        boolean found = false;
        boolean alreadyProcessed  = false;
        
        while (!found) {
            
            if (idxState.next == null) {
                idxState.done = true;
                break;
            }
            if (!alreadyProcessed) {
                
                if (idxState.next.getNodeType() == XPathNode.ELEMENT) {
                    XPathNode element = idxState.next;
                    
                    //-- index attributes
                    
                    XPathNode attr = element.getFirstAttribute();
                    
                    String elementName = element.getLocalName();
                                    
                    while (attr != null) {
                        if (attr == target) found = true;
                        //-- index Ids
                        String attrName = attr.getLocalName();
                        if (idAttributes.containsKey(attrName)) {
                            String matchName = (String)idAttributes.get(attrName);
                            if (WILD_CARD.equals(matchName) ||
                                elementName.equals(matchName)) {
                                String id = attr.getStringValue();
                                if (idxState.idReferences.get(id) == null) {
                                    idxState.idReferences.put(id,element);
                                }
                            }
                        }
                        attr = attr.getNext();
                    } //-- end attributes loop
                }
            } //-- end target search
            
            //-- set next node to check
            if ((!alreadyProcessed) && (idxState.next.hasChildNodes())) {
                XPathNode child = idxState.next.getFirstChild();
                idxState.next = child;
            }
            else if (idxState.next.getNext() != null) {
                idxState.next = idxState.next.getNext();
                alreadyProcessed = false;
            }
            else {
                idxState.next = idxState.next.getParentNode();
                //-- already checked parent, now check sibling
                alreadyProcessed = true;
            }
        }
        
    } //-- continueIndexing
    
    private IndexState getIndexState(XPathNode root) {
        
        IndexState idxState = null;
        
        for (int i = 0; i < indexingInfo.size(); i++) {
            idxState = (IndexState)indexingInfo.get(i);
            if (idxState.root == root) return idxState;
        }
        
        idxState = new IndexState(root);
        indexingInfo.add(idxState);
        return idxState;
    } //-- getIndexState
    
    
    class IndexState {
        
        XPathNode root        = null;
        XPathNode next        = null;
        boolean done          = false;
        HashMap idReferences  = null;
        
        protected IndexState(XPathNode root) {
            super();
            this.root = root;
            if (root != null) {
                next = getRootElement();
            }
            if (next == null) done = true;
            if (done) idReferences = new HashMap(1);
            else idReferences = new HashMap();
        }   
        
        private XPathNode getRootElement() {
            XPathNode node = root.getFirstChild();            
            while (node != null) {
                if (node.getNodeType() == XPathNode.ELEMENT)
                    return node;
                node = node.getNext();
            }
            return null;
        } //-- getRootElement

    } //-- IndexState
        
} //-- IDIndexer

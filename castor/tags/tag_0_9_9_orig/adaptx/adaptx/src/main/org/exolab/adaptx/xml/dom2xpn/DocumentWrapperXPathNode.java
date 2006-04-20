/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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


package org.exolab.adaptx.xml.dom2xpn;

import org.exolab.adaptx.xpath.XPathNode;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * An XPathNode wrapper for DOM Element nodes, used by the 
 * implementation of XPathNode for the W3C DOM API
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DocumentWrapperXPathNode extends ParentDOMXPathNode {

    /**
     * A reference to the document node we are wrapping
    **/
    private Document _document      = null;

    private ElementWrapperXPathNode _rootElement  = null;
    
    /**
     * Creates a new DocumentWrapperXPathNode
     *
     * @param document the Document that this XPathNode wraps.
     * This must not be null.
    **/
    public DocumentWrapperXPathNode(Document document) {
        super();
        if (document == null)
            throw new IllegalArgumentException("Document must not be null.");
            
        _document = document;
        initialize();
    } //-- DocumentWrapperXPathNode
    
    
    /**
     * Intializes the children of this Document Wrapper
    **/
    private void initialize() {
        
        Node child = _document.getFirstChild();
        
        TextWrapperXPathNode text = null;
        
        while (child != null) {
            switch (child.getNodeType()) {
                case Node.COMMENT_NODE:
                    addChild(new CommentWrapperXPathNode((Comment)child));
                    text = null;
                    break;
                case Node.PROCESSING_INSTRUCTION_NODE:
                    addChild(new PIWrapperXPathNode((ProcessingInstruction)child));
                    text = null;
                    break;
                case Node.ELEMENT_NODE:
                    _rootElement = new ElementWrapperXPathNode((Element)child);
                    addChild(_rootElement);
                    text = null;
                    break;
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    if (text != null) {
                        text.addText((CharacterData)child);
                    }
                    else {
                        text = new TextWrapperXPathNode((CharacterData)child);
                        addChild(text);
                    }
                    break;
                default:
                    break;
            }
            child = child.getNextSibling();
        }
    } //-- initialize
    
    /**
     * Returns the type of this node.
     *
     * @return The type of this node
    **/
    public int getNodeType() {
        return XPathNode.ROOT;
    } //-- getNodeType

    /**
     * Returns the local name of the node. Returns the local
     * name of an element or attribute, the prefix of a namespace
     * node, the target of a processing instruction, or null for
     * all other node types.
     *
     * @return The local name of the node, or null if the node has
     * no name
    **/
    public String getLocalName() {
        return null;
    } //-- getLocalName


    /**
     * Returns the namespace URI the node. Returns the namespace URI
     * of an element, attribute or namespace node,  or null for
     * all other node types.
     *
     * @return The namespace URI of the node, or null if the node has
     * no namespace URI
    **/
    public String getNamespaceURI() {
        return null;
    } //-- getNamespaceURI

    /**
     * Returns the root node.
     *
     * @return The root node
    **/
    public XPathNode getRootNode() {
        return this;
    } //-- getRootNode

    /**
     * Returns the string value of the node. The string value of a text
     * node or an attribute node is it's text value. The string value of
     * an element or a root node is the concatenation of the string value
     * of all its child nodes. The string value of a namespace node is its
     * namespace URI. The string value of a processing instruction is the
     * instruction, and the string value of a comment is the comment text.
     *
     * @return The string value of the node
    **/
    public String getStringValue() {
        if (_rootElement == null)
            return "";
        return _rootElement.getStringValue();
    } //-- getStringValue


    /**
     * Returns the namespace URI associated with this namespace prefix,
     * as defined in the context of this node. Returns null if the prefix
     * is undefined. Returns empty if the prefix is defined and associated
     * with no namespace. This method is valid only for element nodes.
     *
     * @param prefix The namespace prefix
     * @return The namespace URI, or null
    **/
    public String getNamespaceURI( String prefix ) {
        return null;
    } //-- getNamespaceURI


    /**
     * Returns the namespace prefix associated with this namespace URI,
     * as defined in the context of this node. Returns null if no prefix
     * is defined for this namespace URI. Returns an empty string if the
     * default prefix is associated with this namespace URI. This method
     * is valid only for element nodes.
     *
     * @param uri The namespace URI
     * @return The namespace prefix, or null
    **/
    public String getNamespacePrefix( String uri ) {
        return null;
    } //-- getNamespacePrefix

    /**
     * Sets the "next" XPathNode sibling
     *
     * @param node the XPathNode which is the "next" sibling of this
     * XPathNode
    **/    
    void setNext(BaseDOMXPathNode node) {
        throw new IllegalArgumentException("A root node cannot have siblings");
    } //-- setNext

    /**
     * Sets the parent XPathNode
     *
     * @param node the XPathNode which is the parent of this
     * XPathNode
    **/    
    void setParent(BaseDOMXPathNode node) {
        throw new IllegalArgumentException("A root node cannot have a parent");
    } //-- setParent

    /**
     * Sets the "previous" XPathNode sibling
     *
     * @param node the XPathNode which is the "previous" sibling of this
     * XPathNode
    **/    
    void setPrevious(BaseDOMXPathNode node) {
        throw new IllegalArgumentException("A root node cannot have siblings");
    } //-- setPrevious
    
} //-- DocumentWrapperXPathNode

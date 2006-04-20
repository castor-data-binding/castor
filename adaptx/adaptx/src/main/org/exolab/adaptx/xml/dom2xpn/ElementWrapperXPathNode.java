/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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


package org.exolab.adaptx.xml.dom2xpn;

import org.exolab.adaptx.xpath.XPathNode;

import org.w3c.dom.*;

/**
 * An XPathNode wrapper for DOM Element nodes, used by the 
 * implementation of XPathNode for the W3C DOM API
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class ElementWrapperXPathNode extends ParentDOMXPathNode {

    /**
     * A reference to the first namespace node
    **/
    private AttributeWrapperXPathNode _firstAttribute = null;
    
    /**
     * A reference to the first namespace node, we only
     * hold a reference to the first namespace since
     * there should only be a few namespace declarations
     * adding new namespaces can simply traverse to the
     * end if necessary.
    **/
    private NamespaceXPathNode _firstNamespace = null;
    
    /** 
     * The namespace to which this XPathNode belongs
    **/
    private String  _nsURI = null;
    
    /**
     * The calculated local name
    **/
    private String  _localName = null;
    
    /**
     * the namespace prefix
    **/
    private String _nsPrefix = null;
    

    private Element _element      = null;

    /**
     * A flag for lazy initialization of attributes
    **/
    boolean _attsInitialized = false;
    
    /**
     * A flag for lazy initialization of child nodes
    **/
    boolean _childrenInitialized = false;
    
    
    /**
     * Creates a new ElementWrapperXPathNode
     *
     * @param element the Element that this XPathNode wraps
     * @param parent the parent XPathNode, this value cannot be null.
    **/
    ElementWrapperXPathNode(Element element) {
        super();
        _element = element;
        
        //-- process namespace nodes first, 
        //-- we may need these to calculate the namespace 
        //-- of this node, and attributes
        NamedNodeMap atts = element.getAttributes();
        if (atts != null) {
            for (int i = 0; i < atts.getLength(); i++) {
                Node attr = atts.item(i);
                String attName = attr.getNodeName();
                String prefix = null;
                if (attName.equals("xmlns")) {
                    prefix = "";
                }
                else if (attName.startsWith("xmlns:")) {
                    prefix = attName.substring(6);
                }
                
                //-- if we have a namespace node
                if (prefix != null) {
                    addNamespace(prefix, attr.getNodeValue());
                }
            } //-- end processing NamedNodeMap
        }
        
        _localName = _element.getNodeName();
        int idx = _localName.indexOf(':');
        if (idx >= 0) {
            _nsPrefix  = _localName.substring(0,idx);
            _localName = _localName.substring(idx+1);
        }
        else {
            _nsPrefix = "";
        }
        _nsURI     = getNamespaceURI(_nsPrefix);        
    } //-- ElementWrapperXPathNode
    
    /**
     * Intializes the attributes of this ElementWrapper
    **/
    private void initAttributes() {
        
        _attsInitialized = true;
        
        //-- process namespace nodes first, 
        //-- we may need these to calculate the namespace 
        //-- of this node, and attributes
        NamedNodeMap atts = _element.getAttributes();
        if (atts == null) {
            // nothing to do
            return;
        }
        
        AttributeWrapperXPathNode lastAttr = null;
        
        for (int i = 0; i < atts.getLength(); i++) {
            Node attr = atts.item(i);
            String attName = attr.getNodeName();
            if (attName.equals("xmlns") || (attName.startsWith("xmlns:"))) {
                //-- ignore namespace node
                continue;
            }
            
            AttributeWrapperXPathNode attribute 
                = new AttributeWrapperXPathNode((Attr)attr, this);
                
            if (_firstAttribute == null) {
                lastAttr = attribute;
                _firstAttribute = lastAttr;
            }
            else {
                attribute.setPrevious(lastAttr);
                lastAttr.setNext(attribute);
                lastAttr = attribute;
            }
        }
    } //-- initialize attributes
    
    /**
     * Intializes the children of this Element Wrapper
    **/
    private void initChildren() {
        
        _childrenInitialized = true;
        
        Node child = _element.getFirstChild();
        
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
                    addChild(new ElementWrapperXPathNode((Element)child));
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
    } //-- initializeChildren
    
    /**
     * Returns the first child node of this node, or null if
     * the node has no children. Child nodes are only returned
     * for an element or root node.
     *
     * @return The first child node of this node, or null
    **/
    public XPathNode getFirstChild() {
        if (!_childrenInitialized) initChildren();
        return _firstChild;
    } //-- getFirstChild


    /**
     * Returns true if this node has any child nodes.
     *
     * @return True if this node has any child nodes.
    **/
    public boolean hasChildNodes() {
        if (!_childrenInitialized) initChildren();
        return  (_firstChild != null);
    } //-- hasChildNodes

    /**
     * Returns the type of this node.
     *
     * @return The type of this node
    **/
    public int getNodeType() {
        return XPathNode.ELEMENT;
    } //-- getNodeType


    /**
     * Returns the first in a list of attribute nodes, or null
     * if the node has no attributes. This method is valid only
     * for the element node.
     *
     * @return The first in a list of attribute nodes, or null
    **/
    public XPathNode getFirstAttribute() {
        if (!_attsInitialized) initAttributes();
        return _firstAttribute;
    } //-- getFirstAttribute


    /**
     * Returns the value of the named attribute, or null if the
     * node has no such attribute. If the argument <tt>uri</tt>
     * is null, the node's namespace URI will be used. This method
     * is valid only for the element node.
     *
     * @param uri The attribute's namespace URI, or null
     * @param local The attribute's local name
     * @return The attribute's value, or null if no such attribute exists
    **/
    public String getAttribute( String uri, String local ) {
        if (!_attsInitialized) initAttributes();
        
        AttributeWrapperXPathNode attrWrapper = _firstAttribute;
        
        while (attrWrapper != null) {
            
            if (attrWrapper.getLocalName().equals(local)) {
                String ns = attrWrapper.getNamespaceURI();
                if ((ns == null) && (uri == null)) {
                    return attrWrapper.getStringValue();
                }
                else if ((ns != null) && (ns.equals(uri))) {
                    return attrWrapper.getStringValue();
                }
            }
            attrWrapper = (AttributeWrapperXPathNode)attrWrapper.getNext();
        }
        
        return null;
    } //-- getAttribute


    /**
     * Returns the first in a list of namespace nodes, or null
     * if the node has no namespaces. This method is valid only
     * for the element node.
     *
     * @return The first in a list of namespace nodes, or null
    **/
    public XPathNode getFirstNamespace() {
        return _firstNamespace;
    } //-- getFirstNamespace


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
        return _localName;
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
        return _nsURI;
    } //-- getNamespaceURI

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
        if (!_childrenInitialized) initChildren();
        
        if (_firstChild == null)
            return "";
            
        if (_firstChild.getNext() == null)
            return _firstChild.getStringValue();
            
        StringBuffer sb = new StringBuffer(_firstChild.getStringValue());
        XPathNode node = _firstChild.getNext();
        while (node != null) {
            sb.append(node.getStringValue());
            node = node.getNext();
        }
        return sb.toString();
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
        
        NamespaceXPathNode nsNode = _firstNamespace;
        while (nsNode != null) {
            if (nsNode.getLocalName().equals(prefix))
                return nsNode.getStringValue();
            nsNode = (NamespaceXPathNode)nsNode.getNext();
        }
        
        //-- if we reach here we don't have a local declaration
        //-- check parent if it exists...otherwise use element
        //-- to calculate the namespace
        XPathNode parent = getParentNode();
        if (parent != null) {
            return parent.getNamespaceURI(prefix);
        }
        else {
            String attName = null;
            
            if ((prefix == null) || (prefix.length() == 0))
                attName = "xmlns";
            else 
                attName = "xmlns:" + prefix;

            Element elem = _element;
            while (elem != null) {
                NamedNodeMap atts = elem.getAttributes();
                Node attr = atts.getNamedItem(attName);
                if (attr != null) return attr.getNodeValue();
                Node node = elem.getParentNode();
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    break;
                elem = (Element) node;
            }
        }
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
        
        NamespaceXPathNode nsNode = _firstNamespace;
        while (nsNode != null) {
            if (nsNode.getStringValue().equals(uri))
                return nsNode.getLocalName();
            nsNode = (NamespaceXPathNode)nsNode.getNext();
        }
        
        //-- if we reach here we don't have a local declaration
        //-- check parent if it exists...otherwise use element
        //-- to calculate the namespace prefix
        XPathNode parent = getParentNode();
        if (parent != null) {
            return parent.getNamespacePrefix(uri);
        }
        else {
            //-- add code to handle this case...not
            //-- currently needed by this implementation
        }
        return null;
    } //-- getNamespacePrefix


    /* protected methods */
    
    /**
     * Adds the given namespace node to this Element
     *
     * @param nsNode the namespace node to add
    **/
    void addNamespace(NamespaceXPathNode nsNode) {
        nsNode.setNext(null);
        nsNode.setParent(this);
        if (_firstNamespace == null) {
            nsNode.setPrevious(null);
            _firstNamespace = nsNode;
        }
        else {
            NamespaceXPathNode last = _firstNamespace;
            while (last.getNext() != null) {
                last = (NamespaceXPathNode)last.getNext();
            }
            last.setNext(nsNode);
            nsNode.setPrevious(last);
        }
    } //-- addNamespace

    /**
     * Adds the given namespace to this element
     *
     * @param nsNode the namespace node to add
    **/
    void addNamespace(String prefix, String namespace) {
        addNamespace(new NamespaceXPathNode(prefix, namespace));
    } //-- addNamespace
    
} //-- ElementWrapperXPathNode

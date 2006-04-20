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

/**
 * A wrapper class for a Text node, used by the
 * implementation of XPathNode for the W3C DOM API
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class TextWrapperXPathNode extends BaseDOMXPathNode {

    /**
     * An array to hold consectuve Text nodes
    **/
    private CharacterData[]   _textNodes      = null;
    
    /**
     * Creates a new TextWrapperXPathNode
     *
     * @param data the W3C DOM Text or CDATASection node to 
     * create the XPathNode wrapper for.
    **/
    TextWrapperXPathNode(CharacterData data) {
        super();
        _textNodes    = new CharacterData[1];
        _textNodes[0] = data;
    } //-- TextWrapperXPathNode
    
    /**
     * Returns the type of this node.
     *
     * @return The type of this node
    **/
    public int getNodeType() {
        return XPathNode.TEXT;
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
        if (_textNodes.length > 1) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < _textNodes.length; i++) {
                buffer.append(_textNodes[i].getNodeValue());
            }
            return buffer.toString();
        }
        return _textNodes[0].getNodeValue();
    } //-- getStringValue


    /* protected methods */
    
    /**
     * Adds the given Text node to this TextWrapperXPathNode.
     * It is an error if the given text node is not a following-sibling
     * of the text node that this TextWrapperXPathNode was created for.
    **/
    void addText(CharacterData data) {
        
        if (data == null) return;
        
        CharacterData[] newNodes = new CharacterData[_textNodes.length+1];
        int i = 0;
        for (; i < _textNodes.length; i++) {
            newNodes[i] = _textNodes[i];
        }
        newNodes[i] = data;
        _textNodes = newNodes;
    } //-- addText
    
} //-- TextWrapperXPathNode

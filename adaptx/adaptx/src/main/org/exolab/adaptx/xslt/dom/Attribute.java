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


package org.exolab.adaptx.xslt.dom;

import org.exolab.adaptx.xpath.XPathNode;

/**
 * A class representing an Attribute node
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Attribute extends BaseNode {


    private String _value = null;

    /**
     * Creates a new Attribute
     *
     * @param localName the local-name of this node. [Cannot be null]
     * @param value the value of this attribute
    **/
    public Attribute(String localName, String value) {
        super(null, localName);
        if ((localName == null) || (localName.length() == 0))
            throw new IllegalArgumentException("local-name must be non-null "+
                "and have a non-zero length");                
        setValue(value);
    } //-- Attribute
    
    /**
     * Creates a new Attribute
     *
     * @param namespace the namespace URI for this node. [May be null]
     * @param localName the local-name of this node. [Cannot be null]
     * @param value the value of this attribute
    **/
    public Attribute(String namespace, String localName, String value) {
        super(namespace, localName);
        if ((localName == null) || (localName.length() == 0))
            throw new IllegalArgumentException("local-name must be non-null "+
                "and have a non-zero length");                
        setValue(value);
    } //-- Attribute

    
    /* Methods defined by XPathNode */
    
    /**
     * Returns the type of this node.
     *
     * @return The type of this node
    **/
    public int getNodeType() {
        return XPathNode.ATTRIBUTE;
    } //-- getNodeType
 
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
        return _value;
    } //-- getStringValue


    /* Local Methods */
    
    /**
     * Sets the value for this XPathNode
    **/
    public void setValue(String value) {
        if (value == null)
            throw new IllegalArgumentException("value must be non-null.");
        this._value = value;
    } //-- setValue

} //-- Attribute

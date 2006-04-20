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
 * A representation of an Element node
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Element extends ParentNode {


    /**
     * A reference to the first attribute node
    **/
    private Attribute _firstAttribute = null;
    
    /**
     * A reference to the last attribute node
    **/
    private Attribute _lastAttribute  = null;
    
    /**
     * A reference to the first namespace node
    **/
    private Namespace _firstNamespace = null;
    
    /**
     * Creates a new Element
     *
     * @param namespace the namespace URI for this node. [May be null]
     * @param localName the local-name of this node. [Cannot be null]
    **/
    public Element(String namespace, String localName) {
        
        super(namespace, localName);
        
    } //-- Element


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
        return _firstAttribute;
    } //-- getFirstAttribute

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
     * Returns the value of the named attribute, or null if the
     * node has no such attribute. If the argument <tt>uri</tt>
     * is null, the node's namespace URI will be used. This method
     * is valid only for the element node.
     *
     * @param uri The attribute's namespace URI, or null
     * @param localName The attribute's local name
     * @return The attribute's value, or null if no such attribute exists
    **/
    public String getAttribute( String uri, String localName ) {
        
        if (_firstAttribute == null) return null;
        
        XPathNode attr = _firstAttribute;
        while (attr != null) {
            if (namespacesEqual(uri, attr.getNamespaceURI())) {
                if (attr.getLocalName().equals(localName))
                    return attr.getStringValue();
            }
            attr = attr.getNext();
        }
        return null;
    } //-- getAttribute

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
        
        if (prefix == null) prefix = "";
        
        XPathNode ns = _firstNamespace;
        while (ns != null) {
            if (prefix.equals(ns.getLocalName()))
                return ns.getStringValue();
            ns = ns.getNext();
        }
        return super.getNamespaceURI(prefix);
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
        if (uri == null) uri = "";
        
        XPathNode ns = _firstNamespace;
        while (ns != null) {
            if (uri.equals(ns.getStringValue()))
                return ns.getLocalName();
            ns = ns.getNext();
        }
        return super.getNamespacePrefix(uri);
    } //-- getNamespacePrefix
    

    /* Local Methods */
    
    /**
     * Adds the given Attribute to this Element
     *
     * @param attr the Attribute to add
    **/
    public void addAttribute(Attribute attr) {
        
        if (attr == null) return;
        
        attr.setParent( this );
        attr.setNext( null );
        if (_firstAttribute == null) {
            _firstAttribute = attr;
            _lastAttribute = attr;
            _lastAttribute.setPrevious( null );
        }
        else {
            attr.setPrevious( _lastAttribute );
            _lastAttribute.setNext(attr);
            _lastAttribute = attr;
        }
    } //-- Attribute

    /**
     * Adds the given Attribute to this Element
     *
     * @param attr the Attribute to add
    **/
    public void addNamespace(Namespace namespace) {
        
        if (namespace == null) return;
        
        namespace.setParent( this );
        namespace.setNext( null );
        
        if (_firstNamespace == null) {
            _firstNamespace = namespace;
        }
        else {
            XPathNode last = _firstNamespace;
            while ( last.getNext() != null)
                last = last.getNext();
                
            namespace.setPrevious( last );
            ((Namespace)last).setNext(namespace);
        }
    } //-- addNamespace
    
    /**
     * Returns true if the given two namespace URI strings are equal.
     *
     * @param ns1 namespace URI to compare with ns2
     * @param ns2 namespace URI to compare with ns1
    **/
    private boolean namespacesEqual(String ns1, String ns2) {
        if (ns1 == null) {
            return ((ns2 == null) || (ns2.length() == 0));
        }
        else if (ns2 == null) {
            return (ns1.length() == 0);
        }
        else {
            return ns1.equals(ns2);
        }
    } //-- namespacesEqual
    
} //-- Element

/*
 * (C) Copyright Keith Visco 2001-2002.  All rights reserved.
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
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xml.XMLUtil;
import org.exolab.adaptx.util.QuickStack;
import org.exolab.adaptx.util.List;
import org.xml.sax.*;


/**
 * A ResultHandler implementation that builds a XPathNode tree
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XPNBuilder implements ResultHandler {
    
    /**
     * the name of the result tree document element,
     * if one is missing
    **/
    private static final String RESULT_NAME = "result";
    
    private static final String NO_DOC_ELEMENT = 
        "adaptx:result has been added, because nodes were being "+
        "added to the result tree, but no document element " +
        "was present. XSLT result tree's must be well-formed. ";
        
    private static final String MULITPLE_DOC_ELEMENTS = 
        "adaptx:result has been added, because an element was being "+
        "added to the result tree at the document level, but a " +
        "document element already existed. " +
        "XSLT result tree's must be well-formed. ";
        
    
    /**
     * A buffer for holding character data
    **/
    private StringBuffer buffer = null;
    
    
    /**
     * flag to indicate the number of document fragments we are in
    **/
    private short fragCount = 0;
    
    
    //-- do not need these yet
    //private Hashtable idRefs     = null;
    //private Hashtable parentRefs = null;
    
    private boolean   finished   = false;
    
    /**
     * SAX document Locator, set by a SAX Parser
    **/
    private Locator _locator = null;
    
    /**
     * The current node stack
    **/
    QuickStack nodeStack = null;
    
    /**
     * Root XPathNode, the XPathNode being created.
    **/
    private Root _root = null;
    
    /**
     * Flag to indicate whether or not to save
     * location information to each XPathNode.
    **/
    private boolean _saveLocation = false;
    
    /**
     * flag to indicate if we have started a document
    **/
    private boolean started = false;    
        
    
    /**
     * Creates a new XPNBuilder
    **/
    public XPNBuilder() {
        _root = new Root();
        nodeStack = new QuickStack();
        nodeStack.push(_root);
        buffer = new StringBuffer();
    } //-- XPNBuilder
    
    /**
     * Signals to receive CDATA characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void cdata(char[] chars, int start, int length) {
        ParentNode currentNode = (ParentNode)nodeStack.peek();
        buffer.append(chars, start, length);
        currentNode.addChild(new Text(buffer.toString()));
        buffer.setLength(0);
    } //-- cdata
    
    /**
     * Signals the start of characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void characters(char[] chars, int start, int length) 
        throws org.xml.sax.SAXException
    {
        ParentNode currentNode = (ParentNode)nodeStack.peek();
        buffer.append(chars, start, length);
        currentNode.addChild(new Text(buffer.toString()));
        buffer.setLength(0);
    } //-- characters
    
    
    public void comment(String data) {
        ParentNode currentNode = (ParentNode)nodeStack.peek();
        currentNode.addChild(new Comment(data));
    } //-- comment
    
    /**
     * Signals the end of the document
    **/
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        started  = false;
        finished = true;
    } //-- endDocument
    
    /**
     * Signals the start of element
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
    **/
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        //-- handle qname
	    int idx = name.indexOf(':');
	    String prefix = null;
	    if (idx >= 0) {
	        prefix = name.substring(0,idx);
	        name = name.substring(idx+1);
	    }
	    
	    // XXX - We should check namespaces, but
	    // for now, we'll ignore this.

        ParentNode currentNode = (ParentNode)nodeStack.peek();
        String cName = currentNode.getLocalName();
        if (!cName.equals(name)) {
            String err = "element mismatch error, expecting </";
            err += cName + ">, but recieved </";
            err += name +"> instead.";
            throw new SAXException(err);
        }
        nodeStack.pop();
        finished = (nodeStack.size() == 1);
    } //-- endElement
    
    /**
     * Signals to recieve an entity reference with the given name
     * @param name the name of the entity reference
    **/
    public void entityReference(String name) {
        ///-- A hack I know
        String entity = '&' + name + ';';
        ParentNode currentNode = (ParentNode)nodeStack.peek();
        currentNode.addChild(new Text(entity));
    } //-- entityReference
    
    
    /**
     * Starts a documentFragment and returns a handle to the fragment
     * This fragment won't be added to the DOM tree
    **/
    public XPathNode startFragment() {
        Element parent = new Element(null, "#fragment");
        nodeStack.push(parent);
        ++fragCount;
        return parent;
    } //-- startDocumentFragment

    /** 
     * 
    **/
    public void endFragment() {
        if (fragCount > 0) {
            XPathNode node = (XPathNode)nodeStack.peek();
            if (node.getLocalName().equals("#fragment")) {
                nodeStack.pop();
            }
            //else we are in a nasty error!
            --fragCount;
        }
    } //-- endFragment
    
    /**
     * Returns the current node
     * @return the current node
    **/
    public XPathNode getCurrentNode() {
        return (XPathNode)nodeStack.peek();
    } //-- getCurrentNode
    
    /**
     * Returns the Root node
     *
     * @return the root node
    **/
    public XPathNode getRoot() {
        return _root;
    } //-- getRoot
    
    /**
     * Signals the start of ignorable whitespace characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void ignorableWhitespace(char[] chars, int start, int length) 
        throws org.xml.sax.SAXException
    {
        //-- ignore for now!
    } //-- ignorableWhitespace
       
    /**
     * Returns true if the DOM builder has completed
     * @return true if the DOM builder has completed
    **/
    public boolean isFinished() {
        return finished;
    } //-- isFinished
    
    /**
     * Signals to recieve a processing instruction
     * @param target the target of the processing instruction
     * @param data the content of the processing instruction
    **/
    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        ProcessingInstruction pi = null;
        pi = new ProcessingInstruction(target, data);
        ParentNode currentNode = (ParentNode)nodeStack.peek();
        currentNode.addChild(pi);
        
    } //-- processingInstruction
    
    /**
     * Sets the document locator 
     * @param locator the Locator used by this DocumentHandler
    **/
    public void setDocumentLocator(Locator locator) {
        _locator = locator;
    } //-- setDocumentLocator
    
    /**
     * Sets the behavoir of handling character content. If argument is true,
     * character content will be escaped. If false, character content will
     * not be escaped.
     * @param escapeText the flag indicating whether or not to
     * escape character content
    **/
    public void setEscapeText(boolean escapeText) {
        //-- ignored by this Formatter
    }
    
    /**
     * Sets the indent size for all formatters that perform
     * serialization, in which indentation is applicable.
     * @param indentSize the number of characters to indent
    **/
    public void setIndentSize(short indentSize) {
        //-- indent size is ignored by this Formatter
    } //-- setIndentSize
    
    
    /**
     * Sets the output format information for Formatters that
     * perform serialization.
     * @param format the OutputFormat used to specify properties
     * during serialization
    **/
    public void setOutputFormat(OutputFormat format) {
        //-- ignored by this Formatter
    } //-- setOutputFormat
    
    /**
     * Sets whether or not to save location information. Location
     * information can only be saved if the Locator has been
     * set by the SAX Parser.
     *
     * @param saveLocation a boolean that when true, indicates that location
     * information should be saved if possible.
    **/
    public void setSaveLocation(boolean saveLocation) {
        _saveLocation = saveLocation;
    } //-- setSaveLocation
    
    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws SAXException
    {
        started  = true;
        finished = false;
        
    } //-- startDocument
    
    /**
     * Signals the start of element
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
    **/
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException 
    {
        if (name == null) 
            throw new SAXException("missing element name");
            
        ParentNode currentNode = (ParentNode)nodeStack.peek();
        if (currentNode == _root) {
	        XPathNode child  = _root.getFirstChild();
	        if (child != null) {
	            //-- XXX handle error, ignore for now!
	        }
	    }
	    
	    //-- handle qname
	    int idx = name.indexOf(':');
	    String prefix = null;
	    if (idx >= 0) {
	        prefix = name.substring(0,idx);
	        name = name.substring(idx+1);
	    }
	    
	    Element element = new Element(null, name);
	    
	    if ((_saveLocation) && (_locator != null)) {
	        int line = _locator.getLineNumber();
	        int column = _locator.getColumnNumber();
	        element.setProperty(Element.LOCATION_PROPERTY, 
	            new Location(line, column));
	    }
	    
	    //-- add element to current parent node
	    currentNode.addChild(element);
	    
	    if (atts != null) {
	        boolean[] namespaces = new boolean[atts.getLength()];
	        for (int i = 0; i < atts.getLength(); i++) {
	            String attName = atts.getName(i);
	            //--XXX hack, change this
	            //-- handle Namespace nodes
	            if (attName.equals("xmlns")) {
	                element.addNamespace(new Namespace("", 
	                    atts.getValue(i)));
	                namespaces[i] = true;
	            }
	            else if (attName.startsWith("xmlns:")) {
	                String prefixDecl = attName.substring(6);
	                element.addNamespace(new Namespace(prefixDecl, 
	                    atts.getValue(i)));
	                namespaces[i] = true;
	            }
	            else {
	                namespaces[i] = false;
	            }
	        }
	        for (int i = 0; i < namespaces.length; i++) {
	            if (!namespaces[i]) {
	                String attName = atts.getName(i);
	                String ns = null;
	                idx = attName.indexOf(':');
	                if (idx > 0) {
	                    ns = element.getNamespaceURI(attName.substring(0,idx));
	                    attName = attName.substring(idx+1);
	                }
	                element.addAttribute(new Attribute(ns, attName, 
	                    atts.getValue(i)));
	            }
	        }
	    }
	    
	    //-- set namespace if necessary
	    if ((prefix != null) && (prefix.length() > 0)) {
	        String namespace = element.getNamespaceURI(prefix);
	        element.setNamespace(namespace);
	    }
	    else {
	        String namespace = element.getNamespaceURI("");
	        if (namespace != null) {
	            element.setNamespace(namespace);
	        }
	    }

	    
	    nodeStack.push(element);
    } //-- startElement
    
    /**
     * Signals to receive characters which should not be escaped
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void unescapedCharacters(char[] chars, int start, int length) {
        //-- The DOM has no notion of non-escaped characters,
        //-- just call CDATA
        cdata(chars, start, length);
    } //-- unescapedCharacters
    
    //----------------------/
    //-- protected methods -/
    //----------------------/
    
} //-- XPNBuilder

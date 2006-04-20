/*
 * (C) Copyright Keith Visco 1999-2001  All rights reserved.
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
 
package org.exolab.adaptx.xslt.handlers;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.exolab.adaptx.xml.XMLUtil;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.util.QuickStack;
import java.util.Hashtable;


/**
 * An implementation of ResultHandler that builds a DOM tree
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DOMBuilder implements ResultHandler {
    
    /**
     * the name of the result tree document element,
     * if one is missing
    **/
    private static final String RESULT_NAME = "xslp:result";
    
    private static final String NO_DOC_ELEMENT = 
        "xslp:result has been added, because nodes were being "+
        "added to the result tree, but no document element " +
        "was present. XSLT result tree's must be well-formed. ";
        
    private static final String MULITPLE_DOC_ELEMENTS = 
        "xslp:result has been added, because an element was being "+
        "added to the result tree at the document level, but a " +
        "document element already existed. " +
        "XSLT result tree's must be well-formed. ";
        
    
    /**
     * A buffer for holding character data
    **/
    private StringBuffer buffer = null;
    
    
    /**
     * The current node stack
    **/
    QuickStack nodeStack = null;
    
    /**
     * The document being created
    **/
    private Document document = null;
    
    /**
     * flag to indicate if we have started a document
    **/
    private boolean started = false;
    
    /**
     * flag to indicate the number of document fragments we are in
    **/
    private short fragCount = 0;
    
    
    //-- do not need these yet
    //private Hashtable idRefs     = null;
    //private Hashtable parentRefs = null;
    
    private boolean   finished   = false;
    
    /**
     * Creates a new DOMBuilder
     *
     * @param the Document in which to use when building
     * the DOM. If the document is not empty...it will
     * be cleared, by removing the document element.
    **/
    public DOMBuilder(Document document) {
        
        if (document == null) {
            String err = "Document passed to constructor of " +
                "DOMBuilder must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        //-- clean document
        Element elem = document.getDocumentElement();
        if (elem != null) document.removeChild(elem);
        
        this.document = document;
        
        nodeStack = new QuickStack();
        nodeStack.push(document);
        buffer = new StringBuffer();
    } //-- DOMBuilder
    
    /**
     * Creates a new DOMBuilder with the given node
     *
     * @param node the Node to use when building the DOM fragment
     */
    public DOMBuilder(Node node) {
        
        if (node == null) {
            String err = "The DOM node passed to constructor of " +
                "DOMBuilder must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                document = node.getOwnerDocument();
                break;
            case Node.DOCUMENT_NODE:
                document = (Document)node;
                break;
            default:
                String err = "Invalid node type. The DOM node passed to " +
                    "the constructor of DOMBuilder must be either an " +
                    "Element node or a Document node.";
                throw new IllegalArgumentException(err);
        }
        
        nodeStack = new QuickStack();
        nodeStack.push(node);
        buffer = new StringBuffer();
    } //-- DOMBuilder
    
    
    /**
     * Signals to receive CDATA characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void cdata(char[] chars, int start, int length) {
        
        Node currentNode = (Node)nodeStack.peek();
	    if (currentNode == document) {
	        
	        //-- check for allowable whitespace
	        if (XMLUtil.isWhitespace(chars, start, length)) return;
	        
	        //-- create wrapper element
	        Element wrapper = document.createElement(RESULT_NAME);
	        document.appendChild(wrapper);
	        currentNode = wrapper;
	        //-- add comment to wrapper
	        Comment comment = 
	            document.createComment(NO_DOC_ELEMENT);
	        currentNode.appendChild(comment);
	    }
        buffer.append(chars, start, length);
        CDATASection cdata = document.createCDATASection(buffer.toString());
        currentNode.appendChild(cdata);
        buffer.setLength(0);
    }
    
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
        
        Node currentNode = (Node)nodeStack.peek();
	    if (currentNode == document) {
	        
	        //-- check for allowable whitespace
	        if (XMLUtil.isWhitespace(chars, start, length)) return;
	        
	        //-- create wrapper element
	        Element wrapper = document.createElement(RESULT_NAME);
	        document.appendChild(wrapper);
	        currentNode = wrapper;
	        //-- add comment to wrapper
	        Comment comment = 
	            document.createComment(NO_DOC_ELEMENT);
	        currentNode.appendChild(comment);
	    }
        buffer.append(chars, start, length);
        Text text = document.createTextNode(buffer.toString());
        currentNode.appendChild(text);
        buffer.setLength(0);
    } //-- characters
    
    
    public void comment(String data) {
        Node currentNode = (Node)nodeStack.peek();
        Comment remark = document.createComment(data);
        currentNode.appendChild(remark);
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
        Node currentNode = (Node)nodeStack.peek();
        String cName = currentNode.getNodeName();
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
        Node currentNode = (Node)nodeStack.peek();
        EntityReference ref = document.createEntityReference(name);
        currentNode.appendChild(ref);
    } //-- entityReference
    
    
    /**
     * Starts a documentFragment and returns a handle to the fragment
     * This fragment won't be added to the DOM tree
    **/
    protected DocumentFragment startDocumentFragment() {
        DocumentFragment df = document.createDocumentFragment();
        nodeStack.push(df);
        ++fragCount;
        return df;
    } //-- startDocumentFragment

    /** 
     * 
    **/
    protected void stopDocumentFragment() {
        if (fragCount > 0) {
            Node node = (Node)nodeStack.peek();
            if (node.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) {
                nodeStack.pop();
            }
            //else we are in a nasty error!
            --fragCount;
        }
    } //-- startDocumentFragment
    
    /**
     * Returns the current node
     * @return the current node
    **/
    protected Node getCurrentNode() {
        return (Node)nodeStack.peek();
    } //-- getCurrentNode
    
    /**
     * Returns the document created by this DOMFormatter
    **/
    Document getDocument() {
        return document;
    } //-- getDocument
    
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
        pi = document.createProcessingInstruction(target, data);
        Node currentNode = (Node)nodeStack.peek();
        currentNode.appendChild(pi);
        
    } //-- processingInstruction
    
    /**
     * Sets the document locator 
     * @param locator the Locator used by this DocumentHandler
    **/
    public void setDocumentLocator(Locator locator) {
        //-- ignored by this DocumentHandler
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
            
            
        Node currentNode = (Node)nodeStack.peek();
        Element element = null;
        if (currentNode == document) {
	        element = document.getDocumentElement();
	        if (element != null) {
	            document.removeChild(element);
	            //-- create wrapper element
	            Element wrapper = document.createElement(RESULT_NAME);
	            document.appendChild(wrapper);
	            //-- add old document element to wrapper
	            wrapper.appendChild(element);
	            //-- set current element to be wrapper
	            currentNode = wrapper;
	            //-- add comment to wrapper
	            Comment comment = document.createComment(MULITPLE_DOC_ELEMENTS);
	            currentNode.appendChild(comment);
	        }
	    }
	    element = document.createElement(name);
	    if (atts != null) {
	        for (int i = 0; i < atts.getLength(); i++) {
	            element.setAttribute(atts.getName(i), atts.getValue(i));
	        }
	    }
	    currentNode.appendChild(element);
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
    
} //-- DOMBuilder    

/*
 * (C) Copyright Keith Visco 2000-2003  All rights reserved.
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


import org.xml.sax.Attributes;
import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.exolab.adaptx.util.NestedRuntimeException;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xslt.util.Namespaces;

import java.util.Enumeration;

/**
 * A simple implemtation of ResultHandler which acts as an
 * adapter for a SAX ContentHandler.
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class ResultHandlerAdapter2 implements ResultHandler {

    
    /**
     * The default namespace declaration node name
     */
    private static final String XMLNS = "xmlns";
    
    /**
     * The namespace declaration prefix
     */
    private static final String XMLNS_PREFIX = "xmlns:";
    
    private static final String CDATA = "CDATA";
    
    /**
     * A flag to keep track if the startDocument method was called.
     */
    private boolean startDocumentCalled = false;
    
    /**
     * The ContentHandler we are "adapting"
     */
    private ContentHandler _handler = null;
    
    /**
     * The LexicalHandler to handle special SAX events such
     * as Comments and CDATA sections
     */
    private LexicalHandler _lexHandler = null;
    
    /**
     * The namespace declarations
     */
    private Namespaces _namespaces = null;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new ResultHandlerAdapter2 with the given 
     * ContentHandler
     *
     * @param handler the ContentHandler to "adapt".
     */
    public ResultHandlerAdapter2(ContentHandler handler) {
        if (handler == null) {
            String err = "The argument 'handler' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        _handler = handler;
        
        if (handler instanceof LexicalHandler)
            _lexHandler = (LexicalHandler)handler;
            
        _namespaces = new Namespaces();
    } //-- ResultHandlerAdapter2

    /**
     * Sets the given LexicalHandler to handle special events,
     * such as comments.
     *
     * @param lexHandler the LexicalHandler. May be null.
     */
    public void setLexicalHandler(LexicalHandler lexHandler) {
        _lexHandler = lexHandler;
        if (lexHandler == null) {
            if (_handler instanceof LexicalHandler)
                _lexHandler = (LexicalHandler)_handler;
        }
    } //-- setLexicalHandler
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    
    /**
     * Signals to receive CDATA characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void cdata(char[] chars, int start, int length) {
        try {
            if (_lexHandler != null) {
                _lexHandler.startCDATA();
                _handler.characters(chars, start, length);
                _lexHandler.endCDATA();
            }
            else {
                _handler.characters(chars, start, length);
            }
        }
        catch(org.xml.sax.SAXException sx) {
            throw new NestedRuntimeException(sx);
        }
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
        _handler.characters(chars, start, length);
    } //-- characters
    
    
    public void comment(String data) {
        if (_lexHandler != null) {
            try {
                char[] chars = data.toCharArray();
                _lexHandler.comment(chars, 0, chars.length);
            }
            catch(org.xml.sax.SAXException sx) {
                throw new NestedRuntimeException(sx);
            }
        }
    } //-- comment
    
    /**
     * Signals the end of the document
    **/
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        _handler.endDocument();
    } //-- endDocument
    
    /**
     * Signals the end of element
     *
     * @param name the qualified name of the element
     */
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        int idx = name.indexOf(':');
        if (idx >= 0) {
            String prefix = name.substring(0, idx);
            String uri = _namespaces.getNamespaceURI(prefix);
            _handler.endElement(uri, name.substring(idx+1), name);
        }
        else {
            String uri = _namespaces.getNamespaceURI("");
            _handler.endElement(uri, name, name);
        }
        
        Enumeration enum = _namespaces.getLocalNamespacePrefixes();
        while (enum.hasMoreElements()) {
            _handler.endPrefixMapping((String)enum.nextElement());
        }
        _namespaces = _namespaces.getParent();
        
    } //-- endElement
    
    /**
     * Signals to recieve an entity reference with the given name
     * @param name the name of the entity reference
    **/
    public void entityReference(String name) {
        //-- ignore for now
    } //-- entityReference
    
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
        _handler.ignorableWhitespace(chars, start,length);
    } //-- ignorableWhitespace
        
    /**
     * Signals to recieve a processing instruction
     * @param target the target of the processing instruction
     * @param data the content of the processing instruction
    **/
    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        _handler.processingInstruction(target, data);
        
    } //-- processingInstruction
    
    /**
     * Sets the document locator 
     * @param locator the Locator used by this DocumentHandler
    **/
    public void setDocumentLocator(Locator locator) {
        _handler.setDocumentLocator(locator);
    } //-- setDocumentLocator
        
    /**
     * Sets the indent size for all formatters that perform
     * serialization, in which indentation is applicable.
     * @param indentSize the number of characters to indent
    **/
    public void setIndentSize(short indentSize) {
        //-- ignore
    } //-- setIndentSize
    
    
    /**
     * Sets the output format information for Formatters that
     * perform serialization.
     * @param format the OutputFormat used to specify properties
     * during serialization
    **/
    public void setOutputFormat(OutputFormat format) {
        //-- ignore
    } //-- setOutputFormat
    
    
    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws org.xml.sax.SAXException
    {
        startDocumentCalled = true;
        _handler.startDocument();
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
        if (!startDocumentCalled) startDocument();
        
        _namespaces = _namespaces.createNamespaces();
        
        AttributesImpl attributes = new AttributesImpl();
        //-- process atts and handle namespaces
        if ((atts != null) && (atts.getLength() > 0)) {
            //-- first process any namespaces, and any non namespace
            //-- prefixed attributes
            int restartIdx = -1;
            boolean prefixedAtts = false;
            for (int i = 0; i < atts.getLength(); i++) {
                String attName = atts.getName(i);
                if (XMLNS.equals(attName)) {
                    _namespaces.addNamespace("", atts.getValue(i));
                    _handler.startPrefixMapping("", atts.getValue(i));
                }
                else if (attName.startsWith(XMLNS_PREFIX)) {
                    String prefix = attName.substring(XMLNS_PREFIX.length());
                    _namespaces.addNamespace(prefix, atts.getValue(i));
                    _handler.startPrefixMapping(prefix, atts.getValue(i));
                }
                else if (attName.indexOf(':') < 0) {
                    String type = atts.getType(i);
                    if (type == null) type = CDATA;
                    attributes.addAttribute("",attName, attName, type, atts.getValue(i));
                }
                else {
                    if (restartIdx < 0) restartIdx = i;
                }
            }
            //-- process any prefixed attributes
            if (restartIdx >= 0) {
                for (int i = restartIdx; i  < atts.getLength(); i++) {
                    String attName = atts.getName(i);
                    int idx = attName.indexOf(':');
                    if (idx >= 0) {
                        if (attName.startsWith(XMLNS_PREFIX)) continue;
                        String prefix = attName.substring(0, idx);
                        String uri = _namespaces.getNamespaceURI(prefix);
                        String localName = attName.substring(idx+1);
                        String type = atts.getType(i);
                        if (type == null) type = CDATA;
                        attributes.addAttribute(uri, localName, attName, type, atts.getValue(i));
                    }
                }
            }
        } //-- end Attributes processing
        
        
        int idx = name.indexOf(':');
        if (idx >= 0) {
            String prefix = name.substring(0, idx);
            String uri = _namespaces.getNamespaceURI(prefix);
            _handler.startElement(uri, name.substring(idx+1), name, attributes);
        }
        else {
            String uri = _namespaces.getNamespaceURI("");
            _handler.startElement(uri, name, name, attributes);
        }
        
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
        try {
            _handler.characters(chars, start, length);
        }
        catch(org.xml.sax.SAXException sx) {
            throw new NestedRuntimeException(sx);
        }
    } //-- unescapedCharacters
    
} //-- ResultHandlerAdapter

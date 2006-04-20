/*
 * (C) Copyright Keith Visco 1998-2001  All rights reserved.
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
 
package org.exolab.adaptx.xslt.util;

import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xslt.handlers.DefaultHandler;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xslt.XSLTProcessor;
import org.exolab.adaptx.xslt.XSLTStylesheet;
import org.xml.sax.*;
import java.io.*;

/**
 * A simple *hack* to provide a SAX adapter for the source tree
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SAXInput implements DocumentHandler {
    
    XPNBuilder      _xpnBuilder     = null;    
    XSLTStylesheet  _stylesheet     = null;
    XSLTProcessor   _xslProcessor   = null;
    ResultHandler   _handler        = null;
     
    int depth = 0;
    
    /**
     * Creates a new SAXInput
    **/
    public SAXInput() {
        _xpnBuilder = new XPNBuilder();
    } //-- SAXInput
    
    /**
     * Sets the XSLTProcessor to use for the XSL Transformations
     * @param processor the XSLTProcessor to invoke
    **/
    public void setProcessor(XSLTProcessor processor) {
        this._xslProcessor = processor;
    } //-- setProcessor
    
    public void setOutputHandler(ResultHandler handler) {
        this._handler = handler;
    } //-- setOutputHandler
    
    public void setOutputHandler(Writer writer) {
        this._handler = new DefaultHandler(writer);
    } //-- setOutputHandler
    
    public void setStylesheet(XSLTStylesheet stylesheet) {
        this._stylesheet = stylesheet;
    } //-- setStylesheet
    
    //-------------------------------------/
    //- Implementation of DocumentHandler -/
    //-------------------------------------/
    
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
        _xpnBuilder.characters(chars, start, length);
        
    } //-- characters
    
    /**
     * Signals the end of the document
    **/
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        _xpnBuilder.endDocument();
        
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
        _xpnBuilder.endElement(name);
        if (_xpnBuilder.isFinished() && (_xslProcessor != null)) {
            _xslProcessor.process(_xpnBuilder.getRoot(),
                                  _stylesheet, 
                                  _handler);
        }
    } //-- endElement
    
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
        _xpnBuilder.ignorableWhitespace(chars, start, length);
    } //-- ignorableWhitespace
       
    /**
     * Signals to recieve a processing instruction
     * @param target the target of the processing instruction
     * @param data the content of the processing instruction
    **/
    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        _xpnBuilder.processingInstruction(target, data);
    } //-- processingInstruction
    
    /**
     * Sets the document locator 
     * @param locator the Locator used by this DocumentHandler
    **/
    public void setDocumentLocator(Locator locator) {
        _xpnBuilder.setDocumentLocator(locator);
    } //-- setDocumentLocator
    
    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws SAXException
    {
        _xpnBuilder.startDocument();
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
        _xpnBuilder.startElement(name, atts);
        
    } //-- startElement
    
} //-- SAXInput    

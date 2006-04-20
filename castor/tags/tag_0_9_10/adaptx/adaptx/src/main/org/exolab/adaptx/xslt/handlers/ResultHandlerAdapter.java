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


import org.xml.sax.*;
import org.exolab.adaptx.util.NestedRuntimeException;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xslt.ResultHandler;
import java.io.PrintWriter;

/**
 * A simple implemtation of ResultHandler which acts as an
 * adapter for a SAX DocumentHandler.
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class ResultHandlerAdapter implements ResultHandler {

    
    /**
     * The DocumentHandler we are "adapting"
    **/
    private DocumentHandler _handler = null;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new ResultHandlerAdapter
     *
     * @param handler the DocumentHandler to "adapt".
    **/
    public ResultHandlerAdapter(DocumentHandler handler) {
        if (handler == null) {
            PrintWriter pw = new PrintWriter(System.out, true);
            _handler = new DefaultHandler(pw);
        }
        else _handler = handler;
    } //-- ResultHandlerAdapter

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
            _handler.characters(chars, start, length);
        }
        catch(org.xml.sax.SAXException sx) {
            throw new NestedRuntimeException(sx);
        }
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
        _handler.characters(chars, start, length);
    } //-- characters
    
    
    public void comment(String data) {
        //-- cannot do anything here
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
     * Signals the start of element
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
    **/
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        _handler.endElement(name);
    } //-- endElement
    
    /**
     * Signals to recieve an entity reference with the given name
     * @param name the name of the entity reference
    **/
    public void entityReference(String name) {
        //-- cannot do anything since DocumentHandler
        //-- does not have support for this
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
        //-- cannot do anything since DocumentHandler
        //-- does not have support for this
    } //-- setIndentSize
    
    
    /**
     * Sets the output format information for Formatters that
     * perform serialization.
     * @param format the OutputFormat used to specify properties
     * during serialization
    **/
    public void setOutputFormat(OutputFormat format) {
        //-- cannot do anything since DocumentHandler
        //-- does not have support for this
    } //-- setOutputFormat
    
    
    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws org.xml.sax.SAXException
    {
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
        _handler.startElement(name, atts);
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

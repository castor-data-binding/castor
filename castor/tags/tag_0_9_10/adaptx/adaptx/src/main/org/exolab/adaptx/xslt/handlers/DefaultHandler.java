/*
 * (C) Copyright Keith Visco 1999-2003  All rights reserved.
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

//-- imports
import org.exolab.adaptx.util.NestedRuntimeException;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xslt.XSLOutput;
import org.xml.sax.*;
import java.io.*;

/* 
 Note: 
 I don't simply include serialize.* because I don't want a 
 conflict with org.apache.xml.serialize.OutputFormat and 
 org.exolab.adaptx.xslt.OutputFormat which is part of this Package
*/
import org.apache.xml.serialize.BaseMarkupSerializer;
import org.apache.xml.serialize.SerializerFactory;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.TextSerializer;
import org.apache.xml.serialize.HTMLSerializer;


/**
 * The default implementation of ResultHandler.
 * It uses the Apache Serializer classes written by my
 * friend and colleague 
 * <a href="mailto:arkin@intalio.com">Assaf Arkin</a>.
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class DefaultHandler implements ContentHandler, ResultHandler {

    
    /**
     * The output format to use with this Formatter
    **/
    private OutputFormat _format = null;
    
    /**
     * The Apache OutputFormat (converted from _format)
    **/
    private org.apache.xml.serialize.OutputFormat _apOutputFormat = null;
    
    
    /**
     * The write to serialize data to
    **/
    Writer _out = null;
    
    /**
     * The Serializer used for printing output
    **/
    private BaseMarkupSerializer _serializer = null;
    
    private boolean _chooseMethod = false;
    
    
    /**
     * Keeps track of the tree depth
    **/
    private int depth = 0;
    
    private boolean _hasElements = false;
    
    /**
     * A list of saved events
    **/
    private Event _saved = null;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new DefaultHandler
    **/
    public DefaultHandler(Writer out) {
        this(out, null);
    } //-- DefaultHandler

    /**
     * Creates a new DefaultHandler
    **/
    public DefaultHandler(Writer out, OutputFormat format) {
        super();
        
        //-- initialize out...if null use System.out
        if (out == null)
            this._out = new OutputStreamWriter(System.out);
        else 
            this._out = out;
            
        setOutputFormat(format);
        init();
    } //-- DefaultHandler

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
    public void cdata(char[] chars, int start, int length)
    {
        _serializer.startCDATA();
        try {
            _serializer.characters(chars, start, length);
        }
        catch(org.xml.sax.SAXException ex) {
            throw new NestedRuntimeException(ex);
        }
        _serializer.endCDATA();
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
        _serializer.characters(chars, start, length);
    } //-- characters
    
    
    public void comment(String data) 
    {
        if (_hasElements) {
            try {
                _serializer.comment(data);
            }
            catch(java.io.IOException iox) {
                throw new NestedRuntimeException(iox);
            }
        }
        else {
            Event event = new CommentEvent(data);
            if (_saved == null) _saved = event;
            else _saved.append(event);
        }
    } //-- comment
    
    /**
     * Signals the end of the document
    **/
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        _serializer.endDocument();
        try {
            if (_out != null) _out.flush();
        }
        catch(java.io.IOException ioe) {
            //-- Ignore exception, writer may have
            //-- been closed by Serializer, the flush
            //-- is just a precautionary step to make
            //-- sure the buffer has been flushed
            //-- in case the Serializer doesn't do it.
        }
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
        _serializer.endElement(name);
        --depth;
        if (depth == 0) {
            try {
                if (_out != null) _out.flush();
            }
            catch(java.io.IOException ioe) {
                //-- Ignore exception, writer may have
                //-- been closed by Serializer, the flush
                //-- is just a precautionary step to make
                //-- sure the buffer has been flushed
                //-- in case the Serializer doesn't do it.
            }
        }
    } //-- endElement
    
    /**
     * <p>ContentHandler#endElement</p>
     *
     * Signals the end of an element
     *
     * @param name the name of the element
     */
    public void endElement(String namespaceURI, String localName, String qName) 
        throws org.xml.sax.SAXException
    {
        _serializer.endElement(namespaceURI, localName, qName);
        --depth;
        if (depth == 0) {
            try {
                if (_out != null) _out.flush();
            }
            catch(java.io.IOException ioe) {
                //-- Ignore exception, writer may have
                //-- been closed by Serializer, the flush
                //-- is just a precautionary step to make
                //-- sure the buffer has been flushed
                //-- in case the Serializer doesn't do it.
            }
        }
        
    } //-- endElement
    
    /**
     * Signals to end the namespace prefix mapping
     * 
     * @param prefix the namespace prefix 
     */
    public void endPrefixMapping(String prefix)
        throws SAXException
    { 
        _serializer.endPrefixMapping(prefix);
        
    } //-- endPrefixMapping
    
    /**
     * Signals to recieve an entity reference with the given name
     * @param name the name of the entity reference
    **/
    public void entityReference(String name) {
        //printer.printEntityReference(name);
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
        //-- ignore for now!
    } //-- ignorableWhitespace
        
    /**
     * Signals to recieve a processing instruction
     * @param target the target of the processing instruction
     * @param data the content of the processing instruction
    **/
    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        if (_hasElements)
            _serializer.processingInstruction(target, data);
        else {
            Event event = new PIEvent(target, data);
            if (_saved == null) _saved = event;
            else _saved.append(event);
        }
        
    } //-- processingInstruction
    
    /**
     * Sets the document locator 
     * @param locator the Locator used by this DocumentHandler
    **/
    public void setDocumentLocator(Locator locator) {
        _serializer.setDocumentLocator(locator);
    } //-- setDocumentLocator
        
    /**
     * Sets the indent size for all formatters that perform
     * serialization, in which indentation is applicable.
     * @param indentSize the number of characters to indent
    **/
    public void setIndentSize(short indentSize) {
        _apOutputFormat.setIndent(indentSize);
        //-- indent size is ignored by this Formatter
    } //-- setIndentSize
    
    
    /**
     * Sets the output format information for Formatters that
     * perform serialization.
     * @param format the OutputFormat used to specify properties
     * during serialization
    **/
    public void setOutputFormat(OutputFormat format) {
        
        if (_hasElements) {
            String err = "The output format of DefaultFormatter cannot "+
                "be changed while in use.";
            throw new IllegalStateException(err);
        }
        
        if (format == null) 
            this._format = new XSLOutput();
        else
            this._format = format;
            
        this._apOutputFormat = convertFormat(this._format);
        
        //if (_serializer != null)
        //   _serializer.setOutputFormat(_apOutputFormat);
        //-- since serializer does not allow chaning the
        //-- output format once constructed we need to
        //-- create a new one
        if (_serializer != null) {
            try {
                initSerializer(_format.getMethod());
            }
            catch (SAXException sx) {
                throw new IllegalStateException(sx.toString());   
            }
        }
            
    } //-- setOutputFormat
    
    
    /**
     * Signals that an entity was skipped by the parser
     *
     * @param name the skipped entity's name
     */
    public void skippedEntity(String name)
        throws SAXException
    {
        _serializer.skippedEntity(name);
        
    } //-- skippedEntity
    
    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws org.xml.sax.SAXException
    {
        _serializer.startDocument();
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
        if ((!_hasElements) && (_chooseMethod)) {
            initSerializer(name);
            _chooseMethod = false;
        }
        _hasElements = true;
        _serializer.startElement(name, atts);
        ++depth;
    } //-- startElement
    
    /**
     * <p>ContentHandler#startElement</p>
     *
     * Signals the start of element
     *
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
        throws org.xml.sax.SAXException 
    {
        if ((!_hasElements) && (_chooseMethod)) {
            String name = localName;
            if ((name == null) || (name.length() == 0))
                name = qName;
            initSerializer(name);
            _chooseMethod = false;
        }
        _hasElements = true;
        _serializer.startElement(namespaceURI, localName, qName, atts);
        ++depth;
    } //-- startElement
    
    /**
     * Signals to start the namespace - prefix mapping
     * 
     * @param prefix the namespace prefix to map
     * @param uri the namespace URI
     */
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    { 
        if (_hasElements) {
            _serializer.startPrefixMapping(prefix, uri);
        }
        else {
            Event event = new NamespaceEvent(prefix, uri);
            if (_saved == null) _saved = event;
            else _saved.append(event);
        }
        
        
    } //-- startPrefixMapping
    
    /**
     * Signals to receive characters which should not be escaped
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void unescapedCharacters(char[] chars, int start, int length) {
        cdata(chars, start, length);        
    } //-- unescapedCharacters
    
    //---------------------/
    //- Protected Methods -/
    //---------------------/
    
    /**
     * Allows subclasses to get a handle to the internal printer
     * @return the {@link Serializer} being used by this Formatter.
     * This may return null.
    **/
    protected Serializer getSerializer() {
        return _serializer;
    } //-- getSerializer
    
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    private void initSerializer(String method) 
        throws org.xml.sax.SAXException 
    {
        
        if (_hasElements) return;
        
        if (method == null) {
            _chooseMethod = true;
            if (_serializer == null) {
                _serializer = new XMLSerializer(_out, _apOutputFormat);
            }
        }
        else if ("html".equalsIgnoreCase(method))
            _serializer = new HTMLSerializer(_out, _apOutputFormat); 
        else if ("text".equalsIgnoreCase(method)) {
            _serializer = new TextSerializer();
            _serializer.setOutputCharStream( _out);
            _serializer.setOutputFormat( _apOutputFormat );
        }
        else 
            _serializer = new XMLSerializer(_out, _apOutputFormat);
        
        try {
            _serializer.asDocumentHandler();
        }
        catch(java.io.IOException ioe) {
            //
        }
        
        Event event = _saved;
        while (event != null) {
            event.fire(_serializer);
            event = event.next;
        }
    } //-- initSerialzer
    
    private void init() {
        if (_format == null) {
            _format = new XSLOutput();
            _apOutputFormat = convertFormat(_format);
        }
        try {
            initSerializer(_format.getMethod());
        }
        catch (org.xml.sax.SAXException sx) {
            throw new IllegalStateException(sx.toString());
        }
    } //-- init
    
    /**
     * Converts the given {@link org.exolab.adaptx.xslt.OutputFormat} to an
     * {@link org.apache.xml.serialize.OutputFormat}
    **/
    private org.apache.xml.serialize.OutputFormat convertFormat
        (org.exolab.adaptx.xslt.OutputFormat oldFormat) 
    {
        
        
        org.apache.xml.serialize.OutputFormat newFormat 
            = new org.apache.xml.serialize.OutputFormat();
            
        
        //-- Doctype
        newFormat.setDoctype(oldFormat.getDoctypePublicId(),
            oldFormat.getDoctypeSystemId());
        
        //-- Encoding (currently not supported by XSL:P)
        //newFormat.setEncoding(oldFormat.getEnconding());
        
        //-- Indenting
        newFormat.setIndenting(oldFormat.getIndent());
        
        //-- Method
        newFormat.setMethod(oldFormat.getMethod());
        
        //-- Version
        newFormat.setVersion(oldFormat.getVersion());
        
        return newFormat;
        
    } //-- convertFormat
} //-- DefaultFormatter

/**
 * A class to store ProcessingInstruction Events
**/
class PIEvent extends Event {
    
    private String target = null;
    private String data   = null;
    
    /**
     * Creates a new PIEvent
    **/
    public PIEvent(String target, String data) {
        this.target = target;
        this.data = data;
    } //-- PIEvent
    
    /**
     * Signals to fire the event to the given Handler
     *
     * @param handler the BaseMarkupSerializer to send the event to
    **/
    public void fire(BaseMarkupSerializer serializer) 
        throws org.xml.sax.SAXException 
    {
        serializer.processingInstruction(target, data);
    } //-- fire
    
} //-- PIEvent

/**
 * A class to store Comment Events
**/
class CommentEvent extends Event {
    
    private String data   = null;
    
    /**
     * Creates a new CommentEvent
    **/
    public CommentEvent(String data) {
        this.data = data;
    } //-- CommentEvent
    
    /**
     * Signals to fire the event to the given Handler
     *
     * @param handler the BaseMarkupSerializer to send the event to
    **/
    public void fire(BaseMarkupSerializer serializer) 
        throws org.xml.sax.SAXException
    {
        try {
            serializer.comment(data);
        }
        catch (java.io.IOException iox) {
            throw new org.xml.sax.SAXException(iox);
        }
    } //-- fire
    
} //-- CommentEvent

/**
 * A class to store Namespace prefix mapping events
 */
class NamespaceEvent extends Event {
    
    private String prefix   = null;
    private String uri      = null;
    
    /**
     * Creates a new NamespaceEvent
    **/
    public NamespaceEvent(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    } //-- NamespaceEvent
    
    /**
     * Signals to fire the event to the given Handler
     *
     * @param handler the BaseMarkupSerializer to send the event to
    **/
    public void fire(BaseMarkupSerializer serializer) 
        throws org.xml.sax.SAXException
    {
        serializer.startPrefixMapping(prefix, uri);
    } //-- fire
    
} //-- NamespaceEvent


/**
 * A simple Event class for saving a list of events
**/
abstract class Event {
    
    Event next = null;
    
    /**
     * Signals to fire the event to the given Handler
     *
     * @param handler the BaseMarkupSerializer to send the event to
    **/
    public abstract void fire(BaseMarkupSerializer serializer) 
        throws org.xml.sax.SAXException;

    /**
     * Append the given event to the end of the list
    **/
    public void append(Event event) {
        
        if (next == null) next = event;
        else next.append(event);
    }
} //-- Event
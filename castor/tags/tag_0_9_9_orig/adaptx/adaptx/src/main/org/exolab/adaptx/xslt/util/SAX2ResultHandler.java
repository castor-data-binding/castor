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
 
package org.exolab.adaptx.xslt.util;

//-- Java
import java.io.*;

//-- Adaptx
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xslt.OutputFormat;
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xslt.XSLTProcessor;
import org.exolab.adaptx.xslt.XSLTStylesheet;
import org.exolab.adaptx.xslt.handlers.DefaultHandler;
import org.exolab.adaptx.xslt.dom.Attribute;
import org.exolab.adaptx.xslt.dom.Element;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.exolab.adaptx.xml.AttributeListImpl;

//-- SAX
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.SAXException;



/**
 * A SAX 1.0 and 2.0 adapter for the source tree
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SAX2ResultHandler 
    implements ContentHandler, DocumentHandler, LexicalHandler
{
    
    /**
     * default namespace declaration "attribute" name
     */
    private static final String XMLNS_DECL = "xmlns";
    
    /**
     * namespace declaration "attribute" prefix
     */
    private static final String XMLNS_PREFIX = "xmlns:";
    
    /**
     * Flag to indicate a new Namespace scope is needed
     */
    private boolean _createNamespaceScope = true;

    
    /**
     * A flag inidication whether or not the processing
     * is currently inside a CDATA section
     */
    boolean _inCDATASection = false;
    
    Namespaces         _namespaces     = null;
    AttributeListImpl   _currentAtts    = null;
    ResultHandler      _handler        = null;
     
    int depth = 0;
    
    
    /**
     * Creates a new SAX2ResultHandler
     */
    public SAX2ResultHandler(ResultHandler handler) {
        if (handler == null) {
            String err = "The argument 'handler' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _handler = handler;
        _currentAtts = new AttributeListImpl();
        _namespaces  = new Namespaces();
    } //-- SAX2ResultHandler
    
    
    //----------------------------------------------------/
    //- Implementation of ContentHandler/DocumentHandler -/
    //----------------------------------------------------/
    
    /**
     * Signals the start of characters
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
     */
    public void characters(char[] chars, int start, int length) 
        throws org.xml.sax.SAXException
    {
        if (_inCDATASection)
            _handler.cdata(chars, start, length);
        else
            _handler.characters(chars, start, length);
        
    } //-- characters
    
    /**
     * Signals the end of the document
     */
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        _handler.endDocument();
        
    } //-- endDocument
    
    /**
     * <p>DocumentHandler#endElement</p>
     *
     * Signals the end of an element
     *
     * @param name the name of the element
     */
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        _handler.endElement(name);
        
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
        //-- reset namespace scope to parents scope
        _namespaces = _namespaces.getParent();
        
        if ((qName != null) && (qName.length() > 0))
            _handler.endElement(qName);
        else
            _handler.endElement(localName);
            
    } //-- endElement
    
    
    /**
     * Signals to end the namespace prefix mapping
     * 
     * @param prefix the namespace prefix 
     */
    public void endPrefixMapping(String prefix)
        throws SAXException
    { 
        //-- do nothing, already taken care of in 
        //-- endElement
        
    } //-- endPrefixMapping
    
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
        _handler.ignorableWhitespace(chars, start, length);
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
     * Signals that an entity was skipped by the parser
     *
     * @param name the skipped entity's name
     */
    public void skippedEntity(String name)
        throws SAXException
    {
        //-- do nothing
        
    } //-- skippedEntity

    /**
     * Signals the start of a document
    **/
    public void startDocument() 
        throws SAXException
    {
        _handler.startDocument();
    } //-- startDocument
    
    
    /**
     * <p>DocumentHandler#startElement</p>
     *
     * Signals the start of element
     *
     * @param name the name of the element
     * @param atts the AttributeList containing the associated
     * attributes for the element
     */
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException 
    {
        _handler.startElement(name, atts);
        
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
        
        //-- Create a new namespace scope if necessary and
        //-- make sure the flag is reset to true
        if (_createNamespaceScope)
            _namespaces = _namespaces.createNamespaces();
        else
            _createNamespaceScope = true;
            
        
        //-- process any possible namespace attributes
        //-- and any attributes that have a qName
        boolean hasNonQNameAtts = false;
        if ((atts != null) && (atts.getLength() > 0)) {
            //-- look for any potential namespace declarations
            for (int i = 0; i < atts.getLength(); i++) {
                String attName = atts.getQName(i);
                if ((attName != null) && (attName.length() > 0)) {
                    String attValue = atts.getValue(i);
                    _currentAtts.addAttribute(attName, attValue);
                    if (attName.equals(XMLNS_DECL)) {
                        _namespaces.addNamespace("", attValue);
                    }
                    else if (attName.startsWith(XMLNS_PREFIX)) {
                        String prefix = attName.substring(XMLNS_PREFIX.length());
                        _namespaces.addNamespace(prefix, attValue);
                    }
                }
                else hasNonQNameAtts = true;
            }
        }
        
        //-- process any attributes that did not have a qName
        if (hasNonQNameAtts) {
            for (int i = 0; i < atts.getLength(); i++) {
                String attName = atts.getQName(i);
                if ((attName == null) || (attName.length() == 0)) {
                    attName = atts.getLocalName(i);
                    String uri = atts.getURI(i);
                    //-- adjust uri
                    if ((uri != null) && (uri.length() == 0)) uri = null;
                    String prefix = _namespaces.getNamespacePrefix(uri);
                    if ((prefix != null) && (prefix.length() > 0))
                        attName = prefix + ':' + attName;
                    _currentAtts.addAttribute(attName, atts.getValue(i));
                }
            }
        }
        
        
        if ((qName == null) || (qName.length() == 0)) {
            if ((namespaceURI != null) && (namespaceURI.length() > 0)) {
                String prefix = _namespaces.getNamespacePrefix(namespaceURI);
                if ((prefix != null) && (prefix.length() > 0))
                    qName = prefix + ":" + localName;
                else
                    qName = localName;
            }
            else qName = localName;
        }
        
        _handler.startElement(qName, _currentAtts);
        
        //-- clear attributes for next "startElement" usage
        _currentAtts.clear();
        
        
        
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
        if (_createNamespaceScope) {
            _namespaces = _namespaces.createNamespaces();
            _createNamespaceScope = false;
        }
        
        _namespaces.addNamespace(prefix, uri);
        
        //-- add namespace declarations to set of current attributes        
        String attName = null;
        if ((prefix == null)  || (prefix.length() == 0))
            attName = XMLNS_DECL;
        else
            attName = XMLNS_PREFIX + prefix;
            
        _currentAtts.addAttribute(attName, uri);
        
    } //-- startPrefixMapping

    //-------------------------------------/
    //-- Implementation of LexicalHandler -/
    //-------------------------------------/

    /**
     * Signals the start of a comment
     */
    public void comment(char[] ch, int start, int length)
        throws SAXException 
    {
        _handler.comment(new String(ch, start, length));
        
    } //-- comment
    
    /**
     * Signals and end to CDATA section
     */
    public void endCDATA()
        throws SAXException 
    {
        _inCDATASection = false;
    } //-- endCDATA
    
    /**
     * Signals the end of the DTD internal subset
     */
    public void endDTD()
        throws SAXException 
    {
        //-- do nothing
        
    } //-- endDTD
    
    /**
     * Signals the end of an Entity
     */
    public void endEntity(String name)
        throws SAXException 
    {
        //-- do nothing, for now.
    } //-- endEntity
    
    /**
     * Signals the start of a CDATA section
     */
    public void startCDATA()
        throws SAXException 
    {
        _inCDATASection = true;
    } //-- startCDATA
    
    /**
     * Signals the start of the DTD internal subset
     */
    public void startDTD(String name, String publicId, String systemId)
        throws SAXException 
    {
        //-- do nothing
    } //-- startDTD


    /**
     * Signals the start of an Entity
     */
    public void startEntity(String name)
        throws SAXException 
    {
        //-- do nothing, for now
    } //-- startEntity
    
    
} //-- SAXInput    

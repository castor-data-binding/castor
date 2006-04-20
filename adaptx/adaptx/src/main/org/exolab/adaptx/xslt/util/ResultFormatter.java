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

import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xml.AttributeListImpl;
import org.exolab.adaptx.xml.XMLUtil;
import org.exolab.adaptx.util.*;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

/**
 * The ResultFormatter class used by the RuleProcessor to
 * send results to. This class wraps a ResultHandler and
 * does some common processing, such as namespace 
 * management.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class ResultFormatter extends ErrorObserverAdapter {
    
    
    private static final String XMLNS_DECL     = "xmlns";
    private static final String XMLNS_PRE_DECL = "xmlns:";
    
    private static final String ATTR_ERR = 
        "attributes may only be added to the current result element before"+
        " any content has been added.";
        
    /**
     * Flag signaling whether attributes are currently allowed
    **/
    private boolean _allowAtts = false;
    
    /**
     * AttributeList
    **/
    private AttributeListImpl _atts = null;
    
    /**
     * The "un-flushed" element name
    **/
    private String _elementName = null;
    
    /**
     * The namespace for the "un-flushed" element
    **/
    private String _namespace = null;
    
    /**
     * The ResultHandler to send results to
    **/
    private ResultHandler _handler = null;
    
    
    /**
     * The namespaces which are in scope at this time
    **/
    Namespaces _namespaces = null;
    
    /**
     * Creates a new ResultFormatter using the given ResultHandler
     *
     * @param handler the ResultHandler to send data to
    **/
    public ResultFormatter(ResultHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null.");
        }
        _handler = handler;
        _atts = new AttributeListImpl();
        _namespaces = new Namespaces();
        
    } //-- ResultFormatter
    
    
    
    /**
     * Adds the given attribute to the result tree if attributes
     * are allowed at the given time
     *
     * @param name the name of the attribute
     * @param value the value of the attribute
    **/
    public void attribute(String name, String value, String nsURI) {
        
        //-- check for namespace
        if (name.equals(XMLNS_DECL)) {
            _namespaces.defaultNS = value;
            _atts.addAttribute(name, value);
        }
        else if (name.startsWith(XMLNS_PRE_DECL)) {
            String prefix = name.substring(6);
            _namespaces.addMapping(prefix, value);
            _atts.addAttribute(name, value);
        }
        else {
            if (!_allowAtts) {
                String warning = ATTR_ERR + "\n   -- attr: " + name
                    + "=\"" + value + "\"";
                        
                //warning += "\n " + _debugString;
                receiveError(warning);
            }
            else {
                // handle namespaces
                int idx = name.indexOf(':');
                if (idx >= 0) {
                    String prefix = name.substring(0, idx);
                    String currentNS = _namespaces.getNamespace(prefix);
                    if (currentNS == null) {
                        if (nsURI != null) {
                            //-- declare namespace
                            declareNamespace(prefix, nsURI);
                        }
                        else {
                            //-- both currentNS and nsURI are null,
                            //-- report error
                            receiveError("undeclared namespace prefix: " 
                                + prefix);
                        }
                    }
                    else {
                        //-- reassign prefix
                        if ((nsURI != null) && (!currentNS.equals(nsURI))) {
                            declareNamespace(prefix, nsURI);
                        }
                    }
                }
                else if (nsURI != null) {
                    String prefix = _namespaces.getPrefix(nsURI);
                    if ((prefix != null) && (prefix.length() > 0)) {
                        name = prefix + ':' + _elementName;
                    }
                    else {
                        //-- attributes do not use the default namespace
                        //-- and must have a defined prefix if they belong
                        //-- to a specific namespace
                        receiveError("undeclared namespace: " 
                            + nsURI);
                    }
                }
                _atts.addAttribute(name, value);
            }
        }
    } //-- attribute
    
    /**
     * Returns true if an attribute can be successfully added
     * to the current node.
     *
     * @return true if an attribute can be successfully added
     * to the current node.
    **/
    public boolean attsAllowed() {
        return _allowAtts;
    } //-- attsAllowed
    
    /**
     * Signals a block of CDATA content.
     *
     * @param data the CDATA section content
    **/
    public void cdata(String data) {
        if ((data == null) || (data.length() == 0)) return;
        char[] chars = data.toCharArray();
        cdata(chars, 0, chars.length);
    } //-- cdata
    
    /**
     * Signals a block of CDATA content.
     *
     * @param chars a char[] containing the CDATA section content
     * @param start the start offset into the char[]
     * @param length the number of characters
    **/
    public void cdata(char[] chars, int start, int length) {
        content(); //-- signal valid content
        _handler.cdata(chars, start, length);
    } //-- cdata
    
    /**
     * Signals a block of character content.
     *
     * @param data the character content
    **/
    public void characters(String data) {
        if ((data == null) || (data.length() == 0)) return;
        char[] chars = data.toCharArray();
        characters(chars, 0, chars.length);
    } //-- characters
    
    /**
     * Signals a block of character content.
     *
     * @param chars a char[] containing the characters
     * @param start the starting index into the char[]
     * @param length the number of characters
    **/
    public void characters(char[] chars, int start, int length) {
        //-- if we have non whitespace we need to signal 
        //-- that we have valid content
        //-- Note: This currently breaks <xsl:text>!
        //-- commented out: kvisco 20020207
        //if (!XMLUtil.isWhitespace(chars, start, length)) {
        //
            content();
            try {
                _handler.characters(chars, start, length);
            }
            catch (org.xml.sax.SAXException sx) {
                receiveError(sx);
            }
        //}
    } //-- characters
    
    /**
     * Signals to recieve a comment.
     *
     * @param data, the content of the comment
    **/
    public void comment(String data) {
        content();
        //_debugString = "comment:";
        _handler.comment(data);
    } //-- comment
    
    /**
     * Declares the given namespace, the namespace will
     * be added to the next element.
    **/
    public void declareNamespace(String prefix, String uri) {
        
        if (prefix == null) prefix = "";
        if (uri == null) return;
        
        _namespaces.addMapping(prefix, uri);
        
        StringBuffer attName = new StringBuffer(XMLNS_DECL);
        if (prefix.length() > 0) {
            attName.append(':');
            attName.append(prefix);
        }
        
        _atts.addAttribute(attName.toString(), uri);
        
    } //-- declareNamespace
    
    /**
     * Signals the end of the current element.
     *
     * @param name the name of the element
    **/
    public void endElement(String name, String nsURI) {
        content();
        String prefix = "";
        int idx = name.indexOf(':');
        if (idx > 0) {            
            if (nsURI == null) {
                name = name.substring(idx);
            }
            else {
                //-- choose prefix
                String declaredPrefix = _namespaces.getPrefix(nsURI);
                if ((declaredPrefix != null) && 
                    (!declaredPrefix.equals(prefix)))
                    name = declaredPrefix + ':' + name.substring(idx);
            }
        }
        else if (nsURI != null) {
            //-- handle prefix
            String declaredPrefix = _namespaces.getPrefix(nsURI);
            if ((declaredPrefix != null) && (declaredPrefix.length() > 0))
                name = declaredPrefix + ':' + name;
        }
        removeNamespaces();        
        try {
            _handler.endElement(name);
        }
        catch (org.xml.sax.SAXException sx) {
            receiveError(sx);
        }
    } //-- endElement
    
    /**
     * Signals to recieve an entity reference with the given name.
     *
     * @param name the name of the entity reference
    **/
    public void entityReference(String name) {
        content();
        _handler.entityReference(name);
    } //-- entityReference
    
    /**
     * The processor state doesn't always send data to the Formatter,
     * until necessary. This method will force a "flush" of any saved
     * data.
    **/
    public void flush() {
        content();
        try {
            _handler.endDocument();
        }
        catch (org.xml.sax.SAXException sx) {
            receiveError(sx);
        }
    } //-- flush
    
    /**
     * Returns the ResultHandler being used by this ResultFormatter.
     *
     * @return the ResultHandler being used by this ResultFormatter
    **/
    public ResultHandler getResultHandler() {
        return _handler;
    } //-- getResultHandler
    
    /**
     * Signals the start of ignorable whitespace characters.
     *
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void ignorableWhitespace(char[] chars, int start, int length) 
    {
        try {
            _handler.ignorableWhitespace(chars, start, length);
        }
        catch (org.xml.sax.SAXException sx) {
            receiveError(sx);
        }
    } //-- ignorableWhitespace
    
    
    /**
     * Returns true if the given namespace has been
     * declared.
     *
     * @param namespace the namespace to check the declaration for
     * @return true if the given namespace has been
     * declared
    **/
    public boolean isNamespaceDeclared(String namespace) {
        return (_namespaces.getPrefix(namespace) != null);        
    } // -- isNamespaceDeclared
    
    /**
     * Signals to receive the processing instruction.
     *
     * @param target
     * @param data
    **/
    public void processingInstruction(String target, String data) {
        content();
        try {
            _handler.processingInstruction(target, data);
        }
        catch (org.xml.sax.SAXException sx) {
            receiveError(sx);
        }
    } //-- processingInstruction
        
    /**
     * Signals the start of an element.
     *
     * @param name the name of the element, this must
     * be the local name of the element. Any prefixes
     * will be automatically stripped.
    **/
    public void startElement(String name, String nsURI) {
        content();
        addNamespaces();
        
        //-- strip prefix
        int idx = name.indexOf(':');
        if (idx > 0) {
            name = name.substring(idx);
        }
        _elementName = name;
        _namespace   = nsURI;
        
        _allowAtts = true;
    } //-- startElement
    
    
    /**
     * Signals to receive characters which should not be escaped.
     *
     * @param data the character data to receive
    **/
    public void unescapedCharacters(String data) {
        if (data == null) return;
        if (data.length() == 0) return;
        char[] chars = data.toCharArray();
        unescapedCharacters(chars, 0, chars.length);
    } //-- unescapedCharacters
    
    /**
     * Signals to receive characters which should not be escaped.
     *
     * @param chars the character array containing the characters
     * to receive
     * @param start the index into the character array to start receiving
     * characters at
     * @param length the number of characters to recieve
    **/
    public void unescapedCharacters(char[] chars, int start, int length) {
        //-- if we have non whitespace we need to signal 
        //-- that we have valid content
        if (!XMLUtil.isWhitespace(chars, start, length)) {
            content();
            try {
                _handler.characters(chars, start, length);
            }
            catch (org.xml.sax.SAXException sx) {
                receiveError(sx);
            }
        }
    } //-- unescapedCharacters
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Clears the current list of attributes.
    **/
    private void clearAttributes() {
        _atts.clear();
    } //-- clearAttributes
    
    /**
     * Signals the start of content...so we can do clean up
     * if necessary.
    **/
    private void content() {
        if (_elementName != null) {
            if (_namespace != null) {
                String prefix = _namespaces.getPrefix(_namespace);
                if ((prefix != null) && (prefix.length() > 0))
                    _elementName = prefix + ':' + _elementName;
            }
            try {
                _handler.startElement(_elementName, _atts);
            }
            catch (org.xml.sax.SAXException sx) {
                receiveError(sx);
            }
            _atts.clear();
            _elementName = null;
        }
        
        _allowAtts = false;
    } //-- content()
    
    private void addNamespaces() {
        Namespaces namespaces = new Namespaces();
        namespaces.previous = _namespaces;
        _namespaces = namespaces;
    }
    
    private void removeNamespaces() {
        _namespaces = _namespaces.previous;        
    } //-- removeNamespaces
    
    class Namespaces {
        
        private String     defaultNS    = null;
        NSMapping  nsMapping    = null;
        Namespaces previous     = null;
        
        Namespaces() {
            super();
        }
        
        void addMapping(String prefix, String namespace) {
            
            if (namespace == null) return;
            
            if ((prefix == null) || (prefix.length() == 0))
                defaultNS = namespace;
                
            NSMapping current = new NSMapping();
            current.prefix = prefix;
            current.namespace = namespace;
            if (nsMapping != null) 
                current.next = nsMapping;
            nsMapping = current;
            
        } //-- addMapping
        
        String getNamespace(String prefix) {
            NSMapping current = nsMapping;
            while (current != null) {
                if (prefix.equals(current.prefix))
                return current.namespace;
                current = current.next;
            }
            if (previous != null)
                return previous.getNamespace(prefix);
           return null;
        } //-- getNamespace
        
        String getPrefix(String namespace) {
            
            if (namespace == null) return null;
            
            if (namespace.equals(defaultNS)) return "";
            
            NSMapping current = nsMapping;
            while (current != null) {
                if (namespace.equals(current.namespace))
                return current.prefix;
                current = current.next;
            }
            if (previous != null)
                return previous.getPrefix(namespace);
           return null;
        } //-- getPrefix
        
    } //-- Namespaces
    
    class NSMapping {
        String prefix = null;
        String namespace = null;
        NSMapping next = null;
    }
    
} //-- ResultFormatter

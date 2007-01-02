/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 04/06/2001   Arnaud Blandin      Created
 */
package org.exolab.castor.xml.util;

import java.util.Stack;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.Namespaces;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class is a SAX Content Handler that
 * build an AnyNode from a stream of SAX events (either SAX1 for compatibility or SAX2)
 * @author <a href="blandin@intalio.com>Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-29 09:44:19 -0600 (Sat, 29 Apr 2006) $
 */
public class SAX2ANY implements ContentHandler, DocumentHandler, ErrorHandler {
   /**
    * Prefix used by namespace declaration.
    */
    private final static String XMLNS_PREFIX        = "xmlns";
    private final static int    XMLNS_PREFIX_LENGTH = XMLNS_PREFIX.length() + 1; // prefix + ':'

    /**
     * The starting node.
     */
    private AnyNode _startingNode;

    /**
     * The current AnyNode we are building
     */
    private AnyNode _node;

    /**
     * A stack to store all the nodes we are creating
     */
    private Stack _nodeStack = new Stack();

    /**
     * A stack to store the namespaces declaration
     */
    private Stack _namespaces = new Stack();

    /**
     * A flag indicating if the SAX2 Parser is processing the
     * namespace or not. 'true' will indicate that the code of this
     * Content Handler will have to deal with Namespaces.This is the default
     * value.
     */
    private boolean _processNamespace = true;

    /**
     * A flag that indicates we are in a character section.
     */
    private boolean _character = false;

    /**
     * The namespace context of this handler
     */
    private Namespaces _context;

    private boolean _wsPreserve = false;

    /**
     * Default constructor
     */
    public SAX2ANY() {
        super();
        init();
    }

    /**
     * Constructs a SAX2ANY given a namespace context.
     *
     * @param context the namespace context in which this handler acts.
     * @param wsPreserve if white spaces whould be preserved
     */
    public SAX2ANY(Namespaces context, boolean wsPreserve) {
        _context = context;
        _wsPreserve = wsPreserve;
        init();
    }

    private void init() {
        if (_context == null)
            _context = new Namespaces();
    }

    /**
     * Sets the document locator of the current parsed inputsource
     * @param locator the Locator of the current parsed inputsource
     */
    public void setDocumentLocator(final Locator locator) { }

    //----------------- NOT IMPLEMENTED --------------
    //we don't need to implement these methods since
    //we are only dealing with xml fragments
    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        return;
    }

    public void processingInstruction(String target, String data) throws SAXException {
        return;
    }

    public void skippedEntity(String name) throws SAXException {
        return;
    }
    //-------------------------------------------------

    //--Namespace related (SAX2 Events)
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        AnyNode temp = new AnyNode(AnyNode.NAMESPACE, null, prefix, uri, null);
       _namespaces.push(temp);
       if (_processNamespace) {
           _context = _context.createNamespaces();
           _processNamespace = true;
       }
       _context.addNamespace(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        _context.removeNamespace(prefix);
    }

    //--startElement methods SAX1 and SAX2
    /**
     * Implementation of {@link org.xml.sax.DocumentHandler#startElement}
     */
    public void startElement(String name, AttributeList atts)
           throws SAXException {
        _character = false;
        String qName;
        String value;
        AnyNode tempNode = null;

        //Namespace handling code to be moved once we integrate
        //the new event API
        /////////////////NAMESPACE HANDLING/////////////////////
        _context = _context.createNamespaces();
        String prefix = "";
        String namespaceURI = null;
        int idx = name.indexOf(':');
        if (idx >= 0) {
             prefix = name.substring(0,idx);
        }
        namespaceURI = _context.getNamespaceURI(prefix);
        //--Overhead here since we process attributes twice
        for (int i=0; i<atts.getLength(); ++i) {
            qName = atts.getName(i);
            value = atts.getValue(i);
            String nsPrefix = null;

            if (qName.startsWith(XMLNS_PREFIX)) {
                //handles namespace declaration
                // Extract the prefix if any
                nsPrefix = (qName.equals(XMLNS_PREFIX))?null:qName.substring(XMLNS_PREFIX_LENGTH);
                tempNode = new AnyNode(AnyNode.NAMESPACE, getLocalPart(qName), nsPrefix, value, null);
                _context.addNamespace(nsPrefix, value);
                _namespaces.push(tempNode);
                if (prefix.equals(nsPrefix))
                    namespaceURI = value;
            }
        }
        ////////////////////////END OF NAMESPACE HANDLING///////////////

        createNodeElement(namespaceURI, getLocalPart(name), name);
        while (!_namespaces.empty()) {
           tempNode = (AnyNode)_namespaces.pop();
           _node.addNamespace(tempNode);
        }

        //process attributes
        for (int i=0; i<atts.getLength(); ++i) {

            qName = atts.getName(i);
            value = atts.getValue(i);

            //Namespace handling already done
            if (!qName.startsWith(XMLNS_PREFIX)) {
                tempNode = new AnyNode(AnyNode.ATTRIBUTE, getLocalPart(qName), null, null, value);
                _node.addAttribute(tempNode);
            }
        }
        tempNode = null;
    }

    /**
     * Implementation of {@link org.xml.sax.ContentHandler#startElement}
     */
    public void startElement(String namespaceURI,  String localName,
                            String qName, Attributes atts) throws SAXException {
        AnyNode tempNode;

        //--SAX2 Parser has not processed the namespaces so we need to do it.
        if (_processNamespace) {
            //Namespace handling code to be moved once we integrate
            //the new event API
            /////////////////NAMESPACE HANDLING/////////////////////
            _context = _context.createNamespaces();
            String prefix = "";
            int idx = qName.indexOf(':');
            if (idx >= 0) {
                 prefix = qName.substring(0,idx);
            }
            namespaceURI = _context.getNamespaceURI(prefix);
            //--Overhead here since we process attributes twice
            for (int i=0; i<atts.getLength(); ++i) {
                String attrqName = atts.getQName(i);
                String value = atts.getValue(i);
                String nsPrefix = null;
                //handles namespace declaration
                if (attrqName.startsWith(XMLNS_PREFIX)) {
                    // Extract the prefix if any
                    nsPrefix = (attrqName.equals(XMLNS_PREFIX))?null:attrqName.substring(XMLNS_PREFIX_LENGTH);
                    tempNode = new AnyNode(AnyNode.NAMESPACE, getLocalPart(attrqName), nsPrefix, value, null);
                    _context.addNamespace(nsPrefix, value);
                    _namespaces.push(tempNode);
                    if (prefix.equals(nsPrefix))
                        namespaceURI = value;
                }
            }
            ////////////////////////END OF NAMESPACE HANDLING///////////////
        }

        //create element
        createNodeElement(namespaceURI, localName, qName);

        //process attributes
        for (int i=0; i<atts.getLength(); ++i) {

            String uri       = atts.getURI(i);
            String attqName  = atts.getQName(i);
            String value     = atts.getValue(i);
            String prefix    = null;

            //-- skip namespace declarations? (handled above)
            if (_processNamespace )
                if(attqName.startsWith(XMLNS_PREFIX))
                    continue;

            //--attribute namespace prefix?
            if ((attqName.length() != 0) && (attqName.indexOf(':') != -1 ))
                prefix = attqName.substring(0,attqName.indexOf(':'));

            //--namespace not yet processed?
            if (_processNamespace ) {
                // attribute namespace
                if(prefix!=null)
                    uri = _context.getNamespaceURI(prefix);
            }
            //--add attribute
            tempNode = new AnyNode(AnyNode.ATTRIBUTE, getLocalPart(attqName), prefix, uri, value);
            _node.addAttribute(tempNode);
        }

        //--empty the namespace stack and add
        //--the namespace nodes to the current node.
        while (!_namespaces.empty()) {
            tempNode = (AnyNode)_namespaces.pop();
            _node.addNamespace(tempNode);
        }
        tempNode = null;
    }

    //--endElement methods SAX1 and SAX2
    public void endElement(String name) throws SAXException {
        int idx = name.indexOf(':');
        String prefix = (idx >= 0) ? name.substring(0,idx) : "";
        String namespaceURI = _context.getNamespaceURI(prefix);
        endElement(namespaceURI,getLocalPart(name), name);
        _context = _context.getParent();
    }

    public void endElement(String namespaceURI, String localName, String qName)
           throws SAXException {
        _character = false;
        String name = null;
        //-- if namespace processing is disabled then the localName might be null, in that case
        //-- we use the QName
        if (localName != null && localName.length() > 0) {
            name = localName;
        } else {
            name = getLocalPart(qName);
        }

        //--if it is the starting element just returns
        if (_startingNode.getLocalName().equals(name) && _nodeStack.empty())
           return;

        //--else just add the node we have built to the previous node
        _node = (AnyNode)_nodeStack.pop();

        //-- if the stack is empty, we have a new child for the root node
        //-- or a new sibling for the first child of the root node
        if (_nodeStack.empty()) {
            _startingNode.addChild(_node);
            _node = _startingNode;
        } else {
            AnyNode previousNode = (AnyNode) _nodeStack.peek();
            previousNode.addChild(_node);
            //--the node processing is finished -> come back to the previous node
            _node = previousNode;
         }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        //create a Text Node
        String temp = new String(ch, start, length);
        //skip whitespaces
        if (isWhitespace(temp) && !_wsPreserve && !_character) return;
        AnyNode tempNode = new AnyNode(AnyNode.TEXT, null, null, null, temp);
        _node.addChild(tempNode);
        _character = true;
    }


    /**************************************************************************/
    // implementation of ErrorHandler
    public void warning(SAXParseException e) throws SAXException {
        String err = "SAX2ANY warning\n" + "Line : " + e.getLineNumber() + '\n'
                + "URI : " + e.getSystemId() + '\n' + e.getMessage();
        throw new SAXException(err, e);
    } // warning

    public void error(SAXParseException e) throws SAXException {
        String err = "SAX2ANY Error \n" + "Line : " + e.getLineNumber() + '\n'
                + "URI : " + e.getSystemId() + '\n' + e.getMessage();
        throw new SAXException(err, e);
    } // error

    public void fatalError(SAXParseException e) throws SAXException {
        String err = "SAX2ANY Fatal Error \n" + "Line : " + e.getLineNumber()
                + '\n' + "URI : " + e.getSystemId() + '\n' + e.getMessage();
        throw new SAXException(err, e);
    } //fatalError
    /*************************************************************************/

    //Utility methods
    public AnyNode getStartingNode() {
        return _startingNode;
    }

    /**
     * Get the namespace context of this SAX2ANY handler. If the SAX2ANY handler
     * is called during the processing of an XML document, it may happen that
     * the XML fragment parsed by the SAX2ANY handler contains references to
     * namespaces declared in the given context.
     *
     * @return the namespace context to interact with while parsing an XML
     *         fragment with the SAX2ANY handler
     */
    public Namespaces getNamespaceContext() {
        return _context;
    }

    /**
     * Set the namespace context in which this handler acts.
     * If this handler is called during the processing of an XML document, it
     * may happen that the XML fragment parsed by the SAX2ANY handler contains
     * references to namespaces declared in the given context.
     *
     * @param context the namespace context to interact with while parsing an
     * XML fragment with the SAX2ANY handler.
     */
    public void setNamespaceContext(Namespaces context) {
        _context = context;
    }

    /**
     * Checks the given String to determine if it only
     * contains whitespace.
     *
     * @param sb the String to check
     * @return true if the only whitespace characters were
     * found in the given StringBuffer
     */
    private boolean isWhitespace(String string) {
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            switch (ch) {
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    } //-- isWhitespace

    /**
     * Returns the local part of the given NCName. The local part is anything
     * following the namespace prefix. If there is no namespace prefix
     * the returned name will be the same as the given name.
     * @return the local part of the given NCName.
     */
    private String getLocalPart(String ncName) {
        int idx = ncName.indexOf(':');
        if (idx >= 0) return ncName.substring(idx+1);
        return ncName;
    } //-- getLocalPart

    private void createNodeElement(String namespaceURI, String localName,
                                   String qName) {

        String prefix = null;
        //retrieves the prefix if any
        if (namespaceURI != null) {
            prefix = _context.getNamespacePrefix(namespaceURI);
        }
        else if (qName != null) {
            if ((qName.length() != 0) && (qName.indexOf(':') != -1 ))
                prefix = qName.substring(0,qName.indexOf(':'));
        }

        String name = null;
        //-- if namespace processing is disabled then the localName might be null, in that case
        //-- we use the localpart of the QName
        if (localName != null && localName.length() > 0)
            name = localName;
        else
             name = getLocalPart(qName);

        //creates the starting ELEMENT node
        //or a default ELEMENT node
        if ( (_nodeStack.empty()) && (_startingNode == null)) {
           _startingNode = new AnyNode(AnyNode.ELEMENT, name, prefix, namespaceURI, null);
           _node = _startingNode;
        } else {
          _node = new AnyNode(AnyNode.ELEMENT, name, prefix, namespaceURI, null);
          //push the node in the stack
          _nodeStack.push(_node);
        }
    }

}

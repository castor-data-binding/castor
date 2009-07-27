/*
 * Copyright 2007 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: XMLBuilder.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.castor.xmlctf.xmldiff.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.castor.xmlctf.xmldiff.xml.nodes.Attribute;
import org.castor.xmlctf.xmldiff.xml.nodes.Element;
import org.castor.xmlctf.xmldiff.xml.nodes.Namespace;
import org.castor.xmlctf.xmldiff.xml.nodes.ParentNode;
import org.castor.xmlctf.xmldiff.xml.nodes.ProcessingInstruction;
import org.castor.xmlctf.xmldiff.xml.nodes.Root;
import org.castor.xmlctf.xmldiff.xml.nodes.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A ContentHandler implementation that builds a tree of XMLNodes.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class XMLContentHandler implements ContentHandler {

    /** The current node stack. */
    private final Stack         _nodeStack            = new Stack();
    /** Root BaseNode, the BaseNode being created.. */
    private final Root          _root                 = new Root();

    /** SAX document Locator, set by a SAX Parser. */
    private Locator             _locator              = null;
    /** The current node to which we are adding content. */
    private ParentNode          _currentNode          = null;
    /** Keeps track of all URL mapping prefixes currently defined.  */
    private Map                 _prefixes             = new HashMap();

    /**
     * Creates a new XMLBuilder.
     */
    public XMLContentHandler() {
        _nodeStack.push(_root);
        _currentNode = _root;
    }

    /**
     * Creates a new text node from incoming characters.
     *
     * @param chars The character array containing the XML content
     * @param start First index of character for our new Text node
     * @param length count of characters for our Text node.
     * @throws org.xml.sax.SAXException never
     */
    public void characters(final char[] chars, final int start, final int length) throws org.xml.sax.SAXException {
        _currentNode.addChild(new Text(new String(chars, start, length)));
    }

    /**
     * Signals the end of the document.
     * @throws org.xml.sax.SAXException never
     */
    public void endDocument() throws org.xml.sax.SAXException {
        // Nothing to do
    }

    /**
     * Signals the end of an Element.
     * @param uri The namespace URI
     * @param name the local name of the element.
     * @param qName the qualified naem of the element
     * @throws org.xml.sax.SAXException if we have a mismatched end element tag
     */
    public void endElement(final String uri, final String name, final String qName)
                                                   throws org.xml.sax.SAXException {
        final String localName = name;

        final int idx = qName.indexOf(':');
        final String prefix = (idx >= 0) ? qName.substring(0, idx) : "";

        // Check the prefix to make sure it is appropriate
        String uriOfPrefix = _currentNode.getNamespaceURI(prefix);
        String uriOfElement = _currentNode.getNamespaceURI();
        if ((uriOfPrefix == null ^ uriOfElement == null)
                || (uriOfPrefix != null && !uriOfPrefix.equals(uriOfElement))) {
            throw new org.xml.sax.SAXException("In Element " + qName + ", URI of prefix " + uriOfPrefix +
                                               " does not match URI of Element " + uriOfElement);
        }

        String cName = _currentNode.getLocalName();
        if (!cName.equals(localName)) {
            String err = "Element end tag mismatch:  expecting </" + cName
                         + "> but recieved </" + localName + ">";
            throw new SAXException(err);
        }

        _nodeStack.pop();
        _currentNode = (ParentNode) _nodeStack.peek();
    }

    /**
     * Signals the end of prefix mapping.
     * @param prefix The namespace prefix mapping that is ending
     * @throws org.xml.sax.SAXException never
     */
    public void endPrefixMapping(final String prefix) throws SAXException {
        _prefixes.remove(prefix);
    }

    /**
     * Returns the Root node.
     * @return the root node.
     */
    public Root getRoot() {
        return _root;
    }

    /**
     * Ignores ignorable whitespace.
     *
     * @param chars The character array containing the XML content
     * @param start First index of the ignorable whitespace
     * @param length count of characters for the ignorable whitespace
     * @throws org.xml.sax.SAXException never
     */
    public void ignorableWhitespace(final char[] chars, final int start, final int length)
                                                  throws org.xml.sax.SAXException {
        // Deliberately ignore -- we don't care
    }

    /**
     * Creates a new Processing Instruction node.
     *
     * @param target the target of the processing instruction
     * @param data the content of the processing instruction
     * @throws org.xml.sax.SAXException never
     */
    public void processingInstruction(final String target, final String data)
                                                  throws org.xml.sax.SAXException {
        ProcessingInstruction pi = new ProcessingInstruction(target, data);
        _currentNode.addChild(pi);
    }

    /**
     * Configures the Locator we will use.
     *
     * @param locator the Locator used by this DocumentHandler.
     */
    public void setDocumentLocator(final Locator locator) {
        _locator = locator;
    }

    /**
     * Gives notification about a skipped Entity during XML parsing.
     * @param name the name of the skipped entity.
     */
    public void skippedEntity(final String name) {
        // Nothing to do
    }

    /**
     * Signals the beginning of the document.
     * @throws org.xml.sax.SAXException never
     */
    public void startDocument() throws SAXException {
        // Nothing to do
    }

    /**
     * Signals the beginning of an Element node.
     *
     * @param uri The namespace URI
     * @param name the local name of the element.
     * @param qName the qualified naem of the element
     * @param atts a list of attributes for this Element
     * @throws org.xml.sax.SAXException If we are not given an element name.
     */
    public void startElement(final String uri, final String name, final String qName, final Attributes atts) throws org.xml.sax.SAXException {
        if (qName == null) {
            throw new SAXException("No Element name given");
        }

        final String prefix;
        final String localName;

        int idx = qName.indexOf(':');
        if (idx >= 0) {
            prefix = qName.substring(0, idx);
            localName = qName.substring(idx + 1);
        } else {
            prefix = "";
            localName = qName;
        }

        Element element = new Element(null, localName);

        if (_locator != null) {
            element.setLocation(new Location(_locator));
        }

        _currentNode.addChild(element);

        // Add all current namespaces to this element
        for (Iterator i = _prefixes.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry me = (Map.Entry) i.next();
            element.addNamespace(new Namespace((String)me.getKey(), (String) me.getValue()));
        }

        // Then add all attributes
        if (atts != null && atts.getLength() > 0) {
            for (int i = 0; i < atts.getLength(); i++) {
                String attName = atts.getQName(i);
                String ns = null;
                idx = attName.indexOf(':');
                if (idx > 0) {
                    ns = element.getNamespaceURI(attName.substring(0, idx));
                    attName = attName.substring(idx + 1);
                }
                element.addAttribute(new Attribute(ns, attName, atts.getValue(i)));
            }
        }

        // Set the namespace on this Element, if one is explicit or defaulted
        if (prefix != null && prefix.length() > 0) {
            String namespace = element.getNamespaceURI(prefix);
            element.setNamespace(namespace);
        } else {
            String namespace = element.getNamespaceURI("");
            if (namespace != null) {
                element.setNamespace(namespace);
            }
        }

        _nodeStack.push(element);
        _currentNode = element;
    }

    /**
     * Begins the scope of a prefix-URI Namespace mapping.
     * @param prefix The namespace prefix mapping that is ending
     * @param uri The namespace URI
     * @throws org.xml.sax.SAXException never
     */
    public void startPrefixMapping(final String prefix, final String uri) {
        _prefixes.put(prefix, uri);
    }

}

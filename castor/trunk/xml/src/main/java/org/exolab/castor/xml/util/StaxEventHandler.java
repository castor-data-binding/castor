/*
 * Copyright 2011 Jakub Narloch
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
 */
package org.exolab.castor.xml.util;

import org.castor.core.util.Assert;
import org.exolab.castor.xml.Namespaces;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * A document handler that uses internally a instance of {@link javax.xml.stream.XMLEventWriter} to output the result
 * xml.
 *
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class StaxEventHandler extends DefaultHandler {

    /**
     * Instance of {@link XMLEventFactory} that will be used to create instances of xml events.
     */
    private final XMLEventFactory eventFactory = XMLEventFactory.newInstance();

    /**
     * Instance of {@link XMLEventWriter} to be used to write the marshalled object.
     */
    private final XMLEventWriter xmlEventWriter;

    /**
     * Instance of {@link Namespaces} used for handling the namespace.
     */
    private Namespaces namespaces = new Namespaces();

    /**
     * Flag indicating whether the new namespace scope is required to create.
     */
    private boolean createNamespaceScope = true;

    /**
     * Creates new instance of {@link StaxEventHandler} with given {@link XMLEventWriter}.
     *
     * @param xmlEventWriter the {@link XMLEventWriter} to be used
     *
     * @throws IllegalArgumentException if xmlEventWriter is null
     */
    public StaxEventHandler(XMLEventWriter xmlEventWriter) {
        Assert.paramNotNull(xmlEventWriter, "xmlEventWriter");

        this.xmlEventWriter = xmlEventWriter;
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            // writes the start of document
            xmlEventWriter.add(eventFactory.createStartDocument());
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing document start.", e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            // writes the end of document
            xmlEventWriter.add(eventFactory.createEndDocument());
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing document end.", e);
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (createNamespaceScope) {
            namespaces = namespaces.createNamespaces();
            createNamespaceScope = false;
        }

        namespaces.addNamespace(prefix, uri);
    }

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        try {
            // writes the start of element
            xmlEventWriter.add(eventFactory.createStartElement(new QName(qName),
                    new AttributeIterator(attributes), new NamespaceIterator(namespaces)));
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing element start.", e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            // writes the end of element
            xmlEventWriter.add(eventFactory.createEndElement(new QName(qName), null));
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing element end.", e);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            // writes the characters
            xmlEventWriter.add(eventFactory.createCharacters(new String(ch, start, length)));
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing white spaces.", e);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            // writes the characters
            xmlEventWriter.add(eventFactory.createCharacters(new String(ch, start, length)));
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing characters.", e);
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            // writes the processing instruction
            xmlEventWriter.add(eventFactory.createProcessingInstruction(target, data));
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing processing instruction.", e);
        }
    }

    /**
     * Converts the passed exception into a {@link SAXException} instance with using the provided error message and
     * exception cause.
     *
     * @param msg the error message
     * @param e   the inner cause of newly created exception
     *
     * @throws SAXException the newly created exception instance
     */
    private void convertToSAXException(String msg, XMLStreamException e) throws SAXException {
        throw new SAXException(msg, e);
    }

    /**
     * An attribute iterator that converts the representation of attributes between sax and stax.
     *
     * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
     * @version 1.3.3
     * @since 1.3.3
     */
    private class AttributeIterator implements Iterator {

        /**
         * Represents the list of attributes.
         */
        private final Attributes attributes;

        /**
         * Represents the index that points to current attributes on the list.
         */
        private int index;

        /**
         * Creates new instance of {@link AttributeIterator} class.
         *
         * @param attributes the list of attributes to use
         */
        private AttributeIterator(Attributes attributes) {
            this.attributes = attributes;
        }

        public boolean hasNext() {
            return index < attributes.getLength();
        }

        public Object next() {
            // creates stax attribute instance
            Attribute attribute =
                    eventFactory.createAttribute(attributes.getQName(index), attributes.getValue(index));
            // increments the current index
            index++;
            // returns the instance of created attribute
            return attribute;
        }

        public void remove() {
            throw new UnsupportedOperationException("Method 'remove' is not supported.");
        }
    }

    /**
     * An namespace iterator that converts the representation of namespace between internal Castor representation
     * and stax.
     *
     * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
     * @version 1.3.3
     * @since 1.3.3
     */
    private class NamespaceIterator implements Iterator {

        /**
         * Represents the current namespace context.
         */
        private final Namespaces namespaces;

        /**
         * Represents the current namespace context.
         */
        private final Enumeration namespaceEnumerator;

        /**
         * Indicates whether the default namespace exists.
         */
        private boolean hasDefaultNamespace;

        /**
         * Indicates whether the default namespace has been written.
         */
        private boolean defaultNamespaceWritten;

        /**
         * Creates new instance of {@link AttributeIterator} class.
         *
         * @param namespaces the list of attributes to use
         */
        private NamespaceIterator(Namespaces namespaces) {
            this.namespaces = namespaces;
            this.namespaceEnumerator = namespaces.getLocalNamespacePrefixes();

            // retrieves the default namespace
            String defaultNamespace = namespaces.getNamespaceURI("");
            if (defaultNamespace != null && defaultNamespace.length() > 0) {
                hasDefaultNamespace = true;
            }
        }

        public boolean hasNext() {
            return hasDefaultNamespace && !defaultNamespaceWritten || namespaceEnumerator.hasMoreElements();
        }

        public Object next() {
            Namespace namespace;

            // creates namespace instance
            if(hasDefaultNamespace && !defaultNamespaceWritten) {

                // creates a default namespace instance
                namespace = eventFactory.createNamespace(namespaces.getNamespaceURI(""));
                defaultNamespaceWritten = true;
            } else {

                // creates a namespace instance
                String prefix = (String) namespaceEnumerator.nextElement();
                namespace = eventFactory.createNamespace(prefix, namespaces.getNamespaceURI(prefix));
            }

            // returns the instance of created namespace
            return namespace;
        }

        public void remove() {
            throw new UnsupportedOperationException("Method 'remove' is not supported.");
        }
    }
}

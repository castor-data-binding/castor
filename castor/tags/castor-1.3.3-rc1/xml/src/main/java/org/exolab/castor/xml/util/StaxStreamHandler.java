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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Enumeration;

/**
 * A document handler that uses internally a instance of {@link XMLStreamWriter} to output the result xml.
 *
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class StaxStreamHandler extends DefaultHandler {

    /**
     * Represents the instance of {@link XMLStreamWriter} class, that will be used to output the marshalled object.
     */
    private final XMLStreamWriter xmlStreamWriter;

    /**
     * Instance of {@link org.exolab.castor.xml.Namespaces} used for handling the namespace.
     */
    private Namespaces namespaces = new Namespaces();

    /**
     * Flag indicating whether the new namespace scope is required to create.
     */
    private boolean createNamespaceScope = true;

    /**
     * Creates new instance of {@link StaxStreamHandler} class.
     *
     * @param xmlStreamWriter the instance of {@link XMLStreamWriter} to use
     *
     * @throws IllegalArgumentException if xmlStreamWriter is null
     */
    public StaxStreamHandler(XMLStreamWriter xmlStreamWriter) {
        Assert.paramNotNull(xmlStreamWriter, "xmlStreamWriter");

        this.xmlStreamWriter = xmlStreamWriter;
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            xmlStreamWriter.writeStartDocument();
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the document start.", e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the document end.", e);
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
            // writes the element start
            xmlStreamWriter.writeStartElement(qName);

            // iterates through all attributes and writes them
            for (int i = 0; i < attributes.getLength(); i++) {
                xmlStreamWriter.writeAttribute(attributes.getQName(i), attributes.getValue(i));
            }

            // retrieves the default namespace
            String defaultNamespace = namespaces.getNamespaceURI("");
            if(defaultNamespace != null && defaultNamespace.length() > 0) {
                xmlStreamWriter.setDefaultNamespace(defaultNamespace);
            }

            // iterates over all namespaces declared in current scope
            Enumeration enumeration = namespaces.getLocalNamespacePrefixes();
            String prefix;
            while(enumeration.hasMoreElements()) {
                prefix = (String)enumeration.nextElement();
                xmlStreamWriter.writeNamespace(prefix,
                        namespaces.getNamespaceURI(prefix));
            }
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the element start.", e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            // writes the element end
            xmlStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the element end.", e);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            // writes the characters to output xml
            xmlStreamWriter.writeCharacters(ch, start, length);
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the white spaces.", e);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            // writes the characters to output xml
            xmlStreamWriter.writeCharacters(ch, start, length);
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the characters.", e);
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            // writes the processing instruction
            xmlStreamWriter.writeProcessingInstruction(target, data);
        } catch (XMLStreamException e) {
            convertToSAXException("Error occurred when writing the processing instruction.", e);
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
}

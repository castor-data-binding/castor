/*
 * Copyright 2007 Edward Kuns
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.xmlctf.xmldiff.xml;

import static org.junit.Assert.*;

import org.castor.xmlctf.xmldiff.xml.nodes.Element;
import org.castor.xmlctf.xmldiff.xml.nodes.Root;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLContentHandlerTest {

    private XMLContentHandler handler;

    @Before
    public void setUp() {
        handler = new XMLContentHandler();
    }

    @Test
    public void should_CreateXMLContentHandler() {
        assertNotNull(handler);
    }

    @Test
    public void should_HandleCharacters() throws SAXException {
        String text = "Hello World";
        char[] chars = text.toCharArray();
        handler.characters(chars, 0, chars.length);
    }

    @Test
    public void should_HandleEmptyCharacters() throws SAXException {
        char[] chars = new char[0];
        handler.characters(chars, 0, 0);
    }

    @Test
    public void should_HandleCharactersWithOffset() throws SAXException {
        String fullText = "Hello World";
        char[] chars = fullText.toCharArray();
        handler.characters(chars, 6, 5); // "World"
    }

    @Test
    public void should_HandleCharactersWithSpecialCharacters() throws SAXException {
        String text = "Hello<>&\"'";
        char[] chars = text.toCharArray();
        handler.characters(chars, 0, chars.length);
    }

    @Test
    public void should_HandleCharactersWithNumbers() throws SAXException {
        String text = "12345";
        char[] chars = text.toCharArray();
        handler.characters(chars, 0, chars.length);
    }

    @Test
    public void should_HandleStartElement() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "element", "element", attrs);
    }

    @Test
    public void should_HandleStartElementWithNamespace() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "test", "ex:test", attrs);
    }

    @Test
    public void should_HandleStartElementWithAttributes() throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://example.com", "id", "id", "CDATA", "123");
        handler.startElement("http://example.com", "element", "element", attrs);
    }

    @Test
    public void should_HandleStartElementWithMultipleAttributes() throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://example.com", "id", "id", "CDATA", "123");
        attrs.addAttribute("http://example.com", "name", "name", "CDATA", "test");
        handler.startElement("http://example.com", "element", "element", attrs);
    }

    @Test
    public void should_HandleEndElement() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");
    }

    @Test
    public void should_HandleNestedElements() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "parent", "parent", attrs);
        handler.startElement("http://example.com", "child", "child", attrs);
        handler.endElement("http://example.com", "child", "child");
        handler.endElement("http://example.com", "parent", "parent");
    }

    @Test
    public void should_HandleProcessingInstruction() throws SAXException {
        handler.processingInstruction("xml-stylesheet", "href=\"style.css\"");
    }

    @Test
    public void should_HandleMultipleProcessingInstructions() throws SAXException {
        handler.processingInstruction("xml-stylesheet", "href=\"style1.css\"");
        handler.processingInstruction("xml-stylesheet", "href=\"style2.css\"");
    }

    @Test
    public void should_HandleStartPrefixMapping() throws SAXException {
        handler.startPrefixMapping("test", "http://test.com");
    }

    @Test
    public void should_HandleMultiplePrefixMappings() throws SAXException {
        handler.startPrefixMapping("ns1", "http://ns1.com");
        handler.startPrefixMapping("ns2", "http://ns2.com");
    }

    @Test
    public void should_HandleEndPrefixMapping() throws SAXException {
        handler.startPrefixMapping("test", "http://test.com");
        handler.endPrefixMapping("test");
    }

    @Test
    public void should_HandleStartDocument() throws SAXException {
        handler.startDocument();
    }

    @Test
    public void should_HandleEndDocument() throws SAXException {
        handler.startDocument();
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "root", "root", attrs);
        handler.endElement("http://example.com", "root", "root");
        handler.endDocument();
    }

    @Test
    public void should_HandleSetDocumentLocator() {
        MockLocator locator = new MockLocator(1, 0);
        handler.setDocumentLocator(locator);
    }

    @Test
    public void should_HandleIgnorableWhitespace() throws SAXException {
        String whitespace = "   ";
        char[] chars = whitespace.toCharArray();
        handler.ignorableWhitespace(chars, 0, chars.length);
    }

    @Test
    public void should_HandleSkippedEntity() throws SAXException {
        handler.skippedEntity("entity");
    }

    @Test
    public void should_HandleCompleteDocument() throws SAXException {
        handler.startDocument();

        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "root", "root", attrs);

        String text = "content";
        char[] chars = text.toCharArray();
        handler.characters(chars, 0, chars.length);

        handler.endElement("http://example.com", "root", "root");

        handler.endDocument();
    }

    @Test
    public void should_HandleDocumentWithNamespaces() throws SAXException {
        handler.startDocument();
        handler.startPrefixMapping("", "http://example.com");

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns", "CDATA", "http://example.com");
        handler.startElement("http://example.com", "root", "root", attrs);

        handler.endElement("http://example.com", "root", "root");
        handler.endPrefixMapping("");

        handler.endDocument();
    }

    @Test
    public void should_HandleDocumentWithAttributes() throws SAXException {
        handler.startDocument();

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://example.com", "id", "id", "CDATA", "123");
        attrs.addAttribute("http://example.com", "name", "name", "CDATA", "test");

        handler.startElement("http://example.com", "element", "element", attrs);

        handler.endElement("http://example.com", "element", "element");

        handler.endDocument();
    }

    @Test
    public void should_HandleElementWithChildren() throws SAXException {
        handler.startDocument();

        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "parent", "parent", attrs);

        handler.startElement("http://example.com", "child1", "child1", attrs);
        String text1 = "text1";
        handler.characters(text1.toCharArray(), 0, text1.length());
        handler.endElement("http://example.com", "child1", "child1");

        handler.startElement("http://example.com", "child2", "child2", attrs);
        String text2 = "text2";
        handler.characters(text2.toCharArray(), 0, text2.length());
        handler.endElement("http://example.com", "child2", "child2");

        handler.endElement("http://example.com", "parent", "parent");

        handler.endDocument();
    }

    @Test
    public void should_HandleMixedContent() throws SAXException {
        handler.startDocument();

        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "root", "root", attrs);

        String text1 = "before";
        handler.characters(text1.toCharArray(), 0, text1.length());

        handler.startElement("http://example.com", "child", "child", attrs);
        String childText = "inside";
        handler.characters(childText.toCharArray(), 0, childText.length());
        handler.endElement("http://example.com", "child", "child");

        String text2 = "after";
        handler.characters(text2.toCharArray(), 0, text2.length());

        handler.endElement("http://example.com", "root", "root");

        handler.endDocument();
    }

    @Test
    public void should_HandleElementWithQualifiedName() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "localname", "prefix:localname", attrs);
        handler.endElement("http://example.com", "localname", "prefix:localname");
    }

    @Test
    public void should_HandleAttributeWithNamespace() throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://other.com", "attr", "attr", "CDATA", "value");
        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");
    }

    @Test(expected = SAXException.class)
    public void should_ThrowException_When_NoElementNameGiven() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "localname", null, attrs);
    }

    @Test(expected = SAXException.class)
    public void should_ThrowException_When_EndElementNameMismatch() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "element1", "element1", attrs);
        handler.endElement("http://example.com", "element2", "element2");
    }

    @Test
    public void should_HandlePrefixMappingChange() throws SAXException {
        handler.startPrefixMapping("test", "http://test.com");

        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");

        handler.startPrefixMapping("test", "http://different.com");
        handler.endPrefixMapping("test");
    }

    @Test
    public void should_HandleElementWithNamespacedAttribute() throws SAXException {
        handler.startPrefixMapping("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi:type", "CDATA", "string");

        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");

        handler.endPrefixMapping("xsi");
    }

    @Test
    public void should_HandleGetRoot() {
        Root root = handler.getRoot();
        assertNotNull(root);
    }

    @Test
    public void should_HandleComplexNamespaceScenario() throws SAXException {
        handler.startDocument();
        handler.startPrefixMapping("ns1", "http://ns1.com");
        handler.startPrefixMapping("ns2", "http://ns2.com");

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://ns1.com", "attr1", "ns1:attr1", "CDATA", "value1");
        attrs.addAttribute("http://ns2.com", "attr2", "ns2:attr2", "CDATA", "value2");

        handler.startElement("http://ns1.com", "element", "ns1:element", attrs);

        handler.startElement("http://ns2.com", "child", "ns2:child", new AttributesImpl());
        String text = "nested content";
        handler.characters(text.toCharArray(), 0, text.length());
        handler.endElement("http://ns2.com", "child", "ns2:child");

        handler.endElement("http://ns1.com", "element", "ns1:element");

        handler.endPrefixMapping("ns2");
        handler.endPrefixMapping("ns1");
        handler.endDocument();
    }

    @Test
    public void should_HandleAttributeWithPrefixNoNamespace() throws SAXException {
        handler.startPrefixMapping("custom", "http://custom.com");

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://custom.com", "attr", "custom:attr", "CDATA", "value");

        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");

        handler.endPrefixMapping("custom");
    }

    @Test
    public void should_HandleEmptyPrefixMapping() throws SAXException {
        handler.startPrefixMapping("", "http://default.com");

        Attributes attrs = new AttributesImpl();
        handler.startElement("http://default.com", "element", "element", attrs);
        handler.endElement("http://default.com", "element", "element");

        handler.endPrefixMapping("");
    }

    @Test
    public void should_HandleCharactersOnRoot() throws SAXException {
        String text = "root level text";
        handler.characters(text.toCharArray(), 0, text.length());

        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");
    }

    @Test
    public void should_HandleProcessingInstructionWithEmptyData() throws SAXException {
        handler.processingInstruction("target", "");
    }

    @Test
    public void should_HandleProcessingInstructionWithNullData() throws SAXException {
        handler.processingInstruction("target", null);
    }

    @Test
    public void should_HandleMultipleAttributesWithSameNamespace() throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://example.com", "attr1", "attr1", "CDATA", "value1");
        attrs.addAttribute("http://example.com", "attr2", "attr2", "CDATA", "value2");
        attrs.addAttribute("http://example.com", "attr3", "attr3", "CDATA", "value3");

        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");
    }

    @Test
    public void should_HandleElementNameWithColonButNoPrefix() throws SAXException {
        Attributes attrs = new AttributesImpl();
        handler.startElement("http://example.com", "localname", "localname", attrs);
        handler.endElement("http://example.com", "localname", "localname");
    }

    @Test
    public void should_HandleAttributeNameWithColonPrefix() throws SAXException {
        handler.startPrefixMapping("xml", "http://www.w3.org/XML/1998/namespace");

        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("http://www.w3.org/XML/1998/namespace", "lang", "xml:lang", "CDATA", "en");

        handler.startElement("http://example.com", "element", "element", attrs);
        handler.endElement("http://example.com", "element", "element");

        handler.endPrefixMapping("xml");
    }

    // Mock Locator for testing
    private static class MockLocator implements org.xml.sax.Locator {
        private int lineNumber;
        private int columnNumber;

        public MockLocator(int line, int column) {
            this.lineNumber = line;
            this.columnNumber = column;
        }

        public String getPublicId() {
            return null;
        }

        public String getSystemId() {
            return null;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public int getColumnNumber() {
            return columnNumber;
        }
    }

}

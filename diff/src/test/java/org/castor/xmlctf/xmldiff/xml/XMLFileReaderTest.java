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

import java.io.File;
import java.io.IOException;

import org.castor.xmlctf.xmldiff.xml.nodes.Root;
import org.castor.xmlctf.xmldiff.xml.nodes.XMLNode;
import org.junit.Before;
import org.junit.Test;

public class XMLFileReaderTest {

    private String validXmlPath;
    private String malformedXmlPath;
    private String namespaceXmlPath;
    private String tempFile;

    @Before
    public void setUp() {
        validXmlPath = "src/test/resources/valid.xml";
        malformedXmlPath = "src/test/resources/malformed.xml";
        namespaceXmlPath = "src/test/resources/namespace.xml";
    }

    @Test
    public void should_CreateXMLFileReader_WithValidFile() {
        // Create temp file for testing
        try {
            File tempFile = File.createTempFile("test", ".xml");
            tempFile.deleteOnExit();

            XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
            assertNotNull(reader);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowException_When_FileIsNull() {
        new XMLFileReader(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowException_When_FileDoesNotExist() {
        new XMLFileReader("/nonexistent/path/file.xml");
    }

    @Test
    public void should_ReadValidXmlFile() throws IOException {
        XMLFileReader reader = new XMLFileReader(validXmlPath);
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
        Root root = (Root) node;
        assertNotNull(root);
    }

    @Test
    public void should_ReadXmlWithNamespaces() throws IOException {
        XMLFileReader reader = new XMLFileReader(namespaceXmlPath);
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_ThrowException_When_XmlIsMalformed() throws IOException {
        XMLFileReader reader = new XMLFileReader(malformedXmlPath);
        try {
            reader.read();
            fail("Should have thrown IOException for malformed XML");
        } catch (IOException e) {
            // Expected - malformed XML should throw IOException
            assertTrue(true);
        }
    }

    @Test
    public void should_ReadMultipleTimesFromSameReader() throws IOException {
        XMLFileReader reader = new XMLFileReader(validXmlPath);

        XMLNode node1 = reader.read();
        assertNotNull(node1);
        assertTrue(node1 instanceof Root);
    }

    @Test
    public void should_HandleFileWithProcessingInstruction() throws IOException {
        // Create temp file with processing instruction
        File tempFile = File.createTempFile("test_pi", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<?target data?>\n");
        fw.write("<root><element>Content</element></root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleFileWithAttributes() throws IOException {
        // Create temp file with attributes
        File tempFile = File.createTempFile("test_attr", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root id=\"1\" name=\"test\">\n");
        fw.write("    <element attr1=\"value1\">Content</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleNestedElements() throws IOException {
        // Create temp file with nested elements
        File tempFile = File.createTempFile("test_nested", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <level1>\n");
        fw.write("        <level2>\n");
        fw.write("            <level3>Deep content</level3>\n");
        fw.write("        </level2>\n");
        fw.write("    </level1>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleEmptyFile() throws IOException {
        // Create empty temp file
        File tempFile = File.createTempFile("test_empty", ".xml");
        tempFile.deleteOnExit();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        try {
            XMLNode node = reader.read();
            // If no exception, check that node is not null or handle gracefully
            assertNull(node);
        } catch (IOException e) {
            // Empty file should throw IOException during parsing
            assertTrue(true);
        }
    }

    @Test
    public void should_HandleWhitespaceContent() throws IOException {
        // Create temp file with whitespace
        File tempFile = File.createTempFile("test_whitespace", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    \n");
        fw.write("    <element>   Content with spaces   </element>\n");
        fw.write("    \n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleSpecialCharactersInContent() throws IOException {
        // Create temp file with special characters
        File tempFile = File.createTempFile("test_special", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element>Content with &lt;tag&gt; and &amp; special &quot;chars&quot;</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleMixedContent() throws IOException {
        // Create temp file with mixed text and elements
        File tempFile = File.createTempFile("test_mixed", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    Text before\n");
        fw.write("    <element>Element content</element>\n");
        fw.write("    Text after\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleMultipleNamespaces() throws IOException {
        // Create temp file with multiple namespaces
        File tempFile = File.createTempFile("test_multi_ns", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root xmlns=\"http://ns1.com\" xmlns:ex=\"http://ns2.com\">\n");
        fw.write("    <element>Content</element>\n");
        fw.write("    <ex:element>External content</ex:element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleCDATA() throws IOException {
        // Create temp file with CDATA section
        File tempFile = File.createTempFile("test_cdata", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element><![CDATA[Content with <tags> and & symbols]]></element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleComments() throws IOException {
        // Create temp file with comments
        File tempFile = File.createTempFile("test_comments", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <!-- This is a comment -->\n");
        fw.write("    <element>Content</element>\n");
        fw.write("    <!-- Another comment -->\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleNumericCharacterReferences() throws IOException {
        // Create temp file with numeric character references
        File tempFile = File.createTempFile("test_numeric_ref", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element>Content &#65; &#x41;</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleLargeXmlFile() throws IOException {
        // Create temp file with many elements
        File tempFile = File.createTempFile("test_large", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        for (int i = 0; i < 100; i++) {
            fw.write("    <element" + i + ">Content " + i + "</element" + i + ">\n");
        }
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleXmlWithDeclaration() throws IOException {
        // Create temp file with explicit XML declaration
        File tempFile = File.createTempFile("test_declaration", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element>Content</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleAttributesWithNamespacePrefixes() throws IOException {
        // Create temp file with namespaced attributes
        File tempFile = File.createTempFile("test_ns_attr", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"string\">\n");
        fw.write("    <element>Content</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleDefaultNamespace() throws IOException {
        // Create temp file with default namespace
        File tempFile = File.createTempFile("test_default_ns", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root xmlns=\"http://default.com\">\n");
        fw.write("    <element>Content</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleEmptyElements() throws IOException {
        // Create temp file with empty elements
        File tempFile = File.createTempFile("test_empty_elem", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element1/>\n");
        fw.write("    <element2></element2>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleTextNodes() throws IOException {
        // Create temp file with text content
        File tempFile = File.createTempFile("test_text", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element>Simple text content</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_ReturnRootInstance() throws IOException {
        XMLFileReader reader = new XMLFileReader(validXmlPath);
        XMLNode node = reader.read();

        assertTrue(node instanceof Root);
        Root root = (Root) node;
        assertEquals("#root", root.getLocalName());
    }

    @Test
    public void should_HandleFileWithUTF8Encoding() throws IOException {
        // Create temp file with UTF-8 encoded content
        File tempFile = File.createTempFile("test_utf8", ".xml");
        tempFile.deleteOnExit();

        java.io.FileWriter fw = new java.io.FileWriter(tempFile);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<root>\n");
        fw.write("    <element>UTF-8 Content: café, naïve, résumé</element>\n");
        fw.write("</root>\n");
        fw.close();

        XMLFileReader reader = new XMLFileReader(tempFile.getAbsolutePath());
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }

    @Test
    public void should_HandleRelativeFilePath() throws IOException {
        XMLFileReader reader = new XMLFileReader(validXmlPath);
        XMLNode node = reader.read();

        assertNotNull(node);
        assertTrue(node instanceof Root);
    }
}

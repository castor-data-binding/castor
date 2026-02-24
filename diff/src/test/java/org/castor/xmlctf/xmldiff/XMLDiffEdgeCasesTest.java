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
package org.castor.xmlctf.xmldiff;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

/**
 * Edge case tests for XMLDiff to achieve maximum code coverage.
 */
public class XMLDiffEdgeCasesTest {

    private String testResourcePath;

    @Before
    public void setUp() {
        String basePath = new File("src/test/resources").getAbsolutePath();
        if (!new File(basePath).exists()) {
            basePath = new File("diff/src/test/resources").getAbsolutePath();
        }
        testResourcePath = basePath + File.separator;
    }

    // ===== EDGE CASES FOR COMPARING DOCUMENTS =====

    @Test
    public void should_ReturnPositive_When_Element1HasMoreAttributes() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root attr1=\"val1\" attr2=\"val2\" attr3=\"val3\"></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root attr1=\"val1\"></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find differences in attributes", result > 0);
    }

    @Test
    public void should_ReturnPositive_When_Element1HasDifferentAttributeValue() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root attr=\"value1\"></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root attr=\"value2\"></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find differences in attribute values", result > 0);
    }

    @Test
    public void should_ReturnPositive_When_TextContentDiffers() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><element>text1</element></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><element>text2</element></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find differences in text content", result > 0);
    }

    @Test
    public void should_ReturnPositive_When_ChildrenInDifferentOrder_LooseMode() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><child1/><child2/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><child2/><child1/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        diff.setStrictChildOrder(false);
        int result = diff.compare();
        assertEquals("Should match in loose mode when content is same", 0, result);
    }

    @Test
    public void should_ReturnPositive_When_ChildrenInDifferentOrder_StrictMode() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><child1/><child2/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><child2/><child1/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        diff.setStrictChildOrder(true);
        int result = diff.compare();
        assertTrue("Should find differences in strict mode when order differs", result > 0);
    }

    @Test
    public void should_ReturnPositive_When_MissingRequiredChild() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><child1/><child2/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><child1/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find missing child", result > 0);
    }

    @Test
    public void should_ReturnPositive_When_ExtraChild() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><child1/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><child1/><child2/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find extra child", result > 0);
    }

    @Test
    public void should_ReturnPositive_When_NamespacesAreDifferent() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root xmlns=\"http://ns1.com\"></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root xmlns=\"http://ns2.com\"></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find namespace differences", result > 0);
    }

    @Test
    public void should_ReturnZero_When_NamespacePrefixDifferentButSameUri() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root xmlns:ns1=\"http://example.com\"><ns1:element/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root xmlns:ns2=\"http://example.com\"><ns2:element/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertEquals("Should match when namespace URIs are same", 0, result);
    }

    @Test
    public void should_ReturnPositive_When_ElementTypesDiffer() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><element1/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><element2/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find element name differences", result > 0);
    }

    @Test
    public void should_HandleIgnorableWhitespaceText() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root>\n    <element/>\n</root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><element/></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertEquals("Should ignore whitespace-only text", 0, result);
    }

    @Test
    public void should_CompareAttributeWithSameNamespaceDifferentValue() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"type1\"></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"type2\"></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertTrue("Should find attribute value differences", result > 0);
    }

    @Test
    public void should_CompareMultipleTextNodes() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root>text1<element/>text2</root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root>text1<element/>text2</root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertEquals("Should match with same mixed content", 0, result);
    }

    @Test
    public void should_HandleCompareWithoutPrinting() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><element>content</element></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><element>different</element></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        diff.setPrint(false);
        int result = diff.compare();
        assertTrue("Should find differences even without printing", result > 0);
    }

    @Test
    public void should_CompareEmptyElementsConsistently() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><empty1/><empty2/></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><empty1></empty1><empty2></empty2></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertEquals("Should treat self-closing and empty elements as same", 0, result);
    }

    @Test
    public void should_HandleDeepNestingComparison() throws IOException {
        File tempFile1 = File.createTempFile("test1", ".xml");
        File tempFile2 = File.createTempFile("test2", ".xml");
        tempFile1.deleteOnExit();
        tempFile2.deleteOnExit();

        java.io.FileWriter fw1 = new java.io.FileWriter(tempFile1);
        fw1.write("<?xml version=\"1.0\"?>\n");
        fw1.write("<root><l1><l2><l3><l4>deep</l4></l3></l2></l1></root>\n");
        fw1.close();

        java.io.FileWriter fw2 = new java.io.FileWriter(tempFile2);
        fw2.write("<?xml version=\"1.0\"?>\n");
        fw2.write("<root><l1><l2><l3><l4>deep</l4></l3></l2></l1></root>\n");
        fw2.close();

        XMLDiff diff = new XMLDiff(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath());
        int result = diff.compare();
        assertEquals("Should match deeply nested structures", 0, result);
    }
}

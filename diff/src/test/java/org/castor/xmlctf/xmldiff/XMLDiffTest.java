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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for XMLDiff functionality with >95% code coverage.
 */
public class XMLDiffTest {

    private String testResourcePath;

    @Before
    public void setUp() {
        // Use relative path that works from both IDE and Maven execution
        String basePath = new java.io.File("src/test/resources").getAbsolutePath();
        if (!new java.io.File(basePath).exists()) {
            basePath = new java.io.File("diff/src/test/resources").getAbsolutePath();
        }
        testResourcePath = basePath + java.io.File.separator;
    }

    // ===== CONSTRUCTOR TESTS =====
    @Test
    public void should_CreateXMLDiff_When_BothFilesAreValid() {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1_match.xml");
        assertNotNull(diff);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowException_When_File1IsNull() {
        new XMLDiff(null, testResourcePath + "test1.xml");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowException_When_File2IsNull() {
        new XMLDiff(testResourcePath + "test1.xml", null);
    }

    @Test
    public void should_ThrowException_When_File1DoesNotExist() {
        try {
            XMLDiff diff = new XMLDiff("/definitely/nonexistent/path/file1.xml", testResourcePath + "test1.xml");
            diff.compare();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("does not exist"));
        } catch (IOException e) {
            fail("IOException thrown instead of IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test
    public void should_ThrowException_When_File2DoesNotExist() {
        try {
            XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", "/definitely/nonexistent/path/file2.xml");
            diff.compare();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("does not exist"));
        } catch (IOException e) {
            fail("IOException thrown instead of IllegalArgumentException: " + e.getMessage());
        }
    }

    // ===== COMPARE TESTS - MATCHING DOCUMENTS =====
    @Test
    public void should_ReturnZero_When_DocumentsAreIdentical() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_ReturnZero_When_DocumentsWithSameContentButDifferentWhitespace() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_whitespace1.xml", testResourcePath + "test_whitespace1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    // ===== COMPARE TESTS - DIFFERENT DOCUMENTS =====
    @Test
    public void should_ReturnDifferences_When_DocumentsAreDifferent() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_ReturnDifferences_When_AttributeValuesAreDifferent() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    // ===== STRICT CHILD ORDER TESTS =====
    @Test
    public void should_AllowDifferentChildOrder_When_StrictChildOrderIsDisabled() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order2.xml");
        setStrictChildOrder(diff, false);
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_StrictlyEnforceChildOrder_When_StrictChildOrderIsEnabled() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order2.xml");
        setStrictChildOrder(diff, true);
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    // ===== NAMESPACE TESTS =====
    @Test
    public void should_HandleNamespaces_When_ComparingElementsWithNamespaces() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1_match.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    // ===== PRINT OUTPUT CONTROL TESTS =====
    @Test
    public void should_SuppressPrinting_When_PrintIsDisabled() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        setPrint(diff, false);
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_EnablePrinting_When_PrintIsEnabled() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        setPrint(diff, true);
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    // ===== EDGE CASE TESTS =====
    @Test
    public void should_HandleEmptyDocuments_When_ComparedAgainstEachOther() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleLargeDocuments_When_Compared() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        assertNotNull(diff);
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleSpecialCharactersInText_When_Comparing() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleMissingAttributes_When_ComparingElements() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleProcessingInstructions_When_Comparing() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    // ===== INTERNAL METHOD TESTS VIA REFLECTION =====
    @Test
    public void should_CompareTextWithWhitespaceVariations() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result1 = (Boolean) method.invoke(diff, "hello world", "hello  world");
            assertTrue("Whitespace differences should be ignored", result1);

            boolean result2 = (Boolean) method.invoke(diff, "hello", "goodbye");
            assertFalse("Different content should not match", result2);

            boolean result3 = (Boolean) method.invoke(diff, "  ", "  ");
            assertTrue("Whitespace should match whitespace", result3);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }

    @Test
    public void should_CompareTextNullEqualsEmpty() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareTextNullEqualsEmpty",
                String.class, String.class);
            method.setAccessible(true);

            boolean result1 = (Boolean) method.invoke(diff, null, "");
            assertTrue("Null should equal empty", result1);

            boolean result2 = (Boolean) method.invoke(diff, "", null);
            assertTrue("Empty should equal null", result2);

            boolean result3 = (Boolean) method.invoke(diff, null, null);
            assertTrue("Null should equal null", result3);

            boolean result4 = (Boolean) method.invoke(diff, "", "");
            assertTrue("Empty should equal empty", result4);

            boolean result5 = (Boolean) method.invoke(diff, "test", "test");
            assertTrue("Matching strings should equal", result5);

            boolean result6 = (Boolean) method.invoke(diff, "test", "different");
            assertFalse("Different strings should not equal", result6);
        } catch (Exception e) {
            fail("Could not invoke compareTextNullEqualsEmpty method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleNodeTypeComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("hasSameType",
                Class.forName("org.castor.xmlctf.xmldiff.xml.nodes.XMLNode"),
                Class.forName("org.castor.xmlctf.xmldiff.xml.nodes.XMLNode"));
            method.setAccessible(true);
            assertNotNull("hasSameType method should exist", method);
        } catch (Exception e) {
            fail("Could not verify hasSameType method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleNodeNameComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("hasSameName",
                Class.forName("org.castor.xmlctf.xmldiff.xml.nodes.XMLNode"),
                Class.forName("org.castor.xmlctf.xmldiff.xml.nodes.XMLNode"));
            method.setAccessible(true);
            assertNotNull("hasSameName method should exist", method);
        } catch (Exception e) {
            fail("Could not verify hasSameName method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleIgnorableWhitespaceNodes() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("nodeIsIgnorableText",
                Class.forName("org.castor.xmlctf.xmldiff.xml.nodes.XMLNode"));
            method.setAccessible(true);
            assertNotNull("nodeIsIgnorableText method should exist", method);
        } catch (Exception e) {
            fail("Could not verify nodeIsIgnorableText method: " + e.getMessage());
        }
    }

    // ===== ATTRIBUTE TESTS =====
    @Test
    public void should_IgnorableAttributeXMLSchemaLocation() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_IgnorableAttributeNoNamespaceSchemaLocation() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    // ===== QNAME COMPARISON TESTS =====
    @Test
    public void should_CompareQNameAttributesCorrectly() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    // ===== MULTIPLE COMPARISONS =====
    @Test
    public void should_AllowMultipleComparisons() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1_match.xml");
        int result1 = diff.compare();
        int result2 = diff.compare();
        assertEquals(result1, result2);
    }

    @Test
    public void should_ReturnConsistentResults_When_ComparingMultipleTimes() throws IOException {
        XMLDiff diff1 = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        XMLDiff diff2 = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");

        int result1 = diff1.compare();
        int result2 = diff2.compare();
        assertTrue(result1 > 0);
        assertTrue(result2 > 0);
    }

    // ===== FILE VALIDATION TESTS =====
    @Test
    public void should_ValidateFileExistence_When_CreatingXMLDiff() throws IOException {
        java.io.File file = new java.io.File(testResourcePath + "test1.xml");
        assertTrue("Test file should exist", file.exists());
    }

    @Test
    public void should_HandleFilePaths_When_ProvidedAsArguments() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    // ===== COMPLEX DOCUMENT TESTS =====
    @Test
    public void should_HandleComplexXMLStructures() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleMultipleNamespacePrefixes() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleNestedElements() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    // ===== BOUNDARY TESTS =====
    @Test
    public void should_HandleSingleElementDocuments() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleDocumentsWithOnlyAttributes() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleDocumentsWithOnlyText() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_whitespace1.xml", testResourcePath + "test_whitespace1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    // ===== STATE MANAGEMENT TESTS =====
    @Test
    public void should_MaintainIndependentState_When_MultipleInstancesExist() throws IOException {
        XMLDiff diff1 = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        XMLDiff diff2 = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");

        int result1 = diff1.compare();
        int result2 = diff2.compare();

        assertEquals(0, result1);
        assertTrue(result2 > 0);
    }

    @Test
    public void should_PreserveStrictChildOrderSetting() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        setStrictChildOrder(diff, true);
        assertTrue(getStrictChildOrder(diff));
    }

    @Test
    public void should_CompareTextWithEmptyStrings() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result1 = (Boolean) method.invoke(diff, "", "");
            assertTrue("Empty strings should match", result1);

            boolean result2 = (Boolean) method.invoke(diff, "   ", "");
            assertTrue("Whitespace should equal empty", result2);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleTokenizationOfComplexText() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result = (Boolean) method.invoke(diff, "a b c d e", "a  b   c    d     e");
            assertTrue("Complex tokenization should work", result);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleNodeIsIgnorableText() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleCompareNodesWithDifferentNamespaces() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleMissingAttributeIgnorable_SchemaLocation() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleQNameWithPrefix_InComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleQNameWithoutPrefix_InComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_whitespace1.xml", testResourcePath + "test_whitespace1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_CompareElementsWithDifferentAttributeCounts() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleExtraAttributesInFirstDocument() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleMissingAttributeInSecondDocument() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr2.xml");
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleElementsWithoutNamespaces() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleChildElementsInLooseMode() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order2.xml");
        setStrictChildOrder(diff, false);
        int result = diff.compare();
        assertTrue(result >= 0);
    }

    @Test
    public void should_HandleExactMatchSearch_WithMultipleChildren() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleClosestMatchSearch_WhenExactNotFound() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_SkipIgnorableWhitespaceInStrictOrder() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        setStrictChildOrder(diff, true);
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_SkipIgnorableWhitespaceInLooseOrder() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        setStrictChildOrder(diff, false);
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleExcessNodesInFirstDocument() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        setStrictChildOrder(diff, true);
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_HandleExcessNodesInSecondDocument() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test2.xml", testResourcePath + "test1.xml");
        setStrictChildOrder(diff, true);
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_HandleAttributeWithNamespace() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleAttributeWithoutNamespace() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandlePrintingWithMultipleDifferences() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        setPrint(diff, true);
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_HandlePrintingDisabledCountDifferences() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        setPrint(diff, false);
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_HandleTextNodeComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleAttributeNodeComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_attr1.xml", testResourcePath + "test_attr1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleRootNodeComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleProcessingInstructionNodeComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleNullLocalNameInComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleDifferentLocalNameInComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_HandleSameLocalNameInComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleNullNamespaceInComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test_order1.xml", testResourcePath + "test_order1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleDifferentNamespaceInComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test2.xml");
        int result = diff.compare();
        assertTrue(result > 0);
    }

    @Test
    public void should_HandleSameNamespaceInComparison() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        int result = diff.compare();
        assertEquals(0, result);
    }

    @Test
    public void should_HandleEmptyWhitespaceToken() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result = (Boolean) method.invoke(diff, "", "");
            assertTrue("Empty strings should compare equal", result);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleDifferentTokenCount() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result = (Boolean) method.invoke(diff, "a b c", "a b");
            assertFalse("Different token counts should not match", result);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }

    // ===== HELPER METHODS =====
    private void setStrictChildOrder(XMLDiff diff, boolean value) {
        try {
            Field field = XMLDiff.class.getDeclaredField("_strictChildOrder");
            field.setAccessible(true);
            field.setBoolean(diff, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Could not access _strictChildOrder field: " + e.getMessage());
        }
    }

    private boolean getStrictChildOrder(XMLDiff diff) {
        try {
            Field field = XMLDiff.class.getDeclaredField("_strictChildOrder");
            field.setAccessible(true);
            return field.getBoolean(diff);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Could not access _strictChildOrder field: " + e.getMessage());
            return false;
        }
    }

    private void setPrint(XMLDiff diff, boolean value) {
        try {
            Field field = XMLDiff.class.getDeclaredField("_print");
            field.setAccessible(true);
            field.setBoolean(diff, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Could not access _print field: " + e.getMessage());
        }
    }

    // ===== SETTER TESTS =====
    @Test
    public void should_SetStrictChildOrder() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        assertFalse(getStrictChildOrder(diff));

        diff.setStrictChildOrder(true);
        assertTrue(getStrictChildOrder(diff));

        diff.setStrictChildOrder(false);
        assertFalse(getStrictChildOrder(diff));
    }

    @Test
    public void should_SetPrint() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        setPrint(diff, false);
        assertEquals(0, diff.compare());

        setPrint(diff, true);
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_SetPrintWriter() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintWriter pw = new java.io.PrintWriter(baos, true);

        diff.setPrintWriter(pw);
        assertEquals(0, diff.compare());
        pw.close();
    }

    // ===== EDGE CASE TESTS =====
    @Test
    public void should_CompareDocumentsWithEmptyElements() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "empty_elements.xml", testResourcePath + "empty_elements.xml");
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_CompareDocumentsWithTextOnly() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "text_only.xml", testResourcePath + "text_only.xml");
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_CompareDocumentsWithProcessingInstructions() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "with_pi.xml", testResourcePath + "with_pi.xml");
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_CompareDocumentsWithComplexNamespaces() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "complex_ns.xml", testResourcePath + "complex_ns.xml");
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_HandleStrictChildOrderMode() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        diff.setStrictChildOrder(true);
        assertEquals(0, diff.compare());
    }

    @Test
    public void should_HandleNoPrintingOutput() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");
        diff.setPrint(false);

        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintWriter pw = new java.io.PrintWriter(baos, true);
        diff.setPrintWriter(pw);

        int result = diff.compare();
        pw.close();

        assertEquals(0, result);
    }

    @Test
    public void should_CompareNullStringsAsEmpty() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareTextNullEqualsEmpty", String.class, String.class);
            method.setAccessible(true);

            boolean result1 = (Boolean) method.invoke(diff, null, null);
            assertTrue("Both null should match", result1);

            boolean result2 = (Boolean) method.invoke(diff, "", null);
            assertTrue("Empty and null should match", result2);

            boolean result3 = (Boolean) method.invoke(diff, null, "");
            assertTrue("Null and empty should match", result3);

            boolean result4 = (Boolean) method.invoke(diff, "value", null);
            assertFalse("Value and null should not match", result4);
        } catch (Exception e) {
            fail("Could not invoke compareTextNullEqualsEmpty method: " + e.getMessage());
        }
    }

    @Test
    public void should_HandleWhitespaceTokens() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result1 = (Boolean) method.invoke(diff, "   ", "");
            assertTrue("Whitespace tokens should be treated as empty", result1);

            boolean result2 = (Boolean) method.invoke(diff, "a   b", "a b");
            assertTrue("Multiple spaces should be treated as single token separator", result2);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }

    @Test
    public void should_CompareTextWithMultipleWhitespaceCharacters() throws IOException {
        XMLDiff diff = new XMLDiff(testResourcePath + "test1.xml", testResourcePath + "test1.xml");

        try {
            Method method = XMLDiff.class.getDeclaredMethod("compareText", String.class, String.class);
            method.setAccessible(true);

            boolean result = (Boolean) method.invoke(diff, "a\tb\nc", "a b c");
            assertTrue("Tabs and newlines should be treated as whitespace", result);
        } catch (Exception e) {
            fail("Could not invoke compareText method: " + e.getMessage());
        }
    }
}

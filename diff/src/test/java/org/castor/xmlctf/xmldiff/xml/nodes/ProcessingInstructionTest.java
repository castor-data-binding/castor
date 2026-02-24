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
package org.castor.xmlctf.xmldiff.xml.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProcessingInstructionTest {

    @Test
    public void should_CreateProcessingInstruction_When_TargetAndValueProvided() {
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"style.css\"");
        assertEquals("xml-stylesheet", pi.getLocalName());
        assertEquals("href=\"style.css\"", pi.getStringValue());
        assertEquals(XMLNode.PROCESSING_INSTRUCTION, pi.getNodeType());
        assertNull(pi.getNamespaceURI());
    }

    @Test
    public void should_CreateProcessingInstruction_When_TargetIsNull() {
        ProcessingInstruction pi = new ProcessingInstruction(null, "some value");
        assertNull(pi.getLocalName());
        assertEquals("some value", pi.getStringValue());
    }

    @Test
    public void should_CreateProcessingInstruction_When_ValueIsNull() {
        ProcessingInstruction pi = new ProcessingInstruction("target", null);
        assertEquals("target", pi.getLocalName());
        assertNull(pi.getStringValue());
    }

    @Test
    public void should_CreateProcessingInstruction_When_BothAreNull() {
        ProcessingInstruction pi = new ProcessingInstruction(null, null);
        assertNull(pi.getLocalName());
        assertNull(pi.getStringValue());
    }

    @Test
    public void should_CreateProcessingInstruction_When_TargetIsEmpty() {
        ProcessingInstruction pi = new ProcessingInstruction("", "value");
        assertEquals("", pi.getLocalName());
        assertEquals("value", pi.getStringValue());
    }

    @Test
    public void should_CreateProcessingInstruction_When_ValueIsEmpty() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "");
        assertEquals("target", pi.getLocalName());
        assertEquals("", pi.getStringValue());
    }

    @Test
    public void should_ReturnStringValue_When_GetStringValueCalled() {
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"style.css\" type=\"text/css\"");
        assertEquals("href=\"style.css\" type=\"text/css\"", pi.getStringValue());
    }

    @Test
    public void should_ReturnCorrectNodeType() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "value");
        assertEquals(XMLNode.PROCESSING_INSTRUCTION, pi.getNodeType());
    }

    @Test
    public void should_CreateXmlStylesheetProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"style.css\" type=\"text/css\"");
        assertEquals("xml-stylesheet", pi.getLocalName());
        assertEquals("href=\"style.css\" type=\"text/css\"", pi.getStringValue());
    }

    @Test
    public void should_CreateXmlVersionProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\"");
        assertEquals("xml", pi.getLocalName());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", pi.getStringValue());
    }

    @Test
    public void should_CreateCustomProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("custom-target", "custom-value");
        assertEquals("custom-target", pi.getLocalName());
        assertEquals("custom-value", pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithSpecialCharacters() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "value with <>&\"' characters");
        assertEquals("value with <>&\"' characters", pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithComplexValue() {
        String complexValue = "version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"";
        ProcessingInstruction pi = new ProcessingInstruction("xml", complexValue);
        assertEquals(complexValue, pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithUnicodeCharacters() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "中文值");
        assertEquals("中文值", pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithNumericTarget() {
        ProcessingInstruction pi = new ProcessingInstruction("123", "value");
        assertEquals("123", pi.getLocalName());
    }

    @Test
    public void should_HandleProcessingInstructionWithHyphenatedTarget() {
        ProcessingInstruction pi = new ProcessingInstruction("my-target", "value");
        assertEquals("my-target", pi.getLocalName());
    }

    @Test
    public void should_HandleProcessingInstructionWithColonInTarget() {
        ProcessingInstruction pi = new ProcessingInstruction("ns:target", "value");
        assertEquals("ns:target", pi.getLocalName());
    }

    @Test
    public void should_HandleProcessingInstructionWithLongTarget() {
        String longTarget = "a".repeat(100);
        ProcessingInstruction pi = new ProcessingInstruction(longTarget, "value");
        assertEquals(longTarget, pi.getLocalName());
    }

    @Test
    public void should_HandleProcessingInstructionWithLongValue() {
        String longValue = "a".repeat(10000);
        ProcessingInstruction pi = new ProcessingInstruction("target", longValue);
        assertEquals(longValue, pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithWhitespaceValue() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "   ");
        assertEquals("   ", pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithNewlineInValue() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "line1\nline2");
        assertEquals("line1\nline2", pi.getStringValue());
    }

    @Test
    public void should_HaveNullNamespaceURI() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "value");
        assertNull(pi.getNamespaceURI());
    }

    @Test
    public void should_SetAndGetParent() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "value");
        Element parent = new Element("http://example.com", "element");
        pi.setParent(parent);
        assertEquals(parent, pi.getParentNode());
    }

    @Test
    public void should_SetNamespace() {
        ProcessingInstruction pi = new ProcessingInstruction("target", "value");
        pi.setNamespace("http://example.com");
        // Note: PI should not normally have namespace, but test for completeness
        assertEquals("http://example.com", pi.getNamespaceURI());
    }

    @Test
    public void should_HandleMultipleProcessingInstructions() {
        ProcessingInstruction pi1 = new ProcessingInstruction("xml-stylesheet", "href=\"style1.css\"");
        ProcessingInstruction pi2 = new ProcessingInstruction("xml-stylesheet", "href=\"style2.css\"");
        assertNotEquals(pi1.getStringValue(), pi2.getStringValue());
    }

    @Test
    public void should_GetRootNodeFromProcessingInstruction() {
        Root root = new Root();
        ProcessingInstruction pi = new ProcessingInstruction("target", "value");
        root.addChild(pi);
        assertEquals(root, pi.getRootNode());
    }

    @Test
    public void should_HandleProcessingInstructionWithUrlInValue() {
        String urlValue = "href=\"http://example.com/style.css\"";
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", urlValue);
        assertEquals(urlValue, pi.getStringValue());
    }

    @Test
    public void should_HandlePhpProcessingInstruction() {
        ProcessingInstruction pi = new ProcessingInstruction("php", "echo 'Hello World';");
        assertEquals("php", pi.getLocalName());
        assertEquals("echo 'Hello World';", pi.getStringValue());
    }

    @Test
    public void should_HandleProcessingInstructionWithAttributelikeValue() {
        String value = "attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"";
        ProcessingInstruction pi = new ProcessingInstruction("target", value);
        assertEquals(value, pi.getStringValue());
    }

}

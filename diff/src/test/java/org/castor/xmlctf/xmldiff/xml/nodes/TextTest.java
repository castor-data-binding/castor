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

public class TextTest {

    @Test
    public void should_CreateTextNode_When_ValidValueProvided() {
        Text text = new Text("Hello World");
        assertEquals("Hello World", text.getStringValue());
        assertEquals(XMLNode.TEXT, text.getNodeType());
        assertNull(text.getLocalName());
        assertNull(text.getNamespaceURI());
    }

    @Test
    public void should_CreateTextNode_When_ValueIsNull() {
        Text text = new Text(null);
        assertNull(text.getStringValue());
    }

    @Test
    public void should_CreateTextNode_When_ValueIsEmpty() {
        Text text = new Text("");
        assertEquals("", text.getStringValue());
    }

    @Test
    public void should_CreateTextNode_When_ValueIsWhitespace() {
        Text text = new Text("   ");
        assertEquals("   ", text.getStringValue());
    }

    @Test
    public void should_CreateTextNode_When_ValueIsNewline() {
        Text text = new Text("\n");
        assertEquals("\n", text.getStringValue());
    }

    @Test
    public void should_CreateTextNode_When_ValueIsTab() {
        Text text = new Text("\t");
        assertEquals("\t", text.getStringValue());
    }

    @Test
    public void should_ReturnStringValue_When_GetStringValueCalled() {
        Text text = new Text("test value");
        assertEquals("test value", text.getStringValue());
    }

    @Test
    public void should_SetValue_When_SetValueCalled() {
        Text text = new Text("initial");
        text.setValue("updated");
        assertEquals("updated", text.getStringValue());
    }

    @Test
    public void should_SetValueToNull_When_SetValueCalledWithNull() {
        Text text = new Text("initial");
        text.setValue(null);
        assertNull(text.getStringValue());
    }

    @Test
    public void should_SetValueToEmpty_When_SetValueCalledWithEmpty() {
        Text text = new Text("initial");
        text.setValue("");
        assertEquals("", text.getStringValue());
    }

    @Test
    public void should_HandleLongTextValue() {
        String longValue = "a".repeat(10000);
        Text text = new Text(longValue);
        assertEquals(longValue, text.getStringValue());
    }

    @Test
    public void should_HandleSpecialCharactersInTextValue() {
        String value = "Text with <special> &characters\" 'quotes'";
        Text text = new Text(value);
        assertEquals(value, text.getStringValue());
    }

    @Test
    public void should_HandleUnicodeCharactersInTextValue() {
        String value = "Unicode: 中文 日本語 한국어 العربية";
        Text text = new Text(value);
        assertEquals(value, text.getStringValue());
    }

    @Test
    public void should_HandleMultilineTextValue() {
        String value = "Line 1\nLine 2\nLine 3";
        Text text = new Text(value);
        assertEquals(value, text.getStringValue());
    }

    @Test
    public void should_HandleNumericTextValue() {
        Text text = new Text("12345");
        assertEquals("12345", text.getStringValue());
    }

    @Test
    public void should_HandleFloatingPointTextValue() {
        Text text = new Text("123.456");
        assertEquals("123.456", text.getStringValue());
    }

    @Test
    public void should_HandleJsonTextValue() {
        String json = "{\"key\": \"value\", \"number\": 123}";
        Text text = new Text(json);
        assertEquals(json, text.getStringValue());
    }

    @Test
    public void should_HandleXmlTextValue() {
        String xml = "<element>value</element>";
        Text text = new Text(xml);
        assertEquals(xml, text.getStringValue());
    }

    @Test
    public void should_HandleUrlTextValue() {
        String url = "http://example.com/path?query=value&other=123";
        Text text = new Text(url);
        assertEquals(url, text.getStringValue());
    }

    @Test
    public void should_UpdateValueMultipleTimes() {
        Text text = new Text("initial");
        assertEquals("initial", text.getStringValue());

        text.setValue("second");
        assertEquals("second", text.getStringValue());

        text.setValue("third");
        assertEquals("third", text.getStringValue());
    }

    @Test
    public void should_ReturnCorrectNodeType() {
        Text text = new Text("test");
        assertEquals(XMLNode.TEXT, text.getNodeType());
    }

    @Test
    public void should_HaveNullLocalName() {
        Text text = new Text("test");
        assertNull(text.getLocalName());
    }

    @Test
    public void should_HaveNullNamespaceURI() {
        Text text = new Text("test");
        assertNull(text.getNamespaceURI());
    }

    @Test
    public void should_HandleCDataLikeTextValue() {
        String cdata = "<![CDATA[Special content & characters]]>";
        Text text = new Text(cdata);
        assertEquals(cdata, text.getStringValue());
    }

    @Test
    public void should_HandleScriptLikeContent() {
        String script = "function() { return value; }";
        Text text = new Text(script);
        assertEquals(script, text.getStringValue());
    }

    @Test
    public void should_SetAndGetParent() {
        Text text = new Text("test");
        Element parent = new Element("http://example.com", "element");
        text.setParent(parent);
        assertEquals(parent, text.getParentNode());
    }

    @Test
    public void should_GetRootNodeFromText() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        Text text = new Text("test");

        root.addChild(element);
        element.addChild(text);

        assertEquals(root, text.getRootNode());
    }

    @Test
    public void should_HandleWhitespaceOnlyUpdate() {
        Text text = new Text("initial");
        text.setValue("     ");
        assertEquals("     ", text.getStringValue());
    }

    @Test
    public void should_HandleTextWithCarriageReturn() {
        String value = "Line1\r\nLine2";
        Text text = new Text(value);
        assertEquals(value, text.getStringValue());
    }

}

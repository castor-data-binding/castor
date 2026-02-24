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

public class AttributeTest {

    @Test
    public void should_CreateAttribute_When_AllParametersValid() {
        Attribute attr = new Attribute("http://example.com", "id", "value123");
        assertEquals("http://example.com", attr.getNamespaceURI());
        assertEquals("id", attr.getLocalName());
        assertEquals("value123", attr.getStringValue());
        assertEquals(XMLNode.ATTRIBUTE, attr.getNodeType());
    }

    @Test
    public void should_CreateAttribute_When_NamespaceIsNull() {
        Attribute attr = new Attribute(null, "id", "value123");
        assertNull(attr.getNamespaceURI());
        assertEquals("id", attr.getLocalName());
        assertEquals("value123", attr.getStringValue());
    }

    @Test
    public void should_CreateAttribute_When_ValueIsEmpty() {
        Attribute attr = new Attribute("http://example.com", "id", "");
        assertEquals("", attr.getStringValue());
    }

    @Test
    public void should_CreateAttribute_When_ValueIsNull() {
        Attribute attr = new Attribute("http://example.com", "id", null);
        assertNull(attr.getStringValue());
    }

    @Test
    public void should_ReturnStringValue_When_GetStringValueCalled() {
        Attribute attr = new Attribute("http://example.com", "name", "test-value");
        assertEquals("test-value", attr.getStringValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowException_When_LocalNameIsNull() {
        new Attribute("http://example.com", null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowException_When_LocalNameIsEmpty() {
        new Attribute("http://example.com", "", "value");
    }

    @Test
    public void should_CreateAttribute_With_SpecialCharactersInValue() {
        Attribute attr = new Attribute("http://example.com", "id", "value<>&\"'");
        assertEquals("value<>&\"'", attr.getStringValue());
    }

    @Test
    public void should_CreateAttribute_With_UnicodeValue() {
        Attribute attr = new Attribute("http://example.com", "id", "值");
        assertEquals("值", attr.getStringValue());
    }

    @Test
    public void should_CreateAttribute_With_LongValue() {
        String longValue = "a".repeat(1000);
        Attribute attr = new Attribute("http://example.com", "id", longValue);
        assertEquals(longValue, attr.getStringValue());
    }

    @Test
    public void should_ReturnCorrectNodeType() {
        Attribute attr = new Attribute("http://example.com", "id", "value");
        assertEquals(XMLNode.ATTRIBUTE, attr.getNodeType());
    }

    @Test
    public void should_SetAndGetParent() {
        Attribute attr = new Attribute("http://example.com", "id", "value");
        Element parent = new Element("http://example.com", "element");
        attr.setParent(parent);
        assertEquals(parent, attr.getParentNode());
    }

    @Test
    public void should_SetNamespace() {
        Attribute attr = new Attribute("http://example.com", "id", "value");
        attr.setNamespace("http://newnamespace.com");
        assertEquals("http://newnamespace.com", attr.getNamespaceURI());
    }

    @Test
    public void should_GetNamespaceURI_When_NullNamespaceProvided() {
        Attribute attr = new Attribute(null, "id", "value");
        assertNull(attr.getNamespaceURI());
    }

    @Test
    public void should_HandleMultipleAttributesWithDifferentNamespaces() {
        Attribute attr1 = new Attribute("http://ns1.com", "id", "value1");
        Attribute attr2 = new Attribute("http://ns2.com", "id", "value2");
        assertNotEquals(attr1.getNamespaceURI(), attr2.getNamespaceURI());
    }

    @Test
    public void should_HandleAttributeWithNumericLocalName() {
        Attribute attr = new Attribute("http://example.com", "123", "value");
        assertEquals("123", attr.getLocalName());
    }

    @Test
    public void should_HandleAttributeWithHyphenInLocalName() {
        Attribute attr = new Attribute("http://example.com", "my-attr", "value");
        assertEquals("my-attr", attr.getLocalName());
    }

    @Test
    public void should_HandleAttributeWithColonInLocalName() {
        Attribute attr = new Attribute("http://example.com", "ns:attr", "value");
        assertEquals("ns:attr", attr.getLocalName());
    }

    @Test
    public void should_GetRootNodeFromAttribute() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        Attribute attr = new Attribute("http://example.com", "id", "value");

        root.addChild(element);
        element.addAttribute(attr);

        assertNotNull(attr.getRootNode());
    }
}

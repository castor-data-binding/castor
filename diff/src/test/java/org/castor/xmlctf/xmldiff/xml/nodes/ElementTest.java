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

import java.util.Iterator;

import org.junit.Test;

public class ElementTest {

    @Test
    public void should_CreateElement_When_NamespaceAndLocalNameProvided() {
        Element elem = new Element("http://example.com", "element");
        assertEquals("http://example.com", elem.getNamespaceURI());
        assertEquals("element", elem.getLocalName());
        assertEquals(XMLNode.ELEMENT, elem.getNodeType());
    }

    @Test
    public void should_CreateElement_When_NamespaceIsNull() {
        Element elem = new Element(null, "element");
        assertNull(elem.getNamespaceURI());
        assertEquals("element", elem.getLocalName());
    }

    @Test
    public void should_CreateElement_When_LocalNameIsNull() {
        Element elem = new Element("http://example.com", null);
        assertEquals("http://example.com", elem.getNamespaceURI());
        assertNull(elem.getLocalName());
    }

    @Test
    public void should_CreateElement_When_BothParametersNull() {
        Element elem = new Element(null, null);
        assertNull(elem.getNamespaceURI());
        assertNull(elem.getLocalName());
    }

    @Test
    public void should_AddAttribute_To_Element() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr = new Attribute("http://example.com", "id", "value");
        elem.addAttribute(attr);

        String result = elem.getAttribute("http://example.com", "id");
        assertEquals("value", result);
    }

    @Test
    public void should_GetAttribute_ReturnsNull_When_AttributeNotFound() {
        Element elem = new Element("http://example.com", "element");
        String result = elem.getAttribute("http://example.com", "nonexistent");
        assertNull(result);
    }

    @Test
    public void should_AddMultipleAttributes() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr1 = new Attribute("http://example.com", "id", "value1");
        Attribute attr2 = new Attribute("http://example.com", "name", "value2");
        Attribute attr3 = new Attribute("http://example.com", "type", "value3");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);
        elem.addAttribute(attr3);

        assertEquals("value1", elem.getAttribute("http://example.com", "id"));
        assertEquals("value2", elem.getAttribute("http://example.com", "name"));
        assertEquals("value3", elem.getAttribute("http://example.com", "type"));
    }

    @Test
    public void should_GetAttributeIterator() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr1 = new Attribute("http://example.com", "id", "value1");
        Attribute attr2 = new Attribute("http://example.com", "name", "value2");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);

        Iterator iterator = elem.getAttributeIterator();
        assertTrue(iterator.hasNext());
        Attribute first = (Attribute) iterator.next();
        Attribute second = (Attribute) iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_AddNamespace_To_Element() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns = new Namespace("test", "http://test.com");
        elem.addNamespace(ns);

        String namespaceUri = elem.getNamespaceURI("test");
        assertEquals("http://test.com", namespaceUri);
    }

    @Test
    public void should_GetNamespaceURI_FromPrefix() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns = new Namespace("xsd", "http://www.w3.org/2001/XMLSchema");
        elem.addNamespace(ns);

        String namespaceUri = elem.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceUri);
    }

    @Test
    public void should_GetNamespaceURI_WithDefaultPrefix() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns = new Namespace("", "http://example.com");
        elem.addNamespace(ns);

        String namespaceUri = elem.getNamespaceURI("");
        assertEquals("http://example.com", namespaceUri);
    }

    @Test
    public void should_GetNamespaceURI_WithNullPrefix() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns = new Namespace("", "http://test.com");
        elem.addNamespace(ns);

        String namespaceUri = elem.getNamespaceURI(null);
        assertEquals("http://test.com", namespaceUri);
    }

    @Test
    public void should_ReturnNullForUndefinedNamespacePrefix() {
        Element elem = new Element("http://example.com", "element");
        String namespaceUri = elem.getNamespaceURI("undefined");
        assertNull(namespaceUri);
    }

    @Test
    public void should_GetNamespacePrefix_From_URI() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns = new Namespace("test", "http://test.com");
        elem.addNamespace(ns);

        String prefix = elem.getNamespacePrefix("http://test.com");
        assertEquals("test", prefix);
    }

    @Test
    public void should_GetNamespacePrefix_WithDefaultPrefix() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns = new Namespace("", "http://example.com");
        elem.addNamespace(ns);

        String prefix = elem.getNamespacePrefix("http://example.com");
        assertEquals("", prefix);
    }

    @Test
    public void should_GetNamespacePrefix_ReturnsNull_WhenUriNotFound() {
        Element elem = new Element("http://example.com", "element");
        String prefix = elem.getNamespacePrefix("http://nonexistent.com");
        assertNull(prefix);
    }

    @Test
    public void should_AddChild_To_Element() {
        Element parent = new Element("http://example.com", "parent");
        Element child = new Element("http://example.com", "child");
        parent.addChild(child);

        assertEquals(parent, child.getParentNode());
    }

    @Test
    public void should_GetChildIterator() {
        Element parent = new Element("http://example.com", "parent");
        Element child1 = new Element("http://example.com", "child1");
        Element child2 = new Element("http://example.com", "child2");

        parent.addChild(child1);
        parent.addChild(child2);

        Iterator iterator = parent.getChildIterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void should_GetStringValue_From_TextChildren() {
        Element elem = new Element("http://example.com", "element");
        Text text1 = new Text("Hello ");
        Text text2 = new Text("World");

        elem.addChild(text1);
        elem.addChild(text2);

        assertEquals("Hello World", elem.getStringValue());
    }

    @Test
    public void should_GetStringValue_From_NestedElements() {
        Element parent = new Element("http://example.com", "parent");
        Element child = new Element("http://example.com", "child");
        Text text = new Text("content");

        parent.addChild(child);
        child.addChild(text);

        assertEquals("content", parent.getStringValue());
    }

    @Test
    public void should_SetAndGetLocation() {
        Element elem = new Element("http://example.com", "element");
        org.xml.sax.Locator mockLocator = new org.xml.sax.Locator() {
            public String getPublicId() { return null; }
            public String getSystemId() { return null; }
            public int getLineNumber() { return 1; }
            public int getColumnNumber() { return 10; }
        };
        org.castor.xmlctf.xmldiff.xml.Location loc = new org.castor.xmlctf.xmldiff.xml.Location(mockLocator);
        elem.setLocation(loc);

        assertEquals(loc, elem.getLocation());
    }

    @Test
    public void should_GetLocationReturnsNull_WhenNotSet() {
        Element elem = new Element("http://example.com", "element");
        assertNull(elem.getLocation());
    }

    @Test
    public void should_AddNullAttribute_DoNothing() {
        Element elem = new Element("http://example.com", "element");
        elem.addAttribute(null);

        Iterator iterator = elem.getAttributeIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_AddNullNamespace_DoNothing() {
        Element elem = new Element("http://example.com", "element");
        elem.addNamespace(null);

        String namespaceUri = elem.getNamespaceURI("any");
        assertNull(namespaceUri);
    }

    @Test
    public void should_HandleMultipleNamespaces() {
        Element elem = new Element("http://example.com", "element");
        Namespace ns1 = new Namespace("ns1", "http://ns1.com");
        Namespace ns2 = new Namespace("ns2", "http://ns2.com");
        Namespace ns3 = new Namespace("", "http://example.com");

        elem.addNamespace(ns1);
        elem.addNamespace(ns2);
        elem.addNamespace(ns3);

        assertEquals("http://ns1.com", elem.getNamespaceURI("ns1"));
        assertEquals("http://ns2.com", elem.getNamespaceURI("ns2"));
        assertEquals("http://example.com", elem.getNamespaceURI(""));
    }

    @Test
    public void should_HandleAttributesWithDifferentNamespaces() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr1 = new Attribute("http://ns1.com", "attr1", "value1");
        Attribute attr2 = new Attribute("http://ns2.com", "attr1", "value2");

        elem.addAttribute(attr1);
        elem.addAttribute(attr2);

        assertEquals("value1", elem.getAttribute("http://ns1.com", "attr1"));
        assertEquals("value2", elem.getAttribute("http://ns2.com", "attr1"));
    }

    @Test
    public void should_GetAttribute_WithNullNamespace() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr = new Attribute(null, "id", "value");
        elem.addAttribute(attr);

        String result = elem.getAttribute(null, "id");
        assertEquals("value", result);
    }

    @Test
    public void should_GetAttribute_WithEmptyNamespace() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr = new Attribute("", "id", "value");
        elem.addAttribute(attr);

        String result = elem.getAttribute("", "id");
        assertEquals("value", result);
    }

    @Test
    public void should_SetNamespace() {
        Element elem = new Element("http://example.com", "element");
        elem.setNamespace("http://newnamespace.com");
        assertEquals("http://newnamespace.com", elem.getNamespaceURI());
    }

    @Test
    public void should_GetParentNode() {
        Element parent = new Element("http://example.com", "parent");
        Element child = new Element("http://example.com", "child");
        parent.addChild(child);

        assertEquals(parent, child.getParentNode());
    }

    @Test
    public void should_GetRootNode() {
        Root root = new Root();
        Element elem = new Element("http://example.com", "element");
        root.addChild(elem);

        assertEquals(root, elem.getRootNode());
    }

    @Test
    public void should_HandleComplexHierarchy() {
        Root root = new Root();
        Element level1 = new Element("http://example.com", "level1");
        Element level2 = new Element("http://example.com", "level2");
        Text text = new Text("content");

        root.addChild(level1);
        level1.addChild(level2);
        level2.addChild(text);

        assertEquals(root, level1.getRootNode());
        assertEquals(root, level2.getRootNode());
        assertEquals(root, text.getRootNode());
    }

    @Test
    public void should_HandleLargeNumberOfChildren() {
        Element parent = new Element("http://example.com", "parent");
        for (int i = 0; i < 100; i++) {
            Element child = new Element("http://example.com", "child" + i);
            parent.addChild(child);
        }

        int count = 0;
        Iterator iterator = parent.getChildIterator();
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        assertEquals(100, count);
    }

    @Test
    public void should_HandleEmptyElement() {
        Element elem = new Element("http://example.com", "element");
        assertEquals("", elem.getStringValue());
    }

    @Test
    public void should_ReturnCorrectNodeType() {
        Element elem = new Element("http://example.com", "element");
        assertEquals(XMLNode.ELEMENT, elem.getNodeType());
    }

    @Test
    public void should_HandleElementWithTextAndChildElements() {
        Element parent = new Element("http://example.com", "parent");
        Text text = new Text("text");
        Element child = new Element("http://example.com", "child");
        Text childText = new Text("child content");

        parent.addChild(text);
        parent.addChild(child);
        child.addChild(childText);

        String stringValue = parent.getStringValue();
        assertTrue(stringValue.contains("text"));
        assertTrue(stringValue.contains("child content"));
    }

    @Test
    public void should_HandleAttributeWithoutNamespace() {
        Element elem = new Element("http://example.com", "element");
        Attribute attr = new Attribute(null, "id", "value");
        elem.addAttribute(attr);

        assertEquals("value", elem.getAttribute(null, "id"));
    }

    @Test
    public void should_InheritNamespaceFromParent() {
        Root root = new Root();
        Element parent = new Element("http://example.com", "parent");
        parent.addNamespace(new Namespace("inherited", "http://inherited.com"));
        Element child = new Element("http://example.com", "child");

        root.addChild(parent);
        parent.addChild(child);

        String inheritedUri = child.getNamespaceURI("inherited");
        assertEquals("http://inherited.com", inheritedUri);
    }

    @Test
    public void should_OverrideInheritedNamespace() {
        Element parent = new Element("http://example.com", "parent");
        parent.addNamespace(new Namespace("ns", "http://parent.com"));
        Element child = new Element("http://example.com", "child");
        child.addNamespace(new Namespace("ns", "http://child.com"));

        parent.addChild(child);

        String childUri = child.getNamespaceURI("ns");
        assertEquals("http://child.com", childUri);
    }

}

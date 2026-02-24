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

import org.castor.xmlctf.xmldiff.xml.Location;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Locator;

public class XMLNodeTest {

    private Root root;
    private Element element1;
    private Element element2;
    private Text text1;
    private Attribute attr1;
    private Namespace ns1;
    private ProcessingInstruction pi1;

    @Before
    public void setUp() {
        root = new Root();
        element1 = new Element(null, "element1");
        element2 = new Element(null, "element2");
        text1 = new Text("text content");
        attr1 = new Attribute(null, "attr1", "value1");
        ns1 = new Namespace("prefix", "http://example.com");
        pi1 = new ProcessingInstruction("target", "data");
    }

    @Test
    public void should_GetNodeType_ForRoot() {
        assertEquals(XMLNode.ROOT, root.getNodeType());
    }

    @Test
    public void should_GetNodeType_ForElement() {
        assertEquals(XMLNode.ELEMENT, element1.getNodeType());
    }

    @Test
    public void should_GetNodeType_ForText() {
        assertEquals(XMLNode.TEXT, text1.getNodeType());
    }

    @Test
    public void should_GetNodeType_ForAttribute() {
        assertEquals(XMLNode.ATTRIBUTE, attr1.getNodeType());
    }

    @Test
    public void should_GetNodeType_ForProcessingInstruction() {
        assertEquals(XMLNode.PROCESSING_INSTRUCTION, pi1.getNodeType());
    }

    @Test
    public void should_GetLocalName() {
        assertEquals("element1", element1.getLocalName());
        assertEquals("attr1", attr1.getLocalName());
        assertEquals("prefix", ns1.getPrefix());
    }

    @Test
    public void should_GetLocalName_ForRoot() {
        assertEquals("#root", root.getLocalName());
    }

    @Test
    public void should_GetNamespaceURI() {
        element1.setNamespace("http://ns.com");
        assertEquals("http://ns.com", element1.getNamespaceURI());
    }

    @Test
    public void should_SetNamespace() {
        assertNull(element1.getNamespaceURI());
        element1.setNamespace("http://example.com");
        assertEquals("http://example.com", element1.getNamespaceURI());
    }

    @Test
    public void should_GetParentNode() {
        assertNull(element1.getParentNode());
        root.addChild(element1);
        assertEquals(root, element1.getParentNode());
    }

    @Test
    public void should_GetRootNode() {
        root.addChild(element1);
        element1.addChild(element2);

        assertEquals(root, element1.getRootNode());
        assertEquals(root, element2.getRootNode());
    }

    @Test
    public void should_GetRootNode_ForRootNode() {
        assertEquals(root, root.getRootNode());
    }

    @Test
    public void should_GetStringValue_ForText() {
        String value = text1.getStringValue();
        assertEquals("text content", value);
    }

    @Test
    public void should_GetStringValue_ForAttribute() {
        String value = attr1.getStringValue();
        assertEquals("value1", value);
    }

    @Test
    public void should_GetStringValue_ForElement() {
        root.addChild(element1);
        element1.addChild(text1);

        String value = element1.getStringValue();
        assertEquals("text content", value);
    }

    @Test
    public void should_GetStringValue_ForRoot() {
        root.addChild(element1);
        element1.addChild(text1);

        String value = root.getStringValue();
        assertEquals("text content", value);
    }

    @Test
    public void should_GetStringValue_ForEmptyElement() {
        String value = element1.getStringValue();
        assertEquals("", value);
    }

    @Test
    public void should_GetNamespaceURI_WithPrefix() {
        element1.addNamespace(ns1);
        root.addChild(element1);

        String uri = element1.getNamespaceURI("prefix");
        assertEquals("http://example.com", uri);
    }

    @Test
    public void should_GetNamespaceURI_ForNonExistentPrefix() {
        root.addChild(element1);

        String uri = element1.getNamespaceURI("nonexistent");
        assertNull(uri);
    }

    @Test
    public void should_GetNodeLocation_WithoutLocation() {
        root.addChild(element1);

        String location = element1.getNodeLocation();
        assertNotNull(location);
        assertTrue(location.contains("XPATH"));
    }

    @Test
    public void should_GetNodeLocation_WithLocation() {
        MockLocator locator = new MockLocator(10, 5);
        element1.setLocation(new Location(locator));
        root.addChild(element1);

        String location = element1.getNodeLocation();
        assertNotNull(location);
        assertTrue(location.contains("[10, 5]"));
        assertTrue(location.contains("XPATH"));
    }

    @Test
    public void should_GetXPath_ForElement() {
        root.addChild(element1);

        String xpath = element1.getXPath();
        assertEquals("/element1", xpath);
    }

    @Test
    public void should_GetXPath_ForElementWithPosition() {
        root.addChild(element1);
        root.addChild(element2);

        String xpath1 = element1.getXPath();
        String xpath2 = element2.getXPath();

        assertTrue(xpath1.contains("/element1"));
        assertTrue(xpath2.contains("/element2"));
    }

    @Test
    public void should_GetXPath_ForText() {
        root.addChild(element1);
        element1.addChild(text1);

        String xpath = text1.getXPath();
        assertEquals("/element1/text()", xpath);
    }

    @Test
    public void should_GetXPath_ForAttribute() {
        root.addChild(element1);
        element1.addAttribute(attr1);

        String xpath = attr1.getXPath();
        assertEquals("/element1/@attr1", xpath);
    }

    @Test
    public void should_GetXPath_ForRoot() {
        String xpath = root.getXPath();
        assertEquals("", xpath);
    }

    @Test
    public void should_GetXPath_ForProcessingInstruction() {
        root.addChild(pi1);

        String xpath = pi1.getXPath();
        assertEquals("/pi()", xpath);
    }

    @Test
    public void should_HandleMultipleChildrenWithSameName() {
        Element elem1a = new Element(null, "item");
        Element elem1b = new Element(null, "item");
        Element elem1c = new Element(null, "item");

        root.addChild(elem1a);
        root.addChild(elem1b);
        root.addChild(elem1c);

        String xpath1 = elem1a.getXPath();
        String xpath2 = elem1b.getXPath();
        String xpath3 = elem1c.getXPath();

        assertTrue(xpath1.contains("/item"));
        assertTrue(xpath2.contains("/item[2]"));
        assertTrue(xpath3.contains("/item[3]"));
    }

    @Test
    public void should_GetNodeLocation_ForAttribute() {
        root.addChild(element1);
        element1.addAttribute(attr1);

        String location = attr1.getNodeLocation();
        assertNotNull(location);
        assertTrue(location.contains("XPATH"));
        assertTrue(location.contains("attr1"));
    }

    @Test
    public void should_GetNodeLocation_ForText() {
        root.addChild(element1);
        element1.addChild(text1);

        String location = text1.getNodeLocation();
        assertNotNull(location);
        assertTrue(location.contains("XPATH"));
        assertTrue(location.contains("text()"));
    }

    @Test
    public void should_GetNodeLocation_ForRoot() {
        String location = root.getNodeLocation();
        assertNotNull(location);
        assertTrue(location.contains("XPATH"));
    }

    @Test
    public void should_GetNodeLocation_ForProcessingInstruction() {
        root.addChild(pi1);

        String location = pi1.getNodeLocation();
        assertNotNull(location);
        assertTrue(location.contains("XPATH"));
    }

    @Test
    public void should_GetStringValue_ForNamespace() {
        String value = ns1.getNamespaceUri();
        assertEquals("http://example.com", value);
    }

    @Test
    public void should_HandleDeepNesting() {
        Element parent = new Element(null, "parent");
        Element child = new Element(null, "child");
        Element grandchild = new Element(null, "grandchild");
        Text text = new Text("deep content");

        root.addChild(parent);
        parent.addChild(child);
        child.addChild(grandchild);
        grandchild.addChild(text);

        assertEquals(root, grandchild.getRootNode());
        String xpath = grandchild.getXPath();
        assertTrue(xpath.contains("/parent/child/grandchild"));
    }

    @Test
    public void should_GetStringValue_WithMultipleTextChildren() {
        Text text2 = new Text(" ");
        Text text3 = new Text("more content");

        root.addChild(element1);
        element1.addChild(text1);
        element1.addChild(text2);
        element1.addChild(text3);

        String value = element1.getStringValue();
        assertTrue(value.contains("text content"));
        assertTrue(value.contains("more content"));
    }

    @Test
    public void should_HandleNamespaceInheritance() {
        Namespace nsMapping = new Namespace("", "http://default.ns");
        element1.addNamespace(nsMapping);
        root.addChild(element1);

        Element child = new Element(null, "child");
        element1.addChild(child);

        String uri = child.getNamespaceURI("");
        assertEquals("http://default.ns", uri);
    }

    // Mock Locator for testing
    private static class MockLocator implements Locator {
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

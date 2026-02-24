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

public class RootTest {

    @Test
    public void should_CreateRoot_With_DefaultProperties() {
        Root root = new Root();
        assertEquals(XMLNode.ROOT, root.getNodeType());
        assertEquals("#root", root.getLocalName());
        assertNull(root.getNamespaceURI());
        assertNull(root.getParentNode());
    }

    @Test
    public void should_ReturnRootItself_When_GetRootNodeCalled() {
        Root root = new Root();
        assertEquals(root, root.getRootNode());
    }

    @Test
    public void should_ReturnEmptyIterator_When_NoChildrenAdded() {
        Root root = new Root();
        Iterator iterator = root.getChildIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_AddAndRetrieveChild() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        root.addChild(element);

        Iterator iterator = root.getChildIterator();
        assertTrue(iterator.hasNext());
        assertEquals(element, iterator.next());
    }

    @Test
    public void should_AddMultipleChildren() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "element1");
        Element elem2 = new Element("http://example.com", "element2");
        Element elem3 = new Element("http://example.com", "element3");

        root.addChild(elem1);
        root.addChild(elem2);
        root.addChild(elem3);

        Iterator iterator = root.getChildIterator();
        assertEquals(elem1, iterator.next());
        assertEquals(elem2, iterator.next());
        assertEquals(elem3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_AddNullChild_WithoutError() {
        Root root = new Root();
        root.addChild(null);
        Iterator iterator = root.getChildIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_GetStringValue_OfAllChildren() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "element1");
        Text text1 = new Text("value1");
        elem1.addChild(text1);
        root.addChild(elem1);

        String stringValue = root.getStringValue();
        assertEquals("value1", stringValue);
    }

    @Test
    public void should_GetStringValue_OfEmptyRoot() {
        Root root = new Root();
        assertEquals("", root.getStringValue());
    }

    @Test
    public void should_GetStringValue_WithMultipleElements() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "element1");
        Element elem2 = new Element("http://example.com", "element2");
        Text text1 = new Text("text1");
        Text text2 = new Text("text2");
        elem1.addChild(text1);
        elem2.addChild(text2);
        root.addChild(elem1);
        root.addChild(elem2);

        String stringValue = root.getStringValue();
        assertEquals("text1text2", stringValue);
    }

    @Test
    public void should_SetAndGetNamespace() {
        Root root = new Root();
        root.setNamespace("http://example.com");
        assertEquals("http://example.com", root.getNamespaceURI());
    }

    @Test
    public void should_GetNamespaceURIFromPrefix() {
        Root root = new Root();
        Element elem = new Element("http://example.com", "element");
        elem.addNamespace(new Namespace("test", "http://test.com"));
        root.addChild(elem);

        String namespaceUri = elem.getNamespaceURI("test");
        assertEquals("http://test.com", namespaceUri);
    }

    @Test
    public void should_ReturnNullForUndefinedPrefix() {
        Root root = new Root();
        String namespaceUri = root.getNamespaceURI("undefined");
        assertNull(namespaceUri);
    }

    @Test
    public void should_AddProcessingInstructionAsChild() {
        Root root = new Root();
        ProcessingInstruction pi = new ProcessingInstruction("xml-stylesheet", "href=\"style.css\"");
        root.addChild(pi);

        Iterator iterator = root.getChildIterator();
        assertTrue(iterator.hasNext());
        assertEquals(pi, iterator.next());
    }

    @Test
    public void should_AddProcessingInstructionAndElement() {
        Root root = new Root();
        ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
        Element elem = new Element("http://example.com", "root");
        root.addChild(pi);
        root.addChild(elem);

        Iterator iterator = root.getChildIterator();
        assertEquals(pi, iterator.next());
        assertEquals(elem, iterator.next());
    }

    @Test
    public void should_GetChildCount() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "element1");
        Element elem2 = new Element("http://example.com", "element2");

        root.addChild(elem1);
        root.addChild(elem2);

        int count = 0;
        Iterator iterator = root.getChildIterator();
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        assertEquals(2, count);
    }

    @Test
    public void should_RemoveChild_ByIterator() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "element1");
        Element elem2 = new Element("http://example.com", "element2");
        root.addChild(elem1);
        root.addChild(elem2);

        Iterator iterator = root.getChildIterator();
        iterator.next();
        iterator.remove();

        iterator = root.getChildIterator();
        assertEquals(elem2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_HandleComplexHierarchy() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "element1");
        Element elem2 = new Element("http://example.com", "element2");
        Text text1 = new Text("text1");
        Text text2 = new Text("text2");

        elem1.addChild(text1);
        elem2.addChild(text2);
        root.addChild(elem1);
        root.addChild(elem2);

        assertEquals(root, elem1.getRootNode());
        assertEquals(root, elem2.getRootNode());
        assertEquals(root, text1.getRootNode());
        assertEquals(root, text2.getRootNode());
    }

    @Test
    public void should_GetParentNodeOfRoot_IsNull() {
        Root root = new Root();
        assertNull(root.getParentNode());
    }

    @Test
    public void should_ChildParentRelationship() {
        Root root = new Root();
        Element elem = new Element("http://example.com", "element");
        root.addChild(elem);
        assertEquals(root, elem.getParentNode());
    }

    @Test
    public void should_HandleEmptyElements() {
        Root root = new Root();
        Element emptyElem = new Element("http://example.com", "empty");
        root.addChild(emptyElem);
        assertEquals("", emptyElem.getStringValue());
    }

    @Test
    public void should_AddManyChildren() {
        Root root = new Root();
        for (int i = 0; i < 100; i++) {
            Element elem = new Element("http://example.com", "element" + i);
            root.addChild(elem);
        }

        int count = 0;
        Iterator iterator = root.getChildIterator();
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        assertEquals(100, count);
    }

    @Test
    public void should_MaintainChildOrder() {
        Root root = new Root();
        Element[] elements = new Element[5];
        for (int i = 0; i < 5; i++) {
            elements[i] = new Element("http://example.com", "element" + i);
            root.addChild(elements[i]);
        }

        int index = 0;
        Iterator iterator = root.getChildIterator();
        while (iterator.hasNext()) {
            assertEquals(elements[index], iterator.next());
            index++;
        }
    }

    @Test
    public void should_GetLocalNameAsRootConstant() {
        Root root = new Root();
        assertEquals("#root", root.getLocalName());
    }

    @Test
    public void should_HandleMixedNodeTypes() {
        Root root = new Root();
        ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
        Element elem = new Element("http://example.com", "root");
        Text text = new Text("mixed content");
        elem.addChild(text);

        root.addChild(pi);
        root.addChild(elem);

        Iterator iterator = root.getChildIterator();
        assertTrue(iterator.hasNext());
        assertEquals(pi, iterator.next());
        assertEquals(elem, iterator.next());
        assertFalse(iterator.hasNext());
    }

}

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

public class ParentNodeTest {

    @Test
    public void should_CreateRootAsParentNode() {
        Root root = new Root();
        assertNotNull(root);
        assertEquals(XMLNode.ROOT, root.getNodeType());
    }

    @Test
    public void should_CreateElementAsParentNode() {
        Element element = new Element("http://example.com", "element");
        assertNotNull(element);
        assertEquals(XMLNode.ELEMENT, element.getNodeType());
    }

    @Test
    public void should_ReturnEmptyIteratorWhenNoChildren() {
        Root root = new Root();
        Iterator iterator = root.getChildIterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_ReturnFalseForHasChildNodesWhenEmpty() {
        Root root = new Root();
        assertFalse(root.hasChildNodes());
    }

    @Test
    public void should_ReturnTrueForHasChildNodesWhenHasChildren() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        root.addChild(element);
        assertTrue(root.hasChildNodes());
    }

    @Test
    public void should_AddSingleChild() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        root.addChild(element);

        Iterator iterator = root.getChildIterator();
        assertTrue(iterator.hasNext());
        assertEquals(element, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_AddMultipleChildren() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "elem1");
        Element elem2 = new Element("http://example.com", "elem2");
        Element elem3 = new Element("http://example.com", "elem3");

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
    public void should_AddNullChildWithoutError() {
        Root root = new Root();
        root.addChild(null);
        assertFalse(root.hasChildNodes());
    }

    @Test
    public void should_ReturnEmptyStringValueWhenNoChildren() {
        Root root = new Root();
        assertEquals("", root.getStringValue());
    }

    @Test
    public void should_ReturnStringValueFromSingleChild() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        Text text = new Text("content");
        element.addChild(text);
        root.addChild(element);

        assertEquals("content", root.getStringValue());
    }

    @Test
    public void should_ConcatenateStringValuesFromMultipleChildren() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "elem1");
        Element elem2 = new Element("http://example.com", "elem2");
        Text text1 = new Text("text1");
        Text text2 = new Text("text2");

        elem1.addChild(text1);
        elem2.addChild(text2);
        root.addChild(elem1);
        root.addChild(elem2);

        assertEquals("text1text2", root.getStringValue());
    }

    @Test
    public void should_NormalizeConsecutiveTextNodes() {
        Element parent = new Element("http://example.com", "parent");
        Text text1 = new Text("Hello");
        Text text2 = new Text(" World");

        parent.addChild(text1);
        parent.addChild(text2);

        // Should be normalized into a single text node
        Iterator iterator = parent.getChildIterator();
        assertTrue(iterator.hasNext());
        Text combined = (Text) iterator.next();
        assertEquals("Hello World", combined.getStringValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_NotNormalizeNonTextNodes() {
        Element parent = new Element("http://example.com", "parent");
        Element elem1 = new Element("http://example.com", "elem1");
        Element elem2 = new Element("http://example.com", "elem2");

        parent.addChild(elem1);
        parent.addChild(elem2);

        Iterator iterator = parent.getChildIterator();
        assertEquals(elem1, iterator.next());
        assertEquals(elem2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_SetParentWhenAddingChild() {
        Root root = new Root();
        Element element = new Element("http://example.com", "element");
        root.addChild(element);

        assertEquals(root, element.getParentNode());
    }

    @Test
    public void should_AddManyChildren() {
        Root root = new Root();
        for (int i = 0; i < 100; i++) {
            Element element = new Element("http://example.com", "element" + i);
            root.addChild(element);
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
        assertEquals(5, index);
    }

    @Test
    public void should_HandleMixedChildTypes() {
        Root root = new Root();
        ProcessingInstruction pi = new ProcessingInstruction("xml", "version=\"1.0\"");
        Element element = new Element("http://example.com", "element");

        root.addChild(pi);
        root.addChild(element);

        Iterator iterator = root.getChildIterator();
        assertEquals(pi, iterator.next());
        assertEquals(element, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_HandleNestedParentNodes() {
        Root root = new Root();
        Element parent = new Element("http://example.com", "parent");
        Element child = new Element("http://example.com", "child");
        Text text = new Text("content");

        root.addChild(parent);
        parent.addChild(child);
        child.addChild(text);

        assertEquals(root, parent.getRootNode());
        assertEquals(root, child.getRootNode());
        assertEquals(root, text.getRootNode());
    }

    @Test
    public void should_GetStringValueFromNestedStructure() {
        Root root = new Root();
        Element parent = new Element("http://example.com", "parent");
        Element child1 = new Element("http://example.com", "child1");
        Element child2 = new Element("http://example.com", "child2");
        Text text1 = new Text("text1");
        Text text2 = new Text("text2");

        root.addChild(parent);
        parent.addChild(child1);
        parent.addChild(child2);
        child1.addChild(text1);
        child2.addChild(text2);

        assertEquals("text1text2", root.getStringValue());
    }

    @Test
    public void should_HandleTextWithSpecialCharactersInValue() {
        Element parent = new Element("http://example.com", "parent");
        Text text = new Text("Special: <>&\"'");
        parent.addChild(text);

        assertEquals("Special: <>&\"'", parent.getStringValue());
    }

    @Test
    public void should_HandleLargeNumberOfChildren() {
        Root root = new Root();
        for (int i = 0; i < 1000; i++) {
            Element element = new Element("http://example.com", "element");
            Text text = new Text("text" + i);
            element.addChild(text);
            root.addChild(element);
        }

        int count = 0;
        Iterator iterator = root.getChildIterator();
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        assertEquals(1000, count);
    }

    @Test
    public void should_HandleEmptyChildren() {
        Root root = new Root();
        Element emptyElement = new Element("http://example.com", "empty");
        root.addChild(emptyElement);

        assertEquals("", root.getStringValue());
    }

    @Test
    public void should_NormalizeMultipleConsecutiveTextNodes() {
        Element parent = new Element("http://example.com", "parent");
        Text text1 = new Text("A");
        Text text2 = new Text("B");
        Text text3 = new Text("C");

        parent.addChild(text1);
        parent.addChild(text2);
        parent.addChild(text3);

        // All should be normalized into one text node
        Iterator iterator = parent.getChildIterator();
        assertTrue(iterator.hasNext());
        Text combined = (Text) iterator.next();
        assertEquals("ABC", combined.getStringValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_NotNormalizeTextNodeSeparatedByElement() {
        Element parent = new Element("http://example.com", "parent");
        Text text1 = new Text("Hello");
        Element separator = new Element("http://example.com", "separator");
        Text text2 = new Text("World");

        parent.addChild(text1);
        parent.addChild(separator);
        parent.addChild(text2);

        // Should not be normalized because separated by element
        Iterator iterator = parent.getChildIterator();
        assertTrue(iterator.hasNext());
        assertEquals(text1, iterator.next());
        assertEquals(separator, iterator.next());
        assertEquals(text2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_ReturnCorrectStringValueWithMixedContent() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "elem1");
        Text text1 = new Text("start");
        elem1.addChild(text1);
        root.addChild(elem1);

        Element elem2 = new Element("http://example.com", "elem2");
        Text text2 = new Text("middle");
        elem2.addChild(text2);
        root.addChild(elem2);

        assertEquals("startmiddle", root.getStringValue());
    }

    @Test
    public void should_HandleWhitespaceOnlyTextNodes() {
        Element parent = new Element("http://example.com", "parent");
        Text whitespace = new Text("   ");
        parent.addChild(whitespace);

        assertEquals("   ", parent.getStringValue());
    }

    @Test
    public void should_GetChildIteratorMultipleTimes() {
        Root root = new Root();
        Element elem1 = new Element("http://example.com", "elem1");
        Element elem2 = new Element("http://example.com", "elem2");

        root.addChild(elem1);
        root.addChild(elem2);

        // First iteration
        Iterator iter1 = root.getChildIterator();
        assertTrue(iter1.hasNext());

        // Second iteration should be independent
        Iterator iter2 = root.getChildIterator();
        assertTrue(iter2.hasNext());

        assertEquals(elem1, iter1.next());
        assertEquals(elem1, iter2.next());
    }

    @Test
    public void should_HandleAddChildAfterGettingStringValue() {
        Element parent = new Element("http://example.com", "parent");
        Text text1 = new Text("initial");
        parent.addChild(text1);

        String value1 = parent.getStringValue();
        assertEquals("initial", value1);

        Text text2 = new Text(" added");
        parent.addChild(text2);

        String value2 = parent.getStringValue();
        assertEquals("initial added", value2);
    }

}

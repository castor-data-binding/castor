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

public class NamespaceTest {

    @Test
    public void should_CreateNamespace_When_AllParametersValid() {
        Namespace ns = new Namespace("myprefix", "http://example.com");
        assertEquals("myprefix", ns.getPrefix());
        assertEquals("http://example.com", ns.getNamespaceUri());
    }

    @Test
    public void should_CreateNamespace_When_PrefixIsNull() {
        Namespace ns = new Namespace(null, "http://example.com");
        assertNull(ns.getPrefix());
        assertEquals("http://example.com", ns.getNamespaceUri());
    }

    @Test
    public void should_CreateNamespace_When_NamespaceUriIsNull() {
        Namespace ns = new Namespace("myprefix", null);
        assertEquals("myprefix", ns.getPrefix());
        assertNull(ns.getNamespaceUri());
    }

    @Test
    public void should_CreateNamespace_When_BothAreNull() {
        Namespace ns = new Namespace(null, null);
        assertNull(ns.getPrefix());
        assertNull(ns.getNamespaceUri());
    }

    @Test
    public void should_CreateNamespace_When_PrefixIsEmpty() {
        Namespace ns = new Namespace("", "http://example.com");
        assertEquals("", ns.getPrefix());
        assertEquals("http://example.com", ns.getNamespaceUri());
    }

    @Test
    public void should_CreateNamespace_When_NamespaceUriIsEmpty() {
        Namespace ns = new Namespace("myprefix", "");
        assertEquals("myprefix", ns.getPrefix());
        assertEquals("", ns.getNamespaceUri());
    }

    @Test
    public void should_ReturnPrefix_When_GetPrefixCalled() {
        Namespace ns = new Namespace("test", "http://test.com");
        assertEquals("test", ns.getPrefix());
    }

    @Test
    public void should_ReturnNamespaceUri_When_GetNamespaceUriCalled() {
        Namespace ns = new Namespace("test", "http://test.com");
        assertEquals("http://test.com", ns.getNamespaceUri());
    }

    @Test
    public void should_HandleDefaultNamespace() {
        Namespace ns = new Namespace("", "http://example.com");
        assertEquals("", ns.getPrefix());
    }

    @Test
    public void should_HandleNamespaceWithComplexUri() {
        String complexUri = "http://example.com/2023/v1.0/schema";
        Namespace ns = new Namespace("complex", complexUri);
        assertEquals(complexUri, ns.getNamespaceUri());
    }

    @Test
    public void should_HandleNamespaceWithUrnScheme() {
        String urnUri = "urn:example:namespace:1.0";
        Namespace ns = new Namespace("urn", urnUri);
        assertEquals(urnUri, ns.getNamespaceUri());
    }

    @Test
    public void should_HandleMultipleNamespacesWithSamePrefix() {
        Namespace ns1 = new Namespace("same", "http://example1.com");
        Namespace ns2 = new Namespace("same", "http://example2.com");
        assertEquals(ns1.getPrefix(), ns2.getPrefix());
        assertNotEquals(ns1.getNamespaceUri(), ns2.getNamespaceUri());
    }

    @Test
    public void should_HandleMultipleNamespacesWithSameUri() {
        Namespace ns1 = new Namespace("prefix1", "http://example.com");
        Namespace ns2 = new Namespace("prefix2", "http://example.com");
        assertNotEquals(ns1.getPrefix(), ns2.getPrefix());
        assertEquals(ns1.getNamespaceUri(), ns2.getNamespaceUri());
    }

    @Test
    public void should_HandleNamespaceWithSpecialCharactersInPrefix() {
        Namespace ns = new Namespace("my-prefix", "http://example.com");
        assertEquals("my-prefix", ns.getPrefix());
    }

    @Test
    public void should_HandleNamespaceWithSpecialCharactersInUri() {
        String uri = "http://example.com/path?query=value&other=123";
        Namespace ns = new Namespace("prefix", uri);
        assertEquals(uri, ns.getNamespaceUri());
    }

    @Test
    public void should_HandleNamespaceWithUnicodeCharacters() {
        Namespace ns = new Namespace("前缀", "http://中文.com");
        assertEquals("前缀", ns.getPrefix());
        assertEquals("http://中文.com", ns.getNamespaceUri());
    }

    @Test
    public void should_HandleNamespaceWithLongPrefix() {
        String longPrefix = "a".repeat(256);
        Namespace ns = new Namespace(longPrefix, "http://example.com");
        assertEquals(longPrefix, ns.getPrefix());
    }

    @Test
    public void should_HandleNamespaceWithLongUri() {
        String longUri = "http://example.com/" + "a".repeat(500);
        Namespace ns = new Namespace("prefix", longUri);
        assertEquals(longUri, ns.getNamespaceUri());
    }

    @Test
    public void should_HandleXmlNamespace() {
        Namespace ns = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xml", ns.getPrefix());
        assertEquals("http://www.w3.org/XML/1998/namespace", ns.getNamespaceUri());
    }

    @Test
    public void should_HandleXmlnsNamespace() {
        Namespace ns = new Namespace("xmlns", "http://www.w3.org/2000/xmlns/");
        assertEquals("xmlns", ns.getPrefix());
        assertEquals("http://www.w3.org/2000/xmlns/", ns.getNamespaceUri());
    }

    @Test
    public void should_HandleSchemaNamespace() {
        Namespace ns = new Namespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", ns.getPrefix());
        assertEquals("http://www.w3.org/2001/XMLSchema", ns.getNamespaceUri());
    }

    @Test
    public void should_CreateMultipleNamespaceInstances() {
        Namespace[] namespaces = new Namespace[5];
        for (int i = 0; i < 5; i++) {
            namespaces[i] = new Namespace("prefix" + i, "http://example" + i + ".com");
        }
        for (int i = 0; i < 5; i++) {
            assertEquals("prefix" + i, namespaces[i].getPrefix());
            assertEquals("http://example" + i + ".com", namespaces[i].getNamespaceUri());
        }
    }

    @Test
    public void should_HandleNamespaceWithNumberInPrefix() {
        Namespace ns = new Namespace("prefix123", "http://example.com");
        assertEquals("prefix123", ns.getPrefix());
    }

    @Test
    public void should_HandleNamespaceWithUnderscoreInPrefix() {
        Namespace ns = new Namespace("my_prefix", "http://example.com");
        assertEquals("my_prefix", ns.getPrefix());
    }

    @Test
    public void should_HandleNamespaceEquality() {
        Namespace ns1 = new Namespace("prefix", "http://example.com");
        Namespace ns2 = new Namespace("prefix", "http://example.com");
        assertEquals(ns1.getPrefix(), ns2.getPrefix());
        assertEquals(ns1.getNamespaceUri(), ns2.getNamespaceUri());
    }

}

/*
 * Copyright 2008 Lukas Lang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.exolab.castor.builder.info.nature;

import junit.framework.TestCase;

import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.XMLInfo;
import org.exolab.castor.builder.info.NodeType;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.javasource.JClass;

/**
 * Tests access to {@link ClassInfo} properties via {@link XMLInfoNature}.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 */
public final class XMLNatureClassInfoTest extends TestCase {

    /**
     * The XMLNature the tests uses.
     */
    private XMLInfoNature _xml;
    
    /**
     * The abstract PropertHolder.
     */
    private PropertyHolder _holder;
    
    /**
     * Constructor initializes the XMLNature.
     */
    public XMLNatureClassInfoTest() {
        _holder = new ClassInfo(new JClass("test"));
        _xml = new XMLInfoNature(_holder);
    }
    
    /**
     * Tests set and get of the _xml node name.
     */
    public void testNodeName() {
        _xml.setNodeName("book");
        assertEquals("book", _xml.getNodeName());
    }
    
    /**
     * Tests set and get of the _xml name space prefix.
     */
    public void testNamespacePrefix() {
        _xml.setNamespacePrefix("abc");
        assertEquals("abc", _xml.getNamespacePrefix());
    }

    /**
     * Tests set and get of the _xml name space URI.
     */
    public void testNamespaceURI() {
        _xml.setNamespaceURI("http://www.castor.org/test");
        assertEquals("http://www.castor.org/test", _xml.getNamespaceURI());
    }
    
    /**
     * Tests set and get of the _xml element definition.
     */
    public void testElementDefinition() {
        _xml.setElementDefinition(true);
        assertEquals(true, _xml.isElementDefinition());
    }

    /**
     * Tests set and get of the _xml node type.
     */
    public void testNodeType() {
        _xml.setNodeType(NodeType.ELEMENT);
        assertEquals(NodeType.ELEMENT, _xml.getNodeType());
    }

    /**
     * Tests set and get of the _xml node type name.
     */
    public void testNodeTypeNameElement() {
        _xml.setNodeType(NodeType.ELEMENT);
        assertEquals("element", _xml.getNodeTypeName());
    }
    
    /**
     * Tests set and get of the _xml node type name.
     */
    public void testNodeTypeNameAttribute() {
        _xml.setNodeType(NodeType.ATTRIBUTE);
        assertEquals("attribute", _xml.getNodeTypeName());
    }
    
    /**
     * Tests set and get of the _xml node type name.
     */
    public void testNodeTypeNameText() {
        _xml.setNodeType(NodeType.TEXT);
        assertEquals("text", _xml.getNodeTypeName());
    }
    
//    /**
//     * Tests set and get of the _xml node type name.
//     */
//    public void testNodeTypeNameUnknown() {
//        _xml.setNodeType((short) 5);
//        assertEquals("unknown", _xml.getNodeTypeName());
//    }
    
    /**
     * Tests set and get of the _xml schema type.
     */
    public void testSchemaType() {
        JClass jClass = new JClass("Book");
        XSClass xsClass = new XSClass(jClass, "Book");
        _xml.setSchemaType(xsClass);
        assertEquals(xsClass, _xml.getSchemaType());
    }

    /**
     * Tests set and get of the multi-valued property.
     */
    public void testMultivalued() {
        _xml.setMultivalued(true);
        assertEquals(true, _xml.isMultivalued());
    }
    
    /**
     * Tests set and get of the required property.
     */
    public void testRequired() {
        _xml.setRequired(true);
        assertEquals(true, _xml.isRequired());
    }
    
}

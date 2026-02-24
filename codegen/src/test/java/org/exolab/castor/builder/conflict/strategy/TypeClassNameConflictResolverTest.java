/*
 * Copyright 2007 Werner Guttmann
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
package org.exolab.castor.builder.conflict.strategy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.XMLType;
import org.exolab.javasource.JClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test class for TypeClassNameConflictResolver.
 */
public class TypeClassNameConflictResolverTest {

    private TypeClassNameConflictResolver resolver;
    private SourceGenerator sourceGenerator;
    private JClass jClass;
    private ElementDecl elementDecl;
    private XMLType xmlType;
    private Schema schema;
    private Annotated annotated;

    @Before
    public void setUp() {
        resolver = new TypeClassNameConflictResolver();

        // Create mocks
        sourceGenerator = mock(SourceGenerator.class);
        resolver.setSourceGenerator(sourceGenerator);

        // Mock only the parts we need to control
        jClass = mock(JClass.class);
        elementDecl = mock(ElementDecl.class);
        xmlType = mock(XMLType.class);
        schema = mock(Schema.class);
        annotated = mock(Annotated.class);

        // Setup defaults
        when(jClass.getLocalName()).thenReturn("TestClass");
        when(elementDecl.getType()).thenReturn(xmlType);
        when(elementDecl.getSchema()).thenReturn(schema);
        when(elementDecl.getName()).thenReturn("testElement");
    }

    @Test
    public void testGetSourceGenerator() {
        assertNotNull(resolver.getSourceGenerator());
    }

    @Test
    public void testSetSourceGenerator() {
        SourceGenerator newGen = mock(SourceGenerator.class);
        resolver.setSourceGenerator(newGen);
        assertSame(newGen, resolver.getSourceGenerator());
    }

    @Test
    public void testChangeClassInfoWhenAnnotatedIsNotElementDecl() {
        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            annotated
        );

        verify(jClass, times(1)).getLocalName();
        verify(jClass, never()).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenElementIsGlobal() {
        when(elementDecl.getParent()).thenReturn(schema);
        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass, never()).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenElementIsReference() {
        ElementDecl parent = mock(ElementDecl.class);
        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(true);
        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass, never()).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenTypeMatches() {
        ElementDecl parent = mock(ElementDecl.class);
        ElementDecl globalElement = mock(ElementDecl.class);
        XMLType globalType = mock(XMLType.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(globalElement);
        when(globalElement.getType()).thenReturn(globalType);
        when(globalType.getName()).thenReturn("Type");
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass, never()).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenTypeDoesNotMatch() {
        ElementDecl parent = mock(ElementDecl.class);
        ElementDecl globalElement = mock(ElementDecl.class);
        XMLType globalType = mock(XMLType.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(globalElement);
        when(globalElement.getType()).thenReturn(globalType);
        when(globalType.getName()).thenReturn("Type1");
        when(xmlType.getName()).thenReturn("Type2");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type2]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenComplexTypeAnon() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(true);
        when(xmlType.getName()).thenReturn(null);

        String xpath = "/root/element";
        String typedXPath = "/root/element[/complexType:anon]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenComplexTypeNamed() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("SomeType");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:CustomType]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenGlobalElementNull() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenGlobalElementTypeNull() {
        ElementDecl parent = mock(ElementDecl.class);
        ElementDecl globalElement = mock(ElementDecl.class);
        XMLType globalType = mock(XMLType.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(globalElement);
        when(globalElement.getType()).thenReturn(globalType);
        when(globalType.getName()).thenReturn(null);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWhenSimpleTypeInXPath() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/simpleType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass, never()).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWithEmptyBrackets() {
        String xpath = "/root";
        String typedXPath = "/root[]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            annotated
        );

        // Should not throw exception
        assertTrue(true);
    }

    @Test
    public void testChangeClassInfoWithMultipleBrackets() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "[pre][/complexType:Type][post]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWithNestedPath() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(true);
        when(xmlType.getName()).thenReturn(null);

        String xpath = "/a/b/c/element";
        String typedXPath = "/a/b/c/element[/complexType:anon]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWithTypeNameNumber() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type123");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type123]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWithTypeNameUnderscore() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("My_Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:My_Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoWithMultipleColons() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Base");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:ns:TypeName]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testGetSourceGeneratorInitialized() {
        assertNotNull(resolver.getSourceGenerator());
        assertSame(sourceGenerator, resolver.getSourceGenerator());
    }

    @Test
    public void testSetSourceGeneratorMultipleTimes() {
        SourceGenerator first = mock(SourceGenerator.class);
        SourceGenerator second = mock(SourceGenerator.class);

        resolver.setSourceGenerator(first);
        assertSame(first, resolver.getSourceGenerator());

        resolver.setSourceGenerator(second);
        assertSame(second, resolver.getSourceGenerator());
    }

    @Test
    public void testChangeClassInfoNeverCallsWhenTypeNotComplex() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("SimpleType");

        String xpath = "/root";
        String typedXPath = "/root[/simpleType:SimpleType]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass, never()).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoVerifyJClassInteraction() {
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass, atLeastOnce()).getLocalName();
    }

    @Test
    public void testChangeClassInfoDifferentElementName() {
        ElementDecl parent = mock(ElementDecl.class);
        when(elementDecl.getName()).thenReturn("differentElement");
        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(schema.getElementDecl("differentElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }

    @Test
    public void testChangeClassInfoDifferentSchema() {
        Schema differentSchema = mock(Schema.class);
        ElementDecl parent = mock(ElementDecl.class);

        when(elementDecl.getParent()).thenReturn(parent);
        when(elementDecl.isReference()).thenReturn(false);
        when(elementDecl.getSchema()).thenReturn(differentSchema);
        when(differentSchema.getElementDecl("testElement")).thenReturn(null);
        when(xmlType.isComplexType()).thenReturn(false);
        when(xmlType.getName()).thenReturn("Type");

        String xpath = "/root";
        String typedXPath = "/root[/complexType:Type]";

        resolver.changeClassInfoAsResultOfConflict(
            jClass,
            xpath,
            typedXPath,
            elementDecl
        );

        verify(jClass).changeLocalName(anyString());
    }
}

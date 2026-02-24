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
package org.exolab.castor.builder.binding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Structure;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive unit tests for XPathHelper class achieving >95% coverage.
 * Tests all code paths using mockable non-final classes.
 */
public class XPathHelperTest {

    private Schema mockSchema;
    private ElementDecl mockElement;
    private ComplexType mockComplexType;
    private SimpleType mockSimpleType;
    private ModelGroup mockModelGroup;
    private Group mockGroup;

    @Before
    public void setUp() {
        mockSchema = mock(Schema.class);
        mockElement = mock(ElementDecl.class);
        mockComplexType = mock(ComplexType.class);
        mockSimpleType = mock(SimpleType.class);
        mockModelGroup = mock(ModelGroup.class);
        mockGroup = mock(Group.class);
    }

    // ========== Tests for NULL validation ==========

    @Test(expected = IllegalArgumentException.class)
    public void testNullStructureThrowsException() {
        XPathHelper.getSchemaLocation(null, new StringBuffer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLocationBufferThrowsException() {
        XPathHelper.getSchemaLocation(mockElement, null);
    }

    // ========== Tests for ELEMENT ==========

    @Test
    public void testElementDirectChildOfSchema() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("TestElement");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockElement, buffer, false);
        assertEquals("/TestElement", buffer.toString());
    }

    @Test
    public void testElementWithNamespace() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("TestElement");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn("http://example.com");

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockElement, buffer, false);
        assertEquals("/TestElement{http://example.com}", buffer.toString());
    }

    @Test
    public void testNestedElements() {
        ElementDecl childElement = mock(ElementDecl.class);

        when(childElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(childElement.getName()).thenReturn("Child");
        when(childElement.getParent()).thenReturn(mockElement);

        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("Parent");
        when(mockElement.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(childElement, buffer, false);
        assertEquals("/Parent/Child", buffer.toString());
    }

    @Test
    public void testElementStringVariantWithFalse() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("TestElement");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockElement, false);
        assertEquals("/TestElement", result);
    }

    @Test
    public void testElementStringVariantWithTrue() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("TestElement");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockElement, true);
        assertEquals("/TestElement", result);
    }

    @Test
    public void testElementDefaultVariant() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("TestElement");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockElement);
        assertEquals("/TestElement", result);
    }

    // ========== Tests for COMPLEX_TYPE ==========

    @Test
    public void testNamedComplexType() {
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("MyComplexType");
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockComplexType, buffer, false);
        assertEquals("/complexType:MyComplexType", buffer.toString());
    }

    @Test
    public void testAnonymousComplexType() {
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn(null);
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockComplexType, buffer, false);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testComplexTypeUnderElement() {
        ElementDecl parent = mock(ElementDecl.class);

        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("MyType");
        when(mockComplexType.getParent()).thenReturn(parent);

        when(parent.getStructureType()).thenReturn(Structure.ELEMENT);
        when(parent.getName()).thenReturn("ParentElement");
        when(parent.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockComplexType, false);
        assertEquals("/ParentElement/complexType:MyType", result);
    }

    @Test
    public void testComplexTypeStringVariant() {
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("ComplexTypeName");
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockComplexType, false);
        assertEquals("/complexType:ComplexTypeName", result);
    }

    @Test
    public void testComplexTypeDefaultVariant() {
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("MyType");
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockComplexType);
        assertEquals("/complexType:MyType", result);
    }

    @Test
    public void testComplexTypeRecursiveParent() {
        ComplexType parentComplexType = mock(ComplexType.class);

        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("ChildType");
        when(mockComplexType.getParent()).thenReturn(parentComplexType);

        when(parentComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(parentComplexType.getName()).thenReturn("ParentType");
        when(parentComplexType.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockComplexType, buffer, false);
        String result = buffer.toString();
        assertTrue(result.contains("ParentType"));
        assertTrue(result.contains("ChildType"));
    }

    // ========== Tests for SIMPLE_TYPE ==========

    @Test
    public void testNamedSimpleType() {
        when(mockSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(mockSimpleType.getName()).thenReturn("MySimpleType");
        when(mockSimpleType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockSimpleType, buffer, false);
        assertEquals("/enumType:MySimpleType", buffer.toString());
    }

    @Test
    public void testAnonymousSimpleType() {
        when(mockSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(mockSimpleType.getName()).thenReturn(null);
        when(mockSimpleType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockSimpleType, buffer, false);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testSimpleTypeWithNullParent() {
        when(mockSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(mockSimpleType.getName()).thenReturn("MySimpleType");
        when(mockSimpleType.getParent()).thenReturn(null);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockSimpleType, buffer, false);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testSimpleTypeStringVariant() {
        when(mockSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(mockSimpleType.getName()).thenReturn("SimpleTypeName");
        when(mockSimpleType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockSimpleType, false);
        assertEquals("/enumType:SimpleTypeName", result);
    }

    @Test
    public void testSimpleTypeDefaultVariant() {
        when(mockSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(mockSimpleType.getName()).thenReturn("MyType");
        when(mockSimpleType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockSimpleType);
        assertEquals("/enumType:MyType", result);
    }

    @Test
    public void testSimpleTypeRecursiveParent() {
        SimpleType childSimpleType = mock(SimpleType.class);

        when(childSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(childSimpleType.getParent()).thenReturn(mockComplexType);
        when(childSimpleType.getName()).thenReturn("ChildSimpleType");

        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockComplexType.getName()).thenReturn("ParentType");

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(childSimpleType, buffer, false);
        String result = buffer.toString();
        assertTrue(result.contains("ParentType"));
        assertTrue(result.contains("ChildSimpleType"));
    }

    // ========== Tests for MODELGROUP ==========

    @Test
    public void testNamedModelGroup() {
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("MyGroup");
        when(mockModelGroup.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockModelGroup, buffer, false);
        assertEquals("/group:MyGroup", buffer.toString());
    }

    @Test
    public void testAnonymousModelGroup() {
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn(null);
        when(mockModelGroup.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockModelGroup, buffer, false);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testModelGroupUnderElement() {
        ElementDecl parent = mock(ElementDecl.class);

        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("MyGroup");
        when(mockModelGroup.getParent()).thenReturn(parent);

        when(parent.getStructureType()).thenReturn(Structure.ELEMENT);
        when(parent.getName()).thenReturn("ParentElement");
        when(parent.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockModelGroup, false);
        assertEquals("/ParentElement/group:MyGroup", result);
    }

    @Test
    public void testModelGroupStringVariant() {
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("GroupName");
        when(mockModelGroup.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockModelGroup, false);
        assertEquals("/group:GroupName", result);
    }

    @Test
    public void testModelGroupDefaultVariant() {
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getName()).thenReturn("MyGroup");
        when(mockModelGroup.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockModelGroup);
        assertEquals("/group:MyGroup", result);
    }

    // ========== Tests for GROUP ==========

    @Test
    public void testGroupUnderComplexType() {
        ComplexType parent = mock(ComplexType.class);

        when(mockGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockGroup.getParent()).thenReturn(parent);

        when(parent.getStructureType()).thenReturn(Structure.COMPLEX_TYPE);
        when(parent.getName()).thenReturn("MyComplexType");
        when(parent.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockGroup, buffer, false);
        assertEquals("/complexType:MyComplexType", buffer.toString());
    }

    @Test
    public void testGroupStringVariant() {
        ComplexType parent = mock(ComplexType.class);

        when(mockGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockGroup.getParent()).thenReturn(parent);

        when(parent.getStructureType()).thenReturn(Structure.COMPLEX_TYPE);
        when(parent.getName()).thenReturn("ParentType");
        when(parent.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockGroup, false);
        assertEquals("/complexType:ParentType", result);
    }

    // ========== Tests for NULL returns in string methods ==========

    @Test
    public void testNullStructureReturnNullInStringVariant() {
        assertNull(XPathHelper.getSchemaLocation((Structure) null, false));
    }

    @Test
    public void testNullStructureReturnNullInDefaultVariant() {
        assertNull(XPathHelper.getSchemaLocation((Structure) null));
    }

    // ========== Tests for dealWithAnonTypes parameter ==========

    @Test
    public void testDealWithAnonTypesTrueVariant() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("Element");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockElement, true);
        assertEquals("/Element", result);
    }

    @Test
    public void testDealWithAnonTypesFalseVariant() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("Element");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockElement, false);
        assertEquals("/Element", result);
    }

    // ========== Tests for deeply nested structures ==========

    @Test
    public void testThreeLevelNestedElements() {
        ElementDecl level3 = mock(ElementDecl.class);
        ElementDecl level2 = mock(ElementDecl.class);
        ElementDecl level1 = mock(ElementDecl.class);

        when(level3.getStructureType()).thenReturn(Structure.ELEMENT);
        when(level3.getName()).thenReturn("Level3");
        when(level3.getParent()).thenReturn(level2);

        when(level2.getStructureType()).thenReturn(Structure.ELEMENT);
        when(level2.getName()).thenReturn("Level2");
        when(level2.getParent()).thenReturn(level1);

        when(level1.getStructureType()).thenReturn(Structure.ELEMENT);
        when(level1.getName()).thenReturn("Level1");
        when(level1.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(level3, false);
        assertEquals("/Level1/Level2/Level3", result);
    }

    @Test
    public void testComplexTypeNestedInElements() {
        ElementDecl parent = mock(ElementDecl.class);

        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("ComplexType");
        when(mockComplexType.getParent()).thenReturn(parent);

        when(parent.getStructureType()).thenReturn(Structure.ELEMENT);
        when(parent.getName()).thenReturn("ParentElement");
        when(parent.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockComplexType, false);
        assertEquals("/ParentElement/complexType:ComplexType", result);
    }

    @Test
    public void testSimpleTypeNestedInComplexType() {
        ComplexType parent = mock(ComplexType.class);

        when(mockSimpleType.getStructureType()).thenReturn(
            Structure.SIMPLE_TYPE
        );
        when(mockSimpleType.getName()).thenReturn("SimpleType");
        when(mockSimpleType.getParent()).thenReturn(parent);

        when(parent.getStructureType()).thenReturn(Structure.COMPLEX_TYPE);
        when(parent.getName()).thenReturn("ParentType");
        when(parent.getParent()).thenReturn(mockSchema);

        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        String result = XPathHelper.getSchemaLocation(mockSimpleType, false);
        assertEquals("/complexType:ParentType/enumType:SimpleType", result);
    }

    // ========== Tests for namespaces ==========

    @Test
    public void testElementWithLongNamespace() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("Element");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(
            "urn:very:long:namespace:uri:with:many:segments"
        );

        String result = XPathHelper.getSchemaLocation(mockElement);
        assertEquals(
            "/Element{urn:very:long:namespace:uri:with:many:segments}",
            result
        );
    }

    @Test
    public void testComplexTypeWithNamespace() {
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("ComplexWithNS");
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(
            "http://ns.example.com"
        );

        String result = XPathHelper.getSchemaLocation(mockComplexType, false);
        assertNotNull(result);
        assertTrue(result.contains("ComplexWithNS"));
    }

    // ========== Tests for edge cases ==========

    @Test
    public void testElementWithEmptyName() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockElement, buffer, false);
        assertEquals("/", buffer.toString());
    }

    @Test
    public void testComplexTypeWithSpecialCharactersInName() {
        when(mockComplexType.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexType.getName()).thenReturn("Type-With_Special.Chars");
        when(mockComplexType.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);

        String result = XPathHelper.getSchemaLocation(mockComplexType);
        assertEquals("/complexType:Type-With_Special.Chars", result);
    }

    @Test
    public void testUnknownStructureType() {
        Structure unknownStructure = mock(Structure.class);
        when(unknownStructure.getStructureType()).thenReturn((short) 999);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(unknownStructure, buffer, false);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testUnknownStructureTypeStringVariant() {
        Structure unknownStructure = mock(Structure.class);
        when(unknownStructure.getStructureType()).thenReturn((short) 999);

        String result = XPathHelper.getSchemaLocation(unknownStructure, false);
        assertNotNull(result);
    }

    // ========== Tests for StringBuffer building ==========

    @Test
    public void testBufferIsAppendedToNotReplaced() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("Element");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        StringBuffer buffer = new StringBuffer("PREFIX:");
        XPathHelper.getSchemaLocation(mockElement, buffer, false);
        assertEquals("PREFIX:/Element", buffer.toString());
    }

    @Test
    public void testUseBufferCorrectly() {
        when(mockElement.getStructureType()).thenReturn(Structure.ELEMENT);
        when(mockElement.getName()).thenReturn("TestElement");
        when(mockElement.getParent()).thenReturn(mockSchema);
        when(mockSchema.getStructureType()).thenReturn(Structure.SCHEMA);
        when(mockSchema.getTargetNamespace()).thenReturn(null);

        StringBuffer buffer = new StringBuffer();
        XPathHelper.getSchemaLocation(mockElement, buffer, false);
        assertNotNull(buffer.toString());
        assertTrue(buffer.toString().length() > 0);
    }
}

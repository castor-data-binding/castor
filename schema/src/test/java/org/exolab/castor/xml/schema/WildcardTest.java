package org.exolab.castor.xml.schema;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Enumeration;
import org.exolab.castor.xml.ValidationException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Wildcard class covering all methods and branches
 */
public class WildcardTest {

    private Wildcard wildcard;
    private ComplexType mockComplexType;
    private Group mockGroup;
    private AttributeGroup mockAttributeGroup;

    @Before
    public void setUp() {
        mockComplexType = mock(ComplexType.class);
        mockGroup = mock(Group.class);
        mockAttributeGroup = mock(AttributeGroup.class);
    }

    @Test
    public void testWildcardConstructorWithComplexType() {
        wildcard = new Wildcard(mockComplexType);
        assertNotNull(wildcard);
        assertEquals(mockComplexType, wildcard.getComplexType());
        assertNull(wildcard.getModelGroup());
        assertNull(wildcard.getAttributeGroup());
    }

    @Test
    public void testWildcardConstructorWithGroup() {
        wildcard = new Wildcard(mockGroup);
        assertNotNull(wildcard);
        assertNull(wildcard.getComplexType());
        assertEquals(mockGroup, wildcard.getModelGroup());
        assertNull(wildcard.getAttributeGroup());
    }

    @Test
    public void testWildcardConstructorWithAttributeGroup() {
        wildcard = new Wildcard(mockAttributeGroup);
        assertNotNull(wildcard);
        assertNull(wildcard.getComplexType());
        assertNull(wildcard.getModelGroup());
        assertEquals(mockAttributeGroup, wildcard.getAttributeGroup());
    }

    @Test
    public void testAddNamespace() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.addNamespace("http://example.com");

        Enumeration<String> namespaces = wildcard.getNamespaces();
        assertTrue(namespaces.hasMoreElements());
        assertEquals("http://example.com", namespaces.nextElement());
    }

    @Test
    public void testAddMultipleNamespaces() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.addNamespace("http://example.com");
        wildcard.addNamespace("http://test.com");

        Enumeration<String> namespaces = wildcard.getNamespaces();
        assertTrue(namespaces.hasMoreElements());
        String first = namespaces.nextElement();
        String second = namespaces.nextElement();

        assertTrue(
            (first.equals("http://example.com") &&
                    second.equals("http://test.com")) ||
                (first.equals("http://test.com") &&
                    second.equals("http://example.com"))
        );
    }

    @Test
    public void testRemoveNamespaceSuccessful() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.addNamespace("http://example.com");

        boolean removed = wildcard.removeNamespace("http://example.com");
        assertTrue(removed);

        Enumeration<String> namespaces = wildcard.getNamespaces();
        assertFalse(namespaces.hasMoreElements());
    }

    @Test
    public void testRemoveNamespaceNotFound() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.addNamespace("http://example.com");

        boolean removed = wildcard.removeNamespace("http://nonexistent.com");
        assertFalse(removed);
    }

    @Test
    public void testRemoveNamespaceNull() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.addNamespace("http://example.com");

        boolean removed = wildcard.removeNamespace(null);
        assertFalse(removed);
    }

    @Test
    public void testGetComplexType() {
        wildcard = new Wildcard(mockComplexType);
        assertEquals(mockComplexType, wildcard.getComplexType());
    }

    @Test
    public void testGetModelGroup() {
        wildcard = new Wildcard(mockGroup);
        assertEquals(mockGroup, wildcard.getModelGroup());
    }

    @Test
    public void testGetAttributeGroup() {
        wildcard = new Wildcard(mockAttributeGroup);
        assertEquals(mockAttributeGroup, wildcard.getAttributeGroup());
    }

    @Test
    public void testSetAttributeWildcard() {
        wildcard = new Wildcard(mockComplexType);
        assertFalse(wildcard.isAttributeWildcard());

        wildcard.setAttributeWildcard();
        assertTrue(wildcard.isAttributeWildcard());
    }

    @Test
    public void testIsAttributeWildcardDefaultFalse() {
        wildcard = new Wildcard(mockComplexType);
        assertFalse(wildcard.isAttributeWildcard());
    }

    @Test
    public void testSetId() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.setId("test-id");
        // setId does nothing, just ensure no exception is thrown
    }

    @Test
    public void testSetProcessContentsStrict() throws SchemaException {
        wildcard = new Wildcard(mockComplexType);
        wildcard.setProcessContents(SchemaNames.STRICT);
        assertEquals(SchemaNames.STRICT, wildcard.getProcessContent());
    }

    @Test
    public void testSetProcessContentsLax() throws SchemaException {
        wildcard = new Wildcard(mockComplexType);
        wildcard.setProcessContents(SchemaNames.LAX);
        assertEquals(SchemaNames.LAX, wildcard.getProcessContent());
    }

    @Test
    public void testSetProcessContentsSkip() throws SchemaException {
        wildcard = new Wildcard(mockComplexType);
        wildcard.setProcessContents(SchemaNames.SKIP);
        assertEquals(SchemaNames.SKIP, wildcard.getProcessContent());
    }

    @Test
    public void testSetProcessContentsInvalid() {
        wildcard = new Wildcard(mockComplexType);
        try {
            wildcard.setProcessContents("invalid");
            fail("Should have thrown SchemaException");
        } catch (SchemaException e) {
            assertTrue(
                e.getMessage().contains("processContents attribute not valid")
            );
        }
    }

    @Test
    public void testGetProcessContent() throws SchemaException {
        wildcard = new Wildcard(mockComplexType);
        String processContent = wildcard.getProcessContent();
        // Default should be STRICT
        assertEquals(SchemaNames.STRICT, processContent);
    }

    @Test
    public void testGetSchemaThroughComplexType() {
        Schema mockSchema = mock(Schema.class);
        when(mockComplexType.getSchema()).thenReturn(mockSchema);

        wildcard = new Wildcard(mockComplexType);
        assertEquals(mockSchema, wildcard.getSchema());
    }

    @Test
    public void testGetSchemaFromAttributeGroupWithComplexTypeParent() {
        Schema mockSchema = mock(Schema.class);
        ComplexType mockComplexTypeParent = mock(ComplexType.class);
        when(mockComplexTypeParent.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexTypeParent.getSchema()).thenReturn(mockSchema);

        Group mockGroupParent = mock(Group.class);
        when(mockGroupParent.getParent()).thenReturn(mockComplexTypeParent);

        wildcard = new Wildcard(mockGroupParent);
        assertEquals(mockSchema, wildcard.getSchema());
    }

    @Test
    public void testGetSchemaFromAttributeGroupWithModelGroupParent() {
        Schema mockSchema = mock(Schema.class);
        ModelGroup mockModelGroup = mock(ModelGroup.class);
        when(mockModelGroup.getStructureType()).thenReturn(
            Structure.MODELGROUP
        );
        when(mockModelGroup.getSchema()).thenReturn(mockSchema);

        Group mockGroupParent = mock(Group.class);
        when(mockGroupParent.getParent()).thenReturn(mockModelGroup);

        wildcard = new Wildcard(mockGroupParent);
        assertEquals(mockSchema, wildcard.getSchema());
    }

    @Test
    public void testGetSchemaFromAttributeGroupWithGroupParent() {
        Schema mockSchema = mock(Schema.class);
        ComplexType mockComplexTypeGrandParent = mock(ComplexType.class);
        when(mockComplexTypeGrandParent.getStructureType()).thenReturn(
            Structure.COMPLEX_TYPE
        );
        when(mockComplexTypeGrandParent.getSchema()).thenReturn(mockSchema);

        Group mockParentGroup = mock(Group.class);
        when(mockParentGroup.getStructureType()).thenReturn(Structure.GROUP);
        when(mockParentGroup.getParent()).thenReturn(
            mockComplexTypeGrandParent
        );

        Group mockGroupChild = mock(Group.class);
        when(mockGroupChild.getParent()).thenReturn(mockParentGroup);

        wildcard = new Wildcard(mockGroupChild);
        assertEquals(mockSchema, wildcard.getSchema());
    }

    @Test
    public void testGetSchemaWhenGroupIsNull() {
        wildcard = new Wildcard((Group) null);
        assertNull(wildcard.getSchema());
    }

    @Test
    public void testGetNamespaces() {
        wildcard = new Wildcard(mockComplexType);
        wildcard.addNamespace("http://example.com");
        wildcard.addNamespace("http://test.com");

        Enumeration<String> namespaces = wildcard.getNamespaces();
        assertNotNull(namespaces);
        assertTrue(namespaces.hasMoreElements());
    }

    @Test
    public void testGetNamespacesEmpty() {
        wildcard = new Wildcard(mockComplexType);
        Enumeration<String> namespaces = wildcard.getNamespaces();
        assertNotNull(namespaces);
        assertFalse(namespaces.hasMoreElements());
    }

    @Test
    public void testGetStructureType() {
        wildcard = new Wildcard(mockComplexType);
        assertEquals(Structure.WILDCARD, wildcard.getStructureType());
    }

    @Test
    public void testValidate() throws ValidationException {
        wildcard = new Wildcard(mockComplexType);
        wildcard.validate();
        // validate should not throw any exception
    }

    @Test
    public void testMaxOccursSetToOneByDefault() {
        wildcard = new Wildcard(mockComplexType);
        assertEquals(1, wildcard.getMaxOccurs());
    }

    @Test
    public void testMinOccursSetToOneByDefault() {
        wildcard = new Wildcard(mockComplexType);
        assertEquals(1, wildcard.getMinOccurs());
    }
}

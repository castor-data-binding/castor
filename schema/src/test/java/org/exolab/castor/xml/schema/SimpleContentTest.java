package org.exolab.castor.xml.schema;

import static org.junit.Assert.*;

import org.exolab.castor.xml.schema.simpletypes.ListType;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SimpleContent
 */
public class SimpleContentTest {

    private Schema schema;
    private SimpleType simpleType;
    private SimpleContent simpleContent;

    @Before
    public void setUp() throws Exception {
        schema = new Schema();
        simpleType = new ListType(schema);
        simpleType.setName("testType");
        simpleContent = new SimpleContent();
    }

    @Test
    public void testDefaultConstructor() {
        SimpleContent content = new SimpleContent();
        assertNotNull(content);
        assertEquals(ContentType.SIMPLE, content.getType());
        assertNull(content.getSimpleType());
        assertNull(content.getTypeName());
    }

    @Test
    public void testConstructorWithSimpleType() {
        SimpleContent content = new SimpleContent(simpleType);
        assertNotNull(content);
        assertEquals(ContentType.SIMPLE, content.getType());
        assertEquals(simpleType, content.getSimpleType());
        assertEquals("testType", content.getTypeName());
    }

    @Test
    public void testConstructorWithSchemaAndTypeName() {
        String typeName = "myType";
        SimpleContent content = new SimpleContent(schema, typeName);
        assertNotNull(content);
        assertEquals(ContentType.SIMPLE, content.getType());
        assertEquals(typeName, content.getTypeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullSchema() {
        new SimpleContent(null, "typeName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullTypeName() {
        new SimpleContent(schema, null);
    }

    @Test
    public void testCopyConstructorWithSimpleContent() {
        SimpleContent original = new SimpleContent(simpleType);
        SimpleContent copy = new SimpleContent(original);

        assertNotNull(copy);
        assertEquals(original.getSimpleType(), copy.getSimpleType());
        assertEquals(original.getTypeName(), copy.getTypeName());
    }

    @Test
    public void testCopyConstructorWithNull() {
        SimpleContent copy = new SimpleContent((SimpleContent) null);
        assertNotNull(copy);
        assertNull(copy.getSimpleType());
        assertNull(copy.getTypeName());
    }

    @Test
    public void testCopy() {
        SimpleContent original = new SimpleContent(simpleType);
        SimpleContent copy = original.copy();

        assertNotNull(copy);
        assertEquals(original.getSimpleType(), copy.getSimpleType());
        assertEquals(original.getTypeName(), copy.getTypeName());
    }

    @Test
    public void testSetAndGetSimpleType() {
        SimpleContent content = new SimpleContent();
        assertNull(content.getSimpleType());

        content.setSimpleType(simpleType);
        assertEquals(simpleType, content.getSimpleType());
    }

    @Test
    public void testGetSimpleTypeWithTypeName() throws Exception {
        SimpleContent content = new SimpleContent(schema, "string");

        SimpleType retrieved = content.getSimpleType();
        assertNotNull(retrieved);
        assertEquals("string", retrieved.getName());
    }

    @Test
    public void testGetSimpleTypeWithBuiltInType() throws Exception {
        SimpleContent content = new SimpleContent(schema, "integer");
        SimpleType retrieved = content.getSimpleType();

        assertNotNull(retrieved);
        assertEquals("integer", retrieved.getName());
    }

    @Test
    public void testGetSimpleTypeWithNonSimpleContentComplexType()
        throws Exception {
        ComplexType complexType = new ComplexType(schema);
        complexType.setName("nonSimpleContentType");

        Group group = new Group();
        complexType.addGroup(group);

        schema.addComplexType(complexType);

        SimpleContent content = new SimpleContent(
            schema,
            "nonSimpleContentType"
        );

        try {
            content.getSimpleType();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("must be a simpleContent"));
        }
    }

    @Test
    public void testGetTypeNameWithSimpleType() {
        SimpleContent content = new SimpleContent(simpleType);
        assertEquals("testType", content.getTypeName());
    }

    @Test
    public void testGetTypeNameWithoutSimpleType() {
        SimpleContent content = new SimpleContent(schema, "customTypeName");
        assertEquals("customTypeName", content.getTypeName());
    }

    @Test
    public void testGetTypeNameReturnsNull() {
        SimpleContent content = new SimpleContent();
        assertNull(content.getTypeName());
    }

    @Test
    public void testContentTypeIsSimple() {
        SimpleContent content = new SimpleContent();
        assertEquals(ContentType.SIMPLE, content.getType());
    }

    @Test
    public void testSetSimpleTypeOverwritesPrevious() throws Exception {
        SimpleType firstType = new ListType(schema);
        firstType.setName("firstType");

        SimpleType secondType = new ListType(schema);
        secondType.setName("secondType");

        SimpleContent content = new SimpleContent(firstType);
        assertEquals(firstType, content.getSimpleType());

        content.setSimpleType(secondType);
        assertEquals(secondType, content.getSimpleType());
        assertEquals("secondType", content.getTypeName());
    }

    @Test
    public void testCopyWithSchemaAndTypeName() {
        SimpleContent original = new SimpleContent(schema, "string");
        SimpleContent copy = original.copy();

        assertNotNull(copy);
        assertEquals(original.getTypeName(), copy.getTypeName());
    }

    @Test
    public void testGetSimpleTypeWithAnotherBuiltInType() throws Exception {
        SimpleContent content = new SimpleContent(schema, "boolean");
        SimpleType retrieved = content.getSimpleType();

        assertNotNull(retrieved);
        assertEquals("boolean", retrieved.getName());
    }

    @Test
    public void testSerializable() {
        assertTrue(simpleContent instanceof java.io.Serializable);
    }

    @Test
    public void testMultipleGetSimpleTypeCalls() throws Exception {
        SimpleContent content = new SimpleContent(schema, "string");

        SimpleType first = content.getSimpleType();
        SimpleType second = content.getSimpleType();

        assertSame(first, second);
    }

    @Test
    public void testSetSimpleTypeToNull() {
        SimpleContent content = new SimpleContent(simpleType);
        assertNotNull(content.getSimpleType());

        content.setSimpleType(null);
        assertNull(content.getSimpleType());
    }

    @Test
    public void testCopyPreservesSchema() throws Exception {
        SimpleContent original = new SimpleContent(schema, "string");
        SimpleContent copy = original.copy();

        assertEquals(original.getTypeName(), copy.getTypeName());
    }

    @Test
    public void testGetSimpleTypeWithUnnamedType() throws Exception {
        ListType unnamedType = new ListType(schema);
        SimpleContent content = new SimpleContent(unnamedType);

        assertNull(content.getTypeName());
        assertEquals(unnamedType, content.getSimpleType());
    }

    @Test
    public void testCopyWithAllFields() throws Exception {
        SimpleContent original = new SimpleContent(schema, "string");
        SimpleContent copy = original.copy();

        assertNotNull(copy);
        assertEquals(original.getTypeName(), copy.getTypeName());
    }
}

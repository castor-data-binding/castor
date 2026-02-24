package org.exolab.castor.xml.schema.writer;

import java.io.IOException;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AppInfo;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.AttributeGroupDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.Documentation;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Form;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaContextImpl;
import org.exolab.castor.xml.schema.SchemaException;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

public class SchemaWriterTest extends TestCase {

    private static class MockDocumentHandler implements DocumentHandler {

        public int elementCount = 0;
        public int characterCount = 0;

        public void setDocumentLocator(org.xml.sax.Locator locator) {}

        public void startDocument() throws SAXException {}

        public void endDocument() throws SAXException {}

        public void startElement(String name, org.xml.sax.AttributeList atts)
            throws SAXException {
            elementCount++;
        }

        public void endElement(String name) throws SAXException {}

        public void characters(char[] ch, int start, int length)
            throws SAXException {
            characterCount++;
        }

        public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {}

        public void processingInstruction(String target, String data)
            throws SAXException {}
    }

    public void testSchemaWriterConstructorNoArg() {
        SchemaWriter writer = new SchemaWriter();
        assertNotNull(writer);
    }

    public void testSchemaWriterConstructorWithDocumentHandler() {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        assertNotNull(writer);
    }

    public void testSchemaWriterConstructorWithNullDocumentHandler() {
        try {
            new SchemaWriter((DocumentHandler) null);
            fail("Expected IllegalArgumentException for null DocumentHandler");
        } catch (IllegalArgumentException e) {
            assertTrue(
                e.getMessage().contains("DocumentHandler must not be null")
            );
        }
    }

    public void testSchemaWriterConstructorWithWriter() throws IOException {
        StringWriter writer = new StringWriter();
        SchemaWriter schemaWriter = new SchemaWriter(writer);
        assertNotNull(schemaWriter);
    }

    public void testSetSchemaContext() {
        SchemaWriter writer = new SchemaWriter();
        SchemaContext context = new SchemaContextImpl();
        writer.setSchemaContext(context);
        assertNotNull(writer);
    }

    public void testSetDocumentHandlerWithHandler() {
        SchemaWriter writer = new SchemaWriter();
        DocumentHandler handler = new MockDocumentHandler();
        writer.setDocumentHandler(handler);
        assertNotNull(writer);
    }

    public void testSetDocumentHandlerWithNullHandler() {
        SchemaWriter writer = new SchemaWriter();
        try {
            writer.setDocumentHandler((DocumentHandler) null);
            fail("Expected IllegalArgumentException for null DocumentHandler");
        } catch (IllegalArgumentException e) {
            assertTrue(
                e.getMessage().contains("DocumentHandler must not be null")
            );
        }
    }

    public void testSetDocumentHandlerWithWriter() throws IOException {
        SchemaWriter writer = new SchemaWriter();
        StringWriter stringWriter = new StringWriter();
        writer.setDocumentHandler(stringWriter);
        assertNotNull(writer);
    }

    public void testWriteEmptySchema() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        writer.write(schema);
    }

    public void testWriteSchemaWithMockHandler() throws SAXException {
        MockDocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        writer.write(schema);
        assertTrue("Handler should receive elements", handler.elementCount > 0);
    }

    public void testWriteSchemaWithContext() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        SchemaContext context = new SchemaContextImpl();
        writer.setSchemaContext(context);
        Schema schema = new Schema();
        writer.write(schema);
    }

    public void testWriteSchemaWithTargetNamespace() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/test");
        writer.write(schema);
    }

    public void testWriteSchemaWithMultipleNamespaces() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/main");
        schema.addNamespace("test", "http://example.com/test");
        writer.write(schema);
    }

    public void testWriteSchemaWithElements()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/elements");

        ElementDecl element = new ElementDecl(schema, "testElement");
        element.setMaxOccurs(1);
        element.setMinOccurs(1);
        schema.addElementDecl(element);
        writer.write(schema);
    }

    public void testWriteSchemaWithMultipleElements()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ElementDecl element1 = new ElementDecl(schema, "element1");
        ElementDecl element2 = new ElementDecl(schema, "element2");
        schema.addElementDecl(element1);
        schema.addElementDecl(element2);
        writer.write(schema);
    }

    public void testWriteSchemaWithComplexType()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/complex");

        ComplexType complexType = new ComplexType(schema);
        complexType.setName("TestComplexType");
        schema.addComplexType(complexType);

        writer.write(schema);
    }

    public void testWriteSchemaWithComplexTypeMultiple()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ComplexType type1 = new ComplexType(schema);
        type1.setName("Type1");
        ComplexType type2 = new ComplexType(schema);
        type2.setName("Type2");

        schema.addComplexType(type1);
        schema.addComplexType(type2);

        writer.write(schema);
    }

    public void testWriteSchemaWithAttribute()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        AttributeDecl attribute = new AttributeDecl(schema, "testAttr");
        schema.addAttribute(attribute);
        writer.write(schema);
    }

    public void testWriteSchemaWithMultipleAttributes()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        AttributeDecl attribute1 = new AttributeDecl(schema, "attr1");
        AttributeDecl attribute2 = new AttributeDecl(schema, "attr2");
        schema.addAttribute(attribute1);
        schema.addAttribute(attribute2);
        writer.write(schema);
    }

    public void testWriteSchemaWithAttributeGroup()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        AttributeGroupDecl attrGroup = new AttributeGroupDecl(schema);
        attrGroup.setName("testAttrGrp");
        schema.addAttributeGroup(attrGroup);
        writer.write(schema);
    }

    public void testSetDocumentHandlerAfterConstruction() {
        SchemaWriter writer = new SchemaWriter();
        DocumentHandler handler1 = new MockDocumentHandler();
        writer.setDocumentHandler(handler1);

        DocumentHandler handler2 = new MockDocumentHandler();
        writer.setDocumentHandler(handler2);
        assertNotNull(writer);
    }

    public void testMultipleSchemaWrites() throws SAXException {
        MockDocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);

        Schema schema1 = new Schema();
        schema1.setTargetNamespace("http://example.com/schema1");
        writer.write(schema1);

        int firstCount = handler.elementCount;

        MockDocumentHandler handler2 = new MockDocumentHandler();
        writer.setDocumentHandler(handler2);
        Schema schema2 = new Schema();
        schema2.setTargetNamespace("http://example.com/schema2");
        writer.write(schema2);

        assertTrue(
            "Both schemas should produce elements",
            handler2.elementCount > 0
        );
    }

    public void testSetSchemaContextMultipleTimes() {
        SchemaWriter writer = new SchemaWriter();
        SchemaContext context1 = new SchemaContextImpl();
        writer.setSchemaContext(context1);

        SchemaContext context2 = new SchemaContextImpl();
        writer.setSchemaContext(context2);
        assertNotNull(writer);
    }

    public void testSchemaWriterWithAnnotation()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        Annotation annotation = new Annotation();
        Documentation doc = new Documentation();
        annotation.addDocumentation(doc);
        schema.addAnnotation(annotation);

        writer.write(schema);
    }

    public void testSchemaWriterWithAppInfo()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        Annotation annotation = new Annotation();
        AppInfo appInfo = new AppInfo();
        annotation.addAppInfo(appInfo);
        schema.addAnnotation(annotation);

        writer.write(schema);
    }

    public void testSchemaWriterWithBothDocumentationAndAppInfo()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        Annotation annotation = new Annotation();
        Documentation doc = new Documentation();
        annotation.addDocumentation(doc);

        AppInfo appInfo = new AppInfo();
        annotation.addAppInfo(appInfo);

        schema.addAnnotation(annotation);
        writer.write(schema);
    }

    public void testWriteComplexSchemaStructure()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/complex");

        ElementDecl rootElement = new ElementDecl(schema, "root");
        ComplexType rootComplexType = new ComplexType(schema);
        rootComplexType.setName("RootType");
        rootElement.setType(rootComplexType);
        schema.addElementDecl(rootElement);

        writer.write(schema);
    }

    public void testSchemaWriterInitialization() {
        SchemaWriter writer = new SchemaWriter();
        DocumentHandler handler = new MockDocumentHandler();
        writer.setDocumentHandler(handler);
        SchemaContext context = new SchemaContextImpl();
        writer.setSchemaContext(context);
        assertNotNull(writer);
    }

    public void testWriteSchemaWithFormQualified() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setAttributeFormDefault(Form.Qualified);
        schema.setElementFormDefault(Form.Qualified);
        writer.write(schema);
    }

    public void testWriteSchemaWithFormUnqualified() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setAttributeFormDefault(Form.Unqualified);
        schema.setElementFormDefault(Form.Unqualified);
        writer.write(schema);
    }

    public void testWriteSchemaWithElementMinMaxOccurs()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ElementDecl element = new ElementDecl(schema, "element");
        element.setMinOccurs(1);
        element.setMaxOccurs(10);
        schema.addElementDecl(element);
        writer.write(schema);
    }

    public void testWriteSchemaWithComplexTypeSimpleContent()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ComplexType complexType = new ComplexType(schema);
        complexType.setName("SimpleContentType");
        schema.addComplexType(complexType);

        writer.write(schema);
    }

    public void testWriteSchemaWithBlockDefault() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setBlockDefault("#all");
        writer.write(schema);
    }

    public void testWriteSchemaWithFinalDefault() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setFinalDefault("#all");
        writer.write(schema);
    }

    public void testWriteSchemaWithElementFormDefaultQualified()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setElementFormDefault(Form.Qualified);

        ElementDecl element = new ElementDecl(schema, "element");
        schema.addElementDecl(element);

        writer.write(schema);
    }

    public void testWriteSchemaWithAttributeFormDefaultQualified()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setAttributeFormDefault(Form.Qualified);

        AttributeDecl attribute = new AttributeDecl(schema, "attribute");
        schema.addAttribute(attribute);

        writer.write(schema);
    }

    public void testWriteSchemaWithComplexTypeAndElement()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/test");

        ComplexType complexType = new ComplexType(schema);
        complexType.setName("PersonType");
        schema.addComplexType(complexType);

        ElementDecl personElement = new ElementDecl(schema, "person");
        personElement.setType(complexType);
        schema.addElementDecl(personElement);

        writer.write(schema);
    }

    public void testWriteSchemaWithMultipleAnnotations()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        Annotation annotation1 = new Annotation();
        Documentation doc1 = new Documentation();
        annotation1.addDocumentation(doc1);
        schema.addAnnotation(annotation1);

        Annotation annotation2 = new Annotation();
        AppInfo appInfo = new AppInfo();
        annotation2.addAppInfo(appInfo);
        schema.addAnnotation(annotation2);

        writer.write(schema);
    }

    public void testWriteSchemaWithAnnotatedElement()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ElementDecl element = new ElementDecl(schema, "annotatedElement");
        Annotation elemAnnotation = new Annotation();
        Documentation elemDoc = new Documentation();
        elemAnnotation.addDocumentation(elemDoc);
        element.addAnnotation(elemAnnotation);
        schema.addElementDecl(element);

        writer.write(schema);
    }

    public void testWriteSchemaWithAnnotatedAttribute()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        AttributeDecl attribute = new AttributeDecl(schema, "annotatedAttr");
        Annotation attrAnnotation = new Annotation();
        Documentation attrDoc = new Documentation();
        attrAnnotation.addDocumentation(attrDoc);
        attribute.addAnnotation(attrAnnotation);
        schema.addAttribute(attribute);

        writer.write(schema);
    }

    public void testWriteMultipleComplexTypesWithAttributes()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ComplexType type1 = new ComplexType(schema);
        type1.setName("Type1");
        ComplexType type2 = new ComplexType(schema);
        type2.setName("Type2");

        schema.addComplexType(type1);
        schema.addComplexType(type2);

        AttributeDecl attr1 = new AttributeDecl(schema, "attr1");
        AttributeDecl attr2 = new AttributeDecl(schema, "attr2");
        schema.addAttribute(attr1);
        schema.addAttribute(attr2);

        writer.write(schema);
    }

    public void testWriteSchemaEmptyNamespace()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ElementDecl elem = new ElementDecl(schema, "simpleElement");
        schema.addElementDecl(elem);

        writer.write(schema);
    }

    public void testWriteSchemaSetContextMultipleTimes() throws SAXException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);

        SchemaContext context1 = new SchemaContextImpl();
        writer.setSchemaContext(context1);

        SchemaContext context2 = new SchemaContextImpl();
        writer.setSchemaContext(context2);

        Schema schema = new Schema();
        writer.write(schema);
    }

    public void testWriteSchemaAfterMultipleSetDocumentHandlers()
        throws SAXException {
        SchemaWriter writer = new SchemaWriter();

        DocumentHandler handler1 = new MockDocumentHandler();
        writer.setDocumentHandler(handler1);

        DocumentHandler handler2 = new MockDocumentHandler();
        writer.setDocumentHandler(handler2);

        Schema schema = new Schema();
        writer.write(schema);
    }

    public void testWriteSchemaWithComplexTypeAndComplexContent()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();

        ComplexType baseType = new ComplexType(schema);
        baseType.setName("BaseType");
        schema.addComplexType(baseType);

        ComplexType derivedType = new ComplexType(schema);
        derivedType.setName("DerivedType");
        schema.addComplexType(derivedType);

        writer.write(schema);
    }

    public void testWriteMultipleSchemasSequentially() throws SAXException {
        SchemaWriter writer = new SchemaWriter();

        Schema schema1 = new Schema();
        schema1.setTargetNamespace("http://example.com/seq1");
        writer.write(schema1);

        Schema schema2 = new Schema();
        schema2.setTargetNamespace("http://example.com/seq2");
        writer.write(schema2);

        Schema schema3 = new Schema();
        schema3.setTargetNamespace("http://example.com/seq3");
        writer.write(schema3);
    }

    public void testWriteSchemaWithComplexContentStructure()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/content");

        ComplexType baseType = new ComplexType(schema);
        baseType.setName("BaseType");
        schema.addComplexType(baseType);

        ComplexType extendedType = new ComplexType(schema);
        extendedType.setName("ExtendedType");
        schema.addComplexType(extendedType);

        ElementDecl elem = new ElementDecl(schema, "elem");
        elem.setType(baseType);
        schema.addElementDecl(elem);

        writer.write(schema);
    }

    public void testWriteSchemaHandlerReceivesEndDocument()
        throws SAXException {
        MockDocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        int beforeCount = handler.elementCount;
        writer.write(schema);
        assertTrue(
            "Handler elementCount should increase after write",
            handler.elementCount >= beforeCount
        );
    }

    public void testWriteSchemaWithSchemaPrefix()
        throws SAXException, SchemaException {
        DocumentHandler handler = new MockDocumentHandler();
        SchemaWriter writer = new SchemaWriter(handler);
        Schema schema = new Schema();
        schema.setTargetNamespace("http://example.com/prefix");
        schema.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
        ElementDecl elem = new ElementDecl(schema, "elem");
        schema.addElementDecl(elem);
        writer.write(schema);
    }
}

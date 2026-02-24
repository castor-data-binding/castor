package org.exolab.castor.xml.schema.reader;

import java.io.StringReader;
import junit.framework.TestCase;
import org.exolab.castor.xml.schema.Schema;
import org.xml.sax.InputSource;

public class ReaderCoverageTest extends TestCase {

    public void testSchemaReaderConstruction() throws Exception {
        SchemaReader reader = new SchemaReader();
        assertNotNull(reader);
    }

    public void testParseSimpleSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"root\" type=\"xs:string\"/>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseSchemaWithTargetNamespace() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" " +
            "targetNamespace=\"http://example.com/test\">\n" +
            "  <xs:element name=\"root\" type=\"xs:string\"/>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
        assertEquals("http://example.com/test", schema.getTargetNamespace());
    }

    public void testParseComplexTypeSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:complexType name=\"PersonType\">\n" +
            "    <xs:sequence>\n" +
            "      <xs:element name=\"name\" type=\"xs:string\"/>\n" +
            "      <xs:element name=\"age\" type=\"xs:integer\"/>\n" +
            "    </xs:sequence>\n" +
            "  </xs:complexType>\n" +
            "  <xs:element name=\"person\" type=\"PersonType\"/>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseSimpleTypeSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:simpleType name=\"StringType\">\n" +
            "    <xs:restriction base=\"xs:string\"/>\n" +
            "  </xs:simpleType>\n" +
            "  <xs:element name=\"text\" type=\"StringType\"/>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseAttributeSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:attribute name=\"id\" type=\"xs:string\"/>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseAttributeGroupSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:attributeGroup name=\"idGroup\">\n" +
            "    <xs:attribute name=\"id\" type=\"xs:string\"/>\n" +
            "  </xs:attributeGroup>\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:attributeGroup ref=\"idGroup\"/>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseGroupSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:group name=\"nameGroup\">\n" +
            "    <xs:sequence>\n" +
            "      <xs:element name=\"firstName\" type=\"xs:string\"/>\n" +
            "      <xs:element name=\"lastName\" type=\"xs:string\"/>\n" +
            "    </xs:sequence>\n" +
            "  </xs:group>\n" +
            "  <xs:element name=\"person\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:group ref=\"nameGroup\"/>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseExtensionSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:complexType name=\"BaseType\">\n" +
            "    <xs:sequence>\n" +
            "      <xs:element name=\"id\" type=\"xs:string\"/>\n" +
            "    </xs:sequence>\n" +
            "  </xs:complexType>\n" +
            "  <xs:complexType name=\"DerivedType\">\n" +
            "    <xs:complexContent>\n" +
            "      <xs:extension base=\"BaseType\">\n" +
            "        <xs:sequence>\n" +
            "          <xs:element name=\"name\" type=\"xs:string\"/>\n" +
            "        </xs:sequence>\n" +
            "      </xs:extension>\n" +
            "    </xs:complexContent>\n" +
            "  </xs:complexType>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseRestrictionSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:simpleType name=\"RestrictedString\">\n" +
            "    <xs:restriction base=\"xs:string\">\n" +
            "      <xs:minLength value=\"1\"/>\n" +
            "      <xs:maxLength value=\"100\"/>\n" +
            "    </xs:restriction>\n" +
            "  </xs:simpleType>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseUnionSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:simpleType name=\"StringOrInteger\">\n" +
            "    <xs:union memberTypes=\"xs:string xs:integer\"/>\n" +
            "  </xs:simpleType>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseListSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:simpleType name=\"StringList\">\n" +
            "    <xs:list itemType=\"xs:string\"/>\n" +
            "  </xs:simpleType>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseAnnotationSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:annotation>\n" +
            "    <xs:documentation>This is a test schema</xs:documentation>\n" +
            "  </xs:annotation>\n" +
            "  <xs:element name=\"root\" type=\"xs:string\"/>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseChoiceSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:choice>\n" +
            "        <xs:element name=\"a\" type=\"xs:string\"/>\n" +
            "        <xs:element name=\"b\" type=\"xs:integer\"/>\n" +
            "      </xs:choice>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseAllSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:all>\n" +
            "        <xs:element name=\"a\" type=\"xs:string\"/>\n" +
            "        <xs:element name=\"b\" type=\"xs:integer\"/>\n" +
            "      </xs:all>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseWildcardSchema() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:any minOccurs=\"0\" maxOccurs=\"unbounded\"/>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseMultipleElements() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"elem1\" type=\"xs:string\"/>\n" +
            "  <xs:element name=\"elem2\" type=\"xs:integer\"/>\n" +
            "  <xs:element name=\"elem3\" type=\"xs:boolean\"/>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseElementWithOccurrence() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:element name=\"root\">\n" +
            "    <xs:complexType>\n" +
            "      <xs:sequence>\n" +
            "        <xs:element name=\"item\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"unbounded\"/>\n" +
            "      </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "  </xs:element>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testParseSchemaWithAppInfo() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <xs:annotation>\n" +
            "    <xs:appinfo>Custom metadata</xs:appinfo>\n" +
            "  </xs:annotation>\n" +
            "</xs:schema>";

        InputSource source = new InputSource(new StringReader(schemaXml));
        SchemaReader reader = new SchemaReader(source);
        Schema schema = reader.read();
        assertNotNull(schema);
    }

    public void testSchemaReaderWithStringReader() throws Exception {
        String schemaXml =
            "<?xml version=\"1.0\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "</xs:schema>";

        StringReader sr = new StringReader(schemaXml);
        SchemaReader reader = new SchemaReader(sr, "test.xsd");
        Schema schema = reader.read();
        assertNotNull(schema);
    }
}

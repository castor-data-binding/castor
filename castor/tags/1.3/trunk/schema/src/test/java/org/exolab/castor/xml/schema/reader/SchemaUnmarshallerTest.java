/*
 * Copyright 2008 Lukas Lang, Filip Hianik
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
 */
package org.exolab.castor.xml.schema.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;

import org.castor.xml.BackwardCompatibilityContext;
import org.castor.xml.InternalContext;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AppInfo;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaContextImpl;
import org.exolab.castor.xml.schema.annotations.jdo.Column;
import org.exolab.castor.xml.schema.annotations.jdo.OneToMany;
import org.exolab.castor.xml.schema.annotations.jdo.OneToOne;
import org.exolab.castor.xml.schema.annotations.jdo.Table;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

/**
 * This test is a integration test. It verifies whether a SchemaUnmarshaller
 * correctly unmarshalles a JDO annoted Schema. As a test Schema it uses a
 * Schema with an element declaration referring to a complexType including
 * various elements.
 * 
 * @author Lukas Lang
 * 
 */
public class SchemaUnmarshallerTest extends TestCase {

    /**
     * Test tries to unmarshall jdo specific content in appinfo elements within
     * a complete annoted schema.<br>
     * <br>
     * Action: Parse an annoted schema.<br>
     * Precondition: schema-entity.xml holds a correct annoted schema with
     * appinfo content.<br>
     * Postcondition: The complexType bookType holds a table object in it's
     * annotations. The element declaration of isbn holds a column object. The
     * element declaration of title also holds a column object.
     * @throws IOException 
     * @throws SAXException 
     * @throws FileNotFoundException 
     */
    public void testUnmarshallSchema() throws FileNotFoundException, SAXException, IOException {
        Schema schema = unmarshalSchema("schema-entity.xsd");
        ComplexType bookType = schema.getComplexType("bookType");
        Enumeration annotations = bookType.getAnnotations();
        Annotation annotation = (Annotation) annotations.nextElement();
        Enumeration appInfos = annotation.getAppInfo();
        AppInfo appInfo = (AppInfo) appInfos.nextElement();
        List jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        Table t = (Table) jdoContent.get(0);
        assertEquals("book", t.getName());
        assertEquals("isbn", t.getPrimaryKey().getKey(0));

        ElementDecl isbn = bookType.getElementDecl("isbn");
        annotations = isbn.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        Column c = (Column) jdoContent.get(0);
        assertEquals("isbn", c.getName());
        assertEquals("jdo:string", c.getType());

        ElementDecl title = bookType.getElementDecl("title");
        annotations = title.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        c = (Column) jdoContent.get(0);
        assertEquals("title", c.getName());
        assertEquals("jdo:string", c.getType());
    }

    /**
     * Test tries to unmarshall non-jdo specific content in appinfo elements
     * within a complete annoted schema.<br>
     * <br>
     * Action: Parse an annoted schema.<br>
     * Precondition: schema-entity-non-jdo.xml holds a correct annoted schema
     * with NO JDO content.<br>
     * Postcondition: The complexType bookType, isbn and title have no
     * jdo-specific content in it's annotations.
     * @throws IOException 
     * @throws SAXException 
     */
    public void testUnmarshallNonJdoSchema() throws SAXException, IOException {
        Schema schema = unmarshalSchema("schema-entity-non-jdo.xsd");
        ComplexType bookType = schema.getComplexType("bookType");
        Enumeration annotations = bookType.getAnnotations();
        Annotation annotation = (Annotation) annotations.nextElement();
        Enumeration appInfos = annotation.getAppInfo();
        AppInfo appInfo = (AppInfo) appInfos.nextElement();
        List jdoContent = appInfo.getJdoContent();

        assertEquals(0, jdoContent.size());

        ElementDecl isbn = bookType.getElementDecl("isbn");
        annotations = isbn.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(0, jdoContent.size());

        ElementDecl title = bookType.getElementDecl("title");
        annotations = title.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(0, jdoContent.size());
    }

    /**
     * Test tries to unmarshall non-jdo specific content in appinfo elements
     * within a complete annoted schema.<br>
     * <br>
     * Action: Parse an annoted schema.<br>
     * Precondition: schema-entity-mixed.xml holds a correct annoted schema
     * with NO JDO content, but encapsulated JDO elements.<br>
     * Postcondition: The complexType bookType, isbn and title have no
     * jdo-specific content in it's annotations.
     * @throws IOException 
     * @throws SAXException 
     */
    public void testUnmarshallMixedSchema() throws SAXException, IOException {
        Schema schema = unmarshalSchema("schema-entity-mixed.xsd");
        ComplexType bookType = schema.getComplexType("bookType");
        Enumeration annotations = bookType.getAnnotations();
        Annotation annotation = (Annotation) annotations.nextElement();
        Enumeration appInfos = annotation.getAppInfo();
        AppInfo appInfo = (AppInfo) appInfos.nextElement();
        List jdoContent = appInfo.getJdoContent();

        assertEquals(0, jdoContent.size());

        ElementDecl isbn = bookType.getElementDecl("isbn");
        annotations = isbn.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(0, jdoContent.size());

        ElementDecl title = bookType.getElementDecl("title");
        annotations = title.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(0, jdoContent.size());
    }
    
    /**
     * Test tries to unmarshall jdo specific content in appinfo elements within
     * a complete annoted schema.<br>
     * <br>
     * Action: Parse an annoted schema.<br>
     * Precondition: schema-one-to-many.xml holds a correct annoted schema with
     * appinfo content.<br>
     * Postcondition: The complexType bookType holds a table object in it's
     * annotations. The element declaration of isbn holds a column object. The
     * element declaration of title also holds a column object. The element 
     * declaration of author holds a one-to-many object.
     * @throws IOException 
     * @throws SAXException 
     */
    public void testUnmarshallOneToManySchema() throws SAXException, IOException {
        Schema schema = unmarshalSchema("schema-one-to-many.xsd");
        ComplexType bookType = schema.getComplexType("bookType");
        Enumeration annotations = bookType.getAnnotations();
        Annotation annotation = (Annotation) annotations.nextElement();
        Enumeration appInfos = annotation.getAppInfo();
        AppInfo appInfo = (AppInfo) appInfos.nextElement();
        List jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        Table t = (Table) jdoContent.get(0);
        assertEquals("book", t.getName());
        assertEquals("isbn", t.getPrimaryKey().getKey(0));

        ElementDecl isbn = bookType.getElementDecl("isbn");
        annotations = isbn.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        Column c = (Column) jdoContent.get(0);
        assertEquals("isbn", c.getName());
        assertEquals("varchar", c.getType());

        ElementDecl title = bookType.getElementDecl("title");
        annotations = title.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        c = (Column) jdoContent.get(0);
        assertEquals("title", c.getName());
        assertEquals("varchar", c.getType());
        
        ElementDecl author = bookType.getElementDecl("author");
        annotations = author.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        OneToMany oneToMany = (OneToMany) jdoContent.get(0);
        assertEquals("author_id", oneToMany.getName());
        assertTrue(oneToMany.isReadOnly());
        assertTrue(oneToMany.isDirty());
    }
    
    /**
     * This test tries to instantiate a Table object via reflection.
     */
    public void testDomainObjectReflectionInstantiation() {
        try {
            Table table = (Table) Class.forName(
                    "org.exolab.castor.xml.schema.annotations.jdo.Table")
                    .newInstance();
            table.setName("book");
            assertEquals("book", table.getName());
        } catch (InstantiationException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Test tries to unmarshall jdo specific content in appinfo elements within
     * a complete annoted schema.<br>
     * <br>
     * Action: Parse an annoted schema.<br>
     * Precondition: schema-entity.xml holds a correct annoted schema with
     * appinfo content.<br>
     * Postcondition: The complexType bookType holds a table object in it's
     * annotations. The element declaration of isbn holds a column object. The
     * element declaration of title also holds a column object.
     * @throws IOException 
     * @throws SAXException 
     */
    public void testUnmarshallOneToOneSchema() throws SAXException, IOException {
        Schema schema = unmarshalSchema("schema-one-to-one.xsd");
        ComplexType bookType = schema.getComplexType("bookType");
        Enumeration annotations = bookType.getAnnotations();
        Annotation annotation = (Annotation) annotations.nextElement();
        Enumeration appInfos = annotation.getAppInfo();
        AppInfo appInfo = (AppInfo) appInfos.nextElement();
        List jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        Table t = (Table) jdoContent.get(0);
        assertEquals("book", t.getName());
        assertEquals("isbn", t.getPrimaryKey().getKey(0));

        ElementDecl isbn = bookType.getElementDecl("isbn");
        annotations = isbn.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        Column c = (Column) jdoContent.get(0);
        assertEquals("isbn", c.getName());
        assertEquals("varchar", c.getType());

        ElementDecl title = bookType.getElementDecl("title");
        annotations = title.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        c = (Column) jdoContent.get(0);
        assertEquals("title", c.getName());
        assertEquals("varchar", c.getType());
        
        ElementDecl author = bookType.getElementDecl("author");
        annotations = author.getAnnotations();
        annotation = (Annotation) annotations.nextElement();
        appInfos = annotation.getAppInfo();
        appInfo = (AppInfo) appInfos.nextElement();
        jdoContent = appInfo.getJdoContent();

        assertEquals(1, jdoContent.size());
        OneToOne oneToOne = (OneToOne) jdoContent.get(0);
        assertEquals("author_id", oneToOne.getName());
        assertTrue(oneToOne.isDirty());
        assertTrue(oneToOne.isReadOnly());
        
        
    }

    /**
     * Helper method that (manually) unmarshals an XML schema and returns a 
     * {@link Schema} object instance. 
     * @param schemaName Name of the XML schema to be unmarshalled.
     * @return {@link Schema} object instance.
     * @throws SAXException When a problem with XML data binding occurs.
     * @throws IOException When the XML schema file cannot be accessed.
     */
    private Schema unmarshalSchema(final String schemaName) throws SAXException, IOException {
        Parser parser = null;
        InternalContext internalContext = new BackwardCompatibilityContext();

        try {
            parser = internalContext.getParser();
        } catch (RuntimeException rte) {
            fail("Can't optain sax parser!");
        }

        if (parser == null) {
            fail("Unable to create SAX parser.");
        }

        SchemaContext schemaContext = new SchemaContextImpl();
        SchemaUnmarshaller schemaUnmarshaller = null;
        try {
            schemaUnmarshaller = new SchemaUnmarshaller(schemaContext);
        } catch (XMLException e) {
            fail(e.getMessage());
        }

        Sax2ComponentReader handler = new Sax2ComponentReader(
                schemaUnmarshaller);
        parser.setDocumentHandler(handler);
        parser.setErrorHandler(handler);

        parser.parse(new InputSource(getClass().getResource(schemaName).toExternalForm()));

        Schema schema = schemaUnmarshaller.getSchema();
        return schema;
    }
    
}

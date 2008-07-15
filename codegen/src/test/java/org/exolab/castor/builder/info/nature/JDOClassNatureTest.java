/*
 * Copyright 2008 Tobias Hochwallner
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.exolab.castor.builder.factory.FieldInfoFactory;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.XMLInfo;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.javasource.JClass;

/**
 * Tests access to {@link ClassInfo} properties via {@link JDOClassInfoNature}. Remember that
 * behavior of properties not set before is specified in the {@link BaseNature}
 * class and tested in {@link BaseNatureTest}. Property implementation will not
 * be tested in here.
 * 
 * @author Tobias Hochwallner, Lukas Lang
 * @since 1.2.1
 */
public final class JDOClassNatureTest extends TestCase {

    /**
     * Constructor.
     * 
     * @param name
     *            of the test case.
     */
    public JDOClassNatureTest(final String name) {
        super(name);
    }

    /**
     * Shows the usage of JDOClassNature.
     */
    public void testUsage() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        if (!classInfo.hasNature(JDOClassInfoNature.class.getName())) {
            classInfo.addNature(JDOClassInfoNature.class.getName());
            JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);
            jdo.setTableName("BOOK");
            jdo.addPrimaryKey("ISBN");
            // TODO Tobias jdo.addPrimaryKey("ISBN","Generator");
            assertEquals("BOOK", jdo.getTableName());
            assertEquals(1, jdo.getPrimaryKeys().size());
            assertTrue(jdo.getPrimaryKeys().contains("ISBN"));
        }
    }

    /**
     * Tests set and get JDO table name.
     */
    public void testTableName() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);
        jdo.setTableName("BOOK");
        assertEquals("BOOK", jdo.getTableName());
    }

    /**
     * Tests set and get primary keys. Adding the author column to the primary
     * key in reality would net really make sense.
     */
    public void testPrimaryKeys() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List columns = new LinkedList();
        columns.add("ISBN");
        columns.add("AUTHOR");

        jdo.addPrimaryKey(columns.get(0).toString());
        assertEquals(1, jdo.getPrimaryKeys().size());

        assertTrue(jdo.getPrimaryKeys().contains(columns.get(0)));

        jdo.addPrimaryKey(columns.get(1).toString());

        List primaryKey = jdo.getPrimaryKeys();
        assertEquals(2, jdo.getPrimaryKeys().size());

        assertTrue(primaryKey.containsAll(columns));
    }

    /**
     * Tests set and get {@link AccessMode} of the JDO entity.
     */
    public void testAccessMode() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);
        jdo.setAccessMode(AccessMode.Shared);
        assertNotNull(jdo.getAccessMode());
        assertEquals(AccessMode.Shared, jdo.getAccessMode());
    }

    /**
     * Tests if getFields returns a list of {@link JDOFieldInfoNature}s.
     */
    public void testGetElementFields() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo field = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "isbn");
        field.addNature(JDOFieldInfoNature.class.getName());
        JDOFieldInfoNature jdoField = new JDOFieldInfoNature(field);
        jdoField.setColumnName("isbn");
        jdoField.setColumnType("integer");
        classInfo.addFieldInfo(field);
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List jdoFields = jdo.getFields();
        assertEquals(1, jdoFields.size());
        JDOFieldInfoNature jdoFieldReceived = (JDOFieldInfoNature) jdoFields
                .get(0);
        assertEquals("integer", jdoFieldReceived.getColumnType());
        assertEquals("isbn", jdoFieldReceived.getColumnName());
    }
    
    /**
     * Tests if getFields returns a list of {@link JDOFieldInfoNature}s.
     */
    public void testGetElementFieldsWithMoreThanOneField() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo isbn = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "isbn");
        FieldInfo title = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "title");
        isbn.addNature(JDOFieldInfoNature.class.getName());
        title.addNature(JDOFieldInfoNature.class.getName());
        JDOFieldInfoNature jdoIsbn = new JDOFieldInfoNature(isbn);
        JDOFieldInfoNature jdoTitle = new JDOFieldInfoNature(title);
        jdoIsbn.setColumnName("isbn");
        jdoIsbn.setColumnType("integer");
        jdoTitle.setColumnName("title");
        jdoTitle.setColumnType("varchar");
        
        classInfo.addFieldInfo(isbn);
        classInfo.addFieldInfo(title);
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List jdoFields = jdo.getFields();
        assertEquals(2, jdoFields.size());
        
        JDOFieldInfoNature jdoFieldReceived = (JDOFieldInfoNature) jdoFields
                .get(0);
        
        // We can not depend on the order of the received Natures.
        if (jdoFieldReceived.getColumnName().equals("isbn")) {
            assertEquals("integer", jdoFieldReceived.getColumnType());
            // Now check the second field
            jdoFieldReceived = (JDOFieldInfoNature) jdoFields.get(1);
            assertEquals("title", jdoFieldReceived.getColumnName());
            assertEquals("varchar", jdoFieldReceived.getColumnType());
        } else if (jdoFieldReceived.getColumnName().equals("title")) {
            assertEquals("varchar", jdoFieldReceived.getColumnType());
            // Now check the second field
            jdoFieldReceived = (JDOFieldInfoNature) jdoFields.get(1);
            assertEquals("isbn", jdoFieldReceived.getColumnName());
            assertEquals("integer", jdoFieldReceived.getColumnType());
        } else {
            fail("Fields not found!");
        }
    }

    /**
     * Test getFields with a ClassInfo having a FieldInfo but without
     * JDOFieldInfoNature.
     */
    public void testGetElementFieldsWithoutNatures() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo field = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "isbn");
        classInfo.addFieldInfo(field);
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List jdoFields = jdo.getFields();
        assertEquals(0, jdoFields.size());
    }

    /**
     * Test getFields with a ClassInfo no fields added before.
     */
    public void testGetElementFieldsNoFieldsAdded() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List jdoFields = jdo.getFields();
        assertEquals(0, jdoFields.size());
    }
    
    /**
     * Tests if getFields returns a list of {@link JDOFieldInfoNature}s.
     */
    public void testGetElementFieldsOnlyOneHasNature() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo field = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "isbn");
        FieldInfo field2 = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "abc");
        field.addNature(JDOFieldInfoNature.class.getName());
        JDOFieldInfoNature jdoField = new JDOFieldInfoNature(field);
        jdoField.setColumnName("isbn");
        jdoField.setColumnType("integer");
        classInfo.addFieldInfo(field);
        classInfo.addFieldInfo(field2);
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List jdoFields = jdo.getFields();
        assertEquals(1, jdoFields.size());
        JDOFieldInfoNature jdoFieldReceived = (JDOFieldInfoNature) jdoFields
                .get(0);
        assertEquals("integer", jdoFieldReceived.getColumnType());
        assertEquals("isbn", jdoFieldReceived.getColumnName());
    }
    
    /**
     * Tests if getFields returns a list of {@link JDOFieldInfoNature}s.
     */
    public void testGetAttributeTextElementFields() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        FieldInfoFactory factory = new FieldInfoFactory();
        
        FieldInfo elementField = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "isbn");
        // Set node type
        elementField.addNature(XMLInfoNature.class.getName());
        new XMLInfoNature(elementField).setNodeType(XMLInfo.ELEMENT_TYPE);
        // Set column name
        elementField.addNature(JDOFieldInfoNature.class.getName());
        JDOFieldInfoNature jdoField = new JDOFieldInfoNature(elementField);
        jdoField.setColumnName("isbn");
        classInfo.addFieldInfo(elementField);
        
        FieldInfo attributeField = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "title");
        // Set node type
        attributeField.addNature(XMLInfoNature.class.getName());
        new XMLInfoNature(attributeField).setNodeType(XMLInfo.ATTRIBUTE_TYPE);
        // Set column name
        attributeField.addNature(JDOFieldInfoNature.class.getName());
        jdoField = new JDOFieldInfoNature(attributeField);
        jdoField.setColumnName("title");
        classInfo.addFieldInfo(attributeField);
        
        FieldInfo textField = factory.createFieldInfo(new XSClass(
                new JClass("Book")), "price");
        // Set node type
        textField.addNature(XMLInfoNature.class.getName());
        new XMLInfoNature(textField).setNodeType(XMLInfo.TEXT_TYPE);
        // Set column name
        textField.addNature(JDOFieldInfoNature.class.getName());
        jdoField = new JDOFieldInfoNature(textField);
        jdoField.setColumnName("price");
        classInfo.addFieldInfo(textField);
        
        // Add JDO Nature to ClassInfo.
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List jdoFields = jdo.getFields();
        assertEquals(3, jdoFields.size());

        boolean containsElementField = false;
        boolean containsAttributeField = false;
        boolean containsTextField = false;
        
        for (Iterator fieldIt = jdoFields.iterator(); fieldIt.hasNext();) {
            JDOFieldInfoNature nature = (JDOFieldInfoNature) fieldIt.next();
            if (nature.getColumnName().equals("isbn")) {
                containsElementField = true;
            } else if (nature.getColumnName().equals("title")) {
                containsAttributeField = true;
            } else if (nature.getColumnName().equals("price")) {
                containsTextField = true;
            }
        }
        
        assertTrue(containsElementField);
        assertTrue(containsAttributeField);
        assertTrue(containsTextField);
    }
    
    
    
    
}

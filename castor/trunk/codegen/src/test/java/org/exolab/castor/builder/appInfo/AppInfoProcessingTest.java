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
 */
package org.exolab.castor.builder.appInfo;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;

import org.exolab.castor.builder.SGStateInfo;
import org.exolab.castor.builder.ExtendedSourceGenerator;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.nature.JDOClassInfoNature;
import org.exolab.castor.builder.info.nature.JDOFieldInfoNature;
import org.exolab.javasource.JClass;
import org.xml.sax.InputSource;

/**
 * Test case for testing processing appinfo elements and storing their values
 * in {@link ClassInfo}/FieldInfo instances using Natures.
 *  
 * @author Lukas Lang
 * @since 1.2.1 
 */
public class AppInfoProcessingTest extends TestCase {
    
    private ExtendedSourceGenerator _generator;
    private String _xmlSchema;

    public final void setUp() throws Exception {
        super.setUp();
        _generator = new ExtendedSourceGenerator();
        _generator.setDestDir("./codegen/src/test/java");
        _generator.setSuppressNonFatalWarnings(true);

        // uncomment to use Velocity for code generation
        // generator.setJClassPrinterType("velocity");

        // uncomment the next line to set a binding file for source generation
        // generator.setBinding(new InputSource(getClass().getResource(
        // "binding.xml").toExternalForm()));

        // uncomment the next lines to set custom properties for source
        // generation
        // Properties properties = new Properties();
        // properties.load(getClass().getResource("builder.properties").openStream());
        // generator.setDefaultProperties(properties);
    }

    public final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This method tests the processing of AppInfos from a very basic Schema
     * file. The Schema file contains a single <code>element</code> definition
     * and doesen't contain a top-level, named complex type. The element
     * doesen't have any attributes either.
     * 
     * @throws Exception
     *             if an error occurs.
     */
    public final void testAppInfoProcessingWithSimpleSchema() throws Exception {
        _xmlSchema = getClass().getResource("simple.xsd").toExternalForm();
        InputSource inputSource = new InputSource(_xmlSchema);
        _generator.generateSource(inputSource, getClass().getPackage()
                .getName()
                + ".generated.simple");

        // get the Source Generator's state Info
        SGStateInfo sgState = _generator.getSGStateInfo();

        assertNotNull(sgState);

        /*
         * Get all elements (=keys) the ClassInfoResolver knows of. For these
         * keys the SourceGenerator created ClassInfos in which we are
         * interested.
         */
        Enumeration enumeration = sgState.keys();

        assertTrue(enumeration.hasMoreElements());

        List cInfos = new ArrayList();
        /*
         * Get all ClassInfos. Note that during the source generation process
         * ClassInfos are actually added twice to the ClassInfoResolver's cache: 
         * - once with the XMLBindingComponent/ClassInfo 
         * - and with the JClass/ClassInfo 
         * as key/value pair.
         * Therefore we only get those ClassInfos with a 
         * XMLBindingComponent/ClassInfo pair to avoid duplicates!
         */
        while (enumeration.hasMoreElements()) {
            Object elem = enumeration.nextElement();
            if (!(elem instanceof JClass)) {
                cInfos.add(sgState.resolve(elem));
            }
        }

        assertEquals(1, cInfos.size());

        ClassInfo cInfo = (ClassInfo) cInfos.get(0);

        assertEquals("father", cInfo.getNodeName());

        JDOClassInfoNature cNature = new JDOClassInfoNature(cInfo);

        assertEquals("Father", cNature.getTableName());

        List primaryKeys = cNature.getPrimaryKeys();

        assertEquals(1, primaryKeys.size());
        assertEquals("ssnr", primaryKeys.get(0));

        ///////// FieldInfo
        /*
         *  There should be 3 FieldInfos:
         *  - ssnr
         *  - firstName
         *  - lastName 
         */
        assertEquals(3, cInfo.getFieldCount());

        FieldInfo fInfo = cInfo.getElementField("ssnr");

        assertNotNull(fInfo);

        JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);

        assertEquals("ssnr", fNature.getColumnName());
        assertEquals("integer", fNature.getColumnType());

        fInfo = cInfo.getElementField("firstName");

        assertNotNull(fInfo);

        fNature = new JDOFieldInfoNature(fInfo);

        assertEquals("firstName", fNature.getColumnName());
        assertEquals("varchar", fNature.getColumnType());

        fInfo = cInfo.getElementField("lastName");

        assertNotNull(fInfo);

        fNature = new JDOFieldInfoNature(fInfo);

        assertEquals("lastName", fNature.getColumnName());
        assertEquals("varchar", fNature.getColumnType());
        
        try {
            assertNull(cInfos.get(1));
            fail("Unexpected ClassInfo Element encountered!");
        } catch (IndexOutOfBoundsException e) {
            
        }
    }

    /**
     * This method tests the processing of AppInfos from a Schema that describes
     * a Book. The Schema consists of a global element whose type is described by
     * a global <code>complexType</code> definition consisting of two sub-elements
     * and an attribute. 
     * 
     * @throws Exception
     *             if an error occurs
     */
    public final void testAppInfoProcessingWithEntitySchema() throws Exception {
        _xmlSchema = getClass().getResource("schema-entity.xsd").toExternalForm();
        InputSource inputSource = new InputSource(_xmlSchema);
        _generator.generateSource(inputSource, getClass().getPackage()
                .getName()
                + ".generated.entity");
        
     // get the Source Generator's state Info
        SGStateInfo sgState = _generator.getSGStateInfo();

        assertNotNull(sgState);

        /*
         * Get all elements (=keys) the ClassInfoResolver knows of. For these
         * keys the SourceGenerator created ClassInfos in which we are
         * interested.
         */
        Enumeration enumeration = sgState.keys();

        assertTrue(enumeration.hasMoreElements());

        List cInfos = new ArrayList();
        /*
         * Get all ClassInfos. Note that during the source generation process
         * ClassInfos are actually added twice to the ClassInfoResolver's cache: 
         * - once with the XMLBindingComponent/ClassInfo 
         * - and with the JClass/ClassInfo 
         * as key/value pair.
         * Therefore we only get those ClassInfos with a 
         * XMLBindingComponent/ClassInfo pair to avoid duplicates!
         */
        while (enumeration.hasMoreElements()) {
            Object elem = enumeration.nextElement();
            if (!(elem instanceof JClass)) {
                cInfos.add(sgState.resolve(elem));
            }
        }

        assertEquals(2, cInfos.size());
        
        for (int i = 0; i < cInfos.size(); ++i) {
            ClassInfo cInfo = (ClassInfo) cInfos.get(i);

            assertNotNull(cInfo);

            if (cInfo.getNodeName().equals("book")) {
                /*
                 * No JDO-specific information should be stored to this
                 * ClassInfo.
                 */
                assertFalse(cInfo.hasNature(JDOClassInfoNature.class.getName()));
//                JDOClassNature cNature = new JDOClassNature(cInfo);
//                
//                assertEquals(null, cNature.getTableName());
//                
//                List primaryKeys = cNature.getPrimaryKeys();
//                
//                assertNull(primaryKeys);
                
                assertEquals(0, cInfo.getFieldCount());
                
            } else if (cInfo.getNodeName().equals("bookType")) {
                JDOClassInfoNature cNature = new JDOClassInfoNature(cInfo);
                
                assertEquals("book", cNature.getTableName());
                
                List primaryKeys = cNature.getPrimaryKeys();
                
                assertEquals(1, primaryKeys.size());
                assertEquals("isbn", (String) primaryKeys.get(0));
                
                ///////// FieldInfo
                /*
                 *  There should be 3 FieldInfos:
                 *  - coverType (attribute field)
                 *  - isbn
                 *  - title 
                 */
                assertEquals(3, cInfo.getFieldCount());

                // FieldInfo for coverType
                FieldInfo fInfo = cInfo.getAttributeField("coverType");
                
                assertNotNull(fInfo);
                
                fInfo.addNature(JDOFieldInfoNature.class.getName());
                JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
                
                assertEquals("cover_type", fNature.getColumnName());
                assertEquals("varchar", fNature.getColumnType());
                
                // FieldInfo for isbn                
                fInfo = cInfo.getElementField("isbn");
                
                assertNotNull(fInfo);
                
                fNature = new JDOFieldInfoNature(fInfo);
                
                assertEquals("isbn", fNature.getColumnName());
                assertEquals("varchar", fNature.getColumnType());
                
                // FieldInfo for title
                fInfo = cInfo.getElementField("title");
                
                assertNotNull(fInfo);
                
                fNature = new JDOFieldInfoNature(fInfo);
                
                assertEquals("title", fNature.getColumnName());
                assertEquals("varchar", fNature.getColumnType());
            } else {
                fail("Unexpected ClassInfo Element encountered!");
            }
        }
    }

    public final void testAppInfoProcessingNoJDOContent() throws Exception {
        _xmlSchema = getClass().getResource("schema-entity-non-jdo.xsd").toExternalForm();
        InputSource inputSource = new InputSource(_xmlSchema);
        _generator.generateSource(inputSource, getClass().getPackage()
                .getName()
                + ".generated.noJDO");

        // get the Source Generator's state Info
        SGStateInfo sgState = _generator.getSGStateInfo();

        assertNotNull(sgState);
        
        /*
         * Get all elements (=keys) the ClassInfoResolver knows of. For these
         * keys the SourceGenerator created ClassInfos in which we are
         * interested.
         */
        Enumeration enumeration = sgState.keys();

        assertTrue(enumeration.hasMoreElements());

        List cInfos = new ArrayList();
        /*
         * Get all ClassInfos. Note that during the source generation process
         * ClassInfos are actually added twice to the ClassInfoResolver's cache: 
         * - once with the XMLBindingComponent/ClassInfo 
         * - and with the JClass/ClassInfo 
         * as key/value pair.
         * Therefore we only get those ClassInfos with a 
         * XMLBindingComponent/ClassInfo pair to avoid duplicates!
         */
        while (enumeration.hasMoreElements()) {
            Object elem = enumeration.nextElement();
            if (!(elem instanceof JClass)) {
                cInfos.add(sgState.resolve(elem));
            }
        }
        
        for (int i = 0; i < cInfos.size(); ++i) {
            ClassInfo cInfo = (ClassInfo) cInfos.get(i);
            
            assertFalse(cInfo.hasNature(JDOClassInfoNature.class.getName()));

//            /*
//             * No JDO-specific information should be stored to the
//             * ClassInfo.
//             */
//            JDOClassNature cNature = new JDOClassNature(cInfo);
//            
//            assertNull(cNature.getTableName());
//            
//            List primaryKeys = cNature.getPrimaryKeys();
//            
//            assertNull(primaryKeys);
//            
//            // check that the FieldInfos do not contain any JDO-specific
//            // informations.
//            if (cInfo.getNodeName().equals("book")) {
//                assertEquals(0, cInfo.getFieldCount());
//            } else if (cInfo.getNodeName().equals("bookType")) {
//                assertEquals(2, cInfo.getFieldCount());
//                
//                FieldInfo fInfo = cInfo.getElementField("isbn");
//                
//                assertNotNull(fInfo);
//                
//                JDOFieldNature fNature = new JDOFieldNature(fInfo);
//                
//                assertNotNull(fNature);
//                
//                assertNull(fNature.getColumnName());
//                assertNull(fNature.getColumnType());
//                
//                fInfo = cInfo.getElementField("title");
//                
//                assertNotNull(fInfo);
//                
//                fNature = new JDOFieldNature(fInfo);
//                
//                assertNotNull(fNature);
//                
//                assertNull(fNature.getColumnName());
//                assertNull(fNature.getColumnType());
//            } else {
//                fail("Unexpected ClassInfo Element encountered!");
//            }
        }
    }
    
    public final void testAppInfoProcessingWithComplexContent() throws Exception {
        _xmlSchema = getClass().getResource("complex-content.xsd").toExternalForm();
        InputSource inputSource = new InputSource(_xmlSchema);
        _generator.generateSource(inputSource, getClass().getPackage()
                .getName()
                + ".generated.complexContent");

        // get the Source Generator's state Info
        SGStateInfo sgState = _generator.getSGStateInfo();

        assertNotNull(sgState); 
        
        /*
         * Get all elements (=keys) the ClassInfoResolver knows of. For these
         * keys the SourceGenerator created ClassInfos in which we are
         * interested.
         */
        Enumeration enumeration = sgState.keys();

        assertTrue(enumeration.hasMoreElements());

        List cInfos = new ArrayList();
        /*
         * Get all ClassInfos. Note that during the source generation process
         * ClassInfos are actually added twice to the ClassInfoResolver's cache: 
         * - once with the XMLBindingComponent/ClassInfo 
         * - and with the JClass/ClassInfo 
         * as key/value pair.
         * Therefore we only get those ClassInfos with a 
         * XMLBindingComponent/ClassInfo pair to avoid duplicates!
         */
        while (enumeration.hasMoreElements()) {
            Object elem = enumeration.nextElement();
            if (!(elem instanceof JClass)) {
                cInfos.add(sgState.resolve(elem));
            }
        }
        
        assertEquals(2, cInfos.size());
        
        for (int i = 0; i < cInfos.size(); ++i) {
            ClassInfo cInfo = (ClassInfo) cInfos.get(i);
            
            assertNotNull(cInfo);
            
            JDOClassInfoNature cNature = new JDOClassInfoNature(cInfo);
            
            assertNotNull(cNature);
            
            if (cInfo.getNodeName().equals("person")) {
                String tableName = cNature.getTableName();
                assertEquals("person", tableName);
                
                List primaryKeys = cNature.getPrimaryKeys();
                
                assertEquals(1, primaryKeys.size());
                assertEquals("ssn", (String) primaryKeys.get(0));
                
                assertEquals(4, cInfo.getFieldCount());
                
                FieldInfo[] fInfos = cInfo.getElementFields();
                
                assertNotNull(fInfos);
                
                for (int j = 0; j < fInfos.length; ++j) {
                    FieldInfo fInfo = fInfos[j];
                    assertNotNull(fInfo);
                    JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
                    assertNotNull(fNature);
                    
                    String columnName = fNature.getColumnName();
                    String columnType = fNature.getColumnType();
                    
                    if (fInfo.getNodeName().equals("ssn")) {    
                        assertEquals("ssn", columnName);
                        assertEquals("bigint", columnType);
                    } else if (fInfo.getNodeName().equals("firstName")) {
                        assertEquals("firstName", columnName);
                        assertEquals("varchar", columnType);
                    } else if (fInfo.getNodeName().equals("lastName")) {
                        assertEquals("lastName", columnName);
                        assertEquals("varchar", columnType);
                    } else if (fInfo.getNodeName().equals("birthdate")) {
                        assertEquals("birthdate", columnName);
                        assertEquals("date", columnType);
                    } else {
                        fail("Unexpected FieldInfo Element encountered!");
                    }
                }
            } else if (cInfo.getNodeName().equals("insurant")) {
                String tableName = cNature.getTableName();
                assertEquals("insurant", tableName);
                
                List primaryKeys = cNature.getPrimaryKeys();
                
                assertEquals(2, primaryKeys.size());
                assertEquals("ssn", (String) primaryKeys.get(0));
                assertEquals("policyNumber", (String) primaryKeys.get(1));
                
                assertEquals(1, cInfo.getFieldCount());
                
                FieldInfo fInfo = cInfo.getElementField("policyNumber");
                
                assertNotNull(fInfo);
                
                JDOFieldInfoNature fNature = new JDOFieldInfoNature(fInfo);
                
                assertNotNull(fNature);
                
                String columnName = fNature.getColumnName();
                assertEquals("policyNumber", columnName);
                String columnType = fNature.getColumnType();
                assertEquals("bigint", columnType);
            } else {
                fail("Unexpected ClassInfo Element encountered!");
            }
        }
    }
}
/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.test.framework;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.castor.ddlgen.AbstractGenerator;
import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.DDLWriter;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.Mapping;

/**
 * This class handles all testcase for all database. The specific database will
 * inherite this class. Expecting that all testcase use the same scenarios for
 * all database. The expected results may differ. The engine defines which expected
 * result will be loaded to the testcase. The inherited class may redefine this
 * variable to reuse the test scenarios.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public abstract class AbstractGeneratorTest extends TestCase {
    //--------------------------------------------------------------------------

    /** Path of the expected result pattern files. */
    private static final String EXPECTED_PATH = "../expected/";
    
    /** Path of the test mapping files. */
    private static final String MAPPING_PATH = "../mapping/";

    /** Default parameter prefix. */
    protected static final String PARAM_PREFIX = "default_";

    /** Prostfix of length parameters for types in ddl.properties file. */
    protected static final String PARAM_LENGTH = "_length";

    /** Prostfix of precision parameters for types in ddl.properties file. */
    protected static final String PARAM_PRECISION = "_precision";

    /** Prostfix of decimals parameters for types in ddl.properties file. */
    protected static final String PARAM_DECIMALS = "_decimals";

    //--------------------------------------------------------------------------

    /** Database engine. */
    private String _engine = Expected.ENGINE_GENERIC;

    /** DDL generator. */
    private AbstractGenerator _generator;

    /** Expected result pattern. */
    private Expected _expected;

    //--------------------------------------------------------------------------

    /**
     * Constructor for BaseGeneratorTest
     * 
     * @param testcase
     *            test case
     */
    public AbstractGeneratorTest(final String testcase) {
        super(testcase);
    }

    //--------------------------------------------------------------------------

    /**
     * Get database engine.
     * 
     * @return Database engine.
     */
    public final String getEngine() {
        return _engine;
    }

    /**
     * Set database engine.
     * 
     * @param engine atabase engine.
     */
    public final void setEngine(final String engine) {
        _engine = engine;
    }

    /**
     * Get DDL generator.
     * 
     * @return DDL generator.
     */
    public final AbstractGenerator getGenerator() {
        return _generator;
    }

    /**
     * Set DDL generator.
     * 
     * @param generator DDL generator.
     */
    public final void setGenerator(final AbstractGenerator generator) {
        _generator = generator;
    }
    
    /**
     * Get expected result pattern.
     * 
     * @return Expected result pattern.
     */
    public final Expected getExpected() {
        return _expected;
    }

    /**
     * Set expected result pattern.
     * 
     * @param expected Expected result pattern.
     */
    public final void setExpected(final Expected expected) {
        _expected = expected;
    }

    //--------------------------------------------------------------------------
    
    /**
     * Load test mapping file and expected result pattern file with given filename.
     * 
     * @param filename Name of test mapping and expected result pattern files.
     * @throws Exception If any exception occured during unmarshalling of expected result
     *         patterns, loading of test mapping or creation of schema objects.
     */
    protected final void loadData(final String filename) throws Exception {
        Class cls = AbstractGeneratorTest.class;
        
        URL mappingURL = cls.getResource(MAPPING_PATH + filename);
        Mapping mapping = new Mapping();
        mapping.loadMapping(mappingURL);
        new MappingUnmarshaller().loadMappingOnly(mapping);
        // TODO: Joachim 2007-09-07 the InternalContext should be set into the unmarshaller!
        _generator.setMapping(mapping);
        _generator.createSchema();

        URL expectedURL = cls.getResource(EXPECTED_PATH + filename);
        _expected = Expected.getExpectedResult(expectedURL);
        _expected.setConf(_generator.getConfiguration());
    }

    //--------------------------------------------------------------------------

    /**
     * Create DDL script for one table.
     */
    public final void testSingleTable() {
        try {
            loadData("single_table.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getStringValue(PARAM_PREFIX + "integer" + PARAM_PRECISION, ""),
                    conf.getStringValue(PARAM_PREFIX + "char" + PARAM_LENGTH, "") };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testSingleTable: " + e.getMessage());
        }
    }

    /**
     * Ignore class mapping without table definition.
     */
    public final void testIgnoredTable() {
        try {
            loadData("ignored_table.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testIgnoredTable: " + e.getMessage());
        }
    }

    /**
     * Mapping contains no class definitions..
     */
    public final void testNoTable() {
        try {
            loadData("no_table.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testNoTable: " + e.getMessage());
        }
    }

    /**
     * Create drop table DDL script.
     */
    public final void testDropTable() {
        try {
            loadData("drop_table.xml");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, _generator.getConfiguration());
            
            _generator.generateDrop(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testDropTable: " + e.getMessage());
        }
    }

    /**
     * Identity is defined at class tag.
     */
    public final void testClassId() {
        try {
            loadData("class_id.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generatePrimaryKey(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testClassId: " + e.getMessage());
        }
    }

    /**
     * Identity is defined at class tag and there are multiple identities.
     */
    public final void testClassMultipleId() {
        try {
            loadData("class_multiple_id.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generatePrimaryKey(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, null);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testClassMultipleId: " + e.getMessage());
        }
    }

    /**
     * Identity is defined at field tag.
     */
    public final void testFieldId() {
        try {
            loadData("field_id.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generatePrimaryKey(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testFieldId: " + e.getMessage());
        }
    }

    /**
     * Identity is defined at field tag and there are multiple identities.
     */
    public final void testFieldMultipleId() {
        try {
            loadData("field_multiple_id.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generatePrimaryKey(writer);
            
            writer.close();
            ddl = out.toString();

            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testFieldMultipleId: " + e.getMessage());
        }
    }

    /**
     * Missing identity definition.
     */
    public final void testNoId() {
        try {
            loadData("no_id.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generatePrimaryKey(writer);
            
            writer.close();
            ddl = out.toString();

            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testNoId: " + e.getMessage());
        }
    }

    /**
     * Create many tables from one mapping.
     */
    public final void testMultipleTable() {
        try {
            loadData("multiple_table.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testMultipleTable: " + e.getMessage());
        }
    }

    /**
     * Create a table with 23 fields representing to each possible data type.
     */
    public final void testSingleFieldForAll() {
        try {
            loadData("single_field_for_all.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "tinyint" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "smallint" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "bigint" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "float" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "float" + PARAM_DECIMALS),
                    conf.getInteger(PARAM_PREFIX + "double" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "double" + PARAM_DECIMALS),
                    conf.getInteger(PARAM_PREFIX + "real" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "real" + PARAM_DECIMALS),
                    conf.getInteger(PARAM_PREFIX + "numeric" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "numeric" + PARAM_DECIMALS),
                    conf.getInteger(PARAM_PREFIX + "decimal" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "decimal" + PARAM_DECIMALS),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH),
                    conf.getInteger(PARAM_PREFIX + "varchar" + PARAM_LENGTH),
                    conf.getStringValue(PARAM_PREFIX + "longvarchar" + PARAM_LENGTH),
                    conf.getInteger(PARAM_PREFIX + "timestamp" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "binary" + PARAM_LENGTH),
                    conf.getStringValue(PARAM_PREFIX + "varbinary" + PARAM_LENGTH),
                    conf.getStringValue(PARAM_PREFIX + "longvarbinary" + PARAM_LENGTH),
                    conf.getInteger(PARAM_PREFIX + "time" + PARAM_PRECISION),
                    conf.getStringValue(PARAM_PREFIX + "bigint" + PARAM_DECIMALS),
                    conf.getStringValue(PARAM_PREFIX + "other" + PARAM_LENGTH),
                    conf.getStringValue(PARAM_PREFIX + "javaobject" + PARAM_LENGTH),
                    conf.getStringValue(PARAM_PREFIX + "blob" + PARAM_LENGTH),
                    conf.getStringValue(PARAM_PREFIX + "clob" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testSingleFieldForAll: " + e.getMessage());
        }
    }

    /**
     * Class with one single field only.
     */
    public final void testSingleField() {
        try {
            loadData("single_field.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testSingleField: " + e.getMessage());
        }
    }

    /**
     * Use identity key generator which means to set auto increment on identity column.
     */
    public final void testKeyGenIdentity() {
        try {
            loadData("key_gen_identity.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateKeyGenerator(writer);
            
            writer.close();
            ddl = out.toString();

            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testKeyGenIdentity: " + e.getMessage());
        }
    }

    /**
     * Use high-low key generator. No DDL will be created.
     */
    public final void testKeyGenHighLow() {
        try {
            loadData("key_gen_high-low.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateKeyGenerator(writer);
            
            writer.close();
            ddl = out.toString();

            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testKeyGenHighLow: " + e.getMessage());
        }
    }

    /**
     * Use MAX key generator. No DDL will be created.
     */
    public final void testKeyGenMax() {
        try {
            loadData("key_gen_max.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateKeyGenerator(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testKeyGenMax: " + e.getMessage());
        }
    }

    /**
     * Use sequence key generator. Only some databases support to create sequence and/or
     * trigger statement.
     */
    public final void testKeyGenSequence() {
        try {
            loadData("key_gen_sequence.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateKeyGenerator(writer);
            
            writer.close();
            ddl = out.toString();

            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testKeyGenSequence: " + e.getMessage());
        }
    }

    /**
     * Use UUID key generator. No DDL will be created.
     */
    public final void testKeyGenUUID() {
        try {
            loadData("key_gen_uuid.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateKeyGenerator(writer);
            
            writer.close();
            ddl = out.toString();

            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testKeyGenUUID: " + e.getMessage());
        }
    }

    /**
     * Mapping with one-one relationship. 
     */
    public final void testOneOneRelationship() {
        try {
            loadData("relationship_1_1.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateForeignKey(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testOneOneRelationship: " + e.getMessage());
        }
    }

    /**
     * Mapping with one-many relationship.
     */
    public final void testOneManyRelationship() {
        try {
            loadData("relationship_1_n.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateForeignKey(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testOneManyRelationship: " + e.getMessage());
        }
    }

    /**
     * Mapping with many-many relationship.
     */
    public final void testManyManyRelationship() {
        try {
            loadData("relationship_m_n.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, 0, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, conf);
            
            _generator.generateForeignKey(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testManyManyRelationship: " + e.getMessage());
        }
    }

    /**
     * 2 levels ID's references in a mapping. 
     */
    public final void test2LevelsReference() {
        try {
            loadData("2levels_reference.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getStringValue(PARAM_PREFIX + "integer" + PARAM_PRECISION, ""),
                    conf.getStringValue(PARAM_PREFIX + "char" + PARAM_LENGTH, "") };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("test2LevelsReference: " + e.getMessage());
        }
    }

    /**
     * Ignore field without sql mapping. 
     */
    public final void testIgnoredField() {
        try {
            loadData("ignored_field.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testIgnoredField: " + e.getMessage());
        }
    }

    /**
     * Table without a field.
     */
    public final void testNoField() {
        try {
            loadData("no_field.xml");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, _generator.getConfiguration());
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testNoField: " + e.getMessage());
        }
    }

    /**
     * Overwrite identity definition on class with definition on field.
     */
    public final void testOverwriteFieldId() {
        try {
            loadData("overwrite_field_id.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testOverwriteFieldId: " + e.getMessage());
        }
    }

    /**
     * Relationship with multpile key reference. Identity defined at class.
     */
    public final void testManyKeysReference() {
        try {
            loadData("many_keys_reference.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testManyKeysReference: " + e.getMessage());
        }
    }

    /**
     * Relationship with multpile key reference. Identity defined at field.
     */
    public final void testManyClassKeysReference() {
        try {
            loadData("many_class_keys_reference.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();
            Object[] params = new Object[] {
                    conf.getInteger(PARAM_PREFIX + "integer" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "char" + PARAM_LENGTH) };

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            _generator.generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testManyClassKeysReference: " + e.getMessage());
        }
    }

    /**
     * Create schema.
     */
    public final void testCreateSchema() {
        try {
            loadData("create_schema.xml");

            DDLGenConfiguration conf = _generator.getConfiguration();

            conf.setProperty(DDLGenConfiguration.SCHEMA_NAME_KEY, "test");
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, _generator.getConfiguration());
            
            _generator.getSchema().toCreateDDL(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = _expected.match(_engine, 0, ddl, new String[] {"test"});
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);

            conf.setProperty(DDLGenConfiguration.SCHEMA_NAME_KEY, "");
            
            out = new ByteArrayOutputStream();
            writer = new DDLWriter(out, _generator.getConfiguration());
            
            _generator.getSchema().toCreateDDL(writer);
            
            writer.close();
            ddl = out.toString();
            
            b = _expected.match(_engine, 1, ddl, new String[] {});
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testCreateSchema: " + e.getMessage());
        }
    }

    /**
     * Create index. There will be no DDL created at the moment.
     */
    public final void testCreateIndex() {
        try {
            loadData("index_creation.xml");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, _generator.getConfiguration());
            
            _generator.generateIndex(writer);
            
            writer.close();
            String ddl = out.toString();

            boolean b = _expected.match(_engine, ddl);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + _expected.getLastMatchString(), b);
        } catch (Exception e) {
            fail("testCreateIndex: " + e.getMessage());
        }
    }

    //--------------------------------------------------------------------------
}

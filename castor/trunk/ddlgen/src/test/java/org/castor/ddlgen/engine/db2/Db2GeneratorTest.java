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
package org.castor.ddlgen.engine.db2;

import java.io.ByteArrayOutputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.DDLWriter;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;

/** 
 * Db2 generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class Db2GeneratorTest extends AbstractGeneratorTest {
    public Db2GeneratorTest(final String testcase) {
        super(testcase);
    }

    public Db2GeneratorTest(final String testcase, final boolean useDBEngine) {
        super(testcase);

        if (useDBEngine) { setEngine(Expected.ENGINE_DB2); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.db2 tests");

        // schema test
        suite.addTest(new Db2GeneratorTest("testCreateSchema", true));
        
        // drop test
        suite.addTest(new Db2GeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new Db2GeneratorTest("testSingleTable", false));
        suite.addTest(new Db2GeneratorTest("testMultipleTable", false));
        suite.addTest(new Db2GeneratorTest("testIgnoredTable", false));
        suite.addTest(new Db2GeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new Db2GeneratorTest("testSingleField", false));
        suite.addTest(new Db2GeneratorTest("testSingleFieldForAllDB2", true));
        suite.addTest(new Db2GeneratorTest("testSingleFieldExceptBit", true));        
        suite.addTest(new Db2GeneratorTest("testIgnoredField", false));
        suite.addTest(new Db2GeneratorTest("testNoField", false));
        suite.addTest(new Db2GeneratorTest("testManyKeysReference", false));
        suite.addTest(new Db2GeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new Db2GeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new Db2GeneratorTest("testClassId", true));
        suite.addTest(new Db2GeneratorTest("testClassMultipleId", true));
        suite.addTest(new Db2GeneratorTest("testFieldId", true));
        suite.addTest(new Db2GeneratorTest("testFieldMultipleId", true));
        suite.addTest(new Db2GeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new Db2GeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new Db2GeneratorTest("testOneOneRelationship", false));
        suite.addTest(new Db2GeneratorTest("testOneManyRelationship", false));
        suite.addTest(new Db2GeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new Db2GeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new Db2GeneratorTest("testKeyGenIdentity", true));
        suite.addTest(new Db2GeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new Db2GeneratorTest("testKeyGenMax", false));
        suite.addTest(new Db2GeneratorTest("testKeyGenSequence", true));
        suite.addTest(new Db2GeneratorTest("testKeyGenUUID", false));

        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/db2.properties");
        setGenerator(new Db2Generator(conf));

        KeyGeneratorRegistry keyGenRegistry = new KeyGeneratorRegistry(conf);
        getGenerator().setKeyGenRegistry(keyGenRegistry);
        
        getGenerator().initialize();
    }

    /** 
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        
        setGenerator(null);
    }

    /**
     * {@inheritDoc}
     */
    public void testSingleFieldForAllDB2() {
        try {
            loadData("single_field_for_all.xml");

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DDLWriter writer = new DDLWriter(out, getGenerator().getConfiguration());
                
                getGenerator().generateCreate(writer);
                
                writer.close();
                out.toString();

                fail("bit type is not supported, expected an exception");
            } catch (GeneratorException e) {
                assertTrue(true);
            }
        } catch (Exception e) {
            fail("testSingleFieldForAll: " + e.getMessage());
        }
    }
    
    /**
     * Create a table with 23 fields represented to each data type
     */
    public void testSingleFieldExceptBit() {
        try {
            loadData("single_field_except_bit.xml");

            DDLGenConfiguration conf = getGenerator().getConfiguration();
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
                    conf.getInteger(PARAM_PREFIX + "longvarchar" + PARAM_LENGTH),
                    conf.getInteger(PARAM_PREFIX + "timestamp" + PARAM_PRECISION),
                    conf.getInteger(PARAM_PREFIX + "binary" + PARAM_LENGTH),
                    conf.getInteger(PARAM_PREFIX + "varbinary" + PARAM_LENGTH),
                    getSuffixString(conf, PARAM_PREFIX + "other" + PARAM_LENGTH),
                    getSuffixString(conf, PARAM_PREFIX + "javaobject" + PARAM_LENGTH),
                    getSuffixString(conf, PARAM_PREFIX + "blob" + PARAM_LENGTH),
                    getSuffixString(conf, PARAM_PREFIX + "clob" + PARAM_LENGTH) };
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DDLWriter writer = new DDLWriter(out, conf);
            
            getGenerator().generateCreate(writer);
            
            writer.close();
            String ddl = out.toString();
            
            boolean b = getExpected().match(getEngine(), ddl, params);
            assertTrue("Generated DDL: " + ddl + "\n"
                     + "Expected DDL: " + getExpected().getLastMatchString(), b);
        } catch (Exception e) {
            fail("testSingleFieldForAll: " + e.getMessage());
        }
    }

    private String getSuffixString(final DDLGenConfiguration conf, final String key) {
        String suffix = "";
        int len = conf.getInteger(key).intValue();
        if (len >= 1024) { len = len / 1024; suffix = "K"; }
        if (len >= 1024) { len = len / 1024; suffix = "M"; }
        if (len >= 1024) { len = len / 1024; suffix = "G"; }
        return len + suffix;
    }
}

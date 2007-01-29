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
package org.castor.ddlgen.engine.oracle;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * Oracle generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class OracleGeneratorTest extends AbstractGeneratorTest {
    public OracleGeneratorTest(final String testcase) {
        super(testcase);
    }

    public OracleGeneratorTest(final String testcase, final boolean useDBEngine) {
        super(testcase);

        if (useDBEngine) { setEngine(Expected.ENGINE_ORACLE); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.oracle tests");

        // schema test
        suite.addTest(new OracleGeneratorTest("testCreateSchema", true));

        // drop test
        suite.addTest(new OracleGeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new OracleGeneratorTest("testSingleTable", false));
        suite.addTest(new OracleGeneratorTest("testMultipleTable", false));
        suite.addTest(new OracleGeneratorTest("testIgnoredTable", false));
        suite.addTest(new OracleGeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new OracleGeneratorTest("testSingleField", false));
        suite.addTest(new OracleGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new OracleGeneratorTest("testIgnoredField", false));
        suite.addTest(new OracleGeneratorTest("testNoField", false));
        suite.addTest(new OracleGeneratorTest("testManyKeysReference", false));
        suite.addTest(new OracleGeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new OracleGeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new OracleGeneratorTest("testClassId", false));
        suite.addTest(new OracleGeneratorTest("testClassMultipleId", false));
        suite.addTest(new OracleGeneratorTest("testFieldId", false));
        suite.addTest(new OracleGeneratorTest("testFieldMultipleId", false));
        suite.addTest(new OracleGeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new OracleGeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new OracleGeneratorTest("testOneOneRelationship", false));
        suite.addTest(new OracleGeneratorTest("testOneManyRelationship", false));
        suite.addTest(new OracleGeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new OracleGeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new OracleGeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new OracleGeneratorTest("testKeyGenMax", false));
        suite.addTest(new OracleGeneratorTest("testKeyGenSequence", true));
        suite.addTest(new OracleGeneratorTest("testKeyGenUUID", false));

        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/oracle.properties");
        setGenerator(new OracleGenerator(conf));

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
}

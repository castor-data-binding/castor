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
package org.castor.ddlgen.engine.postgresql;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * PostgreSQL generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class PostgresqlGeneratorTest extends AbstractGeneratorTest {
    public PostgresqlGeneratorTest(final String testcase) {
        super(testcase);
    }

    public PostgresqlGeneratorTest(final String testcase, final boolean useDBEngine) {
        super(testcase);

        if (useDBEngine) { setEngine(Expected.ENGINE_POSTGRESQL); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.postgresql tests");

        // schema test
        suite.addTest(new PostgresqlGeneratorTest("testCreateSchema", true));

        // drop test
        suite.addTest(new PostgresqlGeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new PostgresqlGeneratorTest("testSingleTable", false));
        suite.addTest(new PostgresqlGeneratorTest("testMultipleTable", false));
        suite.addTest(new PostgresqlGeneratorTest("testIgnoredTable", false));
        suite.addTest(new PostgresqlGeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new PostgresqlGeneratorTest("testSingleField", false));
        suite.addTest(new PostgresqlGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new PostgresqlGeneratorTest("testIgnoredField", false));
        suite.addTest(new PostgresqlGeneratorTest("testNoField", false));
        suite.addTest(new PostgresqlGeneratorTest("testManyKeysReference", false));
        suite.addTest(new PostgresqlGeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new PostgresqlGeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new PostgresqlGeneratorTest("testClassId", false));
        suite.addTest(new PostgresqlGeneratorTest("testClassMultipleId", false));
        suite.addTest(new PostgresqlGeneratorTest("testFieldId", false));
        suite.addTest(new PostgresqlGeneratorTest("testFieldMultipleId", false));
        suite.addTest(new PostgresqlGeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new PostgresqlGeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new PostgresqlGeneratorTest("testOneOneRelationship", false));
        suite.addTest(new PostgresqlGeneratorTest("testOneManyRelationship", false));
        suite.addTest(new PostgresqlGeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new PostgresqlGeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new PostgresqlGeneratorTest("testKeyGenIdentity", true));
        suite.addTest(new PostgresqlGeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new PostgresqlGeneratorTest("testKeyGenMax", false));
        suite.addTest(new PostgresqlGeneratorTest("testKeyGenSequence", true));
        suite.addTest(new PostgresqlGeneratorTest("testKeyGenUUID", false));
       
        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/postgresql.properties");
        setGenerator(new PostgresqlGenerator(conf));

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

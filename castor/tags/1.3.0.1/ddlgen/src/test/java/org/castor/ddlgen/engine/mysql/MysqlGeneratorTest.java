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
package org.castor.ddlgen.engine.mysql;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * Mysql generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MysqlGeneratorTest extends AbstractGeneratorTest {
    public MysqlGeneratorTest(final String testcase) {
        super(testcase);
    }

    public MysqlGeneratorTest(final String testcase, final boolean useDBEngine) {
        super(testcase);
        
        if (useDBEngine) { setEngine(Expected.ENGINE_MYSQL); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.mysql tests");

        // schema test
        suite.addTest(new MysqlGeneratorTest("testCreateSchema", true));
        
        // drop test
        suite.addTest(new MysqlGeneratorTest("testDropTable", true));

        // table test
        suite.addTest(new MysqlGeneratorTest("testSingleTable", true));
        suite.addTest(new MysqlGeneratorTest("testMultipleTable", true));
        suite.addTest(new MysqlGeneratorTest("testIgnoredTable", true));
        suite.addTest(new MysqlGeneratorTest("testNoTable", false));

        // field test
        suite.addTest(new MysqlGeneratorTest("testSingleField", true));
        suite.addTest(new MysqlGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new MysqlGeneratorTest("testIgnoredField", true));
        suite.addTest(new MysqlGeneratorTest("testNoField", false));
        suite.addTest(new MysqlGeneratorTest("testManyKeysReference", true));
        suite.addTest(new MysqlGeneratorTest("testManyClassKeysReference", true));
        suite.addTest(new MysqlGeneratorTest("test2LevelsReference", true));

        // primary key test
        suite.addTest(new MysqlGeneratorTest("testClassId", true));
        suite.addTest(new MysqlGeneratorTest("testClassMultipleId", true));
        suite.addTest(new MysqlGeneratorTest("testFieldId", true));
        suite.addTest(new MysqlGeneratorTest("testFieldMultipleId", true));
        suite.addTest(new MysqlGeneratorTest("testOverwriteFieldId", true));
        suite.addTest(new MysqlGeneratorTest("testNoId", true));

        // foreign key test
        suite.addTest(new MysqlGeneratorTest("testOneOneRelationship", true));
        suite.addTest(new MysqlGeneratorTest("testOneManyRelationship", true));
        suite.addTest(new MysqlGeneratorTest("testManyManyRelationship", true));

        // index test
        suite.addTest(new MysqlGeneratorTest("testCreateIndex", false));

        // key generator test
        suite.addTest(new MysqlGeneratorTest("testKeyGenIdentity", true));
        suite.addTest(new MysqlGeneratorTest("testKeyGenHighLow", true));
        suite.addTest(new MysqlGeneratorTest("testKeyGenMax", true));
        suite.addTest(new MysqlGeneratorTest("testKeyGenUUID", true));

        return suite;
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/mysql.properties");
        setGenerator(new MysqlGenerator(conf));

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

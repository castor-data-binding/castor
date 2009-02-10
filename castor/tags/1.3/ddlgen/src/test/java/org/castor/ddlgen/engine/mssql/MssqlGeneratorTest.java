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
package org.castor.ddlgen.engine.mssql;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * Mssql generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MssqlGeneratorTest extends AbstractGeneratorTest {
    public MssqlGeneratorTest(final String testcase) {
        super(testcase);
    }

    public MssqlGeneratorTest(final String testcase, final boolean useDBlEngine) {
        super(testcase);

        if (useDBlEngine) { setEngine(Expected.ENGINE_MSSQL); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.mssql tests");

        //schema test
        suite.addTest(new MssqlGeneratorTest("testCreateSchema", true));

        // drop test
        suite.addTest(new MssqlGeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new MssqlGeneratorTest("testSingleTable", false));
        suite.addTest(new MssqlGeneratorTest("testMultipleTable", false));
        suite.addTest(new MssqlGeneratorTest("testIgnoredTable", false));
        suite.addTest(new MssqlGeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new MssqlGeneratorTest("testSingleField", false));
        suite.addTest(new MssqlGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new MssqlGeneratorTest("testIgnoredField", false));
        suite.addTest(new MssqlGeneratorTest("testNoField", false));
        suite.addTest(new MssqlGeneratorTest("testManyKeysReference", false));
        suite.addTest(new MssqlGeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new MssqlGeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new MssqlGeneratorTest("testClassId", true));
        suite.addTest(new MssqlGeneratorTest("testClassMultipleId", true));
        suite.addTest(new MssqlGeneratorTest("testFieldId", true));
        suite.addTest(new MssqlGeneratorTest("testFieldMultipleId", true));
        suite.addTest(new MssqlGeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new MssqlGeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new MssqlGeneratorTest("testOneOneRelationship", false));
        suite.addTest(new MssqlGeneratorTest("testOneManyRelationship", false));
        suite.addTest(new MssqlGeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new MssqlGeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new MssqlGeneratorTest("testKeyGenIdentity", true));
        suite.addTest(new MssqlGeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new MssqlGeneratorTest("testKeyGenMax", false));
        suite.addTest(new MssqlGeneratorTest("testKeyGenUUID", false));
        
        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/mssql.properties");
        setGenerator(new MssqlGenerator(conf));

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

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
package org.castor.ddlgen.engine.sapdb;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * SapDB generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class SapdbGeneratorTest extends AbstractGeneratorTest {
    public SapdbGeneratorTest(final String testcase) {
        super(testcase);
    }

    public SapdbGeneratorTest(final String testcase, final boolean useDBEngine) {
        super(testcase);

        if (useDBEngine) { setEngine(Expected.ENGINE_SAPDB); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.sapdb tests");

        // schema test
        suite.addTest(new SapdbGeneratorTest("testCreateSchema", true));

        // drop test
        suite.addTest(new SapdbGeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new SapdbGeneratorTest("testSingleTable", false));
        suite.addTest(new SapdbGeneratorTest("testMultipleTable", false));
        suite.addTest(new SapdbGeneratorTest("testIgnoredTable", false));
        suite.addTest(new SapdbGeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new SapdbGeneratorTest("testSingleField", false));
        suite.addTest(new SapdbGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new SapdbGeneratorTest("testIgnoredField", false));
        suite.addTest(new SapdbGeneratorTest("testNoField", false));
        suite.addTest(new SapdbGeneratorTest("testManyKeysReference", false));
        suite.addTest(new SapdbGeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new SapdbGeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new SapdbGeneratorTest("testClassId", false));
        suite.addTest(new SapdbGeneratorTest("testClassMultipleId", false));
        suite.addTest(new SapdbGeneratorTest("testFieldId", false));
        suite.addTest(new SapdbGeneratorTest("testFieldMultipleId", false));
        suite.addTest(new SapdbGeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new SapdbGeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new SapdbGeneratorTest("testOneOneRelationship", false));
        suite.addTest(new SapdbGeneratorTest("testOneManyRelationship", false));
        suite.addTest(new SapdbGeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new SapdbGeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new SapdbGeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new SapdbGeneratorTest("testKeyGenMax", false));
        suite.addTest(new SapdbGeneratorTest("testKeyGenSequence", true));
        suite.addTest(new SapdbGeneratorTest("testKeyGenUUID", false));
        
        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/sapdb.properties");
        setGenerator(new SapdbGenerator(conf));

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

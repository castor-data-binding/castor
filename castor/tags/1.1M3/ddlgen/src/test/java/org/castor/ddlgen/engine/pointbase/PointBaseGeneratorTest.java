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
package org.castor.ddlgen.engine.pointbase;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * PointBase generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class PointBaseGeneratorTest extends AbstractGeneratorTest {
    public PointBaseGeneratorTest(final String testcase) {
        super(testcase);
    }

    public PointBaseGeneratorTest(final String testcase, final boolean useDBlEngine) {
        super(testcase);

        if (useDBlEngine) { setEngine(Expected.ENGINE_POINTBASE); }
    }


    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.pointbase tests");

        //schema test
        suite.addTest(new PointBaseGeneratorTest("testCreateSchema", true));

        // drop test
        suite.addTest(new PointBaseGeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new PointBaseGeneratorTest("testSingleTable", false));
        suite.addTest(new PointBaseGeneratorTest("testMultipleTable", false));
        suite.addTest(new PointBaseGeneratorTest("testIgnoredTable", false));
        suite.addTest(new PointBaseGeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new PointBaseGeneratorTest("testSingleField", false));
        suite.addTest(new PointBaseGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new PointBaseGeneratorTest("testIgnoredField", false));
        suite.addTest(new PointBaseGeneratorTest("testNoField", false));
        suite.addTest(new PointBaseGeneratorTest("testManyKeysReference", false));
        suite.addTest(new PointBaseGeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new PointBaseGeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new PointBaseGeneratorTest("testClassId", true));
        suite.addTest(new PointBaseGeneratorTest("testClassMultipleId", true));
        suite.addTest(new PointBaseGeneratorTest("testFieldId", true));
        suite.addTest(new PointBaseGeneratorTest("testFieldMultipleId", true));
        suite.addTest(new PointBaseGeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new PointBaseGeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new PointBaseGeneratorTest("testOneOneRelationship", false));
        suite.addTest(new PointBaseGeneratorTest("testOneManyRelationship", false));
        suite.addTest(new PointBaseGeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new PointBaseGeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new PointBaseGeneratorTest("testKeyGenIdentity", true));
        suite.addTest(new PointBaseGeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new PointBaseGeneratorTest("testKeyGenMax", false));
        suite.addTest(new PointBaseGeneratorTest("testKeyGenUUID", false));
        
        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/pointbase.properties");
        setGenerator(new PointBaseGenerator(conf));

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

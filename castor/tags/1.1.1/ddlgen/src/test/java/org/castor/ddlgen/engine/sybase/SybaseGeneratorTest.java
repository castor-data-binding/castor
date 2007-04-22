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
package org.castor.ddlgen.engine.sybase;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.KeyGeneratorRegistry;
import org.castor.ddlgen.test.framework.AbstractGeneratorTest;
import org.castor.ddlgen.test.framework.Expected;


/**
 * Sybase generator test.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class SybaseGeneratorTest extends AbstractGeneratorTest {
    public SybaseGeneratorTest(final String testcase) {
        super(testcase);
    }

    public SybaseGeneratorTest(final String testcase, final boolean useDBlEngine) {
        super(testcase);

        if (useDBlEngine) { setEngine(Expected.ENGINE_SYBASE); }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddlgen.engine.sybase tests");

        //schema test
        suite.addTest(new SybaseGeneratorTest("testCreateSchema", true));

        // drop test
        suite.addTest(new SybaseGeneratorTest("testDropTable", false));
        
        // table test
        suite.addTest(new SybaseGeneratorTest("testSingleTable", false));
        suite.addTest(new SybaseGeneratorTest("testMultipleTable", false));
        suite.addTest(new SybaseGeneratorTest("testIgnoredTable", false));
        suite.addTest(new SybaseGeneratorTest("testNoTable", false));

        //field test
        suite.addTest(new SybaseGeneratorTest("testSingleField", false));
        suite.addTest(new SybaseGeneratorTest("testSingleFieldForAll", true));
        suite.addTest(new SybaseGeneratorTest("testIgnoredField", false));
        suite.addTest(new SybaseGeneratorTest("testNoField", false));
        suite.addTest(new SybaseGeneratorTest("testManyKeysReference", false));
        suite.addTest(new SybaseGeneratorTest("testManyClassKeysReference", false));
        suite.addTest(new SybaseGeneratorTest("test2LevelsReference", false));
        
        // primary key test
        suite.addTest(new SybaseGeneratorTest("testClassId", false));
        suite.addTest(new SybaseGeneratorTest("testClassMultipleId", false));
        suite.addTest(new SybaseGeneratorTest("testFieldId", false));
        suite.addTest(new SybaseGeneratorTest("testFieldMultipleId", false));
        suite.addTest(new SybaseGeneratorTest("testOverwriteFieldId", false));
        suite.addTest(new SybaseGeneratorTest("testNoId", false));

        // foreign key test
        suite.addTest(new SybaseGeneratorTest("testOneOneRelationship", false));
        suite.addTest(new SybaseGeneratorTest("testOneManyRelationship", false));
        suite.addTest(new SybaseGeneratorTest("testManyManyRelationship", false));

        // index test
        suite.addTest(new SybaseGeneratorTest("testCreateIndex", false));        
        
        // key generator test
        suite.addTest(new SybaseGeneratorTest("testKeyGenIdentity", true));
        suite.addTest(new SybaseGeneratorTest("testKeyGenHighLow", false));
        suite.addTest(new SybaseGeneratorTest("testKeyGenMax", false));
        suite.addTest(new SybaseGeneratorTest("testKeyGenUUID", false));
        
        return suite;
    }

    /** 
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        DDLGenConfiguration conf = new DDLGenConfiguration();
        conf.addProperties("org/castor/ddlgen/test/config/ddlgen.properties");
        conf.addProperties("org/castor/ddlgen/test/config/sybase.properties");
        setGenerator(new SybaseGenerator(conf));

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

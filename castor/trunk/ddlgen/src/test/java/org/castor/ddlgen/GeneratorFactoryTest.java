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
package org.castor.ddlgen;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.ddlgen.engine.mysql.MysqlGenerator;

/**
 * Test GeneratorFactory
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class GeneratorFactoryTest extends TestCase {

    /**
     * Constructor for GeneratorFactoryTest
     * @param testcase  test case
     */
    public GeneratorFactoryTest(final String testcase) {
        super(testcase);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * test get mysql ddl generator
     */
    public void testGetMySQLDDLGenerator() {
        
        try {
            MysqlGenerator generator = (MysqlGenerator) GeneratorFactory
                .createDDLGenerator("mysql", null, null);
            assertEquals(generator.getClass(), MysqlGenerator.class);
        } catch (GeneratorException e) {
            assertTrue(e.getMessage(), false);
        } 
    }
    
    /**
     * 
     * @return  Test
     * @throws Exception exception
     */
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("DDL generator factory tests");
        suite.addTest(new GeneratorFactoryTest("testGetMySQLDDLGenerator"));
        
        return suite;
    }

}

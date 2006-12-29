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

import org.apache.log4j.xml.DOMConfigurator;
import org.castor.ddlgen.engine.db2.Db2GeneratorTest;
import org.castor.ddlgen.engine.derby.DerbyGeneratorTest;
import org.castor.ddlgen.engine.hsql.HsqlGeneratorTest;
import org.castor.ddlgen.engine.mssql.MssqlGeneratorTest;
import org.castor.ddlgen.engine.mysql.MysqlGeneratorTest;
import org.castor.ddlgen.engine.oracle.OracleGeneratorTest;
import org.castor.ddlgen.engine.pointbase.PointBaseGeneratorTest;
import org.castor.ddlgen.engine.postgresql.PostgresqlGeneratorTest;
import org.castor.ddlgen.engine.sapdb.SapdbGeneratorTest;
import org.castor.ddlgen.engine.sybase.SybaseGeneratorTest;

/**
 * Test all DDL generator
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public class TestAll extends TestCase {

    /**log file*/
    private static final String LOG_FILE = "log4j-test.xml";

    /**
     * Constructor for TestAll
     * @param name name
     */
    public TestAll(final String name) { super(name); }

    /**
     * 
     * @param args params
     */
    public static void main(final String[] args) {
        try {
            DOMConfigurator.configure(LOG_FILE);
            
//            TestResult result =  
            junit.textui.TestRunner.run(TestAll.suite());
//            junit.swingui.TestRunner.run(TestAll.class);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }


    /**
     * 
     * @return Test
     * @throws Exception exception
     */
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.ddl tests");
        
        //GeneratorFactory test
        suite.addTest(GeneratorFactoryTest.suite());
        
        //MySQL generator test
        suite.addTest(MysqlGeneratorTest.suite());

        //Oracle generator test
        suite.addTest(OracleGeneratorTest.suite());

        //PostgreSQL generator test
        suite.addTest(PostgresqlGeneratorTest.suite());

        //Derby generator test
        suite.addTest(DerbyGeneratorTest.suite());

        //Mssql generator test
        suite.addTest(MssqlGeneratorTest.suite());

        //Sapdb generator test
        suite.addTest(SapdbGeneratorTest.suite());

        //db2 generator test
        suite.addTest(Db2GeneratorTest.suite());

        //db2 generator test
        suite.addTest(SybaseGeneratorTest.suite());

        //hsql generator test
        suite.addTest(HsqlGeneratorTest.suite());

        //pointbase generator test
        suite.addTest(PointBaseGeneratorTest.suite());

        return suite;
    }
    
}

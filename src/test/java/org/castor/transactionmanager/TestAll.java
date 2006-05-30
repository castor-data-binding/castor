/*
 * Copyright 2005 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.transactionmanager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run all tests of the org.castor.transactionmanager package.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestAll extends TestCase {
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("All org.castor.transactionmanager tests");

        suite.addTest(TestTransactionManagerAcquireException.suite());

        suite.addTest(TestLocalTransactionManager.suite());
        
        suite.addTest(TestLocalTransactionManagerFactory.suite());
        suite.addTest(TestJNDIENCTransactionManagerFactory.suite());
        suite.addTest(TestJOTMTransactionManagerFactory.suite());
        suite.addTest(TestAbstractTransactionManagerFactory.suite());
        suite.addTest(TestWebSphereTransactionManagerFactory.suite());
        suite.addTest(TestWebSphere5TransactionManagerFactory.suite());
        suite.addTest(TestWebSphere51TransactionManagerFactory.suite());

        suite.addTest(TestTransactionManagerFactoryRegistry.suite());
        suite.addTest(TestTransactionManagerRegistry.suite());

        return suite;
    }

    public TestAll(final String name) { super(name); }
}

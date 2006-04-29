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

import org.castor.transactionmanager.WebSphere51TransactionManagerFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestWebSphere51TransactionManagerFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("WebSphere51TransactionManagerFactory Tests");

        suite.addTest(new TestWebSphere51TransactionManagerFactory("test"));

        return suite;
    }

    public TestWebSphere51TransactionManagerFactory(final String name) { super(name); }

    public void test() {
        assertEquals("websphere51",
                     WebSphere51TransactionManagerFactory.NAME);
        assertEquals("com.ibm.ws.Transaction.TransactionManagerFactory",
                     WebSphere51TransactionManagerFactory.FACTORY_CLASS_NAME);
        assertEquals("getTransactionManager",
                     WebSphere51TransactionManagerFactory.FACTORY_METHOD_NAME);
        
        WebSphere51TransactionManagerFactory factory;
        factory = new WebSphere51TransactionManagerFactory();
        
        assertEquals("websphere51",
                     factory.getName());
        assertEquals("com.ibm.ws.Transaction.TransactionManagerFactory",
                     factory.getFactoryClassName());
        assertEquals("getTransactionManager",
                     factory.getFactoryMethodName());
    }
}

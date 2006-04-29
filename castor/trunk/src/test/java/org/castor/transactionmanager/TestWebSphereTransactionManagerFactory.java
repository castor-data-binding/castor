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

import org.castor.transactionmanager.WebSphereTransactionManagerFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestWebSphereTransactionManagerFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("WebSphereTransactionManagerFactory Tests");

        suite.addTest(new TestWebSphereTransactionManagerFactory("test"));

        return suite;
    }

    public TestWebSphereTransactionManagerFactory(final String name) { super(name); }

    public void test() {
        assertEquals("websphere",
                     WebSphereTransactionManagerFactory.NAME);
        assertEquals("com.ibm.ejs.jts.jta.JTSXA",
                     WebSphereTransactionManagerFactory.FACTORY_CLASS_NAME);
        assertEquals("getTransactionManager",
                     WebSphereTransactionManagerFactory.FACTORY_METHOD_NAME);
        
        WebSphereTransactionManagerFactory factory;
        factory = new WebSphereTransactionManagerFactory();
        
        assertEquals("websphere", factory.getName());
        assertEquals("com.ibm.ejs.jts.jta.JTSXA", factory.getFactoryClassName());
        assertEquals("getTransactionManager", factory.getFactoryMethodName());
    }
}

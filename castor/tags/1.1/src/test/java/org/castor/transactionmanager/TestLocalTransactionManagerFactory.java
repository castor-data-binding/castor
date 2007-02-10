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
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestLocalTransactionManagerFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("LocalTransactionManagerFactory Tests");

        suite.addTest(new TestLocalTransactionManagerFactory("test"));

        return suite;
    }

    public TestLocalTransactionManagerFactory(final String name) { super(name); }

    public void test() {
        assertEquals("local", LocalTransactionManagerFactory.NAME);
        
        Object man1 = LocalTransactionManagerFactory.MANAGER;
        assertTrue(man1 instanceof LocalTransactionManager);
        
        LocalTransactionManagerFactory factory = new LocalTransactionManagerFactory();
        
        assertEquals("local", factory.getName());
        
        Object man2 = factory.getTransactionManager(null);
        assertTrue(man2 instanceof LocalTransactionManager);
    }
}

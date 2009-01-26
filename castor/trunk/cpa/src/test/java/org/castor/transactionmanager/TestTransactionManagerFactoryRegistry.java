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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestTransactionManagerFactoryRegistry extends TestCase {
    private static final boolean DISABLE_LOGGING = false;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("TransactionManagerFactoryRegistry Tests");

        suite.addTest(new TestTransactionManagerFactoryRegistry("test"));

        return suite;
    }

    public TestTransactionManagerFactoryRegistry(final String name) { super(name); }

    public void test() {
        Logger logger = Logger.getLogger(TransactionManagerFactoryRegistry.class);
        Level level = logger.getLevel();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        assertEquals("org.castor.transactionmanager.Factories",
                CPAProperties.TRANSACTION_MANAGER_FACTORIES);
        
        Object temp = new TransactionManagerFactoryDummy();
        
        AbstractProperties properties = CPAProperties.newInstance();
        Object mem = properties.getObject(CPAProperties.TRANSACTION_MANAGER_FACTORIES);
        properties.put(CPAProperties.TRANSACTION_MANAGER_FACTORIES,
                "org.castor.transactionmanager.LocalTransactionManagerFactory, "
                + TransactionManagerFactoryDummy.class.getName());
        
        TransactionManagerFactoryRegistry registry;
        registry = new TransactionManagerFactoryRegistry(properties);
        
        String[] names = registry.getTransactionManagerFactoryNames();
        assertEquals(2, names.length);
        assertTrue("local".equals(names[0]) || "local".equals(names[1]));
        assertTrue("dummy".equals(names[0]) || "dummy".equals(names[1]));
        
        TransactionManagerFactory factory = null;
        try {
            factory = registry.getTransactionManagerFactory("unknown");
            fail("A TransactionManagerAcquireException without cause "
               + "should have been thrown.");
        } catch (TransactionManagerAcquireException ex) {
            assertNull(factory);
        }

        try {
            factory = registry.getTransactionManagerFactory("dummy");
            assertTrue(factory instanceof TransactionManagerFactoryDummy);
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }
        
        properties.put(CPAProperties.TRANSACTION_MANAGER_FACTORIES, mem);

        logger.setLevel(level);
    }
}

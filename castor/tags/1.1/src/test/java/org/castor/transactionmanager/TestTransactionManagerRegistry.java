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

import javax.transaction.TransactionManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestTransactionManagerRegistry extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("TransactionManagerRegistry Tests");

        suite.addTest(new TestTransactionManagerRegistry("test"));

        return suite;
    }

    public TestTransactionManagerRegistry(final String name) { super(name); }

    public void test() {
        Logger logger = Logger.getLogger(TransactionManagerRegistry.class);
        Level level = logger.getLevel();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        assertEquals("org.castor.transactionmanager.InitializeAtRegistration",
                ConfigKeys.TRANSACTION_MANAGER_INIT);
        
        Configuration config = Configuration.getInstance();
        String memF = config.getProperty(ConfigKeys.TRANSACTION_MANAGER_FACTORIES, null);
        String memI = config.getProperty(ConfigKeys.TRANSACTION_MANAGER_INIT, null);
        config.getProperties().put(ConfigKeys.TRANSACTION_MANAGER_FACTORIES,
                TransactionManagerFactoryDummy.class.getName());
        config.getProperties().put(ConfigKeys.TRANSACTION_MANAGER_INIT,
                Boolean.FALSE.toString());
        
        TransactionManagerRegistry tmr = new TransactionManagerRegistry(config);
        String[] managers = tmr.getTransactionManagerNames();
        assertEquals(0, managers.length);
        
        TransactionManager manager = new TransactionManagerDummy();
        try {
            tmr.registerTransactionManager("m1", manager);
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }
        
        managers = tmr.getTransactionManagerNames();
        assertEquals(1, managers.length);
        assertEquals("m1", managers[0]);

        try {
            assertTrue(manager == tmr.getTransactionManager("m1"));
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }

        try {
            tmr.registerTransactionManager("m1", new TransactionManagerDummy());
            fail("A TransactionManagerAcquireException without cause "
                    + "should have been thrown.");
        } catch (TransactionManagerAcquireException ex) {
            assertNull(ex.getCause());
        }
        
        managers = tmr.getTransactionManagerNames();
        assertEquals(1, managers.length);
        assertEquals("m1", managers[0]);

        try {
            assertTrue(manager == tmr.getTransactionManager("m1"));
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }

        tmr.deregisterTransactionManager("m1");
        managers = tmr.getTransactionManagerNames();
        assertEquals(0, managers.length);
        
        try {
            tmr.registerTransactionManager("m2", "dummy", null);
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }
        
        managers = tmr.getTransactionManagerNames();
        assertEquals(1, managers.length);
        assertEquals("m2", managers[0]);

        WebSphereMock.init(new TransactionManagerDummy());
        try {
            manager = tmr.getTransactionManager("m2");
            assertTrue(manager instanceof TransactionManagerDummy);
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }

        try {
            tmr.getTransactionManager("m3");
            fail("A TransactionManagerAcquireException without cause "
                    + "should have been thrown.");
        } catch (TransactionManagerAcquireException ex) {
            assertNull(ex.getCause());
        }

        tmr.deregisterTransactionManager("m2");
        managers = tmr.getTransactionManagerNames();
        assertEquals(0, managers.length);
        
        config.getProperties().put(ConfigKeys.TRANSACTION_MANAGER_INIT, memI);
        config.getProperties().put(ConfigKeys.TRANSACTION_MANAGER_FACTORIES, memF);

        logger.setLevel(level);
    }
}

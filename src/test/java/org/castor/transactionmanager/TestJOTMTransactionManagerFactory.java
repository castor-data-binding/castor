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

import org.castor.transactionmanager.JOTMTransactionManagerFactory;
import org.castor.transactionmanager.TransactionManagerAcquireException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestJOTMTransactionManagerFactory extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("JOTMTransactionManagerFactory Tests");

        suite.addTest(new TestJOTMTransactionManagerFactory("test"));

        return suite;
    }

    public TestJOTMTransactionManagerFactory(final String name) { super(name); }

    public void test() {
        Logger logger = Logger.getLogger(JOTMTransactionManagerFactory.class);
        Level level = logger.getLevel();
        
        assertEquals("jotm",
                     JOTMTransactionManagerFactory.NAME);
        assertEquals("org.objectweb.jotm.Jotm",
                     JOTMTransactionManagerFactory.FACTORY_CLASS_NAME);
        
        JOTMTransactionManagerFactory factory = new JOTMTransactionManagerFactory();
        
        assertEquals("jotm", factory.getName());
        
        TransactionManager manager = null;
        JOTMMock.init(new TransactionManagerDummy());
        try {
            manager = factory.getTransactionManager(JOTMMock.class.getName(), null);
            assertEquals(Boolean.TRUE, JOTMMock.getBoolean1());
            assertEquals(Boolean.FALSE, JOTMMock.getBoolean2());
            assertTrue(manager instanceof TransactionManagerDummy);
        } catch (TransactionManagerAcquireException ex) {
            fail("Unexpected TransactionManagerAcquireException.");
        }

        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        manager = null;
        JOTMMock.init(null);
        try {
            manager = factory.getTransactionManager(JOTMMock.class.getName(), null);
            fail("A TransactionManagerAcquireException without cause "
               + "should have been thrown.");
        } catch (TransactionManagerAcquireException ex) {
            assertEquals(Boolean.TRUE, JOTMMock.getBoolean1());
            assertEquals(Boolean.FALSE, JOTMMock.getBoolean2());
            assertTrue(manager == null);
            assertNull(ex.getCause());
        }

        manager = null;
        JOTMMock.init(new Exception());
        try {
            manager = factory.getTransactionManager(JOTMMock.class.getName(), null);
            fail("A TransactionManagerAcquireException with cause "
               + "should have been thrown.");
        } catch (TransactionManagerAcquireException ex) {
            assertEquals(Boolean.TRUE, JOTMMock.getBoolean1());
            assertEquals(Boolean.FALSE, JOTMMock.getBoolean2());
            assertTrue(manager == null);
            assertTrue(ex.getCause() instanceof Exception);
        }
        
        logger.setLevel(level);
    }
}

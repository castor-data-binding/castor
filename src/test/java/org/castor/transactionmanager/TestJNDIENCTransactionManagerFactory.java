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

import java.util.Properties;
import javax.naming.NotContextException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.castor.transactionmanager.JNDIENCTransactionManagerFactory;
import org.castor.transactionmanager.TransactionManagerAcquireException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestJNDIENCTransactionManagerFactory extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("JNDIENCTransactionManagerFactory Tests");

        suite.addTest(new TestJNDIENCTransactionManagerFactory("test"));

        return suite;
    }

    public TestJNDIENCTransactionManagerFactory(final String name) { super(name); }

    public void test() {
        Logger logger = Logger.getLogger(JNDIENCTransactionManagerFactory.class);
        Level level = logger.getLevel();
        
        assertEquals("jndi",
                     JNDIENCTransactionManagerFactory.NAME);
        assertEquals("java:comp/TransactionManager",
                     JNDIENCTransactionManagerFactory.TRANSACTION_MANAGER_NAME);
        
        JNDIENCTransactionManagerFactory factory = new JNDIENCTransactionManagerFactory();
        
        assertEquals("jndi", factory.getName());
        
        // to test successful operation we would need a JNDI running so we
        // only test fail situation.
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            factory.getTransactionManager(new Properties());
            fail("A TransactionManagerAcquireException with a NotContextException "
               + "should have been thrown.");
        } catch (Exception ex) {
            assertTrue(ex instanceof TransactionManagerAcquireException);
            assertTrue(ex.getCause() instanceof NotContextException);
        }
        
        logger.setLevel(level);
    }
}

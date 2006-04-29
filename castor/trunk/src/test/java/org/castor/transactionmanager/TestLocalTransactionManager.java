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

import org.castor.transactionmanager.LocalTransactionManager;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestLocalTransactionManager extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("LocalTransactionManager Tests");

        suite.addTest(new TestLocalTransactionManager("test"));

        return suite;
    }

    public TestLocalTransactionManager(final String name) { super(name); }

    public void test() {
        LocalTransactionManager ltm = new LocalTransactionManager();
        
        try {
            ltm.begin();
            fail("SystemException should been thrown by begin().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.commit();
            fail("SystemException should been thrown by commit().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.getStatus();
            fail("SystemException should been thrown by getStatus().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.getTransaction();
            fail("SystemException should been thrown by getTransaction().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.resume(null);
            fail("SystemException should been thrown by resume(Transaction).");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.rollback();
            fail("SystemException should been thrown by rollback().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.setRollbackOnly();
            fail("SystemException should been thrown by setRollbackOnly().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.setTransactionTimeout(0);
            fail("SystemException should been thrown by setTransactionTimeout(int).");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
        
        try {
            ltm.suspend();
            fail("SystemException should been thrown by suspend().");
        } catch (Exception ex) {
            assertTrue(ex instanceof javax.transaction.SystemException);
        }
    }
}

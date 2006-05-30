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
public final class TestTransactionManagerAcquireException extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("CacheAcquireException Tests");

        suite.addTest(new TestTransactionManagerAcquireException("test"));

        return suite;
    }

    public TestTransactionManagerAcquireException(final String name) { super(name); }

    public void test() {
        Exception ex;
        
        ex = new TransactionManagerAcquireException("some reason");
        assertTrue(ex instanceof TransactionManagerAcquireException);
        assertEquals("some reason", ex.getMessage());
        assertNull(ex.getCause());
        
        Exception root = new Exception("root exception");
        ex = new TransactionManagerAcquireException("other reason", root);
        assertTrue(ex instanceof TransactionManagerAcquireException);
        assertEquals("other reason", ex.getMessage());
        assertEquals(root, ex.getCause());
    }
}

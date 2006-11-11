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

/**
 * Mock object for testing of JOTMTransactionManagerFactory.
 *  
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class JOTMMock {
    //--------------------------------------------------------------------------
    
    private static boolean _bool1 = false;
    private static boolean _bool2 = false;
    private static Object _result = null;
    
    //--------------------------------------------------------------------------
    
    public static void init(final Object result) {
        _bool1 = false;
        _bool2 = false;
        _result = result;
    }
    
    public static boolean getBoolean1() { return _bool1; }
    
    public static boolean getBoolean2() { return _bool2; }
    
    //--------------------------------------------------------------------------

    public JOTMMock(final boolean bool1, final boolean bool2) {
        _bool1 = bool1;
        _bool2 = bool2;
    }
    
    public TransactionManager getTransactionManager() throws Exception {
        if (_result instanceof Exception) {
            throw (Exception) _result;
        } else if (_result instanceof TransactionManager) {
            return (TransactionManager) _result;
        } else {
            return null;
        }
    }
    
    //--------------------------------------------------------------------------
}

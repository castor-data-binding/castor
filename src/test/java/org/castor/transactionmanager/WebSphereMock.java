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
 * Mock object to test AbstractTransactionManagerFactory used at factories for
 * WebSphere.
 *  
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class WebSphereMock {
    //--------------------------------------------------------------------------
    
    private static Object _result = null;
    
    //--------------------------------------------------------------------------
    
    public static void init(final Object result) {
        _result = result;
    }
    
    //--------------------------------------------------------------------------
    
    private WebSphereMock() { }

    public static TransactionManager getTransactionManager() throws Exception {
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

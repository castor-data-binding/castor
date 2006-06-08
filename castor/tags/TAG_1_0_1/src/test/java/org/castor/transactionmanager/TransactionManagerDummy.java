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

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * Dummy transaction manager for testing.
 *  
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TransactionManagerDummy implements TransactionManager {
    //--------------------------------------------------------------------------

    /**
     * @see javax.transaction.TransactionManager#begin()
     */
    public void begin() { }
    
    /**
     * @see javax.transaction.TransactionManager#commit()
     */
    public void commit() { }
    
    /**
     * @see javax.transaction.TransactionManager#getStatus()
     */
    public int getStatus() { return 0; }
    
    /**
     * @see javax.transaction.TransactionManager#getTransaction()
     */
    public Transaction getTransaction() { return null; }
    
    /**
     * @see javax.transaction.TransactionManager#resume(javax.transaction.Transaction)
     */
    public void resume(final Transaction arg) { }
    
    /**
     * @see javax.transaction.TransactionManager#rollback()
     */
    public void rollback() { }
    
    /**
     * @see javax.transaction.TransactionManager#setRollbackOnly()
     */
    public void setRollbackOnly() { }
    
    /**
     * @see javax.transaction.TransactionManager#setTransactionTimeout(int)
     */
    public void setTransactionTimeout(final int arg) { }
    
    /**
     * @see javax.transaction.TransactionManager#suspend()
     */
    public Transaction suspend() { return null; }

    //--------------------------------------------------------------------------
}

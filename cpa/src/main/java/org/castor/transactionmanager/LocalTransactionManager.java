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

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * Default transaction manager when Castor is used in standalone mode,
 * in other words not within a J2EE container.
 *  
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class LocalTransactionManager implements TransactionManager {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#begin()
     */
    public void begin() throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#commit()
     */
    public void commit() throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#getStatus()
     */
    public int getStatus() throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#getTransaction()
     */
    public Transaction getTransaction() throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#resume(javax.transaction.Transaction)
     */
    public void resume(final Transaction arg) throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#rollback()
     */
    public void rollback() throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#setRollbackOnly()
     */
    public void setRollbackOnly() throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#setTransactionTimeout(int)
     */
    public void setTransactionTimeout(final int arg) throws SystemException {
        throw new SystemException("not supported");
    }
    
    /**
     * {@inheritDoc}
     * @see javax.transaction.TransactionManager#suspend()
     */
    public Transaction suspend() throws SystemException {
        throw new SystemException("not supported");
    }

    //--------------------------------------------------------------------------
}

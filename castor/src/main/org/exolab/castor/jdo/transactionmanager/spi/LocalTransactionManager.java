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
package org.exolab.castor.jdo.transactionmanager.spi;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * Default transaction manager when Castor is used in standalone mode,
 * in other words not within a J2EE container.
 *  
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class LocalTransactionManager implements TransactionManager {
    //--------------------------------------------------------------------------

    public void begin() throws SystemException {
        throw new SystemException("not supported");
    }
    
    public void commit() throws SystemException {
        throw new SystemException("not supported");
    }
    
    public int getStatus() throws SystemException {
        throw new SystemException("not supported");
    }
    
    public Transaction getTransaction() throws SystemException {
        throw new SystemException("not supported");
    }
    
    public void resume(final Transaction arg) throws SystemException {
        throw new SystemException("not supported");
    }
    
    public void rollback() throws SystemException {
        throw new SystemException("not supported");
    }
    
    public void setRollbackOnly() throws SystemException {
        throw new SystemException("not supported");
    }
    
    public void setTransactionTimeout(final int arg) throws SystemException {
        throw new SystemException("not supported");
    }
    
    public Transaction suspend() throws SystemException {
        throw new SystemException("not supported");
    }

    //--------------------------------------------------------------------------
}

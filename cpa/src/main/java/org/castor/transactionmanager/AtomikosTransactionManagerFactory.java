/*
 * Copyright 2009 Ralf Joachim
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

import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Transaction manager factory instance to be used with J2EE containers
 * where the transaction manager used is Atomikos.
 * 
 * Implements {link org.castor.jdo.transaction.TransactionManagerFactory}.
 *
 * @author <a href=" mailto:ralf DOT joachim AT syscon DOT de">Ralf Joachim</a>
 * @version $Revision: 8104 $ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class AtomikosTransactionManagerFactory implements TransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            AtomikosTransactionManagerFactory.class);
    
    /** Name of the Atomikos specific transaction manager class. */
    public static final String MANAGER_CLASS_NAME =
        "com.atomikos.icatch.jta.UserTransactionManager";

    /** The name of the factory. */
    public static final String NAME = "atomikos";

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getName() { return NAME; }

    /**
     * {@inheritDoc}
     */
    public TransactionManager getTransactionManager(final Properties properties)
    throws TransactionManagerAcquireException {
        return getTransactionManager(MANAGER_CLASS_NAME, properties);
    }
    
    /**
     * Constructs a instance of the given manager class name which implements
     * <tt>javax.transaction.TransactionManager</tt> interface with the given
     * properties. This method has been introduced to allow testing with mock
     * objects.
     * 
     * @param managerClassName Class name of the transaction manager.
     * @param properties The properties passed to the transaction manager.
     * @return The transaction manager.
     * @throws TransactionManagerAcquireException If any failure occured when loading
     *         the transaction manager.
     */
    public TransactionManager getTransactionManager(
            final String managerClassName, final Properties properties)
    throws TransactionManagerAcquireException {
        TransactionManager transactionManager = null;
        
        try {
            Class<?> manager = Class.forName(managerClassName);
            Object obj = manager.newInstance(); 
            transactionManager = (TransactionManager) obj;
        } catch (Exception ex) {
            String msg = "Unable to acquire instance of "
                       + "javax.transaction.TransactionManager: " + NAME;
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg, ex);
        }
        
        if (transactionManager == null) {
            String msg = "Unable to acquire instance of "
                       + "javax.transaction.TransactionManager: " + NAME;
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg);
        }
        
        return transactionManager;
    }

    //--------------------------------------------------------------------------
}

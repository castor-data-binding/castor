/*
 * Copyright 2005 Bruce Snyder, Werner Guttmann, Ralf Joachim
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

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Transaction manager factory instance to be used with J2EE containers
 * where the transaction manager is bound to the JNDI ENC of the container.
 * 
 * Implements {link org.exolab.castor.jdo.transactionmanager.
 * TransactionManagerFactory}.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class JNDIENCTransactionManagerFactory implements TransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            JNDIENCTransactionManagerFactory.class);
    
    /** Default JNDI binding for <tt>javax.transaction.TransactionManager</tt>
     *  instance. */
    public static final String TRANSACTION_MANAGER_NAME = "java:comp/TransactionManager";

    /** The name of the factory. */
    public static final String NAME = "jndi";

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @see org.castor.transactionmanager.TransactionManagerFactory#getName()
     */
    public String getName() { return NAME; }

    /**
     * {@inheritDoc}
     * @see org.castor.transactionmanager.TransactionManagerFactory
     *      #getTransactionManager(java.util.Properties)
     */
    public TransactionManager getTransactionManager(final Properties properties)
    throws TransactionManagerAcquireException {
        String jndiENC = properties.getProperty("jndiEnc", TRANSACTION_MANAGER_NAME);
        Object found = null;
        TransactionManager transactionManager = null;
        
        try {
            found = new InitialContext().lookup(jndiENC);
            transactionManager = (TransactionManager) found;  
        } catch (ClassCastException ex) {
            String msg = "Problem casting instance of " + found.getClass().getName()
                       + " to javax.transaction.TransactionManager.";
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg, ex);
        } catch (Exception ex) {
            String msg = "Unable to acquire instance of "
                       + "javax.transaction.TransactionManager: " + jndiENC;
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg, ex);
        }

        if (transactionManager == null) {
            String msg = "Unable to acquire instance of "
                       + "javax.transaction.TransactionManager: " + jndiENC;
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg);
        }
        
        return transactionManager;
    }

    //--------------------------------------------------------------------------
}

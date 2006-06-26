/*
 * Copyright 2005 Werner Guttmann
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

import java.lang.reflect.Method;
import java.util.Properties;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Transaction manager factory instance to be used with J2EE containers
 * where the transaction manager used is JOTM.
 * 
 * Implements {link org.castor.jdo.transaction.TransactionManagerFactory}.
 *
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class JOTMTransactionManagerFactory implements TransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            JOTMTransactionManagerFactory.class);
    
    /** Name of the JOTM specific transaction manager factory class. */
    public static final String FACTORY_CLASS_NAME = "org.objectweb.jotm.Jotm";

    /** The name of the factory. */
    public static final String NAME = "jotm";

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
        return getTransactionManager(FACTORY_CLASS_NAME, properties);
    }
    
    /**
     * Acquires a <tt>javax.transaction.TransactionManager</tt> instance with the
     * given properties from the given factory. The factory implementation needs to
     * be compatible to <tt>org.objectweb.jotm.Jotm</tt>. The method has been
     * introduced to allow testing with mock objects.
     * 
     * @param factoryClassName Class name of the factory copatibla with JOTM.
     * @param properties The properties passed to the transaction manager.
     * @return The transaction manager.
     * @throws TransactionManagerAcquireException If any failure occured when loading
     *         the transaction manager.
     */
    public TransactionManager getTransactionManager(
            final String factoryClassName, final Properties properties)
    throws TransactionManagerAcquireException {
        TransactionManager transactionManager = null;
        
        try {
            Class factory = Class.forName(factoryClassName);
            Class[] types = new Class[] {boolean.class, boolean.class};
            Object[] params = new Object[] {Boolean.TRUE, Boolean.FALSE};
            Object jotm = factory.getConstructor(types).newInstance(params); 
            Method method = factory.getMethod("getTransactionManager", (Class[]) null);
            Object obj = method.invoke(jotm, (Object[]) null);
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

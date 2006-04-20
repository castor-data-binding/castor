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

import java.lang.reflect.Method;
import java.util.Properties;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An abstract factory for acquiring transactions from this J2EE container.
 *
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public abstract class AbstractTransactionManagerFactory
implements TransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            AbstractTransactionManagerFactory.class);
    
    //--------------------------------------------------------------------------

    /**
     * Get name of the factory class.
     * 
     * @return Name of the factory class.
     */
    public abstract String getFactoryClassName();
    
    /**
     * Get name of the factory method.
     * 
     * @return Name of the factory method.
     */
    public abstract String getFactoryMethodName();
    
    /**
     * @see org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory
     *      #getTransactionManager(org.exolab.castor.jdo.conf.JdoConf)
     */
    public final TransactionManager getTransactionManager(final Properties properties)
    throws TransactionManagerAcquireException {
        TransactionManager transactionManager;
        try {
            Class factory = Class.forName(getFactoryClassName());
            Method method = factory.getMethod(getFactoryMethodName(), (Class[]) null);
            Object obj = method.invoke(factory, (Object[]) null);
            transactionManager = (TransactionManager) obj;
        } catch (Exception ex) {
            String msg = "Unable to acquire instance of "
                       + "javax.transaction.TransactionManager: " + getName();
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg, ex);
        }
        
        if (transactionManager == null) {
            String msg = "Unable to acquire instance of "
                       + "javax.transaction.TransactionManager: " + getName();
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg);
        }
        
        return transactionManager;
    }

    //--------------------------------------------------------------------------
}

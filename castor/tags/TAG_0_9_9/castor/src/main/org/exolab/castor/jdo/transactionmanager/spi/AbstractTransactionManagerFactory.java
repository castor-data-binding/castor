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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.transaction.TransactionManager;

import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerAcquireException;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory;
import org.exolab.castor.util.Messages;

/**
 * An abstract factory for acquiring transactions from this J2EE container.
 *
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class AbstractTransactionManagerFactory
implements TransactionManagerFactory {
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
    public final TransactionManager getTransactionManager(final JdoConf jdoConf)
    throws TransactionManagerAcquireException {
        TransactionManager transactionManager;
        try {
            Class factory = Class.forName(getFactoryClassName());
            Method method = factory.getMethod(getFactoryMethodName(), (Class[]) null);
            Object obj = method.invoke(factory, (Object[]) null);
            transactionManager = (TransactionManager) obj;
        } catch (ClassNotFoundException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", getFactoryClassName()), ex);
        } catch (IllegalAccessException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", getFactoryClassName()), ex);
        } catch (InvocationTargetException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", getFactoryClassName()), ex);
        } catch (NoSuchMethodException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", getFactoryClassName()), ex);
        }
        return transactionManager;
    }

    //--------------------------------------------------------------------------
}

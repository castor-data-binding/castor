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
package org.exolab.castor.jdo.transactionmanager.spi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerAcquireException;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory;
import org.exolab.castor.util.Messages;

/**
 * Transaction manager factory instance to be used with J2EE containers
 * where the transaction manager used is JOTM.
 * 
 * Implements {link org.exolab.castor.jdo.transactionmanager.
 * TransactionManagerFactory}.
 *
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public final class JOTMTransactionManagerFactory implements TransactionManagerFactory {

    private static final Log _log = LogFactory.getLog (JOTMTransactionManagerFactory.class);
    
    private static final String JOTM_CLASS_NAME = "org.objectweb.jotm.Jotm";
    /**
     * The name of the factory. 
     */
    private static final String NAME = "jotm";

    /**
     * @see org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory#getName()
     */
    public String getName() { return NAME; }

    /**
     * @see org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory
     *      #getTransactionManager(org.exolab.castor.jdo.conf.JdoConf)
     */
    public TransactionManager getTransactionManager(final JdoConf jdoConf)
    throws TransactionManagerAcquireException {

        TransactionManager transactionManager = null;

        try {
            synchronized (transactionManager) {
                if (transactionManager == null) {
                    Class jotmClass = Class.forName(JOTM_CLASS_NAME);
                    Constructor constructor = jotmClass.getConstructor(new Class[] {Boolean.class, Boolean.class });
                    Object jotm = constructor.newInstance(new Object[] { Boolean.TRUE, Boolean.FALSE }); 
                    Method method = jotmClass.getMethod("getTransactionmanager", (Class[]) null);
                    Object object = method.invoke(jotm, (Object[]) null);
                    transactionManager = (TransactionManager) object;
                }
            }
        } catch (ClassNotFoundException e) {
            String msg = "Problem finding Class " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        } catch (SecurityException e) {
            String msg = 
                "Problem accessing method getTransactionManager() on " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        } catch (NoSuchMethodException e) {
            String msg = 
                "Problem accessing method getTransactionManager() on " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        } catch (IllegalArgumentException e) {
            String msg = 
                "Problem passing arguments to constructor of  " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        } catch (InstantiationException e) {
            String msg = 
                "Problem instantiating Class " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        } catch (IllegalAccessException e) {
            String msg = 
                "Problem accessing method getTransactionManager() on " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        } catch (InvocationTargetException e) {
            String msg = 
                "Problem invoking method getTransactionManager() on " + JOTM_CLASS_NAME;
            _log.error (msg);
            throw new TransactionManagerAcquireException (msg);
        }
        
        if (transactionManager == null) {
            throw new TransactionManagerAcquireException(Messages.format(
                    "jdo.transaction.failToGetManager", NAME));
        }
        
        return transactionManager;
    }
}

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
package org.exolab.castor.jdo.transactionmanager.spi;

import java.util.Enumeration;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NoInitialContextException;
import javax.transaction.TransactionManager;

import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerAcquireException;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory;
import org.exolab.castor.util.Messages;

/**
 * Transaction manager factory instance to be used with J2EE containers
 * where the transaction manager is bound to the JNDI ENC of the container.
 * 
 * Implements {link org.exolab.castor.jdo.transactionmanager.
 * TransactionManagerFactory}.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class JNDIENCTransactionManagerFactory implements TransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** Default JNDI binding for <tt>javax.transaction.TransactionManager</tt>
     *  instance. */
    private static final String TRANSACTION_MANAGER_NAME = "java:comp/TransactionManager";

    /** The name of the factory. */
    private static final String NAME = "jndi";

    //--------------------------------------------------------------------------

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
        Properties properties = new Properties();
        TransactionDemarcation demarcation = jdoConf.getTransactionDemarcation();
        Enumeration parameters = demarcation.getTransactionManager().enumerateParam();
        while (parameters.hasMoreElements()) {
            Param param = (Param) parameters.nextElement();
            properties.put(param.getName(), param.getValue());
        }

        TransactionManager transactionManager;
        Object objectFound = null;
        String jndiENC = properties.getProperty("jndiEnc", TRANSACTION_MANAGER_NAME);
        try {
            objectFound = new InitialContext().lookup(jndiENC);
            transactionManager = (TransactionManager) objectFound;  
        } catch (ClassCastException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToCastToManager",
                objectFound.getClass().getName()), ex); 
        } catch (NoInitialContextException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", jndiENC), ex);
        } catch (NameNotFoundException ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", jndiENC), ex);
        } catch (Exception ex) {
            throw new TransactionManagerAcquireException(Messages.format(
                "jdo.transaction.failToGetManager", jndiENC), ex);
        }

        if (transactionManager==null) {
            throw new TransactionManagerAcquireException(Messages.format(
                    "jdo.transaction.failToGetManager", jndiENC));
        }
        
        return transactionManager;
    }

    //--------------------------------------------------------------------------
}

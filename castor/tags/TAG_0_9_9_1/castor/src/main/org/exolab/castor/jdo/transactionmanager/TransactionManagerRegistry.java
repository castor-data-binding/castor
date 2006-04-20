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
package org.exolab.castor.jdo.transactionmanager;

import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.IdentityMap;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.jdo.transactionmanager.spi.LocalTransactionManagerFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.Messages;

/**
 * Registry for {@link TransactionManager} implementations obtained by the configuartion
 * they are specified in.
 * 
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TransactionManagerRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TransactionManagerRegistry.class);
    
    /** Name of the LocalTransactionManagerFactory. */
    private static final String LOCAL_TX_NAME = LocalTransactionManagerFactory.NAME;

    /** Association between configuration and TransactionManager instance. */
    private static IdentityMap  _managers = new IdentityMap();

    //--------------------------------------------------------------------------

    /**
     * Get TransactionManager for the given configuration. If we previously created a
     * TransactionManager for the same configuration instance we need to return the
     * same TransactionManager instance. Therefore this method needs to be synchronized
     * and we need to use a IdentityMap.
     * 
     * @param jdoConf   The configuration we need a TransactionManager for.
     * @return The TransactionManager.
     * @throws MappingException If any failure occures at creation of TransactionManager.
     */
    public static synchronized TransactionManager getTransactionManager(
            final JdoConf jdoConf) throws MappingException {
        
        TransactionManager manager; 
        if (_managers.containsKey(jdoConf)) {
            // If map contains a TransactionManager for the configuration we use it.
            // We need to check with contains first as we get null in local mode.
            manager = (TransactionManager) _managers.get(jdoConf);
        } else {
            // If we did not find a TransactionManager in the map we need to get a
            // new one. Therefore we first need the mode configured at JdoConf. If
            // no mode is configured at JdoConf we are not able to get the new
            // TransactinManager so we throw an exception to indicate that user
            // should change the configuration.
            String mode = null;
            TransactionDemarcation demarcation = jdoConf.getTransactionDemarcation();
            if (LOCAL_TX_NAME.equals(demarcation.getMode())) {
                mode = LOCAL_TX_NAME;
            } else if (demarcation.getTransactionManager() != null) {
                mode = demarcation.getTransactionManager().getName();
            } else {
                String msg = Messages.message("jdo.transaction.missingConfiguration");
                LOG.error(msg);
                throw new MappingException(msg);
            }

            // Try to obtain a TransactionManagerFactory instance for configured mode
            // from the registry. If the returned TransactionManagerFactory instance
            // is null, throw an exception to indicate that we cannot live without a
            // valid TransactionManagerFactory instance and that the user should
            // change the configuration.
            TransactionManagerFactory factory =
                TransactionManagerFactoryRegistry.getTransactionManagerFactory(mode);
        
            if (factory == null) {
                String msg = Messages.format("jdo.transaction.missingFactory", mode);
                LOG.error(msg);
                throw new MappingException(msg);
            }

            // As we now have a TransactionManagerFactroy we can try to obtain a new
            // TransactionManager with the parameters configured at JdoConf from the
            // factory. If any failure occures loading the TransactionManager we will
            // catch that exception log that failure and throw the exception wrapped
            // into another one again.
            try {
                manager = factory.getTransactionManager(jdoConf);
            } catch (TransactionManagerAcquireException ex) {
                String msg = Messages.format("jdo.transaction.failToGetManager", mode);
                LOG.error(msg, ex);
                throw new MappingException(msg, ex);
            }
            
            // The TransactionManager need to be put into map.
            _managers.put(jdoConf, manager);
        }
        return manager;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Hide default constructor. 
     */
    private TransactionManagerRegistry() { }

    //--------------------------------------------------------------------------
}

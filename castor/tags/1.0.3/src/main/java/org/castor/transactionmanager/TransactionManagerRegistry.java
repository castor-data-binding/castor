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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

/**
 * Registry for {@link TransactionManager} instances obtained by their name.
 * 
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @since 1.0
 */
public final class TransactionManagerRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TransactionManagerRegistry.class);
    
    /** Registry to get TransactionManagerFactory's from. */
    private TransactionManagerFactoryRegistry _registry;
    
    /** Shell the TransactionManager be initialized at registration or lazily when
     *  requested for the first time? */
    private boolean _initializationAtRegistration;

    /** Association between TransactionManager name and instance. */
    private Map _managers = new HashMap();

    //--------------------------------------------------------------------------

    /**
     * Loader class for TransactionManager's.
     */
    private class Loader {
        /** The TransactionManagerFactory to get the TransactionManager from. */
        private TransactionManagerFactory _factory;
        
        /** The properties passed to the TransactionManager at initialization. */
        private Properties _properties;
        
        /** The TransactionManager instance. */
        private TransactionManager _managerInstance;
        
        /**
         * Construct a loader for a TransactionManager that is requested form the
         * TransactionManagerFactory with given name and initialized with given
         * properties.
         * 
         * @param factoryName The name of the TransactionManagerFactory.
         * @param properties The properties passed to the TransactionManager.
         * @throws TransactionManagerAcquireException If TransactoinManagerFactory
         *         with given name could not be found.
         */
        public Loader(final String factoryName, final Properties properties)
        throws TransactionManagerAcquireException {
            _factory = _registry.getTransactionManagerFactory(factoryName);
            _properties = properties;
            _managerInstance = null;
        }
        
        /**
         * Construct a dummy loader with the given TransactionManager instance.
         * 
         * @param managerInstance The TransactionManager instance.
         */
        public Loader(final TransactionManager managerInstance) {
            _factory = null;
            _properties = null;
            _managerInstance = managerInstance;
        }
        
        /**
         * Get the TransactionManager instance.
         * 
         * @return The TransactionManager instance.
         * @throws TransactionManagerAcquireException If any failure occured at
         *         initialization of the TransactionManager.
         */
        public TransactionManager getManagerInstance()
        throws TransactionManagerAcquireException {
            initialize();
            return _managerInstance;
        }
        
        /**
         * Initialize the TransactionManager instance.
         * 
         * @throws TransactionManagerAcquireException If any failure occured at
         *         initialization of the TransactionManager.
         */
        public synchronized void initialize()
        throws TransactionManagerAcquireException {
            if (_managerInstance == null) {
                _managerInstance = _factory.getTransactionManager(_properties);
            }
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * Construct an instance of TransactionManagerRegistry that uses given
     * LocalConfiguration to get required configuration properties.
     * 
     * @param config The LocalConfiguration.
     */
    public TransactionManagerRegistry(final Configuration config) {
        _registry = new TransactionManagerFactoryRegistry(config);

        _initializationAtRegistration = config.getProperty(
                ConfigKeys.TRANSACTION_MANAGER_INIT, false);
    }

    //--------------------------------------------------------------------------

    /**
     * Register TransactionManager with given name. The method requires the name
     * of the TransactionManagerFactory and properties to initialize the manager
     * to be passed as parameters. Depending on the value of configuration property
     * <tt>org.castor.jdo.TransactionManagerInitializeAtRegistration</tt> the
     * manager will be initialized at registration or lazily when requested.
     * 
     * @param managerName The name of the TransactionManager to register.
     * @param factoryName The name of the TransactionManagerFactory used to get
     *                    the factory from TransactionManagerFactoryRegistry.
     * @param properties The properties passed to the TransactionManager at
     *                   initialization.
     * @throws TransactionManagerAcquireException If a TransactionManager with the
     *         same name has already been registered or if any failure occured at
     *         initialization of the TransactionManager.
     */
    public void registerTransactionManager(final String managerName,
            final String factoryName, final Properties properties)
    throws TransactionManagerAcquireException {
        Loader loader = new Loader(factoryName, properties);
        registerTransactionManager(managerName, loader);
        if (_initializationAtRegistration) { loader.initialize(); }
    }
    
    /**
     * Register given TransactionManager with given name. 
     *  
     * @param managerName The name of the TransactionManager to register.
     * @param managerInstance The TransactionManager instance.
     * @throws TransactionManagerAcquireException If a TransactionManager with the
     *         same name has already been registered.
     */
    public void registerTransactionManager(final String managerName,
            final TransactionManager managerInstance)
    throws TransactionManagerAcquireException {
        Loader loader = new Loader(managerInstance);
        registerTransactionManager(managerName, loader);
    }
    
    /**
     * Register TransactionManager with given name. The given loader may hold a
     * preconfigured TransactionManager instance or only the information required
     * to acquire one.
     *  
     * @param managerName The name of the TransactionManager to register.
     * @param loader The loader for the TransactionManager.
     * @throws TransactionManagerAcquireException If a TransactionManager with the
     *         same name has already been registered.
     */
    private void registerTransactionManager(final String managerName, final Loader loader)
    throws TransactionManagerAcquireException {
        synchronized (_managers) {
            if (_managers.containsKey(managerName)) {
                String msg = "A TransactionManager with the name '" + managerName + "' "
                           + "has already been registered.";
                LOG.error(msg);
                throw new TransactionManagerAcquireException(msg);
            }
            _managers.put(managerName, loader);
        }
    }
    
    /**
     * Deregister TransactionManager with given name.
     * 
     * @param managerName The name of the TransactionManager to deregister.
     */
    public void deregisterTransactionManager(final String managerName) {
        synchronized (_managers) {
            _managers.remove(managerName);
        }
    }
    
    /**
     * Returns the names of all the registered {@link TransactionManager}'s. 
     *
     * @return Names of {@link TransactionManager}'s.
     */
    public String[] getTransactionManagerNames() {
        synchronized (_managers) {
            String[] names = new String[_managers.size()];
            return (String[]) _managers.keySet().toArray(names);
        }
    }

    /**
     * Get the TransactionManager with given name.
     * 
     * @param managerName The name of the TransactionManager to return.
     * @return The TransactionManager.
     * @throws TransactionManagerAcquireException If a TransactionManager with name
     *         has not been registered or if any failure occured at initialization
     *         of the TransactionManager.
     */
    public TransactionManager getTransactionManager(final String managerName)
    throws TransactionManagerAcquireException {
        Loader loader = null;
        synchronized (_managers) {
            loader = (Loader) _managers.get(managerName);
        }

        if (loader == null) {
            String msg = "A TransactionManager with the name '" + managerName + "' "
                       + "has not been registered.";
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg);
        }
        
        return loader.getManagerInstance();
    }
    
    //--------------------------------------------------------------------------
}

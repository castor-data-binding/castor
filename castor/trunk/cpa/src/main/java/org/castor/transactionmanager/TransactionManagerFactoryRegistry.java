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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;

/**
 * Registry for {@link TransactionManagerFactory} implementations obtained from the
 * Castor properties file and used by the JDO configuration file.
 * 
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-08 08:58:10 -0600 (Sat, 08 Apr 2006) $
 * @since 1.0
 */
public final class TransactionManagerFactoryRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            TransactionManagerFactoryRegistry.class);

    /** Association between name of implementation and TransactionManagerFactory instance. */
    private Map<String, TransactionManagerFactory> _factories =
        new HashMap<String, TransactionManagerFactory>();

    //--------------------------------------------------------------------------

    /**
     * Construct an instance of TransactionManagerFactoryRegistry that loads the
     * {@link TransactionManagerFactory} implementations specified in the given
     * properties.
     * 
     * @param properties The properties.
     */
    public TransactionManagerFactoryRegistry(final AbstractProperties properties) {
        ClassLoader loader = properties.getApplicationClassLoader();
        Object[] objects = properties.getObjectArray(
                CPAProperties.TRANSACTION_MANAGER_FACTORIES, loader);
        for (int i = 0; i < objects.length; i++) {
            TransactionManagerFactory factory = (TransactionManagerFactory) objects[i];
            _factories.put(factory.getName(), factory);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the names of all the configured {@link TransactionManagerFactory}
     * implementations. A {@link TransactionManagerFactory} instance can be obtained
     * by the {@link #getTransactionManagerFactory} method.
     *
     * @return Names of {@link TransactionManagerFactory} implementations
     */
    public String[] getTransactionManagerFactoryNames() {
        String[] names = new String[_factories.size()];
        return _factories.keySet().toArray(names);
    }

    /**
     * Returns a {@link TransactionManagerFactory} with the specified name. Returns
     * null if the named factory is not supported.
     *
     * @param name The name of the TransactionManagerFactory.
     * @return The {@link TransactionManagerFactory} or null if none exists.
     * @throws TransactionManagerAcquireException If TransactoinManagerFactory
     *         with given name could not be found.
     */
    public TransactionManagerFactory getTransactionManagerFactory(final String name)
    throws TransactionManagerAcquireException {
        TransactionManagerFactory factory = _factories.get(name);
        if (factory == null) {
            String msg = "The TransactionManagerFactory '" + name + "' "
                       + "does not exist in the Castor properties file "
                       + "and is therefore not supported.";
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg);
        }
        return factory;
    }

    //--------------------------------------------------------------------------
}

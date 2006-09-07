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
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

/**
 * Registry for {@link TransactionManagerFactory} implementations obtained from the
 * Castor properties file and used by the JDO configuration file.
 * 
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-08 08:58:10 -0600 (Sat, 08 Apr 2006) $
 * @since 1.0
 */
public final class TransactionManagerFactoryRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            TransactionManagerFactoryRegistry.class);

    /** Association between name of {@link TransactionManager} implementation and 
     *  TransactionManagerFactory instance. */
    private Map _factories = new HashMap();

    //--------------------------------------------------------------------------

    /**
     * Construct an instance of TransactionManagerFactoryRegistry that loads the
     * {@link TransactionManagerFactory} implementations specified in the given
     * LocalConfiguration.
     * 
     * @param config The LocalConfiguration.
     */
    public TransactionManagerFactoryRegistry(final Configuration config) {
        String prop = config.getProperty(ConfigKeys.TRANSACTION_MANAGER_FACTORIES, "");
        StringTokenizer tokenizer = new StringTokenizer(prop, ", ");
        ClassLoader loader = TransactionManagerFactoryRegistry.class.getClassLoader();
        while (tokenizer.hasMoreTokens()) {
            String classname = tokenizer.nextToken();
            try {
                Class cls = loader.loadClass(classname);
                Object obj = cls.newInstance();
                TransactionManagerFactory factory = (TransactionManagerFactory) obj;
                _factories.put(factory.getName(), factory);
            } catch (Exception except) {
                LOG.error("The TransactionManagerFactory " + classname + " "
                        + "specified in the Castor properties file could not "
                        + "be instantiated.");
            }
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
        return (String[]) _factories.keySet().toArray(names);
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
        Object factory = _factories.get(name);
        if (factory == null) {
            String msg = "The TransactionManagerFactory '" + name + "' "
                       + "does not exist in the Castor properties file "
                       + "and is therefore not supported.";
            LOG.error(msg);
            throw new TransactionManagerAcquireException(msg);
        }
        return (TransactionManagerFactory) factory;
    }

    //--------------------------------------------------------------------------
}

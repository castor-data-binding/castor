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
package org.exolab.castor.jdo.transactionmanager;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Messages;

/**
 * Registry for {@link TransactionManagerFactory} implementations
 * obtained from the Castor properties file and used by the
 * JDO mapping configuration file.
 * 
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TransactionManagerFactoryRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(
            TransactionManagerFactoryRegistry.class);

    /** Property listing all the available {@link TransactionManagerFactory}
     *  implementations (<tt>org.exolab.castor.jdo.transactionManagerFactories</tt>). */
    private static final String PROPERTY_TRANSACTION_MANAGER_FACTORY = 
        "org.exolab.castor.jdo.spi.transactionManagerFactories";

    /** Association between name of {@link TransactionManager} implementation and 
     *  TransactionManagerFactory instance. */
    private static Hashtable  _factories = null;

    //--------------------------------------------------------------------------

    /**
     * Returns the names of all the configured {@link TransactionManagerFactory}
     * implementations. A {@link TransactionManagerFactory} instance can be obtained
     * by one of the {@link #getTransactionManagerFactory} methods.
     *
     * @return Names of {@link TransactionManagerFactory} implementations
     */
    public static String[] getTransactionManagerFactoryNames() {
        load();
        
        String[] names = new String[_factories.size()];
        Enumeration enumeration = _factories.keys();
        for (int i = 0; i < names.length; ++i) {
            names[i] = (String) enumeration.nextElement();
        }
        return names;
    }

    /**
     * Returns a {@link TransactionManagerFactory} with the specified name. The factory
     * class names are loaded from the Castor properties file. Returns null if the named
     * factory is not supported.
     *
     * @param name The name of the TransactionManagerFactory.
     * @return The {@link TransactionManagerFactory} or null if none exists with this
     *         name.
     */
    public static TransactionManagerFactory getTransactionManagerFactory(
            final String name) {
        
        // Try to omit entering the synchronized load() method.
        if (_factories == null) { load(); }
        return (TransactionManagerFactory) _factories.get(name);
    }

    /**
     * Load the {@link TransactionManagerFactory} implementations from the properties
     * file, if not loaded before.
     */
    private static void load() {
        if (_factories == null) {
            // Create a new map for the factories but don't assign it to the static
            // property as we try to omit synchronization by checking if static map
            // is initialized.
            Hashtable factories = new Hashtable();
            
            LocalConfiguration cfg = LocalConfiguration.getInstance();
            String prop = cfg.getProperty(PROPERTY_TRANSACTION_MANAGER_FACTORY, "");
            StringTokenizer tokenizer = new StringTokenizer(prop, ", ");
            ClassLoader loader = TransactionManagerFactoryRegistry.class.getClassLoader();
            while (tokenizer.hasMoreTokens()) {
                String classname = tokenizer.nextToken();
                try {
                    Class cls = loader.loadClass(classname);
                    Object obj = cls.newInstance();
                    TransactionManagerFactory factory = (TransactionManagerFactory) obj;
                    factories.put(factory.getName(), factory);
                } catch (Exception except) {
                    LOG.error(Messages.format(
                            "jdo.transaction.failToCreateFactory", classname));
                }
            }
            
            // As the map is initialized now we are on the save side to assign it to
            // the static property.
            _factories = factories;
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * Hide default constructor. 
     */
    private TransactionManagerFactoryRegistry() { }

    //--------------------------------------------------------------------------
}

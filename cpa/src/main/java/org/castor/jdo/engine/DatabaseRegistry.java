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
package org.castor.jdo.engine;

import java.util.Hashtable;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;
import org.castor.core.util.Messages;
import org.castor.cpa.CPAConfiguration;
import org.castor.jdo.conf.Database;
import org.castor.jdo.conf.DatabaseChoice;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.util.JDOClassDescriptorResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 * @since 0.9.9
 */
public final class DatabaseRegistry {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(DatabaseRegistry.class);

    /** Map of all registered connection factories by name. */
    private static final Hashtable  FACTORIES = new Hashtable();

    /**
     * Instantiates a DataSourceConnectionFactory with given name, engine, datasource
     * and mapping.
     * 
     * @param name       The Name of the database configuration.
     * @param engine     The Name of the persistence factory to use.
     * @param datasource The preconfigured datasource to use for creating connections.
     * @param mapping    The previously loaded mapping.
     * @param txManager  The transaction manager to use.
     * @throws MappingException If LockEngine could not be initialized.
     */
    public static synchronized void loadDatabase(
            final String name, final String engine, final DataSource datasource,
            final Mapping mapping, final TransactionManager txManager)
    throws MappingException {
        AbstractConnectionFactory factory = new DataSourceConnectionFactory(
                name, engine, datasource, mapping, txManager);
        
        if (FACTORIES.put(name, factory) != null) {
            LOG.warn(Messages.format("jdo.configLoadedTwice", name));
        }
    }

    /**
     * Instantiates a ConnectionFactory from the JDO configuration file.
     * 
     * @param  source   {@link InputSource} pointing to the JDO configuration. 
     * @param  resolver An entity resolver.
     * @param  loader   A class loader
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase(final InputSource source,
                                                 final EntityResolver resolver,
                                                 final ClassLoader loader)
    throws MappingException {
        loadDatabase(source, resolver, loader, null);
    }

    /**
     * Instantiates a ConnectionFactory from the JDO configuration file.
     * 
     * @param source
     *                {@link InputSource} pointing to the JDO configuration.
     * @param resolver
     *                An entity resolver.
     * @param loader
     *                A class loader
     * @param classDescriptorResolver
     *                {@link ClassDescriptorResolver} used for class to class
     *                descriptor resolution.
     * @throws MappingException
     *                 If the database cannot be instantiated/loaded.
     */
    public static synchronized void loadDatabase(final InputSource source,
            final EntityResolver resolver, final ClassLoader loader,
            final JDOClassDescriptorResolver classDescriptorResolver)
            throws MappingException {
        
        // Load the JDO configuration file from the specified input source.
        JdoConf jdoConf = null;
        jdoConf = JDOConfFactory.createJdoConf(source, resolver, loader);
        LOG.debug("Loaded jdo conf successfully"); 

        loadDatabase(jdoConf, resolver, loader, source.getSystemId(), classDescriptorResolver);
    }

    /**
     * Creates a entry for every database and associates them with their name in a
     * map. It then instantiates all databases if
     * 'org.exolab.castor.jdo.DatabaseInitializeAtLoad' key can not be found or is
     * set to <code>true</code> in castor.properties file. If above property is set
     * to <code>false</code> it will instantiate all databases only when they are
     * needed.
     * 
     * @param  jdoConf      An in-memory jdo configuration. 
     * @param  resolver     An entity resolver.
     * @param  loader       A class loader
     * @param  baseURI      The base URL for the mapping
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase(final JdoConf jdoConf,
                                                  final EntityResolver resolver,
                                                  final ClassLoader loader,
                                                  final String baseURI)
    throws MappingException {
        loadDatabase(jdoConf, resolver, loader, baseURI, null);
    }

    /**
     * Creates a entry for every database and associates them with their name in
     * a map. It then instantiates all databases if
     * 'org.exolab.castor.jdo.DatabaseInitializeAtLoad' key can not be found or
     * is set to <code>true</code> in castor.properties file. If above
     * property is set to <code>false</code> it will instantiate all databases
     * only when they are needed.
     * 
     * @param jdoConf
     *                An in-memory jdo configuration.
     * @param resolver
     *                An entity resolver.
     * @param loader
     *                A class loader
     * @param baseURI
     *                The base URL for the mapping
     * @param classDescriptorResolver
     *                {@link ClassDescriptorResolver} used for class to class
     *                descriptor resolution.
     * @throws MappingException
     *                 If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase(final JdoConf jdoConf,
            final EntityResolver resolver, final ClassLoader loader,
            final String baseURI,
            final JDOClassDescriptorResolver classDescriptorResolver)
            throws MappingException {
        // Do we need to initialize database now or should we
        // wait until we want to use it.
        Configuration cfg = CPAConfiguration.getInstance();
        boolean init = cfg.getBoolean(CPAConfiguration.INITIALIZE_AT_LOAD, true);
        
        // Load the JDO configuration file from the specified input source.
        // databases = JDOConfLoader.getDatabases(baseURI, resolver);
        Database[] databases = jdoConf.getDatabase();
        AbstractConnectionFactory factory;
        for (int i = 0; i < databases.length; i++) {
            // Load the mapping file from the URL specified in the database
            // configuration file, relative to the configuration file.
            // Fail if cannot load the mapping for whatever reason.
            Mapping mapping = new Mapping(loader);
            if (resolver != null) { mapping.setEntityResolver(resolver); }
            if (baseURI != null) { mapping.setBaseURL(baseURI); }
            
            factory = DatabaseRegistry.createFactory(jdoConf, i, mapping);
            factory.setClassDescriptorResolver(classDescriptorResolver);
            if (init) { factory.initialize(); }
            String name = databases[i].getName();
            if (FACTORIES.put(name, factory) != null) {
                LOG.warn(Messages.format("jdo.configLoadedTwice", name));
            }
        }
    }
    
    /**
     * Factory methode to create a ConnectionFactory for given database configuration
     * and given mapping.
     * 
     * @param jdoConf   An in-memory jdo configuration. 
     * @param index     Index of the database configuration inside the jdo configuration.
     * @param mapping   The mapping to load.
     * @return The ConnectionFactory.
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    private static AbstractConnectionFactory createFactory(
            final JdoConf jdoConf, final int index, final Mapping mapping)
    throws MappingException {
        AbstractConnectionFactory factory;
        
        DatabaseChoice choice = jdoConf.getDatabase(index).getDatabaseChoice();
        if (choice == null) {
            String name = jdoConf.getDatabase(index).getName();
            String msg = Messages.format("jdo.missingDataSource", name);
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        if (choice.getDriver() != null) {
            // JDO configuration file specifies a driver, use the driver
            // properties to create a new registry object.
            factory = new DriverConnectionFactory(jdoConf, index, mapping);
        } else if (choice.getDataSource() != null) {
            // JDO configuration file specifies a DataSource object, use the
            // DataSource which was configured from the JDO configuration file
            // to create a new registry object.
            factory = new DataSourceConnectionFactory(jdoConf, index, mapping);
        } else if (choice.getJndi() != null) {
            // JDO configuration file specifies a DataSource lookup through JNDI, 
            // locate the DataSource object frome the JNDI namespace and use it.
            factory = new JNDIConnectionFactory(jdoConf, index, mapping);
        } else {
            String name = jdoConf.getDatabase(index).getName();
            String msg = Messages.format("jdo.missingDataSource", name);
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        return factory;
    }

    /**
     * Check if any database configuration has been loaded.
     * 
     * @return <code>true</code> if a databases configuration has been loaded.
     */
    public static boolean hasDatabaseRegistries() {
        return (!FACTORIES.isEmpty());
    }
    
    /**
     * Check if database configuration with given name has been loaded.
     * 
     * @param  name     Name of the database to check if loaded.
     * @return <code>true</code> if databases configuration has been loaded.
     */
    public static boolean isDatabaseRegistred(final String name) {
        return FACTORIES.containsKey(name);
    }
    
    /**
     * Get the ConnectionFactory for the given database name.
     * 
     * @param  name     Name of the database configuration.
     * @return The ConnectionFactory for the given database name.
     * @throws MappingException If database can not be instantiated.
     */
    public static AbstractConnectionFactory getConnectionFactory(final String name)
    throws MappingException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Fetching ConnectionFactory: " + name);
        }
        
        AbstractConnectionFactory factory;
        factory = (AbstractConnectionFactory) FACTORIES.get(name);
        
        if (factory == null) {
            String msg = Messages.format("jdo.missingDataSource", name);
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        factory.initialize();
        return factory;
    }

    /**
     * Reset all database configurations.
     */
    public static void clear() {
        FACTORIES.clear();
    }
    
    /**
     * Unload the database configuration with given name.
     * 
     * @param  name     Name of the database to be unloaded.
     */
    public static void unloadDatabase(final String name) {
        FACTORIES.remove(name);
    }
    
    /**
     * Hide constructor of utility class.
     */
    private DatabaseRegistry() { }
    
}

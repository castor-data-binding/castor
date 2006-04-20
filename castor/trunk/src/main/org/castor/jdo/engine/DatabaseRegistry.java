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


import org.castor.jdo.conf.Database;
import org.castor.jdo.conf.DatabaseChoice;
import org.castor.jdo.conf.JdoConf;
import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;
import org.castor.util.Messages;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public final class DatabaseRegistry {
    //--------------------------------------------------------------------------

    /** Temporary note to check for the changed jdo-conf syntax. */
    private static final String NOTE_096 =
        "NOTE: JDO configuration syntax has changed with castor 0.9.6, "
      + "please see http://castor.codehaus.org/release-notes.html for details";
  
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(DatabaseRegistry.class);

    /** Map of all registered connection factories by name. */
    private static final Hashtable  FACTORIES = new Hashtable();

    //--------------------------------------------------------------------------

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
     * Instantiates a ConnectionFactory from an in-memory JDO configuration.
     * 
     * @param  jdoConf  An in-memory jdo configuration. 
     * @param  resolver An entity resolver.
     * @param  loader   A class loader
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase(final JdoConf jdoConf,
                                                 final EntityResolver resolver,
                                                 final ClassLoader loader)
    throws MappingException {
        loadDatabase(jdoConf, resolver, loader, null);
    }

    /**
     * Instantiates a ConnectionFactory from the JDO configuration file
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
        // Load the JDO configuration file from the specified input source.
        JdoConf jdoConf = null;
        
        Unmarshaller unmarshaller = new Unmarshaller(JdoConf.class);
        try {
            unmarshaller.setEntityResolver(new DTDResolver(resolver));
            jdoConf = (JdoConf) unmarshaller.unmarshal(source);
        } catch (MarshalException e) {
            LOG.info(NOTE_096);
            throw new MappingException(e); 
        } catch (ValidationException e) {
            throw new MappingException(e);
        }
        
        LOG.debug("Loaded jdo conf successfully"); 

        loadDatabase(jdoConf, resolver, loader, source.getSystemId());
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
    private static synchronized void loadDatabase(final JdoConf jdoConf,
                                                  final EntityResolver resolver,
                                                  final ClassLoader loader,
                                                  final String baseURI)
    throws MappingException {
        // Do we need to initialize database now or should we
        // wait until we want to use it.
        Configuration cfg = Configuration.getInstance();
        boolean init = cfg.getProperty(ConfigKeys.INITIALIZE_AT_LOAD, true);
        
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
    
    //--------------------------------------------------------------------------
    
    /**
     * Hide constructor of utility class.
     */
    private DatabaseRegistry() { }
    
    //--------------------------------------------------------------------------
}

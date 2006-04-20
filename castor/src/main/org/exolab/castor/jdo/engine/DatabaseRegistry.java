/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.DatabaseChoice;
import org.exolab.castor.jdo.conf.Driver;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.drivers.ConnectionProxy;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.PersistenceEngineFactory;
import org.exolab.castor.persist.PersistenceFactoryRegistry;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Messages;
import org.exolab.castor.xml.UnmarshalHandler;
import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class DatabaseRegistry {
    //--------------------------------------------------------------------------

    /**
     * The name of the generic SQL engine, if no SQL engine specified.
     */
    public static final String GENERIC_ENGINE = "generic";

    /**
     * Property telling if database should be initialized when loading.
     */
    private static final String INITIALIZE_AT_LOAD = 
        "org.exolab.castor.jdo.DatabaseInitializeAtLoad";
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(DatabaseRegistry.class);

    /**
     * Listings of all registered databases by name.
     */
    private static final Hashtable  DATABASES = new Hashtable();

    /**
     * Database instances referenced by engine. 
     */
    private static final Hashtable  BY_ENGINES = new Hashtable();

    //--------------------------------------------------------------------------

    /**
     * Instantiates a database instance from an in-memory JDO configuration.
     * 
     * @param  jdoConf  An in-memory JDO configuration. 
     * @param  resolver An entity resolver.
     * @param  loader   A class loader
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase(final JdoConf jdoConf,
                                                 final EntityResolver resolver,
                                                 final ClassLoader loader)
    throws MappingException {
        Database[] databases = JDOConfLoader.getDatabases(jdoConf);
        loadDatabase(databases, resolver, loader, null);
    }

    /**
     * Instantiates a database instance from the JDO configuration file
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
        Database[] databases = JDOConfLoader.getDatabases(source, resolver);
        loadDatabase(databases, resolver, loader, source.getSystemId());
    }
    
    /**
     * Creates a entry for every database and associates them with their name in a
     * map. It then instantiates all databases by calling initDatabase(String) if
     * 'org.exolab.castor.jdo.DatabaseInitializeAtLoad' key can not be found or is
     * set to <code>true</code> in castor.properties file. If above property is set
     * to <code>false</code> it will not instantiate all databases yet but when
     * calling getDatabaseRegistry(String).
     * 
     * @param  databases    Database configuration instances. 
     * @param  resolver     An entity resolver.
     * @param  loader       A class loader
     * @param  baseURI      The base URL for the mapping
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    private static synchronized void loadDatabase(final Database[] databases,
                                                  final EntityResolver resolver,
                                                  final ClassLoader loader,
                                                  final String baseURI)
    throws MappingException {
        // Do we need to initialize database now or should we
        // wait until we want to use it.
        LocalConfiguration cfg = LocalConfiguration.getInstance();
        String property = cfg.getProperty(INITIALIZE_AT_LOAD, "true");
        boolean init = Boolean.valueOf(property).booleanValue();
        
        // Load the JDO configuration file from the specified input source.
        // databases = JDOConfLoader.getDatabases (baseURI, resolver);
        for (int i = 0; i < databases.length; i++) {
            Database database = databases[i];
            
            // Load the mapping file from the URL specified in the database
            // configuration file, relative to the configuration file.
            // Fail if cannot load the mapping for whatever reason.
            Mapping mapping = new Mapping(loader);
            if (resolver != null) { mapping.setEntityResolver(resolver); }
            if (baseURI != null) { mapping.setBaseURL(baseURI); }
            
            Object oldEntry;
            oldEntry = DATABASES.put(database.getName(), new RegEntry(database, mapping));
            if (oldEntry != null) {
                LOG.warn(Messages.format("jdo.configLoadedTwice", database.getName()));
            }
                        

            if (init) { initDatabase(database.getName()); }
        }
    }

    /**
     * Instantiates a database instance from a entry of databases map.
     * 
     * @param  name     Name of the database to instantiate.
     * @return The requested DatabaseRegistry or null if no entry can be found in
     *         the map and therefore no configuration for the database is available.
     * @throws MappingException If DatabaseRegistry can not be instantiated.
     */
    private static synchronized DatabaseRegistry initDatabase(final String name)
    throws MappingException {
        // If there is no entry for the name the database has
        // not been loaded and can not be configured and we
        // can only ignore to initalize it.
        RegEntry entry = (RegEntry) DATABASES.get(name);
        if (entry == null) { return null; }
        
        // If the database was already initialized, ignore
        // this request to initialize it and return the configured
        // instance.
        if (entry.getRegistry() != null) { return entry.getRegistry(); }
        
        Database database = entry.getDatabase();
        Mapping mapping = entry.getMapping();
        try {
            // Initialize all the mappings of the database.
            Enumeration mappings = database.enumerateMapping();
            org.exolab.castor.jdo.conf.Mapping mapConf;
            while (mappings.hasMoreElements()) {
                mapConf = (org.exolab.castor.jdo.conf.Mapping) mappings.nextElement();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading the mapping descriptor: " + mapConf.getHref());
                }
                
                if (mapConf.getHref() != null) {
                    mapping.loadMapping(mapConf.getHref());
                }
            }
        } catch (MappingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MappingException(ex);
        }

        // Complain if no database engine was specified, otherwise get
        // a persistence factory for that database engine.
        PersistenceFactory factory;
        if (database.getEngine() == null) {
            factory = PersistenceFactoryRegistry.getPersistenceFactory(GENERIC_ENGINE);
        } else {
            factory = PersistenceFactoryRegistry.getPersistenceFactory(
                    database.getEngine());
        }
        
        if (factory == null) {
            String msg = Messages.format(
                    "jdo.noSuchEngine", database.getEngine());
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        if (database.getDatabaseChoice() == null) {
            String msg = Messages.format(
                    "jdo.missingDataSource", database.getName());
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        if (database.getDatabaseChoice().getDriver() != null) {
            // JDO configuration file specifies a driver, use the driver
            // properties to create a new registry object.
            entry.setRegistry(initFromDriver(mapping, database, factory));
        } else if (database.getDatabaseChoice().getDataSource() != null) {
            // JDO configuration file specifies a DataSource object, use the
            // DataSource which was configured from the JDO configuration file
            // to create a new registry object.
            entry.setRegistry(initFromDataSource(mapping, database, factory,
                              mapping.getClassLoader()));
        } else if (database.getDatabaseChoice().getJndi() != null) {
            // JDO configuration file specifies a DataSource lookup through JNDI, 
            // locate the DataSource object frome the JNDI namespace and use it.
            entry.setRegistry(initFromJNDI(mapping, database, factory));
        } else {
            String msg = Messages.format(
                    "jdo.missingDataSource", database.getName());
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        return entry.getRegistry();
    }
    
    /**
     * Initialize DatabaseRegistry instance using a JDBC DataSource instance.
     * 
     * @param  mapping      Mapping instance.
     * @param  database     Configuration of the JDO Database element
     * @param  factory      PersistenceFactory instance.
     * @param  classLoader  ClassLoader to use
     * @return DatabaseRegistry instance.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    public static DatabaseRegistry initFromDataSource(final Mapping mapping,
                                                      final Database database,
                                                      final PersistenceFactory factory,
                                                      final ClassLoader classLoader) 
    throws MappingException {
        DatabaseRegistry dbs;
        DataSource dataSource;
        
        dataSource = loadDataSource(database, classLoader);
        
        dbs = new DatabaseRegistry(database.getName(), 
                                   mapping.getResolver(Mapping.JDO, factory), 
                                   factory,
                                   dataSource);
        
        if (LOG.isDebugEnabled()) {
            DatabaseChoice dbc = database.getDatabaseChoice();
            LOG.debug("Using DataSource: " + dbc.getDataSource().getClassName());
        }
        
        return dbs;
    }
    
    /**
     * Initialize JDBC DataSource instance with the given database configuration
     * instances and the given class loader.
     * 
     * @param  database     Database configuration.
     * @param  loader       ClassLoader to use. 
     * @return The initalized DataSource.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    public static DataSource loadDataSource(final Database database,
                                            final ClassLoader loader) 
    throws MappingException {
        DataSource dataSource;
        Param[] parameters;
        Param param;

        DatabaseChoice dbChoice = database.getDatabaseChoice();
        String className = dbChoice.getDataSource().getClassName();
        ClassLoader classLoader = loader;
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        try {
            Class dsClass = Class.forName(className, true, classLoader);
            dataSource = (DataSource) dsClass.newInstance();
        } catch (Exception e) {
            String msg = Messages.format(
                    "jdo.engine.classNotInstantiable", className);
            LOG.error(msg, e);
            throw new MappingException(msg, e);
        }

        parameters = database.getDatabaseChoice().getDataSource().getParam();
        
        Unmarshaller unmarshaller = new Unmarshaller(dataSource);
        UnmarshalHandler handler = unmarshaller.createHandler();
        
        try {
            handler.startDocument();
            handler.startElement("data-source", null);
            
            for (int i = 0; i < parameters.length; i++) {
               param = (Param) parameters[i];
               handler.startElement(param.getName(), null);
               handler.characters(param.getValue().toCharArray(), 0,
                                  param.getValue().length());
               handler.endElement(param.getName());
            }
            
            handler.endElement("data-source");
            handler.endDocument();
        } catch (SAXException e) {
            String msg = Messages.format(
                    "jdo.engine.unableToParseDataSource", className);
            LOG.error(msg, e);
            throw new MappingException(msg, e);
        }

        return dataSource;
    }

    /**
     * Initialize DatabaseRegistry instance using a JDBC Driver.
     * 
     * @param  mapping  Mapping instance.
     * @param  database Configuration of the JDO Database element
     * @param  factory  PersistenceFactory instance.
     * @return DatabaseRegistry instance.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    private static DatabaseRegistry initFromDriver(final Mapping mapping,
                                                   final Database database,
                                                   final PersistenceFactory factory) 
    throws MappingException {
        DatabaseRegistry dbs;
        Properties  props;
        Enumeration params;
        Param       param;
        
        DatabaseChoice dbChoice = database.getDatabaseChoice();
        String driverName = dbChoice.getDriver().getClassName();
        if (driverName != null) {
            try {
                Class.forName(dbChoice.getDriver().getClassName()).newInstance();
            } catch (InstantiationException e) {
                String msg = Messages.format(
                        "jdo.engine.classNotInstantiable", driverName);
                LOG.error(msg, e);
                throw new MappingException(msg, e);
            } catch (IllegalAccessException e) {
                String msg = Messages.format(
                        "jdo.engine.classNotAccessable", driverName, "constructor");
                LOG.error(msg, e);
                throw new MappingException(msg, e);
            } catch (ClassNotFoundException e) {
                String msg = "Can not load class " + driverName;
                LOG.error(msg, e);
                throw new MappingException(msg, e);
            } 
        }
        
        try {
            Driver driver = dbChoice.getDriver();
            if (DriverManager.getDriver(driver.getUrl()) == null) {
                String msg = Messages.format(
                        "jdo.missingDriver", driver.getUrl());
                LOG.error(msg);
                throw new MappingException(msg);
            }
        } catch (SQLException ex) {
            throw new MappingException(ex);
        }
        
        props = new Properties();
        params = dbChoice.getDriver().enumerateParam();
        while (params.hasMoreElements()) {
            param = (Param) params.nextElement();
            props.put (param.getName(), param.getValue());
        }
        
        dbs = new DatabaseRegistry (database.getName(), 
                                    mapping.getResolver(Mapping.JDO, factory), 
                                    factory,
                                    dbChoice.getDriver().getUrl(), 
                                    props);
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using driver: " + driverName);
        }
        
        return dbs;
    }

    /**
     * Initialize DatabaseRegistry instance using a JDBC DataSource object bound to
     * the JNDI ENC.
     *  
     * @param  mapping  Mapping instance.
     * @param  database Configuration of the JDO Database element
     * @param  factory  PersistenceFactory instance.
     * @return DatabaseRegistry instance.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    private static DatabaseRegistry initFromJNDI(final Mapping mapping, 
                                                 final Database database, 
                                                 final PersistenceFactory factory) 
    throws MappingException {
        DatabaseRegistry dbs;
        Object    dataSource;
        
        DatabaseChoice dbChoice = database.getDatabaseChoice();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Using DataSource from JNDI ENC: " + dbChoice.getJndi().getName());
        }
        
        try {
            Context initialContext = new InitialContext(); 
            dataSource = initialContext.lookup(dbChoice.getJndi().getName());
        } catch (NameNotFoundException e) {
            String msg = Messages.format(
                    "jdo.jndiNameNotFound", dbChoice.getJndi().getName());
            LOG.error(msg, e);
            throw new MappingException(msg, e);
        } catch (NamingException e) {
            throw new MappingException(e);
        }
        
        if (!(dataSource instanceof DataSource)) {
            String msg = Messages.format(
                    "jdo.jndiNameNotFound", dbChoice.getJndi().getName());
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        dbs = new DatabaseRegistry (database.getName(), 
                                    mapping.getResolver(Mapping.JDO, factory),
                                    factory,
                                    (DataSource) dataSource);
        
        return dbs;
    }

    /**
     * Check if any database configuration ahs been loaded.
     * 
     * @return <code>true</code> if a databases configuration has been loaded.
     */
    public static boolean hasDatabaseRegistries() {
        return (!DATABASES.isEmpty());
    }
    
    /**
     * Get the LockEngine of the givven DatabaseRegistry.
     * 
     * @param  dbs      The DatabaseRegistry to get the LockEngine of.
     * @return The LockEngine of the given DatabaseRegistry.
     */
    public static LockEngine getLockEngine(final DatabaseRegistry dbs) {
        return dbs._engine;
    }

    /**
     * Get the DatabaseRegistry with the given name. Will instantiated the requested
     * database if this had not been done before.
     * 
     * @param  name     The name of the DatabaseRegistry to search for.
     * @return The requated DatabaseRegistry.
     * @throws MappingException If DatabaseRegistry can not be instantiated.
     */
    public static synchronized DatabaseRegistry getDatabaseRegistry(final String name)
    throws MappingException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Fetching DatabaseRegistry: " + name);
        }
        
        return initDatabase(name);
    }

    /**
     * Create a JDBC connection for the given LockEngine to execute SQL calls
     * to the database.
     * 
     * @param  engine   The LockEngine that needs the connection.
     * @return The JDBC connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection createConnection(final LockEngine engine)
    throws SQLException {
        DatabaseRegistry dbs = (DatabaseRegistry) BY_ENGINES.get(engine);
        return dbs.createConnection();
    }

    /**
     * Reset the database configuration.
     */
    public static void clear() {
        DATABASES.clear();
        BY_ENGINES.clear();

        // reset the JDO configuration data to re-enable loadConfiguration()
        JDOConfLoader.deleteConfiguration();
    }
    
    //--------------------------------------------------------------------------

    /**
     * Instances of this calss are used as entries at the map og databases. It holds
     * the database configuration and the mapping instance until the DatabaseRegistry
     * is instantiated.
     */
    private static final class RegEntry {
        /**
         * The database configuration. Will be reset to null when DatabaseRegistry
         * is set.
         */
        private Database            _db;
        
        /**
         * The mapping instance to load the mappings at instantiation of the
         * DatabaseRegistry. Will be reset to null when DatabaseRegistry is set.
         */
        private Mapping             _map;
        
        /** The DatabaseRegistry. */
        private DatabaseRegistry    _reg = null;
        
        /**
         * Construct a RegEntry with given database and mapping.
         * 
         * @param  db   The database configuration.
         * @param  map  The mapping instance.
         */
        public RegEntry(final Database db, final Mapping map) {
            _db = db;
            _map = map;
        }
        
        /**
         * Get the database configuration.
         * 
         * @return The database configuration.
         */
        public Database getDatabase() { return _db; }
        
        /**
         * Get the mapping instance.
         * 
         * @return The mapping instance.
         */
        public Mapping getMapping() { return _map; }
        
        /**
         * Set the DatabaseRegistry to the given value and reset database
         * configuration and mapping instance to null.
         * 
         * @param  reg The DatabaseRegistry.
         */
        public void setRegistry(final DatabaseRegistry reg) {
            _db = null;
            _map = null;
            _reg = reg;
        }
        
        /**
         * Get the DatabaseRegistry.
         * 
         * @return The DatabaseRegistry.
         */
        public DatabaseRegistry getRegistry() { return _reg; }
    }

    //--------------------------------------------------------------------------

    /**
     * The JDBC URL when using a JDBC driver.
     */
    private String            _jdbcUrl;

    /**
     * The properties when using a JDBC driver.
     */
    private Properties        _jdbcProps;

    /**
     * The data source when using a DataSource.
     */
    private DataSource        _dataSource;

    /**
     * The map resolver for this database source.
     */
    private MappingResolver   _mapResolver;

    /**
     * The database name of this database source.
     */
    private String            _name;

    /**
     * The presistence engine for this database source.
     */
    private LockEngine        _engine;

    //--------------------------------------------------------------------------

    /**
     * Construct a new database registry using a JDBC driver.
     *
     * @param  name         The database name
     * @param  mapResolver  The mapping resolver
     * @param  factory      Factory for persistence engines
     * @param  jdbcUrl      The JDBC URL
     * @param  jdbcProps    The JDBC properties
     * @throws MappingException Error occured when creating persistence engines
     *         for the mapping descriptors
     */
    DatabaseRegistry(final String name, final MappingResolver mapResolver,
                     final PersistenceFactory factory,
                     final String jdbcUrl, final Properties jdbcProps)
    throws MappingException {
        this(name, mapResolver, factory);
        _jdbcUrl = jdbcUrl;
        _jdbcProps = jdbcProps;
    }

    /**
     * Construct a new database registry using a <tt>DataSource</tt>.
     *
     * @param  name         The database name
     * @param  mapResolver  The mapping resolver
     * @param  factory      Factory for persistence engines
     * @param  dataSource   The data source
     * @throws MappingException Error occured when creating persistence engines
     *         for the mapping descriptors.
     */
    DatabaseRegistry(final String name, final MappingResolver mapResolver,
                     final PersistenceFactory factory,
                     final DataSource dataSource)
    throws MappingException {
        this(name, mapResolver, factory);
        _dataSource = dataSource;
    }
    
    /**
     * Base constructor for a new database registry.
     *
     * @param  name         The database name
     * @param  mapResolver  The mapping resolver
     * @param  factory      Factory for persistence engines
     * @throws MappingException Error occured when creating persistence engines
     *         for the mapping descriptors.
     */
    DatabaseRegistry(final String name, final MappingResolver mapResolver,
                     final PersistenceFactory factory)
    throws MappingException {
        _name = name;
        _mapResolver = mapResolver;
        _engine = new PersistenceEngineFactory().createEngine(mapResolver, factory);
        BY_ENGINES.put(_engine, this);
    }

    /**
     * Get MappingResolver of this DatabaseRegistry.
     * 
     * @return MappingResolver
     */
    public MappingResolver getMappingResolver() {
        return _mapResolver;
    }

    /**
     * Get name of the database configuration for this DatabaseRegistry.
     * 
     * @return Name of the databse configuration.
     */
    public String getName() {
        return _name;
    }

    /**
     * Get DataSource of this DatabaseRegistry.
     * 
     * @return DataSource
     */
    public DataSource getDataSource() {
        return _dataSource;
    }

    /**
     * Create a JDBC connection for this DatabaseRegistry to execute SQL calls
     * to the database.
     * 
     * @return The JDBC connection.
     * @throws SQLException If a database access error occurs.
     */
    public Connection createConnection() throws SQLException {
        if (_dataSource != null) {
            return ConnectionProxy.newConnectionProxy(
                    _dataSource.getConnection(), getClass().getName());
        }
        
        return ConnectionProxy.newConnectionProxy(
                DriverManager.getConnection(_jdbcUrl, _jdbcProps), getClass().getName());
    }

    //--------------------------------------------------------------------------
}

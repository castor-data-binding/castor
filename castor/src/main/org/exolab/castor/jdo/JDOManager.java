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

package org.exolab.castor.jdo;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.jdo.engine.DatabaseImpl;
import org.exolab.castor.jdo.engine.DatabaseRegistry;
import org.exolab.castor.jdo.engine.JDOConfLoader;
import org.exolab.castor.jdo.engine.TxDatabaseMap;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerAcquireException;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerFactoryRegistry;
import org.exolab.castor.jdo.transactionmanager.spi.LocalTransactionManagerFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.exolab.castor.util.Messages;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Implementation of the JDOManager engine used for obtaining database
 * connections. After successful instantiation, {@link #getDatabase()} is used
 * to obtain a new database connection. Any number of database connections can
 * be obtained from the same JDOManager object.
 * <p/>
 * An instance of this class is contructed with a two-step approach: 
 * 
 * <ul>
 *    <li>load the JDOManager configuration file through one of the static
 *        loadConfiguration() methods</li>
 *    <li>create an instance of the JDOManager engine using the factory method
 *        createInstance(String) where you supply one of the database names
 *        defined in the configuration file loaded in step 1.</li>
 * </ul>
 * 
 * Example:
 * <pre>
 * 
 * ...
 *
 * JDOManager jdo;
 * Database db;
 * 
 * try {
 *    <font color="red">// load the JDOManager configuration file</font>
 *    JDOManager.loadConfiguration("jdo-config.xml");
 * 
 *    <font color="red">// construct a new JDOManager for 'mydb'</font>
 *    jdo = JDOManager.createInstance("mydb");
 * 
 *    <font color="red">// open a connection to the database</font>
 *    db = jdo.getDatabase();
 * } catch (MappingException ex) {
 *    ...
 * } catch (DatabaseNotFoundException ex) {
 *    ...
 * }
 * 
 * </pre>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class JDOManager
implements DataObjects, Referenceable, ObjectFactory, Serializable {
    //--------------------------------------------------------------------------

    /**
     * The default lock timeout (specified in seconds).
     */
    public static final int DEFAULT_LOCK_TIMEOUT = 10;
    
    /**
     * Default description.
     */
    public static final String DEFAULT_DESCRIPTION = "Castor JDO";
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(JDOManager.class);

    /**
     * Map of JDOManager objects.
     */
    private static Map _jdoInstances = new HashMap();

    /**
     * The application class loader.
     */
    private static ClassLoader _classLoader;

    /**
     * The resolver can be used to resolve cached entities, e.g. for external
     * mapping documents.
     */
    private static EntityResolver _entityResolver;

    /**
     * Location of the JDOManager configuration file. 
     */
    private static InputSource _source;
    
    //--------------------------------------------------------------------------

    /**
     * Factory method for creating a JDOManager instance for one of the
     * databases configured in the JDOManager configuration file. Please make
     * sure that you call loadConfiguration() first.
     *   
     * @param  databaseName Database name as configured in the JDOManager
     *         configuration file.
     * @return A JDOManager instance.
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the mapping from JDOManager configuration
     *         file.
     */
    public static JDOManager createInstance(final String databaseName)
    throws MappingException {
        if (!DatabaseRegistry.hasDatabaseRegistries()) {
            throw new MappingException(Messages.message(
                    "jdo.missing.jdo.configuration"));
        }
        
        if (DatabaseRegistry.getDatabaseRegistry(databaseName) == null) {
            throw new MappingException(Messages.format(
                    "jdo.missing.database.configuration", databaseName));
        }
            
        JDOManager jdoInstance = (JDOManager) _jdoInstances.get(databaseName);
        
        if (jdoInstance == null) {
            jdoInstance = new JDOManager(databaseName);
            
            jdoInstance.setConfiguration(_source);
            jdoInstance.setEntityResolver(_entityResolver);
            jdoInstance.setClassLoader(_classLoader);
            
            _jdoInstances.put(databaseName, jdoInstance);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully created JDOManager instance: "
                        + jdoInstance);
            }
        }
        
        return jdoInstance;
    }

    /**
     * Load the JDOManager configuration from the specified in-memory JdoConf. In
     * addition, custom entity resolver and class loader for the mappings can be 
     * provided. 
     * 
     * @param  jdoConf  the in-memory JdoConf.
     * @param  resolver An (optional) entity resolver to resolve cached
     *         entities, e.g. for external mapping documents. 
     * @param  loader   The class loader to use, null for the default
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final JdoConf jdoConf,
                                         final EntityResolver resolver,
                                         final ClassLoader loader)
    throws MappingException {
        DatabaseRegistry.loadDatabase(jdoConf, resolver, loader);
        
        _classLoader = loader;
        _entityResolver = resolver;
        
        LOG.debug("Successfully loaded JDOManager form in-memory configuration");
    }
    
    /**
     * Load the JDOManager configuration from the specified in-memory JdoConf. In
     * addition, a custom class loader for the mappings can be provided. 
     * 
     * @param  jdoConf  the in-memory JdoConf.
     * @param  loader   The class loader to use, null for the default
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final JdoConf jdoConf,
                                         final ClassLoader loader)
    throws MappingException {
        loadConfiguration(jdoConf, null, loader);
    }
    
    /**
     * Load the JDOManager configuration from the specified in-memory JdoConf.
     * 
     * @param  jdoConf  the in-memory JdoConf.
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final JdoConf jdoConf)
    throws MappingException {
        loadConfiguration(jdoConf, null, null);
    }
    
    /**
     * Load the JDOManager configuration from the specified input source using 
     * a custom class loader. In addition, a custom entity resolver can be 
     * provided. 
     * 
     * @param  source   The JDOManager configuration file describing the
     *         databases, connection factory and mappings.
     * @param  resolver An (optional) entity resolver to resolve cached
     *         entities, e.g. for external mapping documents. 
     * @param  loader   The class loader to use, null for the default
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final InputSource source,
                                         final EntityResolver resolver,
                                         final ClassLoader loader)
    throws MappingException {
        DatabaseRegistry.loadDatabase(source, resolver, loader);
        
        _classLoader = loader;
        _entityResolver = resolver;
        _source = source;
        
        LOG.debug("Successfully loaded JDOManager configuration");
    }
    
    /**
     * Load the JDOManager configuration from the specified location using a 
     * custom class loader.
     *  
     * @param  url      The location from which to load the configuration file. 
     * @param  loader   The custom class loader to use, null for the default.
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the JDOManager configuration/mapping.
     */
    public static void loadConfiguration(final String url,
                                         final ClassLoader loader)
    throws MappingException {
        loadConfiguration(new InputSource(url), null, loader);
    }
    
    /**
     * Load the JDOManager configuration from the specified location.
     * 
     * @param  url      The location from which to load the configuration file. 
     * @throws MappingException The mapping file is invalid, or any error
     *         occured trying to load the JDOManager configuration/mapping.
     */
    public static void loadConfiguration(final String url)
    throws MappingException {
        loadConfiguration(new InputSource(url), null, null);
    }
    
    //--------------------------------------------------------------------------

    /**
     * The URL of the configuration file. If the URL is specified, the first
     * attempt to load a database of this type will use the specified
     * configuration file.
     */
    private InputSource _jdoConfURI;
    
    /**
     * The callback interceptor to which all persistent state events to be sent.
     */
    private CallbackInterceptor _callbackInterceptor;

    /**
     * The instance factory which create new instances of data objects.
     */
    private InstanceFactory _instanceFactory;

    /**
     * The lock timeout for this database. Zero for immediate timeout, an
     * infinite value for no timeout. The timeout is specified in seconds.
     */
    private int _lockTimeout = DEFAULT_LOCK_TIMEOUT;

    /**
     * Description of this database.
     */
    private String _description = DEFAULT_DESCRIPTION;

    /**
     * The transactions to databases map for database pooling.
     */
    private TxDatabaseMap  _txDbPool;

    /**
     * True if user prefer all reachable object to be stored automatically.
     * False (default) if user want only dependent object to be stored.
     */
    private boolean _autoStore = false;

    /**
     * The name of this database.
     */
    private String _databaseName;

    /**
     * The transaction manager factory to be used to obtain a
     * <code>javax.jta.TransactionManager</code> instance.
     */
    private TransactionManagerFactory _transactionManagerFactory = null;

    /**
     * The transaction manager
     */
    private TransactionManager _transactionManager = null;

    //--------------------------------------------------------------------------

    /**
     * Constructs a new JDOManager database factory.
     */
    private JDOManager() {
        super();
    }

    /**
     * Constructs a new JDOManager database factory for database with the given
     * name.
     *
     * @param  databaseName The database name.
     */
    private JDOManager(final String databaseName) {
        _databaseName = databaseName;
    }

    /**
     * Sets the application class loader.
     * <p/>
     * This method should be used with application servers that use multiple
     * class loaders. The default value is "null". It means that application
     * classes are loaded through <code>Class.forName(className)</code>.
     * <p/>
     * Examples:
     * <p/>
     * <code>
     *   jdo.setClassLoader(getClass().getClassLoader());
     * </code><p/><code>
     *   jdo.setClassLoader(Thread.currentThread().getContextClassLoader());
     * </code>
     * 
     * @param classLoader New ClassLoader to be used or null to use the default.
     */
    private void setClassLoader(final ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    /**
     * Returns the application classloader.
     * 
     * @return The currently used ClassLoader or null if default is used.
     */
    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    /**
     * Sets the entity resolver.
     * <p/>
     * The resolver can be used to resolve cached entities, e.g. for external
     * mapping documents. Note, that you cannot create two Database instances
     * that differ only in a resolver.
     * 
     * @param entityResolver New EntityResolver to be used.
     */
    private void setEntityResolver(final EntityResolver entityResolver) {
        _entityResolver = entityResolver;
    }

    /**
     * Returns the entity resolver.
     * 
     * @return The EntityResolver currently in use.
     */
    public EntityResolver getEntityResolver() {
        return _entityResolver;
    }

    /**
     * Sets the URL of the database configuration file. If the URL is
     * specified, the first attempt to load a database of this type will use
     * the specified configuration file. If the URL is not specified, use one
     * of the {@link #loadConfiguration(String)} methods instead.
     * <p/>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @param source The URL of the database configuration file as InputSource.
     */
    public void setConfiguration(final InputSource source) {
        _jdoConfURI = source;
    }

    /**
     * Return the URL of the database configuration file.
     * <p/>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @return The URL of the database configuration file as InputSource.
     */
    public InputSource getConfiguration() {
        return _jdoConfURI;
    }

    /**
     * Overrides the default callback interceptor by a custom interceptor
     * for this database source.
     * <p/>
     * The interceptor is a callback that notifies data objects on persistent
     * state events.
     * <p/>
     * If callback interceptor is not overrided, events will be sent only to
     * that data objects that implement the org.exolab.castor.jdo.Persistent 
     * interface.
     *
     * @param callback The callback interceptor, null if disabled
     */
    public void setCallbackInterceptor(final CallbackInterceptor callback) {
        _callbackInterceptor = callback;
    }

    /**
     * Returns the callback interceptor.
     * 
     * @return The currently used CallbackInterceptor or null if not overriden.
     */
    public CallbackInterceptor getCallbackInterceptor() {
        return _callbackInterceptor;
    }

    /**
     * Overrides the default instance factory by a custom one to be used by
     * Castor to obtaining an instance of a data object when it is needed during
     * loading.
     * <p/>
     * If instance factory is not overrided, and if class loader is not set,
     * Class.forName(className).newInstance() will be used to create a new
     * instance. If instance factory is not override, and class loader is set,
     * loader.loadClass(className).newInstance() will be used instead.
     *
     * @param factory The instance factory, null to use the default
     */
    public void setInstanceFactory(final InstanceFactory factory) {
        _instanceFactory = factory;
    }

    /**
     * Returns the instance factory.
     * 
     * @return The currently used InstanceFactoryor null if not overriden.
     */
    public InstanceFactory getInstanceFactory() {
        return _instanceFactory;
    }

    /**
     * Sets the lock timeout for this database. Use zero for immediate
     * timeout, an infinite value for no timeout. The timeout is specified in
     * seconds.
     * <p/>
     * The standard name for this property is <tt>lockTimeout</tt>.
     *
     * @param seconds The lock timeout, specified in seconds
     */
    public void setLockTimeout(final int seconds) {
        _lockTimeout = seconds;
    }

    /**
     * Returns the lock timeout for this database.
     * <p/>
     * The standard name for this property is <tt>lockTimeout</tt>.
     *
     * @return The lock timeout, specified in seconds
     */
    public int getLockTimeout() {
        return _lockTimeout;
    }

    /**
     * Sets the description of this database.
     * <p/>
     * The standard name for this property is <tt>description</tt>.
     *
     * @param description The description of this database
     */
    public void setDescription(final String description) {
        if (description == null) {
            throw new NullPointerException("Argument 'description' is null");
        }
        _description = description;
    }

    /**
     * Returns the description of this database.
     * <p/>
     * The standard name for this property is <tt>description</tt>.
     *
     * @return The description of this database
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Enable/disable database pooling. This option only affects JDOManager
     * if <tt>transactionManager</tt> is set and a transaction is associated
     * with the thread that call {@link #getDatabase}. If database pooling is
     * enabled, JDOManager will first search in the pool to see if there is
     * already a database for the current transaction. If found, it returns the
     * database; if not, it create a new one, associates it will the transaction
     * and return the newly created database.
     * <p/>
     * This method should be called before {@link #getDatabase}.
     * <p/>
     * <b>Experimental</b> maybe removed in the future releases.
     *
     * @param pool true to enable database pooling
     */
    public void setDatabasePooling(final boolean pool) {
        if (!pool) {
            if (_txDbPool == null) {
                return;
            } else if (_txDbPool.isEmpty()) {
                _txDbPool = null;
                return;
            } else {
                throw new IllegalStateException(
                        "JDO Pooling started. It can not be set to false");
            }
        } else if (_txDbPool == null) {
            _txDbPool = new TxDatabaseMap();
            
        }
        return;
    }

    /**
     * Indicates if database pooling is enable or not.
     * <p/>
     * <b>Experimental</b> maybe removed in the further release.
     * 
     * @see    #setDatabasePooling(boolean)
     * @return True if pooling is enabled for this Database instance. 
     */ 
    public boolean getDatabasePooling() {
        return _txDbPool != null;
    }

    /**
     * Sets <tt>autoStore</tt> mode.
     * 
     * @param  autoStore True if user prefer all reachable object to be stored
     *         automatically; False if user want only dependent object to be
     *         stored.  
     */
    public void setAutoStore(final boolean autoStore) {
        _autoStore = autoStore;
    }

    /**
     * Return if the next database instance will be set to <tt>autoStore</tt>.
     * 
     * @return True if <tt>autoStore</tt> is enabled.
     */
    public boolean isAutoStore() {
        return _autoStore;
    }

    /**
     * Returns the name of this database.
     * <p/>
     * The standard name for this property is <tt>databaseName</tt>.
     *
     * @return The name of this database
     */
    public String getDatabaseName() {
        return _databaseName;
    }
    
    /**
     * Opens and returns a connection to the database. If no configuration
     * exists for the named database a {@link DatabaseNotFoundException}
     * is thrown.
     *
     * @return An open connection to the database.
     * @throws PersistenceException Database access failed.
     */
    public Database getDatabase() throws PersistenceException {
        if (_databaseName == null) {
            LOG.error("getDatabase() called without specifying database name");
            throw new IllegalStateException(Messages.message(
                    "jdo.missing.database.name"));
        }
        
        try {
            if (DatabaseRegistry.getDatabaseRegistry(_databaseName) == null) {
                if (_jdoConfURI == null) {
                    LOG.error("No configuration loaded for database "
                            + _databaseName);
                    throw new DatabaseNotFoundException(Messages.format(
                            "jdo.dbNoMapping", _databaseName));
                }
                
                DatabaseRegistry.loadDatabase(_jdoConfURI,
                                              _entityResolver,
                                              _classLoader);
            }
        } catch (MappingException ex) {
            LOG.error("Problem loading database configuration", ex);
            throw new DatabaseNotFoundException(Messages.format(
                    "jdo.problem.loading.conf", _jdoConfURI), ex);
        }
        
        // load transaction manager factory registry configuration
        try {
            TransactionManagerFactoryRegistry.load(_jdoConfURI,
                                                   _entityResolver);
        } catch (TransactionManagerAcquireException ex) {
            LOG.error("Failed to initalize TransactionManagerFactory", ex);
            throw new PersistenceException(Messages.message(
                    "jdo.problem.initializingTransactionManagerFactory"), ex); 
        }

        if (_transactionManagerFactory == null) {
            String transactionMode = null;
            try {
                TransactionDemarcation demarcation;
                String mode;
                org.exolab.castor.jdo.conf.TransactionManager manager;
                
                demarcation = JDOConfLoader.getTransactionDemarcation(
                        _jdoConfURI, _entityResolver);
                mode = demarcation.getMode();
                manager = demarcation.getTransactionManager();
                                
                if (manager != null) {
                    transactionMode = manager.getName();
                } else if (mode.equals(LocalTransactionManagerFactory.NAME)) {
                    transactionMode = LocalTransactionManagerFactory.NAME;
                } else {
                    LOG.error("Missing configuration for TransactionManager");
                    throw new PersistenceException(Messages.message(
                            "jdo.problem.missingTransactionConfiguration"));
                }
            } catch (MappingException ex) {
                LOG.error("No valid mode for TransactionManager", ex);
                throw new PersistenceException(Messages.message(
                        "jdo.problem.noValidTransactionMode"), ex);
            }
                 
            /*
             * Try to obtain the specified<code>TransactionManagerFactory</code>
             * instance from the registry.
             * 
             * If the returned TransactionManagerFactory instance is null,
             * return an exception to indicate that we cannot live without a
             * valid TransactionManagerFactory instance and that the user
             * should change the configuration.
             */
             _transactionManagerFactory = TransactionManagerFactoryRegistry
                     .getTransactionManagerFactory(transactionMode);
        
            if (_transactionManagerFactory == null) {
                LOG.error("Missing TransactionManagerFactory");
                throw new DatabaseNotFoundException(Messages.format(
                        "jdo.transaction.missingTransactionManagerFactory",
                        transactionMode));
            }

            /*
             * Try to obtain a <code>javax.jta.TransactionManager>/code> from
             * the factory.
             */
            try {
                _transactionManager = _transactionManagerFactory
                        .getTransactionManager();
                
            } catch (TransactionManagerAcquireException ex) {
                LOG.error("Unable to acquire TransactionManager", ex);
                throw new DatabaseNotFoundException(Messages.format(
                        "jdo.transaction.unableToAcquireTransactionManager", 
                        _transactionManagerFactory.getName()), ex);
            }
        }
        
        /* At this point, we MUST have a valid TransactionManagerFactory, and 
         * dependent on its type (LOCAL or not), we MIGHT have a valid
         * <code>javax.jta.TransactionManager</code> instance.
         * 
         * Scenario 1: If the user uses Castor in standalone mode (outside of an
         * J2EE container), we have a TransactionManagerFactory instance only,
         * but no TransactionManager instance.
         * 
         * Scenario 2: If the user uses Castor in J2EE mode (inside of an J2EE
         * container), and wants the container to control transaction
         * demarcation, we have both a TransactionManagerFactory and a
         * TransactionManager instance.
         */
        if ((_transactionManager != null)
                && !LocalTransactionManagerFactory.NAME.equals(
                        _transactionManagerFactory.getName())) {
            
            Transaction        tx;
            DatabaseImpl       dbImpl;

            try {
                tx = _transactionManager.getTransaction();
                if ((_txDbPool != null) && (_txDbPool.containsTx(tx))) {
                    return _txDbPool.get(tx);
                }

                if ((tx != null) && (tx.getStatus() == Status.STATUS_ACTIVE)) {
                    dbImpl = new DatabaseImpl(_databaseName, _lockTimeout, 
                                              _callbackInterceptor, 
                                              _instanceFactory, tx,
                                              _classLoader, _autoStore);

                    if (_txDbPool != null) {
                        _txDbPool.put(tx, dbImpl);
                    }

                    tx.registerSynchronization(dbImpl);
                    return dbImpl;
                }
             } catch (Exception ex) {
                 // NamingException, SystemException, RollbackException
                 LOG.error("Failed to create LocalTransactionManager", ex);
            }
        }
        
        return new DatabaseImpl(_databaseName, _lockTimeout, 
                                _callbackInterceptor, _instanceFactory, null,
                                _classLoader, _autoStore);
    }
    
    /**
     * Constructs a new reference to JDOManager being its own factory.
     * 
     * @return A new Reference to JDOManager.
     * @see    javax.naming.Reference
     * @see    javax.naming.spi.ObjectFactory
     */
    public synchronized Reference getReference() {
        Reference ref;
        
        // We use same object as factory.
        ref = new Reference(getClass().getName(), getClass().getName(), null);
        
        if (_description != null) {
            ref.add(new StringRefAddr("description", _description));
        }
        if (_databaseName != null) {
            ref.add(new StringRefAddr("databaseName", _databaseName));
        }
        if (_jdoConfURI != null) {
            ref.add(new StringRefAddr("configuration", _jdoConfURI.toString()));
        }
        ref.add(new StringRefAddr("lockTimeout",
                                  Integer.toString(_lockTimeout)));
        
        return ref;
    }
    
    /**
     * @see    javax.naming.spi.ObjectFactory
     */
    public Object getObjectInstance(final Object refObj, final Name name,
                                    final Context nameCtx, final Hashtable env)
    throws NamingException {
        Reference ref;
        
        // Can only reconstruct from a reference.
        if (refObj instanceof Reference) {
            ref = (Reference) refObj;
            
            // Make sure reference is of datasource class.
            if (!ref.getClassName().equals(getClass().getName())) {
                throw new NamingException(Messages.format(
                        "jdo.reference.wrong.type", ref.getClassName()));
            }

            JDOManager jdo;
            RefAddr addr;
            
            try {
                jdo = (JDOManager) Class.forName(
                        ref.getClassName()).newInstance();
            } catch (Exception ex) {
                NamingException ne = new NamingException(Messages.format(
                        "jdo.problem.loading.class", ref.getClassName()));
                ne.setRootCause(ex);
                throw ne;
            }
            
            addr = ref.get("description");
            if (addr != null) {
                jdo._description = (String) addr.getContent();
            }
            addr = ref.get ("databaseName");
            if (addr != null) {
                jdo._databaseName = (String) addr.getContent();
            }
            addr = ref.get ("configuration");
            if (addr != null) {
                jdo._jdoConfURI = (InputSource) addr.getContent();
            }
            addr = ref.get ("lockTimeout");
            if (addr != null) {
                jdo._lockTimeout = Integer.parseInt((String) addr.getContent());
            }
            return jdo;
        } else if (refObj instanceof Remote) {
            return refObj;
        } else {
            return null;
        }
    }

    //--------------------------------------------------------------------------
}



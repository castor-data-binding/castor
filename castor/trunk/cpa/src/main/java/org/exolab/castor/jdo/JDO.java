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

import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.Remote;
import java.util.Hashtable;

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

import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.engine.AbstractConnectionFactory;
import org.castor.jdo.engine.DatabaseRegistry;
import org.castor.transactionmanager.LocalTransactionManager;
import org.castor.util.Messages;
import org.exolab.castor.jdo.engine.AbstractDatabaseImpl;
import org.exolab.castor.jdo.engine.GlobalDatabaseImpl;
import org.exolab.castor.jdo.engine.LocalDatabaseImpl;
import org.exolab.castor.jdo.engine.TxDatabaseMap;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.OutputLogInterceptor;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Implementation of the JDO engine used for obtaining database connection. A
 * JDO object is constructed with the name of a database and other properties,
 * and {@link #getDatabase} is used to obtain a new database connection. Any
 * number of database connections can be obtained from the same JDO object.
 * <p/>
 * The database configuration can be loaded using one of the {@link
 * #loadConfiguration(String)} methods. Alternatively, {@link
 * #setConfiguration(String)} can be used to specify the URL of a database
 * configuration file. The configuration will be loaded only once.
 * <p>
 * For example:
 * <pre>
 * <font color="red">// load the database configuration</font>
 * JDO.loadConfiguration( "database.xml" );
 *
 * ...
 *
 * JDO      jdo;
 * Database db;
 *
 * <font color="red">// construct a new JDO for the database 'mydb'</font>
 * jdo = new JDO( "mydb" );
 * <font color="red">// open a connection to the database</font>
 * db = jdo.getDatabase();
 * </pre>
 * Or,
 * <pre>
 * JDO      jdo;
 * Database db;
 *
 * <font color="red">// construct a new JDO for the database 'mydb'</font>
 * jdo = new JDO( "mydb" );
 * <font color="red">// specify the database configuration</font>
 * jdo.setConfiguration( "database.xml" );
 * <font color="red">// open a connection to the database</font>
 * db = jdo.getDatabase();
 * </pre>
 *
 * @deprecated JDO has been replaced by JDOManager. 
 * 
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-12 15:13:08 -0600 (Wed, 12 Apr 2006) $
 */
public final class JDO
implements DataObjects, Referenceable, ObjectFactory, Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 2816194621638396008L;

    /**
     * The default lock timeout for this database is 10 seconds.
     */
    public static final int DEFAULT_LOCK_TIMEOUT = 10;

    /**
     * The URL of the database configuration file. If the URL is
     * specified, the first attempt to load a database of this type
     * will use the specified configuration file.
     */
    private String _jdoConfURI;
    
    /**
     * An in-memory JDO configuration instance.
     */
    private JdoConf _jdoConf;

    /**
     * The log intercpetor to which all logging and tracing messages
     * will be sent.
     * 
     * @deprecated There is no need for this member due to the implementation
     * of Log4J which is controlled via the log4j.properties file.
     */
    private LogInterceptor  _logInterceptor;

    /**
     * The callback interceptor to which all persistent state events
     * to be sent.
     */
    private CallbackInterceptor _callback;

    /**
     * The instance factory to which create a new instance of data object
     */
    private InstanceFactory _instanceFactory;

    /**
     * The lock timeout for this database. Zero for immediate
     * timeout, an infinite value for no timeout. The timeout is
     * specified in seconds.
     */
    private int _lockTimeout = DEFAULT_LOCK_TIMEOUT;

    /**
     * The name of this database.
     */
    private String _dbName;

    /**
     * Description of this database.
     */
    private String _description = "Castor JDO";

    /**
     * The transaction manager
     */
    private TransactionManager _transactionManager = null;

    /**
     * The application class loader.
     */
    private ClassLoader _classLoader;

    /**
     * The resolver can be used to resolve cached entities, e.g.
     * for external mapping documents.
     */
    private EntityResolver _entityResolver;

    /**
     * The transactions to databases map for database pooling
     */
    private TxDatabaseMap  _txDbPool;

    /**
     * True if user prefer all reachable object to be stored automatically.
     * False (default) if user want only dependent object to be stored.
     */
    private boolean _autoStore = false;

    /**
     * Constructs a new JDO database factory. Must call {@link
     * #setDatabaseName} before calling {@link #getDatabase}.
     */
    public JDO() {
        // default constructor, no code
    }

    /**
     * Constructs a new JDO database factory for databases with
     * the given name.
     *
     * @param name The database name
     */
    public JDO(final String name) {
        _dbName = name;
    }

    /**
     * Returns the log writer for this database source.
     * <p>
     * The log writer is a character output stream to which all
     * logging and tracing messages will be printed.
     *
     * @param logWriter A PrintWriter instance.
     * 
     * @deprecated There is no need for this method due to the implementation
     *             of Log4J which is controlled via the log4j.properties file.
     */
    public void setLogWriter(final PrintWriter logWriter) {
        if (logWriter == null) {
            _logInterceptor = null;
        } else {
            _logInterceptor = new OutputLogInterceptor(logWriter);
        }
    }

    /**
     * Sets the log interceptor for this database source.
     * <p>
     * The interceptor is a callback to to which all
     * logging and tracing messages are sent.
     *
     * @param logInterceptor The log interceptor, null if disabled
     * 
     * @deprecated There is no need for this method due to the implementation
     *             of Log4J which is controlled via the log4j.properties file.
     */
    public void setLogInterceptor(final LogInterceptor logInterceptor) {
        _logInterceptor = logInterceptor;
    }

    /**
     * Overrides the default callback interceptor by a custom 
     * interceptor for this database source.
     * <p>
     * The interceptor is a callback that notifies data objects 
     * on persistent state events.
     * <p>
     * If callback interceptor is not overrided, events will be
     * sent to data object that implements the org.exolab.castor.jdo.Persistent 
     * interface.
     *
     * @param callback The callback interceptor, null if disabled
     */
    public void setCallbackInterceptor(final CallbackInterceptor callback) {
        _callback = callback;
    }

    /**
     * Overrides the default instance factory by a custom one
     * to be used by Castor to obtaining an instance of data 
     * object when it is needed during loading.
     * <p>
     * If instance factory is not overrided, and if class loader
     * is not set, Class.forName( className ).newInstance() will
     * be used; if instance factory is not override, and class
     * loader is set, loader.loadClass( className ).newInstance()
     * will be used to create a new instance.
     *
     * @param factory The instance factory, null to use the default
     */
    public void setInstanceFactory(final InstanceFactory factory) {
        _instanceFactory = factory;
    }

    /**
     * Returns the log interceptor for this database source.
     *
     * @return The log interceptor, null if disabled
     * @deprecated There is no need for this method due to the implementation
     * of Log4J which is controlled via the log4j.properties file.
     */
    public LogInterceptor getLogInterceptor() {
        return _logInterceptor;
    }

    /**
     * Sets the application class loader. This method should be used with
     * application servers that use multiple class loaders. The default value
     * is "null". It means that application classes are loaded through
     * <code>Class.forName(className)</code>.
     * <p>
     * Examples:
     * <p>
     * <code>
     *   jdo.setClassLoader(getClass().getClassLoader());
     * </code><p><code>
     *   jdo.setClassLoader(Thread.currentThread().getContextClassLoader());
     * </code>
     * 
     * @param classLoader New ClassLoader to be used or null to use the default.
     */
    public void setClassLoader(final ClassLoader classLoader) {
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
     * Sets the entity resolver. The resolver can be used to resolve cached
     * entities, e.g. for external mapping documents. Note, that you cannot
     * create two Database instances that differ only in a resolver.
     * 
     * @param entityResolver New EntityResolver to be used.
     */
    public void setEntityResolver(final EntityResolver entityResolver) {
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
     * Sets the description of this database.
     * <p>
     * The standard name for this property is <tt>description</tt>.
     *
     * @param description The description of this database
     */
    public void setDescription(final String description) {
        if (description == null) {
            throw new NullPointerException(
                    "DataSource: Argument 'description' is null");
        }
        _description = description;
    }

    /**
     * Returns the description of this database.
     * <p>
     * The standard name for this property is <tt>description</tt>.
     *
     * @return The description of this database
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Sets the name of this database. This attribute is required
     * in order to identify which database to open.
     * <p>
     * The standard name for this property is <tt>databaseName</tt>.
     *
     * @param name The name of this database
     */
    public void setDatabaseName(final String name) {
        _dbName = name;
    }

    /**
     * Returns the name of this database.
     * <p>
     * The standard name for this property is <tt>databaseName</tt>.
     *
     * @return The name of this database
     */
    public String getDatabaseName() {
        return _dbName;
    }

    /**
     * Sets the lock timeout for this database. Use zero for immediate
     * timeout, an infinite value for no timeout. The timeout is
     * specified in seconds.
     * <p>
     * The standard name for this property is <tt>lockTimeout</tt>.
     *
     * @param seconds The lock timeout, specified in seconds
     */
    public void setLockTimeout(final int seconds) {
        _lockTimeout = seconds;
    }

    /**
     * Returns the lock timeout for this database.
     * <p>
     * The standard name for this property is <tt>lockTimeout</tt>.
     *
     * @return The lock timeout, specified in seconds
     */
    public int getLockTimeout() {
        return _lockTimeout;
    }

    /**
     * Sets the URL of the database configuration file. If the URL is
     * specified, the first attempt to load a database of this type
     * will use the specified configuration file. If the URL is not
     * specified, use one of the {@link #loadConfiguration(String)} methods
     * instead.
     * <p>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @param url The URL of the database configuration file
     */
    public void setConfiguration(final String url) {
        _jdoConfURI = url;
    }

    /**
     * Provides JDO with a JDO configuration file. 
     * @param jdoConfiguration A JDO configuration instance.
     */
    public void setConfiguration(final JdoConf jdoConfiguration) {
        _jdoConf = jdoConfiguration;
    }

    /**
     * Return the URL of the database configuration file.
     * <p>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @return The URL of the database configuration file
     */
    public String getConfiguration() {
        return _jdoConfURI;
    }

    /**
     * Enable/disable jdo Database pooling. This option only affects
     * JDO if _transactionManager is set and a transaction is associated
     * with the thread that call {@link #getDatabase}. If jdo Database pooling
     * is enabled, JDO will first search in the pool to see if there
     * is already a Database for the current transaction. If found, it
     * returns the database; if not, it create a new one, associates
     * it will the transaction and return the newly created Database.
     * <p>
     * Method should be called before the invocation of {@link #getDatabase}.
     * <p>
     * <b>Experimental</b> maybe removed in the future releases
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
     * Indicates if jdo database pooling is enable or not.
     * <p>
     * <b>Experimental</b> maybe removed in the further release
     * 
     * @see #setDatabasePooling(boolean)
     * @return <code>true</code> if database pooling is enable.
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
     * Opens and returns a connection to the database. Throws an
     * {@link DatabaseNotFoundException} if the database named was not
     * set in the constructor or with a call to {@link #setDatabaseName},
     * or if no database configuration exists for the named database.
     *
     * @return An open connection to the database
     * @throws PersistenceException Database access failed
     */
    public Database getDatabase() throws PersistenceException {
        if (_dbName == null) {
            throw new IllegalStateException(
                    "Called 'getDatabase' without first setting database name");
        }

        try {
            if (!DatabaseRegistry.isDatabaseRegistred(_dbName)) {
                // use _jdoConfURI to load the JDO configuration
                if (_jdoConfURI != null) {
                    DatabaseRegistry.loadDatabase(new InputSource(_jdoConfURI),
                            _entityResolver,
                            _classLoader);
                // alternatively, use a JdoConf instance to load the JDO config
                } else if (_jdoConf != null) {
                    DatabaseRegistry.loadDatabase(_jdoConf, _entityResolver,
                                                  _classLoader, null);
                } else {
                    throw new DatabaseNotFoundException(Messages.format(
                            "jdo.dbNoMapping", _dbName));
                }
            }

            AbstractConnectionFactory factory;
            factory = DatabaseRegistry.getConnectionFactory(_dbName);
            _transactionManager = factory.getTransactionManager();
        } catch (MappingException ex) {
            throw new DatabaseNotFoundException(ex);
        }

        /* At this point, we MUST have a instance of TransactionManagerFactory,
         * and dependent on its type (LOCAL or not), we MIGHT have a valid
         * <code>javax.jta.TransactionManager</code> instance.
         * 
         * Scenario 1: If the user uses Castor in standalone mode (outside of an
         * J2EE container), we have a TransactionManagerFactory instance only,
         * but no TransactionManager instance.
         * 
         * Scenario 2: If the user uses Castor in J2EE mode (inside of an J2EE
         * container), and wants the container to control transaction
         * demarcation, we have both a TransactionManagerFactory (different
         * from LOCAL) and a TransactionManager instance.
         */
        if (!(_transactionManager instanceof LocalTransactionManager)) {
            Transaction        transaction;
            AbstractDatabaseImpl       dbImpl;

            try {
                transaction = _transactionManager.getTransaction();
                if ((_txDbPool != null) && _txDbPool.containsTx(transaction)) {
                    return _txDbPool.get (transaction);
                }
                
                if ((transaction != null)
                        && (transaction.getStatus() == Status.STATUS_ACTIVE)) {
                    
                    dbImpl = new GlobalDatabaseImpl(_dbName, _lockTimeout, _callback,
                            _instanceFactory, transaction, _classLoader,
                            _autoStore, getDatabasePooling());
                                        
                    if (_txDbPool != null) {
                        _txDbPool.put(transaction, (GlobalDatabaseImpl) dbImpl);
                    }
                    
                    transaction.registerSynchronization((GlobalDatabaseImpl) dbImpl);
                    return dbImpl;
                }
            } catch (Exception ex) {
                // NamingException, SystemException, RollbackException
                if (_logInterceptor != null) {
                    _logInterceptor.exception(ex);
                }
            }
        }
        
        return new LocalDatabaseImpl(_dbName, _lockTimeout, _callback,
                _instanceFactory, _classLoader, _autoStore);
    }

    /**
     * Load database configuration from the specified URL. <tt>url</tt>
     * must point to a JDO configuration file describing the database
     * name, connection factory and mappings.
     * 
     * @param url The JDO configuration file
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final String url)
    throws MappingException {
        DatabaseRegistry.loadDatabase(new InputSource(url), null, null);
    }

    /**
     * Load database configuration from the specified URL. <tt>url</tt>
     * must point to a JDO configuration file describing the database
     * name, connection factory and mappings. <tt>loader</tt> is
     * optional, if null the default class loader is used.
     * 
     * @param url The JDO configuration file
     * @param loader The class loader to use, null for the default
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final String url,
                                         final ClassLoader loader)
    throws MappingException {
        DatabaseRegistry.loadDatabase(new InputSource(url), null, loader);
    }
    
    /**
     * Load database configuration from the specified input source.
     * <tt>source</tt> must point to a JDO configuration file describing
     * the database* name, connection factory and mappings.
     * <tt>resolver</tt> can be used to resolve cached entities, e.g.
     * for external mapping documents. <tt>loader</tt> is optional, if
     * null the default class loader is used.
     * 
     * @param source The JDO configuration file
     * @param resolver An optional entity resolver
     * @param loader The class loader to use, null for the default
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration(final InputSource source,
                                         final EntityResolver resolver, 
                                         final ClassLoader loader)
    throws MappingException {
        DatabaseRegistry.loadDatabase(source, resolver, loader);
    }

    /**
     * Creates a JNDI reference from the current JDO instance, to be bound to
     * a JNDI tree.
     * 
     * @return valid A JNDI Reference.
     * @throws NamingException If the Reference cannot be created.
     *  
     * @see javax.naming.Referenceable#getReference()
     */
    public synchronized Reference getReference() throws NamingException {
        Reference ref;
        
        if (_jdoConfURI == null) {
            throw new NamingException(
                "JDO instance does not have a valid configuration URI set.");
        }
        
        // We use same object as factory.
        ref = new Reference(getClass().getName(), getClass().getName(), null);
        
        if (_description != null) {
            ref.add(new StringRefAddr("description", _description));
        }
        if (_dbName != null) {
            ref.add(new StringRefAddr("databaseName", _dbName));
        }
        if (_jdoConfURI != null) {
            ref.add(new StringRefAddr("configuration", _jdoConfURI));
        }
        ref.add(new StringRefAddr("lockTimeout",
                Integer.toString(_lockTimeout)));
        
        return ref;
    }

    /**
     * {@inheritDoc}
     * Creates an instance of JDO from a JNDI reference.
     *  
     * @see javax.naming.spi.ObjectFactory
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
                throw new NamingException(
                        "Reference not constructed from class "
                        + getClass().getName());
            }
            
            JDO     jdo;
            RefAddr addr;
            
            try {
                jdo = (JDO) Class.forName(ref.getClassName()).newInstance();
            } catch (Exception ex) {
                throw new NamingException(ex.toString());
            }
            addr = ref.get("description");
            if (addr != null) {
                jdo._description = (String) addr.getContent();
            }
            addr = ref.get("databaseName");
            if (addr != null) {
                jdo._dbName = (String) addr.getContent();
            }
            addr = ref.get("configuration");
            if (addr != null) {
                jdo._jdoConfURI = (String) addr.getContent();
            }
            addr = ref.get("lockTimeout");
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
}

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
import org.exolab.castor.persist.OutputLogInterceptor;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.util.Messages;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


/**
 * Implementation of the JDO engine used for obtaining database
 * connection. After successful instantiation, {@link #getDatabase()} is 
 * used to obtain a new database connection. Any number of database 
 * connections can be obtained from the same JDO object.
 * 
 * <p>Please note that this clas is provided on an <b>experimental</b>
 *    base, and might still undergo minor changes.</p> 
 * 
 * <p>An instance of this class is contructed with a two-step 
 * approach:</p> 
 * 
 * <ul>
 *    <li>load the JDO configuration file through one of the static loadConfiguration() 
 *        methods</li>
 *    <li>create an instance of the JDO engine using the factoyr method createInstance(String)
 *        where you supply one of the database names defined in the configuration file 
 *        loaded in step 1).</li>
 * </ul>
 * 
 * <p>
 * Example:
 * <pre>
 *
 * . . .
 *
 * JDO jdo;
 * Database db;
 * 
 * try {
 * 
 *    // load the JDO configuration file 
 *    <font color="red">JDO.loadConfiguration ("jdo-config.xml");</font>
 * 
 *    // Construct a new JDO for the database 'mydb'
 *    <font color="red">jdo = JDO.createInstance ("mydb");</font>
 * 
 *    // Open a connection to the database
 *    <font color="red">db = jdo.getDatabase();</font>
 * 
 * }
 * catch (MappingException e) {
 *    ...
 * }
 * catch (DatabaseNotFoundException) {
 *    ...
 * }
 * 
 * </pre>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class JDO2
    implements DataObjects, Referenceable,
           ObjectFactory, Serializable
{

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance (JDO2.class);

    /**
     * Map of JDO objects.
     */
    private static Map _jdoInstances = new HashMap();

    /**
     * Tthe URL of the database configuration file. If the URL is
     * specified, the first attempt to load a database of this type
     * will use the specified configuration file.
     */
    private InputSource _jdoConfURI;
    
    /**
     * The log intercpetor to which all logging and tracing messages
     * will be sent.
     * @deprecated There is no need for this member due to the implementation
     * of Log4J which is controlled via the log4j.properties file.
     * @see #_log
     */
    private LogInterceptor  _logInterceptor;

    /**
     * The callback interceptor to which all persistent state events
     * to be sent.
     */
    private CallbackInterceptor _callbackInterceptor;

    /**
     * The instance factory to which create a new instance of data object
     */
    private InstanceFactory _instanceFactory;

    /**
     * The default lock timeout (specified in seconds).
     */
    public static final int DEFAULT_LOCK_TIMEOUT = 10;
    
    /**
     * The lock timeout for this database. Zero for immediate
     * timeout, an infinite value for no timeout. The timeout is
     * specified in seconds.
     */
    private int _lockTimeout = DEFAULT_LOCK_TIMEOUT;


    /**
     * The name of this database.
     */
    private String _databaseName;


    /**
     * Default description.
     */
    public static final String DEFAULT_DESCRIPTION= "Castor JDO";
    
    /**
     * Description of this database.
     */
    private String _description = DEFAULT_DESCRIPTION;

    /**
     * The transaction manager factory to be used to obtain a
     * <code>javax.jta.TransactionManager</code> instance.
     */
    private TransactionManagerFactory _transactionManagerFactory = null;

    /**
     * The transaction manager
     */
    private TransactionManager _transactionManager = null;


    /**
     * The application class loader.
     */
    private static ClassLoader _classLoader;


    /**
     * The resolver can be used to resolve cached entities, e.g.
     * for external mapping documents.
     */
    private static EntityResolver _entityResolver;

    /**
     * Location of the JDOOld configuration file. 
     */
    private static InputSource _source;
    
    /**
     * The transactions to databases map for database pooling
     */
    private TxDatabaseMap  _txDbPool;

    /*
     * True if user prefer all reachable object to be stored automatically.
     * False (default) if user want only dependent object to be stored.
     */
    private boolean _autoStore = false;

    /**
     * Constructs a new JDO database factory.
     */
    private JDO2()
    {
    	super();
    }

    /**
     * Constructs a new JDO database factory for database with
     * the given name.
     *
     * @param name The database name
     */
    private JDO2 (String name)
    {
        _databaseName = name;
    }

    /**
     * Factory method for creating a JDOOld instance for one of the databases configured in the 
     * JDOOld config file. Please make sure that you call loadConfiguration() first.  
     * @param databaseName Database name as configured in the JDOOld config file.
     * @return A JDOOld instance.
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static JDO2 createInstance (String databaseName)
    throws MappingException 
    {
        if (!DatabaseRegistry.hasDatabaseRegistries()) {
            throw new MappingException (Messages.message ("jdo.missing.jdo.configuration"));
        }
        
        if (DatabaseRegistry.getDatabaseRegistry (databaseName) == null) {
            throw new MappingException (Messages.format ("jdo.missing.database.configuration", databaseName));
        }
            
        JDO2 jdoInstance = (JDO2) _jdoInstances.get (databaseName);
        
        if (jdoInstance == null) {
        	jdoInstance = new JDO2 (databaseName);
        	
        	jdoInstance.setConfiguration (_source);
        	jdoInstance.setEntityResolver(_entityResolver);
        	jdoInstance.setClassLoader(_classLoader);
        	
        	_jdoInstances.put (databaseName, jdoInstance);
        	
        	_log.debug ("Successfully created JDO instance: " + jdoInstance);
        }
        
        return jdoInstance;
    }

    /**
     * Returns the log writer for this database source.
     * <p>
     * The log writer is a character output stream to which all
     * logging and tracing messages will be printed.
     *
     * @param logWriter A PrintWriter instance.
     * @deprecated There is no need for this method due to the implementation
     * of Log4J which is controlled via the log4j.properties file.
     */
    public void setLogWriter( PrintWriter logWriter )
    {
        if ( logWriter == null )
            _logInterceptor = null;
        else
            _logInterceptor = new OutputLogInterceptor( logWriter );
    }

    /**
     * Sets the log interceptor for this database source.
     * <p>
     * The interceptor is a callback to to which all
     * logging and tracing messages are sent.
     *
     * @param logInterceptor The log interceptor, null if disabled
     * @deprecated There is no need for this method due to the implementation
     * of Log4J which is controlled via the log4j.properties file.
     */
    public void setLogInterceptor( LogInterceptor logInterceptor )
    {
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
    public void setCallbackInterceptor( CallbackInterceptor callback )
    {
        _callbackInterceptor = callback;
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
    public void setInstanceFactory( InstanceFactory factory )
    {
        _instanceFactory = factory;
    }

    /**
     * Returns the log interceptor for this database source.
     *
     * @return The log interceptor, null if disabled
     * @deprecated There is no need for this method due to the implementation
     * of Log4J which is controlled via the log4j.properties file.
     */
    public LogInterceptor getLogInterceptor()
    {
        return _logInterceptor;
    }

    /**
     * Sets the application class loader.
     * This method should be used with application servers that use multiple
     * class loaders. The default value is "null". It means that
     * application classes are loaded through <code>Class.forName(className)</code>.
     * Examples:
     * <p><code>jdo.setClassLoader(getClass().getClassLoader());</code>
     * <p><code>jdo.setClassLoader(Thread.currentThread().getContextClassLoader());</code>
     * @param classLoader A new ClassLoader to be used.
     */
    private void setClassLoader( ClassLoader classLoader)
    {
        _classLoader = classLoader;
    }

    /**
     * Returns the application classloader.
     * @return The currently used ClassLoader.
     */
    public ClassLoader getClassLoader()
    {
        return _classLoader;
    }

    /**
     * Sets the entity resolver.
     * The resolver can be used to resolve cached entities, e.g.
     * for external mapping documents.
     * Note, that you cannot create two Database instances that differ
     * only in a resolver.
     * @param entityResolver A new EntityResolver to be used.
     */
    private void setEntityResolver( EntityResolver entityResolver)
    {
        _entityResolver = entityResolver;
    }

    /**
     * Returns the entity resolver.
     * @return The EntityResolver currently in use.
     */
    public EntityResolver getEntityResolver()
    {
        return _entityResolver;
    }

    /**
     * Sets the description of this database.
     * <p>
     * The standard name for this property is <tt>description</tt>.
     *
     * @param description The description of this database
     */
    public void setDescription( String description )
    {
    if ( description == null )
        throw new NullPointerException( "DataSource: Argument 'description' is null" );
    _description = description;
    }

    /**
     * Returns the description of this database.
     * <p>
     * The standard name for this property is <tt>description</tt>.
     *
     * @return The description of this database
     */
    public String getDescription()
    {
    return _description;
    }

    /**
     * Returns the name of this database.
     * <p>
     * The standard name for this property is <tt>databaseName</tt>.
     *
     * @return The name of this database
     */
    public String getDatabaseName()
    {
        return _databaseName;
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
    public void setLockTimeout( int seconds )
    {
        _lockTimeout = seconds;
    }

    /**
     * Returns the lock timeout for this database.
     * <p>
     * The standard name for this property is <tt>lockTimeout</tt>.
     *
     * @return The lock timeout, specified in seconds
     */
    public int getLockTimeout()
    {
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
     * @param source The URL of the database configuration file as an InputSource.
     */
    public void setConfiguration (InputSource source)
    {
        _jdoConfURI = source;
    }

    /**
     * Return the URL of the database configuration file.
     * <p>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @return The URL of the database configuration file as an InoutSource.
     */
    public InputSource getConfiguration()
    {
        return _jdoConfURI;
    }

    /**
     * Enable/disable jdo Database pooling. This option only affects
     * JDO if transactionManager is set and a transaction is associated
     * with the thread that call {@link #getDatabase}. If jdo Database pooling
     * is enabled, JDO will first search in the pool to see if there
     * is already a Database for the current transaction. If found, it
     * returns the database; if not, it create a new one, associates
     * it will the transaction and return the newly created Database.
     * <p>
     * This method should be called before the invocation of {@link #getDatabase}.
     * <p>
     * <b>Experimental</b> maybe removed in the future releases
     *
     * @param pool true to enable database pooling
     */
    public void setDatabasePooling( boolean pool ) {
        if ( !pool ) {
            if ( _txDbPool == null )
                return;
            else if ( _txDbPool.isEmpty() ) {
                _txDbPool = null;
                return;
            } else
                throw new IllegalStateException("JDO Pooling started. It can not be set to false");
        }
        
        if ( _txDbPool == null ) 
        	_txDbPool = new TxDatabaseMap();
        return;
    }

    /**
     * Indicates if jdo Database pooling is enable or not.
     * <p>
     * <b>Experimental</b> maybe removed in the further release
     * @see #setDatabasePooling(boolean)
     * @return True if pooling is enabled for this Database instance. 
     */ 
    public boolean getDatabasePooling() {
        return _txDbPool != null;
    }

    /**
     * Sets autoStore mode.
     * @param autoStore True if user prefer all reachable object to be stored automatically;
     * False if user want only dependent object to be stored.  
     * See also, {@link JDO2#setAutoStore(boolean)}
     */
    public void setAutoStore( boolean autoStore ) {
        _autoStore = autoStore;
    }

    /**
     * Return if the next Database instance will be set to autoStore.
     * @return True if autoStore is enabled.
     */
    public boolean isAutoStore() {
        return _autoStore;
    }

    /**
     * Opens and returns a connection to the database. Throws an
     * {@link DatabaseNotFoundException} if no database configuration exists for 
     * the named database.
     *
     * @return An open connection to the database
     * @throws DatabaseNotFoundException Attempted to open a database
     *  that does not exist
     * @throws PersistenceException Database access failed
     */
    public Database getDatabase()
        throws DatabaseNotFoundException, PersistenceException
    {
        if ( _databaseName == null )
            throw new IllegalStateException( Messages.message ("jdo.missing.database.name"));
        if ( DatabaseRegistry.getDatabaseRegistry( _databaseName ) == null ) {
            if ( _jdoConfURI == null )
                throw new DatabaseNotFoundException( Messages.format( "jdo.dbNoMapping", _databaseName ) );
            try {
                DatabaseRegistry.loadDatabase( _jdoConfURI, _entityResolver, _classLoader );
            } catch ( MappingException except ) {
                throw new DatabaseNotFoundException( Messages.format ("jdo.problem.loading.conf", _jdoConfURI), except );
            }
        }
        
        // load transaction manager factory registry configuration
        try {
            TransactionManagerFactoryRegistry.load (_jdoConfURI, _entityResolver);
        }
        catch (TransactionManagerAcquireException e) {
            throw new PersistenceException (Messages.message ("jdo.transaction.problemToInitializeTransactionManagerFactory"), e); 
        }

        if (_transactionManagerFactory == null) {
            
            String transactionMode = null;
            try {
                TransactionDemarcation demarcation =
                    JDOConfLoader.getTransactionDemarcation(_jdoConfURI, _entityResolver);
                     
                String demarcationMode =demarcation.getMode();

                org.exolab.castor.jdo.conf.TransactionManager transactionManager = demarcation.getTransactionManager();
                                
                if (transactionManager != null) 
                    transactionMode = transactionManager.getName();
                else {
                    
                    if (demarcationMode.equals(LocalTransactionManagerFactory.NAME))
                        transactionMode= LocalTransactionManagerFactory.NAME;
                    else
                        throw new PersistenceException (Messages.message ("jdo.transaction.missingTransactionManagerConfiguration"));
                }
                    
            }
            catch (MappingException e) {
                throw new PersistenceException ("jdo.transaction.noValidTransactionMode", e);
            }
                 
            /*
             * Try to obtain the specified<code>TransactionManagerFactory</code> instance from the
             * registry.
             * 
             * If the returned TransactionManagerFactory instance is null,
             * return an exception to indicate that we cannot live without a
             * valid TransactionManagerFactory instance and that the user should
             * change her configuration.
             */
             _transactionManagerFactory = 
                TransactionManagerFactoryRegistry.getTransactionManagerFactory (transactionMode);
        
            if (_transactionManagerFactory == null) {
                throw new DatabaseNotFoundException( Messages.format( "jdo.transaction.missingTransactionManagerFactory", transactionMode));
            }

            /*
             *  Try to obtain a <code>javax.jta.TransactionManager>/code> from the factory.
             */
            try {
                _transactionManager = _transactionManagerFactory.getTransactionManager();
            } catch (TransactionManagerAcquireException e) {
                throw new DatabaseNotFoundException( Messages.format( "jdo.transaction.unableToAcquireTransactionManager", 
                    _transactionManagerFactory.getName(),
                    e ) );
            }
        }
        
        /* At this point, we MUST have a valid instance of TransactionManagerFactory, and 
         * dependent on its type (LOCAL or not), we MIGHT have a
         * valid<code>javax. jta.TransactionManager</code> instance.
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
         
        if ( (_transactionManagerFactory.getName().equals (LocalTransactionManagerFactory.NAME ) ) && 
             ( _transactionManager != null) ) {
            Transaction        tx;
            DatabaseImpl       dbImpl;

            try {
                tx = _transactionManager.getTransaction();
                if ( _txDbPool != null && _txDbPool.containsTx( tx ) )
                    return _txDbPool.get( tx );

                if ( tx != null && tx.getStatus() == Status.STATUS_ACTIVE ) {
                    dbImpl = new DatabaseImpl( _databaseName, _lockTimeout, 
                            _callbackInterceptor, _instanceFactory, tx, _classLoader, _autoStore );

                    if ( _txDbPool != null )
                        _txDbPool.put( tx, dbImpl );

                    tx.registerSynchronization( dbImpl );
                    return dbImpl;
                }
             } catch ( Exception except ) {
                // NamingException, SystemException, RollbackException
                if ( _logInterceptor != null )
                    _logInterceptor.exception( except );
            }
        }
        
        return new DatabaseImpl( _databaseName, _lockTimeout, 
                _callbackInterceptor, _instanceFactory, null, _classLoader, _autoStore );
    }

    /**
     * Load the JDO configuration from the specified input source using 
     * a custom class loader. In addition, a custom entity resolver can be 
     * provided. 
     * 
     * @param source The JDO configuration file describing the databases, connection factory and mappings.
     * @param resolver An (optional) entity resolver to resolve cached entities, e.g. for external mapping documents. 
     * @param loader The class loader to use, null for the default
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration (InputSource source, EntityResolver resolver,
            ClassLoader loader)
    throws MappingException
    {
        DatabaseRegistry.loadDatabase (source, resolver, loader);
        
        _classLoader = loader;
        _entityResolver = resolver;
        _source = source;
    }
    
    /**
     * Load the JDO configuration from the specified location using a 
     * custom class loader. 
     * @param url The location from which to load the JDOOld config file. 
     * @param loader The custom class loader to use, null for the default.
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration (String url, ClassLoader loader)
    throws MappingException
    {
        loadConfiguration (new InputSource (url), null, loader);
    }
    
    /**
     * Load the JDO configuration from the specified location.
     * @param url The location from which to load the JDOOld config file. 
     * @throws MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration (String url)
    throws MappingException
    {
        loadConfiguration (new InputSource (url), null, null);
    }
    
    public synchronized Reference getReference()
    {
    	Reference ref;
    	
    	// We use same object as factory.
    	ref = new Reference( getClass().getName(), getClass().getName(), null );
    	
    	if ( _description != null )
    		ref.add( new StringRefAddr( "description", _description ) );
    	if ( _databaseName != null )
    		ref.add( new StringRefAddr( "databaseName", _databaseName ) );
    	if ( _jdoConfURI != null )
    		ref.add( new StringRefAddr( "configuration", _jdoConfURI.toString() ) );
    	ref.add( new StringRefAddr( "lockTimeout", Integer.toString( _lockTimeout ) ) );
    	return ref;
    }
    
    
    public Object getObjectInstance( Object refObj, Name name, Context nameCtx, Hashtable env )
    throws NamingException
	{
    	Reference ref;
    	
    	// Can only reconstruct from a reference.
    	if ( refObj instanceof Reference ) {
    		ref = (Reference) refObj;
    		
    		// Make sure reference is of datasource class.
    		if ( !ref.getClassName().equals( getClass().getName() ) ) {
    			throw new NamingException( Messages.format ("jdo.reference.wrong.type", ref.getClassName()) );
    		}
    			
    		JDO2     ds;
    		RefAddr addr;
    		
    		try {
    			ds = (JDO2) Class.forName( ref.getClassName() ).newInstance();
    		} catch ( Exception except ) {
    			NamingException ne = new NamingException (Messages.format ("jdo.problem.loading.class", ref.getClassName()));
    			ne.setRootCause(except);
    			throw ne;
    		}
    		addr = ref.get ("description");
    		if ( addr != null ) {
    			ds._description = (String) addr.getContent();
    		}
    		addr = ref.get ("databaseName");
    		if ( addr != null ) {
    			ds._databaseName = (String) addr.getContent();
    		}
    		addr = ref.get ("configuration");
    		if ( addr != null ) {
    			ds._jdoConfURI = (InputSource) addr.getContent();
    		}
    		addr = ref.get ("lockTimeout");
    		if ( addr != null ) {
    			ds._lockTimeout = Integer.parseInt( (String) addr.getContent() );
    		}
    		return ds;
    			
    	} else if ( refObj instanceof Remote )
    		return refObj;
    	else
    		return null;
	}

}



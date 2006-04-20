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
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Hashtable;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import java.rmi.Remote;
import javax.naming.InitialContext;
import javax.naming.Referenceable;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.RefAddr;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.ObjectFactory;
import javax.transaction.TransactionManager;
import javax.transaction.Transaction;
import javax.transaction.Status;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.jdo.engine.DatabaseImpl;
import org.exolab.castor.jdo.engine.OQLQueryImpl;
import org.exolab.castor.jdo.engine.DatabaseRegistry;
import org.exolab.castor.persist.OutputLogInterceptor;
import org.exolab.castor.persist.spi.LogInterceptor;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.util.Messages;


/**
 * Implementation of the JDO engine used for obtaining database
 * connection. A JDO object is constructed with the name of a database
 * and other properties, and {@link #getDatabase} is used to obtain a
 * new database connection. Any number of database connections can be
 * obtained from the same JDO object.
 * <p>
 * The database configuration can be loaded using one of the {@link
 * #loadConfiguration} method. Alternatively, {@link #setConfiguration}
 * can be used to specify the URL of a database configuration file.
 * The configuration will loaded only once.
 * <p>
 * For example:
 * <pre>
 * <font color="red">// Load the database configuration</font>
 * JDO.loadConfiguration( "database.xml" );
 *
 * . . .
 *
 * JDO      jdo;
 * Database db;
 *
 * <font color="red">// Construct a new JDO for the database 'mydb'</font>
 * jdo = new JDO( "mydb" );
 * <font color="red">// Open a connection to the database</font>
 * db = jdo.getDatabase();
 * </pre>
 * Or,
 * <pre>
 * JDO      jdo;
 * Database db;
 *
 * <font color="red">// Construct a new JDO and specify the configuration file</font>
 * jdo = new JDO( "mydb" );
 * jdo.setConfiguration( "database.xml" );
 * <font color="red">// Open a connection to the database</font>
 * db = jdo.getDatabase();
 * </pre>
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDO
    implements DataObjects, Referenceable,
           ObjectFactory, Serializable
{


    /**
     * The default lock timeout for this database is 10 seconds.
     */
    public static final int DefaultLockTimeout = 10;


    /**
     * Tthe URL of the database configuration file. If the URL is
     * specified, the first attempt to load a database of this type
     * will use the specified configuration file.
     */
    private String          _dbConf;


    /**
     * The log intercpetor to which all logging and tracing messages
     * will be sent.
     */
    private LogInterceptor  _logInterceptor;


    /**
     * The callback interceptor to which all persistent state events
     * to be sent.
     */
    private CallbackInterceptor _callback;

    /**
     * The lock timeout for this database. Zero for immediate
     * timeout, an infinite value for no timeout. The timeout is
     * specified in seconds.
     */
    private int            _lockTimeout = DefaultLockTimeout;


    /**
     * The name of this database.
     */
    private String         _dbName;


    /**
     * Description of this database.
     */
    private String         _description = "Castor JDO";


    /**
     * The look up name for a transaction manager.
     */
    private String         _tmName = "java:comp/TransactionManager";


    /**
     * The transaction manager
     */
    private TransactionManager tm = null;


    /**
     * The application class loader.
     */
    private ClassLoader    _classLoader;


    /**
     * The resolver can be used to resolve cached entities, e.g.
     * for external mapping documents. 
     */
    private EntityResolver _entityResolver;


    /**
     * Constructs a new JDO database factory. Must call {@link
     * #setDatabaseName} before calling {@link #getDatabase}.
     */
    public JDO()
    {
    }


    /**
     * Constructs a new JDO database factory for databases with
     * the given name.
     *
     * @param name The database name
     */
    public JDO( String name )
    {
        _dbName = name;
    }


    /**
     * Returns the log writer for this database source.
     * <p>
     * The log writer is a character output stream to which all
     * logging and tracing messages will be printed.
     *
     * @return The log writer, null if disabled
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
        _callback = callback;
    }


    /**
     * Returns the log interceptor for this database source.
     *
     * @return The log interceptor, null if disabled
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
     */
    public void setClassLoader( ClassLoader classLoader)
    {
        _classLoader = classLoader;
    }


    /**
     * Returns the application classloader.
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
     */
    public void setEntityResolver( EntityResolver entityResolver)
    {
        _entityResolver = entityResolver;
    }


    /**
     * Returns the entity resolver.
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
     * Sets the name of this database. This attribute is required
     * in order to identify which database to open.
     * <p>
     * The standard name for this property is <tt>databaseName</tt>.
     *
     * @param name The name of this database
     */
    public void setDatabaseName( String name )
    {
        _dbName = name;
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
     * specified, use one of the {@link #loadConfiguration} methods
     * instead.
     * <p>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @param url The URL of the database configuration file
     */
    public void setConfiguration( String url )
    {
        _dbConf = url;
    }


    /**
     * Return the URL of the database configuration file.
     * <p>
     * The standard name for this property is <tt>configuration</tt>.
     *
     * @return The URL of the database configuration file
     */
    public String getConfiguration()
    {
        return _dbConf;
    }


    /**
     * Sets the JNDI name of the transaction manager. If set, JDO will look
     * up this name through JNDI and if available register itself
     * for synchronization and take part in a distributed transaction.
     * <p>
     * The standard name for this property is <tt>transactionManager</tt>.
     *
     * @param tmName The JNDI name of the transaction manager
     */
    public void setTransactionManager( String tmName )
    {
        _tmName = tmName;
    }


    /**
     * Returns the JNDI name of the transaction manager.
     *
     * @returns The JNDI name of the transaction manager
     */
    public String getTransactionManager()
    {
        return _tmName;
    }


    /**
     * Opens and returns a connection to the database. Throws an
     * {@link DatabaseNotFoundException} if the database named was not
     * set in the constructor or with a call to {@link #setDatabaseName},
     * or if no database configuration exists for the named database.
     *
     * @return An open connection to the database
     * @throws DatabaseNotFoundException Attempted to open a database
     *  that does not exist
     * @throws PersistenceException Database access failed
     */
    public Database getDatabase()
        throws DatabaseNotFoundException, PersistenceException
    {
        InputStream  is;

        if ( _dbName == null )
            throw new IllegalStateException( "Called 'getDatabase' without first setting database name" );
        if ( DatabaseRegistry.getDatabaseRegistry( _dbName ) == null ) {
            if ( _dbConf == null )
                throw new DatabaseNotFoundException( Messages.format( "jdo.dbNoMapping", _dbName ) );
            try {
                DatabaseRegistry.loadDatabase( new InputSource( _dbConf ), _entityResolver, _logInterceptor, _classLoader );
            } catch ( MappingException except ) {
                throw new DatabaseNotFoundException( except );
            }
        }

        if ( _tmName != null ) {
            InitialContext     ctx;
            Transaction        tx;
            DatabaseImpl       dbImpl;
            
            try {
                if(tm == null) {
                    ctx = new InitialContext();
                    tm = (TransactionManager) ctx.lookup( _tmName );
                }
                tx = tm.getTransaction();
                if ( tx.getStatus() == Status.STATUS_ACTIVE ) {
                    dbImpl = new DatabaseImpl( _dbName, _lockTimeout, _logInterceptor, 
                            _callback, tx, _classLoader );
                    tx.registerSynchronization( dbImpl );
                    return dbImpl;
                }
             } catch ( NoInitialContextException except ) {
                // No initial context. Just ignore.
             } catch ( NameNotFoundException except ) {
                // No TransactionManager object. Just ignore.
            } catch ( Exception except ) {
                // NamingException, SystemException, RollbackException
                if ( _logInterceptor != null )
                    _logInterceptor.exception( except );
            }
        }
        return new DatabaseImpl( _dbName, _lockTimeout, _logInterceptor, 
                _callback, null, _classLoader );
    }


    /**
     * Load database configuration from the specified URL. <tt>url</tt>
     * must point to a JDO configuration file describing the database
     * name, connection factory and mappings.
     * 
     * @param url The JDO configuration file
     * @throw MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration( String url )
        throws MappingException
    {
        DatabaseRegistry.loadDatabase( new InputSource( url ), null, null, null );
    }


    /**
     * Load database configuration from the specified URL. <tt>url</tt>
     * must point to a JDO configuration file describing the database
     * name, connection factory and mappings. <tt>loader</tt> is
     * optional, if null the default class loader is used.
     * 
     * @param url The JDO configuration file
     * @param loader The class loader to use, null for the default
     * @throw MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration( String url, ClassLoader loader )
        throws MappingException
    {
        DatabaseRegistry.loadDatabase( new InputSource( url ), null, null, loader );
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
     * @param resolve An optional entity resolver
     * @param loader The class loader to use, null for the default
     * @throw MappingException The mapping file is invalid, or any
     *  error occured trying to load the JDO configuration/mapping
     */
    public static void loadConfiguration( InputSource source, EntityResolver resolver,
                                          ClassLoader loader )
        throws MappingException
    {
        DatabaseRegistry.loadDatabase( source, resolver, null, loader );
    }


    public synchronized Reference getReference()
    {
    Reference ref;

    // We use same object as factory.
    ref = new Reference( getClass().getName(), getClass().getName(), null );

        if ( _description != null )
            ref.add( new StringRefAddr( "description", _description ) );
        if ( _dbName != null )
            ref.add( new StringRefAddr( "databaseName", _dbName ) );
        if ( _dbConf != null )
            ref.add( new StringRefAddr( "configuration", _dbConf ) );
        if ( _tmName != null )
            ref.add( new StringRefAddr( "transactionManager", _tmName ) );
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
        if ( ref.getClassName().equals( getClass().getName() ) ) {

        JDO     ds;
        RefAddr addr;

        try {
            ds = (JDO) Class.forName( ref.getClassName() ).newInstance();
        } catch ( Exception except ) {
            throw new NamingException( except.toString() );
        }
        addr = ref.get( "description" );
        if ( addr != null )
            ds._description = (String) addr.getContent();
        addr = ref.get( "databaseName" );
        if ( addr != null )
            ds._dbName = (String) addr.getContent();
        addr = ref.get( "configuration" );
        if ( addr != null )
            ds._dbConf = (String) addr.getContent();
        addr = ref.get( "lockTimeout" );
        if ( addr != null )
                    ds._lockTimeout = Integer.parseInt( (String) addr.getContent() );
        addr = ref.get( "transactionManager" );
        if ( addr != null )
                    ds._tmName = (String) addr.getContent();
        return ds;

        } else
        throw new NamingException( "JDO: Reference not constructed from class " + getClass().getName() );
    } else if ( refObj instanceof Remote )
        return refObj;
    else
        return null;
    }

}



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


import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;
// import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.ConversionException;
//import org.apache.commons.beanutils.ConvertUtils;
//import org.apache.commons.beanutils.Converter;
//import org.apache.commons.beanutils.converters.BigDecimalConverter;
//import org.apache.commons.beanutils.converters.BigIntegerConverter;
//import org.apache.commons.beanutils.converters.BooleanConverter;
//import org.apache.commons.beanutils.converters.ByteConverter;
//import org.apache.commons.beanutils.converters.DoubleConverter;
//import org.apache.commons.beanutils.converters.FloatConverter;
//import org.apache.commons.beanutils.converters.IntegerConverter;
//import org.apache.commons.beanutils.converters.LongConverter;
//import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.PersistenceEngineFactory;
import org.exolab.castor.persist.PersistenceFactoryRegistry;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.Messages;
import org.exolab.castor.xml.UnmarshalHandler;
import org.exolab.castor.xml.Unmarshaller;


/**
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date$
 */
public class DatabaseRegistry
{

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance( DatabaseRegistry.class );

	/**
     * The name of the generic SQL engine, if no SQL engine specified.
     */
    public static final String  GenericEngine = "generic";

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
    private LockEngine _engine;


    /**
     * Listings of all databases by name.
     */
    private static Hashtable  _databases = new Hashtable();

    /**
     * Database instances referenced ny engine. 
     */
    private static Hashtable  _byEngine = new Hashtable();


    public static String  DefaultMapping = "mapping.xml";


    /**
     * Construct a new database registry using a JDBC driver.
     *
     * @param name The database name
     * @param mapResolver The mapping resolver
     * @param factory Factory for persistence engines
     * @param jdbcUrl The JDBC URL
     * @param jdbcProps The JDBC properties
     * @throws MappingException Error occured when creating
     *  persistence engines for the mapping descriptors
     */
    DatabaseRegistry( String name, MappingResolver mapResolver, PersistenceFactory factory,
                      String jdbcUrl, Properties jdbcProps )
        throws MappingException
    {
        this( name, mapResolver, factory );
        _jdbcUrl = jdbcUrl;
        _jdbcProps = jdbcProps;
    }


    /**
     * Construct a new database registry using a <tt>DataSource</tt>.
     *
     * @param name The database name
     * @param mapResolver The mapping resolver
     * @param factory Factory for persistence engines
     * @param dataSource The data source
     * @throws MappingException Error occured when creating
     *  persistence engines for the mapping descriptors
     */
    DatabaseRegistry( String name, MappingResolver mapResolver, PersistenceFactory factory, DataSource dataSource )
        throws MappingException
    {
        this( name, mapResolver, factory );
        _dataSource = dataSource;
    }
    
    /**
     * Base constructor for a new database registry.
     *
     * @param name The database name
     * @param mapResolver The mapping resolver
     * @param factory Factory for persistence engines
     * @throws MappingException Error occured when creating
     *  persistence engines for the mapping descriptors
     */
    DatabaseRegistry( String name, MappingResolver mapResolver, PersistenceFactory factory )
        throws MappingException
    {
        _name = name;
        _mapResolver = mapResolver;
        _engine = new PersistenceEngineFactory().createEngine( mapResolver, factory );
        _byEngine.put( _engine, this );
    }



    public MappingResolver getMappingResolver()
    {
        return _mapResolver;
    }


    public String getName()
    {
        return _name;
    }

    public DataSource getDataSource()
    {
        return _dataSource;
    }


    /**
     * Instantiates a database instance from an im-memory JDO configuration.
     * @param jdoConf An in-memory JDO configuration. 
     * @param resolver An entity resolver.
     * @param loader A class loader
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase( JdoConf jdoConf, EntityResolver resolver, ClassLoader loader)
    	throws MappingException
	{
    	Database[] databases = JDOConfLoader.getDatabases(jdoConf);
    	loadDatabase(databases, resolver, loader, null);
	}

    /**
     * Instantiates a database instance from the JDO configuration file
     * @param source {@link InputSource} pointing to the JDO configuration. 
     * @param resolver An entity resolver.
     * @param loader A class loader
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
    public static synchronized void loadDatabase( InputSource source, EntityResolver resolver, ClassLoader loader )
        throws MappingException
    {
        // Load the JDO configuration file from the specified input source.
        Database[] databases = JDOConfLoader.getDatabases (source, resolver);
        loadDatabase (databases, resolver, loader, source.getSystemId());
    }
    
    /**
     * Instantiates a database instance from a castor.jdo.conf.Database instance
     * @param databases Database configuration instances. 
     * @param resolver An entity resolver.
     * @param loader A class loader
     * @throws MappingException If the database cannot be instantiated/loadeed.
     */
   	private static synchronized void loadDatabase(Database[] databases, EntityResolver resolver, ClassLoader loader, String baseURI)
        throws MappingException
    {
        Mapping            mapping;
        Enumeration        mappings;
        Database           database;
        // Database[]         databases;
        DatabaseRegistry   dbs;
        PersistenceFactory factory;

        try {
            // Load the JDO configuration file from the specified input source.
            // databases = JDOConfLoader.getDatabases (baseURI, resolver);
            
        	for (int i = 0; i < databases.length ;i++) {
        		
        		database = databases[i];
        		
        		// If the database was already configured, ignore
        		// this database configuration (allowing multiple loadings).
        		if ( _databases.get( database.getName() ) != null )
        			return;
        		
        		// Complain if no database engine was specified, otherwise get
        		// a persistence factory for that database engine.
        		if ( database.getEngine() == null  )
        			factory = PersistenceFactoryRegistry.getPersistenceFactory( GenericEngine );
        		else
        			factory = PersistenceFactoryRegistry.getPersistenceFactory( database.getEngine() );
        		if ( factory == null )
        			throw new MappingException( "jdo.noSuchEngine", database.getEngine() );
        		
        		// Load the mapping file from the URL specified in the database
        		// configuration file, relative to the configuration file.
        		// Fail if cannot load the mapping for whatever reason.
        		mapping = new Mapping( loader );
        		if ( resolver != null )
        			mapping.setEntityResolver( resolver );
        		if ( baseURI != null )
        			mapping.setBaseURL( baseURI );
        		
        		mappings = database.enumerateMapping();
        		while ( mappings.hasMoreElements() )
        		{
        			String mappingUrl = ( (org.exolab.castor.jdo.conf.Mapping) mappings.nextElement() ).getHref();
        			_log.debug( "Loading the mapping descriptor: " + mappingUrl );
        			
        			if ( mappingUrl != null )
        			{
        				mapping.loadMapping( mappingUrl );
        			}
        		}
        		
        		if (database.getDatabaseChoice() == null) {
        			throw new MappingException( "jdo.missingDataSource", database.getName() );
        		}
        		
                if ( database.getDatabaseChoice().getDriver() != null ) {
    				// JDO configuration file specifies a driver, use the driver
    				// properties to create a new registry object.
                    dbs = initFromDriver(mapping, database, factory);
    			} else if ( database.getDatabaseChoice().getDataSource() != null ) {
    				// JDO configuration file specifies a DataSource object, use the
    				// DataSource which was configured from the JDO configuration file
    				// to create a new registry object.
    				dbs = initFromDataSource(mapping, database, factory);
    			} else if ( database.getDatabaseChoice().getJndi() != null ) {
    				// JDO configuration file specifies a DataSource lookup through JNDI, 
    				// locate the DataSource object frome the JNDI namespace and use it.
    				dbs = initFromJNDI(mapping, database, factory);
                } else {
                    throw new MappingException( "jdo.missingDataSource", database.getName() );
                }
                
        		// Register the new registry object for the given database name.
        		_databases.put( database.getName(), dbs );
        	
        	}
        } catch ( MappingException except ) {
            throw except;
        } catch ( Exception except ) {
            throw new MappingException( except );
        }
    }


	/**
     * Initialize DatabaseRegistry instance using a JDBC DataSource instance. 
     * @param mapping Mapping instance.
     * @param database Configuration of the JDO Database element
     * @param factory PersistenceFactory instance.
     * @return DatabaseRegistry instance.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    public static DatabaseRegistry initFromDataSource(Mapping mapping, Database database, PersistenceFactory factory) 
		throws MappingException 
	{
        DatabaseRegistry dbs;
		DataSource dataSource;
		
		dataSource = loadDataSource (database);		
		
		dbs = new DatabaseRegistry (database.getName(), 
									mapping.getResolver (Mapping.JDO, factory), 
									factory,
									dataSource);
		
        _log.debug( "Using DataSource: " + database.getDatabaseChoice().getDataSource().getClassName() );
		return dbs;
	}
    
    public static DataSource loadDataSource (Database database) 
    	throws MappingException
	{

    	DataSource dataSource;
		Param[] parameters;
		Param param;
		
		String className = database.getDatabaseChoice().getDataSource().getClassName();
		
		try {
			dataSource = (DataSource) Class.forName (className, true, Thread.currentThread().getContextClassLoader()).newInstance();
		} 
		catch (Exception e) {
			throw new MappingException(Messages.format ("jdo.engine.classNotInstantiable", 
									   className), e);
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
			   handler.characters(param.getValue().toCharArray(), 0, param.getValue().length());
			   handler.endElement(param.getName());
			}

			handler.endElement("data-source");
			handler.endDocument();
		} catch (SAXException e) {
			_log.error ("Unable to parse <data-source> element.", e);
			throw new MappingException ("Unable to parse <data-source> element.", e);
		}
		
        return dataSource;
    }


	/**
     * Initialize DatabaseRegistry instance using a JDBC Driver. 
     * @param mapping Mapping instance.
     * @param database Configuration of the JDO Database element
     * @param factory PersistenceFactory instance.
     * @return DatabaseRegistry instance.
     * @throws MappingException Problem related to analysing the JDO configuration.
     * @throws SQLException Problem related to initialzing the JDBC driver.
     */
    private static DatabaseRegistry initFromDriver(Mapping mapping, Database database, PersistenceFactory factory) 
		throws MappingException, SQLException 
	{
		DatabaseRegistry dbs;
		Properties  props;
		Enumeration params;
		Param       param;

        String driverName = database.getDatabaseChoice().getDriver().getClassName();
		if (driverName != null ) {
		    try {
		        Class.forName (database.getDatabaseChoice().getDriver().getClassName()).newInstance();
		    } 
			catch (InstantiationException e) {
				_log.error (Messages.format ("jdo.engine.classNotInstantiable", driverName), e);
		        throw new MappingException(Messages.format ("jdo.engine.classNotInstantiable", driverName), e);
			} 
			catch (IllegalAccessException e) {
				_log.error (Messages.format ("jdo.engine.classNotAccessable", driverName, "constructor"), e);
		        throw new MappingException(Messages.format ("jdo.engine.classNotAccessable", driverName, "constructor"), e);
			} 
			catch (ClassNotFoundException e) {
				_log.error ("Can not load class " + driverName, e);
		        throw new MappingException("Can not load class " + driverName, e);
			} 
		}

		if (DriverManager.getDriver (database.getDatabaseChoice().getDriver().getUrl()) == null)
		    throw new MappingException( "jdo.missingDriver", database.getDatabaseChoice().getDriver().getUrl());

		props = new Properties();
		params = database.getDatabaseChoice().getDriver().enumerateParam();
		while (params.hasMoreElements()) {
		    param = (Param) params.nextElement();
		    props.put (param.getName(), param.getValue());
		}

		dbs = new DatabaseRegistry (database.getName(), 
									mapping.getResolver(Mapping.JDO, factory), 
									factory,
		                            database.getDatabaseChoice().getDriver().getUrl(), 
									props );
		
        _log.debug( "Using driver: " + driverName );
        
		return dbs;

	}


	/**
     * 
     * Initialize DatabaseRegistry instance using a JDBC DataSource object bound to
     * the JNDI ENC. 
     * @param mapping Mapping instance.
     * @param database Configuration of the JDO Database element
     * @param factory PersistenceFactory instance.
     * @return DatabaseRegistry instance.
     * @throws MappingException Problem related to analysing the JDO configuration.
     */
    private static DatabaseRegistry initFromJNDI(Mapping mapping, 
												 Database database, 
												 PersistenceFactory factory) 
    	throws MappingException 
    {
		DatabaseRegistry dbs;
		Object    dataSource;

		if (_log.isDebugEnabled()) {
			_log.debug( "Using DataSource from JNDI ENC: " + database.getDatabaseChoice().getJndi().getName() );
		}

		try {
			Context initialContext = new InitialContext(); 
		    dataSource = initialContext.lookup (database.getDatabaseChoice().getJndi().getName());
		} 
		catch (NameNotFoundException e ) {	
		    throw new MappingException( "jdo.jndiNameNotFound", database.getDatabaseChoice().getJndi().getName(), e);
		} 
		catch (NamingException e) {
		    throw new MappingException(e);
		}
		if ( !(dataSource instanceof DataSource))
		    throw new MappingException( "jdo.jndiNameNotFound", database.getDatabaseChoice().getJndi().getName());

		dbs = new DatabaseRegistry (database.getName(), 
									mapping.getResolver(Mapping.JDO, factory ),
 									factory,
		                            (DataSource) dataSource );
		
		return dbs;
    }


	public Connection createConnection()
        throws SQLException
    {
        if ( _dataSource != null ) {
            return _dataSource.getConnection();
        }

        return DriverManager.getConnection( _jdbcUrl, _jdbcProps );

    }


    static LockEngine getLockEngine( Class objType )
    {
        Enumeration      enumeration;
        DatabaseRegistry dbs;

        enumeration = _databases.elements();
        while ( enumeration.hasMoreElements() ) {
            dbs = (DatabaseRegistry) enumeration.nextElement();
            if ( dbs._mapResolver.getDescriptor( objType ) != null )
                return dbs._engine;
        }
        return null;
    }


    static LockEngine getLockEngine( DatabaseRegistry dbs )
    {
        return dbs._engine;
    }


    public static DatabaseRegistry getDatabaseRegistry( Object obj )
    {
        return getDatabaseRegistry(obj.getClass());
    }

    public static boolean hasDatabaseRegistries() {
        return (!_databases.isEmpty());
    }
    
    public static DatabaseRegistry getDatabaseRegistry( Class c )
    {
        Enumeration      enumeration;
        DatabaseRegistry dbs;

        enumeration = _databases.elements();
        while ( enumeration.hasMoreElements() ) {
            dbs = (DatabaseRegistry) enumeration.nextElement();
            if ( dbs._mapResolver.getDescriptor( c ) != null )
                return dbs;
        }
        return null;
    }

    public static synchronized DatabaseRegistry getDatabaseRegistry( String name )
    {
        DatabaseRegistry dbs;
        dbs = (DatabaseRegistry) _databases.get( name);
        return dbs;
    }


    static Connection createConnection( LockEngine engine )
        throws SQLException
    {
        DatabaseRegistry dbs;

        dbs = (DatabaseRegistry) _byEngine.get( engine );
        if ( dbs._dataSource != null ) {
            return dbs._dataSource.getConnection();
        }

        return DriverManager.getConnection( dbs._jdbcUrl, dbs._jdbcProps );
    }


    /**
     * Reset the database configuration.
     */
	public static void clear()
	{
		_databases.clear();
		_byEngine.clear();

		 // reset the JDO configuration data to re-enable loadConfiguration()
		JDOConfLoader.deleteConfiguration();
	}

}

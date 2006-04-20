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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.persist.session;


import java.io.IOException;
import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Map;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.mapping.MappingException;
//import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.PersistenceFactoryRegistry;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.LogInterceptor;
import org.exolab.castor.persist.resolvers.Resolver;
import org.exolab.castor.util.DTDResolver;



/**
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DatabaseRegistry
{


    /**
     * The name of the generic SQL engine, if no SQL engine specified.
     */
    public static final String  GenericEngine = "generic";


    /**
     * The JDBC URL when using a JDBC driver.
     */
    //private String            _jdbcUrl;


    /**
     * The properties when using a JDBC driver.
     */
    //private Properties        _jdbcProps;


    /**
     * The data source when using a DataSource.
     */
    //private DataSource        _dataSource;


    /**
     * The map resolver for this database source.
     */
    //private MappingResolver   _mapResolver;

    private Map                 _resolvers;
                            

    private LockEngine          _lockEngine;
    
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


    //private static Hashtable  _byEngine = new Hashtable();


    public static String  DefaultMapping = "mapping.xml";


    /**
     * Construct a new database registry using a JDBC driver.
     *
     * @param name The database name
     * @param mapResolver The mapping resolver
     * @param factory Factory for persistence engines
     * @param jdbcURL The JDBC URL
     * @param jdbcProps The JDBC properties
     * @param log For tracing messages
     * @throws MappingException Error occured when creating
     *  persistence engines for the mapping descriptors
     */
     /*
    DatabaseRegistry( String name, MappingRoot mapping, PersistenceFactory factory,
                      String jdbcUrl, Properties jdbcProps, LogInterceptor log )
        throws MappingException
    {
        this( name, mapResolver, factory, log );
        _jdbcUrl = jdbcUrl;
        _jdbcProps = jdbcProps;
    } */


    /**
     * Construct a new database registry using a <tt>DataSource</tt>.
     *
     * @param name The database name
     * @param mapResolver The mapping resolver
     * @param factory Factory for persistence engines
     * @param dataSource The data source
     * @param log For tracing messages
     * @throws MappingException Error occured when creating
     *  persistence engines for the mapping descriptors
     */
     /*
    DatabaseRegistry( String name, MappingRoot mapping, PersistenceFactory factory,
                    DataSource dataSource, LogInterceptor log )
            throws MappingException {

        this( name, mapResolver, factory, log);
        _dataSource = dataSource;
    } */
    
    /**
     * Base constructor for a new database registry.
     *
     * @param name The database name
     * @param mapResolver The mapping resolver
     * @param factory Factory for persistence engines
     * @param log For tracing messages
     * @throws MappingException Error occured when creating
     *  persistence engines for the mapping descriptors
     */
    DatabaseRegistry( String name, MappingRoot mapping, PersistenceFactory factory,
            Database conf, LogInterceptor log )
            throws MappingException, PersistenceException {

        _name = name;

        // Ok! Now we have MappingRoot and PersistenceFactory, we can initialize
        // the DatabaseRegistry
        _lockEngine = new LockEngine( factory, conf, log );

        // First, initalizes the mapping layer
        _resolvers = Resolver.createResolvers( mapping, _lockEngine, log );
    }

    public String getName() {
        return _name;
    }

    public Resolver getResolver( Class cls ) {
        return (Resolver) _resolvers.get( cls.getName() );
    }

    public LockEngine getLockEngine() {
        return _lockEngine;
    }

    
    /**
     * Get a DatabaseRegistry entry specified in the inputSource. The InputSource 
     * will be unmarshalled into an org.exolab.castor.mapping.xml.Database
     * instance. If there is already an entry exist with the same name as specified
     * in the unmarshalled Database object, this method will have no effect.
     *
     */
    public static synchronized void loadDatabase( InputSource source, EntityResolver resolver,
                                                  LogInterceptor log, ClassLoader loader )
            throws MappingException {

        Unmarshaller       unm;
        Mapping            mapping;
        Enumeration        mappings;
        Database           database;
        DatabaseRegistry   dbs;
        PersistenceFactory factory;

        unm = new Unmarshaller( Database.class );
        try {
            // Load the JDO database configuration file from the specified
            // input source. If the database was already configured, ignore
            // this file (allowing multiple loadings).
            if ( resolver == null )
                unm.setEntityResolver( new DTDResolver() );
            else
                unm.setEntityResolver( new DTDResolver( resolver ) );
            database = (Database) unm.unmarshal( source );
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
            if ( source.getSystemId() != null )
                mapping.setBaseURL( source.getSystemId() );
            mappings = database.enumerateMapping();
            while ( mappings.hasMoreElements() )
                mapping.loadMapping( ( (org.exolab.castor.jdo.conf.Mapping) mappings.nextElement() ).getHref() );

            /*
            if ( database.getDriver() != null ) {
                // JDO configuration file specifies a driver, use the driver
                // properties to create a new registry object.
                Properties  props;
                Enumeration params;
                Param       param;

                if ( database.getDriver().getClassName() != null ) {
                    try {
                        Class.forName( database.getDriver().getClassName() ).newInstance();
                    } catch ( Exception except ) {
                        throw new MappingException( except );
                    }
                }
                if ( DriverManager.getDriver( database.getDriver().getUrl() ) == null )
                    throw new MappingException( "jdo.missingDriver", database.getDriver().getUrl() );

                props = new Properties();
                params = database.getDriver().enumerateParam();
                while ( params.hasMoreElements() ) {
                    param = (Param) params.nextElement();
                    props.put( param.getName(), param.getValue() );
                }
                dbs = new DatabaseRegistry( database.getName(), mapping.getRoot(), factory,
                                            database.getDriver().getUrl(), props, log );
            } else if ( database.getDataSource() != null ) {
                // JDO configuration file specifies a DataSource object, use the
                // DataSource which was configured from the JDO configuration file
                // to create a new registry object.
                DataSource ds;

                ds = (DataSource) database.getDataSource().getParams();
                if ( ds == null )
                    throw new MappingException( "jdo.missingDataSource", database.getName() );
                dbs = new DatabaseRegistry( database.getName(), mapping.getRoot(), factory,
                                            ds, log );
            } else if ( database.getJndi() != null ) {
                // JDO configuration file specifies a DataSource lookup through JNDI,
                // locate the DataSource object frome the JNDI namespace and use it.
                Object    ds;

                try {
                    ds = new InitialContext().lookup( database.getJndi().getName() );
                } catch ( NameNotFoundException except ) {
                    throw new MappingException( "jdo.jndiNameNotFound", database.getJndi().getName() );
                } catch ( NamingException except ) {
                    throw new MappingException( except );
                }
                if ( ! ( ds instanceof DataSource ) )
                    throw new MappingException( "jdo.jndiNameNotFound", database.getJndi().getName() );

                dbs = new DatabaseRegistry( database.getName(), mapping.getRoot(), factory,
                                            (DataSource) ds, log );
            } else {
                throw new MappingException( "jdo.missingDataSource", database.getName() );
            }*/

            dbs = new DatabaseRegistry( database.getName(), mapping.getRoot(), factory, database, log );

            // Register the new registry object for the given database name.
            _databases.put( database.getName(), dbs );

        } catch ( MappingException except ) {
            throw except;
        } catch ( Exception except ) {
            throw new MappingException( except );
        }
    }

    /*

    public Connection createConnection()
        throws SQLException
    {
        if ( _dataSource != null )
            return _dataSource.getConnection();
        else
            return DriverManager.getConnection( _jdbcUrl, _jdbcProps );
    }


    static LockEngine getLockEngine( Class objType )
    {
        Enumeration      enum;
        DatabaseRegistry dbs;

        enum = _databases.elements();
        while ( enum.hasMoreElements() ) {
            dbs = (DatabaseRegistry) enum.nextElement();
            if ( dbs._mapResolver.getDescriptor( objType ) != null )
                return dbs._engine;
        }
        return null;
    }


    static LockEngine getLockEngine( DatabaseRegistry dbs )
    {
        return dbs._engine;
    } */


    public static DatabaseRegistry getDatabaseRegistry( Object obj )
    {
        Enumeration      enum;
        DatabaseRegistry dbs;

        enum = _databases.elements();
        while ( enum.hasMoreElements() ) {
            dbs = (DatabaseRegistry) enum.nextElement();
            if ( dbs._resolvers.get( obj.getClass() ) != null )
                return dbs;
        }
        return null;
    }

    public static synchronized DatabaseRegistry getDatabaseRegistry( String name )
    {
        DatabaseRegistry dbs;
        dbs = (DatabaseRegistry) _databases.get( name );
        return dbs;
    }


    /*
    static Connection createConnection( LockEngine engine )
        throws SQLException
    {
        DatabaseRegistry dbs;

        dbs = (DatabaseRegistry) _byEngine.get( engine );
        if ( dbs._dataSource != null )
            return dbs._dataSource.getConnection();
        else
            return DriverManager.getConnection( dbs._jdbcUrl, dbs._jdbcProps );
    }*/


}

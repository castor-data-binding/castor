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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo.engine;


import java.io.PrintWriter;
import java.util.Properties;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.odmg.ODMGException;
import org.odmg.DatabaseNotFoundException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.jdo.MappingException;
import org.exolab.castor.jdo.MappingTable;
import org.exolab.castor.jdo.mapping.Databases;
import org.exolab.castor.jdo.mapping.Database;
import org.exolab.castor.jdo.mapping.Param;
import org.exolab.castor.jdo.mapping.Mapping;
import org.exolab.castor.jdo.mapping.Include;
import org.exolab.castor.jdo.schema.DTDResolver;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class DatabaseSource
{


    private String            _driverUrl;
    
    
    private Properties        _driverProps;
    
    
    private DataSource        _dataSource;


    private MappingTable      _mapTable = MappingTable.getDefaultMapping();


    private String            _dbName;


    private static Hashtable  _dataSources = new Hashtable();


    DatabaseSource( String dbName, String url, Properties props, MappingTable mapTable )
    {
	_driverUrl = url;
	_driverProps = props;
	if ( mapTable != null )
	    _mapTable = mapTable;
	_dbName = dbName;
    }


    DatabaseSource( String dbName, DataSource dataSource, MappingTable mapTable )
    {
	_dataSource = dataSource;
	if ( mapTable != null )
	    _mapTable = mapTable;
	_dbName = dbName;
    }


    DatabaseSource( String dbName, MappingTable mapTable )
    {
	if ( mapTable != null )
	    _mapTable = mapTable;
	_dbName = dbName;
    }


    public static synchronized void loadMapping( InputSource source )
	throws MappingException
    {
	loadMapping( source, null, null );
    }


    public static synchronized void loadMapping( InputSource source,
						 EntityResolver resolver,
						 PrintWriter logWriter )
	throws MappingException
    {
	Unmarshaller unm;

	unm = new Unmarshaller( Databases.class );
	try {
	    if ( resolver == null )
		unm.setEntityResolver( new DTDResolver() );
	    else
		unm.setEntityResolver( new DTDResolver( resolver ) );
	    if ( logWriter != null )
		unm.setLogWriter( logWriter );
	    loadMapping( (Databases) unm.unmarshal( source ) );
	} catch ( Exception except ) {
	    throw new MappingException( "Nested error: " + except.getMessage() );
	}
    }


    public static synchronized void loadMapping( Databases databases )
	throws MappingException
    {
	Enumeration  enum;
	Enumeration  mapping;
	Database     db;
	MappingTable mapTable;

	enum = databases.listDatabases();
	while ( enum.hasMoreElements() ) {
	    db = (Database) enum.nextElement();
	    mapTable = new MappingTable();

	    mapping = db.listIncludes();
	    while ( mapping.hasMoreElements() ) {
		Include        include;
		DatabaseSource dbs;

		include = (Include) mapping.nextElement();
		dbs = getDatabaseSource( include.getDbName() );
		if ( dbs == null ) {
		    if ( include.getUri() != null ) {
			loadMapping( new InputSource( include.getUri() ) );
			dbs = getDatabaseSource( db.getDbName() );
		    }
		    if ( dbs == null )
			throw new MappingException( "Missing included database mapping " +
						    db.getDbName() );
		}
		mapTable.addMapping( dbs.getMappingTable() );
	    }

	    mapping = db.listMappings();
	    while ( mapping.hasMoreElements() ) {
		mapTable.addMapping( (Mapping) mapping.nextElement() );
	    }


	    if ( db.getDriver() != null ) {
		Properties  props;
		Enumeration params;
		Param       param;

		if ( db.getDriver().getClassName() != null ) {
		    try {
			Class.forName( db.getDriver().getClassName() );
		    } catch ( ClassNotFoundException except ) {
			throw new MappingException( except.toString() );
		    }
		}
		props = new Properties();
		params = db.getDriver().listParams();
		while ( params.hasMoreElements() ) {
		    param = (Param) params.nextElement();
		    props.put( param.getName(), param.getValue() );
		}
		registerDriver( db.getDbName(), db.getDriver().getUrl(), props, mapTable );
	    } else if ( db.getDataSource() != null ) {
		DataSource ds;

		ds = (DataSource) db.getDataSource().getParams();
		if ( ds == null )
		    throw new MappingException( "No data source specified for database " +
						db.getDbName() );
		registerDataSource( db.getDbName(), ds, mapTable );
	    } else if ( db.getDataSourceRef() != null ) {
		Object    ds;
		
		try {
		    ds = new InitialContext().lookup( db.getDbName() );
		} catch ( NameNotFoundException except ) {
		    throw new MappingException( "The JNDI name " + db.getDbName() +
						" does not map to a DataSource" );
		} catch ( NamingException except ) {
		    throw new MappingException( "Nested exception: " + except.toString() );
		}
		if ( ! ( ds instanceof DataSource ) )
		    throw new MappingException( "The JNDI name " + db.getDbName() +
						" does not map to a DataSource" );
		registerDataSource( db.getDbName(), (DataSource) ds, mapTable );
	    } else {
		registerMapping( db.getDbName(), mapTable );
	    }
	}
    }


    public static synchronized DatabaseSource registerDriver( String dbName, String url, Properties props,
							      MappingTable mapTable )
	throws MappingException
    {
	DatabaseSource dbs;

	synchronized ( _dataSources ) {
	    if ( _dataSources.get( dbName ) != null )
		throw new MappingException( "A database with the name " + dbName + " is already registered" );
	    try {
		if ( DriverManager.getDriver( url ) == null )
		    throw new MappingException( "No suitable driver found for URL " + url +
						" - check if URL is correct and driver accessible in classpath" );
	    } catch ( SQLException except ) {
		throw new MappingException( "Error obtaining driver for URL " + url +
					    ": " + except.getMessage() );
	    }
	    if ( props == null )
		props = new Properties();
	    dbs = new DatabaseSource( dbName, url, props, mapTable );
	    _dataSources.put( dbName, dbs );
	    return dbs;
	}
    }


    public static synchronized DatabaseSource registerDataSource( String dbName, DataSource dataSource,
								  MappingTable mapTable )
	throws MappingException
    {
	DatabaseSource dbs;

	synchronized ( _dataSources ) {
	    if ( _dataSources.get( dbName ) != null )
		throw new MappingException( "A database with the name " + dbName + " is already registered" );
	    if ( dataSource == null )
		throw new NullPointerException( "Argument 'dataSource' is null" );
	    dbs = new DatabaseSource( dbName, dataSource, mapTable );
	    _dataSources.put( dbName, dbs );
	    return dbs;
	}
    }


    public static synchronized DatabaseSource registerMapping( String dbName, MappingTable mapTable )
	throws MappingException
    {
	DatabaseSource dbs;

	synchronized ( _dataSources ) {
	    if ( _dataSources.get( dbName ) != null )
		throw new MappingException( "A database with the name " + dbName + " is already registered" );
	    dbs = new DatabaseSource( dbName, mapTable );
	    _dataSources.put( dbName, dbs );
	    return dbs;
	}
    }


    static DatabaseSource getDatabaseSource( Object obj )
    {
	Enumeration    enum;
	DatabaseSource dbs;

	enum = _dataSources.elements();
	while ( enum.hasMoreElements() ) {
	    dbs = (DatabaseSource) enum.nextElement();
	    if ( dbs._mapTable.getDescriptor( obj.getClass() ) != null )
		return dbs;
	}
	return null;
    }


    static synchronized DatabaseSource getDatabaseSource( String dbName )
	throws MappingException
    {
	DatabaseSource dbs;

	dbs = (DatabaseSource) _dataSources.get( dbName );
	if ( dbs == null ) {
	    dbs = registerDatabase( dbName );
	}
	return dbs;
    }


    Connection getConnection()
	throws SQLException
    {
	if ( _driverUrl != null ) {
	    return DriverManager.getConnection( _driverUrl, _driverProps );
	} else {
	    return _dataSource.getConnection();
	}
    }


    MappingTable getMappingTable()
    {
	return _mapTable;
    }


    String getName()
    {
	return _dbName;
    }


    boolean canConnect()
    {
	return ( _driverUrl != null || _dataSource != null );
    }


    static DatabaseSource registerDatabase( String dbName )
	throws MappingException
    {
	Object obj;

	if ( dbName.startsWith( "jdbc:" ) ) {
	    return registerDriver( dbName, dbName, null, null );
	} else if ( dbName.startsWith( "java:" ) ) {
	    try {
		obj = new InitialContext().lookup( dbName );
	    } catch ( NameNotFoundException except ) {
		throw new MappingException( "The JNDI name " + dbName + " does not map to a DataSource" );
	    } catch ( NamingException except ) {
		throw new MappingException( except.toString() );
	    }
	    if ( obj instanceof DataSource ) {
		return registerDataSource( dbName, (DataSource) obj, null );
	    } else {
		throw new MappingException( "The JNDI name " + dbName + " does not map to a DataSource" );
	    }
	}
	return null;
    }



}

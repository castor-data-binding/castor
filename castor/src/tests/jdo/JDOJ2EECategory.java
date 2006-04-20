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
 */

package jdo;


import java.sql.Connection;
import java.sql.SQLException;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.DatabaseRegistry;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.Logger;
import org.exolab.castor.util.Messages;

import java.net.URL;
import java.net.MalformedURLException;
import harness.TestHarness;

import javax.naming.*;
//import javax.ejb.*;

//import mock ejb stuff for JNDI
import org.mockejb.*;
import org.mockejb.interceptor.*;
import org.mockejb.jndi.*;

//import tyrex classes for transaction management
import tyrex.tm.TransactionDomain;
import tyrex.resource.Resources;
import tyrex.resource.Resource;
import tyrex.resource.ResourceException;
import tyrex.tm.DomainConfigurationException;
import tyrex.tm.RecoveryException;

//import javax.sql stuff
import javax.sql.DataSource;

//javax.transaction classes
import javax.transaction.UserTransaction;
import javax.transaction.TransactionManager;


/**
 * This Category is intended to allow developers to test Castor in a J2EE 
 * environment with managed transactions and JNDI.
 * @author <a href="patrick.vankann@fortune-cookie.com">Patrick van Kann</a>
 */

public class JDOJ2EECategory extends TestHarness {
	
	//our JDO 
	private JDO      _jdo;
	
	//initial jndi context to bind 
	//transaction manager and datasource to
	private Context context;
	
	//Tyrex domain and resources
	private TransactionDomain domain;
	private Resources resources;
	private Resource DSResource;
	private DataSource dataSource;
	
	//TransactionManger and UserTransaction
	private TransactionManager tm;
	private UserTransaction ut;
	/*
	 names to bind our resources to in JNDI.
	 */
	//this must match the jndiEnc attribute in j2ee.xml transaction-manager element
	static String TMJNDIName = "java:/TransactionManager";
	//this must match the jndi name element in the database attriute in j2ee.xml
	static String DSJNDIName = "java:/jdbc/castorDS";
	//this is the standard location for UserTransactions
	static String UTJNDIName = "java:/UserTransaction";
	
	public JDOJ2EECategory( TestHarness superTest, String name, String description, Object jdo ) 
	throws Exception {
		super( superTest, name, description );
		_jdo = (JDO) jdo;
		try {
			new URL( _jdo.getConfiguration() );
		} catch ( MalformedURLException e ) {
			_jdo.setConfiguration( getClass().getResource( _jdo.getConfiguration() ).toString() );
		}
		
		/* 
		 The following code configures the J2EE environment.
		 It creates an InitialContext in which to bind
		 1) A TransactionManager 
		 2) A Datasource
		 3) A UserTransaction
		 all delivered from a Tyrex domain.
		 */
		
		//set up a JNDI environment
		try 
		{
			//set the MockContext as the initial context
			MockContextFactory.setAsInitial();
			//create the initial context that will be used for binding our TM
			context = new InitialContext( );
		}
		catch ( NamingException e )
		{
			throw new Exception( "Couldn't set up an InitialContext: " + e.getMessage() );
		}
		
		//set up Tyrex
		//this uses the tyrex-domain.xml config in the tests/jdo directory 
		try 
		{
			domain = TransactionDomain.createDomain( "src/tests/jdo/tyrex-domain.xml" );
			domain.recover();
			resources = domain.getResources();
		}
		catch ( DomainConfigurationException e )
		{
			throw new Exception( "Failed to configure Tyrex domain: " + e.getMessage() );
		}
		catch ( RecoveryException e )
		{
			throw new Exception( "Failed to start the Tyrex domain: " + e.getMessage() );
		}
		
		//get the TransactionManager from Tyrex
		tm = domain.getTransactionManager();
		
		//get the datasource from Tyrex
		try 
		{
			DSResource = resources.getResource( "castor-datasource" );
			dataSource = (DataSource) DSResource;
		}
		catch ( ResourceException e ) 
		{
			throw new Exception( "Failed to get DataSource from Tyrex: " + e.getMessage() );
		}
		
		//get a UserTransaction from Tyrex
		ut = domain.getUserTransaction();
		
		//put everything in JNDI
		try
		{
			context.bind( TMJNDIName ,  tm );
			context.bind( DSJNDIName , dataSource );
			context.bind( UTJNDIName , ut );
		}
		catch ( NamingException e ) 
		{
			throw new Exception( "Couldn't bind to JNDI: " + e.getMessage() );
		}
		
		_jdo.setDatabaseName( "test" );
		_jdo.setLockTimeout( 120 );
		_jdo.setAutoStore( true );
		_jdo.setDatabasePooling( true );
		_jdo.setClassLoader(Thread.currentThread().getContextClassLoader());
		
		if ( verbose ) {
			_jdo.setLogWriter( Logger.getSystemLogger() );
		}
		
	}
	
	/*
	 Retrieve a Database from the configured
	 JDO object.
	 */
	public Database getDatabase( boolean verbose )
	throws PersistenceException
	{
		return _jdo.getDatabase();
	}
	
	/*
	 Obtain the JDO object directly. 
	 */
	public JDO getJDO() {
		return _jdo;
	}
	
    public Connection getJDBCConnection() throws SQLException {
        String name = _jdo.getDatabaseName();
        try {
            return DatabaseRegistry.getDatabaseRegistry(name).createConnection();
        } catch (MappingException ex) {
            throw new SQLException(Messages.format("jdo.dbNoMapping", name));
        }
    }
	
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *	  statements and notices.  Redistributions must also contain a
 *	  copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *	  above copyright notice, this list of conditions and the
 *	  following disclaimer in the documentation and/or other
 *	  materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *	  products derived from this Software without prior written
 *	  permission of Intalio, Inc.  For written permission,
 *	  please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *	  nor may "Exolab" appear in their names without prior written
 *	  permission of Intalio, Inc. Exolab is a registered
 *	  trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *	  (http://www.exolab.org/).
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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.util;

import java.util.Enumeration;
import java.util.Properties;

import org.exolab.castor.jdo.conf.DataSource;
import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.DatabaseChoice;
import org.exolab.castor.jdo.conf.Driver;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Mapping;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.jdo.conf.TransactionManager;


/**
 * Factory to create JDO configurations without the need of a database configuration XML file
 * 
 * <p>This is an example for setting up a JDO configuration using JdoConfFactory:</p>
 * 
 * <code>
 * JDO jdo = new JDO();
 *
 * jdo.setDatabaseName(db_name);
 *
 * org.exolab.castor.jdo.conf.Database jdoDbConf;
 *
 * jdoDbConf = JdoConfFactory.createJdoDbConf(db_name, "oracle", 
 *			   JdoConfFactory.createJdoDriverConf("oracle.jdbc.driver.OracleDriver",
 *												  db_url, username, password));
 *
 * jdoDbConf.addMapping(JdoConfFactory.createJdoMappingConf(map_url.toString()));
 *
 * jdo.setConfiguration(JdoConfFactory.createJdoConf(jdoDbConf));
 * </code>
 *
 * <p>Alternatively to using a org.exolab.castor.jdo.conf.Driver
 * configuration you can also use a JDBC 2.0 DataSource:</p>
 *
 * <code>
 * OracleDataSource ds = new OracleDataSource();
 *
 * ds.setURL(db_url);
 * ds.setUser(username);
 * ds.setPassword(password);
 *
 * jdoDbConf = JdoConfFactory.createJdoDbConf(db_name, "oracle", JdoConfFactory.createJdoDSConf(ds));
 * </code>
 *
 * @author Martin Fuchs <martin-fuchs AT gmx DOT net>
 */
public class JdoConfFactory
{
	/**
	 * @param name name of the database configuration
	 * @param engine name of the database engine
	 * @return Database configuration
	 */
	public static Database createJdoDbConf(String name, String engine)
	{
		Database dbConf = new Database();

		dbConf.setName(name);
		dbConf.setEngine(engine);

		return dbConf;
	}

	/**
	 * @param db_name name of the database configuration
	 * @param engine name of the database engine
	 * @param dsConf JDO datasource configuration
	 * @return Database configuration
	 */
	public static Database createJdoDbConf(String db_name, String engine, DataSource dsConf)
	{
		Database dbConf = createJdoDbConf(db_name, engine);
		DatabaseChoice dbChoice = new DatabaseChoice();

		dbChoice.setDataSource(dsConf);
		dbConf.setDatabaseChoice(dbChoice);

		return dbConf;
	}

	/**
	 * @param db_name name of the database configuration
	 * @param engine name of the database engine
	 * @param driverConf JDO driver configuration
	 * @return Database configuration
	 */
	public static Database createJdoDbConf(String db_name, String engine, Driver driverConf)
	{
		Database dbConf = createJdoDbConf(db_name, engine);
		DatabaseChoice dbChoice = new DatabaseChoice();

		dbChoice.setDriver(driverConf);
		dbConf.setDatabaseChoice(dbChoice);

		return dbConf;
	}


	/**
	 * create a JDO configuration
	 * @param jdoDbConf Database configuration
	 * @param transConf TransactionDemarcation configuration 
	 * @return JDO configuration
	 */
	public static JdoConf createJdoConf(Database jdoDbConf, TransactionDemarcation transConf)
	{
		return createJdoConf (new Database[] {jdoDbConf}, transConf);
	}

	/**
	 * create a JDO configuration
	 * @param jdoDbConfArray Database configuration
	 * @param transConf TransactionDemarcation configuration 
	 * @return JDO configuration
	 */
	public static JdoConf createJdoConf(Database[] jdoDbConfArray, TransactionDemarcation transConf)
	{
		JdoConf jdoConf = new JdoConf();

		jdoConf.setDatabase(jdoDbConfArray);
		jdoConf.setTransactionDemarcation(transConf);

		return jdoConf;
	}

	/**
	 * create a JDO configuration with simple local transaction demarcation
	 * @param jdoDbConf Database configuration
	 * @return JDO configuration
	 */
	public static JdoConf createJdoConf(Database jdoDbConf)
	{
		return createJdoConf (new Database[] { jdoDbConf } );
	}


	/**
	 * create a JDO configuration with simple local transaction demarcation
	 * @param jdoDbConfArray Database configuration
	 * @return JDO configuration
	 */
	public static JdoConf createJdoConf(Database[] jdoDbConfArray)
	{
		JdoConf jdoConf = new JdoConf();

		jdoConf.setDatabase(jdoDbConfArray);
		jdoConf.setTransactionDemarcation(createSimpleTransactionDemarcationConf());

		return jdoConf;
	}

	/**
	 * create a JDO driver configuration from JDBC connection parameters 
	 * @param driver_name JDBC driver name
	 * @param db_url JDBC connect string
	 * @param username user name for the DB login
	 * @param password password for the DB login
	 * @return JDO driver configuration
	 */
	public static Driver createJdoDriverConf(String driver_name, String db_url, String username, String password)
	{
		Driver driverConf = new Driver();

		driverConf.setClassName(driver_name);
		driverConf.setUrl(db_url);
		driverConf.addParam(createJdoConfParam("user", username));
		driverConf.addParam(createJdoConfParam("password", password));

		return driverConf;
	}

	/**
	 * helper function to create a JDO driver configuration parameter
	 * @param name parameter name
	 * @param value parameter value
	 * @return Param object
	 */
	public static Param createJdoConfParam(String name, String value)
	{
		Param param = new Param();

		param.setName(name);
		param.setValue(value);

		return param;
	}


	/**
	 * create a JDO DataSource configuration from a JDBC DataSource instance
	 * and apply the supplied property entries
	 * @param dsClassName JDBC DataSource class name
	 * @param props properties to be used for the DataSource
	 * @return JDO Datasource configuration 
	 */
	public static DataSource createJdoDSConf(String dsClassName, Properties props)
	{
		DataSource dsConf = new DataSource();

		dsConf.setClassName(dsClassName);

		for (Enumeration e=props.keys(); e.hasMoreElements(); ) {
		    Object key = e.nextElement();
		    Object value = props.get(key);

		    dsConf.addParam(createJdoConfParam(key.toString(), value.toString()));
		}

		return dsConf;
	}

	/**
	 * create a JDO DataSource configuration from a JDBC DataSource instance
	 * @param dsClassName JDBC DataSource class name
	 * @return JDO Datasource configuration 
	 */
	public static DataSource createJdoDSConf(String dsClassName)
	{
		DataSource dsConf = new DataSource();

		dsConf.setClassName(dsClassName);

		return dsConf;
	}

	

	/**
	 * create a JDO mapping configuration
	 * @param map_url URL to retrieve mapping configuration file
	 * @return JDO Mapping configuration 
	 */
	public static Mapping createJdoMappingConf(String map_url)
	{
		Mapping mapConf = new Mapping();

		mapConf.setHref(map_url);

		return mapConf;
	}


	/**
	 * create a simple TransactionDemarcation configuration with local transaction handling
	 * @return simple TransactionDemarcation configuration with local transaction handling
	 */
	public static TransactionDemarcation createSimpleTransactionDemarcationConf()
	{
		TransactionDemarcation trans = new TransactionDemarcation();

		trans.setMode("local");
		trans.setTransactionManager(new TransactionManager());

		return trans;
	}
}
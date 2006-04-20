/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices. Redistributions must also contain a
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
 * Copyright 2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.jdo.util;

import java.util.Enumeration;
import java.util.Properties;

import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.DatabaseChoice;
import org.exolab.castor.jdo.conf.DataSource;
import org.exolab.castor.jdo.conf.Driver;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Mapping;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.conf.TransactionDemarcation;

/**
 * Factory to create JDO configurations without the need of a database configuration
 * XML file.
 * 
 * <p>This is an example for setting up a JDO configuration using JDOConfFactory:</p>
 * 
 * <code>
 *    // create driver configuration
 *    org.exolab.castor.jdo.conf.Driver driverConf =
 *        JDOConfFactory.createDriver(DRIVER, CONNECT, USERNAME, PASSWORD);
 *      
 *    // create mapping configuration
 *    org.exolab.castor.jdo.conf.Mapping mappingConf =
 *        JDOConfFactory.createMapping(getClass().getResource(MAPPING).toString());
 *
 *    // create database configuration
 *    org.exolab.castor.jdo.conf.Database dbConf =
 *        JDOConfFactory.createDatabase(DATABASE, ENGINE, driverConf, mappingConf);
 *      
 *    // create and load jdo configuration
 *    JDOManager.loadConfiguration(JDOConfFactory.createJdoConf(dbConf));
 * </code>
 *
 * @author <a href="mailto:martin-fuchs AT gmx DOT net">Martin Fuchs</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class JDOConfFactory {
    //--------------------------------------------------------------------------

    /**
     * Create a JDO configuration with local transaction demarcation and given database.
     * 
     * @param  database     Database configuration
     * @return JDO configuration
     */
    public static JdoConf createJdoConf(final Database database) {
        return createJdoConf(new Database[] {database}, createTransactionDemarcation());
    }

    /**
     * Create a JDO configuration with local transaction demarcation and given databases.
     * 
     * @param  databases   Array of database configurations
     * @return JDO configuration
     */
    public static JdoConf createJdoConf(final Database[] databases) {
        return createJdoConf(databases, createTransactionDemarcation());
    }

    /**
     * Create a JDO configuration with given database and transaction demarcation.
     * 
     * @param  database    Database configuration
     * @param  tx          TransactionDemarcation configuration 
     * @return JDO configuration
     */
    public static JdoConf createJdoConf(final Database database,
                                        final TransactionDemarcation tx) {
        
        return createJdoConf(new Database[] {database}, tx);
    }

    /**
     * Create a JDO configuration with given databases and transaction demarcation.
     * 
     * @param  databases   Array of database configurations
     * @param  tx          TransactionDemarcation configuration 
     * @return JDO configuration
     */
    public static JdoConf createJdoConf(final Database[] databases,
                                        final TransactionDemarcation tx) {
        
        JdoConf jdoConf = new JdoConf();
        jdoConf.setDatabase(databases);
        jdoConf.setTransactionDemarcation(tx);
        return jdoConf;
    }

    /**
     * Create a database configuration with given name, engine and datasource
     * configuration.
     * 
     * @param  name     Name of the database configuration
     * @param  engine   Name of the database engine
     * @param  ds       Datasource configuration
     * @param  mapping  Mapping configurations
     * @return Database configuration
     */
    public static Database createDatabase(final String name, final String engine,
                                          final DataSource ds, final Mapping mapping) {
        
        return createDatabase(name, engine, ds, new Mapping[] {mapping});
    }

    /**
     * Create a database configuration with given name, engine and datasource
     * configuration.
     * 
     * @param  name     Name of the database configuration
     * @param  engine   Name of the database engine
     * @param  ds       Datasource configuration
     * @param  mappings Array of mapping configurations
     * @return Database configuration
     */
    public static Database createDatabase(final String name, final String engine,
                                          final DataSource ds, final Mapping[] mappings) {
        
        DatabaseChoice dbChoice = new DatabaseChoice();
        dbChoice.setDataSource(ds);

        Database dbConf = createDatabase(name, engine);
        dbConf.setDatabaseChoice(dbChoice);
        dbConf.setMapping(mappings);
        return dbConf;
    }

    /**
     * Create a database configuration with given name, engine and driver
     * configuration.
     * 
     * @param  name     Name of the database configuration
     * @param  engine   Name of the database engine
     * @param  driver   Driver configuration
     * @param  mapping  Mapping configurations
     * @return Database configuration
     */
    public static Database createDatabase(final String name, final String engine,
                                          final Driver driver, final Mapping mapping) {
        
        return createDatabase(name, engine, driver, new Mapping[] {mapping});
    }

    /**
     * Create a database configuration with given name, engine and driver
     * configuration.
     * 
     * @param  name     Name of the database configuration
     * @param  engine   Name of the database engine
     * @param  driver   Driver configuration
     * @param  mappings Array of mapping configurations
     * @return Database configuration
     */
    public static Database createDatabase(final String name, final String engine,
                                          final Driver driver, final Mapping[] mappings) {
        
        DatabaseChoice dbChoise = new DatabaseChoice();
        dbChoise.setDriver(driver);

        Database dbConf = createDatabase(name, engine);
        dbConf.setDatabaseChoice(dbChoise);
        dbConf.setMapping(mappings);
        return dbConf;
    }

    /**
     * Helper to create a database configuration with given name and engine.
     * 
     * @param  name    name of the database configuration
     * @param  engine  name of the database engine
     * @return Database configuration
     */
    private static Database createDatabase(final String name, final String engine) {
        Database dbConf = new Database();
        dbConf.setName(name);
        dbConf.setEngine(engine);
        return dbConf;
    }

    /**
     * Create a JDO driver configuration from JDBC connection parameters.
     *  
     * @param  driver   JDBC driver name
     * @param  connect  JDBC connect string
     * @param  user     User name for the DB login
     * @param  password Password for the DB login
     * @return JDO driver configuration
     */
    public static Driver createDriver(final String driver, final String connect,
                                      final String user, final String password) {
        
        Driver driverConf = new Driver();
        driverConf.setClassName(driver);
        driverConf.setUrl(connect);
        driverConf.addParam(createParam("user", user));
        driverConf.addParam(createParam("password", password));
        return driverConf;
    }

    /**
     * Create a JDO datasource configuration from a JDBC DataSource instance
     * and apply the supplied property entries.
     * 
     * @param  datasource   JDBC DataSource class name
     * @param  props        Properties to be used for the DataSource
     * @return JDO Datasource configuration 
     */
    public static DataSource createDataSource(final String datasource,
                                              final Properties props) {
        
        DataSource dsConf = new DataSource();
        dsConf.setClassName(datasource);

        Enumeration e = props.keys();
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            Object value = props.get(key);

            dsConf.addParam(createParam(key.toString(), value.toString()));
        }

        return dsConf;
    }

    /**
     * Helper to create a JDO driver configuration parameter.
     * 
     * @param  name     Parameter name
     * @param  value    Parameter value
     * @return Param object
     */
    private static Param createParam(final String name, final String value) {
        Param param = new Param();
        param.setName(name);
        param.setValue(value);
        return param;
    }

    /**
     * Create a JDO mapping configuration from given URL.
     * 
     * @param  mapping  URL to retrieve mapping configuration file
     * @return JDO Mapping configuration 
     */
    public static Mapping createMapping(final String mapping) {
        Mapping mapConf = new Mapping();
        mapConf.setHref(mapping);
        return mapConf;
    }

    /**
     * Create a transaction demarcation configuration with local transaction handling.
     * 
     * @return TransactionDemarcation configuration with local transaction handling
     */
    public static TransactionDemarcation createTransactionDemarcation() {
        TransactionDemarcation trans = new TransactionDemarcation();
        trans.setMode("local");
        return trans;
    }

    //--------------------------------------------------------------------------
    
    /**
     * Default constructor for utility classes should be private. 
     */
    private JDOConfFactory() { }
    
    //--------------------------------------------------------------------------
}
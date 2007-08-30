/*
 * Copyright 2005 Martin Fuchs, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.jdo.util;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.conf.Database;
import org.castor.jdo.conf.DatabaseChoice;
import org.castor.jdo.conf.DataSource;
import org.castor.jdo.conf.Driver;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.conf.Mapping;
import org.castor.jdo.conf.Param;
import org.castor.jdo.conf.TransactionDemarcation;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Factory to create JDO configurations without the need of a database configuration
 * XML file.
 * 
 * <p>This is an example for setting up a JDO configuration using JDOConfFactory:</p>
 * 
 * <code>
 *    // create driver configuration
 *    org.castor.jdo.conf.Driver driverConf =
 *        JDOConfFactory.createDriver(DRIVER, CONNECT, USERNAME, PASSWORD);
 *      
 *    // create mapping configuration
 *    org.castor.jdo.conf.Mapping mappingConf =
 *        JDOConfFactory.createMapping(getClass().getResource(MAPPING).toString());
 *
 *    // create database configuration
 *    org.castor.jdo.conf.Database dbConf =
 *        JDOConfFactory.createDatabase(DATABASE, ENGINE, driverConf, mappingConf);
 *      
 *    // create and load jdo configuration
 *    JDOManager.loadConfiguration(JDOConfFactory.createJdoConf(dbConf));
 * </code>
 *
 * @author <a href="mailto:martin-fuchs AT gmx DOT net">Martin Fuchs</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-11-19 10:17:16 -0700 (Sat, 19 Nov 2005) $
 * @since 0.9.9.1
 */
public final class JDOConfFactory {
    //--------------------------------------------------------------------------

    /** Temporary note to check for the changed jdo-conf syntax. */
    private static final String NOTE_096 =
        "NOTE: JDO configuration syntax has changed with castor 0.9.6, "
      + "please see http://castor.codehaus.org/release-notes.html for details";
  
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(JDOConfFactory.class);
    
    /**
     * Creates a JdoConf instance from a SAX InputSource, using a Castor XML
     * Unmarshaller.
     * @param source SAX input source representing the JDO configuration.
     * @param resolver SAX entity resolver
     * @param loader Class loader
     * @return The unmarshalled JdoConf instance.
     * @throws MappingException
     */
    public static JdoConf createJdoConf(
            final InputSource source,
            final EntityResolver resolver,
            final ClassLoader loader) throws MappingException {
        
        // Load the JDO configuration file from the specified input source.
        JdoConf jdoConf = null;
        
        Unmarshaller unmarshaller = new Unmarshaller(JdoConf.class);
        try {
            unmarshaller.setEntityResolver(new DTDResolver(resolver));
            jdoConf = (JdoConf) unmarshaller.unmarshal(source);
        } catch (MarshalException e) {
            LOG.info(NOTE_096);
            throw new MappingException(e); 
        } catch (ValidationException e) {
            throw new MappingException(e);
        }

        return jdoConf;
    }
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
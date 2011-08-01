/*
 * Copyright 2010 Ralf Joachim, Clovis Wichoski
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
package org.castor.cpa.test.framework;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.engine.DatabaseRegistry;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * Abstract base class for all CPA test cases.
 * 
 * @author <a href="mailto:clovis AT supridatta DOT com DOT br">Clovis Wichoski</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class CPATestCase extends TestCase {
    //--------------------------------------------------------------------------

    /** Filename of default configuration, to use as a fall back if
     *  retrieving of configuration from System fails. */
    private static final String DEFAULT_CONFIG = "/cpactf-conf.xml";
    
    /** The name of the configuration argument. */
    private static final String ARG_CONFIG = "config";
    
    /** The name of the database argument. */
    private static final String ARG_DATABASE = "database";
    
    /** The name of the transaction argument. */
    private static final String ARG_TRANSACTION = "transaction";
    
    /** The name of the force argument. */
    private static final String ARG_FORCE = "force";
    
    //--------------------------------------------------------------------------

    /** The CPAConfigRegistry helper class. */
    private static CPAConfigRegistry _registry = null;
    
    /** The name of the database that is used. */
    private static String _database;
    
    /** The name of the transaction that is used. */
    private static String _transaction;
    
    /** Whether this test should be executed independent of exclude or not include setting. */
    private static boolean _force;
    
    /** The engine type of the database that is used. */
    private static DatabaseEngineType _engine;
    
    /** The currently used connection. */
    private static Connection _connection;
    
    /** Set that holds all tests that extend CPATestCase. */
    private static Set<String> _tests;
    
    //--------------------------------------------------------------------------
    
    /** The tests TestResult. */
    private TestResult _result = null;
    
    //--------------------------------------------------------------------------
    
    /**
     * Get the one and only static CPA properties.
     * 
     * @return One and only properties instance for Castor CPA modul.
     */
    public static AbstractProperties getProperties() {
        return CPAProperties.getInstance();
    }

    /**
     * Provide the base URL for the configuration file. 
     * 
     * @return The base URL for the configuration file. 
     */
    public static final String getJdoConfBaseURL() {
        return _registry.getJdoConfBaseURL();
    }
    
    /**
     * Provide the database configuration for the given name. 
     * 
     * @param name The name to use for the database configuration. 
     * @return The database configuration for the given name. 
     */
    public static final org.castor.jdo.conf.Database getDbConfig(final String name) {
        return _registry.createDbConfig(name, _database);
    }

    /**
     * Provide the transaction configuration. 

     * @return The transaction configuration.
     */
    public static final org.castor.jdo.conf.TransactionDemarcation getTxConfig() {
        return _registry.createTxConfig(_transaction);
    }

    /**
     * Provide a JDOManager instance for one of the databases configured in the JDOManager 
     * configuration file. 
     * 
     * @param dbConfig Database configuration. 
     * @return A JDOManager instance.
     * @throws MappingException The mapping file is invalid, or any error occurred trying 
     *         to load the JDO configuration/mapping
     */
    public static final JDOManager getJDOManager(final org.castor.jdo.conf.Database dbConfig)
    throws MappingException {
        String name = dbConfig.getName();
        if (!DatabaseRegistry.isDatabaseRegistred(name)) {
            JdoConf jdoConf = JDOConfFactory.createJdoConf(dbConfig, getTxConfig());
            JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        }
        return JDOManager.createInstance(name);
    }
    
    /**
     * 
     * @param name The name to use for the database configuration. 
     * @param mapping URL to retrieve mapping configuration file. 
     * @return A JDOManager instance.
     * @throws MappingException The mapping file is invalid, or any error occurred trying 
     *         to load the JDO configuration/mapping. 
     */
    public static final JDOManager getJDOManager(final String name, final String mapping)
    throws MappingException {
        org.castor.jdo.conf.Database dbConfig = getDbConfig(name);
        dbConfig.addMapping(JDOConfFactory.createXmlMapping(mapping));
        return getJDOManager(dbConfig);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Default Constructor. 
     */
    public CPATestCase() {
        super();
        initialize();
    }
    
    /**
     * Construct CPATestCase with given name.
     * 
     * @param name Name of the test.
     */
    public CPATestCase(final String name) {
        super(name);
        initialize();
    }
    
    /**
     * Initialize database, transaction, connection, ... .
     */
    private void initialize() {
        if (_registry == null) {
            _registry = new CPAConfigRegistry();
            
            String config = System.getProperty(ARG_CONFIG);
            if ((config == null) || config.trim().equals("")) {
                config = CPATestCase.class.getResource(DEFAULT_CONFIG).toExternalForm();
            }
            _registry.loadConfiguration(config);

            _database = System.getProperty(ARG_DATABASE);
            if ((_database == null) || _database.trim().equals("")) {
                _database = _registry.getDefaultDatabaseName();
            }

            _transaction = System.getProperty(ARG_TRANSACTION);
            if ((_transaction == null) || _transaction.trim().equals("")) {
                _transaction = _registry.getDefaultTransactionName();
            }

            _force = Boolean.getBoolean(ARG_FORCE);
            
            _engine = _registry.getEngine(_database);

            try {
                if (!DatabaseRegistry.isDatabaseRegistred("default")) {
                    JdoConf jdoConf = JDOConfFactory.createJdoConf(
                            getDbConfig("default"), getTxConfig());
                    String baseURL = _registry.getJdoConfBaseURL();
                    JDOManager.loadConfiguration(jdoConf, baseURL);
                }
                JDOManager jdo = JDOManager.createInstance("default");
                _connection = jdo.getConnectionFactory().createConnection();
            } catch (Exception ex) {
                throw new IllegalStateException();
            }

            _tests = new HashSet<String>();
        }
        
        String test = getClass().getName();
        test = test.substring(0, test.lastIndexOf('.'));
        if (!_tests.contains(test)) {
            if ((include(_engine) && !exclude(_engine)) || _force) {
                CPAScriptExecutor.execute(_engine, _connection, test);

                _tests.add(test);
            }
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * Is given database engine included in current test? 
     * Database types that support the tested functionality should be included. 
     * 
     * @param engine The database engine that is to test. 
     * @return true, if given database engine is included, false if not. 
     */
    public boolean include(final DatabaseEngineType engine) { return true; }
    
    /**
     * Is given database engine excluded from current test? 
     * Database types that don't support the tested functionality should be excluded. 
     * 
     * @param engine The database engine that is to test.
     * @return true, if given database engine is excluded, false if not. 
     */
    public boolean exclude(final DatabaseEngineType engine) { return false; }
    
    /**
     * Provide whether current database engine is included and not excluded
     * in test or force flag is set. 
     * 
     * @return true if test is included or force flag is set, false otherwise. 
     */
    private boolean canRun() {
        return (include(_engine) && !exclude(_engine)) || _force;
    }

    /**
     * Override run so we can check if test case has to be executed for the database engine
     * under test and remember test result to be able to add results ourself during execution
     * of threaded tests.
     * <br/>
     * {@inheritDoc}
     */
    public final void run(final TestResult result) {
        if (canRun()) {
           _result = result;
           super.run(result);
           _result = null;
        }
    }
    
    /**
     * Handle an exception. Since multiple threads won't have their exceptions caught by the
     * test case they must be handled manually. To do so exceptions in threads have to be
     * catched and passed to this method.
     * 
     * @param t Exception to handle.
     */
    protected final  void handleException(final Throwable t) {
       synchronized (_result) {
          if (t instanceof AssertionFailedError) {
             _result.addFailure(this, (AssertionFailedError) t);
          } else {
             _result.addError(this, t);
          }
       }
    }

    //--------------------------------------------------------------------------
}

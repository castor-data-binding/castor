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
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public abstract class CPATestCase extends TestCase {
    //--------------------------------------------------------------------------

    private static final String DEFAULT_CONFIG = "/cpactf-conf.xml";
    
    private static final String ARG_CONFIG = "config";
    
    private static final String ARG_DATABASE = "database";
    
    private static final String ARG_TRANSACTION = "transaction";
    
    private static final String ARG_FORCE = "force";
    
    //--------------------------------------------------------------------------

    private static CPAConfigRegistry _registry = null;
    
    private static String _database;
    
    private static String _transaction;
    
    private static boolean _force;
    
    private static DatabaseEngineType _engine;
    
    private static Connection _connection;
    
    private static Set<String> _tests;
    
    //--------------------------------------------------------------------------
    
    /** The tests TestResult. */
    private TestResult _result = null;
    
    //--------------------------------------------------------------------------
    
    public static AbstractProperties getProperties() {
        return CPAProperties.getInstance();
    }

    public static final String getJdoConfBaseURL() {
        return _registry.getJdoConfBaseURL();
    }
    
    public static final JdoConf getJdoConf(final String name) {
        return _registry.createJdoConf(name, _database, _transaction);
    }
    
    public static final JdoConf getJdoConf(final String name, final String mapping) {
        return getJdoConf(name, new String[] {mapping});
    }
    
    public static final JdoConf getJdoConf(final String name, final String[] mappings) {
        return _registry.createJdoConf(name, _database, _transaction, mappings);
    }
    
    public static final JDOManager getJDOManager(final String name)
    throws MappingException {
        if (!DatabaseRegistry.isDatabaseRegistred(name)) {
            JdoConf jdoConf = _registry.createJdoConf(name, _database, _transaction);
            String baseURL = _registry.getJdoConfBaseURL();
            JDOManager.loadConfiguration(jdoConf, baseURL);
        }

        return JDOManager.createInstance(name);
    }
    
    public static final JDOManager getJDOManager(final String name, final String mapping)
    throws MappingException {
        return getJDOManager(name, new String[] {mapping});
    }
    
    public static final JDOManager getJDOManager(final String name, final String[] mappings)
    throws MappingException {
        if (!DatabaseRegistry.isDatabaseRegistred(name)) {
            JdoConf jdoConf = _registry.createJdoConf(name, _database, _transaction, mappings);
            String baseURL = _registry.getJdoConfBaseURL();
            JDOManager.loadConfiguration(jdoConf, baseURL);
        }

        return JDOManager.createInstance(name);
    }
    
    //--------------------------------------------------------------------------

    public CPATestCase() {
        super();
        initialize();
    }
    
    public CPATestCase(final String name) {
        super(name);
        initialize();
    }
    
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
                _connection = getJDOManager("default").getConnectionFactory().createConnection();
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

    public boolean include(final DatabaseEngineType engine) { return true; }
    
    public boolean exclude(final DatabaseEngineType engine) { return false; }
    
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

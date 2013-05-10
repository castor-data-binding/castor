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

import junit.framework.TestCase;
import junit.framework.TestResult;

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
    
    /**
     * {@inheritDoc}
     */
    public final void run(final TestResult result) {
        if ((include(_engine) && !exclude(_engine)) || _force) {
            super.run(result);
        }
    }

    //--------------------------------------------------------------------------
}

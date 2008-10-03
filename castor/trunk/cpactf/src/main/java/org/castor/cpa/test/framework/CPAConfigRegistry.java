package org.castor.cpa.test.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.castor.cpa.test.framework.xml.Configuration;
import org.castor.cpa.test.framework.xml.CpactfConf;
import org.castor.cpa.test.framework.xml.DataSource;
import org.castor.cpa.test.framework.xml.Database;
import org.castor.cpa.test.framework.xml.DatabaseChoice;
import org.castor.cpa.test.framework.xml.Driver;
import org.castor.cpa.test.framework.xml.Jndi;
import org.castor.cpa.test.framework.xml.Manager;
import org.castor.cpa.test.framework.xml.Mapping;
import org.castor.cpa.test.framework.xml.Param;
import org.castor.cpa.test.framework.xml.Transaction;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.cpa.test.framework.xml.types.TransactionModeType;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

public final class CPAConfigRegistry {
    //--------------------------------------------------------------------------

    private String _jdoConfBaseURL;
    
    private String _defaultDatabaseName;
    
    private String _defaultTransactionName;
    
    private final Map < String, Configuration > _configurations
        = new HashMap < String, Configuration > ();
    
    private final Map < String, Database > _databases
        = new HashMap < String, Database > ();
    
    private final Map < String, Transaction > _transactions
        = new HashMap < String, Transaction > ();
    
    //--------------------------------------------------------------------------

    public void loadConfiguration(final String url) {
        InputSource source = new InputSource(url);
        _jdoConfBaseURL = source.getSystemId();
        
        CpactfConf cpactfconf = null;
        try {
            Unmarshaller unmarshaller = new Unmarshaller(CpactfConf.class);
            unmarshaller.setEntityResolver(new DTDResolver(null));
            cpactfconf = (CpactfConf) unmarshaller.unmarshal(source);
        } catch (MarshalException e) {
            throw new CPAConfigException(e); 
        } catch (ValidationException e) {
            throw new CPAConfigException(e);
        }

        _defaultDatabaseName = cpactfconf.getDefaultDatabase();
        _defaultTransactionName = cpactfconf.getDefaultTransaction();
        
        Iterator cfgIter = cpactfconf.iterateConfiguration();
        while (cfgIter.hasNext()) {
            Configuration config = (Configuration) cfgIter.next();
            _configurations.put(config.getName(), config);
        }
        
        Iterator dbIter = cpactfconf.iterateDatabase();
        while (dbIter.hasNext()) {
            Database database = (Database) dbIter.next();
            _databases.put(database.getName(), database);
        }
        
        Iterator transIter = cpactfconf.iterateTransaction();
        while (transIter.hasNext()) {
            Transaction trans = (Transaction) transIter.next();
            _transactions.put(trans.getName(), trans);
        }
    }
    
    //--------------------------------------------------------------------------

    public String getJdoConfBaseURL() { return _jdoConfBaseURL; }
    
    public String getDefaultDatabaseName() { return _defaultDatabaseName; }
    
    public String getDefaultTransactionName() { return _defaultTransactionName; }
    
    public DatabaseEngineType getEngine(final String db) {
        if (!_databases.containsKey(db)) {
            throw new CPAConfigException("Database config '" + db + "' not found.");
        }

        Database database = _databases.get(db);
        
        if (database.getEngine() == null) {
            throw new CPAConfigException("No engine specified "
                    + "in database config '" + db + "'.");
        }
        
        return database.getEngine();
    }
    
    public JdoConf createJdoConf(
            final String cfg, final String db, final String tx) {
        if (cfg == null) {
            throw new CPAConfigException("No configuration name specified.");
        } else if (!_configurations.containsKey(cfg)) {
            throw new CPAConfigException("Mapping config '" + cfg + "' not found.");
        } else if (!_databases.containsKey(db)) {
            throw new CPAConfigException("Database config '" + db + "' not found.");
        } else if (!_transactions.containsKey(tx)) {
            throw new CPAConfigException("Transaction config '" + tx + "' not found.");
        }

        return JDOConfFactory.createJdoConf(
                createDatabase(cfg, db, createMappings(cfg)),
                createTransactionDemarcation(tx));
    }
    
    public org.castor.jdo.conf.JdoConf createJdoConf(
            final String cfg, final String db, final String tx, final String[] mappings) {
        if (cfg == null) {
            throw new CPAConfigException("No configuration name specified.");
        } else if (!_databases.containsKey(db)) {
            throw new CPAConfigException("Database config '" + db + "' not found.");
        } else if (!_transactions.containsKey(tx)) {
            throw new CPAConfigException("Transaction config '" + tx + "' not found.");
        }

        return JDOConfFactory.createJdoConf(
                createDatabase(cfg, db, mappings),
                createTransactionDemarcation(tx));
    }
    
    private String[] createMappings(final String cfg) {
        Configuration config = _configurations.get(cfg);
        
        int count = config.getMappingCount();
        String[] mappings = new String[count];
        for (int i = 0; i < count; i++) {
            Mapping mapping = config.getMapping(i);
            if (mapping.getHref() == null) {
                throw new CPAConfigException("No mapping URL specified "
                        + "in mapping config '" + cfg + "'.");
            }
            mappings[i] = mapping.getHref();
        }
        return mappings;
    }
    
    private org.castor.jdo.conf.Database createDatabase(
            final String name, final String db, final String[] mappings) {
        Database database = _databases.get(db);
        
        if (database.getEngine() == null) {
            throw new CPAConfigException("No engine specified "
                    + "in database config '" + db + "'.");
        }
        String engine = database.getEngine().toString();
        
        DatabaseChoice choice = database.getDatabaseChoice();
        if (choice == null) {
            throw new CPAConfigException("Neither driver, datasource nor jndi specified "
                    + "in database config '" + db + "'.");
        }
        
        if (choice.getDriver() != null) {
            return JDOConfFactory.createDatabase(name, engine, 
                    createDriver(db, choice.getDriver()), mappings);
        } else if (choice.getDataSource() != null) {
            return JDOConfFactory.createDatabase(name, engine, 
                    createDataSource(db, choice.getDataSource()), mappings);
        } else if (choice.getJndi() != null) {
            return JDOConfFactory.createDatabase(name, engine, 
                    createJNDI(db, choice.getJndi()), mappings);
        } else {
            throw new CPAConfigException("Neither driver, datasource nor jndi specified "
                    + "in database config '" + db + "'.");
        }
    }
    
    private org.castor.jdo.conf.Driver createDriver(
            final String db, final Driver driver) {
        String classname = driver.getClassName();
        if (classname == null) {
            throw new CPAConfigException("No classname specified for driver "
                    + "in database config '" + db + "'.");
        }

        String connect = driver.getUrl();
        if (connect == null) {
            throw new CPAConfigException("No connect string specified for driver "
                    + "in database config '" + db + "'.");
        }

        String user = null;
        String password = null;
        for (int i = 0; i < driver.getParamCount(); i++) {
            Param param = driver.getParam(i);
            if ("user".equalsIgnoreCase(param.getName())) { user = param.getValue(); }
            if ("password".equalsIgnoreCase(param.getName())) { password = param.getValue(); }
        }
        if (user == null) {
            throw new CPAConfigException("Parameter 'user' is missing for driver "
                    + "in database config '" + db + "'.");
        }
        if (password == null) {
            throw new CPAConfigException("Parameter 'password' is missing for driver "
                    + "in database config '" + db + "'.");
        }
        
        return JDOConfFactory.createDriver(classname, connect, user, password);
    }
    
    private org.castor.jdo.conf.DataSource createDataSource(
            final String db, final DataSource datasource) {
        String classname = datasource.getClassName();
        if (classname == null) {
            throw new CPAConfigException("No classname specified for datasource "
                    + "in database config '" + db + "'.");
        }

        Properties props = new Properties();
        for (int i = 0; i < datasource.getParamCount(); i++) {
            Param param = datasource.getParam(i);
            props.put(param.getName(), param.getValue());
        }
        
        return JDOConfFactory.createDataSource(classname, props);
    }
    
    private org.castor.jdo.conf.Jndi createJNDI(
            final String db, final Jndi jndi) {
        String name = jndi.getName();
        if (name == null) {
            throw new CPAConfigException("No JNDI name specified "
                    + "in database config '" + db + "'.");
        }
        
        return JDOConfFactory.createJNDI(name);
    }
    
    private org.castor.jdo.conf.TransactionDemarcation createTransactionDemarcation(
            final String tx) {
        Transaction trans = _transactions.get(tx);
        
        if (trans.getMode() == TransactionModeType.LOCAL) {
            return JDOConfFactory.createLocalTransactionDemarcation();
        } else if (trans.getMode() == TransactionModeType.GLOBAL) {
            Manager manager = trans.getManager();
            if (manager == null) {
                throw new CPAConfigException("No manager definition found "
                        + "in global transaction config '" + tx + "'.");
            }
            
            String name = manager.getName();
            if (name == null) {
                throw new CPAConfigException("No manager name specified "
                        + "in global transaction config '" + tx + "'.");
            }
            
            Properties props = new Properties();
            for (int i = 0; i < manager.getParamCount(); i++) {
                Param param = manager.getParam(i);
                props.put(param.getName(), param.getValue());
            }
            
            return JDOConfFactory.createGlobalTransactionDemarcation(name, props);
        } else {
            throw new CPAConfigException("Neither local nor global mode specified "
                    + "in transaction config '" + tx + "'.");
        }
    }

    //--------------------------------------------------------------------------
}

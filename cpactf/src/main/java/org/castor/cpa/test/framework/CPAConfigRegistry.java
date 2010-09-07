/*
 * Copyright 2010 Ralf Joachim
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.castor.cpa.test.framework.xml.CpactfConf;
import org.castor.cpa.test.framework.xml.DataSource;
import org.castor.cpa.test.framework.xml.Database;
import org.castor.cpa.test.framework.xml.DatabaseChoice;
import org.castor.cpa.test.framework.xml.Driver;
import org.castor.cpa.test.framework.xml.Jndi;
import org.castor.cpa.test.framework.xml.Manager;
import org.castor.cpa.test.framework.xml.Param;
import org.castor.cpa.test.framework.xml.Transaction;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.cpa.test.framework.xml.types.TransactionModeType;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

/**
 * Registry for CPA configuration.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class CPAConfigRegistry {
    //--------------------------------------------------------------------------

    private String _jdoConfBaseURL;
    
    private String _defaultDatabaseName;
    
    private String _defaultTransactionName;
    
    private final Map<String, Database> _databases
        = new HashMap<String, Database>();
    
    private final Map<String, Transaction> _transactions
        = new HashMap<String, Transaction>();
    
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
        
        Iterator<? extends Database> dbIter = cpactfconf.iterateDatabase();
        while (dbIter.hasNext()) {
            Database database = dbIter.next();
            _databases.put(database.getName(), database);
        }
        
        Iterator<? extends Transaction> transIter = cpactfconf.iterateTransaction();
        while (transIter.hasNext()) {
            Transaction trans = transIter.next();
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
    
    public org.castor.jdo.conf.Database createDbConfig(final String name, final String db) {
        if (name == null) {
            throw new CPAConfigException("No configuration name specified.");
        } else if (!_databases.containsKey(db)) {
            throw new CPAConfigException("Database config '" + db + "' not found.");
        }

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
        
        org.castor.jdo.conf.Database dbConfig;
        if (choice.getDriver() != null) {
            dbConfig = JDOConfFactory.createDatabase(name, engine, 
                    createDriver(db, choice.getDriver()));
        } else if (choice.getDataSource() != null) {
            dbConfig = JDOConfFactory.createDatabase(name, engine, 
                    createDataSource(db, choice.getDataSource()));
        } else if (choice.getJndi() != null) {
            dbConfig = JDOConfFactory.createDatabase(name, engine, 
                    createJNDI(db, choice.getJndi()));
        } else {
            throw new CPAConfigException("Neither driver, datasource nor jndi specified "
                    + "in database config '" + db + "'.");
        }
        return dbConfig;
    }
    
    private org.castor.jdo.conf.Driver createDriver(final String db, final Driver driver) {
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
    
    private org.castor.jdo.conf.DataSource createDataSource(final String db, final DataSource ds) {
        String classname = ds.getClassName();
        if (classname == null) {
            throw new CPAConfigException("No classname specified for datasource "
                    + "in database config '" + db + "'.");
        }

        Properties props = new Properties();
        for (int i = 0; i < ds.getParamCount(); i++) {
            Param param = ds.getParam(i);
            props.put(param.getName(), param.getValue());
        }
        
        return JDOConfFactory.createDataSource(classname, props);
    }
    
    private org.castor.jdo.conf.Jndi createJNDI(final String db, final Jndi jndi) {
        String name = jndi.getName();
        if (name == null) {
            throw new CPAConfigException("No JNDI name specified "
                    + "in database config '" + db + "'.");
        }
        
        return JDOConfFactory.createJNDI(name);
    }
    
    public org.castor.jdo.conf.TransactionDemarcation createTxConfig(final String tx) {
        if (!_transactions.containsKey(tx)) {
            throw new CPAConfigException("Transaction config '" + tx + "' not found.");
        }

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

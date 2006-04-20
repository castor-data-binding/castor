/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
package org.castor.jdo.engine;

import java.util.Enumeration;

import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.transactionmanager.TransactionManagerRegistry;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.PersistenceEngineFactory;
import org.exolab.castor.persist.PersistenceFactoryRegistry;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.Messages;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public abstract class AbstractConnectionFactory implements ConnectionFactory {
    //--------------------------------------------------------------------------

    /** The name of the generic SQL engine, if no SQL engine specified. */
    public static final String GENERIC_ENGINE = "generic";

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(AbstractConnectionFactory.class);

    //--------------------------------------------------------------------------
    
    /** Has the factory been initialized? */
    private boolean             _initialized = false;
    
    /** The jdo configuartion. */
    private JdoConf             _jdoConf;
    
    /** Index of the database configuration in the jdo configuration. */
    private int                 _index;
    
    /** The name of the database configuration. */
    private String              _name;
    
    /** The mapping to load. */
    private Mapping             _mapping;
    
    /** The transaction manager. */
    private TransactionManager  _txManager;
    
    /** The LockEngine only available after initialization. */
    private LockEngine          _engine = null;
    
    //--------------------------------------------------------------------------

    /**
     * Constructs a new AbstractConnectionFactory with given name, engine and mapping.
     * Factory will be ready to use without calling initialize first.
     * 
     * @param name      The Name of the database configuration.
     * @param engine    The Name of the persistence factory to use.
     * @param txManager The transaction manager to use.
     * @param mapping   The previously loaded mapping.
     * @throws MappingException If LockEngine could not be initialized.
     */
    protected AbstractConnectionFactory(final String name, final String engine,
                                        final Mapping mapping,
                                        final TransactionManager txManager)
    throws MappingException {
        _jdoConf = null;
        _index = -1;
        _name = name;
        _mapping = mapping;
        _txManager = txManager;
        
        initializeEngine(engine);
        _initialized = true;
    }
    
    /**
     * Constructs a new AbstractConnectionFactory with given database and mapping.
     * Initialize needs to be called before using the factory to create connections.
     * 
     * @param jdoConf   The jdo configuartion.
     * @param index     Index of the database configuration in the jdo configuration.
     * @param mapping   The mapping to load.
     */
    protected AbstractConnectionFactory(final JdoConf jdoConf, final int index,
                                        final Mapping mapping) {
        _jdoConf = jdoConf;
        _index = index;
        _name = jdoConf.getDatabase(index).getName();
        _mapping = mapping;
        _txManager = null;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Initialize factory if it had not been initialized before.
     * 
     * @throws MappingException If concrete factory or LockEngine fail to initialize
     *                          or mapping could not be loaded. 
     */
    public final void initialize() throws MappingException {
        // If the factory was already initialized, ignore
        // this request to initialize it.
        if (!_initialized) {
            initializeMapping();
            _txManager = TransactionManagerRegistry.getTransactionManager(_jdoConf);
            initializeEngine(_jdoConf.getDatabase(_index).getEngine());
            initializeFactory();
            
            _initialized = true;
        }
    }
    
    /**
     * Load mapping.
     * 
     * @throws MappingException If mapping could not be loaded.
     */
    private void initializeMapping() throws MappingException {
        try {
            // Initialize all the mappings of the database.
            Enumeration mappings = _jdoConf.getDatabase(_index).enumerateMapping();
            org.exolab.castor.jdo.conf.Mapping mapConf;
            while (mappings.hasMoreElements()) {
                mapConf = (org.exolab.castor.jdo.conf.Mapping) mappings.nextElement();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading the mapping descriptor: " + mapConf.getHref());
                }
                
                if (mapConf.getHref() != null) {
                    _mapping.loadMapping(mapConf.getHref());
                }
            }
        } catch (MappingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new MappingException(ex);
        }
    }
    
    /**
     * Initialize LockEngine.
     * 
     * @param engine    Name of the persistence factory to use.
     * @throws MappingException If LockEngine could not be initialized.
     */
    private void initializeEngine(final String engine) throws MappingException {
        // Complain if no database engine was specified, otherwise get
        // a persistence factory for that database engine.
        PersistenceFactory factory;
        if (engine == null) {
            factory = PersistenceFactoryRegistry.getPersistenceFactory(GENERIC_ENGINE);
        } else {
            factory = PersistenceFactoryRegistry.getPersistenceFactory(engine);
        }
        
        if (factory == null) {
            String msg = Messages.format("jdo.noSuchEngine", engine);
            LOG.error(msg);
            throw new MappingException(msg);
        }
        
        _engine = new PersistenceEngineFactory().createEngine(
                this, _mapping.getResolver(Mapping.JDO, factory), factory);
    }
    
    /**
     * Initialize the concrete factory.
     * 
     * @throws MappingException If concrete factory could not be initialized.
     */
    protected abstract void initializeFactory() throws MappingException;
    
    //--------------------------------------------------------------------------
    
    /**
     * Get the name of the database configuration.
     * 
     * @return The name of the database configuration.
     */
    public final String getName() { return _name; }
    
    /**
     * Get the database configuration.
     * 
     * @return The database configuration.
     */
    public final Database getDatabase() { return _jdoConf.getDatabase(_index); }

    /**
     * Get the mapping to load.
     * 
     * @return The mapping to load.
     */
    public final Mapping getMapping() { return _mapping; }
    
    /**
     * Get the transaction manager.
     * 
     * @return The transaction manager.
     */
    public final TransactionManager getTransactionManager() { return _txManager; }
    
    /**
     * Get the LockEngine only available after initialization.
     * 
     * @return The LockEngine.
     */
    public final LockEngine getEngine() { return _engine; }

    //--------------------------------------------------------------------------
}

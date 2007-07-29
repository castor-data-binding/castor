/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.jdo.engine;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.LocalTransactionContext;
import org.castor.util.Messages;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;

/**
 * An implementation of the JDO {@link Database} interface supporting explicit local 
 * transaction demarcation.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public class LocalDatabaseImpl extends AbstractDatabaseImpl {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static Log _log = LogFactory.getFactory().getInstance(LocalDatabaseImpl.class);

    /**
     * Creates an instance of this class.
     * @param dbName database name
     * @param lockTimeout Lock timeout
     * @param callback Callback interceptor
     * @param instanceFactory Instance factory.
     * @param classLoader Current class loader
     * @param autoStore Indicates whetehr to use 'auto-storing'
     * @throws DatabaseNotFoundException If the specified database configuration cannot be found.
     */
    public LocalDatabaseImpl(final String dbName, 
            final int lockTimeout, 
            final CallbackInterceptor callback,
            final InstanceFactory instanceFactory,  
            final ClassLoader classLoader, 
            final boolean autoStore)
    throws DatabaseNotFoundException {
        
        super(dbName, lockTimeout, callback, instanceFactory, classLoader, autoStore);
        
        _ctx = new LocalTransactionContext(this);
        
        _ctx.setLockTimeout(_lockTimeout);
        _ctx.setAutoStore(_autoStore);
        _ctx.setCallback(_callback);
        _ctx.setInstanceFactory(_instanceFactory);
        _classLoader = classLoader;
        
        loadSynchronizables();
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Database#close()
     */
    public synchronized void close() throws PersistenceException {
        try {
            if (isActive()) {
                try {
                    _ctx.rollback();
                } catch (Exception except) {
                }
                
                try {
                    _ctx.close();
                } catch (Exception except) {
                }
                
                throw new PersistenceException(Messages.message("jdo.dbClosedTxRolledback"));
            }
        } finally {
            _scope = null;
        }
    }

    /**
     * Overrides Object.finalize().
     * 
     * Outputs a warning message to the logs if the current DatabaseImpl 
     * instance still has valid scope. In this condition - a condition that 
     * ideally should not occur at all - we close the instance as well to 
     * free up resources.
     * 
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        if (_scope != null || !isActive()) { return; }
            
        // retrieve SQL bound to this Database instance
        OQLQuery oqlQuery = getOQLQuery(); 
        String sql = ((OQLQueryImpl) oqlQuery).getSQL(); 
        
        _log.warn(Messages.format("jdo.finalize_close", this.toString(), _dbName, sql));

        close();
    }

    /**
     * @inheritDoc
     */
    public void begin() throws PersistenceException {
        _log.debug("Beginning tx");

        if (isActive()) {
            throw new PersistenceException(Messages.message("jdo.txInProgress"));
        }

        // _ctx.setStatus(Status.STATUS_ACTIVE);
        _ctx.setStatus(0);
        
        _ctx.setLockTimeout(_lockTimeout);
        _ctx.setAutoStore(_autoStore);
        _ctx.setCallback(_callback);
        _ctx.setInstanceFactory(_instanceFactory);

        registerSynchronizables();
    }

    /**
     * @inheritDoc
     */
    public void commit() throws TransactionNotInProgressException, TransactionAbortedException {
        _log.debug("Committing tx");

        if (!isActive()) {
            throw new TransactionNotInProgressException(Messages.message("jdo.txNotInProgress"));
        }
        
        // if ( _ctx.getStatus() == Status.STATUS_MARKED_ROLLBACK )
        if (_ctx.getStatus() == 1) { 
            throw new TransactionAbortedException(Messages.message("jdo.txRollback"));
        }
        try {
            _ctx.prepare();
            _ctx.commit();
        } catch (TransactionAbortedException except) {
            _log.error(Messages.format("jdo.txAborted", except.getMessage()), except);
            _ctx.rollback();
            throw except;
        } finally {
            try {
                // TODO [SMH]: Temporary fix, see bug 1491/CASTOR-630.
                if (_ctx.isOpen()) {
                    _ctx.close();
                }
           } catch (Exception e) {
               _log.info(e.getMessage(), e);
           }
        }

        unregisterSynchronizables();
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Database#rollback()
     */
    public void rollback() throws TransactionNotInProgressException {
        _log.debug("Rolling back tx");

        if (!isActive()) {
            throw new TransactionNotInProgressException(Messages.message("jdo.txNotInProgress"));
        }
        _ctx.rollback();

        unregisterSynchronizables();
    }

    /**
     * @inheritDoc
     * @see org.exolab.castor.jdo.Database#getJdbcConnection()
     */
    public Connection getJdbcConnection() throws PersistenceException {
        if (_ctx == null || !_ctx.isOpen()) {
            String message = Messages.message("jdo.dbTxNotInProgress.jdbc");
            throw new PersistenceException (message);
        }
        return _ctx.getConnection(_scope.getLockEngine());
    }
}  
                                

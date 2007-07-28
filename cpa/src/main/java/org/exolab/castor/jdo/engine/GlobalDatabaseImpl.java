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

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.GlobalTransactionContext;
import org.castor.util.Messages;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;

/**
 * An implementation of the JDO database supporting explicit transaction
 * demarcation.
 *
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public class GlobalDatabaseImpl extends AbstractDatabaseImpl implements Synchronization {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(GlobalDatabaseImpl.class);

    /**
     * The XA transaction to which this Database instance is attached
     */
    private Transaction _transaction;

    /**
     * The transaction to database map for database pooling.
     */
    private TxDatabaseMap _txMap;
    
    /**
     * Flag to indicate whether Database instances should be cached on a per transaction base.
     */
    boolean _isPoolInUseForGlobalTransactions = false;

    /**
     * Creates an instance of this class.
     * @param dbName Database name
     * @param lockTimeout Lock timeout.
     * @param callback Callback interceptors.
     * @param instanceFactory Instance factory to use.
     * @param transaction Current XA transaction.
     * @param classLoader Current class loader.
     * @param autoStore True if auto-storing is enabled.
     * @param isPoolInUseForGlobalTransactions True if Database instanced should be cached.
     * @throws DatabaseNotFoundException If the specified database cannot be found. 
     */
    public GlobalDatabaseImpl( String dbName, int lockTimeout, CallbackInterceptor callback,
                         InstanceFactory instanceFactory, Transaction transaction, 
                         ClassLoader classLoader, boolean autoStore, 
                         boolean isPoolInUseForGlobalTransactions)
    throws DatabaseNotFoundException {
        
        super (dbName, lockTimeout, callback, instanceFactory, classLoader, autoStore);
        
        _isPoolInUseForGlobalTransactions = isPoolInUseForGlobalTransactions;
        _transaction = transaction;
	
        try {
            _ctx = new GlobalTransactionContext(this);
            _ctx.setStatus(transaction.getStatus());
        } catch (javax.transaction.SystemException se) {
            throw new DatabaseNotFoundException(se);
        }
        
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
            try {
                _ctx.close();
            } catch(Exception e) {
                throw new PersistenceException(e.getMessage(), e);
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
            
        if (!_isPoolInUseForGlobalTransactions) {
            // retrieve SQL bound to this Database instance
            OQLQuery oqlQuery = getOQLQuery(); 
            String sql = ((OQLQueryImpl) oqlQuery).getSQL(); 
            
            _log.warn(Messages.format("jdo.finalize_close", this.toString(), _dbName, sql));
        }
        
        close();
    }

    /**
     * @inheritDoc
     */
    public void begin() throws PersistenceException {
        throw new IllegalStateException(Messages.message("jdo.txInJ2EE"));
    }

    /**
     * @inheritDoc
     */
    public void commit() throws TransactionNotInProgressException, TransactionAbortedException {
        throw new IllegalStateException(Messages.message("jdo.txInJ2EE"));
    }

    /**
     * @inheritDoc
     */
    public void rollback() throws TransactionNotInProgressException {
        throw new IllegalStateException(Messages.message("jdo.txInJ2EE"));
    }

    /**
     * @inheritDoc
     */
    public void beforeCompletion() {
        // XXX [SMH]: Find another test for txNotInProgress
        if (_transaction == null || _ctx == null || ! _ctx.isOpen()) {
            throw new IllegalStateException(Messages.message("jdo.txNotInProgress"));
        }
        if (_ctx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
            try {
                _transaction.setRollbackOnly();
            } catch (SystemException except) {
                _log.warn( Messages.format("jdo.warnException", except));
            }
            return;
        }
        try {
            _ctx.prepare();
        } catch (TransactionAbortedException tae) {
            _log.error(Messages.format("jdo.txAbortedMarkRollback", tae.getMessage()), tae);        
            try {
                _transaction.setRollbackOnly();
            } catch (SystemException se) {
                _log.fatal(Messages.format("jdo.txMarkRollbackFailure", se.getMessage()), se);
            }
            _ctx.rollback();
        }
    }

    /**
     * @inheritDoc
     * @see javax.transaction.Synchronization#afterCompletion(int)
     */
    public void afterCompletion(final int status) {
        try {
            // XXX [SMH]: Find another test for txNotInProgress
            if (_transaction == null || _ctx == null) {
                throw new IllegalStateException(Messages.message("jdo.txNotInProgress"));
            }
            if (_ctx.getStatus() == Status.STATUS_ROLLEDBACK) {
                return;
            }
            if (_ctx.getStatus() != Status.STATUS_PREPARED && status != Status.STATUS_ROLLEDBACK) {
                throw new IllegalStateException( "Unexpected state: afterCompletion called at status " + _ctx.getStatus() );
            }
            switch (status) {
            case Status.STATUS_COMMITTED:
                try {
                    _ctx.commit();
                } catch (TransactionAbortedException except) {
                    _log.fatal(Messages.format("jdo.fatalException", except));
                    _ctx.rollback();
                }
                return;
            case Status.STATUS_ROLLEDBACK:
                _ctx.rollback();
                return;
            default:
                _ctx.rollback();
                throw new IllegalStateException("Unexpected state: afterCompletion called with status " + status);
            }
        } finally {
            if (_txMap != null && _transaction != null) {
                _txMap.remove(_transaction);
                _txMap = null;
            }
        }
    }

    void setTxMap(TxDatabaseMap txMap) {
        _txMap = txMap;
    }

    /**
     * @inheritDoc
     */
    public Connection getJdbcConnection() throws PersistenceException {
        return _ctx.getConnection(_scope.getLockEngine());
    }
}  
                                

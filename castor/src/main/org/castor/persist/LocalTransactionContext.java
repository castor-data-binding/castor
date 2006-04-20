/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.persist;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.util.Messages;

/**
 * A transaction context is required in order to perform operations
 * against the database. The transaction context is mapped to {@link
 * javax.transaction.Transaction} for the ODMG API and into
 * {@link javax.transaction.xa.XAResource} for XA databases. The only
 * way to begin a new transaction is through the creation of a new
 * transaction context. All database access must be performed through
 * a transaction context.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class LocalTransactionContext extends AbstractTransactionContext {
    /** Log instance used for outputting debug statements. */
    private static final Log LOG = LogFactory.getLog(LocalTransactionContext.class);

    /**
     * Create a new transaction context.
     * 
     * @param db Database instance
     */
    public LocalTransactionContext(final Database db) {
        super(db);
    }

    /**
     * @see org.castor.persist.AbstractTransactionContext
     *      #createConnection(org.exolab.castor.persist.LockEngine)
     */
    protected Connection createConnection(final LockEngine engine)
    throws PersistenceException {
        // Get a new connection from the engine. Since the engine has no
        // transaction association, we must do this sort of round trip. An attempt
        // to have the transaction association in the engine inflates the code size
        // in other places.
        try {
            Connection conn = engine.getConnectionFactory().createConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException ex) {
            throw new PersistenceException(Messages.format("persist.nested", ex), ex);
        }
    }

    /**
     * @see org.castor.persist.AbstractTransactionContext#commitConnections()
     */
    protected void commitConnections() throws TransactionAbortedException {
        try {
            // Go through all the connections opened in this transaction, commit and
            // close them one by one.
            Iterator iter = connectionsIterator();
            while (iter.hasNext()) {
                // Checkpoint can only be done if transaction is not running under
                // transaction monitor
                ((Connection) iter.next()).commit();
            }
        } catch (SQLException ex) {
            throw new TransactionAbortedException(
                    Messages.format("persist.nested", ex), ex);
        } finally {
            Iterator iter = connectionsIterator();
            while (iter.hasNext()) {
                try {
                    ((Connection) iter.next()).close();
                } catch (SQLException ex) {
                    LOG.warn("SQLException at close JDBC Connection instance.", ex);
                }
            }
            clearConnections();
        }
    }

    /**
     * @see org.castor.persist.AbstractTransactionContext#rollbackConnections()
     */
    protected void rollbackConnections() {
        // Go through all the connections opened in this transaction, rollback and
        // close them one by one. Ignore errors.
        Iterator iter = connectionsIterator();
        while (iter.hasNext()) {
            Connection conn = (Connection) iter.next();
            try {
                conn.rollback();
                LOG.debug("Connection rolled back");
                conn.close();
                LOG.debug("Connection closed");
            } catch (SQLException ex) {
                LOG.warn("SQLException at rollback/close JDBC Connection instance.", ex);
            }
        }
        clearConnections();
    }

    /**
     * @see org.castor.persist.AbstractTransactionContext#closeConnections()
     */
    protected void closeConnections() throws TransactionAbortedException { }
}

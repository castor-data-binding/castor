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
import org.castor.core.util.Messages;
import org.exolab.castor.jdo.ConnectionFailedException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.persist.LockEngine;

/**
 * A transaction context is required in order to perform operations
 * against the database. The transaction context is mapped to {@link
 * javax.transaction.Transaction} for the ODMG API and into
 * {@link javax.transaction.xa.XAResource} for XA databases. The only
 * way to begin a new transaction is through the creation of a new
 * transaction context. All database access must be performed through
 * a transaction context.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr 2006) $
 * @since 1.0
 */
public final class GlobalTransactionContext extends AbstractTransactionContext {
    /** Log instance used for outputting debug statements. */
    private static final Log LOG = LogFactory.getLog(GlobalTransactionContext.class);

    /**
     * Create a new transaction context.
     * 
     * @param db Database instance
     */
    public GlobalTransactionContext(final Database db) {
        super(db);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.AbstractTransactionContext
     *      #createConnection(org.exolab.castor.persist.LockEngine)
     */
    protected Connection createConnection(final LockEngine engine)
    throws ConnectionFailedException {
        // Get a new connection from the engine. Since the engine has no
        // transaction association, we must do this sort of round trip. An attempt
        // to have the transaction association in the engine inflates the code size
        // in other places.
        try {
            return engine.getConnectionFactory().createConnection();
        } catch (SQLException ex) {
            throw new ConnectionFailedException(Messages.format("persist.nested", ex), ex);
        }
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.AbstractTransactionContext#commitConnections()
     */
    protected void commitConnections() throws TransactionAbortedException {
        Iterator iter = connectionsIterator();
        while (iter.hasNext()) {
            try {
                ((Connection) iter.next()).close();
            }  catch (SQLException ex) { 
                LOG.warn("SQLException at close JDBC Connection instance.", ex);
            } 
        }
        clearConnections();
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.AbstractTransactionContext#rollbackConnections()
     */
    protected void rollbackConnections() {
        // Go through all the connections opened in this transaction, rollback and
        // close them one by one. Ignore errors.
        Iterator iter = connectionsIterator();
        while (iter.hasNext()) {
            try {
                ((Connection) iter.next()).close();
                LOG.debug("Connection closed");
            } catch (SQLException ex) {
                LOG.warn("SQLException at close JDBC Connection instance.", ex);
            }
        }
        clearConnections();
    }

    /**
     * {@inheritDoc}
     * @see org.castor.persist.AbstractTransactionContext#closeConnections()
     */
    protected void closeConnections() throws TransactionAbortedException {
        // Go through all the connections opened in this transaction and close them
        // one by one. Close all that can be closed, after that report error if any.
        Exception error = null;

        Iterator iter = connectionsIterator();
        while (iter.hasNext()) {
            try {
                ((Connection) iter.next()).close();
            } catch (SQLException ex) {
                error = ex;
            }
        }
        clearConnections();
        
        if (error != null) {
            throw new TransactionAbortedException(
                    Messages.format("persist.nested", error), error);
        }
    }
}

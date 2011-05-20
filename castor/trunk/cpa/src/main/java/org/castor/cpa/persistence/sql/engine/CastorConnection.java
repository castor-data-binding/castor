/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 *
 * $Id: SQLStatementDelete.java 8469 2009-12-28 16:47:54Z rjoachim $
 */
package org.castor.cpa.persistence.sql.engine;

import java.sql.Connection;
import java.sql.SQLException;

import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * CastorConnection class holding connection and factory parameters to encapsulate
 * their usage in case of creation of CastorStatements.
 * 
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class CastorConnection {
    //-----------------------------------------------------------------------------------    

    /** Variable to store the PersistenceFactory. */
    private final PersistenceFactory _factory;

    /** Variable to store connection. */
    private Connection _connection;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param factory Instance of PersistenceFactory to be used to create CastorStatements.
     * @param connection Instance of the connection to be used to create CastorStatements.
     */
    public CastorConnection(final PersistenceFactory factory, final Connection connection) {
        _connection = connection;
        _factory = factory;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method to create new CastorStatement using local instances of
     * Connection & PersistenceFactory.
     * 
     * @return New instance of CastorStatement.
     */
    public CastorStatement createStatement() {
        return new CastorStatement(_factory, _connection);
    }

    /**
     * Getter returning current _connection.
     * 
     * @return Connection currently set.
     */
    public Connection getConnection() { return _connection; }

    /**
     * Sets this connection's auto-commit mode to the given state. If a connection is in
     * auto-commit mode, then all its SQL statements will be executed and committed as
     * individual transactions.  Otherwise, its SQL statements are grouped into transactions
     * that are terminated by a call to either the method <code>commit</code> or the method
     * <code>rollback</code>. By default, new connections are in auto-commit mode.
     * 
     * @param autoCommit <code>true</code> to enable auto-commit mode; <code>false</code>
     *        to disable it
     * @exception SQLException if a database access error occurs
     */
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        _connection.setAutoCommit(autoCommit);
    }

    /**
     * Makes all changes made since the previous commit/rollback permanent and releases any
     * database locks currently held by this <code>Connection</code> object. This method should
     * be used only when auto-commit mode has been disabled.
     *
     * @exception SQLException if a database access error occurs or this <code>Connection</code>
     *            object is in auto-commit mode.
     */
    public void commit() throws SQLException {
        _connection.commit();
    }

    /**
     * Undoes all changes made in the current transaction and releases any database locks
     * currently held by this <code>Connection</code> object. This method should be used
     * only when auto-commit mode has been disabled.
     *
     * @exception SQLException if a database access error occurs or this <code>Connection</code>
     *            object is in auto-commit mode.
     */
    public void rollback() throws SQLException {
        _connection.rollback();
    }

    /**
     * Releases this <code>Connection</code> object's database and JDBC resources immediately
     * instead of waiting for them to be automatically released.
     * <P>
     * Calling the method <code>close</code> on a <code>Connection</code> object that is already
     * closed is a no-op.
     * <P>
     * <B>Note:</B> A <code>Connection</code> object is automatically closed when it is garbage
     * collected. Certain fatal errors also close a <code>Connection</code> object.
     *
     * @exception SQLException if a database access error occurs.
     */
    public void close() throws SQLException {
        _connection.close();
    }

    //-----------------------------------------------------------------------------------    
}

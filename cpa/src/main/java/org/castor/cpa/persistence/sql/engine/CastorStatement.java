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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Update;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * CastorStatement class to wrap handling of PreparedStatements by providing functionality
 * to prepare statements, bind parameters, execute statements and, close statements.
 * 
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class CastorStatement {
    //-----------------------------------------------------------------------------------    

    /** Variable to store local instance of the used PersistenceFactory. */
    private final PersistenceFactory _factory;

    /** Variable to store local instance of the used Connection. */
    private final Connection _connection;

    /** Variable to store local instance of the used QueryContext. */
    private QueryContext _context;

    /** Variable to store local instance of the used PreparedStatment. */
    private PreparedStatement _statement;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param factory Instance of PersistenceFactory to be used to create CastorStatements.
     * @param connection Instance of the connection to be used to create CastorStatements.
     */
    public CastorStatement(final PersistenceFactory factory, final Connection connection) {
        _factory = factory;
        _connection = connection;
    }

    //-----------------------------------------------------------------------------------

    /** Method returning statement currently set.
     * 
     * @return Statement currently set.
     */
    public PreparedStatement getStatement() {
        return _statement;
    }

    /** Method to set statement.
     * 
     * @param stmt Statement to be set
     */
    public void setStatement(final PreparedStatement stmt) {
        _statement = stmt;
    }

    /** Method returning size of the parameter map.
     * 
     * @return Size of the current parameter map of the QueryContext.
     */
    public int getParameterSize() {
        return _context.parameterSize();
    }

    //-----------------------------------------------------------------------------------

    /**
     * Method to prepare select statement and store it in local Variable.
     * 
     * @param select Prepared select-object to create statement for.
     * @throws SQLException Reports database access errors.
     */
    public void prepareStatement(final Select select) throws SQLException {
        _context = new QueryContext(_factory);

        select.toString(_context);

        _statement = _connection.prepareStatement(_context.toString());
    }

    /**
     * Method to prepare update statement and store it in local Variable.
     * 
     * @param update Prepared update-object to create statement for.
     * @throws SQLException Reports database access errors.
     */
    public void prepareStatement(final Update update) throws SQLException {
        _context = new QueryContext(_factory);

        update.toString(_context);

        _statement = _connection.prepareStatement(_context.toString());
    }

    /**
     * Method to prepare insert statement and store it in local Variable.
     * 
     * @param insert Prepared insert-object to create statement for.
     * @param prepareStmt Flag to determine if statement has to be built or not.
     * @throws SQLException Reports database access errors.
     */
    public void prepareStatement(final Insert insert, final Boolean prepareStmt)
    throws SQLException {
        _context = new QueryContext(_factory);

        insert.toString(_context);

        if (prepareStmt) {
            _statement = _connection.prepareStatement(_context.toString());
        }
    }

    /**
     * Method to prepare insert statement and store it in local Variable.
     * 
     * @param insert Prepared insert-object to create statement for.
     * @throws SQLException Reports database access errors.
     */
    public void prepareStatement(final Insert insert) throws SQLException {
        _context = new QueryContext(_factory);

        insert.toString(_context);

        _statement = _connection.prepareStatement(_context.toString());
    }

    /**
     * Method to prepare update statement, append passed condition and store it in
     * local Variable.
     * 
     * @param update Prepared update-object to create statement for.
     * @param condition Condition to be appended to the QueryContext.
     * @throws SQLException Reports database access errors.
     */
    public void prepareStatement(final Update update, final Condition condition) 
    throws SQLException {
        _context = new QueryContext(_factory);

        update.toQueryString(_context);

        if (condition != null) {
            _context.append(QueryConstants.SPACE);
            _context.append(QueryConstants.WHERE);
            _context.append(QueryConstants.SPACE);
            condition.toString(_context);
        }

        _statement = _connection.prepareStatement(_context.toString());
    }

    /**
     * Method to prepare delete statement and store it in local Variable.
     * 
     * @param delete Prepared delete-object to create statement for.
     * @throws SQLException Reports database access errors.
     */
    public void prepareStatement(final Delete delete) throws SQLException {
        _context = new QueryContext(_factory);

        delete.toString(_context);

        _statement = _connection.prepareStatement(_context.toString());
    }

    /**
     * Method to bind passed parameters to the local statement.
     * 
     * @param name Name of the parameter to be bound.
     * @param value Value of the parameter to be bound.
     * @param type Type of the parameter to be bound.
     * @throws SQLException Reports database access errors.
     */
    public void bindParameter(final String name, final Object value, final int type)
    throws SQLException {
        if (_statement == null) { throw new SQLException("Statment not prepared!"); }
        _context.bindParameter(_statement, name, value, type);
    }

    /**
     * Method to execute prepared statement.
     * 
     * @return Numeric value telling about success of the execution of the statement.
     * @throws SQLException Reports database access errors.
     */
    public int executeUpdate() throws SQLException {
        if (_statement == null) { throw new SQLException("Statment not prepared!"); }
        return _statement.executeUpdate();
    }

    /**
     * Method to execute prepared statement and return ResultSet.
     * 
     * @return ResultSet containing data returned from database.
     * @throws SQLException Reports database access errors.
     */
    public ResultSet executeQuery() throws SQLException {
        if (_statement == null) { throw new SQLException("Statment not prepared!"); }
        return _statement.executeQuery();
    }

    /**
     * Method to close the prepared statement.
     * 
     * @throws SQLException Reports database access errors.
     */
    public void close() throws SQLException {
        _context = null;

        if (_statement != null) {
            _statement.close();
            _statement = null;
        }
    }

    /**
     * Method to get string representation of the existing context.
     * 
     * @return String representation of the existing context.
     */
    public String toString() {
        if (_context == null) { return null; }
        return _context.toString();
    }

    //-----------------------------------------------------------------------------------    
}

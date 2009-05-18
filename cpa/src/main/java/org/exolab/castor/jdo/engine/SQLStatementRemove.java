/*
 * Copyright 2006 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

public final class SQLStatementRemove {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementRemove.class);

    private final String _type;

    private final SQLColumnInfo[] _ids;

    private String _sqlStatement;
    
    private PreparedStatement _preparedStatement;

    public SQLStatementRemove(final SQLEngine engine, final PersistenceFactory factory) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();
        
        String mapTo = new ClassDescriptorJDONature(engine.getDescriptor()).getTableName();
        buildStatement(factory, mapTo);
    }
    
    private void buildStatement(final PersistenceFactory factory, final String mapTo) {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(factory.quoteName(mapTo));
        sql.append(JDBCSyntax.WHERE);
        for (int i = 0; i < _ids.length; i++) {
            if (i > 0) { sql.append(" AND "); }
            sql.append(factory.quoteName(_ids[i].getName()));
            sql.append(QueryExpression.OP_EQUALS);
            sql.append(JDBCSyntax.PARAMETER);
        }
        _sqlStatement = sql.toString();
        
        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.removing", _type, _sqlStatement));
        }
    }
    
    public synchronized Object executeStatement(final Connection conn, final Identity identity)
    throws PersistenceException {
        try {
            // get prepared statement
            prepareStatement(conn);
            
            // bind it with parameters list for execution
            bindIdentity(identity);
            
            // execute the prepared statement
            executeStatement();
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.deleteFatal", _type, _sqlStatement), except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
            closeStatement();
        }
        
        return null;
    }
    
    /**
     * Prepare the SQL statement.
     * 
     * @param conn An open connection.
     * @throws SQLException If a database access error occurs.
     */
    private void prepareStatement(final Connection conn) throws SQLException {
        _preparedStatement = conn.prepareStatement(_sqlStatement);
        
        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.removing", _type, _preparedStatement.toString()));
        }
    }

    /**
     * Bind identity values to the prepared statement.
     * 
     * @param identity Identity of the object to remove.
     * @throws SQLException If a database access error occurs or type one of the values to
     *         bind is ambiguous.
     */
    private void bindIdentity(final Identity identity) throws SQLException {  
        // counter declaration
        int count = 1;
        
        // loop through the identity fields and bind values to the statement object
        for (int i = 0; i < _ids.length; i++) {
            // bind value to prepared statement
            _preparedStatement.setObject(count++, _ids[i].toSQL(identity.get(i)));
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.format("jdo.removing", _type, _preparedStatement.toString()));
        }
    }

    /**
     * Execute the prepared statement.
     * 
     * @throws SQLException If a database access error occurs or the object to be deleted does
     *         not exist.
     */
    private void executeStatement() throws SQLException {
        int result = _preparedStatement.executeUpdate();
        
        // throw exception if execute returned < 1
        if (result < 1) {
            throw new SQLException("Object to be deleted does not exist!");
        }
    }
    
    /**
     * Close the prepared statement.
     */
    private void closeStatement() {
        try {
            if (_preparedStatement != null) {
                _preparedStatement.close();
            }
        } catch (Exception e) {
            LOG.warn("Problem closing JDBC statement", e);
        }
    }
}

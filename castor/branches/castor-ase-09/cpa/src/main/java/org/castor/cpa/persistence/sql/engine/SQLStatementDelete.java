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
package org.castor.cpa.persistence.sql.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SQLStatementDelete class that makes use of delete class hierarchy to generate SQL query
 * structure. Execute method prepares a SQL statement, binds identity values to parameters
 * of the query and executes it.
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class SQLStatementDelete {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementDelete.class);

    /** ThreadLocal attribute to store prepared statement for a
     *  particular connection that is unique to one thread. */
    private static final ThreadLocal<PreparedStatement> PREPARED_STATEMENT = 
        new ThreadLocal<PreparedStatement>();
   
    //-----------------------------------------------------------------------------------    

    /** Name of engine descriptor. */
    private final String _type;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;

    /** QueryContext for SQL query building, specifying database specific quotations 
     *  and parameters binding. */
    private final QueryContext _ctx;
    
    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.
     * @param factory Persistence factory for the database engine the entity is persisted in.
     *        Used to format the SQL statement.
     */    
    public SQLStatementDelete(final SQLEngine engine, final PersistenceFactory factory) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();
        _ctx = new QueryContext(factory);
        
        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());
    }
    
    /** 
     * Build SQL statement to remove entities of the type this class is responsible for.
     *
     * @param mapTo Table name retrieved from Class Descriptor trough JDO Nature.
     */
    private void buildStatement(final String mapTo) {
        // build condition for delete statement
        Condition condition = new AndCondition();
        for (int i = 0; i < _ids.length; i++) {
            String name = _ids[i].getName();
            condition.and(new Column(name).equal(new Parameter(name)));
        }
        
        // build delete class hierarchy that represents SQL query
        Delete delete = new Delete (mapTo);
        delete.setCondition(condition);
        delete.toString(_ctx);  
        
        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.removing", _type, _ctx.toString()));
        }
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Execute statement to remove entity with given identity from database using given JDBC
     * connection. 
     * 
     * @param conn An open JDBC connection.
     * @param identity Identity of the object to remove.
     * @return Always returns <code>null</code>. 
     * @throws PersistenceException If failed to remove object from database. This could happen
     *         if a database access error occurs, type of one of the values to bind is ambiguous
     *         or object to be deleted does not exist.
     */
    public Object executeStatement(final Connection conn, final Identity identity) 
    throws PersistenceException {
        try {
            // prepare statement
            prepareStatement(conn);
            
            // bind parameters to prepared statement
            bindIdentity(identity);
            
            // execute prepared statement
            executeStatement();
        } catch (SQLException ex) {
            LOG.fatal(Messages.format("jdo.deleteFatal", _type, _ctx.toString()), ex);
            throw new PersistenceException(Messages.format("persist.nested", ex), ex);
        } finally {
            // close statement
            closeStatement();
        }
        
        return null;
    }
    
    /**
     * Prepare the SQL statement.
     * 
     * @param conn An open JDBC connection.
     * @throws SQLException If a database access error occurs.
     */
    private void prepareStatement(final Connection conn) throws SQLException {
        // create prepared statement on JDBC connection
        PreparedStatement preparedStatement = conn.prepareStatement(_ctx.toString());

        // set prepared statement in thread local variable
        PREPARED_STATEMENT.set(preparedStatement);
        
        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.removing", _type, preparedStatement.toString()));
        }
    }

    /**
     * Bind identity values to the prepared statement.
     * 
     * @param identity Identity of the object to remove.
     * @throws SQLException If a database access error occurs or type of one of the values to
     *         bind is ambiguous.
     */
    private void bindIdentity(final Identity identity) throws SQLException { 
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
        
        // loop through the identity fields and bind values to the statement object
        for (int i = 0; i < _ids.length; i++) {
            // bind value to prepared statement
           _ctx.bindParameter(preparedStatement, _ids[i].getName(),
                   _ids[i].toSQL(identity.get(i)), _ids[i].getSqlType());  
        }
    }

    /**
     * Execute the prepared statement.
     * 
     * @throws SQLException If a database access error occurs or the object to be deleted does
     *         not exist.
     */
    private void executeStatement() throws SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.format("jdo.removing", _type, preparedStatement.toString()));
        }

        // execute the prepared statement
        int result = preparedStatement.executeUpdate();
        
        // throw exception if execute returned < 1
        if (result < 1) {
            throw new SQLException("Object to be deleted does not exist!");
        }
    }
    
    /**
     * Close the prepared statement.
     */
    private void closeStatement() {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (Exception ex) {
            LOG.warn("Problem closing JDBC statement", ex);
        }
    }

    //-----------------------------------------------------------------------------------    
}

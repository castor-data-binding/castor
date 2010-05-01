/*
 * Copyright 2010 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLEngine;

 /**
  * SQLStatementInsertCheck class to check duplicate primary key problem. If that
  * would be the case then DuplicateIdentityException will be raised.
  */
public final class SQLStatementInsertCheck {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementInsertCheck.class);
    
    /** ThreadLocal attribute for holding prepared statement for a
     *  particular connection that is unique to one thread. */
    private static final ThreadLocal<PreparedStatement> PREPARED_STATEMENT = 
        new ThreadLocal<PreparedStatement>();
    
    /** The name of engine descriptor. */
    private final String _type;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;
    
    /** QueryContext for SQL query building, specifying database specific quotations 
     *  and parameters binding. */
    private final QueryContext _ctx;

    /**
     * Constructor.
     * 
     * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.
     * @param factory Persistence factory for the database engine the entity is persisted in.
     *        Used to format the SQL statement.
     */  
    public SQLStatementInsertCheck(final SQLEngine engine, final PersistenceFactory factory) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();
        _ctx = new QueryContext(factory);

        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());
    }

    /**
     * Build SQL statement to check for duplicate keys.
     * 
     * @param mapTo Table name from which records need to be fetched.
     */
    private void buildStatement(final String mapTo) {
        // table to be checked
        Table table = new Table(mapTo);
        
        // define conditions for select statement
        Condition condition = new AndCondition();
        for (int i = 0; i < _ids.length; i++) {             
            condition.and(table.column(_ids[i].getName()).equal(new Parameter(_ids[i].getName())));
        }

        // initialize select statement returning only first identity column 
        Select select = new Select(table);
        select.addSelect(table.column(_ids[0].getName()));
        select.setCondition(condition);
         
        // construct SQL query string by walking through select class hierarchy and
        // generate map of parameter names to indices for binding of parameters.     
        select.toString(_ctx);
    }
    
    /**
     * Performs check for Duplicate primary key. 
     * 
     * @param conn An open JDBC connection.
     * @param identity Identity of the object to insert.
     * @throws PersistenceException If a database access error occurs, identity size mismatches.
     */
    public void insertDuplicateKeyCheck(final Connection conn, final Identity identity) 
    throws PersistenceException {        
        try {
            if (identity == null) { return; }
            
            //Prepares SQL Statement
            prepareStatement(conn);                   
            
            //Binds identity
            bindIdentity(identity);  
            
            //Execute query
            ResultSet resultSet = executeQuery();

            if (resultSet.next()) {
                closeStatement();
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _type, identity));
            }
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.insertCheckFatal", _type,  _ctx.toString()), except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
            //close statement
            closeStatement();
        }
    }
    
    /**
     * Prepares the SQL Statement.
     * 
     * @param conn An Open JDBC Connection
     * @throws SQLException If a database access error occurs.
     */
    private void prepareStatement (final Connection conn) 
    throws SQLException { 
        PreparedStatement preparedStatement = conn.prepareStatement(_ctx.toString());
        
        // set prepared statement in thread local variable
        PREPARED_STATEMENT.set(preparedStatement);
         
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.format("jdo.duplicateKeyCheck", _ctx.toString()));
        }
    }
    
    /**
     * Binds Identity.
     * 
     * @param identity Identity of the object to insert.
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void  bindIdentity (final Identity identity) 
    throws PersistenceException, SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get(); 

        // bind the identity to the preparedStatement
        if (identity.size() != _ids.length) {
            throw new PersistenceException("Size of identity field mismatched!");
        }
        for (int i = 0; i < _ids.length; i++) {
            // bind value to prepared statement
            _ctx.bindParameter(preparedStatement, _ids[i].getName(),
                    _ids[i].toSQL(identity.get(i)), _ids[i].getSqlType());  
        }       
    }
    
    /**
     * executeQuery.
     * 
     * @return resultset object containing the results of query
     * @throws SQLException If a database access error occurs
     */
    private ResultSet executeQuery () throws SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
        ResultSet result = preparedStatement.executeQuery();   
        
        return result;
    }
    
    /**
     * closes the opened statement.
     */
    private void closeStatement() {
        try {
            // get prepared statement from thread local variable
            PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
            
            // Close the insert/select statement
            if (preparedStatement != null) { preparedStatement.close(); }
        } catch (SQLException except2) {
            LOG.warn("Problem closing JDBC statement", except2);
        }
    }
}

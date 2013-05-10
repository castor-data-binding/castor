/*
 * Copyright 2009 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann,
 *                Ralf Joachim, Ahmad Hassan
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
 * $Id: SQLStatementUpdateCheck.java 2009-05-30 14:29:32 ahmad $
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
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

/** 
 * SQLStatementUpdatCheck class to check whether the new SQL update statement has failed because
 * entity has been removed previously from persistent storage or the object has been modified
 * before. If the object has been modified an ObjectModifiedException and if object has been
 * deleted an ObjectDeletedException is raised.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:tyip AT leafsoft DOT com">Thomas Yip</a>
 * @author <a href="mailto:bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @version $Revision: 8285 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class SQLStatementUpdateCheck {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementUpdateCheck.class);
    
    /** ThreadLocal attribute for holding prepared statement for a
     *  particular connection that is unique to one thread. */
    private static final ThreadLocal<PreparedStatement> PREPARED_STATEMENT = 
        new ThreadLocal<PreparedStatement>();
   
    //-----------------------------------------------------------------------------------    

    /** The name of engine descriptor. */
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
    public SQLStatementUpdateCheck(final SQLEngine engine, final PersistenceFactory factory) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();
        _ctx = new QueryContext(factory);
        
        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());
    }
    
    /**
     * Build SQL select statement to check weather the entity is still available in persistent
     * storage or not.
     *
     * @param mapTo Table name retrieved from Class Descriptor trough JDO Nature.
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
    
    //-----------------------------------------------------------------------------------    

    /**
     * This function checks whether the object specified in the statement has been previously
     * removed from the persistent storage or has been modified. If the object has been modified
     * an ObjectModifiedException and if object has been deleted an ObjectDeletedException is
     * raised.
     * 
     * @param conn An Open JDBC Connection.
     * @param identity Identity of the object to check for availability.
     * @throws PersistenceException If a database access error occurs, identity size mismatches,
     *         column length mismatches, ObjectDeletedException if object had been deleted or
     *         ObjectModifiedException if object had been modified before.
     */
    public void updateFailureCheck(final Connection conn, final Identity identity)
    throws PersistenceException {
        try {      
            prepareStatement(conn);                   
            
            //Binds identity
            bindIdentity(identity);
    
            //Load Data into resultset
            ResultSet resultSet = executeQuery();
        
            if (resultSet.next()) {                     
                String msg = Messages.format("persist.objectModified", _type, identity);
                throw new ObjectModifiedException(msg);
            }
            String msg = Messages.format("persist.objectDeleted", _type, identity);
            throw new ObjectDeletedException(msg);
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.updateCheckFatal", _type,  _ctx.toString()), except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
            //close statement
            closeStatement();
        }
    }
    
    /**
     * Prepare the SQL Statement.
     * 
     * @param conn An Open JDBC Connection.
     * @throws SQLException If a database access error occurs.
     */
    private void prepareStatement(final Connection conn) 
    throws SQLException { 
        PreparedStatement preparedStatement = conn.prepareStatement(_ctx.toString());
        
        // set prepared statement in thread local variable
        PREPARED_STATEMENT.set(preparedStatement);
         
        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.updateCheck", _type, preparedStatement.toString()));
        }
    }
    
    /**
     * Bind identity values to the prepared statement.
     * 
     * @param identity Identity of the object to check for availability.
     * @throws SQLException If database access error occurs.
     */
    private void  bindIdentity(final Identity identity) 
    throws SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
                
        // bind the identity of the row into the preparedStatement
        for (int i = 0; i < _ids.length; i++) {
            // bind value to prepared statement
            _ctx.bindParameter(preparedStatement, _ids[i].getName(),
                    _ids[i].toSQL(identity.get(i)), _ids[i].getSqlType());  
            
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.bindingIdentity", _ids[i].getName(),
                        _ids[i].toSQL(identity.get(i))));
            }
        }          
    }
    
    /**
     * Execute the prepared statement.
     * 
     * @return Result set containing the results of query.
     * @throws SQLException If a database access error occurs.
     */
    private ResultSet executeQuery () throws SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
        return preparedStatement.executeQuery();   
    }
    
    /**
     * Close the prepared statement.
     */
    private void closeStatement() {
        try {
            // get prepared statement from thread local variable
            PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
            
            // close the select statement
            if (preparedStatement != null) { preparedStatement.close(); }
        } catch (SQLException ex) {
            LOG.warn("Problem closing JDBC statement", ex);
        }
    }

    //-----------------------------------------------------------------------------------    
}

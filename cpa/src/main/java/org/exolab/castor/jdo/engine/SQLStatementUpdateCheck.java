/*
 * Copyright 2006 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim, Ahmad Hassan
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
package org.exolab.castor.jdo.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;

/** 
 * SQLStatementUpdatCheck class to check whether the new SQL Update statement has 
 * failed because it has been removed previously from Persistance storage or the object
 * has been modified before. If the object had been modified then ObjectModifiedException
 * is raised and if object had been deleted then ObjectDeletedException is raised.
 * 
 * @author Ahmad
 */
public final class SQLStatementUpdateCheck {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementUpdateCheck.class);
    
    /** ThreadLocal attribute for holding prepared statement for a
     *  particular connection that is unique to one thread. */
    private static final ThreadLocal<PreparedStatement> PREPARED_STATEMENT = 
        new ThreadLocal<PreparedStatement>();
   
    /** The name of engine descriptor. */
    private final String _type;
    
    /** Table name of the engine descriptor. */
    private final String _mapTo;
    
    /** The SQL statement for update failure reason check. */
    private final String _statement;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;
    
    /** Contains the Information specific to particular engine instancde. */
    private final SQLFieldInfo[] _fields;
    
    /**
     * Constructor.
     * 
     * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.    
     * @param statement SQL statement to check the previously updated/modified record.
     */
    public SQLStatementUpdateCheck(final SQLEngine engine, final String statement) {
        _statement = statement;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(engine.getDescriptor()).getTableName();
        _ids = engine.getColumnInfoForIdentities();
        _fields = engine.getInfo();
    }
    
    /**
     * This function checks whether the object specified in the statement
     * has been previously removed from the persistent storage or has been modified.
     * If the object had been modified then ObjectModifiedException is raised and 
     * if object had been deleted then ObjectDeletedException is raised.
     * 
     * @param conn An Open JDBC Connection
     * @param identity
     * @param oldentity
     * @throws PersistenceException If a database access error occurs, identity size mismatches,
     *         column length mismatches, ObjectDeletedException if object had been deleted or
     *         ObjectModifiedException if object had been modified before.
     */
    public void updateFailureCheck(final Connection conn, final Identity identity,
            final ProposedEntity oldentity) throws PersistenceException {
        try {      
            if (oldentity.getFields() != null) {
            prepareStatement(conn);                   
         
            //Binds identity
            bindIdentity(identity);
        
            //Load Data into resultset
            ResultSet resultSet = executeQuery();
            
            //Process Resultset data
            processData(identity, oldentity, resultSet);                
        }
        throw new ObjectDeletedException(Messages.format(
                "persist.objectDeleted", _type, identity));
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.updateCheckFatal", _type,  _statement), except);
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
        PreparedStatement preparedStatement = conn.prepareStatement(_statement);
        
        // set prepared statement in thread local variable
        PREPARED_STATEMENT.set(preparedStatement);
         
         if (LOG.isTraceEnabled()) {
             LOG.trace(Messages.format("jdo.updateCheck", _type, preparedStatement.toString()));
         }
    }
    
    /**
     * Binds Identity.
     * 
     * @param identity
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void  bindIdentity (final Identity identity) 
    throws PersistenceException, SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
        int offset = 1;
                
        // bind the identity of the row into the preparedStatement
        for (int i = 0; i < _ids.length; i++) {
            preparedStatement.setObject(offset++, _ids[i].toSQL(identity.get(i)));

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.bindingIdentity", _ids[i].getName(),
                        _ids[i].toSQL(identity.get(i))));
            }
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
     * process the ResultSet.
     * 
     * @param identity
     * @param oldentity
     * @param resultSet containing the query results
     * @throws SQLException If a database access error occurs
     * @throws ObjectModifiedException Exception raised if object has been modified
     * previously in the Persistence Storage
     */
    private void processData (final Identity identity, final ProposedEntity oldentity, 
            final ResultSet resultSet) throws SQLException, ObjectModifiedException {
        if (resultSet.next()) {                     
             StringBuffer enlistFieldsNotMatching = new StringBuffer();
             
             int numberOfFieldsNotMatching = 0;
             for (int i = 0; i < _fields.length; i++) {
                 SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                 Object value = oldentity.getField(i);
                 Object currentField = columns[0].toJava(resultSet.getObject(
                         columns[0].getName()));
                 if (_fields[i].getTableName().compareTo(_mapTo) == 0) {
                     if ((value == null) || ((value != null)
                             && (currentField == null))) {
                         enlistFieldsNotMatching.append("(" + _type + ")."
                                 + columns[0].getName() + ": ");
                         enlistFieldsNotMatching.append("[" + value + "/"
                                 + currentField + "]"); 
                     } else if (!value.equals(currentField)) {
                         if (numberOfFieldsNotMatching >= 1) {
                             enlistFieldsNotMatching.append(", ");
                         }
                         enlistFieldsNotMatching.append("(" + _type + ")."
                                 + columns[0].getName() + ": ");
                         enlistFieldsNotMatching.append("[" + value + "/"
                                 + currentField + "]"); 
                         numberOfFieldsNotMatching++;
                     }
                 }
             }

             throw new ObjectModifiedException(Messages.format(
                     "persist.objectModified", _type, identity,
                     enlistFieldsNotMatching.toString()));
         }
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

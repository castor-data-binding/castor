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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
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

 /**
  * SQLStatementInsertCheck class to check duplicate primary key problem. If that
  * would be the case then DuplicateIdentityException will be raised.
  */
public final class SQLStatementInsertCheck {
    //----------------------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementInsertCheck.class);

    /** The name of engine descriptor. */
    private final String _type;

    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;

    /** Variable to store built delete class hierarchy. */
    private Select _select;

    //----------------------------------------------------------------------------------------------
    
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

        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());
    }

    //----------------------------------------------------------------------------------------------

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
        _select = new Select(table);
        _select.addSelect(table.column(_ids[0].getName()));
        _select.setCondition(condition);
    }

    /**
     * Performs check for Duplicate primary key. 
     * @param conn CastorConnection holding connection and PersistenceFactory to be used to create
     *        statement.
     * @param identity Identity of the object to insert.
     * @throws PersistenceException If a database access error occurs, identity size mismatches.
     */
    public void insertDuplicateKeyCheck(final CastorConnection conn, final Identity identity) 
    throws PersistenceException {
        CastorStatement stmt = conn.createStatement();

        try {
            if (identity == null) { return; }

            //Prepares SQL Statement
            stmt.prepareStatement(_select);

            //Binds identity
            bindIdentity(identity, stmt);

            //Execute query
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                stmt.close();
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _type, identity));
            }
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.insertCheckFatal", _type,  stmt.toString()), except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
            //close statement
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
        }
    }

    /**
     * Binds Identity.
     * 
     * @param identity Identity of the object to insert.
     * @param stmt CastorStatement containing Connection and PersistenceFactory.
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void  bindIdentity (final Identity identity, final CastorStatement stmt) 
    throws PersistenceException, SQLException {
        // bind the identity to the preparedStatement
        if (identity.size() != _ids.length) {
            throw new PersistenceException("Size of identity field mismatched!");
        }
        for (int i = 0; i < _ids.length; i++) {
            // bind value to prepared statement
            stmt.bindParameter(_ids[i].getName(), _ids[i].toSQL(identity.get(i)),
                    _ids[i].getSqlType());
        }
    }

    //----------------------------------------------------------------------------------------------
}

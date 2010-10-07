/*
 * Copyright 2010 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann,
 *                Ralf Joachim, Dennis Butterstein
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

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.info.ColInfo;
import org.castor.cpa.persistence.sql.engine.info.ColumnValue;
import org.castor.cpa.persistence.sql.engine.info.TableInfo;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;

/**
 * SQLStatementDelete class that makes use of delete class hierarchy to generate SQL query
 * structure. Execute method prepares a SQL statement, binds identity values to parameters
 * of the query and executes it.
 * 
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class SQLStatementDelete {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementDelete.class);

    //-----------------------------------------------------------------------------------    

    /** Name of engine descriptor. */
    private final String _type;

    /** Variable to store built delete class hierarchy. */
    private Delete _delete;

    /** TableInfo object holding queried table with its relations. */
    private TableInfo _tableInfo;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor.
     * 
     * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.
     */
    public SQLStatementDelete(final SQLEngine engine) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _tableInfo = engine.getTableInfo();

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
        for (ColInfo col : _tableInfo.getPrimaryKey().getColumns()) {
            String name = col.getName();
            condition.and(new Column(name).equal(new Parameter(name)));
        }

        // build delete class hierarchy that represents SQL query
        _delete = new Delete (mapTo);
        _delete.setCondition(condition);

        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.removing", _type, _delete.toString()));
        }
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Execute statement to remove entity with given identity from database using given JDBC
     * connection. 
     * 
     * @param conn CastorConnection holding connection and PersistenceFactory to be used to create
     *        statement.
     * @param identity Identity of the object to remove.
     * @throws PersistenceException If failed to remove object from database. This could happen
     *         if a database access error occurs, type of one of the values to bind is ambiguous
     *         or object to be deleted does not exist.
     */
    public void executeStatement(final CastorConnection conn, final Identity identity)
    throws PersistenceException {
        CastorStatement stmt = conn.createStatement();
        try {
            stmt.prepareStatement(_delete);

            // bind the identity of the row to be stored into the preparedStatement
            for (ColumnValue value : _tableInfo.toSQL(identity)) {
                stmt.bindParameter(value.getName(), value.getValue(), value.getType());
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.removing", _type, stmt.toString()));
            }

            // execute prepared statement
            int result = stmt.executeUpdate();

            // throw exception if execute returned < 1
            if (result < 1) {
                throw new SQLException("Object to be deleted does not exist!");
            }
        } catch (SQLException ex) {
            LOG.fatal(Messages.format("jdo.deleteFatal", _type, stmt.toString()), ex);
            throw new PersistenceException(Messages.format("persist.nested", ex), ex);
        } finally {
            // close statement
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
        }
    }

    //-----------------------------------------------------------------------------------    
}
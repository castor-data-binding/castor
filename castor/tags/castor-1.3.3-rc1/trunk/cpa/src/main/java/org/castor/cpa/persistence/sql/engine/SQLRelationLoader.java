/*
 * Copyright 2011 Thomas Yip, Ralf Joachim, Johannes Venzke
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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.info.ColumnInfo;
import org.castor.cpa.persistence.sql.engine.info.ColumnValue;
import org.castor.cpa.persistence.sql.engine.info.RelationTableInfo;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.castor.jdo.util.JDOUtils;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.spi.Identity;

/**
 * SQLRelationLoader is a quick hack for creating and removing relation from a many-to-many
 * relation database from ClassMolder. Eventually, it will be merged into SQLEngine. But, it
 * requires changing of the Persistence interface.
 *
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:johannes DOT venzke AT revival DOT de">Johannes Venzke</a>
 * @version $Revision$ $Date$
 */
public final class SQLRelationLoader {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLRelationLoader.class);

    //-----------------------------------------------------------------------------------    

    /** The relation table. */
    private RelationTableInfo _relation;
    
    /** The SQL statement for selecting the relation from the relation table. */
    private Select _select;

    /** The SQL statement to insert the a new relation into the relation table. */
    private Insert _insert;

    /** The SQL statement to delete an relation from the relation table. */
    private Delete _delete;

    /** The SQL statement to delete all the relation associate with the left side type. */
    private Delete _deleteAll;

    //-----------------------------------------------------------------------------------    

    /**
     * Create class to handle n-to-m relations of two tables. 
     * 
     * @param relation The relation table. 
     */
    public SQLRelationLoader(final RelationTableInfo relation) {
        _relation = relation;
        
        List<ColumnInfo> left = relation.getLeftForeignKey().getFromColumns();
        List<ColumnInfo> right = relation.getRightForeignKey().getFromColumns();
        
        constructSelectStatement(left, right);
        constructInsertStatement(left, right);
        constructDeleteStatement(left, right);
        constructDeleteAllStatement(left);
    }

    /**
     * Construct SELECT statement.
     *
     * @param left The columns that represent foreign keys to left table of relation.
     * @param right The columns that represent foreign keys to right table of relation.
     */
    private void constructSelectStatement(
            final List<ColumnInfo> left, final List<ColumnInfo> right) {
        _select = new Select(_relation.getTableName());

        AndCondition condition = new AndCondition();
        _select.setCondition(condition);
        
        for (ColumnInfo colInfo : left) {
            String name = colInfo.getName();
            Column column = new Column(name);
            _select.addSelect(column);
            condition.and(column.equal(new Parameter(name)));
        }
        
        for (ColumnInfo colInfo : right) {
            String name = colInfo.getName();
            Column column = new Column(name);
            _select.addSelect(column);
            condition.and(column.equal(new Parameter(name)));
        }
    }

    /**
     *  Construct INSERT statement. 
     *
     * @param left The columns that represent foreign keys to left table of relation.
     * @param right The columns that represent foreign keys to right table of relation.
     */
    private void constructInsertStatement(
            final List<ColumnInfo> left, final List<ColumnInfo> right) {
        _insert = new Insert(_relation.getTableName());
        
        for (ColumnInfo colInfo : left) {
            String name = colInfo.getName();
            _insert.addAssignment(new Column(name), new Parameter(name));
        }
        
        for (ColumnInfo colInfo : right) {
            String name = colInfo.getName();
            _insert.addAssignment(new Column(name), new Parameter(name));
        }
    }

    /**
     * Construct DELETE statement. 
     *
     * @param left The columns that represent foreign keys to left table of relation.
     * @param right The columns that represent foreign keys to right table of relation.
     */
    private void constructDeleteStatement(
            final List<ColumnInfo> left, final List<ColumnInfo> right) {
        _delete = new Delete(_relation.getTableName());
        
        AndCondition condition = new AndCondition();
        _delete.setCondition(condition);

        for (ColumnInfo colInfo : left) {
            String name = colInfo.getName();
            condition.and(new Column(name).equal(new Parameter(name)));
        }
        
        for (ColumnInfo colInfo : right) {
            String name = colInfo.getName();
            condition.and(new Column(name).equal(new Parameter(name)));
        }
    }

    /**
     * Construct DELETE statement for the left side only. 
     *
     * @param left The columns that represent foreign keys to left table of relation.
     */
    private void constructDeleteAllStatement(final List<ColumnInfo> left) {
        _deleteAll = new Delete(_relation.getTableName());
        
        AndCondition condition = new AndCondition();
        _deleteAll.setCondition(condition);

        for (ColumnInfo colInfo : left) {
            String name = colInfo.getName();
            condition.and(new Column(name).equal(new Parameter(name)));
        }
    }

    //-----------------------------------------------------------------------------------    

    public void createRelation(final CastorConnection conn, 
            final Identity left, final Identity right)
    throws PersistenceException {
        CastorStatement selectStatement = conn.createStatement();
        CastorStatement insertStatement = conn.createStatement();
        
        ResultSet rset = null;

        try {
            selectStatement.prepareStatement(_select);
            
            List<ColumnValue> colVals = _relation.toSQL(left, right);
            for (ColumnValue cv : colVals) {
                selectStatement.bindParameter(cv.getName(), cv.getValue(), cv.getType());
            }
            
            rset = selectStatement.executeQuery();
            if (!rset.next()) {
                insertStatement.prepareStatement(_insert);

                for (ColumnValue cv : colVals) {
                    insertStatement.bindParameter(cv.getName(), cv.getValue(), cv.getType());
                }
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.format("jdo.inserting", insertStatement.toString()));
                }
                
                insertStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new PersistenceException(Messages.format("persist.nested", ex), ex);
        } finally {
            // close statement
            try {
                JDOUtils.closeResultSet(rset);
                selectStatement.close();
                insertStatement.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
        }
    }

    public void deleteRelation(final CastorConnection conn, final Identity left)
    throws PersistenceException {
        CastorStatement stmt = conn.createStatement();
        try {
            stmt.prepareStatement(_deleteAll);
            
            for (ColumnValue cv : _relation.toSQL(left)) {
                stmt.bindParameter(cv.getName(), cv.getValue(), cv.getType());
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.removing", stmt.toString()));
            }
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
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

    public void deleteRelation(final CastorConnection conn,
            final Identity left, final Identity right)
    throws PersistenceException {
        CastorStatement stmt = conn.createStatement();
        try {
            stmt.prepareStatement(_delete);
            
            for (ColumnValue cv : _relation.toSQL(left, right)) {
                stmt.bindParameter(cv.getName(), cv.getValue(), cv.getType());
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.removing", stmt.toString()));
            }
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
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

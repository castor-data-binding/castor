/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.persist;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.CastorConnection;
import org.castor.cpa.persistence.sql.engine.CastorStatement;
import org.castor.cpa.persistence.sql.query.Delete;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
import org.castor.jdo.util.JDOUtils;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.persist.spi.Identity;

/**
 * SQLRelationLoader is a quick hack for creating and removing
 * relation from a many-to-many relation database from ClassMolder.
 * Eventually, it will be merged into SQLEngine. But, it requires
 * chaning of the Persistence interface.
 *
 * @author <a href="mailto:yip@intalio.com">Thomas Yip</a>
 */
public class SQLRelationLoader {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLRelationLoader.class);

    //-----------------------------------------------------------------------------------    

    /** The name of the relation table */
    private String _tableName;
    
    private SQLColumnInfo[] _left;

    private SQLColumnInfo[] _right;

    /** The SQL statement for selecting the relation from the relation table. */
    private Select _select;

    /** The SQL statement to insert the a new relation into the relation table. */
    private Insert _insert;

    /** The SQL statement to delete an relation from the relation table. */
    private Delete _delete;

    /** The SQL statement to delete all the relation associate with the left side type. */
    private Delete _deleteAll;

    public SQLRelationLoader(final String table, final String[] key, final int[] keyType,
            final TypeConvertor[] idTo, final TypeConvertor[] idFrom,
            final String[] otherKey, final int[] otherKeyType,
            final TypeConvertor[] ridTo, final TypeConvertor[] ridFrom) {
        
      _left = new SQLColumnInfo[key.length];
      for (int i = 0; i < key.length; i++) {
          _left[i] = new SQLColumnInfo(key[i], keyType[i], idTo[i], idFrom[i]);
      }
      
      _right = new SQLColumnInfo[otherKey.length];
      for (int i = 0; i < otherKey.length; i++) {
          _right[i] = new SQLColumnInfo(otherKey[i], otherKeyType[i], ridTo[i], ridFrom[i]);
      }

        _tableName = table;
        
        // construct select statement
        Table qualifier = new Table(_tableName);
        _select = new Select(qualifier);
        for (SQLColumnInfo colInfo : _left) {
            _select.addSelect(new Column(colInfo.getName()));
        }
        for (SQLColumnInfo colInfo : _right) {
            _select.addSelect(new Column(colInfo.getName()));
        }
        
        AndCondition conditionSelect = new AndCondition();
        for (SQLColumnInfo colInfo : _left) {
            conditionSelect.and(new Column(colInfo.getName()).equal(
                    new Parameter(colInfo.getName())));
        }
        for (SQLColumnInfo colInfo : _right) {
            conditionSelect.and(new Column(colInfo.getName()).equal(
                    new Parameter(colInfo.getName())));
        }
        _select.setCondition(conditionSelect);
        

        // construct insert statement
        _insert = new Insert(_tableName);
        for (SQLColumnInfo colInfo : _left) {
            _insert.addAssignment(new Column(colInfo.getName()), 
                    new Parameter(colInfo.getName()));
        }
        for (SQLColumnInfo colInfo : _right) {
            _insert.addAssignment(new Column(colInfo.getName()), 
                    new Parameter(colInfo.getName()));
        }

        // construct delete statement
        _delete = new Delete(_tableName);
        AndCondition conditionDelete = new AndCondition();
        for (SQLColumnInfo colInfo : _left) {
            conditionDelete.and(new Column(colInfo.getName()).equal(
                    new Parameter(colInfo.getName())));
        }
        for (SQLColumnInfo colInfo : _right) {
            conditionDelete.and(new Column(colInfo.getName()).equal(
                    new Parameter(colInfo.getName())));
        }
        _delete.setCondition(conditionDelete);

        // construct delete statement for the left side only
        _deleteAll = new Delete(_tableName);
        AndCondition conditionDeleteAll = new AndCondition();
        for (SQLColumnInfo colInfo : _left) {
            conditionDeleteAll.and(new Column(colInfo.getName()).equal(
                    new Parameter(colInfo.getName())));
        }
        _deleteAll.setCondition(conditionDeleteAll);
    }

    private Object idToSQL(final int index, final Object object) {
        if ((object == null) || (_left[index].getConvertFrom() == null)) { return object; }
        return _left[index].getConvertFrom().convert(object);
    }

    private Object ridToSQL(final int index, final Object object) {
        if ((object == null) || (_right[index].getConvertFrom() == null)) { return object; }
        return _right[index].getConvertFrom().convert(object);
    }

    public void createRelation(final CastorConnection conn, final Identity left, final Identity right)
    throws PersistenceException {
        CastorStatement selectStatement = conn.createStatement();
        CastorStatement insertStatement = conn.createStatement();
        
        ResultSet rset = null;

        try {
            selectStatement.prepareStatement(_select);
            for (int i = 0; i < left.size(); i++) {
                String name = _left[i].getName();
                Object value = idToSQL(i, left.get(i));
                int type = _left[i].getSqlType();
                
                selectStatement.bindParameter(name, value, type);
            }
            for (int i = 0; i < right.size(); i++) {
                String name = _right[i].getName();
                Object value = ridToSQL(i, right.get(i));
                int type = _right[i].getSqlType();
                
                selectStatement.bindParameter(name, value, type);
            }
            rset = selectStatement.executeQuery();

            insertStatement.prepareStatement(_insert);
            if (!rset.next()) {
                for (int i = 0; i < left.size(); i++) {
                    String name = _left[i].getName();
                    Object value = idToSQL(i, left.get(i));
                    int type = _left[i].getSqlType();
                    
                    insertStatement.bindParameter(name, value, type);
                }
                for (int i = 0; i < right.size(); i++) {
                    String name = _right[i].getName();
                    Object value = ridToSQL(i, right.get(i));
                    int type = _right[i].getSqlType();
                    
                    insertStatement.bindParameter(name, value, type);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.format("jdo.inserting", insertStatement.toString()));
                }
                
                insertStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            LOG.fatal(selectStatement.toString() + "\n" + insertStatement.toString(), ex);
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
            for (int i = 0; i < left.size(); i++) {
                Object value = idToSQL(i, left.get(i));
                
                stmt.bindParameter(_left[i].getName(), value, _left[i].getSqlType());
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.removing", stmt.toString()));
            }
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOG.fatal(stmt.toString(), ex);
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

    public void deleteRelation(final CastorConnection conn, final Identity left, final Identity right)
    throws PersistenceException {
        CastorStatement stmt = conn.createStatement();
        try {
            stmt.prepareStatement(_delete);
            for (int i = 0; i < left.size(); i++) {
                Object value = idToSQL(i, left.get(i));
                
                stmt.bindParameter(_left[i].getName(), value, _left[i].getSqlType());
            }
            for (int i = 0; i < right.size(); i++) {
                Object value = ridToSQL(i, right.get(i));
                
                stmt.bindParameter(_right[i].getName(), value, _right[i].getSqlType());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.removing", stmt.toString()));
            }
            
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOG.fatal(stmt.toString(), ex);
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
}

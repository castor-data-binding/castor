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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.visitor.UncoupleVisitor;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.jdo.util.JDOUtils;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

public final class SQLStatementLoad {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementLoad.class);

    private final SQLEngine _engine;
    
    private final PersistenceFactory _factory;
    
    private final String _type;

    private final String _mapTo;

    /** Map storing mapping between select-column and resultset-column. */
    private Map<String, Integer> _resultColumnMap;

    /** Number of ClassDescriptor that extend this one. */
    private final int _numberOfExtendLevels;

    /** Collection of all the ClassDescriptor that extend this one (closure). */
    private final Collection<ClassDescriptor> _extendingClassDescriptors;

    private String _statementNoLock;
    
    private String _statementLock;

    public SQLStatementLoad(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        _engine = engine;
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(engine.getDescriptor()).getTableName();

        // obtain the number of ClassDescriptor that extend this one.
        _numberOfExtendLevels = SQLHelper.numberOfExtendingClassDescriptors(engine.getDescriptor());
        _extendingClassDescriptors = 
            new ClassDescriptorJDONature(engine.getDescriptor()).getExtended();

        buildStatement();
    }

    private void buildStatement() throws MappingException {
        // Provisional select to construct map holding mapping between select- and
        // resultset-columns.
        Select select = new Select("provisional Select");
        try {
            QueryExpression expr = _factory.getQueryExpression();
            
            Map<String, Boolean> identitiesUsedForTable = new HashMap<String, Boolean>();
            Vector<String> joinTables = new Vector<String>();
            
            // join all the extended table
            ClassDescriptor curDesc = _engine.getDescriptor();
            ClassDescriptor baseDesc;
            while (curDesc.getExtends() != null) {
                baseDesc = curDesc.getExtends();
                String[] curDescIdNames = SQLHelper.getIdentitySQLNames(curDesc);
                String[] baseDescIdNames = SQLHelper.getIdentitySQLNames(baseDesc);
                expr.addInnerJoin(
                        new ClassDescriptorJDONature(curDesc).getTableName(), curDescIdNames,
                        new ClassDescriptorJDONature(curDesc).getTableName(), 
                        new ClassDescriptorJDONature(baseDesc).getTableName(), baseDescIdNames,
                        new ClassDescriptorJDONature(baseDesc).getTableName());
                joinTables.add(new ClassDescriptorJDONature(baseDesc).getTableName());
                curDesc = baseDesc;
            }
            
            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            SQLFieldInfo[] fields = _engine.getInfo();

            
            for (int i = 0; i < fields.length; i++) {
                SQLFieldInfo field = fields[i];
                String tableName = field.getTableName();

                // add id fields for root table if first field points to a separate table
                if ((i == 0) && field.isJoined()) {
                    String[] identities = SQLHelper.getIdentitySQLNames(_engine.getDescriptor());
                    for (int j = 0; j < identities.length; j++) {
                        String name = new ClassDescriptorJDONature(curDesc).getTableName();
                        select.addSelect(new Column(new Table(name), identities[j]));
                        expr.addColumn(name, identities[j]);
                    }
                    identitiesUsedForTable.put(
                            new ClassDescriptorJDONature(curDesc).getTableName(),
                            Boolean.TRUE);
                }
                
                // add id columns to select statement
                if (!field.isJoined()) {
                    ClassDescriptor classDescriptor =
                        field.getFieldDescriptor().getContainingClassDescriptor();
                    boolean isTableNameAlreadyAdded = identitiesUsedForTable.containsKey(
                            new ClassDescriptorJDONature(classDescriptor).getTableName());
                    if (!isTableNameAlreadyAdded) {
                        String[] identities = SQLHelper.getIdentitySQLNames(classDescriptor);
                        for (int j = 0; j < identities.length; j++) {
                            select.addSelect(new Column(new Table(tableName), identities[j]));
                            expr.addColumn(tableName, identities[j]);
                        }
                        identitiesUsedForTable.put(
                                new ClassDescriptorJDONature(classDescriptor).getTableName(),
                                Boolean.TRUE);
                    }
                }

                if (field.isJoined()) {
                    int offset = 0;
                    String[] rightCol = field.getJoinFields();
                    String[] leftCol = new String[ids.length - offset];
                    for (int j = 0; j < leftCol.length; j++) {
                        leftCol[j] = ids[j + offset].getName();
                    }
                    ClassDescriptor clsDescriptor = _engine.getDescriptor();
                    ClassDescriptorJDONature nature = new ClassDescriptorJDONature(clsDescriptor); 
                    if (joinTables.contains(field.getTableName())
                            || nature.getTableName().equals(field.getTableName())) {
                        
                        tableName = field.getTableAlias();
                        expr.addOuterJoin(_mapTo, leftCol, field.getTableName(), rightCol,
                                tableName);
                    } else {
                        expr.addOuterJoin(_mapTo, leftCol, tableName, rightCol, tableName);
                        joinTables.add(tableName);
                    }
                }

                for (int j = 0; j < field.getColumnInfo().length; j++) {
                    select.addSelect(new Column(new Table(tableName),
                            field.getColumnInfo()[j].getName()));
                    expr.addColumn(tableName, field.getColumnInfo()[j].getName());
                }
                
                expr.addTable(field.getTableName(), tableName);
            }

            // 'join' all the extending tables 
            List<ClassDescriptor> classDescriptorsToAdd = new LinkedList<ClassDescriptor>();
            ClassDescriptor classDescriptor = null;
            SQLHelper.addExtendingClassDescriptors(classDescriptorsToAdd,
                    new ClassDescriptorJDONature(_engine.getDescriptor()).getExtended());
            
            if (classDescriptorsToAdd.size() > 0) {
                Iterator<ClassDescriptor> iter = classDescriptorsToAdd.iterator();
                while (iter.hasNext()) {
                    classDescriptor = iter.next();
                    ClassDescriptorJDONature clsDescNature = 
                        new ClassDescriptorJDONature(classDescriptor);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Adding outer left join for "
                                + classDescriptor.getJavaClass().getName() + " on table "
                                + clsDescNature.getTableName());
                    }
                    
                    String[] engDescIdNames = SQLHelper.getIdentitySQLNames(
                            _engine.getDescriptor());
                    String[] clsDescIdNames = SQLHelper.getIdentitySQLNames(classDescriptor);
                    expr.addOuterJoin(_mapTo, engDescIdNames, 
                            clsDescNature.getTableName(), clsDescIdNames,
                            clsDescNature.getTableName());

                    Persistence persistenceEngine;
                    try {
                        persistenceEngine = _factory.getPersistence(classDescriptor);
                    } catch (MappingException e) {
                        throw new QueryException(
                                "Problem obtaining persistence engine for ClassDescriptor "
                                + classDescriptor.getJavaClass().getName(), e);
                    }

                    SQLEngine engine = (SQLEngine) persistenceEngine;
                    SQLColumnInfo[] idInfos = engine.getColumnInfoForIdentities();
                    for (int i = 0; i < idInfos.length; i++) {
                        select.addSelect(new Column(new Table(clsDescNature.getTableName()),
                                idInfos[i].getName()));
                        expr.addColumn(clsDescNature.getTableName(), idInfos[i].getName());
                    }
                    
                    SQLFieldInfo[] fieldInfos = ((SQLEngine) persistenceEngine).getInfo();
                    for (int i = 0; i < fieldInfos.length; i++) {
                        boolean hasFieldToAdd = false;
                        SQLColumnInfo[] columnInfos = fieldInfos[i].getColumnInfo();
                        if (clsDescNature.getTableName().equals(fieldInfos[i].getTableName())) {
                            for (int j = 0; j < columnInfos.length; j++) {
                                select.addSelect(new Column(new Table(clsDescNature.getTableName()),
                                        fieldInfos[i].getColumnInfo()[j].getName()));
                                expr.addColumn(clsDescNature.getTableName(),
                                        fieldInfos[i].getColumnInfo()[j].getName());
                            }
                            hasFieldToAdd = true;
                        }
                        
                        if (hasFieldToAdd) {
                            expr.addTable(clsDescNature.getTableName(),
                                    clsDescNature.getTableName());
                        }
                    }
                }
            }
            
            // get id columns' names
            for (int i = 0; i < ids.length; i++) {
                expr.addParameter(_mapTo, ids[i].getName(), QueryExpression.OP_EQUALS);
            }

            _statementNoLock = expr.getStatement(false);
            _statementLock = expr.getStatement(true);

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.loading", _type, _statementNoLock));
                LOG.trace(Messages.format("jdo.loading.with.lock", _type, _statementLock));
            }
        } catch (QueryException ex) {
            LOG.warn("Problem building SQL", ex);
            throw new MappingException(ex);
        }

        UncoupleVisitor uncle = new UncoupleVisitor();
        uncle.visit(select);
        _resultColumnMap = uncle.getResultColumnMap();
    }
    
    public void executeStatement(final Connection conn, final Identity identity,
            final ProposedEntity entity,final AccessMode accessMode) throws PersistenceException {
        PreparedStatement stmt  = null;
        ResultSet         rs    = null;

        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        SQLFieldInfo[] fields = _engine.getInfo();

        try {
            boolean locked = accessMode == AccessMode.DbLocked;
            String sqlString = locked ? _statementLock : _statementNoLock; 
            stmt = conn.prepareStatement(sqlString);

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.loading", _type, stmt.toString()));
            }

            int fieldIndex = 1;
            // bind the identity of the preparedStatement
            for (int i = 0; i < ids.length; i++) {
                stmt.setObject(fieldIndex++, ids[i].toSQL(identity.get(i)));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.loading", _type, stmt.toString()));
            }

            // execute the SQL query 
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new ObjectNotFoundException(Messages.format(
                        "persist.objectNotFound", _type, identity));
            }

            // if this class is part of an extend relation (hierarchy), let's investigate
            // what the real class type is vs. the specified one as part of the load statement;
            // this is done by looking at (the id fields of all) the extending class
            // desriptors, and by trying to find a (if not the) potential leaf descriptor;
            // if there's no potential leaf descriptor, let's simply continue; if there's
            // one, set the actual values in the ProposedEntity instance and return
            // to indicate that the load shoul dbe re-tried with the correct ClassMolder
            if (_extendingClassDescriptors.size() > 0) {
                Object[] returnValues = 
                    SQLHelper.calculateNumberOfFields(_extendingClassDescriptors, 
                            ids.length, fields.length, _numberOfExtendLevels, rs);
                ClassDescriptor potentialLeafDescriptor = (ClassDescriptor) returnValues[0];

                if ((potentialLeafDescriptor != null)
                        && !potentialLeafDescriptor.getJavaClass().getName().equals(_type)) {

                    entity.initializeFields(potentialLeafDescriptor.getFields().length);
                    entity.setActualEntityClass(potentialLeafDescriptor.getJavaClass());
                    entity.setExpanded(true);
                }

                // make sure that we only return early (as described above), if we actually
                // found a potential leaf descriptor
                if (potentialLeafDescriptor != null) {
                    return;
                }
            }

            boolean notNull;
            // index in fields[] for storing result of SQLTypes.getObject()
            for (int i = 0; i < fields.length; ++i) {
                SQLFieldInfo field = fields[i];
                SQLColumnInfo[] columns = field.getColumnInfo();

                String tableName = field.getTableAlias();

                // If alias of the field is not used, we have to use the tablename
                if (!_resultColumnMap.containsKey(
                        tableName + "." + field.getColumnInfo()[0].getName())) {
                    tableName = field.getTableName();
                }

                if (!field.isJoined() && (field.getJoinFields() == null)) {
                    entity.setField(columns[0].toJava(SQLTypeInfos.getValue(
                            rs, _resultColumnMap.get(tableName + "." + columns[0].getName()),
                            columns[0].getSqlType())), i);
                } else if (!field.isMulti()) {
                    notNull  = false;
                    Object[] id = new Object[columns.length];
                    for (int j = 0; j < columns.length; j++) {
                        id[j] = columns[j].toJava(SQLTypeInfos.getValue(rs,
                                _resultColumnMap.get(tableName + "." + columns[j].getName()),
                                columns[j].getSqlType()));
                        if (id[j] != null) { notNull = true; }
                    }
                    entity.setField(((notNull) ? new Identity(id) : null), i);
                } else {
                    ArrayList<Identity> res = new ArrayList<Identity>();
                    notNull = false;
                    Object[] id = new Object[columns.length];
                    for (int j = 0; j < columns.length; j++) {
                        id[j] = columns[j].toJava(SQLTypeInfos.getValue(rs,
                                _resultColumnMap.get(tableName + "." + columns[j].getName()),
                                columns[j].getSqlType()));
                        if (id[j] != null) { notNull = true; }
                    }
                    if (notNull) { res.add(new Identity(id)); }
                    entity.setField(res, i);
                }
            }

            while (rs.next()) {
                for (int i = 0; i < fields.length; ++i) {
                    SQLFieldInfo field = fields[i];
                    SQLColumnInfo[] columns = field.getColumnInfo();

                    String tableName = field.getTableAlias();
                    // If alias of the field is not used, we have to use the tablename
                    if (!_resultColumnMap.containsKey(
                            tableName + "." + field.getColumnInfo()[0].getName())) {
                        tableName = field.getTableName();
                    }

                    if (field.isMulti()) {
                        ArrayList res = (ArrayList) entity.getField(i);
                        notNull = false;
                        Object[] id = new Object[columns.length];
                        for (int j = 0; j < columns.length; j++) {
                            id[j] = columns[j].toJava(SQLTypeInfos.getValue(rs,
                                    _resultColumnMap.get(tableName + "." + columns[j].getName()),
                                            columns[j].getSqlType()));
                            if (id[j] != null) { notNull = true; }
                        }
                        if (notNull) {
                            Identity com = new Identity(id);
                            if (!res.contains(com)) { res.add(com); }
                        }
                    }
                }
            }

        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.loadFatal", _type,
                    (accessMode == AccessMode.DbLocked) ? _statementLock : _statementNoLock),
                    except);
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        } finally {
            JDOUtils.closeResultSet(rs);
            JDOUtils.closeStatement(stmt);
        }
    }
}

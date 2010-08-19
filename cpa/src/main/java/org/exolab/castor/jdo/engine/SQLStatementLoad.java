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
import org.castor.cpa.persistence.sql.engine.CastorConnection;
import org.castor.cpa.persistence.sql.engine.CastorStatement;
import org.castor.cpa.persistence.sql.query.Qualifier;
import org.castor.cpa.persistence.sql.query.Select;
import org.castor.cpa.persistence.sql.query.Table;
import org.castor.cpa.persistence.sql.query.TableAlias;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Compare;
import org.castor.cpa.persistence.sql.query.condition.CompareOperator;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
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
    
    private Select _select;

    public SQLStatementLoad(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        ClassDescriptor desc = engine.getDescriptor();
        _engine = engine;
        _factory = factory;
        _type = desc.getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(desc).getTableName();

        // obtain the number of ClassDescriptor that extend this one.
        _numberOfExtendLevels = SQLHelper.numberOfExtendingClassDescriptors(desc);
        _extendingClassDescriptors = new ClassDescriptorJDONature(desc).getExtended();

        buildStatement();
    }

    private void buildStatement() throws MappingException {
        try {
            Map<String, Boolean> idsUsedForTable = new HashMap<String, Boolean>();
            Vector<String> joinTables = new Vector<String>();

            ClassDescriptor mainTableDesc = _engine.getDescriptor();
            ClassDescriptor curDesc = mainTableDesc;
            Table mainTbl = new Table(_mapTo);
            _select = new Select(mainTbl);

            ClassDescriptor baseDesc;
            Table tempTable;
            Table walkTable = mainTbl;
            while (curDesc.getExtends() != null) {
                baseDesc = curDesc.getExtends();
                tempTable = new Table(new ClassDescriptorJDONature(baseDesc).getTableName());
                String[] curDescIdNames = SQLHelper.getIdentitySQLNames(curDesc);
                String[] baseDescIdNames = SQLHelper.getIdentitySQLNames(baseDesc);
                AndCondition cond = constructCondition(walkTable, curDescIdNames,
                        CompareOperator.EQ, tempTable, baseDescIdNames);

                walkTable.addInnerJoin(tempTable, cond);
                joinTables.add(new ClassDescriptorJDONature(baseDesc).getTableName());

                walkTable = tempTable;
                curDesc = baseDesc;
            }
            // Place constructed extends-hierarchy to first occurence of the queried table.

            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            SQLFieldInfo[] fields = _engine.getInfo();

            for (int i = 0; i < fields.length; i++) {
                SQLFieldInfo field = fields[i];
                String tableName = field.getTableName();
                Qualifier table = new Table(tableName);

                // add id fields for root table if first field points to a separate table
                if ((i == 0) && field.isJoined()) {
                    String[] identities = SQLHelper.getIdentitySQLNames(mainTableDesc);
                    for (int j = 0; j < identities.length; j++) {
                        String name = new ClassDescriptorJDONature(curDesc).getTableName();
                        _select.addSelect(new Table(name).column(identities[j]));
                    }
                    idsUsedForTable.put(new ClassDescriptorJDONature(curDesc).getTableName(),
                            Boolean.TRUE);
                }

                // add id columns to select statement
                if (!field.isJoined()) {
                    ClassDescriptor clsDesc =
                        field.getFieldDescriptor().getContainingClassDescriptor();
                    boolean isTableNameAlreadyAdded = idsUsedForTable.containsKey(
                            new ClassDescriptorJDONature(clsDesc).getTableName());
                    if (!isTableNameAlreadyAdded) {
                        String[] identities = SQLHelper.getIdentitySQLNames(clsDesc);
                        for (int j = 0; j < identities.length; j++) {
                            _select.addSelect(table.column(identities[j]));
                        }
                        idsUsedForTable.put(new ClassDescriptorJDONature(clsDesc).getTableName(),
                                Boolean.TRUE);
                    }
                }

                if (field.isJoined()) {
                    String[] rightCol = field.getJoinFields();
                    String[] leftCol = new String[ids.length];
                    for (int j = 0; j < leftCol.length; j++) {
                        leftCol[j] = ids[j].getName();
                    }
                    if (joinTables.contains(tableName) || _mapTo.equals(tableName)) {
                        
                        table = new TableAlias((Table) table, field.getTableAlias());
                        mainTbl.addLeftJoin(table, constructCondition(mainTbl, leftCol,
                                CompareOperator.EQ, table, rightCol));
                    } else {
                        mainTbl.addLeftJoin(table, constructCondition(mainTbl, leftCol,
                                CompareOperator.EQ, table, rightCol));
                        joinTables.add(table.name());
                    }
                }

                for (int j = 0; j < field.getColumnInfo().length; j++) {
                    _select.addSelect(table.column(field.getColumnInfo()[j].getName()));
                }
            }

            // 'join' all the extending tables 
            List<ClassDescriptor> classDescriptorsToAdd = new LinkedList<ClassDescriptor>();
            ClassDescriptor clsDesc = null;
            SQLHelper.addExtendingClassDescriptors(classDescriptorsToAdd,
                    new ClassDescriptorJDONature(mainTableDesc).getExtended());
            
            if (classDescriptorsToAdd.size() > 0) {
                Iterator<ClassDescriptor> iter = classDescriptorsToAdd.iterator();
                while (iter.hasNext()) {
                    clsDesc = iter.next();
                    ClassDescriptorJDONature clsDescNature = new ClassDescriptorJDONature(clsDesc);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Adding outer left join for "
                                + clsDesc.getJavaClass().getName() + " on table "
                                + clsDescNature.getTableName());
                    }
                    
                    String[] engDescIdNames = SQLHelper.getIdentitySQLNames(mainTableDesc);
                    String[] clsDescIdNames = SQLHelper.getIdentitySQLNames(clsDesc);
                    Table t = new Table(clsDescNature.getTableName());
                    mainTbl.addLeftJoin(t, constructCondition(mainTbl, engDescIdNames,
                            CompareOperator.EQ, t, clsDescIdNames));

                    Persistence persistenceEngine;
                    try {
                        persistenceEngine = _factory.getPersistence(clsDesc);
                    } catch (MappingException e) {
                        throw new QueryException(
                                "Problem obtaining persistence engine for ClassDescriptor "
                                + clsDesc.getJavaClass().getName(), e);
                    }

                    SQLEngine engine = (SQLEngine) persistenceEngine;
                    SQLColumnInfo[] idInfos = engine.getColumnInfoForIdentities();
                    for (int i = 0; i < idInfos.length; i++) {
                        _select.addSelect(t.column(idInfos[i].getName()));
                    }
                    
                    SQLFieldInfo[] fieldInfos = ((SQLEngine) persistenceEngine).getInfo();
                    for (int i = 0; i < fieldInfos.length; i++) {
                        if (t.name().equals(fieldInfos[i].getTableName())) {
                            for (int j = 0; j < fieldInfos[i].getColumnInfo().length; j++) {
                                _select.addSelect(
                                        t.column(fieldInfos[i].getColumnInfo()[j].getName()));
                            }
                        }
                    }
                }
            }

            // get id columns' names
            Condition condition = new AndCondition();
            for (int i = 0; i < ids.length; i++) {
                String name = ids[i].getName();
                condition.and(mainTbl.column(name).equal(new Parameter(name)));
            }
            _select.setCondition(condition);

        } catch (QueryException ex) {
            LOG.warn("Problem building SQL", ex);
            throw new MappingException(ex);
        }
    }

    /**
     * Method constructing condition for joins.
     * 
     * @param leftTbl Left table of the join
     * @param leftCols Columns of the left table to be used for the condition of the join.
     * @param compareOp Operator defining the compareOperator to be used in condition.
     * @param rightTbl Right table of the join.
     * @param rightCols Columns of the right table to be used for the condition of the join.
     * @return AndCondition containing all conditions for the join.
     */
    private AndCondition constructCondition(final Qualifier leftTbl, final String[] leftCols,
            final CompareOperator compareOp, final Qualifier rightTbl, final String[] rightCols) {
        AndCondition cond = new AndCondition();
        for (int i = 0; i < leftCols.length; i++) {
            cond.and(new Compare(leftTbl.column(leftCols[i]), compareOp,
                    rightTbl.column(rightCols[i])));
        }

        return cond;
    }

    public void executeStatement(final CastorConnection conn, final Identity identity,
            final ProposedEntity entity,final AccessMode accessMode) throws PersistenceException {
        ResultSet rs = null;
        
        UncoupleVisitor uncle = new UncoupleVisitor();
        uncle.visit(_select);
        _resultColumnMap = uncle.getResultColumnMap();
        
        CastorStatement stmt = conn.createStatement();

        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        SQLFieldInfo[] fields = _engine.getInfo();

        try {
            boolean locked = accessMode == AccessMode.DbLocked;
            _select.setLocked(locked);
            stmt.prepareStatement(_select);

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.loading", _type, stmt.toString()));
            }

            for (int i = 0; i < ids.length; i++) {
                stmt.bindParameter(ids[i].getName(), ids[i].toSQL(identity.get(i)),
                      ids[i].getSqlType());
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
            // descriptors, and by trying to find a (if not the) potential leaf descriptor;
            // if there's no potential leaf descriptor, let's simply continue; if there's
            // one, set the actual values in the ProposedEntity instance and return
            // to indicate that the load should be re-tried with the correct ClassMolder
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
                    entity.setField(getColValue(columns[0], rs, tableName), i);
                } else if (!field.isMulti()) {
                    Object[] id = fillObjectArray(columns, rs, tableName);
                    entity.setField(((id != null) ? new Identity(id) : null), i);
                } else {
                    ArrayList<Identity> res = new ArrayList<Identity>();
                    Object[] id = fillObjectArray(columns, rs, tableName);
                    if (id != null) { res.add(new Identity(id)); }
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
                        Object[] id = fillObjectArray(columns, rs, tableName);
                        if (id != null) {
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
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
        }
    }

    /**
     * Method constructing object array containing fetched resultset values.
     * 
     * @param columns Columns array holding columns to be read from resultset.
     * @param rs Resultset holding result from database query.
     * @param tableName Name of the table the columns are belonging to.
     * @return Null: Any of the fetched values was null, Object array: array containing results.
     * @throws SQLException Reports database access errors.
     */
    private Object[] fillObjectArray(final SQLColumnInfo[] columns, final ResultSet rs,
            final String tableName) throws SQLException {
        int colsLength = columns.length;
        boolean notNull = false;
        Object[] id = new Object[colsLength];
        for (int j = 0; j < colsLength; j++) {
            id[j] = getColValue(columns[j], rs, tableName);
            if (id[j] != null) { notNull = true; }
        }

        if (notNull) { return id; } 

        return null;
    }

    /**
     * Method reading value from resulset and converting it to matching data type.
     * 
     * @param col Column to be read from resultset.
     * @param rs Resultset holding result from database query.
     * @param tblName Name of the table the column belongs to.
     * @return Data of the column converted to the matching data type.
     * @throws SQLException Reports database access errors.
     */
    private Object getColValue(final SQLColumnInfo col, final ResultSet rs,
            final String tblName) throws SQLException {
        return col.toJava(SQLTypeInfos.getValue(rs,
                _resultColumnMap.get(tblName + "." + col.getName()), col.getSqlType()));
    }
}

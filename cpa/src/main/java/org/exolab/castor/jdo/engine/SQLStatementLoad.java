/*
 * Copyright 2006 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim,
 *                Dennis Butterstein
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
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.cpa.persistence.sql.engine.CastorConnection;
import org.castor.cpa.persistence.sql.engine.CastorStatement;
import org.castor.cpa.persistence.sql.engine.SQLEngine;
import org.castor.cpa.persistence.sql.engine.info.ColumnInfo;
import org.castor.cpa.persistence.sql.engine.info.ColumnValue;
import org.castor.cpa.persistence.sql.engine.info.TableInfo;
import org.castor.cpa.persistence.sql.engine.info.TableLink;
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
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SQLStatementLoad class that makes use of select class hierarchy to generate SQL query
 * structure. Execute method prepares a SQL statement, binds identity values to parameters
 * of the query, executes it and handles the results of the query.
 * 
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class SQLStatementLoad {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementLoad.class);

    /** Variable holding name of the descriptors' JavaClass. */
    private final String _type;

    /** Variable storing tableName of the queried table. */
    private final String _mapTo;

    /** Map storing mapping between select-column and resultSet-column. */
    private Map<String, Integer> _resultColumnMap;

    /** Number of ClassDescriptor that extend this one. */
    private final int _numberOfExtendLevels;

    /** Collection of all the ClassDescriptor that extend this one (closure). */
    private final Collection<ClassDescriptor> _extendingClassDescriptors;

    /** Variable to store built select class hierarchy. */
    private Select _select;

    /** Variable holding passed SQLEngine. */
    private SQLEngine _engine;

    /** TableInfo object holding queried table with its relations. */
    private TableInfo _mainTableInfo;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor creating new SQLStatementLoad.
     * 
     * @param engine SQLEngine to be used.
     * @param factory PersistenceFactory to be used.
     * @throws MappingException If we get into trouble.
     */
    public SQLStatementLoad(final SQLEngine engine, final PersistenceFactory factory)
    throws MappingException {
        ClassDescriptor desc = engine.getDescriptor();
        _type = desc.getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(desc).getTableName();
        _engine = engine;
        _mainTableInfo = engine.getTableInfo();

        // obtain the number of ClassDescriptor that extend this one.
        _numberOfExtendLevels = SQLHelper.numberOfExtendingClassDescriptors(desc);
        _extendingClassDescriptors = new ClassDescriptorJDONature(desc).getExtended();

        buildStatement();
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Build statement to load a specific record.
     * The query is constructed by the following approach:
     *      1.) First of all we walk up the extends hierarchy of the tables until we reach the
     *          root table. On this way, every table we reach gets joined with an inner join on
     *          the actual tables' primary keys and the extended tables primary key.
     *      2.) All columns and relations of the queried table are added - but only one step in
     *          depth!
     *      3.) Now we walk down the extends hierarchy and add the tables that are extending the
     *          queried table.
     *      4.) Last but not least we add the where condition for the query in order to select
     *          only the desired record.
     * 
     * @throws MappingException If we get into problems.
     */
    private void buildStatement() throws MappingException {
            List<TableInfo> joinTableInfos = new ArrayList<TableInfo>();

            Table mainTbl = new Table(_mapTo);
            _select = new Select(mainTbl);

            TableInfo currentTblInf = _mainTableInfo;
            Table walkTbl = mainTbl;
            Table tempTbl;

            // walk up the extends hierarchy and add columns and joins for every extended table
            // as result we get deeply nested table hierarchy
            while (currentTblInf.getExtendedTable() != null) {
                TableInfo extendedTable = currentTblInf.getExtendedTable();
                tempTbl = new Table(extendedTable.getTableName());

                AndCondition cond = constructCondition(walkTbl,
                        currentTblInf.getPrimaryKey().getColumns(),
                        CompareOperator.EQ, tempTbl,
                        extendedTable.getPrimaryKey().getColumns());

                walkTbl.addInnerJoin(tempTbl, cond);
                joinTableInfos.add(extendedTable);

                addCols(extendedTable, joinTableInfos, mainTbl, true);

                walkTbl = tempTbl;
                currentTblInf = extendedTable;
            }

            // add columns and joins for tables
            addCols(_mainTableInfo, joinTableInfos, mainTbl, true);

            // add columns and joins for tables extending the root table
            addExtendingTables(_mainTableInfo, mainTbl, joinTableInfos);

            // construct where condition for the query
            Condition condition = new AndCondition();
            for (ColumnInfo col : _mainTableInfo.getPrimaryKey().getColumns()) {
                String name = col.getName();
                condition.and(mainTbl.column(name).equal(new Parameter(name)));
            }
            _select.setCondition(condition);
    }

    /**
     * Method handling the extending tables.
     * It's approach is to handle these tables in a depth first manner.
     * 
     * @param info Table to add extending tables from.
     * @param mainTbl The mainTable queried.
     * @param joinTableInfos List holding Tables already joined.
     */
    private void addExtendingTables(final TableInfo info, final Table mainTbl,
            final List<TableInfo> joinTableInfos) {
        for (TableInfo tbl : info.getExtendingTables()) {
            Table t = new Table(tbl.getTableName());

            mainTbl.addLeftJoin(t, constructCondition(mainTbl,
                    _mainTableInfo.getPrimaryKey().getColumns(),
                    CompareOperator.EQ, t, tbl.getPrimaryKey().getColumns()));

            addCols(tbl, joinTableInfos, mainTbl, false);

            addExtendingTables(tbl, t, joinTableInfos);
        }
    }

    /**
     * Method adding columns of a TableInfo object.
     * First primary keys are added, second "normal columns" and last the foreign keys.
     * 
     * @param tblInfo The TableInfo object to add columns of.
     * @param joinTables List of tables already joined.
     * @param mainTbl The table queried.
     * @param addJoin Flag telling if we have to add joins and special columns or not.
     *        True: Joins are added as well as special columns, False: no joins are added and
     *        no special columns are attached to the select object.
     */
    private void addCols(final TableInfo tblInfo, final List<TableInfo> joinTables,
            final Table mainTbl, final boolean addJoin) {
        Qualifier table = new Table(tblInfo.getTableName());

        addColumns(table, tblInfo.getPrimaryKey().getColumns());
        addColumns(table, tblInfo.getColumns());

        // handle foreign keys: add their columns and joins if necessary
        for (TableLink tblLnk : tblInfo.getForeignKeys()) {
            if (!(TableLink.REFERS_TO == tblLnk.getRelationType())) {
                TableInfo joinTableInfo = tblLnk.getTargetTable();
                Qualifier joinTable = new Table(joinTableInfo.getTableName());
                if (addJoin) {
                    if (joinTables.contains(tblLnk.getTargetTable())
                            || _mapTo.equals(tblLnk.getTargetTable().getTableName())) {
                        joinTable = new TableAlias((Table) joinTable, tblLnk.getTableAlias());
                        mainTbl.addLeftJoin(joinTable, constructCondition(table,
                                tblLnk.getStartCols(), CompareOperator.EQ, joinTable,
                                tblLnk.getTargetCols()));
                    } else {
                        mainTbl.addLeftJoin(joinTable, constructCondition(table,
                                tblLnk.getStartCols(), CompareOperator.EQ, joinTable,
                                tblLnk.getTargetCols()));
                        joinTables.add(tblLnk.getTargetTable());
                    }

                    if (TableLink.REFERED_BY == (tblLnk.getRelationType())) {
                        addColumns(joinTable, joinTableInfo.getPrimaryKey().getColumns());
                    } else if (TableLink.MANY_TO_MANY == (tblLnk.getRelationType())) {
                        addColumns(joinTable, joinTableInfo.getColumns());
                    }
                }
            } else {
                addColumns(table, tblLnk.getStartCols());
            }
        }
    }

    /**
     * Method adding given list columns to select statement for the given table.
     * 
     * @param table Table to add columns from.
     * @param columns Columns to be added for given table.
     */
    private void addColumns(final Qualifier table, final List<ColumnInfo> columns) {
        for (ColumnInfo col : columns) {
            _select.addSelect(table.column(col.getName()));
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
    private AndCondition constructCondition(final Qualifier leftTbl, final List<ColumnInfo> leftCols,
            final CompareOperator compareOp, final Qualifier rightTbl,
            final List<ColumnInfo> rightCols) {
        if (leftCols.size() != rightCols.size()) {
            System.out.println("Error while constructing condition! Size of leftCols and rightCols"
                    + " is not equal!");
        }
        AndCondition cond = new AndCondition();
        for (int i = 0; i < leftCols.size(); i++) {
            cond.and(new Compare(leftTbl.column(leftCols.get(i).getName()), compareOp,
                    rightTbl.column(rightCols.get(i).getName())));
        }

        return cond;
    }

    /**
     * Execute statement to load entity with given identity from database using given JDBC
     * connection. 
     * 
     * @param conn CastorConnection holding connection and PersistenceFactory to be used to create
     *        statement.
     * @param identity Identity of the object to remove.
     * @param entity The proposed entity to be filled with results.
     * @param accessMode Used to determine if query level locking should be used or not.
     * @throws PersistenceException If failed to remove object from database. This could happen
     *         if a database access error occurs, type of one of the values to bind is ambiguous
     *         or object to be deleted does not exist.
     */
    @SuppressWarnings("unchecked")
    public void executeStatement(final CastorConnection conn, final Identity identity,
            final ProposedEntity entity, final AccessMode accessMode) throws PersistenceException {
        ResultSet rs = null;

        TableInfo info = _engine.getTableInfo();

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

            for (ColumnValue value : info.toSQL(identity)) {
                stmt.bindParameter(value.getName(), value.getValue(), value.getType());
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
                    // simple
                    Object[] id = fillObjectArray(columns, rs, tableName);
                    entity.setField(((id != null) ? new Identity(id) : null), i);
                } else {
                    // many key, many table
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
                        ArrayList<Identity> res = (ArrayList<Identity>) entity.getField(i);
                        Object[] id = fillObjectArray(columns, rs, tableName);
                        if (id != null) {
                            Identity com = new Identity(id);
                            if (!res.contains(com)) { res.add(com); }
                        }
                    }
                }
            }

        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.loadFatal", _type, _select.toString()), except);
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
     * Method constructing object array containing fetched resultSet values.
     * 
     * @param columns Columns array holding columns to be read from resultSet.
     * @param rs ResultSet holding result from database query.
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
     * Method reading value from resulSet and converting it to matching data type.
     * 
     * @param col Column to be read from resultSet.
     * @param rs ResultSet holding result from database query.
     * @param tblName Name of the table the column belongs to.
     * @return Data of the column converted to the matching data type.
     * @throws SQLException Reports database access errors.
     */
    private Object getColValue(final SQLColumnInfo col, final ResultSet rs,
            final String tblName) throws SQLException {
        return col.toJava(SQLTypeInfos.getValue(rs,
                _resultColumnMap.get(tblName + "." + col.getName()), col.getSqlType()));
    }

    //-----------------------------------------------------------------------------------    
}

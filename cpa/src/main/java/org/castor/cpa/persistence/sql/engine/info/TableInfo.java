/*
 * Copyright 2010 Dennis Butterstein, Ralf Joachim
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
 * $Id: TableInfo.java 8469 2009-12-28 16:47:54Z rjoachim $
 */

package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.persist.spi.Identity;

/**
 * Class representing given table classes as Tables.
 *
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 8469 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class TableInfo {
    //-----------------------------------------------------------------------------------    

    /** Variable storing name of the table. */
    private String _tableName;

    /** Variable storing the table extended by this one. */
    private TableInfo _extendedTable;

    /** List of tables that are extending this one. */
    private List<TableInfo> _extendingTables = new ArrayList<TableInfo>();

    /** Primary key of the table. */
    private final PrimaryKeyInfo _primaryKey;

    /** List of normal columns with no special meaning. */
    private List<ColInfo> _columns = new ArrayList<ColInfo>();

    /** List of foreign keys consisting of TableLinks. */
    private List<TableLink> _fks = new ArrayList<TableLink>();

    /** Index to map columns back to fields. */
    private int _fldIndex = 0;

    //-----------------------------------------------------------------------------------    

    /**
     * Constructor taking tableName in order to construct Table that holds his name only.
     * 
     * @param tableName Name of the table to be constructed.
     */
    private TableInfo(final String tableName) {
        _primaryKey = new PrimaryKeyInfo(this);
        _tableName = tableName;
    }

    /**
     * Constructor that starts building TableInfo hierarchy recursively.
     * 
     * @param clsDesc ClassDescriptor this TableInfo belongs to.
     * @param tblMap Map of TableInfo instances already created.
     * @throws MappingException Exception thrown when errors occur.
     */
    public TableInfo(final ClassDescriptor clsDesc,
            final Map<ClassDescriptor, TableInfo> tblMap) throws MappingException {
        _primaryKey = new PrimaryKeyInfo(this);

        if (!clsDesc.hasNature(ClassDescriptorJDONature.class.getName())) {
            throw new MappingException("ClassDescriptor has not a JDOClassDescriptor");
        }
        _tableName = new ClassDescriptorJDONature(clsDesc).getTableName();

        if (tblMap == null) { throw new MappingException("Table map was not initialized!"); }

        tblMap.put(clsDesc, this);

        // add extended table to tableInfo if exists.
        resolveExtendedTable(clsDesc, tblMap);

        // add extending tables to known resources.
        resolveExtendingTables(clsDesc, tblMap);

        // first we have to add the primary keys of this class.
        resolvePrimaryKeys(clsDesc);

        // then we have to add the other columns, such as foreign keys and normal columns.
        resolveColumns(clsDesc, tblMap);
    }

    private void resolveExtendedTable(final ClassDescriptor clsDesc,
            final Map<ClassDescriptor, TableInfo> tblMap) throws MappingException {
        if (clsDesc.getExtends() != null) {
            _extendedTable = getTableInfo(clsDesc.getExtends(), tblMap);
        }
    }
    
    private void resolveExtendingTables(final ClassDescriptor clsDesc,
            final Map<ClassDescriptor, TableInfo> tblMap) throws MappingException {
        for (ClassDescriptor desc : new ClassDescriptorJDONature(clsDesc).getExtended()) {
            _extendingTables.add(getTableInfo(desc, tblMap));
        }
    }
    
    private void resolvePrimaryKeys(final ClassDescriptor clsDesc) throws MappingException {
        String fldDescJdoNatureName = FieldDescriptorJDONature.class.getName();
        FieldDescriptor[] ids = ((ClassDescriptorImpl) clsDesc).getIdentities();
        for (int i = 0; i < ids.length; i++) {
            if (!ids[i].hasNature(fldDescJdoNatureName)) {
                throw new MappingException("Except JDOFieldDescriptor");
            }
            String[] sqlName = new FieldDescriptorJDONature(ids[i]).getSQLName();
            int[] sqlType =  new FieldDescriptorJDONature(ids[i]).getSQLType();
            FieldHandlerImpl fh = (FieldHandlerImpl) ids[i].getHandler();

            ColInfo column = new ColInfo(sqlName[0], sqlType[0], fh.getConvertTo(),
                    fh.getConvertFrom(), false, -1, false, true);
            addCol(column);
        }
    }
    
    private void resolveColumns(final ClassDescriptor clsDesc,
            final Map<ClassDescriptor, TableInfo> tblMap) throws MappingException {
        String fldDescJdoNatureName = FieldDescriptorJDONature.class.getName();
        FieldDescriptor[] fieldDscs = clsDesc.getFields();
        for (int i = 0; i < fieldDscs.length; i++) {
            // fieldDescriptors[i] is persistent in db if it is not transient
            // and it has a JDOFieldDescriptor or has a ClassDescriptor
            if (fieldDscs[i].isTransient()
                    || !(fieldDscs[i].hasNature(fldDescJdoNatureName))
                    && (fieldDscs[i].getClassDescriptor() == null)) { continue; }

            ClassDescriptor related = fieldDscs[i].getClassDescriptor();
            if (related != null) {
                if (!(related.hasNature(ClassDescriptorJDONature.class.getName()))) {
                    throw new MappingException("Related class is not JDOClassDescriptor");
                }

                FieldDescriptor[] relids = ((ClassDescriptorImpl) related).getIdentities();
                String[] names = constructIdNames(relids);

                FieldDescriptor[] classids = ((ClassDescriptorImpl) clsDesc).getIdentities();
                String[] classnames = constructIdNames(classids);

                if (!(fieldDscs[i].hasNature(fldDescJdoNatureName))) {
                    // needed when there are fields without given columns (test 89)
                    TableInfo table = getTableInfo(related, tblMap);

                    TableLink tblLnk = new TableLink(table, TableLink.MANY_KEY,
                            table.getTableName() + "_f" + i, _primaryKey.getColumns(), _fldIndex);
                    _fks.add(tblLnk);
                    tblLnk.setManyKey(Arrays.asList(classnames));
                } else {
                    FieldDescriptorJDONature jdoFieldNature = 
                        new FieldDescriptorJDONature(fieldDscs[i]);

                    String[] tempNames = jdoFieldNature.getSQLName();
                    if ((tempNames != null) && (tempNames.length != relids.length)) {
                        throw new MappingException("The number of columns of foreign key "
                                + "does not match with what specified in manyKey");
                    }
                    names = (tempNames != null) ? tempNames : names;

                    String[] joinFields = jdoFieldNature.getManyKey();
                    if ((joinFields != null) && (joinFields.length != classnames.length)) {
                        throw new MappingException("The number of columns of foreign key "
                                + "does not match with what specified in manyKey");
                    }
                    joinFields = (joinFields != null) ? joinFields : classnames;

                    if (jdoFieldNature.getManyTable() != null) {
                        TableInfo table = new TableInfo(jdoFieldNature.getManyTable());
                        TableLink tblLnk = new TableLink(table, TableLink.MANY_TABLE,
                                table.getTableName() + "_f" + i, _primaryKey.getColumns(), _fldIndex);
                        _fks.add(tblLnk);

                        // add normal columns
                        for (String name : Arrays.asList(names)) {
                            table.addCol(new ColInfo(name));
                        }

                        // add target columns
                        for (String join : Arrays.asList(joinFields)) {
                            tblLnk.addTargetCol(new ColInfo(join));
                        }
                    } else if (jdoFieldNature.getSQLName() != null) {
                        // 1:1 relation
                        boolean store = (_extendedTable == null) && !jdoFieldNature.isReadonly();
                        ArrayList<ColInfo> columns = new ArrayList<ColInfo>();
                        for (int j = 0; j < relids.length; j++) {
                            if (!(relids[j].hasNature(fldDescJdoNatureName))) {
                                throw new MappingException("Related class identities field does"
                                        + " not contains sql information!");
                            }

                            FieldDescriptor relId = relids[j];
                            FieldHandlerImpl fh = (FieldHandlerImpl) relId.getHandler();

                            columns.add(new ColInfo(names[j],
                                    new FieldDescriptorJDONature(relId).getSQLType()[0],
                                    fh.getConvertTo(), fh.getConvertFrom(), store, _fldIndex,
                                    jdoFieldNature.isDirtyCheck(), false));
                        }
                        TableInfo table = getTableInfo(related, tblMap);
                        TableLink tblLnk = new TableLink(table, TableLink.SIMPLE,
                                table.getTableName() + "_f" + i, columns, _fldIndex);
                        _fks.add(tblLnk);
                        tblLnk.addTargetCols(table.getPrimaryKey().getColumns());
                    } else {
                        TableInfo table = getTableInfo(related, tblMap);
                        TableLink tblLnk = new TableLink(table, TableLink.MANY_KEY,
                                table.getTableName() + "_f" + i, _primaryKey.getColumns(), _fldIndex);
                        _fks.add(tblLnk);

                        tblLnk.setManyKey(Arrays.asList(joinFields));
                    }
                }
            } else {
                FieldDescriptorJDONature jdoFieldNature =
                    new FieldDescriptorJDONature(fieldDscs[i]);
                boolean store = (_extendedTable == null) && !jdoFieldNature.isReadonly();
                boolean dirtyCheck = jdoFieldNature.isDirtyCheck();

                String sqlName = fieldDscs[i].getFieldName();
                if (jdoFieldNature.getSQLName() != null) {
                    sqlName = jdoFieldNature.getSQLName()[0];
                }

                FieldHandlerImpl fh = (FieldHandlerImpl) fieldDscs[i].getHandler();
                addCol(new ColInfo (sqlName, jdoFieldNature.getSQLType()[0], fh.getConvertTo(),
                        fh.getConvertFrom(), store, _fldIndex, dirtyCheck, false));
            }
            _fldIndex++;
        }
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Method checks if table for given classDescriptor exists. If there is one it will be returned
     * otherwise a new Table will be constructed for this classDescriptor.
     * 
     * @param clsDesc ClassDescriptor to search table for.
     * @param tblMap Map holding classDescriptors with corresponding tables.
     * @return Existing table from the map or a new one.
     * @throws MappingException Error thrown when construction of new table fails.
     */
    private TableInfo getTableInfo(final ClassDescriptor clsDesc,
            final Map<ClassDescriptor, TableInfo> tblMap) throws MappingException {
        if (tblMap.containsKey(clsDesc)) {
            return tblMap.get(clsDesc);
        }

        return new TableInfo(clsDesc, tblMap);
    }

    /**
     * Method constructing array of the sqlNames of the belonging columns.
     * 
     * @param fieldDesc FieldDescriptor to get names from.
     * @return Array of the names of the columns.
     * @throws MappingException If an error occurs.
     */
    private String[] constructIdNames(final FieldDescriptor[] fieldDesc) throws MappingException {
        String[] names = new String[fieldDesc.length];
        for (int j = 0; j < fieldDesc.length; j++) {
            names[j] = new FieldDescriptorJDONature(fieldDesc[j]).getSQLName()[0];
            if (names[j] == null) {
                throw new MappingException("Related class identities field does "
                        + "not contain sql information!");
            }
        }

        return names;
    }

    /**
     * Method adjusting tableLinks in the correct manner. This method has to be executed
     * right after the complete table-hierarchy has been built.
     * It is needed because in some cases during construction of the hierarchy not all needed
     * columns will be already created when needed.
     */
    public void adjustTableLinks() {
        for (TableLink tblLnk : _fks) {
            if (TableLink.MANY_KEY == tblLnk.getRelationType()) {
                for (ColInfo col : tblLnk.getTargetTable().iterateAll()) {
                    for (String key : tblLnk.getManyKey()) {
                        if (key.equals(col.getName())) {
                            tblLnk.addTargetCol(col);
                        }
                    }
                }

                if (tblLnk.getTargetCols().isEmpty()) {
                    for (TableLink tblLink : tblLnk.getTargetTable().getFkColumns()) {
                        if (tblLink.getTargetTable().equals(this) && tblLink.getManyKey() != null) {
                            for (ColInfo col : tblLink.getStartCols()) {
                                for (String key : tblLink.getManyKey()) {
                                    if (key.equals(col.getName())) {
                                        tblLnk.addTargetCols(tblLink.getStartCols());
                                    }
                                }
                            }
                        }
                    }
                }

                // needed when many key exists in a table but there is no reference specified
                // in the target-table pointing back (test 2996)
                if (tblLnk.getTargetCols().isEmpty()) {
                    for (String key : tblLnk.getManyKey()) {
                        ColInfo col = new ColInfo(key);
                        tblLnk.getTargetTable().addCol(col);
                        tblLnk.addTargetCol(col);
                    }
                }
            }
        }
    }

    /**
     * Method returning list of all columns belonging to this table.
     * 
     * @return List of collected columns.
     */
    public List<ColInfo> iterateAll() {
        List<ColInfo> cols = new ArrayList<ColInfo>();
        cols.addAll(_columns);
        
        for (TableLink lnk : _fks) {
            for (ColInfo col : lnk.getStartCols()) {
                if (!cols.contains(col)) {
                    cols.add(col);
                }
            }
        }

        return cols;
    }

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param input Identity containing values to be assigned to corresponding columns.
     * @return ArrayList containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Identity input) {
        List<ColumnValue> values = new ArrayList<ColumnValue>();

        for (int i = 0; i < _primaryKey.getColumns().size(); i++) {
            values.add(new ColumnValue(_primaryKey.getColumns().get(i), input.get(i)));
        }

        return values;
    }

    /**
     * Method appending values from passed identity to corresponding columns.
     * 
     * @param input Identity containing values to be assigned to corresponding columns.
     * @return ArrayList containing all columns with their corresponding values.
     */
    public List<ColumnValue> toSQL(final Object[] input) {
        List<ColumnValue> values = new ArrayList<ColumnValue>();

        for (ColInfo column : _columns) {
            if (!column.isPrimaryKey()) {
                values.add(new ColumnValue(column));
            }
        }

        for (TableLink lnk : _fks) {
            for (ColInfo col : lnk.getStartCols()) {
                if (!values.contains(col)) {
                    if (col.getFieldIndex() == -1) {
                        // index of foreign key columns has to be taken from tableLink
                        // because the fields in this case have to use other fieldindexes
                        // than in their tables.
                        col.setFieldIndex(lnk.getFieldIndex());
                    }
                    values.add(new ColumnValue(col));
                }
            }
        }

        int counter = 0;
        for (int i = 0; i < _fldIndex; ++i) {
            Object inpt = input[i];
            if (inpt == null) {
                // append 'is NULL' in case the value is null
                while (counter < values.size()
                        && i == values.get(counter).getFieldIndex()) {
                    values.get(counter).setValue(null);
                    counter++;
                }
            } else if (inpt instanceof Identity) {
                Identity identity = (Identity) inpt;

                int indx = 0;
                while (counter < values.size()
                        && i == values.get(counter).getFieldIndex()) {
                    if (identity.get(indx) != null) {
                        values.get(counter).setValue(identity.get(indx));
                    }
                    indx++;
                    counter++;
                }

                if (identity.size() != indx) {
                    throw new PersistenceException("Size of identity field mismatch!");
                }
            } else {
                while (counter < values.size()
                        && i == values.get(counter).getFieldIndex()) {
                    if (!(inpt instanceof Collection)) {
                        values.get(counter).setValue(inpt);
                    }
                    counter++;
                }
            }
        }

        return values;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Method returning extendedTable currently set.
     * 
     * @return ExtendedTable currently set.
     */
    public TableInfo getExtendedTable() { return _extendedTable; }

    /**
     * Method to add a single column to the columns list.
     * 
     * @param column Column to be added.
     */
    private void addCol(final ColInfo column) {
        _columns.add(column);
        
        if (column.isPrimaryKey()) {
            _primaryKey.addColumn(column);
        }
    }

    /**
     * Method returning columns currently set.
     * 
     * @return List of columns currently set.
     */
    public List<ColInfo> getColumns() { return _columns; }

    /**
     * Method returning list of foreign keys.
     * 
     * @return List of foreign keys.
     */
    public List<TableLink> getFkColumns() { return _fks; }

    /**
     * Get primary key of the table.
     * 
     * @return Primary key of the table.
     */
    public PrimaryKeyInfo getPrimaryKey() { return _primaryKey; }

    /**
     * Method returning name of this table.
     * 
     * @return Name of the table currently set.
     */
    public String getTableName() { return _tableName; }

    /**
     * Method returning list of tables extending this one.
     * 
     * @return List of extending tables.
     */
    public List<TableInfo> getExtendingTables() { return _extendingTables; }

    //-----------------------------------------------------------------------------------    
}

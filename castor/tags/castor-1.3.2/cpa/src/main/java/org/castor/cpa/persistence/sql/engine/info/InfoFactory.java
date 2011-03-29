package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;

public final class InfoFactory {
    //-----------------------------------------------------------------------------------    

	private final Map<String, TableInfo> _entityMap = new HashMap<String, TableInfo>();
    
    //-----------------------------------------------------------------------------------    

	/**
     * Method checks if table for given class descriptor exists. If there is one it will be
     * returned otherwise a new table will be constructed for this class descriptor.
     * 
     * @param classDescriptor ClassDescriptor to search table for.
     * @return Existing table from the map or a new one.
     * @throws MappingException Error thrown when construction of new table fails.
     */
    public TableInfo createTableInfo(final ClassDescriptor classDescriptor)
    throws MappingException {
    	String name = classDescriptor.getJavaClass().getName();
        TableInfo table = _entityMap.get(name);
        if (table == null) {
            if (!classDescriptor.hasNature(ClassDescriptorJDONature.class.getName())) {
                throw new MappingException("ClassDescriptor is not a JDOClassDescriptor");
            }
            ClassDescriptorJDONature classNature = new ClassDescriptorJDONature(classDescriptor);

            table = new TableInfo(classNature.getTableName());
            
            _entityMap.put(name, table);

            // set extended table if exists
            if (classDescriptor.getExtends() != null) {
                table.setExtendedTable(createTableInfo(classDescriptor.getExtends()));
            }

            // add all extending tables if there is one
            for (ClassDescriptor extending : classNature.getExtended()) {
                table.addExtendingTable(createTableInfo(extending));
            }

            // first we have to add the primary keys of this class.
            resolvePrimaryKeys(classDescriptor, table);

            // then we have to add the other columns, such as foreign keys and normal columns.
            resolveColumns(classDescriptor, table);
        }
        return table;
    }
    
    private void resolvePrimaryKeys(final ClassDescriptor classDescriptor, final TableInfo table)
    throws MappingException {
    	for (FieldDescriptor selfFD: ((ClassDescriptorImpl) classDescriptor).getIdentities()) {
            if (!selfFD.hasNature(FieldDescriptorJDONature.class.getName())) {
                throw new MappingException("Excepted JDOFieldDescriptor");
            }
            FieldDescriptorJDONature selfFN = new FieldDescriptorJDONature(selfFD);
            String sqlName = selfFN.getSQLName()[0];
            int selfType = selfFN.getSQLType()[0];
            TypeConvertor convFrom = ((FieldHandlerImpl) selfFD.getHandler()).getConvertFrom();

            ColumnInfo column = new ColumnInfo(sqlName, -1, selfType, convFrom, false, false);
            table.getPrimaryKey().addColumn(column);
    	}
    }
    
    private void resolveColumns(final ClassDescriptor selfCD, final TableInfo table)
    throws MappingException {
        FieldDescriptor[] selfFDs = selfCD.getFields();
        int persIndex = 0;
        for (int fieldIndex = 0; fieldIndex < selfFDs.length; fieldIndex++) {
        	FieldDescriptor selfFD = selfFDs[fieldIndex];
        	
            // field is persistent if it is not transient
            if (selfFD.isTransient()) { continue; }
            
            // field is persistent if it has a JDOFieldDescriptor or has a ClassDescriptor
            if (!selfFD.hasNature(FieldDescriptorJDONature.class.getName())
                    && (selfFD.getClassDescriptor() == null)) { continue; }

            ClassDescriptor referedCD = selfFD.getClassDescriptor();
            if (referedCD != null) {
                if (!(referedCD.hasNature(ClassDescriptorJDONature.class.getName()))) {
                    throw new MappingException("Related class is not a JDOClassDescriptor");
                }
                
                int mode = TableLink.REFERED_BY;
                String[] backCols = null;
                String[] referedCols = null;
                String manyTableName = null;
                boolean isStore = false;
                boolean isDirtyCheck = false;

                if (selfFD.hasNature(FieldDescriptorJDONature.class.getName())) {
                    FieldDescriptorJDONature selfFN = new FieldDescriptorJDONature(selfFD);

                    if (selfFN.getManyTable() != null) {
                    	mode = TableLink.MANY_TO_MANY;
                    } else if (selfFN.getSQLName() != null) {
                    	mode = TableLink.REFERS_TO;
                    } else {
                    	mode = TableLink.REFERED_BY;
                    }
                    
                    backCols = selfFN.getManyKey();
                    referedCols = selfFN.getSQLName();
                    manyTableName = selfFN.getManyTable();
                    isStore = (table.getExtendedTable() == null) && !selfFN.isReadonly();
                    isDirtyCheck = selfFN.isDirtyCheck();
                }

                FieldDescriptor[] selfIDs = ((ClassDescriptorImpl) selfCD).getIdentities();
                if (backCols == null) {
                	backCols = getSQLNames(selfIDs);
                } else {
                    if (backCols.length != selfIDs.length) {
                        throw new MappingException("The number of columns of foreign key "
                                + "does not match with primary key of current class");
                    }
                }

                FieldDescriptor[] referedIDs = ((ClassDescriptorImpl) referedCD).getIdentities();
                if (referedCols == null) {
                	referedCols = getSQLNames(referedIDs);
                } else {
                    if (referedCols.length != referedIDs.length) {
                        throw new MappingException("The number of columns of foreign key "
                                + "does not match with primary key of related class");
                    }
                }

                if (mode == TableLink.MANY_TO_MANY) {
              	    // many to many relation
                    TableInfo manyTable = new TableInfo(manyTableName);
                    TableLink foreignKey = new TableLink(manyTable, mode,
                            manyTable.getTableName() + "_f" + fieldIndex,
                            table.getPrimaryKey().getColumns(), persIndex);
                    table.addForeignKey(foreignKey);

                    // add normal columns
                    for (String referedCol : referedCols) {
                        manyTable.addColumn(new ColumnInfo(referedCol));
                    }

                    // add target columns
                    for (String backCol : backCols) {
                        foreignKey.addTargetCol(new ColumnInfo(backCol));
                    }
                } else if (mode == TableLink.REFERS_TO) {
                    // refers to one
                    ArrayList<ColumnInfo> columns = new ArrayList<ColumnInfo>();
                    for (int j = 0; j < referedIDs.length; j++) {
                        if (!(referedIDs[j].hasNature(FieldDescriptorJDONature.class.getName()))) {
                            throw new MappingException("Related class identities field does"
                                    + " not contains sql information!");
                        }

                        FieldDescriptor relId = referedIDs[j];
                        FieldHandlerImpl fh = (FieldHandlerImpl) relId.getHandler();

                        columns.add(new ColumnInfo(referedCols[j], persIndex,
                                new FieldDescriptorJDONature(relId).getSQLType()[0],
                                fh.getConvertFrom(), isStore, isDirtyCheck));
                    }
                    TableInfo relatedTable = createTableInfo(referedCD);
                    TableLink foreignKey = new TableLink(relatedTable, mode,
                            relatedTable.getTableName() + "_f" + fieldIndex, columns, persIndex);
                    table.addForeignKey(foreignKey);
                    foreignKey.addTargetCols(relatedTable.getPrimaryKey().getColumns());
                } else {
                	// refered by one or many 
                    TableInfo relatedTable = createTableInfo(referedCD);
                    TableLink foreignKey = new TableLink(relatedTable, mode,
                            relatedTable.getTableName() + "_f" + fieldIndex,
                            table.getPrimaryKey().getColumns(), persIndex);
                    table.addForeignKey(foreignKey);
                    foreignKey.setManyKey(Arrays.asList(backCols));
                }
            } else {
            	// simple field
                FieldDescriptorJDONature selfFN = new FieldDescriptorJDONature(selfFD);

                String sqlName = selfFD.getFieldName();
                if (selfFN.getSQLName() != null) {
                    sqlName = selfFN.getSQLName()[0];
                }

                int selfType = selfFN.getSQLType()[0];
                TypeConvertor convFrom = ((FieldHandlerImpl) selfFD.getHandler()).getConvertFrom();
                boolean isStore = (table.getExtendedTable() == null) && !selfFN.isReadonly();
                boolean isDirtyCheck = selfFN.isDirtyCheck();

                ColumnInfo column = new ColumnInfo(sqlName, persIndex, selfType, convFrom,
                		isStore, isDirtyCheck);
                table.addColumn(column);
            }
            
            persIndex++;
        }
    }
    
    /**
     * Method constructing array of the sqlNames of the belonging columns.
     * 
     * @param fieldDesc FieldDescriptor to get names from.
     * @return Array of the names of the columns.
     * @throws MappingException If an error occurs.
     */
    private String[] getSQLNames(final FieldDescriptor[] fieldDesc) throws MappingException {
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

    //-----------------------------------------------------------------------------------    

    public void resolveForeignKeys() {
        for (TableInfo table : _entityMap.values()) {
            adjustTableLinks(table);
        }
    }

    /**
     * Method adjusting tableLinks in the correct manner. This method has to be executed
     * right after the complete table-hierarchy has been built.
     * It is needed because in some cases during construction of the hierarchy not all needed
     * columns will be already created when needed.
     */
    private void adjustTableLinks(final TableInfo table) {
        for (TableLink foreignKey : table.getForeignKeys()) {
            if (TableLink.REFERED_BY == foreignKey.getRelationType()) {
                for (ColumnInfo col : foreignKey.getTargetTable().iterateAll()) {
                    for (String key : foreignKey.getManyKey()) {
                        if (key.equals(col.getName())) {
                            foreignKey.addTargetCol(col);
                        }
                    }
                }

                if (foreignKey.getTargetCols().isEmpty()) {
                    for (TableLink tblLink : foreignKey.getTargetTable().getForeignKeys()) {
                        if (tblLink.getTargetTable().equals(this) && tblLink.getManyKey() != null) {
                            for (ColumnInfo col : tblLink.getStartCols()) {
                                for (String key : tblLink.getManyKey()) {
                                    if (key.equals(col.getName())) {
                                        foreignKey.addTargetCols(tblLink.getStartCols());
                                    }
                                }
                            }
                        }
                    }
                }

                // needed when many key exists in a table but there is no reference specified
                // in the target-table pointing back (test 2996)
                if (foreignKey.getTargetCols().isEmpty()) {
                    for (String key : foreignKey.getManyKey()) {
                        ColumnInfo col = new ColumnInfo(key);
                        foreignKey.getTargetTable().addColumn(col);
                        foreignKey.addTargetCol(col);
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------    
}

package org.castor.cpa.persistence.sql.engine.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final Map<String, EntityTableInfo> _entityMap = new HashMap<String, EntityTableInfo>();
    
    //-----------------------------------------------------------------------------------    

    /**
     * Method checks if table for given class descriptor exists. If there is one it will be
     * returned otherwise a new table will be constructed for this class descriptor.
     * 
     * @param classDescriptor ClassDescriptor to search table for.
     * @return Existing table from the map or a new one.
     * @throws MappingException Error thrown when construction of new table fails.
     */
    public EntityTableInfo createTableInfo(final ClassDescriptor classDescriptor)
    throws MappingException {
        String name = classDescriptor.getJavaClass().getName();
        EntityTableInfo table = _entityMap.get(name);
        if (table == null) {
            if (!classDescriptor.hasNature(ClassDescriptorJDONature.class.getName())) {
                throw new MappingException("ClassDescriptor is not a JDOClassDescriptor");
            }
            ClassDescriptorJDONature classNature = new ClassDescriptorJDONature(classDescriptor);

            table = new EntityTableInfo(classNature.getTableName());
            
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
    
    private void resolvePrimaryKeys(final ClassDescriptor classDescriptor, final EntityTableInfo table)
    throws MappingException {
        for (FieldDescriptor selfFD : ((ClassDescriptorImpl) classDescriptor).getIdentities()) {
            if (!selfFD.hasNature(FieldDescriptorJDONature.class.getName())) {
                throw new MappingException("Excepted JDOFieldDescriptor");
            }
            FieldDescriptorJDONature selfFN = new FieldDescriptorJDONature(selfFD);
            String sqlName = selfFN.getSQLName()[0];
            int selfType = selfFN.getSQLType()[0];
            TypeConvertor convFrom = ((FieldHandlerImpl) selfFD.getHandler()).getConvertFrom();

            table.addPrimaryKeyColumn(sqlName, selfType, convFrom);
        }
    }
    
    private void resolveColumns(final ClassDescriptor selfCD, final EntityTableInfo table)
    throws MappingException {
        int fieldIndex = 0;
        for (FieldDescriptor selfFD : selfCD.getFields()) {
            // field is persistent if it is not transient
            if (!selfFD.isTransient()) {
                ClassDescriptor referedCD = selfFD.getClassDescriptor();

                FieldDescriptorJDONature selfFN = null;
                if (selfFD.hasNature(FieldDescriptorJDONature.class.getName())) {
                    selfFN = new FieldDescriptorJDONature(selfFD);
                }
                
                if (referedCD != null) {
                    if (!(referedCD.hasNature(ClassDescriptorJDONature.class.getName()))) {
                        throw new MappingException("Related class is not a JDOClassDescriptor");
                    }
                    
                    if (selfFN == null) {
                        resolveForeignReference(selfCD, selfFD, table, fieldIndex);
                    } else if (selfFN.getManyTable() != null) {
                        resolveManyToMany(selfCD, selfFD, table, fieldIndex);
                    } else if (selfFN.getSQLName() != null) {
                        resolveForeignKey(selfFD, table, fieldIndex);
                    } else {
                        resolveForeignReference(selfCD, selfFD, table, fieldIndex);
                    }
                    fieldIndex++;
                } else if (selfFN != null) {
                    resolveSimpleColumn(selfFD, table, fieldIndex);
                    fieldIndex++;
                }
            }
        }
    }
    
    private void resolveSimpleColumn(final FieldDescriptor fieldDescriptor,
            final EntityTableInfo table, final int fieldIndex) {
        FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);

        boolean isExtended = (table.getExtendedTable() != null);
        boolean isStore = !fieldNature.isReadonly() && !isExtended;
        boolean isDirtyCheck = fieldNature.isDirtyCheck();

        String columnName = fieldDescriptor.getFieldName();
        if (fieldNature.getSQLName() != null) {
            columnName = fieldNature.getSQLName()[0];
        }
        
        int columnType = fieldNature.getSQLType()[0];
        FieldHandlerImpl columnHandler = (FieldHandlerImpl) fieldDescriptor.getHandler();

        table.addSimpleColumn(fieldIndex, columnName, columnType,
                columnHandler.getConvertFrom(), isStore, isDirtyCheck);
    }

    private void resolveForeignKey(final FieldDescriptor fieldDescriptor,
            final EntityTableInfo table, final int fieldIndex)
    throws MappingException {
        FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);
        
        ClassDescriptor referedCD = fieldDescriptor.getClassDescriptor();
        EntityTableInfo referedTable = createTableInfo(referedCD);

        ForeignKeyInfo foreignKey = new ForeignKeyInfo(fieldIndex, table, referedTable,
                referedTable.getTableName() + "_f" + fieldIndex);
        
        boolean isExtended = (table.getExtendedTable() != null);
        boolean isStore = !fieldNature.isReadonly() && !isExtended;
        boolean isDirtyCheck = fieldNature.isDirtyCheck();

        String[] columnNames = fieldNature.getSQLName();
        List<ColumnInfo> columns = createColumnInfos(fieldIndex,
                referedCD, columnNames, isStore, isDirtyCheck);
        for (ColumnInfo column : columns) {
            foreignKey.addFromColumn(column);
        }
        
        table.addForeignKey(foreignKey);
    }
    
    private void resolveForeignReference(final ClassDescriptor classDescriptor,
            final FieldDescriptor fieldDescriptor, final EntityTableInfo table,
            final int fieldIndex)
    throws MappingException {
        ClassDescriptor referingCD = fieldDescriptor.getClassDescriptor();
        EntityTableInfo referingTable = createTableInfo(referingCD);

        ForeignReferenceInfo foreignReference = new ForeignReferenceInfo(fieldIndex, table,
                referingTable, referingTable.getTableName() + "_f" + fieldIndex);
        
        String[] columnNames = null;
        if (fieldDescriptor.hasNature(FieldDescriptorJDONature.class.getName())) {
            FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);
            columnNames = fieldNature.getManyKey();
        }
        List<ColumnInfo> columns = createColumnInfos(fieldIndex,
                classDescriptor, columnNames, false, false);
        for (ColumnInfo column : columns) {
            foreignReference.addFromColumn(column);
        }
        
        table.addForeignReference(foreignReference);
    }
    
    private void resolveManyToMany(final ClassDescriptor classDescriptor,
            final FieldDescriptor fieldDescriptor, final EntityTableInfo table,
            final int fieldIndex)
    throws MappingException {
        FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);
        
        ClassDescriptor referedCD = fieldDescriptor.getClassDescriptor();
        EntityTableInfo referedTable = createTableInfo(referedCD);

        String relationTableName = fieldNature.getManyTable();
        RelationTableInfo relationTable = new RelationTableInfo(relationTableName);

        ForeignReferenceInfo manyToMany = new ForeignReferenceInfo(fieldIndex, table,
                relationTable, relationTable.getTableName() + "_f" + fieldIndex);

        String[] columnNames = fieldNature.getManyKey();
        List<ColumnInfo> columns = createColumnInfos(fieldIndex,
                classDescriptor, columnNames, false, false);
        for (ColumnInfo column : columns) {
            manyToMany.addFromColumn(column);
        }
        
        ForeignKeyInfo leftForeignKey = new ForeignKeyInfo(0, relationTable, table,
                table.getTableName() + "_f" + 0);
        relationTable.setLeftForeignKey(leftForeignKey);
        
        String[] leftColumnNames = fieldNature.getManyKey();
        List<ColumnInfo> leftColumns = createColumnInfos(0,
                classDescriptor, leftColumnNames, false, false);
        for (ColumnInfo column : leftColumns) {
            leftForeignKey.addFromColumn(column);
        }

        ForeignKeyInfo rightForeignKey = new ForeignKeyInfo(1, relationTable, referedTable,
                referedTable.getTableName() + "_f" + 1);
        relationTable.setRightForeignKey(rightForeignKey);
        
        String[] rightColumnNames = fieldNature.getSQLName();
        List<ColumnInfo> rightColumns = createColumnInfos(1,
                referedCD, rightColumnNames, false, false);
        for (ColumnInfo column : rightColumns) {
            rightForeignKey.addFromColumn(column);
        }
        
        table.addForeignReference(manyToMany);
    }
    
    private List<ColumnInfo> createColumnInfos(final int fieldIndex,
            final ClassDescriptor classDescriptor, final String[] columnNames,
            final boolean store, final boolean dirty)
    throws MappingException {
        FieldDescriptor[] ids = ((ClassDescriptorImpl) classDescriptor).getIdentities();

        if ((columnNames != null) && (columnNames.length != ids.length)) {
            throw new MappingException("The number of columns of foreign key "
                    + "does not match with primary key of refered class");
        }

        List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
        
        for (int j = 0; j < ids.length; j++) {
            FieldDescriptor id = ids[j];
            FieldDescriptorJDONature nature = new FieldDescriptorJDONature(id);

            String columnName = null;
            if (columnNames != null) {
                columnName = columnNames[j];
            } else {
                columnName = nature.getSQLName()[0];
                if (columnName == null) {
                    throw new MappingException("Related class identities field does "
                            + "not contain sql information!");
                }
            }
            
            int columnType = nature.getSQLType()[0];
            FieldHandlerImpl columnHandler = (FieldHandlerImpl) id.getHandler();

            columns.add(new ColumnInfo(fieldIndex, columnName, columnType,
                    columnHandler.getConvertFrom(), false, store, dirty));
        }
        
        return columns;
    }
    
    //-----------------------------------------------------------------------------------    
}

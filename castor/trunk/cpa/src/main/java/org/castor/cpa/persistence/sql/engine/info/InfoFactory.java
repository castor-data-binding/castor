package org.castor.cpa.persistence.sql.engine.info;

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
        int columnIndex = 0;
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
                        resolveForeignReference(selfCD, selfFD, table, columnIndex, fieldIndex);
                    } else if (selfFN.getManyTable() != null) {
                        resolveManyToMany(selfCD, selfFD, table, columnIndex, fieldIndex);
                    } else if (selfFN.getSQLName() != null) {
                        resolveForeignKey(selfFD, table, columnIndex, fieldIndex);
                    } else {
                        resolveForeignReference(selfCD, selfFD, table, columnIndex, fieldIndex);
                    }
                    columnIndex++;
                } else if (selfFN != null) {
                    resolveSimpleColumn(selfFD, table, columnIndex);
                    columnIndex++;
                }
            }
            fieldIndex++;
        }
    }
    
    private void resolveSimpleColumn(final FieldDescriptor fieldDescriptor,
            final EntityTableInfo table, final int columnIndex) {
        FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);

        boolean isExtended = (table.getExtendedTable() != null);
        boolean isStore = !fieldNature.isReadonly() && !isExtended;
        boolean isDirtyCheck = fieldNature.isDirtyCheck();

        String columnName = fieldDescriptor.getFieldName();
        if (fieldNature.getSQLName() != null) {
            columnName = fieldNature.getSQLName()[0];
        }
        
        int columnType = fieldNature.getSQLType()[0];
        FieldHandlerImpl fh = (FieldHandlerImpl) fieldDescriptor.getHandler();

        table.addSimpleColumn(columnName, columnIndex, columnType, fh.getConvertFrom(),
                isStore, isDirtyCheck);
    }

    private void resolveForeignKey(final FieldDescriptor fieldDescriptor,
            final EntityTableInfo table, final int columnIndex, final int fieldIndex)
    throws MappingException {
        FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);
        
        ClassDescriptor referedCD = fieldDescriptor.getClassDescriptor();
        EntityTableInfo referedTable = createTableInfo(referedCD);

        ForeignKeyInfo foreignKey = new ForeignKeyInfo(table, referedTable,
                referedTable.getTableName() + "_f" + fieldIndex);
        
        FieldDescriptor[] referedIds = ((ClassDescriptorImpl) referedCD).getIdentities();

        boolean isExtended = (table.getExtendedTable() != null);
        boolean isStore = !fieldNature.isReadonly() && !isExtended;
        boolean isDirtyCheck = fieldNature.isDirtyCheck();
        
        String[] columnNames = fieldNature.getSQLName();
        if ((columnNames != null) && (columnNames.length != referedIds.length)) {
            throw new MappingException("The number of columns of foreign key "
                    + "does not match with primary key of refered class");
        }
        
        for (int j = 0; j < referedIds.length; j++) {
            FieldDescriptor referedID = referedIds[j];
            FieldDescriptorJDONature referedNature = new FieldDescriptorJDONature(referedID);

            String columnName = null;
            if (columnNames != null) {
                columnName = columnNames[j];
            } else {
                columnName = referedNature.getSQLName()[0];
                if (columnName == null) {
                    throw new MappingException("Related class identities field does "
                            + "not contain sql information!");
                }
            }
            
            int columnType = referedNature.getSQLType()[0];
            FieldHandlerImpl columnHandler = (FieldHandlerImpl) referedID.getHandler();

            foreignKey.addFromColumn(columnName, columnIndex, columnType,
                    columnHandler.getConvertFrom(), isStore, isDirtyCheck);
        }

        table.addForeignKey(foreignKey);
    }
    
    private void resolveForeignReference(final ClassDescriptor classDescriptor,
            final FieldDescriptor fieldDescriptor, final EntityTableInfo table,
            final int columnIndex, final int fieldIndex)
    throws MappingException {
        ClassDescriptor referingCD = fieldDescriptor.getClassDescriptor();
        EntityTableInfo referingTable = createTableInfo(referingCD);

        ForeignReferenceInfo foreignReference = new ForeignReferenceInfo(table, referingTable,
                referingTable.getTableName() + "_f" + fieldIndex);
        
        FieldDescriptor[] ids = ((ClassDescriptorImpl) classDescriptor).getIdentities();

        String[] columnNames = null;
        if (fieldDescriptor.hasNature(FieldDescriptorJDONature.class.getName())) {
            FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);
            columnNames = fieldNature.getManyKey();
        }
        if ((columnNames != null) && (columnNames.length != ids.length)) {
            throw new MappingException("The number of columns of foreign key "
                    + "does not match with primary key of refered class");
        }

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
            
            foreignReference.addFromColumn(columnName, columnIndex, columnType,
                    columnHandler.getConvertFrom());
        }
        
        table.addForeignReference(foreignReference);
    }
    
    private void resolveManyToMany(final ClassDescriptor classDescriptor,
            final FieldDescriptor fieldDescriptor, final EntityTableInfo table,
            final int columnIndex, final int fieldIndex)
    throws MappingException {
        FieldDescriptorJDONature fieldNature = new FieldDescriptorJDONature(fieldDescriptor);
        
        ClassDescriptor referedCD = fieldDescriptor.getClassDescriptor();
        EntityTableInfo referedTable = createTableInfo(referedCD);

        String relationTableName = fieldNature.getManyTable();
        RelationTableInfo relationTable = new RelationTableInfo(relationTableName);

        ManyToMany manyToMany = new ManyToMany(table, relationTable,
                relationTable.getTableName() + "_f" + fieldIndex);

        ForeignKeyInfo leftForeignKey = new ForeignKeyInfo(relationTable, table,
                table.getTableName() + "_f" + 0);
        relationTable.setLeftForeignKey(leftForeignKey);
        
        ForeignKeyInfo rightForeignKey = new ForeignKeyInfo(relationTable, referedTable,
                referedTable.getTableName() + "_f" + 1);
        relationTable.setRightForeignKey(rightForeignKey);
        
        FieldDescriptor[] ids = ((ClassDescriptorImpl) classDescriptor).getIdentities();

        String[] leftColumnNames = fieldNature.getManyKey();
        if ((leftColumnNames != null) && (leftColumnNames.length != ids.length)) {
            throw new MappingException("The number of columns of foreign key "
                    + "does not match with primary key of refered class");
        }
        
        for (int j = 0; j < ids.length; j++) {
            FieldDescriptor id = ids[j];
            FieldDescriptorJDONature nature = new FieldDescriptorJDONature(id);

            String leftColumnName = null;
            if (leftColumnNames != null) {
                leftColumnName = leftColumnNames[j];
            } else {
                leftColumnName = nature.getSQLName()[0];
                if (leftColumnName == null) {
                    throw new MappingException("Related class identities field does "
                            + "not contain sql information!");
                }
            }
            
            int leftColumnType = nature.getSQLType()[0];
            FieldHandlerImpl leftColumnHandler = (FieldHandlerImpl) id.getHandler();
            
            manyToMany.addFromColumn(leftColumnName, columnIndex, leftColumnType,
                    leftColumnHandler.getConvertFrom());
            
            leftForeignKey.addFromColumn(leftColumnName, 0, leftColumnType,
                    leftColumnHandler.getConvertFrom(), false, false);
        }
        
        FieldDescriptor[] referedIds = ((ClassDescriptorImpl) referedCD).getIdentities();

        String[] rightColumnNames = fieldNature.getSQLName();
        if ((rightColumnNames != null) && (rightColumnNames.length != referedIds.length)) {
            throw new MappingException("The number of columns of foreign key "
                    + "does not match with primary key of refered class");
        }

        for (int j = 0; j < referedIds.length; j++) {
            FieldDescriptor referedID = referedIds[j];
            FieldDescriptorJDONature referedNature = new FieldDescriptorJDONature(referedID);

            String rightColumnName = null;
            if (rightColumnNames != null) {
                rightColumnName = rightColumnNames[j];
            } else {
                rightColumnName = referedNature.getSQLName()[0];
                if (rightColumnName == null) {
                    throw new MappingException("Related class identities field does "
                            + "not contain sql information!");
                }
            }
            
            int rightColumnType = referedNature.getSQLType()[0];
            FieldHandlerImpl rightColumnHandler = (FieldHandlerImpl) referedID.getHandler();

            rightForeignKey.addFromColumn(rightColumnName, 1, rightColumnType,
                    rightColumnHandler.getConvertFrom(), false, false);
        }
        
        table.addManyToMany(manyToMany);
    }
    
    //-----------------------------------------------------------------------------------    
}

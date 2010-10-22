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
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;

public final class InfoFactory {
    //-----------------------------------------------------------------------------------    

	private Map<ClassDescriptor, TableInfo> _map = new HashMap<ClassDescriptor, TableInfo>();

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
        TableInfo table = _map.get(classDescriptor);
        if (table == null) {
            if (!classDescriptor.hasNature(ClassDescriptorJDONature.class.getName())) {
                throw new MappingException("ClassDescriptor is not a JDOClassDescriptor");
            }
            ClassDescriptorJDONature classNature = new ClassDescriptorJDONature(classDescriptor);

            table = new TableInfo(classNature.getTableName());
            
            _map.put(classDescriptor, table);

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
        FieldDescriptor[] ids = ((ClassDescriptorImpl) classDescriptor).getIdentities();
        for (int i = 0; i < ids.length; i++) {
            if (!ids[i].hasNature(FieldDescriptorJDONature.class.getName())) {
                throw new MappingException("Excepted JDOFieldDescriptor");
            }
            String[] sqlName = new FieldDescriptorJDONature(ids[i]).getSQLName();
            int[] sqlType =  new FieldDescriptorJDONature(ids[i]).getSQLType();
            FieldHandlerImpl fh = (FieldHandlerImpl) ids[i].getHandler();

            ColInfo column = new ColInfo(sqlName[0], sqlType[0],
                    fh.getConvertFrom(), false, -1, false, true);
            table.addColumn(column);
        }
    }
    
    private void resolveColumns(final ClassDescriptor classDescriptor, final TableInfo table)
    throws MappingException {
        FieldDescriptor[] fieldDscs = classDescriptor.getFields();
        int fieldIndex = 0;
        for (int i = 0; i < fieldDscs.length; i++) {
            // fieldDescriptors[i] is persistent in db if it is not transient
            // and it has a JDOFieldDescriptor or has a ClassDescriptor
            if (fieldDscs[i].isTransient()
                    || !(fieldDscs[i].hasNature(FieldDescriptorJDONature.class.getName()))
                    && (fieldDscs[i].getClassDescriptor() == null)) { continue; }

            ClassDescriptor related = fieldDscs[i].getClassDescriptor();
            if (related != null) {
                if (!(related.hasNature(ClassDescriptorJDONature.class.getName()))) {
                    throw new MappingException("Related class is not JDOClassDescriptor");
                }

                FieldDescriptor[] relids = ((ClassDescriptorImpl) related).getIdentities();
                String[] names = constructIdNames(relids);

                FieldDescriptor[] ids = ((ClassDescriptorImpl) classDescriptor).getIdentities();
                String[] classnames = constructIdNames(ids);

                if (!(fieldDscs[i].hasNature(FieldDescriptorJDONature.class.getName()))) {
                    // needed when there are fields without given columns (test 89)
                    TableInfo relatedTable = createTableInfo(related);

                    TableLink tblLnk = new TableLink(relatedTable, TableLink.MANY_KEY,
                    		relatedTable.getTableName() + "_f" + i,
                            table.getPrimaryKey().getColumns(), fieldIndex);
                    table.addForeignKey(tblLnk);
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
                        TableInfo manyTable = new TableInfo(jdoFieldNature.getManyTable());
                        TableLink tblLnk = new TableLink(manyTable, TableLink.MANY_TABLE,
                                manyTable.getTableName() + "_f" + i,
                                table.getPrimaryKey().getColumns(), fieldIndex);
                        table.addForeignKey(tblLnk);

                        // add normal columns
                        for (String name : Arrays.asList(names)) {
                            manyTable.addColumn(new ColInfo(name));
                        }

                        // add target columns
                        for (String join : Arrays.asList(joinFields)) {
                            tblLnk.addTargetCol(new ColInfo(join));
                        }
                    } else if (jdoFieldNature.getSQLName() != null) {
                        // 1:1 relation
                        boolean store = (table.getExtendedTable() == null)
                        			  && !jdoFieldNature.isReadonly();
                        ArrayList<ColInfo> columns = new ArrayList<ColInfo>();
                        for (int j = 0; j < relids.length; j++) {
                            if (!(relids[j].hasNature(FieldDescriptorJDONature.class.getName()))) {
                                throw new MappingException("Related class identities field does"
                                        + " not contains sql information!");
                            }

                            FieldDescriptor relId = relids[j];
                            FieldHandlerImpl fh = (FieldHandlerImpl) relId.getHandler();

                            columns.add(new ColInfo(names[j],
                                    new FieldDescriptorJDONature(relId).getSQLType()[0],
                                    fh.getConvertFrom(), store, fieldIndex,
                                    jdoFieldNature.isDirtyCheck(), false));
                        }
                        TableInfo relatedTable = createTableInfo(related);
                        TableLink tblLnk = new TableLink(relatedTable, TableLink.SIMPLE,
                                relatedTable.getTableName() + "_f" + i, columns, fieldIndex);
                        table.addForeignKey(tblLnk);
                        tblLnk.addTargetCols(relatedTable.getPrimaryKey().getColumns());
                    } else {
                        TableInfo relatedTable = createTableInfo(related);
                        TableLink tblLnk = new TableLink(relatedTable, TableLink.MANY_KEY,
                                relatedTable.getTableName() + "_f" + i,
                                table.getPrimaryKey().getColumns(), fieldIndex);
                        table.addForeignKey(tblLnk);
                        tblLnk.setManyKey(Arrays.asList(joinFields));
                    }
                }
            } else {
                FieldDescriptorJDONature jdoFieldNature =
                    new FieldDescriptorJDONature(fieldDscs[i]);
                boolean store = (table.getExtendedTable() == null)
                              && !jdoFieldNature.isReadonly();
                boolean dirtyCheck = jdoFieldNature.isDirtyCheck();

                String sqlName = fieldDscs[i].getFieldName();
                if (jdoFieldNature.getSQLName() != null) {
                    sqlName = jdoFieldNature.getSQLName()[0];
                }

                FieldHandlerImpl fh = (FieldHandlerImpl) fieldDscs[i].getHandler();
                table.addColumn(new ColInfo (sqlName, jdoFieldNature.getSQLType()[0],
                		fh.getConvertFrom(), store, fieldIndex, dirtyCheck, false));
            }
            fieldIndex++;
        }
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

    public void resolveForeignKeys() {
        for (TableInfo table : _map.values()) {
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
        for (TableLink tblLnk : table.getForeignKeys()) {
            if (TableLink.MANY_KEY == tblLnk.getRelationType()) {
                for (ColInfo col : tblLnk.getTargetTable().iterateAll()) {
                    for (String key : tblLnk.getManyKey()) {
                        if (key.equals(col.getName())) {
                            tblLnk.addTargetCol(col);
                        }
                    }
                }

                if (tblLnk.getTargetCols().isEmpty()) {
                    for (TableLink tblLink : tblLnk.getTargetTable().getForeignKeys()) {
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
                        tblLnk.getTargetTable().addColumn(col);
                        tblLnk.addTargetCol(col);
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------    
}

/*
 * Copyright 2005 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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

import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.jdo.engine.nature.FieldDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;

/**
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-01-21 04:05:17 -0700 (Sat, 21 Jan 2006) $
 * @since 1.0
 */
public class SQLFieldInfo {
    private final String _tableName;

    private final boolean _store;

    private final boolean _multi;

    private final boolean _joined;

    private final boolean _dirtyCheck;

    private final String[] _joinFields;

    private final SQLColumnInfo[] _columns;
    
    private final FieldDescriptor _fieldDescriptor;
    
    public SQLFieldInfo(final ClassDescriptor clsDesc,
                        final FieldDescriptor fieldDesc,
                        final String classTable, final boolean ext)
    throws MappingException {
        _fieldDescriptor = fieldDesc;
        
        ClassDescriptor related = fieldDesc.getClassDescriptor();
        if (related != null) {
            if (!(related.hasNature(ClassDescriptorJDONature.class.getName()))) {
                throw new MappingException("Related class is not JDOClassDescriptor");
            }

            FieldDescriptor[] relids = ((ClassDescriptorImpl) related).getIdentities();
            String[] relnames = new String[relids.length];
            for (int i = 0; i < relids.length; i++) {
                relnames[i] = new FieldDescriptorJDONature(relids[i]).getSQLName()[0];
                if (relnames[i] == null) {
                    throw new MappingException("Related class identities field does "
                            + "not contains sql information!");
                }
            }
            
            FieldDescriptor[] classids = ((ClassDescriptorImpl) clsDesc).getIdentities();
            String[] classnames = new String[classids.length];
            for (int i = 0; i < classids.length; i++) {
                classnames[i] = new FieldDescriptorJDONature(classids[i]).getSQLName()[0];
                if (classnames[i] == null) {
                    throw new MappingException("Related class identities field does "
                            + "not contains sql information!");
                }
            }

            String[] names = relnames;
            if (!(fieldDesc.hasNature(FieldDescriptorJDONature.class.getName()))) {
                _tableName = new ClassDescriptorJDONature(related).getTableName();
                _store = false;
                _multi = fieldDesc.isMultivalued();
                _joined = true;
                _joinFields = classnames;
                _dirtyCheck = true;
            } else {
                final FieldDescriptorJDONature jdoFieldNature = 
                    new FieldDescriptorJDONature(fieldDesc);

                names = jdoFieldNature.getSQLName();
                if ((names != null) && (names.length != relids.length)) {
                    throw new MappingException("The number of column of foreign keys "
                            + "doesn't not match with what specified in manyKey");
                }
                names = (names != null) ? names : relnames;

                String[] joins = jdoFieldNature.getManyKey();
                if ((joins != null) && (joins.length != classids.length)) {
                    throw new MappingException("The number of column of foreign keys "
                            + "doesn't not match with what specified in manyKey");
                }

                if (jdoFieldNature.getManyTable() != null) {
                    _tableName = jdoFieldNature.getManyTable();
                    _store = false;
                    _multi = fieldDesc.isMultivalued();
                    _joined = true;
                    _joinFields = (joins != null) ? joins : classnames;
                } else if (jdoFieldNature.getSQLName() != null) {
                    _tableName = classTable;
                    _store = !ext && !jdoFieldNature.isReadonly();
                    _multi = false;
                    _joined = false;
                    _joinFields = classnames;
                } else {
                    _tableName = new ClassDescriptorJDONature(related).getTableName();
                    _store = false;
                    _multi = fieldDesc.isMultivalued();
                    _joined = true;
                    _joinFields = (joins != null) ? joins : classnames;
                }

                _dirtyCheck = jdoFieldNature.isDirtyCheck();
            }

            _columns = new SQLColumnInfo[relids.length];
            for (int i = 0; i < relids.length; i++) {
                if (!(relids[i].hasNature(FieldDescriptorJDONature.class.getName()))) {
                    throw new MappingException("Related class identities field does "
                            + "not contains sql information!");
                }

                FieldDescriptor relId = relids[i];
                FieldHandlerImpl fh = (FieldHandlerImpl) relId.getHandler();
                _columns[i] = new SQLColumnInfo(names[i], 
                        new FieldDescriptorJDONature(relId).getSQLType()[0],
                        fh.getConvertTo(), fh.getConvertFrom());
            }
        } else {
            final FieldDescriptorJDONature jdoFieldNature = 
                new FieldDescriptorJDONature(fieldDesc);

            _tableName = classTable;
            _store = !ext && !new FieldDescriptorJDONature(fieldDesc).isReadonly();
            _multi = false;
            _joined = false;
            _joinFields = null;
            _dirtyCheck = jdoFieldNature.isDirtyCheck();
            
            _columns = new SQLColumnInfo[1];
            String sqlName = fieldDesc.getFieldName();
            if (jdoFieldNature.getSQLName() != null) {
                sqlName = jdoFieldNature.getSQLName()[0];
            }
            FieldHandlerImpl fh = (FieldHandlerImpl) fieldDesc.getHandler();
            _columns[0] = new SQLColumnInfo(sqlName, 
                    jdoFieldNature.getSQLType()[0],
                    fh.getConvertTo(), fh.getConvertFrom());
        }
    }
    
    public String getTableName() { return _tableName; }
    
    public boolean isStore() { return _store; }
    
    public boolean isMulti() { return _multi; }
    
    public boolean isJoined() { return _joined; }
    
    public boolean isDirtyCheck() { return _dirtyCheck; }
    
    public String[] getJoinFields() { return _joinFields; }
    
    public SQLColumnInfo[] getColumnInfo() { return _columns; }
    
    public FieldDescriptor getFieldDescriptor() { return _fieldDescriptor; }
    
    public String toString() {
        return _tableName + "." + _fieldDescriptor.getFieldName();
    }
}

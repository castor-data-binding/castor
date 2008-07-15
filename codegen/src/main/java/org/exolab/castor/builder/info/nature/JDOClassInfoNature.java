/*
 * Copyright 2008 Tobias Hochwallner
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
 */
package org.exolab.castor.builder.info.nature;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.castor.core.nature.BaseNature;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.mapping.AccessMode;

/**
 * A JDO specific view of a {@link ClassInfo}. Implementation on property based
 * {@link ClassInfo} access.
 * 
 * TODO Add key generator support.
 * 
 * @author Tobias Hochwallner
 * @since 1.2.1
 */
public final class JDOClassInfoNature extends BaseNature {

    /**
     * Property key of primary key.
     */
    private static final String PRIMARY_KEY = "primarykey";

    /**
     * Property key of table name.
     */
    private static final String TABLE_NAME = "tablename";

    /**
     * The {@link AccessMode} of the jdo entity.
     */
    private static final String ACCESS_MODE = "accessmode";

    /**
     * @param classInfo
     *            the classinfo in focus.
     */
    public JDOClassInfoNature(final ClassInfo classInfo) {
        super(classInfo);
    }

    /**
     * Returns the id of the Nature. Implementation returns the fully qualified
     * class name.
     * 
     * @return the id.
     * @see org.exolab.castor.builder.info.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Adds a column to the primary key. The order of the key columns is not
     * guaranteed.
     * 
     * @param column
     *            column name
     */
    public void addPrimaryKey(final String column) {
        List primaryKey = getPropertyAsList(PRIMARY_KEY);
        primaryKey.add(column);
    }
    
    /**
     * Returns a List of {@String}s holding the columns of the primary key.
     * Keep in mind that by contract of
     * <code>addPrimaryKey(String primaryKey)</code> the order is not
     * guaranteed.
     * 
     * @return the names of the primary key's columns or null if no key added
     *         before.
     */
    public List getPrimaryKeys() {
        return (List) this.getProperty(PRIMARY_KEY);
    }

    /**
     * Returns the table name.
     * 
     * @return the SQL table Name
     */
    public String getTableName() {
        return (String) this.getProperty(TABLE_NAME);
    }

    /**
     * Sets the table name to the given String.
     * 
     * @param tableName
     *            of the SQL table.
     */
    public void setTableName(final String tableName) {
        this.setProperty(TABLE_NAME, tableName);
    }

    /**
     * Sets the {@link org.exolab.castor.mapping.AccessMode} to the given
     * AccessMode.
     * 
     * @param accessMode
     *            access mode
     */
    public void setAccessMode(final AccessMode accessMode) {
        this.setProperty(ACCESS_MODE, accessMode);
    }

    /**
     * Returns the {@link org.exolab.castor.mapping.AccessMode}.
     * 
     * @return access mode of the jdo entity.
     */
    public AccessMode getAccessMode() {
        return (AccessMode) this.getProperty(ACCESS_MODE);
    }
    
    /**
     * Returns a List of {@link JDOFieldInfoNature}s of all {@link FieldInfo}s
     * if the field has a a {@link JDOFieldInfoNature} or an empty List if no
     * field has the Nature. Included are attribute, text and element fields.
     * 
     * @return List of {@link JDOFieldInfoNature}s.
     */
    public List getFields() {       
        ClassInfo holder = (ClassInfo) getHolder();
        // Now merge all fields.
        List mergedFields = new LinkedList();
        mergedFields.addAll(holder.getAttributeFieldsAsCollection());
        mergedFields.addAll(holder.getElementFieldsAsCollection());
        FieldInfo textField = holder.getTextField();
        if (textField != null) {
            mergedFields.add(textField); 
        }
        // Walk through all fields and check for Nature.
        Iterator fieldIterator = mergedFields.iterator();
        List naturedFields = new LinkedList();
        while (fieldIterator.hasNext()) {
            FieldInfo field = (FieldInfo) fieldIterator.next();
            if (field.hasNature(JDOFieldInfoNature.class.getName())) {
                JDOFieldInfoNature nature = new JDOFieldInfoNature(field);
                naturedFields.add(nature);
            }
        }
        return naturedFields;
    }

}

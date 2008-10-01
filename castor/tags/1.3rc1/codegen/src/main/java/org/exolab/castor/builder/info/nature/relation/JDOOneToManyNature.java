/*
 * Copyright 2008 Lukas Lang, Filip Hianik
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
package org.exolab.castor.builder.info.nature.relation;

import java.util.List;

import org.castor.core.nature.BaseNature;
import org.exolab.castor.builder.info.FieldInfo;

/**
 * A {@link JDOOneToManyNature} defines typed properties needed in the context a
 * one-to-many relation between two {@link FieldInfo}s.
 * 
 * @author Lukas Lang, Filip Hianik
 * @since 1.2.1
 */
public class JDOOneToManyNature extends BaseNature {

    /**
     * Property key for foreign key.
     */
    private static final String MANY_KEY = "manykey";
    /**
     * Property key for the property read only.
     */
    private static final String READONLY = "readonly";
    /**
     * Property key for the property dirty.
     */
    private static final String DIRTY = "dirty";

    /**
     * Constructor taking a {@link FieldInfo}.
     * 
     * @param field
     *            The field.
     */
    public JDOOneToManyNature(final FieldInfo field) {
        super(field);
    }

    /**
     * Returns the Nature Id.
     * 
     * @see org.castor.core.nature.Nature#getId()
     * @return The fully qualified {@link Class} name.
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Returns true if no update on the column can be performed, false
     * otherwise. Default value is false.
     * 
     * @return true if readonly, false if not or not set.
     */
    public boolean isReadOnly() {
        return getBooleanPropertyDefaultFalse(READONLY);
    }

    /**
     * Sets the column read only.
     * 
     * @param readOnly
     *            true if read only.
     */
    public void setReadOnly(final boolean readOnly) {
        setProperty(READONLY, new Boolean(readOnly));
    }

    /**
     * Returns true if field will NOT be checked against the database for
     * modification, otherwise false. Default value is false.
     * 
     * @return true if field is not updated, false if not or not set.
     */
    public boolean isDirty() {
        return getBooleanPropertyDefaultFalse(DIRTY);
    }

    /**
     * If set true, field will NOT be checked against the database for
     * modification, otherwise set false.
     * 
     * @param dirty
     *            true if field should not be updated.
     */
    public void setDirty(final boolean dirty) {
        setProperty(DIRTY, new Boolean(dirty));
    }

    /**
     * Returns a List of {@String}s holding the columns of the foreign
     * key. Keep in mind that by contract of
     * <code>addPrimaryKey(String foreignKey)</code> the order is not
     * guaranteed.
     * 
     * @return the names of the foreign key's columns or null if no keys added
     *         before.
     */
    public List getForeignKeys() {
        return (List) this.getProperty(MANY_KEY);
    }

    /**
     * Adds a column to the foreign key. By contract, the order of the key
     * columns is not guaranteed and depends on the returned List implementation
     * the {@link BaseNature} is using.
     * 
     * @param column
     *            The column name.
     */
    public void addForeignKey(final String column) {
        List foreignKey = getPropertyAsList(MANY_KEY);
        foreignKey.add(column);
    }
    
}

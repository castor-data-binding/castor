/*
 * Copyright 2008 Lukas Lang
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

import org.castor.core.nature.BaseNature;
import org.exolab.castor.builder.info.FieldInfo;

/**
 * A JDO specific view of a {@link FieldInfo}. Implementation based on property access
 * on {@link FieldInfo}.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 */
public final class JDOFieldInfoNature extends BaseNature {

    /**
     * Constant for the property name for the sql type of the column.
     */
    private static final String COLUMN_TYPE = "columntype";
    /**
     * Constant for the property name for the name of the column.
     */
    private static final String COLUMN_NAME = "columnname";
    /**
     * Constant for the property read only.
     */
    private static final String READONLY = "readonly";
    /**
     * Constant for the property dirty.
     */
    private static final String DIRTY = "dirty";

    /**
     * Constructor taking a FieldInfo.
     * 
     * @param fieldInfo
     *            in focus.
     */
    public JDOFieldInfoNature(final FieldInfo fieldInfo) {
        super(fieldInfo);
    }

    /**
     * Returns the fully qualified class name of the Nature.
     * 
     * @return the nature id.
     * @see org.exolab.castor.builder.info.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Retrieves the sql column name.
     * 
     * @return name of the column.
     */
    public String getColumnName() {
        return (String) this.getProperty(COLUMN_NAME);
    }

    /**
     * Returns the sql type of the column.
     * 
     * @return the sql type.
     */
    public String getColumnType() {
        return (String) this.getProperty(COLUMN_TYPE);
    }

    /**
     * Sets the sql column name.
     * 
     * @param columnName
     *            name of the column.
     */
    public void setColumnName(final String columnName) {
        this.setProperty(COLUMN_NAME, columnName);
    }

    /**
     * Sets the column sql type.
     * 
     * @param sqlType
     *            of the column.
     */
    public void setColumnType(final String sqlType) {
        this.setProperty(COLUMN_TYPE, sqlType);
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

}

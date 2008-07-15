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
 * A {@link JDOOneToManyNature} defines typed properties needed in the 
 * context a one-to-many relation between two {@link FieldInfo}s.
 * 
 * @author Lukas Lang, Filip Hianik
 * @since 1.2.1
 */
public class JDOOneToManyNature extends BaseNature {

    /**
     * Property key for foreign key.
     */
    private static final String FOREIGN_KEY = "foreignkey";

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
     * Returns a List of {@String}s holding the columns of the foreign key.
     * Keep in mind that by contract of
     * <code>addPrimaryKey(String foreignKey)</code> the order is not
     * guaranteed.
     * 
     * @return the names of the foreign key's columns or null if no keys added
     *         before.
     */
    public List getForeignKeys() {
        return (List) this.getProperty(FOREIGN_KEY);
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
        List foreignKey = getPropertyAsList(FOREIGN_KEY);
        foreignKey.add(column);
    }
}

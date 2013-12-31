/*
 * Copyright 2011 Werner Guttmann
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
 * A SOLRJ specific view of a {@link FieldInfo}. Implementation based on
 * property access on {@link FieldInfo}.
 * 
 * @author Werner Guttmann
 * @since 1.3.3
 */
public final class SolrjFieldInfoNature extends BaseNature {

    /**
     * Constant for the property name for the of the solrj field name.
     */
    private static final String FIELD_NAME = "name";

    /**
     * Indicates whether the solrj annotation indicates an Id field.
     */
    private static final String ID_DEFINITION = "idDefinition";

    /**
     * Constructor taking a {@link FieldInfo}.
     * 
     * @param fieldInfo
     *            in focus.
     */
    public SolrjFieldInfoNature(final FieldInfo fieldInfo) {
        super(fieldInfo);
    }

    /**
     * Returns the fully qualified class name of the nature.
     * 
     * @return the nature id.
     * @see org.exolab.castor.builder.info.nature.Nature#getId()
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Retrieves the solrj field name.
     * 
     * @return name of field.
     */
    public String getFieldName() {
        return (String) this.getProperty(FIELD_NAME);
    }

    /**
     * Sets the solrj field name.
     * 
     * @param fieldName
     *            name of the field.
     */
    public void setFieldName(final String fieldName) {
        this.setProperty(FIELD_NAME, fieldName);
    }

    /**
     * Returns true if the solrj annotation is of type @Id.
     * 
     * @return true if the solrj annotation is of type @Id.
     */
    public boolean isIdDefinition() {
        return getBooleanPropertyDefaultFalse(ID_DEFINITION);
    }

    /**
     * Sets whether or not the annotated solrj field is of type @Id.
     * 
     * @param elementDef
     *            The flag indicating whether or not the annotated solrj field
     *            is of type @Id.
     */
    public void setIdDefinition(final boolean idDefinition) {
        this.setProperty(ID_DEFINITION, new Boolean(idDefinition));
    }

}

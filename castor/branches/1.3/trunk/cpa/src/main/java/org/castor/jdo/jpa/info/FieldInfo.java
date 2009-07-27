/*
 * Copyright 2005 Werner Guttmann
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
 */
package org.castor.jdo.jpa.info;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;

/**
 * This class holds the necessary information so that Castor can properly map a
 * JPA annotated classes member to the database.
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public class FieldInfo implements PropertyHolder {

    /**
     * {@link ClassInfo} instance which 'own' (declares) this {@link FieldInfo}.
     */
    private ClassInfo _declaringClassInfo = null;

    /**
     * Map holding the properties set and read by Natures.
     */
    private Map<String, Object> _properties = new HashMap<String, Object>();

    /**
     * Map holding the available natures.
     */
    private Set<String> _natures = new HashSet<String>();

    /**
     * Name of the field.
     */
    private String _fieldName;

    /**
     * Type of the field.
     */
    private Class<?> _fieldType;

    /**
     * the FieldHandler for the right accessType.
     */
    private FieldHandlerImpl _fieldHandler;

    /**
     * Creates a FieldInfo associated to the given {@link ClassInfo}, describing
     * the given a field with the given name using method (property) access.
     * 
     * @param declaringClass
     *            The ClassInfo this FieldInfo is associated with.
     * @param fieldType
     *            The type of the described field.
     * @param fieldName
     *            The name of the described field.
     * @param fieldHandler
     *            The {@link FieldHandlerImpl} that describes the accessType of
     *            that field.
     */
    public FieldInfo(final ClassInfo declaringClass, final Class<?> fieldType,
            final String fieldName, final FieldHandlerImpl fieldHandler) {
        super();
        _fieldHandler = fieldHandler;
        _fieldName = fieldName;
        _fieldType = fieldType;
        _declaringClassInfo = declaringClass;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.NatureExtendable#addNature(java.lang.String)
     */
    public void addNature(final String nature) {
        _natures.add(nature);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.NatureExtendable#hasNature(java.lang.String)
     */
    public boolean hasNature(final String nature) {
        return _natures.contains(nature);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.PropertyHolder#getProperty(java.lang.String)
     */
    public Object getProperty(final String name) {
        return _properties.get(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.nature.PropertyHolder#setProperty(java.lang.String,
     *      java.lang.Object)
     */
    public void setProperty(final String name, final Object value) {
        _properties.put(name, value);
    }

    /**
     * Returns the declaring {@link ClassInfo} this {@link FieldInfo} is
     * associated with.
     * 
     * @return the declaring {@link ClassInfo} this {@link FieldInfo} is
     *         associated with.
     */
    public ClassInfo getDeclaringClassInfo() {
        return _declaringClassInfo;
    }

    /**
     * Sets the reference to the declaring {@link ClassInfo} this
     * {@link FieldInfo} is associated with.
     * 
     * @param classInfo
     *            The ClassInfo this FieldInfo is associated with.
     */
    public void setDeclaringClassInfo(final ClassInfo classInfo) {
        _declaringClassInfo = classInfo;
    }

    /**
     * Indicates the name of the field described by this {@link FieldInfo}.
     * 
     * @return the name of the field described by this {@link FieldInfo}.
     */
    public String getFieldName() {
        return _fieldName;
    }

    /**
     * Sets the name of the field described by this {@link FieldInfo}.
     * 
     * @param fieldName
     *            set the name of the field described by this {@link FieldInfo}.
     */
    public void setFieldName(final String fieldName) {
        _fieldName = fieldName;
    }

    /**
     * Indicates the type of the field described by this {@link FieldInfo}.
     * 
     * @return the type of the field.
     */
    public Class<?> getFieldType() {
        return _fieldType;
    }

    /**
     * Sets the type of the field described by this {@link FieldInfo}.
     * 
     * @param fieldType
     *            The Type of the field.
     */
    public void setFieldType(final Class<?> fieldType) {
        _fieldType = fieldType;
    }

    /**
     * Returns the {@link FieldHandler} associated with this field.
     * 
     * @return The {@link FieldHandlerImpl} managing access to the underlying
     *         field.
     */
    public FieldHandlerImpl getFieldHandler() {
        return _fieldHandler;
    }

    /**
     * Sets the {@link FieldHandler} associated with this field.
     * 
     * @param fieldHandler
     *            the {@link FieldHandlerImpl} managing access to the underlying
     *            field.
     */
    public void setFieldHandler(final FieldHandlerImpl fieldHandler) {
        _fieldHandler = fieldHandler;
    }

}

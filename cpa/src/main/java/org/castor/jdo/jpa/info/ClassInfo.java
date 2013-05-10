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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.castor.core.nature.PropertyHolder;

/**
 * This class holds the necessary information so that Castor can properly map a
 * JPA annotated {@link Class} to the database.
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public final class ClassInfo implements PropertyHolder {

    /**
     * Set of {@link FieldInfo}'s for all attributes that are members of this
     * Class.
     */
    private Set<FieldInfo> _atts = new LinkedHashSet<FieldInfo>();

    /**
     * Set of {@link FieldInfo}'s that define the primary key of this Class -
     * this is a disjunct set of {@link #_atts}.
     */
    private Set<FieldInfo> _keys = new LinkedHashSet<FieldInfo>();

    /**
     * {@link Map} holding the applicable Natures.
     */
    private Set<String> _natures = new HashSet<String>();

    /**
     * {@link Map} holding the properties set and read by various natures.
     */
    private Map<String, Object> _properties = new HashMap<String, Object>();

    /**
     * A reference to the {@link Class} that is described by this
     * {@link ClassInfo}.
     */
    private Class<?> _describedClass;

    /**
     * Creates a new empty ClassInfo.
     */
    public ClassInfo() {
    }

    /**
     * Creates a new ClassInfo, describing a given Class.
     * 
     * @param describedClass
     *            the Class that is described by this ClassInfo.
     */
    public ClassInfo(final Class<?> describedClass) {
        this._describedClass = describedClass;
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
     * Returns the reference to the {@link Class} object that is described by
     * this {@link ClassInfo}.
     * 
     * @return The reference to the Class object that is described by this
     *         ClassInfo.
     */
    public Class<?> getDescribedClass() {
        return _describedClass;
    }

    /**
     * Sets the {@link Class} that is described by this {@link ClassInfo}.
     * 
     * @param describedClass
     *            The {@link Class} that is described by this {@link ClassInfo}.
     */
    public void setDescribedClass(final Class<?> describedClass) {
        this._describedClass = describedClass;
    }

    /**
     * Add a {@link FieldInfo} to this {@link ClassInfo}; if the
     * {@link FieldInfo} is already in the set of fields, it is NOT added a
     * second time (FieldInfos are unique).
     * 
     * @param fieldInfo
     *            The {@link FieldInfo} to add to this {@link ClassInfo}
     */
    public void addFieldInfo(final FieldInfo fieldInfo) {
        _atts.add(fieldInfo);
    }

    /**
     * Indicates the number of {@link FieldInfo}s already known to this
     * {@link ClassInfo}.
     * 
     * @return The number of {@link FieldInfo} definitions for this
     *         {@link ClassInfo}.
     */
    public int getFieldCount() {
        return _atts.size();
    }

    /**
     * Returns a {@link FieldInfo} that corresponds to an element with the given
     * node name.
     * 
     * @param memberName
     *            the name of the field to get.
     * @return a {@link FieldInfo} that corresponds to an element with the given
     *         node name or null if that field does not exist.
     */
    public FieldInfo getFieldInfoByName(final String memberName) {
        for (FieldInfo fieldInfo : _atts) {
            if ((fieldInfo.getFieldName() != null)
                    && (fieldInfo.getFieldName().equals(memberName))) {
                return fieldInfo;
            }
        }
        return null;
    }

    /**
     * Returns Set of associated fields. If no fields are associated, null is
     * returned. This returned set is just a copy of the internal one, so
     * changes to the Set will not affect the internal Set.
     * 
     * @return a Set of associated fields.
     */
    public Set<FieldInfo> getFieldInfos() {
        return new LinkedHashSet<FieldInfo>(_atts);
    }

    /**
     * Add a {@link FieldInfo} to the key set of this ClassInfo (keys and
     * attributes are disjunct!). It is only inserted once in each of these
     * lists, so normal elements can be "raised" to be keys after first adding
     * them to the class.
     * 
     * @param key
     *            - the FieldInfo to add to the set of key fields.
     */
    public void addKey(final FieldInfo key) {
        if (this._atts.contains(key)) {
            this._atts.remove(key);
        }
        this._keys.add(key);

    }

    /**
     * @return the number of {@link FieldInfo} definitions for this ClassInfo's
     *         key.
     */
    public int getKeyFieldCount() {
        return _keys.size();
    }

    /**
     * Returns a {@link FieldInfo} that corresponds to a key element with the
     * given node name.
     * 
     * @param keyName
     *            the name of the key field to get.
     * @return a {@link FieldInfo} that corresponds to a key element with the
     *         given node name or null if that field is not in the key set.
     */
    public FieldInfo getKeyFieldInfoByName(final String keyName) {
        for (FieldInfo field : _keys) {
            if ((field.getFieldName() != null)
                    && (field.getFieldName().equals(keyName))) {
                return field;
            }
        }
        return null;
    }

    /**
     * Returns Set of key fields. If no fields are associated, null is returned.
     * This returned Set is just a copy of the internal one, so changes to the
     * Set will not affect the internal key set.
     * 
     * @return an array of associated fields.
     */
    public Set<FieldInfo> getKeyFieldInfos() {
        return new LinkedHashSet<FieldInfo>(_keys);
    }

    /**
     * Get the reference to the Class object that is extended by this class.
     * 
     * @return the superclass of the described class.
     */
    public Class<?> getExtendedClass() {
        return this._describedClass.getSuperclass();
    }

}

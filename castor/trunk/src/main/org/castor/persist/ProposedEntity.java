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
package org.castor.persist;

import org.exolab.castor.persist.ClassMolder;

/**
 * Holding structure for information about an entity class instance.
 * 
 * This contains amongst others ...
 * <ul>
 *    <li>the suggested class</li>
 *    <li>the actually loaded class (if expansion took place)</li>
 *    <li>the field values (in form of an object array) of the entity</li>
 *  </ul>
 *  
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public final class ProposedEntity {
    //--------------------------------------------------------------------------

    /** The fields of the object in question. */
    private Object[]    _fields = null;

    /** The object. */
    private Object      _entity = null;

    /** True if the proposed class has been expanded. */
    private boolean     _isExpanded = false;

    /** The proposed class. */
    private Class       _proposedEntityClass = null;

    /** The actualClass. */
    private Class       _actualEntityClass = null;

    /** The actual ClassMolder. */
    private ClassMolder _actualClassMolder = null;
    
    /** True if object stored in ObjectLock shoul dbe ignored. */
    private boolean _objectLockObjectToBeIgnored = false;

    //--------------------------------------------------------------------------

    /**
     * Creates an default instance.
     */
    public ProposedEntity () { }

    /**
     * Creates an instance of this class based upon the values passed in.
     * 
     * @param proposedEntity The entity instance to be copied.
     */
    public ProposedEntity(final ProposedEntity proposedEntity) {
        setFields(proposedEntity.getFields());
        setProposedEntityClass(proposedEntity.getProposedEntityClass());
        setActualClassMolder(proposedEntity.getActualClassMolder());
    }
    /**
     * Initialize field values to the specified number.
     * 
     * @param numberOfFields Number of the field values to be created.
     */
    public void initializeFields(final int numberOfFields) {
        _fields = new Object[numberOfFields];
    }
    
    /**
     * Returns the fields of the object in question.
     * 
     * @return Returns the fields.
     */
    public Object[] getFields() {
        return _fields;
    }

    /**
     * Returns the specified field value of the object in question.
     * @param index Index of the field to be returned.
     * @return Returns the specified field value.
     */
    public Object getField(final int index) {
        return _fields[index];
    }

    /**
     * Indicates whether the fields are set, i.e. not null.
     * @return True if fields are set, i.e. not null(.
     */
    public boolean isFieldsSet() {
        return (_fields != null);
    }
    
    /**
     * Indicates the number of field values set for this entity.
     * @return Number of field values set.
     */
    public int getNumberOfFields() {
        return _fields.length;
    }
    
    /**
     * Sets the fields of the object in question.
     * 
     * @param fields The fields to set.
     */
    public void setFields(final Object[] fields) {
        _fields = fields;
    }

    /**
     * Sets the specified field of the object in question.
     * 
     * @param field The field value to set.
     * @param index Specifies which field to set.
     */
    public void setField(final Object field, final int index) {
        _fields[index] = field;
    }
    /**
     * Returns the object.
     * 
     * @return The object.
     */
    public Object getEntity() {
        return _entity;
    }

    /**
     * Sets the object.
     * 
     * @param entity The object to set.
     */
    public void setEntity(final Object entity) {
        _entity = entity;
    }

    /**
     * True if the proposed class has been expanded.
     * 
     * @return <code>true</code> if class is expanded, <code>false</code> otherwise.
     */
    public boolean isExpanded() {
        return _isExpanded;
    }

    /**
     * Set to true if the proposed class has been expanded.
     * 
     * @param isExpanded The isExpanded to set.
     */
    public void setExpanded(final boolean isExpanded) {
        _isExpanded = isExpanded;
    }

    /**
     * Returns the proposed Class instance.
     * 
     * @return The proposedClass.
     */
    public Class getProposedEntityClass() {
        return _proposedEntityClass;
    }

    /**
     * Sets the proposed Class instance.
     * 
     * @param proposedClass The proposedClass to set.
     */
    public void setProposedEntityClass(final Class proposedClass) {
        _proposedEntityClass = proposedClass;
    }

    /**
     * Returns the actual Class instance.
     * 
     * @return The actualClass.
     */
    public Class getActualEntityClass() {
        return _actualEntityClass;
    }

    /**
     * Sets the actual Class instance.
     * 
     * @param actualClass The actualClass to set.
     */
    public void setActualEntityClass(final Class actualClass) {
        _actualEntityClass = actualClass;
    }

    /**
     * Returns the ClassMolder associated with the actual object.
     * 
     * @return The actual ClassMolder.
     */
    public ClassMolder getActualClassMolder() {
        return _actualClassMolder;
    }

    /**
     * Sets the ClassMolder associated with the actual object.
     * 
     * @param actualClassMolder The ClassMolder associated with the actual object.
     */
    public void setActualClassMolder(final ClassMolder actualClassMolder) {
        _actualClassMolder = actualClassMolder;
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<");
        buffer.append("proposedEntityClass=" + _proposedEntityClass);
        buffer.append("; actualEntityClass=" + _actualEntityClass);
        buffer.append("; entity=" + _entity);
        buffer.append("; actual classmolder=" + _actualClassMolder + "; ");
        if (_fields != null) {
            for (int i = 0; i < _fields.length; i++) {
                buffer.append("fields[" + i + "]='" + _fields[i] + "':");
            }
        }
        buffer.append(">");
        return buffer.toString();
    }

    public boolean isObjectLockObjectToBeIgnored() {
        return _objectLockObjectToBeIgnored;
    }

    public void setObjectLockObjectToBeIgnored(final boolean lockObjectToBeIgnored) {
        _objectLockObjectToBeIgnored = lockObjectToBeIgnored;
    }

    //--------------------------------------------------------------------------
}

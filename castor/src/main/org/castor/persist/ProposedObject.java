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
 * Holding structure for information about the class instance being loaded by internal 
 * classes, revealing what class was suggested, what class actually got loaded, etc.
 *  
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public final class ProposedObject {
    //--------------------------------------------------------------------------

    /** The fields of the object in question. */
    private Object[]    _fields = null;

    /** The object. */
    private Object      _object = null;

    /** True if the proposed class has been expanded. */
    private boolean     _isExpanded = false;

    /** The proposed class. */
    private Class       _proposedClass = null;

    /** The actualClass. */
    private Class       _actualClass = null;

    /** The actual ClassMolder. */
    private ClassMolder _actualClassMolder = null;
    
    /** True if object stored in ObjectLock shoul dbe ignored. */
    private boolean _objectLockObjectToBeIgnored = false;

    //--------------------------------------------------------------------------

    /**
     * Returns the fields of the object in question.
     * 
     * @return Returns the fields.
     */
    public Object[] getFields() {
        return _fields;
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
     * Returns the object.
     * 
     * @return The object.
     */
    public Object getObject() {
        return _object;
    }

    /**
     * Sets the object.
     * 
     * @param object The object to set.
     */
    public void setObject(final Object object) {
        _object = object;
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
    public Class getProposedClass() {
        return _proposedClass;
    }

    /**
     * Sets the proposed Class instance.
     * 
     * @param proposedClass The proposedClass to set.
     */
    public void setProposedClass(final Class proposedClass) {
        _proposedClass = proposedClass;
    }

    /**
     * Returns the actual Class instance.
     * 
     * @return The actualClass.
     */
    public Class getActualClass() {
        return _actualClass;
    }

    /**
     * Sets the actual Class instance.
     * 
     * @param actualClass The actualClass to set.
     */
    public void setActualClass(final Class actualClass) {
        _actualClass = actualClass;
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
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<");
        buffer.append("proposedClass=" + _proposedClass);
        buffer.append("; actualClass=" + _actualClass);
        buffer.append("; object=" + _object);
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

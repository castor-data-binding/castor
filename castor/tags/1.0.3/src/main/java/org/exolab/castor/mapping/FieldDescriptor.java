/*
 * Copyright 2006 Assaf Arkin, Ralf Joachim
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
package org.exolab.castor.mapping;

/**
 * Describes the properties of a field. Implementations will extend this inteface to
 * provide additional properties.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 */
public interface FieldDescriptor {
    //--------------------------------------------------------------------------

    /**
     * Set the class descriptor which contains this field.
     * 
     * @param contClsDesc The class descriptor which contains this field.
     */
    void setContainingClassDescriptor(ClassDescriptor parent);

    /**
     * Get the class descriptor which contains this field.
     * 
     * @return The class descriptor which contains this field.
     */
    ClassDescriptor getContainingClassDescriptor();

    /**
     * Returns the name of the field. The field must have a name, even if set through
     * accessor methods.
     *
     * @return Field name.
     */
    String getFieldName();

    /**
     * Returns the Java type of the field.
     *
     * @return Field type.
     */
    Class getFieldType();

    /**
     * Returns the class descriptor related to the field type. If the field type is a
     * class for which a descriptor exists, this descriptor is returned. If the field
     * type is a class for which no mapping is provided, null is returned.
     *
     * @return The class descriptor of the field type, or null.
     */
    ClassDescriptor getClassDescriptor();

    /**
     * Returns the handler of the field. In order to persist or marshal a field
     * descriptor will be associated with a handler.
     *
     * @return The field handler.
     */
    FieldHandler getHandler();

    /**
     * Returns true if the field is transient. Transient fields are never persisted or
     * marshalled.
     *
     * @return True if transient field.
     */
    boolean isTransient();

    /**
     * Returns true if the field type is immutable.
     *
     * @return True if the field type is immutable.
     */
    boolean isImmutable();

    /**
     * Returns true if the field type is required.
     *
     * @return True if the field type is required.
     */
    boolean isRequired();

    /**
     * Returns true if the field is multivalued (a collection).
     *
     * @return True if the field is multivalued.
     */
    boolean isMultivalued();

    //--------------------------------------------------------------------------
}


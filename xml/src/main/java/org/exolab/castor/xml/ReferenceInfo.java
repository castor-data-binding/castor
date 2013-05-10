/*
 * Copyright 2006 Werner Guttman
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
package org.exolab.castor.xml;

import org.exolab.castor.mapping.FieldDescriptor;

/**
 * Internal class used to save state for reference resolving.
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 0000 $ $Date:$
 */
class ReferenceInfo {

    /** The ID(REF) value of the target object instance. */
    private final String id;
    /** The target object referenced by this IDREF instance. */
    private final Object target;
    /** XML Field descriptor referenced by this IDREF instance. */
    private final XMLFieldDescriptor descriptor;

    /** The 'next' ReferenceInfo instance. */
    private ReferenceInfo next = null;

    /**
     * Creates a new ReferenceInfo
     * @param id
     * @param target
     * @param descriptor
     */
    public ReferenceInfo(final String id, final Object target, final XMLFieldDescriptor descriptor) {
        this.id = id;
        this.target = target;
        this.descriptor = descriptor;
    }

    /**
     * Sets a refrence to the 'next' ReferenceInfo instance.
     * @param info The 'next' ReferenceInfo instance.
     */
    public void setNext(ReferenceInfo info) {
        this.next = info;
    }

    /**
     * Returns the field descriptor referenced by this IDREF instance.
     * @return the field descriptor referenced by this IDREF instance.
     */
    public FieldDescriptor getDescriptor() {
        return this.descriptor;
    }

    /**
     * Returns the target object referenced by this IDREF instance.
     * @return the target object referenced by this IDREF instance.
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Returns the next 'ReferenceInfo' instance.
     * @return the next 'ReferenceInfo' instance.
     */
    public ReferenceInfo getNext() {
        return this.next;
    }

    /**
     * Returns the ID value of the target object instance.
     * @return the ID value of the target object instance.
     */
    public String getId() {
        return this.id;
    }

}

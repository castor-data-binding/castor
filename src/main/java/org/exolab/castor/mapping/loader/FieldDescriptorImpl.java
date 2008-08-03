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
package org.exolab.castor.mapping.loader;

import org.exolab.castor.mapping.FieldHandler;

/**
 * A basic field descriptor implementation. Engines will extend this
 * class to provide additional functionality.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-06 14:55:28 -0700 (Tue, 06 Dec 2005) $
 */
public class FieldDescriptorImpl extends AbstractFieldDescriptor {
    //--------------------------------------------------------------------------

    protected FieldDescriptorImpl() { }
    
    /**
     * Constructs a new field descriptor.
     *
     * @param fieldName The field name
     * @param typeInfo The field type information
     * @param handler The field handler (may be null)
     * @param trans True if the field is transient
     */
    public FieldDescriptorImpl(final String fieldName, final TypeInfo typeInfo,
            final FieldHandler handler, final boolean trans) {
        super();
        
        if (fieldName == null) {
            throw new IllegalArgumentException("Argument 'fieldName' is null");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Argument 'fieldDesc' has no handler");
        }
        
        setFieldName(fieldName);
        setFieldType(typeInfo.getFieldType());
        setHandler(handler);
        setTransient(trans);
        setImmutable(typeInfo.isImmutable());
        setRequired(typeInfo.isRequired());
        setMultivalued(typeInfo.getCollectionHandler() != null);
    }

    //--------------------------------------------------------------------------

    public String toString() {
        return getFieldName() + "(" + getFieldType().getName() + ")";
    }

    //--------------------------------------------------------------------------
}

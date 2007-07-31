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
 *
 * $Id$
 */
package org.castor.persist.resolver;

import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.FieldMolder;

/**
 * Factory class for instantiating ResolverStragegy instances.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @since 0.9.9
 *
 * This is a type.
 */
public final class ResolverFactory {
    
    private ResolverFactory () {
        // nothing to do
    }
    
    /**
     * Factory method to create ResolverStrategy instance.
     * 
     * @param fieldMolder The associated {@link FieldMolder}
     * @param classMolder The associated {@link ClassMolder}
     * @param debug ???
     * @return The corresponding ResolverStratgey instance
     */
    public static ResolverStrategy createRelationResolver (final FieldMolder fieldMolder, 
            final ClassMolder classMolder, 
            final int fieldIndex,
            final boolean debug) 
        /* throws PersistenceException */ {
        
        ResolverStrategy relationResolver = null;
        
        int fieldType = fieldMolder.getFieldType();
        switch (fieldType) {
        case FieldMolder.PRIMITIVE:
            relationResolver = new PrimitiveResolver (classMolder, fieldMolder, fieldIndex, debug);
            break;
        case FieldMolder.SERIALIZABLE:
            relationResolver = new SerializableResolver (classMolder, fieldMolder, fieldIndex, debug);
            break;
        case FieldMolder.PERSISTANCECAPABLE:
            relationResolver = 
                new PersistanceCapableRelationResolver (classMolder, fieldMolder, fieldIndex, debug);
            break;
        case FieldMolder.ONE_TO_MANY:
            relationResolver = 
                new OneToManyRelationResolver (classMolder, fieldMolder, fieldIndex, debug);
            break;
        case FieldMolder.MANY_TO_MANY:
            relationResolver = 
                new ManyToManyRelationResolver (classMolder, fieldMolder, fieldIndex, debug);
            break;
        default:
//            throw new PersistenceException ("Invalid field type '" 
//            + fieldMolder.getFieldType() + "' specified.");
        }
        
        return relationResolver;
    }

}

/*
 * Copyright 2006 Edward Kuns.
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
 * $Id:  $
 */
package org.exolab.castor.builder;

/**
 * Defines contants used throughout source generation.
 * @author ekuns
 * @version $Revision: 0000 $ $Date: $
 */
public interface SourceGeneratorConstants {
    
    /** Enumerations are placed into this special package relative to the generated source. */
    String TYPES_PACKAGE = "types";

    // FIXME Use these strings in places like org.exolab.castor.builder.types.XSListJ2
    /**
     * Represents a java.util.Vector instance.
     */
    String FIELD_INFO_VECTOR     = "vector";
    /**
     * Represents a java.util.ArrayList instance.
     */
    String FIELD_INFO_ARRAY_LIST = "arraylist";
    /**
     * Represents a ODMG collection type instance.
     */
    String FIELD_INFO_ODMG       = "odmg";
    /**
     * Represents a java.util.Collection instance.
     */
    String FIELD_INFO_COLLECTION = "collection";
    /**
     * Represents a java.util.Set instance.
     */
    String FIELD_INFO_SET        = "set";
    /**
     * Represents a java.util.SortedSet instance.
     */
    String FIELD_INFO_SORTED_SET = "sortedset";
}

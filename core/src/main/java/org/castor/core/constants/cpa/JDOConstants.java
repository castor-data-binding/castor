/*
 * Copyright 2008 Lukas Lang
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
package org.castor.core.constants.cpa;

/**
 * Defines CPA/JDO specific constants.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public interface JDOConstants {

    /**
     * File name suffix used for JDO-specific descriptor classes.
     */
    String JDO_DESCRIPTOR_SUFFIX = "JDODescriptor";

    /**
     * Package name of the sub-package where descriptors can be found.
     */
    String JDO_DESCRIPTOR_PACKAGE = "jdo_descriptors";

    /**
     * JDO namespace (as used by the extensions for the XML code generator).
     */
    String JDO_NAMESPACE = "http://www.castor.org/binding/persistence";

    /**
     * Name of the table annotation element.
     */
    String ANNOTATIONS_TABLE_NAME = "table";

    /**
     * Name of the column annotation element.
     */
    String ANNOTATIONS_COLUMN_NAME = "column";

    /**
     * Name of the one-to-one annotation element.
     */
    String ANNOTATIONS_ONE_TO_ONE_NAME = "one-to-one";
    
    /**
     * Name of the one-to-many annotation element.
     */
    String ANNOTATIONS_ONE_TO_MANY = "one-to-many";

    /**
     * Name of the many-to-many annotation element.
     */
    String ANNOTATIONS_MANY_TO_MANY = "many-to-many";
    
    /**
     * Package where to find generated JDO classes to unmarshal annotations.
     */
    String GENERATED_ANNOTATION_CLASSES_PACKAGE = "org.exolab.castor.xml.schema.annotations.jdo";
    
}

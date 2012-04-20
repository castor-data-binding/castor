/*
 * Copyright 2008 Werner Guttmann
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
package org.exolab.castor.builder.info;

/**
 * An enumeration enlisting the possible XML {@link FieldDescriptor}
 * types.
 *
 * @author <a href="mailto:wguttmn AT codehaus DOT com">Werner Guttmann</a>
 */
public enum NodeType {
    
    /**
     * Attribute node (type). 
     */
    ATTRIBUTE,
    
    /**
     * Element node (type). 
     */
    ELEMENT,
    
    /**
     * Text node (type). 
     */
    TEXT

}

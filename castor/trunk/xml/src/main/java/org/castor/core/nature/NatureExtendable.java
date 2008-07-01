/*
 * Copyright 2008 Tobias Hochwallner
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
package org.castor.core.nature;

/**
 * Defines a marker interface that any class needs to implement that wants 
 * to allow natures to be layered on top.
 * 
 * @see ClassInfo
 * @see Nature
 * 
 * @author Tobias Hochwallner, Sebastian Gabmeyer
 * @since 1.2.1
 *
 */
public interface NatureExtendable {
    
    /**
     * Checks if a specified nature has been added.
     * 
     * @param nature the name of the nature.
     * @return true if the specified nature was added.
     */
    boolean hasNature(String nature);
    
    /**
     * Adds a specified nature.
     * 
     * @param nature the name of the nature
     */
    void addNature(String nature);

}

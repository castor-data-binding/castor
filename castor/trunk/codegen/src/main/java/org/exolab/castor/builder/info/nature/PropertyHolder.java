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
package org.exolab.castor.builder.info.nature;

/**
 * PropertyHolder provides get and set methods on a holder. 
 * @see ClassInfo
 * @see Nature
 * 
 * @author Sebastian Gabmeyer
 * @since 1.2.1
 *
 */
public interface PropertyHolder extends NatureExtendable {
    
    /**
     * Get a property by its name.
     * 
     * @param name the name of the property to get.
     * @return the property as specified by the name.
     */
    Object getProperty(final String name);
    
    /**
     * Set a property specified by the name to the passed value.
     * 
     * @param name the name of the property to set.
     * @param value the value to set the specified property to.
     */
    void setProperty(final String name, final Object value);
}

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
package org.castor.mapping;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;

/**
 * A factory for properly acquiring <tt>org.exolab.castor.mapping.MappingLoader</tt> 
 * instances. To provide an implementation for a specific MappingLoader,
 * implement this interface.
 *
 * @author <a href=" mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2005-12-19 15:48:30 -0700 (Mon, 19 Dec 2005) $
 * @since 1.0.4
 */
public interface MappingLoaderFactory {

    /**
     * Returns the short alias for this factory instance.
     * 
     * @return The short alias name. 
     */
    String getName();

    /**
     * Acquires the appropriate <tt>org.exolab.castor.mapping.MappingLoader</tt> with the
     * given properties.
     * 
     * @return The transaction manager.
     * @throws MappingException If any failure occured when loading
     *         the MappingLoader.
     */
    MappingLoader getMappingLoader() 
    throws MappingException;
    
    /**
     * Defines the source type of the underlying MappingLoader
     * @return The source type.
     */
    String getSourceType();

    /**
     * Defines the binding type of the underlying MappingLoader.
     * @return the binding type of the underlying MappingLoader
     */
    BindingType getBindingType();

}

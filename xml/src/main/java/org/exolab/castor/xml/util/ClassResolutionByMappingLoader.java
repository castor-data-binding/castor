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
package org.exolab.castor.xml.util;

import org.exolab.castor.mapping.ClassDescriptor;

/**
 * Resolves {@link ClassDescriptor}s using a {@link MappingLoader}.
 * 
 * @author Lukas Lang
 */
public final class ClassResolutionByMappingLoader extends BaseResolutionCommand {

    /**
     * Constructor taking a MappingLoader.
     */
    public ClassResolutionByMappingLoader() {
        addNature(MappingLoaderNature.class.getName());
    }

    /**
     * Resolves a {@link ClassDescriptor} for the given type using 
     * the {@link MappingLoader} of the {@link MappingLoaderNature}.
     * @see org.exolab.castor.xml.util.ClassResolutionCommand#resolve(java.lang.Class)
     * @return A {@link ClassDescriptor} or null if not found.
     * @param type The type to resolve.
     */
    public ClassDescriptor resolve(final Class type) {
        return new MappingLoaderNature(this)
                .getMappingLoader().getDescriptor(type.getName());
    }


}

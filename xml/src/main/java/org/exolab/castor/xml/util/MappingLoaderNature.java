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
 */
package org.exolab.castor.xml.util;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.mapping.MappingLoader;

/**
 * {@link MappingLoaderNature} provides a {@link MappingLoader}.
 * @author Lukas Lang
 * @since 1.2.1
 *
 */
public final class MappingLoaderNature extends BaseNature {

    /**
     * Nature property name for {@link MappingLoader}.
     */
    private static final String MAPPING_LOADER = "mappingloader";

    /**
     * Constructor taking a {@link PropertyHolder}.
     * @param holder The {@link PropertyHolder} to use.
     */
    protected MappingLoaderNature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * Returns the Nature Id.
     * @see org.castor.core.nature.Nature#getId()
     * @return Fully qualified {@link Class} name.
     */
    public String getId() {
        return getClass().getName();
    }

    /**
     * Sets the {@link MappingLoader}.
     * @param mappingLoader The {@link MappingLoader} to set.
     */
    public void setMappingLoader(final MappingLoader mappingLoader) {
        setProperty(MAPPING_LOADER, mappingLoader);
    }
    
    /**
     * Returns the {@link MappingLoader}.
     * @return A {@link MappingLoader} or null if not set.
     */
    public MappingLoader getMappingLoader() {
        return (MappingLoader) getProperty(MAPPING_LOADER);
    }

}

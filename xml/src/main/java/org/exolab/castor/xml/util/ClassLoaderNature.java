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

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

/**
 * A {@link ClassLoaderNature} provides a ClassLoader.
 * @author Lukas Lang
 * @since 1.2.1
 */
public final class ClassLoaderNature extends BaseNature {

    /**
     * Nature property name for {@link ClassLoader}.
     */
    private static final String CLASS_LOADER = "classloader";

    /**
     * Constructor takes the {@link PropertyHolder} in use.
     * @param holder PropertyHolder in use.
     */
    protected ClassLoaderNature(final PropertyHolder holder) {
        super(holder);
    }

    /** 
     * Returns the Nature Id.
     * @see org.castor.core.nature.Nature#getId()
     * @return The fully qualified class name.
     */
    public String getId() {
        return getClass().getName();
    }
    
    /**
     * Sets the {@link ClassLoader}.
     * @param loader The {@link ClassLoader} to use.
     */
    public void setClassLoader(final ClassLoader loader) {
        setProperty(CLASS_LOADER, loader);
    }
    
    /**
     * Returns a {@link ClassLoader}.
     * @return A {@link ClassLoader} or null if not set.
     */
    public ClassLoader getClassLoader() {
        return (ClassLoader) getProperty(CLASS_LOADER);
    }

}

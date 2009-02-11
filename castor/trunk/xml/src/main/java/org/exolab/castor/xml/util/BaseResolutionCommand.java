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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.mapping.ClassDescriptor;

/**
 * Provides basic {@link HashMap} based property implementation for a
 * {@link ClassDescriptorResolutionCommand}.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public abstract class BaseResolutionCommand implements
        ClassDescriptorResolutionCommand {

    /**
     * Properties set by Natures.
     */
    private Map _properties = new HashMap();

    /**
     * Added Natures.
     */
    private Set _natures = new HashSet();

    /**
     * {@link JDOClassDescriptorResolver} used to register
     * {@link ClassDescriptor} instances during their creation.
     */
    private JDOClassDescriptorResolver classDescriptorResolver;

    /**
     * Get a Nature property.
     * 
     * @param name
     *            Name of the property.
     * @return Property value.
     */
    public final Object getProperty(final String name) {
        return _properties.get(name);
    }

    /**
     * Set a Nature property.
     * 
     * @param name
     *            Name of the property.
     * @param value
     *            Value of the property.
     */
    public final void setProperty(final String name, final Object value) {
        _properties.put(name, value);
    }

    /**
     * Adds a Nature.
     * 
     * @param nature
     *            Nature Id.
     */
    public final void addNature(final String nature) {
        _natures.add(nature);
    }

    /**
     * Returns true if the Nature with the given name was added before, false if
     * not.
     * 
     * @param nature
     *            Nature Id.
     * @return true if added before, false if not.
     */
    public final boolean hasNature(final String nature) {
        return _natures.contains(nature);
    }

    /**
     * Returns the {@link JDOClassDescriptorResolver} used to register
     * {@link ClassDescriptor} instances during their creation.
     * 
     * @return the {@link JDOClassDescriptorResolver} used to register
     *         {@link ClassDescriptor} instances
     */
    protected JDOClassDescriptorResolver getClassDescriptorResolver() {
        return classDescriptorResolver;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.util.ClassDescriptorResolutionCommand#setClassDescriptorResolver(org.exolab.castor.xml.util.JDOClassDescriptorResolver)
     */
    public void setClassDescriptorResolver(
            final JDOClassDescriptorResolver classDescriptorResolver) {
        this.classDescriptorResolver = classDescriptorResolver;
    }

}
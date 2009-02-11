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

import org.castor.core.nature.PropertyHolder;
import org.exolab.castor.mapping.ClassDescriptor;

/**
 * Command resolves {@link ClassDescriptor}s. Commands are capable to 
 * have Natures to provide resource dependencies. 
 * @author Lukas Lang
 *
 */
public interface ClassDescriptorResolutionCommand extends PropertyHolder {

    /**
     * Resolves a {@link ClassDescriptor}..
     * 
     * @param type
     *            type to look up.
     * @return a {@link ClassDescriptor} if found, null if not.
     */
    ClassDescriptor resolve(Class type);

    /**
     * Sets a {@link JDOClassDescriptorResolver} instance to be used for
     * {@link ClassDescriptor} registration.
     * @param classDescriptorResolver {@link JDOClassDescriptorResolver} instance to be used.
     */
    void setClassDescriptorResolver(JDOClassDescriptorResolver classDescriptorResolver);

}
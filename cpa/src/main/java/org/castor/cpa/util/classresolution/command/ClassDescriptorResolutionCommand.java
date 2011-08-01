/*
 * Copyright 2008 Werner Guttmann, Lukas Lang
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
package org.castor.cpa.util.classresolution.command;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.util.JDOClassDescriptorResolver;
import org.exolab.castor.mapping.ClassDescriptor;

/**
 * Command resolves {@link ClassDescriptor}s. Commands are capable to 
 * have Natures to provide resource dependencies. 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @author <a href="mailto:lukas DOT lang AT inode DOT at">Lukas Lang</a>
 * @version $Revision$ $Date$
 *
 */
public interface ClassDescriptorResolutionCommand extends PropertyHolder {
    //-----------------------------------------------------------------------------------

    /**
     * Resolves a {@link ClassDescriptor}..
     * 
     * @param type type to look up.
     * @return a {@link ClassDescriptor} if found, null if not.
     */
    ClassDescriptor resolve(Class<?> type);

    /**
     * Sets a {@link JDOClassDescriptorResolver} instance to be used for
     * {@link ClassDescriptor} registration.
     * 
     * @param classDescriptorResolver {@link JDOClassDescriptorResolver} instance to be used.
     */
    void setClassDescriptorResolver(JDOClassDescriptorResolver classDescriptorResolver);

    //-----------------------------------------------------------------------------------
}
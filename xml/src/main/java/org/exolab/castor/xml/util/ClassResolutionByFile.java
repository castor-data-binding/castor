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

import org.castor.core.constants.cpa.JDOConstants;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.util.resolvers.ResolveHelpers;

/**
 * Resolves {@link ClassDescriptor}s on the file system.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public final class ClassResolutionByFile extends BaseResolutionCommand {

    /**
     * Constructor taking a MappingLoader.
     */
    public ClassResolutionByFile() {
        addNature(ClassLoaderNature.class.getName());
    }

    /**
     * Tries to load a {@link ClassDescriptor} for the given type from the
     * filesystem by lookup the subpackage specified in
     * {@value JDOConstants#JDO_DESCRIPTOR_PACKAGE} using the
     * {@link ClassLoader} of the {@link ClassLoaderNature} .
     * 
     * @param type
     *            to lookup the descriptor for.
     * @return an instance of ClassDescriptor if found, null if not.
     */
    public ClassDescriptor resolve(final Class type) {
        ClassDescriptor classDesc = null;
        ClassLoader classLoader = new ClassLoaderNature(this).getClassLoader();
        Class descriptorClass = null;
        StringBuffer descriptorClassName = new StringBuffer(type.getName());
        descriptorClassName.append(JDOConstants.JDO_DESCRIPTOR_SUFFIX);

        // Lookup the descriptor package
        int offset = descriptorClassName.lastIndexOf(".");
        if (offset != -1) {
            descriptorClassName.insert(offset, ".");
            descriptorClassName.insert(offset + 1,
                    JDOConstants.JDO_DESCRIPTOR_PACKAGE);
            descriptorClass = ResolveHelpers.loadClass(classLoader,
                    descriptorClassName.toString());
        }
        // Descriptor was found, instantiate and return it
        if (descriptorClass != null) {
            try {
                classDesc = (ClassDescriptor) descriptorClass.newInstance();
                return classDesc;
            } catch (InstantiationException e) {
                new RuntimeException(e.getMessage());
            } catch (IllegalAccessException e) {
                new RuntimeException(e.getMessage());
            }
        }
        return classDesc;
    }
}

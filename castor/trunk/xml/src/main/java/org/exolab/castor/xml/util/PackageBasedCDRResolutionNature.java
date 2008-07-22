/*
 * Copyright 2008 Sebastian Gabmeyer
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

import java.util.List;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

/**
 * This class provides a view on a additional properties for
 * {@link ClassDescriptorResolutionCommand}s.
 * 
 * @author Sebastian Gabmeyer
 * @since 1.2.1
 * 
 */
public class PackageBasedCDRResolutionNature extends BaseNature {

    /**
     * Property name for the string list of package names.
     */
    private static final String PACKAGES = "package-names";

    /**
     * The constructor takes a {@link PropertyHolder}.
     * 
     * @param holder
     *            the <em>container</em> to place/read the properties in/from.
     */
    public PackageBasedCDRResolutionNature(final PropertyHolder holder) {
        super(holder);
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return PackageBasedCDRResolutionNature.class.getName();
    }

    /**
     * Adds a package to the string {@link List} of package names.
     * 
     * @param packageName
     *            the name of the package to add.
     */
    public void addPackageName(final String packageName) {
        List packageNames = getPropertyAsList(PACKAGES);
        packageNames.add(packageName);
        setProperty(PACKAGES, packageNames);
    }

    /**
     * Set a String {@link List} of package names that should be searched for
     * {@link org.exolab.castor.mapping.ClassDescriptor}s.
     * 
     * @param packageNames
     *            the String {@link List} of package names to set.
     */
    public void setPackageNames(final List packageNames) {
        setProperty(PACKAGES, packageNames);
    }

    /**
     * Get the String {@link List} of package names that the current
     * {@link ClassDescriptorResolutionCommand} should search for
     * {@link org.exolab.castor.mapping.ClassDescriptor}s in.
     * 
     * @return a String {@link List} of package names.
     */
    public List getPackageNames() {
        return getPropertyAsList(PACKAGES);
    }
}

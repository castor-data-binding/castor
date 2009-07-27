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
package org.exolab.castor.xml.util;

import java.util.Iterator;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.ResolverException;

/**
 * 
 * JDO-specific {@link ClassDescriptorResolver} instance that provides functionality
 * to find or "resolve" {@link ClassDescriptor}s from a given class (name).
 *
 * @since 1.2.1
 */
public interface JDOClassDescriptorResolver extends ClassDescriptorResolver {

    /**
     * Returns the ClassDescriptor for the given class.
     * 
     * @param type
     *            the class name to find the ClassDescriptor for
     * @exception ResolverException Indicates that the given {@link Class} 
     *            cannot be resolved.
     * @return the ClassDescriptor for the given class
     */
    ClassDescriptor resolve(String type) throws ResolverException;

    /**
     * Adds a given {@link Class} instance manually, so that it can be loaded from 
     * the file system.
     * @param domainClass A given {@link Class} instance.
     */
    void addClass(Class domainClass);

    /**
     * Adds a given package name manually, so that class descriptors can be loaded from 
     * this package (from the file system).
     * @param packageName A given package name.
     */
    void addPackage(String packageName);

    /**
     * Returns an iterator over all the known descriptors in the original order they have been
     * added. Each element is of type {@link ClassDescriptor}.
     * @return an {@link Iterator} over all the known JDO class descriptors.
     */
    Iterator descriptorIterator();

    /**
     * Returns the {@link ClassLoader} instance as used internally.
     * @return The {@link ClassLoader} instance used internally.
     */
    ClassLoader getClassLoader();

}
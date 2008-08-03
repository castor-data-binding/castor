/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "Exolab" must not be used to endorse or promote products derived
 * from this Software without prior written permission of Intalio, Inc. For
 * written permission, please contact info@exolab.org.
 * 
 * 4. Products derived from this Software may not be called "Exolab" nor may
 * "Exolab" appear in their names without prior written permission of Intalio,
 * Inc. Exolab is a registered trademark of Intalio, Inc.
 * 
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 * 
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 * 
 * This file was originally developed by Keith Visco during the course of
 * employment at Intalio Inc. All portions of this file developed by Keith Visco
 * after Jan 19 2005 are Copyright (C) 2005 Keith Visco. All Rights Reserved.
 * 
 * $Id: ClassDescriptorResolver.java 5951 2006-05-30 22:18:48Z bsnyder $
 */

package org.exolab.castor.xml;

import java.util.Iterator;

import org.castor.xml.InternalContext;
import org.exolab.castor.xml.util.ResolverStrategy;

/**
 * An interface for finding or "resolving" XMLClassDescriptor classes.
 * 
 * <BR/> <B>Note:</B> This interface is used by the marshalling Framework for
 * resolving XMLClassDescriptors for non-primitive types. There are no
 * guarantees that this class will be called for java native classes.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision: 5951 $ $Date: 2005-02-28 17:41:38 -0700 (Mon, 28 Feb
 *          2005) $
 */
public interface XMLClassDescriptorResolver extends ClassDescriptorResolver {

    /**
     * To set the XMLContext to be used.
     * 
     * @param xmlContext
     *            the XMLContext to be used
     */
    void setInternalContext(final InternalContext xmlContext);

    /**
     * Enables or disables introspection. Introspection is enabled by default.
     * 
     * @param enable
     *            a flag to indicate whether or not introspection is allowed.
     */
    void setUseIntrospection(final boolean enable);

    /**
     * Sets whether or not to look for and load package specific mapping files
     * (".castor.xml").
     * 
     * @param loadPackageMappings
     *            a boolean that enables or disables the loading of package
     *            specific mapping files
     */
    void setLoadPackageMappings(final boolean loadPackageMappings);

    /**
     * Sets the ClassLoader to use when loading class descriptors.
     * 
     * @param loader
     *            the ClassLoader to use
     */
    void setClassLoader(ClassLoader loader);

    /**
     * The resolver strategy to use for class and package resolving. Will set
     * the current attributes into the new strategy.
     * 
     * @param resolverStrategy
     *            the ResolverStrategy to use for resolve calls
     */
    void setResolverStrategy(final ResolverStrategy resolverStrategy);

    /**
     * To set the Introspector to be used. It is stored as attribute of resolver
     * and set as property into the current strategy.
     * 
     * @param introspector
     *            the Introspector to use
     */
    void setIntrospector(final Introspector introspector);

    /**
     * Returns the XMLClassDescriptor for the given class name.
     * 
     * @param className
     *            the class name to find the XMLClassDescriptor for
     * @return the XMLClassDescriptor for the given class name
     * @throws ResolverException in case that resolving fails unrecoverable
     */
    XMLClassDescriptor resolve(String className) throws ResolverException;

    /**
     * Returns the XMLClassDescriptor for the given class name.
     * 
     * @param className
     *            the class name to find the XMLClassDescriptor for
     * @param loader
     *            the ClassLoader to use
     * @return the XMLClassDescriptor for the given class name
     * @throws ResolverException in case that resolving fails unrecoverable
     */
    XMLClassDescriptor resolve(String className, ClassLoader loader) throws ResolverException;

    /**
     * Returns the first XMLClassDescriptor that matches the given XML name and
     * namespaceURI. Null is returned if no descriptor can be found.
     * 
     * @param xmlName
     *            The class name to find the XMLClassDescriptor for.
     * @param namespaceURI
     *            The namespace URI to identify the XMLClassDescriptor.
     * @param loader
     *            The ClassLoader to use.
     * @return The XMLClassDescriptor for the given XML name.
     * @throws ResolverException in case that resolving fails unrecoverable
     */
    XMLClassDescriptor resolveByXMLName(String xmlName, String namespaceURI, ClassLoader loader)
            throws ResolverException;

    /**
     * Returns an enumeration of XMLClassDescriptor objects that match the given
     * xml name.
     * 
     * @param xmlName
     *            The class name to find the XMLClassDescriptor for.
     * @param namespaceURI
     *            The namespace URI to identify the XMLClassDescriptor.
     * @param loader
     *            The ClassLoader to use.
     * @return An Iterator of XMLClassDescriptor objects.
     * @throws ResolverException in case that resolving fails unrecoverable
     */
    Iterator resolveAllByXMLName(String xmlName, String namespaceURI, ClassLoader loader)
            throws ResolverException;

    /**
     * Loads the class descriptor for the class instance specified. The use of
     * this method is useful when no mapping is used, as happens when the domain
     * classes hase been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * 
     * @param className
     *            Name of the class for which the associated descriptor should
     *            be loaded.
     * @throws ResolverException
     *             If there's an unrecoverable problem with resolving a certain
     *             class.
     */
    void addClass(final String className) throws ResolverException;

    /**
     * Loads the class descriptors for the class instances specified. The use of
     * this method is useful when no mapping is used, as happens when the domain
     * classes hase been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * 
     * @param classNames
     *            Names of the classes for which the associated descriptors
     *            should be loaded.
     * @throws ResolverException
     *             If there's an unrecoverable problem with resolving a certain
     *             class.
     */
    void addClasses(final String[] classNames) throws ResolverException;

    /**
     * Loads the class descriptor for the class instance specified. The use of
     * this method is useful when no mapping is used, as happens when the domain
     * classes have been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * 
     * @param clazz
     *            Class for which the associated descriptor should be loaded.
     * @throws ResolverException
     *             If there's an unrecoverable problem with resolving a certain
     *             class.
     */
    void addClass(final Class clazz) throws ResolverException;

    /**
     * Loads the class descriptors for the class instances specified. The use of
     * this method is useful when no mapping is used, as happens when the domain
     * classes hase been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * 
     * @param clazzes
     *            Classes for which the associated descriptors should be loaded.
     * @throws ResolverException
     *             If there's an unrecoverable problem with resolving a certain
     *             class.
     */
    void addClasses(final Class[] clazzes) throws ResolverException;

    /**
     * Loads class descriptors from the package specified. The use of this
     * method is useful when no mapping is used, as happens when the domain
     * classes hase been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * <p>
     * Please note that this functionality will work only if you provide the
     * <tt>.castor.cdr</tt> file with your generated classes (as generated by
     * the XML code generator).
     * <p>
     * 
     * @param packageName
     *            The package name for the (descriptor) classes
     * @throws ResolverException
     *             If there's a problem loading class descriptors for the given
     *             package.
     */
    void addPackage(final String packageName) throws ResolverException;

    /**
     * Loads class descriptors from the packages specified. The use of this
     * method is useful when no mapping is used, as happens when the domain
     * classes hase been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * <p>
     * Please note that this functionality will work only if you provide the
     * <tt>.castor.cdr</tt> files with your generated classes (as generated by
     * the XML code generator).
     * <p>
     * 
     * @param packageNames
     *            The package names for the (descriptor) classes
     * @throws ResolverException
     *             If there's a problem loading class descriptors for the given
     *             package.
     */
    void addPackages(final String[] packageNames) throws ResolverException;

    /**
     * Loads class descriptors from the package specified. The use of this
     * method is useful when no mapping is used, as happens when the domain
     * classes hase been generated using the XML code generator (in which case
     * instead of a mapping file class descriptor files will be generated).
     * <p>
     * Please note that this functionality will work only if you provide the
     * <tt>.castor.cdr</tt> file with your generated classes (as generated by
     * the XML code generator).
     * <p>
     * 
     * @param packageName
     *            The package name for the (descriptor) classes
     * @throws ResolverException
     *             If there's a problem loading class descriptors for the given
     *             package.
     * @deprecated Please use e.g. #addPackage(String) instead.
     */
    void loadClassDescriptors(final String packageName) throws ResolverException;

    /**
     * To clear the descriptor cache.
     * @since 1.1.3
     */
    void cleanDescriptorCache();

} // -- ClassDescriptorResolver

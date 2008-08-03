/*
 * Copyright 2007 Joachim Grueneis
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
package org.exolab.castor.xml.util.resolvers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Some helpers used by the resolver commands. This is a utility class which
 * is NOT meant to be instantiated.
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public final class ResolveHelpers {
	private static final Log LOG = LogFactory.getLog(ResolveHelpers.class);
    
    /**
     * A private constructor as this is a utility class without an own state.
     */
	private ResolveHelpers() {}

    /**
     * Qualifies the given <code>fileName</code> with the given
     * <code>packageName</code> and returns the resulting file path.<br>
     * If <code>packageName</code> is <code>null</code> or a zero-length
     * String, this method will return <code>fileName</code>.<br>
     *
     * @param fileName The file name to be qualified.
     * @param packageName The package name to be used for qualifying.
     * @return The qualified file path.
     */
    public static String getQualifiedFileName(String fileName, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return fileName;
        }
        
        StringBuffer result = new StringBuffer();
        result.append(packageName.replace('.', '/'));
        result.append('/');
        result.append(fileName);
        return result.toString();
    } //-- getQualifiedFileName

    /**
     * Gets the package name of the given class name.
     *
     * @param className
     *            The class name to retrieve the package name from.
     * @return The package name or the empty String if <code>className</code>
     *         is <code>null</code> or does not contain a package.
     */
    public static String getPackageName(String className) {
        if (className == null) {
            return "";
        }

        int idx = className.lastIndexOf('.');
        if (idx >= 0) {
            return className.substring(0, idx);
        }
        return "";
    } //-- getPackageName

    /**
     * Compares the two strings for equality. A Null and empty
     * strings are considered equal.
     *
     * @return true if the two strings are considered equal.
     */
    public static boolean namespaceEquals(String ns1, String ns2) {
        if (ns1 == null) {
            return ns2 == null || ns2.length() == 0;
        }

        if (ns2 == null) {
            return ns1.length() == 0;
        }

        return ns1.equals(ns2);
    } //-- namespaceEquals

    /**
     * Gets the <code>ClassLoader</code> that's actually to be used (e.g. for
     * loading resources).<br>
     * The actual <code>ClassLoader</code> is determined in the following way:
     * <lu>
     * <li> If the passed in "preferred" loader is not <code>null</code>, it
     * is used.
     * <li> If the loader of this XMLClassDescriptor is not <code>null</code>,
     * it is used.
     * <li> The context class loader of the current thread is used. </lu>
     *
     * @param loader The "preferred" <code>ClassLoader</code>.
     * @return The loader to be used.
     */
    public static ClassLoader getClassLoader(ClassLoader loader) {
        if (loader != null) {
            return loader;
        }

        return Thread.currentThread().getContextClassLoader();
    } //-- getClassLoader
    
    /**
     * Capsulates the ClassLoader.loadClass method to throw no exceptions but return null
     * instead. Any exception caught are logged with info severity.
     * @param classLoader the class loader to use
     * @param className the class to load
     * @return the loaded Class or null
     */
    public static Class loadClass(ClassLoader classLoader, String className) {
        if (classLoader == null) {
            String message = "Argument class loader must not be null";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        if ((className == null) || (className.length() == 0)) {
            LOG.debug("Name of class to load is null or empty -> ignored!");
            return null;
        }
        
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignored problem at loading class: " + className
                        + " through class loader: " + classLoader + ", exception: " + e);
            }
            return null;
        }
    } //-- loadClass
}

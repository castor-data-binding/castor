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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.constants.cpa.JDOConstants;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.util.resolvers.ResolveHelpers;

/**
 * This class tries to resolve a {@link ClassDescriptor} from a package that
 * contains a {@link JDOConstants#PKG_CDR_LIST_FILE} file. This file defines the
 * mapping between a domain class and its corresponding
 * <code>ClassDescriptor</code>.
 * 
 * @author Sebastian Gabmeyer
 * @version 1.2.1
 */
public class ClassResolutionByCDR extends BaseResolutionCommand {

    /** The Logger instance to use. */
    private static final Log LOG = LogFactory
            .getLog(ClassResolutionByCDR.class);

    /**
     * Constructor.
     */
    public ClassResolutionByCDR() {
        addNature(ClassLoaderNature.class.getName());
        addNature(PackageBasedCDRResolutionNature.class.getName());
    }

    /**
     * Resolves a <code>type</code> to a {@link ClassDescriptor} by looking for
     * a {@link JDOConstants#PKG_CDR_LIST_FILE} file in the root directory.
     * 
     * @param type
     *            the <code>class</code> to obtain the
     *            <code>ClassDescriptor</code> for.
     * @return the {@link ClassDescriptor} if found, <code>null</code>
     *         otherwise.
     */
    public ClassDescriptor resolve(final Class type) {
        List packages = new PackageBasedCDRResolutionNature(this)
                .getPackageNames();

        if (packages == null) {
            if (LOG.isDebugEnabled()) {
                LOG
                        .debug("No package names to search descriptors in were passed."
                                + " No descriptors will be loaded!");
            }
            return null;
        }

        String typeName = type.getName();

        for (Iterator it = packages.iterator(); it.hasNext();) {
            Map descriptors = getDescriptors((String) it.next());
            if (descriptors.containsKey(typeName)) {
                return (ClassDescriptor) descriptors.get(typeName);
            }
            // TODO: remove this following code if above implementation is
            // sufficient in regard to performance.
            // String packageName = (String) it.next();
            // URL cdrUrl = loader.getResource(
            // ResolveHelpers
            // .getQualifiedFileName(JDOConstants.PKG_CDR_LIST_FILE,
            // packageName));
            // try {
            // Properties cdrList = getProperties(cdrUrl);
            // Enumeration classes = cdrList.keys();
            // for ( ; classes.hasMoreElements(); ) {
            // String className = (String) classes.nextElement();
            // if (className.equals(typeName)) {
            // String classDescriptorName = cdrList.getProperty(className);
            // Class classDescriptor =
            // ResolveHelpers.loadClass(loader, classDescriptorName);
            // if (classDescriptor != null) {
            // return (ClassDescriptor) classDescriptor.newInstance();
            // }
            // }
            // }
            // } catch (Exception e) {
            // String message = "Failed to load package: " + packageName +
            // "with exception: " + e;
            // LOG.warn(message);
            // ResolverException resolverException =
            // (ResolverException) new ResolverException(message).initCause(e);
            // throw resolverException;
            // }
        }
        return null;
    }

    /**
     * Loads {@link Properties} from the specified <code>URL</code>.
     * 
     * @param url
     *            the location where the properties are located.
     * @return the loaded properties.
     * @throws IOException
     *             if an IOException occurs.
     */
    private Properties getProperties(final URL url) throws IOException {
        Properties properties = new Properties();

        java.io.InputStream stream = url.openStream();
        properties.load(stream);
        stream.close();

        return properties;
    }

    /**
     * Get all descriptors from the package defined by the
     * <code>packageName</code> that contains the
     * {@link JDOConstants#PKG_CDR_LIST_FILE} file.
     * 
     * @param packageName
     *            the package to search descriptors for.
     * @return a {@link java.util.List} of descriptors contained in the package.
     */
    public Map<String, ClassDescriptor> getDescriptors(final String packageName) {
        Map<String, ClassDescriptor> descriptors = new HashMap<String, ClassDescriptor>();

        ClassLoader loader = new ClassLoaderNature(this).getClassLoader();

        URL cdrUrl = loader.getResource(ResolveHelpers.getQualifiedFileName(
                JDOConstants.PKG_CDR_LIST_FILE, packageName));

        Properties cdrList = new Properties();

        try {
            cdrList = getProperties(cdrUrl);
            Enumeration classes = cdrList.keys();
            for (; classes.hasMoreElements();) {
                String className = (String) classes.nextElement();
                String classDescriptorName = cdrList.getProperty(className);
                Class classDescriptor = ResolveHelpers.loadClass(loader,
                        classDescriptorName);
                if (classDescriptor != null) {
                    ClassDescriptor descriptorInstance = (ClassDescriptor) classDescriptor
                            .newInstance();
                    descriptors.put(className, descriptorInstance);
                }
            }
        } catch (Exception e) {
            String message = "Failure occured while loading classes from packacge \""
                    + packageName + "\" with exception: " + e;
            LOG.warn(message);
            throw new RuntimeException(e.getMessage());
        }
        return descriptors;
    }
}

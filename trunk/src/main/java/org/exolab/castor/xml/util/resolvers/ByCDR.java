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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLConstants;

/**
 * Resolves a class by reading the package level class-descriptor-resolver file
 * and loading all descriptors mentioned inside.
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class ByCDR extends AbstractResolverPackageCommand {
	private static final Log LOG = LogFactory.getLog(ByCDR.class);
    
	private ArrayList _loadedPackages = new ArrayList();

	/**
	 * No specific stuff needed.
	 */
	public ByCDR() {
        super();
	}

    /**
     * Creates a Properties object and initializes it with the contents of
     * the given URL. The provided URL must exist and be suitable for loading
     * properties from.
     *
     * @param url The URL to load the properties from. This must not be null!
     * @return The loaded properties.
     * @throws IOException If loading the properties from the given ULR failed.
     *
     * @see Properties
     * @see Properties#load(InputStream)
     */
    private Properties getProperties(URL url) throws java.io.IOException {
        Properties cdrList = new Properties();

        java.io.InputStream stream = url.openStream();
        cdrList.load(stream);
        stream.close();

        return cdrList;
    }

    /**
     * Tries to load the CDR file for the given package name using the provided
     * class loader. If the CDR file is available and could be loaded properly the
     * descriptors listed in it are added to this cache.
     * <br>
     * If a descriptor is listed in the CDR file for the given package but
     * could not be loaded (e.g. because the reference class file is not
     * available) the descriptor is ignored but no exception is thrown.
     * <br>
     * If a CDR file is not available for the given package this method will
     * not load any descriptors and not throw any exceptions.
     * <br>
     * Further calls to this method with the same package name will not be
     * processed.
     * <br>
     * {@inheritDoc}
     */
    protected Map internalResolve(final String packageName, final ClassLoader classLoader,
            final Map properties) throws ResolverException {
        
        HashMap results = new HashMap();
        if (!isEmptyPackageName(packageName) && _loadedPackages.contains(packageName)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Package: " + packageName + " has already been loaded.");
            }
            return results;
        }
        if (!isEmptyPackageName(packageName)) { _loadedPackages.add(packageName); }
        
        URL url = classLoader.getResource(ResolveHelpers.getQualifiedFileName(
                XMLConstants.PKG_CDR_LIST_FILE, packageName));
        if (url == null) { return results; }

        try {
            Properties cdrList = this.getProperties(url);

            final Enumeration classes = cdrList.keys();
            while (classes.hasMoreElements()) {
                String clazzName = (String) classes.nextElement();
                String descriptorClassName = (String) cdrList.get(clazzName);
                try {
                    Class descriptorClass = classLoader.loadClass(descriptorClassName);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found descriptor: " + descriptorClass);
                    }
                    if (descriptorClass != null) {
                        results.put(clazzName, descriptorClass.newInstance());
                    } else if (LOG.isDebugEnabled()) {
                        LOG.debug("Loading of descriptor class: " + descriptorClassName
                                + " for class: " + clazzName + " has failed - continue without");
                    }
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Ignored problem at loading of: " + descriptorClassName
                                + " with exception: " + e);
                    }
                }
            }
        } catch (IOException iox) {
            String message = "Failed to load package: " + packageName + " with exception: " + iox;
            LOG.warn(message);
            throw new ResolverException(message);
        }
        return results;
    }
}

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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLConstants;

/**
 * Tries to load the package mapping file for the given package.
 * <br/>
 * If the mapping file is available and could be loaded properly the
 * descriptors listed in it are added to the description class cache
 * of the configuration.
 * <br/>
 * To disable loading of pacakge mapping files just don't use this
 * command.
 * <br/>
 * If a mapping file is not available for the given package this method
 * will not load any descriptors and not throw any exceptions.<br/>
 * The class loader specified in the configuration is used.
 * <br/>
 * The mapping file - if available - is loaded using the
 * <code>MappingLoader</code>.<br/>
 * <br/>
 * Further calls to this command with the same package name will not be
 * processed.
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class ByPackageMapping extends AbstractResolverPackageCommand {
	private static final Log LOG = LogFactory.getLog(ByPackageMapping.class);
    
    private ArrayList _loadedPackages = new ArrayList();

    /**
     * No specific stuff needed.
     */
	public ByPackageMapping() {
        super();
	}

    /**
     * Loads a package mapping file for the given package name using the
     * provided ClassLoader.
     *
     * @param packageName The name of the package to load the mapping file for.
     * @return The loaded Mapping or <code>null</code> if no mapping file
     *         is available for the given package.
     * @throws MappingException
     */
    private Mapping loadMapping(final String packageName, final ClassLoader classLoader)
    throws MappingException {
        URL url = classLoader.getResource(ResolveHelpers.getQualifiedFileName(
                XMLConstants.PKG_MAPPING_FILE, packageName));
        if (url == null) { return null; }
        try {
            Mapping mapping = new Mapping(classLoader);
            mapping.loadMapping(url);
            return mapping;
        } catch (java.io.IOException ioex) {
            throw new MappingException(ioex);
        }
    }

    /**
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
        
        if (!isEmptyPackageName(packageName)) _loadedPackages.add(packageName);
        try {
            final Mapping mapping = this.loadMapping(packageName, classLoader);
            if (mapping != null) {
                MappingUnmarshaller unmarshaller = new MappingUnmarshaller();
                // TODO: Joachim 2007-09-07 the InternalContext should be set into the unmarshaller!
                MappingLoader mappingLoader = unmarshaller.getMappingLoader(mapping, BindingType.XML);
                Iterator descriptors = mappingLoader.descriptorIterator();
                while (descriptors.hasNext()) {
                    XMLClassDescriptor descriptor = (XMLClassDescriptor) descriptors.next();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found descriptor: " + descriptor);
                    }
                    results.put(descriptor.getJavaClass().getName(), descriptor);
                }
            }
        } catch (MappingException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Ignored exception: " + e + " while loading mapping for package: "
                        + packageName);
            }
        }
        return results;
    }
}

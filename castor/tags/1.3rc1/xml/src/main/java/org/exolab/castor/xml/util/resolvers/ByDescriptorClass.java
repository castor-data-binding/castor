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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLConstants;

/**
 * Resolve a class by looking for a descriptor class 'nearby'. The descriptor
 * class has to follow some specific naming conventions (and maybe be in a
 * special package).
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class ByDescriptorClass extends AbstractResolverClassCommand {
	private static final Log LOG = LogFactory.getLog(ByDescriptorClass.class);

    /**
     * No specific stuff needed.
     */
    public ByDescriptorClass() {
        super();
    }

    /**
     * Tries to load an XMLClassDescriptor directly from an existing .class file.
     * <br>
     * The file that is searched for must be located in the classpath, have the name
     * <code>className</code> + "Descriptor", and contain a valid XMLClassDescriptor.
     * <br>
     * If a descriptor is found it is added to the internal descriptor cache.
     * <br>
     * {@inheritDoc}
     */
    protected Map internalResolve(final String className, final ClassLoader classLoader,
            final Map properties) throws ResolverException {
        
        HashMap results = new HashMap();
        if (classLoader == null) {
            LOG.debug("No class loader available.");
            return results;
        }

        StringBuffer descriptorClassName = new StringBuffer(className);
        descriptorClassName.append(XMLConstants.DESCRIPTOR_SUFFIX);
        Class descriptorClass = ResolveHelpers.loadClass(
                classLoader, descriptorClassName.toString());

        // If we didn't find the descriptor, look in descriptor package
        if (descriptorClass == null) {
            int offset = descriptorClassName.lastIndexOf(".");
            if (offset != -1) {
                descriptorClassName.insert(offset , ".");
                descriptorClassName.insert(offset + 1, XMLConstants.DESCRIPTOR_PACKAGE);
                descriptorClass = ResolveHelpers.loadClass(
                        classLoader, descriptorClassName.toString());
            }
        }
        
        if (descriptorClass != null) {
            try {
                LOG.debug("Found descriptor: " + descriptorClass);
                results.put(className, descriptorClass.newInstance());
            } catch (InstantiationException e) {
                LOG.debug("Ignored exception: " + e + "at loading descriptor class for: "
                        + className);
            } catch (IllegalAccessException e) {
                LOG.debug("Ignored exception: " + e + "at loading descriptor class for: "
                        + className);
            }
        }
        return results;
    }
}

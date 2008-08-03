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
import org.exolab.castor.xml.util.ResolverPackageCommand;
import org.exolab.castor.xml.util.ResolverStrategy;

/**
 * The abstract resolver command provides the argument checking, writes a debug
 * message and reads the class loader from the properties... All specific code
 * is found in the extended classes.
 * 
 * @author Joachim Grueneis, jgrueneis AT gmail DOT com
 * @version $Id$
 * @since 1.2
 */
public abstract class AbstractResolverPackageCommand implements ResolverPackageCommand {
    private static final Log LOG = LogFactory.getLog(AbstractResolverPackageCommand.class);

    /**
     * {@inheritDoc}
     */
    public final Map resolve(final String packageName, final Map properties)
    throws ResolverException {
        String pName;
        if ((packageName == null) || ("".equals(packageName))) {
            LOG.debug("Package name is empty! Anyhow, giving it a try...");
            pName = "";
        } else {
            pName = packageName;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Now in resolve method: " + this.getClass().getName()
                    + " resolving: " + packageName);
        }
        
        ClassLoader classLoader = (ClassLoader) properties.get(
                ResolverStrategy.PROPERTY_CLASS_LOADER);
        if (classLoader == null) {
            LOG.debug("No class loader available.");
            return new HashMap();
        }
        return internalResolve(pName, classLoader, properties);
    }

    /**
     * Is the given package name empty?
     * 
     * @param packageName the package name to check
     * @return true if the String is empty
     */
    protected final boolean isEmptyPackageName(final String packageName) {
        return ((packageName == null) || (packageName.length() == 0) || ("".equals(packageName)));
    }


    /**
     * The required parameter checks are in the public method and here we expect that the 
     * resolve logic itself is implemented.
     * 
     * @param className the name of the class to resolve
     * @param classLoader the class loader to use
     * @param props the resolve properties to use
     * @return a Map of className and XMLClassDescriptor
     * @throws ResolverException in case of unrecoverable problems at resolving
     */
    protected abstract Map internalResolve(String packageName, ClassLoader classLoader, Map props)
    throws ResolverException;
}

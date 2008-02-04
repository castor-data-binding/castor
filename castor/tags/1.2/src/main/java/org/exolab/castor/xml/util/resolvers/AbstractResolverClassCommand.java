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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.util.ResolverClassCommand;
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
public abstract class AbstractResolverClassCommand implements ResolverClassCommand {
    private static final Log LOG = LogFactory.getLog(AbstractResolverClassCommand.class);

    /**
     * {@inheritDoc}
     */
    public final Map resolve(final String className, final Map properties)
    throws ResolverException {
        if ((className == null) || ("".equals(className))) {
            String message = "No class to resolve specified";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Now in method: " + this.getClass().getName() + " resolving: " + className);
        }
        
        ClassLoader classLoader = (ClassLoader) properties.get(
                ResolverStrategy.PROPERTY_CLASS_LOADER);
        return this.internalResolve(className, classLoader, properties);
    }

    /**
     * The required parameter checks are in the public method and here we expect that the 
     * resolve logic itself is implemented.
     * 
     * @param className the name of the class to resolve
     * @param classLoader the class loader to use
     * @param props the resolve properties to use
     * @return a Map of className and XMLClassDescriptor
     * @throws ResolverException if unrecoverable problems in resolve occured
     */
    protected abstract Map internalResolve(String className, ClassLoader classLoader, Map props)
    throws ResolverException;
}

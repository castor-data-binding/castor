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
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.util.ResolverStrategy;

/**
 * Resolve a class by creating a generic descriptor based on the informations
 * read from the class with introspection.
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class ByIntrospection extends AbstractResolverClassCommand {
    /**
     * Logger to be used.
     */
    private static final Log LOG = LogFactory.getLog(ByIntrospection.class);

    /**
     * No specific stuff needed.
     */
    public ByIntrospection() {
        super();
    }
    
    /**
     * Creates an XMLClassDescriptor for the given type by using introspection.
     * This method will rely on the <code>Introspector</code> set with
     * <code>setIntrospector</code>. If a descriptor is successfully created it
     * will be added to the DescriptorCache.
     * <br>
     * <b>NOTE</b>: If this XMLClassDescriptorResolver is NOT configured to use
     * introspection this method will NOT create an descriptor.<br>
     * <br>
     * {@inheritDoc}
     */
    protected Map internalResolve(final String className, final ClassLoader classLoader,
            final Map properties) throws ResolverException {
        
        Boolean useIntrospector = 
            (Boolean) properties.get(ResolverStrategy.PROPERTY_USE_INTROSPECTION);
        HashMap results = new HashMap();
        if (classLoader == null) {
            LOG.debug("No class loader available.");
            return results;
        }
        
        if ((useIntrospector != null) && (!useIntrospector.booleanValue())) {
            // I know the logic is a bit weired... either introspection is explicitly 
            // disabled or it is ok!
            LOG.debug("Introspection is disabled!");
            return results;
        }
        
        Introspector introspector = 
            (Introspector) properties.get(ResolverStrategy.PROPERTY_INTROSPECTOR);
        if (introspector == null) {
            String message = "No Introspector defined in properties!";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        Class clazz = ResolveHelpers.loadClass(classLoader, className);
        if (clazz != null) {
            try {
                XMLClassDescriptor descriptor = introspector.generateClassDescriptor(clazz);
                if (descriptor != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found descriptor: " + descriptor);
                    }
                    results.put(clazz.getName(), descriptor);
                }
            } catch (MarshalException e) {
                String message = "Failed to generate class descriptor for: " 
                    + clazz + " with exception: " + e;
                LOG.warn(message);
                throw new ResolverException(message);
            }
        }
        return results;
    }
}

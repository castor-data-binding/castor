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
package org.exolab.castor.xml.util;

import java.util.Map;

import org.castor.xml.AbstractInternalContext;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;

/**
 * A resolver strategy implements how ClassDescriptor's are found for a
 * given class. It uses multiple ResolveCommand's for first time resolution,
 * but also some caching of already evaluated classes.
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public interface ResolverStrategy {
    /** To set the class loader property for resolving. */
    String PROPERTY_CLASS_LOADER =
        "org.exolab.castor.xml.util.ResolverStrategy.ClassLoader";
    
    /** To set the use introspection property for resolving. */
    String PROPERTY_USE_INTROSPECTION =
        "org.exolab.castor.xml.util.ResolverStrategy.useIntrospection";
    
    /** To set the introspector property for resolving. */
    String PROPERTY_INTROSPECTOR =
        "org.exolab.castor.xml.util.ResolverStrategy.Introspector";
    
    /** To set the LoadPackageMappings property for resolving. */
    String PROPERTY_LOAD_PACKAGE_MAPPINGS =
        "org.exolab.castor.xml.util.ResolverStrategy.LoadPackageMappings";
    
    /** To set the mapping loader property for resolving. */
    String PROPERTY_MAPPING_LOADER =
        "org.exolab.castor.xml.util.ResolverStrategy.MappingLoader";
        
    /**
     * To set properties for strategy and/or commands.
     * 
     * @param key name of the property
     * @param value value the property is set to
     */
    void setProperty(final String key, final Object value);

    /**
     * Implementes a strategy how a class is resolved into a list of class descriptors.
     * 
     * @param resolverResults to put the resolver reszlts into
     * @param className the class to resolve
     * @return the ClassDescriptor for the class or null if the class couldn't be resolved
     * @throws ResolverException in case that resolving fails fatally
     */
    ClassDescriptor resolveClass(final ResolverResults resolverResults, final String className)
    throws ResolverException;
    
    /**
     * Implementes a strategy how a package is resolved into a list of class descriptors.
     * 
     * @param resolverResults to put the resolver reszlts into
     * @param packageName the package to resolve
     * @throws ResolverException in case that resolving fails fatally
     */
    void resolvePackage(ResolverResults resolverResults, String packageName)
    throws ResolverException;

    /**
     * As a strategy generate one or more class descriptors it needs a place
     * to put the results to. This is a minimal interface to give the strategy a
     * place where to put generated class descriptors to.
     * 
     * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
     * @version $Revision$
     */
    public interface ResolverResults {
        /**
         * Adds a descriptor to this caches maps.<br>
         * The descriptor is mapped both with the class name and its XML name.
         * 
         * The descriptor will not be mapped with its XML name is
         * <code>null</code>, the empty string (""), or has the value of the
         * constant INTERNAL_CONTAINER_NAME.
         * 
         * If there already is a descriptor for the given <code>className</code>
         * and/or the descriptor's XML name the previously cached descriptor is
         * replaced.
         * 
         * @param className The class name to be used for mapping the given descriptor.
         * @param descriptor The descriptor to be mapped.
         * 
         * @see #INTERNAL_CONTAINER_NAME
         */
        void addDescriptor(String className, XMLClassDescriptor descriptor);

        /**
         * To add not only a single descriptor but a map of descriptors at once.
         * 
         * @param descriptors a Map of className (String) and XMLClassDescriptor pairs
         */
        void addAllDescriptors(Map descriptors);

        /**
         * Gets the descriptor that is mapped to the given class name.
         * 
         * @param className The class name to get a descriptor for.
         * @return The descriptor mapped to the given name or <code>null</code>
         *         if no descriptor is stored in this cache.
         */
        XMLClassDescriptor getDescriptor(String className);
    }
}

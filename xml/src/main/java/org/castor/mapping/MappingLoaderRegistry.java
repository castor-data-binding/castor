/*
 * Copyright 2005 Ralf Joachim, Werner Guttmann
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
package org.castor.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.CoreProperties;
import org.castor.core.util.AbstractProperties;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class MappingLoaderRegistry {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(MappingLoaderRegistry.class);
    
    /** The cached mapping loader factories. */
    private final List  _mappingLoaderFactories = new ArrayList();

    /** Already loaded mapping loaders. */
    private final List _mappingLoaders = new ArrayList();
    
    /**
     * Creates an instance of this registry, loading the mapping loader
     * factories from the castor.properties file. 
     * @param properties Properties.
     */
    public MappingLoaderRegistry(final AbstractProperties properties) {
        Object[] objects = properties.getObjectArray(
                CoreProperties.MAPPING_LOADER_FACTORIES, getClass().getClassLoader());
        for (int i = 0; i < objects.length; i++) {
            _mappingLoaderFactories.add(objects[i]);
        }
    }

    /**
     * Deletes all 'cached' mapping loader factories.
     */
    public void clear() {
        Iterator iter = _mappingLoaders.iterator();
        while (iter.hasNext()) { 
            ((MappingLoader) iter.next()).clear(); 
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns a mapping loader for the suitable source and binding type. The engine's
     * specific mapping loader is used to create binding specific descriptors.
     * The mapping loader is cached in memory and returned in subsequent method calls.
     *
     * @param sourceType The type of the mapping source.
     * @param bindingType The binding type to load from mapping.
     * @return A mapping loader
     * @throws MappingException A mapping error occured preventing
     *         descriptors from being generated from the loaded mapping
     */
    public MappingLoader getMappingLoader(
            final String sourceType,
            final BindingType bindingType) throws MappingException {
        Iterator iter = _mappingLoaderFactories.iterator();
        while (iter.hasNext()) {
            MappingLoaderFactory loaderFactory = (MappingLoaderFactory) iter.next();
            if (loaderFactory.getSourceType().equals(sourceType)
                    && (loaderFactory.getBindingType() == bindingType)) {
                MappingLoader mappingLoader = loaderFactory.getMappingLoader();
                _mappingLoaders.add(mappingLoader);
                return mappingLoader;
            }
        }
        
        String msg = "No mapping loader/factory for: " + "SourceType=" + sourceType
                   + " / BindingType=" + bindingType;
        LOG.error(msg);
        throw new MappingException(msg);
    }
    
    /**
     * Returns a list of 'cached' mapping loader factories.
     * @return a list of 'cached' mapping loader factories.
     */
    public Collection getMappingLoaderFactories() {
        return Collections.unmodifiableCollection(_mappingLoaderFactories);
    }

    //--------------------------------------------------------------------------
}

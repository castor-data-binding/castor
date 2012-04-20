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
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.util.ResolverStrategy;

/**
 * How to sought a descriptor for a class in a specified mapping loader.
 * 
 * @author <a href="mailto:jgrueneis AT gmail DOT com">Joachim Grueneis</a>
 * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
 * @version $Revision$ $Date$
 * @since 1.2
 */
public class ByMappingLoader extends AbstractResolverClassCommand {
	private static final Log LOG = LogFactory.getLog(ByMappingLoader.class);

    /**
     * No specific stuff needed.
     */
    public ByMappingLoader() {
        super();
	}

    /**
     * If a mapping loader is set in the configuration the descriptor for the given
     * class / className is taken from the mapping loader and put into the cache.
     * <br>
     * {@inheritDoc}
     */
    protected Map internalResolve(final String className, final ClassLoader classLoader,
            final Map properties) throws ResolverException {
        
        MappingLoader mappingLoader = (MappingLoader)properties.get(ResolverStrategy.PROPERTY_MAPPING_LOADER);
        HashMap results = new HashMap();
        if (mappingLoader == null) {
            LOG.debug("No mapping loader specified");
            return results;
        }
        
        XMLClassDescriptor descriptor = (XMLClassDescriptor) mappingLoader.getDescriptor(className);
        if (descriptor != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found descriptor: " + descriptor);
            }
            results.put(className, descriptor);
        }
        return results;
    }
}

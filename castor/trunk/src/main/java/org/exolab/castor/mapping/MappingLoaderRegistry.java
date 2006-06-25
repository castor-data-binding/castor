/*
 * Copyright 2005 Ralf Joachim
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
package org.exolab.castor.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class MappingLoaderRegistry {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(MappingLoaderRegistry.class);
    
    /** The cached mapping loaders. */
    private final List  _mappingLoaders = new ArrayList();

    //--------------------------------------------------------------------------

    public MappingLoaderRegistry(final Configuration config, final ClassLoader loader) {
        String prop = config.getProperty(ConfigKeys.MAPPING_LOADERS, "");
        StringTokenizer tokenizer = new StringTokenizer(prop, ", ");
        while (tokenizer.hasMoreTokens()) {
            String classname = tokenizer.nextToken();
            try {
                Class cls = loader.loadClass(classname);
                Class[] types = new Class[] { ClassLoader.class };
                Object[] params = new Object[] { loader };
                Object obj = cls.getConstructor(types).newInstance(params);
                _mappingLoaders.add(obj);
            } catch (Exception ex) {
                LOG.error("Problem instantiating mapping loader implementation: "
                        + classname, ex);
            }
        }
    }

    public void clear() {
        Iterator iter = _mappingLoaders.iterator();
        while (iter.hasNext()) { ((MappingLoader) iter.next()).clear(); }
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
        Iterator iter = _mappingLoaders.iterator();
        while (iter.hasNext()) {
            MappingLoader loader = (MappingLoader) iter.next();
            if (loader.getSourceType().equals(sourceType)
                    && (loader.getBindingType() == bindingType)) {
                return loader;
            }
        }
        
        String msg = "No mapping loader for: " + "SourceType=" + sourceType
                   + " / BindingType=" + bindingType;
        LOG.error(msg);
        throw new MappingException(msg);
    }
    
    public Collection getMappingLoaders() {
        return Collections.unmodifiableCollection(_mappingLoaders);
    }

    //--------------------------------------------------------------------------
}

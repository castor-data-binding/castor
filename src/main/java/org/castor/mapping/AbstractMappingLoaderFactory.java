/*
 * Copyright 2005 Werner Guttmann
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;

/**
 * Abstract base class for MappingLoaderFactory instances
 * 
 * @author me
 * 
 */
public abstract class AbstractMappingLoaderFactory implements
        MappingLoaderFactory {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging </a> instance used for all logging.
     */
    public static final Log LOG = LogFactory
            .getLog(AbstractMappingLoaderFactory.class);

    /**
     * Source type definition
     */
    private static final String SOURCE_TYPE = "CastorXmlMapping";

    /**
     * @inheritDoc
     * @see org.castor.mapping.MappingLoaderFactory#getSourceType()
     */
    public final String getSourceType() {
        return SOURCE_TYPE;
    }

    /**
     * To obtain the class name of the MappingLoader to instantiate
     * 
     * @return The class name of the MappingLoader to instantiate
     */
    public abstract String getClassname();

    /**
     * @inheritDoc
     * @see org.castor.mapping.MappingLoaderFactory#getMappingLoader()
     */
    public final MappingLoader getMappingLoader() throws MappingException {
        MappingLoader mappingLoader = null;
        try {
            ClassLoader loader = getClass().getClassLoader();
            Class cls = loader.loadClass(getClassname());
            Class[] types = new Class[] {ClassLoader.class};
            Object[] args = new Object[] {loader};
            mappingLoader = (MappingLoader) cls.getConstructor(types).newInstance(args);
        } catch (Exception ex) {
            LOG.error(
                    "Problem instantiating mapping loader factory implementation: "
                            + getClassname(), ex);
        }
        return mappingLoader;
    }

}

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

/**
 * An XML-specific factory for acquiring MappingLoader instances.
 * 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2006-04-13 10:49:49 -0600 (Thu, 13 Apr
 *          2006) $
 * @since 1.0.4
 */
public class XMLMappingLoaderFactory extends AbstractMappingLoaderFactory {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging </a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(XMLMappingLoaderFactory.class);

    /** The name of the factory. */
    public static final String NAME = "XML";

    /**
     * Class name of the XML mapping loader
     */
    private static final String CLASS_NAME = "org.exolab.castor.xml.XMLMappingLoader";

    /**
     * @inheritDoc
     * @see org.castor.mapping.MappingLoaderFactory#getName()
     */
    public final String getName() {
        return NAME;
    }

    /**
     * @inheritDoc
     * @see org.castor.mapping.AbstractMappingLoaderFactory#getClassname()
     */
    public String getClassname() {
        return CLASS_NAME;
    }

    /**
     * @inheritDoc
     * @see org.castor.mapping.MappingLoaderFactory#getBindingType()
     */
    public BindingType getBindingType() {
        return BindingType.XML;
    }

}
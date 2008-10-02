/*
 * Copyright 2005-2008 Werner Guttmann
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
package org.exolab.castor.builder.printing;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.builder.BuilderConfiguration;

/**
 * Registry for {@link JClassPrinterFactory} implementations obtained from the
 * Castor XML code generator property file and used by the XML code generator during
 * its operation.
 * 
 * @author <a href="mailto:Werner.Guttmann@morganstanley.com">Werner Guttmann</a>
 * @version $Revision: 7134 $ $Date: 2006-04-08 08:58:10 -0600 (Sat, 08 Apr 2006) $
 * @since 1.2.1
 */
public class JClassPrinterFactoryRegistry {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private final Log _log = LogFactory.getLog(JClassPrinterFactoryRegistry.class);

    /** Association between name of implementation and JClassPrinterFactory instance. */
    private Map<String, JClassPrinterFactory> _factories = new HashMap<String, JClassPrinterFactory>();

    //--------------------------------------------------------------------------

    /**
     * Construct an instance of JClassPrinterFactoryRegistry that loads the
     * {@link JClassPrinterFactory} implementations specified in the given
     * Configuration.
     * 
     * @param config The LocalConfiguration.
     */
    public JClassPrinterFactoryRegistry(final BuilderConfiguration config) {
        String jClassPrinterFactories = config.getJClassPrinterFactories();
        String[] factoryClassNames = jClassPrinterFactories.split(",");
        
        for (int i = 0; i < factoryClassNames.length; i++) {
            JClassPrinterFactory factory;
            try {
                factory = (JClassPrinterFactory) 
                    Class.forName(factoryClassNames[i]).newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("Property entry '" 
                        + factoryClassNames[i] + "' does"
                        + " not represent a valid class name.");
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Property entry '" 
                        + factoryClassNames[i] + "' does" 
                        + " not represent a valid class name.");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Property entry '" 
                        + factoryClassNames[i] + "' does" 
                        + " not represent a valid class name.");
            }
            _factories.put(factory.getName(), factory);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the names of all the configured {@link JClassPrinterFactory}
     * implementations. A {@link JClassPrinterFactory} instance can be obtained
     * by the {@link #getJClassPrinterFactory} method.
     *
     * @return Names of {@link JClassPrinterFactory} implementations
     */
    public String[] getJClassPrinterFactoryNames() {
        String[] names = new String[_factories.size()];
        return _factories.keySet().toArray(names);
    }

    /**
     * Returns a {@link JClassPrinterFactory} with the specified name. Returns
     * null if the named factory is not supported.
     *
     * @param name The name of the JClassPrinterFactory.
     * @return The {@link JClassPrinterFactory} or null if none exists.
     */
    public JClassPrinterFactory getJClassPrinterFactory(final String name) {
        Object factory = _factories.get(name);
        if (factory == null) {
            String msg = "The JClassPrinterFactory '" + name + "' "
                       + "does not exist in the Castor XML code generator properties file "
                       + "and is therefore not supported.";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        return (JClassPrinterFactory) factory;
    }

    //--------------------------------------------------------------------------
}

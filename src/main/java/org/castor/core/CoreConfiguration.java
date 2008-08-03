/*
 * Copyright 2007 Ralf Joachim
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
 *
 * $Id: Configuration.java 6907 2007-03-28 21:24:52Z rjoachim $
 */
package org.castor.core;

import org.castor.core.util.Configuration;

/**
 * Castor configuration of core modul.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public final class CoreConfiguration extends Configuration {
    //--------------------------------------------------------------------------

    /** Path to Castor configuration of core modul. */
    private static final String FILEPATH = "/org/castor/core/";

    /** Name of Castor configuration of core modul. */
    private static final String FILENAME = "castor.core.properties";
    
    //--------------------------------------------------------------------------

    /**
     * Default constructor. Application and domain class loaders will be initialized to the one
     * used to load the Configuration class. No parent configuration will be set.
     * <br/>
     * Note: This constructor is not intended for public use. Use one of the newInstance() methods
     * of the modul specific configuration instead.
     */
    public CoreConfiguration() {
        super();
        loadDefaultProperties(FILEPATH, FILENAME);
    }
    
    /**
     * Construct a configuration that uses the specified class loaders. No parent configuration
     * will be set.
     * <br/>
     * Note: This constructor is not intended for public use. Use one of the newInstance() methods
     * of the modul specific configuration instead.
     * 
     * @param app Classloader to be used for all classes of Castor and its required libraries.
     * @param domain Classloader to be used for all domain objects.
     */
    public CoreConfiguration(final ClassLoader application, final ClassLoader domain) {
        super(application, domain);
        loadDefaultProperties(FILEPATH, FILENAME);
    }
    
    //--------------------------------------------------------------------------
    
    // Specify public keys of core configuration properties here.
    
    /** Property listing all available {@link org.exolab.castor.mapping.MappingLoader}
     *  implementations (<tt>org.castor.mapping.Loaders</tt>). */
    public static final String MAPPING_LOADER_FACTORIES =
        "org.castor.mapping.loaderFactories";

    //--------------------------------------------------------------------------
}

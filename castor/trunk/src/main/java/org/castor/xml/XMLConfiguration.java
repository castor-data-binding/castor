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
package org.castor.xml;

import org.castor.core.CoreConfiguration;
import org.castor.core.util.CastorConfiguration;
import org.castor.core.util.Configuration;

/**
 * Castor configuration of XML modul.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public final class XMLConfiguration extends Configuration {
    //--------------------------------------------------------------------------

    /** Path to Castor configuration of core modul. */
    private static final String FILEPATH = "/org/castor/xml/";

    /** Name of Castor configuration of core modul. */
    private static final String FILENAME = "castor.xml.properties";
    
    /** A static configuration instance to be used during migration to a none static one. */
    // TODO Remove property after support for static configuration has been terminated.
    private static Configuration _instance = null;

    //--------------------------------------------------------------------------
    
    /**
     * Get the one and only static XML configuration.
     * 
     * @return One and only configuration instance for Castor XML modul.
     * @deprecated Don't limit your applications flexibility by using a static configuration. Use
     *             your own configuration instance created with one of the newInstance() methods
     *             instead.
     */
    // TODO Remove method after support for static configuration has been terminated.
    public static synchronized Configuration getInstance() {
        if (_instance == null) { _instance = newInstance(); }
        return _instance;
    }
    
    /**
     * Factory method for a default XML configuration instance. Application and domain class
     * loaders will be initialized to the one used to load the Configuration class. The
     * configuration instance returned will be a CastorConfiguration with a XMLConfiguration and
     * a CoreConfiguration instance as parents. The CastorConfiguration holding user specific
     * properties is the only one that can be modified by put() and remove() methods.
     * XMLConfiguration and CoreConfiguration are responsble to deliver Castor's default values
     * if they have not been overwritten by the user.
     * 
     * @return Configuration instance for Castor XML modul.
     */
    public static Configuration newInstance() {
        Configuration core = new CoreConfiguration();
        Configuration xml = new XMLConfiguration(core);
        Configuration castor = new CastorConfiguration(xml);
        return castor;
    }
    
    /**
     * Factory method for a XML configuration instance that uses the specified class loaders. The
     * configuration instance returned will be a CastorConfiguration with a XMLConfiguration and
     * a CoreConfiguration instance as parents. The CastorConfiguration holding user specific
     * properties is the only one that can be modified by put() and remove() methods.
     * XMLConfiguration and CoreConfiguration are responsble to deliver Castor's default values
     * if they have not been overwritten by the user.
     * 
     * @param app Classloader to be used for all classes of Castor and its required libraries.
     * @param domain Classloader to be used for all domain objects.
     * @return Configuration instance for Castor XML modul.
     */
    public static Configuration newInstance(final ClassLoader app, final ClassLoader domain) {
        Configuration core = new CoreConfiguration(app, domain);
        Configuration xml = new XMLConfiguration(core);
        Configuration castor = new CastorConfiguration(xml);
        return castor;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Construct a configuration with given parent. Application and domain class loaders will be
     * initialized to the ones of the parent. 
     * <br/>
     * Note: This constructor is not intended for public use. Use one of the newInstance() methods
     * instead.
     * 
     * @param parent Parent configuration.
     */
    public XMLConfiguration(final Configuration parent) {
        super(parent);
        loadDefaultProperties(FILEPATH, FILENAME);
    }
    
    //--------------------------------------------------------------------------
    
    // Specify public keys of XML configuration properties here.
    
    //--------------------------------------------------------------------------
}

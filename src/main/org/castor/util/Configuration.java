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
 *
 * $Id$
 */
package org.castor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to hold Castor configuration properties.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class Configuration {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Configuration.class);

    /** Path to default configuration in Castor JAR. */
    private static final String FILEPATH = "/org/exolab/castor/";
    
    /** Name of Castor configuration file. */
    private static final String FILENAME = "castor.properties";

    /** The configuration instance. */
    private static Configuration    _config = null;
    
    /** The configured properties. */
    private Properties              _props = new Properties();
    
    //--------------------------------------------------------------------------

    /**
     * Get the one and only configuration instance. If not done yet, a new configuration
     * will be constructed and default properties will be loaded.
     * 
     * @return The configuration instance.
     */
    public static Configuration getInstance() {
        if (_config == null) { _config = new Configuration(); }
        return _config;
    }
    
    /**
     * Dispose the one and only configuration instance.
     */
    public static void disposeConfiguration() {
        _config = null;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Create a new Configuration instance loading properties from default location.
     */
    public Configuration() {
        loadDefaultProperties();
    }
    
    /**
     * Create a new Configuration instance loading properties from given location.
     * 
     * @param filename  Absolute or relative filename of the properties file.
     */
    public Configuration(final String filename) {
        loadProperties(filename);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Load properties from default location.
     */
    public void loadDefaultProperties() {
        boolean found = false;
        
        // Get detault configuration from the Castor JAR.
        try {
            _props.load(getClass().getResourceAsStream(FILEPATH + FILENAME));
            found = true;
        }  catch (Exception ex) {
            // As we will be trying something else later, record the error for setup
            // purposes, but do not actually treat as a critical failure.
            LOG.warn("Non-critical error during Castor configuration load:", ex);
        }

        // Get overriding configuration from the Java library directory, ignore if not
        // found. If found merge existing properties.
        try {
            String javaHome = System.getProperty("java.home");
            if (javaHome != null) {
                File file = new File(new File(javaHome, "lib"), FILENAME);
                if (file.exists()) {
                    _props.load(new FileInputStream(file));
                    found = true;
                }      
            }
        } catch (SecurityException ex) {
            // Not a critical error, but users will need to know if they need to change
            // their config.
            LOG.warn("Security policy prevented access to Castor configuration:", ex);
        } catch (Exception ex) {
            // As we will be trying something else later, record the error for setup
            // purposes, but do not actually treat as a critical failure.
            LOG.warn("Non-critical error during Castor configuration load:", ex);
        }

        // Cannot find any castor.properties file(s).
        if (!found) {
            String msg = "Could not obtain the default configuration file '"
                       + FILENAME + "' from the Castor JAR.";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
        
        // Get overriding configuration from the classpath, ignore if not found. If
        // found, merge any existing properties.
        try {      
            URL url = getClass().getResource("/" + FILENAME);
            if (url != null) {
                _props.load(url.openStream());
                return;
            }      
        } catch (Exception ex) {
            // As we have previously loaded default configuration treat as a critical
            // failure.
            LOG.warn("Non-critical error during Castor configuration load:", ex);
        }
        
        // If not found, either it doesn't exist, or "." is not part of the class path,
        // try looking at local working directory.
        try {      
            File file = new File(FILENAME);
            if (file.exists() && file.canRead()) {
                _props.load(new FileInputStream(file));
            }
        } catch (Exception ex) {
            // As we have previously loaded default configuration treat as a critical
            // failure.
            LOG.warn("Non-critical error during Castor configuration load:", ex);
        }
    }

    /**
     * Load properties from given filename. It first tries to interpret filename as
     * absolute resource location. If this fails it's assumed that filename should be
     * interpreted relative to classpath. 
     * 
     * @param filename  Absolute or relative filename of the properties file.
     */
    public void loadProperties(final String filename) {
        URL url = null;
        try {
            try {
                url = new URL(filename);
            } catch (MalformedURLException ex) {
                url = getClass().getClassLoader().getResource(filename);
            }
            if (url != null) {
                _props.load(url.openStream());
            } else {
                String msg = "Could not obtain the configuration file '"
                           + filename + "' from the Castor JAR.";
                LOG.error(msg);
                throw new RuntimeException(msg);
            }
        } catch (IOException ex) {
            String msg = "Could not read the configuration file '"
                       + url.toExternalForm() + "' from the Castor JAR.";
            LOG.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Get the configured properties.
     * 
     * @return The configured properties.
     */
    public Properties getProperties() {
        return _props;
    }
    
    /**
     * Get property with given name as string or if property is not available return
     * the given default string. 
     * 
     * @param name          Name of the property.
     * @param defaultValue  Default string to return if property is not available.
     * @return The configured string property or the default string if property is
     *         not available.
     */
    public String getProperty(final String name, final String defaultValue) {
        String value = _props.getProperty(name);
        return (value == null) ? defaultValue : value;
    }
    
    /**
     * Get property with given name as string array or if property is not available
     * return an empty string array. 
     * 
     * @param name          Name of the property.
     * @return The string array of configured property.
     */
    public String[] getProperty(final String name) {
        String value = _props.getProperty(name);
        if (value == null) { return new String[] {}; }
        String[] array = value.split(",");
        for (int i = 0; i < array.length; i++) { array[i] = array[i].trim(); }
        return array;
    }
    
    /**
     * Get property with given name as int value. If property is not available or can
     * not be interpreted as integer the given default int value will be returned. 
     * 
     * @param name          Name of the property.
     * @param defaultValue  Default int value to return if property is not available or
     *                      can not be interpreted as integer.
     * @return The configured int property or the default int value if property is
     *         not available or can not be interpreted as integer.
     */
    public int getProperty(final String name, final int defaultValue) {
        String value = _props.getProperty(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    /**
     * Get property with given name as boolean value. If property is not available or
     * does not equal 'true' or 'false' the given default boolean value will be returned. 
     * 
     * @param name          Name of the property.
     * @param defaultValue  Default boolean value to return if property is not available
     *                      or does not equal 'true' or 'false'
     * @return The configured boolean property or the default boolean value if property
     *         is not available or does not equal 'true' or 'false'.
     */
    public boolean getProperty(final String name, final boolean defaultValue) {
        String value = _props.getProperty(name);
        if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            return defaultValue;
        }
    }

    //--------------------------------------------------------------------------
}

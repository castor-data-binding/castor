/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * Handle the configuration for DDL generator including load configuration files,
 * manage configuration values.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public class Configuration {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Configuration.class);

    /** String representation of boolean <code>true</code>. */
    public static final String TRUE = "true";

    /** String representation of boolean <code>false</code>. */
    public static final String FALSE = "false";

    //--------------------------------------------------------------------------

    /** 
     * handle configuration.
     */
    private final Properties _conf = new Properties();

    //--------------------------------------------------------------------------

    /**
     * Constructor for Configuration.
     */
    public Configuration() {
        super();
        
        addProperties(System.getProperties());
    }

    //--------------------------------------------------------------------------

    /**
     * get boolean value associated with key in the configuration files.
     * 
     * @param key key
     * @return return value associated with key. If not exists, throw an
     *         exception
     * @throws WrongFormatException format error
     * @throws KeyNotFoundException key error
     */
    public final boolean getBoolValue(final String key) throws WrongFormatException,
            KeyNotFoundException {
        String value = this.getStringValue(key);

        if (value == null) {
            throw new KeyNotFoundException("can not found key " + key);
        }

        if (TRUE.equals(value)) {
            return true;
        }

        if (FALSE.equals(value)) {
            return false;
        }

        throw new WrongFormatException("require boolean (true/false), receive "
                + value + " for key=" + key);
    }

    /**
     * get boolean value associated with key in the configuration files.
     * 
     * @param key key
     * @param defaultValue default value
     * @return return value associated with key. If not exists, return the default value
     */
    public final boolean getBoolValue(final String key, final boolean defaultValue) {
        String value = null;
        try {
            value = this.getStringValue(key);
        } catch (KeyNotFoundException e) {
            return defaultValue;
        }

        if (value == null) {
            return defaultValue;
        }

        if (TRUE.equals(value)) {
            return true;
        }

        if (FALSE.equals(value)) {
            return false;
        }

        return defaultValue;
    }

    /**
     * Get property with given name as Integer value. If property is not
     * available or can not be interpreted as integer null will be returned.
     * 
     * @param name Name of the property.
     * @return The configured Integer property or null if property is not
     *         available or can not be interpreted as integer.
     */
    public final Integer getInteger(final String name) {
        String value = _conf.getProperty(name);
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * get String value associated with key in the configuration files.
     * 
     * @param key key
     * @return return value associated with key. If not exists, throw an
     *         exception
     * @throws KeyNotFoundException key error
     */
    public final String getStringValue(final String key) throws KeyNotFoundException {
        String value = (String) _conf.get(key);
        if (value == null || "".equals(value)) {
            throw new KeyNotFoundException(
                    "Can not find value correspondence to " + key);
        }
        return value;
    }

    /**
     * get String value associated with key in the configuration files.
     * 
     * @param key key
     * @param defaultValue default value
     * @return return value associated with key. If not exists, return default
     *         value
     */
    public final String getStringValue(final String key, final String defaultValue) {
        String value = (String) _conf.get(key);
        if (value == null || "".equals(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * add properties (key, value) for configuration, the existed item will
     * be overwrited.
     * @param props properties
     */
    public final void addProperties(final Properties props) {
        if (props != null) {
            Object key;
            Object value;
            for (Enumeration e = props.keys(); e.hasMoreElements(); ) {
                key = e.nextElement();
                value = props.get(key);
                _conf.put(key, value);
            }
        }
    }

    /**
     * add properties (key, value) for configuration, the existed item will
     * be overwrited.
     * 
     * @param filename a properties file
     * @throws GeneratorException generator error
     */
    public final void addProperties(final String filename) throws GeneratorException {
        Properties props = new Properties();
        URL url = null;
        try {
            try {
                url = new URL(filename);
            } catch (MalformedURLException ex) {
                url = getClass().getClassLoader().getResource(filename);
            }
            if (url != null) {
                props.load(url.openStream());
                addProperties(props);
            } else {
                String msg = "Could not obtain the configuration file '"
                           + filename + "' from the Castor JAR.";
                LOG.error(msg);
                throw new GeneratorException(msg);
            }
        } catch (IOException ex) {
            String msg = "Could not read the configuration file '"
                       + url.toExternalForm() + "' from the Castor JAR.";
            LOG.error(msg, ex);
            throw new GeneratorException(msg, ex);
        }
    }

    /**
     * set property value, this will overwrite the loaded value.
     * 
     * @param key key
     * @param value value
     */
    public final void setProperty(final String key, final String value) {
        _conf.put(key, value);
    }

    //--------------------------------------------------------------------------
}

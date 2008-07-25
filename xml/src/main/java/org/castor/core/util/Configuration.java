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
package org.castor.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class to hold Castor configuration properties.
 * 
 * @version $Id: Configuration.java,v 1.8 2006/03/08 17:25:52 jens Exp $
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @since 1.1.3
 */
public abstract class Configuration {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Configuration.class);
    
    /** Classloader to be used for all classes of Castor and its required libraries. */
    private final ClassLoader _applicationClassLoader;
    
    /** Classloader to be used for all domain objects that are marshalled/unmarshalled or
     *  loaded from the database. */
    private final ClassLoader _domainClassLoader;
    
    /** Parent configuration. */
    private final Configuration _parent;
    
    /** Properties map. */
    private final Map _map = new HashMap();
    
    //--------------------------------------------------------------------------
    
    /**
     * Default constructor. Application and domain class loaders will be initialized to the one
     * used to load the Configuration class. No parent configuration will be set.
     */
    protected Configuration() {
        this(null, null);
    }
    
    /**
     * Construct a configuration that uses the specified class loaders. No parent configuration
     * will be set.
     * 
     * @param app Classloader to be used for all classes of Castor and its required libraries.
     * @param domain Classloader to be used for all domain objects.
     */
    protected Configuration(final ClassLoader app, final ClassLoader domain) {
        _applicationClassLoader = (app != null) ? app : getClass().getClassLoader();
        _domainClassLoader = (domain != null) ? domain : getClass().getClassLoader();
        
        _parent = null;
    }
    
    /**
     * Construct a configuration with given parent. Application and domain class loaders will be
     * initialized to the ones of the parent. 
     * 
     * @param parent Parent configuration.
     */
    protected Configuration(final Configuration parent) {
        _applicationClassLoader = parent.getApplicationClassLoader();
        _domainClassLoader = parent.getDomainClassLoader();
        
        _parent = parent;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Get classloader to be used for all classes of Castor and its required libraries.
     * 
     * @return Classloader to be used for all classes of Castor and its required libraries.
     */
    public final ClassLoader getApplicationClassLoader() {
        return _applicationClassLoader;
    }
    
    /**
     * Get classloader to be used for all domain objects that are marshalled/unmarshalled or
     * loaded from the database.
     * 
     * @return Classloader to be used for all domain objects.
     */
    public final ClassLoader getDomainClassLoader() {
        return _domainClassLoader;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Load module configuration from default locations.
     * <br/>
     * First it loads default configuration contained in Castor JAR. This gets overwritten
     * by a configuration found on Java library directory. If no configuration could be found
     * until that point a ConfigurationException will be thrown.
     * 
     * @param path Path to the default configuration to load.
     * @param filename Name of the configuration file.
     */
    protected void loadDefaultProperties(final String path, final String filename) {
        Properties properties = new Properties();
        
        // Get default configuration from the Castor JAR.
        boolean inCastorJar = loadFromClassPath(properties, path + filename);

        // Get overriding configuration from the Java library directory, ignore if not
        // found. If found merge existing properties.
        boolean inJavaLibDir = loadFromJavaHome(properties, filename);

        // Couldn't find configuration in Castor jar nor Java library directory.
        if (!inCastorJar && !inJavaLibDir) {
            throw new ConfigurationException("Failed to load configuration: " + filename);
        }
        
        _map.putAll(properties);
    }
    
    /**
     * Load common user configuration from classpath root and current working directory.
     * <br/>
     * First it loads default configuration contained in Castor JAR. This gets overwritten
     * by a configuration found on Java library directory. If no configuration could be found
     * until that point a ConfigurationException will be thrown. At last an overriding
     * configuration is loaded from root of classpath or, if that could not be found, from
     * local working directory.
     * 
     * @param filename Name of the configuration file.
     */
    protected void loadUserProperties(final String filename) {
        Properties properties = new Properties();
        
        // Get common configuration from the classpath root, ignore if not found.
        boolean onClasspathRoot = loadFromClassPath(properties, "/" + filename);
        
        // If not found on classpath root, either it doesn't exist, or "." is not part of
        // the classpath, try looking at local working directory.
        if (!onClasspathRoot) { loadFromWorkingDirectory(properties, filename); }
        
        _map.putAll(properties);
    }
    
    /**
     * Load properties with given filename from classpath and merge them into the given properties.
     * 
     * @param properties Properties to merge the loaded ones into.
     * @param filename Name of the properties file to load from classpath.
     * @return <code>true</code> if properties could be loaded, <code>false</code> otherwise.
     */
    private boolean loadFromClassPath(final Properties properties, final String filename) {
        InputStream classPathStream = null;
        try {      
            URL url = getClass().getResource(filename);
            if (url != null) {
                classPathStream = url.openStream(); 
                properties.load(classPathStream);
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Configuration loaded from classpath: " + filename);
                }
                
                return true;
            }
            return false;
        } catch (Exception ex) {
            LOG.warn("Failed to load configuration from classpath: " + filename, ex);
            return false;
        } finally {
            if (classPathStream != null) {
                try {
                    classPathStream.close();
                } catch (IOException e) {
                    LOG.warn("Failed to close configuration from classpath: " + filename);
                }
            }
        }
    }
    
    /**
     * Load properties with given filename from Java library directory and merge them into
     * the given properties.
     * 
     * @param properties Properties to merge the loaded ones into.
     * @param filename Name of the properties file to load from Java library directory.
     * @return <code>true</code> if properties could be loaded, <code>false</code> otherwise.
     */
    private boolean loadFromJavaHome(final Properties properties, final String filename) {
        try {
            String javaHome = System.getProperty("java.home");
            if (javaHome == null) { return false; }
            return loadFromFile(properties, new File(new File(javaHome, "lib"), filename));
        } catch (SecurityException ex) {
            LOG.warn("Security policy prevented access to system property 'java.home'.", ex);
            return false;
        }
    }
    
    /**
     * Load properties with given filename from local working directory and merge them into
     * the given properties.
     * 
     * @param properties Properties to merge the loaded ones into.
     * @param filename Name of the properties file to load from local working directory.
     * @return <code>true</code> if properties could be loaded, <code>false</code> otherwise.
     */
    private boolean loadFromWorkingDirectory(final Properties properties, final String filename) {
        return loadFromFile(properties, new File(filename));
    }
    
    /**
     * Load properties with given file and merge them into the given properties.
     * 
     * @param properties Properties to merge the loaded ones into.
     * @param file Properties file to load.
     * @return <code>true</code> if properties could be loaded, <code>false</code> otherwise.
     */
    private boolean loadFromFile(final Properties properties, final File file) {
        InputStream fileStream = null;
        try {      
            if (file.exists() && file.canRead()) {
                fileStream = new FileInputStream(file); 
                properties.load(fileStream);
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Configuration file loaded: " + file);
                }
                
                return true;
            }
            return false;
        } catch (SecurityException ex) {
            LOG.warn("Security policy prevented access to configuration file: " + file, ex);
            return false;
        } catch (Exception ex) {
            LOG.warn("Failed to load configuration file: " + file, ex);
            return false;
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    LOG.warn("Failed to close configuration file: " + file);
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Put given value associated with given key into the properties map of this configuration. If
     * the configuration previously associated the key to another value the previous value will be
     * returned. If a mapping for the key previously exist in the parent configuration only, the
     * method returns <code>null</code> and not the value of the parent. This allows to distingush
     * if the mapping existed in this configuration or one of its parents.
     * <br/>
     * Putting a value in this configuration does not change the value of its parent but the
     * parents value isn't visible any more as it gets overwritten by this configurations one.
     * While this allows to redifine the value of a property it isn't allowed to undefine it.
     * Therefore a <code>NullPointerException</code> will be thrown if the given value is
     * <code>null</code>.
     * 
     * @param key Key of the property to put into configuration.
     * @param value Value to put into configuration associated with the given key..
     * @return Object in this configuration that previously has been associated with the given key.
     */
    public final synchronized Object put(final String key, final Object value) {
        if (value == null) { throw new NullPointerException(); }
        return _map.put(key, value);
    }
    
    /**
     * Remove any value previously associated with the given key from this configuration. The value
     * previously associated with the key int this configuration will be returned. If a mapping
     * for the key existed in the parent configuration only, the method returns <code>null</code>
     * and not the value of the parent. This allows to distingush if the mapping existed in this
     * configuration or one of its parents.
     * <br/>
     * Removing the value from this configuration does not mean that consecutive gets return
     * <code>null</code> as one of the parents may still contain a mapping for the key that
     * was hidden by the mapping in this configuration.
     * 
     * @param key Key of the property to remove from configuration.
     * @return Object in this configuration that previously has been associated with the given key.
     */
    public final synchronized Object remove(final String key) {
        return _map.remove(key);
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to any object value, it will be returned as is. If the property is not found,
     * <code>null</code> will be returned.
     *
     * @param key Key of the property to get from configuration.
     * @return Object in this property map with the specified key value.
     */
    protected synchronized Object get(final String key) {
        Object value = _map.get(key);
        if ((value == null) && (_parent != null)) {
            value = _parent.get(key);
        }
        return value;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a boolean value, it will be returned as is. For string values that are
     * equal, ignore case, to 'true' or 'false', the respective boolean value will be returned. If
     * the property is not found, <code>null</code> will be returned. For all other types and
     * string values a ConfigurationException will be thrown. This behaviour is intended for those
     * usecases that need distinguish between values that are missconfigured or not specified at
     * all.
     *
     * @param key Property key.
     * @return Boolean value in this property map with the specified key value.
     */
    public final Boolean getBoolean(final String key) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof Boolean) {
            return (Boolean) objectValue;
        } else if (objectValue instanceof String) {
            String stringValue = (String) objectValue;
            if ("true".equalsIgnoreCase(stringValue)) {
                return Boolean.TRUE;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return Boolean.FALSE;
            }
        }
        
        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value can not be converted to boolean: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a boolean value, it will be returned as is. For string values that are
     * equal, ignore case, to 'true' or 'false', the respective boolean value will be returned. In
     * all other cases the given default value will be returned.
     *
     * @param key Property key.
     * @param defaultValue Default value.
     * @return Boolean value in this property map with the specified key value.
     */
    public final boolean getBoolean(final String key, final boolean defaultValue) {
        Object objectValue = get(key);
        
        if (objectValue instanceof Boolean) {
            return ((Boolean) objectValue).booleanValue();
        } else if (objectValue instanceof String) {
            String stringValue = (String) objectValue;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        
        return defaultValue;
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a integer value, it will be returned as is. For string values that can
     * be interpreted as signed decimal integer, the respective integer value will be returned. If
     * the property is not found, <code>null</code> will be returned. For all other types and
     * string values a ConfigurationException will be thrown. This behaviour is intended for those
     * usecases that need distinguish between values that are missconfigured or not specified at
     * all.
     *
     * @param key Property key.
     * @return Integer value in this property map with the specified key value.
     */
    public final Integer getInteger(final String key) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof Integer) {
            return (Integer) objectValue;
        } else if (objectValue instanceof String) {
            try {
                return Integer.valueOf((String) objectValue);
            } catch (NumberFormatException ex) {
                Object[] args = new Object[] {key, objectValue};
                String msg = "Configuration value can not be converted to int: {0}={1}";
                throw new ConfigurationException(MessageFormat.format(msg, args), ex);
            }
        }
        
        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value can not be converted to int: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }

    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a integer value, it will be returned as is. For string values that can
     * be interpreted as signed decimal integer, the respective integer value will be returned. In
     * all other cases the given default value will be returned.
     *
     * @param key Property key.
     * @param defaultValue Default value.
     * @return Integer value in this property map with the specified key value.
     */
    public final int getInteger(final String key, final int defaultValue) {
        Object objectValue = get(key);
        
        if (objectValue instanceof Integer) {
            return ((Integer) objectValue).intValue();
        } else if (objectValue instanceof String) {
            String stringValue = (String) objectValue;
            try {
                return Integer.parseInt(stringValue);
            } catch (NumberFormatException ex) {
                return defaultValue;
            }
        }

        return defaultValue;
    }

    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a string value, it will be returned as is. If the property is not found,
     * <code>null</code> will be returned. For all other types a ConfigurationException will be
     * thrown.
     *
     * @param key Property key.
     * @return String value in this property map with the specified key value.
     */
    public final String getString(final String key) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof String) {
            return (String) objectValue;
        }
        
        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value is not a string: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a string value that is not empty, it will be returned as is. In all other
     * cases the given default value will be returned.
     *
     * @param key Property key.
     * @param defaultValue Default value.
     * @return String value in this property map with the specified key value.
     */
    public final String getString(final String key, final String defaultValue) {
        Object objectValue = get(key);
        
        if ((objectValue instanceof String) && !"".equals(objectValue)) {
            return (String) objectValue;
        }
        
        return defaultValue;
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a string array, it will be returned as is. A simple string will be
     * converted into a string array by splitting it into substrings at every occurence of ','
     * character. If the property is not found, <code>null</code> will be returned. For all other
     * types a ConfigurationException will be thrown.
     *
     * @param key Property key.
     * @return String array in this property map with the specified key value.
     */
    public final String[] getStringArray(final String key) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof String[]) {
            return (String[]) objectValue;
        } else if (objectValue instanceof String) {
            return ((String) objectValue).split(",");
        }

        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value is not a String[]: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a class, it will be returned as is. A simple string will be interpreted
     * as class name of which the class will be loaded with the given class loader. If the property
     * is not found, <code>null</code> will be returned. For all other types and if loading of the
     * class fails a ConfigurationException will be thrown.
     *
     * @param key Property key.
     * @param loader Class loader to load classes with.
     * @return Class in this property map with the specified key value.
     */
    public final Class getClass(final String key, final ClassLoader loader) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof Class) {
            return (Class) objectValue;
        } else if (objectValue instanceof String) {
            String classname = (String) objectValue;
            try {
                return loader.loadClass(classname);
            } catch (ClassNotFoundException ex) {
                Object[] args = new Object[] {key, classname};
                String msg = "Could not find class of configuration value: {0}={1}";
                throw new ConfigurationException(MessageFormat.format(msg, args), ex);
            }
        }

        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value is not a Class: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a class array, it will be returned as is. A simple string will be
     * splitted it into substrings at every occurence of ',' character. Each of these substrings
     * will interpreted as class name of which the class will be loaded with the given class
     * loader. If the property is not found, <code>null</code> will be returned. For all other
     * types and if loading of one of the classes fails a ConfigurationException will be thrown.
     *
     * @param key Property key.
     * @param loader Class loader to load classes with.
     * @return Class array in this property map with the specified key value.
     */
    public final Class[] getClassArray(final String key, final ClassLoader loader) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof Class[]) {
            return (Class[]) objectValue;
        } else if (objectValue instanceof String) {
            String[] classnames = ((String) objectValue).split(",");
            Class[] classes = new Class[classnames.length];
            for (int i = 0; i < classnames.length; i++) {
                try {
                    classes[i] = loader.loadClass(classnames[i]);
                } catch (ClassNotFoundException ex) {
                    Object[] args = new Object[] {key, new Integer(i), classnames[i]};
                    String msg = "Could not find class of configuration value: {0}[{1}]={2}";
                    throw new ConfigurationException(MessageFormat.format(msg, args), ex);
                }
            }
            return classes;
        }

        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value is not a Class[]: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to any object value, it will be returned as is. If the property is not found,
     * <code>null</code> will be returned.
     *
     * @param key Property key.
     * @return Object in this property map with the specified key value.
     */
    public final Object getObject(final String key) {
        return get(key);
    }
    
    /**
     * Searches for the property with the specified key in this property map. If the key is not
     * found in this property map, the parent property map, and its parents, recursively, are then
     * checked.
     * <br/>
     * If the key maps to a object array, it will be returned as is. A simple string will be
     * splitted it into substrings at every occurence of ',' character. Each of these substrings
     * will interpreted as class name of which the class will be loaded with the given class
     * loader and instantiated using its default constructor. If the property is not found,
     * <code>null</code> will be returned. For all other types and if loading or instantiation of
     * one of the classes fails a ConfigurationException will be thrown.
     *
     * @param key Property key.
     * @param loader Class loader to load classes with.
     * @return Class array in this property map with the specified key value.
     */
    public final Object[] getObjectArray(final String key, final ClassLoader loader) {
        Object objectValue = get(key);
        
        if (objectValue == null) {
            return null;
        } else if (objectValue instanceof Object[]) {
            return (Object[]) objectValue;
        } else if (objectValue instanceof String) {
            List objects = new ArrayList();
            String[] classnames = ((String) objectValue).split(",");
            for (int i = 0; i < classnames.length; i++) {
                String classname = classnames[i];
                try {
                    if ((classname != null) && !"".equals(classname.trim())) {
                        classname = classname.trim();
                        objects.add(loader.loadClass(classname).newInstance());
                    }
                } catch (ClassNotFoundException ex) {
                    Object[] args = new Object[] {key, new Integer(i), classname};
                    String msg = "Could not find configured class: {0}[{1}]={2}";
                    throw new ConfigurationException(MessageFormat.format(msg, args), ex);
                } catch (IllegalAccessException ex) {
                    Object[] args = new Object[] {key, new Integer(i), classname};
                    String msg = "Could not instantiate configured class: {0}[{1}]={2}";
                    throw new ConfigurationException(MessageFormat.format(msg, args), ex);
                } catch (InstantiationException ex) {
                    Object[] args = new Object[] {key, new Integer(i), classname};
                    String msg = "Could not instantiate configured class: {0}[{1}]={2}";
                    throw new ConfigurationException(MessageFormat.format(msg, args), ex);
                } catch (ExceptionInInitializerError ex) {
                    Object[] args = new Object[] {key, new Integer(i), classname};
                    String msg = "Could not instantiate configured class: {0}[{1}]={2}";
                    throw new ConfigurationException(MessageFormat.format(msg, args), ex);
                } catch (SecurityException ex) {
                    Object[] args = new Object[] {key, new Integer(i), classname};
                    String msg = "Could not instantiate configured class: {0}[{1}]={2}";
                    throw new ConfigurationException(MessageFormat.format(msg, args), ex);
                }
            }
            return objects.toArray();
        }

        Object[] args = new Object[] {key, objectValue};
        String msg = "Configuration value is not an Object[]: {0}={1}";
        throw new ConfigurationException(MessageFormat.format(msg, args));
    }
    
    //--------------------------------------------------------------------------
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 *
 * $Id$
 */
package org.exolab.castor.builder;

//--Castor imports
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Configuration;
import org.castor.util.Messages;
import org.castor.xml.JavaNaming;
import org.castor.xml.JavaNamingImpl;
import org.castor.xml.XMLConfiguration;

/**
 * The configuration for the SourceGenerator.
 * @HACK this class is a configuration but does not base on Castor
 * Configuration! Raised CASTOR-2195 to solve this issue.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class BuilderConfiguration {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(BuilderConfiguration.class);

    /** String representation of directory self pointer. */
    private static final String SELF_DIRECTORY = "./";
    
    /** Length of string representation of directory self pointer. */
    private static final int SELF_DIRECTORY_LENGTH = SELF_DIRECTORY.length();

    /** String representation of directory parent pointer. */
    private static final String PARENT_DIRECTORY = "../";

    /** Length of string representation of directory parent pointer. */
    private static final int PARENT_DIRECTORY_LENGTH = PARENT_DIRECTORY.length();

    /**
     * Names of properties used in the configuration file.
     */
    public static class Property {
        /**
         * Property specifying whether or not to generate source code for bound
         * properties. Currently all properties will be treated as bound
         * properties if this flag is set to true. A value of 'true' enables
         * bound properties.
         *
         * <pre>
         * org.exolab.castor.builder.boundproperties
         * </pre>
         */
        public static final String BOUND_PROPERTIES =
            "org.exolab.castor.builder.boundproperties";

        /**
         * Property specifying whether to implement EnumeratedTypeAccess
         * interface for all generated enumerated type classes.
         *
         * <pre>
         * org.exolab.castor.builder.enumTypeAccessInterface
         * </pre>
         */
        public static final String ENUM_TYPE_ACCESS_INTERFACE =
            "org.exolab.castor.builder.enumTypeAccessInterface";

        /**
         * Property specifying whether or not to generate source code for extra
         * collection methods.
         *
         * <pre>
         * org.exolab.castor.builder.extraCollectionMethods
         * </pre>
         */
        public static final String EXTRA_COLLECTION_METHODS =
            "org.exolab.castor.builder.extraCollectionMethods";

        /**
         * Property specifying the super class for all generated classes.
         *
         * <pre>
         * org.exolab.castor.builder.superclass
         * </pre>
         */
        public static final String SUPER_CLASS =
            "org.exolab.castor.builder.superclass";

        /**
         * Property specifying how element's and type's are mapped into a Java
         * class hierarchy by the Source Generator. The value must contain one
         * of the following. 'element' outputs a Java class hierarchy based on
         * element names used in the XML Schema. This is the default. 'type'
         * outputs a Java class hierarchy based on the type information defined
         * in the XML Schema.
         *
         * <pre>
         * org.exolab.castor.builder.javaclassmapping
         * </pre>
         */
        public static final String JAVA_CLASS_MAPPING =
            "org.exolab.castor.builder.javaclassmapping";

        /**
         * Property listing mapping between XML namespaces and Java packages.
         */
        public static final String NAMESPACE_PACKAGES_OLD =
            "org.exolab.castor.builder.nspackages";

        /**
         * Property listing mapping between XML namespaces and Java packages.
         */
        public static final String NAMESPACE_PACKAGES =
            "org.exolab.castor.xml.nspackages";

        /**
         * Property specifying if we want to have the equals method generated
         * for each generated class.
         */
        public static final String EQUALS_METHOD =
            "org.exolab.castor.builder.equalsmethod";

        /**
         * Property specifying if we want to use Wrapper Objects instead of
         * primitives (eg java.lang.Float instead of float).
         */
        public static final String WRAPPER =
            "org.exolab.castor.builder.primitivetowrapper";

        /**
         * Property specifying if we want to have a 'public static final String'
         * generated for each attribute and element name used within a class
         * descriptor.
         */
        public static final String CLASS_DESC_FIELD_NAMES =
            "org.exolab.castor.builder.classdescfieldnames";

        /**
         * Property specifying whether the Java sources generated should be 1.4
         * or 5.0 compliant.
         */
        public static final String JAVA_VERSION =
            "org.exolab.castor.builder.javaVersion";
        
        /**
         * Forces the code generator to create 'old' Java 1.4 enumeration classes instead 
         * of Java 5 enums for &lt;simpleType&gt; enumerations, even in Java 5 mode.
         */
        public static final String FORCE_JAVA4_ENUMS =
            "org.exolab.castor.builder.forceJava4Enums";

        /**
         * The name of the configuration file.
         *
         * <pre>
         * castor.properties
         * </pre>
         */
        public static final String CONFIG_FILENAME_PROPERTY =
            "castorbuilder.properties";

        /**
         * Maximum number of constant definitions within one file. This property
         * is used to allow the user to configure the maximum number of
         * constant definitions (within a Java class as generated as a result of
         * an enumeration); default is 1000. Is this number if exceeded, no constants
         * will be generated anymore.
         *
         * <pre>
         * org.exolab.castor.builder.maxNumberOfConstants
         * </pre>
         */
        public static final String MAX_CONSTANTS_PROPERTY =
            "org.exolab.castor.builder.maxNumberOfConstants";

        /**
         * Resource to load from the JAR file to load our defaults.
         */
        static final String RESOURCE_NAME =
            "/org/exolab/castor/builder/castorbuilder.properties";

        /**
         * Registered class name conflict resolution strategies.
         *
         * <pre>
         * org.exolab.castor.builder.nameConflictStrategies
         * </pre>
         */
        public static final String NAME_CONFLICT_STRATEGIES =
            "org.exolab.castor.builder.nameConflictStrategies";

        /**
         * Property specifying whether automatic class name conflict resolution
         * should be used or not; defaults to false.
         *
         * <pre>
         * org.exolab.castor.builder.automaticConflictResolution
         * </pre>
         */
        public static final String AUTOMATIC_CONFLICT_RESOLUTION = 
            "org.exolab.castor.builder.automaticConflictResolution";

        /**
         * Property specifying the 'string' used in type strategy to be inserted 
         * between the actual element name and the type name (during automatic class name 
         * conflict resolution); defaults to 'By'.
         *
         * <pre>
         * org.exolab.castor.builder.automaticConflictResolutionTypeSuffix
         * </pre>
         */
        public static final String AUTOMATIC_CONFLICT_RESOLUTION_TYPE_SUFFIX = 
            "org.exolab.castor.builder.automaticConflictResolutionTypeSuffix";
        
        /**
         * Property enlisting the supported {@link JClassPrinterFactory} instances available 
         * for creating {@link JClassPrinter} instances.
         */
        public static final String JCLASSPRINTER_FACTORIES = 
            "org.exolab.castor.builder.jclassPrinterFactories";

        /**
         * Property specifying whether extra members/methods for extracting XML schema
         * documentation should be made available; defaults to false.
         * <pre>
         * org.exolab.castor.builder.extraDocumentationMethods=false
         * </pre>
         */
        public static final String EXTRA_DOCUMENTATION_METHODS = 
            "org.exolab.castor.builder.extraDocumentationMethods";
        
    } //--Property

    /**
     * String value of false.
     */
    private static final String FALSE = "false";

    /**
     * String value of true.
     */
    private static final String TRUE = "true";

    /**
     * String value of element binding property.
     */
    private static final String ELEMENT_VALUE = "element";

    /**
     * String value of type binding property.
     */
    private static final String TYPE_VALUE = "type";

    /**
     * The default properties loaded from the configuration file.
     */
    private Properties _defaultProps = null;

    /**
     * Our properties after all configuration has been loaded.
     */
    private Properties _localProps = null;

    /**
     * Namespace URL to Java package mapping.
     */
    private Hashtable _nspackages = new Hashtable();

    /**
     * schemaLocation to Java package mapping.
     */
    private Hashtable _locpackages = new Hashtable();

    /** JavaNaming to be used. */
    private JavaNaming _javaNaming;
   
    /**
     * hooks for (external) tools to add custom
     * annotations to fields, classes and enumConstants 
     * during source generation.
     */
    private List _annotationBuilders = new ArrayList();

    //------------------/

    /**
     * Creates a default BuilderConfiguration.
     */
    public BuilderConfiguration() {
        super();
        getDefault();
        _localProps = new Properties(_defaultProps);
    } //-- BuilderConfiguration

    /**
     * Returns the default configuration file. Changes to the returned
     * properties set will affect all Castor functions relying on the default
     * configuration.
     *
     * @return The default configuration
     */
    public final synchronized Properties getDefault() {
        if (_defaultProps == null) {
            load();
        }
        return _defaultProps;
    } //-- getDefault

    /**
     * Returns a property from the default configuration file. Equivalent to
     * calling <tt>getProperty</tt> on the result of {@link #getDefault}.
     *
     * @param name
     *            The property name
     * @param defValue
     *            The property's default value
     * @return The property's value
     */
    public final String getProperty(final String name, final String defValue) {
        return _localProps.getProperty(name, defValue);
    } //-- getProperty

    /**
     * Returns true if bound properties are enabled.
     * <p>
     * Enabling bound properties is controlled via the
     * org.exolab.castor.builder.boundproperties item in the
     * castorbuilder.properties file. The value is either 'true' or 'false'.
     *
     * @return true if bound properties are enabled.
     */
    public final boolean boundPropertiesEnabled() {
        return TRUE.equalsIgnoreCase(_localProps.getProperty(Property.BOUND_PROPERTIES));
    } //-- boundPropertiesEnabled

    /**
     * Returns true if we generate an 'equals' method for each generated class.
     * <p>
     * Enabling this property is controlled via the
     * org.exolab.castor.builder.equalsmethod item in the
     * castorbuilder.properties file. The value is either 'true' or 'false'.
     *
     * @return true if bound properties are enabled.
     */
    public final boolean equalsMethod() {
        return TRUE.equalsIgnoreCase(_localProps.getProperty(Property.EQUALS_METHOD));
    } //-- equalsMethod

    /**
     * Sets the 'equalsmethod' property.
     *
     * @param equals The value we want to use.
     */
     public final void setEqualsMethod(final boolean equals) {
        String value = (equals) ? TRUE : FALSE;
        _localProps.setProperty(Property.EQUALS_METHOD, value);
     } //-- setEqualsMethod

    /**
     * Returns true if we generate a 'public static final String' for the name
     * of each attribute and element described by the class descriptor
     * <p>
     * Enabling this property is controlled via the
     * org.exolab.castor.builder.classdescfieldnames item in the
     * castorbuilder.properties file. The value is either 'true' or 'false'.
     *
     * @return true if bound properties are enabled.
     */
    public final boolean classDescFieldNames() {
        return _localProps.getProperty(Property.CLASS_DESC_FIELD_NAMES, "").equalsIgnoreCase(TRUE);
    } //-- classDescFieldNames

    /**
     * Returns true if extra methods for collection fields should be generated.
     * Such methods include set/get methods for the actual collection in
     * addition to the array methods.
     * <p>
     * Enabling extra collection methods is controlled via the
     * org.exolab.castor.builder.extraCollectionMethods property in the
     * castorbuilder.properties file. The value is either 'true' or 'false'.
     *
     * @return true if extra collection methods are enabled.
     */
    public final boolean generateExtraCollectionMethods() {
        return _localProps.getProperty(Property.EXTRA_COLLECTION_METHODS, "")
                .equalsIgnoreCase(TRUE);
    } //-- generateExtraCollectionMethods

    /**
     * Sets the 'classDescFieldNames' property.
     *
     * @param classDescFieldNames
     *            the value we want to ues
     */
     public final void setClassDescFieldNames(final boolean classDescFieldNames) {
        String value = (classDescFieldNames) ? TRUE : FALSE;
        _localProps.setProperty(Property.CLASS_DESC_FIELD_NAMES, value);
     } //-- setClassDescFieldNames

     /**
      * Returns true if primitive types have to be used as Objects (eg.
      * replacing <code>float</code> by <code>java.lang.Float</code>).
      * @return true if primitive types have to be used as Objects.
      */
    public final boolean usePrimitiveWrapper() {
        return _localProps.getProperty(Property.WRAPPER, "").equalsIgnoreCase(TRUE);
    } //-- usePrimitiveWrapper

    /**
     * Sets the 'primitivetowrapper' property.
     *
     * @param wrapper the value we want to use.
     */
    public final void setPrimitiveWrapper(final boolean wrapper) {
        String value = (wrapper) ? TRUE : FALSE;
        _localProps.setProperty(Property.WRAPPER, value);
    } //-- setPrimitiveWrapper

    /**
     * Returns true if we generate the implements EnumeratedTypeAccess interface
     * for enumerated type classes. The value is either 'true' or 'false'
     *
     * @return true if use enumerated type interface is enabled
     */
    public final boolean useEnumeratedTypeInterface() {
        return TRUE.equalsIgnoreCase(_localProps.getProperty(Property.ENUM_TYPE_ACCESS_INTERFACE));
    } //-- useEnumeratedTypeInterface

    /**
     * Returns true if we generate the implements EnumeratedTypeAccess interface
     * for enumerated type classes. The value is either 'true' or 'false'
     *
     * @return true if use enumerated type interface is enabled
     */
    public final boolean useJava50() {
        return "5.0".equalsIgnoreCase(_localProps.getProperty(Property.JAVA_VERSION, "1.4"));
    } //-- useEnumeratedTypeInterface
    
    /**
     * Indicates what kind of enumeration should be created for 
     * &lt;xs:simpleType&gt; enumerations.
     *
     * @return true if Java 5 source code should be generated.
     */
    public final boolean useJava5Enums() {
        return useJava50() && FALSE.equalsIgnoreCase(
                _localProps.getProperty(Property.FORCE_JAVA4_ENUMS, FALSE));
    }

    /**
     * Add support to set java version programmatically.
     */
    public final void forceUseJava50() {
        _localProps.setProperty(Property.JAVA_VERSION, "5.0");
    }
        
    /**
     * Returns true if extra methods for accessing XML schema documentation should
     * be generated; default to 'false'.
     *
     * @return true if if extra methods for accessing XML schema documentation should be generated
     */
    public final boolean generateExtraDocumentationMethods() {
        String extraDocumentationMethods = 
            _localProps.getProperty(Property.EXTRA_DOCUMENTATION_METHODS, "false");
        return new Boolean(extraDocumentationMethods).booleanValue();
    }
    
    /**
     * Returns the maximum number of static constant definitions that are
     * acceptable within one class file; default is 1000.
     *
     * @return the maximum number of static constant definitions acceptable within
     *    one class file
     */
    public final int getMaximumNumberOfConstants() {
        String property = _localProps.getProperty(Property.MAX_CONSTANTS_PROPERTY, "1000");
        return Integer.valueOf(property).intValue();
    }

    /**
     * Sets the 'enumTypeAccessInterface' property.
     *
     * @param flag the value we want to use
     */
    public final void setUseEnumeratedTypeInterface(final boolean flag) {
        String value = (flag) ? TRUE : FALSE;
        _localProps.setProperty(Property.ENUM_TYPE_ACCESS_INTERFACE, value);
    } //-- setUseEnumeratedTypeInterface

    /**
     * Tests the org.exolab.castor.builder.javaclassmapping property for the
     * 'element' value.
     *
     * @return True if the Source Generator is mapping schema elements to Java
     *         classes.
     */
    public boolean mappingSchemaElement2Java() {
        String value = _localProps.getProperty(Property.JAVA_CLASS_MAPPING, "");
        return ELEMENT_VALUE.equalsIgnoreCase(value);
    } //-- mappingSchemaElement2Java

    /**
     * Tests the org.exolab.castor.builder.javaclassmapping property for the 'type' value.
     *
     * @return True if the Source Generator is mapping schema types to Java classes.
     */
    public boolean mappingSchemaType2Java() {
        String value = _localProps.getProperty(Property.JAVA_CLASS_MAPPING, "");
        return TYPE_VALUE.equalsIgnoreCase(value);
    } //-- mappingSchemaType2Java

    /**
     * Overrides the current set of properties with the given properties. Once
     * the properties are set, only a copy will be uses, so any changes to the
     * given properties file after the fact will go unnoticed.
     *
     * @param properties
     *            the Properties file
     */
    public final void setDefaultProperties(final Properties properties) {
        Properties defaults = null;
        if (properties == null) {
            defaults = _defaultProps;
        } else {
            defaults = new Properties(_defaultProps);
            Enumeration enumeration = properties.keys();
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                defaults.setProperty(name, properties.getProperty(name));
            }
        }
        _localProps = new Properties(defaults);
        processNamespacePackageMappings(
                _localProps.getProperty(Property.NAMESPACE_PACKAGES_OLD, ""));
        processNamespacePackageMappings(
                _localProps.getProperty(Property.NAMESPACE_PACKAGES, ""));
    } //-- setDefaultProperties

    /**
     * Sets the namespace to package mapping.
     *
     * @param ns the namespace URI to map
     * @param packageName the package name
     */
    public final void setNamespacePackageMapping(final String ns, final String packageName) {
        _nspackages.put(ns, packageName);
    } //-- setNamespcaePackageMapping

    /**
     * Sets the schemaLocation to package mapping.
     *
     * @param schemaLocation the schemaLocation to map
     * @param packageName the package name to map to
     */
    public final void setLocationPackageMapping(
            final String schemaLocation, final String packageName) {
        _locpackages.put(schemaLocation, packageName);
    }
    
    /**
     * Returns a String representing the list of {@link JClassPrinterFactory} instances
     * configured in the Castor XML code generator property file.
     * @return the list of {@link JClassPrinterFactory} instances as configured, as string.
     */
    public final String getJClassPrinterFactories() {
        return _localProps.getProperty(Property.JCLASSPRINTER_FACTORIES);
    }


    /**
     * Called by {@link #getDefault} to load the configuration the first time.
     * Will not complain about inability to load configuration file from one of
     * the default directories, but if it cannot find the JAR's configuration
     * file, will throw a run time exception.
     */
    protected final synchronized void load() {
        if (_defaultProps == null) {
            //-- load defaults from JAR
            _defaultProps = loadProperties(
                    Property.RESOURCE_NAME, Property.CONFIG_FILENAME_PROPERTY);

            //-- load local defaults

            boolean found = false;

            // Get overriding configuration from the classpath, ignore if not found.
            // If found, merge any existing properties.
            try {
                InputStream is = SourceGenerator.class.getResourceAsStream(
                        "/" + Property.CONFIG_FILENAME_PROPERTY);
                if (is != null) {
                    found = true;
                    _defaultProps.load(is);
                    is.close();
                }
            } catch (Exception except) {
                //-- do nothing
            }

            //-- if not found, either it doesn't exist, or "." is not part of the
            //-- class path, try looking at local working directory
            if (!found) {
                try {
                    File file = new File(Property.CONFIG_FILENAME_PROPERTY);
                    if (file.exists() && file.canRead()) {
                        InputStream is = new FileInputStream(file);
                        _defaultProps.load(is);
                        is.close();
                    }
                } catch (Exception except) {
                    //-- do nothing
                }
            }
        }

        Configuration rtconf =  XMLConfiguration.newInstance();

        // Parse XML namespace and package list from both castor.properties and
        // castorbuilder.properties
        processNamespacePackageMappings(rtconf.getString(Property.NAMESPACE_PACKAGES_OLD, ""));
        processNamespacePackageMappings(rtconf.getString(Property.NAMESPACE_PACKAGES, ""));
        processNamespacePackageMappings(_defaultProps.getProperty(
                Property.NAMESPACE_PACKAGES_OLD, ""));
        processNamespacePackageMappings(_defaultProps.getProperty(
                Property.NAMESPACE_PACKAGES, ""));

        //-- backward compatibility with 0.9.3.9
        String prop = _defaultProps.getProperty(
                JavaNamingImpl.UPPER_CASE_AFTER_UNDERSCORE_PROPERTY, null);
        if (prop != null) {
            /*
             * TODO: joachim: Argh! this shouldn't exist and it shouldn't be here! Setting a
             * static attribute into a class because somewhere later the class is instantiated
             * and use but then the attribute is required... I need to sort out the order how
             * objects are created...
             */
            JavaNamingImpl._upperCaseAfterUnderscore = Boolean.valueOf(prop).booleanValue();
        }
    } //-- load

    /**
     * Gets a Java package to an XML namespace URL.
     * @param nsURL the XML namespace URL to convert into a Java package name
     * @return a Java package name
     */
    public final String lookupPackageByNamespace(final String nsURL) {
        String namespaceURL = (nsURL == null) ? "" : nsURL;

        // Lookup Java package via NS
        String javaPackage = (String) _nspackages.get(namespaceURL);
        if (javaPackage == null) {
            return "";
        }
        return javaPackage;
    } //-- lookupPackageNamespace

    /**
     * Converts a schema location into a Java package.
     *
     * @param schemaLocation
     *            the Schema location to use to look up the Java package
     * @return a Java package name
     */
    public final String lookupPackageByLocation(final String schemaLocation) {
        if (schemaLocation == null) {
            return "";
        }

        // Lookup Java package via schemaLocation
        //--Full path
        String javaPackage = (String) _locpackages.get(schemaLocation);
        if (javaPackage == null) {
            String cleanedSchemaLocation = schemaLocation;
            //--maybe a relative schemaLocation was given
            while (schemaLocation.startsWith(".")) {
                 if (schemaLocation.startsWith(SELF_DIRECTORY)) {
                     cleanedSchemaLocation = schemaLocation.substring(SELF_DIRECTORY_LENGTH);
                 } else if (schemaLocation.startsWith(PARENT_DIRECTORY)) {
                     cleanedSchemaLocation = schemaLocation.substring(PARENT_DIRECTORY_LENGTH);
                 }
            }
            Enumeration keys = _locpackages.keys();
            boolean found = false;
            while (keys.hasMoreElements() && !found) {
                String key = (String) keys.nextElement();
                if (cleanedSchemaLocation.endsWith(key)) {
                    javaPackage = (String) _locpackages.get(key);
                    found = true;
                }
            }
            if (javaPackage == null) {
                javaPackage = "";
            }
        }

        return javaPackage;
    } //-- lookupPackageLocation

    /**
     * processes the given String which contains namespace-to-package mappings.
     *
     * @param mappings the namespace-to-package mappings
     */
    protected final void processNamespacePackageMappings(final String mappings) {
        if (mappings == null) {
            return;
        }

        StringTokenizer tokens = new StringTokenizer(mappings, ",");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int sepIdx = token.indexOf('=');
            if (sepIdx < 0) {
                continue;
            }

            String ns = token.substring(0, sepIdx).trim();
            String javaPackage = token.substring(sepIdx + 1).trim();
            _nspackages.put(ns, javaPackage);
        }
    } //-- processNamespacePackageMappings

    /**
     * indicates whether automatic class name conflict resolution during 
     * XML code generation should take place or not.
     * @return True if automatic mode should be used.
     */
    public boolean isAutomaticConflictResolution() {
        String automaticConflictResolutionProperty = 
            _localProps.getProperty(Property.AUTOMATIC_CONFLICT_RESOLUTION, "false");
        return "true".equalsIgnoreCase(automaticConflictResolutionProperty);
    }

    /**
     * Returns the type 'suffix' used for the type strategy during automatic class name 
     * conflict resolution during XML code generation; default to "" unless a value is specified.
     * @return The type suffix to be inserted between element name and type name
     */
    public String getAutomaticConflictResolutionTypeSuffix() {
        return _localProps.getProperty(Property.AUTOMATIC_CONFLICT_RESOLUTION_TYPE_SUFFIX, "");
    }

    /**
     * To set the {@link JavaNaming} implementation to be used.
     * @param javaNaming JavaNaming implementation to be used
     * @since 1.1.3
     */
    public void setJavaNaming(final JavaNaming javaNaming) {
        _javaNaming = javaNaming;
    }
    
    /**
     * To get the {@link JavaNaming} implementation to be used.
     * @return JavaNaming implementation to be used
     * @since 1.1.3
     */
    public JavaNaming getJavaNaming() {
        return _javaNaming;
    }
    
    /**
     * adds a custom annotation builder.
     * @param annotationBuilder the builder
     */
    public void addAnnotationBuilder(final AnnotationBuilder annotationBuilder) {
        this._annotationBuilders.add(annotationBuilder);
    }
    
    /**
     * returns all applied annotation builders.
     * @return a array of builders for type convenience
     */
    public AnnotationBuilder[] getAnnotationBuilders() {
        return (AnnotationBuilder[])
            this._annotationBuilders.toArray(
                    new AnnotationBuilder[this._annotationBuilders.size()]);
    }

    /**
     * Load the configuration will not complain about inability to load
     * configuration file from one of the default directories, but if
     * it cannot find the JAR's configuration file, will throw a
     * run time exception.
     */
    public static Properties loadProperties(String resourceName, String fileName)
    {
        File        file;
        Properties properties = new Properties();

        boolean found = false;
        
        // Get detault configuration from the Castor JAR.
        // Complain if not found.
        InputStream resourceStream = null;
        try {
            resourceStream = Configuration.class.getResourceAsStream(resourceName); 
            properties.load(resourceStream);
            
            //-- debug information:
            //URL url = Configuration.class.getResource( resourceName );
            //System.out.println("loading configuration: " + url.toExternalForm());
            //-- end debug information
            
            found = true;
        } 
        catch (Exception except) {
            // Do nothing as we will check classpath 
            // and java lib directory below
        } finally {
            if (resourceStream != null) {
                try {
                    resourceStream.close();
                } catch (IOException e) {
                    LOG.warn("Problem closing stream for " + resourceName);
                }
            }
        }

        // Get overriding configuration from the Java
        // library directory, ignore if not found. If
        // found merge existing properties.
        String javaHome = null;
        try {
            javaHome = System.getProperty("java.home");
        } catch (SecurityException e) {
            // Not a critical error, but users will need to know if they need to change their config.   
            LOG.warn(Messages.format("conf.privilegesError", e));
        } catch (Exception e) {
            // As we will be trying something else later, record the error for setup purposes,
            // but do not actually treat as a critical failure.
            LOG.warn(Messages.format("conf.nonCriticalError", e));
        }
        
        if (javaHome != null) {
            InputStream fileStream = null;
            try {      
                file = new File( javaHome, "lib" );
                file = new File( file, fileName );
                if ( file.exists() ) {
                    properties = new Properties(properties);
                    fileStream = new FileInputStream(file);
                    properties.load(fileStream);
                    found = true;
                }      
            } catch (SecurityException e) {
                // Not a critical error, but users will need to know if they need to change their config.   
                LOG.warn(Messages.format("conf.privilegesError", e));
            } catch (IOException e) {
                // Report that we were unable to load the resource.
                LOG.warn(Messages.format("conf.nonCriticalError", e));
            } finally {
                if (fileStream != null) {
                    try {
                        fileStream.close();
                    } catch (IOException e) {
                        LOG.warn("Problem closing stream for " + fileName);
                    }
                }
            }
        }
        

        //-- Cannot find any castor.properties file(s).
        if (!found) {
            throw new RuntimeException( Messages.format( "conf.noDefaultConfigurationFile",
                                                            fileName ) );
        }

        return properties;
    }
} //-- BuilderProperties

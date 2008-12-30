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
     * Factory method for a default XML configuration instance. Application and domain class
     * loaders will be initialized to the one used to load the Configuration class. The
     * configuration instance returned will be a CastorConfiguration with a XMLConfiguration and
     * a CoreConfiguration instance as parents. The CastorConfiguration holding user specific
     * properties is the only one that can be modified by put() and remove() methods.
     * XMLConfiguration and CoreConfiguration are responsible to deliver Castor's default values
     * if they have not been overwritten by the user.
     * 
     * @return Configuration instance for Castor XML module.
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
    
    /**
     * Property specifying the type of XML node to use for
     * primitive values, either 'element' or 'attribute'.
     * 
     * Possible values:
     * - 'element'
     * - 'attribute' (default)
     * 
     * <pre>
     * org.exolab.castor.xml.introspector.primitive.nodetype
     * </pre>
     */
    public static final String PRIMITIVE_NODE_TYPE 
        = "org.exolab.castor.xml.introspector.primitive.nodetype";

    /**
     * Property specifying the class name of the SAX 1 XML parser to 
     * use.
     * 
     * <pre>
     * org.exolab.castor.parser
     * </pre>
     */
    public static final String PARSER = "org.exolab.castor.parser";
    
    // TODO: expand comment to make things clearer; check against code
    /**
     * Property specifying whether to perform document validation 
     * by default.
     * 
     * Possible values:
     * - false (default)
     * - true
     * 
     * <pre>
     * org.exolab.castor.SAXParser.validation
     * </pre>
     */
    public static final String PARSER_VALIDATION = 
        "org.exolab.castor.parser.validation";
    
    /**
     * Property specifying whether to support XML namespaces by default.
     * 
     * Possible values:
     * - false (default)
     * - true
     * 
     * <pre>
     * org.exolab.castor.SAXParser.namespaces
     * </pre>
     */
    public static final String NAMESPACES =
        "org.exolab.castor.parser.namespaces";
    
    /**
     * Property specifying XML namespace to Java package mappings.
     * 
     * <pre>
     * org.exolab.castor.xml.nspackages
     * </pre>
     */
    public static final String NAMESPACE_PACKAGE_MAPPINGS = 
        "org.exolab.castor.xml.nspackages";

    /**
     * Property specifying the 'type' of the XML naming conventions
     * to use. Values of this property must be either "mixed", "lower", or
     * the name of a class which extends {@link org.exolab.castor.xml.AbstractXMLNaming}.
     * 
     * Possible values:
     * - 'mixed' 
     * - 'lower'
     * - A class name (which extends {@link org.exolab.castor.xml.AbstractXMLNaming}).
     * 
     * <pre>
     * org.exolab.castor.xml.naming
     * </pre>
     *
     */
    public static final String XML_NAMING = "org.exolab.castor.xml.naming";
    
    /**
     * Property specifying the 'type' of the Java naming conventions
     * to use. Values of this property must be either null or
     * the name of a class which extends {@link org.castor.xml.JavaNaming}.
     * 
     * Possible values:
     * - null
     * - A class name (which extends {@link org.castor.xml.JavaNaming}).
     * 
     * <pre>
     * org.castor.xml.java_naming
     * </pre>
     *
     */
    public static final String JAVA_NAMING = "org.castor.xml.java.naming";        
    
    /**
     * Property specifying whether to use validation in the Marshalling 
     * framework.
     * 
     *
     * Possible values:
     * - false
     * - true (default)
     * 
     * <pre>
     * org.exolab.castor.marshalling.validation
     * </pre>
     */
     public static final String MARSHALLING_VALIDATION = 
         "org.exolab.castor.marshalling.validation";
    
     /**
      * Property specifying whether XML documents (as generated at marshalling)
      * should use indentation or not.
      * 
      * Possible values:
      * - false (default)
      * - true
      * 
      * <pre>
      * org.exolab.castor.indent
      * </pre>
      */
     public static final String USE_INDENTATION = "org.exolab.castor.indent";

     /**
      * Property specifying additional features for the XML parser.
      * This value contains a comma separated list of features that
      * might or might not be supported by the specified SAX parser.
      * 
      * <pre>
      * org.exolab.castor.sax.features
      * </pre>
      */
     public static final String PARSER_FEATURES = "org.exolab.castor.sax.features";
     
     /**
      * Property specifying features to be disbaled on the underlying SAX parser.
      * This value contains a comma separated list of features to be disabled.
      * 
      * <pre>
      * org.exolab.castor.sax.features-to-disable
      * </pre>
      */
     public static final String PARSER_FEATURES_DISABLED = 
         "org.exolab.castor.sax.features-to-disable";
     
     /**
      * Property specifying the regular expression validator
      * to use. The specified class must implement
      * {@link org.exolab.castor.xml.validators.RegExpValidator}
      * 
      * Possible values:
      * - A class name.
      * 
      * <pre>
      * org.exolab.castor.regexp
      * </pre>
      */
     public static final String REG_EXP_CLASS_NAME = 
         "org.exolab.castor.regexp";

     /**
      * Property specifying whether to run in debug mode.
      * 
      * Possible values:
      * - false (default)
      * - true
      * 
      * <pre>
      * org.exolab.castor.debug
      * </pre>
      */
     public static final String DEBUG = "org.exolab.castor.debug";
     
     /**
      * Property specifying whether to apply strictness to elements when
      * unmarshalling. Default is true which means that elements appearing in the
      * XML document, which cannot be mapped to a class, cause a {@link SAXException}
      * to be thrown. If set to false, these 'unknown' elements are ignored.
      * 
      * Possible values:
      * - false
      * - true (default)
      * 
      * <pre>
      * org.exolab.castor.strictelements
      * </pre>
      */
     public static final String STRICT_ELEMENTS = "org.exolab.castor.xml.strictelements";

     /**
      * Property specifying whether or not to save the "keys" of a {@link Hashtable} or 
      * {@link Map} during marshalling. By default this is true.
      * 
      * Backwards compatibility switch (for 0.9.5.2 users and earlier)
      * 
      * Possible values:
      * - false
      * - true (default) 
      * 
      * <pre>
      * org.exolab.castor.xml.saveMapKeys
      * </pre>
      * 
      * @since 0.9.5.3
      */
     public static final String SAVE_MAP_KEYS = "org.exolab.castor.xml.saveMapKeys";

     /**
      * Property specifying whether the ClassDescriptorResolver should (automatically) search
      * for and consult with package mapping files (.castor.xml) to retrieve class
      * descriptor information; on by default.
      * 
      * Possible values:
      * - false 
      * - true (default)
      *
      * <pre>
      * org.exolab.castor.xml.loadPackageMappings
      * </pre>
      * @since 1.0
      */
     public static final String LOAD_PACKAGE_MAPPING = "org.exolab.castor.xml.loadPackageMappings";
     
     /**
      * Property specifying what factory to use for dealing with XML serializers.
      * 
      * Possible value:
      * - A class name
      * 
      * <pre>
      * org.exolab.castor.xml.serializer.factory
      * </pre>
      * @since 1.0
      */
     public static final String SERIALIZER_FACTORY = 
         "org.exolab.castor.xml.serializer.factory";

     /**
      * Property specifying whether sequence order validation should be lenient.
      * 
      * Possible values
      * - false (default)
      * - true
      * 
      * <pre>
      * org.exolab.castor.xml.lenient.sequence.order=false
      * </pre>
      * since 1.1
      */
     public static final String LENIENT_SEQUENCE_ORDER = 
         "org.exolab.castor.xml.lenient.sequence.order";

     /**
      * Property specifying whether id/href validation should be lenient;
      * defaults to false.
      * 
      * Possible values:
      * - false (default)
      * - true
      * 
      * <pre>
      * org.exolab.castor.xml.lenient.id.validation=false
      * </pre>
      * since 1.1
      */
     public static final String LENIENT_ID_VALIDATION = 
         "org.exolab.castor.xml.lenient.id.validation";

     /**
      * Property specifying whether or not to search for an proxy interface at marshalling.
      * If property is not empty the objects to be marshalled will be searched if they
      * implement one of the given interface names. If the interface is implemented the
      * superclass will be marshalled instead of the class itself.
      * 
      * <pre>
      * org.exolab.castor.xml.proxyInterfaces
      * </pre>
      * @since 1.1.3
      */
     public static final String PROXY_INTERFACES = 
         "org.exolab.castor.xml.proxyInterfaces";
     
     
     /**
      * Property specifying whether element strictness for introspected classes/elements
      * should be lenient (aka allowed); defaults to true.
      * 
      * Possible values:
      * - false
      * - true (default)
      * 
      * <pre>
      * org.exolab.castor.xml.lenient.introspected.element.strictness=true
      * </pre>
      * 
      * @since 1.1.3
      */
     public static final String LENIENT_INTROSPECTED_ELEMENT_STRICTNESS = 
         "org.exolab.castor.xml.lenient.introspected.element.strictness";

     /**
      * Property specifying which collections handlers should be used for
      * Java 1.1 and Java 1.2 run-times.
      * 
      * <pre>
      * org.exolab.castor.mapping.collections
      * </pre>
      */
    public static final String COLLECTION_HANDLERS_FOR_JAVA_11_OR_12 = 
        "org.exolab.castor.mapping.collections";

    /**
     * Property specifying if introspection should be used at class resolving.
     * 
     * <pre>
     * org.castor.xml.class-resolver.use-introspection
     * </pre>
     */
    public static final String USE_INTROSPECTION 
        = "org.castor.xml.class-resolver.use-introspection";

    /**
     * The property name for enabling collection wrapping.
     * The property controls whether or not collections
     * (arrays, vectors, etc) should be wrapped in a container element.
     * For example:
     *
     * <pre>
     *    &lt;foos&gt;
     *       &lt;foo&gt;foo1&lt;/foo&gt;
     *       &lt;foo&gt;foo2&lt;/foo&gt;
     *    &lt;/foos&gt;
     *
     *   instead of the default:
     *
     *    &lt;foos&gt;foo1&lt;foos&gt;
     *    &lt;foos&gt;foo2&lt;/foos&gt;
     *
     * </pre>
     *
     * Use this property with a value of true or false in the
     * castor.properties file
     *
     * org.exolab.castor.xml.introspector.wrapCollections=true
     * -or-
     * org.exolab.castor.xml.introspector.wrapCollections=false
     *
     * This property is false by default.
     */
    public static final String WRAP_COLLECTIONS_PROPERTY =
        "org.exolab.castor.xml.introspector.wrapCollections";

    /**
     * Property that allows to specify whether the validation for 
     * &lt;xs:integer&gt; should accept the old 'int/Integer' members as well;
     * default to false.
     * 
     * Possible values:
     * - false  (default)
     * - true
     * 
     * <pre>
     * org.exolab.castor.xml.lenient.integer.validation=false
     * </pre>
     */
    public static final String LENIENT_INTEGER_VALIDATION = 
        "org.exolab.castor.xml.lenient.integer.validation";    
}

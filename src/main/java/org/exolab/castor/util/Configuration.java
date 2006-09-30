/**
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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;

import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.OutputFormat;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.XMLNaming;
import org.exolab.castor.xml.XMLSerializerFactory;
import org.exolab.castor.xml.util.DefaultNaming;

import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * Provides default configuration for Castor components from the
 * <tt>castor.properties</tt> configuration file. All Castor features
 * rely on the central configuration file.
 * <p>
 * The configuration file is loaded from the Java <tt>lib</tt>
 * directory, the classpath and the Castor JAR. Properties set in the
 * classpath file takes precedence over properties set in the Java library
 * configuration file and properties set in the Castor JAR, allowing for
 * each customization. All three files are named <tt>castor.properties</tt>.
 * <p>
 * For example, to change the parser in use, specify that all
 * documents should be printed with identantion or turn debugging on,
 * create a new configuration file in the current directory, instead
 * of modifying the global one.
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public abstract class Configuration {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(Configuration.class);

    /**
     * Names of properties used in the configuration file.
     */
    public static class Property
    {

        /**
         * Property specifying the type of node to use for
         * primitive values, either "element" or "attribute"
         * <pre>
         * org.exolab.castor.xml.introspector.primitive.nodetype
         * </pre>
         */
        public static final String PrimitiveNodeType 
            = "org.exolab.castor.xml.introspector.primitive.nodetype";


        /**
         * Property specifying the class name of the XML parser to use.
         * <pre>
         * org.exolab.castor.parser
         * </pre>
         */
        public static final String Parser = "org.exolab.castor.parser";

        /**
         * Property specifying whether to perform document validation by default.
         * <pre>
         * org.exolab.castor.SAXParser.validation
         * </pre>
         */
        public static final String ParserValidation = "org.exolab.castor.parser.validation";

        /**
         * Property specifying whether to support Namespaces by default.
         * <pre>
         * org.exolab.castor.SAXParser.namespaces
         * </pre>
         */
        public static final String Namespaces = "org.exolab.castor.parser.namespaces";
        
        
        /**
         * Property specifying Namespace to Package Mappings
         * <pre>
         * org.exolab.castor.xml.nspackages
         * </pre>
         */
        public static final String NamespacePackages = "org.exolab.castor.xml.nspackages";
        

        /**
         * Property specifying the implementation of the naming conventions
         * to use. Values of this property must be either "mixed", "lower", or
         * the name of a class which extends org.exolab.castor.xml.XMLNaming.
         * <pre>
         * org.exolab.castor.xml.naming
         * </pre>
         *
         */
        public static final String Naming = "org.exolab.castor.xml.naming";        

        /**
         * Property specifying whether to use validation in the Marshalling Framework
         * <pre>
         * org.exolab.castor.marshalling.validation
         * </pre>
         */
         public static final String MarshallingValidation = "org.exolab.castor.marshalling.validation";
         
        /**
         * Property specifying whether XML documents should be indented by default.
         * <pre>
         * org.exolab.castor.indent
         * </pre>
         */
        public static final String Indent = "org.exolab.castor.indent";

        /**
         * Property specifying additional features for the parser.
         * This value contains a comma separated list of features that
         * might or might not be supported by the specified parser.
         * <pre>
         * org.exolab.castor.sax.features
         * </pre>
         */
        public static final String ParserFeatures = "org.exolab.castor.sax.features";

        /**
         * Property specifying features to be disbaled on the underlying SAX parser.
         * This value contains a comma separated list of features to be disabled.
         * <pre>
         * org.exolab.castor.sax.features-to-disable
         * </pre>
         */
        public static final String ParserFeaturesToDisable = "org.exolab.castor.sax.features-to-disable";

        public static final String ParserFeatureSeparator = ",";

        /**
         * Property specifying the regular expression validator
         * to use. This specified class must implement
         * org.exolab.castor.xml.validators.RegExpValidator
         * <pre>
         * org.exolab.castor.regexp
         * </pre>
         */
        public static final String RegExp = "org.exolab.castor.regexp";

        /**
         * Property specifying whether to run in debug mode.
         * <pre>
         * org.exolab.castor.debug
         * </pre>
         */
        public static final String Debug = "org.exolab.castor.debug";

        /**
         * Property specifying whether to apply strictness to elements when
         * unmarshalling. Default is true which means that elements appearing in the
         * XML Documnt which cannot be mapped to a class cause a SAXException to be thrown.
         * If set to false, these 'unknown' elements are ignored
         * <pre>
         * org.exolab.castor.strictelements
         * </pre>
         */
        public static final String StrictElements = "org.exolab.castor.xml.strictelements";


        /**
         * Property specifying whether or not to save the "keys" of a Hashtable or 
         * Map during marshalling. By default this is true.
         * <pre>
         * org.exolab.castor.xml.saveMapKeys
         * </pre>
         * @since 0.9.5.3
         */
        public static final String SaveMapKeys = "org.exolab.castor.xml.saveMapKeys";

        /**
         * Property specifying what factory to use for dealing with XML serializers 
         * <pre>
         * org.exolab.castor.xml.serializer.factory
         * </pre>
         * @since 1.0
         */
		public static final String LOAD_PACKAGE_MAPPING = "org.exolab.castor.xml.loadPackageMappings";
        
        public static final String DEFAULT_LOAD_PACKAGE_MAPPING = "true";

        /**
         * Property specifying what factoyr to use for dealing with XML serializers 
         * <pre>
         * org.exolab.castor.xml.serializer.factory
         * </pre>
         * @since 1.0
         */
        public static final String SERIALIZER_FACTORY = "org.exolab.castor.xml.serializer.factory";

        /**
         * The name of the configuration file.
         * <pre>
         * castor.properties
         * </pre>
         */
        public static final String FileName = "castor.properties";

        static final String ResourceName = "/org/exolab/castor/castor.properties";
        
        static final String DEFAULT_SERIALIZER_FACTORY = "org.exolab.castor.xml.XercesXMLSerializerFactory";

        /**
         * Property specifying whether to use ANSI-compliant SQL for MS SQL Server.
         * <pre>
         * org.exolab.castor.jdo.sqlserver.ansi-compliant
         * </pre>
         */
        public static final String SqlServerAnsiCompliant = "org.exolab.castor.jdo.sqlserver.ansi-compliant";
    } //-- class: Property


    static class Features
    {
        public static final String Validation = "http://xml.org/sax/features/validation";
        public static final String Namespaces = "http://xml.org/sax/features/namespaces";
    } //-- class: Features


    // Some static string definitions
    static final String TRUE_VALUE  = "true";
    static final String ON_VALUE    = "on";

	/**
     * The default properties loaded from the configuration file.
     */
    private static Properties   _defaultProps;
    
    /**
     * The default configuration values (retrieved from default properties)
     */
    private static ConfigValues _defaultValues = new ConfigValues();


    /** 
     * Protected default constructor
     */
    Configuration() {
        super();
    } //-- Configuration
    
    /**
     * Returns true if the configuration specifies debugging. 
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return true if the configuration specifies debugging.
     * @see #getDefaultDebug()
     */
    public boolean debug() {
        return getDefaultDebug();
    } //-- debug
    
    /**
     * Returns true if the default configuration specified debugging.
     *
     * @return true if the configuration specifies debugging.
     */
    public static boolean getDefaultDebug()
    {
        getDefault();
        return _defaultValues.debug;
    } //-- getDefaultDebug
    

    /**
     * Access to the property specifying whether to apply strictness to elements when
     * unmarshalling. Default is true which means that elements appearing in the
     * XML Documnt which cannot be mapped to a class cause a SAXException to be thrown.
     * If set to false, these 'unknown' elements are ignored.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return true if element processing should be "strict".
     * @see #getDefaultStrictElements()
     */
    public boolean strictElements() {
        return getDefaultStrictElements();
    } //-- strictElements
    
    /**
     * Access to the property specifying whether to apply strictness to elements when
     * unmarshalling. Default is true which means that elements appearing in the
     * XML Documnt which cannot be mapped to a class cause a SAXException to be thrown.
     * If set to false, these 'unknown' elements are ignored
     *
     * @return true if element processing should be "strict".
     * @see #strictElements()
     */
    public static boolean getDefaultStrictElements() {
        getDefault();
        return _defaultValues.strictElements;
    } //-- getDefaultStrictElements

    /**
     * Returns true if the current configuration (user-supplied or default)
     * specifies validation in the marshalling framework.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return true if by default validation should be performed during the
     * marshalling and unmarshalling process, otherwise false.
     * @see #getDefaultMarshallingValidation()
     */
    public boolean marshallingValidation() {
        return getDefaultMarshallingValidation();
    } //-- marshallingValidation
    
    
    /**
     * Returns true if the default configuration specifies validation in
     * the marshalling framework.
     *
     * @return true if by default validation should be performed during the
     * marshalling and unmarshalling process, otherwise false.
     *
     * @see #marshallingValidation()
     */
    public static boolean getDefaultMarshallingValidation()
    {
        getDefault();
        return _defaultValues.marshallingValidation;
    } //-- getDefaultMarshallingValidation

    
    /**
     * Returns the current properties from the configuration file(s). 
     * The Properties returned may be empty, but never null.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return the current set of configuration properties. 
     * @see #getDefault()
     */
    public Properties getProperties() {
        return getDefault();
    } //-- getProperties
    
    /**
     * Returns the default configuration file. Changes to the returned
     * properties set may affect all Castor functions relying on the
     * default configuration.
     *
     * @return the default configuration properties
     * @see #getProperties()
     */
    public static synchronized Properties getDefault()
    {
        if ( _defaultProps == null ) {
            loadDefaults();
        }
        return _defaultProps;
    } //-- getDefault

    /**
     * Returns a property from the current configuration.
     * Equivalent to calling <tt>getProperty</tt> on the result
     * of {@link #getDefault}.
     *
     * @param name The property name
     * @param defValue The property's default value
     * @return The property's value
     * @see #getDefaultProperty(String,String)
     * @see #getProperties()
     */
    public String getProperty( String name, String defValue )
    {
        return getProperties().getProperty( name, defValue );
        
    } //-- getProperty


    /**
     * Returns a property from the default configuration file.
     * Equivalent to calling <tt>getProperty</tt> on the result
     * of {@link #getDefault}.
     *
     * @param name The property name
     * @param defValue The property's default value
     * @return The property's value
     * @see #getProperty (String, String)
     */
    public static String getDefaultProperty( String name, String defValue )
    {
        return getDefault().getProperty( name, defValue );
    } //-- getDefaultProperty

    /**
     * Returns the currently configured naming conventions to use 
     * for the XML framework
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return the currently configured naming conventions to use 
     * for the XML framework     
     * @see #getDefaultXMLNaming()
     */
    public XMLNaming getXMLNaming() {
        return getDefaultXMLNaming();
    } //-- getXMLNaming


    /**
     * Returns the default naming conventions to use for the XML framework
     *
     * @return the default naming conventions to use for the XML framework     
     */
    public static XMLNaming getDefaultXMLNaming() {
        
        if (_defaultValues.naming != null) return _defaultValues.naming;
        
        String prop = getDefaultProperty( Property.Naming, null);
        if ((prop == null) || (prop.equalsIgnoreCase("lower"))) {
            _defaultValues.naming = new DefaultNaming();
        }
        else if (prop.equalsIgnoreCase("mixed")) {
            DefaultNaming dn = new DefaultNaming();
            dn.setStyle(DefaultNaming.MIXED_CASE_STYLE);
            _defaultValues.naming = dn;
        }
        else {
            try {
                Class cls = Class.forName(prop);
                _defaultValues.naming = (XMLNaming) cls.newInstance();
            }
            catch (Exception except) {
                throw new RuntimeException("Failed to load XMLNaming: " + 
                    except);
            }
        }
        return _defaultValues.naming;
    } //-- getNaming

    /**
     * Return an XML parser implementing the feature list specified 
     * in the configuration file.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return a suitable XML parser
     * @see #getDefaultParser()
     */
    public Parser getParser() {
        return getDefaultParser();
    } //-- getParser
    
    /**
     * Return an XML document parser implementing the feature list
     * specified in the default configuration file.
     *
     * @return a suitable XML parser
     * @see #getParser()
     */
    public static Parser getDefaultParser()
    {
        return getDefaultParser( null );
    } //-- getDefaultParser


    /**
     * Returns an XML document parser implementing the requested
     * set of features. The feature list is a comma separated list
     * of features that parser may or may not support. No errors are
     * generated for unsupported features. If the feature list is not
     * null, it overrides the default feature list specified in the
     * configuration file, including validation and Namespaces.
     *
     * @param features The requested feature list, null for the
     *   defaults
     * @return A suitable XML parser
     */
    public static Parser getDefaultParser( String features )
    {
        String prop;
        Parser parser;
        
        //-- validation?
        prop = getDefault().getProperty( Property.ParserValidation, "false" );
        boolean validation = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
                               
        //-- namespaces?
        prop = getDefault().getProperty( Property.Namespaces, "false" );
        boolean namespaces = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
        

        //-- which parser?
        prop = getDefault().getProperty( Property.Parser );
        if (( prop == null ) || (prop.length() == 0))  {
            // If no parser class was specified, check for JAXP
            // otherwise we default to Xerces.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(namespaces);
            factory.setValidating(validation);
            try {
                SAXParser saxParser = factory.newSAXParser();
                return saxParser.getParser();
            }
            catch(ParserConfigurationException pcx) {
                LOG.error(Messages.format("conf.configurationError", pcx));
            }
            catch(org.xml.sax.SAXException sx) {
                LOG.error(Messages.format("conf.configurationError", sx));
            }
            
        }
        
        
        if ((prop == null) ||
            (prop.length() == 0) ||
            (prop.equalsIgnoreCase("xerces"))) 
        {
            prop = "org.apache.xerces.parsers.SAXParser";
        }
        

        // If a parser class was specified, we try to create it and
        // complain about creation error.
        try {
            Class cls;
            
            cls = Class.forName( prop );
            parser = (Parser) cls.newInstance();
        } catch ( Exception except ) {
            throw new RuntimeException( Messages.format( "conf.failedInstantiateParser",
                                                         prop, except ) );
        }

        if (parser instanceof XMLReader) {
            XMLReader xmlReader = (XMLReader) parser;
            setFeaturesOnXmlReader(features, validation, namespaces, xmlReader);
        }
        
        return parser;
    }

    /**
     * Sets features on XML reader instance.
     * @param features
     * @param validation Whether to enable validation or not.
     * @param namespaces Whether to enable namespace support for not.
     * @param xmlReader The XMLReader instance to configure.
     */
    protected static void setFeaturesOnXmlReader(String features, 
            final boolean validation, 
            final boolean namespaces, 
            final XMLReader xmlReader) {
        StringTokenizer token;
        try {
            xmlReader.setFeature(Features.Validation, validation);
            xmlReader.setFeature(Features.Namespaces, namespaces);
            features = getDefault().getProperty(Property.ParserFeatures, features);
            enableFeatures(features, xmlReader);
            String featuresToDisable = getDefault().getProperty(Property.ParserFeaturesToDisable, "");
            disableFeatures(featuresToDisable, xmlReader);
        } catch (SAXException except) {
            LOG.error(Messages.format("conf.configurationError", except));
        }
    }

    /**
     * Enables selected features on the XMLReader instance
     * @param features Features to enable
     * @param xmlReader XMLReader instance to be configured.
     * @throws SAXNotRecognizedException If the feature is not recognized by the XMLReader.
     * @throws SAXNotSupportedException If the feature is not supported by the XMLReader.
     */
    private static void enableFeatures(final String features, final XMLReader xmlReader) 
        throws SAXNotRecognizedException, SAXNotSupportedException {
        StringTokenizer token;
        if (features != null) {
            token = new StringTokenizer(features, ", ");
            while (token.hasMoreTokens()) {
                xmlReader.setFeature(token.nextToken(), true);
            }
        }
    }

    /**
     * Disables selected features on the XMLReader instance
     * @param features Features to disable
     * @param xmlReader XMLReader instance to be configured.
     * @throws SAXNotRecognizedException If the feature is not recognized by the XMLReader.
     * @throws SAXNotSupportedException If the feature is not supported by the XMLReader.
     */
    private static void disableFeatures(String features, final XMLReader xmlReader) 
        throws SAXNotRecognizedException, SAXNotSupportedException {
        StringTokenizer token;
        if (features != null) {
            token = new StringTokenizer(features, ", ");
            while (token.hasMoreTokens()) {
                xmlReader.setFeature(token.nextToken(), true);
            }
        }
    }

    /**
     * Returns the currently configured NodeType to use for Java 
     * primitives. A null value will be returned if no NodeType was 
     * specified, indicating the default NodeType should be used.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return the NodeType assigned to Java primitives, or null
     * if no NodeType was specified.
     * @see #getDefaultPrimitiveNodeType()
     */
    public NodeType getPrimitiveNodeType() {
        return getDefaultPrimitiveNodeType();
    } //-- getPrimitiveNodeType
    

    /**
     * Returns the NodeType to use for Java primitives.
     * A null value will be returned if no NodeType was specified,
     * indicating the default NodeType should be used.
     *
     * @return the NodeType assigned to Java primitives, or null
     * if no NodeType was specified.
     *
     * @see #getPrimitiveNodeType()
     */
    public static NodeType getDefaultPrimitiveNodeType() {
        
        if (_defaultValues.primitiveNodeType != null) 
            return _defaultValues.primitiveNodeType;
            
        String prop = getDefaultProperty(Property.PrimitiveNodeType, null);
        if (prop == null) return null;
        
        _defaultValues.primitiveNodeType = NodeType.getNodeType(prop);
        return _defaultValues.primitiveNodeType;
    } //-- getDefaultPrimitiveNodeType

    
    /**
     * Returns an XML document parser implementing the requested
     * set of features. The feature list is a comma separated list
     * of features that parser may or may not support. No errors are
     * generated for unsupported features. If the feature list is not
     * null, it overrides the default feature list specified in the
     * configuration file, including validation and Namespaces.
     *
     * @return A suitable XML parser
     */
    public XMLReader getXMLReader()
    {
        return getDefaultXMLReader( null ) ;
        
    } //-- getXMLReader
    
    /**
     * Returns an XML document parser implementing the requested
     * set of features. The feature list is a comma separated list
     * of features that parser may or may not support. No errors are
     * generated for unsupported features. If the feature list is not
     * null, it overrides the default feature list specified in the
     * configuration file, including validation and Namespaces.
     *
     * @return A suitable XML parser
     */
    public static XMLReader getDefaultXMLReader()
    {
        return getDefaultXMLReader( null ) ;
    } //-- getDefaultXMLReader
    
    /**
     * Returns an XML document parser implementing the requested
     * set of features. The feature list is a comma separated list
     * of features that parser may or may not support. No errors are
     * generated for unsupported features. If the feature list is not
     * null, it overrides the default feature list specified in the
     * configuration file, including validation and Namespaces.
     *
     * @param features The requested feature list, null for the
     *   defaults
     * @return A suitable XML parser
     */
    public static XMLReader getDefaultXMLReader( String features )
    {
        String prop;
        XMLReader reader = null;
        
        //-- validation?
        prop = getDefault().getProperty( Property.ParserValidation, "false" );
        boolean validation = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
                               
        //-- namespaces?
        prop = getDefault().getProperty( Property.Namespaces, "false" );
        boolean namespaces = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
        

        //-- which parser?
        prop = getDefault().getProperty( Property.Parser );
        if (( prop == null ) || (prop.length() == 0))  {
            // If no parser class was specified, check for JAXP
            // otherwise we default to Xerces.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(namespaces);
            factory.setValidating(validation);
            try {
                SAXParser saxParser = factory.newSAXParser();
                reader = saxParser.getXMLReader();
            } catch(ParserConfigurationException pcx) {
                LOG.error(Messages.format("conf.configurationError", pcx));
            } catch(org.xml.sax.SAXException sx) {
                LOG.error(Messages.format("conf.configurationError", sx));
            }
            
        }
        
        if (reader == null) {
            if ((prop == null) ||
                (prop.length() == 0) ||
                (prop.equalsIgnoreCase("xerces"))) 
            {
                prop = "org.apache.xerces.parsers.SAXParser";
            }
        
            // If a parser class was specified, we try to create it and
            // complain about creation error.
            try {
                Class cls;
                cls = Class.forName( prop );
                reader = (XMLReader) cls.newInstance();
            } catch ( Exception except ) {
                throw new RuntimeException( Messages.format( "conf.failedInstantiateParser",
                                                            prop, except ) );
            }
        }

        StringTokenizer token;
        setFeaturesOnXmlReader(features, validation, namespaces, reader);
        return reader;
    } //-- getDefaultXMLReader

    /**
     * Returns a new instance of the specified Regular Expression
     * Evaluator, or null if no validator was specified
     *
     * @return the regular expression evaluator,
     * @see #getDefaultRegExpEvaluator()
     */
    public RegExpEvaluator getRegExpEvaluator() {
        return getDefaultRegExpEvaluator();
    } //-- getRegExpEvaluator
    
    /**
     * Returns a new instance of the specified Regular Expression
     * Evaluator, or null if no validator was specified
     *
     * @return the regular expression evaluator,
     *
     * @see #getRegExpEvaluator()
     */
    public static RegExpEvaluator getDefaultRegExpEvaluator() {

        String prop = getDefault().getProperty( Property.RegExp );
        if ( prop == null ) return null;

        RegExpEvaluator regex = null;

        try {
            Class cls = _defaultValues.regExpEvalClass;
            if (cls == null) cls = Class.forName( prop );
            regex = (RegExpEvaluator) cls.newInstance();
        }
        catch ( Exception except ) {
            throw new RuntimeException( Messages.format( "conf.failedInstantiateRegExp",
                                                         prop, except ) );
        }

        return regex;
    } //-- getRegExpEvaluator

    /**
     * Returns a serializer for producing an XML instance document.
     * The caller can specify an alternative output format, may reuse
     * this serializer across several streams, and may serialize both
     * DOM and SAX events. 
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return A suitable serializer
     * @see #getDefaultSerializer()
     */
    public abstract Serializer getSerializer();

    /**
     * Returns a default serializer for producing an XML document.
     * The caller can specify an alternative output format, may reuse
     * this serializer across several streams, and may serialize both
     * DOM and SAX events. If such control is not required, it is
     * recommended to call one of the other two methods.
     *
     * @return A suitable serializer
     * @see #getSerializer()
     */
    public static Serializer getDefaultSerializer() {
        Serializer serializer = getSerializerFactory(getDefault()).getSerializer();
        serializer.setOutputFormat(getDefaultOutputFormat());
        return serializer;
    }

    /**
     * Returns the currently configured OutputFormat for use with a Serializer.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @return the currently configured OutputFormat.
     * @see #getDefaultOutputFormat()
     */
    public abstract OutputFormat getOutputFormat();

    /**
     * Returns the default OutputFormat for use with a Serializer.
     *
     * @return the default OutputFormat
    **/
    public static OutputFormat getDefaultOutputFormat() {

        boolean indent = false;
        
        String prop = getDefault().getProperty( Property.Indent, "" );

        //-- get default indentation
        indent = ( prop.equalsIgnoreCase( TRUE_VALUE ) ||
                   prop.equalsIgnoreCase( ON_VALUE ) );

        OutputFormat format = getSerializerFactory(getDefault()).getOutputFormat();
        format.setMethod(OutputFormat.XML);
        format.setIndenting(indent);
        
        // There is a bad interaction between the indentation and the
        // setPreserveSpace option. The indentated output is strangely indented.
        if (!indent)
            format.setPreserveSpace(true); 

        return format;
    } //-- getOutputFormat


    /**
     * Returns a serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @param output the output stream
     * @return A suitable serializer
     */
    public abstract DocumentHandler getSerializer( OutputStream output )
        throws IOException;

    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output the output stream
     * @return A suitable serializer
     */
    public static DocumentHandler getDefaultSerializer( OutputStream output )
        throws IOException
    {
        Serializer      serializer;
        DocumentHandler docHandler;

        serializer = getDefaultSerializer();
        serializer.setOutputByteStream( output );
        docHandler = serializer.asDocumentHandler();
        if ( docHandler == null )
            throw new RuntimeException( Messages.format( "conf.serializerNotSaxCapable",
                                                         serializer.getClass().getName() ) );
        return docHandler;
    }


    /**
     * Returns a serializer for producing an XML instance document to
     * the designated output stream using the default serialization
     * format.
     *
     * Design note: This method should be overloaded by any 
     * sub-classes.
     *
     * @param output the Writer to write data to.
     * @return A suitable serializer
     */
    public abstract DocumentHandler getSerializer( Writer output )
        throws IOException;

    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output the Writer to write data to.
     * @return A suitable serializer
     */
    public static DocumentHandler getDefaultSerializer( Writer output )
        throws IOException
    {
        Serializer      serializer;
        DocumentHandler docHandler;

        serializer = getDefaultSerializer();
        serializer.setOutputCharStream( output );
        docHandler = serializer.asDocumentHandler();
        if ( docHandler == null )
            throw new RuntimeException( Messages.format( "conf.serializerNotSaxCapable",
                                                         serializer.getClass().getName() ) );
        return docHandler;
    }

    /**
     * Called by {@link #getDefault} to load the configuration the
     * first time. Will not complain about inability to load
     * configuration file from one of the default directories, but if
     * it cannot find the JAR's configuration file, will throw a
     * run time exception.
     */
    protected static void loadDefaults()
    {
		_defaultProps = loadProperties( Property.ResourceName, Property.FileName);

        String     prop;
        prop = _defaultProps.getProperty( Property.Debug, "" );
        if ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) )
            _defaultValues.debug = true;
        prop = _defaultProps.getProperty( Property.MarshallingValidation, "" );
        if ( prop.equalsIgnoreCase( "false" ) || prop.equalsIgnoreCase( "off" ) )
            _defaultValues.marshallingValidation = false;

        prop = _defaultProps.getProperty( Property.StrictElements, "" );
        if ( prop.equalsIgnoreCase( "false" ) || prop.equalsIgnoreCase( "off" ) )
            _defaultValues.strictElements = false;
        else
            _defaultValues.strictElements = true;

        prop = null;
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
	
	
	/**
	 * Inner class to hold values of the configuration
	 */
	static class ConfigValues {
	    
        /**
         * True if the configuration specified debugging.
         */
        boolean debug = false;

        /**
         * True if the configuration specified strictElements
         */
        boolean strictElements = false;

        /**
         * True if the default configuration specified validation in the marshalling Framework
         * True, by default!
         */
        boolean  marshallingValidation = true;

        /**
         * The naming conventions for the XML Framework
         */
        XMLNaming naming = null;
        
        /**
         * The NodeType assigned to java primitives
         */
        NodeType primitiveNodeType = null;
        
        /**
         * The class to use for regular expression evaluation
         */
        Class regExpEvalClass = null;
        
    } //-- class: ConfigValues
	
	/**
	 * Returns the currently configured XMLSerializerFactory instance.
	 * @param props Property set to use.
	 * @return XMLSerializerFactory to use by Castor
	 */
	static protected XMLSerializerFactory getSerializerFactory(Properties props) {
    	XMLSerializerFactory serializerFactory;
        String serializerFactoryName = 
        	props.getProperty(Property.SERIALIZER_FACTORY, Property.DEFAULT_SERIALIZER_FACTORY);
        
        try {
            serializerFactory = (XMLSerializerFactory) 
            Class.forName(serializerFactoryName).newInstance();
        } catch (Exception except) {
            throw new RuntimeException(
                    Messages.format("conf.failedInstantiateSerializerFactory", 
                            serializerFactoryName, except));
        }
        return serializerFactory;
	}

} //-- Configuration

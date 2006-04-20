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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.util;


import java.util.Properties;
import java.io.OutputStream;
import java.io.Writer;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.net.URL;
import org.xml.sax.SAXException;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.XMLReader;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Method;
import org.exolab.castor.util.Messages;


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
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class Configuration
{


    /**
     * Names of properties used in the configuration file.
     */
    public static class Property
    {

        /**
         * Property specifying the class name of the XML serializer to use.
         * <pre>
         * org.exolab.castor.serializer
         * </pre>
         */
        public static final String Serializer = "org.exolab.castor.serializer";

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
        public static final String Validation = "org.exolab.castor.parser.validation";

        /**
         * Property specifying whether to support Namespaces by default.
         * <pre>
         * org.exolab.castor.SAXParser.namespaces
         * </pre>
         */
        public static final String Namespaces = "org.exolab.castor.parser.namespaces";

        /**
         * Property specifying whether to use validation in the Marshalling Framework
         * <pre>
         * org.exolab.castor.marshalling.validation
         * </pre>
         */
         public static final String Marshalling_validation = "org.exolab.castor.marshalling.validation";

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
         * The name of the configuration file.
         * <pre>
         * castor.properties
         * </pre>
         */
        public static final String FileName = "castor.properties";

        static final String ResourceName = "/org/exolab/castor/castor.properties";

    }


    private static class Features
    {
        public static final String Validation = "http://xml.org/sax/features/validation";
        public static final String Namespaces = "http://xml.org/sax/features/namespaces";
    }


    // Some static string definitions
    private static final String TRUE_VALUE  = "true";
    private static final String ON_VALUE    = "on";

	/**
     * The default properties loaded from the configuration file.
     */
    private static Properties _default;


    /**
     * True if the default configuration specified debugging.
     */
    private static boolean    _debug;

    /**
     * True if the default configuration specified validation in the marshalling Framework
     */
     private static boolean  _validation;

    /**
     * Returns true if the default configuration specified debugging.
     */
    public static boolean debug()
    {
        getDefault().getProperty(Property.Debug);
        return _debug;
    }

    /**
     * Returns true if the default configuration specified validation.
     */
    public static boolean validation()
    {
        getDefault().getProperty(Property.Validation);
        return _validation;
    }

    /**
     * Returns the default configuration file. Changes to the returned
     * properties set will affect all Castor functions relying on the
     * default configuration.
     *
     * @return The default configuration
     */
    public static synchronized Properties getDefault()
    {
        if ( _default == null ) {
            load();
        }
        return _default;
    }


    /**
     * Returns a property from the default configuration file.
     * Equivalent to calling <tt>getProperty</tt> on the result
     * of {@link #getDefault}.
     *
     * @param name The property name
     * @param default The property's default value
     * @return The property's value
     */
    public static String getProperty( String name, String defValue )
    {
        return getDefault().getProperty( name, defValue );
    }


    /**
     * Return an XML document parser implementing the feature list
     * specified in the configuration file.
     *
     * @return A suitable XML parser
     */
    public static Parser getParser()
    {
        return getParser( null );
    }


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
    public static Parser getParser( String features )
    {
        String prop;
        Parser parser;

        prop = getDefault().getProperty( Property.Parser );
        if ( prop == null || prop.equalsIgnoreCase( "xerces" ) ) {
            // If no parser class was specified, we default to Xerces.
            parser = new org.apache.xerces.parsers.SAXParser();
        } else {
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
        }

        if ( parser instanceof XMLReader ) {
            StringTokenizer token;
            boolean         flag;

            prop = getDefault().getProperty( Property.Validation, "false" );
            flag = ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) );
            try {
                ( (XMLReader) parser ).setFeature( Features.Validation, flag );
            } catch ( SAXException except ) {
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", except ) );
            }
            prop = getDefault().getProperty( Property.Namespaces, "false" );
            flag = ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) );
            try {
                ( (XMLReader) parser ).setFeature( Features.Namespaces, flag );
            } catch ( SAXException except ) {
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", except ) );
            }

            features = getDefault().getProperty( Property.ParserFeatures, features );
            if ( features != null ) {
                token = new StringTokenizer( features, ", " );
                while ( token.hasMoreTokens() ) {
                    try {
                        ( (XMLReader) parser ).setFeature( token.nextToken(), true );
                    } catch ( SAXException except ) {
                        Logger.getSystemLogger().println( Messages.format( "conf.configurationError", except ) );
                    }
                }
            }
        }
        return parser;
    }


    /**
     * Returns a new instance of the specified Regular Expression
     * Evaluator, or null if no validator was specified
     *
     * @return the regular expression evaluator,
     *
    **/
    public static RegExpEvaluator getRegExpEvaluator() {

        String prop = getDefault().getProperty( Property.RegExp );

        RegExpEvaluator regex = null;

        if ( prop == null ) {
            return null;
        }
        else {
            try {
                Class cls;
                cls = Class.forName( prop );
                regex = (RegExpEvaluator) cls.newInstance();
            }
            catch ( Exception except ) {
                throw new RuntimeException( Messages.format( "conf.failedInstantiateRegExp",
                                                             prop, except ) );
            }
        }

        return regex;
    } //-- getRegExpEvaluator

    /**
     * Returns a default serializer for producing an XML document.
     * The caller can specify an alternative output format, may reuse
     * this serializer across several streams, and may serialize both
     * DOM and SAX events. If such control is not required, it is
     * recommended to call one of the other two methods.
     *
     * @param output The output stream
     * @return A suitable serializer
     */
    public static Serializer getSerializer()
    {
        String     prop;
        Serializer serializer;

        prop = getDefault().getProperty( Property.Serializer );
        if ( prop == null || prop.equalsIgnoreCase( "xerces" ) ) {
            // If no parser class was specified, we default to Xerces.
            serializer = new org.apache.xml.serialize.XMLSerializer();
        } else {
            try {
                serializer = (Serializer) Class.forName( prop ).newInstance();
            } catch ( Exception except ) {
                throw new RuntimeException( Messages.format( "conf.failedInstantiateSerializer",
                                                             prop, except ) );
            }
        }
        serializer.setOutputFormat( getOutputFormat() );
        return serializer;
    }

    /**
     * Returns the default OutputFormat for use with a Serializer.
     *
     * @return the default OutputFormat
    **/
    public static OutputFormat getOutputFormat() {

        boolean indent = false;
        String prop = getDefault().getProperty( Property.Indent, "" );

        //-- get default indentation
        indent = ( prop.equalsIgnoreCase( TRUE_VALUE ) ||
                   prop.equalsIgnoreCase( ON_VALUE ) );

        return new OutputFormat( Method.XML, null, indent );
    } //-- getOutputFormat


    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output The output stream
     * @return A suitable serializer
     */
    public static DocumentHandler getSerializer( OutputStream output )
        throws IOException
    {
        Serializer      serializer;
        DocumentHandler docHandler;

        serializer = getSerializer();
        serializer.setOutputByteStream( output );
        docHandler = serializer.asDocumentHandler();
        if ( docHandler == null )
            throw new RuntimeException( Messages.format( "conf.serializerNotSaxCapable",
                                                         serializer.getClass().getName() ) );
        return docHandler;
    }


    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output The output stream
     * @return A suitable serializer
     */
    public static DocumentHandler getSerializer( Writer output )
        throws IOException
    {
        Serializer      serializer;
        DocumentHandler docHandler;

        serializer = getSerializer();
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
    protected static void load()
    {
		_default = loadProperties( Property.ResourceName, Property.FileName);

        String     prop;
        prop = _default.getProperty( Property.Debug, "" );
        if ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) )
            _debug = true;
        prop = _default.getProperty( Property.Marshalling_validation, "" );
        if ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) )
            _validation = true;
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
        InputStream is;

        // Get detault configuration from the Castor JAR.
        // Complain if not found.
        Properties properties = new Properties();
        try {
            properties.load( Configuration.class.getResourceAsStream( resourceName ) );
        } catch ( Exception except ) {
            // This should never happen
            throw new RuntimeException( Messages.format( "conf.noDefaultConfigurationFile",
                                                         fileName ) );
        }

        // Get overriding configuration from the Java
        // library directory, ignore if not found.
        try {
            file = new File( System.getProperty( "java.home" ), "lib" );
            file = new File( file, fileName );
            if ( file.exists() ) {
                properties = new Properties( properties );
                properties.load( new FileInputStream( file ) );
            }
        } catch ( IOException except ) {
            // Do nothing
        }

        // Get overriding configuration from the classpath,
        // ignore if not found.
        try {
            is = Configuration.class.getResourceAsStream( "/" + fileName );
            if ( is != null ) {
                properties = new Properties( properties );
                properties.load( is );
            }
        } catch ( Exception except ) {
            // Do nothing
        }

		return properties;
	}
} //-- Configuration

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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
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
import org.xml.sax.SAXException;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.Configurable;
import org.xml.sax.helpers.ParserFactory;
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
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class Configuration
{


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
	 * org.exolab.castor.validation
	 * </pre>
	 */
	public static final String Validation = "org.exolab.castor.validation";

	/**
	 * Property specifying whether to support Propertypaces by default.
	 * <pre>
	 * org.exolab.castor.Propertypaces
	 * </pre>
	 */
	public static final String Propertypaces = "org.exolab.castor.Propertypaces";

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
	public static final String Propertypaces = "http://xml.org/sax/features/Propertypaces";
    }


    /**
     * The default properties loaded from the configuration file.
     */
    private static Properties _default;


    /**
     * True if the default configuration specified debugging.
     */
    private static boolean    _debug;


    /**
     * Returns true if the default configuration specified debugging.
     */
    public static boolean debug()
    {
	return _debug;
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
     * Return an XML document parser implementing the feature list
     * specified in the configuration file.
     *
     * @return A suitable XML parser
     */
    public static Parser getParser()
    {
	return getParser( "" );
    }


    /**
     * Returns an XML document parser implementing the requested
     * set of features. The feature list is a comma separated list
     * of features that parser may or may not support. No errors are
     * generated for unsupported features. If the feature list is not
     * null, it overrides the default feature list specified in the
     * configuration file, including validation and Propertypaces.
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
	if ( prop == null ) {
	    // If no parser class was specified, we try to create a default
	    // one using the parser factorie's properties. If this fail,
	    // we complain about missing property.
	    try {
		parser = ParserFactory.makeParser();
	    } catch ( Exception except ) {
		throw new RuntimeException( Messages.format( "castor.conf.missingProperty",
							     Property.Parser ) );
	    }
	} else {
	    // If a parser class was specified, we try to create it and
	    // complain about creation error.
	    try {
		parser = ParserFactory.makeParser( prop );
	    } catch ( Exception except ) {
		throw new RuntimeException( Messages.format( "castor.conf.failedInstantiateParser",
							     prop, except ) );
	    }
	}

	if ( parser instanceof Configurable ) {
	    StringTokenizer token;
	    boolean         flag;

	    if ( features == null ) {
		prop = getDefault().getProperty( Property.Validation, "false" );
		flag = ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) );
		try {
		    ( (Configurable) parser ).setFeature( Features.Validation, flag );
		} catch ( SAXException except ) {
		    // Ignore if feature not supported
		}
		prop = getDefault().getProperty( Property.Propertypaces, "false" );
		flag = ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) );
		try {
		    ( (Configurable) parser ).setFeature( Features.Propertypaces, flag );
		} catch ( SAXException except ) {
		    // Ignore if feature not supported
		}
		
		features = getDefault().getProperty( Property.ParserFeatures, features );
	    }
	    token = new StringTokenizer( features, ", " );
	    while ( token.hasMoreTokens() ) {
		try {
		    ( (Configurable) parser ).setFeature( token.nextToken(), true );
		} catch ( SAXException except ) {
		    // Ignore if feature not supported
		}
	    }
	}
	return parser;
    }


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
	if ( prop == null )
	    throw new RuntimeException( Messages.format( "castor.conf.missingProperty",
							 Property.Serializer ) );
	try {
  	    serializer = (Serializer) Class.forName( prop ).newInstance();
	    prop = getDefault().getProperty( Property.Indent, "" );
	    if ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) ) {
		serializer.setOutputFormat( new OutputFormat( Method.XML, null, true ) );
	    }
	    return serializer;
	} catch ( Exception except ) {
	    throw new RuntimeException( Messages.format( "castor.conf.failedInstantiateSerializer",
							 prop, except ) );
	}
    }


    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output The output stream
     * @return A suitable serializer
     */
    public static DocumentHandler getSerializer( OutputStream output )
    {
	Serializer      serializer;
	DocumentHandler docHandler;

	serializer = getSerializer();
	try {
	    serializer.setOutputByteStream( output );
	} catch ( IOException except ) {
	    // This should never happen
	}
	docHandler = serializer.asDocumentHandler();
	if ( docHandler == null )
	    throw new RuntimeException( Messages.format( "castor.conf.serializerNotSaxCapable",
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
    {
	Serializer      serializer;
	DocumentHandler docHandler;

	serializer = getSerializer();
	serializer.setOutputCharStream( output );
	docHandler = serializer.asDocumentHandler();
	if ( docHandler == null )
	    throw new RuntimeException( Messages.format( "castor.conf.serializerNotSaxCapable",
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
	File        file;
	InputStream is;

	// Get detault configuration from the Castor JAR.
	// Complain if not found.
	_default = new Properties();
	try {
	    _default.load( Configuration.class.getResourceAsStream( Property.ResourceName ) );
	} catch ( Exception except ) {
	    // This should never happen
	    throw new RuntimeException( Messages.format( "castor.conf.notDefaultConfigurationFile",
							 Property.FileName ) );
	}

	// Get overriding configuration from the Java
	// library directory, ignore if not found.
	file = new File( System.getProperty( "java.home" ), "lib" );
	file = new File( file, Property.FileName );
	if ( file.exists() ) {
	    _default = new Properties( _default );
	    try {
		_default.load( new FileInputStream( file ) );
	    } catch ( IOException except ) {
		// Do nothing
	    }
	}

	// Get overriding configuration from the classpath,
	// ignore if not found.
	try {
	    is = Configuration.class.getResourceAsStream( "/" + Property.FileName );
	    if ( is != null ) {
		_default = new Properties( _default );
		_default.load( is );
	    }
	} catch ( Exception except ) {
	    // Do nothing
	}

	String     prop;

	prop = _default.getProperty( Property.Debug, "" );
	if ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) )
	    _debug = true;
    }


}

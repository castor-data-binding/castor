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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Messages;
import org.castor.xml.JavaNaming;
import org.castor.xml.JavaNamingImpl;
import org.castor.xml.XMLConfiguration;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.OutputFormat;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.XMLNaming;
import org.exolab.castor.xml.util.DefaultNaming;
import org.exolab.castor.xml.util.XMLParserUtils;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
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
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class LocalConfiguration extends Configuration {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log _log = LogFactory.getFactory().getInstance(LocalConfiguration.class);

    /**
     * The properties loaded from the local configuration file.
     */
    private Properties   _props;
    
    /**
     * The local configuration values (retrieved from a local properties file)
     */
    private ConfigValues _values = new ConfigValues();

    /**
     * Cache of loaded configurations
     */
    private static Hashtable _configurations = new Hashtable();
    
    /** 
     * The full url to the castor.properties file that was loaded
     */
    private String _resourceUrl = null;
    
    /**
     * Creates a new instance of LocalConfiguration
     */
    public LocalConfiguration() {
        super();
        load();
        //-- Disable for now, not needed yet until we work on
        //-- a way to differentiate different configurations
        //-- throughout the framework...
        //String key = (_resourceUrl == null) ? "" : _resourceUrl;
        //_configurations.put(key, this);
    } //-- LocalConfiguration
    
    /**
     * Returns an instance of the LocalConfiguration, if a previous 
     * configuration has already been loaded for local resource, 
     * it will be returned.
     *
     * @return the LocalConfiguration
     */
    public static synchronized LocalConfiguration getInstance() {
        
        LocalConfiguration config = null;
        URL url = null;
        
        //-- disable for now, it's a huge performance hit, for no gain,
        //-- at the moment anyway. We need to come up with a clean, fast
        //-- way to differentiate between different configurations
        //-- throughout the framework...
        //url = LocalConfiguration.class.getResource("/" + Property.CONFIG_FILENAME_PROPERTY);
        
        String key = "";
        if (url != null) {
            key = url.toString();
        }
        config = (LocalConfiguration)_configurations.get(key);
        if (config == null) {
            config = new LocalConfiguration();
            _configurations.put(key, config);
        }
        return config;
        
    } //-- getInstance
    
    
    /**
     * Returns true if the current configuration has enabled debugging.
     *
     * @return true if the current configuration has enabled debugging,
     * otherwise false.
     */
    public boolean debug()
    {
        getProperties();
        return _values.debug;
        
    } //-- debug

    /**
     * Access to the property specifying whether to apply strictness to elements when
     * unmarshalling. Default is true which means that elements appearing in the
     * XML Documnt which cannot be mapped to a class cause a SAXException to be thrown.
     * If set to false, these 'unknown' elements are ignored
     *
     * @return true if element processing should be "strict".
     */
    public boolean strictElements() {
        getProperties();
        return _values.strictElements;
    } //-- strictElements
    
    /**
     * Returns true if the default configuration specified validation in
     * the marshalling framework.
     */
    public boolean marshallingValidation()
    {
        getProperties();
        return _values.marshallingValidation;
        
    } //-- marshallingValidation
    
    /**
     * Returns the current properties from the configuration file(s). 
     * The Properties returned may be empty, but never null.
     *
     * @return The current set of configuration properties. 
     */
    public synchronized Properties getProperties()
    {
        if ( _props == null ) {
            load();
        }
        return _props;
    }

    /**
     * Returns the naming conventions to use for the XML framework
     *
     * @return the naming conventions to use for the XML framework     
     */
    public XMLNaming getXMLNaming() {
        return getXMLNaming(null);
    }

    /**
     * Returns the naming conventions to use for the XML framework
     *
     * @return the naming conventions to use for the XML framework     
     */
    public XMLNaming getXMLNaming(ClassLoader classLoader) {
        
        if (_values.naming != null) return _values.naming;
        
        String prop = getProperty( Property.Naming, null);
        if ((prop == null) || (prop.equalsIgnoreCase("lower"))) {
            _values.naming = new DefaultNaming();
        }
        else if (prop.equalsIgnoreCase("mixed")) {
            DefaultNaming dn = new DefaultNaming();
            dn.setStyle(DefaultNaming.MIXED_CASE_STYLE);
            _values.naming = dn;
        }
        else {
            try {
                Class cls = null;
                if (classLoader != null) {
                    cls = classLoader.loadClass(prop); 
                } else {
                    cls = Class.forName(prop);
                }
                _values.naming = (XMLNaming) cls.newInstance();
            }
            catch (Exception except) {
                throw new RuntimeException("Failed to load XMLNaming: " + 
                    except);
            }
        }
        return _values.naming;
    } //-- getXMLNaming

    /**
     * The {@link JavaNaming} instance to be used.
     * @return {@link JavaNaming} instance to be used.
     */
    public JavaNaming getJavaNaming() {
        return new JavaNamingImpl();
    }

    /**
     * Return an XML document parser implementing the feature list
     * specified in the configuration file.
     *
     * @return A suitable XML parser
     */
    public Parser getParser()
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
    public Parser getParser(final String features) {
        //-- validation?
        String validationProperty = getProperties().getProperty(Property.ParserValidation, "false");
        boolean validation = (validationProperty.equalsIgnoreCase("true") 
                || validationProperty.equalsIgnoreCase("on"));
                               
        //-- namespaces?
        String namespacesProperty = getProperties().getProperty(Property.Namespaces, "false");
        boolean namespaces = (namespacesProperty.equalsIgnoreCase("true") 
                || namespacesProperty.equalsIgnoreCase("on"));
        

        // obtain a custom SAX parser class name from custom property file
        String parserClassName = getProperties().getProperty(Property.Parser);
        
        Parser parser;
        if (parserClassName == null || parserClassName.length() == 0) {
            SAXParser saxParser = XMLParserUtils.getSAXParser(validation, namespaces);
            try {
                return saxParser.getParser();
            } catch (SAXException e) {
                _log.error(Messages.format("conf.configurationError", e));
            }
        }
        
        // TODO[WG]: in my opinion, this should be removed and an exception be thrown
        if ((parserClassName == null) || 
            (parserClassName.length() == 0) || 
            (parserClassName.equalsIgnoreCase("xerces"))) {
            parserClassName = "org.apache.xerces.parsers.SAXParser";
        }
        

        // if a parser class was specified, we try to create it
        parser = XMLParserUtils.instantiateParser(parserClassName);

        if (parser instanceof XMLReader) {
            XMLReader xmlReader = (XMLReader) parser;
            XMLParserUtils.setFeaturesOnXmlReader(
                    _props.getProperty(XMLConfiguration.PARSER_FEATURES, features),
                    _props.getProperty(XMLConfiguration.PARSER_FEATURES_DISABLED, ""),
                    validation, namespaces, xmlReader);
        }
        
        return parser;
        
    }
    
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
        return getXMLReader( null ) ;
        
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
    public XMLReader getXMLReader( String features )
    {
        
        String prop;
        XMLReader reader = null;
        
        //-- validation?
        prop = getProperties().getProperty( Property.ParserValidation, "false" );
        boolean validation = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
                               
        //-- namespaces?
        prop = getProperties().getProperty( Property.Namespaces, "false" );
        boolean namespaces = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
        

        // obtain a custom SAX parser class name from custom property file
        prop = getProperties().getProperty( Property.Parser );
        
        if (( prop == null ) || (prop.length() == 0)) {
            SAXParser saxParser = XMLParserUtils.getSAXParser(validation, namespaces);
            try {
                reader = saxParser.getXMLReader();
            }
            catch(org.xml.sax.SAXException sx) {
                _log.error( Messages.format( "conf.configurationError", sx ) );
            }
        }
        
        if (reader == null) {
            if ((prop == null) || 
                (prop.length() == 0) || 
                (prop.equalsIgnoreCase("xerces"))) 
            {
                prop = "org.apache.xerces.parsers.SAXParser";
            }
        

            reader = XMLParserUtils.instantiateXMLReader(prop);
            // if a parser class was specified, we try to create it
        }

        XMLParserUtils.setFeaturesOnXmlReader(
                _props.getProperty(XMLConfiguration.PARSER_FEATURES, features),
                _props.getProperty(XMLConfiguration.PARSER_FEATURES_DISABLED, ""),
                validation, namespaces, reader);
        
        return reader;
        
    } //-- getXMLReader
    

    /**
     * Returns the NodeType to use for Java primitives.
     * A null value will be returned if no NodeType was specified,
     * indicating the default NodeType should be used.
     *
     * @return the NodeType assigned to Java primitives, or null
     * if no NodeType was specified.
    **/
    public NodeType getPrimitiveNodeType() {
        
        if (_values.primitiveNodeType != null) 
            return _values.primitiveNodeType;
            
        String prop = getProperty(Property.PrimitiveNodeType, null);
        if (prop == null) return null;
        _values.primitiveNodeType = NodeType.getNodeType(prop);
        return _values.primitiveNodeType;
    } //-- getPrimitiveNodeType
    
    /**
     * Returns a new instance of the specified Regular Expression
     * Evaluator, or null if no validator was specified
     *
     * @return the regular expression evaluator,
     *
    **/
    public RegExpEvaluator getRegExpEvaluator() {

        String prop = getProperties().getProperty( Property.RegExp );

        RegExpEvaluator regex = null;

        if ( prop == null ) return null;
        try {
            if (_values.regExpEvalClass == null) {
                _values.regExpEvalClass = Class.forName( prop );
            }
            regex = (RegExpEvaluator) _values.regExpEvalClass.newInstance();
        }
        catch ( Exception except ) {
            throw new RuntimeException( Messages.format( "conf.failedInstantiateRegExp",
                                                         prop, except ) );
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
     * @return A suitable serializer
     */
    public Serializer getSerializer() {
        Serializer serializer = getSerializerFactory(_props).getSerializer();
        serializer.setOutputFormat(getOutputFormat());
        return serializer;
    }
    
    /**
     * Returns the default OutputFormat for use with a Serializer.
     *
     * @return the default OutputFormat
    **/
    public OutputFormat getOutputFormat() {

        boolean indent = false;
        
        String prop = _props.getProperty( Property.Indent, "" );

        //-- get default indentation
        indent = ( prop.equalsIgnoreCase( TRUE_VALUE ) ||
                   prop.equalsIgnoreCase( ON_VALUE ) );

        OutputFormat format = getSerializerFactory(_props).getOutputFormat();
        format.setMethod(OutputFormat.XML);
        format.setIndenting(indent);
        
        // There is a bad interaction between the indentation and the
        // setPreserveSpace option. The indentated output is strangely indented.
        if (!indent)
            format.setPreserveSpace(true); 

        return format;
    } //-- getOutputFormat


    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output The output stream
     * @return A suitable serializer
     */
    public DocumentHandler getSerializer( OutputStream output )
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
    public DocumentHandler getSerializer( Writer output )
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
     * Indicates whether validation for sequence order should be lenient.
     *
     * @return True if sequence order validation should be lenient.
     */
    public boolean getLenientSequenceOrder()
    {
        return Boolean.valueOf(getProperties().getProperty(Property.LenientSequenceOrder, "false")).booleanValue();
    }

    /**
     * Indicates whether id/href validation should be lenient.
     *
     * @return True if id/href validation should be lenient.
     */
    public boolean getLenientIdValidation()
    {
        return Boolean.valueOf(getProperties().getProperty(Property.LenientIdValidation, "false")).booleanValue();
    }

    /**
     * Calls {@link #getDefault()} to load the configuration the
     * first time and then looks for a local configuration to
     * merge in with the defaults. Will not complain about inability 
     * to load local configuration file from one of the default 
     * directories, but if it cannot find the JAR's configuration file, 
     * will throw a run time exception.
     */
    protected void load()
    {
        //-- load default properties
        _props = new Properties(getDefault());
        
        try {
		    loadProperties( Property.FileName );
		}
		catch(FileNotFoundException fnfe) {
		    //-- Ignore, simply no user supplied castor.properties file.
		    //-- If no default castor.properties is found an exception
		    //-- will occur in the above call to super.load.
		}

        String     prop;
        prop = _props.getProperty( Property.Debug, "" );
        if ( prop.equalsIgnoreCase( "true" ) || prop.equalsIgnoreCase( "on" ) )
            _values.debug = true;
        prop = _props.getProperty( Property.MarshallingValidation, "" );
        if ( prop.equalsIgnoreCase( "false" ) || prop.equalsIgnoreCase( "off" ) )
            _values.marshallingValidation = false;

        prop = _props.getProperty( Property.StrictElements, "" );
        if ( prop.equalsIgnoreCase( "false" ) || prop.equalsIgnoreCase( "off" ) )
            _values.strictElements = false;
        else
            _values.strictElements = true;

        prop = null;
    }

    /**
     * Load the configuration will not complain about inability to load
     * configuration file from one of the default directories, but if
     * it cannot find the JAR's configuration file, will throw a
     * run time exception.
     */
	public void loadProperties(String fileOrResourceName)
	    throws FileNotFoundException
	{
	    

        boolean found = false;
        
        // Get overriding configuration from the classpath,
        // ignore if not found. If found, merge any existing
        // properties.
        try {      
            URL url = getClass().getResource("/" + fileOrResourceName);
            if (url != null) {
                _resourceUrl = url.toString();
                if(_log.isDebugEnabled()) {
                    _log.debug ("Trying to load configuration file from " + _resourceUrl);
                }
                _props.load( url.openStream() );
                //-- debug information
                //System.out.println("merging local configuration: " + url.toExternalForm());
                //-- end debug information
                found = true;
            }      
        } catch ( Exception except ) {
            // Do nothing
        }
        
        //-- if not found, either it doesn't exist, or "." is not part of the
        //-- class path, try looking at local working directory
        if (!found) {
            try {      
                File file = new File(fileOrResourceName);
                if (file.exists() && file.canRead()) {
                    InputStream is = new FileInputStream(file);
                    _props.load( is );
                    is.close();
                    found = true;
                }
            } catch ( Exception except ) {
                //-- do nothing
            }
        }
        


        //-- Cannot find any castor.properties file(s).
        //if (!found) {
        //    throw new FileNotFoundException(fileOrResourceName);
        //}
	}
	
	

} //-- LocalConfiguration

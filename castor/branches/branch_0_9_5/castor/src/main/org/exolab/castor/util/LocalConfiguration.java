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


import java.util.Properties;
import java.io.OutputStream;
import java.io.Writer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLNaming;
import org.exolab.castor.xml.util.DefaultNaming;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


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
public final class LocalConfiguration extends Configuration {

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
        //url = LocalConfiguration.class.getResource("/" + Property.FileName);
        
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
                Class cls = Class.forName(prop);
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
    public Parser getParser( String features )
    {
        String prop;
        Parser parser;
        
        //-- validation?
        prop = getProperties().getProperty( Property.ParserValidation, "false" );
        boolean validation = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
                               
        //-- namespaces?
        prop = getProperties().getProperty( Property.Namespaces, "false" );
        boolean namespaces = ( prop.equalsIgnoreCase( "true" ) || 
                               prop.equalsIgnoreCase( "on" ) );
        

        //-- which parser?
        prop = getProperties().getProperty( Property.Parser );
        if (( prop == null ) || (prop.length() == 0)) {
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
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", pcx ) );
            }
            catch(org.xml.sax.SAXException sx) {
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", sx ) );
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

        if ( parser instanceof XMLReader ) {
            StringTokenizer token;
            XMLReader xmlReader = (XMLReader)parser;
            try {
                xmlReader.setFeature( Features.Validation, validation );
                xmlReader.setFeature( Features.Namespaces, namespaces );
                features = getProperties().getProperty( Property.ParserFeatures, features );
                if ( features != null ) {
                    token = new StringTokenizer( features, ", " );
                    while ( token.hasMoreTokens() ) {
                        xmlReader.setFeature( token.nextToken(), true );
                    }
                }
            } 
            catch ( SAXException except ) {
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", except ) );
            }
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
        

        //-- which parser?
        prop = getProperties().getProperty( Property.Parser );
        if (( prop == null ) || (prop.length() == 0)) {
            // If no parser class was specified, check for JAXP
            // otherwise we default to Xerces.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(namespaces);
            factory.setValidating(validation);
            try {
                SAXParser saxParser = factory.newSAXParser();
                reader = saxParser.getXMLReader();
            }
            catch(ParserConfigurationException pcx) {
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", pcx ) );
            }
            catch(org.xml.sax.SAXException sx) {
                Logger.getSystemLogger().println( Messages.format( "conf.configurationError", sx ) );
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
        try {
            reader.setFeature( Features.Validation, validation );
            reader.setFeature( Features.Namespaces, namespaces );
            features = getProperties().getProperty( Property.ParserFeatures, features );
            if ( features != null ) {
                token = new StringTokenizer( features, ", " );
                while ( token.hasMoreTokens() ) {
                    reader.setFeature( token.nextToken(), true );
                }
            }
        } 
        catch ( SAXException except ) {
            Logger.getSystemLogger().println( Messages.format( "conf.configurationError", except ) );
        }
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
        if (prop == null) 
            return null;
        else {
            _values.primitiveNodeType = NodeType.getNodeType(prop);
            return _values.primitiveNodeType;
        }
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

        if ( prop == null ) {
            return null;
        }
        else {
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
    public Serializer getSerializer()
    {
        String     prop;
        Serializer serializer;

        prop = _props.getProperty( Property.Serializer );
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
    public OutputFormat getOutputFormat() {

        boolean indent = false;
        String prop = _props.getProperty( Property.Indent, "" );

        //-- get default indentation
        indent = ( prop.equalsIgnoreCase( TRUE_VALUE ) ||
                   prop.equalsIgnoreCase( ON_VALUE ) );

        OutputFormat format = new OutputFormat();
        format.setMethod(Method.XML);
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
     * Calls {@link #getDefault} to load the configuration the
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
                //_log.debug ("Trying to load configuration file from " + _resourceUrl);
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

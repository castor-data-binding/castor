/*
 * Copyright 2007 Joachim Grueneis
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
 */
package org.castor.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.castor.util.Messages;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.util.RegExpEvaluator;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.OutputFormat;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.exolab.castor.xml.XMLNaming;
import org.exolab.castor.xml.XMLSerializerFactory;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.util.DefaultNaming;
import org.exolab.castor.xml.util.ResolverStrategy;
import org.exolab.castor.xml.util.XMLParserUtils;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The internal context is meant as center piece providing (and keeping) all
 * information that is required by Marshaller, Unmarshaller, SourceGenerator,
 * MappingTool, SchemaReader and SchemaWriter. It is created, filled with
 * initial data and put into all other parts of Castor by {@link XMLContext}.
 * It is NOT meant to be directly instantiated by user implementations!
 * For all other objects it provides access to Castor state information
 * (e.g. known descriptors) and configuration values.
 * 
 * @author <a href="mailto:jgrueneis At gmail DOT com">Joachim Grueneis</a>
 * @since 1.1.2
 */
public class InternalContext {
    /** Logger to be used. */
    private static final Log LOG = LogFactory.getFactory().getInstance(InternalContext.class);

    /** The configuration to use internally to provide parser, serializer, ... */
    private org.castor.core.util.Configuration _configuration;
    
    /**
     * {@link XMLClassDescriptorResolver} instance used for caching XML-related
     * class descriptors.
     */
    private XMLClassDescriptorResolver _xmlClassDescriptorResolver;
    
    /**
     * The XMLContext knows the one Introspector to be used.
     */
    private Introspector _introspector;

    /**
     * The {@link Resolver} to be used by Schema* stuff.
     */
    private Resolver _schemaResolver;

    /**
     * The XMLClassDescriptor resolver strategy to use.
     */
    private ResolverStrategy _resolverStrategy;

    /**
     * The {@link MappingLoader} to use.
     */
    private MappingLoader _mappingLoader;
    
    /**
     * The {@link XMLNaming} to be used.
     */
    private XMLNaming _xmlNaming;
    
    /**
     * The {@link JavaNaming} to be used.
     */
    private JavaNaming _javaNaming;

    /**
     * The class loader to use.
     */
    private ClassLoader _classLoader;

    /**
     * The {@link NodeType} to use for primitives.
     */
    private NodeType _primitiveNodeType;

    /**
     * The {@link RegExpevaluator}?to use.
     */
    private RegExpEvaluator _regExpEvaluator;

    /**
     * Creates an instance of {@link InternalContext}. The internal context is meant to
     * hold the configuration and state informations, but not necessarily retrieving
     * those values...
     */
    public InternalContext() {
        _configuration = XMLConfiguration.newInstance();
        _javaNaming = new JavaNamingImpl();
    }
    
    /**
     * Instructs Castor to load class descriptors from the mapping given.
     * @param mapping Castor XML mapping (file), from which the required class
     * descriptors will be derived. 
     * @throws MappingException If the {@link Mapping} cannot be loaded and analyzed successfully.
     */
    public void addMapping(final Mapping mapping) throws MappingException {
         MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
         mappingUnmarshaller.setInternalContext(this);
         MappingLoader mappingLoader = 
             mappingUnmarshaller.getMappingLoader(mapping, BindingType.XML);
         _xmlClassDescriptorResolver.setMappingLoader(mappingLoader);        
    }

    /**
     * Loads the class descriptor for the class instance specified. The use of this method is useful
     * when no mapping is used, as happens when the domain classes has been generated
     * using the XML code generator (in which case instead of a mapping file class
     * descriptor files will be generated).
     * 
     * @param clazz the class for which the associated descriptor should be loaded.
     * @throws ResolverException in case that resolving the Class fails fatally
     */ 
    public void addClass(final Class clazz) throws ResolverException { 
        _xmlClassDescriptorResolver.addClass(clazz);
    }

    /**
     * Loads the class descriptor for the class instance specified. The use of this method is useful
     * when no mapping is used, as happens when the domain classes hase been generated
     * using the XML code generator (in which case instead of a mapping file class
     * descriptor files will be generated).
     * 
     * @param clazzes the classes for which the associated descriptor should be loaded.
     * @throws ResolverException in case that resolving the Class fails fatally
     */ 
    public void addClasses(final Class[] clazzes) throws ResolverException {
        _xmlClassDescriptorResolver.addClasses(clazzes);
    }

    /**
     * Loads class descriptors from the package specified. The use of this method is useful
     * when no mapping is used, as happens when the domain classes hase been generated
     * using the XML code generator (in which case instead of a mapping file class
     * descriptor files will be generated).
     * <p>
     * Please note that this functionality will work only if you provide the <tt>.castor.cdr</tt>
     * file with your generated classes (as generated by the XML code generator).
     * <p>
     * @param packageName The package name for the (descriptor) classes
     * @throws ResolverException 
     *          If there's a problem loading class descriptors for the given package. 
     */
    public void addPackage(final String packageName) throws ResolverException { 
        _xmlClassDescriptorResolver.addPackage(packageName); 
    }

    /**
     * Loads class descriptors from the packages specified. The use of this method is useful
     * when no mapping is used, as happens when the domain classes hase been generated
     * using the XML code generator (in which case instead of a mapping file class
     * descriptor files will be generated).
     * <p>
     * Please note that this functionality will work only if you provide the <tt>.castor.cdr</tt>
     * files with your generated classes (as generated by the XML code generator).
     * <p>
     * @param packageNames The package names for the (descriptor) classes
     * @throws ResolverException 
     *          If there's a problem loading class descriptors for the given package. 
     */
    public void addPackages(final String[] packageNames) throws ResolverException {
        _xmlClassDescriptorResolver.addPackages(packageNames);
    }

    /**
     * Sets an application-specific {@link XMLClassDescriptorResolver} instance.
     * @param xmlClassDescriptorResolver the resolver to use
     */
    public void setResolver(final XMLClassDescriptorResolver xmlClassDescriptorResolver) {
        this._xmlClassDescriptorResolver = xmlClassDescriptorResolver;
    }

    /**
     * To set properties for marshalling and unmarshalling behavior. 
     * @param propertyName name of the property to set
     * @param value the value to set to
     */
    public void setProperty(final String propertyName, final Object value) {
        _configuration.put(propertyName, value);
    }

    /**
     * To get the value of a specific property.
     * @param propertyName name of the Property
     * @return the value (Object) of the property
     */
    public Object getProperty(final String propertyName) {
        return _configuration.getObject(propertyName);
    }

    /**
     * Returns the naming conventions to use for the XML framework.
     *
     * @return the naming conventions to use for the XML framework     
     */
    public XMLNaming getXMLNaming() {
        return getXMLNaming(null);
    }

    /**
     * Returns the naming conventions to use for the XML framework.
     * @param classLoader the class loader to be used when instantiating a new naming instance
     * @return the naming conventions to use for the XML framework
     * @TODO: Joachim should be synchronized, shouldn't it be??
     */
    public XMLNaming getXMLNaming(final ClassLoader classLoader) {
        
        if (_xmlNaming != null) {
            return _xmlNaming;
        }
        
        String prop = _configuration.getString(XMLConfiguration.XML_NAMING, null);
        if ((prop == null) || (prop.equalsIgnoreCase("lower"))) {
            _xmlNaming = new DefaultNaming();
        } else if (prop.equalsIgnoreCase("mixed")) {
            DefaultNaming dn = new DefaultNaming();
            dn.setStyle(DefaultNaming.MIXED_CASE_STYLE);
            _xmlNaming = dn;
        } else {
            try {
                Class cls = null;
                if (classLoader != null) {
                    cls = classLoader.loadClass(prop); 
                } else {
                    cls = Class.forName(prop);
                }
                _xmlNaming = (XMLNaming) cls.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load XMLNaming: " + e);
            }
        }
        return _xmlNaming;
    } //-- getXMLNaming

    /**
     * The {@link JavaNaming} instance to be used.
     * @return {@link JavaNaming} instance to be used.
     */
    public JavaNaming getJavaNaming() {
        return _javaNaming;
    }

    /**
     * Return an XML document parser implementing the feature list
     * specified in the configuration file.
     *
     * @return A suitable XML parser
     */
    public Parser getParser() {
        return getParser(null);
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
        Parser parser = null;
        Boolean validation = _configuration.getBoolean(XMLConfiguration.PARSER_VALIDATION);
        Boolean namespaces = _configuration.getBoolean(XMLConfiguration.NAMESPACES);
        String parserClassName = _configuration.getString(XMLConfiguration.PARSER);
        if ((parserClassName == null) || (parserClassName.length() == 0)) {
            SAXParser saxParser = XMLParserUtils.getSAXParser(validation.booleanValue(), namespaces.booleanValue());
            if (saxParser != null) {
                try {
                    parser = saxParser.getParser();
                } catch (SAXException e) {
                    LOG.error(Messages.format("conf.configurationError", e));
                }
            }
        }
        
        if (parser == null) {
            if ((parserClassName == null) 
                    || (parserClassName.length() == 0) 
                    || (parserClassName.equalsIgnoreCase("xerces"))) {
                parserClassName = "org.apache.xerces.parsers.SAXParser";
            }
            
            // if a parser class was specified, we try to create it
            parser = XMLParserUtils.instantiateParser(parserClassName);

            if (parser instanceof XMLReader) {
                XMLReader xmlReader = (XMLReader) parser;
                XMLParserUtils.setFeaturesOnXmlReader(
                        _configuration.getString(XMLConfiguration.PARSER_FEATURES, features),
                        _configuration.getString(XMLConfiguration.PARSER_FEATURES_DISABLED, ""),
                        validation.booleanValue(),
                        namespaces.booleanValue(),
                        xmlReader);
            }
        }
        return parser;
    }

    /**
     * Returns an XML document parser implementing the requested set of
     * features. The feature list is a comma separated list of features that
     * parser may or may not support. No errors are generated for unsupported
     * features. If the feature list is not null, it overrides the default
     * feature list specified in the configuration file, including validation
     * and Namespaces.
     * 
     * @return A suitable XML parser
     */
    public XMLReader getXMLReader() {
        return getXMLReader(null);
    } //-- getXMLReader
    
    /**
     * Returns an XML document parser implementing the requested
     * set of features. The feature list is a comma separated list
     * of features that parser may or may not support. No errors are
     * generated for unsupported features. If the feature list is not
     * null, it overrides the default feature list specified in the
     * configuration file, including validation and Namespaces.
     *
     * @param features the name of feature to set
     * @return A suitable XML parser
     */
    public XMLReader getXMLReader(final String features) {
        XMLReader reader = null;
        Boolean validation = _configuration.getBoolean(XMLConfiguration.PARSER_VALIDATION);
        Boolean namespaces = _configuration.getBoolean(XMLConfiguration.NAMESPACES);
        
        String readerClassName = _configuration.getString(XMLConfiguration.PARSER);
        
        if (readerClassName == null || readerClassName.length() == 0) {
            SAXParser saxParser = 
                XMLParserUtils.getSAXParser(validation.booleanValue(), namespaces.booleanValue());
            if (saxParser != null) {
                try {
                    reader = saxParser.getXMLReader();
                } catch (SAXException e) {
                    LOG.error(Messages.format("conf.configurationError", e));
                }
            }
        }
        
        if (reader == null) {
            if ((readerClassName == null) 
                    || (readerClassName.length() == 0) 
                    || (readerClassName.equalsIgnoreCase("xerces"))) {
                readerClassName = "org.apache.xerces.parsers.SAXParser";
            }
        

            reader = XMLParserUtils.instantiateXMLReader(readerClassName);
        }

        XMLParserUtils.setFeaturesOnXmlReader(
            _configuration.getString(XMLConfiguration.PARSER_FEATURES, features),
            _configuration.getString(XMLConfiguration.PARSER_FEATURES_DISABLED, ""),
            validation.booleanValue(), 
            namespaces.booleanValue(),
            reader);
        
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
        
        if (_primitiveNodeType != null) {
            return _primitiveNodeType;
        }
            
        String prop = _configuration.getString(XMLConfiguration.PRIMITIVE_NODE_TYPE, null);
        if (prop == null) {
            return null;
        }
        _primitiveNodeType = NodeType.getNodeType(prop);
        return _primitiveNodeType;
    } //-- getPrimitiveNodeType
    
    /**
     * Returns a new instance of the specified Regular Expression
     * Evaluator, or null if no validator was specified.
     *
     * @return the regular expression evaluator,
     *
    **/
    public RegExpEvaluator getRegExpEvaluator() {
        if (_regExpEvaluator != null) {
            return _regExpEvaluator;
        }
        
        String regExpEvalClassName = _configuration.getString(XMLConfiguration.REG_EXP_CLASS_NAME);
        if ((regExpEvalClassName == null) || (regExpEvalClassName.length() == 0)) {
            _regExpEvaluator = null;
        } else {
            try {
                Class regExpEvalClass = Class.forName(regExpEvalClassName);
                _regExpEvaluator = (RegExpEvaluator) regExpEvalClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(
                        Messages.format("conf.failedInstantiateRegExp", regExpEvalClassName, e));
            } catch (InstantiationException e) {
                throw new RuntimeException(
                        Messages.format("conf.failedInstantiateRegExp", regExpEvalClassName, e));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        Messages.format("conf.failedInstantiateRegExp", regExpEvalClassName, e));
            }
        }
        return _regExpEvaluator;
    } // -- getRegExpEvaluator

    /**
     * Returns a default serializer for producing an XML document. The caller
     * can specify an alternative output format, may reuse this serializer
     * across several streams, and may serialize both DOM and SAX events. If
     * such control is not required, it is recommended to call one of the other
     * two methods.
     * 
     * @return A suitable serializer
     */
    public Serializer getSerializer() {
        Serializer serializer = getSerializerFactory(
                _configuration.getString(
                        XMLConfiguration.SERIALIZER_FACTORY)).getSerializer();
        serializer.setOutputFormat(getOutputFormat());
        return serializer;
    }
    /**
     * Returns the default OutputFormat for use with a Serializer.
     *
     * @return the default OutputFormat
    **/
    public OutputFormat getOutputFormat() {

        boolean indent = _configuration.getBoolean(XMLConfiguration.USE_INDENTATION, false);

        OutputFormat format = getSerializerFactory(
                _configuration.getString(
                        XMLConfiguration.SERIALIZER_FACTORY))
                        .getOutputFormat();
        format.setMethod(OutputFormat.XML);
        format.setIndenting(indent);
        
        // There is a bad interaction between the indentation and the
        // setPreserveSpace option. The indentated output is strangely indented.
        if (!indent) {
            format.setPreserveSpace(true);
        } 

        return format;
    } //-- getOutputFormat
    
    /**
     * Returns the currently configured XMLSerializerFactory instance.
     * @param serializerFactoryName the class name of the serializer factory
     * @return XMLSerializerFactory to use by Castor
     */
    protected XMLSerializerFactory getSerializerFactory(final String serializerFactoryName) {
        XMLSerializerFactory serializerFactory;
        
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

    /**
     * Returns a default serializer for producing an XML document to
     * the designated output stream using the default serialization
     * format.
     *
     * @param output The output stream
     * @return A suitable serializer
     * @throws IOException if instantiation of the serializer fails
     */
    public DocumentHandler getSerializer(final OutputStream output) throws IOException {
        Serializer serializer;
        DocumentHandler docHandler;

        serializer = getSerializer();
        serializer.setOutputByteStream(output);
        docHandler = serializer.asDocumentHandler();
        if (docHandler == null) {
            throw new RuntimeException(Messages.format("conf.serializerNotSaxCapable", serializer
                    .getClass().getName()));
        }
        return docHandler;
    }


    /**
     * Returns a default serializer for producing an XML document to the
     * designated output stream using the default serialization format.
     * 
     * @param output
     *            The output stream
     * @return A suitable serializer
     * @throws IOException if instantiation of serializer fails
     */
    public DocumentHandler getSerializer(final Writer output) throws IOException {
        Serializer serializer;
        DocumentHandler docHandler;

        serializer = getSerializer();
        serializer.setOutputCharStream(output);
        docHandler = serializer.asDocumentHandler();
        if (docHandler == null) {
            throw new RuntimeException(Messages.format("conf.serializerNotSaxCapable", serializer
                    .getClass().getName()));
        }
        return docHandler;
    }

    /**
     * To get the XMLClassdescriptorResolver instance hold in the context.
     * @return the XMLClassdescriptorResolver instance hold in the context
     */
    public XMLClassDescriptorResolver getXMLClassDescriptorResolver() {
        return _xmlClassDescriptorResolver;
    }

    /**
     * To get the Introspector assigned to this XMLContext.
     * @return the Introspector assigned to this XMLContext
     */
    public Introspector getIntrospector() {
        return _introspector;
    }

    /**
     * To set the {@link Resolver} for Schema*. 
     * @param schemaResolver the {@link Resolver} for Schema*
     */
    public void setSchemaResolver(final Resolver schemaResolver) {
        _schemaResolver = schemaResolver;
    }

    /**
     * To get the {@link Resolver} to use in Schema*.
     * @return get the {@link Resolver} to use in Schema*
     */
    public Resolver getSchemaResolver() {
        return _schemaResolver;
    }

    /**
     * To get the XMLClassDescriptor resolver strategy to be used when
     * resolving classes into class descriptors.
     * @return the ResolverStrategy to use
     */
    public ResolverStrategy getResolverStrategy() {
        return _resolverStrategy;
    }

    /**
     * To set the XMLClassDescriptor resolver strategy to be used.
     * @param resolverStrategy the ResolverStrategy to use
     */
    public void setResolverStrategy(final ResolverStrategy resolverStrategy) {
        _resolverStrategy = resolverStrategy;
    }

    /**
     * To set the {@link MappingLoader} to be used in this Castor session.
     * @param mappingLoader the {@link MappingLoader} to use
     */
    public void setMappingLoader(final MappingLoader mappingLoader) {
        _mappingLoader = mappingLoader;
    }

    /**
     * To get the {@link MappingLoader} specified to be used in this Castor session.
     * @return the {@link MappingLoader} to use
     */
    public MappingLoader getMappingLoader() {
        return _mappingLoader;
    }

    /**
     * To set the {@link JavaNaming}?property.
     * @param javaNaming the {@link JavaNaming} to use
     */
    public void setJavaNaming(final JavaNaming javaNaming) {
        _javaNaming = javaNaming;
    }

    /**
     * To set any boolean property.
     * @param propertyName name of the property to set
     * @param value boolean value to set
     */
    public void setProperty(final String propertyName, final boolean value) {
        _configuration.put(propertyName, new Boolean(value));
    }
    
    /**
     * Providing access to Boolean properties of the configuration.
     * @param propertyName name of the property
     * @return null if property is not set or whichever value is set
     */
    public Boolean getBooleanProperty(final String propertyName) {
        return _configuration.getBoolean(propertyName);
    }

    /**
     * Providing access to String properties of the configuration.
     * @param propertyName name of the property
     * @return null if the property is not set or whichever value is set
     */
    public String getStringProperty(final String propertyName) {
        return _configuration.getString(propertyName);
    }

    /**
     * To set the class loader to be used in all further marshalling, unmarshalling
     * and other actions.
     * @param classLoader the ClassLoader instance to use
     */
    public void setClassLoader(final ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    /**
     * To set the {@link XMLClassDescriptorResolver} to be used. Be aware, that the
     * XMLClassDescriptorResolver instance holds a descriptor cache!! Maybe change it
     * to have the descriptor cache as part of the context?
     * @param xmlClassDescriptorResolver the {@link XMLClassDescriptorResolver} to use
     */
    public void setXMLClassDescriptorResolver(
            final XMLClassDescriptorResolver xmlClassDescriptorResolver) {
        _xmlClassDescriptorResolver = xmlClassDescriptorResolver;
    }

    /**
     * To specify which {@link Introspector}?is to be used.
     * @param introspector {@link Introspector} to be used
     */
    public void setIntrospector(final Introspector introspector) {
        _introspector = introspector;
    }

    /**
     * To get the ClassLoader to use for loading resources.
     * @return the ClassLoader to use
     */
    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    /**
     * Get lenient id validation flag.
     * @return lenient id validation flag
     */
    public boolean getLenientIdValidation() {
        Boolean lenientIdValidation = 
            _configuration.getBoolean(XMLConfiguration.LENIENT_ID_VALIDATION);
        if (lenientIdValidation == null) {
            String message = "Property lenientIdValidation must not be null";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        return lenientIdValidation.booleanValue();
    }

    /**
     * Get lenient sequence order flag.
     * @return lenient sequence order flag
     */
    public boolean getLenientSequenceOrder() {
        Boolean lenientSequenceOrder = 
            _configuration.getBoolean(XMLConfiguration.LENIENT_SEQUENCE_ORDER);
        if (lenientSequenceOrder == null) {
            String message = "Property lenientSequenceOrder must not be null";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        return lenientSequenceOrder.booleanValue();
    }

    /**
     * Get load package mapping flag.
     * @return load package mapping flag
     */
    public Boolean getLoadPackageMapping() {
        return _configuration.getBoolean(XMLConfiguration.LOAD_PACKAGE_MAPPING);
    }
    
    /**
     * To set the load package mapping flag.
     * @param loadPackageMapping the load package mapping flag
     */
    public void setLoadPackageMapping(final Boolean loadPackageMapping) {
        _configuration.put(XMLConfiguration.LOAD_PACKAGE_MAPPING, loadPackageMapping);
    }

    /**
     * To get use-introspection flag.
     * @return use-introspection flag
     */
    public Boolean getUseIntrospector() {
        return _configuration.getBoolean(XMLConfiguration.USE_INTROSPECTION);
    }

    /**
     * To set use-introspection flag.
     * @param useIntrospector use-introspection flag
     */
    public void setUseIntrospector(final Boolean useIntrospector) {
        _configuration.put(XMLConfiguration.USE_INTROSPECTION, useIntrospector);
    }

    /**
     * To get marshalling-validation flag.
     * @return marshalling-validation flag
     */
    public boolean marshallingValidation() {
        Boolean marshallingValidation = 
            _configuration.getBoolean(XMLConfiguration.MARSHALLING_VALIDATION);
        if (marshallingValidation == null) {
            String message = "Property marshallingValidation must not be null";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        return marshallingValidation.booleanValue();
    }

    /**
     * To get strict-element flag.
     * @return strict-element flag
     */
    public boolean strictElements() {
        Boolean strictElements = 
            _configuration.getBoolean(XMLConfiguration.STRICT_ELEMENTS);
        if (strictElements == null) {
            String message = "Property strictElements must not be null";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        return strictElements.booleanValue();
    }

}

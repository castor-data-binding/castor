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

import javax.xml.parsers.SAXParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.castor.util.Messages;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.util.RegExpEvaluator;
import org.exolab.castor.xml.AbstractXMLNaming;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.OutputFormat;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.xml.XMLSerializerFactory;
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
public abstract class AbstractInternalContext implements InternalContext {
    /** Logger to be used. */
    private static final Log LOG = LogFactory.getFactory().getInstance(AbstractInternalContext.class);

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
     * The XMLClassDescriptor resolver strategy to use.
     */
    private ResolverStrategy _resolverStrategy;

    /**
     * The {@link MappingLoader} to use.
     */
    private MappingLoader _mappingLoader;
    
    /**
     * The {@link AbstractXMLNaming} to be used.
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
     * Creates an instance of {@link AbstractInternalContext}. The internal context is meant to
     * hold the configuration and state informations, but not necessarily retrieving
     * those values...
     */
    public AbstractInternalContext() {
        _configuration = XMLConfiguration.newInstance();
        _javaNaming = new JavaNamingImpl();
    }
    
    /**
     * @see org.castor.xml.InternalContext#addMapping(org.exolab.castor.mapping.Mapping)
     */
    public void addMapping(final Mapping mapping) throws MappingException {
         MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
         MappingLoader mappingLoader = 
             mappingUnmarshaller.getMappingLoader(mapping, BindingType.XML);
         _xmlClassDescriptorResolver.setMappingLoader(mappingLoader);        
    }

    /**
     * @see org.castor.xml.InternalContext#addClass(java.lang.Class)
     */ 
    public void addClass(final Class clazz) throws ResolverException { 
        _xmlClassDescriptorResolver.addClass(clazz);
    }

    /**
     * @see org.castor.xml.InternalContext#addClasses(java.lang.Class[])
     */ 
    public void addClasses(final Class[] clazzes) throws ResolverException {
        _xmlClassDescriptorResolver.addClasses(clazzes);
    }

    /**
     * @see org.castor.xml.InternalContext#addPackage(java.lang.String)
     */
    public void addPackage(final String packageName) throws ResolverException { 
        _xmlClassDescriptorResolver.addPackage(packageName); 
    }

    /**
     * @see org.castor.xml.InternalContext#addPackages(java.lang.String[])
     */
    public void addPackages(final String[] packageNames) throws ResolverException {
        _xmlClassDescriptorResolver.addPackages(packageNames);
    }

    /**
     * @see org.castor.xml.InternalContext#setResolver(org.exolab.castor.xml.XMLClassDescriptorResolver)
     */
    public void setResolver(final XMLClassDescriptorResolver xmlClassDescriptorResolver) {
        this._xmlClassDescriptorResolver = xmlClassDescriptorResolver;
    }

    /**
     * @see org.castor.xml.InternalContext#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(final String propertyName, final Object value) {
        _configuration.put(propertyName, value);
    }

    /**
     * @see org.castor.xml.InternalContext#getProperty(java.lang.String)
     */
    public Object getProperty(final String propertyName) {
        return _configuration.getObject(propertyName);
    }

    /**
     * @see org.castor.xml.InternalContext#getXMLNaming()
     */
    public XMLNaming getXMLNaming() {
        return getXMLNaming(null);
    }

    /**
     * @see org.castor.xml.InternalContext#getXMLNaming(java.lang.ClassLoader)
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
     * @see org.castor.xml.InternalContext#getJavaNaming()
     */
    public JavaNaming getJavaNaming() {
        return _javaNaming;
    }

    /**
     * @see org.castor.xml.InternalContext#getParser()
     */
    public Parser getParser() {
        return getParser(null);
    }

    /**
     * @see org.castor.xml.InternalContext#getParser(java.lang.String)
     */
    public Parser getParser(final String features) {
        return XMLParserUtils.getParser(_configuration, features);
    }

    /**
     * @see org.castor.xml.InternalContext#getXMLReader()
     */
    public XMLReader getXMLReader() {
        return getXMLReader(null);
    }
    
    /**
     * @see org.castor.xml.InternalContext#getXMLReader(java.lang.String)
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
     * @see org.castor.xml.InternalContext#getPrimitiveNodeType()
     */
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
     * @see org.castor.xml.InternalContext#getRegExpEvaluator()
     */
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
     * @see org.castor.xml.InternalContext#getSerializer()
     */
    public Serializer getSerializer() {
        return XMLParserUtils.getSerializer(_configuration);
    }
    /**
     * @see org.castor.xml.InternalContext#getOutputFormat()
     */
    public OutputFormat getOutputFormat() {
        return XMLParserUtils.getOutputFormat(_configuration);
    }
    
    /**
     * Returns the currently configured XMLSerializerFactory instance.
     * @param serializerFactoryName the class name of the serializer factory
     * @return XMLSerializerFactory to use by Castor
     */
    protected XMLSerializerFactory getSerializerFactory(final String serializerFactoryName) {
        return XMLParserUtils.getSerializerFactory(serializerFactoryName);
    }

    /**
     * @see org.castor.xml.InternalContext#getSerializer(java.io.OutputStream)
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
     * @see org.castor.xml.InternalContext#getSerializer(java.io.Writer)
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
     * @see org.castor.xml.InternalContext#getXMLClassDescriptorResolver()
     */
    public XMLClassDescriptorResolver getXMLClassDescriptorResolver() {
        return _xmlClassDescriptorResolver;
    }

    /**
     * @see org.castor.xml.InternalContext#getIntrospector()
     */
    public Introspector getIntrospector() {
        return _introspector;
    }

    /**
     * @see org.castor.xml.InternalContext#getResolverStrategy()
     */
    public ResolverStrategy getResolverStrategy() {
        return _resolverStrategy;
    }

    /**
     * @see org.castor.xml.InternalContext#setResolverStrategy(org.exolab.castor.xml.util.ResolverStrategy)
     */
    public void setResolverStrategy(final ResolverStrategy resolverStrategy) {
        _resolverStrategy = resolverStrategy;
    }

    /**
     * @see org.castor.xml.InternalContext#setMappingLoader(org.exolab.castor.mapping.MappingLoader)
     */
    public void setMappingLoader(final MappingLoader mappingLoader) {
        _mappingLoader = mappingLoader;
    }

    /**
     * @see org.castor.xml.InternalContext#getMappingLoader()
     */
    public MappingLoader getMappingLoader() {
        return _mappingLoader;
    }

    /**
     * @see org.castor.xml.InternalContext#setJavaNaming(org.castor.xml.JavaNaming)
     */
    public void setJavaNaming(final JavaNaming javaNaming) {
        _javaNaming = javaNaming;
    }

    /**
     * @see org.castor.xml.InternalContext#setXMLNaming(org.castor.xml.XMLNaming)
     */
    public void setXMLNaming(final XMLNaming xmlNaming) {
        _xmlNaming = xmlNaming;
    }

    /**
     * @see org.castor.xml.InternalContext#setProperty(java.lang.String, boolean)
     */
    public void setProperty(final String propertyName, final boolean value) {
        _configuration.put(propertyName, Boolean.valueOf(value));
    }
    
    /**
     * @see org.castor.xml.InternalContext#getBooleanProperty(java.lang.String)
     */
    public Boolean getBooleanProperty(final String propertyName) {
        return _configuration.getBoolean(propertyName);
    }

    /**
     * @see org.castor.xml.InternalContext#getStringProperty(java.lang.String)
     */
    public String getStringProperty(final String propertyName) {
        return _configuration.getString(propertyName);
    }

    /**
     * @see org.castor.xml.InternalContext#setClassLoader(java.lang.ClassLoader)
     */
    public void setClassLoader(final ClassLoader classLoader) {
        _classLoader = classLoader;
        if (_xmlClassDescriptorResolver != null) {
            _xmlClassDescriptorResolver.setClassLoader(classLoader);
        }
    }

    /**
     * @see org.castor.xml.InternalContext#setXMLClassDescriptorResolver(org.exolab.castor.xml.XMLClassDescriptorResolver)
     */
    public void setXMLClassDescriptorResolver(
            final XMLClassDescriptorResolver xmlClassDescriptorResolver) {
        _xmlClassDescriptorResolver = xmlClassDescriptorResolver;
    }

    /**
     * @see org.castor.xml.InternalContext#setIntrospector(org.exolab.castor.xml.Introspector)
     */
    public void setIntrospector(final Introspector introspector) {
        _introspector = introspector;
    }

    /**
     * @see org.castor.xml.InternalContext#getClassLoader()
     */
    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    /**
     * @see org.castor.xml.InternalContext#getLenientIdValidation()
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
     * @see org.castor.xml.InternalContext#getLenientSequenceOrder()
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
     * @see org.castor.xml.InternalContext#getLoadPackageMapping()
     */
    public Boolean getLoadPackageMapping() {
        return _configuration.getBoolean(XMLConfiguration.LOAD_PACKAGE_MAPPING);
    }
    
    /**
     * @see org.castor.xml.InternalContext#setLoadPackageMapping(java.lang.Boolean)
     */
    public void setLoadPackageMapping(final Boolean loadPackageMapping) {
        _configuration.put(XMLConfiguration.LOAD_PACKAGE_MAPPING, loadPackageMapping);
    }

    /**
     * @see org.castor.xml.InternalContext#getUseIntrospector()
     */
    public Boolean getUseIntrospector() {
        return _configuration.getBoolean(XMLConfiguration.USE_INTROSPECTION);
    }

    /**
     * @see org.castor.xml.InternalContext#setUseIntrospector(java.lang.Boolean)
     */
    public void setUseIntrospector(final Boolean useIntrospector) {
        _configuration.put(XMLConfiguration.USE_INTROSPECTION, useIntrospector);
    }

    /**
     * @see org.castor.xml.InternalContext#marshallingValidation()
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
     * @see org.castor.xml.InternalContext#strictElements()
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

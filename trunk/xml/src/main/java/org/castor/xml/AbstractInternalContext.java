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
import org.castor.core.util.Messages;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
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

    /** The properties to use internally to provide parser, serializer, ... */
    private org.castor.core.util.AbstractProperties _properties;
    
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
        _properties = XMLProperties.newInstance();
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
     * {@inheritDoc}
     * @see org.castor.xml.InternalContext#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(final String propertyName, final Object value) {
        // resetting all values that are only reinitialized if null
        if (propertyName == null) {
            IllegalArgumentException iae = new IllegalArgumentException("setProperty must not be called with a propertyName == null");
            LOG.warn(iae.getMessage());
            throw iae;
        }
        if (propertyName.equals(XMLProperties.XML_NAMING)) {
            if (value instanceof String) {
                setXMLNaming((String)value);
            } else if (value instanceof XMLNaming) {
                setXMLNaming((XMLNaming) value);
            } else {
                IllegalArgumentException iae = new IllegalArgumentException("XML Naming can only be set to a String or an implementation of XMLNaming");
                LOG.warn(iae.getMessage());
                throw iae;
            }
        }
        if (propertyName.equals(XMLProperties.JAVA_NAMING)) {
            if (value instanceof String) {
                setJavaNaming((String)value);
            } else if (value instanceof JavaNaming) {
                setJavaNaming((JavaNaming) value);
            } else {
                IllegalArgumentException iae = new IllegalArgumentException("Java Naming can only be set to a String or an implementation of JavaNaming");
                LOG.warn(iae.getMessage());
                throw iae;
            }
        }
        _primitiveNodeType = null;
        _regExpEvaluator = null;
        // now writing the new property
        _properties.put(propertyName, value);
    }

    /**
     * @see org.castor.xml.InternalContext#getProperty(java.lang.String)
     */
    public Object getProperty(final String propertyName) {
        return _properties.getObject(propertyName);
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.InternalContext#getXMLNaming()
     */
    public XMLNaming getXMLNaming() {
        if (_xmlNaming != null) {
            return _xmlNaming;
        }
        
        String prop = _properties.getString(XMLProperties.XML_NAMING, null);
        setXMLNaming(prop);
        return _xmlNaming;
    }

    /**
     * {@inheritDoc}
     * @see org.castor.xml.InternalContext#getXMLNaming(java.lang.ClassLoader)
     * @deprecated Makes no sence!
     */
    public XMLNaming getXMLNaming(final ClassLoader classLoader) {
        return getXMLNaming();
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
        return XMLParserUtils.getParser(_properties, features);
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
        Boolean validation = _properties.getBoolean(XMLProperties.PARSER_VALIDATION);
        Boolean namespaces = _properties.getBoolean(XMLProperties.NAMESPACES);
        
        String readerClassName = _properties.getString(XMLProperties.PARSER);
        
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
            _properties.getString(XMLProperties.PARSER_FEATURES, features),
            _properties.getString(XMLProperties.PARSER_FEATURES_DISABLED, ""),
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
            
        String prop = _properties.getString(XMLProperties.PRIMITIVE_NODE_TYPE, null);
        if (prop == null) {
            return null;
        }
        _primitiveNodeType = NodeType.getNodeType(prop);
        return _primitiveNodeType;
    } //-- getPrimitiveNodeType
    
    /**
     * {@inheritDoc}
     * @see org.castor.xml.InternalContext#getRegExpEvaluator()
     */
    public RegExpEvaluator getRegExpEvaluator() {
        if (_regExpEvaluator != null) {
            return _regExpEvaluator;
        }
        
        String className = 
            _properties.getString(XMLProperties.REG_EXP_CLASS_NAME, "");
        if (className.length() == 0) {
            _regExpEvaluator = null;
        } else {
            try {
                Class<?> regExpEvalClass = Class.forName(className);
                _regExpEvaluator = (RegExpEvaluator) regExpEvalClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(
                        Messages.format("conf.failedInstantiateRegExp", className, e));
            } catch (InstantiationException e) {
                throw new RuntimeException(
                        Messages.format("conf.failedInstantiateRegExp", className, e));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        Messages.format("conf.failedInstantiateRegExp", className, e));
            }
        }
        return _regExpEvaluator;
    } // -- getRegExpEvaluator

    /**
     * @see org.castor.xml.InternalContext#getSerializer()
     */
    public Serializer getSerializer() {
        return XMLParserUtils.getSerializer(_properties);
    }
    /**
     * @see org.castor.xml.InternalContext#getOutputFormat()
     */
    public OutputFormat getOutputFormat() {
        return XMLParserUtils.getOutputFormat(_properties);
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

    public void setJavaNaming(final String javaNamingProperty) {
        if (javaNamingProperty == null || javaNamingProperty.length() == 0) {
            _javaNaming = new JavaNamingImpl();
        } else {
            try {
                Class<JavaNaming> cls = (Class<JavaNaming>) Class.forName(javaNamingProperty);
                _javaNaming = cls.newInstance();
            } catch (Exception e) {
                IllegalArgumentException iae = new IllegalArgumentException(
                        "Failed to load JavaNaming: " + e);
                LOG.warn(iae.getMessage());
                throw iae;
            }
        }
    }

    /**
     * @see org.castor.xml.InternalContext#setXMLNaming(org.castor.xml.XMLNaming)
     */
    public void setXMLNaming(final XMLNaming xmlNaming) {
        _xmlNaming = xmlNaming;
        // propagate to e.g. Introspector also!!
        if (_introspector != null) {
            _introspector.setNaming(_xmlNaming);
        }
    }

    /**
     * This XMLNaming setter is meant to be used when working in property style
     * instead of setting an XMLNaming implementation.
     * @param xmlNamingProperty to set the XMLNaming property as read from configuration
     */
    public void setXMLNaming(final String xmlNamingProperty) {
        if ((xmlNamingProperty == null) || (xmlNamingProperty.equalsIgnoreCase("lower"))) {
            setXMLNaming(new DefaultNaming());
        } else if (xmlNamingProperty.equalsIgnoreCase("mixed")) {
            DefaultNaming dn = new DefaultNaming();
            dn.setStyle(DefaultNaming.MIXED_CASE_STYLE);
            setXMLNaming(dn);
        } else {
            try {
                Class<XMLNaming> cls = (Class<XMLNaming>) Class.forName(xmlNamingProperty);
                setXMLNaming(cls.newInstance());
            } catch (Exception e) {
                IllegalArgumentException iae = new IllegalArgumentException(
                        "Failed to load XMLNaming: " + e);
                LOG.warn(iae.getMessage());
                throw iae;
            }
        }
        if (_xmlNaming == null) {
            IllegalArgumentException iae = new IllegalArgumentException(
                    "Failed to correctly set XMLNaming; property was: " + xmlNamingProperty);
            LOG.warn(iae.getMessage());
            throw iae;
        }
    }

    /**
     * @see org.castor.xml.InternalContext#setProperty(java.lang.String, boolean)
     */
    public void setProperty(final String propertyName, final boolean value) {
        _properties.put(propertyName, Boolean.valueOf(value));
    }
    
    /**
     * @see org.castor.xml.InternalContext#getBooleanProperty(java.lang.String)
     */
    public Boolean getBooleanProperty(final String propertyName) {
        return _properties.getBoolean(propertyName);
    }

    /**
     * @see org.castor.xml.InternalContext#getStringProperty(java.lang.String)
     */
    public String getStringProperty(final String propertyName) {
        return _properties.getString(propertyName);
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
            _properties.getBoolean(XMLProperties.LENIENT_ID_VALIDATION);
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
            _properties.getBoolean(XMLProperties.LENIENT_SEQUENCE_ORDER);
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
        return _properties.getBoolean(XMLProperties.LOAD_PACKAGE_MAPPING);
    }
    
    /**
     * @see org.castor.xml.InternalContext#setLoadPackageMapping(java.lang.Boolean)
     */
    public void setLoadPackageMapping(final Boolean loadPackageMapping) {
        _properties.put(XMLProperties.LOAD_PACKAGE_MAPPING, loadPackageMapping);
    }

    /**
     * @see org.castor.xml.InternalContext#getUseIntrospector()
     */
    public Boolean getUseIntrospector() {
        return _properties.getBoolean(XMLProperties.USE_INTROSPECTION);
    }

    /**
     * @see org.castor.xml.InternalContext#setUseIntrospector(java.lang.Boolean)
     */
    public void setUseIntrospector(final Boolean useIntrospector) {
        _properties.put(XMLProperties.USE_INTROSPECTION, useIntrospector);
    }

    /**
     * @see org.castor.xml.InternalContext#marshallingValidation()
     */
    public boolean marshallingValidation() {
        Boolean marshallingValidation = 
            _properties.getBoolean(XMLProperties.MARSHALLING_VALIDATION);
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
            _properties.getBoolean(XMLProperties.STRICT_ELEMENTS);
        if (strictElements == null) {
            String message = "Property strictElements must not be null";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        return strictElements.booleanValue();
    }

}

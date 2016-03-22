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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import javax.inject.Inject;
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
import org.exolab.castor.xml.XMLClassDescriptor;
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
 * initial data and put into all other parts of Castor by {@link XMLContext}. It
 * is NOT meant to be directly instantiated by user implementations! For all
 * other objects it provides access to Castor state information (e.g. known
 * descriptors) and configuration values.
 * 
 * @author <a href="mailto:jgrueneis At gmail DOT com">Joachim Grueneis</a>
 * @since 1.1.2
 */
public abstract class AbstractInternalContext implements InternalContext {

   private static final Log LOG = LogFactory.getFactory().getInstance(AbstractInternalContext.class);
   
   private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

   /** 
    * The properties to use internally to provide parser, serializer, ... 
    */
   private org.castor.core.util.AbstractProperties _properties;

   /**
    * {@link XMLClassDescriptorResolver} instance used for caching XML-related
    * class descriptors.
    */
   private XMLClassDescriptorResolver _xmlClassDescriptorResolver;

   /**
    * The XMLContext knows the one {@link Introspector} to be used.
    */
   private Introspector _introspector;

   /**
    * The {@link XMLClassDescriptor} resolver strategy to use.
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
   @Inject
   private JavaNaming _javaNaming;

   /**
    * The {@link ClassLoader} to use.
    */
   private ClassLoader _classLoader;

   /**
    * The {@link NodeType} to use for primitives.
    */
   private NodeType _primitiveNodeType;

   /**
    * The {@link RegExpevaluator} to use.
    */
   private RegExpEvaluator _regExpEvaluator;

   public AbstractInternalContext() {
      _properties = XMLProperties.newInstance();
      // TODO[WG]: remove once injection works
      _javaNaming = new JavaNamingImpl(this);
   }

   @Override
   public void addMapping(final Mapping mapping) throws MappingException {
      MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
      MappingLoader mappingLoader = mappingUnmarshaller.getMappingLoader(mapping, BindingType.XML);
      _xmlClassDescriptorResolver.setMappingLoader(mappingLoader);
   }

   @Override
   public void addClass(final Class<?> clazz) throws ResolverException {
      _xmlClassDescriptorResolver.addClass(clazz);
   }

   @Override
   public void addClasses(final Class<?>[] clazzes) throws ResolverException {
      _xmlClassDescriptorResolver.addClasses(clazzes);
   }

   @Override
   public void addPackage(final String packageName) throws ResolverException {
      _xmlClassDescriptorResolver.addPackage(packageName);
   }

   @Override
   public void addPackages(final String[] packageNames) throws ResolverException {
      _xmlClassDescriptorResolver.addPackages(packageNames);
   }

   @Override
   public void setResolver(final XMLClassDescriptorResolver xmlClassDescriptorResolver) {
      this._xmlClassDescriptorResolver = xmlClassDescriptorResolver;
   }

   @Override
   public void setProperty(final String propertyName, final Object value) {
      // resetting all values that are only reinitialized if null
      if (propertyName == null) {
         IllegalArgumentException iae = new IllegalArgumentException(
               "setProperty must not be called with a propertyName == null");
         LOG.warn(iae.getMessage());
         throw iae;
      }
      if (propertyName.equals(XMLProperties.XML_NAMING)) {
         if (value instanceof String) {
            setXMLNaming((String) value);
         } else if (value instanceof XMLNaming) {
            setXMLNaming((XMLNaming) value);
         } else {
            IllegalArgumentException iae = new IllegalArgumentException(
                  "XML Naming can only be set to a String or an implementation of XMLNaming");
            LOG.warn(iae.getMessage());
            throw iae;
         }
      }
      if (propertyName.equals(XMLProperties.JAVA_NAMING)) {
         if (value instanceof String) {
            setJavaNaming((String) value);
         } else if (value instanceof JavaNaming) {
            setJavaNaming((JavaNaming) value);
         } else {
            IllegalArgumentException iae = new IllegalArgumentException(
                  "Java Naming can only be set to a String or an implementation of JavaNaming");
            LOG.warn(iae.getMessage());
            throw iae;
         }
      }
      _primitiveNodeType = null;
      _regExpEvaluator = null;

      // now writing the new property
      this.setPropertyInternal(propertyName, value);
   }

   @Override
   public Object getProperty(final String propertyName) {
      return _properties.getObject(propertyName);
   }

   @Override
   public XMLNaming getXMLNaming() {
      if (_xmlNaming != null) {
         return _xmlNaming;
      }

      String prop = _properties.getString(XMLProperties.XML_NAMING, null);
      setXMLNaming(prop);
      return _xmlNaming;
   }

   /**
    * @deprecated Makes no sence!
    */
   @Override
   public XMLNaming getXMLNaming(final ClassLoader classLoader) {
      return getXMLNaming();
   } // -- getXMLNaming

   @Override
   public JavaNaming getJavaNaming() {
      return _javaNaming;
   }

   @Override
   public Parser getParser() {
      return getParser(null);
   }

   @Override
   public Parser getParser(final String features) {
      return XMLParserUtils.getParser(_properties, features);
   }

   @Override
   public XMLReader getXMLReader() {
      return getXMLReader(null);
   }

   @Override
   public XMLReader getXMLReader(final String features) {
      XMLReader reader = null;
      Boolean validation = _properties.getBoolean(XMLProperties.PARSER_VALIDATION);
      Boolean namespaces = _properties.getBoolean(XMLProperties.NAMESPACES);

      String readerClassName = _properties.getString(XMLProperties.PARSER);

      if (readerClassName == null || readerClassName.length() == 0) {
         SAXParser saxParser = XMLParserUtils.getSAXParser(validation.booleanValue(),
               namespaces.booleanValue());
         if (saxParser != null) {
            try {
               reader = saxParser.getXMLReader();
            } catch (SAXException e) {
               LOG.error(Messages.format("conf.configurationError", e));
            }
         }
      }

      if (reader == null) {
         if ((readerClassName == null) || (readerClassName.length() == 0)
               || (readerClassName.equalsIgnoreCase("xerces"))) {
            readerClassName = "org.apache.xerces.parsers.SAXParser";
         }

         reader = XMLParserUtils.instantiateXMLReader(readerClassName);
      }

      XMLParserUtils.setFeaturesOnXmlReader(_properties.getString(XMLProperties.PARSER_FEATURES, features),
            _properties.getString(XMLProperties.PARSER_FEATURES_DISABLED, ""), validation.booleanValue(),
            namespaces.booleanValue(), reader);

      return reader;

   }

   @Override
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
   }

   @Override
   public RegExpEvaluator getRegExpEvaluator() {
      if (_regExpEvaluator != null) {
         return _regExpEvaluator;
      }

      String className = _properties.getString(XMLProperties.REG_EXP_CLASS_NAME, "");
      if (className.length() == 0) {
         _regExpEvaluator = null;
      } else {
         try {
            Class<?> regExpEvalClass = Class.forName(className);
            _regExpEvaluator = (RegExpEvaluator) regExpEvalClass.newInstance();
         } catch (ClassNotFoundException e) {
            throw new RuntimeException(Messages.format("conf.failedInstantiateRegExp", className, e));
         } catch (InstantiationException e) {
            throw new RuntimeException(Messages.format("conf.failedInstantiateRegExp", className, e));
         } catch (IllegalAccessException e) {
            throw new RuntimeException(Messages.format("conf.failedInstantiateRegExp", className, e));
         }
      }
      return _regExpEvaluator;
   }

   @Override
   public Serializer getSerializer() {
      return XMLParserUtils.getSerializer(_properties);
   }

   @Override
   public OutputFormat getOutputFormat() {
      return XMLParserUtils.getOutputFormat(_properties);
   }

   /**
    * Returns the currently configured XMLSerializerFactory instance.
    * 
    * @param serializerFactoryName
    *           the class name of the serializer factory
    * @return XMLSerializerFactory to use by Castor
    */
   protected XMLSerializerFactory getSerializerFactory(final String serializerFactoryName) {
      return XMLParserUtils.getSerializerFactory(serializerFactoryName);
   }

   @Override
   public DocumentHandler getSerializer(final OutputStream output) throws IOException {
      Serializer serializer;
      DocumentHandler docHandler;

      serializer = getSerializer();
      serializer.setOutputByteStream(output);
      docHandler = serializer.asDocumentHandler();
      if (docHandler == null) {
         throw new RuntimeException(Messages.format("conf.serializerNotSaxCapable", serializer.getClass()
               .getName()));
      }
      return docHandler;
   }

   @Override
   public DocumentHandler getSerializer(final Writer output) throws IOException {
      Serializer serializer;
      DocumentHandler docHandler;

      serializer = getSerializer();
      serializer.setOutputCharStream(output);
      docHandler = serializer.asDocumentHandler();
      if (docHandler == null) {
         throw new RuntimeException(Messages.format("conf.serializerNotSaxCapable", serializer.getClass()
               .getName()));
      }
      return docHandler;
   }

   @Override
   public XMLClassDescriptorResolver getXMLClassDescriptorResolver() {
      return _xmlClassDescriptorResolver;
   }

   @Override
   public Introspector getIntrospector() {
      return _introspector;
   }

   @Override
   public ResolverStrategy getResolverStrategy() {
      return _resolverStrategy;
   }

   @Override
   public void setResolverStrategy(final ResolverStrategy resolverStrategy) {
      _resolverStrategy = resolverStrategy;
   }

   @Override
   public void setMappingLoader(final MappingLoader mappingLoader) {
      _mappingLoader = mappingLoader;
   }

   @Override
   public MappingLoader getMappingLoader() {
      return _mappingLoader;
   }

   @Override
   public void setJavaNaming(final JavaNaming javaNaming) {
      _javaNaming = javaNaming;
   }

   public void setJavaNaming(final String javaNamingProperty) {
      if (javaNamingProperty == null || javaNamingProperty.length() == 0) {
         _javaNaming = new JavaNamingImpl(this);
      } else {
         try {
            Class<JavaNaming> cls = (Class<JavaNaming>) Class.forName(javaNamingProperty);
            _javaNaming = cls.newInstance();
         } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException("Failed to load JavaNaming: " + e);
            LOG.warn(iae.getMessage());
            throw iae;
         }
      }
   }

   @Override
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
    * 
    * @param xmlNamingProperty
    *           to set the XMLNaming property as read from configuration
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
            IllegalArgumentException iae = new IllegalArgumentException("Failed to load XMLNaming: " + e);
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

   @Override
   public void setProperty(final String propertyName, final boolean value) {
      this.setPropertyInternal(propertyName, Boolean.valueOf(value));
   }

   private void setPropertyInternal(final String propertyName, final Object value) {
      Object oldValue = this._properties.getObject(propertyName);

      if (oldValue == null) {
         if (value != null) {
            this._properties.put(propertyName, value);
            this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, value);
         }
      } else {
         if (!oldValue.equals(value)) {
            this._properties.put(propertyName, value);
            this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, value);
         }
      }
   }

   @Override
   public Boolean getBooleanProperty(final String propertyName) {
      return _properties.getBoolean(propertyName);
   }

   @Override
   public String getStringProperty(final String propertyName) {
      return _properties.getString(propertyName);
   }

   @Override
   public void setClassLoader(final ClassLoader classLoader) {
      _classLoader = classLoader;
      if (_xmlClassDescriptorResolver != null) {
         _xmlClassDescriptorResolver.setClassLoader(classLoader);
      }
   }

   @Override
   public void setXMLClassDescriptorResolver(final XMLClassDescriptorResolver xmlClassDescriptorResolver) {
      _xmlClassDescriptorResolver = xmlClassDescriptorResolver;
   }

   @Override
   public void setIntrospector(final Introspector introspector) {
      _introspector = introspector;
   }

   @Override
   public ClassLoader getClassLoader() {
      return _classLoader;
   }

   @Override
   public boolean getLenientIdValidation() {
      Boolean lenientIdValidation = _properties.getBoolean(XMLProperties.LENIENT_ID_VALIDATION);
      if (lenientIdValidation == null) {
         String message = "Property lenientIdValidation must not be null";
         LOG.warn(message);
         throw new IllegalStateException(message);
      }
      return lenientIdValidation.booleanValue();
   }

   @Override
   public boolean getLenientSequenceOrder() {
      Boolean lenientSequenceOrder = _properties.getBoolean(XMLProperties.LENIENT_SEQUENCE_ORDER);
      if (lenientSequenceOrder == null) {
         String message = "Property lenientSequenceOrder must not be null";
         LOG.warn(message);
         throw new IllegalStateException(message);
      }
      return lenientSequenceOrder.booleanValue();
   }

   @Override
   public Boolean getLoadPackageMapping() {
      return _properties.getBoolean(XMLProperties.LOAD_PACKAGE_MAPPING);
   }

   @Override
   public void setLoadPackageMapping(final Boolean loadPackageMapping) {
      _properties.put(XMLProperties.LOAD_PACKAGE_MAPPING, loadPackageMapping);
   }

   @Override
   public Boolean getUseIntrospector() {
      return _properties.getBoolean(XMLProperties.USE_INTROSPECTION);
   }

   @Override
   public void setUseIntrospector(final Boolean useIntrospector) {
      _properties.put(XMLProperties.USE_INTROSPECTION, useIntrospector);
   }

   @Override
   public boolean marshallingValidation() {
      Boolean marshallingValidation = _properties.getBoolean(XMLProperties.MARSHALLING_VALIDATION);
      if (marshallingValidation == null) {
         String message = "Property marshallingValidation must not be null";
         LOG.warn(message);
         throw new IllegalStateException(message);
      }
      return marshallingValidation.booleanValue();
   }

   @Override
   public boolean strictElements() {
      Boolean strictElements = _properties.getBoolean(XMLProperties.STRICT_ELEMENTS);
      if (strictElements == null) {
         String message = "Property strictElements must not be null";
         LOG.warn(message);
         throw new IllegalStateException(message);
      }
      return strictElements.booleanValue();
   }

   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener) {
      this.propertyChangeSupport.addPropertyChangeListener(listener);
   }

   @Override
   public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
   }

   @Override
   public void removePropertyChangeListener(PropertyChangeListener listener) {
      this.propertyChangeSupport.removePropertyChangeListener(listener);
   }

   @Override
   public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
   }
}

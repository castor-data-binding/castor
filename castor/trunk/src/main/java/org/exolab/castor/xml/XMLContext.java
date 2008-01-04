/*
 * Copyright 2007 Werner Guttmann
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
package org.exolab.castor.xml;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.castor.xml.InternalContext;
import org.castor.xml.AbstractInternalContext;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.tools.MappingTool;
import org.exolab.castor.util.ChangeLog2XML;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.schema.ScopableResolver;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.exolab.castor.xml.schema.writer.SchemaWriter;
import org.exolab.castor.xml.util.ResolverStrategy;
import org.exolab.castor.xml.util.resolvers.CastorXMLStrategy;
import org.xml.sax.InputSource;

/**
 * Bootstrap class for Castor XML that allows you to load information about the
 * domain objects used with Castor XML (marshallers and unmarshallers) by various means.
 * 
 * @author <a href="mailto:werner DOT guttmann At gmx DOT net">Werner Guttmann</a>
 * @since 1.1.2
 */
public class XMLContext {
    /** Logger to be used. */
    private static final Log LOG = LogFactory.getFactory().getInstance(XMLContext.class);
    
    /**
     * The internal XML context is the class which holds a couple of Castor states as it
     * provides some central methods needed in various places of Castor.
     */
    private InternalContext _internalContext;

    /**
     * Creates an instance of {@link XMLContext} with an internal XML context.
     */
    public XMLContext() {
        AbstractInternalContext internalContext = new AbstractInternalContext() { };
        
        internalContext.setClassLoader(getClass().getClassLoader());
        
        XMLClassDescriptorResolver cdr = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory
            .createClassDescriptorResolver(BindingType.XML);
        cdr.setInternalContext(internalContext);
        internalContext.setXMLClassDescriptorResolver(cdr);

        Introspector introspector = new Introspector();
        introspector.setInternalContext(internalContext);
        internalContext.setIntrospector(introspector);
        
        ResolverStrategy resolverStrategy = new CastorXMLStrategy();
        internalContext.setResolverStrategy(resolverStrategy);

        Resolver schemaResolver = new ScopableResolver();
        internalContext.setSchemaResolver(schemaResolver);
        
        _internalContext = internalContext;
    }
    
    /**
     * Instructs Castor to load class descriptors from the mapping given.
     * @param mapping Castor XML mapping (file), from which the required class
     * descriptors will be derived. 
     * @throws MappingException If the {@link Mapping} cannot be loaded and analyzed successfully.
     */
    public void addMapping(final Mapping mapping) throws MappingException {
         MappingUnmarshaller mappingUnmarshaller = new MappingUnmarshaller();
         MappingLoader mappingLoader = 
             mappingUnmarshaller.getMappingLoader(mapping, BindingType.XML);
         _internalContext.getXMLClassDescriptorResolver()
             .setMappingLoader(mappingLoader);        
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
        _internalContext.getXMLClassDescriptorResolver().addClass(clazz);
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
        _internalContext.getXMLClassDescriptorResolver().addClasses(clazzes);
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
        _internalContext.getXMLClassDescriptorResolver().addPackage(packageName); 
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
        _internalContext.getXMLClassDescriptorResolver().addPackages(packageNames);
    }

    /**
     * Creates an instance of a Castor XML specific {@link Mapping} instance.
     * @return a Castor XML specific {@link Mapping} instance.
     */
    public Mapping createMapping() {
        Mapping mapping = new Mapping();
//        mapping.setBindingType(BindingType.XML);
        return mapping;
    }
    
    /**
     * Creates a new {@link Marshaller} instance to be used for marshalling.
     * @return A new {@link Marshaller} instance.
     */
    public Marshaller createMarshaller() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new Marshaller instance.");
        }
        Marshaller marshaller = new Marshaller();
        marshaller.setInternalContext(_internalContext);
        return marshaller;
    }
    
    /**
     * Creates a new {@link Unmarshaller} instance to be used for unmarshalling. 
     * @return A new {@link Unmarshaller} instance, preconfigured with 
     *  a {@link XMLClassDescriptorResolver} instance with the class
     *  descriptors cached as loaded above.
     */
    public Unmarshaller createUnmarshaller() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new Unmarshaller instance.");
        }
        Unmarshaller unmarshaller = new Unmarshaller(_internalContext);
        return unmarshaller;
    }
    
    /**
     * To create a schema reader instance for reading XSD files.
     * @param inputSource the InputSource to read from
     * @return the SchemaReader instance created and initialized
     */
    public SchemaReader createSchemaReader(final InputSource inputSource) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new SchemaReader instance.");
        }
        SchemaReader sr = new SchemaReader();
        sr.setInternalContext(_internalContext);
        sr.setInputSource(inputSource);
        return sr;
    }
    
    /**
     * To create a schema writer instance for writing XSD files.
     * @param writer the Writer to write the text representation of the schema to
     * @return the SchemaWriter instance created and initialized
     * @throws IOException in case that initialization of SchemaWriter fails
     */
    public SchemaWriter createSchemaWriter(final Writer writer) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new SchemaWriter instance.");
        }
        SchemaWriter sw = new SchemaWriter();
        sw.setInternalContext(_internalContext);
        sw.setDocumentHandler(writer);
        return sw;
    }

    /**
     * To create a MappingTool instance.
     * @return the MappingTool instance ready to use
     */
    public MappingTool createMappingTool() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new MappingTool instance.");
        }
        MappingTool mt = new MappingTool();
        mt.setInternalContext(_internalContext);
        return mt;
    }

    /**
     * To create a new {@link ChangeLog2XML} instance.
     * @return the {@link ChangeLog2XML} instance ready to use
     */
    public ChangeLog2XML createChangeLog2XML() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new ChangeLog2XML instance.");
        }
        ChangeLog2XML changeLog2XML = new ChangeLog2XML();
        changeLog2XML.setInternalContext(_internalContext);
        return changeLog2XML;
    }

    /**
     * To set properties for marshalling and unmarshalling behavior. 
     * @param propertyName name of the property to set
     * @param value the value to set to
     */
    public void setProperty(final String propertyName, final Object value) {
        _internalContext.setProperty(propertyName, value);
    }

    /**
     * To set properties for marshalling and unmarshalling behavior. 
     * @param propertyName name of the property to set
     * @param value the value to set to
     */
    public void setProperty(final String propertyName, final boolean value) {
        _internalContext.setProperty(propertyName, value);
    }

    /**
     * To get the value of a specific property.
     * @param propertyName name of the Property
     * @return the value (Object) of the property
     */
    public Object getProperty(final String propertyName) {
        return _internalContext.getProperty(propertyName);
    }

    /**
     * To get the {@link InternalContext} as used when instantiating other
     * classes. Mind that this method is only used in tests and should
     * NOT be used in production code!
     * 
     * @return the {@link InternalContext} used
     * @deprecated
     */
    public InternalContext getInternalContext() {
        return _internalContext;
    }
}

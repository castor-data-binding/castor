/*
 * Copyright 2005 Ralf Joachim
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
package org.castor.mapping;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.CoreProperties;
import org.castor.core.util.Messages;
import org.castor.xml.InternalContext;
import org.castor.xml.AbstractInternalContext;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.Include;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.ClassDescriptorResolverFactory;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.exolab.castor.xml.util.ResolverStrategy;
import org.exolab.castor.xml.util.resolvers.CastorXMLStrategy;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class MappingUnmarshaller {
    //--------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(MappingUnmarshaller.class);
    
    /** The registry of MappingLoader's. */
    private final MappingLoaderRegistry _registry;
    
    /** The IDResolver to give to the Unmarshaller. This allows resolving "extends" and
     *  "depends" for included Mappings. */
    private final MappingUnmarshallIDResolver _idResolver;

    /** A flag that indicates of whether or not to allow redefinitions of class
     *  mappings. */
    private boolean _allowRedefinitions = false;

    /**
     * The {@link AbstractInternalContext}?holds all 'global' Castor states and access to
     * configuration.
     */
    private InternalContext _internalContext;
    
    //--------------------------------------------------------------------------

    /**
     * Construct a new MappingUnmarshaller.
     */
    public MappingUnmarshaller() {
        _registry = new MappingLoaderRegistry(new CoreProperties());
        _idResolver = new MappingUnmarshallIDResolver();
        AbstractInternalContext internalContext = new AbstractInternalContext() { };
        internalContext.setClassLoader(getClass().getClassLoader());
        
        XMLClassDescriptorResolver cdr = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory
            .createClassDescriptorResolver(BindingType.XML);
        cdr.setInternalContext(internalContext);
        internalContext.setXMLClassDescriptorResolver(cdr);

        Introspector introspector = new Introspector();
        introspector.setInternalContext(internalContext);
        internalContext.setIntrospector(introspector);
        cdr.setIntrospector(introspector);
        
        ResolverStrategy resolverStrategy = new CastorXMLStrategy();
        internalContext.setResolverStrategy(resolverStrategy);
        cdr.setResolverStrategy(resolverStrategy);

        _internalContext = internalContext;
    }
    
    /**
     * Enables or disables the ability to allow the redefinition
     * of class mappings.
     * 
     * @param allow a boolean that when true enables redefinitions.
    **/
    public void setAllowRedefinitions(final boolean allow) {
        _allowRedefinitions = allow;
    }

    //--------------------------------------------------------------------------

    /**
     * Returns a mapping resolver for the suitable engine. The engine's
     * specific mapping loader is created and used to create engine
     * specific descriptors, returning a suitable mapping resolver.
     * The mapping resolver is cached in memory and returned in
     * subsequent method calls.
     *
     * @param mapping The mapping to load and resolve.
     * @param bindingType The binding type to read from mapping.
     * @return A mapping resolver.
     * @throws MappingException A mapping error occured preventing
     *         descriptors from being generated from the loaded mapping.
     */
    public MappingLoader getMappingLoader(final Mapping mapping,
                                          final BindingType bindingType)
    throws MappingException {
        return getMappingLoader(mapping, bindingType, null);
    }

    /**
     * Returns a mapping resolver for the suitable engine. The engine's
     * specific mapping loader is created and used to create engine
     * specific descriptors, returning a suitable mapping resolver.
     * The mapping resolver is cached in memory and returned in
     * subsequent method calls.
     *
     * @param mapping The mapping to load and resolve.
     * @param bindingType The binding type to read from mapping.
     * @param param Arbitrary parameter that is to be passed to resolver.loadMapping().
     * @return A mapping resolver
     * @throws MappingException A mapping error occured preventing
     *         descriptors from being generated from the loaded mapping.
     */
    public MappingLoader getMappingLoader(final Mapping mapping,
                                          final BindingType bindingType,
                                          final Object param)
    throws MappingException {
        synchronized (this) {
            Iterator iter = mapping.getMappingSources().iterator();
            while (iter.hasNext()) {
                MappingSource source = (MappingSource) iter.next();
                loadMappingInternal(mapping, source.getResolver(), source.getSource());
            }

            AbstractMappingLoader loader;
            loader = (AbstractMappingLoader) _registry.getMappingLoader(
                    "CastorXmlMapping", bindingType);
            loader.setClassLoader(mapping.getClassLoader());
            loader.setAllowRedefinitions(_allowRedefinitions);
            loader.setInternalContext(_internalContext);
            loader.loadMapping(mapping.getRoot(), param);
            return loader;
        }
    }

    public void loadMappingOnly(final Mapping mapping)
    throws MappingException {
        synchronized (this) {
            Iterator iter = mapping.getMappingSources().iterator();
            while (iter.hasNext()) {
                MappingSource source = (MappingSource) iter.next();
                loadMappingInternal(mapping, source.getResolver(), source.getSource());
            }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Internal recursive loading method. This method will load the
     * mapping document into a mapping object and load all the included
     * mapping along the way into a single collection.
     *
     * @param mapping The mapping instance.
     * @param resolver The entity resolver to use.
     * @param url The URL of the mapping file.
     * @throws IOException An error occured when reading the mapping file.
     * @throws MappingException The mapping file is invalid.
     */
    protected void loadMappingInternal(final Mapping mapping, final DTDResolver resolver,
                                       final String url)
    throws IOException, MappingException {
        try {
            InputSource source = resolver.resolveEntity(null, url);
            if (source == null) { source = new InputSource(url); }
            if (source.getSystemId() == null) { source.setSystemId(url); }
            LOG.info(Messages.format("mapping.loadingFrom", url));
            loadMappingInternal(mapping, resolver, source);
        } catch (SAXException ex) {
            throw new MappingException(ex);
        }
    }

    /**
     * Internal recursive loading method. This method will load the
     * mapping document into a mapping object and load all the included
     * mapping along the way into a single collection.
     *
     * @param mapping The mapping instance.
     * @param resolver The entity resolver to use. May be null.
     * @param source The input source.
     * @throws MappingException The mapping file is invalid.
     */
    private void loadMappingInternal(final Mapping mapping, final DTDResolver resolver,
                                     final InputSource source)
    throws MappingException {
        // Clear all the cached resolvers, so they can be reconstructed a
        // second time based on the new mappings loaded
        _registry.clear();
        
        Object id = source.getSystemId();
        if (id == null) { id = source.getByteStream(); }
        if (id != null) {
            //check that the mapping has already been processed
            if (mapping.processed(id)) { return; }

            //mark the mapping as being processed
            mapping.markAsProcessed(id);
        }
        
        MappingRoot root = mapping.getRoot();
        _idResolver.setMapping(root);

        try {
            // Load the specificed mapping source
            Unmarshaller unm = new Unmarshaller(MappingRoot.class);
            unm.setValidation(false);
            unm.setEntityResolver(resolver);
            unm.setClassLoader(Mapping.class.getClassLoader());
            unm.setIDResolver(_idResolver);
            unm.setUnmarshalListener(
                    new MappingUnmarshallListener(this, mapping, resolver));

            MappingRoot loaded = (MappingRoot) unm.unmarshal(source);
                
            // Load all the included mapping by reference
            //-- note: this is just for processing any
            //-- includes which may have previously failed
            //-- using the IncludeListener...and to
            //-- report any potential errors.
            Enumeration includes = loaded.enumerateInclude();
            while (includes.hasMoreElements()) {
                Include include = (Include) includes.nextElement();
                if (!mapping.processed(include.getHref())) {
                    try {
                        loadMappingInternal(mapping, resolver, include.getHref());
                    } catch (Exception ex) {
                        throw new MappingException(ex);
                    }
                }
            }
            
            // gather "class" tags
            Enumeration enumeration = loaded.enumerateClassMapping();
            while (enumeration.hasMoreElements()) {
                root.addClassMapping((ClassMapping) enumeration.nextElement());
            }

            // gather "key-generator" tags
            enumeration = loaded.enumerateKeyGeneratorDef();
            while (enumeration.hasMoreElements()) {
                root.addKeyGeneratorDef((KeyGeneratorDef) enumeration.nextElement());
            }
            
            // gather "field-handler" tags
            root.setFieldHandlerDef(loaded.getFieldHandlerDef());

        } catch (Exception ex) {
            throw new MappingException(ex);
        }
    }

    /**
     * To set the internal context.
     * @param internalContext the {@link AbstractInternalContext}?to use
     */
//    public void setInternalContext(final InternalContext internalContext) {
//        _internalContext = internalContext;
//    }

    //--------------------------------------------------------------------------
}

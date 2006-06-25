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
package org.exolab.castor.mapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import org.castor.util.Configuration;
import org.castor.util.Messages;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.Include;
import org.exolab.castor.mapping.xml.KeyGeneratorDef;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.net.util.URIUtils;
import org.exolab.castor.util.DTDResolver;
import org.exolab.castor.xml.UnmarshalListener;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Utility class for loading mapping files and providing them to the
 * XML marshaller, JDO engine etc. The mapping file can be loaded from
 * a URL, input stream or SAX <tt>InputSource</tt>.
 * <p>
 * Multiple mapping files can be loaded with the same <tt>Mapping</tt>
 * object. When loading master mapping files that include other mapping
 * files it might be convenient to use {@link #setBaseURL} or {@link
 * #setEntityResolver}.
 * <p>
 * If the desired class loader is different than the one used by Castor
 * (e.g. if Castor is installed as a Java extension), the <tt>Mapping</tt>
 * object can be constructed with the proper class loader.
 * <p>
 * The following example loads two mapping files:
 * <pre>
 * Mapping mapping;
 *
 * mapping = new Mapping( getClass().getClassLoader() );
 * mapping.loadMapping( "mapping.xml" );
 * mapping.loadMapping( url );
 * </pre>
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public class Mapping {
    /**
     * Log writer to report progress. May be null.
     */
    private PrintWriter _logWriter;


    /**
     * The entity resolver to use. May be null.
     */
    private DTDResolver _resolver = new DTDResolver();


    /**
     * The loaded mapping.
     */
    private MappingRoot _mapping;

    /**
     * The IDResolver to give to the Unmarshaller
     * This allows resolving "extends" and "depends"
     * for included Mappings
    **/
    private ClassMappingResolver _idResolver = null;

    /**
     * The mapping state
     */
    private MappingState _state = new MappingState();
    
    /** A flag that indicates of whether or not to allow redefinitions of class
     *  mappings. */
    private boolean _allowRedefinitions = false;
    
    /** The class loader to use. */
    private final ClassLoader _classLoader;
    
    private MappingLoaderRegistry _registry;

    /**
     * Constructs a new mapping.
     *
     * @param loader The class loader to use, null for the default
     */
    public Mapping(final ClassLoader loader) {
        if (loader == null) {
            _classLoader = getClass().getClassLoader();
        } else {
            _classLoader = loader;
        }
        
        _resolver = new DTDResolver();
        _idResolver = new ClassMappingResolver();
        _registry = new MappingLoaderRegistry(Configuration.getInstance(), _classLoader);
    }

    /**
     * Constructs a new mapping.
     */
    public Mapping() {
        this( null );
    }

    /**
     * Returns a mapping resolver for the suitable engine. The engine's
     * specific mapping loader is created and used to create engine
     * specific descriptors, returning a suitable mapping resolver.
     * The mapping resolver is cached in memory and returned in
     * subsequent method calls.
     *
     * @param bindingType The binding type to read from mapping
     * @return A mapping resolver
     * @throws MappingException A mapping error occured preventing
     *         descriptors from being generated from the loaded mapping
     */
    public MappingLoader getResolver(final BindingType bindingType)
    throws MappingException {
        return getResolver(bindingType, null);
    }

    /**
     * Returns a mapping resolver for the suitable engine. The engine's
     * specific mapping loader is created and used to create engine
     * specific descriptors, returning a suitable mapping resolver.
     * The mapping resolver is cached in memory and returned in
     * subsequent method calls.
     *
     * @param bindingType The binding type to read from mapping
     * @param param Arbitrary parameter that is to be passed to resolver.loadMapping()
     * @return A mapping resolver
     * @throws MappingException A mapping error occured preventing
     *         descriptors from being generated from the loaded mapping
     */
    public synchronized MappingLoader getResolver(
            final BindingType bindingType,
            final Object param)
    throws MappingException {
        if (getRoot() == null) {
            throw new MappingException("Must call loadMapping first");
        }
        
        AbstractMappingLoader loader;
        loader = (AbstractMappingLoader) _registry.getMappingLoader("CastorXmlMapping", bindingType);
        loader.setClassLoader(_classLoader);
        loader.setAllowRedefinitions(_allowRedefinitions);
        loader.loadMapping(getRoot(), param);
        return loader;
    }

    /**
     * Returns a MappingRoot which contains all loaded mapping classes and
     * key generators definition.
     */
    public MappingRoot getRoot() {
        return _mapping;
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

    /**
     * Sets the log writer. If not null, errors and other messages
     * will be directed to that log writer.
     *
     * @param logWriter The log writer to use
     */
    public void setLogWriter(final PrintWriter logWriter) {
        _logWriter = logWriter;
    }

    /**
     * Sets the entity resolver. The entity resolver can be used to
     * resolve external entities and cached documents that are used
     * from within mapping files.
     *
     * @param resolver The entity resolver to use
     */
    public void setEntityResolver(final EntityResolver resolver) {
        _resolver = new DTDResolver(resolver);
    }


    /**
     * Sets the base URL for the mapping and related files. If the base
     * URL is known, files can be included using relative names. Any URL
     * can be passed, if the URL can serve as a base URL it will be used.
     * If url is an absolute path, it is converted to a file URL.
     *
     * @param url The base URL
     */
    public void setBaseURL(String url) {
        //-- remove filename if necessary:
        if (url != null) {        
            int idx = url.lastIndexOf('/');
            if (idx < 0) idx = url.lastIndexOf('\\');
            if (idx >= 0) {
                int extIdx = url.indexOf('.', idx);
                if (extIdx > 0) {
                    url = url.substring(0, idx);
                }
            }
        }
        
        try {
          _resolver.setBaseURL( new URL( url ) );
        } catch ( MalformedURLException except ) {
          // try to parse the url as an absolute path
          try {
            if ( _logWriter != null )
                _logWriter.println( Messages.format( "mapping.wrongURL", url ) );
            _resolver.setBaseURL( new URL("file", null, url) );
          } catch ( MalformedURLException except2 ) { }
        }
    }

    /**
     * Returns the class loader used by this mapping object. The returned
     * class loaded may be the one passed in the constructor, the one used
     * to load Castor, or in some 1.1 JVMs null.
     *
     * @return The class loader used by this mapping object (may be null)
     */
    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    /**
     * Loads the mapping from the specified URL. If an entity resolver
     * was specified, will use the entity resolver to resolve the URL.
     * This method is also used to load mappings referenced from another
     * mapping or configuration file.
     *
     * @param url The URL of the mapping file
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping(String url) throws IOException, MappingException {
        if (_resolver.getBaseURL() == null) {
            setBaseURL(url);
            url = URIUtils.getRelativeURI(url);
        }
        loadMappingInternal(url);
    }

    /**
     * Loads the mapping from the specified URL.
     *
     * @param url The URL of the mapping file
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping(final URL url) throws IOException, MappingException {
        try {
            if (_resolver.getBaseURL() == null) {
                _resolver.setBaseURL(url);
            }
            InputSource source = _resolver.resolveEntity(null, url.toExternalForm());
            if (source == null) {
                source = new InputSource(url.toExternalForm());
                source.setByteStream(url.openStream());
            } else
                source.setSystemId(url.toExternalForm());
            if (_logWriter != null) {
                _logWriter.println( Messages.format( "mapping.loadingFrom", url.toExternalForm() ) );
            }
            loadMappingInternal(source);
        } catch (SAXException ex) {
            throw new MappingException(ex);
        }
    }

    /**
     * Loads the mapping from the specified input source.
     *
     * @param source The input source
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping(final InputSource source) throws MappingException {
        loadMappingInternal(source);
    }

    /**
     * Internal recursive loading method. This method will load the
     * mapping document into a mapping object and load all the included
     * mapping along the way into a single collection.
     *
     * @param url The URL of the mapping file
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    private void loadMappingInternal(final String url)
    throws IOException, MappingException {
        try {
            InputSource source = _resolver.resolveEntity(null, url);
            if (source == null) { source = new InputSource(url); }
            if (source.getSystemId() == null) { source.setSystemId(url); }
            if (_logWriter != null) {
                _logWriter.println(Messages.format("mapping.loadingFrom", url));
            }
            loadMappingInternal(source);
        } catch (SAXException ex) {
            throw new MappingException(ex);
        }
    }

    /**
     * Internal recursive loading method. This method will load the
     * mapping document into a mapping object and load all the included
     * mapping along the way into a single collection.
     *
     * @param source The input source
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    private void loadMappingInternal(final InputSource source)
    throws MappingException {
        // Clear all the cached resolvers, so they can be reconstructed a
        // second time based on the new mappings loaded
        _registry.clear();
        
        //check that the mapping has already been processed
        if ((source.getSystemId()!=null) && _state.processed(source.getSystemId())) {
            //-- already processed...just return
            return;
        }
        
        try {
            if (_mapping == null) {
                _mapping = new MappingRoot();
                _idResolver.setMapping(_mapping);
            }

            //mark the mapping as being processed
            if (source.getSystemId() != null) {
                _state.markAsProcessed(source.getSystemId(), _mapping);
            }
                
            // Load the specificed mapping source
            Unmarshaller unm = new Unmarshaller(MappingRoot.class);
            unm.setEntityResolver(_resolver);
            if (_logWriter != null) { unm.setLogWriter(_logWriter); }
            unm.setClassLoader(Mapping.class.getClassLoader());
            unm.setIDResolver(_idResolver);
            unm.setUnmarshalListener(new IncludeListener());

            MappingRoot loaded = (MappingRoot) unm.unmarshal( source );
                
            // Load all the included mapping by reference
            //-- note: this is just for processing any
            //-- includes which may have previously failed
            //-- using the IncludeListener...and to
            //-- report any potential errors.
            Enumeration includes = loaded.enumerateInclude();
            while (includes.hasMoreElements()) {
                Include include = (Include) includes.nextElement();
                if (!_state.processed(include.getHref())) {
                    try {
                        loadMappingInternal(include.getHref());
                    } 
                    catch (Exception ex) {
                        throw new MappingException(ex);
                    }
                }
            }
            
            // gather "class" tags
            Enumeration enumeration = loaded.enumerateClassMapping();
            while (enumeration.hasMoreElements()) {
                _mapping.addClassMapping( (ClassMapping) enumeration.nextElement() );
            }

            // gather "key-generator" tags
            enumeration = loaded.enumerateKeyGeneratorDef();
            while (enumeration.hasMoreElements()) {
                _mapping.addKeyGeneratorDef((KeyGeneratorDef) enumeration.nextElement());
            }
        } catch (Exception ex) {
            throw new MappingException(ex);
        }
    }
    
    /**
     * An IDResolver to allow us to resolve ClassMappings
     * from included Mapping files
     */
    class ClassMappingResolver implements org.exolab.castor.xml.IDResolver {
        private MappingRoot _mapping = null;

        ClassMappingResolver() {
            super();
        }

        public void setMapping(MappingRoot mapping) {
            this._mapping = mapping;
        }

        /**
         * Returns the Object whose id matches the given IDREF,
         * or null if no Object was found.
         * @param idref the IDREF to resolve.
         * @return the Object whose id matches the given IDREF.
        **/
        public Object resolve(String idref) {
            if (_mapping == null) { return null; }
            for (int i = 0; i < _mapping.getClassMappingCount(); i++) {
                ClassMapping clsMap = _mapping.getClassMapping(i);
                if (idref.equals(clsMap.getName())) { return clsMap; }
            }
            return null;
        }
    } //-- ClassMappingResolver

    /**
     * A class to keep track of the loaded mapping.
     */
    class MappingState {
        private Hashtable _processed = null;

        /**
         * Creates a new SchemaUnmarshallerState
         */
        MappingState() {
            _processed   = new Hashtable(1);
         }

        /**
         * Marks the given mapping as having been processed.
         * @param systemID the key identifying the physical location
         * of the mapping to mark.
         * @param mapping the mapping to mark as having
         * been processed.
         */
        void markAsProcessed(String systemID, MappingRoot mapping) {
            _processed.put(systemID,mapping);
        }

        /**
         * Returns true if the given Mapping has been marked as processed
         * @param mapping the mapping to check for being marked as processed
         */
        boolean processed(MappingRoot mapping) {
            return _processed.contains(mapping);
        }

        /**
         * Returns true if the given systemID has been marked as processed
         * @param systemID location the systemID  to check for being marked as processed
         */
        boolean processed(String systemID) {
            return _processed.containsKey(systemID);
        }

        /**
         * Returns the mapping corresponding to the given systemID
         * @param systemID the systemID of the mapping
         */
         MappingRoot getMapping(String systemID) {
             return (MappingRoot)_processed.get(systemID);
         }
    }
    
    /**
     * An UnmarshalListener to handle mapping includes
     */
    class IncludeListener implements UnmarshalListener {
        /* Not used for includes processing */
        public void initialized (Object object) {
            // not used
        }

        /* Not used for includes processing */
        public void attributesProcessed(Object object) {
            // not used ...
        }

        /* Not used for includes processing */
        public void fieldAdded (String fieldName, Object parent, Object child) {
            //-- do nothing
        }

        /**
         * This method is called after an object
         * has been completely unmarshalled, including
         * all of its children (if any).
         *
         * @param object the Object that was unmarshalled.
         */
        public void unmarshalled (Object object) {
            if (object instanceof Include) {
                Include include = (Include) object;
                try {
                    loadMappingInternal( include.getHref() );
                } 
                catch ( Exception except ) {
                    //-- ignore error, it'll get reported
                    //-- later when we re-process the
                    //-- includes of the parent Mapping in
                    //-- loadMappingInternal
                }
            }
        }
    } //-- UnmarshalListener
} // class: Mapping



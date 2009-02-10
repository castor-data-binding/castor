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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.mapping.MappingSource;
import org.exolab.castor.mapping.xml.MappingRoot;
import org.exolab.castor.net.util.URIUtils;
import org.exolab.castor.util.DTDResolver;
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
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 */
public final class Mapping {
    //--------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(Mapping.class);
    
    private static final String DEFAULT_SOURCE_TYPE = "CastorXmlMapping";
    
    /** List of mapping sources to resolve. */
    private final List _mappings = new ArrayList();

    /** Set of processed systemID's. */
    private final Set _processed = new HashSet();

    /** The loaded mapping. */
    private final MappingRoot _root = new MappingRoot();

    /** The class loader to use. */
    private final ClassLoader _classLoader;
    
    /** The entity resolver to use. May be null. */
    private DTDResolver _resolver = new DTDResolver();
    
    //--------------------------------------------------------------------------

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
    }

    /**
     * Constructs a new mapping.
     */
    public Mapping() { this(null); }

    //--------------------------------------------------------------------------

    /**
     * Get list of mapping sources to resolve.
     * 
     * @return List of mapping sources to resolve.
     * @throws MappingException If no mapping source has been loaded previously.
     */
    public List getMappingSources() throws MappingException {
        return Collections.unmodifiableList(_mappings);
    }

    /**
     * Marks the given mapping as having been processed.
     * 
     * @param id systemID or stream to identify the mapping to mark.
     */
    public void markAsProcessed(final Object id) {
        _processed.add(id);
    }

    /**
     * Returns true if the given systemID or stream has been marked as processed.
     * 
     * @param id systemID or stream to check for being marked as processed.
     * @return true if the given systemID or stream has been marked as processed.
     */
    public boolean processed(final Object id) {
        return _processed.contains(id);
    }
    
    /**
     * Get the loaded mapping.
     * 
     * @return The loaded mapping.
     */
    public MappingRoot getRoot() { return _root; }
    
    //--------------------------------------------------------------------------

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
    public void setBaseURL(final String url) {
        String location = url;
        //-- remove filename if necessary:
        if (location != null) {        
            int idx = location.lastIndexOf('/');
            if (idx < 0) idx = location.lastIndexOf('\\');
            if (idx >= 0) {
                int extIdx = location.indexOf('.', idx);
                if (extIdx > 0) {
                    location = location.substring(0, idx);
                }
            }
        }
        
        try {
          _resolver.setBaseURL(new URL(location));
        } catch (MalformedURLException except) {
          // try to parse the url as an absolute path
          try {
              LOG.info(Messages.format("mapping.wrongURL", location));
              _resolver.setBaseURL(new URL("file", null, location));
          } catch (MalformedURLException except2) { }
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Loads the mapping from the specified URL with type defaults to
     * 'CastorXmlMapping'. If an entity resolver was specified, will use
     * the entity resolver to resolve the URL. This method is also used
     * to load mappings referenced from another mapping or configuration
     * file.
     *
     * @param url The URL of the mapping file.
     * @throws IOException An error occured when reading the mapping file.
     * @throws MappingException The mapping file is invalid.
     */
    public void loadMapping(final String url) throws IOException, MappingException {
        loadMapping(url, DEFAULT_SOURCE_TYPE);
    }

    /**
     * Loads the mapping from the specified URL. If an entity resolver
     * was specified, will use the entity resolver to resolve the URL.
     * This method is also used to load mappings referenced from another
     * mapping or configuration file.
     *
     * @param url The URL of the mapping file.
     * @param type The source type.
     * @throws IOException An error occured when reading the mapping file.
     * @throws MappingException The mapping file is invalid.
     */
    public void loadMapping(final String url, final String type)
    throws IOException, MappingException {
        String location = url;
        if (_resolver.getBaseURL() == null) {
            setBaseURL(location);
            location = URIUtils.getRelativeURI(location);
        }
        try {
            InputSource source = _resolver.resolveEntity(null, location);
            if (source == null) { source = new InputSource(location); }
            if (source.getSystemId() == null) { source.setSystemId(location); }
            LOG.info(Messages.format("mapping.loadingFrom", location));
            loadMapping(source, type);
        } catch (SAXException ex) {
            throw new MappingException(ex);
        }
    }

    /**
     * Loads the mapping from the specified URL with type defaults to
     * 'CastorXmlMapping'.
     *
     * @param url The URL of the mapping file.
     * @throws IOException An error occured when reading the mapping file.
     * @throws MappingException The mapping file is invalid.
     */
    public void loadMapping(final URL url) throws IOException, MappingException {
        loadMapping(url, DEFAULT_SOURCE_TYPE);
    }

    /**
     * Loads the mapping from the specified URL.
     *
     * @param url The URL of the mapping file.
     * @param type The source type.
     * @throws IOException An error occured when reading the mapping file.
     * @throws MappingException The mapping file is invalid.
     */
    public void loadMapping(final URL url, final String type)
    throws IOException, MappingException {
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
            LOG.info(Messages.format("mapping.loadingFrom", url.toExternalForm()));
           loadMapping(source, type);
        } catch (SAXException ex) {
            throw new MappingException(ex);
        }
    }

    /**
     * Loads the mapping from the specified input source with type defaults to
     * 'CastorXmlMapping'.
     *
     * @param source The input source.
     */
    public void loadMapping(final InputSource source) {
        loadMapping(source, DEFAULT_SOURCE_TYPE);
    }

    /**
     * Loads the mapping from the specified input source.
     *
     * @param source The input source.
     * @param type The source type.
     */
    public void loadMapping(final InputSource source, final String type) {
        _mappings.add(new MappingSource(source, type, _resolver));
    }

    //--------------------------------------------------------------------------
}



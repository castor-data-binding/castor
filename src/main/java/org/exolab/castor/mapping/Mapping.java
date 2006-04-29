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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.mapping;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import org.castor.util.Messages;
import org.exolab.castor.mapping.loader.MappingLoader;
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
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class Mapping
{


    /**
     * Associates engine name (XML, JDO, etc) with the class of its
     * mapping loader.
     */
    static class EngineMapping
    {

        private final String _name;

        private final String _loaderClass;

        EngineMapping( String name, String loaderClass )
        {
            _name = name;
            _loaderClass = loaderClass;
        }

        public String getLoaderClass()
        {
            return _loaderClass;
        }

        public String toString()
        {
            return _name;
        }

    }


    /**
     * Use this object to obtain the mapping resolver for JDO from
     * {@link #getResolver(EngineMapping)}.
     */
    public static final EngineMapping JDO =
        new EngineMapping( "jdo", "org.exolab.castor.jdo.engine.JDOMappingLoader" );


    /**
     * Use this object to obtain the mapping resolver for DAX from
     * {@link #getResolver(EngineMapping)}.
     */
    public static final EngineMapping DAX =
        new EngineMapping( "dax", "org.exolab.castor.dax.engine.DAXMappingLoader" );


    /**
     * Use this object to obtain the mapping resolver for XML from
     * {@link #getResolver(EngineMapping)}.
     */
    public static final EngineMapping XML =
        new EngineMapping( "xml", "org.exolab.castor.xml.XMLMappingLoader" );


    /**
     * A flag that indicates of whether or not to allow 
     * redefinitions of class mappings
     */
    private boolean _allowRedefinitions = false;
    

    /**
     * Log writer to report progress. May be null.
     */
    private PrintWriter _logWriter;


    /**
     * The class loader to use.
     */
    private ClassLoader _loader;


    /**
     * The entity resolver to use. May be null.
     */
    private DTDResolver _resolver = new DTDResolver();


    /**
     * The loaded mapping.
     */
    private MappingRoot  _mapping;

    /**
     * The IDResolver to give to the Unmarshaller
     * This allows resolving "extends" and "depends"
     * for included Mappings
    **/
    private ClassMappingResolver _idResolver = null;

    /**
     * The cached resolvers.
     */
    private Hashtable  _resolvers = new Hashtable();

    /**
     * The mapping state
     */
    private MappingState _state = new MappingState();


    /**
     * Constructs a new mapping.
     *
     * @param loader The class loader to use, null for the default
     */
    public Mapping( ClassLoader loader )
    {
        if ( loader == null )
            loader = getClass().getClassLoader();
        _loader = loader;
        _resolver = new DTDResolver();
        _idResolver = new ClassMappingResolver();
    }


    /**
     * Constructs a new mapping.
     */
    public Mapping()
    {
        this( null );
    }


    /**
     * Returns a mapping resolver for the suitable engine. The engine's
     * specific mapping loader is created and used to create engine
     * specific descriptors, returning a suitable mapping resolver.
     * The mapping resolver is cached in memory and returned in
     * subsequent method calls.
     *
     * @param engine The mapping engine
     * @return A mapping resolver
     * @throws MappingException A mapping error occured preventing
     *  descriptors from being generated from the loaded mapping
     * @see #JDO
     * @see #XML
     * @see #DAX
     */
    public MappingResolver getResolver( EngineMapping engine )
        throws MappingException
    {
        return getResolver( engine, null );
    }

    /**
     * Returns a mapping resolver for the suitable engine. The engine's
     * specific mapping loader is created and used to create engine
     * specific descriptors, returning a suitable mapping resolver.
     * The mapping resolver is cached in memory and returned in
     * subsequent method calls.
     *
     * @param engine The mapping engine
     * @param param Arbitrary parameter that is to be passed to resolver.loadMapping()
     * @return A mapping resolver
     * @throws MappingException A mapping error occured preventing
     *  descriptors from being generated from the loaded mapping
     * @see #JDO
     * @see #XML
     * @see #DAX
     */
    public synchronized MappingResolver getResolver( EngineMapping engine, Object param )
        throws MappingException
    {
        MappingResolver resolver;

        if ( _mapping == null )
            throw new MappingException( "Must call loadMapping first" );
        resolver = (MappingResolver) _resolvers.get( engine );
        if ( resolver == null ) {
            MappingLoader loaderImpl;
            Class         loaderClass;
            Constructor   loaderConst;

            try {
                if (_loader != null)
                    loaderClass = _loader.loadClass(engine.getLoaderClass() );
                else
                    loaderClass = Class.forName(engine.getLoaderClass());

                loaderConst = loaderClass.getConstructor( new Class[] { ClassLoader.class, PrintWriter.class } );
                loaderImpl = (MappingLoader) loaderConst.newInstance( new Object[] { _loader, _logWriter } );
                // Put loader in hash table first, so we don't get an error message if this
                // method is called a second time
                resolver = loaderImpl;
                _resolvers.put( engine, resolver );
                loaderImpl.setAllowRedefinitions(_allowRedefinitions);
                loaderImpl.loadMapping( _mapping, param );
            } 
            catch ( Exception except ) {
                throw new MappingException( except );
            }
        }
        return resolver;
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
    public void setAllowRedefinitions(boolean allow) {
        _allowRedefinitions = allow;
    } //-- setAllowRedefinitions

    /**
     * Sets the log writer. If not null, errors and other messages
     * will be directed to that log writer.
     *
     * @param logWriter The log writer to use
     */
    public void setLogWriter( PrintWriter logWriter )
    {
        _logWriter = logWriter;
    }


    /**
     * Sets the entity resolver. The entity resolver can be used to
     * resolve external entities and cached documents that are used
     * from within mapping files.
     *
     * @param resolver The entity resolver to use
     */
    public void setEntityResolver( EntityResolver resolver )
    {
        _resolver = new DTDResolver( resolver );
    }


    /**
     * Sets the base URL for the mapping and related files. If the base
     * URL is known, files can be included using relative names. Any URL
     * can be passed, if the URL can serve as a base URL it will be used.
     * If url is an absolute path, it is converted to a file URL.
     *
     * @param url The base URL
     */
    public void setBaseURL( String url )
    {
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
    public ClassLoader getClassLoader()
    {
        return _loader;
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
    public void loadMapping( String url )
        throws IOException, MappingException
    {
        if ( _resolver.getBaseURL() == null ) {
            setBaseURL( url );
            url = URIUtils.getRelativeURI(url);
        }
        loadMappingInternal( url );
    }


    /**
     * Loads the mapping from the specified URL.
     *
     * @param url The URL of the mapping file
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping( URL url )
        throws IOException, MappingException
    {
        InputSource source;

        try {
            if ( _resolver.getBaseURL() == null )
                _resolver.setBaseURL( url );
            source = _resolver.resolveEntity( null, url.toExternalForm() );
            if ( source == null ) {
                source = new InputSource( url.toExternalForm() );
                source.setByteStream( url.openStream() );
            } else
                source.setSystemId( url.toExternalForm() );
            if ( _logWriter != null )
                _logWriter.println( Messages.format( "mapping.loadingFrom", url.toExternalForm() ) );
            loadMappingInternal( source );
        } catch ( SAXException except ) {
            throw new MappingException( except );
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
    public void loadMapping( InputSource source )
        throws IOException, MappingException
    {
        loadMappingInternal( source );
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
    private void loadMappingInternal( String url )
        throws IOException, MappingException
    {
        InputSource source;

        try {
            source = _resolver.resolveEntity( null, url );
            if ( source == null )
                source = new InputSource( url );
            if (source.getSystemId() == null)
               source.setSystemId(url);
            if ( _logWriter != null )
                _logWriter.println( Messages.format( "mapping.loadingFrom", url ) );
            loadMappingInternal( source );
        } catch ( SAXException except ) {
            throw new MappingException( except );
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
    private void loadMappingInternal( InputSource source )
        throws IOException, MappingException
    {
        MappingRoot  loaded;
        Unmarshaller unm;
        Enumeration  enumeration;

        // Clear all the cached resolvers, so they can be reconstructed a
        // second time based on the new mappings loaded
        _resolvers.clear();
        
        //check that the mapping has already been processed
        if ((source.getSystemId()!=null) && _state.processed(source.getSystemId()) ) {
            //-- already processed...just return
            return;
        }
        try {
            if ( _mapping == null ) {
                _mapping = new MappingRoot();
                _idResolver.setMapping(_mapping);
            }

            //mark the mapping as being processed
            if (source.getSystemId() != null)
                _state.markAsProcessed(source.getSystemId(), _mapping);
                
            // Load the specificed mapping source
            unm = new Unmarshaller( MappingRoot.class );
            unm.setEntityResolver( _resolver );
            if ( _logWriter != null )
                unm.setLogWriter( _logWriter );
            unm.setClassLoader( Mapping.class.getClassLoader() );
            unm.setIDResolver(_idResolver);
            unm.setUnmarshalListener(new IncludeListener());

            loaded = (MappingRoot) unm.unmarshal( source );

                
            // Load all the included mapping by reference
            //-- note: this is just for processing any
            //-- includes which may have previously failed
            //-- using the IncludeListener...and to
            //-- report any potential errors.
            Enumeration includes = loaded.enumerateInclude();
            while ( includes.hasMoreElements() ) {
                Include include = (Include) includes.nextElement();
                if (!_state.processed( include.getHref() )) {
                    try {
                        loadMappingInternal( include.getHref() );
                    } 
                    catch ( Exception except ) {
                        throw new MappingException( except );
                    }
                }
            }
            
            // gather "class" tags
            enumeration = loaded.enumerateClassMapping();
            while ( enumeration.hasMoreElements() )
                _mapping.addClassMapping( (ClassMapping) enumeration.nextElement() );

            // gather "key-generator" tags
            enumeration = loaded.enumerateKeyGeneratorDef();
            while ( enumeration.hasMoreElements() ) {
                _mapping.addKeyGeneratorDef( (KeyGeneratorDef) enumeration.nextElement() );
            }
        } catch ( Exception except ) {
            throw new MappingException( except );
        }
    } //-- loadMappingInternal

    /**
     * An IDResolver to allow us to resolve ClassMappings
     * from included Mapping files
    **/
    class ClassMappingResolver
        implements org.exolab.castor.xml.IDResolver
    {
        private MappingRoot _mapping = null;

        ClassMappingResolver() {
            super();
        }

        public void setMapping(MappingRoot mapping) {
            this._mapping = mapping;
        } //-- setMapping

        /**
         * Returns the Object whose id matches the given IDREF,
         * or null if no Object was found.
         * @param idref the IDREF to resolve.
         * @return the Object whose id matches the given IDREF.
        **/
        public Object resolve(String idref) {
            if (_mapping == null) return null;
            for (int i = 0; i < _mapping.getClassMappingCount(); i++) {
                ClassMapping clsMap = _mapping.getClassMapping(i);
                if (idref.equals(clsMap.getName()))
                    return clsMap;
            }
            return null;
        } //-- resolve

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
        } //-- SchemaUnmarshallerState

      /**
       * Marks the given mapping as having been processed.
       * @param systemID the key identifying the physical location
       * of the mapping to mark.
       * @param mapping the mapping to mark as having
       * been processed.
       */
        void markAsProcessed(String systemID, MappingRoot mapping) {
            _processed.put(systemID,mapping);
        } //-- markAsProcessed

        /**
         * Returns true if the given Mapping has been marked as processed
         * @param mapping the mapping to check for being marked as processed
         */
        boolean processed(MappingRoot mapping) {
            return _processed.contains(mapping);
        } //-- processed

        /**
         * Returns true if the given systemID has been marked as processed
         * @param systemID location the systemID  to check for being marked as processed
         */
        boolean processed(String systemID) {
            return _processed.containsKey(systemID);
        } //-- processed

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
        public void fieldAdded (String fieldName, Object parent, Object child) 
        {
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



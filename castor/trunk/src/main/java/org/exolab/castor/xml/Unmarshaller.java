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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;


//-- castor imports
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.ObjectFactory;
import org.exolab.castor.xml.location.FileLocation;
import org.exolab.castor.xml.util.DOMEventProducer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/**
 * An unmarshaller to allowing unmarshalling of XML documents to
 * Java Objects. The Class must specify
 * the proper access methods (setters/getters) in order for instances
 * of the Class to be properly unmarshalled.
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-02-23 14:16:51 -0700 (Thu, 23 Feb 2006) $
 */
public class Unmarshaller {

    /**
     * Logger from commons-logging
     */
    private static final Log LOG = LogFactory.getLog(Unmarshaller.class);

    //----------------------------/
    //- Private Member Variables -/
    //----------------------------/

    /**
     * The class descriptor resolver
     */
    private XMLClassDescriptorResolver _cdResolver = null;

    /**
     * The Class that this Unmarshaller was created with
     */
    private Class _class = null;

    /**
     * A boolean indicating whether or not collections (including
     * arrays) should be cleared upon initial use by Castor.
     * False by default for backward compatibility.
     */
     private boolean _clearCollections = false;

    /**
     * Castor configuration
     */
     private Configuration _config = null;
     

    /**
     * A user specified IDResolver for resolving IDREFs
     */
    private IDResolver _idResolver = null;

    /**
     * A boolean that specifies whether or not
     * non-matched attributes should be ignored upon
     * unmarshalling.
     */
    private boolean _ignoreExtraAtts = true;

    /**
     * A boolean that specifies whether or not
     * non-matched elements should be ignored upon
     * unmarshalling.
     */
    private boolean _ignoreExtraElements = false;

    /**
     * The instance of _class to Unmarshal into (optional)
     */
    private Object _instanceObj = null;

    /**
     * The EntityResolver used for resolving entities
     */
    EntityResolver entityResolver = null;

    /**
     * The class loader to use
     */
    private ClassLoader _loader = null;

    /**
     * A boolean to indicate that objects should
     * be re-used where appropriate
     */
    private boolean _reuseObjects = false;

    
    /**
     * The unmarshaller listener that listens to unmarshalling event
     */
    private UnmarshalListener _unmarshalListener = null;

    /**
     * The flag indicating whether or not to validate during
     * unmarshalling
     */
    private boolean _validate = false;

    /**
     * A flag indicating the unmarshaller should preserve 
     * "ignorable" whitespace. The XML instance can
     * control it's own behavior using the xml:space
     * attribute. This sets the "default" behavior
     * when xml:space="default".
     */
    private boolean _wsPreserve = false;
    
    /**
     * A list of namespace To Package Mappings
     */
    private HashMap _namespaceToPackage = null;

    /**
     * An optional factory for unmarshalling objects
     */
	private ObjectFactory _objectFactory;
    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new basic Unmarshaller
     *
     * When using this constructor it will most likely be
     * necessary to use a mapping file or ClassDescriptorResolver
     * So that the Unmarshaller can find the classes during the
     * unmarshalling process.
     */
    public Unmarshaller() {
        this((Class)null, (ClassLoader)null);
    } //-- Unmarshaller()

    /**
     * Creates a new Unmarshaller with the given Class
     *
     * @param c the Class to create the Unmarshaller for, this
     * may be null, if the Unmarshaller#setMapping is called
     * to load a mapping for the root element of xml document.
     */
    public Unmarshaller(Class c) {
        this(c, null);
    } //-- Unmarshaller(Class)

    /**
     * Creates a new Unmarshaller with the given Class
     *
     * @param c the Class to create the Unmarshaller for, this
     * may be null, if the Unmarshaller#setMapping is called
     * to load a mapping for the root element of xml document.
     * @param loader The ClassLoader to use.
     */
    public Unmarshaller(Class c, ClassLoader loader) {
        super();
        initConfig();
        setClass(c);
        _loader = loader;
        if ((loader == null) && (c != null)) {
            _loader = c.getClassLoader();
        }
        _cdResolver = (XMLClassDescriptorResolver) 
            ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
        _cdResolver.setClassLoader(loader);
    } //-- Unmarshaller(Class)


    /**
     * Creates a new Unmarshaller with the given Mapping
     *
     * @param mapping The Mapping to use.
     */
    public Unmarshaller(Mapping mapping)
        throws MappingException
    {
        super();
        initConfig();
        if (mapping != null) {
            setMapping(mapping);
            this._loader = mapping.getClassLoader();
        }
    } //-- Unmarshaller(Mapping)


    /**
     * Creates a new Unmarshaller with the given Object
     *
     * @param root the instance to unmarshal into. This
     * may be null, if the Unmarshaller#setMapping is called
     * to load a mapping for the root element of xml document.
     */
    public Unmarshaller(Object root) {
        initConfig();
        if (root != null) {
            final Class clazz = root.getClass();
            setClass(clazz);
            _loader = clazz.getClassLoader();
        }
        _instanceObj = root;
    } //-- Unmarshaller(Class)

    /**
     * Used by constructors to get configuration information
     */
    private void initConfig() {
        _config   = LocalConfiguration.getInstance();
        _validate = _config.marshallingValidation();
        _ignoreExtraElements = (!_config.strictElements());
        
        //-- process namespace to package mappings
        String mappings = _config.getProperty(Configuration.Property.NamespacePackages, null);
        if (mappings != null) {
            StringTokenizer tokens = new StringTokenizer(mappings, ",");
            while(tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                int sepIdx = token.indexOf('=');
                if (sepIdx < 0) continue;
                String ns = token.substring(0,sepIdx).trim();
                String javaPackage = token.substring(sepIdx+1).trim();
                addNamespaceToPackageMapping(ns, javaPackage);
            }
        }
        
        
    } //-- initConfig
    
    /**
     * Adds a mapping from the given namespace URI to the given
     * package name
     * 
     * @param nsURI the namespace URI to map from
     * @param packageName the package name to map to
     */
    public void addNamespaceToPackageMapping(String nsURI, String packageName) {
        
        
        if (_namespaceToPackage == null) {
        	_namespaceToPackage = new HashMap();
        }
        if (nsURI == null) nsURI = "";
        if (packageName == null) packageName = "";
        _namespaceToPackage.put(nsURI, packageName);
        
    } //-- addNamespaceToPackageMapping
    

    /**
     * Creates and initalizes an UnmarshalHandler
     * @return the new UnmarshalHandler
    **/
    public UnmarshalHandler createHandler() {

        UnmarshalHandler handler = new UnmarshalHandler(_class);
        
        if (_cdResolver == null) {
            _cdResolver = (XMLClassDescriptorResolver) 
                ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
            _cdResolver.setClassLoader(_loader);
        }
        handler.setResolver(_cdResolver);
        handler.setClearCollections(_clearCollections);
        handler.setReuseObjects(_reuseObjects);
        handler.setValidation(_validate);
        handler.setIgnoreExtraAttributes(_ignoreExtraAtts);
        handler.setIgnoreExtraElements(_ignoreExtraElements);
        handler.setConfiguration(_config);
        handler.setWhitespacePreserve(_wsPreserve);
        
        // If the object factory has been set, set it on the handler
        if (this._objectFactory != null) {
        	handler.setObjectFactory(this._objectFactory);
        }

        //-- copy namespaceToPackageMappings
        if (_namespaceToPackage != null) {
        	Iterator keys = _namespaceToPackage.keySet().iterator();
            while (keys.hasNext()) {
                String nsURI = (String)keys.next();
                String pkgName = (String) _namespaceToPackage.get(nsURI);
            	handler.addNamespaceToPackageMapping(nsURI, pkgName);
            }
        }

        if (_instanceObj != null) {
            handler.setRootObject(_instanceObj);
        }
        if (_idResolver != null)
            handler.setIDResolver(_idResolver);

        if (_loader != null)
            handler.setClassLoader(_loader);

        if (_unmarshalListener != null)
            handler.setUnmarshalListener(_unmarshalListener);

        return handler;
    } //-- createHandler
    
    /**
     * Indicates whether or not validation should be performed during umarshalling.
     * @return True if validation is performed during umarshalling.
     */
    public boolean isValidating() {
        return _validate;
    }

    /**
     * Sets the 'expected' {@link Class} instance on the Unmarshaller.
     *
     * @param clazz the Class to create the Unmarshaller for, this
     * may be null, if the Unmarshaller#setMapping is called
     * to load a mapping for the root element of xml document.
     */
    public void setClass(Class clazz) {
        _class = clazz;
    } //-- setClass(Class)

    /**
     * Sets the 'expected' {@link Object} instance on the Unmarshaller, into
     * which will be unmarshalled.
     *
     * @param root the instance to unmarshal into. This
     * may be null, if the Unmarshaller#setMapping is called
     * to load a mapping for the root element of xml document.
     */
    public void setObject(Object root) {
        _instanceObj = root;
    } //-- setObject(Object)
    
    /**
     * Sets the ClassLoader to use when loading new classes.
     * <br />
     * <b>Note:</b>This ClassLoader is used for classes
     * loaded by the unmarshaller only. If a Mapping has
     * been set, the Mapping has it's own ClassLoader and
     * may also need to be set propertly.
     * <br />
     *
     * @param loader the ClassLoader to use
    **/
    public void setClassLoader(ClassLoader loader) {
        this._loader = loader;
    } //-- setClassLoader


    /**
     * Sets whether or not to clear collections (including arrays)
     * upon first use to remove default values. By default, and
     * for backward compatibility with previous versions of Castor
     * this value is false, indicating that collections are not
     * cleared before initial use by Castor.
     *
     * @param clear the boolean value that when true indicates
     * collections should be cleared upon first use.
     */
    public void setClearCollections(boolean clear) {
        _clearCollections = clear;
    } //-- setClearCollections

    /**
	 * Custom debugging is replaced with commons-logging
     * @deprecated
    **/
    public void setDebug(boolean debug) {
    	//  no-op
    } //-- setDebug

    /**
     * Sets the EntityResolver to use when resolving system and
     * public ids with respect to entites and Document Type.
     * @param entityResolver the EntityResolver to use when
     * resolving System and Public ids.
    **/
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    } //-- entityResolver

    /**
     * Sets the IDResolver to use when resolving IDREFs for
     * which no associated element may exist in XML document.
     *
     * @param idResolver the IDResolver to use when resolving
     * IDREFs for which no associated element may exist in the
     * XML document.
    **/
    public void setIDResolver(IDResolver idResolver) {
        _idResolver = idResolver;
    } //-- idResolver

    /**
     * Sets whether or not attributes that do not match
     * a specific field should simply be ignored or
     * reported as an error. By default, extra attributes
     * are ignored.
     *
     * @param ignoreExtraAtts a boolean that when true will
     * allow non-matched attributes to simply be ignored.
     */
    public void setIgnoreExtraAttributes(boolean ignoreExtraAtts) {
        _ignoreExtraAtts = ignoreExtraAtts;
    } //-- setIgnoreExtraAttributes

    /**
     * Sets whether or not elements that do not match
     * a specific field should simply be ignored or
     * reported as an error. By default, extra elements
     * are flagged as an error.
     *
     * @param ignoreExtraElements a boolean that when true will
     * allow non-matched elements to simply be ignored.
     */
    public void setIgnoreExtraElements(boolean ignoreExtraElements) {
        _ignoreExtraElements = ignoreExtraElements;
    } //-- setIgnoreExtraElements

    /**
     * Logging is replaced with commons-logging.
     * @param printWriter the PrintWriter to use for logging
     * @deprecated
    **/
    public void setLogWriter(PrintWriter printWriter) {
		// no-op
    } //-- setLogWriter

    /**
     * Sets the Mapping to use during unmarshalling. If the Mapping has a ClassLoader it
     * will be used during unmarshalling.
     *
     * @param mapping Mapping to use during unmarshalling.
     * @see #setResolver
     */
    public void setMapping(final Mapping mapping) throws MappingException {
        if (_loader == null) {
            _loader = mapping.getClassLoader();
        }
        
        if (_cdResolver == null) {
            _cdResolver = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory
                .createClassDescriptorResolver(BindingType.XML);
            
            _cdResolver.setClassLoader(_loader);
        }
        
        MappingUnmarshaller mum = new MappingUnmarshaller();
        MappingLoader resolver = mum.getMappingLoader(mapping, BindingType.XML);
        _cdResolver.setMappingLoader(resolver);
    }

    /**
     * Sets a boolean that when true indicates that objects
     * contained within the object model should be re-used
     * where appropriate. This is only valid when unmarshalling
     * to an existing object.
     *
     * @param reuse the boolean indicating whether or not
     * to re-use existing objects in the object model.
    **/
    public void setReuseObjects(boolean reuse) {
        _reuseObjects = reuse;
    } //-- setReuseObjects

    /**
     * Sets the ClassDescriptorResolver to use during unmarshalling
     * @param cdr the ClassDescriptorResolver to use
     * @see #setMapping
     * <BR />
     * <B>Note:</B> This method will nullify any Mapping
     * currently being used by this Unmarshaller
    **/
    public void setResolver( XMLClassDescriptorResolver cdr ) {

        if (cdr != null)
            _cdResolver = cdr;
        else {
            _cdResolver = (XMLClassDescriptorResolver) 
                ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
            _cdResolver.setClassLoader(_loader);
        }

    } //-- setResolver

    /**
     * Sets an optional {@link UnmarshalListener} to receive pre and
     * post unmarshal notification for each Object in the tree.
     * An UnmarshalListener is often used to allow objects to
     * appropriately initialize themselves by taking appliction
     * specific behavior as they are unloaded.
     * Current only one (1) listener is allowed. If you need
     * register multiple listeners, you will have to create
     * your own master listener that will forward the
     * event notifications and manage the multiple
     * listeners.
     *
     * @param listener the {@link UnmarshalListener} to set.
    **/
    public void setUnmarshalListener(UnmarshalListener listener) {
        _unmarshalListener = listener;
    } //-- setUnmarshalListener


    /**
     * Sets the flag for validation.
     * 
     * @param validate A boolean to indicate whether or not validation should be done
     *        during umarshalling.
     *        <br/>
     *        By default validation will be performed.
     */
    public void setValidation(boolean validate) {
        _validate = validate;
    } //-- setValidation

    /**
     * Sets the top-level whitespace (xml:space) to either
     * preserving or non preserving. The XML document
     * can override this value using xml:space on specific
     * elements.This sets the "default" behavior
     * when xml:space="default".
     *
     * @param preserve a boolean that when true enables
     * whitespace preserving by default. 
     */
    public void setWhitespacePreserve(boolean preserve) {
        _wsPreserve = preserve;
    } //-- setWhitespacePreserve

    /**
     * Unmarshals Objects of this Unmarshaller's Class type.
     * The Class must specify the proper access methods
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param reader the Reader to read the XML from
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
    **/
    public Object unmarshal(Reader reader)
        throws MarshalException, ValidationException
    {
        return unmarshal(new InputSource(reader));
    } //-- unmarshal(Reader reader)

    /**
     * Unmarshals Objects of this Unmarshaller's Class type.
     * The Class must specify the proper access methods
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param eventProducer the EventProducer which produces
     * the SAX events
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
     * @deprecated please use @see #unmarshal(SAX2EventProducer) instead. 
    **/
    public Object unmarshal(EventProducer eventProducer)
        throws MarshalException, ValidationException
    {
        UnmarshalHandler handler = createHandler();
        eventProducer.setDocumentHandler(handler);
        try {
            eventProducer.start();
        }
        catch(org.xml.sax.SAXException sx) {
            convertSAXExceptionToMarshalException(handler, sx);
        }
        return handler.getObject();

    } //-- unmarshal(EventProducer)

    /**
     * Unmarshals Objects of this Unmarshaller's Class type.
     * The Class must specify the proper access methods
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param eventProducer the SAX2EventProducer instance which produces
     * the SAX 2 events
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
     * @since 1.0M3
    **/
    public Object unmarshal(SAX2EventProducer eventProducer)
        throws MarshalException, ValidationException
    {
        UnmarshalHandler handler = createHandler();
        eventProducer.setContentHandler(handler);
        try {
            eventProducer.start();
        }
        catch(org.xml.sax.SAXException sx) {
            convertSAXExceptionToMarshalException(handler, sx);
        }
        return handler.getObject();

    } //-- unmarshal(SAX2EventProducer)


    /**
     * Unmarshals Objects of this Unmarshaller's Class type.
     * The Class must specify the proper access methods
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param source the InputSource to read the XML from
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
    **/
    public Object unmarshal(InputSource source)
        throws MarshalException, ValidationException
    {
        XMLReader reader = null;
        Parser parser = null;
        
        //-- First try XMLReader
        try {
            reader = _config.getXMLReader();
            if (entityResolver != null)
                reader.setEntityResolver(entityResolver);
        } catch(RuntimeException rx) {
        	LOG.debug("Unable to create SAX XMLReader, attempting SAX Parser.");
        }
        
        if (reader == null) {
            parser = _config.getParser();
            if (parser == null)
                throw new MarshalException("Unable to create SAX Parser.");
            if (entityResolver != null)
                parser.setEntityResolver(entityResolver);
        }


        UnmarshalHandler handler = createHandler();
        

        try {
            if (reader != null) {
                reader.setContentHandler(handler);
                reader.setErrorHandler(handler);
                reader.parse(source);
            }
            else {
                parser.setDocumentHandler(handler);
                parser.setErrorHandler(handler);
                parser.parse(source);
            }
        }
        catch(java.io.IOException ioe) {
            throw new MarshalException(ioe);
        }
        catch(org.xml.sax.SAXException sx) {
            convertSAXExceptionToMarshalException(handler, sx);
        }

        return handler.getObject();
    } //-- unmarshal(InputSource)


    /**
     * Unmarshals Objects of this Unmarshaller's Class type.
     * The Class must specify the proper access methods
     * (setters/getters) in order for instances of the Class
     * to be properly unmarshalled.
     * @param node the DOM node to read the XML from
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
    **/
    public Object unmarshal(Node node)
        throws MarshalException, ValidationException
    {
		return unmarshal(new DOMEventProducer(node));
    } //-- unmarshal(EventProducer)

    /**
     * Converts a SAXException to a (localised) MarshalException.
     * @param handler The {@link UnmarshalHandler} required to obtain DocumentLocator instance.
     * @param sex The {@link SAXException} instance
     * @throws MarshalException The {@link MarshalException} instance derived from the SAX exception.
     */
    private void convertSAXExceptionToMarshalException(UnmarshalHandler handler, SAXException sex) throws MarshalException {
        Exception except = sex.getException();
        if (except == null) {
            except = sex;
        }
        MarshalException marshalEx = new MarshalException(except);
        if (handler.getDocumentLocator() != null) {
            FileLocation location = new FileLocation();
            location.setFilename(handler.getDocumentLocator().getSystemId());
            location.setLineNumber(handler.getDocumentLocator().getLineNumber());
            location.setColumnNumber(handler.getDocumentLocator().getColumnNumber());
            marshalEx.setLocation(location);
        }
        throw marshalEx;
    }

    //-------------------------/
    //- Public Static Methods -/
    //-------------------------/


    /**
     * Returns a ContentHandler for the given UnmarshalHandler
     *
     * @return the ContentHandler
     */
    public static ContentHandler getContentHandler(UnmarshalHandler handler)
        throws SAXException
    {
        return handler;
    } //-- getContentHandler

    /**
     * Unmarshals Objects of the given Class type. The Class must specify
     * the proper access methods (setters/getters) in order for instances
     * of the Class to be properly unmarshalled.
     *
     * <p><b>Note:</b>This is a *static* method, any mapping files set
     * on a particular Unmarshaller instance, and any changes made
     * via setters will be unavailable to this method.</p>
     *
     * @param c the Class to create a new instance of
     * @param reader the Reader to read the XML from
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
    **/
    public static Object unmarshal(Class c, Reader reader)
        throws MarshalException, ValidationException
    {
        Unmarshaller unmarshaller = createUnmarshaller(c);
        return unmarshaller.unmarshal(reader);
    } //-- void unmarshal(Writer)

    /**
     * Helper method for static #unmarshal methods to create
     * an {@link Unmarshaller} instance.
     * 
     * @param clazz The root class to be used during unmarshalling.
     * @return An {@link Unmarshaller} instance.
     */
    private static Unmarshaller createUnmarshaller(Class clazz) {
        Unmarshaller unmarshaller = new Unmarshaller(clazz);
        
        // TODO: Should this be at level INFO?
        if (LOG.isDebugEnabled()) {
            LOG.debug("*static* unmarshal method called, this will ignore any "
                    + "mapping files or changes made to an Unmarshaller instance.");
        }
        
        //-- for backward compatibility with Castor versions
        //-- prior to version 0.9.5.3
        unmarshaller.setWhitespacePreserve(true);
        
        return unmarshaller;
    }

    /**
     * Unmarshals Objects of the given Class type. The Class must specify
     * the proper access methods (setters/getters) in order for instances
     * of the Class to be properly unmarshalled.
     *
     * <p><b>Note:</b>This is a *static* method, any mapping files set
     * on a particular Unmarshaller instance, and any changes made
     * via setters will be unavailable to this method.</p>
     *
     * @param c the Class to create a new instance of
     * @param source the InputSource to read the XML from
     * @exception MarshalException when there is an error during
     * the unmarshalling process
     * @exception ValidationException when there is a validation error
     */
    public static Object unmarshal(Class c, InputSource source)
        throws MarshalException, ValidationException
    {
        Unmarshaller unmarshaller = createUnmarshaller(c);
        
        return unmarshaller.unmarshal(source);
    } //-- void unmarshal(Writer)

    /**
     * Unmarshals Objects of the given Class type. The Class must specify
     * the proper access methods (setters/getters) in order for instances
     * of the Class to be properly unmarshalled.
     *
     * <p><b>Note:</b>This is a *static* method, any mapping files set
     * on a particular Unmarshaller instance, and any changes made
     * via setters will be unavailable to this method.</p>
     *
     * @param c The Class to create a new instance of.
     * @param node The DOM Node to read the XML from.
     * @exception MarshalException When there is an error during the unmarshalling
     *            process.
     * @exception ValidationException When there is a validation error.
     */
    public static Object unmarshal(Class c, Node node)
    throws MarshalException, ValidationException {
        Unmarshaller unmarshaller = createUnmarshaller(c);
        
        return unmarshaller.unmarshal(node);
    } //-- void unmarshal(Writer)

	/**
     * Set an object factory for the unmarshaller. This
     * factory will be used to construct the objects being
     * unmarshalled.
     * @param objectFactory Factory used for constructing objects
     *        during unmarshalling.
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {
		this._objectFactory = objectFactory;
	} // -- setObjectFactory
    
	/**
	 * Returns the value of the given Castor XML-specific property.
	 * @param name Qualified name of the CASTOR XML-specific property.
	 * @return The current value of the given property.
	 * @since 1.1.2
    */
	public String getProperty(final String name) {
	    return _config.getProperties().getProperty(name);
	}

	/**
     * Sets a custom value of a given Castor XML-specific property.
	 * @param name Name of the Castor XML property 
	 * @param value Custom value to set.
     * @since 1.1.2
	 */
	public void setProperty(final String name, final String value) {
	    _config.getProperties().setProperty(name, value);
	}
    
} //-- Unmarshaller


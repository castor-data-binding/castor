/*
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
 * Copyright 2001 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.binding;

//-Castor imports
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import org.exolab.castor.builder.binding.xml.AutomaticNamingType;
import org.exolab.castor.builder.binding.xml.Binding;
import org.exolab.castor.builder.binding.xml.ComponentBindingType;
import org.exolab.castor.builder.binding.xml.IncludeType;
import org.exolab.castor.builder.binding.xml.NamingXMLType;
import org.exolab.castor.builder.binding.xml.PackageType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class is responsible for loading a binding document into an in-memory
 * representation that is meant to be used by the SourceGenerator.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class BindingLoader {
    // TODO Implement the enumeration handling

    /**
     * The Source Generator Binding File loaded by this BindingLoader.
     */
    private ExtendedBinding _binding;

    /**
     * The binding resolver used for resolving entities.
     */
    private BindingResolver _resolver = new BindingResolver();

    /**
     * No-arg constructor.
     */
    public BindingLoader() {
        // Nothing to do
    }

    /**
     * Loads the binding file from the {@link URL} given, and populates
     * the {@link ExtendedBinding} instance from the values given.
     * @param url The URL for the binding file to process.
     * @throws BindingException If the binding file cannnot be processed properly.
     */
    public void loadBinding(final String url) throws BindingException {
        InputSource source;
        try {
            source = _resolver.resolveEntity(null, url);
            if (source == null) {
                source = new InputSource(url);
            }
            if (source.getSystemId() == null) {
                source.setSystemId(url);
            }
            loadBinding(source);
        } catch (SAXException ex) {
            throw new BindingException(ex);
        } catch (IOException ioe) {
            throw new BindingException(ioe);
        }
    }

    /**
     * Loads a Binding Document. This method will load the binding document into
     * a binding object and load all the included bindings along the way into a
     * single collection.
     *
     * @param source
     *            The binding document to load.
     * @throws BindingException
     *             thrown when an error occurred during the unmarshalling.
     */
    @SuppressWarnings("unchecked")
    public void loadBinding(final InputSource source) throws BindingException {
        Binding loaded = null;
        if (_binding == null) {
            _binding = new ExtendedBinding();
        }

        //do not use the static method to ensure validation is turned on
        Unmarshaller unmarshaller = new Unmarshaller(Binding.class);
        unmarshaller.setValidation(true);

        try {
            loaded = (Binding) unmarshaller.unmarshal(source);

            //--Copy one by one the components loaded in the root binding
            _binding.setDefaultBindingType(loaded.getDefaultBindingType());

            //--packages
            Enumeration<PackageType> packages = (Enumeration<PackageType>)loaded.enumeratePackage();
            while (packages.hasMoreElements()) {
                PackageType tempPackage = packages.nextElement();
                _binding.addPackage(tempPackage);
            }

            //--NamingXML
            NamingXMLType naming = loaded.getNamingXML();
            if (naming != null) {
                _binding.setNamingXML(naming);
            }

            //--NamingXML
            AutomaticNamingType automaticNaming = loaded.getAutomaticNaming();
            if (automaticNaming != null) {
                _binding.setAutomaticNaming(automaticNaming);
                _binding.handleAutomaticNaming(automaticNaming);
            }
            
            //--elementBindings
            Enumeration<ComponentBindingType> elements = (Enumeration<ComponentBindingType>)loaded.enumerateElementBinding();
            while (elements.hasMoreElements()) {
                ComponentBindingType tempComp = elements.nextElement();
                _binding.addElementBinding(tempComp);
            }

            //--attributeBindings
            Enumeration<ComponentBindingType> attributes = (Enumeration<ComponentBindingType>)loaded.enumerateAttributeBinding();
            while (attributes.hasMoreElements()) {
                ComponentBindingType  tempComp = attributes.nextElement();
                _binding.addAttributeBinding(tempComp);
            }

            //--ComplexTypeBindings
            Enumeration<ComponentBindingType> complexTypes = (Enumeration<ComponentBindingType>)loaded.enumerateComplexTypeBinding();
            while (complexTypes.hasMoreElements()) {
                ComponentBindingType tempComp = complexTypes.nextElement();
                _binding.addComplexTypeBinding(tempComp);
            }

            //--SimpleTypeBindings
            Enumeration<ComponentBindingType> sts = (Enumeration<ComponentBindingType>)loaded.enumerateSimpleTypeBinding();
            while (sts.hasMoreElements()) {
                ComponentBindingType tempComp = sts.nextElement();
                _binding.addSimpleTypeBinding(tempComp);
            }

            //--groupBindings
            Enumeration<ComponentBindingType> groups = (Enumeration<ComponentBindingType>)loaded.enumerateGroupBinding();
            while (groups.hasMoreElements()) {
                ComponentBindingType tempComp = groups.nextElement();
                _binding.addGroupBinding(tempComp);
            }

            //--enumBinding
            Enumeration<ComponentBindingType> enums = (Enumeration<ComponentBindingType>)loaded.enumerateEnumBinding();
            while (enums.hasMoreElements()) {
                ComponentBindingType tempEnum = enums.nextElement();
                _binding.addEnumBinding(tempEnum);
            }

            //--included schemas
            Enumeration<IncludeType> includes = (Enumeration<IncludeType>)loaded.enumerateInclude();
            while (includes.hasMoreElements()) {
                IncludeType tempInclude = includes.nextElement();
                try {
                    loadBinding(tempInclude.getURI());
                } catch (Exception except) {
                    throw new BindingException(except);
                }
            }
        } catch (MarshalException e) {
            throw new BindingException(e);
        } catch (ValidationException e) {
           throw new BindingException(e);
        }
    }

    /**
     * Returns the binding loaded by the BindingLoader.
     *
     * @return the binding loaded by this BindingLoader. This will return null
     *         if no call to loadBinding has been previously made.
     */
    public ExtendedBinding getBinding() {
        return _binding;
    }

    /**
     * Sets the base URL for the binding and related files. If the base URL is
     * known, files can be included using relative names. Any URL can be passed,
     * if the URL can serve as a base URL it will be used.
     *
     * @param url
     *            The base URL
     */
    public void setBaseURL(final String url) {
        try {
            _resolver.setBaseURL(new URL(url));
        } catch (MalformedURLException except) {
            throw new IllegalArgumentException(except.getMessage());
        }
    }

    /**
     * Factory method that returns a binding given an InputSource. The
     * InputSource identifies a Binding Document meant to be loaded.
     *
     * @param source the InputSource identifying the binding document to be
     *        loaded.
     * @return a binding that contains the different component bindings to be
     *         used in the source generator.
     * @throws BindingException thrown when the given InputSource doesn't refer
     *         to a valid Binding document.
     */
    public static ExtendedBinding createBinding(final InputSource source) throws BindingException {
       BindingLoader loader = new BindingLoader();
       loader.loadBinding(source);
       return loader.getBinding();
    }

    /**
     * Factory method for unmarshalling an {@link ExtendedBinding} instance from the 
     * binding file as identified by the given file name. 
     * @param fileName Binding file name.
     * @return An {@link ExtendedBinding} instance populated from the given binding file (name).
     * @throws BindingException If the binding file cannot be processed properly.
     */
    public static ExtendedBinding createBinding(final String fileName) throws BindingException {
        BindingLoader loader = new BindingLoader();
        InputSource source = new InputSource(fileName);
        loader.loadBinding(source);
        return loader.getBinding();
    }

    /**
     * EntityResolver specific to resolving entities related to the Castor XML
     * code generator binding file.
     * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
     */
    class BindingResolver implements EntityResolver {
        
        /**
         * PUBLIC ID for the Castor XML code generator binding file.
         */
        private static final String BINDING_PUBLICID =
            "-//EXOLAB/Castor Binding Schema Version 1.0//EN";
        /**
         * SYSTEM ID for the Castor XML code generator binding file.
         */
        private static final String BINDING_SYSTEMID =
            "http://exolab.castor.org/binding.xsd";
        /**
         * Classpath-based URL to the binding XML schema as shipped in the 
         * Castor XML code generator binary JAR.
         */
        private static final String BINDING_RESOURCE =
            "/org/exolab/castor/builder/binding/binding.xsd";

        /**
         * Base URL, if known.
         */
        private URL _baseUrl;

        /**
         * Sets a base URL for relative processing.
         * @param baseUrl Base URL for relative processing.
         */
        public void setBaseURL(final URL baseUrl) {
            _baseUrl = baseUrl;
        }

        /**
         * Returns the base URL for relative processing.
         * @return base URL for relative processing
         */
        public URL getBaseURL() {
            return _baseUrl;
        }

        /**
         * Code adapted from DTDResolver written by Assaf Arkin.
         *
         * @param publicId The public identifier of the external entity being
         *        referenced, or null if none was supplied.
         * @param systemId The system identifier of the external entity being
         *        referenced.
         * @return An InputSource object describing the new input source, or
         *         null to request that the parser open a regular URI connection
         *         to the system identifier.
         * @throws org.xml.sax.SAXException Any SAX exception, possibly wrapping
         *         another exception.
         * @throws java.io.IOException A Java-specific IO exception, possibly
         *         the result of creating a new InputStream or Reader for the
         *         InputSource.
         * @see org.exolab.castor.util.DTDResolver#resolveEntity(java.lang.String,
         *      java.lang.String)
         * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
         *      java.lang.String)
         */
        public InputSource resolveEntity(final String publicId, final String systemId)
                                                               throws IOException, SAXException {
            InputSource source = null;

            // First, resolve the schema if any
            if  (publicId != null && publicId.equals(BINDING_PUBLICID)) {
                source =  new InputSource(getClass().getResourceAsStream(BINDING_RESOURCE));
                source.setPublicId(publicId);
                return source;
            }

            if  (systemId != null && systemId.equals(BINDING_SYSTEMID)) {
                source =  new InputSource(getClass().getResourceAsStream(BINDING_RESOURCE));
                source.setSystemId(systemId);
                return source;
            }

            // Can't resolve public id, but might be able to resolve relative
            // system id, since we have a base URI.
            if (systemId != null && _baseUrl != null) {
                URL url;
                try {
                    url = new URL(systemId);
                    source = new InputSource(url.openStream());
                    source.setSystemId(systemId);
                    return source;
                } catch (MalformedURLException except) {
                    try {
                        url = new URL(_baseUrl, systemId);
                        source = new InputSource(url.openStream());
                        source.setSystemId(systemId);
                        return source;
                    } catch (MalformedURLException ex2) {
                        throw new SAXException(ex2);
                    }
                }
            }
            // No resolving.
            return null;
        }

    } //--BindingResolver

}

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
 * Copyright 2001 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.binding;

//-Castor imports
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

//--SAX
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

//--java imports
import java.io.IOException;
import java.util.Enumeration;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * This class is responsible for loading a binding document into an in-memory
 * representation that is meant to be used by the SourceGenerator.
 *
 * TODO:  Implement the enumeration handling
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public class BindingLoader {

    /**
     * The Source Generator Binding File loaded by this BindingLoader.
     */
    private ExtendedBinding _binding;

    /**
     * The binding resolver used for resolving entities.
     */
    private BindingResolver _resolver = new BindingResolver();

    public BindingLoader() {
    }

    public void loadBinding(String url) throws BindingException {
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
     *             thrown when an error occured during the unmarshalling.
     */
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
            Enumeration packages = loaded.enumeratePackage();
            while (packages.hasMoreElements()) {
                PackageType tempPackage = (PackageType) packages.nextElement();
                _binding.addPackage(tempPackage);
            }

            //--NamingXML
            NamingXMLType naming = loaded.getNamingXML();
            if (naming != null) {
                _binding.setNamingXML(naming);
            }

            //--elementBindings
            Enumeration elements = loaded.enumerateElementBinding();
            while (elements.hasMoreElements()) {
                ComponentBindingType tempComp = (ComponentBindingType) elements.nextElement();
                _binding.addElementBinding(tempComp);
            }

            //--attributeBindings
            Enumeration attributes = loaded.enumerateAttributeBinding();
            while (attributes.hasMoreElements()) {
                ComponentBindingType  tempComp = (ComponentBindingType) attributes.nextElement();
                _binding.addAttributeBinding(tempComp);
            }

            //--ComplexTypeBindings
            Enumeration complexTypes = loaded.enumerateComplexTypeBinding();
            while (complexTypes.hasMoreElements()) {
                ComponentBindingType tempComp = (ComponentBindingType) complexTypes.nextElement();
                _binding.addComplexTypeBinding(tempComp);
            }

            //--groupBindings
            Enumeration groups = loaded.enumerateGroupBinding();
            while (groups.hasMoreElements()) {
                ComponentBindingType tempComp = (ComponentBindingType) groups.nextElement();
                _binding.addGroupBinding(tempComp);
            }

            //--enumBinding
            //--The following is not yet implemented in the Source Generator
//          Enumeration enums = loaded.enumerateEnumBinding();
//          while (enums.hasMoreElements()) {
//              EnumBinding tempEnum = (EnumBinding)enums.nextElement();
//              _binding.addEnumBinding(tempEnum);
//          }

            //--included schemas
            Enumeration includes = loaded.enumerateInclude();
            while (includes.hasMoreElements()) {
                IncludeType tempInclude = (IncludeType) includes.nextElement();
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
     * Returns the binding loaded by the BindingLoader
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
     * @param source
     *            the InputSource identifying the binding document to be loaded.
     * @return a binding that contains the different component bindings to be
     *         used in the source generator.
     * @throws BindingException
     *             thrown when the given InputSource doesn't refer to a valid
     *             Binding document.
     */
    public static ExtendedBinding createBinding(final InputSource source) throws BindingException {
       BindingLoader loader = new BindingLoader();
       loader.loadBinding(source);
       return loader.getBinding();
    }

    public static ExtendedBinding createBinding(final String fileName) throws BindingException {
        BindingLoader loader = new BindingLoader();
        InputSource source = new InputSource(fileName);
        loader.loadBinding(source);
        return loader.getBinding();
    }

    class BindingResolver implements EntityResolver {
        private static final String BINDING_PUBLICID = "-//EXOLAB/Castor Binding Schema Version 1.0//EN";
        private static final String BINDING_SYSTEMID = "http://exolab.castor.org/binding.xsd";
        private static final String BINDING_RESOURCE = "/org/exolab/castor/builder/binding/binding.xsd";

        /**
         * Base URL, if known.
         */
        private URL            _baseUrl;

        public void setBaseURL(final URL baseUrl) {
            _baseUrl = baseUrl;
        }

        public URL getBaseURL() {
            return _baseUrl;
        }

        /**
         * Code adapted from DTDResolver written by Assaf Arkin.
         *
         * @param publicId
         *            The public identifier of the external entity being
         *            referenced, or null if none was supplied.
         * @param systemId
         *            The system identifier of the external entity being
         *            referenced.
         * @return An InputSource object describing the new input source, or
         *         null to request that the parser open a regular URI connection
         *         to the system identifier.
         * @throws org.xml.sax.SAXException
         *             Any SAX exception, possibly wrapping another exception.
         * @throws java.io.IOException
         *             A Java-specific IO exception, possibly the result of
         *             creating a new InputStream or Reader for the InputSource.
         * @see org.exolab.castor.util.DTDResolver#resolveEntity(java.lang.String,
         *                                                       java.lang.String)
         * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
         *                                               java.lang.String)
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

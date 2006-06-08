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

package org.exolab.castor.util;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.exolab.castor.net.util.URIUtils;

/**
 * Entity resolver for various DTD/schema. Holds information and performs
 * resolving on a variety of DTD and schema, both those defined by Castor and
 * those used by Castor and cached by it.
 * <p>
 * The following DTD and XML schema are supported:
 * <ul>
 * <li>Castor mapping DTD/Schema
 * <li>Castor JDO configuration DTD/Schema
 * <li>XML Schema DTDs
 * </ul>
 * <p>
 * Thie resolver can resolve both public and system identifiers, and will return
 * an input stream into a cached resource in the Castor JAR.
 * <p>
 * This resolver can be used as wrapper to another entity resolver. For example,
 * if a resolver is used for external entities in the mapping file, construct a
 * new resolver using the {@link #DTDResolver(EntityResolver)} constructor.
 * <p>
 *
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class DTDResolver implements EntityResolver {

    /**
     * Holds information about a given DTD of XML Schema.
     */
    static class DTDInfo {

        /**
         * The public identifier. Null if unknown.
         */
        private final String publicId;

        /**
         * The system identifier. Null if unknown.
         */
        private final String systemId;

        /**
         * The resource name, if a copy of the document is available.
         */
        private final String resource;

        /**
         * Creates an instance of DTDInfo.
         *
         * @param publicId public id
         * @param systemId system id
         * @param namespace namespace
         * @param prefix prefix
         * @param resource resource
         */
		DTDInfo(final String publicId,
                final String systemId,
                final String namespace,
                final String prefix,
                final String resource) {
            this.publicId = publicId;
            this.systemId = systemId;
            this.resource = resource;
        }
    }

    /**
     * Defines information about a variety of DTDs, both those defined by Castor
     * and those defined elsewhere and cached by Castor.
     */
    private final DTDInfo[] _dtdInfo = new DTDInfo[] {
            // Information for Mapping DTD and schema
            new DTDInfo("-//EXOLAB/Castor Mapping DTD Version 1.0//EN",
                    "http://castor.exolab.org/mapping.dtd",
                    "castor.exolab.org", "castor",
                    "/org/exolab/castor/mapping/mapping.dtd"),
            new DTDInfo("-//EXOLAB/Castor Mapping Schema Version 1.0//EN",
                    "http://castor.exolab.org/mapping.xsd",
                    "castor.exolab.org", "castor",
                    "/org/exolab/castor/mapping/mapping.xsd"),
            new DTDInfo("-//EXOLAB/Castor Mapping DTD Version 1.0//EN",
                    "http://castor.org/mapping.dtd", "castor.org", "castor",
                    "/org/exolab/castor/mapping/mapping.dtd"),
            new DTDInfo("-//EXOLAB/Castor Mapping Schema Version 1.0//EN",
                    "http://castor.org/mapping.xsd", "castor.org", "castor",
                    "/org/exolab/castor/mapping/mapping.xsd"),
            // Information for JDO configuration DTD and schema
            new DTDInfo(
                    "-//EXOLAB/Castor JDO Configuration DTD Version 1.0//EN",
                    "http://castor.exolab.org/jdo-conf.dtd",
                    "castor.exolab.org", "castor",
                    "/org/castor/jdo/conf/jdo-conf.dtd"),
            new DTDInfo(
                    "-//EXOLAB/Castor JDO Configuration Schema Version 1.0//EN",
                    "http://castor.exolab.org/jdo-conf.xsd",
                    "castor.exolab.org", "castor",
                    "/org/castor/jdo/conf/jdo-conf.xsd"),
            new DTDInfo(
                    "-//EXOLAB/Castor JDO Configuration DTD Version 1.0//EN",
                    "http://castor.org/jdo-conf.dtd", "castor.org", "castor",
                    "/org/castor/jdo/conf/jdo-conf.dtd"),
            new DTDInfo(
                    "-//EXOLAB/Castor JDO Configuration Schema Version 1.0//EN",
                    "http://castor.org/jdo-conf.xsd", "castor.org", "castor",
                    "/org/castor/jdo/conf/jdo-conf.xsd"),
            // Resolving for XML Schema DTDs
            new DTDInfo(
                    "-//W3C//DTD XMLSCHEMA 19991216//EN",
                    "http://www.w3.org/TR/2000/WD-xmlschema-1-20000225/structures.dtd",
                    null, null,
                    "/org/exolab/castor/util/resources/structures.dtd"),
            new DTDInfo(
                    null,
                    "http://www.w3.org/TR/2000/WD-xmlschema-2-20000225/datatypes.dtd",
                    null, null,
                    "/org/exolab/castor/util/resources/datatypes.dtd"),
            new DTDInfo(
                    null,
                    "http://www.w3.org/TR/2000/WD-xmlschema-1-20000225/structures.xsd",
                    null, null,
                    "/org/exolab/castor/util/resources/structures.xsd"),

    };

    /**
     * The wrapped resolver.
     */
    private EntityResolver _resolver;

    /**
     * Base URL, if known.
     */
    private URL _baseUrl;

    /**
     * Constructs a new DTD resolver. This resolver wraps another resolver and
     * will delegate all resolving not related to the Castor mapping files to
     * that resolver. The wrapper resolver will typically be used for entities
     * appearing in the actual mapping file.
     */
    public DTDResolver(EntityResolver resolver) {
        _resolver = resolver;
    }

    /**
     * Constructs a new DTD resolver.
     */
    public DTDResolver() {
    }

    /**
     * Sets the base URL to use.
     * @param baseUrl Base URL.
     */
    public void setBaseURL(final URL baseUrl) {
        _baseUrl = baseUrl;

        // //-- make sure we have a document base and not
        // //-- a full URL to an actual file
        // if (baseUrl != null) {
        // String urlString = baseUrl.toExternalForm();
        // String docBase = URIUtils.getDocumentBase(urlString);
        // if (urlString.equals(docBase)) {
        // _baseUrl = baseUrl;
        // }
        // else if ((docBase != null) && (docBase.length() > 0)) {
        // try {
        // _baseUrl = new URL(docBase);
        // }
        // catch(MalformedURLException mue) {
        // // TODO: bubble up exception instead of
        // // rethrowing
        // String error = "Malformed URL: " + docBase;
        // throw new IllegalStateException(error);
        // }
        // }
        // else {
        // _baseUrl = null;
        // }
        // }
    }

    /**
     * Returns the base URL in use.
     * @return The base URL.
     */
    public URL getBaseURL() {
        return _baseUrl;
    }

    /**
     * Resolves public&szstem ids to files stored within the JAR.
     *
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String,
     *      java.lang.String)
     */
	public InputSource resolveEntity(final String publicId,
            final String systemId) throws IOException, SAXException {
        int i;
        InputSource source = null;

        // First, resolve all the DTD/schema.
        for (i = 0; i < _dtdInfo.length; ++i) {
            if (publicId != null && publicId.equals(_dtdInfo[i].publicId)) {
                source = new InputSource(getClass().getResourceAsStream(
                        _dtdInfo[i].resource));
                source.setPublicId(publicId);
                return source;
            }
            if (systemId != null && systemId.equals(_dtdInfo[i].systemId)) {
                source = new InputSource(getClass().getResourceAsStream(
                        _dtdInfo[i].resource));
                source.setSystemId(systemId);
                return source;
            }
        }

        // If a resolver was specified, use it.
        if (_resolver != null) {
            source = _resolver.resolveEntity(publicId, systemId);
            if (source != null) {
                return source;
            }
        }

        // Can't resolve public id, but might be able to resolve relative
        // system id, since we have a base URI.
        if (systemId != null && _baseUrl != null) {
            URL url;

            try {
                url = new URL(_baseUrl, systemId);
                source = new InputSource(url.openStream());
                source.setSystemId(systemId);
                return source;
            } catch (MalformedURLException except) {
                try {
                    String absURL = URIUtils.resolveAsString(systemId, _baseUrl
                            .toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(systemId);
                    return source;
                } catch (MalformedURLException ex2) {
                    // nothing to do
                }
            } catch (java.io.FileNotFoundException fnfe) {
                try {
                    String absURL = URIUtils.resolveAsString(systemId, _baseUrl
                            .toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(systemId);
                    return source;
                } catch (MalformedURLException ex2) {
                    // nothing to do
                }
            }
            return null;
        }

        // No resolving.
        return null;
    }

}

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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.net.util;

import org.exolab.castor.net.URIResolver;
import org.exolab.castor.net.URIException;
import org.exolab.castor.net.URILocation;

/**
 * The default implementation of URIResolver
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
**/
public final class URIResolverImpl implements URIResolver {

    private static final String HTTP_PROTOCOL = "http:";
    private static final String FILE_PROTOCOL = "file:";

    public URIResolverImpl() {
        super();
    } //-- URIResolver;

	/**
	 * Resolves the given href and documentBase.
     * The href can either be an absolute or a relative URI as defined by
     * IETF RFC 2396:
     * <ul>
     *    <li><b>Absolute</b> URI: [scheme]:'//'[authority][absolute path]</li>
     *    <li><b>Relative</b> URI: '//'[authority][absolute path] or [relative path]
     *    or [absolute path].</li>
     * </ul>
     *
     * <p><b>Note</b>:
     * <ol>
     *   <li>When [authority] is not used then '//' must not be used.</li>
     *   <li>An [absolute path] begins by a '/' sign.</li>
     * </ol>
     * For instance the following URIs are valid:
     * <blockquote>
     *    <pre>
     *        file:/c:/Program Files/MyApp/myresource.xml
     *        file://usr/etc/myresource.xml
     *        http://www.castor.org/index.html
     *        ../relative/index.html
     *    </pre>
     * </blockquote>
     * However, the following <b>won't</b> be valid:
     * <blockquote>
     *    <pre>
     *        file://c:/Program Files/MyApp/myresource.xml
     *    </pre>
     * </blockquote>
	 *
	 * @return the URILocation for the URI
	**/
	public URILocation resolve(String href, String documentBase)
	    throws URIException
	{
	    URILocation uriLocation = null;
        boolean stripHostSeparator = (java.io.File.separatorChar == '\\');
        //--true if the path in the href is absolute
        boolean absolute = false;

        //1--Is there  a scheme? If yes the URI is absolute.
        //NOTE: only HTTP and File protocols are currently supported.
        if ( href.startsWith(HTTP_PROTOCOL) || href.startsWith(FILE_PROTOCOL) ) {
            absolute = true;
        }
        //2-- handle relative URIs: '//'<authority>[absolute path]
        //or [relative path] or [absolute path]
        if (!absolute) {
            if (stripHostSeparator) {
                if (href.startsWith("//"))
                    absolute = true;
            } else if (href.startsWith("///")) {
                absolute = true;
            }

        }

        //3--[relative path]
        if (!absolute) {

             if (href.startsWith("./"))
                href = href.substring(2);

             //--resolve using the document base
             if (documentBase != null) {

                //--resolve the previous directory
                while (href.startsWith("../")) {
                    href = href.substring(3);
                    documentBase = documentBase.substring(0,documentBase.lastIndexOf('/'));
                    documentBase = documentBase.substring(0,documentBase.lastIndexOf('/')+1);
                }
             }//--documentBase != null
        }

	    try {
            uriLocation = new URILocationImpl(href, documentBase);
	    }
	    catch (RuntimeException ex) {
	        throw new URIException(ex.getMessage(), ex);
	    }
	    return uriLocation;
	} //-- resolve

	/**
	 * Resolves the given urn. An implementation of this
	 * method may return null if the URN could not be resolved.
	 *
	 * @return the URILocation for the URN
	 */
	public URILocation resolveURN(String urn)
	    throws URIException
	{
	    return null;
	} //-- resolveURN

} //-- URIResolver

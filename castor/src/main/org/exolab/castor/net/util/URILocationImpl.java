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

import org.exolab.castor.net.URILocation;
import java.io.*;

/**
 * An implementation of URILocation
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class URILocationImpl extends URILocation {

    private String _documentBase = null;
    private String _absoluteURI  = null;
    private String _relativeURI  = null;


    private Reader      _reader = null;
    private InputStream _is     = null;


    /**
     * Creates a new URILocationImpl
    **/
    public URILocationImpl(String href) {
        this(href, null);
    } //-- URILocationImpl

    /**
     * Creates a new URILocationImpl
    **/
    public URILocationImpl(String href, String documentBase) {
        if (href == null)
            throw new IllegalStateException("href must not be null");
        _absoluteURI = URIUtils.resolveAsString(href, documentBase);
    } //-- URILocationImpl


    /**
     * Creates a new URILocationImpl
    **/
    public URILocationImpl(Reader reader, String href) {
        this(href, null);
        _reader = reader;
    } //-- URILocationImpl

    /**
     * Creates a new URILocationImpl
    **/
    public URILocationImpl(InputStream is, String href) {
        this(href, null);
        _is = is;
    } //-- URILocationImpl



	/**
	 * Returns the absolute URI for this URILocation
	 *
	 * @return the absolute URI for this URILocation
	 * @see #getRelativeURI
	 * @see #getBaseURI
	**/
	public String getAbsoluteURI() {
	    return _absoluteURI;
	} //-- getAbsoluteURI

	/**
	 * Returns the base location of this URILocation.
	 * If this URILocation is an URL, the base location
	 * will be equivalent to the document base for the URL.
	 *
	 * @return the base location of this URILocation
	 * @see #getAbsoluteURI
	 * @see #getRelativeURI
	**/
	public String getBaseURI() {
	    if (_documentBase == null)
	        _documentBase = URIUtils.getDocumentBase(_absoluteURI);
	    return _documentBase;
	} //-- getBaseURI

	/**
	 * Returns a Reader for the resource represented
	 * by this URILocation.
	 *
	 * @return a Reader for the resource represented by
	 * this URILocation
	 * @exception java.io.FileNotFoundException
	 * @exception java.io.IOException
	**/
	public Reader getReader() throws java.io.IOException {

	    if (_reader != null)
	        return _reader;
	    if (_is != null)
	        return new InputStreamReader(_is);

	    return URIUtils.getReader(_absoluteURI, null);
	} //-- getReader

	/**
	 * Returns the relative URI for this URILocation
	 *
	 * @return the relative URI for this URILocation
	 * @see #getAbsoluteURI
	 * @see #getBaseURI
	**/
	public String getRelativeURI() {

	    if (_relativeURI == null) {
	        int idx = getBaseURI().length();
	        _relativeURI = _absoluteURI.substring(idx);
	    }

	    return _relativeURI;

	} //-- getRelativeURI

	/**
	 * Returns the String representation of
	 * this URILocation.
	 *
	 * @return the String representation of this URILocation
	**/
	public String toString() {
	    return getAbsoluteURI();
	}

} //-- URILocationImpl

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


package servlet;


import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import javax.servlet.http.HttpServletResponse;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Method;


/**
 * Extends {@link HttpServletResponse} with additional methods for
 * producing XML through SAX events, and controlling the output method
 * and XSL transformation.
 * <p>
 * A Servlet sending XML events should set up the output format and
 * stylesheet prior to calling {@link #getDocumentHandler}. It may not
 * use an output stream or writer and a {@link DocumentHandler} at the
 * same time.
 * <p>
 * <b>Note:</b> This interface depends on a proposed serializer API.
 * Pending the public release of the serializer API, this interface
 * depends on implementation classes found in Xerces. This interfaces
 * will be revised shortly to comply with the serializer API without
 * incurring any functional changes.
 * <p>
 * <b>Note:</b> This interface is based on the SAX 1 API. A future
 * version will add support for the SAX 2 API.
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public interface XMLServletResponse
    extends HttpServletResponse
{


    /**
     * Returns the document handler for sending SAX events (SAX 1).
     * <p>
     * Subsequent calls to this method will return the same document
     * handler. An attempt to call this method after an output stream
     * or writer has been obtained will result in an {@link
     * IllegalStateException}.
     *
     * @return The document handler
     * @throws IOException An I/O exception creating a document handler
     * @throws SAXException An error occured creating a document handler
     * @throws IllegalStateException If this method was called after
     *  a call to {@link #getOutputStream} or {@link #getWriter}
     */
    public DocumentHandler getDocumentHandler()
        throws IOException, SAXException, IllegalStateException;

 
    /**
     * Sets the stylesheet for use for transformation.
     * <p>
     * The stylesheet is provided as a path to a resource.
     *
     * @param path The stylesheet path
     * @throws IOException Could not access the stylesheet
     * @throws IllegalStateException If this method was called after
     *  a call to {@link #getDocumentHandler}
     */
    public void setStylesheet( String path )
        throws IOException, IllegalStateException;


    /**
     * Sets the stylesheet for use for transformation.
     * <p>
     * The stylesheet is provided a SAX  an input source.
     *
     * @param source The stylesheet
     * @throws IOException Could not access the stylesheet
     * @throws IllegalStateException If this method was called after
     *  a call to {@link #getDocumentHandler}
     */
    public void setStylesheet( InputSource source )
        throws IOException, IllegalStateException;


    /**
     * Sets the output format to use for serializing the document.
     * <p>
     * The output format determines the output method to use (XML,
     * HTML, etc) and various other properties that affect the
     * generated document.
     * <p>
     * The output format affects serialization and overrides the
     * <tt>xsl:output</tt> properties of the stylesheet in use.
     * If not output format is specified the default XML output
     * format is used for serialization, or based on the
     * <tt>xsl:output</tt> properties specified in the stylesheet.
     *
     * @param format The output format
     * @throws IllegalStateException If this method was called after
     *  a call to {@link #getDocumentHandler}
     */
    public void setOutputFormat( OutputFormat format )
        throws IllegalStateException;


    /**
     * Sets the output method to use for serializing the document.
     * <p>
     * The output method determines whether the document is produced
     * as XML, HTML, etc. For a list of the default output methods
     * see {@link org.apache.xml.serialize.Method}.
     * <p>
     * Equivalent to calling {@link #setOutputFormat} with only
     * the output method property set.
     *
     * @param method The output method to use
     * @throws IllegalStateException If this method was called after
     *  a call to {@link #getDocumentHandler}
     */
    public void setOutputMethod( String method )
        throws IllegalStateException;


}

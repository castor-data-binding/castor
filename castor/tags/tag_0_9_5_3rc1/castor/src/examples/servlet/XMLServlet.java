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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.SerializerFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Method;
import org.exolab.adaptx.xslt.*;
import org.exolab.adaptx.xslt.handlers.ResultHandlerAdapter;
import org.exolab.adaptx.xslt.util.SAXInput;
import org.exolab.adaptx.net.impl.URILocationImpl;

/**
 * Extends {@link HttpServlet} to provide XML output capabilities.
 * <p>
 * Derived classes should implement {@link service(HttpServletRequest,XMLServletResponse)}
 * and use {@link XMLServletResponse} to produce XML output and
 * optionally apply XSLT transformations on it.
 *
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class XMLServlet
    extends HttpServlet
{


    /**
     * Holds the context of the servlet, used to locate XSLT stylesheets
     * as resource or get the stylesheet path from the parameters.
     */
    private ServletContext  _ctx;

    private static XSLTProcessor    _xslp;
    
    /**
     * Servlets that implement this method should call it.
     */
    public void init( ServletConfig config )
    {
        _ctx = config.getServletContext();
        _xslp = new XSLTProcessor();
    }


    /**
     * @deprecated
     */
    protected final void service( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        service( request, new XMLServletResponseImpl( _ctx, response ) );
    }


    /**
     * Recieved standards HTTP requests and processes them, using
     * XML SAX events as the output mechanism.
     *
     * @param request The {@link HttpServletRequest} object that contains
     *  the request the client has made to the servlet
     * @param response The {@link XMLServletResponse} object that contains
     *  the response the servlet is sending to the client
     * @throws IOException If an I/O exception occured while the Servlet
     *  is handling the request
     * @throws ServletException If the servlet request cannot be handled
     */
    protected abstract void service( HttpServletRequest request, XMLServletResponse response )
        throws ServletException, IOException;



    /**
     * Implementation of an {@link XMLServletResponse} wrappr around a
     * {@link HttpServletResponse} object.
     */
    static final class XMLServletResponseImpl
        implements XMLServletResponse
    {


        private DocumentHandler   _docHandler;


        private XSLTStylesheet   _stylesheet;


        private OutputFormat      _format;


        private final HttpServletResponse _response;

        
        private final ServletContext      _ctx;

        private final XSLTReader           _xslReader;
        

        XMLServletResponseImpl( ServletContext ctx, HttpServletResponse response )
        {
            String param;

            _ctx = ctx;
            _response = response;
            param = _ctx.getInitParameter( "xsl:stylesheet" );
            if ( param != null )
                setStylesheet( param );
            param = _ctx.getInitParameter( "xsl:output-method" );
            if ( param != null )
                setOutputFormat( new OutputFormat( param, null, "true".equals( _ctx.getInitParameter( "xsl:output-indent" ) ) ) );
                
            _xslReader = new XSLTReader();
        }


        public DocumentHandler getDocumentHandler()
            throws IOException, SAXException
        {
            if ( _docHandler != null )
                return _docHandler;
                
            ResultHandler _resultHandler = null;
            
            if ( _format == null ) {
                
                XMLSerializer ser;
                _format = new OutputFormat( Method.XML, _response.getCharacterEncoding(), false );
                ser = new XMLSerializer( _format );
                ser.setOutputCharStream( getWriter() );
                _resultHandler = new ResultHandlerAdapter(ser.asDocumentHandler());
            } else {
                Serializer ser;
                
                _format.setEncoding( _response.getCharacterEncoding() );
                ser = SerializerFactory.getSerializerFactory( _format.getMethod() ).makeSerializer( _format );
                ser.setOutputCharStream( getWriter() );
                _resultHandler = new ResultHandlerAdapter(ser.asDocumentHandler());
            }
            _response.setContentType( "text/" + _format.getMethod() );

            if ( _stylesheet != null ) {
                SAXInput saxInput = new SAXInput();
                saxInput.setProcessor(_xslp);
                saxInput.setOutputHandler(_resultHandler);
                saxInput.setStylesheet(_stylesheet);
                _docHandler = saxInput;
            }
            else _docHandler = _resultHandler;
            
            return _docHandler;
        }


        public void setStylesheet( String path )
        {
            
            //-- why?
            if ( _docHandler != null )
                throw new IllegalStateException( "Cannot set stylesheet after obtaining document handler" );
            
            try {
                _stylesheet = _xslReader.read(path);
            }
            catch(Exception ex) {
                throw new IllegalArgumentException(ex.toString());
            }
            //_stylesheet = new XSLTInputSource( _ctx.getResourceAsStream( path ) );
            
        }
 

        public void setStylesheet( InputSource source )
        {
            //-- why?
            if ( _docHandler != null )
                throw new IllegalStateException( "Cannot set stylesheet after obtaining document handler" );
                
            String uri = source.getSystemId();
            try {
                URILocationImpl location 
                    = new URILocationImpl(source.getCharacterStream(), uri);
                _stylesheet = _xslReader.read(location);
            }
            catch(Exception ex) {
                throw new IllegalArgumentException(ex.toString());
            }
            //_stylesheet = new XSLTInputSource( source );
        }


        public void setOutputFormat( OutputFormat format )
        {
            if ( _docHandler != null )
                throw new IllegalStateException( "Cannot set stylesheet after obtaining document handler" );
            _format = format;
        }


        public void setOutputMethod( String method )
        {
            if ( _docHandler != null )
                throw new IllegalStateException( "Cannot set stylesheet after obtaining document handler" );
            if ( _format != null )
                _format.setMethod( method );
            else
                _format = new OutputFormat( method, null, false );
        }


        public String getCharacterEncoding()
        {
            return _response.getCharacterEncoding();
        }


        public ServletOutputStream getOutputStream()
            throws IOException
        {
            return _response.getOutputStream();
        }


        public PrintWriter getWriter()
            throws IOException
        {
            return _response.getWriter();
        }


        public void setContentLength( int length )
        {
            _response.setContentLength( length );
        }


        public void setContentType( String type )
        {
            _response.setContentType( type );
        }


        public void setBufferSize( int size )
        {
            _response.setBufferSize( size );
        }


        public int getBufferSize()
        {
            return _response.getBufferSize();
        }


        public void flushBuffer()
            throws IOException
        {
            _response.flushBuffer();
        }


        public boolean isCommitted()
        {
            return _response.isCommitted();
        }


        public void reset()
        {
            _response.reset();
        }


        public void setLocale( Locale locale )
        {
            _response.setLocale( locale );
        }


        public Locale getLocale()
        {
            return _response.getLocale();
        }


        public void addCookie( Cookie cookie )
        {
            _response.addCookie( cookie );
        }


        public boolean containsHeader( String name )
        {
            return _response.containsHeader( name );
        }


        public String encodeURL( String url )
        {
            return _response.encodeURL( url );
        }


        public String encodeRedirectURL( String url )
        {
            return _response.encodeRedirectURL( url );
        }


        /**
         * @deprecated
         */
        public String encodeUrl( String url )
        {
            return _response.encodeURL( url );
        }


        /**
         * @deprecated
         */
        public String encodeRedirectUrl( String url )
        {
            return _response.encodeRedirectURL( url );
        }


        public void sendError( int sc, String msg )
            throws IOException
        {
            _response.sendError( sc, msg );
        }


        public void sendError( int sc )
            throws IOException
        {
            _response.sendError( sc );
        }


        public void sendRedirect( String location )
            throws IOException
        {
            _response.sendRedirect( location );
        }


        public void setDateHeader( String name, long date )
        {
            _response.setDateHeader( name, date );
        }


        public void addDateHeader( String name, long date )
        {
            _response.addDateHeader( name, date );
        }


        public void setHeader( String name, String value )
        {
            _response.setHeader( name, value );
        }


        public void addHeader( String name, String value )
        {
            _response.addHeader( name, value );
        }


        public void setIntHeader( String name, int value )
        {
            _response.setIntHeader( name, value );
        }


        public void addIntHeader( String name, int value )
        {
            _response.addIntHeader( name, value );
        }


        public void setStatus( int sc )
        {
            _response.setStatus( sc );
        }


        /**
         * @deprecated
         */
        public void setStatus( int sc, String msg )
        {
            _response.setStatus( sc );
        }


    }


}


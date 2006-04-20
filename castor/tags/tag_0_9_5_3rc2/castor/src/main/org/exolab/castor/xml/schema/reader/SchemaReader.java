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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.castor.xml.schema.reader;

import java.io.Reader;
import java.io.IOException;

import org.exolab.castor.net.URIException;
import org.exolab.castor.net.URILocation;
import org.exolab.castor.net.URIResolver;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.NestedIOException;

import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.XMLException;

import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


import org.apache.xml.serialize.Serializer;

/**
 * A class for reading XML Schemas
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SchemaReader {

    /**
     * The Castor Configuration 
     */
    private Configuration _config = null;

    /**
     * XML Parser instance
     */
    private Parser      _parser   = null;
    
    /**
     * SAX InputSource to Schema
     */
    private InputSource _source   = null;
    
    /**
     * SAX EntityResolver
     */
    private EntityResolver _resolver = null;
    
    /**
     * SAX ErrorHandler
     */
    private ErrorHandler _errorHandler = null;


     /**
     * The resolver to be used for resolving href
     */
    private URIResolver _uriResolver;

    private Schema      _schema   = null;

    private boolean     _validate = true;

    
    /**
     * Creates a new SchemaReader
     */
    private SchemaReader()
        throws IOException
    {
        //-- get default parser from Configuration
        
        _config = LocalConfiguration.getInstance();
        
        Parser parser = null;
        
	    parser = _config.getParser();

        if (parser == null) {
            throw new IOException("fatal error: unable to create SAX parser.");
        }

        _parser = parser;
    } //-- SchemaReader

    /**
     * Creates a new SchemaReader for the given InputSource
     *
     * @param source the InputSource to read the Schema from.
    **/
    public SchemaReader(InputSource source)
        throws IOException
    {
        this();

        if (source == null)
            throw new IllegalArgumentException("InputSource cannot be null");

        _source = source;

    } //-- SchemaReader

    /**
     * Creates a new SchemaReader for the given Reader
     *
     * @param reader the Reader to read the Schema from.
     * @param filename for reporting errors.
    **/
    public SchemaReader(Reader reader, String filename)
        throws IOException
    {
        this();

        if (reader == null) {
            String err = "The argument 'reader' must not be null.";
            throw new IllegalArgumentException(err);
        }

        _source = new InputSource(reader);
        if (filename == null) filename = reader.toString();
        _source.setPublicId(filename);

    } //-- SchemaReader

    /**
     * Creates a new SchemaReader for the given URL
     *
     * @param url the URL string
    **/
    public SchemaReader(String url)
        throws IOException
    {
        this();
        if (url == null) {
            String err = "The argument 'url' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _source = new InputSource(url);

    } //-- SchemaReader

    /**
     * Reads the Schema from the source and returns the Schema
     * object model.
     *
     * <BR />
     * <B>Note:</B> Subsequent calls to this method will simply
     * return a cached copy of the Schema object. To read a new
     * Schema object, create a new Reader.
     *
     * @return the new Schema created from the source of this SchemaReader
    **/
    public Schema read() throws IOException
    {
        if (_schema != null) return _schema;

        SchemaUnmarshaller schemaUnmarshaller = null;

        try {
            SchemaUnmarshallerState state = new SchemaUnmarshallerState();            
            state.setConfiguration(_config);            
            schemaUnmarshaller = new SchemaUnmarshaller(state);
            if (_uriResolver != null)
                schemaUnmarshaller.setURIResolver(_uriResolver);
            
            //-- make sure we mark the URI as processed for cyclic
            //-- imports/includes
            String uri = _source.getSystemId();
            if (uri != null) {
                URIResolver resolver = schemaUnmarshaller.getURIResolver();
                try {
                    URILocation location = resolver.resolve(uri, null);
                    if (location != null) uri = location.toString();
                }
                catch(URIException except) {
                    throw new NestedIOException(except);
                }
                state.markAsProcessed(uri, schemaUnmarshaller.getSchema());
            }
            
            Sax2ComponentReader handler
                = new Sax2ComponentReader(schemaUnmarshaller);
            _parser.setDocumentHandler(handler);
            
            if (_errorHandler == null)
                _parser.setErrorHandler(handler);
            else
                _parser.setErrorHandler(_errorHandler);
                
            if (_resolver != null)
                _parser.setEntityResolver(_resolver);
            _parser.parse(_source);
        }
        catch(XMLException ex) {
            handleException(ex);
        }
        catch(org.xml.sax.SAXException sx) {
            handleException(sx);
        }

        _schema = schemaUnmarshaller.getSchema();

        if (_validate) {
            try {
                _schema.validate();
            }
            catch(org.exolab.castor.xml.ValidationException vx) {
                throw new NestedIOException(vx);
            }
        }

        return _schema;

    } //-- read

    /**
     * Sets the ErrorHandler.
     *
     * @param ErrorHandler.
    **/
    public void setErrorHandler(ErrorHandler errorHandler) {
        _errorHandler = errorHandler;
    } //-- setErrorHandler

    /**
     * Sets whether or not post-read validation should
     * occur. By default, validation is enabled. Note
     * that certain read validation cannot be disabled.
     *
     * @param validate a boolean that when true will force
     * a call to Schema#validate after the schema is read.
    **/
    public void setValidation(boolean validate) {
        _validate = validate;
    } //-- setValidation


    /**
     * Sets the EntityResolver used to resolve SYSTEM Identifier.
     * If the entity resolver is null, the default one will be used.
     *
     * @param resolver the EntityResolver to use.
     */
    public void setEntityResolver(EntityResolver resolver) {
        _resolver = resolver;
    }

    /**
     * Sets the URIResolver used to resolve hrefs.
     * If the entity resolver is null, the default one will be used.
     *
     * @param resolver the URIResolver to use.
     */
    public void setURIResolver(URIResolver uriresolver) {
        _uriResolver = uriresolver;
    }

    private void handleException(XMLException xx)
        throws IOException
    {
        Exception except = xx.getException();
        if (except == null) {
            except = xx;
        }
        else if (except instanceof SAXException) {
            handleException((SAXException)except);
        }
        else if (except instanceof XMLException) {
            handleException((XMLException)except);
        }

        throw new NestedIOException(except);
    } //-- handleException

    private void handleException(SAXException sx)
        throws IOException
    {
        Exception except = sx.getException();
        if (except == null) {
            except = sx;
        }
        else if (except instanceof SAXParseException) {
            SAXParseException spe = (SAXParseException)except;
            String filename = spe.getSystemId();
            if (filename == null) filename = spe.getPublicId();
            if (filename == null) filename = "<filename unavailable>";

            String err = spe.getMessage();

            err += "; " + filename + " [ line: " + spe.getLineNumber();
            err += ", column: " + spe.getColumnNumber() + ']';
            throw new NestedIOException(err, except);
        }
        else if (except instanceof XMLException) {
            handleException((XMLException)except);
        }

        throw new NestedIOException(except);

    } //-- handleException

} //-- SchemaReader

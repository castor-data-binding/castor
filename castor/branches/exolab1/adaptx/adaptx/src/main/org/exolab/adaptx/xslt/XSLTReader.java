/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */

package org.exolab.adaptx.xslt;

import org.exolab.adaptx.net.URIResolver;
import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.URIException;
import org.exolab.adaptx.net.impl.URIResolverImpl;
import org.exolab.adaptx.net.impl.URILocationImpl;
import org.exolab.adaptx.xslt.util.*;
import org.exolab.adaptx.xml.parser.DOMParser;
import org.exolab.adaptx.xml.XMLUtil;
import org.exolab.adaptx.xml.DOM2SAX;
import org.exolab.adaptx.util.ErrorObserverAdapter;
import org.exolab.adaptx.util.ErrorObserver;

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * A class for reading an XSLT stylesheet from a stream or file.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 *
 * <PRE>
 *  Modifcations
 *    19990804: Mike Los (comments with MEL)
 *        - modified #readDocument to close InputStream
 * </PRE>
**/
public class XSLTReader extends ErrorObserverAdapter {
    
    
    private static final String _MISSING_DOCUMENT_ELEMENT_ERR
        = "invalid stylesheet, missing document element.";
          
    private static final String _UNABLE_TO_CREATE_PARSER_ERR
        = "unable to create a SAX or DOM based XML parser.";
        

    /**
     * The DOMParser to use when reading a DOM document
    **/
    private DOMParser _domParser = null;
    
    /**
     * The default ErrorObserver to use when
     * none are specified
    **/
    private static ErrorObserver _errorObserver = 
        new DefaultObserver();
        
    /**
     * A flag indicating whether or not we
     * need to remove the default ErrorObserver
     * from our list of ErrorObservers
    **/
    private boolean _removeDefaultErrorObserver = true;
    
    private URIResolver _resolver = null;
    
      //------------------/
     //- Public Methods -/
    //------------------/


    /**
     * Creates a new Default XSLTReader
    **/
    public XSLTReader() {
        super();
        super.addErrorObserver(_errorObserver);
    } //-- XSLTReader
    
    public XSLTReader(URIResolver uriResolver) {
        this();
        _resolver = uriResolver;
    } //-- XSLTReader
    
    /**
     * Adds the given ErrorObserver to the list of ErrorObservers
     * for this XSLReader
     * @param observer the ErrorObserver to add
    **/
    public void addErrorObserver(ErrorObserver observer) {

        if (observer == null) return;
        if (_removeDefaultErrorObserver) {
            removeErrorObserver(_errorObserver);
            _removeDefaultErrorObserver = false;
        }
        super.addErrorObserver(observer);
    } //-- addErrorObserver
     
    /** 
     * Returns the URIResolver being used by this XSLReader
     *
     * @return the URIResolver being used by this XSLReader
    **/
    public URIResolver getURIResolver() {
        if (_resolver == null) {
            _resolver = new URIResolverImpl();
        }
        return _resolver;   
    } //-- URIResolver
    
    /**
     * Reads the XSLStylesheet pointed to by the given URL
     * @param url the URL of the stylesheet
     * @return the XSLStylesheet
    **/
    public XSLTStylesheet read(URL url)
        throws XSLException, java.io.IOException
        
    {
        InputStream is = url.openStream();
        Reader reader = new InputStreamReader(is);
        return read(new URILocationImpl(reader, url.toString()));
    } //-- read(URL)
    
    
    
    /**
     * Reads an XSL stylesheet using the given DOM Document
     * @param Document the DOM Document that is the Stylesheet
     * @param filename the full path and filename of the Stylesheet
     *  which is used for resolving relative URIs.
    **/
    public XSLTStylesheet read(Document document, String filename) 
        throws XSLException
    {
        
        StylesheetHandler handler = new StylesheetHandler(this);
        XSLTStylesheet stylesheet = handler.getStylesheet();
        if (filename != null) {
            stylesheet.setURILocation(new URILocationImpl(filename));
        }
        if (document == null) return stylesheet;
        
        // get document element and make sure it's
        // the xsl:stylesheet element
        Element root = document.getDocumentElement();
        if (root == null)
            throw new XSLException(_MISSING_DOCUMENT_ELEMENT_ERR); 
            
        try {
            DOM2SAX.process(document, handler);
        }
        catch(org.xml.sax.SAXException sx) {
            
            String message = sx.getMessage();
            Exception ex = sx.getException();
            if (ex != null) {
                if (message == null) message = ex.getMessage();
                if (ex instanceof SAXParseException) {
                    //-- add better support for SAXParseException
                }
            }
            if (message == null) {
                message = "XSLReader: stylesheet parse error.";
            }
            throw new XSLException(message);
        }
        return stylesheet;
    } //-- read(Document, filename)
    
    
    /**
     * Reads an XSL stylesheet from the given uri (filename)
     * @param uri the file name of the XSLT stylesheet to read
     * @return the new XSLStylesheet
     * @exception XSLException
    **/
	public XSLTStylesheet read(InputSource source) 
	    throws XSLException, java.io.IOException 
    {
	    Parser parser = Configuration.getSAXParser();
        if (parser == null) {
            throw new XSLException("Unable to create SAX Parser!");
        }
        String href = source.getSystemId();
        if (href == null) href = source.getPublicId();
        URILocation location = new URILocationImpl(href);
        return read(source, location, parser);
	} //-- read(String)

    /**
     * Reads an XSL stylesheet from the given uri (filename)
     * @param uri the file name of the XSLT stylesheet to read
     * @return the new XSLStylesheet
     * @exception XSLException
    **/
	public XSLTStylesheet read(String uri) 
	    throws XSLException, java.io.IOException 
    {
        return read(uri, null);
	} //-- read(String)
	
    /**
     * Reads an XSL stylesheet from the given uri, using the
     * given documentBase to resolve relative URI's.
     * @param uri the file name of the XSLT stylesheet to read
     * @return the new XSLStylesheet
     * @exception XSLException
    **/
    public XSLTStylesheet read(String uri, String documentBase) 
        throws XSLException, java.io.IOException
    {
        URILocation location = null;
        
        try {
            location = getURIResolver().resolve(uri, documentBase);
        }
        catch (URIException exception) {
            receiveError(exception, ErrorObserver.FATAL);
        }
            
        return read(location);
        
    } //-- read(filename, documentBase)
    
    /**
     * Reads an XSLStylesheet from the given URILocation
     *
     * @param location the URILocation of the XSLT stylesheet
     * @return the new XSLStylesheet
     * @exception XSLException
    **/
	public XSLTStylesheet read(URILocation location) 
	    throws XSLException, java.io.IOException 
	{
        //-- Try SAX first
        Parser parser = Configuration.getSAXParser();
        if (parser != null) {
            InputSource is = new InputSource();
            is.setSystemId(location.getAbsoluteURI());
            is.setCharacterStream(location.getReader());
            return read(is, location, parser);
        }
        DOMParser domParser = Configuration.getDOMParser();
        if (domParser == null) {
            String err = "XSLReader: " + _UNABLE_TO_CREATE_PARSER_ERR;
            throw new XSLException(err);
        }
        Document document = domParser.readDocument(location, this);
        return read(document,location.getAbsoluteURI());
	} //-- read


    /**
     * Sets the URIResolver for this XSLReader
     *
     * @param resolver the URIResolver this XSLReader should
     * use for resolving all URIs.
    **/
    public void setURIResolver(URIResolver resolver) {
        _resolver = resolver;   
    } //-- URIResolver
    
    private XSLTStylesheet read
        (InputSource source, URILocation location, Parser parser) 
        throws XSLException, java.io.IOException
    {
        StylesheetHandler handler = new StylesheetHandler(this);
        XSLTStylesheet stylesheet = handler.getStylesheet();
        stylesheet.setURILocation(location);
        parser.setDocumentHandler(handler);
        try {
            parser.parse(source);
        }
        catch(SAXException sx) {
                    
            SAXParseException sxp = null;
            Exception nested = sx.getException();
                    
            if (sx instanceof SAXParseException)
                sxp = (SAXParseException)sx;
            else if ((nested != null) && 
                        (nested instanceof SAXParseException)) 
                sxp = (SAXParseException)nested;
                    
            if (sxp != null) {
                StringBuffer err = new StringBuffer(sxp.toString());
                err.append("\n - ");
                err.append(sxp.getSystemId());
                err.append("; line: ");
                err.append(sxp.getLineNumber());
                err.append(", column: ");
                err.append(sxp.getColumnNumber());
                        
                receiveError(err.toString());
                throw new XSLException(err.toString());
            }
            else {
                receiveError(sx.toString());
                throw new XSLException(sx.toString());
            }
        }
        
        return stylesheet;
    } //-- read
    
} //-- XSLReader

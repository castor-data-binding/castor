/*
 * (C) Copyright Keith Visco 2003  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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

package org.exolab.adaptx.xml.parser;

import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.xslt.util.DefaultObserver;

//-- DOM
import org.w3c.dom.Document;

//-- SAX
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//-- JAXP 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Creates a generic JAXP DOM Parser
 *
 * @author <A HREF="mailto:keith@kvisco.com">Keith Visco</A>
 * @version $Revision$ $Date$
 */
public class JAXPDOMParser 
    implements org.exolab.adaptx.xml.parser.DOMParser 
{

    /**
     * The DocumentBuilderFactory instance
     */
    private DocumentBuilderFactory _factory = null;
    
    /**
     * An instance of a DocumentBuilder, used only for
     * creating instances of a new Document
     */
    private DocumentBuilder        _builder = null;
    
    
    //----------------/    
    //- Constructors -/
    //----------------/    
    
    /**
     * Creates a new instance of the JAXPDOMParser wrapper
     */
    public JAXPDOMParser() {
        super();
        _factory = DocumentBuilderFactory.newInstance();
        _factory.setNamespaceAware(false);
    } //-- JAXPDOMParser

    /**
     * Private constructor
     */
    private JAXPDOMParser(DocumentBuilderFactory factory) {
        _factory = factory;
    } //-- JAXPDOMParser
    
    //---------------------------------------------------------/    
    //- Interfaces for org.exolab.adaptx.xml.parser.DOMParser -/
    //---------------------------------------------------------/    

    /**
     * Creates a new copy of this DOMParser initialized
     * with the same properties as this DOMParser.
     *
     * @return the new DOMParser instance
     */
    public DOMParser copyInstance() {
        return new JAXPDOMParser(_factory);
    } //-- copyInstance
    
    /**
     * Sets the DocumentType for the given document
     *
     * @param document the Document to set the document type in
     * @param systemId the systemId for the document type
     */
    public void setDocumentType(Document document, String systemId) {
        //-- do nothing for now
    } //-- setDocumentType
    
    /**
     * Sets whether or not to Validate the Document
     * @param validate a boolean indicating whether or not to
     * validate the Document
     **/
    public void setValidation(boolean validate) {
        _factory.setValidating(validate);
    } //-- setValidation
    
    /**
     * Creates a DOM Document 
     *
     * @return the new Document
     */
    public Document createDocument() {
        
        if (_builder == null) {
            try {
                _builder = _factory.newDocumentBuilder();
            }
            catch(ParserConfigurationException pcx) {
                String err = "An exception of type '" + pcx.getClass().getName();
                err += "' occurred while attempting to create a new Document " +
                    "instance; " + pcx.getMessage();
                throw new IllegalStateException(err);
            }
        }
        return _builder.newDocument();
    } //-- createDocument
    
    /**
     * Reads an XML Document from the given Reader
     * @param reader the Reader for reading the XML stream
     * @param filename
     * @param observer the ErrorObserver for notification of errors
     **/
    public Document readDocument
        (URILocation location, ErrorObserver observer) 
    {
        
        
	    Document doc = null;
	    try {
            DocumentBuilder builder = _factory.newDocumentBuilder();
	        InputSource is = new InputSource(location.getReader());
	        is.setSystemId(location.getAbsoluteURI()); //-- important for resolving paths
	        doc = builder.parse(is);
	    }
	    catch (ParserConfigurationException pcx) {
            String err = "An exception of type '" + pcx.getClass().getName();
            err += "' occurred while attempting to read document '" +
                location.getAbsoluteURI() + "';\n " + pcx.getMessage();
	        if (observer != null)
	            observer.receiveError(err, observer.FATAL);
	        else 
	            System.out.println(err);
	    }
	    catch (java.io.IOException iox) {
	        String err = "error while trying to read document '";
	        err += location.getAbsoluteURI() + "';\n " + iox.toString();
	        
	        if (observer != null)
	            observer.receiveError(err, observer.FATAL);
	        else 
	            System.out.println(err);
	    }
	    catch (SAXException sx) {
	        String err = "error while trying to read document '";
	        err += location.getAbsoluteURI() + "';\n " + sx.getMessage();
	        
	        if (sx instanceof SAXParseException) {
	            SAXParseException spx = (SAXParseException)sx;
	            err += "\n   -- error occured at line ";
	            err += spx.getLineNumber();
	            err += ", column " + spx.getColumnNumber();
	        }
	        if (observer != null)
	            observer.receiveError(err, observer.FATAL);
	        else 
	            System.out.println(err);
	    }
        return doc;
		
    } //-- readDocument    
    
} //-- DOMParser


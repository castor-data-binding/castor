/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
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

package org.exolab.adaptx.xml.parser;

import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.xslt.util.DefaultObserver;

import org.w3c.dom.*;
import org.xml.sax.*;      
import java.io.Reader;


/**
 * Creates a DOM Package for use with Apache's Xerces parser.
 * @author <A HREF="mailto:kvisco@ziplink.net">Keith Visco</A>
 * @version $Revision$ $Date$
**/
public class XercesParser 
    implements org.exolab.adaptx.xml.parser.DOMParser 
{

    private org.apache.xerces.parsers.DOMParser parser = null;    
    
    private boolean validate = false;
    
    private boolean initialized = false;
    
    //----------------/    
    //- Constructors -/
    //----------------/    
    
    /**
     * Instantiate parser
     */
    public XercesParser() {
        super();
    } //-- XercesParser

    /**
     * Instantiate parser and read catalog
     *
     */
    private void init() {
	    parser = new org.apache.xerces.parsers.DOMParser();
	    try {
	        parser.setFeature("http://xml.org/sax/features/validation",validate);
	    }
	    catch (java.lang.Exception ex) {};
	    
	    //parser.setCreateEntityReferenceNodes(false);
	    
	    //parser.setNodeExpansion(NonValidatingDOMParser.FULL);
	    //SimpleCatalog catalog=new 
	        //SimpleCatalog(System.getProperty("xml.catalog"));
	    //parser.getEntityHandler().setEntityResolver(catalog);
    } //-- initParser
    
    //---------------------------------------------------------/    
    //- Interfaces for org.exolab.adaptx.xml.parser.DOMParser -/
    //---------------------------------------------------------/    

    /**
     * Creates a new copy of this DOMParser initialized
     * with the same properties as this DOMParser.
     * @return the new DOMParser instance
    **/
    public DOMParser copyInstance() {
        XercesParser xp = new XercesParser();
        xp.validate = this.validate;
        return xp;
    } //-- copyInstance
    
    /**
     * Creates a DOM DocumentType using the DOM package of this DOMReader
     * @return the new DocumentType
     **/
    public void setDocumentType(Document document, String systemId) {
        //-- do nothing for now
    } //-- setDocumentType
    
    /**
     * Sets whether or not to Validate the Document
     * @param validate a boolean indicating whether or not to
     * validate the Document
     **/
    public void setValidation(boolean validate) {
        this.validate    = validate;
        this.initialized = false;
        //parser.setValidation(validate);
    } //-- setValidation
    
    /**
     * Creates a DOM Document 
     * @return the new Document
     **/
    public Document createDocument() {
        return new org.apache.xerces.dom.DocumentImpl();
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
        if (!initialized) init();
        
	    Document doc = null;
	    try {
	        InputSource is = new InputSource(location.getReader());
	        is.setSystemId(location.getAbsoluteURI()); //-- important for resolving paths
	        parser.parse(is);
	        doc = parser.getDocument();
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


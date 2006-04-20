/*
 * (C) Copyright Keith Visco 1999, 2000  All rights reserved.
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

import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.net.URILocation;
import org.w3c.dom.*;
import java.io.Reader;
import java.io.PrintWriter;

/**
 * This class is the basic interface the different DOM parsers need
 * to implement for XSL:P support. 
**/
public interface DOMParser {


    /**
     * Creates a new copy of this DOMParser initialized
     * with the same properties as this DOMParser.
     * @return the new DOMParser instance
    **/
    public DOMParser copyInstance();
    
    /**
     * Creates a DOM Document for this DOMPackage
     * @return the new Document
    **/
    public Document createDocument();

    
    /**
     * Reads an XML Document from the given Reader.
     *
     * @param uriLocation the URILocation of the document to read
     * @param errorWriter the PrintWriter to write all errors to
    **/
    public Document readDocument
        (URILocation uriLocation, ErrorObserver observer);

    /**
     * Sets the DocumentType for the given Document. The
     * Document must be a Document supported by this
     * DOMPackage
    **/
    public void setDocumentType(Document document, String systemId);

    /**
     * Sets whether or not to Validate the Document
     * @param validate a boolean indicating whether or not to
     * validate the Document
    **/
    public void setValidation(boolean validate);

} //-- DOMParser




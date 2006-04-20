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

package org.exolab.adaptx.xslt;


/**
 * A class for maintaining state information for the output
 * of the XSL result tree
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @since XSLT 19990813 (XSL:P version 19990928)
 * @version $Revision$ $Date$
**/
public interface OutputFormat {
    
    
    /**
     * Returns the Public Id that should be used for the Doctype
     * @return the Public Id that should be used for the Doctype,
     * or null if none has been set
    **/
    public String getDoctypePublicId();
    
    /**
     * Returns the System Id that should be used in the Doctype
     * @return the System Id that should be used for the Doctype,
     * or null if none has been set
    **/
    public String getDoctypeSystemId();
    
    /**
     * Returns whether or not indenting the result is allowed
     * @return true if whitespace may be added to the output result
     * for indentation and readability, otherwise returns false
    **/
    public boolean getIndent();
    
    
    /**
     * Returns the output method 
     * <BR />
     * Predefined output methods are: xml, html, and text
     * @return the output method
    **/
    public String getMethod();
    
    /**
     * Returns whether or not the XML declaration should be supressed when
     * serializing the result
     * @return true if the XML declaration should be supressed when
     * serializing the result
    **/
    public boolean getOmitXMLDeclaration();
    
    /**
     * Returns the XML version that should be output during serialization
     * of the result tree
     * @return the XML version that should be used during serialization of
     * of the result tree
    **/
    public String getVersion();
    
    /**
     * Returns whether or not indenting the result is allowed
     * @return true if whitespace may be added to the output result
     * for indentation and readability, otherwise returns false
    **/
    public void setIndent(boolean allowIndentation);
    
    /**
     * Sets the output method 
     * @param method, the output method of this xsl:output object
     * <BR />
     * Predefined output methods are: xml, html, and text
    **/
    public void setMethod(String method);


    /**
     * Sets whether or not the XML declaration should be supressed when
     * serializing the result
     * @param omitDeclaration, the flag indicating whether or not the
     * XML declaration should be supressed when
     * serializing the result. Use true if you do NOT want the XML Declaration
     * to appear in the output. The default value depends on the Method.
     * If the method is "xml", this value will be false by default.
     * If the method is "html", this value will be true by default.
    **/
    public void setOmitXMLDeclaration(boolean omitDeclaration);
    
    /**
     * Sets the version of the XML output (eg "1.0")
     * @param version, the xml version to output
     * <BR />
    **/
    public void setVersion(String version);

    /**
     * Sets the Public Id that should be used for the Doctype
     * @param publicId the Public Id that should be used for the Doctype
    **/
    public void setDoctypePublicId(String publicId);
    
    /**
     * Sets the System Id that should be used in the Doctype
     * @param systemId the System Id for the Doctype
    **/
    public void setDoctypeSystemId(String systemId);
    
    
} //-- Output

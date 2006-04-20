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
public class XSLOutput extends XSLObject implements OutputFormat {
    
    
    /**
     * Creates a new Output with the given parent Stylesheet
    **/
    public XSLOutput() {
        super(XSLObject.OUTPUT);
    } //-- XSLOutput
    
    
    /**
     * Returns the Public Id that should be used for the Doctype
     * @return the Public Id that should be used for the Doctype,
     * or null if none has been set
    **/
    public String getDoctypePublicId() {
        return getAttribute(Names.DOCTYPE_PUBLIC_ATTR);
    } //-- getDoctypePublicId
    
    /**
     * Returns the System Id that should be used in the Doctype
     * @return the System Id that should be used for the Doctype,
     * or null if none has been set
    **/
    public String getDoctypeSystemId() {
        return getAttribute(Names.DOCTYPE_SYSTEM_ATTR);
    } //-- getDoctypeSystemId
    
    /**
     * Returns whether or not indenting the result is allowed
     * @return true if whitespace may be added to the output result
     * for indentation and readability, otherwise returns false
    **/
    public boolean getIndent() {
        String indent = getAttribute(Names.INDENT_ATTR);
        if ((indent == null) || (indent.length() == 0)) {
            if ("html".equalsIgnoreCase(getMethod()))
                return true;
        }
        return "yes".equals(indent);
    } //-- getIndent
    
    /**
     * Returns the output method 
     * <BR />
     * Predefined output methods are: xml, html, and text
     * @return the output method
    **/
    public String getMethod() {
        return getAttribute(Names.METHOD_ATTR);
    }
    
    /**
     * Returns whether or not the XML declaration should be supressed when
     * serializing the result
     * @return true if the XML declaration should be supressed when
     * serializing the result
    **/
    public boolean getOmitXMLDeclaration() {
        String omit = getAttribute(Names.OMIT_XML_DECL_ATTR);
        if (omit == null) {
            // calculate default
            if ("html".equals(getMethod())) return true;
            else return false;
        }
        return "yes".equals(omit);
    } //-- getOmitXMLDeclaration
    
    /**
     * Returns the XML version that should be output during serialization
     * of the result tree
     * @return the XML version that should be used during serialization of
     * of the result tree
    **/
    public String getVersion() {
        
        return getAttribute(Names.VERSION_ATTR);
    } //-- getVersion
    
    
    /**
     * Sets the Public Id that should be used for the Doctype
     * @param publicId the Public Id that should be used for the Doctype
    **/
    public void setDoctypePublicId(String publicId) {
        try {
            setAttribute(Names.DOCTYPE_PUBLIC_ATTR, publicId);
        }
        catch (XSLException xsle) {
            //-- do nothing
        }
    } //-- setDoctypePublicId
    
    /**
     * Sets the System ID that should be used in the Doctype
     * @param systemId the System ID for the Doctype
    **/
    public void setDoctypeSystemId(String systemId) {
        try {
            setAttribute(Names.DOCTYPE_SYSTEM_ATTR, systemId);
        }
        catch (XSLException xsle) {
            //-- do nothing
        }
    } //-- setDoctypeSystemId
    
    /**
     * Returns whether or not indenting the result is allowed
     * @return true if whitespace may be added to the output result
     * for indentation and readability, otherwise returns false
    **/
    public void setIndent(boolean allowIndentation) {
        try {
            String indent = null;
            if (allowIndentation) indent = "yes";
            else indent = "no";
            setAttribute(Names.INDENT_ATTR, indent);
        }
        catch (XSLException xsle) {
            //-- do nothing
        }
    } //-- setIndent
    
    /**
     * Sets the output method 
     * @param method, the output method of this xsl:output object
     * <BR />
     * Predefined output methods are: xml, html, and text
    **/
    public void setMethod(String method) {
        try {
            setAttribute(Names.METHOD_ATTR, method);
        }
        catch(XSLException xslex) {};
    } //-- setMethod

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
    public void setOmitXMLDeclaration(boolean omitDeclaration) {
        try {
            String omit = null;
            if (omitDeclaration) omit = "yes";
            else omit = "no";
            setAttribute(Names.OMIT_XML_DECL_ATTR, omit);
        }
        catch (XSLException xsle) {
            //-- do nothing
        }
    }

    /**
     * Sets the version of the XML output (eg "1.0")
     * @param version, the xml version to output
     * <BR />
     * Predefined output methods are: xml, html, and text
    **/
    public void setVersion(String version) {
        try {
            setAttribute(Names.VERSION_ATTR, version);
        }
        catch(XSLException xslex) {};
    } //-- setVersion
    
} //-- Output

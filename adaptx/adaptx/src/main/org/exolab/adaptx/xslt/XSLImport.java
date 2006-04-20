/*
 * (C) Copyright Keith Visco 1998  All rights reserved.
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 */

package org.exolab.adaptx.xslt;

import org.exolab.adaptx.net.impl.URIUtils;
import org.w3c.dom.*;

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * This class represents an xsl:import or xsl:include
 * XSLElement.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class XSLImport extends XSLObject {
    
    /**
     * an int value indicating the xsl:import type 
    **/
    public static final int IMPORT   = 0;
    
    /**
     * an int value indicating the xsl:include type 
    **/
    public static final int INCLUDE  = 1;

    /**
     * The type of XSLImport
    **/
    private int type = IMPORT;
    
    /**
     * The type of XSLImport
    **/
    private XSLTStylesheet stylesheet = null;
    
      //---------------/
     //- Constructor -/
    //---------------/
    
    /**
     * Creates a new XSLImport
    **/
    public XSLImport() {
        super(XSLObject.IMPORT);
    } //-- XSLImport
    
    /**
     * Creates a new XSLImport
    **/
    public XSLImport(XSLTStylesheet stylesheet) {
        this();
        this.stylesheet = stylesheet;
        try {
            setAttribute(Names.HREF_ATTR, stylesheet.getHref());
        }
        // we won't get an exception here since 'href' is
        // a valid attribute for XSLImport
        catch(XSLException xslException) {};
    } //-- XSLImport
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Retrieves the href attribute of this XSLImport
     * @return the href String of this XSLImport
    **/
	public String getHref() {
	    return getAttribute(Names.HREF_ATTR);
	} //-- getHref
	
	/**
	 * Returns the Stylesheet that this Import references
	 * @return the XSLStylesheet that this import references
	**/
	public XSLTStylesheet getStylesheet() {
	    return this.stylesheet;
	} //-- getStylesheet
	
	/**
	 * Returns an InputStream for the file represented by the href
	 * of this XSLImport
	 * @param documentBase the document base for resolving relative
	 * URIs.
	 * @return an InputStream for the file represented by this
	 * elements href attribute
	**/
	public InputStream getInputStream(String documentBase) 
	    throws java.net.MalformedURLException, 
	        java.io.FileNotFoundException, java.io.IOException
	{
	    return URIUtils.getInputStream(getHref(), documentBase);
	} //-- getInputStream
	
	/**
	 * Sets the Stylesheet that this Import references
	 * @param stylesheet the XSLStylesheet that this Import references
	**/
	public void setStylesheet(XSLTStylesheet stylesheet) {
	    this.stylesheet = stylesheet;
	    try {
	        setAttribute(Names.HREF_ATTR, stylesheet.getHref());
	    }
	    catch(XSLException xslException) {};
	} //-- setStylesheet
	
} //-- XSLImport

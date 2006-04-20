/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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


package org.exolab.adaptx.xslt.dom;

import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.impl.URILocationImpl;
import org.exolab.adaptx.xslt.util.Configuration;
import org.exolab.adaptx.util.NestedIOException;
import org.exolab.adaptx.xml.DOMURILocation;
import org.exolab.adaptx.xml.SAXURILocation;
import org.exolab.adaptx.xslt.util.SAXInput;


import org.xml.sax.*;

/**
 * A Utility class to read an XPathNode, using the org.exolab.adaptx.xslt.dom 
 * package as the implementation of XPathNode.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XPNReader {
    
    
    private static final String REUSE_ERR
        = "This XPNReader was previously used. Please create "+
          "a new one.";
          
    private URILocation _location = null;
    
    /**
     * A handle to the SAX parser
    **/
    private Parser _parser = null;
    
    /**
     * A flag indicating to save location information in
     * the XPathNode tree.
    **/
    private boolean _saveLocation = false;
    
    private boolean _usable = true;

    /**
     * Creates a new XPNReader for the given URILocation.
     *
     * @param location the URILocation to create this reader for.
    **/
    public XPNReader(URILocation location) 
        throws java.io.IOException
    {
        super();
        if (location == null) {
            String err = "The argument 'location' may not be null.";
            throw new IllegalArgumentException(err);
        }
        _location = location;
        initParser();
    } //-- XPNReader
    
    /**
     * Creates a new XPNReader for the given URILocation.
     *
     * @param location the URILocation to create this reader for.
    **/
    public XPNReader(String url) 
        throws java.io.IOException
    {
        super();
        if (url == null) {
            String err = "The argument 'url' may not be null.";
            throw new IllegalArgumentException(err);
        }
        _location = new URILocationImpl(url);
        initParser();
    } //-- XPNReader
    
    private void initParser() 
        throws java.io.IOException
    {
		_parser = Configuration.getSAXParser();
		if (_parser == null) {
		    throw new NestedIOException("unable to create SAX parser.");
		}
    } //-- initParser
    
	/**
	 * Reads an XML Document into an XPathNode from the given URILocation
	 *
	 * @return the XPathNode
	**/
	public XPathNode read() 
	    throws java.io.IOException
	{
	    
	    synchronized(this) {
	        if (!_usable) {
	            throw new java.io.IOException(REUSE_ERR);
	        }
	        _usable = false;
	    }
	    
	    XMLReader reader = null;
	    InputSource source = null;
	    if (_location instanceof SAXURILocation) {
	        SAXURILocation saxLocation = (SAXURILocation)_location;
	        reader = saxLocation.getXMLReader();
	        source = saxLocation.getInputSource();
	    }
	    
		XPathNode node = null;
		
		
        try {
            //-- create InputSource if necessary
            if (source == null) {
                source = new InputSource();
                source.setSystemId(_location.getAbsoluteURI());
                source.setCharacterStream(_location.getReader());
            }
            
            if (reader != null) {
                SAXInput saxInput = new SAXInput(_saveLocation);
                reader.setContentHandler(saxInput);
                reader.parse(source);
                node = saxInput.getRoot();
            }
            else {
		        XPNBuilder builder = new XPNBuilder();
		        builder.setSaveLocation(_saveLocation);
                _parser.setDocumentHandler(builder);
                _parser.parse(source);
                node = builder.getRoot();
            }
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
                throw new NestedIOException(err.toString(), sx);
            }
            else {
                throw new NestedIOException(sx);
            }
        }
        Root root = (Root) node;
        root.setDocumentURI(_location.getAbsoluteURI());
		return root;
	    
	} //-- read
	
    /**
     * Sets whether or not to save location information. Location
     * information can only be saved if the Locator has been
     * set by the SAX Parser.
     *
     * @param saveLocation a boolean that when true, indicates that location
     * information should be saved if possible.
    **/
    public void setSaveLocation(boolean saveLocation) {
        _saveLocation = saveLocation;
    } //-- setSaveLocation
	
} //-- XPNReader

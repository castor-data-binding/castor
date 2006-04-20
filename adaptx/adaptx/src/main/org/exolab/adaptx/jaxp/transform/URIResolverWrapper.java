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

package org.exolab.adaptx.jaxp.transform;    
   
//-- Adaptx    
import org.exolab.adaptx.net.URIException;
import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.impl.URILocationImpl;
import org.exolab.adaptx.xml.DOMURILocation;
import org.exolab.adaptx.xml.SAXURILocation;

//-- JAXP
import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

//-- SAX
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

//-- java
import java.io.Reader;
import java.io.InputStream;

/**
 * An Adaptx wrapper for JAXP URIResolver 
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class URIResolverWrapper 
    implements org.exolab.adaptx.net.URIResolver 
{
        
    private URIResolver _resolver = null;
        
    public URIResolverWrapper(URIResolver resolver) {
        _resolver = resolver;
    }
        
    /**
        * Returns the "wrapped" URIResolver
        *
        * @return the wrapped URIResolver
        */
    public URIResolver getResolver() {
        return _resolver;
    }
        
	/**
	    * Resolves the given href and documentBase. An 
	    * implementation of this method should never return
	    * null. A URIException may be thrown instead.
	    *
	    * @return the URILocation for the URI. [not null]
	    */
	public URILocation resolve(String href, String documentBase)
	    throws URIException
	{
	    Source source = null;
	    try {
	        source = _resolver.resolve(href, documentBase);
	    }
	    catch(javax.xml.transform.TransformerException tx) {
	        throw new URIException(tx);
	    }
	    if (source != null) {
	        if (source instanceof StreamSource) {
	            StreamSource ss = (StreamSource)source;
	            Reader reader = ss.getReader();
	            if (reader != null) {
	                return new URILocationImpl(reader, ss.getSystemId());
	            }
	            InputStream is = ss.getInputStream();
	            if (is != null) {
	                return new URILocationImpl(is, ss.getSystemId());
	            }
	            return new URILocationImpl(ss.getSystemId(), documentBase);
	        }
	        else if (source instanceof DOMSource) {
	            DOMSource domSource = (DOMSource)source;
	            return new DOMURILocation(domSource.getNode(), domSource.getSystemId());
	        }
	        else if (source instanceof SAXSource) {
	            SAXSource saxSource = (SAXSource)source;
	            XMLReader reader = saxSource.getXMLReader();
	            InputSource is = saxSource.getInputSource();
	            return new SAXURILocation(reader, is);
	        }
	        else {
	            String err = "Error: unsupported javax.xml.transform.Source "+
	                "implementation encountered; ";
	            throw new URIException(err + source.getClass().getName());
	        }
	    }
	    return new URILocationImpl(href, documentBase);
	} //-- resolve
            
} //-- class:URIResolverWrapper

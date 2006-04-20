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
package org.exolab.adaptx.xml;

import org.exolab.adaptx.net.*;
import org.exolab.adaptx.net.impl.URIUtils;
import org.w3c.dom.Node;
import java.io.Reader;

/**
 * An implementation of ObjectURILocation for a DOM Node
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 */
public final class DOMURILocation extends ObjectURILocation {
    
    private String _documentBase = null;
    private String _absoluteURI  = null;
    private String _relativeURI  = null;
    

    private Node _node;    
    
    /**
     * Creates a new DOMURILocation
     */
    public DOMURILocation(Node node, String href) {
        
        if (node == null) {
            String err = "The argument 'node' must not be null.";
            throw new IllegalArgumentException(err);
        }
        
        if (href != null) {
            _absoluteURI = URIUtils.resolveAsString(href, null);
        }
    } //-- URILocationImpl
    
	/**
	 * Returns the absolute URI for this URILocation
	 *
	 * @return the absolute URI for this URILocation
	 * @see getRelativeURI
	 * @see getBaseURI
	 */
	public String getAbsoluteURI() {
	    return _absoluteURI;
	} //-- getAbsoluteURI
    
	/**
	 * Returns the base location of this URILocation.
	 * If this URILocation is an URL, the base location
	 * will be equivalent to the document base for the URL.
	 *
	 * @return the base location of this URILocation
	 * @see getAbsoluteURI
	 * @see getRelativeURI
	 */
	public String getBaseURI() {
	    if (_documentBase == null) {
	        if (_absoluteURI != null) {
	            _documentBase = URIUtils.getDocumentBase(_absoluteURI);
	        }
	    }
	    return _documentBase;
	} //-- getBaseURI
	
	/**
	 * Returns the DOM Node from this URILocation
	 *
	 * @return the DOM Node from this URILocation
	 */
	public Node getNode() {
	    return _node;
	} //-- getNode
	
	/**
	 * Returns the Object from this ObjectURILocation.
	 * 
	 * @return the Object from this ObjectURILocation
	 * @see #getNode
	 */
	public Object getObject() {
	    return getNode();
	} //-- getObject
	
	/**
	 * Returns the reader for this URILocation.
	 * <p>
	 *    This method always return null. Use getObject() or
	 *    getNode() methods instead.
	 * </p>
	 *
	 * @return a Reader for the resource represented by
	 * this URILocation
	 * @exception java.io.FileNotFoundException
	 * @exception java.io.IOException
	 */
	public Reader getReader() throws java.io.IOException {
	    return null;
	} //-- getReader
		
	/**
	 * Returns the relative URI for this URILocation
	 *
	 * @return the relative URI for this URILocation
	 * @see getAbsoluteURI
	 * @see getBaseURI
	 */
	public String getRelativeURI() {
	    
	    if (_relativeURI == null) {
	        if (_absoluteURI != null) {
	            int idx = getBaseURI().length();
	            _relativeURI = _absoluteURI.substring(idx);
	        }
	    }
	    return _relativeURI;
	    
	} //-- getRelativeURI
	
	/**
	 * Returns the String representation of
	 * this URILocation.
	 *
	 * @return the String representation of this URILocation
	 */
	public String toString() {
	    StringBuffer sb =  new StringBuffer("DOMURILocation (");
	    sb.append(System.identityHashCode(_node));
	    sb.append(") at ");
	    if (getAbsoluteURI() != null) {
	        sb.append(getAbsoluteURI());
	    }
	    else {
	        sb.append(" unspecified or unknown URL");
	    }
	    return sb.toString();
	} //-- toString

} //-- DOMURILocation

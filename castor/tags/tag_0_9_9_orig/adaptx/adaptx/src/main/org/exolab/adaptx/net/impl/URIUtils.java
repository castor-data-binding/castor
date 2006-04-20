/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 1998-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.adaptx.net.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * A utility class for URI handling
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 */
public class URIUtils {

    /**
     * The File protocol name
     */
    private static final String FILE_PROTOCOL = "file";
    
    /**
     * the File protocol prefix (name + :// + host + /)
     * where host is empty, meaning local host
     */
    private static final String FILE_PROTOCOL_PREFIX = "file:///";
    
    /**
     * the path separator for an URI
     */
    private static final char HREF_PATH_SEP = '/';
    
    /**
     * The Device separator for an URI
     */
    private static final char DEVICE_SEP = '|';
    
	/**
	 * Returns an InputStream for the file represented by the href
	 * argument
	 * @param href the href of the file to get the input stream for.
	 * @param documentBase the document base of the href argument, if it
	 * is a relative href
	 * set documentBase to null if there is none.
	 * @return an InputStream to the desired resource
	 * @exception java.io.FileNotFoundException when the file could not be
	 * found
	 */
	public static InputStream getInputStream(String href, String documentBase) 
	    throws java.io.FileNotFoundException, java.io.IOException
	{
	    
	    //-- check for URL with only using href
	    URL url = null;
	    if ((url = getURL(href)) != null) {
	        return url.openStream();
	    }
	    
	    //-- join document base + href
	    String absHref = makeAbsolute(href, documentBase);
	    
	    //-- try using absolute URI
	    if ((url = getURL(absHref)) != null) {
	        return url.openStream();
	    }
	    
	    // Try local files
	    File file = new File(href);
	    if (file.exists()) {
	        if (!file.isAbsolute()) {
	            File tmpFile = new File(absHref);
	            if (tmpFile.exists()) file = tmpFile;
	        }
	    }
	    else {
	        file = new File(absHref);
	    }
	    return new FileInputStream(file);
	} //-- getInputStream
	
	/**
	 * Returns an OutputStream for the file represented by the href
	 * argument
	 * @param href the href of the file to get the input stream for.
	 * @param documentBase the document base of the href argument, if it
	 * is a relative href
	 * set documentBase to null if there is none.
	 * @return an OutputStream to the desired resource
	 * @exception java.io.FileNotFoundException when the file could not be
	 * found
	 */
	public static OutputStream getOutputStream(String href, String documentBase) 
	    throws java.io.FileNotFoundException, java.io.IOException
	{
	    
	    URL url = null;
	    
	    //-- try using href alone as it might already be a full URL
	    if ((url = getURL(href)) != null) {
	        
	        //-- If we have a file protocol make sure we
	        //-- don't use connection#getOutputStream
	        //-- as it's not supported.
	        if (FILE_PROTOCOL.equals(url.getProtocol())) {
	            //-- try local file
	            File file = new File(url.getFile());
	            if (file.exists()) {
	                return new FileOutputStream(file);
	            }
	        }
	        else {
	            URLConnection connection = url.openConnection();
	            return connection.getOutputStream();
	        }
	    }
	    
	    //-- try using documentBase
	    String absHref = makeAbsolute(href, documentBase);
	    
	    if ((url = getURL(absHref)) != null) {
	        //-- If we have a file protocol make sure we
	        //-- don't use connection#getOutputStream
	        //-- as it's not supported.
	        if (FILE_PROTOCOL.equals(url.getProtocol())) {
	            //-- try local file
	            File file = new File(url.getFile());
	            if (file.exists()) {
	                return new FileOutputStream(file);
	            }
	        }
	        else {
	            URLConnection connection = url.openConnection();
	            return connection.getOutputStream();
	        }
	    }
	        
	    // Try local files
	    File file = new File(href);
	    if (file.exists()) {
	        if (!file.isAbsolute()) {
	            File tmpFile = new File(absHref);
	            if (tmpFile.exists()) file = tmpFile;
	        }
	    }
	    else {
	        file = new File(absHref);
	    }
	    return new FileOutputStream(file);
	    
	} //-- getOutputStream
	

	/**
	 * Returns a Reader for the file represented by the href
	 * argument
	 * @param href the href of the file to get the input stream for.
	 * @param documentBase the document base of the href argument, if it
	 * is a relative href
	 * set documentBase to null if there is none.
	 * @return an InputStream to the desired resource
	 * @exception java.io.FileNotFoundException when the file could not be
	 * found
	 */
	public static Reader getReader(String href, String documentBase) 
	    throws java.io.FileNotFoundException, java.io.IOException
	{
	    InputStream is = getInputStream(href, documentBase);
        return new InputStreamReader(is);    
	} //-- getReader
	
	/**
	 * Returns a Writer for the file represented by the href
	 * argument
	 * @param href the href of the file to get the input stream for.
	 * @param documentBase the document base of the href argument, if it
	 * is a relative href
	 * set documentBase to null if there is none.
	 * @return a Writer to the desired resource
	 * @exception java.io.FileNotFoundException when the file could not be
	 * found
	 */
	public static Writer getWriter(String href, String documentBase) 
	    throws java.io.FileNotFoundException, java.io.IOException
	{
	    OutputStream is = getOutputStream(href, documentBase);
        return new OutputStreamWriter(is);    
	} //-- getWriter
	
	
	/**
	 * Returns the document base of the href argument
	 * @return the document base of the given href
	 */
	public static String getDocumentBase(String href) {
	    
	    String docBase = "";
	    
	    if (href == null) return docBase;
	    
	    int idx = -1;
	    //-- check for URL
	    try {
	        //-- try to create a new URL and see if MalformedURLExcetion is
	        //-- ever thrown
	        URL url = new URL(href);
	        url = null; //-- to remove compiler warnings
	        idx = href.lastIndexOf(HREF_PATH_SEP);
	    }
	    catch(MalformedURLException muex) {
	        //-- The following contains a fix from Shane Hathaway 
	        //-- to handle the case when both "\" and "/" appear in filename
            int idx2 = href.lastIndexOf(HREF_PATH_SEP);
	        idx = href.lastIndexOf(File.separator);
            if (idx2 > idx) idx = idx2;	        
	    }
	   
	    if (idx >= 0) docBase = href.substring(0,idx);
	    
	    return docBase;
	} //-- getDocumentBase

	/**
	 * Returns the relative URI of the href argument
	 *
	 * @return the relative URI the given href 
	 */
	public static String getRelativeURI(String href) {
	    
	    if (href == null) return href;
	    
	    int idx = -1;
	    //-- check for URL
	    try {
	        //-- try to create a new URL and see if MalformedURLExcetion is
	        //-- ever thrown
	        URL url = new URL(href);
	        url = null; //-- to remove compiler warnings
	        idx = href.lastIndexOf(HREF_PATH_SEP);
	    }
	    catch(MalformedURLException muex) {
	        //-- The following contains a fix from Shane Hathaway 
	        //-- to handle the case when both "\" and "/" appear in filename
            int idx2 = href.lastIndexOf(HREF_PATH_SEP);
	        idx = href.lastIndexOf(File.separator);
            if (idx2 > idx) idx = idx2;	        
	    }
	   
	    if (idx >= 0) 
	        return href.substring(idx+1);
	    
	    return href;
	} //-- getRelativeURI
	
	/**
	 * Returns the given href + documentBase
	 *
	 * @return the absolute URL as a string
	 */
	public static String resolveAsString(String href, String documentBase) {
	    
	    if (getURL(href) != null) {
	        return href;
	    }
	    
	    String absHref = makeAbsolute(href, documentBase);
	    
	    if (getURL(absHref) != null) {
	        return absHref;
	    }
	    
	    // Try local files
	    File file = new File(href);
	    if (file.isAbsolute())
	        absHref = createFileURL(file.getAbsolutePath());
	    else {
	        file = new File(absHref);
	        absHref = createFileURL(file.getAbsolutePath());
	    }
	    return absHref;
	} //-- resolveHref
	
	/**
	 * Creates a File URL for the given file name
	 *
	 * @param filename the name of the file
	 * @return the String representation of the File URL
	 */
	private static String createFileURL(String filename) {
	    
	    if (filename == null) return FILE_PROTOCOL_PREFIX;
	    int size = filename.length() + FILE_PROTOCOL_PREFIX.length();
	    StringBuffer sb = new StringBuffer(size);
	    sb.append(FILE_PROTOCOL_PREFIX);
	    char[] chars = filename.toCharArray();
	    for (int i = 0; i < chars.length; i++) {
	        char ch = chars[i];
	        switch (ch) {
	            case '\\':
	                sb.append(HREF_PATH_SEP);
	                break;
	            default:
	                sb.append(ch);
	                break;
	                
	        }
	    }
	    return sb.toString();
	} //-- createFileURL
    
    /**
     * This is just a helper method so I don't have to write try/catch
     * everywhere I'm testing if something is a URL or not.
     * Returns the URL for the given uri, or null if the uri doesn't
     * exist or is malformed.
     *
     * @return the URL or null.
     */
    private static URL getURL(String uri) {
	    try {
	        return new URL(uri);
	    }
	    catch (MalformedURLException muex) {};
        return null;
    } //-- getURL
    
    /**
     * Returns the absolute URI for the given href
     *
     * @return the absolute URI for the given href
     */
    private static String makeAbsolute(String href, String documentBase) {
        
	    //-- join document base + href
	    String absHref = null;
	    if ((documentBase != null) && (documentBase.length() > 0)) {
            if (href.startsWith(documentBase)) return href;	        
	        int idx = documentBase.lastIndexOf(HREF_PATH_SEP);
	        if (idx == (documentBase.length()-1))
	            absHref = documentBase+href;
	        else
	            absHref = documentBase+HREF_PATH_SEP+href;
	    }   
	    else absHref = href;
	    
	    return absHref;	    
    } //-- makeAbsolute
    
} //-- class: URIUtils

/**
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
 * Copyright 2002-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.net.util;


import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * A utility class for URI handling
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
**/
public class URIUtils {

    /**
     * the File protocol
    **/
    private static final String FILE_PROTOCOL_PREFIX = "file:///";

    /**
     * the path separator for an URI
     */
    private static final char HREF_PATH_SEP = '/';
    
    /**
     * the path separate for a URL as a String
     */
    private static final String URL_PATH_SEP_STR = "/";

    /**
     * The current directory designator
     */
    private static final String CURRENT_DIR_OP = ".";
    
    /**
     * The parent directory designator
     */
    private static final String PARENT_DIR_OP  = "..";
    
	/**
	 * Returns an InputStream for the file represented by the href
	 * argument
	 * @param href the href of the file to get the input stream for.
	 * @param documentBase the document base of the href argument, if it
	 * is a relative href
	 * set documentBase to null if there is none.
	 * @return an InputStream to the desired resource
	 * @throws java.io.FileNotFoundException when the file could not be
	 * found
     * @throws java.io.IOException
	 */
	public static InputStream getInputStream(String href, String documentBase)
	    throws java.io.FileNotFoundException, java.io.IOException
	{

	    //-- check for absolute url
	    URL url = null;
	    try {
	        url = new URL(href);
	        return url.openStream();
	    }
	    catch (MalformedURLException muex) {}

	    //-- join document base + href
	    String xHref = null;
	    if ((documentBase != null) && (documentBase.length() > 0)) {
	        int idx = documentBase.lastIndexOf(HREF_PATH_SEP);
	        if (idx == (documentBase.length()-1))
	            xHref = documentBase+href;
	        else
	            xHref = documentBase+HREF_PATH_SEP+href;
	    }
	    else xHref = href;

	    //-- check for relative url
	    try {
	        url = new URL(xHref);
	        return url.openStream();
	    }
	    catch(MalformedURLException muex) {}

	    // Try local files
	    File iFile = new File(href);
	    if (iFile.isAbsolute()) return new FileInputStream(iFile);
	    else iFile = new File(xHref);

	    return new FileInputStream(iFile);

	} //-- getInputStream

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
	**/
	public static Reader getReader(String href, String documentBase)
	    throws java.io.FileNotFoundException, java.io.IOException
	{
	    InputStream is = getInputStream(href, documentBase);
        return new InputStreamReader(is);
	} //-- getReader

	/**
	 * Returns the document base of the href argument
	 * @return the document base of the given href
	**/
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
	**/
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
     * This method removes "." or ".." from absolute URL.
     * I needed this method because the JDK doesn't do this
     * automatically when creating URLs.
     *
     * @param absoluteURL the absolute URI to normalize
     */
    public static String normalize(String absoluteURL) 
        throws MalformedURLException
    {
        if (absoluteURL == null) return absoluteURL;
        if (absoluteURL.indexOf('.') < 0) return absoluteURL;
        
        //-- Note: using StringTokenizer and Stacks
        //-- is not very efficient, this may need        
        //-- some optimizing
        Stack tokens = new Stack();
        StringTokenizer st = new StringTokenizer(absoluteURL, URL_PATH_SEP_STR, true);
        String last = null;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (URL_PATH_SEP_STR.equals(token)) {
                if (URL_PATH_SEP_STR.equals(last)) {
                    tokens.push("");
                }
            }
            else if (PARENT_DIR_OP.equals(token)) {
                if (tokens.empty()) {
                    //-- this should be an error
                    throw new MalformedURLException("invalid absolute URL: " + absoluteURL);
                }
                tokens.pop();
            }
            else {
                if (!CURRENT_DIR_OP.equals(token)) {
                    tokens.push(token);
                }
            }
            last = token;
        }
        
        //-- rebuild URL
        StringBuffer buffer = new StringBuffer(absoluteURL.length());
        for (int i = 0; i < tokens.size(); i++) {
            if (i > 0) buffer.append(HREF_PATH_SEP);
            buffer.append(tokens.elementAt(i).toString());
        }
        return buffer.toString();
    } //-- normalize
    
    
	/**
	 *
	**/
	public static String resolveAsString(String href, String documentBase) {

	    try {
	        //-- try to create a new URL and see if MalformedURLExcetion is
	        //-- ever thrown
	        URL url = new URL(href);
	        url = null; //-- to remove compiler warnings
	        return href;
	    }
	    catch(MalformedURLException muex) {}


	    //-- join document base + href
	    String absolute = null;
	    if ((documentBase != null) && (documentBase.length() > 0)) {
	        int idx = documentBase.lastIndexOf(HREF_PATH_SEP);
	        if (idx == (documentBase.length()-1))
	            absolute = documentBase+href;
	        else
	            absolute = documentBase+HREF_PATH_SEP+href;
            
            
	    }
	    else absolute = href;
	    
        
	    try {
	        //-- try to create a new URL and see if MalformedURLExcetion is
	        //-- ever thrown
            
            if (absolute.indexOf("./") >= 0) {
                //-- normalize . or .. from URL
            	absolute = normalize(absolute);
            }
	        URL url = new URL(absolute);
	        url = null; //-- to remove compiler warnings
	        return absolute;
	    }
	    catch(MalformedURLException muex) {
	        //-- check for unrecognized protocol
	        int idx = absolute.indexOf(':');
	        if (idx >= 0) {
	            String scheme = absolute.substring(0, idx);
	            //-- a bit of a hack, but good enough for now
	            String error = "unknown protocol: " + scheme;
	            if (error.equals(muex.getMessage())) {
	                return absolute;
	            }
	        }
	        
	    }
	    

	    // Try local files
	    String fileURL = absolute;
	    File iFile = new File(href);
	    boolean exists = iFile.exists();
	    fileURL = createFileURL(iFile.getAbsolutePath());
	    if (!iFile.isAbsolute()) {
	        iFile = new File(absolute);
	        if (iFile.exists() || (!exists)) {
	            fileURL = createFileURL(iFile.getAbsolutePath());
	        }
	    }
	    
	    //-- one last sanity check
	    try {
	        //-- try to create a new URL and see if MalformedURLExcetion is
	        //-- ever thrown
	        URL url = new URL(fileURL);
	        url = null; //-- to remove compiler warnings
	        return fileURL;
	    }
	    catch(MalformedURLException muex) {}
	    
	    //-- At this point we we're unsucessful at trying to resolve
	    //-- the href + documentbase, this could be due to a custom
	    //-- protocol or typo in the URI, just return documentBase + 
	    //-- href
	    return absolute;
	} //-- resolveHref

	/**
	 * Creates a File URL for the given file name
	 *
	 * @param filename the name of the file
	 * @return the String representation of the File URL
	**/
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


} //-- URIUtils

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
 * Copyright 2002-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.util;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A class which contains the version information
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-05 15:43:19 -0600 (Wed, 05 Apr 2006) $
 */
public final class Version {

    //-----------------------/
    //- Public Class Fields -/
    //-----------------------/
    
    /**
     * The version number
     */
    public static final String VERSION = "1.3";
        
    /**
     * The version date.
     */
    public static final String VERSION_DATE  = "20091002";
    
    /**
     * The version number with build information
     */ 
    public static final String BUILD_VERSION = Version.getBuildVersion();
    
    
    //------------------------/
    //- Private Class Fields -/
    //------------------------/
    
    private static final String JAR_PROTOCOL  = "jar:";
    private static final String FILE_PROTOCOL = "file:";
    private static final String DATE_FORMAT   = "yyyyMMdd.HHmmss";
    
    //------------------------/
    //- Public Class Methods -/
    //------------------------/
    
    /**
     *
     */
    public static String getBuildVersion() {
        
        StringBuffer buffer = new StringBuffer(VERSION);
        String classname = Version.class.getName();
        String resource = "/" + classname.replace('.', '/') + ".class";
        
        URL url = Version.class.getResource(resource);
        // shouldn't be null, but you never know 
        if (url != null) {  
            buffer.append("  [");
            String href = url.toString();
            Date date = null;
	        if (href.startsWith(JAR_PROTOCOL)) {
	            href = href.substring(JAR_PROTOCOL.length());
	            if (href.startsWith(FILE_PROTOCOL))
	                href = href.substring(FILE_PROTOCOL.length());
    	            
	            int idx =  href.indexOf('!');
	            //-- get entry name (remove '!/' from beginning)
	            String entryName = href.substring(idx+2);
	            href = href.substring(0, idx);
    	        try {
	                ZipFile file = new ZipFile(href);
	                ZipEntry entry = file.getEntry(entryName);
	                if (entry != null) {
	                    long t = entry.getTime();
	                    if (t > 0) {
    	                    date = new Date(entry.getTime());
    	                }
	                }
	            }
	            catch (java.io.IOException iox) {
	                //-- ignore...problem with finding or reading jar.
	            }
	        }
	        else if (href.startsWith(FILE_PROTOCOL)) {
	            File file = new File(href.substring(FILE_PROTOCOL.length()));
	            date = new Date(file.lastModified());
	        }
            if (date != null) {
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                buffer.append(format.format(date));
            }
            else buffer.append("0");
            buffer.append(']');
        }
        return buffer.toString();
        
    } //-- getBuildVersion
    
    /**
     * Command line
     */
    public static void main(String[] args) {
        System.out.println(BUILD_VERSION);
    }
    
} //-- Version

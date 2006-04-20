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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.tests.framework;


import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.jar.JarFile;

/**
 * This class is a set of tools for manipulating files needed by the CTF.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class FileServices {

    public static final String XSD = ".xsd";
    public static final String XML = ".xml";
    public static final String JAVA = ".java";
    public static final String JAR = ".jar";

    /**
     * Copy all the needed documents (java, xsd, xml file) of given file (jar or directory) to a specified directory.
     * @param file the file that contains the entries to copy
     * @param root the directory where to copy the file
     */
    protected static void copySupportFiles(File file, File root)
        throws IOException, FileNotFoundException
    {

        if (file == null)
           throw new IllegalArgumentException("The Entry file is null");
        if (root == null)
           throw new IllegalArgumentException("The destination directory is null");

        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            for (int i=0 ; i<entries.length; i++) {
                File tempEntry = entries[i];
                if (isSupportFile(tempEntry.getName())) {
                   InputStream src = new FileInputStream(tempEntry);
                    File out = new File(root, tempEntry.getName());
                    out.getParentFile().mkdir();
                    copy(src, new FileOutputStream(out));
                }
                else if (tempEntry.isDirectory()) {
                    File out = new File(root, tempEntry.getName());
                    out.mkdir();
                    copySupportFiles(tempEntry, out);
                }
            }//for
        }//directory
        else if (file.getName().endsWith(JAR)) {
            JarFile jar = new JarFile(file);
            for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry)e.nextElement();
                if ( isSupportFile(entry.getName()) ) {
                    InputStream src = jar.getInputStream(entry);
                    File out = new File(root, entry.getName());
                    out.getParentFile().mkdirs();
                    copy(src, new FileOutputStream(out));
                }
            }//for
        }
        //ignore other file type

    }
   /**
     * Copy an InputStream into a OutputStream
     */
    protected static void copy(InputStream src, OutputStream dst)
        throws FileNotFoundException, IOException
    {
        final int BUF_SIZE = 16 * 1024;
        byte[] buf = new byte[BUF_SIZE];
        int read;
        while (true) {
            read = src.read(buf, 0, BUF_SIZE);
            if (read == -1) {
                break;
            }
            dst.write(buf, 0, read);
        }
    }

    /**
     * Return true if the file is a support file for the test. A support file is
     * a schema or a java file.
     */
    private static boolean isSupportFile(String name) {
        return ((name.endsWith(XSD)) || (name.endsWith(JAVA)) || (name.endsWith(XML)) );
    }


}
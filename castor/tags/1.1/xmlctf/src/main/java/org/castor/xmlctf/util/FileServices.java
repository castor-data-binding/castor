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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.castor.xmlctf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * This class is a set of tools for manipulating files needed by the CTF.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class FileServices {

    /** The string for a CVS subdirectory. */
    public static final String CVS  = "CVS";
    /** The string for a Subversion subdirectory. */
    public static final String SVN  = ".svn";
    /** The string for an extension for a XSD file. */
    public static final String XSD  = ".xsd";
    /** The string for an extension for a XML file. */
    public static final String XML  = ".xml";
    /** The string for an extension for a Java file. */
    public static final String JAVA = ".java";
    /** The string for an extension for a JAR file. */
    public static final String JAR  = ".jar";

    /** Buffer size used for copying files. */
    private static final int BUF_SIZE = 16 * 1024;

    /**
     * No-arg constructor.  Private as we're a utility class.
     */
    private FileServices() {
        // Nothing to do
    }

    /**
     * Copy all the needed documents (java, xsd, xml file) of given file (jar or
     * directory) to a specified directory.
     *
     * @param file the file that contains the entries to copy
     * @param root the destination directory to copy files to
     * @throws IOException if an error occurs while copying files
     */
    public static void copySupportFiles(final File file, final File root) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("The Entry file is null");
        }
        if (root == null) {
            throw new IllegalArgumentException("The destination directory is null");
        }

        if (file.isDirectory()) {
            copySupportFilesForDirectory(file, root);
        } else if (file.getName().endsWith(JAR)) {
            copySupportFilesForJarFile(file, root);
        } else {
            //ignore other file type
        }
    }

    /**
     * Unzips support files out of the JAR to our output directory. See
     * {@link #isSupportFile(String)} for the definition of support file. This
     * method does not totally unzip the JAR file. It only extracts certain
     * important files out of the JAR.
     *
     * @param file the file that contains the entries to copy
     * @param root the destination directory to copy files to
     * @throws IOException if an error occurs while copying files
     */
    private static void copySupportFilesForJarFile(final File file, final File root)
                                                                      throws IOException {
        JarFile jar = new JarFile(file);
        for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (isSupportFile(entry.getName())) {
                InputStream src = jar.getInputStream(entry);
                File out = new File(root, entry.getName());
                out.getParentFile().mkdirs();
                copy(src, new FileOutputStream(out));
                src.close();
            }
        }
    }

    /**
     * Copies support files from one directory to our output directory,
     * recursing into other directories. See {@link #isSupportFile(String)} for
     * the definition of support file.
     *
     * @param file the file that contains the entries to copy
     * @param root the destination directory to copy files to
     * @throws IOException if an error occurs while copying files
     */
    private static void copySupportFilesForDirectory(final File file, final File root)
                                                                      throws IOException {
        File[] entries = file.listFiles();
        for (int i = 0; i < entries.length; i++) {
            File tempEntry = entries[i];
            if (isSupportFile(tempEntry.getName())) {
                File out = new File(root, tempEntry.getName());
                out.getParentFile().mkdir();
                FileOutputStream dest = new FileOutputStream(out);

                InputStream src = new FileInputStream(tempEntry);
                copy(src, dest);
                src.close();
                dest.close();
            } else if (tempEntry.isDirectory()) {
                File out = new File(root, tempEntry.getName());
                out.mkdir();
                copySupportFiles(tempEntry, out);
            }
        }
    }

    /**
     * Copies an InputStream into a OutputStream.
     *
     * @param src Source input stream
     * @param dest Destination output stream
     * @throws IOException if an error occurs while copying files
     */
    private static void copy(final InputStream src, final OutputStream dest) throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        int read;
        while (true) {
            read = src.read(buf, 0, BUF_SIZE);
            if (read == -1) {
                break;
            }
            dest.write(buf, 0, read);
        }
    }

    /**
     * Return true if the file is a support file for the test. A support file is
     * a schema or a java file.
     *
     * @param name File name to check to see if it represents a support file
     * @return true if the file is a support file for the test.
     */
    private static boolean isSupportFile(final String name) {
        return name.endsWith(XSD) || name.endsWith(JAVA) || name.endsWith(XML);
    }

    /**
     * Return true if the file provided is a special source control management
     * directory that we want to ignore.
     * @param name Name of a directory
     * @return true if the file provided is an SCM directory
     */
    public static boolean isScmDirectory(final String name) {
        return name.endsWith(FileServices.CVS) || name.equals(FileServices.SVN);
    }

}

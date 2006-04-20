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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.tests.framework;

import java.util.Vector;
import java.util.Enumeration;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import sun.tools.javac.Main;

/**
 * Help to call the standard sun java compiler from a java program 
 *
 * @author <a href="mailto:gignoux@intalio.com">Sebastien Gignoux</a> 
 * @version $Revision$ $Date$
 */

public class CompilerHelper {

    /**
     * Wrapper to the Sun java compiler. Use the system class path.
     *
     * @param outputDir the directory where to put the files, null if we don't care
     * @param sourceFiles a vector a string for all the file we want to compiler
     * @param out the output stream where to put the output of the compiler
     * messages, null if we don't care of the messages.
     * @throws Exception if a compilation error occured.
     */
    public static void compile(String outputDir, Vector sourceFiles, OutputStream out) 
        throws Exception {

        if (out == null) 
            out = new ByteArrayOutputStream();

        Main compiler = new Main(out, "javac");

        Vector argsVect = new Vector();

        argsVect.add("-classpath");
        argsVect.add(System.getProperty("java.class.path"));

        if (outputDir != null) {
            argsVect.add("-d");
            argsVect.add(outputDir);
        }

        argsVect.addAll(sourceFiles);

        String[] args = new String[argsVect.size()];
        
        int i = 0;
        for (Enumeration e=argsVect.elements(); e.hasMoreElements();) 
           args[i++] = (String)e.nextElement();

        boolean bCompRes = compiler.compile(args);

        if (bCompRes == false)
            throw new Exception("Problem while compiling:" + out.toString());
    }

    /**
     * Find all the java file (i.e. file named "*.java") in the given directory
     * and its subdirectories. Put the result in the given Vector.
     *
     * @param result the Vector into which to put the result into
     * @param file the directory to search for java file
     * @return a vector of string of the absolute path of the files
     */
    public static Vector findAllJavaFiles(File file, Vector result) {

        File[] files = file.listFiles();

        if (file == null) {
            // Maybe this is a java file...
            if (isJavaFile(file))
                result.add(file.getAbsolutePath());
            
            return result;
        }

        for (int i=0; i<files.length;++i) {
            File f = files[i];
            if (f.isDirectory())
                // recurse
                result = findAllJavaFiles(f, result);
            else if (isJavaFile(f))
                result.add(f.getAbsolutePath());
            // else ignore
        }

        return result;
    }

    
    /**
     * Return true if the file name end with '.java'
     */
    private static boolean isJavaFile(File file) {
        return file.getName().endsWith(".java");
    }

}

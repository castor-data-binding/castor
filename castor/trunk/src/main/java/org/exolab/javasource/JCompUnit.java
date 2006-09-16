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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * Contributors:
 * --------------
 * Gary Shea (shea AT gtsdesign DOT com)   
 *    - Original Author
 * 
 * Keith Visco 
 *   - Changed JCompElement references to JStructure, some additional
 *     tweaking to get it working with the current Javasource package.
 *
 * $Id$
 */

package org.exolab.javasource;

import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

/**
 * A representation of the Java Source code for a Java compilation unit. This is
 * a useful utility when creating in memory source code. This package was
 * modelled after the Java Reflection API as much as possible to reduce the
 * learning curve.
 * 
 * @author <a href="mailto:shea AT gtsdesign DOT com">Gary Shea</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class JCompUnit {

    /**
     * The Id for Source control systems I needed to break the String to prevent
     * CVS from expanding it here! ;-)
     */
    private static final String DEFAULT_HEADER = "$" + "Id$";

    /**
     * JavaDoc comment for this compilation unit
     */
    private JComment _header = null;

    /**
     * The package for this JCompUnit
     */
    private String _packageName = null;

    /**
     * The file to which this JCompUnit will be written
     */
    private String _fileName = null;

    /**
     * The set of top-level classes that live in this compilation unit.
     */
    //private TypeList classes = null;
    private Vector _classes = null;
    
    /**
     * The set of top-level interfaces that live in this compilation unit.
     */
    //private TypeList interfaces = null;
    private Vector _interfaces = null;

    /**
     * Creates a new JCompUnit
     * 
     * @param packageName the name of the package for this JCompUnit. If
     *            packageName is null or empty, no 'package' line will be
     *            generated.
     * @param fileName the name of the file to which this JCompUnit will be
     *            written
     */
    public JCompUnit(final String packageName, final String fileName) {
        this._packageName = packageName;
        this._fileName    = fileName;
        init();
    } //-- JCompUnit

    /**
     * Creates a new JCompUnit with the given JClass (which must have been
     * created with either a full class name or package/local name) as the
     * public class. Package and file name are taken from jClass.
     * 
     * @param jClass the public class for this JCompUnit
     */
    public JCompUnit(final JClass jClass) {
        this._packageName = jClass.getPackageName();

        // The outer name is the package plus the simple name of the
        // outermost enclosing class.  The file name is just the
        // simple name of the outermost enclosing class, so the
        // package name part must be stripped off.
        
        /*
          Commented out until inner-class support has been added.
          kvisco - 20021211
    
        String outer = jClass.getOuterName();
        int lastDot = outer.lastIndexOf(".");
        String filePrefix;
        if (lastDot != -1) {
            filePrefix = outer.substring (lastDot + 1);
        } else {
            filePrefix = outer;
        }
        */
        String filePrefix = jClass.getLocalName();
        
        this._fileName = filePrefix + ".java";
        init();
        _classes.add(jClass);
        
    } //-- JCompUnit

    /**
     * Creates a new JCompUnit with the given JInterface as public interface.
     * Package and file name are taken from jInterface.
     * 
     * @param jInterface the public interface for this JCompUnit.
     */
    public JCompUnit(final JInterface jInterface) {
        this._packageName = jInterface.getPackageName();
        this._fileName    = jInterface.getLocalName() + ".java";
        init();
        _interfaces.add(jInterface);
    } //-- JCompUnit
    
    private void init() {
        _classes          = new Vector();
        _interfaces       = new Vector();
    }

    /**
     * Adds the given JStructure (either a JInterface or a JClass) to this
     * JCompUnit.
     * 
     * @param jStructure the JStructure to add
     */
    public void addStructure(final JStructure jStructure) {
        if (jStructure instanceof JInterface) {
            addInterface((JInterface) jStructure);
        } else if (jStructure instanceof JClass) {
            addClass((JClass) jStructure);
        } else {
            String err = "Unknown JStructure subclass '" 
                + jStructure.getClass().getName() + "'.";
            throw new IllegalArgumentException(err);
        }

    } //-- addStructure

    /**
     * Adds a JClass to be printed in this file
     * 
     * @param jClass the JClass to be printed in this file
     */
    public void addClass(final JClass jClass) {
        _classes.add(jClass);
    } //-- addClass

    /**
     * Adds a JInterface to be printed in this file.
     * 
     * @param jInterface the JInterface to be printed in this file
     */
    public void addInterface(final JInterface jInterface) {
        _interfaces.add(jInterface);
    } //-- addInterface

    /**
     * Returns a array of String containing all imported classes/packages, also
     * imports within the same package of this object.
     * 
     * @return a array of String containing all import classes/packages, also
     *         imports within the same package of this object
     */
    public SortedSet getImports() {
        SortedSet allImports = new TreeSet();
        
        // add imports from classes
        for (int i = 0; i < _classes.size(); ++i) {
            JClass jClass = (JClass) _classes.get(i);
            
            Enumeration enumeration = jClass.getImports();
            while (enumeration.hasMoreElements()) {
                allImports.add(enumeration.nextElement());
            }
        }
        
        for (int i = 0; i < _interfaces.size(); ++i) {
            JInterface jInterface = (JInterface) _interfaces.get(i);
            Enumeration enumeration = jInterface.getImports();
            while (enumeration.hasMoreElements()) {
                allImports.add(enumeration.nextElement());
            }
        }
        
        return allImports;
    }

    /**
     * Returns the name of the file that this JCompUnit would be printed to,
     * given a call to {@link #print(String, String)}, or if destDir is null, a
     * call to {@link #print()}.
     * 
     * @param destDir the destination directory. This may be null.
     * @return the name of the file that this JCompUnit would be printed to
     */
    public String getFilename(final String destDir) {
        String filename = new String (_fileName);

        //-- Convert Java package to path string
        String javaPackagePath = "";
        if ((_packageName != null) && (_packageName.length() > 0)) {
            javaPackagePath = _packageName.replace('.', File.separatorChar);
        }

        //-- Create fully qualified path (including 'destDir') to file
        File pathFile;
        if (destDir == null) {
            pathFile = new File(javaPackagePath);
        } else {
            pathFile = new File(destDir, javaPackagePath);
        }
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        //-- Prefix filename with path
        if (pathFile.toString().length() > 0) {
            filename = pathFile.toString() + File.separator + filename;
        }

        return filename;
    } //-- getFilename

    /**
     * Returns the name of the package that this JCompUnit is a member of
     * 
     * @return the name of the package that this JCompUnit is a member of, or
     *         null if there is no current package name defined
     */
    public String getPackageName() {
        return this._packageName;
    } //-- getPackageName

    protected static String getPackageFromClassName(final String className) {
        int idx = className.lastIndexOf('.');
        if (idx > 0) { return className.substring(0, idx); }
        return null;
    } //-- getPackageFromClassName

    /**
     * Prints the source code for this JClass in the current directory with the
     * default line seperator of the the runtime platform.
     * 
     * @see #print(String, String)
     */
    public void print() {
        print(null, null);
    } //-- print

    /**
     * Prints the source code for this JClass with the default line seperator of
     * the the runtime platform.
     * 
     * @param destDir the destination directory to use as the root directory for
     *            source generation
     * @see #print(String, String)
     */
    public void print(final String destDir) {
        print(destDir, null);
    } //-- print

    /**
     * Prints the source code for this JCompUnit using the provided root
     * directory and line separator.
     * 
     * @param destDir the destination directory to use as the root directory for
     *            source generation
     * @param lineSeparator the line separator to use at the end of each line.
     *            If null, then the default line separator for the runtime
     *            platform will be used.
     */
    public void print(final String destDir, final String lineSeparator) {
        //-- open output file
        String filename = getFilename(destDir);

        File file = new File(filename);
        JSourceWriter jsw = null;
        try {
            jsw = new JSourceWriter(new FileWriter(file));
        } catch (java.io.IOException ioe) {
            System.out.println("unable to create compilation unit file: " + filename);
            return;
        }

        if (lineSeparator == null) {
            jsw.setLineSeparator(System.getProperty("line.separator"));
        } else {
            jsw.setLineSeparator(lineSeparator);
        }
        print(jsw);
        jsw.flush();
        jsw.close();
    } //-- print

    /**
     * Prints the source code for this JClass to the provided JSourceWriter
     * 
     * @param jsw the JSourceWriter to print to
     */
    public void print(final JSourceWriter jsw) {
        // Traverse the nested class and interface heirarchy and
        // update the names to match the compilation unit.

        resolveNames ();
        StringBuffer buffer = new StringBuffer();

        //-- write file header
        if (_header != null) {
            _header.print(jsw);
        } else {
            jsw.writeln("/*");
            jsw.writeln(" * " + DEFAULT_HEADER);
            jsw.writeln("*/");
        }
        jsw.writeln();
        jsw.flush();

        //-- print package name
        if ((_packageName != null) && (_packageName.length() > 0)) {

            buffer.setLength(0);
            buffer.append("package ");
            buffer.append(_packageName);
            buffer.append(';');
            jsw.writeln(buffer.toString());
            jsw.writeln();
        }

        //-- print imports
        jsw.writeln("  //---------------------------------------------/");
        jsw.writeln(" //- Imported classes, interfaces and packages -/");
        jsw.writeln("//---------------------------------------------/");
        jsw.writeln();
        SortedSet allImports = getImports();
        String compUnitPackage = getPackageName();
        Iterator iter = allImports.iterator();
        while (iter.hasNext()) {
            String importName = (String) iter.next();
            String importsPackage
            = JStructure.getPackageFromClassName(importName);
            if ((importsPackage != null) && !importsPackage.equals(compUnitPackage)) {
                jsw.write("import ");
                jsw.write(importName);
                jsw.writeln(';');
            }
        }
        jsw.writeln();

        // Print the public elements, interfaces first, then classes.
        // There should only be one public element, but if there are
        // more we let the compiler catch it.
        printStructures(jsw, true);

        // Print the remaining non-public elements, interfaces first.
        printStructures(jsw, false);

        jsw.flush();
    } //-- print

    /**
     * Print the source code for the contained JClass objects
     * 
     * @param jsw the JSourceWriter to print to.
     * @param printPublic if true, print only public classes; if false, print
     *            only non-public classes.
     */
    public void printStructures(final JSourceWriter jsw, final boolean printPublic) {

        //-- print class information
        //-- we need to add some JavaDoc API adding comments

        boolean isFirst = true;

        //SortedSet interfaceList = interfaces.sortedOnFullName();
        for (Enumeration e = _interfaces.elements(); e.hasMoreElements(); ) {
            JInterface jInterface = (JInterface) e.nextElement();
            if (jInterface.getModifiers().isPublic() == printPublic) {
                if (isFirst) {
                    Header.print(jsw, printPublic);
                    isFirst = false;
                }
                jInterface.print(jsw, true);
                jsw.writeln();
            }
        }

        //SortedSet classList = classes.sortedOnFullName();
        for (Enumeration e = _classes.elements(); e.hasMoreElements(); ) {
            JClass jClass = (JClass) e.nextElement();
            if (jClass.getModifiers().isPublic() == printPublic) {
                if (isFirst) {
                    Header.print(jsw, printPublic);
                    isFirst = false;
                }
                jClass.print(jsw, true);
                jsw.writeln();
            }
        }
    } //-- printElements(JSourceWriter, int)

    /**
     * Sets the header comment for this JCompUnit
     * 
     * @param comment the comment to display at the top of the source file when
     *            printed
     */
    public void setHeader(final JComment comment) {
        this._header = comment;
    } //-- setHeader

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Update the names of nested classes and interfaces.
     */
    private void resolveNames()  {
        /*
          Commented out until support for inner-classes is added
          kvisco - 20021211
          
        for (int i = 0; i < classes.size(); i++) {
            JClass jClass = (JClass) classes.get(i);
            jClass.resolveNames(packageName, null);
        }
        for (int i = 0; i < interfaces.size(); i++) {
            JInterface jInterface = (JInterface) interfaces.get(i);
            jInterface.resolveNames(packageName, null);
        }
        */
    } //-- resolveNames

    /**
     * Test drive method...to be removed or commented out
    **
    public static void main(String[] args) {
        JCompUnit unit = new JCompUnit("com.acme", "Test.java");

        JClass testClass = new JClass("Test");

        testClass.addImport("java.util.Vector");
        testClass.addMember(new JField(JType.Int, "x"));

        JField field = null;
        field = new JField(JType.Int, "_z");
        field.getModifiers().setStatic(true);
        testClass.addField(field);
        
        testClass.getStaticInitializationCode().add("_z = 75;");
        
        JClass jcString = new JClass("String");
        field = new JField(jcString, "myString");
        field.getModifiers().makePrivate();
        testClass.addMember(field);

        //-- create constructor
        JConstructor cons = testClass.createConstructor();
        testClass.addConstructor(cons);
        cons.getSourceCode().add("this.x = 6;");

        JMethod jMethod = new JMethod(JType.Int, "getX");
        jMethod.setSourceCode("return this.x;");
        testClass.addMethod(jMethod);

        unit.addClass (testClass);

        unit.print();

    } //-- main
    /* */

} //-- JCompUnit

/**
 * Print the headers delineating the public and non-public elements of the
 * compilation unit.
 */
final class Header {

    private static final String[] PUBLIC_HEADER = {
        "  //-----------------------------/",
        " //-  Public Class / Interface -/",
        "//-----------------------------/",
    };
    
    private static final String[] NON_PUBLIC_HEADER = {
        "  //-------------------------------------/",
        " //-  Non-Public Classes / Interfaces  -/",
        "//-------------------------------------/",
    };

    /**
     * Print the specified header to the given Writer.
     * 
     * @param jsw an open JSourceWriter
     * @param printPublic if true print the public header, otherwise print the
     *            non-public header.
     */
    protected static void print (final JSourceWriter jsw, final boolean printPublic) {
        String[] header = printPublic ? PUBLIC_HEADER : NON_PUBLIC_HEADER;
        for (int j = 0; j < header.length; ++j) {
            jsw.writeln(header[j]);
        }
        jsw.writeln();
    }
    
    /**
     * Hide utility class constructor. 
     */
    private Header() { }
} //-- Header

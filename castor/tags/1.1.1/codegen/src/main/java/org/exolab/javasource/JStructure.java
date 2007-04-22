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
 */
package org.exolab.javasource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class represents the basic Java "structure" for a Java source file. This
 * is the base class for JClass and JInterface.
 * <br/>
 * This is a useful utility when creating in memory source code. The code in
 * this package was modelled after the Java Reflection API as much as possible
 * to reduce the learning curve.
 *
 * @author <a href="mailto:skopp AT riege DOT de">Martin Skopp</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public abstract class JStructure extends JType implements JAnnotatedElement {
    //--------------------------------------------------------------------------

    /** The Id for Source control systems.
     *  <br/>
     *  Note: I needed to break the String into parts to prevent CVS from expanding it here! */
    private static final String DEFAULT_HEADER = "$" + "Id$";

    /** The source control version for listed in the JavaDoc
     *  <br/>
     *  Note: I needed to break the String into parts to prevent CVS from expanding it here! */
    private static final String DEFAULT_VERSION = "$" + "Revision$ $" + "Date$";
    
    /** A standard complaint for a bad parameter. */
    private static final String JSW_SHOULD_NOT_BE_NULL = "argument 'jsw' should not be null.";

    //--------------------------------------------------------------------------

    /** The source header. */
    private JComment _header;

    /** The package to which this JStructure belongs. */
    private String _packageName;

    /** List of imported classes and packages. */
    private Vector _imports;

    /** The Javadoc for this JStructure. */
    private JDocComment _jdc;

    /** Implementation of JAnnoatedElement to delagate to. */
    private JAnnotatedElementHelper _annotatedElement;

    /** The JModifiers for this JStructure, which allows us to change the resulting qualifiers. */
    private JModifiers _modifiers;

    /** The set of interfaces implemented/extended by this JStructure. */
    private Vector _interfaces;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JStructure with the given name.
     *
     * @param name The name of the JStructure.
     */
    protected JStructure(final String name) {
        super(name);

        //-- verify name is a valid java class name
        if (!isValidClassName(name)) {
            String lname = getLocalName();
            String err = "'" + lname + "' is ";
            if (JNaming.isKeyword(lname)) {
                err += "a reserved word and may not be used as  a class name.";
            } else {
                err += "not a valid Java identifier.";
            }
            throw new IllegalArgumentException(err);
        }
        
        _header = null;
        _packageName = JNaming.getPackageFromClassName(name);
        _imports = new Vector();
        _jdc = new JDocComment(JDocDescriptor.createVersionDesc(DEFAULT_VERSION));
        _annotatedElement = new JAnnotatedElementHelper();
        _modifiers = new JModifiers();
        _interfaces = new Vector();
    }

    /**
     * Test the provided name and return true if it is a valid class name.
     *
     * @param classname A class name to test.
     * @return True if the provided class name is a valid class name.
     */
    private boolean isValidClassName(final String classname) {
        if (classname == null) { return false; }

        String name = classname;
        int beforeTypeName = name.indexOf("<");
        if (beforeTypeName > 0) {
            name = name.substring(0, beforeTypeName);
        }

        //-- ignore package information, for now
        name = JNaming.getLocalNameFromClassName(name);

        return JNaming.isValidJavaIdentifier(name);
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the JComment header to display at the top of the source file for
     * this JStructure, or null if no header was set.
     *
     * @return The JComment header or null if none exists.
     */
    public final JComment getHeader() {
        return _header;
    }

    /**
     * Sets the header comment for this JStructure.
     *
     * @param comment The comment to display at the top of the source file when printed.
     */
    public final void setHeader(final JComment comment) {
        _header = comment;
    }

    /**
     * Returns the name of the package that this JStructure is a member of.
     *
     * @return The name of the package that this JStructure is a member of, or
     *         null if there is no current package name defined.
     */
    public final String getPackageName() {
        return _packageName;
    }

    /**
     * Returns an Enumeration of imported package and class names for this
     * JStructure.
     *
     * @return The Enumeration of imports. May be empty but will not be null.
     */
    public final Enumeration getImports() {
        return _imports.elements();
    }

    /**
     * Returns true if the given classname exists in the imports of this
     * JStructure.
     *
     * @param classname The class name to check for
     * @return True if the given classname exists in the imports list.
     */
    public final boolean hasImport(final String classname) {
        return _imports.contains(classname);
    }

    /**
     * Adds the given import to this JStructure.  Note:  You cannot import
     * from the "default package," so imports with no package are ignored.
     *
     * @param className Name of the class to import.
     */
    public abstract void addImport(final String className);

    /**
     * Adds the given import to this JStructure. Given class name should not be null or empty.
     * <br/>
     * Note: You cannot import from the "default package," so imports with no package are ignored.
     *
     * @param className Name of the class to import.
     */
    protected final void addImportInternal(final String className) {
        //-- getPackageName
        String pkgName = JNaming.getPackageFromClassName(className);

        if (pkgName != null) {
            if (pkgName.equals(_packageName) || pkgName.equals("java.lang")) {
                return;
            }

            //-- for readabilty keep import list sorted, and make sure
            //-- we do not include more than one of the same import
            for (int i = 0; i < _imports.size(); i++) {
                String imp = (String) _imports.elementAt(i);
                if (imp.equals(className)) { return; }
                if (imp.compareTo(className) > 0) {
                    _imports.insertElementAt(className, i);
                    return;
                }
            }
            _imports.addElement(className);
        }
    }

    /**
     * Adds appropriate import for this JAnnotation.
     *
     * @param annotation A JAnnotation for which we want to add an import to
     *        this JStructure.
     */
    protected final void addImport(final JAnnotation annotation) {
        addImport(annotation.getAnnotationType().getName());
    }

    /**
     * Adds appropriate imports for each JAnnotation in the given Array.
     *
     * @param annotations An Array of JAnnotation; we want to add an import to
     *        this JStructure for each JAnnotation in the Array.
     */
    protected final void addImport(final JAnnotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            addImport(annotations[i].getAnnotationType().getName());
        }
    }

    /**
     * Remove the import of the given class name from this JStucture, returning
     * true if the import was found and removed.
     *
     * @param className Name of the class to remove the import of.
     * @return If the import was previously part of this JStructure, false
     *         otherwise.
     */
    public final boolean removeImport(final String className) {
        boolean result = false;
        if (className == null) { return result; }
        if (className.length() == 0) { return result; }

        result = _imports.removeElement(className);
        return result;
    }

    /**
     * Returns the JavaDoc comment for this JStructure.
     *
     * @return The JDocComment for this JStructure.
     */
    public final JDocComment getJDocComment() {
        return _jdc;
    }

    /**
     * Returns the object managing the annotations for this JStructure.
     * 
     * @return The object managing the annotations for this JStructure.
     */
    protected final JAnnotatedElementHelper getAnnotatedElementHelper() {
        return _annotatedElement;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean hasAnnotations() {
        return _annotatedElement.hasAnnotations();
    }

    /**
     * {@inheritDoc}
     */
    public final JAnnotation[] getAnnotations() {
        return _annotatedElement.getAnnotations();
    }

    /**
     * {@inheritDoc}
     */
    public final JAnnotation getAnnotation(final JAnnotationType annotationType) {
        return _annotatedElement.getAnnotation(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isAnnotationPresent(final JAnnotationType annotationType) {
        return _annotatedElement.isAnnotationPresent(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public final void addAnnotation(final JAnnotation annotation) {
        _annotatedElement.addAnnotation(annotation);
        addImport(annotation);
    }

    /**
     * {@inheritDoc}
     */
    public final JAnnotation removeAnnotation(final JAnnotationType annotationType) {
        return _annotatedElement.removeAnnotation(annotationType);
    }

    /**
     * Returns the JModifiers, which allows the qualifiers to be changed.
     *
     * @return The JModifiers for this JStructure.
     */
    public final JModifiers getModifiers() {
        return _modifiers;
    }

    /**
     * Returns an Enumeration of interface names that this JStructure inherits
     * from.
     *
     * @return The Enumeration of interface names for this JStructure. May be
     *         empty but will not be null.
     */
    public final Enumeration getInterfaces() {
        return _interfaces.elements();
    }

    /**
     * Return the count of the number of Interfaces that have been added to this
     * JStructure.
     *
     * @return The count of the number of Interfaces that have been added to
     *         this JStructure.
     */
    protected final int getInterfaceCount() {
        return _interfaces.size();
    }

    /**
     * Adds the given interface to the list of interfaces this JStructure
     * inherits method declarations from, and either implements (JClass) or
     * extends (JInterface).
     *
     * @param interfaceName The name of the interface to "inherit" method
     *        declarations from.
     */
    public final void addInterface(final String interfaceName) {
        if (!_interfaces.contains(interfaceName)) {
            _interfaces.addElement(interfaceName);
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Returns the field with the given name, or null if no field was found with
     * that name.
     *
     * @param name The name of the field to return.
     * @return The field with the given name, or null if no field was found with
     *         the given name.
     */
    public abstract JField getField(String name);

    /**
     * Returns an array of all the JFields of this JStructure.
     *
     * @return An array of all the JFields of this JStructure.
     */
    public abstract JField[] getFields();

    /**
     * Adds the given JField to this JStructure.
     * <br/>
     * This method is implemented by subclasses and should only accept the
     * proper fields for the subclass otherwise an IllegalArgumentException will
     * be thrown. For example a JInterface will only accept static fields.
     *
     * @param jField The JField to add.
     */
    public abstract void addField(JField jField);

    /**
     * Adds the given JMember to this JStructure.
     * <br/>
     * This method is implemented by subclasses and should only accept the
     * proper types for the subclass otherwise an IllegalArgumentException will
     * be thrown.
     *
     * @param jMember The JMember to add to this JStructure.
     */
    public abstract void addMember(JMember jMember);

    //--------------------------------------------------------------------------
    
    /**
     * Returns the name of the file that this JStructure would be printed to,
     * given a call to {@link #print(String, String)}.
     *
     * @param destDir the destination directory. This may be null.
     * @return the name of the file that this JInterface would be printed to
     */
    public final String getFilename(final String destDir) {
        String filename = getLocalName() + ".java";

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
    }
    
    //--------------------------------------------------------------------------

    /**
     * Prints the source code for this JStructure to the destination directory.
     * Subdirectories will be created if necessary for the package.
     *
     * @param destDir Directory name to use as the root directory for all output.
     * @param lineSeparator The line separator to use at the end of each line.
     *        If null, then the default line separator for the runtime platform will be used.
     */
    public final void print(final String destDir, final String lineSeparator) {
        //-- open output file
        String filename = getFilename(destDir);

        File file = new File(filename);
        JSourceWriter jsw = null;
        try {
            jsw = new JSourceWriter(new FileWriter(file));
        } catch (IOException ioe) {
            System.out.println("unable to create class file: " + filename);
            return;
        }
        if (lineSeparator == null) {
            jsw.setLineSeparator(System.getProperty("line.separator"));
        } else {
            jsw.setLineSeparator(lineSeparator);
        }
        print(jsw);
        jsw.close();
    }

    /**
     * Prints the source code for this JStructure to the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    public abstract void print(JSourceWriter jsw);

    /**
     * A utility method that prints the header to the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    public final void printHeader(final JSourceWriter jsw) {
        if (jsw == null) {
            throw new IllegalArgumentException(JSW_SHOULD_NOT_BE_NULL);
        }

        //-- write class header
        if (_header != null) {
            _header.print(jsw);
        } else {
            jsw.writeln("/*");
            jsw.writeln(" * " + DEFAULT_HEADER);
            jsw.writeln(" */");
        }
        jsw.writeln();
        jsw.flush();
    }

    /**
     * A utility method that prints the packageDeclaration to the given
     * JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    public final void printPackageDeclaration(final JSourceWriter jsw) {
        if (jsw == null) {
            throw new IllegalArgumentException(JSW_SHOULD_NOT_BE_NULL);
        }

        //-- print package name
        if ((_packageName != null) && (_packageName.length() > 0)) {
            jsw.write("package ");
            jsw.write(_packageName);
            jsw.writeln(';');
            jsw.writeln();
        }
        jsw.flush();
    }

    /**
     * A utility method that prints the imports to the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    protected final void printImportDeclarations(final JSourceWriter jsw) {
        if (jsw == null) {
            throw new IllegalArgumentException(JSW_SHOULD_NOT_BE_NULL);
        }

        //-- print imports
        if (_imports.size() > 0) {
            jsw.writeln("  //---------------------------------/");
            jsw.writeln(" //- Imported classes and packages -/");
            jsw.writeln("//---------------------------------/");
            jsw.writeln();
            Enumeration enumeration = _imports.elements();
            while (enumeration.hasMoreElements()) {
                jsw.write("import ");
                jsw.write(enumeration.nextElement());
                jsw.writeln(';');
            }
            jsw.writeln();
            jsw.flush();
        }
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Returns the String representation of this JType.
     */
    public final String toString() {
        return getName();
    }

    //--------------------------------------------------------------------------
}

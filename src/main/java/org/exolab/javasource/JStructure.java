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
 * $Id$
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
 * <p>
 * This is a useful utility when creating in memory source code. The code in
 * this package was modelled after the Java Reflection API as much as possible
 * to reduce the learning curve.
 *
 * @author <a href="mailto:skopp AT riege DOT de">Martin Skopp</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public abstract class JStructure extends JType implements JAnnotatedElement {

    /**
     * The Id for Source control systems.
     * <p>
     * Note: I needed to break the String into two parts to prevent CVS from
     * expanding it here! ;-)
     */
    private static final String DEFAULT_HEADER = "$" + "Id$";

    /**
     * The source control version for listed in the JavaDoc
     * <p>
     * Note: I needed to break the String into parts to prevent CVS from
     * expanding it here! ;-)
     */
    private static final String DEFAULT_VERSION = "$" + "Revision$ $" + "Date$";
    /** A standard complaint for a bad parameter. */
    private static final String JSW_SHOULD_NOT_BE_NULL = "argument 'jsw' should not be null.";

    /**
     * The source header.
     */
    private JComment _header = null;

    /**
     * List of imported classes and packages.
     */
    private Vector _imports = null;

    /**
     * The set of interfaces implemented/extended by this JStructure.
     */
    private Vector _interfaces    = null;

    /**
     * The Javadoc for this JStructure.
     */
    private JDocComment _jdc      = null;

    /**
     * The JModifiers for this JStructure, which allows us to change the
     * resulting qualifiers.
     */
    private JModifiers _modifiers = null;

    /**
     * The package to which this JStructure belongs.
     */
    private String _packageName   = null;

    /**
     * Implementation of JAnnoatedElement to delagate to.
     */
    private JAnnotatedElementHelper _annotatedElement = null;

    /**
     * Creates a new JStructure with the given name.
     *
     * @param name the name of the JStructure.
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
        this._packageName = getPackageFromClassName(name);
        _imports          = new Vector();
        _interfaces       = new Vector();
        _jdc              = new JDocComment();
        _modifiers        = new JModifiers();
        _annotatedElement = new JAnnotatedElementHelper();
        //-- initialize default Java doc
        _jdc.addDescriptor(JDocDescriptor.createVersionDesc(DEFAULT_VERSION));
    } //-- JStructure

    /**
     * Adds the given JField to this JStructure.
     * <p>
     * This method is implemented by subclasses and should only accept the
     * proper fields for the subclass otherwise an IllegalArgumentException will
     * be thrown. For example a JInterface will only accept static fields.
     *
     * @param jField the JField to add
     */
    public abstract void addField(JField jField);

    /**
     * Adds the given JMember to this JStructure.
     * <p>
     * This method is implemented by subclasses and should only accept the
     * proper types for the subclass otherwise an IllegalArgumentException will
     * be thrown.
     *
     * @param jMember the JMember to add to this JStructure.
     */
    public abstract void addMember(JMember jMember);

    /**
     * Adds the given import to this JStructure.
     *
     * @param className name of the class to import.
     */
    public void addImport(final String className) {
        if (className == null || className.length() == 0) { return; }

        //-- getPackageName
        String pkgName = getPackageFromClassName(className);

        if (pkgName != null) {
            if (pkgName.equals(this._packageName) || pkgName.equals("java.lang")) {
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
    } //-- addImport

    /**
     * Adds appropriate import for this JAnnotation.
     *
     * @param annotation a JAnnotation for which we want to add an import to
     *            this JStructure
     */
    protected final void addImport(final JAnnotation annotation) {
        addImport(annotation.getAnnotationType().getName());
    }

    /**
     * Adds appropriate imports for each JAnnotation in the given Array.
     *
     * @param annotations an Array of JAnnotation; we want to add an import to
     *            this JStructure for each JAnnotation in the Array
     */
    protected final void addImport(final JAnnotation[] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            addImport(annotations[i].getAnnotationType().getName());
        }
    }

    /**
     * Adds the given interface to the list of interfaces this JStructure
     * inherits method declarations from, and either implements (JClass) or
     * extends (JInterface).
     *
     * @param interfaceName the name of the interface to "inherit" method
     *            declarations from.
     */
    public final void addInterface(final String interfaceName) {
        if (!_interfaces.contains(interfaceName)) {
            _interfaces.addElement(interfaceName);
        }
    } //-- addInterface

    /**
     * Adds the given interface to the list of interfaces this JStructure
     * inherits method declarations from, and either implements (JClass) or
     * extends (JInterface).
     *
     * @param jInterface the JInterface to inherit from.
     */
    public final void addInterface(final JInterface jInterface) {
        if (jInterface == null) { return; }
        String interfaceName = jInterface.getName();
        if (!_interfaces.contains(interfaceName)) {
            _interfaces.addElement(interfaceName);
        }
    } //-- addInterface

    /* *
     * Adds the given JMethodSignature to this JClass
     *
     * @param jMethodSig the JMethodSignature to add.
     * @throws IllegalArgumentException when the given JMethodSignature
     *             conflicts with an existing method signature.
     * /
 /*
    public void addMethod(JMethodSignature jMethodSig)
        throws IllegalArgumentException
    {
        if (jMethodSig == null) {
            String err = "The JMethodSignature cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- XXXX: check method name and signatures *add later*

        //-- keep method list sorted for esthetics when printing
        //-- START SORT :-)
        boolean added = false;
        short modifierVal = 0;
        JModifiers modifiers = jMethodSig.getModifiers();
        for (int i = 0; i < methods.size(); i++) {
            JMethodSignature tmp = (JMethodSignature) methods.elementAt(i);
            //-- first compare modifiers
            if (tmp.getModifiers().isProtected()) {
                if (!modifiers.isProtected()) {
                    methods.insertElementAt(jMethodSig, i);
                    added = true;
                    break;
                }
            }
            //-- compare names
            if (jMethodSig.getName().compareTo(tmp.getName()) < 0) {
                    methods.insertElementAt(jMethodSig, i);
                    added = true;
                    break;
            }
        }
        //-- END SORT
        if (!added) methods.addElement(jMethodSig);

        //-- check parameter packages to make sure we have them
        //-- in our import list

        String[] pkgNames = jMethodSig.getParameterClassNames();
        for (int i = 0; i < pkgNames.length; i++) {
            addImport(pkgNames[i]);
        }
        //-- check return type to make sure it's included in the
        //-- import list
        JType jType = jMethodSig.getReturnType();
        if (jType != null) {
            while (jType.isArray())
                jType = jType.getComponentType();

            if   (!jType.isPrimitive())
                 addImport(jType.getName());
        }
        //-- check exceptions
        JClass[] exceptions = jMethodSig.getExceptions();
        for (int i = 0; i < exceptions.length; i++) {
            addImport(exceptions[i].getName());
        }
    } //-- addMethod
*/

    /**
     * Returns the field with the given name, or null if no field was found with
     * that name.
     *
     * @param name the name of the field to return.
     * @return the field with the given name, or null if no field was found with
     *         the given name.
     */
    public abstract JField getField(String name);

    /**
     * Returns an array of all the JFields of this JStructure.
     *
     * @return an array of all the JFields of this JStructure.
     */
    public abstract JField[] getFields();

    /**
     * Returns the name of the file that this JStructure would be printed to,
     * given a call to {@link #print(String, String)}, or given a call to
     * {@link #print()} if the parameter destDir is null.
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
    } //-- getFilename

    /**
     * Returns the JComment header to display at the top of the source file for
     * this JStructure, or null if no header was set.
     *
     * @return the JComment header or null if none exists.
     */
    public final JComment getHeader() {
        return this._header;
    } //-- getHeader

    /**
     * Returns an Enumeration of imported package and class names for this
     * JStructure.
     *
     * @return the Enumeration of imports. May be empty but will not be null.
     */
    public final Enumeration getImports() {
        return _imports.elements();
    } //-- getImports

    /**
     * Returns an Enumeration of interface names that this JStructure inherits
     * from.
     *
     * @return the Enumeration of interface names for this JStructure. May be
     *         empty but will not be null.
     */
    public final Enumeration getInterfaces() {
        return _interfaces.elements();
    } //-- getInterfaces

    /**
     * Returns the JavaDoc comment for this JStructure.
     *
     * @return the JDocComment for this JStructure.
     */
    public final JDocComment getJDocComment() {
        return _jdc;
    } //-- getJDocComment

    /**
     * Returns the object managing the annotations for this JStructure.
     * @return the object managing the annotations for this JStructure.
     */
    protected final JAnnotatedElementHelper getAnnotatedElementHelper() {
        return _annotatedElement;
    } //-- getAnnotatedElementHelper

    /* *
     * Returns an array of all the JMethodSignatures of this JInterface.
     *
     * @return an array of all the JMethodSignatures of this JInterface.
     * /
/*
    public JMethodSignature[] getMethods() {
        JMethodSignature[] marray = new JMethodSignature[methods.size()];
        methods.copyInto(marray);
        return marray;
    } //-- getMethods
*/

    /* *
     * Returns the JMethodSignature with the given name, and occuring at or
     * after the given starting index.
     *
     * @param name the name of the JMethodSignature to return.
     * @param startIndex the starting index to begin searching from.
     * @return the JMethodSignature, or null if not found.
     * /
/*
    public JMethodSignature getMethod(String name, int startIndex) {
        for (int i = startIndex; i < methods.size(); i++) {
            JMethodSignature jMethod = (JMethodSignature)methods.elementAt(i);
            if (jMethod.getName().equals(name)) return jMethod;
        }
        return null;
    } //-- getMethod
*/

    /* *
     * Returns the JMethodSignature at the given index.
     *
     * @param index the index of the JMethodSignature to return.
     * @return the JMethodSignature at the given index.
     * /
 /*
    public JMethodSignature getMethod(int index) {
        return (JMethodSignature)methods.elementAt(index);
    } //-- getMethod
 */

    /**
     * Returns the JModifiers, which allows the qualifiers to be changed.
     *
     * @return the JModifiers for this JStructure.
     */
    public final JModifiers getModifiers() {
        return _modifiers;
    } //-- getModifiers

    /**
     * Returns the name of the package that this JStructure is a member of.
     *
     * @return the name of the package that this JStructure is a member of, or
     *         null if there is no current package name defined.
     */
    public final String getPackageName() {
        return this._packageName;
    } //-- getPackageName

    /**
     * Returns the name of the class represented by this JStructure.
     *
     * @param stripPackage a boolean that when true indicates that only the
     *            local name (no package) should be returned.
     * @return the name of the class represented by this JStructure, including
     *         the full package if stripPackage is false.
     */
    public final String getName(final boolean stripPackage) {
        String name = super.getName();
        if (stripPackage) {
            int period = name.lastIndexOf(".");
            if (period > 0) { name = name.substring(period + 1); }
        }
        return name;
    } //-- getName

    /**
     * Returns true if the given classname exists in the imports of this
     * JStructure.
     *
     * @param classname the class name to check for
     * @return true if the given classname exists in the imports list.
     */
    public final boolean hasImport(final String classname) {
        return _imports.contains(classname);
    } //-- hasImport

    /**
     * Remove the import of the given class name from this JStucture, returning
     * true if the import was found and removed.
     *
     * @param className Name of the class to remove the import of
     * @return if the import was previously part of this JStructure, false
     *         otherwise.
     */
    public final boolean removeImport(final String className) {
        boolean result = false;
        if (className == null) { return result; }
        if (className.length() == 0) { return result; }

        result = _imports.removeElement(className);
        return result;
    } //-- removeImport

    /**
     * Test the provided name and return true if it is a valid class name.
     *
     * @param classname A class name to test.
     * @return true if the provided class name is a valid class name.
     */
    public static boolean isValidClassName(final String classname) {
        if (classname == null) { return false; }

        String name = classname;
        int beforeTypeName = name.indexOf("<");
        if (beforeTypeName > 0) {
            name = name.substring(0, beforeTypeName);
        }

        //-- ignore package information, for now
        int period = name.lastIndexOf(".");
        if (period > 0) {
            name = name.substring(period + 1);
        }

        return JNaming.isValidJavaIdentifier(name);
    } //-- isValidClassName

    /**
     * Prints the source code for this JStructure in the current working
     * directory. Sub-directories will be created if necessary for the package.
     */
    public final void print() {
        print((String) null, (String) null);
    } //-- printSrouce

    /**
     * Prints the source code for this JStructure to the destination directory.
     * Subdirectories will be created if necessary for the package.
     *
     * @param destDir directory name to use as the root directory for all output
     * @param lineSeparator the line separator to use at the end of each line.
     *            If null, then the default line separator for the runtime
     *            platform will be used.
     */
    public final void print(final String destDir, final String lineSeparator) {
        getLocalName();

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
    } //-- print

    /**
     * Prints the source code for this JStructure to the given JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to.
     */
    public abstract void print(JSourceWriter jsw);

    /**
     * A utility method that prints the header to the given JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to.
     */
    public final void printHeader(final JSourceWriter jsw) {
        if (jsw == null) {
            throw new IllegalArgumentException(JSW_SHOULD_NOT_BE_NULL);
        }

        //-- write class header
        JComment header = getHeader();
        if (header != null) {
            header.print(jsw);
        } else {
            jsw.writeln("/*");
            jsw.writeln(" * " + DEFAULT_HEADER);
            jsw.writeln(" */");
        }
        jsw.writeln();
        jsw.flush();
    } //-- printHeader

    /**
     * A utility method that prints the imports to the given JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to.
     */
    public final void printImportDeclarations(final JSourceWriter jsw) {
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
    } //-- printImportDeclarations

    /**
     * A utility method that prints the packageDeclaration to the given
     * JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to.
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
    } //-- printPackageDeclaration

    /* *
     * Prints the source code for this JStructure to the given
     * JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to.
     * /
/*
    public abstract void print(JSourceWriter jsw);


        StringBuffer buffer = new StringBuffer();


        printHeader();
        printPackageDeclaration();
        printImportDeclarations();

        //------------/
        //- Java Doc -/
        //------------/

        jdc.print(jsw);

        //-- print class information
        //-- we need to add some JavaDoc API adding comments

        buffer.setLength(0);

        if (modifiers.isPrivate()) {
            buffer.append("private ");
        }
        else if (modifiers.isPublic()) {
            buffer.append("public ");
        }

        if (modifiers.isAbstract()) {
            buffer.append("abstract ");
        }

        buffer.append("interface ");
        buffer.append(getLocalName());
        buffer.append(' ');
        if (interfaces.size() > 0) {
            boolean endl = false;
            if (interfaces.size() > 1) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
                endl = true;
            }
            buffer.append("extends ");
            for (int i = 0; i < interfaces.size(); i++) {
                if (i > 0) buffer.append(", ");
                buffer.append(interfaces.elementAt(i));
            }
            if (endl) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
            }
            else buffer.append(' ');
        }

        buffer.append('{');
        jsw.writeln(buffer.toString());
        buffer.setLength(0);
        jsw.writeln();

        jsw.indent();

        //-- print method signatures

        if (methods.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //-----------/");
            jsw.writeln(" //- Methods -/");
            jsw.writeln("//-----------/");
            jsw.writeln();
        }

        for (int i = 0; i < methods.size(); i++) {
            JMethodSignature signature = (JMethodSignature) methods.elementAt(i);
            signature.print(jsw);
            jsw.writeln(';');
        }

        jsw.unindent();
        jsw.writeln('}');
        jsw.flush();
        jsw.close();
    } //-- printSource
*/

    /**
     * Sets the header comment for this JStructure.
     *
     * @param comment the comment to display at the top of the source file when
     *            printed
     */
    public final void setHeader(final JComment comment) {
        this._header = comment;
    } //-- setHeader

    /**
     * Allows changing the package name of this JStructure.
     *
     * @param packageName the package name to use
     */
    public void setPackageName(final String packageName)  {
        this._packageName = packageName;
        changePackage(packageName);
    } //-- setPackageName

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #addAnnotation(org.exolab.javasource.JAnnotation)
     * {@inheritDoc}
     */
    public final void addAnnotation(final JAnnotation annotation) {
        _annotatedElement.addAnnotation(annotation);
        addImport(annotation);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #getAnnotation(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public final JAnnotation getAnnotation(final JAnnotationType annotationType) {
        return _annotatedElement.getAnnotation(annotationType);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement#getAnnotations()
     * {@inheritDoc}
     */
    public final JAnnotation[] getAnnotations() {
        return _annotatedElement.getAnnotations();
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #isAnnotationPresent(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public final boolean isAnnotationPresent(final JAnnotationType annotationType) {
        return _annotatedElement.isAnnotationPresent(annotationType);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #removeAnnotation(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public final JAnnotation removeAnnotation(final JAnnotationType annotationType) {
        return _annotatedElement.removeAnnotation(annotationType);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement#hasAnnotations()
     * {@inheritDoc}
     */
    public final boolean hasAnnotations() {
        return _annotatedElement.hasAnnotations();
    }

    //---------------------/
    //- Protected Methods -/
    //---------------------/

    /**
     * Return the count of the number of Interfaces that have been added to this
     * JStructure.
     *
     * @return the count of the number of Interfaces that have been added to
     *         this JStructure.
     */
    protected final int getInterfaceCount() {
        return _interfaces.size();
    }

    /**
     * Prints the given source string to the JSourceWriter using the given
     * prefix at the beginning of each new line.
     *
     * @param prefix the prefix for each new line.
     * @param source the source code to print
     * @param jsw the JSourceWriter to print to.
     */
    protected static void printlnWithPrefix(final String prefix, final String source,
            final JSourceWriter jsw) {
        jsw.write(prefix);
        if (source == null) { return; }

        char[] chars = source.toCharArray();
        int lastIdx = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '\n') {
                //-- free buffer
                jsw.write(chars, lastIdx, (i - lastIdx) + 1);
                lastIdx = i + 1;
                if (i < chars.length) {
                    jsw.write(prefix);
                }
            }
        }
        //-- free buffer
        if (lastIdx < chars.length) {
            jsw.write(chars, lastIdx, chars.length - lastIdx);
        }
        jsw.writeln();
    } //-- printlnWithPrefix

    /**
     * Returns the package name from the given class name.
     *
     * @param className an arbitrary class name, optionally including a package
     * @return the package name from the given class name.
     */
    protected static String getPackageFromClassName(final String className) {
        int idx = className.lastIndexOf('.');
        if (idx > 0) { return className.substring(0, idx); }
        return null;
    } //-- getPackageFromClassName

} //-- JStructure

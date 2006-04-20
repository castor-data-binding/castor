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
import java.io.Writer;
import java.io.PrintWriter;

import java.util.Vector;

/**
 * A representation of the Java Source code for a Java Interface. 
 * This is a useful utility when creating in memory source code.
 * The code in this package was modelled after the Java Reflection API
 * as much as possible to reduce the learning curve.
 *
 * @author <a href="mailto:skopp@riege.de">Martin Skopp</a> 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JInterface extends JType {

    /**
     * The Id for Source control systems
     * I needed to separate this line to prevent CVS from
     * expanding it here! ;-)
    **/
    private static final String DEFAULT_HEADER
        = "$"+"Id$";

    /**
     * The version for JavaDoc
     * I needed to separate this line to prevent CVS from
     * expanding it here! ;-)
    **/
    private static final String version = "$"+"Revision$ $"+"Date$";

    private JComment header = null;

    /**
     * List of imported classes and packages
    **/
    private Vector imports = null;

    /**
     * The set of interfaces implemented by this JClass
    **/
    private Vector interfaces    = null;

    /**
     * The Javadoc for this JInterface
    **/
    private JDocComment jdc      = null;
    
    /**
     * The list of methods of this JClass
    **/
    private Vector methods       = null;

    /**
     * The JModifiers for this JClass, which allows us to
     * change the resulting qualifiers
    **/
    private JModifiers modifiers = null;

    /**
     * The package to which this JClass belongs
    **/
    private String packageName   = null;

    /**
     * Creates a new JInterface with the given name.
     *
     * @param name the name of the JInterface.
     * @throws IllegalArgumentException when the given name
     * is not a valid Class name.
    **/
    public JInterface(String name)
        throws IllegalArgumentException
    {
        super(name);
        
        //-- verify name is a valid java class name
        if (!isValidClassName(name)) {
            String lname = getLocalName();
            String err = "'" + lname + "' is ";
            if (JNaming.isKeyword(lname))
                err += "a reserved word and may not be used as "
                    + " a class name.";
            else 
                err += "not a valid Java identifier.";

            throw new IllegalArgumentException(err);
        }
        this.packageName = getPackageFromClassName(name);
        imports          = new Vector();
        interfaces       = new Vector();
        jdc              = new JDocComment();
        methods          = new Vector();
        modifiers        = new JModifiers();
        //-- initialize default Java doc
        jdc.appendComment("Interface " + getLocalName() + ".");
        jdc.addDescriptor(JDocDescriptor.createVersionDesc(version));
        
    } //-- JInterface


    public void addImport(String className) {
        if (className == null) return;
        if (className.length() == 0) return;

        //-- getPackageName
        String pkgName = getPackageFromClassName(className);

        if (pkgName != null) {
            if (pkgName.equals(this.packageName)) return;
            if (pkgName.equals("java.lang")) return;

            //-- for readabilty keep import list sorted, and make sure
            //-- we do not include more than one of the same import
            for (int i = 0; i < imports.size(); i++) {
                String imp = (String)imports.elementAt(i);
                if (imp.equals(className)) return;
                if (imp.compareTo(className) > 0) {
                    imports.insertElementAt(className, i);
                    return;
                }
            }
            imports.addElement(className);
        }
    } //-- addImport

    /**
     * Adds the given interface to the list of interfaces this
     * JInterface extends.
     *
     * @param interfaceName the name of the interface to extend.
    **/
    public void addInterface(String interfaceName) {
        if (!interfaces.contains(interfaceName))
            interfaces.addElement(interfaceName);
    } //-- addInterface

    /**
     * Adds the given interface to the list of interfaces this
     * JInterface extends.
     *
     * @param jInterface the interface to extend.
    **/
    public void addInterface(JInterface jInterface) {
        if (jInterface == null) return;
        String interfaceName = jInterface.getName();
        if (!interfaces.contains(interfaceName)) {
            interfaces.addElement(interfaceName);
        }
    } //-- addInterface


    /**
     * Adds the given JMethodSignature to this JClass
     *
     * @param jMethodSig the JMethodSignature to add.
     * @throws IllegalArgumentException when the given
     * JMethodSignature conflicts with an existing
     * method signature.
    **/
    public void addMethod(JMethodSignature jMethodSig)
        throws IllegalArgumentException
    {
        if (jMethodSig == null) {
            String err = "The JMethodSignature cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- check method name and signatures *add later*

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
                 addImport( ((JClass)jType).getName());
        }
        //-- check exceptions
        JClass[] exceptions = jMethodSig.getExceptions();
        for (int i = 0; i < exceptions.length; i++) {
            addImport(exceptions[i].getName());
        }
    } //-- addMethod

    /**
     * Returns the name of the file that this JInterface would be
     * printed as, given a call to #print.
     *
     * @param destDir the destination directory. This may be null.
     * @return the name of the file that this JInterface would be
     * printed as, given a call to #print.
    **/
    public String getFilename(String destDir) {

        String filename = getLocalName() + ".java";

		//-- Convert Java package to path string
		String javaPackagePath = "";
        if ((packageName != null) && (packageName.length() > 0)) {
            javaPackagePath = packageName.replace('.',File.separatorChar);
		}

		//-- Create fully qualified path (including 'destDir') to file
        File pathFile;
        if (destDir==null)
            pathFile=new File(javaPackagePath);
        else
            pathFile=new File(destDir,javaPackagePath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

		//-- Prefix filename with path
		if (pathFile.toString().length()>0)
			filename = pathFile.toString()+File.separator+filename;

        return filename;
    } //-- getFilename

    /**
     * Returns the Java Doc comment for this JClass
     * @return the JDocComment for this JClass
    **/
    public JDocComment getJDocComment() {
        return jdc;
    } //-- getJDocComment

    /**
     * Returns an array of all the JMethodSignatures of this JInterface.
     *
     * @return an array of all the JMethodSignatures of this JInterface.
    **/
    public JMethodSignature[] getMethods() {
        JMethodSignature[] marray = new JMethodSignature[methods.size()];
        methods.copyInto(marray);
        return marray;
    } //-- getMethods

    /**
     * Returns the JMethodSignature with the given name,
     * and occuring at or after the given starting index.
     *
     * @param name the name of the JMethodSignature to return.
     * @param startIndex the starting index to begin searching
     * from. 
     * @return the JMethodSignature, or null if not found.
    **/
    public JMethodSignature getMethod(String name, int startIndex) {
        for (int i = startIndex; i < methods.size(); i++) {
            JMethodSignature jMethod = (JMethodSignature)methods.elementAt(i);
            if (jMethod.getName().equals(name)) return jMethod;
        }
        return null;
    } //-- getMethod

    /**
     * Returns the JMethodSignature at the given index.
     *
     * @param index the index of the JMethodSignature to return.
     * @return the JMethodSignature at the given index.
    **/
    public JMethodSignature getMethod(int index) {
        return (JMethodSignature)methods.elementAt(index);
    } //-- getMethod

    /**
     * Returns the JModifiers which allows the qualifiers to be changed.
     *
     * @return the JModifiers for this JInterface.
    **/
    public JModifiers getModifiers() {
        return modifiers;
    } //-- getModifiers

    /**
     * Returns the name of the package that this JInterface is a member 
     * of.
     *
     * @return the name of the package that this JInterface is a member 
     * of, or null if there is no current package name defined.
    **/
    public String getPackageName() {
        return this.packageName;
    } //-- getPackageName


    /**
     * Returns the name of the interface.
     *
     * @param stripPackage a boolean that when true indicates that only
     * the local name (no package) should be returned.
     * @return the name of the class.
    **/
	public String getName(boolean stripPackage) {
		String name = super.getName();
		if (stripPackage)
		{
			int period = name.lastIndexOf(".");
			if (period>0)
				name = name.substring(period+1);
		}
		return name;
	} //-- getName


    public boolean removeImport(String className) {
        boolean result = false;
        if (className == null) return result;
        if (className.length() == 0) return result;

        result = imports.removeElement(className);
        return result;
    } //-- removeImport

    
    public static boolean isValidClassName(String name) {
        
        if (name == null) return false;
        
        //-- ignore package information, for now
		int period = name.lastIndexOf(".");
		if (period>0)
			name = name.substring(period+1);
	    
	    return JNaming.isValidJavaIdentifier(name);
    } //-- isValidClassName

    public void print() {
        print(null,null);
    } //-- printSrouce

    /**
     * Prints the source code for this JInterface
     *
     * @param lineSeparator the line separator to use at the end of each line.
     * If null, then the default line separator for the runtime platform will
     * be used.
    **/
    public void print(String destDir, String lineSeparator) {

        String name = getLocalName();

        //-- open output file
        String filename = getFilename(destDir);

        File file = new File(filename);
        JSourceWriter jsw = null;
        try {
            jsw = new JSourceWriter(new FileWriter(file));
        }
        catch(java.io.IOException ioe) {
            System.out.println("unable to create class file: " + filename);
            return;
        }

        if (lineSeparator == null) {
            lineSeparator = System.getProperty("line.separator");
        }
        jsw.setLineSeparator(lineSeparator);

        StringBuffer buffer = new StringBuffer();


        //-- write class header
        if (header != null) header.print(jsw);
        else {
            jsw.writeln("/*");
            jsw.writeln(" * " + DEFAULT_HEADER);
            jsw.writeln("*/");
        }
        jsw.writeln();
        jsw.flush();

        //-- print package name
        if ((packageName != null) && (packageName.length() > 0)) {

            buffer.setLength(0);
            buffer.append("package ");
            buffer.append(packageName);
            buffer.append(';');
            jsw.writeln(buffer.toString());
            jsw.writeln();
        }

        //-- print imports
        jsw.writeln("  //---------------------------------/");
        jsw.writeln(" //- Imported classes and packages -/");
        jsw.writeln("//---------------------------------/");
        jsw.writeln();
        for (int i = 0; i < imports.size(); i++) {
            jsw.write("import ");
            jsw.write(imports.elementAt(i));
            jsw.writeln(';');
        }
        jsw.writeln();

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


    /**
     * Sets the header comment for this JClass
     * @param comment the comment to display at the top of the source file
     * when printed
    **/
    public void setHeader(JComment comment) {
        this.header = comment;
    } //-- setHeader

    /**
     * Allows changing the package name of this JClass
     * @param packageName the package name to use
    **/
    public void setPackageName(String packageName)  {
        this.packageName = packageName;
        changePackage(packageName);
    } //-- setPackageName


    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     *
    **/
    private void printlnWithPrefix(String prefix, String source, JSourceWriter jsw) {
        jsw.write(prefix);
        if (source == null) return;

        char[] chars = source.toCharArray();
        int lastIdx = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == '\n') {
                //-- free buffer
                jsw.write(chars,lastIdx,(i-lastIdx)+1);
                lastIdx = i+1;
                if (i < chars.length) {
                    jsw.write(prefix);
                }
            }
        }
        //-- free buffer
        if (lastIdx < chars.length) {
            jsw.write(chars, lastIdx, chars.length-lastIdx);
        }
        jsw.writeln();

    } //-- printWithPrefix


    private static String getPackageFromClassName(String className) {
        int idx = -1;
        if ((idx = className.lastIndexOf('.')) > 0) {
            return className.substring(0, idx);
        }
        return null;
    } //-- getPackageFromClassName

    /**
     * Test drive method...to be removed or commented out
    **
    public static void main(String[] args) {
        JInterface jInterface = new JInterface("Test");

        //-- add an import
        jInterface.addImport("java.util.Vector");
        JClass jString = new JClass("String");

        //-- add an interface
        jInterface.addInterface("java.io.Serializable");
        
        //-- add a method signature
        JMethodSignature jMethodSig = new JMethodSignature("getName", jString);
        jInterface.addMethod(jMethodSig);
        jInterface.print();
    } //-- main
    /* */

} //-- JInterface

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

import java.util.Enumeration;
import java.util.Vector;

/**
 * A representation of the Java Source code for a Java Interface. This is a
 * useful utility when creating in memory source code. The code in this package
 * was modelled after the Java Reflection API as much as possible to reduce the
 * learning curve.
 *
 * @author <a href="mailto:skopp AT riege DOT de">Martin Skopp</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-02-26 17:30:28 -0700 (Sat, 26 Feb 2005) $
 */
public final class JInterface extends JStructure {
    //--------------------------------------------------------------------------

    /** Default size of the map for fields. */
    private static final int DEFAULT_FIELD_MAP_SIZE = 3;
    
    //--------------------------------------------------------------------------

    /** The fields for this JInterface. */
    private JNamedMap _fields;
    
    /** The list of methods of this JInterface. */
    private Vector<JMethodSignature> _methods;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JInterface with the given name.
     *
     * @param name The name of the JInterface.
     */
    public JInterface(final String name) {
        super(name);
        
        _fields = null;
        _methods = new Vector<JMethodSignature>();

        //-- initialize default Java doc
        getJDocComment().appendComment("Interface " + getLocalName() + ".");
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void addImport(final String className) {
        if (className == null || className.length() == 0) { return; }
        addImportInternal(className);
    }

    /**
     * Adds the given JMember to this Jinterface.
     * <p>
     * This method is implemented by subclasses and should only accept the
     * proper types for the subclass otherwise an IllegalArgumentException will
     * be thrown.
     *
     * @param jMember The JMember to add to this JStructure.
     */
    public void addMember(final JMember jMember) {
        if (jMember == null) {
            throw new IllegalArgumentException("argument 'jMember' may not be null.");
        }
        if (jMember instanceof JField) {
            addField((JField) jMember);
        } else {
            throw new IllegalArgumentException("invalid member for JInterface: "
                    + jMember.toString());
        }
    }

    /**
     * Returns an array of all the JFields of this Jinterface.
     *
     * @return An array of all the JFields of this Jinterface.
     */
    public JField[] getFields() {
        if (_fields == null) {
            return new JField[0];
        }
        int size = _fields.size();
        JField[] farray = new JField[size];
        for (int i = 0; i < size; i++) {
            farray[i] = (JField) _fields.get(i);
        }
        return farray;
    }

    /**
     * Returns the field with the given name, or null if no field was found with
     * the given name.
     *
     * @param name The name of the field to return.
     * @return The field with the given name, or null if no field was found with
     *         the given name.
     */
    public JField getField(final String name) {
        if (_fields == null) { return null; }
        return (JField) _fields.get(name);
    }

    /**
     * Adds the given JField to this Jinterface.
     * <p>
     * This method is implemented by subclasses and should only accept the
     * proper fields for the subclass otherwise an IllegalArgumentException will
     * be thrown. For example a JInterface will only accept static fields.
     *
     * @param jField The JField to add.
     */
    public void addField(final JField jField) {
        if (jField == null) {
            throw new IllegalArgumentException("argument 'jField' cannot be null");
        }

        String name = jField.getName();

        //-- check for duplicate field name
        if ((_fields != null) && (_fields.get(name) != null)) {
            String err = "duplicate name found: " + name;
            throw new IllegalArgumentException(err);
        }

        //-- check for proper modifiers
        JModifiers modifiers = jField.getModifiers();
        if (!modifiers.isStatic()) {
            String err = "Fields added to a JInterface must be static.";
            throw new IllegalArgumentException(err);
        }
        if (modifiers.isPrivate()) {
            String err = "Fields added to a JInterface must not be private.";
            throw new IllegalArgumentException(err);
        }

        //-- only initialize fields if we need it, many interfaces
        //-- don't contain any fields, no need to waste space
        if (_fields == null) {
            _fields = new JNamedMap(DEFAULT_FIELD_MAP_SIZE);
        }

        _fields.put(name, jField);

        // if member is of a type not imported by this class
        // then add import
        JType type = jField.getType();
        while (type.isArray()) {
            type = ((JArrayType) type).getComponentType();
        }
        if (!type.isPrimitive()) {
            addImport(((JClass) type).getName());
        }

        // ensure annotation classes are imported
        addImport(jField.getAnnotations());
    }

    /**
     * Returns an array of all the JMethodSignatures of this JInterface.
     *
     * @return An array of all the JMethodSignatures of this JInterface.
     */
    public JMethodSignature[] getMethods() {
        JMethodSignature[] marray = new JMethodSignature[_methods.size()];
        _methods.copyInto(marray);
        return marray;
    }

    /**
     * Returns the JMethodSignature with the given name and occuring at or
     * after the given starting index.
     *
     * @param name The name of the JMethodSignature to return.
     * @param startIndex The starting index to begin searching from.
     * @return The JMethodSignature, or null if not found.
     */
    public JMethodSignature getMethod(final String name, final int startIndex) {
        for (int i = startIndex; i < _methods.size(); i++) {
            JMethodSignature jMethod = _methods.elementAt(i);
            if (jMethod.getName().equals(name)) { return jMethod; }
        }
        return null;
    }

    /**
     * Returns the JMethodSignature at the given index.
     *
     * @param index The index of the JMethodSignature to return.
     * @return The JMethodSignature at the given index.
     */
    public JMethodSignature getMethod(final int index) {
        return _methods.elementAt(index);
    }

    /**
     * Adds the given JMethodSignature to this Jinterface.
     *
     * @param jMethodSig The JMethodSignature to add.
     */
    public void addMethod(final JMethodSignature jMethodSig) {
        if (jMethodSig == null) {
            String err = "The JMethodSignature cannot be null.";
            throw new IllegalArgumentException(err);
        }

        //-- check method name and signatures *add later*

        //-- keep method list sorted for esthetics when printing
        //-- START SORT :-)
        boolean added = false;
        JModifiers modifiers = jMethodSig.getModifiers();
        for (int i = 0; i < _methods.size(); i++) {
            JMethodSignature tmp = _methods.elementAt(i);
            //-- first compare modifiers
            if (tmp.getModifiers().isProtected() && !modifiers.isProtected()) {
                _methods.insertElementAt(jMethodSig, i);
                added = true;
                break;
            }
            //-- compare names
            if (jMethodSig.getName().compareTo(tmp.getName()) < 0) {
                _methods.insertElementAt(jMethodSig, i);
                added = true;
                break;
            }
        }
        //-- END SORT
        if (!added) { _methods.addElement(jMethodSig); }

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
            while (jType.isArray()) {
                jType = ((JArrayType) jType).getComponentType();
            }
            if (!jType.isPrimitive()) {
                addImport(jType.getName());
            }
        }
        //-- check exceptions
        JClass[] exceptions = jMethodSig.getExceptions();
        for (int i = 0; i < exceptions.length; i++) {
            addImport(exceptions[i].getName());
        }
        //-- ensure method and parameter annotations imported
        addImport(jMethodSig.getAnnotations());
        JParameter[] params = jMethodSig.getParameters();
        for (int i = 0; i < params.length; i++) {
            addImport(params[i].getAnnotations());
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void print(final JSourceWriter jsw) {
        print(jsw, false);
    }

    /**
     * Prints the source code for this JInterface to the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to. Must not be null.
     * @param classOnly If true, generates the class body without the class
     *            header, package declaration, or imports.
     */
    public void print(final JSourceWriter jsw, final boolean classOnly) {
        if (jsw == null) {
            throw new IllegalArgumentException("argument 'jsw' should not be null.");
        }

        StringBuilder buffer = new StringBuilder(100);

        if (!classOnly) {
            printHeader(jsw);
            printPackageDeclaration(jsw);
            printImportDeclarations(jsw);
        }

        getJDocComment().print(jsw);

        getAnnotatedElementHelper().printAnnotations(jsw);

        buffer.setLength(0);

        JModifiers modifiers = getModifiers();
        if (modifiers.isPrivate()) {
            buffer.append("private ");
        } else if (modifiers.isPublic()) {
            buffer.append("public ");
        }

        if (modifiers.isAbstract()) {
            buffer.append("abstract ");
        }

        buffer.append("interface ");
        buffer.append(getLocalName());
        buffer.append(' ');
        if (getInterfaceCount() > 0) {
            Enumeration<String> enumeration = getInterfaces();
            boolean endl = false;
            if (getInterfaceCount() > 1) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
                endl = true;
            }
            buffer.append("extends ");
            while (enumeration.hasMoreElements()) {
                buffer.append(enumeration.nextElement());
                if (enumeration.hasMoreElements()) { buffer.append(", "); }
            }
            if (endl) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
            } else {
                buffer.append(' ');
            }
        }

        buffer.append('{');
        jsw.writeln(buffer.toString());
        buffer.setLength(0);
        jsw.writeln();

        jsw.indent();

        //-- declare static members

        if (_fields != null) {
            if (_fields.size() > 0) {
                jsw.writeln();
                jsw.writeln("  //--------------------------/");
                jsw.writeln(" //- Class/Member Variables -/");
                jsw.writeln("//--------------------------/");
                jsw.writeln();
            }

            for (int i = 0; i < _fields.size(); i++) {

                JField jField = (JField) _fields.get(i);

                //-- print Java comment
                JDocComment comment = jField.getComment();
                if (comment != null) { comment.print(jsw); }

                // -- annotations
                jField.printAnnotations(jsw);

                // -- print member
                jsw.write(jField.getModifiers().toString());
                jsw.write(' ');

                JType type = jField.getType();
                String typeName = type.toString();
                //-- for esthetics use short name in some cases
                if (typeName.equals(toString())) {
                    typeName = type.getLocalName();
                }
                jsw.write(typeName);
                jsw.write(' ');
                jsw.write(jField.getName());

                String init = jField.getInitString();
                if (init != null) {
                    jsw.write(" = ");
                    jsw.write(init);
                }

                jsw.writeln(';');
                jsw.writeln();
            }
        }

        //-- print method signatures

        if (_methods.size() > 0) {
            jsw.writeln();
            jsw.writeln("  //-----------/");
            jsw.writeln(" //- Methods -/");
            jsw.writeln("//-----------/");
            jsw.writeln();
        }

        for (int i = 0; i < _methods.size(); i++) {
            JMethodSignature signature = _methods.elementAt(i);
            signature.print(jsw);
            jsw.writeln(';');
        }

        jsw.unindent();
        jsw.writeln('}');
        jsw.flush();
        jsw.close();
    }

    //--------------------------------------------------------------------------
}

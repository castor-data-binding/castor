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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 */
package org.exolab.javasource;

import java.util.Enumeration;

/**
 * A representation of the Java Source code for a Java Class. This is a useful
 * utility when creating in memory source code. This package was modelled after
 * the Java Reflection API as much as possible to reduce the learning curve.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:skopp AT riege DOT de">Martin Skopp</a>
 * @version $Revision$ $Date: 2005-05-08 12:32:06 -0600 (Sun, 08 May 2005) $
 */
public class JClass extends AbstractJClass {
    //--------------------------------------------------------------------------

    /** The superclass for this JClass. */
    private JTypeName _superClass;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JClass with the given name.
     *
     * @param name The name of the JClass to create.
     */
    public JClass(final String name) {
        super(name);
    
        _superClass = null;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final void addImport(final String className) {
        if ((className == null) || (className.length() == 0)) { return; }

        JTypeName jtName = new JTypeName(className);

        String cls = jtName.getLocalName();
        String pkg = jtName.getPackageName();

        // If we are extending a class from the default package which conflicts
        // with this import then we cannot import this class
        if (_superClass != null && _superClass.getLocalName().equals(cls)) {
            if (_superClass.getPackageName() == null && pkg != null) {
                return;
            }
        }

        addImportInternal(className);
    }

    /**
     * {@inheritDoc}
     */
    public void addMember(final JMember jMember) {
        if (jMember instanceof JField) {
            addField((JField) jMember);
        } else if (jMember instanceof JMethod) {
            addMethod((JMethod) jMember);
        } else {
            String error = null;
            if (jMember == null) {
                error = "the argument 'jMember' must not be null.";
            } else {
                error = "Cannot add JMember '" + jMember.getClass().getName()
                      + "' to JClass, unrecognized type.";
            }
            throw new IllegalArgumentException(error);
        }
    }

    /**
     * Returns the super class that this class extends.
     *
     * @return superClass The super class that this class extends.
     */
    public final JTypeName getSuperClass() {
        return _superClass;
    }

    /**
     * Returns the qualified name of the super class that this class extends.
     *
     * @return superClass The qualified name of the super class that this class extends.
     */
    public final String getSuperClassQualifiedName() {
        if (_superClass == null) { return null; }
        return _superClass.getQualifiedName();
    }

    /**
     * Sets the super Class that this class extends.
     *
     * @param superClass The super Class that this Class extends.
     */
    public final void setSuperClass(final String superClass) {
        if (superClass == null) {
            _superClass = null;
        } else {
            _superClass = new JTypeName(superClass);
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void print(final JSourceWriter jsw, final boolean classOnly) {
        if (jsw == null) {
            throw new IllegalArgumentException("argument 'jsw' should not be null.");
        }

        //-- print class headers (comment header, package, imports) if desired
        if (!classOnly) {
            printClassHeaders(jsw);
        }

        getJDocComment().print(jsw);

        printClassDefinitionLine(jsw); // includes the opening '{'

        jsw.writeln();
        jsw.indent();

        printMemberVariables(jsw);
        printStaticInitializers(jsw);
        printConstructors(jsw);
        printMethods(jsw);
        printInnerClasses(jsw);

        jsw.unindent();
        jsw.writeln('}');
        jsw.flush();
    }

    /**
     * Writes to the JSourceWriter the line that defines this class.  This
     * line includes the class name, extends and implements entries, and
     * any modifiers such as "private".
     * 
     * @param jsw The JSourceWriter to be used.
     */
    private void printClassDefinitionLine(final JSourceWriter jsw) {
        StringBuilder buffer = new StringBuilder(32);

        //-- first print our annotations
        getAnnotatedElementHelper().printAnnotations(jsw);

        //-- next our modifiers
        JModifiers modifiers = getModifiers();
        if (modifiers.isPrivate()) {
            buffer.append("private ");
        } else if (modifiers.isPublic()) {
            buffer.append("public ");
        }

        if (modifiers.isFinal()) {
            buffer.append ("final ");
        }

        if (modifiers.isAbstract()) {
            buffer.append("abstract ");
        }

        //-- next our class name plus extends and implements entries
        buffer.append("class ");
        buffer.append(getLocalName());
        buffer.append(' ');
        if (_superClass != null) {
            buffer.append("extends ");
            buffer.append(_superClass);
            buffer.append(' ');
        }
        if (getInterfaceCount() > 0) {
            boolean endl = false;
            if ((getInterfaceCount() > 1) || (_superClass != null)) {
                jsw.writeln(buffer.toString());
                buffer.setLength(0);
                endl = true;
            }
            buffer.append("implements ");

            Enumeration<String> enumeration = getInterfaces();
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

        //-- and we're done
        buffer.append('{');
        jsw.writeln(buffer.toString());
        buffer.setLength(0);
    }

        
    /**
     * Changes the local name of this class type.
     * @param localName The new local name to be used.
     */
    public void changeLocalName(final String localName) {
        String packageName = getPackageName();
        if (packageName != null) {
            setName(packageName + "." + localName);
        } else {
            setName(localName);
        }
    }

    //--------------------------------------------------------------------------
}

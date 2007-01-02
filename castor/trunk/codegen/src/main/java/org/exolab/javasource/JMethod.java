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

import java.util.Vector;

/**
 * A class which holds information about the methods of a JClass. Modelled
 * closely after the Java Reflection API. This class is part of package which
 * is used to create source code.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-03 11:57:33 -0700 (Fri, 03 Dec 2004) $
 */
public final class JMethod implements JMember, JAnnotatedElement {
    //--------------------------------------------------------------------------

    /** The set of classes that contain this JMethod. */
    private final Vector _classes;
    
    /** The JavaDoc comment for this JMethod. This will overwrite the JavaDoc for
     *  the JMethodSignature. */
    private JDocComment _jdc = null;
    
    /** The source code for this method. */
    private JSourceCode _source = null;
    
    /** The signature for this method. */
    private JMethodSignature _signature = null;

    //--------------------------------------------------------------------------

    /**
     * Creates a new JMethod with the given name and "void" return type.
     *
     * @param name The method name. Must not be null.
     */
    public JMethod(final String name) {
        if (name == null || name.length() == 0) {
            String err = "The method name must not be null or zero-length";
            throw new IllegalArgumentException(err);
        }
        _classes = new Vector(1);
        _source = new JSourceCode();
        _signature = new JMethodSignature(name);
        _jdc = _signature.getJDocComment();
    }

    /**
     * Creates a new JMethod with the given name and returnType. The return type
     * must not be empty or null. For "void" return types, use
     * {@link #JMethod(String)} instead of this constructor.
     *
     *
     * @param name The method name. Must not be null.
     * @param returnType The return type of the method. Must not be null.
     * @param returnDoc Javadoc comment for the &#064;return annotation. If
     *            null, a default (and mostly useless) javadoc comment will be
     *            generated.
     */
    public JMethod(final String name, final JType returnType, final String returnDoc) {
        this(name);
        
        _signature = new JMethodSignature(name, returnType);
        _jdc = _signature.getJDocComment();
        _jdc.appendComment("Method " + name + "\n\n");
        if (returnDoc != null && returnDoc.length() > 0) {
            _jdc.addDescriptor(JDocDescriptor.createReturnDesc(returnDoc));
        } else {
            _jdc.addDescriptor(JDocDescriptor.createReturnDesc(returnType.getLocalName()));
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Adds the given Exception to this JMethod's throws clause.
     *
     * @param exp The JClass representing the Exception.
     * @param description JavaDoc comment explaining when this exception is thrown.
     */
    public void addException(final JClass exp, final String description) {
        _signature.addException(exp);
        _jdc.addDescriptor(JDocDescriptor.createExceptionDesc(exp.getName(), description));
    }

    /**
     * Adds the given parameter to this JMethod's list of parameters.
     *
     * @param parameter The parameter to add to the this JMethod's list of
     *        parameters.
     */
    public void addParameter(final JParameter parameter) {
        _signature.addParameter(parameter);

        //-- be considerate and add the class name to the
        //-- each declaring class' list of imports
        JType jType = parameter.getType();
        while (jType.isArray() || jType instanceof JCollectionType) {
            if (jType.isArray()) {
                jType = ((JArrayType) jType).getComponentType();
            } else {
                jType = ((JCollectionType) jType).getComponentType();
            }
        }
        if (!jType.isPrimitive()) {
            JClass jClass = (JClass) jType;
            for (int i = 0; i < _classes.size(); i++) {
                ((JClass) _classes.elementAt(i)).addImport(jClass.getName());
            }
        }
    }

    /**
     * Returns the JavaDoc comment describing this JMethod.
     *
     * @return The JavaDoc comment describing this JMethod.
     */
    public JDocComment getJDocComment() {
        return _jdc;
    }

    /**
     * Returns the exceptions that this JMethod throws.
     *
     * @return The exceptions that this JMethod throws.
     */
    public JClass[] getExceptions() {
        return _signature.getExceptions();
    }

    /**
     * Returns the modifiers for this JMethod.
     *
     * @return The modifiers for this JMethod.
     */
    public JModifiers getModifiers() {
        return _signature.getModifiers();
    }

    /**
     * Returns the name of this JMethod.
     *
     * @return The name of this JMethod.
     */
    public String getName() {
        return _signature.getName();
    }

    /**
     * Returns the JParameter at the given index.
     *
     * @param index The index of the JParameter to return.
     * @return The JParameter at the given index.
     */
    public JParameter getParameter(final int index) {
        return _signature.getParameter(index);
    }

    /**
     * Returns the set of JParameters for this JMethod.
     * <br/>
     * <B>Note:</B> the array is a copy, the parameters in the array are the
     * actual references.
     *
     * @return The set of JParameters for this JMethod.
     */
    public JParameter[] getParameters() {
        return _signature.getParameters();
    }

    /**
     * Returns the JType that represents the return type of the JMethod.
     *
     * @return The JType that represents the return type of the JMethod.
     */
    public JType getReturnType() {
        return _signature.getReturnType();
    }

    /**
     * Returns the JMethodSignature for this JMethod.
     *
     * @return The JMethodSignature for this JMethod.
     */
    public JMethodSignature getSignature() {
        return _signature;
    }

    /**
     * Returns the JSourceCode for the method body.
     *
     * @return The JSourceCode for the method body.
     */
    public JSourceCode getSourceCode() {
        return this._source;
    }

    /**
     * Sets the name of this JMethod.
     *
     * @param name The name of this method.
     */
    public void setName(final String name) {
        _signature.setName(name);
    }

    /**
     * Sets the comment describing this JMethod. The comment will be printed
     * when this JMethod is printed.
     *
     * @param comment The comment for this member.
     */
    public void setComment(final String comment) {
        _jdc.setComment(comment);
    }

    /**
     * Sets the JModifiers for this JMethod. This JMethod will use only a copy
     * of the JModifiers.
     * <br/>
     * <B>Note:</B> The JModifiers will be set in the containing
     * JMethodSignature. If the JMethodSignature is used by other methods, keep
     * in mind that it will be changed.
     *
     * @param modifiers The JModifiers to set.
     */
    public void setModifiers(final JModifiers modifiers) {
        _signature.setModifiers(modifiers);
    }

    /**
     * Sets the given string as the source code (method body) for this JMethod.
     *
     * @param source The String that represents the method body.
     */
    public void setSourceCode(final String source) {
        _source = new JSourceCode(source);
    }

    /**
     * Sets the given JSourceCode as the source code (method body) for this
     * JMethod.
     *
     * @param source The JSourceCode that represents the method body.
     */
    public void setSourceCode(final JSourceCode source) {
        _source = source;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return _signature.toString();
    }

    /**
     * {@inheritDoc}
     */
    public JAnnotation getAnnotation(final JAnnotationType annotationType) {
        return _signature.getAnnotation(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public JAnnotation[] getAnnotations() {
        return _signature.getAnnotations();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isAnnotationPresent(final JAnnotationType annotationType) {
        return _signature.isAnnotationPresent(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public void addAnnotation(final JAnnotation annotation) {
        _signature.addAnnotation(annotation);
    }

    /**
     * {@inheritDoc}
     */
    public JAnnotation removeAnnotation(final JAnnotationType annotationType) {
        return _signature.removeAnnotation(annotationType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAnnotations() {
        return _signature.hasAnnotations();
    }

    //--------------------------------------------------------------------------

    /**
     * Prints this JMethod to the given JSourceWriter.
     *
     * @param jsw The JSourceWriter to print to.
     */
    public void print(final JSourceWriter jsw) {
        //------------/
        //- Java Doc -/
        //------------/

        _jdc.print(jsw);

        //--------------------/
        //- Method Signature -/
        //--------------------/

        _signature.print(jsw, false);

        if (_signature.getModifiers().isAbstract()) {
            jsw.writeln(";");
        } else {
            jsw.writeln();
            jsw.writeln("{");
            _source.print(jsw);
            jsw.write("} //-- ");
            jsw.writeln(toString());
        }
    }

    //--------------------------------------------------------------------------
}

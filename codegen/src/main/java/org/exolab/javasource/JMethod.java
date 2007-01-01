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

    /** The set of classes that contain this JMethod. */
    private final Vector _classes;
    /**
     * The JavaDoc comment for this JMethod. This will overwrite the JavaDoc for
     * the JMethodSignature. */
    private JDocComment _jdc = null;
    /** The source code for this method. */
    private JSourceCode _source = null;
    /** The signature for this method. */
    private JMethodSignature _signature = null;

    /**
     * Creates a new JMethod with the given name and "void" return type.
     *
     * @param name the method name. Must not be null.
     */
    public JMethod(final String name) {
        if (name == null || name.length() == 0) {
            String err = "The method name must not be null or zero-length";
            throw new IllegalArgumentException(err);
        }
        this._classes    = new Vector(1);
        this._source     = new JSourceCode();
        this._signature  = new JMethodSignature(name);
        this._jdc        = _signature.getJDocComment();
    } //-- JMethod

    /**
     * Creates a new JMethod with the given name and returnType. The return type
     * must not be empty or null. For "void" return types, use
     * {@link #JMethod(String)} instead of this constructor.
     *
     *
     * @param name the method name. Must not be null.
     * @param returnType the return type of the method. Must not be null.
     * @param returnDoc javadoc comment for the &#064;return annotation. If
     *            null, a default (and mostly useless) javadoc comment will be
     *            generated.
     */
    public JMethod(final String name, final JType returnType, final String returnDoc) {
        this(name);
        this._signature = new JMethodSignature(name, returnType);
        this._jdc       = _signature.getJDocComment();
        this._jdc.appendComment("Method " + name + "\n\n");
        if (returnDoc != null && returnDoc.length() > 0) {
            this._jdc.addDescriptor(JDocDescriptor.createReturnDesc(returnDoc));
        } else {
            this._jdc.addDescriptor(JDocDescriptor.createReturnDesc(returnType.getLocalName()));
        }
    } //-- JMethod

    /**
     * Adds the given Exception to this JMethod's throws clause.
     *
     * @param exp the JClass representing the Exception
     * @param description JavaDoc comment explaining when this exception is thrown
     */
    public void addException(final JClass exp, final String description) {
        this._signature.addException(exp);
        this._jdc.addDescriptor(JDocDescriptor.createExceptionDesc(exp.getName(), description));
    } //-- addException

    /**
     * Adds the given parameter to this JMethod's list of parameters.
     *
     * @param parameter the parameter to add to the this JMethod's list of
     *            parameters.
     */
    public void addParameter(final JParameter parameter) {
        _signature.addParameter(parameter);

        //-- be considerate and add the class name to the
        //-- each declaring class' list of imports
        JType jType = parameter.getType();
        while (jType instanceof JArrayType || jType instanceof JCollectionType) {
            if (jType instanceof JArrayType) {
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
    } //-- addParameter

    /**
     * Returns the JavaDoc comment describing this JMethod.
     *
     * @return the JavaDoc comment describing this JMethod.
     */
    public JDocComment getJDocComment() {
        return this._jdc;
    } //-- getJDocComment

    /* *
     * Returns the class in which this JMethod has been declared
     * @return the class in which this JMethod has been declared
     */
    /*
    public JClass getDeclaringClass() {
        return _declaringClass;
    } //-- getDeclaringClass
    */

    /**
     * Returns the exceptions that this JMethod throws.
     *
     * @return the exceptions that this JMethod throws.
     */
    public JClass[] getExceptions() {
        return _signature.getExceptions();
    } //-- getExceptions

    /**
     * Returns the modifiers for this JMethod.
     *
     * @return the modifiers for this JMethod.
     */
    public JModifiers getModifiers() {
        return _signature.getModifiers();
    } //-- getModifiers

    /**
     * Returns the name of this JMethod.
     *
     * @return the name of this JMethod.
     */
    public String getName() {
        return _signature.getName();
    } //-- getName

    /**
     * Returns the JParameter at the given index.
     *
     * @param index the index of the JParameter to return
     * @return the JParameter at the given index.
     */
    public JParameter getParameter(final int index) {
        return _signature.getParameter(index);
    } //-- getParameter

    /**
     * Returns the set of JParameters for this JMethod.
     * <p>
     * <B>Note:</B> the array is a copy, the parameters in the array are the
     * actual references
     *
     * @return the set of JParameters for this JMethod.
     */
    public JParameter[] getParameters() {
        return _signature.getParameters();
    } //-- getParameters

    /**
     * Returns the JType that represents the return type of the JMethod.
     *
     * @return the JType that represents the return type of the JMethod.
     */
    public JType getReturnType() {
        return _signature.getReturnType();
    } //-- getReturnType

    /**
     * Returns the JMethodSignature for this JMethod.
     *
     * @return the JMethodSignature for this JMethod.
     */
    public JMethodSignature getSignature() {
        return _signature;
    } //-- getSignature

    /**
     * Returns the JSourceCode for the method body.
     *
     * @return the JSourceCode for the method body.
     */
    public JSourceCode getSourceCode() {
        return this._source;
    } //-- getSourceCode

    /**
     * Sets the name of this JMethod.
     *
     * @param name the name of this method
     */
    public void setName(final String name) {
        _signature.setName(name);
    } //-- setName

    /**
     * Sets the comment describing this JMethod. The comment will be printed
     * when this JMethod is printed.
     *
     * @param comment the comment for this member
     * @see #getJDocComment
     */
    public void setComment(final String comment) {
        _jdc.setComment(comment);
    } //-- setComment

    /**
     * Sets the JModifiers for this JMethod. This JMethod will use only a copy
     * of the JModifiers.
     * <p>
     * <B>Note:</B> The JModifiers will be set in the containing
     * JMethodSignature. If the JMethodSignature is used by other methods, keep
     * in mind that it will be changed.
     *
     * @param modifiers the JModifiers to set.
     */
    public void setModifiers(final JModifiers modifiers) {
        _signature.setModifiers(modifiers);
    } //-- setModifiers

    /**
     * Sets the given string as the source code (method body) for this JMethod.
     *
     * @param source the String that represents the method body
     */
    public void setSourceCode(final String source) {
        this._source = new JSourceCode(source);
    } //-- setSource

    /**
     * Sets the given JSourceCode as the source code (method body) for this
     * JMethod.
     *
     * @param source the JSourceCode that represents the method body
     */
    public void setSourceCode(final JSourceCode source) {
        this._source = source;
    } //-- setSource;

    /**
     * Prints this JMethod to the given JSourceWriter.
     *
     * @param jsw the JSourceWriter to print to
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
    } //-- print

    /**
     * Returns the String representation of this JMethod, which is the method
     * prototype.
     *
     * @return the String representation of this JMethod, which is simply the
     *         method prototype
     */
    public String toString() {
        return _signature.toString();
    } //-- toString

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #getAnnotation(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public JAnnotation getAnnotation(final JAnnotationType annotationType) {
        return _signature.getAnnotation(annotationType);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement#getAnnotations()
     * {@inheritDoc}
     */
    public JAnnotation[] getAnnotations() {
        return _signature.getAnnotations();
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #isAnnotationPresent(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public boolean isAnnotationPresent(final JAnnotationType annotationType) {
        return _signature.isAnnotationPresent(annotationType);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #addAnnotation(org.exolab.javasource.JAnnotation)
     * {@inheritDoc}
     */
    public void addAnnotation(final JAnnotation annotation) {
        _signature.addAnnotation(annotation);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement
     *      #removeAnnotation(org.exolab.javasource.JAnnotationType)
     * {@inheritDoc}
     */
    public JAnnotation removeAnnotation(final JAnnotationType annotationType) {
        return _signature.removeAnnotation(annotationType);
    }

    /**
     * @see org.exolab.javasource.JAnnotatedElement#hasAnnotations()
     * {@inheritDoc}
     */
    public boolean hasAnnotations() {
        return _signature.hasAnnotations();
    }

    //---------------------/
    //- PROTECTED METHODS -/
    //---------------------/

    /**
     * Adds the given JClass to the set of classes that contain this method.
     *
     * @param jClass the JClass to add as one of the JClasses that contain this
     *            method
     */
    protected void addDeclaringClass(final JClass jClass) {
        _classes.addElement(jClass);
    } //-- addDeclaringClass

    /**
     * Removes the given JClass from the set of classes that contain this method.
     *
     * @param jClass the JClass to remove as one of the JClasses that contain
     *            this method
     */
    protected void removeDeclaringClass(final JClass jClass) {
        _classes.removeElement(jClass);
    } //-- removeDeclaringClass

    /**
     * Return the list of class names representing the parameters.
     *
     * @return the list of class names representing the parameters
     * @see org.exolab.javasource.JMethodSignature#getParameterClassNames
     */
    protected String[] getParameterClassNames() {
        return _signature.getParameterClassNames();
    } //-- getParameterClassNames

} //-- JMember

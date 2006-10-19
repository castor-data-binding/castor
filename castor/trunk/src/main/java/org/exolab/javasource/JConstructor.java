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
package org.exolab.javasource;

import java.util.Vector;

/**
 * A class for handling source code for a constructor of a JClass.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-05-08 05:24:54 -0600 (Sun, 08 May 2005) $
 */
public final class JConstructor extends JAnnotatedElementHelper {

    /** The set of modifiers for this JConstructor. */
    private JModifiers _modifiers = null;
    /** List of parameters for this JConstructor. */
    private JNamedMap _params       = null;
    /** The Class in this JConstructor has been declared. */
    private JClass _declaringClass = null;
    /** The source code for this constructor. */
    private JSourceCode _sourceCode = null;
    /** The exceptions that this JConstructor throws. */
    private Vector _exceptions = null;

    /**
     * Creates a new JConstructor for the provided declaring class.
     * @param declaringClass the class this constructor creates
     */
    protected JConstructor(final JClass declaringClass) {
        this._declaringClass = declaringClass;
        this._modifiers      = new JModifiers();
        this._params         = new JNamedMap();
        this._sourceCode     = new JSourceCode();
        this._exceptions     = new Vector(1);
    }

    /**
     * Adds the given Exception to this JConstructor's throws clause.
     *
     * @param exp the JClass representing the Exception
     */
    public void addException(final JClass exp) {
        if (exp == null) { return; }

        //-- make sure exception is not already added
        String expClassName = exp.getName();
        for (int i = 0; i < _exceptions.size(); i++) {
            JClass jClass = (JClass) _exceptions.elementAt(i);
            if (expClassName.equals(jClass.getName())) { return; }
        }
        //-- add exception
        _exceptions.addElement(exp);
    } //-- addException

    /**
     * Returns the exceptions that this JConstructor lists in its throws clause.
     *
     * @return the exceptions that this JConstructor lists in its throws clause.
     */
    public JClass[] getExceptions() {
        JClass[] jclasses = new JClass[_exceptions.size()];
        _exceptions.copyInto(jclasses);
        return jclasses;
    } //-- getExceptions

    /**
     * Adds the given parameter to this JConstructor's list of parameters.
     *
     * @param parameter the parameter to add to the this JConstructor's list of
     *            parameters.
     */
    public void addParameter(final JParameter parameter) {
        if (parameter == null) { return; }

        //-- check current params
        if (_params.get(parameter.getName()) != null) {
            StringBuffer err = new StringBuffer();
            err.append("A parameter already exists for the constructor, ");
            err.append(this._declaringClass.getName());
            err.append(", with the name: ");
            err.append(parameter.getName());
            throw new IllegalArgumentException(err.toString());
        }

        _params.put(parameter.getName(), parameter);

        //-- be considerate and add the class name to the
        //-- declaring class's list of imports
        if (_declaringClass != null) {
            JType jType = parameter.getType();
            if (!jType.isPrimitive()) {
                _declaringClass.addImport(jType.getName());
            }
        }
    } //-- addParameter

    /**
     * Returns the class in which this JConstructor has been declared.
     *
     * @return the class in which this JConstructor has been declared.
     */
    public JClass getDeclaringClass() {
        return this._declaringClass;
    } //-- getDeclaringClass

    /**
     * Returns the modifiers for this JConstructor.
     *
     * @return the modifiers for this JConstructor.
     */
    public JModifiers getModifiers() {
        return this._modifiers;
    } //-- getModifiers

    /**
     * Returns an array of JParameters consisting of the parameters of this
     * JConstructor in declared order.
     *
     * @return a JParameter array consisting of the parameters of this
     *         JConstructor in declared order.
     */
    public JParameter[] getParameters() {
        JParameter[] jpArray = new JParameter[_params.size()];
        for (int i = 0; i < jpArray.length; i++) {
            jpArray[i] = (JParameter) _params.get(i);
        }
        return jpArray;
    } //-- getParameters

    /**
     * Returns the source code for this JConstructor.
     * @return the source code.
     */
    public JSourceCode getSourceCode() {
        return this._sourceCode;
    } //-- getSourceCode

    /**
     * Prints this JConstructor to the provided JSourceWriter.
     * @param jsw the JSourceWriter to print the constructor to
     */
    public void print(final JSourceWriter jsw) {
        // -- print annotations
        printAnnotations(jsw);

        if (_modifiers.isPrivate()) {
            jsw.write("private");
        } else if (_modifiers.isProtected()) {
            jsw.write("protected");
        } else {
            jsw.write("public");
        }
        jsw.write(' ');
        jsw.write(_declaringClass.getLocalName());
        jsw.write('(');

        //-- any parameter annotations?
        boolean parameterAnnotations = false;
        for (int i = 0; i < _params.size(); i++) {
            JParameter jParameter = (JParameter) _params.get(i);
            if (jParameter.hasAnnotations()) {
                parameterAnnotations = true;
                break;
            }
        }

        //-- print parameters
        if (parameterAnnotations) { jsw.indent(); }
        for (int i = 0; i < _params.size(); i++) {
            if (i > 0) { jsw.write(", "); }
            if (parameterAnnotations) { jsw.writeln(); }
            JParameter jParameter = (JParameter) _params.get(i);
            jParameter.printAnnotations(jsw);
            String typeAndName = jParameter.toString();
            jsw.write(typeAndName);
        }
        if (parameterAnnotations) { jsw.unindent(); }

        jsw.writeln(") ");
        if (_exceptions.size() > 0) {
            jsw.write("    throws ");
            for (int i = 0; i < _exceptions.size(); i++) {
                if (i > 0) { jsw.write(", "); }
                JClass jClass = (JClass) _exceptions.elementAt(i);
                jsw.write(jClass.getName());
            }
            jsw.writeln();
        }
        jsw.writeln(" {");

        //jsw.indent();
        _sourceCode.print(jsw);
        //jsw.unindent();

        if (!jsw.isNewline()) { jsw.writeln(); }
        jsw.write("} //-- ");
        jsw.writeln(toString());
    } //-- print

    /**
     * Sets the modifiers on this JConstructor.
     *
     * @param modifiers modifiers to set on this constructor
     */
    public void setModifiers(final JModifiers modifiers) {
        this._modifiers = modifiers.copy();
        this._modifiers.setFinal(false);
    } //-- setModifiers

    /**
     * Sets the source code for this constructor.
     *
     * @param sourceCode source code to apply to this constructor
     */
    public void setSourceCode(final String sourceCode) {
        this._sourceCode = new JSourceCode(sourceCode);
    } //-- setSourceCode

    /**
     * Sets the source code for this constructor.
     *
     * @param sourceCode source code to apply to this constructor
     */
    public void setSourceCode(final JSourceCode sourceCode) {
        this._sourceCode = sourceCode;
    } //-- setSourceCode

    /**
     * Return the string representation of this constructor.
     *
     * @see java.lang.Object#toString()
     * {@inheritDoc}
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(_declaringClass.getName());
        sb.append('(');

        //-- print parameters
        for (int i = 0; i < _params.size(); i++) {
            JParameter jp = (JParameter) _params.get(i);
            if (i > 0) { sb.append(", "); }
            sb.append(jp.getType().getName());
        }
        sb.append(')');
        return sb.toString();
    } //-- toString

} //-- JConstructor

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
 *
 * $Id$
 */

package org.exolab.javasource;


import java.util.Vector;

/**
 * A class which holds information about the signtaure
 * of a JMethod.
 *
 * The code in this package was modelled after the Java Reflection API
 * as much as possible to reduce the learning curve.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class JMethodSignature {

    /**
     * The set of modifiers for this JMethod
    **/
    private JModifiers modifiers = null;
    
    /**
     * The return type of this Method
    **/
    private JType returnType    = null;
    
    /**
     * The name of this method
    **/
    private String name          = null;


    /**
     * The list of parameters of this JMethodSignature in declared 
     * order
    **/
    private JNamedMap params       = null;
    
    /**
     * The JavaDoc comment for this method signature.
    **/
    private JDocComment jdc = null;
    
    /**
     * The exceptions that this method throws
    **/
    private Vector exceptions = null;
    
    /**
     * Creates a new method with the given name and return type.
     * For "void" return types, simply pass in null as the returnType
     *
     * @param name, the method name. Must not be null.
     * @param returnType the return type of the method. May be null.
    **/
    public JMethodSignature(String name, JType returnType) {
        
        if ((name == null) || (name.length() == 0)) {
            String err = "The method name must not be null or zero-length";
            throw new IllegalArgumentException(err);
        }
        
        this.jdc          = new JDocComment();
        this.returnType   = returnType;
        this.name         = name;
        this.modifiers    = new JModifiers();
        this.params       = new JNamedMap(3);
        this.exceptions   = new Vector(1); 
 
        //-- add Return type descriptor
        if (returnType != null) {
            jdc.addDescriptor(JDocDescriptor.createReturnDesc(returnType.getLocalName()));
        }
    } //-- JMethodSignature

    /**
     * Adds the given Exception to this JMethodSignature's throws clause.
     *
     * @param exp the JClass representing the Exception
    **/
    public void addException(JClass exp) {
        
        if (exp == null) return;
        
        //-- make sure exception is not already added
        String expClassName = exp.getName();
        for (int i = 0; i < exceptions.size(); i++) {
            JClass jClass = (JClass) exceptions.elementAt(i);
            if (expClassName.equals(jClass.getName())) return;
        }
        //-- add exception
        exceptions.addElement(exp);
    } //-- addException
    
    /**
     * Adds the given parameter to this JMethodSignature's list of 
     * parameters.
     *
     * @param parameter the parameter to add to the this Methods
     * list of parameters.
     * @throws IllegalArgumentException when a parameter already
     * exists for this Method with the same name as the new 
     * parameter.
    **/
    public void addParameter(JParameter parameter) 
        throws IllegalArgumentException
    {
        
        if (parameter == null) return;
        
        String pName = parameter.getName();
        //-- check current params
        if (params.get(pName) != null) {
            StringBuffer err = new StringBuffer();
            err.append("A parameter already exists for this method, ");
            err.append(name);
            err.append(", with the name: ");
            err.append(pName);
            throw new IllegalArgumentException(err.toString());
        }
        
        
        params.put(pName, parameter);
        
        //-- create comment
        jdc.addDescriptor(JDocDescriptor.createParamDesc(pName, null));
        
    } //-- addParameter
    
    /**
     * Returns the exceptions that this JMethodSignature lists
     * in it's throws clause.
     *
     * @return the exceptions that this JMethodSignature lists
     * in it's throws clause.
    **/
    public JClass[] getExceptions() {
        
        JClass[] jclasses = new JClass[exceptions.size()];
        exceptions.copyInto(jclasses);
        return jclasses;
    } //-- getExceptions
    
    /**
     * Returns the JDocComment describing this JMethodSignature
     *
     * @return the JDocComment describing this JMethodSignature
    **/
    public JDocComment getJDocComment() {
        return this.jdc;
    } //-- getJDocComment
    
    /**
     * Returns the modifiers for this JMethodSignature.
     *
     * @return the modifiers for this JMethodSignature.
    **/
    public JModifiers getModifiers() {
        return this.modifiers;
    } //-- getModifiers

    /**
     * Returns the name of the method.
     *
     * @return the name of the method.
    **/
    public String getName() {
        return this.name;
    } //-- getName

    /**
     * Returns the JParameter at the given index.
     *
     * @param index the index of the JParameter to return.
     * @return the JParameter at the given index.
    **/
    public JParameter getParameter(int index) {
        return (JParameter)params.get(index);
    } //-- getParameter
    
    /**
     * Returns the set of JParameters for this JMethodSignature
     * <BR>
     * <B>Note:</B> the array is a copy, the params in the array
     * are the actual references.
     * @return the set of JParameters for this JMethod
    **/
    public synchronized JParameter[] getParameters() {
        JParameter[] pArray = new JParameter[params.size()];
        for (int i = 0; i < pArray.length; i++) {
            pArray[i] = (JParameter)params.get(i);
        }
        return pArray;
    } //-- getParameters
    
    /**
     * Returns the JType that represents the return type for the 
     * method signature.
     *
     * @return the JType that represents the return type for the
     * method signature.
    **/
    public JType getReturnType() {
        return returnType;
    } //-- getReturnType

	/**
	 * Sets the name of the method.
	 *
	 * @param name the name of the method
	 **/
	public void setName(String name) 
	{
		this.name = name;
	} //-- setName
   
    /**
     * Sets the comment describing this JMethodSignature. 
     *
     * @param comment the comment for this member
     * @see #getJDocComment
    **/
    public void setComment(String comment) {
        jdc.setComment(comment);
    } //-- setComment
    
    
    /**
     * Sets the JModifiers for this method signature.
     *
     * @param modifiers the JModifiers for this method signature.
    **/
    public void setModifiers(JModifiers modifiers) {
        this.modifiers = modifiers.copy();
        this.modifiers.setFinal(false);
    } //-- setModifiers

    /**
     * Prints the method signature. A semi-colon (end-of-statement 
     * terminator ';') will Not be printed.
     *
     * @param jsw the JSourceWriter to print to.
    **/
    public void print(JSourceWriter jsw) {
        print(jsw, true);
    } //-- print

    /**
     * Prints the method signature. A semi-colon (end-of-statement 
     * terminator ';') will Not be printed.
     *
     * @param jsw the JSourceWriter to print to.
     * @param printJavaDoc a boolean that when true prints the JDocComment
     * associated with this method signature.
    **/
    public void print(JSourceWriter jsw, boolean printJavaDoc) {
        
        //------------/
        //- Java Doc -/
        //------------/
        
        if (printJavaDoc) jdc.print(jsw);
        
        //-----------------/
        //- Method Source -/
        //-----------------/
        
        jsw.write(modifiers.toString());
        if (modifiers.toString().length() > 0) {
            jsw.write(' ');
        }
        if (returnType != null) {
            jsw.write(returnType);
        }
        else jsw.write("void");
        jsw.write(' ');
        jsw.write(name);
        jsw.write('(');
        
        //-- print parameters
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) jsw.write(", ");
            jsw.write(params.get(i));
        }
        jsw.write(")");
        
        if (exceptions.size() > 0) {
            jsw.writeln();
            jsw.write("    throws ");
            for (int i = 0; i < exceptions.size(); i++) {
                if (i > 0) jsw.write(", ");
                JClass jClass = (JClass) exceptions.elementAt(i);
                jsw.write(jClass.getName());
            }
        }        
    } //-- print


    /**
     * Returns the String representation of this JMethod,
     * which is the method prototype.
     * @return the String representation of this JMethod, which
     * is simply the method prototype
    **/
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        if (returnType != null) {
            sb.append(returnType);
        }
        else sb.append("void");
        sb.append(' ');
        sb.append(name);
        sb.append('(');
        
        //-- print parameters
        for (int i = 0; i < params.size(); i++) {
            JParameter jParam = (JParameter)params.get(i);
            if (i > 0) sb.append(", ");
            sb.append(jParam.getType().getName());
        }
        sb.append(") ");

        return sb.toString();
    } //-- toString

    protected String[] getParameterClassNames() {
        
        
        Vector names = new Vector(params.size());
        
        for (int i = 0; i < params.size(); i++) {
            
            JType  jType  = ((JParameter)params.get(i)).getType();
            while (jType.isArray()) jType = jType.getComponentType();
            if (!jType.isPrimitive()) {
                JClass jclass = (JClass)jType;
                names.addElement( jclass.getName() );
            }
        }
        
        String[] names_array = new String[names.size()];
        names.copyInto(names_array);
        return names_array;
    } //-- getParameterClassNames
    
} //-- JMethodSignature
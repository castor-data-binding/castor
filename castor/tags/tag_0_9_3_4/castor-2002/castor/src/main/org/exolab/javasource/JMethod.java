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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.javasource;


import java.util.Vector;

/**
 * A class which holds information about the methods of
 * a JClass.
 * Modelled closely after the Java Reflection API.
 * This class is part of package which is used to 
 * create source code.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JMethod implements JMember {

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
     * List of parameters of this JMethod in declared order
    **/
    private JNamedMap params       = null;
    
    /**
     * The Class in this JMember has been declared
    **/
    private JClass declaringClass = null;
    
    /**
     * The source code for this method
    **/
    private JSourceCode source = null;
    
    /**
     * The exceptions that this method throws
    **/
    private Vector exceptions = null;

    /**
     * The JavaDoc comment for this method
    **/
    private JDocComment jdc = null;
    
    /**
     * Creates a new method with the given name and returnType.
     * For "void" return types, simply pass in null as the returnType
    **/
    public JMethod(JType returnType, String name) {
        
        this.jdc          = new JDocComment();
        this.returnType   = returnType;
        this.name         = name;
        this.modifiers    = new JModifiers();
        this.params       = new JNamedMap(3);
        this.source       = new JSourceCode();
        this.exceptions   = new Vector(1); 
        
    } //-- JMethod

    
    /**
     * Adds the given Exception to this Method's throws clause
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
     * Adds the given parameter to this Methods list of parameters
     * @param parameter the parameter to add to the this Methods
     * list of parameters.
     * @exception IllegalArgumentException when a parameter already
     * exists for this Method with the same name as the new parameter
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
        
        //-- be considerate and add the class name to the
        //-- declaring class's list of imports
        if (declaringClass != null) {
            if (!parameter.getType().isPrimitive()) {
                JClass jc = (JClass)parameter.getType();
                declaringClass.addImport(jc.getName());
            }
        }
    } //-- addParameter
    
    /**
     * Returns the JDocComment describing this member. 
     * @return the JDocComment describing this member.
    **/
    public JDocComment getJDocComment() {
        return this.jdc;
    } //-- getJDocComment
    
    /**
     * Returns the class in which this JMember has been declared
     * @return the class in which this JMember has been declared
    **/
    public JClass getDeclaringClass() {
        return this.declaringClass;
    } //-- getDeclaringClass
    
    /**
     * Returns the exceptions that this JMember throws
     * @return the exceptions that this JMember throws
    **/
    public JClass[] getExceptions() {
        
        JClass[] jclasses = new JClass[exceptions.size()];
        exceptions.copyInto(jclasses);
        return jclasses;
    } //-- getExceptions


    /**
     * Returns the modifiers for this JMember
     * @return the modifiers for this JMember     
    **/
    public JModifiers getModifiers() {
        return this.modifiers;
    } //-- getModifiers

    /**
     * Returns the name of this JMember
     * @return the name of this JMember
    **/
    public String getName() {
        return this.name;
    } //-- getName

    /**
     * Returns the JParameter at the given index
     * @param index the index of the JParameter to return
     * @return the JParameter at the given index
    **/
    public JParameter getParameter(int index) {
        return (JParameter)params.get(index);
    } //-- getParameter
    
    /**
     * Returns the set of JParameters for this JMethod.
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
    
    public JType getReturnType() {
        return returnType;
    } //-- getReturnType

    public JSourceCode getSourceCode() {
        return this.source;
    } //-- getSourceCode
    
    /**
     * Sets the comment describing this member. The comment 
     * will be printed when this member is printed with the
     * Class Printer
     * @param comment the comment for this member
     * @see #getJDocComment
    **/
    public void setComment(String comment) {
        jdc.setComment(comment);
    } //-- setComment
    
    /**
     * Sets the name of this JMember
     * @param name the name of this JMember
     * @exception IllegalArgumentException when the
     * name is not a valid Java member name, or if a member
     * with the given name already exists in the declaring class
    **/
    public void setName(String name) throws 
        IllegalArgumentException
    {
        this.name = name;
    } //-- setName
    
    public void setModifiers(JModifiers modifiers) {
        this.modifiers = modifiers.copy();
        this.modifiers.setFinal(false);
    } //-- setModifiers

    protected void setDeclaringClass(JClass declaringClass) {
        this.declaringClass = declaringClass;
    } //-- setDeclaringClass

    public void setSourceCode(String source) {
        this.source = new JSourceCode(source);
    } //-- setSource

    public void setSourceCode(JSourceCode source) {
        this.source = source;
    } //-- setSource;
    
    public void print(JSourceWriter jsw) {
        
        //------------/
        //- Java Doc -/
        //------------/
        
        jdc.print(jsw);
        
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
        if (modifiers.isAbstract()) {
            jsw.writeln(";");
        }
        else {
            jsw.writeln();
            jsw.writeln("{");
            source.print(jsw);
            jsw.write("} //-- ");
            jsw.writeln(toString());
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
    
} //-- JMember

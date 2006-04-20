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

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.javasource.*;

import java.util.Vector;

/**
 * A class for representing field members of a Class. FieldInfo objects
 * hold all the information required about a member in order
 * to be able to produce marshal/unmarshal and validation code.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class FieldInfo extends XMLInfo {
 
    /**
     * The Java name for Members described by this FieldInfo
    **/
    private String name = null;
    
    
    private CodeHelper codeHelper = null;
    
    private ClassInfo declaringClassInfo = null;
    
    private String _comment    = null;
    
    /**
     * The default value for this FieldInfo
    **/
    private String _default    = null;
    
    /**
     * The fixed production for this FieldInfo
    **/
    private String _fixed      = null;
    
    /**
     * A flag to indicate a final member
    **/
    private boolean _final     = false;
    
    /**
     * A flag to indicate a static member
    **/
    private boolean _static    = false;
    
    /**
     * Flags whether or not the a MarshalDescriptor should
     * be created for this FieldInfo
    **/
    private boolean _transient = false;
    
    /**
     * A flag to indicate a bound property
    **/
    private boolean _bound = false;
    
    /**
     * Creates a new FieldInfo with the given XML Schema type
     * and the given member name
     * @param XSType the XML Schema type of this member
     * @param name the name of the member
    **/
    public FieldInfo(XSType type, String name) {
        this.name    = name;
        setSchemaType(type);
        _bound = SourceGenerator.boundPropertiesEnabled();
    } //-- FieldInfo

    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Creates the JMembers for this FieldInfo, sometimes a "field"
     * requires more than one java field
     * @return a JMember which has the appropriate type and name
     * for this FieldInfo
    **/
    public void createJavaField(JClass jClass) {
        
        XSType type = getSchemaType();
        
        JType jType = type.getJType();
        
        JField field = new JField(type.getJType(), name);
        
        if (_static || _final) {
            JModifiers modifiers = field.getModifiers();
            modifiers.setFinal(_final);
            modifiers.setStatic(_static);
        }
        
        //-- set init String
        if (_default != null) {
            field.setInitString(_default);
        }
        
        
        //-- set Javadoc comment
        if (_comment != null) field.setComment(_comment);
        
        jClass.addField(field);
        
        //-- special supporting fields
        
        //-- has_field
        if ((!type.isEnumerated()) && type.isPrimitive()) {
            field = new JField(JType.Boolean, "_has" + name);
            field.setComment("keeps track of state for field: " + name);
            jClass.addField(field);
        }
        
        //-- save default value for primitives
        //-- not yet finished
        /*
        if (type.isPrimitive()) {
            field = new JField(jType, "_DEFAULT" + name.toUpperCase());
            JModifiers modifiers = field.getModifiers();
            modifiers.setFinal(true);
            modifiers.setStatic(true);
            
            if (_default != null)
                field.setInitString(_default);
            
            jClass.addField(field);
        }
        */
        
        
    } //-- createJavaField
    
    
    /**
     * Creates the access methods for this FieldInfo
     * @param jClass the JClass to add the methods to
    **/
    public void createAccessMethods(JClass jClass) {
        
        JMethod method    = null;
        JSourceCode jsc   = null;
        
        String mname = methodSuffix();
        
        XSType xsType = getSchemaType();
        JType jType  = xsType.getJType();
        
        
        boolean needs_has 
            = ((!xsType.isEnumerated()) && xsType.isPrimitive());
            
        //-- create get method
        method = new JMethod(jType, "get"+mname);
        jClass.addMethod(method);
        jsc = method.getSourceCode();
        jsc.add("return this.");
        jsc.append(this.name);
        jsc.append(";");
        
        //-- create set method
        method = new JMethod(null, "set"+mname);
        jClass.addMethod(method);
        
        String paramName = this.name;
		//-- Keith, not sure why this was needed? I've commented it out
		//-- since it causes compile failures when a field name is used 
		//-- that is a Java reserved word
        // if (paramName.indexOf('_') == 0)
        //   paramName = paramName.substring(1);
        
        method.addParameter(new JParameter(jType, paramName));
        jsc = method.getSourceCode();
        
        //-- bound properties
        if (_bound) {
            // save old value
            jsc.add("Object old");
            jsc.append(mname);
            jsc.append(" = ");
			//-- 'this.' ensures this refers to the class member not the parameter
            jsc.append(xsType.createToJavaObjectCode("this."+getName()));
            jsc.append(";");
        } 
        
        //-- set new value
        jsc.add("this.");
        jsc.append(getName());
        jsc.append(" = ");
        jsc.append(paramName);
        jsc.append(";");
        
        //-- bound properties
        if (_bound) {
            //notify listeners
            jsc.add("notifyPropertyChangeListeners(\"");
            jsc.append(getName());
            jsc.append("\", old");
            jsc.append(mname);
            jsc.append(", ");
			//-- 'this.' ensures this refers to the class member not the parameter
            jsc.append(xsType.createToJavaObjectCode("this."+getName()));
            jsc.append(");");
        } 
        
        //-- hasProperty
        if (needs_has) {
            jsc.add("this._has");
            jsc.append(getName());
            jsc.append(" = true;");
           
            //-- create hasMethod
            method = new JMethod(JType.Boolean, "has"+mname);
            jClass.addMethod(method);
            jsc = method.getSourceCode();
            jsc.add("return this._has");
            jsc.append(getName());
            jsc.append(";");
			
			//-- if optional then create delete method
			if (!isRequired()) {
                method = new JMethod(null, "delete"+mname);
                jClass.addMethod(method);
                jsc = method.getSourceCode();
                jsc.add("this._has");
                jsc.append(getName());
                jsc.append("= false;");			
            }
        }
		
        
        
    } //-- createAccessMethods
    
    
    public CodeHelper getCodeHelper() {
        return codeHelper;
    } //-- getCodeHelper
    
    
    /**
     * Returns the default value for this FieldInfo
     * @return the default value for this FieldInfo, or null if no
     * default value was set;
    **/
    public String getDefaultValue() {
        return _default;
    } //-- getDefaultValue
    
    /**
     * Returns the fixed production for this FieldInfo, or null
     * if no fixed value has been specified.
     * @return the fixed value for this FieldInfo
     * <BR />
     * NOTE: Fixed values are NOT the same as default values
    **/
    public String getFixedValue() {
        return _fixed;
    } //-- getFixedValue
    
    /**
     * Returns the name of the delete method for this FieldInfo.
     * @return the name of the delete method for this FieldInfo
    **/
    public String getDeleteMethodName() {
        return "delete" + methodSuffix();
    } //-- getDeleteMethodName
    
    /**
     * Returns the name of the has method for this FieldInfo
     * @return the name of the has method for this FieldInfo
    **/
    public String getHasMethodName() {
        return "has" + methodSuffix();
    } //-- getHasMethodName
    
    /**
     * Returns the name of the read method for this FieldInfo
     * @return the name of the read method for this FieldInfo
    **/
    public String getReadMethodName() {
        return "get" + methodSuffix();
    } //-- getReadMethodName
    
    
    /**
     * Returns the name of the write method for this FieldInfo
     * @return the name of the write method for this FieldInfo
    **/
    public String getWriteMethodName() {
        if (isMultivalued()) 
            return "add" + methodSuffix();
        else
            return "set" + methodSuffix();
    } //-- getWriteMethodName
    
    /**
     * Creates code for initialization of this Member
     * @param jsc the JSourceCode in which to add the source to
    **/
    public void generateInitializerCode(JSourceCode jsc) {
        //-- do nothing by default
    } //-- generateInitializerCode
    
    
    /**
     * Returns the comment associated with this Member
     * @return the comment associated with this Member, or null
     * if one has not been set.
    **/
    public String getComment() {
        return _comment;
    } //-- getComment
    
    /**
     * Returns the name of this FieldInfo
     * @return the name of this FieldInfo
    **/
    public String getName() {
        return this.name;
    } //-- getName
    
    /**
     * Returns true if this FieldInfo represents a bound property
     *
     * @return true if this FieldInfo represents a bound property
    **/
    public boolean isBound() {
        return _bound;
    } //-- isBound
    
    /**
     * Returns true if this FieldInfo is a transient member. Transient
     * members are members which should be ignored by the 
     * Marshalling framework
     * @return true if this FieldInfo is transient
    **/
    public boolean isTransient() {
        return (_transient || _final || _static);
    } //-- isTransient
    
    /**
     * Sets the CodeHelper to use when creating source code
     * @param codeHelper the CodeHelper to use when creating source code
     * @see FieldInfo
    **/
    public void setCodeHelper(CodeHelper codeHelper) {
        this.codeHelper = codeHelper;
    } //-- setCodeHelper
    
    /**
     * Sets the comment for this Member
     * @param comment the comment or description for this Member 
    **/
    public void setComment(String comment) {
        _comment = comment;
    } //-- setComment
    
    /**
     * Returns the ClassInfo to which this Member was declared,
     * for inheritance reasons
    **/
    public ClassInfo getDeclaringClassInfo() {
        return this.declaringClassInfo;
    } //-- getDeclaringClassInfo
    
    /**
     * Sets whether or not this FieldInfo represents a bound property
     *
     * @param bound the flag when true indicates that this FieldInfo
     * represents a bound property
    **/
    public void setBound(boolean bound) {
        _bound = bound;
    } //-- setBound
    
    public void setDeclaringClassInfo(ClassInfo declaringClassInfo) {
        this.declaringClassInfo = declaringClassInfo;
    } //-- setDeclaringClassInfo
    
    /**
     * Sets the default value for this FieldInfo
     * @param defaultValue the default value 
    **/
    public void setDefaultValue(String defaultValue) {
        this._default = defaultValue;
    } //-- setDefaultValue;
     
    /**
     * Sets the "final" status of this FieldInfo. Final
     * members are also transient.
     * @param isFinal the boolean indicating the final status,
     * if true this FieldInfo will be treated as final.
    **/
    public void setFinal(boolean isFinal) {
        this._final = isFinal;
    } //-- isFinal
    
    /**
     * Sets the fixed value in which instances of this field type must
     * lexically match
     * @param fixedValue the fixed production for this FieldInfo
     * <BR />
     * NOTE: This is not the same as default value!
    **/
    public void setFixedValue(String fixedValue) {
        this._fixed = fixedValue;
    } //-- setFixedValue
    
    /** 
     * Sets the "static" status of this FieldInfo. Static
     * members are also transient.
     * @param isStatic the boolean indicating the static status,
     * if true this FieldInfo will be treated as static
    **/
    public void setStatic(boolean isStatic) {
        this._static = isStatic;
    } //-- setStatic
    
    /** 
     * Sets the transient status of this FieldInfo. 
     * @param isTransient the boolean indicating the transient status,
     * if true this FieldInfo will be treated as transient
    **/
    public void setTransient(boolean isTransient) {
        this._transient = isTransient;
    } //-- setTransient
    
    /**
     * Returns the method suffix for creating method names.
    **/
    protected String methodSuffix() {
        if (name.startsWith("_"))
            return JavaXMLNaming.toJavaClassName(name.substring(1));
        else
            return JavaXMLNaming.toJavaClassName(name);
    }
    
} //-- FieldInfo


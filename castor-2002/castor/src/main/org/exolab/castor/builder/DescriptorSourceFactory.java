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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.javasource.*;
import org.exolab.castor.builder.types.*;
import org.exolab.castor.builder.util.DescriptorJClass;

/**
 * A factory for creating the source code of descriptor classes
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DescriptorSourceFactory {

        
    //-- org.exolab.castor.mapping
    private static JClass _ClassDescriptorClass 
        = new JClass("org.exolab.castor.mapping.ClassDescriptor");

    private static JClass _FieldDescriptorClass 
        = new JClass("org.exolab.castor.mapping.FieldDescriptor");
        
    //-- org.exolab.castor.xml
    private static JClass fdImplClass
        = new JClass("org.exolab.castor.xml.util.XMLFieldDescriptorImpl");
        
        
    private static JClass fdClass 
        = new JClass("org.exolab.castor.xml.XMLFieldDescriptor");
        
        
    private static JType fdArrayClass = fdClass.createArray();

    private static JClass gvrClass 
        = new JClass("org.exolab.castor.xml.GroupValidationRule");
        
    private static JClass vrClass 
        = new JClass("org.exolab.castor.xml.ValidationRule");
        

    /**
     * Creates the Source code of a MarshalInfo for a given XML Schema
     * element declaration
     * @param element the XML Schema element declaration
     * @return the JClass representing the MarshalInfo source code
    **/
    public static JClass createSource(ClassInfo classInfo) {
        
        
        JMethod     method          = null;
        JSourceCode jsc             = null;
        JSourceCode vcode           = null;
        JClass      jClass          = classInfo.getJClass();
        String      className       = jClass.getName();
        String      localClassName  = jClass.getLocalName();
        
        
        String variableName = "_"+className;
        
        
        JClass classDesc = new DescriptorJClass(className+"Descriptor", 
                                                jClass);
                                                
        //-- get handle to default constuctor
        
        JConstructor cons = classDesc.getConstructor(0);
        jsc = cons.getSourceCode();
        
        //-- Set namespace prefix
        String nsPrefix    = classInfo.getNamespacePrefix();
        if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
            jsc.add("nsPrefix = \"");
            jsc.append(nsPrefix);
            jsc.append("\";");
        }
        
        //-- Set namespace URI
        String nsURI       = classInfo.getNamespaceURI();
        if ((nsURI != null) && (nsURI.length() > 0)) {
            jsc.add("nsURI = \"");
            jsc.append(nsURI);
            jsc.append("\";");
        }
        
        //-- set XML Name
        String xmlName     = classInfo.getNodeName();
        if (xmlName != null) {
            jsc.add("xmlName = \"");
            jsc.append(xmlName);
            jsc.append("\";");
        }
        
        jsc.add("XMLFieldDescriptorImpl desc = null;");
        jsc.add("XMLFieldHandler handler = null;");
        
        //jsc.add("Class[] emptyClassArgs = new Class[0];");
        //jsc.add("Class[] classArgs = new Class[1];");
        
        //-- create validation method
        //method = new JMethod(vrClass.createArray(), "getValidationRules");
        //vcode = method.getSourceCode();
        //vcode.add("return rules;");
        //marshalInfo.addMethod(method);
        
        
        //-- create GroupValidationRule
        
        //jsc.add("gvr = new GroupValidationRule();");
        //jsc.add("BasicValidationRule bvr = null;");

        
        //-- handle text content
        if (classInfo.allowsTextContent()) {
            
            jsc.add("contentDesc = new XMLFieldDescriptorImpl(");
            jsc.append("String.class, \"_content\", \"PCDATA\", ");
            jsc.append("NodeType.Text);");
                    
            jsc.add("contentDesc.setHandler( new XMLFieldHandler() {");
            jsc.indent();
            
            //-- read method
            jsc.add("public Object getValue( Object object ) ");
            jsc.indent();
            jsc.add("throws IllegalStateException");
            jsc.unindent();
            jsc.add("{");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            jsc.add("return target.getContent();");
            jsc.unindent();
            jsc.add("}");
            
            //-- write method
            jsc.add("public void setValue( Object object, Object value) ");
            jsc.indent();
            jsc.add("throws IllegalStateException, IllegalArgumentException");
            jsc.unindent();
            jsc.add("{");
            jsc.indent();
            jsc.add("try {");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            jsc.add("target.setContent( (String) value);");
            jsc.unindent();
            jsc.add("}");
            jsc.add("catch (Exception ex) {");
            jsc.indent();
            jsc.add("throw new IllegalStateException(ex.toString());");
            jsc.unindent();
            jsc.add("}");
            jsc.unindent();
            jsc.add("}");
                
            //-- newInstance method
            jsc.add("public Object newInstance( Object parent ) {");
            jsc.indent();
            jsc.add("return new String();");
            jsc.unindent();
            jsc.add("}");
            
            
            jsc.unindent();
            jsc.add("} );");
        }
        
        FieldInfo[] atts = classInfo.getAttributeFields();
        
        //-- initialized rules
        //jsc.add("rules = new ValidationRule[");
        //jsc.append(Integer.toString(atts.length+1));
        //jsc.append("];");
        
        //--------------------------------/
        //- Create attribute descriptors -/
        //--------------------------------/
        
        jsc.add("//-- initialize attribute descriptors");
        jsc.add("");
        jsc.add("attributes = new XMLFieldDescriptorImpl[");
        jsc.append(Integer.toString(atts.length));
        jsc.append("];");
        
        for (int i = 0; i < atts.length; i++) {
            
            FieldInfo member = atts[i];
            
            //-- skip transient members
            if (member.isTransient()) continue;
            
            jsc.add("//-- ");
            jsc.append(member.getName());
                
            XSType xsType = member.getSchemaType();
            jsc.add("desc = new XMLFieldDescriptorImpl(");
            jsc.append(classType(xsType.getJType()));
            jsc.append(", \"");
            jsc.append(member.getName());
            jsc.append("\", \"");
            jsc.append(member.getNodeName());
            jsc.append("\", NodeType.Attribute);");
                
            
            switch (xsType.getType()) {
                
                case XSType.STRING:
                    jsc.add("desc.setImmutable(true);");
                    break;
                case XSType.CLASS:
                    //XSClass xsClass = (XSClass)xsType;
                    //if (xsClass.isEnumerated()) {
                    //    jsc.append("EnumMarshalDescriptor(");
                    //    jsc.append(xsClass.getName());
                    //    jsc.append(".class,\"");
                    //    break;
                    //}
                    break;
                case XSType.IDREF:
                    jsc.add("desc.setReference(true);");
                    break;
                case XSType.ID:
                    jsc.add("this.identity = desc;");
                    break;
                default:
                    break;
            }
            //-- handler access methods
                
            jsc.add("desc.setHandler( new XMLFieldHandler() {");
            jsc.indent();
            
            //-- read method
            jsc.add("public Object getValue( Object object ) ");
            jsc.indent();
            jsc.add("throws IllegalStateException");
            jsc.unindent();
            jsc.add("{");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            String value = "target."+member.getReadMethodName()+"()";
            jsc.add("return ");
            jsc.append(xsType.createToJavaObjectCode(value));
            jsc.append(";");
            jsc.unindent();
            jsc.add("}");
            
            //-- write method
            jsc.add("public void setValue( Object object, Object value) ");
            jsc.indent();
            jsc.add("throws IllegalStateException, IllegalArgumentException");
            jsc.unindent();
            jsc.add("{");
            jsc.indent();
            jsc.add("try {");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            jsc.add("target.");
            jsc.append(member.getWriteMethodName());
            jsc.append("( ");
            if (xsType.isPrimitive()) {
                jsc.append(xsType.createFromJavaObjectCode("value"));
            }
            else {
                jsc.append("(");
                jsc.append(xsType.getJType().toString());
                jsc.append(") value");
            }
            jsc.append(");");
            
            jsc.unindent();
            jsc.add("}");
            jsc.add("catch (Exception ex) {");
            jsc.indent();
            jsc.add("throw new IllegalStateException(ex.toString());");
            jsc.unindent();
            jsc.add("}");
            jsc.unindent();
            jsc.add("}");
                
            //-- newInstance method
            jsc.add("public Object newInstance( Object parent ) {");
            jsc.indent();
            jsc.add("return ");
            
            if (xsType.isPrimitive() || 
                xsType.getJType().isArray() ||
                (xsType.getType() == XSType.STRING))
            {
                jsc.append("null;");
            }                
            else {
                jsc.append("new ");
                jsc.append(xsType.getJType().getName());
                jsc.append("();");
            }
            jsc.unindent();
            jsc.add("}");
            
            
            jsc.unindent();
            jsc.add("} );");
            
            
            //-- namespace
            if (nsURI != null) {
                jsc.add("desc.setNameSpaceURI(\"");
                jsc.append(nsURI);
                jsc.append("\");");
            }
            
            if (member.isRequired()) {
                jsc.add("desc.setRequired(true);");
            }
            
            
            jsc.add("attributes[");
            jsc.append(Integer.toString(i));
            jsc.append("] = desc;");
            jsc.add("");
            
            //-- Add Validation Code
            //jsc.add("bvr = new BasicValidationRule(\"");
            //jsc.append(member.getNodeName());
            //jsc.append("\");");
            //jsc.add("bvr.setAsAttributeRule();");
            //validationCode(member, jsc);
            //jsc.add("rules[");
            //jsc.append(Integer.toString(i));
            //jsc.append("] = bvr;");
        }
        
        
        //------------------------------/
        //- Create element descriptors -/
        //------------------------------/
        
        //jsc.add("rules[");
        //jsc.append(Integer.toString(atts.length));
        //jsc.append("] = gvr;");
        
        FieldInfo[] elements = classInfo.getElementFields();
        
        jsc.add("//-- initialize element descriptors");
        jsc.add("");
        jsc.add("elements = new XMLFieldDescriptorImpl[");
        jsc.append(Integer.toString(elements.length));
        jsc.append("];");
        
        for (int i = 0; i < elements.length; i++) {
            
            FieldInfo member = elements[i];
            
            //-- skip transient members
            if (member.isTransient()) continue;
            
            XSType xsType = member.getSchemaType();
            
            
            jsc.add("//-- ");
            jsc.append(member.getName());

            boolean any = false;
            boolean isEnumerated = false;
            
            //-- a hack, I know, I will change later (kv)
            if (member.getName().equals("_anyList")) {
                any = true;
                jsc.add("desc = (new XMLFieldDescriptorImpl(");
                jsc.append("Object.class, \"");
                jsc.append(member.getName());
                jsc.append("\", (String)null, NodeType.Element) { ");
                jsc.indent();
                jsc.add("public boolean matches(String xmlName) {");
                jsc.add("    return true;");
                jsc.add("}");
                jsc.unindent();
                jsc.add("});");
            }
            else {
                
                if (xsType.getType() == XSType.LIST)
                    xsType = ((SGList)member).getContent().getSchemaType();
                    
                jsc.add("desc = new XMLFieldDescriptorImpl(");
                jsc.append(classType(xsType.getJType()));
                jsc.append(", \"");
                jsc.append(member.getName());
                jsc.append("\", \"");
                jsc.append(member.getNodeName());
                jsc.append("\", NodeType.Element);");
                
                switch (xsType.getType()) {
                    
                    case XSType.CLASS:
                        isEnumerated = ((XSClass)xsType).isEnumerated();
                        break;
                    case XSType.STRING:
                        jsc.add("desc.setImmutable(true);");
                        break;
                    default:
                        break;
                }
                
            }
            
            //-- handler access methods
                
            jsc.add("handler = (new XMLFieldHandler() {");
            jsc.indent();
            
            //-- read method
            jsc.add("public Object getValue( Object object ) ");
            jsc.indent();
            jsc.add("throws IllegalStateException");
            jsc.unindent();
            jsc.add("{");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            jsc.add("return ");
            String value = "target."+member.getReadMethodName()+"()";
            if (member.isMultivalued()) jsc.append(value);
            else jsc.append(xsType.createToJavaObjectCode(value));
            jsc.append(";");
            jsc.unindent();
            jsc.add("}");
            
            //-- write method
            jsc.add("public void setValue( Object object, Object value) ");
            jsc.indent();
            jsc.add("throws IllegalStateException, IllegalArgumentException");
            jsc.unindent();
            jsc.add("{");
            jsc.indent();
            jsc.add("try {");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            jsc.add("target.");
            jsc.append(member.getWriteMethodName());
            jsc.append("( ");
            if (xsType.isPrimitive()) {
                jsc.append(xsType.createFromJavaObjectCode("value"));
            }
            else if (any) {
                jsc.append(" value ");
            }
            else {
                jsc.append("(");
                jsc.append(xsType.getJType().toString());
                jsc.append(") value");
            }
            jsc.append(");");
            
            jsc.unindent();
            jsc.add("}");
            jsc.add("catch (Exception ex) {");
            jsc.indent();
            jsc.add("throw new IllegalStateException(ex.toString());");
            jsc.unindent();
            jsc.add("}");
            jsc.unindent();
            jsc.add("}");
                
            //-- newInstance method
            jsc.add("public Object newInstance( Object parent ) {");
            jsc.indent();
            jsc.add("return ");
            
                
            if (any || isEnumerated ||
                xsType.isPrimitive() || 
                xsType.getJType().isArray() ||
                (xsType.getType() == XSType.STRING))
            {
                jsc.append("null;");
            }                
            else {
                jsc.append("new ");
                jsc.append(xsType.getJType().getName());
                jsc.append("();");
            }
            jsc.unindent();
            jsc.add("}");
            
            
            jsc.unindent();
            jsc.add("} );");
            
            if (isEnumerated) {
                jsc.add("desc.setHandler( new EnumFieldHandler(");
                jsc.append(classType(xsType.getJType()));
                jsc.append(", handler));");
                jsc.add("desc.setImmutable(true);");
            }
            else jsc.add("desc.setHandler(handler);");
            
            //-- namespace
            if (nsURI != null) {
                jsc.add("desc.setNameSpaceURI(\"");
                jsc.append(nsURI);
                jsc.append("\");");
            }

            if (member.isRequired()) {
                jsc.add("desc.setRequired(true);");
            }
            
            //-- mark as multi or single valued
            jsc.add("desc.setMultivalued("+member.isMultivalued());
            jsc.append(");");
            
            jsc.add("elements[");
            jsc.append(Integer.toString(i));
            jsc.append("] = desc;");
            jsc.add("");
            
            //-- Add Validation Code
            //jsc.add("bvr = new BasicValidationRule(\"");
            //jsc.append(member.getNodeName());
            //jsc.append("\");");
            //validationCode(member, jsc);
            //jsc.add("gvr.addValidationRule(bvr);");
            
        }
        
        return classDesc;
        
    } //-- createSource
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Returns the Class type (as a String) for the given XSType
    **/
    private static String classType(JType jType) {
        if (jType.isPrimitive()) {
            if (jType == JType.Int)
                return "java.lang.Integer.TYPE";
            else if (jType == JType.Double)
                return "java.lang.Double.TYPE";
        }
        return jType.toString() + ".class";
    } //-- classType
    
    private static void validationCode(FieldInfo member, JSourceCode jsc) {
        
        //-- a hack, I know, I will change later
        if (member.getName().equals("_anyList")) return;
        
        XSType xsType = member.getSchemaType();
        
        //-- create local copy of field
        JMember jMember = member.createMember();
        
        
        if (xsType.getType() != XSType.LIST) {
            if (member.isRequired()) {
                jsc.add("bvr.setMinOccurs(1);");
            }
            jsc.add("bvr.setMaxOccurs(1);");
        }
        
        switch (xsType.getType()) {
            
            case XSType.NEGATIVE_INTEGER:
            case XSType.POSITIVE_INTEGER: 
            case XSType.INTEGER:
                jsc.add("{ //-- local scope");
                jsc.indent();
                jsc.add("IntegerValidator iv = new IntegerValidator();");
                XSInteger xsInteger = (XSInteger)xsType;
                if (xsInteger.hasMinimum()) {
                    Integer min = xsInteger.getMinExclusive();
                    if (min != null)
                        jsc.add("iv.setMinExclusive(");
                    else {
                        min = xsInteger.getMinInclusive();
                        jsc.add("iv.setMinInclusive(");
                    }
                    jsc.append(min.toString());
                    jsc.append(");");
                }
                if (xsInteger.hasMaximum()) {
                    Integer max = xsInteger.getMaxExclusive();
                    if (max != null)
                        jsc.add("iv.setMaxExclusive(");
                    else {
                        max = xsInteger.getMaxInclusive();
                        jsc.add("iv.setMaxInclusive(");
                    }
                    jsc.append(max.toString());
                    jsc.append(");");
                }
                
                jsc.add("bvr.setTypeValidator(iv);");
                jsc.unindent();
                jsc.add("}");
                break;
            case XSType.STRING:
                jsc.add("bvr.setTypeValidator(new StringValidator());");
                break;
            case XSType.NCNAME:
                jsc.add("bvr.setTypeValidator(new NameValidator());");
                break;
            case XSType.NMTOKEN:
                jsc.add("bvr.setTypeValidator(new NameValidator(");
                jsc.append("NameValidator.NMTOKEN));");
                break;
            case XSType.LIST:
                XSList xsList = (XSList)xsType;
                SGList sgList = (SGList)member;
                FieldInfo content = sgList.getContent();
                
                jsc.add("bvr.setMinOccurs(");
                jsc.append(Integer.toString(xsList.getMinimumSize()));
                jsc.append(");");
                
                if (xsList.getMaximumSize() > 0) {
                    jsc.add("bvr.setMaxOccurs(");
                    jsc.append(Integer.toString(xsList.getMaximumSize()));
                    jsc.append(");");
                }
                break;
            default:
                break;
        }
    } //-- validationCode
    
            
} //-- MarshalInfoSourceFactory
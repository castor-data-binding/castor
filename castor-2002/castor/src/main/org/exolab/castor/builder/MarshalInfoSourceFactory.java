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


/**
 * A factory for creating The source code of MarshalInfo classes
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class MarshalInfoSourceFactory {

    //-- needed types
    private static JClass methodClass 
        = new JClass("java.lang.reflect.Method");
        
        
    private static JClass simpleMDClass
        = new JClass("org.exolab.castor.xml.SimpleMarshalDescriptor");
        
        
    private static JClass mdClass 
        = new JClass("org.exolab.castor.xml.MarshalDescriptor");
        
        
    private static JType mdArrayClass = mdClass.createArray();

    /**
     * The resolver class type
    **/
    private static JClass iResolver 
        = new JClass("org.exolab.castor.xml.Resolver");
        
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
        
        
        JMethod     method = null;
        JSourceCode vcode  = null;
        
        String className   = classInfo.getClassName();
        String nsPrefix    = classInfo.getNameSpacePrefix();
        String nsURI       = classInfo.getNameSpaceURI();
        
        
        String variableName = "_"+className;
        
        
        //-- initial set up
        String packageName = null;
        int idx = -1;
        if ((idx = className.lastIndexOf('.')) < 0)
            packageName = classInfo.getPackageName();
            
        if (packageName == null) packageName = "";
        else packageName += ".";
        
        JClass marshalInfo = new JClass(packageName+className+"MarshalInfo");
        marshalInfo.addInterface("org.exolab.castor.xml.MarshalInfo");
        marshalInfo.addImport("org.exolab.castor.xml.*");
        marshalInfo.addImport("java.lang.reflect.Method");
        
        marshalInfo.addMember(new JMember(mdArrayClass,  "elements") );
        marshalInfo.addMember(new JMember(mdArrayClass,  "attributes") );
        marshalInfo.addMember(new JMember(simpleMDClass, "contentDesc"));
        marshalInfo.addMember(new JMember(gvrClass, "gvr"));
        marshalInfo.addMember(new JMember(vrClass.createArray(), "rules"));
        
        //marshalInfo.addMember(new JMember(iResolver, "_resolver"));


            
        //-- create constructor
        JConstructor cons = marshalInfo.createConstructor();
        marshalInfo.addConstructor( cons );
        JSourceCode jsc = cons.getSourceCode();
        
        jsc.add("SimpleMarshalDescriptor desc = null;");
        
        jsc.add("Class[] emptyClassArgs = new Class[0];");
        jsc.add("Class[] classArgs = new Class[1];");
        
        //-- create validation method
        method = new JMethod(vrClass.createArray(), "getValidationRules");
        vcode = method.getSourceCode();
        vcode.add("return rules;");
        marshalInfo.addMethod(method);
        
        
        //-- create GroupValidationRule
        
        jsc.add("gvr = new GroupValidationRule();");
        jsc.add("BasicValidationRule bvr = null;");

        
        //-- handle text content
        if (classInfo.allowsTextContent()) {
            jsc.add("contentDesc = new SimpleMarshalDescriptor(");
            jsc.append("String.class, \"vContent\", \"PCDATA\");");
                    
            jsc.add("try {");
            jsc.indent();
            //-- read method
            jsc.add("contentDesc.setReadMethod(");
            jsc.append(className);
            jsc.append(".class.getMethod(\"");
            jsc.append("getContent");
            jsc.append("\", emptyClassArgs));");
                    
            //-- write method
            jsc.add("classArgs[0] = String.class;");
            jsc.add("contentDesc.setWriteMethod(");
            jsc.append(className);
            jsc.append(".class.getMethod(\"");
            jsc.append("setContent");
            jsc.append("\", classArgs));");
                    
            jsc.unindent();
            jsc.add("}");
            jsc.add("catch(java.lang.NoSuchMethodException nsme) {};");
        }
        
        
        
        SGMember[] atts = classInfo.getAttributeMembers();
        
        //-- initialized rules
        jsc.add("rules = new ValidationRule[");
        jsc.append(Integer.toString(atts.length+1));
        jsc.append("];");
        
        //-- create attribute descriptors
        jsc.add("//-- initialize attributes");
        jsc.add("");
        jsc.add("attributes = new MarshalDescriptor[");
        jsc.append(Integer.toString(atts.length));
        jsc.append("];");
        
        for (int i = 0; i < atts.length; i++) {
            
            SGMember member = atts[i];
            
            jsc.add("//-- ");
            jsc.append(member.getName());
                
            XSType xsType = member.getXSType();
            jsc.add("desc = new ");
            
            switch (xsType.getType()) {
                
                case XSType.TIME_INSTANT:
                    jsc.append("DateMarshalDescriptor(\"");
                    break;
                default:
                    jsc.append("SimpleMarshalDescriptor(");
                    jsc.append(classType(xsType));
                    jsc.append(", \"");
                    break;
            }
            
            jsc.append(member.getName());
            jsc.append("\", \"");
            jsc.append(member.getXMLName());
            jsc.append("\");");
            jsc.add("desc.setDescriptorType(DescriptorType.attribute);");
                
            //-- access methods
                
            jsc.add("try {");
            jsc.indent();
            //-- read method
            jsc.add("desc.setReadMethod(");
            jsc.append(className);
            jsc.append(".class.getMethod(\"");
            jsc.append(member.getReadMethodName());
            jsc.append("\", emptyClassArgs));");
                
            //-- write method
            jsc.add("classArgs[0] = ");
            jsc.append(member.getXSType().getJType().getName());
            jsc.append(".class;");
                
            jsc.add("desc.setWriteMethod(");
            jsc.append(className);
            jsc.append(".class.getMethod(\"");
            jsc.append(member.getWriteMethodName());
            jsc.append("\", classArgs));");
                
            jsc.unindent();
            jsc.add("}");
            jsc.add("catch(java.lang.NoSuchMethodException nsme) {};");
            jsc.add("");
            
            if (member.getRequired()) {
                jsc.add("desc.setRequired(true);");
            }
            
            jsc.add("attributes[");
            jsc.append(Integer.toString(i));
            jsc.append("] = desc;");
            jsc.add("");
            
            //-- Add Validation Code
            jsc.add("bvr = new BasicValidationRule(\"");
            jsc.append(member.getXMLName());
            jsc.append("\");");
            jsc.add("bvr.setAsAttributeRule();");
            validationCode(member, jsc);
            jsc.add("rules[");
            jsc.append(Integer.toString(i));
            jsc.append("] = bvr;");
        }
        
        
        
        //-- create element descriptors
        
        jsc.add("rules[");
        jsc.append(Integer.toString(atts.length));
        jsc.append("] = gvr;");
        
        SGMember[] elements = classInfo.getElementMembers();
        
        jsc.add("//-- initialize elements");
        jsc.add("");
        jsc.add("elements = new MarshalDescriptor[");
        jsc.append(Integer.toString(elements.length));
        jsc.append("];");
        
        for (int i = 0; i < elements.length; i++) {
            
            SGMember member = elements[i];
            
            XSType xsType = member.getXSType();
            if (xsType.getType() == XSType.LIST)
                xsType = ((SGList)member).getContent().getXSType();
            
            jsc.add("//-- ");
            jsc.append(member.getName());

            
            //-- a hack, I know, I will change later (kv)
            if (member.getName().equals("_anyList")) {
                jsc.add("desc = (new SimpleMarshalDescriptor(");
                jsc.append(classType(xsType));
                jsc.append(", \"");
                jsc.append(member.getName());
                jsc.append("\", \"");
                jsc.append(member.getXMLName());
                jsc.append("\") {");
                jsc.indent();
                jsc.add("public boolean matches(String xmlName) {");
                jsc.add("    return true;");
                jsc.add("}");
                jsc.unindent();
                jsc.add("});");
            }
            else {
                
                jsc.add("desc = new ");
                switch (xsType.getType()) {
                    
                    case XSType.TIME_INSTANT:
                        jsc.append("DateMarshalDescriptor(\"");
                        break;
                    default:
                        jsc.append("SimpleMarshalDescriptor(");
                        jsc.append(classType(xsType));
                        jsc.append(", \"");
                        break;
                }
                jsc.append(member.getName());
                jsc.append("\", \"");
                jsc.append(member.getXMLName());
                jsc.append("\");");
            }
            
            jsc.add("desc.setDescriptorType(DescriptorType.element);");
                        
            //-- access methods
            
            jsc.add("try {");
            jsc.indent();
            
            //-- read method
            jsc.add("desc.setReadMethod(");
            jsc.append(className);
            jsc.append(".class.getMethod(\"");
            jsc.append(member.getReadMethodName());
            jsc.append("\", emptyClassArgs));");
            
            //-- write method
            jsc.add("classArgs[0] = ");
            
            JType jType = xsType.getJType();
                
            jsc.append(jType.getName());
            jsc.append(".class;");
            
            jsc.add("desc.setWriteMethod(");
            jsc.append(className);
            jsc.append(".class.getMethod(\"");
            jsc.append(member.getWriteMethodName());
            jsc.append("\", classArgs));");
            
            jsc.unindent();
            jsc.add("}");
            jsc.add("catch(java.lang.NoSuchMethodException nsme) {};");
            jsc.add("");
            if (member.getRequired()) {
                jsc.add("desc.setRequired(true);");
            }
            jsc.add("elements[");
            jsc.append(Integer.toString(i));
            jsc.append("] = desc;");
            jsc.add("");
            
            //-- Add Validation Code
            jsc.add("bvr = new BasicValidationRule(\"");
            jsc.append(member.getXMLName());
            jsc.append("\");");
            validationCode(member, jsc);
            jsc.add("gvr.addValidationRule(bvr);");
            
        }
        
        //-- create getAttributeDescriptors method
        method = new JMethod(mdArrayClass, "getAttributeDescriptors");
        jsc = method.getSourceCode();
        jsc.add("return attributes;");
        marshalInfo.addMethod(method);
        
        //-- create getClassType method
        method = new JMethod(SGTypes.Class, "getClassType");
        jsc = method.getSourceCode();
        jsc.add("return ");
        
        XSType xsType = classInfo.getDataType();
        
        jsc.append(classType(xsType));
        jsc.append(";");
        
        marshalInfo.addMethod(method);
        
        //-- create getElementDescriptors method
        method = new JMethod(mdArrayClass, "getElementDescriptors");
        jsc = method.getSourceCode();
        jsc.add("return elements;");
        marshalInfo.addMethod(method);
        
        

        //-- create getContentDescriptor method
        method = new JMethod(mdClass, "getContentDescriptor");
        jsc = method.getSourceCode();
        jsc.add("return contentDesc;");
        marshalInfo.addMethod(method);
        
        //-- create getNameSpacePrefix method
        method = new JMethod(SGTypes.String, "getNameSpacePrefix");
        jsc = method.getSourceCode();
        if (nsPrefix != null) {
            jsc.add("return \"");
            jsc.append(nsPrefix);
            jsc.append("\";");
        }
        else jsc.add("return null;");
        marshalInfo.addMethod(method);
            
        //-- create getNameSpacePrefix method
        method = new JMethod(SGTypes.String, "getNameSpaceURI");
        jsc = method.getSourceCode();
        if (nsURI != null) {
            jsc.add("return \"");
            jsc.append(nsURI);
            jsc.append("\";");
        }
        else jsc.add("return null;");
        marshalInfo.addMethod(method);
        
        
        return marshalInfo;
        
    } //-- createSource
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Returns the Class type (as a String) for the given XSType
    **/
    private static String classType(XSType xsType) {
        switch (xsType.getType()) {
            case XSType.INTEGER:
                return "java.lang.Integer.TYPE";
            case XSType.REAL:
                return "java.lang.Double.TYPE";
            case XSType.STRING:
            case XSType.TIME_INSTANT:
            case XSType.CLASS:
            default:
                return xsType.getJType().getName() + ".class";
        }
    } //-- classType
    
    private static void validationCode(SGMember member, JSourceCode jsc) {
        
        //-- a hack, I know, I will change later
        if (member.getName().equals("_anyList")) return;
        
        XSType xsType = member.getXSType();
        
        //-- create local copy of field
        JMember jMember = member.createMember();
        
        
        if (xsType.getType() != XSType.LIST) {
            if (member.getRequired()) {
                jsc.add("bvr.setMinOccurs(1);");
            }
            jsc.add("bvr.setMaxOccurs(1);");
        }
        
        switch (xsType.getType()) {
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
                SGMember content = sgList.getContent();
                
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
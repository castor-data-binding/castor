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

import org.exolab.castor.builder.types.*;
import org.exolab.javasource.*;
import org.exolab.castor.xml.JavaXMLNaming;

/**
 * This class takes an ElementDecl and creates the Java Source 
 * for for it
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SourceFactory  {
    
    
    private static final String NULL_RESOLVER_ERR 
        = "A null Resolver was passed as an argument to the SGClass constructor.";
        
    private static final String REFERABLE_INTERFACE
        = "org.exolab.castor.xml.Referable";

    private static final String RESOLVER_INTERFACE 
        = "org.exolab.castor.xml.Resolver";
        
    private static final String RESOLVER_CLASS 
        = "org.exolab.castor.xml.IdResolver";
    
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Creates the source code for the given ClassInfo.
     * @param classInfo the ClassInfo object to create Source code for
     * @return the JClass representing the java source code for the
     * ClassInfo argument
    **/
    public static JClass createClassSource(ClassInfo classInfo) {
        
        JMethod      jMethod     = null;
        JSourceCode  jsc         = null;
        JClass       cMain       = null;
        JConstructor constructor = null;
        
        String packageName = classInfo.getPackageName();
        String cName = classInfo.getClassName();
        if (packageName != null)
            cName = packageName+"."+cName;
        
        cMain = new JClass(cName);
        cMain.addInterface("java.io.Serializable");
        
        cMain.addConstructor(constructor = cMain.createConstructor());
        
        //-- add imports
        cMain.addImport("org.exolab.castor.xml.*");
        cMain.addImport("java.io.Writer");
        cMain.addImport("java.io.Reader");
        cMain.addImport("java.io.Serializable");
        
        //-- create default methods
        //-- #validate
        jMethod = new JMethod(null, "validate");
        jMethod.addException(SGTypes.ValidationException);
        //mValidate.getModifiers().makeProtected();
        cMain.addMethod(jMethod);
        jsc = jMethod.getSourceCode();
        jsc.add("org.exolab.castor.xml.Validator.validate(this, null);");
        
        //-- #isValid
        jMethod  = new JMethod(JType.Boolean, "isValid");
        jsc = jMethod.getSourceCode();
        jsc.add("try {");
        jsc.indent();
        jsc.add("validate();");
        jsc.unindent();
        jsc.add("}");
        jsc.add("catch (org.exolab.castor.xml.ValidationException vex) {");
        jsc.indent();
        jsc.add("return false;");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return true;");
        cMain.addMethod(jMethod);        
        
        
        
        //-- handle attributes
        SGMember[] atts = classInfo.getAttributeMembers();
        
        //JSourceCode vcode = mValidate.getSourceCode();
        for (int i = 0; i < atts.length; i++) {
            SGMember member = atts[i];
            cMain.addMember(member.createMember());
            cMain.addMethods(member.createAccessMethods());
            //-- Add initialization code
            member.generateInitializerCode(constructor.getSourceCode());
        }
        
        //-- handle elements
        SGMember[] elements = classInfo.getElementMembers();
        
        for (int i = 0; i < elements.length; i++) {
            SGMember member = elements[i];
            cMain.addMember(member.createMember());
            cMain.addMethods(member.createAccessMethods());
            //-- Add initialization code
            member.generateInitializerCode(constructor.getSourceCode());
        }
        
        //-- handle text content
        if (classInfo.allowsTextContent()) {
            JMember text = new JMember(SGTypes.String, "vContent");
            text.setComment("internal character storage");
            cMain.addMember(text);
            cMain.addMethod(SGHelper.createGetMethod(text));
            cMain.addMethod(SGHelper.createSetMethod(text));
            constructor.getSourceCode().add("vContent = new String();");
        }
        
        //-- #marshal()
        createMarshalMethods(cMain);
        //-- #unmarshal()
        createUnmarshalMethods(cMain);
        
        return cMain;
    } //-- createClassSource
    
    //-------------------/        
    //- Private Methods -/        
    //-------------------/        
        
    private static void createMarshalMethods(JClass parent) {
        
        //-- create main marshal method
        JMethod jMethod = new JMethod(null,"marshal");
        jMethod.addException(SGTypes.IOException);
        jMethod.addException(SGTypes.SAXException);
        jMethod.addParameter(new JParameter(SGTypes.Writer, "out"));
        parent.addMethod(jMethod);
        JSourceCode jsc = jMethod.getSourceCode();
        jsc.add("//-- we must have a valid element before marshalling");
        jsc.add("//validate(false);");
        jsc.add("");
        jsc.add("Marshaller.marshal(this, out);");
        
        
        //-- create helper marshal method
        //-- start helper marshal method, this method will
        //-- be built up as we process the given ElementDecl
        jMethod = new JMethod(null, "marshal");
        JClass jc = new JClass("org.xml.sax.DocumentHandler");
        jMethod.addException(SGTypes.IOException);
        jMethod.addException(SGTypes.SAXException);
        jMethod.addParameter(new JParameter(jc, "handler"));
        parent.addMethod(jMethod);
        jsc = jMethod.getSourceCode();
        jsc.add("//-- we must have a valid element before marshalling");
        jsc.add("//validate(false);");
        jsc.add("");
        jsc.add("Marshaller.marshal(this, handler);");
        
    } //-- createMarshalMethods
    
    private static void createUnmarshalMethods(JClass parent) {
        
        //-- create main marshal method
        JMethod jMethod = new JMethod(parent,"unmarshal");
        jMethod.getModifiers().setStatic(true);
        jMethod.addException(SGTypes.IOException);
        jMethod.addParameter(new JParameter(SGTypes.Reader, "reader"));
        parent.addMethod(jMethod);
        JSourceCode jsc = jMethod.getSourceCode();
        jsc.add("return (");
        jsc.append(parent.getName());
        jsc.append(") Unmarshaller.unmarshal(");
        jsc.append(parent.getName());
        jsc.append(".class, reader);");
        
    } //-- createUnmarshalMethods
    
    /**
     * Creates the useResolver method for an SGClass
    **/
    private static JMethod createUseResolverMethod() {
        
        JMethod jMethod = new JMethod(null, "useResolver");
        
        //String comment = "Sets the IdResolver to use when resolving ";
        //comment += " Id references or when registering Ids.";
        //jMethod.setComment(comment);
        
        JClass jClass = new JClass(RESOLVER_INTERFACE);
        JParameter jParam = new JParameter(jClass, "resolver");
        jMethod.addParameter(jParam);
        
        JSourceCode jsc = jMethod.getSourceCode();
        jsc.add("idResolver = resolver;");
        return jMethod;
        
    } //-- createUseResolverMethod
    
    
    
    
} //-- SourceFactory

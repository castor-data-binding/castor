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
import org.exolab.castor.builder.util.*;
import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.types.BuiltInType;
import org.exolab.javasource.*;


import java.util.Enumeration;

/**
 * This class takes an ElementDecl and creates the Java Source 
 * for for it
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SourceFactory  {
    
        
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Creates a new ClassInfo for the given XML Schema element declaration
     * @param element the XML Schema element declaration to create the 
     * ClassInfo for
     * @param resolver the ClassInfoResolver for resolving "derived" types.
     * @param packageName the package to use when generating source
     * from this ClassInfo
    **/
    public static JClass createSourceCode
        (ElementDecl element, ClassInfoResolver resolver, String packageName) 
    {
        
        FactoryState state = null;
        
        String elementName = element.getName();
        String className = JavaXMLNaming.toJavaClassName(elementName);
        
        className = resolveClassName(className, packageName);
        
        state = new FactoryState(className, resolver);
        
        ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;
        
        initialize(jClass);
        
        //-- name information
        classInfo.setNodeName(element.getName());
        
        //-- namespace information
        Schema  schema = element.getSchema();        
        classInfo.setNamespaceURI(schema.getTargetNamespace());
        
        //-- process annotation
        String comment  = processAnnotations(element);
        if (comment != null) 
            jClass.getJDocComment().setComment(comment);
            
        
        Archetype archetype = element.getArchetype();
        
        boolean derived = false;
        
        if (archetype != null) {
            processArchetype(archetype, state);
        }
        else {
            Datatype datatype = element.getDatatype();
            if (datatype != null)
                classInfo.setSchemaType(TypeConversion.convertType(datatype));
            else
                classInfo.setSchemaType(new XSClass(state.jClass));
        }
        
        //-- add imports required by the marshal methods
        jClass.addImport("java.io.Writer");
        jClass.addImport("java.io.Reader");
        
        //-- #validate()
        createValidateMethods(jClass);
        //-- #marshal()
        createMarshalMethods(jClass);
        //-- #unmarshal()
        createUnmarshalMethods(jClass);
        
        if (resolver != null)
            resolver.bindReference(jClass, classInfo);
        
        return jClass;
    } //-- createSourceCode
    
    /**
     * Creates a new ClassInfo for the given XML Schema type declaration.
     * The type declaration must be a top-level declaration.
     * @param type the XML Schema type declaration to create the 
     * ClassInfo for
     * @param resolver the ClassInfoResolver for resolving "derived" types.
     * @param packageName the package to which generated classes should
     * belong
    **/
    public static JClass createSourceCode
        (Archetype type, ClassInfoResolver resolver, String packageName) 
    {
        if (type == null)
            throw new IllegalArgumentException("null archetype");
            
        if (!type.isTopLevel())
            throw new IllegalArgumentException("Archetype is not top-level.");
        
        String className = JavaXMLNaming.toJavaClassName(type.getName());
        className = resolveClassName(className, packageName);
        
        FactoryState state = new FactoryState(className, resolver);
        
        ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;
        
        initialize(jClass);
        
        //-- make class abstract
        jClass.getModifiers().setAbstract(true);
        
        
        //-- name information
        classInfo.setNodeName(type.getName());
        
        //-- namespace information
        Schema  schema = type.getSchema();        
        classInfo.setNamespaceURI(schema.getTargetNamespace());
        
        //-- process annotation
        String comment  = processAnnotations(type);
        if (comment != null) 
            jClass.getJDocComment().setComment(comment);
            
        
        processArchetype(type, state);
        
        //-- add imports required by the marshal methods
        jClass.addImport("java.io.Writer");
        jClass.addImport("java.io.Reader");
        
        //-- #validate()
        createValidateMethods(jClass);
        //-- #marshal()
        //createMarshalMethods(jClass);
        //-- #unmarshal()
        //createUnmarshalMethods(jClass);
        
        if (resolver != null) {
            resolver.bindReference(jClass, classInfo);
            resolver.bindReference(type, classInfo);
        }
        
        return jClass;
        
    } //-- ClassInfo
    
    /**
     * Creates the Java source code to support the given Datatype
     * @param datatype the Datatype to create the Java source for
     * @return the JClass representation of the given Datatype
    **/
    public static JClass createSourceCode
        (Datatype datatype, ClassInfoResolver resolver, String packageName) 
    {
        
        if (datatype instanceof BuiltInType) {
            String err = "You cannot construct a ClassInfo for a " +
                "built-in datatype.";
            throw new IllegalArgumentException(err);
        }
        
        
        //-- class name information
        String className = JavaXMLNaming.toJavaClassName(datatype.getName());
        className = resolveClassName(className, packageName);
        
        FactoryState state = new FactoryState(className, resolver);
        
        ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;
        
        initialize(jClass);
        
        //-- XML information
        Schema  schema = datatype.getSchema();        
        classInfo.setNamespaceURI(schema.getTargetNamespace());
        classInfo.setNodeName(datatype.getName());
        
        
        //-- process annotation
        String comment  = processAnnotations(datatype);
        if (comment != null) 
            jClass.getJDocComment().setComment(comment);
            
        classInfo.setSchemaType(new XSClass(jClass, datatype.getName()));
        
        //-- handle enumerated types
        
        if (datatype.hasFacet("enumeration")) {
            processEnumeration(datatype, state);
        }
        
        if (resolver != null) {
            resolver.bindReference(jClass, classInfo);
            resolver.bindReference(datatype, classInfo);
        }
        
        return jClass;
        
    } //-- createClassSource
    
    
    /**
     *
    **
    public static JClass createSourceCode(ClassDescriptor descriptor) 
    {
        
        //-- handle null arguments
        if (descriptor == null) {
            String err = "ClassDescriptor passed as an argument to "+
                " SourceFactory#createSourceCode cannot be null.";
            throw new IllegalArgumentException(err);
        }
        
        ClassDescriptor classDesc = descriptor;
        /*
        if (descriptor instanceof XMLClassDescriptor)
            classDesc = (XMLClassDescriptor) descriptor;
        else {
            try {
                classDesc = new XMLClassDescriptorAdapter(descriptor, null);
            }
            catch(org.exolab.castor.mapping.MappingException mx) {
                throw new IllegalStateException(mx.toString());
            }
        }
        *  
         
        Class type = classDesc.getJavaClass();
        JClass jClass = new JClass(type.getName());
        
        //-- Loop through fields and add members
        JMember field = null;
        FieldDescriptor[] fields = classDesc.getFields();
        for (int i = 0; i < fields.size(); i++) {
        }
        
        return jClass;
        
    } //-- createSourceCode
    
    //-------------------/        
    //- Private Methods -/        
    //-------------------/        
    
    /**
     * Initializes the given JClass
    **/
    private static void initialize(JClass jClass) {
        
        
        jClass.addInterface("java.io.Serializable");
        
        //-- add default constructor
        JConstructor con = jClass.createConstructor();
        jClass.addConstructor(con);
        con.getSourceCode().add("super();");
        
        //-- add default import list
        jClass.addImport("org.exolab.castor.xml.*");
        jClass.addImport("java.io.Serializable");
        
    } //-- initialize
    
    /**
     * Creates the #marshal methods for the given JClass
     * @param parent the JClass to create the #marshal methods for
    **/
    private static void createMarshalMethods(JClass parent) {
        
        //-- create main marshal method
        JMethod jMethod = new JMethod(null,"marshal");
        jMethod.addException(SGTypes.MarshalException);
        jMethod.addException(SGTypes.ValidationException);
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
        jMethod.addException(SGTypes.MarshalException);
        jMethod.addException(SGTypes.ValidationException);
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
        jMethod.addException(SGTypes.MarshalException);
        jMethod.addException(SGTypes.ValidationException);
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
     * Creates the Validate methods for the given JClass
     * @param jClass the JClass to create the Validate methods for
    **/
    private static void createValidateMethods(JClass jClass) {
        
        JMethod     jMethod = null;
        JSourceCode jsc     = null;
        
        //-- #validate
        jMethod = new JMethod(null, "validate");
        jMethod.addException(SGTypes.ValidationException);
        
        jClass.addMethod(jMethod);
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
        jClass.addMethod(jMethod);
        
    } //-- createValidateMethods
    
    //-------------------/        
    //- Private Methods -/        
    //-------------------/        
     
    /**
     * Resolves the className out of the given name and the packageName
    **/
    private static String resolveClassName(String name, String packageName) {
        if ((packageName != null) && (packageName.length() > 0)) {
            return packageName+"."+name;
        }
        return name;
    } //-- resolveClassName
    
    /**
     * @param archetype the Archetype for this ClassInfo
     * @param resolver the ClassInfoResolver for resolving "derived" types.
    **/    
    private static void processArchetype
        (Archetype archetype, FactoryState state) 
    {
        
        String typeName = archetype.getName();
        
        ClassInfo classInfo = state.classInfo;
        classInfo.setSchemaType(new XSClass(state.jClass, typeName));
        
        Schema schema = archetype.getSchema();
        classInfo.setNamespaceURI(schema.getTargetNamespace());
        
        
        
        //- Handle derived types
        if (archetype.getSource() != null) {
        
            String sourceName = archetype.getSource();
            Archetype source = schema.getArchetype(sourceName);
            if (source != null) {
                
                String className = null;
                
                ClassInfo cInfo = state.resolve(source);
                if (cInfo == null) {
                    
                    String packageName = state.jClass.getPackageName();
                    JClass jClass = createSourceCode(source, 
                                                     state,
                                                     packageName);
                    cInfo = state.resolve(source);
                    className = jClass.getName();
                }
                else className = cInfo.getJClass().getName();
                
                    
                state.jClass.setSuperClass(className);
                
                //-- copy members from super class
                classInfo.addFieldInfo(cInfo.getAttributeFields());
                classInfo.addFieldInfo(cInfo.getElementFields());
                classInfo.addFieldInfo(cInfo.getTextField());
            }
            else {
                //-- will this ever be null, if we have a valid Schema?
                //-- ignore for now...but add comment in case we
                //-- ever see it.
                System.out.print("ClassInfo#init: ");
                System.out.print("A referenced archetype is null: ");
                System.out.println(sourceName);
            }
        }
        
        //---------------------/
        //- handle attributes -/
        //---------------------/
        //-- loop throug each attribute
        Enumeration enum = archetype.getAttributeDecls();
        while (enum.hasMoreElements()) {
            AttributeDecl attr = (AttributeDecl)enum.nextElement();
            FieldInfo fieldInfo = MemberFactory.createFieldInfo(attr);
            handleField(fieldInfo, state);
        }
        
        //------------------------/
        //- handle content model -/
        //------------------------/
        //-- check contentType
        ContentType contentType = archetype.getContent();
            
        //-- create text member
        if ((contentType == ContentType.textOnly) ||
            (contentType == ContentType.mixed) ||
            (contentType == ContentType.any)) 
        {
            
            FieldInfo fieldInfo = MemberFactory.createFieldInfoForText();
            handleField(fieldInfo, state);
            
            if (contentType == ContentType.any) {
                fieldInfo = MemberFactory.createFieldInfoForAny();
                handleField(fieldInfo, state);
            }
                
        }
        processContentModel(archetype, state);
    } //-- processArchetype


    private static void handleField(FieldInfo fieldInfo, FactoryState state) {
        
        if (fieldInfo == null) return;
        
        JSourceCode scInitializer 
            = state.jClass.getConstructor(0).getSourceCode();
            
        
        state.classInfo.addFieldInfo(fieldInfo);
        
        state.jClass.addMember(fieldInfo.createMember());
        
        //-- do not create access methods for transient fields
        if (!fieldInfo.isTransient()) {
            state.jClass.addMethods(fieldInfo.createAccessMethods());
        }
        
        //-- Add initialization code
        fieldInfo.generateInitializerCode(scInitializer);
        
    } //-- handleField
    
    /**
     * Processes the given ContentModelGroup
     * @param contentModel the ContentModelGroup to process
    **/
    private static void processContentModel
        (ContentModelGroup contentModel, FactoryState state) 
    {
        
        //------------------------------/
        //- handle elements and groups -/
        //------------------------------/
                
        Enumeration enum = contentModel.enumerate();
                
        FieldInfo fieldInfo = null;
        while (enum.hasMoreElements()) {
                    
            Structure struct = (Structure)enum.nextElement();
            switch(struct.getStructureType()) {
                case Structure.ELEMENT:
                    fieldInfo 
                        = MemberFactory.createFieldInfo((ElementDecl)struct);
                    handleField(fieldInfo, state);
                    break;
                case Structure.GROUP:
                    processContentModel((Group)struct, state);
                    break;
                default:
                    break;
            }
        }
            
    } //-- process(ContentModelGroup)
    
    /**
     * Creates Comments from Schema annotations
     * @param annotated the Annotated structure to process
     * @return the generated comment
    **/
    private static String processAnnotations(Annotated annotated) {
        //-- process annotations
        Enumeration enum = annotated.getAnnotations();
        if (enum.hasMoreElements()) {
            StringBuffer comment = new StringBuffer();
            while (enum.hasMoreElements()) {
                Annotation ann = (Annotation) enum.nextElement();
                Enumeration infos = ann.getInfo();
                while (infos.hasMoreElements()) {
                    Info info = (Info) infos.nextElement();
                    String content = info.getContent();
                    if ( content != null) comment.append(content);
                }
            }
            return comment.toString();
        }
        return null;
    } //-- processAnnotations
    
    /**
     * Creates all the necessary enumeration code from the given 
     * datatype
    **/
    private static void processEnumeration
        (Datatype datatype, FactoryState state) 
    {
        
        Enumeration enum = datatype.getFacets("enumeration");
        
        
        
        JClass jClass = state.jClass;
        String className = jClass.getLocalName();
        
        JMember     member = null;
        JDocComment jdc    = null;
        JSourceCode jsc    = null;
        
        //-- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();
        constructor.addParameter(new JParameter(JType.Int, "type"));
        constructor.addParameter(new JParameter(SGTypes.String, "value"));
        jsc = constructor.getSourceCode();
        jsc.add("this.type = type;");
        jsc.add("this.stringValue = value;");
        
        
        
        //-- #valueOf method
        JMethod mValueOf = new JMethod(jClass, "valueOf");
        mValueOf.addParameter(new JParameter(SGTypes.String, "string"));
        mValueOf.getModifiers().setStatic(true);
        jClass.addMethod(mValueOf);
        jdc = mValueOf.getJDocComment();
        jdc.appendComment("Returns a new " + className);
        jdc.appendComment(" based on the given String value.");

        JSourceCode srcValueOf = mValueOf.getSourceCode();
        
        //-- #toString method
        JMethod mToString = new JMethod(SGTypes.String, "toString");
        jClass.addMethod(mToString);
        jdc = mToString.getJDocComment();
        jdc.appendComment("Returns the String representation of this ");
        jdc.appendComment(className);
        mToString.getSourceCode().add("return this.stringValue;");
        
        //-- Loop through "enumeration" facets
        int count = 0;
        
        while (enum.hasMoreElements()) {
            
            Facet facet = (Facet) enum.nextElement();
            
            String value = facet.getValue();
            String typeName = value.toUpperCase();
            String objName = JavaXMLNaming.toJavaMemberName(value);
            
            
            //-- handle int type
            member = new JMember(JType.Int, typeName);
            member.setComment("The " + value + " type");
            JModifiers modifiers = member.getModifiers();
            modifiers.setFinal(true);
            modifiers.setStatic(true);
            modifiers.makePublic();
            member.setInitString(Integer.toString(count));
            jClass.addMember(member);
            
            //-- handle Class type
            member = new JMember(jClass, objName);
            member.setComment("The instance of the " + value + " type");
            
            modifiers = member.getModifiers();
            
            modifiers.setFinal(true);
            modifiers.setStatic(true);
            modifiers.makePublic();
            
            StringBuffer init = new StringBuffer();
            init.append("new ");
            init.append(className);
            init.append("(");
            init.append(typeName);
            init.append(", \"");
            init.append(value);
            init.append("\")");
            
            member.setInitString(init.toString());
            jClass.addMember(member);
            
            //-- add #valueOf code
            
            if (count != 0) srcValueOf.add("else if (\"");
            else srcValueOf.add("if (\"");
            
            srcValueOf.append(value);
            srcValueOf.append("\".equals(string))");
            srcValueOf.indent();
            srcValueOf.add("return ");
            srcValueOf.append(objName);
            srcValueOf.append(";");
            srcValueOf.unindent();
            
            ++count;
        }
        
        //-- add internal type
        member = new JMember(JType.Int, "type");
        member.setInitString("-1");
        jClass.addMember(member);
        
        //-- add internal stringValue
        member = new JMember(SGTypes.String, "stringValue");
        member.setInitString("null");
        jClass.addMember(member);
        
        
        //-- finish #valueOf
        srcValueOf.add("String err = \"'\" + string + \"' is not a valid ");
        srcValueOf.append(className);
        srcValueOf.append("\";");
        srcValueOf.add("throw new IllegalArgumentException(err);");
        
        //-- add #getType method
        
        JMethod mGetType = new JMethod(JType.Int, "getType");
        mGetType.getSourceCode().add("return this.type;");
        jdc = mGetType.getJDocComment();
        jdc.appendComment("Returns the type of this " + className);
        jClass.addMethod(mGetType);
        
        
        
    } //-- processEnumeration
    
} //-- SourceFactory


/**
 * A class used to save State information for the SourceFactory
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
**/
class FactoryState implements ClassInfoResolver {
    
    JClass    jClass           = null;
    ClassInfo classInfo        = null;
    
    private ClassInfoResolver _resolver = null;
    
    protected FactoryState(String className, ClassInfoResolver resolver) {
        jClass    = new JClass(className);
        classInfo = new ClassInfo(jClass);
        if (resolver == null)
            _resolver = new ClassInfoResolverImpl();
        else
            _resolver = resolver;
    } //-- FactoryState
    
    /**
     * Adds the given Reference to this ClassInfo resolver
     * @param key the key to bind a reference to
     * @param classInfo the ClassInfo which is being referenced
    **/
    public void bindReference(Object key, ClassInfo classInfo) {
        _resolver.bindReference(key, classInfo);
    } //-- bindReference
    
    /**
     * Returns the ClassInfo which has been bound to the given key
     * @param key the object to which the ClassInfo has been bound
     * @return the ClassInfo which has been bound to the given key
    **/
    public ClassInfo resolve(Object key) {
        return _resolver.resolve(key);
    } //-- resolve
    
} //-- FactoryState

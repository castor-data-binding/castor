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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.util;

import org.exolab.javasource.*;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.SGTypes;

/**
 * A class which defines the necessary methods for
 * generating ClassDescriptor source files.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-03-10 15:42:54 -0700 (Fri, 10 Mar 2006) $
**/
public class DescriptorJClass extends JClass {


    //-- org.exolab.castor.mapping
    private static JClass _ClassDescriptorClass
        = new JClass("org.exolab.castor.mapping.ClassDescriptor");

    private static JClass _FieldDescriptorClass
        = new JClass("org.exolab.castor.mapping.FieldDescriptor");

    //-- org.exolab.castor.xml
    private static JClass _XMLFieldDescriptorClass
        = new JClass("org.exolab.castor.xml.XMLFieldDescriptor");


    private static JType _TypeValidatorClass
        = new JClass("org.exolab.castor.xml.TypeValidator");

    //-- methods defined by org.exolab.castor.xml.util.XMLClassDescriptorImpl
    private JMethod _getElementDefinition    = null;

    //-- methods defined by org.exolab.castor.xml.XMLClassDescriptor
    private JMethod _getNameSpacePrefix      = null;
    private JMethod _getNameSpaceURI         = null;
    private JMethod _getXMLName              = null;

    //-- methods defined by org.exolab.castor.mapping.ClassDescriptor
    private JMethod _getAccessMode = null;
    private JMethod _getIdentity   = null;
    private JMethod _getExtends    = null;
    private JMethod _getJavaClass  = null;

    private JClass _type = null;

    private BuilderConfiguration _config = null;

    public DescriptorJClass(BuilderConfiguration config, String className, JClass type) {
        super(className);
        _config = config;
        this._type = type;
        init();
    } //-- DescriptorJClass

    /**
     * Initializes this DescriptorJClass with the required
     * methods
    **/
    private void init() {

        JMethod     method = null;
        JSourceCode jsc    = null;
        boolean extended = false;

        //Make sure that the Descriptor is extended XMLClassDescriptor
        //even when the user has specified a super class for all the generated
        //classes
        String superClass = null;
        if (_config != null) {
            superClass = _config.getProperty(BuilderConfiguration.Property.SUPER_CLASS, null);
        }
        if ( (_type.getSuperClassQualifiedName()==null) || 
			 (_type.getSuperClassQualifiedName().equals(superClass)) )
			setSuperClass("org.exolab.castor.xml.util.XMLClassDescriptorImpl");
		else {
                extended = true;
	    		setSuperClass(_type.getSuperClassQualifiedName()+"Descriptor");
        }
        superClass = null;

//        addImport("org.exolab.castor.xml.NodeType");
//        addImport("org.exolab.castor.xml.XMLFieldHandler");
//        addImport("org.exolab.castor.xml.handlers.*");
//        addImport("org.exolab.castor.xml.util.XMLFieldDescriptorImpl");
//        addImport("org.exolab.castor.xml.validators.*");
//        addImport("org.exolab.castor.xml.FieldValidator");


        addField(new JField(JType.BOOLEAN, "elementDefinition"));

        addField(new JField(SGTypes.String,  "nsPrefix"));
        addField(new JField(SGTypes.String,  "nsURI"));
        addField(new JField(SGTypes.String,  "xmlName"));
        //-- if there is a super class, the identity field must remain
        //-- the same than the one in the super class
        addField(new JField(_XMLFieldDescriptorClass, "identity"));

        //-- create default constructor
        addConstructor( createConstructor() );
        JConstructor cons = getConstructor(0);
        jsc = cons.getSourceCode();
        jsc.add("super();");

        if (extended) {
            //-- add base class (for validation)
            jsc.add("setExtendsWithoutFlatten(");
            jsc.append("new ");
            jsc.append(getSuperClassQualifiedName());
            jsc.append("());");
        }

        //jsc.add("Class[] emptyClassArgs = new Class[0];");
        //jsc.add("Class[] classArgs = new Class[1];");

        //---------------------------------------------/
        //- Methods Defined by XMLClassDescriptorImpl -/
        //---------------------------------------------/

        //-- create isElementDefinition method
        method = new JMethod("isElementDefinition", JType.BOOLEAN,
                             "true if XML schema definition of this Class is that of a global\n" +
                             "element or element with anonymous type definition.");
        jsc = method.getSourceCode();
        jsc.add("return elementDefinition;");
        addMethod(method);
        _getElementDefinition = method;

        //-----------------------------------------/
        //- Methods Defined by XMLClassDescriptor -/
        //-----------------------------------------/

        //-- create getNameSpacePrefix method
        method = new JMethod("getNameSpacePrefix", SGTypes.String,
                             "the namespace prefix to use when marshalling as XML.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return nsPrefix;");
        addMethod(method);
        _getNameSpacePrefix = method;

        //-- create getNameSpaceURI method
        method = new JMethod("getNameSpaceURI", SGTypes.String,
                             "the namespace URI used when marshalling and unmarshalling as XML.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return nsURI;");
        addMethod(method);
        _getNameSpaceURI = method;

        //-- create getValidator method
        method = new JMethod("getValidator", _TypeValidatorClass,
                             "a specific validator for the class described by this ClassDescriptor.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return this;");
        addMethod(method);

        //-- create getXMLName method
        method = new JMethod("getXMLName", SGTypes.String,
                             "the XML Name for the Class being described.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return xmlName;");
        addMethod(method);
        _getXMLName = method;

        //--------------------------------------/
        //- Methods defined by ClassDescriptor -/
        //--------------------------------------/


        //-- create getAccessMode method
        JClass amClass = new JClass("org.exolab.castor.mapping.AccessMode");
        method = new JMethod("getAccessMode", amClass,
                             "the access mode specified for this class.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return null;");
        addMethod(method);
        _getAccessMode = method;

        //-- create getExtends method
        method = new JMethod("getExtends", _ClassDescriptorClass,
                             "the class descriptor of the class extended by this class.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        if (extended) {
            jsc.add("return super.getExtends();");
        }
        else {
            jsc.add("return null;");
        }

        //--don't add the type to the import list
        addMethod(method, false);
        _getExtends = method;

        //-- create getIdentity method
        method = new JMethod("getIdentity", _FieldDescriptorClass,
                             "the identity field, null if this class has no identity.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        if (extended) {
            jsc.add("if (identity == null)");
            jsc.indent();
            jsc.add("return super.getIdentity();");
            jsc.unindent();
        }
        jsc.add("return identity;");

        //--don't add the type to the import list
        addMethod(method, false);
        _getIdentity = method;

        //-- create getJavaClass method
        method = new JMethod("getJavaClass", SGTypes.Class,
                             "the Java class represented by this descriptor.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return ");
        jsc.append(classType(_type));
        jsc.append(";");

        //--don't add the type to the import list
        addMethod(method, false);

        _getJavaClass = method;

    } //-- createSource

    public JMethod getElementDefinitionMethod() {
            return _getElementDefinition;
    } //-- getElementDefinitionMethod

    public JMethod getNameSpacePrefixMethod() {
        return _getNameSpacePrefix;
    } //-- getNamespaceURIMethod

    public JMethod getNameSpacePrefixURI() {
        return _getNameSpaceURI;
    } //-- getNamespacePrefixMethod

    public JMethod getXMLNameMethod() {
        return _getXMLName;
    } //-- getIdentityMethod

    public JMethod getAccessModeMethod() {
        return _getAccessMode;
    } //-- getAccessModeMethod

    public JMethod getExtendsMethod() {
        return _getExtends;
    } //-- getExtendsMethod

    public JMethod getIdentityMethod() {
        return _getIdentity;
    } //-- getIdentityMethod

    public JMethod getJavaClassMethod() {
        return _getJavaClass;
    } // getJavaClassMethod

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Returns the Class type (as a String) for the given XSType
     * @param jType the JType we are to return the class name of
     * @return the Class name (as a String) for the given XSType
    **/
    private static String classType(JType jType) {
        if (jType.isPrimitive()) {
            return jType.getWrapperName() + ".TYPE";
        }
        return jType.toString() + ".class";
    } //-- classType

} //-- DescriptorJClass

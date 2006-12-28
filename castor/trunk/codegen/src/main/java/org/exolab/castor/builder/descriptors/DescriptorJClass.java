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
package org.exolab.castor.builder.descriptors;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.xml.XMLConstants;
import org.exolab.javasource.JAnnotation;
import org.exolab.javasource.JAnnotationType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * A class which defines the necessary methods for generating ClassDescriptor
 * source files.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-03-10 15:42:54 -0700 (Fri, 10 Mar 2006) $
 */
public class DescriptorJClass extends JClass {

    /** Class Descriptors extend this base class. */
    private static final String XMLCLASS_DESCRIPTOR_IMPL = "org.exolab.castor.xml.util.XMLClassDescriptorImpl";
    /** FIXME:  Document this field. */
    private static final String MAPPING_ACCESS_MODE      = "org.exolab.castor.mapping.AccessMode";

    /** Class descriptors implement this interface from org.exolab.castor.mapping. */
    private static final JClass CLASS_DESCRIPTOR_CLASS;
    /** Field descriptors implement this interface from org.exolab.castor.mapping. */
    private static final JClass FIELD_DESCRIPTOR_CLASS;

    /** Field descriptors implement this interface from org.exolab.castor.xml. */
    private static final JClass XML_FIELD_DESCRIPTOR_CLASS;
    /** Type validators implement this interface from org.exolab.castor.xml. */
    private static final JType  TYPE_VALIDATOR_CLASS;

    static {
        CLASS_DESCRIPTOR_CLASS     = new JClass("org.exolab.castor.mapping.ClassDescriptor");
        FIELD_DESCRIPTOR_CLASS     = new JClass("org.exolab.castor.mapping.FieldDescriptor");
        XML_FIELD_DESCRIPTOR_CLASS = new JClass("org.exolab.castor.xml.XMLFieldDescriptor");
        TYPE_VALIDATOR_CLASS       = new JClass("org.exolab.castor.xml.TypeValidator");
    }

    /** The type being described by the Descriptor class we'll generate. */
    private final JClass               _type;
    /** Source Builder configuration. */
    private final BuilderConfiguration _config;

    /**
     * Constructs a DescriptorJClass.
     * @param config Builder Configuration
     * @param className name of this descriptor class
     * @param type the type that is described by this descriptor
     */
    public DescriptorJClass(final BuilderConfiguration config, final String className,
                            final JClass type) {
        super(className);
        this._config = config;
        this._type   = type;
        init();
    } //-- DescriptorJClass

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Initializes this DescriptorJClass with the required methods.
     */
    private void init() {
        // Make sure that the Descriptor is extended XMLClassDescriptor even when
        // the user has specified a super class for all the generated classes
        String superClass = null;
        if (_config != null) {
            superClass = _config.getProperty(BuilderConfiguration.Property.SUPER_CLASS, null);
        }

        boolean extended = false;

        if (_type.getSuperClassQualifiedName() == null
            || _type.getSuperClassQualifiedName().equals(superClass)) {
            setSuperClass(XMLCLASS_DESCRIPTOR_IMPL);
        } else {
            if (_type.getSuperClass() == null) {
                setSuperClass(null);
            } else {
                extended = true;
                setSuperClass(getSuperClassName());
            }
        }
        superClass = null;

        if (_type.getPackageName() != null && _type.getPackageName().length() > 0) {
            addImport(_type.getName());
            if (extended) {
                if (_type.getSuperClass() != null) {
                    addImport(getSuperClassName());
                }
            }
        }

        addField(new JField(JType.BOOLEAN, "elementDefinition"));

        addField(new JField(SGTypes.String, "nsPrefix"));
        addField(new JField(SGTypes.String, "nsURI"));
        addField(new JField(SGTypes.String, "xmlName"));
        //-- if there is a super class, the identity field must remain
        //-- the same than the one in the super class
        addField(new JField(XML_FIELD_DESCRIPTOR_CLASS, "identity"));

        //-- create default constructor
        addDefaultConstructor(extended);

        //jsc.add("Class[] emptyClassArgs = new Class[0];");
        //jsc.add("Class[] classArgs = new Class[1];");

        //---------------------------------------------/
        //- Methods Defined by XMLClassDescriptorImpl -/
        //---------------------------------------------/

        addXMLClassDescriptorImplOverrides();

        //-----------------------------------------/
        //- Methods Defined by XMLClassDescriptor -/
        //-----------------------------------------/

        addXMLClassDescriptorOverrides();

        //--------------------------------------/
        //- Methods defined by ClassDescriptor -/
        //--------------------------------------/

        addClassDescriptorOverrides(extended);
    } //-- createSource

    /**
     * Returns the qualified class name of the super class, adjusted to add the
     * 'descriptors' sub-package.
     * @return Returns the qualified class name of the super class
     */
    private String getSuperClassName() {
        final String superClassName;
        if (_type.getSuperClass().getPackageName() == null
                || _type.getSuperClass().getPackageName().equals("")) {
            if (getPackageName() == null) {
                // no target package specified --> do not append package (=null)
                superClassName = _type.getSuperClass().getLocalName()
                        + XMLConstants.DESCRIPTOR_SUFFIX;
            } else {
                // target package specified --> simply use it
                superClassName = getPackageName() + "." + _type.getSuperClass().getLocalName()
                        + XMLConstants.DESCRIPTOR_SUFFIX;
            }
        } else {
            superClassName = _type.getSuperClass().getPackageName()
                    + XMLConstants.DESCRIPTOR_PACKAGE
                    + _type.getSuperClass().getLocalName() +  XMLConstants.DESCRIPTOR_SUFFIX;
        }
        return superClassName;
    }

    /**
     * Adds our default constructor.
     * @param extended true if we extend another class and thus need to call super()
     */
    private void addDefaultConstructor(final boolean extended) {
        addConstructor(createConstructor());
        JConstructor cons = getConstructor(0);
        JSourceCode jsc = cons.getSourceCode();
        jsc.add("super();");

        if (extended) {
            //-- add base class (for validation)
            jsc.add("setExtendsWithoutFlatten(");
            jsc.append("new ");
            jsc.append(getSuperClassQualifiedName());
            jsc.append("());");
        }
    }

    /**
     * Adds the methods we override from.
     * {@link org.exolab.castor.xml.util.XMLClassDescriptorImpl}
     */
    private void addXMLClassDescriptorImplOverrides() {
        //-- create isElementDefinition method
        JMethod getElementDefinition = new JMethod("isElementDefinition", JType.BOOLEAN,
                               "true if XML schema definition of this Class is that of a global\n"
                               + "element or element with anonymous type definition.");
        JSourceCode jsc = getElementDefinition.getSourceCode();
        jsc.add("return elementDefinition;");
        addMethod(getElementDefinition);
    }

    /**
     * Adds the methods we override from.
     * {@link org.exolab.castor.xml.XMLClassDescriptor}
     */
    private void addXMLClassDescriptorOverrides() {
        JMethod method;
        JSourceCode jsc;
        //-- create getNameSpacePrefix method
        method = new JMethod("getNameSpacePrefix", SGTypes.String,
                             "the namespace prefix to use when marshaling as XML.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return nsPrefix;");
        addMethod(method);

        //-- create getNameSpaceURI method
        method = new JMethod("getNameSpaceURI", SGTypes.String,
                             "the namespace URI used when marshaling and unmarshaling as XML.");

        if (_config.useJava50()) {
            method.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = method.getSourceCode();
        jsc.add("return nsURI;");
        addMethod(method);

        //-- create getValidator method
        method = new JMethod("getValidator", TYPE_VALIDATOR_CLASS,
                             "a specific validator for the class described"
                             + " by this ClassDescriptor.");

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
    }

    /**
     * Adds the methods we override from.
     * {@link org.exolab.castor.mapping.ClassDescriptor}
     * @param extended true if we extend another class and thus need to call super()
     */
    private void addClassDescriptorOverrides(final boolean extended) {
        JSourceCode jsc;

        //-- create getAccessMode method
        JClass amClass = new JClass(MAPPING_ACCESS_MODE);
        JMethod getAccessMode = new JMethod("getAccessMode", amClass,
                                     "the access mode specified for this class.");

        if (_config.useJava50()) {
            getAccessMode.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = getAccessMode.getSourceCode();
        jsc.add("return null;");
        addMethod(getAccessMode);

        //-- create getIdentity method
        JMethod getIdentity = new JMethod("getIdentity", FIELD_DESCRIPTOR_CLASS,
                                   "the identity field, null if this class has no identity.");

        if (_config.useJava50()) {
            getIdentity.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = getIdentity.getSourceCode();
        if (extended) {
            jsc.add("if (identity == null) {");
            jsc.indent();
            jsc.add("return super.getIdentity();");
            jsc.unindent();
            jsc.add("}");
        }
        jsc.add("return identity;");

        //--don't add the type to the import list
        addMethod(getIdentity, false);

        //-- create getJavaClass method
        JMethod getJavaClass = new JMethod("getJavaClass", SGTypes.Class,
                                    "the Java class represented by this descriptor.");

        if (_config.useJava50()) {
            getJavaClass.addAnnotation(new JAnnotation(new JAnnotationType("Override")));
        }

        jsc = getJavaClass.getSourceCode();
        jsc.add("return ");
        jsc.append(classType(_type));
        jsc.append(";");

        //--don't add the type to the import list
        addMethod(getJavaClass, false);
    }

    /**
     * Returns the Class type (as a String) for the given XSType.
     *
     * @param jType
     *            the JType we are to return the class name of
     * @return the Class name (as a String) for the given XSType
     */
    private static String classType(final JType jType) {
        if (jType.isPrimitive()) {
            return jType.getWrapperName() + ".TYPE";
        }
        return jType.toString() + ".class";
    } //-- classType

} //-- DescriptorJClass

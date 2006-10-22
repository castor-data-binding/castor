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
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.XSClass;
import org.exolab.castor.builder.types.XSList;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.builder.util.DescriptorJClass;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JField;
import org.exolab.javasource.JModifiers;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * A factory for creating the source code of descriptor classes
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 07:37:49 -0600 (Thu, 13 Apr 2006) $
 */
public class DescriptorSourceFactory {

    /**
     * GeneralizedFieldHandler
     */
    private static final JClass GENERALIZED_FIELD_HANDLER_CLASS =
        new JClass("org.exolab.castor.mapping.GeneralizedFieldHandler");

    private static final String DESCRIPTOR_NAME      = "Descriptor";
    private static final String FIELD_VALIDATOR_NAME = "fieldValidator";

    /**
     * The BuilderConfiguration instance
     */
    private BuilderConfiguration _config = null;

    /**
     * Creates a new DescriptorSourceFactory with the given configuration
     *
     * @param config the BuilderConfiguration instance
     */
    public DescriptorSourceFactory(final BuilderConfiguration config) {
        if (config == null) {
            String err = "The argument 'config' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _config = config;
    } //-- DescriptorSourceFactory

    /**
     * Creates the Source code of a MarshalInfo for a given XML Schema element
     * declaration
     *
     * @param classInfo
     *            the XML Schema element declaration
     * @return the JClass representing the MarshalInfo source code
     */
    public JClass createSource(final ClassInfo classInfo) {
        JSourceCode jsc            = null;
        JClass jClass              = classInfo.getJClass();
        String className           = jClass.getName();
        String localClassName      = jClass.getLocalName();
        DescriptorJClass classDesc = new DescriptorJClass(_config, className + DESCRIPTOR_NAME, jClass);

        //-- get handle to default constuctor
        JConstructor cons = classDesc.getConstructor(0);
        jsc = cons.getSourceCode();

        //-- Set namespace prefix
        String nsPrefix = classInfo.getNamespacePrefix();
        if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
            jsc.add("nsPrefix = \"");
            jsc.append(nsPrefix);
            jsc.append("\";");
        }

        //-- Set namespace URI
        String nsURI = classInfo.getNamespaceURI();
        if ((nsURI != null) && (nsURI.length() > 0)) {
            jsc.add("nsURI = \"");
            jsc.append(nsURI);
            jsc.append("\";");
        }

        //-- set XML Name
        String xmlName = classInfo.getNodeName();
        if (xmlName != null) {
            jsc.add("xmlName = \"");
            jsc.append(xmlName);
            jsc.append("\";");
        }

        //-- set Element Definition flag
        boolean elementDefinition = classInfo.isElementDefinition();
        jsc.add("elementDefinition = ");
        jsc.append(new Boolean(elementDefinition).toString());
        jsc.append(";");

        //-- set grouping compositor
        if (classInfo.isChoice()) {
            jsc.add("");
            jsc.add("//-- set grouping compositor");
            jsc.add("setCompositorAsChoice();");
        } else if (classInfo.isSequence()) {
            jsc.add("");
            jsc.add("//-- set grouping compositor");
            jsc.add("setCompositorAsSequence();");
        }

        //-- To prevent compiler warnings...make sure
        //-- we don't declare temp variables if field count is 0;
        if (classInfo.getFieldCount() == 0) {
            return classDesc;
        }

        //-- declare temp variables
        jsc.add("org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;");
        jsc.add("org.exolab.castor.mapping.FieldHandler             handler        = null;");
        jsc.add("org.exolab.castor.xml.FieldValidator               fieldValidator = null;");

        //-- handle  content
        if (classInfo.allowContent()) {
            createDescriptor(classDesc, classInfo.getTextField(), localClassName, null, jsc);
        }

        ClassInfo   base = classInfo.getBaseClass();
        FieldInfo[] atts = classInfo.getAttributeFields();

        //--------------------------------/
        //- Create attribute descriptors -/
        //--------------------------------/

        jsc.add("//-- initialize attribute descriptors");
        jsc.add("");

        for (int i = 0; i < atts.length; i++) {
            FieldInfo member = atts[i];
            //-- skip transient members
            if (member.isTransient()) {
                continue;
            }

            if (base != null && base.getAttributeField(member.getNodeName()) != null) {
                createRestrictedDescriptor(member, jsc);
            } else {
                createDescriptor(classDesc, member, localClassName, nsURI, jsc);
            }
        }

        //------------------------------/
        //- Create element descriptors -/
        //------------------------------/
        FieldInfo[] elements = classInfo.getElementFields();

        jsc.add("//-- initialize element descriptors");
        jsc.add("");

        for (int i = 0; i < elements.length; i++) {
            FieldInfo member = elements[i];
            //-- skip transient members
            if (member.isTransient()) {
                continue;
            }

            if (base != null && base.getElementField(member.getNodeName()) != null) {
                createRestrictedDescriptor(member, jsc);
            } else {
                createDescriptor(classDesc, member, localClassName, nsURI, jsc);
            }
        }

        return classDesc;
    } //-- createSource

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Create special code for handling a member that is a restriction.
     * This will only change the Validation Code
     * @param member the restricted member for which we generate the restriction handling.
     * @param jsc the source code to which we append the validation code.
     */
    private static void createRestrictedDescriptor(final FieldInfo member, final JSourceCode jsc) {
        jsc.add("desc = (org.exolab.castor.xml.util.XMLFieldDescriptorImpl) getFieldDescriptor(\"");
        jsc.append(member.getNodeName());
        jsc.append("\"");
        jsc.append(", nsURI");
        if (member.getNodeType() == XMLInfo.ELEMENT_TYPE) {
            jsc.append(", org.exolab.castor.xml.NodeType.Element);");
        } else if (member.getNodeType() == XMLInfo.ATTRIBUTE_TYPE) {
            jsc.append(", org.exolab.castor.xml.NodeType.Attribute);");
        } else {
            jsc.append("org.exolab.castor.xml.NodeType.Text);");
        }
        //--modify the validation code
        validationCode(member, jsc);
    }

    /**
     * Creates a specific descriptor for a given member (whether an attribute or
     * an element) represented by a given Class name.
     *
     * @param classDesc
     *            JClass-equivalent descriptor for this Descriptor class
     * @param member
     *            the member for which to create a descriptor
     * @param localClassName
     *            unqualified (no package) name of this class
     * @param nsURI
     *            namespace URI
     * @param jsc
     *            the source code to which we'll add this descriptor
     */
    private void createDescriptor(final DescriptorJClass classDesc, final FieldInfo member,
                                  final String localClassName, String nsURI,
                                  final JSourceCode jsc) {

        XSType xsType       = member.getSchemaType();
        boolean any         = false;
        boolean isElement   = (member.getNodeType() == XMLInfo.ELEMENT_TYPE);
        boolean isAttribute = (member.getNodeType() == XMLInfo.ATTRIBUTE_TYPE);
        boolean isText      = (member.getNodeType() == XMLInfo.TEXT_TYPE);

        jsc.add("//-- ");
        jsc.append(member.getName());

        //-- a hack, I know, I will change later (kv)
        if (member.getName().equals("_anyObject")) {
            any = true;
        }

        if (xsType.getType() == XSType.COLLECTION) {
            //Attributes can handle COLLECTION type for NMTOKENS or IDREFS for instance
            xsType = ((CollectionInfo) member).getContent().getSchemaType();
        }

        //-- Resolve how the node name parameter to the XMLFieldDescriptorImpl constructor is supplied
        String nodeName = member.getNodeName();
        String nodeNameParam = null;
        if ((nodeName != null) && (!isText)) {
            //-- By default the node name parameter is a literal string
            nodeNameParam = "\"" + nodeName + "\"";
            if (_config.classDescFieldNames()) {
                //-- The node name parameter is a reference to a public static final
                nodeNameParam = member.getNodeName().toUpperCase();
                //-- Expose node name as public static final (reused by XMLFieldDescriptorImpl)
                JModifiers publicStaticFinal = new JModifiers();
                publicStaticFinal.makePublic();
                publicStaticFinal.setStatic(true);
                publicStaticFinal.setFinal(true);
                JField jField = new JField(SGTypes.String, nodeNameParam);
                jField.setModifiers(publicStaticFinal);
                jField.setInitString("\"" + nodeName + "\"");
                classDesc.addMember(jField);
            }
        }

        //-- Generate code to new XMLFieldDescriptorImpl instance
        jsc.add("desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(");
        jsc.append(classType(xsType.getJType()));
        jsc.append(", \"");
        jsc.append(member.getName());
        jsc.append("\", ");
        if (nodeNameParam != null) {
            jsc.append(nodeNameParam);
        } else if (isText) {
            jsc.append("\"PCDATA\"");
        } else {
            jsc.append("(String)null");
        }

        if (isElement) {
            jsc.append(", org.exolab.castor.xml.NodeType.Element);");
        } else if (isAttribute) {
            jsc.append(", org.exolab.castor.xml.NodeType.Attribute);");
        } else if (isText) {
            jsc.append(", org.exolab.castor.xml.NodeType.Text);");
        }

        switch (xsType.getType()) {
            case XSType.STRING_TYPE :
                jsc.add("desc.setImmutable(true);");
                break;
                //only for attributes
            case XSType.IDREF_TYPE :
                jsc.add("desc.setReference(true);");
                break;
            case XSType.ID_TYPE :
                jsc.add("this.identity = desc;");
                break;
            case XSType.QNAME_TYPE :
                jsc.add("desc.setSchemaType(\"QName\");");
                /***********************/
            default :
                break;
        } //switch

        //-- handler access methods
        if (member.getXMLFieldHandler() != null) {
            String handler = member.getXMLFieldHandler();
            jsc.add("handler = new " + handler + "();");
            jsc.add("//-- test for generalized field handler");
            jsc.add("if (handler instanceof ");
            jsc.append(GENERALIZED_FIELD_HANDLER_CLASS.getName());
            jsc.append(")");
            jsc.add("{");
            jsc.indent();
            jsc.add("//-- save reference to user-specified handler");
            jsc.add(GENERALIZED_FIELD_HANDLER_CLASS.getName());
            jsc.append(" gfh = (");
            jsc.append(GENERALIZED_FIELD_HANDLER_CLASS.getName());
            jsc.append(")handler;");
            createXMLFieldHandler(member, xsType, localClassName, jsc, true);
            jsc.add("gfh.setFieldHandler(handler);");
            jsc.add("handler = gfh;");
            jsc.unindent();
            jsc.add("}");
        } else {
            createXMLFieldHandler(member, xsType, localClassName, jsc, false);
            addSpecialHandlerLogic(member, xsType, jsc);
        }

        jsc.add("desc.setHandler(handler);");

        //-- container
        if (member.isContainer()) {
            jsc.add("desc.setContainer(true);");
            String className = xsType.getName(); //set the class descriptor
            //Try to prevent endless loop. Note: we only compare the localClassName.
            //If the packages are different an error can happen here
            if (className.equals(localClassName)) {
                jsc.add("desc.setClassDescriptor(this);");
            } else {
                jsc.add("desc.setClassDescriptor(new " + className + DESCRIPTOR_NAME + "());");
            }
        }

        //-- Handle namespaces
        //-- FieldInfo namespace has higher priority than ClassInfo namespace.
        nsURI = member.getNamespaceURI();
        if (nsURI != null) {
            jsc.add("desc.setNameSpaceURI(\"");
            jsc.append(nsURI);
            jsc.append("\");");
        }

        if (any && member.getNamespaceURI() == null) {
            nsURI = null;
        }

        //-- required
        if (member.isRequired()) {
            jsc.add("desc.setRequired(true);");
        }

        //-- nillable
        if (member.isNillable()) {
           jsc.add("desc.setNillable(true);");
        }

        //-- if any it can match all the names
        if (any) {
            jsc.add("desc.setMatches(\"*\");");
        }

        //-- mark as multi or single valued for elements
        if (isElement || isAttribute) {
            jsc.add("desc.setMultivalued(" + member.isMultivalued());
            jsc.append(");");
        }

        jsc.add("addFieldDescriptor(desc);");
        jsc.add("");

        //-- Add Validation Code
        validationCode(member, jsc);
    } //--CreateDescriptor

    /**
     * Creates the XMLFieldHandler for the given FieldInfo.
     *
     * @param member
     *            the member for which to create an XMLFieldHandler
     * @param xsType the XSType (XML Schema Type) of this field
     * @param localClassName
     *            unqualified (no package) name of this class
     * @param jsc
     *            the source code to which we'll add this XMLFieldHandler
     * @param forGeneralizedHandler Whether to generate a generalized field handler
     */
    private void createXMLFieldHandler(final FieldInfo member, final XSType xsType,
                                       final String localClassName, final JSourceCode jsc,
                                       final boolean forGeneralizedHandler) {

        boolean any          = false;
        boolean isEnumerated = false;

        //-- a hack, I know, I will change later (kv)
        if (member.getName().equals("_anyObject")) {
            any = true;
        }

        if (xsType.getType() == XSType.CLASS) {
            isEnumerated = ((XSClass) xsType).isEnumerated();
        }

        jsc.add("handler = new org.exolab.castor.xml.XMLFieldHandler() {");
        jsc.indent();

        //-- getValue(Object) method
        if (_config.useJava50()) {
            jsc.add("@Override");
        }
        jsc.add("public java.lang.Object getValue( java.lang.Object object ) ");
        jsc.indent();
        jsc.add("throws IllegalStateException");
        jsc.unindent();
        jsc.add("{");
        jsc.indent();
        jsc.add(localClassName);
        jsc.append(" target = (");
        jsc.append(localClassName);
        jsc.append(") object;");
        //-- handle primitives
        if ((!xsType.isEnumerated()) && xsType.getJType().isPrimitive() && (!member.isMultivalued())) {
            jsc.add("if(!target." + member.getHasMethodName() + "())");
            jsc.indent();
            jsc.add("return null;");
            jsc.unindent();
        }
        //-- Return field value
        jsc.add("return ");
        String value = "target." + member.getReadMethodName() + "()";
        if (member.isMultivalued()) {
            jsc.append(value); //--Be careful : different for attributes
        } else {
            jsc.append(xsType.createToJavaObjectCode(value));
        }
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        //--end of getValue(Object) method

        boolean isAttribute = (member.getNodeType() == XMLInfo.ATTRIBUTE_TYPE);
        boolean isContent   = (member.getNodeType() == XMLInfo.TEXT_TYPE);

        //-- setValue(Object, Object) method
        if (_config.useJava50()) {
            jsc.add("@Override");
        }
        jsc.add("public void setValue( java.lang.Object object, java.lang.Object value) ");
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
        //-- check for null primitives
        if (xsType.isPrimitive() && !_config.usePrimitiveWrapper()) {
            if ((!member.isRequired()) && (!xsType.isEnumerated()) && (!member.isMultivalued())) {
                jsc.add("// if null, use delete method for optional primitives ");
                jsc.add("if (value == null) {");
                jsc.indent();
                jsc.add("target.");
                jsc.append(member.getDeleteMethodName());
                jsc.append("();");
                jsc.add("return;");
                jsc.unindent();
                jsc.add("}");
            } else {
                jsc.add("// ignore null values for non optional primitives");
                jsc.add("if (value == null) return;");
                jsc.add("");
            }
        } //if primitive

        jsc.add("target.");
        jsc.append(member.getWriteMethodName());
        jsc.append("( ");
        if (xsType.isPrimitive() && !_config.usePrimitiveWrapper()) {
            jsc.append(xsType.createFromJavaObjectCode("value"));
        } else if (any) {
            jsc.append(" value ");
        } else {
            jsc.append("(");
            jsc.append(xsType.getJType().toString());
            //special handling for the type package
            //when we are dealing with attributes
            //This is a temporary solution since we need to handle
            //the 'types' in specific handlers in the future
            //i.e add specific FieldHandler in org.exolab.castor.xml.handlers
            //dateTime is not concerned by the following since it is directly
            //handle by DateFieldHandler
            if ((isAttribute | isContent) && xsType.isDateTime()
                    && xsType.getType() != XSType.DATETIME_TYPE) {
                jsc.append(".parse");
                jsc.append(JavaNaming.toJavaClassName(xsType.getName()));
                jsc.append("((String) value))");
            } else {
                jsc.append(") value");
            }
        }
        jsc.append(");");

        jsc.unindent();
        jsc.add("}");
        jsc.add("catch (java.lang.Exception ex) {");
        jsc.indent();
        jsc.add("throw new IllegalStateException(ex.toString());");
        jsc.unindent();
        jsc.add("}");
        jsc.unindent();
        jsc.add("}");
        //--end of setValue(Object, Object) method
        
        //-- reset method (handle collections only)
        if (member.isMultivalued()) {
            CollectionInfo cInfo = (CollectionInfo) member;
            // FieldInfo content = cInfo.getContent();
            jsc.add("public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {");
            jsc.indent();
            jsc.add("try {");
            jsc.indent();
            jsc.add(localClassName);
            jsc.append(" target = (");
            jsc.append(localClassName);
            jsc.append(") object;");
            String cName = JavaNaming.toJavaClassName(cInfo.getElementName());
            if (cInfo instanceof CollectionInfoJ2) {
                jsc.add("target.clear" + cName + "();"); 
            } else { 
                jsc.add("target.removeAll" + cName + "();");
            }
            jsc.unindent();
            jsc.add("} catch (java.lang.Exception ex) {"); 
            jsc.indent(); 
            jsc.add("throw new IllegalStateException(ex.toString());"); 
            jsc.unindent(); 
            jsc.add("}");
            jsc.unindent();
            jsc.add("}");
        }
        //-- end of reset method
        

        createNewInstanceMethodForXMLFieldHandler(member, xsType, jsc, forGeneralizedHandler,
                                                  any, isEnumerated);
        jsc.unindent();
        jsc.add("};");
    } //--end of XMLFieldHandler

    /**
     * Creates the newInstance() method of the corresponsing XMLFieldHandler.
     * @param member The member element.
     * @param xsType The XSType instance
     * @param jsc The source code to which to append the 'newInstance' method.
     * @param forGeneralizedHandler Whether to generate a generalized field handler
     * @param any Whether to create a newInstance() method for <xs:any>
     * @param isEnumerated Whether to create a newInstance() method for an enumeration.
     */
    private void createNewInstanceMethodForXMLFieldHandler(
            final FieldInfo member, final XSType xsType, final JSourceCode jsc,
            final boolean forGeneralizedHandler, final boolean any,
            final boolean isEnumerated) {
        boolean isAbstract = false;

        // Commented out according to CASTOR-1340
//        if (member.getDeclaringClassInfo() != null) {
//           isAbstract = member.getDeclaringClassInfo().isAbstract();
//        }

        // check whether class of member is declared as abstract
        if (member.getSchemaType() != null && member.getSchemaType().getJType() instanceof JClass) {
            JClass jClass = (JClass) member.getSchemaType().getJType();
            isAbstract = jClass.getModifiers().isAbstract();
        }

        if (!isAbstract && xsType.getJType() instanceof JClass) {
            JClass jClass = (JClass) xsType.getJType();
            isAbstract = jClass.getModifiers().isAbstract();
        }

        if (!isAbstract && member.getSchemaType() instanceof XSList) {
            XSList xsList = (XSList) member.getSchemaType();
            if (xsList.getContentType().getJType() instanceof JClass) {
                JClass componentType = (JClass) xsList.getContentType().getJType();
                if (componentType.getModifiers().isAbstract()) {
                    isAbstract = componentType.getModifiers().isAbstract();
                }
            }
        }

        if (_config.useJava50()) {
            jsc.add("@Override");
            jsc.add("@SuppressWarnings(\"unused\")");
        }

        jsc.add("public java.lang.Object newInstance( java.lang.Object parent ) {");
        jsc.indent();
        jsc.add("return ");

        if (any || forGeneralizedHandler || isEnumerated
                || xsType.isPrimitive()
                || xsType.getJType() instanceof JArrayType
                || (xsType.getType() == XSType.STRING_TYPE) || isAbstract) {
            jsc.append("null;");
        } else {
            jsc.append(xsType.newInstanceCode());
        }

        jsc.unindent();
        jsc.add("}");
    }

    /**
     * Adds additional logic or wrappers around the core handler for special
     * types such as dates, enumerated types, collections, etc.
     *
     * @param member the member for which extra special handler logic may be created
     * @param xsType the field type for which extra special handler logic may be created
     * @param jsc the java source code to which this will be written
     */
    private void addSpecialHandlerLogic(final FieldInfo member, final XSType xsType,
                                        final JSourceCode jsc) {
        if (xsType.isEnumerated()) {
            jsc.add("handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(");
            jsc.append(classType(xsType.getJType()));
            jsc.append(", handler);");
            jsc.add("desc.setImmutable(true);");
        } else if (xsType.getType() == XSType.DATETIME_TYPE) {
            jsc.add("handler = new org.exolab.castor.xml.handlers.DateFieldHandler(");
            jsc.append("handler);");
            jsc.add("desc.setImmutable(true);");
        } else if (xsType.getType() == XSType.DECIMAL_TYPE) {
            jsc.add("desc.setImmutable(true);");
        } else if (member.getSchemaType().getType() == XSType.COLLECTION) {
            //-- Handle special Collection Types such as NMTOKENS and IDREFS
            switch (xsType.getType()) {
                case XSType.NMTOKEN_TYPE:
                case XSType.NMTOKENS_TYPE:
                    //-- use CollectionFieldHandler
                    jsc.add("handler = new org.exolab.castor.xml.handlers.CollectionFieldHandler(");
                    jsc.append("handler, new org.exolab.castor.xml.validators.NameValidator(");
                    jsc.append("org.exolab.castor.xml.validators.NameValidator.NMTOKEN));");
                    break;
                case XSType.QNAME_TYPE:
                    //-- use CollectionFieldHandler
                    jsc.add("handler = new org.exolab.castor.xml.handlers.CollectionFieldHandler(");
                    jsc.append("handler, null);");
                    break;
                case XSType.IDREF_TYPE:
                case XSType.IDREFS_TYPE:
                    //-- uses special code in UnmarshalHandler
                    //-- see UnmarshalHandler#processIDREF
                    jsc.add("desc.setMultivalued(");
                    jsc.append("" + member.isMultivalued());
                    jsc.append(");");
                    break;
                default:
                    break;
            }
        }
    } //-- addSpecialHandlerLogic

    /**
     * Creates the validation code for a given member. This code will be
     * appended to the given JSourceCode.
     *
     * @param member
     *            the member for which to create the validation code.
     * @param jsc
     *            the JSourceCode to fill in.
     */
    private static void validationCode(final FieldInfo member, final JSourceCode jsc) {
        if (member == null || jsc == null) {
            return;
        }

        jsc.add("//-- validation code for: ");
        jsc.append(member.getName());
        String validator = member.getValidator();
        if (validator != null && validator.length() > 0) {
            jsc.add("fieldValidator = new " + validator + "();");
        } else {
            jsc.add("fieldValidator = new org.exolab.castor.xml.FieldValidator();");

            //-- a hack, I know, I will change later
            if (member.getName().equals("_anyObject")) {
                jsc.add("desc.setValidator(fieldValidator);");
                return;
            }

            XSType xsType = member.getSchemaType();
            //--handle collections
            if ((xsType.getType() == XSType.COLLECTION)) {
                XSList xsList = (XSList) xsType;

                jsc.add("fieldValidator.setMinOccurs(");
                jsc.append(Integer.toString(xsList.getMinimumSize()));
                jsc.append(");");
                if (xsList.getMaximumSize() > 0) {
                    jsc.add("fieldValidator.setMaxOccurs(");
                    jsc.append(Integer.toString(xsList.getMaximumSize()));
                    jsc.append(");");
                }

                xsType = ((CollectionInfo) member).getContent().getSchemaType();
                //special handling for NMTOKEN
                if (xsType.getType() == XSType.NMTOKEN_TYPE) {
                    return;
                }
            } else if (member.isRequired()) {
                jsc.add("fieldValidator.setMinOccurs(1);");
            }

            jsc.add("{ //-- local scope");
            jsc.indent();
            xsType.validationCode(jsc, member.getFixedValue(), FIELD_VALIDATOR_NAME);
            jsc.unindent();
            jsc.add("}");
        }
        jsc.add("desc.setValidator(fieldValidator);");
    }

    /**
     * Returns the Class type (as a String) for the given XSType.
     * @param jType the JType whose Class type will be returned
     * @return the Class type (as a String) for the given XSType.
     */
    private static String classType(final JType jType) {
        if (jType.isPrimitive()) {
            return jType.getWrapperName() + ".TYPE";
        }
        return jType.toString() + ".class";
    } //-- classType

} //-- DescriptorSourceFactory

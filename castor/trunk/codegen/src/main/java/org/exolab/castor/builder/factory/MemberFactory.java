/*
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
 * Copyright 1999-2004 (C) Intalio Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the course
 * of employment at Intalio Inc.
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.builder.factory;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.exolab.castor.builder.BuilderConfiguration;
import org.exolab.castor.builder.ClassInfoResolver;
import org.exolab.castor.builder.GroupNaming;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.XMLInfo;
import org.exolab.castor.builder.types.XSListType;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.castor.builder.types.XSString;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.xml.schema.Annotated;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.Documentation;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.Wildcard;
import org.exolab.castor.xml.schema.XMLType;
import org.exolab.castor.xml.schema.simpletypes.ListType;
import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JPrimitiveType;
import org.exolab.javasource.JType;

/**
 * The "Factory" responsible for creating fields for the given schema components.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class MemberFactory extends BaseFactory {

    /**
     * Creates a new MemberFactory using the given FieldInfo factory.
     *
     * @param config the BuilderConfiguration
     * @param infoFactory the FieldInfoFactory to use
     * @param groupNaming Grou pnaming scheme to be used.
     * @param sourceGenerator Calling source generator
     */
    public MemberFactory(final BuilderConfiguration config,
            final FieldInfoFactory infoFactory,
            final GroupNaming groupNaming,
            final SourceGenerator sourceGenerator) {
        super(config, infoFactory, groupNaming, sourceGenerator);

        if (getConfig().generateExtraCollectionMethods()) {
            this.getInfoFactory().setCreateExtraMethods(true);
        }
        String suffix = getConfig().getProperty(CollectionInfo.REFERENCE_SUFFIX_PROPERTY, null);
        this.getInfoFactory().setReferenceMethodSuffix(suffix);

        if (getConfig().boundPropertiesEnabled()) {
            this.getInfoFactory().setBoundProperties(true);
        }
    } //-- MemberFactory

    /**
     * Creates a FieldInfo for content models that support "any" element.
     *
     * @param any the wildcard we will operate on
     * @param useJava50 if true then we will generate code for Java 5
     *
     * @return the new FieldInfo
     */
    public FieldInfo createFieldInfoForAny(final Wildcard any, final boolean useJava50) {
        if (any == null) {
            return null;
        }

        //--currently anyAttribute is not supported
        if (any.isAttributeWildcard()) {
            return null;
        }

        XSType xsType    = new XSClass(SGTypes.OBJECT, "any");
        String vName     = "_anyObject";
        String xmlName   = null;
        FieldInfo result = null;

        if (any.getMaxOccurs() > 1 || any.getMaxOccurs() < 0) {
            result = this.getInfoFactory().createCollection(xsType, vName, "anyObject", useJava50);
            XSListType xsList = ((CollectionInfo) result).getXSList();
            xsList.setMinimumSize(any.getMinOccurs());
            xsList.setMaximumSize(any.getMaxOccurs());
        } else {
            result = this.getInfoFactory().createFieldInfo(xsType, vName);
        }

        if (any.getMinOccurs() > 0) {
            result.setRequired(true);
        } else {
            result.setRequired(false);
        }

        result .setNodeName(xmlName);

        //--LIMITATION:
        //-- 1- we currently support only the FIRST namespace
        //-- 2- ##other, ##any are not supported
        if (any.getNamespaces().hasMoreElements()) {
             String nsURI = (String) any.getNamespaces().nextElement();
             if (nsURI.length() > 0) {
                 if (nsURI.equals("##targetNamespace")) {
                     Schema schema = any.getSchema();
                     if (schema != null) {
                         result.setNamespaceURI(schema.getTargetNamespace());
                     }
                 } else if (!nsURI.startsWith("##")) {
                     result.setNamespaceURI(nsURI);
                 }
             }
        } //--first namespace
        return result;
    } //-- createFieldInfoForAny()

    /**
     * Creates a FieldInfo to hold the value of a choice.
     *
     * @return the new FieldInfo
     */
    public FieldInfo createFieldInfoForChoiceValue() {
        String fieldName = "_choiceValue";
        XSType xsType = new XSClass(SGTypes.OBJECT, "any");
        FieldInfo fInfo = null;
        fInfo = this.getInfoFactory().createFieldInfo(xsType, fieldName);
        fInfo.setNodeType(XMLInfo.ELEMENT_TYPE);
        fInfo.setComment("Internal choice value storage");
        fInfo.setRequired(false);
        fInfo.setTransient(true);
        fInfo.setNodeName("##any");
        fInfo.setMethods(FieldInfo.READ_METHOD);
        return fInfo;
    } //-- createFieldInfoForChoiceValue

    /**
     * Creates a FieldInfo for content.
     * @param component {@link XMLBindingComponent} instance for accessing binding information.
     * @param xsType the type of content
     * @param useJava50 if true, code will be generated for Java 5
     *
     * @return the new FieldInfo
     */
    public FieldInfo createFieldInfoForContent(final XMLBindingComponent component, 
            final XSType xsType, final boolean useJava50) {
        String fieldName = "_content"; //new xsType()???
        if (component.getContentMemberName() != null) {
            fieldName = component.getContentMemberName();
        }
        FieldInfo fInfo = null;
        if (xsType.isCollection()) {
            fInfo = this.getInfoFactory().createCollection(
                    ((XSListType) xsType).getContentType(), fieldName, null, useJava50);
        } else {
            fInfo = this.getInfoFactory().createFieldInfo(xsType, fieldName);
        }
        fInfo.setNodeType(XMLInfo.TEXT_TYPE);
        fInfo.setComment("internal content storage");
        fInfo.setRequired(false);
        fInfo.setNodeName("#text");
        if (xsType instanceof XSString) {
            fInfo.setDefaultValue("\"\"");
        }
        return fInfo;
    } //-- createFieldInfoForContent

    /**
     * Creates a FieldInfo object for the given XMLBindingComponent.
     *
     * @param component the XMLBindingComponent to create the FieldInfo for
     * @param resolver resolver to use to find ClassInfo
     * @param useJava50 if true, code will be generated for Java 5
     * @return the FieldInfo for the given attribute declaration
     */
    public FieldInfo createFieldInfo(final XMLBindingComponent component,
            final ClassInfoResolver resolver, final boolean useJava50) {
        String xmlName = component.getXMLName();
        String memberName = component.getJavaMemberName();
        if (!memberName.startsWith("_")) {
            memberName = "_" + memberName;
        }

        XMLType xmlType = component.getXMLType();

        ClassInfo classInfo = resolver.resolve(component);

        XSType   xsType = null;
        FieldInfo fieldInfo = null;
        boolean enumeration = false;
        boolean simpleTypeCollection = false;

        if (xmlType != null) {
            if (xmlType.isSimpleType()) {
                SimpleType simpleType = (SimpleType) xmlType;

                SimpleType baseType = null;
                String derivationMethod = simpleType.getDerivationMethod();
                if (derivationMethod != null) {
                    if (SchemaNames.RESTRICTION.equals(derivationMethod)) {
                        baseType = (SimpleType) simpleType.getBaseType();
                    }
                }

                //-- handle special case for enumerated types
                if (simpleType.hasFacet(Facet.ENUMERATION)) {
                    //-- LOok FoR CLasSiNfO iF ReSoLvR is NoT NuLL
                    enumeration = true;
                    if (resolver != null) {
                        classInfo = resolver.resolve(xmlType);
                    }
                    if (classInfo != null) {
                        xsType = classInfo.getSchemaType();
                    }
                } else if ((simpleType instanceof ListType) || (baseType instanceof ListType)) {
                    if (baseType != null) {
                        if (!baseType.isBuiltInType()) {
                            simpleTypeCollection = true;
                        }
                    } else {
                        if (!simpleType.isBuiltInType()) {
                            simpleTypeCollection = true;
                        }
                    }
                }

                if (xsType == null) {
                    xsType = component.getJavaType();
                }
            } else if (xmlType.isAnyType()) {
                //-- Just treat as java.lang.Object.
                if (classInfo != null) {
                    xsType = classInfo.getSchemaType();
                }
                if (xsType == null) {
                    xsType = new XSClass(SGTypes.OBJECT);
                }
            } else if (xmlType.isComplexType() && (xmlType.getName() != null)) {
                //--if we use the type method then no class is output for
                //--the element we are processing
                if (getConfig().mappingSchemaType2Java()) {
                    XMLBindingComponent temp = new XMLBindingComponent(
                            getConfig(), getGroupNaming());
                    temp.setBinding(component.getBinding());
                    temp.setView(xmlType);
                    ClassInfo typeInfo = resolver.resolve(xmlType);
                    if (typeInfo != null) {
                        // if we have not processed the <complexType> referenced
                        // by the ClassInfo yet, this will return null
                        // TODO find a way to resolve an unprocessed <complexType>
                        xsType = typeInfo.getSchemaType();
                    } else {
                        String className = temp.getQualifiedName();
                        if (className != null) {
                            JClass jClass = new JClass(className);
                            if (((ComplexType) xmlType).isAbstract()) {
                                jClass.getModifiers().setAbstract(true);
                            }
                            xsType = new XSClass(jClass);
                            className = null;
                        }
                    }
                }
            } // complexType
        } else {
            if (xsType == null) {
                xsType = component.getJavaType();
            }

            if (xsType == null) {
                //-- patch for bug 1471 (No XMLType specified)
                //-- treat unspecified type as anyType
                switch (component.getAnnotated().getStructureType()) {
                case Structure.ATTRIBUTE:
                case Structure.ELEMENT:
                    xsType = new XSClass(SGTypes.OBJECT);
                    break;
                default:
                    // probably a model-group
                    break;
                }
            }
        }

        // is the XSType found?
        if (xsType == null) {
            String className = component.getQualifiedName();
            JClass jClass = new JClass(className);
            if (component.isAbstract()) {
                jClass.getModifiers().setAbstract(true);
            }
            if (getConfig().isAutomaticConflictResolution()) {
                getSourceGenerator().getXMLInfoRegistry().bind(jClass, 
                        component, "field");
            }
            xsType = new XSClass(jClass);
            if (xmlType != null && xmlType.isComplexType()) {
                ComplexType complexType = (ComplexType) xmlType;
                if (complexType.isAbstract() || getConfig().mappingSchemaElement2Java()) {
                    jClass.getModifiers().setAbstract(true);
                }
            }
            className = null;
        }

        // create the fieldInfo
        // check whether this should be a collection or not
        int maxOccurs = component.getUpperBound();
        int minOccurs = component.getLowerBound();
        if (simpleTypeCollection
                || ((maxOccurs < 0 || maxOccurs > 1) && !this.isChoice(component))) {
            String vName = memberName + "List";

            // if xmlName is null it means that
            // we are processing a container object (group)
            // so we need to adjust the name of the members of the collection
            CollectionInfo cInfo;
            cInfo = this.getInfoFactory().createCollection(xsType, vName, memberName,
                                                       component.getCollectionType(), useJava50);

            XSListType xsList = cInfo.getXSList();
            if (!simpleTypeCollection) {
                xsList.setMaximumSize(maxOccurs);
                xsList.setMinimumSize(minOccurs);
            }
            fieldInfo = cInfo;
        } else  {
            switch (xsType.getType()) {
                case XSType.ID_TYPE:
                     fieldInfo = this.getInfoFactory().createIdentity(memberName);
                     break;
                case XSType.COLLECTION:
                case XSType.IDREFS_TYPE:
                case XSType.NMTOKENS_TYPE:
                    String collectionName = component.getCollectionType();
                    XSType contentType = ((XSListType) xsType).getContentType();
                    fieldInfo = this.getInfoFactory().createCollection(contentType,
                                                                   memberName, memberName,
                                                                   collectionName, useJava50);
                    break;
                default:
                    fieldInfo = this.getInfoFactory().createFieldInfo(xsType, memberName);
                    break;
            }
        }

        // initialize the field
        fieldInfo.setNodeName(xmlName);
        fieldInfo.setRequired(minOccurs > 0);
        switch (component.getAnnotated().getStructureType()) {
            case Structure.ELEMENT:
                 fieldInfo.setNodeType(XMLInfo.ELEMENT_TYPE);
                 break;
            case Structure.ATTRIBUTE:
                fieldInfo.setNodeType(XMLInfo.ATTRIBUTE_TYPE);
                break;
            case Structure.MODELGROUP:
            case Structure.GROUP:
                fieldInfo.setNodeName(XMLInfo.CHOICE_NODE_NAME_ERROR_INDICATION);
                fieldInfo.setContainer(true);
                break;
            default:
                break;
        }

        //-- handle namespace URI / prefix
        String nsURI = component.getTargetNamespace();
        if ((nsURI != null) && (nsURI.length() > 0)) {
            fieldInfo.setNamespaceURI(nsURI);
            // TODO set the prefix used in the XML Schema
            //      in order to use it inside the Marshaling Framework
        }

        // handle default value (if any is set)
        handleDefaultValue(component, classInfo, xsType, fieldInfo, enumeration);

        //-- handle nillable values
        if (component.isNillable()) {
            fieldInfo.setNillable(true);
        }

        //-- add annotated comments
        String comment = createComment(component.getAnnotated());
        if (comment != null) {
             fieldInfo.setComment(comment);
        }

        //--specific field handler or validator?
        if (component.getXMLFieldHandler() != null) {
            fieldInfo.setXMLFieldHandler(component.getXMLFieldHandler());
        }

        if (component.getValidator() != null) {
            fieldInfo.setValidator(component.getValidator());
        }

        if (component.getVisiblity() != null) {
            String visibility = component.getVisiblity();
            fieldInfo.setVisibility(visibility);
        }
        
        // deal with substitution groups
        switch (component.getAnnotated().getStructureType()) {
        case Structure.ELEMENT:
            ElementDecl elementDeclaration = (ElementDecl) component.getAnnotated();
            if (elementDeclaration.isReference()) {
                elementDeclaration = elementDeclaration.getReference();
            }
            Enumeration possibleSubstitutes = elementDeclaration.getSubstitutionGroupMembers(); 
            if (possibleSubstitutes.hasMoreElements()) {
                List substitutionGroupMembers = new ArrayList();
                while (possibleSubstitutes.hasMoreElements()) {
                    ElementDecl substitute = (ElementDecl) possibleSubstitutes.nextElement();
                    substitutionGroupMembers.add(substitute.getName());
                }
                fieldInfo.setSubstitutionGroupMembers(substitutionGroupMembers);
            }
        default:
        }

        return fieldInfo;
    }

    /**
     * Determines if the given <code>component</code> represents a choice.
     * 
     * @param component
     *            The XMLBindingComponent to check.
     * @return <code>true</code> if and only if the given XMLBindingComponent
     *         represents a choice. Otherwise returns <code>false</code>
     */
    private boolean isChoice(final XMLBindingComponent component) {
        Group group = this.getGroup(component.getAnnotated());
        if (group == null || group.getOrder() == null) {
            return false;
        }
        
        return group.getOrder().getType() == Order.CHOICE;
    }

    /**
     * Returns the given <code>structure</code> as Group if it represents one.<br>
     * <br>
     * If the given <code>structure</code> has the structure type
     * <code>GROUP</code> this method returns the given <code>structure</code>
     * itself (casted to Group). If the structure is of any other type or is
     * <code>null</code> this method will return <code>null</code>
     * 
     * @param structure
     *            The Structure to be returned as Group.
     * @return The given <code>structure</code> if and only if it is a group;
     *         otherwise returns <code>null</code>.
     */
    private Group getGroup(final Structure structure) {
        if (structure == null) {
            return null;
        }
        
        if (structure.getStructureType() == Structure.GROUP) {
            return (Group) structure;
        }
        
        return null;
    }

    /**
     * Handle default or fixed value, if any is set.
     * @param component The component on which a default value is set
     * @param classInfo The corresponding ClassInfo instance.
     * @param xsType The schema type of the component.
     * @param fieldInfo The FieldInfo into which to inject a default value
     * @param enumeration If we are looking at an enumeration.
     */
    private void handleDefaultValue(final XMLBindingComponent component, final ClassInfo classInfo,
            final XSType xsType, final FieldInfo fieldInfo, final boolean enumeration) {

        String value = component.getValue();
        if (value == null) {
            return;
        }

        value = adjustDefaultValue(xsType, value);

        if (value.length() == 0) {
            value = "\"\"";
        }

        // TODO Need to change this...and to validate the value...to be done at reading time.

        //-- clean up value
        //-- if the xsd field is mapped into a java.lang.String
        if  (xsType.getJType().toString().equals("java.lang.String")) {
            char ch = value.charAt(0);
            if (ch != '\'' && ch != '\"') {
                value = '\"' + value + '\"';
            }
        } else if (enumeration) {
            //-- we'll need to change this when enumerations are no longer treated as strings
            JType jType = (classInfo != null) ? classInfo.getJClass() : xsType.getJType();
            value = jType.getName() + ".valueOf(\"" + value + "\")";
        } else if (xsType.getJType().isArray()) {
            JType componentType = ((JArrayType) xsType.getJType()).getComponentType();
            if (componentType.isPrimitive()) {
                JPrimitiveType primitive = (JPrimitiveType) componentType;
                value = "new " + primitive.getName() + "[] { "
                      + primitive.getWrapperName() + ".valueOf(\"" + value
                      + "\")." + primitive.getName() + "Value() }";
            } else {
                value = "new " + componentType.getName() + "[] { "
                      + componentType.getName() + ".valueOf(\"" + value + "\") }";
                
            }
        } else if (!(xsType.getJType().isPrimitive())) {
            if (xsType.isDateTime()) {
                // Castor marshals DATETIME_TYPE into java.util.Date(), so we need to convert it
                if (xsType.getType() == XSType.DATETIME_TYPE) {
                    // FIXME This fails if the DateTIme has a time zone
                    //       because we throw away the time zone in toDate()
                    value = "new org.exolab.castor.types.DateTime(\"" + value + "\").toDate()";
                } else {
                    value = "new " + xsType.getJType().getName() + "(\"" + value + "\")";
                }
            } else {
                // FIXME This works only if a constructor with String as parameter exists
                value = "new " + xsType.getJType().getName() + "(\"" + value + "\")";
            }
        }

        if (component.isFixed()) {
            fieldInfo.setFixedValue(value);
        } else {
            fieldInfo.setDefaultValue(value);
        }
    }

    /**
     * Adjusts the default value string represenation to reflect the semantics
     * of various 'special' data types.
     *
     * @param xsType The XMl schems type of the value to adjust
     * @param value The actual value to adjust
     * @return an adjusted default value.
     */
    private String adjustDefaultValue(final XSType xsType, final String value) {
        switch (xsType.getType()) {
            case XSType.FLOAT_TYPE:
                return value + 'f';
            case XSType.BOOLEAN_TYPE:
                Boolean bool = new Boolean(value);
                return bool.toString();
            default:
                break;
        }
        return value;
    }

    /**
     * Creates a comment to be used in Javadoc from the given Annotated
     * Structure.
     *
     * @param annotated the Annotated structure to process
     * @return the generated comment
     */
    private String createComment(final Annotated annotated) {
        //-- process annotations
        Enumeration enumeration = annotated.getAnnotations();
        if (enumeration.hasMoreElements()) {
            //-- just use first annotation
            return createComment((Annotation) enumeration.nextElement());
        }
        //-- there were no annotations...try possible references
        switch(annotated.getStructureType()) {
            case Structure.ELEMENT:
                ElementDecl elem = (ElementDecl) annotated;
                if (elem.isReference()) {
                    return createComment(elem.getReference());
                }
                break;
            case Structure.ATTRIBUTE:
                AttributeDecl att = (AttributeDecl) annotated;
                if (att.isReference()) {
                    return createComment(att.getReference());
                }
                break;
            default:
                break;
        }
        return null;
    } //-- createComment

    /**
     * Creates a comment to be used in Javadoc from the given Annotation.
     *
     * @param annotation the Annotation to create the comment from
     * @return the generated comment
     */
    private String createComment(final Annotation annotation) {
        if (annotation == null) {
            return null;
        }

        Enumeration enumeration = annotation.getDocumentation();
        if (enumeration.hasMoreElements()) {
            //-- just use first <info>
            Documentation documentation = (Documentation) enumeration.nextElement();
            return normalize(documentation.getContent());
        }
        return null;
    } //-- createComment

} //-- MemberFactory

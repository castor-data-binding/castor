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
 * Copyright 1999-2003 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.binding.XMLBindingComponent;
import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.simpletypes.ListType;
import org.exolab.castor.util.Configuration;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The "Factory" responsible for creating fields for
 * the given schema components
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class MemberFactory {


    /**
     * The FieldInfo factory.
     */
    private FieldInfoFactory _infoFactory = null;

    /**
     * The BuilderConfiguration instance, for callbacks
     * to obtain certain configured properties
     */
    private BuilderConfiguration _config = null;
    
    /**
     * Creates a new MemberFactory with default type factory.
     *
     * @param config the BuilderConfiguration
     */
    public MemberFactory(BuilderConfiguration config) {
        this(config, new FieldInfoFactory());
    } //-- MemberFactory


    /**
     * Creates a new MemberFactory using the given FieldInfo factory.
     *
     * @param config the BuilderConfiguration
     * @param infoFactory the FieldInfoFactory to use
     */
    public MemberFactory(BuilderConfiguration config, FieldInfoFactory infoFactory)
    {
        super();
        if (config == null) {
            String err = "The 'BuilderConfiguration' argument must not be null.";
            throw new IllegalArgumentException(err);
        }
        _config = config;
        
        if (infoFactory == null)
            _infoFactory = new FieldInfoFactory();
        else
            _infoFactory = infoFactory;
            
        if (_config.generateExtraCollectionMethods()) {
            _infoFactory.setCreateExtraMethods(true);
        }
        String suffix = _config.getProperty(CollectionInfo.REFERENCE_SUFFIX_PROPERTY, null);
        _infoFactory.setReferenceMethodSuffix(suffix);
        
        if (_config.boundPropertiesEnabled()) {
            _infoFactory.setBoundProperties(true);
        }

    } //-- MemberFactory


    /**
     * Creates a FieldInfo for content models that support "any" element.
     * @return the new FieldInfo
    **/
    public FieldInfo createFieldInfoForAny(Wildcard any) {
        if (any == null)
            return null;
        //--currently anyAttribute is not supported
        if (any.isAttributeWildcard()) {
            return null;
        }

        XSType xsType = new XSClass(SGTypes.Object, "any");
        String vName = "_anyObject";
        String xmlName = null;
        FieldInfo result = null;
        if (any.getMaxOccurs() >1 || any.getMaxOccurs() <0 ) {
            result = _infoFactory.createCollection(xsType, vName, "anyObject");
            XSList xsList = ((CollectionInfo)result).getXSList();
            xsList.setMinimumSize(any.getMinOccurs());
            xsList.setMaximumSize(any.getMaxOccurs());
        } 
        else
            result = _infoFactory.createFieldInfo(xsType, vName);
        if (any.getMinOccurs() > 0 )
            result.setRequired(true);
        else
            result.setRequired(false);
        result .setNodeName(xmlName);

        //--LIMITATION:
        //-- 1- we currently support only the FIRST namespace
        //-- 2- ##other, ##any are not supported
        if (any.getNamespaces().hasMoreElements()) {
             String nsURI = (String)any.getNamespaces().nextElement();
             if (nsURI.length() >0) {
                 if (nsURI.equals("##targetNamespace")) {
                     Schema schema = any.getSchema();
                     if (schema != null)
                         result.setNamespaceURI(schema.getTargetNamespace());
                 }
                 else if (!nsURI.startsWith("##")) {
                     result.setNamespaceURI(nsURI);
                 }
             }
        }//--first namespace
        return result;
    } //-- createFieldInfoForAny()


    /**
     * Creates a FieldInfo for content.
     * @param xsType the type of content
     * @return the new FieldInfo
    **/
    public FieldInfo createFieldInfoForContent(XSType xsType) {

        String fieldName = "_content";               //new xsType()???
        FieldInfo fInfo = null;
        if (xsType.getType() == XSType.COLLECTION) {
            fInfo = _infoFactory.createCollection( ((XSList) xsType).getContentType(),
                                                     fieldName,
                                                     null);
                    
        }
        
        else {
            fInfo = _infoFactory.createFieldInfo(xsType,fieldName);
        }
        fInfo.setNodeType(XMLInfo.TEXT_TYPE);
        fInfo.setComment("internal content storage");
        fInfo.setRequired(false);
        fInfo.setNodeName("#text");
        if (xsType instanceof XSString)
            fInfo.setDefaultValue("\"\"");
        return fInfo;

    } //-- createFieldInfoForContent



    /**
     * Creates a FieldInfo object for the given XMLBindingComponent.
     *
     * @param component the XMLBindingComponent to create the
     * FieldInfo for
     * @return the FieldInfo for the given attribute declaration
     */
    public FieldInfo createFieldInfo
        (XMLBindingComponent component, ClassInfoResolver resolver)
    {
        
        String xmlName = component.getXMLName();
        String memberName = component.getJavaMemberName();
        if (!memberName.startsWith("_"))
            memberName = "_"+memberName;

        XMLType xmlType = component.getXMLType();
        
        ClassInfo classInfo = resolver.resolve(component);

        XSType   xsType = null;
        FieldInfo fieldInfo = null;
        boolean enumeration = false;
        boolean simpleTypeCollection = false;

        if (xmlType != null) {
            if (xmlType.isSimpleType()) {
                SimpleType simpleType = (SimpleType)xmlType;

                //-- handle special case for enumerated types
                if (simpleType.hasFacet(Facet.ENUMERATION)) {
                    //-- LOok FoR CLasSiNfO iF ReSoLvR is NoT NuLL
                    enumeration = true;
                    if (resolver != null) {
                        classInfo = resolver.resolve(simpleType);
                    }
                    if (classInfo != null)
                        xsType = classInfo.getSchemaType();
                }
                else if (simpleType instanceof ListType) {
                    if (!simpleType.isBuiltInType())
                        simpleTypeCollection = true;
                }
                
                if (xsType == null)
                    xsType = component.getJavaType();
            }//--simpleType
            else if (xmlType.isAnyType()) {
                //-- Just treat as java.lang.Object.
                if (classInfo != null)
                    xsType = classInfo.getSchemaType();
                if (xsType == null)
                    xsType = new XSClass(SGTypes.Object);
            }//--AnyType
            else if (xmlType.isComplexType() && (xmlType.getName() != null)) {
                //--if we use the type method then no class is output for
                //--the element we are processing
                if (_config.mappingSchemaType2Java()) {
                    XMLBindingComponent temp = new XMLBindingComponent(_config);
                    temp.setBinding(component.getBinding());
                    temp.setView(xmlType);
                    String className = temp.getQualifiedName();
                    if (className != null) {
                    	xsType = new XSClass(new JClass(className));
                        className = null;
                    }
                }
            }//--complexType
        }
        else {
            
            //-- patch for bug 1471 (No XMLType specified)
            //-- treat unspecified type as anyType
            switch (component.getAnnotated().getStructureType()) {
                case Structure.ATTRIBUTE:
                case Structure.ELEMENT:
                    xsType = new XSClass(SGTypes.Object);
                    break;
                default:
                    // probably a model-group
                    break;
            }
        }

        //--is the XSType found?
        if (xsType == null) {
            
            String className = component.getQualifiedName();
            xsType = new XSClass(new JClass(className));
            className = null;
        }
        //--create the fieldInfo
        //-- check whether this should be a collection or not
        int maxOccurs = component.getUpperBound();
        int minOccurs = component.getLowerBound();
        if (simpleTypeCollection || ((maxOccurs < 0) || (maxOccurs > 1))) {
            String vName = memberName+"List";
            
            //--if xmlName is null it means that 
            //--we are processing a container object (group)
            //--so we need to adjust the name of the members of the collection
            CollectionInfo cInfo;
            cInfo = _infoFactory.createCollection(xsType, vName, component.getJavaMemberName());
            
            XSList xsList = cInfo.getXSList();
            if (!simpleTypeCollection) {
                xsList.setMaximumSize(maxOccurs);
                xsList.setMinimumSize(minOccurs);
            }
            fieldInfo = cInfo;
        } else  {
            switch (xsType.getType()) {
                case XSType.ID_TYPE:
                     fieldInfo = _infoFactory.createIdentity(memberName);
                     break;
                case XSType.COLLECTION:
                    String collectionName = component.getCollectionType();
                    fieldInfo = _infoFactory.createCollection( ((XSList) xsType).getContentType(),
                                                               memberName,
                                                               xmlName, collectionName);
                    break;
                default:
                    fieldInfo = _infoFactory.createFieldInfo(xsType, memberName);
                    break;
            }
        }

        boolean createForElement = false;
        boolean createForAttribute = false;
        //--initialize the field
        fieldInfo.setNodeName(xmlName);
        fieldInfo.setRequired(minOccurs > 0);
        switch (component.getAnnotated().getStructureType()) {
            case Structure.ELEMENT:
                 fieldInfo.setNodeType(XMLInfo.ELEMENT_TYPE);
                 ElementDecl element = (ElementDecl)component.getAnnotated();
                 createForElement = true;
                 break;
            case Structure.ATTRIBUTE:
                fieldInfo.setNodeType(XMLInfo.ATTRIBUTE_TYPE);
                createForAttribute = true;
                break;
            case Structure.MODELGROUP:
            case Structure.GROUP:
                fieldInfo.setNodeName("-error-if-this-is-used-");
                fieldInfo.setContainer(true);
                break;
        }

        //-- handle namespace URI / prefix
        String nsURI = component.getTargetNamespace();
        if ((nsURI != null) && (nsURI.length() > 0)) {
            fieldInfo.setNamespaceURI(nsURI);
            /**
             * @todo set the prefix used in the XML Schema in 
             * order to use it inside the Marshalling Framework
             */
        }

        //--handle default value
        //--TO CLEAN UP IN A SEPARATE METHOD???
        String value = component.getValue();
        if (value != null) {
            // various type adjustements
            switch (xsType.getType()) {

                case XSType.FLOAT_TYPE:
                    value = value + 'f';
                    break;

                case XSType.BOOLEAN_TYPE:
                    Boolean bool = new Boolean(value);
                    value = bool.toString();
                    break;

                default:
                    break;
            }

            if (value.length() == 0)
                value="\"\"";
            //-- XXX Need to change this...and we
            //-- XXX need to validate the value...to be done at reading time.

            //-- clean up value
            //-- if the xsd field is mapped into a java.lang.String
            if  (xsType.getJType().toString().equals("java.lang.String")) {
                char ch = value.charAt(0);
                switch (ch) {
                    case '\'':
                    case '\"':
                        break;
                    default:
                        value = '\"' + value + '\"';
                        break;
                }
            } else if (enumeration) {

                //-- we'll need to change this
                //-- when enumerations are no longer
                //-- treated as strings
                JType jType = null; 
                if (classInfo != null) { 
                    jType = classInfo.getJClass();
                } 
                else { 
                    jType = xsType.getJType(); 
                } 
                    
                String tmp = jType.getName() + ".valueOf(\"" + value;
                tmp += "\")";
                value = tmp;
                 
                
            }
            //don't generate code for date/time type since the constructor that parses
            //a string is throwing exception
            else if (!xsType.getJType().isPrimitive() && !xsType.isDateTime()) {
                 //XXX This works only if a constructor
                 //XXX with String as parameter exists
                 value = "new "+xsType.getJType().toString()+"(\""+value+"\")";
            }

            if (component.isFixed())
                fieldInfo.setFixedValue(value);
            else
                fieldInfo.setDefaultValue(value);
        }

        //-- add annotated comments
        String comment = createComment(component.getAnnotated());
        if (comment != null)
             fieldInfo.setComment(comment);

        //--specific field handler or validator?
        if (component.getXMLFieldHandler() != null)
            fieldInfo.setXMLFieldHandler(component.getXMLFieldHandler());
        if (component.getValidator() != null)
            fieldInfo.setValidator(component.getValidator());

        return fieldInfo;
    }

    /**
     * Creates a comment to be used in Javadoc from
     * the given Annotated Structure.
     * @param annotated the Annotated structure to process
     * @return the generated comment
    **/
    private String createComment(Annotated annotated) {

        //-- process annotations
        Enumeration enum = annotated.getAnnotations();
        if (enum.hasMoreElements()) {
            //-- just use first annotation
            return createComment((Annotation) enum.nextElement());
        }
        else {
            //-- there were no annotations...try possible references
            switch(annotated.getStructureType()) {
                case Structure.ELEMENT:
                    ElementDecl elem = (ElementDecl)annotated;
                    if (elem.isReference()) {
                        return createComment(elem.getReference());
                    }
                    break;
                case Structure.ATTRIBUTE:
                    AttributeDecl att = (AttributeDecl)annotated;
                    if (att.isReference()) {
                        return createComment(att.getReference());
                    }
                    break;
                default:
                    break;
            }
        }
        return null;
    } //-- createComment

    /**
     * Creates a comment to be used in Javadoc from the given Annotation
     * @param annotation the Annotation to create the comment from
     * @return the generated comment
    **/
    private String createComment(Annotation annotation) {
        if (annotation == null) return null;
        Enumeration enum = annotation.getDocumentation();
        if (enum.hasMoreElements()) {
            //-- just use first <info>
            Documentation documentation = (Documentation) enum.nextElement();
            return normalize(documentation.getContent());
        }
        return null;
    } //-- createComment

    /**
     * Normalizes the given string for use in comments
     *
     * @param value the String to normalize
    **/
    private static String normalize (String value) {

        if (value == null) return null;

        char[] chars = value.toCharArray();
        char[] newChars = new char[chars.length];
        int count = 0;
        int i = 0;
        boolean skip = false;

        while (i < chars.length) {
            char ch = chars[i++];

            if ((ch == ' ') || (ch == '\t')) {
                if ((!skip) && (count != 0)) {
                    newChars[count++] = ' ';
                }
                skip = true;
            }
            else {
                if (count == 0) {
                    //-- ignore new lines only if count == 0
                    if ((ch == '\r') || (ch == '\n')) {
                        continue;
                    }
                }
                newChars[count++] = ch;
                skip = false;
            }
        }
        return new String(newChars,0,count);
    }

} //-- MemberFactory

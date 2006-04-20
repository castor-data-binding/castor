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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.util.Configuration;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class MemberFactory {


    /**
     * The FieldInfo factory.
    **/
    private FieldInfoFactory infoFactory = null;


    /**
     * Creates a new MemberFactory with default type factory.
    **/
    public MemberFactory() {
        this(new FieldInfoFactory());
    } //-- MemberFactory


    /**
     * Creates a new MemberFactory using the given FieldInfo factory.
     * @param infoFactory the FieldInfoFactory to use
    **/
    public MemberFactory(FieldInfoFactory infoFactory)
    {
        super();

        //this.resolver = resolver;

        if (infoFactory == null)
            this.infoFactory = new FieldInfoFactory();
        else
            this.infoFactory = infoFactory;


    } //-- MemberFactory


    /**
     * Creates a FieldInfo for content models that support "any" element.
     * @return the new FieldInfo
    **/
    public FieldInfo createFieldInfoForAny() {

        XSType xsType = new XSClass(SGTypes.Object, "any");
        String vName = "_anyObject";
        String xmlName = null;
        CollectionInfo cInfo = infoFactory.createCollection(xsType, vName, "anyObject");
        XSList xsList = cInfo.getXSList();
        xsList.setMinimumSize(0);
        cInfo.setRequired(false);
        cInfo.setNodeName(xmlName);
        return cInfo;
    } //-- createFieldInfoForAny()

    /**
     * Creates a FieldInfo for content.
     * @param xsType the type of content
     * @return the new FieldInfo
    **/
    public FieldInfo createFieldInfoForContent(XSType xsType) {

        String fieldName = "_content";               //new xsType()???
        FieldInfo fInfo = infoFactory.createFieldInfo(xsType,fieldName);
        fInfo.setNodeType(XMLInfo.TEXT_TYPE);
        fInfo.setComment("internal content storage");
        fInfo.setRequired(false);
        fInfo.setNodeName("#text");
        if (xsType instanceof XSString)
           fInfo.setDefaultValue("\"\"");
        return fInfo;

    } //-- createFieldInfoForContent


    /**
     * Creates a FieldInfo object for the given attribute
     * declaration
     * @param attribute the XML attribute declaration to create the
     * FieldInfo for
     * @return the FieldInfo for the given attribute declaration
    **/
    public FieldInfo createFieldInfo
        (AttributeDecl attribute, ClassInfoResolver resolver)
    {

        //don't need to check if it is a referenced attribute
        //since it has already been checked in the SourceFactory
        String memberName
            = JavaNaming.toJavaMemberName(attribute.getName(false));

        if (!memberName.startsWith("_"))
            memberName = "_"+memberName;

        FieldInfo fieldInfo = null;

        SimpleType simpleType = attribute.getSimpleType();
        XSType   xsType = null;
        ClassInfo cInfo = null;

        boolean enumeration = false;

        if (simpleType != null) {

            if (simpleType.hasFacet(Facet.ENUMERATION)) {
                enumeration = true;

                //-- LOok FoR CLasSiNfO iF ReSoLvR is NoT NuLL
                if (resolver != null) {
                    cInfo = resolver.resolve(simpleType);
                }

                if (cInfo != null)
                    xsType = cInfo.getSchemaType();
            }

            if (xsType == null)
                xsType = TypeConversion.convertType(simpleType);
        }
        else
            xsType = new XSString();

        switch (xsType.getType()) {
            case XSType.INTEGER_TYPE:
                fieldInfo = infoFactory.createFieldInfo(xsType, memberName);
                break;
            case XSType.ID_TYPE:
                fieldInfo = infoFactory.createIdentity(memberName);
                break;
            case XSType.COLLECTION:
                 fieldInfo = infoFactory.createCollection( ((XSList) xsType).getContentType(),
                                                             memberName,
                                                             attribute.getName()
                                                              );
                 break;
            default:
                fieldInfo = infoFactory.createFieldInfo(xsType, memberName);
                break;
        }

        fieldInfo.setNodeName(attribute.getName());
        fieldInfo.setNodeType(XMLInfo.ATTRIBUTE_TYPE);
        fieldInfo.setRequired(attribute.isRequired());

        String value = attribute.getValue();
        if (value != null) {

           if (value.length() == 0)
              value="\"\"";
            //-- XXX Need to change this...and we
            //-- XXX need to validate the value.
            //-- This should be done by the SOM not the SourceGenerator (Arnaud)

            //-- clean up value
            //-- if the xsd field is mapped into a java.lang.String
            if  (xsType.getJType().toString().equals("java.lang.String"))
            {
                char ch = value.charAt(0);
                switch (ch) {
                    case '\'':
                    case '\"':
                        break;
                    default:
                        value = '\"' + value + '\"';
                        break;
                }
            }
            else if (enumeration) {

                //-- we'll need to change this
                //-- when enumerations are no longer
                //-- treated as strings
                JClass jClass = cInfo.getJClass();
                String tmp = jClass.getName() + ".valueOf(\"" + value;
                tmp += "\");";
                value = tmp;
            }
            else if (!xsType.getJType().isPrimitive()) {
                 //XXX This works only if a constructor
                 //XXX with String as parameter exists
                 value = "new "+xsType.getJType().toString()+"(\""+value+"\")";
            }

            if (attribute.isFixed()) {
                fieldInfo.setFixedValue(value);
            }
            else
                fieldInfo.setDefaultValue(value);
        }


        //fieldInfo.setSchemaType(attribute.getSimpletypeRef());

        //-- add annotated comments
        String comment = createComment(attribute);
        if (comment != null) fieldInfo.setComment(comment);

        return fieldInfo;
    } //-- createFieldInfo

    /**
     * Creates a new FieldInfo based on the given ElementDecl
     *
     * @param element the ElementDecl to create the FieldInfo for
     * @return the new FieldInfo
    **/
    public FieldInfo createFieldInfo
        (ElementDecl element, ClassInfoResolver resolver)
    {

        //-- check whether this should be a Vector or not
        int maxOccurs = element.getMaxOccurs();
        int minOccurs = element.getMinOccurs();

        ElementDecl eDecl = element;
        ClassInfo classInfo = null;

        //-- the element is a reference:
        //-- If mapping schema elements, replace element passed in with referenced element
        //-- if not we just retrieve the correct NCNAME and check to see if the element referenced
        //-- really exist (this check must be handle by the new version of the SOM)

        if (eDecl.isReference()) {

            ElementDecl eRef = eDecl.getReference();
            if (eRef == null) {
                String err = "unable to resolve element reference: ";
                err += element.getName();
                System.out.println(err);
                return null;
            }
            if (SourceGenerator.mappingSchemaElement2Java())
                eDecl = eRef;

            //garbage collected
            eRef = null;
        }
        //-- determine type

        JSourceCode jsc     = null;
        FieldInfo fieldInfo = null;
        XSType   xsType     = null;

        XMLType xmlType = eDecl.getType();
        boolean enumeration  = false;
        boolean isContainer = false;

        //-- SimpleType
        if ((xmlType != null) && xmlType.isSimpleType()) {

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

            if (xsType == null)
                xsType = TypeConversion.convertType(simpleType);

            //-- print warning message if ID, IDREF, IDREFS, NMTOKEN, NTOKENS are
            //-- used as element type
             if ( (xsType.getType() == xsType.ID_TYPE) ||
                 (xsType.getType() == xsType.IDREFS_TYPE)||
                 ( (xsType.getType() == xsType.COLLECTION) &&
                   ( ( (XSList) xsType).getContentType().getType() == xsType.IDREF_TYPE) ) ||
                 (xsType.getType() == xsType.NMTOKEN_TYPE)  )
                    System.out.println("Warning : For XML Compatibility " +
                                        xsType.getName()+" should be used only on attributes\n");

        }
        //-- ComplexType
        else {

            String className = null;
            //-- Java class name depends on mapping setup in properties file
            if (SourceGenerator.mappingSchemaElement2Java())
            {
                // Java class name is element name
                className = JavaNaming.toJavaClassName(eDecl.getName());
                if (className==null)
                    return null;
                else {
                    //make sure we append namespaces information
                    //if defined in the property file
                    String packageName = SourceGenerator.getJavaPackage(eDecl.getSchema().getTargetNamespace());
                    if ((packageName != null) && (packageName.length() > 0)) {
                        className = packageName + '.' + className;
                    }
                   packageName = null;
                }
            }
            else if (SourceGenerator.mappingSchemaType2Java())
            {
                // Java class name is schema type name
                className = JavaNaming.toJavaClassName(getElementType(eDecl));
                // Prefix package qualifier?
                if (eDecl.getType()!=null)
                {
                    Schema elementSchema = eDecl.getSchema();
                    Schema typeSchema = eDecl.getType().getSchema();
                    if (elementSchema!=typeSchema)
                        className = SourceGenerator.getQualifiedClassName(typeSchema.getTargetNamespace(),className);
                }
            }
            xsType = new XSClass(new JClass(className));
            if ((xmlType != null) && (xmlType.isComplexType())) {
                ComplexType cType = (ComplexType)xmlType;
                int max = cType.getMaxOccurs();
                if ((max < 0) || (max > 1)) {
                    //isContainer = true;
                }
            }
        } // complexType

        String fieldName = JavaNaming.toJavaMemberName(eDecl.getName(false));
        if (fieldName.charAt(0) != '_')
            fieldName = "_"+fieldName;

        if (maxOccurs != 1) {
            String vName = fieldName+"List";
            CollectionInfo cInfo
                = infoFactory.createCollection(xsType, vName, eDecl.getName(false));

            XSList xsList = cInfo.getXSList();
            xsList.setMaximumSize(maxOccurs);
            xsList.setMinimumSize(minOccurs);
            fieldInfo = cInfo;

        }
        else  {
             if (xsType.getType() == xsType.COLLECTION)
                 fieldInfo = infoFactory.createCollection( ((XSList) xsType).getContentType(),
                                                             fieldName,
                                                             eDecl.getName(false)
                                                              );

             else fieldInfo = infoFactory.createFieldInfo(xsType, fieldName);
        }

        fieldInfo.setRequired(minOccurs > 0);
        fieldInfo.setNodeName(eDecl.getName(false));
        fieldInfo.setContainer(isContainer);
        //handle fixed or default values
        String value = (eDecl.getDefaultValue() != null)?eDecl.getDefaultValue():eDecl.getFixedValue();

        if (value != null) {

           if (value.length() == 0)
              value="\"\"";
            //-- XXX Need to change this...and we
            //-- XXX need to validate the value.
            //-- This should be done by the SOM not the SourceGenerator (Arnaud)

            //-- clean up value
            //-- if the xsd field is mapped into a java.lang.String
            if  (xsType.getJType().toString().equals("java.lang.String"))
            {
                char ch = value.charAt(0);
                switch (ch) {
                    case '\'':
                    case '\"':
                        break;
                    default:
                        value = '\"' + value + '\"';
                        break;
                }
            }
            else if (enumeration) {

                //-- we'll need to change this
                //-- when enumerations are no longer
                //-- treated as strings
                JClass jClass = classInfo.getJClass();
                String tmp = jClass.getName() + ".valueOf(\"" + value;
                tmp += "\");";
                value = tmp;
            }
            else if (!xsType.getJType().isPrimitive()) {
                 //XXX This works only if a constructor
                 //XXX with String as parameter exists
                 value = "new "+xsType.getJType().toString()+"(\""+value+"\")";
            }
            if (eDecl.getFixedValue() != null) {
                fieldInfo.setFixedValue(value);
            }
            else
                fieldInfo.setDefaultValue(value);
        }
        //-- add annotated comments

        //-- use elementRef first if necessary
        String comment = null;
        Enumeration enum = element.getAnnotations();
        if (enum.hasMoreElements())
            comment = createComment((Annotation)enum.nextElement());
        else {
            comment = createComment(eDecl);
        }

        if (comment != null) fieldInfo.setComment(comment);

        return fieldInfo;
    } //-- createFieldInfo(ElementDecl)

    /**
     * Creates a new FieldInfo based on the given Group
     *
     * @param group the Group to create the FieldInfo for
     * @return a new FieldInfo for the given Group
    **/
    public FieldInfo createFieldInfo
        (Group group, SGStateInfo sgState)
    {

        String groupName = group.getName();

        if (groupName == null) {
            groupName = sgState.getGroupNaming().createClassName(group);
            if (groupName == null) {
                String err = "Unable to create name for group";
                throw new IllegalStateException(err);
            }
        }


        //-- check whether this should be a Vector or not
        int maxOccurs = group.getMaxOccurs();
        int minOccurs = group.getMinOccurs();
       //-- determine type

        JSourceCode jsc     = null;
        FieldInfo fieldInfo = null;
        XSType xsType       = null;

        JClass groupClass = null;
        String className = null;

        ClassInfo classInfo = sgState.resolve(group);
        if (classInfo != null) {
            groupClass = classInfo.getJClass();
            xsType = classInfo.getSchemaType();
        }


        if (groupClass == null) {
            // Java class name is group name or.
            className = JavaNaming.toJavaClassName(groupName);
            xsType = new XSClass(new JClass(className));
        }
        else {
            className = groupClass.getName();
        }

        String fieldName = JavaNaming.toJavaMemberName(className);
        if (fieldName.charAt(0) != '_')
            fieldName = "_"+fieldName;

        if (maxOccurs != 1) {
            String vName = fieldName+"List";
            CollectionInfo cInfo
                = infoFactory.createCollection(xsType, vName, groupName);

            XSList xsList = cInfo.getXSList();
            xsList.setMaximumSize(maxOccurs);
            xsList.setMinimumSize(minOccurs);
            fieldInfo = cInfo;

        }
        else {
             fieldInfo = infoFactory.createFieldInfo(xsType, fieldName);
        }

        fieldInfo.setRequired(minOccurs > 0);
        fieldInfo.setNodeName("-error-if-this-is-used-");
        fieldInfo.setContainer(true);

        //-- add annotated comments
        String comment = null;
        comment = createComment(group);
        if (comment != null) fieldInfo.setComment(comment);

        return fieldInfo;
    } //-- createFieldInfo(Group)


    /**
     * Returns the actual element type (handles 'ref' attribute and anonymous complextypes)
     * @param e The element the type is need from
     * @return The actual element type
     */
    private String getElementType(ElementDecl e)
    {
        ElementDecl element = e;
        Hashtable refs = new Hashtable();
        String className = null;
        while(className==null)
        {
            // Handle element's with 'ref' attribute
            if (e.isReference())
                e = e.getReference();
            if (e == null) {
                String err = "unable to resolve element reference: ";
                err += element.getName(false);
                System.out.println(err);
                return null;
            }
            else if (refs.get(e.getName())!=null) {
                String err = "cyclic element reference: ";
                err += element.getName(false);
                System.out.println(err);
                return null;
            }
            refs.put(e.getName(), e);
            if (e.isReference())
                continue;

            // Is element using a named complexType?
            XMLType xmlType = e.getType();
            if (xmlType!=null)
                className = xmlType.getName();
            if (className==null)
                // No type, then class is the element name (elements using anonymous complexType's)
                className = e.getName(false);
        }
        return className;
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
    private String normalize (String value) {

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

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
import org.exolab.castor.xml.JavaXMLNaming;
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
    */
    private FieldInfoFactory infoFactory = null;


    /** Constructor that creates a default type factory.
    */
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
        String memberName = "obj";
        String vName = "_anyObject";
        String xmlName = null;
        CollectionInfo cInfo = infoFactory.createCollection(xsType, vName, "object");
        XSList xsList = cInfo.getXSList();
        xsList.setMinimumSize(0);
        cInfo.setRequired(false);
        cInfo.setNodeName(xmlName);
        return cInfo;
    } //-- createFieldInfoForAny()

    /**
     * Creates a FieldInfo for Text content
     * @return the new FieldInfo
    **/
    public FieldInfo createFieldInfoForText() {

        XSType xsType = new XSString();
        String fieldName = "_content";
        FieldInfo fInfo = infoFactory.createFieldInfo(new XSString(),fieldName);
        fInfo.setNodeType(XMLInfo.TEXT_TYPE);
        fInfo.setComment("internal character storage");
        fInfo.setRequired(false);
        fInfo.setNodeName("#text");
        fInfo.setDefaultValue("\"\"");
        return fInfo;

    } //-- createFieldInfoForText


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

        String memberName
            = JavaXMLNaming.toJavaMemberName(attribute.getName());

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
            case XSType.INTEGER:
                fieldInfo = infoFactory.createFieldInfo(xsType, memberName);
                fieldInfo.setCodeHelper(
                    new IntegerCodeHelper((XSInteger)xsType)
                );

                break;
            case XSType.ID:
                fieldInfo = infoFactory.createIdentity(memberName);
                break;
            case XSType.COLLECTION:
                 fieldInfo = infoFactory.createCollection( ((XSList) xsType).getContentType(),
                                                             memberName,
                                                             ((XSList) xsType).getContentType().getName()
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
            
            //-- XXX Need to change this...and we
            //-- XXX need to validate the value.
            
            //-- clean up value
            if (xsType.getType() == XSType.STRING) {
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
            
            if (attribute.isFixed())
                fieldInfo.setFixedValue(value);
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
     * Creates a member based on the given ElementDecl
     * @param element the ElementDecl to create the member from
    **/
    public FieldInfo createFieldInfo
        (ElementDecl element, ClassInfoResolver resolver)
    {

        //-- check whether this should be a Vector or not
        int maxOccurs = element.getMaxOccurs();
        int minOccurs = element.getMinOccurs();

        ElementDecl eDecl = element;

		//-- If mapping schema elements, replace element passed in with referenced element
		if (SourceGenerator.mappingSchemaElement2Java())
		{
			if (eDecl.isReference()) {
			    ElementDecl eRef = eDecl.getReference();
			    if (eRef == null) {
			        String err = "unable to resolve element reference: ";
			        err += element.getName();
			        System.out.println(err);
			        return null;
			    }
			    else eDecl = eRef;
			}
		}

		//-- determine type

        JSourceCode jsc     = null;
        FieldInfo fieldInfo = null;
        XSType   xsType     = null;

        XMLType xmlType = eDecl.getType();
        //-- SimpleType
        if ((xmlType != null) && xmlType.isSimpleType()) {

            SimpleType simpleType = (SimpleType)xmlType;

            //-- handle special case for enumerated types
            if (simpleType.hasFacet(Facet.ENUMERATION)) {
                //-- LOok FoR CLasSiNfO iF ReSoLvR is NoT NuLL
                ClassInfo cInfo = null;
                if (resolver != null) {
                    cInfo = resolver.resolve(simpleType);
                }

                if (cInfo != null)
                    xsType = cInfo.getSchemaType();
            }

            if (xsType == null)
                xsType = TypeConversion.convertType(simpleType);

            //-- print warning message if ID, IDREF, IDREFS, NMTOKEN, NTOKENS are
            //-- used as element type
             if ( (xsType.getType() == xsType.ID) ||
                 (xsType.getType() == xsType.IDREF)||
                 ( (xsType.getType() == xsType.COLLECTION) &&
                   ( ( (XSList) xsType).getContentType().getType() == xsType.IDREF) ) ||
                 (xsType.getType() == xsType.NMTOKEN)  )
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
				className = JavaXMLNaming.toJavaClassName(eDecl.getName());
				if (className==null)
					return null;
			}
			else if (SourceGenerator.mappingSchemaType2Java())
			{
				// Java class name is schema type name
				className = JavaXMLNaming.toJavaClassName(getElementType(eDecl));
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
        }

        String fieldName = JavaXMLNaming.toJavaMemberName(eDecl.getName());
        if (fieldName.charAt(0) != '_')
            fieldName = "_"+fieldName;

        if (maxOccurs != 1) {
            String vName = fieldName+"List";
            CollectionInfo cInfo
                = infoFactory.createCollection(xsType, vName, eDecl.getName());

            XSList xsList = cInfo.getXSList();
            xsList.setMaximumSize(maxOccurs);
            xsList.setMinimumSize(minOccurs);
            fieldInfo = cInfo;

        }
        else {
             if (xsType.getType() == xsType.COLLECTION)
                 fieldInfo = infoFactory.createCollection( ((XSList) xsType).getContentType(),
                                                             fieldName,
                                                             eDecl.getName()
                                                              );

             else fieldInfo = infoFactory.createFieldInfo(xsType, fieldName);
        }

        fieldInfo.setRequired(minOccurs > 0);
        fieldInfo.setNodeName(eDecl.getName());

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
            return documentation.getContent();
        }
        return null;
    } //-- createComment

} //-- MemberFactory

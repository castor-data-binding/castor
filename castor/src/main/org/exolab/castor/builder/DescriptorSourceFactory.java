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

package org.exolab.castor.builder;

import org.exolab.javasource.*;
import org.exolab.castor.builder.util.DescriptorJClass;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.builder.types.*;

/**
 * A factory for creating the source code of descriptor classes
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DescriptorSourceFactory {

	//-- org.exolab.castor.mapping
	private static JClass _ClassDescriptorClass =
		new JClass("org.exolab.castor.mapping.ClassDescriptor");

	private static JClass _FieldDescriptorClass =
		new JClass("org.exolab.castor.mapping.FieldDescriptor");

	//-- org.exolab.castor.xml
	private static JClass fdImplClass =
		new JClass("org.exolab.castor.xml.util.XMLFieldDescriptorImpl");

	private static JClass fdClass =
		new JClass("org.exolab.castor.xml.XMLFieldDescriptor");

	private static JType fdArrayClass = fdClass.createArray();

	private static JClass gvrClass =
		new JClass("org.exolab.castor.xml.GroupValidationRule");

	private static JClass vrClass =
		new JClass("org.exolab.castor.xml.ValidationRule");

	private static final String DESCRIPTOR_NAME = "Descriptor";
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
    public DescriptorSourceFactory(BuilderConfiguration config) {
        if (config == null) {
            String err = "The argument 'config' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _config = config;
    } //-- DescriptorSourceFactory
    
	/**
	 * Creates the Source code of a MarshalInfo for a given XML Schema
	 * element declaration
	 * @param element the XML Schema element declaration
	 * @return the JClass representing the MarshalInfo source code
	 */
	public JClass createSource(ClassInfo classInfo) {

		JMethod method = null;
		JSourceCode jsc = null;
		JSourceCode vcode = null;
		JClass jClass = classInfo.getJClass();
		String className = jClass.getName();
		String localClassName = jClass.getLocalName();

		String variableName = "_" + className;

		DescriptorJClass classDesc =
			new DescriptorJClass(_config, className + DESCRIPTOR_NAME, jClass);

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
		if (classInfo.getFieldCount() == 0)
			return classDesc;

		//-- declare temp variables
		jsc.add("org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;");
		jsc.add("org.exolab.castor.xml.XMLFieldHandler              handler        = null;");
		jsc.add("org.exolab.castor.xml.FieldValidator               fieldValidator = null;");

		//-- handle  content
		if (classInfo.allowContent())
			createDescriptor(
				classDesc,
				classInfo.getTextField(),
				localClassName,
				null,
				jsc);

		ClassInfo base = classInfo.getBaseClass();

		FieldInfo[] atts = classInfo.getAttributeFields();
		//--------------------------------/
		//- Create attribute descriptors -/
		//--------------------------------/

		jsc.add("//-- initialize attribute descriptors");
		jsc.add("");

		for (int i = 0; i < atts.length; i++) {
			FieldInfo member = atts[i];
			//-- skip transient members
			if (member.isTransient())
				continue;
			if (base != null
				&& base.getAttributeField(member.getNodeName()) != null)
				createRestrictedDescriptor(member, jsc);
			else
				createDescriptor(classDesc, member, localClassName, nsURI, jsc);
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
			if (member.isTransient())
				continue;
			if (base != null
				&& base.getElementField(member.getNodeName()) != null)
				createRestrictedDescriptor(member, jsc);
			else
				createDescriptor(classDesc, member, localClassName, nsURI, jsc);
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
	 * @param jsc the source code to which we append the valdiation code.
	 */

	private static void createRestrictedDescriptor(
		FieldInfo member,
		JSourceCode jsc) {
		jsc.add("desc = (org.exolab.castor.xml.util.XMLFieldDescriptorImpl) getFieldDescriptor(\"");
		jsc.append(member.getNodeName());
		jsc.append("\"");
		if (member.getNodeType() == FieldInfo.ELEMENT_TYPE)
			jsc.append(", org.exolab.castor.xml.NodeType.Element);");
		else if (member.getNodeType() == FieldInfo.ATTRIBUTE_TYPE)
			jsc.append(", org.exolab.castor.xml.NodeType.Attribute);");
		//--modify the validation code
		validationCode(member, jsc);
	}

	/**
	 * Create a specific descriptor for a given member (whether an attribute or
	 * an element) represented by a given Class name
	 */
	private void createDescriptor
	    (DescriptorJClass classDesc,
	     FieldInfo member,
	     String localClassName, 
	     String nsURI, 
	     JSourceCode jsc) 
    {

		XSType xsType = member.getSchemaType();
		boolean any = false;
		boolean isElement = (member.getNodeType() == FieldInfo.ELEMENT_TYPE);
		boolean isAttribute =
			(member.getNodeType() == FieldInfo.ATTRIBUTE_TYPE);
		boolean isText = (member.getNodeType() == FieldInfo.TEXT_TYPE);

		jsc.add("//-- ");
		jsc.append(member.getName());

		//-- a hack, I know, I will change later (kv)
		if (member.getName().equals("_anyObject"))
			any = true;

		if (xsType.getType() == XSType.COLLECTION)
			//Attributes can handle COLLECTION type for NMTOKENS or IDREFS for instance
			xsType = ((CollectionInfo) member).getContent().getSchemaType();

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

		if (isElement)
			jsc.append(", org.exolab.castor.xml.NodeType.Element);");
		else if (isAttribute)
			jsc.append(", org.exolab.castor.xml.NodeType.Attribute);");
		else if (isText)
			jsc.append(", org.exolab.castor.xml.NodeType.Text);");
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

		//}//else

		//-- handler access methods
		if (member.getXMLFieldHandler() != null) {
			String handler = member.getXMLFieldHandler();
			jsc.add("desc.setHandler(new " + handler + "());");
		} else
		    createXMLFieldHandler(member, xsType, localClassName, jsc);

		//-- container
		if (member.isContainer()) {
			jsc.add("desc.setContainer(true);");
			//set the class descriptor
			String className = xsType.getName();
			//prevent endless loop
			//Limitation: we only compare the localClassName, if the packages are
			//different an error can happen here
			if (className.equals(localClassName))
				jsc.add("desc.setClassDescriptor(this);");
			else
				jsc.add(
					"desc.setClassDescriptor(new "
						+ className
						+ DESCRIPTOR_NAME
						+ "());");
		}

		//-- Handle namespaces
		//-- FieldInfo namespace has higher priority than ClassInfo
		//-- namespace.
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
		//-- if any it can match all the names
		if (any)
			jsc.add("desc.setMatches(\"*\");");
		//-- mark as multi or single valued for elements
		if (isElement) {
			jsc.add("desc.setMultivalued(" + member.isMultivalued());
			jsc.append(");");
		}

		jsc.add("addFieldDescriptor(desc);");
		jsc.add("");

		//-- Add Validation Code
		validationCode(member, jsc);

	} //--CreateDescriptor

    /**
     * Creates the XMLFieldHandler for the given FieldInfo
     */
	private void createXMLFieldHandler
	    (FieldInfo member, XSType xsType, String localClassName, JSourceCode jsc) 
	{

		boolean any = false;
		boolean isEnumerated = false;
		//-- a hack, I know, I will change later (kv)
		if (member.getName().equals("_anyObject"))
			any = true;
		boolean isAttribute =
			(member.getNodeType() == FieldInfo.ATTRIBUTE_TYPE);
	    boolean isContent = 
	        (member.getNodeType() == FieldInfo.TEXT_TYPE);
	    
	    if (xsType.getType() == XSType.CLASS)
			isEnumerated = ((XSClass) xsType).isEnumerated();

		jsc.add("handler = (new org.exolab.castor.xml.XMLFieldHandler() {");
		jsc.indent();

		//-- read method
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
		if ((!xsType.isEnumerated())
			&& xsType.getJType().isPrimitive()
			&& (!member.isMultivalued())) {
			jsc.add("if(!target." + member.getHasMethodName() + "())");
			jsc.indent();
			jsc.add("return null;");
			jsc.unindent();
		}
		//-- Return field value
		jsc.add("return ");
		String value = "target." + member.getReadMethodName() + "()";
		if (member.isMultivalued())
			jsc.append(value); //--Be careful : different for attributes
		else
			jsc.append(xsType.createToJavaObjectCode(value));
		jsc.append(";");
		jsc.unindent();
		jsc.add("}");
		//--end of read method

		//-- write method
		jsc.add(
			"public void setValue( java.lang.Object object, java.lang.Object value) ");
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
			if ((!member.isRequired())
				&& (!xsType.isEnumerated())
				&& (!member.isMultivalued())) {
				jsc.add(
					"// if null, use delete method for optional primitives ");
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
			if ( (isAttribute | isContent)
				&& xsType.isDateTime()
				&& xsType.getType() != xsType.DATETIME_TYPE) {
				jsc.append(".parse");
				jsc.append(JavaNaming.toJavaClassName(xsType.getName()));
				jsc.append("((String) value))");
			} else
				jsc.append(") value");
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
		//--end of write method

		//-- newInstance method
		jsc.add(
			"public java.lang.Object newInstance( java.lang.Object parent ) {");
		jsc.indent();
		jsc.add("return ");
        
        boolean isAbstract = false;
        if (member.getDeclaringClassInfo() != null)
		    isAbstract = member.getDeclaringClassInfo().isAbstract();
        if (any
			|| isEnumerated
			|| xsType.isPrimitive()
			|| xsType.getJType().isArray()
			|| (xsType.getType() == XSType.STRING_TYPE)
            || isAbstract) {
			jsc.append("null;");
		} else {
			jsc.append(xsType.newInstanceCode());
		}
		jsc.unindent();
		jsc.add("}");
		//--end of new Instance method
		jsc.unindent();
		jsc.add("} );");
		//--end of XMLFieldDescriptor

		if (isEnumerated) {
			jsc.add("desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(");
			jsc.append(classType(xsType.getJType()));
			jsc.append(", handler));");
			jsc.add("desc.setImmutable(true);");
		} else if (xsType.getType() == XSType.DATETIME_TYPE) {
			jsc.add("desc.setHandler( new org.exolab.castor.xml.handlers.DateFieldHandler(");
			jsc.append("handler));");
			jsc.add("desc.setImmutable(true);");
		} else if (xsType.getType() == XSType.DECIMAL_TYPE) {
			jsc.add("desc.setHandler(handler);");
			jsc.add("desc.setImmutable(true);");
		}
		//-- Handle special Collection Types such as NMTOKENS and IDREFS
		else if (member.getSchemaType().getType() == XSType.COLLECTION) {
			switch (xsType.getType()) {
				case XSType.NMTOKEN_TYPE:
				case XSType.NMTOKENS_TYPE:
					//-- use CollectionFieldHandler
					jsc.add("desc.setHandler( new org.exolab.castor.xml.handlers.CollectionFieldHandler(");
					jsc.append(
						"handler, new org.exolab.castor.xml.validators.NameValidator(org.exolab.castor.xml.validators.NameValidator.NMTOKEN)));");
					break;
				case XSType.QNAME_TYPE:
					//-- use CollectionFieldHandler
					jsc.add("desc.setHandler( new org.exolab.castor.xml.handlers.CollectionFieldHandler(");
					jsc.append(
						"handler, null));");
					break;
				case XSType.IDREF_TYPE :
				case XSType.IDREFS_TYPE :
					//-- uses special code in UnmarshalHandler
					//-- see UnmarshalHandler#processIDREF
					jsc.add("desc.setMultivalued(" + member.isMultivalued());
					jsc.append(");");
					/* do not break here */
				default :
					jsc.add("desc.setHandler(handler);");
					break;
			}
		} else
			jsc.add("desc.setHandler(handler);");
	}

	/**
	 * Creates the validation code for a given member.
	 * This code will be appended to the given JSourceCode.
	 *
	 * @param member the member for which to create the validation code.
	 * @param jsc the JSourceCode to fill in.
	 */
	private static void validationCode(FieldInfo member, JSourceCode jsc) {

		if (member == null)
			return;
		if (jsc == null)
			return;

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
				CollectionInfo cInfo = (CollectionInfo) member;
				FieldInfo content = cInfo.getContent();

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
				if (xsType.getType() == XSType.NMTOKEN_TYPE)
					return;
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
	 * Returns the Class type (as a String) for the given XSType
	**/
	private static String classType(JType jType) {
		if (jType.isPrimitive()) {
			if (jType == JType.Int)
				return "java.lang.Integer.TYPE";
			else if (jType == JType.Double)
				return "java.lang.Double.TYPE";
			else if (jType == JType.Boolean) {
				return "java.lang.Boolean.TYPE";
			}
		}
		return jType.toString() + ".class";
	} //-- classType

} //-- DescriptorSourceFactory

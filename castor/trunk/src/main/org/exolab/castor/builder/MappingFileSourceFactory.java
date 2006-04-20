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
 * Copyright (C) 2005 Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JType;
import org.exolab.castor.builder.types.*;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.xml.ClassChoice;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.types.BindXmlNodeType;

/**
 * A factory for creating mapping files 
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class MappingFileSourceFactory {
    /**
     * Creates a new MappingFileSourceFactory with the given 
     * configuration
     *
     * @param config the BuilderConfiguration instance
     */
    public MappingFileSourceFactory(BuilderConfiguration config) 
    {
        if (config == null) {
            String err = "The argument 'config' must not be null.";
            throw new IllegalArgumentException(err);
        }
    } //-- MappingFileSourceFactory
    
	/**
     * Creates the class mapping for the given ClassInfo
     * 
	 * @param classInfo the XML Schema element declaration
	 * @return the ClassMapping representing the ClassInfo
	 */
	public ClassMapping createMapping(ClassInfo classInfo) {

        ClassMapping classMapping = new ClassMapping();
        
        JClass jClass = classInfo.getJClass();
        String className = jClass.getName();
        
        classMapping.setName(className);
        
		//-- Set namespace prefix
        MapTo mapTo = new MapTo();
        classMapping.setMapTo(mapTo);
        
		String nsPrefix = classInfo.getNamespacePrefix();
		if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
            mapTo.setNsPrefix(nsPrefix);
		}

		//-- Set namespace URI
		String nsURI = classInfo.getNamespaceURI();
		if ((nsURI != null) && (nsURI.length() > 0)) {
            mapTo.setNsUri(nsURI);
		}

		//-- set XML Name
		mapTo.setXml(classInfo.getNodeName());
		
		//-- set Element Definition flag
		mapTo.setElementDefinition(classInfo.isElementDefinition());
		
		//-- set grouping compositor
		if (classInfo.isChoice()) {
            //-- TODO: need a way to specify choice in 
            //-- Mapping file
		} 
        
        boolean isAbstract = classInfo.isAbstract();
        if (!isAbstract) {
            isAbstract = jClass.getModifiers().isAbstract();
        }
        classInfo.setAbstract(isAbstract);
 
		//-- To prevent compiler warnings...make sure
		//-- we don't declare temp variables if field count is 0;
		if (classInfo.getFieldCount() == 0)
			return classMapping;


		//-- handle  content
		if (classInfo.allowContent())
			createFieldMapping(
				classMapping,
				classInfo.getTextField(),
				null);

		ClassInfo base = classInfo.getBaseClass();
        if (base != null) {
            classMapping.setExtends(base.getJClass().getName());
        }

		FieldInfo[] atts = classInfo.getAttributeFields();
        
		//-----------------------------/
		//- Create attribute mappings -/
		//-----------------------------/

		for (int i = 0; i < atts.length; i++) {
			FieldInfo member = atts[i];
			//-- skip transient members
			if (member.isTransient())
				continue;
            
            //-- skip inherited fields
			if (base != null
				&& base.getAttributeField(member.getNodeName()) != null)
            {
				continue;
            }
            
			createFieldMapping(classMapping, member, nsURI);
		}

		//---------------------------/
		//- Create element mappings -/
		//---------------------------/
        
		FieldInfo[] elements = classInfo.getElementFields();


		for (int i = 0; i < elements.length; i++) {
			FieldInfo member = elements[i];
			//-- skip transient members
			if (member.isTransient())
				continue;
            
            //-- skip inherited fields
			if (base != null
				&& base.getElementField(member.getNodeName()) != null)
            {
				continue;
            }
            
			createFieldMapping(classMapping, member, nsURI);
		}

		return classMapping;

	} //-- createClassMapping

	//-------------------/
	//- Private Methods -/
	//-------------------/

	/**
	 * Create a FieldMapping for a given member and adds it to
     * the given ClassMapping
     * 
	 */
	private void createFieldMapping
	    (ClassMapping classMapping,
	     FieldInfo member,
	     String nsURI) 
    {

        
		XSType xsType = member.getSchemaType();
		boolean any = false;
		boolean isAttribute = (member.getNodeType() == XMLInfo.ATTRIBUTE_TYPE);
		boolean isText = (member.getNodeType() == XMLInfo.TEXT_TYPE);

		//-- a hack, I know, I will change later (kv)
		if (member.getName().equals("_anyObject"))
			any = true;

		if (xsType.getType() == XSType.COLLECTION)
			//Attributes can handle COLLECTION type for NMTOKENS or IDREFS for instance
			xsType = ((CollectionInfo) member).getContent().getSchemaType();

        //-- create class choice on demand
        ClassChoice classChoice = classMapping.getClassChoice();
        if (classChoice == null) { classChoice = new ClassChoice(); }
        
		//-- create field mapping
        FieldMapping fieldMap = new FieldMapping();
        classChoice.addFieldMapping(fieldMap);
        String fieldName = member.getName();
        if (fieldName.charAt(0) == '_') {
            fieldName = fieldName.substring(1);
        }
        fieldMap.setName(fieldName);
        fieldMap.setType(getClassName(xsType.getJType()));
        
        BindXml bindXml = new BindXml();
        fieldMap.setBindXml(bindXml);

        String nodeName = member.getNodeName();
        if ((nodeName != null) && (!isText)) {
            bindXml.setName(nodeName);
        }
        
		if (isAttribute) {
            bindXml.setNode(BindXmlNodeType.ATTRIBUTE);
        }
		else if (isText) {
            bindXml.setNode(BindXmlNodeType.TEXT);
        }
        else {
            bindXml.setNode(BindXmlNodeType.ELEMENT);
        }
        
		switch (xsType.getType()) {
			case XSType.IDREF_TYPE :
                bindXml.setReference(true);
				break;
			case XSType.ID_TYPE :
                classMapping.addIdentity(member.getName());
				break;
			case XSType.QNAME_TYPE :
                bindXml.setType("QName");
			default :
				break;
		} 

        //-- set any user-specified field handler
        fieldMap.setHandler(member.getXMLFieldHandler());
        
		//-- container
		if (member.isContainer()) {
            fieldMap.setContainer(true);
		}

		//-- Handle namespaces
		//-- FieldInfo namespace has higher priority than ClassInfo
		//-- namespace.
        //-- TODO: We need to add better namespace support to
        //-- the bind-xml element, it's not very good at the
        //-- moment
        /*
		nsURI = member.getNamespaceURI();
		if (nsURI != null) {
			jsc.add("desc.setNameSpaceURI(\"");
			jsc.append(nsURI);
			jsc.append("\");");
		}
        
        if (any && member.getNamespaceURI() == null) {
            nsURI = null;
        }
        */
		
        //-- required
		if (member.isRequired()) {
            fieldMap.setRequired(true);
		}
        
        //-- nillable
        if (member.isNillable()) {
            // TODO: Mapping file needs nillable support!
        }
        
		//-- if any it can match all the names
		if (any) {
            bindXml.setMatches("*");
        }
        
		//-- Add Validation Code
        // TODO: mapping file has no validation support,
        // users will need to use xsi:schemaLocation in 
        // their XML instances and enable schema validation
        // on the parser

	} //-- createFieldMapping

	/**
	 * Returns the classname for the given XSType, the class
     * name may be the fully qualified classname or the 
     * mapping loader "short" name. 
	 */
	private static String getClassName(JType jType) {
        
        //-- XXX: Look up short names from:
        //--  org.exolab.castor.mapping.loader.Types
        
		if (jType.isPrimitive()) {
			if (jType == JType.Int)
				return "integer";
			else if (jType == JType.Double)
				return "double";
			else if (jType == JType.Boolean) {
				return "boolean";
			}
		}
		return jType.toString();
	} //-- classType

} //-- DescriptorSourceFactory

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

import org.exolab.castor.xml.schema.*;
import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.javasource.*;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class holds the necessary information
 * so that the source generator can properly create 
 * the necessary Classes for the Object model.
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ClassInfo  {
    
    
    private Vector    elements       = null;
    private Vector    atts           = null;
    
    private String packageName = null;
    private String elementName = null;
    private String className = null;
    
    private String nsPrefix  = null;
    private String nsURI     = null;
    
    private boolean _allowTextContent = false;
    
    /**
     * The XML Schema type for this ClassInfo
    **/
    private XSType dataType = null;
    
    /**
     * Creates a new ClassInfo for the given XML Schema element declaration
     * @param element the XML Schema element declaration to create the 
     * ClassInfo for
    **/
    public ClassInfo(ElementDecl element) 
    {
        this.elementName = element.getName();
        this.nsPrefix    = element.getSchemaAbbrev();
        this.nsURI       = element.getSchemaName();
        
        className = JavaXMLNaming.toJavaClassName(elementName);
        
        Archetype archetype = element.getArchetype();
        
        String dataTypeName = null;
        
        if (archetype != null) {
            
            dataTypeName = archetype.getName();
            
            if (dataTypeName == null) 
                dataTypeName = className;
                
            atts     = new Vector(3);
            elements = new Vector(5);
            
            //---------------------/
            //- handle attributes -/
            //---------------------/
            //-- loop throug each attribute
            Enumeration enum = archetype.getAttributeDecls();
            while (enum.hasMoreElements()) {
                processAttribute((AttributeDecl)enum.nextElement());
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
                _allowTextContent = true;
                
                if (contentType == ContentType.any) {
                    addMember(MemberFactory.createMemberForAny());
                }
                
            }
            process(archetype);
        }
        else {
            dataTypeName = element.getTypeRef();
        }
        dataType = TypeConversion.createXSType(dataTypeName);
        //if (dataType.isPrimitive()) _allowTextContent = true;
        
    } //-- ClassInfo
    
    /**
     * Creates a new ClassInfo for the given XML Schema element declaration
     * @param element the XML Schema element declaration to create the 
     * ClassInfo for
     * @param packageName the package to use when generating source
     * from this ClassInfo
    **/
    public ClassInfo(ElementDecl element, String packageName) 
    {
        this(element);
        this.packageName = packageName;
    } //-- ClassInfo
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Adds the given SGMember to this ClassInfo
     * @param member the SGMember to add
    **/
    public void addMember(SGMember member) {
        if (member.getFromType() == SGMember.ATTRIBUTE) {
            if (atts == null) atts = new Vector(3);
            atts.addElement(member);
        }
        else {
            if (elements == null) elements = new Vector(5);
            elements.addElement(member);
        }
    } //-- addMember
    
    /**
     * @return true if Classes created with this ClassInfo allow
     * text content
    **/
    public boolean allowsTextContent() {
        return this._allowTextContent;
    } //-- allowsTextContent
    
    /**
     * @return an array of attribute members
    **/
    public SGMember[] getAttributeMembers() {
        SGMember[] members = null;
        if (atts != null) {
            members = new SGMember[atts.size()];
            atts.copyInto(members);
        }
        else members = new SGMember[0];
        return members;
    } //-- getAttributeMembers
    
    /**
     * Returns the class name for this ClassInfo
     * @return the class name that should be used when creating classes
     * from this ClassInfo
    **/
    public String getClassName() {
        return this.className;
    } //-- getClassName
    
    /**
     * Returns the XML Schema data type for this ClassInfo
     * @return the XML Schema data type for this ClassInfo
    **/
    public XSType getDataType() {
        return dataType;
    } //-- XSType
    
    /**
     * Returns the XML element name
     * @return the XML element name
    **/
    public String getElementName() {
        return this.elementName;
    } //-- getElementName
    
    /**
     * @return an array of element members
    **/
    public SGMember[] getElementMembers() {
        SGMember[] members = null;
        if (elements != null) {
            members = new SGMember[elements.size()];
            elements.copyInto(members);
        }
        else members = new SGMember[0];
        return members;
    } //-- getElementMembers
    
    /**
     * @return the namespace prefix to use when marshalling as XML.
    **/
    public String getNameSpacePrefix() {
        return nsPrefix;
    } //-- getNameSpacePrefix
    
    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
    **/
    public String getNameSpaceURI() {
        return nsURI;
    } //-- getNameSpaceURI
    
    /**
     * Returns the package name for this ClassInfo
     * @return the package name that should be used when creating classes
     * from this ClassInfo
    **/
    public String getPackageName() {
        return this.packageName;
    } //-- getPackageName
    
    /**
     * Sets the class name for this SGClass
     * @param className the name to use for class name
     * @exception IllegalArgumentException when the given className is not
     * a valid Java Class Name
    **/
    public void setClassName(String className) {
        this.className = className;
    } //-- setClassName
    
    /**
     * Sets the package name for this ClassInfo
     * @param packageName the package name to use for this ClassInfo
    **/
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    } //-- setPackageName
    
    //-------------------/        
    //- Private Methods -/        
    //-------------------/        
        
    /**
     * Processes the given XML attribute declaration and adds
     * an SGMember to this ClassInfo for the declaration
     * @param attribute the XML attribute declaration to process
    **/
    private void processAttribute(AttributeDecl attribute) {
        
        String typeRef = attribute.getDataTypeRef();
        
        String memberName = "v"+
            JavaXMLNaming.toJavaClassName(attribute.getName());
            
        SGMember member = null;
                
        //-- handle built-in types
                
        if (typeRef.equals("integer")) {
            XSInteger xsInteger = new XSInteger();
            member = new SGAttrMember(xsInteger, memberName);
            member.setCodeHelper(new IntegerCodeHelper(xsInteger));
            //-- handle integer related facets
            DataType dataType = attribute.getDataType();
            NumberFacet facet = null;
                    
            //-- maxExclusive
            facet = (NumberFacet) dataType.getFacet(Facet.MAX_EXCLUSIVE);
            if (facet != null) xsInteger.setMaxExclusive(facet.toInt());
                    
            //-- maxInclusive
            facet = (NumberFacet) dataType.getFacet(Facet.MAX_INCLUSIVE);
            if (facet != null) xsInteger.setMaxInclusive(facet.toInt());
                    
            //-- minExclusive
            facet = (NumberFacet) dataType.getFacet(Facet.MIN_EXCLUSIVE);
            if (facet != null) xsInteger.setMinExclusive(facet.toInt());
                    
            //-- minInclusive
            facet = (NumberFacet) dataType.getFacet(Facet.MIN_INCLUSIVE);
            if (facet != null) xsInteger.setMinInclusive(facet.toInt());
                    
        }
        else if (typeRef.equals("ID")) {
            member = new SGId(memberName);
        }
        else if (typeRef.equals("IDREF")) {
            member = new SGIdRef(memberName);
        }
        else {
            XSType xsType = TypeConversion.createXSType(typeRef);
            member = new SGAttrMember(xsType, memberName);
        }
        member.setSchemaType(typeRef);
        member.setXMLName(attribute.getName());
        member.setRequired(attribute.getRequired());
        atts.addElement(member);
    } //-- processAttributes
    
    /**
     * Processes the given ContentModelGroup into a set of members
     * for this ClassInfo
     * @param contentModel the ContentModelGroup to process
    **/
    private void process(ContentModelGroup contentModel) {
        
        //------------------------------/
        //- handle elements and groups -/
        //------------------------------/
                
        Enumeration enum = contentModel.enumerate();
                
        SGMember member = null;
        while (enum.hasMoreElements()) {
                    
            SchemaBase base = (SchemaBase)enum.nextElement();
            switch(base.getDefType()) {
                case SchemaBase.ELEMENT:
                    member = MemberFactory.createMember((ElementDecl)base);
                    addMember(member);
                    break;
                case SchemaBase.GROUP:
                    process((Group)base);
                    break;
                default:
                    break;
            }
        }
            
    }
    
} //-- ClassInfo

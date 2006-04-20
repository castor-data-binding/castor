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

import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaXMLNaming;
import org.exolab.javasource.*;

import java.util.Vector;

/**
 * A class for representing Members of a Class. SGMember objects
 * hold all the information required about a member in order
 * to be able to produce marshal/unmarshal and validation code.
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class SGMember {
 
    public static final short ATTRIBUTE = 0;
    public static final short ELEMENT   = 1;
    public static final short PCDATA    = 2;
    
    
    private boolean isRequired = false;
    
    private String  xmlName   = null;
    
    private String  typeRef   = null;
    
    private XSType type = null;
    
    /**
     * The Java Name for this Member
    **/
    private String name = null;
    
    private String nameSpaceURI = null;
    
    private String nameSpacePrefix = null;
    
    
    private CodeHelper codeHelper = null;
    
    private boolean _multivalued = false;
    
    /**
     * Creates a new SGMember with the given XML Schema type
     * and the given member name
     * @param XSType the XML Schema type of this member
     * @param name the name of the member
    **/
    public SGMember(XSType type, String name) {
        this.name = name;
        this.type = type;
    } //-- SGMember

    //--------------------/
    //- Abstract Methods -/
    //--------------------/
    
    /**
     * Returns which type of XML node this SGMember came from.
    **/
    public abstract short getFromType();
           
    //------------------/
    //- Public Methods -/
    //------------------/
    
    /**
     * Creates a JMember for this SGMember
     * @return a JMember which has the appropriate type and name
     * for this SGMember
    **/
    public JMember createMember() {
        return new JMember(type.getJType(), name);
    } //-- createMember
    
    
    /**
     * Creates the access methods for this SGMember
     * @return the set of access methods needed for
     * this member
    **/
    public JMethod[] createAccessMethods() {
        
        JMethod[] methods = new JMethod[2];
        JSourceCode jsc   = null;
        
        String mname = this.name.substring(1);
        JType jType  = this.type.getJType();
        
        //-- create get method
        methods[0] = new JMethod(jType, "get"+mname);
        jsc = methods[0].getSourceCode();
        jsc.add("return this.");
        jsc.append(this.name);
        jsc.append(";");
        
        //-- create set method
        methods[1] = new JMethod(null, "set"+mname);
        methods[1].addParameter(new JParameter(jType, this.name));
        jsc = methods[1].getSourceCode();
        jsc.add("this.");
        jsc.append(getName());
        jsc.append(" = ");
        jsc.append(getName());
        jsc.append(";");
        
        return methods;
        
    } //-- createAccessMethods
    
    
    public CodeHelper getCodeHelper() {
        return codeHelper;
    } //-- getCodeHelper
    
    
    public String getReadMethodName() {
        StringBuffer sb = new StringBuffer("get");
        sb.append(this.name.substring(1));
        return sb.toString();
    } //-- getReadMethodName
    
    
    public String getWriteMethodName() {
        StringBuffer sb = new StringBuffer();
        if (isMultivalued()) sb.append("add");
        else sb.append("set");
        sb.append(this.name.substring(1));
        return sb.toString();
    } //-- getWriteMethodName
    
    
    /**
     * Creates code for initialization of this Member
     * @param jsc the JSourceCode in which to add the source to
    **/
    public void generateInitializerCode(JSourceCode jsc) {
        //-- do nothing by default
    } //-- generateConstructorCode
    
    
        
    /**
     * Returns the name of this SGMember
     * @return the name of this SGMember
    **/
    public String getName() {
        return this.name;
    } //-- getName
    
    /**
     * Returns the namespace prefix (or Schema abbreviation) for this member
     * @return the namespace prefix (or Schema abbreviation) for this member
    **/
    public String getNameSpacePrefix() {
        return this.nameSpacePrefix;
    } //-- getNameSpacePrefix
    
    /**
     * Returns the namespace URI (or Schema name) for this member
     * @return the namespace URI (or Schema name) for this member
    **/
    public String getNameSpaceURI() {
        return nameSpaceURI;
    } //-- getNameSpaceURI
    
    /**
     * Returns true if the SGMember is required
     * @return true if this SGMember is required, otherwise false is returned.
    **/
    public boolean getRequired() {
        return isRequired;
    } //-- getRequired
    
    /**
     * Gets the String value representing the XML Schema type
    **/
    public String getSchemaType() {
        return this.typeRef;
    } //-- getSchemaType
    
    public String getXMLName() {
        return xmlName;
    } //-- getXMLName
    
    public XSType getXSType() {
        return this.type;
    } //-- getXSType
    
    /**
     * Return whether or not this member is a multi-valued member or not
     * @return true if this member can appear more than once
    **/
    public boolean isMultivalued() {
        return _multivalued;
    } //-- isMultivalued
    
    /**
     * Sets the CodeHelper to use when creating source code
     * @param codeHelper the CodeHelper to use when creating source code
     * @see SGMember
    **/
    public void setCodeHelper(CodeHelper codeHelper) {
        this.codeHelper = codeHelper;
    } //-- setCodeHelper
    
    
    /**
     * Sets whether this member represents an element that
     * allows multiple values, such as an array
     * or Vector
     * @param multivalued the boolean indicating whether or not this member
     * is multi-valued.
    **/
    public void setMultivalued(boolean multivalued) {
        this._multivalued = multivalued;
    } //-- setMultivalued
    
    /**
     * Sets the namespace prefix (or Schema abbreviation)
     * @param nameSpacePrefix the namespace prefix for this member
    **/
    public void setNameSpacePrefix(String nameSpacePrefix) {
        this.nameSpacePrefix = nameSpacePrefix;
    } //-- setNameSpacePrefix
    
    /**
     * Sets the namespace URI (or Schema name) for this member
     * @param nameSpaceURI the namespace URI for this member
    **/
    public void setNameSpaceURI(String nameSpaceURI) {
        this.nameSpaceURI = nameSpaceURI;
    } //-- setNameSpaceURI
    
    public void setRequired(boolean required) {
        this.isRequired = required;
    } //-- setRequired
    
    public void setSchemaType(String schemaType) {
        this.typeRef = schemaType;
    }
    
    public void setXMLName(String xmlName) {
        this.xmlName = xmlName;
    } //-- setXMLName
    
} //-- SGMember


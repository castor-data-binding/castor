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
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SGList extends SGElementMember {
 
        
    private XSList xsList      = null;
    private String contentName = null;
    private XSType contentType = null;
    
    private SGMember content   = null;
    private String elementName;
    
    public SGList(XSType contentType, String name, String elementName) {
        
        super(new XSList(contentType), name);
        xsList = (XSList) getXSType();
        this.contentType = contentType;
        this.contentName = "v" + JavaXMLNaming.toJavaClassName(elementName);
        this.elementName = elementName;
        content = new SGElementMember(contentType, contentName);
    } //-- SGList
    
    /**
     * Creates code for initialization of this Member
     * @param jsc the JSourceCode in which to add the source to
    **/
    public void generateInitializerCode(JSourceCode jsc) {
        jsc.add(getName());
        jsc.append(" = new Vector();");
    } //-- generateConstructorCode
        
    public String getReadMethodName() {
        StringBuffer sb = new StringBuffer("get");
        sb.append(JavaXMLNaming.toJavaClassName(elementName));
        return sb.toString();
    } //-- getReadMethodName
    
    public String getWriteMethodName() {
        StringBuffer sb = new StringBuffer();
        sb.append("add");
        sb.append(JavaXMLNaming.toJavaClassName(elementName));
        return sb.toString();
    } //-- getWriteMethodName
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    public JMethod[] createAccessMethods() {
        
        JMethod[] methods = new JMethod[6];
        JParameter contentParam 
            = new JParameter(contentType.getJType(), contentName);
        JSourceCode jsc = null;
        
        int mc = 0;
        
          //---------------------/
         //- Create add method -/
        //---------------------/
        
        String cName = JavaXMLNaming.toJavaClassName(elementName);
        
        methods[mc] = new JMethod(null, "add"+cName);
        methods[mc].addException(SGTypes.IndexOutOfBoundsException);
        methods[mc].addParameter(contentParam);
                    
        jsc = methods[mc].getSourceCode();
        int maxSize = xsList.getMaximumSize();
        if (maxSize > 0) {
            jsc.add("if (!(");
            jsc.append(getName());
            jsc.append(".size() < ");
            jsc.append(Integer.toString(maxSize));
            jsc.append(")) {");
            jsc.indent();
            jsc.add("throw new IndexOutOfBoundsException();");
            jsc.unindent();
            jsc.add("}");
        }
        jsc.add(getName());
        jsc.append(".addElement(");
        jsc.append(contentType.createToJavaObjectCode(contentName));
        jsc.append(");");
                    

          //---------------------/
         //- Create get method -/
        //---------------------/
        
        ++mc;
        
        JType jType = contentType.getJType();
        methods[mc] = new JMethod(jType, "get"+cName);
        methods[mc].addException(SGTypes.IndexOutOfBoundsException);
        methods[mc].addParameter(new JParameter(JType.Int, "index"));
                    
        jsc = methods[mc].getSourceCode();
                    
        jsc.add("//-- check bounds for index");
        jsc.add("if ((index < 0) || (index > ");
        jsc.append(getName());
        jsc.append(".size())) {");
        jsc.indent();
        jsc.add("throw new IndexOutOfBoundsException();");
        jsc.unindent();
        jsc.add("}");
        
        jsc.add("");
        jsc.add("return ");
        
        String variableName = getName()+".elementAt(index)";
        
        if (contentType.getType() == XSType.CLASS) {
            jsc.append("(");
            jsc.append(jType.getName());
            jsc.append(") ");
            jsc.append(variableName);
        }
        else {
            jsc.append(contentType.createFromJavaObjectCode(variableName));
        }
        jsc.append(";");
        
          //-----------------------/
         //- Create get[] method -/
        //-----------------------/

        ++mc;
        
        jType = jType.createArray();
        methods[mc] = new JMethod(jType, "get"+cName);
        jsc = methods[mc].getSourceCode();
                    
        jsc.add("int size = ");
        jsc.append(getName());
        jsc.append(".size();");
        
        jsc.add(jType.getLocalName());
        jsc.append("[] mArray = new ");
        jsc.append(jType.getLocalName());
        jsc.append("[size];");
        jsc.add("for (int index = 0; index < size; index++) {");
        jsc.indent();
        jsc.add("mArray[index] = ");
        if (contentType.getType() == XSType.CLASS) {
            jsc.append("(");
            jsc.append(jType.getLocalName());
            jsc.append(") ");
            jsc.append(variableName);
        }
        else {
            jsc.append(contentType.createFromJavaObjectCode(variableName));
        }
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return mArray;");
        
          //---------------------/
         //- Create set method -/
        //---------------------/
        
        ++mc;
        
        methods[mc] = new JMethod(null, "set"+cName);
        methods[mc].addException(SGTypes.IndexOutOfBoundsException);
        methods[mc].addParameter(contentParam);
        methods[mc].addParameter(new JParameter(JType.Int, "index"));
                    
        jsc = methods[mc].getSourceCode();
        
        jsc.add("//-- check bounds for index");
        jsc.add("if ((index < 0) || (index > ");
        jsc.append(getName());
        jsc.append(".size())) {");
        jsc.indent();
        jsc.add("throw new IndexOutOfBoundsException();");
        jsc.unindent();
        jsc.add("}");
        
        if (maxSize != 0) {
            jsc.add("if (!(");
            jsc.append(getName());
            jsc.append(".size() < ");
            jsc.append(Integer.toString(maxSize));
            jsc.append(")) {");
            jsc.indent();
            jsc.add("throw new IndexOutOfBoundsException();");
            jsc.unindent();
            jsc.add("}");
        }
        jsc.add(getName());
        jsc.append(".setElementAt(");
        jsc.append(contentType.createToJavaObjectCode(contentName));
        jsc.append(", index);");
        
        
          //--------------------------/
         //- Create getCount method -/
        //--------------------------/
        
        ++mc;
        methods[mc] = new JMethod(JType.Int, "get"+cName+"Count");
        jsc = methods[mc].getSourceCode();
        jsc.add("return ");
        jsc.append(getName());
        jsc.append(".size();");
        
        
          //---------------------------/
         //- Create Enumerate Method -/
        //---------------------------/
        
        ++mc;
        methods[mc] = new JMethod(SGTypes.Enumeration, "enumerate"+cName);
        jsc = methods[mc].getSourceCode();
        jsc.add("return ");
        jsc.append(getName());
        jsc.append(".elements();");
        
        return methods;
        
    } //-- createAccessMethods
    
    /**
     * Returns the main read method for this member
     * @return the main read method for this member
    **/
    public JMethod getReadMethod() {
        
        String methodName = getReadMethodName();
        
        JType jType  = contentType.getJType();
        
        //-- create get method
        JMethod jMethod = new JMethod(jType, methodName);
        JSourceCode jsc = jMethod.getSourceCode();
        jsc.add("return this.");
        jsc.append(getName());
        jsc.append(";");
        
        return jMethod;
    } //-- getReadMethod
    
    public XSList getXSList() {
        return xsList;
    }
    
    public SGMember getContent() {
        return content;
    }
    
    /**
     * Return whether or not this member is a multivalued member or not
     * @return true if this member can appear more than once
    **/
    public boolean isMultivalued() {
        return true;
    }
    
    public void setXMLName(String xmlName) {
        super.setXMLName(xmlName);
        content.setXMLName(xmlName);
    } //-- setXMLName
    
    
} //-- SGList


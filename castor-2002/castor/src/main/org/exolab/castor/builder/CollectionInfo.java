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
 * Copyright 1999,2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * Contribution(s):
 *
 * - Frank Thelen, frank.thelen@poet.de
 *     - Moved creation of access methods into an appropriate
 *       set of separate methods, for extensibility
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.javasource.*;

import java.util.Vector;

/**
 * A helper used for generating source that deals with Collections
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class CollectionInfo extends FieldInfo {


    private XSList xsList      = null;
    private String contentName = null;
    private XSType contentType = null;

    private FieldInfo content   = null;
    private String elementName;

    /**
     * Creates a new CollectionInfo
     * @param contextType the content type of the collection, ie. the type
     * of objects that the collection will contain
     * @param name the name of the Collection
     * @param elementName the element name for each element in collection
    **/
    public CollectionInfo(XSType contentType, String name, String elementName)
    {
        super(new XSList(contentType), name);
        xsList = (XSList) getSchemaType();
        this.contentType = contentType;
        this.contentName = "v" + JavaNaming.toJavaClassName(elementName);
        this.elementName = elementName;
        content = new FieldInfo(contentType, contentName);
    } //-- CollectionInfo

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
        sb.append(JavaNaming.toJavaClassName(getElementName()));
        // fix for avoiding compilation conflict sb.append("List");
        return sb.toString();
    } //-- getReadMethodName

    public String getWriteMethodName() {
        StringBuffer sb = new StringBuffer();
        sb.append("add");
        sb.append(JavaNaming.toJavaClassName(getElementName()));
        return sb.toString();
    } //-- getWriteMethodName

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Creates the Access methods for the collection described
     * by this CollectionInfo
     *
     * @param jClass the JClass to add the methods to.
    **/
    public void createAccessMethods(JClass jClass) {


        JMethod method = null;

        JParameter contentParam
            = new JParameter(getContentType().getJType(), getContentName());

        JSourceCode jsc = null;

          //---------------------/
         //- Create add method -/
        //---------------------/

        String cName = JavaNaming.toJavaClassName(getElementName());

        method = new JMethod(null, "add"+cName);
        jClass.addMethod(method);

        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(contentParam);

        createAddMethod(method);


          //---------------------/
         //- Create get method -/
        //---------------------/


        JType jType = getContentType().getJType();
        method = new JMethod(jType, "get"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(new JParameter(JType.Int, "index"));

        createGetByIndexMethod(method);


          //-----------------------/
         //- Create get[] method -/
        //-----------------------/

        jType = jType.createArray();
        method = new JMethod(jType, "get"+cName);// fix for avoiding compilation conflict +"List");
        jClass.addMethod(method);

        createGetMethod(method);


          //----------------------/
         //- Create set methods -/
        //----------------------/

        method = new JMethod(null, "set"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(contentParam);
        method.addParameter(new JParameter(JType.Int, "index"));

        createSetByIndexMethod(method);

        //-- array setter
        JType arrayType = contentParam.getType().createArray();
        String pName = JavaNaming.toJavaMemberName(cName);
        JParameter arrayParam = new JParameter(arrayType, pName+"Array");
        method = new JMethod(null, "set"+cName);
        method.addParameter(arrayParam);
        jClass.addMethod(method);

        createSetArrayMethod(method);

          //--------------------------/
         //- Create getCount method -/
        //--------------------------/

        method = new JMethod(JType.Int, "get"+cName+"Count");
        jClass.addMethod(method);

        createGetCountMethod(method);


          //---------------------------/
         //- Create Enumerate Method -/
        //---------------------------/

        method = new JMethod(SGTypes.Enumeration, "enumerate"+cName);
        jClass.addMethod(method);

        createEnumerateMethod(method);


          //--------------------------------/
         //- Create remove(Object) Method -/
        //--------------------------------/

        //-- commented out until I fix primitives
        //method = new JMethod(JType.Boolean, "remove"+cName);
        //methods.addElement(method);
        //method.addParameter(contentParam);

        //createRemoveByObjectMethod_Impl(method);


          //--------------------------------/
         //- Create remove(int i) Method -/
        //--------------------------------/

        jType = getContentType().getJType();
        method = new JMethod(jType, "remove"+cName);
        jClass.addMethod(method);
        method.addParameter(new JParameter(JType.Int, "index"));

        createRemoveByIndexMethod(method);


          //-----------------------------/
         //- Create removeAll() Method -/
        //-----------------------------/

        method = new JMethod(null, "removeAll"+cName);
        jClass.addMethod(method);

        createRemoveAllMethod(method);

    } //-- createAccessMethods

    /**
     * Returns the main read method for this member
     * @return the main read method for this member
    **/
    public JMethod getReadMethod() {

        String methodName = getReadMethodName();

        JType jType  = getContentType().getJType();

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

    public String getContentName() {
        return contentName;
    }

    public XSType getContentType() {
        return contentType;
    }

    public FieldInfo getContent() {
        return content;
    }

    public String getElementName() {
        return elementName;
    }

    /**
     * Return whether or not this member is a multivalued member or not
     * @return true if this member can appear more than once
    **/
    public boolean isMultivalued() {
        return true;
    }

    /**
     * Creates implementation of add method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createAddMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        int maxSize = getXSList().getMaximumSize();
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
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createAddMethod


    /**
     * Creates implementation of object[] get() method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createGetMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

        jsc.add("int size = ");
        jsc.append(getName());
        jsc.append(".size();");

        String variableName = getName()+".elementAt(index)";

        JType compType = jType.getComponentType();

        jsc.add(compType.toString());
        jsc.append("[] mArray = new ");
        jsc.append(compType.getLocalName());
        jsc.append("[size]");
        //-- if component is an array, we must add [] after setting
        //-- size
        if (compType.isArray()) jsc.append("[]");
        jsc.append(";");

        jsc.add("for (int index = 0; index < size; index++) {");
        jsc.indent();
        jsc.add("mArray[index] = ");
        if (getContentType().getType() == XSType.CLASS) {
            jsc.append("(");
            jsc.append(jType.getLocalName());
            jsc.append(") ");
            jsc.append(variableName);
        }
        else {
            jsc.append(getContentType().createFromJavaObjectCode(variableName));
        }
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return mArray;");
    } //-- createGetMethod

    /**
     * Creates implementation of the get(index) method.
    **/
    public void createGetByIndexMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

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

        if (getContentType().getType() == XSType.CLASS) {
            jsc.append("(");
            jsc.append(jType.toString());
            jsc.append(") ");
            jsc.append(variableName);
        }
        else {
            jsc.append(getContentType().createFromJavaObjectCode(variableName));
        }
        jsc.append(";");
    } //-- createGetByIndex

    /**
     * Creates implementation of array set method
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createSetArrayMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        String paramName = method.getParameter(0).getName();

        String index = "i";
        if (paramName.equals(index)) index = "j";

        jsc.add("//-- copy array");
        jsc.add(getName());
        jsc.append(".removeAllElements();");
        jsc.add("for (int ");
        jsc.append(index);
        jsc.append(" = 0; ");
        jsc.append(index);
        jsc.append(" < ");
        jsc.append(paramName);
        jsc.append(".length; ");
        jsc.append(index);
        jsc.append("++) {");
        jsc.indent();
        jsc.add(getName());
        jsc.append(".addElement(");
		jsc.append(getContentType().createToJavaObjectCode(paramName+'['+index+']'));
        jsc.append(");");
        jsc.unindent();
        jsc.add("}");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createSetArrayMethod

    /**
     * Creates implementation of set method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createSetByIndexMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        jsc.add("//-- check bounds for index");
        jsc.add("if ((index < 0) || (index > ");
        jsc.append(getName());
        jsc.append(".size())) {");
        jsc.indent();
        jsc.add("throw new IndexOutOfBoundsException();");
        jsc.unindent();
        jsc.add("}");

        int maxSize = getXSList().getMaximumSize();
        if (maxSize > 0) {
            jsc.add("if (!(");
            jsc.append("index < ");
            jsc.append(Integer.toString(maxSize));
            jsc.append(")) {");
            jsc.indent();
            jsc.add("throw new IndexOutOfBoundsException();");
            jsc.unindent();
            jsc.add("}");
        }
        jsc.add(getName());
        jsc.append(".setElementAt(");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(", index);");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createSetMethod

    /**
     * Creates the necessary source code for notifying
     * PropertyChangeListeners when the collection has
     * been updated.
     *
     * @param jsc the JSourceCode to add the new source code to.
    **/
    protected void createBoundPropertyCode(JSourceCode jsc) {
        //notify listeners
        jsc.add("notifyPropertyChangeListeners(\"");
        jsc.append(getName());
        jsc.append("\", null, ");
        jsc.append(getName());
        jsc.append(");");
    } //-- createBoundPropertyCode

    /**
     * Creates implementation of getCount method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createGetCountMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        jsc.add("return ");
        jsc.append(getName());
        jsc.append(".size();");
    } //-- createGetCoundMethod

    /**
     * Creates implementation of Enumerate method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createEnumerateMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        jsc.add("return ");
        jsc.append(getName());
        jsc.append(".elements();");

    } //-- createEnumerateMethod

    /**
     * Creates implementation of remove(Object) method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createRemoveByObjectMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        jsc.add("boolean removed = ");
        jsc.append(getName());
        jsc.append(".removeElement(");
        jsc.append(getContentName());
        jsc.append(");");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

        //-- return value
        jsc.add("return removed;");

    } //-- createRemoveByObjectMethod

    /**
     * Creates implementation of remove(int i) method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createRemoveByIndexMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

        jsc.add("Object obj = ");
        jsc.append(getName());
        jsc.append(".elementAt(index);");
        jsc.add(getName());
        jsc.append(".removeElementAt(index);");


        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

        //-- return value
        jsc.add("return ");
        if (getContentType().getType() == XSType.CLASS) {
            jsc.append("(");
            jsc.append(jType.getName());
            jsc.append(") obj;");
        }
        else {
            jsc.append(getContentType().createFromJavaObjectCode("obj"));
            jsc.append(";");
        }
    } //-- createRemoveByIndexMethod

    /**
     * Creates implementation of removeAll() method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createRemoveAllMethod (JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        jsc.add(getName());
        jsc.append(".removeAllElements();");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createRemoveAllMethod


} //-- CollectionInfo


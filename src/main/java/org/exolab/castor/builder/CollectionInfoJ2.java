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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * Contribution(s):
 *
 * - Frank Thelen, frank.thelen@poet.de
 *     - initial contributor
 *
 * $Id$
 */

package org.exolab.castor.builder;


import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.javasource.*;


/**
 * A helper used for generating source that deals with Java 2 Collections.
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
**/
public class CollectionInfoJ2 extends CollectionInfo {

    /**
     * @param contentType the content type of the collection, ie. the type
     * of objects that the collection will contain
     * @param name the name of the Collection
     * @param elementName the element name for each element in collection
    **/
    public CollectionInfoJ2(XSType contentType, String name, String elementName)
    {
        super(contentType, name, elementName);
        //--override the schemaType
        setSchemaType(new XSListJ2(contentType));
        //--override the internal list
        xsList = (XSListJ2)getSchemaType();
    } //-- CollectionInfoJ2

    /**
     * Creates code for initialization of this Member
     * @param jsc the JSourceCode in which to add the source to
    **/
    public void generateInitializerCode(JSourceCode jsc) {
        jsc.add(getName());
        jsc.append(" = new ArrayList();");
    } //-- generateConstructorCode

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

        JType jType = getContentType().getJType();

        JParameter contentParam = new JParameter(jType, getContentName());
        

        JSourceCode jsc = null;

        String cName = JavaNaming.toJavaClassName(getElementName());

        //-- add{Name}(Object)
        method = new JMethod(null, "add"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(contentParam);
        createAddMethod(method);

        //-- add{Name}(int index, Object)
        method = new JMethod(null, "add"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
		method.addParameter(new JParameter(JType.Int, "index"));
        method.addParameter(contentParam);
        createAddInsertMethod(method);

        //-- {type} get{Name}(int index)
        method = new JMethod(jType, "get"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(new JParameter(JType.Int, "index"));
        createGetByIndexMethod(method);

        //-- {type}[] get{Name}
        method = new JMethod(jType.createArray(), "get"+cName);
        jClass.addMethod(method);
        createGetMethod(method);
        
        if (extraMethods()) {
            //-- Reference getter (non type-safe)
            String suffix = getReferenceMethodSuffix();
            method = new JMethod(SGTypes.ArrayList, "get" + cName + suffix);
            jClass.addMethod(method);
            createGetCollectionReferenceMethod(method);
        }
        

          //----------------------/
         //- Create set methods -/
        //----------------------/

        method = new JMethod(null, "set"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(new JParameter(JType.Int, "index"));
        method.addParameter(contentParam);

        createSetByIndexMethod(method);

        //-- array setter
        JType arrayType = contentParam.getType().createArray();
        String pName = JavaNaming.toJavaMemberName(cName);
        JParameter arrayParam = new JParameter(arrayType, pName+"Array");
        method = new JMethod(null, "set"+cName);
        method.addParameter(arrayParam);
        jClass.addMethod(method);

        createSetArrayMethod(method);
        
        if (extraMethods()) {
            //-- Collection setter
            JParameter vParam = new JParameter(SGTypes.ArrayList, pName+"Collection");
            method = new JMethod(null, "set"+cName);
            method.addParameter(vParam);
            jClass.addMethod(method);
            createSetCollectionMethod(method);
            
            //-- Reference setter (non type-safe)
            String suffix = getReferenceMethodSuffix();
            method = new JMethod(null, "set" + cName + suffix);
            method.addParameter(vParam);
            jClass.addMethod(method);
            createSetCollectionReferenceMethod(method);
        }

         //---------------------------/
        //- Create Enumerate Method -/
       //---------------------------/

        method = new JMethod(SGTypes.Enumeration, "enumerate"+cName);
        jClass.addMethod(method);

        createEnumerateMethod(method);


          //-------------------/
         //- getCount method -/
        //-------------------/

        method = new JMethod(JType.Int, "get"+cName+"Count");
        jClass.addMethod(method);

        createGetCountMethod(method);

          //------------------/
         //- Remove Methods -/
        //------------------/

        //-- boolean remove{Name}({type})
        method = new JMethod(JType.Boolean, "remove"+cName);
        method.addParameter(contentParam);
        createRemoveMethod(method);
        jClass.addMethod(method);

        //-- clear{Name}
        method = new JMethod(null, "clear"+cName);
        createClearMethod(method);

        jClass.addMethod(method);
    } //-- createAccessMethods


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
        jsc.append(".add(");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createAddMethod

    /**
     * Creates implementation of add method with index.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createAddInsertMethod(JMethod method) {

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
        jsc.append(".add(index, ");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createAddMethod

    /**
     * Creates implementation of Enumerate method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createEnumerateMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        jsc.add("return new org.exolab.castor.util.IteratorEnumeration(");
        jsc.append(getName());
        jsc.append(".iterator());");

    } //-- createEnumerateMethod

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

        String variableName = getName()+".get(index)";

        JType compType = jType.getComponentType();

        jsc.add(compType.toString());
        jsc.append("[] mArray = new ");
        if (compType.isArray()) {
            jsc.append(compType.getComponentType().toString());
        }
        else jsc.append(compType.toString());
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
            jsc.append(jType.getName());
            jsc.append(") ");
            jsc.append(variableName);
        }
        else {
            jsc.append(getContentType()
                       .createFromJavaObjectCode(variableName));
        }
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return mArray;");
    } //-- createGetMethod

    /**
     * Creates implementation of the get(index) method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createGetByIndexMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

        jsc.add("//-- check bounds for index");
        jsc.add("if ((index < 0) || (index >= ");
        jsc.append(getName());
        jsc.append(".size())) {");
        jsc.indent();
        jsc.add("throw new IndexOutOfBoundsException();");
        jsc.unindent();
        jsc.add("}");

        jsc.add("");
        jsc.add("return ");

        String variableName = getName()+".get(index)";

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
     * Creates implementation of collection reference get method. This 
     * method simply returns the actual reference to the collection.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createGetCollectionReferenceMethod(JMethod method) {

        String cName = JavaNaming.toJavaMemberName(getElementName());
        
        //-- add method description
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Returns a reference to '");
        comment.appendComment(cName);
        comment.appendComment("'.");
        comment.appendComment(" No type checking is performed on any ");
        comment.appendComment("modications to the Collection.");
        JDocDescriptor jDesc = JDocDescriptor.createReturnDesc();
        jDesc.setDescription("returns a reference to the Collection.");
        comment.addDescriptor(jDesc);
        
        //-- create copy code
        JSourceCode jsc = method.getSourceCode();
        
        jsc.add("return ");
        jsc.append(getName() + ';');

    } //-- createGetCollectionReferenceMethod

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
        jsc.append(".clear();");
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
        jsc.append(".add(");
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
        jsc.add("if ((index < 0) || (index >= ");
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
        jsc.append(".set(index, ");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createSetMethod
    
    /**
     * Creates implementation of collection set method. The method
     * will assign the field a copy of the given collection. The
     * fields will be checked for type safety.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createSetCollectionMethod(JMethod method) {

        String cName = JavaNaming.toJavaMemberName(getElementName());
        
        //-- get param name
        String paramName = method.getParameter(0).getName();
        
        //-- add method description
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Sets the value of '");
        comment.appendComment(cName);
        comment.appendComment("' by copying the given ArrayList.");
        JDocDescriptor jDesc = comment.getParamDescriptor(paramName);
        jDesc.setDescription("the ArrayList to copy.");
        
        //-- create copy code
        JSourceCode jsc = method.getSourceCode();

        String index = "i";
        if (paramName.equals(index)) index = "j";

        jsc.add("//-- copy collection");
        jsc.add(getName());
        jsc.append(".clear();");
        jsc.add("for (int ");
        jsc.append(index);
        jsc.append(" = 0; ");
        jsc.append(index);
        jsc.append(" < ");
        jsc.append(paramName);
        jsc.append(".size(); ");
        jsc.append(index);
        jsc.append("++) {");
        jsc.indent();
        jsc.add(getName());
        jsc.append(".add(");
        jsc.append('(' + getContentType().getJType().toString() + ')');
		jsc.append(getContentType().createToJavaObjectCode(paramName+".get("+index+')'));
        jsc.append(");");
        jsc.unindent();
        jsc.add("}");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createSetCollectionMethod

    /**
     * Creates implementation of collection reference set method. This 
     * method is a non-type safe method which simply assigns the 
     * given collection to the field.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createSetCollectionReferenceMethod(JMethod method) {

        String cName = JavaNaming.toJavaMemberName(getElementName());
        
        //-- get param name
        String paramName = method.getParameter(0).getName();
        
        //-- add method description
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Sets the value of '");
        comment.appendComment(cName);
        comment.appendComment("' by setting it to the given ArrayList.");
        comment.appendComment(" No type checking is performed.");
        JDocDescriptor jDesc = comment.getParamDescriptor(paramName);
        jDesc.setDescription("the ArrayList to copy.");
        
        //-- create copy code
        JSourceCode jsc = method.getSourceCode();
        
        jsc.add(getName());
        jsc.append(" = ");
        jsc.append(paramName + ';');

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createSetCollectionReferenceMethod
    

    /**
     * Creates implementation of remove(Object) method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createRemoveMethod(JMethod method) {
        JSourceCode jsc = method.getSourceCode();

        jsc.add("boolean removed = ");
        jsc.append(getName());
        jsc.append(".remove(");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

        //-- return value
        jsc.add("return removed;");

    } //-- createRemoveMethod

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
        jsc.append(".remove(");
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

        jsc.add("java.lang.Object obj = ");
        jsc.append(getName());
        jsc.append(".get(index);");
        jsc.add(getName());
        jsc.append(".remove(index);");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

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
        jsc.append(".clear();");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createRemoveAllMethod

    /**
     * Creates implementation of clear() method.
     *
     * @param method the JMethod in which to create the source
     * code.
    **/
    public void createClearMethod (JMethod method) {
        JSourceCode jsc = method.getSourceCode();
        jsc.add(getName());
        jsc.append(".clear();");

        //-- bound properties
        if (isBound())
            createBoundPropertyCode(jsc);

    } //-- createClearMethod


} //-- CollectionInfoJ2


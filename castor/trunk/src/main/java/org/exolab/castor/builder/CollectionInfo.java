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

/**
 * A helper used for generating source that deals with Collections
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-02-23 01:08:24 -0700 (Thu, 23 Feb 2006) $
**/
public class CollectionInfo extends FieldInfo {

    /**
     * The property used to overwrite the reference suffix for
     * extra collection methods
     */
    public static final String REFERENCE_SUFFIX_PROPERTY
        = "org.exolab.castor.builder.collections.reference.suffix";

    public static final String DEFAULT_REFERENCE_SUFFIX = "AsReference";
    
    protected XSList xsList      = null;
    private String contentName = null;
    private XSType contentType = null;

    private FieldInfo content   = null;
    private String elementName;

    /**
     * A flag indicating that "extra" accessor methods
     * should be created for returning and setting a
     * reference to the underlying collection
     */
    private boolean _extraMethods = false;
    
    /**
     * The reference suffix to use.
     */
    private String _referenceSuffix = DEFAULT_REFERENCE_SUFFIX;
    
    
    /**
     * Creates a new CollectionInfo
     * @param contentType the content type of the collection, ie. the type
     * of objects that the collection will contain
     * @param name the name of the Collection
     * @param elementName the element name for each element in collection
    **/
    public CollectionInfo(XSType contentType, String name, String elementName)
    {
        super(new XSList(contentType), name);
        xsList = (XSList) getSchemaType();
        this.contentType = contentType;
        if (elementName.charAt(0) == '_') {
            elementName = elementName.substring(1);
        }
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
        
        jsc.append(" = new ");
        jsc.append(xsList.getJType().toString());
        jsc.append("();");
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
        
          //----------------------/
         //- Create add methods -/
        //----------------------/

        String cName = JavaNaming.toJavaClassName(getElementName());

        method = new JMethod(null, "add"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(contentParam);
        createAddMethod(method);

        method = new JMethod(null, "add"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
		method.addParameter(new JParameter(JType.Int, "index"));
        method.addParameter(contentParam);
        createAddInsertMethod(method);

          //---------------------/
         //- Create get method -/
        //---------------------/


        //-- get by index 
        
        JType jType = getContentType().getJType();
        method = new JMethod(jType, "get"+cName);
        jClass.addMethod(method);
        method.addException(SGTypes.IndexOutOfBoundsException);
        method.addParameter(new JParameter(JType.Int, "index"));

        createGetByIndexMethod(method);


        //-- array getter

        jType = jType.createArray();
        method = new JMethod(jType, "get"+cName);
        jClass.addMethod(method);

        createGetMethod(method);

        if (extraMethods()) {
            //-- Reference getter (non type-safe)
            method = new JMethod(SGTypes.createVector(contentType.getJType()), "get" + cName + _referenceSuffix);
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
            //-- Vector setter
            JParameter vParam = new JParameter(SGTypes.createVector(contentType.getJType()), pName+"Vector");
            method = new JMethod(null, "set"+cName);
            method.addParameter(vParam);
            jClass.addMethod(method);
            createSetCollectionMethod(method);
            
            //-- Reference setter (non type-safe
            method = new JMethod(null, "set" + cName + _referenceSuffix);
            method.addParameter(vParam);
            jClass.addMethod(method);
            createSetCollectionReferenceMethod(method);
        }
        

          //--------------------------/
         //- Create getCount method -/
        //--------------------------/

        method = new JMethod(JType.Int, "get"+cName+"Count");
        jClass.addMethod(method);

        createGetCountMethod(method);


          //---------------------------/
         //- Create Enumerate Method -/
        //---------------------------/

        method = new JMethod(SGTypes.createEnumeration(contentType.getJType()),"enumerate"+cName);
        jClass.addMethod(method);

        createEnumerateMethod(method, jClass);


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
            jsc.add("throw new IndexOutOfBoundsException(\"");
            jsc.append(method.getName());
            jsc.append(" has a maximum of ");
            jsc.append(Integer.toString(maxSize));
            jsc.append("\");"); 
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
     * Creates implementation of add method with an index.
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
            jsc.add("throw new IndexOutOfBoundsException(\"");
            jsc.append(method.getName());
            jsc.append(" has a maximum of ");
            jsc.append(Integer.toString(maxSize));
            jsc.append("\");"); 
            jsc.unindent();
            jsc.add("}");
        }
        jsc.add(getName());
        jsc.append(".insertElementAt(");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
		jsc.append(", index);");

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
            jsc.append(getContentType().createFromJavaObjectCode(variableName));
        }
        jsc.append(";");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return mArray;");
    } //-- createGetMethod
    
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
        comment.appendComment("modications to the Vector.");
        JDocDescriptor jDesc = JDocDescriptor.createReturnDesc();
        jDesc.setDescription("returns a reference to the Vector.");
        comment.addDescriptor(jDesc);
        
        //-- create copy code
        JSourceCode jsc = method.getSourceCode();
        
        jsc.add("return ");
        jsc.append(getName() + ';');

    } //-- createGetCollectionReferenceMethod
    

    /**
     * Creates implementation of the get(index) method.
    **/
    public void createGetByIndexMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

        jsc.add("//-- check bounds for index");
        jsc.add("if ((index < 0) || (index >= ");
        jsc.append(getName());
        jsc.append(".size())) {");
        jsc.indent();
        jsc.add("throw new IndexOutOfBoundsException(\"");
        jsc.append(method.getName());
        jsc.append(": Index value '\"+index+\"' not in range [0..\"+(");
        jsc.append(getName());
        jsc.append(".size() - 1) + \"]");
        jsc.append("\");"); 
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
        jsc.add("if ((index < 0) || (index >= ");
        jsc.append(getName());
        jsc.append(".size())) {");
        jsc.indent();
        jsc.add("throw new IndexOutOfBoundsException(\"");
        jsc.append(method.getName());
        jsc.append(": Index value '\"+index+\"' not in range [0..\" + (");
        jsc.append(getName());
        jsc.append(".size() - 1) + \"]");        
        jsc.append("\");"); 
        jsc.unindent();
        jsc.add("}");

        int maxSize = getXSList().getMaximumSize();
        if (maxSize > 0) {
            jsc.add("if (!(");
            jsc.append("index < ");
            jsc.append(Integer.toString(maxSize));
            jsc.append(")) {");
            jsc.indent();
            jsc.add("throw new IndexOutOfBoundsException(\"");
            jsc.append(method.getName());
            jsc.append(" has a maximum of ");
            jsc.append(Integer.toString(maxSize));
            jsc.append("\");"); 
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
        comment.appendComment("' by copying the given Vector.");
        JDocDescriptor jDesc = comment.getParamDescriptor(paramName);
        jDesc.setDescription("the Vector to copy.");
        
        //-- create copy code
        JSourceCode jsc = method.getSourceCode();

        String index = "i";
        if (paramName.equals(index)) index = "j";

        jsc.add("//-- copy vector");
        jsc.add(getName());
        jsc.append(".removeAllElements();");
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
        jsc.append(".addElement(");
        jsc.append('(' + getContentType().getJType().toString() + ')');
		jsc.append(getContentType().createToJavaObjectCode(paramName+".elementAt("+index+')'));
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
        comment.appendComment("' by setting it to the given Vector.");
        comment.appendComment(" No type checking is performed.");
        JDocDescriptor jDesc = comment.getParamDescriptor(paramName);
        jDesc.setDescription("the Vector to copy.");
        
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
     * Sets whether or not to create extra collection methods
     * for accessing the actual collection
     *
     * @param extraMethods a boolean that when true indicates that
     * extra collection accessor methods should be created. False
     * by default.
     * @see #setReferenceMethodSuffix
     */
    public void setCreateExtraMethods(boolean extraMethods) {
        _extraMethods = extraMethods;
    } //-- setCreateExtraMethods
    
    /**
     * Sets the method suffix (ending) to use when creating
     * the extra collection methods.
     *
     * @param suffix the method suffix to use when creating
     * the extra collection methods. If null or emtpty the default
     * value, as specified by DEFAULT_REFERENCE_SUFFIX will
     * used.
     * @see #setCreateExtraMethods
     */
    public void setReferenceMethodSuffix(String suffix) {
        if ((suffix == null) || (suffix.length() == 0)) {
            this._referenceSuffix = DEFAULT_REFERENCE_SUFFIX;
        }
        else _referenceSuffix = suffix;
    } //-- setReferenceMethodSuffix
    
    
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
     * @param jClass TODO
    **/
    public void createEnumerateMethod(JMethod method, JClass jClass) {

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

        jsc.add("java.lang.Object obj = ");
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

    /**
     * Returns true if extra collection methods should be generated.
     * The extra collection methods are methods which return an
     * actual reference to the underlying collection as opposed to
     * an enumeration, iterator, or copy.
     *
     * @return true if extra collection methods should be generated
     */
    protected final boolean extraMethods() {
        return _extraMethods;
    } //-- extraMethods
    
    /**
     * Returns the suffix (ending) that should be used when
     * creating the extra collection methods
     *
     * @return the suffix for the reference methods
     */
    protected final String getReferenceMethodSuffix() {
        return _referenceSuffix;
    } //-- getReferenceMethodSuffix
    

} //-- CollectionInfo


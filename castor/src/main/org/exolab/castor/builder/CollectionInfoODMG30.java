package org.exolab.castor.builder;


import org.exolab.castor.builder.*;
import org.exolab.castor.builder.types.*;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.javasource.*;

import java.util.Vector;


/**
 * A helper used for generating source that deals with Collections.
 * @author <a href="mailto:frank.thelen@poet.de">Frank Thelen</a>
 * @author <a href="mailto:bernd.deichmann@poet.de">Bernd Deichmann</a>
 * @version $Revision$ $Date$
**/
public class CollectionInfoODMG30 extends CollectionInfo {


    /**
     * Creates a new CollectionInfoODMG30
     * @param contextType the content type of the collection, ie. the type
     * of objects that the collection will contain
     * @param name the name of the Collection
     * @param elementName the element name for each element in collection
    **/
    public CollectionInfoODMG30(XSType contentType, String name, String elementName)
    {
        super(contentType, name, elementName);
        //getSchemaType().setJType(new JClass("org.odmg.DArray"));
		setSchemaType(new XSListODMG30(contentType));
		//--override the internal list
        xsList = (XSListODMG30)getSchemaType();
    } //-- CollectionInfoODMG30

    /**
     * Creates code for initialization of this Member
     * @param jsc the JSourceCode in which to add the source to
    **/
    public void generateInitializerCode(JSourceCode jsc) {
            jsc.add(getName());
            jsc.append(" = ODMG.getImplementation().newDArray();");
    } //-- generateConstructorCode

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Creates implementation of add method.
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
        //jsc.append(".addElement(");
        jsc.append(".add(");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");

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
     * Creates implementation of object[] get() method.
    **/
    public void createGetMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

        jsc.add("int size = ");
        jsc.append(getName());
        jsc.append(".size();");

        //String variableName = getName()+".elementAt(index)";
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

        //String variableName = getName()+".elementAt(index)";
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
     * Creates implementation of array set method
     *
     * Method added 12/14/2000 BD
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
        //jsc.append(".setElementAt(");
        //jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        //jsc.append(", index);");
        jsc.append(".set(");                                                        // ODMG 3.0
        jsc.append("index, ");
        jsc.append(getContentType().createToJavaObjectCode(getContentName()));
        jsc.append(");");
    } //-- createSetMethod


    /**
     * Creates implementation of getCount method.
    **/
    public void createGetCountMethod(JMethod method) {
        super.createGetCountMethod(method);
    } //-- createGetCoundMethod

    /**
     * Creates implementation of Enumerate method.
    **/
    public void createEnumerateMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();

        //jsc.add("return ");
        //jsc.append(getName());
        //jsc.append(".elements();");
        jsc.add("java.util.Vector v = new java.util.Vector();");                    // ODMG 3.0
        jsc.add("java.util.Iterator i = ");
        jsc.append(getName());
        jsc.append(".iterator();");
        jsc.add("while(i.hasNext()) v.add(i.next());");
        jsc.add("return v.elements();");

    } //-- createEnumerateMethod

    /**
     * Creates implementation of remove(Object) method.
    **/
    public void createRemoveByObjectMethod(JMethod method) {
		super.createRemoveByObjectMethod(method);
    } //-- createRemoveByObjectMethod

    /**
     * Creates implementation of remove(int i) method.
    **/
    public void createRemoveByIndexMethod(JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        JType jType = method.getReturnType();

        jsc.add("java.lang.Object obj = ");
        jsc.append(getName());
        //jsc.append(".elementAt(index);");
        jsc.append(".get(index);");
        jsc.add(getName());
        //jsc.append(".removeElementAt(index);");
        jsc.append(".remove(index);");
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
    **/
    public void createRemoveAllMethod (JMethod method) {

        JSourceCode jsc = method.getSourceCode();
        jsc.add(getName());
        //jsc.append(".removeAllElements();");
        jsc.append(".clear();");

    } //-- createRemoveAllMethod


} //-- CollectionInfoODMG30


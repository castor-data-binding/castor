package org.exolab.castor.builder.factory;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.AnnotationBuilder;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.nature.XMLInfoNature;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JDocComment;
import org.exolab.javasource.JDocDescriptor;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JModifiers;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JPrimitiveType;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;
import org.exolab.javasource.Java5HacksHelper;

/**
 * This factory takes a FieldInfo and generates the suitable JFields
 * (and optional the getter and setter methods) into the JClass.
 */
public class FieldMemberAndAccessorFactory {
    
    /**
     * The {@link JavaNaming} to use.
     */
    private JavaNaming _javaNaming;;

    /**
     * Creates a factory that offers public methods to create the 
     * field initialization code as well as the getter/setter methods.
     * 
     * @param naming JavaNaming to use
     */
    public FieldMemberAndAccessorFactory(final JavaNaming naming) {
        _javaNaming = naming;
    }

    /**
     * Creates the field initialization code in a constructor.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @param jsc the JSourceCode in which to add the source to
     */
    public void generateInitializerCode(final FieldInfo fieldInfo, final JSourceCode jsc) {
        //set the default value
        XMLInfoNature xmlNature = new XMLInfoNature(fieldInfo);
        
        if (!xmlNature.getSchemaType().isPrimitive()) {
            String value = fieldInfo.getDefaultValue();
            boolean dateTime = xmlNature.getSchemaType().isDateTime();
            if (value == null) {
                value = fieldInfo.getFixedValue();
            }
            if (value != null) {
                StringBuilder buffer = new StringBuilder(50);
                //date/time constructors throw ParseException that
                //needs to be catched in the constructor--> not the prettiest solution
                //when mulitple date/time in a class.
                if (dateTime) {
                    jsc.add("try {");
                    jsc.indent();
                }
                /*
                 * fieldInfo.getWriteMethodName() will either prefix the method
                 * with 'add' (for multivalued fields) or 'set'! 
                 * @see FieldInfo#getWriteMethodeName() 
                 */ 
                buffer.append(fieldInfo.getWriteMethodName());
                //buffer.append(FieldInfo.METHOD_PREFIX_SET);
                //buffer.append(fieldInfo.getMethodSuffix());
                buffer.append('(');
                buffer.append(value);
                buffer.append(");");
                jsc.add(buffer.toString());
                if (dateTime) {
                    jsc.unindent();
                    jsc.add("} catch (java.text.ParseException pe) {");
                    jsc.indent();
                    jsc.add("throw new IllegalStateException(pe.getMessage());");
                    jsc.unindent();
                    jsc.add("}");
                }
            }
        }
    } //-- generateInitializerCode

    /**
     * Adds the suitable JField to the JClass.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @param jClass the jclass the jField will be added to
     */
    public final void createJavaField(final FieldInfo fieldInfo,  final JClass jClass) {
        XMLInfoNature xmlNature = new XMLInfoNature(fieldInfo);
        XSType type = xmlNature.getSchemaType();
        JType jType = type.getJType();
        JField field = new JField(type.getJType(), fieldInfo.getName());

        if (xmlNature.getSchemaType().isDateTime()) {
            field.setDateTime(true);
        }

        if (fieldInfo.isStatic() || fieldInfo.isFinal()) {
            JModifiers modifiers = field.getModifiers();
            modifiers.setFinal(fieldInfo.isFinal());
            modifiers.setStatic(fieldInfo.isStatic());
        }

        if (!(fieldInfo.getVisibility().equals("private"))) {
            JModifiers modifiers = field.getModifiers();
            if (fieldInfo.getVisibility().equals("protected")) {
                modifiers.makeProtected();
            } else if (fieldInfo.getVisibility().equals("public")) {
                modifiers.makePublic();
            }
        }

        //-- set init String
        if (fieldInfo.getDefaultValue() != null) {
            if (!xmlNature.isMultivalued()) {
                field.setInitString(fieldInfo.getDefaultValue());
            } 
        }

        if (fieldInfo.getFixedValue() != null && !xmlNature.getSchemaType().isDateTime()) {
            field.setInitString(fieldInfo.getFixedValue());
        }

        //-- set Javadoc comment
        if (fieldInfo.getComment() != null) {
            field.setComment(fieldInfo.getComment());
        }

        jClass.addField(field);

        //-- special supporting fields

        //-- has_field
        if ((!type.isEnumerated()) && (jType.isPrimitive())) {
            field = new JField(JType.BOOLEAN, "_has" + fieldInfo.getName());
            field.setComment("keeps track of state for field: " + fieldInfo.getName());
            jClass.addField(field);
        }

        //-- save default value for primitives
        //-- not yet finished
        /*
        if (type.isPrimitive()) {
            field = new JField(jType, "_DEFAULT" + name.toUpperCase());
            JModifiers modifiers = field.getModifiers();
            modifiers.setFinal(true);
            modifiers.setStatic(true);

            if (_default != null)
                field.setInitString(_default);

            jClass.addField(field);
        }
        */
    } //-- createJavaField

    /**
     * Adds the getter/setter for this field to the jClass.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @param jClass the jclass the jField will be added to
     * @param useJava50  java version flag
     */
    public void createAccessMethods(final FieldInfo fieldInfo, 
            final JClass jClass, final boolean useJava50, final AnnotationBuilder[] annotationBuilders) {
        if ((fieldInfo.getMethods() & FieldInfo.READ_METHOD) > 0) {
            createGetterMethod(fieldInfo, jClass, useJava50, annotationBuilders);
        }
        if ((fieldInfo.getMethods() & FieldInfo.WRITE_METHOD) > 0) {
            createSetterMethod(fieldInfo, jClass, useJava50);
        }
        if (fieldInfo.requiresHasAndDeleteMethods()) {
            createHasAndDeleteMethods(fieldInfo, jClass);
        }
    } //-- createAccessMethods

    /**
     * Creates the Javadoc comments for the getter method associated with this
     * FieldInfo.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @param jDocComment the JDocComment to add the Javadoc comments to.
     */
    private void createGetterComment(final FieldInfo fieldInfo, 
            final JDocComment jDocComment) {
        String fieldName = fieldInfo.getName();
        //-- remove '_' if necessary
        if (fieldName.indexOf('_') == 0) {
            fieldName = fieldName.substring(1);
        }

        String mComment = "Returns the value of field '" + fieldName + "'.";
        if ((fieldInfo.getComment() != null) && (fieldInfo.getComment().length() > 0)) {
            mComment += " The field '" + fieldName + "' has the following description: ";

            // XDoclet support - Add a couple newlines if it's a doclet tag
            if (fieldInfo.getComment().startsWith("@")) {
                mComment += "\n\n";
            }

            mComment += fieldInfo.getComment();
        }
        jDocComment.setComment(mComment);
    } //-- createGetterComment

    /**
     * Creates the getter methods for this FieldInfo.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @param jClass the JClass to add the methods to
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     */
    private void createGetterMethod(final FieldInfo fieldInfo, 
            final JClass jClass, final boolean useJava50, AnnotationBuilder[] annotationBuilders) {
        JMethod method    = null;
        JSourceCode jsc   = null;

        String mname = fieldInfo.getMethodSuffix();

        XSType xsType = new XMLInfoNature(fieldInfo).getSchemaType();
        JType jType  = xsType.getJType();

        //-- create get method
        method = new JMethod(fieldInfo.getReadMethodName(), jType,
                             "the value of field '" + mname + "'.");
//        if (useJava50) {
//            Java5HacksHelper.addOverrideAnnotations(method.getSignature());
//        }
        
        for (int i = 0; i < annotationBuilders.length; i++) {
            AnnotationBuilder annotationBuilder = annotationBuilders[i];
            annotationBuilder.addFieldGetterAnnotations(fieldInfo, method);
        }
        
        jClass.addMethod(method);
        createGetterComment(fieldInfo, method.getJDocComment());
        jsc = method.getSourceCode();
        jsc.add("return this.");
        jsc.append(fieldInfo.getName());
        jsc.append(";");

        if (xsType.getType() == XSType.BOOLEAN_TYPE) {

            // -- create is<Property>t method
            method = new JMethod(fieldInfo.getIsMethodName(), jType,
                    "the value of field '" + mname + "'.");
//            if (useJava50) {
//                Java5HacksHelper.addOverrideAnnotations(method.getSignature());
//            }
            jClass.addMethod(method);
            createGetterComment(fieldInfo, method.getJDocComment());
            jsc = method.getSourceCode();
            jsc.add("return this.");
            jsc.append(fieldInfo.getName());
            jsc.append(";");

        }

    } //-- createGetterMethod

    /**
     * Creates the "has" and "delete" methods for this field associated with
     * this FieldInfo. These methods are typically only needed for primitive
     * types which cannot be assigned a null value.
     *
     * @param fieldInfo the fieldInfo to translate
     * @param jClass the JClass to add the methods to
     */
    private void createHasAndDeleteMethods(final FieldInfo fieldInfo, 
            final JClass jClass) {
        JMethod method    = null;
        JSourceCode jsc   = null;

        String mname = fieldInfo.getMethodSuffix();

        XSType xsType = new XMLInfoNature(fieldInfo).getSchemaType();
        xsType.getJType();

        //-- create hasMethod
        method = new JMethod(fieldInfo.getHasMethodName(), JType.BOOLEAN,
                             "true if at least one " + mname + " has been added");
        jClass.addMethod(method);
        jsc = method.getSourceCode();
        jsc.add("return this._has");
        String fieldName = fieldInfo.getName();
        jsc.append(fieldName);
        jsc.append(";");

        //-- create delete method
        method = new JMethod(fieldInfo.getDeleteMethodName());
        jClass.addMethod(method);
        jsc = method.getSourceCode();
        jsc.add("this._has");
        jsc.append(fieldName);
        jsc.append("= false;");
        //-- bound properties
        if (fieldInfo.isBound()) {
            //notify listeners
            jsc.add("notifyPropertyChangeListeners(\"");
            if (fieldName.startsWith("_")) {
                jsc.append(fieldName.substring(1));
            } else {
                jsc.append(fieldName);
            }
            jsc.append("\", ");
            //-- 'this.' ensures this refers to the class member not the parameter
            jsc.append(xsType.createToJavaObjectCode("this." + fieldName));
            jsc.append(", null");
            jsc.append(");");
        }
    } //-- createHasAndDeleteMethods

    /**
     * Creates the Javadoc comments for the setter method associated with this
     * FieldInfo.
     * 
     * @param fieldInfo the fieldInfo to translate
     * @param jDocComment the JDocComment to add the Javadoc comments to.
     */
    private void createSetterComment(final FieldInfo fieldInfo, 
            final JDocComment jDocComment) {
        String fieldName = fieldInfo.getName();
        //-- remove '_' if necessary
        if (fieldName.indexOf('_') == 0) {
            fieldName = fieldName.substring(1);
        }

        String atParam = "the value of field '" + fieldName + "'.";

        String mComment = "Sets " + atParam;
        if ((fieldInfo.getComment() != null) && (fieldInfo.getComment().length() > 0)) {
            mComment += " The field '" + fieldName + "' has the following description: ";

            // XDoclet support - Add a couple newlines if it's a doclet tag
            if (fieldInfo.getComment().startsWith("@")) {
                mComment += "\n\n";
            }

            mComment += fieldInfo.getComment();
        }

        jDocComment.setComment(mComment);

        JDocDescriptor paramDesc = jDocComment.getParamDescriptor(fieldName);
        if (paramDesc == null) {
            paramDesc = JDocDescriptor.createParamDesc(fieldName, null);
            jDocComment.addDescriptor(paramDesc);
        }
        paramDesc.setDescription(atParam);
    } //-- createSetterComment

    /**
     * Creates the setter (mutator) method(s) for this FieldInfo.
     *
     * @param fieldInfo the fieldInfo to translate
     * @param jClass the JClass to add the methods to
     * @param useJava50 true if source code is supposed to be generated for Java 5
     */
     private void createSetterMethod(final FieldInfo fieldInfo, 
             final JClass jClass, final boolean useJava50) {
        JMethod method    = null;
        JSourceCode jsc   = null;

        XMLInfoNature xmlNature = new XMLInfoNature(fieldInfo);
        
        String mname  = fieldInfo.getMethodSuffix();
        XSType xsType = xmlNature.getSchemaType();
        JType jType   = xsType.getJType();

        //-- create set method
        /*
         * fieldInfo.getWriteMethodName() will either prefix the method
         * with 'add' (for multivalued fields) or 'set'! 
         * @see FieldInfo#getWriteMethodeName() 
         */ 
        method = new JMethod(fieldInfo.getWriteMethodName());
        jClass.addMethod(method);

        String paramName = fieldInfo.getName();

        //-- make parameter name pretty,
        //-- simply for aesthetic beauty
        if (paramName.indexOf('_') == 0) {
            String tempName = paramName.substring(1);
            if (_javaNaming.isValidJavaIdentifier(tempName)) {
                paramName = tempName;
            }
        }

        method.addParameter(new JParameter(jType, paramName));
//        if (useJava50) {
//            Java5HacksHelper.addOverrideAnnotations(method.getSignature()); // DAB Java 5.0 hack
//        }
        createSetterComment(fieldInfo, method.getJDocComment());
        jsc = method.getSourceCode();

        String fieldName = fieldInfo.getName();
        //-- bound properties
        if (fieldInfo.isBound()) {
            // save old value
            jsc.add("java.lang.Object old");
            jsc.append(mname);
            jsc.append(" = ");
            //-- 'this.' ensures this refers to the class member not the parameter
            jsc.append(xsType.createToJavaObjectCode("this." + fieldName));
            jsc.append(";");
        }

        //-- set new value
        jsc.add("this.");
        jsc.append(fieldName);
        jsc.append(" = ");
        jsc.append(paramName);
        jsc.append(";");

        if (fieldInfo.getFieldInfoReference() != null) {
            jsc.add("this.");
            jsc.append(fieldInfo.getFieldInfoReference().getName());
            jsc.append(" = ");

            JType referencedJType = new XMLInfoNature(fieldInfo.getFieldInfoReference()).getSchemaType().getJType();
            if (referencedJType.isPrimitive()) {
                jsc.append(paramName);
            } else if (jType.isPrimitive()) {
                JPrimitiveType primitive = (JPrimitiveType) jType;
                jsc.append("new ");
                jsc.append(primitive.getWrapperName());
                jsc.append("(");
                jsc.append(paramName);
                jsc.append(")");
            } else {
                jsc.append(paramName);
            }

            jsc.append(";");
        }

        //-- hasProperty
        if (fieldInfo.requiresHasAndDeleteMethods()) {
            jsc.add("this._has");
            jsc.append(fieldName);
            jsc.append(" = true;");
        }

        //-- bound properties
        if (fieldInfo.isBound()) {
            //notify listeners
            jsc.add("notifyPropertyChangeListeners(\"");
            if (fieldName.startsWith("_")) {
                jsc.append(fieldName.substring(1));
            } else {
                jsc.append(fieldName);
            }
            jsc.append("\", old");
            jsc.append(mname);
            jsc.append(", ");
            //-- 'this.' ensures this refers to the class member not the parameter
            jsc.append(xsType.createToJavaObjectCode("this." + fieldName));
            jsc.append(");");
        }
    } //-- createSetterMethod

     /**
      * Returns the javaNaming.
      * 
      * @return the javaNaming instance
      */
    public JavaNaming getJavaNaming() {
        return _javaNaming;
    }


}

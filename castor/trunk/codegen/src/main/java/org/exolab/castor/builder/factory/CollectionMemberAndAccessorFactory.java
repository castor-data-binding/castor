package org.exolab.castor.builder.factory;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.AnnotationBuilder;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.types.XSType;
import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JCollectionType;
import org.exolab.javasource.JDocComment;
import org.exolab.javasource.JDocDescriptor;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * This factory takes a CollectionInfo and generates the suitable JFields
 * and the accessor methods into the JClass.
 */
public class CollectionMemberAndAccessorFactory extends FieldMemberAndAccessorFactory {

    /**
     * Creates a factory that offers public methods to create the 
     * field initialization code as well as the accessor methods.
     * 
     * @param naming JavaNaming to use
     */
    public CollectionMemberAndAccessorFactory(final JavaNaming naming) {
        super(naming);
    }

    /**
     * {@inheritDoc}
     */
    public void generateInitializerCode(final FieldInfo fieldInfo, 
            final JSourceCode sourceCode) {
        CollectionInfo collectionInfo = (CollectionInfo) fieldInfo;
        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(" = new ");
        JType jType = collectionInfo.getXSList().getJType();
        sourceCode.append(((JCollectionType) jType).getInstanceName());
        sourceCode.append("();");
    } // -- generateConstructorCode

    /**
     * {@inheritDoc}
     */
    public final void createAccessMethods(final FieldInfo fieldInfo, 
            final JClass jClass, final boolean useJava50, 
            final AnnotationBuilder[] annotationBuilders) {
        CollectionInfo collectionInfo = (CollectionInfo) fieldInfo;
        this.createAddAndRemoveMethods(collectionInfo, jClass);
        this.createGetAndSetMethods(collectionInfo, jClass, useJava50, annotationBuilders);
        this.createGetCountMethod(collectionInfo, jClass);
        this.createCollectionIterationMethods(collectionInfo, jClass, useJava50);
    } // -- createAccessMethods
    
    /**
     * Creates the add method for this collection.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    protected void createAddMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod(fieldInfo.getWriteMethodName());
        method.addException(SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION,
                "if the index given is outside the bounds of the collection");
        final JParameter parameter = new JParameter(
                fieldInfo.getContentType().getJType(), fieldInfo.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        this.addMaxSizeCheck(fieldInfo, method.getName(), sourceCode);

        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".addElement(");
        sourceCode.append(fieldInfo.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates bound property code..
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param sourceCode the sourceCode to attach to
     */
    protected void createBoundPropertyCode(final CollectionInfo fieldInfo, 
            final JSourceCode sourceCode) {
        sourceCode.add("notifyPropertyChangeListeners(\"");
        String fieldName = fieldInfo.getName();
        if (fieldName.startsWith("_")) {
            sourceCode.append(fieldName.substring(1));
        } else {
            sourceCode.append(fieldName);
        }
        sourceCode.append("\", null, ");
        sourceCode.append(fieldName);
        sourceCode.append(");");
    } // -- createBoundPropertyCode

    /**
     * Creates the enumerate method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     */
    protected void createEnumerateMethod(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("enumerate" + fieldInfo.getMethodSuffix(),
                SGTypes.createEnumeration(fieldInfo.getContentType().getJType(), useJava50, true),
                "an Enumeration over all " + fieldInfo.getContentType().getJType() + " elements");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".elements();");

        jClass.addMethod(method);
    }

    /**
     * Returns true if extra collection methods should be generated. The extra
     * collection methods are methods which return an actual reference to the
     * underlying collection as opposed to an enumeration, iterator, or copy.
     *
     * @param fieldInfo the collectionInfo to translate
     * @return true if extra collection methods should be generated
     */
    private boolean createExtraMethods(final CollectionInfo fieldInfo) {
        return fieldInfo.isExtraMethods();
    } // -- extraMethods

    /**
     * Creates the get as array method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     * @param annotationBuilders the custom builders
     */
    private void createGetAsArrayMethod(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50, 
            AnnotationBuilder[] annotationBuilders) {
        JType baseType = fieldInfo.getContentType().getJType();
        JType arrayType = new JArrayType(baseType, useJava50);
        JMethod method = new JMethod(fieldInfo.getReadMethodName(), arrayType,
                                     "this collection as an Array");

        JSourceCode sourceCode = method.getSourceCode();

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Returns the contents of the collection in an Array.  ");

        if (!(baseType.isPrimitive())) {
            // For non-primitive types, we use the API method made for this purpose
            comment.appendComment("<p>");
            comment.appendComment("Note:  Just in case the collection contents are changing in ");
            comment.appendComment("another thread, we pass a 0-length Array of the correct type ");
            comment.appendComment("into the API call.  This way we <i>know</i> that the Array ");
            comment.appendComment("returned is of exactly the correct length.");

            String baseTypeName = baseType.toString();
            if (baseType.isArray()) {
                sourceCode.add(arrayType.toString() + " array = new ");
                sourceCode.append(baseTypeName.substring(0, baseTypeName.length() - 2) + "[0][];");
            } else {
                sourceCode.add(arrayType.toString() + " array = new ");
                sourceCode.append(baseTypeName + "[0];");
            }
            sourceCode.add("return (" + arrayType.toString() + ") ");
            sourceCode.append("this." + fieldInfo.getName() + ".toArray(array);");
        } else {
            // For primitive types, we have to do this the hard way
            sourceCode.add("int size = this.");
            sourceCode.append(fieldInfo.getName());
            sourceCode.append(".size();");

            sourceCode.add(arrayType.toString());
            sourceCode.append(" array = new ");
            // the first brackets must contain the size...
            int brackets = arrayType.toString().indexOf("[]");
            sourceCode.append(arrayType.toString().substring(0, brackets));
            sourceCode.append("[size]");
            sourceCode.append(";");
            sourceCode.add("java.util.Iterator iter = " + fieldInfo.getName() + ".iterator();");

            String value = "iter.next()";
            sourceCode.add("for (int index = 0; index < size; index++) {");
            sourceCode.indent();
            sourceCode.add("array[index] = ");
            if (fieldInfo.getContentType().getType() == XSType.CLASS) {
                sourceCode.append("(");
                sourceCode.append(arrayType.getName());
                sourceCode.append(") ");
                sourceCode.append(value);
            } else {
                sourceCode.append(fieldInfo.getContentType().createFromJavaObjectCode(value));
            }
            sourceCode.append(";");
            sourceCode.unindent();
            sourceCode.add("}");

            sourceCode.add("return array;");
        }
        
        // add custom annotations
        for (int i = 0; i < annotationBuilders.length; i++) {
            AnnotationBuilder annotationBuilder = annotationBuilders[i];
            annotationBuilder.addFieldGetterAnnotations(fieldInfo, method);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the get as reference method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    private void createGetAsReferenceMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod(fieldInfo.getReadMethodName() 
                + fieldInfo.getReferenceMethodSuffix(),
                fieldInfo.getXSList().getJType(),
                "a reference to the Vector backing this class");

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Returns a reference to '");
        comment.appendComment(fieldInfo.getName());
        comment.appendComment("'. No type checking is performed on any ");
        comment.appendComment("modifications to the Vector.");

        // create code
        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(";");

        jClass.addMethod(method);
    }

    /**
     * Creates the get by index method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    protected void createGetByIndexMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        XSType contentType = fieldInfo.getContentType();
        JMethod method = new JMethod(fieldInfo.getReadMethodName(), contentType.getJType(),
                "the value of the " + contentType.getJType().toString() + " at the given index");

        method.addException(SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION,
                "if the index given is outside the bounds of the collection");
        method.addParameter(new JParameter(JType.INT, "index"));

        JSourceCode sourceCode = method.getSourceCode();
        this.addIndexCheck(fieldInfo, sourceCode, method.getName());

        String value = fieldInfo.getName() + ".get(index)";
        sourceCode.add("return ");
        if (contentType.getType() == XSType.CLASS) {
            sourceCode.append("(");
            sourceCode.append(method.getReturnType().toString());
            sourceCode.append(") ");
            sourceCode.append(value);
        } else {
            sourceCode.append(contentType.createFromJavaObjectCode(value));
        }
        sourceCode.append(";");

        jClass.addMethod(method);
    }

    /**
     * Creates the add/add by index/remove/remove by index/remove all methods.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    private void createAddAndRemoveMethods(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        // create add methods
        this.createAddMethod(fieldInfo, jClass);
        this.createAddByIndexMethod(fieldInfo, jClass);

        // create remove methods
        this.createRemoveObjectMethod(fieldInfo, jClass);
        this.createRemoveByIndexMethod(fieldInfo, jClass);
        this.createRemoveAllMethod(fieldInfo,jClass);
    }

    /**
     * Creates the getter and setter methods.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     * @param annotationBuilders the custom annotationBuilders
     */
    private void createGetAndSetMethods(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50,
            final AnnotationBuilder[] annotationBuilders) {
        // create get methods
        this.createGetByIndexMethod(fieldInfo, jClass);
        this.createGetAsArrayMethod(fieldInfo, jClass, useJava50, annotationBuilders);
        if (this.createExtraMethods(fieldInfo)) {
            this.createGetAsReferenceMethod(fieldInfo, jClass);
        }

        // create set methods
        this.createSetByIndexMethod(fieldInfo, jClass);
        this.createSetAsArrayMethod(fieldInfo, jClass, useJava50);
        if (this.createExtraMethods(fieldInfo)) {
            this.createSetAsCopyMethod(fieldInfo, jClass);
            this.createSetAsReferenceMethod(fieldInfo, jClass, useJava50);
        }
    }

    /**
     * Creates the get count method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    private void createGetCountMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod(fieldInfo.getReadMethodName() + "Count", JType.INT,
                                     "the size of this collection");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".size();");

        jClass.addMethod(method);
    }

    /**
     * Generate methods for iterating over the objects in the collection. For
     * Java-1 collections, we only generate an Enumerator. Implementations for
     * other versions of Java should call this method for backward compatbility
     * and then add any additional new methods.
     *
     * @param fieldInfo the collectionI to translate
     * @param jClass the JClass to which we add this method
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     */
    protected void createCollectionIterationMethods(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50) {
        this.createEnumerateMethod(fieldInfo, jClass, useJava50);
    } // -- createCollectionAccessMethods

    /**
     * Creates the add by index method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     */
    protected void createAddByIndexMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod(fieldInfo.getWriteMethodName());
        method.addException(SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION,
                            "if the index given is outside the bounds of the collection");
        method.addParameter(new JParameter(JType.INT, "index"));
        final JParameter parameter = new JParameter(
        		fieldInfo.getContentType().getJType(), fieldInfo.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        this.addMaxSizeCheck(fieldInfo, method.getName(), sourceCode);

        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".add(index, ");
        sourceCode.append(fieldInfo.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the iterate method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     */
    protected void createIteratorMethod(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("iterate" + fieldInfo.getMethodSuffix(),
                SGTypes.createIterator(fieldInfo.getContentType().getJType(), useJava50, true),
                "an Iterator over all possible elements in this collection");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".iterator();");

        jClass.addMethod(method);
    }

    /**
     * Creates the remove all method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    private void createRemoveAllMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod("removeAll" + fieldInfo.getMethodSuffix());

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".clear();");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the remove by index method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    protected void createRemoveByIndexMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod("remove" + fieldInfo.getMethodSuffix() + "At",
                fieldInfo.getContentType().getJType(),
                "the element removed from the collection");

        method.addParameter(new JParameter(JType.INT, "index"));

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("java.lang.Object obj = this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".remove(index);");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        sourceCode.add("return ");
        if (fieldInfo.getContentType().getType() == XSType.CLASS) {
            sourceCode.append("(");
            sourceCode.append(method.getReturnType().getName());
            sourceCode.append(") obj;");
        } else {
            sourceCode.append(fieldInfo.getContentType().createFromJavaObjectCode("obj"));
            sourceCode.append(";");
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the remove by object method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    private void createRemoveObjectMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod("remove" + fieldInfo.getMethodSuffix(), JType.BOOLEAN,
                                     "true if the object was removed from the collection.");

        final JParameter parameter = new JParameter(fieldInfo.getContentType().getJType(),
                fieldInfo.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("boolean removed = ");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".remove(");
        sourceCode.append(fieldInfo.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        sourceCode.add("return removed;");

        jClass.addMethod(method);
    }

    /**
     * Creates the set as array method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     */
    private void createSetAsArrayMethod(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("set" + fieldInfo.getMethodSuffix());
        final JParameter parameter = new JParameter(new JArrayType(
                fieldInfo.getContentType().getJType(), useJava50), 
                fieldInfo.getContentName() + "Array");
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        String index = "i";
        if (parameter.getName().equals(index)) {
            index = "j";
        }

        sourceCode.add("//-- copy array");
        sourceCode.add(fieldInfo.getName());
        sourceCode.append(".clear();");
        sourceCode.add("");
        sourceCode.add("for (int ");
        sourceCode.append(index);
        sourceCode.append(" = 0; ");
        sourceCode.append(index);
        sourceCode.append(" < ");
        sourceCode.append(parameter.getName());
        sourceCode.append(".length; ");
        sourceCode.append(index);
        sourceCode.append("++) {");
        sourceCode.indent();
        sourceCode.addIndented("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".add(");
        sourceCode.append(fieldInfo.getContentType().createToJavaObjectCode(
                parameter.getName() + "[" + index + "]"));
        sourceCode.append(");");
        sourceCode.unindent();
        sourceCode.add("}");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the set as copy method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    private void createSetAsCopyMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod("set" + fieldInfo.getMethodSuffix());
        JParameter parameter = new JParameter(fieldInfo.getXSList().getJType(),
                fieldInfo.getContentName() + "List");
        method.addParameter(parameter);

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Sets the value of '");
        comment.appendComment(fieldInfo.getName());
        comment.appendComment(
                "' by copying the given Vector. All elements will be checked for type safety.");
        JDocDescriptor jDesc = comment.getParamDescriptor(parameter.getName());
        jDesc.setDescription("the Vector to copy.");

        // create code
        JSourceCode sourceCode = method.getSourceCode();

        sourceCode.add("// copy vector");
        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".clear();");
        sourceCode.add("");

        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".addAll(");
        sourceCode.append(parameter.getName());
        sourceCode.append(");");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the set as reference method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     * @param useJava50 java version flag
     */
    private void createSetAsReferenceMethod(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("set" + fieldInfo.getMethodSuffix() 
                + fieldInfo.getReferenceSuffix());
        final JType collectionJType = fieldInfo.getSchemaType().getJType();
        JParameter parameter = new JParameter(
                collectionJType, fieldInfo.getParameterPrefix() + collectionJType.getLocalName());
        method.addParameter(parameter);

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Sets the value of '");
        comment.appendComment(fieldInfo.getName());
        comment.appendComment("' by setting it to the given Vector.");
        comment.appendComment(" No type checking is performed.");
        comment.appendComment("\n@deprecated");
        JDocDescriptor jDesc = comment.getParamDescriptor(parameter.getName());
        jDesc.setDescription("the Vector to set.");

        // create code
        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(" = ");
        sourceCode.append(parameter.getName());
        sourceCode.append(";");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the set by index method.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param jClass the jClass to add the method to.
     */
    protected void createSetByIndexMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod("set" + fieldInfo.getMethodSuffix());

        method.addException(SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION,
                            "if the index given is outside the bounds of the collection");
        method.addParameter(new JParameter(JType.INT, "index"));
        method.addParameter(new JParameter(fieldInfo.getContentType().getJType(),
                fieldInfo.getContentName()));

        JSourceCode sourceCode = method.getSourceCode();
        this.addIndexCheck(fieldInfo, sourceCode, method.getName());

        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".set(index, ");
        sourceCode.append(fieldInfo.getContentType().createToJavaObjectCode(
                fieldInfo.getContentName()));
        sourceCode.append(");");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }
    
    /**
     * Creates the add max size check.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param methodName the method name
     * @param sourceCode the sourceCode to attach to
     */
    protected void addMaxSizeCheck(final CollectionInfo fieldInfo, 
            final String methodName, final JSourceCode sourceCode) {
        if (fieldInfo.getXSList().getMaximumSize() > 0) {
            final String size = Integer.toString(fieldInfo.getXSList().getMaximumSize());

            sourceCode.add("// check for the maximum size");
            sourceCode.add("if (this.");
            sourceCode.append(fieldInfo.getName());
            sourceCode.append(".size() >= ");
            sourceCode.append(size);
            sourceCode.append(") {");
            sourceCode.indent();
            sourceCode.add("throw new IndexOutOfBoundsException(\"");
            sourceCode.append(methodName);
            sourceCode.append(" has a maximum of ");
            sourceCode.append(size);
            sourceCode.append("\");");
            sourceCode.unindent();
            sourceCode.add("}");
            sourceCode.add("");
        }
    }
    
    /**
     * Creates the index check.
     * 
     * @param fieldInfo the collectionInfo to translate
     * @param methodName the method name
     * @param sourceCode the sourceCode to attach to
     */
    private void addIndexCheck(final CollectionInfo fieldInfo, 
            final JSourceCode sourceCode, final String methodName) {
        sourceCode.add("// check bounds for index");
        sourceCode.add("if (index < 0 || index >= this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".size()) {");

        sourceCode.indent();
        sourceCode.add("throw new IndexOutOfBoundsException(\"");
        sourceCode.append(methodName);
        sourceCode.append(": Index value '\" + index + \"' not in range [0..\" + (this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".size() - 1) + \"]\");");
        sourceCode.unindent();
        sourceCode.add("}");
        sourceCode.add("");
    }


}

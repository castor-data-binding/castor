/*
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
package org.exolab.castor.builder.info;

import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.types.XSList;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.xml.JavaNaming;
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
 * A helper used for generating source that deals with Collections.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-02-23 01:08:24 -0700 (Thu, 23 Feb 2006) $
 */
public class CollectionInfo extends FieldInfo {

    /** Default suffix for the setter/getter by reference method names. */
    public static final String DEFAULT_REFERENCE_SUFFIX  = "AsReference";

    /**
     * The property used to overwrite the reference suffix for extra collection
     * methods.
     */
    public static final String REFERENCE_SUFFIX_PROPERTY =
                       "org.exolab.castor.builder.collections.reference.suffix";

    /**
     * A flag indicating that "extra" accessor methods should be created for
     * returning and setting a reference to the underlying collection.
     */
    private boolean            _extraMethods;
    /** The reference suffix to use. */
    private String             _referenceSuffix          = DEFAULT_REFERENCE_SUFFIX;

    /** Element type name converted to a method suffix. */
    private final String       _methodSuffix;
    /** Element type name converted to a parameter prefix. */
    private final String       _parameterPrefix;
    /** FieldInfo describing the _content (i.e. the elements) of this collection. */
    private final FieldInfo    _content;
    /** The name to be used when referring to the elements of this collection. */
    private final String       _elementName;

    /**
     * Creates a new CollectionInfo.
     *
     * @param contentType
     *            the _content type of the collection, ie. the type of objects
     *            that the collection will contain
     * @param name
     *            the name of the Collection
     * @param elementName
     *            the element name for each element in collection
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     */
    public CollectionInfo(final XSType contentType, final String name,
            final String elementName, final boolean useJava50) {
        super(new XSList(contentType, useJava50), name);

        if (elementName.charAt(0) == '_') {
            this._elementName = elementName.substring(1);
        } else {
            this._elementName = elementName;
        }

        this._methodSuffix = JavaNaming.toJavaClassName(this.getElementName());
        this._parameterPrefix = JavaNaming.toJavaMemberName(this.getElementName());
        this._content = new FieldInfo(contentType, "v" + this.getMethodSuffix());
    } // -- CollectionInfo

    /**
     * Generate the various accessor methods.
     * {@inheritDoc}
     *
     * @see org.exolab.castor.builder.info.FieldInfo
     *      #createAccessMethods(org.exolab.javasource.JClass, boolean)
     */
    public void createAccessMethods(final JClass jClass, final boolean useJava50) {
        this.createAddAndRemoveMethods(jClass);
        this.createGetAndSetMethods(jClass, useJava50);
        this.createGetCountMethod(jClass);
        this.createCollectionIterationMethods(jClass, useJava50);
    } // -- createAccessMethods

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.builder.info.FieldInfo
     *      #generateInitializerCode(org.exolab.javasource.JSourceCode)
     */
    public void generateInitializerCode(final JSourceCode sourceCode) {
        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(" = new ");
        JType jType = this.getXSList().getJType();
        sourceCode.append(((JCollectionType) jType).getInstanceName());
        sourceCode.append("();");
    } // -- generateConstructorCode

    /**
     * Return the contents of the collection.
     * @return the contents of the collection.
     */
    public FieldInfo getContent() {
        return this._content;
    }

    /**
     * Returns the variable name for the content of the collection.
     * @return the variable name for the content of the collection.
     */
    public String getContentName() {
        return this.getContent().getName();
    }

    /**
     * Returns the type of content in this collection.
     * @return the type of content in this collection.
     */
    public XSType getContentType() {
        return this.getContent().getSchemaType();

    }

    /**
     * Returns the name to be used when referring to the elements of this
     * collection.
     *
     * @return the name to be used when referring to the elements of this
     *          collection.
     */
    public String getElementName() {
        return this._elementName;
    }

    /**
     * Returns the schema type represented by this collection.
     * @return the schema type represented by this collection.
     */
    public XSList getXSList() {
        return (XSList) this.getSchemaType();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.builder.info.XMLInfo#isMultivalued()
     */
    public boolean isMultivalued() {
        return true;
    }

    /**
     * Sets whether or not to create extra collection methods for accessing the
     * actual collection.
     *
     * @param extraMethods
     *            a boolean that when true indicates that extra collection
     *            accessor methods should be created. False by default.
     * @see #setReferenceMethodSuffix
     */
    public void setCreateExtraMethods(final boolean extraMethods) {
        this._extraMethods = extraMethods;
    } // -- setCreateExtraMethods

    /**
     * Sets the method suffix (ending) to use when creating the extra collection
     * methods.
     *
     * @param suffix
     *            the method suffix to use when creating the extra collection
     *            methods. If null or emtpty the default value, as specified by
     *            DEFAULT_REFERENCE_SUFFIX will used.
     * @see #setCreateExtraMethods
     */
    public void setReferenceMethodSuffix(final String suffix) {
        if (suffix == null || suffix.length() == 0) {
            this._referenceSuffix = DEFAULT_REFERENCE_SUFFIX;
        } else {
            this._referenceSuffix = suffix;
        }
    } // -- setReferenceMethodSuffix

    private void addIndexCheck(final JSourceCode sourceCode, final String methodName) {
        sourceCode.add("// check bounds for index");
        sourceCode.add("if (index < 0 || index >= this.");
        sourceCode.append(this.getName());
        sourceCode.append(".size()) {");

        sourceCode.indent();
        sourceCode.add("throw new IndexOutOfBoundsException(\"");
        sourceCode.append(methodName);
        sourceCode.append(": Index value '\" + index + \"' not in range [0..\" + (this.");
        sourceCode.append(this.getName());
        sourceCode.append(".size() - 1) + \"]\");");
        sourceCode.unindent();
        sourceCode.add("}");
        sourceCode.add("");
    }

    protected void addMaxSizeCheck(final String methodName, final JSourceCode sourceCode) {
        if (this.getXSList().getMaximumSize() > 0) {
            final String size = Integer.toString(getXSList().getMaximumSize());

            sourceCode.add("// check for the maximum size");
            sourceCode.add("if (this.");
            sourceCode.append(this.getName());
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

    protected void createAddMethod(final JClass jClass) {
        JMethod method = new JMethod(this.getWriteMethodName());
        method.addException(SGTypes.IndexOutOfBoundsException,
                "if the index given is outside the bounds of the collection");
        final JParameter parameter = new JParameter(
                this.getContentType().getJType(), this.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        this.addMaxSizeCheck(method.getName(), sourceCode);

        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(".addElement(");
        sourceCode.append(this.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates the necessary source code for notifying PropertyChangeListeners
     * when the collection has been updated.
     *
     * @param sourceCode
     *            the JSourceCode to add the new source code to.
     */
    protected void createBoundPropertyCode(final JSourceCode sourceCode) {
        sourceCode.add("notifyPropertyChangeListeners(\"");
        sourceCode.append(getName());
        sourceCode.append("\", null, ");
        sourceCode.append(getName());
        sourceCode.append(");");
    } // -- createBoundPropertyCode

    protected void createEnumerateMethod(final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("enumerate" + this.getMethodSuffix(),
                SGTypes.createEnumeration(this.getContentType().getJType(), useJava50),
                "an Enumeration over all " + this.getContentType().getJType() + " elements");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(this.getName());
        sourceCode.append(".elements();");

        jClass.addMethod(method);
    }

    /**
     * Returns true if extra collection methods should be generated. The extra
     * collection methods are methods which return an actual reference to the
     * underlying collection as opposed to an enumeration, iterator, or copy.
     *
     * @return true if extra collection methods should be generated
     */
    protected final boolean createExtraMethods() {
        return this._extraMethods;
    } // -- extraMethods

    protected void createGetAsArrayMethod(final JClass jClass, final boolean useJava50) {
        JType baseType = this.getContentType().getJType();
        JType arrayType = new JArrayType(baseType, useJava50);
        JMethod method = new JMethod(this.getReadMethodName(), arrayType,
                                     "this collection as an Array");

        JSourceCode sourceCode = method.getSourceCode();

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Returns the contents of the collection in an Array.  ");

        if (!baseType.isPrimitive()) {
            // For non-primitive types, we use the API method made for this purpose
            comment.appendComment("<p>");
            comment.appendComment("Note:  Just in case the collection contents are changing in ");
            comment.appendComment("another thread, we pass a 0-length Array of the correct type ");
            comment.appendComment("into the API call.  This way we <i>know</i> that the Array ");
            comment.appendComment("returned is of exactly the correct length.");

            sourceCode.add(arrayType.toString() + " array = new " + baseType.toString() + "[0];");
            sourceCode.add("return (" + arrayType.toString() + ") ");
            sourceCode.append("this." + this.getName() + ".toArray(array);");
        } else {
            // For primitive types, we have to do this the hard way
            sourceCode.add("int size = this.");
            sourceCode.append(this.getName());
            sourceCode.append(".size();");

            sourceCode.add(arrayType.toString());
            sourceCode.append(" array = new ");
            // the first brackets must contain the size...
            int brackets = arrayType.toString().indexOf("[]");
            sourceCode.append(arrayType.toString().substring(0, brackets));
            sourceCode.append("[size]");
            sourceCode.append(";");
            sourceCode.add("java.util.Iterator iter = " + this.getName() + ".iterator();");

            String value = "iter.next()";
            sourceCode.add("for (int index = 0; index < size; index++)");
            sourceCode.append("{");
            sourceCode.indent();
            sourceCode.add("array[index] = ");
            if (getContentType().getType() == XSType.CLASS) {
                sourceCode.append("(");
                sourceCode.append(arrayType.getName());
                sourceCode.append(") ");
                sourceCode.append(value);
            } else {
                sourceCode.append(getContentType().createFromJavaObjectCode(value));
            }
            sourceCode.append(";");
            sourceCode.unindent();
            sourceCode.add("}");

            sourceCode.add("");
            sourceCode.add("return array;");
        }

        jClass.addMethod(method);
    }

    protected void createGetAsReferenceMethod(final JClass jClass) {
        JMethod method = new JMethod(this.getReadMethodName() + this.getReferenceMethodSuffix(),
                                     this.getXSList().getJType(),
                                     "a reference to the Vector backing this class");

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Returns a reference to '");
        comment.appendComment(this.getName());
        comment.appendComment("'. No type checking is performed on any ");
        comment.appendComment("modifications to the Vector.");

        // create code
        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(this.getName());
        sourceCode.append(";");

        jClass.addMethod(method);
    }

    protected void createGetByIndexMethod(final JClass jClass) {
        XSType contentType = this.getContentType();
        JMethod method = new JMethod(this.getReadMethodName(), contentType.getJType(),
                "the value of the " + contentType.getJType().toString() + " at the given index");

        method.addException(SGTypes.IndexOutOfBoundsException,
                "if the index given is outside the bounds of the collection");
        method.addParameter(new JParameter(JType.INT, "index"));

        JSourceCode sourceCode = method.getSourceCode();
        this.addIndexCheck(sourceCode, method.getName());

        String value = this.getName() + ".get(index)";
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

    protected void createAddAndRemoveMethods(final JClass jClass) {
        // create add methods
        this.createAddMethod(jClass);
        this.createAddByIndexMethod(jClass);

        // create remove methods
        this.createRemoveObjectMethod(jClass);
        this.createRemoveByIndexMethod(jClass);
        this.createRemoveAllMethod(jClass);
    }

    /**
     * @param jClass the JClass to which we add this method
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     */
    protected void createGetAndSetMethods(final JClass jClass, final boolean useJava50) {
        // create get methods
        this.createGetByIndexMethod(jClass);
        this.createGetAsArrayMethod(jClass, useJava50);
        if (this.createExtraMethods()) {
            this.createGetAsReferenceMethod(jClass);
        }

        // create set methods
        this.createSetByIndexMethod(jClass);
        this.createSetAsArrayMethod(jClass, useJava50);
        if (this.createExtraMethods()) {
            this.createSetAsCopyMethod(jClass);
            this.createSetAsReferenceMethod(jClass, useJava50);
        }
    }

    protected void createGetCountMethod(final JClass jClass) {
        JMethod method = new JMethod(this.getReadMethodName() + "Count", JType.INT,
                                     "the size of this collection");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(getName());
        sourceCode.append(".size();");

        jClass.addMethod(method);
    }

    /**
     * Generate methods for iterating over the objects in the collection. For
     * Java-1 collections, we only generate an Enumerator. Implementations for
     * other versions of Java should call this method for backward compatbility
     * and then add any additional new methods.
     *
     * @param jClass the JClass to which we add this method
     * @param useJava50
     *            true if source code is supposed to be generated for Java 5
     */
    protected void createCollectionIterationMethods(final JClass jClass, final boolean useJava50) {
        this.createEnumerateMethod(jClass, useJava50);
    } // -- createCollectionAccessMethods

    protected void createAddByIndexMethod(final JClass jClass) {
        JMethod method = new JMethod(this.getWriteMethodName());
        method.addException(SGTypes.IndexOutOfBoundsException,
                            "if the index given is outside the bounds of the collection");
        method.addParameter(new JParameter(JType.INT, "index"));
        final JParameter parameter = new JParameter(
                this.getContentType().getJType(), this.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        this.addMaxSizeCheck(method.getName(), sourceCode);

        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(".add(index, ");
        sourceCode.append(this.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    protected void createIteratorMethod(final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("iterate" + this.getMethodSuffix(),
                SGTypes.createIterator(this.getContentType().getJType(), useJava50),
                "an Iterator over all possible elements in this collection");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return this.");
        sourceCode.append(this.getName());
        sourceCode.append(".iterator();");

        jClass.addMethod(method);
    }

    /**
     * Creates implementation of removeAll() method.
     *
     * @param jClass the JClass to which we add this method
     */
    protected void createRemoveAllMethod(final JClass jClass) {
        JMethod method = new JMethod("removeAll" + this.getMethodSuffix());

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(".clear();");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates implementation of remove(int i) method.
     *
     * @param jClass the JClass to which we add this method
     */
    protected void createRemoveByIndexMethod(final JClass jClass) {
        JMethod method = new JMethod("remove" + this.getMethodSuffix() + "At",
                                     this.getContentType().getJType(),
                                     "the element removed from the collection");

        method.addParameter(new JParameter(JType.INT, "index"));

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("Object obj = this.");
        sourceCode.append(this.getName());
        sourceCode.append(".remove(index);");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        sourceCode.add("return ");
        if (getContentType().getType() == XSType.CLASS) {
            sourceCode.append("(");
            sourceCode.append(method.getReturnType().getName());
            sourceCode.append(") obj;");
        } else {
            sourceCode.append(this.getContentType().createFromJavaObjectCode("obj"));
            sourceCode.append(";");
        }

        jClass.addMethod(method);
    }

    /**
     * Creates implementation of remove(Object) method.
     *
     * @param jClass the JClass to which we add this method
     */
    protected void createRemoveObjectMethod(final JClass jClass) {
        JMethod method = new JMethod("remove" + this.getMethodSuffix(), JType.BOOLEAN,
                                     "true if the object was removed from the collection.");

        final JParameter parameter = new JParameter(this.getContentType().getJType(),
                                                    this.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("boolean removed = ");
        sourceCode.append(this.getName());
        sourceCode.append(".remove(");
        sourceCode.append(this.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        sourceCode.add("return removed;");

        jClass.addMethod(method);
    }

    protected void createSetAsArrayMethod(final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("set" + this.getMethodSuffix());
        final JParameter parameter = new JParameter(new JArrayType(
                this.getContentType().getJType(), useJava50), this.getContentName() + "Array");
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        String index = "i";
        if (parameter.getName().equals(index)) {
            index = "j";
        }

        sourceCode.add("//-- copy array");
        sourceCode.add(this.getName());
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
        sourceCode.append(this.getName());
        sourceCode.append(".add(");
        sourceCode.append(this.getContentType().createToJavaObjectCode(
                parameter.getName() + "[" + index + "]"));
        sourceCode.append(");");
        sourceCode.unindent();
        sourceCode.add("}");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates implementation of collection set method. The method will assign
     * the field a copy of the given collection.<br>
     * The fields will be checked for type safety.
     *
     * @param jClass
     */
    protected void createSetAsCopyMethod(final JClass jClass) {
        JMethod method = new JMethod("set" + this.getMethodSuffix());
        JParameter parameter = new JParameter(this.getXSList().getJType(),
                                              this.getContentName() + "List");
        method.addParameter(parameter);

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Sets the value of '");
        comment.appendComment(this.getName());
        comment.appendComment(
                "' by copying the given Vector. All elements will be checked for type safety.");
        JDocDescriptor jDesc = comment.getParamDescriptor(parameter.getName());
        jDesc.setDescription("the Vector to copy.");

        // create code
        JSourceCode sourceCode = method.getSourceCode();

        sourceCode.add("// copy vector");
        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(".clear();");
        sourceCode.add("");

        sourceCode.add("this.");
        sourceCode.append(getName());
        sourceCode.append(".addAll(");
        sourceCode.append(parameter.getName());
        sourceCode.append(");");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * Creates implementation of collection reference set method. This method is
     * a non-type safe method which simply assigns the given collection to the
     * field.
     *
     * @param jClass
     * @param useJava50 true if source code is supposed to be generated for Java 5
     */
    protected void createSetAsReferenceMethod(final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("set" + this.getMethodSuffix() + _referenceSuffix);
        final JType collectionJType = getSchemaType().getJType();
        JParameter parameter = new JParameter(
                collectionJType, this.getParameterPrefix() + collectionJType.getLocalName());
        method.addParameter(parameter);

        // create Javadoc
        JDocComment comment = method.getJDocComment();
        comment.appendComment("Sets the value of '");
        comment.appendComment(this.getName());
        comment.appendComment("' by setting it to the given Vector.");
        comment.appendComment(" No type checking is performed.");
        comment.appendComment("\n@deprecated");
        JDocDescriptor jDesc = comment.getParamDescriptor(parameter.getName());
        jDesc.setDescription("the Vector to set.");

        // create code
        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(" = ");
        sourceCode.append(parameter.getName());
        sourceCode.append(";");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    private String getParameterPrefix() {
        return _parameterPrefix;
    }

    protected void createSetByIndexMethod(final JClass jClass) {
        JMethod method = new JMethod("set" + this.getMethodSuffix());

        method.addException(SGTypes.IndexOutOfBoundsException,
                            "if the index given is outside the bounds of the collection");
        method.addParameter(new JParameter(JType.INT, "index"));
        method.addParameter(new JParameter(this.getContentType().getJType(),
                                           this.getContentName()));

        JSourceCode sourceCode = method.getSourceCode();
        this.addIndexCheck(sourceCode, method.getName());

        sourceCode.add("this.");
        sourceCode.append(this.getName());
        sourceCode.append(".set(index, ");
        sourceCode.append(this.getContentType().createToJavaObjectCode(getContentName()));
        sourceCode.append(");");

        if (this.isBound()) {
            this.createBoundPropertyCode(sourceCode);
        }

        jClass.addMethod(method);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.builder.info.FieldInfo#getMethodSuffix()
     */
    public String getMethodSuffix() {
        return this._methodSuffix;
    }

    /**
     * Returns the suffix (ending) that should be used when creating the extra
     * collection methods.
     *
     * @return the suffix for the reference methods
     */
    protected final String getReferenceMethodSuffix() {
        return this._referenceSuffix;
    } // -- getReferenceMethodSuffix

} // -- CollectionInfo

package org.exolab.castor.builder.factory;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.SGTypes;
import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JSourceCode;

/**
 * The Factory for Java2 Collections.
 */
public class CollectionJ2MemberAndAccessorFactory extends CollectionMemberAndAccessorFactory {

    /**
     * Creates a new CollectionJ2MemberAndAccessorFactory.
     * @param naming the javaNaming to use
     */
    public CollectionJ2MemberAndAccessorFactory(final JavaNaming naming) {
        super(naming);
    }

    /**
     * {@inheritDoc}
     * <br/>
     * To the Java-1 collection iterators, we add the Java-2 Iterator.
     */
    protected final void createCollectionIterationMethods(final CollectionInfo fieldInfo, 
            final JClass jClass,
                                                    final boolean useJava50) {
        super.createCollectionIterationMethods(fieldInfo, jClass, useJava50);
        this.createIteratorMethod(fieldInfo, jClass, useJava50);
    }

    /**
     * {@inheritDoc}
     */
    protected final void createEnumerateMethod(final CollectionInfo fieldInfo, 
            final JClass jClass, final boolean useJava50) {
        JMethod method = new JMethod("enumerate" + fieldInfo.getMethodSuffix(),
                SGTypes.createEnumeration(fieldInfo.getContentType().getJType(), useJava50, true),
                "an Enumeration over all possible elements of this collection");

        JSourceCode sourceCode = method.getSourceCode();
        sourceCode.add("return java.util.Collections.enumeration(this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(");");

        jClass.addMethod(method);
    }

    /**
     * {@inheritDoc}
     */
    protected final void createAddMethod(final CollectionInfo fieldInfo, 
            final JClass jClass) {
        JMethod method = new JMethod(fieldInfo.getWriteMethodName());
        method.addException(SGTypes.INDEX_OUT_OF_BOUNDS_EXCEPTION,
                            "if the index given is outside the bounds of the collection");
        final JParameter parameter = new JParameter(fieldInfo.getContentType().getJType(),
                fieldInfo.getContentName());
        method.addParameter(parameter);

        JSourceCode sourceCode = method.getSourceCode();
        this.addMaxSizeCheck(fieldInfo, method.getName(), sourceCode);

        sourceCode.add("this.");
        sourceCode.append(fieldInfo.getName());
        sourceCode.append(".add(");
        sourceCode.append(fieldInfo.getContentType().createToJavaObjectCode(parameter.getName()));
        sourceCode.append(");");

        if (fieldInfo.isBound()) {
            this.createBoundPropertyCode(fieldInfo, sourceCode);
        }

        jClass.addMethod(method);
    }
    
}

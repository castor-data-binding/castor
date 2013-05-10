package org.exolab.castor.builder.factory;

import org.castor.xml.JavaNaming;
import org.exolab.castor.builder.info.CollectionInfo;
import org.exolab.javasource.JClass;

/**
 * A Factory that avoids the creation of the index methods.
 */
public class CollectionJ2NoIndexMemberAndAccessorFactory extends CollectionJ2MemberAndAccessorFactory {

    /**
     * Creates a new CollectionJ2NoIndexMemberAndAccessorFactory.
     * @param naming the java naming to use
     */
    public CollectionJ2NoIndexMemberAndAccessorFactory(JavaNaming naming) {
        super(naming);
    }

    /**
     * {@inheritDoc}
     * supresses the method creation
     */
    protected void createAddByIndexMethod(final CollectionInfo fieldInfo,  final JClass jClass) {
        // do not create such method,
    }

    /**
     * {@inheritDoc}
     * supresses the method creation
     */
    protected void createGetByIndexMethod(final CollectionInfo fieldInfo, final JClass jClass) {
        // do not create such method
    }

    /**
     * {@inheritDoc}
     * supresses the method creation
     */
    protected void createSetByIndexMethod(final CollectionInfo fieldInfo, final JClass jClass) {
        // do not create such method
    }

    /**
     * {@inheritDoc}
     * supresses the method creation
     */
    protected void createRemoveByIndexMethod(final CollectionInfo fieldInfo, final JClass jClass) {
        // do not create such method
    }
    
}

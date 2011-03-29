package org.castor.cpa.jpa.info;

import java.util.LinkedHashMap;
import java.util.Map;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.InfoToDescriptorConverter;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.ClassDescriptorImpl;

/**
 * <p>
 * This {@link ClassDescriptorResolutionCommand} is used to generate
 * {@link ClassDescriptor}s from JPA annotated classes.
 * </p>
 * 
 * @see ClassInfoBuilder
 * @see InfoToDescriptorConverter
 * 
 * @author Peter Schmidt
 * @version 20.02.2009
 * 
 */
public class JPATestResolveCommand extends
        org.castor.cpa.util.classresolution.command.BaseResolutionCommand {

    /**
     * Internal loop cache. contains all unfinished {@link ClassDescriptor}s.
     * After conversion (fail or success) the descriptors are removed.
     */
    private Map<Class<?>, ClassDescriptorImpl> _loopCache = 
        new LinkedHashMap<Class<?>, ClassDescriptorImpl>();

    /**
     * Try to resolve/generate a {@link ClassDescriptor} for the given (JPA
     * annotated) type.
     * 
     * @param type
     *            The Java class that needs a descriptor
     * @return Usually a {@link ClassDescriptor} representing the given Class or
     *         null if the given type can not be resolved. When this method is
     *         called recursively (as in bidirectional relations) a reference
     *         to an incomplete {@link ClassDescriptor} is returned, which will
     *         be finished when leaving the loop again.
     * @see org.exolab.castor.xml.util.ClassDescriptorResolutionCommand#resolve(java.lang.Class)
     */
    public final ClassDescriptor resolve(final Class<?> type) {
        ClassInfo buildClassInfo = null;
        try {
            buildClassInfo = ClassInfoBuilder.buildClassInfo(type);
            if (buildClassInfo == null) {
                /*
                 * The type was not describable or not JPA annotated.
                 */
                return null;
            }
        } catch (MappingException e1) {
            /*
             * JPA annotations error occurred.
             */
            e1.printStackTrace();
            return null;
        }

        /*
         * check if we've been there in a loop
         */
        if (_loopCache.containsKey(type)) {
            return _loopCache.get(type);
        }

        /*
         * generate new ClassDescriptor, add it the loopCache and start conversion
         */
        ClassDescriptorImpl classDesc = new ClassDescriptorImpl();
        _loopCache.put(type, classDesc);
        try {
            InfoToDescriptorConverter.convert(buildClassInfo, this
                    .getClassDescriptorResolver(), classDesc);

            /*
             * conversion successful => remove it from the loopCache and return it
             */
            _loopCache.remove(type);
            return classDesc;
        } catch (MappingException e) {
            /*
             * conversion failed => remove it from the loopCache and return null
             */
            e.printStackTrace();
            _loopCache.remove(type);
            return null;
        }
    }
}

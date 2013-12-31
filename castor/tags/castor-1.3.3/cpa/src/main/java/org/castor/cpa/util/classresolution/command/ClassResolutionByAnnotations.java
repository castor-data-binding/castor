/*
 * Copyright 2006 Werner Guttmann, Peter Schmidt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.util.classresolution.command;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @author <a href="mailto:peter-list AT stayduebeauty DOT com">Peter Schmidt</a>
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * 
 */
public class ClassResolutionByAnnotations extends BaseResolutionCommand {
    //-----------------------------------------------------------------------------------

    /** The Logger instance to use. */
    private static final Log LOG = LogFactory
            .getLog(ClassResolutionByAnnotations.class);

    //-----------------------------------------------------------------------------------
    
    /**
     * Internal loop cache. contains all unfinished {@link ClassDescriptor}s.
     * After conversion (fail or success) the descriptors are removed from the cache.
     */
    private Map<Class<?>, ClassDescriptorImpl> _loopCache = 
        new LinkedHashMap<Class<?>, ClassDescriptorImpl>();

    //-----------------------------------------------------------------------------------
    
    /**
     * Try to resolve/generate a {@link ClassDescriptor} for the given (JPA
     * annotated) type.
     * 
     * @param type The Java class that needs a descriptor
     * @return Usually a {@link ClassDescriptor} representing the given Class or
     *         null if the given type can not be resolved. When this method is
     *         called recursively (as in bidirectional relations) a reference
     *         to an incomplete {@link ClassDescriptor} is returned, which will
     *         be finished when leaving the loop again.
     */
    public ClassDescriptor resolve(final Class type) {

        /*
         * check if we've been there in a loop, if yes, return the unfinished descriptor reference.
         */
        if (_loopCache.containsKey(type)) {
            return _loopCache.get(type);
        }
        
        /*
         * we have not been here before -> start building the descriptor.
         */
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
            LOG.error(e1.getMessage());
            return null;
        }

        /*
         * generate new ClassDescriptor, add it the loopCache and start
         * conversion
         */
        ClassDescriptorImpl classDesc = new ClassDescriptorImpl();
        _loopCache.put(type, classDesc);
        try {
            InfoToDescriptorConverter.convert(buildClassInfo, this
                    .getClassDescriptorResolver(), classDesc);

            /*
             * conversion successful => remove it from the loopCache and return
             * it
             */
            _loopCache.remove(type);
            return classDesc;
        } catch (MappingException e) {
            /*
             * conversion failed => remove it from the loopCache and return null
             */

            LOG.error(e.getMessage());
            _loopCache.remove(type);
            return null;
        }
    }
    
    //-----------------------------------------------------------------------------------
}

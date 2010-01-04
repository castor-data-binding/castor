/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.jdo.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;
import org.castor.jdo.jpa.processors.ReflectionsHelper;

/**
 * Processes the JPA annotation {@link OneToMany}. After this processor is done,
 * {@link JPAFieldNature#getRelationTargetEntity()},
 * {@link JPAFieldNature#getRelationMappedBy()},
 * {@link JPAFieldNature#isRelationLazyFetch()} and
 * {@link JPAFieldNature#isRelationOptional()} will return valid values.
 * 
 * @author Peter Schmidt
 * @version 05.02.2009
 * 
 */
public class JPAOneToManyProcessor extends BaseJPAAnnotationProcessor {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPAOneToManyProcessor.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return OneToMany.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, Annotation, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {
        if ((info instanceof JPAFieldNature)
                && (annotation instanceof OneToMany)
                && ((target instanceof Method) || (target instanceof Field))) {
            _log.debug("processing field annotation " + annotation.toString());

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            OneToMany oneToMany = (OneToMany) annotation;

            /*
             * @OneToMany.targetEntity
             */

            Class<?> collectionType;
            try {
                collectionType = ReflectionsHelper.getCollectionType(target,
                        true);
            } catch (AnnotationTargetException e) {
                _log.error(e.getMessage());
                throw e;
            }

            Class<?> targetEntity = oneToMany.targetEntity();
            if (void.class.equals(targetEntity)) {
                try {
                    targetEntity = ReflectionsHelper
                            .getTargetEntityFromGenerics(target);
                    if (targetEntity == null) {
                        // Error => no generics used!
                        String className = ((Member) target)
                                .getDeclaringClass().getName();
                        String targetName = ((Member) target).getName();
                        String message = "Target entity for OneToMany relation on "
                                + className
                                + "#"
                                + targetName
                                + " not specified - use generics or specify targetEntity!";
                        throw new AnnotationTargetException(message);

                    }
                } catch (AnnotationTargetException e) {
                    _log.error(e.getMessage());
                    throw e;
                }
            }

            jpaFieldNature.setRelationTargetEntity(targetEntity);
            jpaFieldNature.setRelationCollectionType(collectionType);

            /*
             * @OneToMany.cascade
             */
            if (oneToMany.cascade().length > 0) {
                _log.warn("Cascading of relations is not supported by Castor!");
            }

            /*
             * @OneToMany.fetch
             */
            jpaFieldNature.setRelationLazyFetch(false);
            if (oneToMany.fetch() == FetchType.LAZY) {
                jpaFieldNature.setRelationLazyFetch(true);
            }

            /*
             * @OneToMany.mappedBy
             */
            if (oneToMany.mappedBy().length() != 0) {
                jpaFieldNature.setRelationMappedBy(oneToMany.mappedBy());
            }

            jpaFieldNature.setOneToMany(true);

            return true;

        }

        return false;
    }
}

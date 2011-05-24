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
package org.castor.cpa.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link OneToOne}. After this processor is done,
 * {@link JPAFieldNature#getRelationTargetEntity()},
 * {@link JPAFieldNature#isRelationLazyFetch()} and
 * {@link JPAFieldNature#isRelationOptional()} will return valid values.
 * 
 * @author Peter Schmidt
 * @version 03.02.2009
 *
 */
public class JPAOneToOneProcessor extends BaseJPAAnnotationProcessor {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPAOneToOneProcessor.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return OneToOne.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, Annotation, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target) {
        if ((info instanceof JPAFieldNature)
                && (annotation instanceof OneToOne)
                && ((target instanceof Method) || (target instanceof Field))) {
            _log.debug("processing field annotation " + annotation.toString());
            OneToOne oneToOne = (OneToOne) annotation;

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            jpaFieldNature.setOneToOne(true);

            /*
             * @OneToOne.targetEntity
             */
            Class<?> targetEntity = null;
            if (void.class.equals(oneToOne.targetEntity())) {
                // Target Entity not set
                if (target instanceof Field) {
                    targetEntity = ((Field) target).getType();
                } else {
                    targetEntity = ((Method) target).getReturnType();
                }
            } else {
                // Target Entity is set
                targetEntity = oneToOne.targetEntity();
            }
            jpaFieldNature.setRelationTargetEntity(targetEntity);

            /*
             * @OneToOne.cascade
             */
            if (oneToOne.cascade().length > 0) {
                jpaFieldNature.setCascadeTypes(oneToOne.cascade());
            }

            /*
             * @OneToOne.fetch
             */
            jpaFieldNature.setRelationLazyFetch(false);
            if (oneToOne.fetch() == FetchType.LAZY) {
                jpaFieldNature.setRelationLazyFetch(true);
            }

            /*
             * @OneToOne.optional
             */
            if (!oneToOne.optional()) {
                _log
                        .warn("Checking of null values is not supported by Castor - "
                                + "the database has to check for null values!");
            }
            jpaFieldNature.setRelationOptional(oneToOne.optional());

            /*
             * @OneToOne.mappedBy
             */
            if (oneToOne.mappedBy().length() != 0) {
                _log
                        .error("Castor does not support inverse OneToOne relations!");
                /*
                 * TODO: Shall we ignore this error or throw an Exception?
                 */
            }

            return true;

        }
        return false;
    }

}

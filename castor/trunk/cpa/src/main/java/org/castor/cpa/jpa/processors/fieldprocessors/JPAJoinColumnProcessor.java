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
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link JoinColumn}. After this processor is
 * done, all joincolumn related methods of {@link JPAFieldNature} will return
 * valid values.
 * 
 * @author Peter Schmidt
 * @version 02.02.2009
 * 
 */
public class JPAJoinColumnProcessor extends BaseJPAAnnotationProcessor {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPAJoinColumnProcessor.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return JoinColumn.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target) {
        if ((info instanceof JPAFieldNature)
                && (annotation instanceof JoinColumn)
                && ((target instanceof Field) || (target instanceof Method))) {
            _log.debug("processing field annotation " + annotation.toString());

            boolean targetValid = false;
            if (target.getAnnotation(OneToOne.class) != null) {
                targetValid = true;
            }
            if (target.getAnnotation(ManyToOne.class) != null) {
                targetValid = true;
            }
            if (target.getAnnotation(OneToMany.class) != null) {
                targetValid = true;
            }
            if (!targetValid) {
                _log
                        .error("JoinTable annotation on "
                                + ((Member) target).getName()
                                + " is not valid! Needs a relationship annotation! Ignoring @JoinTable!");
                return false;
            }

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            JoinColumn joinColumn = (JoinColumn) annotation;
            jpaFieldNature.setJoinColumnName(joinColumn.name());
            jpaFieldNature.setJoinColumnReferencedColumnName(joinColumn
                    .referencedColumnName());
            jpaFieldNature.setJoinColumnUnique(joinColumn.unique());
            jpaFieldNature.setJoinColumnNullable(joinColumn.nullable());
            jpaFieldNature.setJoinColumnInsertable(joinColumn.insertable());
            jpaFieldNature.setJoinColumnUpdatable(joinColumn.updatable());
            jpaFieldNature.setJoinColumnColumnDefinition(joinColumn
                    .columnDefinition());
            jpaFieldNature.setJoinColumnTable(joinColumn.table());

            return true;
        }
        return false;
    }
}

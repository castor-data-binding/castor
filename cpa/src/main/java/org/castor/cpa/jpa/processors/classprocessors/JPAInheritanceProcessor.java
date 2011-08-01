/*
 * Copyright 2011 Werner Guttmann
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
package org.castor.cpa.jpa.processors.classprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import javax.persistence.Inheritance;

import javax.persistence.InheritanceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link Inheritance}. After this processor is done,
 * {@link JPAClassNature#getInheritanceStrategy()} returns a valid value!
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class JPAInheritanceProcessor extends BaseJPAAnnotationProcessor {
    //-----------------------------------------------------------------------------------
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getFactory().getInstance(
            JPAInheritanceProcessor.class);

    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public final Class<? extends Annotation> forAnnotationClass() {
        return Inheritance.class;
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

        if ((info instanceof JPAClassNature)
                && (annotation instanceof Inheritance)
                && (target instanceof Class<?>)
                && (target.isAnnotationPresent(Inheritance.class))) {
            LOG.debug("Processing class annotation " + annotation.toString());

            JPAClassNature jpaClassNature = (JPAClassNature) info;
            Inheritance inheritance = (Inheritance) annotation;
            InheritanceType strategy = inheritance.strategy();
            
            if (strategy != InheritanceType.JOINED) {
                throw new AnnotationTargetException(
                        "InheritanceType not supported: "
                                + strategy.toString());
            }
            
            jpaClassNature.setInheritanceStrategy(strategy);
            return true;
        }

        return false;
    }
    
    //-----------------------------------------------------------------------------------
}

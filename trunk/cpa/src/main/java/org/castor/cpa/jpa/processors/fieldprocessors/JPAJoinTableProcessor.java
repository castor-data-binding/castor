/*
 * Copyright 2006 Peter Schmidt, Martin Kandler, Werner Guttmann
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

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link JoinTable}. After this processor is done,
 * all jointable related methods will return valid values.
 * 
 * @author <a href="mailto:peter-list AT stayduebeauty DOT com">Peter Schmidt</a>
 * @author Martin Kandler
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class JPAJoinTableProcessor extends BaseJPAAnnotationProcessor {
    //-----------------------------------------------------------------------------------

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPAJoinTableProcessor.class);

    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return JoinTable.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {

        if ((info instanceof JPAFieldNature)
                && (annotation instanceof JoinTable)
                && ((target instanceof Field) || (target instanceof Method))) {
            _log.debug("processing field annotation " + annotation.toString());

            boolean targetValid = false;
            if (target.getAnnotation(OneToMany.class) != null) {
                targetValid = true;
            }
            if (target.getAnnotation(ManyToMany.class) != null) {
                targetValid = true;
            }
            if (!targetValid) {
                _log
                        .error("JoinTable annotation on "
                                + ((Member) target).getName()
                                + " is not valid! Needs a ManyToMany or unidiretional OneToMany "
                                + "relationship annotation! Ignoring @JoinTable!");
                return false;
            }

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            JoinTable joinTable = (JoinTable) annotation;

            jpaFieldNature.setJoinTableName(joinTable.name());
            jpaFieldNature.setJoinTableCatalog(joinTable.catalog());
            jpaFieldNature.setJoinTableSchema(joinTable.schema());
            jpaFieldNature.setJoinTableJoinColumns(joinTable.joinColumns());
            jpaFieldNature.setJoinTableInverseJoinColumns(joinTable
                    .inverseJoinColumns());

            if (joinTable.catalog().length() != 0) {
                _log
                        .warn("Castor does not support catalog definition for tables. "
                                + "Use global definition.");
            }
            if (joinTable.schema().length() != 0) {
                _log
                        .warn("Castor does not support schema definition for tables. "
                                + "Use global definition.");
            }
            if (joinTable.uniqueConstraints().length != 0) {
                _log
                        .warn("Castor does not support unique constraint definition for tables.");
            }

        }

        return false;
    }

    //-----------------------------------------------------------------------------------
}

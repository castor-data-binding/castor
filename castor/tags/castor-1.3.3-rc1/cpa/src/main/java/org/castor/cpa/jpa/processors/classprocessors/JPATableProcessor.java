/*
 * Copyright 2005 Martin Kandler, Werner Guttmann
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

import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link Table}. After this processor is done,
 * {@link JPAClassNature#getTableName()},
 * {@link JPAClassNature#getTableCatalog()} and
 * {@link JPAClassNature#getTableSchema()} return valid values!
 * 
 * @author Martin Kandler
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class JPATableProcessor extends BaseJPAAnnotationProcessor {
    //-----------------------------------------------------------------------------------
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getLog(JPATableProcessor.class);

    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return Table.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, Annotation, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target) {

        if ((info instanceof JPAClassNature) && (annotation instanceof Table)) {
            _log.debug("processing class annotation " + annotation.toString());

            JPAClassNature jpaClassNature = (JPAClassNature) info;
            Table table = (Table) annotation;
            jpaClassNature.setTableName(table.name());
            jpaClassNature.setTableCatalog(table.catalog());
            jpaClassNature.setTableSchema(table.schema());

            if (table.catalog().length() != 0) {
                _log.warn("Castor does not support catalog definition for tables. "
                        + "Use global definition.");
            }
            if (table.schema().length() != 0) {
                _log.warn("Castor does not support schema definition for tables. "
                        + "Use global definition.");
            }
            if (table.uniqueConstraints().length != 0) {
                _log.warn("Castor does not support unique constraint definition for tables.");
            }

            return true;
        }

        return false;

    }
    
    //-----------------------------------------------------------------------------------
}

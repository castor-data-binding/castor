/*
 * Copyright 2005 Peter Schmidt, Werner Guttmann
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

import javax.persistence.Column;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link Column}. After this processor is done,
 * all column specific methods of {@link JPAFieldNature} return valid values.
 * 
 * @author <a href="mailto:peter-list AT stayduebeauty DOT com">Peter Schmidt</a>
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class JPAColumnProcessor extends BaseJPAAnnotationProcessor {
    //-----------------------------------------------------------------------------------
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getLog(JPAColumnProcessor.class);

    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return Column.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, Annotation, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target) {
        if ((info instanceof JPAFieldNature) && (annotation instanceof Column)) {
            _log.debug("processing field annotation " + annotation.toString());

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            Column column = (Column) annotation;
            jpaFieldNature.setColumnName(column.name());
            jpaFieldNature.setColumnDefinition(column.columnDefinition());
            jpaFieldNature.setColumnLength(column.length());
            jpaFieldNature.setColumnInsertable(column.insertable());
            jpaFieldNature.setColumnNullable(column.nullable());
            jpaFieldNature.setColumnPrecision(column.precision());
            jpaFieldNature.setColumnScale(column.scale());
            jpaFieldNature.setColumnTable(column.table());
            jpaFieldNature.setColumnUnique(column.unique());
            jpaFieldNature.setColumnUpdatable(column.updatable());
            return true;
        }
        
        return false;
    }
    
    //-----------------------------------------------------------------------------------
}

/*
 * Copyright 2008 Werner Guttmann
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
package org.castor.cpa.jpa.info;

import org.castor.core.annotationprocessing.BaseTargetAwareAnnotationProcessingService;
import org.castor.cpa.jpa.processors.fieldprocessors.JPABasicProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAColumnProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAEnumeratedProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAGeneratedValueProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAIdProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAJoinColumnProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAJoinTableProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPALobProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAManyToManyProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAManyToOneProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAOneToManyProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAOneToOneProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPASequenceGeneratorFieldProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPATableGeneratorFieldProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPATemporalProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPATransientProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAVersionProcessor;

/**
 * This is a standard {@link BaseTargetAwareAnnotationProcessingService}
 * initialized with JPA Annotation processors for field bound JPA annotations.
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public class JPAFieldAnnotationProcessingService extends
        BaseTargetAwareAnnotationProcessingService {

    /**
     * Instantiate a {@link BaseTargetAwareAnnotationProcessingService} with JPA
     * Annotation processors for field bound JPA annotations.
     */
    public JPAFieldAnnotationProcessingService() {
        this.addAnnotationProcessor(new JPABasicProcessor());
        this.addAnnotationProcessor(new JPAColumnProcessor());
        this.addAnnotationProcessor(new JPAIdProcessor());
        this.addAnnotationProcessor(new JPAJoinColumnProcessor());
        this.addAnnotationProcessor(new JPAJoinTableProcessor());
        this.addAnnotationProcessor(new JPAManyToManyProcessor());
        this.addAnnotationProcessor(new JPAManyToOneProcessor());
        this.addAnnotationProcessor(new JPAOneToManyProcessor());
        this.addAnnotationProcessor(new JPAOneToOneProcessor());
        this.addAnnotationProcessor(new JPATransientProcessor());
        this.addAnnotationProcessor(new JPATemporalProcessor());
        this.addAnnotationProcessor(new JPALobProcessor());
        this.addAnnotationProcessor(new JPAEnumeratedProcessor());
        this.addAnnotationProcessor(new JPASequenceGeneratorFieldProcessor());
        this.addAnnotationProcessor(new JPAGeneratedValueProcessor());
        this.addAnnotationProcessor(new JPATableGeneratorFieldProcessor());
        this.addAnnotationProcessor(new JPAVersionProcessor());
    }

}

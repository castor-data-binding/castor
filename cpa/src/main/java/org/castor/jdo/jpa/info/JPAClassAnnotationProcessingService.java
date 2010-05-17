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
package org.castor.jdo.jpa.info;

import org.castor.core.annotationprocessing.BaseTargetAwareAnnotationProcessingService;
import org.castor.jdo.jpa.processors.classprocessors.JPACacheProcessor;
import org.castor.jdo.jpa.processors.classprocessors.JPAEntityProcessor;
import org.castor.jdo.jpa.processors.classprocessors.JPANamedNativeQueryProcessor;
import org.castor.jdo.jpa.processors.classprocessors.JPANamedQueryProcessor;
import org.castor.jdo.jpa.processors.classprocessors.JPANamedQueriesProcessor;
import org.castor.jdo.jpa.processors.classprocessors.JPATableProcessor;

/**
 * This is a standard {@link BaseTargetAwareAnnotationProcessingService}
 * initialized with JPA Annotation processors for class bound JPA annotations.
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public class JPAClassAnnotationProcessingService extends
        BaseTargetAwareAnnotationProcessingService {

    /**
     * Instantiate a {@link BaseTargetAwareAnnotationProcessingService} with JPA
     * Annotation processors for class bound JPA annotations.
     */
    public JPAClassAnnotationProcessingService() {
        this.addAnnotationProcessor(new JPAEntityProcessor());
        this.addAnnotationProcessor(new JPATableProcessor());
        this.addAnnotationProcessor(new JPANamedQueryProcessor());
        this.addAnnotationProcessor(new JPANamedNativeQueryProcessor());
        this.addAnnotationProcessor(new JPANamedQueriesProcessor());
        this.addAnnotationProcessor(new JPACacheProcessor());
    }

}

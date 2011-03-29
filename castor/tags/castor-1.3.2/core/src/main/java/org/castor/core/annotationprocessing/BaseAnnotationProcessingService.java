/*
 * Copyright 2007 Werner Guttmann
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
package org.castor.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.castor.core.nature.BaseNature;

/**
 * An AnnotationProcessingService handles multiple {@link AnnotationProcessor}s
 * and uses them to process one or more {@link Annotation}s. This is a standard
 * implementation that should be sufficient for most purposes.
 * 
 * @see AnnotationProcessingService
 * @author Alexander Eibner, Peter Schmidt
 * @since 1.3
 */

public class BaseAnnotationProcessingService implements
        AnnotationProcessingService {

    /**
     * List of annotation processors.
     */
    private Map<Class<? extends Annotation>, AnnotationProcessor> _annotationProcessorMap = new HashMap<Class<? extends Annotation>, AnnotationProcessor>();

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.annotationprocessing.AnnotationProcessingService#
     *      addAnnotationProcessor(AnnotationProcessor)
     */
    public void addAnnotationProcessor(
            final AnnotationProcessor annotationProcessor) {
        if (annotationProcessor != null) {
            _annotationProcessorMap.put(annotationProcessor
                    .forAnnotationClass(), annotationProcessor);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.annotationprocessing.AnnotationProcessingService#
     *      getAnnotationProcessors()
     */
    public Set<AnnotationProcessor> getAnnotationProcessors() {
        return new HashSet<AnnotationProcessor>(_annotationProcessorMap
                .values());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.annotationprocessing.AnnotationProcessingService#
     *      processAnnotations(BaseNature, Annotation[])
     */
    public <I extends BaseNature> Annotation[] processAnnotations(I info,
            final Annotation[] annotations) {
        ArrayList<Annotation> unprocessed = new ArrayList<Annotation>();

        for (int i = 0; i < annotations.length; i++) {
            if (processAnnotation(info, annotations[i]) == false) {
                unprocessed.add(annotations[i]);
            }
        }

        Annotation[] arrReturn = new Annotation[unprocessed.size()];

        return unprocessed.toArray(arrReturn);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.annotationprocessing.AnnotationProcessingService#processAnnotation
     *      (BaseNature, Annotation)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, final A annotation) {
        boolean processed = false;
        AnnotationProcessor annotationProcessor = _annotationProcessorMap
                .get(annotation.annotationType());
        if (annotationProcessor != null) {
            processed = annotationProcessor.processAnnotation(info, annotation);
        }
        return processed;
    }
}

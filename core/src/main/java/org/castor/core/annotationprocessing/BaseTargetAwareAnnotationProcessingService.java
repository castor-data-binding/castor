/*
 * Copyright 2009 Werner Guttmann
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
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.castor.core.nature.BaseNature;

/**
 * Base implementation class to be used for implementing {@link AnnotationProcessingService}s. 
 * 
 * @see AnnotationProcessingService
 * @author Alexander Eibner, Peter Schmidt
 * @since 1.3.1
 */
public class BaseTargetAwareAnnotationProcessingService extends
        BaseAnnotationProcessingService implements
        TargetAwareAnnotationProcessingService {

    /**
     * List of target aware annotation processors.
     */
    private Map<Class<? extends Annotation>, TargetAwareAnnotationProcessor> _taAnnotationProcessorMap = new HashMap<Class<? extends Annotation>, TargetAwareAnnotationProcessor>();

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.annotationprocessing.AnnotationProcessingService#
     *      addAnnotationProcessor(AnnotationProcessor)
     */
    public void addAnnotationProcessor(
            final TargetAwareAnnotationProcessor taAnnotationProcessor) {
        if (taAnnotationProcessor != null) {
            _taAnnotationProcessorMap.put(taAnnotationProcessor
                    .forAnnotationClass(), taAnnotationProcessor);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessingService#getAllAnnotationProcessors()
     */
    public Set<AnnotationProcessor> getAllAnnotationProcessors() {
        Set<AnnotationProcessor> result = new HashSet<AnnotationProcessor>(
                super.getAnnotationProcessors());
        result.addAll(this._taAnnotationProcessorMap.values());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessingService#getTargetAwareAnnotationProcessors()
     */
    public Set<TargetAwareAnnotationProcessor> getTargetAwareAnnotationProcessors() {
        Set<TargetAwareAnnotationProcessor> result = new HashSet<TargetAwareAnnotationProcessor>(
                this._taAnnotationProcessorMap.values());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessingService#processAnnotation(org.castor.core.nature.BaseNature,
     *      java.lang.annotation.Annotation, java.lang.reflect.AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, A annotation, AnnotatedElement target) throws AnnotationTargetException {
        boolean processed = false;
        TargetAwareAnnotationProcessor annotationProcessor = _taAnnotationProcessorMap
                .get(annotation.annotationType());
        if (annotationProcessor != null) {
            processed = annotationProcessor.processAnnotation(info, annotation,
                    target);
        }
        return processed;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessingService#processAnnotations(org.castor.core.nature.BaseNature,
     *      java.lang.annotation.Annotation[],
     *      java.lang.reflect.AnnotatedElement)
     */
    public <I extends BaseNature> Annotation[] processAnnotations(I info,
            Annotation[] annotations, AnnotatedElement target) throws AnnotationTargetException {
        ArrayList<Annotation> unprocessed = new ArrayList<Annotation>();

        for (int i = 0; i < annotations.length; i++) {
            if (processAnnotation(info, annotations[i], target) == false) {
                unprocessed.add(annotations[i]);
            }
        }

        Annotation[] arrReturn = new Annotation[unprocessed.size()];

        return unprocessed.toArray(arrReturn);
    }

    /**
     * This method acts like it's super method, but also tries to process the
     * annotation with the {@link TargetAwareAnnotationProcessor}s.
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.BaseAnnotationProcessingService#processAnnotation(org.castor.core.nature.BaseNature,
     *      java.lang.annotation.Annotation)
     */
    @Override
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, A annotation) {
        boolean superReturn = super.processAnnotation(info, annotation);
        if (!superReturn) {
            boolean processed = false;
            AnnotationProcessor annotationProcessor = _taAnnotationProcessorMap
                    .get(annotation.annotationType());
            if (annotationProcessor != null) {
                processed = annotationProcessor.processAnnotation(info,
                        annotation);
            }
            return processed;
        }
        return superReturn;
    }

    /**
     * This method acts like it's super method, but also tries to process the
     * annotations with the {@link TargetAwareAnnotationProcessor}s.
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.BaseAnnotationProcessingService#processAnnotations(org.castor.core.nature.BaseNature,
     *      java.lang.annotation.Annotation)
     */
    @Override
    public <I extends BaseNature> Annotation[] processAnnotations(I info,
            Annotation[] annotations) {
        Annotation[] superUnprocessed = super.processAnnotations(info,
                annotations);

        ArrayList<Annotation> unprocessed = new ArrayList<Annotation>();

        for (int i = 0; i < superUnprocessed.length; i++) {
            if (processAnnotation(info, superUnprocessed[i]) == false) {
                unprocessed.add(superUnprocessed[i]);
            }
        }

        Annotation[] arrReturn = new Annotation[unprocessed.size()];

        return unprocessed.toArray(arrReturn);
    }

}

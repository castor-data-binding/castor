/*
 * Copyright 2007 Joachim Grueneis
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
import java.util.Set;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

/**
 * An AnnotationProcessingService handles multiple {@link AnnotationProcessor}s
 * and uses them to process one or more {@link Annotation}s.
 * 
 * @see AnnotationProcessor
 * @author Alexander Eibner, Peter Schmidt, Joachim Grueneis
 * @since 1.3
 */
public interface AnnotationProcessingService {

    /**
     * Calls {@link #processAnnotation(BaseNature, Annotation)} for each given
     * Annotation.
     * 
     * @param info
     *            the {@link BaseNature} (and so its {@link PropertyHolder})
     *            that should be filled with the information read
     * @param annotations
     *            the annotations to process
     * @return Annotation[] filled with unprocessed annotations
     * @see #processAnnotation(BaseNature, Annotation)
     */
    public abstract <I extends BaseNature> Annotation[] processAnnotations(
            I info, final Annotation[] annotations);

    /**
     * The processing action of this service. If an annotation is given which is
     * not supported by this processor false is returned. Otherwise the
     * Annotations specific processor will (try to) process the Annotation and
     * the result of
     * {@link AnnotationProcessor#processAnnotation(BaseNature, Annotation)} is
     * returned.
     * 
     * @param info
     *            the {@link BaseNature} (and so its {@link PropertyHolder})
     *            that should be filled with the information read
     * @param annotation
     *            the annotation to process
     * @return true, if the annotation was processed, false if not
     * @see AnnotationProcessor
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, final A annotation);

    /**
     * Add an {@link AnnotationProcessor} to the service.
     * 
     * @param annotationProcessor
     *            the {@link AnnotationProcessor} to add to this service.
     */
    public void addAnnotationProcessor(
            final AnnotationProcessor annotationProcessor);

    /**
     * Returns the set of {@link AnnotationProcessor}s registered with 
     * this service.
     * @return A set of {@link AnnotationProcessor}s registered with this service.
     */
    public Set<AnnotationProcessor> getAnnotationProcessors();

}
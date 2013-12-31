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
import java.util.Set;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

/**
 * Extension of {@link AnnotationProcessingService} to handle target-aware 
 * {@link Annotation}s.
 * 
 * @see AnnotationProcessingService
 * @author Alexander Eibner, Peter Schmidt
 * @since 1.3.1
 */
public interface TargetAwareAnnotationProcessingService extends
        AnnotationProcessingService {

    /**
     * Calls {@link #processAnnotation(BaseNature, Annotation)} for each given
     * Annotation.
     * 
     * @param info
     *            the {@link BaseNature} (and so its {@link PropertyHolder})
     *            that should be filled with the information read
     * @param annotations
     *            the annotations to process
     * @param target
     *            the target ({@link AnnotatedElement}) of the given annotation
     * @return Annotation[] filled with unprocessed annotations
     * @throws AnnotationTargetException
     *             if an annotation is used in a context that is not valid.
     * @see #processAnnotation(BaseNature, Annotation)
     */
    public abstract <I extends BaseNature> Annotation[] processAnnotations(
            I info, final Annotation[] annotations,
            final AnnotatedElement target) throws AnnotationTargetException;

    /**
     * The processing action of this service. If an annotation is given which is
     * not supported by this processor false is returned. Otherwise the
     * Annotations specific processor will (try to) process the Annotation and
     * the result of
     * {@link TargetAwareAnnotationProcessor#processAnnotation(BaseNature, Annotation, AnnotatedElement)}
     * is returned.
     * 
     * @param info
     *            the {@link BaseNature} (and so its {@link PropertyHolder})
     *            that should be filled with the information read
     * @param annotation
     *            the annotation to process
     * @param target
     *            the target ({@link AnnotatedElement}) of the given annotation
     * @return true, if the annotation was processed, false if not
     * @throws AnnotationTargetException
     *             if an annotation is used in a context that is not valid.
     * @see TargetAwareAnnotationProcessor
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException;

    /**
     * Add an {@link TargetAwareAnnotationProcessor} to the service.
     * 
     * @param annotationProcessor
     *            the {@link TargetAwareAnnotationProcessor} to add to this
     *            service.
     */
    public void addAnnotationProcessor(
            final TargetAwareAnnotationProcessor annotationProcessor);

    /**
     * Returns the set of {@link TargetAwareAnnotationProcessor}s registered
     * with this service.
     * 
     * @return A set of {@link TargetAwareAnnotationProcessor}s registered with
     *         this service.
     */
    public Set<TargetAwareAnnotationProcessor> getTargetAwareAnnotationProcessors();

    /**
     * Returns the set of {@link AnnotationProcessor}s and
     * {@link TargetAwareAnnotationProcessor}s registered with this service.
     * 
     * @return A set of {@link AnnotationProcessor}s registered with this
     *         service.
     */
    public Set<AnnotationProcessor> getAllAnnotationProcessors();

}

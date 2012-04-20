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
import java.lang.reflect.Field;

import org.castor.core.nature.BaseNature;

/**
 * The interface each specific target-aware annotation processor has to fulfill.
 * 
 * @see AnnotationProcessor
 * @author Alexander Eibner, Peter Schmidt
 * @since 1.3.1
 */
public interface TargetAwareAnnotationProcessor extends AnnotationProcessor {

    /**
     * The processing action of this processor. If an annotation is given which
     * is not supported false is returned.
     * 
     * @param info
     *            the Info class that should be filled with the information read
     * @param annotation
     *            the annotation to process
     * @param target
     *            the target ({@link Field}, {@link Class}, etc.) of the given
     *            annotation
     * @return true, if the annotation was processed successfully, false if not
     * @throws AnnotationTargetException
     *             if an annotation is used in a context that is not valid.
     * @see AnnotatedElement
     */
    <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException;

}

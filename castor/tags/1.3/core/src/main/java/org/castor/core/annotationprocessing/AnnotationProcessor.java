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

import org.castor.core.nature.BaseNature;

/**
 * The interface each specific annotation processor has to fulfill.
 * 
 * @author Joachim Grueneis
 * @since 1.3
 */
public interface AnnotationProcessor {
    
    /**
     * Returns for which Annotation this processor is meant.
     * 
     * @return the Class for which this processor is meant
     */
    Class<? extends Annotation> forAnnotationClass();

    /**
     * The processing action of this processor. If an annotation is given which
     * is not supported false is returned.
     * 
     * @param info
     *            the Info class that should be filled with the information read
     * @param annotation
     *            the annotation to process
     * @return true, if the annotation was processed successfully, false if not
     */
    <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            I info, final A annotation);
}
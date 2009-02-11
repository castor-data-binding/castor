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

/**
 * This Exception is thrown when a {@link TargetAwareAnnotationProcessor} can
 * not process an annotation properly.
 * 
 * @author Peter Schmidt
 * @version 1.3.1
 * 
 * @see TargetAwareAnnotationProcessor
 * @see TargetAwareAnnotationProcessingService
 */
public class AnnotationTargetException extends Exception {

    /**
     * Creates an instance of {@link AnnotationTargetException}.
     */
    public AnnotationTargetException() {
        super();
    }

    /**
     * Creates an instance of {@link AnnotationTargetException}.
     * 
     * @param message
     *            Exception message.
     * @param cause
     *            The original cause.
     */
    public AnnotationTargetException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an instance of {@link AnnotationTargetException}.
     * 
     * @param message
     *            Exception message.
     */
    public AnnotationTargetException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link AnnotationTargetException}.
     * 
     * @param cause
     *            The original cause.
     */
    public AnnotationTargetException(Throwable cause) {
        super(cause);
    }

}

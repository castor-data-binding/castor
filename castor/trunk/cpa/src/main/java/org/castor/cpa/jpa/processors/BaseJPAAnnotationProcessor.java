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
package org.castor.cpa.jpa.processors;

import java.lang.annotation.Annotation;

import org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor;
import org.castor.core.nature.BaseNature;

/**
 * Basic abstract {@link TargetAwareAnnotationProcessor} that does not allow
 * processing without a target (returns false).
 * 
 * @author <a href="mailto:peter-list AT stayduebeauty DOT com">Peter Schmidt</a>
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @see AnnotationProcessor#processAnnotation(BaseNature, Annotation)
 */
public abstract class BaseJPAAnnotationProcessor implements
        TargetAwareAnnotationProcessor {
    //-----------------------------------------------------------------------------------

    /**
     * Returns false - non target aware processing shall not be implemented in
     * JPA-Annotation processing.
     * 
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#processAnnotation(BaseNature,
     *      Annotation)
     */
    public final <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation) {
        return false;
    }
    
    //-----------------------------------------------------------------------------------
}

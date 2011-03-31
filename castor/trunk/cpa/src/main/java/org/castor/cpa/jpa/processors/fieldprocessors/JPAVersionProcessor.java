/*
 * Copyright 2010 Werner Guttmann
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
package org.castor.cpa.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.persistence.Version;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.info.JPAVersionManager;
import org.castor.cpa.jpa.info.MultipleVersionFieldDefinitionException;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;
import org.castor.cpa.jpa.processors.ReflectionsHelper;

/**
 * Processes the JPA annotation {@link Version}. After this processor is done,
 * all column specific methods of {@link JPAFieldNature} return valid values.
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public class JPAVersionProcessor extends BaseJPAAnnotationProcessor {

    private static final Log LOG = LogFactory.getLog(JPAVersionProcessor.class);

    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing annotation " + annotation.toString());
        }

        if (!verifyArguments(annotation, target)) {
            return false;
        }

        return processGeneratorDefinition(annotation, target);
    }

    private <A> boolean verifyArguments(final A annotation, final AnnotatedElement target) {
        if (!(annotation instanceof Version)
                || (!target.isAnnotationPresent(Version.class))) {

            return false;
        }
        return true;

    }

    private <A> boolean processGeneratorDefinition(final A annotation,
            final AnnotatedElement target) {

        Method method = ((Method) target);
        Class<?> declaringClass = method.getDeclaringClass();
        JPAVersionManager manager = JPAVersionManager.getInstance();

        try {
            manager.add(declaringClass,
                    ReflectionsHelper.getFieldnameFromGetter(method));
        } catch (MultipleVersionFieldDefinitionException e) {
            return false;
        }

        return true;

    }

    public Class<? extends Annotation> forAnnotationClass() {
        return Version.class;
    }

}

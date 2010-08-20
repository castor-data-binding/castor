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
package org.castor.jdo.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link GeneratedValue}. After this processor is done,
 * all column specific methods of {@link JPAFieldNature} return valid values.
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public class JPAGeneratedValueProcessor extends
		BaseJPAAnnotationProcessor {

	private static final Log LOG = LogFactory
			.getLog(JPAGeneratedValueProcessor.class);

	public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
			I info, A annotation, AnnotatedElement target)
			throws AnnotationTargetException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Processing annotation " + annotation.toString());
		}

		if (!verifyArguments(info, annotation, target)) {
			return false;
		}

		processGeneratorDefinition(info, annotation);

		return true;
	}

	private <I, A> boolean verifyArguments(I info, A annotation,
			AnnotatedElement target) {
		if (!(annotation instanceof GeneratedValue)
				|| (!target.isAnnotationPresent(GeneratedValue.class))
				|| (!target.isAnnotationPresent(Id.class))
				|| !(info instanceof JPAFieldNature)) {
			return false;
		}
		return true;
	}

	private <I, A> void processGeneratorDefinition(I nature, A annotation) {
		GeneratedValue generatedValue = (GeneratedValue) annotation;

		((JPAFieldNature) nature).setGeneratedValueStrategy(generatedValue.strategy());
		((JPAFieldNature) nature).setGeneratedValueGenerator(generatedValue.generator());

	}

	public Class<? extends Annotation> forAnnotationClass() {
		return GeneratedValue.class;
	}

}

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

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.info.GeneratorNameAlreadyUsedException;
import org.castor.jdo.jpa.info.JPAKeyGeneratorManager;
import org.castor.jdo.jpa.info.JPASequenceGeneratorDescriptor;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link SequenceGenerator}. After this processor is done,
 * all column specific methods of {@link JPAFieldNature} return valid values.
 * 
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @since 1.3.2
 */
public class JPASequenceGeneratorFieldProcessor extends
		BaseJPAAnnotationProcessor {

	private static final Log LOG = LogFactory
			.getLog(JPASequenceGeneratorFieldProcessor.class);

	public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
			I info, A annotation, AnnotatedElement target)
			throws AnnotationTargetException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Processing annotation " + annotation.toString());
		}

		if (!verifyArguments(annotation, target)) {
			return false;
		}

		return processGeneratorDefinition(annotation);
	}

	/**
	 * Checks whether all required annotations (as per specification) are present.
	 * 
	 * @param <A> 
	 * @param annotation The actual annotation.
	 * @param target The target of the given annotation.
	 * @return True if all requirements are satisfied.
	 */
	private <A> boolean verifyArguments(A annotation,
			AnnotatedElement target) {
		if (!(annotation instanceof SequenceGenerator)
				|| (!target.isAnnotationPresent(SequenceGenerator.class))
				|| (!target.isAnnotationPresent(Id.class))) {
			return false;
		}
		return true;
	}

	private <A> boolean processGeneratorDefinition(A annotation) {
		SequenceGenerator sequenceGenerator = (SequenceGenerator) annotation;
		
		JPAKeyGeneratorManager manager = JPAKeyGeneratorManager
				.getInstance();

		JPASequenceGeneratorDescriptor descriptor = JPASequenceGeneratorDescriptor.extract(sequenceGenerator);
		
		try {
			manager.add(sequenceGenerator.name(), descriptor);
		} catch (GeneratorNameAlreadyUsedException e) {
			return false;
		}
		
		return true;
	}

	public Class<? extends Annotation> forAnnotationClass() {
		return SequenceGenerator.class;
	}

}

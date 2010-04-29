/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.jdo.jpa.processors.classprocessors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;

import javax.persistence.NamedQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Processes the JPA annotation {@link NamedQuery}. After this processor is
 * done, {@link JPAClassNature#getNamedQuery()} returns a valid value!
 */
public class JPANamedQueryProcessor extends BaseJPAAnnotationProcessor {

	private final Log LOG = LogFactory.getLog(getClass());

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.castor.core.annotationprocessing.AnnotationProcessor#
	 *      forAnnotationClass()
	 */
	public Class<? extends Annotation> forAnnotationClass() {
		return NamedQuery.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
	 *      processAnnotation(BaseNature, Annotation, AnnotatedElement)
	 */
	public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
			final I info, final A annotation, final AnnotatedElement target)
			throws AnnotationTargetException {
		if ((info instanceof JPAClassNature)
				&& (annotation instanceof NamedQuery)
				&& (target instanceof Class<?>)) {
			LOG.debug("Processing class annotation " + annotation.toString());
			final JPAClassNature jpaClassNature = (JPAClassNature) info;
			final NamedQuery namedQuery = (NamedQuery) annotation;
			final Map<String, String> namedQueryMap = new HashMap<String, String>();
			namedQueryMap.put(namedQuery.name(), namedQuery.query());
			jpaClassNature.setNamedQuery(namedQueryMap);
			return true;
		}
		return false;
	}

}

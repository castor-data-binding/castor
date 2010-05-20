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
package org.castor.jdo.jpa.processors.classprocessors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;

import javax.persistence.NamedNativeQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.NamedNativeQueries;

public class JPANamedNativeQueriesProcessor extends BaseJPAAnnotationProcessor {

    private static final Log LOG = LogFactory.getFactory().getInstance(
            JPANamedNativeQueryProcessor.class);

    public Class<? extends Annotation> forAnnotationClass() {
        return NamedNativeQueries.class;
    }

    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {
        if ((info instanceof JPAClassNature)
                && (annotation instanceof NamedNativeQueries)
                && (target instanceof Class<?>)
                && (target.isAnnotationPresent(NamedNativeQueries.class))) {
            LOG.debug("Processing class annotation " + annotation.toString());

            final JPAClassNature jpaClassNature = (JPAClassNature) info;
            final NamedNativeQueries namedNativeQueries = (NamedNativeQueries) annotation;
            final NamedNativeQuery[] namedNativeQueryValues = namedNativeQueries
                    .value();
            final Map<String, String> namedNativeQueryMap = new HashMap<String, String>();

            if (namedNativeQueryValues != null
                    && namedNativeQueryValues.length > 0) {
                for (NamedNativeQuery query : namedNativeQueryValues) {
                    namedNativeQueryMap.put(query.name(), query.query());
                }
            }
            jpaClassNature.setNamedNativeQuery(namedNativeQueryMap);
            return true;
        }
        return false;
    }

}
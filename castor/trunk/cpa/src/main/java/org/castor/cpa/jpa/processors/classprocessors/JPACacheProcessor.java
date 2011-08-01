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
package org.castor.cpa.jpa.processors.classprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.annotations.Cache;
import org.castor.cpa.jpa.annotations.CacheProperty;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class JPACacheProcessor extends BaseJPAAnnotationProcessor {
    //-----------------------------------------------------------------------------------
    
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPACacheProcessor.class);

    //-----------------------------------------------------------------------------------
    
    /**
     * {@inheritDoc}
     */
    public final <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {

        if ((info instanceof JPAClassNature) && (annotation instanceof Cache)
                && (target instanceof Class<?>)
                && (target.isAnnotationPresent(Cache.class))) {
            _log.debug("processing class annotation " + annotation.toString());

            JPAClassNature jpaClassNature = (JPAClassNature) info;

            Cache cache = (Cache) annotation;
            CacheProperty[] annotatedProperties = cache.value();
            Properties plainProperties = new Properties();
            for (CacheProperty property : annotatedProperties) {
                plainProperties.setProperty(property.key(), property.value());
            }
            jpaClassNature.setCacheProperties(plainProperties);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public final Class<? extends Annotation> forAnnotationClass() {
        return Cache.class;
    }

    //-----------------------------------------------------------------------------------
}

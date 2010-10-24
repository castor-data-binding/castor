package org.castor.cpa.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import javax.persistence.Temporal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

public class JPATemporalProcessor extends BaseJPAAnnotationProcessor {

    private final Log _log = LogFactory.getLog(this.getClass());

    public Class<? extends Annotation> forAnnotationClass() {
        return Temporal.class;
    }

    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {
        if ((info instanceof JPAFieldNature)
                && (annotation instanceof Temporal)
                && target.isAnnotationPresent(Temporal.class)) {
            _log.debug("processing field annotation " + annotation.toString());

            final JPAFieldNature jpaFieldNature = (JPAFieldNature) info;
            final Temporal temporal = (Temporal) annotation;
            jpaFieldNature.setTemporalType(temporal.value());
            return true;
        }
        return false;
    }

}

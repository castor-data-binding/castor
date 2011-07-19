package org.castor.cpa.jpa.processors.fieldprocessors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author <a href=" mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class JPAEnumeratedProcessor extends BaseJPAAnnotationProcessor {
    //-----------------------------------------------------------------------------------

    private final Log _log = LogFactory.getLog(getClass());
    
    //-----------------------------------------------------------------------------------

    public Class<? extends Annotation> forAnnotationClass() {
        return Enumerated.class;
    }

    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {
        if ((info instanceof JPAFieldNature)
                && (annotation instanceof Enumerated)
                && target.isAnnotationPresent(Enumerated.class)) {
            _log.debug("processing field annotation " + annotation.toString());

            final JPAFieldNature jpaFieldNature = (JPAFieldNature) info;
            if (((Enumerated) annotation).value() == EnumType.STRING) {
                jpaFieldNature.setStringEnumType(true);
            }
            return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------------------
}

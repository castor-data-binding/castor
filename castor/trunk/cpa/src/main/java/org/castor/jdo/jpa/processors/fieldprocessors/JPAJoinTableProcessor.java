package org.castor.jdo.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;

/**
 * Processes the JPA annotation {@link JoinTable}. After this processor is done,
 * all jointable related methods will return valid values.
 * 
 * @author Peter Schmidt, Martin Kandler
 * @version 10.02.2009
 * 
 */
public class JPAJoinTableProcessor extends BaseJPAAnnotationProcessor {

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPAJoinTableProcessor.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return JoinTable.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.TargetAwareAnnotationProcessor#
     *      processAnnotation(BaseNature, AnnotatedElement)
     */
    public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
            final I info, final A annotation, final AnnotatedElement target)
            throws AnnotationTargetException {

        if ((info instanceof JPAFieldNature)
                && (annotation instanceof JoinTable)
                && ((target instanceof Field) || (target instanceof Method))) {
            _log.debug("processing field annotation " + annotation.toString());

            boolean targetValid = false;
            if (target.getAnnotation(OneToMany.class) != null) {
                targetValid = true;
            }
            if (target.getAnnotation(ManyToMany.class) != null) {
                targetValid = true;
            }
            if (!targetValid) {
                _log
                        .error("JoinTable annotation on "
                                + ((Member) target).getName()
                                + " is not valid! Needs a ManyToMany or unidiretional OneToMany "
                                + "relationship annotation! Ignoring @JoinTable!");
                return false;
            }

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            JoinTable joinTable = (JoinTable) annotation;

            jpaFieldNature.setJoinTableName(joinTable.name());
            /*
             * TODO: default f�r name
             */
            jpaFieldNature.setJoinTableCatalog(joinTable.catalog());
            jpaFieldNature.setJoinTableSchema(joinTable.schema());
            jpaFieldNature.setJoinTableJoinColumns(joinTable.joinColumns());
            jpaFieldNature.setJoinTableInverseJoinColumns(joinTable
                    .inverseJoinColumns());

            if (joinTable.catalog().length() != 0) {
                _log
                        .warn("Castor does not support catalog definition for tables. "
                                + "Use global definition.");
            }
            if (joinTable.schema().length() != 0) {
                _log
                        .warn("Castor does not support schema definition for tables. "
                                + "Use global definition.");
            }
            if (joinTable.uniqueConstraints().length != 0) {
                _log
                        .warn("Castor does not support unique constraint definition for tables.");
            }

        }

        return false;
    }

}

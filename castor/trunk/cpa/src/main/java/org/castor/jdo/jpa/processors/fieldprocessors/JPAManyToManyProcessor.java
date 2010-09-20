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
package org.castor.jdo.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.nature.BaseNature;
import org.castor.jdo.jpa.info.FieldInfo;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;
import org.castor.jdo.jpa.processors.ReflectionsHelper;

/**
 * Processes the JPA annotation {@link ManyToMany}. This is the most complex
 * processor. After this processor is done, we know, that we have 2 fields that
 * have a well defined relation to each other. This processor checks for target
 * entities, relationship owning and checks that there is at least one join
 * definition for this relation.
 * 
 * After this processor is done, all 5 relation linked methods (and of cource
 * the Many2Many related ones) of {@link JPAFieldNature} will return valid
 * values.
 * 
 * @author Peter Schmidt
 * @version 05.02.2009
 * 
 */
public class JPAManyToManyProcessor extends BaseJPAAnnotationProcessor {

    /**
     * This enumeration is used to determine the origin of JoinTable
     * information.
     * 
     * @author Peter Schmidt
     * @version 12.02.2009
     */
    private enum JoinTableStrategy {
        /**
         * copy the information from the related property but invert the
         * JoinColumns.
         */
        inverseCopy,

        /**
         * create default mappings - this property is the relations owner.
         */
        defaultWeOwn,

        /**
         * create default mappings - the other property is the relations owner.
         */
        defaultHeOwns,

        /**
         * do nothing - a {@link JoinTable} annotation exists that will be
         * processed by another processor.
         */
        nothing
    }

    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(
            JPAManyToManyProcessor.class);

    /**
     * The value of {@link ManyToMany#mappedBy()}.
     */
    private String _mappedBy;

    /**
     * @param _fieldInfo
     *            The {@link FieldInfo} of this field.
     */
    private FieldInfo _fieldInfo;

    /**
     * The target of the annotation.
     */
    private AnnotatedElement _target;

    /**
     * {@inheritDoc}
     * 
     * @see org.castor.core.annotationprocessing.AnnotationProcessor#forAnnotationClass()
     */
    public Class<? extends Annotation> forAnnotationClass() {
        return ManyToMany.class;
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
        if ((info instanceof JPAFieldNature)
                && (annotation instanceof ManyToMany)
                && ((target instanceof Method) || (target instanceof Field))) {
            _log.debug("processing field annotation " + annotation.toString());

            JPAFieldNature jpaFieldNature = (JPAFieldNature) info;

            ManyToMany manyToMany = (ManyToMany) annotation;

            /*
             * try generating values
             */
            this._mappedBy = manyToMany.mappedBy();

            this._fieldInfo = jpaFieldNature.getFieldInfo();

            this._target = target;

            // target Entity and collectionType
            Class<?> collectionType;
            try {
                collectionType = ReflectionsHelper.getCollectionType(
                        this._target, true);
            } catch (AnnotationTargetException e) {
                _log.error(e.getMessage());
                throw e;
            }

            Class<?> targetEntity = manyToMany.targetEntity();
            if (void.class.equals(targetEntity)) {
                try {
                    targetEntity = ReflectionsHelper
                            .getTargetEntityFromGenerics(this._target);
                    if (targetEntity == null) {
                        // Error => no generics used!
                        String className = ((Member) this._target)
                                .getDeclaringClass().getName();
                        String targetName = ((Member) this._target).getName();
                        String message = "Target entity for ManyToMany relation on "
                                + className
                                + "#"
                                + targetName
                                + " not specified - use generics or specify targetEntity!";
                        throw new AnnotationTargetException(message);

                    }
                } catch (AnnotationTargetException e) {
                    _log.error(e.getMessage());
                    throw e;
                }
            }

            // related properties GET method

            Method otherPropertiesGetter;
            try {
                otherPropertiesGetter = this
                        .getRelationOtherGetMethod(targetEntity);
            } catch (AnnotationTargetException e) {
                _log.error(e.getMessage());
                throw e;
            }

            // joinTableStrategie => where to get JoinTable information
            JoinTableStrategy joinTableStrategy = this
                    .getJoinTableStrategy(otherPropertiesGetter);

            switch (joinTableStrategy) {
            case defaultHeOwns:
            case defaultWeOwn:
                AnnotationTargetException e = new AnnotationTargetException(
                        "Default values for ManyToMany relations are not supported by Castor!");
                _log.error(e.getMessage());
                throw e;
            case inverseCopy:
                jpaFieldNature.setManyToManyInverseCopy(true);
                break;
            default:
                break;
            }

            /*
             * all values are generated past this point
             */

            /*
             * @ManyToMany.targetEntity
             */

            jpaFieldNature.setRelationTargetEntity(targetEntity);
            jpaFieldNature.setRelationCollectionType(collectionType);

            /*
             * @ManyToMany.cascade
             */
            if (manyToMany.cascade().length > 0) {
                jpaFieldNature.setCascadeTypes(manyToMany.cascade());
            }

            /*
             * @ManyToMany.fetch
             */
            jpaFieldNature.setRelationLazyFetch(false);
            if (manyToMany.fetch() == FetchType.LAZY) {
                jpaFieldNature.setRelationLazyFetch(true);
            }

            /*
             * @ManyToMany.mappedBy
             */
            if (_mappedBy.length() != 0) {
                jpaFieldNature.setRelationMappedBy(_mappedBy);
            }

            jpaFieldNature.setManyToMany(true);
            return true;

        }

        return false;
    }

    /**
     * Get the getter method of the related property using reflection.
     * 
     * @param otherClass
     *            The Class object representing the target entity.
     * @return The getter {@link Method} accessing the related field or NULL if
     *         this relation is unidirectional.
     * @throws AnnotationTargetException
     *             If {@link ManyToMany#mappedBy()} refers to a field that is
     *             not related with this field or the getter Method could not be
     *             found (the property does not exist at all).
     */
    private Method getRelationOtherGetMethod(final Class<?> otherClass)
            throws AnnotationTargetException {

        Class<?> describedClass = _fieldInfo.getDeclaringClassInfo()
                .getDescribedClass();
        String fieldName = _fieldInfo.getFieldName();
        if ((_mappedBy != null) && (_mappedBy.length() != 0)) {
            // we know which property is related to us
            // the other side is an owner => this is bidirectional

            String propertyName = _mappedBy.substring(0, 1).toUpperCase()
                    + _mappedBy.substring(1);
            String methodName = "get" + propertyName;

            try {
                // check if our mappedBy can be a valid relational partner

                // does method exist?
                Method otherMethod = otherClass.getMethod(methodName,
                        new Class<?>[0]);
                // does it have a ManyToMany with us as targetEntity?
                Class<?> targetEntityFromGenerics = ReflectionsHelper
                        .getTargetEntityFromGenerics(otherMethod);
                if (describedClass.equals(targetEntityFromGenerics)) {
                    return otherMethod;
                }
                throw new AnnotationTargetException("MappedBy '" + _mappedBy
                        + "' in Class " + otherClass.getName()
                        + " is not ManyToMany related with '"
                        + describedClass.getName() + "' property '" + fieldName
                        + "'!");

            } catch (AnnotationTargetException e) {
                throw e;
            } catch (Exception e) {
                throw new AnnotationTargetException("MappedBy '" + _mappedBy
                        + "' does not exist in Class " + otherClass.getName()
                        + " (could not find method '" + methodName + "')!");
            }
        }

        // we don't know which property is related to us
        // the other side is not an owner

        for (Method otherMethod : otherClass.getMethods()) {
            // search through all GET methods
            if (!otherMethod.getName().startsWith("get")) {
                continue;
            }

            if (checkMappedByToTarget(otherMethod, describedClass, fieldName)) {
                return otherMethod;
            }
        }

        // the relation is unidirectional!
        return null;
    }

    /**
     * Little helper to check if a {@link AnnotatedElement} ({@link Field} or
     * {@link Method}) has a {@link ManyToMany} annotation that is mapped by the
     * given field (Class and name).
     * 
     * @param property
     *            The property to check
     * @param targetClass
     *            The Class that should be targetEntity of the given property
     * @param targetProperty
     *            The name of the field (mapped by the given property)
     * @return true iff the given property has a {@link ManyToMany} annotation
     *         with targetEntity referreing to targetClass and is mapped by the
     *         given field name targetProperty.
     * @throws AnnotationTargetException
     *             if property does not define a targetEntity and is not generic
     *             or the generic definition is not sufficient
     */
    private boolean checkMappedByToTarget(final AnnotatedElement property,
            final Class<?> targetClass, final String targetProperty)
            throws AnnotationTargetException {
        ManyToMany otherManyToMany = property.getAnnotation(ManyToMany.class);
        if (otherManyToMany != null) {
            // if property has manytomany relation, get its targetEntity

            Class<?> otherTargetEntity = otherManyToMany.targetEntity();
            if (void.class.equals(otherTargetEntity)) {
                otherTargetEntity = ReflectionsHelper
                        .getTargetEntityFromGenerics(property);
                if (otherTargetEntity == null) {
                    // Error => no generics used!
                    String className = ((Member) property).getDeclaringClass()
                            .getName();
                    String targetName = ((Member) property).getName();
                    String message = "Target entity for ManyToMany relation on "
                            + className
                            + "#"
                            + targetName
                            + " not specified - use generics or specify targetEntity!";
                    throw new AnnotationTargetException(message);
                }
            }

            if (otherTargetEntity.equals(targetClass)) {
                // if our entity is the targetEntity
                if (targetProperty.equals(otherManyToMany.mappedBy())) {
                    // if our field is the owning field
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Analyse the relation and determine where to get JoinTable information
     * from.
     * 
     * @param otherPropertiesGetter
     *            The getter Method of the related field.
     * @return The {@link JoinTableStrategy} representing what to do.
     * @throws AnnotationTargetException
     *             If a {@link JoinTable} was found on a non-owning side or both
     *             side are owner and no {@link JoinTable} was found at all
     *             (defaults can not be generated then).
     * 
     * @see {@link JoinTableStrategy}
     */
    private JoinTableStrategy getJoinTableStrategy(
            final Method otherPropertiesGetter)
            throws AnnotationTargetException {
        if (otherPropertiesGetter != null) {
            // bi-directional relation

            String otherFieldName = ReflectionsHelper
                    .getFieldnameFromGetter(otherPropertiesGetter);
            String otherMappedBy = otherPropertiesGetter.getAnnotation(
                    ManyToMany.class).mappedBy();

            boolean weOwn = this._fieldInfo.getFieldName()
                    .equals(otherMappedBy);
            boolean heOwns = otherFieldName.equals(_mappedBy);

            if (_target.getAnnotation(JoinTable.class) != null) {
                // we have a JoinTable definition => we MUST be an owner =>
                // check that!

                if (weOwn) {
                    // we are an owner! => JoinTable is valid here... do nothing
                    return JoinTableStrategy.nothing;
                }
                // we are not an owner, but we have a JoinTable definition =>
                // ERROR
                String message = "JoinTable definition on a non-owning side ("
                        + this._fieldInfo.getFieldName() + ") is not valid!";
                _log.error(message);
                throw new AnnotationTargetException(message);
            }

            // we have no JoinTable definition

            if (otherPropertiesGetter.getAnnotation(JoinTable.class) != null) {
                // other side has JoinTable definition => do nothing
                if (heOwns) {
                    return JoinTableStrategy.inverseCopy;
                }
                // he is not an owner, but has a JoinTable definition => ERROR
                String message = "JoinTable definition on a non-owning side ("
                        + _mappedBy + ") is not valid!";
                _log.error(message);
                throw new AnnotationTargetException(message);

            }

            // there is no JoinTable definition at all!

            if (heOwns && !weOwn) {
                // other side is the only owner => we take his defaults
                return JoinTableStrategy.defaultHeOwns;
            } else if (!heOwns && weOwn) {
                // we are the only owner => we take our defaults
                return JoinTableStrategy.defaultWeOwn;
            }

            // 2 owners but no JoinTable => ERROR!
            String message = "Can not create default mapping if both entities ('"
                    + _fieldInfo.getDeclaringClassInfo().getDescribedClass()
                            .getName()
                    + "' and '"
                    + otherPropertiesGetter.getDeclaringClass().getName()
                    + "')are owner!";
            _log.error(message);
            throw new AnnotationTargetException(message);

        }

        // uni-directional relation

        // we are the only owner => we need @JoinTable OR MappingDefaults
        if (!(_target.getAnnotation(JoinTable.class) != null)) {
            // generate mapping defaults; we are owner
            return JoinTableStrategy.defaultWeOwn;
        }

        // we are owner and have a JoinTable definition... do nothing
        return JoinTableStrategy.nothing;
    }

}

/*
 * Copyright 2008 Werner Guttmann
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
package org.castor.cpa.jpa.info;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.persistence.Entity;

import javax.persistence.MappedSuperclass;
import org.castor.core.annotationprocessing.AnnotationTargetException;
import org.castor.core.annotationprocessing.TargetAwareAnnotationProcessingService;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.ReflectionsHelper;
import org.exolab.castor.mapping.MappingException;

/**
 * Uses a JPA annotated {@link Class} to build a {@link ClassInfo} and
 * {@link FieldInfo}s of it and parse the mapping information in them.
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public final class ClassInfoBuilder {

    /**
     * The {@link TargetAwareAnnotationProcessingService} for class related
     * annotations.
     */
    private static TargetAwareAnnotationProcessingService _classAnnotationProcessingService = 
        new JPAClassAnnotationProcessingService();
    /**
     * The {@link TargetAwareAnnotationProcessingService} for field related
     * annotations.
     */
    private static TargetAwareAnnotationProcessingService _fieldAnnotationProcessingService = 
        new JPAFieldAnnotationProcessingService();

    /**
     * Do not allow instances of utility classes.
     */
    private ClassInfoBuilder() {
    }

    /**
     * Builds a new {@link ClassInfo} describing the given Class. Annotations
     * for the class and its fields are read using the
     * {@link TargetAwareAnnotationProcessingService}s defined by
     * {@link #setClassAnnotationProcessingService(TargetAwareAnnotationProcessingService)}
     * and
     * {@link #setFieldAnnotationProcessingService(TargetAwareAnnotationProcessingService)}
     * . The information is stored in the {@link ClassInfo} and its related
     * {@link FieldInfo}s.
     * 
     * @param type
     *            The Class Object representing the Class that shall be
     *            described.
     * @return a new {@link ClassInfo} describing the given Class or null if the
     *         given type was not describable.
     * @throws MappingException
     *             if annotation placement is invalid (field and property access
     *             for the same field) or if composite keys are used!
     */
    public static ClassInfo buildClassInfo(final Class<?> type)
            throws MappingException {

        if (type == null) {
            throw new IllegalArgumentException("Argument type must not be null");
        }

         /*
          * get and return classInfo from ClassInfoRegistry, if already generated
          */
        ClassInfo classInfo = ClassInfoRegistry.getClassInfo(type);
        if (classInfo != null) {
            return classInfo;
        }
        
        if (!isDescribable(type)) {
            return null;
        }

        /*
         * create new ClassInfo and Nature
         */
        classInfo = new ClassInfo(type);
        classInfo.addNature(JPAClassNature.class.getName());
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        /*
         * process class annotations
         */
        try {
            _classAnnotationProcessingService.processAnnotations(jpaClassNature,
                    type.getAnnotations(), type);
        } catch (AnnotationTargetException e) {
            throw new MappingException(
                    "Could not process class bound annotations for class "
                            + type.getSimpleName(), e);
        }

        /*
         * process annotations for all declared (not inherited) fields =>
         * fieldAccess. This is not supported by castor and so a mapping
         * exception is thrown
         */
        for (Field field : type.getDeclaredFields()) {
            if (field.getAnnotations().length != 0) {

                if (hasJPAAnnotations(field)) {
                    throw new MappingException(
                            "Castor does not support field access, thus annotated fields are "
                                    + "not supported! Move annotations to the getter method of "
                                    + field.getName());
                }
            }
        }

        /*
         * process annotations for all declared (not inherited) getter methods
         */

        for (Method method : type.getDeclaredMethods()) {
            if (ReflectionsHelper.isGetter(method)) {
                if (isDescribable(type, method)) {
                    buildFieldInfo(classInfo, method);
                } else {
                    throw new MappingException(
                            "Invalid method annotated, method is not describeable!");
                }
            }
        }

        if (classInfo.getKeyFieldCount() > 1) {
            // Castor JPA does not support composite keys
            throw new MappingException(
                    "Castor-JPA does not support composite keys (found in "
                            + type.getName() + ")");
        }

        /*
         * register ClassInfo in Registry
         */
        ClassInfoRegistry.registerClassInfo(type, classInfo);
        return classInfo;
    }

    /**
     * Checks if the {@link AnnotatedElement} has any Annotations of the Package
     * javax.persistence (JPA related annotations).
     * 
     * @param annotatedElement
     *            The {@link AnnotatedElement} (Field, Method, Class) to be
     *            checked
     * @return true if any JPA annotations are found.
     */
    private static boolean hasJPAAnnotations(final AnnotatedElement annotatedElement) {
        for (Annotation annotation : annotatedElement.getAnnotations()) {
            Class<? extends Annotation> annotationClass = annotation.annotationType();
            if (annotationClass.getPackage().equals(Package.getPackage("javax.persistence"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Build a {@link FieldInfo} describing the field (accessed by the given
     * {@link Method}) by processing its annotations and add the generated
     * {@link FieldInfo} to the given {@link ClassInfo} (as field or key).
     * 
     * @param classInfo
     *            the {@link ClassInfo} of the declaring class
     * @param method
     *            the {@link Method} used to access (get) the underlying member.
     * @throws MappingException
     *             if a FieldInfo with the same name already exists (usually
     *             when using field AND property access).
     */
    private static void buildFieldInfo(final ClassInfo classInfo, final Method method)
            throws MappingException {
        if (classInfo == null) {
            throw new IllegalArgumentException(
                    "Argument classInfo must not be null.");
        }
        if (method == null) {
            throw new IllegalArgumentException(
                    "Argument method must not be null.");
        }
        String fieldName = ReflectionsHelper.getFieldnameFromGetter(method);
        if (fieldName == null) {
            throw new IllegalArgumentException(
                    "Can not resolve Fieldname from method name.");
        }

        Method setterMethod = null;
        try {
            setterMethod = ReflectionsHelper.getSetterMethodFromGetter(method);
        } catch (SecurityException e) {
            throw new MappingException("Setter method for field " + fieldName
                    + " is not accessible!");
        } catch (NoSuchMethodException e) {
            throw new MappingException("Setter method for field " + fieldName
                    + " does not exist!", e);
        }
        Class<?> fieldType = method.getReturnType();
        
        FieldInfo fieldInfo = new FieldInfo(classInfo, fieldType, fieldName, method, setterMethod);

        fieldInfo.addNature(JPAFieldNature.class.getName());
        JPAFieldNature jpaFieldNature = new JPAFieldNature(fieldInfo);
        try {
            _fieldAnnotationProcessingService.processAnnotations(jpaFieldNature,
                    method.getAnnotations(), method);
        } catch (AnnotationTargetException e) {
            throw new MappingException(
                    "Could not process annotations for method "
                            + method.getName(), e);
        }

        if (jpaFieldNature.isId()) {
            classInfo.addKey(fieldInfo);
        } else {
            classInfo.addFieldInfo(fieldInfo);
        }
    }

    /**
     * Checks whether a class is describable or not. A class is describable if it
     * is not Void, Object or Class and is annotated with the {@link Entity}
     * annotation.
     * 
     * @param type
     *            the class to check
     * @return false if the given type is Void, Object or Class or is not
     *         annotated with the {@link Entity} annotation - all other Classes
     *         are describable.
     */
    private static boolean isDescribable(final Class<?> type) {
        if (Object.class.equals(type) || Void.class.equals(type)
                || Class.class.equals(type)) {
            return false;
        }
        if (type.getAnnotation(Entity.class) == null &&
              type.getAnnotation(MappedSuperclass.class) == null  ) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a {@link Method} is describable or not. A method is NOT
     * describable if:
     * <ul>
     * <li>The declaring Class of the method is NOT the given type (except for
     * methods defined by interfaces)</li>
     * <li>The method is synthetic</li>
     * <li>The method is static</li>
     * <li>The method is transient</li>
     * </ul>
     * 
     * @param type
     *            the Class the method belongs to
     * @param method
     *            the {@link Method} we want to check
     * @return true if the method is describable, false if not.
     */
    private static boolean isDescribable(final Class<?> type, final Method method) {
        boolean isDescribeable = true;
        Class<?> declaringClass = method.getDeclaringClass();

        if ((declaringClass != null) && (!type.equals(declaringClass))
                && (!declaringClass.isInterface())) {
            isDescribeable = false;
        }
        if (method.isSynthetic()) {
            isDescribeable &= false;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            isDescribeable &= false;
        }
        if (Modifier.isTransient(method.getModifiers())) {
            isDescribeable &= false;
        }
        return isDescribeable;
    }
}

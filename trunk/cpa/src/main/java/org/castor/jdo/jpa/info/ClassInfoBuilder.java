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
package org.castor.jdo.jpa.info;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.castor.core.annotationprocessing.AnnotationProcessingService;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;

/**
 * Uses a JPA annotated {@link Class} to build {@link ClassInfo} and
 * {@link FieldInfo}s of it and parse the mapping information in them. For now,
 * all fields are mapped as normal fields. No keys fields are recognized.
 * 
 * @author Peter Schmidt
 * @since 1.3
 */
public class ClassInfoBuilder {

    /**
     * The {@link AnnotationProcessingService} for class related annotations.
     */
    private AnnotationProcessingService _classAnnotationProcessingService;
    /**
     * The {@link AnnotationProcessingService} for field related annotations.
     */
    private AnnotationProcessingService _fieldAnnotationProcessingService;

    /**
     * Create a new ClassInfoBuilder with the JPA annotation capable
     * {@link AnnotationProcessingService}s.
     * <ul>
     * <li>{@link JPAClassAnnotationProcessingService}</li>
     * <li>{@link JPAFieldAnnotationProcessingService}</li>
     * </ul>
     */
    public ClassInfoBuilder() {
        _classAnnotationProcessingService = new JPAClassAnnotationProcessingService();
        _fieldAnnotationProcessingService = new JPAFieldAnnotationProcessingService();
    }

    /**
     * @return the {@link AnnotationProcessingService} used to read annotations
     *         of the class.
     */
    public final AnnotationProcessingService getClassAnnotationProcessingService() {
        return _classAnnotationProcessingService;
    }

    /**
     * Set the {@link AnnotationProcessingService} used to read the classes
     * annotations.
     * 
     * @param annotationProcessingService
     *            the {@link AnnotationProcessingService} used to read the
     *            classes annotations.
     */
    public final void setClassAnnotationProcessingService(
            final AnnotationProcessingService annotationProcessingService) {
        _classAnnotationProcessingService = annotationProcessingService;
    }

    /**
     * @return the {@link AnnotationProcessingService} used to read annotations
     *         of the classes fields.
     */
    public final AnnotationProcessingService getFieldAnnotationProcessingService() {
        return _fieldAnnotationProcessingService;
    }

    /**
     * Set the {@link AnnotationProcessingService} used to read annotations of
     * the classes fields.
     * 
     * @param annotationProcessingService
     *            the {@link AnnotationProcessingService} used to read
     *            annotations of the classes fields.
     */
    public final void setFieldAnnotationProcessingService(
            final AnnotationProcessingService annotationProcessingService) {
        _fieldAnnotationProcessingService = annotationProcessingService;
    }

    /**
     * Builds a new {@link ClassInfo} describing the given Class. Annotations for the class and its
     * fields are read using the {@link AnnotationProcessingService}s defined by
     * {@link #setClassAnnotationProcessingService(AnnotationProcessingService) and
     * 
     * @link #setFieldAnnotationProcessingService(AnnotationProcessingService)} . The information is
     *       stored in the {@link ClassInfo} and its related {@link FieldInfo}s.
     * 
     * @param type
     *            The Class Object representing the Class that shall be described.
     * @return a new {@link ClassInfo} describing the given Class.
     * @throws MappingException
     *          if annotation placement is invalid (field and property access for the same field). 
     */
    public final ClassInfo buildClassInfo(final Class<?> type)
            throws MappingException {

        if (type == null) {
            throw new IllegalArgumentException("Argument type must not be null");
        }

        if (!isDescribeable(type)) {
            return null;
        }

        /*
         * create new ClassInfo and Nature
         */
        ClassInfo classInfo = new ClassInfo(type);
        classInfo.addNature(JPAClassNature.class.getName());
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        /*
         * process class annotations
         */
        _classAnnotationProcessingService.processAnnotations(jpaClassNature,
                type.getAnnotations());

        /*
         * process annotations for all declared (not inherited) fields
         */
        for (Field field : type.getDeclaredFields()) {
            if (field.getAnnotations().length != 0) {
                if (isDescribeable(type, field)) {
                    buildFieldInfo(classInfo, field);
                } else {
                    throw new MappingException(
                            "Invalid field annotated, field is not describeable!");
                }
            }
        }

        /*
         * process annotations for all declared (not inherited) getter methods
         */

        for (Method method : type.getDeclaredMethods()) {
            if (isGetter(method) && method.getAnnotations().length != 0) {
                if (isDescribeable(type, method)) {
                    buildFieldInfo(classInfo, method);
                } else {
                    throw new MappingException(
                            "Invalid method annotated, method is not describeable!");
                }
            }
        }

        return classInfo;
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
    private void buildFieldInfo(final ClassInfo classInfo, final Method method)
            throws MappingException {
        if (classInfo == null) {
            throw new IllegalArgumentException(
                    "Argument classInfo must not be null.");
        }
        if (method == null) {
            throw new IllegalArgumentException(
                    "Argument method must not be null.");
        }
        String fieldName = getFieldNameFromGetterMethod(method);
        if (fieldName == null) {
            throw new IllegalArgumentException(
                    "Can not resolve Fieldname from method name.");
        }

        FieldInfo fieldInfo = classInfo.getFieldInfoByName(fieldName);
        if (fieldInfo == null) {
            fieldInfo = classInfo.getKeyFieldInfoByName(fieldName);
            if (fieldInfo != null) {
                throw new MappingException("Can not annotate field and method!");
            }
        }

        Class<?> fieldType = method.getReturnType();
        TypeInfo typeInfo = new TypeInfo(fieldType);
        FieldHandlerImpl fieldHandler;
        try {
            fieldHandler = new FieldHandlerImpl(fieldName, null, null, method,
                    getSetterMethodFromGetter(method), typeInfo);
        } catch (SecurityException e) {
            throw new MappingException("Setter method for field " + fieldName
                    + " is not accessible!");
        } catch (NoSuchMethodException e) {
            throw new MappingException("Setter method for field " + fieldName
                    + " does not exist!");
        }
        fieldInfo = new FieldInfo(classInfo, fieldType, fieldName, fieldHandler);

        fieldInfo.addNature(JPAFieldNature.class.getName());
        JPAFieldNature jpaFieldNature = new JPAFieldNature(fieldInfo);
        _fieldAnnotationProcessingService.processAnnotations(jpaFieldNature,
                method.getAnnotations());

        // if (jpaFieldNature.isId ()) {
        // classInfo.addKey (fieldInfo);
        // } else {
        classInfo.addFieldInfo(fieldInfo);
        // }
    }

    /**
     * Build a {@link FieldInfo} describing the given {@link Field} by
     * processing its annotations and add the generated {@link FieldInfo} to the
     * given {@link ClassInfo}.
     * 
     * @param classInfo
     *            the {@link ClassInfo} of the declaring class
     * @param field
     *            the {@link Field} to describe.
     * @throws MappingException
     *             If the field is not public, is static or transient
     */
    private void buildFieldInfo(final ClassInfo classInfo, final Field field)
            throws MappingException {
        if (classInfo == null) {
            throw new IllegalArgumentException(
                    "Argument classInfo must not be null.");
        }

        if (field == null) {
            throw new IllegalArgumentException(
                    "Argument field must not be null.");
        }

        Class<?> fieldType = field.getType();
        TypeInfo typeInfo = new TypeInfo(fieldType);
        FieldHandlerImpl fieldHandler = new FieldHandlerImpl(field, typeInfo);
        FieldInfo fieldInfo = new FieldInfo(classInfo, fieldType, field
                .getName(), fieldHandler);

        fieldInfo.addNature(JPAFieldNature.class.getName());
        JPAFieldNature jpaFieldNature = new JPAFieldNature(fieldInfo);
        _fieldAnnotationProcessingService.processAnnotations(jpaFieldNature,
                field.getAnnotations());

        // if (jpaFieldNature.isId ()) {
        // classInfo.addKey (fieldInfo);
        // } else {
        classInfo.addFieldInfo(fieldInfo);
        // }
    }

    /**
     * Convenience method to check whether a {@link Method} is a getter method,
     * i.e. starts with "get" or "is".
     * 
     * @param method
     *            the {@link Method} to check.
     * @return true if the methods name starts with "get" or "is" (Java Beans
     *         convention).
     */
    private boolean isGetter(final Method method) {
        if (method.getName().startsWith("get")
                || method.getName().startsWith("is")) {
            return true;
        }
        return false;
    }

    /**
     * Create a member name from a getter {@link Method}.
     * 
     * @param method
     *            The getter {@link Method}
     * @return The name of the underlying member.
     */
    private String getFieldNameFromGetterMethod(final Method method) {
        if (!isGetter(method)) {
            throw new IllegalArgumentException("Method is not a getter method!");
        }

        if (method.getName().startsWith("get")) {
            String nameRest = method.getName().substring(4);
            return Character.toLowerCase(method.getName().charAt(3)) + nameRest;
        }
        if (method.getName().startsWith("is")) {
            String nameRest = method.getName().substring(3);
            return Character.toLowerCase(method.getName().charAt(2)) + nameRest;
        }
        throw new IllegalArgumentException(
                "Method name does not start with 'get' or 'is'!");
    }

    /**
     * Return a setter {@link Method} for a given getter {@link Method}.
     * 
     * @param getter
     *            The getter {@link Method}
     * @return The setter Method for the given getter Method.
     * @throws NoSuchMethodException
     *             If the setter method does not exist
     * @throws SecurityException
     *             If the setter method is not accessible
     */
    private Method getSetterMethodFromGetter(final Method getter)
            throws SecurityException, NoSuchMethodException {
        if (!isGetter(getter)) {
            throw new IllegalArgumentException("Method is not a getter method!");
        }

        String setterName;
        if (getter.getName().startsWith("get")) {
            String name = getter.getName().substring(1);
            setterName = "s" + name;
        } else if (getter.getName().startsWith("is")) {
            String name = getter.getName().substring(2);
            setterName = "set" + name;
        } else {
            throw new IllegalArgumentException(
                    "Method name does not start with 'get' or 'is'!");
        }

        return getter.getDeclaringClass().getDeclaredMethod(setterName,
                getter.getReturnType());
    }

    /**
     * Checks whether a class is describable or not.
     * 
     * @param type
     *            the class to check
     * @return false if the given type is Void, Object or Class - all other
     *         Classes are describable.
     */
    private boolean isDescribeable(final Class<?> type) {
        if (Object.class.equals(type) || Void.class.equals(type)
                || Class.class.equals(type)) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a field is describable or not. A field is NOT describable
     * if:
     * <ul>
     * <li>The declaring Class of the Field is NOT the given type (except for
     * fields defined by interfaces)</li>
     * <li>The field is synthetic</li>
     * <li>The field is static</li>
     * <li>The field is transient</li>
     * </ul>
     * 
     * @param type
     *            the Class the field belongs to
     * @param field
     *            the field we want to check
     * @return true if the field is describable, false if not.
     */

    private boolean isDescribeable(final Class<?> type, final Field field) {
        boolean isDescribeable = true;
        Class<?> declaringClass = field.getDeclaringClass();
        if ((declaringClass != null) && !type.equals(declaringClass)
                && (!declaringClass.isInterface())) {
            isDescribeable = false;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            isDescribeable &= false;
        }
        if (Modifier.isTransient(field.getModifiers())) {
            isDescribeable &= false;
        }
        if (field.isSynthetic()) {
            isDescribeable &= false;
        }
        return isDescribeable;
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
    private boolean isDescribeable(final Class<?> type, final Method method) {
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

package org.castor.jdo.jpa.processors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.castor.core.annotationprocessing.AnnotationTargetException;

/**
 * Tool class that offers methods to retrieve information from
 * {@link AnnotatedElement}s (Methods or Fields).
 * 
 * @author Peter Schmidt
 * @version 12.02.2009
 * 
 */
public final class ReflectionsHelper {

    /**
     * Private Constructor to prevent instantiation.
     */
    private ReflectionsHelper() {
    }

    /**
     * Get the (raw) type of collection from the annotations target and optional
     * check for JPA 1.0 restrictions (only Collection, Map, List and Set are
     * allowed).
     * 
     * @param target
     *            The ManyToMany annotations target.
     * @param jpaTypesOnly
     *            If set to true only Collection types of JPA 1.0 are allowed.
     *            Others will lead to a thrown Exception.
     * @return The raw collection type.
     * @throws AnnotationTargetException
     *             if the raw collection type can not be inferred from the type
     *             definition or if the type is not supported by JPA 1.0 and
     *             jpaTypesOnly was set to true.
     */
    public static Class<?> getCollectionType(final AnnotatedElement target,
            final boolean jpaTypesOnly) throws AnnotationTargetException {
        Class<?> collectionType = null;
        Type fieldType;
        if (target instanceof Field) {
            fieldType = ((Field) target).getGenericType();
        } else {
            fieldType = ((Method) target).getGenericReturnType();
        }

        if (fieldType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) fieldType).getRawType();
            if (rawType instanceof Class<?>) {
                collectionType = (Class<?>) rawType;
            } else {
                String message = "Can not infer raw type of generic type definition for field "
                        + ((Member) target).getName()
                        + ". Raw type is not a Class object.";
                throw new AnnotationTargetException(message);
            }
        } else if (fieldType instanceof Class<?>) {
            collectionType = (Class<?>) fieldType;
        }
        String collectionTypeName = collectionType.getSimpleName();
        if (jpaTypesOnly) {
            if ((!collectionTypeName.equals("Collection"))
                    && (!collectionTypeName.equals("Map"))
                    && (!collectionTypeName.equals("List"))
                    && (!collectionTypeName.equals("Set"))) {

                throw new AnnotationTargetException(
                        collectionTypeName
                                + " is not supported by JPA 1.0! Only Collection, Set, "
                                + "List or Map are allowed types of OneToMany properties!");
            }
        }
        return collectionType;
    }

    /**
     * Get the target entity information from the target (necessary if it was
     * not specified by the annotation itself).
     * 
     * @param target
     *            The *ToMany annotations target. This method will fail if
     *            target is not of Type {@link Field} or {@link Method}!
     * @return the relations target entity inferred by the targets generic
     *         definition or null if no generic definition was found at all.
     * @throws AnnotationTargetException
     *             if the generic definition is not sufficient
     */
    public static Class<?> getTargetEntityFromGenerics(
            final AnnotatedElement target) throws AnnotationTargetException {

        Type fieldType = null;
        if (target instanceof Field) {
            fieldType = ((Field) target).getGenericType();
        } else if (target instanceof Method) {
            fieldType = ((Method) target).getGenericReturnType();
        } else {
            throw new IllegalArgumentException(
                    "Can only read generic definitions from Methods or Fields.");
        }

        Class<?> targetEntity = null;
        if (fieldType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) fieldType)
                    .getActualTypeArguments();
            Type targetType;
            // check wildcard generic => use upper bound
            if (actualTypeArguments[0] instanceof WildcardType) {
                targetType = ((WildcardType) actualTypeArguments[0])
                        .getUpperBounds()[0];
            } else {
                targetType = actualTypeArguments[0];
            }

            if (targetType instanceof Class<?>) {
                targetEntity = (Class<?>) targetType;
            } else {
                // Error => can not infer targetEntity from generic
                // definition
                String message = "Can not infer target entity for ManyToOne relation on "
                        + ((Member) target).getName()
                        + " - use simplier generics or specify targetEntity!";
                throw new AnnotationTargetException(message);
            }

        } else {
            return null;
        }

        return targetEntity;
    }

    /**
     * Little helper to get a field (bean property) name from a getter method. <br/>
     * Example: <code>getFoo() => "foo"</code><br/>
     * Example: <code>isBar() => "bar"</code>
     * 
     * @param getter
     *            the {@link Method} to analyse.
     * @return the field name determined by the methods name or NULL if the
     *         method is not a getter (start with "get" or "is").
     */
    public static String getFieldnameFromGetter(final Method getter) {
        String[] getterPrefixes = {"get", "is"};

        String methodName = getter.getName();
        String fieldName = null;

        for (String prefix : getterPrefixes) {
            if (methodName.startsWith(prefix)) {
                fieldName = methodName.substring(prefix.length());
            }
        }

        if (fieldName == null) {
            return null;
        }

        fieldName = fieldName.substring(0, 1).toLowerCase()
                + fieldName.substring(1);
        return fieldName;
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
    public static Method getSetterMethodFromGetter(final Method getter)
            throws SecurityException, NoSuchMethodException {
        if (!isGetter(getter)) {
            throw new IllegalArgumentException("Method is not a getter method!");
        }

        String[] prefixes = {"get", "is"};

        String setterName;
        for (String prefix : prefixes) {
            if (getter.getName().startsWith(prefix)) {
                String name = getter.getName().substring(prefix.length());
                setterName = "set" + name;
                return getter.getDeclaringClass().getDeclaredMethod(setterName,
                        getter.getReturnType());
            }
        }
        throw new IllegalArgumentException(
                "Method name does not start with 'get' or 'is'!");
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
    public static boolean isGetter(final Method method) {
        if (method.getName().startsWith("get")
                || method.getName().startsWith("is")) {
            return true;
        }
        return false;
    }

}

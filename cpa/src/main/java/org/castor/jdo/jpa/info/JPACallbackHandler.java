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
package org.castor.jdo.jpa.info;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles JPA annotation-driven callback hooks.
 */
public class JPACallbackHandler implements CallbackInterceptor {

    private static final Log LOG = LogFactory.getLog(JPACallbackHandler.class);
    
    /**
     * Objects for which callbacks need to be invoked.
     */
    private final List<Object> objectsToInvokeCallbacksOn = new ArrayList<Object>();
    
    /**
     * Memorises overridden callbacks.
     */
    private final Map<String, Object> overriddenCallbacks = new HashMap<String, Object>();

    public Class<?> loaded(final Object object, final AccessMode accessMode)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `loaded`.");
        }
        handleCallbacksFor(PostLoad.class, object);
        return object.getClass();
    }

    public void modifying(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `modifying`.");
        }
        handleCallbacksFor(PreUpdate.class, object);
    }

    public void storing(final Object object, final boolean modified)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `storing`.");
        }
        if (modified) {
            handleCallbacksFor(PostUpdate.class, object);
        }
    }

    public void creating(final Object object, final Database db)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `creating`.");
        }
        handleCallbacksFor(PrePersist.class, object);
    }

    public void created(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `created`.");
        }
        handleCallbacksFor(PostPersist.class, object);
    }

    public void removing(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `removing`.");
        }
        handleCallbacksFor(PreRemove.class, object);
    }

    public void removed(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `removed`.");
        }
        handleCallbacksFor(PostRemove.class, object);
    }

    public void releasing(final Object object, final boolean committed) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `releasing`.");
        }
        // TODO: impl.?
    }

    public void using(final Object object, final Database db) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `using`.");
        }
        // TODO: impl.?
    }

    public void updated(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `updated`.");
        }
        // TODO: impl.?
    }

    /**
     * Handles callbacks accordingly.
     *
     * @param annotationClass the annotation to look for
     * @param object          the object to handle callbacks for
     * @param <A>             helper annotation generics
     * @throws InvocationTargetException on callback invocation error
     * @throws IllegalAccessException    on illegal method access error
     * @throws InstantiationException    on instantiation error
     * @throws NoSuchMethodException     on method not present error
     */
    private <A extends Annotation> void handleCallbacksFor(
            final Class<A> annotationClass, final Object object)
            throws InvocationTargetException, IllegalAccessException,
            InstantiationException, NoSuchMethodException {
        objectsToInvokeCallbacksOn.clear();
        overriddenCallbacks.clear();
        walkCallbacksHierarchyFor(annotationClass, object);
        if (objectsToInvokeCallbacksOn.size() > 1) {
            handleOverriddenCallbacksFor(annotationClass,
                    objectsToInvokeCallbacksOn.get(objectsToInvokeCallbacksOn
                            .size() - 1));
        }
        for (Object obj : objectsToInvokeCallbacksOn) {
            invokeCallbacksFor(annotationClass, obj);
        }
        for (Map.Entry<String, Object> entry : overriddenCallbacks.entrySet()) {
            invokeCallback(entry.getValue().getClass().getDeclaredMethod(
                    entry.getKey()), entry.getValue());
        }
    }

    /**
     * Walks callbacks hierarchy accordingly.
     *
     * @param annotationClass the annotation to look for
     * @param object          the object to walk hierarchy for
     * @param <A>             helper annotation generics
     * @throws InvocationTargetException on callback invocation error
     * @throws IllegalAccessException    on illegal method access error
     * @throws InstantiationException    on instantiation error
     */
    private <A extends Annotation> void walkCallbacksHierarchyFor(
            final Class<A> annotationClass, final Object object)
            throws InvocationTargetException, IllegalAccessException,
            InstantiationException {
        final Class<?> klass = object.getClass();
        if (klass.isAnnotationPresent(Entity.class)) {
            final Class<?> superclass = klass.getSuperclass();
            if (superclass != Object.class) {
                walkCallbacksHierarchyFor(annotationClass, superclass
                        .newInstance());
            }
            final EntityListeners entityListeners = klass.getAnnotation(
                    EntityListeners.class);
            if (entityListeners != null) {
                final Class<?>[] listeners = entityListeners.value();
                for (Class<?> listener : listeners) {
                    invokeCallbacksFor(annotationClass, listener.newInstance());
                }
            } // Store object to invoke callbacks on.
            objectsToInvokeCallbacksOn.add(object);
        }
    }

    /**
     * Handles overridden CB methods accordingly.
     *
     * @param annotationClass the annotation to look for
     * @param object          the object to handle
     * @throws InvocationTargetException on callback invocation error
     * @throws IllegalAccessException    on illegal method access error
     */
    private <A extends Annotation> void handleOverriddenCallbacksFor(
            final Class<A> annotationClass, final Object object)
    throws InvocationTargetException, IllegalAccessException {
        Class<?> klass = object.getClass();
        Class<?> superclass = klass.getSuperclass();
        while (superclass != Object.class) {
            for (Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotationClass)) {
                    final String methodName = method.getName();
                    try {
                        final Method overridden = superclass.getDeclaredMethod(
                                methodName);
                        if (overridden.isAnnotationPresent(annotationClass)) {
                            overriddenCallbacks.put(methodName, object);
                        }
                    } catch (NoSuchMethodException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format(
                                    "CB method `%s` is not overridden in `%s`.",
                                    method.getName(), superclass.getSimpleName()
                            ));
                        }
                    }
                }
            }
            superclass = superclass.getSuperclass();
        }
    }

    /**
     * Invokes callback methods accordingly.
     *
     * @param annotationClass the annotation to look for
     * @param object          the object to invoke callbacks on
     * @param <A>             helper annotation generics
     * @throws InvocationTargetException on callback invocation error
     * @throws IllegalAccessException    on illegal method access error
     */
    private <A extends Annotation> void invokeCallbacksFor(
            final Class<A> annotationClass, final Object object)
            throws InvocationTargetException, IllegalAccessException {
        final Class<?> klass = object.getClass();
        final Method[] declaredMethods = klass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                if (!overriddenCallbacks.containsKey(method.getName())) {
                    invokeCallback(method, object);
                }
            }
        }
    }

    /**
     * Invokes a callback method accordingly.
     *
     * @param method the CB method to invoke
     * @param object the object to invoke on
     * @throws InvocationTargetException on callback invocation error
     * @throws IllegalAccessException    on illegal method access error
     */
    private void invokeCallback(final Method method, final Object object)
            throws InvocationTargetException, IllegalAccessException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Invoking CB method `%s` on `%s`.",
                    method.getName(), object.getClass().getSimpleName()
            ));
        }
        method.setAccessible(true);
        method.invoke(object);
    }

}

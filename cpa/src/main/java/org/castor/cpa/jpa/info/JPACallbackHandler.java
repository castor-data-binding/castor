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
package org.castor.cpa.jpa.info;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * Handles JPA annotation-driven callback hooks.
 */
public class JPACallbackHandler implements CallbackInterceptor {

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(JPACallbackHandler.class);

    /**
     * Objects for which callbacks need to be invoked.
     */
    private final List<Object> _objectsToInvokeCallbacksOn = new ArrayList<Object>();

    /**
     * Memorises overridden callbacks.
     */
    private final Map<String, Object> _overriddenCallbacks = new HashMap<String, Object>();

    /**
     * Memorises if superclass listeners are to be excluded.
     */
    private boolean _excludeSuperclassListeners = false;

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
        // TODO impl.?
    }

    public void using(final Object object, final Database db) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `using`.");
        }
        // TODO impl.?
    }

    public void updated(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `updated`.");
        }
        // TODO impl.?
    }

    /**
     * Handles callbacks accordingly.
     * 
     * @param annotationClass
     *            the annotation to look for
     * @param object
     *            the object to handle callbacks for
     * @param <A>
     *            helper annotation generics
     * @throws InvocationTargetException
     *             on callback invocation error
     * @throws IllegalAccessException
     *             on illegal method access error
     * @throws InstantiationException
     *             on instantiation error
     * @throws NoSuchMethodException
     *             on method not present error
     */
    private <A extends Annotation> void handleCallbacksFor(
            final Class<A> annotationClass, final Object object)
            throws InvocationTargetException, IllegalAccessException,
            InstantiationException, NoSuchMethodException {
        _objectsToInvokeCallbacksOn.clear();
        _overriddenCallbacks.clear();
        _excludeSuperclassListeners = false;
        walkCallbacksHierarchyFor(annotationClass, object);
        if (_objectsToInvokeCallbacksOn.size() > 1) {
            handleOverriddenCallbacksFor(annotationClass,
                    _objectsToInvokeCallbacksOn.get(_objectsToInvokeCallbacksOn
                            .size() - 1));
        }
        for (Object obj : _objectsToInvokeCallbacksOn) {
            invokeCallbacksFor(annotationClass, obj);
        }
        for (Map.Entry<String, Object> entry : _overriddenCallbacks.entrySet()) {
            invokeCallback(entry.getValue().getClass().getDeclaredMethod(
                    entry.getKey()), entry.getValue());
        }
    }

    /**
     * Walks callbacks hierarchy accordingly.
     * 
     * @param annotationClass
     *            the annotation to look for
     * @param object
     *            the object to walk hierarchy for
     * @param <A>
     *            helper annotation generics
     * @throws InvocationTargetException
     *             on callback invocation error
     * @throws IllegalAccessException
     *             on illegal method access error
     * @throws InstantiationException
     *             on instantiation error
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
            if (!_excludeSuperclassListeners) {
                invokeListenerCallbacksFor(annotationClass, klass);
            }
            if (klass.isAnnotationPresent(ExcludeSuperclassListeners.class)) {
                _excludeSuperclassListeners = true;
            }
            _objectsToInvokeCallbacksOn.add(object);
        }
    }

    /**
     * Invokes listener callbacks accordingly.
     * 
     * @param annotationClass
     *            the annotation to look for
     * @param klass
     *            the class to handle listeners
     * @param <A>
     *            helper annotation generics
     * @throws InvocationTargetException
     *             on callback invocation error
     * @throws IllegalAccessException
     *             on illegal method access error
     * @throws InstantiationException
     *             on instantiation error
     */
    private <A extends Annotation> void invokeListenerCallbacksFor(
            final Class<A> annotationClass, final Class<?> klass)
            throws InvocationTargetException, IllegalAccessException,
            InstantiationException {
        if (!klass.isAnnotationPresent(ExcludeDefaultListeners.class)) {
            final EntityListeners entityListeners = klass
                    .getAnnotation(EntityListeners.class);
            if (entityListeners != null) {
                final Class<?>[] listeners = entityListeners.value();
                for (Class<?> listener : listeners) {
                    invokeCallbacksFor(annotationClass, listener.newInstance());
                }
            }
        }
    }

    /**
     * Handles overridden CB methods accordingly.
     * 
     * @param annotationClass
     *            the annotation to look for
     * @param object
     *            the object to handle
     * @throws InvocationTargetException
     *             on callback invocation error
     * @throws IllegalAccessException
     *             on illegal method access error
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
                        final Method overridden = superclass
                                .getDeclaredMethod(methodName);
                        if (overridden.isAnnotationPresent(annotationClass)) {
                            _overriddenCallbacks.put(methodName, object);
                        }
                    } catch (NoSuchMethodException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG
                                    .debug(String
                                            .format(
                                                    "CB method `%s` is not overridden in `%s`.",
                                                    method.getName(),
                                                    superclass.getSimpleName()));
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
     * @param annotationClass
     *            the annotation to look for
     * @param object
     *            the object to invoke callbacks on
     * @param <A>
     *            helper annotation generics
     * @throws InvocationTargetException
     *             on callback invocation error
     * @throws IllegalAccessException
     *             on illegal method access error
     */
    private <A extends Annotation> void invokeCallbacksFor(
            final Class<A> annotationClass, final Object object)
            throws InvocationTargetException, IllegalAccessException {
        final Class<?> klass = object.getClass();
        final Method[] declaredMethods = klass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                if (!_overriddenCallbacks.containsKey(method.getName())) {
                    invokeCallback(method, object);
                }
            }
        }
    }

    /**
     * Invokes a callback method accordingly.
     * 
     * @param method
     *            the CB method to invoke
     * @param object
     *            the object to invoke on
     * @throws InvocationTargetException
     *             on callback invocation error
     * @throws IllegalAccessException
     *             on illegal method access error
     */
    private void invokeCallback(final Method method, final Object object)
            throws InvocationTargetException, IllegalAccessException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Invoking CB method `%s` on `%s`.", method
                    .getName(), object.getClass().getSimpleName()));
        }
        method.setAccessible(true);
        method.invoke(object);
    }

}

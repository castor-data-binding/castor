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

/**
 * Handles JPA annotation-driven callback hooks.
 */
public class JPACallbackHandler implements CallbackInterceptor {

    private static final Log LOG = LogFactory.getLog(JPACallbackHandler.class);

    public Class<?> loaded(final Object object, final AccessMode accessMode)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `loaded`.");
        }
        invokeCallbacksFor(PostLoad.class, object);
        return object.getClass();
    }

    public void modifying(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `modifying`.");
        }
        invokeCallbacksFor(PreUpdate.class, object);
    }

    public void storing(final Object object, final boolean modified)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `storing`.");
        }
        if (modified) {
            invokeCallbacksFor(PostUpdate.class, object);
        }
    }

    public void creating(final Object object, final Database db)
            throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `creating`.");
        }
        invokeCallbacksFor(PrePersist.class, object);
    }

    public void created(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `created`.");
        }
        invokeCallbacksFor(PostPersist.class, object);
    }

    public void removing(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `removing`.");
        }
        invokeCallbacksFor(PreRemove.class, object);
    }

    public void removed(final Object object) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Calling `removed`.");
        }
        invokeCallbacksFor(PostRemove.class, object);
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
        final Method[] declaredMethods = object.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(annotationClass)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Invoking callback method: " + method.getName());
                }
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }

}

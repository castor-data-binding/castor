/*
 * Copyright 2006 Thierry Guerin
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
package org.exolab.castor.xml.wls8;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class WeblogicXercesImplementation {
    private static final Log LOG = LogFactory.getLog(WeblogicXercesImplementation.class);

    /**
     * Returns a Method object that reflects the specified public member method of the class 
     * or interface represented by <code>aClass</code>.
     * <br> If either an SecurityException or NoSuchMethodException 
     * is thrown, calls {@link #handleStaticInitException(Exception)}.
     * 
     * @param aClass The class.
     * @param methodName The name of the method.
     * @param parameterTypes The list of parameters.
     * @return The Method object that matches the specified name and parameterTypes.
     * @see Class#getMethod(java.lang.String, java.lang.Class[])
     */
    protected static Method getMethod(Class aClass, String methodName, Class[] parameterTypes) {
        Method method = null;
        try {
            method = aClass.getMethod(methodName, parameterTypes);
        }
        catch (SecurityException e) {
            handleStaticInitException("Error while trying to get the method "
                    + methodName + " in class " + aClass, e);
        }
        catch (NoSuchMethodException e) {
            handleStaticInitException("Error while trying to get the method "
                    + methodName + " in class " + aClass, e);
        }
        return method;
    }

    /**
     * Invokes a method. Calls {@link Method#invoke(java.lang.Object, java.lang.Object[])}. If either
     * an IllegalArgumentException, IllegalAccessException or InvocationTargetException is thrown,
     * calls {@link #handleMethodInvokeException(Exception)}.
     * 
     * @param anObject The object instance to invoke the method on.
     * @param method The method to invoke.
     * @param params The parameters to pass to the method.
     * @return If the method completes normally, the value it returns is returned to the caller 
     * of invoke; if the value has a primitive type, it is first appropriately wrapped in an object.
     * If the underlying method return type is void, the invocation returns null.
     * @see Method#invoke(java.lang.Object, java.lang.Object[])
     */
    protected Object invoke(Object anObject, Method method, Object[] params) {
        try {
            return method.invoke(anObject, params);
        }
        catch (IllegalArgumentException e) {
            handleMethodInvokeException(e);
        }
        catch (IllegalAccessException e) {
            handleMethodInvokeException(e);
        }
        catch (InvocationTargetException e) {
            handleMethodInvokeException(e);
        }
        // this code is never reached in theory but is necessary because handleMethodInvokeException
        // throws a RuntimeException (unchecked).
        return null;
    }
    
    // exception handling methods
    public static void handleStaticInitException(Exception e) {
        handleStaticInitException("Error while intializing class", e);
    }

    /**
     * Throws a Runtime exception with <code>e</code>'s {@link Exception#getMessage() message} as its
     * detail message. Also logs the exception as an error.
     * Called if an error occurs during the static initialization of WeblogicXercesSerializer 
     * & OutputFormat (these classes use reflection to get the Weblogic classes & methods).
     * 
     * @param message The Message that will be inserted before <code>e</code>'s 
     * {@link Exception#getMessage() message} in the RuntimeException's detail message.
     * @param e The exception that will be "wrapped" in a RuntimeException
     */
    public static void handleStaticInitException(String message, Exception e) {
        LOG.error(message, e);
        if (e instanceof RuntimeException) // don't wrap the exception
            throw (RuntimeException) e;
        throw new RuntimeException(message + ". " + e.getMessage()); // java 1.3, can't wrap using the 1.4 constructor
    }
    
    protected static void handleMethodInvokeException(Exception e) {
        handleMethodInvokeException("Error while trying to invoke a method", e);
    }

    /**
     * Throws a Runtime exception with <code>e</code>'s {@link Exception#getMessage() message} as its
     * detail message. Also logs the exception as an error.
     * 
     * @param message The Message that will be inserted before <code>e</code>'s 
     * {@link Exception#getMessage() message} in the RuntimeException's detail message.
     * @param e The exception that will be "wrapped" in a RuntimeException.
     */
    protected static void handleMethodInvokeException(String message, Exception e) {
        LOG.error(message, e);
        if (e instanceof RuntimeException) // don't wrap the exception
            throw (RuntimeException) e;
        throw new RuntimeException(message + ". " + e.getMessage()); // java 1.3, can't wrap using the 1.4 constructor
    }
}

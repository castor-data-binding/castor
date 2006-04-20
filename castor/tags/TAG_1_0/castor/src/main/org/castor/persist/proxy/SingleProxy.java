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
 *
 * $Id$
 */
package org.castor.persist.proxy;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.persist.ProposedObject;
import org.castor.persist.TransactionContext;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public final class SingleProxy implements MethodInterceptor, Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -1498354553937679053L;

    private static Log _log = LogFactory.getFactory().getInstance(SingleProxy.class);
    
    private TransactionContext _tx;
    private LockEngine _engine;
    private ClassMolder _classMolder;
    private Class _clazz;
    private Object _identity;
    private Object _object;
    private AccessMode _accessMode;
    
    private boolean _hasMaterialized = false;
    
    /**
     * Creates an instance of SingleProxy.
     * @param tx Actual TransactionContext.
     * @param engine Associated LockEngine
     * @param classMolder Associated ClassMolder.
     * @param clazz Associated Class instance.
     * @param identity Identity object.
     * @param object Object to be lazy-loaded.
     * @param accessMode Access mode identifier.
     */
    private SingleProxy(final TransactionContext tx,
            final LockEngine engine,
            final ClassMolder classMolder,
            final Class clazz,
            final Object identity,
            final Object object,
            final AccessMode accessMode) {
        if (_log.isDebugEnabled()) {
            _log.debug("create new SingleProxy -> " + classMolder.getName()
                    + " with id " + identity);
        }
        _tx = tx;
        _engine = engine;
        _classMolder = classMolder;
        _clazz = clazz;
        _identity = identity;
        _object = object;
        _accessMode = accessMode;
    }
    
    /**
     * Factory method to create SingleProxy instance. 
     * @param tx Actual TransactionContext.
     * @param engine Associated LockEngine
     * @param classMolder Associated ClassMolder.
     * @param identity Identity object.
     * @param object Object to be lazy-loaded.
     * @param accessMode Access mode identifier.
     * @return A SingleProxy instance.
     * @throws ObjectNotFoundException
     */
    public static synchronized Object getProxy(final TransactionContext tx,
            final LockEngine engine, final ClassMolder classMolder,
            final Object identity, final Object object,
            final AccessMode accessMode) 
    throws ObjectNotFoundException {
        try {
            Class clazz = Class.forName(classMolder.getName());
            SingleProxy sp = new SingleProxy(tx, engine, classMolder, clazz,
                    identity, object, accessMode);
            return Enhancer.create(clazz, new Class[] {LazyCGLIB.class}, sp);
        } catch (Throwable ex) {
            if (_log.isErrorEnabled()) {
                String msg = "error on enhance class";
                if (classMolder != null) {
                    msg += " " + classMolder.getName();
                }
                _log.error(msg, ex);
            }
            throw new ObjectNotFoundException("lazy loading error - "
                    + ex.getMessage());
        }
    }
    
    /**
     * @see net.sf.cglib.proxy.MethodInterceptor #intercept(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[],
     *      net.sf.cglib.proxy.MethodProxy)
     */
    public Object intercept(final Object obj, final Method method, final Object[] args,
            final MethodProxy proxy) 
        throws Throwable {
    
        String methodName = method.getName();

        // to not load if method geClass() or finalize()
        if ("writeReplace".equals(methodName)) {
            if (_log.isDebugEnabled()) {
                _log.debug("writeReplacing " + _classMolder.getName()
                        + " with identity " + _identity);
            }
            if (!_hasMaterialized) {
                try {
                    _object = loadOnly();
                } catch (ObjectNotFoundException e) {
                    _log.error("Object with identity " + _identity
                            + " does not exist", e);
                    throw new NotSerializableException("Object with identity "
                            + _identity + " does not exist");
                } catch (PersistenceException e) {
                    _log.error("Problem serializing object with identity "
                            + _identity, e);
                    throw new NotSerializableException(
                            "Problem serializing object with identity "
                                    + _identity);
                }
            }

            if (_log.isDebugEnabled()) {
                _log.debug("Serializing instance of "
                        + _object.getClass().getName());
                _log.debug("_object = " + _object);
            }
            return _object;
        } else if ("interceptedClass".equals(methodName)) {
            return _clazz;
        } else if ("interceptedIdentity".equals(methodName)) {
            return _identity;
        } else if ("getClass".equals(methodName)) {
            return method.invoke(obj, args);
        } else if ("finalize".equals(methodName)) {
            return method.invoke(obj, args);
        } else if ("getId".equals(methodName)) {
            if (!_hasMaterialized) {
                return _identity;
            }
        }

        // load object, if not previous loaded
        if (_object == null) {
            _object = load(obj);
        }
        // try {
        // if ( _log.isDebugEnabled() && _classMolder != null ) {
        // _log.debug("load object " + _classMolder.getName() + " with id " +
        // _identity);
        // }
        // _object = _tx.load(_engine, _classMolder, _identity, _object,
        // _accessMode);
        // hasMaterialized = true;
        // } catch (ObjectNotFoundException ex) {
        // if ( _log.isDebugEnabled() ) {
        // _log.debug("object not found -> " + ex.toString());
        // }
        // // if a ObjectNotFoundException occur then create a empty instance
        // if ( obj instanceof net.sf.cglib.proxy.Factory ) {
        // _object = obj.getClass().getSuperclass().newInstance();
        //              }
        //          }
        //      }

        // object found?
        if (_object == null) {
            return null;
        }

        // invoke original method in loaded object
        return method.invoke(_object, args);
    }

    /**
     * Tool method to load the underlying (proxied) object instance. 
     * @return The actual object
     * @throws ObjectNotFoundException
     * @throws PersistenceException
     */
    private Object loadOnly() throws PersistenceException {
        Object instance = null;
        if (_log.isDebugEnabled() && _classMolder != null) {
            _log.debug("load object " + _classMolder.getName() + " with id "
                    + _identity);
        }
        ProposedObject proposedValue = new ProposedObject();
        proposedValue.setProposedClass(_clazz);
        proposedValue.setObject(_object);
        instance = _tx.load(_engine, _classMolder, _identity, proposedValue,
                _accessMode);
        _hasMaterialized = true;
        return instance;
    }
    
    private Object load(final Object proxiedObject) 
    throws PersistenceException, IllegalAccessException, InstantiationException {
        Object instance = null;
        try {
            instance = loadOnly();
        } catch (ObjectNotFoundException ex) {
            if (_log.isDebugEnabled()) {
                _log.debug("object not found -> " + ex.toString());
            }
            // if a ObjectNotFoundException occur then create a empty instance
            if (proxiedObject instanceof net.sf.cglib.proxy.Factory) {
                instance = proxiedObject.getClass().getSuperclass()
                        .newInstance();
            }
        }
        return instance;
    }
    
}

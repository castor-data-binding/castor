/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test19;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.junit.Ignore;

@Ignore
public final class CallbackStateInterceptor
implements CallbackInterceptor, InstanceFactory {
    private CallbackState _callbacksInvoked = new CallbackState();
    
    public CallbackState getCallbackState() {
        return _callbacksInvoked;
    }

    public Class<?> loaded(final Object object, final AccessMode accessMode)
    throws Exception {
        _callbacksInvoked.allow(CallbackState.LOADED);

        return object.getClass();
    }

    public void storing(final Object object, final boolean modified)
    throws Exception {
        _callbacksInvoked.allow(CallbackState.STORING);
    }

    public void creating(final Object object, final Database db)
    throws Exception {
        _callbacksInvoked.allow(CallbackState.CREATING);
    }

    public void created(final Object object) throws Exception {
        _callbacksInvoked.allow(CallbackState.CREATED);
    }

    public void removing(final Object object) throws Exception {
        _callbacksInvoked.allow(CallbackState.REMOVING);
    }

    public void removed(final Object object) throws Exception {
        _callbacksInvoked.allow(CallbackState.REMOVED);
    }

    public void releasing(final Object object, final boolean committed) {
        _callbacksInvoked.allow(CallbackState.RELEASING);
    }

    public void using(final Object object, final Database db) {
        _callbacksInvoked.allow(CallbackState.USING);
    }

    public void updated(final Object object) throws Exception {
        _callbacksInvoked.allow(CallbackState.UPDATED);
    }

    public Object newInstance(final String className, final ClassLoader loader) {
        _callbacksInvoked.allow(CallbackState.INSTANTIATE);

        try {
            if (loader != null) {
                return loader.loadClass(className).newInstance();
            }
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
        } catch (ExceptionInInitializerError ex) {
        } catch (SecurityException ex) {
        }
        
        return null;
    }
}

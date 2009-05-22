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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.InstanceFactory;
import org.junit.Ignore;

/**
 * Test for the behaviors the InstanceFactory interface.
 **/
public final class TestInstanceFactory extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestInstanceFactory.class);
    private static final String DBNAME = "test19";
    private static final String MAPPING = "/org/castor/cpa/test/test19/mapping.xml";
    private Database       _db;
    private JDOManager     _jdo;
    private InternalCallbackInterceptor _i;

    public TestInstanceFactory(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }

    public void setUp() throws Exception {
        _i = new InternalCallbackInterceptor();
        
        _jdo = getJDOManager(DBNAME, MAPPING);
        _jdo.setCallbackInterceptor(_i);
        _jdo.setInstanceFactory(_i);

        _db  = _jdo.getDatabase();

        OQLQuery oql;
        QueryResults qres;

        LOG.debug("Delete everything");
        _db.begin();
        oql = _db.getOQLQuery("SELECT p FROM "
                + TimeStampableObject.class.getName() + " p WHERE id=$1");
        oql.bind(TimeStampableObject.DEFAULT_ID);
        qres = oql.execute();
        while (qres.hasMore()) {
            _db.remove(qres.next());
        }
        oql.close();
        _db.commit();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) { _db.rollback(); }

        OQLQuery                oql;
        QueryResults            qres;

        LOG.debug("Delete everything");
        _db.begin();
        oql = _db.getOQLQuery("SELECT p FROM "
                + TimeStampableObject.class.getName() + " p WHERE id=$1");
        oql.bind(TimeStampableObject.DEFAULT_ID);
        qres = oql.execute();
        while (qres.hasMore()) {
            _db.remove(qres.next());
        }
        oql.close();
        _db.commit();

        _db.close();

        _jdo.setCallbackInterceptor(null);
    }
    
    public void testRun() throws Exception {
        TimeStampableObject     object;
        CallbacksInvoked        cbi = new CallbacksInvoked();

        cbi.allow(CallbacksInvoked.CREATING);
        cbi.allow(CallbacksInvoked.USING);
        cbi.allow(CallbacksInvoked.CREATED);
        cbi.allow(CallbacksInvoked.STORING);
        cbi.allow(CallbacksInvoked.RELEASING);

        _i._callbacksInvoked.init();
        _db.begin();

        object = new TimeStampableObject();
        LOG.debug("Creating new object: " + object);
        _db.create(object);

        _db.commit();

        if (!cbi.equals(_i._callbacksInvoked)) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
        }


        cbi.init();
        cbi.allow(CallbacksInvoked.USING);
        cbi.allow(CallbacksInvoked.LOADED);
        cbi.allow(CallbacksInvoked.STORING);
        cbi.allow(CallbacksInvoked.RELEASING);
        cbi.allow(CallbacksInvoked.INSTANTIATE);

        _i._callbacksInvoked.init();
        _db.begin();

        object = (TimeStampableObject) _db.load(
                TimeStampableObject.class,
                new Integer(TimeStampableObject.DEFAULT_ID));
        object.setValue1("Alan");

        _db.commit();

        if (!cbi.equals(_i._callbacksInvoked)) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
        }


        cbi.init();
        cbi.allow(CallbacksInvoked.USING);
        cbi.allow(CallbacksInvoked.UPDATED);
        cbi.allow(CallbacksInvoked.STORING);
        cbi.allow(CallbacksInvoked.RELEASING);

        _i._callbacksInvoked.init();

        object.setValue2("long transaction new value");
        _db.begin();
        _db.update(object);
        _db.commit();

        if (!cbi.equals(_i._callbacksInvoked)) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
        }


        cbi.init();
        cbi.allow(CallbacksInvoked.USING);
        cbi.allow(CallbacksInvoked.LOADED);
        cbi.allow(CallbacksInvoked.REMOVING);
        cbi.allow(CallbacksInvoked.REMOVED);
        cbi.allow(CallbacksInvoked.RELEASING);
        cbi.allow(CallbacksInvoked.INSTANTIATE);

        _i._callbacksInvoked.init();
        _db.begin();

        object = (TimeStampableObject) _db.load(
                TimeStampableObject.class,
                new Integer(TimeStampableObject.DEFAULT_ID));
        _db.remove(object);

        _db.commit();

        if (!cbi.equals(_i._callbacksInvoked)) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i._callbacksInvoked);
        }

    }

    @Ignore
    private class InternalCallbackInterceptor implements CallbackInterceptor, InstanceFactory {
        private CallbacksInvoked _callbacksInvoked = new CallbacksInvoked();
        private CallbacksInvoked _coverage = new CallbacksInvoked();

        public Class<?> loaded(final Object object, final AccessMode accessMode)
        throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.LOADED);
            _coverage.allow(CallbacksInvoked.LOADED);

            return object.getClass();
        }

        public void storing(final Object object, final boolean modified)
        throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.STORING);
            _coverage.allow(CallbacksInvoked.STORING);
        }

        public void creating(final Object object, final Database db)
        throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.CREATING);
            _coverage.allow(CallbacksInvoked.CREATING);
        }

        public void created(final Object object) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.CREATED);
            _coverage.allow(CallbacksInvoked.CREATED);
        }

        public void removing(final Object object) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.REMOVING);
            _coverage.allow(CallbacksInvoked.REMOVING);
        }

        public void removed(final Object object) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.REMOVED);
            _coverage.allow(CallbacksInvoked.REMOVED);
        }

        public void releasing(final Object object, final boolean committed) {
            _callbacksInvoked.allow(CallbacksInvoked.RELEASING);
            _coverage.allow(CallbacksInvoked.RELEASING);
        }

        public void using(final Object object, final Database db) {
            _callbacksInvoked.allow(CallbacksInvoked.USING);
            _coverage.allow(CallbacksInvoked.USING);
        }

        public void updated(final Object object) throws Exception {
            _callbacksInvoked.allow(CallbacksInvoked.UPDATED);
            _coverage.allow(CallbacksInvoked.UPDATED);
        }

        public Object newInstance(final String className, final ClassLoader loader) {
            _callbacksInvoked.allow(CallbacksInvoked.INSTANTIATE);
            _coverage.allow(CallbacksInvoked.INSTANTIATE);

            try {
                if (loader != null) { return loader.loadClass(className).newInstance(); }
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

    @Ignore
    private class CallbacksInvoked implements java.io.Externalizable {
        /** SerialVersionUID */
        private static final long serialVersionUID = -2946772385247299812L;
        
        public static final int LOADED      = 0;        
        public static final int STORING     = 1;
        public static final int CREATING    = 2;    
        public static final int CREATED     = 3;   
        public static final int REMOVING    = 4;   
        public static final int REMOVED     = 5;
        public static final int RELEASING   = 6;   
        public static final int USING       = 7;  
        public static final int UPDATED     = 8; 
        public static final int INSTANTIATE = 9;

        private static final short DONT_CARE = 0;       
        private static final short ALLOW     = 1;       
        private static final short DISALLOW  = 2;       

        private short[] _calls = new short[10];

        public CallbacksInvoked() {
            init();
        }

        public CallbacksInvoked(final int[] operations) {
            init();

            for (int i = 0; i < operations.length; i++) {
                allow(operations[i]);
            }
        }

        public void init() {
            for (int i = 0; i < _calls.length; i++) {
                _calls[i] = DISALLOW;
            }
        }
        
        public void dontCare(final int i) {
            if ((i < 0) || (i > _calls.length - 1)) { return; }
            _calls[i] = DONT_CARE;
        }
        
        public void allow(final int i) {
            if ((i < 0) || (i > _calls.length - 1)) { return; }
            _calls[i] = ALLOW;
        }

        public void disallow(final int i) {
            if ((i < 0) || (i > _calls.length - 1)) { return; }
            _calls[i] = DISALLOW;
        }

        public boolean equals(final Object object) {
            if (!(object instanceof CallbacksInvoked)) { return false; }

            CallbacksInvoked that = (CallbacksInvoked) object;
            for (int i = 0; i < _calls.length; i++) {
                if ((this._calls[i] != DONT_CARE)
                    && (that._calls[i] != DONT_CARE)
                    && (this._calls[i] != that._calls[i])) { return false; }
            }

            return true;
        }
        
        public int hashCode() {
            return _calls.hashCode();
        }

        public void writeExternal(final ObjectOutput out)
        throws IOException {
            for (int i = 0; i < _calls.length; i++) {
                out.writeShort(_calls[i]);
            }
        }
        
        public void readExternal(final ObjectInput in)
        throws IOException, ClassNotFoundException {
            for (int i = 0; i < _calls.length; i++) {
                _calls[i] = in.readShort();
            }
        }

        public String toString() {
            String str = "[";
            for (int i = 0; i < _calls.length; i++) {
                switch (_calls[i]) {
                case DONT_CARE:
                    str += "*";
                    break;
                case ALLOW:
                    str += "1";
                    break;
                case DISALLOW:
                    str += "0";
                    break;
                default:
                    fail("Unknown call type");
                }
            }
            str += "]";
            return str;
        }
    }
}

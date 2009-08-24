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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for the behaviors the InstanceFactory interface.
 */
public final class TestInstanceFactory extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestInstanceFactory.class);
    private static final String DBNAME = "test19";
    private static final String MAPPING = "/org/castor/cpa/test/test19/mapping.xml";
    private Database _db;
    private JDOManager _jdo;
    private CallbackStateInterceptor _i;

    public TestInstanceFactory(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _i = new CallbackStateInterceptor();
        
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
        TimeStampableObject object;
        CallbackState cbi = new CallbackState();

        cbi.init();
        cbi.allow(CallbackState.CREATING);
        cbi.allow(CallbackState.USING);
        cbi.allow(CallbackState.CREATED);
        cbi.allow(CallbackState.STORING);
        cbi.allow(CallbackState.RELEASING);

        _i.getCallbackState().init();
        _db.begin();

        object = new TimeStampableObject();
        LOG.debug("Creating new object: " + object);
        _db.create(object);

        _db.commit();

        if (!cbi.equals(_i.getCallbackState())) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
        }


        cbi.init();
        cbi.allow(CallbackState.USING);
        cbi.allow(CallbackState.LOADED);
        cbi.allow(CallbackState.STORING);
        cbi.allow(CallbackState.RELEASING);
        cbi.allow(CallbackState.INSTANTIATE);

        _i.getCallbackState().init();
        _db.begin();

        object = _db.load(TimeStampableObject.class,
                new Integer(TimeStampableObject.DEFAULT_ID));
        object.setValue1("Alan");

        _db.commit();

        if (!cbi.equals(_i.getCallbackState())) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
        }


        cbi.init();
        cbi.allow(CallbackState.USING);
        cbi.allow(CallbackState.UPDATED);
        cbi.allow(CallbackState.STORING);
        cbi.allow(CallbackState.RELEASING);

        _i.getCallbackState().init();

        object.setValue2("long transaction new value");
        _db.begin();
        _db.update(object);
        _db.commit();

        if (!cbi.equals(_i.getCallbackState())) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
        }


        cbi.init();
        cbi.allow(CallbackState.USING);
        cbi.allow(CallbackState.LOADED);
        cbi.allow(CallbackState.REMOVING);
        cbi.allow(CallbackState.REMOVED);
        cbi.allow(CallbackState.RELEASING);
        cbi.allow(CallbackState.INSTANTIATE);

        _i.getCallbackState().init();
        _db.begin();

        object = _db.load(TimeStampableObject.class,
                new Integer(TimeStampableObject.DEFAULT_ID));
        _db.remove(object);

        _db.commit();

        if (!cbi.equals(_i.getCallbackState())) {
            LOG.error("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
            fail("Callbacks were not properly invoked: "
                    + cbi + " != " + _i.getCallbackState());
        }

    }
}

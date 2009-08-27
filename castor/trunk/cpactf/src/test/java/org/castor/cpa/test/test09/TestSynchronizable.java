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
package org.castor.cpa.test.test09;

import java.util.ArrayList;
import java.util.List;

import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestSynchronizable extends CPATestCase {
    private static ArrayList<String> _synchronizables = new ArrayList<String>();
    private static final String DBNAME = "test09";
    private static final String MAPPING = "/org/castor/cpa/test/test09/mapping.xml";
    
    private Database _db;
    private Object _oldProperty;
    
    public static List<String> getSynchronizableList() {
        return _synchronizables;
    }

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestSynchronizable(final String name) {
        super(name);
        _synchronizables.clear();
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        AbstractProperties properties = getProperties();
        _oldProperty = properties.getObject(CPAProperties.TX_SYNCHRONIZABLE);
        properties.put(CPAProperties.TX_SYNCHRONIZABLE, SynchronizableImpl.class.getName());

        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void testSynchronizable() throws Exception {
        _db.begin();
        
        OQLQuery query = _db.getOQLQuery(
                "select o from " + Sample.class.getName() + " o");
        QueryResults result = query.execute();
        while (result.hasMore()) { _db.remove(result.next()); }
        result.close();
        _db.commit();
        
        _synchronizables.clear();
        
        // create a default TestObject
        _db.begin();
        _db.create(new Sample());
        _db.commit();
        
        // update TestObject the first time
        _db.begin();
        Sample st = _db.load(Sample.class,
                new Integer(Sample.DEFAULT_ID));
        st.setValue1(Sample.DEFAULT_VALUE_1 + Sample.DEFAULT_VALUE_1);
        st.setValue2(Sample.DEFAULT_VALUE_2 + Sample.DEFAULT_VALUE_2);
        _db.commit();
        
        // update TestObject the second time
        _db.begin();
        Sample lt = _db.load(Sample.class,
                new Integer(Sample.DEFAULT_ID));
        lt.setValue1(Sample.DEFAULT_VALUE_1);
        lt.setValue2(Sample.DEFAULT_VALUE_2);
        _db.commit();
        
        // create another default TestObject
        // should fail and rollback
        try {
            _db.begin();
            _db.create(new Sample());
            _db.commit();
        } catch (Exception ex) {
            _db.rollback();
        }

        // remove TestObject
        _db.begin();
        _db.remove(_db.load(Sample.class,
                new Integer(Sample.DEFAULT_ID)));
        _db.commit();
        
        assertTrue("TxSynchronizable should see 5 instead of "
                + _synchronizables.size() + " changes",
                _synchronizables.size() == 5);
        assertTrue("1. change of TxSynchronizable is wrong",
                "created:3 / one / two".equals(_synchronizables.get(0)));
        assertTrue("2. change of TxSynchronizable is wrong",
                "updated:3 / oneone / twotwo".equals(_synchronizables.get(1)));
        assertTrue("3. change of TxSynchronizable is wrong",
                "updated:3 / one / two".equals(_synchronizables.get(2)));
        assertTrue("4. change of TxSynchronizable is wrong",
                "rolledback".equals(_synchronizables.get(3)));
        assertTrue("5. change of TxSynchronizable is wrong",
                "deleted:3 / one / two".equals(_synchronizables.get(4)));
    }

    public void tearDown() throws Exception {
        _synchronizables.clear();
        _db.close();
        
        AbstractProperties properties = getProperties();
        if (_oldProperty != null) {
            properties.put(CPAProperties.TX_SYNCHRONIZABLE, _oldProperty);
        } else {
            properties.remove(CPAProperties.TX_SYNCHRONIZABLE);
        }
    }
}

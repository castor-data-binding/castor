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
package org.castor.cpa.test.test27;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for dependent relationship between data objects for long transaction. A
 * dependent object life cycle rely on its master object. For example, if the
 * master object is deleted, it will be deleted by Castor as well. If the
 * dependent object is dereferenced, it will be removed from the database.
 */
public final class TestDependentUpdate extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestDependentUpdate.class);
    private static final String DBNAME = "test27";
    private static final String MAPPING = "/org/castor/cpa/test/test27/mapping.xml";
    private Database _db;

    public TestDependentUpdate(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.ORACLE);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }
    
    public void testRun() throws PersistenceException, SQLException {
        delete();
        create();
        change();
        dependentUpdate();
    }
    
    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    private void delete() throws PersistenceException {
        OQLQuery oql;
        QueryResults qres;
        int cnt;

        LOG.debug("Delete everything");
        _db.begin();
        oql = _db.getOQLQuery("SELECT master FROM "
                + Master.class.getName() + " master");
        qres = oql.execute();

        for (cnt = 0; qres.hasMore(); cnt++) {
            _db.remove(qres.next());
        }
        LOG.debug("Deleting " + cnt + " master objects");

        oql = _db.getOQLQuery("SELECT group FROM " + Group.class.getName() + " group");
        qres = oql.execute();
        for (cnt = 0; qres.hasMore(); cnt++) {
            _db.remove(qres.nextElement());
        }
        LOG.debug("Deleting " + cnt + " group objects");
        _db.commit();
    }

    private void create() throws PersistenceException {
        Master master;
        Detail detail;
        Group group;

        LOG.debug("Attempt to create master with details");
        _db.begin();
        master = new Master();
        master.addDetail(new Detail(5));
        detail = new Detail(6);
        detail.addDetail2(new Detail2());
        detail.addDetail2(new Detail2());
        detail.setDetail3(new Detail3(101));
        master.addDetail(detail);
        detail = new Detail(7);
        detail.addDetail2(new Detail2());
        detail.addDetail2(new Detail2());
        master.addDetail(detail);
        group = new Group();
        _db.create(group);
        master.setGroup(group);
        _db.create(master);
        _db.commit();

        _db.begin();
        master = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        if (master != null) {
            if (master.getGroup() == null) {
                LOG.error("loaded master without group: " + master);
                fail("expecting group");
            } else if (master.getGroup().getId() != Group.DEFAULT_ID) {
                LOG.error("loaded master with wrong group: " + master);
                fail("incorrect group");
            }
            if (master.getDetails() == null
                    || !master.getDetails().contains(new Detail(5))
                    || !master.getDetails().contains(new Detail(6))
                    || !master.getDetails().contains(new Detail(7))) {
                LOG.error("loaded master without three details: " + master);
                fail("incorrect detail(s)");
            }
            detail = master.findDetail(5);
            if (detail.getDetails2() != null
                    && detail.getDetails2().size() != 0) {
                LOG.error("loaded detail 5 with details2: " + detail);
                fail("unexpected element found");
            }
            detail = master.findDetail(6);
            if (detail.getDetails2() == null
                    || detail.getDetails2().size() != 2) {
                LOG.error("loaded detail 6 without two details: " + detail);
                fail("details' size mismatch");
            }
            if (detail.getDetail3() == null
                    || detail.getDetail3().getId() != 101) {
                LOG.error("loaded detail 6 with wrong detail3: " + detail);
                fail("loaded detail 6 with wrong detail3: " + detail);
            }
            detail = master.findDetail(7);
            if (detail.getDetails2() == null
                    || detail.getDetails2().size() != 2) {
                LOG.error("loaded detail 7 without two details: " + detail);
                fail("details' size mismatch");
            }
        } else {
            LOG.error("failed to create master with details and group");
            fail("failed to create master with details and group");
        }
        _db.commit();
        LOG.debug("Created master with details: " + master);
    }
    
    private void change() throws PersistenceException {
        Master master;
        Detail detail;

        LOG.debug("Attempt to change details");
        _db.begin();
        master = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        if (master == null) {
            LOG.error("failed to find master with details group");
            fail("master not found");
        }
        // remove detail with id == 5
        master.getDetails().remove(master.getDetails().indexOf(master.findDetail(5)));
        // add new detail
        master.addDetail(new Detail(8));
        // add new detail and create it explicitely
        detail = new Detail(9);
        master.addDetail(detail);
        detail = master.findDetail(6);
        // change 1:1 dependent relationship
        detail.setDetail3(new Detail3(102));
        // delete, then create detail with id == 7 explicitly
        detail = master.findDetail(7);
        master.getDetails().remove(master.getDetails().indexOf(detail));
        master.addDetail(detail);
        _db.commit();

        _db.begin();
        master = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        if (master != null) {
            if (master.getDetails().size() == 0
                    || master.getDetails().contains(new Detail(5))
                    || !master.getDetails().contains(new Detail(6))
                    || master.findDetail(6).getDetails2() == null
                    || master.findDetail(6).getDetails2().size() != 2
                    || master.findDetail(6).getDetail3() == null
                    || master.findDetail(6).getDetail3().getId() != 102
                    || !master.getDetails().contains(new Detail(7))
                    || !master.getDetails().contains(new Detail(8))
                    || !master.getDetails().contains(new Detail(9))) {
                LOG.error("loaded master has wrong set of details: " + master);
                fail("loaded master has wrong set of details");
            } else {
                LOG.debug("Details changed correctly: " + master);
            }
        } else {
            LOG.error("master not found");
            fail("master not found");
        }
        _db.commit();
    }
    
    private void dependentUpdate() throws PersistenceException, SQLException {
        Master master;
        Detail detail;
        Master master2;
        Detail2 detail2;
        int detailId = 0;

        LOG.debug("Test long transaction with dirty checking");
        _db.begin();
        master = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        if (master == null) {
            LOG.error("failed to find master with details group");
            fail("master not found");
        }
        _db.commit();
        _db.begin();
        master2 = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        master2.setValue1(master2.getValue1() + "2");
        _db.commit();

        LOG.debug("Test 1");
        try {
            _db.begin();
            _db.update(master);
            _db.commit();
            LOG.error("Dirty checking doesn't work");
            fail("dirty check failed");
        } catch (ObjectModifiedException exept) {
            _db.rollback();
            LOG.debug("OK: Dirty checking works");
        }

        LOG.debug("Test 2");
        detail = new Detail(5);
        detail2 = new Detail2();
        detail.addDetail2(detail2);
        master2.addDetail(detail);
        master2.getDetails().remove(new Detail(8));
        master2.getDetails().remove(new Detail(9));
        try {
            _db.begin();
            _db.update(master2);
            _db.commit();
            detailId = detail2.getId();
            LOG.debug("OK: Dirty checking works");
        } catch (ObjectModifiedException exept) {
            _db.rollback();
            LOG.error("Dirty checking doesn't work");
            fail("dirty check failed");
        }

        LOG.debug("Test 3");
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        conn.createStatement().execute(
                "UPDATE test27_master SET value1='concurrent' WHERE id=" + master2.getId());
        _db.commit(); 
        master2.setValue1("long transaction new value");
        try {
            _db.begin();
            _db.update(master2);
            _db.commit();
            LOG.error("Dirty checking doesn't work");
            fail("dirty check failed");
        } catch (ObjectModifiedException except) {
            if (_db.isActive()) {
                _db.rollback();
            }

            LOG.debug("OK: Dirty checking works");
        }
        _db.begin();
        master = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        if (master != null) {
            if (master.getDetails().size() == 0
                    || !master.getDetails().contains(new Detail(5))
                    || master.findDetail(5).findDetail2(detailId) == null
                    || !master.getDetails().contains(new Detail(6))
                    || master.findDetail(6).getDetails2() == null
                    || master.findDetail(6).getDetails2().size() != 2
                    || !master.getDetails().contains(new Detail(7))
                    || master.getDetails().contains(new Detail(8))
                    || master.getDetails().contains(new Detail(9))) {
                LOG.error("loaded master has wrong set of details: " + master);
                fail("unexpect set of details");
            } else {
                LOG.debug("Details changed correctly in the long transaction: " + master);
            }
        } else {
            LOG.error("master not found");
            fail("master not found");
        }
        _db.commit();

        // modify an dependent object and see if it got updated
        LOG.debug("Test 3");
        detail = master.findDetail(5);
        detail.setValue1("new updated value");
        detail.findDetail2(detailId).setValue1("new detail 2 value");
        _db.begin();
        _db.update(master);
        _db.commit();

        _db.begin();
        master = (Master) _db.load(Master.class, new Integer(
                Master.DEFAULT_ID));
        if (master != null) {
            if (master.getDetails() == null
                    || master.getDetails().size() == 0
                    || !master.getDetails().contains(new Detail(5))
                    || !master.getDetails().contains(new Detail(6))
                    || !master.getDetails().contains(new Detail(7))
                    || master.getDetails().contains(new Detail(8))
                    || master.getDetails().contains(new Detail(9))
                    || !"new updated value".equals(master.findDetail(5)
                            .getValue1())
                    || master.findDetail(5).findDetail2(detailId) == null
                    || !"new detail 2 value".equals(master.findDetail(5)
                            .findDetail2(detailId).getValue1())) {

                LOG.error("loaded master has wrong set of details: " + master);
                fail("unexpected set of details");
            } else {
                LOG.debug("Details changed correctly in the long transaction: " + master);
            }
        } else {
            LOG.error("master not found");
            fail("master not found");
        }
        _db.commit();
    }
}

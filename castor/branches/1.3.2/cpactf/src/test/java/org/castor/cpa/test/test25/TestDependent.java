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
package org.castor.cpa.test.test25;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for dependent relationship between data objects.
 * A dependent object life cycle rely on its master object.
 * For example, if the master object is deleted, it will
 * be deleted by Castor as well. If the dependent object
 * is dereferenced, it will be removed from the database.
 */
public final class TestDependent extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestDependent.class);
    private static final String DBNAME = "test25";
    private static final String MAPPING = "/org/castor/cpa/test/test25/mapping.xml";
    private Database _db;

    /**
     * Constructor
     * 
     * @param category
     *            The test suite for these tests
     */
    public TestDependent(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)  
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }    
    
    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void testRun() throws PersistenceException {
        delete();
        create();
        change();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    private void delete() throws PersistenceException {
        OQLQuery oql;
        QueryResults qres;
        int cnt;

        LOG.debug("Delete everything");
        _db.begin();
        oql = _db.getOQLQuery(
                "SELECT master FROM " + Master.class.getName() + " master");
        qres = oql.execute();

        for (cnt = 0; qres.hasMore(); cnt++) {
            _db.remove(qres.next());
        }
        oql.close();
        LOG.debug("Deleting " + cnt + " master objects");

        oql = _db.getOQLQuery("SELECT group FROM " + Group.class.getName() + " group");
        qres = oql.execute();
        for (cnt = 0; qres.hasMore(); cnt++) {
            _db.remove(qres.nextElement());
        }
        oql.close();
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
        master.addDetail(detail);
        detail = new Detail(7);
        detail.setDetail3(new Detail3(101));
        detail.addDetail2(new Detail2());
        detail.addDetail2(new Detail2());
        master.addDetail(detail);
        group = new Group();
        _db.create(group);
        master.setGroup(group);
        _db.create(master);
        _db.commit();

        _db.begin();
        master = _db.load(Master.class, new Integer(Master.DEFAULT_ID));
        if (master != null) {
            if (master.getGroup() == null) {
                LOG.error("loaded master without group: " + master);
                fail("loaded master without group: " + master);
            } else if (master.getGroup().getId() != Group.DEFAULT_ID) {
                LOG.error("loaded master with wrong group: " + master);
                fail("loaded master with wrong group: " + master);
            }
            if (master.getDetails() == null
                    || !master.getDetails().contains(new Detail(5))
                    || !master.getDetails().contains(new Detail(6))
                    || !master.getDetails().contains(new Detail(7))) {
                LOG.error("loaded master without three details: " + master);
                fail("loaded master without three details: " + master);
            }
            detail = master.findDetail(5);
            if (detail.getDetails2() != null
                    && detail.getDetails2().size() != 0) {
                LOG.error("loaded detail 5 with details2: " + detail);
                fail("loaded detail 5 with details2: " + detail);
            }
            detail = master.findDetail(6);
            if (detail.getDetails2() == null
                    || detail.getDetails2().size() != 2) {
                LOG.error("loaded detail 6 without two details: " + detail);
                fail("loaded detail 6 without two details: " + detail);
            }
            detail = master.findDetail(7);
            if (detail.getDetails2() == null
                    || detail.getDetails2().size() != 2) {
                LOG.error("loaded detail 7 without two details: " + detail);
                fail("loaded detail 7 without two details: " + detail);
            }
            if (detail.getDetail3() == null
                    || detail.getDetail3().getId() != 101) {
                LOG.error("loaded detail 6 wrong detail3: " + detail);
                fail("loaded detail 7 wrong detail3: " + detail);
            }
        } else {
            LOG.error("failed to create master with details and group");
            fail("failed to create master with details and group");
        }
        LOG.debug("Created master with details: " + master);
        _db.commit();
    }
    
    private void change() throws PersistenceException {
        Master master;
        Detail detail;
        OQLQuery oql;
        QueryResults qres;

        LOG.debug("Attempt to change details");
        _db.begin();
        master = _db.load(Master.class, new Integer(Master.DEFAULT_ID));
        if (master == null) {
            LOG.error("failed to find master with details group");
            fail("failed to find master with details group");
        }
        // remove detail with id == 5
        master.getDetails().remove(master.getDetails().indexOf(master.findDetail(5)));
        // remove detail with id == 6 explicitly
        detail = master.findDetail(6);
        master.getDetails().remove(master.getDetails().indexOf(detail));
        detail = master.findDetail(7);
        detail.setDetail3(new Detail3(102));
        // add new detail
        master.addDetail(new Detail(8));
        // add new detail and create it explicitely
        detail = new Detail(9);
        master.addDetail(detail);
        // delete, then create detail with id == 7 explicitly
        detail = master.findDetail(7);
        master.getDetails().remove(master.getDetails().indexOf(detail));
        master.addDetail(detail);
        _db.commit();
        
        _db.begin();
        master = _db.load(Master.class, new Integer(Master.DEFAULT_ID));
        if (master != null) {
            if (master.getDetails().size() == 0
                    || master.getDetails().contains(new Detail(5))
                    || master.getDetails().contains(new Detail(6))
                    || !master.getDetails().contains(new Detail(7))
                    || !master.getDetails().contains(new Detail(8))
                    || !master.getDetails().contains(new Detail(9))) {
                LOG.error("loaded master has wrong set of details: " + master);
                fail("loaded master has wrong set of details: " + master);
            } else {
                LOG.debug("Details changed correctly: " + master);
            }
            detail = master.findDetail(7);
            if (detail.getDetail3() == null
                    || detail.getDetail3().getId() != 102) {
                LOG.error("loaded detail y wrong detail3: " + detail);
                fail("loaded detail 7 wrong detail3: " + detail);
            }
        } else {
            LOG.error("master not found");
            fail("master not found");
        }
        _db.commit();

        LOG.debug("Test OQL query");
        _db.begin();
        oql = _db.getOQLQuery("SELECT master FROM " + Master.class.getName()
                + " master WHERE master.details.value1=$1");
        oql.bind(Detail.DEFAULT_VALUE);
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.debug("OK: correct result of query 1 ");
        } else {
            LOG.error("incorrect result of query 1 ");
            fail("incorrect result of query 1");
        }
        oql.bind(Detail.DEFAULT_VALUE + "*");
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.error("incorrect result of query 2 ");
            fail("incorrect result of query 2");
        } else {
            LOG.debug("OK: correct result of query 2 ");
        }
        oql.close();
        oql = _db.getOQLQuery("SELECT master FROM " + Master.class.getName()
                + " master WHERE master.details.details2.value1=$1");
        oql.bind(Detail2.DEFAULT_VALUE);
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.debug("OK: correct result of query 3 ");
        } else {
            LOG.error("incorrect result of query 3 ");
            fail("incorrect result of query 3");
        }
        oql.bind(Detail2.DEFAULT_VALUE + "*");
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.error("incorrect result of query 4 ");
            fail("incorrect result of query 4");
        } else {
            LOG.debug("OK: correct result of query 4 ");
        }
        oql.close();
        oql = _db.getOQLQuery("SELECT master FROM " + Master.class.getName()
                + " master WHERE master.group=$1");
        oql.bind(Group.DEFAULT_ID);
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.debug("OK: correct result of query 5 ");
        } else {
            LOG.error("incorrect result of query 5 ");
            fail("incorrect result of query 5");
        }
        oql.close();
        _db.commit();
    }
}

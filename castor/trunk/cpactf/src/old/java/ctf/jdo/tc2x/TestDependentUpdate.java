/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc2x;

import java.sql.Connection;
import java.sql.SQLException;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectModifiedException;

/**
 * Test for dependent relationship between data objects for long transaction. A
 * dependent object life cycle rely on its master object. For example, if the
 * master object is deleted, it will be deleted by Castor as well. If the
 * dependent object is dereferenced, it will be removed from the database.
 */
public final class TestDependentUpdate extends CastorTestCase {
    private static final Log LOG = LogFactory.getLog(TestDependentUpdate.class);

    private Connection _conn;
    private JDOCategory _category;
    private Database _db;

    public TestDependentUpdate(final TestHarness category) {
        super(category, "TC27", "Dependent update objects tests");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit(false);
    }
    
    public void runTest() throws PersistenceException, SQLException {
        delete();
        create();
        change();
        test();
    }
    
    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
        _conn.close();
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
    
    private void test() throws PersistenceException, SQLException {
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
        _conn.createStatement().execute(
                "UPDATE tc2x_master SET value1='concurrent' WHERE id=" + master2.getId());
        _conn.commit();
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

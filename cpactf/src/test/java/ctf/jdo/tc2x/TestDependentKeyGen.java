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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc2x;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Test for dependent relationship between data objects.
 * A dependent object life cycle rely on its master object.
 * For example, if the master object is deleted, it will
 * be deleted by Castor as well. If the dependent object
 * is dereferenced, it will be removed from the database.
 */
public final class TestDependentKeyGen extends CastorTestCase {
    private static final Log LOG = LogFactory.getLog(TestDependentKeyGen.class);
    
    private JDOCategory    _category;
    private Database       _db;
    private int            _masterId, _detailId5, _detailId6, _detailId7;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestDependentKeyGen(final TestHarness category) {
        super(category, "TC26", "Dependent objects tests");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException {
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
                "SELECT master FROM " + MasterKeyGen.class.getName() + " master");
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
        MasterKeyGen master;
        DetailKeyGen detail, detail5, detail6, detail7;
        Group group;

        LOG.debug("Attempt to create master with details");
        _db.begin();
        master = new MasterKeyGen();
        detail5 = new DetailKeyGen();
        master.addDetail(detail5);
        detail6 = new DetailKeyGen();
        detail6.addDetail2(new DetailKeyGen2());
        detail6.addDetail2(new DetailKeyGen2());
        master.addDetail(detail6);
        detail7 = new DetailKeyGen();
        detail7.addDetail2(new DetailKeyGen2());
        detail7.addDetail2(new DetailKeyGen2());
        detail7.setDetail3(new DetailKeyGen3());
        master.addDetail(detail7);
        group = new Group();
        _db.create(group);

        master.setGroup(group);
        _db.create(master);
        _db.commit();

        _masterId = master.getId();
        _detailId5 = detail5.getId();
        _detailId6 = detail6.getId();
        _detailId7 = detail7.getId();

        _db.begin();
        master = (MasterKeyGen) _db.load(MasterKeyGen.class, new Integer(_masterId));
        if (master != null) {
            if (master.getGroup() == null) {
                LOG.error("loaded master without group: " + master);
                fail("loaded master without group: " + master);
            } else if (master.getGroup().getId() != Group.DEFAULT_ID) {
                LOG.error("loaded master with wrong group: " + master);
                fail("loaded master with wrong group: " + master);
            }
            if (master.getDetails() == null
                    || !master.getDetails().contains(
                            new DetailKeyGen(_detailId5))
                    || !master.getDetails().contains(
                            new DetailKeyGen(_detailId6))
                    || !master.getDetails().contains(
                            new DetailKeyGen(_detailId7))) {
                LOG.error("loaded master without three details: " + master);
                fail("loaded master without three details: " + master);
            }
            detail = master.findDetail(_detailId5);
            if (detail.getDetails2() != null
                    && detail.getDetails2().size() != 0) {
                LOG.error("loaded detail 5 with details2: " + detail);
                fail("loaded detail 5 with details2: " + detail);
            }
            if (detail.getDetail3() != null) {
                LOG.error("loaded detail 5 with unexpected details3: " + detail);
                fail("loaded detail 5 with unexpected details3: " + detail);
            }
            detail = master.findDetail(_detailId6);
            if (detail.getDetails2() == null
                    || detail.getDetails2().size() != 2) {
                LOG.error("loaded detail 6 without two details: " + detail);
                fail("loaded detail 6 without two details2: " + detail);
            }
            if (detail.getDetail3() != null) {
                LOG.error("loaded detail 6 with unexpected details3: " + detail);
                fail("loaded detail 6 with unexpected details3: " + detail);
            }

            detail = master.findDetail(_detailId7);
            if (detail.getDetails2() == null
                    || detail.getDetails2().size() != 2) {
                LOG.error("loaded detail 7 without two details: " + detail);
                fail("loaded detail 7 without two details2: " + detail);
            }
            if (detail.getDetail3() == null) {
                LOG.error("loaded detail 7 without the expected details3: " + detail);
                fail("loaded detail 7 without the expected details3: " + detail);
            }
        } else {
            LOG.error("failed to create master with details and group");
            fail("failed to create master with details and group");
        }
        LOG.debug("Created master with details: " + master);
        _db.commit();
    }
    
    private void change() throws PersistenceException {
        MasterKeyGen master;
        DetailKeyGen detail6, detail7, detail8, detail9;
        int detailId8, detailId9;
        OQLQuery oql;
        QueryResults qres;

        LOG.debug("Attempt to change details");
        _db.begin();
        master = (MasterKeyGen) _db.load(MasterKeyGen.class, new Integer(_masterId));
        if (master == null) {
            LOG.error("failed to find master with details group");
            fail("failed to find master with details group");
        }
        // remove detail with id == 5
        master.getDetails().remove(
                master.getDetails().indexOf(master.findDetail(_detailId5)));
        // remove detail with id == 6 explicitly
        detail6 = master.findDetail(_detailId6);
        master.getDetails().remove(master.getDetails().indexOf(detail6));
        // add new detail
        detail8 = new DetailKeyGen();
        master.addDetail(detail8);
        // add new detail and create it explicitely
        detail9 = new DetailKeyGen();
        master.addDetail(detail9);
        // delete, then create detail with id == 7 explicitly
        detail7 = master.findDetail(_detailId7);
        master.getDetails().remove(master.getDetails().indexOf(detail7));
        master.addDetail(detail7);
        _db.commit();
        
        detailId8 = detail8.getId();
        detailId9 = detail9.getId();

        _db.begin();
        master = (MasterKeyGen) _db.load(MasterKeyGen.class, new Integer(_masterId));
        if (master != null) {
            if (master.getDetails().size() == 0
                    || master.getDetails().contains(new DetailKeyGen(_detailId5))
                    || master.getDetails().contains(new DetailKeyGen(_detailId6))
                    || !master.getDetails().contains(new DetailKeyGen(_detailId7))
                    || !master.getDetails().contains(new DetailKeyGen(detailId8))
                    || !master.getDetails().contains(new DetailKeyGen(detailId9))) {
                LOG.error("loaded master has wrong set of details: " + master);
                fail("loaded master has wrong set of details: " + master);
            } else {
                LOG.debug("Details changed correctly: " + master);
            }
        } else {
            LOG.error("master not found");
            fail("master not found");
        }
        _db.commit();

        LOG.debug("Test OQL query");
        _db.begin();
        oql = _db.getOQLQuery("SELECT master FROM " + MasterKeyGen.class.getName()
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
        oql = _db.getOQLQuery("SELECT master FROM " + MasterKeyGen.class.getName()
                + " master WHERE master.details.details2.value1=$1");
        oql.bind(DetailKeyGen2.DEFAULT_VALUE);
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.debug("OK: correct result of query 3 ");
        } else {
            LOG.error("incorrect result of query 3 ");
            fail("incorrect result of query 3");
        }
        oql.bind(DetailKeyGen2.DEFAULT_VALUE + "*");
        qres = oql.execute();
        if (qres.hasMore()) {
            LOG.error("incorrect result of query 4 ");
            fail("incorrect result of query 4");
        } else {
            LOG.debug("OK: correct result of query 4 ");
        }
        oql.close();
        oql = _db.getOQLQuery("SELECT master FROM " + MasterKeyGen.class.getName()
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

        LOG.debug("Test rollback");
        _db.begin();
        master = (MasterKeyGen) _db.load(MasterKeyGen.class, new Integer(_masterId));
        int detailsCount = master.getDetails().size();
        _db.rollback();
        if (detailsCount != master.getDetails().size()) {
            LOG.error((master.getDetails().size() - detailsCount)
                    + " details added in rollback");
            fail("Details added in rollback");
        }
    }
}

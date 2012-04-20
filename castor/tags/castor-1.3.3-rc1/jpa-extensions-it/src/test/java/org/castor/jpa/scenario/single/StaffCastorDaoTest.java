/*
 * Copyright 2009 Werner Guttmann
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
package org.castor.jpa.scenario.single;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.spring.orm.CastorObjectRetrievalFailureException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is part of the functional test suite for Castor JDO
 * and assists in testing JPA annotation support.
 * 
 * @author Werner Guttmann
 * @since 1.3.1
 */
@ContextConfiguration(locations = { "spring-config.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class StaffCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    private static Log LOG = LogFactory.getLog(StaffCastorDaoTest.class);

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private StaffDao staffDao;

    @Test
    @Transactional
    public void save() {
        Staff staff = new Staff();
        staff.setStaff_id(1234);
        staff.setFirst_name("Bob");
        staff.setLast_name("Builder");
        staff.setEarning(123.45);
        staff.setOn_vacation(false);

        this.staffDao.save(staff);

        Staff st = this.staffDao.getStaff(1234);
        assertNotNull(st);
        assertEquals("Bob", st.getFirst_name());
        assertEquals(123.45, st.getEarning(), 0.1);
    }

    @Test
    // @Transactional
    @Rollback(false)
    public void update() throws PersistenceException {
        Database db = jdoManager.getDatabase();
        assertNotNull(db);

        Staff staff = new Staff();
        staff.setStaff_id(1236);
        staff.setFirst_name("Bab");
        staff.setLast_name("Builder");

        db.begin();
        this.staffDao.save(staff);
        db.commit();

        db.begin();
        Staff toUpdate = this.staffDao.getStaff(1236);
        assertNotNull(toUpdate);
        toUpdate.setFirst_name("Bill");

        LOG.debug("try updating");
        // this.staffDao.save(toUpdate);
        db.commit();

        db.begin();
        Staff updated = this.staffDao.getStaff(1236);
        assertNotNull(updated);
        assertEquals("Bill", updated.getFirst_name());

        this.staffDao.delete(updated);

        db.commit();
        db.close();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void saveWithDelete() throws PersistenceException {
        Database db = jdoManager.getDatabase();
        assertNotNull(db);

        Staff staff = new Staff();
        staff.setStaff_id(1235);
        staff.setFirst_name("First");
        staff.setLast_name("Last");
        staff.setEarning(1023.12);

        db.begin();
        LOG.debug("save First, Last");
        this.staffDao.save(staff);
        db.commit();

        Staff st = this.staffDao.getStaff(1235);
        assertNotNull(st);
        assertEquals("First", st.getFirst_name());

        db.begin();
        LOG.debug("delete First, Last");
        this.staffDao.delete(st);
        db.commit();

        try {
            db.begin();
            this.staffDao.getStaff(1235);
            fail();
        } catch (CastorObjectRetrievalFailureException e) {
            LOG.debug("First, Last not found in DB");
        } finally {
            db.commit();
        }
    }
}

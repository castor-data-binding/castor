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
package org.castor.jpa.scenario.one_to_many;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.castor.spring.orm.CastorObjectRetrievalFailureException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RoleCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private RoleDao roleDao;

    @Test
    @Transactional
    public void save() {

        Actor actor = new Actor();
        actor.setSvnr(1234567890);
        actor.setFirstname("Max");
        actor.setLastname("Mustermann");

        Role bc = new Role();
        bc.setId(1234);
        bc.setName("Der Bulle");
        bc.setActor(actor);

        this.roleDao.saveRole(bc);

        Role x = this.roleDao.getRole(1234);
        assertNotNull(x);
        assertEquals("Der Bulle", x.getName());
        assertEquals("Max", x.getActor().getFirstname());
        assertEquals("Mustermann", x.getActor().getLastname());
        assertEquals(1234567890, x.getActor().getSvnr());
    }

    @Test
    @Transactional
    public void update() throws PersistenceException {
        Database db = jdoManager.getDatabase();
        assertNotNull(db);

        Actor actor = new Actor();
        actor.setSvnr(1234567890);
        actor.setFirstname("Max");
        actor.setLastname("Mustermann");

        Role bc = new Role();
        bc.setId(1234);
        bc.setName("Der Bulle");
        bc.setActor(actor);

        db.begin();
        this.roleDao.saveRole(bc);
        db.commit();

        db.begin();

        Role toUpdate = roleDao.getRole(1234);
        assertNotNull(toUpdate);
        assertEquals("Der Bulle", toUpdate.getName());
        assertEquals("Max", toUpdate.getActor().getFirstname());

        Actor act = new Actor();
        act.setSvnr(1234);
        act.setFirstname("First");
        act.setLastname("Last");

        toUpdate.setActor(act);
        toUpdate.setName("Updated");

        db.commit();

        Role updated = this.roleDao.getRole(1234);

        assertNotNull(updated);
        assertEquals("Updated", updated.getName());
        assertEquals("First", updated.getActor().getFirstname());

        db.begin();
        this.roleDao.deleteRole(updated);
        db.commit();
        db.close();
    }

    @Test
    @Transactional
    public void delete() {

        Actor actor = new Actor();
        actor.setSvnr(1234567890);
        actor.setFirstname("Max");
        actor.setLastname("Mustermann");

        Role bc = new Role();
        bc.setId(1234);
        bc.setName("Der Bulle");
        bc.setActor(actor);

        this.roleDao.saveRole(bc);

        Role got = roleDao.getRole(1234);
        assertNotNull(got);
        assertEquals("Der Bulle", got.getName());

        roleDao.deleteRole(got);

        try {
            roleDao.getRole(1234);
            fail();
        } catch (CastorObjectRetrievalFailureException e) {
        }
    }
}

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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.castor.spring.orm.CastorObjectRetrievalFailureException;
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
public class ActorCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ActorDao actorDao;

    @Test
    @Transactional
    public void save() {
        Collection<Role> roles = new ArrayList<Role>();
        Role newRole = new Role();
        newRole.setId(1234);
        newRole.setName("Der Bulle");
        roles.add(newRole);

        Actor actor = new Actor();
        actor.setSvnr(1234567890);
        actor.setFirstname("Max");
        actor.setLastname("Mustermann");
        actor.setRoles(roles);

        this.actorDao.saveActor(actor);

        Actor x = this.actorDao.getActor(1234567890);
        assertEquals("Max", x.getFirstname());
        assertEquals("Mustermann", x.getLastname());
        assertEquals(1234567890, x.getSvnr());
        assertEquals(1, x.getRoles().size());
        Role role = x.getRoles().iterator().next();
        assertEquals(1234, role.getId());
        assertEquals("Der Bulle", role.getName());
    }

    @Test
    @Transactional
    public void delete() {

        Actor actor = new Actor();
        actor.setSvnr(1234567890);
        actor.setFirstname("Max");
        actor.setLastname("Mustermann");

        this.actorDao.saveActor(actor);

        Actor got = actorDao.getActor(1234567890);
        assertEquals("Max", got.getFirstname());
        assertEquals("Mustermann", got.getLastname());

        actorDao.deleteActor(got);

        try {
            actorDao.getActor(1234567890);
            fail();
        } catch (CastorObjectRetrievalFailureException e) {
        }

    }

}

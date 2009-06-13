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
package org.castor.jpa.scenario.one_to_one;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.castor.spring.orm.CastorObjectRetrievalFailureException;
import org.exolab.castor.jdo.JDOManager;
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
public class TimeslotCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JDOManager jdoManager;

    @Autowired
    private TimeslotDao timeslotDao;

    @Test
    @Transactional
    public void save() {

        Timeslot timeslot = new Timeslot();
        timeslot.setId(1);
        timeslot.setTitle("ErsteShow");
        this.timeslotDao.save(timeslot);

        Timeslot tag = this.timeslotDao.getTimeslot(1);
        assertNotNull(tag);
        assertEquals("ErsteShow", tag.getTitle());
    }

    @Test
    @Transactional
    public void saveWithDelete() {
        Timeslot timeslot_to_save = new Timeslot();
        timeslot_to_save.setId(2);
        timeslot_to_save.setTitle("ZweiterTimeslot");
        this.timeslotDao.save(timeslot_to_save);

        Timeslot timeslot_to_delete = this.timeslotDao.getTimeslot(2);
        assertNotNull(timeslot_to_delete);
        assertEquals("ZweiterTimeslot", timeslot_to_delete.getTitle());

        this.timeslotDao.delete(timeslot_to_delete);

        try {
            this.timeslotDao.getTimeslot(2);
            fail();
        } catch (CastorObjectRetrievalFailureException e) {
        }

    }

}

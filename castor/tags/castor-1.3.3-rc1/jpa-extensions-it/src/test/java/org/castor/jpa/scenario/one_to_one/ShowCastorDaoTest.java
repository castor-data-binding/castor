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
public class ShowCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private ShowDao showDao;

    @Test
    @Transactional
    public void save() {
        Timeslot timeslot = new Timeslot();
        timeslot.setId(3);
        timeslot.setTitle("Timeslot-ErsteShow");

        Show show = new Show();
        show.setId(1);
        show.setName("ErsteShow");
        show.setTimeslot(timeslot);
        this.showDao.save(show);

        Show tag = this.showDao.getShow(1);
        assertNotNull(tag);
        assertEquals("ErsteShow", tag.getName());

        assertNotNull(show.getTimeslot());
        assertEquals(3, tag.getTimeslot().getId());
        assertEquals("Timeslot-ErsteShow", tag.getTimeslot().getTitle());
    }

    @Test
    @Transactional
    public void update() throws PersistenceException {
        Database db = jdoManager.getDatabase();
        assertNotNull(db);

        Timeslot timeslot = new Timeslot();
        timeslot.setId(2);
        timeslot.setTitle("myTimeslot");

        Show show = new Show();
        show.setId(2);
        show.setName("myShow");
        show.setTimeslot(timeslot);

        db.begin();
        this.showDao.save(show);
        db.commit();

        db.begin();
        Show update_show = this.showDao.getShow(2);
        assertNotNull(update_show);
        assertEquals("myShow", update_show.getName());

        update_show.setName("myShowUpdate");
        db.commit();

        db.begin();

        Show test_update_show = this.showDao.getShow(2);
        assertNotNull(test_update_show);
        assertEquals("myShowUpdate", test_update_show.getName());
        assertEquals("myTimeslot", test_update_show.getTimeslot().getTitle());

        test_update_show.getTimeslot().setTitle("myTimeslotUpdate");
        db.commit();

        Show test_timeslotupdate_show = this.showDao.getShow(2);
        assertNotNull(test_timeslotupdate_show);
        assertEquals("myTimeslotUpdate", test_timeslotupdate_show.getTimeslot()
                .getTitle());

        db.begin();
        this.showDao.delete(test_timeslotupdate_show);
        db.commit();
        db.close();
    }

    @Test
    @Transactional
    public void delete() {
        Show show_to_save = new Show();
        show_to_save.setId(2);
        show_to_save.setName("ZweiteShow");
        show_to_save.setTimeslot(new Timeslot());
        this.showDao.save(show_to_save);

        Show show_to_delete = this.showDao.getShow(2);
        assertNotNull(show_to_delete);
        assertEquals("ZweiteShow", show_to_delete.getName());

        this.showDao.delete(show_to_delete);

        try {
            this.showDao.getShow(2);
            fail();
        } catch (CastorObjectRetrievalFailureException e) {
        }
    }

    @Test
    @Transactional
    public void saveWithRating() {
        OneToOne_rating rating = new OneToOne_rating();
        rating.setId(1);
        rating.setValue(5);

        Show show_to_save = new Show();
        show_to_save.setId(9);
        show_to_save.setName("myShow");
        show_to_save.setTimeslot(new Timeslot());
        show_to_save.setRating(rating);
        this.showDao.save(show_to_save);

        Show got = this.showDao.getShow(9);
        assertNotNull(got);
        assertNotNull(got.getRating());
        assertEquals(1, got.getRating().getId());
        assertEquals(5, got.getRating().getValue());

    }
}

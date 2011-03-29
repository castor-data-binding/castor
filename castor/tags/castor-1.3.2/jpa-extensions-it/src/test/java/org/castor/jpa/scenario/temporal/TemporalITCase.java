package org.castor.jpa.scenario.temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TemporalITCase {

    private static final long ID = 1L;

    @Autowired
    private JDOManager jdoManager;

    @Before
    public void initDb() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    public void cleanDb() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    @Test
    public void temporalIsConvertedAsExpected() throws Exception {
        final Database db = jdoManager.getDatabase();
        final Person personToPersist = new Person();
        personToPersist.setId(ID);
        final Calendar calendar = new GregorianCalendar();
        calendar.set(1969, 0, 1);
        final Date date = calendar.getTime();
        personToPersist.setBirthDate(date);
        personToPersist.setAnotherDate(date);
        personToPersist.setYetAnotherDate(date);

        db.begin();
        db.create(personToPersist);
        db.commit();

        db.begin();
        final Person loadedPerson = db.load(Person.class, ID);
        db.commit();
        
        assertNotNull(loadedPerson);
        assertEquals(date, loadedPerson.getBirthDate());
        assertEquals(date, loadedPerson.getAnotherDate());
        assertEquals(date, loadedPerson.getYetAnotherDate());
    }

}

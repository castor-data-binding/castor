package org.castor.jpa.scenario.callbacks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.spring.orm.CastorSystemException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager",
        defaultRollback = true)
@Transactional
public class CallbackHooksITCase {

    public static final Log LOG = LogFactory.getLog(CallbackHooksITCase.class);
    @Autowired
    protected JDOManager jdoManager;
    @Autowired
    private PersonDao personDao;

    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final long ID_3 = 3L;

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
    public void prePersistCallbackHook() throws Exception {
        final Person personToPersist = new Person();
        personToPersist.setId(ID_1);
        personToPersist.setName("Max Mustermann");
        try {
            personDao.save(personToPersist);
            fail("Should throw exceptions for person.");
        } catch (CastorSystemException e) {
            LOG.debug("Exceptions for person thrown as expected: "
                    + e.getMessage());
        }

        final Cat catToPersist = new Cat();
        catToPersist.setId(ID_1);
        catToPersist.setName("Waldi");
        try {
            final Database db = jdoManager.getDatabase();
            db.begin();
            db.create(catToPersist);
            db.commit();
            fail("Should throw exceptions for cat.");
        } catch (PersistenceException e) {
            LOG.debug("Exceptions for cat thrown as expected: "
                    + e.getMessage());
        }
    }

    @Test
    public void postPersistCallbackHook() throws Exception {
        final Person personToPersist = new Person();
        personToPersist.setId(ID_1);
        personToPersist.setName("Manfred Mustermann");
        try {
            personDao.save(personToPersist);
            fail("Should throw exceptions.");
        } catch (CastorSystemException e) {
            LOG.debug("Exceptions thrown as expected: " + e.getMessage());
        }
    }

    @Test
    public void preRemoveCallbackHook() throws Exception {
        final Person personToPersist = new Person();
        personToPersist.setId(ID_2);
        personToPersist.setName("Max Musterfrau");
        personDao.save(personToPersist);

        final Person loadedPerson = personDao.getById(ID_2);
        assertNotNull(loadedPerson);
        try {
            personDao.delete(personToPersist);
            fail("Should throw exceptions.");
        } catch (CastorSystemException e) {
            LOG.debug("Exceptions thrown as expected: " + e.getMessage());
        }
    }

    @Test
    public void postRemoveCallbackHook() throws Exception {
        final Person personToPersist = new Person();
        personToPersist.setId(ID_2);
        personToPersist.setName("Manfred Musterfrau");
        personDao.save(personToPersist);

        final Person loadedPerson = personDao.getById(ID_2);
        assertNotNull(loadedPerson);
        try {
            personDao.delete(personToPersist);
            fail("Should throw exceptions.");
        } catch (CastorSystemException e) {
            LOG.debug("Exceptions thrown as expected: " + e.getMessage());
        }
    }

    @Test
    public void preUpdateCallbackHook() throws Exception {
        final Database db = jdoManager.getDatabase();
        final Person personToPersist = new Person();
        personToPersist.setId(ID_2);
        personToPersist.setName("Manfred Musterfrau");
        db.begin();
        db.create(personToPersist);
        db.commit();

        try {
            db.begin(); // TODO: note that `PostLoad` CB's only called here.
            final Person loadedPerson = db.load(Person.class, ID_2);
            assertNotNull(loadedPerson);
            loadedPerson.setName("Max Musterfrau");
            db.commit();
            fail("Should throw exceptions.");
        } catch (PersistenceException e) {
            LOG.debug("Exceptions thrown as expected: " + e.getMessage());
        }
    }

    @Test
    public void postUpdateCallbackHook() throws Exception {
        final Database db = jdoManager.getDatabase();
        final Person personToPersist = new Person();
        personToPersist.setId(ID_3);
        personToPersist.setName("Manfred Musterfrau");
        db.begin();
        db.create(personToPersist);
        db.commit();

        try {
            db.begin(); // TODO: note that `PostLoad` CB's only called here.
            final Person loadedPerson = db.load(Person.class, ID_3);
            assertNotNull(loadedPerson);
            loadedPerson.setName("Hans Wurst");
            db.commit();
            fail("Should throw exceptions.");
        } catch (PersistenceException e) {
            LOG.debug("Exceptions thrown as expected: " + e.getMessage());
        }
    }

}

package org.castor.jpa.scenario.version;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class VersionITCase {

    public final Log LOG = LogFactory.getLog(getClass());

    @Autowired
    protected JDOManager jdoManager;

    Database db;
    CacheManager cacheManager;

    @Before
    @Transactional
    public void initDb() throws PersistenceException {
        db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    @Transactional
    public void cleanDb() throws PersistenceException {
        if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    @Test
    @Transactional
    public void versionWillBeAlteredUponUpdate() throws Exception {
        OftenUsed object = createPersistentObject(OftenUsed.class);
        long version1 = object.getVersion();

        changeName(object, "name");

        long version2 = object.getVersion();
        assertTrue(version1 != version2);
    }

    @Test(expected = ObjectModifiedException.class)
    @Transactional
    public void exceptionWillBeThrownUponConcurrentModification()
            throws Exception {
        OftenUsed object = createPersistentObject(OftenUsed.class);

        OftenUsed failing = loadPersistentObject(OftenUsed.class,
                object.getId());

        changeName(object, "name");

        changeName(failing, "failing");
    }

    @Test
    @Transactional
    public void versionWillBeAlteredUponUpdateOfNonCachingEntity()
            throws Exception {
        NonCached object = createPersistentObject(NonCached.class);
        long version1 = object.getVersion();

        changeName(object, "name");

        long version2 = object.getVersion();
        assertTrue(version1 != version2);
    }

    @Test
    @Transactional
    public void exceptionWillBeThrownUponConcurrentModificationOfNonCachingEntity()
            throws Exception {
        NonCached object = createPersistentObject(NonCached.class);

        NonCached failing = loadPersistentObject(NonCached.class,
                object.getId());

        changeName(object, "name");

        changeName(failing, "failing");
    }

    private <T extends VersionTest> T createPersistentObject(Class<T> k)
            throws Exception {
        T instance = k.newInstance();
        db.begin();
        db.create(instance);
        db.commit();

        return instance;
    }

    @SuppressWarnings("unchecked")
    private <T extends VersionTest> T loadPersistentObject(Class<T> k, long id) throws Exception {
        db.begin();
        T loaded = (T) db
                .getOQLQuery(
                        "select s from " + k.getCanonicalName()
                                + " s where s.id=" + id).execute().next();

        db.commit();
        return loaded;
    }

    private void changeName(VersionTest object, String name) throws Exception {
        db.begin();
        object.setName(name);
        db.update(object);
        db.commit();
    }
}

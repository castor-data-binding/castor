package org.castor.jpa.scenario.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
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
public class CacheITCase {

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
    public void unlimitedCachingEntityIsCachedUponPersisting() throws Exception {
        cleanUpTableIfNeeded(UnlimitedCachingEntity.class);
        createPersistentObject(UnlimitedCachingEntity.class);
        assertTrue(isEntityCached(UnlimitedCachingEntity.class));

    }

    @Test
    @Transactional
    public void limitedCachingEntityIsCachedUponPersisting() throws Exception {
        cleanUpTableIfNeeded(LimitedCachingEntity.class);
        createPersistentObject(LimitedCachingEntity.class);
        assertTrue(isEntityCached(LimitedCachingEntity.class));
    }

    @Test
    @Transactional
    public void nonCachingEntityIsNotCachedUponPersisting() throws Exception {
        cleanUpTableIfNeeded(NonCachingEntity.class);
        createPersistentObject(NonCachingEntity.class);
        assertFalse(isEntityCached(NonCachingEntity.class));
    }

    private <T extends CacheTestEntity> void cleanUpTableIfNeeded(Class<T> k)
            throws Exception {
        db.begin();
        T toDelete = null;
        try {
            toDelete = db.load(k, 1l);
        } catch (Exception e) {

        }
        if (toDelete != null) {
            db.remove(toDelete);
        }
        db.commit();
    }

    private <T extends CacheTestEntity> void createPersistentObject(Class<T> k)
            throws Exception {
        T person = k.newInstance();
        person.setId(1l);
        person.setName("name");

        db.begin();
        db.create(person);
        db.commit();
    }

    private <T extends CacheTestEntity> boolean isEntityCached(Class<T> k)
            throws Exception {
        db.begin();
        cacheManager = db.getCacheManager();
        boolean isCached = cacheManager.isCached(k, 1l);
        db.commit();

        return isCached;
    }

}

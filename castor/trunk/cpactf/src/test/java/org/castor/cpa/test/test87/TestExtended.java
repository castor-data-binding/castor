package org.castor.cpa.test.test87;

import java.sql.Connection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestExtended extends CPATestCase {
    private static final String DBNAME = "test87";
    private static final String MAPPING = "/org/castor/cpa/test/test87/mapping.xml";
    
    private static final Log LOG = LogFactory.getLog(TestExtended.class);
    
    private static long _baseTimestamp;
    private static long _extTimestamp;
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestExtended.class.getName());

        suite.addTest(new TestExtended("delete"));
        suite.addTest(new TestExtended("create"));
        suite.addTest(new TestExtended("loadBase"));
        suite.addTest(new TestExtended("loadExtended"));
        suite.addTest(new TestExtended("queryBase"));
        suite.addTest(new TestExtended("queryExtended"));
        suite.addTest(new TestExtended("updateBaseShort"));
        suite.addTest(new TestExtended("updateExtendedShort"));
        suite.addTest(new TestExtended("updateBaseLongCached"));
        suite.addTest(new TestExtended("updateExtendedLongCached"));
        suite.addTest(new TestExtended("updateBaseLongExpired"));
        suite.addTest(new TestExtended("updateExtendedLongExpired"));
        suite.addTest(new TestExtended("remove"));

        return suite;
    }

    public TestExtended(final String name) {
        super(name);
    }

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM test87_extended");
        conn.createStatement().execute("DELETE FROM test87_base");
        conn.close();
        
        LOG.debug("Deleted all records from database.");

        // Expire all objects in cache
        Database db = jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.close();
        
        LOG.debug("Expired all objects from cache.");
    }
    
    public void create() throws Exception {
        TimeStampableBase base = new TimeStampableBase(
                TimeStampableBase.DEFAULT_ID, TimeStampableBase.DEFAULT_NAME);
        TimeStampableExtended ext = new TimeStampableExtended(
                TimeStampableExtended.DEFAULT_ID, TimeStampableExtended.DEFAULT_NAME,
                TimeStampableExtended.DEFAULT_NOTE);
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        // Timestamp should have been initialized to 0 by constructor
        long beginBaseTimestamp = base.jdoGetTimeStamp();
        LOG.debug("Timestamp of base after begin: " + beginBaseTimestamp);
        assertEquals(TimeStampableBase.DEFAULT_TIMESTAMP, beginBaseTimestamp);
        long beginExtTimestamp = ext.jdoGetTimeStamp();
        LOG.debug("Timestamp of extended after begin: " + beginExtTimestamp);
        assertEquals(TimeStampableExtended.DEFAULT_TIMESTAMP, beginExtTimestamp);
        
        db.create(base);
        db.create(ext);
        
        // Timestamp should have been changed at create.
        long createBaseTimestamp = base.jdoGetTimeStamp();
        LOG.debug("Timestamp of base after create: " + createBaseTimestamp);
        assertTrue(beginBaseTimestamp != createBaseTimestamp);
        long createExtTimestamp = ext.jdoGetTimeStamp();
        LOG.debug("Timestamp of extended after create: " + createExtTimestamp);
        assertTrue(beginExtTimestamp != createExtTimestamp);
        
        db.commit();
        
        // Timestamp should not been changed by commit.
        long commitBaseTimestamp = base.jdoGetTimeStamp();
        LOG.debug("Timestamp of base after commit: " + commitBaseTimestamp);
        assertEquals(createBaseTimestamp, commitBaseTimestamp);
        long commitExtTimestamp = ext.jdoGetTimeStamp();
        LOG.debug("Timestamp of extended after commit: " + commitExtTimestamp);
        assertEquals(createExtTimestamp, commitExtTimestamp);
        
        db.close();

        // Timestamp should not been changed by database close.
        long closeBaseTimestamp = base.jdoGetTimeStamp();
        LOG.debug("Timestamp of base after close: " + closeBaseTimestamp);
        assertEquals(commitBaseTimestamp, closeBaseTimestamp);
        long closeExtTimestamp = ext.jdoGetTimeStamp();
        LOG.debug("Timestamp of extended after close: " + closeExtTimestamp);
        assertEquals(commitExtTimestamp, closeExtTimestamp);
        
        // Properties should not been changed
        assertEquals(TimeStampableBase.DEFAULT_ID, base.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, base.getName());
        assertEquals(TimeStampableExtended.DEFAULT_ID, ext.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, ext.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, ext.getNote());
        
        // Remember timestamp for later compare.
        _baseTimestamp = commitBaseTimestamp;
        _extTimestamp = commitExtTimestamp;
    }
    
    public void loadBase() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableBase first = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, first.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, first.getName());
        
        LOG.debug("Timestamp of base after first load: " + first.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, first.jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        TimeStampableBase second = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, second.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, second.getName());
        
        LOG.debug("Timestamp of base after second load: " + second.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, second.jdoGetTimeStamp());
        
        // Remember timestamp for later compare.
        _baseTimestamp = second.jdoGetTimeStamp();
    }
    
    public void loadExtended() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableExtended first = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, first.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, first.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, first.getNote());
        
        LOG.debug("Timestamp of extended after first load: " + first.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, first.jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        TimeStampableExtended second = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, second.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, second.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, second.getNote());
        
        LOG.debug("Timestamp of extended after second load: " + second.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, second.jdoGetTimeStamp());
        
        // Remember timestamp for later compare.
        _extTimestamp = second.jdoGetTimeStamp();
    }
    
    public void queryBase() throws Exception {
        OQLQuery query;
        QueryResults results;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        db.begin();
        query = db.getOQLQuery("SELECT entity FROM "
                + TimeStampableBase.class.getName() + " entity WHERE id = $1");
        query.bind(TimeStampableBase.DEFAULT_ID);
        results = query.execute();
        assertTrue(results.hasMore());
        TimeStampableBase first = (TimeStampableBase) results.next();
        db.commit();

        assertEquals(TimeStampableBase.DEFAULT_ID, first.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, first.getName());
        
        LOG.debug("Timestamp of base after first query: " + first.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, first.jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        query = db.getOQLQuery("SELECT entity FROM "
                + TimeStampableBase.class.getName() + " entity WHERE id = $1");
        query.bind(TimeStampableBase.DEFAULT_ID);
        results = query.execute();
        assertTrue(results.hasMore());
        TimeStampableBase second = (TimeStampableBase) results.next();
        db.commit();
        db.close();

        assertEquals(TimeStampableBase.DEFAULT_ID, second.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, second.getName());
        
        LOG.debug("Timestamp of base after second query: " + second.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, second.jdoGetTimeStamp());
        
        // Remember timestamp for later compare.
        _baseTimestamp = second.jdoGetTimeStamp();
    }
    
    public void queryExtended() throws Exception {
        OQLQuery query;
        QueryResults results;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        db.begin();
        query = db.getOQLQuery("SELECT entity FROM "
                + TimeStampableExtended.class.getName() + " entity WHERE id = $1");
        query.bind(TimeStampableExtended.DEFAULT_ID);
        results = query.execute();
        assertTrue(results.hasMore());
        TimeStampableExtended first = (TimeStampableExtended) results.next();
        db.commit();

        assertEquals(TimeStampableExtended.DEFAULT_ID, first.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, first.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, first.getNote());
        
        LOG.debug("Timestamp of extended after first query: " + first.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, first.jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        query = db.getOQLQuery("SELECT entity FROM "
                + TimeStampableExtended.class.getName() + " entity WHERE id = $1");
        query.bind(TimeStampableExtended.DEFAULT_ID);
        results = query.execute();
        assertTrue(results.hasMore());
        TimeStampableExtended second = (TimeStampableExtended) results.next();
        db.commit();
        db.close();

        assertEquals(TimeStampableExtended.DEFAULT_ID, second.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, second.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, second.getNote());
        
        LOG.debug("Timestamp of extended after second query: " + second.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, second.jdoGetTimeStamp());
        
        // Remember timestamp for later compare.
        _extTimestamp = second.jdoGetTimeStamp();
    }
    
    public void updateBaseShort() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableBase entity = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);

        LOG.debug("Timestamp of base before short update: " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, entity.jdoGetTimeStamp());
        
        entity.setName(TimeStampableBase.ALTERNATE_NAME);
        
        db.commit();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableBase.ALTERNATE_NAME, entity.getName());
        
        LOG.debug("Timestamp of base after short update: " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_baseTimestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _baseTimestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableBase check = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableBase.ALTERNATE_NAME, check.getName());
        
        LOG.debug("Timestamp of base check after short update: " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, check.jdoGetTimeStamp());
    }

    public void updateExtendedShort() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableExtended entity = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);

        LOG.debug("Timestamp of extended before short update: " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, entity.jdoGetTimeStamp());
        
        entity.setName(TimeStampableExtended.ALTERNATE_NAME);
        entity.setNote(TimeStampableExtended.ALTERNATE_NOTE);
        
        db.commit();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableExtended.ALTERNATE_NAME, entity.getName());
        assertEquals(TimeStampableExtended.ALTERNATE_NOTE, entity.getNote());
        
        LOG.debug("Timestamp of extended after short update: " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_extTimestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _extTimestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableExtended check = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableExtended.ALTERNATE_NAME, check.getName());
        assertEquals(TimeStampableExtended.ALTERNATE_NOTE, check.getNote());
        
        LOG.debug("Timestamp of extended check after short update: " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, check.jdoGetTimeStamp());
    }

    public void updateBaseLongCached() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableBase entity = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp of base before long update (cached): " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, entity.jdoGetTimeStamp());
        
        entity.setName(TimeStampableBase.DEFAULT_NAME);

        db.begin();
        db.update(entity);
        db.commit();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, entity.getName());
        
        LOG.debug("Timestamp of base after long update (cached): " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_baseTimestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _baseTimestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableBase check = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableBase.DEFAULT_NAME, check.getName());
        
        LOG.debug("Timestamp of base check (cached): " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, check.jdoGetTimeStamp());
    }

    public void updateExtendedLongCached() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableExtended entity = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp of extended before long update (cached): "
                + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, entity.jdoGetTimeStamp());
        
        entity.setName(TimeStampableExtended.DEFAULT_NAME);
        entity.setNote(TimeStampableExtended.DEFAULT_NOTE);

        db.begin();
        db.update(entity);
        db.commit();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, entity.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, entity.getNote());
        
        LOG.debug("Timestamp of extended after long update (cached): "
                + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_extTimestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _extTimestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableExtended check = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableExtended.DEFAULT_NAME, check.getName());
        assertEquals(TimeStampableExtended.DEFAULT_NOTE, check.getNote());
        
        LOG.debug("Timestamp of extended check (cached): " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, check.jdoGetTimeStamp());
    }

    public void updateBaseLongExpired() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableBase entity = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp of base before long update (expired): " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, entity.jdoGetTimeStamp());
        
        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        entity.setName(TimeStampableBase.ALTERNATE_NAME);

        db.begin();
        db.update(entity);
        db.commit();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableBase.ALTERNATE_NAME, entity.getName());
        
        LOG.debug("Timestamp of base after long update (expired): " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_baseTimestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _baseTimestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableBase check = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableBase.ALTERNATE_NAME, check.getName());
        
        LOG.debug("Timestamp of base check (expired): " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, check.jdoGetTimeStamp());
    }

    public void updateExtendedLongExpired() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableExtended entity = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp of extended before long update (expired): "
                + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, entity.jdoGetTimeStamp());
        
        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        entity.setName(TimeStampableExtended.ALTERNATE_NAME);
        entity.setNote(TimeStampableExtended.ALTERNATE_NOTE);

        db.begin();
        db.update(entity);
        db.commit();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableExtended.ALTERNATE_NAME, entity.getName());
        assertEquals(TimeStampableExtended.ALTERNATE_NOTE, entity.getNote());
        
        LOG.debug("Timestamp of extended after long update (expired): "
                + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_extTimestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _extTimestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableExtended check = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableExtended.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableExtended.ALTERNATE_NAME, check.getName());
        assertEquals(TimeStampableExtended.ALTERNATE_NOTE, check.getNote());
        
        LOG.debug("Timestamp of extended check (expired): " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_extTimestamp, check.jdoGetTimeStamp());
    }

    public void remove() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableBase base = (TimeStampableBase) db.load(
                TimeStampableBase.class, TimeStampableBase.DEFAULT_ID);
        TimeStampableExtended ext = (TimeStampableExtended) db.load(
                TimeStampableExtended.class, TimeStampableExtended.DEFAULT_ID);

        // Compare current timestamp with the remembert one;
        assertEquals(_baseTimestamp, base.jdoGetTimeStamp());
        assertEquals(_extTimestamp, ext.jdoGetTimeStamp());
        
        LOG.debug("Timestamp of base before remove: " + base.jdoGetTimeStamp());
        LOG.debug("Timestamp of extended before remove: " + ext.jdoGetTimeStamp());
        
        db.remove(base);
        db.remove(ext);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableBase.DEFAULT_ID, base.getId());
        assertEquals(TimeStampableBase.ALTERNATE_NAME, base.getName());
        assertEquals(TimeStampableExtended.DEFAULT_ID, ext.getId());
        assertEquals(TimeStampableExtended.ALTERNATE_NAME, ext.getName());
        assertEquals(TimeStampableExtended.ALTERNATE_NOTE, ext.getNote());
        
        LOG.debug("Timestamp of base after remove: " + base.jdoGetTimeStamp());
        LOG.debug("Timestamp of extended after remove: " + ext.jdoGetTimeStamp());
    }
}

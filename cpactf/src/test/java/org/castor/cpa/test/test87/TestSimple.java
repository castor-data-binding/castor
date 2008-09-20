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

public final class TestSimple extends CPATestCase {
    private static final String DBNAME = "test87";
    private static final String MAPPING = "/org/castor/cpa/test/test87/mapping.xml";
    
    private static final Log LOG = LogFactory.getLog(TestSimple.class);
    
    private static long _timestamp;
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestSimple.class.getName());

        suite.addTest(new TestSimple("delete"));
        suite.addTest(new TestSimple("create"));
        suite.addTest(new TestSimple("load"));
        suite.addTest(new TestSimple("query"));
        suite.addTest(new TestSimple("updateShort"));
        suite.addTest(new TestSimple("updateLongCached"));
        suite.addTest(new TestSimple("updateLongExpired"));
        suite.addTest(new TestSimple("remove"));

        return suite;
    }

    public TestSimple(final String name) {
        super(name);
    }

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM test87_entity");
        conn.close();
        
        LOG.debug("Deleted all records from database.");

        // Expire all objects in cache
        Database db = jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.close();
        
        LOG.debug("Expired all objects from cache.");
    }
    
    public void create() throws Exception {
        TimeStampableEntity entity = new TimeStampableEntity(
                TimeStampableEntity.DEFAULT_ID, TimeStampableEntity.DEFAULT_NAME);
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        // Timestamp should have been initialized to 0 by constructor
        long beginTimestamp = entity.jdoGetTimeStamp();
        LOG.debug("Timestamp after begin: " + beginTimestamp);
        assertEquals(TimeStampableEntity.DEFAULT_TIMESTAMP, beginTimestamp);
        
        db.create(entity);
        
        // Timestamp should have been changed at create.
        long createTimestamp = entity.jdoGetTimeStamp();
        LOG.debug("Timestamp after create: " + createTimestamp);
        assertTrue(beginTimestamp != createTimestamp);
        
        db.commit();
        
        // Timestamp should not been changed by commit.
        long commitTimestamp = entity.jdoGetTimeStamp();
        LOG.debug("Timestamp after commit: " + commitTimestamp);
        assertEquals(createTimestamp, commitTimestamp);
        
        db.close();

        // Timestamp should not been changed by database close.
        long closeTimestamp = entity.jdoGetTimeStamp();
        LOG.debug("Timestamp after close: " + closeTimestamp);
        assertEquals(commitTimestamp, closeTimestamp);
        
        // Properties should not been changed
        assertEquals(TimeStampableEntity.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, entity.getName());
        
        // Remember timestamp for later compare.
        _timestamp = commitTimestamp;
    }
    
    public void load() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableEntity first = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, first.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, first.getName());
        
        LOG.debug("Timestamp after first load: " + first.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, first.jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        TimeStampableEntity second = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, second.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, second.getName());
        
        LOG.debug("Timestamp after second load: " + second.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, second.jdoGetTimeStamp());
        
        // Remember timestamp for later compare.
        _timestamp = second.jdoGetTimeStamp();
    }
    
    public void query() throws Exception {
        OQLQuery query;
        QueryResults results;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        db.begin();
        query = db.getOQLQuery("SELECT entity FROM "
                + TimeStampableEntity.class.getName() + " entity WHERE id = $1");
        query.bind(TimeStampableEntity.DEFAULT_ID);
        results = query.execute();
        assertTrue(results.hasMore());
        TimeStampableEntity first = (TimeStampableEntity) results.next();
        db.commit();

        assertEquals(TimeStampableEntity.DEFAULT_ID, first.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, first.getName());
        
        LOG.debug("Timestamp after first query: " + first.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, first.jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        query = db.getOQLQuery("SELECT entity FROM "
                + TimeStampableEntity.class.getName() + " entity WHERE id = $1");
        query.bind(TimeStampableEntity.DEFAULT_ID);
        results = query.execute();
        assertTrue(results.hasMore());
        TimeStampableEntity second = (TimeStampableEntity) results.next();
        db.commit();
        db.close();

        assertEquals(TimeStampableEntity.DEFAULT_ID, second.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, second.getName());
        
        LOG.debug("Timestamp after second query: " + second.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, second.jdoGetTimeStamp());
        
        // Remember timestamp for later compare.
        _timestamp = second.jdoGetTimeStamp();
    }
    
    public void updateShort() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableEntity entity = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);

        LOG.debug("Timestamp before short update: " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, entity.jdoGetTimeStamp());
        
        entity.setName(TimeStampableEntity.ALTERNATE_NAME);
        
        db.commit();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableEntity.ALTERNATE_NAME, entity.getName());
        
        LOG.debug("Timestamp after short update: " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_timestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _timestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableEntity check = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableEntity.ALTERNATE_NAME, check.getName());
        
        LOG.debug("Timestamp check after short update: " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, check.jdoGetTimeStamp());
    }

    public void updateLongCached() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableEntity entity = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp before long update (cached): " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, entity.jdoGetTimeStamp());
        
        entity.setName(TimeStampableEntity.DEFAULT_NAME);

        db.begin();
        db.update(entity);
        db.commit();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, entity.getName());
        
        LOG.debug("Timestamp after long update (cached): " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_timestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _timestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableEntity check = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableEntity.DEFAULT_NAME, check.getName());
        
        LOG.debug("Timestamp check after long update (cached): " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, check.jdoGetTimeStamp());
    }

    public void updateLongExpired() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableEntity entity = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp before long update (expired): " + entity.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, entity.jdoGetTimeStamp());
        
        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        entity.setName(TimeStampableEntity.ALTERNATE_NAME);

        db.begin();
        db.update(entity);
        db.commit();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableEntity.ALTERNATE_NAME, entity.getName());
        
        LOG.debug("Timestamp after long update (expired): " + entity.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_timestamp == entity.getTimeStamp());
      
        // Remember timestamp for later compare.
        _timestamp = entity.jdoGetTimeStamp();

        db.begin();
        TimeStampableEntity check = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, check.getId());
        assertEquals(TimeStampableEntity.ALTERNATE_NAME, check.getName());
        
        LOG.debug("Timestamp check after long update (expired): " + check.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, check.jdoGetTimeStamp());
    }

    public void remove() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableEntity entity = (TimeStampableEntity) db.load(
                TimeStampableEntity.class, TimeStampableEntity.DEFAULT_ID);

        // Compare current timestamp with the remembert one;
        assertEquals(_timestamp, entity.jdoGetTimeStamp());
        
        LOG.debug("Timestamp before remove: " + entity.jdoGetTimeStamp());
        
        db.remove(entity);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableEntity.DEFAULT_ID, entity.getId());
        assertEquals(TimeStampableEntity.ALTERNATE_NAME, entity.getName());
        
        LOG.debug("Timestamp after remove: " + entity.jdoGetTimeStamp());
    }
}

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

public final class TestRelation extends CPATestCase {
    private static final String DBNAME = "test87";
    private static final String MAPPING = "/org/castor/cpa/test/test87/mapping.xml";
    
    private static final Log LOG = LogFactory.getLog(TestRelation.class);
    
    private static long _productTimeStamp;
    private static long _groupTimeStamp;
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestRelation.class.getName());

        suite.addTest(new TestRelation("delete"));
        suite.addTest(new TestRelation("create"));
        suite.addTest(new TestRelation("load"));
        suite.addTest(new TestRelation("updateShort"));
        suite.addTest(new TestRelation("updateLongCached"));
        suite.addTest(new TestRelation("updateLongExpired"));
        suite.addTest(new TestRelation("remove"));

        return suite;
    }

    public TestRelation(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM test87_product");
        conn.createStatement().execute("DELETE FROM test87_group");
        conn.close();
        
        LOG.debug("Deleted all records from database.");

        // Expire all objects in cache
        Database db = jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.close();
        
        LOG.debug("Expired all objects from cache.");
    }
    
    public void create() throws Exception {
        TimeStampableGroup group = new TimeStampableGroup(
                TimeStampableGroup.DEFAULT_ID, TimeStampableGroup.DEFAULT_NAME);
        
        TimeStampableProduct product = new TimeStampableProduct(
                TimeStampableProduct.DEFAULT_ID, TimeStampableProduct.DEFAULT_NAME, group);
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        db.create(group);
        
        // Timestamp of group should have been changed at create of group.
        long groupTimestamp1 = group.jdoGetTimeStamp();
        LOG.debug("Timestamp of group after create: " + groupTimestamp1);
        assertTrue(TimeStampableGroup.DEFAULT_TIMESTAMP != groupTimestamp1);
        
        db.create(product);
        
        // Timestamp of product should have been changed at create of product.
        long productTimestamp1 = product.jdoGetTimeStamp();
        LOG.debug("Timestamp of product after create: " + productTimestamp1);
        assertTrue(TimeStampableProduct.DEFAULT_TIMESTAMP != productTimestamp1);
        
        // Timestamp of group related by product should not have been changed at create of product.
        long groupTimestamp2 = product.getGroup().jdoGetTimeStamp();
        LOG.debug("Timestamp of group refered by product after create: " + groupTimestamp2);
        assertEquals(groupTimestamp1, groupTimestamp2);
        
        // Timestamp of group should not have been changed at create of product.
        long groupTimestamp3 = group.jdoGetTimeStamp();
        LOG.debug("Timestamp of group after create of product: " + groupTimestamp3);
        assertEquals(groupTimestamp1, groupTimestamp3);
        
        db.commit();
        
        // Timestamp of product should not been changed by commit.
        long productTimestamp2 = product.jdoGetTimeStamp();
        LOG.debug("Timestamp of product after commit: " + productTimestamp2);
        assertEquals(productTimestamp1, productTimestamp2);
        
        // Timestamp of group related by product should not been changed by commit.
        long groupTimestamp4 = product.getGroup().jdoGetTimeStamp();
        LOG.debug("Timestamp of group refered by product after commit: " + groupTimestamp4);
        assertEquals(groupTimestamp1, groupTimestamp4);
        
        // Timestamp of group should not been changed by commit.
        long groupTimestamp5 = group.jdoGetTimeStamp();
        LOG.debug("Timestamp of group after commit: " + groupTimestamp5);
        assertEquals(groupTimestamp1, groupTimestamp5);
        
        db.close();

        // Remember timestamps for later compare.
        _productTimeStamp = productTimestamp1;
        _groupTimeStamp = groupTimestamp1;
    }
    
    public void load() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableGroup firstGroup = (TimeStampableGroup) db.load(
                TimeStampableGroup.class, TimeStampableGroup.DEFAULT_ID);
        TimeStampableProduct firstProduct = (TimeStampableProduct) db.load(
                TimeStampableProduct.class, TimeStampableProduct.DEFAULT_ID);
        db.commit();
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, firstGroup.getId());
        assertEquals(TimeStampableGroup.DEFAULT_NAME, firstGroup.getName());
        
        assertEquals(TimeStampableProduct.DEFAULT_ID, firstProduct.getId());
        assertEquals(TimeStampableProduct.DEFAULT_NAME, firstProduct.getName());
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, firstProduct.getGroup().getId());
        assertEquals(TimeStampableGroup.DEFAULT_NAME, firstProduct.getGroup().getName());
        
        LOG.debug("Timestamp of group after first load: " + firstGroup.jdoGetTimeStamp());
        LOG.debug("Timestamp of product after first load: " + firstProduct.jdoGetTimeStamp());
        LOG.debug("Timestamp of group refered by product after first load: "
                + firstProduct.getGroup().jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_groupTimeStamp, firstGroup.jdoGetTimeStamp());
        assertEquals(_productTimeStamp, firstProduct.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, firstProduct.getGroup().jdoGetTimeStamp());

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        TimeStampableGroup secondGroup = (TimeStampableGroup) db.load(
                TimeStampableGroup.class, TimeStampableGroup.DEFAULT_ID);
        db.commit();
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, secondGroup.getId());
        assertEquals(TimeStampableGroup.DEFAULT_NAME, secondGroup.getName());
        
        LOG.debug("Timestamp of group after second load: " + secondGroup.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_groupTimeStamp, secondGroup.jdoGetTimeStamp());
        
        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        LOG.debug("Expired all objects from cache.");

        db.begin();
        TimeStampableProduct secondProduct = (TimeStampableProduct) db.load(
                TimeStampableProduct.class, TimeStampableProduct.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableProduct.DEFAULT_ID, secondProduct.getId());
        assertEquals(TimeStampableProduct.DEFAULT_NAME, secondProduct.getName());
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, secondProduct.getGroup().getId());
        assertEquals(TimeStampableGroup.DEFAULT_NAME, secondProduct.getGroup().getName());
        
        LOG.debug("Timestamp of product after first load: " + secondProduct.jdoGetTimeStamp());
        LOG.debug("Timestamp of group refered by product after first load: "
                + secondProduct.getGroup().jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_productTimeStamp, secondProduct.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, secondProduct.getGroup().jdoGetTimeStamp());

        // Remember timestamp for later compare.
        _productTimeStamp = secondProduct.jdoGetTimeStamp();
        _groupTimeStamp = secondProduct.getGroup().jdoGetTimeStamp();
    }
    
    public void updateShort() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableGroup group = (TimeStampableGroup) db.load(
                TimeStampableGroup.class, TimeStampableGroup.DEFAULT_ID);

        LOG.debug("Timestamp before short update of group: " + group.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_groupTimeStamp, group.jdoGetTimeStamp());
        
        group.setName(TimeStampableGroup.ALTERNATE_NAME);
        
        db.commit();
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, group.getId());
        assertEquals(TimeStampableGroup.ALTERNATE_NAME, group.getName());
        
        LOG.debug("Timestamp after short update of group: " + group.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_groupTimeStamp == group.getTimeStamp());
      
        // Remember timestamp for later compare.
        _groupTimeStamp = group.jdoGetTimeStamp();

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        db.begin();
        TimeStampableProduct product = (TimeStampableProduct) db.load(
                TimeStampableProduct.class, TimeStampableProduct.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableProduct.DEFAULT_ID, product.getId());
        assertEquals(TimeStampableProduct.DEFAULT_NAME, product.getName());
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, product.getGroup().getId());
        assertEquals(TimeStampableGroup.ALTERNATE_NAME, product.getGroup().getName());
        
        LOG.debug("Timestamp check of product after short update of group: "
                + product.jdoGetTimeStamp());
        LOG.debug("Timestamp check of group after short update of group: "
                + product.getGroup().jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_productTimeStamp, product.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, product.getGroup().jdoGetTimeStamp());
    }

    public void updateLongCached() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableGroup group = (TimeStampableGroup) db.load(
                TimeStampableGroup.class, TimeStampableGroup.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp before long update of group (cached): " + group.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_groupTimeStamp, group.jdoGetTimeStamp());
        
        group.setName(TimeStampableGroup.DEFAULT_NAME);

        db.begin();
        db.update(group);
        db.commit();
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, group.getId());
        assertEquals(TimeStampableGroup.DEFAULT_NAME, group.getName());
        
        LOG.debug("Timestamp after long update of group (cached): " + group.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_groupTimeStamp == group.getTimeStamp());
      
        // Remember timestamp for later compare.
        _groupTimeStamp = group.jdoGetTimeStamp();

        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        db.begin();
        TimeStampableProduct product = (TimeStampableProduct) db.load(
                TimeStampableProduct.class, TimeStampableProduct.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableProduct.DEFAULT_ID, product.getId());
        assertEquals(TimeStampableProduct.DEFAULT_NAME, product.getName());
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, product.getGroup().getId());
        assertEquals(TimeStampableGroup.DEFAULT_NAME, product.getGroup().getName());
        
        LOG.debug("Timestamp check of product after short update of group: "
                + product.jdoGetTimeStamp());
        LOG.debug("Timestamp check of group after short update of group: "
                + product.getGroup().jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_productTimeStamp, product.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, product.getGroup().jdoGetTimeStamp());
    }

    public void updateLongExpired() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableGroup group = (TimeStampableGroup) db.load(
                TimeStampableGroup.class, TimeStampableGroup.DEFAULT_ID);
        db.commit();

        LOG.debug("Timestamp before long update of group (expired): " + group.jdoGetTimeStamp());
        
        // Compare current timestamp with the remembert one;
        assertEquals(_groupTimeStamp, group.jdoGetTimeStamp());
        
        // Expire all objects in cache
        db.getCacheManager().expireCache();
        
        group.setName(TimeStampableGroup.ALTERNATE_NAME);

        db.begin();
        db.update(group);
        db.commit();
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, group.getId());
        assertEquals(TimeStampableGroup.ALTERNATE_NAME, group.getName());
        
        LOG.debug("Timestamp after long update of group (expired): " + group.jdoGetTimeStamp());
        
        // Timestamp should have been changed at update.
        assertFalse(_groupTimeStamp == group.getTimeStamp());
      
        // Remember timestamp for later compare.
        _groupTimeStamp = group.jdoGetTimeStamp();

        db.begin();
        TimeStampableProduct product = (TimeStampableProduct) db.load(
                TimeStampableProduct.class, TimeStampableProduct.DEFAULT_ID);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableProduct.DEFAULT_ID, product.getId());
        assertEquals(TimeStampableProduct.DEFAULT_NAME, product.getName());
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, product.getGroup().getId());
        assertEquals(TimeStampableGroup.ALTERNATE_NAME, product.getGroup().getName());
        
        LOG.debug("Timestamp check of product after short update of group: "
                + product.jdoGetTimeStamp());
        LOG.debug("Timestamp check of group after short update of group: "
                + product.getGroup().jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_productTimeStamp, product.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, product.getGroup().jdoGetTimeStamp());
    }

    public void remove() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        TimeStampableProduct product = (TimeStampableProduct) db.load(
                TimeStampableProduct.class, TimeStampableProduct.DEFAULT_ID);
        TimeStampableGroup group = product.getGroup();
        
        // Compare current timestamp with the remembert one;
        assertEquals(_productTimeStamp, product.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, product.getGroup().jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, group.jdoGetTimeStamp());
        
        LOG.debug("Timestamp of product before remove: " + product.jdoGetTimeStamp());
        LOG.debug("Timestamp of group before remove: " + group.jdoGetTimeStamp());
        
        db.remove(product);
        db.remove(group);
        db.commit();
        db.close();
        
        assertEquals(TimeStampableProduct.DEFAULT_ID, product.getId());
        assertEquals(TimeStampableProduct.DEFAULT_NAME, product.getName());
        
        assertEquals(TimeStampableGroup.DEFAULT_ID, group.getId());
        assertEquals(TimeStampableGroup.ALTERNATE_NAME, group.getName());
        
        LOG.debug("Timestamp of product after remove: " + product.jdoGetTimeStamp());
        LOG.debug("Timestamp of group after remove: " + group.jdoGetTimeStamp());

        // Compare current timestamp with the remembert one;
        assertEquals(_productTimeStamp, product.jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, product.getGroup().jdoGetTimeStamp());
        assertEquals(_groupTimeStamp, group.jdoGetTimeStamp());
    }
}

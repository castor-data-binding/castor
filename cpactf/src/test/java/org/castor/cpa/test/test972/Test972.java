package org.castor.cpa.test.test972;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

public final class Test972 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test972.class);
    
    private static final String DBNAME = "test972";
    private static final String MAPPING = "/org/castor/cpa/test/test972/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test972.class.getName());
        
        suite.addTest(new Test972("testLongTransaction"));
        suite.addTest(new Test972("testRollback"));

        return suite;
    }
    
    public Test972(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void testLongTransaction() throws Exception {
        Product       product = null;
        ProductGroup  group;
        QueryResults  results;

        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        db.begin();
        LOG.info("Begin transaction to remove Product objects");

        // Look up the products and if found delete them from the database
        OQLQuery productOql = db.getOQLQuery(
                "SELECT p FROM " + Product.class.getName() + " p WHERE p.id = $1");

        for (int i = 4; i < 10; ++i) {
            productOql.bind(i);
            results = productOql.execute();
            while (results.hasMore())  {
                product = (Product) results.next();
                LOG.debug("Deleting existing product: " + product);
                db.remove(product);
            }
        }

        LOG.info("End transaction to remove Product objects");
        db.commit();

        db.begin();
        LOG.info("Begin transaction: one-to-one, one-to-many, and dependent relations");
        
        // If no such group exists in the database, create a new object and persist it
        OQLQuery groupOql = db.getOQLQuery(
                "SELECT g FROM " + ProductGroup.class.getName() + " g WHERE id = $1");
        groupOql.bind(3);
        results = groupOql.execute();
        if (!results.hasMore()) {
            group = new ProductGroup();
            group.setId(3);
            group.setName("a group");
            db.create(group);
            LOG.debug("Creating new group: " + group);
        } else {
            group = (ProductGroup) results.next();
            LOG.debug("Query result: " + group);
        }

        // If no such product exists in the database, create a new object and persist it
        // Note: product uses group, so group object has to be created first, but can
        //       be persisted later
        productOql.bind(4);
        results = productOql.execute();
        if (!results.hasMore()) {
            product = new Product();
            product.setId(4);
            product.setName("product4");
            product.setPrice(200);
            product.setGroup(group);
            LOG.debug("Creating new product: " + product);
            db.create(product);
        } else {
            LOG.debug("Query result: " + results.next());
        }

        LOG.info("End transaction: one-to-one, one-to-many and dependent relations");
        db.commit();

        db.begin();
        LOG.info("Begin transaction: one-to-one and dependent relations");

        group = (ProductGroup) db.load(ProductGroup.class, new Integer(3));
        
        // If no such products with ids 5-8 exist, create new objects and persist them
        for (int i = 5; i < 10; ++i) {
            int j = i + 1;
            productOql.bind(j);
            results = productOql.execute();
            if (!results.hasMore()) {
                product = new Product();
                product.setId(i);
                product.setName("product" + product.getId());
                product.setPrice(300);
                product.setGroup(group);
                LOG.debug("Creating new product: " + product);
                db.create(product);
            } else {
                LOG.debug("Query result: " + results.next());
            }
        }

        LOG.info("End transaction: one-to-one and dependent relations");
        db.commit();

        product.setPrice(333);
        LOG.info("Updated Product price: " + product);

        db.begin();
        LOG.info("Begin transaction: long transaction");
        // Don't forget to implement TimeStampable for the long transaction!!!
        db.update(product);
        LOG.info("End transaction: long transaction");
        db.commit();

        db.close();
        LOG.info("Test complete");
    }

    public void testRollback() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        LOG.info("Preparing for rollback bug");

        // Prepare the database: add a NewProduct, which references the same
        // ProductGroup as most Product objects do
        ProductGroup group = (ProductGroup) db.load(ProductGroup.class, new Integer(3));
        NewProduct newProduct = null;
        try {
            newProduct = (NewProduct) db.load(NewProduct.class, new Integer(1));
        } catch (ObjectNotFoundException e) {
            newProduct = new NewProduct();
            newProduct.setId(1);
            newProduct.setName("NewProduct1");
            newProduct.setPrice(1.0f);
            newProduct.setGroup(group);
            db.create(newProduct);
            LOG.info("NewProduct1 created.");
        }
        
        db.commit();
        
        // Now the data are prepared:
        // We have Product7 and NewProduct1, both are pointing to Group3
        // Below we will load both objects in on transaction and then call rollback
        db.begin();
        LOG.info("Trying to reproduce rollback bug");
        
        // loading both product entities
        newProduct = (NewProduct) db.load(NewProduct.class, new Integer(1));
        Product product = (Product) db.load(Product.class, new Integer(7));
        LOG.debug("Product loaded: " + product);
        LOG.debug("NewProduct loaded: " + newProduct);
        
        // we only loaded both objects in one tx. Now we are doing a rollback.
        LOG.info("Calling Rollback");
        db.rollback();
        
        LOG.info("End transaction: Trying to reproduce rollback bug");
        db.close();
    }
}

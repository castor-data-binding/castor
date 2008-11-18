package org.castor.cpa.test.test1379;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public class Test1379 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test1379.class);
    
    private static final String DBNAME = "test1379";
    private static final String MAPPING = "/org/castor/cpa/test/test1379/mapping.xml";

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.DERBY) 
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void test() throws Exception {
        Computer      computer = null;
        Product       product;
        QueryResults  results;

        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        removeProduct(db);
        removeComputer(db);
        createComputer(db);
        createProduct(db);

        db.begin();
        LOG.info("Begin transaction: one-to-one and dependent relations");

        OQLQuery productOql = db.getOQLQuery(
                "SELECT p FROM " + Product.class.getName() + " p WHERE p.id = $1");

        // If no such products with ids 5-8 exist, create new objects and persist them
        for (int i = 5; i < 10; ++i) {
            int j = i + 1;
            productOql.bind(j);
            results = productOql.execute();
            if (!results.hasMore()) {
                Computer computerToCreate = new Computer();
                computerToCreate.setId(i);
                computerToCreate.setCpu("cpu");
                computerToCreate.setName("computer" + computerToCreate.getId());
                computerToCreate.setPrice(300);
                LOG.debug("Creating new computer: " + computerToCreate);
                db.create(computerToCreate);
            } else {
                LOG.debug("Query result: " + results.next());
            }
        }

        LOG.info("End transaction: one-to-one and dependent relations");
        db.commit();

        db.begin();
        LOG.info("Begin transaction: load previously created products");

        product = (Product) db.load(Product.class, new Integer(4));
        assertNotNull (product);
        assertEquals (4, product.getId());
        // assertEquals (200.0f, product.getPrice());
        assertEquals ("computer4", product.getName());

        for (int x = 5; x < 10; x++) {
            product = (Product) db.load(Product.class, new Integer(x));
            assertNotNull (product);
            assertEquals (x, product.getId());
            assertEquals ("computer" + x, product.getName());
        }

        LOG.info("End transaction: load previously created products");
        db.commit();

        product.setPrice(333);
        LOG.info("Updated Product price: " + product);

        db.begin();
        LOG.info("Begin transaction: long transaction");
        // Don't forget to implement TimeStampable for the long transaction!!!
        db.update(product);
        LOG.info("End transaction: long transaction");
        db.commit();

        db.begin();
        LOG.info("Begin transaction: update extends relation in long transaction");

        OQLQuery computerOql = db.getOQLQuery(
                "SELECT c FROM " + Computer.class.getName() + " c WHERE c.id = $1");

        computerOql.bind(44);
        results = computerOql.execute();
        while (results.hasMore()) {
            computer = new Computer();
            computer = (Computer) results.next();
            LOG.debug("Found existing computer: " + computer);
        }

        LOG.info("End transaction: update extends relation in long transaction");
        db.commit();

        computer.setPrice(425);
        LOG.info("Updated Computer price: " + product);

        db.begin();
        LOG.info("Begin transaction: update extends relation in long transaction");
        // Don't forget to implement TimeStampable for the long transaction!!!
        db.update(computer);
        LOG.info("End transaction: update extends relation in long transaction");
        db.commit();

        db.begin();
        LOG.info("Begin transaction: load previously created non-leaf product");

        product = (Product) db.load(Product.class, new Integer(99));
        assertNotNull (product);
        assertEquals (99, product.getId());
        assertEquals("product99", product.getName());
        LOG.info("End transaction: load previously created non-leaf product");
        db.commit();
        
        db.close();
        LOG.info("Test complete");
    }

    private void removeProduct(final Database db) throws Exception {
        Product       product;
        QueryResults  results;

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
        
        productOql.bind(99);
        results = productOql.execute();
        while (results.hasMore())  {
            product = (Product) results.next();
            LOG.debug("Deleting existing product: " + product);
            db.remove(product);
        }

        LOG.info("End transaction to remove Product objects");
        db.commit();
    }
    
    private void removeComputer(final Database db) throws Exception {
        QueryResults  results;

        db.begin();
        LOG.info("Begin transaction to remove Computer object");
        
        // Look up the computer and if found delete them from the database
        OQLQuery computerOql = db.getOQLQuery(
                "SELECT c FROM " + Computer.class.getName() + " c WHERE c.id = $1");
        
        computerOql.bind(44);
        results = computerOql.execute();
        while (results.hasMore())  {
            Computer computerToDelete = (Computer) results.next();
            LOG.debug("Deleting existing computer: " + computerToDelete);
            db.remove(computerToDelete);
        }

        LOG.info("End transaction to remove Computer objects");
        db.commit();
    }
    
    private void createComputer(final Database db) throws Exception {
        QueryResults  results;

        db.begin();
        LOG.info("Begin transaction: one-to-one, one-to-many, and dependent relations");
        
        // If no such product exists in the database, create a new object and persist it
        // Note: product uses group, so group object has to be created first, but can
        //       be persisted later
        OQLQuery productOql = db.getOQLQuery(
                "SELECT p FROM " + Product.class.getName() + " p WHERE p.id = $1");

        productOql.bind(4);
        results = productOql.execute();
        if (!results.hasMore()) {
            Computer computerToCreate = new Computer();
            computerToCreate.setId(4);
            computerToCreate.setCpu("pentium");
            computerToCreate.setName("computer4");
            computerToCreate.setPrice(200);
            LOG.debug("Creating new computer: " + computerToCreate);
            db.create(computerToCreate);
        } else {
            LOG.debug("Query result: " + results.next());
        }

        // If no such computer exists in the database, create a new object and persist it
        // Note: computer uses group, so group object has to be created first, but can
        //       be persisted later
        OQLQuery computerOql = db.getOQLQuery(
                "SELECT c FROM " + Computer.class.getName() + " c WHERE c.id = $1");

        computerOql.bind(44);
        results = computerOql.execute();
        if (!results.hasMore()) {
            Computer computerToCreate = new Computer();
            computerToCreate.setId(44);
            computerToCreate.setCpu("Pentium");
            computerToCreate.setName("MyPC");
            computerToCreate.setPrice(400);
            LOG.debug("Creating new computer: " + computerToCreate);
            db.create(computerToCreate);
        } else {
            LOG.debug("Query result: " + results.next());
        }
        
        LOG.info("End transaction: one-to-one, one-to-many and dependent relations");
        db.commit();
    }
    
    private void createProduct(final Database db) throws Exception {
        QueryResults  results;

        db.begin();
        LOG.info("Begin transaction: create non-leaf node");
        
        // If no such product exists in the database, create a new object and persist it
        // Note: product uses group, so group object has to be created first, but can
        //       be persisted later
        OQLQuery productOql = db.getOQLQuery(
                "SELECT p FROM " + Product.class.getName() + " p WHERE p.id = $1");

        productOql.bind(99);
        results = productOql.execute();
        if (!results.hasMore()) {
            Product productToCreate = new Product();
            productToCreate.setId(99);
            productToCreate.setName("product99");
            productToCreate.setPrice(900);
            LOG.debug("Creating new computer: " + productToCreate);
            db.create(productToCreate);
        } else {
            LOG.debug("Query result: " + results.next());
        }
        
        LOG.info("End transaction: create non-leaf node");
        db.commit();
    }
}

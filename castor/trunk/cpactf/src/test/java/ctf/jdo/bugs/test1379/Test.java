package ctf.jdo.bugs.test1379;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * This example is only intended to show how castor can be set up 
 * in a standalone environment. For detailed examples on the mapping file, 
 * database schemas, supported features and their expected behaviors, 
 * please consult the JDO test cases instead. The JDO test cases can be 
 * found in the full CVS snapshot and located under the directory of 
 * src/tests/jdo and src/tests/myapp.
 */
public class Test extends TestCase {
    
    private static Log _log = LogFactory.getLog(Test.class);
    
    public static final String JdoConfFile = "jdo-conf.xml";
    public static final String Usage = "Usage: example jdo";

    private JDOManager  _jdo;

    public void setUp() throws Exception {
        String jdoConf = getClass().getResource(JdoConfFile).toString();
        JDOManager.loadConfiguration(jdoConf);
        _jdo = JDOManager.createInstance("ADSBV");
        _jdo.getDatabase();
    }

    public void testMethod() throws Exception {
        Database      db;
        Computer computer = null;
        Product       product = null;
        OQLQuery      productOql;
        OQLQuery      computerOql;
        QueryResults  results;

        db = _jdo.getDatabase();

        db.begin();
        _log.info("Begin transaction to remove Product objects");

        // Look up the products and if found delete them from the database
        productOql = db.getOQLQuery(
                "SELECT p FROM " + Product.class.getName() + " p WHERE p.id = $1");

        for (int i = 4; i < 10; ++i) {
            productOql.bind(i);
            results = productOql.execute();
            while (results.hasMore())  {
                product = (Product) results.next();
                _log.debug("Deleting existing product: " + product);
                db.remove(product);
            }
        }
        
        productOql.bind(99);
        results = productOql.execute();
        while (results.hasMore())  {
            product = (Product) results.next();
            _log.debug("Deleting existing product: " + product);
            db.remove(product);
        }

        _log.info("End transaction to remove Product objects");
        db.commit();

        db.begin();
        _log.info("Begin transaction to remove Computer object");
        
        // Look up the computer and if found delete them from the database
        computerOql = db.getOQLQuery(
                "SELECT c FROM " + Computer.class.getName() + " c WHERE c.id = $1");
        computerOql.bind(44);
        results = computerOql.execute();
        while (results.hasMore())  {
            Computer computerToDelete = (Computer) results.next();
            _log.debug("Deleting existing computer: " + computerToDelete);
            db.remove(computerToDelete);
        }

        _log.info("End transaction to remove Computer objects");
        db.commit();

        db.begin();
        _log.info("Begin transaction: one-to-one, one-to-many, and dependent relations");
        
        // If no such product exists in the database, create a new object and persist it
        // Note: product uses group, so group object has to be created first, but can
        //       be persisted later
        productOql.bind(4);
        results = productOql.execute();
        if (!results.hasMore()) {
            Computer computerToCreate = new Computer();
            computerToCreate.setId(4);
            computerToCreate.setCpu("pentium");
            computerToCreate.setName("computer4");
            computerToCreate.setPrice(200);
            _log.debug("Creating new computer: " + computerToCreate);
            db.create(computerToCreate);
        } else {
            _log.debug("Query result: " + results.next());
        }

        // If no such computer exists in the database, create a new object and persist it
        // Note: computer uses group, so group object has to be created first, but can
        //       be persisted later
        computerOql.bind(44);
        results = computerOql.execute();
        if (!results.hasMore()) {
            Computer computerToCreate = new Computer();
            computerToCreate.setId(44);
            computerToCreate.setCpu("Pentium");
            computerToCreate.setName("MyPC");
            computerToCreate.setPrice(400);
            _log.debug("Creating new computer: " + computerToCreate);
            db.create(computerToCreate);
        } else {
            _log.debug("Query result: " + results.next());
        }
        
        _log.info("End transaction: one-to-one, one-to-many and dependent relations");
        db.commit();

        db.begin();
        _log.info("Begin transaction: create non-leaf node");
        
        // If no such product exists in the database, create a new object and persist it
        // Note: product uses group, so group object has to be created first, but can
        //       be persisted later
        productOql.bind(99);
        results = productOql.execute();
        if (!results.hasMore()) {
            Product productToCreate = new Product();
            productToCreate.setId(99);
            productToCreate.setName("product99");
            productToCreate.setPrice(900);
            _log.debug("Creating new computer: " + productToCreate);
            db.create(productToCreate);
        } else {
            _log.debug("Query result: " + results.next());
        }
        
        db.commit();
        
        _log.info("End transaction: create non-leaf node");
        

        db.begin();
        _log.info("Begin transaction: one-to-one and dependent relations");

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
                _log.debug("Creating new computer: " + computerToCreate);
                db.create(computerToCreate);
            } else {
                _log.debug("Query result: " + results.next());
            }
        }

        _log.info("End transaction: one-to-one and dependent relations");
        db.commit();

        db.begin();
        _log.info("Begin transaction: load previously created products");

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

        _log.info("End transaction: load previously created products");
        db.commit();

        product.setPrice(333);
        _log.info("Updated Product price: " + product);

        db.begin();
        _log.info("Begin transaction: long transaction");
        // Don't forget to implement TimeStampable for the long transaction!!!
        db.update(product);
        _log.info("End transaction: long transaction");
        db.commit();

        db.begin();
        _log.info("Begin transaction: update extends relation in long transaction");

        computerOql.bind(44);
        results = computerOql.execute();
        while (results.hasMore()) {
            computer = new Computer();
            computer = (Computer) results.next();
            _log.debug("Found existing computer: " + computer);
        }

        _log.info("End transaction: update extends relation in long transaction");
        db.commit();

        computer.setPrice(425);
        _log.info("Updated Computer price: " + product);

        db.begin();
        _log.info("Begin transaction: update extends relation in long transaction");
        // Don't forget to implement TimeStampable for the long transaction!!!
        db.update(computer);
        _log.info("End transaction: update extends relation in long transaction");
        db.commit();

        db.begin();
        _log.info("Begin transaction: load previously created non-leaf product");

        product = (Product) db.load(Product.class, new Integer(99));
        assertNotNull (product);
        assertEquals (99, product.getId());
        assertEquals("product99", product.getName());
        _log.info("End transaction: load previously created non-leaf product");
        db.commit();
        
        db.close();
        _log.info("Test complete");
    }
}
